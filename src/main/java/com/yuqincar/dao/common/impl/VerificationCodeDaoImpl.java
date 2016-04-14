package com.yuqincar.dao.common.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.yuqincar.dao.common.VerificationCodeDao;
import com.yuqincar.domain.common.VerificationCode;

@Repository
public class VerificationCodeDaoImpl extends BaseDaoImpl<VerificationCode> implements VerificationCodeDao {
	public VerificationCode getByPhoneNumber(String phoneNumber) {
		return (VerificationCode)getSession().createQuery("from VerificationCode c where c.phoneNumber=?")
					.setParameter(0, phoneNumber).uniqueResult();
		
		/*
		//由于验证码会用来作为APP每次请求的密码，所以不能有过期时间。
		long THRESHTIME = 3*60*1000L; // 3分钟
		System.out.println("codes size: " + codes.size());
		if(codes!=null && codes.size()>0) {
			VerificationCode code =  codes.get(0);
			Date createTime = code.getCreateTime();
			Date now = new Date();
			if(now.getTime() - createTime.getTime() < THRESHTIME && now.getTime() - createTime.getTime()>0L) {
				return code;
			} else {
				return null;
			}
		}
		return null;
		*/
	}
}
