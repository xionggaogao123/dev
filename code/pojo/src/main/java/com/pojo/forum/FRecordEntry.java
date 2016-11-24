package com.pojo.forum;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/6/21.
 *
 *  帖子
 *{
 *     ptid : postId 帖子id
 *     pt : postTitle 主题
 *     ti : time 操作时间
 *     psid : personId 回复人（删除人）Id
 *     uid : userId 处理对象 Id
 *     log : logRecord 行为标志，1表示删除，2表示回复
 *     pstid: postSectionId板块Id
 *     ls:logScan 浏览标志 是否浏览过 0表示未浏览过1表示为浏览过
 *
 *}
 */
public class FRecordEntry extends BaseDBObject {
    public FRecordEntry(){}

    public FRecordEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }

    public String getPostTitle(){
        return getSimpleStringValue("pt");
    }

    public void setPostTitle(String postTitle){
        setSimpleValue("pt",postTitle);
    }

    public ObjectId getPersonId(){
        return getSimpleObjecIDValue("psid");
    }

    public void setPersonId(ObjectId id){
        setSimpleValue("psid",id);
    }

    public Long getTime(){
        return getSimpleLongValue("ti");
    }

    public void setTime(Long time){
        setSimpleValue("ti", time);
    }

    public ObjectId getPostId(){
        return getSimpleObjecIDValue("ptid");
    }

    public void setPostId(ObjectId id){
        setSimpleValue("ptid",id);
    }

    public int  getLogRecord(){
            return getSimpleIntegerValue("log");
    }

    public void setLogRecord(int logRecord){
        setSimpleValue("log", logRecord);
    }

    public ObjectId getUserId(){
        if(getBaseEntry().containsField("uid")) {
            return getSimpleObjecIDValue("uid");
        }else{
            return null;
        }
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getPostSectionId(){
        if(getBaseEntry().containsField("pstid")) {
            return getSimpleObjecIDValue("pstid");
        }else{
            return null;
        }
    }

    public void setPostSectionId(ObjectId id){
        setSimpleValue("pstid",id);
    }

    public int  getLogScan(){
        return getSimpleIntegerValue("ls");
    }

    public void setLogScan(int logScan){
        setSimpleValue("ls", logScan);
    }
}
