package com.pojo.emarket;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
/**
 * 评论
 * {
 *  ui:用户ID
 *  com:评论内容
 *  t:时间
 * }
 * @author fourer
 *
 */
public class Comment extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 30911156704785214L;

	
	
	
	public Comment(BasicDBObject baseEntry) {
		super(baseEntry);
	}


	public Comment(ObjectId ui, String comment) {
		super();
		BasicDBObject baseEntry =new BasicDBObject()
		.append("ui", ui)
		.append("com", comment)
		.append("t", System.currentTimeMillis());
		setBaseEntry(baseEntry);
	}
	
	
	public ObjectId getUi() {
		return getSimpleObjecIDValue("ui");
	}
	public void setUi(ObjectId ui) {
		setSimpleValue("ui", ui);
	}
	public String getComment() {
		return getSimpleStringValue("com");
	}
	public void setComment(String comment) {
		setSimpleValue("com", comment);
	}
	public long getTime() {
		return getSimpleLongValue("t");
	}
	public void setTime(long time) {
		setSimpleValue("t", time);
	}
	
}
