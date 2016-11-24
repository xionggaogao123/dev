package com.pojo.teacherevaluation;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.app.NameValuePair;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2016/4/19.
 * 教师评价设置  包含  等第设置 评分时间 评比规则 考核模式  te_setting
 * si 学校id
 * y  年度 例如2015-2016年度  保存为2015-2016
 * evid  评价id  对应MemberGroupEntry _id  2016.7.29新增
 * rule 评比规则
 * ptb 个人陈述时间开始 //20160912废弃
 * pte 个人陈述时间结束 //20160912废弃
 * gtb 组内互评时间开始 //20160912废弃
 * gte 组内互评时间结束 //20160912废弃
 * etb 考核打分时间开始
 * ete 考核打分时间结束
 * mode 考核模式  1：打分模式   2：等级模式  缺省为1  20160919新增
 * mgs 等级模式下等级设置  List<NameValuePair> 20160919新增
 * grd  等第 List<GradeSetting>
 */
public class SettingEntry extends BaseDBObject {

    public SettingEntry(){}

    public SettingEntry(ObjectId schoolId, String year, ObjectId evaluationId){
        this(schoolId, year, evaluationId, "", 0, 0, new ArrayList<GradeSetting>(), 1, new ArrayList<NameValuePair>());
    }

    public SettingEntry(BasicDBObject baseEntry){
        setBaseEntry(baseEntry);
    }

    public SettingEntry(ObjectId schoolId, String year, ObjectId evaluationId, String rule,
                        long evaluationTimeBegin,long evaluationTimeEnd, List<GradeSetting> gradeSettings, int mode, List<NameValuePair> modeGrades){
        BasicDBObject baseEntry = new BasicDBObject()
                .append("si", schoolId)
                .append("y", year)
                .append("evid", evaluationId)
                .append("rule", rule)
                .append("etb", evaluationTimeBegin)
                .append("ete", evaluationTimeEnd)
                .append("grd", MongoUtils.convert(MongoUtils.fetchDBObjectList(gradeSettings)))
                .append("mode", mode)
                .append("mgs", MongoUtils.convert(MongoUtils.fetchDBObjectList(modeGrades)))
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

    public ObjectId getEvaluationId(){
        return getSimpleObjecIDValue("evid");
    }

    public void setEvaluationId(ObjectId evaluationId){
        setSimpleValue("evid", evaluationId);
    }

    public String getRule(){
        return getSimpleStringValue("rule");
    }

    public void setRule(String rule){
        setSimpleValue("rule", rule);
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

    public int getMode(){
        return getSimpleIntegerValueDef("mode", 1);
    }

    public void setMode(int mode){
        setSimpleValue("mode", mode);
    }

    public List<NameValuePair> getModeGrades(){
        List<NameValuePair> retList = new ArrayList<NameValuePair>();
        BasicDBList list = (BasicDBList)getSimpleObjectValue("mgs");
        if(null != list && !list.isEmpty()){
            for(Object o : list){
                retList.add(new NameValuePair((BasicDBObject)o));
            }
        }
        return retList;
    }

    public void setModeGrades(List<NameValuePair> modeGrades){
        setSimpleValue("mgs", MongoUtils.convert(MongoUtils.fetchDBObjectList(modeGrades)));
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
