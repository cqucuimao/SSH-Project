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
import com.yuqincar.domain.order.ReserveCarApplyOrder;
import com.yuqincar.domain.order.ReserveCarApplyOrderStatusEnum;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.businessParameter.BusinessParameterService;
import com.yuqincar.service.monitor.WarningMessageService;
import com.yuqincar.service.order.OrderService;
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
		List<User> approveUserList = businessParameterService.getBusinessParameter().getReserveCarApplyOrderApproveUser();
		List<User> applyUserList = businessParameterService.getBusinessParameter().getReserveCarApplyOrderApplyUser();
		List<User> carApproveUserList = businessParameterService.getBusinessParameter().getReserveCarApplyOrderCarApproveUser();
		List<User> driverApproveUserList = businessParameterService.getBusinessParameter().getReserveCarApplyOrderDriverApproveUser();
		
		if( approveUserList.contains(user) || applyUserList.contains(user) || carApproveUserList.contains(user) || driverApproveUserList.contains(user)){
			return true;
		}
		return false;
	}
	
	public boolean isCanShowApply(){
		User user = ((User)ActionContext.getContext().getSession().get("user"));
		List<User> applyUserList = businessParameterService.getBusinessParameter().getReserveCarApplyOrderApplyUser();
		
		if(applyUserList.contains(user)){
			return true;
		}
		return false;
	}
	
	public boolean isCanShowApprove(){
		User user = ((User)ActionContext.getContext().getSession().get("user"));
		List<User> approveUserList = businessParameterService.getBusinessParameter().getReserveCarApplyOrderApproveUser();
		
		if(approveUserList.contains(user)){
			return true;
		}
		return false;
	}
	
	public boolean isCanShowCarApprove(){
		User user = ((User)ActionContext.getContext().getSession().get("user"));
		List<User> carApproveUserList = businessParameterService.getBusinessParameter().getReserveCarApplyOrderCarApproveUser();
		if(carApproveUserList.contains(user)){
			return true;
		}
		return false;
	}
	
	public boolean isCanShowDriverApprove(){
		User user = ((User)ActionContext.getContext().getSession().get("user"));
		List<User> driverApproveUserList = businessParameterService.getBusinessParameter().getReserveCarApplyOrderDriverApproveUser();
		
		if(driverApproveUserList.contains(user)){
			return true;
		}
		return false;
	}
	
	/**
	 * 获取提醒数量
	 */
	public int getApproveCount(){
		
		return reserveCarApplyOrderService.getReserveCarApplyOrderByStatus(ReserveCarApplyOrderStatusEnum.SUBMITTED).size();
	}
	
	public int getRejectedCount(){
		
		return reserveCarApplyOrderService.getReserveCarApplyOrderByStatus(ReserveCarApplyOrderStatusEnum.REJECTED).size();
	}
	
	public int getConfigureCarCount(){
		int count = 0;
		List<ReserveCarApplyOrder> rcaos = reserveCarApplyOrderService.getReserveCarApplyOrderByStatus(ReserveCarApplyOrderStatusEnum.APPROVED);
		for(ReserveCarApplyOrder rcao:rcaos){
			if(rcao.getCarApproveUser() == null){
				count++;
			}
		}
		return count;
	}
	
	public int getConfigureDriverCount(){
		int count = 0;
		List<ReserveCarApplyOrder> rcaos = reserveCarApplyOrderService.getReserveCarApplyOrderByStatus(ReserveCarApplyOrderStatusEnum.APPROVED);
		for(ReserveCarApplyOrder rcao:rcaos){
			if(rcao.getDriverApproveUser() == null){
				count++;
			}
		}
		return count;
	}
}
