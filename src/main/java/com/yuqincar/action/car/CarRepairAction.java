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
import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.car.CarRepairService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class CarRepairAction extends BaseAction implements ModelDriven<CarRepair> {
	
	private CarRepair model;
	
	@Autowired
	private CarRepairService carRepairService;
	
	@Autowired
	private CarService carService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	
	private Date date1;
	
	private Date date2;
	
	public String queryForm() throws Exception {
		QueryHelper helper = new QueryHelper(CarRepair.class, "cr");
		
		if(model.getCar()!=null && model.getCar().getPlateNumber()!=null 
				&& !"".equals(model.getCar().getPlateNumber()))
			helper.addWhereCondition("cr.car.plateNumber like ?", "%"+model.getCar().getPlateNumber()+"%");
		
		if(model.getDriver()!=null && model.getDriver().getId()!=null)
			helper.addWhereCondition("cr.driver.id = ?", model.getDriver().getId());

		if(date1!=null && date2!=null)
			helper.addWhereCondition("(TO_DAYS(cr.fromDate)-TO_DAYS(?))>=0 and (TO_DAYS(?)-TO_DAYS(cr.fromDate))>=0", 
					date1 ,date2);
		else if(date1==null && date2!=null)
			helper.addWhereCondition("(TO_DAYS(?)-TO_DAYS(cr.fromDate))>=0", date2);
		else if(date1!=null && date2==null)
			helper.addWhereCondition("(TO_DAYS(cr.fromDate)-TO_DAYS(?))>=0", date1);
		helper.addOrderByProperty("cr.id", false);		
		PageBean<CarRepair> pageBean = carRepairService.queryCarRepair(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("carRepairHelper", helper);
		return "list";
	}
	
	public String list() {
		QueryHelper helper = new QueryHelper(CarRepair.class, "cr");
		helper.addOrderByProperty("cr.id", false);
		PageBean<CarRepair> pageBean = carRepairService.queryCarRepair(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("carRepairHelper", helper);
		return "list";
	}
	
	public String freshList(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("carRepairHelper");
		PageBean<CarRepair> pageBean = carRepairService.queryCarRepair(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
	
	public String delete() throws Exception {
		carRepairService.deleteCarRepairById(model.getId());
		return freshList();
	}
	
	/** 添加页面 */
	public String addUI() throws Exception {
		return "saveUI";
	}
	
	/** 添加 */
	public String add() throws Exception {

		if(DateUtils.compareYMD(model.getToDate(), new Date())>0){
			addFieldError("toDate", "你输入的维修时间段不能晚于今天！");
			return "saveUI";
		}
		
		boolean before = model.getToDate().before(model.getFromDate());
		if(before){
			addFieldError("toDate", "你输入的截止时间不能早于起始时间！");
			return "saveUI";
		}
		
		Car car1 = carService.getCarByPlateNumber(model.getCar().getPlateNumber());
		User driver=userService.getById(model.getDriver().getId());
		
		model.setCar(car1);
		model.setDriver(driver);
		model.setAppointment(false);
		carRepairService.saveCarRepair(model);
		model=null;
		ActionContext.getContext().getValueStack().push(getModel());
		return freshList();
	}
	
	public String saveAppointment(){
		if(DateUtils.compareYMD(new Date(), model.getFromDate())>0){
			addFieldError("toDate", "你输入的维修时间段不能早于今天！");
			return "saveAppoint";
		}
		
		boolean before = model.getToDate().before(model.getFromDate());
		if(before){
			addFieldError("toDate", "你输入的截止时间不能早于起始时间！");
			return "saveAppoint";
		}
		
		Car car = carService.getCarByPlateNumber(model.getCar().getPlateNumber());
		User driver = userService.getById(model.getDriver().getId());
		List<List<BaseEntity>> taskList = orderService.getCarTask(car, model.getFromDate(), model.getToDate());
		taskList.addAll(orderService.getDriverTask(driver,  model.getFromDate(), model.getToDate()));
		boolean haveTask = false;
		int taskType = 0;
		for(List<BaseEntity> dayList:taskList){
			if(dayList!=null && dayList.size()>0){
				BaseEntity baseEntity = dayList.get(0);
				haveTask = true;  //1订单  2 保养 3 年审 4 维修
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
			addFieldError("", "添加维修记录失败！因为车辆或司机在该时间段内有"+clazz);
			return "saveAppoint";
		}else{
			carRepairService.carRepairAppointment(car, driver, model.getFromDate(), model.getToDate());
			model=null;
			ActionContext.getContext().getValueStack().push(getModel());
		}

		return freshList();
	}
	
	/** 修改页面 */
	public String editUI() throws Exception {
		// 准备回显的数据
		
		CarRepair carRepair = carRepairService.getCarRepairById(model.getId());
		
		if(carRepair.getMoney()!=null)
			carRepair.setMoney(carRepair.getMoney().setScale(0, BigDecimal.ROUND_HALF_UP));
		carRepair.setFromDate(DateUtils.getYMD(DateUtils.getYMDString(carRepair.getFromDate())));
		carRepair.setToDate(DateUtils.getYMD(DateUtils.getYMDString(carRepair.getToDate())));
		carRepair.setPayDate(DateUtils.getYMD(DateUtils.getYMDString(carRepair.getPayDate())));
		ActionContext.getContext().getValueStack().push(carRepair);

		return "saveUI";
	}
	
	/** 修改 */
	public String edit() throws Exception {
		if(DateUtils.compareYMD(model.getToDate(), new Date())>0){
			addFieldError("toDate", "你输入的维修时间段不能晚于今天！");
			return "saveUI";
		}
		boolean before = model.getToDate().before(model.getFromDate());
		if(before){
			addFieldError("toDate", "你输入的截止时间不能早于起始时间！");
			return "saveUI";
		}
		
		CarRepair carRepair = carRepairService.getCarRepairById(model.getId());
		Car car = carService.getCarByPlateNumber(model.getCar().getPlateNumber());
		User driver=userService.getById(model.getDriver().getId());

		//设置要修改的属性
		carRepair.setCar(car);
		carRepair.setDriver(driver);
		carRepair.setFromDate(model.getFromDate());
		carRepair.setToDate(model.getToDate());
		carRepair.setRepairLocation(model.getRepairLocation());
		carRepair.setMoney(model .getMoney());
		carRepair.setReason(model.getReason());
		carRepair.setMemo(model .getMemo());
		carRepair.setPayDate(model.getPayDate());
		carRepair.setAppointment(false);

		//更新到数据库
		carRepairService.updateCarRepair(carRepair);
		model=null;
		ActionContext.getContext().getValueStack().push(getModel());

		return freshList();
	}
	
	/** 预约*/
	public String appoint() throws Exception{
		return "saveAppoint";
	}
	
	/** 详细信息*/
	public String detail() throws Exception{
		
		// 准备回显的数据
		CarRepair carRepair = carRepairService.getCarRepairById(model.getId());
		ActionContext.getContext().getValueStack().push(carRepair);
		return "carRepairDetail";
		
	}

	public CarRepair getModel() {
		if(model==null)
			model=new CarRepair();
		return model;
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
