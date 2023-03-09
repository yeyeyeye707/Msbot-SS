package com.badeling.msbot.infrastructure.dao.repository;

import com.badeling.msbot.infrastructure.dao.entity.FlagListener;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FlagListenerRepository extends CrudRepository<FlagListener, String> {
    @Query(value = "select * from  flag_listener where in_valid = 1",nativeQuery=true)
    List<FlagListener> getListenersQQ();

    @Query(value = "select * from flag_listener where qq = ?1 limit 1",nativeQuery=true)
    FlagListener getListener(Long qq);
    @Modifying
    @Transactional

    @Query(value = "update flag_listener set in_valid = 0 where qq = ?1",nativeQuery=true)
    void disableListener(Long qq);
}
