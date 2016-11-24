package com.pojo.exam;

import com.mongodb.BasicDBObject;
import com.pojo.base.BaseDBObject;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

/**
 * Created by Caocui on 2015/7/30.
 * 考试科目成绩对象，依附于ScoreEntry对象
 * 对应数据库集合
 * 成绩类型:st  0:正常 1：缺考  2：免考
 * <p/>
 * 被添加
 * subId:科目id ObjectId
 * subNm:科目名称 String
 * subS:科目成绩 Integer 未打分时为null
 * full:满分 Integer
 * fail:及格分 Integer
 * abs:缺考 Integer 0：未缺考  1：缺考
 * exemp:免考 Integer 0:未免考 1：免考
 */
public class SubjectScoreEntry extends BaseDBObject {
    public SubjectScoreEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public SubjectScoreEntry(ObjectId subjectId, String subjectName, float score, int showType, float full, float fail, Integer abs, Integer exemp) {
        super();
        BasicDBObject baseEntry = new BasicDBObject()
                .append("subId", subjectId)
                .append("subNm", subjectName)
                .append("subS", score)
                .append("st", showType)
                .append("full", full)
                .append("fail", fail)
                .append("abs", 0)
                .append("exemp", 0)
                .append(Constant.ID, new ObjectId());
        setBaseEntry(baseEntry);
    }

    public void setSubjectId(ObjectId subjectId) {
        setSimpleValue("subId", subjectId);
    }

    public ObjectId getSubjectId() {
        return getSimpleObjecIDValue("subId");
    }

    public void setScore(float score) {
        setSimpleValue("subS", score);
    }

    public double getScore() {
        try {
            return getSimpleDoubleValue("subS");
        } catch (NullPointerException e) {
            return  0;
        }
    }

    public void setShowType(int showType) {
        setSimpleValue("st", showType);
    }

    public int getShowType() {
        return getSimpleIntegerValueDef("st", 1);
    }
    
    
    public String getSubjectName(){
    	return getSimpleStringValue("subNm");
    }
    
    public void setSubjectName(String subjectName){
    	setSimpleValue("subNm", subjectName );
    }

    public void setFull(float full) {
        setSimpleValue("full", full);
    }

    public double getFull() {
        return getSimpleDoubleValue("full");
    }


}
