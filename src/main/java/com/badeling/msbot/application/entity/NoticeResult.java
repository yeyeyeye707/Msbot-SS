package com.badeling.msbot.application.entity;

import lombok.Data;

@Data
public class NoticeResult {

    /**
     * 要回复的内容
     */
    private String reply;


    /**
     * 消息内容是否作为纯文本发送（即不解析 CQ 码），只在 reply 字段是字符串时有效
     * 默认不转义
     */
    private boolean auto_escape;

    /**
     * 是否要在回复开头 at 发送者（自动添加），发送者是匿名用户时无效
     * 默认at发送者
     */
    private boolean at_sender;

    public String getJsonStr(){
        return null;
    }
}
