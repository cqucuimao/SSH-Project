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
		helper.addOrderByProperty("cr.id", false);		
		PageBean<CarRepair> pageBean = carRepairService.queryCarRepair(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("carRepairHelper", helper);
		return "list";
	}
		
	public String list() {
		QueryHelper helper = new QueryHelper(CarRepair.class, "cr");
		if(carId!=null)
			helper.addWhereCondition("cr.car.id=?", carId);
		helper.addOrderByProperty("cr.id", false);
		PageBean<CarRepair> pageBean = carRepairService.queryCarRepair(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("carRepairHelper", helper);
		return "list";
	}
		
	public String freshList(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("carRepairHelper");
		PageBean<CarRepair> pageBean = carRepairService.queryCarRepair(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
		
	public String delete() throws Exception {
		carRepairService.deleteCarRepairById(model.getId());
		return freshList();
	}
	
	public String excel() throws Exception {
		return "excel";
	}
	
	public String importExcelFile() throws Exception{
        InputStream is = new FileInputStream(upload);
		List<List<String>> excelLines = ExcelUtil.getLinesFromExcel(is, 1, 1, 9, 0);
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
							cr.setRepairLocation(excelLines.get(i).get(1));
							
							//维修开始时间
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");  
						    Date fromDate;				
						    fromDate = sdf.parse(excelLines.get(i).get(2));
							cr.setFromDate(fromDate);
							
							//维修结束时间
							Date toDate;				
						    toDate = sdf.parse(excelLines.get(i).get(3));
							cr.setToDate(toDate);
							
							//送修人
							String name = excelLines.get(i).get(4).replaceAll( "\\s", "" );
							User driver = userService.getByLoginName(name);
							if(driver == null){
								failReason = "未知司机";
								ActionContext.getContext().getValueStack().push(failReason);
								ActionContext.getContext().getValueStack().push(result);
								return "false";
							}else{
								cr.setDriver(driver);				
							}
							
							//维修内容
							cr.setMemo(excelLines.get(i).get(5));
							
							
							cr.setReason(excelLines.get(i).get(6));							
							
							if(excelLines.get(i).get(7).length()>0){
								BigDecimal mng = new BigDecimal(excelLines.get(i).get(7));
								cr.setMoneyNoGuaranteed(mng);
							}
							
							//金额
							BigDecimal bd = new BigDecimal(excelLines.get(i).get(8));
							
							cr.setMoney(bd);
							
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
		carRepairService.saveCarRepair(model);
		model=null;
		ActionContext.getContext().getValueStack().push(getModel());
		return freshList();
	}
		
	public String editUI() throws Exception {
		// 准备回显的数据
		
		CarRepair carRepair = carRepairService.getCarRepairById(model.getId());
		
		if(carRepair.getMoney()!=null)
			carRepair.setMoney(carRepair.getMoney().setScale(0, BigDecimal.ROUND_HALF_UP));
		carRepair.setFromDate(DateUtils.getYMD(DateUtils.getYMDString(carRepair.getFromDate())));
		carRepair.setToDate(DateUtils.getYMD(DateUtils.getYMDString(carRepair.getToDate())));
		ActionContext.getContext().getValueStack().push(carRepair);
		User user = (User)ActionContext.getContext().getSession().get("user");
		ActionContext.getContext().put("editNormalInfo", user.hasPrivilegeByUrl("/carRepair_editNormalInfo"));
		ActionContext.getContext().put("editKeyInfo", user.hasPrivilegeByUrl("/carRepair_editKeyInfo"));

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
		User user = (User)ActionContext.getContext().getSession().get("user");
		CarRepair carRepair = carRepairService.getCarRepairById(model.getId());
		if(user.hasPrivilegeByUrl("/carRepair_editNormalInfo")){

			carRepair.setCar(model.getCar());
			carRepair.setDriver(model.getDriver());
			carRepair.setRepairLocation(model.getRepairLocation());
			carRepair.setReason(model.getReason());
			carRepair.setMemo(model .getMemo());
		}
		if(user.hasPrivilegeByUrl("/carRepair_editKeyInfo")){

			carRepair.setFromDate(model.getFromDate());
			carRepair.setToDate(model.getToDate());
			carRepair.setMoney(model .getMoney());
			carRepair.setMoneyNoGuaranteed(model.getMoneyNoGuaranteed());
		}

		//更新到数据库
		carRepairService.updateCarRepair(carRepair);
		model=null;
		ActionContext.getContext().getValueStack().push(getModel());

		return freshList();
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
