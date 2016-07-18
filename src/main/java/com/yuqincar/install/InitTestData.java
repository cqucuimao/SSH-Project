package com.yuqincar.install;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.CustomerOrganization.CustomerOrganizationDao;
import com.yuqincar.dao.car.CarCareDao;
import com.yuqincar.dao.car.CarDao;
import com.yuqincar.dao.car.CarExamineDao;
import com.yuqincar.dao.car.CarInsuranceDao;
import com.yuqincar.dao.car.CarRefuelDao;
import com.yuqincar.dao.car.CarRepairDao;
import com.yuqincar.dao.car.CarServiceTypeDao;
import com.yuqincar.dao.car.DriverLicenseDao;
import com.yuqincar.dao.car.ServicePointDao;
import com.yuqincar.dao.customer.CustomerDao;
import com.yuqincar.dao.customer.OtherPassengerDao;
import com.yuqincar.dao.monitor.DeviceDao;
import com.yuqincar.dao.monitor.LocationDao;
import com.yuqincar.dao.order.CarServiceSuperTypeDao;
import com.yuqincar.dao.order.OrderDao;
import com.yuqincar.dao.order.PriceDao;
import com.yuqincar.dao.order.PriceTableDao;
import com.yuqincar.dao.orderStatement.OrderStatementDao;
import com.yuqincar.dao.privilege.DepartmentDao;
import com.yuqincar.dao.privilege.UserDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarCare;
import com.yuqincar.domain.car.CarExamine;
import com.yuqincar.domain.car.CarInsurance;
import com.yuqincar.domain.car.CarRefuel;
import com.yuqincar.domain.car.CarRepair;
import com.yuqincar.domain.car.CarStatusEnum;
import com.yuqincar.domain.car.DriverLicense;
import com.yuqincar.domain.car.PlateTypeEnum;
import com.yuqincar.domain.car.ServicePoint;
import com.yuqincar.domain.car.TransmissionTypeEnum;
import com.yuqincar.domain.monitor.Device;
import com.yuqincar.domain.order.ChargeModeEnum;
import com.yuqincar.domain.order.Customer;
import com.yuqincar.domain.order.CustomerOrganization;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.order.OrderSourceEnum;
import com.yuqincar.domain.order.OrderStatement;
import com.yuqincar.domain.order.OrderStatusEnum;
import com.yuqincar.domain.order.OtherPassenger;
import com.yuqincar.domain.order.PriceTable;
import com.yuqincar.domain.privilege.Department;
import com.yuqincar.domain.privilege.Role;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.domain.privilege.UserStatusEnum;
import com.yuqincar.domain.privilege.UserTypeEnum;
import com.yuqincar.service.common.DiskFileService;
import com.yuqincar.service.privilege.RoleService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.DateUtils;

@Component
public class InitTestData {
	@Resource
	public SessionFactory sessionFactory;

	@Resource
	private LocationDao locationDao;

	@Resource
	private ServicePointDao servicePointDao;

	@Resource
	private CarServiceTypeDao carServiceTypeDao;

	@Resource
	private DepartmentDao departmentDao;

	@Resource
	private DriverLicenseDao driverLicenseDao;

	@Resource
	private UserDao userDao;

	@Resource
	private CarDao carDao;

	@Resource
	private DeviceDao deviceDao;

	@Resource
	private CustomerDao customerDao;

	@Resource
	private CustomerOrganizationDao customerOrganizationDao;

	@Resource
	private OrderDao orderDao;
	
	@Resource
	private OtherPassengerDao otherPassengerDao;

	@Resource
	private CarCareDao carCareDao;

	@Resource
	private CarExamineDao carExamineDao;

	@Resource
	private CarRepairDao carRepairDao;
	
	@Resource
	private CarRefuelDao carRefuelDao;
	
	@Resource
	private CarInsuranceDao carInsuranceDao;
	
	@Resource
	private OrderStatementDao orderStatementDao;
	
	@Resource
	private PriceDao priceDao;
	
	@Resource
	private PriceTableDao priceTableDao;
	
	@Resource
	private CarServiceSuperTypeDao carServiceSuperTypeDao;

	@Autowired
	private DiskFileService diskFileService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserService userService;

	@Transactional
	public void install() {
		Session session = sessionFactory.getCurrentSession();
		
		initUser();
		initDriver();
		initDevice();
		initCar();
		initCustomer();
		//initOrder();
		//initCarCare();
		//initCarExamine();
		//initCarRepair();
		//initCarRefuel();
		//initCarInsurance();
		//initOrderStatement();
	}

	private void initCarRepair() {
		CarRepair cr1 = new CarRepair();
		cr1.setCar(carDao.getById(1L));
		cr1.setDriver(cr1.getCar().getDriver());
		cr1.setFromDate(DateUtils.getYMD("2016-01-31"));
		cr1.setAppointment(false);
		cr1.setMemo(null);
		cr1.setPayDate(DateUtils.getYMD("2016-01-21"));
		cr1.setMoney(new BigDecimal(88));
		carRepairDao.save(cr1);

		CarRepair cr2 = new CarRepair();
		cr2.setCar(carDao.getById(2L));
		cr2.setDriver(cr2.getCar().getDriver());
		cr2.setFromDate(DateUtils.getYMD("2016-02-01"));
		cr2.setAppointment(false);
		cr2.setMemo(null);
		cr2.setPayDate(DateUtils.getYMD("2016-02-22"));
		cr2.setMoney(new BigDecimal(210));
		carRepairDao.save(cr2);

		CarRepair cr3 = new CarRepair();
		cr3.setCar(carDao.getById(3L));
		cr3.setDriver(cr3.getCar().getDriver());
		cr3.setFromDate(DateUtils.getYMD("2016-02-01"));
		cr3.setAppointment(false);
		cr3.setMemo(null);
		cr3.setPayDate(DateUtils.getYMD("2016-03-23"));
		cr3.setMoney(new BigDecimal(190));
		carRepairDao.save(cr3);
		
		CarRepair cr4 = new CarRepair();
		cr4.setCar(carDao.getById(4L));
		cr4.setDriver(cr4.getCar().getDriver());
		cr4.setFromDate(DateUtils.getYMD("2016-02-01"));
		cr4.setAppointment(false);
		cr4.setMemo(null);
		cr4.setPayDate(DateUtils.getYMD("2016-04-24"));
		cr4.setMoney(new BigDecimal(40));
		carRepairDao.save(cr4);
		
		CarRepair cr5 = new CarRepair();
		cr5.setCar(carDao.getById(5L));
		cr5.setDriver(userDao.getByLoginName("司机2"));
		cr5.setFromDate(DateUtils.getYMD("2016-02-01"));
		cr5.setAppointment(false);
		cr5.setMemo(null);
		cr5.setPayDate(DateUtils.getYMD("2016-05-25"));
		cr5.setMoney(new BigDecimal(240));
		carRepairDao.save(cr5);
	}

	private void initCarExamine() {
		CarExamine ce1 = new CarExamine();
		ce1.setCar(carDao.getById(1L));
		ce1.setDriver(ce1.getCar().getDriver());
		ce1.setDate(DateUtils.getYMD("2016-01-28"));
		ce1.setAppointment(true);
		ce1.setMemo(null);
		carExamineDao.save(ce1);

		CarExamine ce2 = new CarExamine();
		ce2.setCar(carDao.getById(2L));
		ce2.setDriver(ce2.getCar().getDriver());
		ce2.setDate(DateUtils.getYMD("2016-01-29"));
		ce2.setAppointment(true);
		ce2.setMemo(null);
		carExamineDao.save(ce2);

		CarExamine ce3 = new CarExamine();
		ce3.setCar(carDao.getById(3L));
		ce3.setDriver(ce3.getCar().getDriver());
		ce3.setDate(DateUtils.getYMD("2016-01-30"));
		ce3.setAppointment(true);
		ce3.setMemo(null);
		carExamineDao.save(ce3);
	}

