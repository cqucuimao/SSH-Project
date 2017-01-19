package com.yuqincar.service.privilege;

import java.util.Date;
import java.util.List;

import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.Customer;
import com.yuqincar.domain.order.OtherPassenger;
import com.yuqincar.domain.order.ProtocolOrderPayOrder;
import com.yuqincar.domain.privilege.Contract;
import com.yuqincar.service.base.BaseService;
import com.yuqincar.utils.QueryHelper;

public interface ContractService extends BaseService{

    
   /* *//**
     * 模糊查询在名称和简称中包含关键字的客户单位。简称的查询结果放在前面，名称的查询结果放在后面。
     * 
     * @param keyword
     *            用于查询的关键字
     * @return 满足条件的客户单位。只返回第1页的查询结果。如果系统允许指定分页大小，那么指定大小为5条记录。
     *
    public PageBean queryUserByKeyword(String keyword);
    
    
    /** 查询合同*//*
    public PageBean<Contract> queryContract(int pageNum , QueryHelper helper);
    
    *//**
     * 保存合同。
     * 保存合同之前，在客户端验证：员工姓名、合同开始、结束时间不能为空。判断，将员工所有的合同中结束日期最晚的日期赋值给contractExpired
     *//*
    
    public void saveContract(Contract contract);
    
    *//**
     * 修改合同。
     * 用法同saveCustomer。
     * @param customer
     *//*
    public void updateContract(Contract contract);
    
    *//**
     * 判断是否能够删除客户。如果有订单与客户关联，则不能删除，否则就可以删除。
     * 调用orderDao的queryOrder方法来查询是否有订单与客户关联。
     * @param customerId
     * @return
     *//*
    public boolean canDeleteCustomer(long customerId);
    
    *//**
     * 删除合同。
     * @param customerId
     *//*
    //public void deleteContract(long contractId);
*/    
    public List<Contract> getAllContracts();
    
    public PageBean<Contract> queryContract(int pageNum , QueryHelper helper);
    
    public Contract getContractById(Long id);
    
    public void updateContract(Contract c);
    
    public void deleteContract(Long id);
    
    public void saveContract(Contract c);
}
