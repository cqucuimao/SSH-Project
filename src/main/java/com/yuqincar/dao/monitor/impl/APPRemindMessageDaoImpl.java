package com.yuqincar.dao.monitor.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.dao.monitor.APPRemindMessageDao;
import com.yuqincar.domain.order.APPRemindMessage;
import com.yuqincar.domain.privilege.User;
@Repository
public class APPRemindMessageDaoImpl extends BaseDaoImpl<APPRemindMessage> implements APPRemindMessageDao {

	public List<APPRemindMessage> getAllUnsendedRemindMessage(User user) {
		return getSession().createQuery("from APPRemindMessage as a where a.sended = false and a.user=?")//
				.setParameter(0, user)
				.list();
	}

	/**
	 * 将参数message中的sended设置为true，以表示成功发送到了APP端。
	 * @param message
	 */
	public void setRemindMessageSended(APPRemindMessage message) {
		message.setSended(true);
		this.update(message);
	}

}
