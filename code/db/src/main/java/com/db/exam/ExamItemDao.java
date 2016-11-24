package com.db.exam;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.exam.ExamItemEntry;
import com.sys.constants.Constant;

/**
 * @author cxy
 * 2015-7-26 17:49:48
 * 
 *  考试小分类型设置Dao类
 */
public class ExamItemDao extends BaseDao{
	/**
	 * 添加一条考试小分类型信息
	 * @param e
	 * @return
	 */
	public ObjectId addExamItemEntry(ExamItemEntry e)
	{
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM_ITEM, e.getBaseEntry());
		return e.getID();
	}
	
	/**
	 * 根据Id查询一个特定的考试小分类型信息
	 * @param id
	 * @return
	 */
	public ExamItemEntry getExamItemEntry(ObjectId id)
	{
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM_ITEM, query, Constant.FIELDS);
		if(null!=dbo)
		{
			return new ExamItemEntry((BasicDBObject)dbo);
		}
		return null;
	}
	
	
	
	/**
	 * 删除一条考试小分类型
	 * @param id
	 */
	public void deleteExamItem(ObjectId id){
		DBObject query =new BasicDBObject(Constant.ID,id);
		remove(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM_ITEM, query);
	}
	
	/**
	 * 根据ID更新一条等级信息
	 */
	public void changeToParentByIds(ObjectId examId,ObjectId subjectId,String itemId){

		DBObject query =new BasicDBObject("eid",examId)
											.append("sid", subjectId)
											.append("iid",itemId);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,
													new BasicDBObject()
													.append("il", Constant.ZERO));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM_ITEM, query, updateValue);
		
	}
	
	/**
	 * 根据考试ID和科目ID查询所有的小分分类信息
	 * @param id
	 * @return
	 */
	public List<ExamItemEntry> queryExamItemEntriesByIds(ObjectId examId,ObjectId subjectId)
	{
		BasicDBObject query = new BasicDBObject();
		query.append("eid", examId)
			 .append("sid",subjectId);
		DBObject orderBy = new BasicDBObject("iid",Constant.ASC); 
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(),Constant.COLLECTION_EXAM_ITEM,query,Constant.FIELDS,orderBy);
        List<ExamItemEntry> resultList = new ArrayList<ExamItemEntry>();
        for(DBObject dbObject:dbObjects){
        	ExamItemEntry entry = new ExamItemEntry((BasicDBObject)dbObject);
        	resultList.add(entry);
        }
		return resultList;
	}
	
	/**
	 * 删除一个考试一个科目的所有小分分类
	 * @param id
	 */
	public void deleteAllExamItem(ObjectId examId,Object subjectId){
		DBObject query =new BasicDBObject("eid",examId).append("sid",subjectId);
		remove(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM_ITEM, query);
	}
}
