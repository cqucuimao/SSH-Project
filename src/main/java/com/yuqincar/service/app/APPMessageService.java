package com.yuqincar.service.app;

import java.util.Map;

import com.yuqincar.domain.order.Customer;
import com.yuqincar.domain.privilege.User;

public interface APPMessageService {
	public void sendMessageToDriverAPP(User user,String message,Map<String,Object> params);
	
	public void sendMessageToCustomerAPP(Customer customer, String message,Map<String,Object> params);
	
	public void sendMessageToSchedulerAPP(User user, String message,Map<String,Object> params);
}
