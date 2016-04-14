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
import com.yuqincar.service.car.CarExamineService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.order.OrderService;
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
	
	/** 列表 */
	public String list() throws Exception {
		QueryHelper helper = new QueryHelper(CarExamine.class, "ce");
		
		if(model.getCar()!=null && model.getCar().getPlateNumber()!=null && !"".equals(model.getCar().getPlateNumber()))
		helper.addWhereCondition("ce.car.plateNumber like ?", "%"+model.getCar().getPlateNumber()+"%");
		
		//if(model.getMoney()!=null && !"".equals(model.getMoney()))
			//helper.addWhereCondition("ce.money=?", model.getMoney());

		System.out.println(helper.getQueryListHql());
		
		PageBean pageBean = carExamineService.queryCarExamine(pageNum, helper);
		
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("carExamineHelper", helper);
		return "list";
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
		
		boolean before = model.getExamineIntervalYear().before(model.getDate());
		//System.out.println(before);
		if(before){
		
			//addActionError("预约时间必须晚于今天！");
			addFieldError("examineIntervalYear", "你输入的下次年审时间不能早于年审时间！");
			//ActionContext.getContext().put("date", "预约时间必须晚于今天！");
			return "saveUI";
		}
		
		Car car1 = carService.getCarByPlateNumber(model.getCar().getPlateNumber());
		
		model.setCar(car1);
		//model.setAppointment(true);
		carExamineService.saveCarExamine(model);
		return "toList";
	}
	
	/** 修改页面 */
	public String editUI() throws Exception {
		// 准备回显的数据
		CarExamine carExamine = carExamineService.getCarExamineById(model.getId());
		
		carExamine.setDate(DateUtils.getYMD(DateUtils.getYMDString(carExamine.getDate())));
		carExamine.setExamineIntervalYear(DateUtils.getYMD(DateUtils.getYMDString(carExamine.getExamineIntervalYear())));
		
		ActionContext.getContext().getValueStack().push(carExamine);

		return "saveUI";
	}
	
	/** 修改 */
	public String edit() throws Exception {
		//从数据库中取出原对象
		
		boolean before = model.getExamineIntervalYear().before(model.getDate());
		//System.out.println(before);
		if(before){
		
			//addActionError("预约时间必须晚于今天！");
			addFieldError("examineIntervalYear", "你输入的下次年审时间不能早于年审时间！");
			//ActionContext.getContext().put("date", "预约时间必须晚于今天！");
			return "saveUI";
		}
		
		CarExamine carExamine = carExamineService.getCarExamineById(model.getId());

		//设置要修改的属性
		carExamine.setDate(model.getDate());
		carExamine.setExamineIntervalYear(model.getExamineIntervalYear());
		carExamine.setMoney(model .getMoney());
		carExamine.setMemo(model .getMemo());
		carExamine.setAppointment(model.isAppointment());

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
	
	
	/** 验证下次年审时间不能早于年审时间*/
	/*public void validateAdd(){
		boolean before = model.getExamineIntervalYear().before(model.getDate());
		//System.out.println(before);
		if(before){
		
			//addActionError("预约时间必须晚于今天！");
			addFieldError("examineIntervalYear", "你输入的下次年审时间不能早于年审时间！");
			//ActionContext.getContext().put("date", "预约时间必须晚于今天！");
		}
	}*/
	
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
		List<List<BaseEntity>> taskList = orderService.getCarTask(car, model.getDate(), model.getDate());
		boolean haveTask=false;
		int taskType=0;  //1订单  2 保养 3 年审 4 维修
		for(List<BaseEntity> dayList:taskList){
			if(dayList!=null && dayList.size()>0){
				BaseEntity baseEntity = dayList.get(0);
				haveTask=true;
				if(baseEntity instanceof Order)
					taskType=0;
				else if (baseEntity instanceof CarCare)
					taskType=1;
				else if(baseEntity instanceof CarExamine)
					taskType=2;
				else if(baseEntity instanceof CarRepair)
					taskType=3;
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
			addFieldError("", "添加年审记录失败！因为该时间段内有"+clazz);
			return "saveAppoint";
		}else {
			carExamineService.carExamineAppointment(car, model.getDate());
		}
		return "toList";
	}

}