	private void initCarCare() {
		CarCare cc1 = new CarCare();
		cc1.setCar(carDao.getById(1L));		
		cc1.setDriver(cc1.getCar().getDriver());
		cc1.setDate(DateUtils.getYMD("2016-01-01"));
		cc1.setMileInterval(7500);
		cc1.setMoney(new BigDecimal(30));
		cc1.setMemo("保养1的备注");
		cc1.setAppointment(false);
		carCareDao.save(cc1);

		CarCare cc2 = new CarCare();
		cc2.setCar(carDao.getById(2L));
		cc2.setDriver(cc2.getCar().getDriver());
		cc2.setDate(DateUtils.getYMD("2016-02-02"));
		cc2.setMileInterval(7500);
		cc2.setMoney(new BigDecimal(150));
		cc2.setMemo("保养2的备注");
		cc2.setAppointment(false);
		carCareDao.save(cc2);

		CarCare cc3 = new CarCare();
		cc3.setCar(carDao.getById(3L));
		cc3.setDriver(cc3.getCar().getDriver());
		cc3.setDate(DateUtils.getYMD("2016-03-03"));
		cc3.setMileInterval(7500);
		cc3.setMoney(new BigDecimal(300));
		cc3.setMemo("保养3的备注");
		cc3.setAppointment(false);
		carCareDao.save(cc3);

		CarCare cc4 = new CarCare();
		cc4.setCar(carDao.getById(4L));
		cc4.setDriver(cc4.getCar().getDriver());
		cc4.setDate(DateUtils.getYMD("2016-04-04"));
		cc4.setMileInterval(7500);
		cc4.setMoney(new BigDecimal(200));
		cc4.setMemo("保养4的备注");
		cc4.setAppointment(false);
		carCareDao.save(cc4);

		CarCare cc5 = new CarCare();
		cc5.setCar(carDao.getById(5L));
		cc5.setDriver(userDao.getByLoginName("司机2"));
		cc5.setDate(DateUtils.getYMD("2016-05-24"));
		cc5.setAppointment(false);
		cc5.setMoney(new BigDecimal(100));
		cc5.setMemo(null);
		carCareDao.save(cc5);

		CarCare cc6 = new CarCare();
		cc6.setCar(carDao.getById(6L));
		cc6.setDriver(cc6.getCar().getDriver());
		cc6.setDate(DateUtils.getYMD("2016-06-25"));
		cc6.setAppointment(false);
		cc6.setMoney(new BigDecimal(600));
		cc6.setMemo(null);
		carCareDao.save(cc6);

		CarCare cc7 = new CarCare();
		cc7.setCar(carDao.getById(7L));
		cc7.setDriver(cc7.getCar().getDriver());
		cc7.setDate(DateUtils.getYMD("2016-07-26"));
		cc7.setAppointment(false);
		cc7.setMoney(new BigDecimal(70));
		cc7.setMemo(null);
		carCareDao.save(cc7);

		CarCare cc8 = new CarCare();
		cc8.setCar(carDao.getById(8L));
		cc8.setDriver(cc8.getCar().getDriver());
		cc8.setDate(DateUtils.getYMD("2016-08-27"));
		cc8.setAppointment(false);
		cc8.setMoney(new BigDecimal(280));
		cc8.setMemo(null);
		carCareDao.save(cc8);
	}
	
	private void initCarRefuel(){
		CarRefuel cr1 = new CarRefuel();
		cr1.setCar(carDao.getById(1L));
		cr1.setDriver(cr1.getCar().getDriver());
		cr1.setDate(DateUtils.getYMD("2016-01-20"));
		cr1.setMoney(new BigDecimal(110));
		carRefuelDao.save(cr1);
		
		CarRefuel cr2 = new CarRefuel();
		cr2.setCar(carDao.getById(2L));
		cr2.setDriver(cr2.getCar().getDriver());
		cr2.setDate(DateUtils.getYMD("2016-02-21"));
		cr2.setMoney(new BigDecimal(170));
		carRefuelDao.save(cr2);
		
		CarRefuel cr3 = new CarRefuel();
		cr3.setCar(carDao.getById(3L));
		cr3.setDriver(cr3.getCar().getDriver());
		cr3.setDate(DateUtils.getYMD("2016-03-22"));
		cr3.setMoney(new BigDecimal(30));
		carRefuelDao.save(cr3);
		
		CarRefuel cr4 = new CarRefuel();
		cr4.setCar(carDao.getById(4L));
		cr4.setDriver(cr4.getCar().getDriver());
		cr4.setDate(DateUtils.getYMD("2016-04-23"));
		cr4.setMoney(new BigDecimal(400));
		carRefuelDao.save(cr4);
		
		CarRefuel cr5 = new CarRefuel();
		cr5.setCar(carDao.getById(5L));
		cr5.setDriver(userDao.getByLoginName("司机2"));
		cr5.setDate(DateUtils.getYMD("2016-05-24"));
		cr5.setMoney(new BigDecimal(240));
		carRefuelDao.save(cr5);
		
		CarRefuel cr6 = new CarRefuel();
		cr6.setCar(carDao.getById(6L));
		cr6.setDriver(cr6.getCar().getDriver());
		cr6.setDate(DateUtils.getYMD("2016-06-25"));
		cr6.setMoney(new BigDecimal(400));
		carRefuelDao.save(cr6);
		
		CarRefuel cr7 = new CarRefuel();
		cr7.setCar(carDao.getById(7L));
		cr7.setDriver(cr7.getCar().getDriver());
		cr7.setDate(DateUtils.getYMD("2016-07-26"));
		cr7.setMoney(new BigDecimal(70));
		carRefuelDao.save(cr7);
	}
	
	private void initCarInsurance(){
		CarInsurance ci1 = new CarInsurance();
		ci1.setCar(carDao.getById(1L));
		ci1.setPayDate(DateUtils.getYMD("2016-01-20"));
		ci1.setFromDate(new Date());
		ci1.setToDate(DateUtils.getYMD("2026-01-20"));
		ci1.setInsureCompany("平安保险");
		ci1.setPolicyNumber("1111111");
		ci1.setMoney(new BigDecimal(10));
		carInsuranceDao.save(ci1);
		
		CarInsurance ci2 = new CarInsurance();
		ci2.setCar(carDao.getById(2L));
		ci2.setPayDate(DateUtils.getYMD("2016-02-21"));
		ci2.setFromDate(new Date());
		ci2.setToDate(DateUtils.getYMD("2026-02-21"));
		ci2.setInsureCompany("平安保险1");
		ci2.setPolicyNumber("2222222");
		ci2.setMoney(new BigDecimal(110));
		carInsuranceDao.save(ci2);
		
		CarInsurance ci3 = new CarInsurance();
		ci3.setCar(carDao.getById(3L));
		ci3.setPayDate(DateUtils.getYMD("2016-03-22"));
		ci3.setFromDate(new Date());
		ci3.setToDate(DateUtils.getYMD("2026-03-22"));
		ci3.setInsureCompany("平安保险2");
		ci3.setPolicyNumber("3333333");
		ci3.setMoney(new BigDecimal(170));
		carInsuranceDao.save(ci3);
		
		CarInsurance ci4 = new CarInsurance();
		ci4.setCar(carDao.getById(4L));
		ci4.setPayDate(DateUtils.getYMD("2016-04-23"));
		ci4.setFromDate(new Date());
		ci4.setToDate(DateUtils.getYMD("2026-04-23"));
		ci4.setInsureCompany("平安保险3");
		ci4.setPolicyNumber("4444444");
		ci4.setMoney(new BigDecimal(210));
		carInsuranceDao.save(ci4);
		
		CarInsurance ci5 = new CarInsurance();
		ci5.setCar(carDao.getById(5L));
		ci5.setPayDate(DateUtils.getYMD("2016-05-24"));
		ci5.setFromDate(new Date());
		ci5.setToDate(DateUtils.getYMD("2026-05-24"));
		ci5.setInsureCompany("平安保险4");
		ci5.setPolicyNumber("5555555");
		ci5.setMoney(new BigDecimal(90));
		carInsuranceDao.save(ci5);
	}
	
