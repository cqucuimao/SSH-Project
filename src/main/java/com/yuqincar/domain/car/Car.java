package com.yuqincar.domain.car;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.monitor.Device;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.utils.Text;

/*
 * 车
 */

@Entity
public class Car extends BaseEntity {
	@Text("车牌号")
	@Column(nullable = false, unique=true)
	private String plateNumber;	//车牌号

	@Text("品牌")
	@Column(nullable = false)
	private String brand;	//品牌

	@Text("型号")
	@Column(nullable = false)
	private String model;	//型号

	@Text("车架号")
	@Column(nullable = false, unique=true)
	private String VIN;	//识别码

	@Text("发动机号")
	@Column(unique=true)
	private String EngineSN;	//发动机号码

	@Text("车型")
	@OneToOne(fetch=FetchType.LAZY)
	private CarServiceType serviceType;	//车型。代表收费标准

	@Text("司机")
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	private User driver;	//司机

	@Text("业务点")
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	private ServicePoint servicePoint;	//业务点

	@Text("车载设备")
	@OneToOne(fetch=FetchType.LAZY)
	private Device device;	//车载设备

	@Text("里程数")
	private int mileage;	//里程数

	@Text("注册时间")
	@Column(nullable = false)
	private Date registDate;	//注册时间

	@Text("备注")
	private String memo;	//备注

	@Text("下次保养里程")
	private int nextCareMile;	//下次保养里程数

	@Text("保险过期日期")
	private Date insuranceExpiredDate;	//保险过期时间

	@Text("年检到期日期")
	private Date nextExaminateDate;	//年检到期时间

	@Text("车辆状态")
    private CarStatusEnum status; //车辆状态

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getVIN() {
		return VIN;
	}

	public void setVIN(String vIN) {
		VIN = vIN;
	}

	public String getEngineSN() {
		return EngineSN;
	}

	public void setEngineSN(String engineSN) {
		EngineSN = engineSN;
	}

	public CarServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(CarServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public User getDriver() {
		return driver;
	}

	public void setDriver(User driver) {
		this.driver = driver;
	}

	public ServicePoint getServicePoint() {
		return servicePoint;
	}

	public void setServicePoint(ServicePoint servicePoint) {
		this.servicePoint = servicePoint;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public int getMileage() {
		return mileage;
	}

	public void setMileage(int mileage) {
		this.mileage = mileage;
	}

	public Date getRegistDate() {
		return registDate;
	}

	public void setRegistDate(Date registDate) {
		this.registDate = registDate;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public int getNextCareMile() {
		return nextCareMile;
	}

	public void setNextCareMile(int nextCareMile) {
		this.nextCareMile = nextCareMile;
	}

	public Date getInsuranceExpiredDate() {
		return insuranceExpiredDate;
	}

	public void setInsuranceExpiredDate(Date insuranceExpiredDate) {
		this.insuranceExpiredDate = insuranceExpiredDate;
	}

	public Date getNextExaminateDate() {
		return nextExaminateDate;
	}

	public void setNextExaminateDate(Date nextExaminateDate) {
		this.nextExaminateDate = nextExaminateDate;
	}

    public CarStatusEnum getStatus() {
        return status;
    }

    public void setStatus(CarStatusEnum status) {
        this.status = status;
    }
	
	
}
