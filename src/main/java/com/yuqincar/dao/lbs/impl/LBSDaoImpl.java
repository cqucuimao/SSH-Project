package com.yuqincar.dao.lbs.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuqincar.dao.lbs.LBSDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.monitor.Device;
import com.yuqincar.domain.monitor.Location;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.HttpMethod;

@Repository
public class LBSDaoImpl implements LBSDao{
	
	public Location getCurrentLocation(Car car) {
		if(car.getDevice()==null || StringUtils.isEmpty(car.getDevice().getSN()))
			return null;
		String api = "http://api.capcare.com.cn:1045/api/device.get.do?device_sn="
				+car.getDevice().getSN()+"&token=FCD037A9-56FF-4962-9B63-8CFA860840C5&user_id=45036&app_name=M2616_BD&language=zh_CN";
		String json = HttpMethod.get(api);
		JSONObject data = (JSONObject) JSON.parse(json);
		JSONObject device = (JSONObject) data.get("device");
		JSONObject position = (JSONObject) device.get("position");
		Location location = new Location();
		location.setLongitude(Double.parseDouble(position.getString("lng")));
		location.setLatitude(Double.parseDouble(position.getString("lat")));
		return location;
	}

	public float getCurrentMile(Car car) {
		if(car.getDevice()==null || StringUtils.isEmpty(car.getDevice().getSN()))
			return 0;
		String api = "http://api2.capcare.com.cn:1045/api/device.get.do?device_sn="
				+car.getDevice().getSN()+"&token=FCD037A9-56FF-4962-9B63-8CFA860840C5&user_id=45036&app_name=M2616_BD&language=zh_CN";
		String json = HttpMethod.get(api);
		JSONObject data = (JSONObject) JSON.parse(json);
		JSONObject device = (JSONObject) data.get("device");
		JSONObject position = (JSONObject) device.get("position");
		String mileStr=position.getString("altitude");
		if(!StringUtils.isEmpty(mileStr))
			return Float.valueOf(mileStr).floatValue();
		else
			return 0;
	}
	
	
	public float getStepMile(Car car, Date beginTime, Date endTime) {
		if(car.getDevice()==null || StringUtils.isEmpty(car.getDevice().getSN()))
			return 0;
		String api = "http://api.capcare.com.cn:1045/api/get.part.do?device_sn="
				+car.getDevice().getSN()+"&begin="+beginTime.getTime()+"&end="+endTime.getTime()
				+"&token=FCD037A9-56FF-4962-9B63-8CFA860840C5&user_id=45036&app_name=M2616_BD&language=zh_CN&_=1450765172310";
		String json = HttpMethod.get(api);
		JSONObject data = (JSONObject) JSON.parse(json);
		JSONArray track = (JSONArray) data.get("track");
		float distance = 0;
		for(int i=0;i<track.size();i++){
			JSONObject obj = (JSONObject) track.get(i);
			distance += obj.getFloatValue("distance");
		}
		return distance;
		
	
	}
	public static void main(String[] args) {
		LBSDao lbsDao = new LBSDaoImpl();
		Car car=new Car();
		Device device=new Device();
		device.setSN("892626060704940");
		car.setDevice(device);
		Date date1=DateUtils.getYMDHM("2016-09-06 09:00");
		Date date2=DateUtils.getYMDHM("2016-09-07 09:00");
		System.out.println(lbsDao.getStepMile(car,date1,date2));
//		float mile = lbsDao.getCurrentMile("892620160125106");
//		System.out.println("mile="+mile);
	}

}
