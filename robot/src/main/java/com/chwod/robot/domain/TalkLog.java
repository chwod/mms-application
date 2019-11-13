package com.chwod.robot.domain;

import java.sql.Timestamp;

public class TalkLog implements java.io.Serializable {

	private static final long serialVersionUID = -9065090960915003121L;

	private Long logId;
	private String sessionId;
	private String talkWord;
	private String parseWord;
	private String responseWord;
	private Integer responseCode;
	private String serverName;
	private int serverPort;
	private String scheme;
	private String request_uri;
	private String queryString;
	private String protocal;
	private String method;
	private int localPort;
	private String localAddr;
	private Integer contentLength;
	private String contentType;
	private String remoteAddr;
	private Integer remotePort;
	private String remoteHost;
	private String remoteUser;
	private String referer;
	private String userAgent;
	private String acceptLanguage;
	private String remoteLocale;
	private Timestamp createTime;
	public Long getLogId() {
		return logId;
	}
	public void setLogId(Long logId) {
		this.logId = logId;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getTalkWord() {
		return talkWord;
	}
	public void setTalkWord(String talkWord) {
		this.talkWord = talkWord;
	}
	public String getParseWord() {
		return parseWord;
	}
	public void setParseWord(String parseWord) {
		this.parseWord = parseWord;
	}
	public String getResponseWord() {
		return responseWord;
	}
	public void setResponseWord(String responseWord) {
		this.responseWord = responseWord;
	}
	public Integer getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public int getServerPort() {
		return serverPort;
	}
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	public String getScheme() {
		return scheme;
	}
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}
	public String getRequest_uri() {
		return request_uri;
	}
	public void setRequest_uri(String request_uri) {
		this.request_uri = request_uri;
	}
	public String getQueryString() {
		return queryString;
	}
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	public String getProtocal() {
		return protocal;
	}
	public void setProtocal(String protocal) {
		this.protocal = protocal;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public int getLocalPort() {
		return localPort;
	}
	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}
	public String getLocalAddr() {
		return localAddr;
	}
	public void setLocalAddr(String localAddr) {
		this.localAddr = localAddr;
	}
	public Integer getContentLength() {
		return contentLength;
	}
	public void setContentLength(Integer contentLength) {
		this.contentLength = contentLength;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getRemoteAddr() {
		return remoteAddr;
	}
	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}
	public Integer getRemotePort() {
		return remotePort;
	}
	public void setRemotePort(Integer remotePort) {
		this.remotePort = remotePort;
	}
	public String getRemoteHost() {
		return remoteHost;
	}
	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}
	public String getRemoteUser() {
		return remoteUser;
	}
	public void setRemoteUser(String remoteUser) {
		this.remoteUser = remoteUser;
	}
	public String getReferer() {
		return referer;
	}
	public void setReferer(String referer) {
		this.referer = referer;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public String getAcceptLanguage() {
		return acceptLanguage;
	}
	public void setAcceptLanguage(String acceptLanguage) {
		this.acceptLanguage = acceptLanguage;
	}
	public String getRemoteLocale() {
		return remoteLocale;
	}
	public void setRemoteLocale(String remoteLocale) {
		this.remoteLocale = remoteLocale;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
