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
                storageStatusList.add("2");
                query.append("storageStatus", new BasicDBObject(Constant.MONGO_IN,storageStatusList));
            }

        }else{
            //出库不展示
            List<String> outStorageStatusList = new ArrayList<String>();
            outStorageStatusList.add("5");
            //待维修不展示
            outStorageStatusList.add("6");
            query.append("storageStatus", new BasicDBObject(Constant.MONGO_NOTIN,outStorageStatusList));
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
        //待维修不展示
        outStorageStatusList.add("6");//待维修
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
        //对拼接的维修范围做处理
        List<String> needRepairCommentList = new ArrayList<String>();
        if (map.get("needRepairComments") != null){
            for (String needRepairComment : map.get("needRepairComments").toString().split(",")){
                if (needRepairComment != ""){
                    needRepairCommentList.add(needRepairComment);
                }
            }
            updateParam.append("needRepairComment",needRepairCommentList);
            //需要维修的手机 状态改成待维修 6
            updateParam.append("storageStatus","6");
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
            //需要维修的手机 状态改成待维修 6
            updateParam.append("storageStatus","6");
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

    /**
     * 发货管理-获取当前型号库存手机颜色
     * @return
     */
    public List<String> getCurrentModelColor(String phoneModel) {
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("phoneModel", phoneModel);
        //可用
        query.append("useStatus", "1");
        //出库不展示
        List<String> outStorageStatusList = new ArrayList<String>();
        outStorageStatusList.add("5");
        query.append("storageStatus", new BasicDBObject(Constant.MONGO_NOTIN,outStorageStatusList));

        //查询
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_STORAGE_MANAGE, query, Constant.FIELDS);
        List<String> stringList = new ArrayList<String>();
        for (DBObject dbObject : dbObjects){
            if (!stringList.contains(new StorageManageEntry(dbObject).getColor())){
                stringList.add(new StorageManageEntry(dbObject).getColor());
            }
        }
        return stringList;
    }

    /**
     * 发货管理-配置手机检测可用
     * @return
     */
    public int checkPhoneInfoValid(String imeiNo, String color) {
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("imeiNo", imeiNo);
        query.append("color", color);
        //可用
        query.append("useStatus", "1");
        //出库不展示
        List<String> outStorageStatusList = new ArrayList<String>();
        outStorageStatusList.add("5");
        query.append("storageStatus", new BasicDBObject(Constant.MONGO_NOTIN,outStorageStatusList));

        //查询
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_STORAGE_MANAGE, query);
        return count;
    }

    /**
     * 根据imeiNo 更新出库状态
     * @param map
     * @return
     */
    public String updateStorageInfoByImeiNo(Map map) {
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("imeiNo", map.get("imeiNo").toString());
        //可用
        query.append("useStatus", "1");
        //出库不展示
        List<String> outStorageStatusList = new ArrayList<String>();
        outStorageStatusList.add("5");
        query.append("storageStatus", new BasicDBObject(Constant.MONGO_NOTIN,outStorageStatusList));

        //更新内容
        BasicDBObject updateParam = new BasicDBObject();
        updateParam.append("storageStatus","5");

        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, updateParam);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_STORAGE_MANAGE,query,updateValue);

        return map.get("imeiNo").toString();
    }

    public List<String> getPhoneModel() {
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO);
//        //可用
//        query.append("useStatus", "1");
//        //出库不展示
//        List<String> outStorageStatusList = new ArrayList<String>();
//        outStorageStatusList.add("5");
//        query.append("storageStatus", new BasicDBObject(Constant.MONGO_NOTIN,outStorageStatusList));

        //查询
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_STORAGE_MANAGE, query, Constant.FIELDS);
        List<String> stringList = new ArrayList<String>();
        for (DBObject dbObject : dbObjects){
            if (!stringList.contains(new StorageManageEntry(dbObject).getPhoneModel())){
                stringList.add(new StorageManageEntry(dbObject).getPhoneModel());
            }
        }
        return stringList;
    }

    /**
     * 维修入库
     * @param imeiNo
     * @param storageStatus
     * @param useStatus
     * @param inStorageTime
     * @param inStorageYear
     * @param inStorageMonth
     * @return
     */
    public String updateStorageInfoByImeiNo(String imeiNo, String storageStatus, String useStatus, String inStorageTime, String inStorageYear, String inStorageMonth) {
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("imeiNo", imeiNo);

        //更新内容
        BasicDBObject updateParam = new BasicDBObject();
        updateParam.append("storageStatus",storageStatus);
        updateParam.append("useStatus",useStatus);
        updateParam.append("inStorageTime",inStorageTime);
        updateParam.append("inStorageYear",inStorageYear);
        updateParam.append("inStorageMonth",inStorageMonth);

        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, updateParam);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_STORAGE_MANAGE,query,updateValue);

        return imeiNo;
    }

    /**
     * 批量维修入库
     * @param imeiNoList
     * @param storageStatus
     * @param useStatus
     * @param inStorageTime
     * @param inStorageYear
     * @param inStorageMonth
     */
    public void updateStorageInfoByImeiNoList(List<String> imeiNoList, String storageStatus, String useStatus, String inStorageTime, String inStorageYear, String inStorageMonth) {
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("imeiNo", new BasicDBObject(Constant.MONGO_IN, imeiNoList));

        //更新内容
        BasicDBObject updateParam = new BasicDBObject();
        updateParam.append("storageStatus",storageStatus);
        updateParam.append("useStatus",useStatus);
        updateParam.append("inStorageTime",inStorageTime);
        updateParam.append("inStorageYear",inStorageYear);
        updateParam.append("inStorageMonth",inStorageMonth);

        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, updateParam);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_STORAGE_MANAGE,query,updateValue);
    }

    /**
     * 维修管理-获取当前型号库存手机颜色
     * @return
     */
    public List<String> getCurrentModelColorForRepair(String phoneModel) {
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("phoneModel", phoneModel);
//        //可用
//        query.append("useStatus", "1");
//        //出库不展示
//        List<String> outStorageStatusList = new ArrayList<String>();
//        outStorageStatusList.add("5");
//        query.append("storageStatus", new BasicDBObject(Constant.MONGO_NOTIN,outStorageStatusList));

        //查询
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_STORAGE_MANAGE, query, Constant.FIELDS);
        List<String> stringList = new ArrayList<String>();
        for (DBObject dbObject : dbObjects){
            if (!stringList.contains(new StorageManageEntry(dbObject).getColor())){
                stringList.add(new StorageManageEntry(dbObject).getColor());
            }
        }
        return stringList;
    }
}
