package com.pojo.operation;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 *
 *  通用评论表
 * Created by James on 2017/8/25.
 * Id                                 	 id
 parentId            关联作业id    		  pid
 userId              评论人id       	 uid
 backId               回复人id           bid
 level               层级                 lev
 dateTime           发表时间               dtm
 Description          描述                 des
 Type               1图片2视屏3录音	      typ
 role                 1 家长 2学生        rol
 cover                封面图片           cov
 second               秒数               sec
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
            ObjectId backId,
            int level,
            long dateTime,
            int type,
            int role,
            String description,
            int second,
            String cover,
            String fileUrl
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("des", description)
                .append("pid",parentId)
                .append("uid",userId)
                .append("bid",backId)
                .append("lev",level)
                .append("typ",type)
                .append("rol",role)
                .append("sec", second)
                .append("cov",cover)
                .append("ful", fileUrl)
                .append("dtm", dateTime)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public AppOperationEntry(
            ObjectId id,
            ObjectId parentId,
            ObjectId userId,
            ObjectId backId,
            int level,
            long dateTime,
            int type,
            int role,
            String description,
            int second,
            String cover,
            String fileUrl
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID,id)
                .append("des", description)
                .append("pid", parentId)
                .append("uid",userId)
                .append("bid", backId)
                .append("lev",level)
                .append("typ", type)
                .append("rol", role)
                .append("sec", second)
                .append("cov",cover)
                .append("ful", fileUrl)
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
    public ObjectId getBackId(){
        return getSimpleObjecIDValue("bid");
    }

    public void setBackId(ObjectId backId){
        setSimpleValue("bid",backId);
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
    public int getRole(){
        return getSimpleIntegerValue("rol");
    }

    public void setRole(int role){
        setSimpleValue("rol",role);
    }
    public int getLevel(){
        return getSimpleIntegerValue("lev");
    }

    public void setLevel(int level){
        setSimpleValue("lev",level);
    }

    public int getSecond(){
        return getSimpleIntegerValue("sec");
    }

    public void setSecond(int second){
        setSimpleValue("sec",second);
    }

    public String getCover(){
        return getSimpleStringValue("cov");
    }

    public void setCover(String cover){
        setSimpleValue("cov",cover);
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
