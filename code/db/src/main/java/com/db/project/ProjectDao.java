package com.db.project;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.preparation.PreparationEntry;
import com.pojo.project.ProjectEntry;
import com.pojo.school.SchoolEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.utils.StringUtil;

/**
 * 课题研究Dao
 * 2015-8-27 20:56:04
 *
 * @author cxy
 */
public class ProjectDao extends BaseDao {

    /**
     * 添加一条课题信息
     *
     * @param e
     * @return
     */
    public ObjectId addProjectEntry(ProjectEntry e) {
        save(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_PROJECT, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 根据ID查询
     *
     * @param userId
     * @return
     */
    public ProjectEntry getProjectEntryById(ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_PROJECT, query, Constant.FIELDS);
        if (null != dbo) {
            return new ProjectEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 根据传进的参数查询
     *
     * @param schoolIds
     * @return
     */
    public List<ProjectEntry> queryProjectionEntries(String level, String keyword) {
        List<ProjectEntry> retList = new ArrayList<ProjectEntry>();
        BasicDBObject query = new BasicDBObject();
        if (!StringUtils.isBlank(level) && !"ALL".equals(level)) {
            query.append("pl", level);
        }
        if (!StringUtils.isBlank(keyword)) {
            query.append("ptna", MongoUtils.buildRegex(keyword));
        }
        BasicDBObject orderBy = new BasicDBObject("pt", Constant.DESC);
        List<DBObject> list = find(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_PROJECT, query, Constant.FIELDS, orderBy);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new ProjectEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }
}
