package com.badeling.msbot.infrastructure.cqhttp.api.entity;

import lombok.Data;

@Data
public class GroupBanRequest {
    long group_id;

    long user_id;

    long duration;
}
