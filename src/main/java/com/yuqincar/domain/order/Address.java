package com.yuqincar.domain.order;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.monitor.Location;
import com.yuqincar.utils.Text;

/**
 * 地址
 * @author chenhengxin
 *
 */
@Entity
public class Address extends BaseEntity {
	@Text("简要描述")
	@Column(nullable=false)
	private String description;	//位置描述。对应于地图中的兴趣点

	@Text("详细地址")
	@Column(nullable=false)
	private String detail;	//详细地址

	@Text("位置")
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	private Location location;	//位置

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	
	
	
}
