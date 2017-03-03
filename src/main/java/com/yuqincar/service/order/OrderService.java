package com.yuqincar.service.order;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarServiceType;
import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.ChargeModeEnum;
import com.yuqincar.domain.order.DayOrderDetail;
import com.yuqincar.domain.order.DriverActionVO;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.OrderOperationTypeEnum;
import com.yuqincar.domain.order.OrderStatusEnum;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.base.BaseService;
import com.yuqincar.utils.QueryHelper;

public interface OrderService extends BaseService {
	public static final String SCHEDULE_FROM_NEW="FROM_NEW";
	public static final String SCHEDULE_FROM_QUEUE="FROM_QUEUE";
	public static final String SCHEDULE_FROM_UPDATE="FROM_UPDATE";
	//public static final String SN_PREFIX="YQ";
	//public static final String SN_COOPERATION_PREFIX="W";

	/**
	 * 得到所有的车型。
	 * 
	 * @return
	 */
	public List<CarServiceType> getAllCarServiceType();

	/**
	 * 进队列。实质是先保存订单，等待调度。 需要设置进队列时间queueTime。
	 * 保存订单前，需要设置order的SN置，原则是"YYMMXXXXXX"
	 * ,YY表示两位年，MM表示两位月，XXXXX表示每个月的流水号，每个月从00001开始。
	 * 
	 * @param order
	 */
	public void EnQueue(Order order,String baseSN,int copyNumber);

	/**
	 * 得到推荐的汽车。并将满足条件的汽车按照匹配度降序排列，可分页。 推荐汽车的原则： 
	 * 1. 车型符合（需满足） 
	 * 2. 可用（没有报废，没有非里程计费订单，没有预约维修、预约保养、预约年审）（需满足）
	 * 3. 距离远近（距离近的排前面）
	 * 4. 订单少的排前面 
	 * 5. 司机评价（评价好的排前面） 
	 * 6. 近期订单多少（一个月内，少的排前面）
	 * 
	 * @param pageNum
	 *            分页
	 * @return
	 */
	public PageBean getRecommandedCar(CarServiceType serviceType,ChargeModeEnum chargeMode,
			Date planBeginDate, Date planEndDate, int pageNum);

	/**
	 * 返回车辆在一个时间段内的事务（包括订单、保养、维修、年审）。 必须要断言:fromDate!=null && toDate!=null &&
	 * fromDate<toDate(能比较大小么)
	 * 
	 * @param car
	 * @param fromDate
	 * @param toDate
	 * @return List<BaseEntity>中包含了查询到的事物对象或者null。
	 *         List的大小等于fromDate与toDate跨越的天数。
	 *         即：每个List元素对应一天的事物对象，如果这一天空闲，那么List的对应元素为null
	 *         如果有订单任务，就返回此订单对象（Order） 如果有保养任务，就返回此保养记录对象（CarCare）
	 *         如果有维修任务，就返回此维修记录对象（CarRepair） 如果有年审任务，就返回此年审记录对象（CarExamine）
	 * @throws Exception
	 */
	public List<List<Order>> getCarTask(Car car, Date fromDate, Date toDate);
	
	public List<List<Order>> getDriverTask(User driver, Date fromDate, Date toDate);

	/**
	 * 是否可以调度。用于在订单列表中判断是否可以显示“调度”按钮。
	 * 
	 * @param order
	 * @return order.status==INQUEUE
	 */
	public boolean canScheduleOrder(Order order);

	/**
	 * 调度车辆。如果order.status==INQUEUE，那么就不需要再保存order，而是修改其car和driver的属性值，否则就要先保存。
	 * 保存Order前，需要设置SN属性，详情请见EnQueue方法的解释。 再操作前，必须要判断如下的合法性： 1.
	 * order的状态必须为null或INQUEUE才能调度。以防止已被其它调度员先调度。 2.
	 * 必须判断car在时间上是空闲的（按天算）。以防止此car已被其它调度员调度。 3. 必须判断car.status==NORMAL 4.
	 * 必须判断car没有在订单时间内进行维修、年审、保养（没有时间段内的CarInsurance记录,CarRepair记录,CarExamine记录）
	 * 调度成功后，需要生成APPRemindMessage，并发送相同内容的短消息给司机。
	 * 
	 * @param order
	 *            订单
	 * @return 	"OK" 表示成功
	 */
	public String scheduleOrder(String scheduleMode,Order order,String organizationName, String customerName,Car car, User driver, int copyNumber,Order toUpdateOrder,User user);

