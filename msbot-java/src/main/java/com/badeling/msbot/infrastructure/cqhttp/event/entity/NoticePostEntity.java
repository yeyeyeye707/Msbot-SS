package com.badeling.msbot.infrastructure.cqhttp.event.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Data;
import lombok.experimental.NonFinal;

@Data
@NonFinal
@EqualsAndHashCode(callSuper=false)
public class NoticePostEntity extends BasePostEntity {
    @JsonProperty("notice_type")
    String noticeType;
}
