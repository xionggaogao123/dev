package com.fulaan.user.service;



import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.text.Collator;
import java.util.*;

import cn.jpush.api.report.UsersResult;

import com.db.exam.ScoreDao;
import com.fulaan.examresult.controller.IdNameDTO;
import com.fulaan.school.service.SchoolService;



import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sql.dao.UserBalanceDao;



import com.db.app.RegionDao;
import com.db.school.*;
import com.db.user.UserDao;
import com.fulaan.groupdiscussion.service.EaseMobService;
import com.fulaan.myclass.controller.UserIdAndScoreView;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.FieldValuePair;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.app.IdValuePair;
import com.pojo.app.RegionEntry;
import com.pojo.emarket.UserBalanceDTO;
import com.pojo.school.*;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.user.UserInfoDTO;
import com.pojo.user.UserRole;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.MD5Utils;


/**
 * Created by yan on 2015/2/27.
 */
@Service
public class UserService {

	private static final Logger logger=Logger.getLogger(UserService.class);
	/**
	 * 用户一般性字段
	 */
	public static final DBObject COMMON_FIELDS=new BasicDBObject("nm",1).append("avt", 1);
	
	
    private UserDao userDao=new UserDao();
    private SchoolDao schoolDao=new SchoolDao();
    private ClassDao classDao = new ClassDao();
    private TeacherClassSubjectDao teacherClassSubjectDao =new TeacherClassSubjectDao();
    private UserBalanceDao userBalanceDao =new UserBalanceDao();
    private InterestClassDao interestClassDao = new InterestClassDao();
    private RegionDao regionDao=new RegionDao();
    @Autowired
    EaseMobService easeMobService;
    @Autowired
    SchoolService schoolService;

    private DepartmentDao departmentDao =new DepartmentDao();

    private ScoreDao scoreDao = new ScoreDao();


    /**
     * 通过用户id获取用户所在学校的注册地址id
     * @param id
     * @return
     */
    public String findGeoIdByUserId(String id) {
        UserEntry userEntry=userDao.getUserEntry(new ObjectId(id), Constant.FIELDS);
        ObjectId schoolId = userEntry.getSchoolID();
        SchoolEntry schoolEntry=schoolDao.getSchoolEntry(schoolId,Constant.FIELDS);
        ObjectId regionId=schoolEntry.getRegionId();
        return regionId.toString();
    }

    /**
     * 通过用户id获取用户信息
     * @param id
     * @return
     */
    public UserDetailInfoDTO findUserInfoHasCityName(String id) {
        UserEntry userEntry=userDao.getUserEntry(new ObjectId(id), Constant.FIELDS);
        ObjectId schoolId = userEntry.getSchoolID();
        SchoolEntry schoolEntry=schoolDao.getSchoolEntry(schoolId,Constant.FIELDS);
        ObjectId regionId=schoolEntry.getRegionId();
        RegionEntry regionEntry=regionDao.getRegionById(regionId);
        UserDetailInfoDTO userInfoDTO=new UserDetailInfoDTO(userEntry);
        userInfoDTO.setCityName(regionEntry.getName());
        return userInfoDTO;
    }
    /*
    * 主键查询
    *
    * */
    public UserDetailInfoDTO getUserInfoById(String id) {
        UserEntry userEntry=userDao.getUserEntry(new ObjectId(id), Constant.FIELDS);
        if(userEntry==null) return null;
        return new UserDetailInfoDTO(userEntry);
    }

    /**
     * 查询用户信息
     * @param uids
     * @return
     */
    public List<UserDetailInfoDTO> findUserInfoByUserIds(List<String> uids) {
        List<ObjectId> objectIdList=new ArrayList<ObjectId>();
        for(String sid:uids){
            objectIdList.add(new ObjectId(sid));
        }
        List<UserEntry> userEntryList=userDao.getUserEntryList(objectIdList,Constant.FIELDS);
        List<UserDetailInfoDTO> userInfoDTO4WBs=new ArrayList<UserDetailInfoDTO>();
        for(UserEntry userEntry:userEntryList){
            UserDetailInfoDTO userInfoDTO4WB=new UserDetailInfoDTO(userEntry);
            userInfoDTO4WBs.add(userInfoDTO4WB);
        }
        return userInfoDTO4WBs;
    }

    /**
     * 查询用户信息
     * @param uids
     * @return
     */
    public List<UserDetailInfoDTO> findUserInfoByIds(List<ObjectId> uids) {
        List<UserEntry> userEntryList=userDao.getUserEntryList(uids,Constant.FIELDS);
        List<UserDetailInfoDTO> userInfoDTO4WBs=new ArrayList<UserDetailInfoDTO>();
        for(UserEntry userEntry:userEntryList){
            UserDetailInfoDTO userInfoDTO4WB=new UserDetailInfoDTO(userEntry);
            userInfoDTO4WBs.add(userInfoDTO4WB);
        }
        sortList(userInfoDTO4WBs);
        return userInfoDTO4WBs;
    }

    /**
     * 获取用户信息
     * @param userIds
     * @return
     */
    public List<IdNameDTO> findUserIdNameByIds(List<ObjectId> userIds) {
        List<UserEntry> userEntryList=userDao.getUserEntryList(userIds,Constant.FIELDS);
        List<IdNameDTO> userList = new ArrayList<IdNameDTO>();
        for (UserEntry userEntry : userEntryList) {
            userList.add(new IdNameDTO(userEntry.getID().toString(), userEntry.getUserName()));
        }
        return userList;
    }


    /**
     * 对list进行排序
     * @param list
     */
    public void sortList(List<UserDetailInfoDTO> list){
        Collections.sort(list, new Comparator<UserDetailInfoDTO>() {
            public int compare(UserDetailInfoDTO obj1 , UserDetailInfoDTO obj2) {
                int flag= Collator.getInstance(Locale.CHINESE).compare(obj1.getUserName(),obj2.getUserName());

                return flag;
            }
        });
    }


