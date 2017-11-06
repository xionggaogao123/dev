package com.pojo.controlphone;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by James on 2017/11/3.
 *
 ControlPhone(管控手机表)
 id
 name               名称            			 nam
 phone              电话            			 pho
 type               类型（1 常用 2导入）		 typ
 createTime         创建时间                     ctm
 isremove           是否删除             	     isr
 */
public class ControlPhoneEntry extends BaseDBObject {
    public ControlPhoneEntry(){

    }

    public ControlPhoneEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }
    //添加构造
    public ControlPhoneEntry(
            ObjectId parentId,
            ObjectId userId,
            String name,
            String phone,
            int type
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("pid",parentId)
                .append("uid",userId)
                .append("nam", name)
                .append("pho", phone)
                .append("typ", type)
                .append("ctm", new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public ControlPhoneEntry(
            ObjectId id,
            ObjectId parentId,
            ObjectId userId,
            String name,
            String phone,
            int type
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID, id)
                .append("pid",parentId)
                .append("uid", userId)
                .append("nam", name)
                .append("pho", phone)
                .append("typ", type)
                .append("ctm", new Date().getTime())
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

    public String getName(){
        return getSimpleStringValue("nam");
    }

    public void setName(String name){
        setSimpleValue("nam",name);
    }


    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }

    public String getPhone(){
        return getSimpleStringValue("pho");
    }

    public void setPhone(String phone){
        setSimpleValue("pho",phone);
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
