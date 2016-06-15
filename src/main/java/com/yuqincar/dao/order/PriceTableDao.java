package com.yuqincar.dao.order;

import com.yuqincar.dao.common.BaseDao;
import com.yuqincar.domain.order.PriceTable;

public interface PriceTableDao extends BaseDao<PriceTable> {
	public PriceTable getPriceTableByTitle(String title);
}
