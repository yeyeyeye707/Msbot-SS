package com.badeling.msbot.infrastructure.dao.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class FlagListener {
    @Id
    Long qq;

    Byte in_valid;

    String msg;

    Long creator;
}
