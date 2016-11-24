package com.fulaan.myschool.service;

import com.db.crontab.CrontabDao;
import com.db.interestCategory.InterestClassTermsDao;
import com.db.lesson.DirDao;
import com.db.school.InterestClassDao;
import com.db.school.SchoolDao;
import com.db.school.TeacherClassSubjectDao;
import com.db.user.UserDao;
import com.db.zouban.ZouBanCourseDao;
import com.fulaan.examresult.service.ExamResultService;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.utils.TermUtil;
import com.fulaan.zouban.dto.ZouBanCourseDTO;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pojo.app.IdNameValuePair;
import com.pojo.app.IdValuePair;
import com.pojo.crontab.CrontabEntry;
import com.pojo.lesson.DirEntry;
import com.pojo.lesson.DirType;
import com.pojo.school.*;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.utils.MongoUtils;
import com.pojo.zouban.ZouBanCourseEntry;
import com.sys.constants.Constant;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Hao on 2015/4/1.
 */
@Service
public class InterestClassService {

    private InterestClassDao interestClassDao =new InterestClassDao();
    private SchoolDao schoolDao=new SchoolDao();
    private UserDao userDao=new UserDao();
    private TeacherClassSubjectDao teacherClassSubjectDao=new TeacherClassSubjectDao();
    private DirDao dirDao = new DirDao();
    private ExamResultService examResultService = new ExamResultService();
    private InterestClassTermsDao interestClassTermsDao = new InterestClassTermsDao();
    private CrontabDao crontabDao = new CrontabDao();
    private ZouBanCourseDao zouBanCourseDao = new ZouBanCourseDao();//株洲专用


    /*
   *
   *  添加拓展课
   *
   * */
    public ObjectId addExpandClass(InterestClassDTO expandClass) {
        InterestClassEntry expandClassEntry=expandClass.exportEntry();
        String termName = examResultService.getCurrentTerm();
        List<IdNameValuePair> terms = new ArrayList<IdNameValuePair>();
        IdNameValuePair term = new IdNameValuePair(null,termName, expandClass.getTermType());
        terms.add(term);
        expandClassEntry.setTerm(terms);
        if(expandClassEntry.getInterestClassStudents()==null){
            expandClassEntry.setInterestClassStudents(null);
        }
        return interestClassDao.addExpandClass(expandClassEntry);
    }
    /*
    *
    * 更新扩展课信息
    *
    * */
    public void updateExpandClass(InterestClassDTO expandClass) {
//        InterestClassEntry expandClassEntry=expandClass.exportEntry();
        InterestClassEntry interestClassEntry = interestClassDao.findEntryByClassId(new ObjectId(expandClass.getId()));
        interestClassEntry.setClassName(expandClass.getClassName());
        interestClassEntry.setTeacherId(new ObjectId(expandClass.getTeacherId()));
        interestClassEntry.setOpenTime(expandClass.getOpenTime().getTime());
        interestClassEntry.setCloseTime(expandClass.getCloseTime().getTime());
        interestClassEntry.setGradeIds(MongoUtils.convertToObjectIdList(expandClass.getGradeList()));
        interestClassEntry.setTeacherId(new ObjectId(expandClass.getTeacherId()));
        interestClassEntry.setSubjectId(new ObjectId(expandClass.getSubjectId()));
        interestClassEntry.setTotalCount(expandClass.getTotalStudentCount());
        interestClassEntry.setClassContent(expandClass.getClassContent());
        interestClassEntry.setClassTime(expandClass.getClassTime());
        interestClassEntry.setRoom(expandClass.getRoom());
        interestClassEntry.setSex(expandClass.getSex());
        interestClassDao.updateExpandClass(interestClassEntry);
    }
    /*
    *
    * 删除拓展课
    *
    * */
    public void deleteExpandClass(String classid, int termType, ObjectId schoolId) {
        if(termType < 0){
            SchoolEntry schoolEntry = schoolDao.getSchoolEntry(schoolId, Constant.FIELDS);
            termType = schoolEntry.getTermType();
        }
        InterestClassEntry interestClassEntry = interestClassDao.findEntryByClassId(new ObjectId(classid));
        List<IdNameValuePair> terms = interestClassEntry.getTerm();
        List<IdNameValuePair> termsAfterDelete = new ArrayList<IdNameValuePair>();
        for(IdNameValuePair term : terms){
            if((Integer)term.getValue() != termType){
                termsAfterDelete.add(term);
            }
        }
        interestClassEntry.setTerm(termsAfterDelete);
        interestClassDao.updateExpandClass(interestClassEntry);

//        interestClassDao.deleteExpandClassById(new ObjectId(classid));
        //删除tcsubject中关系
        teacherClassSubjectDao.removeByClassId(new ObjectId(classid));
        //删除dir表中关系
    }

