package com.yuqincar.service.common.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.common.VerificationCodeDao;
import com.yuqincar.domain.common.VerificationCode;
import com.yuqincar.service.common.VerificationCodeService;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService{
	@Autowired
	private VerificationCodeDao codeDao;

	@Transactional
	public void save(VerificationCode code) {
		codeDao.save(code);
	}

	@Transactional
	public void update(VerificationCode code) {
		codeDao.update(code);
	}

	public VerificationCode getByPhoneNumber(String phoneNumber) {
		return codeDao.getByPhoneNumber(phoneNumber);
	}
}
