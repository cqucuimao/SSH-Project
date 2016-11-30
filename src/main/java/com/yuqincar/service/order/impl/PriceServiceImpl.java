package com.yuqincar.service.order.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.order.PriceDao;
import com.yuqincar.dao.order.PriceTableDao;
import com.yuqincar.domain.car.CarServiceType;
import com.yuqincar.domain.order.CustomerOrganization;
import com.yuqincar.domain.order.Price;
import com.yuqincar.domain.order.PriceTable;
import com.yuqincar.service.order.PriceService;

@Service
public class PriceServiceImpl implements PriceService {
	@Autowired
	private PriceDao priceDao;
	@Autowired
	private PriceTableDao priceTableDao;

	public List<PriceTable> getAllPriceTable() {
		return priceTableDao.getAll();
	}

	@Transactional
	public PriceTable copyPriceTable(String title, PriceTable originPriceTable) {
		PriceTable priceTable=new PriceTable();
		priceTable.setTitle(title);
		Map<CarServiceType,Price> map=new HashMap<CarServiceType,Price>();
		for(CarServiceType cst:originPriceTable.getCarServiceType().keySet()){
			Price p=originPriceTable.getCarServiceType().get(cst);
			Price price=new Price();
			price.setPerDay(p.getPerDay());
			price.setPerHalfDay(p.getPerHalfDay());
			price.setPerHourAfterLimit(p.getPerHourAfterLimit());
			price.setPerMileAfterLimit(p.getPerMileAfterLimit());
			price.setPerPlaneTime(p.getPerPlaneTime());
			priceDao.save(price);
			map.put(cst, price);
		}
		priceTable.setCarServiceType(map);
		
		priceTableDao.save(priceTable);
		return priceTable;
	}

	@Transactional
	public void addPriceTable(PriceTable priceTable,Price price) {
		priceDao.save(price);
		priceTableDao.update(priceTable);
	}
	
	@Transactional
	public void updatePriceTable(PriceTable priceTable,Price price) {
		priceDao.update(price);
		priceTableDao.update(priceTable);
	}
	
	@Transactional
	public void deletePrice(PriceTable priceTable,long carServiceTypeId) {
		priceDao.delete(carServiceTypeId);
		priceTableDao.update(priceTable);
	}

	public boolean canDeletePriceTable(Long id) {
		return !priceTableDao.getDefaultPriceTable().getId().equals(id);
	}

	@Transactional
	public void deletePriceTable(Long id) {
		priceTableDao.delete(id);
	}

	public PriceTable getPriceTableById(Long id) {
		return priceTableDao.getById(id);
	}

	public PriceTable getDefaultPriceTable() {
		return priceTableDao.getDefaultPriceTable();
	}

	@Transactional
	public void updatePrice(Price price) {
		priceDao.update(price);
	}

}
