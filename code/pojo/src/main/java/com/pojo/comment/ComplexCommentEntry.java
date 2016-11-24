package com.pojo.comment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;

/**
 * @author cxy 
 * 			2015-7-26 17:50:03
 *         复杂评论Entry类 
 *         collectionName : complexcomment 
 *         评论人ID : cuid(commentUserId) 
 *         评论人名称 : cuna(commentUserName) 
 *         回复人ID : ruid(replyUserId) 
 *         回复人名称 : runa(replyUserName) 
 *         回复时间 : ts(timestamp)    long
 *         回复项目ID : tid(targetId) 
 *         优点 : ad(advantage)   130字内
 *         缺点 : wp(weakPoint)   130字内
 *         星级 : sl(starLevel)  详情见StarLevel List<CommentStar>
 *         		[
 *         		 StarLevel
 *         			{
 *         				na (name)
 *         				sl (starNum)1~5
 *         			}
 *         		]
 *         
 *         回复父评论的id: pid(parentId) string 若是回复评论的，就是ObjectId的String形式，若是一级（最上级）评论，则是"0"
 */
public class ComplexCommentEntry extends BaseDBObject{
	public ComplexCommentEntry(BasicDBObject baseEntry) { 
		super(baseEntry);
	}
	
	public ComplexCommentEntry(ObjectId commentUserId,String commentUserName,ObjectId replyUserId,String replyUserName,
							long timestamp,ObjectId targetId,String advantage,String weakPoint,List<StarLevel> starLevel,String parentId) {
		super();

		BasicDBObject baseEntry = new BasicDBObject().append("cuid", commentUserId)
													 .append("cuna", commentUserName)
													 .append("ruid", replyUserId)
													 .append("runa", replyUserName)
													 .append("ts", timestamp)
													 .append("tid", targetId)
													 .append("ad", advantage)
													 .append("wp", weakPoint)
													 .append("sl", MongoUtils.convert(MongoUtils.fetchDBObjectList(starLevel)))
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
	
	public String getAdvantage() {
		return getSimpleStringValue("ad");
	}
	
	public void setAdvantage(String advantage) {
		setSimpleValue("ad", advantage);
	}
	
	public String getWeakPoint() {
		return getSimpleStringValue("wp");
	}
	
	public void setWeakPoint(String weakPoint) {
		setSimpleValue("wp", weakPoint);
	}
	
	public List<StarLevel> getStarLevelList() {
		List<StarLevel> gradeList =new ArrayList<StarLevel>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("sl");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				gradeList.add(new StarLevel((BasicDBObject)o));
			}
		}
		return gradeList;
	}
	public void setStarLevelList(Collection<StarLevel> starLevelList) {
		List<DBObject> list=MongoUtils.fetchDBObjectList(starLevelList);
		setSimpleValue("sl", MongoUtils.convert(list));
	}
	
	public String getParentId() {
		return getSimpleStringValue("pid");
	}
	
	public void setParentId(String parentId) {
		setSimpleValue("pid", parentId);
	}
}
