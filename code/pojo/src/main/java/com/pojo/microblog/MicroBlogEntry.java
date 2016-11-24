package com.pojo.microblog;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdNameValuePair;
import com.pojo.app.IdValuePair;
import com.pojo.app.Platform;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.DeleteState;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 微博
 * <pre>
 * collectionName:bolgs
 * </pre>
 * <pre>
 * {
     ui:用户ID
     ty:类型 1是顶层微博
     st:状态；详见MicroBlogState
     con:内容
     pf：平台;参见Platform
 	 pbt:发布时间,long
 	 ut:更新时间，long
 	 it:是否是主题帖0，1
	 bt:学生 2  老师 1
     rc:回复数量
     zc：赞的数量
     maid:主微博id
 	 ir:是否看过
     si:学校ID
     stp:查看类型
     at{
         id:objectID
         v:用户名
       }
     cli：回复的博客ID
     [
       
     ]
     ili:图片；参见MicroBlogImage
     [
       
     ]
     zci:赞的ID
     [
      
     ]
 	cls:班级
 	[
    ]
 	vids:视频id
 	[
 	]
 * }
 * </pre>
 * @author fourer
 */
public class MicroBlogEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4526663136529725767L;
	
	public MicroBlogEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}

	public MicroBlogEntry(ObjectId userId, int type,  String content,Platform pl,int top,int blogtype,ObjectId mainid,int isread,ObjectId schoolID,
			 List<MicroBlogImage> imageList,IdValuePair atInfo,List<ObjectId> classAry,List<IdNameValuePair> videoIds,int seachType,int manageType) {
		this(
				userId,
				type,
				DeleteState.NORMAL.getState(),
				content,
				pl,
				System.currentTimeMillis(),
				System.currentTimeMillis(),
				top,
				blogtype,
				Constant.ZERO,
				Constant.ZERO,
				mainid,
				isread,
				schoolID,
				new ArrayList<ObjectId>(),
				imageList,
				new ArrayList<ObjectId>(),
				atInfo,
				classAry,
				videoIds,
				seachType,
				manageType
			);
	}
	
	public MicroBlogEntry(ObjectId userId, int type, int state, String content,
			Platform pl,long publishTime,long updateTime,int top,int blogtype, int replyCount, int zanCount,ObjectId mainid,int isread,
            ObjectId schoolID,
			List<ObjectId> commentList, List<MicroBlogImage> imageList,List<ObjectId> zanList,IdValuePair atInfo,List<ObjectId> classAry,List<IdNameValuePair> videoIds,int seachType,int manageType) {
		super();
		
		BasicDBObject baseEntry =new BasicDBObject()
		.append("ui", userId)
		.append("ty", type)
		.append("st", state)
		.append("con", content)
		.append("pf", pl.getType())
		.append("pbt", publishTime)
		.append("upt", updateTime)
		.append("it", top)
		.append("bt", blogtype)
		.append("rc", replyCount)
		.append("zc", zanCount)
		.append("maid", mainid)
		.append("ir",isread)
        .append("si", schoolID)
        .append("cli", MongoUtils.convert(commentList))
        .append("ili", MongoUtils.convert(MongoUtils.fetchDBObjectList(imageList)))
		.append("zci", MongoUtils.convert(zanList))
		.append("cls",MongoUtils.convert(classAry))
		.append("vids", MongoUtils.convert(MongoUtils.fetchDBObjectList(videoIds)))
		.append("stp",seachType)
		.append("sc", 0)
		.append("mtp",manageType);
		if(null!=atInfo)
		{
			baseEntry.append("at", atInfo.getBaseEntry());
		}
		else
		{
			baseEntry.append("at", new BasicDBObject());
		}
		
		;
		setBaseEntry(baseEntry);
	}
	

	public IdValuePair getAtInfo() {
		BasicDBObject dbo =(BasicDBObject)getSimpleObjectValue("at");
		return new IdValuePair(dbo);
	}

	public void setAtInfo(IdValuePair atInfo) {
		setSimpleValue("at", atInfo.getBaseEntry());
	}

	
	public ObjectId getUserId() {
		return getSimpleObjecIDValue("ui");
	}
	public void setUserId(ObjectId userId) {
		setSimpleValue("ui", userId);
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
	public int getPlatformType() {
		return getSimpleIntegerValue("pf");
	}
	public void setPlatformType(int platformType) {
		setSimpleValue("pf", platformType);
	}
	public int getReplyCount() {
		return getSimpleIntegerValue("rc");
	}
	public void setReplyCount(int replyCount) {
		setSimpleValue("rc", replyCount);
	}
	public int getZanCount() {
		return getSimpleIntegerValue("zc");
	}
	public void setZanCount(int zanCount) {
		setSimpleValue("zc", zanCount);
	}
	public long getPublishTime() {return getSimpleLongValue("pbt");}
	public void setPublishTime(long publishTime) {setSimpleValue("pbt",publishTime);}
	public int getBlogtype() {return getSimpleIntegerValue("bt");}
	public void setBlogtype(int blogtype) { setSimpleValue("bt",blogtype);}
	public int getTop() {return getSimpleIntegerValue("it");}
	public void setTop(int top) {setSimpleValue("it", top);}
	public ObjectId getMainid() {
		return getSimpleObjecIDValue("maid");
	}
	public void setMainid(ObjectId mainid) {
		setSimpleValue("maid",mainid);
	}
	public ObjectId getSchoolID() {
		return getSimpleObjecIDValue("si");
	}
	public void setSchoolID(ObjectId schoolID) {
		setSimpleValue("si", schoolID);
	}
	public List<ObjectId> getCommentList() {
		List<ObjectId> retList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("cli");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add((ObjectId)o);
			}
		}
		return retList;
	}
	public void setCommentList(List<ObjectId> commentList) {
		setSimpleValue("cli", MongoUtils.convert(commentList));
	}
	public List<MicroBlogImage> getImageList() {
		List<MicroBlogImage> retList =new ArrayList<MicroBlogImage>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("ili");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(new MicroBlogImage((BasicDBObject)o));
			}
		}
		return retList;
	}
	public void setImageList(List<MicroBlogImage> imageList) {
		List<DBObject> list =MongoUtils.fetchDBObjectList(imageList);
		setSimpleValue("ili", MongoUtils.convert(list));
	}

	public List<ObjectId> getZanList() {
		List<ObjectId> retList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("zci");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add((ObjectId)o);
			}
		}
		return retList;
	}
	public void setZanList(List<ObjectId> zanList) {
		setSimpleValue("zci", MongoUtils.convert(zanList));
	}

	public List<IdNameValuePair> getVideoIds() {
		List<IdNameValuePair> retList =new ArrayList<IdNameValuePair>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("vids");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(new IdNameValuePair((BasicDBObject)o));
			}
		}
		return retList;
	}
	public void setVideoIds(List<IdNameValuePair> videoIds) {
		List<DBObject> list =MongoUtils.fetchDBObjectList(videoIds);
		setSimpleValue("vids", MongoUtils.convert(list));
	}
	public int getSeachType() {return getSimpleIntegerValue("stp");}
	public void setSeachType(int seachType) { setSimpleValue("stp",seachType);}
	public long getUpdateTime() {return getSimpleLongValue("upt");}
	public void setUpdateTime(long updateTime) {setSimpleValue("upt",updateTime);}
	public int getShouCang() {return getSimpleIntegerValue("sc");}
	public void setShouCang(int shouCang) { setSimpleValue("sc",shouCang);}
}
