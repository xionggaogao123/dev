package com.pojo.extendedcourse;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by James on 2018-12-06.
 *
 * 拓展课 — 用户参与情况
 *
 *  schoolId            学校id                sid
 *  gradeName           年级名称              gnm
 *  gradeId             年级id                gid
 *  communityId         社群id                cmid
 *  courseId            课程id                coid
 *  userId              用户id                uid
 *  status              状态                  sta              0 已退课     1  报名       2 入选
 *  applyUserId         操作人                aid              （最近一次操作人）
 *  applyType           操作方式              aty              0 自己报名   1 家长报名   2 老师开始报名前调整    3  老师结束后调整
 *
 */
public class ExtendedUserApplyEntry  extends BaseDBObject{
    public ExtendedUserApplyEntry(){

    }

    public ExtendedUserApplyEntry(BasicDBObject dbObject){
        super(dbObject);
    }

    public ExtendedUserApplyEntry(
            ObjectId schoolId,
            String gradeName,
            ObjectId gradeId,
            ObjectId communityId,
            ObjectId courseId,
            ObjectId userId,
            int status,
            ObjectId applyUserId,
            int applyType
    ){

        BasicDBObject dbObject=new BasicDBObject()
                .append("sid", schoolId)
                .append("gnm", gradeName)
                .append("gid", gradeId)
                .append("cmid", communityId)
                .append("cid", courseId)
                .append("uid",userId)
                .append("sta", status)
                .append("aid", applyUserId)
                .append("aty", applyType)
                .append("ctm",new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);

    }

    public int getStatus(){
        return getSimpleIntegerValue("sta");
    }

    public void setStatus(int status){
        setSimpleValue("sta",status);
    }


    public int getApplyType(){
        return getSimpleIntegerValue("aty");
    }

    public void setApplyType(int applyType){
        setSimpleValue("aty",applyType);
    }


    public void setGradeId(ObjectId gradeId){
        setSimpleValue("gid",gradeId);
    }

    public ObjectId getGradeId(){
        return getSimpleObjecIDValue("gid");
    }

    public void setCommunityId(ObjectId communityId){
        setSimpleValue("cmid",communityId);
    }

    public ObjectId getCommunityId(){
        return getSimpleObjecIDValue("cmid");
    }

    public void setCourseId(ObjectId courseId){
        setSimpleValue("cid",courseId);
    }

    public ObjectId getCourseId(){
        return getSimpleObjecIDValue("cid");
    }

    public void setApplyUserId(ObjectId applyUserId){
        setSimpleValue("aid",applyUserId);
    }

    public ObjectId getApplyUserId(){
        return getSimpleObjecIDValue("aid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setSchoolId(ObjectId schoolId){
        setSimpleValue("sid",schoolId);
    }

    public ObjectId getSchoolId(){
        return getSimpleObjecIDValue("sid");
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
