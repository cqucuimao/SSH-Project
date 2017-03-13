package com.yuqincar.service.sms;

import java.util.List;

import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.message.SMSRecord;
import com.yuqincar.service.base.BaseService;
import com.yuqincar.utils.QueryHelper;

public interface SMSRecordService extends BaseService{
    public List<SMSRecord> getAllSMSRecord();
    
    public PageBean<SMSRecord> querySMSRecord(int pageNum , QueryHelper helper);
    
   
    public void saveSMSRecord(SMSRecord sr);
}
