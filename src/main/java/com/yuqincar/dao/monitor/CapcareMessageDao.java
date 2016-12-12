package com.yuqincar.dao.monitor;

import com.yuqincar.dao.common.BaseDao;
import com.yuqincar.domain.monitor.CapcareMessage;

public interface CapcareMessageDao extends BaseDao<CapcareMessage>{
	
	public CapcareMessage getCapcareMessageByPlateNumber(String plateNumber);

}
