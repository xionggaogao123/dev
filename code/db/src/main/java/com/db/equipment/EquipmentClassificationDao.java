package com.db.equipment;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.equipment.EquipmentClassificationEntry;
import com.sys.constants.Constant;

/**
 * 设备分类Dao
 * @author cxy
 * 2015-8-6 10:24:47
 * 
 */
public class EquipmentClassificationDao extends BaseDao{
	/**
	 * 添加一条校园设备分类信息
	 * @param e
	 * @return
	 */
	public ObjectId addEquipmentClassificationEntry(EquipmentClassificationEntry e)
	{
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_EQUIPMENT_CLASSIFICATION, e.getBaseEntry());
		return e.getID();
	}
	
	/**
	 * 根据Id查询一个特定的校园设备分类信息
	 * @param id
	 * @return
	 */
	public EquipmentClassificationEntry getEquipmentClassificationEntry(ObjectId id)
	{
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EQUIPMENT_CLASSIFICATION, query, Constant.FIELDS);
		if(null!=dbo)
		{
			return new EquipmentClassificationEntry((BasicDBObject)dbo);
		}
		return null;
	}
	
	/**
	 * 删除一条校园设备分类
	 * @param id
	 */
	public void deleteEquipmentClassification(String id){
		DBObject query =new BasicDBObject("pcid",id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject().append("ir", 1));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_EQUIPMENT_CLASSIFICATION, query, updateValue);
	}
	
	/**
	 * 根据ID更新一条校园设备分类信息
	 */
	public void updateEquipmentClassification(ObjectId id,String equipmentClassificationName,String equipmentClassificationPostscript,
												String equipmentClassificationId,String equipmentClassificationParentId){

		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,
													new BasicDBObject()
														.append("pcna", equipmentClassificationName)
														.append("pcps", equipmentClassificationPostscript)
														.append("pcid", equipmentClassificationId)
														.append("pcpid", equipmentClassificationParentId));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_EQUIPMENT_CLASSIFICATION, query, updateValue);
		
	}
	/**
	 * 根据ID更新一条校园设备分类信息的基本信息
	 */
	public void updateEquipmentClassificationBaseInfo(String id,String equipmentClassificationName,String equipmentClassificationPostscript){

		DBObject query =new BasicDBObject("pcid",id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,
													new BasicDBObject()
														.append("pcna", equipmentClassificationName)
														.append("pcps", equipmentClassificationPostscript));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_EQUIPMENT_CLASSIFICATION, query, updateValue);
		
	}
	/**
	 * 根据学校ID查询设备信息
	 * @param id
	 * @return
	 */
	public List<EquipmentClassificationEntry> queryEquipmentClassificationBySchoolIdAndEquipmentClassificationId(ObjectId schoolId)
	{
		BasicDBObject query = new BasicDBObject();
		query.append("scid",schoolId)
			 .append("ir",Constant.ZERO);
		DBObject orderBy = new BasicDBObject("_id",Constant.ASC); 
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(),Constant.COLLECTION_EQUIPMENT_CLASSIFICATION,query,Constant.FIELDS,orderBy);
        List<EquipmentClassificationEntry> resultList = new ArrayList<EquipmentClassificationEntry>();
        for(DBObject dbObject:dbObjects){
        	EquipmentClassificationEntry equipmentClassificationEntry = new EquipmentClassificationEntry((BasicDBObject)dbObject);
        	resultList.add(equipmentClassificationEntry);
        }
		return resultList;
	}
	
	/**
	 * 根据父ID查询所有孩子设备信息
	 * @param id
	 * @return
	 */
	public List<EquipmentClassificationEntry> queryEquipmentClassificationByParentId(String parentId)
	{
		BasicDBObject query = new BasicDBObject();
		query.append("pcpid",parentId)
			 .append("ir",Constant.ZERO);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(),Constant.COLLECTION_EQUIPMENT_CLASSIFICATION,query,Constant.FIELDS);
        List<EquipmentClassificationEntry> resultList = new ArrayList<EquipmentClassificationEntry>();
        for(DBObject dbObject:dbObjects){
        	EquipmentClassificationEntry equipmentClassificationEntry = new EquipmentClassificationEntry((BasicDBObject)dbObject);
        	resultList.add(equipmentClassificationEntry);
        }
		return resultList;
	}
	/**
	 * 删除一个学校所有设备分类
	 * @param id
	 */
	public void deleteAllEquipmentClassifications(ObjectId id){
		DBObject query = new BasicDBObject("scid",id);
		remove(MongoFacroty.getAppDB(), Constant.COLLECTION_EQUIPMENT_CLASSIFICATION, query);
	}
}
