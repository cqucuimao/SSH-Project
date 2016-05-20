/**
 * University Of Chongqing.com Inc.
 * Copyright (c) 2004-2015 All Rights Reserved.
 */
package com.yuqincar.dao.order.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.dao.order.OrderDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarServiceType;
import com.yuqincar.domain.car.CarStatusEnum;
import com.yuqincar.domain.car.ServicePoint;
import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.monitor.Location;
import com.yuqincar.domain.order.ChargeModeEnum;
import com.yuqincar.domain.order.DayOrderDetail;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.OrderStatusEnum;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.utils.Configuration;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;


@Repository
public class OrderDaoImpl extends BaseDaoImpl<Order> implements OrderDao {
	private static Log log = LogFactory.getLog(OrderDaoImpl.class);

	@SuppressWarnings("unchecked")
	public List<CarServiceType> getAllCarServiceType() {
		return getSession().createQuery("from CarServiceType").list();
	}

	public void EnQueue(Order order,String baseSN) {

		/**
		 * 进队列。实质是先保存订单，等待调度。 需要设置进队列时间queueTime。
		 * 保存订单前，需要设置order的SN置，原则是"YYMMXXXXXX"
		 * ,YY表示两位年，MM表示两位月，XXXXX表示每个月的流水号，每个月从00001开始。
		 * 
		 */
		String sn = getSN(order,baseSN);
		order.setSn(sn);
		order.setQueueTime(new Date());
		// 设置订单状态,状态设置为进队列
		order.setStatus(OrderStatusEnum.INQUEUE);
		save(order);
	}

	public Order getOrderBySN(String sn) {
		return (Order) (getSession().createQuery("from order_ where sn=?")
				.setParameter(0, sn).uniqueResult());
	}

	public List<List<BaseEntity>> getCarTask(Car car, Date fromDate, Date toDate) {

		List<List<BaseEntity>> list = new ArrayList<List<BaseEntity>>();
		String hql = null;
		int days = DateUtils.elapseDays(fromDate, toDate, true, true);
		BaseEntity entity = null;
		
		for (int i = 0; i < days; i++) {
			List<BaseEntity> dayList=null;
			Date date = DateUtils.getOffsetDate(fromDate, i);
			// 1、CarCare
			hql = "from CarCare where car.id=? and TO_DAYS(?)=TO_DAYS(date)";
			entity = (BaseEntity) getSession().createQuery(hql)
					.setParameter(0, car.getId()).setParameter(1, date)
					.uniqueResult();
			if(entity!=null){
				dayList=new ArrayList<BaseEntity>();
				dayList.add(entity);
				entity=null;
			}
			
			// 2、CarRepair
			hql = "from CarRepair where car.id=? and TO_DAYS(fromDate)<=TO_DAYS(?) and TO_DAYS(?)<=TO_DAYS(toDate)";
			entity = (BaseEntity) getSession().createQuery(hql)
					.setParameter(0, car.getId()).setParameter(1, date)
					.setParameter(2, date).uniqueResult();
			if (entity != null) {
				if(dayList==null)
					dayList=new ArrayList<BaseEntity>();
				dayList.add(entity);
				entity=null;
			}
			
			// 3、CarExam
			hql = "from CarExamine where car.id=? and TO_DAYS(?)=TO_DAYS(date)";
			entity = (BaseEntity) getSession().createQuery(hql)
					.setParameter(0, car.getId()).setParameter(1, date)
					.uniqueResult();
			if (entity != null) {
				if(dayList==null)
					dayList=new ArrayList<BaseEntity>();
				dayList.add(entity);
				entity=null;
			}
			
			// 4、Order 预订用车或者实际在用车都算
			hql = "from order_ as o where o.status<>? and o.car=? and (((o.chargeMode=? or o.chargeMode=?) and TO_DAYS(o.planBeginDate)=TO_DAYS(?)) or ((o.chargeMode=? or o.chargeMode=?) and TO_DAYS(o.planBeginDate)<=TO_DAYS(?) and TO_DAYS(?)<=TO_DAYS(o.planEndDate)))";
			List<BaseEntity> orderList = (List<BaseEntity>) getSession().createQuery(hql)
					.setParameter(0, OrderStatusEnum.CANCELLED).setParameter(1, car)
					.setParameter(2, ChargeModeEnum.MILE).setParameter(3, ChargeModeEnum.PLANE).setParameter(4, date)
					.setParameter(5, ChargeModeEnum.DAY).setParameter(6, ChargeModeEnum.PROTOCOL).setParameter(7, date)
					.setParameter(8, date).list();
			if(orderList!=null && orderList.size()>0){
				if(dayList==null)
					dayList=new ArrayList<BaseEntity>();
				dayList.addAll(orderList);
			}
			
			list.add(dayList);
		}
		return list;
	}

	/**
	 * @see com.yuqincar.dao.order.OrderDao#canScheduleOrder(com.yuqincar.domain.order.Order)
	 */
	public boolean canScheduleOrder(Order order) {
		return ((Order) getSession().get(Order.class, order.getId()))
				.getStatus().equals(OrderStatusEnum.INQUEUE);
	}

