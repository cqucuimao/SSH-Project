package com.yuqincar.service.monitor.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		//每次查询一辆车位置信息的url： http://api.capcare.com.cn:1045/api/device.get.do?device_sn=892626060704080&timestamp=1481097540000&token=FCD037A9-56FF-4962-9B63-8CFA860840C5&user_id=45036&app_name=M2616_BD
		
		String beginUrl = "http://api.capcare.com.cn:1045/api/device.get.do?";
		
		String endUrl = "&token=FCD037A9-56FF-4962-9B63-8CFA860840C5&user_id=45036&app_name=M2616_BD";
		
		//获取所有监控车辆
		List<Car> allMonitorCar = carService.getCarsForMonitoring();
		
		//获得所有监控车辆的device_sn
		List<String> deviceSn = new ArrayList<String>();
		for(Car car : allMonitorCar){
			deviceSn.add(car.getDevice().getSN());
		}
		
		//根据设备号和时间戳，获取每辆车的位置信息。如果返回信息的mode不是A，时间倒退一周再查询一次。如果倒退到2016-9-1还没找到mode为A的位置信息，就把当前的位置作为初始化位置。
		for(String sn : deviceSn){
			
			String dateStr = "2016-9-1 00:00";  
			long longDateOf20160901 = DateUtils.getYMDHM(dateStr).getTime();
			
			Date now = new Date();
			long longDateOfNow = now.getTime();
			
			String url = beginUrl+"device_sn="+sn+"&timestamp="+String.valueOf(longDateOfNow)+endUrl;
			//访问凯步关爱
			System.out.println(url);
			String capcareData = HttpMethod.get(url);
			System.out.println("capcareData="+capcareData);
			JSONObject jsonObject = JSONObject.fromObject(capcareData);
			JSONObject device = jsonObject.getJSONObject("device");
			JSONObject position = device.getJSONObject("position");
			String plateNumber = device.get("name").toString();
			String longitude = position.get("lng").toString();
			String latitude = position.get("lat").toString();
			String speed = position.get("speed").toString();
			String status = position.get("status").toString();
			String direction = position.get("direction").toString();
			
			GetBaiduMsg gbm = new GetBaiduMsg();
			//如果mode不是A，时间倒退一周再查询一次
			while(!position.get("mode").toString().equals("A") && longDateOfNow > longDateOf20160901){
				longDateOfNow  = longDateOfNow - 604800;
				System.out.println(longDateOfNow);
				url = beginUrl+"device_sn="+sn+"&timestamp="+String.valueOf(longDateOfNow)+endUrl;
				
				jsonObject = JSONObject.fromObject(capcareData);
				device = jsonObject.getJSONObject("device");
				position = device.getJSONObject("position");
				
				plateNumber = device.get("name").toString();
				longitude = position.get("lng").toString();
				latitude = position.get("lat").toString();
				speed = position.get("speed").toString();
				status = position.get("status").toString();
				direction = position.get("direction").toString();
			}
			//到百度去进行坐标转换，坐标为0时，初始化位置为出厂位置。并且数据初始化到数据库
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
		
		int limitLen = 100;
			
		//考虑到服务器重启后的第一次访问，内存中是没有位置信息的，如果map.size()==0,那么就从数据库的CapcareMessage实体中获取位置来初始化map。
		if(capcareMap.size() == 0){
			List<CapcareMessage> capcareMessages = capcareMessageDao.getAll();
			for(CapcareMessage capcareMessage : capcareMessages){
				capcareMap.put(capcareMessage.getPlateNumber(), capcareMessage);
			}
		}
		
		//这两行代码是到凯步平台获取全部监控车辆的位置信息，返回Json数据
		GetCapcareMsg gcm = new GetCapcareMsg();
		String capcareMessageString = gcm.excute();
		
		JSONObject jsonObject = JSONObject.fromObject(capcareMessageString);
		JSONArray devices = jsonObject.getJSONArray("devices");
		JSONArray notNullDevices = new JSONArray();
		//需要访问百度API进行坐标转换，每次最多100个坐标
		int sum = devices.size();
		//去除其中坐标为0的元素  && 去除mode不为A的元素
		for(int i=0;i<sum;i++){
			String lng = devices.getJSONObject(i).getJSONObject("position").get("lng").toString();
			String lat = devices.getJSONObject(i).getJSONObject("position").get("lat").toString();
			String mode = devices.getJSONObject(i).getJSONObject("position").get("mode").toString();
			//过滤掉坐标是0以及mode不是A的信息
			if(! lng.equals("0.0") && ! lat.equals("0.0") && !mode.equals("A")){
				notNullDevices.add(devices.getJSONObject(i));
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
					String plateNumber = notNullDevices.getJSONObject(i*len+k).get("name").toString();
					String longitude = baiduPositionArray.getJSONObject(k).get("x").toString();
					String latitude = baiduPositionArray.getJSONObject(k).get("y").toString();
					String status = notNullDevices.getJSONObject(i*len+k).getJSONObject("position").get("status").toString();
					String speed = notNullDevices.getJSONObject(i*len+k).getJSONObject("position").get("speed").toString();
					String direction = notNullDevices.getJSONObject(i*len+k).getJSONObject("position").get("direction").toString();
					CapcareMessage capcareMessage;
					//如果凯步平台返回的plateNumber在capcareMap中，更新数据库中的值
					if(capcareMap.containsKey(plateNumber)){
						
						capcareMessage = capcareMessageDao.getCapcareMessageByPlateNumber(plateNumber);
						capcareMessage.setPlateNumber(plateNumber);
						capcareMessage.setLongitude(longitude);
						capcareMessage.setLatitude(latitude);
						capcareMessage.setSpeed(speed);
						capcareMessage.setStatus(status);
						capcareMessage.setDirection(direction);
						capcareMessageDao.update(capcareMessage);
					
					}else{//如果凯步平台返回的plateNumber不在capcareMap中，将数据插入到数据库中	
						capcareMessage = new CapcareMessage();
						capcareMessage.setPlateNumber(plateNumber);
						capcareMessage.setLongitude(longitude);
						capcareMessage.setLatitude(latitude);
						capcareMessage.setSpeed(speed);
						capcareMessage.setStatus(status);
						capcareMessage.setDirection(direction);
						capcareMessageDao.save(capcareMessage);
					}
				
					//更新capcareMap的值
					capcareMap.put(plateNumber, capcareMessage);
			}
		}
	}		

}
