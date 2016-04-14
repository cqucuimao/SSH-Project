/**
 * University Of Chongqing.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.yuqincar.service.CustomerOrganization;

import java.util.List;

import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.monitor.Location;
import com.yuqincar.domain.order.Customer;
import com.yuqincar.domain.order.CustomerOrganization;
import com.yuqincar.service.base.BaseService;
import com.yuqincar.utils.QueryHelper;

/**
 * CustomerOrganizationService
 * @author wanglei
 * @version $Id: CustomerOrganizationService.java, v 0.1 2016年1月8日 下午4:12:20 wanglei Exp $
 */
public interface CustomerOrganizationService extends BaseService {
	
	public PageBean<CustomerOrganization> queryCustomerOrganizationByKeyword(String keyword);
	
    public CustomerOrganization getById(Long id);
    
    /** 查询客户单位*/
    public PageBean<CustomerOrganization> queryCustomerOrganization(int pageNum , QueryHelper helper);
    
    /**
     * 判断单位名称是否已经存在。
     * 在调用saveCustomerOrganization/updateCustomerOrganization接口之前，一定要确保单位名称唯一。需要调用此接口来判断，如果已经存在，就提示用户修改。
     * @param selfId  当在调用saveCustomerOrganization之前，selfId为0；当在调用updateCustomerOrganization之前，selfId为正在被修改的客户单位
     * 的id，因为在寻找是否有重名的实体是，需要把自己排出在外。（如果不明白，找我商量。）
     * @param name
     * @return
     */
    public boolean isNameExist(long selfId, String name);
    
    /**
     * 判断单位简称是否已经存在。
     * 用法同isCustomerOrganizationNameExist。
     * @param selfId 意义同isCustomerOrganizationNameExist
     * @param abbreviation
     * @return
     */
    public boolean isAbbreviationExist(long selfId, String abbreviation);
    
    /**
     * 保存客户单位。
     * 允许保存之前，需要在客户端验证name, address.description, address.detail都不能为空；abbreviation可以为空。另外需要确保
     * address.location中的经度和纬度都不能为0，否则给予用户提示：无法获取该单位的坐标信息，请通过定位点或备选项来选择客户单位的地址。
     * @param customerOrganization
     */
    public void saveCustomerOrganization(CustomerOrganization customerOrganization);
    
    /**
     * 修改客户单位。
     * 用法与意义同saveCustomerOrganization方法。
     * @param customerOrganization
     */
    public void updateCustomerOrganization(CustomerOrganization customerOrganization);
    
    /**
     * 判断是否能删除客户单位。如果有订单与客户单位关联，则不能删除，否则就可以删除。
     * 调用orderDao的queryOrder方法来查询是否有订单与客户单位关联。
     * @param id
     * @return
     */
    public boolean canDeleteCustomerOrganization(long customerOrganizationId);
    
    public void deleteCustomerOrganization(long customerOrganizationId);

	public CustomerOrganization getCustomerOrganizationByName(String name);

	public void saveLocation(Location location);

	public PageBean getPageBean(int pageNum, QueryHelper helper);
}
