package com.yuqincar.domain.monitor;

import javax.persistence.Entity;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.utils.Text;

/*
 *	位置（经度，纬度）。不保存到数据库中。参见LocationDB
 */
@Entity
public class Location extends BaseEntity{

	@Text("经度")
	private double longitude;	//经度
	@Text("纬度")
	private double latitude;	//纬度

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
