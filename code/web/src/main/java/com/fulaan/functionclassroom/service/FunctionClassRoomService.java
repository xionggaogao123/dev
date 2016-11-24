package com.fulaan.functionclassroom.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.db.factory.MongoFacroty;
import com.db.functionclassroom.FunctionClassRoomDao;
import com.db.school.DepartmentDao;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.funcitonclassroom.ClassRoomAppointmentEntry;
import com.pojo.funcitonclassroom.ClassRoomEntry;
import com.pojo.school.DepartmentEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;

/**
 * @author huanxiaolei@ycode.cn
 * @2015年12月21日
 */
@Service
public class FunctionClassRoomService {
    FunctionClassRoomDao fuctionClassRoomDao=new FunctionClassRoomDao() ;
    DepartmentDao departmentDao=new DepartmentDao() ;
	
	/**
	 * 添加功能教室
	 * @param e
	 * @author huanxiaolei@ycode.cn
	 */
	public void addFunctionClassRoomEntry(ClassRoomEntry e){
		fuctionClassRoomDao.addFunctionClassRoom(e);
	}
	
	/**
	 * 功能教室详情
	 * @param classRoomId
	 * @return
	 * @author huanxiaolei@ycode.cn
	 */
	public ClassRoomEntry findClassRoom(ObjectId classRoomId){
		return fuctionClassRoomDao.findClassRoomEntry(classRoomId);
	}
	
	/**
	 * 删除一条功能教室
	 * @param id
	 * @author huanxiaolei@ycode.cn
	 */
	public void delClassRoomEntry(ObjectId classRoomId){
		fuctionClassRoomDao.deleteClassRoomEntry(classRoomId);
	}
	
	/**
	 * 保存编辑功能教室
	 * @author huanxiaolei@ycode.cn
	 */
	public void updateClassRoom(ObjectId classRoomId,int number,String classRoomName,List<ObjectId> userIds){
		fuctionClassRoomDao.updateClassRoomEntry(classRoomId, number, classRoomName, userIds);
	}
	/**
	 * 查询功能教室管理员
	 * @param schoolId
	 * @return
	 */
	public List<UserEntry> findFunClassRoomManages(ObjectId schoolId){
		return fuctionClassRoomDao.findFunClassRoomManage(schoolId);
	}
	/**
	 * 根据学校id查询所有该学校的功能教室
	 * @param schId
	 * @return 
	 * @author chengwei@ycode.cn
	 */
	public List<ClassRoomEntry> queryRoomsBySchId(ObjectId schId, int skip, int limit){
		return fuctionClassRoomDao.queryRoomsBySchId(schId, skip, limit);
	}
	
	/**
	 * 根据教室Id查询所有预约记录
	 * @param clsId
	 * @return
	 * @author chengwei@ycode.cn
	 */
	public List<ClassRoomAppointmentEntry> queryApptByClsId(ObjectId clsId,String term,int skip,int limit){
		return fuctionClassRoomDao.queryApptByClsId(clsId,term,skip,limit);
	}
	
	/**
	 * 添加一条预约记录
	 * @param e
	 * @return
	 * @author chengwei@ycode.cn
	 */
	public List<ClassRoomAppointmentEntry> saveAppointment(ClassRoomAppointmentEntry e){
		List<ClassRoomAppointmentEntry> list = fuctionClassRoomDao.saveAppointment(e);
		return list;
	}
	
	/**
	 * 删除一条预约记录
	 * @param id
	 * @author chengwei@ycode.cn
	 */
	public void deleteAppointment(ObjectId id){
		fuctionClassRoomDao.deleteAppointment(id);
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
		List<ClassRoomAppointmentEntry> list = fuctionClassRoomDao.updateAppintment(id, startTime, endTime, reasons);
		return list;
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
		fuctionClassRoomDao.updateAppint(id, startTime, endTime, reasons);
	}
	
	/**
	 * 根据id更新一条预约记录(我的预约)
	 * @param id
	 * @param startTime
	 * @param endTime
	 * @param reasons
	 * @author chengwei@ycode.cn
	 */
	public void updateMyAppintment(ObjectId id, Long startTime, Long endTime, String reasons){
		fuctionClassRoomDao.updateClassRoomAppointmentEntry(id, startTime, endTime, reasons);
	}
	
	/**
	 * 根据id查询一条预约记录
	 * @param id
	 * @return
	 * @author chengwei@ycode.cn
	 */
	public ClassRoomAppointmentEntry getAppointment(ObjectId id){
		return fuctionClassRoomDao.getAppointment(id);
	}
	
	/**
	 * 根据管理者id获取功能教室信息
	 * @param schoolId
	 * @param administratorId
	 * @return
	 * @author zhanghao@ycode.cn
	 */
	public List<ClassRoomEntry> findManageRoom(ObjectId schoolId,ObjectId administratorId,int skip,int size){
		return fuctionClassRoomDao.findClassRooms(administratorId, schoolId, skip, size);
	}
	
	/**
	 * 根据教室ID查询预约信息
	 * @param roomId
	 * @return
	 * @author zhanghao@ycode.cn
	 */
	public List<ClassRoomAppointmentEntry> findRoomDetil(ObjectId roomId,String term ,int skip, int limit){
		return fuctionClassRoomDao.findRoomDetils(roomId,term,skip,limit);
	}
	
	/**
	 * 添加一条预约信息
	 * @param c
	 * @return
	 * @author zhanghao@ycode.cn
	 */
	public ObjectId addaReservation(ClassRoomAppointmentEntry c){
		fuctionClassRoomDao.addClassRoomAppointmentEntry(c);
		return c.getID();
	}
	
