package com.yuqincar.domain.privilege;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.utils.Text;

@Entity
public class Privilege extends BaseEntity{	
	@Text("默认页面")
	@Column(nullable=false,unique=true)
	private String url;
	
	@Text("功能名称")
	private String name;
	
	@Text("所属角色")
	@ManyToMany(mappedBy="privileges",fetch=FetchType.LAZY)
	private Set<Role> roles = new HashSet<Role>();
	
	@Text("父功能")
	@ManyToOne(fetch=FetchType.LAZY)	
	private Privilege parent;

	@Text("子功能")
	@OneToMany(mappedBy="parent",cascade=CascadeType.REMOVE)
	private Set<Privilege> children = new HashSet<Privilege>();

	public Privilege() {
	}

	public Privilege(String name, String url, Privilege parent) {
		this.name = name;
		this.url = url;
		this.parent = parent;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Privilege getParent() {
		return parent;
	}

	public void setParent(Privilege parent) {
		this.parent = parent;
	}

	public Set<Privilege> getChildren() {
		return children;
	}

	public void setChildren(Set<Privilege> children) {
		this.children = children;
	}

}
