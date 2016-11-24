package com.db.notice;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdValuePair;
import com.pojo.calendar.Event;
import com.pojo.notice.NoticeEntry;
import com.pojo.notice.NoticeReadsEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.exceptions.ResultTooManyException;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通知操作
 * @author fourer
 */
public class NoticeDao extends BaseDao {
	

	
	public final static DBObject SORTBY =new BasicDBObject("ist",-1).append(Constant.ID, -1);
	/**
	 * 添加
	 * @param e
	 * @return
	 */
	public ObjectId addNoticeEntry(NoticeEntry e)
	{
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_NOTICE_NAME, e.getBaseEntry());
		ObjectId nid= e.getID();
		NoticeReadsEntry nre =new NoticeReadsEntry(nid, new ArrayList<ObjectId>());
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_NOTICE_READ_NAME, nre.getBaseEntry());
		return nid;
	}
	
	
	
	
	
	
	/**
	 * 详情
	 * @param id
	 * @return
	 */
	public NoticeEntry getNoticeEntry(ObjectId id)
	{
		NoticeEntry e=null;
		BasicDBObject query =new BasicDBObject(Constant.ID,id);
		DBObject dbo =	findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NOTICE_NAME, query, Constant.FIELDS);
		if(null!=dbo)
		{
			e= new NoticeEntry((BasicDBObject)dbo);
		}
		
		if(null!=e)
		{
		  NoticeReadsEntry nre=this.getNoticeReadsEntry(e.getID());
		  
		  List<IdValuePair> users =new ArrayList<IdValuePair>();
		  
		  for(ObjectId uid:nre.getReadUsers())
		  {
			  users.add(new IdValuePair(uid, System.currentTimeMillis()));
		  }
		  e.setReadUsers(users);
		  e.setTotalReadUser(nre.getTotalReadUser());
		}
		
		return e;
	}
	/**
	 * 添加一个文档
	 * @param id
	 * @param p
	 */
	public void addDocFile(ObjectId id ,IdValuePair p)
	{
		BasicDBObject query =new BasicDBObject(Constant.ID,id);
		BasicDBObject updateValue =new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("df",p.getBaseEntry()));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_NOTICE_NAME, query, updateValue);
	}
	
	/**
	 * 添加一个声音
	 * @param id
	 * @param p
	 */
	public void addVoiceFile(ObjectId id ,IdValuePair p)
	{
		BasicDBObject query =new BasicDBObject(Constant.ID,id);
		BasicDBObject updateValue =new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("vf",p.getBaseEntry()));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_NOTICE_NAME, query, updateValue);
	}
	/**
	 * 用户读消息
	 * @param userId
	 */
	public void userReadNotice(ObjectId id,ObjectId userId)
	{
		BasicDBObject query =new BasicDBObject("ni",id);
		BasicDBObject updateValue =new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("rus",userId))
		.append(Constant.MONGO_INC, new BasicDBObject("trus",Constant.ONE));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_NOTICE_READ_NAME, query, updateValue);
	}
	
	/**
	 * 删除
	 * @param id
	 */
	public void deleteNotice(ObjectId id)
	{
		BasicDBObject query =new BasicDBObject(Constant.ID,id);
		remove(MongoFacroty.getAppDB(),Constant.COLLECTION_NOTICE_NAME, query);
	}
	
	/**
	 * 更新时间
	 * @param id
	 */
	public void updateIsTop(ObjectId id,int top)
	{
		BasicDBObject query =new BasicDBObject(Constant.ID,id);
		BasicDBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("ist",top));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_NOTICE_NAME, query, updateValue);
	}
	
	/**
	 * 得到总数，用于分页
	 * @param ur
	 * @param userId
	 * @param schoolId
	 * @return
	 */
	public int count(int ur,ObjectId userId,ObjectId schoolId,ObjectId childId, int type)
	{
		BasicDBObject query = buildQuery(ur, userId, schoolId,childId);
        query.append("ty",type);
		return count(MongoFacroty.getAppDB(), Constant.COLLECTION_NOTICE_NAME, query);
	}
	
	/**
	 * 
	 * @param ur 用户角色，只分老师和学生
	 * @param userId 
	 * @param schoolId
	 * @param skip
	 * @param limit
	 * @return
	 * @throws ResultTooManyException
	 */
	public List<NoticeEntry> getNoticeEntrys(int ur,ObjectId userId,ObjectId schoolId,ObjectId childId, int type, int skip,int limit) throws ResultTooManyException
	{
		BasicDBObject query = buildQuery(ur, userId, schoolId,childId);
        query.append("ty",type);
		return queryRes(skip, limit, query);
	}
	
	
	
	
	
	
	
	/**
	 * 供日历部分调用接口
	 * @param ur
	 * @param userId
	 * @param schoolId
	 * @param beginTime
	 * @param endTime
	 * @param skip
	 * @param limit
	 * @return
	 */
	public List<Event> getNoticeEvent(int ur,ObjectId userId,ObjectId schoolId,ObjectId childId,long beginTime, long endTime,int skip,int limit )
	{
		List<Event> retList =new ArrayList<Event>();
		BasicDBObject query = buildQuery(ur, userId, schoolId,childId);
		query.append("isSyn", 1);
		query.append("ty", Constant.ZERO);
		
		List<DBObject> dboList = find(MongoFacroty.getAppDB(),Constant.COLLECTION_NOTICE_NAME, query,Constant.FIELDS,SORTBY,skip,limit);
		NoticeEntry ne=null;
		if(null!=dboList && !dboList.isEmpty())
		{
			for (DBObject dbobject : dboList) 
			{
				ne = new NoticeEntry((BasicDBObject) dbobject);
				if(isRequire(ne, beginTime, endTime))
				{
					Event e=new Event(userId, Constant.THREE, ne.getName(), ne.getContent(), ne.getBeginTime(), ne.getEndTime());
					e.setID(ne.getID());
					retList.add(e);
				}
			}
		}
		return retList;
	}
	
	
	/**
	 * 得到通知的阅读情况
	 * @param ids
	 * @return
	 */
	public Map<ObjectId, NoticeReadsEntry> getNoticeReadsEntryMap(Collection<ObjectId> ids)
	{
		Map<ObjectId, NoticeReadsEntry> retMap =new HashMap<ObjectId, NoticeReadsEntry>();
		BasicDBObject query = new BasicDBObject("ni",new BasicDBObject(Constant.MONGO_IN,ids));
		
		List<DBObject> dboList = find(MongoFacroty.getAppDB(),Constant.COLLECTION_NOTICE_READ_NAME, query,new BasicDBObject("ni",1).append("trus", 1).append("rus", 1));
		
		NoticeReadsEntry ne=null;
		if(null!=dboList && !dboList.isEmpty())
		{
			for (DBObject dbobject : dboList) 
			{
				ne = new NoticeReadsEntry((BasicDBObject) dbobject);
				retMap.put(ne.getNoticeId(), ne);
			}
		}
		
		return retMap;
	}


	/**
	 * 得到通知的阅读情况
	 * @param ids
	 * @return
	 */
	public List< NoticeReadsEntry> getNoticeReadsEntryListByParam(Collection<ObjectId> ids, ObjectId dslId, ObjectId delId, BasicDBObject fields)
	{
		List< NoticeReadsEntry> list =new ArrayList<NoticeReadsEntry>();
		BasicDBObject query = new BasicDBObject("ni",new BasicDBObject(Constant.MONGO_IN, ids));

		BasicDBList dblist =new BasicDBList();
		if(dslId!=null){
			dblist.add(new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_GTE, dslId)));
		}
		if(delId!=null){
			dblist.add(new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_LTE, delId)));
		}
		if(dblist.size()>0){
			query.append(Constant.MONGO_AND,dblist);
		}

		List<DBObject> dboList = find(MongoFacroty.getAppDB(),Constant.COLLECTION_NOTICE_READ_NAME, query,fields);

		NoticeReadsEntry ne=null;
		if(null!=dboList && !dboList.isEmpty())
		{
			for (DBObject dbobject : dboList)
			{
				ne = new NoticeReadsEntry((BasicDBObject) dbobject);
				list.add(ne);
			}
		}

		return list;
	}
	
	
	
	/**
	 * 得到通知的阅读情况
	 * @param ids
	 * @return
	 */
	public NoticeReadsEntry getNoticeReadsEntry(ObjectId id)
	{
		BasicDBObject query = new BasicDBObject("ni",id);
		
		DBObject dbo = findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_NOTICE_READ_NAME, query,Constant.FIELDS);
		
		NoticeReadsEntry ne=null;
		if(null!=dbo )
		{
			ne = new NoticeReadsEntry((BasicDBObject) dbo);
			return ne;
		}
		return null;
	}
	
	
	
	
	/**
	 * 未读通知数量
	 * @param ur
	 * @param userId
	 * @param schoolId
	 * @param childId
	 * @return
	 */
	public Integer getNoReadCount(int ur,ObjectId userId,ObjectId schoolId,ObjectId childId )
	{
		BasicDBObject query = buildQuery(ur, userId, schoolId,childId);
		query.append("ty", Constant.ZERO);
		
		List<ObjectId> ids =new ArrayList<ObjectId>();
		List<DBObject> dboList = find(MongoFacroty.getAppDB(),Constant.COLLECTION_NOTICE_NAME, query,new BasicDBObject("_id",1));
		NoticeEntry ne=null;
		if(null!=dboList && !dboList.isEmpty())
		{
			for (DBObject dbobject : dboList) 
			{
				ne = new NoticeEntry((BasicDBObject) dbobject);
				ids.add(ne.getID());
			}
		}
		BasicDBObject newQuery=new BasicDBObject("ni",new BasicDBObject(Constant.MONGO_IN,ids));
		newQuery.append("rus", new BasicDBObject(Constant.MONGO_NOTIN,Arrays.asList(userId)));
		return count(MongoFacroty.getAppDB(),Constant.COLLECTION_NOTICE_READ_NAME, newQuery);
	}
	
	
	/**
	 * 得到一个事件 
	 * @param id
	 * @return
	 */
	public Event getNoticeEvent(ObjectId id)
	{
	   NoticeEntry ne =getNoticeEntry( id);
	   if(null!=ne)
	   {
		   Event e=new Event(new ObjectId(), Constant.THREE, ne.getName(), ne.getContent(), ne.getBeginTime(), ne.getEndTime());
		   e.setID(ne.getID());
		   return e;
	   }
	  return null;
	}

	
	
    public List<NoticeEntry> getNoticePublishByParamList(List<ObjectId> usIds, ObjectId dslId, ObjectId delId, int skip, int limit, BasicDBObject fields, String orderBy) {
        List<NoticeEntry> retList =new ArrayList<NoticeEntry>();
        BasicDBObject query =new BasicDBObject("ti",new BasicDBObject(Constant.MONGO_IN,usIds));

        BasicDBList dblist =new BasicDBList();
        if(dslId!=null){
            dblist.add(new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_GTE,dslId)));
        }
        if(delId!=null){
            dblist.add(new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_LTE, delId)));
        }
        if(dblist.size()>0){
            query.append(Constant.MONGO_AND,dblist);
        }

        BasicDBObject sort =null;
        if (!"".equals(orderBy)){
            sort =new BasicDBObject(orderBy,Constant.DESC);
        }else{
            sort =new BasicDBObject(Constant.ID,Constant.DESC);
        }
        List<DBObject> list =new ArrayList<DBObject>();
        if(skip>=0 && limit>0)
        {
            list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NOTICE_NAME, query, fields, sort, skip, limit);
        }
        else
        {
            list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NOTICE_NAME, query, fields, sort);
        }

        for(DBObject dbo:list)
        {
            retList.add(new NoticeEntry((BasicDBObject)dbo));
        }
        return retList;
    }

    public int selNoticePublishCount(List<ObjectId> usIds, ObjectId dslId, ObjectId delId) {
        BasicDBObject query =new BasicDBObject("ti",new BasicDBObject(Constant.MONGO_IN,usIds));
        BasicDBList dblist =new BasicDBList();
        if(dslId!=null){
            dblist.add(new BasicDBObject(Constant.ID,new BasicDBObject(Constant.MONGO_GTE,dslId)));
        }
        if(delId!=null){
            dblist.add(new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_LTE, delId)));
        }
        if(dblist.size()>0){
            query.append(Constant.MONGO_AND,dblist);
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_NOTICE_NAME, query);
    }

    /**
     * 得到全部通知
     *
     * @return
     */
    @Deprecated
    public List<NoticeEntry> getAllNoticeEntry()
    {

        List<DBObject> dboList = find(MongoFacroty.getAppDB(),Constant.COLLECTION_NOTICE_NAME,new BasicDBObject(), Constant.FIELDS);

        List<NoticeEntry> retList =new ArrayList<NoticeEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new NoticeEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }
    
    
    /**
	 * p判断时间是否和要求
	 * @param e
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private boolean isRequire(NoticeEntry e,long beginTime, long endTime)
	{
		long bt=e.getBeginTime();
		long et=e.getEndTime();
		
		if(bt<=endTime && et>=endTime)
		{
			return true;
		}
		
		if(bt<=beginTime && et>=beginTime)
		{
			return true;
		}
		

		if(bt>=beginTime && et<=endTime)
		{
			return true;
		}
		
		
		if(bt>=beginTime && bt<=endTime)
		{
			return true;
		}
		
		return false;
	}
	
	

	private List<NoticeEntry> queryRes(int skip, int limit, BasicDBObject query) {
		List<NoticeEntry> retList = new ArrayList<NoticeEntry>();
		List<DBObject> dboList = find(MongoFacroty.getAppDB(),Constant.COLLECTION_NOTICE_NAME, query,new BasicDBObject("us",0),SORTBY,skip,limit);
		if (null != dboList && !dboList.isEmpty()) {
			NoticeEntry e;
			for (DBObject dbobject : dboList) 
			{
				e = new NoticeEntry((BasicDBObject) dbobject);
				retList.add(e);
			}
		}
		return retList;
	}

	
	private BasicDBObject buildQuery(int userRole, ObjectId userId,
			ObjectId schoolId,ObjectId childId) {
		BasicDBList list =new BasicDBList();
		BasicDBObject dbo =null;
		BasicDBObject dbo1=null;
		if(UserRole.isTeacher(userRole)) //老师
		{
			dbo =new BasicDBObject("all.id",schoolId).append("all.ists", Constant.ONE);
			dbo1 =new BasicDBObject("all.id",schoolId).append("all.ist", Constant.ONE);
		}
		if(UserRole.isStudentOrParent(userRole)) //家长或学生
		{
			dbo =new BasicDBObject("all.id",schoolId).append("all.ists", Constant.ONE);
			dbo1 =new BasicDBObject("all.id",schoolId).append("all.iss", Constant.ONE);
		}
		BasicDBObject dbo2 =new BasicDBObject("us.id",userId);
		BasicDBObject dbo3 =new BasicDBObject("ti",userId);
		
		BasicDBObject dbo4=null;
		if(UserRole.isParent(userRole))
		{
			dbo4=new BasicDBObject("us.id",childId);
		}
		
		if(null!=dbo)
		{
		  list.add(dbo);
		}
		if(null!=dbo1)
		{
		 list.add(dbo1);
		}
		list.add(dbo2);
		list.add(dbo3);
		if(null!=dbo4)
		{
			list.add(dbo4);
		}
		BasicDBObject query =new BasicDBObject(Constant.MONGO_OR,list).append("ty", Constant.ZERO);
		return query;
	}
	
	
	@Deprecated
	 public List<NoticeEntry> getNoticeEntry(int skip,int limit ){
	        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NOTICE_NAME,new BasicDBObject(), Constant.FIELDS,Constant.MONGO_SORTBY_ASC,skip,limit);
	        List<NoticeEntry> entryList=new ArrayList<NoticeEntry>();
	        for(DBObject dbObject:list){
	        	NoticeEntry entry=new NoticeEntry((BasicDBObject)dbObject);
	        	entryList.add(entry);
	        }
	        return entryList;
	    }
	
	/**
	 * 更新时间
	 * @param id
	 */
	public void updateTotalUserCount(ObjectId id,String fields,int count)
	{
		BasicDBObject query =new BasicDBObject(Constant.ID,id);
		BasicDBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject(fields,count));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_NOTICE_NAME, query, updateValue);
	}
	
}
