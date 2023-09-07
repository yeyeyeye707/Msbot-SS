package com.badeling.msbot.interfaces.facade;

import com.badeling.msbot.infrastructure.config.ConstRepository;
import com.badeling.msbot.infrastructure.dao.repository.MsgNoPrefixRepository;
import com.badeling.msbot.infrastructure.maplegg.repository.MapleGGApiRepository;
import com.badeling.msbot.infrastructure.maplegg.repository.RankInfoImgRepository;
import com.badeling.msbot.infrastructure.maplegg.service.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("health")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HealthHttpController {

    private final ConstRepository constRepository;

    @GetMapping("date")
    public String date() {
        return new Date().toString();
    }

    @GetMapping("test")
    public Object test() {
        return constRepository.getBotName();
    }
}
