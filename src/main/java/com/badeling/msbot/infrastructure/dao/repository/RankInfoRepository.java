package com.badeling.msbot.infrastructure.dao.repository;

import com.badeling.msbot.infrastructure.dao.entity.RankInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RankInfoRepository extends CrudRepository<RankInfo, Long>{
	@Query(value = "select * from rank_info where user_id = ?1",nativeQuery=true)
	RankInfo getInfoByUserId(String user_id);
}
