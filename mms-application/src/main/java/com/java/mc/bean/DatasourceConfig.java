package com.java.mc.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DatasourceConfig implements Serializable {

	private static final long serialVersionUID = 4657363552280281132L;
	private Integer id;
	private String displayName;
	private Short sqlType;
	private String host;
	private Integer port;
	private String dbName;
	private String archName;
	private Short authType;
	private String username;
	private String password;
	private String status;
	private Timestamp createTime;
	private Timestamp updateTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Short getSqlType() {
		return sqlType;
	}
	public void setSqlType(Short sqlType) {
		this.sqlType = sqlType;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getArchName() {
		return archName;
	}
	public void setArchName(String archName) {
		this.archName = archName;
	}
	public Short getAuthType() {
		return authType;
	}
	public void setAuthType(Short authType) {
		this.authType = authType;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@JsonIgnore
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
