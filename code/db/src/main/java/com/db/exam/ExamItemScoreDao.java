package com.db.exam;

import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.exam.ExamItemScoreEntry;
import com.sys.constants.Constant;
/**
 * 考试小分分数Dao类
 * @author cxy
 *
 */
public class ExamItemScoreDao extends BaseDao{
	/**
	 * 添加一条考试小分分数信息
	 * @param e
	 * @return
	 */
	public ObjectId addExamItemScoreEntry(ExamItemScoreEntry e)
	{
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM_ITEM_SCORE, e.getBaseEntry());
		return e.getID();
	}
	
	/**
	 * 根据Id查询一个特定的考试小分分数信息
	 * @param id
	 * @return
	 */
	public ExamItemScoreEntry getExamItemScoreEntry(ObjectId id)
	{
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM_ITEM_SCORE, query, Constant.FIELDS);
		if(null!=dbo)
		{
			return new ExamItemScoreEntry((BasicDBObject)dbo);
		}
		return null;
	}
	
	
	
	/**
	 * 删除一条考试小分分数
	 * @param id
	 */
	public void deleteExamItemScore(ObjectId id){
		DBObject query =new BasicDBObject(Constant.ID,id);
		remove(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM_ITEM_SCORE, query);
	}
	
	/**
	 * 删除某个考试某个科目的所有小分信息
	 * @param id
	 */
	public void deleteAllExamItemScore(ObjectId examId,ObjectId subjectId){
		DBObject query = new BasicDBObject("eid",examId).append("sid",subjectId);
		remove(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM_ITEM_SCORE, query);
	}
	
	/**
	 * 批量添加小分信息
	 * @param e
	 * @return
	 */
	public void addExamItemScoreEntryList(List<DBObject> list){
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_EXAM_ITEM_SCORE, list);
	}
}