    /**
     * 删除tcsubject表和dir中的相关数据
     * @param classId
     * @param teacherId
     * @param subjectId
     */
    public  void deleteTcsubjectAndDir(ObjectId classId,ObjectId teacherId,ObjectId subjectId)
    {
        ObjectId tclid=teacherClassSubjectDao.getTcsubjectId(classId, teacherId, subjectId);
        if(tclid!=null)
        {
            teacherClassSubjectDao.deleteById(tclid);
            dirDao.deleteByOwnId(tclid);
        }
    }
    /*
    *
    * 更新扩展课的可用状态  开启或关闭
    *
    * */
    public int updateExpandClassState(String classId) {
        int state= interestClassDao.findState(new ObjectId(classId));
        InterestClassEntry interestClassEntry = interestClassDao.findEntryByClassId(new ObjectId(classId));
        if(0 == interestClassEntry.getIsLongCourse()){
            ObjectId relatedId = interestClassEntry.getRelationId();
            if(relatedId != null){
                if(state==1){
                    interestClassDao.updateExpandState(relatedId,0);
                }else {
                    interestClassDao.updateExpandState(relatedId,1);
                }
            }
        }
        if(state==1){
            interestClassDao.updateExpandState(new ObjectId(classId),0);
            return 0;
        }else {
            interestClassDao.updateExpandState(new ObjectId(classId),1);
            return 1;
        }
    }
    /*
    *
    * 拓展课中添加学生
    *
    * */
    public String addStudent2ExpandClass(String expandClassId, String studentId,int courseType, int dropState, Boolean isStudent) {
        InterestClassEntry interestClassEntry = interestClassDao.findEntryByClassId(new ObjectId(expandClassId));
        List<InterestClassStudent> studentList = interestClassEntry.getInterestClassStudents();
        List<InterestClassStudent> currentStudentList = interestClassEntry.getCurrentInterestClassStudents();
        String[] stuIds = studentId.split(",");
        boolean k=true;
        String message = "添加成功";
        List<String> existStu = new ArrayList<String>();
        if(studentList!=null){
            for(InterestClassStudent student : studentList){
                existStu.add(student.getStudentId().toString());
            }
        }
        List<String> currentExistStu = new ArrayList<String>();
        if(studentList!=null){
            for(InterestClassStudent student : currentStudentList){
                currentExistStu.add(student.getStudentId().toString());
            }
        }
        List<String> readyToAdd = new ArrayList<String>();
        for(String stuId : stuIds){
            if(!existStu.contains(stuId) && !isTimeConflict(stuId, interestClassEntry)){
                readyToAdd.add(stuId);
            } else if(existStu.contains(stuId)){
                if(isStudent) {
                    message = "学生历史已选过本课";
                } else if(!currentExistStu.contains(stuId)) {
                    readyToAdd.add(stuId);
                }
            } else {
                message = "学生已有课程与当前课程时间冲突";
            }
        }
        if(readyToAdd.size() <=0){
            k = false;
        }
        if(interestClassEntry.getCurrentInterestClassStudents().size() >= interestClassEntry.getTotalCount()){
            k = false;
            message = "本课程学生已满";
        }
        if(interestClassEntry.getCurrentInterestClassStudents().size() + readyToAdd.size() > interestClassEntry.getTotalCount()){
            k = false;
            message = "添加学生过多，已超过人数上限，请减少人数";
        }
        if(k){
            for(String stu : readyToAdd){
                interestClassDao.addStudent2ExpandClass(new ObjectId(expandClassId),new ObjectId(stu),courseType, interestClassEntry.getTermType(), dropState);
            }
        }
        return message;
    }

