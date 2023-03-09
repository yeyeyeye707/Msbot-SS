package com.badeling.msbot.domain.notice.increase.service;

import com.badeling.msbot.application.entity.NoticeResult;
import com.badeling.msbot.domain.notice.increase.entity.GroupIncreaseEntity;
import com.badeling.msbot.infrastructure.dao.entity.Msg;
import com.badeling.msbot.infrastructure.dao.repository.MsgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class GroupIncreaseService {
    @Autowired
    private MsgRepository msgRepository;

    public NoticeResult handleResult(GroupIncreaseEntity entity) {
        Random r = new Random();
        List<Msg> msgList = msgRepository.findMsgByExtQuestion("固定回复welcome");
        int random = r.nextInt(msgList.size());
        Msg msg = msgList.get(random);

        NoticeResult result = new NoticeResult();
        result.setReply(msg.getAnswer());
        result.setAt_sender(false);
        result.setAuto_escape(false);
        return result;
    }
}