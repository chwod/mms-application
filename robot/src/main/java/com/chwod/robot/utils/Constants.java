package com.chwod.robot.utils;

public class Constants {

	public static final String Y = "Y";
	public static final String N = "N";
	
	//robot learning
	public static enum LEARNING {
		OK
	};
	
	public static final String COUNT = "COUNT";
	public static final String GLOBAL_CONFIGURATION = "GLOBAL_CONFIGURATION";
	public static final String PROCESS_DEEP = "PROCESS_DEEP";
	public static final String CONTEXT_EVENT = "CONTEXT_EVENT";
	public static final String CURRENT_CONTEXT_EVENTS = "CURRENT_CONTEXT_EVENTS";
	public static final String CURRENT_CONTEXT_STATUS_MISSING_SUBJECT = "CURRENT_CONTEXT_STATUS_MISSING_SUBJECT";
	public static final String CURRENT_CONTEXT_STATUS_QUESTION = "CURRENT_CONTEXT_STATUS_QUESTION";
	public static final String CURRENT_CONTEXT_STATUS_MISSING_RELATIONSHIP_IS = "CURRENT_CONTEXT_STATUS_MISSING_RELATIONSHIP_IS";
	
	//experience properties
	public static final String IS = "is";
	public static final String BELONG = "belong";
	
	//robot self
	public static final String ROBOT_SELF = "ROBOT_SELF";
	public static final String BIRTHDAY_YEAR = "BIRTHDAY_YEAR";
	public static final String BIRTHDAY_MONTH = "BIRTHDAY_MONTH";
	public static final String BIRTHDAY_DAY = "BIRTHDAY_DAY";
	
	//Part of speech
	/**
	 * 主语
	 */
	public static final String SUBJECT = "SUBJECT";
	/**
	 * 谓语
	 */
	public static final String PREDICATE = "PREDICATE";
	/**
	 * 宾语
	 */
	public static final String OBJECT = "OBJECT";
	/**
	 * 动语
	 */
	public static final String VERB = "VERB";
	/**
	 * 定语
	 */
	public static final String ATTRIBUTIVE = "ATTRIBUTIVE";
	/**
	 * 状语
	 */
	public static final String ADVERBIAL = "ADVERBIAL";
	/**
	 * 补语
	 */
	public static final String COMPLEMENT = "COMPLEMENT";
	/**
	 * 中心语
	 */
	public static final String CENTRALLANGUAGE = "CENTRALLANGUAGE";
	
	//http relations
	public static final String Accept_Language = "Accept-Language";
	public static final String Referer = "Referer";
	public static final String User_Agent = "User-Agent";
	public static final String requestWord = "requestWord";
	public static final String parseWord = "parseWord";
	public static final String responseWord = "responseWord";
	public static final String status = "status";
	public static final String index = "index";
	public static final String message = "message";
	public static final String result = "result";
	public static final String time = "time";
	public static final String MESSAGE_EMPTY = "Why don't you talk?";
	public static final String MESSAGE_MORE = "Please speak a little short(<50)";
	
	//tables
	public static final String CONFIGURATION = "CONFIGURATION";
	public static final String KNOWLEDGE = "KNOWLEDGE";
	public static final String TALK_LOG = "TALK_LOG";
	public static final String EXPERIENCE = "EXPERIENCE";
	
	//columns
	public static final String TITLE = "TITLE";
	public static final String NAME = "NAME";
	public static final String VALUE = "VALUE";
	public static final String VAL = "VAL";
	public static final String SEQUENCE = "SEQUENCE";
	public static final String STATUS = "STATUS";
	public static final String DESCRIPTION = "DESCRIPTION";
	public static final String CREATE_TIME = "CREATE_TIME";
	public static final String LOG_ID = "LOG_ID";
	public static final String SESSION_ID = "SESSION_ID";
	public static final String TALK_WORD = "TALK_WORD";
	public static final String PARSE_WORD = "PARSE_WORD";
	public static final String RESPONSE_WORD = "RESPONSE_WORD";
	public static final String RESPONSE_CODE = "RESPONSE_CODE";
	public static final String REMOTE_ADDR = "REMOTE_ADDR";
	public static final String REMOTE_PORT = "REMOTE_PORT";
	public static final String REMOTE_HOST = "REMOTE_HOST";
	public static final String REMOTE_USER = "REMOTE_USER";
	public static final String REMOTE_LOCALE = "REMOTE_LOCALE";
	public static final String REFERER = "REFERER";
	public static final String USER_AGENT = "USER_AGENT";
	public static final String ACCEPT_LANGUAGE = "ACCEPT_LANGUAGE";
	public static final String ID = "ID";
	public static final String EVENT_ID = "EVENT_ID";
	public static final String PROPERTY = "PROPERTY";
	public static final String CONTENT_TYPE = "CONTENT_TYPE";
	public static final String CONTENT_LENGTH = "CONTENT_LENGTH";
	public static final String LOCAL_ADDR = "LOCAL_ADDR";
	public static final String LOCAL_PORT = "LOCAL_PORT";
	public static final String METHOD = "METHOD";
	public static final String PROTOCAL = "PROTOCAL";
	public static final String QUERY_STRING = "QUERY_STRING";
	public static final String REQUEST_URI = "REQUEST_URI";
	public static final String SCHEME = "SCHEME";
	public static final String SERVER_NAME = "SERVER_NAME";
	public static final String SERVER_PORT = "SERVER_PORT";
	public static final String CONTENT = "CONTENT";
}
