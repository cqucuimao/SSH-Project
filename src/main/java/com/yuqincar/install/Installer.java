package com.yuqincar.install;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.car.CarServiceTypeDao;
import com.yuqincar.dao.order.CarServiceSuperTypeDao;
import com.yuqincar.dao.order.PriceDao;
import com.yuqincar.dao.order.PriceTableDao;
import com.yuqincar.dao.privilege.PrivilegeDao;
import com.yuqincar.dao.privilege.RoleDao;
import com.yuqincar.dao.privilege.UserDao;
import com.yuqincar.domain.car.CarServiceSuperType;
import com.yuqincar.domain.car.CarServiceType;
import com.yuqincar.domain.order.Price;
import com.yuqincar.domain.order.PriceTable;
import com.yuqincar.domain.order.WatchKeeper;
import com.yuqincar.domain.privilege.Privilege;
import com.yuqincar.domain.privilege.Role;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.domain.privilege.UserStatusEnum;
import com.yuqincar.domain.privilege.UserTypeEnum;
import com.yuqincar.service.order.WatchKeeperService;
import com.yuqincar.utils.ExcelUtil;

/**
 * 安装程序（初始化数据）
 */
@Component
public class Installer {

	@Resource
	public SessionFactory sessionFactory;
	
	@Resource
	public UserDao userDao;
	
	@Resource
	public RoleDao roleDao;
	
	@Resource 
	public PrivilegeDao privilegeDao;
	
	@Resource
	public CarServiceSuperTypeDao carServiceSuperTypeDao;
	
	@Resource
	public CarServiceTypeDao carServiceTypeDao;
	
	@Resource
	public PriceTableDao priceTableDao;
	
	@Resource
	public PriceDao priceDao;
	
	@Resource
	public WatchKeeperService watchKeeperService;
	

