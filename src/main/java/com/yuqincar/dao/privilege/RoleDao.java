package com.yuqincar.dao.privilege;

import com.yuqincar.dao.common.BaseDao;
import com.yuqincar.domain.privilege.Role;

public interface RoleDao extends BaseDao<Role>{
	public Role getRoleByName(String name);
}
