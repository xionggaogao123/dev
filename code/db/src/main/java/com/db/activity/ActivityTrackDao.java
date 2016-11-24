package com.db.activity;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.activity.ActTrackEntry;
import com.pojo.activity.enums.ActTrackDevice;
import com.pojo.activity.enums.ActTrackType;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yan on 2015/3/4.
 *  index:rid_uid_att
 *       {"rid":1,"uid":1,"att":1}
 */
public class ActivityTrackDao extends BaseDao {
    public List<ActTrackEntry> findActTrack(List<ObjectId> ids, int begin, int pageSize) {
        BasicDBObject query=new BasicDBObject();
        BasicDBList values = new BasicDBList();
        values.add( new BasicDBObject("uid", new BasicDBObject(Constant.MONGO_IN,ids)));
        values.add( new BasicDBObject("rid", new BasicDBObject(Constant.MONGO_IN,ids)).append("att",1));
        query.put(Constant.MONGO_OR,values);
        BasicDBObject sortfield=new BasicDBObject();
        sortfield.put("ct", -1);
        List<DBObject> objectList= find(MongoFacroty.getAppDB(),Constant.COLLECTION_ACTIVITY_TRACK_NAME, query,Constant.FIELDS,sortfield,begin,pageSize);
        List<ActTrackEntry> actTrackEntryList=new ArrayList<ActTrackEntry>();
        for(DBObject dbObject:objectList){
            actTrackEntryList.add(new ActTrackEntry((BasicDBObject)dbObject));
        }
        return actTrackEntryList;
    }

    /*
    * 动态类型
    * 用户id
    * 关联id（可能是userid 或者actid 视动态类型而定）
    * 动态发生时间
    * 事件所发生的设备（ios  android pc）
    * */
    public void insertActTrack(int actTrackType, String userId, String activityId,long time, int fromDevice) {
        ActTrackEntry actTrackEntry=new ActTrackEntry();
        actTrackEntry.setUserId(new ObjectId(userId));
        actTrackEntry.setRelatedId(new ObjectId(activityId));
        actTrackEntry.setCreateTime(time);
        actTrackEntry.setActTrackDevice(ActTrackDevice.values()[fromDevice]);
        actTrackEntry.setActTrackType(ActTrackType.values()[actTrackType]);
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_TRACK_NAME, actTrackEntry.getBaseEntry());
    }

    public void insertActTrack(ActTrackEntry actTrackEntry) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_TRACK_NAME, actTrackEntry.getBaseEntry());
    }

    public int findActTrackCount(List<ObjectId> ids) {
        BasicDBObject query=new BasicDBObject();
        BasicDBList values = new BasicDBList();
        values.add( new BasicDBObject("uid", new BasicDBObject(Constant.MONGO_IN,ids)));
        values.add( new BasicDBObject("rid", new BasicDBObject(Constant.MONGO_IN,ids)).append("att",1));
        query.put(Constant.MONGO_OR,values);
        int count=count(MongoFacroty.getAppDB(), Constant.COLLECTION_ACTIVITY_TRACK_NAME, query);
        return count;
    }

    /**
     * 获取所有的活动id列表并删除
     * add by miaoqiang
     * @param activityIds
     * @return
     */
    public void deleteByActivityId(List<ObjectId> activityIds)
    {
        BasicDBObject query=new BasicDBObject();
        query.append("rid", new BasicDBObject(Constant.MONGO_IN,activityIds));

        remove(MongoFacroty.getAppDB(),Constant.COLLECTION_ACTIVITY_TRACK_NAME,query);
    }
}
