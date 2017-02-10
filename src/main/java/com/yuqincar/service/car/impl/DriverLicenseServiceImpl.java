package com.yuqincar.service.car.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.car.DriverLicenseDao;
import com.yuqincar.domain.car.DriverLicense;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.ProtocolOrderPayOrder;
import com.yuqincar.service.car.DriverLicenseService;
import com.yuqincar.utils.QueryHelper;

@Service
public class DriverLicenseServiceImpl implements DriverLicenseService {

	@Autowired
	private DriverLicenseDao driverLicenseDao;
	
	public List<DriverLicense> getAllDriverLicenses(){
		return driverLicenseDao.getAll();
	}
	
	public PageBean<DriverLicense> queryDriverLicense(int pageNum , QueryHelper helper)
	{
		return driverLicenseDao.getPageBean(pageNum, helper);
	}
	
	public DriverLicense getDriverLicenseById(Long id) {
		return driverLicenseDao.getById(id);
	}
	
	@Transactional
	public void updateDriverLicense(DriverLicense dl) {
		driverLicenseDao.update(dl);
	}
}

