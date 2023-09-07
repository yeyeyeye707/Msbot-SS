package com.badeling.msbot.domain.message.group.service;

import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.domain.message.group.entity.GroupMessageResult;
import com.badeling.msbot.infrastructure.config.ConstRepository;
import com.badeling.msbot.infrastructure.config.MsbotConst;
import com.badeling.msbot.infrastructure.message.handler.service.MessageHandler;
import com.badeling.msbot.infrastructure.dao.repository.BlackListRepository;
import com.badeling.msbot.infrastructure.message.handler.serviceimpl.MessageHandlerBotName;
import com.badeling.msbot.infrastructure.repeat.event.RepeatEventListener;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class GroupRequestService {
    private BlackListRepository blackListRepository;

    private MessageHandler[] messageHandlerArray;

    private ConstRepository constRepository;

    private RepeatEventListener repeatEventListener;


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

        GroupMessageResult result = null;

        if (msg.startsWith(constRepository.getBotName() + " -h")) {
            result = new GroupMessageResult();
            StringBuilder sb = new StringBuilder();
            sb.append("├── 命令 {参数} [可选参数]\r\n")
                    .append("│   └── 说明\r\n");
            for (MessageHandler handler : messageHandlerArray) {
                sb.append(handler.help());
            }
            result.setReply(sb.toString());
            return result;
        } else {
            for (MessageHandler handler : messageHandlerArray) {
                if (handler.canHandle(msg)) {
                    result = handler.handle(request);
                    break;
                }
            }
        }


        //未命中复读机监听
        if (result == null) {
            repeatEventListener.onReceiveGroupMsg(request);
        }
        return result;
    }
}
