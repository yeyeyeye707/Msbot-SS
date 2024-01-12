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

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BotHandlerMsgQuery implements BotHandler {

    private final UserService userService;

    private final MsgRepository msgRepository;
    private final CqMessageBuildService cqMessageBuildService;

    private static final Pattern pattern = Pattern.compile("( {0,3})查询(.+)");

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String help() {
        return "│   ├── 查询{问题}\r\n" +
                "│   │   └── 需要管理员权限\n";
    }

    @Override
    public int getOrder() {
        return 6;
    }

    @Override
    public Tuple2<CqMessageEntity, Boolean> handler(GroupMessagePostEntity request, Matcher m) throws IlleagleUserException {
        var entity = cqMessageBuildService.create();

        userService.checkManager(request.getUserId(), "需要管理员喔，去掉查询可以直接寻找答案");

        String question = m.group(m.groupCount());
        Set<Msg> oldMsgList = msgRepository.findMsgLikeQuestion(question);
        if (oldMsgList == null || oldMsgList.isEmpty()) {
            entity.text("查询结果为空");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Msg msg : oldMsgList) {
                //TODO.. 语音
                sb.append("ID:").append(msg.getId());
                sb.append(" 问题：").append(msg.getQuestion());
                sb.append(" 回答：");
                if (msg.getAnswer().contains("[CQ:record")) {
                    sb.append(msg.getAnswer().replace("[CQ:record,file", "[voice"));
                    sb.append(msg.getLink());
                } else {
                    sb.append(msg.getAnswer());
                    if (msg.getLink() != null) {
                        sb.append("#abcde#").append(msg.getLink());
                    }
                }
                sb.append("\r\n");
            }
            entity.notAutoEscape().text(sb.toString());
        }

        return Tuple2.of(entity, false);
    }
}
