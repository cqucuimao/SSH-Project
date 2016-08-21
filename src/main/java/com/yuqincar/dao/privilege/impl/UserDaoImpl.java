package com.yuqincar.dao.privilege.impl;

import org.springframework.stereotype.Repository;

import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.dao.privilege.UserDao;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.domain.privilege.UserStatusEnum;
import com.yuqincar.domain.privilege.UserTypeEnum;
import com.yuqincar.utils.QueryHelper;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
@Repository
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao{

	public User getByLoginName(String loginName) {
		if(StringUtils.isEmpty(loginName)) {
			return null;
		}
		
		return (User)getSession().createQuery("from User u where u.loginName=?")//
					.setParameter(0, loginName).uniqueResult();
	}

	public User getByLoginNameAndPassword(String loginName, String password) {
		if(loginName == null || password == null) return null;
		String md5 = DigestUtils.md5Hex(password);
		return getByLoginNameAndMD5Password(loginName,md5);
	}
	
	public User getByLoginNameAndMD5Password(String loginName, String password){
		if(loginName == null || password == null) 
			return null;
		return (User) getSession().createQuery(
				"FROM User u WHERE u.loginName=? AND u.password=?")
				.setParameter(0, loginName)
				.setParameter(1, password)
				.uniqueResult();
	}

	public List<User> getByRealNameAndDepartment(String username, Long departmentId) {
		QueryHelper helper = new QueryHelper(User.class, "u");

		if(username!=null && !"".equals(username))
			helper.addWhereCondition("u.name like ?", "%"+username+"%");
		else if(departmentId!=null && departmentId>0) {
			helper.addWhereCondition("u.department.id = ?", departmentId);
		}
		return (List<User>) getSession().createQuery(helper.getQueryListHql());
	}

	public List<User> getByName(String name,boolean driverOnly,String department) {
		if(!department.equals("null") && department.length()>0){
			if(name!=null && !name.isEmpty())
				if(driverOnly)
					return getSession().createQuery("from User u where u.name like ? and u.userType=? and u.status=? and u.department.name=? order by convert_gbk(u.name) asc")
							.setParameter(0, "%"+name+"%").setParameter(1,UserTypeEnum.DRIVER)
							.setParameter(2, UserStatusEnum.NORMAL).setParameter(3, department).list();
				else
					return getSession().createQuery("from User u where u.name like ? and u.status=? and u.department.name=? and u.loginName<>'admin' order by convert_gbk(u.name) asc")
							.setParameter(0, "%"+name+"%").setParameter(1, UserStatusEnum.NORMAL)
							.setParameter(2, department).list();
			else
				if(driverOnly)
					return getSession().createQuery("from User u where u.userType=? and u.status=? and u.department.name=? order by convert_gbk(u.name) asc")
							.setParameter(0, UserTypeEnum.DRIVER).setParameter(1, UserStatusEnum.NORMAL)
							.setParameter(2, department).list();
				else
					return getSession().createQuery("from User u where u.status=? and u.department.name=? and u.loginName<>'admin' order by convert_gbk(u.name) asc")
							.setParameter(0, UserStatusEnum.NORMAL).setParameter(1, department).list();
		}else{
			if(name!=null && !name.isEmpty())
				if(driverOnly)
					return getSession().createQuery("from User u where u.name like ? and u.userType=? and u.status=? order by convert_gbk(u.name) asc")
							.setParameter(0, "%"+name+"%").setParameter(1,UserTypeEnum.DRIVER)
							.setParameter(2, UserStatusEnum.NORMAL).list();
				else
					return getSession().createQuery("from User u where u.name like ? and u.status=? and u.loginName<>'admin' order by convert_gbk(u.name) asc")
							.setParameter(0, "%"+name+"%").setParameter(1, UserStatusEnum.NORMAL).list();
			else
				if(driverOnly)
					return getSession().createQuery("from User u where u.userType=? and u.status=? order by convert_gbk(u.name) asc")
							.setParameter(0, UserTypeEnum.DRIVER).setParameter(1, UserStatusEnum.NORMAL).list();
				else
					return getSession().createQuery("from User u where u.status=? and u.loginName<>'admin' order by convert_gbk(u.name) asc")
							.setParameter(0, UserStatusEnum.NORMAL).list();
		}
		
	}
	

}
