package com.db.controlphone;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.controlphone.ControlMessageEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/11/15.
 */
public class ControlMessageDao extends BaseDao {
    //添加
    public String addEntry(ControlMessageEntry entry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_MESSAGE, entry.getBaseEntry());
        return entry.getID().toString() ;
    }

    //答题列表（分页）
    public List<ControlMessageEntry> getUserResultPageList(ObjectId parentId,ObjectId sonId,int page,int pageSize) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("pid",parentId);
        query.append("uid",sonId);
        List<DBObject> dboList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_CONTROL_MESSAGE, query, Constant.FIELDS,new BasicDBObject("ctm",Constant.ASC),(page - 1) * pageSize, pageSize);
        List<ControlMessageEntry> retList =new ArrayList<ControlMessageEntry>();
        if(null!=dboList && !dboList.isEmpty())
        {
            for(DBObject dbo:dboList)
            {
                retList.add(new ControlMessageEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }
    /**
     * 符合搜索条件的对象个数
     * @return
     */
    public int getNumber(ObjectId parentId,ObjectId sonId) {
        BasicDBObject query =new BasicDBObject();
        query.append("isr", Constant.ZERO);
        query.append("pid",parentId);
        query.append("uid",sonId);
        int count =
                count(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_CONTROL_MESSAGE,
                        query);
        return count;
    }
}
