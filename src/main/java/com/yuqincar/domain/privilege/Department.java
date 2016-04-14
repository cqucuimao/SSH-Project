package com.yuqincar.domain.privilege;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.utils.Text;

@Entity
public class Department extends BaseEntity{

	@Text("用户")
	@OneToMany(mappedBy="department")		//由User对象的department属性来管理关联关系，相当于inverse=true
	private Set<User> users = new HashSet<User>();
	
	@Text("上级部门")
	@ManyToOne(fetch=FetchType.LAZY)
	private Department parent;
	
	@Text("下级部门")
	@OneToMany(mappedBy="parent",cascade=CascadeType.REMOVE)
	private Set<Department> children = new HashSet<Department>();

	@Text("部门名称")
	private String name;
	
	@Text("备注")
	private String description;

	public Department getParent() {
		return parent;
	}

	public void setParent(Department parent) {
		this.parent = parent;
	}

	public Set<Department> getChildren() {
		return children;
	}

	public void setChildren(Set<Department> children) {
		this.children = children;
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

}
