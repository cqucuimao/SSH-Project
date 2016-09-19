package com.yuqincar.action.monitor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionContext;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.monitor.WarningMessage;
import com.yuqincar.domain.monitor.WarningMessageTypeEnum;
import com.yuqincar.domain.order.OrderStatement;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.monitor.WarningMessageService;
import com.yuqincar.utils.DateUtils;
import com.yuqincar.utils.QueryHelper;

@Controller
@Scope("prototype")
public class AlarmAction extends BaseAction{

	@Autowired
	WarningMessageService warningMessageService;
	@Autowired
	CarService carService;
	
	private String driverName;
	private String plateNumber;
	private Date beginDate;
	private Date endDate;
	private String carId;
	private String warningType;
	private String warningIds;
	private Car car;
	private User driver;
	
	//查询未处理的警告信息
	public void getUndealedMessages(){
		System.out.println("in getUndealedMessages");
		String jsonStr=null;
		List<WarningMessage> messages=warningMessageService.getUndealedMessages();
		if(messages.size()==0)
		   jsonStr="{\"messages\":null,"+"\"status\":0}";
		else{
		   List<MessageVO> messageList=parseMessages(messages);
		   jsonStr="{\"messages\":"+JSON.toJSONString(messageList)+","+"\"status\":1}";
		}
		this.writeJson(jsonStr);
	}
	
	//处理警告信息
	public void dealWarnings(){
		String[] strIds=warningIds.split(",");
		Long[] ids=new Long[strIds.length];
		for(int i=0;i<ids.length;i++){
			ids[i]=Long.parseLong(strIds[i]);
		}
		warningMessageService.dealWarnings(ids);
		this.writeJson("{\"status\":\"1\"}");
	}
	
	/**
	 * 返回主页面列表信息
	 * @return
	 */
	public String home(){
		
		QueryHelper helper = new QueryHelper(WarningMessage.class, "w");
		//设置司机名称
	    if (driver!=null) {
			 helper.addWhereCondition("w.car.driver=?", driver);
		}
	    //设置车牌号
	    if (car!=null) {
			 helper.addWhereCondition("w.car=?", car);
		}
	    //设置查询的时间范围
	    //设置开始时间
	    if (beginDate != null) {
	 		helper.addWhereCondition("w.date>=?", beginDate);
	 	}
	 	// 设置结束时间
	 	if (endDate != null) {
	 		helper.addWhereCondition("w.date<=?", endDate);
	 	}
	    PageBean<OrderStatement> pageBean = warningMessageService.getPageBean(pageNum, helper);
	    ActionContext.getContext().getValueStack().push(pageBean);
		
		return "home";
	}
	
	public String getWarningIds() {
		return warningIds;
	}
	public void setWarningIds(String warningIds) {
		this.warningIds = warningIds;
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
	
	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getWarningType() {
		return warningType;
	}

	public void setWarningType(String warningType) {
		this.warningType = warningType;
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

	public List<MessageVO> parseMessages(List<WarningMessage> messages){
		int size=messages.size();
		List<MessageVO> messageList=new ArrayList<MessageVO>(size);
		for(int i=0;i<size;i++){
			MessageVO message=new MessageVO();
			message.setId(messages.get(i).getId().toString());
			message.setPlateNumber(messages.get(i).getCar().getPlateNumber());
			if(messages.get(i).getCar().getDriver()!=null){
				message.setDriverName(messages.get(i).getCar().getDriver().getName());
				message.setPhoneNumber(messages.get(i).getCar().getDriver().getPhoneNumber());
			}else{
				message.setDriverName("");
				message.setPhoneNumber("");
			}
			message.setDate(DateUtils.getYMDHMSString(messages.get(i).getDate()));
			message.setCarId(messages.get(i).getCar().getId().toString());
			String type=null;
			if(messages.get(i).getType().equals(WarningMessageTypeEnum.PULLEDOUT))
				type="设备拔出";
			else
				type="异常行驶";
			message.setType(type);
			messageList.add(message);
		}
		return messageList;
	}
	
	class MessageVO{
		private String id;
		private String plateNumber;
		private String driverName;
		private String phoneNumber;
		private String date;
		private String type;
		private String carId;
		
		public String getCarId() {
			return carId;
		}
		public void setCarId(String carId) {
			this.carId = carId;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getPlateNumber() {
			return plateNumber;
		}
		public void setPlateNumber(String plateNumber) {
			this.plateNumber = plateNumber;
		}
		public String getDriverName() {
			return driverName;
		}
		public void setDriverName(String driverName) {
			this.driverName = driverName;
		}
		public String getPhoneNumber() {
			return phoneNumber;
		}
		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
	}
}
