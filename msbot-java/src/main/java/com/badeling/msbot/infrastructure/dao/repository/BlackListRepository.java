package com.badeling.msbot.infrastructure.dao.repository;

import com.badeling.msbot.infrastructure.config.MsbotConst;
import org.springframework.stereotype.Repository;

@Repository
public class BlackListRepository {
    //TODO.. 黑名单放数据库.

    public boolean inBlackList(long userId) {
        for (long temp : MsbotConst.blackList) {
            if (userId == (temp)) {
                return true;
            }
        }
        return false;
    }
}
