package com.pojo.zouban;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.pojo.app.IdValuePair;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangkaidong on 2016/7/19.
 * <p/>
 * 学生等级考分层成绩
 * {
 * term: term 学年
 * gid: gradeId 年级id
 * subid: subjectId 学科id
 * sl: scoreList 学生成绩列表（IdValuePair{id:studentId,v:score}）
 * }
 */
public class ScoreEntry extends BaseDBObject {
    public ScoreEntry() {
        super();
    }

    public ScoreEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public ScoreEntry(String term, ObjectId gradeId, ObjectId subjectId, List<IdValuePair> scoreList) {
        BasicDBObject baseEntry = new BasicDBObject()
                .append("term", term)
                .append("gid", gradeId)
                .append("subid", subjectId)
                .append("sl", MongoUtils.convert(MongoUtils.fetchDBObjectList(scoreList)));
        setBaseEntry(baseEntry);
    }


    public String getTerm() {
        return getSimpleStringValue("term");
    }

    public void setTerm(String term) {
        setSimpleValue("term", term);
    }

    public ObjectId getGradeId() {
        return getSimpleObjecIDValue("gid");
    }

    public void setGradeId(ObjectId gradeId) {
        setSimpleValue("gid", gradeId);
    }

    public ObjectId getSubjectId() {
        return getSimpleObjecIDValue("subid");
    }

    public void setSubjectId(ObjectId subjectId) {
        setSimpleValue("subid", subjectId);
    }

    public List<IdValuePair> getScoreList() {
        BasicDBList basicDBList = (BasicDBList) getSimpleObjectValue("sl");
        List<IdValuePair> scoreList = new ArrayList<IdValuePair>();
        if (basicDBList != null) {
            for (Object o : basicDBList) {
                scoreList.add(new IdValuePair((BasicDBObject) o));
            }
        }
        return scoreList;
    }

    public void setScoreList(List<IdValuePair> scoreList) {
        setSimpleValue("sl", MongoUtils.convert(MongoUtils.fetchDBObjectList(scoreList)));
    }


}
