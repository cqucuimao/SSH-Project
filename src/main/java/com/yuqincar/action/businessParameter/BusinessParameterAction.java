package com.yuqincar.action.businessParameter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.businessParameter.BusinessParameter;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarStatusEnum;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.businessParameter.BusinessParameterService;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class BusinessParameterAction extends BaseAction{
	
	@Autowired
	private BusinessParameterService businessParameterService;
	
	public String employees(){
		
		BusinessParameter businessParameter = businessParameterService.getBusinessParameter();
		System.err.println("%%%"+businessParameter.getEmployeesIn4SForSMS());
		List<User> for4sUser = businessParameter.getEmployeesIn4SForSMS();
		List<User> forCarCareUser = businessParameter.getEmployeesForCarCareAppointmentSMS();
		
		ActionContext.getContext().put("for4sUserList", for4sUser);
		ActionContext.getContext().put("forCarCareUserList", forCarCareUser);
		return "employees";
	}

}
