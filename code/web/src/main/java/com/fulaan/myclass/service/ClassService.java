package com.fulaan.myclass.service;

import com.db.lesson.DirDao;
import com.db.lesson.LessonDao;
import com.db.school.*;
import com.db.user.UserDao;
import com.db.user.UserSchoolYearExperienceDao;
import com.db.video.VideoViewRecordDao;
import com.db.zouban.ZouBanCourseDao;
import com.fulaan.interestCategory.service.InterestClassTermsService;
import com.fulaan.myclass.controller.MyClassController;
import com.fulaan.myclass.controller.StudentLessonStatView;
import com.fulaan.myclass.controller.TeacherInfoView;
import com.fulaan.myclass.controller.TranscriptView;
import com.fulaan.myschool.controller.GradeView;
import com.fulaan.myschool.controller.TeacherSubjectView;
import com.fulaan.myschool.service.InterestClassService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.utils.SpringContextUtil;
import com.fulaan.utils.TermUtil;
import com.fulaan.zouban.service.TermService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.FieldValuePair;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.app.IdValuePair;
import com.pojo.app.IdValuePairDTO;
import com.pojo.attendance.Attendance;
import com.pojo.exercise.ExerciseAnswerEntry;
import com.pojo.lesson.DirEntry;
import com.pojo.lesson.DirType;
import com.pojo.lesson.LessonEntry;
import com.pojo.school.*;
import com.pojo.user.*;
import com.pojo.video.VideoViewRecordEntry;
import com.pojo.zouban.IdNamePair;
import com.pojo.zouban.ZouBanCourseEntry;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.AvatarUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.Collator;
import java.util.*;

/**
 * Created by hao on 2015/2/27.
 */
@Service
public class ClassService {

    private ClassDao classDao = new ClassDao();
    private UserDao userDao = new UserDao();
    private DirDao dirDao = new DirDao();
    private SchoolDao schoolDao = new SchoolDao();
    private LessonDao lessonDao = new LessonDao();
    private TeacherClassSubjectDao teacherClassSubjectDao = new TeacherClassSubjectDao();
    private InterestClassDao interestClassDao = new InterestClassDao();
    private ExerciseAnswerDao exerciseAnswerDao = new ExerciseAnswerDao();
    private VideoViewRecordDao videoViewRecordDao = new VideoViewRecordDao();
    private AttendanceDao attendanceDao = new AttendanceDao();
    private UserSchoolYearExperienceDao userSchoolYearExperienceDao = new UserSchoolYearExperienceDao();
    private HomeWorkDao homeWorkDao = new HomeWorkDao();
    private InterestClassTermsService interestClassTermsService = new InterestClassTermsService();
    private InterestClassService interestClassService = new InterestClassService();
    private TermService termService = new TermService();
    private ZouBanCourseDao zouBanCourseDao = new ZouBanCourseDao();


    /**
     * 根据学生ID查找所在班级，仅返回名字,ID，班级类型
     *
     * @param studentId
     * @return
     */
    public List<ClassEntry> getStudentClassList(ObjectId studentId) {
        return classDao.getClassEntryListByStudentId(studentId, new BasicDBObject("nm", 1).append("cty", 1));
    }

    /**
     * 根据ID查询详情
     *
     * @param objectId
     * @param fields
     * @return
     */
    public ClassEntry getClassEntryById(ObjectId objectId, DBObject fields) {
        return classDao.getClassEntryById(objectId, fields);
    }

    public InterestClassEntry findInterestClassEntry(ObjectId classId) {
        InterestClassEntry interestClassEntry = interestClassDao.findEntryByClassId(classId);
        return interestClassEntry;
    }


    /**
     * 根据学生ID查询学生主班级
     *
     * @param studentId
     * @param fields
     * @return
     */
    public ClassEntry getClassEntryByStuId(ObjectId studentId, DBObject fields) {
        return classDao.getClassEntryByStuId(studentId, fields);
    }


    /**
     * 添加一个老师
     *
     * @param id
     * @param teacherId
     */
    public void addTeacher(ObjectId id, ObjectId teacherId) {
        classDao.addTeacher(id, teacherId);
    }


    /**
     * 添加一个学生
     *
     * @param id
     * @param studentId
     */
    public void addStudent(ObjectId id, ObjectId studentId) {
        classDao.addStudent(id, studentId);
    }


    /**
     * 更新班主任
     *
     * @param id
     * @param masterid
     */
    public void updateMaster(ObjectId id, ObjectId masterid) {
        classDao.updateMaster(id, masterid);
    }

    /**
     * 添加一个ClassEntry
     *
     * @param e
     * @return
     */
    public ObjectId addClassEntry(ClassEntry e) {
        return classDao.addClassEntry(e);
    }

    /**
     * 根据班级ID查找ClassEntry
     *
     * @param cos
     * @param fields
     * @return
     */
    public Map<ObjectId, ClassEntry> getClassEntryMap(Collection<ObjectId> cos, DBObject fields) {
        return classDao.getClassEntryMap(cos, fields);
    }

    /**
     * 得到班级
     *
     * @param teacherId
     * @param schoolId
     * @return
     * @throws IllegalParamException
     */
    public List<ClassInfoDTO> getSimpleClassInfoDTOs(ObjectId teacherId, ObjectId schoolId) throws IllegalParamException {
        List<ClassInfoDTO> classInfoDTOList = new ArrayList<ClassInfoDTO>();
        List<ClassEntry> classEntryList = classDao.getClassEntryByTeacherId(teacherId, Constant.FIELDS);

        SchoolEntry se = schoolDao.getSchoolEntry(schoolId, Constant.FIELDS);
        if (null == se)
            throw new IllegalParamException();
        Map<ObjectId, Grade> gradeMap = new HashMap<ObjectId, Grade>();
        for (Grade g : se.getGradeList()) {
            gradeMap.put(g.getGradeId(), g);
        }
        ClassInfoDTO dto = null;
        Grade g = null;
        for (ClassEntry ce : classEntryList) {
            dto = new ClassInfoDTO(ce);
            g = gradeMap.get(ce.getGradeId());
            if (null != g) {
                dto.setGradeName(g.getName());
                dto.setGradeType(g.getGradeType());
            }
            classInfoDTOList.add(dto);
        }

        return classInfoDTOList;

    }

    /**
     * 得到班级
     *
     * @param userId
     * @param schoolId
     * @return
     */
    public ClassInfoDTO getClassInfoDTOByStuId(ObjectId userId, ObjectId schoolId) throws IllegalParamException {

        ClassEntry ce = classDao.getClassEntryByStuId(userId, Constant.FIELDS);
        SchoolEntry se = schoolDao.getSchoolEntry(schoolId, Constant.FIELDS);
        if (null == se)
            throw new IllegalParamException();
        Map<ObjectId, Grade> gradeMap = new HashMap<ObjectId, Grade>();
        for (Grade g : se.getGradeList()) {
            gradeMap.put(g.getGradeId(), g);
        }
        ClassInfoDTO dto = new ClassInfoDTO(ce);
        Grade g = gradeMap.get(ce.getGradeId());
        if (null != g) {
            dto.setGradeName(g.getName());
            dto.setGradeType(g.getGradeType());
        }
        return dto;
    }

    /*
    *
    * 根据班级id 和关键字查找班级学生
    *
    * */
    public List<UserDetailInfoDTO> findStuByClassIdAndKeyword(String classId, String keyword) {
        ClassEntry classEntry = classDao.getClassEntryById(new ObjectId(classId), Constant.FIELDS);
        List<ObjectId> objectIdList = classEntry.getStudents();
        List<UserEntry> userEntryList = userDao.findUserEntriesLimitRoleAndKeyword(objectIdList, keyword, Constant.FIELDS);
        List<UserDetailInfoDTO> userInfoDTO4WBList = new ArrayList<UserDetailInfoDTO>();
        for (UserEntry userEntry : userEntryList) {
            UserDetailInfoDTO userInfoDTO4WB = new UserDetailInfoDTO(userEntry);
            userInfoDTO4WBList.add(userInfoDTO4WB);
        }
        return userInfoDTO4WBList;
    }


    /*
    * 班级经验值排名前五的学生
    *
    * */
    public List<UserDetailInfoDTO> findStudentTop5ByClassId(String classId) {
        List<ObjectId> objectIdList = classDao.getClassEntryById(new ObjectId(classId), Constant.FIELDS).getStudents();
        List<UserEntry> userEntryList = userDao.findUserEntriesLimitRole(objectIdList, Constant.FIELDS);
        Collections.sort(userEntryList, new Comparator<UserEntry>() {
            @Override
            public int compare(UserEntry o1, UserEntry o2) {
                return o2.getExperiencevalue() - o1.getExperiencevalue();
            }
        });
        if (userEntryList.size() > 5) {
            userEntryList = userEntryList.subList(0, 5);
        }
        List<UserDetailInfoDTO> userInfoDTO4WBList = new ArrayList<UserDetailInfoDTO>();
        for (UserEntry userEntry : userEntryList) {
            UserDetailInfoDTO userInfoDTO4WB = new UserDetailInfoDTO(userEntry);
            userInfoDTO4WBList.add(userInfoDTO4WB);
        }
        return userInfoDTO4WBList;
    }

    /*
    * 查找学生主要班级名称
    *
    * */
    public String findMainClassNameByUserId(String userId) {
        ClassEntry classEntry = classDao.getClassEntryByStuId(new ObjectId(userId), Constant.FIELDS);
        if (classEntry == null) {
            return null;
        }
        return classEntry.getName();
    }

    /*
    *
    * 根据班级id 查找班级学生
    *
    * */
    public List<UserDetailInfoDTO> findStuByClassId(String classId) {
        ClassEntry classEntry = classDao.getClassEntryById(new ObjectId(classId), Constant.FIELDS);
        //选修课学生
        ZouBanCourseEntry zouBanCourseEntry = interestClassService.getZoubanCourseEntry(new ObjectId(classId));

        List<ObjectId> objectIdList = new ArrayList<ObjectId>();

        if (classEntry != null) {
            objectIdList.addAll(classEntry.getStudents());
        } else if (zouBanCourseEntry != null) {
            objectIdList.addAll(zouBanCourseEntry.getStudentList());
        }

        List<UserEntry> userEntryList = userDao.getUserEntryList(objectIdList, Constant.FIELDS);

        List<UserDetailInfoDTO> userInfoDTO4WBList = new ArrayList<UserDetailInfoDTO>();
        for (UserEntry userEntry : userEntryList) {
            UserDetailInfoDTO userInfoDTO4WB = new UserDetailInfoDTO(userEntry);
            userInfoDTO4WBList.add(userInfoDTO4WB);
        }
        return userInfoDTO4WBList;
    }

    /**
     * 根据班级Id查找兴趣班学生列表
     * author:qiangm
     *
     * @param classId
     * @return
     */
    public List<UserDetailInfoDTO> findStuByInterestClassId(String classId) {
        InterestClassEntry classEntry = interestClassDao.findEntryByClassId(new ObjectId(classId));
        ZouBanCourseEntry zouBanCourseEntry = interestClassService.getZoubanCourseEntry(new ObjectId(classId));

        List<ObjectId> objectIdList = new ArrayList<ObjectId>();

        if (classEntry != null) {//兴趣班
            List<InterestClassStudent> studentList = classEntry.getCurrentInterestClassStudents();
            for (InterestClassStudent interestClassStudent : studentList) {
                objectIdList.add(interestClassStudent.getStudentId());
            }
        } else if (zouBanCourseEntry != null) {//选修课
            objectIdList.addAll(zouBanCourseEntry.getStudentList());
        }

        List<UserEntry> userEntryList = userDao.findUserEntriesLimitRole(objectIdList, Constant.FIELDS);

        List<UserDetailInfoDTO> userInfoDTO4WBList = new ArrayList<UserDetailInfoDTO>();
        for (UserEntry userEntry : userEntryList) {
            UserDetailInfoDTO userInfoDTO4WB = new UserDetailInfoDTO(userEntry);
            userInfoDTO4WBList.add(userInfoDTO4WB);
        }
        return userInfoDTO4WBList;
    }

    /**
     * 根据班级ID查找学生ID list
     *
     * @param classId
     * @return
     */
    public List<ObjectId> findStuByClassId(ObjectId classId) {
        List<ObjectId> stuList = new ArrayList<ObjectId>();
        ClassEntry classEntry = classDao.getClassEntryById(classId, Constant.FIELDS);
        if (classEntry != null) {//行政班
            stuList = classEntry.getStudents();
        } else {//兴趣班
            InterestClassEntry interestClassEntry = interestClassDao.findEntryByClassId(classId);
            if (interestClassEntry != null) {
                List<InterestClassStudent> studentList = interestClassEntry.getInterestClassStudents();
                for (InterestClassStudent interestClassStudent : studentList) {
                    stuList.add(interestClassStudent.getStudentId());
                }
            } else {//选修课
                ZouBanCourseEntry zouBanCourseEntry = interestClassService.getZoubanCourseEntry(classId);
                stuList.addAll(zouBanCourseEntry.getStudentList());
            }
        }
        return stuList;
    }

