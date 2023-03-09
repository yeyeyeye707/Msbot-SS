package com.badeling.msbot.infrastructure.cqhttp.event.service;

import com.badeling.msbot.infrastructure.config.MsbotConst;
import com.badeling.msbot.infrastructure.cqhttp.api.entity.GetImageResult;
import com.badeling.msbot.infrastructure.cqhttp.api.service.GetImageService;
import com.badeling.msbot.infrastructure.util.ImgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MessageImageService {


    private static final String MSG_IMG_TYPE = "[CQ:image";
    private static final Pattern MSG_IMG_URL_PATTERN = Pattern.compile("url=(\\S+?)[\\]|\\,]");
    private static final Pattern MSG_IMG_FILE_PATTERN = Pattern.compile("file=(\\S+?)[\\]|\\,]");


    @Autowired
    GetImageService getImageService;

    public String saveImagesToLocal(String msg) {
        System.out.println(msg);

        StringBuilder sb = new StringBuilder();
        String imgData;
        while (msg.contains(MSG_IMG_TYPE)) {
            int start = msg.indexOf(MSG_IMG_TYPE);
            int end = msg.indexOf(']') + 1;
            sb.append(msg.substring(0, start));
            imgData = msg.substring(start, end);
//            System.out.println(imgData);

            //下载url.
            String url = null;
            Matcher m = MSG_IMG_URL_PATTERN.matcher(imgData);
            if (m.find()) {
                url = m.group(1);
            } else {
                System.out.println("no url. read file");
                m = MSG_IMG_FILE_PATTERN.matcher(imgData);
                if (m.find()) {
                    String fileName = m.group(1);
//                    System.out.println(fileName);

                    GetImageResult response = getImageService.getImage(fileName);
                    if ("ok".equals(response.getStatus()) && response.getData() != null) {
                        url = response.getData().getUrl();
                    } else {
                        System.err.println(response);
                    }
                }
            }

            if (url != null) {
//                System.out.println(url);
                try {
                    String imageName = UUID.randomUUID().toString().replaceAll("-", "") + ".jpg";
                    ImgUtil.download(url, MsbotConst.imageUrl + "save/", imageName);
                    sb.append("[CQ:image,file=save/");
                    sb.append(imageName);
                    sb.append("]");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            msg = msg.substring(end);
//            System.out.println(msg);
        }
        sb.append(msg);

        return sb.toString();
    }
}
