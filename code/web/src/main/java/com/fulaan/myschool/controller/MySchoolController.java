package com.fulaan.myschool.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.base.service.DirService;
import com.fulaan.department.service.DepartmentService;
import com.fulaan.educationbureau.dto.EducationBureauDTO;
import com.fulaan.groupdiscussion.service.EaseMobService;
import com.fulaan.interestCategory.service.InterestClassTermsService;
import com.fulaan.learningcenter.service.LeagueService;
import com.fulaan.managecount.service.ManageCountService;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.service.CampusService;
import com.fulaan.myschool.service.InterestClassService;
import com.fulaan.myschool.service.NewsService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.school.service.TeacherClassSubjectService;
import com.fulaan.teacher.service.TeacherService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.PinYin2Abbreviation;
import com.fulaan.utils.QiniuFileUtils;
import com.fulaan.utils.pojo.KeyValue;
import com.mongodb.BasicDBObject;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.app.IdValuePairDTO;
import com.pojo.app.NameValuePair;
import com.pojo.app.SessionValue;
import com.pojo.app.SimpleDTO;
import com.pojo.interestCategory.InterestClassTermsDTO;
import com.pojo.lesson.DirDTO;
import com.pojo.lesson.DirEntry;
import com.pojo.lesson.DirType;
import com.pojo.news.News;
import com.pojo.news.NewsColumn;
import com.pojo.news.NewsEntry;
import com.pojo.school.*;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.user.UserInfoDTO;
import com.pojo.user.UserRole;
import com.pojo.utils.MongoUtils;
import com.sys.constants.Constant;
import com.sys.exceptions.FileUploadException;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.PermissionUnallowedException;
import com.sys.utils.AvatarUtils;
import com.sys.utils.FileUtil;
import com.sys.utils.MD5Utils;
import com.sys.utils.RespObj;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * written by hao
 */
@Controller
@RequestMapping("/myschool")
public class MySchoolController extends BaseController {
	private static final org.apache.log4j.Logger logger= Logger.getLogger(MySchoolController.class);
    @Resource
    private UserService userService;
    @Resource
    private SchoolService schoolService;
    @Resource
    private TeacherService teacherService;
    @Resource
    private NewsService newsService;
    @Resource
    private ClassService classService;
    @Resource
    private InterestClassService interestClassService;
    @Resource
    private TeacherClassSubjectService teacherClassSubjectService;
    @Resource
    private DirService dirService;
    @Resource
    private LeagueService leagueService;
    @Resource
    private ManageCountService manageCountService;
    @Resource
    private CampusService campusService;
    @Resource
    private DepartmentService departmentService;

    @Autowired
    private EaseMobService easeMobService;
    @Autowired
    private InterestClassTermsService interestClassTermsService;



    /**
     * 如果是家长登陆，则返回相应地学生信息
     * @return
     */
    private UserDetailInfoDTO findRealUserInfo() {
        String userId = getUserId().toString();
        UserDetailInfoDTO userInfoDTO4WB = userService.getUserInfoById(userId);
        if (userInfoDTO4WB == null) {
            try {
                throw new Exception("找不到当前用户信息，用户id非法");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (userInfoDTO4WB.getRole() == UserRole.PARENT.getRole()) {
            userInfoDTO4WB = userService.findStuInfoByParentId(userId);
        }
        return userInfoDTO4WB;
    }

    /**
     * 奖惩
     * @return
     */
    @RequestMapping("/jiangcheng")
    public String managerJiangcheng()
    {
    	 return "myschool/jiangcheng";
    }

    /**
     * 获取学校的年级列表和科目列表
     * @param model
     * @return
     */
    @RequestMapping("/masterclass")
    public String class4HeadMaster(Model model) {
        //获取科目列表
        SchoolDTO schoolDTO = schoolService.findSchoolById(getSessionValue().getSchoolId());
        List<Subject> subjects = schoolDTO.getSubjectList();
        model.addAttribute("gradeList", schoolDTO.getGradeList());
        model.addAttribute("subjectList", subjects);
        return "myschool/class";
    }

    
    @RequestMapping("/teacher")
    public String teacherHref(Model model) {
        //获取科目列表
        SchoolDTO schoolDTO = schoolService.findSchoolById(getSessionValue().getSchoolId());
        List<Subject> subjects = schoolDTO.getSubjectList();
        model.addAttribute("gradeList", schoolDTO.getGradeList());
        model.addAttribute("subjectList", subjects);
        return "myschool/teacher";
    }

    /**
     * 查找年级的普通班和兴趣班
     * @param gradeId
     * @return
     */
    @RequestMapping("/findclass")
    @ResponseBody
    public Map<String, Object> findClassByGradeId(String gradeId) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        if (null==gradeId || !ObjectId.isValid(gradeId)) {
            List<ClassInfoDTO> classInfoDTOList = classService.findClassInfoBySchoolId(getSessionValue().getSchoolId());
            List<InterestClassDTO> interestClassDTOList = interestClassService.findExpandClassBySchoolId(getSessionValue().getSchoolId(), null, -1);
            returnMap.put("commonClass", classInfoDTOList);
            returnMap.put("interestClass", interestClassDTOList);
        } else {
            List<ClassInfoDTO> classInfoDTOList = classService.findClassByGradeId(gradeId);
            List<InterestClassDTO> interestClassDTOList = interestClassService.findInterestClassByGradeId(gradeId);
            returnMap.put("commonClass", classInfoDTOList);
            returnMap.put("interestClass", interestClassDTOList);
        }
        return returnMap;
    }

    @RequestMapping("/classcourse")
    public String classCourse() {
        return "myschool/class-course-school";
    }

    @RequestMapping("/classsubject")
    public String classSubject(@RequestParam String classId, Model model) {
        if (null!=classId && ObjectId.isValid(classId)) {
            ClassInfoDTO classInfoDTO = classService.findClassInfoByClassId(classId);
            InterestClassDTO interestClassDTO = null;
            if (classInfoDTO == null) {
                interestClassDTO = interestClassService.findInterestClassByClassId(classId);
            }
            List<ObjectId> classIdList = new ArrayList<ObjectId>();
            classIdList.add(new ObjectId(classId));
            List<TeacherClassSubjectDTO> tcsList = teacherClassSubjectService.findTeacherClassSubjectByClassIds(classIdList);

            model.addAttribute("classInfo", classInfoDTO);
            model.addAttribute("interestClassInfo", interestClassDTO);
            model.addAttribute("classSubjects", tcsList);
        }
        return "myschool/subject-of-class";
    }


    @RequestMapping("/teacherdetail")
    public String teacherLessonDetail(@RequestParam ObjectId teacherId,Model model) {
        List<DirDTO> list = courseOfBack(teacherId, DirType.BACK_UP);
        
        String rootDirId="";
        if(null!=list && list.size()>0)
        {
        	rootDirId=list.get(0).getId();
        }
        model.addAttribute("rootDirId",rootDirId);
        return "myschool/teacher-course-school";
    }

    /*
    *
    * 老师备课空间调用
    *
    * */
    @RequestMapping("/backdir")
    @ResponseBody
    public List<DirDTO> courseOfBack(@RequestParam ObjectId userId, DirType type) {
        List<DirDTO> retList = new ArrayList<DirDTO>();
        List<ObjectId> ownerList = new ArrayList<ObjectId>();
        ObjectId ui = null == userId ? getUserId() : userId;
        ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());

        List<TeacherClassSubjectDTO> tlist = null;

        switch (type) {
            case BACK_UP:
                ownerList.add(ui);
                break;
            case CLASS_LESSON: {
                tlist = teacherClassSubjectService.getTeacherClassSubjectDTOList(ui, null);
                for (TeacherClassSubjectDTO dto : tlist) {
                    ownerList.add(new ObjectId(dto.getId()));
                }
                break;
            }
            case SCHOOL_RESOURCE:
                ownerList.add(schoolId);
                break;
            case UNION_RESOURCE:
                List<LeagueEnrty> list = leagueService.getLeagueEnrtys(schoolId, new BasicDBObject(Constant.ID, 1));
                ownerList.addAll(MongoUtils.getFieldObjectIDs(list, Constant.ID));
                break;
		default:
			break;
        }

        List<DirEntry> dirList = dirService.getDirEntryList(ownerList, Constant.FIELDS, type.getType());

        for (DirEntry dire : dirList) {
            DirDTO dirDTO = new DirDTO(dire);
            retList.add(dirDTO);
        }
        return retList;
    }

    @RequestMapping("/subjectdir")
    @ResponseBody
    public List<Map<String, Object>> courseDirOfClass(@RequestParam String classId) {
        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
        List<ObjectId> classIdList = new ArrayList<ObjectId>();
        classIdList.add(new ObjectId(classId));
        List<TeacherClassSubjectDTO> tcsList = teacherClassSubjectService.findTeacherClassSubjectByClassIds(classIdList);

        Map<ObjectId, Object> subjectMap = new HashMap<ObjectId, Object>();
        Map<ObjectId, List<ObjectId>> tcsIds = new HashMap<ObjectId, List<ObjectId>>();

        for (TeacherClassSubjectDTO teacherClassSubjectDTO : tcsList) {
            IdValuePairDTO subjectInfo = teacherClassSubjectDTO.getSubjectInfo();
            subjectMap.put(subjectInfo.getId(), subjectInfo);

            List<ObjectId> oids = tcsIds.get(subjectInfo.getId());
            if (oids == null)
                oids = new ArrayList<ObjectId>();
            oids.add(new ObjectId(teacherClassSubjectDTO.getId()));
            tcsIds.put(subjectInfo.getId(), oids);
        }

        for (ObjectId subjectId : subjectMap.keySet()) {
            List<ObjectId> tcsIdList = tcsIds.get(subjectId);
            if (tcsIdList != null) {
                Map<String, Object> innerMap = new HashMap<String, Object>();
                List<DirEntry> dirs = dirService.getDirEntryList(tcsIdList, Constant.FIELDS, -1);
                List<DirView> dirViewList = new ArrayList<DirView>();
                if (dirs != null) {
                    for (DirEntry dirEntry : dirs) {
                        DirView dirView = new DirView();
                        dirView.setId(dirEntry.getID().toString());
                        dirView.setName(dirEntry.getDirName());
                        ObjectId parentId = dirEntry.getParentId();
                        if (parentId != null) {
                            dirView.setParent(parentId.toString());
                        }
                        dirViewList.add(dirView);
                    }
                }
                innerMap.put("classSubject", subjectMap.get(subjectId));
                innerMap.put("dirs", dirViewList);
                returnList.add(innerMap);
            }
        }
        return returnList;
    }

    //=======================================我的同事====================================================================
    /**
    * 我的同事  仅针对老师
    *
    * */
    @RequestMapping("/mycoll")
    public String myColleague(Model model) {
        UserDetailInfoDTO userInfoDTO4WB = findRealUserInfo();
        //获取科目列表
        SchoolDTO schoolDTO = schoolService.findSchoolById(userInfoDTO4WB.getSchoolID());
        List<Subject> subjects = schoolDTO.getSubjectList();
        Map<String, String> map = new HashMap<String, String>();
        //获取年级列表
        for (Subject subject : subjects) {
            map.put(subject.getSubjectId().toString(), subject.getName());
        }
        model.addAttribute("gradeList", schoolDTO.getGradeList());
        model.addAttribute("subjectList", subjects);
        return "myschool/myColleagues";
    }

