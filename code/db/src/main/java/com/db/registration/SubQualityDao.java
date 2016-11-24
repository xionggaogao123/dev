package com.db.registration;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.registration.SubQualityEntry;
import com.sys.constants.Constant;
/**
 * 素质教育子项目Dao
 * @author cxy
 * 2015-11-25 14:48:45
 */
public class SubQualityDao extends BaseDao {
	/**
     * 添加素质教育项目
     * @param e
     * @return
     */
    public ObjectId addSubQualityEntry(SubQualityEntry e){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_SUB_QUALITY, e.getBaseEntry());
        return e.getID();
    }
    
    /**
     * 删除一条素质教育项目
     * @param id
     */
    public void deleteSubQualityEntry(ObjectId id){
    	DBObject query = new BasicDBObject(Constant.ID,id);
    	remove(MongoFacroty.getAppDB(), Constant.COLLECTION_SUB_QUALITY, query);
    }
    
    /**
	 * 根据ID更新一条素质教育项目信息
	 */
	public void updateSubQualityEntry(ObjectId id,String name,String requirement){
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,
													new BasicDBObject().append("nm", name).append("rq",requirement));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_SUB_QUALITY, query, updateValue);
		
	}
	
	/**
     * 通过schoolId查询素质教育项目信息
     * @param schoolId
     */
    public List<SubQualityEntry> querySubQualityList(ObjectId parentId) {
        List<SubQualityEntry> retList = new ArrayList<SubQualityEntry>();
        DBObject query =new BasicDBObject("pid",parentId);
        DBObject orderBy = new BasicDBObject("ti",Constant.ASC); 
        List<DBObject> list= find(MongoFacroty.getAppDB(), Constant.COLLECTION_SUB_QUALITY, query, Constant.FIELDS, orderBy);
        if(null!=list && !list.isEmpty()){
            for(DBObject dbo:list){
                retList.add(new SubQualityEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }
    
}
