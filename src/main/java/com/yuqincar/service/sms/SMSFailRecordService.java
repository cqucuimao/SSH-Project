package com.yuqincar.service.sms;

import java.util.List;

import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.message.SMSFailRecord;
import com.yuqincar.service.base.BaseService;
import com.yuqincar.utils.QueryHelper;

public interface SMSFailRecordService extends BaseService{
    public List<SMSFailRecord> getAllSMSFailRecord();
    
    public PageBean<SMSFailRecord> querySMSFailRecord(int pageNum , QueryHelper helper);
    
   
    public void saveSMSFailRecord(SMSFailRecord sfr);
}
