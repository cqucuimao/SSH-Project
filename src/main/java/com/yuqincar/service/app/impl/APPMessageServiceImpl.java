package com.yuqincar.service.app.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dbay.apns4j.IApnsService;
import com.dbay.apns4j.impl.ApnsServiceImpl;
import com.dbay.apns4j.model.ApnsConfig;
import com.dbay.apns4j.model.Payload;
import com.yuqincar.domain.order.APPRemindMessage;
import com.yuqincar.domain.order.Customer;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.domain.privilege.UserAPPDeviceTypeEnum;
import com.yuqincar.service.app.APPMessageService;
import com.yuqincar.service.app.APPRemindMessageService;
import com.yuqincar.utils.Configuration;

@Service
public class APPMessageServiceImpl implements APPMessageService {
	private static IApnsService apnsDriverAPPService;
	private static IApnsService apnsCustomerAPPService;

	@Autowired
	private APPRemindMessageService appRemindMessageService;
	
	private void sendMessage(IApnsService service, UserAPPDeviceTypeEnum deviceType, String appToken, String message,Map<String,Object> params){
		System.out.println("in SendMessage");
		System.out.println("deviceType="+deviceType);
		System.out.println("appToken="+appToken);
		System.out.println("message="+message);
		if(params!=null){
			for(String key:params.keySet())
				System.out.println(key+"="+params.get(key));
		}
		if(deviceType==UserAPPDeviceTypeEnum.ANDROID){
			//TODO 目前采用app轮训，需要插入数据库。后期应该改为百度云推送
//			APPRemindMessage arm=new APPRemindMessage();
//			arm.setUser(user);
//			arm.setDescription(message);
//			arm.setSended(false);
//			appRemindMessageService.save(arm);
			
		}else if(deviceType==UserAPPDeviceTypeEnum.IOS){
			Payload payload = new Payload();
			payload.setAlert(message);
			payload.setSound("default");
			//TODO setBadge的含义是设置手机桌面图标上的红色数字，目前用1，后期用未开始订单数量代替。
			payload.setBadge(1); 
			if(params!=null && params.keySet().size()>0)
				for(String key : params.keySet())
					payload.addParam(key, params.get(key));
			System.out.println("in SendMessage 1");
			service.sendNotification(appToken, payload);
			System.out.println("in SendMessage 2");
		}
	}
	
	public void sendMessageToDriverAPP(User user, String message,Map<String,Object> params) {		
		sendMessage(getApnsServiceForDriverAPP(),user.getAppDeviceType(),user.getAppDeviceToken(),message,params);		
	}

	public void sendMessageToCustomerAPP(Customer customer, String message,Map<String,Object> params) {
		sendMessage(getApnsServiceForCustomerAPP(), customer.getAppDeviceType(),customer.getAppDeviceToken(),message,params);
	}
	
	public void sendMessageToSchedulerAPP(User user, String message,Map<String,Object> params) {
		//TODO
	}
	


	private static IApnsService getApnsServiceForDriverAPP() {
		if (apnsDriverAPPService == null) {
			ApnsConfig config = new ApnsConfig();
			config.setKeyStore(Configuration.getIOSDriverPushKeyStore());
			config.setDevEnv(true);
			config.setPassword(Configuration.getAppleDeveloperPassword());
			config.setPoolSize(5);
			config.setName("DriverAPP");
			apnsDriverAPPService = ApnsServiceImpl.createInstance(config);
		}
		return apnsDriverAPPService;
	}
	
	private static IApnsService getApnsServiceForCustomerAPP() {
		if (apnsCustomerAPPService == null) {
			ApnsConfig config = new ApnsConfig();
			config.setKeyStore(Configuration.getIOSCustomerPushKeyStore());
			config.setDevEnv(true);
			config.setPassword(Configuration.getAppleDeveloperPassword());
			config.setPoolSize(5);
			config.setName("CustomerAPP");
			apnsCustomerAPPService = ApnsServiceImpl.createInstance(config);
		}
		return apnsCustomerAPPService;
	}

}
