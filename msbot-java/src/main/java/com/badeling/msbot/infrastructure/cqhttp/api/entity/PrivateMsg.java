package com.badeling.msbot.infrastructure.cqhttp.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrivateMsg {
    private Long user_id;

    private String message;

    private boolean auto_escape = true;
}
