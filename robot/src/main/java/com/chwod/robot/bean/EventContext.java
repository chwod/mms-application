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
	
	private String focus;				// 焦点

	private String time; 				// 时间
	private String address;				// 地点
	private String people; 				// 人物
	private String event; 				// 事件

	private String currentEvent; 		// 当前事件
	private String targetEvent; 		// 目标事件
	
	private List<String> elementList;	// 需要处理的元素列表，有前后顺序

	/**
	 * 句型
	 * @return
	 */
	public Short getType() {
		return type;
	}
	/**
	 * 句型
	 */
	public void setType(Short type) {
		this.type = type;
	}
	/**
	 * 子类型
	 * @return
	 */
	public Short getSubType() {
		return subType;
	}
	/**
	 * 子类型
	 */
	public void setSubType(Short subType) {
		this.subType = subType;
	}
	/**
	 * 主语
	 * @return
	 */
	public String getSubject() {
		return subject;
	}
	/**
	 * 主语
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * 谓语
	 * @return
	 */
	public String getPredicate() {
		return predicate;
	}
	/**
	 * 谓语
	 */
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}
	/**
	 * 宾语
	 * @return
	 */
	public String getObject() {
		return object;
	}
	/**
	 * 宾语
	 */
	public void setObject(String object) {
		this.object = object;
	}
	/**
	 * 动语
	 * @return
	 */
	public String getVerb() {
		return verb;
	}
	/**
	 * 动语
	 */
	public void setVerb(String verb) {
		this.verb = verb;
	}
	/**
	 * 定语
	 * @return
	 */
	public String getAttributive() {
		return attributive;
	}
	/**
	 * 定语
	 */
	public void setAttributive(String attributive) {
		this.attributive = attributive;
	}
	/**
	 * 状语
	 * @return
	 */
	public String getAdverbial() {
		return adverbial;
	}
	/**
	 * 状语
	 */
	public void setAdverbial(String adverbial) {
		this.adverbial = adverbial;
	}
	/**
	 * 补语
	 * @return
	 */
	public String getComplement() {
		return complement;
	}
	/**
	 * 补语
	 */
	public void setComplement(String complement) {
		this.complement = complement;
	}
	/**
	 * 中心语
	 * @return
	 */
	public String getCentralLanguage() {
		return centralLanguage;
	}
	/**
	 * 中心语
	 */
	public void setCentralLanguage(String centralLanguage) {
		this.centralLanguage = centralLanguage;
	}
	/**
	 * 时间
	 * @return
	 */
	public String getTime() {
		return time;
	}
	/**
	 * 时间
	 */
	public void setTime(String time) {
		this.time = time;
	}
	/**
	 * 地点
	 * @return
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * 地点
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * 人物
	 * @return
	 */
	public String getPeople() {
		return people;
	}
	/**
	 * 人物
	 */
	public void setPeople(String people) {
		this.people = people;
	}
	/**
	 * 事件
	 * @return
	 */
	public String getEvent() {
		return event;
	}
	/**
	 * 事件
	 */
	public void setEvent(String event) {
		this.event = event;
	}
	/**
	 * 当前事件
	 * @return
	 */
	public String getCurrentEvent() {
		return currentEvent;
	}
	/**
	 * 当前事件
	 */
	public void setCurrentEvent(String currentEvent) {
		this.currentEvent = currentEvent;
	}
	/**
	 * 目标事件
	 * @return
	 */
	public String getTargetEvent() {
		return targetEvent;
	}
	/**
	 * 目标事件
	 */
	public void setTargetEvent(String targetEvent) {
		this.targetEvent = targetEvent;
	}
	/**
	 * 元素列表
	 * @return
	 */
	public List<String> getElementList() {
		return elementList;
	}
	/**
	 * 元素列表
	 */
	public void setElementList(List<String> elementList) {
		this.elementList = elementList;
	}
	/**
	 * 介词
	 * @return
	 */
	public String getPreposition() {
		return preposition;
	}
	/**
	 * 介词
	 */
	public void setPreposition(String preposition) {
		this.preposition = preposition;
	}
	/**
	 * 关注点
	 * @return
	 */
	public String getFocus() {
		return focus;
	}
	/**
	 * 关注点
	 */
	public void setFocus(String focus) {
		this.focus = focus;
	}

}
