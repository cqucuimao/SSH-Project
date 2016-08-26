package com.yuqincar.service.privilege.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.privilege.RoleDao;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.privilege.Role;
import com.yuqincar.service.privilege.RoleService;
import com.yuqincar.utils.QueryHelper;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDao roleDao;
	

	public List<Role> getAll() {
		List<Role> roles=roleDao.getAll();
		for(int i=roles.size()-1;i>=0;i--)
			if(roles.get(i).getName().equals("超级管理员")){
				roles.remove(i);
				break;
			}
		return roles;
	}

	@Transactional
	public void delete(Long id) {
		roleDao.delete(id);
		
	}

	@Transactional
	public void save(Role role) {
		roleDao.save(role);
		
	}

	public Role getById(Long id) {
		return roleDao.getById(id);
	}

	@Transactional
	public void update(Role role) {
		roleDao.update(role);
	}

	public List<Role> getByIds(Long[] ids) {
		// TODO Auto-generated method stub
		return roleDao.getByIds(ids);
	}

	
	public PageBean getPageBean(int pageNum, QueryHelper queryHelper) {		
		return roleDao.getPageBean(pageNum, queryHelper);
	}
	
	public Role getRoleByName(String roleName){
		return roleDao.getRoleByName(roleName);
	}
	
}
