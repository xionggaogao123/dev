package com.pojo.equipment;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;

/**
 * @author cxy 
 * 	2015-7-26 17:50:03
 *  校园器材Entry类 
 *  collectionName : equipment 
 *  器材编号 : enu(equipmentNumber) 
 *  器材名称 : ena(equipmentName) 
 *  器材规格 : esp(equipmentSpecifications)
 *  器材产地 : eo(equipmentOrgin)
 *  器材品牌 : eb(equipmentBrand)
 *  器材分类id : ecid(equipmentClassificationId)
 *  使用人员name : euna(equipmentUserName)
 *  所属学校id : scid(schoolId)
 *  删除标志位 : ir(isRemoved,0为未删除，1为已删除)
 */
public class EquipmentEntry extends BaseDBObject{
	
	public EquipmentEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public EquipmentEntry(ObjectId shcoolId,String equipmentNumber,String equipmentName,String equipmentSpecifications,
							String equipmentOrgin,String equipmentBrand,ObjectId equipmentClassificationId,String equipmentUserName) {
		super();

		BasicDBObject baseEntry = new BasicDBObject().append("scid", shcoolId)
													 .append("ir", Constant.ZERO)
													 .append("enu", equipmentNumber)
													 .append("ena", equipmentName)
													 .append("esp", equipmentSpecifications)
													 .append("eo", equipmentOrgin)
													 .append("eb", equipmentBrand)
													 .append("euna", equipmentUserName)
													 .append("ecid", equipmentClassificationId);
		setBaseEntry(baseEntry);
	}
	
	public String getEquipmentNumber() {
		return getSimpleStringValue("enu");
	}

	public void setEquipmentNumber(String equipmentNumber) {
		setSimpleValue("enu", equipmentNumber);
	}
	
	public String getEquipmentName() {
		return getSimpleStringValue("ena");
	}

	public void setEquipmentName(String equipmentName) {
		setSimpleValue("ena", equipmentName);
	}
	
	public String getEquipmentSpecifications() {
		return getSimpleStringValue("esp");
	}
	
	public void setEquipmentSpecifications(String equipmentName) {
		setSimpleValue("esp", equipmentName);
	}
	
	public String getEquipmentOrgin() {
		return getSimpleStringValue("eo");
	}
	
	public void setEquipmentOrgin(String equipmentOrgin) {
		setSimpleValue("eo", equipmentOrgin);
	}
	
	public String getEquipmentBrand() {
		return getSimpleStringValue("eb");
	}
	
	public void setEquipmentBrand(String equipmentBrand) {
		setSimpleValue("eb", equipmentBrand);
	}
	
	public String getEquipmentUserName() {
		return getSimpleStringValue("euna");
	}
	
	public void setEquipmentUserName(String equipmentUserName) {
		setSimpleValue("euna", equipmentUserName);
	}
	
	public String getEquipmentClassificationId() {
		return getSimpleStringValue("ecid");
	}
	public void setEquipmentClassificationId(String equipmentClassificationId) {
		setSimpleValue("ecid", equipmentClassificationId);
	}
	
	public String getSchoolId() {
		return getSimpleStringValue("scid");
	}
	public void setSchoolId(String schoolId) {
		setSimpleValue("scid", schoolId);
	}
}
