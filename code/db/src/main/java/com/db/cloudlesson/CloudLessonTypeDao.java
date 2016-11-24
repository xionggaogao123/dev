package com.db.cloudlesson;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.cloudlesson.CloudLessonTypeEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 云课程类别操作类
 * @author fourer
 *
 */
public class CloudLessonTypeDao extends BaseDao {

	public ObjectId addCloudLessonTypeEntry(CloudLessonTypeEntry e)
	{
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_CLOUDLESSONTYPE_NAME, e.getBaseEntry());
		return e.getID();
	}
	
	/**
	 * 更新字段
	 * @param id
	 * @param field
	 * @param value
	 */
	public void updateValue(ObjectId id,String field,Object value)
	{
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject(field,value));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLOUDLESSONTYPE_NAME, query, updateValue);
	}
	
	/**
	 * 查询
	 * @param ids
	 * @return
	 */
	public List<CloudLessonTypeEntry> getList(List<ObjectId> ids)
	{
		List<CloudLessonTypeEntry> retList =new ArrayList<CloudLessonTypeEntry>();
		BasicDBObject query =new BasicDBObject();
		query.append(Constant.ID, new BasicDBObject(Constant.MONGO_IN,ids));
	   	List<DBObject> list=	find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLOUDLESSONTYPE_NAME, query, Constant.FIELDS);
		
	   	if(null!=list && list.size()>0)
		{
		   	for(DBObject dbo:list)
		   	{
			   retList.add(new CloudLessonTypeEntry((BasicDBObject)dbo));
		   	}
	   	}
	   	return retList;
	}

	/**
	 * 查询
	 * @param schoolType 学校类型；大于-1时生效
	 * @param SubjectType 科目类型；大于-1时生效
	 * @param CloudLessonGradeType 云课程年级类型；大于-1时生效
	 * @return
	 */
	public List<CloudLessonTypeEntry> getList(int schoolType,int SubjectType,int CloudLessonGradeType)
	{
		List<CloudLessonTypeEntry> retList =new ArrayList<CloudLessonTypeEntry>();
		BasicDBObject query =new BasicDBObject();
		if(schoolType>Constant.NEGATIVE_ONE)
		{
			query.append("sty", schoolType);
		}
		if(SubjectType>Constant.NEGATIVE_ONE)
		{
			query.append("sub", SubjectType);
		}
		if(CloudLessonGradeType>Constant.NEGATIVE_ONE)
		{
			query.append("ccgt", CloudLessonGradeType);
		}

		List<DBObject> list=	find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLOUDLESSONTYPE_NAME, query, Constant.FIELDS);

		if(null!=list && list.size()>0)
		{
			for(DBObject dbo:list)
			{
				retList.add(new CloudLessonTypeEntry((BasicDBObject)dbo));
			}
		}
		return retList;
	}

	public Map<ObjectId, CloudLessonTypeEntry> getCloudLessonEntryMap(List<ObjectId> ids) {
		Map<ObjectId, CloudLessonTypeEntry> retMap =new HashMap<ObjectId, CloudLessonTypeEntry>();
		BasicDBObject query =new BasicDBObject();
		query.append(Constant.ID, new BasicDBObject(Constant.MONGO_IN,ids));
		List<DBObject> list=	find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLOUDLESSONTYPE_NAME, query, Constant.FIELDS);

		if(null!=list && list.size()>0)
		{
			CloudLessonTypeEntry e;
			for(DBObject dbo:list)
			{
				e=new CloudLessonTypeEntry((BasicDBObject)dbo);
				retMap.put(e.getID(), e);
			}
		}
		return retMap;
	}
}
