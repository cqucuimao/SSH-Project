package com.yuqincar.service.order.impl;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.order.ReserveCarApplyOrderDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.ReserveCarApplyOrder;
import com.yuqincar.domain.order.ReserveCarApplyOrderStatusEnum;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.businessParameter.BusinessParameterService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.order.ReserveCarApplyOrderService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.service.sms.SMSService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Service
public class ReserveCarApplyOrderServiceImpl implements ReserveCarApplyOrderService {
	@Autowired
	private ReserveCarApplyOrderDao reserveCarApplyOrderDao;
	@Autowired
	private UserService userService;
	@Autowired
	private SMSService smsService;
	@Autowired
	private CarService carService;	
	@Autowired
	private BusinessParameterService businessParameterService;
	

	public boolean canDelete(ReserveCarApplyOrder rcao,User user){
		if(rcao.getProposer().equals(user) && (rcao.getStatus()==ReserveCarApplyOrderStatusEnum.NEW
				|| rcao.getStatus()==ReserveCarApplyOrderStatusEnum.REJECTED))
			return true;
		else
			return false;
	}
	
	public boolean canUpdate(ReserveCarApplyOrder rcao,User user){
		if(rcao.getProposer().equals(user) && rcao.getStatus()==ReserveCarApplyOrderStatusEnum.NEW)
			return true;
		else
			return false;
	}
	
	public boolean canApprove(ReserveCarApplyOrder rcao,User user){
		if(rcao.getApproveUser().equals(user) && rcao.getStatus()==ReserveCarApplyOrderStatusEnum.SUBMITTED)
			return true;
		else
			return false;
	}
	
	public boolean canConfigCar(ReserveCarApplyOrder rcao,User user){
		if(rcao.getCarApproveUser().equals(user) && rcao.getStatus()==ReserveCarApplyOrderStatusEnum.APPROVED && !rcao.isCarApproved())
			return true;
		else
			return false;
	}
	
	public boolean canConfigDriver(ReserveCarApplyOrder rcao,User user){
		if(rcao.getDriverApproveUser().equals(user) && rcao.getStatus()==ReserveCarApplyOrderStatusEnum.APPROVED && !rcao.isDriverApproved())
			return true;
		else
			return false;
	}
	
	@Transactional
	public void save(ReserveCarApplyOrder reserveCarApplyOrder){
		reserveCarApplyOrder.setStatus(ReserveCarApplyOrderStatusEnum.NEW);
		reserveCarApplyOrder.setNewTime(new Date());
		reserveCarApplyOrderDao.save(reserveCarApplyOrder);
	}
	
	@Transactional
	public void update(ReserveCarApplyOrder reserveCarApplyOrder){
		reserveCarApplyOrderDao.update(reserveCarApplyOrder);
	}
	
	@Transactional
	public void saveAndSubmit(ReserveCarApplyOrder reserveCarApplyOrder){
		reserveCarApplyOrder.setStatus(ReserveCarApplyOrderStatusEnum.SUBMITTED);
		reserveCarApplyOrder.setNewTime(new Date());
		reserveCarApplyOrder.setSubmittedTime(new Date());
		reserveCarApplyOrder.setApproveUser(businessParameterService.getBusinessParameter().getReserveCarApplyOrderApproveUser().get(0));
		reserveCarApplyOrder.setCarApproveUser(businessParameterService.getBusinessParameter().getReserveCarApplyOrderCarApproveUser().get(0));
		reserveCarApplyOrder.setDriverApproveUser(businessParameterService.getBusinessParameter().getReserveCarApplyOrderDriverApproveUser().get(0));
		reserveCarApplyOrderDao.save(reserveCarApplyOrder);
		
		Map<String,String> params=new HashMap<String,String>();
		params.put("proposer", reserveCarApplyOrder.getProposer().getName());
		smsService.sendTemplateSMS(reserveCarApplyOrder.getApproveUser().getPhoneNumber(),SMSService.SMS_TEMPLATE_RESERVECARAPPLYORDER_SUBMITTED_FOR_APPROVEUSER , params);
	}
	
