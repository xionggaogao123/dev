package com.db.forum;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.forum.FSignEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by wangkaidong on 2016/5/30.
 *
 * 签到DAO
 *
 */
public class FSignDao extends BaseDao{

    /**
     * 新增&更新签到
     * */
    public ObjectId saveOrUpdate(FSignEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_COURSE_TABLE,entry.getBaseEntry());
        return entry.getID();
    }

    /**
     * 查询签到记录
     *
     * @param userId
     * */
    public FSignEntry findSignByUserId(ObjectId userId){
        DBObject dbObject = findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_FORUM_SIGN,new BasicDBObject("uid",userId),Constant.FIELDS);
        if(dbObject != null){
            return new FSignEntry((BasicDBObject)dbObject);
        } else {
            return null;
        }
    }


}
