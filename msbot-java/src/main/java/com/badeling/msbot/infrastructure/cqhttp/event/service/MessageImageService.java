package com.badeling.msbot.infrastructure.cqhttp.event.service;

import com.badeling.msbot.infrastructure.config.ConstRepository;
import com.badeling.msbot.infrastructure.cq.service.CqMessageBuildService;
import com.badeling.msbot.infrastructure.cqhttp.api.entity.GetImageResult;
import com.badeling.msbot.infrastructure.cqhttp.api.service.GetImageService;
import com.badeling.msbot.infrastructure.util.ImgUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageImageService {


    private static final String MSG_IMG_TYPE = "[CQ:image";
    private static final Pattern MSG_IMG_URL_PATTERN = Pattern.compile("url=(\\S+?)[\\]|\\,]");
    private static final Pattern MSG_IMG_FILE_PATTERN = Pattern.compile("file=(\\S+?)[\\]|\\,]");


    private final GetImageService getImageService;
    private final ConstRepository constRepository;
    private final ImgUtil imgUtil;
    private final CqMessageBuildService cqMessageBuildService;

    /**
     * 图片存本地,并且替换地址
     *
     * @param msg 收到的消息
     * @return 可以用来发送的消息
     */
    public String saveImagesToLocal(String msg) {
//        System.out.println(msg);

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
                    imgUtil.download(url, constRepository.getImageUrl() + "save/", imageName);
                    var e = cqMessageBuildService.create()
                            .image("save/" + imageName);
                    sb.append(e.getMessage());
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

    /**
     * 查看消息中是否存在图片
     *
     * @param msg 收到的消息
     * @return 图片名
     */
    public Optional<String> getImageName(String msg) {
        if (msg.contains(MSG_IMG_TYPE)) {
            var m = MSG_IMG_FILE_PATTERN.matcher(msg);
            if (m.find()) {
                var file = m.group(1);
                return Optional.of(file);
            }
        }

        return Optional.empty();
    }
}
