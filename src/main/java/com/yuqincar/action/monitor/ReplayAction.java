package com.yuqincar.action.monitor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ModelDriven;
import com.yuqincar.action.common.BaseAction;
import com.yuqincar.domain.car.Car;
import com.yuqincar.service.car.CarService;

@Controller
@Scope("prototype")
public class ReplayAction extends BaseAction implements ModelDriven<Car>{
	
	private String carPlateNumber; 
    public String getCarPlateNumber() {
		return carPlateNumber;
	}

	public void setCarPlateNumber(String carPlateNumber) {
		this.carPlateNumber = carPlateNumber;
	}

	private Car model=new Car();
	
	@Autowired
	private CarService carService;

	/**
	 * 返回主页面列表信息
	 * @return
	 */
	public String home(){
		model.setPlateNumber(carPlateNumber);
		return "home";
	}
	
	/**
	 * 返回json信息
	 * @return
	 */
	public void list(){
		
		String driverName=model.getDriver().getName();
		String plateNumber=model.getPlateNumber();
		
        List<SnVO> snsVO=new ArrayList<SnVO>();
		
		if(!"".equals(driverName)&&!"".equals(plateNumber)){
			Car car= carService.findByDriverNameAndPlateNumber(driverName, plateNumber);
			SnVO snVO=null;
			if(car!=null){
			   snVO=parseCar(car);
			}
			snsVO.add(snVO);
		}else if(!"".equals(driverName)){
			List<Car> cars=carService.findByDriverName(driverName);
			snsVO=parseCars(cars);
		}else if(!"".equals(plateNumber)){
			Car car=carService.getCarByPlateNumber(plateNumber);
			SnVO snVO=null;
			if(car!=null){
			   snVO=parseCar(car);
			}
			snsVO.add(snVO);
		}
		
		String jsonStr="{\"sns\":"+JSON.toJSONString(snsVO)+"}"; 
		this.writeJson(jsonStr);
	}
	
	public Car getModel() {
		return model;
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
