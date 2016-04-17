package com.yuqincar.dao.lbs;

import com.yuqincar.domain.monitor.Location;

public interface LBSDao {
	//得到汽车的位置
	public Location getCurrentLocation(String sn);
	
	//得到汽车当前里程数 读OBD
	public float getCurrentMile(String sn);
	
	//得到beginTime和endTime之间的里程数。
	public float getStepMile(String sn, String beginTime, String endTime);

}
