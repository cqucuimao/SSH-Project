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
    
    private void prepareDriverData(){
    	if(beginDate==null)
    		beginDate = new Date();
    	if(endDate==null)
    		endDate = DateUtils.getOffsetDate(beginDate, 4);
    	List<LinkedHashMap<LineTitleVO, LinkedHashMap<String, Integer>>> driverStatus = new ArrayList<LinkedHashMap<LineTitleVO, LinkedHashMap<String, Integer>>>();
        LinkedHashMap<LineTitleVO, LinkedHashMap<String, Integer>> teMap = null;
        LinkedHashMap<String, Integer> driverUseInfoNum = null;
         
        teMap = new LinkedHashMap<LineTitleVO, LinkedHashMap<String, Integer>>();
        driverUseInfoNum = new LinkedHashMap<String, Integer>();
        List<List<Order>> driverUseInfo = orderService.getDriverTask(driver, beginDate, endDate);
        int i = 0;
        for (List<Order> dayList : driverUseInfo) {
        	if(dayList!=null && dayList.size()>0){
        		if(dayList.get(0)!=null)
        			driverUseInfoNum.put(DateUtils.getYMDString2(DateUtils.getOffsetDate(beginDate, i++)), 0);
 				else
 					driverUseInfoNum.put(DateUtils.getYMDString2(DateUtils.getOffsetDate(beginDate, i++)), 1);
     		}else{
     			driverUseInfoNum.put(DateUtils.getYMDString2(DateUtils.getOffsetDate(beginDate, i++)), 1);
     		}
     	}
        LineTitleVO ltvo=new LineTitleVO();
        ltvo.setType("driver");
        ltvo.setId(driver.getId());
        ltvo.setDriverName(driver.getName());
        ltvo.setPlateNumber("");
        ltvo.setServiceType("");
        ltvo.setPhone(driver.getPhoneNumber());
        teMap.put(ltvo, driverUseInfoNum);
        driverStatus.add(teMap);

        PageBean<LinkedHashMap<LineTitleVO, LinkedHashMap<String, Integer>>> pageBean = new PageBean<LinkedHashMap<LineTitleVO, LinkedHashMap<String, Integer>>>(
             pageNum, 10, driverStatus.size(), driverStatus);
        ActionContext.getContext().getValueStack().push(pageBean);
    }
    
    private void prepareCarData(){
    	QueryHelper helper = new QueryHelper(Car.class, "c");
		helper.addWhereCondition("c.status=?", CarStatusEnum.NORMAL);
		if(car!=null)
			helper.addWhereCondition("c.id=?", car.getId());
		if(driver!=null)
			helper.addWhereCondition("c.driver=?", driver);
		if(servicePointId==0)
			servicePointId=1;
		helper.addWhereCondition("c.servicePoint.id=?", servicePointId);
    	List<Car> carList=carService.queryCar(pageNum, helper).getRecordList();

    	if(beginDate==null)
    		beginDate = new Date();
    	if(endDate==null)
    		endDate = DateUtils.getOffsetDate(beginDate, 4);
        //每一个car保存对应5天的状态信息
        List<LinkedHashMap<LineTitleVO, LinkedHashMap<String, Integer>>> carStatus = new ArrayList<LinkedHashMap<LineTitleVO, LinkedHashMap<String, Integer>>>();
        LinkedHashMap<LineTitleVO, LinkedHashMap<String, Integer>> teMap = null;
        LinkedHashMap<String, Integer> carUseInfoNum = null;
        System.out.println("carList.size="+carList.size());
        for (Car car : carList) {
            try {
                teMap = new LinkedHashMap<LineTitleVO, LinkedHashMap<String, Integer>>();
                carUseInfoNum = new LinkedHashMap<String, Integer>();
                List<List<Order>> carUseInfo = orderService.getCarTask(car, beginDate, endDate);
                int i = 0;
                System.out.println("carUseInfo.size="+carUseInfo.size());
                for (List<Order> dayList : carUseInfo) {
    				if(dayList!=null && dayList.size()>0){
    					if(dayList.get(0)!=null)
    						carUseInfoNum.put(DateUtils.getYMDString2(DateUtils.getOffsetDate(beginDate, i++)), 0);
						else
							carUseInfoNum.put(DateUtils.getYMDString2(DateUtils.getOffsetDate(beginDate, i++)), 1);
    				}else{
    					carUseInfoNum.put(DateUtils.getYMDString2(DateUtils.getOffsetDate(beginDate, i++)), 1);
    				}
    			}
                LineTitleVO ltvo=new LineTitleVO();
    	        ltvo.setType("car");
    	        ltvo.setId(car.getId());
    	        if(car.getDriver()!=null){
    	        	ltvo.setDriverName(car.getDriver().getName());
        	        ltvo.setPhone(car.getDriver().getPhoneNumber());
    	        }
    	        else{
    	        	ltvo.setDriverName("");
    	        	ltvo.setPhone("");
    	        }
    	        ltvo.setPlateNumber(car.getPlateNumber());
    	        ltvo.setServiceType(car.getServiceType().getTitle());
    	        teMap.put(ltvo, carUseInfoNum);
                carStatus.add(teMap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("carStatus.size="+carStatus.size());
        PageBean<LinkedHashMap<LineTitleVO, LinkedHashMap<String, Integer>>> pageBean = new PageBean<LinkedHashMap<LineTitleVO, LinkedHashMap<String, Integer>>>(
            pageNum, 10, carStatus.size(), carStatus);
        ActionContext.getContext().getValueStack().push(pageBean);
    }

    public String taskList() {
    	if(driver!=null)
    		prepareDriverData();
    	else
    		prepareCarData();
    		
     	ActionContext.getContext().put("servicePointList", carService.getAllServicePoint());
    	servicePointId=0;
    	beginDate=null;	//为了不在界面上回显日期
    	endDate=null;
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

	public class LineTitleVO{
    	private String type;//如果是查车，type="car";如果是查人，type="driver"
    	private long id;
    	private String driverName;
    	private String plateNumber;
    	private String serviceType;
    	private String phone;
    	
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public String getDriverName() {
			return driverName;
		}
		public void setDriverName(String driverName) {
			this.driverName = driverName;
		}
		public String getPlateNumber() {
			return plateNumber;
		}
		public void setPlateNumber(String plateNumber) {
			this.plateNumber = plateNumber;
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
    }
}
