/**
 * University Of Chongqing.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.yuqincar.service.customer;

import java.util.List;

import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.Customer;
import com.yuqincar.domain.order.CustomerOrganization;
import com.yuqincar.domain.order.OtherPassenger;
import com.yuqincar.service.base.BaseService;
import com.yuqincar.utils.QueryHelper;

/**
 * CustomerService
 * @author wanglei
 * @version $Id: CustomerService.java, v 0.1 2016年1月8日 下午4:09:53 wanglei Exp $
 */
public interface CustomerService extends BaseService {

    public Customer getById(Long id);

    /**
     * 模糊查询在名称和简称中包含关键字的客户单位。简称的查询结果放在前面，名称的查询结果放在后面。
     * 
     * @param keyword
     *            用于查询的关键字
     * @return 满足条件的客户单位。只返回第1页的查询结果。如果系统允许指定分页大小，那么指定大小为5条记录。
     */
    public PageBean queryCustomerOrganizationByKeyword(String keyword);
    
    
    /** 查询客户*/
    public PageBean<Customer> queryCustomer(int pageNum , QueryHelper helper);
    
    /**
     * 保存客户。
     * 保存客户之前，在客户端验证：姓名、电话号码不能为空。无论是否同一个单位，都允许出现同名的客户。
     */
    
    public void saveCustomer(Customer customer);
    
    /**
     * 修改客户。
     * 用法同saveCustomer。
     * @param customer
     */
    public void updateCustomer(Customer customer);
    
    /**
     * 判断是否能够删除客户。如果有订单与客户关联，则不能删除，否则就可以删除。
     * 调用orderDao的queryOrder方法来查询是否有订单与客户关联。
     * @param customerId
     * @return
     */
    public boolean canDeleteCustomer(long customerId);
    
    /**
     * 删除客户。
     * @param customerId
     */
    public void deleteCustomer(long customerId);
    
    /**
     * 获取某个单位下面的某个客户
     * @param customerName
     * @param CustomerOrganizationName
     * @return
     */
    public Customer getCustomerByNameAndOrganization(String customerName,String CustomerOrganizationName);
    
    public Customer getCustomerByPhoneNumber(String phoneNumber);
    
    public void deleteOtherPassenger(Customer customer,OtherPassenger otherPassenger);
    
    /*
     * 获取某个单位下的全部客户
     */
    public List<Customer> getAllCustomerByOrganization(long customerOrganizationId);
}
