package com.badeling.msbot.infrastructure.message.bot.serviceimpl;

import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.domain.message.group.entity.GroupMessageResult;
import com.badeling.msbot.infrastructure.cqhttp.event.service.MessageImageService;
import com.badeling.msbot.infrastructure.dao.entity.FlagListener;
import com.badeling.msbot.infrastructure.dao.repository.FlagListenerRepository;
import com.badeling.msbot.infrastructure.message.bot.service.BotHandler;
import com.badeling.msbot.infrastructure.repeat.service.RepeatService;
import com.badeling.msbot.infrastructure.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BotHandlerRepeatReport implements BotHandler {
    private final RepeatService repeatService;
    private Pattern pattern = Pattern.compile("^( {0,3})复读机周报");

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String help() {
        return "│   ├── 复读机周报\r\n";
    }

    @Override
    public GroupMessageResult handler(GroupMessagePostEntity request, Matcher m) {
        var report = repeatService.getRepeatReport(request.getGroupId());

        var result = new GroupMessageResult();
        result.setReply(report);
        return result;
    }

    @Override
    public int getOrder() {
        return 13;
    }
}
