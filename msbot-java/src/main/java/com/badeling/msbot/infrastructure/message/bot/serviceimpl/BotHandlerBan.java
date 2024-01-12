package com.badeling.msbot.infrastructure.message.bot.serviceimpl;

import com.badeling.msbot.common.Tuple2;
import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.infrastructure.cq.entity.CqMessageEntity;
import com.badeling.msbot.infrastructure.cq.service.CqMessageBuildService;
import com.badeling.msbot.infrastructure.cqhttp.api.entity.GroupBanRequest;
import com.badeling.msbot.infrastructure.cqhttp.api.service.GroupBanService;
import com.badeling.msbot.infrastructure.message.bot.service.BotHandler;
import com.badeling.msbot.infrastructure.rban.service.RandomBanService;
import com.badeling.msbot.infrastructure.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BotHandlerBan implements BotHandler {
    private static final Pattern pattern = Pattern.compile("^( *)big surprise");


    private final UserService userService;
    private final RandomBanService randomBanService;
    private final GroupBanService banService;
    private final CqMessageBuildService cqMessageBuildService;

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String help() {

        return "│   ├── big surprise\r\n" +
                "│   │   └── 狗管理\r\n";
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public Tuple2<CqMessageEntity, Boolean> handler(GroupMessagePostEntity request, Matcher m) {
        var entity = cqMessageBuildService.create();

        if (userService.aboveManager(request.getUserId())) {
            randomBanService.ban(request.getGroupId());
            entity.text("杀！");
        } else {
            GroupBanRequest b = new GroupBanRequest();
            b.setDuration(60);
            b.setGroup_id(request.getGroupId());
            b.setUser_id(request.getUserId());
            banService.ban(b);

            entity.text("好巧哦");
        }

        return Tuple2.of(entity, false);
    }
}