	private void initOrderStatement(){
		OrderStatement os1 = new OrderStatement();
		os1.setDate(DateUtils.getYMD("2016-01-07"));
		os1.setFromDate(DateUtils.getYMD("2000-01-03"));
		os1.setName("市教委20151225");
		os1.setOrderNum(3);
		os1.setToDate(DateUtils.getYMD("2026-01-03"));
		os1.setTotalMoney(new BigDecimal(961));
		os1.setCustomerOrganization(customerOrganizationDao.getByAbbreviation("重大"));
		orderStatementDao.save(os1);
		
		OrderStatement os2 = new OrderStatement();
		os2.setDate(DateUtils.getYMD("2016-02-18"));
		os2.setFromDate(DateUtils.getYMD("2000-01-04"));
		os2.setName("市检察院20161225");
		os2.setOrderNum(2);
		os2.setToDate(DateUtils.getYMD("2026-01-04"));
		os2.setTotalMoney(new BigDecimal(705));
		os2.setCustomerOrganization(customerOrganizationDao.getByAbbreviation("重师"));
		orderStatementDao.save(os2);
		
		OrderStatement os3 = new OrderStatement();
		os3.setDate(DateUtils.getYMD("2016-03-21"));
		os3.setFromDate(DateUtils.getYMD("2000-01-05"));
		os3.setName("安监局20161225");
		os3.setOrderNum(2);
		os3.setToDate(DateUtils.getYMD("2026-01-05"));
		os3.setTotalMoney(new BigDecimal(339));
		os3.setCustomerOrganization(customerOrganizationDao.getByAbbreviation("市委"));
		orderStatementDao.save(os3);
		
		OrderStatement os4 = new OrderStatement();
		os4.setDate(DateUtils.getYMD("2016-04-19"));
		os4.setFromDate(DateUtils.getYMD("2000-01-06"));
		os4.setName("市政府20161225");
		os4.setOrderNum(5);
		os4.setToDate(DateUtils.getYMD("2026-01-06"));
		os4.setTotalMoney(new BigDecimal(428));
		os4.setCustomerOrganization(customerOrganizationDao.getByAbbreviation("重大"));
		orderStatementDao.save(os4);
		
		OrderStatement os5 = new OrderStatement();
		os5.setDate(DateUtils.getYMD("2016-05-23"));
		os5.setFromDate(DateUtils.getYMD("2000-01-07"));
		os5.setName("海关20161225");
		os5.setOrderNum(8);
		os5.setToDate(DateUtils.getYMD("2026-01-07"));
		os5.setTotalMoney(new BigDecimal(729));
		os5.setCustomerOrganization(customerOrganizationDao.getByAbbreviation("重师"));
		orderStatementDao.save(os5);
	}

