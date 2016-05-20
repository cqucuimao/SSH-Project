package com.yuqincar.domain.order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarRefuel;
import com.yuqincar.domain.car.CarServiceType;
import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.common.DiskFile;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.utils.Text;

/*
 * 订单
 */
@Entity(name = "order_")
public class Order extends BaseEntity {

	@Text("订单号")
	@Column(nullable = false, unique = true)
	private String sn; // 订单号。 形如YYMMXXXXX

	@Text("客户单位")
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable = false)
	private CustomerOrganization customerOrganization; // 客户单位

	@Text("客户")
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Customer customer; // 客户

	@Text("联系电话")
	@Column(nullable = false)
	private String phone; // 联系电话

	@Text("付费方式")
	@Column(nullable = false)
	private ChargeModeEnum chargeMode; // 付费方式

	@Text("计划开始时间")
	@Column
	private Date planBeginDate;

	@Text("计划结束时间")
	private Date planEndDate;
	
	@Text("实际开始时间")
	@Column
	private Date actualBeginDate;

	@Text("实际结束时间")
	private Date actualEndDate;

	@Text("车型")
	@OneToOne(fetch=FetchType.LAZY)
	private CarServiceType serviceType; // 车型

	@Text("出发地")
	private String fromAddress; // 上车地点

	@Text("目的地")
	private String toAddress; // 下车地点
	
	@Text("每天行程细节")
	@OneToMany(mappedBy="order")
	private List<DayOrderDetail> dayDetails;

	@Text("出库路码")
	private float beginMile; 
	
	@Text("客户上车路码")
	private float customerGetonMile;
	
	@Text("客户下车路码")
	private float customerGetoffMile;

	@Text("回库路码")
	private float endMile;

	@Text("计费路码")
	private float totalChargeMile;

	@Text("核算金额")
	private BigDecimal orderMoney;

	@Text("实收金额")
	private BigDecimal actualMoney;

	@Text("进队列时间")
	private Date queueTime; 

	@Text("调度时间")
	private Date scheduleTime;

	@Text("接受时间")
	private Date acceptedTime;

	@Text("执行车辆")
	@OneToOne(fetch=FetchType.LAZY)
	private Car car;

	@Text("执行司机")
	@OneToOne(fetch=FetchType.LAZY)
	private User driver;

	@Text("订单状态")
	@Column(nullable = false)
	private OrderStatusEnum status;

	@Text("备注")
	private String memo; // 备注

	@Text("操作记录")
	@OneToMany(mappedBy = "order", fetch=FetchType.LAZY)
	private List<OrderOperationRecord> operationRecord; // 对订单进行各种操作的记录

	@Text("客户签名")
	@OneToOne(fetch=FetchType.LAZY)
	private DiskFile signature; // 客户签名

	@Text("所属对账单")
	@OneToOne(fetch=FetchType.LAZY)
	private OrderStatement orderStatement;//所属的对账单
	
	@Text("调度员")
	@OneToOne(fetch=FetchType.LAZY)
	private User scheduler;
	
	@Text("是否在调度")
	private boolean scheduling; //是否正在调度
	
	@Text("调度开始时间")
	private Date schedulingBeginTime; //调度开始时间
	
	@Text("订单来源")
	private OrderSourceEnum orderSource;
	
	@Text("是否为他人叫车")
	private boolean callForOther;
	
	@Text("实际乘车人")
	private String otherPassengerName;
	
	@Text("实际乘车人手机号码")
	private String otherPhoneNumber;
	
	@Text("是否给实际乘车人发送短信")
	private boolean callForOtherSendSMS;
	
	@Text("是否已经评价过")
	private boolean evaluated;
	
	@Text("评价分数")
	private int grade;
	
	@Text("油费")
	private BigDecimal refuelMoney;
	
	@Text("加油记录")
	@OneToOne(fetch=FetchType.LAZY)
	private CarRefuel refuel;
	
	@Text("洗车费")
	private BigDecimal washingFee;
	
	@Text("停车费")
	private BigDecimal parkingFee;
	
	@Text("过路费")
	private BigDecimal toll;
	
	@Text("食宿费")
	private BigDecimal roomAndBoardFee;
	
	@Text("其它费用")
	private BigDecimal otherFee;
	
	@Text("意见及建议")
	private String options;

	public OrderStatement getOrderStatement() {
		return orderStatement;
	}

	public void setOrderStatement(OrderStatement orderStatement) {
		this.orderStatement = orderStatement;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public CustomerOrganization getCustomerOrganization() {
		return customerOrganization;
	}

	public void setCustomerOrganization(
			CustomerOrganization customerOrganization) {
		this.customerOrganization = customerOrganization;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public ChargeModeEnum getChargeMode() {
		return chargeMode;
	}

	public void setChargeMode(ChargeModeEnum chargeMode) {
		this.chargeMode = chargeMode;
	}

	public Date getPlanBeginDate() {
		return planBeginDate;
	}

	public void setPlanBeginDate(Date planBeginDate) {
		this.planBeginDate = planBeginDate;
	}

	public Date getPlanEndDate() {
		return planEndDate;
	}

	public void setPlanEndDate(Date planEndDate) {
		this.planEndDate = planEndDate;
	}

	public CarServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(CarServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public BigDecimal getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(BigDecimal orderMoney) {
		this.orderMoney = orderMoney;
	}

	public BigDecimal getActualMoney() {
		return actualMoney;
	}

	public void setActualMoney(BigDecimal actualMoney) {
		this.actualMoney = actualMoney;
	}

	public Date getQueueTime() {
		return queueTime;
	}

	public void setQueueTime(Date queueTime) {
		this.queueTime = queueTime;
	}

	public Date getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(Date scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public User getDriver() {
		return driver;
	}

	public void setDriver(User driver) {
		this.driver = driver;
	}

	public OrderStatusEnum getStatus() {
		return status;
	}

	public void setStatus(OrderStatusEnum status) {
		this.status = status;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public List<OrderOperationRecord> getOperationRecord() {
		return operationRecord;
	}

	public void setOperationRecord(List<OrderOperationRecord> operationRecord) {
		this.operationRecord = operationRecord;
	}

	public DiskFile getSignature() {
		return signature;
	}

	public void setSignature(DiskFile signature) {
		this.signature = signature;
	}

	public Date getAcceptedTime() {
		return acceptedTime;
	}

	public void setAcceptedTime(Date acceptedTime) {
		this.acceptedTime = acceptedTime;
	}

	public User getScheduler() {
		return scheduler;
	}

	public void setScheduler(User scheduler) {
		this.scheduler = scheduler;
	}

	public boolean isScheduling() {
		return scheduling;
	}

	public void setScheduling(boolean scheduling) {
		this.scheduling = scheduling;
	}

	public Date getSchedulingBeginTime() {
		return schedulingBeginTime;
	}

	public void setSchedulingBeginTime(Date schedulingBeginTime) {
		this.schedulingBeginTime = schedulingBeginTime;
	}

	public OrderSourceEnum getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(OrderSourceEnum orderSource) {
		this.orderSource = orderSource;
	}

	public boolean isCallForOther() {
		return callForOther;
	}

	public void setCallForOther(boolean callForOther) {
		this.callForOther = callForOther;
	}
	
	public String getOtherPassengerName() {
		return otherPassengerName;
	}

	public void setOtherPassengerName(String otherPassengerName) {
		this.otherPassengerName = otherPassengerName;
	}

	public String getOtherPhoneNumber() {
		return otherPhoneNumber;
	}

	public void setOtherPhoneNumber(String otherPhoneNumber) {
		this.otherPhoneNumber = otherPhoneNumber;
	}

	public boolean isCallForOtherSendSMS() {
		return callForOtherSendSMS;
	}

	public void setCallForOtherSendSMS(boolean callForOtherSendSMS) {
		this.callForOtherSendSMS = callForOtherSendSMS;
	}

	public boolean isEvaluated() {
		return evaluated;
	}

	public void setEvaluated(boolean evaluated) {
		this.evaluated = evaluated;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public List<DayOrderDetail> getDayDetails() {
		return dayDetails;
	}

	public void setDayDetails(List<DayOrderDetail> dayDetails) {
		this.dayDetails = dayDetails;
	}

	public float getBeginMile() {
		return beginMile;
	}

	public void setBeginMile(float beginMile) {
		this.beginMile = beginMile;
	}

	public float getCustomerGetonMile() {
		return customerGetonMile;
	}

	public void setCustomerGetonMile(float customerGetonMile) {
		this.customerGetonMile = customerGetonMile;
	}

	public float getCustomerGetoffMile() {
		return customerGetoffMile;
	}

	public void setCustomerGetoffMile(float customerGetoffMile) {
		this.customerGetoffMile = customerGetoffMile;
	}

	public float getEndMile() {
		return endMile;
	}

	public void setEndMile(float endMile) {
		this.endMile = endMile;
	}

	public float getTotalChargeMile() {
		return totalChargeMile;
	}

	public void setTotalChargeMile(float totalChargeMile) {
		this.totalChargeMile = totalChargeMile;
	}

	public CarRefuel getRefuel() {
		return refuel;
	}

	public void setRefuel(CarRefuel refuel) {
		this.refuel = refuel;
	}

	public BigDecimal getRefuelMoney() {
		return refuelMoney;
	}

	public void setRefuelMoney(BigDecimal refuelMoney) {
		this.refuelMoney = refuelMoney;
	}

	public BigDecimal getWashingFee() {
		return washingFee;
	}

	public void setWashingFee(BigDecimal washingFee) {
		this.washingFee = washingFee;
	}

	public BigDecimal getParkingFee() {
		return parkingFee;
	}

	public void setParkingFee(BigDecimal parkingFee) {
		this.parkingFee = parkingFee;
	}

	public BigDecimal getToll() {
		return toll;
	}

	public void setToll(BigDecimal toll) {
		this.toll = toll;
	}

	public BigDecimal getRoomAndBoardFee() {
		return roomAndBoardFee;
	}

	public void setRoomAndBoardFee(BigDecimal roomAndBoardFee) {
		this.roomAndBoardFee = roomAndBoardFee;
	}

	public BigDecimal getOtherFee() {
		return otherFee;
	}

	public void setOtherFee(BigDecimal otherFee) {
		this.otherFee = otherFee;
	}

	public Date getActualBeginDate() {
		return actualBeginDate;
	}

	public void setActualBeginDate(Date actualBeginDate) {
		this.actualBeginDate = actualBeginDate;
	}

	public Date getActualEndDate() {
		return actualEndDate;
	}

	public void setActualEndDate(Date actualEndDate) {
		this.actualEndDate = actualEndDate;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}	
}
