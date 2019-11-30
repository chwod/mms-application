package com.java.mc.service;

import java.security.Security;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.java.mc.bean.BatchJob;
import com.java.mc.utils.Constants;

@Component
public class GeneralMailService implements Service {
	private static final Logger logger = LoggerFactory.getLogger(GeneralMailService.class);

	@Override
	@SuppressWarnings("restriction")
	public void doAction(BatchJob batchJob) throws Exception {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(batchJob.getMsConfig().getSmtpHost());
		mailSender.setPort(batchJob.getMsConfig().getSmtpPort());
		MimeMessage mailMessage = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,
				MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "gbk");
		messageHelper.setTo(batchJob.getToList());
		messageHelper.setFrom(batchJob.getSenderAddress(), batchJob.getSenderTitle());
		messageHelper.setSubject(batchJob.getSubject());
		messageHelper.setText(batchJob.getContent(), true);
		if (batchJob.getAttachment() != null && batchJob.getAttachmentFile() != null) {
			messageHelper.addAttachment(batchJob.getAttachment(), batchJob.getAttachmentFile());
		}

		Properties prop = new Properties();
		if (batchJob.getMsConfig().isAuth()) {
			prop.put("mail.smtp.auth", true);
			if ((Constants.SERVER_TYPE_EXCHANGE == batchJob.getMsConfig().getServerType())
					&& (batchJob.getMsConfig().getDomainName() != null)
					&& (batchJob.getMsConfig().getDomainName().trim().length() > 0)) {
				mailSender.setUsername(new StringBuffer(batchJob.getMsConfig().getDomainName()).append("\\")
						.append(batchJob.getSenderUserName()).toString());
			} else {
				mailSender.setUsername(batchJob.getSenderUserName());
			}
			mailSender.setPassword(batchJob.getSenderPassword());
		} else {
			prop.put("mail.smtp.auth", false);
		}

		if (batchJob.getMsConfig().isSsl()) {
			Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
			prop.put("mail.smtp.socketFactory.class", Constants.SSL_FACTORY);
			prop.put("mail.smtp.socketFactory.fallback", "false");
			prop.put("mail.smtp.socketFactory.port", batchJob.getMsConfig().getSmtpPort());
		}

		if (batchJob.getMsConfig().isTls()) {
			prop.put("mail.smtp.starttls.enable", "true");
			prop.put("mail.smtp.ssl.checkserveridentity", "false");
			prop.put("mail.smtp.ssl.trust", batchJob.getMsConfig().getSmtpHost());
		}
		
		prop.put("mail.smtp.connectiontimeout", "10000");
		prop.put("mail.smtp.timeout", "10000");
		
		mailSender.setJavaMailProperties(prop);
		try {
			mailSender.send(mailMessage);
			logger.info("[action=sendMail][result=success][from={}][to={}]", batchJob.getSenderAddress(),
					batchJob.getToAddressList());
		} catch (Exception e) {
			logger.info("[action=sendMail][result=failed][from={}][to={}][message={}]", batchJob.getSenderAddress(),
					batchJob.getToAddressList(), e.getMessage());
			throw e;
		}
	}
}
