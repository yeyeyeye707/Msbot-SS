package com.badeling.msbot.domain.message.personal.entity;

import com.badeling.msbot.application.entity.MessageResult;
import lombok.Data;

@Data
public class PersonMessageResult extends MessageResult {
    String reply;

    boolean auto_escape;
}
