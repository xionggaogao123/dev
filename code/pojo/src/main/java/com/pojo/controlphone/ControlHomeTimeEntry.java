package com.pojo.controlphone;

/**
 * Created by James on 2018-11-16.
 */

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 *      家管控时间设置
 *         id             id
 *         type           类型                   1 常规   2 特殊
 *         week           星期                    （1-7）
 *         dateFrom       日期从                     特殊日期
 *         dateTo       日期到                     特殊日期
 *         schoolTimeFrom      上学开始时间
 *         schoolTimeTo        	上学结束时间
 *         bedTimeFrom      睡眠开始时间
 *         bedTimeTo        	睡眠结束时间
 *         parentId		家长id
 *         sonId        孩子id
 *         hour         hour (防沉迷时间)
 *         holidayName  假期名称
 * */
public class ControlHomeTimeEntry extends BaseDBObject {

    public ControlHomeTimeEntry() {
    }

    public ControlHomeTimeEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }
    public ControlHomeTimeEntry(DBObject dbObject) {
        setBaseEntry((BasicDBObject) dbObject);
    }

    //新增构造
    public ControlHomeTimeEntry(
            int type,
            int week,
            String dateFrom,
            String dateTo,
            String schoolTimeFrom,
            String schoolTimeTo,
            String bedTimeFrom,
            String bedTimeTo,
            ObjectId parentId,
            ObjectId userId,
            String holidayName,
            long hour
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append("type", type)
                .append("week", week)
                .append("dateFrom", dateFrom)
                .append("dateTo", dateTo)
                .append("schoolTimeFrom", schoolTimeFrom)
                .append("schoolTimeTo", schoolTimeTo)
                .append("bedTimeFrom", bedTimeFrom)
                .append("bedTimeTo", bedTimeTo)
                .append("pid", parentId)
                .append("uid", userId)
                .append("holidayName", holidayName)
                .append("hou",hour)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public ControlHomeTimeEntry(
            ObjectId id,
            int type,
            int week,
            String dateFrom,
            String dateTo,
            String schoolTimeFrom,
            String schoolTimeTo,
            String bedTimeFrom,
            String bedTimeTo,
            ObjectId parentId,
            ObjectId userId,
            String holidayName,
            long hour
    ){
        BasicDBObject dbObject=new BasicDBObject()
                .append(Constant.ID, id)
                .append("type", type)
                .append("week", week)
                .append("dateFrom", dateFrom)
                .append("dateTo", dateTo)
                .append("schoolTimeFrom", schoolTimeFrom)
                .append("schoolTimeTo", schoolTimeTo)
                .append("bedTimeFrom", bedTimeFrom)
                .append("bedTimeTo", bedTimeTo)
                .append("pid", parentId)
                .append("uid", userId)
                .append("holidayName", holidayName)
                .append("hou",hour)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    public long getHour(){
        return getSimpleLongValueDef("hou",1800000);
    }
    public void setHour(long hour){
        setSimpleValue("hou",hour);
    }

    public int getType(){
        return getSimpleIntegerValue("type");
    }
    public void setType(int type){
        setSimpleValue("type",type);
    }
    public int getWeek(){
        return getSimpleIntegerValue("week");
    }
    public void setWeek(int week){
        setSimpleValue("week",week);
    }

    public String getDateFrom(){
        return getSimpleStringValue("dateFrom");
    }
    public void setDateFrom(String dateFrom){
        setSimpleValue("dateFrom",dateFrom);

    }
    public String getDateTo(){
        return getSimpleStringValue("dateTo");
    }
    public void setDateTo(String dateTo){
        setSimpleValue("dateTo",dateTo);

    }
    public String getSchoolTimeFrom(){
        return getSimpleStringValue("schoolTimeFrom");
    }
    public void setSchoolTimeFrom(String schoolTimeFrom){
        setSimpleValue("schoolTimeFrom",schoolTimeFrom);

    }
    public String getSchoolTimeTo(){
        return getSimpleStringValue("schoolTimeTo");
    }
    public void setSchoolTimeTo(String schoolTimeTo){
        setSimpleValue("schoolTimeTo",schoolTimeTo);

    }
    public String getBedTimeFrom(){
        return getSimpleStringValue("bedTimeFrom");
    }
    public void setBedTimeFrom(String bedTimeFrom){
        setSimpleValue("bedTimeFrom",bedTimeFrom);

    }
    public String getBedTimeTo(){
        return getSimpleStringValue("bedTimeTo");
    }
    public void setBedTimeTo(String bedTimeTo){
        setSimpleValue("bedTimeTo",bedTimeTo);

    }
    public ObjectId getParentId(){
        return getSimpleObjecIDValue("pid");
    }
    public String getHolidayName(){
        return getSimpleStringValue("holidayName");
    }
    public void setHolidayName(String holidayName){
        setSimpleValue("holidayName",holidayName);

    }
    public void setParentId(ObjectId parentId){
        setSimpleValue("pid",parentId);
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setIsr(int isr){
        setSimpleValue("isr",isr);
    }
}
