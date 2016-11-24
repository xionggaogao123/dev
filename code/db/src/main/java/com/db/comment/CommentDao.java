package com.db.comment;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.comment.CommentEntry;
import com.pojo.comment.ComplexCommentEntry;
import com.sys.constants.Constant;
/**
 * 评论Dao
 * 2015-8-19 15:00:57
 * @author cxy
 *
 */
public class CommentDao extends BaseDao{
	/**
	 * 添加一条评论信息
	 * @param e
	 * @return
	 */
	public ObjectId addCommentEntry(CommentEntry e){
		save(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_COMMENT , e.getBaseEntry());
		return e.getID();
	}
	
	/**
	 * 查询所有针对该targetId的一级评论
	 * @return
	 */
	public List<CommentEntry> queryCommentsByTargetId(ObjectId targetId){
		BasicDBObject query = new BasicDBObject();
		query.append("tid",targetId).append("pid","0");
		DBObject orderBy = new BasicDBObject("ts",Constant.DESC); 
        List<DBObject> dbObjects = find(MongoFacroty.getCloudAppDB(),Constant.COLLECTION_COMMENT,query,Constant.FIELDS,orderBy);
        List<CommentEntry> resultList = new ArrayList<CommentEntry>();
        for(DBObject dbObject:dbObjects){
        	CommentEntry entry = new CommentEntry((BasicDBObject)dbObject);
        	resultList.add(entry);
        }
		return resultList;
	}
	
	/**
	 * 查询一个评论的所有回复
	 * @return
	 */
	public List<CommentEntry> queryCommentsByParentId(String parentId){
		BasicDBObject query = new BasicDBObject();
		query.append("pid",parentId);
		DBObject orderBy = new BasicDBObject("ts",Constant.ASC); 
        List<DBObject> dbObjects = find(MongoFacroty.getCloudAppDB(),Constant.COLLECTION_COMMENT,query,Constant.FIELDS,orderBy);
        List<CommentEntry> resultList = new ArrayList<CommentEntry>();
        for(DBObject dbObject:dbObjects){
        	CommentEntry entry = new CommentEntry((BasicDBObject)dbObject);
        	resultList.add(entry);
        }
		return resultList;
	}
	
	/**
	 * 根据Id查询一个特定的评论信息
	 * @param id
	 * @return
	 */
	public CommentEntry getCommentEntry(ObjectId id)
	{
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject dbo =findOne(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_COMMENT, query, Constant.FIELDS);
		if(null!=dbo)
		{
			return new CommentEntry((BasicDBObject)dbo);
		}
		return null;
	}
}
