package com.badeling.msbot.infrastructure.cqhttp.api.entity;

import lombok.Data;

@Data
public class PrivateMsg {
    private Long user_id;

    private String message;

    private boolean auto_escape = true;
}
