package com.yuqincar.dao.privilege.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.dao.privilege.DepartmentDao;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.privilege.Department;
import com.yuqincar.domain.privilege.User;

@Repository
public class DepartmentDaoImpl extends BaseDaoImpl<Department> implements DepartmentDao{

	public List<Department> findChildren(Long parentId) {
		return getSession().createQuery(//
				"FROM Department d WHERE d.parent.id=?")//
				.setParameter(0, parentId)//
				.list();
	}

	public List<Department> findTopList() {
		return getSession().createQuery(//
				"FROM Department d WHERE d.parent IS NULL")//
				.list();
	}

	public boolean canDeleteDepartment(Long id) {
		
		List<User> users = getSession().createQuery("from User where department.id=?").
				setParameter(0, id).list();
		if(users.size() != 0)
			return false;
		return true;
	}

	public Department getDepartmentByName(String name) {
		
		return (Department)getSession().createQuery(//
				"FROM Department d WHERE d.name=?")//
				.setParameter(0, name)
				.uniqueResult();
	}

}
