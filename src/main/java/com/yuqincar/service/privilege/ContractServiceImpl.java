package com.yuqincar.service.privilege;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sun.mail.handlers.image_gif;
import com.yuqincar.dao.common.DiskFileDao;
import com.yuqincar.dao.privilege.ContractDao;
import com.yuqincar.domain.common.DiskFile;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.ProtocolOrderPayOrder;
import com.yuqincar.domain.privilege.Contract;
import com.yuqincar.service.common.DiskFileService;
import com.yuqincar.utils.QueryHelper;

@Service
public class ContractServiceImpl implements ContractService {

	@Autowired
	private ContractDao contractDao;
	
	@Autowired
	private DiskFileDao diskFileDao;
	
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
		/*List<DiskFile> diskFiles = c.getDiskFiles();
		DiskFile df=new DiskFile();
		for (int i=0;i<diskFiles.size();i++) {
			df=diskFiles.get(i);
			df.setContract(c);
			diskFileDao.update(df);
		}
		System.out.println("size = "+deletedFileIds.size());
			for(int j=0;j<deletedFileIds.size();j++)
			{
				System.out.println("nnname = "+c.getUser().getName());
				diskFileDao.delete(deletedFileIds.get(j));
			}
		
		System.out.println("user name = "+c.getUser().getName());*/
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
