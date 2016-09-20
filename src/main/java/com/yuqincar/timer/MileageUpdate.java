package com.yuqincar.timer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.lbs.LBSDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarCareAppointment;
import com.yuqincar.domain.car.CarStatusEnum;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.car.CarCareAppointmentService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.service.sms.SMSService;
import com.yuqincar.utils.DateUtils;

@Component
public class MileageUpdate {

	@Autowired
	public CarService carService;
	
	@Autowired
	public OrderService orderService;
	
	@Autowired
	public UserService userService;
	
	@Autowired
	public SMSService smsService;
	
	@Autowired
	public CarCareAppointmentService carCareAppointmentService;

	@Autowired
	public LBSDao lbsDao;

	@Scheduled(cron = "0 0 1 * * ?")
	@Transactional
	public void update() {
		List<Car> cars = carService.getAll();
		for(Car car : cars) {
			if(car.getStatus()==CarStatusEnum.SCRAPPED || car.isBorrowed())
				continue;
			if(car.getDevice()==null || StringUtils.isEmpty(car.getDevice().getSN()))
				continue;
			
			int mile = (int) lbsDao.getCurrentMile(car);
			car.setMileage(mile);
			
			//判断是否保养过期，如果car.getNextCareMile==0，说明该车的数据不全，不处理。
			if(car.getNextCareMile()>0 && car.getMileage()>car.getNextCareMile()){
				System.out.println(car.getPlateNumber()+"保养里程过期。 "+mile+">"+car.getNextCareMile());
				car.setCareExpired(true);
								
				if(car.getDriver()!=null){
					CarCareAppointment cca=new CarCareAppointment();
					cca.setCar(car);
					cca.setDriver(car.getDriver());						
					int n=0;
					Date date=null;
					while(true){
						date=DateUtils.getOffsetDateFromNow(n);
						List<List<Order>> carOrderList=orderService.getCarTask(car, date, date);
						List<List<Order>> driverOrderList=null;
						if(cca.getDriver()!=null)
							driverOrderList=orderService.getDriverTask(cca.getDriver(), date, date);
						if((carOrderList==null || carOrderList.size()==0 || carOrderList.get(0)==null || carOrderList.get(0).size()==0)
								&& (driverOrderList==null || driverOrderList.size()==0 || driverOrderList.get(0)==null || driverOrderList.get(0).size()==0)){
							break;
						}else
							n++;
					}
					cca.setDate(date);
					cca.setDone(false);
					//保存预约信息时，已经给司机发送短信了。
					carCareAppointmentService.saveCarCareAppointment(cca);
					
					Map<String,String> params=new HashMap<String,String>();
					params.put("plateNumber", car.getPlateNumber());
					params.put("date", DateUtils.getYMDString(date));
					for(User manager:userService.getUserByRoleName("车辆保养管理员"))
						smsService.sendTemplateSMS(manager.getPhoneNumber(), SMSService.SMS_TEMPLATE_CARCARE_APPOINTMENT_GENERATED_FOR_MANAGER, params);
				}else{
					Map<String,String> params=new HashMap<String,String>();
					params.put("plateNumber", car.getPlateNumber());
					for(User manager:userService.getUserByRoleName("车辆保养管理员"))
						smsService.sendTemplateSMS(manager.getPhoneNumber(), SMSService.SMS_TEMPLATE_CARCARE_APPOINTMENT_GENERATED_NO_DRIVER, params);
				}
			}
			carService.updateCar(car);
		}
	}
}