    /**
    * 同事列表
    * 仅针对老师
    * 根据页面条件 抽取出符合条件的老师
    * */
    @RequestMapping("/selteacher")
    @ResponseBody
    public Map<String, Object> selectTeacher(@RequestParam String gradeId, String subjectId) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        List<UserDetailInfoDTO> userInfoDTOList = null;
        if (!ObjectId.isValid(gradeId) && !ObjectId.isValid(subjectId)) {
            userInfoDTOList = teacherService.findTeacherOfSchool(getSessionValue().getSchoolId());
        } else {
            userInfoDTOList = teacherService.findTeacherInNeed(gradeId, subjectId);
        }
        returnMap.put("rows", userInfoDTOList);
        return returnMap;
    }

    //===============================学科管理============================================================================

    @RequestMapping("/managesubject")
    public String manageSubject() {
        return "myschool/manage-subject";
    }


    /*
    *  subjectList of school
    *
    * */
    @RequestMapping("/sublist")
    @ResponseBody
    public Map<String, Object> subjectList() {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        List<SubjectView> subjectList = schoolService.findSubjectList(getSchoolId().toString());
        returnMap.put("rows", subjectList);
        return returnMap;
    }

    /*
    * gradeList of school
    *
    * */
    @RequestMapping("/gradelist")
    @ResponseBody
    public Map<String, Object> gradeList() {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        List<GradeView> idValuePairList = schoolService.findGradeList(getSessionValue().getSchoolId());
        returnMap.put("rows", idValuePairList);
        return returnMap;
    }

    /**
     * 教育局使用
     * @param schoolId
     * @return
     */
    @RequestMapping("/gradelist/edu")
    @ResponseBody
    public Map<String, Object> gradeListEdu(@RequestParam(required = false, defaultValue = "") String schoolId) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        if(schoolId.equals("")){
            schoolId = getSessionValue().getSchoolId();
        }
        List<GradeView> idValuePairList = schoolService.findGradeList(schoolId);
        returnMap.put("rows", idValuePairList);
        return returnMap;
    }

    /**
     * 根据科目id返回科目信息
     * @param subjectId
     * @return
     */
    @RequestMapping("/findsub")
    @ResponseBody
    public SubjectView findSubject(@RequestParam String subjectId) {
        SubjectView subjectView = schoolService.findSubjectBySchoolIdAndSubId(getSessionValue().getSchoolId(), subjectId);
        return subjectView;
    }

    /**
     * 更新学科名字和拥有此学科的年级列表
     * @param subjectId
     * @param newSubjectName
     * @param gradeArray
     * @return
     */
    @RequestMapping("/upsub")
    @ResponseBody
    public boolean updateSubjectName(@RequestParam String subjectId,
                                     @RequestParam String newSubjectName,
                                     @RequestParam String gradeArray,
                                     @RequestParam String subjectTeacher) {
        boolean k = schoolService.updateSubjectNameAndGrade(subjectId, getSessionValue().getSchoolId(), newSubjectName, gradeArray,subjectTeacher);
        return k;
    }

    /**
     * 添加一个学科
     * @param subjectName
     * @param gradeArray
     * @return
     */
    @RequestMapping("/addsub")
    @ResponseBody
    public boolean addSubject(@RequestParam String subjectName, @RequestParam String gradeArray,@RequestParam String subjectTeacher) {
        boolean k = schoolService.addSubject2School(getSessionValue().getSchoolId(), gradeArray, subjectName,subjectTeacher);
        return k;
    }

    /**
     * 删除一个学科
     * @param subjectId
     * @return
     */
    @RequestMapping("/delsub")
    @ResponseBody
    public Map<String,Object> deleteSubject(@RequestParam String subjectId) {
        Map<String,Object> map=schoolService.deleteSubject(getSessionValue().getSchoolId(), subjectId);
        return map;
    }

