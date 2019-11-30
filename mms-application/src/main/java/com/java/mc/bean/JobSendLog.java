package com.java.mc.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class JobSendLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6507908930345016403L;
	private Long id;
	private String seq;
	private String from;
	private String to;
	private Short status;
	private String subject;
	private String content;
	private String attachment;
	private String returnCode;
	private String message;
	private Timestamp sendTime;
	private String formatSendTime;
	private String formatCreateTime;
	
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Timestamp getSendTime() {
		return sendTime;
	}
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}
	public String getFormatSendTime() {
		return formatSendTime;
	}
	public void setFormatSendTime(String formatSendTime) {
		this.formatSendTime = formatSendTime;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFormatCreateTime() {
		return formatCreateTime;
	}
	public void setFormatCreateTime(String formatCreateTime) {
		this.formatCreateTime = formatCreateTime;
	}
}
