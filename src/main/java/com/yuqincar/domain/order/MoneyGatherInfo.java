package com.yuqincar.domain.order;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.utils.Text;
/**
 * 对账单收款信息
 */
@Entity
public class MoneyGatherInfo extends BaseEntity {
	@Text("所属对账单")
	@OneToOne(fetch=FetchType.LAZY)
	private OrderStatement orderStatement;
	
	@Text("金额")
	@Column(nullable=false)
	private BigDecimal money;
	
	@Text("收款日期")
	@Column(nullable=false)
	private Date date;
	
	@Text("备注")
	@Column(nullable=false)
	private String memo;

	public OrderStatement getOrderStatement() {
		return orderStatement;
	}

	public void setOrderStatement(OrderStatement orderStatement) {
		this.orderStatement = orderStatement;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}	
}
