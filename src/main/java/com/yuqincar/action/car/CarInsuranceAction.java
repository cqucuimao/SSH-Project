package com.yuqincar.action.car;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarCare;
import com.yuqincar.domain.car.CarInsurance;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.service.car.CarInsuranceService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class CarInsuranceAction extends BaseAction implements ModelDriven<CarInsurance> {
	
	private CarInsurance model = new CarInsurance();
	
	@Autowired
	private CarInsuranceService carInsuranceService;
	
	@Autowired
	private CarService carService;
	
	/** 列表 */
	public String list() throws Exception {
		QueryHelper helper = new QueryHelper(CarInsurance.class, "ci");
		
		if(model.getCar()!=null && model.getCar().getPlateNumber()!=null && !"".equals(model.getCar().getPlateNumber()))
		helper.addWhereCondition("ci.car.plateNumber like ?", "%"+model.getCar().getPlateNumber()+"%");
		
		//if(model.getInsureCompany()!=null && !"".equals(model.getInsureCompany()))
			//helper.addWhereCondition("u.insureCompany=?", model.getInsureCompany());
		
		PageBean pageBean = carInsuranceService.queryCarInsurance(pageNum, helper);
		
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("carInsuranceHelper", helper);
		return "list";
	}
	
	/** 添加页面 */
	public String addUI() throws Exception {
		return "saveUI";
	}
	
	/** 添加 */
	public String add() throws Exception {
		// 保存到数据库
		
		Car car1 = carService.getCarByPlateNumber(model.getCar().getPlateNumber());
		model.setCar(car1);
		
		carInsuranceService.saveCarInsurance(model);
		return "toList";
	}
	
	/** 修改页面 */
	public String editUI() throws Exception {
		// 准备回显的数据
		CarInsurance carInsurance = carInsuranceService.getCarInsuranceById(model.getId());
		ActionContext.getContext().getValueStack().push(carInsurance);
		return "saveUI";
	}
	
	/** 提醒*/
	public String remind() throws Exception{
		QueryHelper helper = new QueryHelper(CarInsurance.class, "ci");
		
		if(model.getCar()!=null && model.getCar().getPlateNumber()!=null && !""
				.equals(model.getCar().getPlateNumber()))
			helper.addWhereCondition("ci.car.plateNumber like ?", 
					"%"+model.getCar().getPlateNumber()+"%");
		
		
		PageBean pageBean = carInsuranceService.queryCarInsurance(pageNum, helper);
		
		ActionContext.getContext().getValueStack().push(pageBean);
		return "remindList";
	}
	
	/** 详细信息*/
	public String detail() throws Exception{
		
		// 准备回显的数据
		CarInsurance carInsurance = carInsuranceService.getCarInsuranceById(model.getId());
		ActionContext.getContext().getValueStack().push(carInsurance);
		return "carInsuranceDetail";
	}
	
	public CarInsurance getModel() {
		// TODO Auto-generated method stub
		return model;
	}
	
	public String insurance(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("carInsuranceHelper");
		PageBean pageBean = carInsuranceService.queryCarInsurance(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
	
	
	/** 验证截止时间不能早于起始时间*/
	public void validateAdd(){
		boolean before = model.getToDate().before(model.getFromDate());
		//System.out.println(before);
		if(before){
		
			//addActionError("预约时间必须晚于今天！");
			addFieldError("toDate", "你输入的截止时间不能早于起始时间！");
			//ActionContext.getContext().put("date", "预约时间必须晚于今天！");
		}
	}

}
