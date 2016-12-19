package com.fulaan.school;

import com.db.app.RegionDao;
import com.db.interestCategory.InterestCategoryDao;
import com.db.school.*;
import com.db.user.UserDao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.RegionEntry;
import com.pojo.app.SimpleDTO;
import com.pojo.interestCategory.InterestCategoryEntry;
import com.pojo.school.*;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.exceptions.IllegalParamException;
import com.sys.utils.MD5Utils;
import com.sys.utils.ValidationUtils;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * Created by hao on 2015/2/27.
 */
@Service
public class SchoolService {

    private static final Logger logger = Logger.getLogger(SchoolService.class);

    private SchoolDao schoolDao = new SchoolDao();

    private ClassDao classDao = new ClassDao();

    private UserDao userDao = new UserDao();

    private RegionDao regionDao = new RegionDao();

    private TeacherDao teacherDao = new TeacherDao();

    private InterestClassDao interestClassDao = new InterestClassDao();

    private TeacherClassSubjectDao teacherClassSubjectDao = new TeacherClassSubjectDao();

    private DepartmentDao departmentDao = new DepartmentDao();

    private InterestCategoryDao interestCategoryDao = new InterestCategoryDao();


    /**
     * 根据学校名和初始密码获取学校，用于导出学校成员列表
     *
     * @param name
     * @return
     */
    public SchoolEntry getSchoolEntry(String name, String initPwd) {
        return schoolDao.getSchoolEntry(name, initPwd);
    }


    /**
     * 添加一个学校
     *
     * @param e
     * @return
     */
    public ObjectId addSchoolEntry(SchoolEntry e) {
        return schoolDao.addSchoolEntry(e);
    }

    /**
     * 根据学校ID查询学校
     *
     * @param id
     * @return
     */
    public SchoolEntry getSchoolEntry(ObjectId id, DBObject fields) {
        return schoolDao.getSchoolEntry(id, fields);
    }


