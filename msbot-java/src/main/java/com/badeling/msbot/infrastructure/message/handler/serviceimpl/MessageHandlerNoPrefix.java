package com.badeling.msbot.infrastructure.message.handler.serviceimpl;

import com.badeling.msbot.common.Tuple2;
import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import com.badeling.msbot.infrastructure.cq.entity.CqMessageEntity;
import com.badeling.msbot.infrastructure.cq.service.CqMessageBuildService;
import com.badeling.msbot.infrastructure.cqhttp.api.entity.GroupMsg;
import com.badeling.msbot.infrastructure.cqhttp.api.service.GroupMsgService;
import com.badeling.msbot.infrastructure.dao.repository.MsgNoPrefixRepository;
import com.badeling.msbot.infrastructure.message.handler.service.MessageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(999)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageHandlerNoPrefix implements MessageHandler {
    private final MsgNoPrefixRepository msgNoPrefixRepository;

    private final GroupMsgService groupMsgService;

    private final CqMessageBuildService cqMessageBuildService;


    @Override
    public String help() {
        return "└── [布尔问学习的问题]\r\n";
    }

    @Override
    public boolean canHandle(String msg) {
        return true;
    }

    public Tuple2<CqMessageEntity, Boolean> handle(GroupMessagePostEntity message) {

        var list = msgNoPrefixRepository.findMsgNP(message.getRawMessage());
        if (list == null || list.isEmpty()) {
            return null;
        }

        //找到的都回复?
        var m = list.get(0);
        if (m.isExact()) {
            GroupMsg groupMsg = new GroupMsg();
            groupMsg.setMessage(m.getAnswer());
            groupMsg.setGroup_id(message.getGroupId());
            groupMsgService.sendGroupMsg(groupMsg);
            System.err.println(groupMsg.toString());
        }

        var entity = cqMessageBuildService.create()
                .notAutoEscape()
                .text(m.getAnswer());
        return Tuple2.of(entity, false);
    }
}
