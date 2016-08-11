package com.yuqincar.domain.car;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.utils.Text;

@Entity
public class DriverLicense extends BaseEntity {

	@Text("驾照编号")
	@Column(nullable = false, unique=true)
	private String licenseID;

	@Text("过期日期")
	@Column(nullable = false)
	private Date expireDate;

	public String getLicenseID() {
		return licenseID;
	}

	public void setLicenseID(String licenseID) {
		this.licenseID = licenseID;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	
	
}
