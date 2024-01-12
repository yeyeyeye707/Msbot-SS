package com.badeling.msbot.infrastructure.message.bot.serviceimpl;

import com.badeling.msbot.common.Tuple2;
import com.badeling.msbot.domain.message.exception.IlleagleUserException;
import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.infrastructure.cq.entity.CqMessageEntity;
import com.badeling.msbot.infrastructure.cq.service.CqMessageBuildService;
import com.badeling.msbot.infrastructure.cqhttp.event.service.MessageImageService;
import com.badeling.msbot.infrastructure.dao.entity.FlagListener;
import com.badeling.msbot.infrastructure.dao.repository.FlagListenerRepository;
import com.badeling.msbot.infrastructure.message.bot.service.BotHandler;
import com.badeling.msbot.infrastructure.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BotHandlerFlagRaceRemind implements BotHandler {
    Pattern pattern = Pattern.compile("跑旗订阅([\\s\\S]+)");

    private final UserService userService;
    private final FlagListenerRepository flagListenerRepository;
    private final MessageImageService messageImageService;
    private final CqMessageBuildService cqMessageBuildService;

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String help() {
        return "│   ├── 跑旗订阅{取消|提醒内容}\r\n" +
                "│   │   ├── 周日跑旗订阅/取消\r\n";
    }

    @Override
    public Tuple2<CqMessageEntity, Boolean> handler(GroupMessagePostEntity request, Matcher m) throws IlleagleUserException {
        userService.checkManager(request.getUserId());


        var cq = cqMessageBuildService.create();

        String msg = m.group();
        if (msg.contains("取消")) {
            flagListenerRepository.disableListener(request.getGroupId());
            cq.text("已取消");
        } else {
            FlagListener listener = flagListenerRepository.getListener(request.getGroupId());
            if (listener == null) {
                listener = new FlagListener();
                listener.setQq(request.getGroupId());
            }
            String ans = messageImageService.saveImagesToLocal(msg);
            listener.setMsg(ans);
            listener.setCreator(request.getUserId());
            listener.setIn_valid((byte) 1);
            flagListenerRepository.save(listener);

            cq.notAutoEscape()
                    .text(ans);
        }

        return Tuple2.of(cq, false);
    }

    @Override
    public int getOrder() {
        return 12;
    }
}
