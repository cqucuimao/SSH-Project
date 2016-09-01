package com.yuqincar.service.car.impl;

import java.awt.dnd.DragGestureEvent;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mysql.jdbc.Driver;
import com.yuqincar.action.lbs.GetCarviolationMsg;
import com.yuqincar.dao.car.CarViolationDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarViolation;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.car.CarService;
import com.yuqincar.service.car.CarViolationService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.QueryHelper;
import com.yuqincar.utils.SubStringCharacter;

import net.sf.json.JSONObject;


@Service
public class CarViolationServiceImpl implements CarViolationService {
	
	@Autowired
	private CarViolationDao carViolationDao;
	
	@Autowired
	private CarService carService;
	
	@Autowired
	private UserService userService;
	
	private String carorg;    //管局名称 hangzhou
    private String lsprefix;  //车牌前缀  %e6%b5%99
    private String lsnum;     //车牌剩余部分 AH5b57
    private String lstype;    //车辆类型   02
    private String frameno;   //车架号 根据管局需要输入 229561
    private String engineno;  //发动机号 根据管局需要输入
    private int    iscity;    //是否返回城市 1返回 默认0不返回 不一定100%返回结果，准确度90% town、lat、lng仅供参考
	private String PlateNumber;
	/*
			 参数名称	类型	说明
		lsprefix	string	车牌前缀
		lsnum	string	车牌剩余部分
		carorg	string	管局名称
		usercarid	int	车牌ID
		time	string	时间
		address	string	地点
		content	string	违章内容
		legalnum	string	违章代码
		price	string	罚款金额
		score	string	扣分
		illegalid	int	违章ID
		number	string	违章编号
		agency	string	采集机关
		province	string	省
		city	string	市
		town	string	县
		lat	string	纬度 参考，有误差
		lng	string	经度 参考，有误差

	  */
	@Transactional
	public void pullViolationFromCQJG() throws UnsupportedEncodingException, ParseException {
	    SubStringCharacter sub=new SubStringCharacter(); 
		List<Car> cars=carService.getCarsForPullingViolation();
		for (Car c:cars)
	   {         
		    	 carorg="chongqing";
		    	 PlateNumber=c.getPlateNumber();
		    	 lsprefix=sub.subPre(PlateNumber,2);
		    	 lsnum=sub.subLsnum(lsprefix,PlateNumber);
		    	 lsprefix=URLEncoder.encode(lsprefix,"UTF-8");
		    	 if (c.getSeatNumber()>7) 
		    	 {
		    		 lstype="01";
				 }else
				 {
					 lstype="02"; 
				 }
		    	 //lstype="02";
		    	 /*if(c.isStandbyCar())lstype="02";
		    	 else lstype="01";*/
		    	 
		    	 frameno =c.getVIN();
		    	 if (frameno==null) 
		    	 {
		    		 System.out.println("frameno=: "+frameno+"为空!");
		    		 continue;
		    	 }
		    	 engineno=c.getEngineSN();
		    	 iscity=0;
		    	 System.out.println("**********"+"carorg="+carorg+"lsprefix="+lsprefix+"lsnum="+lsnum+"lstype="+lstype+"frameno="+frameno+"engineno="+engineno+"iscity="+iscity);
		    	 GetCarviolationMsg get=new GetCarviolationMsg();
		    	 String data= get.excute(carorg,lsprefix,lsnum,lstype,frameno,engineno,iscity);
		    	 System.out.println(data); 
			
			//测试数据用的
		         //String string=URLEncoder.encode("渝","UTF-8");
		         //GetCarviolationMsg get=new GetCarviolationMsg();
		         //String data= get.excute("chongqing",string,"CFU007","02","LSGPC54R0AF047043"," ",0);
		     //测试结束    
		    	 JSONObject jsonObject = JSONObject.fromObject(data);
		    	 try {
			        	if(Integer.parseInt(jsonObject.getString("status"))>0)
			        	{
			        		System.out.println("status"+jsonObject.getString("status")+"msg"+jsonObject.getString("msg"));
			        		continue;
			        	}
					} catch (Exception e) {
						continue;
					}
		    	 JSONObject  dataList=jsonObject.getJSONObject("result");
		    	 
		    	try {
		    		 if (dataList.getJSONArray("list")==null) continue;
				} catch (Exception e) {
					continue;
				}
		    	 net.sf.json.JSONArray lists=dataList.getJSONArray("list");
				
		 for(int i=0;i<lists.size();i++)
		   {         System.out.println("****************"+i);
		   			 System.out.println(lists.size());
			    	 JSONObject info=lists.getJSONObject(i);
			         String address=info.getString("address");
			         String content=info.getString("content");
			         String time=info.getString("time");
			         String scorestring=info.getString("score");
			         int  score=Integer.parseInt(scorestring);
			         String moneystring=info.getString("price");
			         BigDecimal money=new BigDecimal(moneystring);
			         //time=time.substring(0, 9);
			         SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd HH:mm");
			         Date date=format.parse(time);
			         
			         //添加违章信息到carViolation数据库里去。 
			          CarViolation carValation=new CarViolation();
			          System.out.println(c.getPlateNumber());
			          
			          if (c.getDriver()!=null) 
			          {
			        	  System.out.println(c.getDriver().getId()+"***************");
				          User driver=userService.getById(c.getDriver().getId());
				          carValation.setDriver(driver);
					  }
			          System.out.println("message"+"date"+date+"address"+address+"content"+content+"score"+score+"money"+money);
			          carValation.setCar(c);
			          carValation.setDate(date);
			          carValation.setPlace(address);
			          carValation.setDescription(content);
			          carValation.setPenaltyPoint(score);
			          carValation.setPenaltyMoney(money);
			          carValation.setDealt(false);
			          carValation.setImported(true);
			          carViolationDao.save(carValation);
		    }
		}
	}

	@Transactional
	public void saveCarViolation(CarViolation carViolation){
		carViolationDao.save(carViolation);
	}
	
	public boolean canUpdateCarViolation(Long id){
		return !carViolationDao.getById(id).isDealt();
	}

	@Transactional
	public void updateCarViolation(CarViolation carViolation){
		carViolationDao.update(carViolation);
	}

	@Transactional
	public void deleteCarViolation(Long id){
		carViolationDao.delete(id);
	}
	
	public PageBean<CarViolation> queryCarViolation(int pageNum, QueryHelper helper) {
		return carViolationDao.getPageBean(pageNum, helper);
	}
	
	public CarViolation getCarViolationById(long id) {
	
		return carViolationDao.getById(id);
	}

	public List<CarViolation> getCarViolationByCar(Car car) {
		
		return carViolationDao.getCarViolationByCar(car);
	}
	
	


}
