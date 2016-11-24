package com.pojo.research;




import java.util.Date;




import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;

/**
 * @author zk
 *		2015.08.25  09:41
 *		教研专题Entry类
 *		collectionName ： teachingAndResearch
 *		教研专题分类:  trsc(teachingandResearchSubjectClassification)
 *		教研专题名称：    tartt (teachingAndResearchTopicsTitle)
 *		发布时间:	   ptime(publishTime)
 *		正文:       cot(contentText)
 *		封面:		   cmg(coverImage)
 *		发布人ID:  puid (publishUserId)
 *		发布人name: puna (publishUserName)
 */
public class TeachingAndResearchEntry extends BaseDBObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TeachingAndResearchEntry() {
		super();
		
	}

	public TeachingAndResearchEntry(BasicDBObject baseEntry){
		super(baseEntry);
	}
	
	public TeachingAndResearchEntry
		(String teachingandResearchSubjectClassification,String teachingAndResearchTopicsTitle,
		String contentText,String coverImage,ObjectId publishUserId,String publishUserName){
			super();
		
		BasicDBObject baseEntry=new BasicDBObject().append("trsc",teachingandResearchSubjectClassification)
												   .append("tartt",teachingAndResearchTopicsTitle)
												   .append("ptime",new Date().getTime())
												   .append("cot",contentText)
												   .append("puid",publishUserId)
												   .append("puna",publishUserName)
												   .append("cmg",coverImage);
				setBaseEntry(baseEntry);
	}
	
	public String getTeachingandResearchSubjectClassification(){
		return getSimpleStringValue("trsc");
	}
	
	public void setTeachingandResearchSubjectClassification(String teachingandResearchSubjectClassification){
		setSimpleValue("trsc",teachingandResearchSubjectClassification);
	}
	
	public String getTeachingAndResearchTopicsTitle(){
		return getSimpleStringValue("tartt");
	}
	
	public void setTeachingAndResearchTopicsTitle(String teachingAndResearchTopicsTitle){
		setSimpleValue("tartt",teachingAndResearchTopicsTitle);
	}
	
	public long getPublishTime(){
		return getSimpleLongValue("ptime");
	}
	
	public void setPublishTime(){
		setSimpleValue("ptime",new Date().getTime());
	}
	
	public String getContentText(){
		return getSimpleStringValue("cot");
	}
	
	public void setContentText(String contentText){
		setSimpleValue("cot",contentText);
	}
	
	public String getCoverImage(){
		return getSimpleStringValue("cmg");
	}
	
	public void setCoverImage(String coverImage){
		setSimpleValue("cmg",coverImage);
	}
	
	public ObjectId getProjectUserId() {
		return getSimpleObjecIDValue("puid");
	}
	public void setProjectUserId(ObjectId projectUserId) {
		setSimpleValue("puid", projectUserId);
	}
	
	public String getProjectUserName() {
		return getSimpleStringValue("puna");
	}
	
	public void setProjectUserName(String projectUserName) {
		setSimpleValue("puna", projectUserName);
	}
	
}
