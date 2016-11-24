package com.fulaan.managecount.service;

import com.db.WebSpiderSchool.WebSpiderDao;
import com.db.microblog.MicroBlogDao;
import com.db.school.ExerciseAnswerDao;
import com.fulaan.base.service.DirService;
import com.fulaan.educationbureau.dto.EducationBureauDTO;
import com.fulaan.educationbureau.service.EducationBureauService;
import com.fulaan.homeschool.service.NoticeService;
import com.fulaan.learningcenter.service.CloudLessonService;
import com.fulaan.learningcenter.service.ExerciseService;
import com.fulaan.learningcenter.service.HomeWorkService;
import com.fulaan.learningcenter.service.LessonService;
import com.fulaan.log.dto.LogDTO;
import com.fulaan.log.service.LogService;
import com.fulaan.managecount.dto.ManageCountDTO;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.controller.GradeView;
import com.fulaan.school.service.SchoolService;
import com.fulaan.school.service.TeacherClassSubjectService;
import com.fulaan.user.service.UserService;
import com.fulaan.video.service.VideoViewRecordService;
import com.mongodb.BasicDBObject;
import com.pojo.WebSpiderSchool.WebSpiderSchool;
import com.pojo.WebSpiderSchool.WebSpiderSchoolDTO;
import com.pojo.educationbureau.EducationBureauEntry;
import com.pojo.exercise.ExerciseAnswerEntry;
import com.pojo.exercise.ExerciseEntry;
import com.pojo.lesson.DirEntry;
import com.pojo.lesson.LessonEntry;
import com.pojo.lesson.LessonType;
import com.pojo.managecount.TeacherCountType;
import com.pojo.microblog.MicroBlogEntry;
import com.pojo.notice.NoticeEntry;
import com.pojo.school.*;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.pojo.utils.DeleteState;
import com.pojo.utils.MongoUtils;
import com.pojo.video.VideoViewRecordEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.exceptions.ResultTooManyException;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by guojing on 2015/4/9.
 */
@Service
public class ManageCountService {
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private ClassService classService;
    @Autowired
    private UserService userService;
    @Autowired
    private LogService logService;
    @Autowired
    private EducationBureauService educationBureauService;

    private MicroBlogDao microBlogDao = new MicroBlogDao();

    private LessonService lessonService =new LessonService();

    private ExerciseService exerciseService =new ExerciseService();

    private HomeWorkService homeWorkService =new HomeWorkService();

    private NoticeService noticeService =new NoticeService();

    private ExerciseAnswerDao exerciseAnswerDao =new ExerciseAnswerDao();

    private VideoViewRecordService videoViewService=new VideoViewRecordService();

    private CloudLessonService cloudLessonService=new CloudLessonService();

    private DirService dirService =new DirService();

    private TeacherClassSubjectService teacherClassLessonService =new TeacherClassSubjectService();

    private WebSpiderDao webSpiderDao=new WebSpiderDao();

    /**
     * 查询教育局管理学校信息
     * @param userId
     */
    public List<SchoolDTO> getEducationSchoolByUserId(ObjectId userId) {
        //查询教育局管理信息
        EducationBureauEntry educationBureauEntry = educationBureauService.selEducationByUserId(userId.toString());
        List<SchoolDTO> list=new ArrayList<SchoolDTO>();
        if(educationBureauEntry !=null){
            //获取教育管辖的学校id集合
            List<ObjectId> schoolIds= educationBureauEntry.getSchoolIds();
            if(null!=schoolIds && !schoolIds.isEmpty()){
                //查询出教育局管辖下的所有学校信息
                list=schoolService.findSchoolInfoBySchoolIds(schoolIds);
            }
        }
        return list;
    }

    /**
     * 查询教育局信息
     * @param userId
     */
    public EducationBureauDTO getEducationByUserId(ObjectId userId) {
        //查询教育局管理信息
        EducationBureauEntry educationBureauEntry = educationBureauService.selEducationByUserId(userId.toString());
        EducationBureauDTO dto=null;
        if(educationBureauEntry !=null){
            dto=new EducationBureauDTO(educationBureauEntry);
        }
        return dto;
    }

    /**
     * 教育局查询平台使用统计所需数据
     * @param userId
     * @param gradeType
     * @param schoolId
     * @param dateStart
     * @param dateEnd
     * @return
     * @throws ResultTooManyException
     */
    public Map<String, Object> eduSchoolsTotalData(String userId, int gradeType, String schoolId, String dateStart, String dateEnd) throws ResultTooManyException{
        List<ObjectId> schoolIds=getSchoolIdsMap(userId,schoolId);
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(schoolIds,gradeType);
        Map<String,Object> resultMap=getSchoolTotalData(uisMap, dateStart, dateEnd);
        getUserLogData(schoolIds, gradeType, dateStart, dateEnd,resultMap);
        return resultMap;
    }

    /**
     * 获取用户访问日志信息
     * @param schoolIds
     * @param gradeType
     * @param dateStart
     * @param dateEnd
     * @param resultMap
     */
    private void getUserLogData(List<ObjectId> schoolIds, int gradeType, String dateStart, String dateEnd, Map<String,Object> resultMap){
        int actionType=1;//当actionType=1表示用户访问日志
        DateTimeUtils time=new DateTimeUtils();
        long dsl=time.getStrToLongTime(dateStart);
        long del=time.getStrToLongTime(dateEnd);
        //查询用户的访问统计
        List<ManageCountDTO> list=new ArrayList<ManageCountDTO>();
        //查询用户访问日志
        List<LogDTO> logs=logService.getLogEntryByParamList(schoolIds, gradeType, 0, actionType, dsl, del);
        List<String> dateAreas=getUseTimeArea(dateStart,dateEnd);
        //把每日的用户访问量，放到对应的日期下
        Map<String,Integer> logsMap = new HashMap<String,Integer>();
        for(LogDTO item:logs){
            String actionTime=time.getDateToStrTime(item.getActionTime());
            Integer logCount=logsMap.get(actionTime);
            if(logCount==null){
                logsMap.put(actionTime,1);
            }else {
                logsMap.put(actionTime,logCount+1);
            }
        }
        //把每日的用户访问量，放到对应的日期下
        for(String date: dateAreas){
            ManageCountDTO mcDto=new ManageCountDTO();
            mcDto.setCreateTime(date);
            int newCount=0;
            Integer logCount=logsMap.get(date);
            if(logCount!=null){
                newCount=logCount;
            }
            mcDto.setNewCountTotal(newCount);
            list.add(mcDto);
        }
        resultMap.put("acList",list);

        //分别查询老师、学生、家长访问的数量，返回Map集合
        //获取学生用户的访问量
        int scount = logService.getLogEntryByParamCount(schoolIds, gradeType, UserRole.STUDENT.getRole(), actionType, dsl, del);

        //获取老师用户的访问量
        int tcount = logService.getLogEntryByParamCount(schoolIds, gradeType, UserRole.TEACHER.getRole(), actionType, dsl, del);

        //获取学生家长用户的访问量
        int pcount = logService.getLogEntryByParamCount(schoolIds, gradeType, UserRole.PARENT.getRole(), actionType, dsl, del);

        resultMap.put("tcount", tcount);
        resultMap.put("scount", scount);
        resultMap.put("pcount", pcount);

    }

    /**
     * 教育局用户查询各学校访问统计
     * @param userId
     * @param gradeType
     * @param schoolId
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public List<ManageCountDTO> eduAccessAnalysis(String userId, int gradeType, String schoolId, String dateStart, String dateEnd) throws ResultTooManyException {
        List<ObjectId> schoolIds=getSchoolIdsMap(userId,schoolId);
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(schoolIds,gradeType);
        int actionType=1;//当actionType=1表示用户访问日志
        List<ManageCountDTO> acList =null;
        //查询用户访问
        acList=selAndHandleLogEntry(uisMap.get("allIds"), actionType, dateStart, dateEnd);
        return acList;
    }

    /**
     * 根据年级类型查询统计对象id
     * @param schoolIds
     * @param gradeType
     * @return
     */
    private Map<String,List<ObjectId>> getRoleUserIdByGradeParam(List<ObjectId> schoolIds, int gradeType) {
        List<Grade> gradelist=schoolService.findSchoolInfoByParams(schoolIds,gradeType);
        List<ObjectId> gradeIds=new ArrayList<ObjectId>();
        for(Grade grade:gradelist){
            gradeIds.add(grade.getGradeId());
        }
        //根据年级id查询年级下的班级信息
        List<ClassInfoDTO> clsLis=classService.findClassByGradeIds(gradeIds);
        Map<String,List<ObjectId>> uisMap=new HashMap<String, List<ObjectId>>();
        List<ObjectId> stuIds=new ArrayList<ObjectId>();
        List<ObjectId> teaIds=new ArrayList<ObjectId>();
        if(clsLis!=null&&clsLis.size()>0) {
            //从班级信息下获取学生id和教师id
            for (ClassInfoDTO classInfo : clsLis) {
                if (classInfo.getStudentIds() != null) {
                    stuIds.addAll(classInfo.getStudentIds());
                }
                if (classInfo.getTeacherIds() != null) {
                    teaIds.addAll(classInfo.getTeacherIds());
                }
            }
            //去除教师id集合中重复的id
            teaIds = notRepeaMethod(teaIds);
        }
        //根据学生id集合取得学生家长id集合
        List<ObjectId> parIds=getParentIdByStuId(stuIds);
        List<ObjectId> allIds=new ArrayList<ObjectId>();
        allIds.addAll(teaIds);
        allIds.addAll(stuIds);
        allIds.addAll(parIds);
        uisMap.put("teaIds",teaIds);
        uisMap.put("stuIds",stuIds);
        uisMap.put("parIds",parIds);
        uisMap.put("allIds",allIds);
        return uisMap;
    }
    /**
     * 查询统计对象id
     * @param userId
     * @param schoolId
     * @return
     */
    private List<ObjectId> getSchoolIdsMap(String userId, String schoolId) {
        List<ObjectId> schoolIds=new ArrayList<ObjectId>();
        if("".equals(schoolId)){
            //查询教育局管理信息
            EducationBureauEntry educationBureauEntry = educationBureauService.selEducationByUserId(userId);
            if(educationBureauEntry !=null){
                //获取教育管辖的学校id集合
                schoolIds= educationBureauEntry.getSchoolIds();
            }
        }else{
            schoolIds.add(new ObjectId(schoolId));
        }
        return schoolIds;
    }
    /**
     * 查询统计对象id
     * @param schoolIds
     * @param gradeType
     * @return
     */
    private Map<String, List<ObjectId>> getUserIdsMap(List<ObjectId> schoolIds, int gradeType) {
        Map<String,List<ObjectId>> uisMap=null;
        if(gradeType!=0){//判断是否是查询年级下的用户日志统计
            //查询年级中的老师、学生、家长的userId
            uisMap=getRoleUserIdByGradeParam(schoolIds, gradeType);
        }else if(schoolIds!=null&&schoolIds.size()>0){//判断是否是查询学校下的用户日志统计
            //查询学校中的老师、学生、家长的userId
            uisMap=getRoleUserIdBySchoolIds(schoolIds);
        }
        return uisMap;
    }

    /**
     * 根据学校id查询班级中的老师、学生、家长的userId
     * @param schoolIds
     * @return
     */
    public Map<String,List<ObjectId>> getRoleUserIdBySchoolIds(List<ObjectId> schoolIds){
        Map<String,List<ObjectId>> uisMap=new HashMap<String, List<ObjectId>>();
        List<ObjectId> stuIds=new ArrayList<ObjectId>();
        List<ObjectId> parIds=new ArrayList<ObjectId>();
        List<ObjectId> teaIds=new ArrayList<ObjectId>();
        List<ObjectId> allIds=new ArrayList<ObjectId>();
        //根据学校id查询学校中的老师用户信息
        List<UserEntry> userList=userService.findUserRoleInfoBySchoolIds(schoolIds);


        for(UserEntry userInfo:userList){
            if(UserRole.isTeacher(userInfo.getRole())||UserRole.isHeadmaster( userInfo.getRole())
                    ||UserRole.isLeaderClass(userInfo.getRole())||UserRole.isLeaderOfGrade(userInfo.getRole())
                    ||UserRole.isLeaderOfSubject(userInfo.getRole())){
                teaIds.add(userInfo.getID());
            }else if(UserRole.isStudent(userInfo.getRole())){
                stuIds.add(userInfo.getID());
            }else if(UserRole.isParent(userInfo.getRole())) {
                parIds.add(userInfo.getID());
            }
            allIds.add(userInfo.getID());
        }
        uisMap.put("teaIds",teaIds);
        uisMap.put("stuIds",stuIds);
        uisMap.put("parIds",parIds);
        uisMap.put("allIds",allIds);
        return uisMap;
    }

    /**
     * 教育局用户查询各学校访问人员统计
     * @param userId
     * @param gradeType
     * @param schoolId
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public Map<String, Object> eduPeopleTotal(String userId, int gradeType, String schoolId, String dateStart, String dateEnd) throws ResultTooManyException {
        DateTimeUtils time=new DateTimeUtils();
        long dsl=time.getStrToLongTime(dateStart);
        long del=time.getStrToLongTime(dateEnd);
        //通过classId,gradeId,schoolid查询用户id信息

        List<ObjectId> schoolIds=new ArrayList<ObjectId>();
        if("".equals(schoolId)){
            //查询教育局管理信息
            EducationBureauEntry educationBureauEntry = educationBureauService.selEducationByUserId(userId.toString());
            if(educationBureauEntry !=null){
                //获取教育管辖的学校id集合
                schoolIds= educationBureauEntry.getSchoolIds();
            }
        }else{
            schoolIds.add(new ObjectId(schoolId));
        }

        Map<String,List<ObjectId>> uisMap=getUserIdsMap(schoolIds, gradeType);
        int actionType=1;//当actionType=1表示用户访问日志
        //获取学生用户的访问量
        int scount = logService.getLogEntryByParamCount(uisMap.get("stuIds"), actionType, dsl, del);
        //获取老师用户的访问量
        int tcount = logService.getLogEntryByParamCount(uisMap.get("teaIds"), actionType, dsl, del);
        //获取学生家长用户的访问量
        int pcount = logService.getLogEntryByParamCount(uisMap.get("parIds"), actionType, dsl, del);

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("tcount", tcount);
        map.put("scount", scount);
        map.put("pcount", pcount);
        return map;
    }

    /**
     * 备课统计
     * @param userId
     * @param gradeType
     * @param schoolId
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public Map<String, Object> eduLessonCount(String userId, int gradeType, String schoolId, String dateStart, String dateEnd) {
        DateTimeUtils time=new DateTimeUtils();
        long dsl=time.getStrToLongTime(dateStart);
        long del=time.getStrToLongTime(dateEnd);
        ObjectId dslId=new ObjectId(DateTimeUtils.stringToDate(dateStart,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        ObjectId delId=new ObjectId(DateTimeUtils.stringToDate(dateEnd,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        //通过classId,gradeId,schoolid查询用户id信息

        List<ObjectId> schoolIds=getSchoolIdsMap(userId,schoolId);
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(schoolIds,gradeType);

        Map<String,Object> map = new HashMap<String,Object>();
        //查询学生云课程观看数
        int stuCloudCount = showCloudLessonCount(dslId, delId, uisMap);
        //查询学生班级课程观看数
        int stuClassCount = showClassLessonCount(dslId, delId, uisMap);
        //备课空间
        int teaLessonCount =selLessonCount(LessonType.BACKUP_LESSON, dsl, del, uisMap);
        //班级课程访问数
        int teaClassCount = selLessonCount(LessonType.CLASS_LESSON, dsl, del, uisMap);
        //查询作业访问数
        int homeworkCount = selHomeWorkCount(dslId, delId, uisMap);
        //查询考试访问数
        int paperCount = selPaperCount(dsl, del, uisMap);

        map.put("stuCloudCount", stuCloudCount);
        map.put("stuClassCount", stuClassCount);
        map.put("teaLessonCount", teaLessonCount);
        map.put("teaClassCount", teaClassCount);
        map.put("homeworkCount", homeworkCount);
        map.put("paperCount", paperCount);
        return map;
    }

    /**
     * 查询学校信息
     * @param schoolId
     */
    public SchoolDTO searchSchoolInfo(String schoolId){
        SchoolDTO schoolDTO=schoolService.findSchoolById(schoolId);
        return schoolDTO;
    }

