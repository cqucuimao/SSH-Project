package com.yuqincar.domain.document;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.common.DiskFile;
import com.yuqincar.domain.privilege.Department;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.utils.Text;

@Entity
public class Document extends BaseEntity{
	
	@Text("公文名称")
	@Column(nullable=false)
	private String name;
	
	@Text("公文类别")
	@ManyToOne
	@JoinColumn(nullable=false)
	private DocumentType type;
	
	@Text("发起部门")
	@OneToOne
	private Department launchDepartment;
	
	@Text("发起人")
	private User launchUser;
	
	@Text("创建日期")
	private Date foundDate;
	
	@Text("发起日期")
	private Date launchDate;
	
	@Text("状态")
	private DocumentStatusEnum status;
	
	@Text("当前审核人")
	private User approvingUser;
	
	@Text("最后审核人")
	private User lastApproveUser;
		
	@Text("审核记录")
	@OneToMany
	@JoinTable(name = "document_record", joinColumns = { @JoinColumn(name = "document_id")},
	inverseJoinColumns = { @JoinColumn(name = "documentRecord_id") })
	private List<DocumentRecord> records;
	
	@Text("附件")
	@OneToMany
	@JoinTable(name = "document_attachment", joinColumns = { @JoinColumn(name = "document_id")},
	inverseJoinColumns = { @JoinColumn(name = "diskFile_id") })
	private List<DiskFile> attachments;
	
	@Text("备注")
	private String memo;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DocumentType getType() {
		return type;
	}

	public void setType(DocumentType type) {
		this.type = type;
	}

	public Department getLaunchDepartment() {
		return launchDepartment;
	}

	public void setLaunchDepartment(Department launchDepartment) {
		this.launchDepartment = launchDepartment;
	}

	public User getLaunchUser() {
		return launchUser;
	}

	public void setLaunchUser(User launchUser) {
		this.launchUser = launchUser;
	}

	public Date getFoundDate() {
		return foundDate;
	}

	public void setFoundDate(Date foundDate) {
		this.foundDate = foundDate;
	}

	public Date getLaunchDate() {
		return launchDate;
	}

	public void setLaunchDate(Date launchDate) {
		this.launchDate = launchDate;
	}

	public DocumentStatusEnum getStatus() {
		return status;
	}

	public void setStatus(DocumentStatusEnum status) {
		this.status = status;
	}

	public User getApprovingUser() {
		return approvingUser;
	}

	public void setApprovingUser(User approvingUser) {
		this.approvingUser = approvingUser;
	}

	public User getLastApproveUser() {
		return lastApproveUser;
	}

	public void setLastApproveUser(User lastApproveUser) {
		this.lastApproveUser = lastApproveUser;
	}

	public List<DocumentRecord> getRecords() {
		return records;
	}

	public void setRecords(List<DocumentRecord> records) {
		this.records = records;
	}

	public List<DiskFile> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<DiskFile> attachments) {
		this.attachments = attachments;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
	
}
