package com.chwod.robot.bean;

import java.util.List;

public class EventContext {

	//sentence type
	/**
	 * 陈述句
	 */
	public static final short SENTENCE_TYPE_DECLARATIVE = 1;
	/**
	 * 肯定陈述句
	 */
	public static final short SENTENCE_TYPE_DECLARATIVE_AFFIRMATIVE = 11;
	/**
	 * 否定陈述句
	 */
	public static final short SENTENCE_TYPE_DECLARATIVE_NEGATIVE = 12;
	/**
	 * 双重否定句
	 */
	public static final short SENTENCE_TYPE_DECLARATIVE_DOUBLE_NEGATIVE = 13;
	/**
	 * 疑问句
	 */
	public static final short SENTENCE_TYPE_QUESTION = 2;
	/**
	 * 是非问句
	 */
	public static final short SENTENCE_TYPE_QUESTION_YESNO = 21;
	/**
	 * 特指问句
	 */
	public static final short SENTENCE_TYPE_QUESTION_SPECIAL = 22;
	/**
	 * 选择问句
	 */
	public static final short SENTENCE_TYPE_QUESTION_ALTERNATIVE = 23;
	/**
	 * 正反问句
	 */
	public static final short SENTENCE_TYPE_QUESTION_A_NOT_A = 24;
	/**
	 * 反问句
	 */
	public static final short SENTENCE_TYPE_QUESTION_RHETORICAL = 25;
	/**
	 * 祈使句
	 */
	public static final short SENTENCE_TYPE_IMPERATIVE = 3;
	/**
	 * 命令祈使句
	 */
	public static final short SENTENCE_TYPE_IMPERATIVE_COMMAND = 31;
	/**
	 * 请求祈使句
	 */
	public static final short SENTENCE_TYPE_IMPERATIVE_REQUEST = 32;
	/**
	 * 禁止祈使句
	 */
	public static final short SENTENCE_TYPE_IMPERATIVE_PROHIBIT = 33;
	/**
	 * 感叹句
	 */
	public static final short SENTENCE_TYPE_EXCLAMATORY = 4;
	
	//sentence predicate type
	/**
	 * 是
	 */
	public static final String SENTECNE_PREDICATE_TYPE_IS = "IS";
	/**
	 * 属于
	 */
	public static final String SENTECNE_PREDICATE_TYPE_BELONG = "BELONG";

	private Short type;
	private Short subType;

	private String subject; 			// 主语
	private String predicate; 			// 谓语
	private String object; 				// 宾语
	private String verb; 				// 动语
	private String attributive; 		// 定语
	private String adverbial; 			// 状语
	private String complement; 			// 补语
	private String centralLanguage; 	// 中心语
	private String preposition;			// 介词

	private String time; 				// 时间
	private String address;				// 地点
	private String people; 				// 人物
	private String event; 				// 事件

	private String currentEvent; 		// 当前事件
	private String targetEvent; 		// 目标事件
	
	private List<String> elementList;	// 需要处理的元素列表，有前后顺序

	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	public Short getSubType() {
		return subType;
	}

	public void setSubType(Short subType) {
		this.subType = subType;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPredicate() {
		return predicate;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getVerb() {
		return verb;
	}

	public void setVerb(String verb) {
		this.verb = verb;
	}

	public String getAttributive() {
		return attributive;
	}

	public void setAttributive(String attributive) {
		this.attributive = attributive;
	}

	public String getAdverbial() {
		return adverbial;
	}

	public void setAdverbial(String adverbial) {
		this.adverbial = adverbial;
	}

	public String getComplement() {
		return complement;
	}

	public void setComplement(String complement) {
		this.complement = complement;
	}

	public String getCentralLanguage() {
		return centralLanguage;
	}

	public void setCentralLanguage(String centralLanguage) {
		this.centralLanguage = centralLanguage;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPeople() {
		return people;
	}

	public void setPeople(String people) {
		this.people = people;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getCurrentEvent() {
		return currentEvent;
	}

	public void setCurrentEvent(String currentEvent) {
		this.currentEvent = currentEvent;
	}

	public String getTargetEvent() {
		return targetEvent;
	}

	public void setTargetEvent(String targetEvent) {
		this.targetEvent = targetEvent;
	}

	public List<String> getElementList() {
		return elementList;
	}

	public void setElementList(List<String> elementList) {
		this.elementList = elementList;
	}

	public String getPreposition() {
		return preposition;
	}

	public void setPreposition(String preposition) {
		this.preposition = preposition;
	}

}