    /**
     * 查询学校的年级信息
     * @param schoolId
     */
    public List<GradeView> searchSchoolGradeInfo(String schoolId){
        //通过学校id查询学校中的年级信息
        List<GradeView> list=schoolService.searchSchoolGradeList(schoolId);
        return list;
    }

    /**
     * 查询老师所带的班级信息
     * @param teacherId
     * @param
     */
    public List<ClassInfoDTO> getTeacherClassesInfo(ObjectId teacherId,ObjectId schoolId) throws IllegalParamException {
        List<ClassInfoDTO> list=classService.getSimpleClassInfoDTOs(teacherId,schoolId);
        return list;
    }

    /**
     * 查询年级的班级信息
     * @param gradeId
     */
    public List<ClassInfoDTO> getGradeClassesInfo(String gradeId){
        //通过年级id查询出班级信息
        List<ClassInfoDTO> list=classService.getGradeClassesInfo(gradeId);
        return list;
    }

    /**
     * 学校查询平台使用统计所需数据
     * @param gradeId
     * @param classId
     * @param schoolId
     * @param dateStart
     * @param dateEnd
     * @return
     * @throws ResultTooManyException
     */
    public Map<String,Object> schoolTotalData(String gradeId, String classId, String schoolId, String dateStart, String dateEnd) throws ResultTooManyException {
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId, gradeId, schoolId);
        Map<String,Object> resultMap=getSchoolTotalData(uisMap, dateStart, dateEnd);

        ObjectId sId=null;
        ObjectId gId=null;
        ObjectId cId=null;
        if(classId!=null&&!"".equals(classId)){ //判断班级id是否为空
            cId=new ObjectId(classId);
        }

        if(gradeId!=null&&!"".equals(gradeId)){//当班级id为空时，判断年级id是否为空
            gId=new ObjectId(gradeId);
        }
        if(schoolId!=null&&!"".equals(schoolId)){//当班级id且年级id为空时，判断学校id是否为空
            sId=new ObjectId(schoolId);
        }

        getUserLogData(sId, gId, cId, dateStart, dateEnd,resultMap);

