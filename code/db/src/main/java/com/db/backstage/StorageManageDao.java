package com.db.backstage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.backstage.StorageManageEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: taotao.chan
 * @Date: 2018/9/18 10:19
 * @Description:
 */
public class StorageManageDao extends BaseDao {

    //批量新增
    public void saveList(List<DBObject> storageManageDbObjectList) {
        save(MongoFacroty.getAppDB(),Constant.COLLECTION_PHONES_STORAGE_MANAGE,storageManageDbObjectList);
    }

    public Map<String,Object> getStorageInfoList(int page, int pageSize, String imeiNo, String storageStatus, String useStatus, String year, String month) {
        //封装查询参数
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO);
        if (!"".equals(imeiNo)){
            query.append("imeiNo", imeiNo);
        }
        if (!"".equals(storageStatus)) {
            if ("0".equals(storageStatus)){
                //新机入库查询
                query.append("storageStatus", storageStatus);
            }else {
                List<String> storageStatusList = new ArrayList<String>();
                storageStatusList.add("1");
                storageStatusList.add("3");
                storageStatusList.add("4");
                query.append("storageStatus", new BasicDBObject(Constant.MONGO_IN,storageStatusList));
            }

        }
        if (!"".equals(useStatus)) {
            query.append("useStatus", useStatus);
        }
        if (!"".equals(year)) {
            query.append("inStorageYear", year);
        }
        if (!"".equals(month)) {
            query.append("inStorageMonth", month);
        }
        //出库不展示
        List<String> outStorageStatusList = new ArrayList<String>();
        outStorageStatusList.add("5");
        query.append("storageStatus", new BasicDBObject(Constant.MONGO_NOTIN,outStorageStatusList));
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_STORAGE_MANAGE, query, Constant.FIELDS,
                new BasicDBObject("inStorageTime", Constant.DESC),(page-1)*pageSize,pageSize);
        List<StorageManageEntry> storageManageEntryList = new ArrayList<StorageManageEntry>();
        for (DBObject dbObject : dbObjects){
            storageManageEntryList.add(new StorageManageEntry(dbObject));
        }
        Map<String,Object> result = new HashMap<String, Object>();
        result.put("entryList",storageManageEntryList);
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_STORAGE_MANAGE, query);
        result.put("count",count);
        return result;
    }

    /**
     * 库存总量
     * @return
     */
    public int getStorageCount() {
        //封装查询参数
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO);
        //出库不展示
        List<String> outStorageStatusList = new ArrayList<String>();
        outStorageStatusList.add("5");
        query.append("storageStatus", new BasicDBObject(Constant.MONGO_NOTIN,outStorageStatusList));
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_STORAGE_MANAGE, query);
        return count;
    }

    /**
     * 新机入库数量
     * @return
     */
    public int getStorageListGroupByStorageStatus() {
        //封装查询参数
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("storageStatus", "0");
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_STORAGE_MANAGE, query);
        return count;
    }

    /**
     * 可用数量
     * @return
     */
    public int getStorageListGroupByUseStatus() {
        //封装查询参数
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("useStatus", "1");
        //出库不展示
        List<String> outStorageStatusList = new ArrayList<String>();
        outStorageStatusList.add("5");
        query.append("storageStatus", new BasicDBObject(Constant.MONGO_NOTIN,outStorageStatusList));
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_STORAGE_MANAGE, query);
        return count;
    }

    public String updateStorageInfoById(Map map) {
        BasicDBObject query = new BasicDBObject();
        query.append(Constant.ID, new ObjectId(map.get("id").toString()));

        BasicDBObject updateParam = new BasicDBObject();
        if (map.get("storageStatus") != null) {
            updateParam.append("storageStatus",map.get("storageStatus").toString());
        }
        if (map.get("comment") != null) {
            updateParam.append("comment",map.get("comment").toString());
        }
        if (map.get("useStatus") != null) {
            updateParam.append("useStatus",map.get("useStatus").toString());
        }
        if (map.get("isr") != null) {
            updateParam.append("isr",map.get("isr"));
        }
        if (map.get("commentType") != null) {
            updateParam.append("commentType",map.get("commentType").toString());
        }
        //对拼接的Id做处理
        List<String> needRepairCommentList = new ArrayList<String>();
        if (map.get("needRepairComments") != null){
            for (String needRepairComment : map.get("needRepairComments").toString().split(",")){
                if (needRepairComment != ""){
                    needRepairCommentList.add(needRepairComment);
                }
            }
            updateParam.append("needRepairComment",needRepairCommentList);
        }
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, updateParam);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_STORAGE_MANAGE,query,updateValue);
        return map.get("id").toString();
    }

    public String updateStorageInfoByIds(Map map) {
        BasicDBObject query = new BasicDBObject();
        //对拼接的Id做处理
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        if (map.get("ids") != null){
            for (String id : map.get("ids").toString().split(",")){
                if (id != ""){
                    objectIdList.add(new ObjectId(id));
                }
            }
        }

        query.append(Constant.ID, new BasicDBObject(Constant.MONGO_IN,objectIdList));

        BasicDBObject updateParam = new BasicDBObject();
        if (map.get("storageStatus") != null) {
            updateParam.append("storageStatus",map.get("storageStatus").toString());
        }
        if (map.get("comment") != null) {
            updateParam.append("comment",new ObjectId(map.get("comment").toString()));
        }
        if (map.get("useStatus") != null) {
            updateParam.append("useStatus",map.get("useStatus").toString());
        }
        if (map.get("isr") != null) {
            updateParam.append("isr",map.get("isr"));
        }
        if (map.get("commentType") != null) {
            updateParam.append("commentType",map.get("commentType").toString());
        }
        //对拼接的Id做处理
        List<String> needRepairCommentList = new ArrayList<String>();
        if (map.get("needRepairComments") != null){
            for (String needRepairComment : map.get("needRepairComments").toString().split(",")){
                if (needRepairComment != ""){
                    needRepairCommentList.add(needRepairComment);
                }
            }
            updateParam.append("needRepairComment",needRepairCommentList);
        }
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, updateParam);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_STORAGE_MANAGE,query,updateValue);
        return map.get("ids").toString();
    }

    public int findDataByImeiNo(String imeiNo) {
        //封装查询参数
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("imeiNo", imeiNo);
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_STORAGE_MANAGE, query);
        return count;
    }
}
