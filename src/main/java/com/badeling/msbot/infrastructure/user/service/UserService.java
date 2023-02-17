package com.badeling.msbot.infrastructure.user.service;

import com.badeling.msbot.infrastructure.config.MsbotConst;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public boolean aboveManager(long qq){
        return qq == MsbotConst.masterId
                || MsbotConst.managerId.contains(qq);
    }

    public boolean isManager(long qq){
        return qq == MsbotConst.masterId;
    }
}
