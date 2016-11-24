package com.pojo.teacherevaluation;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.app.IdValuePair;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2016/4/19.
 * 考核详情  te_item
 * si 学校id
 * y  年度 例如2015-2016年度  保存为2015-2016
 * tid 被考核老师id
 * stat 个人陈述
 * evi  实证材料
 * lhs  量化成绩
 * hps  互评成绩
 * lds  考核小组领导成绩
 * gps  考核小组成员成绩
 * flh  最终量化成绩 即 量化成绩平均分
 * fhp  最终互评成绩 即 互评成绩平均分
 * fld  最终领导成绩 即 领导成绩平均分
 * fgp  最终成员成绩 即 成员成绩平均分
 * fs   最终成绩
 * fss  标准化成绩
 * rk   排名
 */
public class EvaluationItemEntry extends BaseDBObject {

    public EvaluationItemEntry(){}

    public EvaluationItemEntry(ObjectId schoolId, String year, ObjectId teacherId){
        this(schoolId, year, teacherId, "", "", new ArrayList<ElementScore>(), new ArrayList<ElementScore>(), new ArrayList<ElementScore>(), new ArrayList<ElementScore>(),
                0, 0, 0, 0, 0);
    }

    public EvaluationItemEntry(BasicDBObject baseEntry){
        setBaseEntry(baseEntry);
    }

