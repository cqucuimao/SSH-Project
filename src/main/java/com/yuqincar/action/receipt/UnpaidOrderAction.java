package com.yuqincar.action.receipt;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.domain.order.Order;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.utils.DateUtils;

@Controller
@Scope("prototype")
public class UnpaidOrderAction implements ModelDriven<Order> {
	
	private Order model=new Order();
	
	@Autowired
	OrderService orderService;
	
	public String list(){
		//重置开始时间 00:00:00
	    Date beginDate=DateUtils.getMinDate(model.getActualBeginDate());
	    //重置结束时间23:59:59
		Date endDate=DateUtils.getMaxDate(model.getActualEndDate());
		
		String orgName=null;
		if(model.getCustomerOrganization()!=null)
			orgName=model.getCustomerOrganization().getName();		
		List<Order> orderList=orderService.getUnpaidOrderByOrgNameAndTime(orgName,beginDate, endDate);
		ActionContext.getContext().put("orderList", orderList);
		return "list";
	}
	
	public String home(){
		return "home";
	}
	
	public Order getModel() {
		return model;
	}

}
