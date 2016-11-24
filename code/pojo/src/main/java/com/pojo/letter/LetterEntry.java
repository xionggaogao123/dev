package com.pojo.letter;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;

/**
 * 信件信息
 * <pre>
 * collectionName:letters
 * </pre>
 * <pre>
 * {
 *  si：发送人ID
 *  
 *  ty:信件种类 ；详见LetterType
 *  st:信件状态 ；详见LetterState
 *  con:信件内容
 *  res:信件接受信息；详见ReceiveInfo
 *  [
 *   
 *  ]
 *  exd:附加数据
 *  {
 *   
 *  }
 * }
 * </pre>
 * @author fourer
 */
public class LetterEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8646957128963417447L;

	
	
	public LetterEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}


	public LetterEntry(ObjectId senderId, String content,ObjectId receiverId) {
		ReceiveInfo info =new ReceiveInfo(receiverId, LetterState.LETTER_SEDND_SUCCESS.getState(), null);
		List<ReceiveInfo> list =new ArrayList<ReceiveInfo>();
		list.add(info);
		initLetterEntry(senderId,LetterType.COMMON_LETTER.getType(),LetterState.LETTER_SEDND_SUCCESS.getState(),content,list,null);
	}
	
	
	public LetterEntry(ObjectId senderId, String content,List<ObjectId> receiverIds) {
		List<ReceiveInfo> list =new ArrayList<ReceiveInfo>();
		for(ObjectId receiverId:receiverIds)
		{
			ReceiveInfo info =new ReceiveInfo(receiverId, LetterState.LETTER_SEDND_SUCCESS.getState(), null);
			list.add(info);
		}
		initLetterEntry(senderId,LetterType.COMMON_LETTER.getType(),LetterState.LETTER_SEDND_SUCCESS.getState(),content,list,null);
	}
	
	
	
	public LetterEntry(ObjectId senderId, int type, int state, String content,
			List<ReceiveInfo> receiveList, BasicDBObject extraData) {
		super();
		initLetterEntry(senderId, type, state, content, receiveList, extraData);
	}

	
	
	private void initLetterEntry(ObjectId senderId, int type, int state,
			String content, List<ReceiveInfo> receiveList,
			BasicDBObject extraData) {
		List<DBObject> list=MongoUtils.fetchDBObjectList(receiveList);
		BasicDBObject baseEntry =new BasicDBObject()
		.append("si", senderId)
		.append("ty", type)
		.append("st", state)
		.append("con", content)
		.append("res", MongoUtils.convert(list))
		.append("exd", extraData);
		setBaseEntry(baseEntry);
	}
	
	
	
	public ObjectId getSenderId() {
		return getSimpleObjecIDValue("si");
	}
	public void setSenderId(ObjectId senderId) {
		setSimpleValue("si", senderId);
	}
	public int getType() {
		return getSimpleIntegerValue("ty");
	}
	public void setType(int type) {
		setSimpleValue("ty", type);
	}
	public int getState() {
		return getSimpleIntegerValue("st");
	}
	public void setState(int state) {
		setSimpleValue("st", state);
	}
	public String getContent() {
		return getSimpleStringValue("con");
	}
	public void setContent(String content) {
		setSimpleValue("con", content);
	}
	public List<ReceiveInfo> getReceiveList() {
		List<ReceiveInfo> retList =new ArrayList<ReceiveInfo>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("res");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(new ReceiveInfo((BasicDBObject)o));
			}
		}
		return retList;
	}
	public void setReceiveList(List<ReceiveInfo> receiveList) {
		List<DBObject> list=MongoUtils.fetchDBObjectList(receiveList);
		setSimpleValue("res", MongoUtils.convert(list));
	}
	public BasicDBObject getExtraData() {
		return (BasicDBObject)getSimpleObjectValue("exd");
	}
	public void setExtraData(BasicDBObject extraData) {
		setSimpleValue("exd", extraData);
	}
	
}
