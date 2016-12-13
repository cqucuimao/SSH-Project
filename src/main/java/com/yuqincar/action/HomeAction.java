package com.yuqincar.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.OrderStatusEnum;
import com.yuqincar.domain.order.ProtocolOrderPayOrder;
import com.yuqincar.domain.order.ReserveCarApplyOrder;
import com.yuqincar.domain.order.ReserveCarApplyOrderStatusEnum;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.businessParameter.BusinessParameterService;
import com.yuqincar.service.monitor.WarningMessageService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.service.order.ProtocolOrderPayOrderService;
import com.yuqincar.service.order.ReserveCarApplyOrderService;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class HomeAction extends ActionSupport {
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private BusinessParameterService businessParameterService;
	
	@Autowired
	private WarningMessageService warningMessageService;
	
	@Autowired
	private ReserveCarApplyOrderService reserveCarApplyOrderService;
	
	@Autowired
	private ProtocolOrderPayOrderService protocolOrderPayOrderService;
	
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
	/**
	 * 扩充常备车库的申请提醒
	 */
	public boolean isCanShowReserveCarApplyOrderBlock(){
		User user = ((User)ActionContext.getContext().getSession().get("user"));
		if(reserveCarApplyOrderService.getRejects(user).size()==0
				&& reserveCarApplyOrderService.getNeedApprove(user).size()==0
				&& reserveCarApplyOrderService.getNeedConfigureCar(user).size()==0
				&& reserveCarApplyOrderService.getNeedConfigureDriver(user).size()==0)
			return false;
		else
			return true;
	}
	
	public boolean isCanShowRejected(){
		User user = ((User)ActionContext.getContext().getSession().get("user"));
		return reserveCarApplyOrderService.getRejects(user).size()>0;
	}
	
	public boolean isCanShowApprove(){
		User user = ((User)ActionContext.getContext().getSession().get("user"));
		return reserveCarApplyOrderService.getNeedApprove(user).size()>0;
	}
	
	public boolean isCanShowCarApprove(){
		User user = ((User)ActionContext.getContext().getSession().get("user"));
		return reserveCarApplyOrderService.getNeedConfigureCar(user).size()>0;
	}
	
	public boolean isCanShowDriverApprove(){
		User user = ((User)ActionContext.getContext().getSession().get("user"));
		return reserveCarApplyOrderService.getNeedConfigureDriver(user).size()>0;
	}
	
	/**
	 * 获取提醒数量
	 */
	public int getApproveCount(){
		User user = ((User)ActionContext.getContext().getSession().get("user"));	
		return reserveCarApplyOrderService.getNeedApprove(user).size();
	}
	
	public int getRejectedCount(){	
		User user = ((User)ActionContext.getContext().getSession().get("user"));	
		return reserveCarApplyOrderService.getRejects(user).size();
	}
	
	public int getConfigureCarCount(){
		User user = ((User)ActionContext.getContext().getSession().get("user"));	
		return reserveCarApplyOrderService.getNeedConfigureCar(user).size();
	}
	
	public int getConfigureDriverCount(){
		User user = ((User)ActionContext.getContext().getSession().get("user"));	
		return reserveCarApplyOrderService.getNeedConfigureDriver(user).size();
	}
	
	//协议订单收款单提醒事项
	public boolean isCanShowUnpaidPopo(){
		System.out.println("4444");
		return ((User)ActionContext.getContext().getSession().get("user")).hasPrivilegeByUrl("/protocolOrderPayOrder_list");
	}
	
	public int getUnpaidPopoCount(){
		System.out.println("5555");
		int count = 0;
		List<ProtocolOrderPayOrder> popos = protocolOrderPayOrderService.getAllPopos();
		for(ProtocolOrderPayOrder popo:popos){
			if(popo.getOrderStatement() == null){
				count++;
			}
		}
		return count;
	}
}
