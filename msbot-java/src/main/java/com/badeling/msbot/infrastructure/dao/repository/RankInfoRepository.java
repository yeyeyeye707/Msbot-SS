package com.badeling.msbot.infrastructure.dao.repository;

import com.badeling.msbot.infrastructure.dao.entity.RankInfo;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

//@NoRepositoryBean
@Repository
public interface RankInfoRepository extends JpaRepository<RankInfo, Long> {
	@Query(value = "select * from rank_info where user_id = ?1",nativeQuery=true)
	RankInfo getInfoByUserId(String user_id);
}
