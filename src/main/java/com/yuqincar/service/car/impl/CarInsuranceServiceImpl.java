package com.yuqincar.service.car.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.car.CarDao;
import com.yuqincar.dao.car.CarInsuranceDao;
import com.yuqincar.dao.car.CommercialInsuranceDao;
import com.yuqincar.dao.car.CommercialInsuranceTypeDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarInsurance;
import com.yuqincar.domain.car.CarStatusEnum;
import com.yuqincar.domain.car.CommercialInsurance;
import com.yuqincar.domain.car.CommercialInsuranceType;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.service.car.CarInsuranceService;
import com.yuqincar.utils.QueryHelper;

@Service
public class CarInsuranceServiceImpl implements CarInsuranceService {
	
	@Autowired
	private CarInsuranceDao carInsuranceDao;
	@Autowired
	private CommercialInsuranceTypeDao commercialInsuranceTypeDao;
	@Autowired
	private CommercialInsuranceDao commercialInsuranceDao;
	@Autowired
	private CarDao carDao;

	@Transactional
	public void saveCarInsurance(CarInsurance carInsurance,List<CommercialInsuranceType> commercialInsuranceType,List<Date> commercialInsuranceBeginDate,
			List<Date> commercialInsuranceEndDate,List<BigDecimal> commercialInsuranceCoverageMoney,
			List<BigDecimal> commercialInsuranceMoney,int inputRows) {
		
		carInsuranceDao.save(carInsurance);
		
		carInsurance.getCar().setInsuranceExpiredDate(carInsurance.getToDate());
		if(carInsurance.getCar().getInsuranceExpiredDate().after(new Date()))
			carInsurance.getCar().setInsuranceExpired(false);
		else
			carInsurance.getCar().setInsuranceExpired(true);
		carDao.update(carInsurance.getCar());
		
		for(int i=0;i<inputRows;i++){
			CommercialInsurance commercialInsurance = new CommercialInsurance();
			commercialInsurance.setInsurance(carInsurance);
			commercialInsurance.setCommercialInsuranceType(commercialInsuranceType.get(i));
			if(commercialInsuranceBeginDate.get(i).before(commercialInsuranceEndDate.get(i))){
				commercialInsurance.setCommercialInsuranceBeginDate(commercialInsuranceBeginDate.get(i));
				commercialInsurance.setCommercialInsuranceEndDate(commercialInsuranceEndDate.get(i));
			}			
			commercialInsurance.setCommercialInsuranceCoverageMoney(commercialInsuranceCoverageMoney.get(i));
			commercialInsurance.setCommercialInsuranceMoney(commercialInsuranceMoney.get(i));
			commercialInsuranceDao.save(commercialInsurance);
		}
	}
	@Transactional
	public void addCommercialInsurance(List<CommercialInsurance> commercialInsurances,CarInsurance carInsurance) {
		
		for(int i=0;i<commercialInsurances.size();i++){
			commercialInsuranceDao.save(commercialInsurances.get(i));
		}
		carInsuranceDao.update(carInsurance);
	}

	public CarInsurance getCarInsuranceById(long id) {
		return carInsuranceDao.getById(id);
	}

	public PageBean<CarInsuranceService> queryCarInsurance(int pageNum, QueryHelper helper) {
		return carInsuranceDao.getPageBean(pageNum, helper);
	}
	
	public PageBean<Car> getNeedInsuranceCars(int pageNum){
		Date now = new Date();
		Date a = new Date(now.getTime() +  24*60*60*1000 * 30L );
		QueryHelper helper = new QueryHelper(Car.class, "c");
		helper.addWhereCondition("c.insuranceExpiredDate < ? and c.status=? and c.borrowed=?", a,CarStatusEnum.NORMAL,false);
		helper.addOrderByProperty("c.insuranceExpiredDate", true);
		return carDao.getPageBean(pageNum, helper);		
	}
	
	public BigDecimal statisticCarInsurance(Date fromDate, Date toDate){
		return carInsuranceDao.statisticCarInsurance(fromDate, toDate);
	}
	@Transactional	
	public void saveCommercialInsuranceType(CommercialInsuranceType commercialInsuranceType){
		commercialInsuranceTypeDao.save(commercialInsuranceType);
	}
	
	public boolean canDeleteCommercialInsuranceType(Long id){
		return commercialInsuranceTypeDao.canDeleteCommercialInsuranceType(id);
	}
	@Transactional
	public void deleteCommercialInsuranceType(Long id){
		commercialInsuranceTypeDao.delete(id);
	}
	
	public CommercialInsuranceType getCommercialInsuranceTypeById(Long id){
		return commercialInsuranceTypeDao.getById(id);
	}
	@Transactional
	public void updateCommercialInsuranceType(CommercialInsuranceType commercialInsuranceType){
		commercialInsuranceTypeDao.update(commercialInsuranceType);
	}
	
	public List<CommercialInsuranceType> getAllCommercialInsuranceType(){
		return commercialInsuranceTypeDao.getAll();
	}
	@Transactional
	public void saveCommercialInsurance(CommercialInsurance commercialInsurance){
		commercialInsuranceDao.save(commercialInsurance);
	}
	@Transactional
	public void deleteCommercialInsurance(Long id){
		commercialInsuranceDao.delete(id);
	}

	

}
