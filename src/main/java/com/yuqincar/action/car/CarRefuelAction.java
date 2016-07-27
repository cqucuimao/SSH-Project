package com.yuqincar.action.car;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.record.OldCellRecord;
import org.apache.struts2.ServletActionContext;
import org.bouncycastle.jce.provider.JDKDSASigner.stdDSA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.pdf.hyphenation.TernaryTree.Iterator;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.dao.car.CarRefuelDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarRefuel;
import com.yuqincar.domain.car.TollCharge;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.car.CarRefuelService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.common.ExportService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.ExcelUtil;
import com.yuqincar.utils.QueryHelper;

import freemarker.template.utility.DateUtil;

@Controller
@Scope("prototype")
public class CarRefuelAction extends BaseAction implements ModelDriven<CarRefuel> {
	
	private CarRefuel model = new CarRefuel();
	
	@Autowired
	private CarRefuelService carRefuelService;
	
	@Autowired
	private CarService carService;
	
	@Autowired
	private ExportService exportservice;
	
	
	@Autowired
	private CarRefuelDao carRefueldao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderService orderService;
	
	private Date date1;
	
	private Date date2;
	
	private File upload;
	
	private String uploadFileName;
	
	private String uploadContentType;
	
	private String unknownCarOrDriver;
	
	private int result;
	
	private String gDate;
	/** 查询 */
	public String queryList(){
		QueryHelper helper = new QueryHelper(CarRefuel.class, "cr");
		
		if(model.getCar()!=null && model.getCar().getPlateNumber()!=null && !""
				.equals(model.getCar().getPlateNumber()))
			helper.addWhereCondition("cr.car.plateNumber like ?", 
					"%"+model.getCar().getPlateNumber()+"%");
		if(date1!=null && date2!=null)
			helper.addWhereCondition("(TO_DAYS(cr.date)-TO_DAYS(?))>=0 and (TO_DAYS(?)-TO_DAYS(cr.date))>=0", 
					date1 ,date2);
		else if(date1==null && date2!=null)
			helper.addWhereCondition("(TO_DAYS(?)-TO_DAYS(cr.date))>=0", date2);
		else if(date1!=null && date2==null)
			helper.addWhereCondition("(TO_DAYS(cr.date)-TO_DAYS(?))>=0", date1);
		
		helper.addOrderByProperty("cr.id", false);
		PageBean pageBean = carRefuelService.queryCarRefuel(pageNum, helper);
		
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("carRefuelHelper", helper);
		return "list";
	}
	
