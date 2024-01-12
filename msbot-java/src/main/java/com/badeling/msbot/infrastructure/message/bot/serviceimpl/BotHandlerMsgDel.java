package com.badeling.msbot.infrastructure.message.bot.serviceimpl;

import com.badeling.msbot.common.Tuple2;
import com.badeling.msbot.domain.message.exception.IlleagleUserException;
import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.infrastructure.cq.entity.CqMessageEntity;
import com.badeling.msbot.infrastructure.cq.service.CqMessageBuildService;
import com.badeling.msbot.infrastructure.dao.entity.Msg;
import com.badeling.msbot.infrastructure.dao.repository.MsgRepository;
import com.badeling.msbot.infrastructure.message.bot.service.BotHandler;
import com.badeling.msbot.infrastructure.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BotHandlerMsgDel implements BotHandler {
    private final UserService userService;
    private final MsgRepository msgRepository;
    private final CqMessageBuildService cqMessageBuildService;

    private static final Pattern pattern = Pattern.compile("( {0,3})删除问题( *)(\\d+)");

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String help() {
        return "│   ├── 删除问题{问题id}\r\n" +
                "│   │   └── 管理员可以删除自己创建的问题\r\n";
    }

    @Override
    public int getOrder() {
        return 4;
    }

    @Override
    public Tuple2<CqMessageEntity, Boolean> handler(GroupMessagePostEntity request, Matcher m) throws IlleagleUserException {
        userService.checkManager(request.getUserId());

        var entity = cqMessageBuildService.create();

        String id = m.group(m.groupCount());
        Msg findQuestion = msgRepository.findQuestionById(id);
        if (findQuestion == null) {
            entity.text("指定问题不存在#" + id);
        } else {
            if (!String.valueOf(request.getUserId()).equals(findQuestion.getCreateId())) {
                userService.checkMaster(request.getUserId(), "只能删除自己创建的问题喔,创建qq:" + findQuestion.getCreateId());
            }
            msgRepository.delete(findQuestion);
            entity.text("删除成功 问题:" + findQuestion.getQuestion());
        }

        return Tuple2.of(entity, false);
    }
}
