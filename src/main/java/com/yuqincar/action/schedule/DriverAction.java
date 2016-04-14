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
import com.yuqincar.domain.order.CustomerOrganization;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.CustomerOrganization.CustomerOrganizationService;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.order.OrderService;
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
    private CustomerOrganizationService customerOrganizationService;
    
    
    private long driverId;
    private String plateNumber;
    private Date   beginDate;
    private Date   endDate;
    private long servicePointId;
    
    private String customerOrganizationName;

    public String taskList() {
		QueryHelper helper = new QueryHelper(Car.class, "c");
		helper.addWhereCondition("c.status=?", CarStatusEnum.NORMAL);
		if(driverId>0)
			helper.addWhereCondition("c.driver.id=?", driverId);
		if(plateNumber!=null && !plateNumber.isEmpty())
			helper.addWhereCondition("c.plateNumber=?", plateNumber);
		if(servicePointId==0)
			servicePointId=1;
		helper.addWhereCondition("c.servicePoint.id=?", servicePointId);
    	List<Car> carList=carService.queryCar(pageNum, helper).getRecordList();
    	ActionContext.getContext().put("servicePointList", carService.getAllServicePoint());

    	System.out.println("beginDate="+beginDate);
    	System.out.println("endDate="+endDate);
    	if(beginDate==null)
    		beginDate = new Date();
    	if(endDate==null)
    		endDate = DateUtils.getOffsetDate(beginDate, 4);
        //每一个car保存对应5天的状态信息
        List<LinkedHashMap<Car, LinkedHashMap<String, Integer>>> carStatus = new ArrayList<LinkedHashMap<Car, LinkedHashMap<String, Integer>>>();
        LinkedHashMap<Car, LinkedHashMap<String, Integer>> teMap = null;
        LinkedHashMap<String, Integer> carUseInfoNum = null;
        for (Car car : carList) {
            try {
                teMap = new LinkedHashMap<Car, LinkedHashMap<String, Integer>>();
                carUseInfoNum = new LinkedHashMap<String, Integer>();
                List<List<BaseEntity>> carUseInfo = orderService.getCarTask(car, beginDate, endDate);
                int i = 0;
                for (List<BaseEntity> dayList : carUseInfo) {
    				if(dayList!=null && dayList.size()>0){
    					BaseEntity baseEntity=dayList.get(0);//用最前面的实体作为显示依据。
    					if (baseEntity instanceof CarCare) {
    						carUseInfoNum.put(DateUtils.getYMDString2(DateUtils
    								.getOffsetDate(beginDate, i++)), 0);
    					} else if (baseEntity instanceof CarRepair) {
    						carUseInfoNum.put(DateUtils.getYMDString2(DateUtils
    								.getOffsetDate(beginDate, i++)), 1);
    					} else if (baseEntity instanceof CarExamine) {
    						carUseInfoNum.put(DateUtils.getYMDString2(DateUtils
    								.getOffsetDate(beginDate, i++)), 2);
    					} else if (baseEntity instanceof Order) {
    						carUseInfoNum.put(DateUtils.getYMDString2(DateUtils
    								.getOffsetDate(beginDate, i++)), 3);
    					} else {
    						carUseInfoNum.put(DateUtils.getYMDString2(DateUtils
    								.getOffsetDate(beginDate, i++)), 4);
    					}
    				}else{
    					carUseInfoNum.put(DateUtils.getYMDString2(DateUtils
    							.getOffsetDate(beginDate, i++)), 4);
    				}
    			}
                teMap.put(car, carUseInfoNum);
                carStatus.add(teMap);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        PageBean<LinkedHashMap<Car, LinkedHashMap<String, Integer>>> pageBean = new PageBean<LinkedHashMap<Car, LinkedHashMap<String, Integer>>>(
            pageNum, 10, carStatus.size(), carStatus);
        ActionContext.getContext().getValueStack().push(pageBean);
    	System.out.println("driverId="+driverId);
    	System.out.println("plateNumber="+plateNumber);
    	System.out.println("servicePointId="+servicePointId);
    	System.out.println("beginDate="+beginDate);
    	System.out.println("endDate="+endDate);
    	driverId=0;
    	plateNumber=null;
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
    
    
}
