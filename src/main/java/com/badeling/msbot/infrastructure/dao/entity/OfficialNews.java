package com.badeling.msbot.infrastructure.dao.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
