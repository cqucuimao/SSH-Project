package com.yuqincar.service.order.impl;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.order.ReserveCarApplyOrderDao;
import com.yuqincar.domain.businessParameter.BusinessParameter;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.ReserveCarApplyOrder;
import com.yuqincar.domain.order.ReserveCarApplyOrderStatusEnum;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.businessParameter.BusinessParameterService;
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
	private BusinessParameterService businessParameterService;
	@Transactional
	public void saveReserveCarApplyOrder(ReserveCarApplyOrder reserveCarApplyOrder){
		//如果是新建时提交，发短信通知  approveUser
		if(reserveCarApplyOrder.getStatus() == ReserveCarApplyOrderStatusEnum.SUBMITTED){
	    	Map<String,String> params=new HashMap<String,String>();
			params.put("proposer", reserveCarApplyOrder.getProposer().getName());
			for(User approveUser:businessParameterService.getBusinessParameter().getReserveCarApplyOrderApproveUser()){
				smsService.sendTemplateSMS(approveUser.getPhoneNumber(),SMSService.SMS_TEMPLATE_RESERVECARAPPLYORDER_SUBMITTED_FOR_APPROVEUSER , params);
			}
		}
		reserveCarApplyOrderDao.save(reserveCarApplyOrder);
	}
	@Transactional
	public void updateReserveCarApplyOrder(ReserveCarApplyOrder reserveCarApplyOrder){
		//如果是修改时提交，发短信通知  approveUser
		if(reserveCarApplyOrder.getStatus() == ReserveCarApplyOrderStatusEnum.SUBMITTED){
	    	Map<String,String> params=new HashMap<String,String>();
			params.put("proposer", reserveCarApplyOrder.getProposer().getName());
			for(User approveUser:businessParameterService.getBusinessParameter().getReserveCarApplyOrderApproveUser()){
				smsService.sendTemplateSMS(approveUser.getPhoneNumber(),SMSService.SMS_TEMPLATE_RESERVECARAPPLYORDER_SUBMITTED_FOR_APPROVEUSER , params);
			}
		}
		//如果驳回，发短信通知  proposer
		if(reserveCarApplyOrder.getStatus() == ReserveCarApplyOrderStatusEnum.REJECTED){
	    	Map<String,String> params=new HashMap<String,String>();
			params.put("approveUser", reserveCarApplyOrder.getApproveUser().getName());
			params.put("reason", reserveCarApplyOrder.getApproveMemo());
			smsService.sendTemplateSMS(reserveCarApplyOrder.getProposer().getPhoneNumber(),SMSService.SMS_TEMPLATE_RESERVECARAPPLYORDER_REJECTED_FOR_PROPOSER , params);
		}
		//如果审核通过，发短息通知 proposer,carApproveUser,driverApproveUser
		if(reserveCarApplyOrder.getStatus() == ReserveCarApplyOrderStatusEnum.APPROVED && 
				reserveCarApplyOrder.getCarApproveUser() == null && reserveCarApplyOrder.getDriverApproveUser() == null){
	    	Map<String,String> params=new HashMap<String,String>();
			params.put("approveUser", reserveCarApplyOrder.getApproveUser().getName());
			//applyUser
			smsService.sendTemplateSMS(reserveCarApplyOrder.getProposer().getPhoneNumber(), SMSService.SMS_TEMPLATE_RESERVECARAPPLYORDER_APPROVED_FOR_PROPOSER_CARAPPROVEUSER_DRIVERAPPROVEUSER, params);
			//carApproveUser
			for(User carApproveUser:businessParameterService.getBusinessParameter().getReserveCarApplyOrderCarApproveUser()){
				smsService.sendTemplateSMS(carApproveUser.getPhoneNumber(),SMSService.SMS_TEMPLATE_RESERVECARAPPLYORDER_APPROVED_FOR_PROPOSER_CARAPPROVEUSER_DRIVERAPPROVEUSER, params);
			}
			//driverApproveUser
			for(User driverApproveUser:businessParameterService.getBusinessParameter().getReserveCarApplyOrderDriverApproveUser()){
				smsService.sendTemplateSMS(driverApproveUser.getPhoneNumber(),SMSService.SMS_TEMPLATE_RESERVECARAPPLYORDER_APPROVED_FOR_PROPOSER_CARAPPROVEUSER_DRIVERAPPROVEUSER, params);
			}
		}
		//如果已配置车辆，通知proposer
		if(reserveCarApplyOrder.getCars() != null && reserveCarApplyOrder.getCars().size() != 0){
			Map<String,String> params=new HashMap<String,String>();
			params.put("carApproveUser", reserveCarApplyOrder.getCarApproveUser().getName());
			smsService.sendTemplateSMS(reserveCarApplyOrder.getProposer().getPhoneNumber(),SMSService.SMS_TEMPLATE_RESERVECARAPPLYORDER_CARAPPROVED_FOR_PROPOSER , params);
		}
		//如果已配置司机，通知proposer
		if(reserveCarApplyOrder.getDrivers() != null && reserveCarApplyOrder.getDrivers().size() != 0){
			Map<String,String> params=new HashMap<String,String>();
			params.put("driverApproveUser", reserveCarApplyOrder.getDriverApproveUser().getName());
			smsService.sendTemplateSMS(reserveCarApplyOrder.getProposer().getPhoneNumber(),SMSService.SMS_TEMPLATE_RESERVECARAPPLYORDER_DRIVERAPPROVED_FOR_PROPOSER , params);
		}
		
		reserveCarApplyOrderDao.update(reserveCarApplyOrder);
	}
	@Transactional
	public void deleteReserveCarApplyOrder(Long id){
		reserveCarApplyOrderDao.delete(id);
	}
	
	public PageBean<ReserveCarApplyOrder> queryReserveCarApplyOrder(int pageNum, QueryHelper helper){
		return reserveCarApplyOrderDao.getPageBean(pageNum, helper);
	}
	
	public List<ReserveCarApplyOrder> queryAllReserveCarApplyOrder(QueryHelper helper){
		return reserveCarApplyOrderDao.getAllQuerry(helper);
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
	public List<ReserveCarApplyOrder> getReserveCarApplyOrderByStatus(ReserveCarApplyOrderStatusEnum status) {
		QueryHelper queryHelper=new QueryHelper(ReserveCarApplyOrder.class,"rcao");
		queryHelper.addWhereCondition("rcao.status=?", status);
		return reserveCarApplyOrderDao.getAllQuerry(queryHelper);
	}
	
}
