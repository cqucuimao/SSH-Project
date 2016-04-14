package com.yuqincar.domain.monitor;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.utils.Text;

/*
 * 车载设备
 */
@Entity
public class Device extends BaseEntity {
	@Text("SN")
	@Column(nullable=false)
	private String SN;

	@Text("PN")
	@Column(nullable=false)
	private String PN;
	
	@Text("SIM卡号码")
	private String simNumber;
	
	@Text("安装日期")
	private Date date;	//安装时间
	@Text("厂商")
	private String	manufacturer;	//厂商。设备有可能来自不同的厂商。
	
	public String getSN() {
		return SN;
	}
	public void setSN(String sN) {
		SN = sN;
	}
	public String getPN() {
		return PN;
	}
	public void setPN(String pN) {
		PN = pN;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getSimNumber() {
		return simNumber;
	}
	public void setSimNumber(String simNumber) {
		this.simNumber = simNumber;
	}
}
