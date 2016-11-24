package com.fulaan.repair.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import com.db.repair.RepairDao;
import com.db.school.SchoolDao;
import com.pojo.repair.RepairEntry;
import com.pojo.school.SchoolEntry;
import com.sys.constants.Constant;

/**
 * 报修service
 * @author cxy
 *
 */
@Service
public class RepairService {
	
	
	private RepairDao repairDao = new RepairDao();
	private SchoolDao schoolDao = new SchoolDao();
	
	
	/**
	 * 添加一条报修信息
	 * @param e
	 * @return
	 */
	public ObjectId addRepairEntry(RepairEntry e)
	{
		repairDao.addRepairEntry(e);
		return e.getID();
	}
	
	/**
	 * 根据Id查询一个特定的报修信息
	 * @param id
	 * @return
	 */
	public RepairEntry getRepairEntry(ObjectId id)
	{
		return repairDao.getRepairEntry(id);
	}
	
	/**
	 * 查询本人所有报修记录
	 * @param repairTermType default("ALL")
	 * @param repairDepartmentId default("ALL")
	 * @param repairProcedure default("ALL")
	 * @return
	 */
	public List<RepairEntry> queryRepairsBydeclaredAndFields(ObjectId declareRepairPersonId,String repairTermType,String repairProcedure){
		
		return repairDao.queryRepairsBydeclaredAndFields(declareRepairPersonId,repairTermType,repairProcedure);
	}
	
	/**
	 * 为管理报修页面查询报修记录
	 * @return
	 */
	public List<RepairEntry> queryRepairsForManagePage(ObjectId repairDepartmentId,String repairTermType,String repairProcedure,ObjectId userId,ObjectId schoolId,Integer isReport,Integer isResolve){
		
		return repairDao.queryRepairsForManagePage(repairDepartmentId, repairTermType, repairProcedure, userId,schoolId,isReport,isResolve);
	}
	
	/**
	 * 根据ID更新一条报修信息
	 * @param id
	 * @param repairDepartmentId
	 * @param repairDepartmentName
	 * @param repairPlace
	 * @param phone
	 * @param repairType
	 * @param repairContent
	 */
	public void updateRepair(ObjectId id,ObjectId repairDepartmentId,String repairDepartmentName,String repairPlace,
			String repairType,String repairContent,String phone,String iamgePath){
		
		repairDao.updateRepair( id, repairDepartmentId, repairDepartmentName, repairPlace, repairType, repairContent,phone,iamgePath);
		
	}
	
	/**
	 * 删除一条
	 * @param id
	 */
	public void deleteRepair(ObjectId id){
		repairDao.deleteRepair(id);
	}
	
	/**
	 * 根据ID更新一条报修信息星级
	 * @param id
	 * @param repairDepartmentId
	 */
	public void updateRepariEvaluation(ObjectId id,int repariEvaluation){

		repairDao.updateRepariEvaluation(id, repariEvaluation);
	}
	
	/**
	 * 将维修任务指派给一个User
	 * @param id
	 * @param repairDepartmentId
	 */
	public void deliverRepairMissionToUser(ObjectId repairId,ObjectId workerId,String workerName){
		
		repairDao.deliverRepairMissionToUser(repairId, workerId, workerName);
	}
	
	/**
	 * 提交维修结果
	 * @param id
	 * @param repairDepartmentId
	 */
	public void commitRepairResult(ObjectId repairId,String resultContent){
		
		repairDao.commitRepairResult(repairId, resultContent);
	}

	/**
	 * 根据学校的id集合查询所有学校的信息(l)
	 */
	public List<SchoolEntry> getSchoolList(List<ObjectId> schoolIds) {
		List<SchoolEntry> list = new ArrayList<SchoolEntry>();
		Map<ObjectId, SchoolEntry> map = schoolDao.getSchoolMap(schoolIds,Constant.FIELDS);
		Set<?> entries = map.entrySet();
		if (entries != null) {
			Iterator<?> it = entries.iterator();
			while (it.hasNext()) {
				Entry<?, ?> entry = (Entry<?, ?>) it.next();
				Object value = entry.getValue();
				list.add((SchoolEntry) value);
			}
		}

		return list;
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
		return repairDao.queryRepairsForEducation(educationId, repairTermType, repairProcedure, schoolId,isReport,isResolve);
	}
	

	/**
	 * 根据ID更新一条上报教育局的报修信息
	 * @param id
	 * @param repairDepartmentName
	 * @param phone
	 * @param reportReason
	 */
	public void updateReport(ObjectId id,ObjectId repairDepartmentId,String repairDepartmentName,String phone,String reportReason,String manager){
		
		repairDao.updateUpReport( id,repairDepartmentId, repairDepartmentName, phone,reportReason,manager);
		
	}
	
	/**
	 * 教育局指派维修人员
	 * @param id
	 * @param solveRepairPersonName
	 */
	 public void updateEduRepair(ObjectId id,String solveRepairPersonName){
		 repairDao.updateEduRepair(id, solveRepairPersonName);
	 }
	 /**
	  * 更新维修结果
	  * @param id
	  * @param repairResult
	  */
	 public void updateEduResult(ObjectId id,String repairResult){
		 repairDao.updateEduResult(id, repairResult);
	 }
}

