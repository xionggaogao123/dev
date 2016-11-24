package com.db.zouban;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.zouban.ZoubanState;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by qiangm on 2015/10/14.
 */
public class ZoubanStateDao extends BaseDao{
    /**
     * 添加状态
     * @param zoubanStat
     */
    public void addState(ZoubanState zoubanStat)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_ZOUBAN_STATE,zoubanStat.getBaseEntry());
    }

    /**
     * 根据学校id、年级id以及学期查询当前状态
     * @param gradeId
     * @param term
     * @return
     */
    public ZoubanState findZoubanState(ObjectId gradeId,String term)
    {
        BasicDBObject query=new BasicDBObject();
        query.append("gid",gradeId);
        query.append("te",term);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_ZOUBAN_STATE,query,Constant.FIELDS);
        if(dbObject!=null)
        {
            return new ZoubanState((BasicDBObject) dbObject);
        }
        return null;
    }

    /**
     * 设置走班选课进度
     * @param gradeId
     * @param term
     * @param state
     */
    public void setGradeState(ObjectId gradeId,String term,int state)
    {
        BasicDBObject query=new BasicDBObject()
                .append("te",term)
                .append("gid",gradeId);
        BasicDBObject updateValue=new BasicDBObject()
                .append(Constant.MONGO_SET,new BasicDBObject("st",state));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_ZOUBAN_STATE,query,updateValue);
    }

    /**
     * 设置走班选课进度2
     * @param gradeId
     * @param term
     * @param state
     */
    public void setGradeSubState(ObjectId gradeId,String term,int state)
    {
        BasicDBObject query=new BasicDBObject()
                .append("te",term)
                .append("gid",gradeId);
        BasicDBObject updateValue=new BasicDBObject()
                .append(Constant.MONGO_SET,new BasicDBObject("st2",state));
        update(MongoFacroty.getAppDB(),Constant.COLLECTION_ZOUBAN_STATE,query,updateValue);
    }
}
