package com.yuqincar.action.car;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.dao.car.CarCareDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarCare;
import com.yuqincar.domain.car.CarExamine;
import com.yuqincar.domain.car.CarRefuel;
import com.yuqincar.domain.car.CarRepair;
import com.yuqincar.domain.car.TollCharge;
import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.car.CarCareService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.ExcelUtil;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class CarCareAction extends BaseAction implements ModelDriven<CarCare> {
	
	private CarCare model;
	
	@Autowired
	private CarCareService carCareService;
	
	@Autowired
	private CarService carService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CarCareDao carCareDao;
	
    private File upload;
	
	private String uploadFileName;
	
	private String uploadContentType;
	
	private String failReason;
	
	private int result;
	
	private Date date;

	private Date date1;
	
	private Date date2;
	
	private Long carId;
	
	
	public String queryForm(){
		QueryHelper helper = new QueryHelper(CarCare.class, "cc");
		
		if(model.getCar()!=null)
			helper.addWhereCondition("cc.car=?", model.getCar());
		
		if(model.getDriver()!=null)
			helper.addWhereCondition("cc.driver=?", model.getDriver());
		
		if(date1!=null && date2!=null)
			helper.addWhereCondition("(TO_DAYS(cc.date)-TO_DAYS(?))>=0 and (TO_DAYS(?)-TO_DAYS(cc.date))>=0", 
					date1 ,date2);
		else if(date1==null && date2!=null)
			helper.addWhereCondition("(TO_DAYS(?)-TO_DAYS(cc.date))>=0", date2);
		else if(date1!=null && date2==null)
			helper.addWhereCondition("(TO_DAYS(cc.date)-TO_DAYS(?))>=0", date1);
		helper.addWhereCondition("appointment=?", false);
		helper.addOrderByProperty("cc.id", false);
		PageBean<CarCare> pageBean = carCareService.queryCarCare(pageNum, helper);		
		ActionContext.getContext().getValueStack().push(pageBean);		
		ActionContext.getContext().getSession().put("carCareHelper", helper);
		return "list";	
	}
	
	public String queryAppointForm(){
		QueryHelper helper = new QueryHelper(CarCare.class, "cc");
		if(model.getCar()!=null)
			helper.addWhereCondition("cc.car=?", model.getCar());
		
		if(model.getDriver()!=null)
			helper.addWhereCondition("cc.driver=?", model.getDriver());
		
		if(date1!=null && date2!=null)
			helper.addWhereCondition("(TO_DAYS(cc.date)-TO_DAYS(?))>=0 and (TO_DAYS(?)-TO_DAYS(cc.date))>=0", 
					date1 ,date2);
		else if(date1==null && date2!=null)
			helper.addWhereCondition("(TO_DAYS(?)-TO_DAYS(cc.date))>=0", date2);
		else if(date1!=null && date2==null)
			helper.addWhereCondition("(TO_DAYS(cc.date)-TO_DAYS(?))>=0", date1);
		helper.addWhereCondition("appointment=?", true);
		helper.addOrderByProperty("cc.id", false);
		PageBean<CarCare> pageBean = carCareService.queryCarCare(pageNum, helper);		
		ActionContext.getContext().getValueStack().push(pageBean);		
		ActionContext.getContext().getSession().put("carCareAppointHelper", helper);
		return "appointList";	
	}

	public String list() {
		QueryHelper helper = new QueryHelper(CarCare.class, "cc");
		helper.addWhereCondition("cc.appointment=?", false);
		if(carId!=null)
			helper.addWhereCondition("cc.car.id=?", carId);
		helper.addOrderByProperty("cc.id", false);
		PageBean<CarCare> pageBean = carCareService.queryCarCare(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("carCareHelper", helper);
		return "list";
	}
	
	public String appointList() {
		QueryHelper helper = new QueryHelper(CarCare.class, "cc");
		helper.addWhereCondition("cc.appointment=?", true);
		helper.addOrderByProperty("cc.id", false);
		PageBean<CarCare> pageBean = carCareService.queryCarCare(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("carCareAppointHelper", helper);
		return "appointList";
	}
	
	public String freshList(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("carCareHelper");
		PageBean<CarCare> pageBean = carCareService.queryCarCare(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
	
	public String freshAppointList(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("carCareAppointHelper");
		PageBean<CarCare> pageBean = carCareService.queryCarCare(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "appointList";
	}
	
	public String delete() throws Exception {
		carCareService.deleteCarCareById(model.getId());
		return freshList();
	}
	
	public String deleteAppointment() throws Exception {
		carCareService.deleteCarCareById(model.getId());
		return freshAppointList();
	}
	
	public String excel() throws Exception {
		return "excel";
	}
	
	public String importExcelFile() throws Exception{
		InputStream is = new FileInputStream(upload);
		ExcelUtil eu = new ExcelUtil();
		List<List<String>> excelLines = eu.getLinesFromExcel(is, 1, 1, 8, 0);
		List<CarCare> carCares = new ArrayList<CarCare>();
		
		for(int i=1;i<excelLines.size();i++){
					
		        try {       
		        	         result=i;
							CarCare cc = new CarCare();
							
							//车辆
							System.out.println("车牌号="+excelLines.get(i).get(0));
							Car car = carService.getCarByPlateNumber(excelLines.get(i).get(0));
							if(car == null){
								failReason = "未知车辆";
								ActionContext.getContext().getValueStack().push(failReason);
								ActionContext.getContext().getValueStack().push(result);
								return "false";
							}else{
								cc.setCar(car);
							}
							
							//承修单位
							System.out.println("承修单位="+excelLines.get(i).get(1));
							cc.setCareDepo(excelLines.get(i).get(1));
							
							//保养时间
							System.out.println("保养时间="+excelLines.get(i).get(2));
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");  
						    Date date;				
							date = sdf.parse(excelLines.get(i).get(2));
							cc.setDate(date);
							
							//送修人
							System.out.println("司机="+excelLines.get(i).get(3));
							User driver = userService.getByLoginName(excelLines.get(i).get(3));
							if(driver == null){
								failReason = "未知司机";
								ActionContext.getContext().getValueStack().push(failReason);
								ActionContext.getContext().getValueStack().push(result);
								return "false";
							}else{
								cc.setDriver(driver);				
							}
							
							//保养内容
							System.out.println("保养内容="+excelLines.get(i).get(4));
							cc.setMemo(excelLines.get(i).get(4));
							
							//保养里程数
							System.out.println("保养里程数="+excelLines.get(i).get(5));
							Integer careMiles = Integer.parseInt(excelLines.get(i).get(5));
							cc.setCareMiles(careMiles);
							
							//间隔里程
							System.out.println("间隔里程="+excelLines.get(i).get(6));
							Integer mileInterval = Integer.parseInt(excelLines.get(i).get(6));
							cc.setMileInterval(mileInterval);
							
							//金额
							System.out.println("金额="+excelLines.get(i).get(7));
							BigDecimal bd = new BigDecimal(excelLines.get(i).get(7));
							cc.setMoney(bd);
							
							cc.setAppointment(false);
							carCares.add(cc);
						} catch (Exception e) {
							failReason = "不明原因";
							ActionContext.getContext().getValueStack().push(failReason);
							ActionContext.getContext().getValueStack().push(result);
							return "false";
						}
				}
			
		result = excelLines.size() - 1;
		carCareService.importExcelFile(carCares);
		ActionContext.getContext().getValueStack().push(result);
		return "success";	
	}
	
	/** 添加页面 */
	public String addUI() throws Exception {
		return "saveUI";
	}
	
	/** 添加 */
	public String add() throws Exception {
		System.out.println("in add1, model="+model);
		// 保存到数据库
		
		if(DateUtils.compareYMD(model.getDate(), new Date())>0){
			addFieldError("date", "你输入的保养日期不能晚于今天！");
			return "saveUI";
		}
		
		model.setAppointment(false);
		carCareService.saveCarCare(model);
		model=null;
		ActionContext.getContext().getValueStack().push(getModel());
		return freshList();
	}
	
	public String editUI(){
		// 准备回显的数据
		CarCare carCare = carCareService.getCarCareById(model.getId());
		if(carCare.getMoney()!=null)
			carCare.setMoney(carCare.getMoney().setScale(0, BigDecimal.ROUND_HALF_UP));
		carCare.setDate(DateUtils.getYMD(DateUtils.getYMDString(carCare.getDate())));
		ActionContext.getContext().getValueStack().push(carCare);
		
		return "saveUI";
	}
	
	public String editAppointmentUI(){
		// 准备回显的数据
		CarCare carCare = carCareService.getCarCareById(model.getId());
		carCare.setDate(DateUtils.getYMD(DateUtils.getYMDString(carCare.getDate())));
		ActionContext.getContext().getValueStack().push(carCare);
		
		return "saveAppoint";
	}
	
	/** 修改 */
	public String edit() throws Exception {
		if(DateUtils.compareYMD(model.getDate(), new Date())>0){		
			addFieldError("date", "你输入的保养日期不能晚于今天！");
			return "saveUI";
		}
		
		CarCare carCare = carCareService.getCarCareById(model.getId());

		carCare.setCar(model.getCar());
		carCare.setDriver(model.getDriver());
		carCare.setDate(model.getDate());
		carCare.setCareDepo(model.getCareDepo());
		carCare.setCareMiles(model.getCareMiles());
		carCare.setMileInterval(model.getMileInterval());
		carCare.setMoney(model.getMoney());
		carCare.setMemo(model.getMemo());

		//更新到数据库
		carCareService.updateCarCare(carCare);
		model=null;
		ActionContext.getContext().getValueStack().push(getModel());
		return freshList();
	}
	
	public String appoint() throws Exception{		
		return "saveAppoint";
	}
	
	public String remind(){
		PageBean<Car> pageBean=carCareService.getNeedCareCars(pageNum);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "remindList";
	}
	
	public String detail() throws Exception{
		CarCare carCare = carCareService.getCarCareById(model.getId());
		ActionContext.getContext().getValueStack().push(carCare);
		return "carCareDetail";
	}

	public CarCare getModel() {
		if(model==null)
			model=new CarCare();
		return model;
	}
	
	public String saveAppointment(){		
		List<List<BaseEntity>> taskList = orderService.getCarTask(model.getCar(), model.getDate(), model.getDate());
		taskList.addAll(orderService.getDriverTask(model.getDriver(),  model.getDate(), model.getDate()));
		boolean haveTask=false;
		int taskType=0;	//1订单  2 保养 3 年审 4 维修
		for(List<BaseEntity> dayList: taskList){
			if(dayList!=null && dayList.size()>0){
				BaseEntity baseEntity=dayList.get(0);
				haveTask=true;
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
			//提醒用户不能预约保养
			String clazz=null;
			switch(taskType){
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
			addFieldError("", "添加保养预约失败！因为车辆或司机在该时间段内有"+clazz);
			return "saveAppoint";
		}else{
			CarCare carCare=new CarCare();
			carCare.setCar(model.getCar());
			carCare.setDriver(model.getDriver());
			carCare.setDate(model.getDate());
			carCare.setDone(model.isDone());
			carCareService.saveAppointment(carCare);
			model=null;
			ActionContext.getContext().getValueStack().push(getModel());
		}

		return freshAppointList();		
	}
	
	public String editAppointment(){		
		Date date1=carCareService.getCarCareById(model.getId()).getDate();
		Date date2=model.getDate();
		if(!DateUtils.getYMDString(date1).equals(DateUtils.getYMDString(date2))){
			List<List<BaseEntity>> taskList = orderService.getCarTask(model.getCar(), model.getDate(), model.getDate());
			taskList.addAll(orderService.getDriverTask(model.getDriver(),  model.getDate(), model.getDate()));
			boolean haveTask=false;
			int taskType=0;	//1订单  2 保养 3 年审 4 维修
			for(List<BaseEntity> dayList: taskList){
				if(dayList!=null && dayList.size()>0){
					BaseEntity baseEntity=dayList.get(0);
					haveTask=true;
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
				//提醒用户不能预约保养
				String clazz=null;
				switch(taskType){
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
				addFieldError("", "修改保养预约失败！因为车辆或司机在该时间段内有"+clazz);
				return "saveAppoint";
			}
		}
			
		CarCare carCare = carCareService.getCarCareById(model.getId());
		carCare.setCar(model.getCar());
		carCare.setDriver(model.getDriver());
		carCare.setDate(model.getDate());
		carCare.setDone(model.isDone());
		System.out.println("here, carCare.getDate()="+DateUtils.getYMDString(carCare.getDate()));
		carCareService.updateAppointment(carCare);
		model=null;
		ActionContext.getContext().getValueStack().push(getModel());
		return freshAppointList();
	}
	
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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
	
	public File getUpload() {
		return upload;
	}
	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
	}
}
