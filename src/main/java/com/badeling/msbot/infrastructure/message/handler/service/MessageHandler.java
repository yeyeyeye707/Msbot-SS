package com.badeling.msbot.infrastructure.message.handler.service;

import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.domain.message.group.entity.GroupMessageResult;


public interface MessageHandler {
    boolean canHandle(String msg);

    String help();

    GroupMessageResult handle(GroupMessagePostEntity message);
}
