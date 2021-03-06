package com.yuqincar.domain.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.utils.Text;
/**
 * 对账单
 */
@Entity
public class OrderStatement extends BaseEntity {
	@Text("对账单名称")
	@Column(nullable=false)
	private String name;//名称
	
	@Text("生成日期")
	@Column(nullable=false)
	private Date date;//生成日期
	
	@Text("客户单位")
	@OneToOne
	private CustomerOrganization customerOrganization;//客户单位
	
	@Text("开始日期")
	@Column(nullable=false)
	private Date fromDate;//所包含订单中最早的日期
	
	@Text("结束日期")
	@Column(nullable=false)
	private Date toDate;//所包含订单中最晚的日期
	
	@Text("订单数量")
	@Column(nullable=false)
	private int orderNum;//所包含的订单数量
	
	@Text("总金额")
	@Column(nullable=false)
	private BigDecimal totalMoney;//所包含的订单总金额
	
	@Text("发票号码")
	private String invoiceNumber;
	
	@Text("发票金额")
	private BigDecimal invoiceMoney;
	
	@Text("开票日期")
	private Date invoiceDate;
	
	@Text("实收金额")
	private BigDecimal actualTotalMoney;
	
	@Text("订单")
	@OneToMany(mappedBy="orderStatement")
	private List<Order> orders = new ArrayList<Order>();
	
	@Text("协议订单收款单")
	@OneToMany(mappedBy="orderStatement")
	private List<ProtocolOrderPayOrder> popos = new ArrayList<ProtocolOrderPayOrder>();
	
	@Text("对账单状态")
	@Column(nullable=false)
	private OrderStatementStatusEnum status;
	
	@Text("到款情况")
	@OneToMany(mappedBy="orderStatement")
	private List<MoneyGatherInfo> gatherList;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public CustomerOrganization getCustomerOrganization() {
		return customerOrganization;
	}
	public void setCustomerOrganization(CustomerOrganization customerOrganization) {
		this.customerOrganization = customerOrganization;
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
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public BigDecimal getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(BigDecimal totalMoney) {
		this.totalMoney = totalMoney;
	}
	public List<Order> getOrders() {
		return orders;
	}
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}
	public OrderStatementStatusEnum getStatus() {
		return status;
	}
	public void setStatus(OrderStatementStatusEnum status) {
		this.status = status;
	}
	public BigDecimal getActualTotalMoney() {
		return actualTotalMoney;
	}
	public void setActualTotalMoney(BigDecimal actualTotalMoney) {
		this.actualTotalMoney = actualTotalMoney;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public BigDecimal getInvoiceMoney() {
		return invoiceMoney;
	}
	public void setInvoiceMoney(BigDecimal invoiceMoney) {
		this.invoiceMoney = invoiceMoney;
	}
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public List<MoneyGatherInfo> getGatherList() {
		return gatherList;
	}
	public void setGatherList(List<MoneyGatherInfo> gatherList) {
		this.gatherList = gatherList;
	}
	public List<ProtocolOrderPayOrder> getPopos() {
		return popos;
	}
	public void setPopos(List<ProtocolOrderPayOrder> popos) {
		this.popos = popos;
	}	
}
