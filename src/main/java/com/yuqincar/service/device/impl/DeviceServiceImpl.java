package com.yuqincar.service.device.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.monitor.DeviceDao;
import com.yuqincar.domain.monitor.Device;
import com.yuqincar.service.device.DeviceService;

@Service
public class DeviceServiceImpl implements DeviceService {

	@Autowired
	private DeviceDao deviceDao;

	public Device getDeviceById(long id) {
		return deviceDao.getById(id);
	}

	@Transactional
	public void update(Device device) {
		deviceDao.update(device);
	}
	
}
