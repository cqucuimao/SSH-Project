package com.yuqincar.service.app;

import com.yuqincar.domain.order.Customer;
import com.yuqincar.domain.privilege.User;

public interface APPMessageService {
	public void sendMessage(User user,String message);
	
	public void sendMessage(Customer customer, String message);
}
