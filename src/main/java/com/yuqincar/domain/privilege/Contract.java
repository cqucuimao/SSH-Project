package com.yuqincar.domain.privilege;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.common.DiskFile;
import com.yuqincar.utils.Text;

@Entity
public class Contract extends BaseEntity {

	@Text("所属用户")
	@ManyToOne(fetch=FetchType.LAZY )
	private User user;
	
	@Text("开始时间")
	private Date fromDate;

	@Text("结束时间")
	private Date toDate;
	
	@Text("扫描件")
	@OneToMany
	@JoinTable(name = "contract_scanning", joinColumns = { @JoinColumn(name = "contract_id")},
	inverseJoinColumns = { @JoinColumn(name = "diskFile_id") })
	private List<DiskFile> diskFiles;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public List<DiskFile> getDiskFiles() {
		return diskFiles;
	}

	public void setDiskFiles(List<DiskFile> diskFiles) {
		this.diskFiles = diskFiles;
	}

}
