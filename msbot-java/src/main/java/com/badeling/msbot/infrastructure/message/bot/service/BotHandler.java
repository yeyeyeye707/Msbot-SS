package com.badeling.msbot.infrastructure.message.bot.service;

import com.badeling.msbot.common.Tuple2;
import com.badeling.msbot.domain.message.exception.IlleagleUserException;
import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.infrastructure.cq.entity.CqMessageEntity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface BotHandler extends org.springframework.core.Ordered {
    Pattern getPattern();

    String help();

    /** 消息, at发送人*/
    Tuple2<CqMessageEntity, Boolean> handler(GroupMessagePostEntity request, Matcher m) throws IlleagleUserException;
}
