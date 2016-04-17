package com.yuqincar.service.monitor.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.monitor.TemporaryWarningDao;
import com.yuqincar.dao.monitor.WarningMessageDao;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.monitor.WarningMessage;
import com.yuqincar.domain.monitor.WarningMessageTypeEnum;
import com.yuqincar.domain.order.OrderStatement;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.monitor.WarningMessageService;
import com.yuqincar.utils.QueryHelper;

@Service
public class WarningMessageServiceImpl implements WarningMessageService {

	@Autowired
	private WarningMessageDao warningMessageDao;
	@Autowired
	private TemporaryWarningDao tempWarningDao;
	@Autowired
	private CarService carService;

	public PageBean<OrderStatement> getPageBean(int pageNum, QueryHelper helper) {
		return warningMessageDao.getPageBean(pageNum, helper);
	}

	@Transactional
	public void addWarningMessage(Long carId, Date date, WarningMessageTypeEnum pulledout) {
		WarningMessage message=new WarningMessage();
		message.setCar(carService.getCarById(carId));
		message.setType(WarningMessageTypeEnum.PULLEDOUT);
		message.setDate(date);
		message.setDealed(false);
		warningMessageDao.save(message);
	}

	public boolean isTempMessageExist(Long carId) {
		return tempWarningDao.isTempMessageExist(carId);
	}

	@Transactional
	public void deleteTempMessage(Long carId) {
		tempWarningDao.deleteTempMessage(carId);
	}

	@Transactional
	public void addTempWarningMessage(Long carId) {
		tempWarningDao.addTempWarningMessage(carId);
	}

	public List<WarningMessage> getUndealedMessages() {
		return warningMessageDao.getUndealedMessages();
	}

	@Transactional
	public void dealWarnings(Long[] ids) {
		warningMessageDao.dealWarnings(ids);
	}

}
