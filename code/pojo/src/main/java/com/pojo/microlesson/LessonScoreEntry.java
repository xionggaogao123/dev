package com.pojo.microlesson;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2015/8/28.
 */

/**
 * 课程打分
 * <pre>
 * collectionName:lesosnscore
 * </pre>
 * <pre>
 * {
 * lsid:课程id
 * lsnm:课程名称
 * mid:比赛id
 * allscore:总分
 * avg:平均分
 * sort:排名
 * </pre>
 * @author Alex
 */
public class LessonScoreEntry extends BaseDBObject {
    private static final long serialVersionUID = 9140987189405241952L;

    public LessonScoreEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public LessonScoreEntry(ObjectId lessonid, ObjectId matchid,String lessonname, int allscore,Double average,int sort,List<ScoreTypeEntry> scoreTypeList) {

        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("lsid", lessonid)
                .append("mid", matchid)
                .append("lsnm",lessonname)
                .append("allscore", allscore)
                .append("avg",average)
                .append("sort", sort)
                .append("stypes", MongoUtils.convert(MongoUtils.fetchDBObjectList(scoreTypeList)));
        setBaseEntry(baseEntry);
    }

    public ObjectId getLessonid() {
        return getSimpleObjecIDValue("lsid");
    }
    public void setLessonid(ObjectId lessonid) {
        setSimpleValue("lsid", lessonid);
    }
    public ObjectId getMatchid() {
        return getSimpleObjecIDValue("mid");
    }
    public void setMatchid(ObjectId matchid) {
        setSimpleValue("mid", matchid);
    }
    public int getAllScore() {
        return getSimpleIntegerValue("allscore");
    }
    public void setAllScore(int allscore) {
        setSimpleValue("allscore", allscore);
    }
    public double getAverage() {
        return getSimpleDoubleValue("avg");
    }
    public void setAverage(int average) {
        setSimpleValue("avg", average);
    }
    public String getLessonname() {
        return getSimpleStringValue("lsnm");
    }
    public void setLessonname(String lessonname) {
        setSimpleValue("lsnm",lessonname);
    }
    public int getSort() {
        return getSimpleIntegerValue("sort");
    }
    public void setSort(int sort) {
        setSimpleValue("sort", sort);
    }

    public List<ScoreTypeEntry> getScoreTypeList() {
        List<ScoreTypeEntry> retList =new ArrayList<ScoreTypeEntry>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("stypes");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(new ScoreTypeEntry((BasicDBObject)o));
            }
        }
        return retList;
    }
    public void setScoreTypeList(List<ScoreTypeEntry> scoreTypeList) {
        List<DBObject> list=MongoUtils.fetchDBObjectList(scoreTypeList);
        setSimpleValue("stypes", MongoUtils.convert(list));
    }


}
