package com.yuqincar.action.receipt;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarRefuel;
import com.yuqincar.domain.car.CarStatusEnum;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.Customer;
import com.yuqincar.domain.order.CustomerOrganization;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.OrderStatusEnum;
import com.yuqincar.domain.order.ProtocolOrderPayOrder;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.service.order.ProtocolOrderPayOrderService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class ProtocolOrderPayOrderAction extends BaseAction implements ModelDriven<ProtocolOrderPayOrder> {
	
	private ProtocolOrderPayOrder model=new ProtocolOrderPayOrder();
	
	private String protocolOrderSn;
	
	//查询参数
	private CustomerOrganization protocolOrderCustomerOrganization;
	private String protocolOrderPaid;
	private String actionFlag;
	
	//新增和修改参数
	private String protocolOrderPayOrderSn;
	private Date fromDate;
	private Date toDate;
	private BigDecimal money;
	
	/*//修改时回显参数
	private String oldPopoSn;
	private Date oldFromDate;
	private Date oldToDate;
	private BigDecimal oldMoney;*/
	

	private long orderId;
	
	@Autowired
	private ProtocolOrderPayOrderService protocolOrderPayOrderService;
	@Autowired
	private OrderService orderService;
	
	public String list(){
		QueryHelper helper = new QueryHelper(ProtocolOrderPayOrder.class, "popo");
		helper.addOrderByProperty("popo.id", false);
		PageBean pageBean = protocolOrderPayOrderService.queryProtocolOrderPayOrder(helper, pageNum);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("protocolOrderPayOrderHelper", helper);
		return "list";
	}
	
	public String freshList(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("protocolOrderPayOrderHelper");
		PageBean pageBean = protocolOrderPayOrderService.queryProtocolOrderPayOrder(helper, pageNum);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
	
	//private ProtocolOrderPayOrder popo;
	
	//private boolean paid=model.isPaid();
	public String MangerQueryForm(){
		//System.out.println("execute in this!");
		QueryHelper helper = new QueryHelper(ProtocolOrderPayOrder.class, "po");
		if(protocolOrderSn!=null)
			helper.addWhereCondition("po.order.sn like ?", "%"+protocolOrderSn+"%");
		if(protocolOrderCustomerOrganization!=null)
			helper.addWhereCondition("po.order.customerOrganization=?",protocolOrderCustomerOrganization);
		//System.out.println(protocolOrderPaid);
		if(protocolOrderPaid.equals("是"))
			helper.addWhereCondition("po.paid=?",true);
		else if(protocolOrderPaid.equals("否"))
			helper.addWhereCondition("po.paid=?",false);
		PageBean pageBean = protocolOrderPayOrderService.queryProtocolOrderPayOrder(helper, pageNum);		
		ActionContext.getContext().getValueStack().push(pageBean);	
		ActionContext.getContext().getSession().put("protocolOrderPayOrderHelper", helper);
		return "list";
	}
	
	/** 添加页面 */
	public String addUI() throws Exception {
		return "saveUI";
	}
	
	/** 添加*/
	public String add() throws Exception {
		if(orderService.getOrderBySN(protocolOrderPayOrderSn)==null)
		{
			addFieldError("protocolOrderPayOrderSn", "你填写的订单不存在！");
			return addUI();
		}	
		if(protocolOrderPayOrderService.canAddProtocolOrderPayOrder(orderService.getOrderBySN(protocolOrderPayOrderSn))==false)
		{
			addFieldError("protocolOrderPayOrderSn", "你填写的协议订单不存在或者状态不正确！");
			return addUI();
		}
		model.setOrder(orderService.getOrderBySN(protocolOrderPayOrderSn));
		protocolOrderPayOrderService.saveProtocolOrderPayOrder(model);
		
		ActionContext.getContext().getValueStack().push(new ProtocolOrderPayOrder());
		return freshList();
	}
	
	/** 从订单详情中添加页面 */
	public String addFromAddUI() throws Exception {
		Order order_=orderService.getOrderById(orderId);
		protocolOrderPayOrderSn=order_.getSn();
		return "saveUI";
	}
	
	/** 修改页面 */
	public String editUI() throws Exception {
		ActionContext.getContext().put("actionFlag", actionFlag);
		// 准备回显的数据
		ProtocolOrderPayOrder popo = protocolOrderPayOrderService.getProtocolOrderPayOrderById(model.getId());
		ActionContext.getContext().getValueStack().push(popo);
		//订单号
		if (popo.getOrder()!= null) {
			protocolOrderPayOrderSn= popo.getOrder().getSn();
		}
		//处理起止时间
		if (popo.getFromDate() != null) {
			fromDate = popo.getFromDate();
		}
		if (popo.getToDate() != null) {
			toDate = popo.getToDate();
		}
		
		//处理金额
		if (popo.getMoney() != null) {
			money = popo.getMoney();
		}
	    
		return "saveUI";
	}
	
	/** 修改*/
	public String edit() throws Exception {
		//从数据库中取出原对象
		ProtocolOrderPayOrder popo = protocolOrderPayOrderService.getProtocolOrderPayOrderById(model.getId());
		
		popo.setOrder(orderService.getOrderBySN(protocolOrderPayOrderSn));
		popo.setFromDate(model.getFromDate());
		popo.setToDate(model.getToDate());
		popo.setMoney(model.getMoney());
		
		//更新到数据库
		protocolOrderPayOrderService.updateProtocolOrderPayOrder(popo);
		ActionContext.getContext().getValueStack().push(new ProtocolOrderPayOrder());
		return freshList();
	}
	
	/** 删除*/
	public String delete() throws Exception {
		protocolOrderPayOrderService.deleteProtocolOrderPayOrder(model.getId());
		return freshList();
	}
	
	public String home(){
		return "home";
	}

	public ProtocolOrderPayOrder getModel() {
		return model;
	}

	public String getProtocolOrderSn() {
		return protocolOrderSn;
	}

	public void setProtocolOrderSn(String protocolOrderSn) {
		this.protocolOrderSn = protocolOrderSn;
	}

	public CustomerOrganization getProtocolOrderCustomerOrganization() {
		return protocolOrderCustomerOrganization;
	}

	public void setProtocolOrderCustomerOrganization(CustomerOrganization protocolOrderCustomerOrganization) {
		this.protocolOrderCustomerOrganization = protocolOrderCustomerOrganization;
	}

	public String getProtocolOrderPaid() {
		return protocolOrderPaid;
	}

	public void setProtocolOrderPaid(String protocolOrderPaid) {
		this.protocolOrderPaid = protocolOrderPaid;
	}

	public String getProtocolOrderPayOrderSn() {
		return protocolOrderPayOrderSn;
	}

	public void setProtocolOrderPayOrderSn(String protocolOrderPayOrderSn) {
		this.protocolOrderPayOrderSn = protocolOrderPayOrderSn;
	}
	
	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	
	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

/*	public Date getOldFromDate() {
		return oldFromDate;
	}

	public void setOldFromDate(Date oldFromDate) {
		this.oldFromDate = oldFromDate;
	}

	public BigDecimal getOldMoney() {
		return oldMoney;
	}

	public void setOldMoney(BigDecimal oldMoney) {
		this.oldMoney = oldMoney;
	}

	public String getOldPopoSn() {
		return oldPopoSn;
	}

	public void setOldPopoSn(String oldPopoSn) {
		this.oldPopoSn = oldPopoSn;
	}

	public Date getOldToDate() {
		return oldToDate;
	}

	public void setOldToDate(Date oldToDate) {
		this.oldToDate = oldToDate;
	}
*/
	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	
}
