package com.badeling.msbot.domain.message.personal.entity;

import com.badeling.msbot.application.entity.MessageResult;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonMessageResult extends MessageResult {
    String reply;

    boolean auto_escape;
}
