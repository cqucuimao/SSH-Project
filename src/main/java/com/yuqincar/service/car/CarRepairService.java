package com.yuqincar.service.car;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarRepair;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.utils.DateRange;
import com.yuqincar.utils.QueryHelper;

public interface CarRepairService {
	/**
	 * 增加维修记录。事后登记方式。
	 * 指定appointment为false。
	 * @param carRepair
	 */
	public void saveCarRepair(CarRepair carRepair);
	
	public CarRepair getCarRepairById(long id);
	
	public PageBean<CarRepair> queryCarRepair(int pageNum , QueryHelper helper);
		
	public void deleteCarRepairById(Long id);
	
	/**
	 * 修改维修记录。只能对预约的维修记录进行修改。
	 * 设置appointment为false
	 * @param carRepair
	 */
	public void updateCarRepair(CarRepair carRepair);
	
	public BigDecimal statisticCarRepair(Date fromDate, Date toDate);	
	
	public void importExcelFile(List<CarRepair> carRepairs);
	
}
