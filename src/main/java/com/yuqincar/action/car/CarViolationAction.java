package com.yuqincar.action.car;

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
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.privilege.Role;
import com.yuqincar.service.car.CarViolationService;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class CarViolationAction extends BaseAction implements ModelDriven<CarViolation>{
	
	private CarViolation model = new CarViolation();
	
	@Autowired
	private CarViolationService carViolationService;

	
	
	/** 列表 *//*
	public String list() throws Exception {
		
		QueryHelper helper = new QueryHelper(CarViolation.class, "cv");

		if (model.getCar().getPlateNumber() != null && !"".equals(model.getCar().getPlateNumber() ))
			helper.addWhereCondition("cv.title like ?", "%"+model.getTitle()+"%");
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
	
	*//** 删除 *//*
	public String delete() throws Exception {
		if(carService.canDeleteCarServiceType(model.getId()))
			carService.deleteCarServiceType(model.getId());
		return "toList";
	}
	
	*//** 添加页面 *//*
	public String addUI() throws Exception {
		
		return "saveUI";
	}
	
	*//** 添加 *//*
	public String add() throws Exception {
		// 封装对象
		//model.setDepartment(departmentService.getById(departmentId));
		//List<Role> roleList = roleService.getByIds(roleIds);
		//model.setRoles(new HashSet<Role>(roleList));

		// 保存到数据库
		carService.saveCarViolation(model);

		return "toList";
	}
	
	*//** 修改页面 *//*
	public String editUI() throws Exception {
		// 准备回显的数据
		CarServiceType carServiceType = carService.getCarServiceTypeById(model.getId());
		ActionContext.getContext().getValueStack().push(carServiceType);

		// 准备数据：departmentList
		//ActionContext.getContext().put("departmentList", departmentService.findTopList());

		// 准备数据：roleList
		//List<Role> roleList = roleService.findAll();
		//ActionContext.getContext().put("roleList", roleList);

		return "saveUI";
	}
	
	*//** 修改 *//*
	public String edit() throws Exception {
		//从数据库中取出原对象
		CarServiceType carServiceType = carService.getCarServiceTypeById(model.getId());

		//设置要修改的属性
		carServiceType.setTitle(model.getTitle());
		carServiceType.setPricePerKM(model.getPricePerKM());
		carServiceType.setPricePerDay(model.getPricePerDay());
		carServiceType.setPersonLimit(model.getPersonLimit());
		//更新到数据库
		carService.updateCarServiceType(carServiceType);

		return "toList";
	}
	public CarServiceType getModel() {
		// TODO Auto-generated method stub
		return model;
	}*/
	
	public CarViolation getModel() {
		// TODO Auto-generated method stub
		return null;
	}
}