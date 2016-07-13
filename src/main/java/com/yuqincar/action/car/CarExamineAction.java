package com.yuqincar.action.car;

import java.util.Date;
import java.util.List;

import org.springframework.aop.ThrowsAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarCare;
import com.yuqincar.domain.car.CarExamine;
import com.yuqincar.domain.car.CarRepair;
import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.car.CarExamineService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class CarExamineAction extends BaseAction implements ModelDriven<CarExamine> {
	
	private CarExamine model = new CarExamine();
	
	@Autowired
	private CarExamineService carExamineService;
	
	@Autowired
	private CarService carService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	
	private String plateNumber;
	
	private Date recentExamineDate;
	
	/** 列表 */
	public String list() throws Exception {
		QueryHelper helper = new QueryHelper(CarExamine.class, "ce");
		
		if(model.getCar()!=null && model.getCar().getPlateNumber()!=null && !"".equals(model.getCar().getPlateNumber()))
			helper.addWhereCondition("ce.car.plateNumber like ?", "%"+model.getCar().getPlateNumber()+"%");
		
		if(model.getDriver()!=null && model.getDriver().getId()!=null)
			helper.addWhereCondition("ce.driver.id = ?", model.getDriver().getId());

		System.out.println(helper.getQueryListHql());
		
		PageBean pageBean = carExamineService.queryCarExamine(pageNum, helper);
		
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("carExamineHelper", helper);
		return "list";
	}
	
	/**获取下次年审的时间 */
	public void getNextExamineDate(){
		System.out.println("in getNextExamineDate!!!");
		System.out.println("recentExamineDate="+recentExamineDate);
		System.out.println("plateNumber="+plateNumber);
		Car car = carService.getCarByPlateNumber(plateNumber);
		Date nextExamineDate = carExamineService.getNextExamineDate(car, recentExamineDate);
		NextExamineDateVO nedvo = new NextExamineDateVO();
		nedvo.setNextExamineDate(nextExamineDate);
		
		String json = JSON.toJSONString(nedvo);
		writeJson(json);
	}
	
	/** 删除 */
	public String delete() throws Exception {
		carExamineService.deleteCarExamineById(model.getId());
		return "toList";
	}
	
	/** 添加页面 */
	public String addUI() throws Exception {
		return "saveUI";
	}
	
	/** 添加 */
	public String add() throws Exception {
		// 保存到数据库
		boolean before = model.getNextExamineDate().before(model.getDate());
		//System.out.println(before);
		if(before){
		
			//addActionError("预约时间必须晚于今天！");
			addFieldError("examineIntervalYear", "你输入的下次年审时间不能早于年审时间！");
			//ActionContext.getContext().put("date", "预约时间必须晚于今天！");
			return "saveUI";
		}
		
		Car car1 = carService.getCarByPlateNumber(model.getCar().getPlateNumber());
		User driver = userService.getById(model.getDriver().getId());
		
		model.setCar(car1);
		model.setDriver(driver);
		model.setAppointment(false);
		carExamineService.saveCarExamine(model);
		return "toList";
	}
	
	/** 修改页面 */
	public String editUI() throws Exception {
		// 准备回显的数据
		CarExamine carExamine = carExamineService.getCarExamineById(model.getId());
		
		carExamine.setDate(DateUtils.getYMD(DateUtils.getYMDString(carExamine.getDate())));
		carExamine.setNextExamineDate(DateUtils.getYMD(DateUtils.getYMDString(carExamine.getNextExamineDate())));
		
		ActionContext.getContext().getValueStack().push(carExamine);

		return "saveUI";
	}
	
	/** 修改 */
	public String edit() throws Exception {
		//从数据库中取出原对象
		
		boolean before = model.getNextExamineDate().before(model.getDate());
		//System.out.println(before);
		if(before){
		
			//addActionError("预约时间必须晚于今天！");
			addFieldError("examineIntervalYear", "你输入的下次年审时间不能早于年审时间！");
			//ActionContext.getContext().put("date", "预约时间必须晚于今天！");
			return "saveUI";
		}
		
		CarExamine carExamine = carExamineService.getCarExamineById(model.getId());
		Car car=carService.getCarByPlateNumber(model.getCar().getPlateNumber());
		User driver=userService.getById(model.getDriver().getId());

		//设置要修改的属性
		carExamine.setCar(car);
		carExamine.setDriver(driver);
		carExamine.setDate(model.getDate());
		carExamine.setNextExamineDate(model.getNextExamineDate());
		carExamine.setMoney(model .getMoney());
		carExamine.setMemo(model .getMemo());
		carExamine.setAppointment(false);

		//更新到数据库
		carExamineService.updateCarExamine(carExamine);

		return "toList";
	}
	
	/** 登记*/
	public String register() throws Exception{
		return "saveUI";
	}
	
	/** 预约*/
	public String appoint() throws Exception{
		return "saveAppoint";
	}
	
	/** 提醒*/
	public String remind() throws Exception{
		QueryHelper helper = new QueryHelper(CarExamine.class, "ce");
		
		if(model.getCar()!=null && model.getCar().getPlateNumber()!=null && !"".equals(model.getCar().getPlateNumber()))
			helper.addWhereCondition("ce.car.plateNumber like ?", "%"+model.getCar().getPlateNumber()+"%");
		
		PageBean pageBean = carExamineService.queryCarExamine(pageNum, helper);
		
		ActionContext.getContext().getValueStack().push(pageBean);
		return "remindList";
	}
	
	/** 详细信息*/
	public String detail() throws Exception{
		
		// 准备回显的数据
		CarExamine carExamine = carExamineService.getCarExamineById(model.getId()); 
		ActionContext.getContext().getValueStack().push(carExamine);
		
		return "carExamineDetail";
	}
	

	public CarExamine getModel() {
		// TODO Auto-generated method stub
		return model;
	}
	
	public String examine(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("carExamineHelper");
		PageBean pageBean = carExamineService.queryCarExamine(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
	
	public String saveAppointment(){
		Date today = new Date();
		//System.out.println(today);
		boolean before = model.getDate().before(today);
		//System.out.println(before);
		if(before){
		
			//addActionError("预约时间必须晚于今天！");
			addFieldError("date", "你输入的预约时间不能早于今天！");
			//ActionContext.getContext().put("date", "预约时间必须晚于今天！");
			return "saveAppoint";
		}
		
		Car car = carService.getCarByPlateNumber(model.getCar().getPlateNumber());
		User driver = userService.getById(model.getDriver().getId());
		List<List<BaseEntity>> taskList = orderService.getCarTask(car, model.getDate(), model.getDate());
		taskList.addAll(orderService.getDriverTask(driver,  model.getDate(), model.getDate()));
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
			carExamineService.carExamineAppointment(car, driver, model.getDate());
		}
		return "toList";
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
	
	public Date getRecentExamineDate() {
		return recentExamineDate;
	}

	public void setRecentExamineDate(Date recentExamineDate) {
		this.recentExamineDate = recentExamineDate;
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
	

}
