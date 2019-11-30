package com.java.mc.schedule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.java.mc.bean.BatchJob;
import com.java.mc.bean.DatasourceConfig;
import com.java.mc.bean.MailServerConfig;
import com.java.mc.bean.MailTask;
import com.java.mc.bean.Schedule;
import com.java.mc.bean.SendCondition;
import com.java.mc.bean.ShortMessageTask;
import com.java.mc.db.DBConnection;
import com.java.mc.db.DBOperation;
import com.java.mc.utils.CheckUtils;
import com.java.mc.utils.Constants;
import com.java.mc.utils.DBUtils;
import com.java.mc.utils.ValidationUtils;
import com.java.mc.utils.WebUtils;

@Service
@Transactional
public class ScheduleJob implements org.quartz.Job {
	private static final Logger logger = LoggerFactory.getLogger(ScheduleJob.class);

	private static final String path = System.getProperty("user.dir");

	@Autowired
	private DBOperation dbOperation;

	@Autowired
	private DBConnection dbConnection;

	@Autowired
	private Job job;

	@Value("${vg.batch.job.name}")
	private String jobName;

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private JobOperator jobOperator;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.trace("job[jobname:{},groupname:{}] running!", context.getJobDetail().getKey().getName(),
				context.getJobDetail().getKey().getGroup());

		Long startTime = System.currentTimeMillis();

		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		Integer scheduleId = jobDataMap.getInt(Constants.SCHEDULE_ID);
		if (StringUtils.isEmpty(scheduleId)) {
			throw new JobExecutionException("can not found schedule id");
		}
		Schedule schedule = this.dbOperation.getScheduleById(scheduleId);
		if (schedule == null) {
			throw new JobExecutionException("can not found schedule");
		}

