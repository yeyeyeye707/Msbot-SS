package com.badeling.msbot.infrastructure.dao.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
@Table(name = "repeat_time")
@IdClass(RepeatTime.ID.class)
public class RepeatTime {
    @Id
    private Long user_id;

    @Id
    private Long group_id;

    private int count;

    @Data
    public static class ID implements Serializable {

        private Long user_id;
        private Long group_id;
    }
}
