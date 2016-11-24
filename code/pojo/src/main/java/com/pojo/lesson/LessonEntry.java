package com.pojo.lesson;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.emarket.Comment;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

/**
 * 课程
 * <pre>
 * collectionName:lessons
 * </pre>
 * <pre>
 * {
 *  nm:名字
 *  con:内容
 *  ty:课程类型;参见LessonType
 *  ui:用户
 *  di:目录ID
 *  im：图片地址
 *  lut:最后更新时间
 *  vc:视频个数
 *  dc:文档个数
 *  ec:练习个数
 *  pc:被推送的次数
 *  
 *  
 *  vis:[] 视频
 *  dcl:文档列表；LessonWare
 *  [
 *   {
	 *   id:此课件ID
	 *   fty:文件类型；FileType
	 *   nm:名字
	 *   pa:地址
 *   }
 *  ]
 *  exl:课后小练习；
 *  课后小练习由于实现逻辑和考试一样，所以关联到ExerciseEntry ty=2 表示是一个课后小练习
 *  
 *  si:原课程ID,可以是云课程id，也可以是课程id
 *  isc:是否来源于云课程；1是 0不是
 *  comc:评论个数
 *  coms: 参见Comment
    [
             * {
			 *  ui:用户ID
			 *  com:评论内容
			 *  t:时间
			 * }
     ]
     
     ir:是否删除 0没有删除 1已经删除
 *  
 * }
 * </pre>
 * @author fourer
 */
public class LessonEntry extends BaseDBObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4404702655525196406L;
	
	public LessonEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public LessonEntry(String name, String content, LessonType type, ObjectId ui,ObjectId dirId,ObjectId sourceId,int isFromCloud) {
		this(name,
				content,
				type,
				ui,
				dirId, //dirId
				Constant.EMPTY,//imgUrl
				new ArrayList<ObjectId>(),//videoIds
				System.currentTimeMillis(),//lastUpdateTime
				Constant.ZERO,
				Constant.ZERO,
				Constant.ZERO,
				new ArrayList<LessonWare>(),
				null,
				sourceId,
				isFromCloud
				);
	}
	
	public LessonEntry(String name, String content, LessonType type,ObjectId ui,
			ObjectId dirId, String imgUrl, List<ObjectId> videoIds,
			long lastUpdateTime,  int videoCount,
			int documentCount, int exerciseCount,List<LessonWare> lessonWareList,ObjectId classDoc
			,ObjectId sourceId,int isFromCloud
			) {
		super();
		List<DBObject> list =MongoUtils.fetchDBObjectList(lessonWareList);
		BasicDBObject baseEntry =new BasicDBObject()
		.append("nm", name)
		.append("con", content)
		.append("ty", type.getType())
		.append("ui", ui)
		.append("di", dirId)
		.append("im", imgUrl)
		.append("vis", MongoUtils.convert(videoIds))
		.append("lut", lastUpdateTime)
		.append("vc", videoCount)
		.append("dc", documentCount)
		.append("ec", exerciseCount)
		.append("dcl", MongoUtils.convert(list))
		.append("exl", classDoc)
		.append("si", sourceId)
		.append("isc", isFromCloud)
		.append("comc", Constant.ZERO)
		.append("coms", new BasicDBList())
		.append("pc", Constant.ZERO)
		.append("ir", Constant.ZERO);
		;
		setBaseEntry(baseEntry);
	}
	
	
	public int getIsRemove() {
		return getSimpleIntegerValue("ir");
	}
	public void setIsRemove(int isRemove) {
		setSimpleValue("ir", isRemove);
	}

	public int getDocumentCount() {
		return getSimpleIntegerValue("dc");
	}

	public void setDocumentCount(int documentCount) {
		setSimpleValue("dc", documentCount);
	}

	public int getPushCount() {
		return getSimpleIntegerValue("pc");
	}

	public void setPushCount(int pushCount) {
		setSimpleValue("pc", pushCount);
	}

	public int getIsFromCloud() {
		return getSimpleIntegerValue("isc");
	}

	public void setIsFromCloud(int isFromCloud) {
		setSimpleValue("isc", isFromCloud);
	}

	public ObjectId getSourceId() {
		return getSimpleObjecIDValue("si");
	}

	public void setSourceId(ObjectId sourceId) {
		setSimpleValue("si", sourceId);
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
	public int getType() {
		return getSimpleIntegerValue("ty");
	}
	public void setType(int type) {
		setSimpleValue("ty", type);
	}
	public ObjectId getUserId() {
		return getSimpleObjecIDValue("ui");
	}
	public void setUserId(ObjectId ui) {
		setSimpleValue("ui", ui);
	}
	public ObjectId getDirId() {
		return getSimpleObjecIDValue("di");
	}
	public void setDirId(ObjectId dirId) {
		setSimpleValue("di", dirId);
	}
	public String getImgUrl() {
		return getSimpleStringValue("im");
	}
	public void setImgUrl(String imgUrl) {
		setSimpleValue("im", imgUrl);
	}
	public List<ObjectId> getVideoIds() {
		return MongoUtils.getFieldObjectIDs(this, "vis");
	}
	public void setVideoIds(List<ObjectId> videoIds) {
		setSimpleValue("vis", MongoUtils.convert(videoIds));
	}
	public long getLastUpdateTime() {
		return getSimpleLongValue("lut");
	}
	public void setLastUpdateTime(long lastUpdateTime) {
		setSimpleValue("lut", lastUpdateTime);
	}
	public int getVideoCount() {
		return getSimpleIntegerValue("vc");
	}
	public void setVideoCount(int videoCount) {
		setSimpleValue("vc", videoCount);
	}
	public int getLessonWareCount() {
		return getSimpleIntegerValue("dc");
	}
	public void setLessonWareCount(int documentCount) {
		setSimpleValue("dc", documentCount);
	}
	public int getExerciseCount() {
		return getSimpleIntegerValue("ec");
	}
	public void setExerciseCount(int exerciseCount) {
		setSimpleValue("ec", exerciseCount);
	}

	public List<LessonWare> getLessonWareList() {
		List<LessonWare> lessonWareList =new ArrayList<LessonWare>();
		
		BasicDBList list =(BasicDBList)getSimpleObjectValue("dcl");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				lessonWareList.add(  new LessonWare((BasicDBObject)o));
			}
		}
		return lessonWareList;
	}

	public void setLessonWareList(List<LessonWare> lessonWareList) {
		List<DBObject> list =MongoUtils.fetchDBObjectList(lessonWareList);
		setSimpleValue("dcl",  MongoUtils.convert(list));
	}

	public ObjectId getExercise() {
        return getSimpleObjecIDValue("exl");
	}

	public void setExercise(ObjectId  exercise) {
		setSimpleValue("exl", exercise);
	}
	
	public List<Comment> getCommentList() {
		List<Comment> retList =new ArrayList<Comment>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("coms");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add(new Comment((BasicDBObject)o));
			}
		}
		return retList;
	}
	public void setCommentList(List<Comment> goodsCommentList) {
		
		List<DBObject> list =MongoUtils.fetchDBObjectList(goodsCommentList);
		setSimpleValue("coms", MongoUtils.convert(list));
	}
	
	public int getCommentCount() {
		return getSimpleIntegerValue("comc");
	}
	public void setCommentCount(int commentCount) {
		setSimpleValue("comc", commentCount);
	}
	
}