    private Boolean isTimeConflict(String userId, InterestClassEntry interestClassEntry){
        List<String> timeList = interestClassEntry.getClassTime();
        List<String> courseTypeList = new ArrayList<String>();
        if(interestClassEntry.getIsLongCourse() == 1){
            courseTypeList.add("1");
            courseTypeList.add("2");
        } else {
            if (1 == interestClassEntry.getFirstTerm()) {
                courseTypeList.add("1");
            }
            if (1 == interestClassEntry.getSecondTerm()) {
                courseTypeList.add("2");
            }
        }
        Map<String, Object> classTimeMap = findClassTimeMap(userId);
        for (String str : timeList) {
            for (String type : courseTypeList) {
                Object o = classTimeMap.get(userId + "-" + type + "-" + str);
                if (o != null) {
                    return true;
                }

            }
        }
        return false;
    }
    /*
    *
    * 拓展课中删除学生
    *
    * */
    public boolean deleteStudent2ExpandClass(String expandClassId, String studentId) {
        interestClassDao.deleteStudentFromExpandClass(new ObjectId(expandClassId), new ObjectId(studentId));
        return true;
    }

    public void deleteStudentFromAllExpandClass(ObjectId studentId){
        interestClassDao.deleteStudentFromAllExpandClass(studentId);
    }

    /**
     * 通过学校查询兴趣班
     * @return
     */
    public List<InterestClassEntry> getInterestClassEntryBySchoolId(ObjectId schoolId)
    {
    	return interestClassDao.findClassBySchoolId(schoolId,Constant.NEGATIVE_ONE,  null, null);
    }

    /**
     * 通过老師id查询兴趣班
     * @return
     */
    public List<InterestClassEntry> getInterestClassEntryByTeacherId(ObjectId userId) {
        return interestClassDao.getInterestClassEntryByTeacherId(userId,Constant.NEGATIVE_ONE,  null, null);
    }
    
    /*
   *
   * 依据学校id 查找拓展课
   * */
    public List<InterestClassDTO> findExpandClassBySchoolId(String schoolID, String interestCategoryId, int termType) {
        if(termType < 0){
            SchoolEntry schoolEntry=schoolDao.getSchoolEntry(new ObjectId(schoolID),Constant.FIELDS);
            termType = schoolEntry.getTermType();
        }
        List<InterestClassEntry> classEntryList= interestClassDao.findClassBySchoolId(new ObjectId(schoolID), termType, interestCategoryId, null);
        List<ObjectId> ids=new ArrayList<ObjectId>();
        List<InterestClassDTO> interestClassDTOList =new ArrayList<InterestClassDTO>();
        List<InterestClassEntry> classlist = new ArrayList<InterestClassEntry>();
        for(InterestClassEntry interestClassEntry:classEntryList){
            if ((interestClassEntry.getFirstTerm()==1&&interestClassEntry.getSecondTerm()==0)
                    ||(interestClassEntry.getFirstTerm()==0&&interestClassEntry.getSecondTerm()==0)) {
                classlist.add(interestClassEntry);
            }

        }
        for (InterestClassEntry interestClassEntry:classlist) {
            InterestClassDTO interestClassDTO =new InterestClassDTO();
            if (interestClassEntry.getFirstTerm()==1&&interestClassEntry.getSecondTerm()==0) {
                interestClassEntry.setClassName(interestClassEntry.getClassName().substring(0,interestClassEntry.getClassName().length()-3));
            }
            interestClassDTO.setClassName(interestClassEntry.getClassName());
            interestClassDTO.setState(interestClassEntry.getState());
            ObjectId teacherId=interestClassEntry.getTeacherId();
            if(teacherId!=null){
                interestClassDTO.setTeacherId(teacherId.toString());
                ids.add(teacherId);
            }
            interestClassDTO.setId(interestClassEntry.getID().toString());

            interestClassDTOList.add(interestClassDTO);
        }
        List<UserEntry> userEntryList=userDao.getUserEntryList(ids, Constant.FIELDS);
        Map<String,UserEntry> map=new HashMap<String, UserEntry>();
        if(userEntryList!=null){
            for(UserEntry userEntry:userEntryList){
                map.put(userEntry.getID().toString(),userEntry);
            }
        }
        for(InterestClassDTO interestClassDTO : interestClassDTOList){
            UserEntry userEntry=map.get(interestClassDTO.getTeacherId());
            if(userEntry!=null) {
                interestClassDTO.setTeacherName(userEntry.getUserName());
            }

        }
        return interestClassDTOList;
    }

