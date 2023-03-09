package com.badeling.msbot.infrastructure.dao.entity;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
public class FlagListener {
    @Id
    Long qq;

    Byte in_valid;

    String msg;

    Long creator;
}
