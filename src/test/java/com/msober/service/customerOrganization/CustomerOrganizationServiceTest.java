package com.msober.service.customerOrganization;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.msober.base.BaseTest;
import com.yuqincar.domain.order.Address;
import com.yuqincar.domain.order.CustomerOrganization;
import com.yuqincar.service.CustomerOrganization.CustomerOrganizationService;

public class CustomerOrganizationServiceTest extends BaseTest {
	
	@Autowired
	private CustomerOrganizationService customerOrganizationService;
	
	@Test
	public void testInit(){
		assertNotNull(customerOrganizationService);
	}
	
	
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/customerOrganization/customerorganizations_inserts.xml" })
	public void testIsNaneExist(){
		assertTrue("单位名字存在", customerOrganizationService.isNameExist(1L, "重庆大学"));
		assertFalse("单位名字不存在", customerOrganizationService.isNameExist(1L, "西北大学"));
	}
	
	
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/customerOrganization/customerorganizations_inserts.xml" })
	public void testIsAbbreviationExist(){
		assertTrue("单位简称存在", customerOrganizationService.isAbbreviationExist(1L, "重大"));
		assertFalse("单位简称不存在", customerOrganizationService.isAbbreviationExist(1L, "西大"));
	}
	
	
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/customerOrganization/customerorganizations_inserts.xml" })
	public void testSaveCustomerOrganization(){
		CustomerOrganization customerOrganization = new CustomerOrganization();
		customerOrganization.setName("四川外国语大学");
		customerOrganization.setAbbreviation("川外");
		customerOrganizationService.saveCustomerOrganization(customerOrganization);
		
		CustomerOrganization customerOrganization2 = customerOrganizationService.getById(customerOrganization.getId());
		assertEquals(customerOrganization2.getName(), "四川外国语大学");
		assertEquals(customerOrganization2.getAbbreviation(), "川外");
	}
	
	
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/customerOrganization/customerorganizations_inserts.xml" })
	public void testUpdateCustomerOrganization(){
		CustomerOrganization customerOrganization = customerOrganizationService.getById(1L);
		customerOrganization.setName("重庆医科大学");
		customerOrganization.setAbbreviation("重医");
		customerOrganizationService.updateCustomerOrganization(customerOrganization);
		
		CustomerOrganization customerOrganization2 = customerOrganizationService.getById(1L);
		assertEquals(customerOrganization2.getName(), "重庆医科大学");
		assertEquals(customerOrganization2.getAbbreviation(), "重医");
	}
	
	
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/customerOrganization/customerorganizations_inserts.xml" })
	public void testCanDeleteCustomerOrganization(){
		assertFalse("有订单和客户单位关联，不能删除", customerOrganizationService.canDeleteCustomerOrganization(1L));
		assertTrue("没有订单和客户单位关联，可以删除", customerOrganizationService.canDeleteCustomerOrganization(3L));
	}
	
	
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/customerOrganization/customerorganizations_inserts.xml" })
	public void testDeleteCustomerOrganization(){
		customerOrganizationService.deleteCustomerOrganization(3L);
		CustomerOrganization customerOrganization = customerOrganizationService.getById(3L);
		assertNull(customerOrganization);
	}

}
