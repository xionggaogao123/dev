package com.pojo.microlesson;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdValuePair;
import com.pojo.base.BaseDBObject;
import com.pojo.utils.DeleteState;
import com.pojo.utils.MongoUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2015/8/28.
 */

/**
 * 老师打分
 * <pre>
 * collectionName:matchscore
 * </pre>
 * <pre>
 * {
 * sui:打分者ID
 * snm:打分者名字
 * sc:总分数
 * comment:评论
 *lsid:课程id
 * pubtime:打分时间，long
 * </pre>
 * @author Alex
 */
public class TeacherScoreEntry extends BaseDBObject {

    /**
     *
     */
    private static final long serialVersionUID = -4045149980009578327L;

    public TeacherScoreEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public TeacherScoreEntry(ObjectId suserid,String susername,int score,String comment,ObjectId lessonid,List<ScoreTypeEntry> scoreTypeList) {
        this(suserid,susername,score,comment,lessonid,System.currentTimeMillis(),scoreTypeList);
    }

    public TeacherScoreEntry(ObjectId suserid, String susername, int score, String comment,ObjectId lessonid,long time,List<ScoreTypeEntry> scoreTypeList) {

        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("sui", suserid)
                .append("snm", susername)
                .append("sc", score)
                .append("comment", comment)
                .append("lsid",lessonid)
                .append("pubtime", time)
                .append("stypes", MongoUtils.convert(MongoUtils.fetchDBObjectList(scoreTypeList)));
        setBaseEntry(baseEntry);
    }

    public ObjectId getSuserid() {
        return getSimpleObjecIDValue("sui");
    }
    public void setSuserid(ObjectId suserid) {
        setSimpleValue("sui", suserid);
    }
    public ObjectId getLessonid() {
        return getSimpleObjecIDValue("lsid");
    }
    public void setLessonid(ObjectId lessonid) {
        setSimpleValue("lsid", lessonid);
    }
    public int getScore() {
        return getSimpleIntegerValue("sc");
    }
    public void setScore(int score) {
        setSimpleValue("sc", score);
    }
    public String getComment() {
        return getSimpleStringValue("comment");
    }
    public void setComment(int comment) {
        setSimpleValue("comment", comment);
    }
    public long getTime() {
        return getSimpleLongValue("pubtime");
    }
    public void setTime(long time) {
        setSimpleValue("pubtime",time);
    }
    public String getSusername() {
        return getSimpleStringValue("snm");
    }
    public void setSusername(String susername) {
        setSimpleValue("snm",susername);
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
