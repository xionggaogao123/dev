package com.pojo.operation;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *  通用评论表
 * Created by James on 2017/8/25.
 * Id                                      id
 * contactId        关联作业id            cid
 parentId            父id    		  pid
 userId              评论人id       	 uid
 backId               回复人id           bid
 level               层级                 lev
 dateTime           发表时间               dtm
 Description          描述                 des
 Type               1图片2视屏3录音	      typ
 role                 1 家长讨论  2 学生讨论  3 学生提交  rol  5 联系我们
 cover                封面图片           cov
 second               秒数               sec
 fileUrl              文件地址           ful
 jiajing              加精             jj
 zanList             点赞列表
 */
public class AppNewOperationEntry extends BaseDBObject {
    public AppNewOperationEntry(){

    }
    public AppNewOperationEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }
    //添加构造
    public AppNewOperationEntry(
            ObjectId contactId,
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
        List<ObjectId> zanList=new ArrayList<ObjectId>();
        BasicDBObject dbObject=new BasicDBObject()
                .append("cid",contactId)
                .append("des", description)
                .append("pid", parentId)
                .append("uid",userId)
                .append("bid",backId)
                .append("lev",level)
                .append("typ",type)
                .append("rol",role)
                .append("sec", second)
                .append("cov",cover)
                .append("ful", fileUrl)
                .append("dtm", dateTime)
                .append("zc", Constant.ZERO)
                .append("jj", Constant.ZERO)
                .append("zl", MongoUtils.convert(zanList))
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public AppNewOperationEntry(
            ObjectId id,
            ObjectId contactId,
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
                .append("cid",contactId)
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

    public int getJiajing(){
        return getSimpleIntegerValue("jj");
    }
    public void setJiajing(int jiajing){
        setSimpleValue("jj",jiajing);
    }

    public ObjectId getParentId(){
        return getSimpleObjecIDValue("pid");
    }

    public void setParentId(ObjectId parentId){
        setSimpleValue("pid",parentId);
    }
    public ObjectId getContactId(){
        return getSimpleObjecIDValue("cid");
    }

    public void setContactId(ObjectId contactId){
        setSimpleValue("cid",contactId);
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

    public List<ObjectId> getZanList(){
        List<ObjectId> zanList=new ArrayList<ObjectId>();
        if (!getBaseEntry().containsField("zl")) {
            return zanList;
        } else {
            BasicDBList basicDBList=(BasicDBList)getSimpleObjectValue("zl");
            if(null!=basicDBList&&!basicDBList.isEmpty()){
                for(Object o:basicDBList){
                    zanList.add((ObjectId)o);
                }
            }
        }
        return zanList;
    }

    public void setZanList(List<ObjectId> zanList){
        setSimpleValue("zl",MongoUtils.convert(zanList));
    }

    public int getZanCount(){
        return getSimpleIntegerValueDef("zc",Constant.ZERO);
    }

    public void setZanCount(int zanCount){
        setSimpleValue("zc",zanCount);
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
