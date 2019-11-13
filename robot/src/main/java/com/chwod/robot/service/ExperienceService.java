package com.chwod.robot.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.chwod.robot.domain.Experience;
import com.chwod.robot.utils.Constants;

@Component
public class ExperienceService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * save a new experience.
	 * @param experience
	 */
	public void save(Experience experience) {
		this.jdbcTemplate.update("insert into experience (parse_word, property, content) values (?,?,?)",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, experience.getParseWord());
						ps.setString(2, experience.getProperty());
						ps.setString(3, experience.getContent());
					}
				});
	}

	/**
	 * get all possible solutions by parse word and property.
	 * @param parseWord
	 * @param property
	 * @return list of all possible solutions.
	 */
	public List<Experience> GetAllPossibleSolutions(String parseWord, String property) {
		String sql = "select content, count(content) as count from experience where parse_word = ? and property = ? group by content order by 2";
		return this.jdbcTemplate.query(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, parseWord);
				ps.setString(2, property);
			}
		}, new RowMapper<Experience>() {

			@Override
			public Experience mapRow(ResultSet rs, int rowNum) throws SQLException {
				Experience experience = new Experience();
				experience.setContent(rs.getString(Constants.CONTENT));
				experience.setCount(rs.getLong(Constants.COUNT));
				return experience;
			}
		});
	}
}
