package com.pojo.letter;

import org.bson.types.ObjectId;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * 信件收受信息
 * ri:收信件人的ID
 * st:信件状态; 参见LetterState
 * rpi:信件回复ID
 * @author fourer
 *
 */
public class ReceiveInfo extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8141221991971452011L;
    
    
	public ReceiveInfo(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public ReceiveInfo(ObjectId receiverId, int state, ObjectId replyId) {
		super();
		BasicDBObject baseEntry =new BasicDBObject("ri",receiverId)
		.append("st", state)
		.append("rpi", replyId);
		setBaseEntry(baseEntry);
	}


	public ObjectId getReceiverId() {
		return getSimpleObjecIDValue("ri");
	}
	public void setReceiverId(ObjectId receiverId) {
		setSimpleValue("ri", receiverId);
	}
	public int getState() {
		return getSimpleIntegerValue("st");
	}
	public void setState(int state) {
		setSimpleValue("st", state);
	}
	public ObjectId getReplyId() {
		return getSimpleObjecIDValue("rpi");
	}
	public void setReplyId(ObjectId replyId) {
		setSimpleValue("rpi", replyId);
	}
}
