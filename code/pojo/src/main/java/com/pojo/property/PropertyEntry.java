package com.pojo.property;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

/**
 * @author cxy 
 * 	2015-7-26 17:50:03
 *  校园资产Entry类 
 *  collectionName:property 
 *  校产编号 : pnu(propertyNumber) 
 *  校产名称 : pna(propertyName) 
 *  校产规格 : psp(propertySpecifications)
 *  校产产地 : po(propertyOrgin)
 *  校产品牌 : pb(propertyBrand)
 *  校产分类id : pcid(propertyClassificationId)
 *  所属学校id : scid(schoolId)
 *  删除标志位 : ir(isRemoved,0为未删除，1为已删除)
 */
public class PropertyEntry extends BaseDBObject{
	
	public PropertyEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public PropertyEntry(ObjectId shcoolId,String propertyNumber,String propertyName,String propertySpecifications,
							String propertyOrgin,String propertyBrand,ObjectId propertyClassificationId) {
		super();

		BasicDBObject baseEntry = new BasicDBObject().append("scid", shcoolId)
													 .append("ir", Constant.ZERO)
													 .append("pnu", propertyNumber)
													 .append("pna", propertyName)
													 .append("psp", propertySpecifications)
													 .append("po", propertyOrgin)
													 .append("pb", propertyBrand)
													 .append("pcid", propertyClassificationId);
		setBaseEntry(baseEntry);
	}
	
	public String getPropertyNumber() {
		return getSimpleStringValue("pnu");
	}

	public void setPropertyNumber(String propertyNumber) {
		setSimpleValue("pnu", propertyNumber);
	}
	
	public String getPropertyName() {
		return getSimpleStringValue("pna");
	}

	public void setPropertyName(String propertyName) {
		setSimpleValue("pna", propertyName);
	}
	
	public String getPropertySpecifications() {
		return getSimpleStringValue("psp");
	}
	
	public void setPropertySpecifications(String propertyName) {
		setSimpleValue("psp", propertyName);
	}
	
	public String getPropertyOrgin() {
		return getSimpleStringValue("po");
	}
	
	public void setPropertyOrgin(String propertyOrgin) {
		setSimpleValue("po", propertyOrgin);
	}
	
	public String getPropertyBrand() {
		return getSimpleStringValue("pb");
	}
	
	public void setPropertyBrand(String propertyBrand) {
		setSimpleValue("pb", propertyBrand);
	}
	
	public String getPropertyClassificationId() {
		return getSimpleStringValue("pcid");
	}
	public void setPropertyClassificationId(String propertyClassificationId) {
		setSimpleValue("pcid", propertyClassificationId);
	}
	
	public ObjectId getSchoolId() {
		return getSimpleObjecIDValue("scid");
	}
	public void setSchoolId(String schoolId) {
		setSimpleValue("scid", schoolId);
	}
}
