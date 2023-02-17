package com.badeling.msbot.infrastructure.dao.repository;

import com.badeling.msbot.infrastructure.dao.entity.OfficialNewsListener;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OfficialNewsListenerRepository extends CrudRepository<OfficialNewsListener, Integer> {
    @Query(value = "select * from official_news_listener where in_valid = 1",nativeQuery=true)
    List<OfficialNewsListener> getOfficialNewsListeners();

    @Query(value = "select * from official_news_listener where qq = ?1", nativeQuery = true)
    OfficialNewsListener getOfficialNewsListenerByQQ(Long qq);

    @Query(value = "update official_news_listener set in_valid = 0 where qq = ?1", nativeQuery = true)
    void disableListener(Long qq);
}
