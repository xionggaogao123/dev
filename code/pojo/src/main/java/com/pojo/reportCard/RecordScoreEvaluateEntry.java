package com.pojo.reportCard;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by scott on 2017/9/29.
 * {
 *     groupExamDetailId:考试Id
 *     excellentPercent:优秀率
 *     qualifyPercent:合格率
 *     unQualifyPercent:不合格率
 *     avgScore:平均分
 *     maxScore:最高分
 *     minScore:最低分
 * }
 */
public class RecordScoreEvaluateEntry extends BaseDBObject {

    public RecordScoreEvaluateEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject)dbObject);
    }

    public RecordScoreEvaluateEntry(ObjectId groupExamDetailId,
                                    double excellentPercent,
                                    double qualifyPercent,
                                    double unQualifyPercent,
                                    double avgScore,
                                    double maxScore,
                                    double minScore){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("eid",groupExamDetailId)
                .append("ep",excellentPercent)
                .append("qp",qualifyPercent)
                .append("uqp",unQualifyPercent)
                .append("as",avgScore)
                .append("mxs",maxScore)
                .append("mis",minScore)
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }
    
    public RecordScoreEvaluateEntry(ObjectId groupExamDetailId,
                                    String excellentPercentStr,
                                    String qualifyPercentStr,
                                    String unQualifyPercentStr,
                                    String avgScoreStr,
                                    String maxScoreStr,
                                    String minScoreStr){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("eid",groupExamDetailId)
                .append("eps",excellentPercentStr)
                .append("qps",qualifyPercentStr)
                .append("uqps",unQualifyPercentStr)
                .append("ass",avgScoreStr)
                .append("mxss",maxScoreStr)
                .append("miss",minScoreStr)
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public void setMinScore(double minScore){
        setSimpleValue("mis",minScore);
    }

    public double getMinScore(){
        return getSimpleDoubleValue("mis");
    }
    
    public void setMinScoreStr(String minScoreStr){
        setSimpleValue("miss",minScoreStr);
    }

    public String getMinScoreStr(){
        return getSimpleStringValue("miss");
    }

    public void setMaxScore(double maxScore){
        setSimpleValue("mxs",maxScore);
    }

    public double getMaxScore(){
        return getSimpleIntegerValue("mxs");
    }
    
    public void setMaxScoreStr(String maxScoreStr){
        setSimpleValue("mxss",maxScoreStr);
    }

    public String getMaxScoreStr(){
        return getSimpleStringValue("mxss");
    }

    public void setAvgScore(double avgScore){
        setSimpleValue("as",avgScore);
    }

    public double getAvgScore(){
        return getSimpleDoubleValue("as");
    }
    
    public void setAvgScoreStr(String avgScoreStr){
        setSimpleValue("ass",avgScoreStr);
    }

    public String getAvgScoreStr(){
        return getSimpleStringValue("ass");
    }

    public void setUnQualifyPercent(double unQualifyPercent){
        setSimpleValue("uqp",unQualifyPercent);
    }

    public double getUnQualifyPercent(){
        return getSimpleDoubleValue("uqp");
    }
    
    public void setUnQualifyPercentStr(String unQualifyPercentStr){
        setSimpleValue("uqps",unQualifyPercentStr);
    }

    public String getUnQualifyPercentStr(){
        return getSimpleStringValue("uqps");
    }

    public void setQualifyPercent(double qualifyPercent){
        setSimpleValue("qp",qualifyPercent);
    }

    public double getQualifyPercent(){
        return getSimpleDoubleValue("qp");
    }
    
    public void setQualifyPercentStr(String qualifyPercentStr){
        setSimpleValue("qps",qualifyPercentStr);
    }

    public String getQualifyPercentStr(){
        return getSimpleStringValue("qps");
    }

    public void setExcellentPercent(double excellentPercent){
        setSimpleValue("ep",excellentPercent);
    }

    public double getExcellentPercent(){
        return getSimpleDoubleValue("ep");
    }
    
    public void setExcellentPercentStr(String excellentPercentStr){
        setSimpleValue("eps",excellentPercentStr);
    }

    public String getExcellentPercentStr(){
        return getSimpleStringValue("eps");
    }

    public void setGroupExamDetailId(ObjectId groupExamDetailId){
        setSimpleValue("eid",groupExamDetailId);
    }

    public ObjectId getGroupExamDetailId(){
        return getSimpleObjecIDValue("eid");
    }

}