//==========================================管理老师=====================================================
    /*
    *
    *老师管理
    *
    * */
    @RequestMapping("/manageteacher")
    public String mangeTeacher() {
        return "myschool/manage-teacher";
    }

    /**
     * 添加老师
     * @param teacherName
     * @param jobNum
     * @param permission
     * @return
     */
    @RequestMapping("/addteacher")
    @ResponseBody
    public boolean addTeacher(@RequestParam String teacherName,
                              @RequestParam String jobNum,
                              @RequestParam String permission) {
        String chatid = schoolService.addTeacher(getSessionValue().getSchoolId(), teacherName, jobNum, permission);
        boolean k = true;
        if (StringUtils.isEmpty(chatid)) {
            k = false;
        } else  {
            easeMobService.createNewUser(chatid);
        }
        return k;
    }

    /**
     * 编辑时的回显老师信息
     * @param teacherId
     * @return
     */
    @RequestMapping("/findteacher")
    @ResponseBody
    public UserDetailInfoDTO findTeacherInfo(@RequestParam String teacherId) {
        UserDetailInfoDTO userInfoDTO4WB = userService.getUserInfoById(teacherId);
        return userInfoDTO4WB;
    }

    /**
     * 更新老师信息
     * @param teacherId
     * @param teacherName
     * @param jobNum
     * @param permission
     * @param isManage
     * @return
     */
    @RequestMapping("/upteach")
    @ResponseBody
    public boolean updateTeacherInfo(@RequestParam String teacherId,
                                     @RequestParam String teacherName,
                                     @RequestParam String jobNum,
                                     @RequestParam String permission,
                                     @RequestParam String isManage) {
        int per = Integer.parseInt(permission);
        if ("1".equals(isManage)) {
            //只有老师可以附带管理员功能
            if (UserRole.isTeacher(per)) {
                permission = per + UserRole.ADMIN.getRole() + "";
            }
        }
        boolean k = schoolService.updateTeacher(teacherId, teacherName, jobNum, permission);
        return k;
    }

    /**
     * 删除老师
     * @param teacherId
     * @return
     */
    @RequestMapping("/delteach")
    @ResponseBody
    public boolean deleteTeacher(@RequestParam String teacherId) {
        if (null== teacherId ||      !ObjectId.isValid(teacherId)) {
            return false;
        }
    	logger.info("DELETEUSER:"+getUserId());
    	logger.info("studentId:"+teacherId);
        userService.deleteUser(new ObjectId(teacherId));
        return true;
    }

    /**
     * 老师列表
     * @param keyWord
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping("/teacherlist")
    @ResponseBody
    public Map<String, Object> teacherList(String keyWord, Integer page, Integer pageSize) {
        page = page == null ? 1 : page;
        pageSize = pageSize == null ? 30 : pageSize;

        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 30;
        int skip = (page - 1) * pageSize;
        List<UserDetailInfoDTO> userInfoDTOList = schoolService.teacherList(getSessionValue().getSchoolId(), keyWord, skip, pageSize);
        int total = schoolService.countTeacher(getSessionValue().getSchoolId(), keyWord);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("page", page);
        map.put("pageSize", pageSize);
        map.put("total", total);
        map.put("rows", userInfoDTOList);
        return map;
    }

    /**
     * 初始化老师密码
     * @param userId
     * @return
     */
    @RequestMapping("/initpwd")
    @ResponseBody
    public boolean initPwdOfTeacher(@RequestParam String userId,int type) {
    	
    	logger.info("initPwdOfTeacher"+getUserId());
        boolean k = true;
        if (type==0) {
            UserDetailInfoDTO userInfoDTO4WB = userService.getUserInfoById(userId);
            if (userInfoDTO4WB.getConnectIds()!=null && userInfoDTO4WB.getConnectIds().size()!=0) {
                for (String userid : userInfoDTO4WB.getConnectIds()) {
                    k = schoolService.initPwdOfTeacher(userid, getSessionValue().getSchoolId());
                }
            }
        } else {
            k = schoolService.initPwdOfTeacher(userId, getSessionValue().getSchoolId());
        }
        return k;
    }

    /**
     * 新建老师时检查用户名
     * @param userName
     * @return
     */
    @RequestMapping("/checkUserName")
    @ResponseBody
    public Map<String, Object> checkUserName(@RequestParam String userName) {
        //List<UserDetailInfoDTO> userDetailInfoDTOList = userService.findUserInfoByUserName(userName);
        SchoolDTO schoolDTO = schoolService.findSchoolById(getSessionValue().getSchoolId());
        Map<String, Object> returnMap = new HashMap<String, Object>();

        Map<String,Object> result=checkAndGetName(userName);
        returnMap.put("userName", result.get("userName"));
        returnMap.put("resultCode", result.get("resultCode"));
        returnMap.put("pwd", schoolDTO.getInitPwd());
        return returnMap;
    }

    /**
     * 检查核对教师用户名是否已经存在
     * @param userName
     * @return
     */
    public Map<String,Object> checkAndGetName(String userName)
    {
        Map<String,Object> map=new HashMap<String, Object>();
        List<UserDetailInfoDTO> userDetailInfoDTOList = userService.findUserInfoByUserName(userName);
        if (userDetailInfoDTOList != null && !userDetailInfoDTOList.isEmpty()) {
            UserDetailInfoDTO userDetailInfoDTO = userDetailInfoDTOList.get(userDetailInfoDTOList.size() - 1);
            String name = userDetailInfoDTO.getUserName();
            boolean nameHasDigit = false;
            String nameCount="";
            String nameBase=name;
            for(int i=name.length()-1;i>0;i--)
            {
                char ch=name.charAt(i);
                if(Character.isDigit(ch))
                {
                    nameCount=ch+nameCount;
                    nameHasDigit = true;
                    nameBase=name.substring(0,i);
                }
                else
                {
                    break;
                }
            }
            if(nameHasDigit) {
                name = nameBase + checkIfValid(Integer.toString(Integer.parseInt(nameCount) + 1));
            }
            else{
                name = name + "1";
            }
            //return checkAndGetName(name);
            map.put("userName",checkAndGetName(name).get("userName"));
            map.put("resultCode",1);
            return map;
        }
        else
        {
            map.put("userName",userName);
            map.put("resultCode",0);
            return map;
        }
    }

    /**
     * 检查名字是否是合适的，即不可以是4，13，14，38，250等数字
     * @param name
     * @return
     */
    public String checkIfValid(String name)
    {
        if(name.equals("4")||name.equals("13")||name.equals("14")||name.equals("38")||name.equals("250"))
            return checkIfValid(Integer.toString(Integer.parseInt(name)+1));
        return name;
    }
    //====================================管理校园=======================================================================

    @RequestMapping("/mangeschool")
    public String manageMySchool() {
        return "myschool/manage-school";
    }

    /**
     * 学校信息
     * @return
     */
    @RequestMapping("/schoolinfo")
    @ResponseBody
    public SchoolDTO findCurrentSchool() {
        SchoolDTO schoolDTO = schoolService.findSchoolById(getSessionValue().getSchoolId());
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        if (month < 9 && month >= 2) {
            schoolDTO.setSchoolYear((year - 1) + "-" + year + "学年第二学期");
        } else if(month >= 9) {
            schoolDTO.setSchoolYear(year + "-" + (year + 1) + "学年第一学期");
        } else {
            schoolDTO.setSchoolYear((year - 1) + "-" + year + "学年第一学期");
        }
        return schoolDTO;
    }


    /**
     * 开始新学期  目前只适用于  兴趣班
     * @return
     */
    @RequestMapping("/newterm")
    @ResponseBody
    public RespObj newTerm() {
        RespObj respObj = RespObj.FAILD;
        try {
//            UserDetailInfoDTO userDetailInfoDTO = userService.getUserInfoById(getUserId().toString());
            interestClassService.newTerm(getSessionValue().getSchoolId());
            respObj = RespObj.SUCCESS;
        } catch (Exception e){
            e.printStackTrace();
        }
        return respObj;
    }

    /**
     *设置新建用户默认密码
     * @param newPwd
     * @return
     */
    @RequestMapping("/upnewinitpwd")
    @ResponseBody
    public boolean setNewInitPwd(@RequestParam String newPwd) {
        UserDetailInfoDTO userInfoDTO4WB = userService.getUserInfoById(getUserId().toString());
        return schoolService.updateNewInitPwd(userInfoDTO4WB.getSchoolID(), newPwd);
    }


     /**
      * 取得学校默认密码
      *
      * */
    @RequestMapping("/getSchoolDefaultPwd")
    @ResponseBody
    public String getSchoolDefaultPwd() {
        ObjectId schoolId=new ObjectId(getSessionValue().getSchoolId());
        SchoolEntry schoolEntry=schoolService.getSchoolEntry(schoolId,Constant.FIELDS);
        return schoolEntry.getInitialPassword();
    }
    /**
     * 需要权限检查
     * @param all 是否是全学校  -1不是；1全部老师 2全部学生
     * @param users 用户ID，用“,”相连
     * @return
     * @throws IllegalParamException
     */
    @UserRoles(noValue={UserRole.STUDENT,UserRole.PARENT})
    @RequestMapping("/resetSelectPassword")
    @ResponseBody
    public boolean resetSelectPassword(@RequestParam(defaultValue="") final String all,
                             String users,String pwd) throws IllegalParamException
    {

    	logger.info("resetSelectPassword"+getUserId());
        if(StringUtils.isBlank(all) && StringUtils.isBlank(users))
        {
            throw new IllegalParamException("user is error!!");
        }

        List<ObjectId> userIds =new ArrayList<ObjectId>();

        ObjectId schoolId=new ObjectId(getSessionValue().getSchoolId());

        if(StringUtils.isNotBlank(all))
        {
            Map<String,List<ObjectId>> uisMap=manageCountService.getRoleUserIdBySchoolId(schoolId.toString());

            if(all.indexOf("1")>=Constant.ZERO)
            {
                userIds=uisMap.get("teaIds");

            }
            if(all.indexOf("2")>=Constant.ZERO)
            {
                userIds=uisMap.get("stuIds");

            }
            if(all.indexOf("3")>=Constant.ZERO)
            {
                List<ObjectId> stuIds=uisMap.get("stuIds");
                List<ObjectId> teaIds=uisMap.get("teaIds");
                userIds.addAll(stuIds);
                userIds.addAll(teaIds);
            }

        }

        if(StringUtils.isNotBlank(users))
        {
            String[] userArr=StringUtils.split(users, Constant.COMMA);
            for(String user:userArr)
            {
                if(null!=user && ObjectId.isValid(user))
                {
                    userIds.add(new ObjectId(user));
                }
            }
        }
        return schoolService.resetSelectPassword(userIds, pwd);
    }

    /**
     * 更新学校信息
     * @param schoolName
     * @param schoolLevel
     * @return
     */
    @RequestMapping("/upschool")
    @ResponseBody
    public boolean updateSchool(@RequestParam String schoolName, @RequestParam String schoolLevel) {
        String[] levels = schoolLevel.split(",");
        int num = 0;
        for (int i = 0; i < levels.length; i++) {
            if ("1".equals(levels[i])) {
                if (i == 0) {
                    num += 2;
                } else if (i == 1) {
                    num += 4;
                } else if (i == 2) {
                    num += 8;
                }
            }
        }
        schoolService.updateSchoolNameAndLevel(getSessionValue().getSchoolId(), schoolName, num);
        return true;
    }

    /**
     * 添加新的年级
     * @param gradename
     * @param currentgradeid
     * @param teacherid
     * @return
     */
    @RequestMapping("/addgrade")
    @ResponseBody
    public boolean addGrade(@RequestParam String gradename,
                            @RequestParam String currentgradeid,
                            @RequestParam String teacherid,
                            @RequestParam String cteacherid) {
    	
    	logger.info("addGrade"+getUserId());
        Grade grade = new Grade(new BasicDBObject());
        UserDetailInfoDTO userInfoDTO4WB = userService.getUserInfoById(getUserId().toString());
        grade.setName(gradename);
        if (!StringUtils.isBlank(teacherid)) {
            if (ObjectId.isValid(teacherid)) {
                grade.setLeader(new ObjectId(teacherid));
            }
        }
        if (!StringUtils.isBlank(cteacherid)) {
            if (ObjectId.isValid(cteacherid)) {
                grade.setCleader(new ObjectId(cteacherid));
            }
        }
        if (!StringUtils.isBlank(currentgradeid)) {
            grade.setGradeType(Integer.parseInt(currentgradeid));
        } else {
            grade.setGradeType(0);
        }
        grade.setGradeId(new ObjectId());

        schoolService.addGrade(userInfoDTO4WB.getSchoolID(), grade);
        return true;
    }

    /**
     * 修改年级信息
     * @param gradeLevel
     * @param gradename
     * @param gradeid
     * @param teacherid
     * @return
     */
    @RequestMapping("/upgrade")
    @ResponseBody
    public boolean updateGrade(@RequestParam String gradeLevel,
                               @RequestParam String gradename,
                               @RequestParam String gradeid,
                               @RequestParam String teacherid,
                               @RequestParam String cteacherid) {
    	logger.info("updateGrade"+getUserId());
        Grade grade = new Grade(new BasicDBObject());
        UserDetailInfoDTO userInfoDTO4WB = userService.getUserInfoById(getUserId().toString());
        grade.setName(gradename);
        grade.setGradeId(new ObjectId(gradeid));
        if (!StringUtils.isBlank(teacherid) && !teacherid.equals("0")) {
            grade.setLeader(new ObjectId(teacherid));
        }
        if (!StringUtils.isBlank(cteacherid) && !cteacherid.equals("0")) {
            grade.setCleader(new ObjectId(cteacherid));
        }
        grade.setGradeType(Integer.parseInt(gradeLevel));
        schoolService.updateGradeById(userInfoDTO4WB.getSchoolID(), grade);
        return true;
    }

    /**
    *
    * 删除年级信息
    *
    * */
    @RequestMapping("/delgrade")
    @ResponseBody
    public Map<String, Object> deleteGrade(String gradeid) {
    	logger.info("deleteGrade "+getUserId());
        //检查是否有该年级下面是否有子班级
        Map<String, Object> map = new HashMap<String, Object>();
        boolean k = classService.existClass(gradeid);
        if (k) {
            map.put("code", 1);
            map.put("message", "该年级下存在子班级，不可删除");
            return map;
        }
        UserDetailInfoDTO userInfoDTO4WB = userService.getUserInfoById(getUserId().toString());
        schoolService.deleteGradeById(userInfoDTO4WB.getSchoolID(), gradeid);
        map.put("code", 2);
        map.put("message", "删除成功");
        return map;
    }

    @RequestMapping("/manageclass")
    public String classListPage(@RequestParam String gradeId) {
        return "myschool/manage-class";
    }

    /**
    *
    * 班级列表
    *
    * */
    @RequestMapping("/classlist")
    @ResponseBody
    public Map<String, Object> classList(@RequestParam String gradeid) {
        List<ClassInfoDTO> classInfoDTOList = classService.findClassByGradeId(gradeid);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows", classInfoDTOList);
        return map;
    }


    /**
    *
    * 查找班级信息
    *
    * */
    @RequestMapping("/findclassinfo")
    @ResponseBody
    public ClassInfoDTO findClassInfo(@RequestParam String classId) {
        ClassInfoDTO classInfoDTO = classService.findClassInfoByClassId(classId);
        return classInfoDTO;
    }

    /**
    *
    * 修改班级
    *
    * */
    @RequestMapping("/upclass")
    @ResponseBody
    public Map<String, Object> updateClassInfo(@RequestParam String classid,
                                               @RequestParam String classname,
                                               @RequestParam String teacherid) {
        Map<String, Object> map = new HashMap<String, Object>();
        classService.updateClassInfo(classid, classname, teacherid);
        map.put("code", 200);
        map.put("message", "修改成功");
        return map;
    }

    /**
    *
    * 添加班级
    *
    * */
    @RequestMapping("/addclass")
    @ResponseBody
    public boolean addClassInfo(@RequestParam String classname,
                                @RequestParam String teacherid,
                                @RequestParam String gradeid) {
        UserDetailInfoDTO userInfoDTO4WB = userService.getUserInfoById(getUserId().toString());
        ClassInfoDTO classInfoDTO = new ClassInfoDTO();
        classInfoDTO.setClassName(classname);
        if (null!=teacherid && ObjectId.isValid(teacherid) ) {
            classInfoDTO.setMainTeacherId(teacherid);
//            List<ObjectId> objectIdList = new ArrayList<ObjectId>();
//            objectIdList.add(new ObjectId(teacherid));
//            classInfoDTO.setTeacherIds(objectIdList);
        }
        classInfoDTO.setGradeId(gradeid);
        classInfoDTO.setSchoolId(userInfoDTO4WB.getSchoolID());
        classService.addClassInfo(classInfoDTO);
        return true;
    }

    /**
   *
   * 删除班级
   * 返回false 说明该班级下有老师不能删除
   * 返回true  表示删除成功
   * */
    @RequestMapping("/delclassinfo")
    @ResponseBody
    public boolean deleteClassInfo(@RequestParam String classid) {
    	logger.info("deleteClassInfo "+getUserId()+" classid="+classid);
        boolean k = classService.deleteClassInfoById(classid);
        return k;
    }

    @RequestMapping("/managestudent")
    public String manageStudent(@RequestParam String classId) {
        return "myschool/manage-student";
    }

    /**
    *
    * 班级老师-科目列表  同时返回班级学生列表
    *
    * */
    @RequestMapping("/testulist")
    @ResponseBody
    public Map<String, Object> teacherList(@RequestParam String classid) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<ObjectId> objectIdList = classService.findTeacherIdsById(new ObjectId(classid));
        if (objectIdList == null || objectIdList.isEmpty()) {
            //没有老师，继续查找学生
            ClassInfoDTO classInfoDTO = classService.findClassInfoByClassId(classid);
            List<ObjectId> studentIds = classInfoDTO.getStudentIds();
            List<UserDetailInfoDTO> userInfoDTO4WBList = userService.findUserInfoByIds(studentIds);
            if(userInfoDTO4WBList==null || userInfoDTO4WBList.isEmpty())
            {
                map.put("code", 500);
                return map;
            }
            map.put("techList", new ArrayList<TeacherSubjectView>());
            map.put("stuList", userInfoDTO4WBList);
            return map;
        } else {
            List<TeacherSubjectView> teacherSubjectViewList = classService.findTeacherByIds(objectIdList, classid);
            ClassInfoDTO classInfoDTO = classService.findClassInfoByClassId(classid);
            List<ObjectId> studentIds = classInfoDTO.getStudentIds();
            List<UserDetailInfoDTO> userInfoDTO4WBList = userService.findUserInfoByIds(studentIds);
            map.put("techList", teacherSubjectViewList);
            map.put("stuList", userInfoDTO4WBList);
            return map;
        }
    }

    /**
    *
    * 班级科目列表
    *
    * */
    @RequestMapping("/subjectlist")
    @ResponseBody
    public Map<String, Object> subjectListOfClass(@RequestParam String classId) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (!StringUtils.isBlank(classId)) {
            //当前班级的所在年级所开设所有科目
            ClassInfoDTO classInfo = classService.findClassInfoByClassId(classId);
            String gradeId = classInfo.getGradeId();
            if (!StringUtils.isBlank(gradeId)) {
                List<SubjectView> subjectViewList = schoolService.
                        findSubjectListBySchoolIdAndGradeId(getSessionValue().getSchoolId(), gradeId);
                map.put("rows", subjectViewList);
            }
        }
        return map;
    }

    /**
    *
    * 添加一个老师 科目关系
    *
    * */
    @RequestMapping("/addtesub")
    @ResponseBody
    public boolean addTeacherSubject(@RequestParam String classid,
                                     @RequestParam String teacherid,
                                     @RequestParam String subjectid) {
        classService.addTeacherSubject(classid, teacherid, subjectid);
        UserDetailInfoDTO teacher = userService.getUserInfoById(teacherid);
        int role=teacher.getRole();
        if(UserRole.isHeadmaster(role)  && !UserRole.isTeacher(role) ){
            userService.updateRole(teacherid,role+UserRole.TEACHER.getRole());
        }
        return true;
    }

    /**
    *
    * 修改老师-科目记录
    *
    * */
    @RequestMapping("/uptesub")
    @ResponseBody
    public boolean updateTeacherSubject(@RequestParam String tcsId,
                                        @RequestParam String teacherid,
                                        @RequestParam String subjectid) {
        List<SubjectView> idValuePairList = schoolService.findSubjectList(getSessionValue().getSchoolId());
        Map<String, String> map = new HashMap<String, String>();
        for (SubjectView subject : idValuePairList) {
            map.put(subject.getId(), subject.getName());
        }
        classService.updateTeacherSubject(tcsId, teacherid, subjectid, map.get(subjectid));
        return true;
    }

    /**
    *
    * 删除老师科目记录
    *
    * */
    @RequestMapping("/deltesub")
    @ResponseBody
    public boolean deleteTeacherSubject(@RequestParam String tcsId) {
    	
    	logger.info("deleteTeacherSubject "+getUserId()+" tcsId="+tcsId);
        TeacherClassSubjectDTO dto=classService.findTeacherClassSubjectInfo(tcsId);
        classService.deleteTeacherSubjectById(tcsId);
        String teacherId=dto.getTeacherId();
        List<TeacherClassSubjectDTO> dtoList=classService.findTeacherCLassSubjectByTeacherId(teacherId);
        if(dtoList==null || dtoList.isEmpty()){
            UserDetailInfoDTO userDetailInfoDTO=userService.getUserInfoById(teacherId);
            int role=userDetailInfoDTO.getRole();
            if(UserRole.isHeadmaster(role) && UserRole.isTeacher(role)){
                userService.updateRole(teacherId,role-UserRole.TEACHER.getRole());
            }
        }
        return true;
    }

    /**
     * 检查学生姓名是否重复
     * @param userName
     * @param number
     * @return
     */
    @RequestMapping("/checkstunm")
    @ResponseBody
    public Map<String, Object> checkStuName(@RequestParam String userName, @RequestParam String number,String userId) {
        UserDetailInfoDTO userInfoDTO4WB = userService.getUserInfoById(getUserId().toString());
        if(!StringUtils.isEmpty(userId)) {
            userInfoDTO4WB = userService.getUserInfoById(userId);
        }
        SchoolDTO schoolDTO = schoolService.findSchoolById(userInfoDTO4WB.getSchoolID());
        String initPwd = schoolDTO.getInitPwd();
        Map<String, Object> map = new HashMap<String, Object>();
        String username = userInfoDTO4WB.getUserName();
        String parentname = username + "爸爸 " + username + "妈妈";
        if (!userName.equals(userInfoDTO4WB.getUserName())) {
            Map<String,Object> result=checkAndGetStuName(userName);
            username = result.get("userName").toString();
            parentname = result.get("userName") + "爸爸 "+result.get("userName") +"妈妈";
        }

        map.put("username", username);
        map.put("parusername", parentname);
        map.put("password", initPwd);
        return map;

    }

    /**
     * 检查核对学生用户名是否已经存在
     * @param userName
     * @return
     */
    public Map<String,Object> checkAndGetStuName(String userName)
    {
        Map<String,Object> map=new HashMap<String, Object>();
        UserEntry e=userService.searchUserByUserName(userName);
        if(e==null)
        {
        	e=userService.searchUserByUserLoginName(userName);
        }
        if (null!=e) {
            boolean nameHasDigit = false;
            String nameCount="";
            String nameBase=userName;
            for(int i=userName.length()-1;i>=0;i--)
            {
                char ch=userName.charAt(i);
                if(Character.isDigit(ch))
                {
                    nameCount=ch+nameCount;
                    nameHasDigit = true;
                    nameBase=userName.substring(0,i);
                }
                else
                {
                    break;
                }
            }
            if(nameHasDigit) {
                //count=Integer.parseInt(checkIfValid(Integer.toString(Integer.parseInt(nameCount) + 1)));
                userName = nameBase + checkIfValid(Integer.toString(Integer.parseInt(nameCount) + 1));
            }
            else{
                userName = userName + "1";
                //count=1;
            }
            //return checkAndGetName(name);
            map.put("userName",checkAndGetStuName(userName).get("userName"));
            //map.put("count",count);
            return map;
        }
        else
        {
            map.put("userName",userName);
            //map.put("count",count);
            return map;
        }
    }



    /**
    *
    * 添加班级学生
    *
    * */
    @RequestMapping("/addstu")
    @ResponseBody
    public boolean addStu2Class(@RequestParam String classId,
                                @RequestParam String stuName,
                                @RequestParam String stuNickname,
                                @RequestParam int sex,
                                @RequestParam String parentName) {
//        UserDetailInfoDTO userInfoDTO4WB = new UserDetailInfoDTO();
//        ObjectId stuId = new ObjectId();
//        ObjectId parentId = new ObjectId();
//        userInfoDTO4WB.setSex(sex);
//        userInfoDTO4WB.setUserName(stuName);
//        userInfoDTO4WB.setNickName(stuNickname);
//        userInfoDTO4WB.setId(stuId.toString());
//        userInfoDTO4WB.setRelationId(parentId.toString());
//        userInfoDTO4WB.setRole(UserRole.STUDENT.getRole());
//        
//        userInfoDTO4WB.setSchoolID(getSessionValue().getSchoolId());
//        SchoolDTO dto=  schoolService.findSchoolById(getSessionValue().getSchoolId());
//        userInfoDTO4WB.setPassWord(MD5Utils.getMD5String(dto.getInitPwd()));;
//        
//        //添加学生
//        userService.addUserInfo(userInfoDTO4WB);
//        userInfoDTO4WB.setUserName(parentName);
//        userInfoDTO4WB.setNickName(parentName);
//        userInfoDTO4WB.setId(parentId.toString());
//        userInfoDTO4WB.setRelationId(stuId.toString());
//        userInfoDTO4WB.setRole(UserRole.PARENT.getRole());
//        //添加家长
//        userService.addUserInfo(userInfoDTO4WB);
//        //将学生加入班级
//        classService.addStudentId(classId, stuId.toString());
        
    	logger.info("addStu2Class "+getUserId());
        SchoolDTO dto=  schoolService.findSchoolById(getSessionValue().getSchoolId());
        
        
        //
        ObjectId studentId =new ObjectId();
        ObjectId fatherId =new ObjectId();
        ObjectId matherId =new ObjectId();
        //添加学生
        UserEntry studentEntry =new UserEntry(stuName, MD5Utils.getMD5String(dto.getInitPwd()), Constant.EMPTY, Constant.EMPTY);
        studentEntry.setLetter(PinYin2Abbreviation.cn2py(stuName));
        studentEntry.setSex(sex);
        studentEntry.setNickName(stuNickname);
        studentEntry.setID(studentId);
        studentEntry.setRole(UserRole.STUDENT.getRole());
        studentEntry.setSchoolID(new ObjectId(getSessionValue().getSchoolId()));
        studentEntry.setChatId(studentId.toString());
        List<ObjectId> parentIds =new ArrayList<ObjectId>();
        parentIds.add(fatherId);
        parentIds.add(matherId);
        studentEntry.setConnectIds(parentIds);;
        userService.addUser(studentEntry);
        
        
        
        //添加爸爸
        UserEntry fatherEntry =new UserEntry(stuName+"爸爸", MD5Utils.getMD5String(dto.getInitPwd()), Constant.EMPTY, Constant.EMPTY);
        fatherEntry.setLetter(PinYin2Abbreviation.cn2py(fatherEntry.getUserName()));
        fatherEntry.setSex(1);
        fatherEntry.setNickName(stuName+"爸爸");
        fatherEntry.setID(fatherId);
        fatherEntry.setRole(UserRole.PARENT.getRole());
        fatherEntry.setSchoolID(new ObjectId(getSessionValue().getSchoolId()));
        fatherEntry.setConnectIds(Arrays.asList(studentId));
        fatherEntry.setChatId(fatherId.toString());
        
        userService.addUser(fatherEntry);
        
        
        //添加爸爸
        UserEntry matherEntry =new UserEntry(stuName+"妈妈", MD5Utils.getMD5String(dto.getInitPwd()), Constant.EMPTY, Constant.EMPTY);
        matherEntry.setLetter(PinYin2Abbreviation.cn2py(matherEntry.getUserName()));
        matherEntry.setSex(0);
        matherEntry.setNickName(stuName+"妈妈");
        matherEntry.setID(matherId);
        matherEntry.setRole(UserRole.PARENT.getRole());
        matherEntry.setSchoolID(new ObjectId(getSessionValue().getSchoolId()));
        matherEntry.setConnectIds(Arrays.asList(studentId));
        matherEntry.setChatId(matherId.toString());
        userService.addUser(matherEntry);
        
        //将学生加入班级
        classService.addStudentId(classId, studentId.toString());
        
        try
        {
        	
	        easeMobService.createNewUser(studentId.toString());
	        easeMobService.createNewUser(fatherId.toString());
	        easeMobService.createNewUser(matherId.toString());
        }catch(Exception ex)
        {
        }
        
        return true;
    }

    /**
    *
    * 查询学生和家长信息
    *
    * */
    @RequestMapping("/stuandparent")
    @ResponseBody
    public Map<String, Object> findStudentAndParent(@RequestParam String studentId) {
        Map<String, Object> map = new HashMap<String, Object>();
        UserDetailInfoDTO stu = userService.getUserInfoById(studentId);
        List<UserDetailInfoDTO> parents = userService.findParentByStuId(studentId);
        String parent = StringUtils.EMPTY;
        if (parents!=null && parents.size()!=0) {
            for (UserDetailInfoDTO userDetail : parents) {
                parent += userDetail.getUserName() + " ";
            }
        }
        map.put("student", stu);
        map.put("parent", parent);
        return map;
    }

    /*
    *
    *
    * 修改班级学生信息
    *
    * */
    @RequestMapping("/upstu")
    @ResponseBody
    public boolean updateStudentInfo(@RequestParam String studentId,
                                     @RequestParam String stuNickname,
                                     @RequestParam int sex) {
    	logger.info("updateStudentInfo "+getUserId());
        if (!StringUtils.isEmpty(stuNickname)) {
            userService.updateUserNameAndSexById(studentId, stuNickname, sex);
        }
        return true;
    }

    /*
    *
    *
    * 删除班级学生信息
    *
    *
    * */
    @RequestMapping("/delstu")
    @ResponseBody
    public boolean deleteStuFromClass(@RequestParam String classId, @RequestParam String studentId) {
    	logger.info("DELETEUSER:"+getUserId());
    	logger.info("studentId:"+studentId);
    	
        classService.deleteStuFromClass(classId, studentId);
        interestClassService.deleteStudentFromAllExpandClass(new ObjectId(studentId));
        //user集合不做删除
        userService.deleteUser(new ObjectId(studentId));
        return true;
    }

    /*
    *
    * 初始化用户密码  密码修正为该用户所在学校的密码
    *
    * */
    @RequestMapping("/resetpwd")
    @ResponseBody
    public boolean initPwd(String studentid) {
    	
      	logger.info("initPwd:"+getUserId());
    	
        UserDetailInfoDTO userInfoDTO4WB = userService.getUserInfoById(getUserId().toString());
        SchoolDTO schoolDTO = schoolService.findSchoolById(userInfoDTO4WB.getSchoolID());
        String initPwd = schoolDTO.getInitPwd();
        userService.resetPwd(new ObjectId(studentid), initPwd);
        return true;
    }

    /*
    *
    * 学生调班级
    *
    *
    * */
    @RequestMapping("/changestu")
    @ResponseBody
    public boolean classList(@RequestParam String oldClassId,
                             @RequestParam String newClassId,
                             @RequestParam String studentId) {
        if (oldClassId.equals(newClassId)) {
            return true;
        }
        classService.updateStuClass(oldClassId, newClassId, studentId);
        return true;
    }

    //=====================================管理拓展课====================================================================
    @RequestMapping("/manageinterest")
    public String manageInterestClass(Map<String, Object> model) {
        int role = getSessionValue().getUserRole();
        if(UserRole.isManager(role) || UserRole.isHeadmaster(role)){//管理员或校长
            model.put("role", 1);
        } else {
            List<ClassEntry> classEntries = classService.findClassByMasterId(getUserId());
            if(classEntries.size() > 0){//班主任
                model.put("role", 2);
            } else {//普通老师
                model.put("role", 3);
            }
        }
        return "myschool/manage-interestCategory";
    }

    @RequestMapping("/interestList")
    public String interestList(Map<String, Object> model) {
        model.put("userId", getSessionValue().getId());
        InterestClassTermsDTO interestClassTermsDTO = interestClassTermsService.getInterestClassTermsDTO(new ObjectId(getSessionValue().getSchoolId()));
        List<IdNameValuePairDTO> terms = interestClassTermsDTO.getTerms();
        IdNameValuePairDTO term = terms.get(terms.size() - 1);
        model.put("term", term.getName());
        return "myschool/manage-interest";
    }

    /*
    *
    * 查询单个兴趣班信息
    *
    * */
    @RequestMapping("/oneinterest")
    @ResponseBody
    public InterestClassDTO findInterest(@RequestParam String classId) {
        if (null!=classId && ObjectId.isValid(classId)) {
            InterestClassDTO interestClassDTO = interestClassService.findInterestClassByClassId(classId);
//            if(!interestClassDTO.getIsLongCourse()){//短课
//                interestClassDTO.setClassName(interestClassDTO.getClassName().substring(0,interestClassDTO.getClassName().length()-3));
//            }
            return interestClassDTO;
        }
        return null;
    }

    /*
    *
    * 拓展课列表
    *
    * */
    @RequestMapping("/expandlist")
    @ResponseBody
    public Map<String, Object> listExpandClass(String interestCategoryId, @RequestParam(required = false, defaultValue = "-1") int termType) {
        Map<String, Object> map = new HashMap<String, Object>();
        UserDetailInfoDTO userInfoDTO4WB = userService.getUserInfoById(getUserId().toString());
        List<InterestClassDTO> classInfoDTOList = interestClassService.findExpandClassBySchoolId(userInfoDTO4WB.getSchoolID(), interestCategoryId, termType);
        map.put("rows", classInfoDTOList);
        return map;
    }

    /*
   *
   * 添加拓展课
   *
   * */
    @RequestMapping("/addexpand")
    @ResponseBody
    public boolean addExpandClass(@RequestParam String interestClassName,
                                  @RequestParam String teacherid,
                                  @RequestParam int studentCount,
                                  @RequestParam String classtime,
                                  @RequestParam String subjectid,
                                  @RequestParam String typeId,
                                  @RequestParam String opentime,
                                  @RequestParam String closetime,
                                  @RequestParam String coursetype,
                                  @RequestParam String firstteam,
                                  @RequestParam String secondteam,
                                  @RequestParam String gradeArry,
                                  @RequestParam String classContent,
                                  @RequestParam int sex,
                                  String room) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyy/MM/dd HH:mm");
        InterestClassDTO expandClass = new InterestClassDTO();
        expandClass.setClassName(interestClassName);
        expandClass.setTeacherId(teacherid);
        expandClass.setTotalStudentCount(studentCount);
        String schoolId = getSessionValue().getSchoolId();
        expandClass.setSchoolId(schoolId);
        String[] classTimes = classtime.split(",");
        expandClass.setClassTime(Arrays.asList(classTimes));
        expandClass.setSubjectId(subjectid);
        expandClass.setClassContent(classContent);
        expandClass.setRoom(room);
        expandClass.setSex(sex);

        Date openTime = null;
        Date closeTime = null;
        try {
            openTime = simpleDateFormat.parse(opentime);
            closeTime = simpleDateFormat.parse(closetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        expandClass.setOpenTime(openTime);
        expandClass.setCloseTime(closeTime);
        String[] grades = gradeArry.split(",");
        expandClass.setGradeList(Arrays.asList(grades));
        SchoolDTO school = schoolService.findSchoolById(schoolId);
        expandClass.setTermType(school.getSchoolTermType());
//        expandClass.setTermType(1);
        expandClass.setState(1);
        expandClass.setTypeId(typeId);

        if ("1".equals(coursetype)) {
            expandClass.setIsLongCourse(true);
            expandClass.setFirstTerm("0");
            expandClass.setSecondTerm("0");
            ObjectId classId=interestClassService.addExpandClass(expandClass);
            //将教师-科目关系加入数据库
            interestClassService.addTeacherSubject(classId, teacherid, subjectid);
            System.out.println("长课  " + classId);
        } else {
            expandClass.setIsLongCourse(false);
            ObjectId second = null;
            if(secondteam.equals("1")){
                expandClass.setFirstTerm("0");
                expandClass.setSecondTerm("1");
                expandClass.setClassName(interestClassName + "短课2");
                second=interestClassService.addExpandClass(expandClass);
                //将教师-科目关系加入数据库
                interestClassService.addTeacherSubject(second, teacherid, subjectid);
                System.out.println("短课2  " + second);
            }
            if(firstteam.equals("1")){
                expandClass.setFirstTerm("1");
                expandClass.setSecondTerm("0");
                expandClass.setClassName(interestClassName + "短课1");
                expandClass.setRelationId(second);
                ObjectId classId=interestClassService.addExpandClass(expandClass);
                //将教师-科目关系加入数据库
                interestClassService.addTeacherSubject(classId, teacherid, subjectid);
                System.out.println("短课1  " + classId);
            }

        }
//        expandClass.setFirstTerm(firstteam);
//        expandClass.setSecondTerm(secondteam);


        return true;
    }

    /*
    *
    * 修改拓展课
    *
    * */
    @RequestMapping("/upexpand")
    @ResponseBody
    public boolean updateExpandClass(@RequestParam String interestClassName,
                                     @RequestParam String teacherid,
                                     @RequestParam int studentCount,
                                     @RequestParam String classtime,
                                     @RequestParam String subjectid,
                                     @RequestParam String typeId,
                                     @RequestParam String opentime,
                                     @RequestParam String closetime,
                                     @RequestParam String classid,
                                     @RequestParam String gradeArry,
                                     @RequestParam String classContent,
                                     @RequestParam String room,
                                     @RequestParam int sex) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyy/MM/dd HH:mm");
        InterestClassDTO expandClass = new InterestClassDTO();
        String[] classTimes = classtime.split(",");
        String[] grades = gradeArry.split(",");
        Date openTime = null;
        Date closeTime = null;
        InterestClassDTO oldClass = interestClassService.findInterestClassByClassId(classid);
        try {
            openTime = simpleDateFormat.parse(opentime);
            closeTime = simpleDateFormat.parse(closetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        expandClass.setClassName(interestClassName);
        expandClass.setRoom(room);
        expandClass.setSex(sex);
        expandClass.setTeacherId(teacherid);
        expandClass.setTotalStudentCount(studentCount);
        expandClass.setClassTime(Arrays.asList(classTimes));
        expandClass.setSubjectId(subjectid);
        expandClass.setOpenTime(openTime);
        expandClass.setCloseTime(closeTime);
        expandClass.setGradeList(Arrays.asList(grades));
        expandClass.setId(new ObjectId(classid).toString());
        expandClass.setSchoolId(getSessionValue().getSchoolId());
        expandClass.setClassContent(classContent);
        expandClass.setTypeId(oldClass.getTypeId());
        expandClass.setTermType(oldClass.getTermType());
//        expandClass.
//        expandClass.setTypeId(typeId);
        if(oldClass.getIsLongCourse() == true){//长课
            //更新拓展课信息
            interestClassService.updateExpandClass(expandClass);
            //修改tcsubject表和dir表
            if(!teacherid.equals(oldClass.getTeacherId())||!subjectid.equals(oldClass.getSubjectId()))
                interestClassService.updateTeacherSubject(new ObjectId(classid), teacherid, subjectid, oldClass.getTeacherId(), oldClass.getSubjectId(), interestClassName);
            return true;
        } else {//短课
            //更新短课1
            expandClass.setClassName(interestClassName + "短课1");
            interestClassService.updateExpandClass(expandClass);
            if(!teacherid.equals(oldClass.getTeacherId())||!subjectid.equals(oldClass.getSubjectId()))
                interestClassService.updateTeacherSubject(new ObjectId(classid), teacherid, subjectid, oldClass.getTeacherId(), oldClass.getSubjectId(), interestClassName + "短课1");

            //更新短课2
            ObjectId relationId = oldClass.getRelationId();
            expandClass.setId(relationId.toString());
            expandClass.setClassName(interestClassName + "短课2");
            interestClassService.updateExpandClass(expandClass);
            if(!teacherid.equals(oldClass.getTeacherId())||!subjectid.equals(oldClass.getSubjectId()))
                interestClassService.updateTeacherSubject(relationId, teacherid, subjectid, oldClass.getTeacherId(), oldClass.getSubjectId(), interestClassName + "短课2");
            return true;
        }
//        //更新拓展课信息
//        interestClassService.updateExpandClass(expandClass);
//        //修改tcsubject表和dir表
//        if(!teacherid.equals(oldClass.getTeacherId())||!subjectid.equals(oldClass.getSubjectId()))
//            interestClassService.updateTeacherSubject(new ObjectId(classid), teacherid, subjectid, oldClass.getTeacherId(), oldClass.getSubjectId(), interestClassName);
//        return true;
    }

    /*
    *
    * 删除拓展课
    *
    * */
    @RequestMapping("/delexpand")
    @ResponseBody
    public boolean deleteExpandClass(@RequestParam String classid, @RequestParam(required = false, defaultValue = "-1") int termType) {
    	
    	logger.info("deleteExpandClass "+getUserId()+" classid="+classid);
        InterestClassDTO interest = interestClassService.findInterestClassByClassId(classid);
        List<InterestClassStudentDTO> list = interest.getCurrentStudentList();
        if (list == null || list.isEmpty()) {
            ObjectId relationId = interest.getRelationId();
            ObjectId schoolId = new ObjectId(getSessionValue().getSchoolId());
            if(relationId!=null && !relationId.toString().equals("")){//存在关联课程
                InterestClassDTO relationClass = interestClassService.findInterestClassByClassId(relationId.toString());
                List<InterestClassStudentDTO> reList = relationClass.getCurrentStudentList();
                if(reList==null || reList.isEmpty()){
                    interestClassService.deleteExpandClass(classid, termType, schoolId);
                    interestClassService.deleteTcsubjectAndDir(new ObjectId(classid),new ObjectId(interest.getTeacherId()),new ObjectId(interest.getSubjectId()));

                    interestClassService.deleteExpandClass(relationId.toString(), termType, schoolId);
                    interestClassService.deleteTcsubjectAndDir(relationId,new ObjectId(relationClass.getTeacherId()),new ObjectId(relationClass.getSubjectId()));
                    return true;
                } else {
                    return false;
                }
            } else {
                interestClassService.deleteExpandClass(classid, termType, schoolId);
                interestClassService.deleteTcsubjectAndDir(new ObjectId(classid),new ObjectId(interest.getTeacherId()),new ObjectId(interest.getSubjectId()));
                return true;
            }


        } else {
            return false;
        }


    }

    /*
    *
    * 开启或关闭拓展课
    *
    * */
    @RequestMapping("/upexpstat")
    @ResponseBody
    public int updateExpandClassState(@RequestParam String classid) {
        int state = interestClassService.updateExpandClassState(classid);
        return state;
    }

    /*
    *
    * 兴趣班学生列表
    *
    *
    * */
    @RequestMapping("/interestdata")
    @ResponseBody
    public Map<String, Object> dataMap(@RequestParam String classId) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        InterestClassDTO interestClassDTO = interestClassService.findInterestClassByClassId(classId);
        List<InterestClassStudentDTO> stuList1 = interestClassDTO.getStudentList();
        //得到当次选课的学生
        List<InterestClassStudentDTO> stuList = new ArrayList<InterestClassStudentDTO>();
        if(null!=stuList1 && stuList1.size()>0){
            for(InterestClassStudentDTO dto : stuList1){
                if(dto.getTermType() == interestClassDTO.getTermType()){
                    stuList.add(dto);
                }
            }
        }
        List<ObjectId> studentIds = new ArrayList<ObjectId>();
        if (stuList != null) {
            for (InterestClassStudentDTO interestClassStudentDTO : stuList) {
                studentIds.add(new ObjectId(interestClassStudentDTO.getStudentId()));
            }
            Map<ObjectId, UserEntry> userMap = userService.getUserEntryMap(studentIds, new BasicDBObject("nm", 1));
            for (InterestClassStudentDTO studentDTO : stuList) {
                UserEntry userEntry = userMap.get(new ObjectId(studentDTO.getStudentId()));
                if (userEntry != null) {
                    studentDTO.setStudentName(userEntry.getUserName());
                    String name = classService.findMainClassNameByUserId(studentDTO.getStudentId());
                    studentDTO.setClassName(name);
                }
            }
            returnMap.put("students", stuList);
        }
        returnMap.put("classInfo", interestClassDTO);
        return returnMap;
    }

    /*
    * 学校所有学生
    *
    * */
    @RequestMapping("allstu")
    @ResponseBody
    public Map<String, Object> schoolAllStudents() {
        UserDetailInfoDTO userInfoDTO4WB = userService.getUserInfoById(getUserId().toString());
        List<ClassInfoDTO> classInfoDTOList = classService.findClassInfoBySchoolId(userInfoDTO4WB.getSchoolID());
        Set<ObjectId> stuIds = new HashSet<ObjectId>();
        for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
            stuIds.addAll(classInfoDTO.getStudentIds());
        }
        List<ObjectId> ids = new ArrayList<ObjectId>();
        ids.addAll(stuIds);
        List<UserDetailInfoDTO> userInfoDTO4WBList = userService.findUserInfoByIds(ids);
        Map<String, Object> students = new HashMap<String, Object>();
        students.put("rows", userInfoDTO4WBList);
        return students;
    }

    @RequestMapping("/getSchoolAllStudent")
    @ResponseBody
    public Map<String, Object> getSchoolAllStudent(){
        System.out.println();
        return userService.getSchoolAllStudent(getSessionValue().getSchoolId());
    }

    @RequestMapping("/getGradeStudent")
    @ResponseBody
    public Map<String, Object> getGradeStudent(String interestClassId, String gradeId, String classId){
        InterestClassDTO dto = interestClassService.findInterestClassByClassId(interestClassId);
        List<String> gradeList = dto.getGradeList();

        List<GradeView> gradeViewList = schoolService.searchSchoolGradeList(getSessionValue().getSchoolId());
        List<GradeView> views = new ArrayList<GradeView>();
        for(GradeView gradeView : gradeViewList){
            for(String gId : gradeList){
                if(gradeView.getId().equals(gId)){
                    views.add(gradeView);
                }
            }

        }

        if(null != gradeId && "" != gradeId){
            gradeList.clear();
            gradeList.add(gradeId);
        }

        Map<String, Object> model= userService.getGradeStudent(gradeList, classId);

        model.put("gradeList", views);
        return model;
    }

    /*
    *
    *某个兴趣班的学生信息
    *
    * */
    @RequestMapping("/expclassstu")
    @ResponseBody
    public Map<String, Object> expandClassStu(@RequestParam String classid) {
        Map<String, Object> map = new HashMap<String, Object>();
        InterestClassDTO interestClassDTO = interestClassService.findInterestClassByClassId(classid);
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        for (InterestClassStudentDTO interestClassStudentDTO : interestClassDTO.getStudentList()) {
            objectIdList.add(new ObjectId(interestClassStudentDTO.getStudentId()));
        }
        List<ObjectId> objectIds = objectIdList;
        List<UserDetailInfoDTO> userInfoDTO4WBList = userService.findUserInfoByIds(objectIds);
        for (UserDetailInfoDTO userInfoDTO4WB : userInfoDTO4WBList) {
            userInfoDTO4WB.setMainClassName(classService.findMainClassNameByUserId(userInfoDTO4WB.getId().toString()));
        }
        map.put("rows", userInfoDTO4WBList);
        return map;
    }

    /*
    *
    * 为兴趣班添加一个学生
    *
    * */
    @RequestMapping("/addstu2expand")
    @ResponseBody
    public RespObj addStu2ExpandClass(@RequestParam String classId,
                                      @RequestParam String studentId,
                                      @RequestParam Integer courseType,
                                      @RequestParam(required = false, defaultValue = "0") int dropState) {
    	
       	logger.info("addStu2ExpandClass "+getUserId()+" classid="+classId);
        RespObj respObj = RespObj.FAILD;
        try{
            InterestClassDTO interestClassDTO = interestClassService.findInterestClassByClassId(classId);
            if(courseType == 2){
                interestClassDTO = interestClassService.findInterestClassByClassId(interestClassDTO.getRelationId().toString());
            }
            String message = interestClassService.addStudent2ExpandClass(interestClassDTO.getId(), studentId, courseType, dropState, false);
            respObj = RespObj.SUCCESS;
            respObj.setMessage(message);
        } catch (Exception e){
            e.printStackTrace();
        }

        return respObj;
    }

    /*
    * 兴趣班删除一个学生
    *
    *
    * */
    @RequestMapping("/delstu4expand")
    @ResponseBody
    public boolean deleteStu2ExpandClass(@RequestParam String classId,
                                         @RequestParam String studentId,
                                         @RequestParam Integer type) {
    	
    	logger.info("deleteStu2ExpandClass "+getUserId()+" classid="+classId);
        InterestClassDTO interestClassDTO = interestClassService.findInterestClassByClassId(classId);
        if(type == 2){
            interestClassDTO = interestClassService.findInterestClassByClassId(interestClassDTO.getRelationId().toString());
        }
        boolean k = interestClassService.deleteStudent2ExpandClass(interestClassDTO.getId(), studentId);
        return k;
    }

    //=====================================新闻管理======================================================================
    /*
    *
    * 新闻管理
    * 导向
    * */
    @RequestMapping("/managenews")
    public String mangeNews(Integer page, Model model) {
        return "myschool/manage-news";
    }

    /*
    *
    * 添加一个新闻
    * */
    @RequestMapping("/addnews")
    @ResponseBody
    public boolean addNews(@RequestParam String columnId,@RequestParam String title,@RequestParam int pinned,
                          @RequestParam String thumb,@RequestParam String digest,@RequestParam String content) {
    	
    	logger.info("addNews "+getUserId()+" content="+content);
        News news=new News();

        news.setColumn(columnId);
        news.setContent(content);
        news.setDigest(digest);
        news.setPinned(pinned);
        news.setThumb(thumb);
        news.setTitle(title);
        news.setReadCount(0);
        news.setUserId(getUserId().toString());
        news.setId(new ObjectId().toString());
        int role=getSessionValue().getUserRole();
        if(UserRole.isEducation(role)){
            EducationBureauDTO eduDto=manageCountService.getEducationByUserId(getUserId());
            news.setEducationId(eduDto.getId());
        }else{
            news.setSchoolId(getSessionValue().getSchoolId());
        }
        newsService.addNews(news);
        return true;
    }

    /*
    *
    * 新闻列表 带分页
    *
    * */
    @RequestMapping("/newslist")
    @ResponseBody
    public Map<String, Object> newsList(@RequestParam String columnId,@RequestParam int page, @RequestParam int pageSize,@RequestParam String title) {
        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 10;
        int skip = (page - 1) * pageSize;

        Map<String, Object> map = new HashMap<String, Object>();

        int role=getSessionValue().getUserRole();
        String educationId="";
        String schoolId="";
        if(UserRole.isEducation(role)){
            EducationBureauDTO eduDto=manageCountService.getEducationByUserId(getUserId());
            educationId=eduDto.getId();
        }else{
            schoolId=getSessionValue().getSchoolId();
        }
        List<News> newsList = newsService.newsList(skip, pageSize,columnId,title,schoolId,educationId);
        int total = newsService.countPage(schoolId,educationId, columnId,title);


        map.put("page", page);
        map.put("pageSize", pageSize);
        map.put("total",total);
        map.put("rows", newsList);
        return map;
    }

    /*
    * 修改新闻
    *
    * */
    @RequestMapping("/upnews")
    @ResponseBody
    public boolean updateNews(@RequestParam String newsId,@RequestParam String columnId,@RequestParam String title,@RequestParam int pinned,
                    @RequestParam String thumb,@RequestParam String digest,@RequestParam String content) {
        News news=new News();

        news.setColumn(columnId);
        news.setContent(content);
        news.setDigest(digest);
        news.setPinned(pinned);
        news.setThumb(thumb);
        news.setTitle(title);
        news.setUserId(getUserId().toString());
        news.setId(newsId);
        //news.setSchoolId(getSessionValue().getSchoolId());
        newsService.updateNews(news);
        return true;
    }

    /**
     * 批量删除新闻
     * @return
     */
    @RequestMapping("/delManyNews")
    @ResponseBody
    public boolean deleteManyNews(@RequestParam String newsIds)
    {
    	logger.info("deleteManyNews "+getUserId()+" newsIds="+newsIds);
        List<String> list=new ArrayList<String>();
        String[] objArr=newsIds.split(",");
        for(int i=0;i<objArr.length;i++)
        {
            list.add(objArr[i]);
        }
        newsService.deleteManyNews(list);
        return true;
    }

    /*
    *
    * 删除新闻
    *
    * */
    @RequestMapping("/delnews")
    @ResponseBody
    public boolean deleteNews(@RequestParam String newsId) {
    	logger.info("deleteNews "+getUserId()+" newsId="+newsId);
        newsService.deleteNewsById(newsId);
        return true;
    }


    /**
     * 获取详细内容
     */
    @RequestMapping("/getOneNewsInfo")
    @ResponseBody
    public News getOneNewsInfo(@RequestParam String newsId)
    {
        News news  = newsService.findOneNewsById(newsId);
        return news;
    }

    /**
     * 根据栏目名获取置顶的五条----不登录使用
     * @param type
     * @return
     */
    @SessionNeedless
    @RequestMapping("/getNoticeAndNews")
    @ResponseBody
    public List<News> getNoticeAndNews(@RequestParam String schoolId,@RequestParam String type)
    {
        List<News> newsList=new ArrayList<News>();
        List<NewsEntry> newsEntryList=newsService.getNoticeAndNews(schoolId,type);
        for(int i=0;i<newsEntryList.size();i++)
        {
            News news=new News(newsEntryList.get(i));
            newsList.add(news);
        }
        return newsList;
    }

    /**
     * 获取新闻公告内容-----不需登陆
     * @param newsId
     * @return
     */
    @SessionNeedless
    @RequestMapping("/getNoticeContent")
    @ResponseBody
    public News getNoticeContent(@RequestParam String newsId)
    {
        //阅读量+1；
        newsService.updateReadCount(newsId);
        return newsService.findOneNewsById(newsId);
    }

    /**
     * 跳转到公告栏----不需登陆
     * @return
     */
    @SessionNeedless
    @RequestMapping("/gotoNotice")
    public String gotoNotice()
    {
        return "/customizedpage/hexielu/notice_hexielu";
    }

    /**
     * 跳转到新闻栏-----不需登陆
     * @return
     */
    @SessionNeedless
    @RequestMapping("/gotoNews")
    public String gotoNews()
    {
        return "/customizedpage/hexielu/news_hexielu";
    }

    /**
     * 获取所有新闻以及公告列表信息-----不需登陆
     * @param page
     * @param pageSize
     * @param type
     * @return
     */
    @SessionNeedless
    @RequestMapping("/getNoticeAndNewsList")
    @ResponseBody
    public Map<String,Object> getNoticeAndNewsList(@RequestParam int page,@RequestParam int pageSize,@RequestParam String schoolId,@RequestParam String type)
    {
        Map<String,Object> map=new HashMap<String, Object>();
        List<News> pinnedNews=getNoticeAndNews(schoolId,type);
        List<News> notPinnedNews=getOtherNews(page,pageSize,type,pinnedNews,schoolId);//非置顶条目数量
        int total=getOtherNewsCount(type,pinnedNews,schoolId);//除了置顶项外共有多少条
        map.put("pinned",pinnedNews);//置顶的内容，最多五条
        map.put("page",page);
        map.put("pageSize",pageSize);
        map.put("total",total);
        map.put("notPinned",notPinnedNews);
        return map;
    }

    /**
     * 获取非置顶条目
     * @param page
     * @param pageSize
     * @param type
     * @param pinnedList
     * @return
     */

    public List<News> getOtherNews(int page,int pageSize,String type,List<News> pinnedList,String schoolName)
    {
        List<News> newsList=new ArrayList<News>();
        List<NewsEntry> newsEntryList=newsService.getOtherNews(page,pageSize,type,pinnedList,schoolName);
        for(int i=0;i<newsEntryList.size();i++)
        {
            News news=new News(newsEntryList.get(i));
            newsList.add(news);
        }
        return newsList;
    }

    /**
     * 获取非置顶条目数量，供分页使用
     * @param type
     * @param pinnedList
     * @return
     */
    public int getOtherNewsCount(String type,List<News> pinnedList,String schoolName)
    {
        return newsService.getOtherNewsCount(type,pinnedList,schoolName);
    }

    /**
     * 新闻首页切换下一页上一页
     * @param newsId 新闻ID
     * @param columnId 栏目ID
     * @param type 上一页0 下一页1
     * @return
     */
    @SessionNeedless
    @RequestMapping("/getSwitchContent")
    @ResponseBody
    public News getSwitchContent(@RequestParam String newsId,@RequestParam String columnId,@RequestParam int type)
    {
        return newsService.getSwitchContent(newsId,columnId,type);
    }
    //==========================================================新闻栏目管理开始===========================================

    /**
     * 首页
     * @return
     */
    @RequestMapping("/managenewscolumn")
    public String manageNewsColumn()
    {
        return "myschool/manage-newscolumn";
    }
    /**
     * 获取栏目列表，供添加新闻选取栏目使用,不包含分页
     */
    @RequestMapping("/getColumnList")
    @ResponseBody
    public List<NewsColumn> getColumnList()
    {
        int role=getSessionValue().getUserRole();
        String educationId="";
        String schoolId="";
        if(UserRole.isEducation(role)){
            EducationBureauDTO eduDto=manageCountService.getEducationByUserId(getUserId());
            educationId=eduDto.getId();
        }else{
            schoolId=getSessionValue().getSchoolId();
        }
        List<NewsColumn> newsList = newsService.getAllNewsColumnList(schoolId,educationId);
        return newsList;
    }
    /**
     * 获取栏目列表，含分页


     */
    @RequestMapping("/newscolumnlist")
    @ResponseBody
    public Map<String,Object> getNewsColumnList(@RequestParam int page, @RequestParam int pageSize)
    {
        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 20;
        int skip = (page - 1) * pageSize;

        Map<String, Object> map = new HashMap<String, Object>();

        int role=getSessionValue().getUserRole();
        String educationId="";
        String schoolId="";
        if(UserRole.isEducation(role)){
            EducationBureauDTO eduDto=manageCountService.getEducationByUserId(getUserId());
            educationId=eduDto.getId();
        }else{
            schoolId=getSessionValue().getSchoolId();
        }
        List<NewsColumn> newsList = newsService.getNewsColumnList(skip, pageSize, schoolId, educationId);
        int total=newsService.countColumnPage(schoolId, educationId);
        if(newsList.size()==0 && page>1)
        {
            //当前页无数据，回到上一页。适用于批量删除本页全部数据
            skip=(page-2)*pageSize;
            newsList = newsService.getNewsColumnList(skip, pageSize,schoolId, educationId);
        }

        map.put("page", page);
        map.put("pageSize", pageSize);
        map.put("total",total);

        map.put("rows", newsList);
        return map;
    }
    /**
     * 添加新闻栏目
     */
    @RequestMapping("/addnewscolumn")
    @ResponseBody
    public int addNewsColumn(@RequestParam String columnName,@RequestParam String columnDir)
    {
        NewsColumn newsColumn=new NewsColumn();
        newsColumn.setColumnName(columnName);
        /*if (newsColumn != null && !StringUtils.isBlank(newsColumn.getId()) && ObjectId.isValid(newsColumn.getId())) {
            return "forward:/myschool/upnews.do";//待定
        }*/
        newsColumn.setColumnDir(columnDir);
        newsColumn.setId(new ObjectId().toString());
        int role=getSessionValue().getUserRole();
        if(UserRole.isEducation(role)){
            EducationBureauDTO eduDto=manageCountService.getEducationByUserId(getUserId());
            newsColumn.setEducationId(eduDto.getId());
        }else{
            newsColumn.setSchoolId(getSessionValue().getSchoolId());
        }

        return newsService.addNewsColumn(newsColumn);

    }
    /**
     * 删除新闻栏目
     */
    @RequestMapping("/deleteNewsColumn")
    @ResponseBody
    public boolean deleteNewsColumn(@RequestParam String newsColumnId)
    {
    	
    	logger.info("deleteNewsColumn "+getUserId()+" newsColumnId="+newsColumnId);
        if(newsService.checkIfCanDelete(newsColumnId))//可删除
        {
            newsService.deleteNewsColumn(newsColumnId);
            return true;
        }
        return false;
    }

    /**
     * 批量删除新闻栏目
     * @param newsIds
     * @return
     */
    @RequestMapping("/delManyNewsColumn")
    @ResponseBody
    public Map<String,Object> deleteManyNewsColumn(@RequestParam String newsIds)
    {
    	
    	logger.info("deleteManyNewsColumn "+getUserId()+" newsIds="+newsIds);
        List<String> canDeleteList=new ArrayList<String>();
        List<String> cannotDeleteList=new ArrayList<String>();
        String[] objArr=newsIds.split(",");
        for(int i=0;i<objArr.length;i++)
        {
            if(newsService.checkIfCanDelete(objArr[i]))//可删除
            {
                canDeleteList.add(objArr[i]);
            }
            else
            {
                cannotDeleteList.add(objArr[i]);
            }
        }
        newsService.deleteManyNewsColumn(canDeleteList);
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("delete",canDeleteList.size());
        map.put("notdelete",cannotDeleteList);
        return map;

    }
    /**
     * 修改新闻栏目
     */
    @RequestMapping("/updatenewscolumn")
    @ResponseBody
    public int updateNewsColumn(@RequestParam String columnId,@RequestParam String columnName,@RequestParam String columnDir)
    {
    	
    	logger.info("updateNewsColumn "+getUserId()+" columnName="+columnName);
        NewsColumn newsColumn=new NewsColumn();
        newsColumn.setColumnName(columnName);
        /*if (newsColumn != null && !StringUtils.isBlank(newsColumn.getId()) && ObjectId.isValid(newsColumn.getId())) {
            return "forward:/myschool/upnews.do";//待定
        }*/
        newsColumn.setColumnDir(columnDir);
        newsColumn.setId(columnId);
        int role=getSessionValue().getUserRole();
        if(UserRole.isEducation(role)){
            EducationBureauDTO eduDto=manageCountService.getEducationByUserId(getUserId());
            newsColumn.setEducationId(eduDto.getId());
        }else{
            newsColumn.setSchoolId(getSessionValue().getSchoolId());
        }
        return newsService.updateNewsColumn(newsColumn);

    }

    /**
     * 编辑新闻栏目
     * @param newsColumnId
     * @param model
     * @return
     */
    @RequestMapping("/editnewscolumn")
    public String editNewsColumn(String newsColumnId, Model model) {
        if (!StringUtils.isBlank(newsColumnId)) {
            NewsColumn newsColumn = newsService.findOneNewsColumnById(newsColumnId);
            model.addAttribute("news", newsColumn);
        }
        return "myschool/edit-news";
    }
    /**
     * 获取栏目详细信息
     */
    @RequestMapping("/getonenewscolumn")
    @ResponseBody
    public NewsColumn getOneNewsColumn(@RequestParam String newsColumnId)
    {
        NewsColumn newsColumn=newsService.findOneNewsColumnById(newsColumnId);
        return newsColumn;
    }

    /**
     * 上传新闻图片
     * @param request
     * @return
     */
    @SuppressWarnings("all")
    @RequestMapping(value = "/addNewsPic", produces = "text/html; charset=utf-8")
    public @ResponseBody Map addBlogPic(MultipartRequest request) {
       
		Map result = new HashMap<String,Object>();
        String filePath = FileUtil.UPLOADNEWSDIR;
        List<String> fileUrls = new ArrayList<String>();
        try {
            Map<String, MultipartFile> fileMap = request.getFileMap();
            for (MultipartFile file : fileMap.values()) {
                String examName =getFileName(file);
                RespObj upladTestPaper= QiniuFileUtils.uploadFile(examName, file.getInputStream(), QiniuFileUtils.TYPE_IMAGE);
                if(upladTestPaper.getCode()!= Constant.SUCCESS_CODE)
                {
                    throw new FileUploadException();
                }
                fileUrls.add(QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE,examName));
            }
            result.put("result", true);
            result.put("path", fileUrls);
        }  catch (Exception e) {
            e.printStackTrace();
            result.put("result", false);
        }
        return result;
    }

    //得到文件名
    private String getFileName(MultipartFile file)
    {
        return new ObjectId().toString()+Constant.POINT+ file.getOriginalFilename();
    }

    //==========================================================新闻栏目管理结束===========================================
    
    /**
     * 得到班级ID，名字和年级信息,用于互动课堂
     * @param teacherId
     * @return
     * @throws IllegalParamException
     */
    @RequestMapping("/classinfos")
    @ResponseBody
    public List<ClassInfoDTO> getClassInfoDTOs(@ObjectIdType  ObjectId teacherId) throws IllegalParamException
    {
    	UserEntry ue=userService.searchUserId(teacherId);
    	if(null==ue)
    	{
    		throw new IllegalParamException("Can not find UserEntry for user;"+teacherId);
    	}
    	List<ClassInfoDTO> retList =classService.getSimpleClassInfoDTOs(teacherId, ue.getSchoolID());
    	return retList;
    }

    /****************************** guojing edit start 2015-11-26************************************/
    /**
     * 互动课堂老师获取班级学科信息
     * @param teacherId
     * @return
     * @throws IllegalParamException
     */
    @RequestMapping("/classsubjects")
    @ResponseBody
    public List<TeacherClassSubjectDTO> getTeacherClassLessons(@ObjectIdType ObjectId teacherId) throws IllegalParamException
    {
        UserEntry ue=userService.searchUserId(teacherId);
        if(null==ue)
        {
            throw new IllegalParamException("Can not find UserEntry for user;"+teacherId);
        }

        List<TeacherClassSubjectDTO> list =teacherClassSubjectService.getTeacherClassSubjectDTOList(teacherId, null);
        return list;
    }
    /****************************** guojing edit end 2015-11-26************************************/
    /**
     * 得到班级成员;应用于互动课堂
     * @param classId
     * @return
     * @throws IllegalParamException 
     */
    @RequestMapping("/members")
    @ResponseBody
    public Map<String,List<UserInfoDTO>> getClassMembers(@ObjectIdType ObjectId classId) throws IllegalParamException
    {
        Map<String,List<UserInfoDTO>> retMap =new HashMap<String, List<UserInfoDTO>>();
    	ClassEntry e=classService.getClassEntryById(classId, Constant.FIELDS);
        List<UserInfoDTO> teacherInfos =new ArrayList<UserInfoDTO>();
        List<UserInfoDTO> studentInfos =new ArrayList<UserInfoDTO>();
    	if(null==e)
    	{
            retMap.put("teachers", teacherInfos);
            retMap.put("students", studentInfos);
            return retMap;
    		//throw new IllegalParamException();
    	}
    	
    	Set<ObjectId> teacherIds =new HashSet<ObjectId>(e.getTeachers());
    	Set<ObjectId> students =new HashSet<ObjectId>(e.getStudents());
    	
    	Set<ObjectId> idSet =new HashSet<ObjectId>();
    	idSet.addAll(teacherIds);
    	idSet.addAll(students);
    	
    	Map<ObjectId, UserEntry> map=userService.getUserEntryMap(idSet,Constant.FIELDS);
    	

    	
    	UserInfoDTO dto =null;
    	for(Map.Entry<ObjectId, UserEntry> entry:map.entrySet())
    	{
    		dto=new UserInfoDTO(entry.getValue());
            dto.setNickName(dto.getName());
            dto.setAvt(AvatarUtils.getAvatar(dto.getAvt(), 1));
    		if(teacherIds.contains(entry.getKey()))
    		{
    			teacherInfos.add(dto);
    		}
    		
    		if(students.contains(entry.getKey()))
    		{
    			studentInfos.add(dto);
    		}
    	}
    	retMap.put("teachers", teacherInfos);
    	retMap.put("students", studentInfos);
    	return retMap;
    }

