package com.yuqincar.dao.privilege;

import java.util.List;

import com.yuqincar.dao.common.BaseDao;
import com.yuqincar.domain.privilege.Department;

public interface DepartmentDao extends BaseDao<Department> {

	List<Department> findChildren(Long parentId);

	List<Department> findTopList();

	boolean canDeleteDepartment(Long id);

	Department getDepartmentByName(String name);

}
