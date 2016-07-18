package com.yuqincar.service.privilege;

import java.util.List;

import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.privilege.Department;
import com.yuqincar.service.base.BaseService;
import com.yuqincar.utils.QueryHelper;

public interface DepartmentService extends BaseService {

	List<Department> getAll();

	void delete(Long id);

	void save(Department department);

	Department getById(Long id);

	void update(Department department);

	PageBean getPageBean(int pageNum, QueryHelper helper);

	boolean canDeleteDepartment(Long id);

}
