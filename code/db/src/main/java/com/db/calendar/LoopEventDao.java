package com.db.calendar;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.calendar.LoopEvent;
import com.sys.constants.Constant;

/**
 * 循环事件操作
 * 
 * @author fourer
 *
 */
public class LoopEventDao extends BaseDao {

	/**
	 * 添加一个循环事件
	 * 
	 * @param e
	 * @return
	 */
	public ObjectId addLoopEvent(LoopEvent e) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_LOOPEVENT_NAME,
				e.getBaseEntry());
		return e.getID();
	}

	/**
	 * 删除
	 * 
	 * @param userid
	 * @param id
	 */
	public void remove(ObjectId userid, ObjectId id) {
		BasicDBObject query = new BasicDBObject("uid", userid).append(
				Constant.ID, id);
		remove(MongoFacroty.getAppDB(), Constant.COLLECTION_LOOPEVENT_NAME,
				query);
	}

	/**
	 * 详情
	 * 
	 * @param userid
	 * @param id
	 */
	public LoopEvent getLoopEvent(ObjectId userid, ObjectId id) {
		BasicDBObject query = new BasicDBObject("uid", userid).append(
				Constant.ID, id);

		DBObject dbo = findOne(MongoFacroty.getAppDB(),
				Constant.COLLECTION_LOOPEVENT_NAME, query, Constant.FIELDS);
		if (null != dbo) {
			return new LoopEvent((BasicDBObject) dbo);
		}
		return null;
	}

	/**
	 * 
	 * @param userid
	 * @return
	 */
	public List<LoopEvent> getLoopEventList(ObjectId userid, long endTime) {
		List<LoopEvent> retList = new ArrayList<LoopEvent>();
		BasicDBObject query = new BasicDBObject("uid", userid).append("bt",
				new BasicDBObject(Constant.MONGO_LTE, endTime));

		List<DBObject> res = find(MongoFacroty.getAppDB(),
				Constant.COLLECTION_LOOPEVENT_NAME, query, Constant.FIELDS);
		if (null != res && !res.isEmpty()) {
			for (DBObject dbo : res) {
				retList.add(new LoopEvent((BasicDBObject) dbo));
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
	public void updateRemoves(ObjectId userid, ObjectId id, long begintime) {
		BasicDBObject query = new BasicDBObject("uid", userid).append(
				Constant.ID, id);
		BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_SET,
				new BasicDBObject().append("reb", begintime));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_LOOPEVENT_NAME,
				query, updateValue);
	}

	
	/**
	 * 添加删除日期
	 * @param date
	 * @param userid
	 * @param id
	 */
	public void addDeleteDate(String date, ObjectId userid, ObjectId id) {
		BasicDBObject query = new BasicDBObject("uid", userid).append(
				Constant.ID, id);
		BasicDBObject updateValue = new BasicDBObject(Constant.MONGO_PUSH,
				new BasicDBObject("dld", date));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_LOOPEVENT_NAME,
				query, updateValue);
	}

}
