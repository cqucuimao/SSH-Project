package com.yuqincar.action.queryStatistics;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.CarCare;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.Order;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class CompletedOrderStatisticAction extends BaseAction {
	
	@Autowired
	private OrderService orderService;
	
	private Date date;
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	/** 列表（今日统计） */
	public String list() throws Exception {
		QueryHelper helper = new QueryHelper("order_", "o");
		
		if(date!=null && !"".equals(date))
			helper.addWhereCondition("? <= o.date and o.date <= ?",DateUtils.getMaxDate(date),DateUtils.getMinDate(date));
				
		PageBean pageBean = orderService.queryOrder(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
	
	/** 本周统计*/
	public String weekList() throws Exception{
		QueryHelper helper = new QueryHelper("order_", "o");
		
		if(date!=null && !"".equals(date))
			helper.addWhereCondition("? <= o.date and o.date <= ?",DateUtils.getWeek(date).getLowDate(),DateUtils.getWeek(date).getHighDate());
		
		PageBean pageBean = orderService.queryOrder(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "weekList";
	}
	
	/** 本月统计*/
	public String monthList() throws Exception{
		QueryHelper helper = new QueryHelper("order_", "o");
		
		if(date!=null && !"".equals(date))
			helper.addWhereCondition("? <= o.date and o.date <= ?", DateUtils.getFirstDateOfMonth(date),DateUtils.getEndDateOfMonth(date));
		
		PageBean pageBean = orderService.queryOrder(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "monthList";
	}
	
	/** 查询*/
	public String query() throws Exception{
		//System.out.println(date);
		QueryHelper helper = new QueryHelper("order_", "o");
		
		/*if(plateNumber!=null && !"".equals(plateNumber))
			helper.addWhereCondition("cc.car.plateNumber = ?", plateNumber );
		
		if(driverName!=null && !"".equals(driverName))
			helper.addWhereCondition("cc.car.driver.name like ?", "%" + driverName + "%");
		
		if(date1!=null && date2!=null)
			//helper.addWhereCondition("? <= cc.date and cc.date <= ?", date1, date2);
			helper.addWhereCondition("(TO_DAYS(cc.date)-TO_DAYS(?))>=0 and (TO_DAYS(?)-TO_DAYS(cc.date))>=0", date1 ,date2);*/
		
		
		PageBean pageBean = orderService.queryOrder(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "query";
	}
	
	

}
