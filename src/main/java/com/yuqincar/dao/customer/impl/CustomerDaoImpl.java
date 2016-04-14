/**
 * University Of Chongqing.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.yuqincar.dao.customer.impl;

import java.util.List;

import org.hibernate.hql.ast.tree.FromElement;
import org.springframework.stereotype.Repository;

import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.dao.customer.CustomerDao;
import com.yuqincar.domain.order.Customer;
import com.yuqincar.domain.order.Order;

/**
 * CustomerDaoImpl
 * @author wanglei
 * @version $Id: CustomerDaoImpl.java, v 0.1 2016年1月8日 下午4:02:09 wanglei Exp $
 */
@Repository
public class CustomerDaoImpl extends BaseDaoImpl<Customer>implements CustomerDao {
	
	private CustomerDao customerDao;

    /** 
     * @see com.yuqincar.dao.common.BaseDao#getById(java.lang.Long)
     */
	
    public Customer getById(Long id) {
        return (Customer) getSession().get(Customer.class, id);
    }
    
	public boolean canDeleteCustomer(long customerId) {
		List<Order> orders = getSession().createQuery("from order_ as o where o.customer.id=?").
				setParameter(0, customerId).list();
		if(orders.size()!=0)
			return false;
		return true;
	}
	
	public Customer getCustomerByNameAndOrganization(String customerName,String customerOrganizationName){
		return (Customer)getSession().createQuery("from Customer as c where c.name=? and c.customerOrganization.name=?").
				setParameter(0,customerName).setParameter(1, customerOrganizationName).uniqueResult();
	}
	
	public Customer getCustomerByPhoneNumber(String phoneNumber){
		return (Customer)getSession().createQuery("from Customer as c where ? in Elements(c.phones)").
				setParameter(0,phoneNumber).uniqueResult();
	}

	public List<Customer> getAllCustomerByOrganization(long customerOrganizationId) {
		List<Customer> customers = getSession().createQuery("from Customer c where c.customerOrganization.id=?").
				setParameter(0, customerOrganizationId).list();
		return customers;
	}
}