//===============================================管理部门============================================================
    /**
     * 部门管理
     * @return
     */
    @RequestMapping("/manager/department")
    public String manageDepartment() {
        return "myschool/manage-department";
    }

    /**
     * 部门人员管理
     * @return
     */
    @RequestMapping("/manager/department/members")
    public String manageDepartmentMembers(@ObjectIdType  ObjectId id,Map<String,Object> model) {
        model.put("depId", id.toString());
        return "myschool/manage-department-member";
    }


    /**
     * 学校部门管理
     * @return
     */
    @RequestMapping("/department")
    public String loadSchoolDepartment(Map<String,Object> model)
    {
    	return "myschool/manage-department";
    }

    /**
     * 得到学校部门
     * @return
     */
    @RequestMapping("/department/list")
    @ResponseBody
    public List<SimpleDTO> schoolDepartmentList()
    {
    	SessionValue sv =getSessionValue();
    	List<SimpleDTO> list=	schoolService.getDepartments(new ObjectId(sv.getSchoolId()));
    	return list;
    }
    /**
     * 添加学校部门
     * @return
     */
    @RequestMapping("/department/add")
    @ResponseBody
    public RespObj addSchoolDepartment(String name,String des)
    {
    	SessionValue sv =getSessionValue();
    	schoolService.addDepartment(new ObjectId(sv.getSchoolId()), name, des);
    	return RespObj.SUCCESS;
    }
    
    /**
     * 删除学校部门
     * @return
     */
    @RequestMapping("/department/delete")
    @ResponseBody
    public RespObj deleteSchoolDepartment(@ObjectIdType ObjectId id)
    {
    	DepartmentEntry e=schoolService.getDepartmentEntry(id);
    	if(null==e)
    	{
    		return RespObj.FAILD;
    	}
    	SessionValue sv =getSessionValue();
    	if(!e.getSchoolId().equals(new ObjectId(sv.getSchoolId())))
    	{
    		return RespObj.FAILD;
    	}
    	schoolService.removeDepartment(id);
    	return RespObj.SUCCESS;
    }
    

    /**
     * 学校部门详情
     * @return
     * @throws IllegalParamException 
     */
    @RequestMapping("/department/detail")
    @ResponseBody
    public DepartmentDTO schoolDepartmentDetail(@ObjectIdType ObjectId id) throws IllegalParamException
    {
    	DepartmentEntry e=schoolService.getDepartmentEntry(id);
    	if(null==e)
    	{
    		throw new IllegalParamException();
    	}
    
    	DepartmentDTO dto =new DepartmentDTO();
    	IdValuePairDTO dep =new IdValuePairDTO(e.getID(), e.getName());
    	
    	Map<ObjectId, UserEntry> userMap =userService.getUserEntryMap(e.getMembers(), Constant.FIELDS);
    	
    	List<IdValuePairDTO> dtoList =new ArrayList<IdValuePairDTO>();
    	for(Map.Entry<ObjectId, UserEntry> entry:userMap.entrySet())
    	{
    		dtoList.add(new IdValuePairDTO(entry.getKey(),entry.getValue().getUserName()));
    	}
    	
		
		Collections.sort(dtoList,new Comparator<IdValuePairDTO>(){
			public int compare(IdValuePairDTO arg0,IdValuePairDTO arg1)
			{
				Collator cmp = Collator.getInstance(java.util.Locale.CHINA);
				if (cmp.compare(arg0.getValue().toString(), arg1.getValue().toString())>0){
					return 1;
				}else if (cmp.compare(arg0.getValue().toString(), arg1.getValue().toString())<0){
					return -1;
				}
				return 0;
			}
		});
	
    	
    	dto.setDep(dep);
    	dto.setMember(dtoList);
    	return dto;
    }
    
    /**
     * 部门添加成员
     * @return
     * @throws IllegalParamException 
     */
    @RequestMapping("/department/member/add")
    @ResponseBody
    public RespObj addDepMember(@ObjectIdType ObjectId id,String uid) throws IllegalParamException
    {
    	
    	logger.info("addDepMember "+getUserId()+" uid="+uid);
    	RespObj obj =new RespObj(Constant.FAILD_CODE);
    	DepartmentEntry e=schoolService.getDepartmentEntry(id);
    	if(null==e)
    	{
    		obj.setMessage("参数错误");
    		return obj;
    	}
    
    	SessionValue sv =getSessionValue();
    	if(!e.getSchoolId().equals(new ObjectId(sv.getSchoolId())))
    	{
    		obj.setMessage("参数错误");
    		return obj;
    	}
        String[] userIds  =uid.split(",");
        for(String userId : userIds){
            UserEntry ue=userService.searchUserId(new ObjectId(userId));
            if(null==ue)
            {
                obj.setMessage("没有找到该用户");
                return obj;
            }
            if(!ue.getSchoolID().equals(e.getSchoolId()))
            {
                obj.setMessage("不是本校用户");
                return obj;
            }

            schoolService.addMember(id, new ObjectId(userId));
        }
    	

    	
    	return RespObj.SUCCESS;
    }
    
    /**
     * 删除部门成员
     * @return
     * @throws IllegalParamException 
     */
    @RequestMapping("/department/member/del")
    @ResponseBody
    public RespObj delDepMember(@ObjectIdType ObjectId id,@ObjectIdType ObjectId memberId) throws IllegalParamException
    {
    	logger.info("delDepMember "+getUserId()+" memberId="+memberId);

    	DepartmentEntry e=schoolService.getDepartmentEntry(id);
    	if(null==e)
    	{
    		return RespObj.FAILD;
    	}
    
    	SessionValue sv =getSessionValue();
    	if(!e.getSchoolId().equals(new ObjectId(sv.getSchoolId())))
    	{
    		return RespObj.FAILD;
    	}
    	schoolService.removeMember(id, memberId);
    	return RespObj.SUCCESS;
    }
    
    /**
     * 部门文件列表
     * @param id
     * @return
     */
    @RequestMapping("/department/file/list")
    @ResponseBody
    public List<DepartmentFileDTO> getDepartmentFileDTOs(@ObjectIdType ObjectId id)
    {
    	return departmentService.getDepartmentFileDTOs(id);
    }
    
    /**
     * 部门人员列表
     * @param id
     * @return
     */
    @RequestMapping("/department/member/list")
    @ResponseBody
    public List<NameValuePair> getDepartmentUsers(@ObjectIdType ObjectId id)
    {
    	return departmentService.getDepartmentUser(id);
    }
    
    
    /**
     * 部门文件
     * @return
     */
    @RequestMapping("/department/file")
    public String departmentFile(@ObjectIdType ObjectId id,Map<String,Object> model)
    {
    	 model.put("id", id.toString());
    	 return "myschool/department_file";
    }
    /**
     * 添加文件
     * @param id
     * @param
     * @return
     * @throws IOException 
     */
    @RequestMapping("/department/file/add")
    @ResponseBody
    public RespObj addDepartmentFile(@ObjectIdType ObjectId id,MultipartFile file,HttpServletRequest req) 
    {
    	if(null==file || StringUtils.isBlank(file.getOriginalFilename()))
    	{
    		return RespObj.FAILD;
    	}
    	
    	RespObj obj =new RespObj(Constant.FAILD_CODE);
    	DepartmentEntry e=schoolService.getDepartmentEntry(id);
    	Set<ObjectId> userSet =new HashSet<ObjectId>(e.getMembers());
    	if(!userSet.contains(getUserId()))
    	{
    		obj.setMessage("没有权限");
    		return obj;
    	}
    	ObjectId fileKeyId=new ObjectId();
    	String path=req.getServletContext().getRealPath("/upload/departFiles");
        File localFile =new File(path, fileKeyId.toString()+"_"+file.getOriginalFilename());
        try {
        	if(!localFile.exists())
        	{
        		localFile.createNewFile();
        	}
			FileUtils.copyInputStreamToFile(file.getInputStream(), localFile);
		} catch (IOException e1) {
			obj.setMessage("文件操作错误");
    		return obj;
		}
        String fileKey=fileKeyId.toString()+"."+FilenameUtils.getExtension(file.getOriginalFilename());
        try {
			QiniuFileUtils.uploadFile(fileKey, new FileInputStream(localFile), QiniuFileUtils.TYPE_DOCUMENT);
		} catch  (Exception e1) {
			obj.setMessage("文件操作错误");
			return obj;
		}
        DepartmentFile dFile =new DepartmentFile(file.getOriginalFilename(), getUserId(), fileKey, file.getSize());
        departmentService.addDepartFile(id, dFile);
        return RespObj.SUCCESS;
    }
    
    /**
     * 删除文件
     * @param id
     * @param
     * @return
     * @throws IOException 
     */
    @RequestMapping("/department/file/remove")
    @ResponseBody
    public RespObj addDepartmentFile(@ObjectIdType ObjectId id,@ObjectIdType ObjectId fileId) 
    {
    	
    	logger.info("addDepartmentFile "+getUserId()+" id="+id);
    	RespObj obj =new RespObj(Constant.FAILD_CODE);
    	DepartmentEntry e=schoolService.getDepartmentEntry(id);
    	Set<ObjectId> userSet =new HashSet<ObjectId>(e.getMembers());
    	if(!userSet.contains(getUserId()))
    	{
    		obj.setMessage("没有权限");
    		return obj;
    	}
    	DepartmentFile file=null;
    	for(DepartmentFile dFile:e.getDepartmentFiles())
    	{
    		if(dFile.getId().equals(fileId))
    		{
    			file=dFile;
    			break;
    		}
    	}
    	
    	if(null==file)
    	{
    		obj.setMessage("参数错误");
    		return obj;
    	}
    	
    	if(!file.getUserId().equals(getUserId()))
    	{
    		obj.setMessage("没有权限");
    		return obj;
    	}
    	
        departmentService.removeDepartFile(id, file);
        return RespObj.SUCCESS;
    }
    /**
     * 下载文件
     * @return
     * @throws UnsupportedEncodingException 
     */
    @RequestMapping("/department/file/down")
    @ResponseBody
    public String downDepartmentFile(@ObjectIdType ObjectId fileId,HttpServletResponse response) throws UnsupportedEncodingException 
    {
    	DepartmentEntry e=schoolService.getDepartmentEntryByFileId(fileId);
    	Set<ObjectId> userSet =new HashSet<ObjectId>(e.getMembers());
    	if(!userSet.contains(getUserId()))
    	{
    		return null;
    	}
    	DepartmentFile file=null;
    	for(DepartmentFile dFile:e.getDepartmentFiles())
    	{
    		if(dFile.getId().equals(fileId))
    		{
    			file=dFile;
    			break;
    		}
    	}
    	if(null==file)
    	{
    		return null;
    	}
        String qiniuPath=QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, file.getPath());
		response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
		response.setHeader( "Content-Disposition", "attachment;filename=" + new String( file.getName().getBytes("utf-8"), "ISO8859-1" ) );
        
        try {
            InputStream inputStream = QiniuFileUtils.downFileByUrl(qiniuPath);
            OutputStream os = response.getOutputStream();
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
            os.close();
            inputStream.close();
        }  catch (IOException ex) {
        }
        return null;
    }
    
    /**
     * 排课页面加载
     * @return
     */
    @RequestMapping("/load/paike")
    public String loadPaike()
    {
    	return "/scheduling/scheduling";
    }
    
    /**
     * 排课
     * @param response
     * @return
     * @throws IOException 
     * @throws PermissionUnallowedException 
     */
    @RequestMapping("/paike")
    public String paike(HttpServletResponse response,HttpServletRequest req) throws IOException, PermissionUnallowedException 
    {
    	SchoolEntry se=schoolService.getSchoolEntry( new ObjectId(getSessionValue().getSchoolId()),Constant.FIELDS);
    	if(se.getName().indexOf("阜阳市第二十一中学")<0 && se.getName().indexOf("和谐路")<0 )
    	{
    		throw new PermissionUnallowedException("no permission to download paike!!!");
    	}
    	String file="排课.[阜阳市第二十一中学].zip";
    	if(se.getName().indexOf("和")>0)
    	{
    		file="排课.[和谐路小学].zip";
    	}
 		response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
 		response.setHeader( "Content-Disposition", "attachment;filename=" + new String( file.getBytes("utf-8"), "ISO8859-1" ) );
        try {
        	 String uploadpath=req.getServletContext().getRealPath("/upload/resources");
        	 File f= new File(uploadpath, file);
             InputStream inputStream = new FileInputStream(f);
             OutputStream os = response.getOutputStream();
             byte[] b = new byte[2048];
             int length;
             while ((length = inputStream.read(b)) > 0) {
                 os.write(b, 0, length);
             }
             os.flush();
             os.close();
             inputStream.close();
         }  catch (IOException ex) {
         }
        return null;
    }


