package com.chwod.robot.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import com.chwod.robot.domain.TalkLog;
import com.chwod.robot.service.TalkLogService;
import com.chwod.robot.utils.Constants;

@Component
public class LogFilter implements Filter {

	@Autowired
	private TalkLogService talkLogService;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		this.doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
	}

	private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		boolean flag = request.getMethod().equalsIgnoreCase(RequestMethod.POST.toString()) ? true : false;
		if (!flag) {
			chain.doFilter(request, response);
			return;
		}
		// log record
		TalkLog talkLog = new TalkLog();
		talkLog.setAcceptLanguage(request.getHeader(Constants.Accept_Language));
		talkLog.setRemoteLocale(request.getLocale().getDisplayName());
		talkLog.setReferer(request.getHeader(Constants.Referer));
		talkLog.setRemoteAddr(request.getRemoteAddr());
		talkLog.setRemoteHost(request.getRemoteHost());
		talkLog.setContentType(request.getContentType());
		talkLog.setContentLength(request.getContentLength());
		talkLog.setLocalAddr(request.getLocalAddr());
		talkLog.setLocalPort(request.getLocalPort());
		talkLog.setMethod(request.getMethod());
		talkLog.setProtocal(request.getProtocol());
		talkLog.setQueryString(request.getQueryString());
		talkLog.setRequest_uri(request.getRequestURI());
		talkLog.setScheme(request.getScheme());
		talkLog.setServerName(request.getServerName());
		talkLog.setServerPort(request.getServerPort());
		talkLog.setRemotePort(request.getRemotePort());
		talkLog.setRemoteUser(request.getRemoteUser());
		talkLog.setSessionId(request.getSession(true).getId());
		talkLog.setUserAgent(request.getHeader(Constants.User_Agent));
		chain.doFilter(request, response);
		talkLog.setTalkWord(String.valueOf(request.getAttribute(Constants.requestWord)));
		talkLog.setParseWord(String.valueOf(request.getAttribute(Constants.parseWord)));
		talkLog.setResponseWord(String.valueOf(request.getAttribute(Constants.responseWord)));
		talkLog.setResponseCode(response.getStatus());
		this.talkLogService.save(talkLog);
	}

	@Override
	public void destroy() {

	}

}
