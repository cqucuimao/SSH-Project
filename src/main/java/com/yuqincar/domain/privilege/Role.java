package com.yuqincar.domain.privilege;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.utils.Text;


/**
 * 岗位
 * 
 * 
 */
@Entity
public class Role extends BaseEntity{
	@Text("角色名称")
	private String name;
	
	@Text("说明")
	private String description;
	
	@Text("所属用户")
	@ManyToMany(mappedBy="roles", fetch=FetchType.LAZY)
	private Set<User> users = new HashSet<User>();
	
	@Text("具有功能")
	@ManyToMany
	private Set<Privilege> privileges = new HashSet<Privilege>();
	
	@Text("不具有功能")
	@ManyToMany
	@JoinTable(name = "role_noprivilege", joinColumns = { @JoinColumn(name = "role_id")},
	inverseJoinColumns = { @JoinColumn(name = "privilege_id") })
	private Set<Privilege> noPrivileges = new HashSet<Privilege>();
	
	
	public Set<Privilege> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(Set<Privilege> privileges) {
		this.privileges = privileges;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Privilege> getNoPrivileges() {
		return noPrivileges;
	}

	public void setNoPrivileges(Set<Privilege> noPrivileges) {
		this.noPrivileges = noPrivileges;
	}
	
}
