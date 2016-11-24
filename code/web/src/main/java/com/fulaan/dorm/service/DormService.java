package com.fulaan.dorm.service;

import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.dorm.DormDao;
import com.db.school.ClassDao;
import com.db.school.DepartmentDao;
import com.db.school.SchoolDao;
import com.db.user.UserDao;
import com.pojo.dorm.DormAreaEntry;
import com.pojo.dorm.DormBuildingEntry;
import com.pojo.dorm.DormEntry;
import com.pojo.dorm.DormFloorEntry;
import com.pojo.dorm.DormStudentEntry;
import com.pojo.school.ClassEntry;
import com.pojo.school.DepartmentEntry;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;
import com.pojo.school.Grade;
import com.sys.constants.Constant;

@Service
public class DormService {
	private DormDao dormDao = new DormDao();
	private ClassDao classDao=new ClassDao();
	private SchoolDao schoolDao=new SchoolDao();
	private UserDao userDao=new UserDao();
	private DepartmentDao departmentDao=new DepartmentDao() ;
	/**
	 * 添加一条宿舍信息
	 * @author huanxiaolei@ycode.cn
	 * @param e
	 * @return
	 */
	public ObjectId addDormEntry(DormEntry e){
		return dormDao.addDorm(e);
	}
	
	/**
	 * 根据学校id查询宿舍区
	 * @author huanxiaolei@ycode.cn
	 * @param schoolId
	 * @return
	 */
	public List<DormAreaEntry> findDormAreaEntry(ObjectId schoolId){
		return dormDao.findBySchoolId(schoolId);
	}
	
	/**
	 * 根据宿舍区id查询宿舍
	 * @author huanxiaolei@ycode.cn
	 * @param schoolId
	 * @return
	 */
	public List<DormEntry> findDorms(ObjectId schoolId,int skip,int size){
		return dormDao.findAllDorm(schoolId,skip,size);
	}
	
	/**
	 * 根据宿舍区id查询宿舍
	 * @author huanxiaolei@ycode.cn
	 * @param schoolId
	 * @return
	 */
	public List<DormEntry> findDormsByQuery(String columName,ObjectId queryId, ObjectId schoolId,int skip,int size){
		return dormDao.findAllDormByQueryId(columName, queryId, schoolId, skip,size);
	}
	
	/**
	 * 根据宿舍id查询宿舍学生信息
	 * @author huanxiaolei@ycode.cn
	 * @param schoolId
	 * @return
	 */
	public List<DormStudentEntry> findDormStudentEntry(ObjectId dormId){
		return dormDao.selDormStudentById(dormId);
	}
	
	/**
	 * 根据宿舍id查询宿舍床位
	 * @author huanxiaolei@ycode.cn
	 * @param schoolId
	 * @return
	 */
	public DormEntry findDormEntry(ObjectId dormId){
		return dormDao.selDormEntryById(dormId);
	}
	
	/**
	 * @Description:添加一个宿舍区  
	 * @param dormAreaEntry
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	public ObjectId addDormArea(DormAreaEntry dormAreaEntry){
		dormDao.addDormAreaEntry(dormAreaEntry);
		return dormAreaEntry.getID();
	}
	
	/**
	 * @Description: 添加一个宿舍楼 
	 * @param dormBuildingEntry
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	public ObjectId addDormBuilding(DormBuildingEntry dormBuildingEntry){
		dormDao.addDormBuildingEntry(dormBuildingEntry);
		return dormBuildingEntry.getID();
	}
	
	/**
	 * @Description: 添加一个宿舍层
	 * @param dormFloorEntry
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	public ObjectId addDormFloor(DormFloorEntry dormFloorEntry){
		dormDao.addDormFloorEntry(dormFloorEntry);
		return dormFloorEntry.getID();
	}
	
	/**
	 * @Description: 删除一个宿舍区（逻辑删除） 
	 * @param id
	 * @author:lujiang@ycode.cn
	 */
	public void deleteDormAreaById(ObjectId id){
		List<DormEntry> dormlist = dormDao.findByAreaId(id);
		for(DormEntry dormEntry : dormlist){
			List<DormStudentEntry> stuList = dormDao.selDormStudentById(dormEntry.getID());
			for(DormStudentEntry dormStudent :stuList){
				dormDao.delStudentForDorm(dormStudent.getID());
			}
			dormDao.deleteDorm(dormEntry.getID());
		}
		List<DormBuildingEntry> buidList = dormDao.findByDormAreaId(id);
		for(DormBuildingEntry dormBuild : buidList){
			List<DormFloorEntry> floorList = dormDao.findByDormBuildId(dormBuild.getID());
			for(DormFloorEntry dormFloor : floorList){
				dormDao.deleteDormFloorEntry(dormFloor.getID());
			}
			dormDao.deleteDormBuildingEntry(dormBuild.getID());
		}
		dormDao.deleteDormAreaEntry(id);
	}
	