  /*
 *
 * 开始新学期
 *
 * schools  termType 属性+1
 * && 复制当前学校interestClass 数据重新插入(清空学生idList 且更新 termType字段)
 * */
    public void newTerm(String schoolID) {
        SchoolEntry schoolEntry=schoolDao.getSchoolEntry(new ObjectId(schoolID),Constant.FIELDS);
        int tt=schoolEntry.getTermType();
        String termName = examResultService.getCurrentTerm();
        IdNameValuePair term = new IdNameValuePair(null, termName, tt+1);
        schoolDao.updateTermType(new ObjectId(schoolID),tt+1);
        //查询旧的兴趣班记录 修改后重新插入
        List<InterestClassEntry> interestClassEntryList=
                interestClassDao.findClassBySchoolId(schoolEntry.getID(),tt,null, null);
        for(InterestClassEntry interestClassEntry:interestClassEntryList){
            interestClassEntry.setStudentCount(0);
            interestClassEntry.setTermType(tt + 1);
            List<IdNameValuePair> terms = interestClassEntry.getTerm();
            terms.add(term);
            interestClassEntry.setTerm(terms);
            interestClassDao.updateExpandClass(interestClassEntry);
        }
        interestClassTermsDao.updateTerms(new ObjectId(schoolID),term);
    }

    /**
     * 用于定时任务，每年的2月1号凌晨和9月1号的凌晨执行，为所有学校开始新的选课
     */
    public void timedNewTerm(){
        String name = "interestclassnewterm";
        try{
            Random random = new Random();
            int num = random.nextInt(2000);
            Thread.sleep(num);
            CrontabEntry crontabEntry = crontabDao.getCrontabEntry(name);
            if(crontabEntry == null){
                CrontabEntry entry = new CrontabEntry(name, 1);
                crontabDao.add(entry);
                crontabEntry = entry;
            }
            Thread.sleep(10000);//等待10秒
            CrontabEntry returnCrontabEntry = crontabDao.update(name, crontabEntry.getVersion() + 1);
            if(crontabEntry.getVersion() == returnCrontabEntry.getVersion()){
                updateTermForAllSchool();
            }
        } catch (Exception e){

        }

    }

    private void updateTermForAllSchool(){
        int skip=0;
        int limit=200;

        while(true) {
            List<SchoolEntry> schoolList=schoolDao.getSchoolEntry(skip, limit);

            if(null==schoolList || schoolList.isEmpty()) {
                break;
            }
            for(SchoolEntry school:schoolList) {
                newTerm(school.getID().toString());
            }
            skip=skip+200;
        }
    }

