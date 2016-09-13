package com.yuqincar.service.car;

import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarRepairAppointment;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.utils.QueryHelper;

public interface CarRepairAppointmentService {
	public void saveCarRepairAppointment(CarRepairAppointment carRepairAppointment);
	
	public CarRepairAppointment getCarRepairAppointmentById(long id);
	
	public PageBean<CarRepairAppointment> queryCarRepairAppointment(int pageNum , QueryHelper helper);
	
	public void updateCarRepairAppointment(CarRepairAppointment carRepairAppointment);
	
	public boolean isExistAppointment(long selfId,Car car);
	
	public void deleteCarRepairAppointmentById(long id);
}
