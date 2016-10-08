package com.fulaan.service;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.fulaan.entity.Staff;

/**
 * @author xusy 2016-9-23
 * 职员信息service
 */
public interface StaffService {

	/**
	 * 获取表中职员总数
	 * @param entity
	 * @return
	 */
	public Long getTotalNum(Class<Staff> entity);
	
	/**
	 * 获取当前页职员信息记录
	 * @param pageNum 当前页数
	 * @param size 每页记录条数
	 * @return
	 */
	public List<Staff> getStaffsByPageNum(int pageNum, int size);
	
	/**
	 * 根据主键id查找职员
	 * @param entity
	 * @param id 主键
	 * @return
	 */
	public Staff get(Class<Staff> entity, Serializable id);
	
	/**
	 * 删除职员
	 * @param staff
	 */
	public void removeStaff(Staff staff);
	
	/**
	 * 根据职工号查找职工
	 * @param jobTitle
	 */
	public List<Staff> findStaffByJobNum(String jobNum);
	
	/**
	 * 根据登录名查找职工
	 * @param loginName
	 * @return
	 */
	public List<Staff> findStaffByLoginName(String loginName);
	
	/**
	 * 查出所有员工
	 * @return
	 */
	public List<Staff> findAllStaff();
	
	public void save(Staff staff);
	
	public void update(Staff staff);
	
	public List<Staff> findByCriteria(DetachedCriteria criteria);
}