    /*
    *
    * 依据主键查找班级信息
    *
    * */
    public ClassInfoDTO findClassInfoByClassId(String classId) {
        ClassEntry classEntry = classDao.getClassEntryById(new ObjectId(classId), Constant.FIELDS);
        if (classEntry == null)
            return null;
        ClassInfoDTO classInfoDTO = new ClassInfoDTO();
        classInfoDTO.setClassName(classEntry.getName());
        classInfoDTO.setId(classEntry.getID().toString());
        classInfoDTO.setIntroduce(classEntry.getIntroduce());
        classInfoDTO.setStudentIds(classEntry.getStudents());
        classInfoDTO.setTeacherIds(classEntry.getTeachers());
        classInfoDTO.setGradeId(classEntry.getGradeId().toString());
        List<ObjectId> ids = classEntry.getTeachers();
//        if(ids!=null && !ids.isEmpty()){
        classInfoDTO.setMainTeacherId(classEntry.getMaster() == null ? "0" : classEntry.getMaster().toString());
//        }

        SchoolEntry se = schoolDao.getSchoolEntry(classEntry.getSchoolId(), Constant.FIELDS);
        if (null != se) {
            Map<ObjectId, Grade> gradeMap = new HashMap<ObjectId, Grade>();
            for (Grade g : se.getGradeList()) {
                gradeMap.put(g.getGradeId(), g);
            }
            Grade g = gradeMap.get(classEntry.getGradeId());
            if (null != g) {
                classInfoDTO.setGradeName(g.getName());
                classInfoDTO.setGradeType(g.getGradeType());
            }
        }

        return classInfoDTO;
    }

    /**
     * 根据学校id查询年级下的班级信息
     *
     * @param schoolID
     * @return
     */
    public List<ClassInfoDTO> findClassInfoBySchoolId(String schoolID) {
        List<ClassEntry> classEntryList = classDao.findClassInfoBySchoolId(new ObjectId(schoolID), Constant.FIELDS);

        List<ClassInfoDTO> classInfoDTOs = new ArrayList<ClassInfoDTO>();
        for (ClassEntry classEntry : classEntryList) {
            ClassInfoDTO classInfoDTO = new ClassInfoDTO();

            classInfoDTO.setClassName(classEntry.getName());
            classInfoDTO.setId(classEntry.getID().toString());
            classInfoDTO.setIntroduce(classEntry.getIntroduce());
            classInfoDTO.setStudentIds(classEntry.getStudents());
            if (null != classEntry.getGradeId()) {
                classInfoDTO.setGradeId(classEntry.getGradeId().toString());
            }
            classInfoDTOs.add(classInfoDTO);
        }
        return classInfoDTOs;
    }

