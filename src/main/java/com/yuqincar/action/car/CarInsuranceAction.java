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
import com.yuqincar.domain.car.CarRefuel;
import com.yuqincar.domain.car.CommercialInsurance;
import com.yuqincar.domain.car.CommercialInsuranceType;
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
	
	private Date date1;
	
	private Date date2;
	
	private int inputRows;
	
	private Long carId;
	
	private List<Long> commercialInsuranceType =new ArrayList<Long>();	
	
	private List<Date> commercialInsuranceBeginDate = new ArrayList<Date>();
	
	private List<Date> commercialInsuranceEndDate = new ArrayList<Date>();
	
	private List<BigDecimal> commercialInsuranceCoverageMoney = new ArrayList<BigDecimal>();
	
	private List<BigDecimal> commercialInsuranceMoney = new ArrayList<BigDecimal>();
	
	private List<String> commercialInsuranceMemo = new ArrayList<String>();
	
	/** 列表 */
	public String queryList(){
		QueryHelper helper = new QueryHelper(CarInsurance.class, "ci");
		
		if(model.getCar()!=null)
			helper.addWhereCondition("ci.car=?", model.getCar());
		
		if(date1!=null && date2!=null)
			helper.addWhereCondition("(TO_DAYS(ci.payDate)-TO_DAYS(?))>=0 and (TO_DAYS(?)-TO_DAYS(ci.payDate))>=0", 
					date1 ,date2);
		else if(date1==null && date2!=null)
			helper.addWhereCondition("(TO_DAYS(?)-TO_DAYS(ci.payDate))>=0", date2);
		else if(date1!=null && date2==null)
			helper.addWhereCondition("(TO_DAYS(ci.payDate)-TO_DAYS(?))>=0", date1);
		
		helper.addOrderByProperty("ci.id", false);		
		PageBean pageBean = carInsuranceService.queryCarInsurance(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("carInsuranceHelper", helper);
		return "list";
	}
	
	/** 列表 */
	public String list(){
		QueryHelper helper = new QueryHelper(CarInsurance.class, "ci");
		if(carId!=null)
			helper.addWhereCondition("ci.car.id=?", carId);
		helper.addOrderByProperty("ci.id", false);
		PageBean pageBean = carInsuranceService.queryCarInsurance(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("carInsuranceHelper", helper);
		return "list";
	}

	public String freshList(){
		QueryHelper helper = (QueryHelper)ActionContext.getContext().getSession().get("carInsuranceHelper");
		PageBean pageBean = carInsuranceService.queryCarInsurance(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
	
	/** 添加页面 */
	public String addUI() throws Exception {
		ActionContext.getContext().put("commercialInsuranceTypes", carInsuranceService.getAllCommercialInsuranceType());	 
		return "saveUI";
	}
	
	/** 添加 */
	public String add() throws Exception {
		List<CommercialInsuranceType> cit =new ArrayList<CommercialInsuranceType>();
		for(int i=0;i<inputRows;i++){
			if(commercialInsuranceBeginDate.get(i).after(commercialInsuranceEndDate.get(i))){
				addFieldError("commercialInsurance", "商业保险生效日期晚于了截止日期！");
				return addUI();
			}
			cit.add(carInsuranceService.getCommercialInsuranceTypeById(commercialInsuranceType.get(i)));
		}
		carInsuranceService.saveCarInsurance(model,cit,commercialInsuranceBeginDate,commercialInsuranceEndDate,
				commercialInsuranceCoverageMoney,commercialInsuranceMoney,commercialInsuranceMemo,inputRows);
		
				
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
	public String remind(){
		PageBean pageBean = carInsuranceService.getNeedInsuranceCars(pageNum);
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
	
	//添加商业保险--界面
	public String addCommercialInsuranceUI(){
		
		// 准备回显的数据
		CarInsurance carInsurance = carInsuranceService.getCarInsuranceById(model.getId());
		List<CommercialInsurance> commercialInsurances = carInsurance.getCommercialInsuranceList();
		ActionContext.getContext().put("commercialInsurances", commercialInsurances);
		ActionContext.getContext().getValueStack().push(carInsurance);
		ActionContext.getContext().put("commercialInsuranceTypes", carInsuranceService.getAllCommercialInsuranceType());	 
		return "commercialInsuranceUI";
	}
	
	//添加商业保险
	public String addCommercialInsurance(){
		List<CommercialInsurance> cis = new ArrayList<CommercialInsurance>();
		CarInsurance carInsurance = carInsuranceService.getCarInsuranceById(model.getId());
		for(int i=0;i<inputRows;i++){
			CommercialInsurance commercialInsurance = new CommercialInsurance();
			commercialInsurance.setInsurance(carInsurance);
			commercialInsurance.setCommercialInsuranceType(carInsuranceService.getCommercialInsuranceTypeById(commercialInsuranceType.get(i)));
			if(commercialInsuranceBeginDate.get(i).before(commercialInsuranceEndDate.get(i))){
				commercialInsurance.setCommercialInsuranceBeginDate(commercialInsuranceBeginDate.get(i));
				commercialInsurance.setCommercialInsuranceEndDate(commercialInsuranceEndDate.get(i));
			}			
			commercialInsurance.setCommercialInsuranceCoverageMoney(commercialInsuranceCoverageMoney.get(i));
			commercialInsurance.setCommercialInsuranceMoney(commercialInsuranceMoney.get(i));
			commercialInsurance.setCommercialInsuranceMemo(commercialInsuranceMemo.get(i));
			cis.add(commercialInsurance);
		}
		
		carInsurance.setFromDate(model.getFromDate());
		carInsurance.setToDate(model.getToDate());
		carInsurance.setMoney(model.getMoney());
		
		carInsuranceService.addCommercialInsurance(cis, carInsurance);
		
		return "toList";
	}
	
	public CarInsurance getModel() {
		return model;
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
	
	

	public List<String> getCommercialInsuranceMemo() {
		return commercialInsuranceMemo;
	}

	public void setCommercialInsuranceMemo(List<String> commercialInsuranceMemo) {
		this.commercialInsuranceMemo = commercialInsuranceMemo;
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

	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
	}
	
	
	
}
