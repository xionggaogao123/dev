package com.fulaan.user.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fulaan.annotation.SessionNeedless;
import com.fulaan.base.controller.BaseController;
import com.fulaan.cache.CacheHandler;
import com.fulaan.educationbureau.service.EducationBureauService;
import com.fulaan.friendscircle.service.FriendService;
import com.fulaan.log.service.LogService;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.controller.ExcelStudentRecord;
import com.fulaan.myschool.controller.ExcelTeacherRecord;
import com.fulaan.myschool.controller.ExcelTeacherRecord.GradeAndClass;
import com.fulaan.myschool.controller.UserManagerController;
import com.fulaan.school.service.SchoolService;
import com.fulaan.school.service.TeacherClassSubjectService;
import com.fulaan.user.service.UserService;
import com.pojo.app.SessionValue;
import com.pojo.school.Grade;
import com.pojo.school.SchoolEntry;
import com.pojo.school.SchoolType;
import com.pojo.user.UserEntry;
import com.pojo.user.UserEntry.Binds;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.UnBindException;
import com.sys.exceptions.UnLoginException;
import com.sys.utils.HttpClientUtils;

/**
 * 高新一中sso
 * @author fourer
 *
 */
@Controller
@RequestMapping("/gxschool")
public class GaoxinUserController extends BaseController implements
		ApplicationContextAware {

	private static final Logger logger =Logger.getLogger(GaoxinUserController.class);
	
	
	
	private UserService userService =new UserService();

	@Autowired
	private ClassService classService;

	private SchoolService schoolService=new SchoolService();

	@Resource
	private FriendService friendService;

	@Autowired
	private LogService logService;
	
	@Autowired
	private TeacherClassSubjectService teacherClassSubjectService;

	@Autowired
	private EducationBureauService educationBureauService;
	
	
	


	/**
	 * 
	 * @param request
	 * @param response
	 * @param code 回调code
	 * @return
	 * @throws UnLoginException
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws IllegalParamException
	 * @throws UnBindException
	 * @throws ServletException
	 * @throws JSONException
	 */
	@SessionNeedless
	@RequestMapping("/sso/login")
	public String ssoLogin ( HttpServletRequest request,HttpServletResponse response,String code) throws UnLoginException, ClientProtocolException, IOException, IllegalParamException, UnBindException, ServletException, JSONException
	{
		
		Map<String,String> paramMap =new HashMap<String, String>();
		paramMap.put("code", code);
		String token=HttpClientUtils.post("http://www.hschool.cc/oauth/token?client_id=fulan&client_secret=fulanhongschool&grant_type=authorization_code&redirect_uri=http://www.jngxqdyzx.com:8081/gxschool/sso/login.do", paramMap);
		logger.info("gaoxin token:"+token);
		JSONObject tokenJSON=new JSONObject(token);
		
		String tokenValue =tokenJSON.getString("access_token");
		
		String curUser=HttpClientUtils.get("http://www.hschool.cc/api/account",tokenValue);
		JSONObject currentUserJSON=new JSONObject(curUser);
		logger.info("currentUserJSON:"+curUser);
		String loginName=currentUserJSON.getString("login");
		
		int id=   currentUserJSON.getInt("id");
		
		int schoolId =currentUserJSON.getInt("unitId");
		
		String schoolName =currentUserJSON.getString("unitName");
		
		String realName=currentUserJSON.getString("name");
		String userTypes =currentUserJSON.getJSONArray("userTypes").join(",");
		
		UserEntry e=userService.searchUserByUserBind(3,loginName);
		

		/**
		 *  -------------自动创建用户逻辑--------------------
		 */
		
		if(null==e)
		{
			try {
				e= handleSSOLogin( id,loginName ,userTypes, tokenValue,schoolId,schoolName);
			} catch (Exception e1) {
				throw new UnLoginException(e1.getLocalizedMessage());
			}
		}

		if(null==e) //没有绑定用户
		{
			throw new  UnBindException("un bind k6kt user!!!");
		}

		try
		{
			if(0l==e.getLastActiveDate()){
				String params=e.getID().toString()+0l;
				String flkey= CacheHandler.getKeyString(CacheHandler.CACHE_USER_FIRST_LOGIN,params);
				CacheHandler.cache(flkey,Constant.USER_FIRST_LOGIN, Constant.SESSION_FIVE_MINUTE);
			}
			//更新最后登录日期
			userService.updateLastActiveDate(e.getID());
		}catch(Exception ex)
		{
			logger.error("",ex);
		}
		
		
		if(Constant.ONE==e.getUserType()) //有效时间用户 
		{
			long validBeginTime =e.getValidBeginTime();
			long validTime=e.getValidTime();
			if(0L==validBeginTime) //第一次登陆
			{
				try {
					userService.update(e.getID(), "vabt", System.currentTimeMillis());
				} catch (IllegalParamException e1) {
					
				};
			}
			else
			{
				if(System.currentTimeMillis()>validBeginTime+validTime*1000)
				{
					throw new UnLoginException();
				}
			}
		}
		
		SchoolEntry schoolEntry = null;
		try {
			schoolEntry = schoolService.getSchoolEntryByUserId(e.getID());
		}
		catch (IllegalParamException ie){
			logger.error("Can not find school for user:"+e);
		}

	
		//处理SessionValue
		SessionValue value =new SessionValue();
		value.setId(e.getID().toString());
		value.setUserName(e.getUserName());
		value.setRealName(e.getNickName());
		value.setK6kt(e.getK6KT());
		value.setSso();
		if(e.getK6KT() == 1) {//k6kt用户
			value.setSchoolId(e.getSchoolID().toString());
			if (schoolEntry != null && schoolEntry.getLogo() != null) {
				value.setSchoolLogo(schoolEntry.getLogo());
			}

			value.setUserRole(e.getRole());
			value.setAvatar(e.getAvatar());
			value.setUserPermission(e.getPermission());
			value.setUserRemovePermission(e.getRemovePermission());
			value.setExperience(e.getExperiencevalue());
			value.setChatid(e.getChatId());
			value.setSchoolNavs(schoolEntry.getSchoolNavs());
			value.setSchoolName(schoolEntry.getName());
		}
		//放入缓存
		ObjectId cacheUserKey =new ObjectId();
		//ck_key
		CacheHandler.cacheUserKey(e.getID().toString(), cacheUserKey.toString(), Constant.SECONDS_IN_DAY);
		//s_key
		CacheHandler.cacheSessionValue(cacheUserKey.toString(), value, Constant.SECONDS_IN_DAY);
		//处理cookie
		Cookie userKeycookie = new Cookie(Constant.COOKIE_USER_KEY,cacheUserKey.toString());
		userKeycookie.setMaxAge(Constant.SECONDS_IN_DAY);
		userKeycookie.setPath(Constant.BASE_PATH);
		response.addCookie(userKeycookie);

		request.setAttribute(BaseController.SESSION_VALUE, value);

		try {
			Cookie nameCookie = new Cookie(Constant.COOKIE_USERNAME_KEY,URLEncoder.encode(realName, Constant.UTF_8));
			nameCookie.setMaxAge(Constant.SECONDS_IN_MONTH);
			nameCookie.setPath(Constant.BASE_PATH);
			response.addCookie(nameCookie);
		} catch (UnsupportedEncodingException e1) {
			logger.error("", e1);
		}
		
		return "redirect:/user/homepage.do";
	}
	
	
	/**
	 * 
	 * @param userId 用户ID
	 * @param userLoginName 用户登录名
	 * @param schoolId 学校ID
	 * @return
	 * @throws Exception
	 */
	public UserEntry handleSSOLogin(int userId,String userLoginName,String userTypes, String token,int schoolId,String schName ) throws Exception
	{
		
		//先查找绑定数据
		SchoolEntry seEntry=schoolService.getSchoolEntryByBind(3, String.valueOf(schoolId));
		if(null==seEntry)
		{
			seEntry=schoolService.getSchoolEntry(schName.trim(), Constant.EMPTY);
			if(null!=seEntry && !seEntry.getRegionId().equals(new ObjectId("57d0d616f446bc626804e8a7")))
			{
				seEntry=null;
			}
		}
		
		int schoolType=SchoolType.JUNIOR.getType();
		if(null==seEntry) //没有此学校
		{
			
			String schoolInfo=HttpClientUtils.get("http://www.hschool.cc/api/unit/"+schoolId,token);
			JSONObject schoolInfoJSON=new JSONObject(schoolInfo);
			
			logger.info("schoolInfoJSON:"+schoolInfo);
			
			String schoolName=schoolInfoJSON.getString("name");
			
			JSONArray schoolTypeArr=schoolInfoJSON.getJSONArray("schoolType");
			
			String schoolTypeArrStr=schoolTypeArr.join(",");
			
			ObjectId regionId=new ObjectId("57d0d616f446bc626804e8a7"); //默认济南市
			
			if(schoolTypeArrStr.indexOf("Kindergarten")>=0)
			{
				schoolType+=SchoolType.KINDERGARENER.getType();
			}
			if(schoolTypeArrStr.indexOf("Primary")>=0)
			{
				schoolType+=SchoolType.PRIMARY.getType();
			}
			if(schoolTypeArrStr.indexOf("Junior")>=0)
			{
				schoolType+=SchoolType.JUNIOR.getType();
			}
			if(schoolTypeArrStr.indexOf("Senior")>=0)
			{
				schoolType+=SchoolType.SENIOR.getType();
			}
			
			
			String time =String.valueOf(System.currentTimeMillis());
			String pwd=time.substring(time.length()-6);
			
			seEntry =new SchoolEntry(schoolType, schoolName.trim(), regionId, pwd);
			seEntry.setSchoolNavs(22);
			
			Binds us =new Binds(3, String.valueOf(schoolId));
			
			seEntry.setUserBind(us);
			seEntry.setSchoolNavs(26);
			seEntry.setModuleStr("1,2,3,4,5,7,9,10,11,14,20,23");
			schoolService.addSchoolEntry(seEntry);
			
			logger.info("add school:"+seEntry);
		}
		else
		{
			schoolType=seEntry.getSchoolType();
		}
		
		boolean isStudent=false;
		
		if(userTypes.indexOf("Student")>=0)
		{
			isStudent=true;
		}
		
		int sex=1;
		
		int gradeCode=-1;
		
		//零年级、一年级~九年级、高一、高二、高三、已毕业
		String grade="";

		List<ExcelTeacherRecord> teachers =new ArrayList<ExcelTeacherRecord>();
		List<ExcelStudentRecord> students=new ArrayList<ExcelStudentRecord>();
		
		
		if(isStudent) //学生
		{
			
			 String studentInfourl="http://www.hschool.cc/api/student/"+userId;
			
			 String studentInfo=HttpClientUtils.get(studentInfourl,token);
				
			JSONObject studentJSON=new JSONObject(studentInfo);
				
			logger.info("studentJSON:"+studentInfo);
			String gender=studentJSON.getString("gender");
				
			if(gender.equals("Female"))
			{
					sex=0;
			}
				
			String userName=studentJSON.getString("name");
			int gradeNumber=studentJSON.getInt("gradeNumber");
			gradeCode=getGradeCode(schoolType,gradeNumber);
			String className =studentJSON.getString("className");
			ExcelStudentRecord excelStudentRecord=new ExcelStudentRecord();
            excelStudentRecord.setSex(sex);
            excelStudentRecord.setStudentName(userName);
            excelStudentRecord.setGradeCode(gradeCode);
            excelStudentRecord.setClassName(className);
            students.add(excelStudentRecord);
		}
		else
		{
			
			 String studentInfourl="http://www.hschool.cc/api/teacher/"+userId;
				
			 String teacherInfo=HttpClientUtils.get(studentInfourl,token);
			
			 JSONObject teacherJSON=new JSONObject(teacherInfo);
			
			 logger.info("teacherJSON:"+teacherInfo);
				
			 String gender=teacherJSON.getString("gender");
				
			if(gender.endsWith("Female"))
			{
					sex=0;
			}
				
			String userName=teacherJSON.getString("name");
				
			
			ExcelTeacherRecord record =new ExcelTeacherRecord(userName, "", sex, UserRole.TEACHER.getRole(), "");
			//添加教学班级
			String classInfourl="http://www.hschool.cc/api/teach/class/teacher";
			String classInfo=HttpClientUtils.get(classInfourl,token);
			
			
			JSONArray jsonArray = new JSONArray(classInfo);
			
			logger.info("jsonArray:"+classInfo);
			for(int i=0;i<jsonArray.length();i++)
			{
				try
				{
					JSONObject json=jsonArray.getJSONObject(i);
					String subjectName=json.getString("subjectName");
					GradeAndClass gac=new GradeAndClass(subjectName, "", -1, "", false, false, false, false);
					System.out.println(json);
					record.addGradeAndClass(gac);
				}catch(Exception ex)
				{
					
				}
			}
			teachers.add(record);
		}
		
		
		 Map<String,Object> returnMap=new HashMap<String, Object>();
		 returnMap.put("teacherList",teachers);
         returnMap.put("studentList",students);
         
         UserManagerController uc= (UserManagerController)applicationContext.getBean("userManagerController");
 		 //确保学校下有该年级
 		 if(gradeCode>-1)
 		 {
 			 Grade g=uc.isContainGrade(seEntry.getGradeList(), gradeCode);
 			 if(null==g) //增加年级
 			 {
 				  g =new Grade(grade,gradeCode, null,new ObjectId());
	  			  schoolService.addGrade(seEntry.getID().toString(), g);
 			 }
 			seEntry=schoolService.getSchoolEntry(seEntry.getID(), Constant.FIELDS);
 			 
 		 }
         List<ObjectId> uList =new ArrayList<ObjectId>();
         uc.handleExcelMapData(returnMap,seEntry,uList);
         UserEntry.Binds userBind =new UserEntry.Binds(3, userLoginName);
         userService.update(uList.get(0), "binds", userBind.getBaseEntry());
         UserEntry ue=userService.searchUserId(uList.get(0));
         logger.info("youhong add user:"+ue);
         return ue;
	}
	
	
	private int getGradeCode(int schoolType,int gradeNumber)
	{
		if(schoolType==2)
		{
			return gradeNumber;
		}
		if(schoolType==4)
		{
			return 6+gradeNumber;
		}
		if(schoolType==8)
		{
			return 9+gradeNumber;
		}
		
		if(schoolType==6)
		{
			return gradeNumber;
		}
		
		if(schoolType==12)
		{
			return 6+gradeNumber;
		}
		
		return -1;
	}
	
	

	
	private  ApplicationContext applicationContext;  
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext=applicationContext;
	}

	
}
