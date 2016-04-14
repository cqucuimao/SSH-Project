package com.yuqincar.dao.privilege;

import com.yuqincar.dao.common.BaseDao;
import com.yuqincar.domain.privilege.Privilege;

public interface PrivilegeDao extends BaseDao<Privilege> {
	public Privilege getPrivilegeByUrl(String url);
}
