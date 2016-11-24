package com.db.functionclassroom;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.funcitonclassroom.ClassRoomAppointmentEntry;
import com.pojo.funcitonclassroom.ClassRoomEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;



public class FunctionClassRoomDao extends BaseDao  {
	/**
	 * 根据使用者id查询预约表
	 * @param userId
	 * @author caotiecheng
	 * @return 
	 */
	public List<ClassRoomAppointmentEntry> myClassRoomAppointment(ObjectId userId,int skip,int size){
		BasicDBObject query = new BasicDBObject("ir",Constant.ZERO);
		query.append("uid", userId);
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(),Constant.COLLECTION_CLASSROOM_APPOINTMENT,query,Constant.FIELDS,new BasicDBObject().append("st", Constant.DESC),skip,size);
        List<ClassRoomAppointmentEntry> resultList = new ArrayList<ClassRoomAppointmentEntry>();
        for(DBObject dbObject:dbObjects){
        	ClassRoomAppointmentEntry classRoomAppointmentEntry = new ClassRoomAppointmentEntry((BasicDBObject)dbObject);
        	resultList.add(classRoomAppointmentEntry);
        }
        
		return resultList;
	}
	
	/**
	 * 根据启始时间及用户id查询预约表
	 * @param userId
	 * @param searchTmStart
	 * @param searchTmEnd
	 * @param skip
	 * @param size
	 * @return
	 * @author caotiecheng
	 */
	public List<ClassRoomAppointmentEntry> myAppointBySearchTime(ObjectId userId,Long searchTmStart,Long searchTmEnd,int skip,int size){
		BasicDBObject query = new BasicDBObject("uid", userId).append("ir", Constant.ZERO);
		BasicDBList values = new BasicDBList();
				// et>endTime & st<startTime
				values.add(new BasicDBObject()
					.append("et", new BasicDBObject(Constant.MONGO_GT, searchTmEnd))
					.append("st", new BasicDBObject(Constant.MONGO_LT, searchTmStart)));
				// et>startTime & st<startTime
				values.add(new BasicDBObject()
					.append("et", new BasicDBObject(Constant.MONGO_GT, searchTmStart))
					.append("st", new BasicDBObject(Constant.MONGO_LT, searchTmStart)));
				// st<startTime & et>endTime
				values.add(new BasicDBObject()
					.append("st", new BasicDBObject(Constant.MONGO_LT, searchTmEnd))
					.append("et", new BasicDBObject(Constant.MONGO_GT, searchTmEnd)));
				// st>startTime & et<endTime
				values.add(new BasicDBObject()
					.append("st", new BasicDBObject(Constant.MONGO_GT, searchTmStart))
					.append("et", new BasicDBObject(Constant.MONGO_LT, searchTmEnd)));
		
		query.put(Constant.MONGO_OR, values);
		List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM_APPOINTMENT, query, Constant.FIELDS,new BasicDBObject().append("st", Constant.DESC),skip,size);
		if(dbObjects.size()==0){
			return null;
		}else{
			//转换数据类型
			List<ClassRoomAppointmentEntry> resultList = new ArrayList<ClassRoomAppointmentEntry>();
			for(DBObject dbObject: dbObjects){
				resultList.add(new ClassRoomAppointmentEntry((BasicDBObject)dbObject));
			}
			return resultList;
		}
	}
	
	/**
	 * 根据预约id更新起始时间、结束时间、预约说明
	 * @param id
	 * @param classRoomName
	 * @param startTime
	 * @param endTime
	 * @author caotiecheng
	 */
	public void updateMyAppointmentByAppointmentId(ObjectId id,String reasons,Long startTime,Long endTime){

		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,
				new BasicDBObject()
		.append("rs", reasons)
		.append("st", startTime)
		.append("et", endTime));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM_APPOINTMENT, query, updateValue);

	}
	
	/**
	 * 根据预约id删除一条预约表数据
	 * @param id
	 * @author caotiecheng
	 */
	public void deleteRepair(ObjectId id){
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject().append("ir", 1));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM_APPOINTMENT, query, updateValue);
	}
	/**
	 * 添加功能教室
	 * @param e
	 * @author huanxiaolei@ycode.cn
	 */
	public ObjectId addFunctionClassRoom(ClassRoomEntry e){
		save(MongoFacroty.getAppDB(),Constant.COLLECTION_FUNCTION_CLASSROOM,e.getBaseEntry());
		return e.getID();
	}
	/**
	 * 功能教室列表
	 * @return
	 * @author huanxiaolei@ycode.cn
	 */
	public List<ClassRoomEntry> findClassRoomEntryList(ObjectId schoolId){
		DBObject query = new BasicDBObject("sid",schoolId).append("ir", Constant.ZERO);
		DBObject orderBy = new BasicDBObject("num",Constant.ASC);
		List<DBObject> dbObjects = find(MongoFacroty.getAppDB(),Constant.COLLECTION_FUNCTION_CLASSROOM,query,Constant.FIELDS,orderBy);
		List<ClassRoomEntry> resultList = new ArrayList<ClassRoomEntry>();
		for(DBObject dbObject:dbObjects){
			resultList.add(new ClassRoomEntry((BasicDBObject)dbObject));
		}
		return resultList;
	}
	/**
	 * 功能教室详情（编辑所用）
	 * @param classRoomId
	 * @return
	 * @author huanxiaolei@ycode.cn
	 */
	public ClassRoomEntry findClassRoomEntry(ObjectId classRoomId){
		DBObject query = new BasicDBObject(Constant.ID,classRoomId);
		DBObject dbo = findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_FUNCTION_CLASSROOM,query,Constant.FIELDS);
	    if(null!=dbo){
	    	return new ClassRoomEntry((BasicDBObject)dbo);
	    }
	    return null;
	}
	/**
	 * 删除一条功能教室
	 * @param id
	 * @author huanxiaolei@ycode.cn
	 */
	public void deleteClassRoomEntry(ObjectId classRoomId){
		DBObject query = new BasicDBObject(Constant.ID, classRoomId);
		DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ir", Constant.ONE));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_FUNCTION_CLASSROOM, query, updateValue);
	
		DBObject querys = new BasicDBObject("cid", classRoomId);
		DBObject updateValues = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ir", Constant.ONE));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM_APPOINTMENT, querys, updateValues);
	}
	/**
	 * 保存编辑功能教室
	 * @param id
	 * @author huanxiaolei@ycode.cn
	 */
	public void updateClassRoomEntry(ObjectId classRoomId,int number,String classRoomName,List<ObjectId> userIds){
		DBObject query = new BasicDBObject(Constant.ID, classRoomId);
		DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("num", number).append("cn", classRoomName).append("aids", userIds));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_FUNCTION_CLASSROOM, query, updateValue);
	
		DBObject querys = new BasicDBObject("cid", classRoomId);
		DBObject updateValues = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("cn", classRoomName));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM_APPOINTMENT, querys, updateValues);
	}
	/**
	 * 查询功能教室管理员
	 * @param schoolId
	 * @author huanxiaolei@ycode.cn
	 */
	public List<UserEntry> findFunClassRoomManage(ObjectId schoolId){
		DBObject query = new BasicDBObject("si",schoolId).append("r",new BasicDBObject(Constant.MONGO_GTE,UserRole.FUNCTION_ROOM_MANAGER.getRole())).append("ir", Constant.ZERO);
		List<DBObject> dbObjects = find(MongoFacroty.getAppDB(),Constant.COLLECTION_USER_NAME,query,Constant.FIELDS);
		List<UserEntry> resultList = new ArrayList<UserEntry>();
		for(DBObject dbObject:dbObjects){
			resultList.add(new UserEntry((BasicDBObject)dbObject));
		}
		return resultList;
	}
	
	/**
	 * 根据管理者id获取功能教室信息
	 * @author zhanghao
	 * @param administratorId
	 * @return
	 */
	public List<ClassRoomEntry> findClassRooms(ObjectId administratorId,ObjectId schoolId,int skip,int size){
		BasicDBObject query = new BasicDBObject();	
		if(administratorId!=null){
			query.append("aids", administratorId);
		}
		if(schoolId!=null){
			query.append("sid", schoolId);
		}
		query.append("ir", Constant.ZERO);
		DBObject orderBy = new BasicDBObject("num",Constant.ASC);
		List<DBObject> dbObjects = find(MongoFacroty.getAppDB(),Constant.COLLECTION_FUNCTION_CLASSROOM,query,Constant.FIELDS,orderBy,skip,size);
		List<ClassRoomEntry> resultList = new ArrayList<ClassRoomEntry>();
		for(DBObject dbObject:dbObjects){
			ClassRoomEntry classRoom = new ClassRoomEntry((BasicDBObject)dbObject);
			resultList.add(classRoom);
		}
		return resultList;
	}
	
	/**
	 * 根据学校id查询所有该学校的功能教室
	 * @param schId
	 * @return 
	 * @author chengwei@ycode.cn
	 */
	public List<ClassRoomEntry> queryRoomsBySchId(ObjectId schId, int skip, int limit){
		//根据条件查处数据
		BasicDBObject query = new BasicDBObject("sid", schId).append("ir", Constant.ZERO);
		BasicDBObject orderBy = new BasicDBObject("num", Constant.ASC);
		List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_FUNCTION_CLASSROOM, query, Constant.FIELDS, orderBy, skip, limit);
		//转换数据类型
		List<ClassRoomEntry> resultList = new ArrayList<ClassRoomEntry>();
		for(DBObject dbObject : dbObjects){
			resultList.add(new ClassRoomEntry((BasicDBObject)dbObject));
		}
		//返回数据
		return resultList;
	}
	
	/**
	 * 根据教室Id查询所有预约记录
	 * @param clsId
	 * @return
	 * @author chengwei@ycode.cn
	 */
	public List<ClassRoomAppointmentEntry> queryApptByClsId(ObjectId clsId, String term ,int skip, int limit){
		//根据条件查处数据
		BasicDBObject query = new BasicDBObject().append("cid", clsId).append("ir", Constant.ZERO).append("term", term);
		BasicDBObject orderBy = new BasicDBObject("st", Constant.DESC);
		List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM_APPOINTMENT, query, Constant.FIELDS, orderBy, skip, limit);
		//转换数据类型
		List<ClassRoomAppointmentEntry> resultList = new ArrayList<ClassRoomAppointmentEntry>();
		for(DBObject dbObject: dbObjects){
			resultList.add(new ClassRoomAppointmentEntry((BasicDBObject)dbObject));
		}
		return resultList;
	}
	
	/**
	 * 添加一条预约记录
	 * @param e
	 * @return
	 * @author chengwei@ycode.cn
	 */
	public List<ClassRoomAppointmentEntry> saveAppointment(ClassRoomAppointmentEntry e){
		List<ClassRoomAppointmentEntry> list = hadAppointment(e.getStartTime(), e.getEndTime(), e.getClassRoomId());
		if(list==null){
			save(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM_APPOINTMENT, e.getBaseEntry());
			return null;
		}else{
			return list;
		}
	}
	
	/**
	 * 判断预约时间是否已被占用
	 * @return
	 * @author chengwei@ycode.cn
	 */
	public List<ClassRoomAppointmentEntry> hadAppointment(Long startTime, Long endTime, ObjectId cid){
		BasicDBObject query = new BasicDBObject();
		BasicDBList values = new BasicDBList();
		// et>startTime & et<endTime
		values.add(new BasicDBObject()
			.append("et", new BasicDBObject(Constant.MONGO_GT, startTime).append(Constant.MONGO_LT, endTime))
			.append("ir", Constant.ZERO)
			.append("cid", cid));
		// st>startTime & st<endTime
		values.add(new BasicDBObject()
			.append("st", new BasicDBObject(Constant.MONGO_GT, startTime).append(Constant.MONGO_LT, endTime))
			.append("ir", Constant.ZERO)
			.append("cid", cid));
		// st<startTime & et>endTime
		values.add(new BasicDBObject()
			.append("st", new BasicDBObject(Constant.MONGO_LTE, startTime))
			.append("et", new BasicDBObject(Constant.MONGO_GTE, endTime))
			.append("ir", Constant.ZERO)
			.append("cid", cid));
		query.put(Constant.MONGO_OR, values);
		List<DBObject> dbObjects = find(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM_APPOINTMENT, query, Constant.FIELDS);
		if(dbObjects.size()==0){
			return null;
		}else{
			//转换数据类型
			List<ClassRoomAppointmentEntry> resultList = new ArrayList<ClassRoomAppointmentEntry>();
			for(DBObject dbObject: dbObjects){
				resultList.add(new ClassRoomAppointmentEntry((BasicDBObject)dbObject));
			}
			return resultList;
		}
	}
	
	/**
	 * 删除一条预约记录
	 * @param id
	 * @author chengwei@ycode.cn
	 */
	public void deleteAppointment(ObjectId id){
		DBObject query = new BasicDBObject(Constant.ID, id);
		DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ir", Constant.ONE));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM_APPOINTMENT, query, updateValue);
	}
	
	/**
	 * 恢復一條刪除的記錄
	 * @param id
	 * @author chengwei@ycode.cn
	 */
	public void recoverAppointment(ObjectId id){
		DBObject query = new BasicDBObject(Constant.ID, id);
		DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject("ir", Constant.ZERO));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM_APPOINTMENT, query, updateValue);
	}
	
	
	/**
	 * 根据id更新一条预约记录
	 * @param id
	 * @param startTime
	 * @param endTime
	 * @param user
	 * @param reasons
	 * @author chengwei@ycode.cn
	 */
	public List<ClassRoomAppointmentEntry> updateAppintment(ObjectId id, Long startTime, Long endTime, String reasons){
		//根据id获取本条记录
		ClassRoomAppointmentEntry e = getAppointment(id);
		ObjectId cid = e.getClassRoomId();
		//刪除本記錄
		deleteAppointment(id);
		//查找是否有已被佔用
		List<ClassRoomAppointmentEntry> list = hadAppointment(startTime, endTime, cid);
		//恢復本記錄
		recoverAppointment(id);
		
		if(list==null){
			DBObject query = new BasicDBObject(Constant.ID, id);
			DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject()
					.append("st", startTime)
					.append("et", endTime)
					.append("rs", reasons));
			update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM_APPOINTMENT, query, updateValue);
			return null;
		}else{
			return list;
		}
			
	}
	
	/**
	 * 根据id更新一条预约记录
	 * @param id
	 * @param startTime
	 * @param endTime
	 * @param user
	 * @param reasons
	 * @author caotiecheng@ycode.cn
	 */
	public void updateAppint(ObjectId id, Long startTime, Long endTime, String reasons){
		//根据id获取本条记录
		ClassRoomAppointmentEntry e = getAppointment(id);
			DBObject query = new BasicDBObject(Constant.ID, id);
			DBObject updateValue = new BasicDBObject(Constant.MONGO_SET, new BasicDBObject()
					.append("st", startTime)
					.append("et", endTime)
					.append("rs", reasons));
			update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM_APPOINTMENT, query, updateValue);
	}
	
	/**
	 * 根据id查询一条预约记录
	 * @param id
	 * @return
	 * @author chengwei@ycode.cn
	 */
	public ClassRoomAppointmentEntry getAppointment(ObjectId id){
		DBObject query = new BasicDBObject(Constant.ID, id).append("ir", Constant.ZERO);
		DBObject object = findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM_APPOINTMENT, query, Constant.FIELDS);
		if(object==null){
			return null;
		}else{
			return new ClassRoomAppointmentEntry((BasicDBObject)object);
		}
	}
	
	/**
	 * 根据教室id查询所有预约信息
	 * @author zhanghao 
	 * @param roomId
	 * @return
	 */
	public List<ClassRoomAppointmentEntry> findRoomDetils(ObjectId roomId,String term ,int skip, int limit){
		BasicDBObject query = new BasicDBObject();
		if(roomId!=null){
			query.append("cid", roomId);
		}
		if(!"".equals(term)){
			query.append("term", term);
		}
		query.append("ir", Constant.ZERO);
		DBObject orderBy = new BasicDBObject("st",Constant.DESC);
		List<DBObject> dbObjects=find(MongoFacroty.getAppDB(),Constant.COLLECTION_CLASSROOM_APPOINTMENT,query,Constant.FIELDS,orderBy,skip,limit);
		List<ClassRoomAppointmentEntry> resultList = new ArrayList<ClassRoomAppointmentEntry>();
		for(DBObject dbObject:dbObjects){
			ClassRoomAppointmentEntry clr=new ClassRoomAppointmentEntry((BasicDBObject)dbObject);
			resultList.add(clr);
		}
		return resultList;
	}
	
	/**
	 * 添加一条预约信息
	 *@author  zhanghao 
	 * @param c
	 * @return
	 */
	public ObjectId addClassRoomAppointmentEntry(ClassRoomAppointmentEntry c){
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM_APPOINTMENT, c.getBaseEntry());
		return c.getID();
	}
	
	/**
	 * 根据ID更新一条预约信息
	 * @author zhanghao
	 * @param id
	 * @param startTime
	 * @param endTime
	 * @param reason
	 */
	public void updateClassRoomAppointmentEntry(ObjectId id,Long startTime,Long endTime,String reason){
		DBObject  query = new BasicDBObject(Constant.ID,id);
		DBObject updateValue=new BasicDBObject(Constant.MONGO_SET,new BasicDBObject().append("st", startTime)
												                .append("et", endTime).append("rs", reason));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM_APPOINTMENT, query, updateValue);
	}
	
	/**
	 * 根据ID删除一条预约信息(逻辑删除)
	 * @author zhanghao
	 * @param id
	 */
	public void deleteReservation (Object id){
		DBObject  query = new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject().append("ir", 1));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM_APPOINTMENT, query, updateValue);
	}
	
	/**
	 * 根据教室ID,使用开始时间,使用结束时间查询预约详情(判断时间是否有冲突)
	 * @param roomId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @author zhanghao@ycode.cn
	 */
	public List<ClassRoomAppointmentEntry> findClassRoomAppointmentEntryByTime(ObjectId roomId,Long startTime,Long endTime){
		BasicDBObject query = new BasicDBObject();
		BasicDBList values = new BasicDBList();
		// et>startTime & et<endTime
				values.add(new BasicDBObject()
					.append("et", new BasicDBObject(Constant.MONGO_GT, startTime).append(Constant.MONGO_LT, endTime))
					.append("ir", Constant.ZERO)
					.append("cid", roomId));
				// st>startTime & st<endTime
				values.add(new BasicDBObject()
					.append("st", new BasicDBObject(Constant.MONGO_GT, startTime).append(Constant.MONGO_LT, endTime))
					.append("ir", Constant.ZERO)
					.append("cid", roomId));
				// st<startTime & et>endTime
				values.add(new BasicDBObject()
					.append("st", new BasicDBObject(Constant.MONGO_LTE, startTime))
					.append("et", new BasicDBObject(Constant.MONGO_GTE, endTime))
					.append("ir", Constant.ZERO)
					.append("cid", roomId));
		
		query.put(Constant.MONGO_OR, values);
		if(roomId!=null){
			query.append("cid", roomId);
		}
		query.append("ir", Constant.ZERO);
		DBObject orderBy = new BasicDBObject("st",Constant.DESC);
		List<DBObject> dbObjects=find(MongoFacroty.getAppDB(),Constant.COLLECTION_CLASSROOM_APPOINTMENT,query,Constant.FIELDS,orderBy);
		if(dbObjects.size()==0){
			return null;
		}
		List<ClassRoomAppointmentEntry> resultList = new ArrayList<ClassRoomAppointmentEntry>();
		for(DBObject dbObject:dbObjects){
			ClassRoomAppointmentEntry clr=new ClassRoomAppointmentEntry((BasicDBObject)dbObject);
			resultList.add(clr);
		}
		return resultList;
	}
	
	/**
	 * 查询学校功能教室总数
	 * @param schid
	 * @return
	 * @author chengwei@ycode.cn
	 */
	public int countRooms(ObjectId schId){
		BasicDBObject query = new BasicDBObject("ir", Constant.ZERO).append("sid", schId);
		int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_FUNCTION_CLASSROOM, query);
		return count;
	}
	
	/**
	 * 根据功能教室id查询预约记录总数
	 * @param schid
	 * @return
	 * @author chengwei@ycode.cn
	 */
	public int countAppointments(ObjectId classId, String term){
		BasicDBObject query = new BasicDBObject("ir", Constant.ZERO).append("cid", classId).append("term", term);
		return count(MongoFacroty.getAppDB(), Constant.COLLECTION_CLASSROOM_APPOINTMENT, query);
	}
	
	/**
	 * 根据学校id,使用者id,学期,查询学校功能教室总数
	 * @param schid
	 * @return
	 * @author zhanghao@ycode.cn
	 */
	public int countManageRooms(ObjectId schId,ObjectId userId){
		BasicDBObject query = new BasicDBObject("ir", Constant.ZERO).append("sid", schId).append("aid", userId);
		int count = count(MongoFacroty.getAppDB(), Constant.COLLECTION_FUNCTION_CLASSROOM, query);
		return count;
	}
	/**
	 * 获得序号
	 * @param ques
	 * @author huanxiaolei@ycode.cn
	 */
	public List<ClassRoomEntry> getNumber(ObjectId schoolId){
		DBObject query = new BasicDBObject("sid",schoolId).append("ir", Constant.ZERO);
		DBObject orderBy = new BasicDBObject("num",Constant.ASC);
		List<DBObject> dbObjects = find(MongoFacroty.getAppDB(),Constant.COLLECTION_FUNCTION_CLASSROOM,query,new BasicDBObject("num",Constant.ONE).append("cn", Constant.ONE),orderBy);
		List<ClassRoomEntry> resultList = new ArrayList<ClassRoomEntry>();
		for(DBObject dbObject:dbObjects){
			resultList.add(new ClassRoomEntry((BasicDBObject)dbObject));
		}
		return resultList;
	}
	/**
	 * 根据教室Id去重查询有的学期
	 * @param classId
	 * @author huanxiaolei@ycode.cn
	 */
	public BasicDBList distinctTerm(ObjectId classId){
		DBObject cmd = new BasicDBObject("distinct",Constant.COLLECTION_CLASSROOM_APPOINTMENT).append("key" , "term")
											.append("query",new BasicDBObject("cid", classId).append("ir", Constant.ZERO));
		return (BasicDBList)MongoFacroty.getAppDB().command(cmd).get("values");
	}
	/**
	 * 根据用户iD统计预约教室总数(全查询)	
	 * @param userId
	 * @return
	 * @author caotiecheng
	 */
	public int countSum(ObjectId userId){
		DBObject query = new BasicDBObject("uid",userId).append("ir", Constant.ZERO);
		return count(MongoFacroty.getAppDB(),Constant.COLLECTION_CLASSROOM_APPOINTMENT,query);
	}
	
	
	/**
	 *  根据用户iD统计预约教室总数(检索)
	 * @param userId
	 * @param searchTmStart
	 * @param searchTmEnd
	 * @return
	 */
	public int countSum1(ObjectId userId,long searchTmStart,long searchTmEnd){
		DBObject query = new BasicDBObject("uid",userId).append("ir", Constant.ZERO);
		BasicDBList values = new BasicDBList();
		// et>endTime & st<startTime
		values.add(new BasicDBObject()
			.append("et", new BasicDBObject(Constant.MONGO_GT, searchTmEnd))
			.append("st", new BasicDBObject(Constant.MONGO_LT, searchTmStart)));
		// et>startTime & st<startTime
		values.add(new BasicDBObject()
			.append("et", new BasicDBObject(Constant.MONGO_GT, searchTmStart))
			.append("st", new BasicDBObject(Constant.MONGO_LT, searchTmStart)));
		// st<startTime & et>endTime
		values.add(new BasicDBObject()
			.append("st", new BasicDBObject(Constant.MONGO_LT, searchTmEnd))
			.append("et", new BasicDBObject(Constant.MONGO_GT, searchTmEnd)));
		// st>startTime & et<endTime
		values.add(new BasicDBObject()
			.append("st", new BasicDBObject(Constant.MONGO_GT, searchTmStart))
			.append("et", new BasicDBObject(Constant.MONGO_LT, searchTmEnd)));
		
		query.put(Constant.MONGO_OR, values);
		
		return count(MongoFacroty.getAppDB(),Constant.COLLECTION_CLASSROOM_APPOINTMENT,query);
	}
	/**
	 * 查询学校所有管理员
	 * @Description:  
	 * @param schoolId
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	public List<UserEntry> findManagers(ObjectId schoolId){
		 BasicDBObject query=new BasicDBObject("si", schoolId)
     	.append("r",  UserRole.ADMIN.getRole())
     	.append("ir", Constant.ZERO);
     List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(),Constant.COLLECTION_USER_NAME,query,Constant.FIELDS);
     List<UserEntry> userEntryList=new ArrayList<UserEntry>();
     for(DBObject dbObject:dbObjectList){
         UserEntry userEntry=new UserEntry((BasicDBObject)dbObject);
         userEntryList.add(userEntry);
     }
     return userEntryList;
	}
	
	/**
	 * 查询学校所有管理员
	 * @Description:  
	 * @param schoolId
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	public List<UserEntry> findAllUsersBySchoolId(ObjectId schoolId){
		 BasicDBObject query=new BasicDBObject("si", schoolId).append("ir", Constant.ZERO).append("r", new BasicDBObject(Constant.MONGO_GTE,UserRole.HEADMASTER.getRole()));
     List<DBObject> dbObjectList=find(MongoFacroty.getAppDB(),Constant.COLLECTION_USER_NAME,query,Constant.FIELDS);
     List<UserEntry> userEntryList=new ArrayList<UserEntry>();
     for(DBObject dbObject:dbObjectList){
         UserEntry userEntry=new UserEntry((BasicDBObject)dbObject);
         userEntryList.add(userEntry);
     }
     return userEntryList;
	}

	/**
	 *
	 * @param userId
	 * @return
	 */
	public int findMyManageRoom(ObjectId userId) {
		BasicDBObject query = new BasicDBObject();
		query.append("aids", userId);
		query.append("ir", Constant.ZERO);
		return count(MongoFacroty.getAppDB(),Constant.COLLECTION_FUNCTION_CLASSROOM,query);
	}
}
