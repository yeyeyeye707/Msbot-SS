package com.badeling.msbot.infrastructure.dao.repository;

import com.badeling.msbot.infrastructure.config.MsbotConst;
import org.springframework.stereotype.Repository;

@Repository
public class BlackListRepository {
    public boolean inBlackList(long userId) {
        for (long temp : MsbotConst.blackList) {
            if (userId == (temp)) {
                return true;
            }
        }
        return false;
    }
}
