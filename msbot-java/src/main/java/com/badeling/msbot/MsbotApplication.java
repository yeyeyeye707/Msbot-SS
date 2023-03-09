package com.badeling.msbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@EntityScan(basePackages = "com.badeling.msbot.infrastructure.dao.entity")
//@EnableJpaRepositories(basePackages = "com.badeling.msbot.infrastructure.dao.repository")
public class MsbotApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsbotApplication.class, args);
    }

}
