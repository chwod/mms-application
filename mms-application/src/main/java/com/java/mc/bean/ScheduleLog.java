package com.java.mc.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ScheduleLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1385901255032417940L;
	private Long id;
	private Integer scheduleId;
	private String status;
	private String executeTime;
	private Long runtimeCount;
	private String message;
	public void setId(Long id) {
		this.id = id;
	}
	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setExecuteTime(Timestamp executeTime) {
		if(executeTime != null){
			this.executeTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(executeTime);
		}
	}
	public void setRuntimeCount(Long runtimeCount) {
		this.runtimeCount = runtimeCount;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Long getId() {
		return id;
	}
	public Integer getScheduleId() {
		return scheduleId;
	}
	public String getStatus() {
		return status;
	}
	public String getExecuteTime() {
		return executeTime;
	}
	public Long getRuntimeCount() {
		return runtimeCount;
	}
	public String getMessage() {
		return message;
	}
	
	
}
