package com.yuqincar.service.businessParameter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.businessParameter.BusinessParameterDao;
import com.yuqincar.domain.businessParameter.BusinessParameter;
import com.yuqincar.service.businessParameter.BusinessParameterService;

@Service
public class BusinessParameterServiceImpl implements BusinessParameterService{

	@Autowired
	private BusinessParameterDao businessParameterDao;
	public BusinessParameter getBusinessParameter() {
		
		return businessParameterDao.getBusinessParameter();
	}
	@Transactional
	public void updateBusinessParameter(BusinessParameter businessParameter) {
		businessParameterDao.update(businessParameter);
	}

	
}
