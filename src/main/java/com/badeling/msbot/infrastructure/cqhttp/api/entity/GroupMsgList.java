package com.badeling.msbot.infrastructure.cqhttp.api.entity;

import lombok.Data;

/**
 * 非cqhttp定义api传递内容
 */
@Data
public class GroupMsgList {

    /**
     * 群号
     */
    private Long[] group_id;

    /**
     * 消息
     */
    private String[] message;

    /**
     * 消息内容是否作为纯文本发送（即不解析 CQ 码），只在 message 字段是字符串时有效
     */
    private boolean auto_escape;
}
