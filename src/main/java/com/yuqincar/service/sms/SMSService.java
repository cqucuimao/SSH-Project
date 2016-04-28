package com.yuqincar.service.sms;

import java.util.Map;

public interface SMSService {
	public final static String SMS_TEMPLATE_MILE_ORDER_ACCEPTED = "91550674";
	public final static String SMS_TEMPLATE_DAY_ORDER_ACCEPTED = "91550675";
	public final static String SMS_TEMPLATE_CARCARE_APPOINTMENT = "91550676";
	public final static String SMS_TEMPLATE_EXAMINE_APPOINTMENT = "91550677";
	public final static String SMS_TEMPLATE_REPAIR_APPOINTMENT = "91550678";
	public final static String SMS_TEMPLATE_VERFICATION_CODE = "91550121";
	public final static String SMS_TEMPLATE_ORDER_POSTPONE = "91550688";
	public final static String SMS_TEMPLATE_ORDER_CANCELLED = "91550689";
	public final static String SMS_TEMPLATE_ORDER_ENQUEUE = "91550805";
	/**
	 * 发送模板短信
	 * @param templateId
	 * @param params
	 * @param devMode
	 * @return identifier 相当于短信id
	 */
	
	public String sendTemplateSMS(String phoneNumber,String templateId,Map<String,String> params);
	
}
