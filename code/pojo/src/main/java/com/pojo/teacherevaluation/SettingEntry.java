package com.pojo.teacherevaluation;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2016/4/19.
 * 教师评价设置  包含  等第设置 评分时间 评比规则  te_setting
 * si 学校id
 * y  年度 例如2015-2016年度  保存为2015-2016
 * rule 评比规则
 * ptb 个人陈述时间开始
 * pte 个人陈述时间结束
 * gtb 组内互评时间开始
 * gte 组内互评时间结束
 * etb 考核小组打分时间开始
 * ete 考核小组打分时间结束
 * grd  等第 List<GradeSetting>
 */
public class SettingEntry extends BaseDBObject {

    public SettingEntry(){}

    public SettingEntry(ObjectId schoolId, String year){
        this(schoolId, year, "", 0, 0, 0, 0, 0, 0, new ArrayList<GradeSetting>());
    }

    public SettingEntry(BasicDBObject baseEntry){
        setBaseEntry(baseEntry);
    }

    public SettingEntry(ObjectId schoolId, String year, String rule,
                        long personalTimeBegin, long groupTimeBegin, long evaluationTimeBegin,
                        long personalTimeEnd, long groupTimeEnd, long evaluationTimeEnd,
                        List<GradeSetting> gradeSettings){
        BasicDBObject baseEntry = new BasicDBObject()
                .append("si", schoolId)
                .append("y", year)
                .append("rule", rule)
                .append("ptb", personalTimeBegin)
                .append("pte", personalTimeEnd)
                .append("gpb", groupTimeBegin)
                .append("gpe", groupTimeEnd)
                .append("etb", evaluationTimeBegin)
                .append("ete", evaluationTimeEnd)
                .append("grd", MongoUtils.convert(MongoUtils.fetchDBObjectList(gradeSettings)))
                ;
        setBaseEntry(baseEntry);
    }

    public ObjectId getSchoolId(){
        return getSimpleObjecIDValue("si");
    }

    public void setSchoolId(ObjectId schoolId){
        setSimpleValue("si", schoolId);
    }

    public String getYear(){
        return getSimpleStringValue("y");
    }

    public void setYear(String year){
        setSimpleValue("y", year);
    }

    public String getRule(){
        return getSimpleStringValue("rule");
    }

    public void setRule(String rule){
        setSimpleValue("rule", rule);
    }

    public long getPersonalTimeBegin(){
        return getSimpleLongValue("ptb");
    }

    public void setPersonalTimeBegin(long personalTimeBegin){
        setSimpleValue("ptb", personalTimeBegin);
    }

    public long getPersonalTimeEnd(){
        return getSimpleLongValue("pte");
    }

    public void setPersonalTimeEnd(long personalTimeEnd){
        setSimpleValue("pte", personalTimeEnd);
    }

    public long getGroupTimeBegin(){
        return getSimpleLongValue("gpb");
    }

    public void setGroupTimeBegin(long groupTimeBegin){
        setSimpleValue("gpb", groupTimeBegin);
    }

    public long getGroupTimeEnd(){
        return getSimpleLongValue("gpe");
    }

    public void setGroupTimeEnd(long groupTimeEnd){
        setSimpleValue("gpe", groupTimeEnd);
    }

    public long getEvaluationTimeBegin(){
        return getSimpleLongValue("etb");
    }

    public void setEvaluationTimeBegin(long evaluationTimeBegin){
        setSimpleValue("etb", evaluationTimeBegin);
    }

    public long getEvaluationTimeEnd(){
        return getSimpleLongValue("ete");
    }

    public void setEvaluationTimeEnd(long evaluationTimeEnd){
        setSimpleValue("ete", evaluationTimeEnd);
    }

    public List<GradeSetting> getGradeSettings(){
        List<GradeSetting> retList = new ArrayList<GradeSetting>();
        BasicDBList list = (BasicDBList)getSimpleObjectValue("grd");
        if(null != list && !list.isEmpty()){
            for(Object o : list){
                retList.add(new GradeSetting((BasicDBObject)o));
            }
        }
        return retList;
    }

    public void setGradeSettings(List<GradeSetting> gradeSettings){
        setSimpleValue("grd", MongoUtils.convert(MongoUtils.fetchDBObjectList(gradeSettings)));
    }




    /**
     * 等第设置
     * id
     * nm
     * bg 开始分数 单位%
     * ed 结束分数 单位%
     */
    public static class GradeSetting extends BaseDBObject{
        public GradeSetting(){}

        public GradeSetting(BasicDBObject baseEntry){
            setBaseEntry(baseEntry);
        }

        public GradeSetting(ObjectId id, String name, double begin, double end){
            BasicDBObject baseEntry = new BasicDBObject()
                    .append("id", id)
                    .append("nm", name)
                    .append("bg", begin)
                    .append("ed", end)
                    ;
            setBaseEntry(baseEntry);
        }

        public ObjectId getId(){
            return getSimpleObjecIDValue("id");
        }

        public void setId(ObjectId id){
            setSimpleValue("id", id);
        }

        public String getName(){
            return getSimpleStringValue("nm");
        }

        public void setName(String name){
            setSimpleValue("nm", name);
        }

        public double getBegin(){
            return getSimpleDoubleValue("bg");
        }

        public void setBegin(double begin){
            setSimpleValue("bg", begin);
        }

        public double getEnd(){
            return getSimpleDoubleValue("ed");
        }

        public void setEnd(double end){
            setSimpleValue("ed", end);
        }
    }
}
