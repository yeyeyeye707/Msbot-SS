package com.badeling.msbot.infrastructure.message.bot.serviceimpl;

import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.domain.message.group.entity.GroupMessageResult;
import com.badeling.msbot.infrastructure.cqhttp.api.entity.GroupMemberListResponseEntity;
import com.badeling.msbot.infrastructure.cqhttp.api.service.GroupMemberListService;
import com.badeling.msbot.infrastructure.message.bot.service.BotHandler;
import com.badeling.msbot.infrastructure.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BotHandlerAiTe implements BotHandler {
    private static final Pattern pattern = Pattern.compile("^( *)艾特");

    private static final String atFormat = "[CQ:at,qq=%d]";

    @Autowired
    UserService userService;
    //随机ban一个
    Random random = new Random();

    @Autowired
    GroupMemberListService memberListService;

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
    public int getOrder(){
        return 10;
    }

    @Override
    public GroupMessageResult handler(GroupMessagePostEntity request, Matcher m) {
        GroupMessageResult result = new GroupMessageResult();
//        result.setAuto_escape(false);

        if(userService.aboveManager(request.getUserId())){

            List<GroupMemberListResponseEntity> all = memberListService.list(request.getGroupId());
            if(all == null || all.isEmpty()){
                return result;
            }

            List<GroupMemberListResponseEntity> members = all.stream()
                    .filter(_m -> "member".equalsIgnoreCase(_m.getRole()) )
                    .toList();
            if(members.isEmpty()){
                return result;
            }


            int haha = random.nextInt(members.size());
            String msg = atFormat.formatted(members.get(haha).getUser_id());
            result.setReply(msg);
        }else{
            String msg = atFormat.formatted(request.getUserId());
            result.setReply(msg);
        }

        return  result;
    }
}
