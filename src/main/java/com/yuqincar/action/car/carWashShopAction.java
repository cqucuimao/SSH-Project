package com.yuqincar.action.car;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.CarWash;
import com.yuqincar.domain.car.CarWashShop;
import com.yuqincar.service.car.CarWashService;

@Controller
@Scope("prototype")
  public class carWashShopAction extends BaseAction implements ModelDriven<CarWashShop> {
   
	private CarWashShop model = new CarWashShop();
	@Autowired 
	CarWashService carWashService;
	
   public String list(){
	    
	    List<CarWashShop> carWashshop = carWashService.getAllCarWashShop();
		ActionContext.getContext().put("carWashshop", carWashshop);
		return "list";
	    
   }
   public String delete() throws Exception {
	    System.out.println("id="+model.getId());
	    carWashService.deleteCarWashShop(model.getId());
	    
	    return "toList";
    }
  
    public String savaUI()
{
	   
	     return "SaveUI";
}

    public String add(){
	   
	    carWashService.saveCarWashShop(model);

	    return "toList";
   }

    public String editUI()
    {   
    	CarWashShop carWashShop=carWashService.getCarWashShopById(model.getId());
	    ActionContext.getContext().getValueStack().push(carWashShop);
	    return "SaveUI";
    }
    
    public String edit() throws Exception {
    	
		return "toList";
	}
    public boolean isCanDeleteCarWashShop(){
		CarWashShop carWashShop = (CarWashShop) ActionContext.getContext().getValueStack().peek();
		if(carWashService.canDeleteCarWashShop(carWashShop.getId()))
			return true;
		else 
			return false;
	}

  public CarWashShop getModel() {
		return model;
	}

}
