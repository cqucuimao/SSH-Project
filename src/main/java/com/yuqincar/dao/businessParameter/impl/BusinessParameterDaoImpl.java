package com.yuqincar.dao.businessParameter.impl;

import org.springframework.stereotype.Repository;

import com.yuqincar.dao.businessParameter.BusinessParameterDao;
import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.domain.businessParameter.BusinessParameter;
import com.yuqincar.domain.order.PriceTable;

@Repository
public class BusinessParameterDaoImpl extends BaseDaoImpl<BusinessParameter> implements BusinessParameterDao{

	public BusinessParameter getBusinessParameter() {

		return (BusinessParameter) (getSession().createQuery("from BusinessParameter").uniqueResult());
	}

}
