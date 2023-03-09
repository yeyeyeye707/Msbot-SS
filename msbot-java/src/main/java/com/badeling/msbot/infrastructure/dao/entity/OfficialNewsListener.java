package com.badeling.msbot.infrastructure.dao.entity;

import lombok.Data;
import jakarta.persistence.*;

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
