package com.badeling.msbot.domain.message.personal.entity;

import com.badeling.msbot.infrastructure.cqhttp.event.entity.MessageSenderEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Data;

@Data
@EqualsAndHashCode(callSuper = true)
public class PersonalMessageSenderEntity extends MessageSenderEntity {
    @JsonProperty("group_id")
    Long groupId;
}
