package com.yuqincar.timer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.monitor.TemporaryWarning;
import com.yuqincar.domain.monitor.WarningMessageTypeEnum;
import com.yuqincar.domain.order.Order;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.monitor.WarningMessageService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.utils.HttpMethod;

@Component
public class WarningCheckEveryMinutes {
	
	private static final String BASEURL = "http://api.capcare.com.cn:1045/api/";
	private static final String ENDURL = "&token=FCD037A9-56FF-4962-9B63-8CFA860840C5&user_id=45036&app_name=M2616_BD";
	
	@Autowired
	public CarService carService;
	@Autowired
	public WarningMessageService warningService;
	@Autowired
	public OrderService orderService;
	
	@Scheduled(cron = "0 0/5 * * * ?") // 每5分钟执行一次
	@Transactional
	public void checkWarning(){
		List<Car> cars=carService.getAllNormalCars();
		for(int i=0;i<cars.size();i++){
			Long carId=cars.get(i).getId();
			String device_sn=cars.get(i).getDevice().getSN();
			StringBuffer deviceUrl=new StringBuffer();
			deviceUrl.append(BASEURL);
			deviceUrl.append("device.get.do?device_sn="+device_sn);
			deviceUrl.append(ENDURL);
			String json = HttpMethod.get(deviceUrl.toString());
			//System.out.println("获取得到设备数据信息");
			//System.out.println(json);
			JSONObject result=JSON.parseObject(json);
			//判断是否是  拔出报警，如果是  拔出报警，则直接将警告记录插入到数据库，否则判断是否是  异常行驶
			try{
				JSONArray alarms=result.getJSONArray("alarms");
				if("拔出报警".equals(alarms.getJSONObject(0).getString("info"))){
					warningService.addWarningMessage(carId, WarningMessageTypeEnum.PULLEDOUT);
				}
			}catch(Exception e){
				JSONObject position=result.getJSONObject("device").getJSONObject("position");
			    String status=position.getString("status");
				String speed=position.getString("speed");
				//System.out.println(status);
				//System.out.println(speed);
				//如果车辆处于行驶状态  行驶1 速度!=0
				if("1".equals(status)&&!"0.0".equals(speed)){
					//查看该行驶车辆是否有订单
					Order order=orderService.getCurrentOrderByCarId(carId);
					//订单为空  这说明该车是处于 异常行驶状态 或者 临时异常行驶状态   二次验证
					if(order==null){
						//如果当前警告记录存在 第一次验证的临时警告记录 则删除临时警告 并插入真实警告
						if(warningService.isTempMessageExist(carId)){
						   warningService.deleteTempMessage(carId);
						   warningService.addWarningMessage(carId, WarningMessageTypeEnum.UNPLANNED_RUNNING);
						}else{
						   //否则插入临时警告
						   warningService.addTempWarningMessage(carId);
						}
					}
				}
			}
		}
	}

}
