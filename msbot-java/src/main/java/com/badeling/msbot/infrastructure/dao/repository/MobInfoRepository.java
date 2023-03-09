package com.badeling.msbot.infrastructure.dao.repository;

import com.badeling.msbot.infrastructure.dao.entity.MobInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface MobInfoRepository extends CrudRepository<MobInfo, Long>{
	@Query(value = "select * from mob_info where mob_id = ?1",nativeQuery=true)
	MobInfo findMobInfoByMobId(String mobId);
}
