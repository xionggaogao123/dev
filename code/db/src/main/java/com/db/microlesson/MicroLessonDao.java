package com.db.microlesson;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdValuePair;
import com.pojo.microlesson.*;
import com.pojo.utils.DeleteState;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang_xinxin on 2015/8/21.
 */
public class MicroLessonDao extends BaseDao {

    /**
     * 添加比赛
     *
     * @param e
     * @return
     */
    public ObjectId addMicroMatch(MicroMatchEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_MATCH_NAME, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 查询比赛数量
     *
     * @param id
     * @return
     */
    public int selMicroMatchCount(ObjectId id) {
        DBObject query = new BasicDBObject("bureauid", id).append("delflg", DeleteState.NORMAL.getState());
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_MATCH_NAME, query);
    }

    /**
     * 查询比赛列表
     *
     * @param id
     * @param page
     * @param pageSize
     * @return
     */
    public List<MicroMatchEntry> selMicroMatch(ObjectId id, int page, int pageSize) {
        List<MicroMatchEntry> retList = new ArrayList<MicroMatchEntry>();
        List<DBObject> list = new ArrayList<DBObject>();
        BasicDBObject query = new BasicDBObject("delflg", DeleteState.NORMAL.getState());
        query.append("bureauid", id);
        list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_MATCH_NAME, query, null, Constant.MONGO_SORTBY_DESC, page, pageSize);
        for (DBObject dbo : list) {
            retList.add(new MicroMatchEntry((BasicDBObject) dbo));
        }
        return retList;
    }

    /**
     * 单条比赛详情
     *
     * @param matchid
     * @return
     */
    public MicroMatchEntry getMatchDetail(String matchid, DBObject fields) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new ObjectId(matchid));
        query.append("delflg", DeleteState.NORMAL.getState());
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_MATCH_NAME, query, fields);
        if (null != dbo) {
            return new MicroMatchEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 添加比赛课程
     *
     * @param matchid
     * @param pair
     */
    public void updateMicroMatch(String matchid, TypeLessonEntry pair) {
        DBObject query = new BasicDBObject(Constant.ID, new ObjectId(matchid));
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PUSH, new BasicDBObject("leslit", pair.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MATCH_NAME, query, updateValue);

    }

    public void deletematchlesson(TypeLessonEntry lesson, String matchid) {
        DBObject query = new BasicDBObject(Constant.ID, new ObjectId(matchid));
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("leslit", lesson.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MATCH_NAME, query, updateValue);
    }

    public void deleteMatch(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("delflg", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MATCH_NAME, query, updateValue);
    }

    public ObjectId addSocreInfo(TeacherScoreEntry teacherScore) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_MATCH_SCORE_NAME, teacherScore.getBaseEntry());
        return teacherScore.getID();
    }

    public List<TeacherScoreEntry> getLessonScore(ObjectId lessonid) {
        List<TeacherScoreEntry> retList = new ArrayList<TeacherScoreEntry>();
        BasicDBObject query = new BasicDBObject("lsid", lessonid);
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_MATCH_SCORE_NAME, query, null);
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new TeacherScoreEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    public LessonScoreEntry selLessonScore(ObjectId lessonid, ObjectId dirId) {
        BasicDBObject query = new BasicDBObject("mid", dirId);
        if (lessonid != null) {
            query.append("lsid", lessonid);
        }
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_MATCH_LESSON_SCORE_NAME, query, null);
        if (null != dbo) {
            return new LessonScoreEntry((BasicDBObject) dbo);
        }
        return null;


    }

    /**
     * 添加课程分数
     *
     * @param lessonScore
     * @return
     */
    public ObjectId addLessonScore(LessonScoreEntry lessonScore) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_MATCH_LESSON_SCORE_NAME, lessonScore.getBaseEntry());
        return lessonScore.getID();

    }

    /**
     * 更新课程分数
     *
     * @param lessonScore
     */
    public void updateLessonScore(LessonScoreEntry lessonScore) {
        List<DBObject> scoreList = new ArrayList<DBObject>();
        if (lessonScore.getScoreTypeList() != null && lessonScore.getScoreTypeList().size() != 0) {
            for (ScoreTypeEntry ste : lessonScore.getScoreTypeList()) {
                scoreList.add(ste.getBaseEntry());
            }
        }
        DBObject query = new BasicDBObject("lsid", lessonScore.getLessonid());
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("allscore", lessonScore.getAllScore()).append("avg", lessonScore.getAverage()).append("lsnm", lessonScore.getLessonname()).append("stypes", scoreList));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MATCH_LESSON_SCORE_NAME, query, updateValue);
    }


    /**
     * @param id
     * @param lessonid
     * @return
     */
    public MicroMatchEntry selMicroMatchByuserid(ObjectId id, ObjectId lessonid) {
//        List<ObjectId> ids = new ArrayList<ObjectId>();
//        ids.add(id);
        BasicDBObject query = new BasicDBObject("rater", id).append("leslit.v", lessonid.toString());
        query.append("delflg", DeleteState.NORMAL.getState());
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_MATCH_NAME, query, null);
        if (null != dbo) {
            return new MicroMatchEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * @param id
     * @param lessonId
     * @return
     */
    public TeacherScoreEntry selTeacherScore(ObjectId id, ObjectId lessonId) {
        BasicDBObject query = new BasicDBObject("sui", id).append("lsid", lessonId);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_MATCH_SCORE_NAME, query, null);
        if (null != dbo) {
            return new TeacherScoreEntry((BasicDBObject) dbo);
        }
        return null;
    }

    /**
     * 获取得分
     *
     * @param lst
     * @param matchid
     * @return
     */
    public List<LessonScoreEntry> selLessonScorelist(List<ObjectId> lst, ObjectId matchid) {
        BasicDBObject query = new BasicDBObject("mid", matchid);
        if (lst != null && lst.size() != 0) {
            query.append("lsid", new BasicDBObject(Constant.MONGO_IN, lst));
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_MATCH_LESSON_SCORE_NAME, query, null, new BasicDBObject("sort", 1));
        List<LessonScoreEntry> retList = new ArrayList<LessonScoreEntry>();
        if (null != list && !list.isEmpty()) {
            for (DBObject dbo : list) {
                retList.add(new LessonScoreEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    /**
     * @param microMatchEntry
     */
    public void updatematch(MicroMatchEntry microMatchEntry, ObjectId id) {
        BasicDBObject query = new BasicDBObject(Constant.ID, id);
        List<DBObject> scoreList = new ArrayList<DBObject>();
        if (microMatchEntry.getScoreTypeList() != null && microMatchEntry.getScoreTypeList().size() != 0) {
            for (ScoreTypeEntry ste : microMatchEntry.getScoreTypeList()) {
                scoreList.add(ste.getBaseEntry());
            }
        }
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("mum", microMatchEntry.getMatchname())
                .append("con", microMatchEntry.getConntent())
                .append("path", microMatchEntry.getPath())
                .append("begtime", microMatchEntry.getBegintime())
                .append("endtime", microMatchEntry.getEndtime())
                .append("scobegtime", microMatchEntry.getScorebegintime())
                .append("scoendtime", microMatchEntry.getScoreendtime())
                .append("stypes", scoreList));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MATCH_NAME, query, updateValue);

    }

    public void updateLessonScoreSort(ObjectId lessonid, ObjectId matchid, int sort) {
        DBObject query = new BasicDBObject("lsid", lessonid).append("mid", matchid);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("sort", sort));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MATCH_LESSON_SCORE_NAME, query, updateValue);
    }

    /**
     * 增加减少评委
     *
     * @param id
     * @param userid
     * @param type
     */
    public void updateMatchUser(String id, String userid, int type) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new ObjectId(id));
        String mongotype = null;
        if (type == 2) {
            mongotype = Constant.MONGO_PULL;
        } else {
            mongotype = Constant.MONGO_PUSH;
        }
        BasicDBObject updateValue = new BasicDBObject(mongotype, new BasicDBObject("rater", new ObjectId(userid)));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MATCH_NAME, query, updateValue);

    }

    /**
     * 添加分类
     *
     * @param id
     * @param pair
     */
    public void updateMatchType(String id, IdValuePair pair) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new ObjectId(id));
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_PUSH, new BasicDBObject("mtypes", pair.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MATCH_NAME, query, updateValue);
    }

    /**
     * 删除分类
     *
     * @param idv
     */
    public void missMatchType(String id, IdValuePair idv) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new ObjectId(id));
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("mtypes", idv.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MATCH_NAME, query, updateValue);
    }

    /**
     * @param id
     * @param idv
     */
    public void missMatchLessonType(String id, TypeLessonEntry idv) {
        BasicDBObject query = new BasicDBObject(Constant.ID, new ObjectId(id));
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("leslit", idv.getBaseEntry()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_MATCH_NAME, query, updateValue);
    }
}
