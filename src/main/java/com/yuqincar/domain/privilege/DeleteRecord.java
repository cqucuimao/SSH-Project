package com.yuqincar.domain.privilege;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.yuqincar.domain.common.BaseEntity;

@Entity
public class DeleteRecord extends BaseEntity{

	@Column(nullable = false)
	private Long entityId;

	@Column(nullable = false)
	private Date deleteTime;

	@Column
	private String entityName;

	@Column
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "delete_operator_id")
	private User deleteOperator;

	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getDeleteOperator() {
		return deleteOperator;
	}

	public void setDeleteOperator(User deleteOperator) {
		this.deleteOperator = deleteOperator;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
	
}
