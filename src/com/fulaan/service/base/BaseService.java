package com.fulaan.service.base;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

public interface BaseService {
    
    /**
     * 保存一个对象
     * @param obj
     */
    public void save(Object obj);
    
    /**
     * 保存或更新对象
     * @param obj
     */
    public void saveOrUpdate(Object obj);
    
    /**
     * 批量保存或更新集合中对象
     * @param collection
     */
    public void saveOrUpdateAll(Collection<?> collection);
    
    /**
     * 更新对象
     * @param obj
     */
    public void update(Object obj);
    
    /**
     * 更新保存对象
     * @param obj
     */
    public void merge(Object obj);
    
    /**
     * 根据实体对象和主键ID查找
     * @param entity
     * @param id 主键id
     * @return
     */
    public <T> T get(Class<T> entity, Serializable id);
    
    /**
     * 获取实体所对应的表中所有数据
     * @param entity
     * @return
     */
    public <T> List<T> findAll(Class<T> entity);
    
    /**
     * 根据查询语句进行查询
     * @param query 查询语句
     * @return
     */
    public List find(String query);
    
    /**
     * 根据查询语句进行查询
     * @param query 查询语句
     * @param param 参数
     * @return
     */
    public List find(String query, Object param);
    
    /**
     * 根据查询语句进行查询
     * @param query 查询语句
     * @param objParams 参数数组
     * @return
     */
    public List find(String query, Object[] objParams);
    
    /**
     * 删除
     * @param obj
     */
    public void delete(Object obj);
    
    /**
     * 查询实体所对应的表中记录条数
     * @param entity
     * @return
     */
    public long countAll(Class<?> entity);
    
    /**
     * 根据查询语句计算条数
     * @param queryString
     * @return
     */
    public long count(String queryString);
    
    public long count(String queryString, Object param);
    
    public long count(String queryString, Object[] params);
    
    /**
     * 获取当前页数据
     * @param queryString 查询语句
     * @param index 开始获取的下标
     * @param size 每页条数
     * @return
     */
    public List getPageItem(String queryString, int index, int size);
    
    public List findByCriteria(DetachedCriteria criteria);
}
