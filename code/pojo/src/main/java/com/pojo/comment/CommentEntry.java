package com.pojo.comment;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * @author cxy 
 * 			2015-7-26 17:50:03
 *         评论Entry类 
 *         collectionName : comment 
 *         评论人ID : cuid(commentUserId) 
 *         评论人名称 : cuna(commentUserName) 
 *         回复人ID : ruid(replyUserId) 
 *         回复人名称 : runa(replyUserName) 
 *         回复时间 : ts(timestamp)    long
 *         回复项目ID : tid(targetId) 
 *         回复内容 : cc(commentContent)   130字内
 *         回复父评论的id: pid(parentId) string 若是回复评论的，就是ObjectId的String形式，若是一级（最上级）评论，则是"0"
 */
public class CommentEntry extends BaseDBObject{
	
	public CommentEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public CommentEntry(ObjectId commentUserId,String commentUserName,ObjectId replyUserId,String replyUserName,
							long timestamp,ObjectId targetId,String commentContent,String parentId) {
		super();

		BasicDBObject baseEntry = new BasicDBObject().append("cuid", commentUserId)
													 .append("cuna", commentUserName)
													 .append("ruid", replyUserId)
													 .append("runa", replyUserName)
													 .append("ts", timestamp)
													 .append("tid", targetId)
													 .append("cc", commentContent)
													 .append("pid", parentId);

		setBaseEntry(baseEntry);

	}
	
	public ObjectId getCommentUserId() {
		return getSimpleObjecIDValue("cuid");
	}
	public void setCommentUserId(String commentUserId) {
		setSimpleValue("cuid", commentUserId);
	}
	
	public String getCommentUserName() {
		return getSimpleStringValue("cuna");
	}
	
	public void setCommentUserName(String commentUserName) {
		setSimpleValue("cuna", commentUserName);
	}
	
	public ObjectId getReplyUserId() {
		return getSimpleObjecIDValue("ruid");
	}
	public void setReplyUserId(String replyUserId) {
		setSimpleValue("ruid", replyUserId);
	}
	
	public String getReplyUserName() {
		return getSimpleStringValue("runa");
	}
	
	public void setReplyUserName(String replyUserName) {
		setSimpleValue("runa", replyUserName);
	}
	
	public long getTimestamp() {
		return getSimpleLongValue("ts");
	}
	public void setTimestamp(long timestamp) {
		setSimpleValue("ts", timestamp);
	}
	
	public ObjectId getTargetId() {
		return getSimpleObjecIDValue("tid");
	}
	public void setTargetId(String targetId) {
		setSimpleValue("tid", targetId);
	}
	
	public String getCommentContent() {
		return getSimpleStringValue("cc");
	}
	
	public void setCommentContent(String commentContent) {
		setSimpleValue("cc", commentContent);
	}
	
	public String getParentId() {
		return getSimpleStringValue("pid");
	}
	
	public void setParentId(String parentId) {
		setSimpleValue("pid", parentId);
	}
}