	private void initOrder() {
		Order order1 = new Order();
		order1.setSn("16010001");
		order1.setCustomerOrganization(customerOrganizationDao
				.getByAbbreviation("重大"));
		order1.setCustomer(order1.getCustomerOrganization().getCustomers()
				.get(0));
		order1.setPhone(order1.getCustomer().getPhones().get(0));
		order1.setChargeMode(ChargeModeEnum.MILE);
		order1.setPlanBeginDate(DateUtils.getYMDHMS("2016-01-20 12:15:00"));
		order1.setPlanEndDate(null);
		order1.setServiceType(carServiceTypeDao.getById(1L));
		order1.setOrderMoney(new BigDecimal(5));
		order1.setQueueTime(DateUtils.getYMDHMS("2016-01-20 12:00:00"));
		order1.setMemo(null);
		order1.setStatus(OrderStatusEnum.INQUEUE);
		order1.setCallForOther(true);
		order1.setOtherPassengerName("张三");
		order1.setOtherPhoneNumber("13773100001");
		order1.setCallForOtherSendSMS(false);
		order1.setOrderSource(OrderSourceEnum.SCHEDULER);
		orderDao.save(order1);
		if(order1.getCustomer().getOtherPassengers()==null || order1.getCustomer().getOtherPassengers().size()==0)
			order1.getCustomer().setOtherPassengers(new ArrayList<OtherPassenger>(1));
		OtherPassenger otherPassenger=new OtherPassenger();
		otherPassenger.setCustomer(order1.getCustomer());
		System.out.println("order1.customer.name="+order1.getCustomer().getName());
		System.out.println("order1.customer.id="+order1.getCustomer().getId());
		otherPassenger.setName(order1.getOtherPassengerName());
		otherPassenger.setPhoneNumber(order1.getOtherPhoneNumber());
		otherPassenger.setDeleted(false);
		otherPassengerDao.save(otherPassenger);
		order1.getCustomer().getOtherPassengers().add(otherPassenger);
		customerDao.update(order1.getCustomer());

		Order order2 = new Order();
		order2.setSn("16010002");
		order2.setCustomerOrganization(customerOrganizationDao
				.getByAbbreviation("重大"));
		order2.setCustomer(order2.getCustomerOrganization().getCustomers()
				.get(1));
		order2.setPhone(order2.getCustomer().getPhones().get(0));
		order2.setChargeMode(ChargeModeEnum.DAY);
		order2.setPlanBeginDate(DateUtils.getYMDHMS("2016-01-21 00:00:00"));
		order2.setPlanEndDate(DateUtils.getYMDHMS("2016-01-23 00:00:00"));
		order2.setOrderMoney(new BigDecimal(600));
		order2.setServiceType(carServiceTypeDao.getById(1L));
		order2.setQueueTime(DateUtils.getYMDHMS("2016-01-20 12:15:00"));
		order2.setMemo(null);
		order2.setStatus(OrderStatusEnum.INQUEUE);
		order2.setCallForOther(false);
		order2.setOtherPassengerName("李四");
		order2.setOtherPhoneNumber("13773100002");
		order2.setCallForOtherSendSMS(false);
		order2.setOrderSource(OrderSourceEnum.SCHEDULER);
		orderDao.save(order2);
		if(order2.getCustomer().getOtherPassengers()==null || order2.getCustomer().getOtherPassengers().size()==0)
			order2.getCustomer().setOtherPassengers(new ArrayList<OtherPassenger>(1));
		otherPassenger=new OtherPassenger();
		otherPassenger.setCustomer(order2.getCustomer());
		System.out.println("order2.customer.name="+order2.getCustomer().getName());
		System.out.println("order2.customer.id="+order2.getCustomer().getId());
		otherPassenger.setName(order2.getOtherPassengerName());
		otherPassenger.setPhoneNumber(order2.getOtherPhoneNumber());
		otherPassenger.setDeleted(false);
		otherPassengerDao.save(otherPassenger);
		order2.getCustomer().getOtherPassengers().add(otherPassenger);
		customerDao.update(order2.getCustomer());

		Order order3 = new Order();
		order3.setSn("16010003");
		order3.setCustomerOrganization(customerOrganizationDao
				.getByAbbreviation("重师"));
		order3.setCustomer(order3.getCustomerOrganization().getCustomers()
				.get(0));
		order3.setPhone(order3.getCustomer().getPhones().get(0));
		order3.setChargeMode(ChargeModeEnum.MILE);
		order3.setPlanBeginDate(DateUtils.getYMDHMS("2016-01-20 12:45:00"));
		order3.setPlanEndDate(null);
		order3.setServiceType(carServiceTypeDao.getById(2L));
		order3.setOrderMoney(new BigDecimal(60));
		order3.setQueueTime(null);
		order3.setScheduleTime(DateUtils.getYMDHMS("2016-01-20 12:25:00"));
		order3.setCar(carDao.getById(6L));
		order3.setDriver(order3.getCar().getDriver());
		order3.setMemo(null);
		order3.setStatus(OrderStatusEnum.SCHEDULED);
		order3.setCallForOther(false);
		order3.setOrderSource(OrderSourceEnum.SCHEDULER);
		orderDao.save(order3);

		Order order4 = new Order();
		order4.setSn("16010004");
		order4.setCustomerOrganization(customerOrganizationDao
				.getByAbbreviation("重师"));
		order4.setCustomer(order4.getCustomerOrganization().getCustomers()
				.get(1));
		order4.setPhone(order4.getCustomer().getPhones().get(0));
		order4.setChargeMode(ChargeModeEnum.DAY);
		order4.setPlanBeginDate(DateUtils.getYMDHMS("2016-01-21 00:00:00"));
		order4.setPlanEndDate(DateUtils.getYMDHMS("2016-01-21 00:00:00"));
		order4.setOrderMoney(new BigDecimal(200));
		order4.setServiceType(carServiceTypeDao.getById(1L));
		order4.setQueueTime(DateUtils.getYMDHMS("2016-01-20 12:24:00"));
		order4.setScheduleTime(DateUtils.getYMDHMS("2016-01-20 12:25:00"));
		order4.setCar(carDao.getById(1L));
		order4.setDriver(order4.getCar().getDriver());
		order4.setMemo(null);
		order4.setStatus(OrderStatusEnum.SCHEDULED);
		order4.setCallForOther(false);
		order4.setOrderSource(OrderSourceEnum.SCHEDULER);
		orderDao.save(order4);

		Order order5 = new Order();
		order5.setSn("16010005");
		order5.setCustomerOrganization(customerOrganizationDao
				.getByAbbreviation("海关"));
		order5.setCustomer(order5.getCustomerOrganization().getCustomers()
				.get(0));
		order5.setPhone(order5.getCustomer().getPhones().get(0));
		order5.setChargeMode(ChargeModeEnum.MILE);
		order5.setPlanBeginDate(DateUtils.getYMDHMS("2016-01-20 13:00:00"));
		order5.setPlanEndDate(null);
		order5.setServiceType(carServiceTypeDao.getById(1L));
		order5.setOrderMoney(new BigDecimal(15));
		order5.setQueueTime(null);
		order5.setScheduleTime(DateUtils.getYMDHMS("2016-01-20 12:00:00"));
		order5.setAcceptedTime(DateUtils.getYMDHMS("2016-01-20 12:02:00"));
		order5.setCar(carDao.getById(4L));
		order5.setDriver(order5.getCar().getDriver());
		order5.setMemo(null);
		order5.setStatus(OrderStatusEnum.ACCEPTED);
		order5.setCallForOther(false);
		order5.setOrderSource(OrderSourceEnum.SCHEDULER);
		orderDao.save(order5);

		Order order6 = new Order();
		order6.setSn("16010006");
		order6.setCustomerOrganization(customerOrganizationDao
				.getByAbbreviation("海关"));
		order6.setCustomer(order6.getCustomerOrganization().getCustomers()
				.get(1));
		order6.setPhone(order6.getCustomer().getPhones().get(0));
		order6.setChargeMode(ChargeModeEnum.DAY);
		order6.setPlanBeginDate(DateUtils.getYMDHMS("2016-01-22 13:04:24"));
		order6.setPlanEndDate(DateUtils.getYMDHMS("2016-01-23 13:04:24"));
		order6.setOrderMoney(new BigDecimal(400));
		order6.setServiceType(carServiceTypeDao.getById(1L));
		order6.setQueueTime(DateUtils.getYMDHMS("2016-01-20 11:03:21"));
		order6.setScheduleTime(DateUtils.getYMDHMS("2016-01-20 12:04:00"));
		order6.setAcceptedTime(DateUtils.getYMDHMS("2016-01-20 12:08:00"));
		order6.setCar(carDao.getById(5L));
		order6.setDriver(order6.getCar().getDriver());
		order6.setMemo(null);
		order6.setStatus(OrderStatusEnum.ACCEPTED);
		order6.setCallForOther(false);
		order6.setOrderSource(OrderSourceEnum.SCHEDULER);
		orderDao.save(order6);

		Order order7 = new Order();
		order7.setSn("16010007");
		order7.setCustomerOrganization(customerOrganizationDao
				.getByAbbreviation("安监局"));
		order7.setCustomer(order7.getCustomerOrganization().getCustomers()
				.get(0));
		order7.setPhone(order7.getCustomer().getPhones().get(0));
		order7.setChargeMode(ChargeModeEnum.MILE);
		order7.setPlanBeginDate(DateUtils.getYMDHMS("2016-01-20 12:30:17"));
		order7.setPlanEndDate(null);
		order7.setServiceType(carServiceTypeDao.getById(1L));
		order7.setOrderMoney(new BigDecimal(30));
		order7.setQueueTime(DateUtils.getYMDHMS("2016-01-20 12:07:32"));
		order7.setScheduleTime(DateUtils.getYMDHMS("2016-01-20 12:00:00"));
		order7.setAcceptedTime(DateUtils.getYMDHMS("2016-01-20 12:02:00"));
		order7.setCar(carDao.getById(4L));
		order7.setDriver(order7.getCar().getDriver());
		order7.setMemo(null);
		order7.setStatus(OrderStatusEnum.BEGIN);
		order7.setCallForOther(false);
		order7.setOrderSource(OrderSourceEnum.SCHEDULER);
		orderDao.save(order7);

		Order order8 = new Order();
		order8.setSn("16010008");
		order8.setCustomerOrganization(customerOrganizationDao
				.getByAbbreviation("安监局"));
		order8.setCustomer(order8.getCustomerOrganization().getCustomers()
				.get(1));
		order8.setPhone(order8.getCustomer().getPhones().get(0));
		order8.setChargeMode(ChargeModeEnum.MILE);
		order8.setPlanBeginDate(DateUtils.getYMDHMS("2016-01-20 12:45:17"));
		order8.setPlanEndDate(null);
		order8.setServiceType(carServiceTypeDao.getById(1L));
		order8.setOrderMoney(new BigDecimal(45));
		order8.setQueueTime(DateUtils.getYMDHMS("2016-01-20 12:06:32"));
		order8.setScheduleTime(DateUtils.getYMDHMS("2016-01-20 12:08:00"));
		order8.setAcceptedTime(DateUtils.getYMDHMS("2016-01-20 12:22:00"));
		order8.setCar(carDao.getById(5L));
		order8.setDriver(order8.getCar().getDriver());
		order8.setMemo(null);
		order8.setStatus(OrderStatusEnum.BEGIN);
		order8.setCallForOther(false);
		order8.setOrderSource(OrderSourceEnum.SCHEDULER);
		orderDao.save(order8);

		Order order9 = new Order();
		order9.setSn("16010009");
		order9.setCustomerOrganization(customerOrganizationDao
				.getByAbbreviation("市委"));
		order9.setCustomer(order9.getCustomerOrganization().getCustomers()
				.get(0));
		order9.setPhone(order9.getCustomer().getPhones().get(0));
		order9.setChargeMode(ChargeModeEnum.MILE);
		order9.setPlanBeginDate(DateUtils.getYMDHMS("2016-01-19 13:10:00"));
		order9.setPlanEndDate(null);
		order9.setServiceType(carServiceTypeDao.getById(3L));
		order9.setOrderMoney(new BigDecimal(200));
		order9.setActualMoney(new BigDecimal(90));
		order9.setQueueTime(null);
		order9.setScheduleTime(DateUtils.getYMDHMS("2016-01-19 12:15:00"));
		order9.setAcceptedTime(DateUtils.getYMDHMS("2016-01-19 12:23:00"));
		order9.setCar(carDao.getById(7L));
		order9.setDriver(order9.getCar().getDriver());
		order9.setMemo(null);
		order9.setStatus(OrderStatusEnum.END);
		order9.setCallForOther(false);
		order9.setOrderSource(OrderSourceEnum.SCHEDULER);
		orderDao.save(order9);

		Order order10 = new Order();
		order10.setSn("16010010");
		order10.setCustomerOrganization(customerOrganizationDao
				.getByAbbreviation("市委"));
		order10.setCustomer(order10.getCustomerOrganization().getCustomers()
				.get(1));
		order10.setPhone(order10.getCustomer().getPhones().get(0));
		order10.setChargeMode(ChargeModeEnum.DAY);
		order10.setPlanBeginDate(DateUtils.getYMDHMS("2016-01-16 13:10:00"));
		order10.setPlanEndDate(DateUtils.getYMDHMS("2016-01-19 13:10:00"));
		order10.setServiceType(carServiceTypeDao.getById(1L));
		order10.setOrderMoney(new BigDecimal(1600));
		order10.setActualMoney(new BigDecimal(1600));
		order10.setQueueTime(null);
		order10.setScheduleTime(DateUtils.getYMDHMS("2016-01-16 18:15:00"));
		order10.setAcceptedTime(DateUtils.getYMDHMS("2016-01-16 19:21:11"));
		order10.setCar(carDao.getById(8L));
		order10.setDriver(order10.getCar().getDriver());
		order10.setMemo(null);
		order10.setStatus(OrderStatusEnum.END);
		order10.setCallForOther(false);
		order10.setOrderSource(OrderSourceEnum.SCHEDULER);
		orderDao.save(order10);

		Order order11 = new Order();
		order11.setSn("16010011");
		order11.setCustomerOrganization(customerOrganizationDao
				.getByAbbreviation("市府"));
		order11.setCustomer(order11.getCustomerOrganization().getCustomers()
				.get(0));
		order11.setPhone(order11.getCustomer().getPhones().get(0));
		order11.setChargeMode(ChargeModeEnum.MILE);
		order11.setPlanBeginDate(DateUtils.getYMDHMS("2016-01-18 15:20:00"));
		order11.setPlanEndDate(null);
		order11.setServiceType(carServiceTypeDao.getById(1L));
		order11.setOrderMoney(new BigDecimal(15));
		order11.setActualMoney(new BigDecimal(9));
		order11.setQueueTime(null);
		order11.setScheduleTime(DateUtils.getYMDHMS("2016-01-18 13:11:47"));
		order11.setAcceptedTime(DateUtils.getYMDHMS("2016-01-18 13:15:29"));
		order11.setCar(carDao.getById(7L));
		order11.setDriver(order11.getCar().getDriver());
		order11.setMemo(null);
		order11.setStatus(OrderStatusEnum.PAYED);
		order11.setCallForOther(false);
		order11.setOrderSource(OrderSourceEnum.SCHEDULER);
		orderDao.save(order11);

		Order order12 = new Order();
		order12.setSn("16010012");
		order12.setCustomerOrganization(customerOrganizationDao
				.getByAbbreviation("市府"));
		order12.setCustomer(order12.getCustomerOrganization().getCustomers()
				.get(1));
		order12.setPhone(order12.getCustomer().getPhones().get(0));
		order12.setChargeMode(ChargeModeEnum.MILE);
		order12.setPlanBeginDate(DateUtils.getYMDHMS("2016-01-17 16:15:00"));
		order12.setPlanEndDate(null);
		order12.setServiceType(carServiceTypeDao.getById(1L));
		order12.setOrderMoney(new BigDecimal(45));
		order12.setActualMoney(new BigDecimal(27));
		order12.setQueueTime(null);
		order12.setScheduleTime(DateUtils.getYMDHMS("2016-01-17 15:57:47"));
		order12.setAcceptedTime(DateUtils.getYMDHMS("2016-01-17 15:59:29"));
		order12.setCar(carDao.getById(8L));
		order12.setDriver(order12.getCar().getDriver());
		order12.setMemo(null);
		order12.setStatus(OrderStatusEnum.PAYED);
		order12.setCallForOther(false);
		order12.setOrderSource(OrderSourceEnum.SCHEDULER);
		orderDao.save(order12);
	}

