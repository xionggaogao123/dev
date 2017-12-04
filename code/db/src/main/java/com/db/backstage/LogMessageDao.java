package com.db.backstage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.backstage.LogMessageEntry;
import com.sys.constants.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/12/4.
 */
public class LogMessageDao extends BaseDao {
    //添加
    public String addEntry(LogMessageEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_LOG_MESSAGE, entry.getBaseEntry());
        return entry.getID().toString() ;
    }

    public List<LogMessageEntry> selectContentList(int page,int pageSize) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_LOG_MESSAGE, query, Constant.FIELDS,new BasicDBObject("ctm",Constant.DESC),(page - 1) * pageSize, pageSize);
        List<LogMessageEntry> retList =new ArrayList<LogMessageEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new LogMessageEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }

    /**
     * 符合搜索条件的对象个数
     * @return
     */
    public int getNumber() {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_LOG_MESSAGE,
                        query);
        return count;
    }


}
