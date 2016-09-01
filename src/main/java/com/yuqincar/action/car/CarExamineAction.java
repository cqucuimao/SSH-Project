package com.yuqincar.action.car;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.dao.car.CarExamineDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarCare;
import com.yuqincar.domain.car.CarExamine;
import com.yuqincar.domain.car.CarRepair;
import com.yuqincar.domain.car.CarStatusEnum;
import com.yuqincar.domain.car.CarViolation;
import com.yuqincar.domain.car.TollCharge;
import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.car.CarExamineService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.car.CarViolationService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class CarExamineAction extends BaseAction implements ModelDriven<CarExamine> {
	
	private CarExamine model;
	
	@Autowired
	private CarExamineService carExamineService;
	
	@Autowired
	private CarService carService;
	
	@Autowired
	CarExamineDao carExaminedao;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private CarViolationService carViolationService;
	
	@Autowired
	private UserService userService;
	
	private Date recentExamineDate;
	
	private Date date1;
	
	private Date date2;
	
	private Long carId;
	
	private Date payDate;
	
	private BigDecimal tollChargeMoney;
	
	private BigDecimal overdueFine;
	
	private BigDecimal moneyForCardReplace;
	
	private Date nextPayDate;
	
	private String actionFlag;
	
	private String yearMonth;
	
	public String queryForm() throws Exception {
		QueryHelper helper = new QueryHelper(CarExamine.class, "ce");
		
		if(model.getCar()!=null)
			helper.addWhereCondition("ce.car=?", model.getCar());
		
		if(model.getDriver()!=null)
			helper.addWhereCondition("ce.driver=?", model.getDriver());
		
		if(date1!=null && date2!=null)
			helper.addWhereCondition("(TO_DAYS(ce.date)-TO_DAYS(?))>=0 and (TO_DAYS(?)-TO_DAYS(ce.date))>=0", 
					date1 ,date2);
		else if(date1==null && date2!=null)
			helper.addWhereCondition("(TO_DAYS(?)-TO_DAYS(ce.date))>=0", date2);
		else if(date1!=null && date2==null)
			helper.addWhereCondition("(TO_DAYS(ce.date)-TO_DAYS(?))>=0", date1);
		helper.addWhereCondition("ce.appointment=?", false);
		helper.addOrderByProperty("ce.id", false);		
		PageBean<CarExamine> pageBean = carExamineService.queryCarExamine(pageNum, helper);		
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("carExamineHelper", helper);
		return "list";
	}
	
	public String queryAppointForm() throws Exception {
		QueryHelper helper = new QueryHelper(CarExamine.class, "ce");
		
		if(model.getCar()!=null)
			helper.addWhereCondition("ce.car=?", model.getCar());
		
		if(model.getDriver()!=null)
			helper.addWhereCondition("ce.driver=?", model.getDriver());
		
		if(date1!=null && date2!=null)
			helper.addWhereCondition("(TO_DAYS(ce.date)-TO_DAYS(?))>=0 and (TO_DAYS(?)-TO_DAYS(ce.date))>=0", 
					date1 ,date2);
		else if(date1==null && date2!=null)
			helper.addWhereCondition("(TO_DAYS(?)-TO_DAYS(ce.date))>=0", date2);
		else if(date1!=null && date2==null)
			helper.addWhereCondition("(TO_DAYS(ce.date)-TO_DAYS(?))>=0", date1);
		helper.addWhereCondition("ce.appointment=?", true);
		helper.addOrderByProperty("ce.id", false);		
		PageBean<CarExamine> pageBean = carExamineService.queryCarExamine(pageNum, helper);		
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("carExamineAppointHelper", helper);
		return "appointList";
	}

	public String list() {
		QueryHelper helper = new QueryHelper(CarExamine.class, "ce");
		helper.addWhereCondition("ce.appointment=?", false);
		if(carId!=null)
			helper.addWhereCondition("ce.car.id=?", carId);
		helper.addOrderByProperty("ce.id", false);
		PageBean<CarExamine> pageBean = carExamineService.queryCarExamine(pageNum, helper);	
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("carExamineHelper", helper);
		return "list";
	}
	
	public String appointList() {
		QueryHelper helper = new QueryHelper(CarExamine.class, "ce");
		helper.addWhereCondition("ce.appointment=?", true);
		helper.addOrderByProperty("ce.id", false);
		PageBean<CarExamine> pageBean = carExamineService.queryCarExamine(pageNum, helper);	
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("carExamineAppointHelper", helper);
		return "appointList";
	}
	
	public String freshList(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("carExamineHelper");
		PageBean<CarExamine> pageBean = carExamineService.queryCarExamine(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
	
	public String freshAppointList(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("carExamineAppointHelper");
		PageBean<CarExamine> pageBean = carExamineService.queryCarExamine(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "appointList";
	}
	
	/**获取下次年审的时间 */
	public void getNextExamineDate(){
		Car car = carService.getCarById(carId);
		Date nextExamineDate = carExamineService.getNextExamineDate(car, recentExamineDate);
		NextExamineDateVO nedvo = new NextExamineDateVO();
		nedvo.setNextExamineDate(nextExamineDate);
		
		String json = JSON.toJSONString(nedvo);
		writeJson(json);
	}
	
	public String delete() throws Exception {
		carExamineService.deleteCarExamineById(model.getId());
		return freshList();
	}
	
	public String deleteAppointment() throws Exception {
		carExamineService.deleteCarExamineById(model.getId());
		return freshAppointList();
	}
	
	public String addUI() throws Exception {
		System.out.println("actionFlag="+actionFlag);
		ActionContext.getContext().put("actionFlag", actionFlag);
		return "saveUI";
	}
	
	public String add() throws Exception {		
		if(DateUtils.compareYMD(model.getDate(), new Date())>0){
			addFieldError("date", "你输入的年审日期不能晚于今天！");
			return "saveUI";
		}
		
		boolean before = model.getNextExamineDate().before(model.getDate());
		if(before){		
			addFieldError("examineIntervalYear", "你输入的下次年审时间不能早于年审时间！");
			return "saveUI";
		}
		
		model.setAppointment(false);
		
		TollCharge tollCharge = new TollCharge();
		tollCharge.setCar(model.getCar());
		tollCharge.setPayDate(payDate);
		tollCharge.setMoney(tollChargeMoney);
		tollCharge.setNextPayDate(nextPayDate);
		tollCharge.setOverdueFine(overdueFine);
		tollCharge.setMoneyForCardReplace(moneyForCardReplace);
		
		carExamineService.saveCarExamine(model,tollCharge);
		model=null;
		ActionContext.getContext().getValueStack().push(getModel());
		return freshList();
	}
	
	public String editUI() throws Exception {
		// 准备回显的数据
		CarExamine carExamine = carExamineService.getCarExamineById(model.getId());
		carExamine.setDate(DateUtils.getYMD(DateUtils.getYMDString(carExamine.getDate())));
		carExamine.setNextExamineDate(DateUtils.getYMD(DateUtils.getYMDString(carExamine.getNextExamineDate())));
		
		ActionContext.getContext().getValueStack().push(carExamine);

		return "saveUI";
	}
	
	public String editAppointmentUI(){
		// 准备回显的数据
		CarExamine carExamine = carExamineService.getCarExamineById(model.getId());
		carExamine.setDate(DateUtils.getYMD(DateUtils.getYMDString(carExamine.getDate())));
		ActionContext.getContext().getValueStack().push(carExamine);

		return "saveAppoint";
	}
	
	public String edit() throws Exception {		
		if(DateUtils.compareYMD(model.getDate(), new Date())>0){
			addFieldError("date", "你输入的年审日期不能晚于今天！");
			return "saveUI";
		}
		
		boolean before = model.getNextExamineDate().before(model.getDate());
		if(before){
			addFieldError("examineIntervalYear", "你输入的下次年审时间不能早于年审时间！");
			return "saveUI";
		}
		
		CarExamine carExamine = carExamineService.getCarExamineById(model.getId());

		//设置要修改的属性
		carExamine.setCar(model.getCar());
		carExamine.setDriver(model.getDriver());
		carExamine.setDate(model.getDate());
		carExamine.setNextExamineDate(model.getNextExamineDate());
		carExamine.setMoney(model .getMoney());
		carExamine.setMemo(model .getMemo());

		//更新到数据库
		carExamineService.updateCarExamine(carExamine);
		model=null;
		ActionContext.getContext().getValueStack().push(getModel());

		return freshList();
	}
	
	public String register() throws Exception{
		return "saveUI";
	}
	
	public String appoint() throws Exception{
		return "saveAppoint";
	}
	
	/** 提醒*/
	public String remind() throws Exception{
		QueryHelper helper = new QueryHelper(Car.class, "c");
		Date now=new Date();
		Date b = new Date(now.getTime() +  24*60*60*1000 * 15L );
		helper.addWhereCondition("c.nextExaminateDate < ? and c.status=? and c.borrowed=?", b,CarStatusEnum.NORMAL,false);
		helper.addOrderByProperty("c.nextExaminateDate", true);	
		PageBean<Car> pageBean=carExamineService.getNeedExamineCars(pageNum,helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("carHelper1", helper);
		return "remindList";
	}
	
	//查询提醒
	public String queryRemind() throws Exception{
		QueryHelper helper = new QueryHelper(Car.class, "c");
		Date now=new Date();
		Date b = new Date(now.getTime() +  24*60*60*1000 * 15L );
		System.out.println("b="+b);
		helper.addWhereCondition("c.nextExaminateDate < ? and c.status=? and c.borrowed=?", b,CarStatusEnum.NORMAL,false);	
		if(yearMonth != null && yearMonth.length()>0){
			String ymd = yearMonth + "-01";
			Date d1= DateUtils.getYMD(ymd);
			Date d2 = DateUtils.getEndDateOfMonth(d1);
			System.out.println("d1="+d1);
			System.out.println("d2="+d2);
			helper.addWhereCondition("(TO_DAYS(c.nextExaminateDate) - TO_DAYS(?)) >=0 and (TO_DAYS(c.nextExaminateDate) - TO_DAYS(?)) <= 0", d1,d2);
		}		
		helper.addOrderByProperty("c.nextExaminateDate", true);	
		PageBean<Car> pageBean=carExamineService.getNeedExamineCars(pageNum,helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("carHelper1", helper);
		return "remindList";
	}
	
	public String freshRemind(){
		
		QueryHelper helper = (QueryHelper)ActionContext.getContext().getSession().get("carHelper1");
		PageBean<Car> pageBean=carExamineService.getNeedExamineCars(pageNum,helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "remindList";
	}
	
	public String getProtocolInfo(){
		String protocolInfo = "";
		Car car=(Car)ActionContext.getContext().getValueStack().peek();
		Order order = orderService.getProtocolOrderByCar(car);
		if(order != null){
			protocolInfo = order.getCustomerOrganization().getName()+"("+order.getCustomer().getName()+" "+order.getPhone()+")";
		}
		return protocolInfo;
	}
	
	public String getCarViolationCount(){
		System.out.println("++++++++");
		String carViolationCount = "";
		Car car=(Car)ActionContext.getContext().getValueStack().peek();
		List<CarViolation> carViolations = carViolationService.getCarViolationByCar(car);
		System.out.println("&&&&&&&&&");
		if(carViolations != null && carViolations.size()>0){
			carViolationCount = carViolations.size()+"";
		}
		return carViolationCount;
	}
	
	/** 详细信息*/
	public String detail() throws Exception{
		
		// 准备回显的数据
		CarExamine carExamine = carExamineService.getCarExamineById(model.getId()); 
		ActionContext.getContext().getValueStack().push(carExamine);
		
		return "carExamineDetail";
	}
	

	public CarExamine getModel() {
		if(model==null)
			model = new CarExamine();
		return model;
	}
	
	public String saveAppointment(){		
		List<List<BaseEntity>> taskList = orderService.getCarTask(model.getCar(), model.getDate(), model.getDate());
		taskList.addAll(orderService.getDriverTask(model.getDriver(),  model.getDate(), model.getDate()));
		boolean haveTask=false;
		int taskType=0;  //1订单  2 保养 3 年审 4 维修
		for(List<BaseEntity> dayList:taskList){
			if(dayList!=null && dayList.size()>0){
				BaseEntity baseEntity = dayList.get(0);
				haveTask=true;
				if(baseEntity instanceof Order)
					taskType=1;
				else if (baseEntity instanceof CarCare)
					taskType=2;
				else if(baseEntity instanceof CarExamine)
					taskType=3;
				else if(baseEntity instanceof CarRepair)
					taskType=4;
				break;
			}
		}
		
		if(haveTask){
			String clazz=null;
			switch (taskType) {
			case 1:
				clazz="订单";
				break;
			case 2:
				clazz="保养记录";
				break;
			case 3:
				clazz="年审记录";
				break;
			case 4:
				clazz="维修记录";
				break;
			}
			addFieldError("", "添加年审记录失败！因为车辆或司机在该时间段内有"+clazz);
			return "saveAppoint";
		}else {
			CarExamine carExamine=new CarExamine();
			carExamine.setCar(model.getCar());
			carExamine.setDriver(model.getDriver());
			carExamine.setDate(model.getDate());
			carExamine.setAppointment(true);
			carExamine.setDone(model.isDone());
			carExamineService.saveAppointment(carExamine);
			model=null;
			ActionContext.getContext().getValueStack().push(getModel());
		}
		return freshAppointList();
	}
	
	public String editAppointment(){		
		Date date1=carExamineService.getCarExamineById(model.getId()).getDate();
		Date date2=model.getDate();
		if(!DateUtils.getYMDString(date1).equals(DateUtils.getYMDString(date2))){
			List<List<BaseEntity>> taskList = orderService.getCarTask(model.getCar(), model.getDate(), model.getDate());
			taskList.addAll(orderService.getDriverTask(model.getDriver(),  model.getDate(), model.getDate()));
			int taskType=0;  //1订单  2 保养 3 年审 4 维修
			for(List<BaseEntity> dayList:taskList){
				if(dayList!=null && dayList.size()>0){
					BaseEntity baseEntity = dayList.get(0);
					if(baseEntity instanceof Order)
						taskType=1;
					else if (baseEntity instanceof CarCare)
						taskType=2;
					else if(baseEntity instanceof CarExamine){
						if(baseEntity.getId().equals(model.getId())) //将正在修改的年审预约记录排除在外
							continue;
						taskType=3;
					}
					else if(baseEntity instanceof CarRepair)
						taskType=4;
					break;
				}
			}
			
			if(taskType!=0){
				String clazz=null;
				switch (taskType) {
				case 1:
					clazz="订单";
					break;
				case 2:
					clazz="保养记录";
					break;
				case 3:
					clazz="年审记录";
					break;
				case 4:
					clazz="维修记录";
					break;
				}
				addFieldError("", "添加年审记录失败！因为车辆或司机在该时间段内有"+clazz);
				return "saveAppoint";
			}
		}
			
		CarExamine carExamine=carExamineService.getCarExamineById(model.getId());
		carExamine.setCar(model.getCar());
		carExamine.setDriver(model.getDriver());
		carExamine.setDate(model.getDate());
		carExamine.setAppointment(true);
		carExamine.setDone(model.isDone());
		carExamineService.saveAppointment(carExamine);
		model=null;
		ActionContext.getContext().getValueStack().push(getModel());
		return freshAppointList();
	}
	
	public Date getRecentExamineDate() {
		return recentExamineDate;
	}

	public void setRecentExamineDate(Date recentExamineDate) {
		this.recentExamineDate = recentExamineDate;
	}
	
	public Date getDate1() {
		return date1;
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
	}

	public Date getDate2() {
		return date2;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
	}

	class NextExamineDateVO{
		private Date nextExamineDate;

		public Date getNextExamineDate() {
			return nextExamineDate;
		}

		public void setNextExamineDate(Date nextExamineDate) {
			this.nextExamineDate = nextExamineDate;
		}		
	}

	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public BigDecimal getTollChargeMoney() {
		return tollChargeMoney;
	}

	public void setTollChargeMoney(BigDecimal tollChargeMoney) {
		this.tollChargeMoney = tollChargeMoney;
	}

	public BigDecimal getOverdueFine() {
		return overdueFine;
	}

	public void setOverdueFine(BigDecimal overdueFine) {
		this.overdueFine = overdueFine;
	}

	public BigDecimal getMoneyForCardReplace() {
		return moneyForCardReplace;
	}

	public void setMoneyForCardReplace(BigDecimal moneyForCardReplace) {
		this.moneyForCardReplace = moneyForCardReplace;
	}

	public Date getNextPayDate() {
		return nextPayDate;
	}

	public void setNextPayDate(Date nextPayDate) {
		this.nextPayDate = nextPayDate;
	}

	public String getActionFlag() {
		return actionFlag;
	}

	public void setActionFlag(String actionFlag) {
		this.actionFlag = actionFlag;
	}

	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

}
