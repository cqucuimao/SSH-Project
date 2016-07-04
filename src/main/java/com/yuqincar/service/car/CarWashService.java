package com.yuqincar.service.car;

import java.util.List;

import com.yuqincar.domain.car.CarWash;
import com.yuqincar.domain.car.CarWashShop;
import com.yuqincar.domain.car.Material;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.utils.QueryHelper;

public interface CarWashService {
	public void saveCarWashShop(CarWashShop carWashShop);
	
	public boolean canDeleteCarWashShop(Long id);
	
	public void deleteCarWashShop(Long id);
	
	public CarWashShop getCarWashShopById(Long id);
	
	public List<CarWashShop> getAllCarWashShop();
	
	public void saveCarWash(CarWash carWash);
	
	public void updateCarWash(CarWash carWash);
	
	public void deleteCarWash(Long id);
	
	public PageBean<CarWash> queryCarWash(int pageNum , QueryHelper helper);
	
	public CarWash getCarWashById(Long id);
}
