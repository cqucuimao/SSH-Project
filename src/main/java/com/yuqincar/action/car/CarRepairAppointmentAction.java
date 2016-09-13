package com.yuqincar.action.car;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.CarCareAppointment;
import com.yuqincar.domain.car.CarRepair;
import com.yuqincar.domain.car.CarRepairAppointment;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.service.car.CarRepairAppointmentService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class CarRepairAppointmentAction extends BaseAction implements ModelDriven<CarRepairAppointment>{

	private CarRepairAppointment model;
	@Autowired
	CarRepairAppointmentService carRepairAppointmentService;
	
	private int doneOrUndone;
	
	private Date date1;
	
	private Date date2;
	
	public String queryList(){
		QueryHelper helper = new QueryHelper(CarRepairAppointment.class, "cra");
		if(doneOrUndone == 0){
			helper.addWhereCondition("cra.done=?", false);
		}
		if(doneOrUndone == 1){
			helper.addWhereCondition("cra.done=?", true);
		}
		if(model.getCar()!=null)
			helper.addWhereCondition("cra.car=?", model.getCar());
		
		if(model.getDriver()!=null)
			helper.addWhereCondition("cra.driver=?", model.getDriver());
		
		if(date1!=null && date2!=null)
			helper.addWhereCondition("(TO_DAYS(cra.fromDate)-TO_DAYS(?))>=0 and (TO_DAYS(?)-TO_DAYS(cra.fromDate))>=0", 
					date1 ,date2);
		else if(date1==null && date2!=null)
			helper.addWhereCondition("(TO_DAYS(?)-TO_DAYS(cra.fromDate))>=0", date2);
		else if(date1!=null && date2==null)
			helper.addWhereCondition("(TO_DAYS(cra.fromDate)-TO_DAYS(?))>=0", date1);
		helper.addOrderByProperty("cra.id", false);
		PageBean<CarRepairAppointment> pageBean = carRepairAppointmentService.queryCarRepairAppointment(pageNum, helper);		
		ActionContext.getContext().getValueStack().push(pageBean);		
		ActionContext.getContext().getSession().put("carRepairAppointmentHelper", helper);
		return "list";
	}
	
	public String list(){
		QueryHelper helper = new QueryHelper(CarRepairAppointment.class, "cra");
		helper.addWhereCondition("cra.done=?", false);
		helper.addOrderByProperty("cra.id", false);
		PageBean<CarRepairAppointment> pageBean = carRepairAppointmentService.queryCarRepairAppointment(pageNum, helper);		
		ActionContext.getContext().getValueStack().push(pageBean);		
		ActionContext.getContext().getSession().put("carRepairAppointmentHelper", helper);
		return "list";
	}
	
	public String freshList(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("carRepairAppointmentHelper");
		PageBean<CarRepairAppointment> pageBean = carRepairAppointmentService.queryCarRepairAppointment(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
	
	public String addUI(){
		
		return "saveUI";
	}
	
	public String add(){
		if(carRepairAppointmentService.isExistAppointment(0, model.getCar())){
			addFieldError("carRepairAppointment", "该车辆已有未完成的维修预约，不能再新增！");
			return addUI();
		}
		model.setDone(false);
		carRepairAppointmentService.saveCarRepairAppointment(model);
		model=null;
		ActionContext.getContext().getValueStack().push(getModel());
		return freshList();
	}

	public String editUI(){
		CarRepairAppointment carRepairAppointment = carRepairAppointmentService.getCarRepairAppointmentById(model.getId());
		ActionContext.getContext().getValueStack().push(carRepairAppointment);
		carRepairAppointment.setFromDate(DateUtils.getYMD(DateUtils.getYMDString(carRepairAppointment.getFromDate())));
		carRepairAppointment.setToDate(DateUtils.getYMD(DateUtils.getYMDString(carRepairAppointment.getToDate())));
	
		return "saveUI";
	}

	public String edit(){
		if(carRepairAppointmentService.isExistAppointment(model.getId(), model.getCar())){
			addFieldError("carRepairAppointment", "该车辆已有未完成的维修预约，不能再新增！");
			return addUI();
		}
		CarRepairAppointment carRepairAppointment = carRepairAppointmentService.getCarRepairAppointmentById(model.getId());
		carRepairAppointment.setDone(model.isDone());
		carRepairAppointment.setCar(model.getCar());
		carRepairAppointment.setFromDate(model.getFromDate());
		carRepairAppointment.setToDate(model.getToDate());
		carRepairAppointment.setDriver(model.getDriver());
		
		carRepairAppointmentService.updateCarRepairAppointment(carRepairAppointment);
		ActionContext.getContext().getValueStack().push(new CarRepairAppointment());
	
		return freshList();
	}
	
	public String delete(){
		carRepairAppointmentService.deleteCarRepairAppointmentById(model.getId());
		ActionContext.getContext().getValueStack().push(new CarRepairAppointment());
		return freshList();
	}
	
	public CarRepairAppointment getModel() {
		if(model==null)
			model=new CarRepairAppointment();
		return model;
	}

	public int getDoneOrUndone() {
		return doneOrUndone;
	}

	public void setDoneOrUndone(int doneOrUndone) {
		this.doneOrUndone = doneOrUndone;
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
