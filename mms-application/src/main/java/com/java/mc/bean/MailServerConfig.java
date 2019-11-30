package com.java.mc.bean;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MailServerConfig implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5265670404087898792L;
	private Integer id;
	private short setuptype;
	private Integer msid;
	private Integer dsid;
	private String displayName;
	private Boolean testflag;
	private String testmail;
	private short serverType;
	private short connType;
	private boolean auth = true;
	private String domainName;
	private String smtpHost;
	private Integer smtpPort;
	private String ior;
	private boolean ssl = false;
	private boolean tls = false;
	private String popHost;
	private Integer popPort;
	private boolean popsEnable;
	private String defaultSenderAddress;
	private String defaultSenderTitle;
	private String defaultSenderUserName;
	private String defaultSenderPassword;
	private String mailFile;
	private int limitCycle;
	private int limitCount;
	private Integer proxyId;
	private String status;
	private Timestamp createTime;
	private Timestamp updateTime;
	private Integer tryCount = 0;
	
	public short getSetuptype() {
		return setuptype;
	}
	public void setSetuptype(short setuptype) {
		this.setuptype = setuptype;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getDsid() {
		return dsid;
	}
	public void setDsid(Integer dsid) {
		this.dsid = dsid;
	}
	public Boolean getTestflag() {
		return testflag;
	}
	public void setTestflag(Boolean testflag) {
		this.testflag = testflag;
	}
	public String getTestmail() {
		return testmail;
	}
	public void setTestmail(String testmail) {
		this.testmail = testmail;
	}
	public short getServerType() {
		return serverType;
	}
	public void setServerType(short serverType) {
		this.serverType = serverType;
	}
	public short getConnType() {
		return connType;
	}
	public void setConnType(short connType) {
		this.connType = connType;
	}
	public boolean isAuth() {
		return auth;
	}
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public void setAuth(boolean auth) {
		this.auth = auth;
	}
	public String getSmtpHost() {
		return smtpHost;
	}
	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}
	public Integer getSmtpPort() {
		return smtpPort;
	}
	public void setSmtpPort(Integer smtpPort) {
		this.smtpPort = smtpPort;
	}
	public String getIor() {
		return ior;
	}
	public void setIor(String ior) {
		this.ior = ior;
	}
	public Boolean isSsl() {
		return ssl;
	}
	public void setSsl(boolean ssl) {
		this.ssl = ssl;
	}
	public Boolean isTls() {
		return tls;
	}
	public void setTls(boolean tls) {
		this.tls = tls;
	}
	public String getPopHost() {
		return popHost;
	}
	public void setPopHost(String popHost) {
		this.popHost = popHost;
	}
	public Integer getPopPort() {
		return popPort;
	}
	public void setPopPort(Integer popPort) {
		this.popPort = popPort;
	}
	public boolean isPopsEnable() {
		return popsEnable;
	}
	public void setPopsEnable(boolean popsEnable) {
		this.popsEnable = popsEnable;
	}
	public String getDefaultSenderAddress() {
		return defaultSenderAddress;
	}
	public void setDefaultSenderAddress(String defaultSenderAddress) {
		this.defaultSenderAddress = defaultSenderAddress;
	}
	public String getDefaultSenderTitle() {
		return defaultSenderTitle;
	}
	public void setDefaultSenderTitle(String defaultSenderTitle) {
		this.defaultSenderTitle = defaultSenderTitle;
	}
	public String getDefaultSenderUserName() {
		return defaultSenderUserName;
	}
	public void setDefaultSenderUserName(String defaultSenderUserName) {
		this.defaultSenderUserName = defaultSenderUserName;
	}
	
	@JsonIgnore
	public String getDefaultSenderPassword() {
		return defaultSenderPassword;
	}
	public void setDefaultSenderPassword(String defaultSenderPassword) {
		this.defaultSenderPassword = defaultSenderPassword;
	}
	public String getMailFile() {
		return mailFile;
	}
	public void setMailFile(String mailFile) {
		this.mailFile = mailFile;
	}
	public int getLimitCycle() {
		return limitCycle;
	}
	public void setLimitCycle(int limitCycle) {
		this.limitCycle = limitCycle;
	}
	public int getLimitCount() {
		return limitCount;
	}
	public void setLimitCount(int limitCount) {
		this.limitCount = limitCount;
	}
	public Integer getProxyId() {
		return proxyId;
	}
	public void setProxyId(Integer proxyId) {
		this.proxyId = proxyId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreateTime() {
		if(this.createTime != null){
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.createTime);
		}
		return null;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		if(this.updateTime != null){
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.updateTime);
		}
		return null;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getTryCount() {
		return tryCount;
	}
	public void setTryCount(Integer tryCount) {
		this.tryCount = tryCount;
	}
	public Integer getMsid() {
		return msid;
	}
	public void setMsid(Integer msid) {
		this.msid = msid;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
}
