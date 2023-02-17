package com.badeling.msbot.infrastructure.message.bot.serviceimpl;

import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.domain.message.group.entity.GroupMessageResult;
import com.badeling.msbot.infrastructure.cqhttp.api.entity.GroupMsgList;
import com.badeling.msbot.infrastructure.cqhttp.api.service.GroupMsgService;
import com.badeling.msbot.infrastructure.dao.entity.OfficialNews;
import com.badeling.msbot.infrastructure.dao.entity.OfficialNewsListener;
import com.badeling.msbot.infrastructure.dao.repository.OfficialNewsListenerRepository;
import com.badeling.msbot.infrastructure.dao.repository.OfficialNewsRepository;
import com.badeling.msbot.infrastructure.message.bot.service.BotHandler;
import com.badeling.msbot.infrastructure.user.service.UserService;
import com.badeling.msbot.infrastructure.util.ImgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BotHandlerNews implements BotHandler {
    private static Pattern pattern = Pattern.compile("^( *)新闻(.*)");
    private static Pattern NUMBER_PATTERN = Pattern.compile("(\\d+)");

    @Autowired
    OfficialNewsRepository officialNewsRepository;

    @Autowired
    OfficialNewsListenerRepository officialNewsListenerRepository;

    @Autowired
    GroupMsgService groupMsgService;

    @Autowired
    UserService userService;


    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String help(){
        return "│   ├── 新闻[订阅/取消订阅/新闻数量]\r\n" +
                "│   │   ├── 官网新闻页面消息订阅/取消\r\n" +
                "│   │   └── 看看最近几条新闻,默认1\r\n";
    }

    @Override
    public int getOrder(){
        return 7;
    }

    @Override
    public GroupMessageResult handler(GroupMessagePostEntity request, Matcher m) {
        GroupMessageResult result = new GroupMessageResult();

        String cmd = m.group(2);

        if(cmd.contains("取消订阅")){
            if(!userService.aboveManager(request.getUserId())){
                result.setReply("需要管理员权限");
                return result;
            }
            officialNewsListenerRepository.disableListener(request.getGroupId());
            result.setReply("取消订阅");
            return result;
        }

        if(cmd.contains("订阅")){
            if(!userService.aboveManager(request.getUserId())){
                result.setReply("需要管理员权限");
                return result;
            }
            OfficialNewsListener current = officialNewsListenerRepository.getOfficialNewsListenerByQQ(request.getGroupId());
            if(current != null){
                if(current.getIn_valid() == 1){
                    result.setReply("已订阅");
                    return result;
                }
                current.setIn_valid((byte) 1);
                officialNewsListenerRepository.save(current);
            }else{
                current = new OfficialNewsListener();
                current.setIn_valid((byte) 1);
                current.setQq(request.getGroupId());
                current.setType((byte) 1);
                officialNewsListenerRepository.save(current);

            }
            result.setReply("订阅成功");
            return result;
        }


        Matcher _m = NUMBER_PATTERN.matcher(cmd);
        int limit;
        if(_m.find()){
            limit = Integer.parseInt(_m.group());
            limit = Math.max(Math.min(10, limit), 1);
        }else{
            limit = 1;
        }
        List<OfficialNews> news = officialNewsRepository.findOfficialNewsLimit(limit);
        if(news != null && !news.isEmpty()){
            List<String> replys = new ArrayList<>();
            for(OfficialNews e : news){
                StringBuilder sb = new StringBuilder();
                sb.append(e.getTitle()).append('#').append(e.getId()).append("\r\n");
                sb.append("[CQ:image,file=").append(e.getImg_path()).append("]\r\n");
                sb.append(e.getUrl());
                replys.add(sb.toString());
            }

            //群发消息
            GroupMsgList msgList = new GroupMsgList();
            msgList.setGroup_id(new Long[]{request.getGroupId()});
            msgList.setAuto_escape(false);
            msgList.setMessage(replys.stream().toArray(String[]::new));

            groupMsgService.sendGroupMsgList(msgList);
        }



        return null;
    }
}
