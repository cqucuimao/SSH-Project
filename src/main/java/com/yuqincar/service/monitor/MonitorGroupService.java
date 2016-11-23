package com.yuqincar.service.monitor;

import java.util.List;

import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.monitor.MonitorGroup;
import com.yuqincar.service.base.BaseService;

public interface MonitorGroupService extends BaseService{
	
	public void delete(Long id);
	
	public void save(MonitorGroup monitorGroup);
	
	public void update(MonitorGroup monitorGroup);
	
	public List<MonitorGroup> getAll();
	
	public MonitorGroup getById(Long id);
	
	public List<Car> sortCarByPlateNumber();
}
