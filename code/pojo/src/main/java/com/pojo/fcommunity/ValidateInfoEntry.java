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
 *     st:status 审核状态(0:未审核 1:已审核)
 *     aut:authority 权限状态(0:有权限 1:没有权限)
 *     ir:remove
 * }
 */
public class ValidateInfoEntry extends BaseDBObject {

     public ValidateInfoEntry(BasicDBObject dbObject){
         setBaseEntry(dbObject);
     }

     public ValidateInfoEntry(ObjectId userId, ObjectId reviewedId, String applyMessage,
                              ObjectId communityId, int type){
         BasicDBObject dbObject=new BasicDBObject()
                 .append("uid",userId)
                 .append("rw",reviewedId)
                 .append("msg",applyMessage)
                 .append("cmId",communityId)
                 .append("ty",type)
                 .append("st",0)
                 .append("aut",0)
                 .append("ir",0);
         setBaseEntry((BasicDBObject)dbObject);
     }

     public ObjectId getUserId(){
         return getSimpleObjecIDValue("uid");
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

     public int getAuthority(){
         return getSimpleIntegerValue("aut");
     }

     public int getRemove(){
         return getSimpleIntegerValue("ir");
     }

     public int getApprovedStatus(){
         return getSimpleIntegerValueDef("aps",-1);
     }

     public ObjectId getApprovedId(){
         if(getBaseEntry().containsField("ap")){
             return getSimpleObjecIDValue("ap");
         }else{
             return null;
         }
     }

}
