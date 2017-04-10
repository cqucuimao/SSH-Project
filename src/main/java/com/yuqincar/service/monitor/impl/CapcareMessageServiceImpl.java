package com.yuqincar.service.monitor.impl;

import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.conn.ConnectTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.yuqincar.action.lbs.GetBaiduMsg;
import com.yuqincar.action.lbs.GetCapcareMsg;
import com.yuqincar.dao.monitor.CapcareMessageDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.monitor.CapcareMessage;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.monitor.CapcareMessageService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.HttpMethod;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
@Service
public class CapcareMessageServiceImpl implements CapcareMessageService{
	
	//声明的静态变量，用于存储车辆位置信息。始终存在于内存中...
	public static Map<String,CapcareMessage> capcareMap = new HashMap<String, CapcareMessage>();
	
	@Autowired
	private CapcareMessageDao capcareMessageDao;
	
	@Autowired
	private CarService carService;

	@Transactional
	public void delete(long id) {
		capcareMessageDao.delete(id);
	}

	@Transactional
	public void save(CapcareMessage capcareMessage) {
		capcareMessageDao.save(capcareMessage);
	}

	@Transactional
	public void update(CapcareMessage capcareMessage) {
		capcareMessageDao.update(capcareMessage);
	}

	public CapcareMessage getCapcareMessageByPlateNumber(String plateNumber) {

		return null;
	}
	@Transactional
	public void initCapcareMessages() {
		//按照一定时间间隔，倒退的顺序去capcare平台获取每辆车的位置信息。
		//然后将这一次轨迹的最后一个mode为A的位置点数据的位置信息作为这个车辆的初始化数据。
		//每次查询一辆车位置信息的url： http://api.capcare.com.cn:1045/api/device.get.do?device_sn=892626060704080&token=FCD037A9-56FF-4962-9B63-8CFA860840C5&user_id=45036&app_name=M2616_BD
		//http://api.capcare.com.cn:1045/api/get.part.do?device_sn=892616060701392&begin=1472659200000&end=1482163200000&token=FCD037A9-56FF-4962-9B63-8CFA860840C5&user_id=45036&app_name=M2616_BD
		String beginUrl = "http://api.capcare.com.cn:1045/api/device.get.do?";
		String endUrl = "&token=FCD037A9-56FF-4962-9B63-8CFA860840C5&user_id=45036&app_name=M2616_BD";
		
		String beginUrlPart = "http://api.capcare.com.cn:1045/api/get.part.do?";
		String endUrlPart = "&token=FCD037A9-56FF-4962-9B63-8CFA860840C5&user_id=45036&app_name=M2616_BD";
		//获取所有监控车辆
		List<Car> allMonitorCar = carService.getCarsForMonitoring();
		
		//获得所有监控车辆的device_sn
		List<String> deviceSn = new ArrayList<String>();
		for(Car car : allMonitorCar){
			deviceSn.add(car.getDevice().getSN());
		}
		
		//根据设备号和时间戳，获取每辆车的位置信息。如果返回信息的mode不是A，时间倒退一周再查询一次。如果倒退到2016-9-1还没找到mode为A的位置信息，就把当前的位置作为初始化位置。
		int n=10;
		for(int i=120;i<deviceSn.size();i++){
			
			String sn=deviceSn.get(i);
			if(n==0)
				break;
			n--;

			System.out.println("i="+i+":"+sn);
			String url = beginUrl+"device_sn="+sn+endUrl;
			//访问凯步关爱
			String capcareData = HttpMethod.get(url);
			JSONObject jsonObject = JSONObject.fromObject(capcareData);
			JSONObject device = jsonObject.getJSONObject("device");
			JSONObject position = device.getJSONObject("position");
			String plateNumber = device.get("name").toString();
			String longitude = position.get("lng").toString();
			String latitude = position.get("lat").toString();
			String speed = position.get("speed").toString();
			String status = position.get("status").toString();
			String direction = position.get("direction").toString();
			
			String dateStr = "2016-9-1 00:00"; 
			long dateOf20160901 = DateUtils.getYMDHM(dateStr).getTime();
			
			Date now = new Date();
			long dateOfNow = now.getTime();
			long begin = dateOfNow - 604800000;
			long end = dateOfNow;
			
			//如果mode不是A，就查询轨迹，获取轨迹中的最新位置。如果获取不到，倒退一周再查询，一直倒退到2016-09-01
			while(!position.get("mode").toString().equals("A") &&  begin > dateOf20160901){
				String urlPart = beginUrlPart+"device_sn="+sn+"&begin="+begin+"&end="+end+endUrlPart;
				System.out.println(urlPart);
				String json = HttpMethod.get(urlPart);
				//开始时间和结束时间都倒退一周
				begin = begin - 604800000;
				end = end - 604800000;
				JSONObject jsonObjectPart = JSONObject.fromObject(json);
				if(jsonObjectPart.getString("ret").equals("1") ){
					JSONArray tracks = jsonObjectPart.getJSONArray("track");
					if(tracks.size()>0){
						JSONArray states = tracks.getJSONObject(0).getJSONArray("states");
						latitude = states.getJSONObject(0).getString("lat");
						longitude = states.getJSONObject(0).getString("lng");
						break;
					}else{
						latitude = "0.0";
						longitude = "0.0";
					}
				}else{
					latitude = "0.0";
					longitude = "0.0";
					break;
				}
			}
			//到百度去进行坐标转换，坐标为0时，初始化位置为出厂位置。并且数据初始化到数据库
			GetBaiduMsg gbm = new GetBaiduMsg();
			if(! longitude.equals("0.0") && ! latitude.equals("0.0")){

				String pointStr = longitude+","+latitude;
				JSONObject baiduJasonObject = JSONObject.fromObject(gbm.excute(pointStr));
				JSONArray baiduPointArray = baiduJasonObject.getJSONArray("result");
				CapcareMessage cm = new CapcareMessage();
				cm.setPlateNumber(plateNumber);
				cm.setLongitude(baiduPointArray.getJSONObject(0).get("x").toString());
				cm.setLatitude(baiduPointArray.getJSONObject(0).get("y").toString());
				cm.setSpeed(speed);
				cm.setStatus(status);
				cm.setDirection(direction);
				

				capcareMessageDao.save(cm);
			}else{
				String pointStr = "113.932132"+","+"22.637883";
				JSONObject baiduJasonObject = JSONObject.fromObject(gbm.excute(pointStr));
				JSONArray baiduPointArray = baiduJasonObject.getJSONArray("result");
				CapcareMessage cm = new CapcareMessage();
				cm.setPlateNumber(plateNumber);
				cm.setLongitude(baiduPointArray.getJSONObject(0).get("x").toString());
				cm.setLatitude(baiduPointArray.getJSONObject(0).get("y").toString());
				cm.setSpeed(speed);
				cm.setStatus(status);
				cm.setDirection(direction);

				capcareMessageDao.save(cm);
			}
			
		}
	
	}