    public InterestClassDTO findInterestClassByClassId(String classId) {
        InterestClassEntry interestClassEntry= interestClassDao.findEntryByClassId(new ObjectId(classId));
        InterestClassDTO interestClassDTO =new InterestClassDTO();

        interestClassDTO.setIsLongCourse(interestClassEntry.getIsLongCourse()==1?true:false);
        interestClassDTO.setClassName(interestClassEntry.getClassName());
        interestClassDTO.setClassTime(interestClassEntry.getClassTime());
        interestClassDTO.setCloseTime(new Date(interestClassEntry.getCloseTime()));
        interestClassDTO.setFirstTerm(interestClassEntry.getFirstTerm() + "");
        interestClassDTO.setSchoolId(interestClassEntry.getSchoolId().toString());
        interestClassDTO.setId(interestClassEntry.getID().toString());
        interestClassDTO.setOpenTime(new Date(interestClassEntry.getOpenTime()));
        interestClassDTO.setSecondTerm(interestClassEntry.getSecondTerm() + "");
        interestClassDTO.setState(interestClassEntry.getState());
        interestClassDTO.setStudentCount(interestClassEntry.getStudentCount());
        interestClassDTO.setTotalStudentCount(interestClassEntry.getTotalCount());
        interestClassDTO.setClassContent(interestClassEntry.getClassContent());
        interestClassDTO.setRelationId(interestClassEntry.getRelationId());
        interestClassDTO.setRoom(interestClassEntry.getRoom());
        interestClassDTO.setSex(interestClassEntry.getSex());
        List<InterestClassStudentDTO> studentList=new ArrayList<InterestClassStudentDTO>();
        for(InterestClassStudent entry:interestClassEntry.getInterestClassStudents()){
            InterestClassStudentDTO interestClassStudentDTO =new InterestClassStudentDTO();
            interestClassStudentDTO.setCourseType(entry.getCourseType());
            interestClassStudentDTO.setStudentId(entry.getStudentId().toString());
            interestClassStudentDTO.setTermType(entry.getTermType());
            interestClassStudentDTO.setDropState(entry.getDropState());
            studentList.add(interestClassStudentDTO);
        }
        if (interestClassEntry.getIsLongCourse()==0) {
            interestClassDTO.setClassName(interestClassEntry.getClassName().substring(0,interestClassEntry.getClassName().length()-3));
            InterestClassEntry interestClassEntry2= interestClassDao.findEntryByClassId(interestClassEntry.getRelationId());
            if(interestClassEntry2 != null) {
                for (InterestClassStudent entry : interestClassEntry2.getInterestClassStudents()) {
                    InterestClassStudentDTO interestClassStudentDTO = new InterestClassStudentDTO();
                    interestClassStudentDTO.setCourseType(entry.getCourseType());
                    interestClassStudentDTO.setStudentId(entry.getStudentId().toString());
                    interestClassStudentDTO.setTermType(entry.getTermType());
                    interestClassStudentDTO.setDropState(entry.getDropState());
                    studentList.add(interestClassStudentDTO);
                }
            }
        }
        interestClassDTO.setStudentList(studentList);
        List<String> grades=new ArrayList<String>();
        List<ObjectId> gradeIds=interestClassEntry.getGradeIds();
        if(gradeIds!=null){
            for(ObjectId o:gradeIds){
                grades.add(o.toString());
            }
        }
        interestClassDTO.setGradeList(grades);
        interestClassDTO.setSubjectId(interestClassEntry.getSubjectId().toString());
        interestClassDTO.setTeacherId(interestClassEntry.getTeacherId().toString());
        interestClassDTO.setTermType(interestClassEntry.getTermType());
        UserEntry userEntry=userDao.getUserEntry(interestClassEntry.getTeacherId(), Constant.FIELDS);
        interestClassDTO.setTeacherName(userEntry.getUserName());

        return interestClassDTO;
    }

