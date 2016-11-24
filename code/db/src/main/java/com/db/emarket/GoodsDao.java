package com.db.emarket;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.FieldValuePair;
import com.pojo.emarket.Comment;
import com.pojo.emarket.GoodsEntry;
import com.pojo.emarket.GoodsState;
import com.pojo.emarket.OrderEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 商品操作类
 *
 * @author fourer
 */
public class GoodsDao extends BaseDao {

    private static final DBObject ORDER = new BasicDBObject("lut", Constant.DESC).append("sc", Constant.DESC).append("pr", Constant.ASC);

    /**
     * 添加
     *
     * @param e
     * @return
     */
    public ObjectId addGoodsEntry(GoodsEntry e) {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_GOODS_NAME, e.getBaseEntry());
        return e.getID();
    }


    /**
     * 更新多个字段值
     *
     * @param id
     * @param pairs
     */
    public void update(ObjectId id, FieldValuePair... pairs) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject value = new BasicDBObject();
        for (FieldValuePair pair : pairs) {
            value.append(pair.getField(), pair.getValue());
        }
        BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET, value);
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_GOODS_NAME, query, updateValue);
    }

    /**
     * 得到商品
     *
     * @param id
     * @return
     */
    public GoodsEntry getGoodsEntry(ObjectId id, DBObject fields) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_GOODS_NAME, query, fields);
        if (null != dbo) {
            return new GoodsEntry((BasicDBObject) dbo);
        }
        return null;
    }


    /**
     * @param sub    科目;大于-1时生效
     * @param grades 年级 ;大于-1时生效
     * @param type   种类;大于-1时生效
     * @param state  状态;大于-1时生效
     * @param name   搜索的名字
     * @param owner  拥有者
     * @param order  排序
     * @param skip
     * @param limit
     * @return
     * @throws IllegalParamException
     */
    public List<GoodsEntry> getGoodsList(int sub, List<Integer> grades, int type, int state, String name, List<ObjectId> users, ObjectId owner, DBObject fields, DBObject order, int skip, int limit, List<ObjectId> userids) throws IllegalParamException {
        BasicDBObject dbo = buildQuery(sub, grades, type, state, name, users, owner, userids);
        List<GoodsEntry> retList = new ArrayList<GoodsEntry>();

        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_GOODS_NAME, dbo, fields, null == ORDER ? ORDER : order, skip, limit);
        if (null != list && list.size() > 0) {
            for (DBObject dBObject : list) {
                retList.add(new GoodsEntry((BasicDBObject) dBObject));
            }
        }
        return retList;
    }

    /**
     * @param sub    科目;大于-1时生效
     * @param grades 年级 ;大于-1时生效
     * @param type   种类;大于-1时生效
     * @param state  状态;大于-1时生效
     * @param name   搜索的名字
     * @param owner  拥有者
     * @param order  排序
     * @return
     * @throws IllegalParamException
     */
    public List<GoodsEntry> getGoodsList(int sub, List<Integer> grades, int type, int state, String name, List<ObjectId> users, ObjectId owner, DBObject fields, DBObject order, List<ObjectId> userids) throws IllegalParamException {
        BasicDBObject dbo = buildQuery(sub, grades, type, state, name, users, owner, userids);
        List<GoodsEntry> retList = new ArrayList<GoodsEntry>();

        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_GOODS_NAME, dbo, fields, null == ORDER ? ORDER : order);
        if (null != list && list.size() > 0) {
            for (DBObject dBObject : list) {
                retList.add(new GoodsEntry((BasicDBObject) dBObject));
            }
        }
        return retList;
    }

    /**
     * 根据ID查询商品
     *
     * @param goodsIds
     * @param fields
     * @return
     * @throws IllegalParamException
     */
    public List<GoodsEntry> getGoodsList(Collection<ObjectId> goodsIds, DBObject fields) throws IllegalParamException {
        BasicDBObject dbo = new BasicDBObject(Constant.ID, new BasicDBObject(Constant.MONGO_IN, goodsIds));
        List<GoodsEntry> retList = new ArrayList<GoodsEntry>();

        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_GOODS_NAME, dbo, fields);
        if (null != list && list.size() > 0) {
            for (DBObject dBObject : list) {
                retList.add(new GoodsEntry((BasicDBObject) dBObject));
            }
        }
        return retList;
    }


    /**
     * @param sub    科目;大于-1时生效
     * @param grades 年级 ;大于-1时生效
     * @param type   种类;大于-1时生效
     * @param state  状态;大于-1时生效
     * @param name   搜索的名字
     * @param owner  拥有者
     * @return
     * @throws IllegalParamException
     */
    public int countGoods(int sub, List<Integer> grades, int type, int state, String name, List<ObjectId> users,
                          ObjectId owner, List<ObjectId> userids) throws IllegalParamException {
        BasicDBObject dbo = buildQuery(sub, grades, type, state, name, users, owner, userids);
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_GOODS_NAME, dbo);

    }


    /**
     * 增加一个评论
     *
     * @param id
     * @param comment
     */
    public void addComment(ObjectId id, Comment comment) {
        DBObject dbo = new BasicDBObject(Constant.ID, id);
        BasicDBList list = new BasicDBList();
        list.add(comment.getBaseEntry());
        BasicDBObject operDBO = new BasicDBObject(Constant.MONGO_EACH, list).append(Constant.MONGO_SORT, new BasicDBObject("t", -1));
        DBObject updateValue = new BasicDBObject(Constant.MONGO_PUSH, new BasicDBObject("coms", operDBO))
                .append(Constant.MONGO_INC, new BasicDBObject("comc", Constant.ONE));
        ;
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_GOODS_NAME, dbo, updateValue);
    }


    /**
     * 得到商品,带有评论
     *
     * @param id
     * @param skip
     * @param limit
     * @return
     */
    public GoodsEntry getGoodsEntry(ObjectId id, int skip, int limit) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBList list = new BasicDBList();
        list.add(skip);
        list.add(limit);
        DBObject fields = new BasicDBObject("coms", new BasicDBObject(Constant.MONGO_SLICE, list));
        DBObject dbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_GOODS_NAME, query, fields);
        if (null != dbo) {
            GoodsEntry e = new GoodsEntry((BasicDBObject) dbo);
            return e;
        }
        return null;
    }


    /**
     * 删除最早的商品评论
     *
     * @param id
     */
    public void deleteComment(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        DBObject updateValue = new BasicDBObject(Constant.MONGO_POP, new BasicDBObject("coms", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_GOODS_NAME, query, updateValue);
    }


    private BasicDBObject buildQuery(int sub, List<Integer> grades, int type,
                                     int state, String name, List<ObjectId> users, ObjectId owner, List<ObjectId> userids) throws IllegalParamException {
        BasicDBObject dbo = new BasicDBObject();
        List<Integer> subjects = new ArrayList<Integer>();
        if (sub > Constant.NEGATIVE_ONE) {
            subjects.add(sub);
            dbo.append("subs", new BasicDBObject(Constant.MONGO_IN, subjects));
        }
        if (null != grades && !grades.isEmpty()) {
            dbo.append("ccgts", new BasicDBObject(Constant.MONGO_IN, grades));
        }
        if (type > Constant.NEGATIVE_ONE) {
            dbo.append("ty", type);
        }
        if (state > Constant.NEGATIVE_ONE) {
            dbo.append("st", state);
        }
        if (StringUtils.isNotBlank(name)) {
            if ((null != users && users.size() != 0) || (null != userids && userids.size() != 0)) {
                BasicDBList values = new BasicDBList();
                if (null != users && users.size() != 0) {
                    values.add(new BasicDBObject("ow", new BasicDBObject(Constant.MONGO_IN, users)));
                }
                if (null != userids && userids.size() != 0) {
                    values.add(new BasicDBObject("ow", new BasicDBObject(Constant.MONGO_IN, userids)));
                }
                values.add(new BasicDBObject("nm", MongoUtils.buildRegex(name)));

                dbo.put(Constant.MONGO_OR, values);
            } else {
                dbo.append("nm", MongoUtils.buildRegex(name));
            }

        }
        if (null != owner) {
            dbo.append("ow", owner);
        }

        if (dbo.isEmpty()) {
            throw new IllegalParamException();
        }
        return dbo;
    }


    /**
     * @param goodid
     * @param endtime
     */
    public void updateExpiretime(ObjectId goodid, long endtime) {
        DBObject query = new BasicDBObject(Constant.ID, goodid);
        BasicDBObject updatevalue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("enddt", endtime).append("st", GoodsState.SHELVE.getType()));

//		updatevalue.append(Constant.MONGO_SET,new BasicDBObject("st", GoodsState.SHELVE.getType()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_GOODS_NAME, query, updatevalue);
    }

    /**
     * 删除商品
     *
     * @param id
     */
    public void deleteGoods(ObjectId id) {
        DBObject query = new BasicDBObject(Constant.ID, id);
        BasicDBObject updatevalue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("st", GoodsState.DELETE.getType()));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_GOODS_NAME, query, updatevalue);
    }

    /**
     * 更新商品
     *
     * @param relateId
     * @param goodsid
     * @param gradeIds
     * @param price
     * @param expiretime
     * @param lessonContent
     * @param isopen
     * @param userId
     */
    public void updateGoodEntry(ObjectId relateId, String goodsid, List<Integer> subjects, List<Integer> gradeIds, double price, long expiretime, String lessonContent, int isopen, String userId) {
        DBObject query = new BasicDBObject(Constant.ID, new ObjectId(goodsid));
        /*BasicDBObject updatevalue = new BasicDBObject(Constant.MONGO_SET,new BasicDBObject("pr",price));
        updatevalue.append(Constant.MONGO_SET, new BasicDBObject("et", expiretime))
                .append(Constant.MONGO_SET,new BasicDBObject("io", isopen))
                .append(Constant.MONGO_SET, new BasicDBObject("desc", lessonContent))
                .append(Constant.MONGO_PUSH, new BasicDBObject("subs", MongoUtils.convert(subjects)))
                .append(Constant.MONGO_PUSH,new BasicDBObject("ccgts",MongoUtils.convert(gradeIds)));*/

        BasicDBObject updatevalue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("pr", price)
                .append("et", expiretime)
                .append("io", isopen)
                .append("desc", lessonContent)
                .append("subs", MongoUtils.convert(subjects))
                .append("ccgts", MongoUtils.convert(gradeIds))
        );
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_GOODS_NAME, query, updatevalue);

    }

    /**
     * @param userid
     * @param skip
     * @param limit
     */
    public List<OrderEntry> selmyExpireLesson(String userid, int skip, int limit) {
        BasicDBObject dbo = new BasicDBObject();
        dbo.append("ow", new ObjectId(userid));
        dbo.append("enddt", new BasicDBObject("$lt", new Date().getTime()));
        dbo.append("enddt", new BasicDBObject("$ne", 0));
        List<OrderEntry> retList = new ArrayList<OrderEntry>();
        List<DBObject> list = find(MongoFacroty.getAppDB(), Constant.COLLECTION_GOODS_NAME, dbo, null, Constant.MONGO_SORTBY_DESC, skip, limit);
        if (null != list && list.size() > 0) {
            for (DBObject dBObject : list) {
                GoodsEntry goods = new GoodsEntry((BasicDBObject) dBObject);
                BasicDBObject query = new BasicDBObject();
                query.append("gi.id", goods.getID());

                DBObject orderdbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_ORDERS_NAME, query, null);
                retList.add(new OrderEntry((BasicDBObject) orderdbo));
            }
        }

        return retList;
    }

    public int selmyExpireLessonCount(String userid) {
        BasicDBObject dbo = new BasicDBObject();
        dbo.append("ow", new ObjectId(userid));
        dbo.append("enddt", new BasicDBObject("$lt", new Date().getTime()));
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_GOODS_NAME, dbo);
    }

    public GoodsEntry getGoodsEntryBylessonId(ObjectId id) {
        DBObject dbo = new BasicDBObject("rid", id);
        DBObject orderdbo = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_GOODS_NAME, dbo, Constant.FIELDS);
        if (null != orderdbo) {
            return new GoodsEntry((BasicDBObject) orderdbo);
        }
        return null;
    }

    public void updateCount(ObjectId relateGoodId) {
        DBObject query = new BasicDBObject(Constant.ID, relateGoodId);
        BasicDBObject updatevalue = new BasicDBObject(Constant.MONGO_INC, new BasicDBObject("sc", Constant.ONE));
        update(MongoFacroty.getAppDB(), Constant.COLLECTION_GOODS_NAME, query, updatevalue);

    }

}
