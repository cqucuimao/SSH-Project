/**
 * University Of Chongqing.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.yuqincar.service.CustomerOrganization.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.CustomerOrganization.CustomerOrganizationDao;
import com.yuqincar.dao.monitor.LocationDao;
import com.yuqincar.dao.order.PriceTableDao;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.monitor.Location;
import com.yuqincar.domain.order.CustomerOrganization;
import com.yuqincar.service.CustomerOrganization.CustomerOrganizationService;
import com.yuqincar.utils.QueryHelper;

/**
 * CustomerOrganizationServiceImpl
 * 
 * @author wanglei
 * @version $Id: CustomerOrganizationServiceImpl.java, v 0.1 2016年1月8日 下午4:12:45
 *          wanglei Exp $
 */
@Service
public class CustomerOrganizationServiceImpl implements
		CustomerOrganizationService {

	@Autowired
	private CustomerOrganizationDao customerOrganizationDao;
	
	@Autowired
	private PriceTableDao priceTableDao;
	
	@Autowired
	private LocationDao locationDao;

	public PageBean<CustomerOrganization> queryCustomerOrganizationByKeyword(String keyword) {
		return customerOrganizationDao.queryCustomerOrganizationByKeyword(keyword);
	}

	public CustomerOrganization getById(Long id) {
		return customerOrganizationDao.getById(id);
	}

	@Transactional
	public void saveCustomerOrganization(
			CustomerOrganization customerOrganization) {
		customerOrganization.setPriceTable(priceTableDao.getDefaultPriceTable());
		customerOrganizationDao.save(customerOrganization);
	}

	@Transactional
	public void updateCustomerOrganization(
			CustomerOrganization customerOrganization) {
		customerOrganizationDao.update(customerOrganization);
	}

	public boolean canDeleteCustomerOrganization(long customerOrganizationId) {
		return customerOrganizationDao
				.canDeleteCustomerOrganization(customerOrganizationId);
	}

	@Transactional
	public void deleteCustomerOrganization(long customerOrganizationId) {
		customerOrganizationDao.delete(customerOrganizationId);
	}

	public PageBean<CustomerOrganization> getPageBean(int pageNum,
			QueryHelper helper) {
		return customerOrganizationDao.getPageBean(pageNum, helper);
	}

	public PageBean<CustomerOrganization> queryCustomerOrganization(
			int pageNum, QueryHelper helper) {
		return customerOrganizationDao.getPageBean(pageNum, helper);
	}

	public boolean isNameExist(long selfId, String name) {
		return customerOrganizationDao.isNameExist(selfId, name);
	}

	public boolean isAbbreviationExist(long selfId, String abbreviation) {
		return customerOrganizationDao
				.isAbbreviationExist(selfId, abbreviation);
	}

	public CustomerOrganization getCustomerOrganizationByName(String name) {
		return customerOrganizationDao.getByName(name);
	}

	@Transactional
	public void saveLocation(Location location) {
		locationDao.save(location);
	}
}