	public int scheduleOrder(String scheduleMode,Order order, Car car, User driver, User user) {
		if(scheduleMode==OrderService.SCHEDULE_FROM_QUEUE && order.getStatus()!=OrderStatusEnum.INQUEUE)
			return 1;
		
		if(order.getStatus()==OrderStatusEnum.INQUEUE &&
				(!order.isScheduling() || !order.getScheduler().equals(user)))
			return 9;
		
		// 判断车子状态
		int carStatus = isCarAndDriverAvailable(order, car,driver);
		if (carStatus != 0) {
			return carStatus;
		}
		
		if(scheduleMode==OrderService.SCHEDULE_FROM_NEW){
			order.setSn(getSN(order,null));
			order.setCar(car);
			order.setStatus(OrderStatusEnum.SCHEDULED);
			order.setDriver(car.getDriver());
			order.setScheduler(user);
			order.setScheduleTime(new Date());
			save(order);
		}else if(scheduleMode==OrderService.SCHEDULE_FROM_QUEUE){
			order.setCar(car);
			order.setDriver(car.getDriver());
			order.setStatus(OrderStatusEnum.SCHEDULED);
			order.setScheduler(user);
			order.setScheduleTime(new Date());
			order.setScheduling(false);
			update(order);
		}else if(scheduleMode==OrderService.SCHEDULE_FROM_UPDATE){
			order.setCar(car);
			order.setDriver(car.getDriver());
			order.setScheduler(user);
			update(order);
		}
		
		return 0;
	}

	/**
	 * @see com.yuqincar.dao.order.OrderDao#getOrderQueue()
	 */
	@SuppressWarnings("unchecked")
	public List<Order> getOrderQueue() {
		return getSession()
				.createQuery(
						"from order_ where status=? order by queueTime asc")
				.setParameter(0, OrderStatusEnum.INQUEUE).list();
	}

	//按照距离location最近的直线距离升序排序的业务点列表
	public List<ServicePoint> getAscendingDirectNearestServicePoints(Location location){
		final List<ServicePoint> servicePointList=(List<ServicePoint>)getSession().createQuery("from ServicePoint").list();
		final List<Double> servicePointDistanceList=new LinkedList<Double>();
		for(ServicePoint servicePoint:servicePointList)
			servicePointDistanceList.add(straightLineDistance(location.getLongitude(),location.getLatitude(),
					servicePoint.getPointAddress().getLocation().getLongitude(),servicePoint.getPointAddress().getLocation().getLatitude()));
		Collections.sort(servicePointList, new Comparator<ServicePoint>(){
			public int compare(ServicePoint sp1, ServicePoint sp2) {
				int index1=servicePointList.indexOf(sp1);
				int index2=servicePointList.indexOf(sp2);
				if(servicePointDistanceList.get(index1).doubleValue()<servicePointDistanceList.get(index2).doubleValue())
					return -1;
				else if(servicePointDistanceList.get(index1).doubleValue()>servicePointDistanceList.get(index2).doubleValue())
					return 1;
				return 0;
            }
		});
		return servicePointList;
	}
	
