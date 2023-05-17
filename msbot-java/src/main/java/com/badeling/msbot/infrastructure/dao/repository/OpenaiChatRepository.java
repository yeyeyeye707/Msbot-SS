package com.badeling.msbot.infrastructure.dao.repository;

import com.badeling.msbot.infrastructure.dao.entity.OpenaiChat;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface OpenaiChatRepository extends CrudRepository<OpenaiChat, Long> {
    @Query(value = "select * from openai_chat where id = ?1 and in_valid = 1", nativeQuery = true)
    Optional<OpenaiChat> findAny(Long id);

    @Query(value = "select * from openai_chat where qq = ?1 and in_valid = 1", nativeQuery = true)
    Optional<OpenaiChat> findByQQ(Long qq);

    @Modifying
    @Transactional
    @Query(value = "update openai_chat set chat = ?2 where id = ?1", nativeQuery = true)
    void updateChat(Long id, String chat);

    @Modifying
    @Transactional
    @Query(value = "update openai_chat set in_valid = 0 where id = ?1", nativeQuery = true)
    void disableChat(Long id);
}
