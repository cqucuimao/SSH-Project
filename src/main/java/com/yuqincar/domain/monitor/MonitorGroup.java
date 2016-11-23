package com.yuqincar.domain.monitor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.utils.Text;

/**
 * 监控车辆分组
 * @author cocoa
 *
 */
@Entity
public class MonitorGroup extends BaseEntity{
	
	@Text("分组名称")
	private String title;
	
	@Text("所属车辆")
	@ManyToMany
    @JoinTable(name="monitorgroup_car",
               joinColumns=@JoinColumn(name="monitorgroup"),
               inverseJoinColumns=@JoinColumn(name="car"))
	private Set<Car> cars = new HashSet<Car>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<Car> getCars() {
		return cars;
	}

	public void setCars(Set<Car> cars) {
		this.cars = cars;
	}
	
	

}
