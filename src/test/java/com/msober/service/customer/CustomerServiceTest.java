package com.msober.service.customer;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.msober.base.BaseTest;
import com.yuqincar.domain.order.Customer;
import com.yuqincar.domain.order.CustomerOrganization;
import com.yuqincar.service.CustomerOrganization.CustomerOrganizationService;
import com.yuqincar.service.customer.CustomerService;

public class CustomerServiceTest extends BaseTest {
	
	@Autowired
	private CustomerService customerService;
	@Autowired
	private CustomerOrganizationService customerOrganizationService;
	
	
	@Test
	public void testInit(){
		assertNotNull(customerService);
		assertNotNull(customerOrganizationService);
	}
	
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/customer/customers_inserts.xml" })
	@Transactional
	public void testSaveCustomer(){
		Customer customer = new Customer();
		CustomerOrganization customerOrganization = customerOrganizationService.getById(1L);
		customer.setName("三教");
		customer.setCustomerOrganization(customerOrganization);
		customerService.saveCustomer(customer);
		
		Customer customer1 = customerService.getById(customer.getId());
		assertEquals(customer1.getCustomerOrganization().getId().longValue(), 1L);
		assertEquals(customer1.getName(), "三教");
	}
	
	
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/customer/customers_inserts.xml" })
	@Transactional
	public void testUpdateCustomer(){
		Customer customer = customerService.getById(1L);
		customer.setCustomerOrganization(customerOrganizationService.getById(2L));
		customer.setName("二麻子");
		customerService.updateCustomer(customer);
		
		Customer customer2 = customerService.getById(1L);
		assertEquals(customer2.getCustomerOrganization().getId().longValue(), 2);
		assertEquals(customer2.getName(), "二麻子");
	}
	
	
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/customer/customers_inserts.xml" })
	public void testDeleteCustomer(){
		customerService.deleteCustomer(2L);
		Customer customer = customerService.getById(2L);
		assertNull(customer);
	}
	
	
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/customer/customers_inserts.xml" })
	public void testCanDeleteCustomer(){
		assertFalse("有订单和客户关联，不能删除", customerService.canDeleteCustomer(1L));
		assertTrue("没有订单和客户关联，可以删除", customerService.canDeleteCustomer(3L));
	}
	

}
