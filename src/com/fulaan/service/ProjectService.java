package com.fulaan.service;

import java.util.List;

import com.fulaan.entity.Project;

public interface ProjectService {

	/**
	 * 获取表中项目总数
	 * @param entity
	 * @return
	 */
	public Long getTotalNum(Class<Project> entity);
	
	/**
	 * 根据雇员查询id查询其所属项目
	 * @param id 雇员主键id
	 * @return
	 */
	public Long getTotalNumByStaffId(int id);
	
	public Long getTotalNumByStaffIdAndStatud(int id, int status);
	
	/**
	 * 获取该登录用户当前页数据
	 * @param pageNum
	 * @param size
	 * @param id
	 * @return
	 */
	public List getProjectPageListByStaffId(int pageNum, int size, int id);
	
	/**
	 * 获取该页项目数据
	 * @param pageNum 当前页数
	 * @param size 每页记录条数
	 * @return
	 */
	public List<Project> getProjectsByPageNum(int pageNum, int size);
	
	public void save(Project project);
	
	public Project get(int id);
	
	/**
	 * 根据用户id查找负责的项目
	 * @param id 用户id
	 * @return
	 */
	public List<Project> getOwnerProjectByStaffId(int id);
	
	public void update(Project project);
	
}
