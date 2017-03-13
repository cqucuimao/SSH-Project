package com.yuqincar.action.queryStatistics;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.message.SMSRecord;
import com.yuqincar.service.sms.SMSRecordService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class smsRecordAction extends BaseAction {
	
	@Autowired
	private SMSRecordService smsRecordService;
	
	private Date fromDateQ;
	private Date toDateQ;
	private String phoneNumberQ;
	private String contentQ;
	
	/**列表*/
	public String list(){
		QueryHelper helper = new QueryHelper(SMSRecord.class, "sr");
		helper.addOrderByProperty("sr.id", false);
		PageBean<SMSRecord> pageBean = smsRecordService.querySMSRecord(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		ActionContext.getContext().getSession().put("smsRecordHelper", helper);
		return "list";
	}
	
	/**更新合同列表*/
	public String freshList(){
		QueryHelper helper=(QueryHelper)ActionContext.getContext().getSession().get("smsRecordHelper");
		helper.addOrderByProperty("c.id", false);
		PageBean<SMSRecord> pageBean = smsRecordService.querySMSRecord(pageNum, helper);
		ActionContext.getContext().getValueStack().push(pageBean);
		return "list";
	}
	
	/**查询*/
	public String queryList(){
		QueryHelper helper = new QueryHelper(SMSRecord.class, "sr");
		helper.addOrderByProperty("sr.id", false);
		if(phoneNumberQ!=null)
			helper.addWhereCondition("sr.phoneNumber like ?", "%"+phoneNumberQ+"%");
		if(phoneNumberQ!=null)
			helper.addWhereCondition("sr.content like ?", "%"+contentQ+"%");
		if(fromDateQ!=null && toDateQ==null)
		{
			helper.addWhereCondition("sr.date>=?",DateUtils.getMinDate(fromDateQ));
		}
		if(toDateQ!=null && fromDateQ==null )
			helper.addWhereCondition("sr.date<=?", DateUtils.getMaxDate(toDateQ));
		if(toDateQ!=null && fromDateQ!=null )
			helper.addWhereCondition("sr.date between ? and ? ",
					DateUtils.getMinDate(fromDateQ), DateUtils.getMaxDate(toDateQ));
		PageBean<SMSRecord> pageBean = smsRecordService.querySMSRecord(pageNum, helper);		
		ActionContext.getContext().getValueStack().push(pageBean);	
		ActionContext.getContext().getSession().put("smsRecordHelper", helper);
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
}
