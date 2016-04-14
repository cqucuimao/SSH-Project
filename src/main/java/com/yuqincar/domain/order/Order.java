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
import com.yuqincar.domain.car.CarServiceType;
import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.common.DiskFile;
import com.yuqincar.domain.monitor.Location;
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
	private Date planBeginDate; // 预计开始时间

	@Text("计划结束时间")
	private Date planEndDate; // 预计结束时间。如果是按里程租车，则为null。

	@Text("乘车人数")
	@Column(nullable = false)
	private int passengerNumber; // 乘车人数

	@Text("车型")
	@OneToOne(fetch=FetchType.LAZY)
	private CarServiceType serviceType; // 车型

	@Text("出发地")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
	private Address fromAddress; // 上车地点

	@Text("目的地")
    @OneToOne(fetch = FetchType.LAZY)
	private Address toAddress; // 下车地点

	@Text("订单里程")
    private float orderMile; // 订单里程数。初始时，等于按照计划估算的里程数，调度员可修改此值

	@Text("开始时里程")
	private float actualBeginMile; // 实际开始时的里程数

	@Text("结束时里程")
	private float actualEndMile; // 实际结束时的里程数

	@Text("实际里程")
	private float actualMile; // 实际里程数。根据司机点击APP中的“开始”和“结束”时间点，由车载设备上传的里程数相减得到。

	@Text("订单金额")
	@Column(nullable = false, precision = 19, scale = 6)
	private BigDecimal orderMoney; // 订单价格。

	@Text("实际金额")
	@Column(precision = 19, scale = 6)
	private BigDecimal actualMoney; // 实际价格。根据实际里程数或实际发生天数计算的实际价格。

	@Text("实际开始时间")
	private Date actualBeginDate; // 实际开始时间。司机点击“开始”时的时间

	@Text("实际结束时间")
	private Date actualEndDate; // 实际结束时间。司机点击“结束”时的时间

	@Text("实际天数")
	@Column(columnDefinition="int default 0")
	private int actualDay = 0; // 实际发生的天数

	@Text("实际开始位置")
	@OneToOne(fetch=FetchType.LAZY)
	private Location actualBeginLocation; // 实际开始位置

	@Text("实际结束位置")
	@OneToOne(fetch=FetchType.LAZY)
	private Location actualEndLocation; // 实际结束位置

	@Text("进队列时间")
	private Date queueTime; // 进队列的时间。如果没有进队列，就为null。

	@Text("调度时间")
	private Date scheduleTime; // 调度时间

	@Text("接受时间")
	private Date acceptedTime; //接受时间

	@Text("执行车辆")
	@OneToOne(fetch=FetchType.LAZY)
	private Car car; // 执行订单的车辆

	@Text("执行司机")
	@OneToOne(fetch=FetchType.LAZY)
	private User driver; // 执行订单的司机

	@Text("订单状态")
	@Column(nullable = false)
	private OrderStatusEnum status; // 订单状态

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

	public int getPassengerNumber() {
		return passengerNumber;
	}

	public void setPassengerNumber(int passengerNumber) {
		this.passengerNumber = passengerNumber;
	}

	public CarServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(CarServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public Address getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(Address fromAddress) {
		this.fromAddress = fromAddress;
	}

	public Address getToAddress() {
		return toAddress;
	}

	public void setToAddress(Address toAddress) {
		this.toAddress = toAddress;
	}

	public float getOrderMile() {
		return orderMile;
	}

	public void setOrderMile(float orderMile) {
		this.orderMile = orderMile;
	}

	public float getActualBeginMile() {
		return actualBeginMile;
	}

	public void setActualBeginMile(float actualBeginMile) {
		this.actualBeginMile = actualBeginMile;
	}

	public float getActualEndMile() {
		return actualEndMile;
	}

	public void setActualEndMile(float actualEndMile) {
		this.actualEndMile = actualEndMile;
	}

	public float getActualMile() {
		return actualMile;
	}

	public void setActualMile(float actualMile) {
		this.actualMile = actualMile;
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

	public int getActualDay() {
		return actualDay;
	}

	public void setActualDay(int actualDay) {
		this.actualDay = actualDay;
	}

	public Location getActualBeginLocation() {
		return actualBeginLocation;
	}

	public void setActualBeginLocation(Location actualBeginLocation) {
		this.actualBeginLocation = actualBeginLocation;
	}

	public Location getActualEndLocation() {
		return actualEndLocation;
	}

	public void setActualEndLocation(Location actualEndLocation) {
		this.actualEndLocation = actualEndLocation;
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
}
