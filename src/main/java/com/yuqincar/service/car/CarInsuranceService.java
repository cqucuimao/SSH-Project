package com.yuqincar.service.car;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarInsurance;
import com.yuqincar.domain.car.CommercialInsurance;
import com.yuqincar.domain.car.CommercialInsuranceType;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.utils.QueryHelper;

public interface CarInsuranceService {
	/**
	 * 增加保险记录。
	 * 需要设置car.insuranceExpiredDate为toDate
	 * @param carInsurance
	 */
	public void saveCarInsurance(CarInsurance carInsurance,List<CommercialInsuranceType> commercialInsuranceType,List<Date> commercialInsuranceBeginDate,
			List<Date> commercialInsuranceEndDate,List<BigDecimal> commercialInsuranceCoverageMoney,
			List<BigDecimal> commercialInsuranceMoney,int inputRows);
	
	public CarInsurance getCarInsuranceById(long id);
	
	public PageBean<CarInsuranceService> queryCarInsurance(int pageNum , QueryHelper helper);
	
	/**
	 * 列出所有需要提醒保险过期的车辆。条件： insuranceExpiredDate-nowdate<30
	 * 按insuranceExpiredDate升序排列
	 * @return
	 */
	public PageBean<Car> getNeedInsuranceCars(int pageNum);
	
	public BigDecimal statisticCarInsurance(Date fromDate, Date toDate);
	
	public void saveCommercialInsuranceType(CommercialInsuranceType commercialInsuranceType);
	
	public boolean canDeleteCommercialInsuranceType(Long id);
	
	public void deleteCommercialInsuranceType(Long id);
	
	public CommercialInsuranceType getCommercialInsuranceTypeById(Long id);
	
	public void updateCommercialInsuranceType(CommercialInsuranceType commercialInsuranceType);
	
	public List<CommercialInsuranceType> getAllCommercialInsuranceType();
	
	public void saveCommercialInsurance(CommercialInsurance commercialInsurance);
	
	public void deleteCommercialInsurance(Long id);
	/**
	 * 添加商业保险的时候，保险起止时间和金额也要更新
	 * @param commercialInsurances
	 */
	public void addCommercialInsurance(List<CommercialInsurance> commercialInsurances,CarInsurance carInsurance);
}
