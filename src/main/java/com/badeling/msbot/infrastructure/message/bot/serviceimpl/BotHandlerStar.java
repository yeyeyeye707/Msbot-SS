package com.badeling.msbot.infrastructure.message.bot.serviceimpl;

import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.domain.message.group.entity.GroupMessageResult;
import com.badeling.msbot.infrastructure.message.bot.service.BotHandler;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BotHandlerStar implements BotHandler {
    private static Pattern pattern = Pattern.compile("(\\d+)级(\\d+)星");
    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String help(){
        return "│   ├── {等级}级{数量}星\r\n" +
                "│   │   └── TODO..\n";
    }

    @Override
    public int getOrder(){
        return 9;
    }

    @Override
    public GroupMessageResult handler(GroupMessagePostEntity request, Matcher m) {
        return null;
    }
}