        return resultMap;
    }

    /**
     * 获取用户访问日志信息
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @param resultMap
     */
    private void getUserLogData(ObjectId schoolId, ObjectId gradeId, ObjectId classId, String dateStart, String dateEnd, Map<String,Object> resultMap){
        int actionType=1;//当actionType=1表示用户访问日志
        DateTimeUtils time=new DateTimeUtils();
        long dsl=time.getStrToLongTime(dateStart);
        long del=time.getStrToLongTime(dateEnd);
        //查询用户的访问统计
        List<ManageCountDTO> list=new ArrayList<ManageCountDTO>();
        //查询用户访问日志
        List<LogDTO> logs=logService.getLogEntryByParamList(schoolId, gradeId, classId, 0, 0, actionType, dsl, del);
        List<String> dateAreas=getUseTimeArea(dateStart,dateEnd);
        //把每日的用户访问量，放到对应的日期下
        Map<String,Integer> logsMap = new HashMap<String,Integer>();
        for(LogDTO item:logs){
            String actionTime=time.getDateToStrTime(item.getActionTime());
            Integer logCount=logsMap.get(actionTime);
            if(logCount==null){
                logsMap.put(actionTime,1);
            }else {
                logsMap.put(actionTime,logCount+1);
            }
        }
        //把每日的用户访问量，放到对应的日期下
        for(String date: dateAreas){
            ManageCountDTO mcDto=new ManageCountDTO();
            mcDto.setCreateTime(date);
            int newCount=0;
            Integer logCount=logsMap.get(date);
            if(logCount!=null){
                newCount=logCount;
            }
            mcDto.setNewCountTotal(newCount);
            list.add(mcDto);
        }
        resultMap.put("acList",list);

        //分别查询老师、学生、家长访问的数量，返回Map集合
        //获取学生用户的访问量
        int scount = logService.getLogEntryByParamCount(schoolId, gradeId, classId, UserRole.STUDENT.getRole(), 0, actionType, dsl, del);

        //获取老师用户的访问量
        int tcount = logService.getLogEntryByParamCount(schoolId, gradeId, classId, UserRole.TEACHER.getRole(), 0, actionType, dsl, del);

        //获取学生家长用户的访问量
        int pcount = logService.getLogEntryByParamCount(schoolId, gradeId, classId, UserRole.PARENT.getRole(), 0, actionType, dsl, del);

        resultMap.put("tcount", tcount);
        resultMap.put("scount", scount);
        resultMap.put("pcount", pcount);

    }



    /**
     * 查询平台使用统计所需数据
     * @param uisMap
     * @param dateStart
     * @param dateEnd
     * @return
     * @throws ResultTooManyException
     */
    public Map<String,Object> getSchoolTotalData(Map<String,List<ObjectId>> uisMap, String dateStart, String dateEnd) throws ResultTooManyException{
        Map<String,Object> resultMap=new HashMap<String, Object>();
        //备课统计、资源统计，返回Map集合
        DateTimeUtils time=new DateTimeUtils();
        long dsl=time.getStrToLongTime(dateStart);
        long del=time.getStrToLongTime(dateEnd);

        ObjectId dslId=new ObjectId(DateTimeUtils.stringToDate(dateStart,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        ObjectId delId=new ObjectId(DateTimeUtils.stringToDate(dateEnd,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        //查询学生云课程观看数
        int stuCloudCount = showCloudLessonCount(dslId, delId, uisMap);

        //查询学生班级课程观看数
        int stuClassCount = showClassLessonCount(dslId, delId, uisMap);

        //备课空间
        int teaLessonCount =selLessonCount(LessonType.BACKUP_LESSON, dsl, del, uisMap);

        //班级课程访问数
        int teaClassCount = selLessonCount(LessonType.CLASS_LESSON, dsl, del, uisMap);

        //查询作业访问数
        int homeworkCount = selHomeWorkCount(dslId, delId, uisMap);

        //查询考试访问数
        int paperCount = selPaperCount(dsl, del, uisMap);

        resultMap.put("stuCloudCount", stuCloudCount);
        resultMap.put("stuClassCount", stuClassCount);
        resultMap.put("teaLessonCount", teaLessonCount);
        resultMap.put("teaClassCount", teaClassCount);
        resultMap.put("homeworkCount", homeworkCount);
        resultMap.put("paperCount", paperCount);
        return resultMap;
    }

    /**
     * 学校查询平台使用统计所需数据
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @return
     * @throws ResultTooManyException
     */
    public Map<String, Object> teacherClassTotalData(String classId, String dateStart, String dateEnd) throws ResultTooManyException {
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId, "", "");
        Map<String,Object> resultMap=getTeacherClassTotalData(uisMap, dateStart, dateEnd);
        ObjectId sId=null;
        ObjectId gId=null;
        ObjectId cId=null;
        if(classId!=null&&!"".equals(classId)){ //判断班级id是否为空
            cId=new ObjectId(classId);
        }
        getTeacherClassUserLogData(sId, gId, cId, dateStart, dateEnd, resultMap);
        return resultMap;
    }

    /**
     * 获取用户访问日志信息
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @param resultMap
     */
    private void getTeacherClassUserLogData(ObjectId schoolId, ObjectId gradeId, ObjectId classId, String dateStart, String dateEnd, Map<String,Object> resultMap){
        int actionType=1;//当actionType=1表示用户访问日志
        DateTimeUtils time=new DateTimeUtils();
        long dsl=time.getStrToLongTime(dateStart);
        long del=time.getStrToLongTime(dateEnd);
        //查询用户的访问统计
        List<ManageCountDTO> list=new ArrayList<ManageCountDTO>();
        //查询用户访问日志
        List<LogDTO> logs=logService.getLogEntryByParamList(schoolId, gradeId, classId, 0, 2, actionType, dsl, del);
        List<String> dateAreas=getUseTimeArea(dateStart,dateEnd);
        //把每日的用户访问量，放到对应的日期下
        Map<String,Integer> logsMap = new HashMap<String,Integer>();
        for(LogDTO item:logs){
            String actionTime=time.getDateToStrTime(item.getActionTime());
            Integer logCount=logsMap.get(actionTime);
            if(logCount==null){
                logsMap.put(actionTime,1);
            }else {
                logsMap.put(actionTime,logCount+1);
            }
        }
        //把每日的用户访问量，放到对应的日期下
        for(String date: dateAreas){
            ManageCountDTO mcDto=new ManageCountDTO();
            mcDto.setCreateTime(date);
            int newCount=0;
            Integer logCount=logsMap.get(date);
            if(logCount!=null){
                newCount=logCount;
            }
            mcDto.setNewCountTotal(newCount);
            list.add(mcDto);
        }
        resultMap.put("acList",list);

        //分别查询老师、学生、家长访问的数量，返回Map集合
        //获取学生用户的访问量
        int scount = logService.getLogEntryByParamCount(schoolId, gradeId, classId, UserRole.STUDENT.getRole(), 0, actionType, dsl, del);

        //获取老师用户的访问量
        //int tcount = logService.getLogEntryByParamCount(schoolId, gradeId, classId, UserRole.TEACHER.getRole(), 0, actionType, dsl, del);

        //获取学生家长用户的访问量
        int pcount = logService.getLogEntryByParamCount(schoolId, gradeId, classId, UserRole.PARENT.getRole(), 0, actionType, dsl, del);

        //resultMap.put("tcount", tcount);
        resultMap.put("scount", scount);
        resultMap.put("pcount", pcount);

    }

    /**
     * 查询平台使用统计所需数据
     * @param uisMap
     * @param dateStart
     * @param dateEnd
     * @return
     * @throws ResultTooManyException
     */
    public Map<String,Object> getTeacherClassTotalData(Map<String,List<ObjectId>> uisMap, String dateStart, String dateEnd) throws ResultTooManyException{
        Map<String,Object> resultMap=new HashMap<String, Object>();
        //备课统计、资源统计，返回Map集合
        DateTimeUtils time=new DateTimeUtils();
        long dsl=time.getStrToLongTime(dateStart);
        long del=time.getStrToLongTime(dateEnd);

        ObjectId dslId=new ObjectId(DateTimeUtils.stringToDate(dateStart,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        ObjectId delId=new ObjectId(DateTimeUtils.stringToDate(dateEnd,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        //查询学生云课程观看数
        int stuCloudCount = showCloudLessonCount(dslId, delId, uisMap);

        //查询学生班级课程观看数
        int stuClassCount = showClassLessonCount(dslId, delId, uisMap);

        //备课空间
        //int teaLessonCount =selLessonCount(LessonType.BACKUP_LESSON, dsl, del, uisMap);

        //班级课程访问数
        //int teaClassCount = selLessonCount(LessonType.CLASS_LESSON, dsl, del, uisMap);

        //查询作业访问数
        //int homeworkCount = selHomeWorkCount(dslId, delId, uisMap);

        //查询考试访问数
        //int paperCount = selPaperCount(dsl, del, uisMap);

        resultMap.put("stuCloudCount", stuCloudCount);
        resultMap.put("stuClassCount", stuClassCount);
        //resultMap.put("teaLessonCount", teaLessonCount);
        //resultMap.put("teaClassCount", teaClassCount);
        //resultMap.put("homeworkCount", homeworkCount);
        //resultMap.put("paperCount", paperCount);
        return resultMap;
    }

    /**
     * 访问统计
     * @param gradeId
     * @param classId
     * @param schoolid
     * @param dateStart
     * @param dateEnd
     */
    public List<ManageCountDTO> accessAnalysis(
            String gradeId,
            String classId,
            String schoolid,
            String dateStart,
            String dateEnd) throws ResultTooManyException {
        List<ManageCountDTO> acList =null;
        int actionType=1;//当actionType=1表示用户访问日志
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId, gradeId, schoolid);
        //查询用户访问
        acList=selAndHandleLogEntry(uisMap.get("allIds"), actionType, dateStart, dateEnd);
        return acList;
    }

    /**
     * 根据查询条件查询用户访问
     * @param usIds
     * @param actionType
     * @param dateStart
     * @param dateEnd
     * @return
     * @throws ResultTooManyException
     */
    public List<ManageCountDTO> selAndHandleLogEntry(
            List<ObjectId> usIds,
            int actionType,
            String dateStart,
            String dateEnd) throws ResultTooManyException {
        List<ManageCountDTO> list=new ArrayList<ManageCountDTO>();
        DateTimeUtils timeUtils=new DateTimeUtils();
        long dsl=timeUtils.getStrToLongTime(dateStart);
        long del=timeUtils.getStrToLongTime(dateEnd);
        //查询用户访问日志
        List<LogDTO> logs=logService.getLogEntryByParamList(usIds, actionType, dsl, del);
        List<String> dateAreas=getUseTimeArea(dateStart,dateEnd);
        //把每日的用户访问量，放到对应的日期下
        Map<String,Integer> logsMap = new HashMap<String,Integer>();
        for(LogDTO item:logs){
            String actionTime=timeUtils.getDateToStrTime(item.getActionTime());
            Integer logCount=logsMap.get(actionTime);
            if(logCount==null){
                logsMap.put(actionTime,1);
            }else {
                logsMap.put(actionTime,logCount+1);
            }
        }
        //把每日的用户访问量，放到对应的日期下
        for(String date: dateAreas){
            ManageCountDTO mcDto=new ManageCountDTO();
            mcDto.setCreateTime(date);
            int newCount=0;
            Integer logCount=logsMap.get(date);
            if(logCount!=null){
                newCount=logCount;
            }
            mcDto.setNewCountTotal(newCount);
            list.add(mcDto);
        }
        return list;
    }

    /**
     * 根据班级id查询班级中的老师、学生、家长的userId
     * @param classId
     * @return
     */
    public Map<String,List<ObjectId>> getRoleUserIdByClassId(String classId){
        Map<String,List<ObjectId>> uisMap=new HashMap<String, List<ObjectId>>();
        //查询班级信息
        ClassInfoDTO classInfo=classService.findClassInfoByClassId(classId);
        //取得班级下的全部学生id
        List<ObjectId> stuIds=classInfo.getStudentIds();
        if(stuIds==null){
            stuIds=new ArrayList<ObjectId>();
        }
        //取得班级下的全部老师id
        List<ObjectId> teaIds=classInfo.getTeacherIds();
        if(teaIds==null){
            teaIds=new ArrayList<ObjectId>();
        }
        //通过学生id集合获取学生家长id集合
        List<ObjectId> parIds=getParentIdByStuId(stuIds);
        List<ObjectId> allIds=new ArrayList<ObjectId>();
        allIds.addAll(teaIds);
        allIds.addAll(stuIds);
        allIds.addAll(parIds);
        uisMap.put("teaIds",teaIds);
        uisMap.put("stuIds",stuIds);
        uisMap.put("parIds",parIds);
        uisMap.put("allIds",allIds);
        return uisMap;
    }

    /**
     * 根据学生id集合取得学生家长id集合
     * @param stuIds
     * @return
     */
    public List<ObjectId> getParentIdByStuId(List<ObjectId> stuIds){
        if(stuIds==null){
            stuIds=new ArrayList<ObjectId>();
        }
        //根据学生id集合查询学生用户信息
        List<UserDetailInfoDTO> stuInfos=userService.findUserInfoByIds(stuIds);
        List<ObjectId> parIds=new ArrayList<ObjectId>();
        //从学生用户信息中取到学生家长id
        for(UserDetailInfoDTO user:stuInfos){
            if(user.getRelationId()!=null&&!"".equals(user.getRelationId())){
                parIds.add(new ObjectId(user.getRelationId()));
            }
        }
        return parIds;
    }

    /**
     * 根据年级id查询班级中的老师、学生、家长的userId
     * @param gradeId
     * @return
     */
    public Map<String,List<ObjectId>> getRoleUserIdByGradeId(String gradeId){
        Map<String,List<ObjectId>> uisMap=new HashMap<String, List<ObjectId>>();
        //根据年级id查询年级下的班级信息
        List<ClassInfoDTO> clsLis=classService.findClassByGradeId(gradeId);
        List<ObjectId> stuIds=new ArrayList<ObjectId>();
        List<ObjectId> teaIds=new ArrayList<ObjectId>();
        //从班级信息下获取学生id和教师id
        if(clsLis!=null&&clsLis.size()>0){
            for(ClassInfoDTO classInfo: clsLis){
                if(classInfo.getStudentIds()!=null){
                    stuIds.addAll(classInfo.getStudentIds());
                }
                if(classInfo.getTeacherIds()!=null){
                    teaIds.addAll(classInfo.getTeacherIds());
                }
            }
        }
        //去除教师id集合中重复的id
        teaIds= notRepeaMethod(teaIds);
        //根据学生id集合取得学生家长id集合
        List<ObjectId> parIds=getParentIdByStuId(stuIds);
        List<ObjectId> allIds=new ArrayList<ObjectId>();
        allIds.addAll(teaIds);
        allIds.addAll(stuIds);
        allIds.addAll(parIds);
        uisMap.put("teaIds",teaIds);
        uisMap.put("stuIds",stuIds);
        uisMap.put("parIds",parIds);
        uisMap.put("allIds",allIds);
        return uisMap;
    }

    /**
     * 去除集合中重复的数据
     * @param array
     * @return
     */
    public List<ObjectId> notRepeaMethod(List<ObjectId> array) {
        List<ObjectId> arr = new ArrayList<ObjectId>();
        Iterator<ObjectId> it = array.iterator();
        while(it.hasNext()){
            ObjectId oId = it.next();
            if(!arr.contains(oId))
                arr.add(oId);
        }
        return arr;
    }

    /**
     * 根据学校id查询班级中的老师、学生、家长的userId
     * @param schoolId
     * @return
     */
    public Map<String,List<ObjectId>> getRoleUserIdBySchoolId(String schoolId){
        Map<String,List<ObjectId>> uisMap=new HashMap<String, List<ObjectId>>();
        List<ObjectId> stuIds=new ArrayList<ObjectId>();
        List<ObjectId> parIds=new ArrayList<ObjectId>();
        List<ObjectId> teaIds=new ArrayList<ObjectId>();

        List<ObjectId> allIds=new ArrayList<ObjectId>();
        //根据学校id查询学校中的老师用户信息
        List<UserEntry> teaList=userService.getTeacherEntryBySchoolId(new ObjectId(schoolId),new BasicDBObject("r",1));
        for(UserEntry userInfo:teaList){
            teaIds.add(userInfo.getID());
            allIds.add(userInfo.getID());
        }
        //根据学校id查询学校中的老师用户信息
        List<UserEntry> stuList=userService.getStudentEntryBySchoolId(new ObjectId(schoolId),new BasicDBObject("r",1).append("cid",1));
        for(UserEntry userInfo:stuList){
            stuIds.add(userInfo.getID());
            allIds.add(userInfo.getID());
            if(userInfo.getConnectIds()!=null&&userInfo.getConnectIds().size()>0) {
                parIds.addAll(userInfo.getConnectIds());
                allIds.addAll(userInfo.getConnectIds());
            }
        }
        uisMap.put("teaIds",teaIds);
        uisMap.put("stuIds",stuIds);
        uisMap.put("parIds",parIds);
        uisMap.put("allIds",allIds);
        return uisMap;
    }

    /**
     * 人员访问统计
     * @param gradeId
     * @param gradeId
     * @param schoolid
     * @return
     */
    public Map<String,Object> peopletotal(
            String gradeId,
            String classId,
            String schoolid,
            String dateStart,
            String dateEnd) throws ResultTooManyException {
        DateTimeUtils time=new DateTimeUtils();
        long dsl=time.getStrToLongTime(dateStart);
        long del=time.getStrToLongTime(dateEnd);
        //通过classId,gradeId,schoolid查询用户id信息
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId, gradeId, schoolid);

        int actionType=1;//当actionType=1表示用户访问日志

        //获取学生用户的访问量
        int scount = logService.getLogEntryByParamCount(uisMap.get("stuIds"), actionType, dsl, del);

        //获取老师用户的访问量
        int tcount = logService.getLogEntryByParamCount(uisMap.get("teaIds"), actionType, dsl, del);

        //获取学生家长用户的访问量
        int pcount = logService.getLogEntryByParamCount(uisMap.get("parIds"), actionType, dsl, del);


        Map<String,Object> map = new HashMap<String,Object>();
        map.put("tcount", tcount);
        map.put("scount", scount);
        map.put("pcount", pcount);
        return map;
    }

    /**
     * 統計
     * @param gradeId
     * @param classId
     * @param schoolid
     * @return
     */
    public Map<String,Object> lessonCount(
            String gradeId,
            String classId,
            String schoolid,
            String dateStart,
            String dateEnd) {
        DateTimeUtils time=new DateTimeUtils();

        long dsl=time.getStrToLongTime(dateStart);
        long del=time.getStrToLongTime(dateEnd);

        ObjectId dslId=new ObjectId(DateTimeUtils.stringToDate(dateStart,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        ObjectId delId=new ObjectId(DateTimeUtils.stringToDate(dateEnd,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        //通过classId,gradeId,schoolid查询用户id信息
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId,gradeId,schoolid);
        Map<String,Object> map = new HashMap<String,Object>();
        //查询学生云课程观看数
        int stuCloudCount = showCloudLessonCount(dslId, delId, uisMap);

        //查询学生班级课程观看数
        int stuClassCount = showClassLessonCount(dslId, delId, uisMap);

        //备课空间
        int teaLessonCount =selLessonCount(LessonType.BACKUP_LESSON, dsl, del, uisMap);

        //班级课程访问数
        int teaClassCount = selLessonCount(LessonType.CLASS_LESSON, dsl, del, uisMap);

        //查询作业访问数
        int homeworkCount = selHomeWorkCount(dslId, delId, uisMap);

        //查询考试访问数
        int paperCount = selPaperCount(dsl, del, uisMap);

        map.put("stuCloudCount", stuCloudCount);
        map.put("stuClassCount", stuClassCount);
        map.put("teaLessonCount", teaLessonCount);
        map.put("teaClassCount", teaClassCount);
        map.put("homeworkCount", homeworkCount);
        map.put("paperCount", paperCount);
        return map;
    }

    /**
     * 查询学生云课程观看数
     * @param dslId
     * @param delId
     * @param uisMap
     * @return
     */
    private int showCloudLessonCount(ObjectId dslId, ObjectId delId, Map<String, List<ObjectId>> uisMap) {
        //查询全部云课程信息
        //取得云课程中的视频id
        Set<ObjectId> videoIds=cloudLessonService.getAllCloudLessonIdList();
        List<ObjectId> stuIds=uisMap.get("stuIds");
        int count=videoViewService.showLessonCount(stuIds,videoIds,dslId,delId);
        return count;//查询学生云课程视频观看数
    }

    /**
     * 查询学生班级课程观看数
     * @param dslId
     * @param delId
     * @param uisMap
     * @return
     */
    private int showClassLessonCount(ObjectId dslId, ObjectId delId, Map<String, List<ObjectId>> uisMap) {
        List<ObjectId> teaIds=uisMap.get("teaIds");

        List<ObjectId> ownerList =new ArrayList<ObjectId>();

        List<TeacherClassSubjectDTO> list = teacherClassLessonService.getTeacherClassSubjectDTOByUidsList(teaIds);
        for (TeacherClassSubjectDTO dto : list) {
            ownerList.add(new ObjectId(dto.getId()));
        }

        List<DirEntry> dirList =dirService.getDirEntryList(ownerList, new BasicDBObject(Constant.ID, 1), LessonType.CLASS_LESSON.getType());
        List<ObjectId> dirids= MongoUtils.getFieldObjectIDs(dirList, Constant.ID);

        //获取用户班级课程信息
        List<LessonEntry> LEs=lessonService.getLessonEntryByParamList(dirids, LessonType.CLASS_LESSON.getType(), 0, 0, 0, 0, "");
        Set<ObjectId> videoIds=new HashSet<ObjectId>();
        for(LessonEntry item:LEs){
            if(item.getVideoCount()>0){
                for(ObjectId oId:item.getVideoIds()){
                    videoIds.add(oId);
                }
            }
        }
        List<ObjectId> stuIds=uisMap.get("stuIds");
        return videoViewService.showLessonCount(stuIds,videoIds,dslId,delId);//查询学生班级课程视频观看数
    }

    /**
     * 查询作业访问数
     * @param dslId
     * @param delId
     * @param uisMap
     * @return
     */
    private int selHomeWorkCount(ObjectId dslId, ObjectId delId, Map<String, List<ObjectId>> uisMap) {
        List<ObjectId> usIds=uisMap.get("teaIds");
        return homeWorkService.selHomeWorkCount(usIds, dslId, delId);
    }

    /**
     * 查询考试访问数
     * @param dsl
     * @param del
     * @param uisMap
     * @return
     */
    private int selPaperCount(long dsl, long del, Map<String, List<ObjectId>> uisMap) {
        List<ObjectId> usIds=uisMap.get("teaIds");
        int type=1;
        return exerciseService.selPaperCount(usIds, type, dsl, del);
    }

    /**
     * 备课空间、班级课程访问数
     * @param lessonType
     * @param dsl
     * @param del
     * @param uisMap
     * @return
     */
    private int selLessonCount(LessonType lessonType, long dsl, long del, Map<String, List<ObjectId>> uisMap) {
        List<ObjectId> usIds=uisMap.get("teaIds");

        List<ObjectId> ownerList =new ArrayList<ObjectId>();
        if(lessonType==LessonType.BACKUP_LESSON){
            ownerList.addAll(usIds);
        }
        if(lessonType==LessonType.CLASS_LESSON){
            List<TeacherClassSubjectDTO> list = teacherClassLessonService.getTeacherClassSubjectDTOByUidsList(usIds);
            for (TeacherClassSubjectDTO dto : list) {
                ownerList.add(new ObjectId(dto.getId()));
            }
        }
        List<DirEntry> dirList =dirService.getDirEntryList(ownerList, new BasicDBObject(Constant.ID, 1), lessonType.getType());
        List<ObjectId> dirids= MongoUtils.getFieldObjectIDs(dirList, Constant.ID);
        return lessonService.selLessonCount(dirids, lessonType.getType(), dsl, del);
    }

    /**
     * 校园排行榜-微校园、微家园发帖数
     * @param role
     * @param size
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @return
     * @throws ResultTooManyException
     */
    public Map getMicroCampusOrHomePostNum(
            Integer role,
            Integer size,
            String schoolId,
            String gradeId,
            String classId,
            String dateStart,
            String dateEnd
    ) throws ResultTooManyException {
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId, gradeId, schoolId);
        int blogtype=-1;
        List<ObjectId> usIds=null;
        if(role== UserRole.TEACHER.getRole()){//当统计对象角色是老师时
            blogtype=1;//blogtype=1,查询微校园发帖数
            usIds=uisMap.get("teaIds");//获取老师id集合
        }else if(role==UserRole.STUDENT.getRole()){//当统计对象角色是学生时
            blogtype=2;//blogtype=2,查询微家园发帖数
            usIds=uisMap.get("stuIds");//获取学生id集合
        }else if(role==UserRole.PARENT.getRole()){//当统计对象角色是学生家长时
            blogtype=2;//blogtype=2,查询微家园发帖数
            usIds=uisMap.get("parIds");//获取学生家长id集合
        }
        Map resultMap=new HashMap();
        List<ManageCountDTO> list =handleMicroCampusOrHomePostNum(usIds, DeleteState.NORMAL, blogtype, dateStart, dateEnd, 0, 0, size,"ui");
        resultMap.put("list",list);
        return resultMap;
    }

    /**
     * 查询统计对象id
     * @param classId
     * @param gradeId
     * @param schoolId
     * @return
     */
    public Map<String, List<ObjectId>> getUserIdsMap(String classId, String gradeId, String schoolId) {
        Map<String,List<ObjectId>> uisMap=null;
        //判断班级id是否为空
        if(classId!=null&&!"".equals(classId)){
            //不为空时，查询班级用户id
            uisMap=getRoleUserIdByClassId(classId);
        }else if(gradeId!=null&&!"".equals(gradeId)){//当班级id为空时，判断年级id是否为空
            //不为空时，查询年级下的所有班级用户id
            uisMap=getRoleUserIdByGradeId(gradeId);
        }else if(schoolId!=null&&!"".equals(schoolId)){//当班级id且年级id为空时，判断学校id是否为空
            //不为空时，查询学校的所有用户id
            uisMap=getRoleUserIdBySchoolId(schoolId);
        }
        return uisMap;
    }

    /**
     * 处理校园排行榜-微校园、微家园发帖数
     * @param usIds
     * @param state
     * @param blogtype
     * @param dateStart
     * @param dateEnd
     * @param skip
     * @param limit
     * @param size
     * @param orderBy
     * @return
     * @throws ResultTooManyException
     */
    public List<ManageCountDTO> handleMicroCampusOrHomePostNum(
            List<ObjectId> usIds,
            DeleteState state,
            int blogtype,
            String dateStart,
            String dateEnd,
            int skip,
            int limit,
            int size,
            String orderBy
    ) throws ResultTooManyException {
        DateTimeUtils time=new DateTimeUtils();
        long dsl=time.getStrToLongTime(dateStart);
        long del=time.getStrToLongTime(dateEnd);
        //根据查询条件查询发微博信息
        List<MicroBlogEntry> MBs= microBlogDao.getMicroBlogEntryByParamList(usIds, state, blogtype, dsl, del, Constant.FIELDS, skip, limit, orderBy);
        Map<String,Integer> mcMap=new HashMap<String,Integer>();
        Set<ObjectId> set = new HashSet(usIds);
        for(MicroBlogEntry item:MBs) {
            if(set.contains(item.getUserId())) {
                String userId=item.getUserId().toString();
                if(!mcMap.containsKey(userId)) {
                    mcMap.put(userId,0);
                }
                mcMap.put(userId,mcMap.get(userId)+1);
            }
        }
        //将查询出的数据结果处理成管理统计所需类型
        List<ManageCountDTO> list=handleManageCountNum(usIds,mcMap,size);
        return list;
    }

    /**
     * 对map中的数据排序
     * @param map
     * @return
     */
    private ArrayList<Map.Entry<String,Integer>> sortMap(Map map){
        List<Map.Entry<String, Integer>> entries = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> obj1 , Map.Entry<String, Integer> obj2) {
                return obj2.getValue() - obj1.getValue();
            }
        });
        return (ArrayList<Map.Entry<String, Integer>>) entries;
    }

    /**
     * 校园排行榜-班级课程上传、备课上传数
     * @param funId
     * @param size
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public Map getClassOrPrepUploadNum(
            Integer funId,
            Integer size,
            String schoolId,
            String gradeId,
            String classId,
            String dateStart,
            String dateEnd
    ){
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId,gradeId,schoolId);
        List<ObjectId> usIds=uisMap.get("teaIds");
        int type=0;
        /*if(funId== TeacherCountType.CLASS_UPLOAD_NUM.getState()){//当访问方法是班级课程时
            type=LessonType.CLASS_LESSON.getType();//type=2时，班级课程上传数
        }*/
        if(funId==TeacherCountType.PREPARATION_UPLOAD_NUM.getState()){//当访问方法是备课上传数时
            type=LessonType.BACKUP_LESSON.getType();//type=1时，备课上传数
        }
        Map resultMap=new HashMap();
        List<ManageCountDTO> list =handleClassOrPrepUploadNum(usIds, type, dateStart, dateEnd,size,"");
        resultMap.put("list", list);
        return resultMap;
    }

    /**
     * 处理校园排行榜-班级课程上传、备课上传数
     * @param usIds
     * @param type
     * @param dateStart
     * @param dateEnd
     * @param size
     * @param orderBy
     * @return
     */
    private List<ManageCountDTO> handleClassOrPrepUploadNum(List<ObjectId> usIds, int type, String dateStart, String dateEnd,int size,String orderBy) {
        DateTimeUtils time=new DateTimeUtils();
        long dsl=time.getStrToLongTime(dateStart);
        long del=time.getStrToLongTime(dateEnd);

        List<ObjectId> ownerList =new ArrayList<ObjectId>();
        if(type==LessonType.BACKUP_LESSON.getType()){
            ownerList.addAll(usIds);
        }
        Map<ObjectId,ObjectId> map1=new HashMap<ObjectId, ObjectId>();
        if(type==LessonType.CLASS_LESSON.getType()){
            List<TeacherClassSubjectDTO> list = teacherClassLessonService.getTeacherClassSubjectDTOByUidsList(usIds);
            for (TeacherClassSubjectDTO dto : list) {
                ownerList.add(new ObjectId(dto.getId()));
                map1.put(new ObjectId(dto.getId()), new ObjectId(dto.getTeacherId()));
            }
        }

        List<DirEntry> dirList =dirService.getDirEntryList(ownerList, new BasicDBObject(Constant.ID, Constant.ONE).append("ow", Constant.ONE), type);
        List<ObjectId> dirids=new ArrayList<ObjectId>();
        Map<ObjectId,ObjectId> map2=new HashMap<ObjectId, ObjectId>();
        for(DirEntry dirEntry : dirList){
            dirids.add(dirEntry.getID());
            map2.put(dirEntry.getID(),dirEntry.getOwerId());
        }

        //List<ObjectId> dirids= MongoUtils.getFieldObjectIDs(dirList, Constant.ID);

        //获取用户备课空间、班级课程信息
        List<LessonEntry> LEs=lessonService.getLessonEntryByParamList(dirids, type, dsl, del, 0, 0, orderBy);

        Map<String,Integer> mcMap=new HashMap<String,Integer>();

        for (LessonEntry item : LEs) {
            if (map2.containsKey(item.getDirId())) {
                ObjectId owerId = map2.get(item.getDirId());
                if(type==LessonType.BACKUP_LESSON.getType()){
                    String userId = owerId.toString();
                    if (!mcMap.containsKey(userId)) {
                        mcMap.put(userId, 0);
                    }
                    mcMap.put(userId, mcMap.get(userId) + 1);
                }
                if(type==LessonType.CLASS_LESSON.getType()) {
                    if (map1.containsKey(owerId)) {
                        ObjectId uid = map1.get(owerId);
                        String userId = uid.toString();
                        if (!mcMap.containsKey(userId)) {
                            mcMap.put(userId, 0);
                        }
                        mcMap.put(userId, mcMap.get(userId) + 1);
                    }
                }
            }
        }

        //将查询出的数据结果处理成管理统计所需类型
        List<ManageCountDTO> list=handleManageCountNum(usIds,mcMap,size);
        return list;
    }

    /**
     * 将查询出的数据结果处理成管理统计所需类型
     * @param usIds
     * @param mcMap
     * @param size
     * @return
     */
    private List<ManageCountDTO> handleManageCountNum(List<ObjectId> usIds, Map<String, Integer> mcMap, int size) {
        //根据统计对象id获取统计对象信息
        List<UserDetailInfoDTO> userInfos=userService.findUserInfoByIds(usIds);
        List<ManageCountDTO> list=new ArrayList<ManageCountDTO>();
        ArrayList<Map.Entry<String,Integer>> entries= null;
        if(size==-1){//size==-1时
            //将结果集的长度，赋给size
            size=mcMap.size();
            //对结果集排序，并返回Arraylist
            entries=new ArrayList<Map.Entry<String, Integer>>(mcMap.entrySet());
        }else{//size!=-1时
            //判断size是否大于结果集长度
            if(size>mcMap.size()){//size大于结果集长度时
                //将结果集的长度，赋给size
                size=mcMap.size();
            }
            //对结果集排序
            entries= sortMap(mcMap);
        }
        Map<String,UserDetailInfoDTO> map=new HashMap<String, UserDetailInfoDTO>();
        for(UserDetailInfoDTO item:userInfos){
            map.put(item.getId(),item);
        }
        //在结果集中取出数量是size条数据
        for( int i=0;i<size;i++){
            UserDetailInfoDTO user=map.get(entries.get(i).getKey());
            ManageCountDTO mcDto=new ManageCountDTO();
            mcDto.setUserId(entries.get(i).getKey());
            mcDto.setName(user.getUserName());
            mcDto.setNewCountTotal(entries.get(i).getValue());
            list.add(mcDto);
        }
        return list;
    }

    /**
     * 校园排行榜-作业上传数
     * @param size
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public Map getHomeworkUploadNum(
            Integer size,
            String schoolId,
            String gradeId,
            String classId,
            String dateStart,
            String dateEnd
    ) {
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId,gradeId,schoolId);
        List<ObjectId> usIds=uisMap.get("teaIds");
        Map resultMap=new HashMap();
        //处理校园排行榜-作业上传数
        List<ManageCountDTO> list =handleHomeworkUploadNum(usIds, dateStart, dateEnd, size, "ti");
        resultMap.put("list",list);
        return resultMap;
    }

    /**
     * 处理校园排行榜-作业上传数
     * @param usIds
     * @param dateStart
     * @param dateEnd
     * @param size
     * @param orderBy
     * @return
     */
    private List<ManageCountDTO> handleHomeworkUploadNum(List<ObjectId> usIds, String dateStart, String dateEnd, Integer size,String orderBy) {
        ObjectId dslId=new ObjectId(DateTimeUtils.stringToDate(dateStart,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        ObjectId delId=new ObjectId(DateTimeUtils.stringToDate(dateEnd,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        //获取统计对象上传的作业信息
        List<HomeWorkEntry> HWs=homeWorkService.getHomeworkUploadByParamList(usIds, dslId, delId, 0, 0, orderBy);
        Map<String,Integer> mcMap=new HashMap<String,Integer>();
        Set<ObjectId> set = new HashSet(usIds);
        for(HomeWorkEntry item:HWs) {
            if(set.contains(item.getTeacherId())) {
                String userId=item.getTeacherId().toString();
                if(!mcMap.containsKey(userId)) {
                    mcMap.put(userId,0);
                }
                mcMap.put(userId,mcMap.get(userId)+1);
            }
        }
        //将查询出的数据结果处理成管理统计所需类型
        List<ManageCountDTO> list=handleManageCountNum(usIds,mcMap,size);
        return list;
    }

    /**
     * 校园排行榜-试卷上传数
     * @param size
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public Map getPapersUploadNum(
            Integer size,
            String schoolId,
            String gradeId,
            String classId,
            String dateStart,
            String dateEnd
    ){
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId,gradeId,schoolId);
        List<ObjectId> usIds=uisMap.get("teaIds");
        Map resultMap=new HashMap();
        int type=1;
        //处理校园排行榜-试卷上传数
        List<ManageCountDTO> list =handlePapersUploadNum(usIds, type, dateStart, dateEnd, size, "ti");
        resultMap.put("list",list);
        return resultMap;
    }

    /**
     * 处理校园排行榜-试卷上传数
     * @param usIds
     * @param type
     * @param dateStart
     * @param dateEnd
     * @param size
     * @param orderBy
     * @return
     */
    private List<ManageCountDTO> handlePapersUploadNum(List<ObjectId> usIds, int type, String dateStart, String dateEnd, Integer size, String orderBy) {
        DateTimeUtils time=new DateTimeUtils();
        long dsl=time.getStrToLongTime(dateStart);
        long del=time.getStrToLongTime(dateEnd);
        //获取统计对象上传的试卷信息
        List<ExerciseEntry> EEs=exerciseService.getPapersUploadByParamList(usIds, type, dsl, del, 0, 0, orderBy);
        Map<String,Integer> mcMap=new HashMap<String,Integer>();
        Set<ObjectId> set = new HashSet(usIds);
        for(ExerciseEntry item:EEs) {
            if(set.contains(item.getTeacherId())) {
                String userId=item.getTeacherId().toString();
                if(!mcMap.containsKey(userId)) {
                    mcMap.put(userId,0);
                }
                mcMap.put(userId,mcMap.get(userId)+1);
            }
        }
        //将查询出的数据结果处理成管理统计所需类型
        List<ManageCountDTO> list=handleManageCountNum(usIds,mcMap,size);
        return list;
    }

    /**
     * 校园排行榜-通知发布数
     * @param size
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public Map getNoticePublishNum(
            Integer size,
            String schoolId,
            String gradeId,
            String classId,
            String dateStart,
            String dateEnd) {
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId,gradeId,schoolId);
        List<ObjectId> usIds=uisMap.get("teaIds");
        Map resultMap=new HashMap();
        //处理校园排行榜-通知发布数
        List<ManageCountDTO> list =handleNoticePublishNum(usIds, dateStart, dateEnd, size, "ti");
        resultMap.put("list",list);
        return resultMap;
    }

    /**
     * 处理校园排行榜-通知发布数
     * @param usIds
     * @param dateStart
     * @param dateEnd
     * @param size
     * @param orderBy
     * @return
     */
    private List<ManageCountDTO> handleNoticePublishNum(
            List<ObjectId> usIds,
            String dateStart,
            String dateEnd,
            Integer size,
            String orderBy
    ) {
        ObjectId dslId=new ObjectId(DateTimeUtils.stringToDate(dateStart,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        ObjectId delId=new ObjectId(DateTimeUtils.stringToDate(dateEnd,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        //获取统计对象通知发布信息
        List<NoticeEntry> NEs=noticeService.getNoticePublishByParamList(usIds, dslId, delId, 0, 0, orderBy);
        Map<String,Integer> mcMap=new HashMap<String,Integer>();
        Set<ObjectId> set = new HashSet(usIds);
        for(NoticeEntry item:NEs) {
            if(set.contains(item.getTeacherId())) {
                String userId=item.getTeacherId().toString();
                if(!mcMap.containsKey(userId)) {
                    mcMap.put(userId,0);
                }
                mcMap.put(userId,mcMap.get(userId)+1);
            }
        }
        //将查询出的数据结果处理成管理统计所需类型
        List<ManageCountDTO> list=handleManageCountNum(usIds,mcMap,size);
        return list;
    }

    /**
     * 校园排行榜-班级课程观看数
     * @param size
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public Map getClassesWatchNum(
            Integer size,
            String schoolId,
            String gradeId,
            String classId,
            String dateStart,
            String dateEnd) {
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId,gradeId,schoolId);
        Map resultMap=new HashMap();
        //处理校园排行榜-班级课程观看数
        List<ManageCountDTO> list = handleClassesWatchNum(uisMap, dateStart, dateEnd, size, "");
        resultMap.put("list",list);
        return resultMap;
    }

    /**
     * 处理校园排行榜-班级课程观看数
     * @param uisMap
     * @param dateStart
     * @param dateEnd
     * @param size
     * @param orderBy
     * @return
     */
    private List<ManageCountDTO> handleClassesWatchNum(Map<String, List<ObjectId>> uisMap, String dateStart, String dateEnd, Integer size, String orderBy) {
        List<ObjectId> teaIds = uisMap.get("teaIds");
        List<ObjectId> ownerList =new ArrayList<ObjectId>();

        List<TeacherClassSubjectDTO> list1 = teacherClassLessonService.getTeacherClassSubjectDTOByUidsList(teaIds);
        for (TeacherClassSubjectDTO dto : list1) {
            ownerList.add(new ObjectId(dto.getId()));
        }

        List<DirEntry> dirList =dirService.getDirEntryList(ownerList, new BasicDBObject(Constant.ID, 1), LessonType.CLASS_LESSON.getType());
        List<ObjectId> dirids= MongoUtils.getFieldObjectIDs(dirList, Constant.ID);

        //获取用户班级课程信息
        List<LessonEntry> LEs=lessonService.getLessonEntryByParamList(dirids, LessonType.CLASS_LESSON.getType(), 0, 0, 0, 0, "");

        List<ObjectId> videoIds=new ArrayList<ObjectId>();
        for(LessonEntry item:LEs){
            if(item.getVideoCount()>0){
                for(ObjectId oId:item.getVideoIds()){
                    videoIds.add(oId);
                }
            }
        }
        ObjectId dslId=new ObjectId(DateTimeUtils.stringToDate(dateStart,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        ObjectId delId=new ObjectId(DateTimeUtils.stringToDate(dateEnd,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        List<ObjectId> stuIds=uisMap.get("stuIds");
        //获取观看的视频信息
        List<VideoViewRecordEntry> VVREs=videoViewService.getVideoViewRecordByParamList(stuIds, dslId, delId, orderBy);
        Map<String,Integer> mcMap=new HashMap<String,Integer>();
        Set<ObjectId> set = new HashSet(stuIds);
        Set<ObjectId> vdSet = new HashSet(videoIds);
        for(VideoViewRecordEntry item:VVREs) {
            if(set.contains(item.getUserInfo().getId())&&vdSet.contains(item.getVideoInfo().getId())) {
                String userId=item.getUserInfo().getId().toString();
                if(!mcMap.containsKey(userId)) {
                    mcMap.put(userId,0);
                }
                mcMap.put(userId,mcMap.get(userId)+1);
            }
        }
        //将查询出的数据结果处理成管理统计所需类型
        List<ManageCountDTO> list=handleManageCountNum(stuIds,mcMap,size);
        return list;
    }

    /**
     * 校园排行榜-作业完成数
     * @param size
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public Map getJobCompletionNum(
            Integer size,
            String schoolId,
            String gradeId,
            String classId,
            String dateStart,
            String dateEnd
    ){
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId, gradeId, schoolId);
        List<ObjectId> usIds=uisMap.get("stuIds");
        Map resultMap=new HashMap();
        //处理校园排行榜-作业完成数
        List<ManageCountDTO> list = handleJobCompletionNum(usIds, dateStart, dateEnd, size, "sul.ti");
        resultMap.put("list",list);
        return resultMap;
    }

    /**
     * 处理校园排行榜-作业完成数
     * @param usIds
     * @param dateStart
     * @param dateEnd
     * @param size
     * @param orderBy
     * @return
     */
    private List<ManageCountDTO> handleJobCompletionNum(List<ObjectId> usIds, String dateStart, String dateEnd, Integer size, String orderBy) {
        DateTimeUtils time=new DateTimeUtils();

        long dsl=time.getStrToLongTime(dateStart);
        long del=time.getStrToLongTime(dateEnd);
        //获取统计对象作业完成信息
        List<HomeWorkEntry> HWs=homeWorkService.getJobCompletionByParamList(usIds, dsl, del, orderBy);
        Map<String,Integer> mcMap=new HashMap<String,Integer>();
        Set<ObjectId> set = new HashSet(usIds);
        for(HomeWorkEntry item:HWs) {
            for(StudentSubmitHomeWork shw:item.getSubmitList()) {
                String studentId=shw.getStudentId()==null?"":shw.getStudentId().toString();
                if(set.contains(shw.getStudentId())) {
                    if(!mcMap.containsKey(studentId)) {
                        mcMap.put(studentId,0);
                    }
                    mcMap.put(studentId,mcMap.get(studentId)+1);
                }
            }
        }
        //将查询出的数据结果处理成管理统计所需类型
        List<ManageCountDTO> list=handleManageCountNum(usIds,mcMap,size);
        return list;
    }

    /**
     * 校园排行榜-试卷完成数
     * @param size
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public Map getPapersCompletionNum(
        Integer size,
        String schoolId,
        String gradeId,
        String classId,
        String dateStart,
        String dateEnd
    ){
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId,gradeId,schoolId);
        Map resultMap=new HashMap();
        //处理校园排行榜-试卷完成数
        List<ManageCountDTO> list = handlePapersCompletionNum(uisMap, dateStart, dateEnd, size, "si");
        resultMap.put("list",list);
        return resultMap;
    }

    /**
     * 处理校园排行榜-试卷完成数
     * @param uisMap
     * @param dateStart
     * @param dateEnd
     * @param size
     * @param orderBy
     * @return
     */
    private List<ManageCountDTO> handlePapersCompletionNum(Map<String,List<ObjectId>> uisMap, String dateStart, String dateEnd, Integer size, String orderBy) {
        List<ObjectId> teaIds = uisMap.get("teaIds");
        int type=1;
        //获取统计对象上传的试卷信息
        List<ExerciseEntry> EEs=exerciseService.getPapersUploadByParamList(teaIds, type, 0, 0, 0, 0, "");
        List<ObjectId> exIds=new ArrayList<ObjectId>();
        Map<ObjectId, String> exMap=new HashMap<ObjectId, String>();
        for(ExerciseEntry item: EEs){
            exIds.add(item.getID());
            exMap.put(item.getID(),item.getName());
        }
        List<ObjectId> stuIds=uisMap.get("stuIds");
        Map<String,Integer> mcMap=new HashMap<String,Integer>();
        if(exIds.size()>0) {
            ObjectId dslId = new ObjectId(DateTimeUtils.stringToDate(dateStart, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
            ObjectId delId = new ObjectId(DateTimeUtils.stringToDate(dateEnd, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
            //获取统计对象试卷完成信息
            Map<String, ExerciseAnswerEntry> EAEMap = exerciseAnswerDao.getPapersCompletionByParamList(stuIds, exIds, dslId, delId, orderBy);
            Set<ObjectId> set = new HashSet(stuIds);
            for (ExerciseAnswerEntry item : EAEMap.values()) {
                if (set.contains(item.getUserId())) {
                    String userId = item.getUserId().toString();
                    if (!mcMap.containsKey(userId)) {
                        mcMap.put(userId, 0);
                    }
                    mcMap.put(userId, mcMap.get(userId) + 1);
                }
            }
        }
        //将查询出的数据结果处理成管理统计所需类型
        List<ManageCountDTO> list=handleManageCountNum(stuIds,mcMap,size);
        return list;
    }

    /**
     * 校园排行榜-云课程观看数
     * @param size
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public Map getCloudCurriculumViewNum(
            Integer size,
            String schoolId,
            String gradeId,
            String classId,
            String dateStart,
            String dateEnd
    ){
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId, gradeId, schoolId);
        Map resultMap=new HashMap();
        //处理校园排行榜-云课程观看数
        List<ManageCountDTO> list = handleCloudCurriculumViewNum(uisMap, dateStart, dateEnd, size, "");
        resultMap.put("list",list);
        return resultMap;
    }

    /**
     * 处理校园排行榜-云课程观看数
     * @param uisMap
     * @param dateStart
     * @param dateEnd
     * @param size
     * @param orderBy
     * @return
     */
    private List<ManageCountDTO> handleCloudCurriculumViewNum(Map<String, List<ObjectId>> uisMap, String dateStart, String dateEnd, Integer size, String orderBy) {
        //查询全部云课程信息
        Set<ObjectId> videoIds=cloudLessonService.getAllCloudLessonIdList();
        ObjectId dslId=new ObjectId(DateTimeUtils.stringToDate(dateStart,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        ObjectId delId=new ObjectId(DateTimeUtils.stringToDate(dateEnd,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        List<ObjectId> stuIds=uisMap.get("stuIds");
        //获取观看的视频信息
        List<VideoViewRecordEntry> VVREs=videoViewService.getVideoViewRecordByParamList(stuIds, dslId, delId, orderBy);
        Map<String,Integer> mcMap=new HashMap<String,Integer>();
        Set<ObjectId> set = new HashSet(stuIds);
        Set<ObjectId> vdSet = new HashSet(videoIds);
        for(VideoViewRecordEntry item:VVREs) {
            if(set.contains(item.getUserInfo().getId())&&vdSet.contains(item.getVideoInfo().getId())) {
                String userId=item.getUserInfo().getId().toString();
                if(!mcMap.containsKey(userId)) {
                    mcMap.put(userId,0);
                }
                mcMap.put(userId,mcMap.get(userId)+1);
            }
        }
        //将查询出的数据结果处理成管理统计所需类型
        List<ManageCountDTO> list=handleManageCountNum(stuIds,mcMap,size);
        return list;
    }

    /**
     * 使用时间分布图-微校园、微家园发帖数
     * @param role
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @return
     * @throws ResultTooManyException
     */
    public Map getMicroCampusOrHomePostUseTime(
            Integer role,
            String schoolId,
            String gradeId,
            String classId,
            String dateStart,
            String dateEnd
    ) throws ResultTooManyException {
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId,gradeId,schoolId);
        int blogtype=-1;
        List<ObjectId> usIds=null;
        if(role== UserRole.TEACHER.getRole()){//当统计对象角色是老师时
            blogtype=1;//blogtype=1,查询微校园发帖数
            usIds=uisMap.get("teaIds");//获取老师id集合
        }else if(role==UserRole.STUDENT.getRole()){//当统计对象角色是学生时
            blogtype=2;//blogtype=2,查询微家园发帖数
            usIds=uisMap.get("stuIds");//获取学生id集合
        }else if(role==UserRole.PARENT.getRole()){//当统计对象角色是学生家长时
            blogtype=2;//blogtype=2,查询微家园发帖数
            usIds=uisMap.get("parIds");//获取学生家长id集合
        }
        Map resultMap=new HashMap();
        //处理使用时间分布图-微校园、微家园发帖数
        List<ManageCountDTO> list =handleMicroCampusOrHomePostUseTime(usIds, DeleteState.NORMAL, blogtype, dateStart, dateEnd, 0, 0);
        resultMap.put("list",list);
        return resultMap;
    }

    /**
     * 处理使用时间分布图-微校园、微家园发帖数
     * @param usIds
     * @param state
     * @param blogtype
     * @param dateStart
     * @param dateEnd
     * @param skip
     * @param limit
     * @return
     * @throws ResultTooManyException
     */
    public List<ManageCountDTO> handleMicroCampusOrHomePostUseTime(
            List<ObjectId> usIds,
            DeleteState state,
            int blogtype,
            String dateStart,
            String dateEnd,
            int skip,
            int limit
        ) throws ResultTooManyException {
        DateTimeUtils time=new DateTimeUtils();
        long dsl=time.getStrToLongTime(dateStart);
        long del=time.getStrToLongTime(dateEnd);
        //根据查询条件查询发微博信息
        List<MicroBlogEntry> MBs= microBlogDao.getMicroBlogEntryByParamList(usIds, state, blogtype, dsl, del, Constant.FIELDS, skip, limit, "pbt");
        List<ManageCountDTO> list=new ArrayList<ManageCountDTO>();
        List<String> dateAreas=getUseTimeArea(dateStart,dateEnd);
        //把每日的用户访问量，放到对应的日期下
        for(String date: dateAreas){
            ManageCountDTO mcDto=new ManageCountDTO();
            mcDto.setCreateTime(date);
            int newCount=0;
            for(MicroBlogEntry item:MBs){
                Long publishTime=item.getPublishTime();
                String pubTime=time.getLongToStrTime(publishTime);
                if(date.equals(pubTime)){
                    newCount++;
                }
            }
            mcDto.setNewCountTotal(newCount);
            list.add(mcDto);
        }
        return list;
    }


    /**
     * 使用时间分布图-班级课程上传、备课上传数
     * @param funId
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public Map getClassOrPrepUploadUseTime(
            Integer funId,
            String schoolId,
            String gradeId,
            String classId,
            String dateStart,
            String dateEnd
    ){
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId,gradeId,schoolId);
        List<ObjectId> usIds=uisMap.get("teaIds");
        int type=0;
        /*if(funId== TeacherCountType.CLASS_UPLOAD_NUM.getState()){//当访问方法是班级课程时
            type=LessonType.CLASS_LESSON.getType();//type=2时，班级课程上传数
        }*/
        if(funId==TeacherCountType.PREPARATION_UPLOAD_NUM.getState()){//当访问方法是备课上传数时
            type=LessonType.BACKUP_LESSON.getType();//type=1时，备课上传数
        }
        Map resultMap=new HashMap();
        //处理使用时间分布图-班级课程上传、备课上传数
        List<ManageCountDTO> list =handleClassOrPrepUploadUseTime(usIds, type, dateStart, dateEnd);
        resultMap.put("list",list);
        return resultMap;
    }

    /**
     * 处理使用时间分布图-班级课程上传、备课上传数
     * @param usIds
     * @param type
     * @param dateStart
     * @param dateEnd
     * @return
     */
    private List<ManageCountDTO> handleClassOrPrepUploadUseTime(List<ObjectId> usIds, int type, String dateStart, String dateEnd) {
        DateTimeUtils time=new DateTimeUtils();
        long dsl = time.getStrToLongTime(dateStart);
        long del=time.getStrToLongTime(dateEnd);

        List<ObjectId> ownerList =new ArrayList<ObjectId>();
        if(type==LessonType.BACKUP_LESSON.getType()){
            ownerList.addAll(usIds);
        }

        if(type==LessonType.CLASS_LESSON.getType()){
            List<TeacherClassSubjectDTO> list = teacherClassLessonService.getTeacherClassSubjectDTOByUidsList(usIds);
            for (TeacherClassSubjectDTO dto : list) {
                ownerList.add(new ObjectId(dto.getId()));
            }
        }

        List<DirEntry> dirList =dirService.getDirEntryList(ownerList, new BasicDBObject(Constant.ID, Constant.ONE).append("ow", Constant.ONE), type);
        List<ObjectId> dirids=new ArrayList<ObjectId>();
        for(DirEntry dirEntry : dirList){
            dirids.add(dirEntry.getID());
        }

        //获取用户备课空间、班级课程信息
        List<LessonEntry> LEs=lessonService.getLessonEntryByParamList(dirids, type, dsl, del, 0, 0, "lut");
        List<ManageCountDTO> list=new ArrayList<ManageCountDTO>();
        List<String> dateAreas=getUseTimeArea(dateStart,dateEnd);
        //把每日的用户访问量，放到对应的日期下
        for(String date: dateAreas){
            ManageCountDTO mcDto=new ManageCountDTO();
            mcDto.setCreateTime(date);
            int newCount=0;
            for(LessonEntry item:LEs){
                Long publishTime=item.getLastUpdateTime();
                String pubTime=time.getLongToStrTime(publishTime);
                if(date.equals(pubTime)){
                    newCount++;
                }
            }
            mcDto.setNewCountTotal(newCount);
            list.add(mcDto);
        }
        return list;
    }

    /**
     * 使用时间分布图-作业上传数
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public Map getHomeworkUploadUseTime(
            String schoolId,
            String gradeId,
            String classId,
            String dateStart,
            String dateEnd
    ) {
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId,gradeId,schoolId);
        List<ObjectId> usIds=uisMap.get("teaIds");
        Map resultMap=new HashMap();
        //处理使用时间分布图-作业上传数
        List<ManageCountDTO> list =handleHomeworkUploadUseTime(usIds, dateStart, dateEnd);
        resultMap.put("list",list);
        return resultMap;
    }

    /**
     * 处理使用时间分布图-作业上传数
     * @param usIds
     * @param dateStart
     * @param dateEnd
     * @return
     */
    private List<ManageCountDTO> handleHomeworkUploadUseTime(List<ObjectId> usIds, String dateStart, String dateEnd) {
        DateTimeUtils time=new DateTimeUtils();
        ObjectId dslId=new ObjectId(DateTimeUtils.stringToDate(dateStart,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        ObjectId delId=new ObjectId(DateTimeUtils.stringToDate(dateEnd,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        //获取统计对象上传的作业信息
        List<HomeWorkEntry> HWs=homeWorkService.getHomeworkUploadByParamList(usIds, dslId, delId, 0, 0, "lut");
        List<ManageCountDTO> list=new ArrayList<ManageCountDTO>();
        List<String> dateAreas=getUseTimeArea(dateStart,dateEnd);
        //把每日的用户访问量，放到对应的日期下
        for(String date: dateAreas){
            ManageCountDTO mcDto=new ManageCountDTO();
            mcDto.setCreateTime(date);
            int newCount=0;
            for(HomeWorkEntry item:HWs){
                String pubTime=time.getLongToStrTime(item.getID().getTime());
                if(date.equals(pubTime)){
                    newCount++;
                }
            }
            mcDto.setNewCountTotal(newCount);
            list.add(mcDto);
        }
        return list;
    }

    /**
     * 使用时间分布图-通知发布数
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public Map getNoticeUseTime(
            String schoolId,
            String gradeId,
            String classId,
            String dateStart,
            String dateEnd) {
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId,gradeId,schoolId);
        List<ObjectId> usIds=uisMap.get("teaIds");
        Map resultMap=new HashMap();
        //处理使用时间分布图-通知发布数
        List<ManageCountDTO> list =handleNoticeUseTime(usIds, dateStart, dateEnd);
        resultMap.put("list",list);
        return resultMap;
    }

    /**
     * 处理使用时间分布图-通知发布数
     * @param usIds
     * @param dateStart
     * @param dateEnd
     * @return
     */
    private List<ManageCountDTO> handleNoticeUseTime(List<ObjectId> usIds, String dateStart, String dateEnd) {
        ObjectId dslId=new ObjectId(DateTimeUtils.stringToDate(dateStart,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        ObjectId delId=new ObjectId(DateTimeUtils.stringToDate(dateEnd,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        //获取统计对象通知发布信息
        List<NoticeEntry> NEs=noticeService.getNoticePublishByParamList(usIds, dslId, delId, 0, 0, "");
        DateTimeUtils time=new DateTimeUtils();
        List<ManageCountDTO> list=new ArrayList<ManageCountDTO>();
        List<String> dateAreas=getUseTimeArea(dateStart,dateEnd);
        //把每日的用户访问量，放到对应的日期下
        for(String date: dateAreas){
            ManageCountDTO mcDto=new ManageCountDTO();
            mcDto.setCreateTime(date);
            int newCount=0;
            for(NoticeEntry item:NEs){
                Long submitTime=item.getID().getTime();
                String pubTime=time.getLongToStrTime(submitTime);
                if(date.equals(pubTime)){
                    newCount++;
                }
            }
            mcDto.setNewCountTotal(newCount);
            list.add(mcDto);
        }
        return list;
    }

    /**
     * 使用时间分布图-试卷上传数
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public Map getPapersUploadUseTime(
            String schoolId,
            String gradeId,
            String classId,
            String dateStart,
            String dateEnd
    ){
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId,gradeId,schoolId);
        List<ObjectId> usIds=uisMap.get("teaIds");
        Map resultMap=new HashMap();
        int type=1;
        //处理使用时间分布图-试卷上传数
        List<ManageCountDTO> list =handlePapersUploadUseTime(usIds, type, dateStart, dateEnd);
        resultMap.put("list",list);
        return resultMap;
    }

    /**
     * 处理使用时间分布图-试卷上传数
     * @param usIds
     * @param type
     * @param dateStart
     * @param dateEnd
     * @return
     */
    private List<ManageCountDTO> handlePapersUploadUseTime(List<ObjectId> usIds, int type, String dateStart, String dateEnd) {
        DateTimeUtils time=new DateTimeUtils();
        long dsl=time.getStrToLongTime(dateStart);
        long del=time.getStrToLongTime(dateEnd);
        //获取统计对象上传的试卷信息
        List<ExerciseEntry> EEs=exerciseService.getPapersUploadByParamList(usIds, type, dsl, del, 0, 0, "lut");
        List<ManageCountDTO> list=new ArrayList<ManageCountDTO>();
        List<String> dateAreas = getUseTimeArea(dateStart, dateEnd);
        //把每日的用户访问量，放到对应的日期下
        for(String date: dateAreas){
            ManageCountDTO mcDto=new ManageCountDTO();
            mcDto.setCreateTime(date);
            int newCount=0;
            for(ExerciseEntry item:EEs){
                Long updateTime=item.getLastUpdateTime();
                String pubTime=time.getLongToStrTime(updateTime);
                if(date.equals(pubTime)){
                    newCount++;
                }
            }
            mcDto.setNewCountTotal(newCount);
            list.add(mcDto);
        }
        return list;
    }

    /**
     * 使用时间分布图-班级课程观看数
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public Map getClassesWatchUseTime(
            String schoolId,
            String gradeId,
            String classId,
            String dateStart,
            String dateEnd
    ){
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId,gradeId,schoolId);
        Map resultMap=new HashMap();
        //处理使用时间分布图-班级课程观看数
        List<ManageCountDTO> list =handleClassesWatchUseTime(uisMap, dateStart, dateEnd);
        resultMap.put("list",list);
        return resultMap;
    }

    /**
     * 处理使用时间分布图-班级课程观看数
     * @param uisMap
     * @param dateStart
     * @param dateEnd
     * @return
     */
    private List<ManageCountDTO> handleClassesWatchUseTime(Map<String, List<ObjectId>> uisMap, String dateStart, String dateEnd) {
        List<ObjectId> teaIds = uisMap.get("teaIds");
        List<ObjectId> ownerList =new ArrayList<ObjectId>();

        List<TeacherClassSubjectDTO> list1 = teacherClassLessonService.getTeacherClassSubjectDTOByUidsList(teaIds);
        for (TeacherClassSubjectDTO dto : list1) {
            ownerList.add(new ObjectId(dto.getId()));
        }

        List<DirEntry> dirList =dirService.getDirEntryList(ownerList, new BasicDBObject(Constant.ID, 1), LessonType.CLASS_LESSON.getType());
        List<ObjectId> dirids= MongoUtils.getFieldObjectIDs(dirList, Constant.ID);

        //获取用户班级课程信息
        List<LessonEntry> LEs=lessonService.getLessonEntryByParamList(dirids, LessonType.CLASS_LESSON.getType(), 0, 0, 0, 0, "");
        List<ObjectId> videoIds=new ArrayList<ObjectId>();
        for(LessonEntry item:LEs){
            if(item.getVideoCount()>0){
                for(ObjectId oId:item.getVideoIds()){
                    videoIds.add(oId);
                }
            }
        }
        ObjectId dslId=new ObjectId(DateTimeUtils.stringToDate(dateStart,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        ObjectId delId=new ObjectId(DateTimeUtils.stringToDate(dateEnd,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        List<ObjectId> stuIds=uisMap.get("stuIds");
        //获取观看的视频信息
        List<VideoViewRecordEntry> VVREs=videoViewService.getVideoViewRecordByParamList(stuIds, dslId, delId, "");
        List<ManageCountDTO> list=new ArrayList<ManageCountDTO>();
        Set<ObjectId> vdSet = new HashSet(videoIds);
        List<String> dateAreas=getUseTimeArea(dateStart,dateEnd);
        DateTimeUtils time=new DateTimeUtils();
        //把每日的用户访问量，放到对应的日期下
        for(String date: dateAreas){
            ManageCountDTO mcDto=new ManageCountDTO();
            mcDto.setCreateTime(date);
            int newCount=0;
            for(VideoViewRecordEntry item:VVREs){
                if(vdSet.contains(item.getVideoInfo().getId())){
                    Long updateTime=item.getID().getTime();
                    String pubTime=time.getLongToStrTime(updateTime);
                    if(date.equals(pubTime)){
                        newCount++;
                    }
                }
            }
            mcDto.setNewCountTotal(newCount);
            list.add(mcDto);
        }
        return list;
    }

    /**
     * 使用时间分布图-作业完成数
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public Map getJobCompletionUseTime(
            String schoolId,
            String gradeId,
            String classId,
            String dateStart,
            String dateEnd
    ){
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId, gradeId, schoolId);
        List<ObjectId> usIds=uisMap.get("stuIds");
        Map resultMap=new HashMap();
        //处理使用时间分布图-作业完成数
        List<ManageCountDTO> list =handleJobCompletionUseTime(usIds, dateStart, dateEnd);
        resultMap.put("list",list);
        return resultMap;
    }

    /**
     * 处理使用时间分布图-作业完成数
     * @param usIds
     * @param dateStart
     * @param dateEnd
     * @return
     */
    private List<ManageCountDTO> handleJobCompletionUseTime(List<ObjectId> usIds, String dateStart, String dateEnd) {
        DateTimeUtils time=new DateTimeUtils();
        long dsl=time.getStrToLongTime(dateStart);
        long del=time.getStrToLongTime(dateEnd);
        //获取统计对象作业完成信息
        List<HomeWorkEntry> HWs=homeWorkService.getJobCompletionByParamList(usIds, dsl, del, "sul.ti");
        List<ManageCountDTO> list=new ArrayList<ManageCountDTO>();
        Set<ObjectId> set = new HashSet(usIds);
        List<String> dateAreas=getUseTimeArea(dateStart, dateEnd);
        //把每日的用户访问量，放到对应的日期下
        for(String date: dateAreas){
            ManageCountDTO mcDto=new ManageCountDTO();
            mcDto.setCreateTime(date);
            int newCount=0;
            for(HomeWorkEntry item:HWs){
                for(StudentSubmitHomeWork shw:item.getSubmitList()) {
                    if(set.contains(shw.getStudentId())) {
                        Long updateTime = shw.getTime();
                        String pubTime = time.getLongToStrTime(updateTime);
                        if (date.equals(pubTime)) {
                            newCount++;
                        }
                    }
                }
            }
            mcDto.setNewCountTotal(newCount);
            list.add(mcDto);
        }
        return list;
    }

    /**
     * 使用时间分布图-试卷完成数
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public Map getPapersCompletionUseTime(
            String schoolId,
            String gradeId,
            String classId,
            String dateStart,
            String dateEnd
    ){

        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId, gradeId, schoolId);
        Map resultMap=new HashMap();
        //处理使用时间分布图-试卷完成数
        List<ManageCountDTO> list =handlePapersCompletionUseTime(uisMap, dateStart, dateEnd);
        resultMap.put("list",list);
        return resultMap;
    }

    /**
     * 处理使用时间分布图-试卷完成数
     * @param uisMap
     * @param dateStart
     * @param dateEnd
     * @return
     */
    private List<ManageCountDTO> handlePapersCompletionUseTime(Map<String, List<ObjectId>> uisMap, String dateStart, String dateEnd) {
        List<ObjectId> teaIds=uisMap.get("teaIds");
        int type=1;
        //获取统计对象上传的试卷信息
        List<ExerciseEntry> EEs=exerciseService.getPapersUploadByParamList(teaIds, type, 0, 0, 0, 0, "");
        List<ObjectId> exIds=new ArrayList<ObjectId>();
        Map<ObjectId, String> exMap=new HashMap<ObjectId, String>();
        for(ExerciseEntry item: EEs){
            exIds.add(item.getID());
            exMap.put(item.getID(),item.getName());
        }
        List<ObjectId> stuIds=uisMap.get("stuIds");
        ObjectId dslId=new ObjectId(DateTimeUtils.stringToDate(dateStart,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        ObjectId delId=new ObjectId(DateTimeUtils.stringToDate(dateEnd,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        //获取统计对象试卷完成信息
        Map<String,ExerciseAnswerEntry> EAEMap=exerciseAnswerDao.getPapersCompletionByParamList(stuIds, exIds, dslId, delId, "");
        List<ManageCountDTO> list=new ArrayList<ManageCountDTO>();
        Set<ObjectId> set = new HashSet(stuIds);
        List<String> dateAreas=getUseTimeArea(dateStart,dateEnd);
        DateTimeUtils time=new DateTimeUtils();
        //把每日的用户访问量，放到对应的日期下
        for(String date: dateAreas) {
            ManageCountDTO mcDto = new ManageCountDTO();
            mcDto.setCreateTime(date);
            int newCount = 0;
            for (ExerciseAnswerEntry item : EAEMap.values()) {
                if (set.contains(item.getUserId())) {
                    Long updateTime = item.getID().getTime();
                    String pubTime = time.getLongToStrTime(updateTime);
                    if (date.equals(pubTime)) {
                        newCount++;
                    }
                }
            }
            mcDto.setNewCountTotal(newCount);
            list.add(mcDto);
        }
        return list;
    }

    /**
     * 使用时间分布图-云课程观看数
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @return
     */
    public Map getCloudCurriculumViewUseTime(
            String schoolId,
            String gradeId,
            String classId,
            String dateStart,
            String dateEnd
    ){
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId,gradeId,schoolId);
        Map resultMap=new HashMap();
        //处理使用时间分布图-云课程观看数
        List<ManageCountDTO> list =handleCloudCurriculumViewUseTime(uisMap, dateStart, dateEnd);
        resultMap.put("list",list);
        return resultMap;
    }

    /**
     * 处理使用时间分布图-云课程观看数
     * @param uisMap
     * @param dateStart
     * @param dateEnd
     * @return
     */
    private List<ManageCountDTO> handleCloudCurriculumViewUseTime(Map<String, List<ObjectId>> uisMap, String dateStart, String dateEnd) {
        //查询全部云课程信息
        Set<ObjectId> videoIds=cloudLessonService.getAllCloudLessonIdList();
        ObjectId dslId=new ObjectId(DateTimeUtils.stringToDate(dateStart,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        ObjectId delId=new ObjectId(DateTimeUtils.stringToDate(dateEnd,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        List<ObjectId> stuIds=uisMap.get("stuIds");
        //获取观看的视频信息
        List<VideoViewRecordEntry> VVREs=videoViewService.getVideoViewRecordByParamList(stuIds, dslId, delId, "");
        List<ManageCountDTO> list=new ArrayList<ManageCountDTO>();
        List<String> dateAreas=getUseTimeArea(dateStart,dateEnd);
        DateTimeUtils time=new DateTimeUtils();
        //把每日的用户访问量，放到对应的日期下
        for(String date: dateAreas){
            ManageCountDTO mcDto=new ManageCountDTO();
            mcDto.setCreateTime(date);
            int newCount=0;
            for(VideoViewRecordEntry item:VVREs){
                if(videoIds.contains(item.getVideoInfo().getId())){
                    Long updateTime=item.getID().getTime();
                    String pubTime=time.getLongToStrTime(updateTime);
                    if(date.equals(pubTime)){
                        newCount++;
                    }
                }
            }
            mcDto.setNewCountTotal(newCount);
            list.add(mcDto);
        }
        return list;
    }

    /**
     * 产生一个date1到date2的时间list
     * @param date1
     * @param date2
     * @return
     */
    private List<String> getUseTimeArea(String date1,String date2){
        List<String> list2 = new ArrayList<String>();
        if(date1==null||"".equals(date1)){
            date1="2014-09-01";
        }
        if(date2==null||"".equals(date2)){
            date2=DateTimeUtils.getCurrDate();
        }
        while (DateTimeUtils.compare_date2(date1,date2)<1) {
            list2.add(date1.substring(0,10));
            date1 = DateTimeUtils.getSpecifiedDayAfter(date1);
        }
        return list2;
    }

    /**
     * 分页查询数量-微校园、微家园发帖数、
     * @param role
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @param pageable
     * @return
     * @throws ResultTooManyException
     */
    public Page<ManageCountDTO> getMicroCampussOrHomePostNumPage(
            Integer role,
            String schoolId,
            String gradeId,
            String classId,
            String dateStart,
            String dateEnd,
            Pageable pageable) throws ResultTooManyException {
        int page = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId,gradeId,schoolId);
        int blogtype=-1;
        List<ObjectId> usIds=null;
        if(role== UserRole.TEACHER.getRole()){//当统计对象角色是老师时
            blogtype=1;//blogtype=1,查询微校园发帖数
            usIds=uisMap.get("teaIds");//获取老师id集合
        }else if(role==UserRole.STUDENT.getRole()){//当统计对象角色是学生时
            blogtype=2;//blogtype=2,查询微家园发帖数
            usIds=uisMap.get("stuIds");//获取学生id集合
        }else if(role==UserRole.PARENT.getRole()){//当统计对象角色是学生家长时
            blogtype=2;//blogtype=2,查询微家园发帖数
            usIds=uisMap.get("parIds");//获取学生家长id集合
        }
        //处理校园排行榜-微校园、微家园发帖数
        List<ManageCountDTO> list =handleMicroCampusOrHomePostNum(usIds, DeleteState.NORMAL, blogtype, dateStart, dateEnd, 0, 0,-1,"pbt");
        //模拟对list分页查询
        List<ManageCountDTO> pageList =listImitatePage(list,page,pageSize);
        Collections.reverse(pageList);
        Long total =(long)list.size();
        return new PageImpl<ManageCountDTO>(pageList, pageable,total);
    }

    /**
     * 模拟对list分页查询
     * @param list
     * @param page
     * @param pageSize
     * @return
     */
    private List<ManageCountDTO> listImitatePage(List<ManageCountDTO> list,int page,int pageSize) {
        int totalCount =list.size();
        int pageCount=0;
        int m=totalCount%pageSize;
        if(m>0){
            pageCount=totalCount/pageSize+1;
        } else {
            pageCount=totalCount/pageSize;
        }
        List<ManageCountDTO> subList=new ArrayList<ManageCountDTO>();
        if(list!=null&&list.size()>0) {
            if (m == 0) {
                subList = list.subList((page - 1) * pageSize, pageSize * (page));
            } else {
                if (page == pageCount) {
                    subList = list.subList((page - 1) * pageSize, totalCount);
                } else {
                    subList = list.subList((page - 1) * pageSize, pageSize * (page));
                }
            }
        }
        return subList;
    }

    /**
     * 分页查询数量-班级课程上传、备课上传数
     * @param funId
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @param pageable
     * @return
     */
    public Page<ManageCountDTO> getClassOrPrepUploadNumPage(
            Integer funId,
            String schoolId,
            String gradeId,
            String classId,
            String dateStart,
            String dateEnd,
            Pageable pageable){
        int page = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId,gradeId,schoolId);
        List<ObjectId> usIds=uisMap.get("teaIds");
        int type=0;
        /*if(funId== TeacherCountType.CLASS_UPLOAD_NUM.getState()){//当访问方法是班级课程时
            type=LessonType.CLASS_LESSON.getType();//type=2时，班级课程上传数
        }*/
        if(funId==TeacherCountType.PREPARATION_UPLOAD_NUM.getState()){//当访问方法是备课上传数时
            type=LessonType.BACKUP_LESSON.getType();//type=1时，备课上传数
        }
        //处理校园排行榜-班级课程上传、备课上传数
        List<ManageCountDTO> list =handleClassOrPrepUploadNum(usIds, type, dateStart, dateEnd, -1, "lut");
        //模拟对list分页查询
        List<ManageCountDTO> pageList =listImitatePage(list,page,pageSize);
        Collections.reverse(pageList);
        Long total =(long)list.size();
        return new PageImpl<ManageCountDTO>(pageList, pageable,total);
    }

    /**
     * 分页查询数量-作业上传数
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @param pageable
     * @return
     */
    public Page<ManageCountDTO> getHomeworkUploadNumPage(
            String schoolId,
            String gradeId,
            String classId,
            String dateStart,
            String dateEnd,
            Pageable pageable) {
        int page = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId,gradeId,schoolId);
        List<ObjectId> usIds=uisMap.get("teaIds");
        //处理校园排行榜-作业上传数
        List<ManageCountDTO> list =handleHomeworkUploadNum(usIds, dateStart, dateEnd, -1, "lut");
        //模拟对list分页查询
        List<ManageCountDTO> pageList =listImitatePage(list,page,pageSize);
        Collections.reverse(pageList);
        Long total =(long)list.size();
        return new PageImpl<ManageCountDTO>(pageList, pageable,total);
    }

    /**
     * 分页查询数量-通知发布数
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @param pageable
     * @return
     */
    public Page<ManageCountDTO> getNoticeNumPage(
            String schoolId,
            String gradeId,
            String classId,
            String dateStart,
            String dateEnd,
            Pageable pageable) {
        int page = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId,gradeId,schoolId);
        List<ObjectId> usIds=uisMap.get("teaIds");
        //处理校园排行榜-通知发布数
        List<ManageCountDTO> list =handleNoticePublishNum(usIds, dateStart, dateEnd, -1, "");
        //模拟对list分页查询
        List<ManageCountDTO> pageList =listImitatePage(list,page,pageSize);
        Collections.reverse(pageList);
        Long total =(long)list.size();
        return new PageImpl<ManageCountDTO>(pageList, pageable,total);
    }

    /**
     * 分页查询数量-试卷上传数
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @param pageable
     * @return
     */
    public Page<ManageCountDTO> getPapersUploadNumPage(
            String schoolId,
            String gradeId,
            String classId,
            String dateStart,
            String dateEnd,
            Pageable pageable){
        int page = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId,gradeId,schoolId);
        List<ObjectId> usIds=uisMap.get("teaIds");
        int type=1;
        //处理校园排行榜-试卷上传数
        List<ManageCountDTO> list =handlePapersUploadNum(usIds, type, dateStart, dateEnd, -1, "");
        //模拟对list分页查询
        List<ManageCountDTO> pageList =listImitatePage(list,page,pageSize);
        Collections.reverse(pageList);
        Long total =(long)list.size();
        return new PageImpl<ManageCountDTO>(pageList, pageable,total);
    }

    /**
     * 分页查询数量-班级课程观看数
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @param pageable
     * @return
     */
    public Page<ManageCountDTO> getClassesWatchNumPage(
            String schoolId,
            String gradeId,
            String classId,
            String dateStart,
            String dateEnd,
            Pageable pageable){
        int page = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId,gradeId,schoolId);
        //处理校园排行榜-班级课程观看数
        List<ManageCountDTO> list =handleClassesWatchNum(uisMap, dateStart, dateEnd, -1, "");
        //模拟对list分页查询
        List<ManageCountDTO> pageList =listImitatePage(list, page, pageSize);
        Collections.reverse(pageList);
        Long total =(long)list.size();
        return new PageImpl<ManageCountDTO>(pageList, pageable,total);
    }

    /**
     * 分页查询数量-作业完成数
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @param pageable
     * @return
     */
    public Page<ManageCountDTO> getJobCompletionNumPage(
            String schoolId,
            String gradeId,
            String classId,
            String dateStart,
            String dateEnd,
            Pageable pageable){
        int page = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId,gradeId,schoolId);
        List<ObjectId> usIds=uisMap.get("stuIds");
        //处理校园排行榜-作业完成数
        List<ManageCountDTO> list =handleJobCompletionNum(usIds, dateStart, dateEnd, -1, "sul.ti");
        //模拟对list分页查询
        List<ManageCountDTO> pageList =listImitatePage(list,page,pageSize);
        Collections.reverse(pageList);
        Long total =(long)list.size();
        return new PageImpl<ManageCountDTO>(pageList, pageable,total);
    }

    /**
     * 分页查询数量-试卷完成数
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @param pageable
     * @return
     */
    public Page<ManageCountDTO> getPapersCompletionNumPage(
            String schoolId,
            String gradeId,
            String classId,
            String dateStart,
            String dateEnd,
            Pageable pageable){
        int page = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId,gradeId,schoolId);
        //处理校园排行榜-试卷完成数
        List<ManageCountDTO> list =handlePapersCompletionNum(uisMap, dateStart, dateEnd, -1, "");
        //模拟对list分页查询
        List<ManageCountDTO> pageList =listImitatePage(list,page,pageSize);
        Collections.reverse(pageList);
        Long total =(long)list.size();
        return new PageImpl<ManageCountDTO>(pageList, pageable,total);
    }

    /**
     * 分页查询数量-云课程观看数
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param dateStart
     * @param dateEnd
     * @param pageable
     * @return
     */
    public Page<ManageCountDTO> getCloudCurriculumViewNumPage(
            String schoolId,
            String gradeId,
            String classId,
            String dateStart,
            String dateEnd,
            Pageable pageable){
        int page = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        Map<String,List<ObjectId>> uisMap=getUserIdsMap(classId,gradeId,schoolId);
        //处理校园排行榜-云课程观看数
        List<ManageCountDTO> list =handleCloudCurriculumViewNum(uisMap, dateStart, dateEnd, -1, "");
        //模拟对list分页查询
        List<ManageCountDTO> pageList =listImitatePage(list, page, pageSize);
        Collections.reverse(pageList);
        Long total =(long)list.size();
        return new PageImpl<ManageCountDTO>(pageList, pageable,total);
    }

    /**
     * 查询功能使用统计明细-微校园、微家园发帖数
     * @param role
     * @param operateUserId
     * @param dateStart
     * @param dateEnd
     * @param pageable
     * @return
     * @throws ResultTooManyException
     */
    public Page<ManageCountDTO> getMicroCampusOrHomePostDetailPage(
            Integer role,
            String operateUserId,
            String dateStart,
            String dateEnd,
            Pageable pageable)
            throws ResultTooManyException {
        int page = pageable.getOffset()-pageable.getPageSize();
        int pageSize = pageable.getPageSize();
        DateTimeUtils time=new DateTimeUtils();
        long dsl=time.getStrToLongTime(dateStart);
        long del=time.getStrToLongTime(dateEnd);
        int blogtype=-1;
        List<ObjectId> usIds=new ArrayList<ObjectId>();
        if(role== UserRole.TEACHER.getRole()){//当统计对象角色是老师时
            blogtype=1;//blogtype=1,查询微校园发帖数
        }else if(role==UserRole.STUDENT.getRole()){//当统计对象角色是学生时
            blogtype=2;//blogtype=2,查询微家园发帖数
        }else if(role==UserRole.PARENT.getRole()){//当统计对象角色是学生家长时
            blogtype=2;//blogtype=2,查询微家园发帖数
        }
        usIds.add(new ObjectId(operateUserId));
        //根据查询条件查询发微博信息
        List<MicroBlogEntry> MBs= microBlogDao.getMicroBlogEntryByParamList(usIds, DeleteState.NORMAL, blogtype, dsl, del, Constant.FIELDS,page, pageSize,"pbt");
        List<ManageCountDTO> list=new ArrayList<ManageCountDTO>();
        for(MicroBlogEntry microBlog:MBs){
            ManageCountDTO mcDto=new ManageCountDTO();
            mcDto.setId(microBlog.getID().toString());
            mcDto.setUserId(microBlog.getUserId().toString());
            mcDto.setName(microBlog.getContent());
            mcDto.setCreateTime(time.getLongToStrTimeTwo(microBlog.getPublishTime()));
            list.add(mcDto);
        }
        sortList(list);
        Collections.reverse(list);
        //根据查询条件查询发微博信息数量
        Long total = microBlogDao.getMicroBlogEntryByParamTotal(usIds, DeleteState.NORMAL, blogtype, dsl, del);
        return new PageImpl<ManageCountDTO>(list, pageable,total);
    }

    /**
     * 查询功能使用统计明细-班级课程上传、备课上传数
     * @param funId
     * @param operateUserId
     * @param dateStart
     * @param dateEnd
     * @param pageable
     * @return
     */
    public Page<ManageCountDTO> getClassOrPrepUploadDetailPage(
            Integer funId,
            String operateUserId,
            String dateStart,
            String dateEnd,
            Pageable pageable){
        int page = pageable.getOffset()-pageable.getPageSize();
        int pageSize = pageable.getPageSize();
        DateTimeUtils time=new DateTimeUtils();
        long dsl=time.getStrToLongTime(dateStart);
        long del=time.getStrToLongTime(dateEnd);
        List<ObjectId> usIds=new ArrayList<ObjectId>();
        usIds.add(new ObjectId(operateUserId));
        int type=0;
        /*if(funId== TeacherCountType.CLASS_UPLOAD_NUM.getState()){//当访问方法是班级课程时
            type=LessonType.CLASS_LESSON.getType();//type=2时，班级课程上传数
        }*/
        if(funId==TeacherCountType.PREPARATION_UPLOAD_NUM.getState()){//当访问方法是备课上传数时
            type=LessonType.BACKUP_LESSON.getType();//type=1时，备课上传数
        }

        List<ObjectId> ownerList =new ArrayList<ObjectId>();
        if(type==LessonType.BACKUP_LESSON.getType()){
            ownerList.addAll(usIds);
        }
        Map<ObjectId,ObjectId> map1=new HashMap<ObjectId, ObjectId>();
        if(type==LessonType.CLASS_LESSON.getType()){
            List<TeacherClassSubjectDTO> list = teacherClassLessonService.getTeacherClassSubjectDTOByUidsList(usIds);
            for (TeacherClassSubjectDTO dto : list) {
                ownerList.add(new ObjectId(dto.getId()));
                map1.put(new ObjectId(dto.getId()), new ObjectId(dto.getTeacherId()));
            }
        }

        List<DirEntry> dirList =dirService.getDirEntryList(ownerList, new BasicDBObject(Constant.ID, Constant.ONE).append("ow", Constant.ONE), type);
        List<ObjectId> dirids=new ArrayList<ObjectId>();
        Map<ObjectId,ObjectId> map2=new HashMap<ObjectId, ObjectId>();
        for(DirEntry dirEntry : dirList){
            dirids.add(dirEntry.getID());
            map2.put(dirEntry.getID(), dirEntry.getOwerId());
        }

        //获取用户备课空间、班级课程信息
        List<LessonEntry> LEs=lessonService.getLessonEntryByParamList(dirids, type, dsl, del, page, pageSize, "lut");
        List<ManageCountDTO> list=new ArrayList<ManageCountDTO>();
        for(LessonEntry lessonEntry:LEs){
            ManageCountDTO mcDto=new ManageCountDTO();
            mcDto.setId(lessonEntry.getID().toString());
            mcDto.setUserId(operateUserId);
            mcDto.setName(lessonEntry.getName());
            mcDto.setCreateTime(time.getLongToStrTimeTwo(lessonEntry.getLastUpdateTime()));
            list.add(mcDto);
        }
        sortList(list);
        Collections.reverse(list);
        //获取用户备课空间、班级课程上传数目
        Long total =(long)lessonService.selLessonCount(dirids,type,dsl,del);
        return new PageImpl<ManageCountDTO>(list, pageable,total);
    }

    /**
     * 查询功能使用统计明细-作业上传发布数
     * @param operateUserId
     * @param dateStart
     * @param dateEnd
     * @param pageable
     * @return
     */
    public Page<ManageCountDTO> getHomeworkUploadDetailPage(String operateUserId, String dateStart, String dateEnd, Pageable pageable) {
        int page = pageable.getOffset()-pageable.getPageSize();
        int pageSize = pageable.getPageSize();
        DateTimeUtils time=new DateTimeUtils();
        ObjectId dslId=new ObjectId(DateTimeUtils.stringToDate(dateStart,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        ObjectId delId=new ObjectId(DateTimeUtils.stringToDate(dateEnd,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        List<ObjectId> usIds=new ArrayList<ObjectId>();
        usIds.add(new ObjectId(operateUserId));
        //获取统计对象上传的作业信息
        List<HomeWorkEntry> HWs=homeWorkService.getHomeworkUploadByParamList(usIds, dslId, delId, page, pageSize, "lut");
        List<ManageCountDTO> list=new ArrayList<ManageCountDTO>();
        for(HomeWorkEntry homeWork:HWs){
            ManageCountDTO mcDto=new ManageCountDTO();
            mcDto.setId(homeWork.getID().toString());
            mcDto.setUserId(homeWork.getTeacherId().toString());
            mcDto.setName(homeWork.getName());
            mcDto.setCreateTime(time.getLongToStrTimeTwo(homeWork.getID().getTime()));
            list.add(mcDto);
        }
        sortList(list);
        Collections.reverse(list);
        //查询作业访问数
        Long total =(long)homeWorkService.selHomeWorkCount(usIds, dslId, delId);
        return new PageImpl<ManageCountDTO>(list, pageable,total);
    }

    /**
     * 查询功能使用统计明细-作业上传、通知发布数
     * @param operateUserId
     * @param dateStart
     * @param dateEnd
     * @param pageable
     * @return
     */
    public Page<ManageCountDTO> getNoticeDetailPage(String operateUserId, String dateStart, String dateEnd, Pageable pageable) {
        int page = pageable.getOffset()-pageable.getPageSize();
        int pageSize = pageable.getPageSize();
        DateTimeUtils time=new DateTimeUtils();
        ObjectId dslId=new ObjectId(DateTimeUtils.stringToDate(dateStart,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        ObjectId delId=new ObjectId(DateTimeUtils.stringToDate(dateEnd,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        List<ObjectId> usIds=new ArrayList<ObjectId>();
        usIds.add(new ObjectId(operateUserId));
        //获取统计对象通知发布信息
        List<NoticeEntry> NEs=noticeService.getNoticePublishByParamList(usIds, dslId, delId, page, pageSize, "");
        List<ManageCountDTO> list=new ArrayList<ManageCountDTO>();
        for(NoticeEntry noticeEntry:NEs){
            ManageCountDTO mcDto=new ManageCountDTO();
            mcDto.setId(noticeEntry.getID().toString());
            mcDto.setUserId(noticeEntry.getTeacherId().toString());
            mcDto.setName(noticeEntry.getContent());
            mcDto.setCreateTime(time.getLongToStrTimeTwo(noticeEntry.getID().getTime()));
            list.add(mcDto);
        }
        sortList(list);
        Collections.reverse(list);
        //获取统计对象通知发布信息数量
        Long total =(long)noticeService.selNoticePublishCount(usIds, dslId, delId);
        return new PageImpl<ManageCountDTO>(list, pageable,total);
    }

    /**
     * 查询功能使用统计明细-试卷上传数
     * @param operateUserId
     * @param dateStart
     * @param dateEnd
     * @param pageable
     * @return
     */
    public Page<ManageCountDTO> getPapersUploadDetailPage(
            String operateUserId,
            String dateStart,
            String dateEnd,
            Pageable pageable){
        int page = pageable.getOffset()-pageable.getPageSize();
        int pageSize = pageable.getPageSize();
        DateTimeUtils time=new DateTimeUtils();
        long dsl=time.getStrToLongTime(dateStart);
        long del=time.getStrToLongTime(dateEnd);
        List<ObjectId> usIds=new ArrayList<ObjectId>();
        usIds.add(new ObjectId(operateUserId));
        int type=1;
        //获取统计对象上传的试卷信息
        List<ExerciseEntry> EEs=exerciseService.getPapersUploadByParamList(usIds, type, dsl, del, page, pageSize, "lut");
        List<ManageCountDTO> list=new ArrayList<ManageCountDTO>();
        for(ExerciseEntry item:EEs){
            ManageCountDTO mcDto=new ManageCountDTO();
            mcDto.setId(item.getID().toString());
            mcDto.setUserId(item.getTeacherId().toString());
            mcDto.setName(item.getName());
            mcDto.setCreateTime(time.getLongToStrTimeTwo(item.getLastUpdateTime()));
            list.add(mcDto);
        }
        sortList(list);
        Collections.reverse(list);
        //查询考试访问数
        Long total =(long)exerciseService.selPaperCount(usIds, type, dsl, del);
        return new PageImpl<ManageCountDTO>(list, pageable,total);
    }

    /**
     * 查询功能使用统计明细-班级课程观看数
     * @param operateUserId
     * @param dateStart
     * @param dateEnd
     * @param pageable
     * @return
     */
    public Page<ManageCountDTO> getClassesWatchDetailPage(
            String operateUserId,
            String dateStart,
            String dateEnd,
            Pageable pageable){
        int page = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        //int pageSize = pageable.getPageSize()+pageable.getOffset();
        //根据学生id查询班级信息
        ClassEntry classEntry= classService.searchClassEntryByStuId(operateUserId);
        List<ObjectId> ownerList =new ArrayList<ObjectId>();

        List<TeacherClassSubjectDTO> list1 = teacherClassLessonService.getTeacherClassSubjectDTOByUidsList(classEntry.getTeachers());
        for (TeacherClassSubjectDTO dto : list1) {
            ownerList.add(new ObjectId(dto.getId()));
        }

        List<DirEntry> dirList =dirService.getDirEntryList(ownerList, new BasicDBObject(Constant.ID, 1), LessonType.CLASS_LESSON.getType());
        List<ObjectId> dirids= MongoUtils.getFieldObjectIDs(dirList, Constant.ID);


        //获取用户备课空间、班级课程信息
        List<LessonEntry> LEs=lessonService.getLessonEntryByParamList(dirids, LessonType.CLASS_LESSON.getType(), 0, 0, 0, 0, "");
        Set<ObjectId> videoIds=new HashSet<ObjectId>();
        for(LessonEntry item:LEs){
            if(item.getVideoCount()>0){
                for(ObjectId oId:item.getVideoIds()){
                    videoIds.add(oId);
                }
            }
        }
        ObjectId dslId=new ObjectId(DateTimeUtils.stringToDate(dateStart,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        ObjectId delId=new ObjectId(DateTimeUtils.stringToDate(dateEnd,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        List<ObjectId> stuIds=new ArrayList<ObjectId>();
        stuIds.add(new ObjectId(operateUserId));
        //获取观看的视频信息
        List<VideoViewRecordEntry> VVREs=videoViewService.getVideoViewRecordByParamList(stuIds, dslId, delId, "");
        List<ManageCountDTO> list=new ArrayList<ManageCountDTO>();
        DateTimeUtils time=new DateTimeUtils();
        for(VideoViewRecordEntry item:VVREs){
            if(videoIds.contains(item.getVideoInfo().getId())){
                ManageCountDTO mcDto=new ManageCountDTO();
                mcDto.setId(item.getID().toString());
                mcDto.setUserId(item.getUserInfo().getId().toString());
                if(item.getVideoInfo().getValue()!=null){
                    mcDto.setName(item.getVideoInfo().getValue().toString());
                }else{
                    mcDto.setName("无名称");
                }
                mcDto.setCreateTime(time.getLongToStrTimeTwo(item.getID().getTime()));
                list.add(mcDto);
            }
        }
        //模拟对list分页查询
        List<ManageCountDTO> pageList =listImitatePage(list,page,pageSize);
        sortList(pageList);
        Collections.reverse(pageList);
        //查询学生课程视频观看数
        Long total =(long)videoViewService.showLessonCount(stuIds, videoIds, dslId, delId);
        return new PageImpl<ManageCountDTO>(pageList, pageable,total);
    }

    /**
     * 查询功能使用统计明细-作业完成数
     * @param operateUserId
     * @param dateStart
     * @param dateEnd
     * @param pageable
     * @return
     */
    public Page<ManageCountDTO> getJobCompletionDetailPage(
            String operateUserId,
            String dateStart,
            String dateEnd,
            Pageable pageable){
        int page = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        List<ObjectId> usIds=new ArrayList<ObjectId>();
        usIds.add(new ObjectId(operateUserId));
        DateTimeUtils time=new DateTimeUtils();
        long dsl=time.getStrToLongTime(dateStart);
        long del=time.getStrToLongTime(dateEnd);
        //获取统计对象作业完成信息
        List<HomeWorkEntry> HWs=homeWorkService.getJobCompletionByParamList(usIds, dsl, del, "sul.ti");
        List<ManageCountDTO> list=new ArrayList<ManageCountDTO>();
        for(HomeWorkEntry item:HWs){
            for(StudentSubmitHomeWork shw:item.getSubmitList()) {
                String sutId=shw.getStudentId()==null?"":shw.getStudentId().toString();
                if(operateUserId.equals(sutId)) {
                    Long updateTime = shw.getTime();
                    String pubTime = time.getLongToStrTimeTwo(updateTime);
                    ManageCountDTO mcDto = new ManageCountDTO();
                    mcDto.setId(sutId);
                    mcDto.setCreateTime(pubTime);
                    mcDto.setName(item.getName());
                    list.add(mcDto);
                }
            }
        }
        //排序
        sortList(list);
        //模拟对list分页查询
        List<ManageCountDTO> pageList =listImitatePage(list,page,pageSize);
        Collections.reverse(pageList);
        Long total =(long)list.size();
        return new PageImpl<ManageCountDTO>(pageList, pageable,total);
    }

    /**
     * 对list进行排序
     * @param list
     */
    private void sortList(List<ManageCountDTO> list){
        Collections.sort(list, new Comparator<ManageCountDTO>() {
            public int compare(ManageCountDTO obj1 , ManageCountDTO obj2) {
                int flag=obj1.getCreateTime().compareTo(obj2.getCreateTime());
                if(flag==0){
                    return obj1.getName().compareTo(obj2.getName());
                }else{
                    return flag;
                }
            }
        });
    }

    /**
     * 查询功能使用统计明细-试卷完成数
     * @param operateUserId
     * @param dateStart
     * @param dateEnd
     * @param pageable
     * @return
     */
    public Page<ManageCountDTO> getPapersCompletionDetailPage(
            String operateUserId,
            String dateStart,
            String dateEnd,
            Pageable pageable){
        int page = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        //根据学生id查询班级信息
        ClassEntry classEntry= classService.searchClassEntryByStuId(operateUserId);
        int type=1;
        //获取统计对象上传的试卷信息
        List<ExerciseEntry> EEs=exerciseService.getPapersUploadByParamList(classEntry.getTeachers(), type, 0, 0, 0, 0, "");
        List<ObjectId> exIds=new ArrayList<ObjectId>();
        Map<ObjectId, String> exMap=new HashMap<ObjectId, String>();
        for(ExerciseEntry item: EEs){
            exIds.add(item.getID());
            exMap.put(item.getID(),item.getName());
        }
        List<ObjectId> usIds=new ArrayList<ObjectId>();
        usIds.add(new ObjectId(operateUserId));
        ObjectId dslId=new ObjectId(DateTimeUtils.stringToDate(dateStart,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        ObjectId delId=new ObjectId(DateTimeUtils.stringToDate(dateEnd,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        List<ManageCountDTO> list=new ArrayList<ManageCountDTO>();
        if(exIds.size()>0){
            //获取统计对象试卷完成信息
            Map<String,ExerciseAnswerEntry> EAEMap=exerciseAnswerDao.getPapersCompletionByParamList(usIds, exIds, dslId, delId, "");
            for (ExerciseAnswerEntry  item: EAEMap.values()) {
                exIds.add(item.getDocumentId());
                DateTimeUtils time=new DateTimeUtils();
                Long updateTime = item.getID().getTime();
                String pubTime = time.getLongToStrTimeTwo(updateTime);
                ManageCountDTO mcDto = new ManageCountDTO();
                mcDto.setId(item.getDocumentId().toString());
                mcDto.setName(exMap.get(item.getDocumentId()));
                mcDto.setCreateTime(pubTime);
                list.add(mcDto);
            }
        }
        //模拟对list分页查询
        List<ManageCountDTO> pageList =listImitatePage(list,page,pageSize);
        sortList(pageList);
        Collections.reverse(pageList);
        Long total =(long)list.size();
        return new PageImpl<ManageCountDTO>(pageList, pageable,total);
    }

    /**
     * 查询功能使用统计明细-云课程观看数
     * @param operateUserId
     * @param dateStart
     * @param dateEnd
     * @param pageable
     * @return
     */
    public Page<ManageCountDTO> getCloudCurriculumViewDetailPage(
            String operateUserId,
            String dateStart,
            String dateEnd,
            Pageable pageable){
        int page = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        //int pageSize = pageable.getPageSize()+pageable.getOffset();
        //查询全部云课程信息
        Set<ObjectId> videoIds=cloudLessonService.getAllCloudLessonIdList();
        ObjectId dslId=new ObjectId(DateTimeUtils.stringToDate(dateStart,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        ObjectId delId=new ObjectId(DateTimeUtils.stringToDate(dateEnd,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
        List<ObjectId> stuIds=new ArrayList<ObjectId>();
        stuIds.add(new ObjectId(operateUserId));
        //获取观看的视频信息
        List<VideoViewRecordEntry> VVREs=videoViewService.getVideoViewRecordByParamList(stuIds, dslId, delId, "");
        List<ManageCountDTO> list=new ArrayList<ManageCountDTO>();
        DateTimeUtils time=new DateTimeUtils();
        for(VideoViewRecordEntry item:VVREs){
            if(videoIds.contains(item.getVideoInfo().getId())){
                ManageCountDTO mcDto=new ManageCountDTO();
                mcDto.setId(item.getID().toString());
                mcDto.setUserId(item.getUserInfo().getId().toString());
                if(item.getVideoInfo().getValue()!=null){
                    mcDto.setName(item.getVideoInfo().getValue().toString());
                }else{
                    mcDto.setName("无名称");
                }
                mcDto.setCreateTime(time.getLongToStrTimeTwo(item.getID().getTime()));
                list.add(mcDto);
            }
        }
        //模拟对list分页查询
        List<ManageCountDTO> pageList =listImitatePage(list,page,pageSize);
        sortList(pageList);
        Collections.reverse(pageList);
        //查询学生课程视频观看数
        Long total =(long)videoViewService.showLessonCount(stuIds, videoIds, dslId, delId);
        return new PageImpl<ManageCountDTO>(pageList, pageable,total);
    }

    /**
     * 查询学校
     * @param provinceId
     * @param cityId
     * @param countyId
     * @param schoolType
     * @return
     */
    public List<WebSpiderSchoolDTO> getSchoolValueList(String provinceId, String cityId, String countyId, int schoolType) {
        List<WebSpiderSchoolDTO> resultList=new ArrayList<WebSpiderSchoolDTO>();
        List<WebSpiderSchool> list=webSpiderDao.getSchoolValueList(provinceId,cityId,countyId);
        if(list!=null&&list.size()>0) {
            for (WebSpiderSchool school : list) {
                WebSpiderSchoolDTO schoolDTO=new WebSpiderSchoolDTO();
                String schoolId=school.getSchoolId()==null?"":school.getSchoolId().toString();
                schoolDTO.setSchoolId(schoolId);
                schoolDTO.setSchoolName(school.getSchoolName());
                schoolDTO.setTest(school.getTest());
                if(schoolType>0) {
                    if ((school.getSchoolType()& schoolType) == schoolType) {
                        resultList.add(schoolDTO);
                    }
                }else{
                    resultList.add(schoolDTO);
                }
            }
        }
        return resultList;
    }
}
