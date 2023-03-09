package com.badeling.msbot.infrastructure.cqhttp.event.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.NonFinal;

@Data
@NonFinal
public class MessageSenderEntity {
    @JsonProperty("user_id")
    Long userId;

    @JsonProperty("nickname")
    String nickname;

    @JsonProperty("sex")
    String sex;

    @JsonProperty("age")
    Integer age;
}
