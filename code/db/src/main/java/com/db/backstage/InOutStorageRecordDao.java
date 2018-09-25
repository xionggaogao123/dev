package com.db.backstage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.backstage.InOutStorageEntry;
import com.pojo.backstage.StorageManageEntry;
import com.sys.constants.Constant;

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

        //封装查询统计所有参数
        BasicDBObject countAllquery = new BasicDBObject();
        countAllquery.append("isr", Constant.ZERO);
        countAllquery.append("storageRecordStatus", "5");

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
}
