package com.yuqincar.dao.lbs;

import java.util.Date;

import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.monitor.Location;

public interface LBSDao {
	//得到汽车的位置
	public Location getCurrentLocation(Car car);
		
	//得到汽车当前里程数读OBD
	public float getCurrentMile(Car car);
	
	//得到beginTime和endTime之间的里程数。
	public float getStepMile(Car car, Date beginTime, Date endTime);
	
	public float getStepMileByEnhancedTrack(Car car, Date beginDate, Date endDate);

}
