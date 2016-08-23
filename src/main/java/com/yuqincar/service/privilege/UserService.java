package com.yuqincar.service.privilege;

import java.util.List;

import com.yuqincar.domain.car.DriverLicense;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.common.TreeNode;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.base.BaseService;
import com.yuqincar.utils.QueryHelper;

public interface UserService extends BaseService{
	/**
	 * 查询所有的用户信息
	 * @return
	 */
	List<User> getAll();

	/**
	 * 删除用户信息
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 保存
	 * @param user
	 */
	void save(User user);

	/**
	 * 获取
	 * @param id
	 * @return
	 */
	User getById(Long id);

	/**
	 * 更新
	 * @param user
	 */
	void update(User user);
	
	List<User> getByIds(Long ids[]);

	/**
	 * 验证用户名与密码，如果正确就返回这个用户，否则返回null
	 * @param loginName
	 * @param password
	 * @return
	 */
	User getByLoginNameAndPassword(String loginName, String password);
	
	User getByLoginNameAndMD5Password(String loginName, String password);
	
	User getByLoginName(String loginName);

	PageBean getPageBean( int pageNum , QueryHelper helper);
	
	TreeNode getUserTree(String name,boolean driverOnly,String departments);
	
	void saveDriverLicense(DriverLicense driverLicense);
	
	void updateDriverLicense(DriverLicense driverLicense);
	
	void saveDispatchUser(String name,String phoneNumber);
	
	void updateDispatchUser(User user);
}
