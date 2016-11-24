package com.pojo.microlesson;

/**
 * Created by wang_xinxin on 2015/8/19.
 */

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
 * 比赛列表
 * <pre>
 * collectionName:match
 * </pre>
 * <pre>
 * {
 * bui:创建者ID
 * mnm:比赛名称
 * con:比赛说明
 *path:图片路径
 * begtime：参赛开始时间，long
 * endtime:参赛结束时间,long
 * scobegtime：评分开始时间，long
 * scoendtime:评分结束时间,long
 * pubtime:发布时间，long
 * delflg:删除flag
 * bureauid:教育局账户
 * rater:评委userid
 * [

 *]
 *leslit:微课列表
 *  [
 *   {
 *    id:比赛分类
 *     v:课程id
 *   }
 *  ]
 *mtypes:分类
 *   [
 *   {
 *    id:比赛分类
 *     v:分类名称
 *   }
 *  ]
 * </pre>
 * @author Alex
 */
public class MicroMatchEntry extends BaseDBObject {

    /**
     *
     */
    private static final long serialVersionUID = -4664276910616121107L;

    public MicroMatchEntry(BasicDBObject baseEntry) {
        super(baseEntry);
    }

    public MicroMatchEntry(ObjectId buserid,String matchname,String conntent,String path,long begintime,long endtime,long scorebegintime,long scoreendtime,List<ObjectId> userlist,List<IdValuePair> matchtypelist,List<ScoreTypeEntry> scoreTypeList,ObjectId bureauid) {
        this(buserid,matchname,conntent,path,begintime,endtime,scorebegintime,scoreendtime, DeleteState.NORMAL.getState(),userlist,new ArrayList<TypeLessonEntry>(),matchtypelist,scoreTypeList,bureauid);
    }


    public MicroMatchEntry(ObjectId buserid, String matchname, String conntent,String path, long begintime, long endtime, long scorebegintime, long scoreendtime, int delflg, List<ObjectId> userlist, List<TypeLessonEntry> lessonlist, List<IdValuePair> matchtypelist,List<ScoreTypeEntry> scoreTypeList,ObjectId bureauid) {
        super();
        BasicDBObject baseEntry =new BasicDBObject()
                .append("bui", buserid)
                .append("mum", matchname)
                .append("con", conntent)
                .append("path",path)
                .append("begtime", begintime)
                .append("endtime", endtime)
                .append("scobegtime", scorebegintime)
                .append("scoendtime", scoreendtime)
                .append("pubtime", System.currentTimeMillis())
                .append("delflg", delflg)
                .append("rater", MongoUtils.convert(userlist))
                .append("leslit", MongoUtils.convert(MongoUtils.fetchDBObjectList(lessonlist)))
                .append("mtypes", MongoUtils.convert(MongoUtils.fetchDBObjectList(matchtypelist)))
                .append("stypes", MongoUtils.convert(MongoUtils.fetchDBObjectList(scoreTypeList)))
                .append("bureauid", bureauid);
        ;
        setBaseEntry(baseEntry);
    }

    public ObjectId getBuserid() {
        return getSimpleObjecIDValue("bui");
    }
    public void setBuserid(ObjectId buserid) {
        setSimpleValue("bui", buserid);
    }
    public ObjectId getBureauid() {
        return getSimpleObjecIDValue("bureauid");
    }
    public void setBureauid(ObjectId bureauid) {
        setSimpleValue("bureauid", bureauid);
    }
    public String getMatchname() {
        return getSimpleStringValue("mum");
    }
    public void setMatchname(String matchname) {
        setSimpleValue("mum", matchname);
    }
    public String getPath() {
        return getSimpleStringValue("path");
    }
    public void setPath(String path) {
        setSimpleValue("path", path);
    }
    public String getConntent() {
        return getSimpleStringValue("con");
    }
    public void setConntent(String conntent) {
        setSimpleValue("con", conntent);
    }
    public int getDelflg() {
        return getSimpleIntegerValue("delflg");
    }
    public void setDelflg(int delflg) {
        setSimpleValue("delflg", delflg);
    }
    public long getBegintime() {
        return getSimpleLongValue("begtime");
    }
    public void setBegintime(long begintime) {
        setSimpleValue("begtime", begintime);
    }
    public long getEndtime() {
        return getSimpleLongValue("endtime");
    }
    public void setEndtime(long endtime) {
        setSimpleValue("endtime", endtime);
    }
    public long getScorebegintime() {
        return getSimpleLongValue("scobegtime");
    }
    public void setScorebegintime(long scorebegintime) {
        setSimpleValue("scobegtime", scorebegintime);
    }
    public long getScoreendtime() {
        return getSimpleLongValue("scoendtime");
    }
    public void setScoreendtime(long scoreendtime) {
        setSimpleValue("scoendtime", scoreendtime);
    }
    public long getPubtime() {
        return getSimpleLongValue("pubtime");
    }
    public void setPubtime(long pubtime) {
        setSimpleValue("pubtime", pubtime);
    }
    public List<ObjectId> getUserlist() {
        List<ObjectId> retList =new ArrayList<ObjectId>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("rater");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add((ObjectId)o);
            }
        }
        return retList;
    }
    public void setUserlist(List<ObjectId> userlist) {
        setSimpleValue("rater", MongoUtils.convert(userlist));
    }

    public List<TypeLessonEntry> getLessonlist() {
        List<TypeLessonEntry> retList =new ArrayList<TypeLessonEntry>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("leslit");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(new TypeLessonEntry((BasicDBObject)o));
            }
        }
        return retList;
    }
    public void setLessonlist(List<TypeLessonEntry> lessonlist) {
        List<DBObject> list=MongoUtils.fetchDBObjectList(lessonlist);
        setSimpleValue("leslit", MongoUtils.convert(list));
    }

    public List<IdValuePair> getMatchtypelist() {
        List<IdValuePair> retList =new ArrayList<IdValuePair>();
        BasicDBList list =(BasicDBList)getSimpleObjectValue("mtypes");
        if(null!=list && !list.isEmpty())
        {
            for(Object o:list)
            {
                retList.add(new IdValuePair((BasicDBObject)o));
            }
        }
        return retList;
    }
    public void setMatchtypelist(List<IdValuePair> matchtypelist) {
        List<DBObject> list=MongoUtils.fetchDBObjectList(matchtypelist);
        setSimpleValue("mtypes", MongoUtils.convert(list));
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
    public void setScoreTypeList(List<ScoreTypeEntry> matchtypelist) {
        List<DBObject> list=MongoUtils.fetchDBObjectList(matchtypelist);
        setSimpleValue("stypes", MongoUtils.convert(list));
    }
}
