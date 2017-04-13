package com.yuqincar.action.car;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.CarCareAppointment;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.businessParameter.BusinessParameterService;
import com.yuqincar.service.car.CarCareAppointmentService;
import com.yuqincar.service.sms.SMSService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;
@Controller
@Scope("prototype")
public class CarCareAppointmentAction extends BaseAction implements ModelDriven<CarCareAppointment>{

	private CarCareAppointment model;
	@Autowired
	private CarCareAppointmentService carCareAppointmentService;
	@Autowired
	private BusinessParameterService businessParameterService;
	@Autowired
	private SMSService smsService;
	
	private int doneOrUndone;
	
	private Date date1;
	
	private Date date2;
	
	private int mileInterval;
	
	public String queryForm(){
		QueryHelper helper = new QueryHelper(CarCareAppointment.class, "cca");
		if(doneOrUndone == 0){
			helper.addWhereCondition("cca.done=?", false);
		}
		if(doneOrUndone == 1){
			helper.addWhereCondition("cca.done=?", true);
		}
		if(model.getCar()!=null)
			helper.addWhereCondition("cca.car=?", model.getCar());
		
		if(model.getDriver()!=null)
			helper.addWhereCondition("cca.driver=?", model.getDriver());
		
		if(date1!=null && date2!=null)
			helper.addWhereCondition("(TO_DAYS(cca.date)-TO_DAYS(?))>=0 and (TO_DAYS(?)-TO_DAYS(cca.date))>=0", 
					date1 ,date2);
		else if(date1==null && date2!=null)
			helper.addWhereCondition("(TO_DAYS(?)-TO_DAYS(cca.date))>=0", date2);
		else if(date1!=null && date2==null)
			helper.addWhereCondition("(TO_DAYS(cca.date)-TO_DAYS(?))>=0", date1);
		helper.addOrderByProperty("cca.id", false);
		PageBean<CarCareAppointment> pageBean = carCareAppointmentService.queryCarCareAppointment(pageNum, helper);		
		ActionContext.getContext().getValueStack().push(pageBean);		
		ActionContext.getContext().getSession().put("carCareAppointmentHelper", helper);
		return "list";	
	}
	
	public String list(){
		QueryHelper helper = new QueryHelper(CarCareAppointment.class, "cca");
		helper.addWhereCondition("cca.done=?", false);
		helper.addOrderByProperty("cca.id", false);
		PageBean<CarCareAppointment> pageBean = carCareAppointmentService.queryCarCareAppointment(pageNum, helper);		
		ActionContext.getContext().getValueStack().push(pageBean);		
		ActionContext.getContext().getSession().put("carCareAppointmentHelper", helper);
		return "list";
	}
	
	public String freshList(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("carCareAppointmentHelper");
		PageBean<CarCareAppointment> pageBean = carCareAppointmentService.queryCarCareAppointment(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
	
	public String addUI(){
		
		return "saveUI";
	}
	
	public String add(){
		if(carCareAppointmentService.isExistAppointment(0, model.getCar())){
			addFieldError("carCareAppointment", "该车辆已有未完成的保养预约，不能再新增！");
			return addUI();
		}
		model.setDone(false);
		//保存预约信息时，已经给司机发送短信了。
		carCareAppointmentService.saveCarCareAppointment(model);
		
		//给4S发送短信（不能写到saveCarCareAppointment中，因为这个方法会被定时任务MileUpdate批量调用，会触发短信流量限制）
		Map<String,String> params=new HashMap<String,String>();
		String plateNumber= model.getCar().getPlateNumber()+"（"+DateUtils.getYMDString(model.getDate())+"）";
		params.put("plateNumber", plateNumber);
		for(User in4SUser:businessParameterService.getBusinessParameter().getEmployeesIn4SForSMS()){
			smsService.sendTemplateSMS(in4SUser.getPhoneNumber(), SMSService.SMS_TEMPLATE_CARCARE_APPOINTMENT_GENERATED_FOR_4S_EMPLOYEE, params);
		}
		
		model=null;
		ActionContext.getContext().getValueStack().push(getModel());
		return freshList();
	}
	
	public String editUI(){
		CarCareAppointment carCareAppointment = carCareAppointmentService.getCarCareAppointmentById(model.getId());
		ActionContext.getContext().getValueStack().push(carCareAppointment);
		carCareAppointment.setDate(DateUtils.getYMD(DateUtils.getYMDString(carCareAppointment.getDate())));
		return "saveUI";
	}
	
	public String edit(){
		if(carCareAppointmentService.isExistAppointment(model.getId(), model.getCar())){
			addFieldError("carCareAppointment", "该车辆已有未完成的保养预约，不能再新增！");
			return addUI();
		}
		CarCareAppointment carCareAppointment = carCareAppointmentService.getCarCareAppointmentById(model.getId());
		//System.out.println("DONE="+model.isDone());
		carCareAppointment.setDone(model.isDone());
		carCareAppointment.setCar(model.getCar());
		carCareAppointment.setDate(model.getDate());
		carCareAppointment.setDriver(model.getDriver());
		
		carCareAppointmentService.updateCarCareAppointment(carCareAppointment,mileInterval);
		ActionContext.getContext().getValueStack().push(new CarCareAppointment());
		return freshList();
	}
	
	public String delete(){
		System.out.println("cui");
		carCareAppointmentService.deleteCarCareAppointmentById(model.getId());
		ActionContext.getContext().getValueStack().push(new CarCareAppointment());
		return freshList();
	}

	public CarCareAppointment getModel() {
		if(model==null)
			model=new CarCareAppointment();
		return model;
	}

	public int getMileInterval() {
		return mileInterval;
	}

	public void setMileInterval(int mileInterval) {
		this.mileInterval = mileInterval;
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
