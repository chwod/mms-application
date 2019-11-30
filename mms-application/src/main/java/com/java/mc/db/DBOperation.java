package com.java.mc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import com.java.mc.bean.BatchJob;
import com.java.mc.bean.DatasourceConfig;
import com.java.mc.bean.GlobalConfig;
import com.java.mc.bean.KV;
import com.java.mc.bean.MailServerConfig;
import com.java.mc.bean.MailServerTemplate;
import com.java.mc.bean.MailTask;
import com.java.mc.bean.Schedule;
import com.java.mc.bean.ScheduleLog;
import com.java.mc.bean.SendCondition;
import com.java.mc.bean.SendConditionOperation;
import com.java.mc.bean.SendConditionOption;
import com.java.mc.bean.ShortMessageConfiguration;
import com.java.mc.bean.ShortMessageTask;
import com.java.mc.utils.Constants;

@Component
public class DBOperation {
	private static final Logger logger = LoggerFactory.getLogger(DBOperation.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * get datasource configurations by datasource id.
	 * 
	 * @return database configurations for remote database.
	 */
	public DatasourceConfig getDSConfigurationById(int id) {
		return this.jdbcTemplate.query("SELECT * FROM MMS_DS_CONFIGURATION WHERE DS_ID = ? AND STATUS = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, id);
						ps.setString(2, Constants.Y);
					}
				}, new ResultSetExtractor<DatasourceConfig>() {

					@Override
					public DatasourceConfig extractData(ResultSet rs) throws SQLException, DataAccessException {
						if (rs.next()) {
							DatasourceConfig datasourceConfig = new DatasourceConfig();
							datasourceConfig.setId(rs.getInt(Constants.DS_ID));
							datasourceConfig.setDisplayName(rs.getString(Constants.DISPLAY_NAME));
							datasourceConfig.setSqlType(
									rs.getString(Constants.SQL_TYPE) == null ? null : rs.getShort(Constants.SQL_TYPE));
							datasourceConfig.setHost(rs.getString(Constants.DB_HOST));
							datasourceConfig.setPort(
									rs.getString(Constants.DB_PORT) == null ? null : rs.getInt(Constants.DB_PORT));
							datasourceConfig.setDbName(rs.getString(Constants.DB_NAME));
							datasourceConfig.setArchName(rs.getString(Constants.DB_ARCHTECTURE_NAME));
							datasourceConfig.setAuthType(rs.getString(Constants.DB_AUTH_TYPE) == null ? null
									: rs.getShort(Constants.DB_AUTH_TYPE));
							datasourceConfig.setUsername(rs.getString(Constants.DB_USERNAME));
							datasourceConfig.setPassword(rs.getString(Constants.DB_PASSWORD));
							datasourceConfig.setStatus(rs.getString(Constants.STATUS));
							datasourceConfig.setCreateTime(rs.getTimestamp(Constants.CREATE_TIME));
							datasourceConfig.setUpdateTime(rs.getTimestamp(Constants.UPDATE_TIME));
							return datasourceConfig;
						}
						return null;
					}

				});
	}

	/**
	 * get data source configurations list.
	 * 
	 * @return database configurations for remote database.
	 */
	public List<DatasourceConfig> getDSConfigurationList() {
		return this.jdbcTemplate.query("SELECT * FROM MMS_DS_CONFIGURATION WHERE STATUS = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, Constants.Y);
					}
				}, new RowMapper<DatasourceConfig>() {

					@Override
					public DatasourceConfig mapRow(ResultSet rs, int rowNum) throws SQLException {
						DatasourceConfig datasourceConfig = new DatasourceConfig();
						datasourceConfig.setId(rs.getInt(Constants.DS_ID));
						datasourceConfig.setDisplayName(rs.getString(Constants.DISPLAY_NAME));
						datasourceConfig.setSqlType(
								rs.getString(Constants.SQL_TYPE) == null ? null : rs.getShort(Constants.SQL_TYPE));
						datasourceConfig.setHost(rs.getString(Constants.DB_HOST));
						datasourceConfig
								.setPort(rs.getString(Constants.DB_PORT) == null ? null : rs.getInt(Constants.DB_PORT));
						datasourceConfig.setDbName(rs.getString(Constants.DB_NAME));
						datasourceConfig.setArchName(rs.getString(Constants.DB_ARCHTECTURE_NAME));
						datasourceConfig.setAuthType(rs.getString(Constants.DB_AUTH_TYPE) == null ? null
								: rs.getShort(Constants.DB_AUTH_TYPE));
						datasourceConfig.setUsername(rs.getString(Constants.DB_USERNAME));
						datasourceConfig.setStatus(rs.getString(Constants.STATUS));
						datasourceConfig.setCreateTime(rs.getTimestamp(Constants.CREATE_TIME));
						datasourceConfig.setUpdateTime(rs.getTimestamp(Constants.UPDATE_TIME));
						return datasourceConfig;
					}

				});
	}

	/**
	 * remove mail setup configuration by id.
	 * 
	 * @param id
	 */
	public void removeMailServerConfigurationById(int id) {
		this.jdbcTemplate.update("UPDATE MMS_MS_CONFIGURATION SET STATUS = ?, UPDATE_TIME = ? WHERE MS_ID = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, Constants.N);
						ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
						ps.setInt(3, id);
					}
				});
	}

	/**
	 * remove short message configuration by id
	 * 
	 * @param id
	 */
	public void removeShortMessageConfigurationById(int id) {
		this.jdbcTemplate.update("UPDATE MMS_SM_CONFIGURATION SET STATUS = ?, UPDATE_TIME = ? WHERE SM_ID = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, Constants.N);
						ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
						ps.setInt(3, id);
					}
				});
	}

	/**
	 * remove datsource setup configuration by id.
	 * 
	 * @param id
	 */
	public void removeDSConfigurationById(int id) {
		this.jdbcTemplate.update("UPDATE MMS_DS_CONFIGURATION SET STATUS = ?, UPDATE_TIME = ? WHERE DS_ID = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, Constants.N);
						ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
						ps.setInt(3, id);
					}
				});
	}

	/**
	 * get mail server configuration by id
	 * 
	 * @param id
	 * @return
	 */
	public MailServerConfig getMailServerConfigrationById(int id) {
		return this.jdbcTemplate.query("SELECT * FROM MMS_MS_CONFIGURATION WHERE MS_ID = ? AND STATUS = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, id);
						ps.setString(2, Constants.Y);
					}
				}, new ResultSetExtractor<MailServerConfig>() {

					@Override
					public MailServerConfig extractData(ResultSet rs) throws SQLException, DataAccessException {
						if (rs.next()) {
							MailServerConfig msc = new MailServerConfig();
							msc.setId(rs.getInt(Constants.MS_ID));
							msc.setDisplayName(rs.getString(Constants.DISPLAY_NAME));
							msc.setServerType(rs.getShort(Constants.SERVER_TYPE));
							msc.setSmtpHost(rs.getString(Constants.SMTP_HOST));
							msc.setSmtpPort(
									rs.getString(Constants.SMTP_PORT) == null ? null : rs.getInt(Constants.SMTP_PORT));
							msc.setConnType(rs.getString(Constants.CONN_TYPE) == null ? null
									: rs.getShort(Constants.CONN_TYPE));
							msc.setIor(rs.getString(Constants.DIIOP_IOR));
							msc.setDomainName(rs.getString(Constants.DOMAIN_NAME));
							msc.setPopHost(rs.getString(Constants.POP_HOST));
							msc.setPopPort(
									rs.getString(Constants.POP_PORT) == null ? null : rs.getInt(Constants.POP_PORT));
							msc.setPopsEnable(rs.getString(Constants.POPS_ENABLE) == null ? null
									: rs.getBoolean(Constants.POPS_ENABLE));
							msc.setDefaultSenderAddress(rs.getString(Constants.DEFAULT_SENDER_ADDRESS));
							msc.setDefaultSenderTitle(rs.getString(Constants.DEFAULT_SENDER_TITLE));
							msc.setDefaultSenderUserName(rs.getString(Constants.DEFAULT_SENDER_USERNAME));
							msc.setDefaultSenderPassword(rs.getString(Constants.DEFAULT_SENDER_PASSWORD));
							msc.setMailFile(rs.getString(Constants.MAIL_FILE));
							msc.setLimitCycle(rs.getInt(Constants.LIMITATION_CYCLE));
							msc.setLimitCount(rs.getInt(Constants.LIMITATION_COUNT));
							msc.setProxyId(
									rs.getString(Constants.PROXY_ID) == null ? null : rs.getInt(Constants.PROXY_ID));
							msc.setCreateTime(rs.getTimestamp(Constants.CREATE_TIME));
							msc.setUpdateTime(rs.getTimestamp(Constants.UPDATE_TIME));
							msc.setAuth(
									rs.getString(Constants.IS_AUTH) == null ? null : rs.getBoolean(Constants.IS_AUTH));
							msc.setSsl(rs.getString(Constants.IS_SSL) == null ? null : rs.getBoolean(Constants.IS_SSL));
							msc.setTls(rs.getString(Constants.IS_TLS) == null ? null : rs.getBoolean(Constants.IS_TLS));
							msc.setStatus(rs.getString(Constants.STATUS));
							return msc;
						}
						return null;
					}
				});
	}

	/**
	 * get short message configuration by id
	 * 
	 * @param id
	 * @return
	 */
	public ShortMessageConfiguration getShortMessageConfigrationById(int id) {
		return this.jdbcTemplate.query("SELECT * FROM MMS_SM_CONFIGURATION WHERE SM_ID = ? AND STATUS = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, id);
						ps.setString(2, Constants.Y);
					}
				}, new ResultSetExtractor<ShortMessageConfiguration>() {

					@Override
					public ShortMessageConfiguration extractData(ResultSet rs)
							throws SQLException, DataAccessException {
						if (rs.next()) {
							ShortMessageConfiguration smc = new ShortMessageConfiguration();
							smc.setId(rs.getInt(Constants.SM_ID));
							smc.setDisplayName(rs.getString(Constants.DISPLAY_NAME));
							smc.setSmTunnel(rs.getObject(Constants.SM_TUNNEL_TYPE) == null ? null
									: rs.getShort(Constants.SM_TUNNEL_TYPE));
							smc.setSmAccessNumber(rs.getLong(Constants.SM_ACCESS_NUMBER));
							smc.setComponyName(rs.getString(Constants.COMPONY_NAME));
							smc.setApplicationId(rs.getString(Constants.APPLICATION_ID));
							smc.setApplicationName(rs.getString(Constants.APPLICATION_NAME));
							smc.setApplicationPassword(rs.getString(Constants.APPLICATION_PASSWORD));
							smc.setExtendCode(rs.getString(Constants.EXTEND_CODE));
							smc.setServiceAddress(rs.getString(Constants.SERVICE_ADDRESS));
							smc.setServiceType(rs.getString(Constants.SERVICE_TYPE));
							smc.setLimitCount(rs.getInt(Constants.LIMITATION_COUNT));
							smc.setLimitCycle(rs.getInt(Constants.LIMITATION_CYCLE));
							smc.setCreateTime(rs.getTimestamp(Constants.CREATE_TIME));
							smc.setUpdateTime(rs.getTimestamp(Constants.UPDATE_TIME));
							return smc;
						}
						return null;
					}
				});
	}

	/**
	 * get all the mail server configuration list
	 * 
	 * @return
	 */
	public List<MailServerConfig> getMailServerConfigrationList() {
		return this.jdbcTemplate.query("SELECT * FROM MMS_MS_CONFIGURATION WHERE STATUS = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, Constants.Y);
					}
				}, new RowMapper<MailServerConfig>() {

					@Override
					public MailServerConfig mapRow(ResultSet rs, int rowNum) throws SQLException {
						MailServerConfig mc = new MailServerConfig();
						mc.setId(rs.getInt(Constants.MS_ID));
						mc.setDisplayName(rs.getString(Constants.DISPLAY_NAME));
						mc.setServerType(rs.getString(Constants.SERVER_TYPE) == null ? null
								: rs.getShort(Constants.SERVER_TYPE));
						mc.setSmtpHost(rs.getString(Constants.SMTP_HOST));
						mc.setSmtpPort(
								rs.getString(Constants.SMTP_PORT) == null ? null : rs.getInt(Constants.SMTP_PORT));
						mc.setConnType(
								rs.getString(Constants.CONN_TYPE) == null ? null : rs.getShort(Constants.CONN_TYPE));
						mc.setIor(rs.getString(Constants.DIIOP_IOR));
						mc.setDomainName(rs.getString(Constants.DOMAIN_NAME));
						mc.setPopHost(rs.getString(Constants.POP_HOST));
						mc.setPopPort(rs.getString(Constants.POP_PORT) == null ? null : rs.getInt(Constants.POP_PORT));
						mc.setPopsEnable(rs.getString(Constants.POPS_ENABLE) == null ? null
								: rs.getBoolean(Constants.POPS_ENABLE));
						mc.setDefaultSenderAddress(rs.getString(Constants.DEFAULT_SENDER_ADDRESS));
						mc.setDefaultSenderTitle(rs.getString(Constants.DEFAULT_SENDER_TITLE));
						mc.setDefaultSenderUserName(rs.getString(Constants.DEFAULT_SENDER_USERNAME));
						mc.setDefaultSenderPassword(rs.getString(Constants.DEFAULT_SENDER_PASSWORD));
						mc.setMailFile(rs.getString(Constants.MAIL_FILE));
						mc.setLimitCycle(rs.getInt(Constants.LIMITATION_CYCLE));
						mc.setLimitCount(rs.getInt(Constants.LIMITATION_COUNT));
						mc.setProxyId(rs.getInt(Constants.PROXY_ID));
						mc.setCreateTime(rs.getTimestamp(Constants.CREATE_TIME));
						mc.setUpdateTime(rs.getTimestamp(Constants.UPDATE_TIME));
						mc.setAuth(rs.getBoolean(Constants.IS_AUTH));
						mc.setSsl(rs.getBoolean(Constants.IS_SSL));
						mc.setTls(rs.getBoolean(Constants.IS_TLS));
						mc.setStatus(rs.getString(Constants.STATUS));
						return mc;
					}
				});
	}

	/**
	 * get short message tunnel configuration list
	 * 
	 * @return
	 */
	public List<ShortMessageConfiguration> getShortMessageConfigrationList() {
		return this.jdbcTemplate.query("SELECT * FROM MMS_SM_CONFIGURATION WHERE STATUS = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, Constants.Y);
					}
				}, new RowMapper<ShortMessageConfiguration>() {

					@Override
					public ShortMessageConfiguration mapRow(ResultSet rs, int rowNum) throws SQLException {
						ShortMessageConfiguration smc = new ShortMessageConfiguration();
						smc.setId(rs.getInt(Constants.SM_ID));
						smc.setDisplayName(rs.getString(Constants.DISPLAY_NAME));
						smc.setSmTunnel(rs.getObject(Constants.SM_TUNNEL_TYPE) == null ? null
								: rs.getShort(Constants.SM_TUNNEL_TYPE));
						smc.setSmAccessNumber(rs.getLong(Constants.SM_ACCESS_NUMBER));
						smc.setComponyName(rs.getString(Constants.COMPONY_NAME));
						smc.setApplicationId(rs.getString(Constants.APPLICATION_ID));
						smc.setApplicationName(rs.getString(Constants.APPLICATION_NAME));
						smc.setApplicationPassword(rs.getString(Constants.APPLICATION_PASSWORD));
						smc.setExtendCode(rs.getString(Constants.EXTEND_CODE));
						smc.setServiceAddress(rs.getString(Constants.SERVICE_ADDRESS));
						smc.setServiceType(rs.getString(Constants.SERVICE_TYPE));
						smc.setStatus(rs.getString(Constants.STATUS));
						smc.setLimitCount(rs.getInt(Constants.LIMITATION_COUNT));
						smc.setLimitCycle(rs.getInt(Constants.LIMITATION_CYCLE));
						smc.setCreateTime(rs.getTimestamp(Constants.CREATE_TIME));
						smc.setUpdateTime(rs.getTimestamp(Constants.UPDATE_TIME));
						return smc;
					}
				});
	}

	public void updateMSConfiguration(MailServerConfig mailServerConfig) {
		this.jdbcTemplate.update(
				"UPDATE MMS_MS_CONFIGURATION "
						+ "SET DISPLAY_NAME = ?, SMTP_HOST = ?, SMTP_PORT = ?, DEFAULT_SENDER_ADDRESS = ?, "
						+ "DEFAULT_SENDER_TITLE = ?, DEFAULT_SENDER_USERNAME = ?, DEFAULT_SENDER_PASSWORD = ?, "
						+ "SERVER_TYPE = ?, CONN_TYPE = ?, MAIL_FILE = ?, DIIOP_IOR = ?, DOMAIN_NAME = ?, "
						+ "IS_AUTH = ?, IS_SSL = ?, IS_TLS = ?, UPDATE_TIME = ? WHERE MS_ID = ? AND STATUS = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, mailServerConfig.getDisplayName());
						ps.setString(2, mailServerConfig.getSmtpHost());
						ps.setInt(3, mailServerConfig.getSmtpPort());
						ps.setString(4, mailServerConfig.getDefaultSenderAddress());
						ps.setString(5, mailServerConfig.getDefaultSenderTitle());
						ps.setString(6, mailServerConfig.getDefaultSenderUserName());
						ps.setString(7, mailServerConfig.getDefaultSenderPassword());
						ps.setShort(8, mailServerConfig.getServerType());
						ps.setShort(9, mailServerConfig.getConnType());
						ps.setString(10, mailServerConfig.getMailFile());
						ps.setString(11, mailServerConfig.getIor());
						ps.setString(12, mailServerConfig.getDomainName());
						ps.setBoolean(13, mailServerConfig.isAuth());
						ps.setBoolean(14, mailServerConfig.isSsl());
						ps.setBoolean(15, mailServerConfig.isTls());
						ps.setTimestamp(16, new Timestamp(System.currentTimeMillis()));
						ps.setInt(17, mailServerConfig.getId());
						ps.setString(18, mailServerConfig.getStatus());
					}
				});
	}

	/**
	 * save mail configurations.
	 * 
	 * @param requestObject
	 *            the object contains mail configurations.
	 */
	public void saveMSConfiguration(MailServerConfig mailServerConfig) {
		if (mailServerConfig != null) {
			// save mail configuration.
			String sql = "INSERT INTO MMS_MS_CONFIGURATION "
					+ " ( DISPLAY_NAME, SMTP_HOST,SMTP_PORT, DEFAULT_SENDER_ADDRESS, "
					+ " DEFAULT_SENDER_TITLE, DEFAULT_SENDER_USERNAME, DEFAULT_SENDER_PASSWORD, "
					+ " SERVER_TYPE, CONN_TYPE, MAIL_FILE, DIIOP_IOR, DOMAIN_NAME, IS_AUTH, IS_SSL, IS_TLS ) "
					+ " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
			this.jdbcTemplate.update(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, mailServerConfig.getDisplayName());
					ps.setString(2, mailServerConfig.getSmtpHost());
					if (mailServerConfig.getSmtpPort() == null) {
						ps.setNull(3, Types.INTEGER);
					} else {
						ps.setInt(3, mailServerConfig.getSmtpPort());
					}
					ps.setString(4, mailServerConfig.getDefaultSenderAddress());
					ps.setString(5, mailServerConfig.getDefaultSenderTitle());
					ps.setString(6, mailServerConfig.getDefaultSenderUserName());
					ps.setString(7, mailServerConfig.getDefaultSenderPassword());
					ps.setInt(8, mailServerConfig.getServerType());
					ps.setInt(9, mailServerConfig.getConnType());
					ps.setString(10, mailServerConfig.getMailFile());
					ps.setString(11, mailServerConfig.getIor());
					ps.setString(12, mailServerConfig.getDomainName());
					ps.setBoolean(13, mailServerConfig.isAuth());
					ps.setBoolean(14, mailServerConfig.isSsl());
					ps.setBoolean(15, mailServerConfig.isTls());
				}
			});
		}
	}

	/**
	 * save a new short message tunnel configuration
	 * 
	 * @param smc
	 */
	public void saveSMConfiguration(ShortMessageConfiguration smc) {
		if (smc != null) {
			this.jdbcTemplate.update("INSERT INTO MMS_SM_CONFIGURATION"
					+ " (DISPLAY_NAME,SM_TUNNEL_TYPE,SM_ACCESS_NUMBER,COMPONY_NAME,APPLICATION_ID,APPLICATION_NAME,APPLICATION_PASSWORD,EXTEND_CODE,SERVICE_ADDRESS,SERVICE_TYPE)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?)", new PreparedStatementSetter() {

						@Override
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setString(1, smc.getDisplayName());
							ps.setInt(2, smc.getSmTunnel());
							ps.setLong(3, smc.getSmAccessNumber());
							ps.setString(4, smc.getComponyName());
							ps.setString(5, smc.getApplicationId());
							ps.setString(6, smc.getApplicationName());
							ps.setString(7, smc.getApplicationPassword());
							ps.setString(8, smc.getExtendCode());
							ps.setString(9, smc.getServiceAddress());
							ps.setString(10, smc.getServiceType());
						}
					});
		}
	}

	/**
	 * update short message tunnel configuration
	 * 
	 * @param smc
	 * @return true the data is exist, other it is false.
	 */
	public boolean updateSMConfiguration(ShortMessageConfiguration smc) {
		int result = 0;
		if (smc != null) {
			result = this.jdbcTemplate.update("UPDATE MMS_SM_CONFIGURATION"
					+ " SET DISPLAY_NAME = ?, SM_TUNNEL_TYPE = ?, SM_ACCESS_NUMBER = ?, COMPONY_NAME = ?,"
					+ " APPLICATION_ID = ?, APPLICATION_NAME = ?, APPLICATION_PASSWORD = ?, EXTEND_CODE = ?, SERVICE_ADDRESS = ?,"
					+ " SERVICE_TYPE = ?, UPDATE_TIME = ?" + " WHERE SM_ID = ?", new PreparedStatementSetter() {

						@Override
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setString(1, smc.getDisplayName());
							ps.setInt(2, smc.getSmTunnel());
							ps.setLong(3, smc.getSmAccessNumber());
							ps.setString(4, smc.getComponyName());
							ps.setString(5, smc.getApplicationId());
							ps.setString(6, smc.getApplicationName());
							ps.setString(7, smc.getApplicationPassword());
							ps.setString(8, smc.getExtendCode());
							ps.setString(9, smc.getServiceAddress());
							ps.setString(10, smc.getServiceType());
							ps.setTimestamp(11, new Timestamp(System.currentTimeMillis()));
							ps.setInt(12, smc.getId());
						}
					});
		}
		return result > 0 ? true : false;
	}

	/**
	 * get template configurations by id.
	 * 
	 * @param id
	 *            the template id.
	 * @return the corresponding configurations.
	 * @throws Exception
	 *             if can not find data by the id gived.
	 */
	public MailServerTemplate getTemplateMailConfigurations(int id) throws Exception {
		MailServerTemplate msinfo = this.jdbcTemplate.queryForObject(
				"SELECT * FROM MMS_MS_TEMPLATE WHERE TEMPLATE_ID = " + id, new RowMapper<MailServerTemplate>() {

					@Override
					public MailServerTemplate mapRow(ResultSet rs, int rowNum) throws SQLException {
						MailServerTemplate msTemplate = new MailServerTemplate();
						msTemplate.setTemplateId(rs.getInt(Constants.TEMPLATE_ID));
						msTemplate.setTemplateName(rs.getString(Constants.TEMPLATE_NAME));
						msTemplate.setSmtphost(rs.getString(Constants.SMTP_HOST));
						msTemplate.setSmtpport(rs.getInt(Constants.SMTP_PORT));
						return msTemplate;
					}
				});

		if (msinfo == null) {
			throw new Exception("未找到邮件服务器配置信息");
		}
		return msinfo;
	}

	public int changePassword(String newpassword, String oldpassword, String username) {
		int count = this.jdbcTemplate.update(
				"UPDATE USERS SET PASSWORD = ?, PASSWORD_IS_EXPIRED = ? WHERE USERNAME = ? AND PASSWORD = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, newpassword);
						ps.setBoolean(2, false);
						ps.setString(3, username);
						ps.setString(4, oldpassword);
					}
				});
		return count;
	}

	/**
	 * save database configurations.
	 * 
	 * @param sqlType
	 *            sql type
	 * @param dbHost
	 *            database host
	 * @param dbPort
	 *            database port
	 * @param dbName
	 *            database name
	 * @param dbArchName
	 *            database architecture name
	 * @param dbAuthType
	 *            database auth type
	 * @param dbUserName
	 *            database user name
	 * @param dbPassword
	 *            database password
	 */
	public void saveDatabaseConfiguration(DatasourceConfig dsconfig) {
		String sql = "INSERT INTO MMS_DS_CONFIGURATION "
				+ "(DISPLAY_NAME, SQL_TYPE, DB_HOST, DB_PORT, DB_NAME, DB_ARCHTECTURE_NAME, DB_AUTH_TYPE,  DB_USERNAME, DB_PASSWORD ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? )";
		this.jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, dsconfig.getDisplayName());
				ps.setInt(2, dsconfig.getSqlType());
				ps.setString(3, dsconfig.getHost());
				ps.setInt(4, dsconfig.getPort());
				ps.setString(5, dsconfig.getDbName());
				ps.setString(6, dsconfig.getArchName());
				ps.setInt(7,
						dsconfig.getAuthType() == null || dsconfig.getAuthType() == Constants.SQLSERVER_WINDOWS_AUTH ? 1
								: 2);
				ps.setString(8, dsconfig.getUsername());
				ps.setString(9, dsconfig.getPassword());
			}
		});
	}

	/**
	 * update datasource configurations.
	 * 
	 * @param dsc
	 */
	public void updateDataSourceConfiguration(DatasourceConfig dsc) {
		this.jdbcTemplate
				.update("UPDATE MMS_DS_CONFIGURATION SET DISPLAY_NAME = ?, SQL_TYPE = ?, DB_HOST = ?, DB_PORT = ?, DB_NAME = ?,"
						+ " DB_ARCHTECTURE_NAME = ?, DB_AUTH_TYPE = ?, DB_USERNAME = ?, DB_PASSWORD = ?, UPDATE_TIME = ? "
						+ "WHERE DS_ID = ? AND STATUS = ?", new PreparedStatementSetter() {

							@Override
							public void setValues(PreparedStatement ps) throws SQLException {
								ps.setString(1, dsc.getDisplayName());
								ps.setInt(2, dsc.getSqlType());
								ps.setString(3, dsc.getHost());
								ps.setInt(4, dsc.getPort());
								ps.setString(5, dsc.getDbName());
								ps.setString(6, dsc.getArchName());
								ps.setInt(7, dsc.getAuthType() == null
										|| dsc.getAuthType() == Constants.SQLSERVER_WINDOWS_AUTH ? 1 : 2);
								ps.setString(8, dsc.getUsername());
								ps.setString(9, dsc.getPassword());
								ps.setTimestamp(10, new Timestamp(System.currentTimeMillis()));
								ps.setInt(11, dsc.getId());
								ps.setString(12, dsc.getStatus());
							}
						});
	}

	/**
	 * 
	 * @return
	 */
	public List<MailServerTemplate> getBatchServerTemplateList() {
		return this.jdbcTemplate.query("SELECT * FROM MMS_MS_TEMPLATE", new RowMapper<MailServerTemplate>() {

			@Override
			public MailServerTemplate mapRow(ResultSet rs, int rowNum) throws SQLException {
				MailServerTemplate msTemplate = new MailServerTemplate();
				msTemplate.setTemplateId(rs.getInt(Constants.TEMPLATE_ID));
				msTemplate.setTemplateName(rs.getString(Constants.TEMPLATE_NAME));
				return msTemplate;
			}

		});
	}

	/**
	 * set mail sender limitation for mail server
	 * 
	 * @param cycle
	 *            cycle time.
	 * @param count
	 *            the totally count.
	 * @param configId
	 *            configuration id.
	 * @return the count of the update configurations.
	 */
	public int setMailLimit(int cycle, int count, int msid) {
		int updatecount = this.jdbcTemplate.update(
				"UPDATE MMS_MS_CONFIGURATION SET LIMITATION_CYCLE = ?, LIMITATION_COUNT = ?, UPDATE_TIME = ? WHERE MS_ID = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, cycle);
						ps.setInt(2, count);
						ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
						ps.setInt(4, msid);
					}
				});

		return updatecount;
	}

	/**
	 * set limitation for short message sender.
	 * 
	 * @param cycle
	 * @param count
	 * @param msid
	 * @return
	 */
	public int setSMLimit(int cycle, int count, int smid) {
		int updatecount = this.jdbcTemplate.update(
				"UPDATE MMS_SM_CONFIGURATION SET LIMITATION_CYCLE = ?, LIMITATION_COUNT = ?, UPDATE_TIME = ? WHERE SM_ID = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, cycle);
						ps.setInt(2, count);
						ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
						ps.setInt(4, smid);
					}
				});

		return updatecount;
	}

	/**
	 * get complete mail job count before a special time
	 * 
	 * @param before
	 * @return
	 */
	public Long getBatchMailJobCompleteCountByMSId(Integer id, Timestamp before) {
		return this.getBatchMailJobCompleteCountByMSId(id, before, null);
	}

	/**
	 * get completely short message job list size for short message sender after
	 * a special time
	 * 
	 * @param id
	 * @param before
	 * @return
	 */
	public Long getBatchSMJobCompleteCountBySMId(Integer id, Timestamp before) {
		return this.getBatchSMJobCompleteCountBySMId(id, before, null);
	}

	/**
	 * get batch job all complete count by schedule id
	 * 
	 * @param id
	 * @return
	 */
	public Long getBatchJobCompleteCountByScheduleId(int id) {
		return this.getBatchJobCompleteCountByScheduleId(id, null, null);
	}

	/**
	 * get all mail job complete count
	 * 
	 * @return
	 */
	public Long getBatchMailJobCompleteCountByMSId(Integer id) {
		return this.getBatchMailJobCompleteCountByMSId(id, null, null);
	}

	/**
	 * get completely job count for short message sender action.
	 * 
	 * @param id
	 * @return
	 */
	public Long getBatchSMJobCompleteCountBySMId(Integer id) {
		return this.getBatchSMJobCompleteCountBySMId(id, null, null);
	}

	/**
	 * get batch job complete count by schedule id in a time space
	 * 
	 * @param id
	 * @param from
	 * @param to
	 * @return
	 */
	public Long getBatchJobCompleteCountByScheduleId(int id, Timestamp from, Timestamp to) {
		return this.jdbcTemplate.query(
				"SELECT COUNT(*) FROM MMS_SCHEDULE_JOB WHERE SCHEDULE_ID = ? AND STATUS = ? AND SEND_TIME >= ? AND SEND_TIME <= ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, id);
						ps.setShort(2, Constants.SEND_END);
						ps.setTimestamp(3, from == null ? new Timestamp(0) : from);
						ps.setTimestamp(4, to == null ? new Timestamp(System.currentTimeMillis()) : to);
					}
				}, new ResultSetExtractor<Long>() {

					@Override
					public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
						return rs.next() == false ? 0 : rs.getLong(1);
					}
				});
	}

	/**
	 * get batch job completely count for mail sender action
	 * 
	 * @param actionType
	 * @param from
	 * @param to
	 * @return
	 */
	public Long getBatchJobCompleteCount(short actionType, Timestamp from, Timestamp to) {
		return this.jdbcTemplate.query(
				"SELECT COUNT(*) FROM MMS_SCHEDULE_JOB AS JOB"
						+ " LEFT JOIN MMS_SCHEDULE AS SCHEDULE ON JOB.SCHEDULE_ID = SCHEDULE.SCHEDULE_ID"
						+ " WHERE SCHEDULE.ACTION_TYPE = ? AND JOB.STATUS = ? AND JOB.SEND_TIME >= ? AND JOB.SEND_TIME <= ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setShort(1, actionType);
						ps.setShort(2, Constants.SEND_END);
						ps.setTimestamp(3, from == null ? new Timestamp(0) : from);
						ps.setTimestamp(4, to == null ? new Timestamp(System.currentTimeMillis()) : to);
					}
				}, new ResultSetExtractor<Long>() {

					@Override
					public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
						return rs.next() == false ? 0 : rs.getLong(1);
					}
				});
	}

	/**
	 * get batch job completely count for mail sending in a time space
	 * 
	 * @param id
	 *            mail server configuration id
	 * @param from
	 * @param to
	 * @return
	 */
	public Long getBatchMailJobCompleteCountByMSId(Integer id, Timestamp from, Timestamp to) {
		return this.jdbcTemplate.query(
				"SELECT COUNT(JOB.JOB_ID) FROM MMS_SCHEDULE_JOB AS JOB"
						+ " LEFT JOIN MMS_SCHEDULE AS SCHEDULE ON JOB.SCHEDULE_ID = SCHEDULE.SCHEDULE_ID"
						+ " WHERE JOB.STATUS = ? AND JOB.CODE = ? AND SCHEDULE.ACTION_TYPE = ? AND SCHEDULE.MS_ID = ? AND JOB.SEND_TIME >= ? AND JOB.SEND_TIME <= ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setShort(1, Constants.SEND_END);
						ps.setShort(2, Constants.SUCCESS);
						ps.setInt(3, Constants.ACTION_MAIL_SCAN);
						ps.setInt(4, id);
						ps.setTimestamp(5, from == null ? new Timestamp(0) : from);
						ps.setTimestamp(6, to == null ? new Timestamp(System.currentTimeMillis()) : to);
					}
				}, new ResultSetExtractor<Long>() {

					@Override
					public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
						return rs.next() == false ? 0 : rs.getLong(1);
					}
				});
	}

	/**
	 * get batch job completely list size for short message sender job and mail
	 * server configuration id in a time space
	 * 
	 * @param id
	 * @param from
	 * @param to
	 * @return
	 */
	public Long getBatchSMJobCompleteCountBySMId(Integer id, Timestamp from, Timestamp to) {
		return this.jdbcTemplate.query(
				"SELECT COUNT(JOB.JOB_ID) FROM MMS_SCHEDULE_JOB AS JOB"
						+ " LEFT JOIN MMS_SCHEDULE AS SCHEDULE ON JOB.SCHEDULE_ID = SCHEDULE.SCHEDULE_ID"
						+ " WHERE JOB.STATUS = ? AND JOB.CODE = ? AND SCHEDULE.ACTION_TYPE = ? AND SCHEDULE.SM_ID = ? AND JOB.SEND_TIME >= ? AND JOB.SEND_TIME <= ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setShort(1, Constants.SEND_END);
						ps.setShort(2, Constants.SUCCESS);
						ps.setInt(3, Constants.ACTION_SM_SCAN);
						ps.setInt(4, id);
						ps.setTimestamp(5, from == null ? new Timestamp(0) : from);
						ps.setTimestamp(6, to == null ? new Timestamp(System.currentTimeMillis()) : to);
					}
				}, new ResultSetExtractor<Long>() {

					@Override
					public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
						return rs.next() == false ? 0 : rs.getLong(1);
					}
				});
	}

	/**
	 * get batch job all complete count by schedule id and status
	 * 
	 * @param id
	 * @return
	 */
	public Long getBatchJobSuccessCountByScheduleId(int id) {
		return this.getBatchJobCompleteCountByScheduleId(id, Constants.SUCCESS, null, null);
	}

	/**
	 * get all mail job success count
	 * 
	 * @return
	 */
	public Long getBatchJobSuccessCount() {
		return this.getBatchJobCompleteCount(Constants.SUCCESS, null, null);
	}

	/**
	 * get batch job complete count by schedule id and status in a time space
	 * 
	 * @param id
	 * @param from
	 * @param to
	 * @return
	 */
	public Long getBatchJobSuccessCountByScheduleId(int id, Timestamp from, Timestamp to) {
		return this.getBatchJobCompleteCountByScheduleId(id, Constants.SUCCESS, from, to);
	}

	/**
	 * get batch job success count in a time space
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public Long getBatchJobSuccessCount(short actionType, Timestamp from, Timestamp to) {
		return this.getBatchJobCompleteCount(actionType, Constants.SUCCESS, from, to);
	}

	/**
	 * get batch job all complete count by schedule id and status
	 * 
	 * @param id
	 * @return
	 */
	public Long getBatchJobFailedCountByScheduleId(int id) {
		return this.getBatchJobCompleteCountByScheduleId(id, Constants.FAILED, null, null);
	}

	/**
	 * get all mail job failed count
	 * 
	 * @return
	 */
	public Long getBatchJobFailedCount() {
		return this.getBatchJobCompleteCount(Constants.FAILED, null, null);
	}

	/**
	 * get batch job complete count by schedule id and status in a time space
	 * 
	 * @param id
	 * @param from
	 * @param to
	 * @return
	 */
	public Long getBatchJobFailedCountByScheduleId(int id, Timestamp from, Timestamp to) {
		return this.jdbcTemplate.query(
				"SELECT COUNT(*) FROM MMS_SCHEDULE_JOB WHERE SCHEDULE_ID = ? AND STATUS = ? AND CODE <> ? AND SEND_TIME >= ? AND SEND_TIME <= ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, id);
						ps.setShort(2, Constants.SEND_END);
						ps.setShort(3, Constants.SUCCESS);
						ps.setTimestamp(4, from == null ? new Timestamp(0) : from);
						ps.setTimestamp(5, to == null ? new Timestamp(System.currentTimeMillis()) : to);
					}
				}, new ResultSetExtractor<Long>() {

					@Override
					public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
						return rs.next() == false ? 0 : rs.getLong(1);
					}
				});
	}

	/**
	 * get batch job failed count in a time space
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public Long getBatchJobFailedCount(short actionType, Timestamp from, Timestamp to) {

		return this.jdbcTemplate.query(
				"SELECT COUNT(*) FROM MMS_SCHEDULE_JOB AS JOB"
						+ " LEFT JOIN MMS_SCHEDULE AS SCHEDULE ON SCHEDULE.SCHEDULE_ID = JOB.SCHEDULE_ID"
						+ " WHERE SCHEDULE.ACTION_TYPE = ? AND JOB.STATUS = ? AND JOB.CODE <> ? AND JOB.SEND_TIME >= ? AND JOB.SEND_TIME <= ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setShort(1, actionType);
						ps.setShort(2, Constants.SEND_END);
						ps.setShort(3, Constants.SUCCESS);
						ps.setTimestamp(4, from == null ? new Timestamp(0) : from);
						ps.setTimestamp(5, to == null ? new Timestamp(System.currentTimeMillis()) : to);
					}
				}, new ResultSetExtractor<Long>() {

					@Override
					public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
						return rs.next() == false ? 0 : rs.getLong(1);
					}
				});

	}

	/**
	 * get batch job complete count by schedule id and status in a time space
	 * 
	 * @param id
	 * @param status
	 * @param from
	 * @param to
	 * @return
	 */
	public Long getBatchJobCompleteCountByScheduleId(int id, Short status, Timestamp from, Timestamp to) {
		return this.jdbcTemplate.query(
				"SELECT COUNT(*) FROM MMS_SCHEDULE_JOB WHERE SCHEDULE_ID = ? AND STATUS = ? AND CODE = ? AND SEND_TIME >= ? AND SEND_TIME <= ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, id);
						ps.setShort(2, Constants.SEND_END);
						ps.setShort(3, status);
						ps.setTimestamp(4, from == null ? new Timestamp(0) : from);
						ps.setTimestamp(5, to == null ? new Timestamp(System.currentTimeMillis()) : to);
					}
				}, new ResultSetExtractor<Long>() {

					@Override
					public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
						return rs.next() == false ? 0 : rs.getLong(1);
					}
				});
	}

	/**
	 * get batch job complete count in a time space
	 * 
	 * @param actionType
	 * @param status
	 * @param from
	 * @param to
	 * @return
	 */
	public Long getBatchJobCompleteCount(short actionType, short status, Timestamp from, Timestamp to) {
		return this.jdbcTemplate.query(
				"SELECT COUNT(*) FROM MMS_SCHEDULE_JOB AS JOB"
						+ " LEFT JOIN MMS_SCHEDULE AS SCHEDULE ON SCHEDULE.SCHEDULE_ID = JOB.SCHEDULE_ID"
						+ " WHERE SCHEDULE.ACTION_TYPE = ? AND JOB.STATUS = ? AND JOB.CODE = ? AND JOB.SEND_TIME >= ? AND JOB.SEND_TIME <= ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setShort(1, actionType);
						ps.setShort(2, Constants.SEND_END);
						ps.setShort(3, status);
						ps.setTimestamp(4, from == null ? new Timestamp(0) : from);
						ps.setTimestamp(5, to == null ? new Timestamp(System.currentTimeMillis()) : to);
					}
				}, new ResultSetExtractor<Long>() {

					@Override
					public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
						return rs.next() == false ? 0 : rs.getLong(1);
					}
				});
	}

	/**
	 * get batch job list by action type in a time space
	 * 
	 * @param actionType
	 * @param from
	 * @param to
	 * @return
	 */
	public List<BatchJob> getBatchJobList(short actionType, Timestamp from, Timestamp to) {

		return this.jdbcTemplate.query(
				"SELECT JOB.*, SCHEDULE.MS_ID AS MS_ID, SCHEDULE.DS_ID AS DS_ID, SCHEDULE.SM_ID AS SM_ID"
						+ " FROM MMS_SCHEDULE_JOB AS JOB LEFT JOIN MMS_SCHEDULE AS SCHEDULE ON SCHEDULE.SCHEDULE_ID = JOB.SCHEDULE_ID"
						+ " WHERE SCHEDULE.ACTION_TYPE = ? AND JOB.STATUS = ? AND JOB.SEND_TIME >= ? AND JOB.SEND_TIME <= ? ORDER BY JOB.SEND_TIME DESC {LIMIT 1000}",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setShort(1, actionType);
						ps.setInt(2, Constants.SEND_END);
						ps.setTimestamp(3, from == null ? new Timestamp(0) : from);
						ps.setTimestamp(4, to == null ? new Timestamp(System.currentTimeMillis()) : to);
					}
				}, new RowMapper<BatchJob>() {

					@Override
					public BatchJob mapRow(ResultSet rs, int rowNum) throws SQLException {
						BatchJob batchJob = new BatchJob();
						batchJob.setJobId(rs.getLong(Constants.JOB_ID));
						batchJob.setSeq(rs.getString(Constants.SEQ));
						batchJob.setSenderAddress(rs.getString(Constants.SENDER_ADDRESS));
						batchJob.setSenderTitle(rs.getString(Constants.SENDER_TITLE));
						batchJob.setSenderUserName(rs.getString(Constants.SENDER_USERNAME));
						batchJob.setSenderPassword(rs.getString(Constants.SENDER_PASSWORD));
						batchJob.setMsid(rs.getString(Constants.MS_ID) == null ? null : rs.getInt(Constants.MS_ID));
						batchJob.setDsid(rs.getString(Constants.DS_ID) == null ? null : rs.getInt(Constants.DS_ID));
						batchJob.setSmid(rs.getString(Constants.SM_ID) == null ? null : rs.getInt(Constants.SM_ID));
						batchJob.setName(rs.getString(Constants.NAME));
						batchJob.setGatewayId(rs.getString(Constants.GATEWAY_ID));
						batchJob.setScheduleId(rs.getInt(Constants.SCHEDULE_ID));
						batchJob.setFromAddress(rs.getString(Constants.FROM_ADDRESS));
						batchJob.setToAddressList(rs.getString(Constants.TO_ADDRESS));
						batchJob.setCcAddressList(rs.getString(Constants.CC_ADDRESS));
						batchJob.setBccAddressList(rs.getString(Constants.BCC_ADDRESS));
						batchJob.setSubject(rs.getString(Constants.SUBJECT));
						batchJob.setContent(rs.getString(Constants.CONTENT));
						batchJob.setAttachment(rs.getString(Constants.ATTACHEMENT));
						batchJob.setStatus(rs.getShort(Constants.STATUS));
						batchJob.setCode(rs.getShort(Constants.CODE));
						batchJob.setMessage(rs.getString(Constants.MESSAGE));
						batchJob.setCreateTime(rs.getTimestamp(Constants.CREATE_TIME));
						batchJob.setSendTime(rs.getTimestamp(Constants.SEND_TIME));
						return batchJob;
					}
				});
	}

	/**
	 * get batch job list by schedule id in a time space
	 * 
	 * @param id
	 * @param from
	 * @param to
	 * @return
	 */
	public List<BatchJob> getBatchJobListByScheduleId(int id, Timestamp from, Timestamp to) {

		return this.jdbcTemplate.query(
				"SELECT JOB.*, SCHEDULE.MS_ID AS MS_ID, SCHEDULE.DS_ID AS DS_ID, SCHEDULE.SM_ID AS SM_ID"
						+ " FROM MMS_SCHEDULE_JOB AS JOB LEFT JOIN MMS_SCHEDULE AS SCHEDULE ON SCHEDULE.SCHEDULE_ID = JOB.SCHEDULE_ID"
						+ " WHERE JOB.SCHEDULE_ID = ? AND " + " (JOB.STATUS = ? OR (JOB.STATUS = ? AND JOB.CODE = ?)) "
						+ " AND JOB.SEND_TIME >= ? AND JOB.SEND_TIME <= ? ORDER BY JOB.SEND_TIME DESC {LIMIT 1000}",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, id);
						ps.setInt(2, Constants.SEND_END);
						ps.setInt(3, Constants.SEND_INTI);
						ps.setInt(4, Constants.PENDING);
						ps.setTimestamp(5, from == null ? new Timestamp(0) : from);
						ps.setTimestamp(6, to == null ? new Timestamp(System.currentTimeMillis()) : to);
					}
				}, new RowMapper<BatchJob>() {

					@Override
					public BatchJob mapRow(ResultSet rs, int rowNum) throws SQLException {
						BatchJob batchJob = new BatchJob();
						batchJob.setJobId(rs.getLong(Constants.JOB_ID));
						batchJob.setSeq(rs.getString(Constants.SEQ));
						batchJob.setSenderAddress(rs.getString(Constants.SENDER_ADDRESS));
						batchJob.setSenderTitle(rs.getString(Constants.SENDER_TITLE));
						batchJob.setSenderUserName(rs.getString(Constants.SENDER_USERNAME));
						batchJob.setSenderPassword(rs.getString(Constants.SENDER_PASSWORD));
						batchJob.setMsid(rs.getString(Constants.MS_ID) == null ? null : rs.getInt(Constants.MS_ID));
						batchJob.setDsid(rs.getString(Constants.DS_ID) == null ? null : rs.getInt(Constants.DS_ID));
						batchJob.setSmid(rs.getString(Constants.SM_ID) == null ? null : rs.getInt(Constants.SM_ID));
						batchJob.setName(rs.getString(Constants.NAME));
						batchJob.setGatewayId(rs.getString(Constants.GATEWAY_ID));
						batchJob.setScheduleId(rs.getInt(Constants.SCHEDULE_ID));
						batchJob.setFromAddress(rs.getString(Constants.FROM_ADDRESS));
						batchJob.setToAddressList(rs.getString(Constants.TO_ADDRESS));
						batchJob.setCcAddressList(rs.getString(Constants.CC_ADDRESS));
						batchJob.setBccAddressList(rs.getString(Constants.BCC_ADDRESS));
						batchJob.setSubject(rs.getString(Constants.SUBJECT));
						batchJob.setContent(rs.getString(Constants.CONTENT));
						batchJob.setAttachment(rs.getString(Constants.ATTACHEMENT));
						batchJob.setStatus(rs.getShort(Constants.STATUS));
						batchJob.setCode(rs.getShort(Constants.CODE));
						batchJob.setMessage(rs.getString(Constants.MESSAGE));
						batchJob.setCreateTime(rs.getTimestamp(Constants.CREATE_TIME));
						batchJob.setSendTime(rs.getTimestamp(Constants.SEND_TIME));
						return batchJob;
					}
				});
	}

	/**
	 * get failed batch job list by action type in a time space
	 * 
	 * @param actionType
	 * @param from
	 * @param to
	 * @return
	 */
	public List<BatchJob> getBatchJobFailedList(short actionType, Timestamp from, Timestamp to) {

		return this.jdbcTemplate.query(
				"SELECT JOB.*, SCHEDULE.MS_ID AS MS_ID, SCHEDULE.DS_ID AS DS_ID, SCHEDULE.SM_ID AS SM_ID"
						+ " FROM MMS_SCHEDULE_JOB AS JOB LEFT JOIN MMS_SCHEDULE AS SCHEDULE ON SCHEDULE.SCHEDULE_ID = JOB.SCHEDULE_ID"
						+ " WHERE SCHEDULE.ACTION_TYPE = ? AND JOB.STATUS = ? AND JOB.CODE <> ? AND JOB.SEND_TIME >= ? AND JOB.SEND_TIME <= ? ORDER BY JOB.SEND_TIME DESC {LIMIT 1000}",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, actionType);
						ps.setInt(2, Constants.SEND_END);
						ps.setShort(3, Constants.SUCCESS);
						ps.setTimestamp(4, from == null ? new Timestamp(0) : from);
						ps.setTimestamp(5, to == null ? new Timestamp(System.currentTimeMillis()) : to);
					}
				}, new RowMapper<BatchJob>() {

					@Override
					public BatchJob mapRow(ResultSet rs, int rowNum) throws SQLException {
						BatchJob batchJob = new BatchJob();
						batchJob.setJobId(rs.getLong(Constants.JOB_ID));
						batchJob.setSeq(rs.getString(Constants.SEQ));
						batchJob.setSenderAddress(rs.getString(Constants.SENDER_ADDRESS));
						batchJob.setSenderTitle(rs.getString(Constants.SENDER_TITLE));
						batchJob.setSenderUserName(rs.getString(Constants.SENDER_USERNAME));
						batchJob.setSenderPassword(rs.getString(Constants.SENDER_PASSWORD));
						batchJob.setMsid(rs.getString(Constants.MS_ID) == null ? null : rs.getInt(Constants.MS_ID));
						batchJob.setDsid(rs.getString(Constants.DS_ID) == null ? null : rs.getInt(Constants.DS_ID));
						batchJob.setSmid(rs.getString(Constants.SM_ID) == null ? null : rs.getInt(Constants.SM_ID));
						batchJob.setName(rs.getString(Constants.NAME));
						batchJob.setGatewayId(rs.getString(Constants.GATEWAY_ID));
						batchJob.setScheduleId(rs.getInt(Constants.SCHEDULE_ID));
						batchJob.setFromAddress(rs.getString(Constants.FROM_ADDRESS));
						batchJob.setToAddressList(rs.getString(Constants.TO_ADDRESS));
						batchJob.setCcAddressList(rs.getString(Constants.CC_ADDRESS));
						batchJob.setBccAddressList(rs.getString(Constants.BCC_ADDRESS));
						batchJob.setSubject(rs.getString(Constants.SUBJECT));
						batchJob.setContent(rs.getString(Constants.CONTENT));
						batchJob.setAttachment(rs.getString(Constants.ATTACHEMENT));
						batchJob.setStatus(rs.getShort(Constants.STATUS));
						batchJob.setCode(rs.getShort(Constants.CODE));
						batchJob.setMessage(rs.getString(Constants.MESSAGE));
						batchJob.setCreateTime(rs.getTimestamp(Constants.CREATE_TIME));
						batchJob.setSendTime(rs.getTimestamp(Constants.SEND_TIME));
						return batchJob;
					}
				});
	}

	/**
	 * get batch job list by schedule id in a time space
	 * 
	 * @param id
	 * @param from
	 * @param to
	 * @return
	 */
	public List<BatchJob> getBatchJobFailedListByScheduleId(int id, Timestamp from, Timestamp to) {

		return this.jdbcTemplate
				.query("SELECT JOB.*, SCHEDULE.MS_ID AS MS_ID, SCHEDULE.DS_ID AS DS_ID, SCHEDULE.SM_ID AS SM_ID"
						+ " FROM MMS_SCHEDULE_JOB AS JOB LEFT JOIN MMS_SCHEDULE AS SCHEDULE ON SCHEDULE.SCHEDULE_ID = JOB.SCHEDULE_ID"
						+ " WHERE JOB.SCHEDULE_ID = ? AND JOB.STATUS = ? AND JOB.CODE <> ? AND JOB.SEND_TIME >= ? AND JOB.SEND_TIME <= ?"
						+ " ORDER BY JOB.SEND_TIME DESC {LIMIT 1000}", new PreparedStatementSetter() {

							@Override
							public void setValues(PreparedStatement ps) throws SQLException {
								ps.setInt(1, id);
								ps.setInt(2, Constants.SEND_END);
								ps.setShort(3, Constants.SUCCESS);
								ps.setTimestamp(4, from == null ? new Timestamp(0) : from);
								ps.setTimestamp(5, to == null ? new Timestamp(System.currentTimeMillis()) : to);
							}
						}, new RowMapper<BatchJob>() {

							@Override
							public BatchJob mapRow(ResultSet rs, int rowNum) throws SQLException {
								BatchJob batchJob = new BatchJob();
								batchJob.setJobId(rs.getLong(Constants.JOB_ID));
								batchJob.setSeq(rs.getString(Constants.SEQ));
								batchJob.setSenderAddress(rs.getString(Constants.SENDER_ADDRESS));
								batchJob.setSenderTitle(rs.getString(Constants.SENDER_TITLE));
								batchJob.setSenderUserName(rs.getString(Constants.SENDER_USERNAME));
								batchJob.setSenderPassword(rs.getString(Constants.SENDER_PASSWORD));
								batchJob.setMsid(
										rs.getString(Constants.MS_ID) == null ? null : rs.getInt(Constants.MS_ID));
								batchJob.setDsid(
										rs.getString(Constants.DS_ID) == null ? null : rs.getInt(Constants.DS_ID));
								batchJob.setSmid(
										rs.getString(Constants.SM_ID) == null ? null : rs.getInt(Constants.SM_ID));
								batchJob.setName(rs.getString(Constants.NAME));
								batchJob.setGatewayId(rs.getString(Constants.GATEWAY_ID));
								batchJob.setScheduleId(rs.getInt(Constants.SCHEDULE_ID));
								batchJob.setFromAddress(rs.getString(Constants.FROM_ADDRESS));
								batchJob.setToAddressList(rs.getString(Constants.TO_ADDRESS));
								batchJob.setCcAddressList(rs.getString(Constants.CC_ADDRESS));
								batchJob.setBccAddressList(rs.getString(Constants.BCC_ADDRESS));
								batchJob.setSubject(rs.getString(Constants.SUBJECT));
								batchJob.setContent(rs.getString(Constants.CONTENT));
								batchJob.setAttachment(rs.getString(Constants.ATTACHEMENT));
								batchJob.setCode(rs.getShort(Constants.CODE));
								batchJob.setMessage(rs.getString(Constants.MESSAGE));
								batchJob.setCreateTime(rs.getTimestamp(Constants.CREATE_TIME));
								batchJob.setSendTime(rs.getTimestamp(Constants.SEND_TIME));
								return batchJob;
							}
						});
	}

	public void resetFailedBatchJobStatus(Timestamp from, Timestamp to) {
		this.jdbcTemplate.update(
				"UPDATE MMS_SCHEDULE_JOB SET STATUS = ?, CODE = ?, MESSAGE = ?, SEND_TIME = ? WHERE STATUS = ? AND CODE = ? AND TO_ADDRESS NOT LIKE '%,%' AND SEND_TIME >= ? AND SEND_TIME <= ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, Constants.SEND_INTI);
						ps.setInt(2, 1);
						ps.setNull(3, Types.VARCHAR);
						ps.setNull(4, Types.TIMESTAMP);
						ps.setShort(5, Constants.SEND_END);
						ps.setShort(6, Constants.FAILED);
						ps.setTimestamp(7, from == null ? new Timestamp(0) : from);
						ps.setTimestamp(8, to == null ? new Timestamp(System.currentTimeMillis()) : to);
					}
				});
	}

	/**
	 * clear the schedule log before the special time
	 * 
	 * @param to
	 */
	public void clearScheduleLogBeforeTime(Timestamp to) {
		this.jdbcTemplate.update("DELETE FROM MMS_SCHEDULE_LOG WHERE CREATE_TIME <= ?", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setTimestamp(1, to == null ? new Timestamp(System.currentTimeMillis()) : to);
			}
		});

	}

	/**
	 * clear the mail log before the special time
	 */
	public void clearBatchLogBeforeTime(Timestamp to) {
		this.jdbcTemplate.update("DELETE FROM MMS_SCHEDULE_JOB WHERE CREATE_TIME <= ?", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setTimestamp(1, to == null ? new Timestamp(System.currentTimeMillis()) : to);
			}
		});
	}

	/**
	 * get runnable mail sender job list.
	 * 
	 * @return
	 */
	public List<BatchJob> getRunnableBatchSenderJobList() {
		return this.jdbcTemplate.query(
				"SELECT DISTINCT JOB.*, SCHEDULE.ACTION_TYPE AS ACTION_TYPE,"
						+ " SCHEDULE.MS_ID AS MS_ID, SCHEDULE.DS_ID AS DS_ID, SCHEDULE.SM_ID AS SM_ID FROM MMS_SCHEDULE_JOB AS JOB"
						+ " LEFT JOIN MMS_SCHEDULE AS SCHEDULE ON JOB.SCHEDULE_ID = SCHEDULE.SCHEDULE_ID WHERE JOB.STATUS = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, Constants.SEND_INTI);
					}
				}, new RowMapper<BatchJob>() {

					@Override
					public BatchJob mapRow(ResultSet rs, int rowNum) throws SQLException {
						BatchJob batchJob = new BatchJob();
						batchJob.setJobId(rs.getLong(Constants.JOB_ID));
						batchJob.setSeq(rs.getString(Constants.SEQ));
						batchJob.setSenderAddress(rs.getString(Constants.SENDER_ADDRESS));
						batchJob.setSenderTitle(rs.getString(Constants.SENDER_TITLE));
						batchJob.setSenderUserName(rs.getString(Constants.SENDER_USERNAME));
						batchJob.setSenderPassword(rs.getString(Constants.SENDER_PASSWORD));
						batchJob.setMsid(rs.getString(Constants.MS_ID) == null ? null : rs.getInt(Constants.MS_ID));
						batchJob.setDsid(rs.getString(Constants.DS_ID) == null ? null : rs.getInt(Constants.DS_ID));
						batchJob.setSmid(rs.getString(Constants.SM_ID) == null ? null : rs.getInt(Constants.SM_ID));
						batchJob.setName(rs.getString(Constants.NAME));
						batchJob.setFromName(rs.getString(Constants.FROM_NAME));
						batchJob.setFromEmail(rs.getString(Constants.FROM_EMAIL));
						batchJob.setFromId(rs.getString(Constants.FROM_ID));
						batchJob.setGatewayId(rs.getString(Constants.GATEWAY_ID));
						batchJob.setScheduleId(rs.getInt(Constants.SCHEDULE_ID));
						batchJob.setFromAddress(rs.getString(Constants.FROM_ADDRESS));
						batchJob.setTo(rs.getString(Constants.TO_LIST));
						batchJob.setToAddressList(rs.getString(Constants.TO_ADDRESS));
						batchJob.setCcAddressList(rs.getString(Constants.CC_ADDRESS));
						batchJob.setBccAddressList(rs.getString(Constants.BCC_ADDRESS));
						batchJob.setSubject(rs.getString(Constants.SUBJECT));
						batchJob.setContent(rs.getString(Constants.CONTENT));
						batchJob.setAttachment(rs.getString(Constants.ATTACHEMENT));
						batchJob.setStatus(rs.getShort(Constants.STATUS));
						batchJob.setCode(rs.getShort(Constants.CODE));
						batchJob.setMessage(rs.getString(Constants.MESSAGE));
						batchJob.setCreateTime(rs.getTimestamp(Constants.CREATE_TIME));
						batchJob.setSendTime(rs.getTimestamp(Constants.SEND_TIME));
						batchJob.setActionType(rs.getShort(Constants.ACTION_TYPE));
						return batchJob;
					}
				});
	}

	/**
	 * set attachmeng process configurations
	 * 
	 * @param suffixList
	 *            suffix list.
	 */
	public void setAttachmentProcessConfiguration(String suffixList) {
		List<String> updateSuffixList = new ArrayList<String>();
		if (suffixList != null && suffixList.length() > 0) {
			for (String suffix : suffixList.split(",")) {
				updateSuffixList.add(suffix);
			}
		}
		this.jdbcTemplate.update("DELETE FROM MMS_GLOBAL_CONFIGURATION WHERE NAME = ?", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, Constants.ATTACHMENT_AS_CONTENT);
			}
		});
		if (updateSuffixList.size() > 0) {
			this.jdbcTemplate.batchUpdate("INSERT INTO MMS_GLOBAL_CONFIGURATION (NAME, CODE) VALUES ( ?, ? )",
					new BatchPreparedStatementSetter() {

						@Override
						public void setValues(PreparedStatement ps, int i) throws SQLException {
							ps.setString(1, Constants.ATTACHMENT_AS_CONTENT);
							ps.setString(2, updateSuffixList.get(i));
						}

						@Override
						public int getBatchSize() {
							return updateSuffixList.size();
						}
					});
		}
	}

	/**
	 * save the mail sender task.
	 * 
	 * @param mtask
	 * @param validAddress
	 */
	public void saveMailJob(MailTask mtask, String validAddress, Integer scheduleId) {
		this.saveMailJob(mtask, validAddress, scheduleId, Constants.SEND_INTI, Constants.PENDING, null);
	}

	public void saveMailJob(MailTask mtask, String validAddress, Integer scheduleId, short status, short code,
			String message) {
		this.jdbcTemplate.update(
				"INSERT INTO MMS_SCHEDULE_JOB (SEQ, FROM_NAME, FROM_EMAIL, FROM_ID, TO_LIST, TO_ADDRESS, SUBJECT, CONTENT, ATTACHEMENT, SCHEDULE_ID, STATUS, CODE, MESSAGE) values (?,?,?,?,?,?,?,?,?,?,?,?,?)",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, String.valueOf(mtask.getSeq()));
						ps.setString(2, mtask.getFromName());
						ps.setString(3, mtask.getFromEmail());
						ps.setString(4, mtask.getFromId());
						ps.setString(5, mtask.getToMail());
						ps.setString(6, validAddress);
						ps.setString(7, mtask.getSubject());
						ps.setString(8, mtask.getContent());
						ps.setString(9, mtask.getAttach());
						ps.setInt(10, scheduleId);
						ps.setShort(11, status);
						ps.setShort(12, code);
						ps.setString(13, message);
					}
				});
	}

	/**
	 * save the short message sender task.
	 * 
	 * @param smtask
	 * @param validPhoneNumbers
	 */
	public void saveSMJob(ShortMessageTask smtask, String validPhoneNumbers, Integer scheduleId) {
		this.saveSMJob(smtask, validPhoneNumbers, scheduleId, Constants.SEND_INTI, Constants.PENDING, null);
	}

	public void saveSMJob(ShortMessageTask smtask, String validPhoneNumbers, Integer scheduleId, short status,
			short code, String message) {
		this.jdbcTemplate.update(
				"INSERT INTO MMS_SCHEDULE_JOB (SEQ, FROM_NAME, FROM_ID, TO_LIST, TO_ADDRESS, SUBJECT, CONTENT, ATTACHEMENT, SCHEDULE_ID, STATUS, CODE, MESSAGE) values (?,?,?,?,?,?,?,?,?,?,?,?)",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, String.valueOf(smtask.getMessageId()));
						ps.setString(2, smtask.getStaffName());
						ps.setString(3, smtask.getStaffNo());
						ps.setString(4, smtask.getToMobileNo());
						ps.setString(5, validPhoneNumbers);
						ps.setString(6, smtask.getSubject());
						ps.setString(7, smtask.getContent());
						ps.setString(8, smtask.getAttach());
						ps.setInt(9, scheduleId);
						ps.setShort(10, status);
						ps.setShort(11, code);
						ps.setString(12, message);
					}
				});
	}

	/**
	 * get file suffix list
	 * 
	 * @return fuile suffix list
	 */
	public List<String> getfileSuffixFilterList() {
		return this.getGlobalConfigList(Constants.ATTACHMENT_AS_CONTENT);
	}

	/**
	 * get global configuration by key name
	 * 
	 * @param key
	 *            key name
	 * @return the value of the global configurations by key name
	 */
	public String getGlobalConfig(String key) {
		List<String> codeList = this.getGlobalConfigList(key);
		return codeList == null ? null : codeList.size() == 0 ? null : codeList.get(0);
	}

	/**
	 * get global configuration list by name.
	 * 
	 * @param key
	 *            name
	 * @return the list of the global configuration.
	 */
	public List<String> getGlobalConfigList(String name) {
		return this.jdbcTemplate.query("SELECT CODE FROM MMS_GLOBAL_CONFIGURATION WHERE NAME = ?",
				new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, name);
					}
				}, new RowMapper<String>() {
					@Override
					public String mapRow(ResultSet rs, int rowNum) throws SQLException {
						return rs.getString(Constants.CODE);
					}
				});
	}

	/**
	 * check the action type is exist or not.
	 * 
	 * @param actionType
	 * @return true if exist
	 */
	public Boolean checkActionTypeExist(Integer actionType) {
		return this.jdbcTemplate.query(
				"SELECT * FROM MMS_GLOBAL_CONFIGURATION WHERE NAME = ? AND VAL = ? AND ACTIVE = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, Constants.SCHEDULE_ACTION_TYPE);
						ps.setInt(2, actionType);
						ps.setString(3, Constants.NORMAL_SCHEDULE);
					}
				}, new ResultSetExtractor<Boolean>() {

					@Override
					public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
						return rs.next() == false ? false : rs.getInt(1) >= 1;
					}
				});
	}

	/**
	 * check whether the password must change or not.
	 * 
	 * @return true if the password must change, otherwise is false.
	 */
	public boolean isPasswordExpired() {
		return this.jdbcTemplate.queryForObject("SELECT PASSWORD_IS_EXPIRED FROM USERS { limit 1 }", Boolean.class);
	}

	public List<Schedule> getScheduleListByHandlerIDForMS(Integer handlerId) {
		return this.jdbcTemplate.query("SELECT SCHEDULE.* FROM MMS_SCHEDULE AS SCHEDULE"
				+ " LEFT JOIN MMS_ADVANCED_SEND_OPTION AS OP ON OP.SCHEDULE_ID = SCHEDULE.SCHEDULE_ID" + " WHERE"
				+ " (SCHEDULE.MS_ID = ? OR OP.HANDLER_ID = ? )" + " AND SCHEDULE.ACTION_TYPE = ?"
				+ " AND SCHEDULE.STATUS <> ?", new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, handlerId);
						ps.setInt(2, handlerId);
						ps.setInt(3, Constants.ACTION_MAIL_SCAN);
						ps.setString(4, Constants.N);
					}
				}, new RowMapper<Schedule>() {

					@Override
					public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
						Schedule schedule = new Schedule();
						schedule.setId(rs.getInt(Constants.SCHEDULE_ID));
						schedule.setActionType(rs.getInt(Constants.ACTION_TYPE));
						schedule.setUrl(rs.getString(Constants.URL));
						schedule.setMsid(rs.getString(Constants.MS_ID) == null ? null : rs.getInt(Constants.MS_ID));
						schedule.setDsid(rs.getString(Constants.DS_ID) == null ? null : rs.getInt(Constants.DS_ID));
						schedule.setSmid(rs.getString(Constants.SM_ID) == null ? null : rs.getInt(Constants.SM_ID));
						schedule.setCommand(rs.getString(Constants.COMMAND));
						schedule.setCommandVariable(rs.getString(Constants.COMMAND_VARIABLE));
						schedule.setSqlSentence(rs.getString(Constants.SQL_SENTENCE));
						schedule.setAttachmentAsContent(rs.getBoolean(Constants.ATTACHMENT_AS_CONTENT));
						schedule.setCreateTime(rs.getTimestamp(Constants.CREATE_TIME));
						schedule.setCronExpression(rs.getString(Constants.CRON_EXPRESSION));
						schedule.setDelayTime(
								rs.getString(Constants.DELAY_TIME) == null ? null : rs.getLong(Constants.DELAY_TIME));
						schedule.setDelayDate(rs.getTimestamp(Constants.DELAY_DATE));
						schedule.setDescription(rs.getString(Constants.DESCRIPTION));
						schedule.setDisplayName(rs.getString(Constants.DISPLAY_NAME));
						schedule.setGroupName(rs.getString(Constants.GROUP_NAME));
						schedule.setJobName(rs.getString(Constants.JOB_NAME));
						schedule.setStatus(rs.getString(Constants.STATUS));
						return schedule;
					}
				});
	}

	public List<Schedule> getScheduleListByHandlerIDForSM(Integer handlerId) {
		return this.jdbcTemplate.query("SELECT SCHEDULE.* FROM MMS_SCHEDULE AS SCHEDULE"
				+ " LEFT JOIN MMS_ADVANCED_SEND_OPTION AS OP ON OP.SCHEDULE_ID = SCHEDULE.SCHEDULE_ID" + " WHERE"
				+ " (SCHEDULE.SM_ID = ? OR OP.HANDLER_ID = ? )" + " AND SCHEDULE.ACTION_TYPE = ?"
				+ " AND SCHEDULE.STATUS <> ?", new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, handlerId);
						ps.setInt(2, handlerId);
						ps.setInt(3, Constants.ACTION_SM_SCAN);
						ps.setString(4, Constants.N);
					}
				}, new RowMapper<Schedule>() {

					@Override
					public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
						Schedule schedule = new Schedule();
						schedule.setId(rs.getInt(Constants.SCHEDULE_ID));
						schedule.setActionType(rs.getInt(Constants.ACTION_TYPE));
						schedule.setUrl(rs.getString(Constants.URL));
						schedule.setMsid(rs.getString(Constants.MS_ID) == null ? null : rs.getInt(Constants.MS_ID));
						schedule.setDsid(rs.getString(Constants.DS_ID) == null ? null : rs.getInt(Constants.DS_ID));
						schedule.setSmid(rs.getString(Constants.SM_ID) == null ? null : rs.getInt(Constants.SM_ID));
						schedule.setCommand(rs.getString(Constants.COMMAND));
						schedule.setCommandVariable(rs.getString(Constants.COMMAND_VARIABLE));
						schedule.setSqlSentence(rs.getString(Constants.SQL_SENTENCE));
						schedule.setAttachmentAsContent(rs.getBoolean(Constants.ATTACHMENT_AS_CONTENT));
						schedule.setCreateTime(rs.getTimestamp(Constants.CREATE_TIME));
						schedule.setCronExpression(rs.getString(Constants.CRON_EXPRESSION));
						schedule.setDelayTime(
								rs.getString(Constants.DELAY_TIME) == null ? null : rs.getLong(Constants.DELAY_TIME));
						schedule.setDelayDate(rs.getTimestamp(Constants.DELAY_DATE));
						schedule.setDescription(rs.getString(Constants.DESCRIPTION));
						schedule.setDisplayName(rs.getString(Constants.DISPLAY_NAME));
						schedule.setGroupName(rs.getString(Constants.GROUP_NAME));
						schedule.setJobName(rs.getString(Constants.JOB_NAME));
						schedule.setStatus(rs.getString(Constants.STATUS));
						return schedule;
					}
				});
	}

	/**
	 * get schedule list by mail setup id
	 * 
	 * @param msid
	 * @return
	 */
	public List<Schedule> getScheduleListByMSID(Integer msid) {
		return this.jdbcTemplate.query("SELECT * FROM MMS_SCHEDULE WHERE MS_ID = ? AND STATUS <> ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, msid);
						ps.setString(2, Constants.N);
					}
				}, new RowMapper<Schedule>() {

					@Override
					public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
						Schedule schedule = new Schedule();
						schedule.setId(rs.getInt(Constants.SCHEDULE_ID));
						schedule.setActionType(rs.getInt(Constants.ACTION_TYPE));
						schedule.setUrl(rs.getString(Constants.URL));
						schedule.setMsid(rs.getString(Constants.MS_ID) == null ? null : rs.getInt(Constants.MS_ID));
						schedule.setDsid(rs.getString(Constants.DS_ID) == null ? null : rs.getInt(Constants.DS_ID));
						schedule.setSmid(rs.getString(Constants.SM_ID) == null ? null : rs.getInt(Constants.SM_ID));
						schedule.setCommand(rs.getString(Constants.COMMAND));
						schedule.setCommandVariable(rs.getString(Constants.COMMAND_VARIABLE));
						schedule.setSqlSentence(rs.getString(Constants.SQL_SENTENCE));
						schedule.setAttachmentAsContent(rs.getBoolean(Constants.ATTACHMENT_AS_CONTENT));
						schedule.setCreateTime(rs.getTimestamp(Constants.CREATE_TIME));
						schedule.setCronExpression(rs.getString(Constants.CRON_EXPRESSION));
						schedule.setDelayTime(
								rs.getString(Constants.DELAY_TIME) == null ? null : rs.getLong(Constants.DELAY_TIME));
						schedule.setDelayDate(rs.getTimestamp(Constants.DELAY_DATE));
						schedule.setDescription(rs.getString(Constants.DESCRIPTION));
						schedule.setDisplayName(rs.getString(Constants.DISPLAY_NAME));
						schedule.setGroupName(rs.getString(Constants.GROUP_NAME));
						schedule.setJobName(rs.getString(Constants.JOB_NAME));
						schedule.setStatus(rs.getString(Constants.STATUS));
						return schedule;
					}
				});
	}

	/**
	 * get schedule list by short message id.
	 * 
	 * @param smid
	 * @return
	 */
	public List<Schedule> getScheduleListBySMID(Integer smid) {
		return this.jdbcTemplate.query("SELECT * FROM MMS_SCHEDULE WHERE SM_ID = ? AND STATUS <> ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, smid);
						ps.setString(2, Constants.N);
					}
				}, new RowMapper<Schedule>() {

					@Override
					public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
						Schedule schedule = new Schedule();
						schedule.setId(rs.getInt(Constants.SCHEDULE_ID));
						schedule.setActionType(rs.getInt(Constants.ACTION_TYPE));
						schedule.setUrl(rs.getString(Constants.URL));
						schedule.setMsid(rs.getString(Constants.MS_ID) == null ? null : rs.getInt(Constants.MS_ID));
						schedule.setDsid(rs.getString(Constants.DS_ID) == null ? null : rs.getInt(Constants.DS_ID));
						schedule.setSmid(rs.getString(Constants.SM_ID) == null ? null : rs.getInt(Constants.SM_ID));
						schedule.setCommand(rs.getString(Constants.COMMAND));
						schedule.setCommandVariable(rs.getString(Constants.COMMAND_VARIABLE));
						schedule.setSqlSentence(rs.getString(Constants.SQL_SENTENCE));
						schedule.setAttachmentAsContent(rs.getBoolean(Constants.ATTACHMENT_AS_CONTENT));
						schedule.setCreateTime(rs.getTimestamp(Constants.CREATE_TIME));
						schedule.setCronExpression(rs.getString(Constants.CRON_EXPRESSION));
						schedule.setDelayTime(
								rs.getString(Constants.DELAY_TIME) == null ? null : rs.getLong(Constants.DELAY_TIME));
						schedule.setDelayDate(rs.getTimestamp(Constants.DELAY_DATE));
						schedule.setDescription(rs.getString(Constants.DESCRIPTION));
						schedule.setDisplayName(rs.getString(Constants.DISPLAY_NAME));
						schedule.setGroupName(rs.getString(Constants.GROUP_NAME));
						schedule.setJobName(rs.getString(Constants.JOB_NAME));
						schedule.setStatus(rs.getString(Constants.STATUS));
						return schedule;
					}
				});
	}

	/**
	 * get schedule list by datasource id.
	 * 
	 * @param msid
	 * @return
	 */
	public List<Schedule> getScheduleListByDSID(Integer dsid) {
		return this.jdbcTemplate.query("SELECT * FROM MMS_SCHEDULE WHERE DS_ID = ? AND STATUS <> ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, dsid);
						ps.setString(2, Constants.N);
					}
				}, new RowMapper<Schedule>() {

					@Override
					public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
						Schedule schedule = new Schedule();
						schedule.setId(rs.getInt(Constants.SCHEDULE_ID));
						schedule.setActionType(rs.getInt(Constants.ACTION_TYPE));
						schedule.setUrl(rs.getString(Constants.URL));
						schedule.setMsid(rs.getString(Constants.MS_ID) == null ? null : rs.getInt(Constants.MS_ID));
						schedule.setDsid(rs.getString(Constants.DS_ID) == null ? null : rs.getInt(Constants.DS_ID));
						schedule.setSmid(rs.getString(Constants.SM_ID) == null ? null : rs.getInt(Constants.SM_ID));
						schedule.setCommand(rs.getString(Constants.COMMAND));
						schedule.setCommandVariable(rs.getString(Constants.COMMAND_VARIABLE));
						schedule.setSqlSentence(rs.getString(Constants.SQL_SENTENCE));
						schedule.setAttachmentAsContent(rs.getBoolean(Constants.ATTACHMENT_AS_CONTENT));
						schedule.setCreateTime(rs.getTimestamp(Constants.CREATE_TIME));
						schedule.setCronExpression(rs.getString(Constants.CRON_EXPRESSION));
						schedule.setDelayTime(
								rs.getString(Constants.DELAY_TIME) == null ? null : rs.getLong(Constants.DELAY_TIME));
						schedule.setDelayDate(rs.getTimestamp(Constants.DELAY_DATE));
						schedule.setDescription(rs.getString(Constants.DESCRIPTION));
						schedule.setDisplayName(rs.getString(Constants.DISPLAY_NAME));
						schedule.setGroupName(rs.getString(Constants.GROUP_NAME));
						schedule.setJobName(rs.getString(Constants.JOB_NAME));
						schedule.setStatus(rs.getString(Constants.STATUS));
						return schedule;
					}
				});
	}

	/**
	 * get schedule list by action type and dsid
	 * 
	 * @param actionType
	 * @param dsid
	 * @return
	 */
	public List<Schedule> getScheduleListByActionTypeAndDsId(short actionType, int dsid) {
		return this.jdbcTemplate.query("SELECT * FROM MMS_SCHEDULE WHERE DS_ID = ? AND ACTION_TYPE = ? AND STATUS <> ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, dsid);
						ps.setInt(2, actionType);
						ps.setString(3, Constants.N);
					}
				}, new RowMapper<Schedule>() {

					@Override
					public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
						Schedule schedule = new Schedule();
						schedule.setId(rs.getInt(Constants.SCHEDULE_ID));
						schedule.setActionType(rs.getInt(Constants.ACTION_TYPE));
						schedule.setUrl(rs.getString(Constants.URL));
						schedule.setMsid(rs.getString(Constants.MS_ID) == null ? null : rs.getInt(Constants.MS_ID));
						schedule.setDsid(rs.getString(Constants.DS_ID) == null ? null : rs.getInt(Constants.DS_ID));
						schedule.setSmid(rs.getString(Constants.SM_ID) == null ? null : rs.getInt(Constants.SM_ID));
						schedule.setCommand(rs.getString(Constants.COMMAND));
						schedule.setCommandVariable(rs.getString(Constants.COMMAND_VARIABLE));
						schedule.setSqlSentence(rs.getString(Constants.SQL_SENTENCE));
						schedule.setAttachmentAsContent(rs.getBoolean(Constants.ATTACHMENT_AS_CONTENT));
						schedule.setCreateTime(rs.getTimestamp(Constants.CREATE_TIME));
						schedule.setCronExpression(rs.getString(Constants.CRON_EXPRESSION));
						schedule.setDelayTime(
								rs.getString(Constants.DELAY_TIME) == null ? null : rs.getLong(Constants.DELAY_TIME));
						schedule.setDelayDate(rs.getTimestamp(Constants.DELAY_DATE));
						schedule.setDescription(rs.getString(Constants.DESCRIPTION));
						schedule.setDisplayName(rs.getString(Constants.DISPLAY_NAME));
						schedule.setGroupName(rs.getString(Constants.GROUP_NAME));
						schedule.setJobName(rs.getString(Constants.JOB_NAME));
						schedule.setStatus(rs.getString(Constants.STATUS));
						return schedule;
					}
				});
	}

	/**
	 * get schedule list by status
	 * 
	 * @param status
	 * @return
	 */
	public List<Schedule> getScheduleListByStatus(String status) {

		return this.jdbcTemplate.query("SELECT * FROM MMS_SCHEDULE WHERE STATUS = ?", new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, status);
			}

		}, new RowMapper<Schedule>() {

			@Override
			public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
				Schedule schedule = new Schedule();
				schedule.setId(rs.getInt(Constants.SCHEDULE_ID));
				schedule.setActionType(rs.getInt(Constants.ACTION_TYPE));
				schedule.setUrl(rs.getString(Constants.URL));
				schedule.setMsid(rs.getString(Constants.MS_ID) == null ? null : rs.getInt(Constants.MS_ID));
				schedule.setDsid(rs.getString(Constants.DS_ID) == null ? null : rs.getInt(Constants.DS_ID));
				schedule.setSmid(rs.getString(Constants.SM_ID) == null ? null : rs.getInt(Constants.SM_ID));
				schedule.setCommand(rs.getString(Constants.COMMAND));
				schedule.setCommandVariable(rs.getString(Constants.COMMAND_VARIABLE));
				schedule.setSqlSentence(rs.getString(Constants.SQL_SENTENCE));
				schedule.setAttachmentAsContent(rs.getBoolean(Constants.ATTACHMENT_AS_CONTENT));
				schedule.setCreateTime(rs.getTimestamp(Constants.CREATE_TIME));
				schedule.setCronExpression(rs.getString(Constants.CRON_EXPRESSION));
				schedule.setDelayTime(
						rs.getString(Constants.DELAY_TIME) == null ? null : rs.getLong(Constants.DELAY_TIME));
				schedule.setDelayDate(rs.getTimestamp(Constants.DELAY_DATE));
				schedule.setDescription(rs.getString(Constants.DESCRIPTION));
				schedule.setDisplayName(rs.getString(Constants.DISPLAY_NAME));
				schedule.setGroupName(rs.getString(Constants.GROUP_NAME));
				schedule.setJobName(rs.getString(Constants.JOB_NAME));
				schedule.setStatus(rs.getString(Constants.STATUS));
				return schedule;
			}

		});

	}

	/**
	 * get schedule list need to sync
	 * 
	 * @return
	 */
	public List<Schedule> getSyncScheduleList() {
		return this.getScheduleListByStatus(Constants.Y);
	}

	/**
	 * new or update schedule list
	 * 
	 * @return
	 */
	public List<Schedule> getInitScheduleList() {
		return this.getScheduleListByStatus(Constants.I);
	}

	/**
	 * get normal schedule list
	 * 
	 * @return
	 */
	public List<Schedule> getAllNormalScheduleList() {
		return this.jdbcTemplate.query(
				"SELECT S.* FROM MMS_SCHEDULE AS S, MMS_GLOBAL_CONFIGURATION AS G "
						+ "WHERE S.ACTION_TYPE = G.VAL AND G.ACTIVE = ? AND G.NAME = ? AND S.STATUS <> ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, Constants.NORMAL_SCHEDULE);
						ps.setString(2, Constants.SCHEDULE_ACTION_TYPE);
						ps.setString(3, Constants.N);
					}
				}, new RowMapper<Schedule>() {

					@Override
					public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
						Schedule schedule = new Schedule();
						schedule.setId(rs.getInt(Constants.SCHEDULE_ID));
						schedule.setActionType(rs.getInt(Constants.ACTION_TYPE));
						schedule.setUrl(rs.getString(Constants.URL));
						schedule.setMsid(rs.getString(Constants.MS_ID) == null ? null : rs.getInt(Constants.MS_ID));
						schedule.setDsid(rs.getString(Constants.DS_ID) == null ? null : rs.getInt(Constants.DS_ID));
						schedule.setSmid(rs.getString(Constants.SM_ID) == null ? null : rs.getInt(Constants.SM_ID));
						schedule.setCommand(rs.getString(Constants.COMMAND));
						schedule.setCommandVariable(rs.getString(Constants.COMMAND_VARIABLE));
						schedule.setSqlSentence(rs.getString(Constants.SQL_SENTENCE));
						schedule.setAttachmentAsContent(rs.getBoolean(Constants.ATTACHMENT_AS_CONTENT));
						schedule.setCreateTime(rs.getTimestamp(Constants.CREATE_TIME));
						schedule.setCronExpression(rs.getString(Constants.CRON_EXPRESSION));
						schedule.setDelayTime(
								rs.getString(Constants.DELAY_TIME) == null ? null : rs.getLong(Constants.DELAY_TIME));
						schedule.setDelayDate(rs.getTimestamp(Constants.DELAY_DATE));
						schedule.setDescription(rs.getString(Constants.DESCRIPTION));
						schedule.setDisplayName(rs.getString(Constants.DISPLAY_NAME));
						schedule.setGroupName(rs.getString(Constants.GROUP_NAME));
						schedule.setJobName(rs.getString(Constants.JOB_NAME));
						schedule.setStatus(rs.getString(Constants.STATUS));
						return schedule;
					}

				});
	}

	/**
	 * get all schedule list
	 * 
	 * @return
	 */
	public List<Schedule> getAllScheduleList() {
		return this.jdbcTemplate
				.query("SELECT SCHEDULE.*, CONFIG.TITLE AS ACTION_DISPLAY_NAME, CONFIG.ACTIVE AS SCHEDULE_TYPE"
						+ " FROM MMS_SCHEDULE AS SCHEDULE"
						+ " LEFT JOIN MMS_GLOBAL_CONFIGURATION AS CONFIG ON CONFIG.VAL = SCHEDULE.ACTION_TYPE AND CONFIG.NAME = ?"
						+ " WHERE SCHEDULE.STATUS <> ?", new PreparedStatementSetter() {

							@Override
							public void setValues(PreparedStatement ps) throws SQLException {
								ps.setString(1, Constants.SCHEDULE_ACTION_TYPE);
								ps.setString(2, Constants.N);
							}
						}, new RowMapper<Schedule>() {

							@Override
							public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
								Schedule schedule = new Schedule();
								schedule.setId(rs.getInt(Constants.SCHEDULE_ID));
								schedule.setActionType(rs.getInt(Constants.ACTION_TYPE));
								schedule.setActionDisplayName(rs.getString(Constants.ACTION_DISPLAY_NAME));
								schedule.setScheduleType(rs.getString(Constants.SCHEDULE_TYPE));
								schedule.setUrl(rs.getString(Constants.URL));
								schedule.setMsid(
										rs.getString(Constants.MS_ID) == null ? null : rs.getInt(Constants.MS_ID));
								schedule.setDsid(
										rs.getString(Constants.DS_ID) == null ? null : rs.getInt(Constants.DS_ID));
								schedule.setSmid(
										rs.getString(Constants.SM_ID) == null ? null : rs.getInt(Constants.SM_ID));
								schedule.setCommand(rs.getString(Constants.COMMAND));
								schedule.setCommandVariable(rs.getString(Constants.COMMAND_VARIABLE));
								schedule.setSqlSentence(rs.getString(Constants.SQL_SENTENCE));
								schedule.setAttachmentAsContent(rs.getBoolean(Constants.ATTACHMENT_AS_CONTENT));
								schedule.setCronExpression(rs.getString(Constants.CRON_EXPRESSION));
								schedule.setDelayTime(rs.getString(Constants.DELAY_TIME) == null ? null
										: rs.getLong(Constants.DELAY_TIME));
								schedule.setDelayDate(rs.getTimestamp(Constants.DELAY_DATE));
								schedule.setDescription(rs.getString(Constants.DESCRIPTION));
								schedule.setDisplayName(rs.getString(Constants.DISPLAY_NAME));
								schedule.setGroupName(rs.getString(Constants.GROUP_NAME));
								schedule.setJobName(rs.getString(Constants.JOB_NAME));
								schedule.setStatus(rs.getString(Constants.STATUS));
								schedule.setUpdateTime(rs.getTimestamp(Constants.UPDATE_TIME));
								schedule.setCreateTime(rs.getTimestamp(Constants.CREATE_TIME));
								return schedule;
							}

						});
	}

	/**
	 * remove dsid from schedule
	 * 
	 * @param id
	 * @param status
	 */
	public void removeScheduleDSID(int id) {
		this.jdbcTemplate.update(
				"UPDATE MMS_SCHEDULE SET DS_ID = ?, UPDATE_TIME = ? WHERE SCHEDULE_ID = ? AND STATUS = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setNull(1, Types.INTEGER);
						ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
						ps.setInt(3, id);
						ps.setString(4, Constants.Y);
					}
				});
	}

	/**
	 * remove msid from schedule
	 * 
	 * @param id
	 * @param status
	 */
	public void removeScheduleMSID(int id) {
		this.jdbcTemplate.update(
				"UPDATE MMS_SCHEDULE SET MS_ID = ?, UPDATE_TIME = ? WHERE SCHEDULE_ID = ? AND STATUS = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setNull(1, Types.INTEGER);
						ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
						ps.setInt(3, id);
						ps.setString(4, Constants.Y);
					}
				});
	}

	/**
	 * remove handler id if the relation configuration has removed.
	 * 
	 * @param handlerId
	 * @param scheduleId
	 */
	public void removeScheduleHandlerID(int handlerId, int scheduleId) {
		this.jdbcTemplate.update(
				"UPDATE MMS_ADVANCED_SEND_OPTION SET HANDLER_ID = ?, UPDATE_TIME = ? WHERE HANDLER_ID = ? AND SCHEDULE_ID = ? AND STATUS = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setNull(1, Types.INTEGER);
						ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
						ps.setInt(3, handlerId);
						ps.setInt(4, scheduleId);
						ps.setString(5, Constants.Y);
					}
				});
	}

	/**
	 * remove sm id from schedule by schedule id
	 * 
	 * @param id
	 */
	public void removeScheduleSMID(int id) {
		this.jdbcTemplate.update(
				"UPDATE MMS_SCHEDULE SET SM_ID = ?, UPDATE_TIME = ? WHERE SCHEDULE_ID = ? AND STATUS = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setNull(1, Types.INTEGER);
						ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
						ps.setInt(3, id);
						ps.setString(4, Constants.Y);
					}
				});
	}

	/**
	 * change the schedule status
	 * 
	 * @param id
	 * @param status
	 */
	public void setScheduleStatus(int id, String status) {
		this.jdbcTemplate.update("UPDATE MMS_SCHEDULE SET STATUS = ?, UPDATE_TIME = ? WHERE SCHEDULE_ID = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, status);
						ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
						ps.setInt(3, id);
					}
				});
	}

	/**
	 * get schedule task type list
	 * 
	 * @return
	 */
	public List<GlobalConfig> getScheduleActionTypeList() {
		return this.getGlobalConfigurationListByName(Constants.SCHEDULE_ACTION_TYPE);
	}

	/**
	 * get all normal schedule task type list
	 * 
	 * @return
	 */
	public List<GlobalConfig> getNormalScheduleActionTypeList() {
		return this.getGlobalConfigurationListByName(Constants.SCHEDULE_ACTION_TYPE, Constants.NORMAL_SCHEDULE);
	}

	/**
	 * get schedule last input data list
	 * 
	 * @return
	 */
	public List<GlobalConfig> getLastInputDataList() {
		return this.getGlobalConfigurationListByName(Constants.SCHEDULE_MMS_LAST_INPUT, Constants.Y);
	}

	/**
	 * get global configuration list by name
	 * 
	 * @param name
	 * @return
	 */
	public List<GlobalConfig> getGlobalConfigurationListByName(String name) {
		return this.getGlobalConfigurationListByName(name, Constants.Y);
	}

	/**
	 * set the last input data
	 * 
	 * @param data
	 * @param title
	 */
	public void setLastInputData(String data, String title) {
		this.jdbcTemplate.update(
				"UPDATE MMS_GLOBAL_CONFIGURATION SET CONTENT = ?, UPDATE_TIME = ? WHERE NAME = ? AND TITLE = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, data);
						ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
						ps.setString(3, Constants.SCHEDULE_MMS_LAST_INPUT);
						ps.setString(4, title);
					}
				});
	}

	/**
	 * get global configuration list by name and active.
	 * 
	 * @param name
	 * @param active
	 * @return
	 */
	public List<GlobalConfig> getGlobalConfigurationListByName(String name, String active) {
		return this.jdbcTemplate.query("SELECT * FROM MMS_GLOBAL_CONFIGURATION WHERE NAME = ? AND ACTIVE = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, name);
						ps.setString(2, active);
					}
				}, new RowMapper<GlobalConfig>() {

					@Override
					public GlobalConfig mapRow(ResultSet rs, int rowNum) throws SQLException {
						GlobalConfig globalConfig = new GlobalConfig();
						globalConfig.setId(rs.getInt(Constants.CONFIG_ID));
						globalConfig.setName(rs.getString(Constants.NAME));
						globalConfig.setCode(rs.getString(Constants.CODE));
						globalConfig.setVal(rs.getString(Constants.VAL) == null ? null : rs.getInt(Constants.VAL));
						globalConfig.setTitle(rs.getString(Constants.TITLE));
						globalConfig.setContent(rs.getString(Constants.CONTENT));
						globalConfig.setDescription(rs.getString(Constants.DESCRIPTION));
						globalConfig.setActive(rs.getString(Constants.ACTIVE));
						globalConfig.setCreateTime(rs.getTimestamp(Constants.CREATE_TIME));
						globalConfig.setUpdateTime(rs.getTimestamp(Constants.UPDATE_TIME));
						return globalConfig;
					}
				});
	}

	/**
	 * get schedule task type object by value.
	 * 
	 * @param value
	 * @return
	 */
	public GlobalConfig getScheduleActionTypeByVal(Integer value) {
		return this.getGlobalConfigurationByNameAndVal(Constants.SCHEDULE_ACTION_TYPE, value);
	}

	/**
	 * get global configuration by name and val
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public GlobalConfig getGlobalConfigurationByNameAndVal(String name, Integer value) {
		return this.jdbcTemplate.query("SELECT * FROM MMS_GLOBAL_CONFIGURATION WHERE NAME = ? AND VAL = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, name);
						ps.setInt(2, value);
					}
				}, new ResultSetExtractor<GlobalConfig>() {

					@Override
					public GlobalConfig extractData(ResultSet rs) throws SQLException, DataAccessException {
						if (rs.next()) {
							GlobalConfig GlobalConfig = new GlobalConfig();
							GlobalConfig.setId(rs.getInt(Constants.CONFIG_ID));
							GlobalConfig.setActive(rs.getString(Constants.ACTIVE));
							GlobalConfig.setCode(rs.getString(Constants.CODE));
							GlobalConfig.setCreateTime(rs.getTimestamp(Constants.CREATE_TIME));
							GlobalConfig.setDescription(rs.getString(Constants.DESCRIPTION));
							GlobalConfig.setName(rs.getString(Constants.NAME));
							GlobalConfig.setTitle(rs.getString(Constants.TITLE));
							GlobalConfig.setUpdateTime(rs.getTimestamp(Constants.UPDATE_TIME));
							GlobalConfig.setVal(rs.getObject(Constants.VAL) == null ? null : rs.getInt(Constants.VAL));
							return GlobalConfig;
						}
						return null;
					}
				});
	}

	/**
	 * get send condition list
	 * 
	 * @return
	 */
	public List<SendConditionOption> getSendConditionOptionList() {
		return this.jdbcTemplate.query(
				"SELECT TITLE, CONTENT, CODE, VAL, DESCRIPTION FROM MMS_GLOBAL_CONFIGURATION WHERE NAME = ? ORDER BY SEQUENCE",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, Constants.SEND_CONDITION_OPTION);
					}
				}, new RowMapper<SendConditionOption>() {

					@Override
					public SendConditionOption mapRow(ResultSet rs, int rowNum) throws SQLException {
						SendConditionOption sco = new SendConditionOption();
						sco.setName(rs.getString(Constants.TITLE));
						sco.setValue(rs.getObject(Constants.VAL) == null ? null : rs.getInt(Constants.VAL));
						sco.setCode(rs.getString(Constants.CODE));
						sco.setParentCode(rs.getString(Constants.CONTENT));
						sco.setDescription(rs.getString(Constants.DESCRIPTION));
						return sco;
					}
				});
	}

	/**
	 * get option configuration by option id
	 * 
	 * @param optionId
	 * @return
	 */
	public SendConditionOption getSendConditionOption(Integer optionId) {
		return this.jdbcTemplate.query(
				"SELECT TITLE, CODE, VAL, DESCRIPTION FROM MMS_GLOBAL_CONFIGURATION WHERE VAL = ? AND NAME = ? AND ORDER BY SEQUENCE",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, optionId);
						ps.setString(2, Constants.SEND_CONDITION_OPTION);
					}
				}, new ResultSetExtractor<SendConditionOption>() {

					@Override
					public SendConditionOption extractData(ResultSet rs) throws SQLException, DataAccessException {
						if (rs.next()) {
							SendConditionOption sco = new SendConditionOption();
							sco.setName(rs.getString(Constants.TITLE));
							sco.setValue(rs.getObject(Constants.VAL) == null ? null : rs.getInt(Constants.VAL));
							sco.setCode(rs.getString(Constants.CODE));
							sco.setDescription(rs.getString(Constants.DESCRIPTION));
							return sco;
						}
						return null;
					}
				});
	}

	/**
	 * get send condition operation list
	 * 
	 * @param type
	 * @return
	 */
	public List<SendConditionOption> getSendConditionOperationList() {
		return this.jdbcTemplate.query(
				"SELECT G.TITLE AS TITLE, G.VAL AS VAL, G.CODE AS CODE, G.DESCRIPTION AS DESCRIPTION, P.CODE AS PARENTCODE"
						+ " FROM MMS_GLOBAL_CONFIGURATION AS G"
						+ " LEFT JOIN MMS_GLOBAL_CONFIGURATION AS R ON R.VAL = G.VAL AND R.NAME = ?"
						+ " LEFT JOIN MMS_GLOBAL_CONFIGURATION AS P ON P.CODE = R.CODE AND P.NAME = ?"
						+ " WHERE G.NAME = ? AND P.CODE IS NOT NULL ORDER BY R.SEQUENCE",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, Constants.SEND_CONDITION_OPTION_OPERATION);
						ps.setString(2, Constants.SEND_CONDITION_OPTION);
						ps.setString(3, Constants.SEND_CONDITION_OPERATION);
					}
				}, new RowMapper<SendConditionOption>() {

					@Override
					public SendConditionOption mapRow(ResultSet rs, int rowNum) throws SQLException {
						SendConditionOption sco = new SendConditionOption();
						sco.setName(rs.getString(Constants.TITLE));
						sco.setValue(rs.getInt(Constants.VAL));
						sco.setCode(rs.getString(Constants.CODE));
						sco.setDescription(rs.getString(Constants.DESCRIPTION));
						sco.setParentCode(rs.getString("PARENTCODE"));
						return sco;
					}
				});
	}

	/**
	 * get operation configuration by option id and operation id
	 */
	public SendConditionOption getSendConditionOperation(Integer optionId, Integer operationId) {
		return this.jdbcTemplate.query(
				"SELECT G.TITLE AS TITLE, G.VAL AS VAL, G.CODE AS CODE, G.DESCRIPTION AS DESCRIPTION, P.CODE AS PARENTCODE"
						+ " FROM MMS_GLOBAL_CONFIGURATION AS G"
						+ " LEFT JOIN MMS_GLOBAL_CONFIGURATION AS R ON R.VAL = G.VAL AND R.NAME = ?"
						+ " LEFT JOIN MMS_GLOBAL_CONFIGURATION AS P ON P.CODE = R.CODE AND P.NAME = ?"
						+ " WHERE G.VAL = ? AND P.VAL = ? AND G.NAME = ? ORDER BY G.SEQUENCE",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, Constants.SEND_CONDITION_OPTION_OPERATION);
						ps.setString(2, Constants.SEND_CONDITION_OPTION);
						ps.setInt(3, operationId);
						ps.setInt(4, optionId);
						ps.setString(5, Constants.SEND_CONDITION_OPERATION);
					}
				}, new ResultSetExtractor<SendConditionOption>() {

					@Override
					public SendConditionOption extractData(ResultSet rs) throws SQLException, DataAccessException {
						if (rs.next()) {
							SendConditionOption sco = new SendConditionOption();
							sco.setName(rs.getString(Constants.TITLE));
							sco.setValue(rs.getInt(Constants.VAL));
							sco.setCode(rs.getString(Constants.CODE));
							sco.setDescription(rs.getString(Constants.DESCRIPTION));
							sco.setParentCode(rs.getString("PARENTCODE"));
							return sco;
						}
						return null;
					}
				});
	}

	/**
	 * update schedule by schedule instance
	 * 
	 * @param schedule
	 */
	public void updateSchedule(Schedule schedule) {
		this.jdbcTemplate.update(
				"UPDATE MMS_SCHEDULE SET DISPLAY_NAME = ?, URL = ?, MS_ID = ?, DS_ID = ?, SM_ID = ?, ATTACHMENT_AS_CONTENT = ?, COMMAND = ?, COMMAND_VARIABLE = ?, SQL_SENTENCE = ?, CRON_EXPRESSION = ?, DELAY_TIME = ?, DELAY_DATE = ?, DESCRIPTION = ?, STATUS = ?, UPDATE_TIME = ? WHERE SCHEDULE_ID = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, schedule.getDisplayName());
						ps.setString(2, schedule.getUrl());
						ps.setObject(3, schedule.getMsid());
						ps.setObject(4, schedule.getDsid());
						ps.setObject(5, schedule.getSmid());
						ps.setBoolean(6,
								schedule.isAttachmentAsContent() == null ? false : schedule.isAttachmentAsContent());
						ps.setString(7, schedule.getCommand());
						ps.setString(8, schedule.getCommandVariable());
						ps.setString(9, schedule.getSqlSentence());
						ps.setString(10, schedule.getCronExpression());
						ps.setLong(11, schedule.getDelayTime() == null ? 0 : schedule.getDelayTime());
						ps.setTimestamp(12, schedule.getDelayDate());
						ps.setString(13, schedule.getDescription());
						ps.setString(14, schedule.getStatus());
						ps.setTimestamp(15, new Timestamp(System.currentTimeMillis()));
						ps.setInt(16, schedule.getId());
					}
				});
	}

	/**
	 * batch update schedule job status
	 * 
	 * @param batchJobList
	 */
	public void updateScheduleJobStatus(List<? extends BatchJob> batchJobList) {
		// update locale job status.
		String sql = "UPDATE MMS_SCHEDULE_JOB SET FROM_ADDRESS = ?, STATUS = ?, SEND_TIME = ?, CODE = ?, MESSAGE = ?, GATEWAY_ID = ? WHERE JOB_ID = ?";
		this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, batchJobList.get(i).getFromAddress());
				ps.setInt(2, batchJobList.get(i).getStatus());
				ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
				ps.setShort(4, batchJobList.get(i).getCode());
				ps.setString(5, batchJobList.get(i).getMessage());
				ps.setString(6, batchJobList.get(i).getGatewayId());
				ps.setLong(7, batchJobList.get(i).getJobId());
			}

			@Override
			public int getBatchSize() {
				return batchJobList.size();
			}
		});
	}

	/**
	 * create a new schedule.
	 * 
	 * @param schedule
	 */
	public int saveSchedule(Schedule schedule) {
		String sql = "INSERT INTO MMS_SCHEDULE (DISPLAY_NAME, JOB_NAME, ACTION_TYPE, URL, MS_ID, DS_ID, SM_ID, ATTACHMENT_AS_CONTENT, COMMAND, COMMAND_VARIABLE, SQL_SENTENCE, CRON_EXPRESSION, DELAY_TIME, DELAY_DATE, DESCRIPTION, STATUS) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setString(1, schedule.getDisplayName());
				ps.setString(2, schedule.getJobName());
				ps.setInt(3, schedule.getActionType());
				ps.setString(4, schedule.getUrl());
				ps.setObject(5, schedule.getMsid());
				ps.setObject(6, schedule.getDsid());
				ps.setObject(7, schedule.getSmid());
				ps.setBoolean(8, schedule.isAttachmentAsContent() == null ? false : schedule.isAttachmentAsContent());
				ps.setString(9, schedule.getCommand());
				ps.setString(10, schedule.getCommandVariable());
				ps.setString(11, schedule.getSqlSentence());
				ps.setString(12, schedule.getCronExpression());
				ps.setLong(13, schedule.getDelayTime() == null ? 0 : schedule.getDelayTime());
				ps.setTimestamp(14, schedule.getDelayDate());
				ps.setString(15, schedule.getDescription());
				ps.setString(16, schedule.getStatus());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}

	/**
	 * create a new schedule log
	 * 
	 * @param scheduleId
	 * @param status
	 * @param timecount
	 * @param message
	 */
	public void createScheduleLog(Integer scheduleId, String status, Integer timecount, String message) {
		this.jdbcTemplate.update(
				"INSERT INTO MMS_SCHEDULE_LOG (SCHEDULE_ID, STATUS, RUNTIME_COUNT, MESSAGE) VALUES ( ?, ?, ?, ?)",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setLong(1, scheduleId);
						ps.setString(2, status);
						ps.setInt(3, timecount);
						ps.setString(4,
								message == null ? null
										: message.length() > 1000 ? StringUtils.concat("...", StringUtils
												.substring(message, message.length() - 997, message.length()))
												: message);
					}
				});
	}

	/**
	 * get schedule by schedule id
	 * 
	 * @param id
	 * @return
	 */
	public Schedule getScheduleById(int id) {
		return this.jdbcTemplate.query("SELECT * FROM MMS_SCHEDULE WHERE SCHEDULE_ID = ? AND STATUS <> ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, id);
						ps.setString(2, Constants.N);
					}
				}, new ResultSetExtractor<Schedule>() {

					@Override
					public Schedule extractData(ResultSet rs) throws SQLException, DataAccessException {
						if (rs.next()) {
							Schedule schedule = new Schedule();
							schedule.setId(rs.getInt(Constants.SCHEDULE_ID));
							schedule.setActionType(rs.getInt(Constants.ACTION_TYPE));
							schedule.setUrl(rs.getString(Constants.URL));
							schedule.setMsid(rs.getString(Constants.MS_ID) == null ? null : rs.getInt(Constants.MS_ID));
							schedule.setDsid(rs.getString(Constants.DS_ID) == null ? null : rs.getInt(Constants.DS_ID));
							schedule.setCommand(rs.getString(Constants.COMMAND));
							schedule.setCommandVariable(rs.getString(Constants.COMMAND_VARIABLE));
							schedule.setSqlSentence(rs.getString(Constants.SQL_SENTENCE));
							schedule.setAttachmentAsContent(rs.getBoolean(Constants.ATTACHMENT_AS_CONTENT));
							schedule.setCreateTime(rs.getTimestamp(Constants.CREATE_TIME));
							schedule.setCronExpression(rs.getString(Constants.CRON_EXPRESSION));
							schedule.setDelayTime(rs.getString(Constants.DELAY_TIME) == null ? null
									: rs.getLong(Constants.DELAY_TIME));
							schedule.setDelayDate(rs.getTimestamp(Constants.DELAY_DATE));
							schedule.setDescription(rs.getString(Constants.DESCRIPTION));
							schedule.setDisplayName(rs.getString(Constants.DISPLAY_NAME));
							schedule.setGroupName(rs.getString(Constants.GROUP_NAME));
							schedule.setJobName(rs.getString(Constants.JOB_NAME));
							schedule.setStatus(rs.getString(Constants.STATUS));
							return schedule;
						}
						return null;
					}
				});

	}

	/**
	 * get running schedule count
	 * 
	 * @return
	 */
	public Integer getRunningScheduleCount() {
		return this.jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MMS_SCHEDULE WHERE STATUS = 'Y'", Integer.class);
	}

	public Integer getAllNormalScheduleCount() {
		return this.jdbcTemplate.query(
				"SELECT COUNT(*) FROM MMS_SCHEDULE AS S LEFT JOIN MMS_GLOBAL_CONFIGURATION AS G "
						+ "ON S.ACTION_TYPE = G.VAL WHERE G.ACTIVE = ? AND G.NAME = ? AND S.STATUS = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, Constants.NORMAL_SCHEDULE);
						ps.setString(2, Constants.SCHEDULE_ACTION_TYPE);
						ps.setString(3, Constants.Y);
					}
				}, new ResultSetExtractor<Integer>() {

					@Override
					public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
						return rs.next() == false ? 0 : rs.getInt(1);
					}
				});
	}

	/**
	 * get executing schedule count
	 * 
	 * @return
	 */
	public Integer getNormalScheduleLogExecuteCount() {
		return this.getNormalScheduleLogExecuteCount(new Timestamp(-1));
	}

	/**
	 * get executing schedule count after a special time
	 * 
	 * @return
	 */
	public Integer getNormalScheduleLogExecuteCount(Timestamp from) {
		return this.getNormalScheduleLogExecuteCount(from, new Timestamp(System.currentTimeMillis()));
	}

	/**
	 * get executing schedule count before a special time
	 * 
	 * @return
	 */
	public Integer getNormalScheduleLogExecuteCountBeforeTime(Timestamp to) {
		return this.getNormalScheduleLogExecuteCount(new Timestamp(-1), to);
	}

	/**
	 * get executing schedule count in a time space
	 * 
	 * @return
	 */
	public Integer getNormalScheduleLogExecuteCount(Timestamp from, Timestamp to) {
		return this.jdbcTemplate.query(
				"SELECT COUNT(*) FROM MMS_SCHEDULE_LOG AS L"
						+ " LEFT JOIN MMS_SCHEDULE AS S ON L.SCHEDULE_ID = S.SCHEDULE_ID "
						+ " LEFT JOIN MMS_GLOBAL_CONFIGURATION AS G ON S.ACTION_TYPE = G.VAL"
						+ " WHERE S.STATUS <> ? AND G.ACTIVE = ? AND G.NAME = ? AND L.EXECUTION_TIME >= ? AND L.EXECUTION_TIME <= ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, Constants.N);
						ps.setString(2, Constants.NORMAL_SCHEDULE);
						ps.setString(3, Constants.SCHEDULE_ACTION_TYPE);
						ps.setTimestamp(4, from == null ? new Timestamp(-1) : from);
						ps.setTimestamp(5, to == null ? new Timestamp(System.currentTimeMillis()) : to);
					}
				}, new ResultSetExtractor<Integer>() {

					@Override
					public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
						return rs.next() == false ? 0 : rs.getInt(1);
					}
				});
	}

	/**
	 * get executing schedule count by schedule id.
	 * 
	 * @param id
	 * @return
	 */
	public Integer getScheduleLogExecuteCount(Integer id) {
		return this.getScheduleLogExecuteCount(id, new Timestamp(-1));
	}

	/**
	 * get executing schedule count by schedule id after a special time
	 * 
	 * @param id
	 * @param from
	 * @return
	 */
	public Integer getScheduleLogExecuteCount(Integer id, Timestamp from) {
		return this.getScheduleLogExecuteCount(id, from, new Timestamp(System.currentTimeMillis()));
	}

	/**
	 * get executing schedule count by schedule id before a special time
	 * 
	 * @param id
	 * @param from
	 * @return
	 */
	public Integer getScheduleLogExecuteCountBeforeTime(Integer id, Timestamp to) {
		return this.getScheduleLogExecuteCount(id, new Timestamp(-1), to);
	}

	/**
	 * get executing schedule count by schedule id in a time spaces
	 * 
	 * @param id
	 * @param from
	 * @param to
	 * @return
	 */
	public Integer getScheduleLogExecuteCount(Integer id, Timestamp from, Timestamp to) {
		Object[] args = { id, from == null ? new Timestamp(-1) : from,
				to == null ? new Timestamp(System.currentTimeMillis()) : to };
		return this.jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM MMS_SCHEDULE_LOG WHERE SCHEDULE_ID = ? AND EXECUTION_TIME >= ? and EXECUTION_TIME <= ?",
				args, Integer.class);
	}

	/**
	 * get success schedule count
	 * 
	 * @return
	 */
	public Integer getNormalScheduleLogSuccessCount() {
		return this.getNormalScheduleLogCountByStatus(Constants.Y);
	}

	/**
	 * get success schedule count by schedule id.
	 * 
	 * @return
	 */
	public Integer getScheduleLogSuccessCount(Integer id) {
		return this.getScheduleLogCountByStatus(id, Constants.Y);
	}

	/**
	 * get success schedule count by schedule id after a special time
	 * 
	 * @return
	 */
	public Integer getScheduleLogSuccessCount(Integer id, Timestamp from) {
		return this.getScheduleLogCountByStatus(id, Constants.Y, from);
	}

	/**
	 * get success schedule log count by schedule id before a special time
	 * 
	 * @return
	 */
	public Integer getScheduleLogSuccessCountBeforeTime(Integer id, Timestamp to) {
		return this.getScheduleLogCountByStatusBeforeTime(id, Constants.Y, to);
	}

	/**
	 * get success schedule log count by schedule id in a time space
	 * 
	 * @return
	 */
	public Integer getScheduleLogSuccessCount(Integer id, Timestamp from, Timestamp to) {
		return this.getScheduleLogCountByStatus(id, Constants.Y, from, to);
	}

	/**
	 * get normal schedule failed log count
	 * 
	 * @return
	 */
	public Integer getNormalScheduleLogFailedCount() {
		return this.getNormalScheduleLogCountByStatus(Constants.N);
	}

	/**
	 * get normal schedule failed log count by schedule id.
	 * 
	 * @return
	 */
	public Integer getScheduleLogFailedCount(Integer id) {
		return this.getScheduleLogCountByStatus(id, Constants.N);
	}

	/**
	 * get failed schedule log count by schedule id after a special time
	 * 
	 * @return
	 */
	public Integer getScheduleLogFailedCount(Integer id, Timestamp from) {
		return this.getScheduleLogCountByStatus(id, Constants.N, from);
	}

	/**
	 * get failed schedule log count by schedule id before a special time
	 * 
	 * @return
	 */
	public Integer getScheduleLogFailedCountBeforeTime(Integer id, Timestamp to) {
		return this.getScheduleLogCountByStatusBeforeTime(id, Constants.N, to);
	}

	/**
	 * get failed schedule log count by schedule id in a time space
	 * 
	 * @return
	 */
	public Integer getScheduleLogFailedCount(Integer id, Timestamp from, Timestamp to) {
		return this.getScheduleLogCountByStatus(id, Constants.N, from, to);
	}

	/**
	 * get normal schedule log count by status
	 * 
	 * @return
	 */
	public Integer getNormalScheduleLogCountByStatus(String status) {
		return this.getNormalScheduleLogCountByStatus(status, null);
	}

	/**
	 * get normal schedule log count by status after a special time
	 * 
	 * @return
	 */
	public Integer getNormalScheduleLogCountByStatus(String status, Timestamp from) {
		return this.getNormalScheduleLogCountByStatus(status, from, new Timestamp(System.currentTimeMillis()));
	}

	/**
	 * get normal schedule log count by status after a special time
	 * 
	 * @return
	 */
	public Integer getNormalScheduleLogCountByStatusBeforeTime(String status, Timestamp to) {
		return this.getNormalScheduleLogCountByStatus(status, new Timestamp(-1), to);
	}

	/**
	 * get normal schedule log count by status in a time space
	 * 
	 * @return
	 */
	public Integer getNormalScheduleLogCountByStatus(String status, Timestamp from, Timestamp to) {
		return this.jdbcTemplate.query(
				"SELECT COUNT(*) FROM MMS_SCHEDULE_LOG AS L"
						+ " LEFT JOIN MMS_SCHEDULE AS S ON L.SCHEDULE_ID = S.SCHEDULE_ID "
						+ " LEFT JOIN MMS_GLOBAL_CONFIGURATION AS G ON S.ACTION_TYPE = G.VAL"
						+ " WHERE L.STATUS = ? AND S.STATUS <> ? AND G.ACTIVE = ? AND G.NAME = ? AND L.EXECUTION_TIME >= ? AND L.EXECUTION_TIME <= ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, status);
						ps.setString(2, Constants.N);
						ps.setString(3, Constants.NORMAL_SCHEDULE);
						ps.setString(4, Constants.SCHEDULE_ACTION_TYPE);
						ps.setTimestamp(5, from == null ? new Timestamp(-1) : from);
						ps.setTimestamp(6, to == null ? new Timestamp(System.currentTimeMillis()) : to);
					}
				}, new ResultSetExtractor<Integer>() {

					@Override
					public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
						return rs.next() == false ? 0 : rs.getInt(1);
					}
				});
	}

	/**
	 * get schedule log count by id and status
	 * 
	 * @return
	 */
	public Integer getScheduleLogCountByStatus(Integer id, String status) {
		return this.getScheduleLogCountByStatus(id, status, null);
	}

	/**
	 * get schedule log count by id and status after a special time
	 * 
	 * @return
	 */
	public Integer getScheduleLogCountByStatus(Integer id, String status, Timestamp from) {
		return this.getScheduleLogCountByStatus(id, status, from, new Timestamp(System.currentTimeMillis()));
	}

	/**
	 * get schedule log count by id and status before a special time
	 * 
	 * @return
	 */
	public Integer getScheduleLogCountByStatusBeforeTime(Integer id, String status, Timestamp to) {
		return this.getScheduleLogCountByStatus(id, status, new Timestamp(-1), to);
	}

	/**
	 * get schedule log count by id and status in a time space
	 * 
	 * @return
	 */
	public Integer getScheduleLogCountByStatus(Integer id, String status, Timestamp from, Timestamp to) {
		Object[] args = { id, status, from == null ? new Timestamp(-1) : from,
				to == null ? new Timestamp(System.currentTimeMillis()) : to };
		return this.jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM MMS_SCHEDULE_LOG WHERE SCHEDULE_ID = ? AND STATUS = ? AND EXECUTION_TIME >= ? AND EXECUTION_TIME <= ?",
				args, Integer.class);
	}

	/**
	 * get schedule log statistics count
	 * 
	 * @param status
	 * @param from
	 * @return
	 */
	public Map<Integer, Integer> getScheduleLogExecuteCountStatistics() {
		return this.getScheduleLogExecuteCountStatistics(new Timestamp(-1));
	}

	/**
	 * get schedule log statistics count after a special time
	 * 
	 * @param status
	 * @param from
	 * @return
	 */
	public Map<Integer, Integer> getScheduleLogExecuteCountStatistics(Timestamp from) {
		return this.getScheduleLogExecuteCountStatistics(from, new Timestamp(System.currentTimeMillis()));
	}

	/**
	 * get schedule log statistics count before a special time
	 * 
	 * @param status
	 * @param from
	 * @return
	 */
	public Map<Integer, Integer> getScheduleLogExecuteCountStatisticsBeforeTime(Timestamp to) {
		return this.getScheduleLogExecuteCountStatistics(new Timestamp(-1), to);
	}

	/**
	 * get schedule log statistics count in a time space.
	 * 
	 * @param status
	 * @param from
	 * @return
	 */
	public Map<Integer, Integer> getScheduleLogExecuteCountStatistics(Timestamp from, Timestamp to) {
		return this.jdbcTemplate.query(
				"SELECT SCHEDULE_ID, COUNT(*) FROM MMS_SCHEDULE_LOG WHERE 1 = 1 AND EXECUTION_TIME >= ? AND EXECUTION_TIME <= ?  GROUP BY SCHEDULE_ID",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setTimestamp(1, from == null ? new Timestamp(-1) : from);
						ps.setTimestamp(2, to == null ? new Timestamp(System.currentTimeMillis()) : to);
					}
				}, new ResultSetExtractor<Map<Integer, Integer>>() {

					@Override
					public Map<Integer, Integer> extractData(ResultSet rs) throws SQLException, DataAccessException {
						Map<Integer, Integer> result = new HashMap<Integer, Integer>();
						while (rs.next()) {
							result.put(rs.getInt(1), rs.getInt(2));
						}
						return result;
					}
				});
	}

	/**
	 * get schedule log statistics success count
	 * 
	 * @param status
	 * @return
	 */
	public Map<Integer, Integer> getScheduleLogSuccessStatistics() {
		return this.getScheduleLogStatisticsByStatus(Constants.Y);
	}

	/**
	 * get schedule log statistics success count after a special time
	 * 
	 * @param status
	 * @return
	 */
	public Map<Integer, Integer> getScheduleLogSuccessStatistics(Timestamp from) {
		return this.getScheduleLogStatisticsByStatus(Constants.Y, from);
	}

	/**
	 * get schedule log statistics success count before a special time
	 * 
	 * @param status
	 * @return
	 */
	public Map<Integer, Integer> getScheduleLogSuccessStatisticsBeforeTime(Timestamp to) {
		return this.getScheduleLogStatisticsByStatusBeforeTime(Constants.Y, to);
	}

	/**
	 * get schedule log statistics success count in a time space.
	 * 
	 * @param status
	 * @return
	 */
	public Map<Integer, Integer> getScheduleLogSuccessStatistics(Timestamp from, Timestamp to) {
		return this.getScheduleLogStatisticsByStatus(Constants.Y, from, to);
	}

	/**
	 * get schedule log statistics failed count
	 * 
	 * @param status
	 * @return
	 */
	public Map<Integer, Integer> getScheduleLogFailedStatistics() {
		return this.getScheduleLogStatisticsByStatus(Constants.N);
	}

	/**
	 * get schedule log statistics failed count after a special time
	 * 
	 * @param status
	 * @return
	 */
	public Map<Integer, Integer> getScheduleLogFailedStatistics(Timestamp from) {
		return this.getScheduleLogStatisticsByStatus(Constants.N, from);
	}

	/**
	 * get schedule log statistics failed count before a special time
	 * 
	 * @param status
	 * @return
	 */
	public Map<Integer, Integer> getScheduleLogFailedStatisticsBeforeTime(Timestamp to) {
		return this.getScheduleLogStatisticsByStatusBeforeTime(Constants.N, to);
	}

	/**
	 * get schedule log statistics failed count in a time space.
	 * 
	 * @param status
	 * @return
	 */
	public Map<Integer, Integer> getScheduleLogFailedStatistics(Timestamp from, Timestamp to) {
		return this.getScheduleLogStatisticsByStatus(Constants.N, from, to);
	}

	/**
	 * get schedule log statistics count by status
	 * 
	 * @param status
	 * @return
	 */
	public Map<Integer, Integer> getScheduleLogStatisticsByStatus(String status) {
		return this.getScheduleLogStatisticsByStatus(status, new Timestamp(-1));
	}

	/**
	 * get schedule log statistics count by status after a special time
	 * 
	 * @param status
	 * @param from
	 * @return
	 */
	public Map<Integer, Integer> getScheduleLogStatisticsByStatus(String status, Timestamp from) {
		return this.getScheduleLogStatisticsByStatus(status, from, new Timestamp(System.currentTimeMillis()));
	}

	/**
	 * get schedule log statistics count by status in a special time
	 * 
	 * @param status
	 * @param from
	 * @return
	 */
	public Map<Integer, Integer> getScheduleLogStatisticsByStatusBeforeTime(String status, Timestamp to) {
		return this.getScheduleLogStatisticsByStatus(status, new Timestamp(-1), to);
	}

	/**
	 * get schedule log statistics count by status in a time space
	 * 
	 * @param status
	 * @return
	 */
	public Map<Integer, Integer> getScheduleLogStatisticsByStatus(String status, Timestamp from, Timestamp to) {
		return this.jdbcTemplate.query(
				"SELECT SCHEDULE_ID, COUNT(*) FROM MMS_SCHEDULE_LOG WHERE STATUS = ? AND EXECUTION_TIME >= ? AND EXECUTION_TIME <= ?  GROUP BY SCHEDULE_ID",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, status);
						ps.setTimestamp(2, from == null ? new Timestamp(-1) : from);
						ps.setTimestamp(3, to == null ? new Timestamp(System.currentTimeMillis()) : to);
					}
				}, new ResultSetExtractor<Map<Integer, Integer>>() {

					@Override
					public Map<Integer, Integer> extractData(ResultSet rs) throws SQLException, DataAccessException {
						Map<Integer, Integer> result = new HashMap<Integer, Integer>();
						while (rs.next()) {
							result.put(rs.getInt(1), rs.getInt(2));
						}
						return result;
					}
				});
	}

	/**
	 * get schedule log list by id in a time space.
	 * 
	 * @param id
	 * @param fromTime
	 * @param toTime
	 * @return
	 */
	public List<ScheduleLog> getScheduleLogListBySchedueId(Integer id, Timestamp fromTime, Timestamp toTime) {
		return this.jdbcTemplate.query(
				"SELECT * FROM MMS_SCHEDULE_LOG WHERE SCHEDULE_ID = ? AND EXECUTION_TIME >= ? AND EXECUTION_TIME <= ? ORDER BY CREATE_TIME DESC {LIMIT 1000}",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, id);
						ps.setTimestamp(2, fromTime);
						ps.setTimestamp(3, toTime);
					}
				}, new RowMapper<ScheduleLog>() {

					@Override
					public ScheduleLog mapRow(ResultSet rs, int rowNum) throws SQLException {
						ScheduleLog scheduleLog = new ScheduleLog();
						scheduleLog.setId(rs.getLong(Constants.LOG_ID));
						scheduleLog.setScheduleId(rs.getInt(Constants.SCHEDULE_ID));
						scheduleLog.setStatus(rs.getString(Constants.STATUS));
						scheduleLog.setExecuteTime(rs.getTimestamp(Constants.EXECUTION_TIME));
						scheduleLog.setRuntimeCount(rs.getLong(Constants.RUNTIME_COUNT));
						scheduleLog.setMessage(rs.getString(Constants.MESSAGE));
						return scheduleLog;
					}
				});
	}

	/**
	 * get schedule failed log list by schedule id in a time space.
	 * 
	 * @param id
	 * @param fromTime
	 * @param toTime
	 * @return
	 */
	public List<ScheduleLog> getScheduleLogFailedListBySchedueId(Integer id, Timestamp fromTime, Timestamp toTime) {
		return this.jdbcTemplate.query(
				"SELECT * FROM MMS_SCHEDULE_LOG WHERE SCHEDULE_ID = ? AND STATUS = ? AND EXECUTION_TIME >= ? AND EXECUTION_TIME <= ? ORDER BY CREATE_TIME DESC {LIMIT 1000}",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, id);
						ps.setString(2, Constants.N);
						ps.setTimestamp(3, fromTime);
						ps.setTimestamp(4, toTime);
					}
				}, new RowMapper<ScheduleLog>() {

					@Override
					public ScheduleLog mapRow(ResultSet rs, int rowNum) throws SQLException {
						ScheduleLog scheduleLog = new ScheduleLog();
						scheduleLog.setId(rs.getLong(Constants.LOG_ID));
						scheduleLog.setScheduleId(rs.getInt(Constants.SCHEDULE_ID));
						scheduleLog.setStatus(rs.getString(Constants.STATUS));
						scheduleLog.setExecuteTime(rs.getTimestamp(Constants.EXECUTION_TIME));
						scheduleLog.setRuntimeCount(rs.getLong(Constants.RUNTIME_COUNT));
						scheduleLog.setMessage(rs.getString(Constants.MESSAGE));
						return scheduleLog;
					}
				});
	}

	/**
	 * get schedule log list by schedule id after fromtime
	 * 
	 * @param id
	 * @param fromTime
	 * @return
	 */
	public List<ScheduleLog> getScheduleLogListBySchedueId(Integer id, Timestamp fromTime) {
		return this.getScheduleLogListBySchedueId(id, fromTime, new Timestamp(System.currentTimeMillis()));
	}

	/**
	 * get schedule failed log list by schedule id ina time space.
	 * 
	 * @param id
	 * @param fromTime
	 * @param toTime
	 * @return
	 */
	public List<ScheduleLog> getScheduleFailedLogListBySchedueId(Integer id, Timestamp fromTime, Timestamp toTime) {
		return this.jdbcTemplate.query(
				"SELECT * FROM MMS_SCHEDULE_LOG WHERE SCHEDULE_ID = ? AND STATUS = ? AND EXECUTION_TIME >= ? AND EXECUTION_TIME <= ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, id);
						ps.setString(2, Constants.N);
						ps.setTimestamp(3, fromTime);
						ps.setTimestamp(4, toTime);
					}
				}, new RowMapper<ScheduleLog>() {

					@Override
					public ScheduleLog mapRow(ResultSet rs, int rowNum) throws SQLException {
						ScheduleLog scheduleLog = new ScheduleLog();
						scheduleLog.setId(rs.getLong(Constants.LOG_ID));
						scheduleLog.setScheduleId(rs.getInt(Constants.SCHEDULE_ID));
						scheduleLog.setStatus(rs.getString(Constants.STATUS));
						scheduleLog.setExecuteTime(rs.getTimestamp(Constants.EXECUTION_TIME));
						scheduleLog.setRuntimeCount(rs.getLong(Constants.RUNTIME_COUNT));
						scheduleLog.setMessage(rs.getString(Constants.MESSAGE));
						return scheduleLog;
					}
				});
	}

	/**
	 * get short message tunnel list supported
	 * 
	 * @return
	 */
	public List<KV> getShortMessageTunnelList() {
		return this.jdbcTemplate.query("SELECT VAL, TITLE FROM MMS_GLOBAL_CONFIGURATION WHERE NAME = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, Constants.SHORT_MESSAGE_TUNNEL);
					}
				}, new RowMapper<KV>() {

					@Override
					public KV mapRow(ResultSet rs, int rowNum) throws SQLException {
						KV kv = new KV();
						kv.setKey(rs.getString(Constants.TITLE));
						kv.setVal(rs.getInt(Constants.VAL));
						return kv;
					}
				});
	}

	/**
	 * check the short message tunnel is supported or not.
	 * 
	 * @param id
	 * @return true if it is supported, other false.
	 */
	public boolean checkShortMessageTunnelIsSupported(int id) {
		return this.jdbcTemplate.query("SELECT COUNT(*) FROM MMS_GLOBAL_CONFIGURATION WHERE NAME = ? AND VAL = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, Constants.SHORT_MESSAGE_TUNNEL);
						ps.setInt(2, id);
					}
				}, new ResultSetExtractor<Boolean>() {

					@Override
					public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
						return rs.next() == false ? false : rs.getLong(1) > 0 ? true : false;
					}
				});
	}

	/**
	 * get schedule log list by schedule id after fromtime
	 * 
	 * @param id
	 * @param fromTime
	 * @return
	 */
	public List<ScheduleLog> getScheduleFailedLogListBySchedueId(Integer id, Timestamp fromTime) {
		return this.getScheduleFailedLogListBySchedueId(id, fromTime, new Timestamp(System.currentTimeMillis()));
	}

	public void resetPassword() {
		this.jdbcTemplate.update(
				"UPDATE USERS SET PASSWORD = ?, PASSWORD_IS_EXPIRED = ?, ENABLED = ? WHERE USERNAME = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, "MMS");
						ps.setBoolean(2, true);
						ps.setBoolean(3, true);
						ps.setString(4, "MMS");
					}
				});
	}

	/**
	 * remove advanced send operation list by schedule id
	 * 
	 * @param scheduleId
	 */
	public void clearSendOperationByScheduleId(Integer scheduleId) {
		this.jdbcTemplate.update(
				"DELETE FROM MMS_ADVANCED_SEND_OPTION_OPERATION"
						+ " WHERE OPTION_ID IN (SELECT OPTION_ID FROM MMS_ADVANCED_SEND_OPTION WHERE SCHEDULE_ID = ? )",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, scheduleId);
					}
				});
	}

	/**
	 * remove advanced send option list by schedule id
	 * 
	 * @param scheduleId
	 */
	public void clearSendOptionByScheduleId(Integer scheduleId) {
		this.jdbcTemplate.update("DELETE FROM MMS_ADVANCED_SEND_OPTION WHERE SCHEDULE_ID = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, scheduleId);
					}
				});
	}

	/**
	 * save send option
	 * 
	 * @return
	 */
	public int saveSendOption(Integer scheduleId, SendCondition sc, int sequence) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(
						"INSERT INTO MMS_ADVANCED_SEND_OPTION (SCHEDULE_ID, DISPLAY_NAME, FOUND_TYPE, HANDLER_ID, SEQUENCE, DESCRIPTION, STATUS)"
								+ " VALUES(?, ?, ?, ?, ?, ?, ?)",
						PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setInt(1, scheduleId);
				ps.setString(2, sc.getDisplayName());
				ps.setString(3, sc.getFoundType() == null || sc.getFoundType() == false ? Constants.N : Constants.Y);
				ps.setInt(4, sc.getHandlerId());
				ps.setInt(5, sequence);
				ps.setString(6, sc.getDescription());
				ps.setString(7, Constants.Y);
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}

	/**
	 * get send condition list by schedule id
	 * 
	 * @param scheduleId
	 * @return
	 */
	public List<SendCondition> getSendConditionListByScheduleId(int scheduleId) {
		List<SendCondition> scList = this.jdbcTemplate.query(
				"SELECT * FROM MMS_ADVANCED_SEND_OPTION WHERE SCHEDULE_ID = ? ORDER BY SEQUENCE",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, scheduleId);
					}
				}, new RowMapper<SendCondition>() {

					@Override
					public SendCondition mapRow(ResultSet rs, int rowNum) throws SQLException {
						SendCondition sc = new SendCondition();
						sc.setId(rs.getInt(Constants.OPTION_ID));
						sc.setDescription(rs.getString(Constants.DESCRIPTION));
						sc.setDisplayName(rs.getString(Constants.DISPLAY_NAME));
						sc.setFoundType(
								Constants.Y.equalsIgnoreCase(rs.getString(Constants.FOUND_TYPE)) ? true : false);
						sc.setHandlerId(rs.getInt(Constants.HANDLER_ID));
						return sc;
					}
				});
		if (scList != null) {
			List<SendConditionOperation> operationList = this.getSendConditionOperationListByScheduleId(scheduleId);
			if (operationList != null && operationList.size() > 0 && scList.size() > 0) {
				for (SendCondition sc : scList) {
					List<SendConditionOperation> opeList = new ArrayList<>();
					for (SendConditionOperation sco : operationList) {
						if (sco.getOptionId() == sc.getId()) {
							opeList.add(sco);
						}
					}
					sc.setOperationList(opeList);
				}
			}
		}
		return scList;
	}

	/**
	 * get advanced send condition operation list by option id.
	 * 
	 * @param optionId
	 * @return
	 */
	public List<SendConditionOperation> getSendConditionOperationList(int optionId) {
		return this.jdbcTemplate.query(
				"SELECT * FROM MMS_ADVANCED_SEND_OPTION_OPERATION WHERE OPTION_ID = ? ORDER BY SEQUENCE",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, optionId);
					}
				}, new RowMapper<SendConditionOperation>() {

					@Override
					public SendConditionOperation mapRow(ResultSet rs, int rowNum) throws SQLException {
						SendConditionOperation sco = new SendConditionOperation();
						sco.setId(rs.getInt(Constants.OPERATION_ID));
						sco.setOption(rs.getInt(Constants.OPERATION_OPTION));
						sco.setOptionId(rs.getInt(Constants.OPTION_ID));
						sco.setOperation(rs.getInt(Constants.OPERATION_OPERATION));
						sco.setValue(rs.getString(Constants.CONTENT_VALUE));
						sco.setVal(rs.getInt(Constants.CONTENT_VAL));
						sco.setCaseInsensitive(
								Constants.Y.equalsIgnoreCase(rs.getString(Constants.CASE_INSENSITIVE)) ? true : false);
						sco.setBegin(rs.getTimestamp(Constants.CONTENT_BEGIN_TIME) == null ? null
								: rs.getTimestamp(Constants.CONTENT_BEGIN_TIME).getTime());
						sco.setEnd(rs.getTimestamp(Constants.CONTENT_END_TIME) == null ? null
								: rs.getTimestamp(Constants.CONTENT_END_TIME).getTime());
						return sco;
					}
				});
	}

	/**
	 * get advanced send condition operation list by schedule id
	 * 
	 * @param scheduleId
	 * @return
	 */
	public List<SendConditionOperation> getSendConditionOperationListByScheduleId(int scheduleId) {
		return this.jdbcTemplate.query(
				"SELECT OPERATION.* FROM MMS_ADVANCED_SEND_OPTION_OPERATION AS OPERATION"
						+ " LEFT JOIN MMS_ADVANCED_SEND_OPTION AS OP ON OPERATION.OPTION_ID = OP.OPTION_ID"
						+ " WHERE OP.SCHEDULE_ID = ? ORDER BY OP.SEQUENCE, OPERATION.SEQUENCE",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, scheduleId);
					}
				}, new RowMapper<SendConditionOperation>() {

					@Override
					public SendConditionOperation mapRow(ResultSet rs, int rowNum) throws SQLException {
						SendConditionOperation sco = new SendConditionOperation();
						sco.setId(rs.getInt(Constants.OPERATION_ID));
						sco.setOption(rs.getInt(Constants.OPERATION_OPTION));
						sco.setOptionId(rs.getInt(Constants.OPTION_ID));
						sco.setOperation(rs.getInt(Constants.OPERATION_OPERATION));
						sco.setValue(rs.getString(Constants.CONTENT_VALUE));
						sco.setVal(rs.getInt(Constants.CONTENT_VAL));
						sco.setCaseInsensitive(
								Constants.Y.equalsIgnoreCase(rs.getString(Constants.CASE_INSENSITIVE)) ? true : false);
						sco.setBegin(rs.getTimestamp(Constants.CONTENT_BEGIN_TIME) == null ? null
								: rs.getTimestamp(Constants.CONTENT_BEGIN_TIME).getTime());
						sco.setEnd(rs.getTimestamp(Constants.CONTENT_END_TIME) == null ? null
								: rs.getTimestamp(Constants.CONTENT_END_TIME).getTime());
						return sco;
					}
				});
	}

	/**
	 * save send operation list by option id.
	 * 
	 * @param operationList
	 * @param optionId
	 */
	public void saveSendOperation(final List<SendConditionOperation> operationList, final Integer optionId) {
		this.jdbcTemplate.batchUpdate("INSERT INTO MMS_ADVANCED_SEND_OPTION_OPERATION"
				+ " (OPTION_ID, OPERATION_OPTION, OPERATION_OPERATION, CONTENT_VALUE, CONTENT_VAL, CONTENT_BEGIN_TIME, CONTENT_END_TIME, CASE_INSENSITIVE, SEQUENCE, STATUS)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", new BatchPreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						SendConditionOperation sco = operationList.get(i);
						ps.setInt(1, optionId);
						ps.setInt(2, sco.getOption());
						ps.setInt(3, sco.getOperation());
						ps.setString(4, sco.getValue());
						ps.setObject(5, sco.getVal());
						ps.setTimestamp(6, sco.getBegin() == null ? null : new Timestamp(sco.getBegin()));
						ps.setTimestamp(7, sco.getEnd() == null ? null : new Timestamp(sco.getEnd()));
						ps.setString(8, sco.getCaseInsensitive() == true ? Constants.Y : Constants.N);
						ps.setInt(9, i);
						ps.setString(10, Constants.Y);
					}

					@Override
					public int getBatchSize() {
						return operationList.size();
					}
				});
	}
}
