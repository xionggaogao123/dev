package com.db.forum;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.forum.Classify;
import com.pojo.forum.FPostEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by admin on 2016/5/31.
 * 帖子DAO
 */
public class FPostDao extends BaseDao {

  /**
   * 添加帖子
   **/
  public ObjectId addFPost(FPostEntry e) {
    save(MongoFacroty.getAppDB(), getCollection(), e.getBaseEntry());
    return e.getID();
  }

  /**
   * 获取帖子（分页）
   **/
  public List<FPostEntry> getFPostEntries(String startTime, String endTime, int reported, DBObject fields,
                                          String regular, DBObject sort, int cream, int inSet, ObjectId postSectionId,
                                          ObjectId personId, int classify,
                                          long gtTime, int skip, int limit, int remove) {
    DBObject query = buildQuery(startTime, endTime, reported, regular, cream, inSet, postSectionId, personId, classify, gtTime, remove);
    List<FPostEntry> retList = new ArrayList<FPostEntry>();
    List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), getCollection(), query, fields, sort, skip, limit);
    for (DBObject dbObject : dbObjectList) {
      retList.add(new FPostEntry((BasicDBObject) dbObject));
    }
    return retList;
  }

  public List<FPostEntry> getCreamData(int cream){
    BasicDBObject query=new BasicDBObject("cr",cream);
    BasicDBObject order=new BasicDBObject("crti",-1);
    List<FPostEntry> retList = new ArrayList<FPostEntry>();
    List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), getCollection(), query, Constant.FIELDS, order, 0, 10);
    for (DBObject dbObject : dbObjectList) {
      retList.add(new FPostEntry((BasicDBObject) dbObject));
    }
    return retList;

  }

  /**
   * 获取下线的帖子
   *
   * @return List
   */
  public List<FPostEntry> getFPostOffAcEntries() {
    BasicDBObject query = new BasicDBObject()
            .append("ist", -1)
            .append("cate", 1)
            .append("r", 0);
    //排序，根据更新的时间
    BasicDBObject sort = new BasicDBObject("up", -1);
    return query(query, sort);
  }

  /**
   * 获取所有的大赛贴
   **/
  public List<FPostEntry> getFPostAcEntriesAll() {
    List<FPostEntry> retList = new ArrayList<FPostEntry>();
    BasicDBObject query1 = new BasicDBObject()
            .append("ist", 1)
            .append("r", 0);
    BasicDBObject query2 = new BasicDBObject()
            .append("ist", -1)
            .append("cate", 1)
            .append("r", 0);
    //排序，根据更新的时间
    BasicDBObject sort = new BasicDBObject("up", -1);
    retList.addAll(query(query1, sort));
    retList.addAll(query(query2, sort));
    return retList;
  }

  /**
   * 获取大赛帖子的个数
   */
  public int getCompetionCount() {
    BasicDBObject query1 = new BasicDBObject()
            .append("ist", 1)
            .append("r", 0);
    BasicDBObject query2 = new BasicDBObject()
            .append("ist", -1)
            .append("cate", 1)
            .append("r", 0);
    return count(query1) + count(query2);
  }

  /**
   * 查帖子数量
   */
  public int getFPostEntriesCount(String startTime, String endTime, int reported,
                                  String regular, int cream, int inSet, ObjectId postSectionId,
                                  ObjectId personId, int classify, long gtTime, int remove) {
    DBObject query = buildQuery(startTime, endTime, reported, regular, cream, inSet, postSectionId, personId, classify, gtTime, remove);
    return count(query);
  }

  /**
   * 根据id获取帖子
   *
   * @param id 帖子id
   * @return FPostEntry
   */
  public FPostEntry getFPostEntry(ObjectId id) {
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID, id)
            .append("r", 0);
    return queryOne(query);
  }

  /**
   * 删除帖子
   **/
  public void deletePost(ObjectId Id) {
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID, Id);
    BasicDBObject update = new BasicDBObject()
            .append(Constant.MONGO_SET, new BasicDBObject("r", 1));
    update(query, update);
  }

  /**
   * 获取板块的帖子个数
   **/
  public int getPostCount(ObjectId id) {
    BasicDBObject query = new BasicDBObject()
            .append("pstid", id)
            .append("r", 0);
    return count(query);
  }

  /**
   * 获取板块的今日发帖数目
   **/
  public int getTodayPostCount(ObjectId pstid) {
    BasicDBObject query = new BasicDBObject()
            .append("pstid", pstid);
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.MILLISECOND, 0);
    query.append("ti", new BasicDBObject(Constant.MONGO_LTE, System.currentTimeMillis()).append(Constant.MONGO_GTE, cal.getTimeInMillis()));
    return count(query);
  }

  /**
   * 获取某人某板块的主题数（发帖数/帖子数）
   **/
  public int getPostCountByCondition(ObjectId postSectionId, ObjectId personId) {
    BasicDBObject query = new BasicDBObject()
            .append("pstid", postSectionId)
            .append("psid", personId);
    return count(query);
  }

  /**
   * 获取某人的发帖总数
   **/
  public int getPostCountByCondition(ObjectId personId) {
    BasicDBObject query = new BasicDBObject()
            .append("psid", personId);
    return count(query);
  }

  /**
   * 获取板块的总浏览数（每个帖子的浏览数相加）
   */
  public int getTotalScanCount(ObjectId postSectionId) {
    int scanCount = 0;
    BasicDBObject query = new BasicDBObject()
            .append("pstid", postSectionId);
    List<FPostEntry> items = query(query);
    for (FPostEntry item : items) {
      scanCount += item.getScanCount();
    }
    return scanCount;
  }


  /**
   * 更新浏览个数
   */
  public void updateScanCount(ObjectId postId) {
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID, postId);
    BasicDBObject updateValue = new BasicDBObject()
            .append(Constant.MONGO_INC, new BasicDBObject("sc", 1));
    update(query, updateValue);
  }

  /**
   * 增加评论个数
   */
  public void updateCommentCount(ObjectId postId) {
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID, postId);
    BasicDBObject updateValue = new BasicDBObject()
            .append(Constant.MONGO_INC, new BasicDBObject("cc", 1));
    update(query, updateValue);
  }

  /**
   * 减少评论个数
   */
  public void updateDecCommentCount(ObjectId postId) {
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID, postId);
    BasicDBObject updateValue = new BasicDBObject()
            .append(Constant.MONGO_INC, new BasicDBObject("cc", -1));
    update(query, updateValue);
  }

  /**
   * 更新精华
   *
   * @param postId
   */
  public void updateCream(ObjectId postId, int cream) {
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID, postId);
    BasicDBObject updateValue = new BasicDBObject()
            .append(Constant.MONGO_SET, new BasicDBObject("cr", cream));
    update(query, updateValue);
  }

  /**
   * 更新点赞
   */
  public void updateBtnZan(ObjectId userReplyId, boolean flag, ObjectId postId) {
    BasicDBObject query = new BasicDBObject(Constant.ID, postId);
    BasicDBObject updateValue = new BasicDBObject();
    if (flag) {
      BasicDBObject upValue = new BasicDBObject()
              .append("url", userReplyId);
      updateValue.append(Constant.MONGO_ADDTOSET, upValue)
              .append(Constant.MONGO_INC, new BasicDBObject("prc", 1));
    } else {
      BasicDBObject downValue = new BasicDBObject()
              .append("url", userReplyId);
      updateValue.append(Constant.MONGO_PULL, downValue)
              .append(Constant.MONGO_INC, new BasicDBObject("prc", -1));
    }
    update(query, updateValue);
  }

  /**
   * 更新点赞
   */
  public void updateZan(int count, ObjectId postId) {
    BasicDBObject query = new BasicDBObject(Constant.ID, postId);
    BasicDBObject updateValue = new BasicDBObject().append(Constant.MONGO_SET,new BasicDBObject("prc", count));
    update(query, updateValue);
  }


  /**
   * 更新反对
   */
  public void updateOppose(ObjectId userReplyId, boolean flag, ObjectId postId) {
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID, postId);
    BasicDBObject updateValue = new BasicDBObject();
    BasicDBObject value = new BasicDBObject()
            .append("opl", userReplyId);
    if (flag) {
      updateValue.append(Constant.MONGO_ADDTOSET, value)
              .append(Constant.MONGO_INC, new BasicDBObject("opc", 1));
    } else {
      updateValue.append(Constant.MONGO_PULL, value)
              .append(Constant.MONGO_INC, new BasicDBObject("opc", -1));
    }
    update(query, updateValue);
  }


  /**
   * 更新置顶
   *
   * @param postId
   */
  public void updateTop(ObjectId postId, int top) {
    BasicDBObject query = new BasicDBObject(Constant.ID, postId);
    BasicDBObject updateValue = new BasicDBObject()
            .append(Constant.MONGO_SET, new BasicDBObject("tp", top));
    update(query, updateValue);
  }

  /**
   * 更新最后回复人以及最后回复时间
   *
   * @param postId
   */
  public void updateReplyInf(ObjectId postId, ObjectId replyUserId) {
    BasicDBObject query = new BasicDBObject(Constant.ID, postId);
    BasicDBObject update = new BasicDBObject()
            .append("rei", replyUserId)
            .append("upt", System.currentTimeMillis());
    BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, update);
    update(query, updateValue);
  }

  /**
   * 更新举报列表
   *
   * @param postId
   */
  public void updateReportedList(FPostEntry.Reported e, ObjectId postId, int reported,
                                 String reportedComment, int reportedExperience) {
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID, postId);
    BasicDBObject update = new BasicDBObject()
            .append("rpt", reported)
            .append("rpe", reportedExperience)
            .append("rpc", reportedComment);
    BasicDBObject updateValue = new BasicDBObject()
            .append(Constant.MONGO_ADDTOSET, new BasicDBObject("rpl", e.getBaseEntry()))
            .append(Constant.MONGO_SET, update);
    update(query, updateValue);
  }

  /**
   * 批量逻辑删除帖子
   * 此处必须是逻辑删除
   *
   * @param ids
   */
  public void removeFPostLogic(Collection<ObjectId> ids) {
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
    BasicDBObject updateValue = new BasicDBObject()
            .append(Constant.MONGO_SET, new BasicDBObject("r", 1));
    update(query, updateValue);
  }


  /**
   * 批量恢复帖子
   **/
  public void recoverFPostLogic(Collection<ObjectId> ids, int remove) {
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
    BasicDBObject updateValue = new BasicDBObject()
            .append(Constant.MONGO_SET, new BasicDBObject("r", remove));
    update(query, updateValue);
  }

  public void updateRewardCountValue(ObjectId userId) {
    DBObject query = new BasicDBObject()
            .append(Constant.ID, userId);
    DBObject updateValue = new BasicDBObject()
            .append(Constant.MONGO_INC, new BasicDBObject("rwc", -1));
    update(query, updateValue);
  }

  private String getCollection() {
    return Constant.COLLECTION_FORUM_POST;
  }

  private List<FPostEntry> query(BasicDBObject query) {
    List<FPostEntry> retList = new ArrayList<FPostEntry>();
    List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), getCollection(), query, Constant.FIELDS);
    for (DBObject dbObject : dbObjectList) {
      FPostEntry post = new FPostEntry((BasicDBObject) dbObject);
      retList.add(post);
    }
    return retList;
  }

  private List<FPostEntry> query(BasicDBObject query, BasicDBObject sort) {
    List<FPostEntry> retList = new ArrayList<FPostEntry>();
    List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), getCollection(), query, Constant.FIELDS, sort);
    for (DBObject dbObject : dbObjectList) {
      FPostEntry post = new FPostEntry((BasicDBObject) dbObject);
      retList.add(post);
    }
    return retList;
  }

  private FPostEntry queryOne(BasicDBObject query) {
    DBObject dbo = findOne(MongoFacroty.getAppDB(), getCollection(), query, Constant.FIELDS);
    if (null != dbo) {
      return new FPostEntry((BasicDBObject) dbo);
    }
    return null;
  }

  private void update(DBObject query, DBObject update) {
    update(MongoFacroty.getAppDB(), getCollection(), query, update);
  }

  private int count(DBObject query) {
    return count(MongoFacroty.getAppDB(), getCollection(), query);
  }

  private long handleDate(String date) {
    SimpleDateFormat sdf = new SimpleDateFormat(DateTimeUtils.DATE_YYYY_MM_DD);
    try {
      Date dt = sdf.parse(date);
      return dt.getTime();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return 0;
  }

  private DBObject buildQuery(String startTime, String endTime, int reported,
                              String regular, int cream, int inSet, ObjectId postSectionId,
                              ObjectId personId, int classify,
                              long gtTime, int remove) {
    BasicDBObject query = new BasicDBObject();
    if (postSectionId != null) {
      query.append("pstid", postSectionId);
    }
    if (classify != -1) {
      if (classify == 7) {
        BasicDBList values = new BasicDBList();
        values.add(new BasicDBObject("clf", 3));
        values.add(new BasicDBObject("clf", 4));
        values.add(new BasicDBObject("clf", 7));
        query.put("$or", values);
      } else {
        query.append("clf", classify);
      }
    }
    if (inSet != -1) {
      query.append("ist", inSet);
    } else {
      query.append("ist", new BasicDBObject(Constant.MONGO_NOTIN, new int[]{1}));
    }
    if (gtTime != 0) {
      query.append("ti", new BasicDBObject(Constant.MONGO_LTE, System.currentTimeMillis()).append(Constant.MONGO_GTE, gtTime));
    }

    query.append("r", remove);
    if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
      long startPostTime = handleDate(startTime);
      long endPostTime = handleDate(endTime);
      query.append("ti", new BasicDBObject(Constant.MONGO_LTE, endPostTime).append(Constant.MONGO_GTE, startPostTime));
    }
    if (cream != -1) {
      query.append("cr", cream);
    }
    if (personId != null) {
      query.append("psid", personId);
    }
    if (reported != -1) {
      query.append("rpt", reported);
    }
    if (StringUtils.isNotBlank(regular)) {
      query.append("pt", MongoUtils.buildRegex(regular));
    }
    return query;
  }

  /**
   * 根据类别，板块获取帖子个数
   **/
  public int sectionPostCount(ObjectId sectionId, Classify classify) {
    BasicDBObject query = new BasicDBObject()
            .append("pstid", sectionId)
            .append("clf", classify.getIndex())
            .append("r", 0);
    return count(query);
  }

  public void updatePosttitle(ObjectId postId,String title) {
    BasicDBObject query = new BasicDBObject()
            .append(Constant.ID,postId);
    BasicDBObject udpate = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("acmm",title));
    update(MongoFacroty.getAppDB(),getCollection(),query,udpate);
  }

}
