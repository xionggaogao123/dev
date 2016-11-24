package com.fulaan.comment.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.comment.ComplexCommentDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.comment.ComplexCommentEntry;
import com.sys.constants.Constant;
/**
 * 复杂评论service
 * 2015-8-26 19:35:34
 * @author cxy
 *
 */
@Service
public class ComplexCommentService {
	
	ComplexCommentDao complexCommentService = new ComplexCommentDao();
	
	/**
	 * 添加一条评论信息
	 * @param e
	 * @return
	 */
	public ObjectId addComplexCommentEntry(ComplexCommentEntry e){
		return complexCommentService.addComplexCommentEntry(e);
	}
	
	/**
	 * 查询所有针对该targetId的一级评论
	 * @return
	 */
	public List<ComplexCommentEntry> queryComplexCommentsByTargetId(ObjectId targetId){
		return complexCommentService.queryComplexCommentsByTargetId(targetId);
	}
	
	/**
	 * 查询一个评论的所有回复
	 * @return
	 */
	public List<ComplexCommentEntry> queryComplexCommentsByParentId(String parentId){
		return complexCommentService.queryComplexCommentsByParentId(parentId);
	}
	/**
	 * 根据Id查询一个特定的校园资产信息
	 * @param id
	 * @return
	 */
	public ComplexCommentEntry getComplexCommentEntry(ObjectId id)
	{
		return complexCommentService.getComplexCommentEntry(id);
	}
}