	private void initCustomer() {
		PriceTable priceTable=priceTableDao.getPriceTableByTitle("价格表");
		
		CustomerOrganization co1 = new CustomerOrganization();
		co1.setName("重庆师范大学");
		co1.setAbbreviation("重师");
		co1.setPriceTable(priceTable);
		customerOrganizationDao.save(co1);

		CustomerOrganization co2 = new CustomerOrganization();
		co2.setName("重庆大学");
		co2.setAbbreviation("重大");
		co2.setPriceTable(priceTable);
		customerOrganizationDao.save(co2);

		CustomerOrganization co3 = new CustomerOrganization();
		co3.setName("重庆海关");
		co3.setAbbreviation("海关");
		co3.setPriceTable(priceTable);
		customerOrganizationDao.save(co3);

		CustomerOrganization co4 = new CustomerOrganization();
		co4.setName("重庆市安全监察局");
		co4.setAbbreviation("安监局");
		co4.setPriceTable(priceTable);
		customerOrganizationDao.save(co4);

		CustomerOrganization co5 = new CustomerOrganization();
		co5.setName("重庆市人民政府");
		co5.setAbbreviation("市府");
		co5.setPriceTable(priceTable);
		customerOrganizationDao.save(co5);

		CustomerOrganization co6 = new CustomerOrganization();
		co6.setName("中共重庆市委员会");
		co6.setAbbreviation("市委");
		co6.setPriceTable(priceTable);
		customerOrganizationDao.save(co6);

		Customer c1 = new Customer();
		c1.setName("客户1");
		c1.setGender(true);
		c1.setCustomerOrganization(customerOrganizationDao
				.getByAbbreviation("重大"));
		List<String> phoneList1 = new ArrayList<String>();
		phoneList1.add("13883100001");
		phoneList1.add("65100001");
		c1.setPhones(phoneList1);
		customerDao.save(c1);

		Customer c2 = new Customer();
		c2.setName("客户2");
		c2.setGender(true);
		c2.setCustomerOrganization(customerOrganizationDao
				.getByAbbreviation("重大"));
		List<String> phoneList2 = new ArrayList<String>();
		phoneList2.add("13883100002");
		c2.setPhones(phoneList2);
		customerDao.save(c2);

		Customer c3 = new Customer();
		c3.setName("客户3");
		c3.setGender(true);
		c3.setCustomerOrganization(customerOrganizationDao
				.getByAbbreviation("重师"));
		List<String> phoneList3 = new ArrayList<String>();
		phoneList3.add("13883100003");
		phoneList3.add("65100003");
		c3.setPhones(phoneList3);
		customerDao.save(c3);

		Customer c4 = new Customer();
		c4.setName("客户4");
		c4.setGender(true);
		c4.setCustomerOrganization(customerOrganizationDao
				.getByAbbreviation("重师"));
		List<String> phoneList4 = new ArrayList<String>();
		phoneList4.add("13883100004");
		c4.setPhones(phoneList4);
		customerDao.save(c4);

		Customer c5 = new Customer();
		c5.setName("客户5");
		c5.setGender(true);
		c5.setCustomerOrganization(customerOrganizationDao
				.getByAbbreviation("海关"));
		List<String> phoneList5 = new ArrayList<String>();
		phoneList5.add("13883100005");
		phoneList5.add("65100005");
		c5.setPhones(phoneList5);
		customerDao.save(c5);

		Customer c6 = new Customer();
		c6.setName("客户6");
		c6.setGender(true);
		c6.setCustomerOrganization(customerOrganizationDao
				.getByAbbreviation("海关"));
		List<String> phoneList6 = new ArrayList<String>();
		phoneList6.add("13883100006");
		c6.setPhones(phoneList6);
		customerDao.save(c6);

		Customer c7 = new Customer();
		c7.setName("客户7");
		c7.setGender(true);
		c7.setCustomerOrganization(customerOrganizationDao
				.getByAbbreviation("安监局"));
		List<String> phoneList7 = new ArrayList<String>();
		phoneList7.add("13883100007");
		phoneList7.add("65100007");
		c7.setPhones(phoneList7);
		customerDao.save(c7);

		Customer c8 = new Customer();
		c8.setName("客户8");
		c8.setGender(true);
		c8.setCustomerOrganization(customerOrganizationDao
				.getByAbbreviation("安监局"));
		List<String> phoneList8 = new ArrayList<String>();
		phoneList8.add("13883100008");
		c8.setPhones(phoneList8);
		customerDao.save(c8);

		Customer c9 = new Customer();
		c9.setName("客户9");
		c9.setGender(true);
		c9.setCustomerOrganization(customerOrganizationDao
				.getByAbbreviation("市委"));
		List<String> phoneList9 = new ArrayList<String>();
		phoneList9.add("13883100009");
		phoneList9.add("65100009");
		c9.setPhones(phoneList9);
		customerDao.save(c9);

		Customer c10 = new Customer();
		c10.setName("客户10");
		c10.setGender(true);
		c10.setCustomerOrganization(customerOrganizationDao
				.getByAbbreviation("市委"));
		List<String> phoneList10 = new ArrayList<String>();
		phoneList10.add("13883100010");
		c10.setPhones(phoneList10);
		customerDao.save(c10);

		Customer c11 = new Customer();
		c11.setName("客户11");
		c11.setGender(true);
		c11.setCustomerOrganization(customerOrganizationDao
				.getByAbbreviation("市府"));
		List<String> phoneList11 = new ArrayList<String>();
		phoneList11.add("13883100011");
		phoneList11.add("65100011");
		c11.setPhones(phoneList11);
		customerDao.save(c11);

		Customer c12 = new Customer();
		c12.setName("客户12");
		c12.setGender(true);
		c12.setCustomerOrganization(customerOrganizationDao
				.getByAbbreviation("市府"));
		List<String> phoneList12 = new ArrayList<String>();
		phoneList12.add("13883100012");
		c12.setPhones(phoneList12);
		customerDao.save(c12);

		List<Customer> cl1 = new ArrayList<Customer>();
		cl1.add(c1);
		cl1.add(c2);
		co2.setCustomers(cl1);
		customerOrganizationDao.update(co2);

		List<Customer> cl2 = new ArrayList<Customer>();
		cl2.add(c3);
		cl2.add(c4);
		co1.setCustomers(cl2);
		customerOrganizationDao.update(co1);

		List<Customer> cl3 = new ArrayList<Customer>();
		cl3.add(c5);
		cl3.add(c6);
		co3.setCustomers(cl3);
		customerOrganizationDao.update(co3);

		List<Customer> cl4 = new ArrayList<Customer>();
		cl4.add(c7);
		cl4.add(c8);
		co4.setCustomers(cl4);
		customerOrganizationDao.update(co4);

		List<Customer> cl5 = new ArrayList<Customer>();
		cl5.add(c9);
		cl5.add(c10);
		co5.setCustomers(cl5);
		customerOrganizationDao.update(co5);

		List<Customer> cl6 = new ArrayList<Customer>();
		cl6.add(c11);
		cl6.add(c12);
		co6.setCustomers(cl6);
		customerOrganizationDao.update(co6);
	}

