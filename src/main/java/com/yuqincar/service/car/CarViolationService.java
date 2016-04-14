package com.yuqincar.service.car;

import java.math.BigDecimal;
import java.util.Date;

import com.yuqincar.domain.car.CarViolation;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.utils.QueryHelper;

public interface CarViolationService {
	/**
	 * 从www.cqjg.gov.cn去拉去所有车辆的违章信息。如果发现有违章信息，且并没有在数据库中记录，那么就新生成一条
	 * CarViolation的记录。
	 * 需要注意的是：
	 * 1. 该网站会不会对平凡查询的请求予以拒绝？如果会，那么需要每查完一辆车就停顿一段时间（10S	or 30S）。
	 * 2. 改网站的数据格式可能会改变。如果在读取过程中，发现数据格式改变，那么就没必要继续进行。结束前记录log日志，并向项目人员发送短信。
	 */
	public void pullViolationFromCQJG();
	
	public PageBean<CarViolation> queryCarViolation(int pageNum, QueryHelper helper);
	
	public boolean canDealCarViolation(long carViolationId);
	
	public void dealCarViolation(long carViolationId, int penaltyPoint,
			BigDecimal penaltyMoney, Date dealtDate);

	public CarViolation getCarViolationById(long id);
}
