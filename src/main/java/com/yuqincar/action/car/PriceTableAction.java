package com.yuqincar.action.car;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarServiceSuperType;
import com.yuqincar.domain.car.CarServiceType;
import com.yuqincar.domain.common.DiskFile;
import com.yuqincar.domain.order.Price;
import com.yuqincar.domain.order.PriceTable;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.common.DiskFileService;
import com.yuqincar.service.order.PriceService;

@Controller
@Scope("prototype")
public class PriceTableAction extends BaseAction{
		
	@Autowired
	private CarService carService;
	
	@Autowired
	private PriceService priceService;
	
	
	private Long priceTableId;
	private Long carServiceSuperTypeId;
	private Long carServiceTypeId;
	private int inputRows;
	private String superTitle;
	private List<String> typeTitle;
	private String actionFlag;
	private String serviceTypeTitle;
	private String typeTitleOption;
	/*价格相关数据*/
	private BigDecimal perDay;
	private BigDecimal perHalfDay;
	private BigDecimal perMileAfterLimit;
	private BigDecimal perHourAfterLimit;
	private BigDecimal perPlaneTime;
	
	/** 价格表列表 */
	public String list(){
		List<PriceTable> priceTableList = priceService.getAllPriceTable();
		ActionContext.getContext().put("priceTableList",priceTableList);
		return "list";
	}
	
	public String detail(){
		DecimalFormat df = new DecimalFormat ("#.0");
		String tmp;
		int index=0;
    	List<CarServiceSuperType> listSuper=new ArrayList<CarServiceSuperType>();
    	Map<CarServiceType, Price> priceMap=new HashMap<CarServiceType, Price>();
    	Map<String, List<List<String>>> mapListResults=new LinkedHashMap<String, List<List<String>>>();
    	listSuper=carService.getAllCarServiceSuperType();
    	PriceTable priceTable=new PriceTable();
    	priceTable=priceService.getPriceTableById(priceTableId);
    	priceMap=priceTable.getCarServiceType();
    	for (int i = 0; i < listSuper.size(); i++) 
    		mapListResults.put(listSuper.get(i).getSuperTitle(), new ArrayList<List<String>>());
        for (CarServiceType key : priceMap.keySet()) 
    	 {
    	        List<String> list=new ArrayList<String>();
	    		list.add(key.getId().toString());
    	        list.add(key.getTitle());
	      		list.add(priceMap.get(key).getPerDay().setScale(0,BigDecimal.ROUND_HALF_UP).toString());
	      		list.add(priceMap.get(key).getPerHalfDay().setScale(0,BigDecimal.ROUND_HALF_UP).toString());
	      		tmp=df.format(priceMap.get(key).getPerMileAfterLimit());
    	        index=tmp.indexOf('.');
    	        if (Integer.parseInt(tmp.substring(index+1, index+2))>0) {
    	        	BigDecimal bigDecimal=new BigDecimal(tmp);
    	        	tmp=df.format(bigDecimal);
    	        	list.add(tmp);
				}else {
					list.add(priceMap.get(key).getPerMileAfterLimit().setScale(0,BigDecimal.ROUND_HALF_UP).toString());
				}
	      		list.add(priceMap.get(key).getPerHourAfterLimit().setScale(0,BigDecimal.ROUND_HALF_UP).toString());
	      		list.add(priceMap.get(key).getPerPlaneTime().setScale(0,BigDecimal.ROUND_HALF_UP).toString());
	      		mapListResults.get(key.getSuperType().getSuperTitle()).add(list);
    	    }
     	ActionContext.getContext().getSession().put("mapList", mapListResults);
		
		return "detail";
	}
	
	public String addUI(){
		
		List<CarServiceSuperType> superTypeList = carService.getAllCarServiceSuperType();
		List<CarServiceType> typeList = new ArrayList<CarServiceType>();
		
		ActionContext.getContext().put("superTypeList", superTypeList);
		ActionContext.getContext().put("typeList", typeList);
		
		return "saveUI";
	}
	
	public String add(){
		PriceTable priceTable = priceService.getPriceTableById(priceTableId);
		//CarServiceSuperType carServiceSuperType = carService.getCarServiceSuperTypeById(carServiceSuperTypeId);
		CarServiceType carServiceType = carService.getCarServiceTypeById(carServiceTypeId);
		Map<CarServiceType,Price> map = priceTable.getCarServiceType();
		Price price = new Price();
		price.setPerDay(perDay);
		price.setPerHalfDay(perHalfDay);
		price.setPerHourAfterLimit(perHourAfterLimit);
		price.setPerMileAfterLimit(perMileAfterLimit);
		price.setPerPlaneTime(perPlaneTime);
		
		map.put(carServiceType, price);
		priceTable.setCarServiceType(map);
		priceService.addPriceTable(priceTable,price);
		return detail();
	}
	
