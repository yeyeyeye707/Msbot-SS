package com.badeling.msbot.infrastructure.cqhttp.event.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Data;
import lombok.experimental.NonFinal;

@Data
@NonFinal
public class BasePostEntity {
    @JsonProperty("time")
    Long time;


    @JsonProperty("self_id")
    Long selfId;

    @JsonProperty("post_type")
    String postType;
}