    /**
     * 查找全校学生
     * @return
     */
    public Map<String, Object> getSchoolAllStudent(String schoolId){
        Map<String, Object> model = new HashMap<String, Object>();
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        List<UserEntry> userEntryList = userDao.getStudentEntryBySchoolId(new ObjectId(schoolId),new BasicDBObject("nm", 1));
        if(userEntryList!=null && userEntryList.size()>0){
            for(UserEntry userEntry : userEntryList){
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("nickName", userEntry.getUserName());
                map.put("id", userEntry.getID().toString());
                rows.add(map);
            }
        }
        model.put("rows", rows);
        return model;
    }

    /**
     * 查找特定年级的学生
     * @return
     */
    public Map<String, Object> getGradeStudent(List<String> gradeList, String classId){
        List<Map<String, Object>> classList = new ArrayList<Map<String, Object>>();
        List<ObjectId> studentIdList = new ArrayList<ObjectId>();
        List<ObjectId> gradeIdList = new ArrayList<ObjectId>();
        for (String gradeId : gradeList) {
            gradeIdList.add(new ObjectId(gradeId));
        }
        List<ClassEntry> classEntryList = classDao.findClassEntryByGradeIds(gradeIdList);

        if (null != classEntryList) {
            for (ClassEntry classEntry : classEntryList) {
                studentIdList.addAll(classEntry.getStudents());
                Map<String, Object> info = new HashMap<String, Object>();
                info.put("classId", classEntry.getID().toString());
                info.put("className", classEntry.getName());
                classList.add(info);
            }
        }
        if(null != classId && "" != classId) {
            ClassEntry classEntry = classDao.getClassEntryById(new ObjectId(classId), Constant.FIELDS);
            if(null !=classEntry) {
                studentIdList.clear();
                studentIdList.addAll(classEntry.getStudents());
            }
        }
        Map<String, Object> model = new HashMap<String, Object>();
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        List<UserEntry> userEntryList = userDao.getUserEntryList(studentIdList,new BasicDBObject("nm", 1));
        if(userEntryList!=null && userEntryList.size()>0){
            for(UserEntry userEntry : userEntryList){
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("nickName", userEntry.getUserName());
                map.put("id", userEntry.getID().toString());
                rows.add(map);
            }
        }
        model.put("rows", rows);
        model.put("classList", classList);
        return model;
    }

    /*
    *
    * 通过父ID 查询孩子信息
    *
    * */
    public UserDetailInfoDTO findStuInfoByParentId(String id) {
        UserEntry userInfo=userDao.getUserEntryByParentId(new ObjectId(id));
        if(userInfo==null) return  null;
        return new UserDetailInfoDTO(userInfo);
    }

    /**
     * 查询用户信息
     * @param userId
     * @return
     */
    public UserDetailInfoDTO selectUserInfoHasClassName(String userId) {
        UserEntry userEntry=userDao.getUserEntry(new ObjectId(userId), Constant.FIELDS);
        UserDetailInfoDTO userInfoDTO4WB=new UserDetailInfoDTO(userEntry);
        //查找 地区名称
        SchoolEntry schoolEntry=schoolDao.getSchoolEntry(userEntry.getSchoolID(),Constant.FIELDS);
        ObjectId regionId=schoolEntry.getRegionId();
        RegionEntry regionEntry=regionDao.getRegionById(regionId);
        userInfoDTO4WB.setCityName(regionEntry.getName());
        //查找班级名称
        ClassEntry classEntry=classDao.getClassEntryByStuId(userEntry.getID(), new BasicDBObject("nm", 1));
        userInfoDTO4WB.setMainClassName(classEntry.getName());
        return userInfoDTO4WB;
    }
    
    /**
     * 根据用户名精确查询
     * @param userName
     * @return
     */
    public UserEntry searchUserByUserName(String userName) {
    	return userDao.searchUserByUserName(userName);
    }
    
    
    /**
     * 根据用户登录名精确查询
     * @param userLoginName
     * @return
     */
    public UserEntry searchUserByUserLoginName(String userLoginName) {
    	return userDao.searchUserByLoginName(userLoginName);
    }
    
   
    /**
     * 根据用户身份证查询
     * @param sid
     * @return
     */
    public UserEntry searchUserBySid(String sid) {
    	return userDao.searchUserBySid(sid);
    }
    
    /**
     * 根据用户手机登录
     * @param mobile
     * @return
     */
    public UserEntry searchUserByMobile(String mobile) 
    {
    	return userDao.searchUserByMobile(mobile);
    }
    
    /**
     * 根据用户邮箱
     * @param mobile
     * @return
     */
    public UserEntry searchUserByEmail(String email)
    {
    	return userDao.searchUserByEmail(email);
    }
    
    /**
     * 根据ID查询
     * @param id
     * @return
     */
    public UserEntry searchUserId(ObjectId id)
    {
    	return userDao.getUserEntry(id, Constant.FIELDS);
    }

    /**
     * 根据CHATID查询
     * @param id
     * @return
     */
    public UserEntry searchUserChatId(String id)
    {
        return userDao.getUserEntryByChatid(id, Constant.FIELDS);
    }
    
    
   /**
    * 通过绑定用户查询 
    * @param type
    * @param bindValue
    * @return
    */
   public UserEntry searchUserByUserBind (int type, String bindValue)
   {
	   return userDao.searchUserByUserBind(type, bindValue);
   }
    
   
    /**
     * 根据用户ID查询，返回用户的map
     * @param ids
     * @param fields
     * @return
     */
    public Map<ObjectId, UserEntry> getUserEntryMap(Collection<ObjectId> ids,DBObject fields)
    {
    	return userDao.getUserEntryMap(ids, fields);
    }

    /**
     * 根据用户ID查询，返回用户的map
     * @param ids
     * @param fields
     * @return
     */
    public Map<ObjectId, UserEntry> getUserEntryMapByUserIds(Collection<ObjectId> ids,String userName,DBObject fields)
    {
        return userDao.getUserEntryMap(ids,userName,fields);
    }

    /**
     * 根据用户ID查询，返回用户的map
     * @param ids
     * @param fields
     * @return
     */
    public Map<ObjectId, UserEntry> getUserEntryMap2(Collection<ObjectId> ids,DBObject fields)
    {
        return userDao.getUserEntryMap2(ids, fields);
    }


