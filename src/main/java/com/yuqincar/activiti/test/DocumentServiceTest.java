/*package com.yuqincar.activiti.test;

import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.yuqincar.service.document.DocumentService;

public class DocumentServiceTest extends BaseTest{
	
	@Autowired
	private DocumentService documentService;
	
	//这是例子
	@Test
    @DatabaseSetup({ "classpath:/data/privilege/customers_inserts.xml" }) 
	public void testDeleteById() {
		//customerService.delete(4L);
		
		//Customer c = customerService.getById(4L);
		//assertNull(c);
	}
	
	//测试部署流程
	@Test
	public void testDeployCheckProcess(){
		String classPath = "com/yuqincar/activiti/test/test.bpmn";
		documentService.deployCheckProcess(classPath);
		System.out.println();
	}

	//测试删除流程
	@Test
	public void testDeleteCheckProcess(){
		
	}
	
	//测试获取审核链条的信息
	@Test
	public void testGetCheckProcessDescription(){
		
	}
	
	//测试提交审核
	@Test
	public void testSubmitToCheck(){
		
	}
	
	//测试user能够获得的document分页对象
	@Test
	public void testGetToBeChecked(){
		
	}
	
	//测试当前对象能够审核document吗
	@Test
	public void testIsCanBeChecked(){
		
	}
	
	//测试获取对象的曾经审核人
	@Test
	public void testGetCheckedUser(){
		
	}
	
	//测试审核操作
	@Test
	public void testCheck(){
		
	}
	
}
*/