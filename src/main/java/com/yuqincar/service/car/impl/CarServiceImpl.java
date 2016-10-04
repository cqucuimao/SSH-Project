package com.yuqincar.service.car.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.inject.Inject;
import com.yuqincar.dao.car.CarDao;
import com.yuqincar.dao.car.CarServiceTypeDao;
import com.yuqincar.dao.car.ServicePointDao;
import com.yuqincar.dao.monitor.DeviceDao;
import com.yuqincar.dao.order.CarServiceSuperTypeDao;
import com.yuqincar.dao.order.OrderDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarServiceSuperType;
import com.yuqincar.domain.car.CarServiceType;
import com.yuqincar.domain.car.CarStatusEnum;
import com.yuqincar.domain.car.ServicePoint;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.common.TreeNode;
import com.yuqincar.domain.monitor.Device;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.car.CarService;
import com.yuqincar.utils.QueryHelper;

@Service
public class CarServiceImpl implements CarService {
	@Autowired
	private CarDao carDao;
	@Autowired
	private CarServiceTypeDao carServiceTypeDao;
	@Autowired
	private CarServiceSuperTypeDao carServiceSuperTypeDao;
	@Autowired
	private ServicePointDao servicePointDao;
	@Autowired
	private DeviceDao deviceDao;
	
	/*----------车辆基本信息-----------*/
	@Transactional
	public void saveCar(Car car) {
		carDao.save(car);

	}

	@Transactional
	public void updateCar(Car car) {
		carDao.update(car);

	}

	public Car getCarById(long id) {
		System.out.println("carDao="+carDao);
		System.out.println("ID="+id);
		return carDao.getById(id);
	}

	public boolean isPlateNumberExist(long selfId, String plateNumber) {
		return carDao.isPlateNumberExist(selfId, plateNumber);
	}

	public boolean isVINExist(long selfId, String VIN) {
		return carDao.isVINExist(selfId,VIN);
	}

	public boolean isEngineSNExist(long selfId, String engineSN) {
		return carDao.isEngineSNExist(selfId,engineSN);
	}
	
	public List<Car> getCarsByDriver(User driver){
		QueryHelper helper=new QueryHelper(Car.class,"c");
		helper.addWhereCondition("c.driver=?", driver);
		return carDao.getAllQuerry(helper);
	}

	public boolean canDeleteCar(long id) {
		return carDao.canDeleteCar(id);
	}

	@Transactional
	public void deleteCarById(long id) {
		carDao.delete(id);
	}

	public PageBean<Car> queryCar(int pageNum, QueryHelper helper) {
		return carDao.getPageBean(pageNum, helper);
	}
	/*-----------车辆服务类型-----------*/
	@Transactional
	public void saveCarServiceType(CarServiceType serviceType) {
		carServiceTypeDao.save(serviceType);

	}

	@Transactional
	public void updateCarServiceType(CarServiceType serviceType) {
		carServiceTypeDao.update(serviceType);

	}

	public CarServiceType getCarServiceTypeById(long id) {
		
		return carServiceTypeDao.getById(id);
	}

	public boolean canDeleteCarServiceType(long id) {
		return carServiceTypeDao.canDeleteCarServiceType(id);
	}

	@Transactional
	public void deleteCarServiceType(long id) {
		carServiceTypeDao.delete(id);

	}

	public List<CarServiceType> getAllCarServiceType() {
		return carServiceTypeDao.getAll();
	}
	
	public List<CarServiceSuperType> getAllCarServiceSuperType() {

		return carServiceSuperTypeDao.getAll();
	}
	
	/*-----------车辆服务点-----------*/
	@Transactional
	public void saveServicePoint(ServicePoint servicePoint) {
		servicePointDao.save(servicePoint);
	}

	@Transactional
	public void updateServicePoint(ServicePoint servicePoint) {
		servicePointDao.update(servicePoint);

	}

	public ServicePoint getServicePointById(long id) {
		return servicePointDao.getById(id);
	}

	public boolean canDeleteServicePoint(long id) {
		return servicePointDao.canDeleteServicePoint(id);
	}

	@Transactional
	public void deleteServicePoint(long id) {
		servicePointDao.delete(id);
	}
	
	public List<ServicePoint> getAllServicePoint() {
		return servicePointDao.getAll();
	}
	
	public Car getCarByPlateNumber(String plateNumber) {
		return carDao.getByPlateNumber(plateNumber);
	}

