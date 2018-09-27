package com.db.backstage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.backstage.InOutStorageEntry;
import com.pojo.backstage.StorageManageEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: taotao.chan
 * @Date: 2018/9/20 10:20
 * @Description:
 */
public class InOutStorageRecordDao extends BaseDao {
    public void addProjectOutStorageRecordList(List<DBObject> dbObjectList) {
        save(MongoFacroty.getAppDB(),Constant.COLLECTION_PHONES_IN_OUT_STORAGE_RECORD,dbObjectList);
    }

    public Map<String,Object> getOutStorageHistoryList(int page, int pageSize, String inputParams, String year, String month) {
        //封装查询参数
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO);

        //排除imeiNo为空的
        List<String> stringList = new ArrayList<String>();
        stringList.add("");

        //封装查询统计所有参数
        BasicDBObject countAllquery = new BasicDBObject();
        countAllquery.append("isr", Constant.ZERO);
        countAllquery.append("storageRecordStatus", "5");
        countAllquery.append("imeiNo", new BasicDBObject(Constant.MONGO_NOTIN,stringList));

        if (!"".equals(year)) {
            query.append("outStorageYear", year);
        }
        if (!"".equals(month)) {
            query.append("outStorageMonth", month);
        }
        if (!"".equals(inputParams)) {
            BasicDBList values = new BasicDBList();
//            values.add(new BasicDBObject()
//                    .append("imeiNo", inputParams)
//                    .append("projectName", inputParams)
//                    .append("parentName", inputParams));
            BasicDBObject query1 = new BasicDBObject().append("imeiNo", inputParams);
            BasicDBObject query2 = new BasicDBObject().append("projectName", inputParams);
            BasicDBObject query3 = new BasicDBObject().append("parentName", inputParams);
            values.add(query1);
            values.add(query2);
            values.add(query3);
            query.put(Constant.MONGO_OR, values);
        }
        //展示出库
        query.append("storageRecordStatus", "5");
        query.append("imeiNo", new BasicDBObject(Constant.MONGO_NOTIN,stringList));
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_IN_OUT_STORAGE_RECORD, query, Constant.FIELDS,
                new BasicDBObject("outStorageTime", Constant.DESC),(page-1)*pageSize,pageSize);
        List<InOutStorageEntry> inOutStorageEntryList = new ArrayList<InOutStorageEntry>();
        for (DBObject dbObject : dbObjects){
            inOutStorageEntryList.add(new InOutStorageEntry(dbObject));
        }
        Map<String,Object> result = new HashMap<String, Object>();
        result.put("entryList",inOutStorageEntryList);
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_IN_OUT_STORAGE_RECORD, query);
        result.put("count",count);
        int countAll = count(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_IN_OUT_STORAGE_RECORD, countAllquery);
        result.put("countAll",countAll);
        return result;
    }

    public List<InOutStorageEntry> getDeliveryOptionList(String flag) {
        //封装查询参数
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO);
        //出库
        query.append("storageRecordStatus", "5");

        query.append("deliveryMethod", "快递");

        List<String> stringList = new ArrayList<String>();
        stringList.add("");
        if ("will" == flag){
            query.append("excompanyNo", new BasicDBObject(Constant.MONGO_IN,stringList));
        }else{
            query.append("excompanyNo", new BasicDBObject(Constant.MONGO_NOTIN,stringList));
        }

        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_IN_OUT_STORAGE_RECORD, query, Constant.FIELDS);
        List<InOutStorageEntry> inOutStorageEntryList = new ArrayList<InOutStorageEntry>();
        for (DBObject dbObject : dbObjects){
            inOutStorageEntryList.add(new InOutStorageEntry(dbObject));
        }
        return inOutStorageEntryList;
    }

    /**
     * 发货管理列表信息
     * @param page
     * @param pageSize
     * @param inputParams
     * @param year
     * @param month
     * @param deliveryFlag
     * @return
     */
    public Map<String,Object> getDeliveryInfoList(int page, int pageSize, String inputParams, String year, String month, String deliveryFlag) {
        //封装查询参数
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO);

        if (!"".equals(year)) {
            query.append("outStorageYear", year);
        }
        if (!"".equals(month)) {
            query.append("outStorageMonth", month);
        }
        if (!"".equals(inputParams)) {
            BasicDBList values = new BasicDBList();
            BasicDBObject query1 = new BasicDBObject().append("imeiNo", inputParams);
            BasicDBObject query2 = new BasicDBObject().append("projectName", inputParams);
            BasicDBObject query3 = new BasicDBObject().append("parentName", inputParams);
            values.add(query1);
            values.add(query2);
            values.add(query3);
            query.put(Constant.MONGO_OR, values);
        }
        //出库
        query.append("storageRecordStatus", "5");

        query.append("deliveryMethod", "快递");
        //发货状态
        List<String> stringList = new ArrayList<String>();
        stringList.add("");
        if ("will".equals(deliveryFlag)){
            query.append("excompanyNo", new BasicDBObject(Constant.MONGO_IN,stringList));
        }else{
            query.append("excompanyNo", new BasicDBObject(Constant.MONGO_NOTIN,stringList));
        }
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_IN_OUT_STORAGE_RECORD, query, Constant.FIELDS,
                new BasicDBObject("outStorageTime", Constant.DESC),(page-1)*pageSize,pageSize);
        List<InOutStorageEntry> inOutStorageEntryList = new ArrayList<InOutStorageEntry>();
        for (DBObject dbObject : dbObjects){
            inOutStorageEntryList.add(new InOutStorageEntry(dbObject));
        }
        Map<String,Object> result = new HashMap<String, Object>();
        result.put("entryList",inOutStorageEntryList);
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_IN_OUT_STORAGE_RECORD, query);
        result.put("count",count);
        return result;
    }

    /**
     * 根据唯一索引更新物流信息
     * @param map
     * @return
     */
    public String updateDeliveryLogisticsInfoById(Map map) {
        BasicDBObject query = new BasicDBObject();
        query.append(Constant.ID, new ObjectId(map.get("id").toString()));

        BasicDBObject updateParam = new BasicDBObject();
        if (map.get("excompanyNo") != null) {
            updateParam.append("excompanyNo",map.get("excompanyNo").toString());
        }
        if (map.get("expressNo") != null) {
            updateParam.append("expressNo",map.get("expressNo").toString());
        }

        if (map.get("imeiNo") != null) {
            updateParam.append("imeiNo",map.get("imeiNo").toString());
        }

        if (map.get("color") != null) {
            updateParam.append("color",map.get("color").toString());
        }

        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, updateParam);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_IN_OUT_STORAGE_RECORD,query,updateValue);
        return map.get("id").toString();
    }

    /**
     * 根据唯一索引集合更新物流信息
     * @param map
     * @return
     */
    public String updateDeliveryLogisticsInfoByIds(Map map) {
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
        if (map.get("excompanyNo") != null) {
            updateParam.append("excompanyNo",map.get("excompanyNo").toString());
        }
        if (map.get("expressNo") != null) {
            updateParam.append("expressNo",map.get("expressNo").toString());
        }

        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, updateParam);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_IN_OUT_STORAGE_RECORD,query,updateValue);
        return map.get("ids").toString();

    }
}
