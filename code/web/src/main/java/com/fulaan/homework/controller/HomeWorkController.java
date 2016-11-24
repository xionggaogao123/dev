package com.fulaan.homework.controller;

import com.fulaan.annotation.ObjectIdType;
import com.fulaan.annotation.SessionNeedless;
import com.fulaan.annotation.UserRoles;
import com.fulaan.base.controller.BaseController;
import com.fulaan.base.service.DirService;
import com.fulaan.cache.CacheHandler;
import com.fulaan.examresult.service.ExamResultService;
import com.fulaan.experience.service.ExperienceService;
import com.fulaan.groupdiscussion.service.EaseMobService;
import com.fulaan.homework.dto.SubmitInfoDTO;
import com.fulaan.learningcenter.controller.LessonController;
import com.fulaan.learningcenter.service.ExerciseItemService;
import com.fulaan.learningcenter.service.ExerciseService;
import com.fulaan.learningcenter.service.HomeWorkService;
import com.fulaan.learningcenter.service.LessonService;
import com.fulaan.letter.controller.LetterController;
import com.fulaan.letter.service.LetterService;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.controller.TeacherSubjectView;
import com.fulaan.myschool.service.InterestClassService;
import com.fulaan.reminder.service.ReminderService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.school.service.TeacherClassSubjectService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.JPushUtils;
import com.fulaan.utils.QiniuFileUtils;
import com.fulaan.utils.SpringContextUtil;
import com.fulaan.zouban.dto.ZouBanCourseDTO;
import com.mongodb.BasicDBObject;
import com.pojo.app.*;
import com.pojo.lesson.DirEntry;
import com.pojo.lesson.DirType;
import com.pojo.lesson.LessonEntry;
import com.pojo.lesson.LessonWare;
import com.pojo.letter.LetterEntry;
import com.pojo.reminder.ReminderEntry;
import com.pojo.school.*;
import com.pojo.school.HomeworkDTO.StudentSubmitHomeWorkDTO;
import com.pojo.user.*;
import com.pojo.utils.MongoUtils;
import com.pojo.zouban.ZouBanCourseEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.PermissionUnallowedException;
import com.sys.exceptions.ResultTooManyException;
import com.sys.utils.AvatarUtils;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.HtmlUtils;
import com.sys.utils.RespObj;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * 作业controller
 *
 * @author fourer
 */
@Controller
@RequestMapping("/homework")
public class HomeWorkController extends BaseController {

    private static final Logger logger = Logger.getLogger(HomeWorkController.class);


    private HomeWorkService homeWorkService = new HomeWorkService();
    private TeacherClassSubjectService teacherClassLessonService = new TeacherClassSubjectService();
    private UserService userService = new UserService();
    private ExperienceService experienceService = new ExperienceService();
    private ClassService classService = new ClassService();
    private LetterService letterService = new LetterService();
    @Autowired
    private EaseMobService easeMobService;
    @Autowired
    private LessonController lessonController;
    @Autowired
    private TeacherClassSubjectService teacherClassSubjectService;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private ExerciseService exerciseService;
    @Autowired
    private DirService dirService;
    @Autowired
    private ExamResultService examResultService;
    @Autowired
    private ExerciseItemService exerciseItemService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private ReminderService reminderService;
    @Autowired
    private InterestClassService interestClassService;


    private final JPushUtils pushUtils = new JPushUtils();

    /**
     * 老师的首页
     *
     * @return
     * @throws IOException
     */
    @RequestMapping("/teacher")
    public String teacher(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return "homework/teacherindex";
    }

    /**
     * 老师批改
     *
     * @return
     */
    @RequestMapping("/teacher/detail")
    public String teacherDetail() {
        return "homework/teacherdetail";
    }


    /**
     * 学生首页
     *
     * @return
     */
    @RequestMapping("/student")
    public String student() {
        return "homework/studentindex";
    }

