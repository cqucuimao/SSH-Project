package com.yuqincar.dao.order;

import java.util.Date;
import java.util.List;

import com.yuqincar.dao.common.BaseDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarServiceType;
import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.ChargeModeEnum;
import com.yuqincar.domain.order.DayOrderDetail;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.privilege.User;

public interface OrderDao extends BaseDao<Order> {

    /**
     * 得到所有的车型。
     * 
     * @return
     */
    public List<CarServiceType> getAllCarServiceType();

    /**
     * 进队列。实质是先保存订单，等待调度。 需要设置进队列时间queueTime。
     * 保存订单前，需要设置order的SN置，原则是"YYMMXXXXXX",YY表示两位年，MM表示两位月，XXXXX表示每个月的流水号，每个月从00001开始。
     * @param order
     */
    public void EnQueue(Order order,String baseSN);

    /**
     * 通过sn查询订单
     * 
     * @param sn
     * @return
     */
    public Order getOrderBySN(String sn);

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
     */
    public List<List<Order>> getCarTask(Car car, Date fromDate, Date toDate);
    
    public List<List<Order>> getDriverTask(User driver, Date fromDate, Date toDate);

    /**
     * 调度车辆。如果order.status==INQUEUE，那么就不需要再保存order，而是修改其car和driver的属性值，否则就要先保存。
     * 保存Order前，需要设置SN属性，详情请见EnQueue方法的解释。
     * 再操作前，必须要判断如下的合法性： 1. order的状态必须为null或INQUEUE才能调度。以防止已被其它调度员先调度。 2.
     * 必须判断car在时间上是空闲的（按天算）。以防止此car已被其它调度员调度。 3. 必须判断car.status==NORMAL 4.
     * 必须判断car没有在订单时间内进行维修、年审、保养（没有时间段内的CarInsurance记录,CarRepair记录,CarExamine记录）
     * 调度成功后，需要发送相同内容的短消息给司机。
     * 
     * @param order
     *            订单
     * @return  "OK" 表示成功
     */
    public String scheduleOrder(String scheduleMode,Order order, Car car, User driver, User user);

    /**
     * 得到队列中的所有订单，并按时间升序排列（距离现在时间较远的排前面）。也就是返回order.status==IN_QUEUE的所有订单。
     * 
     * @return
     */
    public List<Order> getOrderQueue();

    /**
     * 得到推荐的汽车。并将满足条件的汽车按照匹配度降序排列，可分页。 推荐汽车的原则： 1. 车型符合（需满足） 2.
     * 可用（没有报废，没有订单，没有维修、保养、年审）（需满足） 3. 距离远近（距离近的排前面） 4. 司机评价（评价好的排前面） 5.
     * 近期订单多少（一个月内，少的排前面）
     * 
     * @param pageNum
     *            分页
     * @return
     */
    public PageBean getRecommandedCar(CarServiceType serviceType, ChargeModeEnum chargeMode,
                                      Date planBeginDate, Date planEndDate, int pageNum);

    public Order getOrderById(long id);
    
    
    /**
	 * 得到司机还未执行的所有订单。按时间降序排列。所查询到的订单需满足如下要求：
	 * 1. order.car.driver==user
	 * 2. order.status==SCHEDULED
	 * @param user
	 * @return
	 */
	public List<Order> getAllUndoOrders(User user);

	public Order getUndoOrder(User user , Long orderId);

	public Order getDoneOrderDetailById(Long orderId);

    public List<Car> getAllCar();


	public Order getBeginOrder(User user);

    /**
     * 根据车辆id获得该车当前正在执行的订单
     * @param id
     * @return
     */
	public Order getCurrentOrderByCarId(Long id);

    /**
     * 根据单位名称，开始时间，结束时间查询相应的未收款订单
     * 未收款订单的条件是，当前订单状态OrderStatusEnum为END,所属orderStatement为null
     * @param orgName
     * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Order> getUnpaidOrderByOrgNameAndTime(String orgName, Date beginDate, Date endDate);
	
	/**
	 * 判断是否可以将订单调度给car。要求order中的chargeMode,planBeginDate,planEndDate不能为空。
	 * @param order
	 * @param car
	 * @return  "OK"  成功
     */
	public String isCarAndDriverAvailable(Order order, Car car, User driver);
	
	/**
	 * 
	 * @param order
	 * @param actualMile
	 */
	public void orderEnd(Order order, float actualMile);
	
	public List<Order> getNeedRemindProtocolOrder();
	
	public Order getEarliestOrderInQueue();
	
	public List<Order> getToBeDeprivedSchedulingOrder();

	public boolean canDistributeOrderToUser(User user);
	
	public Order getOrderDistributed(User user);	
	
	public DayOrderDetail getDayOrderDetailByDate(Order order,Date date);
}
