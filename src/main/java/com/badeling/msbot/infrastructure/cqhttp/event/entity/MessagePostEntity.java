package com.badeling.msbot.infrastructure.cqhttp.event.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Data;
import lombok.experimental.NonFinal;

@Data
@NonFinal
@EqualsAndHashCode(callSuper = true)
public class MessagePostEntity<SENDER extends  MessageSenderEntity> extends BasePostEntity{
    @JsonProperty("message_type")
    String messageType;

    @JsonProperty("sub_type")
    String subType;

    @JsonProperty("message_id")
    Integer messageId;

    @JsonProperty("user_id")
    Long userId;

//    @JsonProperty("message")
//    MessageLinkEntity message;

    @JsonProperty("raw_message")
    String rawMessage;

    @JsonProperty("font")
    Integer font;

    @JsonProperty("sender")
    SENDER sender;
}
