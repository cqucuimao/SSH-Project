package com.yuqincar.service.car;

import java.util.List;

import com.yuqincar.domain.car.DriverLicense;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.ProtocolOrderPayOrder;
import com.yuqincar.service.base.BaseService;
import com.yuqincar.utils.QueryHelper;

public interface DriverLicenseService  extends BaseService{ 

	 public List<DriverLicense> getAllDriverLicenses();
	    
	 public PageBean<DriverLicense> queryDriverLicense(int pageNum , QueryHelper helper);
	 
	 public DriverLicense getDriverLicenseById(Long id);
	 
	 public void updateDriverLicense(DriverLicense dl);
}
