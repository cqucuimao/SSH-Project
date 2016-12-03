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
import com.yuqincar.service.businessParameter.BusinessParameterService;
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
	
	@Autowired
	private BusinessParameterService businessParameterService;
	
	private String actionFlag;	//新建的标志变量
	
	private String idString;
	
	private Long proposerId;
	
	private Long approveUserId;
	
	private Long applyUserId;
	
	private Long carApproveUserId;
	
	private Long driverApproveUserId;
	
	public String list(){
		User user=(User)ActionContext.getContext().getSession().get("user");
		QueryHelper helper = new QueryHelper("ReserveCarApplyOrder", "rcao");
		//提交之后，才给实体赋值approveUser/carApproveUser/driverApproveUser，所以有下面的逻辑。
		//这有点复杂，只是权宜之计，根本解决之道是将List页面分开。
		helper.addWhereCondition("rcao.proposer=? or rcao.approveUser=? or (rcao.status>=? and (rcao.carApproveUser=? or rcao.driverApproveUser=?))", 
				user,user,ReserveCarApplyOrderStatusEnum.APPROVED,user,user);
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
		return "saveUI";
	}
	
	public String add(){
		model.setProposer((User)ActionContext.getContext().getSession().get("user"));
		reserveCarApplyOrderService.save(model);
		return freshList();
	}
	//新建时提交
	public String submitForNew(){
		model.setProposer((User)ActionContext.getContext().getSession().get("user"));
		reserveCarApplyOrderService.saveAndSubmit(model);
		return freshList();
	}
	
	public String editUI(){
		ReserveCarApplyOrder rcao = reserveCarApplyOrderService.getById(model.getId());
		ActionContext.getContext().getValueStack().push(rcao);
		return "saveUI";
	}
	
	public String edit(){
		ReserveCarApplyOrder rcao = reserveCarApplyOrderService.getById(model.getId());
		rcao.setCarCount(model.getCarCount());
		rcao.setFromDate(model.getFromDate());
		rcao.setToDate(model.getToDate());
		rcao.setReason(model.getReason());
		reserveCarApplyOrderService.update(rcao);
		return freshList();
	}
	
	//修改时提交
	public String submitForEdit(){
		ReserveCarApplyOrder rcao = reserveCarApplyOrderService.getById(model.getId());
		//设置状态为提交
		rcao.setCarCount(model.getCarCount());
		rcao.setFromDate(model.getFromDate());
		rcao.setToDate(model.getToDate());
		rcao.setReason(model.getReason());
		reserveCarApplyOrderService.submit(rcao);
		//ActionContext.getContext().getValueStack().push(new ReserveCarApplyOrder());
		return freshList();
	}
	
	public String view(){
		ReserveCarApplyOrder rcao = reserveCarApplyOrderService.getById(model.getId());
		ActionContext.getContext().getValueStack().push(rcao);
		return "view";
	}
	
	public boolean isCanDelete(){
		ReserveCarApplyOrder rcao=(ReserveCarApplyOrder)ActionContext.getContext().getValueStack().peek();
		return reserveCarApplyOrderService.canDelete(rcao,(User) ActionContext.getContext().getSession().get("user"));
	}
	
	public boolean isCanUpdate(){
		ReserveCarApplyOrder rcao=(ReserveCarApplyOrder)ActionContext.getContext().getValueStack().peek();
		return reserveCarApplyOrderService.canUpdate(rcao,(User) ActionContext.getContext().getSession().get("user"));
	}
	
	public boolean isCanApprove(){
		ReserveCarApplyOrder rcao=(ReserveCarApplyOrder)ActionContext.getContext().getValueStack().peek();
		return reserveCarApplyOrderService.canApprove(rcao,(User) ActionContext.getContext().getSession().get("user"));
	}
	
	public boolean isCanConfigCar(){
		ReserveCarApplyOrder rcao=(ReserveCarApplyOrder)ActionContext.getContext().getValueStack().peek();
		return reserveCarApplyOrderService.canConfigCar(rcao,(User) ActionContext.getContext().getSession().get("user"));
	}
	
	public boolean isCanConfigDriver(){
		ReserveCarApplyOrder rcao=(ReserveCarApplyOrder)ActionContext.getContext().getValueStack().peek();
		return reserveCarApplyOrderService.canConfigDriver(rcao,(User) ActionContext.getContext().getSession().get("user"));
	}
	
	public String delete(){
		reserveCarApplyOrderService.delete(model.getId());
		return freshList();
	}
	//审核
	public String approveUI(){
		ReserveCarApplyOrder rcao = reserveCarApplyOrderService.getById(model.getId());
		ActionContext.getContext().getValueStack().push(rcao);
		return "saveUI";
	}
	public String approve(){
		ReserveCarApplyOrder rcao = reserveCarApplyOrderService.getById(model.getId());
		rcao.setApproveMemo(model.getApproveMemo());
		
		if(model.isApproved())
			reserveCarApplyOrderService.approve(rcao);
		else
			reserveCarApplyOrderService.reject(rcao);
		return freshList();
	}
	//审批车辆
	public String approveCarUI(){
		ReserveCarApplyOrder rcao = reserveCarApplyOrderService.getById(model.getId());
		//从非常备车库选取车辆
		List<Car> carList = monitorGroupService.sortCarByPlateNumber(carService.getAllCarFromNotStandingAndNotTempStandingGarage());
		List<Car> selectedList = new ArrayList<Car>();
		ActionContext.getContext().put("carList", carList);
		ActionContext.getContext().put("selectedList", selectedList);
		ActionContext.getContext().getValueStack().push(rcao);
		return "saveUI1";
	}
	public String approveCar(){
		ReserveCarApplyOrder rcao = reserveCarApplyOrderService.getById(model.getId());
		rcao.setCarApproveUserMemo(model.getCarApproveUserMemo());
		
		String[] ids = idString.split(",");
		Set<Car> carSet = new HashSet<Car>();
		for(String id:ids){
			Long lId = Long.parseLong(id);
			Car car = carService.getCarById(lId);
			carSet.add(car);
		}
		rcao.setCars(carSet);
		
		reserveCarApplyOrderService.configCar(rcao);
		
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
		rcao.setDriverApproveUserMemo(model.getDriverApproveUserMemo());
		
		String[] ids = idString.split(",");
		Set<User> driverSet = new HashSet<User>();
		for(String id:ids){
			Long lId = Long.parseLong(id);
			driverSet.add(userService.getById(lId));
		}
		rcao.setDrivers(driverSet);
		
		reserveCarApplyOrderService.configDriver(rcao);
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

	public Long getProposerId() {
		return proposerId;
	}

	public void setProposerId(Long proposerId) {
		this.proposerId = proposerId;
	}

	public Long getApplyUserId() {
		return applyUserId;
	}

	public void setApplyUserId(Long applyUserId) {
		this.applyUserId = applyUserId;
	}

	public Long getCarApproveUserId() {
		return carApproveUserId;
	}

	public void setCarApproveUserId(Long carApproveUserId) {
		this.carApproveUserId = carApproveUserId;
	}

	public Long getDriverApproveUserId() {
		return driverApproveUserId;
	}

	public void setDriverApproveUserId(Long driverApproveUserId) {
		this.driverApproveUserId = driverApproveUserId;
	}

	public ReserveCarApplyOrder getModel() {
		return model;
	}

	public Long getApproveUserId() {
		return approveUserId;
	}

	public void setApproveUserId(Long approveUserId) {
		this.approveUserId = approveUserId;
	}

}
