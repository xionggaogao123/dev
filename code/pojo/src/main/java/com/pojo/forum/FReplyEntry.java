package com.pojo.forum;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by wangkaidong on 2016/5/30.
 *
 * 回帖
 * {
 *     ptid : postId 帖子id
 *     pt : postTitle 帖子标题
 *     rimg : replyImage 回帖人的头像
 *     rc : replyComment 回帖内容
 *     ti : time 回帖时间
 *     pstid : postSectionId 帖子所属版块id
 *     psid: personId 用户id（回帖人id）
 *     psnm : personName 回帖人呢称
 *     pf : platform 帖子来源
 *
 * }
 */
public class FReplyEntry extends BaseDBObject {

    public FReplyEntry(){}

    public FReplyEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }
    public ObjectId getPostId(){
        return getSimpleObjecIDValue("ptid");
    }

    public void setPostId(ObjectId id){
        setSimpleValue("ptid",id);
    }

    public String getPostTitle(){
        return getSimpleStringValue("pt");
    }

    public void setPostTitle(String postTitle){
        setSimpleValue("pt",postTitle);
    }

    public void setReplyImage(String replyImage){
        setSimpleValue("rimg",replyImage);
    }

    public String getReplyImage(){
        return getSimpleStringValue("rimg");
    }

    public String getReplyComment(){
        return getSimpleStringValue("rc");
    }

    public void setReplyComment(String replyComment){
        setSimpleValue("rc",replyComment);
    }

    public ObjectId getPostSectionId(){
        return getSimpleObjecIDValue("pstid");
    }

    public void setPostSectionId(ObjectId id){
        setSimpleValue("pstid",id);
    }

    public Long getTime(){
        return getSimpleLongValue("ti");
    }

    public void setTime(Long time){
        setSimpleValue("ti", time);
    }

    public String getPlatform(){
        return getSimpleStringValue("pf");
    }

    public void setPlatform(String platform){
        setSimpleValue("pf",platform);
    }

    public String getPersonName(){
        return getSimpleStringValue("psnm");
    }

    public void setPersonName(String personName){
        setSimpleValue("psnm",personName);
    }

    public ObjectId getPersonId(){
        return getSimpleObjecIDValue("psid");
    }

    public void setPersonId(ObjectId id){
        setSimpleValue("psid",id);
    }
}