    /*
    *
    * 查询年级下是否存在班级
    *
    * */
    public boolean existClass(String gradeId) {
        List<ClassEntry> classEntryList = classDao.findClassEntryByGradeId(new ObjectId(gradeId));
        if (classEntryList == null || classEntryList.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * 根据年级id查询年级下的班级信息
     *
     * @param gradeid
     * @return
     */
    public List<ClassInfoDTO> findClassByGradeId(String gradeid) {
        List<ClassEntry> classEntryList = classDao.findClassEntryByGradeId(new ObjectId(gradeid));
        List<ClassInfoDTO> classInfoDTOList = new ArrayList<ClassInfoDTO>();

        if (classEntryList == null || classEntryList.size() == 0) {
            return classInfoDTOList;
        }


        List<ObjectId> objectIdList = new ArrayList<ObjectId>();

        for (ClassEntry classEntry : classEntryList) {
            ClassInfoDTO classInfoDTO = new ClassInfoDTO();
            classInfoDTO.setClassName(classEntry.getName());
            classInfoDTO.setId(classEntry.getID().toString());
            classInfoDTO.setIntroduce(classEntry.getIntroduce());
            classInfoDTO.setStudentIds(classEntry.getStudents());
            classInfoDTO.setTeacherIds(classEntry.getTeachers());
            if (null != classEntry.getMaster()) {
                classInfoDTO.setMainTeacherId(classEntry.getMaster().toString());
                objectIdList.add(classEntry.getMaster());
            }

            classInfoDTOList.add(classInfoDTO);
        }
        List<UserEntry> userEntryList = userDao.getUserEntryList(objectIdList, new BasicDBObject("nm", 1));
        Map<ObjectId, UserEntry> map = new HashMap<ObjectId, UserEntry>();
        for (UserEntry userEntry : userEntryList) {
            map.put(userEntry.getID(), userEntry);
        }
        for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
            String teacherId = classInfoDTO.getMainTeacherId();
            if (!StringUtils.isBlank(teacherId)) {
                UserEntry userEntry = map.get(new ObjectId(teacherId));
                if (userEntry != null) {
                    classInfoDTO.setMainTeacher(userEntry.getUserName());
                }
            }

        }

        //对班级列表按顺序排序
        ClassInfoDTO classInfoDTO = classInfoDTOList.get(0);
        if ((classInfoDTO.getClassName().contains("(") && classInfoDTO.getClassName().contains(")")) ||
                (classInfoDTO.getClassName().contains("（") && classInfoDTO.getClassName().contains("）"))) {
            Collections.sort(classInfoDTOList, new Comparator<ClassInfoDTO>() {
                @Override
                public int compare(ClassInfoDTO o1, ClassInfoDTO o2) {
                    try {
                        String name1 = o1.getClassName();
                        String name2 = o2.getClassName();

                        String n1 = "";
                        String n2 = "";

                        if (name1.contains("(")) {
                            n1 = name1.substring(name1.indexOf("(") + 1, name1.indexOf(")"));
                        } else {
                            n1 = name1.substring(name1.indexOf("（") + 1, name1.indexOf("）"));
                        }

                        if (name2.contains("(")) {
                            n2 = name2.substring(name2.indexOf("(") + 1, name2.indexOf(")"));
                        } else {
                            n2 = name2.substring(name2.indexOf("（") + 1, name2.indexOf("）"));
                        }

                        return Integer.parseInt(n1) - Integer.parseInt(n2);
                    } catch (Exception ex) {

                    }
                    return 0;
                }
            });
        }
        return classInfoDTOList;
    }

    /**
     * 根据年级id查询年级下的班级id
     *
     * @param gradeids
     * @return
     */
    public List<ClassInfoDTO> findClassByGradeIdList(List<ObjectId> gradeids) {
        List<ClassEntry> classEntryList = classDao.findClassEntryByGradeIds(gradeids);
        if (classEntryList == null)
            return null;

        List<ClassInfoDTO> classInfoDTOList = new ArrayList<ClassInfoDTO>();

        for (ClassEntry classEntry : classEntryList) {
            ClassInfoDTO classInfoDTO = new ClassInfoDTO();
            classInfoDTO.setId(classEntry.getID().toString());
            classInfoDTOList.add(classInfoDTO);
        }
        return classInfoDTOList;
    }

    /*
    * 更新classInfo 信息  by classid
    * 级联更新  teacherClassSubject 中的班级名称字段
    * 级联更新 DirEntry 中 才，className
    * */
    public void updateClassInfo(String classId, String classname, String teacherId) {
        ObjectId clazzId = new ObjectId(classId);

        ObjectId newClassMaster = null;
        if (null != teacherId && ObjectId.isValid(teacherId)) {
            newClassMaster = new ObjectId(teacherId);
        }
        classDao.updateClassNameAndTeacherIdById(clazzId, classname, newClassMaster);
        teacherClassSubjectDao.updateClassNameByClassId(clazzId, classname);
        List<ObjectId> classIdList = new ArrayList<ObjectId>();
        classIdList.add(clazzId);
        List<TeacherClassSubjectEntry> tcsList = teacherClassSubjectDao.findEntryByClassIds(classIdList);
        if (tcsList != null) {
            for (TeacherClassSubjectEntry entry : tcsList) {
                //只更新顶级目录
                dirDao.updateTopClassDirByOwnerId(entry.getID(), "dn", classname + entry.getSubjectInfo().getValue());
            }
        }
    }

    /*
    *
    * 添加班级信息
    *
    * */
    public void addClassInfo(ClassInfoDTO classInfoDTO) {
        ClassEntry classEntry = classInfoDTO.exportEntry();
        classDao.addClassEntry(classEntry);
    }

    /*
    *
    * 删除某个班级 前提是该班级下没有老师
    *
    * */
    public boolean deleteClassInfoById(String classid) {
        ClassEntry classEntry = classDao.getClassEntryById(new ObjectId(classid), Constant.FIELDS);
        List<ObjectId> teacherIds = classEntry.getTeachers();
        List<ObjectId> studentIds = classEntry.getStudents();
        boolean t = teacherIds == null || teacherIds.isEmpty();
        boolean s = studentIds == null || studentIds.isEmpty();
        if (s && t) {
            classDao.deleteById(new ObjectId(classid));
            return true;
        }
        if (s) {
            if (teacherIds.size() == 1 && teacherIds.get(0).equals(classEntry.getMaster())) {
                //只有一个班主任时 也可以删除
                classDao.deleteById(new ObjectId(classid));
                return true;
            }
        }
        return false;
    }

    /*
    *
    * 查找班级老师id
    * */
    public List<ObjectId> findTeacherIdsById(ObjectId objectId) {
        ClassEntry classEntry = classDao.getClassEntryById(objectId, Constant.FIELDS);
        if (classEntry == null)//普通班级里没有找到，在兴趣班找
        {
            InterestClassEntry interestClassEntry = interestClassDao.findEntryByClassId(objectId);
            List<ObjectId> teachers = new ArrayList<ObjectId>();
            teachers.add(interestClassEntry.getTeacherId());
            return teachers;
        }

        List<ObjectId> teachers = classEntry.getTeachers();
        return teachers;
    }

    /*
    * 查找班级老师信息
    *
    * */
    public List<TeacherInfoView> findTeachersByClassId(ObjectId classId) {
        List<ObjectId> uids = findTeacherIdsById(classId);
        List<UserEntry> userEntryList = userDao.getUserEntryList(uids, Constant.FIELDS);

        List<TeacherClassSubjectEntry> teacherClassSubjectEntryList = teacherClassSubjectDao.findSubjectByTeacherIdAndClassId(uids, classId);
        //构建map
        Map<ObjectId, List<String>> subjectMap = new HashMap<ObjectId, List<String>>();
        for (TeacherClassSubjectEntry teacherClassSubjectEntry : teacherClassSubjectEntryList) {
            List<String> subjectList = subjectMap.get(teacherClassSubjectEntry.getTeacherId());
            if (subjectList == null) {
                subjectList = new ArrayList<String>();
            }
            subjectList.add(teacherClassSubjectEntry.getSubjectInfo().getValue().toString());
            subjectMap.put(teacherClassSubjectEntry.getTeacherId(), subjectList);
        }
        List<TeacherInfoView> teacherInfoViewArrayList = new ArrayList<TeacherInfoView>();
        for (UserEntry userEntry : userEntryList) {
            TeacherInfoView teacherInfoView = new TeacherInfoView();
            teacherInfoView.setId(userEntry.getID().toString());
            teacherInfoView.setUserName(userEntry.getUserName());
            teacherInfoView.setImageUrl(AvatarUtils.getAvatar(userEntry.getAvatar(), 1));
            teacherInfoView.setRole(userEntry.getRole());
            teacherInfoView.setSubjectNameList(subjectMap.get(userEntry.getID()));

            teacherInfoViewArrayList.add(teacherInfoView);
        }
        return teacherInfoViewArrayList;
    }

    /*
    *
    * 依据班级id 查询老师科目信息Entry
    * */
    public List<TeacherSubjectView> findTeacherByIds(List<ObjectId> objectIdList, String classId) {
        List<TeacherClassSubjectEntry> teacherClassLessonEntryList =
                teacherClassSubjectDao.findSubjectByTeacherIdAndClassId(objectIdList, new ObjectId(classId));

        Map<ObjectId, UserEntry> userMap = new HashMap<ObjectId, UserEntry>();
        List<TeacherSubjectView> teacherSubjectViewList = new ArrayList<TeacherSubjectView>();
        List<UserEntry> userEntryList = userDao.getUserEntryList(objectIdList, Constant.FIELDS);
        for (UserEntry userEntry : userEntryList) {
            userMap.put(userEntry.getID(), userEntry);
        }
        for (TeacherClassSubjectEntry teacherClassLessonEntry : teacherClassLessonEntryList) {

            TeacherSubjectView teacherSubjectView = new TeacherSubjectView();
            teacherSubjectView.setTclId(teacherClassLessonEntry.getID().toString());
            teacherSubjectView.setTeacherId(teacherClassLessonEntry.getTeacherId().toString());
            teacherSubjectView.setSubjectId(teacherClassLessonEntry.getSubjectInfo().getId().toString());
            teacherSubjectView.setSubjectName(teacherClassLessonEntry.getSubjectInfo().getValue().toString());

            UserEntry userEntry = userMap.get(teacherClassLessonEntry.getTeacherId());
            if (userEntry != null) {
                teacherSubjectView.setUserName(userEntry.getUserName());
            }
            teacherSubjectViewList.add(teacherSubjectView);
        }

        return teacherSubjectViewList;
    }

    /*
    * 添加一个老师科目  关系 到teacherClassSubjectEntry  同时向  classEntry中的老师数组中添加  该老师id
    *
    * */
    public void addTeacherSubject(String classid, String teacherid, String subjectid) {
        ClassEntry classEntry = classDao.getClassEntryById(new ObjectId(classid), Constant.FIELDS);
        ObjectId schoolId = classEntry.getSchoolId();
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(schoolId, Constant.FIELDS);
        List<Subject> subjects = schoolEntry.getSubjects();
        Map<ObjectId, String> subjectMap = new HashMap<ObjectId, String>();
        for (Subject subject : subjects) {
            subjectMap.put(subject.getSubjectId(), subject.getName());
        }
        TeacherClassSubjectEntry teacherClassLessonEntry = new TeacherClassSubjectEntry(new BasicDBObject());
        teacherClassLessonEntry.setTeacherId(new ObjectId(teacherid));
        teacherClassLessonEntry.setClassInfo(new IdValuePair(new ObjectId(classid), classEntry.getName()));
        teacherClassLessonEntry.setSubjectInfo(new IdValuePair(new ObjectId(subjectid), subjectMap.get(new ObjectId(subjectid))));
        //插入teacherClassSubjectEntry
        ObjectId tclId = teacherClassSubjectDao.addTeacherClassSubjectEntry(teacherClassLessonEntry);
        //新建dir
        DirEntry entry = new DirEntry(tclId, classEntry.getName() + subjectMap.get(new ObjectId(subjectid)), null, 0, DirType.CLASS_LESSON);
        dirDao.addDirEntry(entry);
        //插入 classEntry集合 如果数组中不存在 才插入
        List<ObjectId> idList = classEntry.getTeachers();
        boolean k = false;
        if (idList != null) {
            for (ObjectId objectId : idList) {
                if (objectId.toString().equals(teacherid)) {
                    k = true;
                    break;
                }
            }
        }
        if (!k) {
            classDao.addTeacher(new ObjectId(classid), new ObjectId(teacherid));
        }
    }

    /*
    *
    * 更新老师科目信息
    *
    * */
    public void updateTeacherSubject(String tclid, String teacherid, String subjectid, String subjectName) {
        ObjectId tcsId = new ObjectId(tclid);
        ObjectId teacherId = new ObjectId(teacherid);
        //更新teacherClassEntry 集合
        //因为老师课程 关系在班级之下  所以班级年级信息不做修改
        teacherClassSubjectDao.updateTeacherAndSubject(tcsId, new ObjectId(teacherid), new ObjectId(subjectid), subjectName);
        //保证同步  如果有必要  更新classEntry中的老师id 数组
        teacherClassSubjectDao.getTeacherClassSubjectEntry(new ObjectId(tclid));
        TeacherClassSubjectEntry subjectEntry = teacherClassSubjectDao.getTeacherClassSubjectEntry(tcsId);
        ObjectId classId = subjectEntry.getClassInfo().getId();
        ClassEntry classEntry = classDao.getClassEntryById(classId, Constant.FIELDS);
        List<ObjectId> teacherIds = classEntry.getTeachers();
        boolean k = false;//表示classEntry中不包含当前参数teacherid
        if (teacherIds != null) {
            for (ObjectId objectId : teacherIds) {
                if (objectId.toString().equals(teacherid)) {
                    k = true;
                    break;
                }
            }
        }
        if (!k) {
            classDao.addTeacher(classId, teacherId);
        }

    }

    /*
    *
    * 依据主键删除  老师科目信息记录
    *
    * */
    public void deleteTeacherSubjectById(String tclId) {
        //级联  判断是否删除classEntry中的 tid
        ObjectId teacherClassSubjectId = new ObjectId(tclId);
        //先查找 再删除
        TeacherClassSubjectEntry subjectEntry = teacherClassSubjectDao.getTeacherClassSubjectEntry(teacherClassSubjectId);
        teacherClassSubjectDao.deleteById(teacherClassSubjectId);
        ObjectId classId = subjectEntry.getClassInfo().getId();
        List<ObjectId> classIdList = new ArrayList<ObjectId>();
        classIdList.add(classId);
        List<TeacherClassSubjectEntry> subjectEntryList = teacherClassSubjectDao.findEntryByClassIds(classIdList);
        boolean k = false;//默认不存在
        if (subjectEntryList != null) {
            for (TeacherClassSubjectEntry teacherClassSubjectEntry : subjectEntryList) {
                k = teacherClassSubjectEntry.getTeacherId().equals(subjectEntry.getTeacherId());
                if (k) break;
            }
        }
        if (!k) {//确实不存在
            //删除classEntry中的tid
            classDao.deleteTeacherByIdAndTeacherId(classId, subjectEntry.getTeacherId());
        }
    }

    /*
    * 班级添加学生
    * */
    public void addStudentId(String classId, String stuId) {
        classDao.addStudent(new ObjectId(classId), new ObjectId(stuId));
    }

    /*
    * 班级删除学生
    * */
    public void deleteStuFromClass(String classId, String studentid) {
        classDao.deleteStuById(new ObjectId(classId), new ObjectId(studentid));
    }

    /*
    *
    * 学生调换班级
    *
    * */
    public void updateStuClass(String oldClassId, String newClassId, String studentId) {
        classDao.deleteStuById(new ObjectId(oldClassId), new ObjectId(studentId));
        classDao.addStudent(new ObjectId(newClassId), new ObjectId(studentId));
    }

    /*
    * 查找学生所在班级list
    *
    * */
    public List<ClassInfoDTO> findClassInfoByStuId(ObjectId stuId) {
        List<ClassInfoDTO> classInfoDTOList = new ArrayList<ClassInfoDTO>();
        List<InterestClassEntry> interestClassEntries = interestClassDao.findClassInfoByStuId(stuId);
        if (interestClassEntries != null) {
            for (InterestClassEntry interestClassEntry : interestClassEntries) {
                ClassInfoDTO classInfoDTO = new ClassInfoDTO();
                classInfoDTO.setId(interestClassEntry.getID().toString());
                classInfoDTO.setClassName(interestClassEntry.getClassName());
                classInfoDTO.setClassType(2);
                classInfoDTOList.add(classInfoDTO);
            }
        }
        List<ClassEntry> classEntryList = classDao.findClassEntryByStuId(stuId);
        if (classEntryList != null) {
            for (ClassEntry classEntry : classEntryList) {
                ClassInfoDTO classInfoDTO = new ClassInfoDTO();
                classInfoDTO.setId(classEntry.getID().toString());
                classInfoDTO.setClassName(classEntry.getName());
                classInfoDTO.setClassType(1);
                classInfoDTO.setTeacherIds(classEntry.getTeachers());
                classInfoDTO.setStudentCount(classEntry.getTotalStudent());
                classInfoDTOList.add(classInfoDTO);
            }
        }
        return classInfoDTOList;
    }

    /**
     * 查询学生所在班级
     * @param studentId
     * @return
     */
    public List<ClassEntry> findClassEntryByStuId(ObjectId studentId) {
        return classDao.findClassEntryByStuId(studentId);
    }


    /*
    *
    * 当前老师 所教班级的集合 不包括兴趣班
    *
    *
    * */
    public List<ClassInfoDTO> findClassInfoByTeacherId(ObjectId teacherId) {
//        Map<ObjectId,Set<ObjectId>> classMap=teacherClassSubjectDao.getClassLessonSet(teacherId);
        //主班级
        List<ClassEntry> classEntryList = classDao.getClassEntryByTeacherId(teacherId, Constant.FIELDS);
        List<ObjectId> ids = new ArrayList<ObjectId>();
        List<ClassInfoDTO> classInfoDTOList = new ArrayList<ClassInfoDTO>();
        //迭代主班级
        for (ClassEntry classEntry : classEntryList) {
            try {
                int hwCount = homeWorkDao.findHomeWorkEntrysCount(null, classEntry.getID(), 3, new ObjectId("000000000000000000000000"), null, 0);
                ClassInfoDTO classInfoDTO = new ClassInfoDTO();
                classInfoDTO.setHomeworkCount(hwCount);
                classInfoDTO.setClassName(classEntry.getName());
                classInfoDTO.setClassType(1);
                classInfoDTO.setGradeId(classEntry.getGradeId().toString());
                ObjectId teacherID = classEntry.getMaster();
                if (teacherID != null) {
                    classInfoDTO.setMainTeacherId(teacherID.toString());
                    ids.add(teacherID);
                }
                classInfoDTO.setId(classEntry.getID().toString());
                classInfoDTO.setStudentCount(classEntry.getStudents().size());
                classInfoDTO.setTeacherIds(classEntry.getTeachers());
                classInfoDTOList.add(classInfoDTO);
            } catch (Exception ex) {

            }

        }
        List<UserEntry> userEntryList = userDao.getUserEntryList(ids, Constant.FIELDS);
        Map<String, UserEntry> map = new HashMap<String, UserEntry>();
        if (userEntryList != null) {
            for (UserEntry userEntry : userEntryList) {
                map.put(userEntry.getID().toString(), userEntry);
            }
        }
        for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
            UserEntry userEntry = map.get(classInfoDTO.getMainTeacherId());
            if (userEntry != null)
                classInfoDTO.setMainTeacher(userEntry.getUserName());
//            Set<ObjectId> idSet=classMap.get(classInfoDTO.getId());
//            if(idSet!=null){
//                classInfoDTO.setLessonCount(idSet.size());
//            }
        }
        return classInfoDTOList;
    }