	private void initDevice() {
		Device d1 = new Device();
		d1.setSN("892620160125106");
		d1.setPN("DPN00001");
		d1.setDate(new Date());
		d1.setManufacturer("跟屁虫");
		deviceDao.save(d1);

		Device d2 = new Device();
		d2.setSN("354188046608662");
		d2.setPN("DPN00002");
		d2.setDate(new Date());
		d2.setManufacturer("跟屁虫");
		deviceDao.save(d2);

		Device d3 = new Device();
		d3.setSN("354188048138338");
		d3.setPN("DPN00003");
		d3.setDate(new Date());
		d3.setManufacturer("跟屁虫");
		deviceDao.save(d3);

		Device d4 = new Device();
		d4.setSN("354188046608662");
		d4.setPN("DPN00004");
		d4.setDate(new Date());
		d4.setManufacturer("跟屁虫");
		deviceDao.save(d4);

		Device d5 = new Device();
		d5.setSN("354188046608662");
		d5.setPN("DPN00005");
		d5.setDate(new Date());
		d5.setManufacturer("跟屁虫");
		deviceDao.save(d5);

		Device d6 = new Device();
		d6.setSN("354188046608662");
		d6.setPN("DPN00006");
		d6.setDate(new Date());
		d6.setManufacturer("跟屁虫");
		deviceDao.save(d6);

		Device d7 = new Device();
		d7.setSN("354188048138338");
		d7.setPN("DPN00007");
		d7.setDate(new Date());
		d7.setManufacturer("跟屁虫");
		deviceDao.save(d7);

		Device d8 = new Device();
		d8.setSN("354188048138338");
		d8.setPN("DPN00008");
		d8.setDate(new Date());
		d8.setManufacturer("跟屁虫");
		deviceDao.save(d8);
	}

