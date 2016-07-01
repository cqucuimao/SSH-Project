package com.yuqincar.service.car.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.car.MaterialDao;
import com.yuqincar.domain.car.Material;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.service.car.MaterialService;
import com.yuqincar.utils.QueryHelper;

@Service
public class MaterialServiceImpl implements MaterialService {
	@Autowired
	private MaterialDao materialDao;
	
	@Transactional
	public void saveMaterial(Material material) {
		materialDao.save(material);
	}

	@Transactional
	public void updateMaterial(Material material) {
		materialDao.update(material);
	}

	@Transactional
	public void deleteMaterial(Long id) {
		materialDao.delete(id);
	}

	public PageBean<Material> queryMaterial(int pageNum, QueryHelper helper) {
		return materialDao.getPageBean(pageNum, helper);
	}

	public Material getMaterialById(Long id) {
		return materialDao.getById(id);
	}
}
