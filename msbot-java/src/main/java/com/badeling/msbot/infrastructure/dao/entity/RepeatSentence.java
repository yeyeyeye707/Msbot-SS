package com.badeling.msbot.infrastructure.dao.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
@Table(name = "repeat_sentence")
@IdClass(RepeatSentence.ID.class)
public class RepeatSentence {

    @Id
    private Long group_id;
    @Id
    private Long user_id;
    private Integer count;

    private String msg;

    @Data
    public static class ID implements Serializable {
        private Long group_id;

        private Long user_id;
    }
}
