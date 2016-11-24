package com.db.review;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.preparation.PreparationEntry;
import com.pojo.review.ReviewEntry;
import com.sys.constants.Constant;
import com.sys.utils.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 评课议课持久化
 * Created by Caocui on 2015/8/26.
 */
public class ReviewDao extends BaseDao {

    /**
     * 发布评课议课记录
     *
     * @param reviewEntry
     */
    public ObjectId add(ReviewEntry reviewEntry) {
        this.save(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_REVIEW, reviewEntry.getBaseEntry());
        return reviewEntry.getID();
    }

    /**
     * 获取符合条件的评课议课记录总数
     *
     * @param queryId   查询条件值
     * @param columName 查询条件列
     * @param pageNo    页码
     * @param pageSize  每页记录数
     * @return
     */

    public List<ReviewEntry> findReviewEntry(String queryId, String columName, int pageNo, int pageSize,ObjectId edId) {
        BasicDBObject query = new BasicDBObject("edid",edId).append("ir", Constant.ZERO);
        if (!StringUtils.isEmpty(queryId) && !StringUtils.isEmpty(columName)) {
            query.append(columName, queryId);
        }
        List<DBObject> dbObjects = find(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_REVIEW,
                query, Constant.FIELDS,
                new BasicDBObject("pt", Constant.DESC), (pageNo - Constant.ONE) * pageSize, pageSize);
        List<ReviewEntry> resultList = new ArrayList<ReviewEntry>();
        for (DBObject dbObject : dbObjects) {
            resultList.add(new ReviewEntry((BasicDBObject) dbObject));
        }
        return resultList;
    }

    /**
     * 获取符合条件的评课议课记录总数
     *
     * @param queryId   查询条件值
     * @param columName 查询条件列
     * @return
     */
    public int count(String queryId, String columName,ObjectId edid) {
        BasicDBObject query = new BasicDBObject("edid",edid).append("ir", Constant.ZERO);
        if (!StringUtils.isEmpty(queryId) && !StringUtils.isEmpty(columName)) {
            query.append(columName, queryId);
        }
        return this.count(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_REVIEW, query);
    }

    /**
     * 根据ID获取对应的对象息
     *
     * @param id
     * @return
     */
    public ReviewEntry getReviewEntry(ObjectId id) {
        DBObject object = this.findOne(MongoFacroty.getCloudAppDB(),
                Constant.COLLECTION_REVIEW,
                new BasicDBObject().append(Constant.ID, id),
                Constant.FIELDS);
        return object == null ? null : new ReviewEntry((BasicDBObject) object);
    }
    
    /**
	 * 查询所有的列表信息
	 * @param resourceDictionaryId
	 * @param columName
	 * @return
	 */
	public List<ReviewEntry> getAllReviewEntries(ObjectId edId){
		BasicDBObject query = new BasicDBObject("edid",edId).append("ir", Constant.ZERO);
		DBObject orderBy = new BasicDBObject("pt",Constant.DESC); 
        List<DBObject> dbObjects = find(MongoFacroty.getCloudAppDB(),Constant.COLLECTION_REVIEW,query,Constant.FIELDS,orderBy);
        List<ReviewEntry> resultList = new ArrayList<ReviewEntry>();
        for(DBObject dbObject:dbObjects){
        	ReviewEntry entry = new ReviewEntry((BasicDBObject)dbObject);
        	resultList.add(entry);
        }
		return resultList;
	}

	/**
	 * 根据传入的数据字典ID和字段名查询集体备课List
	 * @param resourceDictionaryId
	 * @param columName
	 * @return
	 */
	public List<ReviewEntry> getReviewEntriesByResourceDictionaryId(String resourceDictionaryId,String columName,ObjectId edId){
		BasicDBObject query = new BasicDBObject("edid",edId).append("ir", Constant.ZERO);
		query.append(columName,resourceDictionaryId);
		DBObject orderBy = new BasicDBObject("pt",Constant.DESC); 
        List<DBObject> dbObjects = find(MongoFacroty.getCloudAppDB(),Constant.COLLECTION_REVIEW,query,Constant.FIELDS,orderBy);
        List<ReviewEntry> resultList = new ArrayList<ReviewEntry>();
        for(DBObject dbObject:dbObjects){
        	ReviewEntry entry = new ReviewEntry((BasicDBObject)dbObject);
        	resultList.add(entry);
        }
		return resultList;
	}
	
	/**
     * 给某一个评课议课去除一个课件
     * @param userId
     * @return
     */
    public void delFileForReview(ObjectId reviewId,ObjectId fileId)
    {
        BasicDBObject query =new BasicDBObject(Constant.ID,reviewId);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_PULL,new BasicDBObject("cs",fileId));
        update(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_REVIEW, query, updateValue);
    }
}
