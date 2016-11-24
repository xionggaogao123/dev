package com.pojo.registration;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
/**
 * 评价子项目Entry
 * collection : subquality
 {
           nm(name) : 评价项目名称
           re(requirement) : 项目要求
           pid(parentId) : 父级评价项目ObjectId
		   ti(time)创建时间
 }
 * @author cxy
 * 2015-11-25 14:13:28
 */
public class SubQualityEntry extends BaseDBObject {
	public SubQualityEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}

	public SubQualityEntry(String name, String requirement , ObjectId parentId) {
		super();

		BasicDBObject baseEntry = new BasicDBObject().append("nm", name)
													 .append("rq", requirement)
													 .append("pid", parentId)
													 .append("ti", System.currentTimeMillis());

		setBaseEntry(baseEntry);
	}
	
	public String getName() {
		return getSimpleStringValue("nm");
	}
	public void setName(String name) {
		setSimpleValue("nm", name);
	}
	
	public String getRequirement() {
		return getSimpleStringValue("rq");
	}
	public void setRequirement(String requirement) {
		setSimpleValue("rq", requirement);
	}
}
