package com.badeling.msbot.infrastructure.message.bot.serviceimpl;

import com.badeling.msbot.common.Tuple2;
import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.infrastructure.cq.entity.CqMessageEntity;
import com.badeling.msbot.infrastructure.cq.service.CqMessageBuildService;
import com.badeling.msbot.infrastructure.cqhttp.api.entity.GroupMemberListResponseEntity;
import com.badeling.msbot.infrastructure.cqhttp.api.service.GroupMemberListService;
import com.badeling.msbot.infrastructure.message.bot.service.BotHandler;
import com.badeling.msbot.infrastructure.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BotHandlerAiTe implements BotHandler {
    private static final Pattern pattern = Pattern.compile("^( *)艾特");

    private final UserService userService;
    private final GroupMemberListService memberListService;
    private final CqMessageBuildService cqMessageBuildService;
    //随机ban一个
    private final Random random = new Random();

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String help() {

        return "│   ├── 艾特\r\n" +
                "│   │   └── 起床起床\r\n";
    }

    @Override
    public int getOrder() {
        return 10;
    }

    @Override
    public Tuple2<CqMessageEntity, Boolean> handler(GroupMessagePostEntity request, Matcher m) {
        var cq = cqMessageBuildService.create();

        if (userService.aboveManager(request.getUserId())) {

            List<GroupMemberListResponseEntity> all = memberListService.list(request.getGroupId());
            if (all == null || all.isEmpty()) {
                return null;
            }

            List<GroupMemberListResponseEntity> members = all.stream()
                    .filter(_m -> "member".equalsIgnoreCase(_m.getRole()))
                    .toList();
            if (members.isEmpty()) {
                return null;
            }


            int haha = random.nextInt(members.size());
            cq.atQQ(members.get(haha).getUser_id());
        } else {
            cq.atQQ(request.getUserId());
        }

        return Tuple2.of(cq, false);
    }
}
