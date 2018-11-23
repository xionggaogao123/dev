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

import java.text.SimpleDateFormat;
import java.util.*;

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

//        query.append("deliveryMethod", "快递");//送货上门也展示

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

//        query.append("deliveryMethod", "快递");//送货上门也展示
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

    public String addRepairManage(InOutStorageEntry inOutStorageEntry) {
        save(MongoFacroty.getAppDB(),Constant.COLLECTION_PHONES_IN_OUT_STORAGE_RECORD,inOutStorageEntry.getBaseEntry());
        return inOutStorageEntry.getID().toString();
    }

    public Map<String,Object> getRepairManageList(int page, int pageSize, String inputParams, String year, String month, int isr) {
        //封装查询参数
        BasicDBObject query = new BasicDBObject();
        query.append("isr", isr);

        if (!"".equals(year)) {
            query.append("inStorageYear", year);
        }
        if (!"".equals(month)) {
            query.append("inStorageMonth", month);
        }
        if (!"".equals(inputParams)) {
            BasicDBList values = new BasicDBList();
            BasicDBObject query1 = new BasicDBObject().append("imeiNo", inputParams);
            BasicDBObject query2 = new BasicDBObject().append("parentId", inputParams);
            BasicDBObject query3 = new BasicDBObject().append("parentName", inputParams);
            values.add(query1);
            values.add(query2);
            values.add(query3);
            query.put(Constant.MONGO_OR, values);
        }
        //待维修（isr 0 待处理 isr 1 已完成）
        query.append("storageRecordStatus", "6");
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_IN_OUT_STORAGE_RECORD, query, Constant.FIELDS,
                new BasicDBObject("inStorageTime", Constant.DESC),(page-1)*pageSize,pageSize);
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
     * 维修管理-修改
     * @param map
     * @return
     */
    public String updateRepairManage(Map map) {
        BasicDBObject query = new BasicDBObject();
        query.append(Constant.ID, new ObjectId(map.get("id").toString()));

        BasicDBObject updateParam = new BasicDBObject();
        if (map.get("repairType") != null) {
            updateParam.append("repairType",map.get("repairType").toString());
        }
        //对拼接的维修范围做处理
        List<String> needRepairCommentList = new ArrayList<String>();
        if (map.get("repairCommentList") != null){
            for (String needRepairComment : map.get("repairCommentList").toString().split(",")){
                if (needRepairComment != ""){
                    needRepairCommentList.add(needRepairComment);
                }
            }
            updateParam.append("needRepairComment",needRepairCommentList);
        }
        if (map.get("repairCommentList") != null) {
            updateParam.append("repairCommentList",map.get("repairCommentList").toString());
        }

        if (map.get("repairCost") != null) {
            updateParam.append("repairCost",map.get("repairCost").toString());
        }

        if (map.get("isPay") != null) {
            updateParam.append("isPay",map.get("isPay").toString());
        }
        if (map.get("payFrom") != null) {
            updateParam.append("payFrom",map.get("payFrom").toString());
        }
        if (map.get("afterRepair") != null) {
            updateParam.append("afterRepair",map.get("afterRepair").toString());
        }
        if (map.get("isr") != null) {
            updateParam.append("isr",Integer.parseInt(map.get("isr").toString()));
            //维修完成 添加入库时间
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            String inStorageTime = dateFormat.format(new Date());
            String inStorageYear = calendar.get(Calendar.YEAR)+"";
            String inStorageMonth = (calendar.get(Calendar.MONTH)+1)+"";
            updateParam.append("inStorageTime",inStorageTime);
            updateParam.append("inStorageYear",inStorageYear);
            updateParam.append("inStorageMonth",inStorageMonth);
        }



        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, updateParam);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_IN_OUT_STORAGE_RECORD,query,updateValue);

        return map.get("id").toString();
    }

    /**
     * 新增单个记录
     * @param inOutStorageEntry
     * @return
     */
    public String addProjectOutStorageRecord(InOutStorageEntry inOutStorageEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_IN_OUT_STORAGE_RECORD, inOutStorageEntry.getBaseEntry());
        return inOutStorageEntry.getID().toString();
    }

    /**
     * 出库跟踪-按项目查找
     * @param page
     * @param pageSize
     * @param inputParams
     * @param projectId
     * @return
     */
    public Map<String, Object> getOutStorageListByProject(int page, int pageSize, String inputParams, String projectId) {
        Map<String, Object> result = new HashMap<String, Object>();
        //封装查询参数
        BasicDBObject query = new BasicDBObject();
        query.append("isr",0);
        query.append("storageRecordStatus","5");//出库
        query.append("imeiNo",new BasicDBObject(Constant.MONGO_NE, ""));//imeiNo 不为空
        if (!"".equals(inputParams)) {
            BasicDBList values = new BasicDBList();
            BasicDBObject query1 = new BasicDBObject().append("imeiNo", inputParams);
            BasicDBObject query2 = new BasicDBObject().append("parentId", inputParams);
            BasicDBObject query3 = new BasicDBObject().append("parentName", inputParams);
            values.add(query1);
            values.add(query2);
            values.add(query3);
            query.put(Constant.MONGO_OR, values);
        }
        if (!"".equals(projectId)){
            query.append("projectId",projectId);
        }
//        query.append("excompanyNo", new BasicDBObject(Constant.MONGO_NE,""));
        //isr 为1 已回收
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_IN_OUT_STORAGE_RECORD, query, Constant.FIELDS,
                new BasicDBObject("inStorageTime", Constant.DESC),(page-1)*pageSize,pageSize);
        List<InOutStorageEntry> inOutStorageEntryList = new ArrayList<InOutStorageEntry>();
        for (DBObject dbObject : dbObjects){
            inOutStorageEntryList.add(new InOutStorageEntry(dbObject));
        }
        result.put("entryList",inOutStorageEntryList);
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_IN_OUT_STORAGE_RECORD, query);
        result.put("count",count);
        return result;
    }

    /**
     * 出库跟踪-手机IMEI查验，如果未在出库状态则跳过，并告知前台
     * @param imeiNo
     * @return
     */
    public int findOutStorageByImeiNo(String imeiNo) {
        Map<String, Object> result = new HashMap<String, Object>();
        //封装查询参数
        BasicDBObject query = new BasicDBObject();
        query.append("imeiNo",imeiNo);
        query.append("storageRecordStatus","5");//出库
        query.append("isr", 0);//未回收
        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_IN_OUT_STORAGE_RECORD, query);
        return count;
    }

    /**
     * 出库跟踪-更新用户信息
     * @param imeiNo
     * @param parentName
     * @param parentId
     * @param parentMobile
     * @param studentName
     * @param studentId
     * @param studentMobile
     * @param schoolName
     * @param accessClass
     * @param address
     */
    public void updateOutOutStorageByImeiNo(String imeiNo, String parentName, String parentId, String parentMobile,
                                            String studentName, String studentId, String studentMobile,
                                            String schoolName, String accessClass, String address) {
        Map<String, Object> result = new HashMap<String, Object>();
        //封装查询参数
        BasicDBObject query = new BasicDBObject();
        query.append("imeiNo",imeiNo);
        query.append("storageRecordStatus","5");//出库
        query.append("isr", 0);//未回收

        BasicDBObject updateParam = new BasicDBObject();
        if (!"".equals(parentName)){
            updateParam.append("parentName",parentName);
        }
        if (!"".equals(parentId)){
            updateParam.append("parentId",parentId);
        }
        if (!"".equals(parentMobile)){
            updateParam.append("parentMobile",parentMobile);
        }
        if (!"".equals(studentName)){
            updateParam.append("studentName",studentName);
        }
        if (!"".equals(studentId)){
            updateParam.append("studentId",studentId);
        }
        if (!"".equals(studentMobile)){
            updateParam.append("studentMobile",studentMobile);
        }
        if (!"".equals(schoolName)){
            updateParam.append("schoolName",schoolName);
        }
        if (!"".equals(accessClass)){
            updateParam.append("accessClass",accessClass);
        }
        if (!"".equals(address)){
            updateParam.append("address",address);
        }
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, updateParam);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_IN_OUT_STORAGE_RECORD,query,updateValue);
    }

    /**
     * 出库跟踪-单个回收
     * 更改选中数据为已回收 即isr 为 1
     * @param id
     */
    public void singleRecycleInStorageById(String id) {
        //封装查询参数
        BasicDBObject query = new BasicDBObject();
        query.append(Constant.ID,new ObjectId(id));

        BasicDBObject updateParam = new BasicDBObject();
        updateParam.append("isr",1);

        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, updateParam);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_IN_OUT_STORAGE_RECORD,query,updateValue);
    }

    /**
     * 出库跟踪-批量回收
     * 更改选中数据为已回收 即isr 为 1
     * @param ids
     */
    public void batchRecycleInStorageByIds(String ids) {
        //封装查询参数
        List<ObjectId> objectIds = new ArrayList<ObjectId>();
        if (ids != ""){
            for (String id : ids.split(",")){
                if (id != ""){
                    objectIds.add(new ObjectId(id));
                }
            }
        }

        BasicDBObject query = new BasicDBObject();
        query.append(Constant.ID,new BasicDBObject(Constant.MONGO_IN, objectIds));

        BasicDBObject updateParam = new BasicDBObject();
        updateParam.append("isr",1);

        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, updateParam);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_IN_OUT_STORAGE_RECORD,query,updateValue);
    }

    /**
     * 出库跟踪-按手机查找
     * 手机列表展示
     * @param page
     * @param pageSize
     * @param inputParams
     * @param year
     * @param month
     * @param imeiNo//用来查详情的入参
     * @return
     */
    public Map<String,Object> getOutStorageListByPhone(int page, int pageSize, String inputParams, String year, String month, String imeiNo) {
        //封装查询参数
        BasicDBObject query = new BasicDBObject();
        query.append("imeiNo",new BasicDBObject(Constant.MONGO_NE, ""));//imeiNo 不为空
//        query.append("isr", Constant.ZERO);
        if (!"".equals(imeiNo)){
            query.append("imeiNo", imeiNo);
        }
        if (!"".equals(year)) {
            query.append("creationYear", year);
        }
        if (!"".equals(month)) {
            query.append("creationMonth", month);
        }

        if (!"".equals(inputParams)) {
            BasicDBList values = new BasicDBList();
            BasicDBObject query1 = new BasicDBObject().append("imeiNo", inputParams);
            BasicDBObject query2 = new BasicDBObject().append("parentId", inputParams);
            BasicDBObject query3 = new BasicDBObject().append("parentName", inputParams);
            values.add(query1);
            values.add(query2);
            values.add(query3);
            query.put(Constant.MONGO_OR, values);
        }

        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_IN_OUT_STORAGE_RECORD, query, Constant.FIELDS,
                new BasicDBObject("creationTime", Constant.ASC)/*,(page-1)*pageSize,pageSize*/);
        List<InOutStorageEntry> inOutStorageEntries = new ArrayList<InOutStorageEntry>();
        for (DBObject dbObject : dbObjects){
            inOutStorageEntries.add(new InOutStorageEntry(dbObject));
        }
        Map<String,Object> result = new HashMap<String, Object>();
        result.put("entryList",inOutStorageEntries);
