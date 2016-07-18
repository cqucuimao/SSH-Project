package com.yuqincar.service.car;

import com.yuqincar.domain.car.CarViolation;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.utils.QueryHelper;

public interface CarViolationService {
	
	public void pullViolationFromCQJG();
	
	public void saveCarViolation(CarViolation carViolation);
	
	public boolean canUpdateCarViolation(Long id);
	
	public void updateCarViolation(CarViolation carViolation);
	
	public void deleteCarViolation(Long id);
	
	public PageBean<CarViolation> queryCarViolation(int pageNum, QueryHelper helper);

	public CarViolation getCarViolationById(long id);
}
