package com.java.mc.bean;

import java.io.Serializable;
import java.util.List;

public class SendCondition implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8821077208282084433L;
	private Integer id;
	private String displayName;
	private Boolean foundType;
	private Integer handlerId;
	private String description;
	private List<SendConditionOperation> operationList;
	private String operations;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public Boolean getFoundType() {
		return foundType;
	}
	public void setFoundType(Boolean foundType) {
		this.foundType = foundType;
	}
	public Integer getHandlerId() {
		return handlerId;
	}
	public void setHandlerId(Integer handlerId) {
		this.handlerId = handlerId;
	}
	public List<SendConditionOperation> getOperationList() {
		return operationList;
	}
	public void setOperationList(List<SendConditionOperation> operationList) {
		this.operationList = operationList;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOperations() {
		return operations;
	}
	public void setOperations(String operations) {
		this.operations = operations;
	}
}
