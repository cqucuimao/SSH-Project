package com.yuqincar.dao.privilege.impl;

import org.springframework.stereotype.Repository;

import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.dao.privilege.PrivilegeDao;
import com.yuqincar.domain.privilege.Privilege;

@Repository
public class PrivilegeDaoImpl extends BaseDaoImpl<Privilege> implements PrivilegeDao{

	public Privilege getPrivilegeByUrl(String url){
		return (Privilege)getSession().createQuery("from Privilege as p where p.url=?").setParameter(0, url).uniqueResult();
	}
}
