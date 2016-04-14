package com.yuqincar.service.car;

import java.math.BigDecimal;
import java.util.Date;

import com.yuqincar.domain.car.CarRefuel;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.utils.QueryHelper;

public interface CarRefuelService {
	
	public void saveCarRefuel(CarRefuel carRefuel);
	
	public CarRefuel getCarRefuelById(long id);
	
	public PageBean<CarRefuelService> queryCarRefuel(int pageNum , QueryHelper helper);
	
	public BigDecimal statisticCarRefuel(Date fromDate, Date toDate);

}
