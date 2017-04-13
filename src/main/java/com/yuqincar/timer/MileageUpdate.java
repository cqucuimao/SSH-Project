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
import com.yuqincar.service.businessParameter.BusinessParameterService;
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
	
	@Autowired
	public BusinessParameterService businessParameterService;

	@Scheduled(cron = "0 0 8 * * ?")
	@Transactional
	public void update() {
		List<Car> cars = carService.getAll();
		StringBuffer plateNumber4Manager=new StringBuffer();
		StringBuffer plateNumber4NoDriver=new StringBuffer();
		StringBuffer plateNumber44SEmployee=new StringBuffer();
		for(Car car : cars) {
			if(car.getStatus()==CarStatusEnum.SCRAPPED || car.isBorrowed())
				continue;
			if(car.getDevice()==null || StringUtils.isEmpty(car.getDevice().getSN()))
				continue;
			
			int mile = (int) lbsDao.getCurrentMile(car);
			car.setMileage(mile);
			
			//要到保养里程前，提前通知司机
			if(car.getDriver()!=null && !car.isCareExpired() && car.getNextCareMile()>0 && (car.getNextCareMile()-businessParameterService.getBusinessParameter().getMileageForCarCareRemind()<car.getMileage() && car.getMileage()<=car.getNextCareMile())){
				Map<String,String> params=new HashMap<String,String>();
				params.put("plateNumber", car.getPlateNumber());
				String mileage = businessParameterService.getBusinessParameter().getMileageForCarCareRemind()+"";
				params.put("mileage", mileage);
				smsService.sendTemplateSMS(car.getDriver().getPhoneNumber(), SMSService.SMS_TEMPLATE_CARCARE_NEARBY, params);
			}
			
			//判断是否保养过期，如果car.getNextCareMile==0，说明该车的数据不全，不处理。
			if(!car.isCareExpired() && car.getNextCareMile()>0 && car.getMileage()>car.getNextCareMile()){
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
					
					if(plateNumber4Manager.length()>0)
						plateNumber4Manager.append("，");
					plateNumber4Manager.append(car.getPlateNumber()).append("（").append(DateUtils.getYMDString(date)).append("）");
					
					if(plateNumber44SEmployee.length()>0)
						plateNumber44SEmployee.append("，");
					plateNumber44SEmployee.append(car.getPlateNumber()).append("（").append(DateUtils.getYMDString(date)).append("）");
				}else{
					if(plateNumber4NoDriver.length()>0)
						plateNumber4NoDriver.append("，");
					plateNumber4NoDriver.append(car.getPlateNumber());
				}
			}
			carService.updateCar(car);
		}
		
		Map<String,String> params=new HashMap<String,String>();
		
		if(plateNumber4Manager.length()>0){
			params.put("plateNumber", plateNumber4Manager.toString());
			for(User managerForCarCareAppointment:businessParameterService.getBusinessParameter().getEmployeesForCarCareAppointmentSMS())
				smsService.sendTemplateSMS(managerForCarCareAppointment.getPhoneNumber(), SMSService.SMS_TEMPLATE_CARCARE_APPOINTMENT_GENERATED_FOR_MANAGER, params);
			params.clear();
		}
		
		if(plateNumber44SEmployee.length()>0){
			params.put("plateNumber", plateNumber44SEmployee.toString());
			for(User in4SUser:businessParameterService.getBusinessParameter().getEmployeesIn4SForSMS()){
				smsService.sendTemplateSMS(in4SUser.getPhoneNumber(), SMSService.SMS_TEMPLATE_CARCARE_APPOINTMENT_GENERATED_FOR_4S_EMPLOYEE, params);
			}
			params.clear();
		}
			
		if(plateNumber4NoDriver.length()>0){
			params.put("plateNumber", plateNumber4NoDriver.toString());
			for(User managerForCarCareAppointment:businessParameterService.getBusinessParameter().getEmployeesForCarCareAppointmentSMS())
				smsService.sendTemplateSMS(managerForCarCareAppointment.getPhoneNumber(), SMSService.SMS_TEMPLATE_CARCARE_APPOINTMENT_GENERATED_NO_DRIVER, params);
		}
	}
}
