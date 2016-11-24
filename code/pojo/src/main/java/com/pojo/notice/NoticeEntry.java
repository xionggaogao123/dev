package com.pojo.notice;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdNameValuePair;
import com.pojo.app.IdValuePair;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 通知
 * <pre>
 * collectionName:notices
 * </pre>
 * <pre>
 * {
 *  ti:老师ID
 *  all:是否全部;
 *  {
 *    id:学校ID
 *    ist:是不是全体老师 0 不是，1是
 *    iss:是不是全体学生 0 不是，1是
 *    ists:是不是全体师生 0 不是，1是
 *  }
 *  bt:开始时间
 *  et:结束时间
 *  ty:0 是通知，1是公文流转
 *  ist:是不是至顶通知 1是， 0不是
 *  isSyn:是否需要同步到日历 0不需要 1需要
 *  nm:名字
 *  con:内容
 *  tus:发给用户的总数
 *  us: 发给用户
 *  [
 *   {
 *    id:id
 *     v:相应名称
 *   }
 *  ]
 *  trus:已读数量
 *  rus: 已读用户 ;已经移至noticesreads中存储
 *  [
 *   {
 *    id:id
 *     v:时间
 *   }
 *  ]
 *  vf:语音地址
 *  [
 *   {
 *    id:
 *    nm:
 *    v:
 *   }
 *  ]
 *  df:文档地址
 *  [
 *   {
 *    id:
 *    nm:
 *    v:
 *   }
 *  ]
 * }
 * </pre>
 * @author fourer
 */
