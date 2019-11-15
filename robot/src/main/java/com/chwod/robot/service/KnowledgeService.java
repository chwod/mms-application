package com.chwod.robot.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.chwod.robot.bean.EventContext;
import com.chwod.robot.domain.Knowledge;
import com.chwod.robot.utils.Constants;

@Service
public class KnowledgeService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * save a knowledge
	 * 
	 * @param knowledge
	 */
	public void save(Knowledge knowledge) {
		this.jdbcTemplate.update("insert into knowledge(event_id, name, property, content) values (?, ?, ?, ?)",
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, knowledge.getEventId());
						ps.setString(2, knowledge.getName());
						ps.setString(3, knowledge.getProperty());
						ps.setString(4, knowledge.getContent());
					}
				});
	}

	/**
	 * Get all possible name solutions.
	 * 
	 * @param value
	 * @param parentContent
	 * @return
	 */
	public List<Knowledge> GetAllPossibleNameSolutions(String content, String parentContent) {
		String sql = "select r.name as name, count(r.name) as count from (select distinct k.* from knowledge as k"
				+ " left join knowledge as c on k.event_id = c.event_id and k.id <> c.id"
				+ " left join knowledge as n on k.event_id = n.event_id and k.id <> n.id"
				+ " where k.property = ? and k.content = ? and c.name = k.content and c.property = ? and c.content = ?"
				+ " and n.name = k.name and n.property = c.property and n.content = c.content) as r"
				+ " where 1 = 1 group by r.name order by 2 desc";
		List<Knowledge> result = this.jdbcTemplate.query(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, EventContext.SENTECNE_PREDICATE_TYPE_IS);
				ps.setString(2, content);
				ps.setString(3, EventContext.SENTECNE_PREDICATE_TYPE_BELONG);
				ps.setString(4, parentContent);
			}
		}, new RowMapper<Knowledge>() {

			@Override
			public Knowledge mapRow(ResultSet rs, int rowNum) throws SQLException {
				Knowledge knowledge = new Knowledge();
				knowledge.setName(rs.getString(Constants.NAME));
				knowledge.setCount(rs.getLong(Constants.COUNT));
				return knowledge;
			}
		});
		return result;
	}
	
	/**
	 * get all possible content solutions
	 * @param name
	 * @param content
	 * @return
	 */
	public List<Knowledge> getAllPossibleContentSolutions(String name, String content){
		String sql = "select r.content as content, count(r.content) as count from (select distinct k.* from knowledge as k"
				+ " left join knowledge as n on k.event_id = n.event_id and k.id <> n.id"
				+ " left join knowledge as c on k.event_id = c.event_id and k.id <> c.id"
				+ " where k.name = n.name and k.content = c.content and n.content = c.name"
				+ " and k.property = c.property and k.property = ? and n.property = ?"
				+ " and n.name = ? and n.content = ?) as r"
				+ " group by r.content order by 2 desc";
		return this.jdbcTemplate.query(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, EventContext.SENTECNE_PREDICATE_TYPE_BELONG);
				ps.setString(2, EventContext.SENTECNE_PREDICATE_TYPE_IS);
				ps.setString(3, name);
				ps.setString(4, content);
			}
		}, new RowMapper<Knowledge>() {

			@Override
			public Knowledge mapRow(ResultSet rs, int rowNum) throws SQLException {
				Knowledge knowledge = new Knowledge();
				knowledge.setContent(rs.getString(Constants.CONTENT));
				knowledge.setCount(rs.getLong(Constants.COUNT));
				return knowledge;
			}
		});
	}
	
	/**
	 * get all valid content solutions
	 * @param name
	 * @param content
	 * @param modificationProperty
	 * @return
	 */
	public List<Knowledge> getAllValidContentSolutions(String name, String content, String modificationProperty){
		String sql = "select r.content as content, count(r.content) as count from (select distinct k.* from knowledge as k"
				+ " left join knowledge as n on k.event_id = n.event_id and k.id <> n.id"
				+ " left join knowledge as c on k.event_id = c.event_id and k.id <> c.id"
				+ " left join knowledge as v on k.content = v.name"
				+ " where k.name = n.name and k.content = c.content and n.content = c.name"
				+ " and k.property = c.property and k.property = ? and n.property = ?"
				+ " and n.name = ? and n.content = ? and v.property = n.property and v.content = ? and v.id is not null) as r"
				+ " group by r.content order by 2 desc";
		return this.jdbcTemplate.query(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, EventContext.SENTECNE_PREDICATE_TYPE_BELONG);
				ps.setString(2, EventContext.SENTECNE_PREDICATE_TYPE_IS);
				ps.setString(3, name);
				ps.setString(4, content);
				ps.setString(5, modificationProperty);
			}
		}, new RowMapper<Knowledge>() {

			@Override
			public Knowledge mapRow(ResultSet rs, int rowNum) throws SQLException {
				Knowledge knowledge = new Knowledge();
				knowledge.setContent(rs.getString(Constants.CONTENT));
				knowledge.setCount(rs.getLong(Constants.COUNT));
				return knowledge;
			}
		});
	}
	
	/**
	 * get know solutions.
	 * @param name
	 * @param property
	 * @return
	 */
	public List<Knowledge> getKnowSolutions(String name, String property) {
		String sql = "select k.content as content, count(k.content) as count from knowledge as k where k.name = ? and k.property = ? group by k.content order by 2 desc";
		return this.jdbcTemplate.query(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, name);
				ps.setString(2, property);
			}

		}, new RowMapper<Knowledge>() {

			@Override
			public Knowledge mapRow(ResultSet rs, int rowNum) throws SQLException {
				Knowledge knowledge = new Knowledge();
				knowledge.setContent(rs.getString(Constants.CONTENT));
				knowledge.setCount(rs.getLong(Constants.COUNT));
				return knowledge;
			}
		});
	}
	
	/**
	 * check valid knowledge.
	 * @param name
	 * @param property
	 * @param content
	 * @return	return true if know, other unknown or invalid.
	 */
	public Boolean checkvalidKnowledge(String name, String property, String content){
		return this.jdbcTemplate.query("select * from knowledge where name = ? and property = ? and content = ?", new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, name);
				ps.setString(2, property);
				ps.setString(3, content);
			}
		}, new ResultSetExtractor<Boolean>() {

			@Override
			public Boolean extractData(ResultSet rs) throws SQLException, DataAccessException {
				return rs.next();
			}
		});
	}
	
}
