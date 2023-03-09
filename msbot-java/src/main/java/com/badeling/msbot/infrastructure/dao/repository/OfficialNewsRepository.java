package com.badeling.msbot.infrastructure.dao.repository;

import com.badeling.msbot.infrastructure.dao.entity.OfficialNews;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OfficialNewsRepository extends CrudRepository<OfficialNews, Integer> {
    @Query(value = "select title from official_news where title in ?1",nativeQuery=true)
    List<String> findByTitle(List<String> title);

    @Query(value = "select * from official_news order by id desc limit ?1",nativeQuery=true)
    List<OfficialNews> findOfficialNewsLimit(int limit);
}
