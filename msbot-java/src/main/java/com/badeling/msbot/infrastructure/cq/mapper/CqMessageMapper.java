package com.badeling.msbot.infrastructure.cq.mapper;

import com.badeling.msbot.common.Tuple2;
import com.badeling.msbot.domain.message.group.entity.GroupMessageResult;
import com.badeling.msbot.domain.message.personal.entity.PersonMessageResult;
import com.badeling.msbot.infrastructure.cq.entity.CqMessageEntity;
import com.badeling.msbot.infrastructure.cqhttp.api.entity.GroupMsg;
import com.badeling.msbot.infrastructure.cqhttp.api.entity.PrivateMsg;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;

@Service
public class CqMessageMapper {

    @Nullable
    public PrivateMsg toPrivateMsg(@Nullable CqMessageEntity cq, Long userId) {
        return cq == null ? null : PrivateMsg.builder()
                .user_id(userId)
                .auto_escape(cq.isAutoEscape())
                .message(cq.getMessage())
                .build();
    }

    @Nullable
    public GroupMsg toGroupMsg(@Nullable CqMessageEntity cq, Long groupId) {
        return cq == null ? null : GroupMsg.builder()
                .group_id(groupId)
                .auto_escape(cq.isAutoEscape())
                .message(cq.getMessage())
                .build();
    }

    @Nullable
    public GroupMessageResult toGroupMessageResult(@Nullable CqMessageEntity cq) {
        return toGroupMessageResult(cq, false);
    }

    @Nullable
    public GroupMessageResult toGroupMessageResult(@Nullable Tuple2<CqMessageEntity, Boolean> tuple2) {
        if (tuple2 == null) {
            return null;
        }
        return toGroupMessageResult(tuple2.getT1(), tuple2.getT2());
    }

    @Nullable
    public GroupMessageResult toGroupMessageResult(@Nullable CqMessageEntity cq, @Nullable Boolean atSender) {
        var at = atSender != null && atSender;
        return cq == null ? null : GroupMessageResult.builder()
                .at_sender(at)
                .auto_escape(!at || cq.isAutoEscape())
                .reply(cq.getMessage())
                .build();
    }

    @Nullable
    public PersonMessageResult toPersonMessageResult(@Nullable CqMessageEntity cq) {
        return cq == null ? null : PersonMessageResult.builder()
                .auto_escape(cq.isAutoEscape())
                .reply(cq.getMessage())
                .build();
    }
}
