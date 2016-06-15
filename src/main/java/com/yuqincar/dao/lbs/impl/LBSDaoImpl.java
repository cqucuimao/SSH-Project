package com.yuqincar.dao.lbs.impl;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuqincar.dao.lbs.LBSDao;
import com.yuqincar.domain.monitor.Location;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.HttpMethod;

@Repository
public class LBSDaoImpl implements LBSDao{
	
	public Location getCurrentLocation(String sn) {
		String api = "http://api.capcare.com.cn:1045/api/device.get.do?device_sn="
				+sn+"&token=FCD037A9-56FF-4962-9B63-8CFA860840C5&user_id=45036&app_name=M2616_BD&language=zh_CN";
		String json = HttpMethod.get(api);
		JSONObject data = (JSONObject) JSON.parse(json);
		JSONObject device = (JSONObject) data.get("device");
		JSONObject position = (JSONObject) device.get("position");
		Location location = new Location();
		location.setLongitude(Double.parseDouble(position.getString("lng")));
		location.setLatitude(Double.parseDouble(position.getString("lat")));
		System.out.println("location.lng"+location.getLongitude());
		System.out.println("location.lat"+location.getLatitude());
		return location;
	}
	
	public float getMileAtMoment(String sn, Date date){
		//TODO
		return 0;
	}

	public float getCurrentMile(String sn) {
		String api = "http://api.capcare.com.cn:1045/api/obd.last.do?device_sn="
				+sn+"&token=FCD037A9-56FF-4962-9B63-8CFA860840C5&user_id=45036&app_name=M2616_BD&language=zh_CN";
		String json = HttpMethod.get(api);
		
		JSONObject data = (JSONObject) JSON.parse(json);
		JSONObject obd = (JSONObject) data.get("obd");
		String totalDistance=null;
		//TODO 由于目前测试设备不能直接读取公里数，需要另行设置初始公里后，才可以。所以现在返回的obd是null，为了集成测试，暂且直接复制十万公里。
		if(obd!=null)
			totalDistance = obd.getString("totalDistance");
		else
			totalDistance = "100000";
		return Float.valueOf(totalDistance);
	}
	
	
	public float getStepMile(String sn, Date beginTime, Date endTime) {
		String api = "http://api.capcare.com.cn:1045/api/get.part.do?device_sn="
				+sn+"&begin="+beginTime.getTime()+"&end="+endTime.getTime()+"&token=FCD037A9-56FF-4962-9B63-8CFA860840C5&user_id=45036&app_name=M2616_BD&language=zh_CN&_=1450765172310";
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
//		Location location=lbsDao.getCurrentLocation("892620160125106");
//		System.out.println("location.lng"+location.getLongitude());
//		System.out.println("location.lat"+location.getLatitude());
		Date from=DateUtils.getYMDHMS("2016-04-15 08:20:00");
		Date to=DateUtils.getYMDHMS("2016-04-15 22:00:00");
		System.out.println("from="+from);
		System.out.println("to="+to);
		float mile = lbsDao.getStepMile("892620160125106",from,to);
		System.out.println(mile);
//		
//		//1453309203000 2016/1/21 1:0:3
//		//1453341603000 2016/1/21 10:0:3
//		float mile2 = lbsDao.getCurrentMile("354188046608662","1453309203000","1453341603000");
		
	}

}