    /**
     * 学生详情
     *
     * @return
     */
    @RequestMapping({"/student/detail", "/teacher/detail1"})
    public String studentDetail(@ObjectIdType ObjectId lessonId, Map<String, Object> model, HttpServletRequest req) {
        LessonController lessonController = new LessonController();
        try {
            lessonController.viewLesson(lessonId, model, req);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (model.get("type").equals("txt")) {//文本
            return "homework/studentdetail_text";
        } else if (model.get("type").equals("swf")) {//swf
            return "homework/studentdetail_swf";
        }
        return "homework/studentdetail";
    }

    /**
     * 得到作业的提示数量
     *
     * @return
     */
    @RequestMapping("/getHWReminderCount")
    @ResponseBody
    public Map<String, Object> getHomeworkReminderCount() {
        Map<String, Object> model = new HashMap<String, Object>();
        int reminder = reminderService.findHWReminderCount(getUserId(), null, null, null);
        model.put("reminder", reminder);
        return model;
    }


    /**
     * 老师编辑作业时回显视频课程数据
     *
     * @param lessonId 作业中视频课程的ID
     * @param model
     * @return
     */
    @UserRoles(noValue = {UserRole.STUDENT, UserRole.PARENT})
    @RequestMapping("/teacher/edit")
    public String editHomework(@ObjectIdType ObjectId lessonId, Map<String, Object> model) {
        LessonEntry e = lessonService.getLessonEntry(lessonId);
        if (e.getDirId() != null && !e.getDirId().toString().equals("000000000000000000000000")) {
            DirEntry dirEntry = dirService.getDirEntry(e.getDirId(), null);
            //所在文件夹的type
            model.put("dirType", DirType.getDirType(dirEntry.getType()).toString());
        }
        //该课程被推送次数
        model.put("pushCount", e.getPushCount());

        if (e.getExercise() != null) {
            model.put("exerciseId", e.getExercise().toString());
            String exename = exerciseService.getExerciseEntry(e.getExercise()).getName();
            model.put("exerciseName", exename);
        }
        return "homework/edithomework";
    }

    /**
     * 老师首页筛选条件
     *
     * @return
     */
    @UserRoles(noValue = {UserRole.STUDENT, UserRole.PARENT})
    @RequestMapping("/teacher/section")
    @ResponseBody
    public Map<String, Object> getClassSubject() {
        Map<String, Object> model = new HashMap<String, Object>();
        ObjectId uid = getUserId();
        String cacheKey = CacheHandler.getKeyString(CacheHandler.CACHE_HWSECTION_KEY, String.valueOf(uid));
        String value = CacheHandler.getStringValue(cacheKey);
        if (StringUtils.isBlank(value)) {
//        if(true){
            ObjectId teacherId = getUserId();
            UserDetailInfoDTO teacherInfo = userService.getUserInfoById(teacherId.toString());
            List<ObjectId> classIds = new ArrayList<ObjectId>();
            //通过老师的id查询所教班级和科目//不包括兴趣班
            List<ClassInfoDTO> classInfoDTOList = classService.findClassInfoByTeacherId(teacherId);
            Map<String, ClassInfoDTO> classMap = new HashMap<String, ClassInfoDTO>();
            for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
                String classId = classInfoDTO.getId();
                classIds.add(new ObjectId(classId));
                classMap.put(classId, classInfoDTO);
            }
            List<TeacherClassSubjectDTO> teacherClassSubjectDTOList = teacherClassSubjectService.getTeacherClassSubjectDTOList(teacherId, classIds);
            List<Map<String, Object>> classSubjuectList = new ArrayList<Map<String, Object>>();
            for (TeacherClassSubjectDTO tcsDTO : teacherClassSubjectDTOList) {
                Map<String, Object> info = new HashMap<String, Object>();
                IdValuePairDTO classInfo = tcsDTO.getClassInfo();
                ObjectId classId = classInfo.getId();
                String className = (String) classInfo.getValue();
                IdValuePairDTO subjectInfo = tcsDTO.getSubjectInfo();
                ObjectId subjectId = subjectInfo.getId();
                String subjectName = (String) subjectInfo.getValue();
                info.put("classId", classId.toString());
                info.put("className", className);
                info.put("subjectId", subjectId.toString());
                info.put("subjectName", subjectName);
                info.put("classType", 1);
                int reminder = reminderService.findHWReminderCount(getUserId(), classId, subjectId, null);
                info.put("reminder", reminder);
                //新增老师头像、作业数、班级人数
                info.put("teacherAvatar", teacherInfo.getImgUrl());
                ClassInfoDTO classInfoDTO = classMap.get(classId.toString());
                info.put("studentCount", classInfoDTO.getStudentCount());
                try {
                    int homeworkCount = homeWorkService.countHomeWorkEntrys(teacherId, classId, 3, subjectId, 1, 0);
                    info.put("homeworkCount", homeworkCount);
                } catch (ResultTooManyException e) {
                    e.printStackTrace();
                }
                classSubjuectList.add(info);
            }
            //通过老师id查询所教兴趣班
            List<InterestClassDTO> interestClassDTOList = schoolService.findInterestClassByTeacherId(getSessionValue().getId(), -1, getSessionValue().getSchoolId());
            if (interestClassDTOList != null) {
                for (InterestClassDTO interestClassDTO : interestClassDTOList) {
                    Map<String, Object> info = new HashMap<String, Object>();
                    info.put("classId", interestClassDTO.getId());
                    info.put("className", interestClassDTO.getClassName());
                    info.put("subjectId", interestClassDTO.getSubjectId());
                    info.put("subjectName", "兴趣班");
                    info.put("classType", 2);
                    List<FieldValuePair> lesson = classService.getLessonIndexAndName(new ObjectId(interestClassDTO.getId()), -1);
                    info.put("lesson", lesson);
                    int reminder = reminderService.findHWReminderCount(getUserId(), new ObjectId(interestClassDTO.getId()), new ObjectId(interestClassDTO.getSubjectId()), null);
                    info.put("reminder", reminder);
                    //新增老师头像、作业数、班级人数
                    info.put("teacherAvatar", teacherInfo.getImgUrl());
                    info.put("studentCount", interestClassDTO.getStudentCount());
                    try {
                        int homeworkCount = homeWorkService.countHomeWorkEntrys(teacherId, new ObjectId(interestClassDTO.getId()), 3, new ObjectId("000000000000000000000000"), 0, 0);
                        info.put("homeworkCount", homeworkCount);
                    } catch (ResultTooManyException e) {
                        e.printStackTrace();
                    }
                    classSubjuectList.add(info);
                }
            }
            //通过老师id查询所有非走班课和选修课
            List<ZouBanCourseDTO> zoubanCourseDTOList = interestClassService.findZoubanCourseByTeacherId(teacherId);
            if (zoubanCourseDTOList != null) {
                for (ZouBanCourseDTO zouBanCourseDTO : zoubanCourseDTOList) {
                    Map<String, Object> info = new HashMap<String, Object>();
                    info.put("classId", zouBanCourseDTO.getZbCourseId());
                    info.put("className", zouBanCourseDTO.getClassName());
                    info.put("subjectId", zouBanCourseDTO.getSubjectId());
                    info.put("subjectName", "选修课");
                    info.put("classType", 3);
                    int reminder = reminderService.findHWReminderCount(getUserId(), new ObjectId(zouBanCourseDTO.getZbCourseId()), new ObjectId(zouBanCourseDTO.getSubjectId()), null);
                    info.put("reminder", reminder);
                    //新增老师头像、作业数、班级人数
                    info.put("teacherAvatar", teacherInfo.getImgUrl());
                    info.put("studentCount", zouBanCourseDTO.getStudentsCount());
                    try {
                        int homeworkCount = homeWorkService.countHomeWorkEntrys(teacherId, new ObjectId(zouBanCourseDTO.getZbCourseId()), 3, new ObjectId("000000000000000000000000"), 0, 0);
                        info.put("homeworkCount", homeworkCount);
                    } catch (ResultTooManyException e) {
                        e.printStackTrace();
                    }
                    classSubjuectList.add(info);
                }
            }

            if (teacherClassSubjectDTOList.size() > 0) {
                model.put("classType", 1);
            } else if(interestClassDTOList.size() > 0) {
                model.put("classType", 2);
            } else {
                model.put("classType",3);
            }
            model.put("classSubjectList", classSubjuectList);
            JSONObject data = new JSONObject(model);
            CacheHandler.cache(cacheKey, data.toString(), Constant.SESSION_FIVE_MINUTE);
            return model;
        } else {
            try {
                JSONObject jsonObject = new JSONObject(value);
                model = getTeacherSectionData(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return model;
        }
    }

    /**
     * 反序列化教师筛选数据
     *
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    public Map<String, Object> getTeacherSectionData(JSONObject jsonObject) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("classType", jsonObject.get("classType"));
        JSONArray classSubjectList = jsonObject.getJSONArray("classSubjectList");
        List<Map<String, Object>> classSubList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < classSubjectList.length(); i++) {
            JSONObject classSubject = classSubjectList.getJSONObject(i);
            Map<String, Object> info = new HashMap<String, Object>();
            info.put("classId", classSubject.get("classId"));
            info.put("className", classSubject.get("className"));
            info.put("subjectId", classSubject.get("subjectId"));
            info.put("subjectName", classSubject.get("subjectName"));
            info.put("classType", classSubject.get("classType"));
            info.put("teacherAvatar", classSubject.get("teacherAvatar"));
            info.put("studentCount", classSubject.get("studentCount"));
            info.put("homeworkCount", classSubject.get("homeworkCount"));
            int reminder = reminderService.findHWReminderCount(getUserId(), new ObjectId((String) info.get("classId")), new ObjectId((String) info.get("subjectId")), null);
            info.put("reminder", reminder);
//            info.put("reminder", classSubject.get("reminder"));
            if ((Integer) classSubject.get("classType") == 2) {
                List<FieldValuePair> lesson = classService.getLessonIndexAndName(new ObjectId((String) classSubject.get("classId")), -1);
                info.put("lesson", lesson);
            }
            classSubList.add(info);
        }
        map.put("classSubjectList", classSubList);
        return map;
    }

    /**
     * 得到学生ID
     *
     * @return
     */
    private ObjectId getStudentId() {
        String userId = getSessionValue().getId();
        UserDetailInfoDTO userInfoDTO4WB = userService.getUserInfoById(userId);
        if (UserRole.isParent(userInfoDTO4WB.getRole())) {//如果是家长转换成学生
            userInfoDTO4WB = userService.findStuInfoByParentId(userId);
        }
        return new ObjectId(userInfoDTO4WB.getId());
    }

    /**
     * 学生首页筛选条件
     *
     * @return
     */
    @RequestMapping("/student/section")
    @ResponseBody
    public Map<String, Object> getSubjectList() {
        Map<String, Object> model = new HashMap<String, Object>();
        ObjectId uid = getUserId();
        String cacheKey = CacheHandler.getKeyString(CacheHandler.CACHE_HWSECTION_KEY, String.valueOf(uid));
        String value = CacheHandler.getStringValue(cacheKey);
        if (StringUtils.isBlank(value)) {
//        if(true){
            //查询班级的所有科目
            List<Map<String, Object>> classSubjectList = new ArrayList<Map<String, Object>>();
            List<ClassInfoDTO> classInfoDTOList = classService.findClassInfoByStuId(getStudentId());
            Collections.reverse(classInfoDTOList);
            model.put("classType", 3);
            model.put("classId", "");
            for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
                if (classInfoDTO.getClassType() == 1) {//主班
                    String classId = classInfoDTO.getId();
                    model.put("classType", 1);
                    List<ObjectId> objectIdList = classService.findTeacherIdsById(new ObjectId(classId));
                    if (objectIdList != null && !objectIdList.isEmpty()) {
                        List<TeacherSubjectView> teacherSubjectViewList = classService.findTeacherByIds(objectIdList, classId);
                        for (TeacherSubjectView teacherSubjectView : teacherSubjectViewList) {
                            try {
                                Map<String, Object> info = new HashMap<String, Object>();
                                info.put("id", classId);
                                info.put("name", teacherSubjectView.getSubjectName());
                                info.put("value", teacherSubjectView.getSubjectId());
                                int reminder = reminderService.findHWReminderCount(getUserId(), new ObjectId(classId), new ObjectId(teacherSubjectView.getSubjectId()), null);
                                info.put("reminder", reminder);
                                //新增
                                info.put("className", classInfoDTO.getClassName());
                                info.put("studentCount", classInfoDTO.getStudentCount());


                                UserDetailInfoDTO teacherInfo = userService.getUserInfoById(teacherSubjectView.getTeacherId());
                                info.put("teacherAvatar", teacherInfo.getImgUrl());
                                int homeworkCount = homeWorkService.countHomeWorkEntrys(null,
                                        new ObjectId(classId), 3, new ObjectId(teacherSubjectView.getSubjectId()), 1, 0);
                                info.put("homeworkCount", homeworkCount);
                                classSubjectList.add(info);
                            } catch (Exception ex) {

                            }
                        }
                    }
                } else if (classInfoDTO.getClassType() == 2) {//兴趣班
//                    IdNameValuePairDTO classSubject = new IdNameValuePairDTO(classInfoDTO.getId(), classInfoDTO.getId(), "000000000000000000000000");
                    if((Integer)model.get("classType") != 1)
                        model.put("classType",2);
                    InterestClassDTO interestClassEntry = interestClassService.findInterestClassByClassId(classInfoDTO.getId());
                    List<InterestClassStudentDTO> studentList = interestClassEntry.getCurrentStudentList();
                    boolean isNow = false;
                    if (null != studentList) {
                        for (InterestClassStudentDTO dto : studentList) {
                            if (dto.getStudentId().equals(uid.toString())) {
                                isNow = true;
                                break;
                            }
                        }
                    }
                    if (isNow) {//本学期兴趣班
                        Map<String, Object> info = new HashMap<String, Object>();
                        info.put("id", classInfoDTO.getId());
                        info.put("name", classInfoDTO.getClassName());
                        info.put("value", "000000000000000000000000");
                        int reminder = reminderService.findHWReminderCount(getUserId(), new ObjectId(classInfoDTO.getId()), null, null);
                        info.put("reminder", reminder);
                        //新增
                        info.put("className", classInfoDTO.getClassName());
                        info.put("studentCount", classInfoDTO.getStudentCount());
                        UserDetailInfoDTO teacherInfo = userService.getUserInfoById(interestClassEntry.getTeacherId());
                        info.put("teacherAvatar", teacherInfo.getImgUrl());
                        try {
                            int homeworkCount = homeWorkService.countHomeWorkEntrys(new ObjectId(interestClassEntry.getTeacherId()), new ObjectId(classInfoDTO.getId()), 3, new ObjectId("000000000000000000000000"), 0, 0);
                            info.put("homeworkCount", homeworkCount);
                        } catch (ResultTooManyException e) {
                            e.printStackTrace();
                        }
                        classSubjectList.add(info);
                    }
                }
            }

            List<ZouBanCourseDTO> zouBanCourseDTOList = interestClassService.getStudentCourse(getUserId());
            for(ZouBanCourseDTO zouBanCourseDTO : zouBanCourseDTOList){
                Map<String,Object> info = new HashMap<String, Object>();
                info.put("id",zouBanCourseDTO.getZbCourseId());
                info.put("name",zouBanCourseDTO.getClassName());
                info.put("value",zouBanCourseDTO.getSubjectId());
                int reminder = reminderService.findHWReminderCount(getUserId(), new ObjectId(zouBanCourseDTO.getZbCourseId()), null, null);
                info.put("reminder", reminder);
                info.put("className",zouBanCourseDTO.getCourseName());
                info.put("studentCount",zouBanCourseDTO.getStudentsCount());

                UserDetailInfoDTO teacherInfo = userService.getUserInfoById(zouBanCourseDTO.getTeacherId());
                if(teacherInfo != null){
                    info.put("teacherAvatar", teacherInfo.getImgUrl());
                } else {
                  info.put("teacherAvatar", "");
                }
                try {
                    int homeworkCount = homeWorkService.countHomeWorkEntrys(new ObjectId(zouBanCourseDTO.getTeacherId()), new ObjectId(zouBanCourseDTO.getZbCourseId()), 3, new ObjectId("000000000000000000000000"), 0, 0);
                    info.put("homeworkCount", homeworkCount);
                } catch (ResultTooManyException e) {
                    e.printStackTrace();
                }
                classSubjectList.add(info);
            }

            if (classSubjectList != null && classSubjectList.size() > 0) {
                model.put("classId", classSubjectList.get(0).get("id"));
            }
            model.put("classSubjectList", classSubjectList);
            JSONObject data = new JSONObject(model);
            CacheHandler.cache(cacheKey, data.toString(), Constant.SESSION_FIVE_MINUTE);
            return model;
        } else {
            try {
                JSONObject jsonObject = new JSONObject(value);
                model = getStudentSectionData(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return model;
        }
    }

    /**
     * 反序列化学生筛选数据
     *
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    public Map<String, Object> getStudentSectionData(JSONObject jsonObject) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("classType", jsonObject.get("classType"));
        map.put("classId", jsonObject.get("classId"));
        JSONArray classSubjectList = jsonObject.getJSONArray("classSubjectList");
//        List<IdNameValuePairDTO> classSubList = new ArrayList<IdNameValuePairDTO>();
        List<Map<String, Object>> classSubList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < classSubjectList.length(); i++) {
            JSONObject classSubject = classSubjectList.getJSONObject(i);
//            IdNameValuePairDTO info = new IdNameValuePairDTO();
            Map<String, Object> info = new HashMap<String, Object>();
//            info.setId(classSubject.get("id"));
//            info.setName((String) classSubject.get("name"));
//            info.setValue(classSubject.get("value"));
            info.put("id", classSubject.get("id"));
            info.put("name", classSubject.get("name"));
            info.put("value", classSubject.get("value"));
            info.put("teacherAvatar", classSubject.get("teacherAvatar"));
            info.put("studentCount", classSubject.get("studentCount"));
            info.put("homeworkCount", classSubject.get("homeworkCount"));
            info.put("className", classSubject.get("className"));
            int reminder = 0;
            if (!((String) info.get("value")).equals("000000000000000000000000")) {//行政班
                reminder = reminderService.findHWReminderCount(getUserId(), new ObjectId((String) info.get("id")), new ObjectId((String) info.get("value")), null);
            } else {//兴趣班
                reminder = reminderService.findHWReminderCount(getUserId(), new ObjectId((String) info.get("id")), null, null);
            }
            info.put("reminder", reminder);
//            info.put("reminder",classSubject.get("reminder"));
            classSubList.add(info);
        }
        map.put("classSubjectList", classSubList);
        return map;
    }


    /**
     * 老师查看 “班级（学科）” 作业列表
     *
     * @param subjectId   科目ID
     * @param classId     班级ID
     * @param page        页数
     * @param pageSize    每页个数
     * @param type        0：课前 1：课后 2：其他 3：全部
     * @param term        0：本学期 1：全部
     * @param contentType 0：全部 1：有视频 2：有附件 3：有进阶练习 4：有语音
     * @return
     * @throws ResultTooManyException
     */
    @UserRoles(noValue = {UserRole.STUDENT, UserRole.PARENT})
    @RequestMapping("/teacher/list")
    @ResponseBody
    public Map<String, Object> getTeacherHomeWorks(@ObjectIdType @RequestParam(required = false, defaultValue = "000000000000000000000000") ObjectId subjectId,
                                                   @ObjectIdType ObjectId classId, int page, int pageSize,
                                                   @RequestParam(required = false, defaultValue = "3") int type,
                                                   @RequestParam(required = false, defaultValue = "0") int term,
                                                   @RequestParam(required = false, defaultValue = "0") int contentType) throws ResultTooManyException {
        ObjectId teacherId = getUserId();
        //校长
//        if(UserRole.isHeadmaster(getSessionValue().getUserRole()))
//        {
//            teacherId=null;
//        }
        Map<String, Object> model = new HashMap<String, Object>();
        //传入的page从1开始
        List<HomeWorkEntry> homeWorkEntryList = homeWorkService.getHomeWorkEntrys(teacherId, classId, page - 1, pageSize, type, subjectId, term, contentType);
        List<HomeworkDTO> homeworkDTOList = new ArrayList<HomeworkDTO>();
//        List<ObjectId> teacherIdList=new ArrayList<ObjectId>();

        int allStuNo = 0;
        if (homeWorkEntryList.size() > 0) {
            ClassEntry classEntry = classService.getClassEntryById(classId, Constant.FIELDS);
            if (classEntry != null) {
                allStuNo = classEntry.getStudents().size();
            } else {
                InterestClassEntry interestClassEntry = interestClassService.getInterestClassEntryById(classId);
                if(interestClassEntry != null){
                    allStuNo = interestClassEntry.getCurrentInterestClassStudents().size();
                } else {
                    ZouBanCourseEntry zouBanCourseEntry = interestClassService.getZoubanCourseEntry(classId);
                    if(zouBanCourseEntry != null){
                        allStuNo = zouBanCourseEntry.getStudentList().size();
                    }
                }
            }
        }



        for (HomeWorkEntry homeWorkEntry : homeWorkEntryList) {
            HomeworkDTO homeworkDTO = new HomeworkDTO(homeWorkEntry);
            homeworkDTO.setAllStuNo(allStuNo);
            homeworkDTO.setSubmitStuNo(getHomeWorkSubmitNo(classId, homeWorkEntry.getID()));
            homeworkDTOList.add(homeworkDTO);
            List<FieldValuePair> indexList = homeworkDTO.getClassLessonIndex();
            for (FieldValuePair dto : indexList) {
                if (dto.getField().equals(classId.toString())) {
                    homeworkDTO.setLessonIndex((Integer) dto.getValue());
                }
            }
//            teacherIdList.add(homeWorkEntry.getTeacherId());
        }
//        List<UserDetailInfoDTO> userDetailInfoDTOList=userService.findUserInfoByIds(teacherIdList);
        for (HomeworkDTO homeworkDTO : homeworkDTOList) {
            homeworkDTO.setUserName(getSessionValue().getUserName());
            homeworkDTO.setUserAvatar(AvatarUtils.getAvatar(getSessionValue().getAvatar(), AvatarType.MIN_AVATAR.getType()));
            //修改bug：老师头像及名字不能从session中获取，改为通过id获取
//            for(UserDetailInfoDTO user : userDetailInfoDTOList)
//            {
//                if(user.getId().equals(homeworkDTO.getTeacherId()))
//                {
//                    homeworkDTO.setUserName(user.getUserName());
//                    //homeworkDTO.setUserAvatar(AvatarUtils.getAvatar(user.getImgUrl(),AvatarType.MIN_AVATAR.getType()));
//                    homeworkDTO.setUserAvatar(user.getImgUrl());
//                    break;
//                }
//            }
            if (homeworkDTO.getDocFile() != null && homeworkDTO.getDocFile().size() == 0) {
                homeworkDTO.setDocFile(null);
            }
            homeworkDTO.setVoiceNum(homeworkDTO.getVoiceFile().size());
            if (homeworkDTO.getVoiceFile() != null && homeworkDTO.getVoiceFile().size() == 0) {
                homeworkDTO.setVoiceFile(null);
            }
//            homeworkDTO.setTitle(KeyWordFilterUtil.getReplaceStrTxtKeyWords(homeworkDTO.getTitle(), "*", 2));
            if (homeworkDTO.getContent() == null) {
                homeworkDTO.setContent("");
            } else {
//                homeworkDTO.setContent(KeyWordFilterUtil.getReplaceStrTxtKeyWords(homeworkDTO.getContent(), "*", 2));
            }

            //根据class 调整回复条数和最后回复时间
            int submitCount = 0;
            long lastsubmitTime = 0;
            for (StudentSubmitHomeWorkDTO studentSubmitHomeWorkDTO : homeworkDTO.getList()) {
                if (studentSubmitHomeWorkDTO.getClassId() != null && studentSubmitHomeWorkDTO.getClassId().equals(classId)) {
                    submitCount++;
                    if (studentSubmitHomeWorkDTO.getTime() > lastsubmitTime) {
                        lastsubmitTime = studentSubmitHomeWorkDTO.getTime();
                    }
                }
            }
            homeworkDTO.setSubmitCount(submitCount);
            homeworkDTO.setLastSubmitTime(lastsubmitTime);

            //课程相关
            ObjectId lessonId = new ObjectId(homeworkDTO.getLessonId());
            LessonEntry lessonEntry = lessonService.getLessonEntry(lessonId);
            if (!lessonEntry.getDirId().toString().equals("000000000000000000000000")) {
                homeworkDTO.setPg(0);
            }
            List<ObjectId> exercises = new ArrayList<ObjectId>();
            ObjectId exerciseId = lessonEntry.getExercise();
            exercises.add(exerciseId);
            Map<ObjectId, Integer> map = exerciseItemService.statItemCount(exercises);
            if (lessonEntry.getExercise() == null) {
                homeworkDTO.setExerciseId("noExercise");
            } else {
                homeworkDTO.setExerciseId(lessonEntry.getExercise().toString());
            }
//            homeworkDTO.setVideoNum(lessonEntry.getVideoCount());
            homeworkDTO.setFileNum(lessonEntry.getDocumentCount());
            Integer exerciseNum = exerciseId == null ? new Integer(0) : map.get(exerciseId);

            //提醒相关
            int reminder = reminderService.findHWReminderCount(getUserId(), classId, null, new ObjectId(homeworkDTO.getId()));
            homeworkDTO.setReminder(reminder);
            homeworkDTO.setExerciseNum(exerciseNum);
        }

        model.put("rows", homeworkDTOList);
        model.put("total", homeWorkService.countHomeWorkEntrys(teacherId, classId, type, subjectId, term, contentType));
        model.put("page", page);
        model.put("pageSize", pageSize);

        return model;
    }

    private int getHomeWorkSubmitNo(ObjectId classId, ObjectId homeworkId) {
        List<StudentSubmitHomeWork> list = homeWorkService.getStudentSubmitHomeWorks(homeworkId, classId);
        Set<ObjectId> set = new HashSet<ObjectId>();
        for (StudentSubmitHomeWork homeWork : list) {
            set.add(homeWork.getStudentId());
        }
        return set.size();
    }


    /**
     * 新建作业
     *
     * @param subjectId 科目ID
     * @param title     标题
     * @param content   内容
     * @param voicefile 音频
     * @param type      类型 0：课前  1：课后  2：其他
     * @return
     */
    @UserRoles(noValue = {UserRole.STUDENT, UserRole.PARENT})
    @RequestMapping("/add")
    @ResponseBody
    public Map<String, Object> addHomeWork(@ObjectIdType ObjectId subjectId, String title, String content,
                                           String voicefile, int type) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("code", "500");

        IdNameValuePair voiceivp = null;
        if (voicefile != null && voicefile != "") {
            voiceivp = new IdNameValuePair(new ObjectId(), "voice", voicefile);

        }

        HomeWorkEntry e = new HomeWorkEntry(getUserId(), new ArrayList<IdValuePair>(), title, HtmlUtils.delScriptTag(content),
                voiceivp, null, new ArrayList<IdValuePair>());

        e.setVersion(1);
        e.setType(type);
        e.setTerm(examResultService.getCurrentTerm());
        List<ObjectId> subjectList = new ArrayList<ObjectId>();
        subjectList.add(subjectId);
        e.setSubjectIdList(subjectList);
        e.setCorrect(1);
        //新建课程
        try {
            RespObj lessonObj = lessonController.createLesson(new ObjectId("000000000000000000000000"), title);
            e.setLessonId(new ObjectId((String) lessonObj.getMessage()));
        } catch (IllegalParamException ie) {
            ie.printStackTrace();
        }

        ObjectId id = homeWorkService.addHomeWork(e);
        logger.info(getUserId() + " created homework ;" + e);
        model.put("code", "200");
        model.put("message", id.toString());
        model.put("lessonId", e.getLessonId().toString());


        return model;
    }

    /**
     * 需要权限检查
     * @return
     */
    /**
     * 编辑保存作业
     *
     * @param homeworkId  作业ID
     * @param title       标题
     * @param content     内容
     * @param classIdList 班级列表
     * @param subjectList 科目列表
     * @param voicefile   声音文件
     * @param type        类型 0：课前 1：课后 2：其他
     * @param pg
     * @return
     */
    @UserRoles(noValue = {UserRole.STUDENT, UserRole.PARENT})
    @RequestMapping("/edit")
    @ResponseBody
    public RespObj editHomeWork(@ObjectIdType ObjectId homeworkId, String title, String content, String classIdList, String subjectList,
                                String voicefile, int type, int pg) {
        HomeWorkEntry homeWorkEntry = homeWorkService.getHomeWorkEntry(homeworkId);
        homeWorkEntry.setName(title);
        homeWorkEntry.setContent(content);
        homeWorkEntry.setType(type);
        homeWorkEntry.setCorrect(pg);

        List<ObjectId> subject_List = new ArrayList<ObjectId>();
        for (String subjectId : subjectList.split(",")) {
            subject_List.add(new ObjectId(subjectId));
        }
        homeWorkEntry.setSubjectIdList(subject_List);

        RespObj obj = RespObj.FAILD;
        String[] classes = classIdList.split(";");
        Map<ObjectId, IdValuePair> classPairMap = new HashMap<ObjectId, IdValuePair>();
        Map<ObjectId, IdValuePair> classLessonMap = new HashMap<ObjectId, IdValuePair>();
        String[] claArr = new String[classes.length];
        int[] index = new int[classes.length];
        for (int i = 0; i < classes.length; i++) {
            claArr[i] = classes[i].split(",")[1];
            index[i] = Integer.parseInt(classes[i].split(",")[2]);
        }
        List<ObjectId> stuList = new ArrayList<ObjectId>();
        List<ObjectId> classList = new ArrayList<ObjectId>();
        for (int i = 0; i < claArr.length; i++) {
            String cla = claArr[i];
            if (StringUtils.isBlank(cla) || !ObjectId.isValid(cla)) {
                obj.setMessage("班级参数错误");
                return obj;
            }
            IdValuePair clapair = new IdValuePair(new ObjectId(cla), index[i]);
            IdValuePair clapair1 = new IdValuePair(new ObjectId(cla), index[i]);
            classPairMap.put(clapair.getId(), clapair);
            classLessonMap.put(clapair.getId(), clapair1);
            classList.add(new ObjectId(cla));
            ZouBanCourseEntry zouBanCourseEntry = interestClassService.getZoubanCourseEntry(new ObjectId(cla));
            if(zouBanCourseEntry != null){
                stuList.addAll(zouBanCourseEntry.getStudentList());
            } else {
                stuList.addAll(classService.findStuByClassId(new ObjectId(cla)));
            }
        }
        List<TeacherClassSubjectDTO> list = teacherClassLessonService.getTeacherClassSubjectDTOList(getUserId(), classPairMap.keySet());

        if (list.size() != classPairMap.size()) {
            obj.setMessage("班级参数错误");
            //return obj;
        }

        homeWorkEntry.setClassLessonIndex(new ArrayList<IdValuePair>(classLessonMap.values()));
        ObjectId classId;
        for (TeacherClassSubjectDTO dto : list) {
            classId = dto.getClassInfo().getId();
            classPairMap.get(classId).setValue(dto.getClassInfo().getValue());
        }
        homeWorkEntry.setClasses(new ArrayList<IdValuePair>(classPairMap.values()));
        List<IdNameValuePair> voiceList = new ArrayList<IdNameValuePair>();
        IdNameValuePair voiceivp = null;
        if (voicefile != null && voicefile != "") {
            voiceivp = new IdNameValuePair(new ObjectId(), "voice", voicefile);
            voiceList.add(voiceivp);
            homeWorkEntry.setVoiceFile(voiceList);
            homeWorkEntry.setVoiceNum(voiceList.size());
        } else {
            homeWorkEntry.setVoiceNum(0);
        }


        LessonEntry lessonEntry = lessonService.getLessonEntry(homeWorkEntry.getLessonId());
        homeWorkEntry.setVideoNum(lessonEntry.getVideoIds().size());
        homeWorkEntry.setFileNum(lessonEntry.getDocumentCount());


        List<ObjectId> exercises = new ArrayList<ObjectId>();
        ObjectId exerciseId = lessonEntry.getExercise();
        exercises.add(exerciseId);
        Map<ObjectId, Integer> map = exerciseItemService.statItemCount(exercises);
        Integer exerciseNum = exerciseId == null ? new Integer(0) : map.get(exerciseId);
        if (exerciseNum == null) {
            homeWorkEntry.setExerciseNum(0);
        } else {
            homeWorkEntry.setExerciseNum(exerciseNum);
        }

        homeWorkService.editHomeWork(homeWorkEntry);
        obj = new RespObj(Constant.SUCCESS_CODE, homeworkId.toString());

        //增加提醒
        ReminderEntry reminderEntry = new ReminderEntry(getUserId(), stuList, classList, subject_List, homeworkId);
        reminderService.addReminder(reminderEntry);

        ExpLogType homeworkScore = ExpLogType.PUBLISH_HOMEWORK;
        experienceService.updateScore(getUserId().toString(), homeworkScore, homeworkId.toString());
        try {
            pushUtils.pushHomeWork(homeWorkEntry, getSessionValue().getUserName());
        } catch (Exception ex) {
            logger.error("", ex);
        }
        return obj;
    }

    /**
     * 删除作业
     *
     * @param hwid 作业ID
     * @return
     */
    @UserRoles(noValue = {UserRole.STUDENT, UserRole.PARENT})
    @RequestMapping("/remove")
    @ResponseBody
    public RespObj removeHomeWork(@ObjectIdType ObjectId hwid) {
        homeWorkService.removeHomework(hwid);
        reminderService.removeReminder(null, null, hwid);
        return RespObj.SUCCESS;

    }


    /**
     * 上传附件
     *
     * @param req
     * @param file
     * @return
     */
    //技术原因，上传到upload/homework下
    @RequestMapping("/uploadattach")
    @ResponseBody
    public Map<String, Object> uploadAttachment(HttpServletRequest req, MultipartFile file) {
        String filekey = "homework-" + new ObjectId().toString() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));

        String parentPath = req.getServletContext().getRealPath("/upload") + "/homework";
        File parentFile = new File(parentPath);
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        req.getServletContext().getRealPath("/upload");
        String urlPath = "/upload/homework/" + filekey;

        File attachFile = new File(parentFile, filekey);
        try {
            //QiniuFileUtils.uploadFile(filekey,file.getInputStream(),QiniuFileUtils.TYPE_DOCUMENT);
            FileUtils.copyInputStreamToFile(file.getInputStream(), attachFile);

        } catch (Exception ioe) {

        }
        Map<String, Object> model = new HashMap<String, Object>();

        model.put("uploadType", 1);
        model.put("realname", urlPath);
        return model;
    }

