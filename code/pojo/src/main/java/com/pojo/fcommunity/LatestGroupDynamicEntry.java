package com.pojo.fcommunity;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scott on 2017/2/15.
 * {
 *     communityId
 *     communityDetailId
 *     userId
 *     nickname(群昵称)
 *     readedList
 *     type
 * }
 */
public class LatestGroupDynamicEntry extends BaseDBObject {

    public LatestGroupDynamicEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject) dbObject);
    }

    public LatestGroupDynamicEntry(ObjectId communityId, ObjectId communityDetailId, ObjectId userId,
                                   List<ObjectId> readedList,int type,String message){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("cid",communityId)
                .append("cdid",communityDetailId)
                .append("uid",userId)
                .append("rl",readedList)
                .append("ty",type)
                .append("msg",message)
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public String getMessage(){
        return getSimpleStringValue("msg");
    }

    public void setMessage(String message){
        setSimpleValue("msg",message);
    }

    public int getType(){
        return getSimpleIntegerValue("ty");
    }

    public void setType(int type){
        setSimpleValue("ty",type);
    }

    public void setReadedList(List<ObjectId> readedList){
        setSimpleValue("rl",MongoUtils.convert(readedList));
    }

    public List<ObjectId> getReadedList(){
        List<ObjectId> rets=new ArrayList<ObjectId>();
        BasicDBList dbList=getDbList("rl");
        if(null!=dbList&&!dbList.isEmpty()) {
            for (Object obj : dbList) {
                rets.add((ObjectId) obj);
            }
        }
        return rets;
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getCommunityId(){
        return getSimpleObjecIDValue("cid");
    }

    public void setCommunityId(ObjectId communityId){
        setSimpleValue("cid",communityId);
    }

    public ObjectId getCommunityDetailId(){
        return getSimpleObjecIDValue("cdid");
    }

    public void setCommunityDetailId(ObjectId communityDetailId){
        setSimpleValue("cdid",communityDetailId);
    }
}