	@SuppressWarnings("unchecked")
	public PageBean getRecommandedCar(CarServiceType serviceType, ChargeModeEnum chargeMode,
			Date planBeginDate, Date planEndDate, int pageNum) {

		/**
		 ** 1. 车型符合（需满足） 
		 ** 2. 车可用（没有报废，没有按天计费订单，没有协议计费订单，没有预约维修、预约保养、预约年审）（需满足）
		 ** 3. 司机可用（没有按天计费订单，没有协议计费订单，没有预约维修、预约保养、预约年审）（需满足）
		 ** 4. 订单少的排前面 
		 ** 5. 司机评价（评价好的排前面） 
		 ** 6. 近期订单多少（一个月内，少的排前面）
		 ** 
		 ** 
		 ** 算法： 
		 * 1. 针对驻车点列表中的每个驻车点： 
		 * 		2.1 如果chargeMode==MILE || chargeMode==PLANE，按照下述条件从数据库中查询出： 
		 * 			（A）没有报废
		 * 			（B）车型符合 
		 * 			（C）planBeginDate这一天没有DAY和PROTOCOL计费订单（未取消）
		 * 			（D）planBeginDate这一天没有预约维修、预约保养、预约年审 
		 * 			（E）对应的司机也满足（C）、（D）
		 * 		2.2 否则，按照下述条件从数据库中查询出： 
		 * 			（A）没有报废 
		 * 			（B）车型符合
		 * 			（C）planBeginDate到planEndDate之间没有任何订单（未取消）
		 * 			（D）planBeginDate到planEndDate之间没有预约维修、预约保养、预约年审 
		 * 			（E）对应的司机也满足（C）、（D）
		 * 2. 对车辆列表中的车辆按照以下条件进行排序：
		 * 		2.1 如果chargeMode==MILE || chargeMode==PLANE，按照下述条件排序：
		 * 			车辆在planBeginDate这一天具有的订单（按里程收费，未取消）数量升序
		 * 		2.2 否则，按照下述条件排序：
		 * 			车辆在planBeginDate和planEndDate之间具有的订单（按里程收费，未取消）数量升序
		 * 3. 对车辆列表中的车辆按照以下条件进行第二顺序排序：
		 * 		司机评价（评价好的排前面）
		 * 4. 对车辆列表中的车辆按照以下条件进行第三顺序排序：
		 * 		近期订单多少（一个月内，少的排前面）
		 * 5. 如果车辆列表中的车辆数量大于了pageSize，则删除多余的。
		 */
		System.out.println("in OrderDaoImpl, getRecommendedCar");
		final List<Car> carList=new LinkedList<Car>();	//车辆列表
		final List<Integer> orderCountList=new LinkedList<Integer>();	//与车辆列表对应的车辆具有的订单数量列表（用于排序）
		final List<Integer> orderMonthCountList=new LinkedList<Integer>();	//与车辆列表对应的车辆最近一个月具有的订单数量列表（用于排序）
		int pageSize = Configuration.getPageSize();
		
		List<Car> tempCarList;
		if(chargeMode==ChargeModeEnum.MILE || chargeMode==ChargeModeEnum.PLANE){
			String hql = "from Car as car where car.status<>? and serviceType=? and car.standbyCar =?";
					  hql = hql+" and car not in (select o.car from order_ as o where o.chargeMode==? and o.chargeMode==? and  and o.status<>? and o.status<>? and o.status<>? and TO_DAYS(o.planBeginDate)<=TO_DAYS(?) and TO_DAYS(?)<=TO_DAYS(o.planEndDate))";
					  hql = hql+" and car not in (select cc.car from CarCare as cc where cc.appointment=? and TO_DAYS(cc.date)=TO_DAYS(?))";
					  hql = hql+" and car not in (select ce.car from CarExamine as ce where ce.appointment=? and TO_DAYS(ce.date)=TO_DAYS(?))";
					  hql = hql+" and car not in (select cr.car from CarRepair as cr where cr.appointment=? and TO_DAYS(cr.fromDate)<=TO_DAYS(?) and TO_DAYS(?)<=TO_DAYS(cr.toDate))";
					  
					  hql = hql+" and car.driver is not null";
					  hql = hql+" and car.driver not in (select o.driver from order_ as o where o.chargeMode==? and o.chargeMode==? and o.status<>? and o.status<>? and o.status<>? and TO_DAYS(o.planBeginDate)<=TO_DAYS(?) and TO_DAYS(?)<=TO_DAYS(o.planEndDate))";
					  hql = hql+" and car.driver not in (select cc.driver from CarCare as cc where cc.appointment=? and TO_DAYS(cc.date)=TO_DAYS(?))";
					  hql = hql+" and car.driver not in (select ce.driver from CarExamine as ce where ce.appointment=? and TO_DAYS(ce.date)=TO_DAYS(?))";
					  hql = hql+" and car.driver not in (select cr.driver from CarRepair as cr where cr.appointment=? and TO_DAYS(cr.fromDate)<=TO_DAYS(?) and TO_DAYS(?)<=TO_DAYS(cr.toDate))";
			tempCarList=getSession().createQuery(hql)
					.setParameter(0, CarStatusEnum.SCRAPPED).setParameter(1, serviceType).setParameter(2,false)
					
					.setParameter(3, ChargeModeEnum.DAY).setParameter(4, ChargeModeEnum.PROTOCOL)
					.setParameter(5, OrderStatusEnum.CANCELLED).setParameter(6, OrderStatusEnum.END)
					.setParameter(7, OrderStatusEnum.PAYED).setParameter(8, planBeginDate)
					.setParameter(9, planBeginDate).setParameter(10, true)
					.setParameter(11, planBeginDate).setParameter(12,true)
					.setParameter(13, planBeginDate).setParameter(14,true)
					.setParameter(15,planBeginDate).setParameter(16, planBeginDate)
					
					.setParameter(17, ChargeModeEnum.DAY).setParameter(18, ChargeModeEnum.PROTOCOL)
					.setParameter(19, OrderStatusEnum.CANCELLED).setParameter(20, OrderStatusEnum.END)
					.setParameter(21, OrderStatusEnum.PAYED).setParameter(22, planBeginDate)
					.setParameter(23, planBeginDate).setParameter(24, true)
					.setParameter(25, planBeginDate).setParameter(26,true)
					.setParameter(27, planBeginDate).setParameter(28,true)
					.setParameter(29,planBeginDate).setParameter(30, planBeginDate).list();					
		} else {
			String hql = "from Car as car where car.status<>? and serviceType=? and car.standbyCar =?";
					  hql = hql+" and car not in (select o.car from order_ as o where o.status<>? and o.status<>? and o.status<>? and (";
					   		 hql+="(TO_DAYS(?)<=TO_DAYS(o.planBeginDate) and TO_DAYS(o.planBeginDate) <=TO_DAYS(?)) or ";
					   		 hql+="(TO_DAYS(?)<=TO_DAYS(o.planEndDate) and TO_DAYS(o.planEndDate) <=TO_DAYS(?)) or ";
					   		 hql+="(TO_DAYS(o.planBeginDate)<=TO_DAYS(?) and TO_DAYS(?) <=TO_DAYS(o.planEndDate))))";
					  hql = hql+" and car not in (select cc.car from CarCare as cc where cc.appointment=? and TO_DAYS(?)<=TO_DAYS(cc.date) and TO_DAYS(cc.date)<=TO_DAYS(?))";
					  hql = hql+" and car not in (select ce.car from CarExamine as ce where ce.appointment=? and TO_DAYS(?)<=TO_DAYS(ce.date) and TO_DAYS(ce.date)<=TO_DAYS(?))";
					  hql = hql+" and car not in (select cr.car from CarRepair as cr where appointment=? and (";
					   		hql+="(TO_DAYS(cr.fromDate)<=TO_DAYS(?) and TO_DAYS(?) <=TO_DAYS(cr.toDate)) or ";
					   		hql+="(TO_DAYS(cr.fromDate)<=TO_DAYS(?) and TO_DAYS(?) <=TO_DAYS(cr.toDate)) or ";
					   		hql+="(TO_DAYS(?)<=TO_DAYS(cr.fromDate) and TO_DAYS(cr.toDate) <=TO_DAYS(?))))";
					   		
					  hql = hql+" and car.driver is not null";
					  hql = hql+" and car.driver not in (select o.driver from order_ as o where o.status<>? and o.status<>? and o.status<>? and (";
					   		 hql+="(TO_DAYS(?)<=TO_DAYS(o.planBeginDate) and TO_DAYS(o.planBeginDate) <=TO_DAYS(?)) or ";
					   		 hql+="(TO_DAYS(?)<=TO_DAYS(o.planEndDate) and TO_DAYS(o.planEndDate) <=TO_DAYS(?)) or ";
					   		 hql+="(TO_DAYS(o.planBeginDate)<=TO_DAYS(?) and TO_DAYS(?) <=TO_DAYS(o.planEndDate))))";
					  hql = hql+" and car.driver not in (select cc.driver from CarCare as cc where cc.appointment=? and TO_DAYS(?)<=TO_DAYS(cc.date) and TO_DAYS(cc.date)<=TO_DAYS(?))";
					  hql = hql+" and car.driver not in (select ce.driver from CarExamine as ce where ce.appointment=? and TO_DAYS(?)<=TO_DAYS(ce.date) and TO_DAYS(ce.date)<=TO_DAYS(?))";
					  hql = hql+" and car.driver not in (select cr.driver from CarRepair as cr where appointment=? and (";
					   		hql+="(TO_DAYS(cr.fromDate)<=TO_DAYS(?) and TO_DAYS(?) <=TO_DAYS(cr.toDate)) or ";
					   		hql+="(TO_DAYS(cr.fromDate)<=TO_DAYS(?) and TO_DAYS(?) <=TO_DAYS(cr.toDate)) or ";
					   		hql+="(TO_DAYS(?)<=TO_DAYS(cr.fromDate) and TO_DAYS(cr.toDate) <=TO_DAYS(?))))";
			tempCarList=getSession().createQuery(hql)
					.setParameter(0, CarStatusEnum.SCRAPPED).setParameter(1, serviceType).setParameter(2, false)
					
					.setParameter(3, OrderStatusEnum.CANCELLED).setParameter(4, OrderStatusEnum.END).setParameter(5, OrderStatusEnum.PAYED)
					.setParameter(6, planBeginDate).setParameter(7, planEndDate)
					.setParameter(8, planBeginDate).setParameter(9, planEndDate)
					.setParameter(10, planBeginDate).setParameter(11,planEndDate)
					.setParameter(12, true).setParameter(13,planBeginDate)
					.setParameter(14, planEndDate).setParameter(15, true)
					.setParameter(16,planBeginDate).setParameter(17, planEndDate)
					.setParameter(18, true).setParameter(19,planBeginDate)
					.setParameter(20, planBeginDate).setParameter(21,planEndDate)
					.setParameter(22, planEndDate).setParameter(23,planBeginDate)
					.setParameter(24, planEndDate)
					
					.setParameter(25, OrderStatusEnum.CANCELLED).setParameter(26, OrderStatusEnum.END).setParameter(27, OrderStatusEnum.PAYED)
					.setParameter(28, planBeginDate).setParameter(29, planEndDate)
					.setParameter(30, planBeginDate).setParameter(31, planEndDate)
					.setParameter(32, planBeginDate).setParameter(33,planEndDate)
					.setParameter(34, true).setParameter(35,planBeginDate)
					.setParameter(36, planEndDate).setParameter(37, true)
					.setParameter(38,planBeginDate).setParameter(39, planEndDate)
					.setParameter(40, true).setParameter(41,planBeginDate)
					.setParameter(42, planBeginDate).setParameter(43,planEndDate)
					.setParameter(44, planEndDate).setParameter(45,planBeginDate)
					.setParameter(46, planEndDate).list();
		}
		carList.addAll(tempCarList);
			
		//为订单数量排序做准备
		for(Car car:tempCarList){
			int count;
			if(chargeMode==ChargeModeEnum.MILE || chargeMode==ChargeModeEnum.PLANE){
				System.out.println("7");
				String hql="select count(o) from order_ as o where o.status<>? and o.car=? and (o.chargeMode=? or o.chargeMode=?) and TO_DAYS(?)=TO_DAYS(o.planBeginDate)";
				Object obj=getSession().createQuery(hql).setParameter(0, OrderStatusEnum.CANCELLED)
						.setParameter(1, car).setParameter(2, ChargeModeEnum.MILE).setParameter(3, ChargeModeEnum.PLANE)
						.setParameter(4, planBeginDate).uniqueResult();
				System.out.println("obj="+obj);
				count=Integer.parseInt(obj.toString());
				System.out.println("count="+count);
			}else{
				System.out.println("8");
				String hql="select count(o) from order_ as o where o.status<>? and o.car=? and (o.chargeMode=? or o.chargeMode=?) and TO_DAYS(?)<=TO_DAYS(o.planBeginDate) and TO_DAYS(o.planBeginDate)<=TO_DAYS(?)";
				Object obj=getSession().createQuery(hql).setParameter(0, OrderStatusEnum.CANCELLED)
						.setParameter(1, car).setParameter(2, ChargeModeEnum.MILE).setParameter(3, ChargeModeEnum.PLANE)
						.setParameter(4, planBeginDate).setParameter(5, planEndDate).uniqueResult();
				count=Integer.parseInt(obj.toString());
			}
			System.out.println("8.1");
			orderCountList.add(count);
			System.out.println("8.2");
		}

		System.out.println("9");
		//为近期（一个月）订单数量排序做准备
		Date now = new Date();
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.DAY_OF_MONTH, -30);
		Date monthAgo = ca.getTime();
		for(Car car:tempCarList){
			System.out.println("10");
			String hql = "select count(o) from order_ as o where o.status<>? and o.car=? and TO_DAYS(?)<=TO_DAYS(o.planBeginDate) and TO_DAYS(o.planEndDate)<=TO_DAYS(?)";
			Object obj=getSession().createQuery(hql)
				.setParameter(0,OrderStatusEnum.CANCELLED).setParameter(1, car).setParameter(2, now).setParameter(3, monthAgo).uniqueResult();
			orderMonthCountList.add(Integer.parseInt(obj.toString()));
		}