	@Transactional
	public void install() {
		Session session = sessionFactory.getCurrentSession();
		// =======================================================================
		// 1，超级管理员
		User user = new User();
		user.setLoginName("admin");
		user.setName("超级管理员");
		user.setPassword(DigestUtils.md5Hex("admin")); // 密码要使用MD5摘要
		user.setStatus(UserStatusEnum.NORMAL);
		user.setUserType(UserTypeEnum.OFFICE);
		userDao.save(user); // 保存

		// =======================================================================
		// 2，权限数据
		Privilege  menu1, menu2, menu3, menu4, menu5;

		//权限管理
		menu1 = new Privilege("权限管理", "/privilege", null);
		privilegeDao.save(menu1);
		Privilege menu1_1 = new Privilege("角色管理", "/role_list", menu1);
		privilegeDao.save(menu1_1);
		privilegeDao.save(new Privilege("角色删除", "/role_delete", menu1_1));
		privilegeDao.save(new Privilege("角色添加", "/role_add", menu1_1));
		privilegeDao.save(new Privilege("角色修改", "/role_edit", menu1_1));
		privilegeDao.save(new Privilege("角色设置权限", "/role_setPrivilege", menu1_1));

		Privilege menu1_2 = new Privilege("部门管理", "/department_list", menu1);
		privilegeDao.save(menu1_2);
		privilegeDao.save(new Privilege("部门删除", "/department_delete", menu1_2));
		privilegeDao.save(new Privilege("部门添加", "/department_add", menu1_2));
		privilegeDao.save(new Privilege("部门修改", "/department_edit", menu1_2));

		Privilege menu1_3 = new Privilege("用户管理", "/user_list", menu1);
		privilegeDao.save(menu1_3);
		privilegeDao.save(new Privilege("用户删除", "/user_delete", menu1_3));
		privilegeDao.save(new Privilege("用户添加", "/user_add", menu1_3));
		privilegeDao.save(new Privilege("用户修改", "/user_edit", menu1_3));
		privilegeDao.save(new Privilege("用户初始化密码", "/user_initPassword", menu1_3));
		
		//车辆调度
		Privilege scheduleMenu = new Privilege("车辆调度", "/schedule", null);
		privilegeDao.save(scheduleMenu);
		Privilege schedulingMenu=new Privilege("调度","/schedule_scheduling",scheduleMenu);
		privilegeDao.save(schedulingMenu);
		privilegeDao.save(new Privilege("","/schedule_getCar",schedulingMenu));
		privilegeDao.save(new Privilege("","/schedule_getCustomerOrganization",schedulingMenu));
		privilegeDao.save(new Privilege("","/schedule_getCustomer",schedulingMenu));
		privilegeDao.save(new Privilege("","/schedule_getPhoneNumber",schedulingMenu));
		privilegeDao.save(new Privilege("","/schedule_getOtherPassengers",schedulingMenu));
		privilegeDao.save(new Privilege("","/schedule_getOtherPhoneNumbers",schedulingMenu));
		privilegeDao.save(new Privilege("","/schedule_getChargeModeString",schedulingMenu));
		privilegeDao.save(new Privilege("","/schedule_inQueue",schedulingMenu));
		privilegeDao.save(new Privilege("","/schedule_isCarAndDriverAvailable",schedulingMenu));
		privilegeDao.save(new Privilege("","/schedule_isOrderDeprived",schedulingMenu));
		privilegeDao.save(new Privilege("","/schedule_showDeprived",schedulingMenu));
		privilegeDao.save(new Privilege("","/schedule_startSchedule",schedulingMenu));
		privilegeDao.save(new Privilege("","/schedule_getRecommandDriver",schedulingMenu));
		
		Privilege queueMenu=new Privilege("待调度队列","/schedule_queue",scheduleMenu);
		privilegeDao.save(queueMenu);
		privilegeDao.save(new Privilege("","/schedule_cancelOrder",queueMenu));
		privilegeDao.save(new Privilege("","/schedule_scheduleFromQueue",queueMenu));
		privilegeDao.save(new Privilege("","/schedule_dispatchOrder",queueMenu));
		privilegeDao.save(new Privilege("","/schedule_watchKeeper",queueMenu));
		privilegeDao.save(new Privilege("","/schedule_configWatchKeeper",queueMenu));
		
		Privilege orderMenu=new Privilege("订单管理","/order_orderManager",scheduleMenu);
		privilegeDao.save(orderMenu);
		privilegeDao.save(new Privilege("","/schedule_updateOrder",orderMenu));
		privilegeDao.save(new Privilege("","/order_print",orderMenu));
		privilegeDao.save(new Privilege("","/order_operate",orderMenu));
		privilegeDao.save(new Privilege("","/order_getAcceptAction",orderMenu));
		privilegeDao.save(new Privilege("","/order_getBeginAction",orderMenu));
		privilegeDao.save(new Privilege("","/order_getGetonAction",orderMenu));
		privilegeDao.save(new Privilege("","/order_getGetoffAction",orderMenu));
		privilegeDao.save(new Privilege("","/order_getEndAction",orderMenu));
		privilegeDao.save(new Privilege("","/order_deleteDriverAction",orderMenu));
		privilegeDao.save(new Privilege("","/order_popupModify",orderMenu));
		privilegeDao.save(new Privilege("","/order_view",orderMenu));
		privilegeDao.save(new Privilege("","/order_cancel",orderMenu));
		privilegeDao.save(new Privilege("","/order_cancelDo",orderMenu));
		privilegeDao.save(new Privilege("","/order_reschedule",orderMenu));
		privilegeDao.save(new Privilege("","/order_rescheduleDo",orderMenu));
		privilegeDao.save(new Privilege("","/order_postpone",orderMenu));
		privilegeDao.save(new Privilege("","/order_postponeDo",orderMenu));
		privilegeDao.save(new Privilege("","/order_info",orderMenu));
		privilegeDao.save(new Privilege("","/order_protocolOrderRemind",orderMenu));
		privilegeDao.save(new Privilege("","/order_unAcceptedOrderRemind",orderMenu));
		privilegeDao.save(new Privilege("","/order_orderManagerQueryForm",orderMenu));
		privilegeDao.save(new Privilege("","/order_list",orderMenu));
		privilegeDao.save(new Privilege("","/order_getSignature",orderMenu));
 
		Privilege driverTaskListMenu=new Privilege("司机任务动态表","/driver_taskList",scheduleMenu);
		privilegeDao.save(driverTaskListMenu);
		
		//车辆监控
		Privilege monitorMenu = new Privilege("车辆监控", "/monitor", null);
		privilegeDao.save(monitorMenu);
		
		Privilege realtimeMenu=new Privilege("实时监控","/realtime_home",monitorMenu);
		privilegeDao.save(realtimeMenu);
		privilegeDao.save(new Privilege("","/realtime_mapWindow",realtimeMenu));
		privilegeDao.save(new Privilege("","/alarm_dealWarnings",realtimeMenu));
		privilegeDao.save(new Privilege("","/realtime_list",realtimeMenu));
		privilegeDao.save(new Privilege("","/alarm_getUndealedMessages",realtimeMenu));
		privilegeDao.save(new Privilege("","/realtime_getCarInfo",realtimeMenu));
		privilegeDao.save(new Privilege("","/api",realtimeMenu));
		privilegeDao.save(new Privilege("","/baidu",realtimeMenu));
		privilegeDao.save(new Privilege("","/realtime_orderDetail",realtimeMenu));
		privilegeDao.save(new Privilege("","/realtime_allNormalCars",realtimeMenu));
		privilegeDao.save(new Privilege("","/order_getDriverJson",realtimeMenu));
		privilegeDao.save(new Privilege("","/order_getPlateNumberJson",realtimeMenu));
		
		
		Privilege replayMenu=new Privilege("轨迹回放","/replay_home",monitorMenu);
		privilegeDao.save(replayMenu);
		privilegeDao.save(new Privilege("","/replay_list",replayMenu));
		
		Privilege alarmMenu=new Privilege("历史警报","/alarm_home",monitorMenu);
		privilegeDao.save(alarmMenu);
		
		//收款管理
		Privilege receiptMenu = new Privilege("收款管理", "/receipt", null);
		privilegeDao.save(receiptMenu);
		
		Privilege unpaidOrderMenu=new Privilege("未收款订单","/unpaidOrder_home",receiptMenu);
		privilegeDao.save(unpaidOrderMenu);
		privilegeDao.save(new Privilege("","/unpaidOrder_list",unpaidOrderMenu));
		privilegeDao.save(new Privilege("","/orderStatement_isOrderStatementExist",unpaidOrderMenu));
		privilegeDao.save(new Privilege("","/orderStatement_newOrderStatement",unpaidOrderMenu));
		privilegeDao.save(new Privilege("","/orderStatement_orderStatementList",unpaidOrderMenu));
		privilegeDao.save(new Privilege("","/orderStatement_addOrderStatement",unpaidOrderMenu));
		
		Privilege orderStatementMenu=new Privilege("对账单","/orderStatement_list",receiptMenu);
		privilegeDao.save(orderStatementMenu);
		privilegeDao.save(new Privilege("","/orderStatement_paidList",orderStatementMenu));
		privilegeDao.save(new Privilege("","/orderStatement_info",orderStatementMenu));
		privilegeDao.save(new Privilege("","/orderStatement_generatePDF",orderStatementMenu));
		privilegeDao.save(new Privilege("","/orderStatement_cancelOrders",orderStatementMenu));
		privilegeDao.save(new Privilege("","/orderStatement_cancelOrderStatement",orderStatementMenu));
		privilegeDao.save(new Privilege("","/orderStatement_confirmReceipt",orderStatementMenu));
		
		//车辆管理
		Privilege carMenu = new Privilege("车辆管理", "/car", null);
		privilegeDao.save(carMenu);
		Privilege informationMenu=new Privilege("基本信息","/car_list",carMenu);
		privilegeDao.save(informationMenu);
		privilegeDao.save(new Privilege("","/car_queryList",informationMenu));
		privilegeDao.save(new Privilege("","/car_delete",informationMenu));
		privilegeDao.save(new Privilege("","/car_addUI",informationMenu));
		privilegeDao.save(new Privilege("","/car_add",informationMenu));
		privilegeDao.save(new Privilege("","/car_editUI",informationMenu));
		privilegeDao.save(new Privilege("","/car_detail",informationMenu));
		privilegeDao.save(new Privilege("","/car_edit",informationMenu));
		privilegeDao.save(new Privilege("","/car_editDevice",informationMenu));
		privilegeDao.save(new Privilege("","/car_popup",informationMenu));
		
		Privilege serviceTypeMenu=new Privilege("车型管理","/carServiceType_list",carMenu);
		privilegeDao.save(serviceTypeMenu);
		privilegeDao.save(new Privilege("","/carServiceType_delete",serviceTypeMenu));
		privilegeDao.save(new Privilege("","/carServiceType_addUI",serviceTypeMenu));
		privilegeDao.save(new Privilege("","/carServiceType_add",serviceTypeMenu));
		privilegeDao.save(new Privilege("","/carServiceType_editUI",serviceTypeMenu));
		privilegeDao.save(new Privilege("","/carServiceType_edit",serviceTypeMenu));
		privilegeDao.save(new Privilege("","/carServiceType_getImage",serviceTypeMenu));
		
		Privilege violationMenu=new Privilege("违章信息","/carViolation_list",carMenu);
		privilegeDao.save(violationMenu);
		privilegeDao.save(new Privilege("","/carViolation_addUI",violationMenu));
		
		
		Privilege servicePointMenu=new Privilege("驻车点管理","/servicePoint_list",carMenu);
		privilegeDao.save(servicePointMenu);
		privilegeDao.save(new Privilege("","/servicePoint_delete",servicePointMenu));
		privilegeDao.save(new Privilege("","/servicePoint_addUI",servicePointMenu));
		privilegeDao.save(new Privilege("","/servicePoint_add",servicePointMenu));
		privilegeDao.save(new Privilege("","/servicePoint_edit",servicePointMenu));
		privilegeDao.save(new Privilege("","/servicePoint_editUI",servicePointMenu));
		privilegeDao.save(new Privilege("","/servicePoint_validateAdd",servicePointMenu));
		
		Privilege carCareMenu = new Privilege("车辆保养", "/carCare_list", carMenu);
		privilegeDao.save(carCareMenu);
		privilegeDao.save(new Privilege("", "/carCare_delete", carCareMenu));
		privilegeDao.save(new Privilege("", "/carCare_addUI", carCareMenu));
		privilegeDao.save(new Privilege("", "/carCare_add", carCareMenu));
		privilegeDao.save(new Privilege("", "/carCare_editUI", carCareMenu));
		privilegeDao.save(new Privilege("", "/carCare_edit", carCareMenu));
		privilegeDao.save(new Privilege("", "/carCare_appoint", carCareMenu));
		privilegeDao.save(new Privilege("", "/carCare_remind", carCareMenu));
		privilegeDao.save(new Privilege("", "/carCare_detail", carCareMenu));
		privilegeDao.save(new Privilege("", "/carCare_care", carCareMenu));
		privilegeDao.save(new Privilege("", "/carCare_saveAppointment", carCareMenu));
		
		Privilege carInsuranceMenu = new Privilege("车辆保险", "/carInsurance_list", carMenu);
		privilegeDao.save(carInsuranceMenu);
		privilegeDao.save(new Privilege("", "/carInsurance_addUI", carInsuranceMenu));
		privilegeDao.save(new Privilege("", "/carInsurance_add", carInsuranceMenu));
		privilegeDao.save(new Privilege("", "/carInsurance_editUI", carInsuranceMenu));
		privilegeDao.save(new Privilege("", "/carInsurance_remind", carInsuranceMenu));
		privilegeDao.save(new Privilege("", "/carInsurance_detail", carInsuranceMenu));
		privilegeDao.save(new Privilege("", "/carInsurance_insurance", carInsuranceMenu));
		privilegeDao.save(new Privilege("", "/carInsurance_validateAdd", carInsuranceMenu));
		
		Privilege carRepairMenu = new Privilege("车辆维修", "/carRepair_list", carMenu);
		privilegeDao.save(carRepairMenu);
		privilegeDao.save(new Privilege("", "/carRepair_delete", carRepairMenu));
		privilegeDao.save(new Privilege("", "/carRepair_addUI", carRepairMenu));
		privilegeDao.save(new Privilege("", "/carRepair_add", carRepairMenu));
		privilegeDao.save(new Privilege("", "/carRepair_saveAppointment", carRepairMenu));
		privilegeDao.save(new Privilege("", "/carRepair_editUI", carRepairMenu));
		privilegeDao.save(new Privilege("", "/carRepair_edit", carRepairMenu));
		privilegeDao.save(new Privilege("", "/carRepair_appoint", carRepairMenu));
		privilegeDao.save(new Privilege("", "/carRepair_detail", carRepairMenu));
		privilegeDao.save(new Privilege("", "/carRepair_repair", carRepairMenu));
		
		Privilege carRefuelMenu = new Privilege("加油信息", "/carRefuel_list", carMenu);
		privilegeDao.save(carRefuelMenu);
		privilegeDao.save(new Privilege("", "/carRefuel_addUI", carRefuelMenu));
		privilegeDao.save(new Privilege("", "/carRefuel_add", carRefuelMenu));
		privilegeDao.save(new Privilege("", "/carRefuel_editUI", carRefuelMenu));
		privilegeDao.save(new Privilege("", "/carRefuel_detail", carRefuelMenu));
		privilegeDao.save(new Privilege("", "/carRefuel_refuel", carRefuelMenu));
		
		Privilege carExamineMenu = new Privilege("车辆年审", "/carExamine_list", carMenu);
		privilegeDao.save(carExamineMenu);
		privilegeDao.save(new Privilege("", "/carExamine_delete", carExamineMenu));
		privilegeDao.save(new Privilege("", "/carExamine_addUI", carExamineMenu));
		privilegeDao.save(new Privilege("", "/carExamine_add", carExamineMenu));
		privilegeDao.save(new Privilege("", "/carExamine_editUI", carExamineMenu));
		privilegeDao.save(new Privilege("", "/carExamine_edit", carExamineMenu));
		privilegeDao.save(new Privilege("", "/carExamine_register", carExamineMenu));
		privilegeDao.save(new Privilege("", "/carExamine_appoint", carExamineMenu));
		privilegeDao.save(new Privilege("", "/carExamine_remind", carExamineMenu));
		privilegeDao.save(new Privilege("", "/carExamine_detail", carExamineMenu));
		privilegeDao.save(new Privilege("", "/carExamine_examine", carExamineMenu));
		privilegeDao.save(new Privilege("", "/carExamine_saveAppointment", carExamineMenu));
		
		Privilege tollChargeMenu = new Privilege("路桥费", "/tollCharge_list", carMenu);
		privilegeDao.save(tollChargeMenu);
		privilegeDao.save(new Privilege("", "/tollCharge_queryForm", tollChargeMenu));
		privilegeDao.save(new Privilege("", "/tollCharge_delete", tollChargeMenu));
		privilegeDao.save(new Privilege("", "/tollCharge_editUI", tollChargeMenu));
		privilegeDao.save(new Privilege("", "/tollCharge_freshList", tollChargeMenu));
		privilegeDao.save(new Privilege("", "/tollCharge_saveUI", tollChargeMenu));
		privilegeDao.save(new Privilege("", "/tollCharge_remind", tollChargeMenu));
		privilegeDao.save(new Privilege("", "/tollCharge_save", tollChargeMenu));
		privilegeDao.save(new Privilege("", "/tollCharge_edit", tollChargeMenu));
		privilegeDao.save(new Privilege("", "/tollCharge_remindExport", tollChargeMenu));
		
		//客户管理
		Privilege customerMenu = new Privilege("客户管理", "/customer", null);
		privilegeDao.save(customerMenu);
		Privilege customerInformationMenu = new Privilege("客户信息", "/customer_list", customerMenu);
		privilegeDao.save(customerInformationMenu);
		privilegeDao.save(new Privilege("","/customer_delete",customerInformationMenu));
		privilegeDao.save(new Privilege("","/customer_addUI",customerInformationMenu));
		privilegeDao.save(new Privilege("","/customer_add",customerInformationMenu));
		privilegeDao.save(new Privilege("","/customer_editUI",customerInformationMenu));
		privilegeDao.save(new Privilege("","/customer_edit",customerInformationMenu));
		privilegeDao.save(new Privilege("","/customer_validateAdd",customerInformationMenu));
		privilegeDao.save(new Privilege("","/customer_validateEdit",customerInformationMenu));
		privilegeDao.save(new Privilege("","/customer_isCanDelete",customerInformationMenu));
		privilegeDao.save(new Privilege("","/customer_checkPeople",customerInformationMenu));
		privilegeDao.save(new Privilege("","/customer_customer",customerInformationMenu));
		
		Privilege customerOrganizationMenu = new Privilege("单位信息", "/customerOrganization_list", customerMenu);
		privilegeDao.save(customerOrganizationMenu);
		privilegeDao.save(new Privilege("", "/customerOrganization_popup", customerOrganizationMenu));
		privilegeDao.save(new Privilege("", "/customerOrganization_delete", customerOrganizationMenu));
		privilegeDao.save(new Privilege("", "/customerOrganization_addUI", customerOrganizationMenu));
		privilegeDao.save(new Privilege("", "/customerOrganization_add", customerOrganizationMenu));
		privilegeDao.save(new Privilege("", "/customerOrganization_editUI", customerOrganizationMenu));
		privilegeDao.save(new Privilege("", "/customerOrganization_edit", customerOrganizationMenu));
		privilegeDao.save(new Privilege("", "/customerOrganization_isCanDelete", customerOrganizationMenu));
		privilegeDao.save(new Privilege("", "/customerOrganization_customerOrganization", customerOrganizationMenu));
		
		//查询统计
		Privilege statisticMenu = new Privilege("查询统计", "/statistic", null);
		privilegeDao.save(statisticMenu);
		Privilege refuelStatisticMenu = new Privilege("加油数据", "/refuelStatistic_list", statisticMenu);
		privilegeDao.save(refuelStatisticMenu);
		privilegeDao.save(new Privilege("","/refuelStatistic_weekList",refuelStatisticMenu));
		privilegeDao.save(new Privilege("","/refuelStatistic_monthList",refuelStatisticMenu));
		privilegeDao.save(new Privilege("","/refuelStatistic_query",refuelStatisticMenu));
		privilegeDao.save(new Privilege("", "/refuelStatistic_MoMHistogram", refuelStatisticMenu));
		privilegeDao.save(new Privilege("", "/refuelStatistic_YoYHistogram", refuelStatisticMenu));
		
		Privilege insuranceStatisticMenu = new Privilege("保险记录", "/insuranceStatistic_list", statisticMenu);
		privilegeDao.save(insuranceStatisticMenu);
		privilegeDao.save(new Privilege("", "/insuranceStatistic_weekList", insuranceStatisticMenu));
		privilegeDao.save(new Privilege("", "/insuranceStatistic_monthList", insuranceStatisticMenu));
		privilegeDao.save(new Privilege("", "/insuranceStatistic_query", insuranceStatisticMenu));
		privilegeDao.save(new Privilege("", "/insuranceStatistic_MoMHistogram", insuranceStatisticMenu));
		privilegeDao.save(new Privilege("", "/insuranceStatistic_YoYHistogram", insuranceStatisticMenu));
		
		Privilege careStatisticMenu = new Privilege("保养记录", "/careStatistic_list", statisticMenu);
		privilegeDao.save(careStatisticMenu);
		privilegeDao.save(new Privilege("", "/careStatistic_weekList", careStatisticMenu));
		privilegeDao.save(new Privilege("", "/careStatistic_monthList", careStatisticMenu));
		privilegeDao.save(new Privilege("", "/careStatistic_query", careStatisticMenu));
		privilegeDao.save(new Privilege("", "/careStatistic_care", careStatisticMenu));
		privilegeDao.save(new Privilege("", "/careStatistic_showHistogram", careStatisticMenu));
		privilegeDao.save(new Privilege("", "/careStatistic_MoMHistogram", careStatisticMenu));
		privilegeDao.save(new Privilege("", "/careStatistic_YoYHistogram", careStatisticMenu));
		
		Privilege repairStatisticMenu = new Privilege("维修记录", "/repairStatistic_list", statisticMenu);
		privilegeDao.save(repairStatisticMenu);
		privilegeDao.save(new Privilege("", "/repairStatistic_weekList", repairStatisticMenu));
		privilegeDao.save(new Privilege("", "/repairStatistic_monthList", repairStatisticMenu));
		privilegeDao.save(new Privilege("", "/repairStatistic_query", repairStatisticMenu));
		privilegeDao.save(new Privilege("", "/repairStatistic_MoMHistogram", repairStatisticMenu));
		privilegeDao.save(new Privilege("", "/repairStatistic_YoYHistogram", repairStatisticMenu));
		
		// =======================================================================
		// 3，角色数据
		Role scheduleRole=new Role();
		scheduleRole.setName("调度员");
		scheduleRole.setPrivileges(new HashSet<Privilege>());
		scheduleRole.getPrivileges().add(scheduleMenu);
		scheduleRole.setNoPrivileges(new HashSet<Privilege>());
		scheduleRole.getNoPrivileges().add(privilegeDao.getPrivilegeByUrl("/schedule_scheduleFromQueue"));
		roleDao.save(scheduleRole);
				
		Role advanceScheduleRole=new Role();
		advanceScheduleRole.setName("高级调度员");
		advanceScheduleRole.setPrivileges(new HashSet<Privilege>());
		advanceScheduleRole.getPrivileges().add(privilegeDao.getPrivilegeByUrl("/schedule_scheduleFromQueue"));
		roleDao.save(advanceScheduleRole);
		
		Role monitorRole=new Role();
		monitorRole.setName("监控员");
		monitorRole.setPrivileges(new HashSet<Privilege>());
		monitorRole.getPrivileges().add(monitorMenu);
		roleDao.save(monitorRole);
		
		Role receiptRole=new Role();
		receiptRole.setName("财务人员");
		receiptRole.setPrivileges(new HashSet<Privilege>());
		receiptRole.getPrivileges().add(receiptMenu);
		roleDao.save(receiptRole);
		
		Role carManagerRole=new Role();
		carManagerRole.setName("车辆管理员");
		carManagerRole.setPrivileges(new HashSet<Privilege>());
		carManagerRole.getPrivileges().add(carMenu);
		roleDao.save(carManagerRole);
		
		Role customerManagerRole = new Role();
		customerManagerRole.setName("客户管理员");
		customerManagerRole.setPrivileges(new HashSet<Privilege>());
		customerManagerRole.getPrivileges().add(customerMenu);
		roleDao.save(customerManagerRole);
		
		Role companyLeaderRole = new Role();
		companyLeaderRole.setName("公司领导");
		companyLeaderRole.setPrivileges(new HashSet<Privilege>());
		companyLeaderRole.getPrivileges().add(statisticMenu);
		roleDao.save(companyLeaderRole);
		
		installWatchKeeper();
		initPriceTable();
	}
		
