package com.yuqincar.action.car;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.dev.ReSave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarCare;
import com.yuqincar.domain.car.CarExamine;
import com.yuqincar.domain.car.CarRefuel;
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
import com.yuqincar.utils.ExcelUtil;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class CarRepairAction extends BaseAction implements ModelDriven<CarRepair> {
	
	private CarRepair model;
	
	@Autowired
	private CarRepairService carRepairService;
	
	@Autowired
	private CarService carService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	
	 private File upload;
		
	private String uploadFileName;
		
	private String uploadContentType;
	
	private String failReason;
		
	private int result;
	
	private Date date1;
	
	private Date date2;
	
	private Long carId;
	
	public String queryForm() throws Exception {
		QueryHelper helper = new QueryHelper(CarRepair.class, "cr");
		
		if(model.getCar()!=null)
			helper.addWhereCondition("cr.car=?", model.getCar());
		
		if(model.getDriver()!=null)
			helper.addWhereCondition("cr.driver=?", model.getDriver());

		if(date1!=null && date2!=null)
			helper.addWhereCondition("(TO_DAYS(cr.fromDate)-TO_DAYS(?))>=0 and (TO_DAYS(?)-TO_DAYS(cr.fromDate))>=0", 
					date1 ,date2);
		else if(date1==null && date2!=null)
			helper.addWhereCondition("(TO_DAYS(?)-TO_DAYS(cr.fromDate))>=0", date2);
		else if(date1!=null && date2==null)
			helper.addWhereCondition("(TO_DAYS(cr.fromDate)-TO_DAYS(?))>=0", date1);
		helper.addWhereCondition("appointment=?", false);
		helper.addOrderByProperty("cr.id", false);		
		PageBean<CarRepair> pageBean = carRepairService.queryCarRepair(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("carRepairHelper", helper);
		return "list";
	}
	
	public String queryAppointForm() throws Exception {
		QueryHelper helper = new QueryHelper(CarRepair.class, "cr");
		
		if(model.getCar()!=null)
			helper.addWhereCondition("cr.car=?", model.getCar());
		
		if(model.getDriver()!=null)
			helper.addWhereCondition("cr.driver=?", model.getDriver());

		if(date1!=null && date2!=null)
			helper.addWhereCondition("(TO_DAYS(cr.fromDate)-TO_DAYS(?))>=0 and (TO_DAYS(?)-TO_DAYS(cr.fromDate))>=0", 
					date1 ,date2);
		else if(date1==null && date2!=null)
			helper.addWhereCondition("(TO_DAYS(?)-TO_DAYS(cr.fromDate))>=0", date2);
		else if(date1!=null && date2==null)
			helper.addWhereCondition("(TO_DAYS(cr.fromDate)-TO_DAYS(?))>=0", date1);
		helper.addWhereCondition("appointment=?", true);
		helper.addOrderByProperty("cr.id", false);		
		PageBean<CarRepair> pageBean = carRepairService.queryCarRepair(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("carRepairAppointHelper", helper);
		return "appointList";
	}
	
	public String list() {
		QueryHelper helper = new QueryHelper(CarRepair.class, "cr");
		helper.addWhereCondition("appointment=?", false);
		if(carId!=null)
			helper.addWhereCondition("cr.car.id=?", carId);
		helper.addOrderByProperty("cr.id", false);
		PageBean<CarRepair> pageBean = carRepairService.queryCarRepair(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("carRepairHelper", helper);
		return "list";
	}
	
	public String appointList() {
		QueryHelper helper = new QueryHelper(CarRepair.class, "cr");
		helper.addWhereCondition("appointment=?", true);
		helper.addOrderByProperty("cr.id", false);
		PageBean<CarRepair> pageBean = carRepairService.queryCarRepair(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("carRepairAppointHelper", helper);
		return "appointList";
	}
	
	public String freshList(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("carRepairHelper");
		PageBean<CarRepair> pageBean = carRepairService.queryCarRepair(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
	
	public String freshAppointList(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("carRepairAppointHelper");
		PageBean<CarRepair> pageBean = carRepairService.queryCarRepair(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "appointList";
	}
	
	public String delete() throws Exception {
		carRepairService.deleteCarRepairById(model.getId());
		return freshList();
	}
	
	public String deleteAppointment() throws Exception {
		carRepairService.deleteCarRepairById(model.getId());
		return freshAppointList();
	}
	
	public String excel() throws Exception {
		return "excel";
	}
	
	public String importExcelFile() throws Exception{
        InputStream is = new FileInputStream(upload);
		ExcelUtil eu = new ExcelUtil();
		List<List<String>> excelLines = eu.getLinesFromExcel(is, 1, 1, 10, 0);
		List<CarRepair> carRepairs = new ArrayList<CarRepair>();
				for(int i=1;i<excelLines.size();i++){
					try {
							result=i;
						    CarRepair cr = new CarRepair();
							
							//车辆
							System.out.println("车牌号="+excelLines.get(i).get(0));
							Car car = carService.getCarByPlateNumber(excelLines.get(i).get(0));
							if(car == null){
								failReason = "未知车辆";
								ActionContext.getContext().getValueStack().push(failReason);
								ActionContext.getContext().getValueStack().push(result);
								return "false";
							}else{
								cr.setCar(car);
							}
							
							//承修单位
							System.out.println("承修单位="+excelLines.get(i).get(1));
							cr.setRepairLocation(excelLines.get(i).get(1));
							
							//维修开始时间
							System.out.println("维修开始时间="+excelLines.get(i).get(2));
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");  
						    Date fromDate;				
						    fromDate = sdf.parse(excelLines.get(i).get(2));
							cr.setFromDate(fromDate);
							
							//维修结束时间
							System.out.println("维修结束时间="+excelLines.get(i).get(3));
						    Date toDate;				
						    toDate = sdf.parse(excelLines.get(i).get(3));
							cr.setToDate(toDate);
							
							//送修人
							System.out.println("司机="+excelLines.get(i).get(4));
							User driver = userService.getByLoginName(excelLines.get(i).get(4));
							if(driver == null){
								userService.saveDispatchUser(excelLines.get(i).get(4));
								User dispatchDriver = userService.getByLoginName(excelLines.get(i).get(4));
								cr.setDriver(dispatchDriver);
							}else{
								cr.setDriver(driver);				
							}
							
							//维修内容
							System.out.println("维修内容="+excelLines.get(i).get(5));
							cr.setMemo(excelLines.get(i).get(5));
							
							
							System.out.println("维修原因="+excelLines.get(i).get(6));
							cr.setReason(excelLines.get(i).get(6));
							
							//付款日期
							System.out.println("付款日期="+excelLines.get(i).get(7));
						    Date payDate;				
						    payDate = sdf.parse(excelLines.get(i).get(7));
							cr.setPayDate(payDate);
							
							System.out.println("未赔付金额="+excelLines.get(i).get(8));
							if(excelLines.get(i).get(8).length()>0){
								BigDecimal mng = new BigDecimal(excelLines.get(i).get(8));
								System.out.println("mng= "+mng);
								cr.setMoneyNoGuaranteed(mng);
							}
							
							//金额
							System.out.println("金额="+excelLines.get(i).get(9));
							BigDecimal bd = new BigDecimal(excelLines.get(i).get(9));
							
							cr.setMoney(bd);
							
							cr.setAppointment(false);
							
							carRepairs.add(cr);
						} catch (Exception e) {
							failReason = "不明原因";
							ActionContext.getContext().getValueStack().push(failReason);
							ActionContext.getContext().getValueStack().push(result);
							return "false";
						}
				}
				result = excelLines.size() - 1;
				carRepairService.importExcelFile(carRepairs);
				ActionContext.getContext().getValueStack().push(result);
				return "success";	
	}
	
	public String addUI() throws Exception {
		return "saveUI";
	}
	
	public String add() throws Exception {

		if(DateUtils.compareYMD(model.getToDate(), new Date())>0){
			addFieldError("toDate", "你输入的维修时间段不能晚于今天！");
			return "saveUI";
		}
		
		boolean before = model.getToDate().before(model.getFromDate());
		if(before){
			addFieldError("toDate", "你输入的截止时间不能早于起始时间！");
			return "saveUI";
		}
		model.setAppointment(false);
		carRepairService.saveCarRepair(model);
		model=null;
		ActionContext.getContext().getValueStack().push(getModel());
		return freshList();
	}
	
	public String saveAppointment(){		
		boolean before = model.getToDate().before(model.getFromDate());
		if(before){
			addFieldError("toDate", "你输入的截止时间不能早于起始时间！");
			return "saveAppoint";
		}
		
		List<List<BaseEntity>> taskList = orderService.getCarTask(model.getCar(), model.getFromDate(), model.getToDate());
		taskList.addAll(orderService.getDriverTask(model.getDriver(),  model.getFromDate(), model.getToDate()));
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
			CarRepair carRepair=new CarRepair();
			carRepair.setCar(model.getCar());
			carRepair.setDriver(model.getDriver());
			carRepair.setFromDate(model.getFromDate());
			carRepair.setToDate(model.getToDate());
			carRepair.setDone(model.isDone());
			carRepair.setAppointment(true);
			carRepairService.saveAppointment(carRepair);
			model=null;
			ActionContext.getContext().getValueStack().push(getModel());
		}

		return freshAppointList();
	}
	
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
		if(DateUtils.compareYMD(model.getToDate(), new Date())>0){
			addFieldError("toDate", "你输入的维修时间段不能晚于今天！");
			return "saveUI";
		}
		boolean before = model.getToDate().before(model.getFromDate());
		if(before){
			addFieldError("toDate", "你输入的截止时间不能早于起始时间！");
			return "saveUI";
		}
		
		CarRepair carRepair = carRepairService.getCarRepairById(model.getId());

		//设置要修改的属性
		carRepair.setCar(model.getCar());
		carRepair.setDriver(model.getDriver());
		carRepair.setFromDate(model.getFromDate());
		carRepair.setToDate(model.getToDate());
		carRepair.setRepairLocation(model.getRepairLocation());
		carRepair.setMoney(model .getMoney());
		carRepair.setMoneyNoGuaranteed(model.getMoneyNoGuaranteed());
		carRepair.setReason(model.getReason());
		carRepair.setMemo(model .getMemo());
		carRepair.setPayDate(model.getPayDate());
		carRepair.setAppointment(false);

		//更新到数据库
		carRepairService.updateCarRepair(carRepair);
		model=null;
		ActionContext.getContext().getValueStack().push(getModel());

		return freshList();
	}
	
	public String editAppointmentUI(){
		CarRepair carRepair = carRepairService.getCarRepairById(model.getId());		
		carRepair.setFromDate(DateUtils.getYMD(DateUtils.getYMDString(carRepair.getFromDate())));
		carRepair.setToDate(DateUtils.getYMD(DateUtils.getYMDString(carRepair.getToDate())));
		ActionContext.getContext().getValueStack().push(carRepair);
		return "saveAppoint";
	}
	
	public String editAppointment(){		
		boolean before = model.getToDate().before(model.getFromDate());
		if(before){
			addFieldError("toDate", "你输入的截止时间不能早于起始时间！");
			return "saveAppoint";
		}
		
		Date fromDate1=carRepairService.getCarRepairById(model.getId()).getFromDate();
		Date toDate1=carRepairService.getCarRepairById(model.getId()).getToDate();
		Date fromdate2=model.getFromDate();
		Date todate2=model.getToDate();
		if(!DateUtils.getYMDString(fromDate1).equals(DateUtils.getYMDString(fromdate2)) || 
				!DateUtils.getYMDString(toDate1).equals(DateUtils.getYMDString(todate2))){
			List<List<BaseEntity>> taskList = orderService.getCarTask(model.getCar(), model.getFromDate(), model.getToDate());
			taskList.addAll(orderService.getDriverTask(model.getDriver(),  model.getFromDate(), model.getToDate()));
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
			}
		}
		CarRepair carRepair=carRepairService.getCarRepairById(model.getId());
		carRepair.setCar(model.getCar());
		carRepair.setDriver(model.getDriver());
		carRepair.setFromDate(model.getFromDate());
		carRepair.setToDate(model.getToDate());
		carRepair.setDone(model.isDone());
		carRepairService.saveAppointment(carRepair);
		model=null;
		ActionContext.getContext().getValueStack().push(getModel());
		return freshAppointList();
	}
	
	public String appoint() throws Exception{
		return "saveAppoint";
	}
	
	public String detail() throws Exception{
		CarRepair carRepair = carRepairService.getCarRepairById(model.getId());
		ActionContext.getContext().getValueStack().push(carRepair);
		return "carRepairDetail";		
	}

	public CarRepair getModel() {
		if(model==null)
			model=new CarRepair();
		return model;
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
