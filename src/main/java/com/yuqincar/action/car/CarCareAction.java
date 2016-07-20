package com.yuqincar.action.car;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarCare;
import com.yuqincar.domain.car.CarExamine;
import com.yuqincar.domain.car.CarRepair;
import com.yuqincar.domain.car.TollCharge;
import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.car.CarCareService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class CarCareAction extends BaseAction implements ModelDriven<CarCare> {
	
	private CarCare model;
	
	@Autowired
	private CarCareService carCareService;
	
	@Autowired
	private CarService carService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	
	private Date date;

	private Date date1;
	
	private Date date2;

	public String queryForm(){
		QueryHelper helper = new QueryHelper(CarCare.class, "u");
		
		if(model.getCar()!=null && model.getCar().getPlateNumber()!=null && !""
				.equals(model.getCar().getPlateNumber()))
			helper.addWhereCondition("u.car.plateNumber like ?", "%"+model.getCar().getPlateNumber()+"%");
		
		if(model.getDriver()!=null && model.getDriver().getId()!=null)
			helper.addWhereCondition("u.driver.id = ?", model.getDriver().getId());
		
		if(date1!=null && date2!=null)
			helper.addWhereCondition("(TO_DAYS(u.date)-TO_DAYS(?))>=0 and (TO_DAYS(?)-TO_DAYS(u.date))>=0", 
					date1 ,date2);
		else if(date1==null && date2!=null)
			helper.addWhereCondition("(TO_DAYS(?)-TO_DAYS(u.date))>=0", date2);
		else if(date1!=null && date2==null)
			helper.addWhereCondition("(TO_DAYS(u.date)-TO_DAYS(?))>=0", date1);
		helper.addOrderByProperty("u.id", false);
		PageBean pageBean = carCareService.queryCarCare(pageNum, helper);		
		ActionContext.getContext().getValueStack().push(pageBean);		
		ActionContext.getContext().getSession().put("carCareHelper", helper);
		return "list";	
	}

	public String list() {
		QueryHelper helper = new QueryHelper(CarCare.class, "u");
		helper.addOrderByProperty("u.id", false);
		PageBean<CarCare> pageBean = carCareService.queryCarCare(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("carCareHelper", helper);
		return "list";
	}
	
	public String freshList(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("carCareHelper");
		PageBean<CarCare> pageBean = carCareService.queryCarCare(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
	
	/** 删除 */
	public String delete() throws Exception {
		carCareService.deleteCarCareById(model.getId());
		return freshList();
	}
	
	/** 添加页面 */
	public String addUI() throws Exception {
		return "saveUI";
	}
	
	/** 添加 */
	public String add() throws Exception {
		System.out.println("in add1, model="+model);
		// 保存到数据库
		
		if(DateUtils.compareYMD(model.getDate(), new Date())>0){
			addFieldError("date", "你输入的保养日期不能晚于今天！");
			return "saveUI";
		}
		
		Car car1 = carService.getCarByPlateNumber(model.getCar().getPlateNumber());
		User driver = userService.getById(model.getDriver().getId());
		
		model.setCar(car1);
		model.setDriver(driver);
		model.setAppointment(false);
		carCareService.saveCarCare(model);
		model=null;
		ActionContext.getContext().getValueStack().push(getModel());
		System.out.println("in add1, model="+model);
		return freshList();
	}
	
	/** 修改页面 */
	public String editUI() throws Exception {
		// 准备回显的数据
		CarCare carCare = carCareService.getCarCareById(model.getId());
		if(carCare.getMoney()!=null)
			carCare.setMoney(carCare.getMoney().setScale(0, BigDecimal.ROUND_HALF_UP));
		carCare.setDate(DateUtils.getYMD(DateUtils.getYMDString(carCare.getDate())));
		ActionContext.getContext().getValueStack().push(carCare);
		
		return "saveUI";
	}
	
	/** 修改 */
	public String edit() throws Exception {
		if(DateUtils.compareYMD(model.getDate(), new Date())>0){		
			addFieldError("date", "你输入的保养日期不能晚于今天！");
			return "saveUI";
		}
		
		CarCare carCare = carCareService.getCarCareById(model.getId());

		//设置要修改的属性
		carCare.setDate(model.getDate());
		carCare.setMileInterval(model.getMileInterval());
		carCare.setMoney(model.getMoney());
		carCare.setMemo(model.getMemo());
		carCare.setAppointment(false);

		//更新到数据库
		carCareService.updateCarCare(carCare);
		model=null;
		ActionContext.getContext().getValueStack().push(getModel());
		return freshList();
	}
	
	/** 预约*/
	public String appoint() throws Exception{		
		return "saveAppoint";
	}
	
	/** 提醒*/
	public String remind(){
		PageBean pageBean=carCareService.getNeedCareCars(pageNum);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "remindList";
	}
	
	/** 详细信息*/
	public String detail() throws Exception{
		
		// 准备回显的数据
		CarCare carCare = carCareService.getCarCareById(model.getId());
		ActionContext.getContext().getValueStack().push(carCare);
		return "carCareDetail";
	}

	public CarCare getModel() {
		System.out.println("in getModel,model="+model);
		if(model==null)
			model=new CarCare();
		return model;
	}
	
	public String saveAppointment(){
		if(DateUtils.compareYMD(model.getDate(), new Date())<0){
			addFieldError("date", "你输入的预约日期不能早于今天！");
			return "saveAppoint";
		}
		
		Car car = carService.getCarByPlateNumber(model.getCar().getPlateNumber());
		User driver = userService.getById(model.getDriver().getId());
		
		//System.out.println("规范化?"+car.getBrand()+"?打工算法妨功害能"+car.getMemo());
		List<List<BaseEntity>> taskList = orderService.getCarTask(car, model.getDate(), model.getDate());
		taskList.addAll(orderService.getDriverTask(driver,  model.getDate(), model.getDate()));
		boolean haveTask=false;
		int taskType=0;	//1订单  2 保养 3 年审 4 维修
		for(List<BaseEntity> dayList: taskList){
			if(dayList!=null && dayList.size()>0){
				BaseEntity baseEntity=dayList.get(0);
				haveTask=true;
				if(baseEntity instanceof Order)
					taskType=1;
				else if(baseEntity instanceof CarCare)
					taskType=2;
				else if(baseEntity instanceof CarExamine)
					taskType=3;
				else if(baseEntity instanceof CarRepair)
					taskType=4;
				break;
			}
		}
		
		if(haveTask){
			//提醒用户不能预约保养
			String clazz=null;
			switch(taskType){
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
			addFieldError("", "添加保养记录失败！因为车辆或司机在该时间段内有"+clazz);
			return "saveAppoint";
		}else{
			//插入预约保养记录
			carCareService.carCareAppointment(car, driver, model.getDate());
			model=null;
			ActionContext.getContext().getValueStack().push(getModel());
		}

		return freshList();		
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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
}
