package com.yuqincar.domain.order;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.OneToMany;

import com.yuqincar.domain.car.CarServiceType;
import com.yuqincar.domain.common.BaseEntity;

@Entity
public class PriceTable extends BaseEntity {
	private String title;
	
	@OneToMany
    @JoinTable(name="pricetable_price",
               joinColumns=@JoinColumn(name="pricetable"),
               inverseJoinColumns=@JoinColumn(name="price"))
    @MapKeyJoinColumn(name="carservicetype")
	private Map<CarServiceType,Price> carServiceType;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Map<CarServiceType, Price> getCarServiceType() {
		return carServiceType;
	}

	public void setCarServiceType(Map<CarServiceType, Price> carServiceType) {
		this.carServiceType = carServiceType;
	}	
}
