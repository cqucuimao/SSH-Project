package com.yuqincar.dao.monitor;

import java.util.List;

import com.yuqincar.dao.common.BaseDao;
import com.yuqincar.domain.order.APPRemindMessage;
import com.yuqincar.domain.privilege.User;

public interface APPRemindMessageDao extends BaseDao<APPRemindMessage> {

	List<APPRemindMessage> getAllUnsendedRemindMessage(User user);

	void setRemindMessageSended(APPRemindMessage message);

}
