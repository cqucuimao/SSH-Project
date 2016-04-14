package com.yuqincar.service.privilege;

import java.util.Collection;
import java.util.List;

import com.yuqincar.domain.privilege.Privilege;
import com.yuqincar.domain.privilege.Role;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.base.BaseService;

public interface PrivilegeService extends BaseService {
	
	List<Privilege> getAll();

	void delete(Long id);

	void save(Privilege privilege);

	Privilege getById(Long id);

	void update(Privilege privilege);

	List<Privilege> getByIds(Long[] privilegeIds);
	
	Collection<String> getUserPrivilegeUrls(User user);
}
