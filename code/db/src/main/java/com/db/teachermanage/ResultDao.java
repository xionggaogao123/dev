package com.db.teachermanage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.teachermanage.ResultEntry;
import com.pojo.teachermanage.TitleEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2016/3/7.
 */
public class ResultDao extends BaseDao {
    /**
     * 增加成果信息
     *
     * @param e
     * @return
     */
    public ObjectId addResultEntry(ResultEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_RESULT, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 删除成果信息
     * @param userId
     */
    public void removeResultEntry(ObjectId userId) {
        DBObject query =new BasicDBObject("ui",userId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_RESULT, query);
    }

    /**
     * 获取成果信息
     * @param userId
     * @param fields
     * @return
     */
    public List<ResultEntry> getResultList(ObjectId userId,DBObject fields) {
        BasicDBObject query =new BasicDBObject("ui",userId);
        List<DBObject> list=find(MongoFacroty.getAppDB(), Constant.COLLECTION_RESULT, query, fields);
        List<ResultEntry> resultEntries=new ArrayList<ResultEntry>();
        for(DBObject dbObject:list){
            ResultEntry resumeEntry=new ResultEntry((BasicDBObject)dbObject);
            resultEntries.add(resumeEntry);
        }
        return resultEntries;
    }
}
