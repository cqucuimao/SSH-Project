package com.yuqincar.service.privilege;

import java.util.List;

import com.yuqincar.domain.car.DriverLicense;
import com.yuqincar.domain.common.Company;
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
	
	public User getByLoginNameAndMD5Password(String loginName, String password,long companyId);
	
	List<User> getByIds(Long ids[]);

	/**
	 * 验证用户名与密码，如果正确就返回这个用户，否则返回null
	 * @param loginName
	 * @param password
	 * @return
	 */
	List<User> getUsersByLoginName(String loginName);
	
	User getByLoginNameAndPassword(String loginName, String password,long companyId);

	User getByLoginName(String loginName, long companyId);
	
	User getByName(String name);
	
	List<User> getAllDrivers();

	PageBean getPageBean( int pageNum , QueryHelper helper);
	
	TreeNode getUserTree(String name,boolean driverOnly,String departments);
	
	void saveDriverLicense(DriverLicense driverLicense);
	
	void updateDriverLicense(DriverLicense driverLicense);
	
	void saveDispatchUser(String name,String phoneNumber);
	
	void updateDispatchUser(User user);
	/**
	 * 没有订单、保养、维修、加油、洗车才能删除
	 * @param id
	 * @return
	 */
	boolean canDeleteUser(Long id);
	/**
	 * 姓名是否存在
	 */
	boolean isNameExist(long selfId,String name);
	
	/**
	 * 登录名是否存在
	 */
	boolean isLoginNameExist(long selfId,String loginName);
	
	public List<User> getUserByRoleName(String roleName);
	
	/**
	 * 获取具有相同登录名用户的所有公司
	 */
	public List<Company> getUserCompanys(String loginName);
}
