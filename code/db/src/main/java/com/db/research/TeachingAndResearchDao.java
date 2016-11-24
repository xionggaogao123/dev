package com.db.research;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.research.TeachingAndResearchEntry;
import com.sys.constants.Constant;

/**
 * 
 * @author zk create on 2015/08/26
 */
public class TeachingAndResearchDao extends BaseDao {
	/*
	 * 根据教研专题分类查询
	 */
	public List<TeachingAndResearchEntry> findByTeachingandResearchSubjectClassification(
			String teachingandResearchSubjectClassification, int skip, int size) {
		DBObject query = null;
		DBObject orderBy = new BasicDBObject(Constant.MONGO_SORT, Constant.DESC);
		if (!StringUtils.isBlank(teachingandResearchSubjectClassification)) {
			query = new BasicDBObject("trsc",teachingandResearchSubjectClassification);
			List<DBObject> dbObjectList;
			if ("全部".equals(teachingandResearchSubjectClassification)) {
				dbObjectList = find(MongoFacroty.getCloudAppDB(),Constant.COLLECTION_TEACHING_AND_RESEARCH, new BasicDBObject(),
									Constant.FIELDS , orderBy, skip, size);
			} else {
				dbObjectList = find(MongoFacroty.getCloudAppDB(),
						Constant.COLLECTION_TEACHING_AND_RESEARCH, query,
						Constant.FIELDS , orderBy, skip, size);
			}
			List<TeachingAndResearchEntry> resultList = new ArrayList<TeachingAndResearchEntry>();
			for (DBObject dbobjectlist : dbObjectList) {
				TeachingAndResearchEntry teachingAndResearchEntry = new TeachingAndResearchEntry((BasicDBObject) dbobjectlist);
				resultList.add(teachingAndResearchEntry); 
			}
			return resultList;

		}
		return null;
	}

	public int countFindByTeachingandResearchSubjectClassification(
			String teachingandResearchSubjectClassification) {
		BasicDBObject query;
		if ("全部".equals(teachingandResearchSubjectClassification)) {
			query = new BasicDBObject();
		} else {
			query = new BasicDBObject("trsc",
					teachingandResearchSubjectClassification);
		}
		return count(MongoFacroty.getCloudAppDB(),
				Constant.COLLECTION_TEACHING_AND_RESEARCH, query);

	}

	/*
	 * 根据教研专题名称(ID)查询
	 */
	public TeachingAndResearchEntry findByTeachingAndResearchTopicsTitle(
			ObjectId id) {
		BasicDBObject query = new BasicDBObject(Constant.ID, id);
		List<DBObject> dbList = MongoFacroty.getCloudAppDB()
				.getCollection(Constant.COLLECTION_TEACHING_AND_RESEARCH)
				.find(query).toArray();
		TeachingAndResearchEntry ta = null;
		for (DBObject dbobject : dbList) {
			ta = new TeachingAndResearchEntry((BasicDBObject) dbobject);
		}
		return ta;
	}

	/*
	 * 关键字查询
	 */
	public List<TeachingAndResearchEntry> findByKeyWord(String keyWord,
			String teachingandResearchSubjectClassification, int skip, int size) {
		BasicDBObject query = null;
		if ("全部".equals(teachingandResearchSubjectClassification)) {
			Pattern pattern = Pattern
					.compile(keyWord, Pattern.CASE_INSENSITIVE);
			query = new BasicDBObject("tartt", pattern);
			List<DBObject> dbList = MongoFacroty.getCloudAppDB()
					.getCollection(Constant.COLLECTION_TEACHING_AND_RESEARCH)
					.find(query).skip(skip).limit(size).toArray();
			List<TeachingAndResearchEntry> list = new ArrayList<TeachingAndResearchEntry>();
			for (DBObject dbobject : dbList) {
				TeachingAndResearchEntry tart = new TeachingAndResearchEntry(
						(BasicDBObject) dbobject);
				list.add(tart);
			}
			return list;
		}
		if (!StringUtils.isBlank(keyWord)) {
			Pattern pattern = Pattern
					.compile(keyWord, Pattern.CASE_INSENSITIVE);
			query = new BasicDBObject("tartt", pattern).append("trsc",
					teachingandResearchSubjectClassification);

		}
		List<DBObject> dbList = MongoFacroty.getCloudAppDB()
				.getCollection(Constant.COLLECTION_TEACHING_AND_RESEARCH)
				.find(query).skip(skip).limit(size).toArray();
		List<TeachingAndResearchEntry> list = new ArrayList<TeachingAndResearchEntry>();
		for (DBObject dbobject : dbList) {
			TeachingAndResearchEntry tart = new TeachingAndResearchEntry(
					(BasicDBObject) dbobject);
			list.add(tart);
		}
		return list;
	}

	public int countFindByKeyWordService(String keyWord,
			String teachingandResearchSubjectClassification) {
		BasicDBObject query = null;
		if ("全部".equals(teachingandResearchSubjectClassification)) {
			Pattern pattern = Pattern
					.compile(keyWord, Pattern.CASE_INSENSITIVE);
			query = new BasicDBObject("tartt", pattern);
			return count(MongoFacroty.getCloudAppDB(),
					Constant.COLLECTION_TEACHING_AND_RESEARCH, query);
		}
		if (!StringUtils.isBlank(keyWord)) {
			Pattern pattern = Pattern
					.compile(keyWord, Pattern.CASE_INSENSITIVE);
			query = new BasicDBObject("tartt", pattern).append("trsc",
					teachingandResearchSubjectClassification);

		}
		return count(MongoFacroty.getCloudAppDB(),
				Constant.COLLECTION_TEACHING_AND_RESEARCH, query);
	}

	/*
	 * 创建教研专题
	 */
	public boolean save(String teachingAndResearchTopicsTitle,
			String contentText,
			String teachingandResearchSubjectClassification, String cover,
			ObjectId userId, String userName) {
		TeachingAndResearchEntry teach = new TeachingAndResearchEntry();
		teach.setTeachingAndResearchTopicsTitle(teachingAndResearchTopicsTitle);
		teach.setPublishTime();
		teach.setContentText(contentText);
		teach.setTeachingandResearchSubjectClassification(teachingandResearchSubjectClassification);
		teach.setCoverImage(cover);
		teach.setProjectUserId(userId);
		teach.setProjectUserName(userName);
		save(MongoFacroty.getCloudAppDB(),
				Constant.COLLECTION_TEACHING_AND_RESEARCH, teach.getBaseEntry());

		return true;
	}
}
