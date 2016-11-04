package com.yuqincar.service.privilege.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuqincar.dao.privilege.CompanyDao;
import com.yuqincar.domain.common.Company;
import com.yuqincar.service.privilege.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	CompanyDao companyDao;

	public Company getCompanyById(long companyId) {
		return companyDao.getById(companyId);
	}
	
	
}
