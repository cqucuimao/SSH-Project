package com.yuqincar.service.car;

import java.util.List;

import com.yuqincar.domain.car.CarCare;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.utils.QueryHelper;

public interface CarCareService {
	
	public void saveCarCare(CarCare carCare);
		
	public CarCare getCarCareById(long id);
	
	public PageBean<CarCare> queryCarCare(int pageNum , QueryHelper helper);
	
	public void deleteCarCareById(long id);
	
	public void updateCarCare(CarCare carCare);
		
	public void importExcelFile(List<CarCare> carCares);
}
