package com.pojo.notice;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;

/**
 * 通知阅读表;
 * 注意：NoticeReadsEntry 虽然为一个entry，但是操作类写在noticeDao 中。
 * <pre>
 * ni：通知ID
 * rus:
 * [
 *    id:id
 * ]
 * trus:已读数量
 * </pre>
 * @author fourer
 *
 */
public class NoticeReadsEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9135996582517455058L;
	
	
	
	public NoticeReadsEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	
	public NoticeReadsEntry(ObjectId id,List<ObjectId> users)
	{
		BasicDBObject baseEntry =new BasicDBObject()
		.append("ni", id)
		.append("rus",  MongoUtils.convert(users))
		.append("trus", null==users?0:users.size())
		;
		setBaseEntry(baseEntry);
	}
	
	public ObjectId getNoticeId() {
		return getSimpleObjecIDValue("ni");
	}

	public void setNoticeId(ObjectId noticeId) {
		setSimpleValue("ni", noticeId);
	}

	public void setReadUsers(List<ObjectId> users) {
		setSimpleValue("rus", MongoUtils.convert(users));
	}

	public List<ObjectId> getReadUsers() {
		List<ObjectId> retList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("rus");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add((ObjectId)o);
			}
		}
		return retList;
	}

	
	public int getTotalReadUser() {
		return getSimpleIntegerValueDef("trus", 0);
	}

	public void setTotalReadUser(int totalReadUser) {
		setSimpleValue("trus", totalReadUser);
	}
}
