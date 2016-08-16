package com.yuqincar.action.car;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.dao.order.CarServiceSuperTypeDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarServiceSuperType;
import com.yuqincar.domain.car.CarServiceType;
import com.yuqincar.domain.car.TollCharge;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.CustomerOrganization;
import com.yuqincar.domain.order.Price;
import com.yuqincar.domain.order.PriceTable;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.CustomerOrganization.CustomerOrganizationService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.car.TollChargeService;
import com.yuqincar.service.order.PriceService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class TollChargeAction extends BaseAction implements ModelDriven<TollCharge> {
	
	private TollCharge model;
	
	@Autowired
	private TollChargeService tollChargeService;
	
	@Autowired
	private CarService carService;
	
	@Autowired 
	PriceService priceSerivce;
	@Autowired 
	CarServiceSuperTypeDao carServiceSuperTypeDao;
	
	@Autowired
	UserService userService;
	@Autowired
	CustomerOrganizationService organizationService;
	
	private Date beginDate;
	
	private Date endDate;

	private Long carId;

	public String queryForm(){
		QueryHelper helper = new QueryHelper("TollCharge", "tc");
		
		if(model.getCar()!=null)
			helper.addWhereCondition("tc.car=?", model.getCar());
		
		if(beginDate!=null && endDate!=null)
			helper.addWhereCondition("(TO_DAYS(tc.payDate)-TO_DAYS(?))>=0 and (TO_DAYS(?)-TO_DAYS(tc.payDate))>=0", 
					beginDate ,endDate);
		else if(beginDate==null && endDate!=null)
			helper.addWhereCondition("(TO_DAYS(?)-TO_DAYS(tc.payDate))>=0", endDate);
		else if(beginDate!=null && endDate==null)
			helper.addWhereCondition("(TO_DAYS(tc.payDate)-TO_DAYS(?))>=0", beginDate);
		helper.addOrderByProperty("tc.payDate", false);
		
		PageBean pageBean = tollChargeService.queryTollCharge(pageNum, helper);		
		ActionContext.getContext().getValueStack().push(pageBean);		
		ActionContext.getContext().getSession().put("tollChargeHelper", helper);
		return "list";		
	}

	public String list() {
		System.out.println("in list");
		QueryHelper helper = new QueryHelper("TollCharge", "tc");
		if(carId!=null)
			helper.addWhereCondition("tc.car.id=?", carId);
		helper.addOrderByProperty("tc.id", false);
		PageBean<TollCharge> pageBean = tollChargeService.queryTollCharge(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("tollChargeHelper", helper);
		return "list";
	}
	
	public String freshList(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("tollChargeHelper");
		PageBean<TollCharge> pageBean = tollChargeService.queryTollCharge(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
	
	public String delete(){
		tollChargeService.deleteTollCharge(model.getId());
		return freshList();
	}
	
	public String saveUI(){
		return "saveUI";
	}
	
	public String save(){
		tollChargeService.saveTollCharge(model);
		ActionContext.getContext().getValueStack().push(new TollCharge());
		return freshList();
	}
	
	public String editUI(){
		TollCharge tollCharge = tollChargeService.getTollChargeById(model.getId());
		ActionContext.getContext().getValueStack().push(tollCharge);
		return "saveUI";
	}
	
	public String edit(){
		TollCharge tollCharge = tollChargeService.getTollChargeById(model.getId());
		tollCharge.setCar(model.getCar());
		tollCharge.setPayDate(model.getPayDate());
		tollCharge.setMoney(model.getMoney());
		tollCharge.setOverdueFine(model.getOverdueFine());
		tollCharge.setMoneyForCardReplace(model.getMoneyForCardReplace());
		tollCharge.setNextPayDate(model.getNextPayDate());
		tollChargeService.updateTollCharge(tollCharge);
		ActionContext.getContext().getValueStack().push(new TollCharge());
		return freshList();
	}
		
	public String remind(){
		QueryHelper helper = new QueryHelper("Car", "c");
		helper.addWhereCondition("c.nextTollChargeDate is not null");
		PageBean<Car> pageBean = carService.queryCar(pageNum, helper);
		List<RemindVO> voList=new ArrayList<RemindVO>(pageBean.getRecordList().size());
		for(Car car:pageBean.getRecordList()){
			RemindVO vo=new RemindVO();
			vo.setCar(car);
			vo.setNextPayDate(DateUtils.getYMDString(car.getNextTollChargeDate()));
			TollCharge tc=tollChargeService.getRecentTollCharge(car);
			if(tc!=null)
				vo.setPayDate(DateUtils.getYMDString(tc.getPayDate()));
			else
				vo.setPayDate("");
			voList.add(vo);
		}
		pageBean.setRecordList(voList);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "remind";
	}
	
	public TollCharge getModel() {
		if(model==null)
			model=new TollCharge();
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
	
	public class RemindVO{
		private Car car;
		private String payDate;
		private String nextPayDate;
		
		public Car getCar() {
			System.out.println("in getCar()");
			return car;
		}
		public void setCar(Car car) {
			this.car = car;
		}
		public String getPayDate() {
			System.out.println("in getPayDate()");
			return payDate;
		}
		public void setPayDate(String payDate) {
			this.payDate = payDate;
		}
		public String getNextPayDate() {
			System.out.println("in getNextPayDate()");
			return nextPayDate;
		}
		public void setNextPayDate(String nextPayDate) {
			this.nextPayDate = nextPayDate;
		}
	}

	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
	}

}
