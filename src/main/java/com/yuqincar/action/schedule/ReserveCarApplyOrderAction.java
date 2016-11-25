package com.yuqincar.action.schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.support.ResourceTransactionManager;

import com.mchange.v2.async.StrandedTaskReporting;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.ReserveCarApplyOrder;
import com.yuqincar.domain.order.ReserveCarApplyOrderStatusEnum;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.monitor.MonitorGroupService;
import com.yuqincar.service.order.ReserveCarApplyOrderService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class ReserveCarApplyOrderAction extends BaseAction implements ModelDriven<ReserveCarApplyOrder>{

	private ReserveCarApplyOrder model = new ReserveCarApplyOrder();
	
	@Autowired
	private ReserveCarApplyOrderService reserveCarApplyOrderService;
	
	@Autowired
	private MonitorGroupService monitorGroupService;
	
	@Autowired
	private CarService carService;
	
	@Autowired
	private UserService userService;
	
	private String actionFlag;	//新建的标志变量
	
	private String idString;
	
	public String queryList(){
		QueryHelper helper = new QueryHelper("ReserveCarApplyOrder", "rcao");
		helper.addOrderByProperty("rcao.id", false);
		PageBean<ReserveCarApplyOrder> pageBean = reserveCarApplyOrderService.queryReserveCarApplyOrder(pageNum, helper);	
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("rcaoHelper", helper);
		return "list";
	}
	
	public String list(){
		QueryHelper helper = new QueryHelper("ReserveCarApplyOrder", "rcao");
		helper.addOrderByProperty("rcao.id", false);
		PageBean<ReserveCarApplyOrder> pageBean = reserveCarApplyOrderService.queryReserveCarApplyOrder(pageNum, helper);	
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("rcaoHelper", helper);
		return "list";
	}
	
	public String freshList(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("rcaoHelper");
		PageBean<ReserveCarApplyOrder> pageBean = reserveCarApplyOrderService.queryReserveCarApplyOrder(pageNum, helper);	
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
	
	public String addUI(){
		List<Car> carList = new ArrayList<Car>();
		List<Car> selectedList = new ArrayList<Car>();
		ActionContext.getContext().put("carList", carList);
		ActionContext.getContext().put("selectedList", selectedList);
		return "saveUI";
	}
	
	public String add(){
		//设置状态为新建
		model.setStatus(ReserveCarApplyOrderStatusEnum.getByLabel("新建"));
		//设置新建时间为当前时间
		model.setNewTime(new Date());
		reserveCarApplyOrderService.saveReserveCarApplyOrder(model);
		ActionContext.getContext().getValueStack().push(new ReserveCarApplyOrder());
		return freshList();
	}
	//新建时提交
	public String submitForNew(){
		//设置状态为提交
		model.setStatus(ReserveCarApplyOrderStatusEnum.getByLabel("已提交审核"));
		//设置新建时间和提交时间为当前时间
		model.setNewTime(new Date());
		model.setSubmittedTime(new Date());
		reserveCarApplyOrderService.saveReserveCarApplyOrder(model);
		ActionContext.getContext().getValueStack().push(new ReserveCarApplyOrder());
		return freshList();
	}
	
	public String editUI(){
		ReserveCarApplyOrder rcao = reserveCarApplyOrderService.getById(model.getId());
		ActionContext.getContext().getValueStack().push(rcao);
		return "saveUI";
	}
	
	public String edit(){
		ReserveCarApplyOrder rcao = reserveCarApplyOrderService.getById(model.getId());
		rcao.setProposer(model.getProposer());
		rcao.setCarCount(model.getCarCount());
		rcao.setFromDate(model.getFromDate());
		rcao.setToDate(model.getToDate());
		rcao.setReason(model.getReason());
		//更改新建时间
		rcao.setNewTime(new Date());
		reserveCarApplyOrderService.updateReserveCarApplyOrder(rcao);
		ActionContext.getContext().getValueStack().push(new ReserveCarApplyOrder());
		return freshList();
	}
	
	//修改时提交
	public String submitForEdit(){
		ReserveCarApplyOrder rcao = reserveCarApplyOrderService.getById(model.getId());
		//设置状态为提交
		rcao.setStatus(ReserveCarApplyOrderStatusEnum.getByLabel("已提交审核"));
		rcao.setProposer(model.getProposer());
		rcao.setCarCount(model.getCarCount());
		rcao.setFromDate(model.getFromDate());
		rcao.setToDate(model.getToDate());
		rcao.setReason(model.getReason());
		//设置提交时间为当前时间
		rcao.setSubmittedTime(new Date());
		reserveCarApplyOrderService.updateReserveCarApplyOrder(rcao);
		ActionContext.getContext().getValueStack().push(new ReserveCarApplyOrder());
		return freshList();
	}
	
	public String view(){
		ReserveCarApplyOrder rcao = reserveCarApplyOrderService.getById(model.getId());
		ActionContext.getContext().getValueStack().push(rcao);
		return "view";
	}
	
	public String delete(){
		reserveCarApplyOrderService.deleteReserveCarApplyOrder(model.getId());
		return freshList();
	}
	//审核
	public String approveUI(){
		ReserveCarApplyOrder rcao = reserveCarApplyOrderService.getById(model.getId());
		List<Car> carList = new ArrayList<Car>();
		List<Car> selectedList = new ArrayList<Car>();
		ActionContext.getContext().put("carList", carList);
		ActionContext.getContext().put("selectedList", selectedList);
		ActionContext.getContext().getValueStack().push(rcao);
		return "saveUI";
	}
	public String approve(){
		ReserveCarApplyOrder rcao = reserveCarApplyOrderService.getById(model.getId());
		rcao.setApproved(model.isApproved());
		rcao.setApproveMemo(model.getApproveMemo());
		//审核通过
		if(model.isApproved()){

			rcao.setApprovedTime(new Date());
			rcao.setStatus(ReserveCarApplyOrderStatusEnum.getByLabel("审核通过"));
		}else{//被驳回

			rcao.setRejectedTime(new Date());
			rcao.setStatus(ReserveCarApplyOrderStatusEnum.getByLabel("被驳回"));
		}
		reserveCarApplyOrderService.updateReserveCarApplyOrder(rcao);
		return freshList();
	}
	//审批车辆
	public String approveCarUI(){
		ReserveCarApplyOrder rcao = reserveCarApplyOrderService.getById(model.getId());
		//从非常备车库选取车辆
		List<Car> carList = monitorGroupService.sortCarByPlateNumber(carService.getAll());
		List<Car> selectedList = new ArrayList<Car>();
		ActionContext.getContext().put("carList", carList);
		ActionContext.getContext().put("selectedList", selectedList);
		ActionContext.getContext().getValueStack().push(rcao);
		return "saveUI1";
	}
	public String approveCar(){
		ReserveCarApplyOrder rcao = reserveCarApplyOrderService.getById(model.getId());
		rcao.setCarApproveUser(model.getCarApproveUser());
		rcao.setCarApproveUserMemo(model.getCarApproveUserMemo());
		rcao.setCarApproved(model.isCarApproved());
		if(model.isCarApproved()){
			String[] ids = idString.split(",");
			Set<Car> carSet = new HashSet<Car>();
			for(String id:ids){
				Long lId = Long.parseLong(id);
				Car car = carService.getCarById(lId);
				//把车辆置为临时常备车
				car.setTempStandingGarage(true);
				carService.saveCar(car);
				carSet.add(car);
			}
			rcao.setCars(carSet);
		}else{
			rcao.setCars(null);
		}
		//审批车辆时，判断司机审批是否通过
		if(rcao.isDriverApproved() && model.isCarApproved()){
			//申请状态设为已配置
			rcao.setStatus(ReserveCarApplyOrderStatusEnum.getByLabel("已配置"));
		}
		reserveCarApplyOrderService.updateReserveCarApplyOrder(rcao);
		
		return freshList();
	}
	//审批司机
	public String approveDriverUI(){
		ReserveCarApplyOrder rcao = reserveCarApplyOrderService.getById(model.getId());
		List<User> driverList = reserveCarApplyOrderService.sortUserByName(userService.getAllDrivers());
		List<User> selectedList = new ArrayList<User>();
		ActionContext.getContext().put("userList", driverList);
		ActionContext.getContext().put("selectedList", selectedList);
		ActionContext.getContext().getValueStack().push(rcao);
		return "saveUI2";
	}
	public String approveDriver(){
		ReserveCarApplyOrder rcao = reserveCarApplyOrderService.getById(model.getId());
		rcao.setDriverApproveUser(model.getDriverApproveUser());
		rcao.setDriverApproveUserMemo(model.getDriverApproveUserMemo());
		rcao.setDriverApproved(model.isDriverApproved());
		if(model.isDriverApproved()){
			String[] ids = idString.split(",");
			Set<User> driverSet = new HashSet<User>();
			for(String id:ids){
				Long lId = Long.parseLong(id);
				driverSet.add(userService.getById(lId));
			}
			rcao.setDrivers(driverSet);
		}else{
			rcao.setDrivers(null);
		}
		//审批司机时，判断车辆审批是否通过
		if(model.isDriverApproved() && rcao.isCarApproved()){
			//申请状态设为已配置
			rcao.setStatus(ReserveCarApplyOrderStatusEnum.getByLabel("已配置"));
		}
		reserveCarApplyOrderService.updateReserveCarApplyOrder(rcao);
		return freshList();
	}
	
	public String getActionFlag() {
		return actionFlag;
	}

	public void setActionFlag(String actionFlag) {
		this.actionFlag = actionFlag;
	}

	public String getIdString() {
		return idString;
	}

	public void setIdString(String idString) {
		this.idString = idString;
	}

	public ReserveCarApplyOrder getModel() {
		return model;
	}

}
