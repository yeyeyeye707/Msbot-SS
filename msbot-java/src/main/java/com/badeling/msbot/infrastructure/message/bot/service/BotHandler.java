package com.badeling.msbot.infrastructure.message.bot.service;

import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.domain.message.group.entity.GroupMessageResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface BotHandler extends org.springframework.core.Ordered{
    Pattern getPattern();

    String help();

    GroupMessageResult handler(GroupMessagePostEntity request, Matcher m);
}
