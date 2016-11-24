package com.db.studentattendance;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.studentattendance.StudentAttendanceEntry;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;

/**
 * 学生考勤信息dao
 * @author xusy 2016-11-08
 */
public class StudentAttendanceDao extends BaseDao {

	/**
	 * 新增或更新学生考勤信息
	 * @param studentAttendanceEntry
	 * @return
	 */
	public ObjectId saveStudentAttendance(StudentAttendanceEntry studentAttendanceEntry) {
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_STU_ATTENDANCE, studentAttendanceEntry.getBaseEntry());
		return studentAttendanceEntry.getID();
	}
	
	/**
	 * 根据id查询学生考勤信息
	 * @param id
	 * @param fields
	 * @return
	 */
	public StudentAttendanceEntry getStuAttendanceEntry(ObjectId id, DBObject fields) {
		
		DBObject query = new BasicDBObject(Constant.ID, id);
		DBObject object = findOne(MongoFacroty.getAppDB(), 
				Constant.COLLECTION_STU_ATTENDANCE, query, fields);
		if(object != null) {
			return new StudentAttendanceEntry((BasicDBObject) object);
		}
		
		return null;
	}
	
	/**
	 * 根据id修改考勤信息字段值
	 * @param id 
	 * @param field 需要修改的字段
	 * @param value 修改值
	 */
	public void update(ObjectId id, String field, Object value) {
		DBObject query = new BasicDBObject(Constant.ID, id);
		DBObject updateObj = 
				new BasicDBObject(Constant.MONGO_SET, new BasicDBObject(field, value));
		update(MongoFacroty.getAppDB(), 
				Constant.COLLECTION_STU_ATTENDANCE, query, updateObj);
	}
	
	/**
	 * 分页获取班级下的所有考勤信息
	 * @param clazzIds 班级id
	 * @param skip 
	 * @param limit 条数
	 * @return
	 */
	public List<StudentAttendanceEntry> getClazzStuAttendanceInfo(List<ObjectId> clazzIds, int skip, int limit) {
		
		List<StudentAttendanceEntry> stuendanceEntryList = new ArrayList<StudentAttendanceEntry>();
		
		BasicDBObject query = new BasicDBObject("cid", new BasicDBObject(Constant.MONGO_IN, clazzIds));
		BasicDBObject order = new BasicDBObject("sid", Constant.ASC);
		List<DBObject> dbObjList = find(MongoFacroty.getAppDB(), 
				Constant.COLLECTION_STU_ATTENDANCE, 
				query, Constant.FIELDS, order, skip, limit);
		
		for(DBObject obj : dbObjList) {
			stuendanceEntryList.add(new StudentAttendanceEntry((BasicDBObject) obj));
		}
		
		return stuendanceEntryList;
	}
	
	public List<StudentAttendanceEntry> getAttendanceInfo(
			List<ObjectId> gradeIds, List<ObjectId> clazzIds,
			List<String> dateList, String studentName, int type) {
		
		List<StudentAttendanceEntry> stuAttendEntryList = new ArrayList<StudentAttendanceEntry>();
		
		BasicDBObject query = new BasicDBObject("de", new BasicDBObject(Constant.MONGO_IN, dateList));
		if(gradeIds != null && gradeIds.size() > 0) { // 年级
			query.append("gid", new BasicDBObject(Constant.MONGO_IN, gradeIds));
		}
		
		if(clazzIds != null && clazzIds.size() > 0) { // 班级
			query.append("cid", new BasicDBObject(Constant.MONGO_IN, clazzIds));
		}
		
		if(StringUtils.isNotBlank(studentName)) { // 学生姓名，模糊查询
			query.append("sn", MongoUtils.buildRegex(studentName));
		}
		
		if(type != -1) { // -1:所有状态   其他值：具体状态
			query.append("ast", type);
		}
		
		BasicDBObject order = new BasicDBObject("de", Constant.DESC);
		
		List<DBObject> objList = find(MongoFacroty.getAppDB(), 
				Constant.COLLECTION_STU_ATTENDANCE, query, Constant.FIELDS, order);
		for(DBObject obj : objList) {
			stuAttendEntryList.add(new StudentAttendanceEntry((BasicDBObject) obj));
		}
		
		return stuAttendEntryList;
	}
	
	/**
	 * 根据所给条件查询考勤信息
	 * @param clazzId 班级id
	 * @param dateList 日期list
	 * @param studentName 学生姓名
	 * @param type 考勤状态
	 * @return
	 */
	public List<StudentAttendanceEntry> getAttendanceInfo(
			ObjectId clazzId, List<String> dateList, String studentName, int type) {
		
		List<StudentAttendanceEntry> stuAttendEntry = new ArrayList<StudentAttendanceEntry>();
		
		BasicDBObject query = new BasicDBObject("cid", clazzId);
		query.append("de", new BasicDBObject(Constant.MONGO_IN, dateList));
		if(type != -1) {
			query.append("ast", type);
		}
		if(StringUtils.isNotBlank(studentName)) { // 模糊查询
			query.append("sn", MongoUtils.buildRegex(studentName));
		}
		
		List<DBObject> objectList = find(MongoFacroty.getAppDB(), Constant.COLLECTION_STU_ATTENDANCE, query, Constant.FIELDS);
		for(DBObject obj : objectList) {
			stuAttendEntry.add(new StudentAttendanceEntry((BasicDBObject) obj));
		}
		
		return stuAttendEntry;
	}
	
	/**
	 * 删除一条记录
	 * @param id
	 */
	public void deleteById(ObjectId id) {
		
		BasicDBObject query = new BasicDBObject(Constant.ID, id);
		
		remove(MongoFacroty.getAppDB(), Constant.COLLECTION_STU_ATTENDANCE, query);
	}
	
}
