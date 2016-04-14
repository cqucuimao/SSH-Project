package com.yuqincar.utils;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionContext;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.service.base.BaseService;

/**
 * 辅助拼接HQL语句的工具类
 * 目的是为了生成查询语句
 */
public class QueryHelper {

	private String fromClause; // From子句
	private String whereClause = ""; // Where子句
	private String orderByClause = ""; // OrderBy子句

	private List<Object> parameters = new ArrayList<Object>(); // 参数列表

	/**
	 * 生成From子句
	 * 
	 * @param clazz
	 * @param alias
	 *            别名
	 */
	public QueryHelper(Class clazz, String alias) {
		fromClause = "FROM " + clazz.getSimpleName() + " " + alias;
	}

	public QueryHelper(String clazz, String alias) {
		fromClause = "FROM " + clazz + " " + alias;
	}
	/**
	 * 拼接Where子句
	 * 
	 * @param condition
	 * @param args
	 */
	public QueryHelper addWhereCondition(String condition, Object... args) {
		// 拼接
		if (whereClause.length() == 0) {
			whereClause = " WHERE " + condition;
		} else {
			whereClause += " AND " + condition;
		}
		// 处理参数
		if (args != null && args.length > 0) {
			for (Object arg : args) {
				parameters.add(arg);
			}
		}
		return this;
	}


	/**
	 * 拼接OrderBy子句
	 * 
	 * @param propertyName
	 * @param asc
	 * true表示升序，false表示降序
	 */
	public QueryHelper addOrderByProperty(String propertyName, boolean asc) {
		if (orderByClause.length() == 0) {
			orderByClause = " ORDER BY " + propertyName + (asc ? " ASC" : " DESC");
		} else {
			orderByClause += ", " + propertyName + (asc ? " ASC" : " DESC");
		}
		return this;
	}


	/**
	 * 获取查询数据列表的HQL语句
	 * 
	 * @return
	 */
	public String getQueryListHql() {
		return fromClause + whereClause + orderByClause;
	}

	/**
	 * 获取查询总记录数的HQL语句（没有OrderBy子句）
	 * 
	 * @return
	 */
	public String getQueryCountHql() {
		return "SELECT COUNT(*) " + fromClause + whereClause;
	}
	
	/**
	 * 获取某列和的HQL语句
	 */
	public String getQuerySumHql(String column) {
		return "SELECT COUNT("+column+") " + fromClause + whereClause;
	}

	/**
	 * 获取参数列表
	 * 
	 * @return
	 */
	public List<Object> getParameters() {
		return parameters;
	}

	/**
	 * 准备PageBean对象到Struts2的栈顶
	 * @param service
	 * @param pageNum
	 
	public void preparePageBean(BaseService baseService, int pageNum) {
		PageBean pageBean = baseService.getPageBean(pageNum, this);
		ActionContext.getContext().getValueStack().push(pageBean);
	}
	*/
}
