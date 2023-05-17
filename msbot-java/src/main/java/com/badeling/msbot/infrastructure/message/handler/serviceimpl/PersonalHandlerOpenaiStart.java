package com.badeling.msbot.infrastructure.message.handler.serviceimpl;

import com.badeling.msbot.domain.message.personal.entity.PersonMessageEntity;
import com.badeling.msbot.domain.message.personal.entity.PersonMessageResult;
import com.badeling.msbot.infrastructure.config.ConstRepository;
import com.badeling.msbot.infrastructure.message.handler.service.PersonalHandler;
import com.badeling.msbot.infrastructure.openai.entity.ChatMessage;
import com.badeling.msbot.infrastructure.openai.service.OpenaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Order(1)
public class PersonalHandlerOpenaiStart implements PersonalHandler {
    @Autowired
    private OpenaiService openaiService;

    @Autowired
    private ConstRepository constRepository;

    @Override
    public boolean canHandle(PersonMessageEntity request) {
        return request.getRawMessage().startsWith("开始聊天");
    }

    @Override
    public PersonMessageResult handle(PersonMessageEntity request) {
        var result = new PersonMessageResult();

        boolean can = constRepository.getOpenaiUser().contains(request.getUserId());
        if(!can){
            result.setReply("你谁啊?");
            return result;
        }

        var start = openaiService.startChat(request.getUserId());
        if (start == null) {
            result.setReply("失败了.");
        } else {
            //前文.
            var msg = formatLastMessageList(start.getChat());
            result.setReply(msg);
        }

        return result;
    }

    private String formatLastMessageList(List<ChatMessage> messageList) {
        StringBuilder sb = new StringBuilder();
        sb.append("之前对话:\r\n\r\n");

        messageList
                .forEach(message -> {
                    sb.append(message.format()).append("\r\n\n");
                });

        return sb.toString();
    }
}