    /*
    * 统计学生视频 和习题信息
    *
    * */
    private List<StudentStat> statStudentInfo(ObjectId classId, ObjectId schoolId, List<ObjectId> studentIds, int termType) {

        List<VideoViewRecordEntry> videoViewRecordEntries = videoViewRecordDao.findVideoViewRecordByClassId(classId);
        List<ObjectId> stuIds = studentIds;
        //构建id 头像map
        List<UserEntry> userEntryList = userDao.getUserEntryList(stuIds, Constant.FIELDS);
        Map<ObjectId, String> imgMap = new HashMap<ObjectId, String>();
        for (UserEntry userEntry : userEntryList) {
            imgMap.put(userEntry.getID(), AvatarUtils.getAvatar(userEntry.getAvatar(), 1));
        }

        Map<ObjectId, ClassInfoDTO> classMap = getStudentClassInfo(schoolId, stuIds);

        //构建视频观看数量map
        Map<ObjectId, Integer> viewCountMap = new HashMap<ObjectId, Integer>();
        for (VideoViewRecordEntry videoViewRecordEntry : videoViewRecordEntries) {
            ObjectId studentId = videoViewRecordEntry.getUserInfo().getId();
            Integer viewCount = viewCountMap.get(studentId);
            if (viewCount == null) {
                viewCountMap.put(studentId, 1);
            } else {
                viewCountMap.put(studentId, viewCount + 1);
            }
        }

        //构建做题数量map
        List<ExerciseAnswerEntry> exerciseAnswerEntries = exerciseAnswerDao.findAnswerByUserIds(stuIds);
        Map<ObjectId, Integer> exerciseMap = new HashMap<ObjectId, Integer>();
        for (ExerciseAnswerEntry exerciseAnswerEntry : exerciseAnswerEntries) {
            Integer count = exerciseMap.get(exerciseAnswerEntry.getUserId());
            if (count != null) {
                exerciseMap.put(exerciseAnswerEntry.getUserId(), count + 1);
            } else {
                exerciseMap.put(exerciseAnswerEntry.getUserId(), 1);
            }
        }

        //构建考勤map
        List<InterestClassLessonScoreEntry> interestClassLessonScoreEntryList = interestClassDao.findLessonScoreByClassId(classId, termType);
        Map<ObjectId, Integer> attendanceMap = new HashMap<ObjectId, Integer>();
        for (InterestClassLessonScoreEntry entry : interestClassLessonScoreEntryList) {
            if (entry.getTermType() == termType) {
                Integer count = attendanceMap.get(entry.getUserId());
                if (count != null) {
                    attendanceMap.put(entry.getUserId(), count + entry.getAttendance());
                } else {
                    attendanceMap.put(entry.getUserId(), entry.getAttendance());
                }
            }

        }
        //课时数
        int lessonNum = 0;
        if (attendanceMap.size() > 0) {
            int remainder = interestClassLessonScoreEntryList.size() % attendanceMap.size();
            lessonNum = remainder == 0 ? interestClassLessonScoreEntryList.size() / attendanceMap.size() : interestClassLessonScoreEntryList.size() / attendanceMap.size() + 1;
        }
        List<UserSchoolYearExperienceEntry> schoolYearExps = userSchoolYearExperienceDao.getUserSchoolYearExperienceList(classId);
        Map<ObjectId, Integer> schoolYearExpMap = new HashMap<ObjectId, Integer>();
        for (UserSchoolYearExperienceEntry schoolYearExp : schoolYearExps) {
            schoolYearExpMap.put(schoolYearExp.getUserId(), schoolYearExp.getSchoolYearExperience());
        }

        List<StudentStat> studentStatList = new ArrayList<StudentStat>();
        //迭代db层对象 构建view层对象
        for (UserEntry userEntry : userEntryList) {
            Integer viewCount = viewCountMap.get(userEntry.getID());
            Integer exerciseDoneCount = exerciseMap.get(userEntry.getID());
            Integer at = attendanceMap.get(userEntry.getID());
            if (at == null) {
                at = 0;
            }

            StudentStat studentStat = new StudentStat();
            studentStat.setStudentId(userEntry.getID().toString());
            studentStat.setRole(userEntry.getRole());
            studentStat.setUserName(userEntry.getUserName());
            studentStat.setAttendance(at + "/" + lessonNum);
            ClassInfoDTO dto = classMap.get(userEntry.getID());
            if (null != dto.getGradeName()) {
                studentStat.setGradeName(dto.getGradeName());
            } else {
                studentStat.setGradeName("");
            }
            if (null != dto.getClassName()) {
                studentStat.setClassName(dto.getClassName());
            } else {
                studentStat.setClassName("");
            }
            studentStat.setSex(userEntry.getSex());
            studentStat.setStudentNum(userEntry.getStudyNum());
            studentStat.setStudentJob(userEntry.getJob());
            studentStat.setImageURL(imgMap.get(userEntry.getID()));
            studentStat.setEndViewNum(viewCount == null ? 0 : viewCount);
            studentStat.setEndQuestionNum(exerciseDoneCount == null ? 0 : exerciseDoneCount);

            int exp = schoolYearExpMap.get(userEntry.getID()) == null ? 0 : schoolYearExpMap.get(userEntry.getID());

            studentStat.setExperienceValue(exp);

            studentStatList.add(studentStat);
        }
        sortList(studentStatList);
        return studentStatList;
    }

    /**
     * 对list进行排序
     *
     * @param list
     */
    public void sortList(List<StudentStat> list) {
        Collections.sort(list, new Comparator<StudentStat>() {
            public int compare(StudentStat obj1, StudentStat obj2) {
                int flag = obj1.getGradeName().compareTo(obj2.getGradeName());
                if (flag == 0) {
                    flag = obj1.getClassName().compareTo(obj2.getClassName());
                }
                if (flag == 0) {
                    flag = Collator.getInstance(Locale.CHINESE).compare(obj1.getUserName(), obj2.getUserName());
                    //flag=obj1.getUserName().compareTo(obj2.getUserName());
                }
                return flag;
            }
        });
    }


    /*
    *
    * 统计单个学生学习情况
    *
    * */
    public List<StudentLessonStatView> statStudent(String studentId) {
        List<VideoViewRecordEntry> videoViewRecordEntries = videoViewRecordDao.findByStudentId(new ObjectId(studentId));
        //获取视频id
        Set<ObjectId> set = new HashSet<ObjectId>();
        for (VideoViewRecordEntry videoViewRecordEntry : videoViewRecordEntries) {
            set.add(videoViewRecordEntry.getVideoInfo().getId());
        }
        List<LessonEntry> lessonEntries = lessonDao.findLessonByVideoIds(set);

        List<StudentLessonStatView> studentLessonStatViewList = new ArrayList<StudentLessonStatView>();
        List<ObjectId> exerciseIds = new ArrayList<ObjectId>();
        for (LessonEntry lessonEntry : lessonEntries) {
            StudentLessonStatView studentLessonStatView = new StudentLessonStatView();
            //设置课程名称
            studentLessonStatView.setLessonName(lessonEntry.getName());
            //设置当前学生是否看完该课程下的全部视频
            List<ObjectId> videoIds = lessonEntry.getVideoIds();
            if (videoIds != null) {
                for (ObjectId objectId : videoIds) {
                    //如果lesson 中的视频id  recordMap 中有一个未包含就判定该lesson 当前学生未看完，并跳出循环
                    if (!set.contains(objectId))
                        studentLessonStatView.setIsView(false);
                    break;
                }
            }
            //收集exerciseId
            exerciseIds.add(lessonEntry.getExercise());
            //设置总题目数
            studentLessonStatView.setTotalExerciseCount(lessonEntry.getExerciseCount());
            studentLessonStatView.setDocumentId(lessonEntry.getExercise());
            studentLessonStatViewList.add(studentLessonStatView);
        }
        //获取学生作答的全部答案
        List<ExerciseAnswerEntry> exerciseAnswerEntries = exerciseAnswerDao.findByUserId(new ObjectId(studentId));
        Map<ObjectId, Integer> countMap = new HashMap<ObjectId, Integer>();
        Map<ObjectId, Integer> correctMap = new HashMap<ObjectId, Integer>();
        for (ExerciseAnswerEntry exerciseAnswerEntry : exerciseAnswerEntries) {
            Integer count = countMap.get(exerciseAnswerEntry.getDocumentId());
            if (count != null) {

                countMap.put(exerciseAnswerEntry.getDocumentId(), count + 1);
            } else {
                countMap.put(exerciseAnswerEntry.getDocumentId(), 1);
            }


            Integer rightCount = correctMap.get(exerciseAnswerEntry.getDocumentId());
            if (exerciseAnswerEntry.getIsRight() == 1) {
                if (rightCount != null) {
                    correctMap.put(exerciseAnswerEntry.getDocumentId(), count + 1);
                } else {
                    correctMap.put(exerciseAnswerEntry.getDocumentId(), 1);
                }
            }

        }
        //注入已经完成的习题数目(非做对的)
        for (StudentLessonStatView studentLessonStatView : studentLessonStatViewList) {
            ObjectId documentId = studentLessonStatView.getDocumentId();
            if (documentId != null) {
                Integer count = countMap.get(documentId);
                Integer correct = correctMap.get(documentId);

                studentLessonStatView.setDoneExerciseCount(count != null ? count : 0);
                int total = studentLessonStatView.getTotalExerciseCount();
                if (correct == null || correct.equals(0) || total == 0) {
                    studentLessonStatView.setCorrectRate("-");
                } else {
                    studentLessonStatView.setCorrectRate((correct / total) * 100 + "%");
                }
            }

        }
        Collections.sort(studentLessonStatViewList, new Comparator<StudentLessonStatView>() {
            @Override
            public int compare(StudentLessonStatView o1, StudentLessonStatView o2) {
                return o1.getLessonName().compareTo(o2.getLessonName());
            }
        });
        return studentLessonStatViewList;
    }


