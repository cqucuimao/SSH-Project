package com.yuqincar.action.monitor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.Car;
import com.yuqincar.service.car.CarService;
import com.yuqincar.utils.Configuration;
import com.yuqincar.utils.DateUtils;

@Controller
@Scope("prototype")
public class ReplayAction extends BaseAction{
	
	private Long carId;
	
	private Date beginTime;
	
	private Date endTime;
	
	private String baiduKey;
		
	@Autowired
	private CarService carService;
	
	private Car car;

	/**
	 * 返回主页面列表信息
	 * @return
	 */
	public String home(){
		if(carId!=null){
			car=carService.getCarById(carId);
			beginTime=DateUtils.getMinDate(new Date());
			endTime=new Date();
		}
		return "home";
	}
	
	/**
	 * 返回json信息
	 * @return
	 */
	public void list(){
        List<SnVO> snsVO=new ArrayList<SnVO>();
		
		if(car!=null){
			snsVO.add(parseCar(car));
		}
		
		String jsonStr="{\"sns\":"+JSON.toJSONString(snsVO)+"}"; 
		this.writeJson(jsonStr);
	}
	
	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}
	
	
	public String getBaiduKey() {
		return Configuration.getBaiduKey();
	}

	public void setBaiduKey(String baiduKey) {
		this.baiduKey = baiduKey;
	}

	public SnVO parseCar(Car car){
		SnVO snVO=new SnVO();
		snVO.setSn(car.getDevice().getSN());
		return snVO;
	}
	
	public List<SnVO> parseCars(List<Car> cars){
		
		List<SnVO> snsVO=new ArrayList<SnVO>(cars.size());
		for(int i=0;i<cars.size();i++){
			SnVO snVO=new SnVO();
			snVO.setSn(cars.get(i).getDevice().getSN());
			snsVO.add(snVO);
		}
		return snsVO;
	}
	
	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	class SnVO{
		private String sn;     //设备sn号

		public String getSn() {
			return sn;
		}

		public void setSn(String sn) {
			this.sn = sn;
		}
	}
}