	public List<CapcareMessage> getAllCapcareMessage() {
		
		return capcareMessageDao.getAll();
	}
	
	/**
	 * 每个10S从凯步获取车辆位置信息
	 */
	public void getCapcareMessagePerTenSecondFromCapcare() {
		int limitLen = 98;
		//考虑到服务器重启后的第一次访问，内存中是没有位置信息的，如果map.size()==0,那么就从数据库的CapcareMessage实体中获取位置来初始化map。
		if(capcareMap.size() == 0){
			List<CapcareMessage> capcareMessages = capcareMessageDao.getAll();
			for(CapcareMessage capcareMessage : capcareMessages){
				capcareMap.put(capcareMessage.getPlateNumber(), capcareMessage);
			}
		}


		//这两行代码是到凯步平台获取全部监控车辆的位置信息，返回Json数据
		GetCapcareMsg gcm = new GetCapcareMsg();
		String capcareMessageString  = gcm.excute();
		
		JSONObject jsonObject = JSONObject.fromObject(capcareMessageString);
		JSONArray devices = jsonObject.getJSONArray("devices");
		JSONArray notNullDevices = new JSONArray();
		int sum = devices.size();
		CapcareMessage capcareMessage;
		String sn = null;
		String plateNumberFromCapcare = null;
		
		for(int i=0;i<sum;i++){
			sn = devices.getJSONObject(i).getString("sn");
			//catch NullPointerException
			try {
				 plateNumberFromCapcare = carService.getCarByDeviceSN(sn).getPlateNumber();
			} catch (NullPointerException e) {
				continue;
			}
			float lng = Float.valueOf(devices.getJSONObject(i).getJSONObject("position").get("lng").toString()).floatValue();
			float lat = Float.valueOf(devices.getJSONObject(i).getJSONObject("position").get("lat").toString()).floatValue();
			String mode = devices.getJSONObject(i).getJSONObject("position").get("mode").toString();
			String time = devices.getJSONObject(i).getJSONObject("position").get("receive").toString();
			
			if(capcareMap.containsKey(plateNumberFromCapcare)){
				if(lng>0 && lat>0 && "A".equals(mode) 
						&& (Long.parseLong(time) >= Long.parseLong(capcareMap.get(plateNumberFromCapcare).getLastTime())))
					notNullDevices.add(devices.getJSONObject(i));
			}else{
				if(lng>0 && lat>0 && "A".equals(mode)){
					notNullDevices.add(devices.getJSONObject(i));
					capcareMessage = new CapcareMessage();
					capcareMessage.setPlateNumber(plateNumberFromCapcare);
					capcareMessage.setLongitude("");
					capcareMessage.setLatitude("");
					capcareMessage.setSpeed("");
					capcareMessage.setStatus("");
					capcareMessage.setDirection("");
					capcareMessage.setLastTime("");
					Car car=carService.getCarByPlateNumber(plateNumberFromCapcare);
					capcareMessage.setCompany(car.getCompany());
					capcareMessageDao.save(capcareMessage);

					capcareMap.put(plateNumberFromCapcare, capcareMessage);
					
				}else{
					capcareMessage = new CapcareMessage();
					capcareMessage.setPlateNumber(plateNumberFromCapcare);
					capcareMessage.setLongitude("113.932132");
					capcareMessage.setLatitude("22.637883");
					capcareMessage.setSpeed("0");
					capcareMessage.setStatus("2");
					capcareMessage.setDirection("0");
					capcareMessage.setLastTime(time);
					Car car=carService.getCarByPlateNumber(plateNumberFromCapcare);
					capcareMessage.setCompany(car.getCompany());
					capcareMessageDao.save(capcareMessage);

					capcareMap.put(plateNumberFromCapcare, capcareMessage);
				}					
			}			
		}

		int positionNum = notNullDevices.size(); 
		int groups = positionNum/limitLen + 1;//组数
		int lastGroupNum = positionNum%limitLen;//最后一组的个数
		//请求百度API进行坐标转换,因为坐标转换每次最多100个，所以需要分组
		
		for(int i=0;i<groups;i++){
			String positionStr = "";
			if(i != groups-1){						//不是最后一组
				for(int j=0;j<limitLen;j++){
						if(j != 0){
							positionStr +=";";
						}
						positionStr = positionStr +notNullDevices.getJSONObject(i*limitLen+j).getJSONObject("position").get("lng").toString()+","+
								notNullDevices.getJSONObject(i*limitLen+j).getJSONObject("position").get("lat").toString();
				}
			}else{									//最后一组特别处理
				for(int j=0;j<lastGroupNum;j++){
						if(j != 0 ){
							positionStr +=";";
						}
						positionStr = positionStr +notNullDevices.getJSONObject(i*limitLen+j).getJSONObject("position").get("lng").toString()+","+
								notNullDevices.getJSONObject(i*limitLen+j).getJSONObject("position").get("lat").toString();
					}
			}
			//再对每一组进行坐标转换
			GetBaiduMsg gbm = new GetBaiduMsg();
			JSONObject baiduJasonObject = JSONObject.fromObject(gbm.excute(positionStr));
			JSONArray baiduPositionArray = baiduJasonObject.getJSONArray("result");
			int len = baiduPositionArray.size();
			//再对每一组数据进行处理
			for(int k=0;k<len;k++){
				//这是凯步以及经过坐标转换后返回的数据
				String plateNumber = carService.getCarByDeviceSN(notNullDevices.getJSONObject(i*limitLen+k).getString("sn")).getPlateNumber();
				String longitude = baiduPositionArray.getJSONObject(k).get("x").toString();
				String latitude = baiduPositionArray.getJSONObject(k).get("y").toString();
				String status = notNullDevices.getJSONObject(i*limitLen+k).getJSONObject("position").get("status").toString();
				String speed = notNullDevices.getJSONObject(i*limitLen+k).getJSONObject("position").get("speed").toString();
				String direction = notNullDevices.getJSONObject(i*limitLen+k).getJSONObject("position").get("direction").toString();
				String lastTime = notNullDevices.getJSONObject(i*limitLen+k).getJSONObject("position").getString("receive");
				capcareMessage = capcareMessageDao.getCapcareMessageByPlateNumber(plateNumber);
				capcareMessage.setLongitude(longitude);
				capcareMessage.setLatitude(latitude);
				capcareMessage.setSpeed(speed);
				capcareMessage.setStatus(status);
				capcareMessage.setDirection(direction);
				capcareMessage.setLastTime(lastTime);
				capcareMessageDao.update(capcareMessage);
				capcareMap.put(plateNumber, capcareMessage);
			}
		}
	}

