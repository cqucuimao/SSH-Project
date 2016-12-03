package com.yuqincar.domain.order;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.utils.Text;

@Entity
public class ReserveCarApplyOrder extends BaseEntity {
	@Text("申请原因")
	private String reason;
	
	@Text("申请人")
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable = false)
	private User proposer;
	
	@Text("申请车辆数量")
	private int carCount;
	
	@Text("使用起始日期")
	private Date fromDate;
	
	@Text("使用结束日期")
	private Date toDate;
	
	@Text("新建时间")
	private Date newTime;
	
	@Text("提交时间")
	private Date submittedTime;
	
	@Text("审核时间")
	private Date approvedTime;
	
	@Text("配置完成时间")
	private Date configuredTime;
	
	@Text("审核是否通过")
	private boolean approved;
	
	@Text("审核人")
	@OneToOne(fetch=FetchType.LAZY)
	private User approveUser;
	
	@Text("审核意见")
	private String approveMemo;
	
	@Text("申请状态")
	@Column(nullable = false)
	private ReserveCarApplyOrderStatusEnum status;
	
	@Text("车辆配置人")
	@OneToOne(fetch=FetchType.LAZY)
	private User carApproveUser;
	
	@Text("车辆配置人意见")
	private String carApproveUserMemo;
	
	@Text("车辆配置是否完成")
	private boolean carApproved;
	
	@Text("配置车辆")
	@OneToMany
	@JoinTable(name = "reserveApply_car", joinColumns = { @JoinColumn(name = "apply_id")},
		inverseJoinColumns = { @JoinColumn(name = "car_id") })
	private Set<Car> cars;
	
	@Text("司机配置人")
	@OneToOne(fetch=FetchType.LAZY)
	private User driverApproveUser;
	
	@Text("司机配置意见")
	private String driverApproveUserMemo;
	
	@Text("司机配置是否完成")
	private boolean driverApproved;
	
	@Text("配置司机")
	@OneToMany
	@JoinTable(name = "reserveApply_driver", joinColumns = { @JoinColumn(name = "apply_id")},
		inverseJoinColumns = { @JoinColumn(name = "driver_id") })
	private Set<User> drivers;
	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public User getProposer() {
		return proposer;
	}
	public void setProposer(User proposer) {
		this.proposer = proposer;
	}
	public int getCarCount() {
		return carCount;
	}
	public void setCarCount(int carCount) {
		this.carCount = carCount;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public Date getNewTime() {
		return newTime;
	}
	public void setNewTime(Date newTime) {
		this.newTime = newTime;
	}
	public Date getSubmittedTime() {
		return submittedTime;
	}
	public void setSubmittedTime(Date submittedTime) {
		this.submittedTime = submittedTime;
	}
	public Date getApprovedTime() {
		return approvedTime;
	}
	public void setApprovedTime(Date approvedTime) {
		this.approvedTime = approvedTime;
	}
	public ReserveCarApplyOrderStatusEnum getStatus() {
		return status;
	}
	public void setStatus(ReserveCarApplyOrderStatusEnum status) {
		this.status = status;
	}
	public boolean isApproved() {
		return approved;
	}
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	public String getApproveMemo() {
		return approveMemo;
	}
	public void setApproveMemo(String approveMemo) {
		this.approveMemo = approveMemo;
	}
	public User getCarApproveUser() {
		return carApproveUser;
	}
	public void setCarApproveUser(User carApproveUser) {
		this.carApproveUser = carApproveUser;
	}
	public String getCarApproveUserMemo() {
		return carApproveUserMemo;
	}
	public void setCarApproveUserMemo(String carApproveUserMemo) {
		this.carApproveUserMemo = carApproveUserMemo;
	}
	public boolean isCarApproved() {
		return carApproved;
	}
	public void setCarApproved(boolean carApproved) {
		this.carApproved = carApproved;
	}
	public Set<Car> getCars() {
		return cars;
	}
	public void setCars(Set<Car> cars) {
		this.cars = cars;
	}
	public User getDriverApproveUser() {
		return driverApproveUser;
	}
	public void setDriverApproveUser(User driverApproveUser) {
		this.driverApproveUser = driverApproveUser;
	}
	public String getDriverApproveUserMemo() {
		return driverApproveUserMemo;
	}
	public void setDriverApproveUserMemo(String driverApproveUserMemo) {
		this.driverApproveUserMemo = driverApproveUserMemo;
	}
	public boolean isDriverApproved() {
		return driverApproved;
	}
	public void setDriverApproved(boolean driverApproved) {
		this.driverApproved = driverApproved;
	}
	public Set<User> getDrivers() {
		return drivers;
	}
	public void setDrivers(Set<User> drivers) {
		this.drivers = drivers;
	}
	public Date getConfiguredTime() {
		return configuredTime;
	}
	public void setConfiguredTime(Date configuredTime) {
		this.configuredTime = configuredTime;
	}
	public User getApproveUser() {
		return approveUser;
	}
	public void setApproveUser(User approveUser) {
		this.approveUser = approveUser;
	}
	
}
