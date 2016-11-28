package com.fulaan.project.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.project.ProjectDao;
import com.db.project.SubProjectDao;
import com.pojo.project.SubProjectEntry;

/**
 * 子课题Service
 * 2015-8-27 21:08:57
 *
 * @author cxy
 */
@Service
public class SubProjectService {

    private ProjectDao projectDao = new ProjectDao();

    private SubProjectDao subProjectDao = new SubProjectDao();

    /**
     * 添加一条课题信息
     *
     * @param e
     * @return
     */
    public ObjectId addSubProjectEntry(SubProjectEntry e) {
        return subProjectDao.addSubProjectEntry(e);
    }

    /**
     * 根据ID查询
     *
     * @param userId
     * @return
     */
    public SubProjectEntry getSubProjectEntryById(ObjectId id) {
        return subProjectDao.getSubProjectEntryById(id);
    }

    /**
     * 根据父课题ID查找子课题List
     *
     * @param schoolIds
     * @return
     */
    public List<SubProjectEntry> querySubProjectionEntriesByParentId(ObjectId parentId) {
        return subProjectDao.querySubProjectionEntriesByParentId(parentId);
    }
}
