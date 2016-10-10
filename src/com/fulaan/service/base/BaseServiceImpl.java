package com.fulaan.service.base;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Service;

import com.fulaan.dao.base.BaseDao;

@Service
public class BaseServiceImpl implements BaseService{

    @Resource
    BaseDao baseDao;
    
    @Override
    public void save(Object obj) {
        baseDao.save(obj);
    }

    @Override
    public void saveOrUpdate(Object obj) {
        baseDao.saveOrUpdate(obj);
    }

    @Override
    public void saveOrUpdateAll(Collection<?> collection) {
        baseDao.saveOrUpdateAll(collection);
    }

    @Override
    public void update(Object obj) {
        baseDao.update(obj);
    }

    @Override
    public void merge(Object obj) {
        baseDao.merge(obj);
    }

    @Override
    public <T> T get(Class<T> entity, Serializable id) {
        return baseDao.get(entity, id);
    }

    @Override
    public <T> List<T> findAll(Class<T> entity) {
        return baseDao.findAll(entity);
    }

    @Override
    public List find(String query) {
        return baseDao.find(query);
    }

    @Override
    public List find(String query, Object param) {
        return baseDao.find(query, param);
    }

    @Override
    public List find(String query, Object[] objParams) {
        return baseDao.find(query, objParams);
    }

    @Override
    public void delete(Object obj) {
        baseDao.delete(obj);
    }

    @Override
    public long countAll(Class<?> entity) {
        return baseDao.countAll(entity);
    }

    @Override
    public long count(String queryString) {
        return baseDao.count(queryString);
    }

    @Override
    public long count(String queryString, Object param) {
        return baseDao.count(queryString, param);
    }

    @Override
    public long count(String queryString, Object[] params) {
        return baseDao.count(queryString, params);
    }

    @Override
    public List getPageItem(String queryString, int index, int size) {
        return baseDao.getPageItem(queryString, index, size);
    }

    @Override
    public List findByCriteria(DetachedCriteria criteria) {
        return baseDao.findByCriteria(criteria);
    }

}