    public List<UserDetailInfoDTO> findStuByClassId(String classId) {

        InterestClassEntry classEntry=interestClassDao.findEntryByClassId(new ObjectId(classId));

        List<ObjectId> objectIdList= ClassService.collectObjectId(classEntry.getInterestClassStudents());
        List<UserEntry> userEntryList=userDao.findUserEntriesLimitRole(objectIdList,Constant.FIELDS);

        List<UserDetailInfoDTO> userInfoDTO4WBList=new ArrayList<UserDetailInfoDTO>();
        for(UserEntry userEntry:userEntryList){
            UserDetailInfoDTO userInfoDTO4WB=new UserDetailInfoDTO(userEntry);
            userInfoDTO4WBList.add(userInfoDTO4WB);
        }
        return userInfoDTO4WBList;
    }
    /*
*
* 查找兴趣班学生成绩单
*
* */
    public InterestClassTranscriptDTO findTranscriptByUserIdAndClassId(String userId, String classId, int termType) {
        InterestClassTranscriptEntry transEntry = interestClassDao.findTranscriptByUserIdAndClassId(new ObjectId(userId), new ObjectId(classId), termType);
        if(transEntry!=null){
            return new InterestClassTranscriptDTO(transEntry);
        }
        return null;
    }

    public List<InterestClassDTO> findInterestClassByStudentId(String id, int termType) {
        ObjectId userId = new ObjectId(id);
        List<InterestClassEntry> interestClassEntryList=interestClassDao.findClassInfoByStuId(userId);
        List<InterestClassEntry> classEntryList = new ArrayList<InterestClassEntry>();
        if(termType > 0){
            for(InterestClassEntry interestClassEntry:interestClassEntryList){
                List<InterestClassStudent> studentList = interestClassEntry.getInterestClassStudentsByTermType(termType);
                if(null != studentList){
                    for(InterestClassStudent student : studentList){
                        if(student.getStudentId().equals(userId) && student.getTermType()==termType){
                            classEntryList.add(interestClassEntry);
                            break;
                        }
                    }
                }
            }
        } else {
            classEntryList = interestClassEntryList;
        }

        List<InterestClassDTO> interestClassDTOList =new ArrayList<InterestClassDTO>();
        List<ObjectId> userIds=new ArrayList<ObjectId>();
        for(InterestClassEntry interestClassEntry:classEntryList){
            InterestClassDTO interestClassDTO =new InterestClassDTO(interestClassEntry);
            interestClassDTOList.add(interestClassDTO);
            userIds.add(interestClassEntry.getTeacherId());
        }

        //注入userName属性
        Map<ObjectId, UserEntry> userMap = userDao.getUserEntryMap(userIds, Constant.FIELDS);
        for(InterestClassDTO interestClassDTO : interestClassDTOList){
            String teacherId= interestClassDTO.getTeacherId();
            UserEntry userEntry=userMap.get(new ObjectId(teacherId));
            if(userEntry!=null){
                interestClassDTO.setTeacherName(userEntry.getUserName());
            }
        }
        return interestClassDTOList;
    }

    public Map<String, Object> findClassTimeMap(String userId) {
        List<InterestClassEntry> classEntryList=interestClassDao.findClassInfoByStuId(new ObjectId(userId));
        List<InterestClassEntry> currentList = new ArrayList<InterestClassEntry>();
        if(classEntryList!=null && classEntryList.size()>0){
            for(InterestClassEntry entry : classEntryList){
                List<InterestClassStudent> currentStudentList = entry.getCurrentInterestClassStudents();
                if(currentStudentList!=null && currentStudentList.size()>0){
                    for(InterestClassStudent student : currentStudentList){
                        if(student.getStudentId().toString().equals(userId)){
                            currentList.add(entry);
                            break;
                        }
                    }
                }
            }
        }
        Map<String,Object> classTimeMap=new HashMap<String, Object>();
        if(currentList!=null){
            for(InterestClassEntry interestClassEntry:currentList){
               List<String> studentList=interestClassEntry.getClassTime();
                if(studentList!=null){
                    for(String string:studentList){
                        List<InterestClassStudent> studentEntries=interestClassEntry.getInterestClassStudents();
                        if(studentEntries!=null){
                            for(InterestClassStudent studentEntry:studentEntries){
                                if (studentEntry.getCourseType()==0) {
                                    classTimeMap.put(studentEntry.getStudentId()+"-"+1+"-"+string,"");
                                    classTimeMap.put(studentEntry.getStudentId()+"-"+2+"-"+string,"");
                                } else {
                                    classTimeMap.put(studentEntry.getStudentId()+"-"+studentEntry.getCourseType()+"-"+string,"");
                                }
                            }
                        }
                    }
                }
            }
        }
        return classTimeMap;
    }

