package com.pojo.backstage;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * @Auther: taotao.chan
 * @Date: 2018/9/27 14:46
 * @Description:
 */
/**
 *         id             id
 *         type           类型                   1 常规   2 特殊
 *         week           星期                    （1-7）
 *         dateFrom       日期从                     特殊日期
 *         dateTo       日期到                     特殊日期
 *         schoolTimeFrom      上学开始时间
 *         schoolTimeTo        	上学结束时间
 *         bedTimeFrom      睡眠开始时间
 *         bedTimeTo        	睡眠结束时间
 *         schoolId		学校id
 *         holidayName  假期名称
 * */
public class SchoolControlTimeEntry extends BaseDBObject {
    public SchoolControlTimeEntry() {
    }

    public SchoolControlTimeEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }
    public SchoolControlTimeEntry(DBObject dbObject) {
        setBaseEntry((BasicDBObject) dbObject);
    }

    //新增构造
    public SchoolControlTimeEntry(
            int type,
            int week,
            String dateFrom,
            String dateTo,
            String schoolTimeFrom,
            String schoolTimeTo,
            String bedTimeFrom,
            String bedTimeTo,
            ObjectId schoolId,
            String holidayName
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
                .append("schoolId", schoolId)
                .append("holidayName", holidayName)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    //修改构造
    public SchoolControlTimeEntry(
            ObjectId id,
            int type,
            int week,
            String dateFrom,
            String dateTo,
            String schoolTimeFrom,
            String schoolTimeTo,
            String bedTimeFrom,
            String bedTimeTo,
            ObjectId schoolId,
            String holidayName
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
                .append("schoolId", schoolId)
                .append("holidayName", holidayName)
                .append("isr", 0);
        setBaseEntry(dbObject);
    }

    public int getType(){
        return getSimpleIntegerValue("type");
    }
    public int getWeek(){
        return getSimpleIntegerValue("week");
    }

    public String getDateFrom(){
        return getSimpleStringValue("dateFrom");
    }
    public String getDateTo(){
        return getSimpleStringValue("dateTo");
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
    public String getSchoolId(){
        return getSimpleObjecIDValue("schoolId") == null ? "": getSimpleObjecIDValue("schoolId").toString();
    }
    public String getHolidayName(){
        return getSimpleStringValue("holidayName");
    }
}
