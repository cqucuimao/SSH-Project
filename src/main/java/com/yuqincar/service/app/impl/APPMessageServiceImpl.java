package com.yuqincar.service.app.impl;

import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.model.PushMsgToSingleDeviceRequest;
import com.baidu.yun.push.model.PushMsgToSingleDeviceResponse;
import com.dbay.apns4j.IApnsService;
import com.dbay.apns4j.impl.ApnsServiceImpl;
import com.dbay.apns4j.model.ApnsConfig;
import com.dbay.apns4j.model.Payload;
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
	private static PushKeyPair driverPushKeyPair;
	private static PushKeyPair customerPushKeyPair;

	@Autowired
	private APPRemindMessageService appRemindMessageService;
	
	private void sendMessageToApple(IApnsService service, String appToken, String message,Map<String,Object> params){
		if(appToken==null)
			return;
		
		if(params!=null){
			for(String key:params.keySet())
				System.out.println(key+"="+params.get(key));
		}
			
		Payload payload = new Payload();
		payload.setAlert(message);
		payload.setSound("default");
		//TODO setBadge的含义是设置手机桌面图标上的红色数字，目前用1，后期用未开始订单数量代替。
		payload.setBadge(1); 
		if(params!=null && params.keySet().size()>0)
			for(String key : params.keySet())
				payload.addParam(key, params.get(key));
		service.sendNotification(appToken, payload);
	}
	
	private void sendMessageToAndroid(PushKeyPair pushKeyPair, String appToken, String message,Map<String,Object> params){
		if(appToken==null)
			return;
		
		System.out.println("in sendMessageToAndroid");
		// 创建BaiduPushClient，访问SDK接口
		BaiduPushClient pushClient=new BaiduPushClient(pushKeyPair,BaiduPushConstants.CHANNEL_REST_URL);
		System.out.println("1");
		/*
		// 注册YunLogHandler，获取本次请求的交互信息
		pushClient.setChannelLogHandler (new YunLogHandler () {
			public void onHandle (YunLogEvent event) {
				System.out.println(event.getMessage());
		    }
		});
		*/
		
		try {
			System.out.println("2");
			//message为推送的具体内容，具体格式参见http://push.baidu.com/doc/restapi/msg_struct
			JSONObject notification = new JSONObject();
		    notification.put("title", "渝勤汽车");
		    notification.put("description",message);
			System.out.println("3");
		    /*    	
			notification.put("notification_builder_id", 0);
		    notification.put("notification_basic_style", 4);
		    notification.put("open_type", 1);
		    notification.put("url", "http://push.baidu.com");
		    */
		    JSONObject customContent = new JSONObject();
			System.out.println("4");
		    if(params!=null && params.keySet()!=null && params.keySet().size()>0)
		    	for(String key : params.keySet())
		    		customContent.put(key, params.get(key));
		    notification.put("custom_content", customContent);
			System.out.println("5");
					
			// 设置请求参数，创建请求实例
			PushMsgToSingleDeviceRequest request=new PushMsgToSingleDeviceRequest().
					addChannelId(appToken). //设备token
					addDeviceType(3).  //3 is for android
					addMessageType(1). //1标示推送为通知
					addMessage(notification.toString());

			System.out.println("6");
			// 执行Http请求
			PushMsgToSingleDeviceResponse response=pushClient.pushMsgToSingleDevice(request);
					
			// Http请求返回值解析
			System.out.println("msgId:"+response.getMsgId()+",sendTime:"+response.getSendTime());
					
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessageToDriverAPP(User user, String message,Map<String,Object> params) {
		if(user.getAppDeviceType()==UserAPPDeviceTypeEnum.IOS)
			sendMessageToApple(getApnsServiceForDriverAPP(),user.getAppDeviceToken(),message,params);
		else
			sendMessageToAndroid(getBaiduPushKeyPairForDriverAPP(),user.getAppDeviceToken(),message,params);
	}

	public void sendMessageToCustomerAPP(Customer customer, String message,Map<String,Object> params) {
		if(customer.getAppDeviceType()==UserAPPDeviceTypeEnum.IOS)
			sendMessageToApple(getApnsServiceForCustomerAPP(),customer.getAppDeviceToken(),message,params);
		else
			sendMessageToAndroid(getBaiduPushKeyPairForCustomerAPP(),customer.getAppDeviceToken(),message,params);
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
	
	private static PushKeyPair getBaiduPushKeyPairForDriverAPP(){
		if (driverPushKeyPair == null) 
			driverPushKeyPair=new PushKeyPair(Configuration.getBaiduDriverPushApiKey(), Configuration.getBaiduDriverPushSecretKey());
		return driverPushKeyPair;
	}
	
	private static PushKeyPair getBaiduPushKeyPairForCustomerAPP(){
		if (customerPushKeyPair == null) 
			customerPushKeyPair=new PushKeyPair(Configuration.getBaiduCustomerPushApiKey(), Configuration.getBaiduCustomerPushSecretKey());
		return customerPushKeyPair;
	}

}
