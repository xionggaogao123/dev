package com.fulaan.service;

import java.util.Date;
import java.util.List;

import com.fulaan.entity.ProjectLog;
import com.fulaan.service.base.BaseService;

public interface ProjectLogService extends BaseService{

	/**
	 * 获取该项目下日志创建的日期列表
	 * @param prjId 项目id
	 * @return
	 */
	public List<Date> getProLogDateList(int prjId);
	
	/**
	 * 获取该日期下所有此项目的日志
	 * @param prjId 项目id
	 * @param date 日期
	 * @return
	 */
	public List<ProjectLog> getLogByDate(int prjId, String date);
	
}
