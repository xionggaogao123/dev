package com.db.operation;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.appnotice.AppNoticeEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/9/22.
 */
public class AppNoticeDao extends BaseDao{

    /**
     * 保存
     * @param entry
     */
    public void saveAppNoticeEntry(AppNoticeEntry entry){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,entry.getBaseEntry());
    }

    /**
     * 保存几条信息
     * @param entries
     */
    public void saveAppNoticeEntries(List<AppNoticeEntry> entries){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE, MongoUtils.fetchDBObjectList(entries));
    }


    /**
     * 查询我已发送的通知
     * @param userId
     * @return
     */
    public List<AppNoticeEntry> getMySendAppNoticeEntries(ObjectId userId,int page,int pageSize){
        List<AppNoticeEntry> entries=new ArrayList<AppNoticeEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("uid",userId)
                .append("ir",Constant.ZERO);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppNoticeEntry(dbObject));
            }
        }
        return entries;
    }


    /**
     * 获取我接收到的通知
     * @param groupIds
     * @return
     */
    public List<AppNoticeEntry> getMyReceivedAppNoticeEntries(List<ObjectId> groupIds,int page,int pageSize,
            ObjectId userId){
        List<AppNoticeEntry> entries=new ArrayList<AppNoticeEntry>();
        BasicDBObject query=new BasicDBObject()
                .append("gi",new BasicDBObject(Constant.MONGO_IN,groupIds))
                .append("uid",new BasicDBObject(Constant.MONGO_NE,userId))
                .append("ir",Constant.ZERO);
        List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,query,
                Constant.FIELDS,Constant.MONGO_SORTBY_DESC,(page-1)*pageSize,pageSize);
        if(null!=dbObjectList&&!dbObjectList.isEmpty()){
            for(DBObject dbObject:dbObjectList){
                entries.add(new AppNoticeEntry(dbObject));
            }
        }
        return entries;
    }


    /**
     * 已阅读
     * @param userId
     * @param id
     */
    public void pushReadList(ObjectId userId,ObjectId id){
        BasicDBObject query=new BasicDBObject()
                .append(Constant.ID,id);
        BasicDBObject updateValue=new BasicDBObject(Constant.MONGO_PUSH,new BasicDBObject("rl",userId));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,query,updateValue);
    }


    /**
     * 查询entry
     * @param id
     */
    public AppNoticeEntry getAppNoticeEntry(ObjectId id){
        BasicDBObject query=new BasicDBObject()
                .append(Constant.ID,id);
        DBObject dbObject=findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_NEW_VERSION_APP_NOTICE,query,Constant.FIELDS);
        if(null!=dbObject){
            return new AppNoticeEntry(dbObject);
        }else{
            return null;
        }
    }


}
