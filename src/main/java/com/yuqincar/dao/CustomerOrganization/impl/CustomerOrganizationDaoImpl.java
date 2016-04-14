/**
 * University Of Chongqing.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.yuqincar.dao.CustomerOrganization.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.yuqincar.dao.CustomerOrganization.CustomerOrganizationDao;
import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.monitor.Location;
import com.yuqincar.domain.order.Customer;
import com.yuqincar.domain.order.CustomerOrganization;
import com.yuqincar.domain.order.Order;
import com.yuqincar.utils.Configuration;

@Repository
public class CustomerOrganizationDaoImpl extends
		BaseDaoImpl<CustomerOrganization> implements CustomerOrganizationDao {
	@SuppressWarnings("rawtypes")
	public PageBean<CustomerOrganization> queryCustomerOrganizationByKeyword(String keyword) {

		/*
		 * 1、先查简称的模糊结果 2、再查全称的模糊结果
		 */
		// 获取pageSize等信息
		int pageSize = Configuration.getPageSize();
		String hql = "from CustomerOrganization where abbreviation like :abbreviation or name like :name";
		Query query = getSession().createQuery(hql);
		query.setString("abbreviation", "%" + keyword + "%");
		query.setString("name", "%" + keyword + "%");
		List customerOrganizationList = query.list();
		return new PageBean(1, pageSize, customerOrganizationList.size(),
				customerOrganizationList);
	}

	public CustomerOrganization getById(Long id) {
		return (CustomerOrganization) getSession().get(
				CustomerOrganization.class, id);
	}

	public CustomerOrganization getByName(String name) {
		return (CustomerOrganization) getSession().createQuery(//
				"FROM CustomerOrganization d WHERE d.name=?")
				.setParameter(0, name).uniqueResult();
	}

	public boolean canDeleteCustomerOrganization(long customerOrganizationId) {
		List<Order> orders = getSession()
				.createQuery("from order_ as o where o.customerOrganization.id=?")
				.setParameter(0, customerOrganizationId).list();
		System.out.println("size="+orders.size());
		
		List<Customer> customers=getSession().createQuery("from Customer as c where c.customerOrganization.id=?")
				.setParameter(0, customerOrganizationId).list();
		if (orders.size() != 0 || customers.size()!=0)
			return false;
		return true;
	}

	public boolean isNameExist(long selfId, String name) {
		if (name == null || name.equals("")) {
			return false;
		}

		List<CustomerOrganization> customerOrganizations = getSession()
				.createQuery("from CustomerOrganization co where co.name=? and co.id<>?")
				.setParameter(0, name)
				.setParameter(1, selfId)
				.list();
		if (customerOrganizations.size() != 0)
			return true;
		return false;
	}

	public boolean isAbbreviationExist(long selfId, String abbreviation) {
		if (abbreviation == null || abbreviation.equals("")) {
			return false;
		}

		List<CustomerOrganization> customerOrganizations = getSession()
				.createQuery(
						"from CustomerOrganization co where co.abbreviation=?")
				.setParameter(0, abbreviation)
				.list();
		if (customerOrganizations.size() != 0)
			return true;
		return false;
	}

	public void saveLocation(Location location) {
		getSession().save(location);
	}

	public CustomerOrganization getByAbbreviation(String abbreviation) {
		return (CustomerOrganization) getSession()
				.createQuery(
						"from CustomerOrganization co WHERE co.abbreviation=?")
				.setParameter(0, abbreviation).uniqueResult();
	}

}
