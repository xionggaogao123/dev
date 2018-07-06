package com.pojo.controlphone;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by James on 2018/3/22.
 * id
 * communityId   社区id           cid(  针对家长 孩子id  针对老师 社区id  针对孩子 null)
 * userId        用户id           uid
 * version       管控版本号       vsn
 * status        在线状态         sta          0  离线      1  在线   //mqtt
 * channelId     标记id           hid
 * time          消息时间         tim
 * dateTime      时间             dtm   dtm
 * type          类型             typ         1  收到         2  发出
 *
 */
public class ControlVersionEntry extends BaseDBObject {
    public ControlVersionEntry(){

    }

    public ControlVersionEntry(BasicDBObject object){
        super(object);
    }
    //添加构造
    public ControlVersionEntry( ObjectId communityId,
                                ObjectId userId,
                                String version,
                                int status,
                                int type){
        BasicDBObject basicDBObject = new BasicDBObject()
                .append("cid",communityId)
                .append("uid", userId)
                .append("vsn",version)
                .append("sta",status)
                .append("typ",type)
                .append("hid","")
                .append("dtm", new Date().getTime())
                .append("tim",new Date().getTime())
                .append("isr", 0);

        setBaseEntry(basicDBObject);
    }

    public ObjectId getCommunityId(){
        return getSimpleObjecIDValue("cid");
    }
    public void setCommunityId(ObjectId communityId){
        setSimpleValue("cid",communityId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }
    public String getVersion(){
        return getSimpleStringValue("vsn");
    }

    public void setVersion(String version){
        setSimpleValue("vsn", version);
    }

    public long getDateTime(){
        return getSimpleLongValue("dtm");
    }

    public void setDateTime(long dateTime){
        setSimpleValue("dtm",dateTime);
    }

    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }

    public int getStatus(){
        return getSimpleIntegerValueDef("sta",1);
    }

    public void setStatus(int status){
        setSimpleValue("sta",status);
    }

    public long getTime(){
        return getSimpleLongValue("tim");
    }

    public void setTime(long time){
        setSimpleValue("tim",time);
    }

    public String getChannelId(){
        return  getSimpleStringValue("hid");
    }
    public void setChannelId(String channelId){
        setSimpleValue("hid",channelId);
    }



}
