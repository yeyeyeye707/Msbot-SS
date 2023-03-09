package com.badeling.msbot.infrastructure.dao.repository;

import com.badeling.msbot.infrastructure.dao.entity.LevelExp;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface LevelExpRepository extends CrudRepository<LevelExp, Long> {
    @Query(value = "select * from  ms_level_exp where lv = ?1",nativeQuery=true)
    LevelExp getLevelExpBy(int lv);
}