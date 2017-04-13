package com.yuqincar.utils;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;
import org.springframework.beans.factory.annotation.Autowired;

import com.yuqincar.domain.common.BaseEnum;
import com.yuqincar.domain.order.CustomerOrganization;
import com.yuqincar.service.CustomerOrganization.CustomerOrganizationService;

public class EnumSelectorConverter extends StrutsTypeConverter {
	
	public Object convertFromString(Map context, String[] values, Class toClass) {
		if(values[0].equals("-1"))
			return null;
		BaseEnum baseEnum=(BaseEnum)toClass.getEnumConstants()[0];
		return baseEnum.getById(Integer.parseInt(values[0]));
    }  
  
    public String convertToString(Map context, Object o) {
		BaseEnum be=(BaseEnum)o;
		if(be!=null)
    		return be.getLabel();
    	else
    		return "";  
    }  
}
