package com.yuqincar.service.car;

import java.math.BigDecimal;
import java.util.Date;

import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarRepair;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.utils.QueryHelper;

public interface CarRepairService {
	/**
	 * 增加维修记录。事后登记方式。
	 * 指定appointment为false。
	 * @param carRepair
	 */
	public void saveCarRepair(CarRepair carRepair);
	
	/**
	 * 预约保养。事前登记。
	 * 生成CarRepair，设置fromDate和toDate作为调度的时间依据，并置appointment为true。其余内容使用updateCarRepair设置。
	 * @param car
	 * @param date
	 */
	public void carRepairAppointment(Car car,Date fromDate,Date toDate);
	
	public CarRepair getCarRepairById(long id);
	
	public PageBean<CarRepairService> queryCarRepair(int pageNum , QueryHelper helper);
	
	/**
	 * 
	 * @param carRepair
	 * @return carRepair.appointment==true
	 */
	public boolean canDeleteCarRepair(CarRepair carRepair);
	
	public void deleteCarRepairById(Long id);
	
	/**
	 * 
	 * @param carRepair
	 * @return carRepair.appointment==true
	 */
	public boolean canUpdateCarRepair(CarRepair carRepair);
	
	/**
	 * 修改维修记录。只能对预约的维修记录进行修改。
	 * 设置appointment为false
	 * @param carRepair
	 */
	public void updateCarRepair(CarRepair carRepair);
	
	public BigDecimal statisticCarRepair(Date fromDate, Date toDate);	
}