    /**
     * 根据用户名查询，返回用户的id
     * @param ids
     * @param fields
     * @return
     */
    public List<ObjectId> getUserIdsByName(Collection<String> userNames)
    {
        return userDao.getUserIdsByUserName(userNames);
    }

    
    /**
     * 添加用户
     */
    public ObjectId addUser(UserEntry e)
    {
    	return userDao.addUserEntry(e);
    }
    

    /**
     * 删除用户
     * @param uid
     */
    public void deleteUser(ObjectId uid)
    {
    	userDao.logicRemoveUser(uid);
    }

    /**
     * 通过学校ID查找用户信息
     * @param schoolID
     * @param fields
     * @return
     */
    public Map<ObjectId,UserEntry>  getUserEntryMapBySchoolid(ObjectId schoolID,DBObject fields)
    {
    	Map<ObjectId,UserEntry> retMap =new HashMap<ObjectId, UserEntry>();
    	List<UserEntry> list= userDao.getUserInfoBySchoolid(schoolID,fields);
    	for(UserEntry ue:list)
    	{
    		retMap.put(ue.getID(), ue);
    	}
    	return retMap;
    }
    
    /**
     * 得到一个学校的老师
     * @param schoolId
     * @param fields
     * @return
     */
    public List<UserEntry> getTeacherEntryBySchoolId(ObjectId schoolId, BasicDBObject fields)
    {
    	return userDao.getTeacherEntryBySchoolId(schoolId, fields);
    }

    /**
     * 得到一个学校的老师
     * @param schoolId
     * @param fields
     * @return
     */
    public List<UserEntry> getTeacherEntryBySchoolId(ObjectId schoolId, String teaName, BasicDBObject fields)
    {
        return userDao.getTeacherEntryBySchoolId(schoolId, teaName, fields);
    }

    /**
     * 得到一个学校的学生
     * @param schoolId
     * @param fields
     * @return
     */
    public List<UserEntry> getStudentEntryBySchoolId(ObjectId schoolId, BasicDBObject fields)
    {
        return  userDao.getStudentEntryBySchoolId(schoolId, fields);
    }
    /**
     *  得到用户学校信息 
     * @param users 必须包含_id和si两个字段
     * @param schoolFields 学校中的字段
     * @return key为用户的ID
     */
    public Map<ObjectId,SchoolEntry> getUserSchoolInfo(Collection<UserEntry> users,DBObject schoolFields)
    {
    	Map<ObjectId,SchoolEntry> retMap =new HashMap<ObjectId, SchoolEntry>();
    	
    	if(null!=users)
    	{
	    	List<ObjectId> schoolsId =MongoUtils.getFieldObjectIDs(users, "si");
	    	Map<ObjectId, SchoolEntry> schoolInfoMap=schoolDao.getSchoolMap(schoolsId, schoolFields);
	    	SchoolEntry se;
	    	for(UserEntry ue:users)
	    	{
	    		se=schoolInfoMap.get(ue.getSchoolID());
	    		if(null!=se)
	    		{
	    			retMap.put(ue.getID(), se);
	    		}
	    	}
    	}
    	return retMap;
    }
    
