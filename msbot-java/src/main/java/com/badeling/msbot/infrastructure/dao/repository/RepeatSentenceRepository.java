package com.badeling.msbot.infrastructure.dao.repository;

import com.badeling.msbot.infrastructure.dao.entity.RepeatSentence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RepeatSentenceRepository extends JpaRepository<RepeatSentence, RepeatSentence.ID> {

    @Query(value = "select * from repeat_sentence where group_id = ?1 order by count desc limit 1;", nativeQuery = true)
    RepeatSentence getFirstSentence(Long gid);

    @Query(value = "select * from repeat_sentence where group_id = ?1 and user_id = ?2 limit 1;", nativeQuery = true)
    RepeatSentence getSentence(Long gid, Long uid);

    @Modifying
    @Transactional
    @Query(value = "update repeat_sentence set count = 0;", nativeQuery = true)
    void clearAll();
}
