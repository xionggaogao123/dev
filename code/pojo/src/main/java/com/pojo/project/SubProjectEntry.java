package com.pojo.project;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;

/**
 * @author cxy 
 * 			2015-7-26 17:50:03
 *         子课题Entry类 
 *         collectionName : subProject 
 *         发布人ID : pid(publishId)
 *         发布人名 :  pna(publishName)
 *         子课题名称 : spna(subProjectName)
 *         发布时间 : pt(publishTime) long
 *         正文 : spc(subProjectContent) 
 *         课件[] : cs(courseware)
 *         父课题ID: paid(parentId)
 *         封面图片: pc(projectCover)
 *         
 */
public class SubProjectEntry extends BaseDBObject{
	
	public SubProjectEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public SubProjectEntry(ObjectId publishId,String publishName,String subProjectName,long publishTime,
								String subProjectContent,List<ObjectId> courseware,ObjectId parentId,String projectCover) {
			super();

			BasicDBObject baseEntry = new BasicDBObject().append("pid", publishId)
									 					 .append("pna", publishName)
									 					 .append("spna", subProjectName)
									 					 .append("pt", publishTime)
									 					 .append("spc", subProjectContent)
									 					 .append("cs", courseware)
									 					 .append("paid", parentId)
									 					 .append("pc", projectCover);

			setBaseEntry(baseEntry);
	}
	
	public ObjectId getPublishId() {
		return getSimpleObjecIDValue("pid");
	}
	public void setPublishId(String publishId) {
		setSimpleValue("pid", publishId);
	}
	
	public String getPublishName() {
		return getSimpleStringValue("pna");
	}
	
	public void setPublishName(String publishName) {
		setSimpleValue("pna", publishName);
	}
	
	public String getSubProjectName() {
		return getSimpleStringValue("spna");
	}
	
	public void setSubProjectName(String subProjectName) {
		setSimpleValue("spna", subProjectName);
	}
	
	public long getPublishTime() {
		return getSimpleLongValue("pt");
	}
	public void setPublishTime(long publishTime) {
		setSimpleValue("pt", publishTime);
	}
	
	public String getSubProjectContent() {
		return getSimpleStringValue("spc");
	}
	
	public void setSubProjectContent(String subProjectContent) {
		setSimpleValue("spc", subProjectContent);
	}
	
	public List<ObjectId> getCoursewareList() {
		List<ObjectId> resultList =new ArrayList<ObjectId>();
		BasicDBList list =(BasicDBList)getSimpleObjectValue("cs");
		if(null!=list && !list.isEmpty())
		{
				for(Object o:list)
				{
					resultList.add( (ObjectId)o);
				}
		}
		return resultList;
	}
	public void setCoursewareList(List<ObjectId> coursewareList) {
		setSimpleValue("cs",  MongoUtils.convert(coursewareList));
	}
	
	public ObjectId getParentId() {
		return getSimpleObjecIDValue("paid");
	}
	public void setParentId(String parentId) {
		setSimpleValue("paid", parentId);
	}
	
	public String getProjectCover() {
		return getSimpleStringValue("pc");
	}
	public void setProjectCover(String projectCover) {
		setSimpleValue("pc", projectCover);
	}
}
