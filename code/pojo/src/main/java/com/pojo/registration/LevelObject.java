package com.pojo.registration;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
/**
 * 属性信息，依附于QualityObject和SubQualityObject
{
		id: ID
		nm : name 名称 
		sr : scoreRange 分数范围 
}
 * @author cxy
 * 2015-9-27 17:03:24
 */
public class LevelObject  extends BaseDBObject {
	public LevelObject(BasicDBObject baseEntry) {
		super(baseEntry);
	}

	public LevelObject(String name,int scoreRange) {
		super();

		BasicDBObject baseEntry = new BasicDBObject().append("nm", name)
													 .append("sr", scoreRange)
													 .append("id", new ObjectId());

		setBaseEntry(baseEntry);
	}
	
	public String getName() {
		return getSimpleStringValue("nm");
	}
	public void setName(String name) {
		setSimpleValue("nm", name);
	}
	
	public int getScoreRange() {
		return getSimpleIntegerValueDef("sr",0);
	}
	public void setScoreRange(int scoreRange) {
		setSimpleValue("sr", scoreRange);
	}
}