	/**
	 * 根据ID,跟新一条预约信息
	 * @param id
	 * @param startTime
	 * @param endTime
	 * @param reason
	 * @author zhanghao@ycode.cn
	 */
	public void updateReservation(ObjectId id,Long startTime,Long endTime,String reason){
		fuctionClassRoomDao.updateClassRoomAppointmentEntry(id, startTime, endTime, reason);
	}
	
	/**
	 * 根据ID删除一条预约信息(逻辑删除)
	 * @param id
	 * @author zhanghao@ycode.cn
	 */
	public void deleteAReservation(ObjectId id){
		fuctionClassRoomDao.deleteReservation(id);
	}
	
	/**
	 * 根据教室ID,使用开始时间,使用结束时间判断预约是否有冲突
	 * @param roomId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @author zhanghao@ycode.cn
	 */
	public List<ClassRoomAppointmentEntry> findReservationByTime(ObjectId roomId,Long startTime,Long endTime){
		return fuctionClassRoomDao.findClassRoomAppointmentEntryByTime(roomId, startTime, endTime);
	}
	
	/**
	 * 查询学校功能教室总数
	 * @param schid
	 * @return
	 * @author chengwei@ycode.cn
	 */
	public int countRooms(ObjectId schId){
		return fuctionClassRoomDao.countRooms(schId);
	}
	
	/**
	 * 根据功能教室id查询预约记录总数
	 * @param schid
	 * @return
	 * @author chengwei@ycode.cn
	 */
	public int countAppointments(ObjectId classId, String term){
		return fuctionClassRoomDao.countAppointments(classId, term);
	}
	/**
	 * 根据用户id查询我的预约列表
	 * @param userId
	 * @return
	 * @author caotiecheng@ycode.cn
	 */
	public List<ClassRoomAppointmentEntry> myClassRoomAppointment(ObjectId userId,int skip,int size){
		return fuctionClassRoomDao.myClassRoomAppointment(userId,skip,size);
	}
	/**
	 * 根据启始时间及用户id查询预约表
	 * @param userId
	 * @param startTm
	 * @param endTm
	 * @param searchTm
	 * @param skip
	 * @param size
	 * @return
	 * @author caotiecheng@ycode.cn
	 */
	public List<ClassRoomAppointmentEntry> myAppointBySearchTime(ObjectId userId,Long searchTmStart,Long searchTmEnd,int skip,int size){
		return fuctionClassRoomDao.myAppointBySearchTime(userId,searchTmStart,searchTmEnd,skip,size);
	}
	
	/**
	 * 根据id跟新一条预约记录
	 * @param id
	 * @param startTime
	 * @param endTime
	 * @param user
	 * @param reasons
	 * @author caotiecheng@ycode.cn
	 */
	public void myClassRoomAppointment(ObjectId id,String reasons,Long startTime,Long endTime){
		fuctionClassRoomDao.updateMyAppointmentByAppointmentId(id,reasons,startTime,endTime);
	}
	
	
	/**
	 * 根据学校id,管理者id查询学校功能教室总数
	 * @param schid
	 * @return
	 * @author zhanghao@ycode.cn
	 */
	public int countManageRooms(ObjectId schId,ObjectId userId){
		return fuctionClassRoomDao.countManageRooms(schId,userId);
	}
	/**
	 * 获得序号
	 * @author huanxiaolei@ycode.cn
	 */
	public List<ClassRoomEntry> getNum(ObjectId schoolId){
		return fuctionClassRoomDao.getNumber(schoolId);
	}
	/**
	 * 根据教室Id查询所有学期
	 * @param clsId
	 * @return
	 * @author huanxiaolei@ycode.cn
	 */
	public List<String> getTerms(ObjectId classRoomId){
		List<String> termList = new ArrayList<String>();
		BasicDBList dbList =  fuctionClassRoomDao.distinctTerm(classRoomId);
		for(Object o:dbList){
			termList.add(o.toString());
		}
		return termList;
	}
	
	/**
	 * 根据用户iD统计预约教室总数	
	 */
	public int countSum(ObjectId userId){
		return fuctionClassRoomDao.countSum(userId);
	}
	
	/**
	 * 根据用户iD统计预约教室总数(检索)	
	 */
	public int countSum1(ObjectId userId,long searchTmStart,long searchTmEnd){
		return fuctionClassRoomDao.countSum1(userId, searchTmStart, searchTmEnd);
	}
	/**
	 * 判断登陆着部门是否具有权限
	 * @Description:  
	 * @param uid
	 * @param dName
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	public boolean isDeAdmin(ObjectId uid,String dName){
		List<DepartmentEntry> dList = departmentDao.getDepartmentsByUserId(uid);
		for(DepartmentEntry de : dList){
			if(de.getName().equals(dName)){
				return true;
			}
		}
		return false;
	}
	/**
	 * 查询学校所有管理员
	 * @Description:  
	 * @param schoolId
	 * @return
	 * @author:lujiang@ycode.cn
	 */
	public List<UserEntry> findManagers(ObjectId schoolId){
     List<UserEntry> userEntryList= fuctionClassRoomDao.findManagers(schoolId);
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
     List<UserEntry> userEntryList= fuctionClassRoomDao.findAllUsersBySchoolId(schoolId);
     return userEntryList;
	}

	/**
	 * 我是否管理教室
	 * @param userId
	 * @return
	 */
	public int findMyManageRoom(ObjectId userId) {
		return fuctionClassRoomDao.findMyManageRoom(userId);
	}
}






