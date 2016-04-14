package com.msober.service.common;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.msober.base.BaseTest;
import com.yuqincar.domain.common.VerificationCode;
import com.yuqincar.service.common.VerificationCodeService;


public class VerificationCodeServiceTest extends BaseTest {

	@Autowired
	private VerificationCodeService codeService;
	
	@Test
	@DatabaseSetup({ "classpath:../test-classes/data/common/VerificationCode.xml" })
	public void testGetByPhoneNumber() {
		VerificationCode code = codeService.getByPhoneNumber("13912341234");
		System.out.println(code);
		
		VerificationCode code2 = codeService.getByPhoneNumber("1391234");
		System.out.println(code2);
		
		VerificationCode code3 = codeService.getByPhoneNumber("13912341235");
		System.out.println(code3);
		
	}


}
