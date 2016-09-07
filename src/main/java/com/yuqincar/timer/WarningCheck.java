package com.yuqincar.timer;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.monitor.WarningMessageTypeEnum;
import com.yuqincar.domain.order.Order;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.monitor.WarningMessageService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.utils.HttpMethod;

@Component
public class WarningCheck {
	private static final String PULL_OUT_WARNING_URL="http://api.capcare.com.cn:1045/api/alarm.all.do?begin=-1&end=-1&token=FCD037A9-56FF-4962-9B63-8CFA860840C5&user_id=45036&app_name=M2616_BD&language=zh_CN&_=1450765409539";
	private static final String DELETE_PULL_OUT_WARNING_URL="http://api2.capcare.com.cn:1045/api/alarm.delete.do?alarm_id=%s-%s&app_name=M2616_BD&language=zh_CN&token=FCD037A9-56FF-4962-9B63-8CFA860840C5&user_id=45036&_=1460880585103";
	private static final String UNPLANNED_RUNNING_URL = "http://api.capcare.com.cn:1045/api/device.get.do?device_sn=%s&token=FCD037A9-56FF-4962-9B63-8CFA860840C5&user_id=45036&app_name=M2616_BD";
	
	@Autowired
	public CarService carService;
	@Autowired
	public WarningMessageService warningService;
	@Autowired
	public OrderService orderService;
	
	@Scheduled(cron = "10 * * * * ?")  //每分钟（第10秒）执行一次
	@Transactional
	public void checkPullOutWarning(){
		System.out.println("in checkPullOutWarning");
		String json = HttpMethod.get(PULL_OUT_WARNING_URL);
		JSONObject result=JSON.parseObject(json);
		JSONArray alarms=result.getJSONArray("alarms");
		if(alarms!=null && alarms.size()>0){
			for(int i=0;i<alarms.size();i++){
				String sn=alarms.getJSONObject(i).getString("deviceSn");
				if("拔出报警".equals(alarms.getJSONObject(i).getString("info"))){
					Date date=new Date(Long.valueOf(alarms.getJSONObject(i).getString("time")));
					warningService.addWarningMessage(carService.getCarByDeviceSN(sn).getId(), date, WarningMessageTypeEnum.PULLEDOUT);
				}
				//删除报警
				String warningId=alarms.getJSONObject(i).getString("id");
				String url=String.format(DELETE_PULL_OUT_WARNING_URL, sn,warningId);
				String re = HttpMethod.get(url.toString());
			}
		}
	}
	
	//暂时取消非计划形式的报警。因为不好区分出正常情况。
	//@Scheduled(cron = "0 0/1 * * * ?") // 每5分钟执行一次
	@Transactional
	public void checkUnplannedRunningWarning(){
		List<Car> cars=carService.getCarsForMonitoring();
		for(Car car:cars){
			if(car.getDevice()==null || StringUtils.isEmpty(car.getDevice().getSN()))
				continue;
			String url=String.format(UNPLANNED_RUNNING_URL, car.getDevice().getSN());
			String json = HttpMethod.get(url.toString());
			JSONObject result=JSON.parseObject(json);
			if(result==null)
				continue;
			JSONObject device=result.getJSONObject("device");
			if(device==null)
				continue;
			JSONObject position=device.getJSONObject("position");
			if(position==null)
				continue;
		    String status=position.getString("status");
		    if(status==null)
		    	continue;
			String speed=position.getString("speed");
			if(speed==null)
				continue;
			//如果车辆处于行驶状态  行驶1 速度>0
			if("1".equals(status) && Double.valueOf(speed)>0){
				//查看该行驶车辆是否有订单
				Order order=orderService.getCurrentOrderByCarId(car.getId());
				//订单为空  这说明该车是处于 异常行驶状态 或者 临时异常行驶状态   二次验证
				if(order==null){
					//如果当前警告记录存在 第一次验证的临时警告记录 则删除临时警告 并插入真实警告
					if(warningService.isTempMessageExist(car.getId())){
					   warningService.deleteTempMessage(car.getId());
					   warningService.addWarningMessage(car.getId(), new Date(), WarningMessageTypeEnum.UNPLANNED_RUNNING);
					}else{
					   //否则插入临时警告
					   warningService.addTempWarningMessage(car.getId());
					}
				}else{
					//如果上次有临时警告，本次没有出现，那么就应该删除临时警告
					if(warningService.isTempMessageExist(car.getId()))
						warningService.deleteTempMessage(car.getId());
				}
			}
		}
	}
}
