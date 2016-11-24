package com.db.calendar;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.calendar.Event;
import com.sys.constants.Constant;
/**
 * 单个事件操作
 * @author fourer
 *
 */
public class EventDao extends BaseDao {

	/**
	 * 添加
	 * 
	 * @param e
	 * @return
	 */
	public ObjectId addEvent(Event e) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_EVENT_NAME,
				e.getBaseEntry());
		return e.getID();
	}

	/**
	 * 刪除
	 * 
	 * @param userid
	 * @param id
	 */
	public void removeEvent(ObjectId userid, ObjectId id) {
		BasicDBObject query = new BasicDBObject("uid", userid).append(
				Constant.ID, id);
		remove(MongoFacroty.getAppDB(), Constant.COLLECTION_EVENT_NAME, query);
	}

	/**
	 * 详情
	 * 
	 * @param userid
	 * @param id
	 * @return
	 */
	public Event getEvent(ObjectId userid, ObjectId id) {
		BasicDBObject query = new BasicDBObject("uid", userid).append(
				Constant.ID, id);

		DBObject dbo = findOne(MongoFacroty.getAppDB(),
				Constant.COLLECTION_EVENT_NAME, query, Constant.FIELDS);
		if (null != dbo) {
			return new Event((BasicDBObject) dbo);
		}
		return null;
	}

	/**
	 * 查詢
	 * 
	 * @param uid
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public List<Event> getEventList(ObjectId uid, long beginTime, long endTime) {
		List<Event> retList = new ArrayList<Event>();
		BasicDBObject query = new BasicDBObject("uid", uid);
		BasicDBList list = new BasicDBList();
		list.add(new BasicDBObject().append("bt",
				new BasicDBObject(Constant.MONGO_LTE, endTime)).append("et",
				new BasicDBObject(Constant.MONGO_GTE, endTime)));
		list.add(new BasicDBObject().append("bt",
				new BasicDBObject(Constant.MONGO_LTE, beginTime)).append("et",
				new BasicDBObject(Constant.MONGO_GTE, beginTime)));
		list.add(new BasicDBObject().append("bt",
				new BasicDBObject(Constant.MONGO_GTE, beginTime)).append("et",
				new BasicDBObject(Constant.MONGO_LTE, endTime)));
		list.add(new BasicDBObject().append("bt", new BasicDBObject(
				Constant.MONGO_GTE, beginTime).append(Constant.MONGO_LTE,
				endTime)));
		query.append(Constant.MONGO_OR, list);

		List<DBObject> res = find(MongoFacroty.getAppDB(),
				Constant.COLLECTION_EVENT_NAME, query, Constant.FIELDS);
		if (null != res && !res.isEmpty()) {
			for (DBObject dbo : res) {
				retList.add(new Event((BasicDBObject) dbo));
			}
		}
		return retList;
	}

	/**
	 * 标记删除
	 * 
	 * @param userid
	 * @param id
	 * @param type
	 * @param begintime
	 */
	public void updateRemoves(ObjectId userid, ObjectId id, int type, long begintime) {
		BasicDBObject query = new BasicDBObject("uid", userid).append(
				Constant.ID, id);
		BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
				new BasicDBObject("ret", type).append("reb", begintime));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_EVENT_NAME, query,
				updateValue);
	}

}