    /**
     * 通过学校查询老师信息，并且按照 <li>学科</li>
                           <li>班级</li>
                           <li>班主任</li>
                      进行分组
     * @param schoolId
     * @param type 1学科 2 班级 3班主任
     * @return
     */
    public Map<IdNameValuePairDTO,Set<IdNameValuePairDTO>> getTeacherMap(ObjectId schoolId,int type)
    {
    	Map<IdNameValuePairDTO,Set<IdNameValuePairDTO>> retMap =new HashMap<IdNameValuePairDTO, Set<IdNameValuePairDTO>>();
    	//如果学校人数过大，会有性能影响
    	//todo by fourer
    	List<UserEntry> userEntryList = userDao.getUserEntryBySchoolIdList(schoolId, new BasicDBObject("r",1).append("nnm", 1));
    	Map<ObjectId,UserEntry> userMap=new HashMap<ObjectId, UserEntry>();
    	for(UserEntry e:userEntryList)
    	{
    		if(UserRole.isTeacher(e.getRole()))
    		{
    			userMap.put(e.getID(), e);
    		}
    	}
    	
    	UserEntry e;
    	if(type==1 || type==2)//学科 班级
    	{
    		List<TeacherClassSubjectEntry> tcjList=teacherClassSubjectDao.findSubjectByTeacherIds(userMap.keySet());
    		for(TeacherClassSubjectEntry tcj:tcjList)
    		{
    			IdNameValuePairDTO dto =new IdNameValuePairDTO(type==1?tcj.getSubjectInfo():tcj.getClassInfo());
    			if(!retMap.containsKey(dto))
    			{
    				retMap.put(dto, new HashSet<IdNameValuePairDTO>());
    			}
    			e=userMap.get(tcj.getTeacherId());
    			if(null!=e)
    			{
    				retMap.get(dto).add(new IdNameValuePairDTO(e));
    			}
    		}
    	}
    
    	if(type==3)//班主任
    	{
    		List<ClassEntry> ceList=classDao.findClassInfoBySchoolId(schoolId, Constant.FIELDS);
    		for(ClassEntry ce:ceList)
    		{
    			IdNameValuePairDTO dto =new IdNameValuePairDTO(ce);
    			if(!retMap.containsKey(dto))
    			{
    				retMap.put(dto, new HashSet<IdNameValuePairDTO>());
    			}
    			e=userMap.get(ce.getMaster());
    			if(null!=e)
    			{
    				retMap.get(dto).add(new IdNameValuePairDTO(e));
    			}
    		}
    	}
    	return retMap;
    }
    
    
    /**
     * 通过学校查询学生或家长信息，并且按照 <li>班级</li>进行分组
     * @param schoolId
     * @param type 1学生 2家长
     * @return
     */
    public Map<IdNameValuePairDTO,Set<IdNameValuePairDTO>> getStudentOrParentMap(ObjectId schoolId,int type)
    {
    	Map<IdNameValuePairDTO,Set<IdNameValuePairDTO>> retMap =new HashMap<IdNameValuePairDTO, Set<IdNameValuePairDTO>>();
    	List<ClassEntry> ceList=classDao.findClassInfoBySchoolId(schoolId, Constant.FIELDS);
    	
    	Set<ObjectId> totalSet =new HashSet<ObjectId>();
    
    	for(ClassEntry ce:ceList)
    	{
    			IdNameValuePairDTO dto =new IdNameValuePairDTO(ce);
    			if(!retMap.containsKey(dto))
    			{
    				retMap.put(dto, new HashSet<IdNameValuePairDTO>());
    			}
    			
    			for(ObjectId stuId:ce.getStudents())
    			{
    				retMap.get(dto).add(new IdNameValuePairDTO(stuId));
    				totalSet.add(stuId);
    			}
    	}
    	
    	Map<ObjectId, UserEntry> userMap=	userDao.getUserEntryMap(totalSet, new BasicDBObject().append("nm", 1).append("cid", 1)) ;
    	UserEntry e;
    	for(Map.Entry<IdNameValuePairDTO,Set<IdNameValuePairDTO>> entry:retMap.entrySet() )
    	{
    		for(IdNameValuePairDTO dto:entry.getValue())
    		{
    			e=userMap.get(dto.getId());
    			if(null!=e)
    			{
    				if(Constant.ONE==type)
    				{
    				  dto.setValue(e.getUserName());
    				}
    				if(Constant.TWO==type)
    				{
    					//todo 如果有学生没有关联家长，则创建一个objectid，这样系统可以运行，只是增加一点垃圾数据
    					dto.setId(e.getConnectIds().size()>0?e.getConnectIds().get(0):new ObjectId());
    				    dto.setValue(e.getUserName()+"家长");
    				}
    			}
    		}
    	}
    	return retMap;
    }
    
    
    /**
     * 得到部门人员
     * @param schoolId
     * @return
     */
    public Map<IdNameValuePairDTO,Set<IdNameValuePairDTO>> getDepartmemtMembersMap(ObjectId schoolId)
    {
    	Map<IdNameValuePairDTO,Set<IdNameValuePairDTO>> retMap =new HashMap<IdNameValuePairDTO, Set<IdNameValuePairDTO>>();
    	
    	List<DepartmentEntry> depList=departmentDao.getDepartmentEntrys(schoolId);
    	
    	Set<ObjectId> totalSet =new HashSet<ObjectId>();
    
    	for(DepartmentEntry ce:depList)
    	{
    			IdNameValuePairDTO dto =new IdNameValuePairDTO(ce);
    			if(!retMap.containsKey(dto))
    			{
    				retMap.put(dto, new HashSet<IdNameValuePairDTO>());
    			}
    			
    			for(ObjectId stuId:ce.getMembers())
    			{
    				retMap.get(dto).add(new IdNameValuePairDTO(stuId));
    				totalSet.add(stuId);
    			}
    	}
    	
    	Map<ObjectId, UserEntry> userMap=	userDao.getUserEntryMap(totalSet, new BasicDBObject().append("nm", 1).append("cid", 1)) ;
    	UserEntry e;
    	for(Map.Entry<IdNameValuePairDTO,Set<IdNameValuePairDTO>> entry:retMap.entrySet() )
    	{
    		for(IdNameValuePairDTO dto:entry.getValue())
    		{
    			e=userMap.get(dto.getId());
    			if(null!=e)
    			{
    			  dto.setValue(e.getUserName());
    			}
    		}
    	}
    	return retMap;
    }
    
    /**
     * 查找校领导
     * @param schoolId
     * @return
     */
    public List<UserEntry> getSchoolLeader(ObjectId schoolId)
    {
    	return userDao.getSchoolLeader(schoolId);
    }

    /**
     * 添加用户
     * @param userInfoDTO4WB
     * @return
     */
    public String addUserInfo(UserDetailInfoDTO userInfoDTO4WB) {
        UserEntry userEntry=userInfoDTO4WB.exportEntry();
        userDao.addUserEntry(userEntry);
        //添加一个用户，增加环信功能
        try
        {
          easeMobService.createNewUser(userEntry.getChatId().toString());
        }catch(Exception ex)
        {
        	logger.error("", ex);
        }
        return userEntry.getID().toString();
    }

