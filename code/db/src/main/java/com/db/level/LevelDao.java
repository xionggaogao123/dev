package com.db.level;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.level.LevelEntry;
import com.sys.constants.Constant;

/**
 * @author cxy
 * 2015-7-26 17:49:48
 * 
 *  等级设置Dao类
 */
public class LevelDao extends BaseDao{
	/**
	 * 添加一条等级信息
	 * @param e
	 * @return
	 */
	public ObjectId addLevelEntry(LevelEntry e)
	{
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_LEVEL, e.getBaseEntry());
		return e.getID();
	}
	
	/**
	 * 根据Id查询一个特定的等级信息
	 * @param id
	 * @return
	 */
	public LevelEntry getLevelEntry(ObjectId id)
	{
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_LEVEL, query, Constant.FIELDS);
		if(null!=dbo)
		{
			return new LevelEntry((BasicDBObject)dbo);
		}
		return null;
	}
	
	/**
	 * 删除一条等级
	 * @param id
	 */
	public void deleteLevel(ObjectId id){
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject().append("ir", 1));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_LEVEL, query, updateValue);
	}
	
	/**
	 * 根据ID更新一条等级信息
	 */
	public void updateLevel(ObjectId id,String levelName,int scoreRange){

		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,
													new BasicDBObject()
													.append("ln", levelName)
													.append("sr", scoreRange));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_LEVEL, query, updateValue);
		
	}
	/**
	 * 查询本校所有的等级记录
	 * @return
	 */
	public List<LevelEntry> queryLevelsBySchoolId(ObjectId schoolId){
		BasicDBObject query = new BasicDBObject();
		query.append("ir", Constant.ZERO)
			 .append("scid",schoolId);
		DBObject orderBy = new BasicDBObject("sr",Constant.DESC); 
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(),Constant.COLLECTION_LEVEL,query,Constant.FIELDS,orderBy);
        List<LevelEntry> resultList = new ArrayList<LevelEntry>();
        for(DBObject dbObject:dbObjects){
        	LevelEntry repairEntry = new LevelEntry((BasicDBObject)dbObject);
        	resultList.add(repairEntry);
        }
		return resultList;
	}
}
