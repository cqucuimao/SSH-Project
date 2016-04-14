package com.yuqincar.dao.monitor;

import com.yuqincar.dao.common.BaseDao;
import com.yuqincar.domain.monitor.TemporaryWarning;

public interface TemporaryWarningDao extends BaseDao<TemporaryWarning> {

	/**
	 * 查看相应的临时警告记录是否存在
	 * @param carId
	 * @return
	 */
	boolean isTempMessageExist(Long carId);

	/**
	 * 根据车辆id删除相应临时记录
	 * @param carId
	 */
	void deleteTempMessage(Long carId);

	/**
	 * 添加临时警告记录
	 * @param carId
	 */
	void addTempWarningMessage(Long carId);

}
