package com.pojo.fcommunity;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by admin on 2016/12/26.
 * {
 *     uid:userId 邀请人Id
 *     rw:reviewedId 被邀请人Id
 *     aps:approvedStatus 同意状态（0:默认 1:已同意  2 已拒绝）
 *     ctm: createTime  发出时间
 *     btm: backTime  回复时间
 *     sta:status  状态 （0 未处理  1 已处理）
 *     msg:applyMessage 申请备注
 *     cid:contactId  关联Id
 *     cty:contactType 类型   0 群组  1 社群
 *     ir:remove
 * }
 */
public class ValidateGroupInfoEntry extends BaseDBObject {

     public ValidateGroupInfoEntry(BasicDBObject dbObject){
         setBaseEntry(dbObject);
     }

    public ValidateGroupInfoEntry(){

    }

    public ValidateGroupInfoEntry(
            ObjectId userId,
            ObjectId reviewedId,
            ObjectId contactId,
            int contactType,
            int approvedStatus,
            long backTime,
            int status,
            String applyMessage
                ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid",userId)
                .append("rw", reviewedId)
                .append("cid", contactId)
                .append("sta", status)
                .append("cty", contactType)
                .append("aps",approvedStatus)
                .append("btm",backTime)
                .append("sta",status)
                .append("msg",applyMessage)
                .append("ctm", new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);

    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getReviewedId(){
        return getSimpleObjecIDValue("rw");
    }
    public void setReviewedId(ObjectId reviewedId){
        setSimpleValue("rw",reviewedId);
    }
    public ObjectId getContactId(){
        return getSimpleObjecIDValue("cid");
    }
    public void setContactId(ObjectId contactId){
        setSimpleValue("cid",contactId);
    }
    public int getStatus(){
        return getSimpleIntegerValue("sta");
    }

    public void setStatus(int status){
        setSimpleValue("sta",status);
    }

    public int getContactType(){
        return getSimpleIntegerValue("cty");
    }

    public void setContactType(int contactType){
        setSimpleValue("cty",contactType);
    }

    public int getApprovedStatus(){
        return getSimpleIntegerValue("aps");
    }

    public void setApprovedStatus(int approvedStatus){
        setSimpleValue("aps",approvedStatus);
    }


    public String getApplyMessage(){
        return  getSimpleStringValue("msg");
    }
    public void setApplyMessage(String applyMessage){
        setSimpleValue("msg",applyMessage);
    }

    public long getBackTime(){
        return getSimpleLongValue("btm");
    }

    public void setBackTime(long backTime){
        setSimpleValue("btm",backTime);
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
