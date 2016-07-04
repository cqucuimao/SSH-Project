package com.yuqincar.domain.car;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.order.Order;
import com.yuqincar.utils.Text;
/*
 * 商业保险
 */
@Entity
public class CommercialInsurance extends BaseEntity {
	@Text("所属保单")
	@ManyToOne(fetch=FetchType.LAZY )
	private CarInsurance insurance;
	
	@Text("类型")
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	private CommercialInsuranceType type;
	
	@Text("金额")
	@Column(nullable=false)
	private BigDecimal money;
	
	@Text("承保金额")
	@Column(nullable=false)
	private BigDecimal coverageMoney;
	
	@Text("生效日期")
	@Column(nullable=false)
	private Date beginDate;
	
	@Text("截至日期")
	@Column(nullable=false)
	private Date endDate;

	public CommercialInsuranceType getType() {
		return type;
	}

	public void setType(CommercialInsuranceType type) {
		this.type = type;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getCoverageMoney() {
		return coverageMoney;
	}

	public void setCoverageMoney(BigDecimal coverageMoney) {
		this.coverageMoney = coverageMoney;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
