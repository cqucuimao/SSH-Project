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
				return carService.getCarById(id);
			}else
			{
				String name=values[0];
				char ch='(';
				if(name.contains(Character.toString(ch)))
				{
					int index=name.indexOf('(');
					name=name.substring(0, index);
					return carService.getCarByPlateNumber(name);
				}
				else
					return carService.getCarByPlateNumber(values[0]);
				
			}
		}
		return null;
    }  
  
    public String convertToString(Map context, Object o) {
    	Car car=(Car)o;
    	if(car!=null)
    		return String.valueOf(car.getId());
    	else
    		return "";  
    }  
}
