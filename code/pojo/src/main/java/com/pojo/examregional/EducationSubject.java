package com.pojo.examregional;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;

/**
 * 科目信息，依附于SchoolEntry
 * <pre>
 * {
         si:科目ID
 *       nm:名字
 *       ty:类型
 *       gis:年级ID
 *       [
 *       
 *       ]
 * }
 * </pre>
 * @author fourer
 */
public class EducationSubject extends BaseDBObject {
	
	public EducationSubject(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public EducationSubject(String name, List<ObjectId> gradeIdList) {
		super();
		BasicDBObject dbo =new BasicDBObject()
		.append("si", new ObjectId())
		.append("nm", name)
		.append("gis",  MongoUtils.convert(gradeIdList));
		setBaseEntry(dbo);
	}
	
	public EducationSubject(String name, int type,List<ObjectId> gradeIdList) {
		super();
		BasicDBObject dbo =new BasicDBObject()
		.append("si", new ObjectId())
		.append("nm", name)
		.append("ty", type)
		.append("gis",  MongoUtils.convert(gradeIdList));
		setBaseEntry(dbo);
	}
	public ObjectId getSubjectId() {
		return getSimpleObjecIDValue("si");
	}
	public void setSubjectId(ObjectId subjectId) {
		setSimpleValue("si", subjectId);
	}
	public String getName() {
		return getSimpleStringValue("nm");
	}
	public void setName(String name) {
		setSimpleValue("nm", name);
	}
	
	
	public List<ObjectId> getGradeIds() {
		List<ObjectId> retList =new ArrayList<ObjectId>();
	    BasicDBList list =(BasicDBList)getSimpleObjectValue("gis");
	    if(null!=list && !list.isEmpty())
	    {
	        for(Object o:list)
	        {
	             retList.add((ObjectId)o);
	        }
	     }
	    return retList;
	}
	public void setGradeIds(List<ObjectId> gradeIds) {
		setSimpleValue("gis", MongoUtils.convert(gradeIds));
	}

	public int getSubjectType() {
		return getSimpleIntegerValue("ty");
	}
	public void setSubjectType(int subjectType) {
		setSimpleValue("ty", subjectType);
	}
	
	
}
