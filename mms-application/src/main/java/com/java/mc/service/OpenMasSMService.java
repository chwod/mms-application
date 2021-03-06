package com.java.mc.service;
//package cn.com.vgtech.mc.service;
//
//import java.util.regex.Pattern;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import com.chinamobile.openmas.client.Sms;
//
//import cn.com.vgtech.mc.bean.BatchJob;
//import cn.com.vgtech.mc.utils.Constants;
//
//@Component
//public class OpenMasSMService implements Service{
//
//	private static final Logger logger = LoggerFactory.getLogger(OpenMasSMService.class);
//
//	@Override
//	public void doAction(BatchJob batchJob) throws Exception {
//		if (batchJob != null && batchJob.getSmConfig() != null) {
//			if (Constants.SHORT_MESSAGE_TUNNEL_OPENMAS == batchJob.getSmConfig().getSmTunnel()) {
//				if (Constants.WEB_SERVICE.equalsIgnoreCase(batchJob.getSmConfig().getServiceType())) {
//					Sms sms = new Sms(batchJob.getSmConfig().getServiceAddress());
//					String[] destinationAddresses = batchJob.getToList();
//					String message = batchJob.getContent();
//					String extendCode = batchJob.getSmConfig().getExtendCode() == null ? null
//							: batchJob.getSmConfig().getExtendCode().toString();
//					String ApplicationID = batchJob.getSmConfig().getApplicationId() == null ? null
//							: batchJob.getSmConfig().getApplicationId().toString();
//					String Password = batchJob.getSmConfig().getApplicationPassword();
//
//					String gatewayId = sms.SendMessage(destinationAddresses, message, extendCode, ApplicationID,
//							Password);
//					if (gatewayId != null && Pattern.matches(
//							"[a-zA-Z0-9]{8}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{4}-[a-zA-Z0-9]{12}", gatewayId)) {
//						batchJob.setGatewayId(gatewayId);
//						logger.info("[action=sendsm][status=success][from={}][to={}][returnMessage={}]",
//								new StringBuffer(String.valueOf(batchJob.getSmConfig().getSmAccessNumber()))
//										.append(batchJob.getSmConfig().getExtendCode() == null ? ""
//												: batchJob.getSmConfig().getExtendCode())
//										.toString(),
//								batchJob.getToAddressList(), gatewayId);
//						return;
//					} else {
//						batchJob.setMessage(gatewayId);
//						logger.info("[action=sendsm][status=failed][from={}][to={}][returnMessage={}]",
//								new StringBuffer(String.valueOf(batchJob.getSmConfig().getSmAccessNumber()))
//										.append(batchJob.getSmConfig().getExtendCode() == null ? ""
//												: batchJob.getSmConfig().getExtendCode())
//										.toString(),
//								batchJob.getToAddressList(), gatewayId);
//						throw new Exception(gatewayId);
//					}
//				} else {
//					throw new Exception("不支持的服务类型");
//				}
//			} else {
//				throw new Exception("不支持的短信通道");
//			}
//		}
//		throw new Exception("非法数据");
//	}
//
////	public static void main(String... strings) {
////		try {
////			Sms sms = new Sms("http://nb067.openmas.net:9080/OpenMasService");
////			String[] destinationAddresses = new String[] { "13439693838" };
////			String message = "短信测试";
////			String extendCode = "";
////			String ApplicationID = "8007001";
////			String Password = "dAG3dk3DVpPy";
////
////			String gatewayId = sms.SendMessage(destinationAddresses, message, extendCode, ApplicationID, Password);
////			System.out.println(gatewayId);
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
////	}
//}
