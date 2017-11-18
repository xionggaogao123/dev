package com.pojo.backstage;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by James on 2017/11/18.
 * id
 createTime          创建时间             ctm
 userId               用户id              uid
 function              功能               fun
 type                1 图片 2 文字       typ
 isCheck               是否通过           isc
 isRemove              是否删除           isr
 content               内容               con
 */
public class UnlawfulPictureTextEntry extends BaseDBObject {

    public UnlawfulPictureTextEntry(){

    }

    public UnlawfulPictureTextEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }
    //添加构造
    public UnlawfulPictureTextEntry(
            ObjectId userId,
            int function,
            String content,
            int type
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid", userId)
                .append("con", content)
                .append("fun", function)
                .append("typ", type)
                .append("ctm", new Date().getTime())
                .append("isc", 0)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public UnlawfulPictureTextEntry(
            ObjectId id,
            ObjectId userId,
            int function,
            String content,
            int type
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("uid", userId)
                .append("con", content)
                .append("fun", function)
                .append("typ", type)
                .append("ctm", new Date().getTime())
                .append("isc", 0)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }


    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public String getContent(){
        return getSimpleStringValue("con");
    }

    public void setContent(String content){
        setSimpleValue("con",content);
    }


    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }

    public String getFunction(){
        return getSimpleStringValue("fun");
    }

    public void setFunction(String function){
        setSimpleValue("fun",function);
    }

    public long getCreateTime(){
        return getSimpleLongValue("ctm");
    }
    public void setCreateTime(long createTime){
        setSimpleValue("ctm",createTime);
    }
    public int getIsCheck(){
        return getSimpleIntegerValue("isc");
    }

    public void setIsCheck(int isCheck){
        setSimpleValue("isc",isCheck);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }

}