    //获取兴趣班课时学生得分列表
    public List<InterestClassLessonScore> findLessonScoreByClassId(String classId) {
        List<InterestClassLessonScoreEntry> lessonScoreEntries = interestClassDao.findLessonScoreByClassId(new ObjectId(classId), -1);
        List<ObjectId> ids = new ArrayList<ObjectId>();
        List<InterestClassLessonScore> list = new ArrayList<InterestClassLessonScore>();
        for (InterestClassLessonScoreEntry lessonScoreEntry : lessonScoreEntries) {
            InterestClassLessonScore interestClassLessonScore = new InterestClassLessonScore(lessonScoreEntry);
            list.add(interestClassLessonScore);
            ids.add(lessonScoreEntry.getUserId());
        }
        Map<ObjectId, UserEntry> userEntryMap = userDao.getUserEntryMap(ids, Constant.FIELDS);
        for (InterestClassLessonScore lessonScore : list) {
            UserEntry userEntry = userEntryMap.get(new ObjectId(lessonScore.getUserid()));
            if (userEntry != null) {
                lessonScore.setStudentName(userEntry.getUserName());
                lessonScore.setStudentAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(), 1));
            }
        }
        return list;
    }

    public List<FieldValuePair> getLessonIndexAndName(ObjectId classId, int termType) {
        List<FieldValuePair> lessonIndexAndName = new ArrayList<FieldValuePair>();
        Map<Integer, Object> map = new HashMap<Integer, Object>();
        List<InterestClassLessonScoreEntry> lessonScoreEntries = interestClassDao.findLessonScoreByClassId(classId, termType);
        InterestClassEntry interestClassEntry = interestClassDao.findEntryByClassId(classId);
        if (termType < 0) {
            termType = interestClassEntry.getTermType();
        }
        for (InterestClassLessonScoreEntry entry : lessonScoreEntries) {
            if (entry.getTermType() == termType) {
                map.put(entry.getLessonIndex(), entry.getLessonName());
            }
        }
        for (Integer key : map.keySet()) {
            FieldValuePair fieldValuePair = new FieldValuePair(key.toString(), map.get(key));
            lessonIndexAndName.add(fieldValuePair);
        }

        return lessonIndexAndName;
    }

    public List<Map<String, Object>> getLessonIndexWeekIndexAndName(ObjectId classId, int termType) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<Integer, InterestClassLessonScoreEntry> lessonIndexMap = new TreeMap<Integer, InterestClassLessonScoreEntry>();
        List<InterestClassLessonScoreEntry> lessonScoreEntries = interestClassDao.findLessonScoreByClassId(classId, termType);
        InterestClassEntry interestClassEntry = interestClassDao.findEntryByClassId(classId);
        if (termType < 0) {
            termType = interestClassEntry.getTermType();
        }
        for (InterestClassLessonScoreEntry entry : lessonScoreEntries) {
            if (entry.getTermType() == termType) {
                lessonIndexMap.put(entry.getLessonIndex(), entry);
            }
        }
        for (Integer key : lessonIndexMap.keySet()) {
            Map<String, Object> map = new HashMap<String, Object>();
            InterestClassLessonScoreEntry entry = lessonIndexMap.get(key);
            map.put("li", entry.getLessonIndex());
            map.put("wi", entry.getWeekIndex());
            map.put("lnm", entry.getLessonName());
            list.add(map);

        }

        return list;
    }


    public static List<ObjectId> collectObjectId(List<InterestClassStudent> studentList) {
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        for (InterestClassStudent interestClassStudent : studentList) {
            objectIdList.add(interestClassStudent.getStudentId());
        }
        return objectIdList;
    }

    /*
    * 查询兴趣班 课时打分信息
    *
    * */
    public List<InterestClassLessonScore> findLessonScoreByIndexAndClassId(String classId, Integer index, int termType) {
        List<InterestClassLessonScoreEntry> lessonScoreEntryList = interestClassDao.findLessonScoreByClassIdAndIndex(new ObjectId(classId), index);
        if (termType < 0) {
            InterestClassEntry interestClassEntry = interestClassDao.findEntryByClassId(new ObjectId(classId));
            termType = interestClassEntry.getTermType();
        }
        List<InterestClassLessonScore> lessonScoreList = new ArrayList<InterestClassLessonScore>();
        for (InterestClassLessonScoreEntry lessonScoreEntry : lessonScoreEntryList) {
            if (lessonScoreEntry.getTermType() == termType) {
                InterestClassLessonScore lessonScore = new InterestClassLessonScore(lessonScoreEntry);
                lessonScoreList.add(lessonScore);
            }
        }
        return lessonScoreList;
    }

    public InterestClassLessonScoreEntry findLessonScoreEntry(ObjectId classId, ObjectId stuId, Integer index) {
        return interestClassDao.findLessonScoreEntry(classId, stuId, index);
    }

    /*
    * 统计兴趣班学生信息
    * */
    public List<StudentStat> statInterestClassStudentInfo(ObjectId classId, int termType) {
        InterestClassEntry interestClassEntry = interestClassDao.findEntryByClassId(classId);
        List<InterestClassStudent> totalStu = interestClassEntry.getInterestClassStudents();
        if (termType < 0) {
            termType = interestClassEntry.getTermType();
        }
        List<InterestClassStudent> studentList = new ArrayList<InterestClassStudent>();
        if (null != totalStu && totalStu.size() > 0) {
            for (InterestClassStudent student : totalStu) {
                if (student.getTermType() == termType) {
                    studentList.add(student);
                }
            }
        }
        return statStudentInfo(classId, interestClassEntry.getSchoolId(), collectObjectId(studentList), termType);
    }

    /*
    * 统计行政班学生信息
    * */
    public List<StudentStat> statCommonClassStudentInfo(ObjectId classId) {
        ClassEntry classEntry = classDao.getClassEntryById(classId, Constant.FIELDS);
        return statStudentInfo(classId, classEntry.getSchoolId(), classEntry.getStudents(), -1);
    }

    /*
    * 获得下一个课时的索引(兴趣班)
    * */
    public int getLessonScoreNextIndex(String classId, int termType) {
//        int lessonIndex=interestClassDao.findLessonScoreMaxIndex(new ObjectId(classId));
        int lessonIndex = 0;
        List<InterestClassLessonScoreEntry> list = interestClassDao.findLessonScoreByClassId(new ObjectId(classId), termType);
        InterestClassEntry interestClassEntry = interestClassDao.findEntryByClassId(new ObjectId(classId));
        if (null != list && list.size() > 0) {
            for (InterestClassLessonScoreEntry entry : list) {
//                if(entry.getTermType() == interestClassEntry.getTermType()){
                if (entry.getLessonIndex() > lessonIndex) {
                    lessonIndex = entry.getLessonIndex();
                }
//                }
            }
        }
        return lessonIndex + 1;
    }


    public void saveInterestClassLessonScore(InterestClassLessonScoreEntry lessonScoreEntry, int termType) {
        if (termType < 0) {
            InterestClassEntry interestClassEntry = interestClassDao.findEntryByClassId(lessonScoreEntry.getClassId());
            termType = interestClassEntry.getTermType();
        }
        lessonScoreEntry.setTermType(termType);
        interestClassDao.saveLessonScore(lessonScoreEntry);
    }

    public void updateInterestClassLessonScore(InterestClassLessonScoreEntry entry) {
        interestClassDao.updateInterestClassLessonScore(entry);
    }

    public void editLessonName(ObjectId classId, Integer index, String lessonName, int weekIndex) {
        interestClassDao.updateLessonName(classId, index, lessonName, weekIndex);
    }

    /*
    * 删除lessonScore  记录
    *
    * */
    public int deleteLessonScoreByClassIdAndLessonIndex(String classId, Integer lessonIndex, Integer termType, String schoolId) {
        List<InterestClassLessonScoreEntry> scoreEntries = interestClassDao.findLessonScoreByClassIdAndIndex(new ObjectId(classId), lessonIndex);
        if (termType == -1) {
            InterestClassEntry interestClassEntry = interestClassDao.findEntryByClassId(new ObjectId(classId));
            if (interestClassEntry.getBaseEntry().containsField("tt"))
                termType = interestClassEntry.getTermType();
        }
        interestClassDao.deleteLessonScoreByClassIdAndLessonIndex(new ObjectId(classId), lessonIndex, termType);
        if (scoreEntries.size() > 0) {
            return scoreEntries.get(0).getWeekIndex();
        }
        return termService.findWeekIndex(schoolId, TermUtil.getSchoolYear());
    }

    /*
    *
    *根据兴趣班id 查找成绩单
    *
    * */
    public List<InterestClassTranscriptDTO> findTranscriptByClassId(String classId, int termType) {
        if (termType < 0) {
            InterestClassEntry interestClassEntry = interestClassDao.findEntryByClassId(new ObjectId(classId));
            termType = interestClassEntry.getTermType();
        }
        //构建考勤map
        List<InterestClassLessonScoreEntry> interestClassLessonScoreEntryList = interestClassDao.findLessonScoreByClassId(new ObjectId(classId), termType);
        Map<ObjectId, Integer> attendanceMap = new HashMap<ObjectId, Integer>();
        if (interestClassLessonScoreEntryList != null && interestClassLessonScoreEntryList.size() > 0) {
            for (InterestClassLessonScoreEntry entry : interestClassLessonScoreEntryList) {
                Integer count = attendanceMap.get(entry.getUserId());
                if (count != null) {
                    attendanceMap.put(entry.getUserId(), count + entry.getAttendance());
                } else {
                    attendanceMap.put(entry.getUserId(), entry.getAttendance());
                }
            }
        }
        //课时数
        int lessonNum = 0;
        if (attendanceMap.size() > 0) {
            int remainder = interestClassLessonScoreEntryList.size() % attendanceMap.size();
            lessonNum = remainder == 0 ? interestClassLessonScoreEntryList.size() / attendanceMap.size() : interestClassLessonScoreEntryList.size() / attendanceMap.size() + 1;
        }
        List<InterestClassTranscriptEntry> totaltranscriptEntryList = interestClassDao.findTranscriptByClassId(new ObjectId(classId));
        List<InterestClassTranscriptEntry> transcriptEntryList = new ArrayList<InterestClassTranscriptEntry>();
        if (totaltranscriptEntryList != null && totaltranscriptEntryList.size() > 0) {
            for (InterestClassTranscriptEntry entry : totaltranscriptEntryList) {
                if (entry.getTermType() == termType) {
                    transcriptEntryList.add(entry);
                }
            }
        }
        List<InterestClassTranscriptDTO> transcriptList = new ArrayList<InterestClassTranscriptDTO>();
        for (InterestClassTranscriptEntry transcriptEntry : transcriptEntryList) {
            InterestClassTranscriptDTO classTranscript = new InterestClassTranscriptDTO(transcriptEntry);
//            List<InterestClassLessonScoreEntry> lessonScores=interestClassDao.findLessonScoreByClassIdAndUserId(new ObjectId(classId),transcriptEntry.getUserId());
//            if(lessonScores!=null){
//                int i=0;
//                for(InterestClassLessonScoreEntry scoreEntry:lessonScores){
//                    i+=scoreEntry.getStudentScore();
//                }
//                classTranscript.setSemesterscore(i);
//            }
            Integer at = attendanceMap.get(transcriptEntry.getUserId());
            if (at == null) {
                at = 0;
            }
            classTranscript.setAttendance(at + "/" + lessonNum);
            transcriptList.add(classTranscript);
        }


        return transcriptList;
    }

    //更新图片路径字段
    public void updatePicByIdSelective(InterestClassTranscriptDTO transcript) {
        interestClassDao.updateTransPicById(new ObjectId(transcript.getId()), transcript.getResultspicsrc());
    }

    /*
    * 插入成绩单信息 (兴趣班)
    * */
    public void insertTranscript(InterestClassTranscriptDTO transcript, int termType) {
        InterestClassTranscriptEntry transcriptEntry = transcript.exportEntry();
        transcriptEntry.setTermType(termType);
        interestClassDao.saveTranscript(transcriptEntry);
    }

    /*
    *
    * 依据兴趣班班级Id 和学生id 查找成绩单
    *
    * */
    public TranscriptView findTranscriptViewByUserIdAndClassId(String userId, String classId, int termType) {
        UserEntry userEntry = userDao.getUserEntry(new ObjectId(userId), Constant.FIELDS);
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(userEntry.getSchoolID(), Constant.FIELDS);
        InterestClassEntry interestClassEntry = interestClassDao.findEntryByClassId(new ObjectId(classId));

        UserEntry teacher = userDao.getUserEntry(interestClassEntry.getTeacherId(), Constant.FIELDS);
        InterestClassTranscriptEntry transcriptEntry =
                interestClassDao.findTranscriptByUserIdAndClassId(new ObjectId(userId), new ObjectId(classId), termType);

        List<Subject> subjectList = schoolEntry.getSubjects();
        String subjectName = null;
        if (subjectList != null) {
            for (Subject subject : subjectList) {
                subject.getSubjectId().equals(interestClassEntry.getSubjectId());
                subjectName = subject.getName();
                break;
            }
        }
        if (transcriptEntry != null) {
            InterestClassTranscriptDTO transcript = new InterestClassTranscriptDTO(transcriptEntry);
            TranscriptView transcriptView = new TranscriptView();

            transcriptView.setClassId(transcript.getClassid());
            transcriptView.setUserId(transcript.getUserid());
            transcriptView.setNickName(userEntry.getNickName());
            transcriptView.setStudentNum(userEntry.getStudyNum());
            transcriptView.setSchoolName(schoolEntry.getName());
            transcriptView.setTermType(schoolEntry.getTermType());
            transcriptView.setClassname(interestClassEntry.getClassName());
            transcriptView.setFinalResult(transcript.getFinalresult());
            transcriptView.setResultsPicSrc(transcript.getResultspicsrc());
            transcriptView.setSubjectName(subjectName);
            transcriptView.setTeacherNAME(teacher.getUserName());
            transcriptView.setTeacherComments(transcript.getTeachercomments());
            transcriptView.setUsualResult(transcript.getSemesterscore());

            return transcriptView;
        }
        return null;
    }

    /*
    * 查找学生在某个兴趣班下的所有课时打分信息
    *
    * */
    public List<InterestClassLessonScore> findLessonScoreByUserIdAndClassId(String userId, String classId) {
        List<InterestClassLessonScoreEntry> lessonScoreEntries = interestClassDao.findLessonScoreByClassIdAndUserId(new ObjectId(classId), new ObjectId(userId));
        List<ObjectId> ids = new ArrayList<ObjectId>();
        List<InterestClassLessonScore> list = new ArrayList<InterestClassLessonScore>();
        for (InterestClassLessonScoreEntry lessonScoreEntry : lessonScoreEntries) {
            InterestClassLessonScore interestClassLessonScore = new InterestClassLessonScore(lessonScoreEntry);
            list.add(interestClassLessonScore);
            ids.add(lessonScoreEntry.getUserId());
        }
        Map<ObjectId, UserEntry> userEntryMap = userDao.getUserEntryMap(ids, Constant.FIELDS);
        for (InterestClassLessonScore lessonScore : list) {
            UserEntry userEntry = userEntryMap.get(new ObjectId(lessonScore.getUserid()));
            if (userEntry != null) {
                lessonScore.setStudentName(userEntry.getUserName());
                lessonScore.setStudentAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(), 1));
            }
        }
        return list;
    }

    /*
    *
    * 更新兴趣班学生成绩单
    *
    * */
    public void updateByTranscriptIdSelective(InterestClassTranscriptDTO transcript) {
        interestClassDao.updateTransSelectiveById(transcript.exportEntry());
    }

    /*
    *
    * 查找学校所有兴趣班信息
    * */
    public List<InterestClassDTO> findAllInterestClassOfSchool(String schoolId, ObjectId gradeId, Boolean stateFlag, String categoryId, int sex) {
        if ("allCategory".equals(categoryId)) {
            categoryId = null;
        }
        ObjectId schoolID = new ObjectId(schoolId);
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(schoolID, Constant.FIELDS);
        List<InterestClassEntry> classEntryList = interestClassDao.findInterestClassOfSchool(schoolID, schoolEntry.getTermType(), gradeId, stateFlag, categoryId, sex);
        List<InterestClassEntry> classlist = new ArrayList<InterestClassEntry>();
        for (InterestClassEntry classentry : classEntryList) {//找到全部短课2
            if ((classentry.getFirstTerm() == 0 && classentry.getSecondTerm() == 1)) {
                classlist.add(classentry);
            }
        }
        List<InterestClassStudent> cslist;
        List<InterestClassEntry> removeClass = new ArrayList<InterestClassEntry>();
        for (InterestClassEntry class2entry : classlist) {
            for (InterestClassEntry allClassEntry : classEntryList) {
                if (allClassEntry.getFirstTerm() == 1 && allClassEntry.getSecondTerm() == 0) {//短课1
                    if (allClassEntry.getRelationId() != null && allClassEntry.getRelationId().equals(class2entry.getID())) {
                        removeClass.add(class2entry);
                        allClassEntry.setSecondTerm(1);
                        allClassEntry.setClassName(allClassEntry.getClassName().substring(0, allClassEntry.getClassName().length() - 3));
                        cslist = allClassEntry.getInterestClassStudents();
                        if (class2entry.getInterestClassStudents() != null && class2entry.getInterestClassStudents().size() != 0) {
                            cslist.addAll(class2entry.getInterestClassStudents());
                        }
                        allClassEntry.setInterestClassStudents(cslist);
                    }
                }
            }
        }
        classEntryList.removeAll(removeClass);
        return interestClassEntryList2ModelList(classEntryList);
    }

    private List<InterestClassDTO> interestClassEntryList2ModelList(List<InterestClassEntry> interestClassEntries) {
        List<InterestClassDTO> list = new ArrayList<InterestClassDTO>();
        Set<ObjectId> teacherIds = new HashSet<ObjectId>();

        for (InterestClassEntry interestClassEntry : interestClassEntries) {
            InterestClassDTO interestClassDTO = new InterestClassDTO(interestClassEntry);
            list.add(interestClassDTO);
            teacherIds.add(new ObjectId(interestClassDTO.getTeacherId()));
        }
        Map<ObjectId, UserEntry> userMap = userDao.getUserEntryMap(teacherIds, Constant.FIELDS);
        for (InterestClassDTO interestClassDTO : list) {
            String teacherId = interestClassDTO.getTeacherId();
            UserEntry userEntry = userMap.get(new ObjectId(teacherId));
            if (userEntry != null) {
                interestClassDTO.setTeacherName(userEntry.getUserName());
            }
        }
        return list;
    }

    /**
     * 根据学生id查询班级信息
     *
     * @param stuId
     * @return
     */
    public ClassEntry searchClassEntryByStuId(String stuId) {
        ClassEntry classEntry = classDao.getClassEntryByStuId(new ObjectId(stuId), Constant.FIELDS);
        return classEntry;
    }

    /**
     * 通过班级查找班级的年级信息；用于学生筛选拓展课
     *
     * @param classEntry
     * @return
     */
    public Grade getClassGradeInfo(ClassEntry classEntry) {
        SchoolEntry se = schoolDao.getSchoolEntry(classEntry.getSchoolId(), Constant.FIELDS);
        if (null != se) {
            for (Grade g : se.getGradeList()) {
                if (g.getGradeId().equals(classEntry.getGradeId())) {
                    return g;
                }
            }
        }
        return null;
    }


    /*
    *
    * 获得年级的所有班级信息
    *
    * */
    public List<ClassInfoDTO> getGradeClassesInfo(String gradeId) {
        List<ClassEntry> classEntryList = classDao.findClassEntryByGradeId(new ObjectId(gradeId));
        List<ClassInfoDTO> classInfoDTOList = new ArrayList<ClassInfoDTO>();
        if (classEntryList != null) {
            for (ClassEntry classEntry : classEntryList) {
                ClassInfoDTO classInfoDTO = new ClassInfoDTO();
                classInfoDTO.setClassName(classEntry.getName());
                classInfoDTO.setId(classEntry.getID().toString());
                classInfoDTO.setIntroduce(classEntry.getIntroduce());
                classInfoDTOList.add(classInfoDTO);
            }
        }
        return classInfoDTOList;
    }

    /*
*
* 获得年级的所有班级信息
*
* */
    public List<ClassInfoDTO> getGradeClassesInfo(ObjectId gradeId, ObjectId teacherId) {
        List<ClassEntry> classEntryList = classDao.getGradeClassesInfo(gradeId, teacherId);
        List<ClassInfoDTO> classInfoDTOList = new ArrayList<ClassInfoDTO>();
        if (classEntryList != null) {
            for (ClassEntry classEntry : classEntryList) {
                ClassInfoDTO classInfoDTO = new ClassInfoDTO();
                classInfoDTO.setClassName(classEntry.getName());
                classInfoDTO.setId(classEntry.getID().toString());
                classInfoDTO.setIntroduce(classEntry.getIntroduce());
                classInfoDTOList.add(classInfoDTO);
            }
        }
        return classInfoDTOList;
    }

    /**
     * 查询学校下所有班级
     *
     * @return
     */
    public List<ClassEntry> findClassInfoBySchoolId(ObjectId schoolId, DBObject field) {
        return classDao.findClassInfoBySchoolId(schoolId, field);
    }

    public List<ClassEntry> findClassInfoByTeacherId(ObjectId teacherId, DBObject field) {
        List<ClassEntry> list = classDao.getClassEntryByTeacherId(teacherId, field);
        return list;
    }

    public TeacherClassSubjectDTO findTeacherClassSubjectInfo(String tcsId) {
        if (null == tcsId || !ObjectId.isValid(tcsId)) {
            return null;
        }
        ObjectId teacherClassSubjectId = new ObjectId(tcsId);
        TeacherClassSubjectEntry entry = teacherClassSubjectDao.findEntryByPrimaryKey(teacherClassSubjectId);
        if (entry == null) return null;
        return new TeacherClassSubjectDTO(entry);
    }

    public List<TeacherClassSubjectDTO> findTeacherCLassSubjectByTeacherId(String teacherId) {
        if (null == teacherId || !ObjectId.isValid(teacherId)) {
            return null;
        }
        List<TeacherClassSubjectEntry> entryList = teacherClassSubjectDao.findTeacherCLassSubjectByTeacherId(new ObjectId(teacherId));
        List<TeacherClassSubjectDTO> dtoList = new ArrayList<TeacherClassSubjectDTO>();
        if (entryList != null) {
            for (TeacherClassSubjectEntry entry : entryList) {
                dtoList.add(new TeacherClassSubjectDTO(entry));
            }
        }
        return dtoList;
    }

    public List<ClassEntry> findClassEntryByMasterId(ObjectId masterId) {
        return classDao.findClassEntryByMasterId(masterId);
    }

    //*********************************考勤及学号管理*********************************************************

    /**
     * 根据学生id获取考勤记录
     *
     * @param studentId
     * @return
     */
    public List<Attendance> getAttendanceByStuId(ObjectId studentId, ObjectId classId) {
        return attendanceDao.getAttendanceEntryListByStuId(studentId, classId);
    }

    /**
     * 添加考勤记录
     *
     * @param attendance
     * @return
     */
    public boolean addAttendance(Attendance attendance) {
        //首先判断是否有重复
        boolean ifHave = attendanceDao.checkIfHave(attendance);
        if (ifHave)//已经存在，不可添加
        {
            return false;
        } else {
            attendanceDao.save(attendance.export());
            return true;
        }
    }

    /**
     * 删除考勤记录
     */
    public void deleteAttendance(ObjectId attendanceId) {
        attendanceDao.delete(attendanceId);
    }

    /**
     * 修改学生学号或职务
     *
     * @param userId
     * @param value
     * @param type
     */
    public void updateStuNumOrJob(ObjectId userId, String value, int type) {
        try {
            if (type == 0) {
                userDao.update(userId, "sn", value, false);
            } else if (type == 1) {
                userDao.update(userId, "jo", value, false);
            }
        } catch (Exception e) {

        }
    }


    /**
     * 根据班主任的userId查找任班主任的班级,未查询到返回null
     *
     * @return
     */
    public ClassEntry findClassByClassLeaderUserId(ObjectId classLeaderUserId) {

        return findClassByClassLeaderUserId(classLeaderUserId);
    }

    /**
     * 分页统计学生的选课信息
     */
    public Page<StudentStat> studentInterestClassCount(String schoolId, String gradeId, String classId, int termType, Pageable pageable) {
        int page = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        List<StudentStat> list = studentInterestClassInfo(schoolId, gradeId, classId, termType);
        //模拟对list分页查询
        List<StudentStat> pageList = listImitatePage(list, page, pageSize);
        Long total = (long) list.size();
        return new PageImpl<StudentStat>(pageList, pageable, total);
    }

    /*
    * 统计学生的选课信息
    *
    * */
    @SuppressWarnings("unchecked")
    public List<StudentStat> studentInterestClassInfo(String schoolId, String gradeId, String classId, int termType) {
        List<IdNameValuePairDTO> terms = interestClassTermsService.getInterestClassTermsDTO(new ObjectId(schoolId)).getTerms();

        //通过classId,gradeId,schoolid查询用户id信息
        Map<String, List<ObjectId>> uisMap = getUserIdsMap(classId, gradeId, schoolId);
        List<ObjectId> stuIds = uisMap.get("stuIds");
        //构建id 头像map
        List<UserEntry> userEntryList = userDao.getUserEntryList(stuIds, Constant.FIELDS);
        Map<ObjectId, String> imgMap = new HashMap<ObjectId, String>();
        for (UserEntry userEntry : userEntryList) {
            imgMap.put(userEntry.getID(), AvatarUtils.getAvatar(userEntry.getAvatar(), 1));
        }
        Map<ObjectId, ClassInfoDTO> classMap = getStudentClassInfo(new ObjectId(schoolId), stuIds);

        List<InterestClassDTO> interestClassList = findAllInterestClassOfSchool(schoolId, null, false, null, 2);

        Map<String, List<InterestClassDTO>> icMap = new HashMap<String, List<InterestClassDTO>>();
        for (InterestClassDTO icDto : interestClassList) {
            if (icDto.getStudentList() != null) {
                for (InterestClassStudentDTO icsDto : icDto.getStudentList()) {
                    if (termType == -1 || icsDto.getTermType() == termType) {
                        List<InterestClassDTO> icList = icMap.get(icsDto.getStudentId());
                        if (icList == null) {
                            icList = new ArrayList<InterestClassDTO>();
                        }
                        icList.add(icDto);
                        icMap.put(icsDto.getStudentId(), icList);
                    }
                }
            }
        }

        List<StudentStat> studentList = new ArrayList<StudentStat>();
        //迭代db层对象 构建view层对象
        for (UserEntry userEntry : userEntryList) {
            StudentStat studentDto = new StudentStat();
            studentDto.setStudentId(userEntry.getID().toString());
            studentDto.setRole(userEntry.getRole());
            studentDto.setUserName(userEntry.getUserName());
            studentDto.setImageURL(imgMap.get(userEntry.getID()));
            ClassInfoDTO dto = classMap.get(userEntry.getID());
            if (null != dto.getGradeName()) {
                studentDto.setGradeName(dto.getGradeName());
            } else {
                studentDto.setGradeName("");
            }
            if (null != dto.getClassName()) {
                studentDto.setClassName(dto.getClassName());
            } else {
                studentDto.setClassName("");
            }
            String interestClassInfo = "";
            List<TermTypeDTO> termTypeDTOList = new ArrayList<TermTypeDTO>();
            List<IdValuePairDTO> iClassList = new ArrayList<IdValuePairDTO>();
            List<InterestClassDTO> icList = icMap.get(userEntry.getID().toString());
            if (icList != null) {
                for (InterestClassDTO icDto : icList) {
                    String classtype = "长课";
                    List<InterestClassStudentDTO> stus = icDto.getStudentList();
                    if (stus != null && stus.size() != 0) {
                        for (InterestClassStudentDTO ics : stus) {
                            if (userEntry.getID().toString().equals(ics.getStudentId())) {
                                if (ics.getCourseType() == 1) {
                                    classtype = "短课1";
                                } else if (ics.getCourseType() == 2) {
                                    classtype = "短课2";
                                } else if (ics.getCourseType() == 0) {
                                    classtype = "长课";
                                }
                                String classInfo = getTermName(terms, ics.getTermType()) + icDto.getClassName() + "(" + icDto.getTeacherName() + ")" + classtype;
                                TermTypeDTO termTypeDTO = new TermTypeDTO(ics.getTermType(), classInfo);
                                IdValuePairDTO classDto = new IdValuePairDTO(new ObjectId(icDto.getId()), classInfo);
                                iClassList.add(classDto);
                                termTypeDTOList.add(termTypeDTO);
                            }

                        }
                    }

                }
            }
            Collections.sort(termTypeDTOList);
            for (TermTypeDTO termTypeDTO : termTypeDTOList) {
                interestClassInfo += termTypeDTO.getClassInfo() + "；<br>";
            }

            studentDto.setInterestClassInfo(interestClassInfo);
            studentDto.setInterestClassList(iClassList);
            studentList.add(studentDto);
        }

        sortList(studentList);

        return studentList;
    }

    private String getTermName(List<IdNameValuePairDTO> terms, int termType) {
        String termName = "第" + termType + "次: ";
        if (null != terms) {
            for (IdNameValuePairDTO term : terms) {
                if ((Integer) term.getValue() == termType) {
                    termName = term.getName() + ": ";
                    break;
                }
            }
        }
        return termName;
    }

    public Map<ObjectId, ClassInfoDTO> getStudentClassInfo(ObjectId schoolId, List<ObjectId> stuIds) {
        List<ClassEntry> classEntryList = classDao.getClassEntryByStuIds(stuIds, Constant.FIELDS);
        Map<ObjectId, ClassInfoDTO> map = new HashMap<ObjectId, ClassInfoDTO>();
        SchoolEntry se = schoolDao.getSchoolEntry(schoolId, Constant.FIELDS);
        if (null != se) {
            Map<ObjectId, Grade> gradeMap = new HashMap<ObjectId, Grade>();
            for (Grade g : se.getGradeList()) {
                gradeMap.put(g.getGradeId(), g);
            }
            ClassInfoDTO dto = null;
            Grade g = null;
            for (ClassEntry ce : classEntryList) {
                dto = new ClassInfoDTO(ce);
                g = gradeMap.get(ce.getGradeId());
                if (null != g) {
                    dto.setGradeName(g.getName());
                }

                if (ce.getStudents() != null && ce.getStudents().size() > 0) {
                    for (ObjectId id : ce.getStudents()) {
                        map.put(id, dto);
                    }
                }
            }
        }
        return map;
    }

    /**
     * 模拟对list分页查询
     *
     * @param list
     * @param page
     * @param pageSize
     * @return
     */
    public List<StudentStat> listImitatePage(List<StudentStat> list, int page, int pageSize) {
        int totalCount = list.size();
        int pageCount = 0;
        int m = totalCount % pageSize;
        if (m > 0) {
            pageCount = totalCount / pageSize + 1;
        } else {
            pageCount = totalCount / pageSize;
        }
        List<StudentStat> subList = new ArrayList<StudentStat>();
        if (list != null && list.size() > 0) {
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
     * 查询统计对象id
     *
     * @param classId
     * @param gradeId
     * @param schoolId
     * @return
     */
    public Map<String, List<ObjectId>> getUserIdsMap(String classId, String gradeId, String schoolId) {
        Map<String, List<ObjectId>> uisMap = null;
        //判断班级id是否为空
        if (classId != null && !"".equals(classId)) {
            //不为空时，查询班级用户id
            uisMap = getRoleUserIdByClassId(classId);
        } else if (gradeId != null && !"".equals(gradeId)) {//当班级id为空时，判断年级id是否为空
            //不为空时，查询年级下的所有班级用户id
            uisMap = getRoleUserIdByGradeId(gradeId);
        } else if (schoolId != null && !"".equals(schoolId)) {//当班级id且年级id为空时，判断学校id是否为空
            //不为空时，查询学校的所有用户id
            uisMap = getRoleUserIdBySchoolId(schoolId);
        }
        return uisMap;
    }

    /**
     * 根据班级id查询班级中的学生的userId
     *
     * @param classId
     * @return
     */
    public Map<String, List<ObjectId>> getRoleUserIdByClassId(String classId) {
        Map<String, List<ObjectId>> uisMap = new HashMap<String, List<ObjectId>>();
        //查询班级信息
        ClassInfoDTO classInfo = findClassInfoByClassId(classId);
        //取得班级下的全部学生id
        List<ObjectId> stuIds = classInfo.getStudentIds();
        if (stuIds == null) {
            stuIds = new ArrayList<ObjectId>();
        }
        uisMap.put("stuIds", stuIds);
        return uisMap;
    }

    /**
     * 根据年级id查询班级中的学生的userId
     *
     * @param gradeId
     * @return
     */
    public Map<String, List<ObjectId>> getRoleUserIdByGradeId(String gradeId) {
        Map<String, List<ObjectId>> uisMap = new HashMap<String, List<ObjectId>>();
        //根据年级id查询年级下的班级信息
        List<ClassInfoDTO> clsLis = findClassByGradeId(gradeId);
        List<ObjectId> stuIds = new ArrayList<ObjectId>();
        //从班级信息下获取学生id和教师id
        for (ClassInfoDTO classInfo : clsLis) {
            if (classInfo.getStudentIds() != null) {
                stuIds.addAll(classInfo.getStudentIds());
            }
        }
        uisMap.put("stuIds", stuIds);
        return uisMap;
    }

    /**
     * 根据学校id查询班级中的学生的userId
     *
     * @param schoolId
     * @return
     */
    public Map<String, List<ObjectId>> getRoleUserIdBySchoolId(String schoolId) {
        Map<String, List<ObjectId>> uisMap = new HashMap<String, List<ObjectId>>();
        //根据学校id查询学校的班级信息
        List<ClassInfoDTO> clsLis = findClassInfoBySchoolId(schoolId);
        List<ObjectId> stuIds = new ArrayList<ObjectId>();
        //从班级信息下获取学生id
        for (ClassInfoDTO classInfo : clsLis) {
            if (classInfo.getStudentIds() != null) {
                stuIds.addAll(classInfo.getStudentIds());
            }
        }
        uisMap.put("stuIds", stuIds);
        return uisMap;
    }

    /**
     * 根据学生Id和班级Id查找学生成绩单
     *
     * @param userId
     * @param classId
     * @return
     */
    public InterestClassTranscriptEntry getTranscriptByUserIdAndClassId(ObjectId userId, String classId, int termType) {
        InterestClassTranscriptEntry classTranscript = interestClassDao.findTranscriptByUserIdAndClassId(userId, new ObjectId(classId), termType);
//        List<InterestClassLessonScoreEntry> lessonScores=interestClassDao.findLessonScoreByClassIdAndUserId(new ObjectId(classId),userId);
//        if(lessonScores!=null){
//            int i=0;
//            for(InterestClassLessonScoreEntry scoreEntry:lessonScores){
//                i+=scoreEntry.getStudentScore();
//            }
//            classTranscript.setTotalLessonScore(i);
//        }
        return classTranscript;
    }

    /**
     * 根据学生Id和班级Id查找学生课时成绩
     *
     * @param userId
     * @param classId
     * @return
     */
    public List<InterestClassLessonScoreEntry> getLessonScoreByUserIdAndClassId(ObjectId userId, String classId) {
        return interestClassDao.findLessonScoreByClassIdAndUserId(new ObjectId(classId), userId);
    }

    /**
     * 更新班级年级ID
     *
     * @param cid
     * @param gid
     */
    public void updateGrade(ObjectId cid, ObjectId gid) {
        classDao.updateGrade(cid, gid);
    }

    /**
     * 更新班级名称
     *
     * @param id
     * @param name
     */
    public void updateClassName(ObjectId id, String name) {
        classDao.updateName(id, name);
    }


    public List<ClassInfoDTO> findClassByGradeIds(List<ObjectId> gradeIds) {
        List<ClassEntry> classEntryList = classDao.findClassEntryByGradeIds(gradeIds);
        if (classEntryList == null) return null;

        List<ClassInfoDTO> classInfoDTOList = new ArrayList<ClassInfoDTO>();
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();

        for (ClassEntry classEntry : classEntryList) {
            ClassInfoDTO classInfoDTO = new ClassInfoDTO();
            classInfoDTO.setClassName(classEntry.getName());
            classInfoDTO.setId(classEntry.getID().toString());
            classInfoDTO.setIntroduce(classEntry.getIntroduce());
            classInfoDTO.setStudentIds(classEntry.getStudents());
            classInfoDTO.setTeacherIds(classEntry.getTeachers());
            if (null != classEntry.getMaster()) {
                classInfoDTO.setMainTeacherId(classEntry.getMaster().toString());
                objectIdList.add(classEntry.getMaster());
            }

            classInfoDTOList.add(classInfoDTO);
        }
        List<UserEntry> userEntryList = userDao.getUserEntryList(objectIdList, Constant.FIELDS);
        Map<ObjectId, UserEntry> map = new HashMap<ObjectId, UserEntry>();
        for (UserEntry userEntry : userEntryList) {
            map.put(userEntry.getID(), userEntry);
        }
        for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
            String teacherId = classInfoDTO.getMainTeacherId();
            if (!StringUtils.isBlank(teacherId)) {
                UserEntry userEntry = map.get(new ObjectId(teacherId));
                if (userEntry != null) {
                    classInfoDTO.setMainTeacher(userEntry.getUserName());
                }
            }

        }
        return classInfoDTOList;
    }

    /**
     * 查找班主任所在的行政班
     *
     * @param masterId
     * @return
     * @author shanchao
     */
    public List<ClassEntry> findClassByMasterId(ObjectId masterId) {
        return classDao.findClassByMasterId(masterId);
    }


    /**
     * 查找学生对应的班级
     *
     * @param stuIds
     * @param
     * @return
     */
    public Map<ObjectId, ClassEntry> getClassEntryByStuIds(List<ObjectId> stuIds) {
        Map<ObjectId, ClassEntry> retMap = new HashMap<ObjectId, ClassEntry>();
        List<ClassEntry> cList = classDao.getClassEntryByStuIds(stuIds, new BasicDBObject("nm", 1).append("stus", 1).append("gid",1));
        for (ClassEntry ce : cList) {
            for (ObjectId stuId : ce.getStudents()) {
                retMap.put(stuId, ce);
            }
        }
        return retMap;
    }


    /**
     * 查询班级成员
     *
     * @param ui
     * @param
     * @return
     */
    public Map<ObjectId, UserEntry> getClassMembersByStudentId(ObjectId ui) {
        UserEntry ue = userDao.getUserEntry(ui, Constant.FIELDS);
        if (null != ue) {
            ObjectId searchUI = ue.getID();
            if (UserRole.isParent(ue.getRole())) {
                UserEntry parent = userDao.getUserEntry(ui, Constant.FIELDS);
                searchUI = parent.getConnectIds().get(0);
            }
            ClassEntry ce = classDao.getClassEntryByStuId(searchUI, Constant.FIELDS);
            Set<ObjectId> idset = new HashSet<ObjectId>();
            idset.addAll(ce.getStudents());
            idset.addAll(ce.getTeachers());

            Map<ObjectId, UserEntry> userMap = userDao.getUserEntryMap(idset, new BasicDBObject("nm", 1).append("r", 1));
            return userMap;
        }
        return new HashMap<ObjectId, UserEntry>();
    }


    /**
     * 班主任查看本班学生考勤
     *
     * @param classId
     * @param termType
     * @param schoolId
     * @param year
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getInterestClassAttendanceClassMaster(ObjectId classId, int termType, ObjectId schoolId, String year, int term) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        TermService termService = (TermService) SpringContextUtil.getBean("termService");
        int weekNum = termService.findPassedWeeks(schoolId.toString(), year, term);
        ClassEntry classEntry = classDao.getClassEntryById(classId, Constant.FIELDS);
        List<ObjectId> studentIds = classEntry.getStudents();
        if (studentIds.size() > 0) {
            if (termType < 0) {
                SchoolEntry schoolEntry = schoolDao.getSchoolEntry(schoolId, Constant.FIELDS);
                termType = schoolEntry.getTermType();
            }
            Map<ObjectId, UserEntry> userMap = userDao.getUserEntryMap(studentIds, new BasicDBObject("nm", 1));
            List<InterestClassEntry> interestClassEntryList =
                    interestClassDao.findClassBySchoolId(schoolId, termType, null, classEntry.getGradeId());
            if (interestClassEntryList.size() > 0) {
                Map<ObjectId, List<InterestClassEntry>> stuInterestClassMap = formateStuInterestClassMap(studentIds, interestClassEntryList, termType);
                for (Map.Entry<ObjectId, List<InterestClassEntry>> entry : stuInterestClassMap.entrySet()) {
                    ObjectId stuId = entry.getKey();
                    UserEntry userEntry = userMap.get(stuId);
                    if (userEntry == null) {
                        continue;
                    }
                    List<InterestClassEntry> interestClassEntries = entry.getValue();
                    for (InterestClassEntry interestClassEntry : interestClassEntries) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("stuNm", userEntry.getUserName());
                        map.put("classNm", interestClassEntry.getClassName());
                        List<String> strings = new ArrayList<String>();
                        for (int i = 1; i <= weekNum; i++) {
                            strings.add("");
                        }
                        List<InterestClassLessonScoreEntry> scores = interestClassDao.findStuLessonScore(interestClassEntry.getID(), stuId, termType);
                        Iterator<InterestClassLessonScoreEntry> iterator = scores.iterator();
                        while (iterator.hasNext()) {
                            InterestClassLessonScoreEntry scoreEntry = iterator.next();
                            String string = strings.get(scoreEntry.getWeekIndex() - 1);
                            String at = scoreEntry.getAttendance() == 1 ? "到" : "缺";
                            string += scoreEntry.getLessonName() + "(" + at + ")";
                            strings.set(scoreEntry.getWeekIndex() - 1, string);
                        }
                        map.put("weeks", strings);
                        list.add(map);
                    }
                }
            } else {
                throw new Exception("该学期没有拓展课");
            }
        } else {
            throw new Exception("该班级下没有学生");
        }

        return list;
    }

    private Map<ObjectId, List<InterestClassEntry>> formateStuInterestClassMap(List<ObjectId> studentIds, List<InterestClassEntry> interestClassEntryList, int termType) {
        Map<ObjectId, List<InterestClassEntry>> map = new HashMap<ObjectId, List<InterestClassEntry>>();
        for (InterestClassEntry interestClassEntry : interestClassEntryList) {
            List<InterestClassStudent> interestClassStudents = interestClassEntry.getInterestClassStudentsByTermType(termType);
            for (InterestClassStudent interestClassStudent : interestClassStudents) {
                ObjectId stuId = interestClassStudent.getStudentId();
                if (studentIds.contains(stuId)) {
                    List<InterestClassEntry> list = map.get(stuId);
                    if (list != null) {
                        list.add(interestClassEntry);
                    } else {
                        list = new ArrayList<InterestClassEntry>();
                        list.add(interestClassEntry);
                        map.put(stuId, list);
                    }
                }

            }
        }
        return map;
    }

    public void exportExcel(ObjectId schoolId, HttpServletResponse response, int termType) {
        //获取全校老师、校长、管理员列表
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(schoolId, Constant.FIELDS);

        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("期末总评");
        //为sheet1生成第一行，用于放表头信息
        List<String> title = new ArrayList<String>();
        title.add("年级");
        title.add("班级");
        title.add("学生姓名");
        title.add("课程名称");
        title.add("老师评语");
        title.add("考勤");
        title.add("日常评价");
        title.add("总体评价");
        appendRow(sheet, 0, title.toArray());


        List<ClassEntry> classEntries = classDao.findClassInfoBySchoolId(schoolId, Constant.FIELDS);
        Map<String, ClassEntry> stuClassEntryMap = new HashMap<String, ClassEntry>();
        for (ClassEntry classEntry : classEntries) {
            List<ObjectId> stus = classEntry.getStudents();
            for (ObjectId stu : stus) {
                stuClassEntryMap.put(stu.toString(), classEntry);
            }

        }
        List<Grade> grades = schoolEntry.getGradeList();
        Map<ObjectId, Grade> gradeMap = new HashMap<ObjectId, Grade>();
        for (Grade grade : grades) {
            gradeMap.put(grade.getGradeId(), grade);
        }

        int rowNo = 1;

        SchoolService schoolService = (SchoolService) SpringContextUtil.getBean("schoolService");
        List<InterestClassDTO> interestClassEntries = schoolService.findAllInterestClass(schoolId.toString(), termType, "", "allCategory");
        MyClassController myClassController = (MyClassController) SpringContextUtil.getBean("myClassController");
        for (InterestClassDTO interestClassDTO : interestClassEntries) {
            Map<String, Object> map = myClassController.finalAssessmentForApp(interestClassDTO.getId(), termType);
            List<InterestClassTranscriptDTO> transcriptDTOs = (List<InterestClassTranscriptDTO>) map.get("transcriptList");
            for (InterestClassTranscriptDTO transcriptDTO : transcriptDTOs) {
                List<String> datas = new ArrayList<String>();
                ClassEntry classEntry = stuClassEntryMap.get(transcriptDTO.getUserid());
                if (classEntry == null) {
                    continue;
                }
                Grade grade = gradeMap.get(classEntry.getGradeId());
                datas.add(grade.getName());
                datas.add(classEntry.getName());
                datas.add(transcriptDTO.getUserName());
                datas.add(interestClassDTO.getClassName());
                datas.add(transcriptDTO.getTeachercomments());
                datas.add(transcriptDTO.getAttendance());
                datas.add(buildScoreInfo(transcriptDTO.getSemesterscore()));
                datas.add(buildScoreInfo(transcriptDTO.getFinalresult()));
                appendRow(sheet, rowNo, datas.toArray());
                rowNo++;
            }
        }


        outPutWorkBook(wb, response, schoolEntry.getName());

    }

    private String buildScoreInfo(int score) {
        if (score >= 5) {
            return "优秀";
        } else if (score == 4) {
            return "良好";
        } else if (score == 3) {
            return "及格";
        } else {
            return "需努力";
        }
    }

    private void appendRow(HSSFSheet sheet, int rowNum, Object... data) {
        HSSFRow row = sheet.createRow(rowNum);
        for (int i = 0, j = data.length; i < j; i++) {
            Cell cell = row.createCell(i);
            sheet.setColumnWidth(i, 3200);
            cell.setCellValue(data[i] == null ? "" : data[i].toString());
        }
    }

    private void outPutWorkBook(HSSFWorkbook wb, HttpServletResponse response, String schoolName) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] content = os.toByteArray();
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(schoolName + "拓展课学生总评.xls", "UTF-8"));
            response.setContentLength(content.length);
            outputStream.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                    outputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 按周查看拓展课考勤
     *
     * @param schoolId
     * @param termType
     * @param gradeId
     * @param categoryId
     * @param weekIndex
     * @return
     */
    public List<Map<String, Object>> getAttendanceByWeekIndex(ObjectId schoolId, int termType, String gradeId, String categoryId, int weekIndex) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(schoolId, Constant.FIELDS);
        ObjectId grade = null;
        if (!"".equals(gradeId)) {
            grade = new ObjectId(gradeId);
        }
        if ("allCategory".equals(categoryId)) {
            categoryId = null;
        }
        if (termType < 0) {
            termType = schoolEntry.getTermType();
        }
        List<InterestClassEntry> interestClassEntryList =
                interestClassDao.findClassBySchoolId(schoolEntry.getID(), termType, categoryId, grade);
        List<ObjectId> teachers = new ArrayList<ObjectId>();
        for (InterestClassEntry interestClassEntry : interestClassEntryList) {
            teachers.add(interestClassEntry.getTeacherId());
        }
        Map<ObjectId, UserEntry> userEntryMap = userDao.getUserEntryMap(teachers, new BasicDBObject("nm", 1));

        for (InterestClassEntry interestClassEntry : interestClassEntryList) {
            UserEntry teacher = userEntryMap.get(interestClassEntry.getTeacherId());
            if (teacher == null) {
                continue;
            }
            List<InterestClassLessonScoreEntry> lessonScoreEntries = interestClassDao.findLessonScoreEntry(interestClassEntry.getID(), termType, weekIndex);
            if (lessonScoreEntries.size() > 0) {
                Map<Integer, List<InterestClassLessonScoreEntry>> weekLessonScoresMap = buildWeekIndexAndLessonScoresMap(lessonScoreEntries);
                for (Map.Entry<Integer, List<InterestClassLessonScoreEntry>> entry : weekLessonScoresMap.entrySet()) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("cNm", interestClassEntry.getClassName());
                    map.put("tNm", teacher.getUserName());
                    map.put("at", getAttendance(entry.getValue()));
                    map.put("li", entry.getKey());
                    map.put("lNm", entry.getValue().get(0).getLessonName());
                    map.put("cRoom", interestClassEntry.getRoom());
                    list.add(map);
                }
            }

        }

        return list;
    }

    private Map<Integer, List<InterestClassLessonScoreEntry>> buildWeekIndexAndLessonScoresMap(List<InterestClassLessonScoreEntry> lessonScoreEntries) {
        Map<Integer, List<InterestClassLessonScoreEntry>> map = new LinkedHashMap<Integer, List<InterestClassLessonScoreEntry>>();
        for (InterestClassLessonScoreEntry lessonScoreEntry : lessonScoreEntries) {
            Integer lessonIndex = lessonScoreEntry.getLessonIndex();
            List<InterestClassLessonScoreEntry> entries = map.get(lessonIndex);
            if (entries == null) {
                entries = new ArrayList<InterestClassLessonScoreEntry>();
            }
            entries.add(lessonScoreEntry);
            map.put(lessonIndex, entries);
        }
        return map;
    }

    private String getAttendance(List<InterestClassLessonScoreEntry> scoreEntries) {
        int at = 0;
        for (InterestClassLessonScoreEntry entry : scoreEntries) {
            if (entry.getAttendance() == 1) {
                at++;
            }
        }
        return at + "/" + scoreEntries.size();
    }

    public List<GradeView> getSimpleGradeViews(ObjectId teacherId, ObjectId schoolId) {
        List<GradeView> gradeList = new ArrayList<GradeView>();
        List<ClassEntry> classEntryList = classDao.getClassEntryByTeacherId(teacherId, Constant.FIELDS);

        SchoolEntry se = schoolDao.getSchoolEntry(schoolId, Constant.FIELDS);
        List<String> gradeIdList = new ArrayList<String>();
        if (se != null) {
            Map<ObjectId, Grade> gradeMap = new HashMap<ObjectId, Grade>();
            for (Grade g : se.getGradeList()) {
                gradeMap.put(g.getGradeId(), g);
            }
            GradeView dto = null;
            Grade g = null;
            for (ClassEntry ce : classEntryList) {
                dto = new GradeView();
                g = gradeMap.get(ce.getGradeId());
                if (null != g) {
                    dto.setId(g.getGradeId().toString());
                    dto.setName(g.getName());
                }
                if (!gradeIdList.contains(dto.getId())) {
                    gradeList.add(dto);
                    gradeIdList.add(g.getGradeId().toString());
                }
            }
        }
        return gradeList;
    }

    public Map<ObjectId, ClassEntry> getClassEntryMapByGradeId(ObjectId gradeId, DBObject fields) {
        return classDao.getClassEntryMapByGradeId(gradeId, fields);
    }

    public List<ClassEntry> getClassEntryByParam(ObjectId gradeId, List<ObjectId> noIds, DBObject fields) {
        return classDao.getClassEntryByParam(gradeId, noIds, fields);
    }


    /**
     * 查找老师所带的行政班、兴趣班、教学班
     *
     * @param teacherId
     */
    public List<IdNamePair> getAllClassByTeacherId(ObjectId teacherId, ObjectId schoolId) {
        List<IdNamePair> classes = new ArrayList<IdNamePair>();
        //行政班
        List<ClassEntry> classEntries = classDao.getClassEntryByTeacherId(teacherId, new BasicDBObject("nm", 1));
        if (classEntries.size() > 0) {
            for (ClassEntry classEntry : classEntries) {
                classes.add(new IdNamePair(classEntry.getID(), classEntry.getName()));
            }
        }
        //兴趣班
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(schoolId, new BasicDBObject("tt", 1));
        List<InterestClassEntry> interestClassEntries = interestClassDao.findClassInfoByTeacherId(teacherId, schoolEntry.getTermType());
        if (interestClassEntries.size() > 0) {
            for (InterestClassEntry classEntry : interestClassEntries) {
                classes.add(new IdNamePair(classEntry.getID(), classEntry.getClassName()));
            }
        }
        //教学班
        List<ZouBanCourseEntry> zouBanCourseEntries = zouBanCourseDao.findCourseListByTeacherId(TermUtil.getSchoolYear(), teacherId, false);
        if (zouBanCourseEntries.size() > 0) {
            for (ZouBanCourseEntry courseEntry : zouBanCourseEntries) {
                classes.add(new IdNamePair(courseEntry.getID(), courseEntry.getClassName()));
            }
        }
        return classes;
    }

    /**
     * 查找学生所在的行政班、兴趣班、教学班
     *
     * @param studentId
     */
    public List<IdNamePair> getAllClassByStudentId(ObjectId studentId, ObjectId schoolId) {
        List<IdNamePair> classes = new ArrayList<IdNamePair>();
        //行政班
        ClassEntry classEntry = classDao.getClassEntryByStuId(studentId, new BasicDBObject("nm", 1).append("gid", 1));
        if (classEntry != null) {
            classes.add(new IdNamePair(classEntry.getID(), classEntry.getName()));
        }
        //兴趣班
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(schoolId, new BasicDBObject("tt", 1));
        List<InterestClassDTO> interestClassDTOs = interestClassService.findInterestClassByStudentId(studentId.toString(), schoolEntry.getTermType());
        if (interestClassDTOs.size() > 0) {
            for (InterestClassDTO interestClassDTO : interestClassDTOs) {
                classes.add(new IdNamePair(new ObjectId(interestClassDTO.getId()), interestClassDTO.getClassName()));
            }
        }

        //教学班
        if (classEntry != null) {
            List<ZouBanCourseEntry> zouBanCourseEntries = zouBanCourseDao.getStudentChooseZB(studentId, TermUtil.getSchoolYear(), classEntry.getGradeId());
            if (zouBanCourseEntries.size() > 0) {
                for (ZouBanCourseEntry courseEntry : zouBanCourseEntries) {
                    classes.add(new IdNamePair(courseEntry.getID(), courseEntry.getClassName()));
                }
            }
        }

        return classes;
    }

    //service end
}