    //获取兴趣班所有学生信息
    public List<UserDetailInfoDTO> findAllInterestClassStuByClassId(String classId, int termType) {
        InterestClassEntry interestClassEntry=interestClassDao.findEntryByClassId(new ObjectId(classId));
        if(interestClassEntry==null || interestClassEntry.getInterestClassStudents()==null){
            return new ArrayList<UserDetailInfoDTO>();
        }
        List<InterestClassStudent>studentList = new ArrayList<InterestClassStudent>();
        if(termType < 0){
            studentList = interestClassEntry.getCurrentInterestClassStudents();
        } else {
            studentList = interestClassEntry.getInterestClassStudentsByTermType(termType);
        }

        List<UserEntry> userEntryList=userDao.getUserEntryList(collectObjectId(studentList), Constant.FIELDS);
        List<UserDetailInfoDTO> userDetailInfoDTOList=new ArrayList<UserDetailInfoDTO>();
        for(UserEntry userEntry:userEntryList){
            UserDetailInfoDTO userDetailInfoDTO=new UserDetailInfoDTO(userEntry);
            userDetailInfoDTOList.add(userDetailInfoDTO);
        }
        return userDetailInfoDTOList;
    }

    public static List<ObjectId> collectObjectId(List<InterestClassStudent>studentList){
        List<ObjectId> objectIdList=new ArrayList<ObjectId>();
        for(InterestClassStudent interestClassStudent:studentList){
            objectIdList.add(interestClassStudent.getStudentId());
        }
        return  objectIdList;
    }

    public List<InterestClassDTO> findInterestClassByGradeId(String gradeId) {
        if(null==gradeId || !ObjectId.isValid(gradeId)){
            return null;
        }
        List<InterestClassEntry> interestClassEntryList=interestClassDao.findClassByGradeId(new ObjectId(gradeId));
        List<InterestClassDTO> interestClassDTOList=new ArrayList<InterestClassDTO>();
        if(interestClassEntryList!=null){
            for(InterestClassEntry interestClassEntry:interestClassEntryList){
                InterestClassDTO interestClassDTO=new InterestClassDTO(interestClassEntry);
                interestClassDTOList.add(interestClassDTO);
            }
        }
        return interestClassDTOList;
    }

    /**
     * 将老师与科目关系添加至tcschool表中
     * author:qiangm
     * @param classid
     * @param teacherid
     * @param subjectid
     */
    public void addTeacherSubject(ObjectId classid, String teacherid, String subjectid) {
        InterestClassEntry classEntry= interestClassDao.findEntryByClassId(classid);
        ObjectId schoolId = classEntry.getSchoolId();
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(schoolId,Constant.FIELDS);
        List<Subject> subjects = schoolEntry.getSubjects();
        Map<ObjectId, String> subjectMap = new HashMap<ObjectId, String>();
        for (Subject subject : subjects) {
            subjectMap.put(subject.getSubjectId(), subject.getName());
        }
        TeacherClassSubjectEntry teacherClassLessonEntry = new TeacherClassSubjectEntry(new BasicDBObject());
        teacherClassLessonEntry.setTeacherId(new ObjectId(teacherid));
        teacherClassLessonEntry.setClassInfo(new IdValuePair(classid, classEntry.getClassName()));
        teacherClassLessonEntry.setSubjectInfo(new IdValuePair(new ObjectId(subjectid), subjectMap.get(new ObjectId(subjectid))));
        //插入teacherClassSubjectEntry
        ObjectId tclId = teacherClassSubjectDao.addTeacherClassSubjectEntry(teacherClassLessonEntry);
        //新建dir
        DirEntry entry=new DirEntry(tclId,classEntry.getClassName()+subjectMap.get(new ObjectId(subjectid)),null,0, DirType.CLASS_LESSON);
        dirDao.addDirEntry(entry);
    }

