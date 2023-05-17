package com.badeling.msbot.domain.message.personal.service;

import com.badeling.msbot.domain.message.personal.entity.PersonMessageEntity;
import com.badeling.msbot.domain.message.personal.entity.PersonMessageResult;
import com.badeling.msbot.infrastructure.message.handler.service.PersonalHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonalMessageService {

    @Autowired
    List<PersonalHandler> handlersList;


    public PersonMessageResult handler(PersonMessageEntity request) {
        for (var handler : handlersList) {
            if (handler.canHandle(request)) {
                return handler.handle(request);
            }
        }

        return null;
    }
}