    /**
     * 得到学生年级信息
     *
     * @param studentId
     * @return
     */
    public Grade getStudentGrade(ObjectId studentId) {
        ClassEntry e = classDao.getClassEntryByStuId(studentId, Constant.FIELDS);
        if (null != e) {
            SchoolEntry se = schoolDao.getSchoolEntry(e.getSchoolId(), Constant.FIELDS);
            if (null != se.getGradeList()) {
                for (Grade g : se.getGradeList()) {
                    if (e.getGradeId().equals(g.getGradeId())) {
                        return g;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 得到老师年级
     *
     * @param teacherId
     * @return
     * @throws IllegalParamException
     */
    public List<Grade> getTeacherGrades(ObjectId teacherId) throws IllegalParamException {
        UserEntry ue = userDao.getUserEntry(teacherId, new BasicDBObject("si", Constant.ONE));
        if (null == ue) {
            throw new IllegalParamException();
        }
        SchoolEntry se = schoolDao.getSchoolEntry(ue.getSchoolID(), Constant.FIELDS);
        if (null == se) {
            throw new IllegalParamException();
        }
        return se.getGradeList();
    }


    /**
     * 通过用户ID查询用户的学校
     *
     * @param userId
     * @return
     * @throws IllegalParamException
     */
    public SchoolEntry getSchoolEntryByUserId(ObjectId userId) throws IllegalParamException {
        UserEntry userEntry = userDao.getUserEntry(userId, new BasicDBObject().append("si", 1));
        if (null == userEntry) {
            throw new IllegalParamException("Can not find UserEntry for user:" + userId.toString());
        }
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(userEntry.getSchoolID(), Constant.FIELDS);
        return schoolEntry;
    }


    /**
     * 给一个年级增加组长
     *
     * @param schoolId 学校ID
     * @param gradeId  年级ID
     * @param leaderId 组长ID
     */
    public void addGradeLeader(ObjectId schoolId, ObjectId gradeId, ObjectId leaderId) {
        schoolDao.addGradeLeader(schoolId, gradeId, leaderId);
    }

    /**
     * 修改某一个年级的班级
     *
     * @param id
     * @param subjectId
     * @param gradeids
     */
    public void updateGradeForSubject(ObjectId id, ObjectId subjectId, List<ObjectId> gradeids) {
        schoolDao.updateGradeForSubject(id, subjectId, gradeids);
    }

    /**
     * 部门详情
     *
     * @param id
     * @return
     */
    public DepartmentEntry getDepartmentEntry(ObjectId id) {
        return departmentDao.getDepartmentEntry(id);
    }


    /**
     * 根据部门文件查询部门
     *
     * @param fileId
     * @return
     */
    public DepartmentEntry getDepartmentEntryByFileId(ObjectId fileId) {
        return departmentDao.getDepartmentEntryByFileId(fileId);
    }


    /**
     * 得到学校的部门
     *
     * @param schoolId
     * @return
     */
    public List<SimpleDTO> getDepartments(ObjectId schoolId) {
        List<SimpleDTO> retList = new ArrayList<SimpleDTO>();
        List<DepartmentEntry> dboList = departmentDao.getDepartmentEntrys(schoolId);
        for (DepartmentEntry e : dboList) {
            retList.add(new SimpleDTO(e));
        }
        return retList;
    }

    /**
     * 添加学校的部门
     *
     * @param schoolId
     * @param name
     */
    public ObjectId addDepartment(ObjectId schoolId, String name, String des) {
        DepartmentEntry e = new DepartmentEntry(schoolId, name, des, null, null);
        return departmentDao.addDepartmentEntry(e);
    }


    /**
     * 删除部门
     */
    public void removeDepartment(ObjectId id) {
        departmentDao.removeDepartmentEntry(id);
    }

    /**
     * 部门添加成员
     */
    public void addMember(ObjectId id, ObjectId userID) {
        departmentDao.addMember(id, userID);
    }

    /**
     * 删除成员
     *
     * @param id
     * @param userID
     */
    public void removeMember(ObjectId id, ObjectId userID) {
        departmentDao.deleteMember(id, userID);
    }


    /**
     * 根据ID查询地域信息
     *
     * @param regionId
     * @return
     */
    public RegionEntry getRegionEntry(ObjectId regionId) {
        return regionDao.getRegionById(regionId);
    }


    public String findSchoolNameByUserId(String userId) {
        UserEntry userEntry = userDao.getUserEntry(new ObjectId(userId), new BasicDBObject().append("si", 1));
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(userEntry.getSchoolID(), Constant.FIELDS);
        return schoolEntry.getName();
    }

    public String findCityNameByUserId(String userId) {
        UserEntry userEntry = userDao.getUserEntry(new ObjectId(userId), new BasicDBObject().append("si", 1));
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(userEntry.getSchoolID(), Constant.FIELDS);
        RegionEntry regionEntry = regionDao.getRegionById(schoolEntry.getRegionId());
        return regionEntry.getName();
    }

    public String findAddressById(String schoolid) {
        SchoolEntry school = schoolDao.getSchoolEntry(new ObjectId(schoolid), Constant.FIELDS);
        RegionEntry regionEntry = regionDao.getRegionById(school.getRegionId());
        RegionEntry region = regionDao.getRegionById(regionEntry.getParentId());
        String province = region.getName() == null ? "" : region
                .getName();
        String city = regionEntry.getName() == null ? "" : regionEntry.getName();
        String name = school.getName() == null ? "" : school.getName();
        return province + " " + city + " " + name;
    }

    /*
    *
    * 主键查询学校
    *
    *
    * */
    public SchoolDTO findSchoolById(String schoolID) {
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(new ObjectId(schoolID), Constant.FIELDS);
        SchoolDTO schoolDTO = new SchoolDTO(schoolEntry);
        return schoolDTO;
    }

    /*
    * 更新科目名称和年级
    *
    * */
    public boolean updateSubjectNameAndGrade(String subjectId, String schoolId, String newSubjectName, String gradeArray) {
        ObjectId schoolID = new ObjectId(schoolId);
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(schoolID, Constant.FIELDS);
        List<Subject> subjectList = schoolEntry.getSubjects();
        Subject sub = null;
        if (subjectList != null) {
            for (Subject subject : subjectList) {
                if (subject.getSubjectId().toString().equals(subjectId)) {
                    sub = subject;
                    break;
                }
            }
        }
        //deleteSubject(schoolId,subjectId);
        //因为已经有老师选择该科目，故用deleteSubject方法无法删除
        deleteSubjectUnJudge(schoolId, subjectId);
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        String[] ids = gradeArray.split(",");
        for (int i = 0; i < ids.length; i++) {
            ObjectId gradeId = new ObjectId(ids[i]);
            objectIdList.add(gradeId);
        }
        sub.setName(newSubjectName);
        sub.setGradeIds(objectIdList);
        addSubject2School(schoolID, sub);
        return true;
    }

    /*
    *
    * 为学校添加科目
    *
    * */
    public void addSubject2School(ObjectId schoolId, Subject sub) {
        //  schoolDao.updateSubject(schoolId, sub);
        schoolDao.addSubject(schoolId, sub);
    }

    /*
    *
    * 根据 年级 科目名称 创建该科目并添加到学校
    *
    * */
    public boolean addSubject2School(String schoolId, String gradeIds, String subjectName) {
        Subject subject = new Subject(new BasicDBObject());
        subject.setSubjectId(new ObjectId());
        subject.setName(subjectName);

        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        String[] ids = gradeIds.split(",");
        for (int i = 0; i < ids.length; i++) {
            ObjectId gradeId = new ObjectId(ids[i]);
            objectIdList.add(gradeId);
        }
        subject.setGradeIds(objectIdList);
        schoolDao.addSubject(new ObjectId(schoolId), subject);
        return true;
    }

    /**
     * 直接删除学科，不判断是否有老师已经选择，用于修改学科时，删除原有的，继而添加新的，id不变
     * add by miaoqiang
     *
     * @param schoolId
     * @param subjectId
     * @return
     */
    public Map<String, Object> deleteSubjectUnJudge(String schoolId, String subjectId) {
        Map<String, Object> map = new HashMap<String, Object>();
        Subject subject = new Subject(new BasicDBObject());
        subject.setSubjectId(new ObjectId(subjectId));
        schoolDao.deleteSubject(new ObjectId(schoolId), subject);
        map.put("result", true);
        return map;
    }

    /*
    *
    *删除学校科目
    * */
    public Map<String, Object> deleteSubject(String schoolId, String subjectId) {
        Map<String, Object> map = new HashMap<String, Object>();
        //先检查是否已经有老师选了该科目，已选科目则不可删除
        List<TeacherClassSubjectEntry> subjectEntryList = teacherClassSubjectDao.findTeacherClassSubjectBySubjectId(new ObjectId(subjectId));
        if (subjectEntryList.size() == 0) {
            Subject subject = new Subject(new BasicDBObject());
            subject.setSubjectId(new ObjectId(subjectId));
            schoolDao.deleteSubject(new ObjectId(schoolId), subject);
            map.put("result", true);
            return map;
        } else {
            List<String> result = new ArrayList<String>();
            List<ObjectId> classList = new ArrayList<ObjectId>();
            for (TeacherClassSubjectEntry tcsubject : subjectEntryList) {
                classList.add(tcsubject.getClassInfo().getId());
            }

            Map<ObjectId, ClassEntry> classEntryMap = classDao.getClassEntryMap(classList, Constant.FIELDS);


            SchoolEntry schoolEntry = schoolDao.getSchoolEntry(new ObjectId(schoolId), Constant.FIELDS);//获取当前学校

            for (Map.Entry<ObjectId, ClassEntry> entry : classEntryMap.entrySet()) {
                for (Grade grade : schoolEntry.getGradeList()) {
                    if (grade.getGradeId().equals(entry.getValue().getGradeId())) {
                        result.add(grade.getName() + entry.getValue().getName());
                        break;
                    }
                }
            }

            map.put("result", false);
            map.put("reason", result);
            return map;
        }
    }

    /*
    * 添加老师
    *
    * */
    public String addTeacher(String schoolId, String teacherName, String jobNum, String permission) {
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(new ObjectId(schoolId), Constant.FIELDS);
        String pwd = schoolEntry.getInitialPassword();
        String k = teacherDao.addTeacher(new ObjectId(schoolId), teacherName, jobNum, permission, pwd);
        return k;
    }

    /*
    *
    * 通过主键 更新老师信息
    * */
    public boolean updateTeacher(String teacherId, String teacherName, String jobNum, String permission) {
        UserEntry user = userDao.getUserEntry(new ObjectId(teacherId), Constant.FIELDS);
        int role = user.getRole();
        String roleStr = "";
        //如果原来是校长或老师，修改为校长，则增加角色(modify by miaoqiang)
        if ((UserRole.isHeadmaster(role) || UserRole.isTeacher(role)) && (Integer.parseInt(permission) == 8 ||
                Integer.parseInt(permission) == 72)) {
            role = Integer.parseInt(permission) | role;
            roleStr = Integer.toString(role);
        } else {
            roleStr = permission;
        }
        boolean k = teacherDao.updateTeacher(new ObjectId(teacherId), teacherName, jobNum, roleStr);
        return k;
    }

    public void updateTeacherRole(String teacherId, String teacherName, String jobNum, String permission, int ismanage) {
        UserEntry user = userDao.getUserEntry(new ObjectId(teacherId), Constant.FIELDS);
        int role = user.getRole();
        String roleStr = "";
        int int_permission = Integer.parseInt(permission);
        if (UserRole.isTeacher(int_permission)) {
            if (ismanage == 1) {
                int_permission += UserRole.ADMIN.getRole();
            }
        }

        //如果原来是校长或老师，修改为校长，则增加角色(modify by miaoqiang)
        if (UserRole.isTeacher(role) && int_permission == 8) {
            role = int_permission | UserRole.TEACHER.getRole();
            roleStr = Integer.toString(role);
        } else {
            roleStr = Integer.toString(int_permission);
        }
        //roleStr = Integer.toString(int_permission);
        teacherDao.updateTeacher(new ObjectId(teacherId), teacherName, jobNum, roleStr);
    }

    /*
    *
    * 老师名称搜索并分页
    *
    * */
    public List<UserDetailInfoDTO> teacherList(String schoolId, String keyWord, int skip, int size) {
        List<UserDetailInfoDTO> userInfoDTOList = teacherDao.teacherList(new ObjectId(schoolId), keyWord, skip, size);
        return userInfoDTOList;
    }

    /*
    *
    * 初始化密码
    *
    *
    * */
    public boolean initPwdOfTeacher(String userId, String schoolID) {
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(new ObjectId(schoolID), Constant.FIELDS);
        boolean k = teacherDao.updatePwd(new ObjectId(userId), MD5Utils.getMD5String(schoolEntry.getInitialPassword()));
        return k;
    }

    /*
    *
    * 更新学校初始密码
    *
    * */
    public boolean updateNewInitPwd(String schoolID, String newPwd) {
        schoolDao.update(new ObjectId(schoolID), "inp", newPwd);
        return true;
    }


    public void update(ObjectId id, String field, Object value) {
        schoolDao.update(id, field, value);
    }

    /*
    *
    * 添加一个年级
    *
    * */
    public void addGrade(String schoolId, Grade grade) {
        schoolDao.addGrade(new ObjectId(schoolId), grade);
    }

    /*
    *
    * 通过学校id 更新年级信息
    *
    * */
    public void updateGradeById(String schoolId, Grade grade) {
        schoolDao.updateById(new ObjectId(schoolId), grade);
    }

    /*
    *
    * 删除某个学校的年级
    *
    * */
    public void deleteGradeById(String schoolId, String gradeId) {
        schoolDao.deleteGradeById(new ObjectId(schoolId), new ObjectId(gradeId));
    }

    /*
    *
    * 更新学校名称 和等级
    *
    * */
    public void updateSchoolNameAndLevel(String schoolId, String schoolName, int num) {
        schoolDao.updateSchoolNameAndLevel(new ObjectId(schoolId), schoolName, num);
    }

    //查找当前学校所有兴趣班
    public List<InterestClassDTO> findAllInterestClass(String schoolId, int termType, String gradeId, String categoryId) {
        //构建兴趣班分类map
        List<InterestCategoryEntry> interestCategoryEntryList = interestCategoryDao.findInterestCategory(new ObjectId(schoolId));
        Map<String, String> typeMap = new HashMap<String, String>();
        if (interestCategoryEntryList != null && interestCategoryEntryList.size() > 0) {
            for (InterestCategoryEntry entry : interestCategoryEntryList) {
                typeMap.put(entry.getID().toString(), entry.getName());
            }
        }

        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(new ObjectId(schoolId), Constant.FIELDS);
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
        List<InterestClassDTO> classInfoDTOList = new ArrayList<InterestClassDTO>();
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        for (InterestClassEntry interestClassEntry : interestClassEntryList) {
            try {
                InterestClassDTO interestClassDTO = new InterestClassDTO(interestClassEntry);
                interestClassDTO.setStudentList(interestClassDTO.getTermStudentList(termType));
                String typeName = "未分类";
                if (typeMap.get(interestClassDTO.getTypeId()) != null) {
                    typeName = typeMap.get(interestClassDTO.getTypeId());
                }
                interestClassDTO.setTypeName(typeName);
                classInfoDTOList.add(interestClassDTO);
                userIds.add(interestClassEntry.getTeacherId());
            } catch (Exception ex) {
                logger.error("", ex);
            }
        }
        /*构建userMap  用于注入userName属性*/
        Map<ObjectId, UserEntry> userMap = userDao.getUserEntryMap(userIds, Constant.FIELDS);
        /*构建lessonCountMap  用于构建课程数量属性*/
//        List<TeacherClassSubjectEntry> tcsList = teacherClassSubjectDao.findSubjectByTeacherIds(userIds);
//        Map<ObjectId, Integer> countMap = new HashMap<ObjectId, Integer>();
//        for (TeacherClassSubjectEntry tcl : tcsList) {
//            Integer count = countMap.get(tcl.getTeacherId());
//            if (count == null) {
//                count = 0;
//            }
//            countMap.put(tcl.getTeacherId(), count + 1);
//        }


        for (InterestClassDTO interestClassDTO : classInfoDTOList) {
            if (ObjectId.isValid(interestClassDTO.getTeacherId())) {
                UserEntry userEntry = userMap.get(new ObjectId(interestClassDTO.getTeacherId()));
                if (userEntry != null) {
                    interestClassDTO.setTeacherName(userEntry.getUserName());
                    interestClassDTO.setTeacherAvatar("http://7xiclj.com1.z0.glb.clouddn.com/" + userEntry.getAvatar());
                }

                Integer lessonCount = getLessonCount(new ObjectId(interestClassDTO.getId()), termType);
                interestClassDTO.setLessonCount(lessonCount == null ? 0 : lessonCount);
            }
            interestClassDTO.setStudentCount(interestClassDTO.getStudentList().size());
        }
        return classInfoDTOList;
    }

    private int getLessonCount(ObjectId classId, int termType) {
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
        return lessonNum;
    }

    //查找当前老师所有兴趣班级
    public List<InterestClassDTO> findInterestClassByTeacherId(String teacherId, int termType, String schoolId) {

        //构建兴趣班分类map
        List<InterestCategoryEntry> interestCategoryEntryList = interestCategoryDao.findInterestCategory(new ObjectId(schoolId));
        Map<String, String> typeMap = new HashMap<String, String>();
        if (interestCategoryEntryList != null && interestCategoryEntryList.size() > 0) {
            for (InterestCategoryEntry entry : interestCategoryEntryList) {
                typeMap.put(entry.getID().toString(), entry.getName());
            }
        }

        UserEntry user = userDao.getUserEntry(new ObjectId(teacherId), Constant.FIELDS);
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(user.getSchoolID(), Constant.FIELDS);
        TeacherClassSubjectDao teacherClassSubjectDao = new TeacherClassSubjectDao();
        if (termType < 0) {
            termType = schoolEntry.getTermType();
        }

        List<InterestClassEntry> interestClassEntryList = interestClassDao.findClassInfoByTeacherId(new ObjectId(teacherId), termType);

        List<InterestClassDTO> classInfoDTOList = new ArrayList<InterestClassDTO>();
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        for (InterestClassEntry interestClassEntry : interestClassEntryList) {
            InterestClassDTO interestClassDTO = new InterestClassDTO(interestClassEntry);
            interestClassDTO.setStudentList(interestClassDTO.getTermStudentList(termType));
            String typeName = "未分类";
            if (typeMap.get(interestClassDTO.getTypeId()) != null) {
                typeName = typeMap.get(interestClassDTO.getTypeId());
            }
            interestClassDTO.setTypeName(typeName);
            classInfoDTOList.add(interestClassDTO);
            userIds.add(interestClassEntry.getTeacherId());
        }
        /*构建userMap  用于注入userName属性*/
        Map<ObjectId, UserEntry> userMap = userDao.getUserEntryMap(userIds, Constant.FIELDS);
        /*构建lessonCountMap  用于构建课程数量属性*/
//        List<TeacherClassSubjectEntry> tcsList = teacherClassSubjectDao.findSubjectByTeacherIds(userIds);
//        Map<ObjectId, Integer> countMap = new HashMap<ObjectId, Integer>();
//        for (TeacherClassSubjectEntry tcl : tcsList) {
//            Integer count = countMap.get(tcl.getTeacherId());
//            if (count == null) {
//                count = 0;
//            }
//            countMap.put(tcl.getTeacherId(), count + 1);
//        }

        for (InterestClassDTO interestClassDTO : classInfoDTOList) {
            UserEntry userEntry = userMap.get(new ObjectId(interestClassDTO.getTeacherId()));
            if (userEntry != null) {
                interestClassDTO.setTeacherName(userEntry.getUserName());
                interestClassDTO.setTeacherAvatar("http://7xiclj.com1.z0.glb.clouddn.com/" + userEntry.getAvatar());
            }
            Integer lessonCount = getLessonCount(new ObjectId(interestClassDTO.getId()), termType);
            interestClassDTO.setLessonCount(lessonCount == null ? 0 : lessonCount);
            interestClassDTO.setStudentCount(interestClassDTO.getStudentList().size());
        }
        return classInfoDTOList;
    }

    /*
    * 依据 学校id 和 科目Id 查找科目信息
    *
    * */
//    public SubjectView findSubjectBySchoolIdAndSubId(String schoolId, String subjectId) {
//        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(new ObjectId(schoolId),Constant.FIELDS);
//        List<Subject> subjectList = schoolEntry.getSubjects();
//        Subject sub = null;
//        if (subjectList != null) {
//            for (Subject subject : subjectList) {
//                if (subject.getSubjectId().toString().equals(subjectId)) {
//                    sub = subject;
//                    break;
//                }
//            }
//        }
//        if (sub != null) {
//            return new SubjectView(sub);
//        }
//        return null;
//    }

    /*
    *
    * 查询年级的科目信息
    *
    * */
//    public List<SubjectView> findSubjectListBySchoolIdAndGradeId(String schoolId, String gradeId) {
//        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(new ObjectId(schoolId),Constant.FIELDS);
//        List<Subject> subjectList = schoolEntry.getSubjects();
//        List<SubjectView> subjectViewList = new ArrayList<SubjectView>();
//        if (subjectList != null) {
//            for (Subject subject : subjectList) {
//                List<ObjectId> gradIds = subject.getGradeIds();
//                if (gradIds != null) {
//                    for (ObjectId gid : gradIds) {
//                        if (gid != null && gid.toString().equals(gradeId)) {
//                            SubjectView subjectView = new SubjectView();
//                            subjectView.setId(subject.getSubjectId().toString());
//                            subjectView.setName(subject.getName());
//                            subjectViewList.add(subjectView);
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//        return subjectViewList;
//    }
//
//    public Map<String, SubjectView> findSubjectViewMapBySchoolIdAndGradeId(String schoolId, String gradeId) {
//        Map<String, SubjectView> map = new HashMap<String, SubjectView>();
//        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(new ObjectId(schoolId),Constant.FIELDS);
//        List<Subject> subjectList = schoolEntry.getSubjects();
//        if (subjectList != null) {
//            for (Subject subject : subjectList) {
//                List<ObjectId> gradIds = subject.getGradeIds();
//                if (gradIds != null) {
//                    for (ObjectId gid : gradIds) {
//                        if (gid != null && gid.toString().equals(gradeId)) {
//                            SubjectView subjectView = new SubjectView();
//                            subjectView.setId(subject.getSubjectId().toString());
//                            subjectView.setName(subject.getName());
//                            map.put(subjectView.getId(), subjectView);
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//        return map;
//    }

    /*
    *
    *搜索学校年级列表
    *
    * */
//    public List<GradeView> searchSchoolGradeList(String schoolId) {
//        List<Grade> grades = schoolDao.findSchoolInfoBySchoolId(new ObjectId(schoolId));
//        List<GradeView> gradeViews = new ArrayList<GradeView>();
//        if (grades != null) {
//            for (Grade grade : grades) {
//                gradeViews.add(new GradeView(grade));
//            }
//        }
//        return gradeViews;
//    }
//
//    /*
//    *
//    * 统计学校老师数量 依据keyWord ==>userName
//    * */
//    public int countTeacher(String schoolId, String keyWord) {
//        int count = teacherDao.countTeacher(new ObjectId(schoolId), keyWord);
//        return count;
//    }

    /*
    *
    * 查询多个学校信息的list
    *
    * */
    public List<SchoolDTO> findSchoolInfoBySchoolIds(List<ObjectId> schoolIds) {
        List<SchoolDTO> list = new ArrayList<SchoolDTO>();
        List<SchoolEntry> SEList = schoolDao.getSchoolEntryList(schoolIds);
        for (SchoolEntry schoolEntry : SEList) {
            SchoolDTO schoolDTO = new SchoolDTO(schoolEntry);
            list.add(schoolDTO);
        }
        return list;
    }

    /**
     * 批量修改用户密码
     *
     * @param userIds
     * @param password
     */
    public boolean resetSelectPassword(List<ObjectId> userIds, String password) {
        boolean k = userDao.resetSelectPassword(userIds, password);
        return k;
    }


    /**
     * 通过用户ID查询用户的学校,不丢异常版
     *
     * @param userId
     * @return
     */
    public SchoolEntry getSchoolEntryByUserIdWithoutException(ObjectId userId) {
        UserEntry userEntry = userDao.getUserEntry(userId, new BasicDBObject().append("si", 1));
        if (null == userEntry) {
            return null;
        }
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(userEntry.getSchoolID(), Constant.FIELDS);
        return schoolEntry;
    }

    /**
     * 根据userid列表查询其城市name列表以及地区列表
     *
     * @param userIds
     * @return
     */
    public Map<String, List> findCityNameByUserIds(List<String> userIds) {
        List<ObjectId> userIdList = new ArrayList<ObjectId>();
        for (String str : userIds) {
            userIdList.add(new ObjectId(str));
        }
        List<UserEntry> userEntryList = userDao.getUserEntryList(userIdList, new BasicDBObject().append("si", 1));
        List<ObjectId> schoolIdList = new ArrayList<ObjectId>();//学校Idlist
        for (UserEntry userEntry : userEntryList) {
            schoolIdList.add(userEntry.getSchoolID());
        }
        List<SchoolEntry> schoolEntryList = schoolDao.getSchoolEntryList(schoolIdList);//学校列表
        List<ObjectId> regionIdList = new ArrayList<ObjectId>();
        for (SchoolEntry schoolEntry : schoolEntryList) {
            regionIdList.add(schoolEntry.getRegionId());
        }
        List<RegionEntry> regionEntryList = regionDao.getRegionEntryList(regionIdList);
        Map<String, List> map = new HashMap<String, List>();

        map.put("school", schoolEntryList);
        map.put("region", regionEntryList);
        return map;
    }

    public List<Grade> findSchoolInfoByParams(List<ObjectId> schoolIds, int gradeType) {
        List<Grade> list = schoolDao.findSchoolInfoByParams(schoolIds, gradeType);
        return list;
    }

    public List<SchoolEntry> getSchoolEntryListByRegionForEdu() {
        return schoolDao.getSchoolEntryListByRegionForEdu();
    }

    /**
     * 根据学校id获取学科
     *
     * @param schoolid
     * @return
     */
    public Map<ObjectId, Subject> getSubjectEntryMap(String schoolid) {
        Map<ObjectId, Subject> retMap = new HashMap<ObjectId, Subject>();
        SchoolDTO schoolDTO = findSchoolById(schoolid);
        List<Subject> subjects = schoolDTO.getSubjectList();
        if (subjects != null) {
            for (Subject subject : subjects) {
                retMap.put(subject.getSubjectId(), subject);
            }
        }
        return retMap;
    }

    /**
     * 根据学校名获取学校Ids
     *
     * @param name
     * @return
     */
    public List<ObjectId> getSchoolIdByNames(String name) {
        return schoolDao.getSchoolIdByNames(name);
    }

    public List<SchoolDTO> getSchoolEntryByRegion(Set<ObjectId> regionIds, Set<ObjectId> noschoolIds, String schoolName) {
        List<SchoolDTO> list = new ArrayList<SchoolDTO>();
        DBObject fields = new BasicDBObject("nm", Constant.ONE);
        List<SchoolEntry> SEList = schoolDao.getSchoolEntryByRegion(regionIds, noschoolIds, schoolName, fields);
        for (SchoolEntry schoolEntry : SEList) {
            SchoolDTO schoolDTO = new SchoolDTO();
            schoolDTO.setSchoolId(schoolEntry.getID().toString());
            schoolDTO.setSchoolName(schoolEntry.getName());
            list.add(schoolDTO);
        }
        return list;
    }

    /**
     * 根据学校id获取学科
     *
     * @param schoolidlist
     * @return
     */
    public Map<ObjectId, SchoolEntry> getSchoolEntryMap(List<ObjectId> schoolidlist) {
        Map<ObjectId, SchoolEntry> retMap = new HashMap<ObjectId, SchoolEntry>();
        List<SchoolEntry> schoolEntryList = schoolDao.getSchoolEntryList(schoolidlist);
        if (schoolEntryList != null) {
            for (SchoolEntry schoolEntry : schoolEntryList) {
                retMap.put(schoolEntry.getID(), schoolEntry);
            }
        }
        return retMap;
    }

    /**
     * 根据学校id 返回学校map
     *
     * @param schoolIds
     * @param fields
     * @return
     */
    public Map<ObjectId, SchoolEntry> getSchoolMap(Collection<ObjectId> schoolIds, DBObject fields) {
        return schoolDao.getSchoolMap(schoolIds, fields);
    }

    public List<SchoolEntry> getAllSchoolEntryList(DBObject fields) {
        return schoolDao.getAllSchoolEntryList(fields);
    }
}
