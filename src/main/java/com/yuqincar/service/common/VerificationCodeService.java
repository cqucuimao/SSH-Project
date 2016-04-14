package com.yuqincar.service.common;

import com.yuqincar.domain.common.VerificationCode;

public interface VerificationCodeService {
	public void save(VerificationCode code);
	public void update(VerificationCode code);
	public VerificationCode getByPhoneNumber(String phoneNumber);
}
