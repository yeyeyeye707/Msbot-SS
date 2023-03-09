package com.badeling.msbot.interfaces.facade;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("health")
public class HealthHttpController {

    @GetMapping
    public String health(){
        return new Date().toString();
    }
}