public class NoticeEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8455803226184995858L;
	
	public NoticeEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	
	
	public NoticeEntry(ObjectId teacherId,NoticeAllInfo all, List<IdValuePair> users,
			String name, String content, List<IdNameValuePair> voiceFile, List<IdNameValuePair> docFile,Long beginTime,Long endTime,int type,int isSyn,int xuexiaolead
			) {
		super();
		init(teacherId, all, users, name, content, voiceFile, docFile, beginTime, endTime, type, isSyn);
		//setTotalUser(users.size()-xuexiaolead>0?users.size()-xuexiaolead:0);
	}
	
	public NoticeEntry(ObjectId teacherId,NoticeAllInfo all, List<IdValuePair> users,
			String name, String content, List<IdNameValuePair> voiceFile, List<IdNameValuePair> docFile,Long beginTime,Long endTime,int type,int isSyn
			) {
		super();
		
		init(teacherId, all, users, name, content, voiceFile, docFile, beginTime, endTime, type, isSyn);
	}


	private void init(ObjectId teacherId, NoticeAllInfo all,
			List<IdValuePair> users, String name, String content,
			List<IdNameValuePair> voiceFile, List<IdNameValuePair> docFile,Long beginTime,Long endTime,int type,int isSyn) {
		BasicDBObject baseEntry =new BasicDBObject()
		.append("ti", teacherId)
		.append("us", MongoUtils.convert(MongoUtils.fetchDBObjectList(users)))
		.append("rus", new BasicDBList())
		.append("nm", name)
		.append("con", content)
		.append("vf", MongoUtils.convert(MongoUtils.fetchDBObjectList(voiceFile)))
		.append("df", MongoUtils.convert(MongoUtils.fetchDBObjectList(docFile)))
		.append("bt", beginTime)
		.append("et", endTime)
        .append("ty", type)
        .append("ist", Constant.ZERO)
		.append("isSyn", isSyn)
		.append("tus", users.size())
		//.append("trus", Constant.ZERO);
		;
		if(null!=all)
		{
			baseEntry.append("all", all.getBaseEntry());
		}
		else
		{
			baseEntry.append("all", null);
		}
		
		setBaseEntry(baseEntry);
	}
	
	

	public int getTotalUser() {
		return getSimpleIntegerValueDef("tus", 0);
	}
	public void setTotalUser(int totalUser) {
		setSimpleValue("tus", totalUser);
	}

	public int getTotalReadUser() {
		return getSimpleIntegerValueDef("trus", 0);
	}

	public void setTotalReadUser(int totalReadUser) {
		setSimpleValue("trus", totalReadUser);
	}

	public int getIsSyn() {
		return getSimpleIntegerValue("isSyn");
	}

	public void setIsSyn(int isSyn) {
		setSimpleValue("isSyn", isSyn);
	}

	public int getIsTop() {
		return getSimpleIntegerValue("ist");
	}


	public void setIsTop(int isTop) {
		setSimpleValue("ist", isTop);
	}


	public Long getBeginTime() {
		return getSimpleLongValue("bt");
	}
	public void setBeginTime(Long beginTime) {
		setSimpleValue("bt", beginTime);
	}
	public Long getEndTime() {
		return getSimpleLongValue("et");
	}
	public void setEndTime(Long endTime) {
		setSimpleValue("et", endTime);
	}

    public int getType() {
        return getSimpleIntegerValue("ty");
    }

    public void setType(int type) {
        setSimpleValue("ty", type);
    }

	public NoticeAllInfo getAllSchool() {
		BasicDBObject dbo =(BasicDBObject)getSimpleObjectValue("all");
		if(null!=dbo)
		{
			return new NoticeAllInfo(dbo);
		}
		return null;
	}


	public void setAllSchool(NoticeAllInfo allSchool) {
		if(null==allSchool)
		{
			setSimpleValue("all", new BasicDBObject());
		}
		else
		{
			setSimpleValue("all", allSchool.getBaseEntry());
		}
		
	}


	public ObjectId getTeacherId() {
		return getSimpleObjecIDValue("ti");
	}
	public void setTeacherId(ObjectId teacherId) {
		setSimpleValue("ti", teacherId);
	}


	public List<IdValuePair> getUsers() {
		List<IdValuePair> retList =new ArrayList<IdValuePair>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("us");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(new IdValuePair((BasicDBObject)o));
			}
		}
		return retList;
	}


	public void setUsers(List<IdValuePair> users) {
		List<DBObject> list=MongoUtils.fetchDBObjectList(users);
		setSimpleValue("us", MongoUtils.convert(list));
	}
	
	public List<IdValuePair> getReadUsers() {
		List<IdValuePair> retList =new ArrayList<IdValuePair>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("rus");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(new IdValuePair((BasicDBObject)o));
			}
		}
		return retList;
	}

	public void setReadUsers(List<IdValuePair> users) {
		List<DBObject> list=MongoUtils.fetchDBObjectList(users);
		setSimpleValue("rus", MongoUtils.convert(list));
	}

	public String getName() {
		return getSimpleStringValue("nm");
	}
	public void setName(String name) {
		setSimpleValue("nm", name);
	}
	public String getContent() {
		return getSimpleStringValue("con");
	}
	public void setContent(String content) {
		setSimpleValue("con", content);
	}
	
	
	
	
	public List<IdNameValuePair> getVoiceFile() {
		List<IdNameValuePair> retList =new ArrayList<IdNameValuePair>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("vf");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(new IdNameValuePair((BasicDBObject)o));
			}
		}
		return retList;
	}


	public void setVoiceFile(List<IdNameValuePair> vf) {
		List<DBObject> list=MongoUtils.fetchDBObjectList(vf);
		setSimpleValue("vf", MongoUtils.convert(list));
	}
	
	
	
	public List<IdNameValuePair> getDocFile() {
		List<IdNameValuePair> retList =new ArrayList<IdNameValuePair>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("df");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(new IdNameValuePair((BasicDBObject)o));
			}
		}
		return retList;
	}


	public void setDocFile(List<IdNameValuePair> df) {
		List<DBObject> list=MongoUtils.fetchDBObjectList(df);
		setSimpleValue("df", MongoUtils.convert(list));
	}
	
	
	/**
	 *  {
	 *    id:学校ID
	 *    ist:是不是全体老师 0 不是，1是
	 *    iss:是不是全体学生 0 不是，1是
	 *    ists:是不是全体师生 0 不是，1是
	 *  }
	 * @author fourer
	 *
	 */
	public static class NoticeAllInfo extends BaseDBObject
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = -1964926859866773281L;
		
		
		public NoticeAllInfo(BasicDBObject baseEntry) {
			super(baseEntry);
		}
		public NoticeAllInfo(ObjectId schoolId, int isAllTeacher,
				int isAllStudent, int isAllTeacherAndStudent) {
			super();
			BasicDBObject dbo =new BasicDBObject()
			.append("id", schoolId)
			.append("ist", isAllTeacher)
			.append("iss", isAllStudent)
			.append("ists", isAllTeacherAndStudent);
			setBaseEntry(dbo);
		}
		public ObjectId getSchoolId() {
			return getSimpleObjecIDValue("id");
		}
		public void setSchoolId(ObjectId schoolId) {
			setSimpleValue("id", schoolId);
		}
		public int getIsAllTeacher() {
			return getSimpleIntegerValue("ist");
		}
		public void setIsAllTeacher(int isAllTeacher) {
			setSimpleValue("ist", isAllTeacher);
		}
		public int getIsAllStudent() {
			return getSimpleIntegerValue("iss");
		}
		public void setIsAllStudent(int isAllStudent) {
			setSimpleValue("iss", isAllStudent);
		}
		public int getIsAllTeacherAndStudent() {
			return getSimpleIntegerValue("ists");
		}
		public void setIsAllTeacherAndStudent(int isAllTeacherAndStudent) {
			setSimpleValue("ists", isAllTeacherAndStudent);
		}
		
		
		
	}
	
}
