package com.yuqincar.action.receipt;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarStatusEnum;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.CustomerOrganization;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.ProtocolOrderPayOrder;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.service.order.ProtocolOrderPayOrderService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class PopoAddStatementAction extends BaseAction implements ModelDriven<ProtocolOrderPayOrder> {
	
	private ProtocolOrderPayOrder model=new ProtocolOrderPayOrder();
	
	@Autowired
	ProtocolOrderPayOrderService protocolOrderPayOrderService;
	
	private CustomerOrganization popoAddStatementCustomerOrganization;
	
	private Date popoAddStatementFromDate;
	
	private Date popoAddStatementToDate;
	
	public String list(){
		QueryHelper helper = new QueryHelper(ProtocolOrderPayOrder.class, "popo");
		helper.addWhereCondition("popo.orderStatement is null");
		PageBean<ProtocolOrderPayOrder> pageBean = protocolOrderPayOrderService.queryProtocolOrderPayOrder(helper, pageNum);		
		ActionContext.getContext().getValueStack().push(pageBean);	
		ActionContext.getContext().getSession().put("protocolOrderPayOrderHelper", helper);
		return "list";
	}
	
	public String freshList(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("protocolOrderPayOrderHelper");
		helper.addWhereCondition("popo.orderStatement is null");
		PageBean pageBean = protocolOrderPayOrderService.queryProtocolOrderPayOrder(helper, pageNum);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
	
	public String queryList(){
		QueryHelper helper = new QueryHelper(ProtocolOrderPayOrder.class, "popo");
		helper.addWhereCondition("popo.orderStatement is null");
		
		if(popoAddStatementCustomerOrganization!=null)
			helper.addWhereCondition("popo.order.customerOrganization=?", popoAddStatementCustomerOrganization);
		if(popoAddStatementFromDate!=null && popoAddStatementToDate==null)
		{
			helper.addWhereCondition("popo.toDate>=?",DateUtils.getMinDate(popoAddStatementFromDate));
		}
		if(popoAddStatementToDate!=null && popoAddStatementFromDate==null )
			helper.addWhereCondition("popo.fromDate<=?", DateUtils.getMaxDate(popoAddStatementToDate));
		if(popoAddStatementToDate!=null && popoAddStatementFromDate!=null )
			helper.addWhereCondition("popo.toDate between ? and ? or popo.fromDate between ? and ?",
					DateUtils.getMinDate(popoAddStatementFromDate), DateUtils.getMaxDate(popoAddStatementToDate),
					DateUtils.getMinDate(popoAddStatementFromDate),DateUtils.getMaxDate(popoAddStatementToDate));
		System.out.println("Sql=="+helper.getQueryListHql());
		PageBean<ProtocolOrderPayOrder> pageBean = protocolOrderPayOrderService.queryProtocolOrderPayOrder(helper, pageNum);		
		ActionContext.getContext().getValueStack().push(pageBean);	
		ActionContext.getContext().getSession().put("protocolOrderPayOrderHelper", helper);
		return "list";
	}
	
	public String home(){
		return "home";
	}

	public ProtocolOrderPayOrder getModel() {
		return model;
	}

	public CustomerOrganization getPopoAddStatementCustomerOrganization() {
		return popoAddStatementCustomerOrganization;
	}

	public void setPopoAddStatementCustomerOrganization(CustomerOrganization popoAddStatementCustomerOrganization) {
		this.popoAddStatementCustomerOrganization = popoAddStatementCustomerOrganization;
	}


	public Date getPopoAddStatementFromDate() {
		return popoAddStatementFromDate;
	}

	public void setPopoAddStatementFromDate(Date popoAddStatementFromDate) {
		this.popoAddStatementFromDate = popoAddStatementFromDate;
	}

	public Date getPopoAddStatementToDate() {
		return popoAddStatementToDate;
	}

	public void setPopoAddStatementToDate(Date popoAddStatementToDate) {
		this.popoAddStatementToDate = popoAddStatementToDate;
	}

	
	
}
