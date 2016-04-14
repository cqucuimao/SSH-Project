package com.yuqincar.dao.monitor;

import java.util.List;

import com.yuqincar.dao.common.BaseDao;
import com.yuqincar.domain.monitor.WarningMessage;

public interface WarningMessageDao extends BaseDao<WarningMessage> {
    //获取所有未处理的警告信息
	List<WarningMessage> getUndealedMessages();

	//根据id序列 处理相应的警告记录
	void dealWarnings(Long[] ids);

}
