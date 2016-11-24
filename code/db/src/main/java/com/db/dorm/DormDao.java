package com.db.dorm;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.dorm.DormAreaEntry;
import com.pojo.dorm.DormBuildingEntry;
import com.pojo.dorm.DormEntry;
import com.pojo.dorm.DormFloorEntry;
import com.pojo.dorm.DormStudentEntry;
import com.sys.constants.Constant;


/**
 * Created by caotiecheng on 2015/12/8.
 */
public class DormDao extends BaseDao {
	 /**
     * 添加学生宿舍
     * @author caotiecheng
     * @param e
     * @return
     */
    public ObjectId addDorm(DormEntry e)
    {
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM, e.getBaseEntry());
        return e.getID();
    }
    
    /**
	 * 删除一条宿舍信息
	 * @author caotiecheng
	 * @param id
	 */
	public void deleteDorm(ObjectId id){
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject().append("ir", 1));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM, query, updateValue);
	}
    
	
	/**
	 * @Description: 根据ID更新一条学生宿舍的宿舍区id 
	 * @param id
	 * @param dormAreaId
	 * @author:lujiang@ycode.cn
	 */
	public void updateDormAreaId(ObjectId id,ObjectId dormAreaId){
		
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,
				new BasicDBObject()
		.append("daid", dormAreaId));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM, query, updateValue);
		
	}
	/**
	 * @Description: 根据ID更新一条学生宿舍的宿舍楼id 
	 * @param id
	 * @param dormBuildId
	 * @author:lujiang@ycode.cn
	 */
	public void updateDormBuildId(ObjectId id,ObjectId dormBuildId){
		
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,
				new BasicDBObject()
		.append("dbid", dormBuildId));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM, query, updateValue);
		
	}
	
    /**
     * 通过宿舍id查询学生
     * @author caotiecheng
     * @param id
     */
    public List<DormStudentEntry> selDormStudentById(ObjectId dormId) {
        BasicDBObject query =new BasicDBObject("did",dormId);
        DBObject orderBy = new BasicDBObject("bd",Constant.ASC);
        List<DBObject> dbObjects= find(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM_STUDENT,query, Constant.FIELDS,orderBy);
        List<DormStudentEntry> dormStudentList=new ArrayList<DormStudentEntry>();
        for(DBObject dbo:dbObjects){
        	DormStudentEntry studentList=new DormStudentEntry((BasicDBObject)dbo);
        	dormStudentList.add(studentList);
		}
		return dormStudentList;
    }
    
    /**
     * 通过宿舍id查询宿舍床位
     * @author caotiecheng
     * @param id
     */
    public DormEntry selDormEntryById(ObjectId dormId) {
        BasicDBObject query =new BasicDBObject(Constant.ID,dormId);
        DBObject dbo= findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM, query, Constant.FIELDS);
        if(null!=dbo){
        	return new DormEntry((BasicDBObject)dbo);
		}
		return null;
    }
    
    /**
     * 查询入住的全部学生
     * @author 张浩
     * @param dormId
     * @param grade
     * @param dlass
     * @param sex
     * @param studentName
     * @param studentId
     * @return
     */
    public List<DormStudentEntry> findAllStudent(ObjectId schoolId,ObjectId gradeId,ObjectId dlassId,
    		                   String sex, String name,String studentNum,int pageNo,int pageSize){
    	BasicDBObject query =new BasicDBObject();
    	Pattern pattern = Pattern
				.compile(name, Pattern.CASE_INSENSITIVE);
    	Pattern pattern1 = Pattern
				.compile(studentNum, Pattern.CASE_INSENSITIVE);
    	if(schoolId!=null){
    		query.append("sid", schoolId);
    	}
    	if(gradeId!=null){
    		query.append("gid", gradeId);
    	}
    	if(dlassId!=null){
    		query.append("cid",dlassId);
    	}
    	if(!"ALL".equals(sex)){
    		query.append("sex", sex);
    	}
    	if(!"".equals(name)){
    		query.append("stnm", pattern);
    	}
    	if(!"".equals(studentNum)){
    		query.append("stn",  pattern1);
    	}
    	
    	DBObject orderBy = new BasicDBObject("ct",Constant.ASC); 
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(),Constant.COLLECTION_DORM_STUDENT,query,
        		                        Constant.FIELDS,orderBy,pageNo,pageSize);
        List<DormStudentEntry> studentList=new ArrayList<DormStudentEntry>();
    	for(DBObject dbo : dbObjects){
    		DormStudentEntry dormStudentEntry = new DormStudentEntry((BasicDBObject)dbo);
    		studentList.add(dormStudentEntry);
    	}
    	return studentList;
    }

    /**
     * 查询入住的全部学生数量
     * @author 张浩
     * @param dormId
     * @param grade
     * @param dlass
     * @param sex
     * @param studentName
     * @param studentId
     * @return
     */
    public int findAllStudentCount(ObjectId schoolId,ObjectId gradeId,ObjectId dlassId,
    		                       String sex, String name,String studentNum){
    	BasicDBObject query =new BasicDBObject();
    	Pattern pattern = Pattern
				.compile(name, Pattern.CASE_INSENSITIVE);
    	Pattern pattern1 = Pattern
				.compile(studentNum, Pattern.CASE_INSENSITIVE);
    	if(schoolId!=null){
    		query.append("sid", schoolId);
    	}
    	if(gradeId!=null){
    		query.append("gid", gradeId);
    	}
    	if(dlassId!=null){
    		query.append("cid",dlassId);
    	}
    	if(!"ALL".equals(sex)){
    		query.append("sex", sex);
    	}
    	if(!"".equals(name)){
		query.append("stnm", pattern);
    	}
    	if(!"".equals(studentNum)){
    		query.append("stn",pattern1);
    	}
    	return count(MongoFacroty.getAppDB(),Constant.COLLECTION_DORM_STUDENT,query);
    }
	/**
	 * 添加一个宿舍区
	 * @author zhanghao@ycode.cn
	 * @param d
	 * @return
	 */
	public ObjectId addDormAreaEntry(DormAreaEntry d){
		save(MongoFacroty.getAppDB(),Constant.COLLECTION_DORM_AREA,d.getBaseEntry());
		return d.getID();
	}
	
	/**
	 * 添加一个宿舍楼
	 * @author zhanghao@ycode.cn
	 * @param b
	 * @return
	 */
	public ObjectId addDormBuildingEntry(DormBuildingEntry b){
		save(MongoFacroty.getAppDB(),Constant.COLLECTION_DORM_BUILDING,b.getBaseEntry());
		return b.getID();
	}
	
	/**
	 * 添加一个宿舍层
	 * @author zhanghao@ycode.cn
	 * @param f
	 * @return
	 */
	public ObjectId addDormFloorEntry(DormFloorEntry f){
		save(MongoFacroty.getAppDB(),Constant.COLLECTION_DORM_FLOOR,f.getBaseEntry());
		return f.getID();
	}
	
	/**
	 * 删除一个宿舍区（逻辑删除）
	 * @author zhanghao@ycode.cn
	 * @param id
	 */
	public void deleteDormAreaEntry(ObjectId id){
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject().append("ir", 1));
		update(MongoFacroty.getAppDB(),Constant.COLLECTION_DORM_AREA,query,updateValue);
	}
	
	/**
	 * 删除一个宿舍楼（逻辑删除）
	 * @author zhanghao@ycode.cn
	 * @param id
	 */
	public void deleteDormBuildingEntry(ObjectId id){
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject().append("ir", 1));
		update(MongoFacroty.getAppDB(),Constant.COLLECTION_DORM_BUILDING,query,updateValue);
	}
	
	/**
	 * 删除一个宿舍层（逻辑删除）
	 * @author zhanghao@ycode.cn
	 * @param id
	 */
	public void deleteDormFloorEntry(ObjectId id){
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject().append("ir", 1));
		update(MongoFacroty.getAppDB(),Constant.COLLECTION_DORM_FLOOR,query,updateValue);
	}
	
	/**
	 * 根据ID跟新一个宿舍区
	 * @author zhanghao@ycode.cn
	 * @param id
	 * @param dormAreaName
	 */
	public void updateDormAreaEntry(ObjectId id,String dormAreaName){
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,
				new BasicDBObject().append("dnm", dormAreaName));
		update(MongoFacroty.getAppDB(),Constant.COLLECTION_DORM_AREA,query,updateValue);
	}
	
	/**
	 * 根据ID跟新一个宿舍楼
	 * @author zhanghao@ycode.cn
	 * @param id
	 * @param dormBuildingName
	 */
	public void updateDormBuildingEntry(ObjectId id,String dormBuildingName,ObjectId dormAreaId){
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,
				new BasicDBObject().append("bnm", dormBuildingName).append("aid", dormAreaId));
		update(MongoFacroty.getAppDB(),Constant.COLLECTION_DORM_BUILDING,query,updateValue);
	}
	
	/**
	 * 根据ID跟新一个宿舍层
	 * @author zhanghao@ycode.cn
	 * @param id
	 * @param dormFloorName
	 */
	public void updateDormFloorEntry(ObjectId id,String dormFloorName,ObjectId dormBuildingId){
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,
				new BasicDBObject().append("fnm", dormFloorName).append("bid", dormBuildingId));
		update(MongoFacroty.getAppDB(),Constant.COLLECTION_DORM_FLOOR,query,updateValue);
	}
	
	/**
	 * @Description:  根据id查询宿舍区
	 * @param id
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	public DormAreaEntry findDormAreaById(ObjectId id){
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM_AREA, query, Constant.FIELDS);
		if(null!=dbo)
		{
			return new DormAreaEntry((BasicDBObject)dbo);
		}
		return null;
	}
	
	/**
	 * @Description:  根据id查询宿舍楼
	 * @param id
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	public DormBuildingEntry findDormBuildingById(ObjectId id){
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM_BUILDING, query, Constant.FIELDS);
		if(null!=dbo)
		{
			return new DormBuildingEntry((BasicDBObject)dbo);
		}
		return null;
	}
	
	/**
	 * @Description:  根据id查询宿舍楼层
	 * @param id
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	public DormFloorEntry findDormFloorById(ObjectId id){
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM_FLOOR, query, Constant.FIELDS);
		if(null!=dbo)
		{
			return new DormFloorEntry((BasicDBObject)dbo);
		}
		return null;
	}
	
	/**
	 * @Description:  根据宿舍区id查询所有宿舍楼
	 * @param dormAreaId
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	public List<DormBuildingEntry> findByDormAreaId(ObjectId dormAreaId){
		DBObject query =new BasicDBObject().append("aid", dormAreaId).append("ir", Constant.ZERO);
		DBObject orderBy = new BasicDBObject("ct",Constant.ASC); 
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(),Constant.COLLECTION_DORM_BUILDING,query,Constant.FIELDS,orderBy);
        List<DormBuildingEntry> resultList = new ArrayList<DormBuildingEntry>();
        for(DBObject dbObject:dbObjects){
        	DormBuildingEntry dormBuildingEntry = new DormBuildingEntry((BasicDBObject)dbObject);
        	resultList.add(dormBuildingEntry);
        }
		return resultList;
	}
	
	/**
	 * @Description:  根据宿舍楼id查询所有宿舍层
	 * @param dormBuildId
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	public List<DormFloorEntry> findByDormBuildId(ObjectId dormBuildId){
		DBObject query =new BasicDBObject("bid",dormBuildId).append("ir", Constant.ZERO);
		DBObject orderBy = new BasicDBObject("ct",Constant.ASC); 
		List<DBObject> dbObjects = find(MongoFacroty.getAppDB(),Constant.COLLECTION_DORM_FLOOR,query,Constant.FIELDS,orderBy);
		List<DormFloorEntry> resultList = new ArrayList<DormFloorEntry>();
		for(DBObject dbObject:dbObjects){
			DormFloorEntry dormFloorEntry = new DormFloorEntry((BasicDBObject)dbObject);
			resultList.add(dormFloorEntry);
		}
		return resultList;
	}
	
	/**
	 * @Description:  根据学校id查询所有宿舍区
	 * @param schoolId
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	public List<DormAreaEntry> findBySchoolId(ObjectId schoolId){
		DBObject query =new BasicDBObject("scid",schoolId).append("ir", Constant.ZERO);
		DBObject orderBy = new BasicDBObject("ct",Constant.ASC); 
		List<DBObject> dbObjects = find(MongoFacroty.getAppDB(),Constant.COLLECTION_DORM_AREA,query,Constant.FIELDS,orderBy);
		List<DormAreaEntry> resultList = new ArrayList<DormAreaEntry>();
		for(DBObject dbObject:dbObjects){
			DormAreaEntry dormAreaEntry = new DormAreaEntry((BasicDBObject)dbObject);
			resultList.add(dormAreaEntry);
		}
		return resultList;
	}
	
    
    /**
     * 查询全部学生宿舍
     * @author caotiecheng
     */
    public List<DormEntry> findAllDorm(ObjectId schoolId,int skip,int size) {
        List<DormEntry> retList =new ArrayList<DormEntry>();
        DBObject query = new BasicDBObject("scid",schoolId).append("ir", Constant.ZERO);
        DBObject orderBy = new BasicDBObject("ct",Constant.DESC);
        List<DBObject> list= find(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM, query, Constant.FIELDS,orderBy,skip,size);
        if(null!=list && !list.isEmpty())
        {
            for(DBObject dbo:list)
            {
                retList.add(new DormEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }
    
    /**
     * 查询全部学生宿舍
     * @author caotiecheng
     */
    public List<DormEntry> findAllDormByQueryId(String columName,ObjectId queryId,ObjectId schoolId,int skip,int size) {
        List<DormEntry> retList =new ArrayList<DormEntry>();
        DBObject query = new BasicDBObject("scid",schoolId).append(columName, queryId).append("ir", Constant.ZERO);
        DBObject orderBy = new BasicDBObject("ct",Constant.DESC);
        List<DBObject> list= find(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM, query, Constant.FIELDS,orderBy,skip,size);
        if(null!=list && !list.isEmpty())
        {
            for(DBObject dbo:list)
            {
                retList.add(new DormEntry((BasicDBObject)dbo));
            }
        }
        return retList;
    }
    
    /**
     * 宿舍添加学生
     * @author huanxiaolei@ycode.cn
     * @param dormId
     * @param dormStudent
     */
    public ObjectId addStudentToDorm(DormStudentEntry e){
        save(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM_STUDENT, e.getBaseEntry());
        return e.getID();
    }
    
    /**
     * 宿舍删除学生
     * @author huanxiaolei@ycode.cn
     * @param id
     */
    public void delStudentForDorm(ObjectId id){
    	DBObject query = new BasicDBObject(Constant.ID, id);
    	remove(MongoFacroty.getAppDB(),Constant.COLLECTION_DORM_STUDENT,query);
    }
    
    /**
     * @Description:根据宿舍楼层id查询所有宿舍  
     * @param dormFloorId
     * @return
     * @author:lujiang@ycode.cn
     */
	public List<DormEntry> findByDormFloorId(ObjectId dormFloorId){
		DBObject query =new BasicDBObject("dfid",dormFloorId).append("ir", Constant.ZERO);
		DBObject orderBy = new BasicDBObject("ct",Constant.ASC); 
		List<DBObject> dbObjects = find(MongoFacroty.getAppDB(),Constant.COLLECTION_DORM,query,Constant.FIELDS,orderBy);
		List<DormEntry> resultList = new ArrayList<DormEntry>();
		for(DBObject dbObject:dbObjects){
			DormEntry dormEntry = new DormEntry((BasicDBObject)dbObject);
			resultList.add(dormEntry);
		}
		return resultList;
	}
	
	 /**
     * 获取宿舍总数
     * @author:huanxiaolei@ycode.cn
     * @param schoolId
     * @return
     */
    public int countDormEntry(ObjectId schoolId) {
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM, new BasicDBObject("scid", schoolId).append("ir", Constant.ZERO));
    }
    
    /**
     * 获取宿舍总数有条件查询
     * @author:huanxiaolei@ycode.cn
     * @param schoolId
     * @return
     */
    public int countDormEntryByQuery(ObjectId schoolId,String columName,ObjectId queryId) {
        return count(MongoFacroty.getAppDB(), Constant.COLLECTION_DORM, new BasicDBObject("scid", schoolId).append(columName, queryId).append("ir", Constant.ZERO));
    }
    
	/**
	 * @Description:根据宿舍楼id查询所有宿舍  
	 * @param dormBuildId
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	public List<DormEntry> findByBuildId(ObjectId dormBuildId){
		DBObject query =new BasicDBObject().append("dbid", dormBuildId).append("ir", Constant.ZERO);
		DBObject orderBy = new BasicDBObject("ct",Constant.ASC); 
		List<DBObject> dbObjects = find(MongoFacroty.getAppDB(),Constant.COLLECTION_DORM,query,Constant.FIELDS,orderBy);
		List<DormEntry> resultList = new ArrayList<DormEntry>();
		for(DBObject dbObject:dbObjects){
			DormEntry dormEntry = new DormEntry((BasicDBObject)dbObject);
			resultList.add(dormEntry);
		}
		return resultList;
	}
	/**
	 * @Description:根据宿舍区id查询所有宿舍  
	 * @param dormBuildId
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	public List<DormEntry> findByAreaId(ObjectId dormAreaId){
		DBObject query =new BasicDBObject().append("daid", dormAreaId).append("ir", Constant.ZERO);
		DBObject orderBy = new BasicDBObject("ct",Constant.ASC); 
		List<DBObject> dbObjects = find(MongoFacroty.getAppDB(),Constant.COLLECTION_DORM,query,Constant.FIELDS,orderBy);
		List<DormEntry> resultList = new ArrayList<DormEntry>();
		for(DBObject dbObject:dbObjects){
			DormEntry dormEntry = new DormEntry((BasicDBObject)dbObject);
			resultList.add(dormEntry);
		}
		return resultList;
	}
	/**
	 * 根据学生id查询是否已有宿舍
	 * @author:huanxiaolei@ycode.cn
	 * @return
	 */
	public DormStudentEntry findByStudentId(ObjectId studentId){
		DBObject query =new BasicDBObject("stid", studentId);
		DBObject dbo= findOne(MongoFacroty.getAppDB(),Constant.COLLECTION_DORM_STUDENT,query,Constant.FIELDS);
		if(null!=dbo){
			return new DormStudentEntry((BasicDBObject)dbo);
		}
		return null;
	}
	/**
	 * 根据班级id查询已有宿舍的学生
	 * @author:huanxiaolei@ycode.cn
	 * @param classId
	 * @return
	 */
	public List<DormStudentEntry> findByClassId(ObjectId classId){
		DBObject query = new BasicDBObject("cid",classId); 
		List<DBObject> dbObjects=find(MongoFacroty.getAppDB(),Constant.COLLECTION_DORM_STUDENT,query,Constant.FIELDS);
		List<DormStudentEntry> dormStudentList = new ArrayList<DormStudentEntry>();
		for(DBObject dbo:dbObjects){
			DormStudentEntry dormStudentEntry = new DormStudentEntry((BasicDBObject)dbo);
			dormStudentList.add(dormStudentEntry);
		}
		return dormStudentList;
	}
	/**
     * 宿舍列表删除宿舍同时删除本宿舍学生
     * @author huanxiaolei@ycode.cn
     * @param id
     */
    public void delDormStudent(ObjectId id){
    	DBObject query = new BasicDBObject("did", id);
    	remove(MongoFacroty.getAppDB(),Constant.COLLECTION_DORM_STUDENT,query);
    }
}
