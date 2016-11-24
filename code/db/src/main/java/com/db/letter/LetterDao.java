package com.db.letter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.letter.LetterEntry;
import com.pojo.letter.LetterState;
import com.pojo.letter.ReceiveInfo;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.ResultTooManyException;

/**
 * letter 操作类
 * index:si_ty_res-ri
 *       {"si":1,"ty":1,"res.ri":1}
 * @author fourer

 */
public class LetterDao extends BaseDao {
	/**
	 * 添加一个信件
	 * @param e
	 */
	public ObjectId addLetterEntry(LetterEntry e)
	{
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_LETTER_NAME, e.getBaseEntry());
		return e.getID();
	}
	
	/**
	 * 发送者删除
	 * @param sender
	 * @param letterId
	 */
	public void senderDelete(ObjectId sender,ObjectId letterId)
	{
		DBObject query =new BasicDBObject(Constant.ID,letterId).append("si", sender);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("st",LetterState.LETTER_DELETED.getState()));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_LETTER_NAME, query, updateValue);
	}
	
	/**
	 * 接受者删除
	 * @param receiver
	 * @param letterId
	 */
	public void receiverDelete(ObjectId receiver,ObjectId letterId)
	{
		DBObject query =new BasicDBObject(Constant.ID,letterId).append("res.ri", receiver);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("res.$.st",LetterState.LETTER_DELETED.getState()));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_LETTER_NAME, query, updateValue);
	}
	
	/**
	 * 回复信件
	 * @param letterId
	 * @param receiver
	 * @param replyId
	 */
	public void writeReply(ObjectId letterId,ObjectId receiver,ObjectId replyId)
	{
		DBObject query =new BasicDBObject(Constant.ID,letterId).append("res.ri", receiver);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("res.$.st",LetterState.LETTER_REPLY.getState()).append("res.$.rpi", replyId));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_LETTER_NAME, query, updateValue);
	}

    /**
     * 回复信件
     * @param letterId
     * @param receiver
     * @param replyId
     */
    public void receiverRead(ObjectId letterId,ObjectId receiver)
    {
        DBObject query =new BasicDBObject(Constant.ID,letterId).append("res.ri", receiver);
        DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("res.$.st",LetterState.LETTER_READED.getState()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_LETTER_NAME, query, updateValue);
    }

	

	
	/**
	 * 根据ID查询
	 * @param letterId
	 * @return
	 */
	public LetterEntry getLetterEntry(ObjectId letterId)
	{
		BasicDBObject query =new BasicDBObject(Constant.ID,letterId);
		DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_LETTER_NAME, query, Constant.FIELDS);
		if(null!=dbo)
		{
			return new LetterEntry((BasicDBObject)dbo);
		}
		return null;
	}
	
	/**
	 * 得到信件数量
	 * @param senderId 发送者 ；不为null时生效
	 * @param type 信件种类;大于-1时生效
	 * @param state 信件状态;大于-1时生效
	 * @param receiverId 接受者ID；不为null时生效
	 * @param receiveLetterState 接受者信件状态;大于0时生效
	 */
	public  int countLetters(ObjectId senderId, int type, int state,ObjectId receiverId, int receiveLetterState)
	{
		BasicDBObject query = buildQuery(senderId, type, state, receiverId,
				receiveLetterState);
		return	 count(MongoFacroty.getAppDB(), Constant.COLLECTION_LETTER_NAME, query);
	}
	
	/**
	 * 
	 * @param senderId 发送者 ；不为null时生效
	 * @param type 信件种类;大于-1时生效
	 * @param state 信件状态;大于-1时生效
	 * @param receiverId 接受者ID；不为null时生效
	 * @param receiveLetterState 接受者信件状态;大于-1时生效
	 * @param skip 用于分页; 值大于0时生效
	 * @param limit 用于分页; 值大于0时生效
	 * @param fields 
	 * @return
	 * @throws IllegalParamException
	 */
	public List<LetterEntry> getLetterEntryList(ObjectId senderId, int type, int state,ObjectId receiverId, int receiveLetterState,int skip,int limit,DBObject fields) throws ResultTooManyException
	{
		BasicDBObject query = buildQuery(senderId, type, state, receiverId,
				receiveLetterState);
		if(query.isEmpty())
			throw new ResultTooManyException();
		List<LetterEntry> retList =new ArrayList<LetterEntry>();
		List<DBObject> list=new ArrayList<DBObject>();
		if(skip>Constant.NEGATIVE_ONE && limit>Constant.NEGATIVE_ONE)
		{
			 list =find(MongoFacroty.getAppDB(), Constant.COLLECTION_LETTER_NAME, query, fields, Constant.MONGO_SORTBY_DESC, skip, limit);
		}
		else
		{
			 list =find(MongoFacroty.getAppDB(), Constant.COLLECTION_LETTER_NAME, query, fields);
		}
		for(DBObject dbo:list)
		{
			retList.add(new LetterEntry((BasicDBObject)dbo));
		}
		return retList;
	}
	

	/**
	 *  根据ID集合查询，返回map key为ID
	 * @param ids
	 * @param fields
	 * @return
	 * @throws ResultTooManyException
	 */
	public Map<ObjectId, LetterEntry> getLetterEntryMap(Collection<ObjectId> ids,DBObject fields) 
	{
		Map<ObjectId, LetterEntry> retMap =new HashMap<ObjectId, LetterEntry>();
		BasicDBObject query = new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_IN,ids));
		List<DBObject>  list =find(MongoFacroty.getAppDB(), Constant.COLLECTION_LETTER_NAME, query, fields); 
		LetterEntry e;
		for(DBObject dbo:list)
		{
			e=new LetterEntry((BasicDBObject)dbo);
			retMap.put(e.getID(), e);
		}
		return retMap;
	}
	


    /** 得到与某位联系人得所有信件
     * @param userId
     * @param peerUserId
     * @param skip
     * @param limit
     * @return
     */
    public List<LetterEntry> getLetterEntryListByPeerUserId(ObjectId userId,ObjectId peerUserId,int skip,int limit){
        BasicDBObject query1 = new BasicDBObject();
        query1.append("si",userId);
        query1.append("st",new BasicDBObject("$ne",LetterState.LETTER_DELETED.getState()));
        query1.append("res.ri",peerUserId);

        BasicDBObject query2 = new BasicDBObject();
        query2.append("si",peerUserId);
        query2.append("res.ri",userId);
        query2.append("res.st", new BasicDBObject("$ne",LetterState.LETTER_DELETED.getState()));


        BasicDBList orlist = new BasicDBList();
        orlist.add( query1);
        orlist.add( query2);


        DBObject matchQuery = new BasicDBObject("$or",orlist);
        List<DBObject> list =find(MongoFacroty.getAppDB(), Constant.COLLECTION_LETTER_NAME, matchQuery, Constant.FIELDS, Constant.MONGO_SORTBY_DESC, skip, limit);

        List<LetterEntry> retList =new ArrayList<LetterEntry>();

        for(DBObject dbo:list)
        {
            retList.add(new LetterEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    /** 得到与某位联系人得所有信件
     * @param userId
     * @param peerUserId
     * @return
     */
    public int countLetterEntryListByPeerUserId(ObjectId userId,ObjectId peerUserId){
        BasicDBObject query1 = new BasicDBObject();
        query1.append("si",userId);
        query1.append("st",new BasicDBObject("$ne",LetterState.LETTER_DELETED.getState()));
        query1.append("res.ri",peerUserId);

        BasicDBObject query2 = new BasicDBObject();
        query2.append("si",peerUserId);
        query2.append("res.ri",userId);
        query2.append("res.st", new BasicDBObject("$ne",LetterState.LETTER_DELETED.getState()));


        BasicDBList orlist = new BasicDBList();
        orlist.add( query1);
        orlist.add( query2);


        DBObject matchQuery = new BasicDBObject("$or",orlist);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_LETTER_NAME, matchQuery);

    }
	
	
    /**
     * 得到用户有多少未读信件
     * @param userId
     * @return
     */
    public int  getUnReadLetterCount (ObjectId userId)
    {
    	ReceiveInfo info =new ReceiveInfo(userId, LetterState.LETTER_SEDND_SUCCESS.getState(), null);
    	BasicDBObject query = new BasicDBObject("res",info.getBaseEntry());
    	return count(MongoFacroty.getAppDB(), Constant.COLLECTION_LETTER_NAME, query);
    }
    
   
    
    
    

	
	
	
	private BasicDBObject buildQuery(ObjectId senderId, int type, int state,
			ObjectId receiverId, int receiveLetterState) {
		BasicDBObject query =new BasicDBObject();
		if(null!=senderId)
		{
			query.append("si", senderId);
		}
		if(type>Constant.NEGATIVE_ONE)
		{
			query.append("ty", type);
		}
		if(state>Constant.NEGATIVE_ONE)
		{
			query.append("st", state);
		}
		if(null!=receiverId)
		{
			query.append("res.ri", receiverId);
		}
		if(receiveLetterState>Constant.NEGATIVE_ONE)
		{
			query.append("res.st", receiveLetterState);
		}
		return query;
	}
	
	
	/**
	 * 此方法只用作统计，可以删除
	 * 按照收件人查询
	 * @param receives
	 * @param field
	 * @return
	 */
	@Deprecated
	public List<LetterEntry> getLetters(Collection<ObjectId> receives,BasicDBObject field)
	{
		BasicDBObject query =new BasicDBObject("res.ri",new BasicDBObject(Constant.MONGO_IN,receives));
		
		List<DBObject> list =find(MongoFacroty.getAppDB(), Constant.COLLECTION_LETTER_NAME, query, field);

	    List<LetterEntry> retList =new ArrayList<LetterEntry>();

	    for(DBObject dbo:list)
	    {
	            retList.add(new LetterEntry((BasicDBObject)dbo));
	    }
	        return retList;
		
	}


	
}
