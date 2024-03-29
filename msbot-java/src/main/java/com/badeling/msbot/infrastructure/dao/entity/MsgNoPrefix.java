package com.badeling.msbot.infrastructure.dao.entity;


import jakarta.persistence.*;

@Entity
public class MsgNoPrefix {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String question;
	private String answer;
	private boolean exact;
	public MsgNoPrefix(){
		
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public boolean isExact() {
		return exact;
	}
	public void setExact(boolean exact) {
		this.exact = exact;
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
