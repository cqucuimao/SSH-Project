package com.yuqincar.action.car;

import java.io.IOException;
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
import com.yuqincar.domain.car.CarStatusEnum;
import com.yuqincar.domain.car.CarViolation;
import com.yuqincar.domain.car.CommercialInsurance;
import com.yuqincar.domain.car.CommercialInsuranceType;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.car.CarInsuranceService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.common.ExportService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class CarInsuranceAction extends BaseAction implements ModelDriven<CarInsurance> {
	
	private CarInsurance model = new CarInsurance();
	
	@Autowired
	private CarInsuranceService carInsuranceService;
	
	@Autowired
	private CarService carService;
	
	@Autowired
	private ExportService exportservice;
	
	private Date date1;
	
	private Date date2;
	
	private int inputRows;
	
	private String actionFlag;
	
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
		List<CommercialInsurance> commercialInsurances = carInsurance.getCommercialInsuranceList();
		//List<Long> longs = new ArrayList<Long>();
		/*for(int i=0;i<commercialInsurances.size();i++){
			commercialInsuranceType.add(commercialInsurances.get(i).getCommercialInsuranceType().getId());
		}*/
		//commercialInsuranceType = longs;
		ActionContext.getContext().put("commercialInsurances", commercialInsurances);
		ActionContext.getContext().getValueStack().push(carInsurance);
		ActionContext.getContext().put("commercialInsuranceTypes", carInsuranceService.getAllCommercialInsuranceType());	
		User user = (User)ActionContext.getContext().getSession().get("user");
		ActionContext.getContext().put("editNormalInfo", user.hasPrivilegeByUrl("/carInsurance_editNormalInfo"));
		ActionContext.getContext().put("editKeyInfo", user.hasPrivilegeByUrl("/carInsurance_editKeyInfo"));
		
		return "saveUI";
	}
	
	public String edit(){
		User user = (User)ActionContext.getContext().getSession().get("user");
		
		CarInsurance carInsurance = carInsuranceService.getCarInsuranceById(model.getId());
		List<CommercialInsurance> commercialInsurances = carInsurance.getCommercialInsuranceList();
		for(int i=0;i<commercialInsurances.size();i++){
			if(user.hasPrivilegeByUrl("/carInsurance_editNormalInfo")){
				commercialInsurances.get(i).setCommercialInsuranceBeginDate(commercialInsuranceBeginDate.get(i));
				commercialInsurances.get(i).setCommercialInsuranceEndDate(commercialInsuranceEndDate.get(i));
				commercialInsurances.get(i).setCommercialInsuranceMemo(commercialInsuranceMemo.get(i));
				commercialInsurances.get(i).setCommercialInsuranceCoverageMoney(commercialInsuranceCoverageMoney.get(i));
			}
			if(user.hasPrivilegeByUrl("/carInsurance_editKeyInfo")){
				commercialInsurances.get(i).setCommercialInsuranceMoney(commercialInsuranceMoney.get(i));
			}
		}
		carInsurance.setCommercialInsuranceList(commercialInsurances);
		if(user.hasPrivilegeByUrl("/carInsurance_editNormalInfo")){
			carInsurance.setCar(model.getCar());
			carInsurance.setInsureCompany(model.getInsureCompany());
			carInsurance.setCompulsoryPolicyNumber(model.getCompulsoryPolicyNumber());
			carInsurance.setCommercialPolicyNumber(model.getCommercialPolicyNumber());
			carInsurance.setCompulsoryBeginDate(model.getCompulsoryBeginDate());
			carInsurance.setCompulsoryEndDate(model.getCompulsoryEndDate());
			carInsurance.setVehicleTaxBeginDate(model.getVehicleTaxBeginDate());
			carInsurance.setVehicleTaxEndDate(model.getVehicleTaxEndDate());
			carInsurance.setMemo(model.getMemo());
			carInsurance.setFromDate(model.getFromDate());
			carInsurance.setToDate(model.getToDate());
		}
		if(user.hasPrivilegeByUrl("/carInsurance_editNormalInfo")){
			carInsurance.setCompulsoryMoney(model.getCompulsoryMoney());
			carInsurance.setVehicleTaxMoney(model.getVehicleTaxMoney());
			carInsurance.setMoney(model.getMoney());
		}
		carInsuranceService.updateCarInsurance(carInsurance);
		
		return "toList";
	}
	
	/** 提醒*/
	public String remind(){
		PageBean pageBean = carInsuranceService.getNeedInsuranceCars(pageNum);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "remindList";
	}
	
	//导出保险提醒
	public void exportRemind() throws IOException{
		QueryHelper helper = new QueryHelper(Car.class, "c");
		Date now = new Date();
		Date a = new Date(now.getTime() +  24*60*60*1000 * 30L );
		helper.addWhereCondition("c.insuranceExpiredDate < ? and c.status=? and c.borrowed=?", a,CarStatusEnum.NORMAL,false);
		helper.addOrderByProperty("c.insuranceExpiredDate", true);
		//获得所有需要提醒的车辆
		List<Car> cars = carInsuranceService.getAllNeedInsuranceCars(helper);
		
		//表名
		String excelName = "重庆渝勤公司车辆保险到期提醒表";
		
		//表格的title
		List<String> title = new ArrayList<String>();
		title.add("车牌号");
		title.add("司机");
		title.add("手机");
		title.add("保险过期日期");
		title.add("是否过保");
		
		//导出到excel中的类型
		List<List<String>> allList = new ArrayList<List<String>>();
		
		
		for(int i=0;i<cars.size();i++){
			//excel表中的每一行（车牌号，司机，手机，保险过期日期，是否过保）
			List<String> excelCarsList = new ArrayList<String>();
			//车牌号（车牌号不能为空）
			excelCarsList.add(cars.get(i).getPlateNumber());
			//司机(司机可能为空)
			if(cars.get(i).getDriver() != null){
				excelCarsList.add(cars.get(i).getDriver().getName());
			}else{
				excelCarsList.add("");
			}
			//手机号，即司机手机号码
			if(cars.get(i).getDriver() != null){
				excelCarsList.add(cars.get(i).getDriver().getPhoneNumber()+"");
			}else{
				excelCarsList.add("");
			}
			//保险过期日期,日期只保留年月日
			excelCarsList.add(DateUtils.getYMDString(cars.get(i).getInsuranceExpiredDate()));
			//是否过保
			if(cars.get(i).isInsuranceExpired()){
				excelCarsList.add("是");
			}else{
				excelCarsList.add("否");
			}
			
			allList.add(excelCarsList);
			
		}
		//调用exportservice中的exportExcel接口实现表格的导出
		exportservice.exportExcel(excelName, title, allList, response);
		
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

	public String getActionFlag() {
		return actionFlag;
	}

	public void setActionFlag(String actionFlag) {
		this.actionFlag = actionFlag;
	}
	
	
	
}
