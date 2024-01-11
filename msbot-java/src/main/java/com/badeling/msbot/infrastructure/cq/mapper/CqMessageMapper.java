package com.badeling.msbot.infrastructure.cq.mapper;

import com.badeling.msbot.domain.message.group.entity.GroupMessageResult;
import com.badeling.msbot.domain.message.personal.entity.PersonMessageResult;
import com.badeling.msbot.infrastructure.cq.entity.CqMessageEntity;
import com.badeling.msbot.infrastructure.cqhttp.api.entity.GroupMsg;
import com.badeling.msbot.infrastructure.cqhttp.api.entity.PrivateMsg;
import org.springframework.stereotype.Service;

@Service
public class CqMessageMapper {

    public PrivateMsg toPrivateMsg(Long userId, CqMessageEntity cq) {
        return PrivateMsg.builder()
                .user_id(userId)
                .auto_escape(cq.isAutoEscape())
                .message(cq.getMessage())
                .build();
    }

    public GroupMsg toGroupMsg(Long groupId, CqMessageEntity cq) {
        return GroupMsg.builder()
                .group_id(groupId)
                .auto_escape(cq.isAutoEscape())
                .message(cq.getMessage())
                .build();
    }

    public GroupMessageResult toGroupMessageResult(boolean atSender, CqMessageEntity cq) {
        return GroupMessageResult.builder()
                .at_sender(atSender)
                .auto_escape(cq.isAutoEscape())
                .reply(cq.getMessage())
                .build();
    }

    public PersonMessageResult toPersonMessageResult(CqMessageEntity cq) {
        return PersonMessageResult.builder()
                .auto_escape(cq.isAutoEscape())
                .reply(cq.getMessage())
                .build();
    }
}
