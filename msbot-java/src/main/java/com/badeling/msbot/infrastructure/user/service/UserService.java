package com.badeling.msbot.infrastructure.user.service;

import com.badeling.msbot.infrastructure.config.ConstRepository;
import com.badeling.msbot.infrastructure.config.MsbotConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private ConstRepository constRepository;

    public boolean aboveManager(long qq){
        return qq == constRepository.getMasterQQ()
                || constRepository.getManagerQQ().contains(qq);
    }

    public boolean isManager(long qq){
        return qq == constRepository.getMasterQQ();
    }
}
