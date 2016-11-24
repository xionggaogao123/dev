package com.fulaan.exam.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.exam.ExamItemDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.exam.ExamItemEntry;
import com.sys.constants.Constant;

/**
 * 考试小分类型设置service类
 * @author cxy
 *
 */
@Service
public class ExamItemService {
	private ExamItemDao examItemDao = new ExamItemDao();
	
	/**
	 * 添加一条考试小分类型信息
	 * @param e
	 * @return
	 */
	public ObjectId addExamItemEntry(ExamItemEntry e)
	{
		return examItemDao.addExamItemEntry(e);
	}
	
	/**
	 * 根据Id查询一个特定的考试小分类型信息
	 * @param id
	 * @return
	 */
	public ExamItemEntry getExamItemEntry(ObjectId id)
	{
		return examItemDao.getExamItemEntry(id);
	}
	
	
	
	/**
	 * 删除一条考试小分类型
	 * @param id
	 */
	public void deleteExamItem(ObjectId id){
		examItemDao.deleteExamItem(id);
	}
	
	/**
	 * 根据ID更新一条等级信息
	 */
	public void changeToParentByIds(ObjectId examId,ObjectId subjectId,String itemId){

		examItemDao.changeToParentByIds(examId, subjectId, itemId);
		
	}
	
	/**
	 * 根据考试ID和科目ID查询所有的小分分类信息
	 * @param id
	 * @return
	 */
	public List<ExamItemEntry> queryExamItemEntriesByIds(ObjectId examId,ObjectId subjectId)
	{
		return examItemDao.queryExamItemEntriesByIds(examId, subjectId);
	}
	
	/**
	 * 删除一个考试一个科目的所有小分分类
	 * @param id
	 */
	public void deleteAllExamItem(ObjectId examId,Object subjectId){
		examItemDao.deleteAllExamItem(examId, subjectId);
	}
}
