package com.pojo.salary;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
/**
 * 2015-07-18 15:54:35
 * 
 *  工资项目Entry类
 * <pre>
 * collectionName:salaryItem
 * </pre>
 * <pre>
 * {
 * 	sid (schoolId),学校ID
 *  in (itemName), 不能重复
 *  it (itemType), 发款和扣款
 * }
 * </pre>
 * @author yangling
 * 2015-07-18 15:54:35
 */
public class SalaryItemEntry extends BaseDBObject {
	
	private static final long serialVersionUID = 2004296173128260124L;

	public SalaryItemEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public SalaryItemEntry(String itemName, String itemType,ObjectId schoolId) {
		super();
		
		BasicDBObject baseEntry =new BasicDBObject()
		.append("in", itemName)
		.append("it", itemType)
		.append("sid", schoolId);
		
		setBaseEntry(baseEntry);
	}
	
	public SalaryItemEntry(String itemName, String itemType) {
		super();
		
		BasicDBObject baseEntry =new BasicDBObject()
		.append("in", itemName)
		.append("it", itemType);
		
		setBaseEntry(baseEntry);
	}
	
	public String getItemName(){
		return this.getSimpleStringValue("in");
	}
	
	public void setItemName(String itemName){
		this.setSimpleValue("in", itemName);
	}
	
	public String getItemType(){
		return this.getSimpleStringValue("it");
	}
	
	public void setItemType(String itemType){
		this.setSimpleValue("it", itemType);
	}
	

	public ObjectId getSchoolId() {
		return getSimpleObjecIDValue("sid");
	}

	public void setSchoolId(ObjectId schoolId) {
		this.setSimpleValue("sid", schoolId);
	}
	
}
