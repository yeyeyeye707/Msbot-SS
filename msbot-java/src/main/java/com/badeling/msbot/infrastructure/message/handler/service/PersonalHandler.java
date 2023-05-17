package com.badeling.msbot.infrastructure.message.handler.service;

import com.badeling.msbot.domain.message.personal.entity.PersonMessageEntity;
import com.badeling.msbot.domain.message.personal.entity.PersonMessageResult;

public interface PersonalHandler {
    boolean canHandle(PersonMessageEntity request);

    PersonMessageResult handle(PersonMessageEntity request);
}
