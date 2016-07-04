package com.yuqincar.dao.car;

import com.yuqincar.dao.common.BaseDao;
import com.yuqincar.domain.car.CommercialInsuranceType;

public interface CommercialInsuranceTypeDao extends BaseDao<CommercialInsuranceType>{

	boolean canDeleteCommercialInsuranceType(long id);

}