    /**
     * 重置密码
     * @param id
     * @param initPwd
     * @return
     */
    public void resetPwd(ObjectId id, String initPwd) {
        try {
			userDao.update(id,"pw",MD5Utils.getMD5String(initPwd),false);
		} catch (IllegalParamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void updateNickNameAndSexById(String studenrid, String stnickname, int sex) {
        userDao.updateNickNameAndSexById(new ObjectId(studenrid), stnickname, sex);
    }

    public void updateUserNameAndSexById(String studenrid, String stname, int sex) {
        userDao.updateUserNameAndSexById(new ObjectId(studenrid), stname, sex);
        UserEntry  userEntry = userDao.getUserEntry(new ObjectId(studenrid),null);
        if (userEntry.getConnectIds()!=null && userEntry.getConnectIds().size()!=0) {
            for (ObjectId userid :userEntry.getConnectIds()) {
                UserEntry  user = userDao.getUserEntry(userid,null);
                if (user.getUserName().contains("爸爸")) {
                    FieldValuePair fvp = new FieldValuePair("nm",stname+"爸爸");
                    userDao.update(userid,fvp);
                } else if (user.getUserName().contains("妈妈")) {
                    FieldValuePair fvp = new FieldValuePair("nm",stname+"妈妈");
                    userDao.update(userid,fvp);
                } else if (user.getUserName().contains("家长")) {
                    FieldValuePair fvp = new FieldValuePair("nm",stname+"家长");
                    userDao.update(userid,fvp);
                }

            }
        }
        //同步到考务管理学生姓名
        scoreDao.updateStuName(new ObjectId(studenrid), stname);
    }

    public void addExp4Student(List<UserIdAndScoreView> teacherScoreJsonList) {
        for(UserIdAndScoreView userIdAndScoreView :teacherScoreJsonList){
            UserEntry userEntry=userDao.getUserEntry(new ObjectId(userIdAndScoreView.getUserId()), new BasicDBObject("exp", 1).append(Constant.ID,1));
            int exp=userEntry.getExperiencevalue()+ userIdAndScoreView.getScore();
            try {
                userDao.update(userEntry.getID(),"exp",exp,true);
            } catch (IllegalParamException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 获取用户账户余额
     * @param userId
     * @return
     */
    public double getUserBalance(String userId){
        UserBalanceDTO ubInfo = userBalanceDao.getUserBalanceInfo(userId);
    	double balance = 0.0;
    	if(null != ubInfo){
    		balance = ubInfo.getBalance();
    		//System.out.println(balance);
    	}
    	return balance;
    }


    public List<UserEntry> findUserRoleInfoBySchoolId(String schoolId) {
        BasicDBObject fields =new BasicDBObject("r",Constant.ONE);
        List<UserEntry> userEntryList = userDao.getUserEntryBySchoolIdList(new ObjectId(schoolId), fields);
        return userEntryList;
    }


    /**
     * 根据学校id查询学校全部人员信息
     * @param schoolId
     * @return
     */
    public List<UserDetailInfoDTO> findUserInfoBySchoolId(String schoolId, int role) {
    	
        List<UserEntry> userEntryList = userDao.getUserBySchoolIdAndRole(new ObjectId(schoolId), role, new BasicDBObject("nm",1));
        List<UserDetailInfoDTO> userInfoDTOList = new ArrayList<UserDetailInfoDTO>();
        for (UserEntry userEntry : userEntryList) {
            UserDetailInfoDTO userInfoDTO = new UserDetailInfoDTO(userEntry.getID(),userEntry.getRealUserName());
            userInfoDTOList.add(userInfoDTO);
        }
        return userInfoDTOList;
    }


    public List<UserDetailInfoDTO> findUserBySchoolId2(String schoolId) {
        List<UserEntry> userEntryList = userDao.getUserEntryBySchoolIdList(new ObjectId(schoolId), new BasicDBObject(Constant.ID, 1).append("chatid", 1).append("avt", 1).append("nnm", 1).append("nm", 1).append("r",1));
        List<UserDetailInfoDTO> userInfoDTOList = new ArrayList<UserDetailInfoDTO>();
        for (UserEntry userEntry : userEntryList) {
            UserDetailInfoDTO userInfoDTO = new UserDetailInfoDTO(userEntry,1);
            userInfoDTOList.add(userInfoDTO);
        }
        return userInfoDTOList;
    }

    /**
     * 根据学校id查询学校全部人员信息
     * @param schoolID
     * @return
     */
    public List<UserDetailInfoDTO> getUserInfoBySchoolid(ObjectId schoolID) {
        List<UserEntry> userEntryList = userDao.getTeacherEntryBySchoolId(schoolID, Constant.FIELDS);
        List<UserDetailInfoDTO> userlist = new ArrayList<UserDetailInfoDTO>();
        if (userEntryList!=null && userEntryList.size()!=0) {
            for (UserEntry user : userEntryList) {
                if (UserRole.isHeadmaster(user.getRole())) {
                    userlist.add(new UserDetailInfoDTO(user));
                }
            }
        }
        return userlist;
    }

    public Map<String, Object> getPresTerBySchoolid(ObjectId id,ObjectId schoolID,Map<String,Object> map) {
        List<UserEntry> userEntryList = userDao.getTeacherEntryBySchoolId(schoolID, Constant.FIELDS);
        List<UserDetailInfoDTO> presidentList = new ArrayList<UserDetailInfoDTO>();
        List<UserDetailInfoDTO> teachersList = new ArrayList<UserDetailInfoDTO>();
        if (userEntryList!=null && userEntryList.size()!=0) {
            for (UserEntry user : userEntryList) {
                if (UserRole.isHeadmaster(user.getRole())) {
                    presidentList.add(new UserDetailInfoDTO(user));
                }
                if (UserRole.isTeacher(user.getRole())||UserRole.isManager(user.getRole())) {
                    if (!user.getID().equals(id)) {
                        teachersList.add(new UserDetailInfoDTO(user));
                    }
                }
            }
            map.put("presidentList", presidentList);
            map.put("teachersList", teachersList);
        }
        return map;
    }

    /**
     * 查询用户信息
     * @param userName
     * @return
     */
    public List<UserDetailInfoDTO> findUserInfoByUserName(String userName) {
        List<UserEntry> userEntryList=userDao.findEntryByUserName(userName);
        List<UserDetailInfoDTO> userDetailInfoDTOList=new ArrayList<UserDetailInfoDTO>();
        if(userEntryList!=null){
            for(UserEntry userEntry:userEntryList){
                UserDetailInfoDTO userDetailInfoDTO=new UserDetailInfoDTO(userEntry);
                userDetailInfoDTOList.add(userDetailInfoDTO);
            }
        }
        return userDetailInfoDTOList;
    }

    /** 更新用户头像
     * @param userId
     * @param avatar
     * @throws Exception
     */
    public void updateAvatar(String userId,String avatar) throws Exception{
        if(!ObjectId.isValid(userId))
        {
            throw new IllegalParamException("the id ["+userId+" ] is valid!!");
        }
        FieldValuePair fvp = new FieldValuePair("avt",avatar);
        userDao.update(new ObjectId(userId),fvp);
    }

    /**
     * 取得用户密码
     * @param userId
     * @return
     * @throws Exception
     */
    public String getUserPassword(String userId)throws Exception{
        if(!ObjectId.isValid(userId))
        {
            throw new IllegalParamException("the id ["+userId+" ] is valid!!");
        }
        return userDao.getUserEntry(new ObjectId(userId),Constant.FIELDS).getPassword();

    }

    /**
     * 修改用户密码
     * @param userId
     * @return
     * @throws Exception
     */
    public void updatePassword(String userId,String password)throws Exception{
        if(!ObjectId.isValid(userId))
        {
            throw new IllegalParamException("the id ["+userId+" ] is valid!!");
        }
        FieldValuePair fvp = new FieldValuePair("pw",password);
        userDao.update(new ObjectId(userId),fvp);
    }

    public void updateUserGroupList(List<String> userlist,IdValuePair idvalue) {
        for (String userid : userlist) {
            userDao.updateUserGroupList(userid,idvalue);
        }
    }

    public void deleteUserGroupList(List<String> memberList,String roomid) {
        for (String userid : memberList) {
            UserEntry userentry = userDao.getUserEntryByChatid(userid, Constant.FIELDS);
            for (IdValuePair idvaluepair : userentry.getGroupInfoList()) {
                if (idvaluepair.getBaseEntry().getString("id").equals(roomid)) {
                    userDao.deleteUserGroupList(userid,idvaluepair);
                }
            }

        }
    }


    /** 得到用户所在的班级列表
     * @param userId
     * @return
     */
    public List<ClassInfoDTO> getClassDTOList(ObjectId userId,int userRole){
        List<ClassInfoDTO> classInfoDTOList = new ArrayList<ClassInfoDTO>();

        if(UserRole.isStudent(userRole) || UserRole.isParent(userRole)){

            if(UserRole.isParent(userRole)){
                UserDetailInfoDTO stuinfo = findStuInfoByParentId(userId.toString());
                if (stuinfo!=null) {
                    userId = new ObjectId(stuinfo.getId());
                }
                else {
                    userId = null;
                }
            }

            if(userId!=null) {
                List<ClassEntry> classEntryList = classDao.getClassEntryListByStudentId(userId,
                        Constant.FIELDS);
                for (ClassEntry classEntry : classEntryList) {

                    ClassInfoDTO classInfoDTO = new ClassInfoDTO();
                    classInfoDTO.setClassName(classEntry.getName());
                    classInfoDTO.setId(classEntry.getID().toString());
                    classInfoDTOList.add(classInfoDTO);
                }

                //兴趣班
                List<InterestClassEntry> interestClassEntryList = interestClassDao.findClassInfoByStuId(userId);
                for (InterestClassEntry classEntry : interestClassEntryList) {

                    ClassInfoDTO classInfoDTO = new ClassInfoDTO();
                    classInfoDTO.setClassName(classEntry.getClassName());
                    classInfoDTO.setId(classEntry.getID().toString());
                    classInfoDTOList.add(classInfoDTO);
                }
            }


        }
        else if(UserRole.isHeadmaster(userRole))
        {
            String schoolId=getUserInfoById(userId.toString()).getSchoolID();
            List<ClassEntry> classEntryList = classDao.findClassInfoBySchoolId(new ObjectId(schoolId),Constant.FIELDS);
            //todo : 兴趣班先不加
            for(ClassEntry classEntry:classEntryList){

                ClassInfoDTO classInfoDTO = new ClassInfoDTO();
                classInfoDTO.setClassName(classEntry.getName());
                classInfoDTO.setId(classEntry.getID().toString());
                classInfoDTOList.add(classInfoDTO);
            }


            //兴趣班
            List<InterestClassEntry> interestClassEntryList = interestClassDao.findClassBySchoolId(new ObjectId(schoolId), -1, null, null);
            for(InterestClassEntry classEntry:interestClassEntryList){

                ClassInfoDTO classInfoDTO = new ClassInfoDTO();
                classInfoDTO.setClassName(classEntry.getClassName());
                classInfoDTO.setId(classEntry.getID().toString());
                classInfoDTOList.add(classInfoDTO);
            }
        }else if(UserRole.isTeacher(userRole) ) {
            List<ClassEntry> classEntryList = classDao.getClassEntryByTeacherId(userId,Constant.FIELDS);
            //todo : 兴趣班先不加
            for(ClassEntry classEntry:classEntryList){

                ClassInfoDTO classInfoDTO = new ClassInfoDTO();
                classInfoDTO.setClassName(classEntry.getName());
                classInfoDTO.setId(classEntry.getID().toString());
                classInfoDTOList.add(classInfoDTO);
            }


            //兴趣班
            List<InterestClassEntry> interestClassEntryList = interestClassDao.findClassInfoByTeacherId(userId,-1);
            for(InterestClassEntry classEntry:interestClassEntryList){

                ClassInfoDTO classInfoDTO = new ClassInfoDTO();
                classInfoDTO.setClassName(classEntry.getClassName());
                classInfoDTO.setId(classEntry.getID().toString());
                classInfoDTOList.add(classInfoDTO);
            }


        }


        return classInfoDTOList;
    }

    /**
     * 查询学生家长信息
     * @param studentId
     * @return
     */
    public List<UserDetailInfoDTO> findParentByStuId(String studentId) {
        List<UserDetailInfoDTO> userDetailInfoDTOs = new ArrayList<UserDetailInfoDTO>();
        if(null!=studentId && ObjectId.isValid(studentId)){
            List<UserEntry> userEntryList=userDao.findParentEntryByStuId(new ObjectId(studentId));
            for (UserEntry user : userEntryList) {
                userDetailInfoDTOs.add(new UserDetailInfoDTO(user));
            }
        }
        return userDetailInfoDTOs;
    }

    public void updateIntroduction(String introduce, String userId) {
        userDao.updateIntroduction(introduce, new ObjectId(userId));
    }
    /*
    *更新用户角色
    * */
    public boolean updateRole(String teacherId, int role) {
        if(null==teacherId || !ObjectId.isValid(teacherId)){
            return  false;
        }
        try {
            userDao.update(new ObjectId(teacherId),"r",role,true);
        } catch (IllegalParamException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

   
    
    
    /**
     * 查询薪资制表所有的老师
    * @Title: findTeatherBySchoolId 
    * @Description: TODO(这里用一句话描述这个方法的作用) 
    * @param @param schoolId
    * @param @return    设定文件 
    * @return List<UserInfoDTO>    返回类型 
    * @throws
     */
    public List<UserInfoDTO> findTeatherBySchoolId(String schoolId) {
        List<UserEntry> userEntryList = userDao.getTeacherEntryBySchoolId(new ObjectId(schoolId), Constant.FIELDS);
        List<UserInfoDTO> userInfoDTOList = new ArrayList<UserInfoDTO>();
        for (UserEntry userEntry : userEntryList) {
        	UserInfoDTO userInfoDTO = new UserInfoDTO(userEntry);
            userInfoDTOList.add(userInfoDTO);
        }
        return userInfoDTOList;
    }
    
    
    


    /**
     * 更新微校园时间
     * @param id
     */
    public void updSchoolHomeDate(ObjectId id) {
        userDao.updSchoolHomeDate(id);
    }

    /**
     * 更新微家园时间
     * @param id
     */
    public void updFamilyHomeDate(ObjectId id) {
        userDao.updFamilyHomeDate(id);
    }
    
    /**
     * 更新最后登录时间
     * @param ui
     * @throws IllegalParamException 
     */
    public void updateLastActiveDate(ObjectId ui) throws IllegalParamException
    {
    	userDao.update(ui, "lad", System.currentTimeMillis(), true);
    }
    
    
    /**
     * 更新用户字段
     * @param userId
     * @param field
     * @param value
     * @throws IllegalParamException
     */
    public void update(ObjectId userId,String field,Object value) throws IllegalParamException
    {
    	userDao.update(userId, field, value, false);
    }
    
    
    
    
    



    public List<UserEntry> findUserRoleInfoBySchoolIds(List<ObjectId> schoolIds) {
        List<UserEntry> userEntryList = userDao.findUserRoleInfoBySchoolIds(schoolIds);
        return userEntryList;
    }

    /*
   * 检查用户名是否重复
   *
   * */
    public boolean existUserInfo(String userName) {
        boolean k=userDao.existUserInfo(userName);
        return k;
    }

    /**
     * 根据学校id查询老师信息
     * @param schoolId
     * @return
     */
    public List<UserDetailInfoDTO> findTeacherInfoBySchoolId(String schoolId) {
        List<UserDetailInfoDTO> userInfoDTOList =new ArrayList<UserDetailInfoDTO>();
        
        List<UserEntry> list=getTeacherEntryBySchoolId(new ObjectId(schoolId),Constant.FIELDS);
        for(UserEntry ue:list)
        {
        	try
        	{
        	  userInfoDTOList.add(new UserDetailInfoDTO(ue));
        	}catch(Exception ex)
        	{
        	}
        }
        
   
        List<UserDetailInfoDTO> teacherList=new ArrayList<UserDetailInfoDTO>();
        for(UserDetailInfoDTO userInfo:userInfoDTOList){
            if(UserRole.isTeacher((int)userInfo.getRole())){
                teacherList.add(userInfo);
            }else if(UserRole.isHeadmaster((int) userInfo.getRole())){
                teacherList.add(userInfo);
            }else if(UserRole.isLeaderClass((int) userInfo.getRole())){
                teacherList.add(userInfo);
            }else if(UserRole.isLeaderOfGrade((int) userInfo.getRole())){
                teacherList.add(userInfo);
            }else if(UserRole.isLeaderOfSubject((int)userInfo.getRole())){
                teacherList.add(userInfo);
            }
        }
        return teacherList;
    }

    
    
    /**
     * 根据chatids查询
     * @param chatids
     * @param fields
     * @return
     */
    public List<UserEntry>  getUserEntrysByChatids(List<String> chatids,DBObject fields)
    {
    	return userDao.getUserEntrysByChatids(chatids, fields);
    }

    /**
     * 根据条件查询用户
     * @param role
     * @param noUserIds
     * @return
     */
    public List<UserInfoDTO> getUserListByParam(int role, String userName, Set<ObjectId> noUserIds) {
        List<UserEntry> userEntryList = userDao.getUserListByParam(role, userName, noUserIds);
        List<UserInfoDTO> dtolist = new ArrayList<UserInfoDTO>();
        for (UserEntry userEntry : userEntryList) {
            UserInfoDTO userInfoDTO = new UserInfoDTO(userEntry);
            dtolist.add(userInfoDTO);
        }
        return dtolist;
    }

    /**
     * k6kt增加经验值
     * @param name
     */
    public void updateExp(String name) {
        UserEntry user = userDao.getUserEntryByName(name);
        userDao.updateExperenceValue(user.getID());
    }


    /**
     * 根据用户名精确查询
     * @param page
     * @param pageSize
     * @return
     */
    public List<UserEntry> searchUser(String schoolId,String name,int jinyan,int page,int pageSize) {
        return userDao.searchUser(schoolId,name, jinyan, page < 1 ? 0 : ((page - 1) * pageSize), pageSize);
    }

    public int searchUserCount(String schoolId,String name,int jinyan) {
        return userDao.searchUserCount(schoolId,name,jinyan);
    }

    public List<UserDetailInfoDTO> getK6ktEntryByRoles() {
        List<UserEntry> userEntryList = userDao.getK6ktEntryByRoles(Constant.FIELDS);
        List<UserDetailInfoDTO> userInfoDTOs = new ArrayList<UserDetailInfoDTO>();
        List<ObjectId> schoolids = new ArrayList<ObjectId>();
        for (UserEntry user : userEntryList) {
            schoolids.add(user.getSchoolID());
        }
        Map<ObjectId, SchoolEntry> schoolEntryMap = schoolService.getSchoolEntryMap(schoolids);
        for (UserEntry user : userEntryList) {
            UserDetailInfoDTO userDetailInfoDTO = new UserDetailInfoDTO(user);
            userDetailInfoDTO.setSchoolName(schoolEntryMap.get(user.getSchoolID()).getName());
            userInfoDTOs.add(userDetailInfoDTO);
        }
        return userInfoDTOs;
    }
    public List<UserDetailInfoDTO> getK6ktEntryByRoles2() {
        List<UserEntry> userEntryList = userDao.getK6ktEntryByRoles2(Constant.FIELDS);
        List<UserDetailInfoDTO> userInfoDTOs = new ArrayList<UserDetailInfoDTO>();
        List<ObjectId> schoolids = new ArrayList<ObjectId>();
        for (UserEntry user : userEntryList) {
            schoolids.add(user.getSchoolID());
        }
        Map<ObjectId, SchoolEntry> schoolEntryMap = schoolService.getSchoolEntryMap(schoolids);
        for (UserEntry user : userEntryList) {
            UserDetailInfoDTO userDetailInfoDTO = new UserDetailInfoDTO(user);
            userDetailInfoDTO.setSchoolName(schoolEntryMap.get(user.getSchoolID()).getName());
            userInfoDTOs.add(userDetailInfoDTO);
        }
        return userInfoDTOs;
    }
    public ObjectId randomAnId(){
        List<ObjectId> userIdList = userDao.getUserForEBusiness(new ObjectId("55934c14f6f28b7261c19c62"), Constant.FIELDS);
        Random random = new Random();
        int j = random.nextInt(400);
        return userIdList.get(j);
    }

    public void updateJinYanTime(String userid) {
        userDao.updateJinYanTime(new ObjectId(userid));

    }
    
    
    /**
     * 通过并定用户名登录
     * @return
     */
    public UserEntry getUserEntryByBindName(String bindName,int Type)
    {
     return userDao.getUserEntryByBindName(bindName,Type);
    }
    
    /**
     * 得到
     * @param name
     * @return
     */
    public Map<String, RegionEntry> getRegionEntryMap()
    {
    	return regionDao.getRegionEntryMap(null);
    }

    /**
     * 新注册商城用户赠送购物券
     * */
    public void giveVouchers(ObjectId userId){
       // voucherService.addEVoucher(userId,1000);
       // voucherService.addEVoucher(userId,1000);
    }

    /**
     * 学生爸爸
     * */
    public String getFather(ObjectId stuId){
        UserEntry student = userDao.getUserEntry(stuId,Constant.FIELDS);
        List<ObjectId> parents = student.getConnectIds();
        if(parents.size() == 2){//新数据
            return parents.get(0).toString();
        }
        return parents.get(1).toString();
    }

    /**
     * 学生妈妈
     * */
    public String getMother(ObjectId stuId){
        UserEntry student = userDao.getUserEntry(stuId,Constant.FIELDS);
        List<ObjectId> parents = student.getConnectIds();
        if(parents.size() == 2){//新数据
            return parents.get(1).toString();
        }
        return parents.get(2).toString();
    }

//    public List<UserDetailInfoDTO> findUserHuanXin(ObjectId schoolId) {
//        List<UserEntry> userEntryList = userDao.getUserEntryHuanXin(schoolId, Constant.FIELDS);
//        List<UserDetailInfoDTO> userInfoDTOList = new ArrayList<UserDetailInfoDTO>();
//        for (UserEntry userEntry : userEntryList) {
//            UserDetailInfoDTO userInfoDTO = new UserDetailInfoDTO(userEntry);
//            userInfoDTOList.add(userInfoDTO);
//        }
//        return userInfoDTOList;
//    }

    
    
    /**
     * 通过学校id查询学校的老师和校长
     * @param schoolIds
     * @param fields
     * @return
     */
    public List<UserEntry> getTeacherEntryBySchoolIds(Collection<ObjectId> schoolIds, BasicDBObject fields) {
        return userDao.getTeacherEntryBySchoolIds(schoolIds, fields);
    }

    /**
     * 根据学校id查询班级中的老师、学生、家长的userId
     * @param schoolId
     * @return
     */
    public Map<String,List<ObjectId>> getRoleUserIdMapBySchoolId(String schoolId){
        Map<String,List<ObjectId>> uisMap=userDao.getTeacherIdMapBySchoolId(new ObjectId(schoolId),new BasicDBObject("r",1));
        return uisMap;
    }

    /**
     * 更新是否领优惠券
     * @param userId
     */
    public void updateUserCoupon(ObjectId userId) {
        userDao.updateUserCoupon(userId);
    }

    /**
     * 得到所有部门人员
     * @param schoolId
     * @return
     */
    public Map<IdNameValuePairDTO,Set<IdNameValuePairDTO>> getDepartmemtMembersMap2(ObjectId schoolId)
    {
        Map<IdNameValuePairDTO,Set<IdNameValuePairDTO>> retMap =new HashMap<IdNameValuePairDTO, Set<IdNameValuePairDTO>>();

        List<DepartmentEntry> depList=departmentDao.getDepartmentEntrys(schoolId);

        List<UserEntry> userList = userDao.getTeacherEntryBySchoolId(schoolId, "", new BasicDBObject("nm", 1));

        List<ObjectId> userids = new ArrayList<ObjectId>();
        for (UserEntry user : userList) {
            userids.add(user.getID());
        }
        for(DepartmentEntry ce:depList)
        {
            for(ObjectId stuId:ce.getMembers())
            {
                userids.remove(stuId);
            }
        }
        depList.add(new DepartmentEntry(schoolId,"未部门","",null,userids));
        Set<ObjectId> totalSet =new HashSet<ObjectId>();

        for(DepartmentEntry ce:depList)
        {
            IdNameValuePairDTO dto =new IdNameValuePairDTO(ce);
            if(!retMap.containsKey(dto))
            {
                retMap.put(dto, new HashSet<IdNameValuePairDTO>());
            }

            for(ObjectId stuId:ce.getMembers())
            {
                retMap.get(dto).add(new IdNameValuePairDTO(stuId));
                totalSet.add(stuId);
            }
        }

        Map<ObjectId, UserEntry> userMap=	userDao.getUserEntryMap(totalSet, new BasicDBObject().append("nm", 1).append("cid", 1)) ;
        UserEntry e;
        for(Map.Entry<IdNameValuePairDTO,Set<IdNameValuePairDTO>> entry:retMap.entrySet() )
        {
            for(IdNameValuePairDTO dto:entry.getValue())
            {
                e=userMap.get(dto.getId());
                if(null!=e)
                {
                    dto.setValue(e.getUserName());
                }
            }
        }
        return retMap;
    }
}
