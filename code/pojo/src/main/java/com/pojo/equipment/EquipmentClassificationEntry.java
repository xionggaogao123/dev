package com.pojo.equipment;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
/**
 * 
 * @author cxy
 * 2015-8-6 10:12:35
 * collectionName  : equipmentclassification
 * 设备分类Id      : pcid(equipmentClassificationId) -->这是String类型的
 * 设备分类名称	   : pcna(equipmentClassificationName)
 * 设备分类描述    	   : pcps(equipmentClassificationPostscript)
 * 设备分类父节点id : pcpid(equipmentClassificationParentId) -->这是String类型的
 * 设备分类学校Id   : scid(schoolId)
 * 删除标志位                  : ir(isRemoved,0为未删除，1为已删除)
 */
public class EquipmentClassificationEntry extends BaseDBObject{
	public EquipmentClassificationEntry(BasicDBObject baseEntry) {
		super(baseEntry);
	}
	
	public EquipmentClassificationEntry(ObjectId shcoolId,String equipmentClassificationName,String equipmentClassificationPostscript,
												String equipmentClassificationId,String equipmentClassificationParentId) {
		super();

		BasicDBObject baseEntry = new BasicDBObject().append("scid", shcoolId)
									 .append("ir", Constant.ZERO)
									 .append("pcid", equipmentClassificationId)
									 .append("pcna", equipmentClassificationName)
									 .append("pcps", equipmentClassificationPostscript)
									 .append("pcpid", equipmentClassificationParentId)
									 .append("scid", shcoolId);
		setBaseEntry(baseEntry);
	}
	
	public String getEquipmentClassificationName() {
		return getSimpleStringValue("pcna");
	}

	public void setEquipmentClassificationName(String equipmentClassificationName) {
		setSimpleValue("pcna", equipmentClassificationName);
	}
	
	public String getEquipmentClassificationPostscript() {
		return getSimpleStringValue("pcps");
	}

	public void setEquipmentClassificationPostscript(String equipmentClassificationPostscript) {
		setSimpleValue("pcps", equipmentClassificationPostscript);
	}
	
	public String getEquipmentClassificationId() {
		return getSimpleStringValue("pcid");
	}

	public void setEquipmentClassificationId(String equipmentClassificationId) {
		setSimpleValue("pcid", equipmentClassificationId);
	}
	
	public String getEquipmentClassificationParentId() {
		return getSimpleStringValue("pcpid");
	}
	
	public void setEquipmentClassificationParentId(String equipmentClassificationParentId) {
		setSimpleValue("pcpid", equipmentClassificationParentId);
	}
	
	public ObjectId getSchoolId() {
		return getSimpleObjecIDValue("scid");
	}
	public void setSchoolId(String schoolId) {
		setSimpleValue("scid", schoolId);
	}
}
