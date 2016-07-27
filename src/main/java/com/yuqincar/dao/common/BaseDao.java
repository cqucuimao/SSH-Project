package com.yuqincar.dao.common;

import java.util.List;

import org.hibernate.Session;

import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.order.Order;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.utils.QueryHelper;

public interface BaseDao<T> {

	/**
	 * 保存实体
	 * @param entity
	 */
	void save(T entity);

	/**
	 * 删除实体
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 更新实体
	 * @param entity
	 */
	void update(T entity);

	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	T getById(Long id);
	
	/**
	 * 根据id数组查询多个
	 * @param ids
	 * @return
	 */
	List<T> getByIds(Long[] ids);


	/**
	 * 查询所有
	 * @return
	 */
	List<T> getAll();
	
	List<T> getAllQuerry( QueryHelper queryHelper) ;
		// 注意空格！
	
	/**
	 * 获取当前对话
	 * @return
	 */
	public Session getSession();
	
	/**
	 * 公共的查询分页信息的方法
	 * 
	 * @param pageNum
	 * @param queryHelper
	 *            查询语句 + 参数列表
	 * @return
	 */
	PageBean getPageBean(int pageNum, QueryHelper queryHelper);
}
