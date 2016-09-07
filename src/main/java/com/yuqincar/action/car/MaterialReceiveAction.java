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
	
	private Long carId;

	//头部快速查询
	public String queryForm(){
		QueryHelper helper = new QueryHelper("Material", "mr");
		if(model.getCar()!=null)
			helper.addWhereCondition("mr.car=?", model.getCar());
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
		if(carId!=null)
			helper.addWhereCondition("mr.car.id=?", carId);
		helper.addOrderByProperty("mr.id", false);
		PageBean<Material> pageBean = materialService.queryMaterial(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("materialHelper", helper);
		return "list";
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
		materialService.saveMaterial(model);
		ActionContext.getContext().getValueStack().push(new Material());
		return freshList();
	}
   
   public String editUI(){
	    Material material = materialService.getMaterialById(model.getId());
		ActionContext.getContext().getValueStack().push(material);
		ActionContext.getContext().put("type", "edit");
		User user=(User)request.getSession().getAttribute("user");
		ActionContext.getContext().put("editKeyInfo", user.hasPrivilegeByUrl("/materialReceive_editKeyInfo"));
		ActionContext.getContext().put("editNormalInfo", user.hasPrivilegeByUrl("/materialReceive_editNormalInfo"));
		return "saveUI";
	}
	
   public String edit(){
	    User user=(User)request.getSession().getAttribute("user");
	    Material material = materialService.getMaterialById(model.getId());
		
	    if (user.hasPrivilegeByUrl("/materialReceive_editNormalInfo")) {
	    material.setCar(model.getCar());
	    material.setDriver(model.getDriver());
		material.setContent(model.getContent());
	    }
	    if (user.hasPrivilegeByUrl("/materialReceive_editKeyInfo")) {
		material.setDate(model.getDate());
		material.setValue(model.getValue());
	    }
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
	public Long getCarId() {
		return carId;
	}
	public void setCarId(Long carId) {
		this.carId = carId;
	}	
}	

