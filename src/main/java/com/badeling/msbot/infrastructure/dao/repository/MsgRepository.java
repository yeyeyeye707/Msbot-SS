package com.badeling.msbot.infrastructure.dao.repository;

import com.badeling.msbot.infrastructure.dao.entity.Msg;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


public interface MsgRepository extends CrudRepository<Msg, Long>{
	@Query(value = "select * from msg",nativeQuery=true)
	Set<Msg> findAllQuestion();
	
	@Modifying
	@Transactional
	@Query(value = "delete from msg where id = ?1",nativeQuery=true)
	void deleteQuestion(String id);
	
	@Query(value = "select * from msg where question like %?1%",nativeQuery=true)
	Set<Msg> findMsgLikeQuestion(String question);

//	@Query(value = "select * from ms.msg where locate(?1, question) > 0 or locate(question, ?1) > 0",nativeQuery = true)
//	Set<Msg> findMsgLocateQuestion(String question);


	@Query(value = "select * from ms.msg where id = (select t1.id" +
			" from (select id," +
			" question," +
			" locate(?1, question) as l1," +
			" locate(question, ?1) as l2" +
			" from ms.msg) as t1" +
			" where t1.l1 > 0" +
			" or t1.l2 > 0" +
			" order by (abs(char_length(t1.question) - ?2)), t1.l1, t1.l2 asc" +
			" limit 1" +
			" );",nativeQuery = true)
	Msg findMsgLocateQuestion(String question, int len);
	
	@Query(value = "select * from msg where id = ?1",nativeQuery=true)
	Msg findQuestionById(String id);
	
	@Query(value = "select * from msg where question = ?1",nativeQuery=true)
	List<Msg> findMsgByExtQuestion(String question);
	
	@Modifying
	@Transactional
	@Query(value = "update msg set count = ?2 where id = ?1",nativeQuery=true)
	void modifyCount(Long id,Integer count);
}