	private void initCar() {
		ServicePoint sp1 = new ServicePoint();
		sp1.setName("重庆大学业务点");
		servicePointDao.save(sp1);

		ServicePoint sp2 = new ServicePoint();
		sp2.setName("龙头寺业务点");
		servicePointDao.save(sp2);

		ServicePoint sp3 = new ServicePoint();
		sp3.setName("解放碑业务点");
		servicePointDao.save(sp3);
		
		Car car1 = new Car();
		car1.setPlateNumber("渝A00001");
		car1.setBrand("大众");
		car1.setModel("帕萨特");
		car1.setVIN("SVN000001");
		car1.setEngineSN("ESN00001");
		car1.setServiceType(carServiceTypeDao.getCarServiceTypeByTitle("大众帕萨特"));
		car1.setDriver(userService.getByLoginName("司机1"));
		car1.setServicePoint(sp1);
		car1.setDevice(deviceDao.getById(1L));
		car1.setMileage(80000);
		car1.setRegistDate(new Date());
		car1.setMemo("车辆1的备注");
		car1.setNextCareMile(87500);
		car1.setInsuranceExpiredDate(DateUtils.getYMD("2017-01-20"));
		car1.setNextExaminateDate(DateUtils.getYMD("2018-01-20"));
		car1.setStatus(CarStatusEnum.NORMAL);
		car1.setStandbyCar(false);
		car1.setEnrollDate(new Date());
		car1.setPlateType(PlateTypeEnum.BLUE);
		car1.setSeatNumber(5);
		car1.setTransmissionType(TransmissionTypeEnum.AUTO);
		carDao.save(car1);

		Car car2 = new Car();
		car2.setPlateNumber("渝A00002");
		car2.setBrand("大众");
		car2.setModel("迈腾");
		car2.setVIN("SVN000002");
		car2.setEngineSN("ESN00002");
		car2.setServiceType(carServiceTypeDao.getCarServiceTypeByTitle("大众迈腾"));
		car2.setDriver(userService.getByLoginName("司机2"));
		car2.setServicePoint(sp1);
		car2.setDevice(deviceDao.getById(2L));
		car2.setMileage(90000);
		car2.setRegistDate(new Date());
		car2.setMemo("车辆2的备注");
		car2.setNextCareMile(97500);
		car2.setInsuranceExpiredDate(DateUtils.getYMD("2017-02-20"));
		car2.setNextExaminateDate(DateUtils.getYMD("2018-02-20"));
		car2.setStatus(CarStatusEnum.NORMAL);
		car2.setStandbyCar(false);
		car2.setEnrollDate(new Date());
		car2.setPlateType(PlateTypeEnum.BLUE);
		car2.setSeatNumber(5);
		car2.setTransmissionType(TransmissionTypeEnum.AUTO);
		carDao.save(car2);

		Car car3 = new Car();
		car3.setPlateNumber("渝A00003");
		car3.setBrand("福特");
		car3.setModel("蒙迪欧");
		car3.setVIN("SVN000003");
		car3.setEngineSN("ESN00003");
		car3.setServiceType(carServiceTypeDao.getCarServiceTypeByTitle("蒙迪欧致胜"));
		car3.setDriver(userService.getByLoginName("司机2"));
		car3.setServicePoint(sp1);
		car3.setDevice(deviceDao.getById(3L));
		car3.setMileage(100000);
		car3.setRegistDate(new Date());
		car3.setMemo("车辆3的备注");
		car3.setNextCareMile(107500);
		car3.setInsuranceExpiredDate(DateUtils.getYMD("2017-03-20"));
		car3.setNextExaminateDate(DateUtils.getYMD("2018-03-20"));
		car3.setStatus(CarStatusEnum.NORMAL);
		car3.setStandbyCar(false);
		car3.setEnrollDate(new Date());
		car3.setPlateType(PlateTypeEnum.BLUE);
		car3.setSeatNumber(5);
		car3.setTransmissionType(TransmissionTypeEnum.AUTO);
		carDao.save(car3);

		Car car4 = new Car();
		car4.setPlateNumber("渝A00004");
		car4.setBrand("沃尔沃");
		car4.setModel("S80");
		car4.setVIN("SVN000004");
		car4.setEngineSN("ESN00004");
		car4.setServiceType(carServiceTypeDao.getCarServiceTypeByTitle("沃尔沃S80/2.5"));
		car4.setDriver(userService.getByLoginName("司机4"));
		car4.setServicePoint(sp2);
		car4.setDevice(deviceDao.getById(4L));
		car4.setMileage(110000);
		car4.setRegistDate(new Date());
		car4.setMemo("车辆4的备注");
		car4.setNextCareMile(117500);
		car4.setInsuranceExpiredDate(DateUtils.getYMD("2017-04-20"));
		car4.setNextExaminateDate(DateUtils.getYMD("2018-04-20"));
		car4.setStatus(CarStatusEnum.NORMAL);
		car4.setStandbyCar(false);
		car4.setEnrollDate(new Date());
		car4.setPlateType(PlateTypeEnum.BLUE);
		car4.setSeatNumber(5);
		car4.setTransmissionType(TransmissionTypeEnum.AUTO);
		carDao.save(car4);

		Car car5 = new Car();
		car5.setPlateNumber("渝A00005");
		car5.setBrand("日产");
		car5.setModel("凯美瑞");
		car5.setVIN("SVN000005");
		car5.setEngineSN("ESN00005");
		car5.setServiceType(carServiceTypeDao.getCarServiceTypeByTitle("凯美瑞"));
		car5.setServicePoint(sp2);
		car5.setDevice(deviceDao.getById(5L));
		car5.setMileage(120000);
		car5.setRegistDate(new Date());
		car5.setMemo("车辆5的备注");
		car5.setNextCareMile(127500);
		car5.setInsuranceExpiredDate(DateUtils.getYMD("2017-05-20"));
		car5.setNextExaminateDate(DateUtils.getYMD("2018-05-20"));
		car5.setStatus(CarStatusEnum.NORMAL);
		car5.setStandbyCar(true);
		car5.setEnrollDate(new Date());
		car5.setPlateType(PlateTypeEnum.BLUE);
		car5.setSeatNumber(5);
		car5.setTransmissionType(TransmissionTypeEnum.AUTO);
		carDao.save(car5);

		Car car6 = new Car();
		car6.setPlateNumber("渝A00006");
		car6.setBrand("别克");
		car6.setModel("GL8");
		car6.setVIN("SVN000006");
		car6.setEngineSN("ESN00006");
		car6.setServiceType(carServiceTypeDao.getCarServiceTypeByTitle("商务别克"));
		car6.setDriver(userService.getByLoginName("司机6"));
		car6.setServicePoint(sp2);
		car6.setDevice(deviceDao.getById(6L));
		car6.setMileage(130000);
		car6.setRegistDate(new Date());
		car6.setMemo("车辆6的备注");
		car6.setNextCareMile(137500);
		car6.setInsuranceExpiredDate(DateUtils.getYMD("2017-06-20"));
		car6.setNextExaminateDate(DateUtils.getYMD("2018-06-20"));
		car6.setStatus(CarStatusEnum.NORMAL);
		car6.setStandbyCar(false);
		car6.setEnrollDate(new Date());
		car6.setPlateType(PlateTypeEnum.BLUE);
		car6.setSeatNumber(5);
		car6.setTransmissionType(TransmissionTypeEnum.AUTO);
		carDao.save(car6);

		Car car7 = new Car();
		car7.setPlateNumber("渝A00007");
		car7.setBrand("别克");
		car7.setModel("君威");
		car7.setVIN("SVN000007");
		car7.setEngineSN("ESN00007");
		car7.setServiceType(carServiceTypeDao.getCarServiceTypeByTitle("别克"));
		car7.setDriver(userService.getByLoginName("司机7"));
		car7.setServicePoint(sp3);
		car7.setDevice(deviceDao.getById(7L));
		car7.setMileage(140000);
		car7.setRegistDate(new Date());
		car7.setMemo("车辆7的备注");
		car7.setNextCareMile(147500);
		car7.setInsuranceExpiredDate(DateUtils.getYMD("2017-07-20"));
		car7.setNextExaminateDate(DateUtils.getYMD("2018-07-20"));
		car7.setStatus(CarStatusEnum.NORMAL);
		car7.setStandbyCar(false);
		car7.setEnrollDate(new Date());
		car7.setPlateType(PlateTypeEnum.BLUE);
		car7.setSeatNumber(5);
		car7.setTransmissionType(TransmissionTypeEnum.AUTO);
		carDao.save(car7);

		Car car8 = new Car();
		car8.setPlateNumber("渝A00008");
		car8.setBrand("丰田");
		car8.setModel("RAV4");
		car8.setVIN("SVN000008");
		car8.setEngineSN("ESN00008");
		car8.setServiceType(carServiceTypeDao.getCarServiceTypeByTitle("丰田RAV4、CRV越野"));
		car8.setDriver(userService.getByLoginName("司机8"));
		car8.setServicePoint(sp3);
		car8.setDevice(deviceDao.getById(8L));
		car8.setMileage(150000);
		car8.setRegistDate(new Date());
		car8.setMemo("车辆8的备注");
		car8.setNextCareMile(157500);
		car8.setInsuranceExpiredDate(DateUtils.getYMD("2017-08-20"));
		car8.setNextExaminateDate(DateUtils.getYMD("2018-08-20"));
		car8.setStatus(CarStatusEnum.NORMAL);
		car8.setStandbyCar(false);
		car8.setEnrollDate(new Date());
		car8.setPlateType(PlateTypeEnum.BLUE);
		car8.setSeatNumber(5);
		car8.setTransmissionType(TransmissionTypeEnum.AUTO);
		carDao.save(car8);

		Car car9 = new Car();
		car9.setPlateNumber("渝A00009");
		car9.setBrand("奥迪");
		car9.setModel("A6");
		car9.setVIN("SVN000009");
		car9.setEngineSN("ESN00009");
		car9.setServiceType(carServiceTypeDao.getCarServiceTypeByTitle("2.4奥迪"));
		car9.setDriver(userService.getByLoginName("司机8"));
		car9.setServicePoint(sp3);
		car9.setDevice(deviceDao.getById(8L));
		car9.setMileage(150000);
		car9.setRegistDate(new Date());
		car9.setMemo("车辆9的备注");
		car9.setNextCareMile(157500);
		car9.setInsuranceExpiredDate(DateUtils.getYMD("2017-09-20"));
		car9.setNextExaminateDate(DateUtils.getYMD("2018-09-20"));
		car9.setStatus(CarStatusEnum.SCRAPPED);// 这辆车是报废了的。
		car9.setStandbyCar(false);
		car9.setEnrollDate(new Date());
		car9.setPlateType(PlateTypeEnum.BLUE);
		car9.setSeatNumber(5);
		car9.setTransmissionType(TransmissionTypeEnum.AUTO);
		carDao.save(car9);
	}

