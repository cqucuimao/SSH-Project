package com.yuqincar.dao.car;

import com.yuqincar.dao.common.BaseDao;
import com.yuqincar.domain.car.ServicePoint;

public interface ServicePointDao extends BaseDao<ServicePoint>{

	boolean canDeleteServicePoint(long id);

}
