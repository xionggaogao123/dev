package com.pojo.cloudlesson;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 云课程
 * <pre>
 * collectionName:cloudclasses
 * </pre>
 * <pre>
 * {
 *  nm:名字
 *  con:内容
 *  uid:上传用户id
 *  or:排序
 *  im:图片地址
 *  vis:视频ID
 *  [
 *
 *  ]
 *  sub:科目ID；对应SubjectType
 *  ccgts:云课程年级；对应GradeType
 *  [
 *
 *  ]
 *
 *  cctys:云课程类别；对应CloudLessonTypeEntry
 *  [
 *
 *  ]
 *  删除标志位  : ir (isRemoved) 0没有删除 1已经删除
 *  入库标志位  : is (isSaved)  0没有入库 1已经入库
 *  归属类型：ot(ownType) 0:大众所有，1：私人专属
 *  专属教育局ID： edid(educationBureauId)
 *  不可用教育局IDs： neids(noEducationBureauIds)
 *  资源来源 : rf (resourceFrom)
 * }
 * </pre>
 * @author fourer
 */
public class CloudLessonEntry extends BaseDBObject {

	/**
	 *
	 */
	private static final long serialVersionUID = 7105040754235610727L;

	public CloudLessonEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}





	public CloudLessonEntry(String name, ObjectId userId, String content, int order, String imageUrl, List<ObjectId> videoIds, int subject,
							List<Integer> cloudClassGradeTypes, List<ObjectId> cloudClassTypes, int isRemoved, int isSaved, int ownType, ObjectId eduId, List<ObjectId> noEdIds,String resourceFrom) {
		super();
		BasicDBObject baseEntry =new BasicDBObject()
				.append("nm", name)
				.append("uid", userId)
				.append("con", content)
				.append("or", order)
				.append("im", imageUrl)
				.append("vis", videoIds)
				.append("sub", subject)
				.append("ccgts", MongoUtils.convert(cloudClassGradeTypes))
				.append("cctys", MongoUtils.convert(cloudClassTypes))

				.append("ir", isRemoved)
				.append("is", isSaved)

				.append("ot", ownType)
				.append("edid", eduId)

				.append("neids", noEdIds)

				.append("rf", resourceFrom);
		setBaseEntry(baseEntry);

	}

	public String getName() {
		return getSimpleStringValue("nm");
	}
	public void setName(String name) {
		setSimpleValue("nm", name);
	}

	public ObjectId getUserId() {
		return getSimpleObjecIDValue("uid");
	}
	public void setUserId(ObjectId userId) {
		setSimpleValue("uid", userId);
	}

	public String getContent() {
		return getSimpleStringValue("con");
	}
	public void setContent(String content) {
		setSimpleValue("con", content);
	}
	public int getOrder() {
		return getSimpleIntegerValue("or");
	}
	public void setOrder(int order) {
		setSimpleValue("or", order);
	}
	public String getImageUrl() {
		return getSimpleStringValue("im");
	}
	public void setImageUrl(String imageUrl) {
		setSimpleValue("im", imageUrl);
	}


	public List<ObjectId> getVideoIds() {
		List<ObjectId> retList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("vis");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add((ObjectId)o);
			}
		}
		return retList;
	}
	public void setVideoIds(List<ObjectId> videoIds) {
		setSimpleValue("vis", MongoUtils.convert(videoIds));
	}


	public int getSubject() {
		return getSimpleIntegerValue("sub");
	}
	public void setSubject(int subject) {
		setSimpleValue("sub", subject);
	}

	public List<Integer> getCloudClassGradeTypes() {
		List<Integer> retList =new ArrayList<Integer>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("ccgts");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add((Integer)o);
			}
		}
		return retList;
	}
	public void setCloudClassGradeTypes(List<Integer> cloudClassGradeTypes) {
       setSimpleValue("ccgts", MongoUtils.convert(cloudClassGradeTypes));
	}

	public List<ObjectId> getCloudClassTypes() {
		List<ObjectId> retList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("cctys");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add((ObjectId)o);
			}
		}
		return retList;
	}
	public void setCloudClassTypes(List<ObjectId> cloudClassTypes) {
		setSimpleValue("cctys", MongoUtils.convert(cloudClassTypes));
	}

	public int getIsRemoved() {
		return getSimpleIntegerValue("ir");
	}
	public void setIsRemoved(int isRemoved) {
		setSimpleValue("ir", isRemoved);
	}

	public int getIsSaved() {
		return getSimpleIntegerValueDef("is",1);
	}
	public void setIsSaved(int isSaved) {
		setSimpleValue("is", isSaved);
	}

	public int getOwnType() {
		return getSimpleIntegerValue("ot");
	}
	public void setOwnType(int ownType) {
		setSimpleValue("ot", ownType);
	}

	public ObjectId getEduId() {
		return getSimpleObjecIDValue("edid");
	}
	public void setEduId(ObjectId eduId) {
		setSimpleValue("edid", eduId);
	}

	public List<ObjectId> getNoEdIds() {
		List<ObjectId> retList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("neids");
		if(null!=list && !list.isEmpty())
		{
			for(Object o:list)
			{
				retList.add((ObjectId)o);
			}
		}
		return retList;
	}
	public void setNoEdIds(List<ObjectId> noEdIds) {
		setSimpleValue("neids", MongoUtils.convert(noEdIds));
	}

	public String getResourceFrom() {
		return getSimpleStringValue("rf");
	}
	public void setResourceFrom(String resourceFrom) {
		setSimpleValue("rf", resourceFrom);
	}
}
