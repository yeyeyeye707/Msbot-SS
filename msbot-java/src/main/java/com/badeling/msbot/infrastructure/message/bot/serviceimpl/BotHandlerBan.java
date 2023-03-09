package com.badeling.msbot.infrastructure.message.bot.serviceimpl;

import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.domain.message.group.entity.GroupMessageResult;
import com.badeling.msbot.infrastructure.cqhttp.api.entity.GroupBanRequest;
import com.badeling.msbot.infrastructure.cqhttp.api.service.GroupBanService;
import com.badeling.msbot.infrastructure.message.bot.service.BotHandler;
import com.badeling.msbot.infrastructure.rban.service.RandomBanService;
import com.badeling.msbot.infrastructure.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BotHandlerBan implements BotHandler {
    private static Pattern pattern = Pattern.compile("^( *)big surprise");

    @Autowired
    UserService userService;
    @Autowired
    RandomBanService randomBanService ;

    @Autowired
    GroupBanService banService;

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
    public int getOrder(){
        return 0;
    }

    @Override
    public GroupMessageResult handler(GroupMessagePostEntity request, Matcher m) {
        GroupMessageResult result = new GroupMessageResult();
        result.setAuto_escape(true);

        if(userService.aboveManager(request.getUserId())){
            randomBanService.ban(request.getGroupId());
            result.setReply("杀！");
        }else{
            GroupBanRequest b = new GroupBanRequest();
            b.setDuration(60);
            b.setGroup_id(request.getGroupId());
            b.setUser_id(request.getUserId());
            banService.ban(b);

            result.setReply("好巧哦");
        }

        return  result;
    }
}
