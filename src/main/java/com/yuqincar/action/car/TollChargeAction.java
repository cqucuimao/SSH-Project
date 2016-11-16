package com.yuqincar.action.car;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.dao.order.CarServiceSuperTypeDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarStatusEnum;
import com.yuqincar.domain.car.TollCharge;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.CustomerOrganization.CustomerOrganizationService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.car.TollChargeService;
import com.yuqincar.service.common.ExportService;
import com.yuqincar.service.order.PriceService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class TollChargeAction extends BaseAction implements ModelDriven<TollCharge> {
	
	private TollCharge model;
	
	@Autowired
	private TollChargeService tollChargeService;
	
	@Autowired
	private CarService carService;
	
	@Autowired 
	PriceService priceSerivce;
	@Autowired 
	CarServiceSuperTypeDao carServiceSuperTypeDao;
	
	@Autowired
	UserService userService;
	
	@Autowired
	private ExportService exportservice;
	
	@Autowired
	CustomerOrganizationService organizationService;
	
	private Date beginDate;
	
	private Date endDate;

	private Long carId;

	public String queryForm(){
		QueryHelper helper = new QueryHelper("TollCharge", "tc");
		
		if(model.getCar()!=null)
			helper.addWhereCondition("tc.car=?", model.getCar());
		
		if(beginDate!=null && endDate!=null)
			helper.addWhereCondition("(TO_DAYS(tc.payDate)-TO_DAYS(?))>=0 and (TO_DAYS(?)-TO_DAYS(tc.payDate))>=0", 
					beginDate ,endDate);
		else if(beginDate==null && endDate!=null)
			helper.addWhereCondition("(TO_DAYS(?)-TO_DAYS(tc.payDate))>=0", endDate);
		else if(beginDate!=null && endDate==null)
			helper.addWhereCondition("(TO_DAYS(tc.payDate)-TO_DAYS(?))>=0", beginDate);
		helper.addOrderByProperty("tc.payDate", false);
		
		PageBean pageBean = tollChargeService.queryTollCharge(pageNum, helper);		
		ActionContext.getContext().getValueStack().push(pageBean);		
		ActionContext.getContext().getSession().put("tollChargeHelper", helper);
		return "list";		
	}

	public String list() {
		System.out.println("in list");
		QueryHelper helper = new QueryHelper("TollCharge", "tc");
		if(carId!=null)
			helper.addWhereCondition("tc.car.id=?", carId);
		helper.addOrderByProperty("tc.id", false);
		PageBean<TollCharge> pageBean = tollChargeService.queryTollCharge(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("tollChargeHelper", helper);
		dealProblems();
		return "list"; 
	}
	
	public String freshList(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("tollChargeHelper");
		PageBean<TollCharge> pageBean = tollChargeService.queryTollCharge(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
	
	public String delete(){
		tollChargeService.deleteTollCharge(model.getId());
		return freshList();
	}
	
	public String saveUI(){
		return "saveUI";
	}
	
	public String save(){
		tollChargeService.saveTollCharge(model);
		ActionContext.getContext().getValueStack().push(new TollCharge());
		return freshList();
	}
	
	public String editUI(){
		TollCharge tollCharge = tollChargeService.getTollChargeById(model.getId());
		ActionContext.getContext().getValueStack().push(tollCharge);
		ActionContext.getContext().put("type", "edit");
		User user=(User)request.getSession().getAttribute("user");
		ActionContext.getContext().put("editKeyInfo", user.hasPrivilegeByUrl("/tollCharge_editKeyInfo"));
		ActionContext.getContext().put("editNormalInfo", user.hasPrivilegeByUrl("/tollCharge_editNormalInfo"));
		return "saveUI";
	}
	
	public String edit(){
		User user=(User)request.getSession().getAttribute("user");
		TollCharge tollCharge = tollChargeService.getTollChargeById(model.getId());
		if (user.hasPrivilegeByUrl("/tollCharge_editNormalInfo")) {
		tollCharge.setCar(model.getCar());
		tollCharge.setNextPayDate(model.getNextPayDate());
		}
		
		if (user.hasPrivilegeByUrl("/tollCharge_editNormalInfo")) {
		tollCharge.setPayDate(model.getPayDate());
		tollCharge.setMoney(model.getMoney());
		tollCharge.setOverdueFine(model.getOverdueFine());
		tollCharge.setMoneyForCardReplace(model.getMoneyForCardReplace());
		}
		
		tollChargeService.updateTollCharge(tollCharge);
		ActionContext.getContext().getValueStack().push(new TollCharge());
		return freshList();
	}
		
	public String remind(){
		QueryHelper helper = new QueryHelper("Car", "c");
		helper.addWhereCondition("c.nextTollChargeDate is not null");
		helper.addWhereCondition("c.status=?", CarStatusEnum.NORMAL);
		helper.addWhereCondition("c.borrowed=?", false);
		PageBean<Car> pageBean = carService.queryCar(pageNum, helper);
		List<RemindVO> voList=new ArrayList<RemindVO>(pageBean.getRecordList().size());
		for(Car car:pageBean.getRecordList()){
			RemindVO vo=new RemindVO();
			vo.setCar(car);
			vo.setNextPayDate(DateUtils.getYMDString(car.getNextTollChargeDate()));
			TollCharge tc=tollChargeService.getRecentTollCharge(car);
			if(tc!=null)
				vo.setPayDate(DateUtils.getYMDString(tc.getPayDate()));
			else
				vo.setPayDate("");
			voList.add(vo);
		}
		pageBean.setRecordList(voList);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "remind";
	}
	
	//导出路桥费提醒
	public void exportRemind() throws IOException{
		QueryHelper helper = new QueryHelper("Car", "c");
		helper.addWhereCondition("c.nextTollChargeDate is not null");
		helper.addWhereCondition("c.status=?", CarStatusEnum.NORMAL);
		helper.addWhereCondition("c.borrowed=?", false);
		//获得所有需要提醒路桥费的车辆
		List<Car> cars = tollChargeService.getAllNeedTollCharge(helper);
		
		//表名
		String excelName = "重庆渝勤公司车辆路桥费提醒表";
		
		//表格的title
		List<String> title = new ArrayList<String>();
		title.add("车牌号");
		title.add("上次缴款日期");
		title.add("下次缴款日期");
		
		//导出到excel中的类型
		List<List<String>> allList = new ArrayList<List<String>>();
		
		
		for(int i=0;i<cars.size();i++){
			//excel表中的每一行（车牌号，上次缴款日期，下次缴款日期）
			List<String> excelCarsList = new ArrayList<String>();
			//车牌号（车牌号不能为空）
			excelCarsList.add(cars.get(i).getPlateNumber());
			//上次缴款日期，日期只保留年月日，可能为空
			if(tollChargeService.getRecentTollCharge(cars.get(i)) != null){
				excelCarsList.add(DateUtils.getYMDString(tollChargeService.getRecentTollCharge(cars.get(i)).getPayDate()));
			}else{
				excelCarsList.add("");
			}
			//下次缴款日期，日期只保留年月日
			excelCarsList.add(DateUtils.getYMDString(cars.get(i).getNextTollChargeDate()));
			
			allList.add(excelCarsList);
		}
		//调用exportservice中的exportExcel接口实现表格的导出
		exportservice.exportExcel(excelName, title, allList, response);
	}
	
	//由于程序的错误，导致部分车辆的nextTollChargeDate和tollChargeExpired有误。所以写这个方法进行弥补。
	//在tollCharge_list中调用一次，通过页面访问调用一次。
	private void dealProblems(){
		for(Car car:carService.getAll()){
			if(car.getStatus()==CarStatusEnum.SCRAPPED || car.isBorrowed())
				continue;
			QueryHelper helper = new QueryHelper("TollCharge", "tc");
			helper.addWhereCondition("tc.car=?", car);
			List<TollCharge> tollCharges=tollChargeService.queryAllTollCharge(helper);
			if(tollCharges==null || tollCharges.size()==0)
				continue;
			Date lastNextDate=tollCharges.get(0).getNextPayDate();
			for(int i=1;i<tollCharges.size();i++)
				if(lastNextDate.before(tollCharges.get(i).getNextPayDate()))
					lastNextDate=tollCharges.get(i).getNextPayDate();
			
			car.setNextTollChargeDate(lastNextDate);
			if(car.getNextTollChargeDate().after(new Date()))
				car.setTollChargeExpired(false);
			else
				car.setTollChargeExpired(true);
			carService.updateCar(car);
		}
	}
	
	public TollCharge getModel() {
		if(model==null)
			model=new TollCharge();
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
	
	public class RemindVO{
		private Car car;
		private String payDate;
		private String nextPayDate;
		
		public Car getCar() {
			System.out.println("in getCar()");
			return car;
		}
		public void setCar(Car car) {
			this.car = car;
		}
		public String getPayDate() {
			System.out.println("in getPayDate()");
			return payDate;
		}
		public void setPayDate(String payDate) {
			this.payDate = payDate;
		}
		public String getNextPayDate() {
			System.out.println("in getNextPayDate()");
			return nextPayDate;
		}
		public void setNextPayDate(String nextPayDate) {
			this.nextPayDate = nextPayDate;
		}
	}

	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
	}

}
