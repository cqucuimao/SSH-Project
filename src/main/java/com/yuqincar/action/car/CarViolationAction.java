package com.yuqincar.action.car;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarServiceType;
import com.yuqincar.domain.car.CarViolation;
import com.yuqincar.domain.car.CarWash;
import com.yuqincar.domain.car.CarWashShop;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.privilege.Role;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.car.CarViolationService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class CarViolationAction extends BaseAction implements ModelDriven<CarViolation>{
	
	private CarViolation model = new CarViolation();
	
	@Autowired
	private CarViolationService carViolationService;
	
	@Autowired
	private CarService carService;
	
	@Autowired
	private UserService userService;
	
    private Date beginDate;
	
	private Date endDate;
	
	private String date;

		//头部快速查询
		public String queryForm(){
			QueryHelper helper = new QueryHelper("CarViolation", "cv");
			if(model.getCar()!=null && model.getCar().getPlateNumber()!=null && !""
					.equals(model.getCar().getPlateNumber()))
				helper.addWhereCondition("cv.car.plateNumber like ?", "%"+model.getCar().getPlateNumber()+"%");
			if(beginDate!=null && endDate!=null)
				helper.addWhereCondition("(TO_DAYS(cv.date)-TO_DAYS(?))>=0 and (TO_DAYS(?)-TO_DAYS(cv.date))>=0", 
						beginDate ,endDate);
			else if(beginDate==null && endDate!=null)
				helper.addWhereCondition("(TO_DAYS(?)-TO_DAYS(cv.date))>=0", endDate);
			else if(beginDate!=null && endDate==null)
				helper.addWhereCondition("(TO_DAYS(cv.date)-TO_DAYS(?))>=0", beginDate);
			helper.addOrderByProperty("cv.date", false);
			
			PageBean pageBean = carViolationService.queryCarViolation(pageNum, helper);		
			ActionContext.getContext().getValueStack().push(pageBean);		
			ActionContext.getContext().getSession().put("carViolationHelper", helper);
			return "list";		
		}
	
		 public String list() {
			 	QueryHelper helper = new QueryHelper("CarViolation", "cv");
				helper.addOrderByProperty("cv.id", false);
				PageBean pageBean = carViolationService.queryCarViolation(pageNum, helper);
				ActionContext.getContext().getValueStack().push(pageBean);
				ActionContext.getContext().getSession().put("carViolationHelper", helper);
				return "list";
			}
		 
		//翻页的时候保留条件并显示数据
		   public String freshList(){
				QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("carViolationHelper");
				PageBean pageBean = carViolationService.queryCarViolation(pageNum, helper);
				ActionContext.getContext().getValueStack().push(pageBean);
				return "list";
			}
		   public String delete(){
			  carViolationService.deleteCarViolation(model.getId());
			   return freshList();
			}
	 
		   public String saveUI(){
			   return "saveUI";
			  
			}
		   
		   public String save(){
			    System.out.println("********************************"+date);
			   	System.out.println("model.getDate()="+model.getDate());
			    Car car = carService.getCarByPlateNumber(model.getCar().getPlateNumber());
				model.setCar(car);
				User driver=userService.getById(model.getDriver().getId());
				model.setDriver(driver);
				model.setImported(false);
				
				carViolationService.saveCarViolation(model);
				ActionContext.getContext().getValueStack().push(new CarViolation());
				return freshList();
			}
		   
		   public String editUI(){
			    CarViolation carViolation = carViolationService.getCarViolationById(model.getId());
				ActionContext.getContext().getValueStack().push(carViolation);
				return "saveUI";
			}
		   
		   public String edit(){
			   System.out.println("in edit");
			    System.out.println("********************************"+DateUtils.getYMDHMString(model.getDate()));
			    CarViolation carViolation = carViolationService.getCarViolationById(model.getId());				
				User driver=userService.getById(model.getDriver().getId());
				Car car= carService.getCarByPlateNumber(model.getCar().getPlateNumber());
				carViolation.setCar(car);
				carViolation.setDriver(driver);
				carViolation.setDate(model.getDate());
				carViolation.setPlace(model.getPlace());
				carViolation.setDescription(model.getDescription());
				carViolation.setPenaltyPoint(model.getPenaltyPoint());
				carViolation.setPenaltyMoney(model.getPenaltyMoney());
				carViolation.setDealt(model.isDealt());
				carViolation.setDealtDate(model.getDealtDate());
				carViolationService.updateCarViolation(carViolation);
				ActionContext.getContext().getValueStack().push(new CarViolation());
				return freshList();
			}
		   
		   public boolean isCanUpdateCarViolation(){
				CarViolation carViolation = (CarViolation) ActionContext.getContext().getValueStack().peek();
				if(carViolationService.canUpdateCarViolation(carViolation.getId()))
					return true;
				else 
					return false;
			  }
		 
		   
	public CarViolation getModel() {
		// TODO Auto-generated method stub
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	
	
}