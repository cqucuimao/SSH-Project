package com.yuqincar.service.privilege.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.yuqincar.dao.privilege.DepartmentDao;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.privilege.Department;
import com.yuqincar.service.privilege.DepartmentService;
import com.yuqincar.utils.QueryHelper;

@Service
public class DepartmentsServiceImpl implements DepartmentService {

	@Autowired
	DepartmentDao departmentDao;
	
	public List<Department> getAll() {
		return departmentDao.getAll();
	}

	@Transactional
	public void delete(Long id) {
		departmentDao.delete(id);
	}

	@Transactional
	public void save(Department department) {
		departmentDao.save(department);
	}

	public Department getById(Long id) {
		return departmentDao.getById(id);
	}

	@Transactional
	public void update(Department department) {
		departmentDao.update(department);
		
	}

	public List<Department> findChildren(Long parentId) {
		return departmentDao.findChildren(parentId);
	}

	public List<Department> findTopList() {
		return departmentDao.findTopList();
	}

	public PageBean getPageBean(int pageNum, QueryHelper queryHelper) {
		return departmentDao.getPageBean(pageNum, queryHelper);
	}



}
