package com.yuqincar.domain.order;


import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.privilege.UserAPPDeviceTypeEnum;
import com.yuqincar.utils.Text;

/*
 * 客户
 */
@Entity
public class Customer extends BaseEntity {

	@Text("所属单位")
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	private CustomerOrganization customerOrganization;	//所属单位

	@Text("客户姓名")
	@Column(nullable=false)
	private String name;	//姓名

	@Text("联系电话")
	@ElementCollection
	@CollectionTable
	@Column(name="phone_number")
	@OrderColumn(name="list_order")
	private List<String> phones;	//联系电话

	@Text("常用地址")	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name = "customer_address", joinColumns = { @JoinColumn(name = "customer_id")},
		inverseJoinColumns = { @JoinColumn(name = "address_id") })
	private List<Address> addresses;	//常用地址
	
	@Text("常用乘车人")
	@OneToMany(mappedBy="customer")
	private List<OtherPassenger> otherPassengers;
	
	@Text("性别")
	private boolean gender; //true表示男性，false表示女性
	
	@Text("APP设备类型")
	private UserAPPDeviceTypeEnum appDeviceType;
	@Text("APP标识Token")
	private String appDeviceToken;

	public CustomerOrganization getCustomerOrganization() {
		return customerOrganization;
	}

	public void setCustomerOrganization(CustomerOrganization customerOrganization) {
		this.customerOrganization = customerOrganization;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getPhones() {
		return phones;
	}

	public void setPhones(List<String> phones) {
		this.phones = phones;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public List<OtherPassenger> getOtherPassengers() {
		return otherPassengers;
	}

	public void setOtherPassengers(List<OtherPassenger> otherPassengers) {
		this.otherPassengers = otherPassengers;
	}

	public boolean isGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}

	public UserAPPDeviceTypeEnum getAppDeviceType() {
		return appDeviceType;
	}

	public void setAppDeviceType(UserAPPDeviceTypeEnum appDeviceType) {
		this.appDeviceType = appDeviceType;
	}

	public String getAppDeviceToken() {
		return appDeviceToken;
	}

	public void setAppDeviceToken(String appDeviceToken) {
		this.appDeviceToken = appDeviceToken;
	}
	
	
}
