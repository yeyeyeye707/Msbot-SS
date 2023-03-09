package com.badeling.msbot.domain.message.group.entity;

import com.badeling.msbot.infrastructure.cqhttp.event.entity.MessagePostEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Data;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupMessagePostEntity extends MessagePostEntity<GroupMessageSenderEntity> {
    @JsonProperty("group_id")
    Long groupId;
}