		System.out.println("12");
		Collections.sort(carList,new Comparator<Car>(){
			public int compare(Car car1, Car car2) {
				System.out.println("13");
				int index1=carList.indexOf(car1);
				int index2=carList.indexOf(car2);
				if(orderCountList.get(index1)<orderCountList.get(index2))
					return -1;
				else if(orderCountList.get(index1)>orderCountList.get(index2))
					return 1;
				else if(orderMonthCountList.get(index1)<orderMonthCountList.get(index2))
					return -1;
				else if(orderMonthCountList.get(index1)>orderMonthCountList.get(index2))
					return 1;
				else
					return 0;
			}
		});

		System.out.println("before return");
		for(Car car:carList)
			System.out.println("car="+car.getPlateNumber());
		return new PageBean(pageNum, pageSize, carList.size(),carList);
	}

	/**
	 * @see com.yuqincar.dao.order.OrderDao#getOrderById(long)
	 */
	public Order getOrderById(long id) {
		return (Order) getSession().get(Order.class, id);
	}

	/**
	 * 求Map<K,V>中Value(值)的最小值对应的key
	 * 
	 * @param map
	 * @return
	 */
	private Object getMinValue(Map<Object, Double> map) {
		if (map == null)
			return null;
		Collection<Double> c = map.values();
		Object[] obj = c.toArray();
		Arrays.sort(obj);
		Iterator<Object> it = map.keySet().iterator();
		while (it.hasNext()) {
			Object keyString = it.next();
			if (map.get(keyString).equals(obj[0]))
				return keyString;
		}
		return null;
	}

	public int isCarAndDriverAvailable(Order order, Car car, User driver) {
			
		System.out.println("1");
		if (car.getStatus().equals(CarStatusEnum.SCRAPPED)) {
			return 3;
		}

		System.out.println("2");
		if(order.getServiceType()!=car.getServiceType())
			return 7;

		System.out.println("4");
		String hql = null;

		if (order.getChargeMode() == ChargeModeEnum.MILE || order.getChargeMode() == ChargeModeEnum.PLANE) {
			System.out.println("5");
			hql = "from order_ where status<>? and status<>? and status<>? and car=? and （chargeMode==? or chargeMode==?) and TO_DAYS(planBeginDate)<=TO_DAYS(?) and TO_DAYS(?)<=TO_DAYS(planEndDate)";
			List list=null;
			if(order.getId()!=null && order.getId()>0){
				hql = hql + " and id<>?";	//如果order有id值，说明是从队列调度或修改，那么需要将order排除在外。
				list=getSession().createQuery(hql)
						.setParameter(0, OrderStatusEnum.CANCELLED).setParameter(1, OrderStatusEnum.END)
						.setParameter(2, OrderStatusEnum.PAYED).setParameter(3, car)
						.setParameter(4, ChargeModeEnum.DAY).setParameter(5,ChargeModeEnum.PROTOCOL)
						.setParameter(6, order.getPlanBeginDate()).setParameter(7, order.getPlanBeginDate())
						.setParameter(8, order.getId()).list();
			}else{
				list=getSession().createQuery(hql)
						.setParameter(0, OrderStatusEnum.CANCELLED).setParameter(1, OrderStatusEnum.END)
						.setParameter(2, OrderStatusEnum.PAYED).setParameter(3, car)
						.setParameter(4, ChargeModeEnum.DAY).setParameter(5,ChargeModeEnum.PROTOCOL)
						.setParameter(6, order.getPlanBeginDate()).setParameter(7, order.getPlanBeginDate()).list();
			}
			if (list.size() > 0)
				return 2;
			
			hql = "from order_ where status<>? and status<>? and status<>? and driver=? and （chargeMode==? or chargeMode==?) and TO_DAYS(planBeginDate)<=TO_DAYS(?) and TO_DAYS(?)<=TO_DAYS(planEndDate)";
			list=null;
			if(order.getId()!=null && order.getId()>0){
				hql = hql + " and id<>?";	//如果order有id值，说明是从队列调度或修改，那么需要将order排除在外。
				list=getSession().createQuery(hql)
						.setParameter(0, OrderStatusEnum.CANCELLED).setParameter(1, OrderStatusEnum.END)
						.setParameter(2, OrderStatusEnum.PAYED).setParameter(3, driver)
						.setParameter(4, ChargeModeEnum.DAY).setParameter(5,ChargeModeEnum.PROTOCOL)
						.setParameter(6, order.getPlanBeginDate()).setParameter(7, order.getPlanBeginDate())
						.setParameter(8, order.getId()).list();
			}else{
				list=getSession().createQuery(hql)
						.setParameter(0, OrderStatusEnum.CANCELLED).setParameter(1, OrderStatusEnum.END)
						.setParameter(2, OrderStatusEnum.PAYED).setParameter(3, driver)
						.setParameter(4, ChargeModeEnum.DAY).setParameter(5,ChargeModeEnum.PROTOCOL)
						.setParameter(6, order.getPlanBeginDate()).setParameter(7, order.getPlanBeginDate()).list();
			}
			if (list.size() > 0)
				return 8;
			
		} else {
			hql = "from order_ where status<>? and status<>? and status<>? and car=? and (";
			hql = hql
					+ "(TO_DAYS(?)<=TO_DAYS(planBeginDate) and TO_DAYS(planBeginDate) <=TO_DAYS(?)) or ";
			hql = hql
					+ "(TO_DAYS(?)<=TO_DAYS(planEndDate) and TO_DAYS(planEndDate) <=TO_DAYS(?)) or ";
			hql = hql
					+ "(TO_DAYS(planBeginDate)<=TO_DAYS(?) and TO_DAYS(?) <=TO_DAYS(planEndDate))";
			hql = hql + ")";
			List list=null;
			if(order.getId()!=null && order.getId()>0){
				hql = hql + " and id<>?";	//如果order有id值，说明是从队列调度或修改，那么需要将order排除在外。
				list = getSession().createQuery(hql)
						.setParameter(0, OrderStatusEnum.CANCELLED)
						.setParameter(1, OrderStatusEnum.END)
						.setParameter(2, OrderStatusEnum.PAYED)
						.setParameter(3, car)
						.setParameter(4, order.getPlanBeginDate())
						.setParameter(5, order.getPlanEndDate())
						.setParameter(6, order.getPlanBeginDate())
						.setParameter(7, order.getPlanEndDate())
						.setParameter(8, order.getPlanBeginDate())
						.setParameter(9, order.getPlanEndDate())
						.setParameter(10, order.getId()).list();
			}else{
				list = getSession().createQuery(hql)
						.setParameter(0, OrderStatusEnum.CANCELLED)
						.setParameter(1, OrderStatusEnum.END)
						.setParameter(2, OrderStatusEnum.PAYED)
						.setParameter(3, car)
						.setParameter(4, order.getPlanBeginDate())
						.setParameter(5, order.getPlanEndDate())
						.setParameter(6, order.getPlanBeginDate())
						.setParameter(7, order.getPlanEndDate())
						.setParameter(8, order.getPlanBeginDate())
						.setParameter(9, order.getPlanEndDate()).list();
			}
			if (list.size() > 0) {
				return 2;
			}
			
			hql = "from order_ where status<>? and status<>? and status<>? and driver=? and (";
			hql = hql
					+ "(TO_DAYS(?)<=TO_DAYS(planBeginDate) and TO_DAYS(planBeginDate) <=TO_DAYS(?)) or ";
			hql = hql
					+ "(TO_DAYS(?)<=TO_DAYS(planEndDate) and TO_DAYS(planEndDate) <=TO_DAYS(?)) or ";
			hql = hql
					+ "(TO_DAYS(planBeginDate)<=TO_DAYS(?) and TO_DAYS(?) <=TO_DAYS(planEndDate))";
			hql = hql + ")";
			list=null;
			if(order.getId()!=null && order.getId()>0){
				hql = hql + " and id<>?";	//如果order有id值，说明是从队列调度或修改，那么需要将order排除在外。
				list = getSession().createQuery(hql)
						.setParameter(0, OrderStatusEnum.CANCELLED)
						.setParameter(1, OrderStatusEnum.END)
						.setParameter(2, OrderStatusEnum.PAYED)
						.setParameter(3, driver)
						.setParameter(4, order.getPlanBeginDate())
						.setParameter(5, order.getPlanEndDate())
						.setParameter(6, order.getPlanBeginDate())
						.setParameter(7, order.getPlanEndDate())
						.setParameter(8, order.getPlanBeginDate())
						.setParameter(9, order.getPlanEndDate())
						.setParameter(10, order.getId()).list();
			}else{
				list = getSession().createQuery(hql)
						.setParameter(0, OrderStatusEnum.CANCELLED)
						.setParameter(1, OrderStatusEnum.END)
						.setParameter(2, OrderStatusEnum.PAYED)
						.setParameter(3, driver)
						.setParameter(4, order.getPlanBeginDate())
						.setParameter(5, order.getPlanEndDate())
						.setParameter(6, order.getPlanBeginDate())
						.setParameter(7, order.getPlanEndDate())
						.setParameter(8, order.getPlanBeginDate())
						.setParameter(9, order.getPlanEndDate()).list();
			}
			if (list.size() > 0) {
				return 8;
			}
		}

		System.out.println("7");
		// 判断有无保养预约记录
		if (order.getChargeMode() == ChargeModeEnum.MILE || order.getChargeMode() == ChargeModeEnum.PLANE) {
			System.out.println("8");
			hql = "from CarCare where car.id=? and appointment=? and TO_DAYS(?)=TO_DAYS(date)";
			if (getSession().createQuery(hql).setParameter(0, car.getId())
					.setParameter(1, true)
					.setParameter(2, order.getPlanBeginDate()).list().size() > 0) {
				return 6;
			}
		} else {
			System.out.println("9");
			hql = "from CarCare where car.id=? and appointment=? and TO_DAYS(?)<=TO_DAYS(date) and TO_DAYS(date)<=TO_DAYS(?)";
			if (getSession().createQuery(hql).setParameter(0, car.getId())
					.setParameter(1, true)
					.setParameter(2, order.getPlanBeginDate())
					.setParameter(3, order.getPlanEndDate()).list().size() > 0) {
				return 6;
			}
		}

		System.out.println("10");
		// 判断有无维修预约记录
		if (order.getChargeMode() == ChargeModeEnum.MILE || order.getChargeMode() == ChargeModeEnum.PLANE) {
			System.out.println("11");
			hql = "from CarRepair where car.id=? and appointment=? and TO_DAYS(fromDate)<=TO_DAYS(?) and TO_DAYS(?)<=TO_DAYS(toDate)";
			if (getSession().createQuery(hql).setParameter(0, car.getId())
					.setParameter(1, true)
					.setParameter(2, order.getPlanBeginDate())
					.setParameter(3, order.getPlanBeginDate()).list().size() > 0) {
				return 4;
			}
		} else {
			System.out.println("12");
			hql = "from CarRepair where car.id=? and appointment=? and (";
			hql = hql
					+ "(TO_DAYS(fromDate)<=TO_DAYS(?) and TO_DAYS(?) <=TO_DAYS(toDate)) or ";
			hql = hql
					+ "(TO_DAYS(fromDate)<=TO_DAYS(?) and TO_DAYS(?) <=TO_DAYS(toDate)) or ";
			hql = hql
					+ "(TO_DAYS(?)<=TO_DAYS(fromDate) and TO_DAYS(toDate) <=TO_DAYS(?))";
			hql = hql + ")";
			List carRepairList = getSession().createQuery(hql)
					.setParameter(0, car.getId()).setParameter(1, true)
					.setParameter(2, order.getPlanBeginDate())
					.setParameter(3, order.getPlanBeginDate())
					.setParameter(4, order.getPlanEndDate())
					.setParameter(5, order.getPlanEndDate())
					.setParameter(6, order.getPlanBeginDate())
					.setParameter(7, order.getPlanEndDate()).list();
			if (carRepairList.size() > 0) {
				return 4;
			}
		}

		System.out.println("13");
		// 判断有无年检预约记录
		if (order.getChargeMode() == ChargeModeEnum.MILE || order.getChargeMode() == ChargeModeEnum.PLANE) {
			System.out.println("14");
			hql = "from CarExamine where car.id=? and appointment=? and TO_DAYS(?)=TO_DAYS(date)";
			if (getSession().createQuery(hql).setParameter(0, car.getId())
					.setParameter(1, true)
					.setParameter(2, order.getPlanBeginDate()).list().size() > 0) {
				return 5;
			}
		} else {
			System.out.println("15");
			hql = "from CarExamine where car.id=? and appointment=? and TO_DAYS(?)<=TO_DAYS(date) and TO_DAYS(date)<=TO_DAYS(?)";
			if (getSession().createQuery(hql).setParameter(0, car.getId())
					.setParameter(1, true)
					.setParameter(2, order.getPlanBeginDate())
					.setParameter(3, order.getPlanEndDate()).list().size() > 0) {
				return 5;
			}
		}
		System.out.println("16");
		return 0;
	}

	/**
	 * 计算地球上任意两点(经纬度)距离
	 * 
	 * @param long1
	 *            第一点经度
	 * @param lat1
	 *            第一点纬度
	 * @param long2
	 *            第二点经度
	 * @param lat2
	 *            第二点纬度
	 * @return 返回距离 单位：米
	 */
	private double straightLineDistance(double long1, double lat1,
			double long2, double lat2) {
		double a, b, R;
		R = 6378137; // 地球半径
		lat1 = lat1 * Math.PI / 180.0;
		lat2 = lat2 * Math.PI / 180.0;
		a = lat1 - lat2;
		b = (long1 - long2) * Math.PI / 180.0;
		double d;
		double sa2, sb2;
		sa2 = Math.sin(a / 2.0);
		sb2 = Math.sin(b / 2.0);
		d = 2
				* R
				* Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1)
						* Math.cos(lat2) * sb2 * sb2));
		return d;
	}

	/**
	 * 生成sn号函数
	 * 
	 * @return
	 */
	private String getSN(Order order,String baseSN) {
		if(baseSN==null){
			// 设置sn号,从数据库查当前年月的数据,如果没有,从00001开始,如果有加1即可
			String sn = null;
			Calendar cc = Calendar.getInstance();
			String yy = String.valueOf(cc.get(Calendar.YEAR)).substring(2);
			String mm = String.valueOf(cc.get(Calendar.MONTH) + 1);
			String yearMonth = (yy.length() < 2 ? "0" + yy : yy)
					+ (mm.length() < 2 ? "0" + mm : mm);
			// 通过createTime判断,降序排列
			String sql = "from order_ where date_format(createTime,'%Y-%m')=date_format(?,'%Y-%m') order by sn*1 desc";
			Query query = getSession().createQuery(sql).setParameter(0,new Date());
			List list = query.list();
			if (list.size() == 0) {
				sn = yearMonth + "00001";
			} else {
				sn = String
						.valueOf(Integer.parseInt(((Order) list.get(0)).getSn()) + 1);
			}
			return sn;
		}else{	//复制订单时，需要指定
			int intSN=Integer.parseInt(baseSN);
			intSN++;
			return String.valueOf(intSN);
		}
	}

	/**
	 * 得到司机还未执行的所有订单。按时间降序排列。所查询到的订单需满足如下要求： 1. order.car.driver==user 2.
	 * order.status==SCHEDULED
	 * 
	 * @param user
	 * @return
	 */
	public List<Order> getAllUndoOrders(User user) {
		String hql = "from order_ as o where o.driver=? and (o.status=? or o.status=?) order by o.planBeginDate desc";

		return getSession().createQuery(hql)//
				.setParameter(0, user)//
				.setParameter(1, OrderStatusEnum.SCHEDULED)//
				.setParameter(2, OrderStatusEnum.ACCEPTED)//
				.list();
	}

	public Order getUndoOrder(User user, Long orderId) {
		String hql = "from order_ as o where o.id=? and o.driver=? and o.status=?";

		return (Order) getSession().createQuery(hql)//
				.setParameter(0, orderId)//
				.setParameter(1, user)//
				.setParameter(2, OrderStatusEnum.SCHEDULED)//
				.uniqueResult();
	}

	public Order getDoneOrderDetailById(Long orderId) {
		String hql = "from order_ as o where o.id=? and o.status=?";
		return (Order) getSession().createQuery(hql)//
				.setParameter(0, orderId)//
				.setParameter(1, OrderStatusEnum.END)//
				.uniqueResult();
	}

	/**
	 * @see com.yuqincar.dao.order.OrderDao#getAllCar()
	 */
	public List<Car> getAllCar() {
		return getSession().createQuery("from Car").list();
	}

	public Order getBeginOrder(User user) {
		String hql = "from order_ as o where o.driver=? and (o.status=? or o.status=? or o.status=?)";
		List<Order> orders = getSession().createQuery(hql)
				.setParameter(0, user)
				.setParameter(1, OrderStatusEnum.BEGIN)
				.setParameter(2, OrderStatusEnum.GETON)
				.setParameter(3, OrderStatusEnum.GETOFF)
				.list();
		if (orders.size() == 1) {
			return orders.get(0);
		} else if (orders.size() > 1) {
			log.warn("order size not 1 , actual is " + orders.size());
			return orders.get(0);
		} else {
			return null;
		}
	}

	public Order getCurrentOrderByCarId(Long id) {
		return (Order) getSession()
				.createQuery("from order_ as o where o.car.id=? and o.status=?")//
				.setParameter(0, id)//
				.setParameter(1, OrderStatusEnum.BEGIN)//
				.uniqueResult();
	}

	public List<Car> getCarsWithoutOrderNow() {
		StringBuffer sb = new StringBuffer();
		sb.append("from Car as car ");
		sb.append("where car.status=? ");
		sb.append("and car not in (select o.car from order_ as o where o.status=?) ");// 没有正在执行的订单
		sb.append("and car not in (select cc.car from CarCare as cc where cc.appointment=? and TO_DAYS(NOW())-TO_DAYS(cc.date)=0) ");// 没有在同一天的预约保养
		sb.append("and car not in (select cr.car from CarRepair as cr where cr.appointment=? and TO_DAYS(NOW())-TO_DAYS(cr.fromDate)>=0) ");// 没有预约的维修
		sb.append("and car not in (select ce.car from CarExamine as ce where ce.appointment=? and TO_DAYS(NOW())-TO_DAYS(ce.date)=0))");// 没有在同一天的预约年检

		return getSession().createQuery(sb.toString())
				.setParameter(0, CarStatusEnum.NORMAL)
				.setParameter(1, OrderStatusEnum.BEGIN).setParameter(2, true)
				.setParameter(3, true).setParameter(4, true).list();
	}

	/**
	 * 根据单位名称，开始时间，结束时间查询相应的未收款订单
	 * 未收款订单的条件是，当前订单状态OrderStatusEnum为END,所属orderStatement为null
	 * 
	 * @param orgName
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Order> getUnpaidOrderByOrgNameAndTime(String orgName,
			Date beginDate, Date endDate) {

		QueryHelper helper = new QueryHelper("order_", "o");
		helper.addWhereCondition("o.orderStatement is null");
		helper.addWhereCondition("o.status = ?", OrderStatusEnum.END);
		// 设置单位名称
		if ((orgName != null) && (!"".equals(orgName))) {
			helper.addWhereCondition("o.customerOrganization.name=?", orgName);
		}
		// 设置开始时间
		if (beginDate != null) {
			helper.addWhereCondition("o.actualBeginDate>=?", beginDate);
		}
		// 设置结束时间
		if (endDate != null) {
			helper.addWhereCondition("o.actualEndDate<=?", endDate);
		}
		String hql = helper.getQueryListHql();
		Query query = getSession().createQuery(hql);
		for (int i = 0; i < helper.getParameters().size(); i++) {
			query.setParameter(i, helper.getParameters().get(i));
		}
		return query.list();
	}

	public void orderEnd(Order order,float actualMile){
		//TODO
//		order.setActualMile(actualMile);
//		int actualDay = DateUtils.elapseDays(order.getActualBeginDate(), order.getActualEndDate(),true,true);
//		order.setActualDay(actualDay);
//		order.setActualMoney(calculateOrderMoney(order.getServiceType(), order.getChargeMode(), order.getActualMile(), order.getActualDay()));
//		
//		if(order.getChargeMode().equals(ChargeModeEnum.MILE)) {
//			if(order.getOrderMile()==0){
//				order.setOrderMile(order.getActualMile());
//				order.setOrderMoney(order.getActualMoney());
//			}				
//		} else if(order.getChargeMode().equals(ChargeModeEnum.DAY) 
//				|| order.getChargeMode().equals(ChargeModeEnum.PROTOCOL)) {	//设置实际天数
//			order.setOrderMile(order.getActualMile());
//			if(order.getOrderMoney()==null || 
//					order.getOrderMoney().compareTo(new BigDecimal(0))==0)
//				order.setOrderMoney(order.getActualMoney());
//		}
//		order.setStatus(OrderStatusEnum.END);
//		save(order);
	}
		
	public List<Order> getNeedRemindProtocolOrder(){
		String hql = "from order_ as o where o.chargeMode=? and o.status=? and TO_DAYS(o.planEndDate)-TO_DAYS(?)<=7";
		return (List<Order>)getSession().createQuery(hql).setParameter(0, ChargeModeEnum.PROTOCOL)
				.setParameter(1, OrderStatusEnum.BEGIN).setParameter(2, new Date()).list();
	}
	
	public Order getEarliestOrderInQueue(){
		String hql = "from order_ as o where o.scheduling=? and o.status=? order by o.queueTime asc";
		List<Order> list=(List<Order>)getSession().createQuery(hql).setParameter(0, false)
										.setParameter(1, OrderStatusEnum.INQUEUE).list();
		if(list==null || list.size()==0)
			return null;
		else
			return list.get(0);
	}
	
	public List<Order> getToBeDeprivedSchedulingOrder(){
		Date date=new Date();
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE,-Configuration.getDepriveScheduleMinute());
		date=cal.getTime();
		String hql = "from order_ as o where o.status=? and o.scheduling=? and o.schedulingBeginTime<?";
		List list=getSession().createQuery(hql).setParameter(0, OrderStatusEnum.INQUEUE)
								.setParameter(1, true).setParameter(2, date).list();
		return (List<Order>)list;
	}
	
	public boolean canDistributeOrderToUser(User user){
		String hql = "from order_ as o where o.status=? and o.scheduler=? and o.scheduling=?";
		return getSession().createQuery(hql).setParameter(0, OrderStatusEnum.INQUEUE)
				.setParameter(1, user).setParameter(2, true).list().size()==0;
	}
	
	public Order getOrderDistributed(User user){
		String hql = "from order_ as o where o.status=? and o.scheduler=? and o.scheduling=?";
		return (Order) getSession().createQuery(hql).setParameter(0, OrderStatusEnum.INQUEUE)
				.setParameter(1, user).setParameter(2, true).uniqueResult();
	}
	
	public DayOrderDetail getDayOrderDetailByDate(Order order,Date date){
		String hql = "from DayOrderDetail as dod where dod.order=? and TO_DAYS(dod.getonDate)=TO_DAYS(?)";
		return (DayOrderDetail)getSession().createQuery(hql)
				.setParameter(0, order).setParameter(1, date).uniqueResult();
	}
}
