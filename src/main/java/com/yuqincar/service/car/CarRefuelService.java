package com.yuqincar.service.car;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yuqincar.domain.car.CarRefuel;
import com.yuqincar.domain.car.CarWashShop;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.utils.QueryHelper;

public interface CarRefuelService {
	
	public void saveCarRefuel(CarRefuel carRefuel);
	
	public CarRefuel getCarRefuelById(long id);
	
	public PageBean<CarRefuel> queryCarRefuel(int pageNum , QueryHelper helper);
	
	public BigDecimal statisticCarRefuel(Date fromDate, Date toDate);
	
	public void importExcelFile(List<CarRefuel> carRefuels);
	
	public List<CarRefuel> getAllCarRefuel(QueryHelper queryHelper);
	
}
