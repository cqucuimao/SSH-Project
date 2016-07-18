package com.yuqincar.service.privilege.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.car.DriverLicenseDao;
import com.yuqincar.dao.privilege.UserDao;
import com.yuqincar.domain.car.DriverLicense;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.common.TreeNode;
import com.yuqincar.domain.privilege.Role;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.domain.privilege.UserGenderEnum;
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
	public void update(User user) {
		DriverLicense driverLicense = user.getDriverLicense();
		userDao.update(user);
	}

	@Transactional
	public void save(User user) {
		// 默认密码是1234
		String md5 = DigestUtils.md5Hex("123456"); // 密码要使用MD5摘要
		user.setPassword(md5);
		DriverLicense driverLicense = user.getDriverLicense();
		driverLicenseDao.save(driverLicense);
		
		// 保存到数据库
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

	
	public List<TreeNode> getUserTree(String name,boolean driverOnly) {
		System.out.println("in getUserTree, driverOnly="+driverOnly);
		List<User> users ;
		boolean flag = false;
		users=userDao.getByName(name, driverOnly);
		for(User user:users)
			System.out.println("userName="+user.getName());
		
		List<TreeNode> nodes = new ArrayList<TreeNode>();
		
		for(User u:users) {
			
			TreeNode child = new TreeNode();
			child.setName(u.getName());
			child.setId(u.getId());
			
			String departmentName = "";
			if(u.getDepartment()!=null) {
				departmentName = u.getDepartment().getName();
			}

			TreeNode parent = getParentTreeNode(nodes,departmentName);
			
			if(parent!=null) {
				if(parent.getChildren()!=null)
					parent.getChildren().add(child);
			} else {
				TreeNode p = new TreeNode();
				p.setId(u.getId());
				p.setName(departmentName);
				p.setChildren(new ArrayList());
				p.getChildren().add(child);
				p.setOpen(flag);
				nodes.add(p);
			}
		}
		
		return nodes;
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
	

}
