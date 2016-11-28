package com.yuqincar.action.businessParameter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.mysql.fabric.xmlrpc.base.Array;
import com.opensymphony.xwork2.ActionContext;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.businessParameter.BusinessParameter;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarStatusEnum;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.businessParameter.BusinessParameterService;
import com.yuqincar.service.order.ReserveCarApplyOrderService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class BusinessParameterAction extends BaseAction{
	
	@Autowired
	private BusinessParameterService businessParameterService;
	
	@Autowired
	private ReserveCarApplyOrderService reserveCarApplyOrderService;
	
	@Autowired
	private UserService userService;

	private String actionFlag; //通过该变量标记不同的action
	
	private String viewFlag;	//通过该变量标记不同的文字信息
	
	private String idString;
	
	private Long for4SUserId;
	
	private Long forCarCareUserId;
	
	private Long approveUserId;
	
	private Long applyUserId;
	
	private Long carApproveUserId;
	
	private Long driverApproveUserId;
	
	public String addUI(){
		
		List<User> showList = new ArrayList<User>();
		List<User> selectedList = new ArrayList<User>();
		
		if(actionFlag.equals("for4S") || actionFlag.equals("forCarCare")){

			showList = reserveCarApplyOrderService.sortUserByName(userService.getAll());
		}
		if(actionFlag.equals("approveUser")){
			showList = userService.getUserByRoleName("公司领导");
			showList.removeAll(businessParameterService.getBusinessParameter().getReserveCarApplyOrderApproveUser());
		}
		if(actionFlag.equals("applyUser")){
			showList = userService.getUserByRoleName("运营科领导");		
			showList.removeAll(businessParameterService.getBusinessParameter().getReserveCarApplyOrderApplyUser());
		}
		if(actionFlag.equals("carApproveUser")){
			showList = userService.getUserByRoleName("技术保障科领导");
			showList.removeAll(businessParameterService.getBusinessParameter().getReserveCarApplyOrderCarApproveUser());
		}
		if(actionFlag.equals("driverApproveUser")){
			showList = userService.getUserByRoleName("人力资源科领导");
			showList.removeAll(businessParameterService.getBusinessParameter().getReserveCarApplyOrderDriverApproveUser());
		}
		ActionContext.getContext().put("showList", showList);
		ActionContext.getContext().put("selectedList", selectedList);
		return "saveUI";
	}
	
	/**
	 * 4S员工相关
	 */
	//4S员工列表
	public String employeesFor4S(){
		viewFlag = "4S员工";
		BusinessParameter businessParameter = businessParameterService.getBusinessParameter();
		List<User> for4SUsers = businessParameter.getEmployeesIn4SForSMS();
		ActionContext.getContext().put("for4SUserList", for4SUsers);
		return "list";
	}
	//添加4S员工
	public String addEmployeesFor4S(){
		viewFlag = "4S员工";
		BusinessParameter businessParameter = businessParameterService.getBusinessParameter();
		if(idString != null && !idString.equals("")){
			String[] ids = idString.split(",");
			List<User> userList = businessParameter.getEmployeesIn4SForSMS();
			for(String id : ids){
				Long lid = Long.parseLong(id);
				userList.add(userService.getById(lid));
			}
			businessParameter.setEmployeesIn4SForSMS(userList);
		}
		businessParameterService.updateBusinessParameter(businessParameter);
		
		return employeesFor4S();
	}
	/**
	 * 保养管理员相关
	 */
	//保养管理员列表
	public String employeesForCarCare(){
		viewFlag = "保养管理员";
		BusinessParameter businessParameter = businessParameterService.getBusinessParameter();
		List<User> forCarCareUser = businessParameter.getEmployeesForCarCareAppointmentSMS();
		ActionContext.getContext().put("forCarCareUserList", forCarCareUser);
		return "list";
	}
	//添加保养管理员
	public String addEmployeesForCarCare(){
		viewFlag = "保养管理员";
		BusinessParameter businessParameter = businessParameterService.getBusinessParameter();
		if(idString != null && !idString.equals("")){
			String[] ids = idString.split(",");
			List<User> userList = businessParameter.getEmployeesForCarCareAppointmentSMS();
			for(String id : ids){
				Long lid = Long.parseLong(id);
				userList.add(userService.getById(lid));
			}
			businessParameter.setEmployeesForCarCareAppointmentSMS(userList);
		}
		businessParameterService.updateBusinessParameter(businessParameter);
		return employeesForCarCare();
	}
	
	/**
	 * 审核人相关
	 */
	//审核人列表
	public String reserveCarApplyOrderApproveUser(){
		viewFlag = "审核人";
		BusinessParameter businessParameter = businessParameterService.getBusinessParameter();
		List<User> approveUserList = businessParameter.getReserveCarApplyOrderApproveUser();
		ActionContext.getContext().put("approveUserList", approveUserList);
		return "list";
	}
	//添加审核人
	public String addReserveCarApplyOrderApproveUser(){
		viewFlag = "审核人";
		BusinessParameter businessParameter = businessParameterService.getBusinessParameter();
		if(idString != null && !idString.equals("")){
			String[] ids = idString.split(",");
			List<User> userList = businessParameter.getReserveCarApplyOrderApproveUser();
			for(String id : ids){
				Long lid = Long.parseLong(id);
				userList.add(userService.getById(lid));
			}
			businessParameter.setReserveCarApplyOrderApproveUser(userList);
		}
		businessParameterService.updateBusinessParameter(businessParameter);
		return reserveCarApplyOrderApproveUser();
	}
	/**
	 * 申请人相关
	 */
	//申请人列表
	public String reserveCarApplyOrderApplyUser(){
		viewFlag = "常备车库申请人";
		BusinessParameter businessParameter = businessParameterService.getBusinessParameter();
		List<User> applyUserList = businessParameter.getReserveCarApplyOrderApplyUser();
		ActionContext.getContext().put("applyUserList", applyUserList);
		return "list";
	}
	//添加申请人
	public String addReserveCarApplyOrderApplyUser(){
		viewFlag = "常备车库申请人";
		BusinessParameter businessParameter = businessParameterService.getBusinessParameter();
		if(idString != null && !idString.equals("")){
			String[] ids = idString.split(",");
			List<User> userList = businessParameter.getReserveCarApplyOrderApplyUser();
			for(String id : ids){
				Long lid = Long.parseLong(id);
				userList.add(userService.getById(lid));
			}
			businessParameter.setReserveCarApplyOrderApplyUser(userList);
		}
		businessParameterService.updateBusinessParameter(businessParameter);
		return reserveCarApplyOrderApplyUser();
	}
	/**
	 * 车辆审批相关
	 */
	//车辆审批人列表
	public String reserveCarApplyOrderCarApproveUser(){
		viewFlag = "车辆配置人";
		BusinessParameter businessParameter = businessParameterService.getBusinessParameter();
		List<User> carApproveUserList = businessParameter.getReserveCarApplyOrderCarApproveUser();
		ActionContext.getContext().put("carApproveUserList", carApproveUserList);
		return "list";
	}
	//添加车辆审批人
	public String addReserveCarApplyOrderCarApproveUser(){
		viewFlag = "车辆配置人";
		BusinessParameter businessParameter = businessParameterService.getBusinessParameter();
		if(idString != null && !idString.equals("")){
			String[] ids = idString.split(",");
			List<User> userList = businessParameter.getReserveCarApplyOrderCarApproveUser();
			for(String id : ids){
				Long lid = Long.parseLong(id);
				userList.add(userService.getById(lid));
			}
			businessParameter.setReserveCarApplyOrderCarApproveUser(userList);
		}
		businessParameterService.updateBusinessParameter(businessParameter);
		return reserveCarApplyOrderCarApproveUser();
	}
	/**
	 * 司机审批相关
	 */
	//司机审批人列表
	public String reserveCarApplyOrderDriverApproveUser(){
		viewFlag = "司机配置人";
		BusinessParameter businessParameter = businessParameterService.getBusinessParameter();
		List<User> driverApproveUserList = businessParameter.getReserveCarApplyOrderDriverApproveUser();
		ActionContext.getContext().put("driverApproveUserList", driverApproveUserList);
		return "list";
	}
	//添加司机审批人
	public String addReserveCarApplyOrderDriverApproveUser(){
		viewFlag = "司机配置人";
		BusinessParameter businessParameter = businessParameterService.getBusinessParameter();
		if(idString != null && !idString.equals("")){
			String[] ids = idString.split(",");
			List<User> userList = businessParameter.getReserveCarApplyOrderDriverApproveUser();
			for(String id : ids){
				Long lid = Long.parseLong(id);
				userList.add(userService.getById(lid));
			}
			businessParameter.setReserveCarApplyOrderDriverApproveUser(userList);
		}
		businessParameterService.updateBusinessParameter(businessParameter);
		return reserveCarApplyOrderDriverApproveUser();
	}
	
	
	//删除
	public String delete(){

		System.out.println("actionFlag="+actionFlag);
		
		BusinessParameter businessParameter = businessParameterService.getBusinessParameter();
		if(actionFlag.equals("for4S")){
			
			List<User> users = businessParameter.getEmployeesIn4SForSMS();
			users.remove(userService.getById(for4SUserId));
			businessParameter.setEmployeesIn4SForSMS(users);
			businessParameterService.updateBusinessParameter(businessParameter);
			return employeesFor4S();
			
		}else if(actionFlag.equals("forCarCare")){
			
			List<User> users = businessParameter.getEmployeesForCarCareAppointmentSMS();
			users.remove(userService.getById(forCarCareUserId));
			businessParameter.setEmployeesForCarCareAppointmentSMS(users);
			businessParameterService.updateBusinessParameter(businessParameter);
			return employeesForCarCare();
					
		}else if(actionFlag.equals("approveUser")){
			
			List<User> users = businessParameter.getReserveCarApplyOrderApproveUser();
			users.remove(userService.getById(approveUserId));
			businessParameter.setReserveCarApplyOrderApproveUser(users);
			businessParameterService.updateBusinessParameter(businessParameter);
			return reserveCarApplyOrderApproveUser();
			
		}else if(actionFlag.equals("applyUser")){
			
			List<User> users = businessParameter.getReserveCarApplyOrderApplyUser();
			users.remove(userService.getById(applyUserId));
			businessParameter.setReserveCarApplyOrderApplyUser(users);
			businessParameterService.updateBusinessParameter(businessParameter);
			return reserveCarApplyOrderApplyUser();
			
		}else if(actionFlag.equals("carApproveUser")){
			
			List<User> users = businessParameter.getReserveCarApplyOrderCarApproveUser();
			users.remove(userService.getById(carApproveUserId));
			businessParameter.setReserveCarApplyOrderCarApproveUser(users);
			businessParameterService.updateBusinessParameter(businessParameter);
			return reserveCarApplyOrderCarApproveUser();
			
		}else{
			
			List<User> users = businessParameter.getReserveCarApplyOrderDriverApproveUser();
			users.remove(userService.getById(driverApproveUserId));
			businessParameter.setReserveCarApplyOrderDriverApproveUser(users);
			businessParameterService.updateBusinessParameter(businessParameter);
			return reserveCarApplyOrderDriverApproveUser();
			
		}
	}
	
	public String getActionFlag() {
		return actionFlag;
	}
	public void setActionFlag(String actionFlag) {
		this.actionFlag = actionFlag;
	}
	public String getViewFlag() {
		return viewFlag;
	}
	public void setViewFlag(String viewFlag) {
		this.viewFlag = viewFlag;
	}
	public String getIdString() {
		return idString;
	}
	public void setIdString(String idString) {
		this.idString = idString;
	}

	public Long getFor4SUserId() {
		return for4SUserId;
	}

	public void setFor4SUserId(Long for4sUserId) {
		for4SUserId = for4sUserId;
	}

	public Long getForCarCareUserId() {
		return forCarCareUserId;
	}

	public void setForCarCareUserId(Long forCarCareUserId) {
		this.forCarCareUserId = forCarCareUserId;
	}

	public Long getApproveUserId() {
		return approveUserId;
	}

	public void setApproveUserId(Long approveUserId) {
		this.approveUserId = approveUserId;
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
	
}
