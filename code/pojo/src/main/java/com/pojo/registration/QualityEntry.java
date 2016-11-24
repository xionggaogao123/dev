package com.pojo.registration;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
/**
 * 评价项目Entry
 * collection : quality
 {
           nm(name) : 评价项目名称
		   sid(schoolId) :学校Id
		   ti(time)创建时间
 }
 * @author cxy
 * 2015-11-25 14:13:28
 */
public class QualityEntry extends BaseDBObject {
	public QualityEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}

	public QualityEntry(String name,ObjectId schoolId) {
		super();

		BasicDBObject baseEntry = new BasicDBObject().append("nm", name)
													 .append("sid",schoolId)
													 .append("ti", System.currentTimeMillis());

		setBaseEntry(baseEntry);
	}
	
	public String getName() {
		return getSimpleStringValue("nm");
	}
	public void setName(String name) {
		setSimpleValue("nm", name);
	}
}
