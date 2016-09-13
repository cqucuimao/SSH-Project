package com.yuqincar.action.car;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarServiceSuperType;
import com.yuqincar.domain.car.CarServiceType;
import com.yuqincar.domain.common.DiskFile;
import com.yuqincar.domain.order.PriceTable;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.common.DiskFileService;
import com.yuqincar.service.order.PriceService;

@Controller
@Scope("prototype")
public class PriceTableAction extends BaseAction implements ModelDriven<CarServiceType>{
	
	private CarServiceType model = new CarServiceType();
		
	@Autowired
	private CarService carService;
	
	@Autowired
	private PriceService priceService;
	
	private Long orderId;
	private int inputRows;
	private String superTypeTitle;
	private List<String> typeTitle;
	private String actionFlag;
	
	/** 列表 */
	public String list() throws Exception {
		List<PriceTable> priceTableList = priceService.getAllPriceTable();
		ActionContext.getContext().put("priceTableList",priceTableList);
		return "list";
		
	}
	/** 删除 */
	public String delete() throws Exception {
		if(carService.canDeleteCarServiceType(model.getId()))
			carService.deleteCarServiceType(model.getId());
		return "toList";
	}
	
	/** 添加页面 */
	public String addServiceTypeUI() throws Exception {
		return "serviceTypeSaveUI";
	}
	
	/** 添加 */
	public String addServiceType() throws Exception {
		
		CarServiceSuperType csst = new CarServiceSuperType();
		List<CarServiceType> carServiceTypes = new ArrayList<CarServiceType>();
		for(int i=0;i<inputRows;i++){
			CarServiceType carServiceType = new CarServiceType();
			if(typeTitle.get(i) != null){
				carServiceType.setTitle(typeTitle.get(i));
			}
			carServiceTypes.add(carServiceType);
		}
		csst.setTitle(superTypeTitle);
		csst.setTypes(carServiceTypes);
		
		carService.saveCarServiceSuperType(csst);

		return serviceTypeList();
	}
	
	/** 修改页面 */
	public String editServiceTypeUI() throws Exception {
		// 准备回显的数据
		CarServiceSuperType carServiceSuperType = carService.getCarServiceSuperTypeByTitle(superTypeTitle);
		ActionContext.getContext().getValueStack().push(carServiceSuperType);
		List<CarServiceType> carServiceTypes = carServiceSuperType.getTypes();
		ActionContext.getContext().put("carServiceTypes", carServiceTypes);
		return "serviceTypeSaveUI";
	}
	
	
	/** 修改 */
	public String editServiceType() throws Exception {
		//从数据库中取出原对象
		CarServiceSuperType carServiceSuperType = carService.getCarServiceSuperTypeByTitle(superTypeTitle);
		//设置要修改的属性
		
		//更新到数据库
		//carService.updateCarServiceType(carServiceType);

		return "toList";
	}
	
	public String serviceTypeList(){
		//List<CarServiceType> carServiceTypes = carService.getAllCarServiceType();
		
		List<CarServiceSuperType> carServiceSuperTypes = carService.getAllCarServiceSuperType();
		Map<String, List<String>> mapList=new LinkedHashMap<String, List<String>>();
		
		for(int i=0;i<carServiceSuperTypes.size();i++){
			List<String> carServiceTypeTitles = new ArrayList<String>();
			for(int j=0;j<carServiceSuperTypes.get(i).getTypes().size();j++){
				carServiceTypeTitles.add(carServiceSuperTypes.get(i).getTypes().get(j).getTitle());
			}
			mapList.put(carServiceSuperTypes.get(i).getTitle(),carServiceTypeTitles );
		}
		
		ActionContext.getContext().getSession().put("mapList", mapList);
		
		return "serviceTypeList";
	}
	
	//判断车型能否删除
	public boolean isCanDeleteServiceType(){
		CarServiceType carServiceType = (CarServiceType) ActionContext.getContext().getValueStack().peek();
		if(carService.canDeleteCarServiceType(carServiceType.getId()))
			return true;
		else 
			return false;
	}
	
	
	
	public CarServiceType getModel() {
		return model;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public int getInputRows() {
		return inputRows;
	}
	public void setInputRows(int inputRows) {
		this.inputRows = inputRows;
	}
	public String getSuperTypeTitle() {
		return superTypeTitle;
	}
	public void setSuperTypeTitle(String superTypeTitle) {
		this.superTypeTitle = superTypeTitle;
	}
	public List<String> getTypeTitle() {
		return typeTitle;
	}
	public void setTypeTitle(List<String> typeTitle) {
		this.typeTitle = typeTitle;
	}
	public String getActionFlag() {
		return actionFlag;
	}
	public void setActionFlag(String actionFlag) {
		this.actionFlag = actionFlag;
	}
	
}