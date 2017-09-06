package com.db.wrongquestion;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.utils.MongoUtils;
import com.pojo.wrongquestion.ErrorBookEntry;
import com.pojo.wrongquestion.ErrorBookEntry.AnswerExplain;
import com.pojo.wrongquestion.ErrorBookEntry.ErrorBookAttach;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * 在线练习-我的错题本Dao
 * 
 * added by xusy 2017-03-14
 */
public class ErrorBookDao extends BaseDao {
	
	/**
	 * 添加错题
	 * @param entry
	 */
	public ObjectId addToErrorBook(ErrorBookEntry entry) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_BOOK_ERROR, entry.getBaseEntry());
		return entry.getID();
	}
	
	/**
	 * 更新悬赏状态
	 * 
	 * @param id
	 * @param status
	 */
	public void updateRewardStatus(ObjectId id, int status) {
		BasicDBObject query = new BasicDBObject(Constant.ID, id);
		BasicDBObject updateValue = 
				new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("isrd", status));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_BOOK_ERROR, query, updateValue);
	}
	
	/**
	 * 更新错题是否掌握（学会）状态
	 * 
	 * @param id
	 * @param status
	 */
	public void updateGraspStatus(ObjectId id, int status) {
		BasicDBObject query = new BasicDBObject(Constant.ID, id);
		BasicDBObject updateValue = 
				new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("isgp", status));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_BOOK_ERROR, query, updateValue);
	}
	
	/**
	 * 获取错题
	 * 
	 * @param id
	 * @return
	 */
	public ErrorBookEntry getFromErrorBook(ObjectId id) {
		BasicDBObject query = new BasicDBObject(Constant.ID, id);
		DBObject obj = 
				findOne(MongoFacroty.getAppDB(),
						Constant.COLLECTION_BOOK_ERROR,
						query, Constant.FIELDS);
		if(obj != null) {
			return new ErrorBookEntry((BasicDBObject) obj);
		}
		
		return null;
	}
	
	/**
	 * 将错题从错题本中删除
	 * 
	 * @param id
	 */
	public void removeFromErrorBook(ObjectId id) {
		BasicDBObject query = new BasicDBObject(Constant.ID, id);
		BasicDBObject updateValue = 
				new BasicDBObject(Constant.MONGO_SET,
						new BasicDBObject("ir", 1));
		
		update(MongoFacroty.getAppDB(),
				Constant.COLLECTION_BOOK_ERROR,
				query, 
				updateValue);
	}
	
	/**
	 * 为错题添加解析
	 * 
	 * @param id
	 * @param explain
	 */
	public void addExplainToQuestion(ObjectId id, AnswerExplain explain) {
		BasicDBObject query = new BasicDBObject(Constant.ID, id);
		BasicDBObject updateValue = 
				new BasicDBObject(Constant.MONGO_PUSH,
						new BasicDBObject("exs", explain.getBaseEntry()));
		
		update(MongoFacroty.getAppDB(),
				Constant.COLLECTION_BOOK_ERROR,
				query, 
				updateValue);
	}
	
	/**
	 * 删除错题解析
	 * 
	 * @param id
	 * @param explainId
	 */
	public void removeExplainFromQuestin(ObjectId id, ObjectId explainId) {
		BasicDBObject query = new BasicDBObject(Constant.ID, id);
		BasicDBObject updateValue = 
				new BasicDBObject(Constant.MONGO_PULL,
						new BasicDBObject("exs", new BasicDBObject("id", explainId)));
		
		update(MongoFacroty.getAppDB(),
				Constant.COLLECTION_BOOK_ERROR,
				query, 
				updateValue);
	}
	
	/**
	 * 添加错题答案
	 * 
	 * @param id
	 * @param answer
	 */
	public void addAnswer(ObjectId id, String answer) {
		BasicDBObject query = new BasicDBObject(Constant.ID, id);
		BasicDBObject updateValue = 
				new BasicDBObject(Constant.MONGO_SET,
						new BasicDBObject("an", answer));
	}
	
	/**
	 * 删除附件
	 * 
	 * @param id
	 * @param attachmentId
	 */
	public void removeAttachment(ObjectId id, ObjectId attachmentId) {
		BasicDBObject query = new BasicDBObject(Constant.ID, id);
		BasicDBObject updateValue = 
				new BasicDBObject(Constant.MONGO_PULL,
						new BasicDBObject("atta", new BasicDBObject("id", attachmentId)));
		
		update(MongoFacroty.getAppDB(),
				Constant.COLLECTION_BOOK_ERROR,
				query, 
				updateValue);
	}
	
	/**
	 * 删除附件
	 * 
	 * @param id
	 * @param
	 */
	public void removeAttachment(ObjectId id, List<ErrorBookAttach> removeAttachList) {
		BasicDBObject query = new BasicDBObject(Constant.ID, id);
		BasicDBObject updateValue = 
				new BasicDBObject(Constant.MONGO_PULLALL,
						new BasicDBObject("atta", MongoUtils.fetchDBObjectList(removeAttachList)));
		
		update(MongoFacroty.getAppDB(),
				Constant.COLLECTION_BOOK_ERROR,
				query, 
				updateValue);
	}
	
	/**
	 * 添加附件
	 * 
	 * @param id
	 * @param attachment
	 */
	public void addAttachment(ObjectId id, ErrorBookAttach attachment) {
		BasicDBObject query = new BasicDBObject(Constant.ID, id);
		BasicDBObject updateValue = 
				new BasicDBObject(Constant.MONGO_PUSH,
						new BasicDBObject("atta", attachment.getBaseEntry()));
		
		update(MongoFacroty.getAppDB(),
				Constant.COLLECTION_BOOK_ERROR,
				query, 
				updateValue);
	}
	
	/**
	 * 添加附件
	 * 
	 * @param id
	 * @param attaList
	 */
	public List<ObjectId> addAttachment(ObjectId id, List<ErrorBookAttach> attaList) {
		BasicDBObject query = new BasicDBObject(Constant.ID, id);
		BasicDBObject updateValue = 
				new BasicDBObject(Constant.MONGO_PUSHALL,
						new BasicDBObject("atta", 
								MongoUtils.convert(MongoUtils.fetchDBObjectList(attaList))));
		update(MongoFacroty.getAppDB(),
				Constant.COLLECTION_BOOK_ERROR,
				query, 
				updateValue);
		
		List<ObjectId> idList = new ArrayList<ObjectId>();
		if(attaList != null 
				&& !attaList.isEmpty()) {
			for(ErrorBookAttach atta : attaList) {
				idList.add(atta.getErrorBookAttachId());
			}
		}
		
		return idList;
	}
	
	/**
	 * 查询数量
	 *  
	 * @param userId
	 * @param subjectId
	 * @param gradeId
	 * @param pointList
	 * @param keyword
	 * @return
	 */
	public int countQuestion(
			ObjectId userId, 
			ObjectId subjectId,
			ObjectId gradeId,
			List<ObjectId> pointList, 
			String keyword,
			boolean isErrorQuestion) {
		
		BasicDBObject query = 
				new BasicDBObject("uid", userId)
				.append("ir", Constant.ZERO);
		
		if(subjectId != null) {
			query.append("sbj", subjectId);
		}
		
		if(gradeId != null) {
			query.append("gid", gradeId);
		}
		
		if(pointList != null 
				&& !pointList.isEmpty()) {
			query.append("kps", new BasicDBObject(Constant.MONGO_IN, pointList));
		}
		
		if(StringUtils.isNotBlank(keyword)) {
			//BasicDBObject explainRex = new BasicDBObject("exs.epl", MongoUtils.buildRegex(keyword));
			BasicDBObject anRex = new BasicDBObject("an", MongoUtils.buildRegex(keyword));
			BasicDBObject descRex = new BasicDBObject("desc", MongoUtils.buildRegex(keyword));
			
			BasicDBList orList = new BasicDBList();
			//orList.add(explainRex);
			orList.add(anRex);
			orList.add(descRex);
			
			query.append(Constant.MONGO_OR, orList);
		}
		
		// 查询错题还是掌握的习题
		if (isErrorQuestion) {
			query.append("isgp", new BasicDBObject(Constant.MONGO_NE, Constant.ONE));
		} else {
			query.append("isgp", Constant.ONE);
		}
		
		return count(MongoFacroty.getAppDB(), Constant.COLLECTION_BOOK_ERROR, query);
	}
	
	/**
	 * 分页查询我的错题
	 * 
	 * @param userId
	 * @param subjectId
	 * @param pointList
	 * @param keyword
	 * @param skip
	 * @param pageSize
	 * @return
	 */
	public List<ErrorBookEntry> findErrorQuestionWithPaging(
			ObjectId userId, 
			ObjectId subjectId,
			ObjectId gradeId,
			List<ObjectId> pointList, 
			String keyword, int skip, int pageSize, boolean isErrorQuestion) {
		
		BasicDBObject query = 
				new BasicDBObject("uid", userId)
				.append("ir", Constant.ZERO);
		
		if(subjectId != null) {
			query.append("sbj", subjectId);
		}
		
		if(gradeId != null) {
			query.append("gid", gradeId);
		}
		
		if(pointList != null 
				&& !pointList.isEmpty()) {
			query.append("kps", new BasicDBObject(Constant.MONGO_IN, pointList));
		}
		
		if(StringUtils.isNotBlank(keyword)) {
			//BasicDBObject explainRex = new BasicDBObject("exs.epl", MongoUtils.buildRegex(keyword));
			BasicDBObject anRex = new BasicDBObject("an", MongoUtils.buildRegex(keyword));
			BasicDBObject descRex = new BasicDBObject("desc", MongoUtils.buildRegex(keyword));
			
			BasicDBList orList = new BasicDBList();
			//orList.add(explainRex);
			orList.add(anRex);
			orList.add(descRex);
			
			query.append(Constant.MONGO_OR, orList);
		}
		
		// 查询错题还是掌握的习题
		if (isErrorQuestion) {
			query.append("isgp", new BasicDBObject(Constant.MONGO_NE, Constant.ONE));
		} else {
			query.append("isgp", Constant.ONE);
		}
		
		List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(),
				Constant.COLLECTION_BOOK_ERROR, query,
				Constant.FIELDS, Constant.MONGO_SORTBY_ASC, skip, pageSize);
		List<ErrorBookEntry> bookEntryList = new ArrayList<ErrorBookEntry>();
		if(dbObjectList != null 
				&& !dbObjectList.isEmpty()) {
			for(DBObject obj : dbObjectList) {
				bookEntryList.add(new ErrorBookEntry((BasicDBObject) obj));
			}
		}
		
		return bookEntryList;
	}

	public int getErrorQuestionCount(ObjectId id) {
		BasicDBObject query = new BasicDBObject(Constant.ID, id);
		return count(MongoFacroty.getAppDB(), Constant.COLLECTION_BOOK_ERROR, query);
	}

	public List<ErrorBookEntry> getErrorItemByIds(List<ObjectId> ids) {
		BasicDBObject query = new BasicDBObject("ir", Constant.ZERO);
		query.append(Constant.ID, new BasicDBObject(Constant.MONGO_IN, ids));
		List<DBObject> dbObjectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_BOOK_ERROR, query, Constant.FIELDS, Constant.MONGO_SORTBY_ASC);
		List<ErrorBookEntry> bookEntryList = new ArrayList<ErrorBookEntry>();
		if(dbObjectList != null && !dbObjectList.isEmpty()) {
			for(DBObject obj : dbObjectList) {
				bookEntryList.add(new ErrorBookEntry((BasicDBObject) obj));
			}
		}
		return bookEntryList;
	}
}
