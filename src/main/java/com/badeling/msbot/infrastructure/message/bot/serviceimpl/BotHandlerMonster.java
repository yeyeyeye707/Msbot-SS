package com.badeling.msbot.infrastructure.message.bot.serviceimpl;

import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.domain.message.group.entity.GroupMessageResult;
import com.badeling.msbot.infrastructure.message.bot.service.BotHandler;
import com.badeling.msbot.infrastructure.wz.service.WzXmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BotHandlerMonster implements BotHandler {
    private static  Pattern pattern = Pattern.compile("^( {0,3})怪物(.+)");
    private static Pattern NUMBER_PATTERN = Pattern.compile("^(\\d+)$");

    @Autowired
    private WzXmlService wzXmlService;

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String help(){
        return "│   ├── 怪物{怪物名字/怪物id/更新}\r\n" +
                "│   │   └── 更新暂时不行.\r\n";
    }

    @Override
    public int getOrder(){
        return 3;
    }

    @Override
    public GroupMessageResult handler(GroupMessagePostEntity request, Matcher m) {
        GroupMessageResult result = new GroupMessageResult();
        String mob = m.group(2);
        if(mob.equals("更新")){
            result.setReply("没数据.");
            return result;
        }

        mob = mob.replace("怪物", "")
                .replace(" ", "")
                .replace("&#91;", "")
                .replace("&#93;", "");

        String reply;
        Matcher isId = NUMBER_PATTERN.matcher(mob);
        if(isId.find()){
            reply = wzXmlService.searchMobById(mob);
        } else {
            reply = wzXmlService.searchMobByName(mob);
        }

        result.setReply((reply == null || reply.isEmpty()) ? "没找到" :reply);

        return result;
    }
}