	private void initDriver() {
		Department business = new Department();
		business.setName("业务部");
		departmentDao.save(business);

		User user1 = new User();
		user1.setDepartment(business);
		user1.setLoginName("司机1");
		user1.setPassword(DigestUtils.md5Hex("123456"));
		user1.setName("司机1");
		user1.setPhoneNumber("13883101475");
		user1.setUserType(UserTypeEnum.DRIVER);
		user1.setStatus(UserStatusEnum.NORMAL);
		DriverLicense dl1 = new DriverLicense();
		dl1.setLicenseID("1000001");
		dl1.setExpireDate(DateUtils.getYMD("2020-01-01"));
		driverLicenseDao.save(dl1);
		user1.setDriverLicense(dl1);
		userDao.save(user1);

		User user2 = new User();
		user2.setDepartment(business);
		user2.setLoginName("司机2");
		user2.setPassword(DigestUtils.md5Hex("123456"));
		user2.setName("司机2");
		user2.setPhoneNumber("13883100002");
		user2.setUserType(UserTypeEnum.DRIVER);
		user2.setStatus(UserStatusEnum.NORMAL);
		DriverLicense dl2 = new DriverLicense();
		dl2.setLicenseID("1000002");
		dl2.setExpireDate(DateUtils.getYMD("2020-02-01"));
		driverLicenseDao.save(dl2);
		user2.setDriverLicense(dl2);
		userDao.save(user2);

		User user3 = new User();
		user3.setDepartment(business);
		user3.setLoginName("司机3");
		user3.setPassword(DigestUtils.md5Hex("123456"));
		user3.setName("司机3");
		user3.setPhoneNumber("13883100003");
		user3.setUserType(UserTypeEnum.DRIVER);
		user3.setStatus(UserStatusEnum.NORMAL);
		DriverLicense dl3 = new DriverLicense();
		dl3.setLicenseID("1000003");
		dl3.setExpireDate(DateUtils.getYMD("2020-03-01"));
		driverLicenseDao.save(dl3);
		user3.setDriverLicense(dl3);
		userDao.save(user3);

		User user4 = new User();
		user4.setDepartment(business);
		user4.setLoginName("司机4");
		user4.setPassword(DigestUtils.md5Hex("123456"));
		user4.setName("司机4");
		user4.setPhoneNumber("13883100004");
		user4.setUserType(UserTypeEnum.DRIVER);
		user4.setStatus(UserStatusEnum.NORMAL);
		DriverLicense dl4 = new DriverLicense();
		dl4.setLicenseID("1000004");
		dl4.setExpireDate(DateUtils.getYMD("2020-04-01"));
		driverLicenseDao.save(dl4);
		user4.setDriverLicense(dl4);
		userDao.save(user4);

		User user5 = new User();
		user5.setDepartment(business);
		user5.setLoginName("司机5");
		user5.setPassword(DigestUtils.md5Hex("123456"));
		user5.setName("司机5");
		user5.setPhoneNumber("13883100005");
		user5.setUserType(UserTypeEnum.DRIVER);
		user5.setStatus(UserStatusEnum.NORMAL);
		DriverLicense dl5 = new DriverLicense();
		dl5.setLicenseID("1000005");
		dl5.setExpireDate(DateUtils.getYMD("2020-05-01"));
		driverLicenseDao.save(dl5);
		user5.setDriverLicense(dl5);
		userDao.save(user5);

		User user6 = new User();
		user6.setDepartment(business);
		user6.setLoginName("司机6");
		user6.setPassword(DigestUtils.md5Hex("123456"));
		user6.setName("司机6");
		user6.setPhoneNumber("13883100006");
		user6.setUserType(UserTypeEnum.DRIVER);
		user6.setStatus(UserStatusEnum.NORMAL);
		DriverLicense dl6 = new DriverLicense();
		dl6.setLicenseID("1000006");
		dl6.setExpireDate(DateUtils.getYMD("2020-06-01"));
		driverLicenseDao.save(dl6);
		user6.setDriverLicense(dl6);
		userDao.save(user6);

		User user7 = new User();
		user7.setDepartment(business);
		user7.setLoginName("司机7");
		user7.setPassword(DigestUtils.md5Hex("123456"));
		user7.setName("司机7");
		user7.setPhoneNumber("13883100007");
		user7.setUserType(UserTypeEnum.DRIVER);
		user7.setStatus(UserStatusEnum.NORMAL);
		DriverLicense dl7 = new DriverLicense();
		dl7.setLicenseID("1000007");
		dl7.setExpireDate(DateUtils.getYMD("2020-07-01"));
		driverLicenseDao.save(dl7);
		user7.setDriverLicense(dl7);
		userDao.save(user7);

		User user8 = new User();
		user8.setDepartment(business);
		user8.setLoginName("司机8");
		user8.setPassword(DigestUtils.md5Hex("123456"));
		user8.setName("司机8");
		user8.setPhoneNumber("13883100008");
		user8.setUserType(UserTypeEnum.DRIVER);
		user8.setStatus(UserStatusEnum.NORMAL);
		DriverLicense dl8 = new DriverLicense();
		dl8.setLicenseID("1000008");
		dl8.setExpireDate(DateUtils.getYMD("2020-08-01"));
		driverLicenseDao.save(dl8);
		user8.setDriverLicense(dl8);
		userDao.save(user8);
	}
	
	private void initUser(){
		Department business = new Department();
		business.setName("管理部门");
		departmentDao.save(business);
		
		User user1=new User();
		user1.setDepartment(business);
		user1.setLoginName("调度员1");
		user1.setName("调度员1");
		user1.setPhoneNumber("12345678901");
		user1.setPassword(DigestUtils.md5Hex("123456"));
		user1.setStatus(UserStatusEnum.NORMAL);
		user1.setUserType(UserTypeEnum.OFFICE);
		user1.setRoles(new HashSet<Role>());
		user1.getRoles().add(roleService.getRoleByName("调度员"));
		userDao.save(user1);		

		User user2=new User();
		user2.setDepartment(business);
		user2.setLoginName("调度员2");
		user2.setName("调度员2");
		user2.setPhoneNumber("12345678902");
		user2.setPassword(DigestUtils.md5Hex("123456"));
		user2.setStatus(UserStatusEnum.NORMAL);
		user2.setUserType(UserTypeEnum.OFFICE);
		user2.setRoles(new HashSet<Role>());
		user2.getRoles().add(roleService.getRoleByName("调度员"));
		user2.getRoles().add(roleService.getRoleByName("高级调度员"));
		userDao.save(user2);
		
		User user4=new User();
		user4.setDepartment(business);
		user4.setLoginName("监控员1");
		user4.setName("监控员1");
		user4.setPhoneNumber("18875208072");
		user4.setPassword(DigestUtils.md5Hex("123456"));
		user4.setStatus(UserStatusEnum.NORMAL);
		user4.setUserType(UserTypeEnum.OFFICE);
		user4.setRoles(new HashSet<Role>());
		user4.getRoles().add(roleService.getRoleByName("监控员"));
		userDao.save(user4);
		
		User user5=new User();
		user5.setDepartment(business);
		user5.setLoginName("财务人员1");
		user5.setName("财务人员1");
		user5.setPhoneNumber("18875208073");
		user5.setPassword(DigestUtils.md5Hex("123456"));
		user5.setStatus(UserStatusEnum.NORMAL);
		user5.setUserType(UserTypeEnum.OFFICE);
		user5.setRoles(new HashSet<Role>());
		user5.getRoles().add(roleService.getRoleByName("财务人员"));
		userDao.save(user5);
		
		User user3=new User();
		user3.setDepartment(business);
		user3.setLoginName("车辆管理员1");
		user3.setName("车辆管理员1");
		user3.setPhoneNumber("11111111");
		user3.setPassword(DigestUtils.md5Hex("123456"));
		user3.setStatus(UserStatusEnum.NORMAL);
		user3.setUserType(UserTypeEnum.OFFICE);
		user3.setRoles(new HashSet<Role>());
		user3.getRoles().add(roleService.getRoleByName("车辆管理员"));
		userDao.save(user3);
		
		User user6 = new User();
		user6.setDepartment(business);
		user6.setLoginName("客户管理员1");
		user6.setName("客户管理员1");
		user6.setPhoneNumber("12345678903");
		user6.setPassword(DigestUtils.md5Hex("123456"));
		user6.setStatus(UserStatusEnum.NORMAL);
		user6.setUserType(UserTypeEnum.OFFICE);
		user6.setRoles(new HashSet<Role>());
		user6.getRoles().add(roleService.getRoleByName("客户管理员"));
		userDao.save(user6);
		
		User user7 = new User();
		user7.setDepartment(business);
		user7.setLoginName("公司领导1");
		user7.setName("公司领导1");
		user7.setPhoneNumber("12345678904");
		user7.setPassword(DigestUtils.md5Hex("123456"));
		user7.setStatus(UserStatusEnum.NORMAL);
		user7.setUserType(UserTypeEnum.OFFICE);
		user7.setRoles(new HashSet<Role>());
		user7.getRoles().add(roleService.getRoleByName("公司领导"));
		userDao.save(user7);
	}

	public static void main(String[] args) {
		System.out.println("正在导入初始化数据...");

		// 从Spring容器中取出对象
		ApplicationContext ac = new ClassPathXmlApplicationContext(
				"classpath:applicationContext.xml");
		InitTestData init = (InitTestData) ac.getBean("initTestData");
		// 执行初始化
		init.install();

		System.out.println("导入初始化数据完毕！");
	}

}
