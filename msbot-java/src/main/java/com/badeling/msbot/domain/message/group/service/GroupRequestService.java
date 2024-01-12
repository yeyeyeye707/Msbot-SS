package com.badeling.msbot.domain.message.group.service;

import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.domain.message.group.entity.GroupMessageResult;
import com.badeling.msbot.infrastructure.config.ConstRepository;
import com.badeling.msbot.infrastructure.cq.mapper.CqMessageMapper;
import com.badeling.msbot.infrastructure.cq.service.CqMessageBuildService;
import com.badeling.msbot.infrastructure.message.handler.service.MessageHandler;
import com.badeling.msbot.infrastructure.dao.repository.BlackListRepository;
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

    private CqMessageBuildService cqMessageBuildService;
    private CqMessageMapper cqMessageMapper;


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
            var e = cqMessageBuildService.create();
            e.text("├── 命令 {参数} [可选参数]")
                    .changeLine()
                    .text("│   └── 说明")
                    .changeLine();

            for (MessageHandler handler : messageHandlerArray) {
                e.text(handler.help());
            }
            return cqMessageMapper.toGroupMessageResult(e);
        } else {
            for (MessageHandler handler : messageHandlerArray) {
                if (handler.canHandle(msg)) {
                    var t2 = handler.handle(request);
                    result = cqMessageMapper.toGroupMessageResult(t2);
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
