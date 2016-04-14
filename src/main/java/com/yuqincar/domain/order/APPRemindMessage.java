package com.yuqincar.domain.order;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.utils.Text;

/**
 * 发送给司机APP的提醒消息
 * @author chenhengxin
 *
 */
@Entity
public class APPRemindMessage extends BaseEntity {
	@Text("用户")
	@OneToOne(fetch=FetchType.LAZY)
	private User user;	//用户。
	@Text("消息内容")
	private String description;	//消息内容
	@Text("是否发送成功")
	private boolean sended;	//是否已经发送到APP端
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isSended() {
		return sended;
	}
	public void setSended(boolean sended) {
		this.sended = sended;
	}
	
	
	
}