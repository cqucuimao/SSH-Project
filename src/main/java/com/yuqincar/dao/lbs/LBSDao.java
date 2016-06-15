package com.yuqincar.dao.lbs;

import java.util.Date;

import com.yuqincar.domain.monitor.Location;

public interface LBSDao {
	//得到汽车的位置
	public Location getCurrentLocation(String sn);
	
	//得到汽车在某个时间点上的里程数
	public float getMileAtMoment(String sn, Date date);
	
	//得到汽车当前里程数读OBD
	public float getCurrentMile(String sn);
	
	//得到beginTime和endTime之间的里程数。
	public float getStepMile(String sn, Date beginTime, Date endTime);

}
