package com.yuqincar.domain.monitor;

import java.util.List;

/**
 * 凯步获取的数据，不需要保存到数据库
 * @author cocoa
 *
 */
public class CapcareMessage {
	//车牌号
	private String plateNumber;
	//经度
	private String longitude;
	//纬度
	private String latitude;
	//速度
	private String speed;
	//状态
	private String status;
	//方向
	private String direction;
	
	
	public String getPlateNumber() {
		return plateNumber;
	}
	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	
	
}
