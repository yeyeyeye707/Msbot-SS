package com.badeling.msbot.infrastructure.message.bot.serviceimpl;

import com.badeling.msbot.common.Tuple2;
import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.infrastructure.cq.entity.CqMessageEntity;
import com.badeling.msbot.infrastructure.cq.service.CqMessageBuildService;
import com.badeling.msbot.infrastructure.cqhttp.api.entity.GroupMsgList;
import com.badeling.msbot.infrastructure.cqhttp.api.service.GroupMsgService;
import com.badeling.msbot.infrastructure.dao.entity.Msg;
import com.badeling.msbot.infrastructure.dao.repository.MsgRepository;
import com.badeling.msbot.infrastructure.message.bot.service.BotHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
public class BotHandlerMsgDefault implements BotHandler {
    @Autowired
    public BotHandlerMsgDefault(
            MsgRepository msgRepository,
            GroupMsgService groupMsgService,
            CqMessageBuildService cqMessageBuildService
    ) {
        this.msgRepository = msgRepository;
        this.groupMsgService = groupMsgService;
        this.cqMessageBuildService = cqMessageBuildService;
        this.UNKOWN_IMG = Stream.of(
                        "img/buzhidao1.gif",
                        "buzhidao2.gif",
                        "buzhidao3.png",
                        "buzhidao4.png",
                        "buzhidao5.png"
                ).map(img -> cqMessageBuildService.create().image(img))
                .toArray(CqMessageEntity[]::new);
        this.random = new Random();
    }

    final MsgRepository msgRepository;
    final GroupMsgService groupMsgService;
    final CqMessageBuildService cqMessageBuildService;

    private final CqMessageEntity[] UNKOWN_IMG;
    private final Random random;


    private static final Pattern pattern = Pattern.compile(".*");

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String help() {
        return "│   └── [学习的问题]\r\n";
    }

    @Override
    public int getOrder() {
        return 999;
    }

    @Override
    public Tuple2<CqMessageEntity, Boolean> handler(GroupMessagePostEntity request, Matcher m) {
        String msg = m.group();
        if (msg == null || msg.isEmpty()) {
            return randomUnknown();
        }

        Msg closest = msgRepository.findMsgLocateQuestion(msg, msg.length());

        if (closest == null) {
            return randomUnknown();
        }

        //回复
        var entity = cqMessageBuildService.create();
        entity.notAutoEscape()
                .text(closest.getAnswer());

        //有额外需要说的
        if (closest.getLink() != null) {
            //TODO..
            GroupMsgList msgList = new GroupMsgList();
            msgList.setGroup_id(new Long[]{request.getGroupId()});
            msgList.setAuto_escape(false);
            msgList.setMessage(closest.getLink().split("#abcde#"));
            groupMsgService.sendGroupMsgList(msgList);
        }

        return Tuple2.of(entity, false);
    }

    private Tuple2<CqMessageEntity, Boolean> randomUnknown() {
        int r = random.nextInt(UNKOWN_IMG.length);
        return Tuple2.of(UNKOWN_IMG[r], false);
    }
}
