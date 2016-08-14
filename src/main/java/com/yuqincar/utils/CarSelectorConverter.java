package com.yuqincar.utils;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarServiceType;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.privilege.UserService;

public class CarSelectorConverter extends StrutsTypeConverter {
	@Autowired
	private CarService carService;
	
	public Object convertFromString(Map context, String[] values, Class toClass) {  
		if(toClass==Car.class){
			String plateNumber=(String)values[0];
			return carService.getCarByPlateNumber(plateNumber);
		}
		return null;
    }  
  
    public String convertToString(Map context, Object o) {
    	CarServiceType serviceType=(CarServiceType)o;
    	if(serviceType!=null)
    		return String.valueOf(serviceType.getId());
    	else
    		return "";  
    }  
}
