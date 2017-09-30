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
 *     userId:学生Id
 *     score:学生分值
 *     scoreLevel:学生等第分值 分为 A+:100 A:99 A-:98 B+:97 B:96 B-:95 C+:94 C:93 C-:92 D+:91 D:90 D-:89
 * }
 */
public class RecordExamScoreEntry extends BaseDBObject{

    public RecordExamScoreEntry(DBObject dbObject){
        setBaseEntry((BasicDBObject) dbObject);
    }

    public RecordExamScoreEntry(ObjectId groupExamDetailId,
                                ObjectId userId,
                                double score,
                                int scoreLevel){
        BasicDBObject basicDBObject=new BasicDBObject()
                .append("eid",groupExamDetailId)
                .append("uid",userId)
                .append("sc",score)
                .append("scl",scoreLevel)
                .append("ir", Constant.ZERO);
        setBaseEntry(basicDBObject);
    }

    public void setScoreLevel(int scoreLevel){
        setSimpleValue("scl",scoreLevel);
    }

    public int getScoreLevel(){
        return getSimpleIntegerValue("scl");
    }

    public void setScore(double score){
        setSimpleValue("sc",score);
    }

    public double getScore(){
        return getSimpleIntegerValue("sc");
    }

    public void setUserId(ObjectId userId){
        setSimpleValue("uid",userId);
    }

    public ObjectId getUserId(){
        return getSimpleObjecIDValue("uid");
    }

    public void setGroupExamDetailId(ObjectId groupExamDetailId){
        setSimpleValue("eid",groupExamDetailId);
    }

    public ObjectId getGroupExamDetailId(){
        return getSimpleObjecIDValue("eid");
    }
}
