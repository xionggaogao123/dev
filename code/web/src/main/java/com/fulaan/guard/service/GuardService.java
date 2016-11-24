package com.fulaan.guard.service;

import java.util.Collection;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.guard.StudentEnterDao;
import com.db.guard.StudentOutDao;
import com.db.guard.VisitDao;
import com.db.school.ClassDao;
import com.db.school.DepartmentDao;
import com.db.user.UserDao;
import com.mongodb.DBObject;
import com.pojo.guard.StudentEnterEntry;
import com.pojo.guard.StudentOutEntry;
import com.pojo.guard.VisitorEntry;
import com.pojo.school.ClassEntry;
import com.pojo.school.DepartmentEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;

/** 
 * @author chengwei@ycode.cn
 * @version 2015年12月8日 上午10:33:01 
 * 类说明 
 */
@Service
public class GuardService {

	private StudentEnterDao studentEnterDao = new StudentEnterDao();
	private StudentOutDao studentOutDao = new StudentOutDao();
	private VisitDao visitDao = new VisitDao();
	private ClassDao cd = new ClassDao();
	private UserDao ud = new UserDao();
	private DepartmentDao departmentDao=new DepartmentDao() ;
	/**
	 * 添加一条进校记录(cw)
	 * @param e
	 * @return
	 */
	public ObjectId addStudentEnterEntry(StudentEnterEntry e){
		studentEnterDao.addStudentEnterEntry(e);
		return e.getID();
	}
	
	/**
	 * 添加一条出校记录(cw)
	 * @param e
	 * @return
	 */
	public ObjectId addStudentOutEntry(StudentOutEntry e){
		studentOutDao.addStudentOutEntry(e);
		return e.getID();
	}
	
	/**
	 * 删除一条进校记录(cw)
	 * @param id
	 */
	public void deleteStudentEnterEntry(ObjectId id){
		studentEnterDao.deleteStudentEnterEntry(id);
	}
	
	/**
	 * 删除一条出校记录(cw)
	 * @param id
	 */
	public void deleteStudentOutEntry(ObjectId id){
		studentOutDao.deleteStudentOutEntry(id);
	}
	
	/**
	 * 根据条件查询进校记录(cw)
	 * @param grade
	 * @param classroom
	 * @return
	 */
	public List<StudentEnterEntry> queryEnterStudents(String grade, String classroom,int skip,int size){
		return studentEnterDao.queryEnterStudents(grade, classroom,skip,size);
		
	}
	
	/**
	 * 根据条件查询出校记录(cw)
	 * @param grade
	 * @param classroom
	 * @return
	 */
	public List<StudentOutEntry> queryOutStudents(String grade, String classroom,int skip,int size){
		return studentOutDao.queryOutStudents(grade, classroom,skip,size);
		
	}
	
	/**
	 * 根据id查询学生进校记录(cw)
	 * @param id
	 * @return
	 */
	public StudentEnterEntry getEnterStudent(ObjectId id){
		return studentEnterDao.getEnterStudent(id);
	}
	
	/**
	 * 根据id查询学生出校记录(cw)
	 * @param id
	 * @return
	 */
	public StudentEnterEntry getOutStudent(ObjectId id){
		return studentOutDao.getOutStudent(id);
	}
	
	/**
	 * 增加一条来访记录(cw)
	 * @param e
	 * @return
	 */
	public ObjectId addVisitorEntry(VisitorEntry e){
		return visitDao.addVisitorEntry(e);
	}
	
	/**
	 * 删除一条访客记录(cw)
	 * @param id
	 */
	public void deleteVisitorEntry(ObjectId id){
		visitDao.deleteVisitorEntry(id);
	}
	
	/**
	 * 查询所有访客记录(cw)
	 * @return
	 */
	public List<VisitorEntry> queryVisitors(int skip,int size){
		return visitDao.queryVisitors(skip,size);
	}
	
	/**
	 * 根据id查询一条访客记录
	 * @param id
	 * @return
	 */
	public VisitorEntry getVisitor(ObjectId id){
		return visitDao.getVisitor(id);

	}
	
	/**
	 * 根据学校ID查询
	 */
	public List<ClassEntry> findByScId(ObjectId schoolId){
		return cd.findClassInfoBySchoolId(schoolId,Constant.FIELDS);
	}
	
	/**
	 * 根据学生ID查相关信息
	 */
	public List<UserEntry> findStuName(Collection<ObjectId> ids,DBObject fields){
		return ud.getUserEntryList(ids, fields);
	}
	
	/**
	 * 根据年级ID查班级
	 */
	public List<ClassEntry> findByGraId(ObjectId gradeId){
		return cd.findClassEntryByGradeId(gradeId);
	}
	
	/**
	 * 进校人数
	 */
	public int countEnter(String grade, String classroom){
		return studentEnterDao.countEnter(grade,classroom);
	}
	
	/**
	 * 出校人数
	 */
	public int countOut(String grade, String classroom){
		return studentOutDao.countOut(grade,classroom);
	}
	
	/**
	 * 访客人数
	 */
	public int countVisit(){
		return visitDao.countVisit();
	}
	/**
	 * 判断登陆着部门是否具有权限
	 * @Description:  
	 * @param uid
	 * @param dName
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	public boolean isGuardAdmin(ObjectId uid,String dName){
		List<DepartmentEntry> dList = departmentDao.getDepartmentsByUserId(uid);
		for(DepartmentEntry de : dList){
			if(de.getName().equals(dName)){
				return true;
			}
		}
		return false;
	}
}
