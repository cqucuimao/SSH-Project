package com.yuqincar.dao.privilege;

import java.util.List;

import com.yuqincar.dao.common.BaseDao;
import com.yuqincar.domain.privilege.User;

public interface UserDao extends BaseDao<User>{
	public User getByLoginName(String loginName);
	public User getByLoginNameAndPassword(String loginName, String password);
	public User getByLoginNameAndMD5Password(String loginName, String password);
	
	/*供popup查询使用*/
	public List<User> getByRealNameAndDepartment(String username,Long departmentId);
	
	public List<User> getByName(String name,boolean driverOnly);
} 