	public String editUI(){
		PriceTable priceTable = priceService.getPriceTableById(priceTableId);
		CarServiceSuperType carServiceSuperType = carService.getCarServiceSuperTypeByTitle(superTitle);
		CarServiceType carServiceType = carService.getCarServiceTypeByTitle(serviceTypeTitle);
		List<CarServiceSuperType> superTypeList = carService.getAllCarServiceSuperType();
		List<CarServiceType> typeList = carService.getAllCarServiceType();
		ActionContext.getContext().put("superTypeList", superTypeList);
		ActionContext.getContext().put("typeList", typeList);
		Price price = priceTable.getCarServiceType().get(carServiceType);
		
		carServiceSuperTypeId = carServiceSuperType.getId();
		carServiceTypeId = carServiceType.getId();
		perDay = price.getPerDay();
		perHalfDay = price.getPerHalfDay();
		perHourAfterLimit = price.getPerHourAfterLimit();
		perMileAfterLimit = price.getPerMileAfterLimit();
		perPlaneTime = price.getPerPlaneTime();
		
		return "saveUI";
	}
	
	public String edit(){

		PriceTable priceTable = priceService.getPriceTableById(priceTableId);
		CarServiceType carServiceType = carService.getCarServiceTypeById(carServiceTypeId);
		Price price = priceTable.getCarServiceType().get(carServiceType);
		price.setPerDay(perDay);
		price.setPerHalfDay(perHalfDay);
		price.setPerHourAfterLimit(perHourAfterLimit);
		price.setPerMileAfterLimit(perMileAfterLimit);
		price.setPerPlaneTime(perPlaneTime);
		
		priceService.updatePriceTable(priceTable, price);
		return detail();
	}
	
	public String delete(){

		PriceTable priceTable = priceService.getPriceTableById(priceTableId);
		CarServiceType carServiceType = carService.getCarServiceTypeByTitle(serviceTypeTitle);
		Price price = priceTable.getCarServiceType().get(carServiceType);
		Map<CarServiceType, Price> map = priceTable.getCarServiceType();
		map.remove(carServiceType,price);
		priceTable.setCarServiceType(map);
		
		priceService.deletePrice(priceTable, price.getId());
		return detail();
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
			if(typeTitle.get(i) != null && !typeTitle.get(i).equals("")){
				CarServiceType carServiceType = new CarServiceType();
				carServiceType.setTitle(typeTitle.get(i));
				carServiceTypes.add(carServiceType);
			}else{
				carServiceTypes = null;
			}
		}
		csst.setSuperTitle(superTitle);
		csst.setTypes(carServiceTypes);
		
		carService.saveCarServiceSuperType(csst);

