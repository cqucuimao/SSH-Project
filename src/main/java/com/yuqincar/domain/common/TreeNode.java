package com.yuqincar.domain.common;

import java.util.List;
import java.util.Map;

public class TreeNode {
	private Long id;
	private String name;
	private String nocheck;
	private List<TreeNode> children;
	private Boolean open;
	private Map<String,Object> param;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<TreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
	public Boolean getOpen() {
		return open;
	}
	public void setOpen(Boolean open) {
		this.open = open;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Map<String, Object> getParam() {
		return param;
	}
	public void setParam(Map<String, Object> param) {
		this.param = param;
	}
	public String getNocheck() {
		return nocheck;
	}
	public void setNocheck(String nocheck) {
		this.nocheck = nocheck;
	}	
	
}
