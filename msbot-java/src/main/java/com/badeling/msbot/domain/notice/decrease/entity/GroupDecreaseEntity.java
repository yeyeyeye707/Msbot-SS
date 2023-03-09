package com.badeling.msbot.domain.notice.decrease.entity;

import com.badeling.msbot.infrastructure.cqhttp.event.entity.NoticePostEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Data;


@Data
@EqualsAndHashCode(callSuper = true)
public class GroupDecreaseEntity extends NoticePostEntity {
    @JsonProperty("sub_type")
    String subType;

    @JsonProperty("group_id")
    long groupId;

    @JsonProperty("operator_id")
    long operatorId;

    @JsonProperty("user_id")
    long userId;
}
