package com.badeling.msbot.infrastructure.message.handler.service;

import com.badeling.msbot.common.Tuple2;
import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.infrastructure.cq.entity.CqMessageEntity;
import jakarta.annotation.Nullable;


public interface MessageHandler {
    boolean canHandle(String msg);

    String help();

    /** 消息,at发送者 */
    @Nullable
    Tuple2<CqMessageEntity, Boolean> handle(GroupMessagePostEntity message);
}
