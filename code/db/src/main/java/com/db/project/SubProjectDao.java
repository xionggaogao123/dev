package com.db.project;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.project.ProjectEntry;
import com.pojo.project.SubProjectEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
/**
 * 子课题Dao
 * 2015-8-27 20:56:42
 * @author cxy
 *
 */
public class SubProjectDao extends BaseDao{
	/**
	 * 添加一条课题信息
	 * @param e
	 * @return
	 */
	public ObjectId addSubProjectEntry(SubProjectEntry e){
		save(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_SUB_PROJECT , e.getBaseEntry());
		return e.getID();
	}
	/**
     * 根据ID查询
     * @param userId
     * @return
     */
    public SubProjectEntry getSubProjectEntryById(ObjectId id)
    {
        BasicDBObject query =new BasicDBObject(Constant.ID,id);
        DBObject dbo =findOne(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_SUB_PROJECT,query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new SubProjectEntry((BasicDBObject)dbo);
        }
        return null;
    }
    /**
     * 根据父课题ID查找子课题List
     * @param schoolIds
     * @return
     */
    public List<SubProjectEntry> querySubProjectionEntriesByParentId(ObjectId parentId) {
        List<SubProjectEntry> retList = new ArrayList<SubProjectEntry>();
        BasicDBObject query = new BasicDBObject("paid",parentId);
        BasicDBObject orderBy = new BasicDBObject("pt",Constant.ASC);
        List<DBObject> list = find(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_SUB_PROJECT, query, Constant.FIELDS, orderBy);
        if(null!=list && !list.isEmpty())
        {
            for(DBObject dbo:list)
            {
                retList.add(new SubProjectEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }
}
