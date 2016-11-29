package com.pojo.fcommunity;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/11/29.
 * {
 *     rst:roleStr 社区成员原有角色(社长，副社长，社员)
 *     uid:userId 用户Id
 *     reid:relationId 关联人Id
 *     ty:type 系统消息类型 1:退出社区 2:通知别人（针对社员及副社长）3:通知自己(针对副社长) 4:通知自己(针对社长) 5:通知自己(针对社长)
 *     ti:time 创建系统消息时间
 *     unr:unRead 消息是否处理标志（0:未读 1:已读）
 *     r:remove 删除已否 0 1
 * }
 */
public class CommunitySystemInfoEntry extends BaseDBObject{

    public CommunitySystemInfoEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public CommunitySystemInfoEntry(String roleStr,ObjectId userId,ObjectId relationId,int type,
                                    ObjectId communityId,long time){
        BasicDBObject dbObject=new BasicDBObject()
                .append("rst",roleStr)
                .append("uid",userId)
                .append("reid",relationId)
                .append("ty",type)
                .append("cmid",communityId)
                .append("ti",time)
                .append("unr",0)
                .append("r",0);
        setBaseEntry(dbObject);
    }

    public ObjectId getRelationId(){
        return getSimpleObjecIDValue("reid");
    }

    public void setRelationId(ObjectId relationId){
        setSimpleValue("reid",relationId);
    }

    public String getRoleStr(){
        return getSimpleStringValue("rst");
    }

    public void setRoleStr(String roleStr){
        setSimpleValue("rst",roleStr);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public int getType(){
        return getSimpleIntegerValue("ty");
    }

    public void setType(int type){
        setSimpleValue("ty",type);
    }

    public ObjectId getCommunityId(){
        return getSimpleObjecIDValue("cmid");
    }


    public void setCommunityId(ObjectId communityId){
        setSimpleValue("cmid",communityId);
    }

    public long getTime(){
        return getSimpleLongValue("ti");
    }

    public void setTime(long time){
        setSimpleValue("ti",time);
    }

    public int getRemove(){
        return getSimpleIntegerValue("r");
    }

    public void setRemove(int remove){
        setSimpleValue("r",remove);
    }

    public int getUnRead(){
        return getSimpleIntegerValue("unr");
    }

    public void setUnRead(int unRead){
        setSimpleValue("unr",unRead);
    }

}
