package com.yuqincar.service.customer.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.customer.CustomerDao;
import com.yuqincar.dao.customer.OtherPassengerDao;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.Customer;
import com.yuqincar.domain.order.OtherPassenger;
import com.yuqincar.service.customer.CustomerService;
import com.yuqincar.utils.QueryHelper;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;
    
    @Autowired
    private OtherPassengerDao otherPassengerDao;

    /** 
     * @see com.yuqincar.service.customer.CustomerService#getById(java.lang.Long)
     */
    public Customer getById(Long id) {
        return customerDao.getById(id);
    }

	@Transactional
	public void saveCustomer(Customer customer) {
		customerDao.save(customer);
	}

	@Transactional
	public void updateCustomer(Customer customer) {
		customerDao.update(customer);
	}

	public boolean canDeleteCustomer(long customerId) {
		return customerDao.canDeleteCustomer(customerId);
	}

	@Transactional
	public void deleteCustomer(long customerId) {
		customerDao.delete(customerId);
	}

	public PageBean<Customer> queryCustomer(int pageNum, QueryHelper helper) {
		return customerDao.getPageBean(pageNum, helper);
	}

	public PageBean queryCustomerOrganizationByKeyword(String keyword) {
		// TODO Auto-generated method stub
		return null;
	}

    public Customer getCustomerByNameAndOrganization(String customerName,String customerOrganizationName){
    	return customerDao.getCustomerByNameAndOrganization(customerName,customerOrganizationName);
    }
    
    public Customer getCustomerByPhoneNumber(String phoneNumber){
    	return customerDao.getCustomerByPhoneNumber(phoneNumber);
    }

	@Transactional
    public void deleteOtherPassenger(Customer customer,OtherPassenger otherPassenger){
    	otherPassenger.setDeleted(true);
    	otherPassengerDao.update(otherPassenger);
    }

	public List<Customer> getAllCustomerByOrganization(long customerOrganizationId) {
		return customerDao.getAllCustomerByOrganization(customerOrganizationId);
	}
}