	@Transactional
	public void submit(ReserveCarApplyOrder reserveCarApplyOrder){
		reserveCarApplyOrder.setStatus(ReserveCarApplyOrderStatusEnum.SUBMITTED);
		reserveCarApplyOrder.setSubmittedTime(new Date());
		reserveCarApplyOrder.setApproveUser(businessParameterService.getBusinessParameter().getReserveCarApplyOrderApproveUser().get(0));
		reserveCarApplyOrder.setCarApproveUser(businessParameterService.getBusinessParameter().getReserveCarApplyOrderCarApproveUser().get(0));
		reserveCarApplyOrder.setDriverApproveUser(businessParameterService.getBusinessParameter().getReserveCarApplyOrderDriverApproveUser().get(0));
		reserveCarApplyOrderDao.update(reserveCarApplyOrder);
		
		Map<String,String> params=new HashMap<String,String>();
		params.put("proposer", reserveCarApplyOrder.getProposer().getName());
		smsService.sendTemplateSMS(reserveCarApplyOrder.getApproveUser().getPhoneNumber(),SMSService.SMS_TEMPLATE_RESERVECARAPPLYORDER_SUBMITTED_FOR_APPROVEUSER , params);
	}

	@Transactional
	public void approve(ReserveCarApplyOrder reserveCarApplyOrder){
		reserveCarApplyOrder.setApproved(true);
		reserveCarApplyOrder.setApprovedTime(new Date());
		reserveCarApplyOrder.setStatus(ReserveCarApplyOrderStatusEnum.APPROVED);
		reserveCarApplyOrderDao.update(reserveCarApplyOrder);
		
		Map<String,String> params=new HashMap<String,String>();
		params.put("approveUser", reserveCarApplyOrder.getApproveUser().getName());
		smsService.sendTemplateSMS(reserveCarApplyOrder.getProposer().getPhoneNumber(), SMSService.SMS_TEMPLATE_RESERVECARAPPLYORDER_APPROVED_FOR_PROPOSER_CARAPPROVEUSER_DRIVERAPPROVEUSER, params);
		smsService.sendTemplateSMS(reserveCarApplyOrder.getCarApproveUser().getPhoneNumber(),SMSService.SMS_TEMPLATE_RESERVECARAPPLYORDER_APPROVED_FOR_PROPOSER_CARAPPROVEUSER_DRIVERAPPROVEUSER, params);
		smsService.sendTemplateSMS(reserveCarApplyOrder.getDriverApproveUser().getPhoneNumber(),SMSService.SMS_TEMPLATE_RESERVECARAPPLYORDER_APPROVED_FOR_PROPOSER_CARAPPROVEUSER_DRIVERAPPROVEUSER, params);
	}
	
	@Transactional
	public void reject(ReserveCarApplyOrder reserveCarApplyOrder){
		reserveCarApplyOrder.setApproved(false);
		reserveCarApplyOrder.setApprovedTime(new Date());
		reserveCarApplyOrder.setStatus(ReserveCarApplyOrderStatusEnum.REJECTED);
		reserveCarApplyOrderDao.update(reserveCarApplyOrder);
		
		Map<String,String> params=new HashMap<String,String>();
		params.put("approveUser", reserveCarApplyOrder.getApproveUser().getName());
		params.put("reason", reserveCarApplyOrder.getApproveMemo());
		smsService.sendTemplateSMS(reserveCarApplyOrder.getProposer().getPhoneNumber(),SMSService.SMS_TEMPLATE_RESERVECARAPPLYORDER_REJECTED_FOR_PROPOSER , params);
	}
	
	@Transactional
	public void configCar(ReserveCarApplyOrder reserveCarApplyOrder){
		reserveCarApplyOrder.setCarApproved(true);
		if(reserveCarApplyOrder.isDriverApproved()){
			reserveCarApplyOrder.setStatus(ReserveCarApplyOrderStatusEnum.CONFIGURED);
			reserveCarApplyOrder.setConfiguredTime(new Date());
		}
		reserveCarApplyOrderDao.update(reserveCarApplyOrder);
		
		for(Car car:reserveCarApplyOrder.getCars()){
			car.setTempStandingGarage(true);
			carService.updateCar(car);
		}
		
		Map<String,String> params=new HashMap<String,String>();
		params.put("carApproveUser", reserveCarApplyOrder.getCarApproveUser().getName());
		smsService.sendTemplateSMS(reserveCarApplyOrder.getProposer().getPhoneNumber(),SMSService.SMS_TEMPLATE_RESERVECARAPPLYORDER_CARAPPROVED_FOR_PROPOSER , params);
	}
	
