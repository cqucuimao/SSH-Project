package com.yuqincar.dao.common.impl;

import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.Where;

import com.opensymphony.xwork2.ActionContext;
import com.yuqincar.dao.common.BaseDao;
import com.yuqincar.domain.common.BaseEntity;
import com.yuqincar.domain.common.PageBean;
import com.yuqincar.domain.privilege.DeleteRecord;
import com.yuqincar.domain.privilege.User;
import com.yuqincar.utils.Configuration;
import com.yuqincar.utils.QueryHelper;


@SuppressWarnings("unchecked")
public class BaseDaoImpl<T> implements BaseDao<T> {

	@Resource
	private SessionFactory sessionFactory;		//父类私有属性，spring仍然能够注入
	protected Class<T> clazz = null; 			//获取T的类型


	public BaseDaoImpl() {
		// 通过反射获取T的真是类型
		//getGenericSuperclass()获得带有泛型的父类
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();	
		//getActualTypeArguments获取参数化类型(泛型)的数组，泛型可能有多个
		this.clazz = (Class<T>) pt.getActualTypeArguments()[0];	//此处BaseDao仅有一个泛型参数
	}

	/**
	 * 获取当前可用的Session
	 * 
	 * @return
	 */
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}
	
	private User getCurrentUser() {
		if(ActionContext.getContext()!=null)
			return (User) ActionContext.getContext().getSession().get("user");
		return null;
	}

	public void save(T entity) {
		if(entity instanceof BaseEntity) {
			
			BaseEntity baseEntity = (BaseEntity)entity;
			if(getCurrentUser()!=null)
				baseEntity.setCompany(getCurrentUser().getCompany());
			User user = getCurrentUser();
			baseEntity.setCreator(user);
			baseEntity.setLastUpdator(user);
			baseEntity.setCreateTime(new Date());
			baseEntity.setLastUpdateTime(new Date());
			
		}
		System.out.println("getSession().save()");
		getSession().save(entity);
	}

	public void update(T entity) {
		if(entity instanceof BaseEntity) {
			BaseEntity baseEntity = (BaseEntity)entity;
			User user = getCurrentUser();
			baseEntity.setLastUpdateTime(new Date());
			baseEntity.setLastUpdator(user);
		}
		getSession().update(entity);
	}

	public void delete(Long id) {
		if (id == null) {
			return;
		}
		Object entity = getById(id);
		if (entity != null) {
			DeleteRecord record = new DeleteRecord();
			
			User user = getCurrentUser();
			if(user!=null) {
				record.setDeleteOperator(user);
				record.setContent(user.toString());
			}
			record.setEntityId(id);
			record.setDeleteTime(new Date());
			record.setEntityName(entity.getClass().getName());
			
			if(entity instanceof BaseEntity) {
				BaseEntity baseEntity = (BaseEntity)entity;
				record.setEntityId(((BaseEntity) entity).getId());
			}
			getSession().delete(entity);
			getSession().save(record);
		}
		
	}

	public T getById(Long id) {
		if(id == null) return null;
		return (T) getSession().get(clazz, id);
	}

	public List<T> getByIds(Long[] ids) {
		if(ids == null || ids.length == 0){
			return Collections.EMPTY_LIST;
		}
		return getSession().createQuery(//
				// 注意空格！
				"FROM " + clazz.getSimpleName() + " WHERE id IN (:ids)")//
				.setParameterList("ids", ids)// 注意一定要使用setParameterList()方法！
				.list();
	}
	
	public List<T> getByIdsCompanyNull(Long[] ids) {
		if(ids == null || ids.length == 0){
			return Collections.EMPTY_LIST;
		}
		return getSession().createQuery(//
				// 注意空格！
				"FROM " + clazz.getSimpleName() + " WHERE company is null and id IN (:ids)")//
				.setParameterList("ids", ids)// 注意一定要使用setParameterList()方法！
				.list();
	}


	public List<T> getAll() {
		// 注意空格！
		return getSession().createQuery("FROM " + clazz.getSimpleName()).list();
	}
	
	public List<T> getAllCompanyNull() {
		// 注意空格！
		return getSession().createQuery("FROM " + clazz.getSimpleName()+" where company is null").list();
	}
	public PageBean getPageBean(int pageNum, QueryHelper queryHelper ) {
		int pageSize = Configuration.getPageSize();
		return getPageBean(pageNum,queryHelper,pageSize);
	}
	
	/**
	 * 公共的查询分页信息的方法
	 * 
	 * @param pageNum
	 * @param queryHelper
	 * @return
	 */
	public PageBean getPageBean(int pageNum, QueryHelper queryHelper , int pageSize) {

		// 获取pageSize等信息
		List<Object> parameters = queryHelper.getParameters();

		// 查询一页的数据列表
		Query query = getSession().createQuery(queryHelper.getQueryListHql());
		if (parameters != null && parameters.size() > 0) { // 设置参数
			for (int i = 0; i < parameters.size(); i++) {
				query.setParameter(i, parameters.get(i));
			}
		}
		query.setFirstResult((pageNum - 1) * pageSize);
		query.setMaxResults(pageSize);
		List list = query.list(); // 查询

		// 查询总记录数
		query = getSession().createQuery(queryHelper.getQueryCountHql()); // 注意空格！
		if (parameters != null && parameters.size() > 0) { // 设置参数
			for (int i = 0; i < parameters.size(); i++) {
				query.setParameter(i, parameters.get(i));
			}
		}
		Long count = (Long) query.uniqueResult(); // 查询

		return new PageBean(pageNum, pageSize, count.intValue(), list);
	}
	
	public List<T> getAllQuerry( QueryHelper queryHelper) {
		List<Object> parameters = queryHelper.getParameters();
		Query query = getSession().createQuery(queryHelper.getQueryListHql());
		if (parameters != null && parameters.size() > 0) {
			for (int i = 0; i < parameters.size(); i++) {
				query.setParameter(i, parameters.get(i));
			}
		}
		return query.list();	}
}
