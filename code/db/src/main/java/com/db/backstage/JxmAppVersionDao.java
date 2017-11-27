package com.db.backstage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.backstage.JxmAppVersionEntry;
import com.sys.constants.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/11/27.
 */
public class JxmAppVersionDao extends BaseDao {

    //添加
    public String addEntry(JxmAppVersionEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_APP_VERSION, entry.getBaseEntry());
        return entry.getID().toString() ;
    }
    //用户的所有课程列表
    public List<JxmAppVersionEntry> getIsNewObjectId() {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_JXM_APP_VERSION, query, Constant.FIELDS);
        List<JxmAppVersionEntry> retList =new ArrayList<JxmAppVersionEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new JxmAppVersionEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }
}
