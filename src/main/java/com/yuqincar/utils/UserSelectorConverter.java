package com.yuqincar.utils;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuqincar.domain.car.CarServiceType;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.privilege.UserService;

public class UserSelectorConverter extends StrutsTypeConverter {
	@Autowired
	private UserService userService;
	
	public Object convertFromString(Map context, String[] values, Class toClass) {  
		if(toClass==User.class){
			System.out.println("************* i am in converter");
			Long id=Long.valueOf((String)values[0]);
			return userService.getById(id);
		}
		return null;
    }  
  
    public String convertToString(Map context, Object o) {
    	System.out.println("in convertToString");
    	CarServiceType serviceType=(CarServiceType)o;
    	if(serviceType!=null)
    		return String.valueOf(serviceType.getId());
    	else
    		return "";  
    }  
}
