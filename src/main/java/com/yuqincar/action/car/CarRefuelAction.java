package com.yuqincar.action.car;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarRefuel;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.car.CarRefuelService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.ExcelUtil;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class CarRefuelAction extends BaseAction implements ModelDriven<CarRefuel> {
	
	private CarRefuel model = new CarRefuel();
	
	@Autowired
	private CarRefuelService carRefuelService;
	
	@Autowired
	private CarService carService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderService orderService;
	
	private Date date1;
	
	private Date date2;
	
	private File upload;
	
	private String uploadFileName;
	
	private String uploadContentType;
	/** 列表 */
	public String list() throws Exception {
		QueryHelper helper = new QueryHelper(CarRefuel.class, "cr");
		
		if(model.getCar()!=null && model.getCar().getPlateNumber()!=null && !""
				.equals(model.getCar().getPlateNumber()))
			helper.addWhereCondition("cr.car.plateNumber like ?", 
					"%"+model.getCar().getPlateNumber()+"%");
		
		if(date1!=null && date2!=null)
			helper.addWhereCondition("(TO_DAYS(cr.date)-TO_DAYS(?))>=0 and (TO_DAYS(?)-TO_DAYS(cr.date))>=0", 
					date1 ,date2);
		
		PageBean pageBean = carRefuelService.queryCarRefuel(pageNum, helper);
		
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("carRefuelHelper", helper);
		return "list";
	}
	
	/** 添加页面 */
	public String addUI() throws Exception {
		return "saveUI";
	}
	
	/** 导入excel页面 */
	public String excel() throws Exception {
		return "excel";
	}
	/**
	 * 导入excel
	 * @throws Exception
	 */
	public String importExcelFile() throws Exception{
		InputStream is = new FileInputStream(upload);
		carRefuelService.importExcelFile(is, 1, 1, 6, 0);
		return "toList";
	}
	
	/** 添加 */
	public String add() throws Exception {
		// 保存到数据库
		
		Car car = carService.getCarByPlateNumber(model.getCar().getPlateNumber());
		User driver = userService.getById(model.getDriver().getId());
		
		model.setCar(car);
		model.setDriver(driver);
		
		carRefuelService.saveCarRefuel(model);
		return "toList";
	}
	
	/** 修改页面 */
	public String editUI() throws Exception {
		// 准备回显的数据
		CarRefuel carRefuel = carRefuelService.getCarRefuelById(model.getId());
		ActionContext.getContext().getValueStack().push(carRefuel);
		return "saveUI";
	}
	
	/** 详细信息*/
	public String detail() throws Exception{
		
		// 准备回显的数据
		CarRefuel carRefuel = carRefuelService.getCarRefuelById(model.getId());
		ActionContext.getContext().getValueStack().push(carRefuel);
		return "carRefuelDetail";
		
	}
	
	public CarRefuel getModel() {
		return model;
	}
	
	public String refuel(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("carRefuelHelper");
		PageBean pageBean = carRefuelService.queryCarRefuel(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
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
	
	
	
}
