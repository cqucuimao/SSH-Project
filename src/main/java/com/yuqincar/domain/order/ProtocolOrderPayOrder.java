package com.yuqincar.domain.order;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.utils.Text;

@Entity
public class ProtocolOrderPayOrder extends BaseEntity {
	@Text("订单")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	private Order order;
	
	@Text("计费起始日期")
	private Date fromDate;

	@Text("计费结束日期")
	private Date toDate;
	
	@Text("金额")
	private BigDecimal money;
	
	@Text("所属收款单")
	@OneToOne(fetch=FetchType.LAZY)
	private OrderStatement orderStatement;
	
	@Text("是否收款")
	private boolean paid;

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
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

	public OrderStatement getOrderStatement() {
		return orderStatement;
	}

	public void setOrderStatement(OrderStatement orderStatement) {
		this.orderStatement = orderStatement;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}	
}