    /**
     * 上传语音
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("/uploadvoice")
    @ResponseBody
    public List<String> uploadFile(MultipartRequest request, HttpServletRequest req) throws Exception {
        List<String> fileUrls = new ArrayList<String>();
        Map<String, MultipartFile> fileMap = request.getFileMap();
        for (MultipartFile file : fileMap.values()) {
            String fileKey = "homework-" + new ObjectId().toString() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
            String parentPath = req.getServletContext().getRealPath("/upload") + "/homework";
            File parentFile = new File(parentPath);
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            File destFile = new File(parentFile, file.getOriginalFilename());
            file.transferTo(destFile);
            InputStream inputStream = new FileInputStream(destFile);
//            InputStream inputStream = file.getInputStream();


            String extName = FilenameUtils.getExtension(file.getOriginalFilename());

            if (extName.equalsIgnoreCase("amr")) {
                String saveFileKey = new ObjectId().toString() + ".mp3";
                com.sys.utils.QiniuFileUtils.convertAmrToMp3(fileKey, saveFileKey, inputStream);
                String path = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, saveFileKey);
                fileUrls.add(path);
            } else {
                QiniuFileUtils.uploadFile(fileKey, inputStream, QiniuFileUtils.TYPE_DOCUMENT);
                String path = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_DOCUMENT, fileKey);
                fileUrls.add(path);
            }
        }

        return fileUrls;
    }

    /**
     * 保存涂鸦过的图像
     *
     * @param req
     * @return
     */
    @RequestMapping("/saveeditedimage")
    @ResponseBody
    public RespObj saveImage(HttpServletRequest req) {
        String filekey = "homework-" + new ObjectId().toString() + ".png";

        String parentPath = req.getServletContext().getRealPath("/upload") + "/homework";
        File parentFile = new File(parentPath);
        File attachFile = new File(parentFile, filekey);
        try {
            ServletInputStream inputStream = req.getInputStream();
            FileUtils.copyInputStreamToFile(inputStream, attachFile);
        } catch (Exception ex) {
            logger.error("", ex);
            return RespObj.FAILD;
        }
        //替换imageid对应的url

        String urlPath = "/upload/homework/" + filekey;
        ObjectId imageId = new ObjectId(req.getParameter("imageId"));
        homeWorkService.updateDocImage(imageId, urlPath);


        return RespObj.SUCCESS;
    }

