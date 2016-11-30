package com.yuqincar.service.order;

import java.util.List;

import com.yuqincar.domain.order.Price;
import com.yuqincar.domain.order.PriceTable;

public interface PriceService {
	public List<PriceTable> getAllPriceTable();
	
	public PriceTable copyPriceTable(String title, PriceTable originPriceTable);
	
	public void updatePriceTable(PriceTable priceTable,Price price);
	
	public void addPriceTable(PriceTable priceTable,Price price);
	
	public boolean canDeletePriceTable(Long id);
	
	public void deletePriceTable(Long id);
	
	public PriceTable getPriceTableById(Long id);
	
	public PriceTable getDefaultPriceTable();
	
	public void updatePrice(Price price);
	
	public void deletePrice(PriceTable priceTable,long carServiceTypeId);
}
