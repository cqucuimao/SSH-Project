package com.yuqincar.domain.document;

import java.util.Date;

import javax.persistence.Entity;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.utils.Text;

@Entity
public class DocumentRecord extends BaseEntity{
	
	@Text("审核人")
	private User approveUser;
	
	@Text("审核意见")
	private boolean approveResult;
	
	@Text("审核时间")
	private Date approveDate;
	
	@Text("理由")
	private String reasons;

	public User getApproveUser() {
		return approveUser;
	}

	public void setApproveUser(User approveUser) {
		this.approveUser = approveUser;
	}

	public boolean isApproveResult() {
		return approveResult;
	}

	public void setApproveResult(boolean approveResult) {
		this.approveResult = approveResult;
	}

	public Date getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}

	public String getReasons() {
		return reasons;
	}

	public void setReasons(String reasons) {
		this.reasons = reasons;
	}
	
}
