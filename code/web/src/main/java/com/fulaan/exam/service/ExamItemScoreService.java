package com.fulaan.exam.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.exam.ExamItemScoreDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.exam.ExamItemScoreEntry;
import com.sys.constants.Constant;

/**
 * 考试小分分数service类
 * @author cxy
 *
 */
@Service
public class ExamItemScoreService {
	private ExamItemScoreDao examItemScoreDao = new ExamItemScoreDao();
	
	/**
	 * 添加一条考试小分分数信息
	 * @param e
	 * @return
	 */
	public ObjectId addExamItemScoreEntry(ExamItemScoreEntry e)
	{
		return examItemScoreDao.addExamItemScoreEntry(e);
	}
	
	/**
	 * 根据Id查询一个特定的考试小分分数信息
	 * @param id
	 * @return
	 */
	public ExamItemScoreEntry getExamItemScoreEntry(ObjectId id)
	{
		return examItemScoreDao.getExamItemScoreEntry(id);
	}
	
	
	
	/**
	 * 删除一条考试小分分数
	 * @param id
	 */
	public void deleteExamItemScore(ObjectId id){
		examItemScoreDao.deleteExamItemScore(id);
	}
	
	/**
	 * 删除某个考试某个科目的所有小分信息
	 * @param id
	 */
	public void deleteAllExamItemScore(ObjectId examId,ObjectId subjectId){
		examItemScoreDao.deleteAllExamItemScore(examId, subjectId);
	}
	
	/**
	 * 批量添加小分信息
	 * @param e
	 * @return
	 */
	public void addExamItemScoreEntryList(List<DBObject> list){
		examItemScoreDao.addExamItemScoreEntryList(list);
	}
}
