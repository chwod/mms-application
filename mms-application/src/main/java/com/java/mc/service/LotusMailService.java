package com.java.mc.service;
//package cn.com.vgtech.mc.service;
//
//import java.util.Vector;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import cn.com.vgtech.mc.bean.BatchJob;
//import cn.com.vgtech.mc.utils.Constants;
//import lotus.domino.Database;
//import lotus.domino.Document;
//import lotus.domino.EmbeddedObject;
//import lotus.domino.MIMEEntity;
//import lotus.domino.NotesFactory;
//import lotus.domino.RichTextItem;
//import lotus.domino.Session;
//import lotus.domino.Stream;
//
//@Component
//public class LotusMailService implements Service {
//	private static final Logger logger = LoggerFactory.getLogger(LotusMailService.class);
//
//	@Override
//	public void doAction(BatchJob batchJob) throws Exception {
//
//		Session session;
//		if (Constants.LOTUS_CONNECTION_IOR == batchJob.getMsConfig().getConnType()) {
//			session = NotesFactory.createSessionWithIOR(batchJob.getMsConfig().getIor(), batchJob.getSenderUserName(),
//					batchJob.getSenderPassword());
//		} else {
//			String lotusServerAddress = new StringBuffer(batchJob.getMsConfig().getSmtpHost()).append(":")
//					.append(batchJob.getMsConfig().getSmtpPort()).toString();
//			session = NotesFactory.createSession(lotusServerAddress, batchJob.getSenderUserName(),
//					batchJob.getSenderPassword());
//		}
//		Database db = session.getDatabase(null, batchJob.getMsConfig().getMailFile());
//		Document doc = db.createDocument();
//
//		doc.appendItemValue(Constants.SUBJECT, batchJob.getSubject());
//		MIMEEntity body = doc.createMIMEEntity(Constants.BODY);
//		Stream stream = session.createStream();
//		stream.writeText(batchJob.getContent());
//		body.setContentFromText(stream, "text/html;charset=UTF-8", MIMEEntity.ENC_NONE);
//		stream.close();
//
//		// add attachment file
//		if (batchJob.getAttachment() != null && batchJob.getAttachmentFile() != null) {
//			RichTextItem attachment = doc.createRichTextItem("attachment");
//			attachment.embedObject(EmbeddedObject.EMBED_ATTACHMENT, "", batchJob.getAttachmentFile().getAbsolutePath(),
//					null);
//		}
//
//		Vector<String> sendto = new Vector<String>();
//		for (String email : batchJob.getToList()) {
//			sendto.addElement(email);
//		}
//		try {
//			doc.send(false, sendto);
//			logger.info("Send the mail from {} to {} success.", batchJob.getSenderUserName(),
//					batchJob.getToAddressList());
//		} catch (Exception e) {
//			logger.info("Failed to send the mail from {} to {}, the failed meesage is {}", batchJob.getSenderUserName(),
//					batchJob.getToAddressList(), e.getMessage());
//			throw e;
//		}
//	}
//}