	/** 列表 */
	public String list(){
		QueryHelper helper = new QueryHelper(CarRefuel.class, "cr");
		helper.addOrderByProperty("cr.id", false);
		PageBean pageBean = carRefuelService.queryCarRefuel(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("carRefuelHelper", helper);
		return "list";
	}

	public String freshList(){
		QueryHelper helper = (QueryHelper)ActionContext.getContext().getSession().get("carRefuelHelper");
		PageBean<CarRefuel> pageBean = carRefuelService.queryCarRefuel(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
	
	/** 添加页面 */
	public String addUI() throws Exception {
		System.out.println("***************");
		return "saveUI";
	}
	
	/** 导入excel页面 */
	public String excel() throws Exception {
		return "excel";
	}
	/**
	 * 导入excel
	 * @throws Exception
	 */
	public String importExcelFile() throws Exception{
		InputStream is = new FileInputStream(upload);
		
		ExcelUtil eu = new ExcelUtil();
		List<List<String>> excelLines = eu.getLinesFromExcel(is, 1, 1, 7, 0);
		List<CarRefuel> carRefuels = new ArrayList<CarRefuel>();
		for(int i=1;i<excelLines.size();i++){
			try {
				result = i;
				CarRefuel cr = new CarRefuel();
				//流水号
				//System.out.println("流水号="+excelLines.get(i).get(0));
				cr.setSn(excelLines.get(i).get(0));
				//金额
				//System.out.println("金额="+excelLines.get(i).get(1));
				BigDecimal bd = new BigDecimal(excelLines.get(i).get(1));
				cr.setMoney(bd);
				//油量
				//System.out.println("油量="+excelLines.get(i).get(2));
				float volume = Float.parseFloat(excelLines.get(i).get(2));
				cr.setVolume(volume);
				//车辆
				//System.out.println("车牌号="+excelLines.get(i).get(3));
				Car car = carService.getCarByPlateNumber(excelLines.get(i).get(3));
				if(car == null){
					unknownCarOrDriver = "未知车辆";
					ActionContext.getContext().getValueStack().push(unknownCarOrDriver);
					ActionContext.getContext().getValueStack().push(result);
					return "false";
				}else{
					cr.setCar(car);
				}
				//司机
				//System.out.println("司机="+excelLines.get(i).get(4));
				User driver = userService.getByLoginName(excelLines.get(i).get(4));
				if(driver == null){
					unknownCarOrDriver = "未知司机";
					ActionContext.getContext().getValueStack().push(unknownCarOrDriver);
					ActionContext.getContext().getValueStack().push(result);
					return "false";
				}else{
					cr.setDriver(driver);				
				}
				//交易时间
				//System.out.println("交易时间="+excelLines.get(i).get(5));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");  
			    Date date;				
				date = sdf.parse(excelLines.get(i).get(5));
				cr.setDate(date);
				//是否外购
				System.out.println("是否外购="+excelLines.get(i).get(6));
				String outSource = excelLines.get(i).get(6);
				if(outSource == "是" || outSource.equals("是")){
					cr.setOutSource(true);
				}else{
					 cr.setOutSource(false);
				}
				carRefuels.add(cr);
			} catch (Exception e) {
				unknownCarOrDriver = "不明原因";
				ActionContext.getContext().getValueStack().push(unknownCarOrDriver);
				ActionContext.getContext().getValueStack().push(result);
				return "false";
			}	
		}	
		result = excelLines.size() - 1;
		carRefuelService.importExcelFile(carRefuels);
		ActionContext.getContext().getValueStack().push(result);
		return "success";		
	}
	//到处报表弹出时间函数
     public String outPutOil_time() {
		return "outPutOil_time";
	}
	
     //实现报表导出具体的操作
     public void OilReport() throws ParseException, IOException{
    	 DateUtils du = new DateUtils();
    	 SimpleDateFormat format=new SimpleDateFormat("yyyy-MM");
    	 Date date=format.parse(gDate);
    	 Date date3 = du.getFirstDateOfMonth(date);
    	 Date date4 = du.getEndDateOfMonth(date);
    	 QueryHelper helper = new QueryHelper(CarRefuel.class, "cr");
 		 if(date3!=null && date4!=null)
 			helper.addWhereCondition("(TO_DAYS(cr.date)-TO_DAYS(?))>=0 and (TO_DAYS(?)-TO_DAYS(cr.date))>=0", 
 					date3 ,date4);
 		helper.addOrderByProperty("cr.car.plateNumber", true);
 		List lists=new ArrayList<CarRefuel>();
 		//PageBean pageBean = carRefuelService.queryCarRefuel(pageNum, helper);
 		//lists=pageBean.getRecordList(); 
 		lists=carRefuelService.getAllCarRefuel(helper);
 		System.out.println("+++++++++++++list_size+++++++++++++"+lists.size());
 		
 		CarRefuel carRefuel=new CarRefuel();
   	    List<List<String>> listAll = new ArrayList<List<String>>();
    	  
 		for(int j=0;j<lists.size();j++){
	    carRefuel=(CarRefuel) lists.get(j);
	    System.out.println("carRefuel=　　"+carRefuel.getCar().getPlateNumber()+" "+carRefuel.getDriver().getName());
		if(carRefuel.isOutSource()){
		   	    int flag=1;
			    for(int h=0;h<listAll.size();h++)
			    {
			    	//如果外购油已经存在list中了。
			    	//System.out.println("carRefuel.getCar().getPlateNumber()"+carRefuel.getCar().getPlateNumber());
			        if(listAll.get(h).get(1).equals(carRefuel.getCar().getPlateNumber()))
			        {
				        String volumeString=listAll.get(h).get(5);
				        String moneyString=listAll.get(h).get(6);
				        String name_old=listAll.get(h).get(4);
				        if (volumeString.length()!=0) 
				        {
					         float volume_old=Float.parseFloat(volumeString);
					         float volume_new=carRefuel.getVolume();
					         volume_old=volume_new+volume_old;
					         listAll.get(h).set(5,String.valueOf(volume_old));
						}else 
						{
							 listAll.get(h).set(5,String.valueOf(carRefuel.getVolume()));
						}
				        if (moneyString.length()!=0) {
				        	 BigDecimal money_old=new BigDecimal(moneyString);
						     BigDecimal money_new=carRefuel.getMoney();
						     BigDecimal money=money_new.add(money_old);
						     listAll.get(h).set(6, money.toString());
						}else 
						{
							 listAll.get(h).set(6, carRefuel.getMoney().toString());
						}
				        
				        if (name_old.length()!=0) {
				        	String name_new=carRefuel.getDriver().getName();
				        	if(!(name_old.contains(name_new)))
				        	{
					        	name_old=name_old+","+name_new;
					        	listAll.get(h).set(4, name_old);
					        }
						}else 
						{
							listAll.get(h).set(4, carRefuel.getDriver().getName());
						}
				        
				        flag=0;
				        //System.out.println("in_flag0=: "+flag);
				        break;
			        }
			  }
			  if(flag==1)
			  {
				    //外购油不存在的情况下
				  
				    //System.out.println("out_flag=: "+flag);
				    //System.out.println(carRefuel.getCar().getPlateNumber());
				    List<String> oiList = new ArrayList<String>();
			        float volume=carRefuel.getVolume();
			        BigDecimal money=carRefuel.getMoney();
			        oiList.add("");
			        oiList.add(carRefuel.getCar().getPlateNumber());
			        oiList.add("");
			        oiList.add("");
			        oiList.add(carRefuel.getDriver().getName());
			        oiList.add(String.valueOf(volume));
			        oiList.add(String.valueOf(money));
			        oiList.add("");
			        oiList.add("");
			        listAll.add(oiList);
			  }
			  
		} 
	   else{
			
				int flag2=1;
			    for(int s=0;s<listAll.size();s++){
			    	//如果非外购油已经存在list中了。
			        if(listAll.get(s).get(1).equals(carRefuel.getCar().getPlateNumber()))
			        {
			        	    String volumeString=listAll.get(s).get(2);
					        String moneyString=listAll.get(s).get(3);
					        String name_old=listAll.get(s).get(0);
					        if (volumeString.length()!=0) 
					        {
						         float volume_old=Float.parseFloat(volumeString);
						         float volume_new=carRefuel.getVolume();
						         volume_old=volume_new+volume_old;
						         listAll.get(s).set(2,String.valueOf(volume_old));
							}else 
							{
								 listAll.get(s).set(2,String.valueOf(carRefuel.getVolume()));
							}
					        if (moneyString.length()!=0) {
					        	 BigDecimal money_old=new BigDecimal(moneyString);
							     BigDecimal money_new=carRefuel.getMoney();
							     BigDecimal money=money_new.add(money_old);
							     listAll.get(s).set(3, money.toString());
							}else 
							{
								 listAll.get(s).set(3, carRefuel.getMoney().toString());
							}
					        
					        if (name_old.length()!=0) {
					        	String name_new=carRefuel.getDriver().getName();
					        	if(!(name_old.contains(name_new)))
					        	{
						        	name_old=name_old+","+name_new;
						        	listAll.get(s).set(0, name_old);
						        }
							}else 
							{
								listAll.get(s).set(0, carRefuel.getDriver().getName());
							}
					        
					        flag2=0;
					        //System.out.println("in_flag0=: "+flag);
					        break;
			        }
			  }
			  if(flag2==1){
				    //非外购油不存在的情况下
				    List<String> oiList = new ArrayList<String>();
			        float volume=carRefuel.getVolume();
			        BigDecimal money=carRefuel.getMoney();
			        oiList.add(carRefuel.getDriver().getName());
			        oiList.add(carRefuel.getCar().getPlateNumber());
			        oiList.add(String.valueOf(volume));
			        oiList.add(String.valueOf(money));
			        oiList.add("");
			        oiList.add("");
			        oiList.add("");
			        oiList.add("");
			        oiList.add("");
			        listAll.add(oiList);
			  }
		       	
		}
		
 }
 		 List<String> list_add=new ArrayList<String>();
 		
 		//统计总金额
 		for(int k=0;k<listAll.size();k++)
 		{    
 			  list_add=listAll.get(k);
 			  String v_in=list_add.get(2);
 			  String m_in=list_add.get(3);
 			  String v_out=list_add.get(5);
 			  String m_out=list_add.get(6);
 			  //合并油量小计
 			  if (v_in.length()!=0 && v_out.length()!=0) 
 			  {
				 float v_is=Float.parseFloat(v_out);
				 float v_no=Float.parseFloat(v_in);
				 v_is=v_is+v_no;
				 listAll.get(k).set(7,String.valueOf(v_is));
			  }
 			  else if(v_in.length()==0 && v_out.length()!=0)
 			  {
 				  listAll.get(k).set(7, v_out);
 			  }
 			  else if(v_in.length()!=0 && v_out.length()==0)
 			  {
 				 listAll.get(k).set(7, v_in);
 			  }
 			  //合并金额小计
 			 if (m_in.length()!=0 && m_out.length()!=0) 
			  {
 				 BigDecimal m_is=new BigDecimal(m_in);
 				 BigDecimal m_no=new BigDecimal(m_out);
			     BigDecimal moneyall=m_is.add(m_no);
				 listAll.get(k).set(8,moneyall.toString());
			  }
			  else if(m_in.length()==0 && m_out.length()!=0)
			  {
				  listAll.get(k).set(8, m_out);
			  }
			  else if(m_in.length()!=0 && m_out.length()==0)
			  {
				 listAll.get(k).set(8, m_in);
			  }
 			
 		}
 		
 		/* System.out.println(listAll.size());
 		 for (int k=0;k<listAll.size();k++)
 		 {
 			 System.out.println(listAll.get(k).toString());
   	      }*/
 		 List<String> title=new ArrayList<String>();
 		 title.add("姓名");
 		 title.add("车号");
 		 title.add("油站油量");
 		 title.add("油站金额");
 		 title.add("姓名");
 		 title.add("外购油量");
 		 title.add("外购金额");
 		 title.add("油量小计");
 		 title.add("金额小计");
 		 
 		 String report_name="重庆渝勤公司油料表"+" "+gDate.substring(0,4)+"年"+gDate.substring(5,7)+"月";
 		 //exportservice.exportExcel(report_name, title, listAll, response);
         System.out.println("this is coming here******************");
 }
	
     /** 添加 */
	public String add() throws Exception {
		// 保存到数据库
		
		Car car = carService.getCarByPlateNumber(model.getCar().getPlateNumber());
		User driver = userService.getById(model.getDriver().getId());	
		model.setCar(car);
		model.setDriver(driver);
		carRefuelService.saveCarRefuel(model);
		ActionContext.getContext().getValueStack().push(new CarRefuel());
		return freshList();
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

	public Date getDate1() {
		return date1;
	}

	public void setDate1(Date date1) {
		this.date1 = date1;
	}

	public Date getDate2() {
		return date2;
	}

	public void setDate2(Date date2) {
		this.date2 = date2;
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

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getUnknownCarOrDriver() {
		return unknownCarOrDriver;
	}

	public void setUnknownCarOrDriver(String unknownCarOrDriver) {
		this.unknownCarOrDriver = unknownCarOrDriver;
	}

	public String getgDate() {
		return gDate;
	}

	public void setgDate(String gDate) {
		this.gDate = gDate;
	}

	
	
	
	
	
}