/*        int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_IN_OUT_STORAGE_RECORD, query);
        result.put("count",count);*/
        return result;
    }

    public Map<String,Object> getReadInfoList(String storageRecordStatus) {
        //封装查询参数
        BasicDBObject query = new BasicDBObject();
        query.append("isr", Constant.ZERO);


        if ("5".equals(storageRecordStatus)){
            //出库
            query.append("storageRecordStatus", "5");
//            query.append("deliveryMethod", "快递");//送货上门也展示
            //发货状态
            List<String> stringList = new ArrayList<String>();
            stringList.add("");
            query.append("excompanyNo", new BasicDBObject(Constant.MONGO_IN,stringList));
        }else {
            //维修
            query.append("storageRecordStatus", "6");
        }
        //未读
        query.append("isReadFlag", "0");

        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_IN_OUT_STORAGE_RECORD, query);
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
     * 根据唯一索引集合更新未读数据
     * @param map
     * @return
     */
    public String updateReadyReadByIds(Map map) {
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
        updateParam.append("isReadFlag", "1");


        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, updateParam);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_IN_OUT_STORAGE_RECORD,query,updateValue);
        return map.get("ids").toString();
    }

    public InOutStorageEntry getInOutStorageEntryById(ObjectId id) {
        //封装查询参数
        BasicDBObject query = new BasicDBObject();
        query.append(Constant.ID, id);

        DBObject dbObject = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_IN_OUT_STORAGE_RECORD, query);
        InOutStorageEntry inOutStorageEntry = new InOutStorageEntry();
        if (dbObject != null){
            inOutStorageEntry = new InOutStorageEntry(dbObject);
        }
        return inOutStorageEntry;
    }

    /**
     * 出库跟踪-注册绑定IMEI和账号
     * @param mobile
     * @param imeiNo
     * @return
     */
    public void updateOutStorageFollowUserInfo(String mobile, String imeiNo ,String studentId) {
        //封装查询参数
        BasicDBObject query = new BasicDBObject();
        query.append("isr",0);
        query.append("storageRecordStatus","5");//出库
//        query.append("imeiNo",new BasicDBObject(Constant.MONGO_NE, ""));//imeiNo 不为空
        query.append("imeiNo", imeiNo);

        BasicDBObject updateParam = new BasicDBObject();
        updateParam.append("studentMobile", mobile);
        updateParam.append("studentName", mobile);//初次注册 手机号是姓名
        updateParam.append("studentId", studentId);

        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, updateParam);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_PHONES_IN_OUT_STORAGE_RECORD,query,updateValue);
    }
}
