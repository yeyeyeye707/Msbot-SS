package com.badeling.msbot.infrastructure.message.handler.serviceimpl;

import com.badeling.msbot.domain.message.personal.entity.PersonMessageEntity;
import com.badeling.msbot.domain.message.personal.entity.PersonMessageResult;
import com.badeling.msbot.infrastructure.message.handler.service.PersonalHandler;
import com.badeling.msbot.infrastructure.openai.service.OpenaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(2)
public class PersonalHandlerOpenaiEnd implements PersonalHandler {
    @Autowired
    private OpenaiService openaiService;

    @Override
    public boolean canHandle(PersonMessageEntity request) {
        return request.getRawMessage().startsWith("结束聊天");
    }

    @Override
    public PersonMessageResult handle(PersonMessageEntity request) {
        openaiService.endStart(request.getUserId());

        PersonMessageResult result = new PersonMessageResult();
        result.setReply("拜拜");
        return result;
    }
}
