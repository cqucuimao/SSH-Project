package com.yuqincar.service.monitor;

import java.util.Date;
import java.util.List;

import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.monitor.WarningMessage;
import com.yuqincar.domain.monitor.WarningMessageTypeEnum;
import com.yuqincar.domain.order.OrderStatement;
import com.yuqincar.utils.QueryHelper;

public interface WarningMessageService {

	PageBean<OrderStatement> getPageBean(int pageNum, QueryHelper helper);

	//添加报警信息
	void addWarningMessage(Long carId, Date date, WarningMessageTypeEnum pulledout);

	//判断当前车辆的临时警告记录是否存在
	boolean isTempMessageExist(Long carId);

	//删除临时警告记录
	void deleteTempMessage(Long long1);

	//添加临时警告记录
	void addTempWarningMessage(Long carId);

	//获取未处理的警告信息
	List<WarningMessage> getUndealedMessages();

	//根据id序列处理相应的警告记录
	void dealWarnings(Long[] ids);

}
