package com.badeling.msbot.infrastructure.message.bot.serviceimpl;

import com.badeling.msbot.common.Tuple2;
import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.infrastructure.cq.entity.CqMessageEntity;
import com.badeling.msbot.infrastructure.cq.service.CqMessageBuildService;
import com.badeling.msbot.infrastructure.message.bot.service.BotHandler;
import com.badeling.msbot.infrastructure.wz.service.WzXmlService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BotHandlerMonster implements BotHandler {
    private static final Pattern pattern = Pattern.compile("^( {0,3})怪物(.+)");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^(\\d+)$");

    private final WzXmlService wzXmlService;
    private final CqMessageBuildService cqMessageBuildService;

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String help() {
        return "│   ├── 怪物{怪物名字/怪物id/更新}\r\n" +
                "│   │   └── 更新暂时不行.\r\n";
    }

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public Tuple2<CqMessageEntity, Boolean> handler(GroupMessagePostEntity request, Matcher m) {
        var cq = cqMessageBuildService.create();
        String mob = m.group(2);
        if (mob.equals("更新")) {
            return Tuple2.of(cq.text("没数据."), false);
        }

        mob = mob.replace("怪物", "")
                .replace(" ", "")
                .replace("&#91;", "")
                .replace("&#93;", "");

        String reply;
        Matcher isId = NUMBER_PATTERN.matcher(mob);
        if (isId.find()) {
            reply = wzXmlService.searchMobById(mob);
        } else {
            reply = wzXmlService.searchMobByName(mob);
        }

        return Tuple2.of(cq.text((reply == null || reply.isEmpty()) ? "没找到" : reply), false);
    }
}
