package com.badeling.msbot.infrastructure.message.bot.serviceimpl;

import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.domain.message.group.entity.GroupMessageResult;
import com.badeling.msbot.infrastructure.dao.entity.Msg;
import com.badeling.msbot.infrastructure.dao.repository.MsgRepository;
import com.badeling.msbot.infrastructure.message.bot.service.BotHandler;
import com.badeling.msbot.infrastructure.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BotHandlerMsgDel implements BotHandler {
    @Autowired
    private UserService userService;

    @Autowired
    private MsgRepository msgRepository;

    private static  Pattern pattern = Pattern.compile("( {0,3})删除问题( *)(\\d+)");

    @Override
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String help(){
        return "│   ├── 删除问题{问题id}\r\n" +
                "│   │   └── 管理员可以删除自己创建的问题\r\n";
    }

    @Override
    public int getOrder(){
        return 4;
    }

    @Override
    public GroupMessageResult handler(GroupMessagePostEntity request, Matcher m) {
        GroupMessageResult result = new GroupMessageResult();

        if(!userService.aboveManager(request.getUserId())){
            result.setReply("宁是什么东西也配命令老娘？爬爬爬！");
            result.setAt_sender(true);
            return result;
        }
        result.setAuto_escape(true);

        String id = m.group(m.groupCount());
        Msg findQuestion = msgRepository.findQuestionById(id);
        if(findQuestion == null){
            result.setReply("指定问题不存在#" + id);
            return result;
        }

        if(!userService.isManager(request.getUserId())
        && !String.valueOf(request.getUserId()).equals(findQuestion.getCreateId())){
            result.setReply("只能删除自己创建的问题喔,创建qq:"+findQuestion.getCreateId());
            return result;
        }

        msgRepository.delete(findQuestion);
        result.setReply("删除成功 问题:"+findQuestion.getQuestion());
        return result;
    }
}