	/**
	 * 得到队列中的所有订单，并按时间升序排列（距离现在时间较远的排前面）。也就是返回order.status==IN_QUEUE的所有订单。
	 * 
	 * @return
	 */
	public List<Order> getOrderQueue();

	/**
	 * 是否可以取消订单。用于在订单列表中判断是否可以显示“取消”按钮。
	 * 
	 * @param order
	 * @return order.status==INQUEUE || order.status==SCHEDULED || order.status==ACCEPTED
	 */
	public boolean canCancelOrder(Order order);

	/**
	 * 取消一个订单。 需要断言： order.status==INQUEUE || order.status==SCHEDULED || order.status==ACCEPTED
	 * 取消的操作，实质上是设置order.status=CANCELLED;
	 * 并且插入OrderOperationRecord记录,其type值为CANCEL
	 * 
	 * @param order
	 * @param description
	 * @return 0 成功 1 失败
	 * @throws Exception
	 */
	public int cancelOrder(Order order, User user, String description);

	/**
	 * 是否可以修改订单。用于在订单列表中判断是否可以显示“修改”按钮。
	 * 
	 * @param order
	 * @return order.status==INQUEUE || order.status==SCHEDULED
	 */
	public boolean canUpdate(Order order);

	/**
	 * 查询满足条件的订单，并分页。查询条件包括： 1. 客户单位（模糊查询，包括名称和简称都要查） 2. 司机（即姓名，模糊查询） 3.
	 * 车辆（即车牌号，模糊查询） 4.
	 * 起始时间（是个范围，凡是订单的实际开始时间在这个范围内，都应该查询出来。如果还没有开始，那么就查询计划开始时间。） 5.
	 * 状态（OrderStatusEnum所定义的所有可能） 以上查询条件是And的关系。
	 * 
	 * @param pageNum
	 * @param helper
	 * @return
	 */
	
	public boolean canAddProtocolOrderPayOrder(Order order);

	/**
	 * 该账单是否为协议订单。用于判断列表中是否显示“添加分期收款单”按钮
	 * @param pageNum
	 * @param helper
	 * @return
	 */
	public PageBean<Order> queryOrder(int pageNum, QueryHelper helper);
	
	public List<Order> queryAllOrder(QueryHelper helper);
	
	/**
	 * 是否可以延后订单。
	 * 
	 * @param order
	 * @return order.chargeMode==DAY && order.staus==BEGIN
	 */
	public boolean canOrderEndPostpone(Order order);

	/**
	 * 延后订单。 
	 * 1.修改planEndDate为endDate 
	 * 2.插入一条OrderOperationRecord记录：
	 * 	2.1 其中的type属性设置为END_POSTPONE 
	 * 	2.2 其中的description属性需要在description参数的前面加上 延后结束：
	 * 		将结束时间从 <YYYY-MM-DD> 改为 <YYYY-MM-DD> 
	 * 3.需要断言: order.chargeMode==DAY && planEndDate<endDate && order.status==BEGIN
	 * 
	 * @param order
	 * @param endDate
	 * @param description
	 * @return "OK" 表示成功
	 */
	public String orderEndPostpone(Order order, Date endDate, String description, User user);

	/**
	 * 是否可以重新调度。只有到order.status==BEGIN时才可以。
	 * 
	 * @param order
	 * @return 
	 */
	public boolean canOrderReschedule(Order order);

	/**
	 * 重新调度。 
	 * 1.修改car属性为car参数。 
	 * 2.插入一条OrderOperationRecord记录： 
	 * 	2.1 其中的type属性设置为CHANGE_CAR 
	 * 	2.2 其中的description属性需要在description参数的前面加上 更换车辆： 将 <车牌号>（司机姓名） 改为 <车牌号>（司机姓名） 
	 * 3.需要断言：order.status==BEGIN 同时，car必须满足isCarAvailable中对车辆的要求。
	 * 
	 * @param order
	 * @param car
	 * @param user
	 * @return "OK" 表示成功
	 */
	public String orderReschedule(Order order, Car car, User driver, User user,String description);

