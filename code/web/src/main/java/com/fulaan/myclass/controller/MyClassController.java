package com.fulaan.myclass.controller;

import com.alibaba.fastjson.JSON;
import com.fulaan.annotation.ObjectIdType;
import com.fulaan.base.controller.BaseController;
import com.fulaan.experience.service.ExperienceService;
import com.fulaan.interestCategory.service.InterestCategoryService;
import com.fulaan.interestCategory.service.InterestClassTermsService;
import com.fulaan.myclass.service.ClassExcelService;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.controller.GradeView;
import com.fulaan.myschool.service.InterestClassService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.user.service.UserService;
import com.fulaan.utils.TermUtil;
import com.fulaan.zouban.service.TermService;
import com.pojo.app.FieldValuePair;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.attendance.Attendance;
import com.pojo.interestCategory.InterestClassTermsDTO;
import com.pojo.school.*;
import com.pojo.user.*;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.FileUtil;
import com.sys.utils.RespObj;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.text.Collator;
import java.util.*;

/**
 * Created by Hao
 */
@Controller
@RequestMapping("/myclass")
public class MyClassController extends BaseController {

    @Resource
    private UserService userService;
    @Resource
    private ClassService classService;
    @Resource
    private InterestClassService interestClassService;
    @Resource
    private SchoolService schoolService;
    @Resource
    private ExperienceService experienceService;
    private ClassExcelService excelService=new ClassExcelService();
    @Resource
    private InterestCategoryService interestCategoryService;
    @Resource
    private InterestClassTermsService interestClassTermsService;
    @Autowired
    private TermService termService;


    private UserDetailInfoDTO findUser() {
        String userId = getUserId().toString();
        UserDetailInfoDTO userInfoDTO4WB = userService.getUserInfoById(userId);
        if (UserRole.isParent(userInfoDTO4WB.getRole())) {
            userInfoDTO4WB = userService.findStuInfoByParentId(userId);
        }
        return userInfoDTO4WB;
    }

    /*
    *
    * 获取真正需要的用户信息
    *
    * */
    @RequestMapping("/studentinfo")
    @ResponseBody
    public UserDetailInfoDTO findRealUserInfo() {
        return findUser();
    }

    //=======================================学生角色 我的班级================================================================
    /*
    *
    *
    * 我的班级学生页面
    *
    * */
    @RequestMapping("/myclass4stu")
    public String myClass(Model model) {
        UserDetailInfoDTO userInfoDTO4WB = findRealUserInfo();
        List<ClassInfoDTO> classInfoDTOList = classService.findClassInfoByStuId(new ObjectId(userInfoDTO4WB.getId()));
        Collections.reverse(classInfoDTOList);
        model.addAttribute("classInfos", classInfoDTOList);
        return "myclass/studentClass";
    }

    /*
   *
   * 我的班级-->学生列表
   * */
    @RequestMapping("/stulist")
    @ResponseBody
    public Map<String, Object> listAllStudent(@RequestParam String classId, @RequestParam Integer classType) {
        UserDetailInfoDTO userInfoDTO4WB = findRealUserInfo();
        int role = userInfoDTO4WB.getRole();
        if (!UserRole.isStudent(role) && !UserRole.isParent(role)) {
            throw new RuntimeException("角色不匹配，非法访问");
        }
        List<UserDetailInfoDTO> userInfoDTO4WBList;
        if (classType == 1) {
            userInfoDTO4WBList = classService.findStuByClassId(classId);
        } else if (classType == 2) {
            userInfoDTO4WBList = interestClassService.findStuByClassId(classId);
        } else {
            throw new RuntimeException("非法参数: classType:" + classType);
        }
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("rows", userInfoDTO4WBList);
        returnMap.put("code", 200);
        return returnMap;
    }
//===============================================老师角色 我的班级========================================================

    @RequestMapping("/myclass4tea")
    public String listClass(Model model) {
        UserDetailInfoDTO userDetailInfoDTO = userService.getUserInfoById(getUserId().toString());
        model.addAttribute("currentUser", userDetailInfoDTO);
        return "myclass/teacherClass";
    }

    /*
    *
    * 当前老老师所教班级集合  不包括兴趣班
    *
    * */
    @RequestMapping("/classinfos")
    @ResponseBody
    public Map<String, Object> classInfos() {
        UserDetailInfoDTO userInfoDTO4WB = userService.getUserInfoById(getUserId().toString());
        boolean k = UserRole.isStudentOrParent(userInfoDTO4WB.getRole());
        if (k) {
            throw new RuntimeException("角色不匹配，非法访问");
        }
        List<ClassInfoDTO> classInfoDTOList = classService.findClassInfoByTeacherId(new ObjectId(userInfoDTO4WB.getId()));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows", classInfoDTOList);
        return map;
    }

    @RequestMapping("/findClassByMasterId")
    @ResponseBody
    public RespObj findClassByMasterId() {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<ClassEntry> classEntries = classService.findClassByMasterId(getUserId());
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for(ClassEntry classEntry : classEntries){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("cId", classEntry.getID().toString());
            map.put("cNm", classEntry.getName());
            map.put("mst", getSessionValue().getUserName());
            map.put("sNo", classEntry.getStudents().size());
            list.add(map);
        }
        respObj.setMessage(list);
        return respObj;
    }

    /*
    *
    * 导向经验值加分页面
    *
    * */
    @RequestMapping("/addexp")
    public String experience(@RequestParam String classId, Model model) {
        UserDetailInfoDTO userInfoDTO4WB = findRealUserInfo();
        if (UserRole.isStudentOrParent((int) userInfoDTO4WB.getRole())) {
            throw new RuntimeException("角色不匹配，非法访问");
        }
        ClassInfoDTO classInfoDTO = classService.findClassInfoByClassId(classId);
        List<UserDetailInfoDTO> userInfoDTO4WBList = userService.findUserInfoByIds(classInfoDTO.getStudentIds());
        model.addAttribute("rows", userInfoDTO4WBList);
        model.addAttribute("total", classInfoDTO.getStudentIds().size());
        model.addAttribute("code", 200);
        model.addAttribute("classId", classId);
        return "myclass/teacherScoreEdit";
    }

    /*
    *
    * 保存加分结果
    *
    * */
    @RequestMapping("/saveexp")
    @ResponseBody
    public boolean saveExp(@RequestParam String scoreData) {
        List<UserIdAndScoreView> teacherScoreJsonList = JSON.parseArray(scoreData, UserIdAndScoreView.class);
        //userService.addExp4Student(teacherScoreJsonList);
        for (UserIdAndScoreView teacherScoreJson : teacherScoreJsonList) {
            experienceService.updateScore(teacherScoreJson.getUserId().toString(), ExpLogType.RATE, teacherScoreJson.getScore(), getUserId().toString());
        }
        return true;
    }

    /*
    *
    *
    *  导向班级统计页面
    *
    *
    * */
    @RequestMapping("/lead2stat")
    public String lead2stat(@RequestParam String classId, Model model) {
        model.addAttribute("classId", classId);
        ClassEntry classEntry = classService.getClassEntryById(new ObjectId(classId), Constant.FIELDS);
        if(null != classEntry){
            ObjectId masterId = classEntry.getMaster();
            if(null!= masterId && masterId.equals(getUserId())){
                model.addAttribute("master", 1);
            } else {
                model.addAttribute("master", 0);
            }
        }
        return "myclass/classManage";
    }


    /*
    *
    * 班级所有老师列表
    *
    * */
    @RequestMapping("/teacherinofs")
    @ResponseBody
    public Map<String, Object> teacherInfo(@RequestParam String classId) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<TeacherInfoView> userDetailInfoDTOs = classService.findTeachersByClassId(new ObjectId(classId));
        map.put("teacherList", userDetailInfoDTOs);
        ClassInfoDTO classInfoDTO=classService.findClassInfoByClassId(classId);
        String className=classInfoDTO==null?interestClassService.findInterestClassByClassId(classId).getClassName():classInfoDTO.getClassName();

