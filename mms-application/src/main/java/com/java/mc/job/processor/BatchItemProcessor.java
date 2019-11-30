package com.java.mc.job.processor;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.FileCopyUtils;

import com.java.mc.bean.BatchJob;
import com.java.mc.bean.Schedule;
import com.java.mc.db.DBOperation;
import com.java.mc.service.BatchService;
import com.java.mc.utils.Constants;

public class BatchItemProcessor implements ItemProcessor<BatchJob, BatchJob> {
	private static final Logger logger = LoggerFactory.getLogger(BatchItemProcessor.class);

	@Autowired
	private DBOperation dbOperation;

	@Autowired
	private BatchService batchService;

	@Value("${vg.batch.delaytime}")
	private Short delayTime;

	@Override
	public BatchJob process(BatchJob batchJob) throws Exception {
		if (batchJob != null) {

			try {
				Resource resource = null;
				if (batchJob.getAttachment() != null && batchJob.getAttachment().trim().length() > 0) {
					Thread.sleep(this.delayTime); //delay time to wait the file was created completely.
					resource = new FileSystemResource(batchJob.getAttachment().trim());
				}
				String content = batchJob.getContent();
				File attachmentFile = null;
				if (resource != null) {
					attachmentFile = resource.getFile();
				}
				Schedule schedule = this.dbOperation.getScheduleById(batchJob.getScheduleId());
				if (schedule != null && schedule.isAttachmentAsContent()) {
					if (content == null || content.trim().length() <= 0) {
						if (resource != null) {
							String fileName = resource.getFilename();
							if (fileName != null && fileName.lastIndexOf(".") > 0) {
								String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
								if (suffix != null && suffix.length() > 0) {
									if (this.dbOperation.getfileSuffixFilterList().contains(suffix.toLowerCase())) {
										content = FileCopyUtils
												.copyToString(new EncodedResource(resource, "gbk").getReader());
									}
								}
							}
						}
					}
					if ((batchJob.getContent() == null || batchJob.getContent().trim().length() <= 0)
							&& (content != null && content.trim().length() > 0)) {
						attachmentFile = null;
					}
				}

				batchJob.setContent(content == null ? "" : content);
				batchJob.setAttachment(attachmentFile == null ? null : attachmentFile.getName());
				batchJob.setAttachmentFile(attachmentFile);

				this.batchService.run(batchJob);
				batchJob.setStatus(Constants.SEND_END);
				batchJob.setCode(Constants.SUCCESS);
				batchJob.setSendTime(new Timestamp(new Date().getTime()));
			} catch (Exception e) {
				batchJob.setStatus(Constants.SEND_END);
				batchJob.setCode(Constants.FAILED);
				batchJob.setMessage(e.getMessage());
				batchJob.setSendTime(new Timestamp(new Date().getTime()));
			}

		}
		return batchJob;
	}
}
