package com.yuqincar.dao.lbs.impl;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuqincar.dao.lbs.LBSDao;
import com.yuqincar.domain.monitor.Location;
import com.yuqincar.utils.HttpMethod;

@Repository
public class LBSDaoImpl implements LBSDao{
	
	public Location getCurrentLocation(String sn) {
		Location location = new Location();
		location.setLatitude(123.123);
		location.setLongitude(111.11);
		return location;
	}

	public float getCurrentMile(String sn) {
		String api = "http://api.capcare.com.cn:1045/api/obd.last.do?device_sn="
				+sn+"&token=FCD037A9-56FF-4962-9B63-8CFA860840C5&user_id=45036&app_name=M2616_BD&language=zh_CN";
		String json = HttpMethod.get(api);
		
		JSONObject data = (JSONObject) JSON.parse(json);
		JSONObject obd = (JSONObject) data.get("obd");
		String totalDistance = obd.getString("totalDistance");
		return Float.valueOf(totalDistance);
	}
	
	
	public float getCurrentMile(String sn, String beginTime, String endTime) {
		String api = "http://api.capcare.com.cn:1045/api/statistic.do?user_id=45036&token=FCD037A9-56FF-4962-9B63-8CFA860840C5&app_name=M2616_BD&language=zh_CN";
		String params = String.format("&device_sn=%s&begin=%s&end=%s", sn,beginTime,endTime);
		
		String json = HttpMethod.get(api+params);
		JSONObject data = (JSONObject) JSON.parse(json);
		JSONArray mileages = (JSONArray) data.get("mileages");
		float distance = 0;
		if(mileages.size()>0) {
			JSONObject mileage = (JSONObject) mileages.get(0);
			distance = mileage.getFloatValue("distance");
		}
		return distance;

		
	}
	public static void main(String[] args) {
		LBSDao lbsDao = new LBSDaoImpl();
		float mile = lbsDao.getCurrentMile("354188046608662");
		System.out.println(mile);
		
		//1453309203000 2016/1/21 1:0:3
		//1453341603000 2016/1/21 10:0:3
		float mile2 = lbsDao.getCurrentMile("354188046608662","1453309203000","1453341603000");
		
		System.out.println(mile2);
	}

}