	public List<Car> findByDriverNameAndPlateNumberAndServicePointName(String driverName, String plateNumber,
			String servicePointName) {
		return carDao.findByDriverNameAndPlateNumberAndServicePointName(driverName,plateNumber,servicePointName);
	}

	public List<Car> getByServicePointName(String servicePointName) {
		return carDao.findByServicePointName(servicePointName);
	}

	public List<Car> findByDriverName(String driverName) {
		return carDao.findByDriverName(driverName);
	}

	public List<Car> getAll() {
		return carDao.getAll();
	}
	
	public List<TreeNode> getCarTree(String plateNumber,String synchDriver) {
		System.out.println("in getCarTree, plateNumber="+plateNumber);
		List<Car> cars ;
		//默认展开
		boolean flag = true;
		cars= carDao.searchByPlateNumber(plateNumber);
		
		List<TreeNode> nodes = new ArrayList<TreeNode>();
		
		for(Car c : cars) {
			
			TreeNode child = new TreeNode();
			
			child.setName(c.getPlateNumber());
			child.setId(c.getId());
			if(!StringUtils.isEmpty(synchDriver)){
				if(c.getDriver()!=null){
					Map<String,Object> param=new HashMap<String,Object>();
					param.put("driverName", c.getDriver().getName());
					param.put("driverId", c.getDriver().getId());
					child.setParam(param);
				}
			}
			
			String departmentName = "";
			if(c.getServicePoint()!=null) {
				departmentName = c.getServicePoint().getName();
			}

			TreeNode parent = getParentTreeNode(nodes,departmentName);
			
			if(parent!=null) {
				if(parent.getChildren()!=null) {
					parent.getChildren().add(child);
				}
			} else {
				TreeNode p = new TreeNode();
				p.setName(departmentName);
				p.setChildren(new ArrayList());
				p.getChildren().add(child);
				p.setOpen(flag);
				nodes.add(p);
			}
		}
		
		return nodes;
	}

	private TreeNode getParentTreeNode(List<TreeNode> nodes,String departmentName) {
		for(TreeNode node : nodes) {
			if(node.getName().equals(departmentName)) {
				return node;
			}
		}
		return null;
	}

	public List<Car> getCarsForMonitoring() {
		QueryHelper queryHelper=new QueryHelper(Car.class,"c");
		queryHelper.addWhereCondition("c.status=?", CarStatusEnum.NORMAL);
		queryHelper.addWhereCondition("c.device is not null");
		queryHelper.addWhereCondition("c.borrowed=?", false);
		return carDao.getAllQuerry(queryHelper);
	}
	
	public List<Car> getCarsForPullingViolation(){
		QueryHelper queryHelper=new QueryHelper(Car.class,"c");
		queryHelper.addWhereCondition("c.status=?", CarStatusEnum.NORMAL);
		queryHelper.addWhereCondition("c.borrowed=?", false);
		return carDao.getAllQuerry(queryHelper);
	}

	@Transactional
	public void saveDevice(Device device) {
		
		deviceDao.save(device);
	}
	
	public Car getCarByDeviceSN(String SN){
		return carDao.getCarByDeviceSN(SN);
	}
	@Transactional
	public void saveCarServiceSuperType(CarServiceSuperType carServiceSuperType) {
		
		for(int i=0;i<carServiceSuperType.getTypes().size();i++){
			carServiceSuperType.getTypes().get(i).setSuperType(carServiceSuperType);
			carServiceTypeDao.save(carServiceSuperType.getTypes().get(i));
		}
		
		carServiceSuperTypeDao.save(carServiceSuperType);
		
	}

	public CarServiceSuperType getCarServiceSuperTypeByTitle(String title) {
		return carServiceSuperTypeDao.getCarServiceSuperTypeByTitle(title);
	}
	
	@Transactional
	public void updateCarServiceSuperType(CarServiceSuperType carServiceSuperType,int inputRows) {
		
		List<CarServiceType> carServiceTypes = carServiceSuperType.getTypes();
		for(int i=carServiceTypes.size()-1;i>inputRows;i--){
			carServiceSuperType.getTypes().get(i).setSuperType(carServiceSuperType);
			carServiceTypeDao.save(carServiceSuperType.getTypes().get(i));
		}
		carServiceSuperTypeDao.update(carServiceSuperType);
	}

}
