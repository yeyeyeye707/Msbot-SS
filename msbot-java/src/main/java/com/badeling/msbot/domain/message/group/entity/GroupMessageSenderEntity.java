package com.badeling.msbot.domain.message.group.entity;

import com.badeling.msbot.infrastructure.cqhttp.event.entity.MessageSenderEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Data;

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupMessageSenderEntity extends MessageSenderEntity {
    @JsonProperty("card")
    String card;

    @JsonProperty("area")
    String area;

    @JsonProperty("level")
    String level;

    @JsonProperty("role")
    String role;

    @JsonProperty("title")
    String title;
}
