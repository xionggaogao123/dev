package com.pojo.excellentCourses;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 * Created by James on 2018-04-26.
 * 精品课时表
 * id              id                id
 * parentId        所属课程id        pid
 * content         课时说明          con
 * userId          所属人            uid
 * dateTime        日期              dtm
 * startTime       开始时间          stm
 * currentTime     持续时间          utm
 * classOldPrice   期望收益          cop
 * classNewPrice   系统收益          cnp
 * week            星期              wek
 * isRemove        是否删除          isr
 * order           顺序              ord
 * type            状态              typ     0 未开始     1进行中   2已结束
 */
public class HourClassEntry extends BaseDBObject {



    public HourClassEntry(){

    }

    public HourClassEntry(BasicDBObject object){
        super(object);
    }

    //添加构造
    public HourClassEntry(
            ObjectId userId,
            ObjectId parentId,
            String content,
            long startTime,
            long dateTime,
            int currentTime,
            int classOldPrice,
            int classNewPrice,
            int week,
            int order,
            int type
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid", userId)
                .append("pid", parentId)
                .append("con", content)
                .append("stm", startTime)
                .append("dtm", dateTime)
                .append("utm", currentTime)
                .append("cop", classOldPrice)
                .append("cnp", classNewPrice)
                .append("wek", week)
                .append("ord", order)
                .append("typ", type)
                .append("ctm",new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public HourClassEntry(
            ObjectId id,
            ObjectId userId,
            ObjectId parentId,
            String content,
            long startTime,
            long dateTime,
            int currentTime,
            int classOldPrice,
            int classNewPrice,
            int week,
            int order,
            int type
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID, id)
                .append("uid", userId)
                .append("pid", parentId)
                .append("con", content)
                .append("stm", startTime)
                .append("dtm", dateTime)
                .append("utm", currentTime)
                .append("cop", classOldPrice)
                .append("cnp", classNewPrice)
                .append("wek", week)
                .append("ord", order)
                .append("typ", type)
                .append("ctm",new Date().getTime())
                .append("isr", 0);
        setBaseEntry(dbObject);
    }
    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }
    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }
    public ObjectId getParentId(){
        return getSimpleObjecIDValue("pid");
    }
    public void setParentId(ObjectId parentId){
        setSimpleValue("pid",parentId);
    }
    public String getContent(){
        return getSimpleStringValue("con");
    }
    public void setContent(String content){
        setSimpleValue("con", content);
    }

    public int getCurrentTime(){
        return getSimpleIntegerValue("utm");
    }

    public void setCurrentTime(int currentTime){
        setSimpleValue("utm",currentTime);
    }

    public long getStartTime(){
        return getSimpleLongValue("stm");
    }

    public void setStartTime(long startTime){
        setSimpleValue("stm",startTime);
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


    public int getClassOldPrice(){
        return getSimpleIntegerValue("cop");
    }

    public void setClassOldPrice(int classOldPrice){
        setSimpleValue("cop",classOldPrice);
    }

    public int getClassNewPrice(){
        return getSimpleIntegerValue("cnp");
    }

    public void setClassNewPrice(int classNewPrice){
        setSimpleValue("cnp",classNewPrice);
    }
    public int getWeek(){
        return getSimpleIntegerValue("wek");
    }

    public void setWeek(int week){
        setSimpleValue("wek",week);
    }
    public int getType(){
        return getSimpleIntegerValue("typ");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }

    public int getOrder(){
        return getSimpleIntegerValue("ord");
    }

    public void setOrder(int order){
        setSimpleValue("ord",order);
    }

    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }

}
