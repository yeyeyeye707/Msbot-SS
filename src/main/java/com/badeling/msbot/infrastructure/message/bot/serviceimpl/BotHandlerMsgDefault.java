package com.badeling.msbot.infrastructure.message.bot.serviceimpl;

import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.domain.message.group.entity.GroupMessageResult;
import com.badeling.msbot.infrastructure.cqhttp.api.entity.GroupMsgList;
import com.badeling.msbot.infrastructure.cqhttp.api.service.GroupMsgService;
import com.badeling.msbot.infrastructure.dao.entity.Msg;
import com.badeling.msbot.infrastructure.dao.repository.MsgRepository;
import com.badeling.msbot.infrastructure.message.bot.service.BotHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BotHandlerMsgDefault implements BotHandler {
    private String[] UNKOWN_IMG = {
            "[CQ:image,file=img/buzhidao1.gif]",
            "[CQ:image,file=img/buzhidao2.gif]",
            "[CQ:image,file=img/buzhidao3.png]",
            "[CQ:image,file=img/buzhidao4.png]",
            "[CQ:image,file=img/buzhidao5.png]"
    };
    @Autowired
    MsgRepository msgRepository;

    @Autowired
    GroupMsgService groupMsgService;

    private final Random random = new Random();


    Pattern pattern = Pattern.compile(".*");

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String help() {
        return "│   └── [学习的问题]\r\n";
    }

    @Override
    public int getOrder(){
        return 999;
    }

    @Override
    public GroupMessageResult handler(GroupMessagePostEntity request, Matcher m) {
        String msg = m.group();
        if (msg == null || msg.isEmpty()) {
            return randomUnknown();
        }

        //让数据库做这个.
//        Set<Msg> msgs = msgRepository.findMsgLocateQuestion(msg, msg.length());
//        if (msgs == null || msgs.isEmpty()) {
//            return randomUnknown();
//        }
//
//        //找相互包含的 最接近的.
//        Iterator<Msg> i = msgs.iterator();
//        Msg m, closest = null;
//        int min = Integer.MAX_VALUE;
//        for (int dif, length = msg.length(); i.hasNext() && min > 0; ) {
//            m = i.next();
//            if (m != null && m.getQuestion() != null) {
//                dif = m.getQuestion().length() - length;
//                if (dif >= min) {
//                    continue;
//                }
//
//                min = dif;
//                closest = m;
//            }
//        }
        Msg closest = msgRepository.findMsgLocateQuestion(msg, msg.length());

        if (closest == null) {
            return randomUnknown();
        }

        //回复
        GroupMessageResult result = new GroupMessageResult();
        result.setReply(closest.getAnswer());

        //有额外需要说的
        if(closest.getLink() != null){
            GroupMsgList msgList = new GroupMsgList();
            msgList.setGroup_id(new Long[]{request.getGroupId()});
            msgList.setAuto_escape(false);
            msgList.setMessage(closest.getLink().split("#abcde#"));
            groupMsgService.sendGroupMsgList(msgList);
        }

        return result;
    }

    private GroupMessageResult randomUnknown() {
        GroupMessageResult result = new GroupMessageResult();

        int r = random.nextInt(UNKOWN_IMG.length);

        result.setReply(UNKOWN_IMG[r]);
        return result;
    }
}
