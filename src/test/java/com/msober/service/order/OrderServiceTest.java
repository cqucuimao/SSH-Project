/**
 * University Of Chongqing.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.msober.service.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.msober.base.BaseTest;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarServiceType;
import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.monitor.Location;
import com.yuqincar.domain.order.Address;
import com.yuqincar.domain.order.ChargeModeEnum;
import com.yuqincar.domain.order.Customer;
import com.yuqincar.domain.order.CustomerOrganization;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.OrderOperationTypeEnum;
import com.yuqincar.domain.order.OrderSourceEnum;
import com.yuqincar.domain.order.OrderStatusEnum;
import com.yuqincar.service.CustomerOrganization.CustomerOrganizationService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.DateUtils;

public class OrderServiceTest extends BaseTest {

	@Autowired
	private OrderService orderService;

	@Autowired
	private CarService carService;

	@Autowired
	private UserService userService;

	@Autowired
	private CustomerOrganizationService customerOrganizationService;

	/*
	 * 服务必须注入进来
	 */
	@Test
	public void init() {
		Assert.assertNotNull(orderService);
		Assert.assertNotNull(carService);
		Assert.assertNotNull(userService);
	}

	/*
	 * 模糊匹配机构测试 不能匹配到 1、模糊匹配"清华",应该没有数据 能匹配到 1、模糊匹配 "重大",应该只有一条记录,id=3 2、模糊匹配
	 * "沙坪坝区",应该有两条记录 3、模糊匹配"大",两条记录，且简称记录排在排在前面,第一条 id=3,第二条id=4
	 */
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/order/testQueryCustomerOrganizationByKeyword.xml" })
	public void testQueryCustomerOrganizationByKeyword() {

		/**
		 * 测试数据如下 <customerorganization id="1" abbreviation="教委"
		 * name="北京市沙坪坝区教育委员会" address_id="1"/> <customerorganization id="2"
		 * abbreviation="体委" name="上海江北区体育委员会" address_id="1"/>
		 * <customerorganization id="3" abbreviation="重大" name="重庆市沙坪坝区沙正街174号"
		 * address_id="1"/> <customerorganization id="4" abbreviation="重邮"
		 * name="深圳市南岸区重庆邮电大学" address_id="1"/>
		 */
		/**
		 * 查询关键词-匹配项数-匹配第一个id-匹配第二个id
		 */
		String[][] param = new String[][] { { "清华", "0", "null", "null" }, // 1、模糊匹配"清华",应该没有数据
				{ "重大", "1", "3", "null" }, // 2、模糊匹配 "重大",应该只有一条记录,id=3
				{ "沙坪坝区", "2", "1", "3" }, // 3、模糊匹配 "沙坪坝区",应该有两条记录
				{ "大", "2", "3", "4" },// 4、模糊匹配"大",两条记录，且简称记录排在排在前面,第一条
										// id=3,第二条id=4
		};

		for (String[] pa : param) {

			PageBean pageBean = customerOrganizationService
					.queryCustomerOrganizationByKeyword(pa[0]);

			Assert.assertTrue("匹配记录数应该为" + pa[1], pageBean.getRecordList()
					.size() == Integer.parseInt(pa[1]));

			if (pageBean.getRecordList().size() >= 2) {

				Assert.assertTrue(
						"第一条id为" + pa[2],
						((CustomerOrganization) pageBean.getRecordList().get(0))
								.getId() == Integer.parseInt(pa[2]));
				Assert.assertTrue(
						"第二条id为" + pa[3],
						((CustomerOrganization) pageBean.getRecordList().get(1))
								.getId() == Integer.parseInt(pa[3]));
			} else if (pageBean.getRecordList().size() >= 1) {

				Assert.assertTrue(
						"第一条id为" + pa[2],
						((CustomerOrganization) pageBean.getRecordList().get(0))
								.getId() == Integer.parseInt(pa[2]));
			}

		}
	}

	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/order/carservicetype_inserts.xml" })
	public void getAllCarServiceTypeTest() {

		/**
		 * 测试数据 <carservicetype id="1" personLimit="4" pricePerDay="5.000000"
		 * pricePerKM="6.000000" title="titile"/> <carservicetype id="2"
		 * personLimit="5" pricePerDay="6.000000" pricePerKM="7.000000"
		 * title="rrr"/>
		 */
		List<CarServiceType> list = orderService.getAllCarServiceType();
		Assert.assertTrue("数据列表有两个数据", list.size() == 2);

	}

	/**
	 * 1、当月没有数据,从00001开始 2、当月已经有数据,在最大基础上加1
	 */
	@SuppressWarnings("deprecation")
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/order/order_inserts.xml" })
	public void EnQueueTest() {

		/**
		 * 测试数据 <order_ id="2" createTime="2015-11-01 10:23:26.0" sn="151100001"
		 * status="2" />
		 */
		Calendar cc = Calendar.getInstance();
		String yearMonth = String.valueOf(cc.get(Calendar.YEAR)).substring(2)
				+ String.valueOf(cc.get(Calendar.MONTH) + 1);
		// 1、当月没有数据
		Order order = generateData();
		orderService.EnQueue(order,null,0);
		Order order1 = orderService.getOrderBySN(yearMonth + "00001");
		Assert.assertTrue("SN号不正确", order1 != null);
		Assert.assertTrue("订单状态不正确",
				order1.getStatus() == OrderStatusEnum.INQUEUE);

		// 2、当月已经有数据
		Order order3 = orderService.getOrderById(2L);
		orderService.EnQueue(order,null,0);
		Order order4 = orderService.getOrderById(order.getId());
		Assert.assertTrue(
				"SN号不正确",
				order4.getSn().equals(
						(Integer.parseInt(order3.getSn()) + 1) + ""));
		Assert.assertTrue("订单状态不正确",
				order4.getStatus() == OrderStatusEnum.INQUEUE);

	}

	/**
	 * 1、size必须对 2、有内容的size必须对,就是非空的日子 3、日期输入不合法需要测试
	 */
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/order/getCarTask_inserts.xml" })
	public void getCarTaskTest() {

		/**
		 * 不会发生重叠 <car id="1" /> <carcare id="1"
		 * fromDate="2015-12-22 21:28:22.0" toDate="2015-12-23 21:28:33.0"
		 * car_id="1"/> <carexamine id="1" fromDate="2015-12-24 21:28:49.0"
		 * toDate="2015-12-25 21:28:53.0" car_id="1"/> <carrepair id="1"
		 * fromDate="2015-12-26 21:29:21.0" toDate="2015-12-27 21:29:24.0"
		 * car_id="1"/> <order_ id="2" actualBeginDate="2015-12-28 10:23:26.0"
		 * actualEndDate="2015-12-30 10:23:26.0" />
		 */

		Car car = new Car();
		car.setId(1L);

		// 开始时间-结束时间-size-有数据size-errorMessage
		String[][] param = new String[][] {
				{ "null", "2015-11-20", "0", "0", "开始日期不能为空" }, // 开始时间为空
				{ "2015-11-20", "null", "0", "0", "结束日期不能为空" }, // 结束时间为空
				{ "2015-11-20", "2015-10-20", "0", "0", "结束日期必须大于等于开始日期" }, // 开始时间大于结束时间
				{ "2015-12-22", "2015-12-22", "1", "1", "null" }, // 开始时间等于结束时间
				{ "2015-12-20", "2015-12-30", "11", "9", "null" } // 开始时间小于结束时间
		};

		List<List<BaseEntity>> list = new ArrayList<List<BaseEntity>>();
		for (String[] pa : param) {

			Date fromDate = DateUtils.getYMD(pa[0]);
			Date toDate = DateUtils.getYMD(pa[1]);
			try {

				int count = 0;
				list = orderService.getCarTask(car, fromDate, toDate);
				Assert.assertTrue("数据项size不对",
						list.size() == Integer.parseInt(pa[2]));
				for (List<BaseEntity> baseEntity : list) {
					if (baseEntity != null) {
						count++;
					}
				}
				Assert.assertTrue("有活动的车的天数不对哇!",
						count == Integer.parseInt(pa[3]));
			} catch (Exception e) {
				Assert.assertEquals("错误消息不对啊！", pa[4], e.getMessage());
			}
		}
	}

	/**
	 * 1、可调度 2、不可调度
	 */
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/order/order_inserts.xml" })
	public void canScheduleOrderTest() {

		/**
		 * 测试数据 <order_ id="2" createTime="2015-11-01 10:23:26.0" sn="151100001"
		 * status="2" />
		 */
		// 1、可以调度
		Order order1 = orderService.getOrderById(2L);
		order1.setStatus(OrderStatusEnum.INQUEUE);
		try {
//			orderService.updateOrder(order1, null);
		} catch (Exception e) {

		}
		Assert.assertTrue("应该可以调度", orderService.canScheduleOrder(order1));

		// 不可以调度
		Assert.assertTrue("应该不可以调度",
				!orderService.canScheduleOrder(orderService.getOrderById(20L)));

	}

	@Test
	public void queryCustomerTest() {

	}

	/**
	 * 测试size和顺序
	 */
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/order/getQueue_inserts.xml" })
	public void getOrderQueueTest() {

		/**
		 * 测试数据 <order_ id="1" queueTime="2015-11-03 10:23:26.0" status="0"/>
		 * <order_ id="2" queueTime="2015-11-01 10:23:26.0" status="1"/> <order_
		 * id="3" queueTime="2015-11-01 10:23:26.0" status="0"/> <order_ id="4"
		 * queueTime="2015-11-01 10:23:26.0" status="5"/>
		 */
		List<Order> orderList = orderService.getOrderQueue();
		// 1、队列里面有两项
		Assert.assertTrue("队列数据项数不对!", orderList.size() == 2);
		// 2、id=3的排在第一位
		Assert.assertTrue("数据排列顺序不对!", orderList.get(0).getId() == 3);
	}

	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/order/getQueue_inserts.xml" })
	public void canCancelOrderTest() {

		/**
		 * 测试数据 <order_ id="1" queueTime="2015-11-03 10:23:26.0" status="0"/>
		 * <order_ id="2" queueTime="2015-11-01 10:23:26.0" status="1"/> <order_
		 * id="3" queueTime="2015-11-01 10:23:26.0" status="0"/> <order_ id="4"
		 * queueTime="2015-11-01 10:23:26.0" status="5"/>
		 */
		// id=1,2,3可以取消，id=4不可以取消
		Assert.assertTrue("应该可以取消订单",
				orderService.canCancelOrder(orderService.getOrderById(1L)));
		Assert.assertTrue("应该可以取消订单",
				orderService.canCancelOrder(orderService.getOrderById(2L)));
		Assert.assertTrue("应该可以取消订单",
				orderService.canCancelOrder(orderService.getOrderById(3L)));
		Assert.assertTrue("应该不可以取消订单",
				!orderService.canCancelOrder(orderService.getOrderById(4L)));

	}

	@DatabaseSetup({ "classpath:../test-classes/data/order/getQueue_inserts.xml" })
	public void cancelOrderTest() {
		/**
		 * 测试数据 <order_ id="1" queueTime="2015-11-03 10:23:26.0" status="0"/>
		 * <order_ id="2" queueTime="2015-11-01 10:23:26.0" status="1"/> <order_
		 * id="4" queueTime="2015-11-01 10:23:26.0" status="5"/>
		 */
		// id=1,2可以取消，id=4不可以取消
		Object[][] param = new Object[][] {
				{ 1L, 0, null, OrderStatusEnum.CANCELLED,
						OrderOperationTypeEnum.CANCEL },
				{ 2L, 0, null, OrderStatusEnum.CANCELLED,
						OrderOperationTypeEnum.CANCEL },
				{ 4L, 1, "订单状态不正确,无法取消", null, null, null } };

		for (Object[] pa : param) {
			try {

//				Assert.assertTrue("返回结果不正确", orderService
//						.cancelOrder(orderService.getOrderById((Long) pa[0]),
//								null, "取消订单"));
				Assert.assertTrue("订单状态不正确",
						orderService.getOrderById((Long) pa[0]).getStatus()
								.equals(pa[3]));
				// TODO
				// Assert.assertTrue("操作记录不存在");
				// Assert.assertTrue("操作记录状态不正确");
			} catch (Exception e) {
				Assert.assertEquals("错误消息不正确", pa[2], e.getMessage());
			}
		}

	}

	@Transactional
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/order/getRecommandedCar_inserts.xml" })
	public void updateOrderTest() {

		/**
		 * 测试数据 car id=1报废一辆 id=2维修一辆 id=3保养一辆 id=4有订单一辆 id=5年检一辆 id=6,7,8,9正常4辆
		 * 3辆距离近 一辆距离远 order id=1,2,3,4,5,6,id=7 新的
		 */
		// 1、订单已经开始，即订单状态不对
		// 2、正常更新
		Object[][] param = new Object[][] { { 1L, 10L, 1, "订单状态不正确,无法更新" }, // 失败，订单状态不对
				{ 7L, 10L, 0, null } // 成功
		};

		for (Object[] pa : param) {
			Order order = orderService.getOrderById((Long) pa[0]);
			order.setPhone("77777");
			try {
//				int result = orderService.updateOrder(order,
//						userService.getById(1L));
//				Assert.assertTrue("结果校验失败!,result=" + result,
//						result == (Integer) pa[2]);
			} catch (Exception e) {
				Assert.assertTrue("错误消息不正确",
						e.getMessage().equals((String) pa[3]));
			}
		}

	}

	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/order/canUpdateMile_inserts.xml" })
	public void canModifyMileTest() {
		/**
		 * <order_ id="1" status="4" car_id="1" customer_id="1"
		 * customerOrganization_id="1" fromAddress_id="1" serviceType_id="1"
		 * toAddress_id="1" actualBeginMile="0.0" actualEndMile="0.0"/> <order_
		 * id="2" status="5" car_id="2" customer_id="1"
		 * customerOrganization_id="1" fromAddress_id="1" serviceType_id="1"
		 * toAddress_id="1" actualBeginMile="0.0" actualEndMile="0.0"/> <order_
		 * id="3" status="2" car_id="2" customer_id="1"
		 * customerOrganization_id="1" fromAddress_id="1" serviceType_id="1"
		 * toAddress_id="1" actualBeginMile="0.0" actualEndMile="0.0"/>
		 */
		Object[][] param = new Object[][] { { 1L, false }, { 2L, false },
				{ 3L, true } };
		for (Object[] pa : param) {
			Order order = orderService.getOrderById((Long) pa[0]);
//			boolean result = orderService.canModifyMile(order);
//			Assert.assertTrue("返回值不对", result == (Boolean) pa[1]);
		}

	}

	@Transactional
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/order/canUpdateMile_inserts.xml" })
	public void modifyOrderMileTest() {
		/**
		 * <order_ id="1" status="4" car_id="1" customer_id="1"
		 * customerOrganization_id="1" fromAddress_id="1" serviceType_id="1"
		 * toAddress_id="1" actualBeginMile="0.0" actualEndMile="0.0"/> <order_
		 * id="2" status="5" car_id="2" customer_id="1"
		 * customerOrganization_id="1" fromAddress_id="1" serviceType_id="1"
		 * toAddress_id="1" actualBeginMile="0.0" actualEndMile="0.0"/> <order_
		 * id="3" status="2" car_id="2" customer_id="1"
		 * customerOrganization_id="1" fromAddress_id="1" serviceType_id="1"
		 * toAddress_id="1" actualBeginMile="0.0" actualEndMile="0.0"/>
		 */
		Object[][] param = new Object[][] { { 1L, 1, "订单状态不正确,无法修改" },
				{ 2L, 1, "订单状态不正确,无法修改" }, { 3L, 0, null } };
		for (Object[] pa : param) {
			Order order = orderService.getOrderById((Long) pa[0]);
			try {
//				int result = orderService.modifyOrderMile(order, 100,
//						userService.getById(1L));
//				Assert.assertTrue("返回结果不正确", result == (Integer) pa[1]);
			} catch (Exception e) {
				Assert.assertTrue("错误消息不正确!", pa[2].equals(e.getMessage()));
			}

		}
	}

	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/order/orderEndInAdvance_inserts.xml" })
	public void canOrderEndInAdvanceTest() {

		Object[][] param = new Object[][] { { 1L, false }, { 2L, false },
				{ 3L, true } };
		for (Object[] pa : param) {
			Order order = orderService.getOrderById((Long) pa[0]);
//			boolean result = orderService.canOrderEndInAdvance(order);
//			Assert.assertTrue("返回值不对", result == (Boolean) pa[1]);
		}

	}

	@Transactional
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/order/orderEndInAdvance_inserts.xml" })
	public void orderEndInAdvanceTest() {
		/**
		 * <order_ id="1" status="4" car_id="1" customer_id="1"
		 * customerOrganization_id="1" fromAddress_id="1" serviceType_id="1"
		 * toAddress_id="1" actualBeginMile="0.0" actualEndMile="0.0"/> <order_
		 * id="2" status="5" car_id="2" customer_id="1"
		 * customerOrganization_id="1" fromAddress_id="1" serviceType_id="1"
		 * toAddress_id="1" actualBeginMile="0.0" actualEndMile="0.0"/> <order_
		 * id="3" status="2" car_id="2" customer_id="1"
		 * customerOrganization_id="1" fromAddress_id="1" serviceType_id="1"
		 * toAddress_id="1" actualBeginMile="0.0" actualEndMile="0.0"/>
		 */
		Object[][] param = new Object[][] { { 1L, 1, "订单状态不正确" },
				{ 2L, 1, "订单状态不正确" }, { 3L, 0, null } };
		for (Object[] pa : param) {
			Order order = orderService.getOrderById((Long) pa[0]);
			try {
//				int result = orderService.orderEndInAdvance(order, new Date(),
//						"提前结束订单", userService.getById(1L));
//				Assert.assertTrue("返回结果不正确", result == (Integer) pa[1]);
			} catch (Exception e) {
				Assert.assertTrue("错误消息不正确!", pa[2].equals(e.getMessage()));
			}

		}
	}

	@Transactional
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/order/orderEndPost_inserts.xml" })
	public void orderEndPostponeTest() {
		/**
		 * <order_ id="1" status="4" car_id="1" customer_id="1"
		 * customerOrganization_id="1" fromAddress_id="1" serviceType_id="1"
		 * toAddress_id="1" actualBeginMile="0.0" actualEndMile="0.0"/> <order_
		 * id="2" status="5" car_id="2" customer_id="1"
		 * customerOrganization_id="1" fromAddress_id="1" serviceType_id="1"
		 * toAddress_id="1" actualBeginMile="0.0" actualEndMile="0.0"/> <order_
		 * id="3" status="2" car_id="2" customer_id="1"
		 * customerOrganization_id="1" fromAddress_id="1" serviceType_id="1"
		 * toAddress_id="1" actualBeginMile="0.0" actualEndMile="0.0"/>
		 */
		Object[][] param = new Object[][] { { 1L, 1, "订单参数不正确,不可以延后" },
				{ 2L, 1, "订单参数不正确,不可以延后" }, { 3L, 0, null } };
		for (Object[] pa : param) {
			Order order = orderService.getOrderById((Long) pa[0]);
			try {
				int result = orderService.orderEndPostpone(order, new Date(),
						"推迟结束订单", userService.getById(1L));
				Assert.assertTrue("返回结果不正确", result == (Integer) pa[1]);
			} catch (Exception e) {
				Assert.assertTrue("错误消息不正确!", pa[2].equals(e.getMessage()));
			}

		}
	}

	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/order/changecar.xml" })
	public void canChangeCarTest() {

		Object[][] param = new Object[][] { { 1L, false }, { 2L, true } };
		for (Object[] pa : param) {
			Order order = orderService.getOrderById((Long) pa[0]);
//			boolean result = orderService.canChangeCar(order);
//			Assert.assertTrue("返回值不对", result == (Boolean) pa[1]);
		}
	}

	@Transactional
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/order/getRecommandedCar_inserts.xml" })
	public void changeCarTest() {
		/**
		 * 数据准备 location 沙坪坝(id=1,106.447882,29.580908)
		 * 江北(id=2,106.487551,29.594476) 两条 servicepoint 引用上面的 两条id=1,id=2对应
		 * carservicetype 准备两条数据id=1(两人车),id=2(四人车) driver 2人 id=1,张三,id=2,李四
		 * car id=1报废一辆 id=2维修一辆 id=3保养一辆 id=4有订单一辆 id=5年检一辆 id=6,7,8,9正常4辆
		 * 3辆距离近 一辆距离远 (carrepair)|(carcare) |() customer 1个就够了id=1
		 * customerOgation 1个id=1 address 一个id=1 order_ 订单6单 距离近的3单(car.id=6)
		 * 2单(car.id=7) 1单(car.id=8)排列
		 */
		Object[][] param = new Object[][] { { 9L, 1L, 1 }, // 报废车
				{ 9L, 2L, 1 }, // 维修车
				{ 9L, 3L, 1 }, // 保养车
				{ 9L, 4L, 1 }, // 有订单
				{ 9L, 5L, 1 }, // 年检
				{ 1L, 6L, 1, "订单参数不正确,无法换车" }, // 状态不对
				{ 9L, 7L, 0 } // 成功
		};

		for (Object[] pa : param) {
			try {
//				int result = orderService.changeCar(
//						orderService.getOrderById((Long) pa[0]),
//						carService.getCarById((Long) pa[1]),
//						userService.getById(1L));
//				Assert.assertTrue("返回值不对", result == (Integer) pa[2]);
			} catch (Exception e) {
				Assert.assertTrue("错误信息不对", e.getMessage().equals(pa[3]));
			}

		}
	}

	/**
	 * 得到推荐的汽车。并将满足条件的汽车按照匹配度降序排列，可分页。 推荐汽车的原则： 1. 车型符合（需满足） 2.
	 * 可用（没有报废，没有订单，没有维修、保养、年审）（需满足） 3. 距离远近（距离近的排前面） 4. 司机评价（评价好的排前面） 5.
	 * 近期订单多少（少的排前面）
	 * 
	 * @param pageNum
	 *            分页
	 * @return
	 */
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/order/recom.xml" })
	public void getRecommandedCarTest() {
		/**
		 * getRecommandedCar(CarServiceType serviceType, Location location, Date
		 * planBeginDate, Date planEndDate, int pageNum)
		 */
		/**
		 * 数据准备 location 沙坪坝(id=1,106.447882,29.580908)
		 * 江北(id=2,106.487551,29.594476) 两条 servicepoint 引用上面的 两条id=1,id=2对应
		 * carservicetype 准备两条数据id=1(两人车),id=2(四人车) driver 2人 id=1,张三,id=2,李四
		 * car id=1报废一辆 id=2维修一辆 id=3保养一辆 id=4有订单一辆 id=5年检一辆 id=6,7,8,9正常4辆
		 * 3辆距离近 一辆距离远 (carrepair)|(carcare) |() customer 1个就够了id=1
		 * customerOgation 1个id=1 address 一个id=1 order_ 订单6单 距离近的3单(car.id=6)
		 * 2单(car.id=7) 1单(car.id=8)排列
		 */
		Date planBeginDate = DateUtils.getYMD("2015-12-26");
		Date planEndDate = DateUtils.getYMD("2015-12-27");
		// 异常用例测试
		// 1、车型不符合
		CarServiceType serviceType1 = new CarServiceType();
		serviceType1.setId(222L);
		//PageBean pageBean1 = orderService.getRecommandedCar(serviceType1, ChargeModeEnum.MILE, null,
		//		planBeginDate, planEndDate, 1);
		//Assert.assertNull("应该是没有符合的车型", pageBean1);
		// 正常用例测试
		// 1、沙坪坝上车 106.454781,29.579903,有三辆符合要求，排序是 8 7 6 订单数少的排在前面
		List<CarServiceType> serviceTypesList = orderService
				.getAllCarServiceType();
		Location location1 = new Location();
		location1.setId(100L);
		location1.setLatitude(106.454781);
		location1.setLongitude(29.579903);
		//PageBean pageBean2 = orderService.getRecommandedCar(
		//		serviceTypesList.get(0), ChargeModeEnum.MILE,location1, planBeginDate, planEndDate,
		//		1);
		//Assert.assertNotNull("记录数不应该为空", pageBean2);
		//Assert.assertTrue("记录数不对", pageBean2.getRecordCount() == 3);
		//Assert.assertTrue(
		//		"数据记录顺序不对",
		//		((Car) pageBean2.getRecordList().get(0)).getId() == 8
		//				&& ((Car) pageBean2.getRecordList().get(1)).getId() == 7);

		// 2、江北上车 106.496749,29.594979 只有一辆符合要求，9
		Location location2 = new Location();
		location2.setId(101L);
		location2.setLatitude(106.496749);
		location2.setLongitude(29.594979);
//		PageBean pageBean3 = orderService.getRecommandedCar(
//				serviceTypesList.get(0),ChargeModeEnum.MILE, location2, planBeginDate, planEndDate,
//				1);
//		Assert.assertNotNull("记录数不应该为空", pageBean3);
//		Assert.assertTrue("数据记录不对",
//				((Car) pageBean3.getRecordList().get(0)).getId() == 9);

	}

	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/order/getRecommandedCar_inserts.xml" })
	public void scheduleOrderTest() {
		/**
		 * 调度车辆。如果order.status==INQUEUE，那么就不需要再保存order，而是修改其car和driver的属性值，
		 * 否则就要先保存。 保存Order前，需要设置SN属性，详情请见EnQueue方法的解释。 再操作前，必须要判断如下的合法性： 1.
		 * order的状态必须为null或INQUEUE才能调度。以防止已被其它调度员先调度。 2.
		 * 必须判断car在时间上是空闲的（按天算）。以防止此car已被其它调度员调度。 3. 必须判断car.status==NORMAL 4.
		 * 必须判断car没有在订单时间内进行维修
		 * 、年审、保养（没有时间段内的CarInsurance记录,CarRepair记录,CarExamine记录）
		 * 调度成功后，需要生成APPRemindMessage，并发送相同内容的短消息给司机。
		 * 
		 * @param order
		 *            订单
		 * @return 0 成功 orderId=7,carId=10 1 订单已经被调度 orderId=1 carId=10 2
		 *         车辆已经被调度 orderId=8,carId=6 3 车辆已报废 orderId=7,carId=1 4 车辆在维修
		 *         orderId=7,carId=2 5 车辆在年审 orderId=7,carId=5 6 车辆在保养
		 *         orderId=7,carId=3
		 */
		/*
		 * 数据准备 car id=1报废一辆 id=2维修一辆 id=3保养一辆 id=4有订单一辆 id=5年检一辆 id=6,7,8,9正常4辆
		 * order_ 订单6单 距离近的3单(car.id=6) 2单(car.id=7) 1单(car.id=8)排列
		 */

		Object[][] param = new Object[][] { { 7L, 10L, 0 }, // 成功
				{ 1L, 10L, 1 }, // 订单已被调度
				{ 8L, 6L, 2 }, // 车辆已被调度
				{ 7L, 1L, 3 }, // 车辆已报废
				{ 7L, 2L, 4 }, // 车辆在维修
				{ 7L, 5L, 5 }, // 车辆在年审
				{ 7L, 3L, 6 } };// 车辆在保养
		for (Object[] pa : param) {
//			int result = orderService.scheduleOrder(
//					orderService.getOrderById((Long) pa[0]),
//					carService.getCarById((Long) pa[1]),0);
//			Assert.assertTrue("返回值不对,orderId=" + pa[0] + ",carId=" + pa[1]
//					+ ",result=" + result, result == (Integer) pa[2]);
		}

	}

	/*
	 * 构造一张表数据
	 */
	private Order generateData() {

		Order order = new Order();
		order.setSn((new Random()).nextLong() + "");

		Address address = new Address();
		address.setId(1L);
//		order.setFromAddress(address);

		Address toAddress = new Address();
		toAddress.setId(1L);
		//order.setToAddress(toAddress);

		Location location = new Location();
		location.setId(1L);

		CustomerOrganization customerOrganization = new CustomerOrganization();
		customerOrganization.setId(1L);

		order.setCustomerOrganization(customerOrganization);

		Customer customer = new Customer();
		customer.setId(1L);
		order.setCustomer(customer);

		order.setPhone("1234566777");
		order.setChargeMode(ChargeModeEnum.DAY);
		order.setPlanBeginDate(new Date());
		order.setPlanEndDate(new Date());
		//order.setPassengerNumber(4);

		CarServiceType carServiceType = new CarServiceType();
		carServiceType.setId(1L);
		order.setServiceType(carServiceType);

		order.setOrderMoney(new BigDecimal(120));
		order.setActualMoney(new BigDecimal(120));
		order.setStatus(OrderStatusEnum.BEGIN);
		order.setOrderSource(OrderSourceEnum.SCHEDULER);
		order.setCallForOther(false);

		return order;

	}

	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/order/getCarsWithoutOrderNow_inserts.xml" })
	public void getCarsWithoutOrderNowTest() {
		List<Car> carList = orderService.getCarsWithoutOrderNow();
		Assert.assertEquals(carList.size(), 6);
		Assert.assertTrue(carList.get(0).getId() == 1);
		Assert.assertTrue(carList.get(1).getId() == 2);
		Assert.assertTrue(carList.get(2).getId() == 3);
		Assert.assertTrue(carList.get(3).getId() == 6);
		Assert.assertTrue(carList.get(4).getId() == 7);
		Assert.assertTrue(carList.get(5).getId() == 8);

	}
}
