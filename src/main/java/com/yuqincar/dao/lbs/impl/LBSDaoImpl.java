package com.yuqincar.dao.lbs.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
	}
	
	private List<MileInfo> getMile(Car car, long from, long to){
		String api="http://api2.capcare.com.cn:1045/api/findDeviceSport.do?deviceSn="+car.getDevice().getSN()
				+"&begin=%s&end=%s&token="+Configuration.getCapcareToken()+"&user_id="+Configuration.getCapcareUserId()
				+"&app_name="+Configuration.getCapcareAppName()+"&language=zh_CN";
			
		String json=HttpMethod.get(String.format(api, from,to));
		JSONObject data = (JSONObject) JSON.parse(json);
		JSONArray spots = (JSONArray) data.get("spots");
		if(spots!=null && spots.size()>0){
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
	}
		
	private MileInfo getMileOnTime(Car car,Date date){
		if(car.getDevice()==null)
			return null;
		
		//如果没有返回这个时间点的数据，那么就向两个方向扩展，获取最近的数据。
		
		long minute=60000;
		long hour=3600000;
		long d=date.getTime();
		List<MileInfo> miles;
		
		miles=getMile(car,d,d+minute);
		if(miles!=null && miles.size()>0){
//			System.out.println("here:"+miles.get(0).getTime()+"-"+miles.get(0).getMile()+"-"+(d-miles.get(0).getTime()));
			for(int i=0;i<miles.size();i++)
				if(miles.get(i).getMile()>0)
					return miles.get(i);
		}
			
		long floorLimit=1481731200000L; //总里程数从2016-12-15 00:00:00（1481731200000）这个时间才开始存储
		long upperLimit=new Date().getTime();
		MileInfo mile=null;
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
				
//			if(leftMile!=null)
//				System.out.println("left:"+leftMile.getTime()+"-"+leftMile.getMile()+"-"+(d-leftMile.getTime()));
//			if(rightMile!=null)
//				System.out.println("right:"+rightMile.getTime()+"-"+rightMile.getMile()+"-"+(rightMile.getTime()-d));
				
			if(leftMile!=null && rightMile!=null){
				if(d-leftMile.getTime()<rightMile.getTime()-d)
					mile=leftMile;
				else
					mile=rightMile;
			}else if(leftMile!=null)
				mile=leftMile;
			else if(rightMile!=null)
				mile=rightMile;
				
			if(mile!=null && mile.getMile()>0)
				break;
				
			leftTo=leftTo-hour;
			leftFrom=leftTo-hour;
			rightFrom=rightFrom+hour;
			rightTo=rightFrom+hour;
		}
		return mile;
	}
	
	
	public float getStepMile(Car car, Date beginTime, Date endTime) {
		if(car.getDevice()==null)
			return -1;
//		float mile1=getStepMileByMeter(car,beginTime,endTime);
		float mile2=getStepMileByEnhancedMeter(car,beginTime,endTime);
//		float mile3=getStepMileByTrack(car,beginTime,endTime);
		float mile4=getStepMileByEnhancedTrack(car,beginTime,endTime);
		System.out.println("mile2="+mile2);
		System.out.println("mile4="+mile4);
		if(mile2>mile4)
			return mile2;
		else
			return mile4;
	}
	
	private float getStepMileByMeter(Car car, Date beginTime, Date endTime){
		MileInfo from=getMileOnTime(car,beginTime);
		MileInfo to=getMileOnTime(car,endTime);
		if(from!=null && to!=null)
			return new BigDecimal((to.getMile()-from.getMile())/1000).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
		else
			return -1;
	}
	
	private float getStepMileByEnhancedMeter(Car car, Date beginTime, Date endTime){
		MileInfo from=getMileOnTime(car,beginTime);
		MileInfo to=getMileOnTime(car,endTime);
//		System.out.println("from:"+from.getMile());
//		System.out.println("to:"+to.getMile());
		if(from!=null && to!=null){
			long step=2*60*60*1000;	//每次查两小时。因为接口限制了不能超过两小时。
			long begin=from.getTime();
			long end=to.getTime();
			float offsetMile=0;
			while(true){
				if(begin>=to.getTime())
					break;
				if(end>=to.getTime())
					end=to.getTime();
				List<MileInfo> miles=getMile(car,begin,end);
				if(miles!=null && miles.size()>1){
//					System.out.println("0:"+miles.get(0).getMile());
					for(int i=1;i<miles.size();i++){
//						System.out.println(i+":"+miles.get(i).getMile());
						if(miles.get(i).getMile()==0)
							continue;
						float lastMile=0;
						for(int j=i-1;j>=0;j--){
							lastMile=miles.get(j).getMile();
							if(lastMile>0)
								break;
						}
						if(lastMile==0)
							continue;
						if(miles.get(i).getMile()<lastMile){
							offsetMile=offsetMile+(lastMile-miles.get(i).getMile());
//							System.out.println("offsetMile="+offsetMile);
						}
					}
				}
				begin=begin+step;
				end=begin+step;
			}
			return new BigDecimal((to.getMile()-from.getMile()+offsetMile)/1000).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
		}else
			return -1;
	}
	
	private float getStepMileByTrack(Car car, Date beginTime, Date endTime) {
		if(car.getDevice()==null || StringUtils.isEmpty(car.getDevice().getSN()))
			return 0;
		String api = "http://api.capcare.com.cn:1045/api/get.part.do?device_sn="
				+car.getDevice().getSN()+"&begin="+beginTime.getTime()+"&end="+endTime.getTime()
				+"&token="+Configuration.getCapcareToken()+"&user_id="+Configuration.getCapcareUserId()
				+"&app_name="+Configuration.getCapcareAppName()+"&language=zh_CN&_=1450765172310";
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

/**
	 * 利用车辆轨迹计算两个时间点之间的行驶里程数，单位KM
	 * @param car
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	private float getStepMileByEnhancedTrack(Car car, Date beginDate, Date endDate){
		
		float distanceOfTrack = 0;
		float betweenTrackDistance = 0;
		
		String url = "http://api.capcare.com.cn:1045/api/get.part.do?device_sn="+car.getDevice().getSN()
								+"&begin="+beginDate.getTime()+"&end="+endDate.getTime()+"&token="+Configuration.getCapcareToken()
								+"&user_id="+Configuration.getCapcareUserId()+"&app_name="+Configuration.getCapcareAppName();
		
		String json = HttpMethod.get(url);
		JSONObject jsonObject = (JSONObject) JSON.parse(json);

		JSONArray tracksOfJson = jsonObject.getJSONArray("track");
		JSONArray tracks = new JSONArray();
		
		//得到的轨迹序列是倒序的，先进行倒序排列
		for(int i=tracksOfJson.size()-1;i>=0;i--){
			distanceOfTrack += Float.parseFloat(tracksOfJson.getJSONObject(i).getString("distance"));
			tracks.add(tracksOfJson.getJSONObject(i));
		}
		//如果轨迹段数小于2，返回distanceOfTrack
		if(tracks.size() < 2){
			return distanceOfTrack;
		}else{
			//计算前一段轨迹的结束点到下一段轨迹的开始点的距离
			for(int i=0;i<tracks.size()-1;i++){
				JSONArray states = tracks.getJSONObject(i).getJSONArray("states");
				JSONArray nextStates = tracks.getJSONObject(i+1).getJSONArray("states");
				String origins = states.getJSONObject(0).getString("lat")+","+states.getJSONObject(0).getString("lng");
				String destinations = nextStates.getJSONObject(1).getString("lat")+","+nextStates.getJSONObject(1).getString("lng");
				betweenTrackDistance += calculatePathDistance(origins,destinations);
			}
			return new BigDecimal(betweenTrackDistance+distanceOfTrack).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
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
        //这里使用的百度ak是徐伟良师兄注册的，系统目前的ak=wzq3sn49ZUQuOFRvdoS4HaQnpZLBFBMd，暂时还不能使用这个百度API，已经修改百度开发者的功能，估计等一段时间就可以了。
		String url = "http://api.map.baidu.com/direction/v1/routematrix?output=json&origins="
						+origins+"&destinations="+destinations+"&ak=XNcVScWmj4gRZeSvzIyWQ5TZ";
		String json = HttpMethod.get(url);
		        JSONObject distanceObj=(JSONObject) JSON.parseObject(json).getJSONObject("result").getJSONArray("elements").get(0);
		        String value=distanceObj.getJSONObject("distance").getString("value");
		float distance=Float.parseFloat(value)/1000;
		return distance;
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
	
	public static void main(String[] args) {
		LBSDao lbsDao = new LBSDaoImpl();
		Car car=new Car();
		Device device=new Device();
		device.setSN("892626060702225");
		car.setDevice(device);
		Date date1=DateUtils.getYMDHMS("2016-12-19 00:00:00");
		System.out.println(date1.getTime());
		Date date2=DateUtils.getYMDHMS("2016-12-19 20:06:36");
		System.out.println(date2.getTime());
//		Date date1=DateUtils.getYMDHM("2016-09-06 09:00");
//		Date date2=DateUtils.getYMDHM("2016-09-07 09:00");
//		System.out.println(lbsDao.getStepMile(car,date1,date2));
		float mile = lbsDao.getStepMile(car, date1, date2);
		System.out.println("mile="+mile);
	}

}
