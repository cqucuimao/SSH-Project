package com.msober.service.app;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.msober.base.BaseTest;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.APPRemindMessage;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.OrderStatusEnum;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.app.APPRemindMessageService;
import com.yuqincar.service.app.DriverAPPService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.DateUtils;

public class DriverAppServiceTest extends BaseTest {
	
	@Autowired
	private DriverAPPService driverAppService;
	@Autowired
	private UserService userService;
	@Autowired
	private APPRemindMessageService appRemindMessageService;
	@Autowired
	private OrderService orderService;

	@Test
    @DatabaseSetup({ "classpath:../test-classes/data/app/Orders.xml" })
	public void testGetUndoOrder() {
		User u = new User();
		u.setId(1L);
		Order o =  driverAppService.getUndoOrder(u, 101L);
		assertEquals(o.getId(),(Long)101L);
		assertEquals(o.getStatus(),OrderStatusEnum.SCHEDULED);

	}
	
	@Test
    @DatabaseSetup({ "classpath:../test-classes/data/app/Orders.xml" })
	public void testGetAllUndoOrders() {
		List<Order> orders = driverAppService.getAllUndoOrders(userService.getByLoginName("driver1"));
		assertEquals(orders.size(),1);
		
		List<Order> orders2 = driverAppService.getAllUndoOrders(userService.getByLoginName("driver2"));
		assertEquals(orders2.size(),0);
	}
	
	@Test
    @DatabaseSetup({ "classpath:../test-classes/data/app/APPRemindMessage.xml" })
	public void testGetAllUnsendedRemindMessage() {
		List<APPRemindMessage> messages1 = driverAppService.getAllUnsendedRemindMessage(userService.getByLoginName("driver1"));
		assertEquals(2,messages1.size());
		
		List<APPRemindMessage> messages2 = driverAppService.getAllUnsendedRemindMessage(userService.getByLoginName("driver2"));
		assertEquals(0,messages2.size());
	}
	
	@Test
    @DatabaseSetup({ "classpath:../test-classes/data/app/APPRemindMessage.xml" })
	public void testSetRemindMessageSended() {
		APPRemindMessage message = appRemindMessageService.getById(1L);
		message.setSended(true);
		appRemindMessageService.update(message);
		assertEquals(true,appRemindMessageService.getById(1L).isSended());
	}
	
	@Test
    @DatabaseSetup({ "classpath:../test-classes/data/app/Orders.xml" })
	@Transactional
	public void testOrderBegin() {
		Order order = orderService.getOrderById(2L);
		driverAppService.orderBegin(order);
		
	}
	
	
	@Test
    @DatabaseSetup({ "classpath:../test-classes/data/app/Orders.xml" })
	public void testQueryOrder() {
		User user = userService.getById(2L);
		Date fromDate = DateUtils.getFirstDateOfMonth(new Date());

		Map<String, Object> orderMap = driverAppService.queryEndOrder(1, fromDate, new Date(), user );
		
		PageBean<Order> pageBean = (PageBean) orderMap.get("pageBean");
		Order order23 = pageBean.getRecordList().get(0);
		Order order24 = pageBean.getRecordList().get(1);

		assertEquals(23L,order23.getId().longValue());
		assertEquals(24L,order24.getId().longValue());
		assertEquals(340,orderMap.get("sumActualMile"));

	}
}
