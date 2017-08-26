package com.pojo.operation;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2017/8/25.
 * Id                                 	 id
 parentId            关联作业id    		  pid
 userId              评论人id       	 uid
 dateTime           发表时间               dtm
 Description          描述                 des
 Type               1图片2视屏3录音	      typ
 fileUrl              文件地址           ful
 */
public class AppOperationEntry  extends BaseDBObject {
    public AppOperationEntry(){

    }
    public AppOperationEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }
    //添加构造
    public AppOperationEntry(
            ObjectId parentId,
            ObjectId userId,
            long dateTime,
            int type,
            String description,
            String fileUrl
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("des", description)
                .append("pid",parentId)
                .append("uid",userId)
                .append("typ",type)
                .append("ful",fileUrl)
                .append("dtm", dateTime)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public AppOperationEntry(
            ObjectId id,
            ObjectId parentId,
            ObjectId userId,
            long dateTime,
            int type,
            String description,
            String fileUrl
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("des", description)
                .append("pid",parentId)
                .append("uid",userId)
                .append("typ",type)
                .append("ful",fileUrl)
                .append("dtm", dateTime)
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

    public String getDescription(){
        return getSimpleStringValue("des");
    }

    public void setDescription(String description){
        setSimpleValue("des",description);
    }

    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }

    public String getFileUrl(){
        return getSimpleStringValue("ful");
    }

    public void setFileUrl(String fileUrl){
        setSimpleValue("ful",fileUrl);
    }

    public long getDateTime(){
        return getSimpleLongValue("dtm");
    }
    public void setDateTime(long dateTime){
        setSimpleValue("dtm",dateTime);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
}
