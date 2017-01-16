package com.yuqincar.domain.document;

import javax.persistence.Entity;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.privilege.User;

@Entity
public class EngineCheckNodeMap extends BaseEntity{
	private String node;
	private User user;
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
