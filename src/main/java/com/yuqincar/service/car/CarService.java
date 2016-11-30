package com.yuqincar.service.car;

import java.util.List;

import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarServiceSuperType;
import com.yuqincar.domain.car.CarServiceType;
import com.yuqincar.domain.car.ServicePoint;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.common.TreeNode;
import com.yuqincar.domain.monitor.Device;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.utils.QueryHelper;

public interface CarService {
	
	public void saveCar(Car car);
	
	public void updateCar(Car car);
	
	public Car getCarById(long id);
	
	/**
	 * 删除车辆。
	 * 当有订单与车辆关联时，不能删除车辆。
	 * @param car
	 * @return
	 */
	public boolean canDeleteCar(long id);
	
	public void deleteCarById(long id);
	
	public PageBean<Car> queryCar(int pageNum , QueryHelper helper);
	
	public List<Car> queryAllCar(QueryHelper helper);
	
	public void saveCarServiceType(CarServiceType serviceType);
	
	public void updateCarServiceType(CarServiceType serviceType);
	
	public CarServiceType getCarServiceTypeById(long id);
	
	public void saveCarServiceSuperType(CarServiceSuperType carServiceSuperType);
	
	public CarServiceSuperType getCarServiceSuperTypeByTitle(String title);
	
	public void updateCarServiceSuperType(CarServiceSuperType carServiceSuperType,int inputRows);
	
	/**
	 * 可以删除车型吗
	 * 如果有订单与此车型关联，就不能删除，即使该订单是CANCELLED也不行。
	 * @param serviceType
	 * @return
	 */
	public boolean canDeleteCarServiceType(long id);
	
	public boolean canDeleteCarServiceSuperType(long id);
	
	public void deleteCarServiceType(long id);
	
	public List<CarServiceType> getAllCarServiceType();
	
	public List<CarServiceSuperType> getAllCarServiceSuperType();
	
	public void saveServicePoint(ServicePoint servicePoint);
	
	public void updateServicePoint(ServicePoint servicePoint);
	
	public ServicePoint getServicePointById(long id);
	
	public List<ServicePoint> getAllServicePoint();
	
	/**
	 * 可以删除业务点吗
	 * 如果有车属于这个业务点，就不能删除，但SCRAPPED的车不包含在内。
	 * @param servicePoint
	 * @return
	 */
	public boolean canDeleteServicePoint(long id);
	
	public void deleteServicePoint(long id);

	public Car getCarByPlateNumber(String plateNumber);
	
	/*
	 * 保存车载设备
	 */
	public void saveDevice(Device device);

	/**
	 * 根据驾驶员名称，车牌号，驻车点名称查找车辆
	 * @param driverName
	 * @param plateNumber
	 * @param servicePointName
	 * @return
	 */
	public List<Car> findByDriverNameAndPlateNumberAndServicePointName(String driverName, String plateNumber,
			String servicePointName);

	/**
	 * 根据驻车点查找车辆
	 * @param servicePointName
	 * @return
	 */
	public List<Car> getByServicePointName(String servicePointName);

	/**
	 * 根据驾驶员姓名差找车辆
	 * @param driverName
	 * @return
	 */
	public List<Car> findByDriverName(String driverName);
	
	public List<Car> getAll();
	
	public List<Car> getAllCarFromNotStandingAndNotTempStandingGarage();

	public List<TreeNode> getCarTree(String plateNumber,String synchDriver);

	/**
	 * 获取所有监控的车辆
	 * @return
	 */
	public List<Car> getCarsForMonitoring();
	
	/**
	 * 根据不同车型获取监控的车辆
	 */
	public List<Car> getCarsForMonitoringBySuperType(String superType);
	
	public List<Car> getCarsForPullingViolation();
		
	public Car getCarByDeviceSN(String SN);
	/**
	 * 车牌号是否存在
	 * @param selfId
	 * @param plateNumber
	 * @return
	 */
	public boolean isPlateNumberExist(long selfId, String plateNumber);
	/**
	 * 车架号是否存在
	 * @param selfId
	 * @param VIN
	 * @return
	 */
	public boolean isVINExist(long selfId, String VIN);
	/**
	 * 发动机号是否存在
	 * @param selfId
	 * @param plateNumber
	 * @return
	 */
	public boolean isEngineSNExist(long selfId, String plateNumber);
	
	public List<Car> getCarsByDriver(User driver);
	
	public CarServiceSuperType getCarServiceSuperTypeById(long id);
	
	public CarServiceType getCarServiceTypeByTitle(String title);
	
	public void deleteCarServiceSuperType(long id);
}
