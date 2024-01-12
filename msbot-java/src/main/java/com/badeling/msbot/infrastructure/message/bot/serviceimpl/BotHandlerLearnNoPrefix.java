package com.badeling.msbot.infrastructure.message.bot.serviceimpl;

import com.badeling.msbot.common.Tuple2;
import com.badeling.msbot.domain.message.exception.IlleagleUserException;
import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.infrastructure.cq.entity.CqMessageEntity;
import com.badeling.msbot.infrastructure.cq.service.CqMessageBuildService;
import com.badeling.msbot.infrastructure.cqhttp.event.service.MessageImageService;
import com.badeling.msbot.infrastructure.dao.entity.MsgNoPrefix;
import com.badeling.msbot.infrastructure.dao.repository.MsgNoPrefixRepository;
import com.badeling.msbot.infrastructure.message.bot.service.BotHandler;
import com.badeling.msbot.infrastructure.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BotHandlerLearnNoPrefix implements BotHandler {
    private static final Pattern pattern = Pattern.compile("( {0,3})学习(.*)布尔问(.+)答([\\s\\S]+)");

    private final UserService userService;
    private final MessageImageService messageImageService;
    private final MsgNoPrefixRepository msgNoPrefixRepository;
    private final CqMessageBuildService cqMessageBuildService;

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String help() {
        return "│   ├── 学习布尔问{问题}答{答案}\r\n" +
                "│   │   ├── 只有超级管理员可以使用\r\n" +
                "│   │   └── 学习后,不需要机器人前缀就回答\r\n";
    }

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    public Tuple2<CqMessageEntity, Boolean> handler(GroupMessagePostEntity request, Matcher m) throws IlleagleUserException {
        userService.checkMaster(request.getUserId());


        //处理问题和答案
        String question = m.group(m.groupCount() - 1);
        String ans = m.group(m.groupCount());

        ans = messageImageService.saveImagesToLocal(ans);

        //保存
        MsgNoPrefix mnpf = new MsgNoPrefix();
        mnpf.setQuestion(question);
        mnpf.setAnswer(ans);
        msgNoPrefixRepository.save(mnpf);


        return Tuple2.of(cqMessageBuildService.create().image("img/record.gif"), false);
    }
}
