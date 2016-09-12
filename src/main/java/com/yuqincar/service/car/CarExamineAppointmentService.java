package com.yuqincar.service.car;

import com.yuqincar.domain.car.CarExamineAppointment;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.utils.QueryHelper;

public interface CarExamineAppointmentService {
	public void saveCarExamineAppointment(CarExamineAppointment carExamineAppointment);
	
	public CarExamineAppointment getCarExamineAppointmentById(long id);
	
	public PageBean<CarExamineAppointment> queryCarExamineAppointment(int pageNum , QueryHelper helper);
	
	public void updateCarExamineAppointment(CarExamineAppointment carExamineAppointment);
	
}
