package com.yuqincar.action.car;

import java.math.BigDecimal;
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
	
	private CarRepair model = new CarRepair();
	
	@Autowired
	private CarRepairService carRepairService;
	
	@Autowired
	private CarService carService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	
	/** 列表 */
	public String list() throws Exception {
		QueryHelper helper = new QueryHelper(CarRepair.class, "cr");
		
		if(model.getCar()!=null && model.getCar().getPlateNumber()!=null 
				&& !"".equals(model.getCar().getPlateNumber()))
			helper.addWhereCondition("cr.car.plateNumber like ?", "%"+model.getCar().getPlateNumber()+"%");
		
		if(model.getDriver()!=null && model.getDriver().getId()!=null)
			helper.addWhereCondition("cr.driver.id = ?", model.getDriver().getId());
		
		//if(model.getMoney()!=null && !"".equals(model.getMoney()))
		//helper.addWhereCondition("cr.money=?", model.getMoney());
		
		PageBean pageBean = carRepairService.queryCarRepair(pageNum, helper);
		
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("carRepairHelper", helper);
		return "list";
	}
	
	/** 删除 */
	public String delete() throws Exception {
		carRepairService.deleteCarRepairById(model.getId());
		return "toList";
	}
	
	/** 添加页面 */
	public String addUI() throws Exception {
		return "saveUI";
	}
	
	/** 添加 */
	public String add() throws Exception {
		// 保存到数据库
		boolean before = model.getToDate().before(model.getFromDate());
		//System.out.println(before);
		if(before){
		
			//addActionError("预约时间必须晚于今天！");
			addFieldError("toDate", "你输入的截止时间不能早于起始时间！");
			//ActionContext.getContext().put("date", "预约时间必须晚于今天！");
			return "saveUI";
		}
		
		Car car1 = carService.getCarByPlateNumber(model.getCar().getPlateNumber());
		User driver=userService.getById(model.getDriver().getId());
		
		model.setCar(car1);
		model.setDriver(driver);
		model.setAppointment(false);
		carRepairService.saveCarRepair(model);
		
		return "toList";
	}
	
	public String saveAppointment(){
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
		}
		
		return "toList";
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
		//从数据库中取出原对象
		
		boolean before = model.getToDate().before(model.getFromDate());
		//System.out.println(before);
		if(before){
		
			//addActionError("预约时间必须晚于今天！");
			addFieldError("toDate", "你输入的截止时间不能早于起始时间！");
			//ActionContext.getContext().put("date", "预约时间必须晚于今天！");
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

		return "toList";
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
		return model;
	}
	
	public String repair(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("carRepairHelper");
		PageBean pageBean = carRepairService.queryCarRepair(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}

}
