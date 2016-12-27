package com.yuqincar.domain.monitor;

import java.util.List;

import javax.persistence.Entity;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.utils.Text;

/**
 * 凯步获取的数据，持久化到数据库
 * @author cocoa
 *
 */
@Entity
public class CapcareMessage extends BaseEntity {
	@Text("车牌号")
	private String plateNumber;
	@Text("经度")
	private String longitude;
	@Text("纬度")
	private String latitude;
	@Text("速度")
	private String speed;
	@Text("状态")
	private String status;
	@Text("方向")
	private String direction;
	@Text("最后在线时间")
	private String lastTime;
	
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
	public String getLastTime() {
		return lastTime;
	}
	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}
	
	
}
