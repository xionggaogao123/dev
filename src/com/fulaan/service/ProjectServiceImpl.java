package com.fulaan.service;

import java.math.BigInteger;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fulaan.dao.ProjectDao;
import com.fulaan.entity.Project;

@Service
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	ProjectDao projectDao;

	@Override
	public Long getTotalNum(Class<Project> entity) {
		return projectDao.countAll(entity);
	}

	@Override
	public List<Project> getProjectsByPageNum(int pageNum, int size) {
		
		String sql = "select * from project order by id";
		int index = (pageNum == 0 ? 0 : pageNum - 1) * size;
		List<Project> projectList = projectDao.getPageItem(sql, index, size);
		
		return projectList;
	}

	@Override
	public Long getTotalNumByStaffId(int id) {
		
		String querySql = "";
		
		if(id > 0) {
			querySql = "SELECT count(*) FROM "
					+ "(SELECT project_id FROM project_staff ps WHERE ps.staff_id = " + id
					+ " union SELECT id FROM project p WHERE p.project_owner_id = " + id 
					+ " or p.project_creater_id = " + id +") r";
		} else {
			querySql = "SELECT count(*) FROM project";
		}
		
		SQLQuery sqlQuery = projectDao.getSession().createSQLQuery(querySql);
		
		long countNum = ((BigInteger) sqlQuery.list().get(0)).longValue();
		
		return (long) (sqlQuery.list() != null ? countNum : 0);
	}

	@Override
	public List getProjectPageListByStaffId(int pageNum, int size, int id) {
		
		String querySql = "";
		if(id > 0) {
			querySql = "SELECT * FROM project WHERE id in  "
					+ "(SELECT project_id FROM project_staff ps WHERE ps.staff_id = " + id
					+ " union SELECT id FROM project p WHERE p.project_owner_id = " + id
					+ " or p.project_creater_id = " + id + ")";
		} else {
			querySql = "SELECT * FROM project ORDER BY created_time DESC";
		}
		
		int index = (pageNum == 0 ? 0 : pageNum - 1) * size;
		SQLQuery sqlQuery = projectDao.getSession().createSQLQuery(querySql);
		sqlQuery.setFirstResult(index);
		sqlQuery.setMaxResults(size);
		
		//List<Project> projectList = projectDao.getPageItem(querySql, index, size);
		
		return sqlQuery.list();
	}

	@Override
	public void save(Project project) {
		projectDao.save(project);
	}

	@Override
	public Project get(int id) {
		return projectDao.get(Project.class, id);
	}

	@Override
	public List<Project> getOwnerProjectByStaffId(int id) {
		 
		DetachedCriteria dc = DetachedCriteria.forClass(Project.class);
		dc.add(Restrictions.eq("projectOwner.id", id));
		
		return projectDao.findByCriteria(dc);
	}

	@Override
	public void update(Project project) {
		projectDao.update(project);
	}
	
}
