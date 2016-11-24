package com.pojo.letter;


import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;


/**
 * 信件信息
 * <pre>
 * collectionName:letterrecords
 * </pre>
 * <pre>
 * {
 *  ui: 用户ID
 *  lui:用户ID
 *  us: 参见LetterRecordState
 *  {
 *    id:最新的信件ID
	  ur:是否未读
 *  }
 *  lus: 参见LetterRecordState
 *  {
 *   
 *  }
 * }
 * </pre>
 * @author fourer
 */
public class LetterRecordEntry extends BaseDBObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4376975873951158350L;
	
	public LetterRecordEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	
	
	public LetterRecordEntry(ObjectId myId, ObjectId letterFriendUi,ObjectId recentLetterId) {
		super();
		
		LetterRecordState us =new LetterRecordState(recentLetterId,Constant.ZERO);
		LetterRecordState lus =new LetterRecordState(recentLetterId);
		BasicDBObject dbo =new BasicDBObject()
		.append("ui", myId)
		.append("us", us.getBaseEntry())
		.append("lui", letterFriendUi)
		.append("lus", lus.getBaseEntry());
		setBaseEntry(dbo);
	}
	
	
	public ObjectId getUserId() {
		return getSimpleObjecIDValue("ui");
	}



	public void setUserId(ObjectId userId) {
		setSimpleValue("ui", userId);
	}



	public ObjectId getLetterUserId() {
		return getSimpleObjecIDValue("lui");
	}



	public void setLetterUserId(ObjectId letterUserId) {
		setSimpleValue("lui", letterUserId);
	}

	public LetterRecordState getUserState() {
		BasicDBObject dbo =(BasicDBObject)getSimpleObjectValue("us");
		if(null!=dbo)
		{
			return new LetterRecordState(dbo);
		}
		return null;
	}
	public void setUserState(LetterRecordState userState) {
		setSimpleValue("us", userState.getBaseEntry());
	}
	public LetterRecordState getLetterUserState() {
		BasicDBObject dbo =(BasicDBObject)getSimpleObjectValue("lus");
		if(null!=dbo)
		{
			return new LetterRecordState(dbo);
		}
		return null;
	}
	public void setLetterUserState(LetterRecordState letterUserState) {
		setSimpleValue("lus", letterUserState.getBaseEntry());
	}
	public LetterRecordState getState(String field) {
		BasicDBObject dbo =(BasicDBObject)getSimpleObjectValue(field);
		if(null!=dbo)
		{
			return new LetterRecordState(dbo);
		}
		return null;
	}


	/**
	 * 信件状态
	 * {
	 *  id:最新的信件ID
	 *  ur:是否未读，0没有未读信件，1有未读信件 3已经删除
	 * }
	 * @author fourer
	 *
	 */
	public static class LetterRecordState extends BaseDBObject
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -3466327970976226218L;
	
		
		
		public LetterRecordState(BasicDBObject baseEntry) {
			super(baseEntry);
		}
		public LetterRecordState(ObjectId id) {
			super();
			BasicDBObject dbo =new BasicDBObject()
			.append("id", id)
			.append("ur", Constant.ONE);
			setBaseEntry(dbo);
		}
		
		public LetterRecordState(ObjectId id, int unread) {
			super();
			BasicDBObject dbo =new BasicDBObject()
			.append("id", id)
			.append("ur", unread);
			setBaseEntry(dbo);
		}
		
		public ObjectId getId() {
			return getSimpleObjecIDValue("id");
		}
		public void setId(ObjectId id) {
			setSimpleValue("id", id);
		}
		public Integer getUnRead() {
            return getSimpleIntegerValue("ur");
		}
		public void setUnRead(int unread) {
			setSimpleValue("ur", unread);
		}
	}

}
