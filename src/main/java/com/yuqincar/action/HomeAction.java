package com.yuqincar.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.OrderStatusEnum;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.monitor.WarningMessageService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class HomeAction extends ActionSupport {
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private WarningMessageService warningMessageService;
	
	public String index() {
		return "index";
	}
	
	public String left() throws Exception {
		return "left";
	}
	
	public String right() throws Exception {
		return "right";
	}
	
	public String welcome(){
		return "welcome";
	}
	
	public boolean isCanShowScheduleBlock(){
		return ((User)ActionContext.getContext().getSession().get("user")).hasPrivilegeByUrl("/schedule_queue");
	}
	
	public int getOrderCountInQueue(){
		QueryHelper helper = new QueryHelper("order_", "o");
		helper.addWhereCondition("o.status=?", OrderStatusEnum.INQUEUE);
		helper.addWhereCondition("o.scheduling=?", false);
		PageBean<Order> pageBean = orderService.queryOrder(1, helper);
		return pageBean.getRecordCount();
	}
	
	public boolean isCanShowWarningBlock(){
		return ((User)ActionContext.getContext().getSession().get("user")).hasPrivilegeByUrl("/realtime_home");
	}

	public int getWarningCount(){
		return warningMessageService.getUndealedMessages().size();
	}
}
