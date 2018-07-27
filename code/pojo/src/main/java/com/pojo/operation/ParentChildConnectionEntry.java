package com.pojo.operation;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by James on 2018-07-26.
 * 用于无学生手机的家长的代提交操作
 *
 * parentId                  父id                        pid
 * communityId               社区id                      cid
 * userId                    用户id                      uid
 * userName                  用户姓名                    nam
 * avatar                    头像                        ava
 *
 *
 */
public class ParentChildConnectionEntry extends BaseDBObject {
    public ParentChildConnectionEntry(){

    }
    public ParentChildConnectionEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }
    //添加构造
    public ParentChildConnectionEntry(
            ObjectId parentId,
            ObjectId communityId,
            ObjectId userId,
            String userName,
            String avatar
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("pid", parentId)
                .append("cid",communityId)
                .append("uid", userId)
                .append("una",userName)
                .append("ava", avatar)
                .append("ctm",new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);
    }


    public ObjectId getParentId(){
        return getSimpleObjecIDValue("pid");
    }

    public void setParentId(ObjectId parentId){
        setSimpleValue("pid",parentId);
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


    public String getUserName(){
        return getSimpleStringValue("una");
    }

    public void setUserName(String userName){
        setSimpleValue("una",userName);
    }

    public String getAvatar(){
        return getSimpleStringValue("ava");
    }

    public void setAvatar(String avatar){
        setSimpleValue("ava",avatar);
    }

    public long getCreateTime(){
        return getSimpleLongValue("ctm");
    }
    public void setCreateTime(long createTime){
        setSimpleValue("ctm",createTime);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
}
