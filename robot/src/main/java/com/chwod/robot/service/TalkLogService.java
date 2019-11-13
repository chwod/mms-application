package com.chwod.robot.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

import com.chwod.robot.domain.TalkLog;
import com.chwod.robot.utils.Constants;

@Service
public class TalkLogService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * save a talk log.
	 * 
	 * @param talkLog
	 */
	public void save(TalkLog talkLog) {
		this.jdbcTemplate.update(
				"insert into talk_log (session_id, talk_word, parse_word, response_word, server_port, server_name, scheme, request_uri,"
						+ " query_string, protocal, method, local_port, local_addr, content_length, content_type, response_code,"
						+ " remote_addr, remote_port, remote_host, remote_user, remote_locale, referer, user_agent, accept_language)"
						+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, talkLog.getSessionId());
						ps.setString(2, talkLog.getTalkWord());
						ps.setString(3, talkLog.getParseWord());
						ps.setString(4, talkLog.getResponseWord());
						ps.setInt(5, talkLog.getServerPort());
						ps.setString(6, talkLog.getServerName());
						ps.setString(7, talkLog.getScheme());
						ps.setString(8, talkLog.getRequest_uri());
						ps.setString(9, talkLog.getQueryString());
						ps.setString(10, talkLog.getProtocal());
						ps.setString(11, talkLog.getMethod());
						ps.setInt(12, talkLog.getLocalPort());
						ps.setString(13, talkLog.getLocalAddr());
						ps.setInt(14, talkLog.getContentLength());
						ps.setString(15, talkLog.getContentType());
						ps.setInt(16, talkLog.getResponseCode());
						ps.setString(17, talkLog.getRemoteAddr());
						ps.setInt(18, talkLog.getRemotePort());
						ps.setString(19, talkLog.getRemoteHost());
						ps.setString(20, talkLog.getRemoteUser());
						ps.setString(21, talkLog.getRemoteLocale());
						ps.setString(22, talkLog.getReferer());
						ps.setString(23, talkLog.getUserAgent());
						ps.setString(24, talkLog.getAcceptLanguage());
					}
				});
	}

	/**
	 * get last talk content by session id
	 * 
	 * @param sessionId id of user session
	 * @return talk content.
	 */
	public TalkLog getLastTalkLogBySessionId(String sessionId) {
		return this.jdbcTemplate.query("select * from talk_log where session_id = ? order by create_time desc limit 1", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, sessionId);
			}
		}, new ResultSetExtractor<TalkLog>() {

			@Override
			public TalkLog extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					TalkLog talkLog = new TalkLog();
					talkLog.setLogId(rs.getLong(Constants.LOG_ID));
					talkLog.setSessionId(rs.getString(Constants.SESSION_ID));
					talkLog.setTalkWord(rs.getString(Constants.TALK_WORD));
					talkLog.setParseWord(rs.getString(Constants.PARSE_WORD));
					talkLog.setResponseWord(rs.getString(Constants.RESPONSE_WORD));
					talkLog.setServerPort(rs.getInt(Constants.SERVER_PORT));
					talkLog.setServerName(rs.getString(Constants.SERVER_NAME));
					talkLog.setScheme(rs.getString(Constants.SCHEME));
					talkLog.setRequest_uri(rs.getString(Constants.REQUEST_URI));
					talkLog.setQueryString(rs.getString(Constants.QUERY_STRING));
					talkLog.setProtocal(rs.getString(Constants.PROTOCAL));
					talkLog.setMethod(rs.getString(Constants.METHOD));
					talkLog.setLocalPort(rs.getInt(Constants.LOCAL_PORT));
					talkLog.setLocalAddr(rs.getString(Constants.LOCAL_ADDR));
					talkLog.setContentLength(rs.getInt(Constants.CONTENT_LENGTH));
					talkLog.setContentType(rs.getString(Constants.CONTENT_TYPE));
					talkLog.setResponseCode(rs.getInt(Constants.RESPONSE_CODE));
					talkLog.setRemoteAddr(rs.getString(Constants.REMOTE_ADDR));
					talkLog.setRemotePort(rs.getInt(Constants.REMOTE_PORT));
					talkLog.setRemoteHost(rs.getString(Constants.REMOTE_HOST));
					talkLog.setRemoteUser(rs.getString(Constants.REMOTE_USER));
					talkLog.setRemoteLocale(rs.getString(Constants.REMOTE_LOCALE));
					talkLog.setReferer(rs.getString(Constants.REFERER));
					talkLog.setUserAgent(rs.getString(Constants.USER_AGENT));
					talkLog.setAcceptLanguage(rs.getString(Constants.ACCEPT_LANGUAGE));
					talkLog.setCreateTime(rs.getTimestamp(Constants.CREATE_TIME));
					return talkLog;
				}
				return null;
			}
		});
	}
}
