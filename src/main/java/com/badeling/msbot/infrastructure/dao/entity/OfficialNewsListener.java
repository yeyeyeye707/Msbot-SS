package com.badeling.msbot.infrastructure.dao.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class OfficialNewsListener {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    int id;

    Long qq;

    Byte type;

    Byte in_valid;
}
