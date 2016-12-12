package com.yuqincar.service.monitor;

import java.util.List;

import com.yuqincar.domain.monitor.CapcareMessage;
import com.yuqincar.service.base.BaseService;

public interface CapcareMessageService extends BaseService{
	
	public void delete(long id);
	
	public void save(CapcareMessage capcareMessage);
	
	public void update(CapcareMessage capcareMessage);
	
	public CapcareMessage getCapcareMessageByPlateNumber(String plateNumber);
	
	public void initCapcareMessages();
	
	public List<CapcareMessage> getAllCapcareMessage();
	
	public void getCapcareMessagePerTenSecondFromCapcare();

}
