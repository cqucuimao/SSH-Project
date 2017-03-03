package com.yuqincar.domain.document;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.common.DiskFile;
import com.yuqincar.domain.privilege.Department;
import com.yuqincar.utils.Text;

@Entity
public class DocumentType extends BaseEntity{
	
	@Text("公文类别名")
	@Column(nullable=false)
	private String name;
	
	@Text("发起部门")
	@ManyToMany
	@JoinTable(name = "documentType_department", joinColumns = { @JoinColumn(name = "documentType_id")},
		inverseJoinColumns = { @JoinColumn(name = "department_id") })
	private List<Department> launchDepartments;
	
	@Text("审核链条")
	@OneToOne(fetch=FetchType.LAZY)
	private DiskFile checkLine;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Department> getLaunchDepartments() {
		return launchDepartments;
	}

	public void setLaunchDepartments(List<Department> launchDepartments) {
		this.launchDepartments = launchDepartments;
	}

	public DiskFile getCheckLine() {
		return checkLine;
	}

	public void setCheckLine(DiskFile checkLine) {
		this.checkLine = checkLine;
	}
	

}