	private void initPriceTable(){
		initPriceTable("D:\\Yuqin\\文档\\初始化文档\\价格表.xls","价格表");
		initPriceTable("D:\\Yuqin\\文档\\初始化文档\\商社协议价格表.xls","商社协议价");
	}
	
	private void initPriceTable(String fileName,String tableName){
		Map<CarServiceType,Price> map=new HashMap<CarServiceType,Price>();
		
		List<List<String>> tableContent=ExcelUtil.getLinesFromExcel(fileName, 2, 23, 1, 7, 0);
		for(List<String> line:tableContent){
			CarServiceSuperType superType=carServiceSuperTypeDao.getCarServiceSuperTypeByTitle(line.get(0));
			if(superType==null){
				superType=new CarServiceSuperType();
				superType.setTitle(line.get(0));
				carServiceSuperTypeDao.save(superType);
			}
			
			CarServiceType carServiceType=carServiceTypeDao.getCarServiceTypeByTitle(line.get(1));
			if(carServiceType==null){
				carServiceType=new CarServiceType();
				carServiceType.setTitle(line.get(1));
				carServiceType.setSuperType(superType);
				carServiceTypeDao.save(carServiceType);
			}
			
			Price price=new Price();
			price.setPerDay(new BigDecimal(Float.valueOf(line.get(2))));
			price.setPerHalfDay(new BigDecimal(Float.valueOf(line.get(3))));
			price.setPerMileAfterLimit(new BigDecimal(Float.valueOf(line.get(4))));
			price.setPerHourAfterLimit(new BigDecimal(Float.valueOf(line.get(5))));
			price.setPerPlaneTime(new BigDecimal(Float.valueOf(line.get(6))));
			priceDao.save(price);
			
			map.put(carServiceType, price);
		}

		PriceTable priceTable=new PriceTable();
		priceTable.setTitle(tableName);
		priceTable.setCarServiceType(map);
		priceTableDao.save(priceTable);
	}
	
	private void installWatchKeeper(){
		WatchKeeper wk=new WatchKeeper();
		wk.setKeeper(userDao.getById(1L));
		wk.setOnDuty(false);
		watchKeeperService.saveWatchKeeper(wk);
	}

	public static void main(String[] args) {
		System.out.println("正在初始化数据...");

		// 从Spring容器中取出对象
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		Installer installer = (Installer) ac.getBean("installer");
		// 执行安装
		installer.install();

		System.out.println("初始化数据完毕！");
	}

}
