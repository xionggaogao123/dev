package com.db.school;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdValuePair;
import com.pojo.exercise.ExerciseAnswerEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.ResultTooManyException;

import org.bson.types.ObjectId;

import java.util.*;

/**
 * 操作班级文档答案
 * @author fourer
 *
 */
public class ExerciseAnswerDao extends BaseDao {

	public ObjectId addEntry(ExerciseAnswerEntry e) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_ANSWER_NAME, e.getBaseEntry());
		return e.getID();
	}

	public ObjectId addTempEntry(ExerciseAnswerEntry e) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_ANSWER_TEMP, e.getBaseEntry());
		return e.getID();
	}
	
	
	
	/**
	 * 插入多个
	 * @param list
	 */
	public void addSExerciseAnswerEntrys(List<ExerciseAnswerEntry> list, Boolean isTemp)
	{
		String collectionName = isTemp ? Constant.COLLECTION_EXERCISE_ANSWER_TEMP : Constant.COLLECTION_EXERCISE_ANSWER_NAME;
		save(MongoFacroty.getAppDB(), collectionName, MongoUtils.fetchDBObjectList(list));
	}
	
	
	/**
	 * 根据文档ID，题目ID，用户ID查询
	 * @param documentId 必须参数
	 * @param titleId 不为null时生效
	 * @param userId 不为null时生效
	 * @param fields
	 * @return
	 * @throws ResultTooManyException
	 */
	public List<ExerciseAnswerEntry> getListByDocIdAndItemId(ObjectId documentId, ObjectId titleId, ObjectId userId,DBObject fields) throws ResultTooManyException
	{
		List<ExerciseAnswerEntry> retList =new ArrayList<ExerciseAnswerEntry>();
		BasicDBObject query =new BasicDBObject();
		if(null!=documentId)
		{
			query.put("di", documentId);
		}
		if(null!=titleId)
		{
			query.put("ti", titleId);
		}
		if(null!=userId)
		{
			query.put("ui", userId);
		}
		
		if(query.isEmpty())
		{
			throw new ResultTooManyException();
		}
		
		List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_ANSWER_NAME, query, fields);
		if(null!=list && !list.isEmpty())
		{
			ExerciseAnswerEntry e;
			for(DBObject dbo:list)
			{
				e=new ExerciseAnswerEntry((BasicDBObject)dbo);
				retList.add(e);
			}
		}
		return retList;
	}
	
	
	
	/**
	 * 
	 * @param userId
	 * @param documentIds
	 * @param fields
	 * @return
	 */
	public List<ExerciseAnswerEntry>  getList(ObjectId userId,List<ObjectId> documentIds,DBObject fields, Boolean isTemp)
	{
		String collectionName = isTemp ? Constant.COLLECTION_EXERCISE_ANSWER_TEMP : Constant.COLLECTION_EXERCISE_ANSWER_NAME;
		List<ExerciseAnswerEntry> retList =new ArrayList<ExerciseAnswerEntry>();
		DBObject query =new BasicDBObject("di",new BasicDBObject(Constant.MONGO_IN,documentIds)).append("ui", userId);
		List<DBObject> list=find(MongoFacroty.getAppDB(), collectionName, query, fields);
		if(null!=list && !list.isEmpty())
		{
			ExerciseAnswerEntry e;
			for(DBObject dbo:list)
			{
				e=new ExerciseAnswerEntry((BasicDBObject)dbo);
				retList.add(e);
			}
		}
		return retList;
	}
	
	/**
	 * 
	 * @param ids 根据_id查询
	 * @param fields
	 * @return
	 */
	public List<ExerciseAnswerEntry>  getList(List<ObjectId> ids,DBObject fields)
	{
		List<ExerciseAnswerEntry> retList =new ArrayList<ExerciseAnswerEntry>();
		DBObject query =new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,ids));
		List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_ANSWER_NAME, query, fields);
		if(null!=list && !list.isEmpty())
		{
			ExerciseAnswerEntry e;
			for(DBObject dbo:list)
			{
				e=new ExerciseAnswerEntry((BasicDBObject)dbo);
				retList.add(e);
			}
		}
		return retList;
	}
	
	/**
	 * 删除
	 * @param userId
	 * @param docId
	 */
	public void delete(ObjectId userId,ObjectId docId)
	{
		DBObject query =new BasicDBObject("di",docId).append("ui", userId);
		remove(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_ANSWER_NAME, query);
	}

	public void deleteTemp(ObjectId userId,ObjectId docId)
	{
		DBObject query =new BasicDBObject("di",docId).append("ui", userId);
		remove(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_ANSWER_TEMP, query);
	}
	
	/**
	 * 更改分数字段，用于老师评分
	 * @param docId
	 * @param titleId
	 * @param ui
	 * @param score
	 */
	public void updateScore(ObjectId id,Double score )
	{
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("so",score));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_ANSWER_NAME, query, updateValue);
	}
	
	/**
	 * 更改答案
	 * @param docId
	 * @param titleId
	 * @param answer
	 * @param userScore
	 */
	public void updateAnswer(ObjectId id,String answer,double userScore)
	{
		DBObject query =new BasicDBObject(Constant.ID, id);
		BasicDBObject setValue =new BasicDBObject("an",answer);
		if(userScore>-1D)
		{
			setValue.append("so", userScore);
		}
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,setValue);
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_ANSWER_NAME, query, updateValue);
	}
	
	
	
	
	/**
	 * 更改答案
	 * @param docId
	 * @param titleId
	 * @param answer
	 * @param userScore
	 */
	public void updateAnswer(ObjectId docId,ObjectId titleId ,ObjectId userId,  String answer,double userScore, int isRight, Boolean isTemp)
	{
		String collectionName = isTemp ? Constant.COLLECTION_EXERCISE_ANSWER_TEMP : Constant.COLLECTION_EXERCISE_ANSWER_NAME;
		DBObject query =new BasicDBObject("di", docId).append("ti", titleId).append("ui", userId);
		BasicDBObject setValue =new BasicDBObject("an",answer).append("ir", isRight);
		if(userScore>-1D)
		{
			setValue.append("so", userScore);
		}
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,setValue);
		update(MongoFacroty.getAppDB(),collectionName , query, updateValue);
	}
	
	
	
	/**
	 * 增加一个图片
	 * @param id
	 * @param pair
	 */
	public void addImage(ObjectId id,IdValuePair pair)
	{
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("ims",pair.getBaseEntry()));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_ANSWER_NAME, query, updateValue);
	}

	public void addTempImage(ObjectId stuId, ObjectId titleId,IdValuePair pair)
	{
		DBObject query =new BasicDBObject("ui",stuId).append("ti", titleId);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("ims",pair.getBaseEntry()));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_ANSWER_TEMP, query, updateValue);
	}
	
	
	/**
	 * 删除一个图片
	 * @param id
	 * @param pair
	 */
	public void removeImage(ObjectId id,IdValuePair pair)
	{
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_PULL,new BasicDBObject("ims",pair.getBaseEntry()));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_ANSWER_NAME, query, updateValue);
	}

	public void removeTempImage(ObjectId userId, ObjectId titleId,IdValuePair pair)
	{
		DBObject query =new BasicDBObject("ui",userId).append("ti", titleId);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_PULL,new BasicDBObject("ims",pair.getBaseEntry()));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_ANSWER_TEMP, query, updateValue);
	}
	
	/**
	 * 详情
	 * @param id
	 * @return
	 */
	public ExerciseAnswerEntry getExerciseAnswerEntry(ObjectId id)
	{
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject dbo=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EXERCISE_ANSWER_NAME, query, Constant.FIELDS);
		if(null!=dbo)
		{
			return new ExerciseAnswerEntry((BasicDBObject)dbo);
		}
		return null;
	}
	
    /*
    * 根据userId  查找entry
    *
    * */
    public List<ExerciseAnswerEntry> findAnswerByUserIds(Collection<ObjectId> studentIds) {
        DBObject query =new BasicDBObject("ui",new BasicDBObject(Constant.MONGO_IN,studentIds));
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(),Constant.COLLECTION_EXERCISE_ANSWER_NAME,query,new BasicDBObject("ui",1));

        List<ExerciseAnswerEntry> exerciseAnswerEntries=new ArrayList<ExerciseAnswerEntry>();
        if(dbObjectList!=null){
            for(DBObject dbObject:dbObjectList){
                ExerciseAnswerEntry exerciseAnswerEntry=new ExerciseAnswerEntry((BasicDBObject)dbObject);
                exerciseAnswerEntries.add(exerciseAnswerEntry);
            }
        }
        return exerciseAnswerEntries;
    }
    /*
    * 学生作答的全部正确答案 仅有文档id字段 和是否争取ir 其他为空
    *
    * */
    public List<ExerciseAnswerEntry> findByUserId(ObjectId studentId) {
        BasicDBObject query=new BasicDBObject("ui",studentId);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(),Constant.COLLECTION_EXERCISE_ANSWER_NAME,query,new BasicDBObject("di",1).append("ir",1));
        List<ExerciseAnswerEntry> exerciseAnswerEntries=new ArrayList<ExerciseAnswerEntry>();
        if(dbObjectList!=null){
            for(DBObject dbObject:dbObjectList){
                ExerciseAnswerEntry exerciseAnswerEntry=new ExerciseAnswerEntry((BasicDBObject)dbObject);
                exerciseAnswerEntries.add(exerciseAnswerEntry);
            }
        }
        return exerciseAnswerEntries;
    }

    /**
     * 获取统计对象试卷完成信息
     * @param stuIds
     * @param exIds
     * @param dslId
     * @param delId
     * @param orderBy
     * @return
     */
    public Map<String,ExerciseAnswerEntry> getPapersCompletionByParamList(List<ObjectId> stuIds, List<ObjectId> exIds, ObjectId dslId, ObjectId delId, String orderBy) {
        BasicDBObject query =new BasicDBObject("ui",new BasicDBObject(Constant.MONGO_IN,stuIds));

        if(exIds!=null&&exIds.size()>0){
            query.append("di",new BasicDBObject(Constant.MONGO_IN,exIds));
        }
        BasicDBList dblist =new BasicDBList();
        if(dslId!=null){
            dblist.add(new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_GTE, dslId)));
        }
        if(delId!=null){
            dblist.add(new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_LTE, delId)));
        }
        if(dblist.size()>0){
            query.append(Constant.MONGO_AND,dblist);
        }

        BasicDBObject sort =null;
        if (!"".equals(orderBy)){
            sort =new BasicDBObject(orderBy,Constant.DESC);
        }else{
            sort =new BasicDBObject(Constant.ID,Constant.DESC);
        }

        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(),Constant.COLLECTION_EXERCISE_ANSWER_NAME,query,new BasicDBObject("di",1).append("ui",1),sort);

        Map<String,ExerciseAnswerEntry> EAEMap=new HashMap<String, ExerciseAnswerEntry>();
        if(dbObjectList!=null){
            for(DBObject dbObject:dbObjectList){
                ExerciseAnswerEntry EAEntry=new ExerciseAnswerEntry((BasicDBObject)dbObject);
                String key=EAEntry.getDocumentId().toString()+EAEntry.getUserId().toString();
                EAEMap.put(key,EAEntry);
            }
        }
        return EAEMap;
    }
}
