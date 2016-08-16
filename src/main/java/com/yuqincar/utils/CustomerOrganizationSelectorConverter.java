package com.yuqincar.utils;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuqincar.domain.order.CustomerOrganization;
import com.yuqincar.service.CustomerOrganization.CustomerOrganizationService;

public class CustomerOrganizationSelectorConverter extends StrutsTypeConverter {
	@Autowired
	private CustomerOrganizationService  customerOrganizationService;
	
	public Object convertFromString(Map context, String[] values, Class toClass) {  
		if(toClass==CustomerOrganization.class){
			Long id=Long.valueOf((String)values[0]);
			return customerOrganizationService.getById(id);
		}
		return null;
    }  
  
    public String convertToString(Map context, Object o) {
    	System.out.println("in convertToString");
    	CustomerOrganization co=(CustomerOrganization)o;
    	if(co!=null)
    		return String.valueOf(co.getId());
    	else
    		return "";  
    }  
}
