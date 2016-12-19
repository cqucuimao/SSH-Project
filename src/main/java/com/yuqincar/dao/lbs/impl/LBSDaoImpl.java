package com.yuqincar.dao.lbs.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuqincar.dao.lbs.LBSDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.monitor.Device;
import com.yuqincar.domain.monitor.Location;
import com.yuqincar.utils.Configuration;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.HttpMethod;

@Repository
public class LBSDaoImpl implements LBSDao{
	
	public Location getCurrentLocation(Car car) {
		if(car.getDevice()==null || StringUtils.isEmpty(car.getDevice().getSN()))
			return null;
		String api = "http://api.capcare.com.cn:1045/api/device.get.do?device_sn="
				+car.getDevice().getSN()+"&token="+Configuration.getCapcareToken()+"&user_id="+Configuration.getCapcareUserId()
				+"&app_name="+Configuration.getCapcareAppName()+"&language=zh_CN";
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
		try{
			if(car.getDevice()==null || StringUtils.isEmpty(car.getDevice().getSN()))
				return 0;
			String api = "http://api2.capcare.com.cn:1045/api/device.get.do?device_sn="
					+car.getDevice().getSN()+"&token="+Configuration.getCapcareToken()+"&user_id="+Configuration.getCapcareUserId()+"&app_name="
					+Configuration.getCapcareAppName()+"&language=zh_CN";
			String json = HttpMethod.get(api);
			JSONObject data = (JSONObject) JSON.parse(json);
			JSONObject device = (JSONObject) data.get("device");
			JSONObject position = (JSONObject) device.get("position");
			String mileStr=position.getString("altitude");
			if(!StringUtils.isEmpty(mileStr))
				return new BigDecimal(mileStr).divide(new BigDecimal(1000)).intValue();
			else
				return 0;
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	
	private List<MileInfo> getMile(Car car, long from, long to){
		try{			
			String api="http://api2.capcare.com.cn:1045/api/findDeviceSport.do?deviceSn="+car.getDevice().getSN()
					+"&begin=%s&end=%s&token="+Configuration.getCapcareToken()+"&user_id="+Configuration.getCapcareUserId()
					+"&app_name="+Configuration.getCapcareAppName()+"&language=zh_CN";
			
			String json=HttpMethod.get(String.format(api, from,to));
			JSONObject data = (JSONObject) JSON.parse(json);
			JSONArray spots = (JSONArray) data.get("spots");
			if(spots.size()>0){
				List<MileInfo> miles=new ArrayList<MileInfo>(spots.size());
				for(int i=spots.size()-1;i>=0;i--){
					JSONObject spot=(JSONObject)spots.get(i);
					MileInfo mi=new MileInfo();
					mi.setMile(spot.getFloatValue("altitude"));
					mi.setTime(spot.getLongValue("receive"));
					miles.add(mi);
				}
				return miles;
			}else
				return null;
		}catch(Exception e){
			return null;
		}
	}
		
	public float getMileOnTime(Car car,Date date){
		if(car.getDevice()==null)
			return -1;
		
		//如果没有返回这个时间点的数据，那么就向两个方向扩展，获取最近的数据。
		
		long minute=60000;
		long hour=3600000;
		long d=date.getTime();
		List<MileInfo> miles;
		
		miles=getMile(car,d,d+minute);
		if(miles!=null && miles.size()>0){
			System.out.println("here:"+miles.get(0).getTime()+"-"+miles.get(0).getMile()+"-"+(d-miles.get(0).getTime()));
			return miles.get(0).getMile();
		}else{
			long floorLimit=1481731200000L; //总里程数从2016-12-15 00:00:00（1481731200000）这个时间才开始存储
			long upperLimit=new Date().getTime();
			float mile=-1;
			long leftFrom,leftTo,rightFrom,rightTo;
			leftFrom=d-hour;
			leftTo=d;
			rightFrom=d;
			rightTo=d+hour;
			while(true){
				boolean lowerThanFloor=false;
				boolean higherThanUpper=false;
				MileInfo leftMile=null,rightMile=null;
				if(leftTo<floorLimit)
					lowerThanFloor=true;
				else if(leftFrom<floorLimit)
					leftFrom=floorLimit;
				
				if(rightFrom>upperLimit)
					higherThanUpper=true;
				else if(rightTo>upperLimit)
					rightTo=upperLimit;
				
				if(lowerThanFloor && higherThanUpper)
					break;
				
				if(!lowerThanFloor){
					miles=getMile(car,leftFrom,leftTo);
					if(miles!=null && miles.size()>0)
						leftMile=miles.get(miles.size()-1);
				}
				
				if(!higherThanUpper){
					miles=getMile(car,rightFrom,rightTo);
					if(miles!=null && miles.size()>0)
						rightMile=miles.get(0);
				}
				
				if(leftMile!=null)
					System.out.println("left:"+leftMile.getTime()+"-"+leftMile.getMile()+"-"+(d-leftMile.getTime()));
				if(rightMile!=null)
					System.out.println("right:"+rightMile.getTime()+"-"+rightMile.getMile()+"-"+(rightMile.getTime()-d));
				
				if(leftMile!=null && rightMile!=null){
					if(d-leftMile.getTime()<rightMile.getTime()-d)
						mile=leftMile.getMile();
					else
						mile=rightMile.getMile();
				}else if(leftMile!=null)
					mile=leftMile.getMile();
				else if(rightMile!=null)
					mile=rightMile.getMile();
				
				if(mile>0)
					break;
				
				leftTo=leftTo-hour;
				leftFrom=leftTo-hour;
				rightFrom=rightFrom+hour;
				rightTo=rightFrom+hour;
			}
			return mile;
		}
	}
	
	
	public float getStepMile(Car car, Date beginTime, Date endTime) {
		if(car.getDevice()==null)
			return -1;
		
		float from=getMileOnTime(car,beginTime);
		float to=getMileOnTime(car,endTime);
		if(from>0 && to>0)
			return to-from;
		else
			return -1;
	}
	
	class MileInfo{
		private float mile;
		private long time;
		public float getMile() {
			return mile;
		}
		public void setMile(float mile) {
			this.mile = mile;
		}
		public long getTime() {
			return time;
		}
		public void setTime(long time) {
			this.time = time;
		}		
	}
	
	/**
	 * 获取两个时间点之间丢失轨迹的距离，单位KM
	 * @param car
	 * @param beginDate
	 * @param endDate
	 * @return
	 * http://api.capcare.com.cn:1045/api/get.part.do?device_sn=892626060703066&begin=1482076800000&end=1482117153000
	 * &token=FCD037A9-56FF-4962-9B63-8CFA860840C5&user_id=45036&app_name=M2616_BD
	 */
	private float getMilesUsingTrack(Car car, Date beginDate, Date endDate){
		
		float sumDistance = 0;
		
		String url = "http://api.capcare.com.cn:1045/api/get.part.do?device_sn="+car.getDevice().getSN()
								+"&begin="+beginDate.getTime()+"&end="+endDate.getTime()+"&token="+Configuration.getCapcareToken()
								+"&user_id="+Configuration.getCapcareUserId()+"&app_name="+Configuration.getCapcareAppName();
		
		String json = HttpMethod.get(url);
		JSONObject jsonObject = (JSONObject) JSON.parse(json);

		JSONArray tracksOfJson = jsonObject.getJSONArray("track");
		JSONArray tracks = new JSONArray();
		
		//得到的轨迹序列是倒序的，先进行倒序排列
		for(int i=tracksOfJson.size()-1;i>=0;i--){
			tracks.add(tracksOfJson.getJSONObject(i));
		}
		//如果轨迹段数小于2，返回0
		if(tracks.size() < 2){
			return 0;
		}else{
			//计算前一段轨迹的结束点到下一段轨迹的开始点的距离
			for(int i=0;i<tracks.size()-1;i++){
				JSONArray states = tracks.getJSONObject(i).getJSONArray("states");
				JSONArray nextStates = tracks.getJSONObject(i+1).getJSONArray("states");
				String origins = states.getJSONObject(0).get("lat").toString()+","+states.getJSONObject(0).get("lng").toString();
				String destinations = nextStates.getJSONObject(1).get("lat").toString()+","+nextStates.getJSONObject(1).get("lng").toString();
				sumDistance += calculatePathDistance(origins,destinations);
			}
			return sumDistance;
		}
	}
	/**
	 * 百度地图接口计算两坐标两之间的距离,单位KM
	 * 这里进行了测试，当两坐标点距离很近或者完全相同时，百度也会有返回值，并且返回值基本在0.2KM以内
	 * @param origins
	 * @param destinations
	 * @return
	 */
	private float calculatePathDistance(String origins, String destinations) {
        //这里使用的百度ak是徐伟良师兄注册的，我们系统目前的ak=wzq3sn49ZUQuOFRvdoS4HaQnpZLBFBMd，暂时还不能使用这个接口
		String url = "http://api.map.baidu.com/direction/v1/routematrix?output=json&origins="
						+origins+"&destinations="+destinations+"&ak=XNcVScWmj4gRZeSvzIyWQ5TZ";
		String json = HttpMethod.get(url);
		        JSONObject distanceObj=(JSONObject) JSON.parseObject(json).getJSONObject("result").getJSONArray("elements").get(0);
		        String value=distanceObj.getJSONObject("distance").getString("value");
		float distance=Float.parseFloat(value)/1000;
		return distance;
	}
	
	public static void main(String[] args) {
		LBSDao lbsDao = new LBSDaoImpl();
		LBSDaoImpl lbsDaoImpl = new LBSDaoImpl();
		Car car=new Car();
		Device device=new Device();
		device.setSN("892626060703348");
		car.setDevice(device);
		Date date1=DateUtils.getYMDHMS("2016-12-14 19:45:59");
		System.out.println(date1.getTime());
		Date date2=DateUtils.getYMDHMS("2016-12-17 08:55:17");
		System.out.println(date2.getTime());
		
		//float mile = lbsDao.getStepMile(car, date1, date2);
		//System.out.println("mile="+mile);
		System.out.println(lbsDaoImpl.getMilesUsingTrack(car,date1,date2));
	}

}
