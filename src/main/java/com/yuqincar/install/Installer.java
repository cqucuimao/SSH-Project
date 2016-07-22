package com.yuqincar.install;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import com.yuqincar.dao.car.CarDao;
import com.yuqincar.dao.car.CarServiceTypeDao;
import com.yuqincar.dao.car.DriverLicenseDao;
import com.yuqincar.dao.car.ServicePointDao;
import com.yuqincar.dao.order.CarServiceSuperTypeDao;
import com.yuqincar.dao.order.PriceDao;
import com.yuqincar.dao.order.PriceTableDao;
import com.yuqincar.dao.privilege.DepartmentDao;
import com.yuqincar.dao.privilege.PrivilegeDao;
import com.yuqincar.dao.privilege.RoleDao;
import com.yuqincar.dao.privilege.UserDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarServiceSuperType;
import com.yuqincar.domain.car.CarServiceType;
import com.yuqincar.domain.car.CarStatusEnum;
import com.yuqincar.domain.car.DriverLicense;
import com.yuqincar.domain.car.PlateTypeEnum;
import com.yuqincar.domain.car.ServicePoint;
import com.yuqincar.domain.order.Price;
import com.yuqincar.domain.order.PriceTable;
import com.yuqincar.domain.order.WatchKeeper;
import com.yuqincar.domain.privilege.Department;
import com.yuqincar.domain.privilege.Privilege;
import com.yuqincar.domain.privilege.Role;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.domain.privilege.UserGenderEnum;
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
	
	@Resource
	private DepartmentDao departmentDao;
	
	@Resource
	private DriverLicenseDao driverLicenseDao;

	@Resource
	private ServicePointDao servicePointDao;
	
	@Resource
	private CarDao carDao;

	
	@Transactional
	public void install() {
		Session session = sessionFactory.getCurrentSession();
		
		//权限数据
		Privilege  menu1, menu2, menu3, menu4, menu5;

		//权限管理
		menu1 = new Privilege("权限管理", "/privilege", null);
		privilegeDao.save(menu1);
		Privilege menu1_1 = new Privilege("角色管理", "/role_list", menu1);
		privilegeDao.save(menu1_1);
		privilegeDao.save(new Privilege("", "/role_delete", menu1_1));
		privilegeDao.save(new Privilege("", "/role_add", menu1_1));
		privilegeDao.save(new Privilege("", "/role_edit", menu1_1));
		privilegeDao.save(new Privilege("", "/role_setPrivilege", menu1_1));

		Privilege menu1_2 = new Privilege("部门管理", "/department_list", menu1);
		privilegeDao.save(menu1_2);
		privilegeDao.save(new Privilege("", "/department_delete", menu1_2));
		privilegeDao.save(new Privilege("", "/department_add", menu1_2));
		privilegeDao.save(new Privilege("", "/department_edit", menu1_2));

		Privilege menu1_3 = new Privilege("用户管理", "/user_list", menu1);
		privilegeDao.save(menu1_3);
		privilegeDao.save(new Privilege("", "/user_queryList", menu1_3));
		privilegeDao.save(new Privilege("", "/user_freshList", menu1_3));
		privilegeDao.save(new Privilege("", "/user_delete", menu1_3));
		privilegeDao.save(new Privilege("", "/user_add", menu1_3));
		privilegeDao.save(new Privilege("", "/user_edit", menu1_3));
		privilegeDao.save(new Privilege("", "/user_initPassword", menu1_3));
		
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
		privilegeDao.save(new Privilege("","/order_editOrderBillUI",orderMenu));
		privilegeDao.save(new Privilege("","/order_editOrderBill",orderMenu));
		privilegeDao.save(new Privilege("","/order_editDriverAction",orderMenu));
		privilegeDao.save(new Privilege("","/order_addAcceptAction",orderMenu));
		privilegeDao.save(new Privilege("","/order_addBeginAction",orderMenu));
		privilegeDao.save(new Privilege("","/order_addGetonAction",orderMenu));
		privilegeDao.save(new Privilege("","/order_addGetoffAction",orderMenu));
		privilegeDao.save(new Privilege("","/order_addEndAction",orderMenu));
		privilegeDao.save(new Privilege("","/order_deleteDriverAction",orderMenu));
		privilegeDao.save(new Privilege("","/order_popupModify",orderMenu));
		privilegeDao.save(new Privilege("","/order_modifyDriverActionTime",orderMenu));
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
		
		Privilege orderStatementMenu=new Privilege("对账单","/orderStatement_newList",receiptMenu);
		privilegeDao.save(orderStatementMenu);
		privilegeDao.save(new Privilege("","/orderStatement_invoicedList",orderStatementMenu));
		privilegeDao.save(new Privilege("","/orderStatement_paidList",orderStatementMenu));
		privilegeDao.save(new Privilege("","/orderStatement_newDetail",orderStatementMenu));
		privilegeDao.save(new Privilege("","/orderStatement_invoicedDetail",orderStatementMenu));
		privilegeDao.save(new Privilege("","/orderStatement_paidDetail",orderStatementMenu));
		privilegeDao.save(new Privilege("","/orderStatement_generatePDF",orderStatementMenu));
		privilegeDao.save(new Privilege("","/orderStatement_cancelOrders",orderStatementMenu));
		privilegeDao.save(new Privilege("","/orderStatement_cancelOrderStatement",orderStatementMenu));
		privilegeDao.save(new Privilege("","/orderStatement_gatherMoneyDo",orderStatementMenu));
		privilegeDao.save(new Privilege("","/orderStatement_gatherComplete",orderStatementMenu));
		privilegeDao.save(new Privilege("","/orderStatement_invoiceDo",orderStatementMenu));
		privilegeDao.save(new Privilege("","/orderStatement_gatherMoney",orderStatementMenu));
		privilegeDao.save(new Privilege("","/orderStatement_invoice",orderStatementMenu));
		privilegeDao.save(new Privilege("","/orderStatement_paidListQueryForm",orderStatementMenu));
		privilegeDao.save(new Privilege("","/orderStatement_freshPaidList",orderStatementMenu));
		
		//车辆管理
		Privilege carMenu = new Privilege("车辆管理", "/car", null);
		privilegeDao.save(carMenu);
		Privilege informationMenu=new Privilege("基本信息","/car_list",carMenu);
		privilegeDao.save(informationMenu);
		privilegeDao.save(new Privilege("","/car_queryList",informationMenu));
		privilegeDao.save(new Privilege("","/car_freshList",informationMenu));
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
		privilegeDao.save(new Privilege("", "/carCare_saveAppointment", carCareMenu));
		privilegeDao.save(new Privilege("", "/carCare_queryForm", carCareMenu));
		privilegeDao.save(new Privilege("", "/carCare_freshList", carCareMenu));
		
		Privilege carInsuranceMenu = new Privilege("车辆保险", "/carInsurance_list", carMenu);
		privilegeDao.save(carInsuranceMenu);
		privilegeDao.save(new Privilege("", "/carInsurance_queryList", carInsuranceMenu));
		privilegeDao.save(new Privilege("", "/carInsurance_freshList", carInsuranceMenu));
		privilegeDao.save(new Privilege("", "/carInsurance_addUI", carInsuranceMenu));
		privilegeDao.save(new Privilege("", "/carInsurance_add", carInsuranceMenu));
		privilegeDao.save(new Privilege("", "/carInsurance_editUI", carInsuranceMenu));
		privilegeDao.save(new Privilege("", "/carInsurance_remind", carInsuranceMenu));
		privilegeDao.save(new Privilege("", "/carInsurance_detail", carInsuranceMenu));
		privilegeDao.save(new Privilege("", "/carInsurance_insurance", carInsuranceMenu));
		privilegeDao.save(new Privilege("", "/carInsurance_validateAdd", carInsuranceMenu));
		privilegeDao.save(new Privilege("", "/commercialInsuranceType_list", carInsuranceMenu));
		privilegeDao.save(new Privilege("", "/commercialInsuranceType_addUI", carInsuranceMenu));
		privilegeDao.save(new Privilege("", "/commercialInsuranceType_add", carInsuranceMenu));
		privilegeDao.save(new Privilege("", "/commercialInsuranceType_editUI", carInsuranceMenu));
		privilegeDao.save(new Privilege("", "/commercialInsuranceType_edit", carInsuranceMenu));
		privilegeDao.save(new Privilege("", "/commercialInsuranceType_delete", carInsuranceMenu));
		
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
		privilegeDao.save(new Privilege("", "/carRepair_refreshList", carRepairMenu));
		privilegeDao.save(new Privilege("", "/carRepair_queryForm", carRepairMenu));
		
		Privilege carRefuelMenu = new Privilege("加油信息", "/carRefuel_list", carMenu);
		privilegeDao.save(carRefuelMenu);
		privilegeDao.save(new Privilege("", "/carRefuel_queryList", carRefuelMenu));
		privilegeDao.save(new Privilege("", "/carRefuel_freshList", carRefuelMenu));
		privilegeDao.save(new Privilege("", "/carRefuel_addUI", carRefuelMenu));
		privilegeDao.save(new Privilege("", "/carRefuel_add", carRefuelMenu));
		privilegeDao.save(new Privilege("", "/carRefuel_editUI", carRefuelMenu));
		privilegeDao.save(new Privilege("", "/carRefuel_detail", carRefuelMenu));
		privilegeDao.save(new Privilege("", "/carRefuel_refuel", carRefuelMenu));
		privilegeDao.save(new Privilege("", "/carRefuel_importExcelFile", carRefuelMenu));
		privilegeDao.save(new Privilege("", "/carRefuel_excel", carRefuelMenu));
		
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
		privilegeDao.save(new Privilege("", "/carExamine_freshList", carExamineMenu));
		privilegeDao.save(new Privilege("", "/carExamine_saveAppointment", carExamineMenu));
		privilegeDao.save(new Privilege("", "/carExamine_getNextExamineDate", carExamineMenu));
		privilegeDao.save(new Privilege("", "/carExamine_queryForm", carExamineMenu));
		
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
		
		//物品领用
		Privilege materialReceiveMenu = new Privilege("物品领用", "/materialReceive_list", carMenu);
		privilegeDao.save(materialReceiveMenu);
		privilegeDao.save(new Privilege("", "/materialReceive_queryForm", materialReceiveMenu));
		privilegeDao.save(new Privilege("", "/materialReceive_delete", materialReceiveMenu));
		privilegeDao.save(new Privilege("", "/materialReceive_editUI", materialReceiveMenu));
		privilegeDao.save(new Privilege("", "/materialReceive_freshList", materialReceiveMenu));
		privilegeDao.save(new Privilege("", "/materialReceive_saveUI", materialReceiveMenu));
		privilegeDao.save(new Privilege("", "/materialReceive_save", materialReceiveMenu));
		privilegeDao.save(new Privilege("", "/materialReceive_edit", materialReceiveMenu));
		
		//洗车登记
		Privilege carWashMenu = new Privilege("洗车登记", "/carWash_list", carMenu);
		privilegeDao.save(carWashMenu);
		privilegeDao.save(new Privilege("", "/carWash_queryForm", carWashMenu));
		privilegeDao.save(new Privilege("", "/carWash_delete", carWashMenu));
		privilegeDao.save(new Privilege("", "/carWash_editUI", carWashMenu));
		privilegeDao.save(new Privilege("", "/carWash_freshList", carWashMenu));
		privilegeDao.save(new Privilege("", "/carWash_saveUI", carWashMenu));
		privilegeDao.save(new Privilege("", "/carWash_save", carWashMenu));
		privilegeDao.save(new Privilege("", "/carWash_edit", carWashMenu));
		privilegeDao.save(new Privilege("", "/carWashShop_list", carWashMenu));
		privilegeDao.save(new Privilege("", "/carWashShop_addUI", carWashMenu));
		privilegeDao.save(new Privilege("", "/carWashShop_delete", carWashMenu));	
		privilegeDao.save(new Privilege("", "/carWashShop_add", carWashMenu));
		privilegeDao.save(new Privilege("", "/carWashShop_editUI", carWashMenu));
		privilegeDao.save(new Privilege("", "/carWashShop_edit", carWashMenu));
		
		//车辆违章
		Privilege carViolationMenu = new Privilege("车辆违章", "/carViolation_list", carMenu);
		privilegeDao.save(carViolationMenu);
		privilegeDao.save(new Privilege("", "/carViolation_queryForm", carViolationMenu));
		privilegeDao.save(new Privilege("", "/carViolation_delete", carViolationMenu));
		privilegeDao.save(new Privilege("", "/carViolation_editUI", carViolationMenu));
		privilegeDao.save(new Privilege("", "/carViolation_freshList", carViolationMenu));
		privilegeDao.save(new Privilege("", "/carViolation_saveUI", carViolationMenu));
		privilegeDao.save(new Privilege("", "/carViolation_save", carViolationMenu));
		privilegeDao.save(new Privilege("", "/carViolation_edit", carViolationMenu));
		
		//客户管理
		Privilege customerMenu = new Privilege("客户管理", "/customer", null);
		privilegeDao.save(customerMenu);
		Privilege customerInformationMenu = new Privilege("客户信息", "/customer_list", customerMenu);
		privilegeDao.save(customerInformationMenu);
		privilegeDao.save(new Privilege("","/customer_queryList",customerInformationMenu));
		privilegeDao.save(new Privilege("","/customer_freshList",customerInformationMenu));
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
		privilegeDao.save(new Privilege("", "/customerOrganization_queryList", customerOrganizationMenu));
		privilegeDao.save(new Privilege("", "/customerOrganization_freshList", customerOrganizationMenu));
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
		
		Role carInfoManagerRole=new Role();
		carInfoManagerRole.setName("车辆信息管理员");
		carInfoManagerRole.setPrivileges(new HashSet<Privilege>());
		carInfoManagerRole.getPrivileges().add(informationMenu);
		roleDao.save(carInfoManagerRole);
		
		Role carViolationManagerRole=new Role();
		carViolationManagerRole.setName("违章信息管理员");
		carViolationManagerRole.setPrivileges(new HashSet<Privilege>());
		carViolationManagerRole.getPrivileges().add( carViolationMenu);
		roleDao.save(carViolationManagerRole);
		
		Role carCareManagerRole=new Role();
		carCareManagerRole.setName("车辆保养管理员");
		carCareManagerRole.setPrivileges(new HashSet<Privilege>());
		carCareManagerRole.getPrivileges().add(carCareMenu);
		roleDao.save(carCareManagerRole);
		
		Role carInsuranceManagerRole=new Role();
		carInsuranceManagerRole.setName("保险信息管理员");
		carInsuranceManagerRole.setPrivileges(new HashSet<Privilege>());
		carInsuranceManagerRole.getPrivileges().add(carInsuranceMenu);
		roleDao.save(carInsuranceManagerRole);
		
		Role carRepairManagerRole=new Role();
		carRepairManagerRole.setName("车辆维修管理员");
		carRepairManagerRole.setPrivileges(new HashSet<Privilege>());
		carRepairManagerRole.getPrivileges().add(carRepairMenu);
		roleDao.save(carRepairManagerRole);
		
		Role carRefuelManagerRole=new Role();
		carRefuelManagerRole.setName("加油信息管理员");
		carRefuelManagerRole.setPrivileges(new HashSet<Privilege>());
		carRefuelManagerRole.getPrivileges().add(carRefuelMenu);
		roleDao.save(carRefuelManagerRole);
		
		Role carExamineManagerRole=new Role();
		carExamineManagerRole.setName("车辆年审管理员");
		carExamineManagerRole.setPrivileges(new HashSet<Privilege>());
		carExamineManagerRole.getPrivileges().add(carExamineMenu);
		roleDao.save(carExamineManagerRole);
		
		Role carTollChargeManagerRole=new Role();
		carTollChargeManagerRole.setName("路桥费信息管理员");
		carTollChargeManagerRole.setPrivileges(new HashSet<Privilege>());
		carTollChargeManagerRole.getPrivileges().add(tollChargeMenu);
		roleDao.save(carTollChargeManagerRole);
		
		Role materialReceiveManagerRole=new Role();
		materialReceiveManagerRole.setName("物品领用信息管理员");
		materialReceiveManagerRole.setPrivileges(new HashSet<Privilege>());
		materialReceiveManagerRole.getPrivileges().add(materialReceiveMenu);
		roleDao.save(materialReceiveManagerRole);
		
		Role carWashManagerRole=new Role();
		carWashManagerRole.setName("洗车信息管理员");
		carWashManagerRole.setPrivileges(new HashSet<Privilege>());
		carWashManagerRole.getPrivileges().add(carWashMenu);
		roleDao.save(carWashManagerRole);
		
		Role adminRole=new Role();
		adminRole.setName("超级管理员");
		adminRole.setPrivileges(new HashSet<Privilege>());
		adminRole.getPrivileges().add(menu1);
		adminRole.getPrivileges().add(serviceTypeMenu);
		adminRole.getPrivileges().add(servicePointMenu);
		roleDao.save(adminRole);
		
		Department department = new Department();
		department.setName("公司");
		departmentDao.save(department);
		User admin = new User();
		admin.setLoginName("admin");
		admin.setName("超级管理员");
		admin.setPhoneNumber("110");
		admin.setPassword(DigestUtils.md5Hex("admin")); // 密码要使用MD5摘要
		admin.setDepartment(department);
		admin.setStatus(UserStatusEnum.NORMAL);
		admin.setUserType(UserTypeEnum.OFFICE);
		admin.setRoles(new HashSet<Role>());
		admin.getRoles().add(adminRole);
		userDao.save(admin); // 保存
		
//		Role carManagerRole=new Role();
//		carManagerRole.setName("车辆管理员");
//		carManagerRole.setPrivileges(new HashSet<Privilege>());
//		carManagerRole.getPrivileges().add(carMenu);
//		roleDao.save(carManagerRole);
		
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
		initServicePoint();
		initDepartmentTable();
		initEmployeeTable();
		initCarTable();
	}
	//初始化业务点
	private void initServicePoint(){
		ServicePoint sp = new ServicePoint();
		sp.setName("企业天地业务点");
		servicePointDao.save(sp);
	}
	
	//初始化部门
	private void initDepartmentTable(){
		initDepartmentTable("D:\\Yuqin\\文档\\初始化文档\\部门表.xls");
	}
		
	private void initDepartmentTable(String fileName){
		List<List<String>> tableContent=ExcelUtil.getLinesFromExcel(fileName, 2, 1, 2, 0);
		for(List<String> line:tableContent){
			Department parentDepartment = departmentDao.getDepartmentByName(line.get(1));	
				if(parentDepartment == null){
					parentDepartment = new Department();
					parentDepartment.setName(line.get(1));
					departmentDao.save(parentDepartment);
				}
				Department department = departmentDao.getDepartmentByName(line.get(0));
				if(department == null){
					department = new Department();
					department.setName(line.get(0));
					department.setParent(parentDepartment);
					departmentDao.save(department);
				}
		}
	}
	
	//初始化员工
	private void initEmployeeTable(){
		initEmployeeTable("D:\\Yuqin\\文档\\初始化文档\\员工表.xls");
	}
	
	private void initEmployeeTable(String fileName){
		List<List<String>> tableContent = ExcelUtil.getLinesFromExcel(fileName, 2, 1, 8, 0);
		for(List<String> line:tableContent){
			User user = userDao.getByLoginName(line.get(0));
			if(user == null){
				user = new User();
				user.setLoginName(line.get(0));
				user.setName(line.get(0));
				user.setPassword(DigestUtils.md5Hex("123456"));
				Department department = departmentDao.getDepartmentByName(line.get(1));
				user.setDepartment(department);
				//设置角色
				List<Role> role = null;
				Long [] ids = new Long[1];
				if(line.get(1) == "公司" || line.get(1).equals("公司")){
					 ids[0] =(long) 7; 
					 role= (List<Role>) roleDao.getByIds(ids);	
					 user.setRoles(new HashSet<Role>(role));
				}else if(line.get(1) == "财务统计科" || line.get(1).equals("财务统计科")){
					ids[0] =(long) 4; 
					role= (List<Role>) roleDao.getByIds(ids);	
					user.setRoles(new HashSet<Role>(role));
				}else if(line.get(1) == "技术保障科" || line.get(1).equals("技术保障科")){
					ids[0] =(long) 5; 
					role= (List<Role>) roleDao.getByIds(ids);		
					user.setRoles(new HashSet<Role>(role));
				}else if((line.get(1) == "运营科" || line.get(1).equals("运营科")) && (line.get(3) == "否" || line.get(3).equals("否"))){
					ids[0] =(long) 1; 
					role= (List<Role>) roleDao.getByIds(ids);		
					user.setRoles(new HashSet<Role>(role));
				}else{
					user.setRoles(null);
				}	
				user.setPhoneNumber(line.get(2));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");  
				try {
				    Date birth = sdf.parse(line.get(4));
					user.setBirth(birth);
				} catch (Exception e) {
					e.printStackTrace();
				}
				user.setGender(UserGenderEnum.getByLabel(line.get(5)));
				
				if(line.get(3) == "否" || line.get(3).equals("否")){
					user.setUserType(UserTypeEnum.OFFICE);
					user.setDriverLicense(null);
				}
				if(line.get(3) == "是" || line.get(3).equals("是")){
					user.setUserType(UserTypeEnum.DRIVER);
					DriverLicense driverLicense = new DriverLicense();
					driverLicense.setLicenseID(line.get(6));
					try {
						Date expireDate = sdf.parse(line.get(7));
						driverLicense.setExpireDate(expireDate);
					} catch (Exception e) {
						e.printStackTrace();
					}
					driverLicenseDao.save(driverLicense);
					user.setDriverLicense(driverLicense);
				}
			    user.setStatus(UserStatusEnum.NORMAL);
			    userDao.save(user);
			}
		}
	}
	
	//初始化价格
	private void initPriceTable(){
		initPriceTable("D:\\Yuqin\\文档\\初始化文档\\价格表.xls","价格表");
		initPriceTable("D:\\Yuqin\\文档\\初始化文档\\商社协议价格表.xls","商社协议价");
	}
	
	private void initPriceTable(String fileName,String tableName){
		Map<CarServiceType,Price> map=new HashMap<CarServiceType,Price>();
		
		List<List<String>> tableContent=ExcelUtil.getLinesFromExcel(fileName, 2, 1, 7, 0);
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
	
	//初始化车辆
	private void initCarTable(){
		initCarTable("D:\\Yuqin\\文档\\初始化文档\\车辆表.xls");
	}
		
	private void initCarTable(String fileName){

		List<List<String>> tableContent=ExcelUtil.getLinesFromExcel(fileName, 2, 1, 14, 0);
		for(List<String> line:tableContent){
			Car car = carDao.getByPlateNumber(line.get(0));
				if(car == null){
					car = new Car();
					car.setPlateNumber(line.get(0));
					car.setServicePoint(servicePointDao.getById((long)1));
					car.setBrand(line.get(1));
					car.setModel(line.get(2));
					car.setVIN(line.get(3));
					car.setEngineSN(line.get(4));
					car.setStatus(CarStatusEnum.NORMAL);
					car.setServiceType(carServiceTypeDao.getCarServiceTypeByTitle(line.get(5)));
					//是否为备用车，非备用车指定司机
					boolean standbyCar = true;
					if(line.get(7) == "否" || line.get(7).equals("否")){
						car.setDriver(userDao.getByLoginName(line.get(6)));
						standbyCar = false;
						car.setStandbyCar(standbyCar);
					}else{
						car.setStandbyCar(standbyCar);
					}
					//注册时间
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");  
					try {
					    Date registDate = sdf.parse(line.get(8));
					    car.setRegistDate(registDate);
					} catch (Exception e) {
						e.printStackTrace();
					}
					car.setPlateType(PlateTypeEnum.getByLabel(line.get(9)));
					
					int seatNumber = Integer.parseInt(line.get(10));
					car.setSeatNumber(seatNumber);
					//保险过期日期
					if(line.get(11) == "无" || line.get(11).equals("无")){
						car.setInsuranceExpiredDate(null);
						car.setInsuranceExpired(true);
					}else{
						try {
						    Date insuranceExpiredDate = sdf.parse(line.get(11));
						    car.setInsuranceExpiredDate(insuranceExpiredDate);
						    Date now = new Date();
						    if(insuranceExpiredDate.after(now)){
						    	car.setInsuranceExpired(true);
						    }else{
						    	car.setInsuranceExpired(false);
						    }
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					//年检到期日期
					if(line.get(12) == "无" || line.get(12).equals("无")){
						car.setNextExaminateDate(null);
					}else{
						try {
						    Date nextExaminateDate = sdf.parse(line.get(12));
						    car.setNextExaminateDate(nextExaminateDate);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					//下次路桥费日期
					if(line.get(13) == "无" || line.get(13).equals("无")){
						car.setNextTollChargeDate(null);
					}else{
						try {
						    Date nextTollChargeDate = sdf.parse(line.get(12));
						    car.setNextTollChargeDate(nextTollChargeDate);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					carDao.save(car);
				}
				
		}
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
