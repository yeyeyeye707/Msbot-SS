package com.badeling.msbot.infrastructure.cqhttp.api.entity;

import lombok.Data;

@Data
public class GroupMemberListResponseEntity {
    Long group_id;
    Long user_id;

    String role;
}
