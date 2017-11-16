package com.pojo.controlphone;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by James on 2017/11/15.\
 * id                           id
 * parentId      父id            pid
 * userId        用户id           uid
 * type          类型             typ  // 1 应用禁用  2
 * name          姓名             nam
 * description   描述             des
 * createTime    创建时间         ctm
 */
public class ControlMessageEntry extends BaseDBObject {
    public ControlMessageEntry(){

    }

    public ControlMessageEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }
    //添加构造
    public ControlMessageEntry(
            ObjectId parentId,
            ObjectId userId,
            String name,
            String description,
            int type
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("pid",parentId)
                .append("uid",userId)
                .append("nam", name)
                .append("pho", description)
                .append("typ", type)
                .append("ctm", new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public ControlMessageEntry(
            ObjectId id,
            ObjectId parentId,
            ObjectId userId,
            String name,
            String description,
            int type
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID, id)
                .append("pid",parentId)
                .append("uid", userId)
                .append("nam", name)
                .append("pho", description)
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

    public String getDescription(){
        return getSimpleStringValue("pho");
    }

    public void setDescription(String description){
        setSimpleValue("pho",description);
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
