package com.yuqincar.action.car;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.mysql.jdbc.Driver;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.dao.car.impl.CarWashShopDaoImpl;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarWash;
import com.yuqincar.domain.car.CarWashShop;
import com.yuqincar.domain.car.Material;
import com.yuqincar.domain.car.ServicePoint;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.car.CarWashService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;
@Controller
@Scope("prototype")
public class CarWashAction extends BaseAction implements ModelDriven<CarWash> {
	
	private CarWash model = new CarWash();
	
	@Autowired
	private CarWashService carWashService;
	
	@Autowired
	private CarService carService;
	
	@Autowired
	private UserService userService;
	
	private Date beginDate;
	
	private Date endDate;

	//头部快速查询
	public String queryForm(){
		QueryHelper helper = new QueryHelper("CarWash", "cw");
		if(model.getCar()!=null && model.getCar().getPlateNumber()!=null && !""
				.equals(model.getCar().getPlateNumber()))
			helper.addWhereCondition("cw.car.plateNumber like ?", "%"+model.getCar().getPlateNumber()+"%");
		if(beginDate!=null && endDate!=null)
			helper.addWhereCondition("(TO_DAYS(cw.date)-TO_DAYS(?))>=0 and (TO_DAYS(?)-TO_DAYS(cw.date))>=0", 
					beginDate ,endDate);
		else if(beginDate==null && endDate!=null)
			helper.addWhereCondition("(TO_DAYS(?)-TO_DAYS(cw.date))>=0", endDate);
		else if(beginDate!=null && endDate==null)
			helper.addWhereCondition("(TO_DAYS(cw.date)-TO_DAYS(?))>=0", beginDate);
		helper.addOrderByProperty("cw.date", false);
		
		PageBean pageBean = carWashService.queryCarWash(pageNum, helper);		
		ActionContext.getContext().getValueStack().push(pageBean);		
		ActionContext.getContext().getSession().put("carWashHelper", helper);
		return "list";		
	}
	//页面初加载数据填充
   public String list() {
	    QueryHelper helper = new QueryHelper("CarWash", "cw");
		helper.addOrderByProperty("cw.id", false);
		PageBean<CarWash> pageBean = carWashService.queryCarWash(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("carWashHelper", helper);
		return "list";
	}
   //翻页的时候保留条件并显示数据
   public String freshList(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("carWashHelper");
		PageBean<CarWash> pageBean = carWashService.queryCarWash(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
   public String delete(){
	   System.out.println("----------------"+model.getId());
       carWashService.deleteCarWash(model.getId());
	   return freshList();
	}
   public String saveUI(){
	   
	   ActionContext.getContext().put("carWashShopList", carWashService.getAllCarWashShop());
	   return "saveUI";
	}
   
   public String save(){
	    Car car = carService.getCarByPlateNumber(model.getCar().getPlateNumber());
		model.setCar(car);
		User driver=userService.getById(model.getDriver().getId());
		model.setDriver(driver);
		CarWashShop carWashShop=carWashService.getCarWashShopById(model.getShop().getId());
		model.setShop(carWashShop);
		carWashService.saveCarWash(model);
		ActionContext.getContext().getValueStack().push(new CarWash());
		return freshList();
	}
   
   public String editUI(){
	    ActionContext.getContext().put("carWashShopList", carWashService.getAllCarWashShop());
	    CarWash carWash = carWashService.getCarWashById(model.getId());
		ActionContext.getContext().getValueStack().push(carWash);
		return "saveUI";
	}
   
   public String edit(){
		  
	    CarWash carWash = carWashService.getCarWashById(model.getId());
		Car car = carService.getCarByPlateNumber(model.getCar().getPlateNumber());
		User driver=userService.getById(model.getDriver().getId());
		CarWashShop carWashShop=carWashService.getCarWashShopById(model.getShop().getId());
		carWash.setCar(car);
		carWash.setDriver(driver);
		carWash.setShop(carWashShop);
		carWash.setMoney(model.getMoney());
		carWash.setDate(model.getDate());
		carWash.setDoPolishing(model.isDoPolishing());
		carWash.setDoEngineClean(model.isDoEngineClean());
		carWash.setDoInnerClean(model.isDoInnerClean());
		carWashService.updateCarWash(carWash);
		ActionContext.getContext().getValueStack().push(new CarWash());
		return freshList();
	}
 
   
   
   public CarWash getModel() {
		return model;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}	

