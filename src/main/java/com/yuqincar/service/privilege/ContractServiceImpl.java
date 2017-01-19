package com.yuqincar.service.privilege;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuqincar.dao.privilege.ContractDao;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.ProtocolOrderPayOrder;
import com.yuqincar.domain.privilege.Contract;
import com.yuqincar.utils.QueryHelper;

@Service
public class ContractServiceImpl implements ContractService {

	@Autowired
	private ContractDao contractDao;
	
	public List<Contract> getAllContracts(){
		return contractDao.getAll();
	}
	
	public PageBean<Contract> queryContract(int pageNum , QueryHelper helper)
	{
		return contractDao.getPageBean(pageNum, helper);
	}
	
	public Contract getContractById(Long id) {
		return contractDao.getById(id);
	}
	
	@Transactional
	public void updateContract(Contract c) {
		contractDao.update(c);
	}
	
	@Transactional
	public void deleteContract(Long id)
	{
		contractDao.delete(id);
	}
	
	@Transactional
	public void saveContract(Contract c) {
		contractDao.save(c);
	}
}
