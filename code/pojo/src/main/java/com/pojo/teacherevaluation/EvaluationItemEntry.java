package com.pojo.teacherevaluation;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.app.IdNameValuePair;
import com.pojo.app.IdValuePair;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fl on 2016/4/19.
 * 考核详情  te_item
 * evid  评价id  对应MemberGroupEntry _id  2016.7.29新增
 * tid 被考核老师id
 * stat 个人陈述
 * evi  实证材料
 * hps  互评成绩
 * lds  考核小组领导成绩
 * gps  考核小组成员成绩
 * fhp  最终互评成绩 即 互评成绩平均分
 * fld  最终领导成绩 即 领导成绩平均分
 * fgp  最终成员成绩 即 成员成绩平均分
 * fs   最终成绩
 * fss  标准化成绩
 * rk   排名
 */
public class EvaluationItemEntry extends BaseDBObject {

    public EvaluationItemEntry(){}

    public EvaluationItemEntry(ObjectId evaluationId, ObjectId teacherId){
        this(evaluationId, teacherId, "", "", new ArrayList<ElementScore>(), new ArrayList<ElementScore>(), new ArrayList<ElementScore>(),
                0, 0, 0, 0);
    }

    public EvaluationItemEntry(BasicDBObject baseEntry){
        setBaseEntry(baseEntry);
    }

    public EvaluationItemEntry(ObjectId evaluationId, ObjectId teacherId, String statement, String evidence,
                               List<ElementScore> huPingScore, List<ElementScore> leaderScore, List<ElementScore> groupScore,
                               double finalHuPingScore, double finalLeaderScore, double finalGroupScore, double finalScore){
        BasicDBObject baseEntry = new BasicDBObject()
                .append("evid", evaluationId)
                .append("tid", teacherId)
                .append("stat", statement)
                .append("evi", evidence)
                .append("hps", MongoUtils.convert(MongoUtils.fetchDBObjectList(huPingScore)))
                .append("lds", MongoUtils.convert(MongoUtils.fetchDBObjectList(leaderScore)))
                .append("gps", MongoUtils.convert(MongoUtils.fetchDBObjectList(groupScore)))
                .append("fhp", finalHuPingScore)
                .append("fld", finalLeaderScore)
                .append("fgp", finalGroupScore)
                .append("fs", finalScore)
                .append("fss", 0.0)
                .append("rk", 0)
                ;
        setBaseEntry(baseEntry);

    }

    public ObjectId getEvaluationId(){
        return getSimpleObjecIDValue("evid");
    }

    public void setEvaluationId(ObjectId evaluationId){
        setSimpleValue("evid", evaluationId);
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






    /**
     * 考核元素打分详情
     * id  打分老师id
     * tj  是否提交 1提交  0未提交（保存状态，不计入最后总分计算） 缺省为1  20160919新增
     * scs 考核元素打分列表
     * [id: 考核元素id, v:分数] 20160919废弃
     * [id: 考核元素id, nm:等级名称，v:分数] 20160919新增 nm等级模式下使用， v打分模式下使用
     */
    public static class ElementScore extends BaseDBObject{
        public ElementScore(){}

        public ElementScore(BasicDBObject baseEntry){
            setBaseEntry(baseEntry);
        }

        public ElementScore(ObjectId id, int tijiao, List<IdNameValuePair> scores){
            BasicDBObject baseEntry = new BasicDBObject()
                    .append("id", id)
                    .append("tj", tijiao)
                    .append("scs", MongoUtils.convert(MongoUtils.fetchDBObjectList(scores)));
            setBaseEntry(baseEntry);
        }

        public ObjectId getEvaluateTeacherId(){
            return getSimpleObjecIDValue("id");
        }

        public void setEvaluateTeacherId(ObjectId evaluateTeacherId){
            setSimpleValue("id", evaluateTeacherId);
        }

        public int getTijiao(){
            return getSimpleIntegerValueDef("tj", 1);
        }

        public void setTijiao(int tijiao){
            setSimpleValue("tj", tijiao);
        }

        public List<IdNameValuePair> getElementScores(){
            List<IdNameValuePair> retList = new ArrayList<IdNameValuePair>();
            BasicDBList list = (BasicDBList)getSimpleObjectValue("scs");
            if(null != list && !list.isEmpty()){
                for(Object o : list){
                    retList.add(new IdNameValuePair((BasicDBObject)o));
                }
            }
            return retList;
        }

        public void setElementScores(List<IdNameValuePair> elementScores){
            setSimpleValue("scs", MongoUtils.convert(MongoUtils.fetchDBObjectList(elementScores)));
        }

        public Double getTotalScore(){
            double totalScore = 0;
            List<IdNameValuePair> elementScores = getElementScores();
            for(IdNameValuePair elementScore : elementScores){
                totalScore += (Double)elementScore.getValue();
            }
            return totalScore;
        }
    }
}