		try {
			Short action = schedule.getActionType().shortValue();
			switch (action) {
			case Constants.ACTION_DB_CONNECTION_VALIDATION:
				this.DBConnectionValidation();
				break;
			case Constants.ACTION_MAIL_SCAN:
				this.mailScanAction(schedule);
				break;
			case Constants.ACTION_DO_BATCH_JOB:
				this.doBatchJobAction();
				break;
			case Constants.ACTION_AUTO_RESTART:
				this.restart();
				break;
			case Constants.ACTION_MMS_PLAN:
				this.MMSPlan(schedule);
				break;
			case Constants.ACTION_WINDOW_COMMAND:
				this.runCommandAction(schedule);
				break;
			case Constants.ACTION_FAILD_BATCHJOB_RERESET:
				this.failedBatchJobResetAction();
				break;
			case Constants.ACTION_DB_RUNSQL:
				this.dbRunSQLAction(schedule);
				break;
			case Constants.ACTION_CLEAR_LOG:
				this.clearLogAction();
				break;
			case Constants.ACTION_SM_SCAN:
				this.shortMessageScanAction(schedule);
				break;
			case Constants.ACTION_DAEMON_MONITOR:
				this.daemonMonitor();
				break;
			default:
				logger.warn("Nothing to do!");
				throw new Exception("unkown action type");
			}

			Long endTime = System.currentTimeMillis();
			this.dbOperation.createScheduleLog(scheduleId, Constants.Y, Long.valueOf((endTime - startTime)).intValue(),
					null);
			return;
		} catch (Exception e) {
			Long endTime = System.currentTimeMillis();
			this.dbOperation.createScheduleLog(scheduleId, Constants.N, Long.valueOf((endTime - startTime)).intValue(),
					e.getMessage());
			return;
		}
	}

	/**
	 * remote db sql executor action
	 * 
	 * @param sql
	 * @param dsid
	 */
	private void dbRunSQLAction(Schedule schedule) {
		this.dbConnection.getRemoteJdbcTemplate(schedule.getDsid()).execute(schedule.getSqlSentence());
	}

	// reset failed mail job status to prepare to resender
	private void failedBatchJobResetAction() {
		Calendar from = Calendar.getInstance();
		from.add(Calendar.MINUTE, -10);
		this.dbOperation.resetFailedBatchJobStatus(new Timestamp(from.getTimeInMillis()), null);
	}

	/**
	 * clear the schedule log and mail log before one month
	 */
	private void clearLogAction() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		this.dbOperation.clearScheduleLogBeforeTime(new Timestamp(calendar.getTimeInMillis()));
		this.dbOperation.clearBatchLogBeforeTime(new Timestamp(calendar.getTimeInMillis()));
	}

	/**
	 * do batch job action
	 */
	private void doBatchJobAction() {
		try {
			this.jobOperator.getRunningExecutions(this.jobName);
		} catch (NoSuchJobException e) {
			try {
				HashMap<String, JobParameter> parameters = new HashMap<String, JobParameter>();  
				parameters.put("currentTime", new JobParameter(System.currentTimeMillis())); 
				this.jobLauncher.run(this.job, new JobParameters(parameters));
			} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
					| JobParametersInvalidException e1) {
			}
		}
	}

	/**
	 * remote database connection validation job
	 * 
	 * @throws Exception
	 */
	private void DBConnectionValidation() throws Exception {
		Map<Integer, JdbcTemplate> connectionMap = this.dbConnection.getRemoteJdbcTemplateMap();
		for (Integer key : connectionMap.keySet()) {
			try {
				this.dbConnection.checkRemoteConnection(key);
			} catch (Exception e) {
				this.dbConnection.resetConnectionByid(key);
			}
		}
	}

	/*
	 * MMS plan
	 */
	private void MMSPlan(Schedule schedule) throws IOException, URISyntaxException {
		WebUtils.access(schedule.getUrl());
	}

	/**
	 * application restart action the daemon application will be start it in one
	 * minute.
	 */
	private void restart() {
		System.exit(0);
	}

	// daemon-application monitor action
	private void daemonMonitor() throws IOException {
		InputStream is = runCommand("wmic process where(Name=\"vgmc-daemon.exe\") list full");
		if (is != null) {
			String str = new BufferedReader(new InputStreamReader(is)).lines().parallel()
					.collect(Collectors.joining("\n"));
			if (str != null && str.length() > 0 && str.toLowerCase().contains("vgmc-daemon")) {
				return;
			}
		}

		// start the daemon application if not exist..
		StringBuffer command = new StringBuffer(path).append("\\vgmc-daemon.exe");
		runCommand(command.toString(), "start");
	}

	private InputStream runCommand(String command) throws IOException {
		return this.runCommand(command, null);
	}

	/**
	 * run window command action
	 * 
	 * @param command
	 * @param params
	 * @throws IOException
	 */
	private void runCommandAction(Schedule schedule) throws IOException {
		this.runCommand(schedule.getCommand(), schedule.getCommandVariable());
	}

	private final InputStream runCommand(String command, String params) throws IOException {
		if (command == null) {
			return null;
		}
		StringBuffer runCommand = new StringBuffer("cmd /c \"").append(command).append("\"");
		if (params != null) {
			runCommand.append(" ").append(params);
		}

		logger.debug("The command is : {}", runCommand);
		return Runtime.getRuntime().exec(runCommand.toString()).getInputStream();
	}

	/**
	 * short message task scan action
	 * 
	 * @param schedule
	 */
	private void shortMessageScanAction(Schedule schedule) {
		DatasourceConfig dsc = this.dbOperation.getDSConfigurationById(schedule.getDsid());
		String sql = DBUtils.getSQL("SELECT * FROM :t WHERE IS_SEND = ?", dsc.getSqlType(), dsc.getArchName(),
				Constants.WEB_SMS);
		List<ShortMessageTask> taskList = this.dbConnection.getRemoteJdbcTemplate(schedule.getDsid()).query(sql,
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, Constants.SEND_INTI);
					}
				}, new RowMapper<ShortMessageTask>() {

					@SuppressWarnings("deprecation")
					@Override
					public ShortMessageTask mapRow(ResultSet rs, int rowNum) throws SQLException {
						ShortMessageTask task = new ShortMessageTask();

						task.setIsSend(Constants.SEND_END);
						task.setCode(Constants.PENDING);

						task.setMessageId(rs.getLong(Constants.MESSAGE_ID));
						task.setStaffNo(rs.getString(Constants.STAFF_NO));
						task.setStaffName(rs.getString(Constants.STAFF_NAME));
						task.setToMobileNo(rs.getString(Constants.TO_MOBILE_NO));
						task.setSubject(rs.getString(Constants.SUBJECT));
						task.setSubject(task.getSubject() == null ? null
								: task.getSubject().trim().length() <= 0 ? null : task.getSubject().trim());
						task.setContent(rs.getString(Constants.CONTENT));
						task.setContent(task.getContent() == null ? null
								: task.getContent().trim().length() <= 0 ? null : task.getContent().trim());
						task.setAttach(rs.getString(Constants.ATTACH));
						return task;
					}
				});

		// pending the job
		this.updateSMStatus(taskList, dsc);

		this.saveSMTask(taskList, schedule, dsc);
	}

	/**
	 * mail scan action
	 * 
	 * @param msid
	 * @param dsid
	 * @param scheduleId
	 */
	private void mailScanAction(Schedule schedule) {
		DatasourceConfig dsc = this.dbOperation.getDSConfigurationById(schedule.getDsid());
		String sql = DBUtils.getSQL("SELECT * FROM :t WHERE SEND = ?", dsc.getSqlType(), dsc.getArchName(),
				Constants.WEBMAIL_V2);
		List<MailTask> mailTaskList = this.dbConnection.getRemoteJdbcTemplate(schedule.getDsid()).query(sql,
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, Constants.SEND_INTI);
					}
				}, new RowMapper<MailTask>() {

					@Override
					public MailTask mapRow(ResultSet rs, int rowNum) throws SQLException {
						MailTask mTask = new MailTask();

						mTask.setStatus(Constants.SEND_END);
						mTask.setCode(Constants.PENDING);

						mTask.setFromName(rs.getString(Constants.FROM_NAME));
						mTask.setFromEmail(rs.getString(Constants.FROM_EMAIL));
						mTask.setFromId(rs.getString(Constants.STAFF_NO));
						mTask.setSeq(rs.getLong(Constants.SEQ));
						mTask.setToMail(rs.getString(Constants.TO_EMAIL));
						mTask.setSubject(rs.getString(Constants.SUBJECT));
						mTask.setSubject(mTask.getSubject() == null ? null
								: mTask.getSubject().trim().length() <= 0 ? null : mTask.getSubject().trim());
						mTask.setContent(rs.getString(Constants.CONTENT));
						mTask.setContent(mTask.getContent() == null ? null
								: mTask.getContent().trim().length() <= 0 ? null : mTask.getContent().trim());
						mTask.setAttach(rs.getString(Constants.ATTACH));
						mTask.setAttach(mTask.getAttach() == null ? null
								: mTask.getAttach().trim().length() <= 0 ? null : mTask.getAttach().trim());
						return mTask;
					}

				});

		logger.trace("Retrive the task list size is : {}", mailTaskList.size());
		// pending the job
		this.updateMailStatus(mailTaskList, dsc);

		this.saveMailTask(mailTaskList, schedule, dsc);
	}

	private void updateMailStatus(final List<MailTask> mailTaskList, DatasourceConfig dsc) {
		if (mailTaskList != null && mailTaskList.size() > 0) {
			String remoteSQL = DBUtils.getSQL("UPDATE :t SET SEND = ?, ERROR_CODE = ? WHERE SEQ = ?", dsc.getSqlType(),
					dsc.getArchName(), Constants.WEBMAIL_V2);
			this.dbConnection.getRemoteJdbcTemplate(dsc.getId()).batchUpdate(remoteSQL,
					new BatchPreparedStatementSetter() {

						@Override
						public void setValues(PreparedStatement ps, int i) throws SQLException {
							ps.setShort(1, mailTaskList.get(i).getStatus());
							ps.setShort(2, mailTaskList.get(i).getCode());
							ps.setLong(3, mailTaskList.get(i).getSeq());
						}

						@Override
						public int getBatchSize() {
							return mailTaskList.size();
						}
					});
		}
	}

	private void updateSMStatus(final List<ShortMessageTask> smtList, DatasourceConfig dsc) {
		if (smtList != null && smtList.size() > 0) {
			String remoteSQL = DBUtils.getSQL("UPDATE :t SET IS_SEND = ?, ERROE_CODE = ? WHERE MESSAGE_ID = ?",
					dsc.getSqlType(), dsc.getArchName(), Constants.WEB_SMS);
			this.dbConnection.getRemoteJdbcTemplate(dsc.getId()).batchUpdate(remoteSQL,
					new BatchPreparedStatementSetter() {

						@Override
						public void setValues(PreparedStatement ps, int i) throws SQLException {
							ps.setShort(1, smtList.get(i).getIsSend());
							ps.setShort(2, smtList.get(i).getCode());
							ps.setLong(3, smtList.get(i).getMessageId());
						}

						@Override
						public int getBatchSize() {
							return smtList.size();
						}
					});
		}
	}

	// save mail sender task
	private void saveMailTask(List<MailTask> mailTaskList, Schedule schedule, DatasourceConfig dsc) {
		List<MailTask> invalidMailTaskList = new ArrayList<MailTask>();
		if (mailTaskList != null && mailTaskList.size() > 0) {
			List<SendCondition> scList = this.dbOperation.getSendConditionListByScheduleId(schedule.getId());
			for (MailTask mt : mailTaskList) {

				// validate to mail string. only support valid email address.
				BatchJob batchJob = new BatchJob();
				batchJob.setActionType(schedule.getActionType().shortValue());
				batchJob.setFromEmail(mt.getFromEmail());
				batchJob.setFromName(mt.getFromName());
				batchJob.setFromId(mt.getFromId());
				batchJob.setTo(mt.getToMail());
				batchJob.setToAddressList(mt.getToMail());
				batchJob.setSubject(mt.getSubject());
				batchJob.setContent(mt.getContent());
				batchJob.setAttachment(mt.getAttach());

				Integer msid = schedule.getMsid();
				try {
					msid = CheckUtils.getHanderId(batchJob, scList, schedule.getMsid());
				} catch (Exception e) {
					mt.setCode(Constants.INVALID);
					invalidMailTaskList.add(mt);
					this.dbOperation.saveMailJob(mt, mt.getToMail(), schedule.getId(), Constants.SEND_END,
							Constants.INVALID, e.getMessage());
					logger.info("[Warning][action=mailScan-invalidMailTask][result=Ignore][message={}][seq={}]",e.getMessage(), mt.getSeq());
					continue;
				}
				
				if(msid == null){
					mt.setCode(Constants.INVALID);
					invalidMailTaskList.add(mt);
					this.dbOperation.saveMailJob(mt, mt.getToMail(), schedule.getId(), Constants.SEND_END,
							Constants.INVALID, "未设定处理程序，或不匹配任何规则");
					logger.info("[Warning][action=mailScan-invalidMailTask][result=Ignore][message={}][seq={}]","未设定处理程序，或不匹配任何规则", mt.getSeq());
					continue;
				}
				
				MailServerConfig msc = this.dbOperation.getMailServerConfigrationById(msid);
				if(msc == null){
					mt.setCode(Constants.INVALID);
					invalidMailTaskList.add(mt);
					this.dbOperation.saveMailJob(mt, mt.getToMail(), schedule.getId(), Constants.SEND_END,
							Constants.INVALID, "处理程序未找到，或已删除");
					logger.info("[Warning][action=mailScan-invalidMailTask][result=Ignore][message={}][seq={}]","处理程序未找到，或已删除", mt.getSeq());
					continue;
				}

				String validAddress = ValidationUtils.mailAddressValidate(mt.getToMail(), msc.getServerType());
				if (validAddress == null) {
					mt.setCode(Constants.INVALID);
					invalidMailTaskList.add(mt);
					this.dbOperation.saveMailJob(mt, mt.getToMail(), schedule.getId(), Constants.SEND_END,
							Constants.INVALID, "收件人地址无效");
					logger.info("[Warning][action=mailScan-invalidMailTask][result=Ignore][message={}][seq={}]","收件人地址无效", mt.getSeq());
					continue;
				}

				this.dbOperation.saveMailJob(mt, validAddress, schedule.getId());
			}
		}

		logger.trace("The invalid task size is {}", invalidMailTaskList.size());
		this.updateMailStatus(invalidMailTaskList, dsc);
	}

	// save short message task
	private void saveSMTask(List<ShortMessageTask> smTaskList, Schedule schedule, DatasourceConfig dsc) {
		List<ShortMessageTask> invalidSMTaskList = new ArrayList<>();
		if (smTaskList != null && smTaskList.size() > 0) {
			for (ShortMessageTask smt : smTaskList) {

				BatchJob batchJob = new BatchJob();
				batchJob.setActionType(schedule.getActionType().shortValue());
				batchJob.setFromName(smt.getStaffName());
				batchJob.setFromId(smt.getStaffNo());
				batchJob.setTo(smt.getToMobileNo());
				batchJob.setToAddressList(smt.getToMobileNo());
				batchJob.setSubject(smt.getSubject());
				batchJob.setContent(smt.getContent());
				batchJob.setAttachment(smt.getAttach());

				// validate to phone number string. only support Chinese phone
				// number.
				String validAddress = ValidationUtils.phoneNumberValidate(smt.getToMobileNo());
				if (validAddress == null) {
					smt.setCode(Constants.INVALID);
					invalidSMTaskList.add(smt);
					this.dbOperation.saveSMJob(smt, smt.getToMobileNo(), schedule.getId(), Constants.SEND_END,
							Constants.INVALID, "收件人地址无效");
					logger.info("[Warning][action=mailScan-invalidMailTask][result=Ignore][message={}][MESSAGE_ID={}]","收件人地址无效", smt.getMessageId());
					continue;
				}

				this.dbOperation.saveSMJob(smt, validAddress, schedule.getId());
			}
		}

		logger.trace("The invalid task size is {}", invalidSMTaskList.size());
		this.updateSMStatus(invalidSMTaskList, dsc);
	}
}
