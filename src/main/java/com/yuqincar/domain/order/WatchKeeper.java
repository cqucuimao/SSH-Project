package com.yuqincar.domain.order;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.utils.Text;

/*
 * 值班设置
 */
@Entity
public class WatchKeeper extends BaseEntity {
	@Text("值班模式")
	private boolean onDuty; //是否在值班模式
	@Text("值班人员")
	@OneToOne(fetch=FetchType.LAZY)
	private User keeper; //值班人员
	
	public boolean isOnDuty() {
		return onDuty;
	}
	public void setOnDuty(boolean onDuty) {
		this.onDuty = onDuty;
	}
	public User getKeeper() {
		return keeper;
	}
	public void setKeeper(User keeper) {
		this.keeper = keeper;
	}	
}
