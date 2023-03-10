package com.badeling.msbot.interfaces.facade;

import com.badeling.msbot.infrastructure.dao.entity.RankInfo;
import com.badeling.msbot.infrastructure.dao.repository.RankInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("health")
public class HealthHttpController {

    @Autowired
    private RankInfoRepository rankInfoRepository;

    @GetMapping("rank")
    public List<RankInfo> rank(){
        return rankInfoRepository.findAll();
    }

    @GetMapping("date")
    public String date(){
        return new Date().toString();
    }
}