//==================================================管理校区===============================
    @RequestMapping("/manageCampus")
    public String manageCampus() {
        return "myschool/manage-campus";
    }

    /**
     * 列表
     * @return
     */
    @RequestMapping("/listCampus")
    @ResponseBody
    public Map<String, Object> listCampus() {
        Map<String, Object> model = new HashMap<String, Object>();
        List<CampusDTO> campusDTOList = new ArrayList<CampusDTO>();
        List<CampusEntry> campusEntryList = campusService.list();
        for(CampusEntry campusEntry : campusEntryList) {
            CampusDTO campusDTO = new CampusDTO();
            campusDTO.setCampusId(campusEntry.getID().toString());
            campusDTO.setName(campusEntry.getName());
            campusDTO.setAddr(campusEntry.getAddr());
            campusDTO.setPhone(campusEntry.getPhone());
            campusDTO.setManager(campusEntry.getManager());
            campusDTOList.add(campusDTO);
        }
        model.put("campusList", campusDTOList);
        return model;
    }

    /**
     * 添加
     * @param name
     * @param addr
     * @param phone
     * @param manager
     * @return
     */
    @RequestMapping("/addCampus")
    @ResponseBody
    public boolean addCampus(String name, String addr, String phone, String manager) {
        CampusEntry campusEntry = new CampusEntry(name, addr, phone, manager);
        campusService.add(campusEntry);
        return true;
    }

    /**
     * 编辑回显
     * @param campusId
     * @return
     */
    @RequestMapping("/getCampusInfo")
    @ResponseBody
    public Map<String, Object> getCampusInfo(String campusId) {
        Map<String, Object> model = new HashMap<String, Object>();
        CampusEntry campusEntry = campusService.getCampusInfo(new ObjectId(campusId));
        model.put("name", campusEntry.getName());
        model.put("addr", campusEntry.getAddr());
        model.put("phone", campusEntry.getPhone());
        model.put("manager", campusEntry.getManager());
        return model;
    }
    /**
     * 编辑
     * @param campusId
     * @param name
     * @param addr
     * @param phone
     * @param manager
     * @return
     */
    @RequestMapping("/editCampus")
    @ResponseBody
    public boolean editCampus(String campusId, String name, String addr, String phone, String manager) {
        CampusEntry campusEntry = new CampusEntry(name, addr, phone, manager);
        campusService.edit(new ObjectId(campusId),campusEntry);
        return true;
    }

    /**
     * 删除
     * @param campusId
     * @return
     */
    @RequestMapping("/deleteCampus")
    @ResponseBody
    public boolean deleteCampus(String campusId) {
    	logger.info("deleteCampus "+getUserId()+" campusId="+campusId);
        campusService.delete(new ObjectId(campusId));
        return true;
    }

    /**
     * 跳转学生选课统计页面
     * @return
     */
    @RequestMapping("stuinteclasscountpage")
    public String stuInteClassCountPage2(Map<String, Object> model)
            throws IllegalParamException{
        String schoolId = getSessionValue().getSchoolId();
        /*List<ClassInfoDTO>  classInfoList = classService.getSimpleClassInfoDTOs(getUserId(), new ObjectId(schoolId));
        Map<String,GradeView> gradeMap=new HashMap<String,GradeView>();
        for(ClassInfoDTO ciDto:classInfoList){
            GradeView gradeView=new GradeView();
            gradeView.setId(ciDto.getGradeId());
            String gradeName=ciDto.getGradeName()==null?"":ciDto.getGradeName();
            gradeView.setName(gradeName);
            gradeMap.put(ciDto.getGradeId(),gradeView);
        }
        List<GradeView> gradeList = new ArrayList<GradeView>();
        for (GradeView grade : gradeMap.values()) {
            gradeList.add(grade);
        }*/

        List<GradeView> gradeList=schoolService.searchSchoolGradeList(schoolId);
        sortGradeList(gradeList);
        model.put("schoolId", schoolId);
        model.put("gradeList", gradeList);
        return "myschool/curriculaI";
    }
    /**
     * 对班级list进行排序
     * @param list
     */
    private void sortGradeList(List<GradeView> list){
        Collections.sort(list, new Comparator<GradeView>() {
            public int compare(GradeView obj1, GradeView obj2) {
                int flag = obj1.getName().compareTo(obj2.getName());
                return flag;
            }
        });
    }

    /**
     * 获取年级的班级
     * @param gradeId
     * @return Map
     */
    @RequestMapping("/getGradeClassValue")
    public @ResponseBody Map getGradeClassValue(@RequestParam("gradeId") String gradeId) throws IllegalParamException{
        //获取用户角色
        List<ClassInfoDTO>  classInfoList =new ArrayList<ClassInfoDTO>();
        Map map = new HashMap();
        //获取年级的班级
        String schoolId = getSessionValue().getSchoolId();

        List<ClassInfoDTO>  list = classService.getSimpleClassInfoDTOs(getUserId(), new ObjectId(schoolId));
        for(ClassInfoDTO dto: list){
            String className=dto.getClassName()==null?"":dto.getClassName();
            dto.setClassName(className);
            if(gradeId.equals(dto.getGradeId())){
                classInfoList.add(dto);
            }
        }
        sortClassList(classInfoList);
        map.put("classList",classInfoList);
        return map;
    }

    /**
     * 获取年级的班级
     * @param gradeId
     * @return Map
     */
    @RequestMapping("/getClassValue")
    public @ResponseBody Map getClassValue(@RequestParam("gradeId") String gradeId) throws IllegalParamException{
        //获取用户角色
        List<ClassInfoDTO>  classInfoList =new ArrayList<ClassInfoDTO>();
        Map map = new HashMap();
        //获取年级的班级
        String schoolId = getSessionValue().getSchoolId();
        List<ClassInfoDTO> list = new ArrayList<ClassInfoDTO>();
        if (StringUtils.isEmpty(gradeId)) {
            List<ClassEntry> cls = classService.findClassInfoBySchoolId(new ObjectId(schoolId), null);
            if (cls!=null && cls.size()!=0) {
                for (ClassEntry ce : cls) {
                    list.add(new ClassInfoDTO(ce));
                }
            }

        } else {
            list = classService.findClassByGradeId(gradeId);
        }
        for(ClassInfoDTO dto: list){
            String className=dto.getClassName()==null?"":dto.getClassName();
            dto.setClassName(className);
//            if(gradeId.equals(dto.getGradeId())){
                classInfoList.add(dto);
//            }
        }
        sortClassList(classInfoList);
        map.put("classList",classInfoList);
        return map;
    }

    /**
     * 对班级list进行排序
     * @param list
     */
    private void sortClassList(List<ClassInfoDTO> list){
        Collections.sort(list, new Comparator<ClassInfoDTO>() {
            public int compare(ClassInfoDTO obj1 , ClassInfoDTO obj2) {
                int flag=obj1.getClassName().compareTo(obj2.getClassName());
                return flag;
            }
        });
    }
    
    
    /**
     * 检查一个两个名字是否同一学校老师；用于老师找回密码
     * @param userName
     * @param checkName
     * @return
     */
	@SessionNeedless
	@RequestMapping("/checkName")
	@ResponseBody
    public RespObj chechTeacherName(String userName,String checkName)
    {
        RespObj obj =new RespObj(Constant.FAILD_CODE);
		
		if(StringUtils.isBlank(checkName) || StringUtils.isBlank(userName) || userName.equalsIgnoreCase(checkName))
		{
			obj.setMessage("参数错误");
			return obj;
		}
		if(checkName.length()<2)
		{
			obj.setMessage("参数错误");
			return obj;
		}
		UserEntry e=userService.searchUserByUserName(userName);
		if(null==e)
		{
			obj.setMessage("没有找到用户");
			return obj;
		}
		if(!UserRole.isTeacher(e.getRole()))
		{
			obj.setMessage("权限错误");
			return obj;
		}
		
		List<UserEntry> list=userService.getTeacherEntryBySchoolId(e.getSchoolID(), Constant.FIELDS);
		
		
		for(UserEntry ue:list)
		{
			
				if(UserRole.isNotStudentAndParent(ue.getRole()))
				{
					if(ue.getUserName().toLowerCase().startsWith(checkName.toLowerCase()))
					{
						return RespObj.SUCCESS;
					}
				}
		}
		return RespObj.FAILD;
    }

}

