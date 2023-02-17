package com.badeling.msbot.infrastructure.official.service;

import com.badeling.msbot.infrastructure.cqhttp.api.entity.GroupMsgList;
import com.badeling.msbot.infrastructure.cqhttp.api.service.GroupMsgService;
import com.badeling.msbot.infrastructure.dao.entity.OfficialNews;
import com.badeling.msbot.infrastructure.dao.entity.OfficialNewsListener;
import com.badeling.msbot.infrastructure.dao.repository.OfficialNewsListenerRepository;
import com.badeling.msbot.infrastructure.dao.repository.OfficialNewsRepository;
import com.badeling.msbot.infrastructure.official.entity.NewsEntity;
import com.badeling.msbot.infrastructure.util.ImgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class OfficialNewScheduledComponent {
    private String url = "https://maplestory.nexon.net/news";
    private Pattern ulPattern = Pattern.compile("<ul class=\"news-container rows\"([\\s\\S]*?)</ul>");
    private Pattern liPattern = Pattern.compile("<li(.*?)</li>");

    private Pattern imgPattern = Pattern.compile("<div class=\\\"photo(.*?)url\\((.*?)\\)");
    private Pattern aPattern = Pattern.compile("<a href=\"(.*?)\">(.*?)</a>");

    @Autowired
    OfficialNewsRepository officialNewsRepository;

    @Autowired
    OfficialNewsListenerRepository officialNewsListenerRepository;

    @Autowired
    GroupMsgService groupMsgService;

    @Scheduled(cron ="0 */5 * * * *")
    public void check() {
//        System.out.println("asdasdasdasdasdasdas");
        List<NewsEntity> webEntities = getNewEntitiesFromWeb();

        if(webEntities.isEmpty()){
            return;
        }

        List<NewsEntity> needSend = filterNeedSend(webEntities);
        if(needSend == null || needSend.isEmpty()){
            return ;
        }

        List<OfficialNewsListener> listeners =  officialNewsListenerRepository.getOfficialNewsListeners();
        if(listeners.isEmpty()){
            return;
        }

        Long[] gids = listeners.stream()
                .map(OfficialNewsListener::getQq)
                .toArray(Long[]::new);

        if(gids == null || gids.length == 0){
            return;
        }

        //群发内容
        List<String> notices = new ArrayList<>();
        //完整保存
        List<OfficialNews> save = new ArrayList<>();

        for(NewsEntity e: needSend){
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("搞个大新闻!!!\r\n");
                sb.append(e.getTitle()).append("\r\n");
                if(e.getImgUrl() == null || e.getImgUrl().isEmpty()){

                }else {
                    String path = ImgUtil.saveTempImage(e.getImgUrl());
                    sb.append("[CQ:image,file=").append(path).append("]\r\n");
                    e.setImgPath(path);
                }
                sb.append(e.getUrl());
                notices.add(sb.toString());
                save.add(e.createDAO());
            }catch (Exception ig){

            }
        }

        //群发消息
        GroupMsgList msgList = new GroupMsgList();
        msgList.setGroup_id(gids);
        msgList.setAuto_escape(false);
        msgList.setMessage(notices.stream().toArray(String[]::new));

        groupMsgService.sendGroupMsgList(msgList);
        officialNewsRepository.saveAll(save);
    }


    //http 查看网页
    // 正则解析xml，找到新闻
    // 也可以用xpath
    private List<NewsEntity> getNewEntitiesFromWeb() {

        List<NewsEntity> webEntities = new ArrayList<>();

        //http 响应
        String content;
        try {
            URL _url = new URL(url);

            HttpURLConnection connection = (HttpURLConnection) _url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod( "GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
             content = buffer.toString();

        }catch (Exception ex){
            ex.printStackTrace();
            return webEntities;
        }


        //找到新闻ul
        Matcher ulMatcher = ulPattern.matcher(content);
        if(ulMatcher.find()){

            String ul = ulMatcher.group();
            Matcher liMatcher = liPattern.matcher(ul);
            //检查每个li
            while(liMatcher.find()){

                String newsUrl = null;
                String newsTitle = null;
                String newsImgUrl = null;

                String li = liMatcher.group();
                Matcher imgMatcher = imgPattern.matcher(li);
                if(imgMatcher.find()){
                    newsImgUrl = imgMatcher.group(2);
                }

                Matcher aMatcher = aPattern.matcher(li);

                while(aMatcher.find()){
                    if(aMatcher.group(2).startsWith("<img")||aMatcher.group(2).equals("Read More")){

                    }else{
                        newsUrl = "https://maplestory.nexon.net"+aMatcher.group(1);
                        newsTitle = (aMatcher.group(2));
                    }
                    li = li.substring(aMatcher.end());
                    aMatcher = aPattern.matcher(li);
                }

//                    System.out.println(newsTitle);
//                    System.out.println(newsImgUrl);
//                    System.out.println(newsUrl);
                NewsEntity e =new NewsEntity();
                e.setTitle(newsTitle);
                e.setImgUrl(newsImgUrl);
                e.setUrl(newsUrl);
                webEntities.add(e);

                ul = ul.substring(liMatcher.end());
                liMatcher = liPattern.matcher(ul);
            }
        }

        return webEntities;
    }


    //与数据库比较 找到需要发送的
    private List<NewsEntity> filterNeedSend(List<NewsEntity> allEntities){

        List<String> titles = allEntities.stream()
                .map(NewsEntity::getTitle)
                .collect(Collectors.toList());

        List<String> sent = officialNewsRepository.findByTitle(titles);
        if(sent.size() >= allEntities.size()){
            return null;
        }

        return allEntities.stream()
                .filter(e-> !sent.contains(e.getTitle()))
                .collect(Collectors.toList());
    }

}
