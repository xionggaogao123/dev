package com.pojo.smalllesson;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * 复兰互动小课堂课程表
 * Created by James on 2017/9/26.
 * id
 * name         课程名           nam
 * dateTime     上课日期         dtm
 * userId       老师用户id       uid
 * imageUrl     二维码           img
 * code         六位数字         cod
 * type         是否上课中       typ  ( type=0(活跃) type=1(已结束))
 * nodeTime     持续时间         ntm  ( 分钟)
 * isRemove     是否删除         isr
 * createTime    时间改变        ctm
 * 
 *
 */
public class SmallLessonEntry extends BaseDBObject {
    public SmallLessonEntry(){

    }
    public SmallLessonEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }
    //添加构造
    public SmallLessonEntry(
            String name,
            ObjectId userId,
            String imageUrl,
            String code,
            int type,
            int nodeTime
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("nam",name)
                .append("dtm", new Date().getTime())
                .append("ctm", new Date().getTime())
                .append("uid", userId)
                .append("img",imageUrl)
                .append("cod",code)
                .append("typ", type)
                .append("ntm", nodeTime)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public SmallLessonEntry(
            ObjectId id,
            String name,
            ObjectId userId,
            String imageUrl,
            String code,
            int type,
            int nodeTime
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID, id)
                .append("nam", name)
                .append("dtm", new Date().getTime())
                .append("ctm", new Date().getTime())
                .append("uid", userId)
                .append("img", imageUrl)
                .append("cod",code)
                .append("typ", type)
                .append("ntm",nodeTime)
                .append("isr", 0);
        setBaseEntry(dbObject);
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

    public String getImageUrl(){
        return getSimpleStringValue("img");
    }

    public void setImageUrl(String imageUrl){
        setSimpleValue("img",imageUrl);
    }
    public String getCode(){
        return getSimpleStringValue("cod");
    }

    public void setCode(String code){
        setSimpleValue("cod",code);
    }



    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }
    public int getNodeTime(){
        return getSimpleIntegerValue("ntm");
    }

    public void setNodeTime(int nodeTime){
        setSimpleValue("ntm",nodeTime);
    }

    public long getDateTime(){
        return getSimpleLongValue("dtm");
    }
    public void setDateTime(long dateTime){
        setSimpleValue("dtm",dateTime);
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
