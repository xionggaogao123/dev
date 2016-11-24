package com.fulaan.equipment.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.equipment.EquipmentDao;
import com.pojo.equipment.EquipmentEntry;
/**
 * 
 * @author cxy
 *
 */
@Service
public class EquipmentService {
	
	EquipmentDao equipmentDao = new EquipmentDao();
	
	/**
	 * 添加一条校园器材信息
	 * @param e
	 * @return
	 */
	public ObjectId addEquipmentEntry(EquipmentEntry e){
		return equipmentDao.addEquipmentEntry(e);
	}
	
	/**
	 * 根据Id查询一个特定的校园器材信息
	 * @param id
	 * @return
	 */
	public EquipmentEntry getEquipmentEntry(ObjectId id){
		return equipmentDao.getEquipmentEntry(id);
	}
	
	/**
	 * 删除一条校园器材
	 * @param id
	 */
	public void deleteEquipment(ObjectId id){
		equipmentDao.deleteEquipment(id);;
	}
	
	/**
	 * 根据ID更新一条校园器材信息
	 */
	public void updateEquipment(ObjectId id,String equipmentNumber,String equipmentName,
							String equipmentSpecifications,String equipmentOrgin,String equipmentBrand,String equipmentUserName){
		equipmentDao.updateEquipment(id, equipmentNumber, equipmentName, equipmentSpecifications, equipmentOrgin, equipmentBrand,equipmentUserName);;
		
	}
	
	/**
	 * 根据学校ID和分类ID查询器材信息
	 * @param id
	 * @return
	 */
	public List<EquipmentEntry> queryPropertiesBySchoolIdAndEquipmentClassificationId(ObjectId schoolId,ObjectId equipmentClassificationId)
	{
		return equipmentDao.queryPropertiesBySchoolIdAndEquipmentClassificationId(schoolId, equipmentClassificationId);
	}
	/**
	 * 根据分类，删除校园设备
	 * @param id
	 */
	public void deleteEquipmentsByEquipmentClassificationId(ObjectId id){
		equipmentDao.deleteEquipmentsByEquipmentClassificationId(id); 
	}
}
