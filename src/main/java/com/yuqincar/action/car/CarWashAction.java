package com.yuqincar.action.car;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
import com.yuqincar.dao.car.impl.CarWashShopDaoImpl;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarCare;
import com.yuqincar.domain.car.CarWash;
import com.yuqincar.domain.car.CarWashShop;
import com.yuqincar.domain.car.Material;
import com.yuqincar.domain.car.ServicePoint;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.car.CarWashService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.ExcelUtil;
import com.yuqincar.utils.QueryHelper;
@Controller
@Scope("prototype")
public class CarWashAction extends BaseAction implements ModelDriven<CarWash> {
	
	private CarWash model = new CarWash();
	
	@Autowired
	private CarWashService carWashService;
	
	@Autowired
	private CarService carService;
	
	@Autowired
	private UserService userService;
    private File upload;
	
	private String uploadFileName;
	
	private String uploadContentType;
	
	private String failReason;
	
	private int result;
	
	private Date beginDate;
	
	private Date endDate;
	
	private String carShop;
	
	private CarWashShop carWashShop;
	
	private Long carId;

	//头部快速查询
	public String queryForm(){
		QueryHelper helper = new QueryHelper("CarWash", "cw");
		if(model.getCar()!=null)
			helper.addWhereCondition("cw.car=?", model.getCar());
		if(beginDate!=null && endDate!=null)
			helper.addWhereCondition("(TO_DAYS(cw.date)-TO_DAYS(?))>=0 and (TO_DAYS(?)-TO_DAYS(cw.date))>=0", 
					beginDate ,endDate);
		else if(beginDate==null && endDate!=null)
			helper.addWhereCondition("(TO_DAYS(?)-TO_DAYS(cw.date))>=0", endDate);
		else if(beginDate!=null && endDate==null)
			helper.addWhereCondition("(TO_DAYS(cw.date)-TO_DAYS(?))>=0", beginDate);
		helper.addOrderByProperty("cw.date", false);
		
		PageBean pageBean = carWashService.queryCarWash(pageNum, helper);		
		ActionContext.getContext().getValueStack().push(pageBean);		
		ActionContext.getContext().getSession().put("carWashHelper", helper);
		return "list";		
	}
	//页面初加载数据填充
   public String list() {
	    QueryHelper helper = new QueryHelper("CarWash", "cw");
	    if(carId!=null)
			helper.addWhereCondition("cw.car.id=?", carId);
		helper.addOrderByProperty("cw.id", false);
		PageBean<CarWash> pageBean = carWashService.queryCarWash(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("carWashHelper", helper);
		return "list";
	}
   
   //翻页的时候保留条件并显示数据
   public String freshList(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("carWashHelper");
		PageBean<CarWash> pageBean = carWashService.queryCarWash(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
   public String delete(){
       carWashService.deleteCarWash(model.getId());
	   return freshList();
	}
   
   public String excel() throws Exception {
		return "excel";
	}
   
   public String importExcelFile() throws Exception{
		InputStream is = new FileInputStream(upload);
		ExcelUtil eu = new ExcelUtil();
		List<List<String>> excelLines = eu.getLinesFromExcel(is, 1, 1, 9, 0);
		List<CarWash> carWash = new ArrayList<CarWash>();
		List<CarWashShop>  shoplists=new ArrayList<CarWashShop>();
		boolean flag=false;
		shoplists=carWashService.getAllCarWashShop();
		for(int i=1;i<excelLines.size();i++){
					
		        try {       
		        	        
		        	        result=i;
							CarWash cw = new CarWash();
							
							//车辆
							//System.out.println("车牌号="+excelLines.get(i).get(0));
							Car car = carService.getCarByPlateNumber(excelLines.get(i).get(0));
							if(car == null){
								failReason = "未知车辆";
								ActionContext.getContext().getValueStack().push(failReason);
								ActionContext.getContext().getValueStack().push(result);
								return "false";
							}else{
								cw.setCar(car);
							}
							
							//司机
							String name = excelLines.get(i).get(1).replaceAll( "\\s", "" );
							User driver = userService.getByLoginName(name);
							if(driver == null){
								failReason = "未知司机";
								ActionContext.getContext().getValueStack().push(failReason);
								ActionContext.getContext().getValueStack().push(result);
								return "false";
							}else{
								cw.setDriver(driver);				
							}
							
							//洗车时间
							//System.out.println("洗车时间="+excelLines.get(i).get(2));
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");  
						    Date date;				
							date = sdf.parse(excelLines.get(i).get(2));
							cw.setDate(date);
							
							//洗车点管理
							//System.out.println("洗车点="+excelLines.get(i).get(3));
							
							 carShop = excelLines.get(i).get(3);
							for(int j=0;j<shoplists.size();j++)
							{
								carWashShop=shoplists.get(j);
								//System.out.println(shoplists.get(j));
								if(carShop.equals(carWashShop.getName()))
								{
									flag=true;
									break;
								}
							}
							if(flag){
								cw.setShop(carWashShop);
							}
							else{
								failReason = "未知洗车点";
								ActionContext.getContext().getValueStack().push(failReason);
								ActionContext.getContext().getValueStack().push(result);
								return "false";
							}
							
							flag=false;
							
							//金额
							//System.out.println("金额="+excelLines.get(i).get(4));
							BigDecimal bd = new BigDecimal(excelLines.get(i).get(4));
							cw.setMoney(bd);
							
							
							BigDecimal inner = new BigDecimal(excelLines.get(i).get(5));
							cw.setInnerCleanMoney(inner);
							BigDecimal po = new BigDecimal(excelLines.get(i).get(6));
							cw.setPolishingMoney(po);
							BigDecimal en = new BigDecimal(excelLines.get(i).get(7));
							cw.setEngineCleanMoney(en);
							BigDecimal cu = new BigDecimal(excelLines.get(i).get(8));
							cw.setCushionCleanMoney(cu);
							
							carWash.add(cw);
							
						} catch (Exception e) {
							failReason = "不明原因";
							ActionContext.getContext().getValueStack().push(failReason);
							ActionContext.getContext().getValueStack().push(result);
							return "false";
						}
				}
			
		result = excelLines.size() - 1;
		carWashService.importExcelFile(carWash);
		ActionContext.getContext().getValueStack().push(result);
		return "success";	
	}
   
   public String saveUI(){
	   
	   ActionContext.getContext().put("carWashShopList", carWashService.getAllCarWashShop());
	   return "saveUI";
	}
   
   public String save(){
	    CarWashShop carWashShop=carWashService.getCarWashShopById(model.getShop().getId());
		model.setShop(carWashShop);
		carWashService.saveCarWash(model);
		ActionContext.getContext().getValueStack().push(new CarWash());
		return freshList();
	}
   
   public String editUI(){
	    ActionContext.getContext().put("carWashShopList", carWashService.getAllCarWashShop());
	    CarWash carWash = carWashService.getCarWashById(model.getId());
		ActionContext.getContext().getValueStack().push(carWash);
		return "saveUI";
	}
   
   public String edit(){
		  
	    CarWash carWash = carWashService.getCarWashById(model.getId());
		CarWashShop carWashShop=carWashService.getCarWashShopById(model.getShop().getId());
		carWash.setCar(model.getCar());
		carWash.setDriver(model.getDriver());
		carWash.setShop(carWashShop);
		carWash.setMoney(model.getMoney());
		carWash.setDate(model.getDate());
		carWash.setInnerCleanMoney(model.getInnerCleanMoney());
		carWash.setPolishingMoney(model.getPolishingMoney());
		carWash.setEngineCleanMoney(model.getEngineCleanMoney());
		carWash.setCushionCleanMoney(model.getCushionCleanMoney());
		carWashService.updateCarWash(carWash);
		ActionContext.getContext().getValueStack().push(new CarWash());
		return freshList();
	}
 
   
   
   public CarWash getModel() {
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
	public File getUpload() {
		return upload;
	}
	public void setUpload(File upload) {
		this.upload = upload;
	}
	public String getUploadFileName() {
		return uploadFileName;
	}
	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	public String getUploadContentType() {
		return uploadContentType;
	}
	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}
	public String getFailReason() {
		return failReason;
	}
	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public Long getCarId() {
		return carId;
	}
	public void setCarId(Long carId) {
		this.carId = carId;
	}
}	

