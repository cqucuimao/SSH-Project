/**
 * University Of Chongqing.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.yuqincar.dao.customer;

import java.util.List;

import com.yuqincar.dao.common.BaseDao;
import com.yuqincar.domain.order.Customer;

public interface CustomerDao extends BaseDao<Customer> {

	public boolean canDeleteCustomer(long customerId);

	public Customer getCustomerByNameAndOrganization(String customerName,String customerOrganizationName);
	
	public Customer getCustomerByPhoneNumber(String phoneNumber);
	
	public List<Customer> getAllCustomerByOrganization(long customerOrganizationId);
}
