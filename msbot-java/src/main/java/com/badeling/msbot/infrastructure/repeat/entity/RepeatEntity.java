package com.badeling.msbot.infrastructure.repeat.entity;

import com.badeling.msbot.domain.message.group.entity.GroupMessagePostEntity;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class RepeatEntity {

    GroupMessagePostEntity msg;

    Long firstUserId;
    Set<Long> repeatUserIds;
    boolean repeated;


    public RepeatEntity(
            GroupMessagePostEntity msg
    ) {
        this.repeatUserIds = new HashSet<>();
        this.reset(msg);
    }

    public void reset(
            GroupMessagePostEntity msg
    ) {
        this.msg = msg;
        this.firstUserId = msg.getUserId();
        this.repeatUserIds.clear();
        this.repeated = false;
    }

    public boolean onRepeat(Long userId) {
        return this.repeatUserIds.add(userId);
    }

    public boolean needRepeat() {
        if (!this.repeated && this.repeatUserIds.size() > 2) {
            this.repeated = true;
            return true;
        }
        return false;
    }
}
