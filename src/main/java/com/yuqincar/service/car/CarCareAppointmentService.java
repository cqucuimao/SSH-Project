package com.yuqincar.service.car;

import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarCareAppointment;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.utils.QueryHelper;

public interface CarCareAppointmentService {
	
	public void saveCarCareAppointment(CarCareAppointment carCareAppointment);
	
	public CarCareAppointment getCarCareAppointmentById(Long id);
			
	public PageBean<CarCareAppointment> queryCarCareAppointment(int pageNum , QueryHelper helper);
		
	public void updateCarCareAppointment(CarCareAppointment carCareAppointment,int mileInterval);
	
	public boolean isExistAppointment(long selfId,Car car);
	
	public void deleteCarCareAppointmentById(long id);
		
}
