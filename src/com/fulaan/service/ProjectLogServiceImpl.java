package com.fulaan.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fulaan.dao.base.BaseDao;
import com.fulaan.entity.ProjectLog;
import com.fulaan.service.base.BaseServiceImpl;

@Service
public class ProjectLogServiceImpl extends BaseServiceImpl implements ProjectLogService {

	@Resource
	BaseDao baseDao;
	
	@Override
	public List<Date> getProLogDateList(int prjId) {
		
		String querySql = "select createdTime from ProjectLog where project.id = ? order by createdTime DESC";
		List<Date> createdDateList = baseDao.find(querySql, prjId);
		
		return createdDateList;
	}

	@Override
	public List<ProjectLog> getLogByDate(int prjId, String date) {
		
		String querySql = "from ProjectLog where created_time like ? and project.id = ?order by createdTime DESC";
		List<ProjectLog> logList = baseDao.find(querySql, new Object[]{date + "%", prjId});
		
		return logList;
	}

}
