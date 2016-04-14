package com.yuqincar.service.car;

import java.util.Date;
import java.util.List;

import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarExamine;
import com.yuqincar.domain.common.PageBean;
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
	
	/**
	 * 预约年审。事前登记。
	 * 生成CarExamine，设置fromDate和toDate作为调度的时间依据，并置appointment为true。其余内容使用updateCarExamine设置。
	 * @param car
	 * @param date
	 */
	public void carExamineAppointment(Car car,Date date);
	
	public CarExamine getCarExamineById(long id);
	
	public PageBean<CarExamineService> queryCarExamine(int pageNum , QueryHelper helper);
	
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
	 * 按nextExaminateDate-nowdate升序排列
	 * @return
	 */
	public List<Car> getAllNeedExamineCars();
}
