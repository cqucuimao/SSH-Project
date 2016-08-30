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
	private CommercialInsuranceType commercialInsuranceType;
	
	@Text("金额")
	@Column(nullable=false)
	private BigDecimal commercialInsuranceMoney;
	
	@Text("承保金额")
	@Column(nullable=false)
	private BigDecimal commercialInsuranceCoverageMoney;
	
	@Text("生效日期")
	@Column(nullable=false)
	private Date commercialInsuranceBeginDate;
	
	@Text("截至日期")
	@Column(nullable=false)
	private Date commercialInsuranceEndDate;
	
	@Text("备注")
	private String commercialInsuranceMemo;
	
	public CarInsurance getInsurance() {
		return insurance;
	}

	public void setInsurance(CarInsurance insurance) {
		this.insurance = insurance;
	}

	public CommercialInsuranceType getCommercialInsuranceType() {
		return commercialInsuranceType;
	}

	public void setCommercialInsuranceType(CommercialInsuranceType commercialInsuranceType) {
		this.commercialInsuranceType = commercialInsuranceType;
	}

	public BigDecimal getCommercialInsuranceMoney() {
		return commercialInsuranceMoney;
	}

	public void setCommercialInsuranceMoney(BigDecimal commercialInsuranceMoney) {
		this.commercialInsuranceMoney = commercialInsuranceMoney;
	}

	public BigDecimal getCommercialInsuranceCoverageMoney() {
		return commercialInsuranceCoverageMoney;
	}

	public void setCommercialInsuranceCoverageMoney(BigDecimal commercialInsuranceCoverageMoney) {
		this.commercialInsuranceCoverageMoney = commercialInsuranceCoverageMoney;
	}

	public Date getCommercialInsuranceBeginDate() {
		return commercialInsuranceBeginDate;
	}

	public void setCommercialInsuranceBeginDate(Date commercialInsuranceBeginDate) {
		this.commercialInsuranceBeginDate = commercialInsuranceBeginDate;
	}

	public Date getCommercialInsuranceEndDate() {
		return commercialInsuranceEndDate;
	}

	public void setCommercialInsuranceEndDate(Date commercialInsuranceEndDate) {
		this.commercialInsuranceEndDate = commercialInsuranceEndDate;
	}

	public String getCommercialInsuranceMemo() {
		return commercialInsuranceMemo;
	}

	public void setCommercialInsuranceMemo(String commercialInsuranceMemo) {
		this.commercialInsuranceMemo = commercialInsuranceMemo;
	}

	
}
