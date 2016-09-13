package com.yuqincar.action.car;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.CarExamine;
import com.yuqincar.domain.car.CarExamineAppointment;
import com.yuqincar.domain.car.CarRepairAppointment;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.service.car.CarExamineAppointmentService;
import com.yuqincar.service.car.CarExamineService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class CarExamineAppointmentAction extends BaseAction implements ModelDriven<CarExamineAppointment>{

	private CarExamineAppointment model;

	@Autowired
	private CarExamineAppointmentService carExamineAppointmentService;
	
	private int doneOrUndone;
	
	private Date date1;
	
	private Date date2;
	
	private String actionFlag;
	
	public String queryList(){

		QueryHelper helper = new QueryHelper(CarExamineAppointment.class, "cea");
		if(doneOrUndone == 0){
			helper.addWhereCondition("cea.done=?", false);
		}
		if(doneOrUndone == 1){
			helper.addWhereCondition("cea.done=?", true);
		}
		if(model.getCar()!=null)
			helper.addWhereCondition("cea.car=?", model.getCar());
		
		if(model.getDriver()!=null)
			helper.addWhereCondition("cea.driver=?", model.getDriver());
		
		if(date1!=null && date2!=null)
			helper.addWhereCondition("(TO_DAYS(cea.date)-TO_DAYS(?))>=0 and (TO_DAYS(?)-TO_DAYS(cea.date))>=0", 
					date1 ,date2);
		else if(date1==null && date2!=null)
			helper.addWhereCondition("(TO_DAYS(?)-TO_DAYS(cea.date))>=0", date2);
		else if(date1!=null && date2==null)
			helper.addWhereCondition("(TO_DAYS(cea.date)-TO_DAYS(?))>=0", date1);
		helper.addOrderByProperty("cea.id", false);
		PageBean<CarExamineAppointment> pageBean = carExamineAppointmentService.queryCarExamineAppointment(pageNum, helper);		
		ActionContext.getContext().getValueStack().push(pageBean);		
		ActionContext.getContext().getSession().put("carExamineAppointmentHelper", helper);
		
		return "list";
	}
	
	public String list(){
		QueryHelper helper = new QueryHelper(CarExamineAppointment.class, "cea");
		helper.addWhereCondition("cea.done=?", false);
		helper.addOrderByProperty("cea.id", false);
		PageBean<CarExamineAppointment> pageBean = carExamineAppointmentService.queryCarExamineAppointment(pageNum, helper);		
		ActionContext.getContext().getValueStack().push(pageBean);		
		ActionContext.getContext().getSession().put("carExamineAppointmentHelper", helper);
		return "list";
	}
	
	public String freshList(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("carExamineAppointmentHelper");
		PageBean<CarExamineAppointment> pageBean = carExamineAppointmentService.queryCarExamineAppointment(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}

	public String addUI(){
		
		return "saveUI";
	}
	
	public String add(){
		if(carExamineAppointmentService.isExistAppointment(0, model.getCar())){
			addFieldError("carExamineAppointment", "该车辆已有未完成的年审预约，不能再新增！");
			return addUI();
		}
		model.setDone(false);
		carExamineAppointmentService.saveCarExamineAppointment(model);
		model=null;
		ActionContext.getContext().getValueStack().push(getModel());
		return freshList();
	}
	
	public String editUI(){
		CarExamineAppointment carExamineAppointment = carExamineAppointmentService.getCarExamineAppointmentById(model.getId());
		ActionContext.getContext().getValueStack().push(carExamineAppointment);
		carExamineAppointment.setDate(DateUtils.getYMD(DateUtils.getYMDString(carExamineAppointment.getDate())));
		return "saveUI";
	}
	
	public String edit(){
		if(carExamineAppointmentService.isExistAppointment(model.getId(), model.getCar())){
			addFieldError("carExamineAppointment", "该车辆已有未完成的年审预约，不能再新增！");
			return addUI();
		}
		CarExamineAppointment carExamineAppointment = carExamineAppointmentService.getCarExamineAppointmentById(model.getId());
		carExamineAppointment.setDone(model.isDone());
		carExamineAppointment.setCar(model.getCar());
		carExamineAppointment.setDate(model.getDate());
		carExamineAppointment.setDriver(model.getDriver());
		
		carExamineAppointmentService.updateCarExamineAppointment(carExamineAppointment);
		
		if(model.isDone()){
			return "saveUI1";
		}else{
			ActionContext.getContext().getValueStack().push(new CarExamineAppointment());
			return freshList();
		}
	}
	
	public String delete(){
		carExamineAppointmentService.deleteCarExamineAppointmentById(model.getId());
		ActionContext.getContext().getValueStack().push(new CarExamineAppointment());
		
		return freshList();
	}
	
	public CarExamineAppointment getModel() {
		if(model == null){
			model = new CarExamineAppointment();
		}
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

	public String getActionFlag() {
		return actionFlag;
	}

	public void setActionFlag(String actionFlag) {
		this.actionFlag = actionFlag;
	}

}
