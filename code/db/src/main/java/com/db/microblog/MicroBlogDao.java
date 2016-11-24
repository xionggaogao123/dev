package com.db.microblog;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.Platform;
import com.pojo.microblog.MicroBlogEntry;
import com.pojo.utils.DeleteState;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.ResultTooManyException;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.*;


/**
 * 微博dao
 * index:ui_ty_st_pf_bt_si_at-id_pbt_ir
 * {"ui":1,"ty":1,"st":1,"pf":1,"bt":1,"si":1,"at.id":1,"pbt":1,"ir":1}
 *
 * @author fourer
 */
public class MicroBlogDao extends BaseDao {

    /**
     * 添加
     *
     * @param e
     * @return
     */
    public ObjectId addMicroBlog(MicroBlogEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, e.getBaseEntry());
        return e.getID();
    }

    /**
     * 删除
     *
     * @param id
     * @param ui
     */
    public void deleteMicroBlog(ObjectId id, ObjectId ui) {

        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject value = new BasicDBObject("st", DeleteState.DELETED.getState());
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query, updateValue);
    }

    /**
     * 删除多个
     *
     * @param ids
     */
    public void deleteMicroBlog(Collection<ObjectId> ids) {
        DBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        DBObject value = new BasicDBObject("st", DeleteState.DELETED.getState());
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query, updateValue);
    }


    /**
     * 添加评论
     *
     * @param id
     * @param replyId
     */
    public void addComment(ObjectId id, ObjectId replyId) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PUSH, new BasicDBObject("cli", replyId)).append(Constant.MONGO_INC, new BasicDBObject("rc", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query, updateValue);
    }

    /**
     * 删除评论
     *
     * @param id
     * @param replyId
     */
    public void deleteComment(ObjectId id, ObjectId replyId) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PULL, new BasicDBObject("cli", replyId)).append(Constant.MONGO_INC, new BasicDBObject("rc", Constant.NEGATIVE_ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query, updateValue);
    }


    /**
     * 是否已经赞过
     *
     * @param id
     * @param ui
     * @return
     */
    public boolean isHaveZan(ObjectId id, ObjectId ui) {
        DBObject query = new BasicDBObject(Constant.ID, id).append("zci", ui);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query) > Constant.ZERO;
    }

    /**
     * 添加一个赞
     *
     * @param id
     * @param ui
     */
    public void addZan(ObjectId id, ObjectId ui) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PUSH, new BasicDBObject("zci", ui)).append(Constant.MONGO_INC, new BasicDBObject("zc", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query, updateValue);
    }

    /**
     * 查询
     *
     * @param ui                用户ID；不为null时生效
     * @param state
     * @param form              平台；不为null时生效
     * @param atUserId；根据at用户查询
     * @param blogtype          是微校园还是微家园
     * @param hottype           最新还是最热、我的帖子
     * @param fields
     * @param skip              用于分页；大于0时生效
     * @param limit             用于分页；大于0时生效
     * @return
     * @throws ResultTooManyException
     */
    public List<MicroBlogEntry> getMicroBlogEntryList(ObjectId ui, DeleteState state, Platform form, ObjectId atUserId, int blogtype, int hottype, DBObject fields, String schoolid, int skip, int limit, int seachtype, List<ObjectId> classAry, String theme, int seachType) throws ResultTooManyException {
        List<MicroBlogEntry> retList = new ArrayList<MicroBlogEntry>();
        BasicDBObject query = new BasicDBObject("st", state.getState());
        if (null != ui && hottype == 3) {
            query.append("ui", ui);
        }
        if (null != form) {
            query.append("pf", form.getType());
        }
        if (blogtype != 0) {
            query.append("bt", blogtype);
        }
        if (seachtype != 1) {
            query.append("cls", new BasicDBObject(Constant.MONGO_IN, classAry));
        } else {
            query.append("cls", new BasicDBObject("$size", 0));
        }
        if (!StringUtils.isEmpty(theme)) {
            query.append("con", MongoUtils.buildRegex(theme));
        }
        query.append("stp", seachType);
        query.append("ty", Constant.ONE);
        query.append("si", new ObjectId(schoolid));

        if (null != atUserId) {
            query.append("at.id", atUserId);
        }

        if (query.isEmpty())
            throw new ResultTooManyException();
        List<DBObject> list = new ArrayList<DBObject>();

        if (skip >= 0 && limit > 0) {
            if (hottype == 2) {
                list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query, fields, new BasicDBObject("zc", -1), skip, limit);
            } else {
                list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query, fields, new BasicDBObject("it", -1).append("_id", -1), skip, limit);
            }
        } else {
            if (hottype == 2) {
                list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query, fields, new BasicDBObject("zc", -1));
            } else {
                list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query, fields, new BasicDBObject("it", -1).append("_id", -1));
            }
        }

        for (DBObject dbo : list) {
            retList.add(new MicroBlogEntry((BasicDBObject) dbo));
        }
        return retList;
    }

    /**
     * 查询单条微博
     *
     * @param blogid
     * @return
     */
    public MicroBlogEntry getOneBlogInfo(ObjectId blogid, DBObject fields) {
        BasicDBObject query = new BasicDBObject(Constant.ID, blogid);
        query.append("st", DeleteState.NORMAL.getState());
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query, fields);
        if (null != dbo) {
            return new MicroBlogEntry((BasicDBObject) dbo);
        }
        return null;
    }


    /**
     * 获取数量
     *
     * @param userid
     * @param hottype
     * @param blogtype
     * @return
     */
    public int getFriendBlogCount(ObjectId userid, int hottype, int blogtype, String schoolid, int seachtype, List<ObjectId> classAry, String theme, int seachType) {
        BasicDBObject query = new BasicDBObject("st", DeleteState.NORMAL.getState());
        if (null != userid && hottype == 3) {
            query.append("ui", userid);
        }
        if (blogtype != 0) {
            query.append("bt", blogtype);
        }
        if (seachtype != 1) {
            query.append("cls", new BasicDBObject(Constant.MONGO_IN, classAry));
        } else {
            query.append("cls", new BasicDBObject("$size", 0));
        }
        query.append("stp", seachType);
        if (!StringUtils.isEmpty(theme)) {
            query.append("con", MongoUtils.buildRegex(theme));
        }
        query.append("ty", Constant.ONE);
        query.append("si", new ObjectId(schoolid));

        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query);
    }

    /**
     * 根据查询条件查询发微博信息
     *
     * @param uis      用户ID；不为null时生效
     * @param state
     * @param blogtype 是微校园还是微家园
     * @param dsl
     * @param del
     * @param fields
     * @param skip     用于分页；大于0时生效
     * @param limit    用于分页；大于0时生效
     * @param orderBy
     * @return
     * @throws ResultTooManyException
     */
    public List<MicroBlogEntry> getMicroBlogEntryByParamList(List<ObjectId> uis, DeleteState state, int blogtype, long dsl, long del, DBObject fields, int skip, int limit, String orderBy) throws ResultTooManyException {
        List<MicroBlogEntry> retList = new ArrayList<MicroBlogEntry>();
        BasicDBObject query = new BasicDBObject("ui", new BasicDBObject(Constant.MONGO_IN, uis));
        BasicDBList dblist = new BasicDBList();
        if (dsl > 0) {
            dblist.add(new BasicDBObject("pbt", new BasicDBObject(Constant.MONGO_GTE, dsl)));
        }
        if (del > 0) {
            dblist.add(new BasicDBObject("pbt", new BasicDBObject(Constant.MONGO_LTE, del)));
        }
        if (dblist.size() > 0) {
            query.append(Constant.MONGO_AND, dblist);
        }

        query.append("st", state.getState());

        if (blogtype != 0) {
            query.append("bt", blogtype);
        }
        BasicDBObject sort = null;
        if (!"".equals(orderBy)) {
            sort = new BasicDBObject(orderBy, Constant.DESC);
        } else {
            sort = new BasicDBObject("pbt", Constant.DESC);
        }
        if (query.isEmpty())
            throw new ResultTooManyException();
        List<DBObject> list = new ArrayList<DBObject>();

        if (skip >= 0 && limit > 0) {
            list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query, fields, sort, skip, limit);
        } else {
            list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query, fields, sort);
        }

        for (DBObject dbo : list) {
            retList.add(new MicroBlogEntry((BasicDBObject) dbo));
        }
        return retList;
    }


    /**
     * 根据ID集合查询
     *
     * @param ids
     * @param fields
     * @return
     * @throws ResultTooManyException
     */
    public List<MicroBlogEntry> getMicroBlogEntryByIds(List<ObjectId> ids, DBObject fields) {
        List<MicroBlogEntry> retList = new ArrayList<MicroBlogEntry>();
        BasicDBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query, fields);
        if (null != list) {
            for (DBObject dbo : list) {
                retList.add(new MicroBlogEntry((BasicDBObject) dbo));
            }
        }
        return retList;
    }

    /**
     * 获取回复评论数量
     *
     * @param blogid
     * @param type
     * @param id
     * @return
     */
    public int getFriendReplyCount(String blogid, int type, ObjectId id) {
        int count = 0;
        if (type == 1) {
            BasicDBObject query = new BasicDBObject("st", DeleteState.NORMAL.getState());
            query.append("at.id", id);
            count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query);
        } else if (type == 2) {
            MicroBlogEntry microblog = getOneBlogInfo(new ObjectId(blogid), null);
            if (microblog != null) {
                List<ObjectId> objectlist = microblog.getCommentList();
                count = objectlist.size();
            }
        }
        return count;
    }

    public List<MicroBlogEntry> getFriendReplyInfo(String blogid, int type, ObjectId id, int page, int pageSize) {
        List<MicroBlogEntry> retList = new ArrayList<MicroBlogEntry>();
        List<DBObject> list = new ArrayList<DBObject>();
        BasicDBObject query = new BasicDBObject("st", DeleteState.NORMAL.getState());
        query.append("at.id", id);
        list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query, null, Constant.MONGO_SORTBY_DESC, page, pageSize);
        for (DBObject dbo : list) {
            retList.add(new MicroBlogEntry((BasicDBObject) dbo));
        }
        return retList;
    }

    public Long getMicroBlogEntryByParamTotal(List<ObjectId> uis, DeleteState normal, int blogtype, long dsl, long del) {
        BasicDBObject query = new BasicDBObject("ui", new BasicDBObject(Constant.MONGO_IN, uis));

        BasicDBList dblist = new BasicDBList();
        if (dsl > 0) {
            dblist.add(new BasicDBObject("pbt", new BasicDBObject(Constant.MONGO_GTE, dsl)));
        }
        if (del > 0) {
            dblist.add(new BasicDBObject("pbt", new BasicDBObject(Constant.MONGO_LTE, del)));
        }
        if (dblist.size() > 0) {
            query.append(Constant.MONGO_AND, dblist);
        }

        query.append("st", normal.getState());

        if (blogtype != 0) {
            query.append("bt", blogtype);
        }
        query.append("ty", Constant.ONE);

        return (long) count(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query);
    }

    public int getNoticeCount(ObjectId userid, ObjectId schoolid) {
        BasicDBObject query = new BasicDBObject("st", DeleteState.NORMAL.getState());
        query.append("ir", Constant.ZERO);
        query.append("at.id", userid);
        query.append("si", schoolid);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query);
    }

    public void updateViewBlog(ObjectId id) {
        BasicDBObject query = new BasicDBObject("st", DeleteState.NORMAL.getState());
        query.append("at.id", id);
        DBObject value = new BasicDBObject("ir", Constant.ONE);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query, updateValue);
    }

    public Long getPhonePublicMicroBlogCount(ObjectId userId, long currTime) {
        BasicDBObject query = new BasicDBObject("ui", userId);
        BasicDBList dbList = new BasicDBList();
        dbList.add(new BasicDBObject("pf", Platform.Android.getType()));
        dbList.add(new BasicDBObject("pf", Platform.IOS.getType()));
        query.append(Constant.MONGO_OR, dbList);
        query.append("pbt", new BasicDBObject(Constant.MONGO_GTE, currTime));
        query.append("st", DeleteState.NORMAL.getState());
        query.append("ty", Constant.ONE);
        return (long) count(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query);
    }

    /**
     * 根据内容查微博，仅仅应用于小助手活动调查等
     *
     * @param content
     * @return
     */
    public List<MicroBlogEntry> getMicroBlogEntryList(String content) {
        List<MicroBlogEntry> retList = new ArrayList<MicroBlogEntry>();
        BasicDBObject query = new BasicDBObject("con", MongoUtils.buildRegex(content));
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query, Constant.FIELDS, new BasicDBObject("zc", -1));
        for (DBObject dbo : list) {
            retList.add(new MicroBlogEntry((BasicDBObject) dbo));
        }
        return retList;
    }


    /**
     * 微校园未读数量
     *
     * @param schoolHomeDate
     * @param schoolid
     * @return
     */
    public int getSchoolMicroblogCount(long schoolHomeDate, String schoolid) {
        BasicDBObject query = new BasicDBObject("si", new ObjectId(schoolid));
        query.append("pbt", new BasicDBObject(Constant.MONGO_GT, schoolHomeDate));
        query.append("ty", Constant.ONE);
        query.append("bt", Constant.ONE);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query);
    }

    /**
     * 微家园未读数量
     *
     * @param familyHomeDate
     * @param schoolid
     * @return
     */
    public int getFamilyMicroblogCount(long familyHomeDate, String schoolid) {
        BasicDBObject query = new BasicDBObject("si", new ObjectId(schoolid));
        query.append("pbt", new BasicDBObject(Constant.MONGO_GT, familyHomeDate));
        query.append("ty", Constant.ONE);
        query.append("bt", Constant.TWO);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query);
    }

    /**
     * 话题数量
     *
     * @param theme
     * @return
     */
    public int getThemeCount(String theme, String schoolid) {
        BasicDBObject query = new BasicDBObject("st", DeleteState.NORMAL.getState());
        if (!StringUtils.isEmpty(theme)) {
            query.append("con", MongoUtils.buildRegex(theme));
        }
        query.append("ty", Constant.ONE);
        if (!StringUtils.isEmpty(schoolid)) {
            query.append("si", new ObjectId(schoolid));
        }
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query);
    }

    /**
     * 超级小助手查询
     *
     * @param ui                用户ID；不为null时生效
     * @param state
     * @param form              平台；不为null时生效
     * @param atUserId；根据at用户查询
     * @param blogtype          是微校园还是微家园
     * @param hottype           最新还是最热、我的帖子
     * @param fields
     * @param skip              用于分页；大于0时生效
     * @param limit             用于分页；大于0时生效
     * @return
     * @throws ResultTooManyException
     */
    public List<MicroBlogEntry> getBlogEntryList(ObjectId ui, DeleteState state, Platform form, ObjectId atUserId, int blogtype, int hottype, DBObject fields, String schoolid, int skip, int limit, String theme, int seachType, Long startDate, Long endDate, int formtype, String keyword, List<ObjectId> userids, String choose1, String choose2) throws ResultTooManyException {
        List<MicroBlogEntry> retList = new ArrayList<MicroBlogEntry>();
        BasicDBObject query = new BasicDBObject("st", state.getState());
        if (null != ui && hottype == 3) {
            query.append("ui", ui);
        }
        if (null != form) {
            query.append("pf", form.getType());
        }
        if (blogtype != 0) {
            query.append("bt", blogtype);
        }
        if (!StringUtils.isEmpty(theme)) {
            query.append("con", MongoUtils.buildRegex(theme));
        }
        if (Integer.valueOf(choose1) == 1) {
            query.append("ili", new BasicDBObject("$not", new BasicDBObject("$size", 0)));
        } else if (Integer.valueOf(choose1) == 2) {
            query.append("vids.0", new BasicDBObject("$exists", 1));
        }
        if (Integer.valueOf(choose2) == 1) {
            List<ObjectId> users = new ArrayList<ObjectId>();
            users.add(ui);
            query.append("zci", new BasicDBObject(Constant.MONGO_IN, users));
        }
        if (!StringUtils.isEmpty(keyword) && userids != null && userids.size() != 0) {
            BasicDBList values = new BasicDBList();
            values.add(new BasicDBObject("con", MongoUtils.buildRegex(keyword)));
            values.add(new BasicDBObject("ui", new BasicDBObject(Constant.MONGO_IN, userids)));
            query.put(Constant.MONGO_OR, values);
        } else {
            if (!StringUtils.isEmpty(keyword)) {
                query.append("con", MongoUtils.buildRegex(keyword));
            } else if (userids != null && userids.size() != 0) {
                query.append("ui", new BasicDBObject(Constant.MONGO_IN, userids));
            }
        }
        query.append("stp", seachType);
        query.append("ty", Constant.ONE);
        if (!StringUtils.isEmpty(schoolid)) {
            query.append("si", new ObjectId(schoolid));
        }

        if (formtype != 0) {
            query.append("pf", formtype);
        }
        if (null != atUserId) {
            query.append("at.id", atUserId);
        }
        BasicDBList dblist = new BasicDBList();
        if (startDate > 0) {
            dblist.add(new BasicDBObject("pbt", new BasicDBObject(Constant.MONGO_GTE, startDate)));
        }
        if (endDate > 0) {
            dblist.add(new BasicDBObject("pbt", new BasicDBObject(Constant.MONGO_LTE, endDate)));
        }
        if (dblist.size() > 0) {
            query.append(Constant.MONGO_AND, dblist);
        }
        if (query.isEmpty())
            throw new ResultTooManyException();
        List<DBObject> list = new ArrayList<DBObject>();

        if (skip >= 0 && limit > 0) {
            if (hottype == 2) {
                list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query, fields, new BasicDBObject("zc", -1), skip, limit);
            } else {
                list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query, fields, new BasicDBObject("it", -1).append("_id", -1), skip, limit);
            }
        } else {
            if (hottype == 2) {
                list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query, fields, new BasicDBObject("zc", -1));
            } else {
                list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query, fields, new BasicDBObject("it", -1).append("_id", -1));
            }
        }

        for (DBObject dbo : list) {
            retList.add(new MicroBlogEntry((BasicDBObject) dbo));
        }
        return retList;
    }

    /**
     * 超级小助手获取数量
     *
     * @param userid
     * @param hottype
     * @param blogtype
     * @return
     */
    public int getBlogCount(ObjectId userid, int hottype, int blogtype, String schoolid, String theme, int seachType, Long startDate, Long endDate, int formtype, String keyword, List<ObjectId> userids, String choose1, String choose2) {
        BasicDBObject query = new BasicDBObject("st", DeleteState.NORMAL.getState());
        if (null != userid && hottype == 3) {
            query.append("ui", userid);
        }
        if (blogtype != 0) {
            query.append("bt", blogtype);
        }
        query.append("stp", seachType);
        if (formtype != 0) {
            query.append("pf", formtype);
        }
        BasicDBList dblist = new BasicDBList();
        if (startDate > 0) {
            dblist.add(new BasicDBObject("pbt", new BasicDBObject(Constant.MONGO_GTE, startDate)));
        }
        if (endDate > 0) {
            dblist.add(new BasicDBObject("pbt", new BasicDBObject(Constant.MONGO_LTE, endDate)));
        }
        if (dblist.size() > 0) {
            query.append(Constant.MONGO_AND, dblist);
        }
        if (Integer.valueOf(choose1) == 1) {
            query.append("ili", new BasicDBObject("$not", new BasicDBObject("$size", 0)));
        } else if (Integer.valueOf(choose1) == 2) {
            query.append("vids.0", new BasicDBObject("$exists", 1));
        }
        if (Integer.valueOf(choose2) == 1) {
            List<ObjectId> users = new ArrayList<ObjectId>();
            users.add(userid);
            query.append("zci", new BasicDBObject(Constant.MONGO_IN, users));
        }
        if (!StringUtils.isEmpty(theme)) {
            query.append("con", MongoUtils.buildRegex(theme));
        }
        if (!StringUtils.isEmpty(keyword) && userids != null && userids.size() != 0) {
            BasicDBList values = new BasicDBList();
            values.add(new BasicDBObject("con", MongoUtils.buildRegex(keyword)));
            values.add(new BasicDBObject("ui", new BasicDBObject(Constant.MONGO_IN, userids)));
            query.put(Constant.MONGO_OR, values);
        } else {
            if (!StringUtils.isEmpty(keyword)) {
                query.append("con", MongoUtils.buildRegex(keyword));
            } else if (userids != null && userids.size() != 0) {
                query.append("ui", new BasicDBObject(Constant.MONGO_IN, userids));
            }
        }

        query.append("ty", Constant.ONE);
        if (!StringUtils.isEmpty(schoolid)) {
            query.append("si", new ObjectId(schoolid));
        }

        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query);
    }

    /**
     * 获取老师12月份微校园
     *
     * @return
     */
    public List<MicroBlogEntry> getTeacherBlogList() {
        List<MicroBlogEntry> retList = new ArrayList<MicroBlogEntry>();
        BasicDBObject query = new BasicDBObject("st", DeleteState.NORMAL.getState());
        query.append("pf", new BasicDBObject(Constant.MONGO_NE, 1));
        query.append("bt", 1);
        query.append("ty", Constant.ONE);
        BasicDBList dblist = new BasicDBList();
        dblist.add(new BasicDBObject("pbt", new BasicDBObject(Constant.MONGO_GTE, 1451577600141l)));
        dblist.add(new BasicDBObject("pbt", new BasicDBObject(Constant.MONGO_LTE, 1454258219141l)));
        if (dblist.size() > 0) {
            query.append(Constant.MONGO_AND, dblist);
        }
//		query.append("pbt",new BasicDBObject(Constant.MONGO_GTE,1454256000187l));
//		query.append("pbt",new BasicDBObject(Constant.MONGO_LTE,1451577600000l));
        List<DBObject> list = new ArrayList<DBObject>();
        list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query, null, new BasicDBObject("pbt", -1));
        for (DBObject dbo : list) {
            retList.add(new MicroBlogEntry((BasicDBObject) dbo));
        }
        return retList;
    }

    /**
     * 删除多条微博
     *
     * @param objList
     */
    public void delteAllMicroBlog(List<ObjectId> objList) {
        DBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, objList));
        DBObject value = new BasicDBObject("st", DeleteState.DELETED.getState());
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query, updateValue);
    }

    /**
     * 删除多条微博
     *
     * @param objList
     */
    public void canceltop(List<ObjectId> objList) {
        DBObject query = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, objList));
        DBObject value = new BasicDBObject("it", 0);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query, updateValue);
    }

    public void updateZhuTi(long time) {
        DBObject query = new BasicDBObject("pbt", new BasicDBObject(Constant.MONGO_LTE, time))
                .append("st", DeleteState.NORMAL.getState()).append("it", Constant.ONE);
//		DBObject query = new BasicDBObject("st", DeleteState.NORMAL.getState()).append("it",Constant.ONE);
        DBObject value = new BasicDBObject("it", 0);
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query, updateValue);
    }

    public Map<ObjectId, List<MicroBlogEntry>> getMicroBlogEntryListByParam(long dsl, long del, BasicDBObject fields) {
        Map<ObjectId, List<MicroBlogEntry>> map = new HashMap<ObjectId, List<MicroBlogEntry>>();
        BasicDBObject query = new BasicDBObject();
        BasicDBList dblist = new BasicDBList();
        if (dsl > 0) {
            dblist.add(new BasicDBObject("pbt", new BasicDBObject(Constant.MONGO_GTE, dsl)));
        }
        if (del > 0) {
            dblist.add(new BasicDBObject("pbt", new BasicDBObject(Constant.MONGO_LTE, del)));
        }
        if (dblist.size() > 0) {
            query.append(Constant.MONGO_AND, dblist);
        }
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_BLOG_NAME, query, fields);
        List<MicroBlogEntry> mbList = null;
        if (null != list && !list.isEmpty()) {
            MicroBlogEntry e;
            for (DBObject dbo : list) {
                e = new MicroBlogEntry((BasicDBObject) dbo);
                mbList = map.get(e.getSchoolID());
                if (mbList == null) {
                    mbList = new ArrayList<MicroBlogEntry>();
                }
                mbList.add(e);
                map.put(e.getSchoolID(), mbList);
            }
        }
        return map;
    }
}
