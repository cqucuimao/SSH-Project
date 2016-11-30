package com.yuqincar.domain.car;

import java.util.HashSet;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.utils.Text;
/*
 * 车型大类
 */
@Entity
public class CarServiceSuperType extends BaseEntity {
	@Text("名称")
	private String superTitle;
	
	@Text("车型")
	@OneToMany(mappedBy="superType")
	private List<CarServiceType> types;

	public String getSuperTitle() {
		return superTitle;
	}

	public void setSuperTitle(String superTitle) {
		this.superTitle = superTitle;
	}

	public List<CarServiceType> getTypes() {
		return types;
	}

	public void setTypes(List<CarServiceType> types) {
		this.types = types;
	}
}
