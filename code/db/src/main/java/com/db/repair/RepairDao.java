package com.db.repair;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.db.base.BaseDao;
import com.db.factory.MongoFacroty;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.repair.RepairEntry;
import com.pojo.reward.RewardEntry;
import com.sys.constants.Constant;

/**
 * 报修Dao
 * @author cxy
 *
 */
public class RepairDao extends BaseDao {
	/**
	 * 添加一条报修信息
	 * @param e
	 * @return
	 */
	public ObjectId addRepairEntry(RepairEntry e)
	{
		save(MongoFacroty.getAppDB(), Constant.COLLECTION_REPAIR, e.getBaseEntry());
		return e.getID();
	}
	
	/**
	 * 根据Id查询一个特定的报修信息
	 * @param id
	 * @return
	 */
	public RepairEntry getRepairEntry(ObjectId id)
	{
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject dbo =findOne(MongoFacroty.getAppDB(), Constant.COLLECTION_REPAIR, query, Constant.FIELDS);
		if(null!=dbo)
		{
			return new RepairEntry((BasicDBObject)dbo);
		}
		return null;
	}
	
	/**
	 * 删除一条
	 * @param id
	 */
	public void deleteRepair(ObjectId id){
		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,new BasicDBObject().append("ir", 1));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_REPAIR, query, updateValue);
	}
	
	/**
	 * 查询本人所有报修记录
	 * @return
	 */
	public List<RepairEntry> queryRepairsBydeclaredAndFields(ObjectId declareRepairPersonId,String repairTermType,String repairProcedure){
		BasicDBObject query = new BasicDBObject();
		if(!("ALL".equals(repairTermType))){
			query.append("rtt", repairTermType);
		}
		
		if(!("ALL".equals(repairProcedure))){
			query.append("rpr",repairProcedure);
		}
		query.append("rdpid", declareRepairPersonId);
		query.append("ir", Constant.ZERO);
		DBObject orderBy = new BasicDBObject("rd",Constant.DESC); 
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(),Constant.COLLECTION_REPAIR,query,Constant.FIELDS,orderBy);
        List<RepairEntry> resultList = new ArrayList<RepairEntry>();
        for(DBObject dbObject:dbObjects){
        	RepairEntry repairEntry = new RepairEntry((BasicDBObject)dbObject);
        	resultList.add(repairEntry);
        }
		return resultList;
	}
	
	
	
	/**
	 * 为管理报修页面查询报修记录
	 * @return
	 */
	public List<RepairEntry> queryRepairsForManagePage(ObjectId repairDepartmentId,String repairTermType,String repairProcedure,ObjectId userId,ObjectId schoolId,Integer isReport,Integer isResolve){
		BasicDBObject query = new BasicDBObject();
		if(!("ALL".equals(repairTermType))){
			query.append("rtt", repairTermType);
		}
		if(!("ALL".equals(repairProcedure))){
			query.append("rpr",repairProcedure);
		}
		if(repairDepartmentId != null){
			query.append("rdid",repairDepartmentId);
		}
		if(userId != null){
			query.append("rspid",userId);
		}
		if(schoolId !=null){
			query.append("scid",schoolId);
		}
		if(isReport !=null){
			query.append("ispt",isReport);
		}
		if(isResolve !=null){
			query.append("isr",isResolve);
		}
		query.append("ir", Constant.ZERO);
		DBObject orderBy = new BasicDBObject("rd",Constant.DESC); 
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(),Constant.COLLECTION_REPAIR,query,Constant.FIELDS,orderBy);
        List<RepairEntry> resultList = new ArrayList<RepairEntry>();
        for(DBObject dbObject:dbObjects){
        	RepairEntry repairEntry = new RepairEntry((BasicDBObject)dbObject);
        	resultList.add(repairEntry);
        }
		return resultList;
	}
	
	/**
	 * 根据ID更新一条报修信息
	 * @param id
	 * @param repairDepartmentId
	 * @param repairPlace
	 * @param phone
	 * @param repairType
	 * @param repairContent
	 */
	public void updateRepair(ObjectId id,ObjectId repairDepartmentId, String repairDepartmentName,
						String repairPlace,String repairType,String repairContent,String phone,String imagePath){

		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,
													new BasicDBObject()
													.append("rdid", repairDepartmentId)
													.append("rdna", repairDepartmentName)
													.append("rpl", repairPlace)
													.append("rt", repairType)
													.append("rc", repairContent)
													.append("ph", phone)
													.append("ipath", imagePath)
													
				);
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_REPAIR, query, updateValue);
		
	}
	
	/**
	 * 根据ID更新一条报修信息星级
	 * @param id
	 * @param repairDepartmentId
	 */
	public void updateRepariEvaluation(ObjectId id,int repariEvaluation){

		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,
													new BasicDBObject()
													.append("rev", repariEvaluation));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_REPAIR, query, updateValue);
		
	}
	
	/**
	 * 将维修任务指派给一个User
	 * @param id
	 * @param repairDepartmentId
	 */
	public void deliverRepairMissionToUser(ObjectId repairId,ObjectId workerId,String workerName){

		DBObject query =new BasicDBObject(Constant.ID,repairId);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,
													new BasicDBObject()
													.append("rspid", workerId)
													.append("rspna", workerName)
													.append("rpr", "已受理"));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_REPAIR, query, updateValue);
		
	}
	
	/**
	 * 提交维修结果
	 * @param id
	 * @param repairDepartmentId
	 */
	public void commitRepairResult(ObjectId repairId,String resultContent){
		DBObject query =new BasicDBObject(Constant.ID,repairId);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,
													new BasicDBObject()
													.append("rre", resultContent)
													.append("rpr", "已完毕"));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_REPAIR, query, updateValue);
		
	}
	
	/**
	 * 教育局中的报修列表
	 * @param educationId
	 * @param repairTermType
	 * @param repairProcedure
	 * @param schoolId
	 * @return
	 */
	public List<RepairEntry> queryRepairsForEducation(ObjectId educationId,String repairTermType,String repairProcedure,ObjectId schoolId,Integer isReport,Integer isResolve){
		BasicDBObject query = new BasicDBObject().append("eid", educationId);
		if(!("ALL".equals(repairTermType))){
			query.append("rtt", repairTermType);
		}
		if(!("ALL".equals(repairProcedure))){
			query.append("rpr",repairProcedure);
		}
		if(schoolId != null){
			query.append("scid",schoolId);
		}
		if(isResolve != null){
			query.append("isr",isResolve);
		}
		
		query.append("ispt", isReport);
		query.append("ir", Constant.ZERO);
		query.append("rdna", "教育局");
		DBObject orderBy = new BasicDBObject("rd",Constant.DESC); 
        List<DBObject> dbObjects = find(MongoFacroty.getAppDB(),Constant.COLLECTION_REPAIR,query,Constant.FIELDS,orderBy);
        List<RepairEntry> resultList = new ArrayList<RepairEntry>();
        for(DBObject dbObject:dbObjects){
        	RepairEntry repairEntry = new RepairEntry((BasicDBObject)dbObject);
        	resultList.add(repairEntry);
        }
		return resultList;
	}
	
 
	/**
	 * 根据ID更新一条上报教育局的报修信息
	 * @param id
	 * @param repairDepartmentName
	 * @param phone
	 * @param reportReason
	 */
	public void updateUpReport(ObjectId id, ObjectId repairDepartmentId, String repairDepartmentName,String phone,String reportReason,String manager){

		DBObject query =new BasicDBObject(Constant.ID,id);
		DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,
													new BasicDBObject()
													.append("rdid", repairDepartmentId)
													.append("rdna", repairDepartmentName)
													.append("rr", reportReason)
													.append("ph", phone)
													.append("rnm", manager)
													.append("ispt", 1)
													.append("rpr", "已受理"));
		update(MongoFacroty.getAppDB(), Constant.COLLECTION_REPAIR, query, updateValue);
		
	}
	/**
	 * 教育局指派维修人员
	 * @param id
	 * @param solveRepairPersonName
	 */
	 public void updateEduRepair(ObjectId id,String solveRepairPersonName){
		 DBObject query =new BasicDBObject(Constant.ID,id);
			DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,
														new BasicDBObject()
														.append("rspna", solveRepairPersonName)
														.append("isr", 1));
			update(MongoFacroty.getAppDB(), Constant.COLLECTION_REPAIR, query, updateValue);
	 }
	 /**
	  * 更新维修结果
	  * @param id
	  * @param repairResult
	  */
	 public void updateEduResult(ObjectId id,String repairResult){
		 DBObject query =new BasicDBObject(Constant.ID,id);
		 DBObject updateValue =new BasicDBObject(Constant.MONGO_SET,
				 new BasicDBObject()
		 .append("rre", repairResult)
		 .append("rpr", "已完毕"));
		 update(MongoFacroty.getAppDB(), Constant.COLLECTION_REPAIR, query, updateValue);
	 }
	
}
