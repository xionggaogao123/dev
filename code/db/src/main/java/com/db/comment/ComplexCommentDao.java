package com.db.comment;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.comment.ComplexCommentEntry;
import com.sys.constants.Constant;
 
/**
 * 复杂评论Dao
 * 2015-8-20 15:02:02
 * @author cxy
 *
 */
public class ComplexCommentDao extends BaseDao{
	/**
	 * 添加一条评论信息
	 * @param e
	 * @return
	 */
	public ObjectId addComplexCommentEntry(ComplexCommentEntry e){
		save(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_COMPLEX_COMMENT , e.getBaseEntry());
		return e.getID();
	}
	
	/**
	 * 查询所有针对该targetId的一级评论
	 * @return
	 */
	public List<ComplexCommentEntry> queryComplexCommentsByTargetId(ObjectId targetId){
		BasicDBObject query = new BasicDBObject();
		query.append("tid",targetId).append("pid","0");
		DBObject orderBy = new BasicDBObject("ts",Constant.DESC); 
        List<DBObject> dbObjects = find(MongoFacroty.getCloudAppDB(),Constant.COLLECTION_COMPLEX_COMMENT,query,Constant.FIELDS,orderBy);
        List<ComplexCommentEntry> resultList = new ArrayList<ComplexCommentEntry>();
        for(DBObject dbObject:dbObjects){
        	ComplexCommentEntry entry = new ComplexCommentEntry((BasicDBObject)dbObject);
        	resultList.add(entry);
        }
		return resultList;
	}
	
	/**
	 * 查询一个评论的所有回复
	 * @return
	 */
	public List<ComplexCommentEntry> queryComplexCommentsByParentId(String parentId){
		BasicDBObject query = new BasicDBObject();
		query.append("pid",parentId);
		DBObject orderBy = new BasicDBObject("ts",Constant.ASC); 
        List<DBObject> dbObjects = find(MongoFacroty.getCloudAppDB(),Constant.COLLECTION_COMPLEX_COMMENT,query,Constant.FIELDS,orderBy);
        List<ComplexCommentEntry> resultList = new ArrayList<ComplexCommentEntry>();
        for(DBObject dbObject:dbObjects){
        	ComplexCommentEntry entry = new ComplexCommentEntry((BasicDBObject)dbObject);
        	resultList.add(entry);
        }
		return resultList;
	}
	
	/**
	 * 根据Id查询一个特定的评论信息
	 * @param id
	 * @return
	 */
	public ComplexCommentEntry getComplexCommentEntry(ObjectId id)
	{
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject dbo =findOne(MongoFacroty.getCloudAppDB(), Constant.COLLECTION_COMPLEX_COMMENT, query, Constant.FIELDS);
		if(null!=dbo)
		{
			return new ComplexCommentEntry((BasicDBObject)dbo);
		}
		return null;
	}
}
