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

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.jasig.cas.client.authentication.AttributePrincipal;
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
import com.fulaan.myschool.controller.UserManagerController;
import com.fulaan.school.service.SchoolService;
import com.fulaan.school.service.TeacherClassSubjectService;
import com.fulaan.user.service.UserService;
import com.pojo.app.SessionValue;
import com.pojo.school.Grade;
import com.pojo.school.SchoolEntry;
import com.pojo.school.SchoolType;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.UnBindException;
import com.sys.exceptions.UnLoginException;

@Controller
@RequestMapping("/yhuser")
public class YouhongUserController extends BaseController implements
		ApplicationContextAware {

	private static final Logger logger =Logger.getLogger(YouhongUserController.class);
	
	
	
	private UserService userService =new UserService();

	@Autowired
	private ClassService classService;

	@Autowired
	private SchoolService schoolService;

	@Resource
	private FriendService friendService;

	@Autowired
	private LogService logService;
	
	@Autowired
	private TeacherClassSubjectService teacherClassSubjectService;

	@Autowired
	private EducationBureauService educationBureauService;
	
	
	

	/**
	 * sso 回调登录
	 * @param request
	 * @param response
	 * @return
	 * @throws UnLoginException
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws IllegalParamException 
	 * @throws UnBindException 
	 * @throws ServletException 
	 */
	@SessionNeedless
	@RequestMapping("/sso/login")
	public String ssoLogin ( HttpServletRequest request,HttpServletResponse response) throws UnLoginException, ClientProtocolException, IOException, IllegalParamException, UnBindException, ServletException
	{
		
		String userId ="";
		String userName="" ;
		String realName ="";
		String roles="";
		String stages ="";
		String schoolName="";
		String orgid="";
		try
		{
		 AttributePrincipal attributePrincipal = (AttributePrincipal)request.getUserPrincipal();
		 Map <String,Object>map = attributePrincipal.getAttributes();
		 userId = String.valueOf(map.get("PERSONID"));
		 userName = String.valueOf(map.get("USER_NAME"));
		 realName = String.valueOf(map.get("REAL_NAME"));
		 roles = String.valueOf(map.get("ROLES"));
		 stages = String.valueOf(map.get("STAGES"));
		 schoolName=String.valueOf(map.get("ORG_NAME"));
		 orgid=String.valueOf(map.get("ORG_ID"));
		 logger.info("youhong++++++++++++++"+map);
		}catch(Exception ex)
		{
			logger.error("", ex);
			throw new UnLoginException();
		}
		
		
		if(StringUtils.isBlank(userName) || StringUtils.isBlank(realName) || StringUtils.isBlank(schoolName))
		{
			throw new UnLoginException();
		}
		
		
		UserEntry e=userService.getUserEntryByBindName(userName,2);
		
		
		/**
		 *  -------------自动创建用户逻辑--------------------
		 */
		if(null==e)
		{
			try {
				e= handleSSOLogin( userId, userName, realName, roles, stages, schoolName,orgid);
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
	
	
	
	public UserEntry handleSSOLogin(String userId,String userName,String realName,String roles,String stages,String schoolName,String orgid) throws Exception
	{
		SchoolEntry seEntry=schoolService.getSchoolEntry(schoolName.trim(), Constant.EMPTY);
		if(null==seEntry) //没有此学校
		{
			ObjectId regionId=new ObjectId("57d0d616f446bc626804e8a7"); //默认济南市
			int schoolType=SchoolType.JUNIOR.getType();
			if(stages.equals("00"))
			{
				schoolType=SchoolType.KINDERGARENER.getType();
			}
			if(stages.equals("01"))
			{
				schoolType=SchoolType.PRIMARY.getType();
			}
			if(stages.equals("02"))
			{
				schoolType=SchoolType.JUNIOR.getType();
			}
			if(stages.equals("03"))
			{
				schoolType=SchoolType.SENIOR.getType();
			}
			
			
			String time =String.valueOf(System.currentTimeMillis());
			String pwd=time.substring(time.length()-6);
			
			seEntry =new SchoolEntry(schoolType, schoolName.trim(), regionId, pwd);
			seEntry.setSchoolNavs(22);
			seEntry.setYhBindId(orgid);
			seEntry.setModuleStr("1,2,9,7,14,4,5,10,20");
			schoolService.addSchoolEntry(seEntry);
			
			logger.info("add school:"+seEntry);
		}
		
		int sex=1;
		
		
		
		int gradeCode=-1;

		
		//零年级、一年级~九年级、高一、高二、高三、已毕业
        
		String grade="";

		
		
		List<ExcelTeacherRecord> teachers =new ArrayList<ExcelTeacherRecord>();
		List<ExcelStudentRecord> students=new ArrayList<ExcelStudentRecord>();
		
		Object realUserName =realName;
		
		String dbName=(realUserName==org.json.JSONObject.NULL)?userName:realUserName.toString();
		
		
		if((roles.equals("0103") || roles.equals("0103,")) && roles.indexOf(0105)<=0) //学生
		{
			 ExcelStudentRecord excelStudentRecord=new ExcelStudentRecord();
             excelStudentRecord.setSex(sex);
             excelStudentRecord.setStudentName(dbName);
             students.add(excelStudentRecord);
		}
		else
		{
			ExcelTeacherRecord record =new ExcelTeacherRecord(dbName, "", sex, UserRole.TEACHER.getRole(), "");
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
        
         userService.update(uList.get(0), "yhbnm", userName);
         
         //UserEntry checkUser =getUserEntry(dbName);
         
         UserEntry ue=userService.searchUserId(uList.get(0));
         logger.info("youhong add user:"+ue);
         return ue;
	}
	
	
	private  ApplicationContext applicationContext;  
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext=applicationContext;
	}

	
}
