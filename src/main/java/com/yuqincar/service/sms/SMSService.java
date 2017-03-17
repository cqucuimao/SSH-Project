package com.yuqincar.service.sms;

import java.util.Map;

public interface SMSService {
	public final static String SMS_TEMPLATE_MILE_ORDER_ACCEPTED = "91550674";
	public final static String SMS_TEMPLATE_DAY_ORDER_ACCEPTED = "91550675";
	public final static String SMS_TEMPLATE_EXAMINE_APPOINTMENT = "91550677";
	public final static String SMS_TEMPLATE_REPAIR_APPOINTMENT = "91550678";
	public final static String SMS_TEMPLATE_VERFICATION_CODE = "91550121";
	public final static String SMS_TEMPLATE_ORDER_POSTPONE = "91550688";
	public final static String SMS_TEMPLATE_ORDER_CANCELLED = "91550689";
	public final static String SMS_TEMPLATE_ORDER_ENQUEUE = "91550805";
	public final static String SMS_TEMPLATE_NEW_ORDER = "91552375";
	public final static String SMS_TEMPLATE_NEW_ORDER_INCLUDE_OTHER_PASSENGER = "91552376";
	public final static String SMS_TEMPLATE_CARCARE_APPOINTMENT_GENERATED_FOR_DRIVER="91551668";
	public final static String SMS_TEMPLATE_CARCARE_APPOINTMENT_GENERATED_FOR_MANAGER="91551669";
	public final static String SMS_TEMPLATE_CARCARE_APPOINTMENT_GENERATED_NO_DRIVER="91551670";
	public final static String SMS_TEMPLATE_CARCARE_APPOINTMENT_GENERATED_FOR_4S_EMPLOYEE="91551817";
	public final static String SMS_TEMPLATE_CARCARE_NEARBY="91551818";
	public final static String SMS_TEMPLATE_RESERVECARAPPLYORDER_SUBMITTED_FOR_APPROVEUSER="91552057";
	public final static String SMS_TEMPLATE_RESERVECARAPPLYORDER_REJECTED_FOR_PROPOSER="91552058";
	public final static String SMS_TEMPLATE_RESERVECARAPPLYORDER_APPROVED_FOR_PROPOSER_CARAPPROVEUSER_DRIVERAPPROVEUSER="91552059";
	public final static String SMS_TEMPLATE_RESERVECARAPPLYORDER_CARAPPROVED_FOR_PROPOSER="91552060";
	public final static String SMS_TEMPLATE_RESERVECARAPPLYORDER_DRIVERAPPROVED_FOR_PROPOSER="91552061";
	public final static String SMS_TEMPLATE_ORDER_END="91552062";
	public final static String SMS_TEMPLATE_RESCHEDULE="91552286";
	
	
	
	
	/**
	 * 发送模板短信
	 * @param templateId
	 * @param params
	 * @param devMode
	 * @return identifier 相当于短信id
	 */
	
	public void sendTemplateSMS(String phoneNumber,String templateId,Map<String,String> params);
	
	public void sendInstantTemplateSMS(String phoneNumber,String templateId,Map<String,String> params);
	
	public void sendSMSInQueue();
}