	/**
	 * @Description: 删除一个宿舍楼（逻辑删除） 
	 * @param id
	 * @author:lujiang@ycode.cn
	 */
	public void deleteDormBuildingById(ObjectId id){
		List<DormEntry> dormList = dormDao.findByBuildId(id);
		for(DormEntry dorm : dormList){
			List<DormStudentEntry> stuList = dormDao.selDormStudentById(dorm.getID());
			for(DormStudentEntry dormStudent :stuList){
				dormDao.delStudentForDorm(dormStudent.getID());
			}
			dormDao.deleteDorm(dorm.getID());
		}
		List<DormFloorEntry> floorList = dormDao.findByDormBuildId(id);
		for(DormFloorEntry dormFloor : floorList){
			dormDao.deleteDormFloorEntry(dormFloor.getID());
		}
		dormDao.deleteDormBuildingEntry(id);
	}
	
	/**
	 * @Description:删除一个宿舍层（逻辑删除）  
	 * @param id
	 * @author:lujiang@ycode.cn
	 */
	public void deleteDormFloorById(ObjectId id){
		List<DormEntry> dormList = dormDao.findByDormFloorId(id);
		for(DormEntry dorm : dormList){
			List<DormStudentEntry> stuList = dormDao.selDormStudentById(dorm.getID());
			for(DormStudentEntry dormStudent :stuList){
				dormDao.delStudentForDorm(dormStudent.getID());
			}
			dormDao.deleteDorm(dorm.getID());
		}
		dormDao.deleteDormFloorEntry(id);
	}
	
	/**
	 * @Description: 根据ID更新一个宿舍区 
	 * @param id
	 * @param dormAreaName
	 * @author:lujiang@ycode.cn
	 */
	public void updateDormArea(ObjectId id,String dormAreaName){
		dormDao.updateDormAreaEntry(id, dormAreaName);
	}
	
	/**
	 * @Description: 根据ID跟新一个宿舍楼 
	 * @param id
	 * @param dormBuildingName
	 * @param dormAreaId
	 * @author:lujiang@ycode.cn
	 */
	public void updateDormBuildingEntry(ObjectId id,String dormBuildingName,ObjectId dormAreaId){
		dormDao.updateDormBuildingEntry(id, dormBuildingName, dormAreaId);
	}
	
	/**
	 * @Description:根据ID跟新一个宿舍层  
	 * @param id
	 * @param dormFloorName
	 * @param dormBuildingId
	 * @author:lujiang@ycode.cn
	 */
	public void updateDormFloor(ObjectId id,String dormFloorName,ObjectId dormBuildingId){
		dormDao.updateDormFloorEntry(id, dormFloorName, dormBuildingId);
	}
	
	/**
	 * @Description:  根据id查询宿舍区
	 * @param id
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	public DormAreaEntry findDormAreaById(ObjectId id){
		DormAreaEntry dormAreaEntry = dormDao.findDormAreaById(id);
		return dormAreaEntry;
	}
	
	/**
	 * @Description:  根据id查询宿舍楼
	 * @param id
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	public DormBuildingEntry findDormBuildingById(ObjectId id){
		DormBuildingEntry dormBuildingEntry = dormDao.findDormBuildingById(id);
		return dormBuildingEntry;
	}
	
	/**
	 * @Description:  根据id查询宿舍楼层
	 * @param id
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	public DormFloorEntry findDormFloorById(ObjectId id){
		DormFloorEntry dormFloorEntry = dormDao.findDormFloorById(id);
		return dormFloorEntry;
	}
	
	/**
	 * @Description:  根据宿舍区id查询所有宿舍楼
	 * @param dormAreaId
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	public List<DormBuildingEntry> findByDormAreaId(ObjectId dormAreaId){
		List<DormBuildingEntry> list = dormDao.findByDormAreaId(dormAreaId);
		return list;
	}
	
	/**
	 * @Description:  根据宿舍楼id查询所有宿舍层
	 * @param dormBuildId
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	public List<DormFloorEntry> findByDormBuildId(ObjectId dormBuildId){
		List<DormFloorEntry> list = dormDao.findByDormBuildId(dormBuildId);
		return list;
	}
	
	/**
	 * 添加一条宿舍学生
	 * @author huanxiaolei@ycode.cn
	 * @param e
	 * @return
	 */
	public void addDormStudent(DormStudentEntry e){
		dormDao.addStudentToDorm(e);
	}
	