	@Transactional
	public void configDriver(ReserveCarApplyOrder reserveCarApplyOrder){
		reserveCarApplyOrder.setDriverApproved(true);
		if(reserveCarApplyOrder.isCarApproved()){
			reserveCarApplyOrder.setStatus(ReserveCarApplyOrderStatusEnum.CONFIGURED);
			reserveCarApplyOrder.setConfiguredTime(new Date());
		}
		reserveCarApplyOrderDao.update(reserveCarApplyOrder);
		
		Map<String,String> params=new HashMap<String,String>();
		params.put("driverApproveUser", reserveCarApplyOrder.getDriverApproveUser().getName());
		smsService.sendTemplateSMS(reserveCarApplyOrder.getProposer().getPhoneNumber(),SMSService.SMS_TEMPLATE_RESERVECARAPPLYORDER_DRIVERAPPROVED_FOR_PROPOSER , params);
	}
	
	@Transactional
	public void delete(Long id){
		reserveCarApplyOrderDao.delete(id);
	}
	
	public PageBean<ReserveCarApplyOrder> queryReserveCarApplyOrder(int pageNum, QueryHelper helper){
		return reserveCarApplyOrderDao.getPageBean(pageNum, helper);
	}

	public List<ReserveCarApplyOrder> getNeedCheckReserveCarApplyOrders() {
		QueryHelper queryHelper=new QueryHelper(ReserveCarApplyOrder.class,"rcao");
		queryHelper.addWhereCondition("rcao.status=?", ReserveCarApplyOrderStatusEnum.CONFIGURED);
		return reserveCarApplyOrderDao.getAllQuerry(queryHelper);
	}

	public ReserveCarApplyOrder getById(Long id) {
		return reserveCarApplyOrderDao.getById(id);
	}
	public List<User> sortUserByName(List<User> users) {

        Comparator<Object> com=Collator.getInstance(java.util.Locale.CHINA);
		
		//排序
		List<User> sortList = new ArrayList<User>();
		String[] names = new String[users.size()];
		for(int i=0;i<users.size();i++){
			names[i] = users.get(i).getName();
		}
		Arrays.sort(names,com);
		for(String name:names){
			sortList.add(userService.getByName(name));
		}
		return sortList;
	}
	
	public List<ReserveCarApplyOrder> getRejects(User proposer){
		QueryHelper helper=new QueryHelper(ReserveCarApplyOrder.class,"rcao");
		helper.addWhereCondition("rcao.status=? and rcao.proposer=?", ReserveCarApplyOrderStatusEnum.REJECTED,proposer);
		return reserveCarApplyOrderDao.getAllQuerry(helper);
	}
	
	public List<ReserveCarApplyOrder> getNeedApprove(User approveUser){
		QueryHelper helper=new QueryHelper(ReserveCarApplyOrder.class,"rcao");
		helper.addWhereCondition("rcao.status=? and rcao.approveUser=?", ReserveCarApplyOrderStatusEnum.SUBMITTED,approveUser);
		return reserveCarApplyOrderDao.getAllQuerry(helper);
	}
	
	public List<ReserveCarApplyOrder> getNeedConfigureCar(User configureCarUser){
		QueryHelper helper=new QueryHelper(ReserveCarApplyOrder.class,"rcao");
		helper.addWhereCondition("rcao.status=? and rcao.carApproved=? and rcao.carApproveUser=?", ReserveCarApplyOrderStatusEnum.APPROVED,false,configureCarUser);
		return reserveCarApplyOrderDao.getAllQuerry(helper);
	}
	
	public List<ReserveCarApplyOrder> getNeedConfigureDriver(User configureDriverUser){
		QueryHelper helper=new QueryHelper(ReserveCarApplyOrder.class,"rcao");
		helper.addWhereCondition("rcao.status=? and rcao.driverApproved=? and rcao.driverApproveUser=?", ReserveCarApplyOrderStatusEnum.APPROVED,false,configureDriverUser);
		return reserveCarApplyOrderDao.getAllQuerry(helper);
	}
	
}