    /**
     * 修改tcsubject表和dir表
     * author:qiangm
     * @param classId
     * @param teacherId
     * @param subjectId
     * @param oldTeacherId
     * @param oldSubjectId
     */
    public void updateTeacherSubject(ObjectId classId, String teacherId,String subjectId,String oldTeacherId,String oldSubjectId,String className)
    {
        ObjectId tcsId=teacherClassSubjectDao.getTcsubjectId(classId, new ObjectId(oldTeacherId), new ObjectId(oldSubjectId));
        InterestClassEntry classEntry= interestClassDao.findEntryByClassId(classId);
        ObjectId schoolId = classEntry.getSchoolId();
        SchoolEntry schoolEntry = schoolDao.getSchoolEntry(schoolId,Constant.FIELDS);
        List<Subject> subjects = schoolEntry.getSubjects();
        Map<ObjectId, String> subjectMap = new HashMap<ObjectId, String>();
        for (Subject subject : subjects) {
            subjectMap.put(subject.getSubjectId(), subject.getName());
        }
        String subjectName=subjectMap.get(new ObjectId(subjectId));
        if(tcsId!=null) {
            teacherClassSubjectDao.updateTeacherAndSubject(tcsId, new ObjectId(teacherId), new ObjectId(subjectId), subjectName, classId, className);
            //修改dir表
            dirDao.updateTopClassDirByOwnerId(tcsId,"dn",className+subjectName);
        }
    }
    /**
     * 获取最新添加的兴趣班班级ID
     * author：qinagm
     * @param intclass
     * @return
     */
    public ObjectId findInterestClassId(InterestClassEntry intclass)
    {
        return interestClassDao.findInterestClassId(intclass);
    }
    
    /**
     * 根据Id查询兴趣班
     * @param ids
     * @param fields
     * @return
     */
    public List<InterestClassEntry> findInterestClassEntrysByIds(Collection<ObjectId> ids,DBObject fields)
    {
    	 return interestClassDao.findInterestClassEntrysByIds(ids,fields);
    }

    public InterestClassEntry getInterestClassEntryById(ObjectId classId){
        return interestClassDao.findEntryByClassId(classId);
    }





    /**
     * 根据老师id查询走班&选修课
     * */
    public List<ZouBanCourseDTO> findZoubanCourseByTeacherId(ObjectId teacherId){
        List<ZouBanCourseDTO> dtoList = new ArrayList<ZouBanCourseDTO>();
        List<ZouBanCourseEntry> entryList = zouBanCourseDao.findCourseListByTeacherId(TermUtil.getSchoolYear(),teacherId,false);
        for(ZouBanCourseEntry entry : entryList){
            dtoList.add(new ZouBanCourseDTO(entry));
        }
        return dtoList;
    }

    /**
     * 根据课程id查询课程(选修课)
     * */
    public ZouBanCourseEntry getZoubanCourseEntry(ObjectId courseId){
        return zouBanCourseDao.getCourseInfoById(courseId);
    }

    /**
     * 根据学生id查询课程
     * */
    public List<ZouBanCourseDTO> getStudentCourse(ObjectId studentId){
        List<ZouBanCourseDTO> dtoList = new ArrayList<ZouBanCourseDTO>();
        List<ZouBanCourseEntry> entryList = zouBanCourseDao.getStudentCourse(TermUtil.getSchoolYear(), studentId);
        for(ZouBanCourseEntry entry : entryList){
            dtoList.add(new ZouBanCourseDTO(entry));
        }

        return dtoList;
    }


}
