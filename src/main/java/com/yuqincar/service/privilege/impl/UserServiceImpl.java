package com.yuqincar.service.privilege.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.struts2.StrutsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.inject.Inject;
import com.yuqincar.dao.car.DriverLicenseDao;
import com.yuqincar.dao.privilege.UserDao;
import com.yuqincar.domain.car.DriverLicense;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.common.TreeNode;
import com.yuqincar.domain.privilege.Department;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.domain.privilege.UserStatusEnum;
import com.yuqincar.domain.privilege.UserTypeEnum;
import com.yuqincar.service.privilege.DepartmentService;
import com.yuqincar.service.privilege.RoleService;
import com.yuqincar.service.privilege.UserService;
import com.yuqincar.utils.QueryHelper;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private DriverLicenseDao driverLicenseDao;
	
	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private RoleService roleService;
	
	public List<User> getByIds(Long[] ids) {
		return userDao.getByIds(ids);
	}

	public List<User> getAll() {
		return userDao.getAll();
	}

	@Transactional
	public void delete(Long id) {
		userDao.delete(id);
	}

	public User getById(Long id) {
		return userDao.getById(id);
	}

	@Transactional
	public void updateDispatchUser(User user) {
		userDao.update(user);
	}
	
	@Transactional
	public void update(User user) {
		User updatedUser = userDao.getById(user.getId());
				
		//办公室员工->司机员工,新建DriverLicense
		if(updatedUser.getUserType() == UserTypeEnum.OFFICE && user.getUserType() == UserTypeEnum.DRIVER){
			driverLicenseDao.save(user.getDriverLicense());
			updatedUser.setDriverLicense(user.getDriverLicense());
		}
		//司机员工->司机员工，修改DriverLicense
		if(updatedUser.getUserType() == UserTypeEnum.DRIVER && user.getUserType() == UserTypeEnum.DRIVER){
			updatedUser.getDriverLicense().setLicenseID(user.getDriverLicense().getLicenseID());
			updatedUser.getDriverLicense().setExpireDate(user.getDriverLicense().getExpireDate());
			driverLicenseDao.update(updatedUser.getDriverLicense());
		}
		//司机员工->办公室员工，删除DriverLicense
		if(updatedUser.getUserType() == UserTypeEnum.DRIVER && user.getUserType() == UserTypeEnum.OFFICE){
			DriverLicense dl=updatedUser.getDriverLicense();
			updatedUser.setDriverLicense(null);
			driverLicenseDao.delete(dl.getId());
		}
		updatedUser.setUserType(user.getUserType());
		
		updatedUser.setRoles(user.getRoles());
		updatedUser.setDepartment(user.getDepartment());
		updatedUser.setBirth(user.getBirth());
		updatedUser.setLoginName(user.getLoginName());
		updatedUser.setName(user.getName());
		updatedUser.setGender(user.getGender());
		updatedUser.setPhoneNumber(user.getPhoneNumber());
		updatedUser.setEmail(user.getEmail());
		updatedUser.setDescription(user.getDescription());
		updatedUser.setStatus(user.getStatus());
		
		userDao.update(updatedUser);
	}

	@Transactional
	public void save(User user) {
		// 默认密码是123456
		String md5 = DigestUtils.md5Hex("123456"); // 密码要使用MD5摘要
		user.setPassword(md5);
		if(user.getUserType()==UserTypeEnum.DRIVER){
			DriverLicense driverLicense = user.getDriverLicense();
			driverLicenseDao.save(driverLicense);
		}
		
		// 保存到数据库
		userDao.save(user);
		
	}
	
	@Transactional
	public void saveDispatchUser(String name,String phoneNumber) {
		
		// 默认密码是123456
		User user =new User();
		Department department = departmentService.getById((long)6);
		String md5 = DigestUtils.md5Hex("123456"); // 密码要使用MD5摘要
		user.setPassword(md5);
		user.setDepartment(department);
		user.setUserType(UserTypeEnum.DRIVER);
		user.setLoginName(name);
		user.setName(name);
		user.setPhoneNumber(phoneNumber);
		user.setStatus(UserStatusEnum.NORMAL);
		
		userDao.save(user);
	}
	
	public User getByLoginNameAndPassword(String loginName, String password) {
		return userDao.getByLoginNameAndPassword(loginName, password);
	}
	
	public User getByLoginNameAndMD5Password(String loginName, String password){
		return userDao.getByLoginNameAndMD5Password(loginName, password);
	}

	
	public PageBean getPageBean(int pageNum, QueryHelper queryHelper) {
		return userDao.getPageBean(pageNum, queryHelper);
	}

	public User getByLoginName(String loginName) {
		return userDao.getByLoginName(loginName);
	}

	
	public TreeNode getUserTree(String name,boolean driverOnly,String departments) {
		List<User> users = new ArrayList<User>();
		List<User> listUser;
		boolean flag = false;
		if(!departments.equals("null") && departments.length()>0){
			String[] ary = departments.split(";");//按照分号分隔字符串
			for(int i=0;i<ary.length;i++){
				listUser=userDao.getByName(name, driverOnly,ary[i]);
				users.addAll(listUser);
			}
		}else{
			users=userDao.getByName(name, driverOnly, departments);
		}
		
		List<TreeNode> nodes = new ArrayList<TreeNode>();
		TreeNode rootNode = new TreeNode();
		rootNode.setName("公司");
		rootNode.setNocheck("true");
		for(User u:users) {
			
				TreeNode child = new TreeNode();
				child.setName(u.getName());
				child.setId(u.getId());
				
				String departmentName = "";
				if(u.getDepartment()!=null && u.getDepartment().getParent() == null){
					nodes.add(child);			
					rootNode.setId(u.getId());
					rootNode.setOpen(flag);
				}
				if(u.getDepartment()!=null && u.getDepartment().getParent() != null) {
					departmentName = u.getDepartment().getName();
				
	
					TreeNode parent = getParentTreeNode(nodes,departmentName);
	
					
					if(parent!=null) {
						if(parent.getChildren()!=null){
							parent.getChildren().add(child);
						}
					} else {
						TreeNode p = new TreeNode();
						p.setId(u.getId());
						p.setName(departmentName);
						p.setNocheck("true");
						p.setChildren(new ArrayList());
						p.getChildren().add(child);
						p.setOpen(flag);
						nodes.add(p);
					}
				}
			}
		rootNode.setChildren(nodes);
		return rootNode;
	}
	
	private TreeNode getParentTreeNode(List<TreeNode> nodes,String departmentName) {
		for(TreeNode node : nodes) {
			if(node.getName().equals(departmentName)) {
				return node;
			}
		}
		return null;
	}

	public void saveDriverLicense(DriverLicense driverLicense) {
		
		driverLicenseDao.save(driverLicense);
		
	}

	public void updateDriverLicense(DriverLicense driverLicense) {
		driverLicenseDao.update(driverLicense);
	}

	public boolean canDeleteUser(Long id) {
		return userDao.canDeleteUser(id);
	}

	public boolean isNameExist(long selfId, String name) {

		return userDao.isNameExist(selfId, name);
	}
	
	public boolean isLoginNameExist(long selfId, String loginName) {

		return userDao.isNameExist(selfId, loginName);
	}

	public List<User> getUserByRoleName(String roleName){
		return userDao.getUsersByRoleName(roleName);
	}
}