    public EvaluationItemEntry(ObjectId schoolId, String year, ObjectId teacherId, String statement, String evidence,
                               List<ElementScore> liangHuaScore, List<ElementScore> huPingScore, List<ElementScore> leaderScore, List<ElementScore> groupScore,
                               double finalLiangHuaScore, double finalHuPingScore, double finalLeaderScore, double finalGroupScore, double finalScore){
        BasicDBObject baseEntry = new BasicDBObject()
                .append("si", schoolId)
                .append("y", year)
                .append("tid", teacherId)
                .append("stat", statement)
                .append("evi", evidence)
                .append("lhs", MongoUtils.convert(MongoUtils.fetchDBObjectList(liangHuaScore)))
                .append("hps", MongoUtils.convert(MongoUtils.fetchDBObjectList(huPingScore)))
                .append("lds", MongoUtils.convert(MongoUtils.fetchDBObjectList(leaderScore)))
                .append("gps", MongoUtils.convert(MongoUtils.fetchDBObjectList(groupScore)))
                .append("flh", finalLiangHuaScore)
                .append("fhp", finalHuPingScore)
                .append("fld", finalLeaderScore)
                .append("fgp", finalGroupScore)
                .append("fs", finalScore)
                .append("fss", 0.0)
                .append("rk", 0)
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

    public ObjectId getTeacherId(){
        return getSimpleObjecIDValue("tid");
    }

    public void setTeacherId(ObjectId teacherId){
        setSimpleValue("tid", teacherId);
    }

    public String getStatement(){
        return getSimpleStringValue("stat");
    }

    public void setStatement(String statement){
        setSimpleValue("stat", statement);
    }

    public String getEvidence(){
        return getSimpleStringValue("evi");
    }

    public void setEvidence(String evidence){
        setSimpleValue("evi", evidence);
    }

    public double getFinalLiangHuaScore(){
        return getSimpleDoubleValue("flh");
    }

    public void setFinalLiangHuaScore(double finalLiangHuaScore){
        setSimpleValue("flh", finalLiangHuaScore);
    }

    public double getFinalHuPingScore(){
        return getSimpleDoubleValue("fhp");
    }

    public void setFinalHuPingScore(double finalHuPingScore){
        setSimpleValue("fhp", finalHuPingScore);
    }

    public double getFinalLeaderScore(){
        return getSimpleDoubleValue("fld");
    }

    public void setFinalLeaderScore(double finalLeaderScore){
        setSimpleValue("fld", finalLeaderScore);
    }

    public double getFinalGroupScore(){
        return getSimpleDoubleValue("fgp");
    }

    public void setFinalGroupScore(double finalGroupScore){
        setSimpleValue("fgp", finalGroupScore);
    }

    public double getFinalScore(){
        return getSimpleDoubleValue("fs");
    }

    public void setFinalScore(double finalScore){
        setSimpleValue("fs", finalScore);
    }

    public double getFinalStdScore(){
        return getSimpleDoubleValue("fss");
    }

    public void setFinalStdScore(double finalStdScore){
        setSimpleValue("fss", finalStdScore);
    }

    public int getRank(){
        return getSimpleIntegerValue("rk");
    }

    public void setRank(int rank){
        setSimpleValue("rk", rank);
    }

    public List<ElementScore> getGroupScore(){
        List<ElementScore> retList = new ArrayList<ElementScore>();
        BasicDBList list = (BasicDBList)getSimpleObjectValue("gps");
        if(null != list && !list.isEmpty()){
            for(Object o : list){
                retList.add(new ElementScore((BasicDBObject)o));
            }
        }
        return retList;
    }

    public void setGroupScore(List<ElementScore> groupScore){
        setSimpleValue("gps", MongoUtils.convert(MongoUtils.fetchDBObjectList(groupScore)));
    }

    public List<ElementScore> getHuPingScore(){
        List<ElementScore> retList = new ArrayList<ElementScore>();
        BasicDBList list = (BasicDBList)getSimpleObjectValue("hps");
        if(null != list && !list.isEmpty()){
            for(Object o : list){
                retList.add(new ElementScore((BasicDBObject)o));
            }
        }
        return retList;
    }

    public void setHuPingScore(List<ElementScore> huPingScore){
        setSimpleValue("hps", MongoUtils.convert(MongoUtils.fetchDBObjectList(huPingScore)));
    }

    public List<ElementScore> getLeaderScore(){
        List<ElementScore> retList = new ArrayList<ElementScore>();
        BasicDBList list = (BasicDBList)getSimpleObjectValue("lds");
        if(null != list && !list.isEmpty()){
            for(Object o : list){
                retList.add(new ElementScore((BasicDBObject)o));
            }
        }
        return retList;
    }

    public void setLeaderScore(List<ElementScore> leaderScore){
        setSimpleValue("lds", MongoUtils.convert(MongoUtils.fetchDBObjectList(leaderScore)));
    }

    public List<ElementScore> getLiangHuaScore(){
        List<ElementScore> retList = new ArrayList<ElementScore>();
        BasicDBList list = (BasicDBList)getSimpleObjectValue("lhs");
        if(null != list && !list.isEmpty()){
            for(Object o : list){
                retList.add(new ElementScore((BasicDBObject)o));
            }
        }
        return retList;
    }

    public void setLiangHuaScore(List<ElementScore> liangHuaScore){
        setSimpleValue("lhs", MongoUtils.convert(MongoUtils.fetchDBObjectList(liangHuaScore)));
    }






    /**
     * 考核元素打分详情
     * id  打分老师id
     * scs 考核元素打分列表
     * [id: 考核元素id, v:分数]
     */
    public static class ElementScore extends BaseDBObject{
        public ElementScore(){}

        public ElementScore(BasicDBObject baseEntry){
            setBaseEntry(baseEntry);
        }

        public ElementScore(ObjectId id, List<IdValuePair> scores){
            BasicDBObject baseEntry = new BasicDBObject()
                    .append("id", id)
                    .append("scs", MongoUtils.convert(MongoUtils.fetchDBObjectList(scores)));
            setBaseEntry(baseEntry);
        }

        public ObjectId getEvaluateTeacherId(){
            return getSimpleObjecIDValue("id");
        }

        public void setEvaluateTeacherId(ObjectId evaluateTeacherId){
            setSimpleValue("id", evaluateTeacherId);
        }

        public List<IdValuePair> getElementScores(){
            List<IdValuePair> retList = new ArrayList<IdValuePair>();
            BasicDBList list = (BasicDBList)getSimpleObjectValue("scs");
            if(null != list && !list.isEmpty()){
                for(Object o : list){
                    retList.add(new IdValuePair((BasicDBObject)o));
                }
            }
            return retList;
        }

        public void setElementScores(List<IdValuePair> elementScores){
            setSimpleValue("scs", MongoUtils.convert(MongoUtils.fetchDBObjectList(elementScores)));
        }
    }
}
