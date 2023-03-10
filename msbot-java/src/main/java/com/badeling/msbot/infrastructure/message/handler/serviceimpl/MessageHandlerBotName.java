package com.badeling.msbot.infrastructure.message.handler.serviceimpl;

import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.domain.message.group.entity.GroupMessageResult;
import com.badeling.msbot.infrastructure.config.ConstRepository;
import com.badeling.msbot.infrastructure.message.bot.service.BotHandler;
import com.badeling.msbot.infrastructure.message.handler.service.MessageHandler;
import com.badeling.msbot.infrastructure.config.MsbotConst;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;

@Service
@Order(2)
public class MessageHandlerBotName implements MessageHandler {

    private final BotHandler[] handlers;
    private final String name;
    private final String qq;

    public MessageHandlerBotName(BotHandler[] handlers, ConstRepository constRepository) {
        this.handlers = handlers;
        this.name = constRepository.getBotName();
        this.qq = "[CQ:at,qq=" + constRepository.getBotQQ() + "]";
    }

    @Override
    public boolean canHandle(String msg) {
        return msg.startsWith(name)
                || msg.startsWith(qq);
    }

    @Override
    public String help() {
        StringBuilder sb = new StringBuilder();
        sb.append("├── ").append(name).append(" [子命令]\r\n");
        for (BotHandler h : handlers) {
            sb.append(h.help());
        }
        return sb.toString();
    }

    @Override
    public GroupMessageResult handle(GroupMessagePostEntity message) {
        String msg = message.getRawMessage()
                .replace(name, "")
                .replace(qq, "");
        Matcher m;
        for (BotHandler h : handlers) {
            m = h.getPattern().matcher(msg);
            if (m.find()) {
                return h.handler(message, m);
            }
        }

        return null;
    }
}