    /**
     * 移动端涂鸦应用；
     *
     * @param req
     * @param req1
     * @return
     */
    @RequestMapping("/mobile/saveeditedimage")
    @ResponseBody
    public RespObj saveImage(MultipartRequest req, HttpServletRequest req1) {

        String filekey = "homework-" + new ObjectId().toString() + ".png";

        String parentPath = req1.getServletContext().getRealPath("/upload") + "/homework";
        File parentFile = new File(parentPath);
        File attachFile = new File(parentFile, filekey);

        Map<String, MultipartFile> fileMap = req.getFileMap();
        for (MultipartFile file : fileMap.values()) {
            try {
                FileUtils.copyInputStreamToFile(file.getInputStream(), attachFile);
            } catch (Exception ex) {
                logger.error("", ex);
                return RespObj.FAILD;
            }
        }
        //替换imageid对应的url

        String urlPath = "/upload/homework/" + filekey;
        ObjectId imageId = new ObjectId(req1.getParameter("imageId"));
        homeWorkService.updateDocImage(imageId, urlPath);

        return RespObj.SUCCESS;
    }


    /**
     * 作业详情（批改作业头部）
     *
     * @param hwid
     * @return
     * @throws PermissionUnallowedException
     */
    @RequestMapping("/detail")
    @ResponseBody
    public HomeworkDTO homeworkDetail(@ObjectIdType ObjectId hwid) throws PermissionUnallowedException {
        HomeWorkEntry e = isHavePermission(hwid);
        HomeworkDTO dto = new HomeworkDTO(e);
//        dto.setTitle(KeyWordFilterUtil.getReplaceStrTxtKeyWords(dto.getTitle(), "*", 2));
//        dto.setContent(KeyWordFilterUtil.getReplaceStrTxtKeyWords(dto.getContent(), "*", 2));
        List<ObjectId> tidList = new ArrayList<ObjectId>();
        tidList.add(e.getTeacherId());


        //
        Map<ObjectId, UserEntry> userDetailMap = userService.getUserEntryMap(tidList, Constant.FIELDS);

        UserEntry userEntry = userDetailMap.get(e.getTeacherId());

        dto.setUserName(userEntry.getUserName());
        dto.setUserAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(),
                AvatarType.MIN_AVATAR.getType()));
        //删除提醒
        reminderService.deleteReceiver(getUserId(), null, null, hwid);

        return dto;
    }

    /**
     * 分页查询学生作业提交情况 , 要传入classId（批改作业列表）
     *
     * @param hwid      作业ID
     * @param classId   班级ID
     * @param page      页数
     * @param pageSize  每页条目数
     * @param classType 1:主班  2：兴趣班
     * @return
     * @throws PermissionUnallowedException
     */
    @RequestMapping("/submit/list")
    @ResponseBody
    public Map<String, Object> getStudentSubmitHomeWorkDTOs(@ObjectIdType ObjectId hwid, @ObjectIdType ObjectId classId,
                                                            int page, int pageSize, int classType) throws PermissionUnallowedException {
        isHavePermission(hwid);
        List<StudentSubmitHomeWorkDTO> retList = new ArrayList<HomeworkDTO.StudentSubmitHomeWorkDTO>();
        boolean isTeacher = UserRole.isTeacher(getSessionValue().getUserRole());
        ObjectId realUserId = getStudentId();//如果是家长，得到学生Id
        if (isTeacher) {
            realUserId = null;
        }
        List<StudentSubmitHomeWork> list = homeWorkService.getStudentSubmitHomeWorks(hwid, classId, (page - 1) * pageSize, pageSize, realUserId);
        for (StudentSubmitHomeWork dbo : list) {
//            if(dbo.getContent()!=null && dbo.getContent()!="") {
//                dbo.setContent(KeyWordFilterUtil.getReplaceStrTxtKeyWords(dbo.getContent(), "*", 2));
//            }
            retList.add(new StudentSubmitHomeWorkDTO(dbo));

        }
        List<ObjectId> ids = MongoUtils.getFieldObjectIDs(list, "si");
        Map<ObjectId, UserEntry> userMap = userService.getUserEntryMap(ids, UserService.COMMON_FIELDS);

        UserEntry ue;

        for (StudentSubmitHomeWorkDTO subDto : retList) {
            ue = userMap.get(new ObjectId(subDto.getUserId()));
            subDto.setUserName(ue.getUserName());
            subDto.setAvatar(AvatarUtils.getAvatar(ue.getAvatar(), AvatarType.MIN_AVATAR.getType()));
            subDto.setIsview(1);
            // 回复内容
            List<StudentSubmitHomeWorkDTO> hf = subDto.getHf();
            if (hf != null && hf.size() > 0) {
//                List<ObjectId> hfIds = new ArrayList<ObjectId>();
//                for (StudentSubmitHomeWorkDTO hfDto : hf) {
//                    hfIds.add(new ObjectId(hfDto.getUserId()));
//                }
//                Map<ObjectId, UserEntry> hfUserMap = userService.getUserEntryMap(hfIds, UserService.COMMON_FIELDS);
                String teacherId = hf.get(0).getUserId();
                UserDetailInfoDTO teacher = userService.getUserInfoById(teacherId);
                for (StudentSubmitHomeWorkDTO hfDto : hf) {
//                    UserEntry hfUser = hfUserMap.get(new ObjectId(hfDto.getUserId()));
                    hfDto.setUserName(teacher.getUserName());
                    hfDto.setAvatar(teacher.getImgUrl());
                }
            }
        }

        Map<String, Object> model = new HashMap<String, Object>();

        model.put("rows", retList);
        model.put("total", homeWorkService.countStudentSubmitHomeWorks(hwid));
        model.put("page", page);
        model.put("pageSize", pageSize);

        //提交人数和班级总人数//移动端使用
        List<UserDetailInfoDTO> students = new ArrayList<UserDetailInfoDTO>();
        if (classType == 1) {
            students = classService.findStuByClassId(classId.toString());
        } else if (classType == 2) {
            students = classService.findStuByInterestClassId(classId.toString());
        }
        model.put("totalStu", students.size());//班级总人数
        Map<String, Object> map = new HashMap<String, Object>();
        for (StudentSubmitHomeWorkDTO studentSubmitHomeWorkDTO : retList) {
            map.put(studentSubmitHomeWorkDTO.getUserName(), studentSubmitHomeWorkDTO);
        }
        model.put("submitStu", map.size());//提交人数


        return model;
    }


    /**
     * 分页查询学生作业提交情况 , 要传入classId（批改作业列表）//移动端使用，只返回自己提交的内容//学生和家长使用
     *
     * @param hwid     作业ID
     * @param classId  班级ID
     * @param page     页数
     * @param pageSize 每页条目数
     * @return
     * @throws PermissionUnallowedException
     */
    @RequestMapping("/submit/list/mobile")
    @ResponseBody
    public Map<String, Object> getStudentSubmitHomeWorkDTO(@ObjectIdType ObjectId hwid, @ObjectIdType ObjectId classId,
                                                           int page, int pageSize, int classType) throws PermissionUnallowedException {
        Map<String, Object> model = getStudentSubmitHomeWorkDTOs(hwid, classId, page, pageSize, classType);
        List<StudentSubmitHomeWorkDTO> retList = (List<StudentSubmitHomeWorkDTO>) model.get("rows");
        model.put("rows", retList);
        return model;
    }

    /**
     * 单个学生的回复
     *
     * @param hwid      作业ID
     * @param classId   班级ID
     * @param studentId 学生ID
     * @param time      提交时间
     * @return
     * @throws PermissionUnallowedException
     */
    @RequestMapping("/submit/mobile")
    @ResponseBody
    public StudentSubmitHomeWorkDTO getStudentSubmitHomeWorkDTO(@ObjectIdType ObjectId hwid, @ObjectIdType ObjectId classId, String studentId, long time, int classType) throws PermissionUnallowedException {
        Map<String, Object> model = getStudentSubmitHomeWorkDTOs(hwid, classId, 1, 500, classType);
        List<StudentSubmitHomeWorkDTO> retList = (List<StudentSubmitHomeWorkDTO>) model.get("rows");
        for (StudentSubmitHomeWorkDTO dto : retList) {
            if (studentId.equals(dto.getUserId()) && time == dto.getTime()) {
                return dto;
            }
        }
        return null;
    }

    /**
     * 得到学生提交作业情况的统计
     *
     * @param hwid      作业ID
     * @param classId   班级ID
     * @param className 班级名称
     * @param classType 班级类型 1：主班 2：兴趣班
     * @return
     * @throws PermissionUnallowedException
     */
    @RequestMapping("/getsubmitInfo")
    @ResponseBody
    public Map<String, Object> getSubmitInfo(@ObjectIdType ObjectId hwid, @ObjectIdType ObjectId classId, String className, int classType) throws PermissionUnallowedException {
        isHavePermission(hwid);
        List<StudentSubmitHomeWorkDTO> retList = new ArrayList<HomeworkDTO.StudentSubmitHomeWorkDTO>();
        List<StudentSubmitHomeWork> list = homeWorkService.getStudentSubmitHomeWorks(hwid, classId);
        for (StudentSubmitHomeWork dbo : list) {
            retList.add(new StudentSubmitHomeWorkDTO(dbo));
        }
        List<ObjectId> ids = MongoUtils.getFieldObjectIDs(list, "si");
        Map<ObjectId, UserEntry> userMap = userService.getUserEntryMap(ids, UserService.COMMON_FIELDS);
        UserEntry ue;
        for (StudentSubmitHomeWorkDTO subDto : retList) {
            ue = userMap.get(new ObjectId(subDto.getUserId()));
            subDto.setUserName(ue.getUserName());
        }

        //统计提交情况
        Map<String, SubmitInfoDTO> infoDTOMap = new HashMap<String, SubmitInfoDTO>();
//        ClassEntry classEntry = classService.getClassEntryById(classId, Constant.FIELDS);
//        List<ObjectId> students = classEntry.getStudents();
//        Map<ObjectId, UserEntry> userEntryMap = userService.getUserEntryMap(students, Constant.FIELDS);

        List<UserDetailInfoDTO> studentList = new ArrayList<UserDetailInfoDTO>();
        if (classType == 1 || classType == 3) {
            studentList = classService.findStuByClassId(classId.toString());
        } else if (classType == 2) {
            studentList = classService.findStuByInterestClassId(classId.toString());
        }

        for (UserDetailInfoDTO stu : studentList) {
            String stuName = stu.getUserName();
            SubmitInfoDTO submitInfoDTO = new SubmitInfoDTO(stuName, className, 0);
            infoDTOMap.put(stuName, submitInfoDTO);
        }
        for (StudentSubmitHomeWorkDTO subDto : retList) {
            String stuName = subDto.getUserName();
            if (infoDTOMap.containsKey(stuName)) {
                SubmitInfoDTO submitInfoDTO = infoDTOMap.get(stuName);
                int subNum = submitInfoDTO.getSubNum();
                submitInfoDTO.setSubNum(subNum + 1);
            } else {
                SubmitInfoDTO submitInfoDTO = new SubmitInfoDTO(stuName, className, 1);
                infoDTOMap.put(stuName, submitInfoDTO);
            }
        }
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("infoDTOMap", infoDTOMap);
        model.put("size", list.size());
        return model;
    }


    /**
     * 学生查看作业列表
     *
     * @param subjectId   科目ID（可选，默认“000000000000000000000000”表示全部学科）
     * @param classId     班级ID
     * @param page        页码
     * @param pageSize    每页个数
     * @param type        类型（可选，默认3） 0：课前 1：课后 2：其他 3：全部
     * @param term        学期  0 ：本学期  1：全部学期
     * @param contentType 0：全部 1：有视频 2：有附件 3：有进阶练习 4：有语音
     * @return
     * @throws ResultTooManyException
     */
    @RequestMapping("/student/list")
    @ResponseBody
    public Map<String, Object> getStudnetHomeWorks(@ObjectIdType @RequestParam(required = false, defaultValue = "000000000000000000000000") ObjectId subjectId,
                                                   String classId, int page, int pageSize,
                                                   @RequestParam(required = false, defaultValue = "3") int type,
                                                   @RequestParam(required = false, defaultValue = "0") int term,
                                                   @RequestParam(required = false, defaultValue = "0") int contentType) throws ResultTooManyException {
        Map<String, Object> model = new HashMap<String, Object>();
        //传入的page从1开始
        List<HomeWorkEntry> homeworkEntryList = homeWorkService.getHomeWorkEntrys(null, classId, page - 1, pageSize, type, subjectId, term, contentType);

        List<ObjectId> tidList = new ArrayList<ObjectId>();
        List<HomeworkDTO> homeworkDTOList = new ArrayList<HomeworkDTO>();
        for (HomeWorkEntry homeWorkEntry : homeworkEntryList) {
            //homeworkDTO.get
            tidList.add(homeWorkEntry.getTeacherId());
        }

        //
        Map<ObjectId, UserEntry> userDetailMap = userService.getUserEntryMap(tidList, Constant.FIELDS);

        for (HomeWorkEntry homeWorkEntry : homeworkEntryList) {
            HomeworkDTO homeworkDTO = new HomeworkDTO(homeWorkEntry);
            homeworkDTO.setUserName(userDetailMap.get(homeWorkEntry.getTeacherId()).getUserName());
            homeworkDTO.setUserAvatar(AvatarUtils.getAvatar(userDetailMap.get(homeWorkEntry.getTeacherId()).getAvatar(),
                    AvatarType.MIN_AVATAR.getType()));
            if (homeworkDTO.getDocFile() != null && homeworkDTO.getDocFile().size() == 0) {
                homeworkDTO.setDocFile(null);
            }
            homeworkDTO.setVoiceNum(homeworkDTO.getVoiceFile().size());
            if (homeworkDTO.getVoiceFile() != null && homeworkDTO.getVoiceFile().size() == 0) {
                homeworkDTO.setVoiceFile(null);
            }


            //课程相关
            ObjectId lessonId = new ObjectId(homeworkDTO.getLessonId());
            LessonEntry lessonEntry = lessonService.getLessonEntry(lessonId);
            List<ObjectId> exercises = new ArrayList<ObjectId>();
            ObjectId exerciseId = lessonEntry.getExercise();
            exercises.add(exerciseId);
            Map<ObjectId, Integer> map = exerciseItemService.statItemCount(exercises);
            if (lessonEntry.getExercise() == null) {
                homeworkDTO.setExerciseId("noExercise");
            } else {
                homeworkDTO.setExerciseId(lessonEntry.getExercise().toString());
            }
            homeworkDTO.setVideoNum(lessonEntry.getVideoCount());
            homeworkDTO.setFileNum(lessonEntry.getDocumentCount());
            Integer exerciseNum = exerciseId == null ? new Integer(0) : map.get(exerciseId);
            homeworkDTO.setExerciseNum(exerciseNum);
            //提醒相关
            int reminder = reminderService.findHWReminderCount(getUserId(), null, null, new ObjectId(homeworkDTO.getId()));
            homeworkDTO.setReminder(reminder);
            homeworkDTOList.add(homeworkDTO);
        }

        model.put("rows", homeworkDTOList);
        model.put("total", homeWorkService.countHomeWorkEntrys(null, classId, type, subjectId, term, contentType));
        model.put("page", page);
        model.put("pageSize", pageSize);

        return model;
    }

    /**
     * 学生提交作业；仅仅用于学生
     *
     * @param content      内容
     * @param filenamelist 附件
     * @param voicefile    声音
     * @param hwId         作业ID
     * @param classId      班级ID
     * @return
     * @throws PermissionUnallowedException
     */
    @UserRoles({UserRole.STUDENT})
    @RequestMapping("/submit")
    @ResponseBody
    public RespObj studentSubmit(String content, String filenamelist, String voicefile, @ObjectIdType ObjectId hwId,
                                 @ObjectIdType ObjectId classId) throws PermissionUnallowedException {
        HomeWorkEntry homeWorkEntry = isHavePermission(hwId);
        IdNameValuePair voiceivp = null;
        if (voicefile != null) {
            voiceivp = new IdNameValuePair(new ObjectId(), "voice", voicefile);
        }
        String[] filelist = null;
        if (filenamelist != null) {
            filelist = filenamelist.split(";");
        }
        List<IdNameValuePair> docList = new ArrayList<IdNameValuePair>();
        if (filelist != null) {
            for (String strtokens : filelist) {
                String[] token = strtokens.split(",");
                if (token != null && token.length == 2) {
                    IdNameValuePair filevp = new IdNameValuePair(new ObjectId(),
                            token[1], token[0]);
                    docList.add(filevp);
                }
            }
        }
        StudentSubmitHomeWork sshw = new StudentSubmitHomeWork(getUserId(), classId, System.currentTimeMillis(), content,

                voiceivp, null, HomeWorkSubmitType.ONCLASS);
        if (docList.size() > 0) {
            sshw.setDocFile(docList);
        }
        homeWorkService.submitHomeWork(hwId, sshw);

        //新增提醒
        List<ObjectId> teacherList = new ArrayList<ObjectId>();
        teacherList.add(homeWorkEntry.getTeacherId());
        List<ObjectId> classList = new ArrayList<ObjectId>();
        classList.add(classId);
        ReminderEntry reminderEntry = new ReminderEntry(getUserId(), teacherList, classList, homeWorkEntry.getSubjectList(), hwId);
        reminderService.addReminder(reminderEntry);

        ExpLogType expLogType = ExpLogType.SUBMIT_HOMEWORK;
        experienceService.updateNoRepeat(getUserId().toString(), expLogType, hwId.toString());
        return RespObj.SUCCESS;
    }

    /**
     * 老师回复学生
     *
     * @param content      内容
     * @param filenamelist 附件列表
     * @param voicefile    声音
     * @param hwId         作业ID
     * @param classId      班级ID
     * @return
     * @throws PermissionUnallowedException
     */
    @RequestMapping("/reply")
    @ResponseBody
    public RespObj teacherReply(String content, String filenamelist, String voicefile, @ObjectIdType ObjectId hwId,
                                @ObjectIdType ObjectId classId, @ObjectIdType ObjectId studentId, long time) throws PermissionUnallowedException {
        StudentSubmitHomeWork sshw = createStudentSubmitHomeWork(content, filenamelist, voicefile, hwId, classId);
        homeWorkService.teacherReply(hwId, studentId, time, sshw);
        return RespObj.SUCCESS;
    }


    public StudentSubmitHomeWork createStudentSubmitHomeWork(String content, String filenamelist, String voicefile, ObjectId hwId,
                                                             ObjectId classId) throws PermissionUnallowedException {
        isHavePermission(hwId);
        IdNameValuePair voiceivp = null;
        if (voicefile != null) {
            voiceivp = new IdNameValuePair(new ObjectId(), "voice", voicefile);
        }
        String[] filelist = null;
        if (filenamelist != null) {
            filelist = filenamelist.split(";");
        }
        List<IdNameValuePair> docList = new ArrayList<IdNameValuePair>();
        if (filelist != null) {
            for (String strtokens : filelist) {
                String[] token = strtokens.split(",");
                if (token != null && token.length == 2) {
                    IdNameValuePair filevp = new IdNameValuePair(new ObjectId(),
                            token[1], token[0]);
                    docList.add(filevp);
                }
            }
        }
        StudentSubmitHomeWork sshw = new StudentSubmitHomeWork(getUserId(), classId, System.currentTimeMillis(), content,

                voiceivp, null, HomeWorkSubmitType.ONCLASS);
        if (docList.size() > 0) {
            sshw.setDocFile(docList);
        }
        return sshw;
    }


    /**
     * 作业学生提交情况
     *
     * @param id
     * @param tag     0没有查看 1 已经查看
     * @param skip
     * @param limit
     * @param isSend
     * @param classId
     * @return
     * @throws PermissionUnallowedException
     * @throws IllegalParamException
     */
    @RequestMapping("/student/submits")
    @ResponseBody
    public PageDTO<SimpleDTO> getHomeworkStudentSubmit(@ObjectIdType ObjectId id,
                                                       @RequestParam(required = false, defaultValue = "0") Integer tag,
                                                       @RequestParam(required = false, defaultValue = "0") Integer skip,
                                                       @RequestParam(required = false, defaultValue = "1") Integer limit,
                                                       @RequestParam(required = false, defaultValue = "0") Integer isSend,
                                                       @RequestParam(required = false, defaultValue = "0") String classId
    ) throws PermissionUnallowedException, IllegalParamException {
        if (tag != Constant.ZERO && tag != Constant.ONE) {
            throw new IllegalParamException();
        }
        List<SimpleDTO> retList = new ArrayList<SimpleDTO>();
        List<StudentSubmitHomeWork> list;
        HomeWorkEntry he = new HomeWorkEntry(null);
        if (classId.equals("0")) {
            he = isHavePermission(id);
            if (null == he)
                throw new IllegalParamException();
            list = he.getSubmitList();
        } else {
            list = homeWorkService.getStudentSubmitHomeWorks(id, new ObjectId(classId), 0, 200, null);
        }

        int total = 0;
        // 计算出的应该查询的用户ID
        Set<ObjectId> calTotalSet = new HashSet<ObjectId>();
        Map<ObjectId, StudentSubmitHomeWork> submitMap = new HashMap<ObjectId, StudentSubmitHomeWork>();

        if (tag == Constant.ONE) // 已经查看的用户
        {
            for (StudentSubmitHomeWork sshw : list) {
                submitMap.put(sshw.getStudentId(), sshw);
                calTotalSet.add(sshw.getStudentId());
                total = calTotalSet.size();
            }
        } else {
            List<ObjectId> classIds = new ArrayList<ObjectId>();
            if (classId.equals("0")) {
                List<IdValuePair> classes = he.getClasses();
                classIds = MongoUtils.getFieldObjectIDs(classes, "id");
            } else {
                classIds.add(new ObjectId(classId));
            }
            Map<ObjectId, ClassEntry> classEntryMap = classService.getClassEntryMap(classIds, new BasicDBObject("stus", 1));
            Set<ObjectId> studentIdSet = new HashSet<ObjectId>();
            for (Map.Entry<ObjectId, ClassEntry> entry : classEntryMap.entrySet()) {
                studentIdSet.addAll(entry.getValue().getStudents());
            }
            studentIdSet.removeAll(MongoUtils.getFieldObjectIDs(list, "si"));
            calTotalSet = new HashSet<ObjectId>(studentIdSet);
            total = calTotalSet.size();

            if (Constant.ONE == isSend) {
                try {
                    //发送信件
                    LetterEntry e = new LetterEntry(he.getTeacherId(), "你还有一份作业没有做，请尽快完成!", new ArrayList<ObjectId>(calTotalSet));
                    letterService.sendLetter(e);
                    Map<ObjectId, UserEntry> users = userService.getUserEntryMap(calTotalSet, new BasicDBObject("chatid", 1));
                    List<String> toChatIds = new ArrayList<String>();
                    for (Map.Entry<ObjectId, UserEntry> entry : users.entrySet()) {
                        toChatIds.add(entry.getValue().getChatId());
                    }
                    easeMobService.sendMessage(getSessionValue().getChatid().toString(), toChatIds, e.getContent(), getSessionValue().getUserName(), getSessionValue().getMaxAvatar());
                } catch (Exception ex) {
                    logger.error("", ex);
                }
                return new PageDTO<SimpleDTO>();
            }
        }


        if (calTotalSet.size() > skip) {
            int endIndex = skip + limit;
            if (endIndex >= calTotalSet.size())
                endIndex = calTotalSet.size();


            List<ObjectId> totlaList = new ArrayList<ObjectId>(calTotalSet);

            Collections.sort(totlaList);

            totlaList = totlaList.subList(skip, endIndex);

            Map<ObjectId, UserEntry> userMap = userService.getUserEntryMap(totlaList, new BasicDBObject("nm", 1).append("avt", 1));
            SimpleDTO dto = null;
            UserEntry ue = null;
            StudentSubmitHomeWork sshw = null;
            for (Map.Entry<ObjectId, UserEntry> entry : userMap.entrySet()) {
                try {
                    dto = new SimpleDTO();
                    ue = entry.getValue();
                    sshw = submitMap.get(entry.getKey());
                    dto.setId(ue.getID().toString());
                    dto.setValue(AvatarUtils.getAvatar(ue.getAvatar(), AvatarType.MIDDLE_AVATAR.getType()));
                    dto.setValue1(ue.getUserName());
                    if (null != sshw) {
                        dto.setValue2(sshw.getTime());
                    }
                    retList.add(dto);
                } catch (Exception ex) {
                    logger.error("", ex);
                }
            }
        }
        return new PageDTO<SimpleDTO>(total, retList);
    }


    /**
     * 是否有权限查看;么有的话报错,有的话返回该作业
     *
     * @param hwid 作业ID
     * @return
     * @throws PermissionUnallowedException
     */
    private HomeWorkEntry isHavePermission(ObjectId hwid) throws PermissionUnallowedException {
        HomeWorkEntry e = homeWorkService.getHomeWorkEntry(hwid);

        //老师
//		boolean isTeachaer=UserRole.isTeacher(getSessionValue().getUserRole());
//		if(isTeachaer)
//		{
//			if(!e.getTeacherId().equals(getSessionValue().getId()))
//			{
//				throw new PermissionUnallowedException();
//			}
//		}
//		//学生
//		boolean isStudent=UserRole.isStudent(getSessionValue().getUserRole());
//		if(isStudent)
//		{
//			List<ClassEntry> list=classService.getStudentClassList(getUserId());
//			List<ObjectId> classIds=MongoUtils.getFieldObjectIDs(list, Constant.ID);
//			Set<ObjectId> classIdSet =new HashSet<ObjectId>(classIds);
//			boolean isIn=false;
//			for(IdValuePair pair:e.getClasses())
//			{
//				if(classIdSet.contains(pair.getId()))
//				{
//					isIn=true;
//					break;
//				}
//			}
//			if(!isIn)
//			{
//				throw new PermissionUnallowedException();
//			}
//		}
        return e;
    }

    @RequestMapping("/correct")
    @ResponseBody
    public Boolean correctHomework(@ObjectIdType ObjectId homeworkId, @ObjectIdType ObjectId studentId, long time) {
        homeWorkService.correctHomework(homeworkId, studentId, time);
        return true;
    }


    public String getFileName(String filePath) {
        return FilenameUtils.getName(filePath);
    }


    public Map<String, Object> uploadAtthment(HttpServletRequest req, MultipartFile file) {
        String filekey = "homework-" + new ObjectId().toString() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));

        String parentPath = req.getServletContext().getRealPath("/upload") + "/homework";
        File parentFile = new File(parentPath);
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        req.getServletContext().getRealPath("/upload");
        String urlPath = "/upload/homework/" + filekey;

        File attachFile = new File(parentFile, filekey);
        try {
            //QiniuFileUtils.uploadFile(filekey,file.getInputStream(),QiniuFileUtils.TYPE_DOCUMENT);
            FileUtils.copyInputStreamToFile(file.getInputStream(), attachFile);

        } catch (Exception ioe) {

        }
        Map<String, Object> model = new HashMap<String, Object>();

        model.put("uploadType", 1);
        model.put("realname", urlPath);
        return model;
    }

    /**
     * 在七牛上下载压缩过的图片，放在本地服务器上
     *
     * @param req
     * @param imageUrl //原始图片地址（本地）
     * @return
     */
    @RequestMapping("/getImage")
    @ResponseBody
    public Map<String, Object> getImage(HttpServletRequest req, String imageUrl) {
        Map<String, Object> model = new HashMap<String, Object>();
        String parentPath = req.getServletContext().getRealPath("/upload") + "/homework/";
        String filekey = imageUrl.substring(17, imageUrl.lastIndexOf(".")) + "-h100" + imageUrl.substring(imageUrl.lastIndexOf("."));
        String imageName = parentPath + filekey;
        File file = new File(imageName);
        if (!file.exists()) {//文件不存在
            try {
                String type = "?imageView/2/w/580";//压缩样式
                String qnUrl = "http://7xjfbm.com1.z0.glb.clouddn.com" + imageUrl + type;//对应七牛地址
                URL url = new URL(qnUrl);
                DataInputStream dataInputStream = new DataInputStream(url.openStream());
                FileOutputStream fileOutputStream = new FileOutputStream(file);

                byte[] buffer = new byte[1024];
                int length;

                while ((length = dataInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, length);
                }

                dataInputStream.close();
                fileOutputStream.close();
                model.put("code", 200);
                model.put("url", "/upload/homework/" + filekey);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {//存在直接返回
            model.put("code", 200);
            model.put("url", "/upload/homework/" + filekey);
        }
        return model;
    }

    @RequestMapping("/deleteStuSubmitHW")
    @ResponseBody
    public RespObj deleteStuSubmitHW(@ObjectIdType ObjectId homeworkId, @ObjectIdType ObjectId studentId, long time) {
        RespObj respObj = RespObj.FAILD;
        try {
            homeWorkService.deleteStuSubmitHW(homeworkId, studentId, time);
            respObj = RespObj.SUCCESS;
        } catch (Exception e) {

        }
        return respObj;
    }
//=============================================================================================================

    /**
     * 根据时间计算所处学期
     *
     * @param time
     * @return
     */
    public String getTerm(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String schoolYear;
        if (month < 9 && month > 2) {
            schoolYear = (year - 1) + "-" + year + "学年第二学期";
        } else if (month >= 9) {
            schoolYear = year + "-" + (year + 1) + "学年第一学期";
        } else {
            schoolYear = (year - 1) + "-" + year + "学年第一学期";
        }
        return schoolYear;
    }

    /**
     * 老作业转换成新作业
     *
     * @param
     */
    public void convert() {
        HomeWorkService homeWorkService = new HomeWorkService();
        TeacherClassSubjectService teacherClassSubjectService = new TeacherClassSubjectService();
        LessonController lessonController = new LessonController();

        List<HomeWorkEntry> homeWorkEntryList = homeWorkService.findAllOldHomeworkEntry();

        if (homeWorkEntryList != null && homeWorkEntryList.size() > 0) {
            long total = homeWorkEntryList.size();
            long i = 0;
            for (HomeWorkEntry homeWorkEntry : homeWorkEntryList) {
                i++;
                System.out.println("=========================================== " + i + "/" + total);
//                HomeWorkEntry homeWorkEntry = homeWorkService.getHomeWorkEntry(homeworkId);
                ObjectId homeworkId = homeWorkEntry.getID();
                System.out.println("课程homeworkId=" + homeworkId);
                homeWorkEntry.setVersion(1);
                homeWorkEntry.setType(1);//默认课后
                homeWorkEntry.setCorrect(1);//默认需要老师批改
                homeWorkEntry.setTerm(getTerm(homeworkId.getTime()));//设置学期

                //查找学科
                ObjectId teacherId = homeWorkEntry.getTeacherId();
                List<IdValuePair> classes = homeWorkEntry.getClasses();
                List<ObjectId> classList = new ArrayList<ObjectId>();
                for (IdValuePair idValuePair : classes) {
                    classList.add(idValuePair.getId());
                }
                List<TeacherClassSubjectDTO> list = teacherClassSubjectService.getTeacherClassSubjectDTOList(teacherId, classList);
                if (list != null && list.size() > 0) {
                    TeacherClassSubjectDTO teacherClassSubjectDTO = list.get(0);
                    ObjectId subjectId = teacherClassSubjectDTO.getSubjectInfo().getId();
                    List<ObjectId> subjectIdList = new ArrayList<ObjectId>();
                    subjectIdList.add(subjectId);
                    homeWorkEntry.setSubjectIdList(subjectIdList);
                } else {
                    System.out.println("无法查到学科");
//                    return;
                    continue;
                }

                //把作业的附件迁移到课程的课件中去
                List<IdNameValuePair> docFiles = homeWorkEntry.getDocFile();
                List<LessonWare> lessonWareList = new ArrayList<LessonWare>();
                if (docFiles != null && docFiles.size() > 0) {
                    for (IdNameValuePair idNameValuePair : docFiles) {
                        LessonWare lessonWare = new LessonWare("txt", idNameValuePair.getName(), (String) idNameValuePair.getValue());
                        lessonWareList.add(lessonWare);
                    }
                }

                //新建课程
                try {
                    RespObj lessonObj = lessonController.createLessonForHW(new ObjectId("000000000000000000000000"), homeWorkEntry.getName(), homeWorkEntry.getTeacherId(), lessonWareList);
                    homeWorkEntry.setLessonId(new ObjectId((String) lessonObj.getMessage()));
                } catch (IllegalParamException ie) {
                    ie.printStackTrace();
                }

                //保存作业
                homeWorkService.editHomeWork(homeWorkEntry);
                System.out.println("转换成功 lessonId = " + homeWorkEntry.getLessonId().toString());
            }
        }

    }

    /**
     * 老班级课程转换成新作业
     *
     * @param
     */
    public void classLessonConvertHW() {
//        public void classLessonConvertHW(ObjectId lessonId){

        LessonService lessonService = new LessonService();
        DirService dirService = new DirService();
        TeacherClassSubjectService teacherClassSubjectService = new TeacherClassSubjectService();

        List<LessonEntry> lessonEntryList = lessonService.findAllClassLesson();

        if (lessonEntryList != null && lessonEntryList.size() > 0) {
            long total = lessonEntryList.size();
            long i = 0L;
            for (LessonEntry lessonEntry : lessonEntryList) {
                i++;
                System.out.println("=========================================== " + i + "/" + total);
//                LessonEntry lessonEntry = lessonService.getLessonEntry(lessonId);
                ObjectId lessonId = lessonEntry.getID();
                System.out.println("课程lessonId=" + lessonId);
                String title = lessonEntry.getName();
                String content = lessonEntry.getContent() == null ? "" : lessonEntry.getContent();
                if (lessonEntry.getType() == 2) {//班级课程
                    DirEntry dirEntry = dirService.getDirEntry(lessonEntry.getDirId(), null);
                    if (dirEntry == null) {
                        System.out.println("课程lessonId=" + lessonId + "  不属于任何目录!");
                        continue;
                    }
                    TeacherClassSubjectEntry tcs = teacherClassSubjectService.getTeacherClassSubjectEntry(dirEntry.getOwerId());
                    List<IdValuePair> classList = new ArrayList<IdValuePair>();
                    if (tcs == null) {
                        System.out.println("课程lessonId=" + lessonId + "  没有owner!");
                        continue;
                    }
                    classList.add(tcs.getClassInfo());
                    HomeWorkEntry e = new HomeWorkEntry(tcs.getTeacherId(), classList, title, HtmlUtils.delScriptTag(content), null, null, new ArrayList<IdValuePair>());

//                    Date date = DateTimeUtils.stringToDate("2015-09-01 00:00:00", DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H);
                    e.setVersion(1);
                    e.setType(1);
                    e.setTerm(getTerm(lessonId.getTime()));
//                    e.setTerm("2015-2016学年第一学期");
//                    e.setSubjectId(tcs.getSubjectInfo().getId());
                    List<ObjectId> subjectIdList = new ArrayList<ObjectId>();
                    subjectIdList.add(tcs.getSubjectInfo().getId());
                    e.setSubjectIdList(subjectIdList);
                    e.setCorrect(1);
                    e.setLessonId(lessonId);
//                    if (lessonId.getTime() < date.getTime()) {//发表日期在2015-09-01 00:00:00之前
//                        e.setID(new ObjectId(date));
//                    }
                    Date date = new Date(lessonId.getTime());
                    e.setID(new ObjectId(date));

                    ObjectId homeworkId = homeWorkService.addHomeWork(e);
                    System.out.println("转换成功 homeworkId = " + homeworkId);
                    System.out.println("作业发表日期 " + DateTimeUtils.convert(homeworkId.getTime(), DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
                } else {
                    System.out.println("课程类型错误 type = " + lessonEntry.getType());
                }
            }
        }

    }


    public void updateCon() {
        //查找所有作业
        HomeWorkService homeWorkService = new HomeWorkService();
        LessonService lessonService = new LessonService();
        ExerciseItemService exerciseItemService = new ExerciseItemService();
        List<HomeWorkEntry> homeWorkEntryList = homeWorkService.findAllHomeworkEntry();
        if (homeWorkEntryList != null && homeWorkEntryList.size() > 0) {
            long size = homeWorkEntryList.size();
            long i = 0;
            for (HomeWorkEntry homeWorkEntry : homeWorkEntryList) {
                i++;
                System.out.println("==============================" + i + "/" + size);
//                LessonEntry lessonEntry = lessonService.getLessonEntry(homeWorkEntry.getLessonId());
//                homeWorkEntry.setVideoNum(lessonEntry.getVideoIds().size() > 0 ? 1 : 0);
//                homeWorkEntry.setFileNum(lessonEntry.getDocumentCount() > 0 ? 1 : 0);
//                homeWorkEntry.setExerciseNum(lessonEntry.getExercise() != null ? 1 : 0);


                LessonEntry lessonEntry = lessonService.getLessonEntry(homeWorkEntry.getLessonId());
                homeWorkEntry.setVideoNum(lessonEntry.getVideoIds().size());
                homeWorkEntry.setFileNum(lessonEntry.getDocumentCount());
                homeWorkEntry.setVoiceNum(homeWorkEntry.getVoiceFile().size());

                List<ObjectId> exercises = new ArrayList<ObjectId>();
                ObjectId exerciseId = lessonEntry.getExercise();
                exercises.add(exerciseId);
                Map<ObjectId, Integer> map = exerciseItemService.statItemCount(exercises);
                Integer exerciseNum = exerciseId == null ? new Integer(0) : map.get(exerciseId);
                if (exerciseNum == null) {
                    homeWorkEntry.setExerciseNum(0);
                } else {
                    homeWorkEntry.setExerciseNum(exerciseNum);
                }
                homeWorkService.editHomeWork(homeWorkEntry);
                System.out.println("更新成功 homeworkId=" + homeWorkEntry.getID().toString());

            }
        } else {
            System.out.println("没查到作业");
        }
    }

    /**
     * k6kt小助手导出作业
     *
     * @param response
     * @return
     */
    @RequestMapping("/exportExcel")
    @ResponseBody
    public boolean exportHomeWorkExcel(HttpServletResponse response) {
        homeWorkService.exportHomeWorkExcel(response);
        return true;

    }

    @RequestMapping("/sendLetters")
    @ResponseBody
    public RespObj sendLetters(@ObjectIdType ObjectId homeworkId, @ObjectIdType ObjectId classId, int classType, HttpServletRequest request) throws Exception {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        Map<String, Object> map = getSubmitInfo(homeworkId, classId, "", classType);
        Map<String, SubmitInfoDTO> infoDTOMap = (Map<String, SubmitInfoDTO>) map.get("infoDTOMap");
        StringBuilder recipient = new StringBuilder();
        for (Map.Entry<String, SubmitInfoDTO> entry : infoDTOMap.entrySet()) {
            SubmitInfoDTO submitInfoDTO = entry.getValue();
            if (submitInfoDTO.getSubNum() == 0) {
                recipient.append(entry.getKey() + ";");
            }
        }
        HomeWorkEntry entry = homeWorkService.getHomeWorkEntry(homeworkId);
        String message = "您还未完成作业 《" + entry.getName() + "》，请抓紧时间完成。";
        LetterController letterController = (LetterController) SpringContextUtil.getBean("letterController");
        letterController.addLetter(recipient.toString(), message, request);
        return respObj;
    }

}
