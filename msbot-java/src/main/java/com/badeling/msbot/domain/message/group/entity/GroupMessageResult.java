package com.badeling.msbot.domain.message.group.entity;

import com.badeling.msbot.application.entity.MessageResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
public class GroupMessageResult extends MessageResult {
    /**
     * 默认构造不at发送者的非纯文字
     */
    public GroupMessageResult(){
        at_sender = false;
        auto_escape = false;
    }
    String reply;

    boolean auto_escape;

    boolean at_sender;

    public void setAt_sender(boolean f){
        this.at_sender = f;
        if(f){
            this.auto_escape = false;
        }
    }
}
