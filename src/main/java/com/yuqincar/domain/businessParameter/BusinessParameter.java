package com.yuqincar.domain.businessParameter;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.utils.Text;

@Entity
public class BusinessParameter extends BaseEntity{
	@Text("短息通知4S店员工")
	@OneToMany
    @JoinTable(name="businessparameter_employeesIn4SForSMS_user",
               joinColumns=@JoinColumn(name="businessparameter"),
               inverseJoinColumns=@JoinColumn(name="user"))
	private List<User> employeesIn4SForSMS;
	
	@Text("短息通知车辆保养管理员")
	@OneToMany
    @JoinTable(name="businessparameter_employeesForCarCareAppointmentSMS_user",
               joinColumns=@JoinColumn(name="businessparameter"),
               inverseJoinColumns=@JoinColumn(name="user"))
	private List<User> employeesForCarCareAppointmentSMS;

	@Text("保养通知提前里程量")
	private int mileageForCarCareRemind;

	public List<User> getEmployeesIn4SForSMS() {
		return employeesIn4SForSMS;
	}

	public List<User> getEmployeesForCarCareAppointmentSMS() {
		return employeesForCarCareAppointmentSMS;
	}

	public void setEmployeesForCarCareAppointmentSMS(List<User> employeesForCarCareAppointmentSMS) {
		this.employeesForCarCareAppointmentSMS = employeesForCarCareAppointmentSMS;
	}

	public void setEmployeesIn4SForSMS(List<User> employeesIn4SForSMS) {
		this.employeesIn4SForSMS = employeesIn4SForSMS;
	}

	public int getMileageForCarCareRemind() {
		return mileageForCarCareRemind;
	}

	public void setMileageForCarCareRemind(int mileageForCarCareRemind) {
		this.mileageForCarCareRemind = mileageForCarCareRemind;
	}
	
	
}