	/**
	 * 那些由于车牌变更，或者在凯步关爱中取消注册的车辆，会依然占据数据库和内存map。
	 * 所以需要写一个定时任务，每晚0点运行。
	 * 反向来查：如果在map中，但是不在凯步平台返回数据中的车牌位置信息，就要从map和数据库中删除掉
	 */
	@Transactional
	public void removeCapcareMessage() {
		
		//获取凯步返回的所有车辆的车牌号
		GetCapcareMsg gcm = new GetCapcareMsg();
		String capcareMessageString = gcm.excute();
		JSONObject jsonObject = JSONObject.fromObject(capcareMessageString);
		JSONArray devices = jsonObject.getJSONArray("devices");
		List<String> plateNumberList = new ArrayList<String>();
		for(int i=0;i<devices.size();i++){
			String plateNumber = carService.getCarByDeviceSN(devices.getJSONObject(i).getString("sn")).getPlateNumber();
			plateNumberList.add(plateNumber);
		}
		
		List<String> keyList = new ArrayList<String>(capcareMap.keySet());
		
		for(String plateNumber : keyList){
			if( !plateNumberList.contains(plateNumber)){
				System.out.println("执行删除车辆:"+plateNumber);
				capcareMap.remove(plateNumber);
				capcareMessageDao.delete(capcareMessageDao.getCapcareMessageByPlateNumber(plateNumber).getId());
			}
		}
		
		
	}		

}
