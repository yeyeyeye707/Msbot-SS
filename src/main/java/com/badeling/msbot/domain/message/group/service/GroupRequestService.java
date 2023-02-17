package com.badeling.msbot.domain.message.group.service;

import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.domain.message.group.entity.GroupMessageResult;
import com.badeling.msbot.infrastructure.config.MsbotConst;
import com.badeling.msbot.infrastructure.message.handler.service.MessageHandler;
import com.badeling.msbot.infrastructure.dao.repository.BlackListRepository;
import com.badeling.msbot.infrastructure.message.handler.serviceimpl.MessageHandlerBotName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupRequestService {
    @Autowired
    private BlackListRepository blackListRepository;

    @Autowired
    private MessageHandler[] messageHandlerArray;

    @Autowired
    private MessageHandlerBotName temp;

    public GroupMessageResult handler(GroupMessagePostEntity request) {

        //黑名单拒绝访问
        if (blackListRepository.inBlackList(request.getUserId())) {
            return null;
        }

        //处理请求
        String msg = request.getRawMessage();
        if (msg == null || msg.isEmpty()) {
            return null;
        }

        if(msg.startsWith(MsbotConst.botName+" -h")){
            GroupMessageResult result = new GroupMessageResult();
            StringBuilder sb = new StringBuilder();
            sb.append("├── 命令 {参数} [可选参数]\r\n")
                    .append("│   └── 说明\r\n");
            for (MessageHandler handler : messageHandlerArray) {
                sb.append(handler.help());
            }
            result.setReply(sb.toString());
            return result;
        }

//        for (MessageHandler handler : messageHandlerArray) {
//            if (handler.canHandle(msg)) {
//                return handler.handle(request);
//            }
//        }
        if (temp.canHandle(msg)) {
            return temp.handle(request);
        }

        return null;
//        return messageHandlerNoPrefix.handle(request);
    }
}
