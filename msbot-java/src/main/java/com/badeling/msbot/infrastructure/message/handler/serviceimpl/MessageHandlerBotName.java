package com.badeling.msbot.infrastructure.message.handler.serviceimpl;

import com.badeling.msbot.common.Tuple2;
import com.badeling.msbot.domain.message.exception.IlleagleUserException;
import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.infrastructure.config.ConstRepository;
import com.badeling.msbot.infrastructure.cq.entity.CqMessageEntity;
import com.badeling.msbot.infrastructure.cq.service.CqMessageBuildService;
import com.badeling.msbot.infrastructure.message.bot.service.BotHandler;
import com.badeling.msbot.infrastructure.message.handler.service.MessageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;

@Service
@Order(2)
public class MessageHandlerBotName implements MessageHandler {

    private final BotHandler[] handlers;
    private final CqMessageBuildService cqMessageBuildService;
    private final String name;
    private final String qq;

    @Autowired
    public MessageHandlerBotName(BotHandler[] handlers, ConstRepository constRepository, CqMessageBuildService cqMessageBuildService) {
        this.handlers = handlers;
        this.cqMessageBuildService = cqMessageBuildService;

        this.name = constRepository.getBotName();

        var e = cqMessageBuildService.create()
                .atQQ(constRepository.getBotQQ());

        this.qq = e.getMessage();
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
    public Tuple2<CqMessageEntity, Boolean> handle(GroupMessagePostEntity message) {
        String msg = message.getRawMessage()
                .replace(name, "")
                .replace(qq, "");
        Matcher m;
        for (BotHandler h : handlers) {
            m = h.getPattern().matcher(msg);
            if (m.find()) {
                try {
                    return h.handler(message, m);
                } catch (IlleagleUserException ex) {
                    var entity = cqMessageBuildService.create();
                    return Tuple2.of(entity.text(ex.getMessage()), true);
                }
            }
        }

        return null;
    }
}
