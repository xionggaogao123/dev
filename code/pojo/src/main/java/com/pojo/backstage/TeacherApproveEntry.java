package com.pojo.backstage;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by James on 2017/11/23.
 * 教师认证
 * id
 * name             姓名                                    nam
 * oldAvatar        用户原头像                              ota
 * newAvatar        用户新头像                              nta
 * userId                                                   uid
 * applyTime        申请时间                                atm
 * type             1 申请   2 通过   3拒绝                 typ
 * approveId        通过人                                  aid
 * loadTime         操作时间                                ltm
 *
 *
 */
public class TeacherApproveEntry extends BaseDBObject {

    public TeacherApproveEntry(){

    }

    public TeacherApproveEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }
    //添加构造
    public TeacherApproveEntry(
            ObjectId userId,
            String name,
            String oldAvatar,
            String newAvatar,
            ObjectId approveId,
            long loadTime,
            int type
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid", userId)
                .append("nam", name)
                .append("ota",oldAvatar)
                .append("nta",newAvatar)
                .append("typ", type)
                .append("aid", approveId)
                .append("atm", new Date().getTime())
                .append("ltm", loadTime)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public TeacherApproveEntry(
            ObjectId id,
            ObjectId userId,
            String name,
            String oldAvatar,
            String newAvatar,
            ObjectId approveId,
            long loadTime,
            int type
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("uid", userId)
                .append("nam", name)
                .append("ota", oldAvatar)
                .append("nta",newAvatar)
                .append("typ", type)
                .append("aid", approveId)
                .append("atm", new Date().getTime())
                .append("ltm", loadTime)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }


    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }
    public ObjectId getApproveId(){
        return getSimpleObjecIDValue("aid");
    }
    public void setApproveId(ObjectId approveId){
        setSimpleValue("aid",approveId);
    }

    public String getName(){
        return getSimpleStringValue("nam");
    }

    public void setName(String name){
        setSimpleValue("nam",name);
    }

    public String getOldAvatar(){
        return getSimpleStringValue("ota");
    }

    public void setOldAvatar(String oldAvatar){
        setSimpleValue("ota",oldAvatar);
    }

    public String getNewAvatar(){
        return getSimpleStringValue("nta");
    }

    public void setNewAvatar(String newAvatar){
        setSimpleValue("nta",newAvatar);
    }

    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }


    public long getLoadTime(){
        return getSimpleLongValue("ltm");
    }
    public void setLoadTime(long loadTime){
        setSimpleValue("ltm",loadTime);
    }
    public long getApplyTime(){
        return getSimpleLongValue("atm");
    }
    public void setApplyTime(long applyTime){
        setSimpleValue("atm",applyTime);
    }


    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }


}
