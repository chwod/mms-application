package com.chwod.robot.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.chwod.robot.domain.Configuration;
import com.chwod.robot.utils.Constants;

@Service
public class ConfigurationService {

	@Value("${global.configuration.process.deep:10}")
	private Integer processDeep;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * get process deep.
	 * @return
	 */
	public int getProcessDeep() {
		Configuration configuration = this.getByNameAndTitle(Constants.GLOBAL_CONFIGURATION, Constants.PROCESS_DEEP);
		return configuration == null ? this.processDeep : configuration.getVal().intValue();
	}
	
	/**
	 * get configuration object by name and title.
	 * 
	 * @param name
	 * @param title
	 * @return
	 */
	public Configuration getByNameAndTitle(String name, String title) {
		return this.jdbcTemplate.query("select * from configuration where name = ? and title = ? and status = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, name);
						ps.setString(2, title);
						ps.setString(3, Constants.Y);
					}
				}, new ResultSetExtractor<Configuration>() {

					@Override
					public Configuration extractData(ResultSet rs) throws SQLException, DataAccessException {
						if (rs.next()) {
							Configuration configuration = new Configuration();
							configuration.setName(rs.getString(Constants.NAME));
							configuration.setTitle(rs.getString(Constants.TITLE));
							configuration.setValue(rs.getString(Constants.VALUE));
							configuration.setVal(rs.getLong(Constants.VAL));
							configuration.setSequence(rs.getInt(Constants.SEQUENCE));
							configuration.setStatus(rs.getString(Constants.STATUS));
							configuration.setDescription(rs.getString(Constants.DESCRIPTION));
							configuration.setCreateTime(rs.getTimestamp(Constants.CREATE_TIME));
							return configuration;
						}
						return null;
					}
				});
	}

	/**
	 * get configuration by name
	 * 
	 * @param name
	 * @return list of configuration
	 */
	public List<Configuration> getByName(String name) {
		return this.jdbcTemplate.query("select * from configuration where name = ? and status = ?",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, name);
						ps.setString(2, Constants.Y);
					}

				}, new RowMapper<Configuration>() {

					@Override
					public Configuration mapRow(ResultSet rs, int rowNum) throws SQLException {
						Configuration configuration = new Configuration();
						configuration.setName(rs.getString(Constants.NAME));
						configuration.setTitle(rs.getString(Constants.TITLE));
						configuration.setValue(rs.getString(Constants.VALUE));
						configuration.setVal(rs.getLong(Constants.VAL));
						configuration.setSequence(rs.getInt(Constants.SEQUENCE));
						configuration.setDescription(rs.getString(Constants.DESCRIPTION));
						configuration.setStatus(rs.getString(Constants.STATUS));
						configuration.setCreateTime(rs.getTimestamp(Constants.CREATE_TIME));
						return configuration;
					}
				});
	}
}
