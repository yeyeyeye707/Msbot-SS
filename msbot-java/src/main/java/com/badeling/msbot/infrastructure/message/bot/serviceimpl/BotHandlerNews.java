package com.badeling.msbot.infrastructure.message.bot.serviceimpl;

import com.badeling.msbot.common.Tuple2;
import com.badeling.msbot.domain.message.exception.IlleagleUserException;
import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.infrastructure.cq.entity.CqMessageEntity;
import com.badeling.msbot.infrastructure.cq.mapper.CqMessageMapper;
import com.badeling.msbot.infrastructure.cq.service.CqMessageBuildService;
import com.badeling.msbot.infrastructure.cqhttp.api.entity.GroupMsgList;
import com.badeling.msbot.infrastructure.cqhttp.api.service.GroupMsgService;
import com.badeling.msbot.infrastructure.dao.entity.OfficialNews;
import com.badeling.msbot.infrastructure.dao.entity.OfficialNewsListener;
import com.badeling.msbot.infrastructure.dao.repository.OfficialNewsListenerRepository;
import com.badeling.msbot.infrastructure.dao.repository.OfficialNewsRepository;
import com.badeling.msbot.infrastructure.message.bot.service.BotHandler;
import com.badeling.msbot.infrastructure.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BotHandlerNews implements BotHandler {
    private static final Pattern pattern = Pattern.compile("^( *)新闻(.*)");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("(\\d+)");

    final OfficialNewsRepository officialNewsRepository;
    final OfficialNewsListenerRepository officialNewsListenerRepository;
    final GroupMsgService groupMsgService;
    final UserService userService;
    final CqMessageBuildService cqMessageBuildService;
    final CqMessageMapper cqMessageMapper;


    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String help() {
        return "│   ├── 新闻[订阅/订阅取消/新闻数量]\r\n" +
                "│   │   ├── 官网新闻页面消息订阅/取消\r\n" +
                "│   │   └── 看看最近几条新闻,默认1\r\n";
    }

    @Override
    public int getOrder() {
        return 7;
    }

    @Override
    public Tuple2<CqMessageEntity, Boolean> handler(GroupMessagePostEntity request, Matcher m) throws IlleagleUserException {
        var entity = cqMessageBuildService.create();

        String cmd = m.group(2);

        if (cmd.contains("订阅取消")) {
            userService.checkManager(request.getUserId());

            officialNewsListenerRepository.disableListener(request.getGroupId());
            entity.text("取消订阅");

            return Tuple2.of(entity, false);
        }

        if (cmd.contains("订阅")) {
            userService.checkManager(request.getUserId());

            OfficialNewsListener current = officialNewsListenerRepository.getOfficialNewsListenerByQQ(request.getGroupId());
            if (current != null) {
                if (current.getIn_valid() == 1) {
                    entity.text("已订阅");
                } else {
                    current.setIn_valid((byte) 1);
                    officialNewsListenerRepository.save(current);
                    entity.text("订阅成功");
                }
            } else {
                current = new OfficialNewsListener();
                current.setIn_valid((byte) 1);
                current.setQq(request.getGroupId());
                current.setType((byte) 1);
                officialNewsListenerRepository.save(current);
                entity.text("订阅成功");
            }

            return Tuple2.of(entity, false);
        }


        Matcher _m = NUMBER_PATTERN.matcher(cmd);
        int limit;
        if (_m.find()) {
            limit = Integer.parseInt(_m.group());
            limit = Math.max(Math.min(10, limit), 1);
        } else {
            limit = 1;
        }
        List<OfficialNews> news = officialNewsRepository.findOfficialNewsLimit(limit);
        if (news != null && !news.isEmpty()) {
            var message = news.stream()
                    .map(e -> cqMessageBuildService.create()
                            .text(e.getTitle())
                            .text("#")
                            .text(String.valueOf(e.getId()))
                            .changeLine()
                            .image(e.getImg_path())
                            .changeLine()
                            .text(e.getUrl())
                            .getMessage()
                    );

            //群发消息
            GroupMsgList msgList = new GroupMsgList();
            msgList.setGroup_id(new Long[]{request.getGroupId()});
            msgList.setAuto_escape(false);
            msgList.setMessage(message.toArray(String[]::new));

            groupMsgService.sendGroupMsgList(msgList);
        }


        return null;
    }
}
