package com.pojo.property;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
/**
 * 
 * @author cxy
 * 2015-8-6 10:12:35
 * collectionName  : propertyClassification
 * 校产分类Id      : pcid(propertyClassificationId) -->这是String类型的
 * 校产分类名称	   : pcna(propertyClassificationName)
 * 校产分类描述    	   : pcps(propertyClassificationPostscript)
 * 校产分类父节点id : pcpid(propertyClassificationParentId) -->这是String类型的
 * 校产分类学校Id   : scid(schoolId)
 * 删除标志位                  : ir(isRemoved,0为未删除，1为已删除)
 */
public class PropertyClassificationEntry extends BaseDBObject{
	public PropertyClassificationEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public PropertyClassificationEntry(ObjectId shcoolId,String propertyClassificationName,String propertyClassificationPostscript,
												String propertyClassificationId,String propertyClassificationParentId) {
		super();

		BasicDBObject baseEntry = new BasicDBObject().append("scid", shcoolId)
									 .append("ir", Constant.ZERO)
									 .append("pcid", propertyClassificationId)
									 .append("pcna", propertyClassificationName)
									 .append("pcps", propertyClassificationPostscript)
									 .append("pcpid", propertyClassificationParentId)
									 .append("scid", shcoolId);
		setBaseEntry(baseEntry);
	}
	
	public String getPropertyClassificationName() {
		return getSimpleStringValue("pcna");
	}

	public void setPropertyClassificationName(String propertyClassificationName) {
		setSimpleValue("pcna", propertyClassificationName);
	}
	
	public String getPropertyClassificationPostscript() {
		return getSimpleStringValue("pcps");
	}

	public void setPropertyClassificationPostscript(String propertyClassificationPostscript) {
		setSimpleValue("pcps", propertyClassificationPostscript);
	}
	
	public String getPropertyClassificationId() {
		return getSimpleStringValue("pcid");
	}

	public void setPropertyClassificationId(String propertyClassificationId) {
		setSimpleValue("pcid", propertyClassificationId);
	}
	
	public String getPropertyClassificationParentId() {
		return getSimpleStringValue("pcpid");
	}
	
	public void setPropertyClassificationParentId(String propertyClassificationParentId) {
		setSimpleValue("pcpid", propertyClassificationParentId);
	}
	
	public ObjectId getSchoolId() {
		return getSimpleObjecIDValue("scid");
	}
	public void setSchoolId(String schoolId) {
		setSimpleValue("scid", schoolId);
	}
}
