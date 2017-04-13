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
			String rValue=values[0];
			boolean isNumber=true;
			for(int i=0;i<rValue.length();i++)
			{
				if(!(rValue.charAt(i)>='0'&&rValue.charAt(i)<='9'))
				{
					isNumber=false;
					break;
				}
			}
			if(isNumber)
			{
				Long id=Long.valueOf(values[0]);
				return userService.getById(id);
			}else
			{
				return userService.getByName(values[0]);
			}
			
		}
		return null;
    }  
  
    public String convertToString(Map context, Object o) {
    	User user=(User)o;
    	if(user!=null)
    		return String.valueOf(user.getId());
    	else
    		return "";  
    }  
}
