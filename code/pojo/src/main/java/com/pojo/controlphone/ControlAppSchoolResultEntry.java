package com.pojo.controlphone;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by James on 2018-09-27.
 *
 * 校管控内应用用户设置表
 * (以校管控为例)
 *
 * communityId             社群id              cid
 * appId                   应用id              aid
 * packageName             包名                pnm
 * 上课时
 * freeTime                在校自由使用时间    ftm
 * markStartFreeTime       生效时间            mtm
 * 放学后
 * OutSchoolCanUseTime     防沉迷时间          otm
 * OutSchoolRule           星期                osr
 * dateTime                生效日期            dtm
 * controlType             管控类型            ctm      1 受管控  2 不受管控
 *
 * saveTime                记录时间            stm
 * type                    生效类型            typ      0 放学后  1 上课中
 *
 */
public class ControlAppSchoolResultEntry extends BaseDBObject {
    public ControlAppSchoolResultEntry(){

    }
    public ControlAppSchoolResultEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public ControlAppSchoolResultEntry(
            ObjectId appId,
            ObjectId communityId,
            String packageName,
            int controlType,
            long freeTime,
            long outSchoolCanUseTime,
            int outSchoolRule,
            long dateTime,
            long markStartFreeTime,
            long saveTime,
            int type){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("aid",appId)
                .append("cid", communityId)
                .append("pnm",packageName)
                .append("cty", controlType)
                .append("ftm",freeTime)
                .append("otm", outSchoolCanUseTime)
                .append("osr", outSchoolRule)
                .append("dtm", dateTime)
                .append("mtm",markStartFreeTime)
                .append("stm",saveTime)
                .append("typ",type)
                .append("isr", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public ObjectId getAppId(){
        return getSimpleObjecIDValue("aid");
    }

    public void setAppId(ObjectId appId){
        setSimpleValue("aid",appId);
    }

    public ObjectId getCommunityId(){
        return getSimpleObjecIDValue("cid");
    }

    public void setCommunityId(ObjectId communityId){
        setSimpleValue("cid",communityId);
    }

    public void setAppPackageName(String appPackageName){
        setSimpleValue("apn",appPackageName);
    }

    public String getAppPackageName(){
        return getSimpleStringValue("apn");
    }

    public void setFreeTime(long freeTime){
        setSimpleValue("ftm",freeTime);
    }

    public long getFreeTime(){
        return getSimpleLongValue("ftm");
    }

    public void setControlType(int controlType){
        setSimpleValue("cty",controlType);
    }

    public int getControlType(){
        return getSimpleIntegerValue("cty");
    }

    public void setOutSchoolCanUseTime(long outSchoolCanUseTime){
        setSimpleValue("otm",outSchoolCanUseTime);
    }

    public long getOutSchoolCanUseTime(){
        return getSimpleLongValue("otm");
    }

    public void setOutSchoolRule(int outSchoolRule){
        setSimpleValue("osr",outSchoolRule);
    }

    public int getOutSchoolRule(){
        return getSimpleIntegerValue("osr");
    }

    public void setDateTime(long dateTime){
        setSimpleValue("dtm",dateTime);
    }

    public long getDateTime(){
        return getSimpleLongValue("dtm");
    }
    public void setMarkStartFreeTime(long markStartFreeTime){
        setSimpleValue("mtm",markStartFreeTime);
    }

    public long getMarkStartFreeTime(){
        return getSimpleLongValue("mtm");
    }
    public void setSaveTime(long saveTime){
        setSimpleValue("stm",saveTime);
    }

    public long getSaveTime(){
        return getSimpleLongValue("stm");
    }

    public void setType(int type){
        setSimpleValue("typ",type);
    }

    public int getType(){
        return getSimpleIntegerValue("typ");
    }


}
