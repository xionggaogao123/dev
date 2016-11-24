package com.pojo.registration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.level.LevelEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
/**
 * 子评价项目信息，依附于QualityObject
 {
		   			nm(name) : 评价项目名称
		   			rq(requirement) : 主要表现和要求
		   			slv(selfLevel) : 自我评价等级
		   			tlv(teacherLevel) : 教师评价等级
		   			
 }
 * @author cxy
 * 2015-9-27 17:03:19
 */
public class SubQualityObject extends BaseDBObject {
	public SubQualityObject(BasicDBObject baseEntry) {
		super(baseEntry);
	}

	public SubQualityObject(String name,String requirement) {
		super();

		BasicDBObject baseEntry = new BasicDBObject().append("nm", name)
													 .append("rq", requirement)
													 .append("slv", Constant.EMPTY)
													 .append("tlv", Constant.EMPTY);

		setBaseEntry(baseEntry);
	}
	
	public String getName() {
		return getSimpleStringValue("nm");
	}
	public void setName(String name) {
		setSimpleValue("nm", name);
	}
	
	public String getSelfLevel() {
		return getSimpleStringValue("slv");
	}
	public void setSelfLevel(String selfLevel) {
		setSimpleValue("slv", selfLevel);
	}
	
	public String getTeacherLevel() {
		return getSimpleStringValue("tlv");
	}
	public void setTeacherLevel(String teacherLevel) {
		setSimpleValue("tlv", teacherLevel);
	}
	
	public String getRequirement() {
		return getSimpleStringValue("rq");
	}
	public void setRequirement(String requirement) {
		setSimpleValue("rq", requirement);
	}
	
	
}
