package com.yuqincar.action.car;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarCare;
import com.yuqincar.domain.car.CarRefuel;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.service.car.CarRefuelService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class CarRefuelAction extends BaseAction implements ModelDriven<CarRefuel> {
	
	private CarRefuel model = new CarRefuel();
	
	@Autowired
	private CarRefuelService carRefuelService;
	
	@Autowired
	private CarService carService;
	
	/** 列表 */
	public String list() throws Exception {
		QueryHelper helper = new QueryHelper(CarRefuel.class, "cr");
		
		if(model.getCar()!=null && model.getCar().getPlateNumber()!=null && !""
				.equals(model.getCar().getPlateNumber()))
			helper.addWhereCondition("cr.car.plateNumber like ?", 
					"%"+model.getCar().getPlateNumber()+"%");
		
		//if(model.getMoney()!=null && !"".equals(model.getMoney()))
			//helper.addWhereCondition("cr.money=?", model.getMoney());
		
		PageBean pageBean = carRefuelService.queryCarRefuel(pageNum, helper);
		
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("carRefuelHelper", helper);
		return "list";
	}
	
	/** 添加页面 */
	public String addUI() throws Exception {
		return "saveUI";
	}
	
	/** 添加 */
	public String add() throws Exception {
		// 保存到数据库
		
		Car car1 = carService.getCarByPlateNumber(model.getCar().getPlateNumber());
		
		model.setCar(car1);
		
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

}
