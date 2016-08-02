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
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.Material;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.car.MaterialService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class MaterialReceiveAction extends BaseAction implements ModelDriven<Material> {
	
	private Material model;
	
	@Autowired
	private MaterialService materialService;
	
	@Autowired
	private CarService carService;
	
	@Autowired
	private UserService userService;
	
	private Date beginDate;
	
	private Date endDate;
	
	private String outId;

	//头部快速查询
	public String queryForm(){
		QueryHelper helper = new QueryHelper("Material", "mr");
		if(model.getCar()!=null && model.getCar().getPlateNumber()!=null && !""
				.equals(model.getCar().getPlateNumber()))
			helper.addWhereCondition("mr.car.plateNumber like ?", "%"+model.getCar().getPlateNumber()+"%");
		if(beginDate!=null && endDate!=null)
			helper.addWhereCondition("(TO_DAYS(mr.date)-TO_DAYS(?))>=0 and (TO_DAYS(?)-TO_DAYS(mr.date))>=0", 
					beginDate ,endDate);
		else if(beginDate==null && endDate!=null)
			helper.addWhereCondition("(TO_DAYS(?)-TO_DAYS(mr.date))>=0", endDate);
		else if(beginDate!=null && endDate==null)
			helper.addWhereCondition("(TO_DAYS(mr.date)-TO_DAYS(?))>=0", beginDate);
		helper.addOrderByProperty("mr.date", false);
		
		PageBean pageBean = materialService.queryMaterial(pageNum, helper);		
		ActionContext.getContext().getValueStack().push(pageBean);		
		ActionContext.getContext().getSession().put("materialHelper", helper);
		return "list";		
	}
	//页面初加载数据填充
   public String list() {
		QueryHelper helper = new QueryHelper("Material", "mr");
		if (!(outId==null)) {
			helper.addWhereCondition("mr.car.plateNumber like ?", "%"+outId+"%");
			}
		helper.addOrderByProperty("mr.id", false);
		PageBean<Material> pageBean = materialService.queryMaterial(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("materialHelper", helper);
		return "list";
	}
   public boolean isTure(){
		if (!(outId==null)) {
			return true;
		}
        return	false;
	}
   
   //翻页的时候保留条件并显示数据
   public String freshList(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("materialHelper");
		PageBean<Material> pageBean = materialService.queryMaterial(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
   //删除用户
   public String delete(){
	   materialService.deleteMaterial(model.getId());
		return freshList();
	}
   public String saveUI(){
		return "saveUI";
	}
   
   public String save(){
		Car car = carService.getCarByPlateNumber(model.getCar().getPlateNumber());
		model.setCar(car);
		User driver=userService.getById(model.getDriver().getId());
		model.setDriver(driver);
		materialService.saveMaterial(model);
		ActionContext.getContext().getValueStack().push(new Material());
		return freshList();
	}
   
   public String editUI(){
	    Material material = materialService.getMaterialById(model.getId());
		ActionContext.getContext().getValueStack().push(material);
		return "saveUI";
	}
	
   public String edit(){
	  
	    Material material = materialService.getMaterialById(model.getId());
		Car car = carService.getCarByPlateNumber(model.getCar().getPlateNumber());
		User driver=userService.getById(model.getDriver().getId());
		material.setCar(car);
	    material.setDriver(driver);
		material.setContent(model.getContent());
		material.setDate(model.getDate());
		material.setValue(model.getValue());
		materialService.updateMaterial(material);
		ActionContext.getContext().getValueStack().push(new Material());
		return freshList();
	}
   
   public Material getModel() {
		if(model==null)
			model=new Material();
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
	public String getOutId() {
		return outId;
	}
	public void setOutId(String outId) {
		this.outId = outId;
	}
   
}	

