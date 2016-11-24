package com.pojo.cloudlesson;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
/**
 * 云课程
 * <pre>
 * collectionName:cloudclasses
 * </pre>
 * <pre>
 * {
 *  nm:名字
 *  con:内容
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
	
	
	
	
	
	public CloudLessonEntry(String name, String content, int order, String imageUrl, List<ObjectId> videoIds, int subject,
			List<Integer> cloudClassGradeTypes, List<ObjectId> cloudClassTypes) {
		super();
		BasicDBObject baseEntry =new BasicDBObject()
		.append("nm", name)
		.append("con", content)
		.append("or", order)
		.append("im", imageUrl)
		.append("vis", videoIds)
		.append("sub", subject)
		.append("ccgts", MongoUtils.convert(cloudClassGradeTypes))
		.append("cctys", MongoUtils.convert(cloudClassTypes));
		setBaseEntry(baseEntry);
		
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
	
	public ObjectId getCloudClassTypes() {
		return getSimpleObjecIDValue("cctys");
	}
	public void setCloudClassTypes(List<ObjectId> cloudClassTypes) {
		  setSimpleValue("cctys", MongoUtils.convert(cloudClassTypes));
	}
}
