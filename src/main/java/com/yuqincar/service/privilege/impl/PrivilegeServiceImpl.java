package com.yuqincar.service.privilege.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.ActionContext;
import com.yuqincar.dao.privilege.PrivilegeDao;
import com.yuqincar.domain.privilege.Privilege;
import com.yuqincar.domain.privilege.Role;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.service.privilege.PrivilegeService;


@Service
public class PrivilegeServiceImpl implements PrivilegeService {

	@Autowired
	private PrivilegeDao privilegeDao;

	public List<Privilege> getAll() {
		return privilegeDao.getAll();
	}

	@Transactional
	public void delete(Long id) {
		privilegeDao.delete(id);
	}

	@Transactional
	public void save(Privilege privilege) {
		privilegeDao.save(privilege);
	}

	public Privilege getById(Long id) {
		return privilegeDao.getById(id);
	}

	@Transactional
	public void update(Privilege privilege) {
		privilegeDao.update(privilege);
	}

	public List<Privilege> getByIds(Long[] privilegeIds) {
		return privilegeDao.getByIds(privilegeIds);
	}
	
	private Collection<Privilege> getDescendantsPrivilege(Privilege privilege, Collection<Privilege> noPrivileges){
		if(privilege.getChildren()==null || privilege.getChildren().size()==0)
			return new HashSet<Privilege>();
		if(noPrivileges.contains(privilege))
			return new HashSet<Privilege>();
		
		Collection<Privilege> privileges=new HashSet<Privilege>();
		for(Privilege child:privilege.getChildren()){
			if(noPrivileges.contains(child))
				continue;
			privileges.add(child);
			Collection<Privilege> descendantsSet=getDescendantsPrivilege(child,noPrivileges);
			if(descendantsSet!=null && descendantsSet.size()>0)
				privileges.addAll(descendantsSet);
		}
		return privileges;
	}
	
	private Collection<Privilege> getAncestorsPrivilege(Privilege privilege){
		if(privilege.getParent()==null)
			return new HashSet<Privilege>();
		Collection<Privilege> privileges=new HashSet<Privilege>();
		privileges.add(privilege.getParent());
		Collection<Privilege> ancestors=getAncestorsPrivilege(privilege.getParent());
		if(ancestors!=null && ancestors.size()>0)
			privileges.addAll(ancestors);
		return privileges;
	}

	public Collection<String> getUserPrivilegeUrls(User user){
		System.out.println("in getUserPrivilegeUrls");
		Collection<String> urlSet=new HashSet<String>();
		for(Role role:user.getRoles()){
			for(Privilege privilege:role.getPrivileges()){
				if(role.getNoPrivileges().contains(privilege))
					continue;
				
				Collection<Privilege> ancestorPrivileges=getAncestorsPrivilege(privilege);
				for(Privilege ancestorPrivilege: ancestorPrivileges)
					if(role.getNoPrivileges().contains(ancestorPrivilege))
						continue;
				
				//将祖先的URL加进去
				for(Privilege ancestor:ancestorPrivileges)
					if(!urlSet.contains(ancestor.getUrl()))
						urlSet.add(ancestor.getUrl());
				//将自己的URL加进去
				if(!urlSet.contains(privilege.getUrl()))
					urlSet.add(privilege.getUrl());
				//将子孙的URL加进去
				for(Privilege descendant: getDescendantsPrivilege(privilege,role.getNoPrivileges())){
					if(!urlSet.contains(descendant.getUrl()))
						urlSet.add(descendant.getUrl());
				}
			}
		}
		return urlSet;
	}
	
	public boolean canUserHasPrivilege(User user, String privelegeUrl){
		for(Role role:user.getRoles()){
			for(Privilege privilege:role.getPrivileges())
				if(privilege.getUrl().equals(privelegeUrl))
					return true;
		}
		return false;
	}
}