	/**
	 * 查询入住的全部学生
	 * @author zhanghao
	 * @param gradeId
	 * @param classId
	 * @param sex
	 * @param name
	 * @param studentId
	 * @return
	 */
	public List<DormStudentEntry>  findDormStudent(ObjectId schoolId,ObjectId gradeId,ObjectId dlassId,String sex, String name,String studentId,int pageNo,int pageSize){
		List<DormStudentEntry> studentList=dormDao.findAllStudent(schoolId,gradeId, dlassId, sex, name, studentId,pageNo,pageSize);
		return studentList;
	}	
	/**
	 * 查询入住的全部学生数量
	 */
	public int findDormStudentCount(ObjectId schoolId,ObjectId gradeId,ObjectId dlassId,String sex, String name,String studentId){
		return dormDao.findAllStudentCount(schoolId, gradeId, dlassId, sex, name, studentId);
	}
	
	/** 删除一条宿舍学生
	 * @author huanxiaolei@ycode.cn
	 * @param e
	 * @return
	 */
	public void delDormStudent(ObjectId id){
		dormDao.delStudentForDorm(id);
	}
	
	 /**
     * @Description:根据宿舍楼层id查询所有宿舍  
     * @param dormFloorId
     * @return
     * @author:lujiang@ycode.cn
     */
	public List<DormEntry> findByDormFloorId(ObjectId dormFloorId){
		return dormDao.findByDormFloorId(dormFloorId);
	}
	
	/**
	 * @Description: 根据ID更新一条学生宿舍的宿舍区id 
	 * @param id
	 * @param dormAreaId
	 * @author:lujiang@ycode.cn
	 */
	public void updateDormAreaId(ObjectId id,ObjectId dormAreaId){
		dormDao.updateDormAreaId(id, dormAreaId);
	}
	
	/**
	 * @Description: 根据ID更新一条学生宿舍的宿舍楼id 
	 * @param id
	 * @param dormBuildId
	 * @author:lujiang@ycode.cn
	 */
	public void updateDormBuildId(ObjectId id,ObjectId dormBuildId){
		dormDao.updateDormBuildId(id, dormBuildId);
	}
	
	/**
     * 获取宿舍总数
     * @author:huanxiaolei@ycode.cn
     * @param schoolId
     */
	public int countDorm(ObjectId schoolId){
		return dormDao.countDormEntry(schoolId);
	}
	
	/**
     * 获取宿舍总数有条件查询
     * @author:huanxiaolei@ycode.cn
     * @param schoolId
     */
	public int countDormByQuery(ObjectId schoolId,String columName,ObjectId queryId){
		return dormDao.countDormEntryByQuery(schoolId,columName,queryId);
	}
	
	/**
	 * @Description:根据宿舍楼id查询所有宿舍  
	 * @param dormBuildId
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	public List<DormEntry> findByBuildId(ObjectId dormBuildId){
		return dormDao.findByBuildId(dormBuildId);
	}
	
	/**
	 * 根据年级id获取班级
	 * @author:huanxiaolei@ycode.cn
	 */
	public List<ClassEntry> findClassListByGradeId(ObjectId gradeId){
		return classDao.findClassEntryByGradeId(gradeId);
	}
	/**
	 * 根据班级ID查询学生信息
	 * @author:huanxiaolei@ycode.cn
	 */
	public List<UserEntry> findStuName(Collection<ObjectId> stuId){
		return userDao.getUserEntryList(stuId,Constant.FIELDS);
	}
	/**
	 * 根据学校id获取年级
	 * @author zhanghao
	 */
	public List<Grade> findGradeList(String schoolId){
		SchoolEntry school=schoolDao.getSchoolEntry(new ObjectId(schoolId),Constant.FIELDS);
		List<Grade> gradeList=school.getGradeList();
		return gradeList;
	}
	/**
	 * 根据学生id查询是否已有宿舍
	 * @author:huanxiaolei@ycode.cn
	 * @return
	 */
	public DormStudentEntry findStudy(ObjectId studentId){
		return dormDao.findByStudentId(studentId);
	}
	/**
	 * 根据班级id查询已有宿舍的学生
	 * @author:huanxiaolei@ycode.cn
	 * @param classId
	 * @return
	 */
	public List<DormStudentEntry> findStudentByClassId(ObjectId classId){
		return dormDao.findByClassId(classId);
	}
	/**
	 * 宿舍列表删除一个宿舍
	 * @author:huanxiaolei@ycode.cn
	 * @param classId
	 * @return
	 */
	public void deleteDrom(ObjectId dormId){
		dormDao.deleteDorm(dormId);
		dormDao.delDormStudent(dormId);
	}
	/**
	 * 判断登陆着部门是否具有权限
	 * @Description:  
	 * @param uid
	 * @param dName
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	public boolean isDormAdmin(ObjectId uid,String dName){
		List<DepartmentEntry> dList = departmentDao.getDepartmentsByUserId(uid);
		for(DepartmentEntry de : dList){
			if(de.getName().equals(dName)){
				return true;
			}
		}
		return false;
	}
}
