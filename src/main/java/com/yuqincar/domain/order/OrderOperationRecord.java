package com.yuqincar.domain.order;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.utils.Text;

/*
 * 订单操作记录
 */
@Entity
public class OrderOperationRecord extends BaseEntity {

	@Text("订单")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	private Order order;	//订单

	@Text("操作类型")
	@Column(nullable=false)
	private OrderOperationTypeEnum type;	//类型

	@Text("操作日期")
	@Column(nullable=false)
	private Date date;	//操作日期

	@Text("操作人")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	private User user;	//操作人

	@Text("描述")
	@Column(nullable=false)
	private String description;	//描述。需要把做了的任何修改记录下来
	
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public OrderOperationTypeEnum getType() {
		return type;
	}
	public void setType(OrderOperationTypeEnum type) {
		this.type = type;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
