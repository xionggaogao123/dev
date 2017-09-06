package com.db.wrongquestion;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.wrongquestion.NameConvertResNameEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by guojing on 2017/3/23.
 */
public class NameConvertResNameDao extends BaseDao {
    /**
     * 增加
     * @param e
     * @return
     */
    public ObjectId addNameConvertResNameEntry(NameConvertResNameEntry e)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NAME_CONVERT, e.getBaseEntry());
        return e.getID();
    }


    /**ResourceDictionaryController
     * 获取名称
     * @param name
     * @return
     */
    public NameConvertResNameEntry getNameConvertResNameEntry(String name) {
        BasicDBObject query =new BasicDBObject("nms", name).append("ir", Constant.ZERO);
        DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NAME_CONVERT, query, Constant.FIELDS);
        if(null!=dbo)
        {
            return new NameConvertResNameEntry((BasicDBObject)dbo);
        }
        return null;
    }
}
