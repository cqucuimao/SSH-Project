package com.yuqincar.service.device;

import com.yuqincar.domain.monitor.Device;
import com.yuqincar.service.base.BaseService;

public interface DeviceService extends BaseService {
	
	public Device getDeviceById(long id);
	
	public void update(Device device);
}
