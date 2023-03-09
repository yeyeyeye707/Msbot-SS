package com.badeling.msbot.infrastructure.dao.entity;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
public class OfficialNews {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    int id;

    String title;

    String img_path;

    String url;
}
