/**
 * University Of Chongqing.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.yuqincar.dao.CustomerOrganization;

import com.yuqincar.dao.common.BaseDao;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.monitor.Location;
import com.yuqincar.domain.order.CustomerOrganization;

public interface CustomerOrganizationDao extends BaseDao<CustomerOrganization> {

	/**
	 * 模糊查询在名称和简称中包含关键字的客户单位。简称的查询结果放在前面，名称的查询结果放在后面。
	 * 
	 * @param keyword
	 *            用于查询的关键字
	 * @return 满足条件的客户单位。只返回第1页的查询结果。如果系统允许指定分页大小，那么指定大小为5条记录。
	 */
	public PageBean<CustomerOrganization> queryCustomerOrganizationByKeyword(String keyword);

	public CustomerOrganization getByAbbreviation(String abbreviation);

	CustomerOrganization getByName(String name);

	boolean canDeleteCustomerOrganization(long customerOrganizationId);

	boolean isNameExist(long selfId, String name);

	boolean isAbbreviationExist(long selfId, String abbreviation);

	void saveLocation(Location location);

}
