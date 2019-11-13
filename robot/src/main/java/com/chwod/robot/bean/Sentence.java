package com.chwod.robot.bean;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

/**
 * the talk sentence object.
 * 
 * @author xliu
 *
 */
public class Sentence {

	private String requestWord;
	private String word;
	private List<Part> partList;
	private String parseWord;
	private String responseWord;

	private Integer processDeep = 0;

	private HttpSession session;
	
	public Sentence(HttpSession session) {
		this.session = session;
	}

	/**
	 * get session object of request. use request session to save the current
	 * context events.
	 * 
	 * @return
	 */
	public HttpSession getSession() {
		return session;
	}

	/**
	 * the orignal word from client
	 * 
	 * @return
	 */
	public String getRequestWord() {
		return requestWord;
	}

	public void setRequestWord(String requestWord) {
		this.requestWord = requestWord;
	}

	/**
	 * the word to the talk service
	 * 
	 * @return
	 */
	public String getWord() {
		if (this.word == null && this.requestWord != null) {
			this.word = this.requestWord;
		}
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	/**
	 * parse the sentence to the part list
	 * 
	 * @return
	 */
	public List<Part> getPartList() {
		return partList;
	}

	public void setPartList(List<Part> partList) {
		this.partList = partList;
	}

	/**
	 * the parse string with only flag with '-' from part list.
	 * 
	 * @return
	 */
	public String getParseWord() {
		if (this.parseWord == null && this.partList != null) {
			this.parseWord = Sentence.parseHelper(this.partList);
		}
		return parseWord;
	}
	
	/**
	 * help method for parse word.
	 * @param partList
	 * @return
	 */
	public static String parseHelper(List<Part> partList) {
		StringBuffer parseString = new StringBuffer(StringUtils.EMPTY);
		for (Part part : partList) {
			parseString.append(part.getFlag()).append("-");
		}
		parseString.deleteCharAt(parseString.length() - 1);
		return parseString.toString();
	}

	public void setParseWord(String parseWord) {
		this.parseWord = parseWord;
	}

	/**
	 * response with the talk result.
	 * 
	 * @return
	 */
	public String getResponseWord() {
		return responseWord;
	}

	public void setResponseWord(String responseWord) {
		this.responseWord = responseWord;
	}

	public Integer getProcessDeep() {
		return processDeep;
	}

	public void deepPlus() {
		this.processDeep++;
	}

	public void setProcessDeep(Integer processDeep) {
		this.processDeep = processDeep;
	}
}
