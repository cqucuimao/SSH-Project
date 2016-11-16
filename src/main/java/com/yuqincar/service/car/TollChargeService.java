package com.yuqincar.service.car;

import java.util.List;

import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.TollCharge;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.utils.QueryHelper;

public interface TollChargeService {
	
	public void saveTollCharge(TollCharge tollCharge);
	
	public void updateTollCharge(TollCharge tollCharge);
	
	public void deleteTollCharge(Long id);
	
	public PageBean<TollCharge> queryTollCharge(int pageNum , QueryHelper helper);
	
	public List<TollCharge> queryAllTollCharge(QueryHelper helper);
	
	public TollCharge getTollChargeById(Long id);
	
	public TollCharge getRecentTollCharge(Car car);
	
	public List<Car> getAllNeedTollCharge(QueryHelper helper);
}
