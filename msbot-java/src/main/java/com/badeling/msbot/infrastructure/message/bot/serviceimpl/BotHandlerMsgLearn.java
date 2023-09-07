package com.badeling.msbot.infrastructure.message.bot.serviceimpl;

import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.infrastructure.cqhttp.event.service.MessageImageService;
import com.badeling.msbot.infrastructure.dao.entity.Msg;
import com.badeling.msbot.infrastructure.dao.repository.MsgRepository;
import com.badeling.msbot.infrastructure.message.bot.service.BotHandler;
import com.badeling.msbot.domain.message.group.entity.GroupMessageResult;
import com.badeling.msbot.infrastructure.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BotHandlerMsgLearn implements BotHandler {
    private static Pattern pattern = Pattern.compile("( {0,3})学习(.*)问(.+)答([\\s\\S]+)");

    @Autowired
    private UserService userService;

    @Autowired
    private MsgRepository msgRepository;

    @Autowired
    private MessageImageService messageImageService;

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String help() {
        return "│   ├── 学习问{问题}答{答案}[#abcde#][回答2]\r\n" +
                "│   │   ├── 以后机器人问题寻找答案\r\n" +
                "│   │   └── 可以通过#abcde#切割回答\r\n";
    }

    @Override
    public int getOrder() {
        return 5;
    }

    @Override
    public GroupMessageResult handler(GroupMessagePostEntity request, Matcher m) {
//        System.out.println("adadasdasdsad");
        GroupMessageResult result = new GroupMessageResult();
        result.setAt_sender(true);

        if (!userService.aboveManager(request.getUserId())) {
            result.setReply("宁是什么东西也配命令老娘？爬爬爬！");
            return result;
        }

        //处理问题和答案
        String question = m.group(m.groupCount() - 1);
        //去掉所有空白
        question = question.replaceAll("\\s*", "");
        String ans = m.group(m.groupCount());

        if ((question.contains("固定回复") || question.contains("随机回复"))
                && !userService.isManager(request.getUserId())) {
            result.setReply("需要超级管理员权限哦.");
            return result;
        }
        ans = messageImageService.saveImagesToLocal(ans);

        //保存
        Msg newMsg = new Msg();
        newMsg.setCreateId(String.valueOf(request.getUserId()));
        newMsg.setQuestion(question);
        if (ans.contains("#abcde#")) {
            System.out.println(ans);
            newMsg.setAnswer(ans.substring(0, ans.indexOf("#abcde#")));
            newMsg.setLink(ans.substring(ans.indexOf("#abcde#") + 7));
        } else {
            System.out.println(ans);
            newMsg.setAnswer(ans);
        }
        msgRepository.save(newMsg);

        result.setReply("[CQ:image,file=img/record.gif]");
        return result;
    }
}
