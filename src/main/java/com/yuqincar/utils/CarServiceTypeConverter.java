package com.yuqincar.utils;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuqincar.domain.car.CarServiceType;
import com.yuqincar.service.car.CarService;

public class CarServiceTypeConverter extends StrutsTypeConverter {
	@Autowired
	private CarService carService;
	
	public Object convertFromString(Map context, String[] values, Class toClass) {  
		if(toClass==CarServiceType.class){
			Long id=Long.valueOf((String)values[0]);
			return carService.getCarServiceTypeById(id);
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
