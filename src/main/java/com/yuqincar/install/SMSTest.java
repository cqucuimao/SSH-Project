package com.yuqincar.install;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.yuqincar.service.sms.SMSService;

@Component
public class SMSTest {
	@Autowired
	private SMSService smsService;
	
	public void sendSMS(){
		String phoneNumber="13883101475";
		Map<String,String> params=new HashMap<String,String>();
		String message=null;
		//1. 
		params.put("customerOrganization", "重庆大学计算机学院");
		params.put("time", "2016-08-26 到 2016-08-27");
		message=smsService.sendTemplateSMS(phoneNumber, SMSService.SMS_TEMPLATE_NEW_ORDER, params);
		params.clear();
		System.out.println("message="+message);
		
		//2. 
		params.put("driverName", "王顺涛");
		params.put("plateNumber", "渝AMF053");
		params.put("driverPhoneNumber", "13637990999");
		params.put("fromAddress", "上清寺（卫计委）");
		params.put("planBeginDate", "2016-08-26 14:30");
		params.put("toAddress", "合川区人民政府");
		message=smsService.sendTemplateSMS(phoneNumber, SMSService.SMS_TEMPLATE_MILE_ORDER_ACCEPTED, params);
		params.clear();
		System.out.println("message="+message);
		
		//3. 
		params.put("driverName", "王顺涛");
		params.put("plateNumber", "渝AMF053");
		params.put("driverPhoneNumber", "13637990999");
		params.put("fromAddress", "上清寺（卫计委）");
		params.put("planDate", "2016-08-26 到 2016-08-27");
		message=smsService.sendTemplateSMS(phoneNumber, SMSService.SMS_TEMPLATE_DAY_ORDER_ACCEPTED, params);
		params.clear();
		System.out.println("message="+message);
		
		//4.
		params.put("param1", "手机");
		params.put("param2", "456789");
		message=smsService.sendTemplateSMS(phoneNumber, SMSService.SMS_TEMPLATE_VERFICATION_CODE, params);
		params.clear();
		System.out.println("message="+message);
		
		//5.
		params.put("sn", "YQ16080100001");
		params.put("oriDate", "2016-08-25");
		params.put("newDate", "2016-08-26");
		message=smsService.sendTemplateSMS(phoneNumber, SMSService.SMS_TEMPLATE_ORDER_POSTPONE, params);
		params.clear();
		System.out.println("message="+message);
		
		//6.
		params.put("sn", "YQ16080100001");
		params.put("reason", "客户的会议取消了。");
		message=smsService.sendTemplateSMS(phoneNumber, SMSService.SMS_TEMPLATE_ORDER_CANCELLED, params);
		params.clear();
		System.out.println("message="+message);
		
		//7.
		params.put("customer", "陈恒鑫");
		params.put("customerOrganization","重庆大学计算机学院");
		params.put("phoneNumber", "13883101475");
		params.put("chargeMode", "按天计费");
		params.put("time", "2016-08-26 到 2016-08-27");
		params.put("address", "上清寺（卫计委）");
		message=smsService.sendTemplateSMS(phoneNumber, SMSService.SMS_TEMPLATE_ORDER_ENQUEUE, params);
		params.clear();
		System.out.println("message="+message);
	}

	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		SMSTest test = (SMSTest) ac.getBean("SMSTest");
		test.sendSMS();
	}

}
