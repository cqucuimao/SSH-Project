package com.yuqincar.install;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.yuqincar.domain.order.ChargeModeEnum;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.OrderSourceEnum;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.service.sms.SMSService;
import com.yuqincar.utils.CommonUtils;
import com.yuqincar.utils.DateUtils;

@Component
public class SMSTest {
	@Autowired
	private SMSService smsService;
	@Autowired
	private OrderService orderService;
	
	public void sendSMS(){
		Map<String,String> params=new HashMap<String,String>();
		String message=null;
		
		Order order=orderService.getOrderBySN("YQ170200009");
		
		params.put("customerOrganization", order.getCustomerOrganization().getName());
		params.put("customerName", order.getCustomer().getName());
		params.put("phoneNumber",order.getPhone());
		params.put("time", DateUtils.getYMDString(order.getPlanBeginDate())+" 到 "+DateUtils.getYMDString(order.getPlanEndDate()));
		message=smsService.sendTemplateSMS(order.getDriver().getPhoneNumber(), SMSService.SMS_TEMPLATE_NEW_ORDER, params);
		params.clear();
		System.out.println("message="+message);
		
		params.put("driverName", order.getDriver().getName());
		params.put("plateNumber", order.getCar().getPlateNumber());
		params.put("driverPhoneNumber", order.getDriver().getPhoneNumber());
		params.put("customerSurname", CommonUtils.getSurname(order.getCustomer().getName()));
		params.put("schedulerName", order.getScheduler().getName());
		params.put("planBeginDate", DateUtils.getYMDString(order.getPlanBeginDate()));
		message=smsService.sendTemplateSMS(order.getPhone(), SMSService.SMS_TEMPLATE_DAY_ORDER_ACCEPTED, params);
		params.clear();		
		System.out.println("message="+message);
		
	}

	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		SMSTest test = (SMSTest) ac.getBean("SMSTest");
		test.sendSMS();
	}

}
