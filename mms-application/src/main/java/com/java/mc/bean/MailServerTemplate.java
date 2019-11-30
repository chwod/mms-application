package com.java.mc.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class MailServerTemplate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3620350573797230359L;
	private int templateId;
	private String templateName;
	private String smtphost;
	private Integer smtpport;
	private Timestamp createTime;
	public int getTemplateId() {
		return templateId;
	}
	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getSmtphost() {
		return smtphost;
	}
	public void setSmtphost(String smtphost) {
		this.smtphost = smtphost;
	}
	public Integer getSmtpport() {
		return smtpport;
	}
	public void setSmtpport(Integer smtpport) {
		this.smtpport = smtpport;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
}
