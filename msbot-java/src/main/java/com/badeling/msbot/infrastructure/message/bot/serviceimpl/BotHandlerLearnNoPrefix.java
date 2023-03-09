package com.badeling.msbot.infrastructure.message.bot.serviceimpl;

import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.domain.message.group.entity.GroupMessageResult;
import com.badeling.msbot.infrastructure.cqhttp.event.service.MessageImageService;
import com.badeling.msbot.infrastructure.dao.entity.MsgNoPrefix;
import com.badeling.msbot.infrastructure.dao.repository.MsgNoPrefixRepository;
import com.badeling.msbot.infrastructure.message.bot.service.BotHandler;
import com.badeling.msbot.infrastructure.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BotHandlerLearnNoPrefix implements BotHandler {
    private static  Pattern pattern = Pattern.compile("( {0,3})学习(.*)布尔问(.+)答([\\s\\S]+)");

    @Autowired
    private UserService userService;

    @Autowired
    private MessageImageService messageImageService;

    @Autowired
    private MsgNoPrefixRepository msgNoPrefixRepository;

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String help(){
        return "│   ├── 学习布尔问{问题}答{答案}\r\n" +
                "│   │   ├── 只有超级管理员可以使用\r\n" +
                "│   │   └── 学习后,不需要机器人前缀就回答\r\n";
    }

    @Override
    public int getOrder(){
        return 2;
    }

    @Override
    public GroupMessageResult handler(GroupMessagePostEntity request, Matcher m) {
        GroupMessageResult result = new GroupMessageResult();
        result.setAt_sender(true);

        if(!userService.isManager(request.getUserId())){
            result.setReply("需要管理员权限哦");
            return result;
        }

        //处理问题和答案
        String question = m.group(m.groupCount() - 1);
        String ans = m.group(m.groupCount());

        ans = messageImageService.saveImagesToLocal(ans);

        //保存
        MsgNoPrefix mnpf = new MsgNoPrefix();
        mnpf.setQuestion(question);
        mnpf.setAnswer(ans);
        msgNoPrefixRepository.save(mnpf);


        result.setAt_sender(false);
        result.setReply("[CQ:image,file=img/record.gif]");
        return result;
    }
}
