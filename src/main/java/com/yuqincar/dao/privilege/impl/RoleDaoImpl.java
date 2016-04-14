package com.yuqincar.dao.privilege.impl;

import org.springframework.stereotype.Repository;

import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.dao.privilege.RoleDao;
import com.yuqincar.domain.order.Customer;
import com.yuqincar.domain.privilege.Role;

@Repository
public class RoleDaoImpl extends BaseDaoImpl<Role> implements RoleDao {
	public Role getRoleByName(String name){
		return (Role)getSession().createQuery("from Role as r where r.name=?").
				setParameter(0,name).uniqueResult();
	}
}
