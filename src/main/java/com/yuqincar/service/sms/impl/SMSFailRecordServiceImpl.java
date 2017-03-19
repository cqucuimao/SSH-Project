package com.yuqincar.service.sms.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.message.SMSFailRecordDao;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.message.SMSFailRecord;
import com.yuqincar.service.sms.SMSFailRecordService;
import com.yuqincar.utils.QueryHelper;

@Service
public class SMSFailRecordServiceImpl implements SMSFailRecordService {

	@Autowired
	private SMSFailRecordDao smsFailRecordDao;
	
	
	public List<SMSFailRecord> getAllSMSFailRecord(){
		return smsFailRecordDao.getAll();
	}
	
	public PageBean<SMSFailRecord> querySMSFailRecord(int pageNum , QueryHelper helper)
	{
		return smsFailRecordDao.getPageBean(pageNum, helper);
	}
	
	@Transactional
	public void saveSMSFailRecord(SMSFailRecord sr) {
		smsFailRecordDao.save(sr);
	}
}
