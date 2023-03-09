package com.badeling.msbot.infrastructure.message.bot.serviceimpl;

import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.domain.message.group.entity.GroupMessageResult;
import com.badeling.msbot.infrastructure.cqhttp.event.service.MessageImageService;
import com.badeling.msbot.infrastructure.dao.entity.FlagListener;
import com.badeling.msbot.infrastructure.dao.repository.FlagListenerRepository;
import com.badeling.msbot.infrastructure.message.bot.service.BotHandler;
import com.badeling.msbot.infrastructure.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BotHandlerFlagRaceRemind implements BotHandler {
    Pattern pattern = Pattern.compile("跑旗订阅([\\s\\S]+)");


    @Autowired
    UserService userService;

    @Autowired
    FlagListenerRepository flagListenerRepository;

    @Autowired
    private MessageImageService messageImageService;

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String help() {
        return "│   ├── 跑旗订阅{取消|提醒内容}\r\n" +
                "│   │   ├── 周日跑旗订阅/取消\r\n" ;
    }

    @Override
    public GroupMessageResult handler(GroupMessagePostEntity request, Matcher m) {
        GroupMessageResult result = new GroupMessageResult();

        if(!userService.aboveManager(request.getUserId())){
            result.setReply("需要管理员权限");
            return result;
        }

        String msg = m.group();
        if(msg.contains("取消")){
            flagListenerRepository.disableListener(request.getGroupId());
            result.setReply("已取消");
        } else {
            FlagListener listener = flagListenerRepository.getListener(request.getGroupId());
            if(listener == null){
                listener = new FlagListener();
                listener.setQq(request.getGroupId());
            }
            String ans = messageImageService.saveImagesToLocal(msg);
            listener.setMsg(ans);
            listener.setCreator(request.getUserId());
            listener.setIn_valid((byte) 1);
            flagListenerRepository.save(listener);

            result.setReply("已订阅:\r\n"+ans);
            result.setAuto_escape(false);
        }
        return result;
    }

    @Override
    public int getOrder() {
        return 12;
    }
}