	public Order getOrderById(long id);

	public Order getOrderBySN(String sn);

	public void save(Order order);

	public List<Car> getAllCar();

	public void update(Order order);

	/**
	 * 根据车辆id查询当前正在执行的订单
	 * 
	 * @param id
	 * @return
	 */
	public Order getCurrentOrderByCarId(Long id);

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
			Date beginDate, Date endDate);
	
	/**
	 * 获取执行订单车辆的轨迹
	 * @param order
	 * @return
	 */
	public String getOrderTrackAbstract(String sn, Date beginDate, Date endDate);
	
	/**
	 * 获取计费方式的中文说明
	 */
	public String getChargeModeString(ChargeModeEnum chargeMode);
	
	/**
	 * 获取订单操作记录类型的中文说明
	 */
	public String getOperationRecordTypeString(OrderOperationTypeEnum type);
	
	/**
	 * 判断是否可以将订单调度给car。要求order中的chargeMode,planBeginDate,planEndDate不能为空。
	 * @param order
	 * @param car
	 * @return
	 */
	public String isCarAndDriverAvailable(Order order, Car car, User driver);
	
	public List<Order> getNeedRemindProtocolOrder();
	
	/**
	*用来为调度员分配一个需要调度的订单。
	*在status为INQUEUE，并且scheduling为false的订单中，找到queueTime最早的订单，并返回。如果没有这样的订单，则返回null。
	*如果有需要返回的Order，则设置scheduling为true，scheduler为scheduler，schedulingBeginTime为当前日期。
	*如果是在街面上选取的某个订单，那么就将这个订单传入，否则传入null。
	*/
	public Order distributeOrder(User scheduler, Order order);
	
	public boolean canDistributeOrderToUser(User user);
	
	public Order getOrderDistributed(User user);
	
	/**
	 * 处理客户单位，客户，电话号码。根据它们是否已经出现在数据库中，来决定是新建、修改或者不做任何处理。
	 * @param customerOrganizationName
	 * @param customerName
	 * @param phoneNumber
	 * @return 依次分别是：客户单位、客户
	 */
	public List<BaseEntity> dealCCP(String customerOrganizationName,String customerName,String phoneNumber);
	
	public DayOrderDetail getDayOrderDetailByDate(Order order,Date date);
	
	/**
	 * 结算订单。根据订单开始时间、上车时间、下车时间、结束时间，计算每天的实际公里、收费公里，以及整个订单的收费情况。
	 * @param order
	 */
	public void orderCheckout(Order order);
	
	public Order getProtocolOrderByCar(Car car);
	
	public List<Order> getToBeDeprivedSchedulingOrder();
	
	/********************************************
	 * 调度人员编辑司机动作
	 ********************************************/
	
	public boolean canEditDriverAction(Order order);
	
	public List<DriverActionVO> getDriverActions(Order order);
	
	public void EditDriverAction(String actionId, Date date, User user);
	
	public void deleteDriverAction(String actionId,User user);
	
	public boolean canAddAcceptAction(Order order);
	
	public boolean canAddBeginAction(Order order);
	
	public boolean canAddGetonAction(Order order); 
	
	public boolean canAddGetoffAction(Order order);
	
	public boolean canAddEndAction(Order order);
	
	public void addAcceptAction(Order order,User user,Date date);
	
	public void addBeginAction(Order order,User user,Date date);
	
	public void addGetonAction(Order order,User user,Date date);
	
	public void addGetoffAction(Order order,User user,Date date);
	
	public void addEndAction(Order order,User user,Date date);
	
	/********************************************
	 * 编辑派车单
	 ********************************************/
	public boolean canEditOrderBill(Order order);
	
	public void editOrderBill(Order order, User user);
	
	public void deleteDayOrderDetail(long id);
	
	public void saveDayOrderDetail(DayOrderDetail dod);
}
