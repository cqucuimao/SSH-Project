package com.msober.service.privilege;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.msober.base.BaseTest;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.QueryHelper;

public class UserServiceTest extends BaseTest{
	@Autowired
	private UserService userService;
	
	/**
	 * 判断spring是否初始化成功userService
	 */
	@Test
	public void testInit() {
		assertNotNull(userService);  
	}
	
	@Test 
	public void testGetById() throws SQLException, Exception {
	}
	
	@Test
    @DatabaseSetup({ "classpath:../test-classes/data/privilege/users_inserts.xml" })
	public void testDeleteById() {
		userService.delete(3L);
		
		User u = userService.getById(3L);
		assertNull(u);
	}
	
	
	@Test
    @DatabaseSetup({ "classpath:../test-classes/data/privilege/users_inserts.xml" })
	public void testUpdate() {
		User u = userService.getById(2L);
		u.setName("xinmingzi");
		userService.update(u);
		
		User u2 = userService.getById(2L);
		assertEquals(u2.getName(), "xinmingzi");
		
	}
	
	@Test
    @DatabaseSetup({ "classpath:../test-classes/data/privilege/users_inserts.xml" })
	public void testGetByLoginNameAndPassword() {
		User u1 = userService.getByLoginNameAndPassword("admin", "admin");
		assertNotNull(u1); 
	}
	
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/privilege/users_inserts.xml" })
	public void testGetByLoginName() {
		User u1 = userService.getByLoginName("admin");
		assertNotNull(u1); 
	}
	
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/privilege/users_inserts.xml" })
	public void testGetPageBean() {
		QueryHelper queryHelper = new QueryHelper(User.class, "u");
		PageBean pageBean1 = userService.getPageBean(1, queryHelper);
		assertEquals(10, pageBean1.getRecordList().size());
		PageBean pageBean2 = userService.getPageBean(2, queryHelper);
		assertEquals(3, pageBean2.getRecordList().size());
	}
	
	
}
