package com.db.equipment;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.equipment.EquipmentEntry;
import com.sys.constants.Constant;

/**
 * 校园器材Dao
 * 2015-7-28 15:23:29
 *
 * @author cxy
 */
public class EquipmentDao extends BaseDao {

    /**
     * 添加一条校园器材信息
     *
     * @param e
     * @return
     */
    public ObjectId addEquipmentEntry(EquipmentEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EQUIPMENT, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 根据Id查询一个特定的校园器材信息
     *
     * @param id
     * @return
     */
    public EquipmentEntry getEquipmentEntry(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EQUIPMENT, query, Constant.FIELDS);
        if (null != dbo) {
            return new EquipmentEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 删除一条校园器材
     *
     * @param id
     */
    public void deleteEquipment(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject().append("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EQUIPMENT, query, updateValue);
    }

    /**
     * 根据ID更新一条校园器材信息
     */
    public void updateEquipment(ObjectId id, String equipmentNumber, String equipmentName, String equipmentSpecifications,
                                String equipmentOrgin, String equipmentBrand, String equipmentUserName) {

        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
                new BasicDBObject()
                        .append("enu", equipmentNumber)
                        .append("ena", equipmentName)
                        .append("esp", equipmentSpecifications)
                        .append("eo", equipmentOrgin)
                        .append("eb", equipmentBrand)
                        .append("euna", equipmentUserName));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EQUIPMENT, query, updateValue);

    }

    /**
     * 根据学校ID和分类ID查询器材信息
     *
     * @param id
     * @return
     */
    public List<EquipmentEntry> queryPropertiesBySchoolIdAndEquipmentClassificationId(ObjectId schoolId, ObjectId equipmentClassificationId) {
        BasicDBObject query = new BasicDBObject();
        query.append("ecid", equipmentClassificationId)
                .append("scid", schoolId)
                .append("ir", Constant.ZERO);
        DBObject orderBy = new BasicDBObject("enu", Constant.ASC);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_EQUIPMENT, query, Constant.FIELDS, orderBy);
        List<EquipmentEntry> resultList = new ArrayList<EquipmentEntry>();
        for (DBObject dbObject : dbObjects) {
            EquipmentEntry equipmentEntry = new EquipmentEntry((BasicDBObject) dbObject);
            resultList.add(equipmentEntry);
        }
        return resultList;
    }

    /**
     * 根据分类，删除校园资产
     *
     * @param id
     */
    public void deleteEquipmentsByEquipmentClassificationId(ObjectId id) {
        DBObject query = new BasicDBObject("pcid", id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject().append("ir", 1));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_EQUIPMENT, query, updateValue);
    }

}
