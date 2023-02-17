package com.badeling.msbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class MsbotApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsbotApplication.class, args);
    }
}
