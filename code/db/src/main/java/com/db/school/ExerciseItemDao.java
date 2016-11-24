package com.db.school;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.exercise.ExerciseItemEntry;
import com.pojo.exercise.ExerciseMixItem;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

/**
 * 练习题目，包括考试等
 * @author fourer
 *
 */
public class ExerciseItemDao extends BaseDao {

	public static final DBObject field =new BasicDBObject("its.$",1).append("di", 1).append("nm", 1).append("ti", 1).append("spt", 1);
	
	public ObjectId addEntry(ExerciseItemEntry e)
	{
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_ITEM_NAME, e.getBaseEntry());
		return e.getID();
	}
	
	/**
	 * 插入多个
	 * @param list
	 */
	public void addEntrys(List<ExerciseItemEntry> list)
	{
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_ITEM_NAME, MongoUtils.fetchDBObjectList(list));
	}
	
	/**
	 * 按照文档ID查询
	 * @param documentId
	 * @return
	 */
	public List<ExerciseItemEntry> getExerciseItemEntrys(ObjectId documentId, DBObject fields)
	{
		List<ExerciseItemEntry> retList =new ArrayList<ExerciseItemEntry>();
		DBObject query =new BasicDBObject("di",documentId);
		List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_ITEM_NAME, query, fields);
		if(null!=list && !list.isEmpty())
		{
			ExerciseItemEntry e;
			for(DBObject dbo:list)
			{
			  e=new ExerciseItemEntry((BasicDBObject)dbo);
			  retList.add(e);
			}
		}
		return retList;
	}
	
	
	/**
	 * 根据小题号查询
	 * @param titleId
	 * @param fields
	 * @return
	 */
	public ExerciseItemEntry getExerciseItemEntryByTitleId(ObjectId titleId,ObjectId docId, DBObject fields)
	{
		DBObject query =new BasicDBObject("its.id",titleId).append("di", docId);
		DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_ITEM_NAME, query, fields);
		if(null!=dbo)
			return new ExerciseItemEntry((BasicDBObject)dbo);
		return null;
	}
	
	/**
	 * 统计每个文档题目个数
	 * @param ids
	 * @return
	 */
	public Map<ObjectId, Integer> statItemCount(Collection<ObjectId> ids)
	{
		Map<ObjectId, Integer> retMap =new HashMap<ObjectId, Integer>();
		DBObject query =new BasicDBObject("di",new BasicDBObject(Constant.MONGO_IN,ids));
		List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_ITEM_NAME, query, new BasicDBObject("its.id",1).append("di", 1));
		if(null!=list && !list.isEmpty())
		{
			ExerciseItemEntry e;
			for(DBObject dbo:list)
			{
			  e=new ExerciseItemEntry((BasicDBObject)dbo);
			  if(!retMap.containsKey(e.getDocumentId()))
			  {
				  retMap.put(e.getDocumentId(), Constant.ZERO);
			  }
			  retMap.put(e.getDocumentId(), retMap.get(e.getDocumentId())+e.getItemList().size());
			}
		}
		return retMap;
	}
	
	/**
	 * 按照文档ID查询
	 * @param documentId
	 * @return
	 */
	public List<ExerciseMixItem> getExerciseMixItems(ObjectId documentId, DBObject fields)
	{
		List<ExerciseMixItem> retList =new ArrayList<ExerciseMixItem>();
		List<ExerciseItemEntry> list =getExerciseItemEntrys(documentId,fields);
		if(!list.isEmpty())
		{
			for(ExerciseItemEntry e:list)
			{
				try
				{
				  retList.addAll(ExerciseMixItem.build(e));
				}catch(Exception ex)
				{
				}
			}
		}
		return retList;
	}
	
	/**
	 * 按照ID查询
	 * @param documentId
	 * @return
	 */
	public ExerciseMixItem getExerciseItemEntry(ObjectId docId,ObjectId id)
	{
		DBObject query =new BasicDBObject("di",docId).append("its.id", id);
		DBObject dbo=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_ITEM_NAME, query, field);
		if(null!=dbo)
		{
			ExerciseItemEntry e =new ExerciseItemEntry((BasicDBObject)dbo);
			ExerciseMixItem item= ExerciseMixItem.build1(e);
			item.setDocumentId(docId);
			return item;
		}
		return null;
	}
	
	
	/**
	 * 根据文档ID删除题目设置
	 * @param docId
	 */
	public void deleteExerciseItems(ObjectId docId)
	{
		DBObject query =new BasicDBObject("di",docId);
		remove(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_ITEM_NAME, query);
	}
	
}
