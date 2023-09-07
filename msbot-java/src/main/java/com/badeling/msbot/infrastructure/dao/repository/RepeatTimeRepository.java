package com.badeling.msbot.infrastructure.dao.repository;

import com.badeling.msbot.infrastructure.dao.entity.RepeatTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface RepeatTimeRepository extends JpaRepository<RepeatTime, RepeatTime.ID> {

    @Query(value = "select * from repeat_time where group_id = ?1 order by count desc limit 3;",nativeQuery=true)
    List<RepeatTime> find3thByGroup(Long group_id);

    @Modifying
    @Transactional
    @Query(value = "insert into repeat_time (`user_id`, `group_id`, `count`) " +
            "values (?1,?2,1) " +
            "on duplicate key update `count` = `count` + 1;", nativeQuery = true)
    void addOnce(Long user_id, Long group_id);


    /*
     * 清理 1.
     * drop table if exists tt;
     * create table tt like repeat_time;
     * rename table repeat_time to repeat_time_123;
     * rename table tt to repeat_time;
     */

    @Modifying
    @Transactional
    @Query(value = "update repeat_time set count = 0;", nativeQuery = true)
    void clearAll();
}
