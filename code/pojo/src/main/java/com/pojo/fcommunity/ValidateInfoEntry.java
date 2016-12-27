package com.pojo.fcommunity;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

/**
 * Created by admin on 2016/12/26.
 * {
 *     uid:userId 申请人Id
 *     rw:reviewedId 审核人Id
 *     ap:approvedId 批准人Id
 *     aps:approvedStatus 批准状态（0:已批准 1:未批准）
 *     msg:applyMessage 申请备注
 *     cmId:communityId 社区Id
 *     ty:type 0:(针对申请人) 1:(针对审核人)
 *     st:status 审核状态(0:未审核 1:已审核(若没有权限,判断是否为本人审核))
 *     aut:authority 权限状态(0:有权限 1:没有权限->针对审核人)
 *     way:way 途径 1:扫描二维码 2:申请加入
 *     rws:reviewState 审核状态(针对申请人 0:审核通过 1:审核失败)
 *     rei:reviewKeyId(为了区分是否是再次申请的,若申请失败可以再申请一次，每次申请都会出现这个关键字Id,申请人和审核人的绑定关系)
 *     ir:remove
 * }
 */
public class ValidateInfoEntry extends BaseDBObject {

     public ValidateInfoEntry(BasicDBObject dbObject){
         setBaseEntry(dbObject);
     }


    /**
     * 针对申请人
     * @param userId
     * @param applyMessage
     * @param communityId
     * @param type
     * @param way
     * @param reviewKeyId
     */
     public ValidateInfoEntry(ObjectId userId, String applyMessage,
                              ObjectId communityId, int type,int way,ObjectId reviewKeyId){
         BasicDBObject dbObject=new BasicDBObject()
                 .append("rw",userId)
                 .append("msg",applyMessage)
                 .append("cmId",communityId)
                 .append("ty",type)
                 .append("way",way)
                 .append("rei",reviewKeyId)
                 .append("st",0)
                 .append("ir",0);
         setBaseEntry((BasicDBObject)dbObject);
     }

    /**
     * 针对审核人
     * @param userId
     * @param reviewedId
     * @param applyMessage
     * @param communityId
     * @param type
     * @param reviewKeyId
     */
     public ValidateInfoEntry(ObjectId userId, ObjectId reviewedId, String applyMessage,
                              ObjectId communityId, int type,ObjectId reviewKeyId){
         BasicDBObject dbObject=new BasicDBObject()
                 .append("uid",userId)
                 .append("rw",reviewedId)
                 .append("msg",applyMessage)
                 .append("cmId",communityId)
                 .append("ty",type)
                 .append("rei",reviewKeyId)
                 .append("st",0)
                 .append("aut",0)
                 .append("ir",0);
         setBaseEntry((BasicDBObject)dbObject);
     }

     public ObjectId getUserId(){
         if(getBaseEntry().containsField("uid")) {
             return getSimpleObjecIDValue("uid");
         }else{
             return null;
         }
     }

     public ObjectId getReviewedId(){
         return getSimpleObjecIDValue("rw");
     }

     public String getApplyMessage(){
         return getSimpleStringValue("msg");
     }

     public ObjectId getCommunityId(){
         return getSimpleObjecIDValue("cmId");
     }

     public int getType(){
         return getSimpleIntegerValue("ty");
     }

     public int getStatus(){
         return  getSimpleIntegerValue("st");
     }

     public void setStatus(int status){
         setSimpleValue("st",status);
     }

     public int getAuthority(){
         return getSimpleIntegerValueDef("aut",-1);
     }

     public int getRemove(){
         return getSimpleIntegerValue("ir");
     }

     public int getWay(){
         return getSimpleIntegerValueDef("way",-1);
     }

     public int getReviewState(){
         return getSimpleIntegerValueDef("rws",-1);
     }

     public void setReviewState(int reviewState){
         setSimpleValue("rws",reviewState);
     }

     public ObjectId getReviewKeyId(){
         return getSimpleObjecIDValue("rei");
     }

     public int getApprovedStatus(){
         return getSimpleIntegerValueDef("aps",-1);
     }

     public void setApprovedStatus(int approvedStatus){
         setSimpleValue("aps",approvedStatus);
     }

     public ObjectId getApprovedId(){
         if(getBaseEntry().containsField("ap")){
             return getSimpleObjecIDValue("ap");
         }else{
             return null;
         }
     }

     public void setApprovedId(ObjectId approvedId){
         setSimpleValue("ap",approvedId);
     }

}
