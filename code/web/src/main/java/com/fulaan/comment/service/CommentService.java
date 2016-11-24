package com.fulaan.comment.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.comment.CommentDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.comment.CommentEntry;
import com.sys.constants.Constant;
/**
 * 普通评论Service
 * 2015-8-30 20:05:49
 * @author cxy
 *
 */
@Service
public class CommentService {
	private CommentDao commentDao = new CommentDao();
	/**
	 * 添加一条评论信息
	 * @param e
	 * @return
	 */
	public ObjectId addCommentEntry(CommentEntry e){
		return commentDao.addCommentEntry(e);
	}
	
	/**
	 * 查询所有针对该targetId的一级评论
	 * @return
	 */
	public List<CommentEntry> queryCommentsByTargetId(ObjectId targetId){
		return commentDao.queryCommentsByTargetId(targetId);
	}
	
	/**
	 * 查询一个评论的所有回复
	 * @return
	 */
	public List<CommentEntry> queryCommentsByParentId(String parentId){
		return commentDao.queryCommentsByParentId(parentId);
	}
	
	/**
	 * 根据Id查询一个特定的评论信息
	 * @param id
	 * @return
	 */
	public CommentEntry getCommentEntry(ObjectId id)
	{
		return commentDao.getCommentEntry(id);
	}
}
