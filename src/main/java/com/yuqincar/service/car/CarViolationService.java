package com.yuqincar.service.car;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;

import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarViolation;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.utils.QueryHelper;

public interface CarViolationService {
	
	public void pullViolationFromCQJG() throws UnsupportedEncodingException, ParseException;
	
	public void saveCarViolation(CarViolation carViolation);
	
	public List<CarViolation> getAllCarViolation();
	
	public boolean canUpdateCarViolation(Long id);
	
	public void updateCarViolation(CarViolation carViolation);
	
	public void deleteCarViolation(Long id);
	
	public PageBean<CarViolation> queryCarViolation(int pageNum, QueryHelper helper);

	public CarViolation getCarViolationById(long id);
	
	public List<CarViolation> getCarViolationByCar(Car car);
}
