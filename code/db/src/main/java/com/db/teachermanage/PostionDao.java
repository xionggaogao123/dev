package com.db.teachermanage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.teachermanage.JobEntry;
import com.pojo.teachermanage.PostionEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2016/3/7.
 */
public class PostionDao extends BaseDao {

    /**
     * 增加行政职务*
     *
     * @param e
     * @return
     */
    public ObjectId addPostionEntry(PostionEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_POSTION, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 删除行政职务*
     *
     * @param userId
     */
    public void removePostionEntry(ObjectId userId) {
        DBObject query = new BasicDBObject("ui", userId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_POSTION, query);
    }

    /**
     * 获取行政职务*
     *
     * @param userId
     * @param fields
     * @return
     */
    public List<PostionEntry> getPostionList(ObjectId userId, DBObject fields) {
        BasicDBObject query = new BasicDBObject("ui", userId);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_POSTION, query, fields);
        List<PostionEntry> postionEntryList = new ArrayList<PostionEntry>();
        for (DBObject dbObject : list) {
            PostionEntry postionEntry = new PostionEntry((BasicDBObject) dbObject);
            postionEntryList.add(postionEntry);
        }
        return postionEntryList;
    }
}
