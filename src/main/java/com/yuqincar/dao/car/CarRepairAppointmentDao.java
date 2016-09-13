package com.yuqincar.dao.car;

import com.yuqincar.dao.common.BaseDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarRepairAppointment;

public interface CarRepairAppointmentDao extends BaseDao<CarRepairAppointment> {
	public boolean isExistAppointment(long selfId,Car car);

}
