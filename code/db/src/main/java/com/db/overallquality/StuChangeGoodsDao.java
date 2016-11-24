package com.db.overallquality;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.overallquality.ChangeState;
import com.pojo.overallquality.StuChangeGoodsEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2016/8/25.
 */
public class StuChangeGoodsDao extends BaseDao {
    /**
     * 添加
     * @param e
     * @return
     */
    public ObjectId addStuChangeGoodsEntry(StuChangeGoodsEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_STU_CHANGE_GOODS, e.getBaseEntry());
        return e.getID();
    }


    /**
     * 查询
     * @param classId
     * @return
     */
    public List<StuChangeGoodsEntry> searchStuChangeGoodsEntryByClassId(ObjectId classId, ChangeState state) {
        BasicDBObject query =new BasicDBObject("ci", classId).append("cs", state.getState());
        BasicDBObject sort =new BasicDBObject(Constant.ID,Constant.DESC);
        List<DBObject> dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_STU_CHANGE_GOODS, query, Constant.FIELDS, sort);
        List<StuChangeGoodsEntry> list=new ArrayList<StuChangeGoodsEntry>();
        if(null!=dbos && !dbos.isEmpty()) {
            for (DBObject dbo : dbos) {
                list.add(new StuChangeGoodsEntry((BasicDBObject) dbo));
            }
        }
        return list;
    }

    /**
     * 修改
     * @param e
     * @return
     */
    public void updateStuChangeGoodsEntry(StuChangeGoodsEntry e) {
        BasicDBObject query =new BasicDBObject(Constant.ID, e.getID());
        BasicDBObject updateValue =new BasicDBObject(Constant.MONGO_SET, e.getBaseEntry());
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_STU_CHANGE_GOODS, query, updateValue);
    }

    /**
     * 修改
     * @param ids
     * @return
     */
    public void batchHandleStuChangeGoods(List<ObjectId> ids, ObjectId userId, String refuseCon, int state, long handleTime) {
        DBObject query =new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN,ids));
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("cs", state).append("hui", userId).append("ht", handleTime).append("rc", refuseCon));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_STU_CHANGE_GOODS, query, updateValue);
    }

    /**
     * 查询
     * @param ids
     * @return
     */
    public List<StuChangeGoodsEntry> searchStuChangeGoodsEntryByIds(List<ObjectId> ids) {
        DBObject query =new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN,ids)).append("cs", ChangeState.AUDIT.getState());
        List<DBObject> dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_STU_CHANGE_GOODS, query, Constant.FIELDS);
        List<StuChangeGoodsEntry> list=new ArrayList<StuChangeGoodsEntry>();
        if(null!=dbos && !dbos.isEmpty()) {
            for (DBObject dbo : dbos) {
                list.add(new StuChangeGoodsEntry((BasicDBObject) dbo));
            }
        }
        return list;
    }


    public List<StuChangeGoodsEntry> searchStuChangeGoodsEntryByParam(ObjectId schoolId, ObjectId userId, int userRole) {
        BasicDBObject query =new BasicDBObject();
        if(UserRole.isHeadmaster(userRole)){
            query.append("si", schoolId);
        }else if(UserRole.isStudentOrParent(userRole)){
            query.append("ui",userId);
        }else{
            query.append("hui",userId);
        }
        BasicDBObject sort =new BasicDBObject(Constant.ID,Constant.DESC);
        List<DBObject> dbos = find(MongoFacroty.getAppDB(), Constant.COLLECTION_STU_CHANGE_GOODS, query, Constant.FIELDS, sort);
        List<StuChangeGoodsEntry> list=new ArrayList<StuChangeGoodsEntry>();
        if(null!=dbos && !dbos.isEmpty()) {
            for (DBObject dbo : dbos) {
                list.add(new StuChangeGoodsEntry((BasicDBObject) dbo));
            }
        }
        return list;
    }
}
