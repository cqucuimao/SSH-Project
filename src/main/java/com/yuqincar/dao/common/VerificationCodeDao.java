package com.yuqincar.dao.common;

import com.yuqincar.domain.common.VerificationCode;

public interface VerificationCodeDao extends BaseDao<VerificationCode>{
	public VerificationCode getByPhoneNumber(String phoneNumber);
}
