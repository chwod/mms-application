package com.chwod.robot.domain;

import java.sql.Timestamp;

public class Experience {

	private Long id;
	private String parseWord;
	private String property;
	private String content;
	private Timestamp createTime;
	
	private Long count;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getParseWord() {
		return parseWord;
	}
	public void setParseWord(String parseWord) {
		this.parseWord = parseWord;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	
	
}
