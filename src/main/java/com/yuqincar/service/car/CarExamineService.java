package com.yuqincar.service.car;

import java.util.Date;
import java.util.List;

import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarCare;
import com.yuqincar.domain.car.CarExamine;
import com.yuqincar.domain.car.CarRefuel;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.utils.QueryHelper;

public interface CarExamineService {
	/**
	 * 增加年审记录。事后登记方式。
	 * 指定appointment为false。
	 * if(car.nextExaminateDate!=null) 将car.nextExaminateDate自加上examineIntervalYear（年）作为下次保养里程的提醒依据。
	 * if(car.nextExaminateDate==null)将car.nextExaminateDate设置为nowdate+examineIntervalYear（年）
	 * @param carCare
	 */
	public void saveCarExamine(CarExamine carExamine);
	
	public CarExamine getCarExamineById(long id);
	
	public PageBean<CarExamine> queryCarExamine(int pageNum , QueryHelper helper);
	
	/**
	 * 
	 * @param carExamine
	 * @return carExamine.appointment==true
	 */
	public boolean canDeleteCarExamine(CarExamine carExamine);
	
	public void deleteCarExamineById(Long id);
	
	/**
	 * 
	 * @param carExamine
	 * @return carExamine.appointment==true
	 */
	public boolean canUpdateCarExamine(CarExamine carExamine);
	
	/**
	 * 修改年审记录。只能对预约的保养记录进行修改。
	 * 设置appointment为false
	 * 关于设置车辆下次保养里程，请参见saveCarExamine的说明。
	 * @param carExamine
	 */
	public void updateCarExamine(CarExamine carExamine);
	
	/**
	 * 列出所有需要提醒年审的车辆。条件：nextExaminateDate-nowdate<15
	 * 按nextExaminateDate升序排列
	 * @return
	 */
	public PageBean<Car> getNeedExamineCars(int pageNum);
	
	/**
	 * 获取下一次年审的日期。
	 * @return Date
	 */
	public Date getNextExamineDate(Car car, Date recentExamineDate);

	public void saveAppointment(CarExamine carExamine);
	
	public void updateAppointment(CarExamine carExamine);
	
}
