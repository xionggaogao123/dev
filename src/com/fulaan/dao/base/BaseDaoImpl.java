package com.fulaan.dao.base;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

@Repository("baseDao")
public class BaseDaoImpl implements BaseDao{
	
	private JdbcTemplate jdbcTemplate;
	
	private HibernateTemplate hibernateTemplate;
	
	protected SessionFactory sessionFactory;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Resource
	public void setJdbcTemplate(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public HibernateTemplate getHibernateTemplate() {
		
		return hibernateTemplate;
	}

	@Resource
	public void setHibernateTemplate(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.hibernateTemplate = new HibernateTemplate(sessionFactory);
		this.hibernateTemplate.setCacheQueries(true);
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Override
	public void save(Object obj) {
		getHibernateTemplate().save(obj);
	}

	@Override
	public void saveOrUpdate(Object obj) {
		getHibernateTemplate().saveOrUpdate(obj);
	}

	@Override
	public void saveOrUpdateAll(Collection<?> collection) {
		getHibernateTemplate().saveOrUpdateAll(collection);
	}

	@Override
	public void update(Object obj) {
		getHibernateTemplate().update(obj);
	}

	@Override
	public void merge(Object obj) {
		getHibernateTemplate().merge(obj);
	}

	@Override
	public <T> T get(Class<T> entity, Serializable id) {
		return (T)getHibernateTemplate().get(entity, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> findAll(Class<T> entity) {
		return getHibernateTemplate().find("from " + entity.getName());
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List find(String query) {
		return getHibernateTemplate().find(query);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List find(String query, Object param) {
		return getHibernateTemplate().find(query, param);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public List find(String query, Object[] objParams) {
		return getHibernateTemplate().find(query, objParams);
	}

	@Override
	public void delete(Object obj) {
		getHibernateTemplate().delete(obj);
	}

	@Override
	public Session getSession() {
		return getSession(hibernateTemplate.isAllowCreate());
	}

	@Override
	public Session getSession(boolean isAllow) {
		return isAllow ? 
				SessionFactoryUtils.
				getSession(hibernateTemplate.getSessionFactory(), 
						hibernateTemplate.getEntityInterceptor(), 
						hibernateTemplate.getJdbcExceptionTranslator()) 
				: SessionFactoryUtils.getSession(hibernateTemplate.getSessionFactory(), false);
	}

	@Override
	@SuppressWarnings("unchecked")
	public long countAll(Class<?> entity) {
		 List<Long> list = getHibernateTemplate().find("select count(*) from " + entity.getName());
		
		 return (list != null && list.size() > 0) ? list.get(0) : 0L;
	}

	@Override
	@SuppressWarnings("unchecked")
	public long count(String queryString) {
		List<Long> list = find(queryString);
		return (list != null && list.size() > 0) ? list.get(0) : 0L;
	}

	@Override
	@SuppressWarnings("unchecked")
	public long count(String queryString, Object param) {
		List<Long> list = find(queryString, param);
		return (list != null && list.size() > 0) ? list.get(0) : 0L;
	}

	@Override
	@SuppressWarnings("unchecked")
	public long count(String queryString, Object[] params) {
		List<Long> list = find(queryString, params);
		return (list != null && list.size() > 0) ? list.get(0) : 0L;
	}

	@Override
	public List getPageItem(String queryString, int index, int size) {
		Session session = getSession();
		Query query = session.createQuery(queryString);
		query.setFirstResult(index);
		query.setMaxResults(size);
		return query.list();
	}

	@Override
	public List findByCriteria(DetachedCriteria criteria) {
		return getHibernateTemplate().findByCriteria(criteria);
	}
	
}
