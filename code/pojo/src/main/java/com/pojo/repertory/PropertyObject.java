package com.pojo.repertory;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
/**
 * 属性信息，依附于RepertoryProperty
 * id  
 * nm
 * @author cxy
 * 2015-9-15 21:01:51
 */
public class PropertyObject extends BaseDBObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7071237571845880069L;
	public PropertyObject(BasicDBObject baseEntry) {
		super(baseEntry);
	}


	public PropertyObject(String id, String nm) {
		super();
		BasicDBObject baseEntry =new BasicDBObject()
		.append("id", id)
		.append("nm", nm);
		setBaseEntry(baseEntry);
	}
	
	public String getId() {
		return getSimpleStringValue("id");
	}
	public void setId(String id) {
		setSimpleValue("id", id);
	}
	
	public String getName() {
		return getSimpleStringValue("nm");
	}
	public void setName(String name) {
		setSimpleValue("nm", name);
	}
}
