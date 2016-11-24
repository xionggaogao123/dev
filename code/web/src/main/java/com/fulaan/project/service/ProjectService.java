package com.fulaan.project.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.factory.MongoFacroty;
import com.db.project.ProjectDao;
import com.db.project.SubProjectDao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.project.ProjectEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
/**
 * 课题Service
 * 2015-8-27 21:09:07
 * @author cxy
 *
 */
@Service
public class ProjectService {
	private ProjectDao projectDao = new ProjectDao();
	
	private SubProjectDao subProjectDao = new SubProjectDao();
	
	/**
	 * 添加一条课题信息
	 * @param e
	 * @return
	 */
	public ObjectId addProjectEntry(ProjectEntry e){
		return projectDao.addProjectEntry(e);
	}
	/**
     * 根据ID查询
     * @param userId
     * @return
     */
    public ProjectEntry getProjectEntryById(ObjectId id)
    {
        return projectDao.getProjectEntryById(id);
    }
    
    /**
     * 根据传进的参数查询
     * @param schoolIds
     * @return
     */
    public List<ProjectEntry> queryProjectionEntries(String level,String keyword) {
       return projectDao.queryProjectionEntries(level, keyword);
    }
}
