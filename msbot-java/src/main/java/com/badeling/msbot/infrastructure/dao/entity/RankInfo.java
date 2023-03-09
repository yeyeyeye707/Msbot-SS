package com.badeling.msbot.infrastructure.dao.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
//@Table(name="rank_info")
@Data
public class RankInfo {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String user_id;
	private String user_name;
}
