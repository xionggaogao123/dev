package com.db.teachermanage;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.teachermanage.CourseProjectEntry;
import com.pojo.teachermanage.ResumeEntry;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2016/3/7.
 */
public class ResumeDao extends BaseDao {

    /**
     * 增加老师个人信息
     *
     * @param e
     * @return
     */
    public ObjectId addResumeEntry(ResumeEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_RESUME, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 删除
     * @param userId
     */
    public void removeResumeEntry(ObjectId userId) {
        DBObject query =new BasicDBObject("ui",userId);
        remove(MongoFacroty.getAppDB(), Constant.COLLECTION_RESUME, query);
    }

    /**
     * 获取老师个人简历
     * @param userId
     * @param fields
     * @return
     */
    public ResumeEntry getResumeList(ObjectId userId,DBObject fields) {
        BasicDBObject query =new BasicDBObject("ui",userId);
        DBObject dbo=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_RESUME, query, fields);
        if (null != dbo) {
            ResumeEntry e = new ResumeEntry((BasicDBObject) dbo);
            return e;
        }
        return null;
    }
}
