package com.pojo.forum;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/7/6.
 *
 * 消息（留言）
 * {
 *     uid : userId 留言人Id
 *     psid: personId 被留言人Id(处理人Id)
 *     cn: content 留言内容
 *     ti: time 留言时间
 *     ty: type 消息类型 0：我的消息 1：系统消息 2:点赞消息
 *     sc: scan 是否浏览 0：未浏览 1：已浏览
 *
 * }
 */
public class FInformationEntry extends BaseDBObject {

    public FInformationEntry(){
    }

    /**系统消息 **/
    public FInformationEntry(ObjectId personId,long time,String content){
        BasicDBObject baseEntry = new BasicDBObject()
                .append("psid",personId)
                .append("ty",1)
                .append("ti",time)
                .append("cn",content)
                .append("sc",0);
        setBaseEntry(baseEntry);
    }
    /**普通消息**/
    public FInformationEntry(ObjectId userId,ObjectId personId,int type,long time,String content,int scan){
        BasicDBObject baseEntry = new BasicDBObject()
                .append("uid",userId)
                .append("psid",personId)
                .append("ty",type)
                .append("ti",time)
                .append("cn",content)
                .append("sc",scan);
        setBaseEntry(baseEntry);
    }
    /**点赞消息**/
    public FInformationEntry(ObjectId userId,ObjectId personId,int type,long time,int scan,ObjectId ptid){
        BasicDBObject baseEntry = new BasicDBObject()
                .append("uid",userId)
                .append("psid",personId)
                .append("ty",type)
                .append("ti",time)
                .append("sc",scan)
                .append("ptid",ptid);
        setBaseEntry(baseEntry);
    }
    /**回复消息 **/
    public FInformationEntry(ObjectId userId,ObjectId personId,int type,long time,int scan,ObjectId ptid,ObjectId replyId){
        BasicDBObject baseEntry = new BasicDBObject()
                .append("uid",userId)
                .append("psid",personId)
                .append("ty",type)
                .append("ti",time)
                .append("sc",scan)
                .append("ptid",ptid)
                .append("rid",replyId);
        setBaseEntry(baseEntry);
    }

    public FInformationEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getPersonId(){
        return getSimpleObjecIDValue("psid");
    }

    public void setPersonId(ObjectId personId){
        setSimpleValue("psid",personId);
    }

    public String getContent(){
        return getSimpleStringValue("cn");
    }

    public void setContent(String content){
        setSimpleValue("cn",content);
    }

    public Long getTime(){
        return getSimpleLongValue("ti");
    }

    public void setTime(Long time){
        setSimpleValue("ti", time);
    }

    public int getType(){
        return getSimpleIntegerValue("ty");
    }

    public void setType(int type){
        setSimpleValue("ty",type);
    }

    public int getScan(){
        return getSimpleIntegerValue("sc");
    }

    public void setScan(int scan){
        setSimpleValue("sc",scan);
    }

    public ObjectId getPostId(){
        return getSimpleObjecIDValue("ptid");
    }

    public void setPostId(ObjectId postId){
        setSimpleValue("ptid",postId);
    }

    public ObjectId getRyplyId(){
        return getSimpleObjecIDValue("rid");
    }

    public void setReplyId(ObjectId replyId){
        setSimpleValue("rid",replyId);
    }
}
