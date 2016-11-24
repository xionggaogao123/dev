package com.db.guard;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.guard.VisitorEntry;
import com.sys.constants.Constant;

/** 
 * @author chengwei@ycode.cn
 * @version 2015年12月8日 下午1:50:55 
 * 类说明 
 */
public class VisitDao extends BaseDao{
	
	/**
	 * 增加来访记录(cw)
	 * @param e
	 * @return
	 */
	public ObjectId addVisitorEntry(VisitorEntry e){
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_VISIT, e.getBaseEntry());
		return e.getID();
	}
	
	/**
	 * 删除一条访客记录(cw)
	 * @param id
	 */
	public void deleteVisitorEntry(ObjectId id){
		DBObject query = new BasicDBObject(Constant.ID, id);
		DBObject updateVaue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ir", Constant.ONE));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_VISIT, query, updateVaue);
	}
	
	/**
	 * 查询所有访客记录(cw)
	 * @return
	 */
	public List<VisitorEntry> queryVisitors(int skip,int size){
		DBObject query = new BasicDBObject("ir",Constant.ZERO);
		DBObject orderBy = new BasicDBObject("vt", Constant.DESC);
		List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_VISIT, query, Constant.FIELDS, orderBy,skip,size);
		List<VisitorEntry> resultList = new ArrayList<VisitorEntry>();
		for(DBObject dbObject : dbObjects){
			VisitorEntry visitorEntry = new VisitorEntry((BasicDBObject)dbObject);
			resultList.add(visitorEntry);
		}
		return resultList;
	}
	
	/**
	 * 根据id查询一条访客记录
	 * @param id
	 * @return
	 */
	public VisitorEntry getVisitor(ObjectId id){
		DBObject query = new BasicDBObject(Constant.ID, id);
		DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_VISIT, query, Constant.FIELDS);
		return new VisitorEntry((BasicDBObject)dbObject);
	}
	
	/**
	 * 获取访客人数
	 */
	public int countVisit(){
		return count(MongoFacroty.getAppDB(), Constant.COLLECTION_VISIT, new BasicDBObject("ir",Constant.ZERO));
	}
}
