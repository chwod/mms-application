package com.java.mc.bean;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class SendType {

	private Integer id;
	private String displayName;
	private String columnName;
	private int operationType;
	private String condition;
	private Integer msid;
	private Integer smid;
	private Timestamp createTime;
	private Timestamp updateTime;
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
}
