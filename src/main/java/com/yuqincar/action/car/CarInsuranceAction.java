package com.yuqincar.action.car;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.yuqincar.domain.car.CarInsurance;
import com.yuqincar.domain.car.CommercialInsurance;
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
	
	private int inputRows;
	
	private List<Long> commercialInsuranceType =new ArrayList<Long>();	
	
	private List<Date> commercialInsuranceBeginDate = new ArrayList<Date>();
	
	private List<Date> commercialInsuranceEndDate = new ArrayList<Date>();
	
	private List<BigDecimal> commercialInsuranceCoverageMoney = new ArrayList<BigDecimal>();
	
	private List<BigDecimal> commercialInsuranceMoney = new ArrayList<BigDecimal>();
	
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
		ActionContext.getContext().put("commercialInsuranceTypes", carInsuranceService.getAllCommercialInsuranceType());	 
		return "saveUI";
	}
	
	/** 添加 */
	public String add() throws Exception {
		// 保存到数据库
		Car car1 = carService.getCarByPlateNumber(model.getCar().getPlateNumber());
		model.setCar(car1);	
		carInsuranceService.saveCarInsurance(model);
		CarInsurance cia = carInsuranceService.getCarInsuranceById(model.getId());
		for(int i=0;i<inputRows;i++){
			CommercialInsurance commercialInsurance = new CommercialInsurance();
			commercialInsurance.setInsurance(cia);
			commercialInsurance.setCommercialInsuranceType(carInsuranceService.getCommercialInsuranceTypeById(commercialInsuranceType.get(i)));
			commercialInsurance.setCommercialInsuranceBeginDate(commercialInsuranceBeginDate.get(i));
			commercialInsurance.setCommercialInsuranceEndDate(commercialInsuranceBeginDate.get(i));
			commercialInsurance.setCommercialInsuranceCoverageMoney(commercialInsuranceCoverageMoney.get(i));
			commercialInsurance.setCommercialInsuranceMoney(commercialInsuranceMoney.get(i));
			carInsuranceService.saveCommercialInsurance(commercialInsurance);
		}		
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
		List<CommercialInsurance> commercialInsurances = carInsurance.getCommercialInsuranceList();
		ActionContext.getContext().put("commercialInsurances", commercialInsurances);
		ActionContext.getContext().getValueStack().push(carInsurance);
		return "carInsuranceDetail";
	}
	
	public CarInsurance getModel() {
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
	

	public List<Long> getCommercialInsuranceType() {
		return commercialInsuranceType;
	}

	public void setCommercialInsuranceType(List<Long> commercialInsuranceType) {
		this.commercialInsuranceType = commercialInsuranceType;
	}

	public int getInputRows() {
		return inputRows;
	}

	public void setInputRows(int inputRows) {
		this.inputRows = inputRows;
	}

	public List<Date> getCommercialInsuranceBeginDate() {
		return commercialInsuranceBeginDate;
	}

	public void setCommercialInsuranceBeginDate(List<Date> commercialInsuranceBeginDate) {
		this.commercialInsuranceBeginDate = commercialInsuranceBeginDate;
	}

	public List<Date> getCommercialInsuranceEndDate() {
		return commercialInsuranceEndDate;
	}

	public void setCommercialInsuranceEndDate(List<Date> commercialInsuranceEndDate) {
		this.commercialInsuranceEndDate = commercialInsuranceEndDate;
	}

	public List<BigDecimal> getCommercialInsuranceCoverageMoney() {
		return commercialInsuranceCoverageMoney;
	}

	public void setCommercialInsuranceCoverageMoney(List<BigDecimal> commercialInsuranceCoverageMoney) {
		this.commercialInsuranceCoverageMoney = commercialInsuranceCoverageMoney;
	}

	public List<BigDecimal> getCommercialInsuranceMoney() {
		return commercialInsuranceMoney;
	}

	public void setCommercialInsuranceMoney(List<BigDecimal> commercialInsuranceMoney) {
		this.commercialInsuranceMoney = commercialInsuranceMoney;
	}
	
	
	
}
