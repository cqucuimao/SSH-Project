package com.yuqincar.domain.order;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.utils.Text;

/*
 * 客户单位
 */
@Entity
public class CustomerOrganization extends BaseEntity {

	@Text("单位名称")
	@Column(nullable=false, unique=true)
	private String name;	//名称

	@Text("单位简称")
	@Column(unique=true)
	private String abbreviation;	//简称

	@Text("客户")
	@OneToMany(mappedBy="customerOrganization")
	private List<Customer> customers;
	
	@Text("客户单位管理员")
	@OneToOne(fetch=FetchType.LAZY)
	private Customer manager;
	
	@Text("价格表")
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	private PriceTable priceTable;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public List<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}

	public Customer getManager() {
		return manager;
	}

	public void setManager(Customer manager) {
		this.manager = manager;
	}

	public PriceTable getPriceTable() {
		return priceTable;
	}

	public void setPriceTable(PriceTable priceTable) {
		this.priceTable = priceTable;
	}
}
