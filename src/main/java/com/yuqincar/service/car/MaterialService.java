package com.yuqincar.service.car;

import com.yuqincar.domain.car.Material;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.utils.QueryHelper;

public interface MaterialService {
	
	public void saveMaterial(Material material);
	
	public void updateMaterial(Material material);
	
	public void deleteMaterial(Long id);
	
	public PageBean<Material> queryMaterial(int pageNum , QueryHelper helper);
	
	public Material getMaterialById(Long id);
}
