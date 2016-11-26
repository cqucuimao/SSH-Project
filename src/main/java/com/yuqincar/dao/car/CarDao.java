package com.yuqincar.dao.car;

import java.util.List;

import com.yuqincar.dao.common.BaseDao;
import com.yuqincar.domain.car.Car;

public interface CarDao extends BaseDao<Car> {

	boolean isPlateNumberExist(long selfId, String plateNumber);

	boolean isVINExist(long selfId, String VIN);

	boolean isEngineSNExist(long selfId, String EngineSN);

	boolean canDeleteCar(long id);

	Car getByPlateNumber(String plateNumber);

	/**
	 * 根据驾驶员名称，车牌号，驻车点查找车辆
	 * @param driverName
	 * @param plateNumber
	 * @param servicePointName
	 * @return
	 */
	List<Car> findByDriverNameAndPlateNumberAndServicePointName(String driverName, String plateNumber,
			String servicePointName);

	/**
	 * 根据驻车点民称查找车辆
	 * @param servicePointName
	 * @return
	 */
	List<Car> findByServicePointName(String servicePointName);

	/**
	 * 根据驾驶员姓名查找车辆
	 * @param driverName
	 * @return
	 */
	List<Car> findByDriverName(String driverName);
	
	public List<Car> searchByPlateNumber(String plateNumber) ;

	/**
	 * 获取所有未报废车辆
	 * @return
	 */
	List<Car> getAllNormalCars();

	public Car getCarByDeviceSN(String SN);
	
	public List<Car> getAllCarFromNotStandingAndNotTempStandingGarage();
}
