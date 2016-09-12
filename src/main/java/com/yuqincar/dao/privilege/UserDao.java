package com.yuqincar.dao.privilege;

import java.util.List;

import com.yuqincar.dao.common.BaseDao;
import com.yuqincar.domain.privilege.User;

public interface UserDao extends BaseDao<User>{
	public User getByLoginName(String loginName);
	public User getByLoginNameAndPassword(String loginName, String password);
	public User getByLoginNameAndMD5Password(String loginName, String password);		
	public List<User> getByName(String name,boolean driverOnly,String department);
	public boolean canDeleteUser(Long id);
	public boolean isNameExist(long selfId,String name);
	public boolean isLoginNameExist(long selfId,String loginName);
	public List<User> getUsersByRoleName(String roleName);
} 
