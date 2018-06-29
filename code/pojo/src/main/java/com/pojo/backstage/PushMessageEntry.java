package com.pojo.backstage;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2018-06-26.
 * jpush系统定时推送消息类
 *
 * sendName        推送人名称                      snm
 * userIds         List<ObjectId>收件人集合        uis
 * contactId       关联id                          cid
 * description     推送描述                        des
 * title           推送主题                        tit
 * sendTime        推送时间                        stm
 * dateTime        推送日期                        dtm
 * type            类型                            typ             1 直播未进入提示    2
 * status          状态                            sta             0 未推送     1 已推送
 *
 */
public class PushMessageEntry extends BaseDBObject {
    public PushMessageEntry(){

    }

    public PushMessageEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }
    //添加构造
    public PushMessageEntry(
            String sendName,
            List<ObjectId> userIds,
            ObjectId contactId,
            String description,
            String title,
            long sendTime,
            long dateTime,
            int type,
            int status

    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("snm", sendName)
                .append("uis", userIds)
                .append("tit", title)
                .append("cid", contactId)
                .append("des", description)
                .append("stm",sendTime)
                .append("dtm", dateTime)
                .append("typ", type)
                .append("sta", status)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    public ObjectId getContactId(){
        return getSimpleObjecIDValue("cid");
    }
    public void setContactId(ObjectId contactId){
        setSimpleValue("cid",contactId);
    }

    public String getSendName(){
        return getSimpleStringValue("snm");
    }

    public void setSendName(String sendName){
        setSimpleValue("snm",sendName);
    }

    public void setUserIds(List<ObjectId> userIds){
        setSimpleValue("uis", MongoUtils.convert(userIds));
    }

    public List<ObjectId> getUserIds(){
        ArrayList<ObjectId> userIds = new ArrayList<ObjectId>();
        BasicDBList dbList = (BasicDBList) getSimpleObjectValue("uis");
        if(dbList != null && !dbList.isEmpty()){
            for (Object obj : dbList) {
                userIds.add((ObjectId)obj);
            }
        }
        return userIds;
    }

    public String getTitle(){
        return getSimpleStringValue("tit");
    }

    public void setTitle(String title){
        setSimpleValue("tit",title);
    }

    public String getDescription(){
        return getSimpleStringValue("des");
    }

    public void setDescription(String description){
        setSimpleValue("des",description);
    }


    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }

    public int getStatus(){
        return getSimpleIntegerValue("sta");
    }

    public void setStatus(int status){
        setSimpleValue("sta",status);
    }
    public long getSendTime(){
        return getSimpleLongValue("stm");
    }
    public void setSendTime(long sendTime){
        setSimpleValue("stm",sendTime);
    }

    public long getDateTime(){
        return getSimpleLongValue("dtm");
    }
    public void setDateTime(long dateTime){
        setSimpleValue("dtm",dateTime);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }

}
