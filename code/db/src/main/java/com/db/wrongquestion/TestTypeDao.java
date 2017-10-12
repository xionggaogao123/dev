package com.db.wrongquestion;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.wrongquestion.TestTypeEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2017/10/12.
 */
public class TestTypeDao extends BaseDao {
    /**
     * 增加
     * @param e
     * @return
     */
    public ObjectId addTestTypeEntry(TestTypeEntry e)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_TEST_TYPE, e.getBaseEntry());
        return e.getID();
    }
    /**
     * 根据类型查找
     */
    public List<TestTypeEntry> getList(String ename){
        BasicDBObject query = new BasicDBObject()
                .append("ena",ename)
                .append("isr", 0); // 未删除
        List<DBObject> dbList =
                find(MongoFacroty.getAppDB(),
                        Constant.COLLECTION_TEST_TYPE,
                        query, Constant.FIELDS,
                        Constant.MONGO_SORTBY_ASC);
        List<TestTypeEntry> entryList = new ArrayList<TestTypeEntry>();
        if (dbList != null && !dbList.isEmpty()) {
            for (DBObject obj : dbList) {
                entryList.add(new TestTypeEntry((BasicDBObject) obj));
            }
        }
        return entryList;

    }
}
