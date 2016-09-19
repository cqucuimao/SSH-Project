/**
 * University Of Chongqing.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.yuqincar.action.schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarCare;
import com.yuqincar.domain.car.CarExamine;
import com.yuqincar.domain.car.CarRepair;
import com.yuqincar.domain.car.CarStatusEnum;
import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.CustomerOrganization.CustomerOrganizationService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.order.OrderService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class DriverAction extends BaseAction {

    @Autowired
    private OrderService orderService;
    @Autowired
    private CarService carService;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerOrganizationService customerOrganizationService;
    
    
    private long driverId;
    private String plateNumber;
    private Date   beginDate;
    private Date   endDate;
    private long servicePointId;
    
    private String customerOrganizationName;
    
    private Car car;
    private User driver;
    private LinkedHashMap<LineTitleVO, LinkedHashMap<String, Integer>> map;
    
    private void prepareDriverData(User _driver, Date _beginDate, Date _endDate){
    	if(_beginDate==null)
    		_beginDate = new Date();
    	if(_endDate==null)
    		_endDate = DateUtils.getOffsetDate(_beginDate, 4);
    	List<LinkedHashMap<LineTitleVO, LinkedHashMap<String, Integer>>> driverStatus = new ArrayList<LinkedHashMap<LineTitleVO, LinkedHashMap<String, Integer>>>();
        LinkedHashMap<LineTitleVO, LinkedHashMap<String, Integer>> teMap = null;

        for(Car c: carService.getCarsByDriver(_driver)){
	        LinkedHashMap<String, Integer> driverUseInfoNum = null;
	         
	        teMap = new LinkedHashMap<LineTitleVO, LinkedHashMap<String, Integer>>();
	        driverUseInfoNum = new LinkedHashMap<String, Integer>();
	        List<List<Order>> driverUseInfo = orderService.getDriverTask(_driver, _beginDate, _endDate);
	        int i = 0;
	        for (List<Order> dayList : driverUseInfo) {
	        	if(dayList!=null && dayList.size()>0){
	        		if(dayList.get(0)!=null)
	        			driverUseInfoNum.put(DateUtils.getYMDString2(DateUtils.getOffsetDate(_beginDate, i++)), 0);
	 				else
	 					driverUseInfoNum.put(DateUtils.getYMDString2(DateUtils.getOffsetDate(_beginDate, i++)), 1);
	     		}else{
	     			driverUseInfoNum.put(DateUtils.getYMDString2(DateUtils.getOffsetDate(_beginDate, i++)), 1);
	     		}
	     	}
	        LineTitleVO ltvo=new LineTitleVO();
	        ltvo.setType("driver");
	        ltvo.setCarId(c.getId());
	        if(c.isCareExpired() || c.isExamineExpired() || c.isInsuranceExpired() || c.isTollChargeExpired())
	        	ltvo.setAvailable(false);
	        else
	        	ltvo.setAvailable(true);
	        ltvo.setDriverName(_driver.getName());
	        ltvo.setServiceType("");
	        ltvo.setPhone(_driver.getPhoneNumber());
	        teMap.put(ltvo, driverUseInfoNum);
	        driverStatus.add(teMap);
        }

        PageBean<LinkedHashMap<LineTitleVO, LinkedHashMap<String, Integer>>> pageBean = new PageBean<LinkedHashMap<LineTitleVO, LinkedHashMap<String, Integer>>>(
             pageNum, 10, driverStatus.size(), driverStatus);
        ActionContext.getContext().getValueStack().push(pageBean);
    }
    
    private void prepareCarData(long _servicePointId, User _driver, Car _car, Date _beginDate, Date _endDate){
    	QueryHelper helper = new QueryHelper(Car.class, "c");
		helper.addWhereCondition("c.status=?", CarStatusEnum.NORMAL);
		if(_car!=null)
			helper.addWhereCondition("c.id=?", _car.getId());
		if(_driver!=null)
			helper.addWhereCondition("c.driver=?", _driver);
		if(_servicePointId==0)
			_servicePointId=1;
		helper.addWhereCondition("c.servicePoint.id=?", _servicePointId);
		PageBean<Car> carPageBean=carService.queryCar(pageNum, helper);
    	List<Car> carList=carPageBean.getRecordList();
    	

    	if(_beginDate==null)
    		_beginDate = new Date();
    	if(_endDate==null)
    		_endDate = DateUtils.getOffsetDate(_beginDate, 4);
        //每一个car保存对应5天的状态信息
        List<LinkedHashMap<LineTitleVO, LinkedHashMap<String, Integer>>> carStatus = new ArrayList<LinkedHashMap<LineTitleVO, LinkedHashMap<String, Integer>>>();
        LinkedHashMap<LineTitleVO, LinkedHashMap<String, Integer>> teMap = null;
        LinkedHashMap<String, Integer> carUseInfoNum = null;
        for (Car car : carList) {
            try {
                teMap = new LinkedHashMap<LineTitleVO, LinkedHashMap<String, Integer>>();
                carUseInfoNum = new LinkedHashMap<String, Integer>();
                List<List<Order>> carUseInfo = orderService.getCarTask(car, _beginDate, _endDate);
                int i = 0;
                for (List<Order> dayList : carUseInfo) {
    				if(dayList!=null && dayList.size()>0){
    					if(dayList.get(0)!=null)
    						carUseInfoNum.put(DateUtils.getYMDString2(DateUtils.getOffsetDate(_beginDate, i++)), 0);
						else
							carUseInfoNum.put(DateUtils.getYMDString2(DateUtils.getOffsetDate(_beginDate, i++)), 1);
    				}else{
    					carUseInfoNum.put(DateUtils.getYMDString2(DateUtils.getOffsetDate(_beginDate, i++)), 1);
    				}
    			}
                LineTitleVO ltvo=new LineTitleVO();
    	        ltvo.setType("car");
    	        ltvo.setCarId(car.getId());
    	        if(car.getDriver()!=null){
    	        	ltvo.setDriverName(car.getDriver().getName());
        	        ltvo.setPhone(car.getDriver().getPhoneNumber());
    	        }
    	        else{
    	        	ltvo.setDriverName("");
    	        	ltvo.setPhone("");
    	        }
    	        if(car.isCareExpired() || car.isExamineExpired() || car.isInsuranceExpired() || car.isTollChargeExpired())
    	        	ltvo.setAvailable(false);
    	        else
    	        	ltvo.setAvailable(true);
    	        ltvo.setServiceType(car.getServiceType().getTitle());
    	        teMap.put(ltvo, carUseInfoNum);
                carStatus.add(teMap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        PageBean<LinkedHashMap<LineTitleVO, LinkedHashMap<String, Integer>>> pageBean = new PageBean<LinkedHashMap<LineTitleVO, LinkedHashMap<String, Integer>>>(
            pageNum, 10, carPageBean.getRecordCount(), carStatus);
        ActionContext.getContext().getValueStack().push(pageBean);
    }

    public String taskList() {
    	ActionContext.getContext().getSession().put("taskList_driver", driver);
    	ActionContext.getContext().getSession().put("taskList_beginDate", beginDate);
    	ActionContext.getContext().getSession().put("taskList_endDate", endDate);
    	ActionContext.getContext().getSession().put("taskList_servicePointId", servicePointId);
    	ActionContext.getContext().getSession().put("taskList_car", car);
    	if(driver!=null){
    		prepareDriverData(driver,beginDate,endDate);
    	}
    	else
    		prepareCarData(servicePointId,driver,car,beginDate,endDate);
    		
     	ActionContext.getContext().put("servicePointList", carService.getAllServicePoint());
    	servicePointId=0;
    	beginDate=null;	//为了不在界面上回显日期
    	endDate=null;
        return "taskList";
    }
    
    public boolean isAvailable(){
		System.out.println("in isAvailable");
    	System.out.println("asdfasfffffffff"+getMap().keySet().toArray(new LineTitleVO[0])[0].isAvailable());
    	return false;
    }
	
	public String freshList(){
		User _driver=(User)ActionContext.getContext().getSession().get("taskList_driver");
		Date _beginDate=(Date)ActionContext.getContext().getSession().get("taskList_beginDate");
		Date _endDate=(Date)ActionContext.getContext().getSession().get("taskList_endDate");
		long _servicePointId=(Long)ActionContext.getContext().getSession().get("taskList_servicePointId");
		Car _car=(Car)ActionContext.getContext().getSession().get("taskList_car");
		if(_driver!=null){
    		prepareDriverData(_driver,_beginDate,_endDate);
    	}else
    		prepareCarData(_servicePointId,_driver,_car,_beginDate,_endDate);
		ActionContext.getContext().put("servicePointList", carService.getAllServicePoint());
		return "taskList";
	}

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }    
    
    public long getDriverId() {
		return driverId;
	}

	public void setDriverId(long driverId) {
		this.driverId = driverId;
	}

	public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }    

	public long getServicePointId() {
		return servicePointId;
	}

	public void setServicePointId(long servicePointId) {
		this.servicePointId = servicePointId;
	}

	public String getCustomerOrganizationName() {
		return customerOrganizationName;
	}

	public void setCustomerOrganizationName(String customerOrganizationName) {
		this.customerOrganizationName = customerOrganizationName;
	}
    
    public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public User getDriver() {
		return driver;
	}

	public void setDriver(User driver) {
		this.driver = driver;
	}

	public LinkedHashMap<LineTitleVO, LinkedHashMap<String, Integer>> getMap() {
		System.out.println("in getMap, map="+map);
		return map;
	}

	public void setMap(
			LinkedHashMap<LineTitleVO, LinkedHashMap<String, Integer>> map) {
		this.map = map;
	}

	public class LineTitleVO{
    	private String type;//如果是查车，type="car";如果是查人，type="driver"
    	private long carId;
    	private String driverName;
    	private boolean available;
    	private String serviceType;
    	private String phone;
    	
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public long getCarId() {
			return carId;
		}
		public void setCarId(long carId) {
			this.carId = carId;
		}
		public String getDriverName() {
			return driverName;
		}
		public void setDriverName(String driverName) {
			this.driverName = driverName;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getServiceType() {
			return serviceType;
		}
		public void setServiceType(String serviceType) {
			this.serviceType = serviceType;
		}
		public boolean isAvailable() {
			return available;
		}
		public void setAvailable(boolean available) {
			this.available = available;
		}    		
    }
}