        map.put("className",className);
        map.put("role", getSessionValue().getUserRole());
        return map;
    }

    /*
   * 学生统计页面   视频观看数量统计
   *
   * */
    @RequestMapping("/statstus")
    @ResponseBody
    public Map<String, Object> stusState(@RequestParam String classId) {
        List<StudentStat> userInfoDTOList = classService.statCommonClassStudentInfo(new ObjectId(classId));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("totalList", userInfoDTOList);
        map.put("classtype", 1);
        return map;
    }


    @RequestMapping("/finduserinfo")
    @ResponseBody
    public UserDetailInfoDTO findUserInfo(@RequestParam String userId) {
        UserDetailInfoDTO userDetailInfoDTO = userService.getUserInfoById(userId);
        return userDetailInfoDTO;
    }


    /*
    *
    *
    *跳转到单个学生统计页面
    *
    * */
    @RequestMapping("/lead2stustat")
    public String lead2StuSta(@RequestParam String studentId, Model model) {
        model.addAttribute("studentId", studentId);
        return "myclass/studentStat";
    }

    /*
    *
    * 针对单独学生的统计
    *
    * */
    @RequestMapping("/stastu")
    @ResponseBody
    public Map<String, Object> statestu(@RequestParam String studentId) {
        List<StudentLessonStatView> studentLessonStatViewList = classService.statStudent(studentId);
        Map<String, Object> returnMap = new HashMap<String, Object>();
        returnMap.put("rows", studentLessonStatViewList);
        return returnMap;
    }


    /*＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝以下为  才艺课/拓展课  相关接口===========================================*/
    @RequestMapping("/tointerestclass")
    public String interestClass(String type, Model model) {
        model.addAttribute("type", type);
        List<ClassEntry> classEntries = classService.findClassByMasterId(getUserId());
        int isMaster = classEntries.size()<=0 ? 0 : 1;
        model.addAttribute("isMaster", isMaster);
        return "interestclass/teacherInterestClass";
    }

    /*
    * 兴趣班集合信息
    * */
    @RequestMapping("/interestclass")
    @ResponseBody
    public Map<String, Object> interestClassInfos(String term, @RequestParam(defaultValue = "") String gradeId, @RequestParam(defaultValue = "") String categoryId,
                                                  @RequestParam(required = false, defaultValue = "-1") int termType) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        //角色判断  如果为校长显示当前学校所有班级   如果为老师 显示所带班级
        int role = getSessionValue().getUserRole();
        List<InterestClassDTO> classInfoDTOList = null;
        if (UserRole.isHeadmaster(role)) {
            classInfoDTOList = schoolService.findAllInterestClass(getSessionValue().getSchoolId(), termType, gradeId, categoryId);
        } else if (UserRole.isTeacher(role)) {
            classInfoDTOList = schoolService.findInterestClassByTeacherId(getSessionValue().getId(), termType, getSessionValue().getSchoolId());
        } else {
            return null;
        }
        returnMap.put("rows", classInfoDTOList);
        return returnMap;
    }

    @RequestMapping("/getGradeAndCategory")
    @ResponseBody
    public Map<String, Object> getGradeAndCategory(){
        Map<String, Object> model  =new HashMap<String, Object>();
        List<Map<String, Object>> list =(List<Map<String, Object>>)interestCategoryService.getInterestCategoryList(new ObjectId(getSessionValue().getSchoolId())).get("list");
        model.put("categoryList", list);
        List<GradeView> gradeList = schoolService.findGradeList(getSessionValue().getSchoolId());
        model.put("gradeList", gradeList);
        InterestClassTermsDTO interestClassTermsDTO = interestClassTermsService.getInterestClassTermsDTO(new ObjectId(getSessionValue().getSchoolId()));
        model.put("termList", interestClassTermsDTO.getTerms());

        int role = getSessionValue().getUserRole();
        if (UserRole.isHeadmaster(role)) {
            model.put("role", true);
        } else {
            model.put("role", false);
        }
        if (UserRole.isManager(role)) {
            model.put("isManager", true);
        } else {
            model.put("isManager", false);
        }
        return model;
    }


    //跳转课时页面
    @RequestMapping("/lead2ls")
    public String lead2LessonScore(@RequestParam Integer type, @RequestParam String classId,@RequestParam(required = false, defaultValue = "-1") int termType, Model model) {
//        model.addAttribute("lessonScores", classService.countLessonsByClassId(type, classId));
        List<Map<String, Object>> lesson = classService.getLessonIndexWeekIndexAndName(new ObjectId(classId), termType);
        model.addAttribute("lessonScores",lesson);
        return "interestclass/manageLesson";
    }

    @RequestMapping("/lead2lsForApp")
    @ResponseBody
    public List<FieldValuePair> lead2LessonScoreForApp(String classId,@RequestParam(required = false, defaultValue = "-1") int termType){
        return classService.getLessonIndexAndName(new ObjectId(classId), termType);
    }

    //接收ajax 课时列表请求
    @RequestMapping("/findLessonScores")
    @ResponseBody
    public List<InterestClassLessonScore> findLessonScores(@RequestParam String classId) {
        List<InterestClassLessonScore> lessonScoreList = classService.findLessonScoreByClassId(classId);
        return lessonScoreList;
    }

    //课时页面添加课时按钮
    @RequestMapping("/addlesson")
    public String getLessonHour(@RequestParam String classId,
                                @RequestParam(value = "idx")Integer index, @RequestParam(required = false, defaultValue = "-1")int termType, Map<String, Object> model) {
        //根据班级找到所有学生
        List<InterestClassLessonScore> scoreList = new ArrayList<InterestClassLessonScore>();
        if (index == null) {
            List<UserDetailInfoDTO> userInfoList = interestClassService.findAllInterestClassStuByClassId(classId, termType);
            scoreList = new ArrayList<InterestClassLessonScore>();
            for (UserDetailInfoDTO userInfo : userInfoList) {
                InterestClassLessonScore lessonScore = new InterestClassLessonScore();
                lessonScore.setClassid(classId);
                lessonScore.setUserid(userInfo.getId());
                lessonScore.setStudentName(userInfo.getUserName());
                lessonScore.setStudentAvatar(userInfo.getImgUrl());
                lessonScore.setAttendance(1);
                scoreList.add(lessonScore);
            }
        } else {
            List<InterestClassLessonScore> scoreList1 = classService.findLessonScoreByIndexAndClassId(classId, index, termType);
            Map<String, Integer> map = new HashMap<String, Integer>();
            if(null != scoreList1){
                for(InterestClassLessonScore score : scoreList1){
                    if(!map.containsKey(score.getUserid())){
                        scoreList.add(score);
                        map.put(score.getUserid(), 1);
                    }
                }

            }
            if (scoreList != null) {
                List<ObjectId> idList = new ArrayList<ObjectId>();
                for (InterestClassLessonScore lessonScore : scoreList) {
                    if(!"".equals(lessonScore.getUserid())) {
                        idList.add(new ObjectId(lessonScore.getUserid()));
                    }
                }
                Map<ObjectId, UserEntry> userMap = userService.getUserEntryMap(idList, Constant.FIELDS);

                for (InterestClassLessonScore lessonScore : scoreList) {
                    if(!"".equals(lessonScore.getUserid())) {
                        UserEntry userEntry = userMap.get(new ObjectId(lessonScore.getUserid()));
                        if (userEntry != null) {
                            lessonScore.setStudentName(userEntry.getUserName());
                            lessonScore.setStudentAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(), Constant.ONE));
                        }
                    }
                }
            }
        }

        Collections.sort(scoreList, new Comparator<InterestClassLessonScore>() {
            @Override
            public int compare(InterestClassLessonScore o1, InterestClassLessonScore o2) {
                String name1 = o1.getStudentName();
                String name2 = o2.getStudentName();
                return Collator.getInstance(Locale.CHINESE).compare(name1, name2);
//                return name1.compareTo(name2);
            }
        });

        model.put("scoreList", scoreList);
        return "interestclass/eachLesson";
    }

    @RequestMapping("/addlessonForApp")
    @ResponseBody
    public List<InterestClassLessonScore> getLessonHourForApp(String classId, Integer index,  @RequestParam(required = false, defaultValue = "-1")int termType) {
        Map<String, Object> model = new HashMap<String, Object>();
        getLessonHour(classId, index, termType, model);
        return (List<InterestClassLessonScore>)model.get("scoreList");
    }

    @RequestMapping("/appAddLesson")
    @ResponseBody
    public RespObj appAddLesson(String classId, @RequestParam(required = false, defaultValue = "-1")int termType){
        RespObj respObj = new RespObj(Constant.FAILD_CODE);
        try {
            List<InterestClassLessonScore> scoreList;
            List<UserDetailInfoDTO> userInfoList = interestClassService.findAllInterestClassStuByClassId(classId, termType);
            scoreList = new ArrayList<InterestClassLessonScore>();
            for (UserDetailInfoDTO userInfo : userInfoList) {
                InterestClassLessonScore lessonScore = new InterestClassLessonScore();
                lessonScore.setLessonindex(0);
                lessonScore.setClassid(classId);
                lessonScore.setUserid(userInfo.getId());
                lessonScore.setStudentName(userInfo.getUserName());
                lessonScore.setStudentAvatar(userInfo.getImgUrl());
                lessonScore.setAttendance(1);
                lessonScore.setStuscore(5);
                lessonScore.setTeacherComment("");
                lessonScore.setPictureUrl(null);
                scoreList.add(lessonScore);
            }
            if(userInfoList.size()>0) {
                try {
                    saveLessonScoreInfo(scoreList, termType);
                } catch (Exception e){
                    respObj.setMessage(e.getMessage());
                    return respObj;
                }
                respObj = RespObj.SUCCESS;
            } else {
                respObj.setMessage("该班级下没有学生，不能新建课时！");
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return respObj;
    }

    @RequestMapping("/interstat")
    public String lead2interestStat(@RequestParam String classId, @RequestParam int termType) {
        return "interestclass/classStat";
    }

    /**
     * 兴趣班考勤
     * @param classId
     * @return
     */
    @RequestMapping("/interatt")
    public String lead2interestAtt(@RequestParam String classId,Model model)
    {
        model.addAttribute("classId", classId);
        return "interestclass/attendance";
    }

    @RequestMapping("/intereststat")
    @ResponseBody
    public Map<String, Object> interestStuState(@RequestParam String classId, @RequestParam(required = false, defaultValue = "-1") int termType) {
        List<StudentStat> userInfoDTOList = classService.statInterestClassStudentInfo(new ObjectId(classId), termType);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("totalList", userInfoDTOList);
        map.put("classtype", 2);
        return map;
    }

    @RequestMapping("/stuLessons")
    public String stuLessons(String classId, String stuId, Map<String, Object> map, @RequestParam(required = false, defaultValue = "-1") int termType) {
        List<InterestClassLessonScore> lessonScoreList = classService.findLessonScoreByUserIdAndClassId(stuId, classId);
        map.put("totalList", lessonScoreList);
        map.put("classtype", 2);
        map.put("dto", getClassTranscript(classId, stuId, termType));
        return "interestclass/stuLessons";
    }

    @RequestMapping("/stuLessonsForApp")
    @ResponseBody
    public List<InterestClassLessonScore> stuLessonsForApp(String classId, String stuId){
        return classService.findLessonScoreByUserIdAndClassId(stuId, classId);
    }


    @RequestMapping("/removeLesson")
    @ResponseBody
    public boolean removeLesson(@RequestParam String classId, @RequestParam Integer lessonIndex,  @RequestParam(required = false, defaultValue = "-1") int termType) {
        try {
            classService.deleteLessonScoreByClassIdAndLessonIndex(classId, lessonIndex, termType, getSchoolId().toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //处理 每课时评分页面的保存按钮请求
    @RequestMapping(value = "/saveLessonScore", method = RequestMethod.POST)
    @ResponseBody  //批量接收json对象
    public String saveLessonScoreInfo(@RequestBody List<InterestClassLessonScore> scoreList, @RequestParam(required = false, defaultValue = "-1") int termType) throws Exception{
        if (scoreList != null && !scoreList.isEmpty()) {
            InterestClassLessonScore score = scoreList.get(0);
            int index = score.getLessonindex();
            String lessonName = score.getLessonName();
            int weekIndex = 1;
            try{
                weekIndex = termService.findWeekIndex(getSessionValue().getSchoolId(), TermUtil.getSchoolYear());
            } catch (Exception e){
                throw new Exception("未设置教学周，请联系管理员设置");
            }
            if (index == 0) {
                //获取下一个课时序号
                index = classService.getLessonScoreNextIndex(score.getClassid(), termType);
                lessonName = "课时" + index;
            } else {
                weekIndex = classService.deleteLessonScoreByClassIdAndLessonIndex(score.getClassid(), index, termType, getSessionValue().getSchoolId());
            }

            //插入数据
            for (InterestClassLessonScore lessonScore : scoreList) {
                //如果lessonIndex==0  说明是新添加  反之 则是数据修改
                lessonScore.setLessonindex(index);
                lessonScore.setLessonName(lessonName);

                InterestClassLessonScoreEntry scoreEntry = lessonScore.exportEntry();
                scoreEntry.setWeekIndex(weekIndex);
                classService.saveInterestClassLessonScore(scoreEntry, termType);
            }

        }

        return "success";
    }

    @RequestMapping("/editLessonName")
    @ResponseBody
    public RespObj editLessonName(@ObjectIdType ObjectId classId, int index, String lessonName, @RequestParam(required = false, defaultValue = "1")int weekIndex){
        classService.editLessonName(classId, index, lessonName, weekIndex);
        return RespObj.SUCCESS;
    }

    //课时页面期末总评按钮
    @RequestMapping("/finalcomment")
    public String finalAssessment(@RequestParam String classId, @RequestParam Integer type, Model model,  @RequestParam(required = false, defaultValue = "-1")int termType) {
        //获取学生列表
        List<UserDetailInfoDTO> userInfoList = interestClassService.findAllInterestClassStuByClassId(classId, termType);
        Collections.sort(userInfoList, new Comparator<UserDetailInfoDTO>() {
            @Override
            public int compare(UserDetailInfoDTO o1, UserDetailInfoDTO o2) {
                String name1 = o1.getUserName();
                String name2 = o2.getUserName();
                return Collator.getInstance(Locale.CHINESE).compare(name1, name2);
            }
        });
        List<InterestClassTranscriptDTO> transcriptList = (List<InterestClassTranscriptDTO>)finalAssessmentForApp(classId, termType).get("transcriptList");
        model.addAttribute("stuList", userInfoList);
        HashMap<String, InterestClassTranscriptDTO> transcriptHashMap = new LinkedHashMap<String, InterestClassTranscriptDTO>();
        for (InterestClassTranscriptDTO transcript : transcriptList) {
            transcriptHashMap.put(transcript.getUserid(), transcript);
        }
        model.addAttribute("tranScriptMap", transcriptHashMap);
        return "interestclass/termEnd";
    }

    @RequestMapping("/finalcommentForApp")
    @ResponseBody
    public Map<String, Object> finalAssessmentForApp(String classId, @RequestParam(required = false, defaultValue = "-1")int termType){
        Map<String, Object> model = new HashMap<String, Object>();
        //获取学生列表
        List<UserDetailInfoDTO> userInfoList = interestClassService.findAllInterestClassStuByClassId(classId,termType);
        Map<String, UserDetailInfoDTO> userMap = new HashMap<String, UserDetailInfoDTO>();
        if(null != userInfoList){
            for(UserDetailInfoDTO user : userInfoList){
                userMap.put(user.getId(), user);
            }
        }
        List<InterestClassTranscriptDTO> transcriptList = classService.findTranscriptByClassId(classId, termType);
        Map<String, InterestClassTranscriptDTO> transcriptDTOMap = new HashMap<String, InterestClassTranscriptDTO>();
        //构建Map
        if(null!=transcriptList && transcriptList.size()>0){
            for(InterestClassTranscriptDTO dto : transcriptList){
                transcriptDTOMap.put(dto.getUserid(),dto);
            }
        }

        List<InterestClassTranscriptDTO> transcriptDTOs = new ArrayList<InterestClassTranscriptDTO>();
        if(null!=transcriptList && transcriptList.size()>0){
            for(UserDetailInfoDTO user : userInfoList){
                InterestClassTranscriptDTO transcriptDTO = transcriptDTOMap.get(user.getId());
                if(null != transcriptDTO){
                    transcriptDTO.setUserName(user.getUserName());
                    transcriptDTO.setUserAvatar(user.getImgUrl());
                    transcriptDTOs.add(transcriptDTO);
                } else {
                    InterestClassTranscriptDTO transcript = new InterestClassTranscriptDTO(classId, user.getId());
                    transcript.setUserName(user.getUserName());
                    transcript.setUserAvatar(user.getImgUrl());
                    transcriptDTOs.add(transcript);
                }
            }

        } else {
            for(UserDetailInfoDTO user : userInfoList){
                InterestClassTranscriptDTO transcript = new InterestClassTranscriptDTO(classId, user.getId());
                transcript.setUserName(user.getUserName());
                transcript.setUserAvatar(user.getImgUrl());
                List<InterestClassLessonScore> lessonScoreList = classService.findLessonScoreByUserIdAndClassId(user.getId(), classId);
                int aveScore = 0;
                if(null != lessonScoreList && lessonScoreList.size()>0){
                    int sum = 0;
                    for(InterestClassLessonScore lesson : lessonScoreList){
                        sum += lesson.getStuscore();
                    }
                    aveScore = (int)Math.round(sum*1.0/lessonScoreList.size());
                }
                transcript.setFinalresult(aveScore);//期末成绩
                transcript.setSemesterscore(aveScore);//平时成绩
                transcriptDTOs.add(transcript);
            }

        }
        Collections.sort(transcriptDTOs, new Comparator<InterestClassTranscriptDTO>() {
            @Override
            public int compare(InterestClassTranscriptDTO dto1, InterestClassTranscriptDTO dto2) {
                String name1 = dto1.getUserName();
                String name2 = dto2.getUserName();
                return Collator.getInstance(Locale.CHINESE).compare(name1, name2);
            }
        });
        model.put("transcriptList", transcriptDTOs);
        return model;
    }

    //文件上传
    @RequestMapping("/upload/lessonResultPic")
    @ResponseBody
    public String uploadFile(@RequestParam("Filedata") MultipartFile file,
                             @RequestParam String classId,
                             @RequestParam String userId,
                             HttpServletRequest request,
                             @RequestParam(required = false, defaultValue = "-1") int termType) {
        //不能写死 要使用API的文件路径分隔符 不同系统文件分隔符不一样
        String path = "/upload/transcript" + "/" + FileUtil.randomPath();
        String filePath = path + "/" + file.getOriginalFilename();
        path = request.getServletContext().getRealPath(path);
        File newFile = new File(FileUtil.mkDir(path), file.getOriginalFilename());
        if (!file.isEmpty()) {
            try {
                file.transferTo(newFile);
            } catch (IOException e) {
                e.printStackTrace();
                return "failure";
            }
        }
        InterestClassTranscriptDTO transcript = new InterestClassTranscriptDTO();
        transcript.setUserid(userId);
        transcript.setClassid(classId);
        transcript.setResultspicsrc(filePath);
        InterestClassTranscriptDTO trans = interestClassService.findTranscriptByUserIdAndClassId(userId, classId, termType);
        if (trans != null) {
            //db中已经存在该条记录
            transcript.setId(trans.getId());
            classService.updatePicByIdSelective(transcript);
        } else {
            classService.insertTranscript(transcript, termType);
        }
        return filePath;
    }

    //文件上传
    @RequestMapping("/upload/lessonResultPicture")
    @ResponseBody
    public String uploadLessonPicture(@RequestParam("Filedata") MultipartFile file,
                             @ObjectIdType ObjectId classId,
                             @ObjectIdType ObjectId userId,
                             Integer index,
                             HttpServletRequest request,
                             @RequestParam(required = false, defaultValue = "-1") int termType) {
        //不能写死 要使用API的文件路径分隔符 不同系统文件分隔符不一样
        String path = "/upload/transcript" + "/" + FileUtil.randomPath();
        String filePath = path + "/" + file.getOriginalFilename();
        path = request.getServletContext().getRealPath(path);
        File newFile = new File(FileUtil.mkDir(path), file.getOriginalFilename());
        if (!file.isEmpty()) {
            try {
                file.transferTo(newFile);
            } catch (IOException e) {
                e.printStackTrace();
                return "failure";
            }
        }

        if (index == 0) {
            //获取下一个课时序号
            index = classService.getLessonScoreNextIndex(classId.toString(), termType);
        }

        InterestClassLessonScoreEntry entry = classService.findLessonScoreEntry(classId, userId, index);
        if(entry != null){//更新
            System.out.println("更新");
            entry.setLessonPictureUrl(filePath);
            classService.updateInterestClassLessonScore(entry);
//        } else {
//            InterestClassLessonScoreEntry newEntry = new InterestClassLessonScoreEntry();
//            newEntry.setUserId(userId);
//            newEntry.setClassId(classId);
//            newEntry.setLessonPictureUrl(filePath);
//            newEntry.setLessonIndex(index);
//            classService.saveInterestClassLessonScore(newEntry);
        }

        return filePath;
    }

    /**
     * 拓展课作业中的图片直接推送到课时成果
     * @param classId
     * @param userId
     * @param index
     * @param picUrl
     * @return
     */
    @RequestMapping("/homeworkPushPic")
    @ResponseBody
    public RespObj homeworkPushPic(@ObjectIdType ObjectId classId, @ObjectIdType ObjectId userId, int index, String picUrl){
        RespObj respObj = RespObj.FAILD;
        if(index<=0){
            respObj.setMessage("课时不存在");
        } else {
            InterestClassLessonScoreEntry entry = classService.findLessonScoreEntry(classId, userId, index);
            entry.setLessonPictureUrl(picUrl);
            classService.updateInterestClassLessonScore(entry);
            respObj = RespObj.SUCCESS;
        }
        return respObj;
    }

    /**
     * 拓展课作业中的图片直接推送到期末总评
     * @param classId
     * @param userId
     * @param picUrl
     * @return
     */
    @RequestMapping("/homeworkPushPicture")
    @ResponseBody
    public RespObj homeworkPushPicture(String classId, String userId, String picUrl){
        RespObj respObj = RespObj.FAILD;
        InterestClassTranscriptDTO transcript = new InterestClassTranscriptDTO();
        transcript.setUserid(userId);
        transcript.setClassid(classId);
        transcript.setResultspicsrc(picUrl);
        int termType = schoolService.findSchoolById(getSessionValue().getSchoolId()).getSchoolTermType();
        InterestClassTranscriptDTO trans = interestClassService.findTranscriptByUserIdAndClassId(userId, classId, termType);
        if (trans != null) {
            //db中已经存在该条记录
            transcript.setId(trans.getId());
            classService.updatePicByIdSelective(transcript);
        } else {
            classService.insertTranscript(transcript, termType);
        }
        respObj = RespObj.SUCCESS;
        return respObj;
    }

    //处理打印成绩单按钮
    @RequestMapping("/printTranscript")
    public String transcript(@RequestParam String userId, @RequestParam String classId,@RequestParam(required = false, defaultValue = "-1")int termType, Model model) {
        //先获取总成绩单view对象  再获取每课时成绩
        TranscriptView transcriptView = classService.findTranscriptViewByUserIdAndClassId(userId, classId, termType);
        if(transcriptView == null){
            transcriptView = new TranscriptView();
            InterestClassEntry interestClassEntry = classService.findInterestClassEntry(new ObjectId(classId));
            transcriptView.setClassname(interestClassEntry.getClassName());
            UserDetailInfoDTO student = userService.getUserInfoById(userId);
            UserDetailInfoDTO teacher = userService.getUserInfoById(interestClassEntry.getTeacherId().toString());
            transcriptView.setTeacherNAME(teacher.getUserName());
            transcriptView.setNickName(student.getUserName());
        }
        if(termType < 0){
            termType = transcriptView.getTermType();
        }
        InterestClassTermsDTO interestClassTermsDTO = interestClassTermsService.getInterestClassTermsDTO(new ObjectId(getSessionValue().getSchoolId()));
        List<IdNameValuePairDTO> terms = interestClassTermsDTO.getTerms();
        if(terms != null){
            for(IdNameValuePairDTO term : terms){
                if((Integer)term.getValue() == termType){
                    transcriptView.setTermName(term.getName());
                }
            }
        }
        List<InterestClassLessonScore> lessonScoreList = classService.findLessonScoreByUserIdAndClassId(userId, classId);
        model.addAttribute("lessonScoreList", lessonScoreList);
        model.addAttribute("transcriptView", transcriptView);
        return "interestclass/transcript";
    }

    //即时保存 成绩单信息
    @RequestMapping("/saveTranscript")
    @ResponseBody
    public String saveTranscript(InterestClassTranscriptDTO transcript, @RequestParam(defaultValue = "-1", required = false) int termType) {
        if (transcript == null) {
            return "error";
        } else {
            InterestClassTranscriptDTO trans = interestClassService.findTranscriptByUserIdAndClassId(transcript.getUserid(), transcript.getClassid(), termType);
            if (trans != null) {
                //db中已经存在该条记录
                transcript.setId(trans.getId());
                classService.updateByTranscriptIdSelective(transcript);
            } else {
                classService.insertTranscript(transcript, termType);
            }
        }
        return "success";
    }

    @RequestMapping("/saveMultiTranscripts")//app使用
    @ResponseBody
    public String saveMultiTranscripts(@RequestBody List<InterestClassTranscriptDTO> transcripts) {
        int termType = schoolService.findSchoolById(getSessionValue().getSchoolId()).getSchoolTermType();
        if (transcripts == null) {
            return "error";
        } else {
            for (InterestClassTranscriptDTO transcript : transcripts) {
                String result = saveTranscript(transcript, termType);
                if (!result.equals("success")) {
                    return "error";
                }
            }
        }
        return "success";
    }

    @RequestMapping("/saveMultiTranscripts/{termType}")//web使用
    @ResponseBody
    public String saveMultiTranscripts(@RequestBody List<InterestClassTranscriptDTO> transcripts, @PathVariable(value = "termType") int termType) {
        if (transcripts == null) {
            return "error";
        } else {
            for (InterestClassTranscriptDTO transcript : transcripts) {
                String result = saveTranscript(transcript, termType);
                if (!result.equals("success")) {
                    return "error";
                }
            }
        }
        return "success";
    }

    /*-------------------------------------兴趣班学生选课相关页面-------------------------------------------------------*/
    @RequestMapping("/toselinclass")
    public String selectInterestClass() {
        return "interestclass/studentSelectClass";
    }

    @RequestMapping("interestclassofschool")
    @ResponseBody
    public Map<String, Object> findAllInterestClass(@RequestParam(required = false, defaultValue = "-1") int termType,
                     
    		@RequestParam(required = false, defaultValue = "allCategory") String categoryId,@RequestParam(required = false, defaultValue = "0") int gx_type) {
    	
    	
    	
    
        Map<String, Object> map = new HashMap<String, Object>();
        if(categoryId.equals("UN"))
            categoryId = "";
        String userId = getUserId().toString();
        int role=getSessionValue().getUserRole();
        if(UserRole.isParent(role))//学生家长
        {
            userId=userService.findStuInfoByParentId(userId).getId();
        }
        
        ObjectId gradeId=null;
        ClassEntry ce= classService.searchClassEntryByStuId(userId);
        if(null!=ce)
        {
        	Grade g=classService.getClassGradeInfo(ce);
        	if(null!=g)
        	{
        		gradeId=g.getGradeId();
        	}
        }

        List<InterestClassDTO> interestClassDTOList = classService.findAllInterestClassOfSchool(getSessionValue().getSchoolId(),gradeId, true, categoryId,getSessionValue().getSex());
       
        List<InterestClassDTO> newInterestClassDTOList =new ArrayList<InterestClassDTO>();
        
        
        if(0==gx_type)
        {
        	newInterestClassDTOList=interestClassDTOList;
        }else
        {
	        Map<ObjectId,Subject> subjectMap=  schoolService.getSubjectEntryMap(getSessionValue().getSchoolId());
	        if(1==gx_type)
	        {
	        	for(InterestClassDTO dto:interestClassDTOList)
	        	{
	        		if(StringUtils.isNotBlank(dto.getSubjectId()) && ObjectId.isValid(dto.getSubjectId()))
	        		{
	        			Subject sj =subjectMap.get(new ObjectId(dto.getSubjectId()));
	        			
	        			if(null!=sj && SubjectType.isCommonSubject(sj.getName()))
	        			{
	        				newInterestClassDTOList.add(dto);
	        			}
	        		}
	        	}
	        }
	        
	        if(2==gx_type)
	        {
	        	for(InterestClassDTO dto:interestClassDTOList)
	        	{
	        		if(StringUtils.isNotBlank(dto.getSubjectId()) && ObjectId.isValid(dto.getSubjectId()))
	        		{
	        			Subject sj =subjectMap.get(new ObjectId(dto.getSubjectId()));
	        			
	        			if(null==sj ||  !SubjectType.isCommonSubject(sj.getName()))
	        			{
	        				newInterestClassDTOList.add(dto);
	        			}
	        		}
	        	}
	        }
        }
        
        makeInterestClassList(newInterestClassDTOList,userId, termType);
        //=================  临时代码  ==============================
//        if(gradeId.toString().equals("55f14f250cf2f06e97c87a1b")){
//            makeInterestClassListNew(interestClassDTOList);
//        }
        //=================  临时代码结束  ==============================

        map.put("interestClassInfoList", newInterestClassDTOList);
        return map;
    }

    /*
    *
    * 学生所参加的所有兴趣班
    *
    *
    * */
    @RequestMapping("stuinterestclass")
    @ResponseBody
    public Map<String, Object> findStudentInterestClass(@RequestParam(required = false, defaultValue = "-1") int termType) {
        Map<String, Object> map = new HashMap<String, Object>();
        String userId = getUserId().toString();
        int role=getSessionValue().getUserRole();
        if(UserRole.isParent(role))//学生家长
        {
            userId=userService.findStuInfoByParentId(userId).getId();
        }
        List<InterestClassDTO> interestClassDTOList =
                interestClassService.findInterestClassByStudentId(userId, termType);
        makeInterestClassList(interestClassDTOList,userId, termType);
        map.put("interestClassInfoList", interestClassDTOList);
        return map;
    }

    private void makeInterestClassList(List<InterestClassDTO> interestClassDTOList,String userId, int termType) {
        int currentTermType = schoolService.getSchoolEntry(getSchoolId(), Constant.FIELDS).getTermType();
        if(termType == -1){
            termType = currentTermType;
        }
        int role=getSessionValue().getUserRole();

        Map<String, Object> classTimeMap = interestClassService.findClassTimeMap(userId);
        for (InterestClassDTO interestClassDTO : interestClassDTOList) {
            if (interestClassDTO.getOpenTime() != null && interestClassDTO.getCloseTime() != null) {
                boolean m = interestClassDTO.getOpenTime().getTime() < System.currentTimeMillis() ? true : false;
                boolean n = System.currentTimeMillis() < interestClassDTO.getCloseTime().getTime() ? true : false;
                if (m && n) {
                    //当前时间为可选时间
                    interestClassDTO.setOpenState(2);
                } else if (!m) {
                    //选课时间未到 未开放
                    interestClassDTO.setOpenState(1);
                } else if (!n) {
                    //选课时间已经过期
                    interestClassDTO.setOpenState(3);
                }
            }
            if(termType < 0){
                termType = interestClassDTO.getTermType();
            }
            List<InterestClassStudentDTO> studentList1 = interestClassDTO.getStudentList();
            List<InterestClassStudentDTO> studentList = new ArrayList<InterestClassStudentDTO>();
            List<InterestClassStudentDTO> historyStuList = new ArrayList<InterestClassStudentDTO>();
            for(InterestClassStudentDTO dto : studentList1){
                if(dto.getTermType() == currentTermType){
                    studentList.add(dto);
                } else {
                    historyStuList.add(dto);
                }
            }

            //判断是否历史已选
            if(null!=historyStuList && historyStuList.size()>0){
                for(InterestClassStudentDTO studentDTO : historyStuList){
                    if(userId.equals(studentDTO.getStudentId())){
                        //选课时间已经过期
                        interestClassDTO.setOpenState(3);
                        break;
                    }
                }
            }
            List<InterestClassStudentDTO> sls = new ArrayList<InterestClassStudentDTO>();
            List<InterestClassStudentDTO> fls = new ArrayList<InterestClassStudentDTO>();
            if (interestClassDTO.getIsLongCourse() != true) {
                if (studentList != null && studentList.size() != 0) {
                    for (InterestClassStudentDTO dto : studentList) {
                        if (dto.getCourseType() == 1) {
                            fls.add(dto);
                        } else {
                            sls.add(dto);
                        }
                    }
                }
            }
            interestClassDTO.setFstulist(fls);
            interestClassDTO.setSstulist(sls);
            //判断是否已选过
            if (studentList != null) {
                for (InterestClassStudentDTO interestClassStudentDTO : studentList1) {
                    boolean k = userId.equals(interestClassStudentDTO.getStudentId());
                    if (k) {
                        interestClassDTO.setIsChoose(k);
                        interestClassDTO.setChooseType(interestClassStudentDTO.getCourseType());
                        break;
                    }
                }
            }

            //判断是否存在时间冲突
            List<String> timeList = interestClassDTO.getClassTime();
            if (timeList != null) {
                for (String str : timeList) {
                    List<InterestClassStudentDTO> interestClassStudentDTOs = interestClassDTO.getStudentList();
                    if (interestClassStudentDTOs != null) {
                        List<String> courseTypeList = new ArrayList<String>();
                        if (interestClassDTO.getIsLongCourse() == true) {
                            courseTypeList.add("1");
                            courseTypeList.add("2");
                        } else {
                            if ("1".equals(interestClassDTO.getFirstTerm())) {
                                courseTypeList.add("1");
                            }
                            if ("1".equals(interestClassDTO.getSecondTerm())) {
                                courseTypeList.add("2");
                            }
                        }

                        for (String type : courseTypeList) {
                            if (interestClassDTO.getIsLongCourse() == true) {
                                Object o = classTimeMap.get(userId + "-" + type + "-" + str);
                                if (o != null) {
                                    interestClassDTO.setTimeConflict(true);
                                    break;
                                }
                            } else {
                                boolean ft = false;
                                boolean st = false;
                                if ("1".equals(interestClassDTO.getFirstTerm())) {
                                    Object o = classTimeMap.get(userId + "-" + 1 + "-" + str);
                                    if (o != null) {
                                        ft = true;
                                    } else {
                                        interestClassDTO.setFchoose(1);
                                    }
                                }
                                if ("1".equals(interestClassDTO.getSecondTerm())) {
                                    Object o = classTimeMap.get(userId + "-" + 2 + "-" + str);
                                    if (o != null) {
                                        st = true;
                                    } else {
                                        interestClassDTO.setSchoose(1);
                                    }
                                }
                                if (ft && st) {
                                    interestClassDTO.setTimeConflict(true);
                                    break;
                                }
                            }

                        }

                    }
//                    break;
                }
            }

            //只保留当前学生
            interestClassDTO.setStudentList(studentList);
            interestClassDTO.setStudentCount(studentList.size());
        }
    }

    private void makeInterestClassListNew(List<InterestClassDTO> interestClassDTOList){
//        //本次选课列表
//        List<InterestClassDTO> currentList = new ArrayList<InterestClassDTO>();
//        //历史选课列表
//        List<InterestClassDTO> historyList = new ArrayList<InterestClassDTO>();
//
//        for(InterestClassDTO dto : interestClassDTOList){
//            if(currentList.size() == 1){//本学期已选一门
//                InterestClassDTO currentClassDTO = currentList.get(0);
//                if(dto.isRequired()){//被测试拓展课是必修课
//                    if(currentClassDTO.isRequired()){//已选拓展课是必修课
//                        //如果历史必修选了太多  dto.dis
//                    } else {//已选拓展课是选修课
//
//                    }
//                } else {//被测试拓展课是选修课
//                    if(currentClassDTO.isRequired()){//已选拓展课是必修课
//                        //本类型历史选太多 dis
//                    } else {//已选拓展课是选修课
//                        dto.setTimeConflict(true);
//                    }
//                }
//
//            } else if(currentList.size()>=2){//已至少选两门
//                dto.setTimeConflict(true);
//            }
//        }

        //不能选重复类别

        //历史选课列表
        Map<String, Object> map = findStudentInterestClass(-1);
        List<InterestClassDTO> historyList = (List<InterestClassDTO>)map.get("interestClassInfoList");

        for(InterestClassDTO dto : interestClassDTOList){
            for(InterestClassDTO historyDto : historyList){
                if(dto.getTypeId().equals(historyDto.getTypeId())){//本类别已选
                    dto.setTimeConflict(true);
                    break;
                }
            }

        }

    }

    @RequestMapping("/addStuToInterestClass")
    @ResponseBody
    public Map<String, Object> addStudent2InterestClass(@RequestParam String userId,
                                                        @RequestParam String classId,
                                                        @RequestParam int courseType) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        //校验人数上限
        InterestClassDTO interestClassDTO = interestClassService.findInterestClassByClassId(classId);
        if(interestClassDTO.getRelationId() != null && courseType == 0){//应对app BUG
            returnMap.put("code", 500);
            returnMap.put("mesg", "该选课是短课，请更换网页端选课");
            return returnMap;
        }
        if(courseType == 2){
            interestClassDTO = interestClassService.findInterestClassByClassId(interestClassDTO.getRelationId().toString());
        }
        if(interestClassDTO.getState() == 0){
            returnMap.put("code", 500);
            returnMap.put("mesg", "该课程已关闭");
            return returnMap;
        }
        List<InterestClassStudentDTO> totalList = interestClassDTO.getStudentList();
        List<InterestClassStudentDTO> list = new ArrayList<InterestClassStudentDTO>();
        if(null!=totalList && totalList.size()>0){
            for(InterestClassStudentDTO dto : totalList){
                if(dto.getTermType() == interestClassDTO.getTermType()){
                    list.add(dto);
                }
            }
        }

//        List<InterestClassStudentDTO> sls = new ArrayList<InterestClassStudentDTO>();
//        List<InterestClassStudentDTO> fls = new ArrayList<InterestClassStudentDTO>();
        if (list != null) {
            if (courseType!=0) {
//                for(InterestClassStudentDTO dto : list) {
//                    if (dto.getCourseType() == 1) {
//                        fls.add(dto);
//                    } else {
//                        sls.add(dto);
//                    }
//                }
//                if (courseType==1 && fls.size()>=interestClassDTO.getTotalStudentCount()) {
//                    returnMap.put("code", 500);
//                    returnMap.put("mesg", "人数已满");
//                    return returnMap;
//                } else if (courseType==2 && sls.size()>=interestClassDTO.getTotalStudentCount()) {
//                    returnMap.put("code", 500);
//                    returnMap.put("mesg", "人数已满");
//                    return returnMap;
//                }
            } else {
                if (list.size() > 0 && list.size() >= interestClassDTO.getTotalStudentCount()) {
                    returnMap.put("code", 500);
                    returnMap.put("mesg", "人数已满");
                    return returnMap;
                }
            }

        }
        //校验 时间冲突

        try {
            String message = interestClassService.addStudent2ExpandClass(interestClassDTO.getId(), userId, courseType, 0, true);
            if("添加成功".equals(message)){
                returnMap.put("code", 200);
            } else {
                returnMap.put("code", 500);
            }
            returnMap.put("msg", message);
            return returnMap;
        } catch (Exception e) {
            returnMap.put("code", 500);
            returnMap.put("mesg", e.getMessage());
            return returnMap;
        }
    }

    @RequestMapping("/removeStuFromInterestClass")
    @ResponseBody
    public Map<String, Object> removeStuFromInterestClass(@RequestParam String userId,
                                                          @RequestParam String classId,
                                                          @RequestParam int courseType) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        InterestClassDTO interestClassDTO = interestClassService.findInterestClassByClassId(classId);
        List<InterestClassStudentDTO> studentDTOs = interestClassDTO.getCurrentStudentList();
        for(InterestClassStudentDTO studentDTO : studentDTOs){
            if(studentDTO.getStudentId().equals(userId)){
                if(studentDTO.getDropState() == 1){
                    returnMap.put("code", 500);
                    returnMap.put("mesg", "该节课由老师指定，无法退课");
                    return returnMap;
                }
            }
        }
        ObjectId relationId = interestClassDTO.getRelationId();
        if(courseType == 2 && relationId!=null){
            interestClassDTO = interestClassService.findInterestClassByClassId(relationId.toString());
        }
        try {
            interestClassService.deleteStudent2ExpandClass(interestClassDTO.getId(), userId);
            returnMap.put("code", 200);
            return returnMap;
        } catch (Exception e) {
            returnMap.put("code", 500);
            returnMap.put("mesg", "删除学生失败");
            return returnMap;
        }
    }


    /*
    *当前用户相关所有班级 移动app接口
    *
    * */
    @RequestMapping("allclass")
    @ResponseBody
    public Map<String, Object> allClassInfo() {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        int role = getSessionValue().getUserRole();
        ObjectId userId = getUserId();
        List<InterestClassDTO> interestClassDTOList = null;
        List<ClassInfoDTO> classInfoDTOList = null;
        if (UserRole.isParent(role)) {
        	UserEntry e=userService.searchUserId(getUserId());
        	
           // UserDetailInfoDTO userDetailInfoDTO = userService.findStuInfoByParentId(userId.toString());
           // userId = new ObjectId(userDetailInfoDTO.getId());
            classInfoDTOList = classService.findClassInfoByStuId(e.getConnectIds().get(0));
            interestClassDTOList = interestClassService.findInterestClassByStudentId(e.getConnectIds().get(0).toString(), -1);
        }
        if (UserRole.isStudent(role)) {
            classInfoDTOList = classService.findClassInfoByStuId(userId);
            interestClassDTOList = interestClassService.findInterestClassByStudentId(userId.toString(), -1);
        } else if (UserRole.isTeacher(role)) {
            classInfoDTOList = classService.findClassInfoByTeacherId(userId);
            interestClassDTOList = schoolService.findInterestClassByTeacherId(userId.toString(),-1, getSessionValue().getSchoolId());
        } else if (UserRole.isHeadmaster(role)) {
            classInfoDTOList = classService.findClassInfoBySchoolId(getSessionValue().getSchoolId());
            interestClassDTOList = schoolService.findAllInterestClass(getSessionValue().getSchoolId(),-1, "", "");
        } else {

        }
        returnMap.put("commonClass", classInfoDTOList);
        returnMap.put("interestClass", interestClassDTOList);
        return returnMap;
    }

    /**
     * 学号管理
     * @return
     */
    @RequestMapping("nummanage")
    public String numManage(@RequestParam String classId,Model model)
    {
        model.addAttribute("classId", classId);
        return "myclass/numManage";
    }
    @RequestMapping("exportExcel")
    @ResponseBody
    public void exportExcel(@RequestParam String classId,HttpServletResponse response)
    {
        excelService.exportExcel(classId, response);
    }
    @RequestMapping("import")
    @ResponseBody
    public boolean importData(@RequestParam("file") MultipartFile file,String classId) throws Exception {
        //获取班级ID为了校验上传文件是否合适
        String fileName = file.getOriginalFilename();
        if (!fileName.endsWith(".xls")) {
            //throw new Exception("文件格式错误！");
            return false;
        }

        new ClassExcelService().updateStudent(file.getInputStream(),classId);
        return true;
    }
    @RequestMapping("download")
    @ResponseBody
    public void downloadExcel(@RequestParam String classId,HttpServletResponse response)
    {

        excelService.generalExcel(classId, response);
    }
    @RequestMapping("attendance")
    public String goToAttendance(@RequestParam String classId,Model model)
    {
        model.addAttribute("classId", classId);
        ClassEntry classEntry = classService.getClassEntryById(new ObjectId(classId), Constant.FIELDS);
        if(null != classEntry){
            ObjectId masterId = classEntry.getMaster();

            if(null!= masterId && masterId.equals(getUserId())){
                model.addAttribute("master", 1);
            } else {
                model.addAttribute("master", 0);
            }
        }
        return "myclass/attendance";
    }
    @RequestMapping("getStudentList")
    @ResponseBody
    public Map<String,Object> getStudentList(@RequestParam String classId,@RequestParam int type)
    {
        Map<String ,Object> map=new HashMap<String, Object>();
        if(type==0)//普通班
        {
            List<UserDetailInfoDTO> studentList = classService.findStuByClassId(classId);
            map.put("studentList", studentList);
            map.put("className", classService.getClassEntryById(new ObjectId(classId), Constant.FIELDS).getName());
        }
        else {//兴趣班
            List<UserDetailInfoDTO> studentList = classService.findStuByInterestClassId(classId);
            map.put("studentList", studentList);
            map.put("className", interestClassService.findInterestClassByClassId(classId).getClassName());
        }
        return map;
    }

    /**
     * 修改学号或职务，type为0是修改学号，type为1是修改职务
     * @param userId
     * @param value
     * @param type
     * @return
     */
    @RequestMapping("updateStuNumOrJob")
    @ResponseBody
    public boolean updateStuNumOrJob(@RequestParam String userId,@RequestParam String value,@RequestParam int type)
    {
        classService.updateStuNumOrJob(new ObjectId(userId),value,type);
        return true;
    }


    @RequestMapping("getAttendance")
    @ResponseBody
    public List<Attendance> getAttendanceByStuId(@RequestParam String studentId,@RequestParam String classId)
    {
        return classService.getAttendanceByStuId(new ObjectId(studentId),new ObjectId(classId));
    }
    @RequestMapping("addAttendance")
    @ResponseBody
    public boolean addAttendance(@RequestParam String studentId,@RequestParam String classId,@RequestParam String date,@RequestParam int time,@RequestParam String remark)
    {
        Attendance attendance=new Attendance();
        attendance.setId(new ObjectId().toString());
        attendance.setClassId(classId);
        attendance.setStudentId(studentId);
        attendance.setDate(date);
        attendance.setTime(time);
        attendance.setRemark(remark);
        return classService.addAttendance(attendance);
    }
    @RequestMapping("updateAttendance")
    @ResponseBody
    public int updateAttendance(@RequestParam String attendanceId,@RequestParam String date,@RequestParam int time,@RequestParam String remark)
    {
        return 0;
    }
    @RequestMapping("deleteAttendance")
    @ResponseBody
    public boolean deleteAttendance(@RequestParam String attendanceId)
    {
        classService.deleteAttendance(new ObjectId(attendanceId));
        return true;
    }

    /**
     * 跳转学生选课统计页面
     * @return
     */
    @RequestMapping("stuinteclasscountpage")
    public String stuInteClassCountPage(@RequestParam String classId,Model model)
    {
        ClassEntry classEntry=classService.getClassEntryById(new ObjectId(classId), Constant.FIELDS);
        String schoolId=classEntry.getSchoolId().toString();
        String gradeId=classEntry.getGradeId().toString();
        model.addAttribute("schoolId", schoolId);
        model.addAttribute("gradeId", gradeId);
        model.addAttribute("classId", classId);
        model.addAttribute("className", classEntry.getName());
        return "myclass/curricula";
    }

    /**
     * 学生选课统计
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param termType  学期
     * @param pageable
     * @return
     */
    @RequestMapping("/studentInterestClassCount")
    @ResponseBody
    public Page<StudentStat> studentInterestClassCount(@RequestParam String schoolId,@RequestParam String gradeId,@RequestParam String classId,
                                                       @RequestParam(required = false, defaultValue = "-1") int termType, Pageable pageable) {
        return classService.studentInterestClassCount(schoolId,gradeId,classId, termType, pageable);
    }

    /*
    * 学生选课统计导出
    *
    * */
    @RequestMapping("exportStuInteClassExcel")
    @ResponseBody
    public void exportStuInteClassExcel(@RequestParam String schoolId,@RequestParam String gradeId,@RequestParam String classId,
                                        @RequestParam(required = false, defaultValue = "-1") int termType, HttpServletResponse response)
    {
        excelService.exportStuInteClassExcel(schoolId, gradeId, classId, termType, response);
    }

    /**
     * 导出拓展课学生名单
     * @param termType
     * @param response
     */
    @RequestMapping("exportInteClassStuExcel")
    @ResponseBody
    public void exportInteClassStuExcel(@RequestParam(required = false, defaultValue = "-1") int termType, HttpServletResponse response)
    {
        excelService.exportInteClassStuExcel(getSessionValue().getSchoolId(), termType, response);
    }

    /**
     * 跳转到学生成绩单页面
     * @return
     */
    @RequestMapping("/studentScore")
    public String studentJudge(@RequestParam String classId,@RequestParam(required = false, defaultValue = "") String stuId,
                               @RequestParam int termType, Model model)
    {
        model.addAttribute("classId", classId);
        model.addAttribute("stuId", stuId);
        model.addAttribute("termType", termType);
        return "interestclass/studentScore";
    }
    /**
     * 根据学生Id和班级Id查找学生成绩单
     * @param classId
     * @return
     */
    @RequestMapping("/getClassTranscript")
    @ResponseBody
    public InterestClassTranscriptDTO getClassTranscript(@RequestParam String classId, @RequestParam(required = false, defaultValue = "") String studentId,
                                                         @RequestParam(required = false, defaultValue = "-1") int termType)
    {
        int role=getSessionValue().getUserRole();
        ObjectId userId=getUserId();
        if(studentId.equals("")){
            if(UserRole.isParent(role))//学生家长
            {
                userId=new ObjectId(userService.findStuInfoByParentId(userId.toString()).getId());
            }
        } else {
            userId = new ObjectId(studentId);
        }

        InterestClassTranscriptEntry entry=classService.getTranscriptByUserIdAndClassId(userId,classId, termType);
        if(entry==null)
            return null;
        int at = 0;
        List<InterestClassLessonScore> list = getLessonScore(classId, studentId);
        for(InterestClassLessonScore lessonScore : list){
            if(lessonScore.getAttendance() == 1){
                at++;
            }
        }
        InterestClassTranscriptDTO dto = new InterestClassTranscriptDTO(entry);
        dto.setAttendance(at + "/" + list.size());
        return dto;
    }

    /**
     * 根据学生Id和班级Id查找课时分数
     * @param classId
     * @return
     */
    @RequestMapping("/getLessonScore")
    @ResponseBody
    public List<InterestClassLessonScore> getLessonScore(@RequestParam String classId, @RequestParam(required = false, defaultValue = "") String studentId)
    {
        ObjectId userId=getUserId();
        int role=getSessionValue().getUserRole();
        if(studentId.equals("")){
            if(UserRole.isParent(role))//学生家长
            {
                userId=new ObjectId(userService.findStuInfoByParentId(userId.toString()).getId());
            }
        } else {
            userId = new ObjectId(studentId);
        }
        List<InterestClassLessonScoreEntry> list=classService.getLessonScoreByUserIdAndClassId(userId,classId);
        List<InterestClassLessonScore> scoreList=new ArrayList<InterestClassLessonScore>();
        for(InterestClassLessonScoreEntry score : list)
        {
            scoreList.add(new InterestClassLessonScore(score));
        }
        return scoreList;
    }

    /**
     * 学生打印成绩单
     * @param classId
     * @param model
     * @return
     */
    @RequestMapping("/stuPrintTranscript")
    public String studentTranscript( @RequestParam String classId, Model model, @RequestParam(required = false, defaultValue = "-1") int termType) {
        String userId = getUserId().toString();
        int role=getSessionValue().getUserRole();
        if(UserRole.isParent(role))//学生家长
        {
            userId=userService.findStuInfoByParentId(userId).getId();
        }
        //先获取总成绩单view对象  再获取每课时成绩
        TranscriptView transcriptView = classService.findTranscriptViewByUserIdAndClassId(userId, classId, termType);
        List<InterestClassLessonScore> lessonScoreList = classService.findLessonScoreByUserIdAndClassId(userId, classId);
        model.addAttribute("lessonScoreList", lessonScoreList);
        model.addAttribute("transcriptView", transcriptView);
        return "interestclass/transcript";
    }

    @RequestMapping("exportFinalExcel")
    public void exportFinalScoresExcel(int termType, HttpServletResponse response){
        classService.exportExcel(getSchoolId(), response, termType);
    }

    @RequestMapping("/classMasterCheckAttendance")
    public String classMasterCheckAttendance(){
        return "interestclass/classMasterCheckAttendance";
    }

    @RequestMapping("/headMasterCheckAttendance")
    public String headMasterCheckAttendance(){
        return "interestclass/headMasterCheckAttendance";
    }

    @RequestMapping("/getInterestClassAttendanceHeadMaster")
    @ResponseBody
    public RespObj getInterestClassAttendanceHeadMaster(int termType, String gradeId, String categoryId, int weekIndex){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        List<Map<String, Object>> list = classService.getAttendanceByWeekIndex(getSchoolId(), termType, gradeId, categoryId, weekIndex);
        respObj.setMessage(list);
        return respObj;
    }

    @RequestMapping("/getInterestClassAttendanceClassMaster")
    @ResponseBody
    public RespObj getInterestClassAttendanceClassMaster(@ObjectIdType ObjectId classId, int termType, String year, int term){
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        try {
            List<Map<String, Object>> list = classService.getInterestClassAttendanceClassMaster(classId, termType, getSchoolId(), year, term);
            respObj.setMessage(list);
        } catch (Exception e){
            e.printStackTrace();
            respObj.setCode("500");
            respObj.setMessage(e.getMessage());
        } finally {
            return respObj;
        }

    }


    @RequestMapping("/findPassedWeeks")
    @ResponseBody
    public RespObj findPassedWeeks(String year, String term) {
        RespObj respObj = new RespObj(Constant.SUCCESS_CODE);
        try {
            int week = termService.getAllWeekByTerm(year, getSchoolId());
            respObj.setMessage(week);
        } catch (Exception e){
            e.printStackTrace();
            respObj.setCode(Constant.FAILD_CODE);
            respObj.setMessage(e.getMessage());
        } finally {
            return respObj;
        }
    }



}
