package com.yuqincar.action.car;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarCare;
import com.yuqincar.domain.car.CarInsurance;
import com.yuqincar.domain.car.CommercialInsurance;
import com.yuqincar.domain.car.CommercialInsuranceType;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.service.car.CarInsuranceService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class CommercialInsuranceTypeAction extends BaseAction implements ModelDriven<CommercialInsuranceType>{

	CommercialInsuranceType model = new CommercialInsuranceType();
	
	@Autowired
	private CarInsuranceService carInsuranceService;
	
	//商业保险类型
	public String list() throws Exception{
		List<CommercialInsuranceType> commercialInsuranceTypeList = carInsuranceService.getAllCommercialInsuranceType();
		ActionContext.getContext().put("commercialInsuranceTypeList", commercialInsuranceTypeList);
		return "list";
	}
	
	//添加商业保险类型--界面
	public String addUI() throws Exception{
		
		return "saveUI";
	}
	
	//添加商业保险类型
	public String add() throws Exception{
		carInsuranceService.saveCommercialInsuranceType(model);
		return "toList";
	}
	
	//删除商业保险类型
	public String delete() throws Exception{

		carInsuranceService.deleteCommercialInsuranceType(model.getId());
		return "toList";
	}
	
	//修改商业保险类型--界面
	public String editUI() throws Exception{
		CommercialInsuranceType commercialInsuranceType  = carInsuranceService.getCommercialInsuranceTypeById(model.getId());
		ActionContext.getContext().getValueStack().push(commercialInsuranceType);
		return "saveUI";
	}
	
	public String edit() throws Exception{
		CommercialInsuranceType commercialInsuranceType  = carInsuranceService.getCommercialInsuranceTypeById(model.getId());
		commercialInsuranceType.setName(model.getName());
		carInsuranceService.updateCommercialInsuranceType(commercialInsuranceType);
		return "toList";
	}
	
	//判断商业类型是否能删除
	public boolean isCanDeleteCommercialInsuranceType(){
		CommercialInsuranceType commercialInsuranceType = (CommercialInsuranceType)ActionContext.getContext().getValueStack().peek();
		if(carInsuranceService.canDeleteCommercialInsuranceType(commercialInsuranceType.getId())){
			return true;
		}else{
			return false;
		}
	}
	
	public CommercialInsuranceType getModel() {
		return model;
	}
	
	


}
