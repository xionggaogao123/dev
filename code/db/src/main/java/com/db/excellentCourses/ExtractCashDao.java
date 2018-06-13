package com.db.excellentCourses;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.excellentCourses.ExtractCashEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-06-06.
 */
public class ExtractCashDao extends BaseDao {

    //添加提现申请
    public String addEntry(ExtractCashEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_EXTRACT_CASH, entry.getBaseEntry());
        return entry.getID().toString();
    }

    //查询
    public ExtractCashEntry getEntry(ObjectId id) {
        BasicDBObject query = new BasicDBObject();
        query.append("isr",Constant.ZERO);
        query.append(Constant.ID, id);
        DBObject obj =
                findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_EXTRACT_CASH, query, Constant.FIELDS);
        if (obj != null) {
            return new ExtractCashEntry((BasicDBObject) obj);
        }
        return null;
    }

    public List<ExtractCashEntry> getAllMemberBySchoolId(int type,String jiaId,int page,int pageSize) {
        BasicDBObject query = new BasicDBObject()
                .append("isr", 0); // 未删除
        if(jiaId!=null&& !jiaId.equals("")){
            query.append("jid",jiaId);
        }
        query.append("typ",type);
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_EXTRACT_CASH,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_DESC,(page - 1) * pageSize, pageSize);
        List<ExtractCashEntry> entryList = new ArrayList<ExtractCashEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new ExtractCashEntry((BasicDBObject) obj));
            }
        }
        return entryList;
    }

    public int countAllMemberBySchoolId(int type,String jiaId){
        BasicDBObject query = new BasicDBObject()
                .append("isr", 0); // 未删除
        if(jiaId!=null&& !jiaId.equals("")){
            query.append("jid",jiaId);
        }
        query.append("typ",type);
        int count = count(MongoFacroty.getAppDB(),Constant.COLLECTION_EXTRACT_CASH,query);
        return count;
    }
}
