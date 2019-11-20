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
	
	//part of speech
	/**
	 * 主语
	 */
	public static final short SENTENCE_PART_OF_SPEECH_SUBJECT = 101;
	/**
	 * 句子类型
	 */
	public static final short SENTENCE_PART_OF_SPEECH_TYPE = 102;
	/**
	 * 句子子类型
	 */
	public static final short SENTENCE_PART_OF_SPEECH_SUBTYPE = 103;
	/**
	 * 谓语
	 */
	public static final short SENTENCE_PART_OF_SPEECH_PREDICATE = 104;
	/**
	 * 宾语
	 */
	public static final short SENTENCE_PART_OF_SPEECH_OBJECT = 105;
	/**
	 * 动语
	 */
	public static final short SENTENCE_PART_OF_SPEECH_VERB = 106;
	/**
	 * 主语定语
	 */
	public static final short SENTENCE_PART_OF_SPEECH_ATTRIBUTIVE_BEFORE_SUBJECT = 107;
	/**
	 * 宾语定语
	 */
	public static final short SENTENCE_PART_OF_SPEECH_ATTRIBUTIVE_BEFORE_OBJECT = 108;
	/**
	 * 状语
	 */
	public static final short SENTENCE_PART_OF_SPEECH_ADVERBIAL = 109;
	/**
	 * 补语
	 */
	public static final short SENTENCE_PART_OF_SPEECH_COMPLEMENT = 110;
	/**
	 * 中心语
	 */
	public static final short SENTENCE_PART_OF_SPEECH_CENTRALLANGUAGE = 111;
	/**
	 * 介词
	 */
	public static final short SENTENCE_PART_OF_SPEECH_PREPOSITION = 112;
	
	//sentence predicate type
	/**
	 * 是
	 */
	public static final String SENTECNE_PREDICATE_TYPE_IS = "IS";
	/**
	 * 属于
	 */
	public static final String SENTECNE_PREDICATE_TYPE_BELONG = "BELONG";
	
	private String sessionId;

	private Short type;
	private Short subType;

	private List<String> subject; 									// 主语
	private String predicate; 									// 谓语
	private List<String> object; 										// 宾语
	private String verb; 										// 动语
	private List<String> attributiveBeforeSubject; 					// 主语定语
	private List<String> attributiveBeforeObject;					// 宾语定语
	private String adverbial; 									// 状语
	private String complement; 									// 补语
	private String centralLanguage; 							// 中心语
	private String preposition;									// 介词
	
	private short focus;		// 焦点

	private String currentEvent; 								// 当前事件
	private String currentResponse;								// 当前事件的回应
	private String targetEvent; 								// 目标事件
	private String targetResponse;								// 目标事件的回应
	
	public EventContext(String sessionId) {
		this.sessionId = sessionId;
	}
	
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
	public List<String> getSubject() {
		return subject;
	}
	/**
	 * 主语
	 */
	public void setSubject(List<String> subject) {
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
	public List<String> getObject() {
		return object;
	}
	/**
	 * 宾语
	 */
	public void setObject(List<String> object) {
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
	public short getFocus() {
		return focus;
	}
	/**
	 * 关注点
	 */
	public void setFocus(short focus) {
		this.focus = focus;
	}
	/**
	 * 主语定语
	 * @return
	 */
	public List<String> getAttributiveBeforeSubject() {
		return attributiveBeforeSubject;
	}
	/**
	 * 主语定语
	 * @param attributiveBeforeSubject
	 */
	public void setAttributiveBeforeSubject(List<String> attributiveBeforeSubject) {
		this.attributiveBeforeSubject = attributiveBeforeSubject;
	}
	/**
	 * 宾语定语
	 * @return
	 */
	public List<String> getAttributiveBeforeObject() {
		return attributiveBeforeObject;
	}
	/**
	 *  宾语定语
	 * @param attributiveBeforeObject
	 */
	public void setAttributiveBeforeObject(List<String> attributiveBeforeObject) {
		this.attributiveBeforeObject = attributiveBeforeObject;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * 当前语境中的回应
	 * @return
	 */
	public String getCurrentResponse() {
		return currentResponse;
	}

	/**
	 * 当前语境中的回应
	 * @param currentResponse
	 */
	public void setCurrentResponse(String currentResponse) {
		this.currentResponse = currentResponse;
	}

	/**
	 * 上一语境中的回应
	 * @return
	 */
	public String getTargetResponse() {
		return targetResponse;
	}

	/**
	 * 上一语境中的回应
	 * @param targetResponse
	 */
	public void setTargetResponse(String targetResponse) {
		this.targetResponse = targetResponse;
	}
}
