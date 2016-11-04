package com.yuqincar.dao.privilege.impl;

import org.springframework.stereotype.Repository;

import com.yuqincar.dao.common.impl.BaseDaoImpl;
import com.yuqincar.dao.privilege.UserDao;
import com.yuqincar.domain.car.Car;
import com.yuqincar.domain.car.CarCare;
import com.yuqincar.domain.car.CarRefuel;
import com.yuqincar.domain.car.CarRepair;
import com.yuqincar.domain.car.CarWash;
import com.yuqincar.domain.common.Company;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.domain.privilege.UserStatusEnum;
import com.yuqincar.domain.privilege.UserTypeEnum;
import com.yuqincar.utils.QueryHelper;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
@Repository
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao{

	public User getByLoginName(String loginName, long companyId) {
		if(StringUtils.isEmpty(loginName) || companyId==0) {
			return null;
		}
		
		return (User)getSession().createQuery("from User where loginName=? and company.id=?")//
					.setParameter(0, loginName).setParameter(1, companyId).uniqueResult();
	}
		
     public List<User> getUsersByLoginName(String loginName){
    	 if(StringUtils.isEmpty(loginName)) {
 			return null;
 		}
 		
 		return (List<User>)getSession().createQuery("from User where loginName=?")//
 					.setParameter(0, loginName).list();
	}
	
	public User getByLoginNameAndPassword(String loginName, String password,long companyId) {
		if(loginName == null || password == null) return null;
		String md5 = DigestUtils.md5Hex(password);
		return getByLoginNameAndMD5Password(loginName,md5,companyId);
	}	
	
	public User getByLoginNameAndMD5Password(String loginName, String password,long companyId){
		if(loginName == null || password == null || companyId==0) 
			return null;
		return (User) getSession().createQuery(
					"FROM User WHERE loginName=? AND password=? AND company.id=?")
					.setParameter(0, loginName)
					.setParameter(1, password)
					.setParameter(2, companyId)
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
					return getSession().createQuery("from User u where u.name like ? and u.userType=? and u.name not like '测试%' and u.status=? and u.department.name=? order by convert_gbk(u.name) asc")
							.setParameter(0, "%"+name+"%").setParameter(1,UserTypeEnum.DRIVER)
							.setParameter(2, UserStatusEnum.NORMAL).setParameter(3, department).list();
				else
					return getSession().createQuery("from User u where u.name like ? and u.name not like '测试%' and u.status=? and u.department.name=? and u.loginName<>'admin' order by convert_gbk(u.name) asc")
							.setParameter(0, "%"+name+"%").setParameter(1, UserStatusEnum.NORMAL)
							.setParameter(2, department).list();
			else
				if(driverOnly)
					return getSession().createQuery("from User u where u.userType=? and u.name not like '测试%' and u.status=? and u.department.name=? order by convert_gbk(u.name) asc")
							.setParameter(0, UserTypeEnum.DRIVER).setParameter(1, UserStatusEnum.NORMAL)
							.setParameter(2, department).list();
				else
					return getSession().createQuery("from User u where u.status=? and u.name not like '测试%' and u.department.name=? and u.loginName<>'admin' order by convert_gbk(u.name) asc")
							.setParameter(0, UserStatusEnum.NORMAL).setParameter(1, department).list();
		}else{
			if(name!=null && !name.isEmpty())
				if(driverOnly)
					return getSession().createQuery("from User u where u.name like ? and u.name not like '测试%' and u.userType=? and u.status=? and u.department.name<>'外派'  order by convert_gbk(u.name) asc")
							.setParameter(0, "%"+name+"%").setParameter(1,UserTypeEnum.DRIVER)
							.setParameter(2, UserStatusEnum.NORMAL).list();
				else
					return getSession().createQuery("from User u where u.name like ? and u.name not like '测试%' and u.status=? and u.loginName<>'admin' and u.department.name<>'外派'  order by convert_gbk(u.name) asc")
							.setParameter(0, "%"+name+"%").setParameter(1, UserStatusEnum.NORMAL).list();
			else
				if(driverOnly)
					return getSession().createQuery("from User u where u.userType=? and u.name not like '测试%' and u.status=? and u.department.name<>'外派' order by convert_gbk(u.name) asc")
							.setParameter(0, UserTypeEnum.DRIVER).setParameter(1, UserStatusEnum.NORMAL).list();
				else
					return getSession().createQuery("from User u where u.status=? and u.name not like '测试%' and u.loginName<>'admin' and u.department.name<>'外派' order by convert_gbk(u.name) asc")
							.setParameter(0, UserStatusEnum.NORMAL).list();
		}
		
	}

	public boolean canDeleteUser(Long id) {
		List<Order> orders = getSession().createQuery("from order_ where driver_id=?").
				setParameter(0, id).list();
		List<CarCare> carCares =getSession().createQuery("from CarCare where driver.id=?").
				setParameter(0, id).list();
		List<CarRefuel> carRefuels = getSession().createQuery("from CarRefuel where driver.id=?").
				setParameter(0,id).list();
		List<CarRepair> carRepairs = getSession().createQuery("from CarRepair where driver.id=?").
				setParameter(0,id).list();
		List<CarWash> carWashs = getSession().createQuery("from CarWash where driver.id=?").
				setParameter(0, id).list();
		if(orders.size() != 0 || carCares.size() != 0 || carRefuels.size() != 0 || carRepairs.size() != 0 || carWashs.size() != 0)
			return false;
		return true;
	}

	public boolean isNameExist(long selfId, String name) {
		if(name == null || name.equals("")){
			return false;
		}
		List<User> users = getSession().createQuery("from User u where u.name=? and u.id<>?")
				.setParameter(0, name)
				.setParameter(1, selfId)
				.list();
		if(users.size() != 0)
			return true;
		return false;
	}
	
	public boolean isLoginNameExist(long selfId, String loginName) {
		if(loginName == null || loginName.equals("")){
			return false;
		}
		List<User> users = getSession().createQuery("from User u where u.loginName=? and u.id<>?")
				.setParameter(0, loginName)
				.setParameter(1, selfId)
				.list();
		if(users.size() != 0)
			return true;
		return false;
	}
	
	public List<User> getUsersByRoleName(String roleName){
		Criteria crit = getSession().createCriteria(User.class)
				.createCriteria("roles");
		crit.add(Restrictions.eq("name", roleName));
		return (List<User>) crit.list();
	}
}
