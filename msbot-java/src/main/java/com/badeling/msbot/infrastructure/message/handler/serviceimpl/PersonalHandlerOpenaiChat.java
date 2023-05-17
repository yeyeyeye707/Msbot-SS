package com.badeling.msbot.infrastructure.message.handler.serviceimpl;

import com.badeling.msbot.domain.message.personal.entity.PersonMessageEntity;
import com.badeling.msbot.domain.message.personal.entity.PersonMessageResult;
import com.badeling.msbot.infrastructure.config.ConstRepository;
import com.badeling.msbot.infrastructure.message.handler.service.PersonalHandler;
import com.badeling.msbot.infrastructure.openai.service.OpenaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Service
@Order(999)
public class PersonalHandlerOpenaiChat implements PersonalHandler {

    @Autowired
    private OpenaiService openaiService;

    @Autowired
    private ConstRepository constRepository;

    @Override
    public boolean canHandle(PersonMessageEntity request) {
        return constRepository.getOpenaiUser().contains(request.getUserId())
                && openaiService.canChat(request.getUserId());
    }

    @Override
    public PersonMessageResult handle(PersonMessageEntity request) {
        openaiService.chat(request.getUserId(), request.getRawMessage());

//        String content = openaiService.chat(request.getUserId(), request.getRawMessage());
//        if (content != null) {
//            var result = new PersonMessageResult();
//            result.setReply(content);
//            return result;
//        }
        return null;
    }
}
