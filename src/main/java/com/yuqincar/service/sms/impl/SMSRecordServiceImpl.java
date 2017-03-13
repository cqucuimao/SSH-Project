package com.yuqincar.service.sms.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.yuqincar.dao.message.SMSRecordDao;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.message.SMSRecord;
import com.yuqincar.service.sms.SMSRecordService;
import com.yuqincar.utils.QueryHelper;

@Service
public class SMSRecordServiceImpl implements SMSRecordService {

	@Autowired
	private SMSRecordDao smsRecordDao;
	
	
	public List<SMSRecord> getAllSMSRecord(){
		return smsRecordDao.getAll();
	}
	
	public PageBean<SMSRecord> querySMSRecord(int pageNum , QueryHelper helper)
	{
		return smsRecordDao.getPageBean(pageNum, helper);
	}
	
	@Transactional
	public void saveSMSRecord(SMSRecord sr) {
		smsRecordDao.save(sr);
	}
}
