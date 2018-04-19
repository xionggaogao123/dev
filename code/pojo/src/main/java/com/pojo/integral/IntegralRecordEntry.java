package com.pojo.integral;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-04-16.
 * 新-----积分经验值的记录表
 * id                        id
 * userId                    uid                    用户id
 * score                     sco                    分值
 * module                    mod                    模块
 * dateTime                  dtm                    日期
 * createTime                ctm                    创建时间
 * sort                      sor                    顺序
 */
public class IntegralRecordEntry extends BaseDBObject {

    public IntegralRecordEntry(){

    }

    public IntegralRecordEntry(BasicDBObject baseEntry){
        super(baseEntry);
    }
    //添加构造
    public IntegralRecordEntry(
            ObjectId userId,
            int score,
            String module,
            long dateTime,
            long createTime,
            int sort
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("uid",userId)
                .append("sco", score)
                .append("mod",module)
                .append("dtm", dateTime)
                .append("ctm", createTime)
                .append("sor", sort)
                .append("isr", Constant.ZERO);
        setBaseEntry(dbObject);
    }

    //修改构造
    public IntegralRecordEntry(
            ObjectId id,
            ObjectId userId,
            int score,
            String module,
            long dateTime,
            long createTime,
            int sort
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID, id)
                .append("uid",userId)
                .append("sco", score)
                .append("mod",module)
                .append("dtm", dateTime)
                .append("ctm", createTime)
                .append("sor", sort)
                .append("isr", Constant.ZERO);
        setBaseEntry(dbObject);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public int getScore(){
        return getSimpleIntegerValue("sco");
    }

    public void setScore(int score){
        setSimpleValue("sco",score);
    }

    public String getDateTime(){
        return getSimpleStringValue("dtm");
    }

    public void setDateTime(String dateTime){
        setSimpleValue("dtm",dateTime);
    }

    public String getCreateTime(){
        return getSimpleStringValue("ctm");
    }

    public void setCreateTime(String createTime){
        setSimpleValue("ctm",createTime);
    }


    public String getModule(){
        return getSimpleStringValue("mod");
    }

    public void setModule(String module){
        setSimpleValue("mod",module);
    }


    public void setSort(int sort){
        setSimpleValue("sor",sort);
    }

    public int getSort(){
        return getSimpleIntegerValue("sor");
    }

    public void setIsRemove(int isRemove){
        setSimpleValue("isr",isRemove);
    }
    public int getIsRemove(){
        return getSimpleIntegerValue("isr");
    }



}