		return serviceTypeList();
	}
	
	/** 修改页面 */
	public String editServiceTypeUI() throws Exception {
		// 准备回显的数据
		CarServiceSuperType carServiceSuperType = carService.getCarServiceSuperTypeById(carServiceSuperTypeId);
		ActionContext.getContext().getValueStack().push(carServiceSuperType);
		List<CarServiceType> carServiceTypes = carServiceSuperType.getTypes();
		ActionContext.getContext().put("carServiceTypes", carServiceTypes);
		return "serviceTypeSaveUI";
	}
	
	
	/** 修改   只增加superType下的一个type*/
	public String editServiceType() throws Exception {
		System.out.println("carServiceSuperTypeId="+carServiceSuperTypeId);
		//从数据库中取出原对象
		CarServiceSuperType carServiceSuperType = carService.getCarServiceSuperTypeById(carServiceSuperTypeId);
		//设置要修改的属性
		List<CarServiceType> carServiceTypes = carServiceSuperType.getTypes();
		for(int i=0;i<inputRows;i++){
			CarServiceType carServiceType = new CarServiceType();
			if(typeTitle.get(i) != null){
				carServiceType.setTitle(typeTitle.get(i));
			}
			carServiceTypes.add(carServiceType);
		}
		carServiceSuperType.setTypes(carServiceTypes);
		
		//更新到数据库
		carService.updateCarServiceSuperType(carServiceSuperType,inputRows);

		return serviceTypeList();
	}
	/**车型列表*/
	public String serviceTypeList(){
		
		List<CarServiceSuperType> carServiceSuperTypes = carService.getAllCarServiceSuperType();
		ActionContext.getContext().put("carServiceSuperTypes", carServiceSuperTypes);
		
		
		return "serviceTypeList";
	}
	//判断superType是否可以删除，有types的不能删除
	public boolean isCanDeleteCarServiceSuperType(){
		CarServiceSuperType carServiceSuperType = (CarServiceSuperType)ActionContext.getContext().getValueStack().peek();
		if(carServiceSuperType.getTypes() == null ||carServiceSuperType.getTypes().size() == 0)
			return true;
		return false;
	}
	
	public String deleteCarServiceSuperType(){
		System.out.println("carServiceSuperTypeId="+carServiceSuperTypeId);
		carService.deleteCarServiceSuperType(carServiceSuperTypeId);
		return serviceTypeList();
	}
	
	//判断车型能否删除
	public boolean isCanDeleteServiceType(){
		CarServiceType carServiceType = (CarServiceType) ActionContext.getContext().getValueStack().peek();
		
		List<PriceTable> priceTables = priceService.getAllPriceTable();
		for(PriceTable priceTable : priceTables){
			if( !priceTable.getCarServiceType().containsKey(carServiceType) && carService.canDeleteCarServiceType(carServiceType.getId())){
				return true;
			}
		}
		return false;
	}
	
	public String deleteCarServiceType(){
		System.out.println("superId="+carServiceSuperTypeId);
		CarServiceSuperType carServiceSuperType = carService.getCarServiceSuperTypeById(carServiceSuperTypeId);
		CarServiceType carServiceType = carService.getCarServiceTypeById(carServiceTypeId);
		List<CarServiceType> carServiceTypes = carServiceSuperType.getTypes();
		carServiceTypes.remove(carServiceType);
		carService.deleteCarServiceType(carServiceTypeId);
		carServiceSuperType.setTypes(carServiceTypes);
		
		carService.updateCarServiceSuperType(carServiceSuperType, 0);
		return serviceTypeList();
	}
	
	public void getCarServiceTypeOption(){
		CarServiceSuperType carServiceSuperType = carService.getCarServiceSuperTypeById(carServiceSuperTypeId);
		CarServiceTypeVO cstvo = new CarServiceTypeVO();
		List<String> listString = new ArrayList<String>();
		for(CarServiceType carServiceType:carServiceSuperType.getTypes()){
			listString.add(carServiceType.getId()+","+carServiceType.getTitle());
		}
		cstvo.setLists(listString);
		
		String json = JSON.toJSONString(cstvo);
		writeJson(json);
	}
	
	class CarServiceTypeVO{
		List<String> lists;

		public List<String> getLists() {
			return lists;
		}

		public void setLists(List<String> lists) {
			this.lists = lists;
		}
		
	}

	public Long getPriceTableId() {
		return priceTableId;
	}

	public void setPriceTableId(Long priceTableId) {
		this.priceTableId = priceTableId;
	}

	public int getInputRows() {
		return inputRows;
	}
	public void setInputRows(int inputRows) {
		this.inputRows = inputRows;
	}
	public String getSuperTitle() {
		return superTitle;
	}

	public void setSuperTitle(String superTitle) {
		this.superTitle = superTitle;
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

	public Long getCarServiceSuperTypeId() {
		return carServiceSuperTypeId;
	}

	public void setCarServiceSuperTypeId(Long carServiceSuperTypeId) {
		this.carServiceSuperTypeId = carServiceSuperTypeId;
	}

	public Long getCarServiceTypeId() {
		return carServiceTypeId;
	}

	public void setCarServiceTypeId(Long carServiceTypeId) {
		this.carServiceTypeId = carServiceTypeId;
	}

	public BigDecimal getPerDay() {
		return perDay;
	}

	public void setPerDay(BigDecimal perDay) {
		this.perDay = perDay;
	}

	public BigDecimal getPerHalfDay() {
		return perHalfDay;
	}

	public void setPerHalfDay(BigDecimal perHalfDay) {
		this.perHalfDay = perHalfDay;
	}

	public BigDecimal getPerMileAfterLimit() {
		return perMileAfterLimit;
	}

	public void setPerMileAfterLimit(BigDecimal perMileAfterLimit) {
		this.perMileAfterLimit = perMileAfterLimit;
	}

	public BigDecimal getPerHourAfterLimit() {
		return perHourAfterLimit;
	}

	public void setPerHourAfterLimit(BigDecimal perHourAfterLimit) {
		this.perHourAfterLimit = perHourAfterLimit;
	}

	public BigDecimal getPerPlaneTime() {
		return perPlaneTime;
	}

	public void setPerPlaneTime(BigDecimal perPlaneTime) {
		this.perPlaneTime = perPlaneTime;
	}

	public String getServiceTypeTitle() {
		return serviceTypeTitle;
	}

	public void setServiceTypeTitle(String serviceTypeTitle) {
		this.serviceTypeTitle = serviceTypeTitle;
	}

	public String getTypeTitleOption() {
		return typeTitleOption;
	}

	public void setTypeTitleOption(String typeTitleOption) {
		this.typeTitleOption = typeTitleOption;
	}
	
	
}