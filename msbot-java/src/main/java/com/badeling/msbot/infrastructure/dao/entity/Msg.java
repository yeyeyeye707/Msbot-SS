package com.badeling.msbot.infrastructure.dao.entity;


import jakarta.persistence.*;

@Entity
public class Msg {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String question;
	private String answer;
	private String createId;
	private String link;
	public Msg() {
		
	}
	
	public Msg(Long id, String question, String answer, String createId, String link) {
		super();
		this.id = id;
		this.question = question;
		this.answer = answer;
		this.createId = createId;
		this.link = link;
	}
	
	public String getCreateId() {
		return createId;
	}
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	public String getLink() {
		return link;
	}


	public void setLink(String link) {
		this.link = link;
	}


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	
	
	
}
