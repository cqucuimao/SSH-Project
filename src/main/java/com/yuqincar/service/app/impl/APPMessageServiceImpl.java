package com.yuqincar.service.app.impl;

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
	private static IApnsService apnsService;

	@Autowired
	private APPRemindMessageService appRemindMessageService;
	
	private void sendMessage(UserAPPDeviceTypeEnum deviceType, String appToken, String message){
		if(deviceType==UserAPPDeviceTypeEnum.ANDROID){
			//TODO 目前采用app轮训，需要插入数据库。后期应该改为百度云推送
//			APPRemindMessage arm=new APPRemindMessage();
//			arm.setUser(user);
//			arm.setDescription(message);
//			arm.setSended(false);
//			appRemindMessageService.save(arm);
			
		}else if(deviceType==UserAPPDeviceTypeEnum.IOS){
			IApnsService service = getApnsService();
			Payload payload = new Payload();
			payload.setAlert(message);
			payload.setSound("default");
			//TODO setBadge的含义是设置手机桌面图标上的红色数字，目前用1，后期用未开始订单数量代替。
			payload.setBadge(1); 
			service.sendNotification(appToken, payload);
		}
	}
	
	public void sendMessage(User user, String message) {		
		sendMessage(user.getAppDeviceType(),user.getAppDeviceToken(),message);		
	}

	public void sendMessage(Customer customer, String message) {
		sendMessage(customer.getAppDeviceType(),customer.getAppDeviceToken(),message);
	}
	


	private static IApnsService getApnsService() {
		if (apnsService == null) {
			ApnsConfig config = new ApnsConfig();
			config.setKeyStore(Configuration.getIOSPushKeyStore());
			config.setDevEnv(true);
			config.setPassword(Configuration.getAppleDeveloperPassword());
			config.setPoolSize(5);
			apnsService = ApnsServiceImpl.createInstance(config);
		}
		return apnsService;
	}

}
