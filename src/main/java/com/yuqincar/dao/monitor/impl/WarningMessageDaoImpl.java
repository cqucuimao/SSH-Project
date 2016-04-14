package com.yuqincar.dao.monitor.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.dao.monitor.WarningMessageDao;
import com.yuqincar.domain.monitor.WarningMessage;

@Repository
public class WarningMessageDaoImpl extends BaseDaoImpl<WarningMessage> implements WarningMessageDao {

	public List<WarningMessage> getUndealedMessages() {
		return getSession().createQuery("from WarningMessage w where w.dealed=?").
				setParameter(0, false).list();
	}

	public void dealWarnings(Long[] ids) {
        for(int i=0;i<ids.length;i++){
        	WarningMessage warning=getById(ids[i]);
        	warning.setDealed(true);
        	save(warning);
        }		
	}
}
