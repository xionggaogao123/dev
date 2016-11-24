package com.pojo.questions;


import com.mongodb.BasicDBObject;

import com.pojo.base.BaseDBObject;


public class PropertiesObj extends BaseDBObject{

	/**
	 * @author zoukai
	 * 属性信息，依附于ItemProperty
	 * id  
	 * nm
	 */
	private static final long serialVersionUID = 1L;
	
	public PropertiesObj(){
		super();
	}
	
	public PropertiesObj(BasicDBObject baseEntry) {
        super(baseEntry);
    }
	
	public PropertiesObj(String id,String nm){
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
