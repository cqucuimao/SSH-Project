package com.msober.xmldata.app;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.msober.base.BaseTest;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.OrderStatusEnum;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.TestUtils;

public class ExportOrderXmlData extends BaseTest {
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private OrderService orderService;

	@Autowired
	private UserService userService;
	
	public void initData() {
		User user1 = new User();
		user1.setLoginName("driver1");
		
		User user2 = new User();
		user2.setLoginName("driver2");
		
		userService.save(user1);
		userService.save(user2);

		
		Order o1 = new Order();
        o1.setSn((new Random()).nextLong() + "");
		o1.setStatus(OrderStatusEnum.SCHEDULED);
		o1.setDriver(user1);
		orderService.save(o1);

		
		Order o2 = new Order();
        o2.setSn((new Random()).nextLong() + "");
		o2.setStatus(OrderStatusEnum.BEGIN);
		o2.setDriver(user2);
		
		orderService.save(o2);
		
	}
	
	@Test
	public void CreateOrder() throws FileNotFoundException, SQLException, DatabaseUnitException, IOException {
		
		initData();
		
		List<String> tables = new ArrayList<String>();
		tables.add("user");
		tables.add("order_");
		TestUtils.exportData(dataSource.getConnection(), tables, "D:\\order_inserts.xml");
	}
}
