package com.yuqincar.action.queryStatistics;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.message.SMSFailRecord;
import com.yuqincar.service.customer.CustomerService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.service.sms.SMSFailRecordService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class smsFailRecordAction extends BaseAction {
	
	@Autowired
	private SMSFailRecordService smsFailRecordService;
	@Autowired
	private UserService userService;
	@Autowired
	private CustomerService customerService;
	private Date fromDateQ;
	private Date toDateQ;
	private String phoneNumberQ;
	private String contentQ;
	/**列表*/
	public String list(){
		System.out.println("smsRecord List");
		QueryHelper helper = new QueryHelper(SMSFailRecord.class, "sr");
		helper.addOrderByProperty("sr.id", false);
		PageBean<SMSFailRecord> pageBean = smsFailRecordService.querySMSFailRecord(pageNum, helper);
		System.out.println("size="+pageBean.getRecordList().size());
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("smsFailRecordHelper", helper);
		return "list";
	}
	
	/**更新合同列表*/
	public String freshList(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("smsFailRecordHelper");
		PageBean<SMSFailRecord> pageBean = smsFailRecordService.querySMSFailRecord(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
	
	/**查询*/
	public String queryList(){
		QueryHelper helper = new QueryHelper(SMSFailRecord.class, "sfr");
		helper.addOrderByProperty("sfr.id", false);
		if(phoneNumberQ!=null)
			helper.addWhereCondition("sfr.phoneNumber like ?", "%"+phoneNumberQ+"%");
		if(phoneNumberQ!=null)
			helper.addWhereCondition("sfr.content like ?", "%"+contentQ+"%");
		if(fromDateQ!=null && toDateQ==null)
		{
			helper.addWhereCondition("sfr.date>=?",DateUtils.getMinDate(fromDateQ));
		}
		if(toDateQ!=null && fromDateQ==null )
			helper.addWhereCondition("sfr.date<=?", DateUtils.getMaxDate(toDateQ));
		if(toDateQ!=null && fromDateQ!=null )
			helper.addWhereCondition("sfr.date between ? and ? ",
					DateUtils.getMinDate(fromDateQ), DateUtils.getMaxDate(toDateQ));
		PageBean<SMSFailRecord> pageBean = smsFailRecordService.querySMSFailRecord(pageNum, helper);		
		ActionContext.getContext().getValueStack().push(pageBean);	
		ActionContext.getContext().getSession().put("smsFailRecordHelper", helper);
		return "list";
	}

	public Date getFromDateQ() {
		return fromDateQ;
	}

	public void setFromDateQ(Date fromDateQ) {
		this.fromDateQ = fromDateQ;
	}

	public Date getToDateQ() {
		return toDateQ;
	}

	public void setToDateQ(Date toDateQ) {
		this.toDateQ = toDateQ;
	}

	public String getPhoneNumberQ() {
		return phoneNumberQ;
	}

	public void setPhoneNumberQ(String phoneNumberQ) {
		this.phoneNumberQ = phoneNumberQ;
	}

	public String getContentQ() {
		return contentQ;
	}

	public void setContentQ(String contentQ) {
		this.contentQ = contentQ;
	}
	
	public String getSendName() {
		SMSFailRecord smsFailRecord=(SMSFailRecord)ActionContext.getContext().getValueStack().peek();
		String sendName="";
		if(userService.getByPhoneNumber(smsFailRecord.getPhoneNumber())!=null)
		{
			sendName=userService.getByPhoneNumber(smsFailRecord.getPhoneNumber()).getName()+"("
					+userService.getByPhoneNumber(smsFailRecord.getPhoneNumber()).getDepartment().getName()+")";
		}
		else if(customerService.getCustomerByPhoneNumber(smsFailRecord.getPhoneNumber())!=null) {
			sendName=customerService.getCustomerByPhoneNumber(smsFailRecord.getPhoneNumber()).getName()+"("
					+customerService.getCustomerByPhoneNumber(smsFailRecord.getPhoneNumber()).getCustomerOrganization().getName()+")";
		}
		return sendName;
	}
}
