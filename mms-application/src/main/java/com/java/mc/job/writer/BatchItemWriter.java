package com.java.mc.job.writer;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.java.mc.bean.BatchJob;
import com.java.mc.bean.DatasourceConfig;
import com.java.mc.db.DBConnection;
import com.java.mc.db.DBOperation;
import com.java.mc.utils.Constants;
import com.java.mc.utils.DBUtils;

public class BatchItemWriter implements ItemWriter<BatchJob> {
	
	@Autowired
	private DBOperation dbOperation;
	
	@Autowired
	private DBConnection dbConnection;

	@Override
	public void write(List<? extends BatchJob> batchJobList) throws Exception {
		if (batchJobList != null && batchJobList.size() > 0) {
			this.dbOperation.updateScheduleJobStatus(batchJobList);
			
			// update remote status
			for(BatchJob batchJob : batchJobList){
				DatasourceConfig dsc = this.dbOperation.getDSConfigurationById(batchJob.getDsid());
				
				//mail action status  
				if(Constants.ACTION_MAIL_SCAN == batchJob.getActionType()){
					String remoteSQL = DBUtils.getSQL("UPDATE :t SET ERROR_CODE = ?, SEND_DT = ? WHERE SEQ = ?",dsc.getSqlType(),dsc.getArchName(),
							Constants.WEBMAIL_V2);
					this.dbConnection.getRemoteJdbcTemplate(batchJob.getDsid()).update(remoteSQL, new PreparedStatementSetter() {
						
						@Override
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setInt(1, batchJob.getCode());
							ps.setString(2, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Timestamp(System.currentTimeMillis())));
							ps.setLong(3, Long.valueOf(batchJob.getSeq()));
						}
					});
				}
				
				//short message status
				if(Constants.ACTION_SM_SCAN == batchJob.getActionType()){
					String remoteSQL = DBUtils.getSQL("UPDATE :t SET ERROE_CODE = ?, SEND_DATE = ?, SEND_TIME = ? WHERE MESSAGE_ID = ?",dsc.getSqlType(),dsc.getArchName(),
							Constants.WEB_SMS);
					this.dbConnection.getRemoteJdbcTemplate(batchJob.getDsid()).update(remoteSQL, new PreparedStatementSetter() {
						
						@Override
						public void setValues(PreparedStatement ps) throws SQLException {
							Long now = System.currentTimeMillis();
							ps.setShort(1, batchJob.getCode());
							ps.setTimestamp(2, new Timestamp(now));
							ps.setString(3, new SimpleDateFormat("HHmmss").format(new Timestamp(now)));
							ps.setLong(4, Long.valueOf(batchJob.getSeq()));
						}
					});
				}
			}
		}
	}
}
