package com.badeling.msbot.infrastructure.dao.repository;

import com.badeling.msbot.infrastructure.dao.entity.MsgNoPrefix;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MsgNoPrefixRepository extends CrudRepository<MsgNoPrefix, Long>{
	@Query(value = "select * from msg_no_prefix order by id desc",nativeQuery=true)
	List<MsgNoPrefix> findMsgNPList();

//	@Query(value = "select * from msg_no_prefix where `question` like %?1%",nativeQuery=true)
	@Query(value = "select * from msg_no_prefix where `question` = ?1",nativeQuery=true)
	List<MsgNoPrefix> findMsgNP(String question);
}
