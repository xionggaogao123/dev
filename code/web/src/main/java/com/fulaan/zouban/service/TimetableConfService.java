package com.fulaan.zouban.service;

import com.db.zouban.EventConflictDao;
import com.db.zouban.TimeTableDao;
import com.db.zouban.TimetableConfDao;
import com.db.zouban.ZouBanCourseDao;
import com.fulaan.examresult.controller.IdNameDTO;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.controller.GradeView;
import com.fulaan.school.service.SchoolService;
import com.fulaan.zouban.dto.*;
import com.pojo.school.ClassEntry;
import com.pojo.school.ClassInfoDTO;
import com.pojo.zouban.*;
import com.sys.constants.Constant;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by wangkaidong on 2016/7/26.
 * <p/>
 * 课表配置Service
 */
@Service
public class TimetableConfService {
    private TimetableConfDao timetableConfDao = new TimetableConfDao();
    private TimeTableDao timeTableDao = new TimeTableDao();
    private ZouBanCourseDao zouBanCourseDao = new ZouBanCourseDao();

    @Autowired
    private ClassService classService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private TimeTableService timeTableService;
    @Autowired
    private ZoubanStateService zoubanStateService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private EventConflictService eventConflictService;
    @Autowired
    private PaikeService paikeService;

    /**
     * 删除班级事务(事务维度（班级）)
     *
     * @param term
     * @param gradeId
     * @param classId
     * @param x
     * @param y
     */
    public void removeEventClass(String term,
                                 ObjectId gradeId,
                                 String classId,
                                 int x,
                                 int y) {
        TimetableConfDTO timetableConf = getTimetableConf(term, gradeId);
        List<ClassEventDTO> classEventDTOList = new ArrayList<ClassEventDTO>();
        List<ClassEventDTO> classEventDTOs = timetableConf.getClassEventList();
        for (ClassEventDTO classEventDTO : classEventDTOs) {
            if (classEventDTO.getX() == x && classEventDTO.getY() == y) {
                List<IdNameDTO> idNameDTOList = new ArrayList<IdNameDTO>();
                List<IdNameDTO> idNameDTOs = classEventDTO.getClassList();
                for (IdNameDTO idNameDTO : idNameDTOs) {
                    if (!classId.equals(idNameDTO.getId())) {
                        idNameDTOList.add(idNameDTO);
                    }
                }
                classEventDTO.setClassList(idNameDTOList);
            }
            classEventDTOList.add(classEventDTO);
        }
        timetableConf.setClassEventList(classEventDTOList);
        updateConf(timetableConf);
    }

    /**
     * 删除该班级对应的课表
     *
     * @param term
     * @param classId
     * @param x
     * @param y
     */
    public void removeTimetableEntry(String term, String classId, int x, int y, String gradeId) {
        TimeTableEntry timeTableEntry = timeTableService.findTimeTableEntry(term, classId, TimetableState.NOTPUBLISHED.getState(), 0);
        List<CourseItem> courseItemDTOs = timeTableEntry.getCourseList();

        Set<ObjectId> courseIdList = new HashSet<ObjectId>();

        for (CourseItem courseItem : courseItemDTOs) {
            if (courseItem.getXIndex() == x && courseItem.getYIndex() == y) {
                //判断该班级事务是否影响到了走班、分层走班以及体育排课
                //判断无课事务是否影响到后面的走班、分层走班以及体育排课。
                if (courseItem.getType() == ZoubanType.ZOUBAN.getType()) {

                    List<TimeTableEntry> timeTableEntryList = timeTableDao.findTimeTableByGrade(term, new ObjectId(gradeId), TimetableState.NOTPUBLISHED.getState(), 0);

                    for (TimeTableEntry timeTableEntry1 : timeTableEntryList) {
                        for (CourseItem courseItem1 : timeTableEntry1.getCourseList()) {
                            if (courseItem1.getType() != ZoubanType.ZOUBAN.getType()) {
                                courseIdList.addAll(courseItem1.getCourse());
                            }
                        }
                    }

                } else if (courseItem.getType() == ZoubanType.GROUPZOUBAN.getType()) {
                    List<TimeTableEntry> timeTableEntryList = timeTableDao.findTimeTableByGrade(term, new ObjectId(gradeId), TimetableState.NOTPUBLISHED.getState(), 0);

                    for (TimeTableEntry timeTableEntry1 : timeTableEntryList) {
                        for (CourseItem courseItem1 : timeTableEntry1.getCourseList()) {
                            if (courseItem1.getType() != ZoubanType.ZOUBAN.getType() &&
                                    courseItem1.getType() != ZoubanType.GROUPZOUBAN.getType()) {
                                courseIdList.addAll(courseItem1.getCourse());
                            }
                        }
                    }

                } else if (courseItem.getType() == ZoubanType.PE.getType()) {
                    List<TimeTableEntry> timeTableEntryList = timeTableDao.findTimeTableByGrade(term, new ObjectId(gradeId), TimetableState.NOTPUBLISHED.getState(), 0);

                    for (TimeTableEntry timeTableEntry1 : timeTableEntryList) {
                        for (CourseItem courseItem1 : timeTableEntry1.getCourseList()) {
                            if (courseItem1.getType() != ZoubanType.ZOUBAN.getType() &&
                                    courseItem1.getType() != ZoubanType.GROUPZOUBAN.getType() &&
                                    courseItem1.getType() != ZoubanType.PE.getType()) {
                                courseIdList.addAll(courseItem1.getCourse());
                            }
                        }
                    }
                }

                timeTableDao.removeCourse(term, new ObjectId(classId), TimetableState.NOTPUBLISHED.getState(), courseItem);
                courseIdList.addAll(courseItem.getCourse());
                break;
            }
        }
        //删除事务冲突
        eventConflictService.removeConflictByCourseList(term, new ObjectId(gradeId), x, y, courseIdList);
    }

    /**
     * 组合两个数组
     *
     * @param teacherList
     * @param teacherList1
     * @return
     */
    public List<IdNameDTO> getToghether(List<IdNameDTO> teacherList, List<IdNameDTO> teacherList1) {
        List<IdNameDTO> idNameDTOs = new ArrayList<IdNameDTO>();
        idNameDTOs.addAll(teacherList);
        for (IdNameDTO idNameDTO : teacherList1) {
            boolean flag = false;
            for (IdNameDTO idNameDTO1 : teacherList) {
                if (idNameDTO.equals(idNameDTO1)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                idNameDTOs.add(idNameDTO);
            }
        }
        return idNameDTOs;
    }

    /**
     * 获取学科老师列表
     *
     * @param term
     * @param schoolId
     */
    public List<SubjectTeacher> getSubjectTeacher(String term, String schoolId) {
        List<GradeView> gradeList = schoolService.findGradeList(schoolId);

        List<SubjectTeacher> subjectTeacherList = new ArrayList<SubjectTeacher>();
        List<SubjectTeacher> list = new ArrayList<SubjectTeacher>();
        for (GradeView gradeView : gradeList) {
            if (subjectTeacherList.size() == 0) {
                subjectTeacherList = commonService.getSubjectTeacherList(schoolId, gradeView.getId());
                list.addAll(subjectTeacherList);
            } else {
                subjectTeacherList.clear();
                subjectTeacherList.addAll(list);
                list.clear();
                List<SubjectTeacher> subjectTeachers = commonService.getSubjectTeacherList(schoolId, gradeView.getId());
                for (int i = 0; i < subjectTeacherList.size(); i++) {
                    boolean flag = false;
                    SubjectTeacher subjectTeacher = subjectTeacherList.get(i);
                    for (int j = 0; j < subjectTeachers.size(); j++) {
                        if (subjectTeachers.get(j).getSubjectName().equals(subjectTeacher.getSubjectName())) {
                            flag = true;
                        }
                    }
                    if (!flag) {
                        list.add(subjectTeacher);
                    }
                }

                for (int i = 0; i < subjectTeachers.size(); i++) {
                    boolean flag = false;
                    SubjectTeacher subjectTeacher = subjectTeachers.get(i);
                    for (int j = 0; j < subjectTeacherList.size(); j++) {
                        if (subjectTeacherList.get(j).getSubjectName().equals(subjectTeacher.getSubjectName())) {
                            List<IdNameDTO> teacherList = subjectTeacherList.get(j).getTeacherList();
                            List<IdNameDTO> teacherList1 = subjectTeacher.getTeacherList();
                            SubjectTeacher item = new SubjectTeacher();
                            item.setSubjectId(subjectTeacher.getSubjectId());
                            item.setSubjectName(subjectTeacher.getSubjectName());
                            List<IdNameDTO> idNameDTOs = getToghether(teacherList, teacherList1);
                            item.setTeacherList(idNameDTOs);
                            list.add(item);
                            flag = true;
                        }
                    }
                    if (!flag) {
                        list.add(subjectTeacher);
                    }
                }
            }
        }
        List<SubjectTeacher> Filter = new ArrayList<SubjectTeacher>();
        for (SubjectTeacher subjectTeacher : list) {
            if (subjectTeacher.getTeacherList().size() == 0) {
                Filter.add(subjectTeacher);
            }
        }
        list.removeAll(Filter);
        return list;
    }

    /**
     * 获取班级维度的班级无可事务
     *
     * @param term
     * @param gradeId
     * @param classId
     * @return
     */
    public TimetableConfDTO getClassEventTimetable(String term, String gradeId, String classId) {
        List<PointDTO> points = new ArrayList<PointDTO>();
        TimetableConfDTO timetableConf = getTimetableConf(term, new ObjectId(gradeId));
        if (timetableConf.getLock() != 0) {
            List<ClassEventDTO> classEventDTOs = timetableConf.getClassEventList();
            int count = classService.findClassByGradeId(gradeId).size();
            for (ClassEventDTO classEventDTO : classEventDTOs) {
                if (classEventDTO.getClassList().size() == count) {
                    PointDTO point = new PointDTO();
                    point.setX(classEventDTO.getX());
                    point.setY(classEventDTO.getY());
                    point.setDescription("无课");
                    points.add(point);
                } else {
                    for (IdNameDTO item : classEventDTO.getClassList()) {
                        if (classId.equals(item.getId())) {
                            PointDTO point = new PointDTO();
                            point.setX(classEventDTO.getX());
                            point.setY(classEventDTO.getY());
                            point.setDescription("无课");
                            points.add(point);
                            break;
                        }
                    }
                }
            }
        }
        timetableConf.setPointDTOs(points);
        return timetableConf;
    }

    /**
     * 判断该年级课表有没有排完
     *
     * @param term
     * @param gradeId
     * @return
     */
    public boolean judgementState(String term, String gradeId) {
        boolean flag = true;
        //检查课程是否排完
        if (!paikeService.checkZBFinished(term, gradeId.toString())) {
            flag = false;
        }

        if (!paikeService.checkFZZBFinished(term, gradeId.toString())) {
            flag = false;
        }

        if (!paikeService.checkPEFinished(term, gradeId.toString())) {
            flag = false;
        }
        if (!paikeService.checkFZBFinished(term, gradeId.toString())) {
            flag = false;
        }

        if (!paikeService.checkDSZFinished(term, gradeId.toString())) {
            flag = false;
        }
        return flag;
    }

    /**
     * 新增班级事务(事务维度（全校以及年级）)
     *
     * @param term
     * @param gradeId
     * @param x
     * @param y
     * @param schoolId
     */
    public void addSchoolClassEventList(String term,
                                        String gradeId,
                                        int x,
                                        int y,
                                        String schoolId) {
        if (("All").equals(gradeId)) {
            List<GradeView> gradeList = schoolService.findGradeList(schoolId);
            for (GradeView gradeView : gradeList) {
                List<ClassInfoDTO> classInfoList = new ArrayList<ClassInfoDTO>();
                List<ClassInfoDTO> classInfoDTOs = classService.findClassByGradeId(gradeView.getId());
                if (null != classInfoDTOs && !classInfoDTOs.isEmpty()) {
                    classInfoList.addAll(classInfoDTOs);
                    List<IdNameDTO> classList = new ArrayList<IdNameDTO>();
                    for (ClassInfoDTO classInfoDTO : classInfoList) {
                        IdNameDTO idNameDTO = new IdNameDTO();
                        idNameDTO.setId(classInfoDTO.getId());
                        idNameDTO.setName(classInfoDTO.getClassName());
                        classList.add(idNameDTO);
                        //删除该班级对应的课表
                        removeTimetableEntry(term, classInfoDTO.getId(), x, y, gradeView.getId());
                    }
                    ClassEventDTO eventDTO = new ClassEventDTO();
                    eventDTO.setName("无课事务");
                    eventDTO.setX(x);
                    eventDTO.setY(y);
                    eventDTO.setClassList(classList);
                    addClassEvent(term, gradeView.getId(), eventDTO);
                }
                int state = zoubanStateService.getZoubanState(term, schoolId, gradeView.getId());
                if (state > 4) {
                    if (!judgementState(term, gradeView.getId())) {
                        //取消发布
                        timeTableService.cancelPublishCourse(term, new ObjectId(schoolId), new ObjectId(gradeView.getId()));

                        zoubanStateService.setZoubanState(term, gradeView.getId(), 4);
                    }
                }
            }

        } else {
            List<ClassInfoDTO> classInfoList = new ArrayList<ClassInfoDTO>();
            List<ClassInfoDTO> classInfoDTOs = classService.findClassByGradeId(gradeId);
            if (null != classInfoDTOs && !classInfoDTOs.isEmpty()) {
                classInfoList.addAll(classInfoDTOs);
                List<IdNameDTO> classList = new ArrayList<IdNameDTO>();
                for (ClassInfoDTO classInfoDTO : classInfoList) {
                    IdNameDTO idNameDTO = new IdNameDTO();
                    idNameDTO.setId(classInfoDTO.getId());
                    idNameDTO.setName(classInfoDTO.getClassName());
                    classList.add(idNameDTO);
                    //删除该班级对应的课表
                    removeTimetableEntry(term, classInfoDTO.getId(), x, y, gradeId);
                }
                ClassEventDTO eventDTO = new ClassEventDTO();
                eventDTO.setName("无课事务");
                eventDTO.setX(x);
                eventDTO.setY(y);
                eventDTO.setClassList(classList);
                addClassEvent(term, gradeId, eventDTO);
            }
            int state = zoubanStateService.getZoubanState(term, schoolId, gradeId.toString());
            if (state > 4) {
                if (!judgementState(term, gradeId.toString())) {
                    //取消发布
                    timeTableService.cancelPublishCourse(term, new ObjectId(schoolId), new ObjectId(gradeId));
                    zoubanStateService.setZoubanState(term, gradeId.toString(), 4);
                }
            }
        }
    }

    /**
     * 新增班级事务(事务维度（班级）)
     *
     * @param term
     * @param gradeId
     * @param classId
     * @param x
     * @param y
     */
    public void addClassEventList(String term,
                                  ObjectId gradeId,
                                  String classId,
                                  int x,
                                  int y) {
        //删除该班级对应的课表
        removeTimetableEntry(term, classId, x, y, gradeId.toString());
        TimetableConfDTO timetableConf = getTimetableConf(term, gradeId);
        List<ClassEventDTO> classEventDTOList = new ArrayList<ClassEventDTO>();
        List<ClassEventDTO> classEventDTOs = timetableConf.getClassEventList();
        ClassEntry classEntry = classService.getClassEntryById(new ObjectId(classId), Constant.FIELDS);
        boolean flag = false;
        if (classEventDTOs.size() > 0) {
            for (ClassEventDTO classEventDTO : classEventDTOs) {
                if (classEventDTO.getX() == x && classEventDTO.getY() == y) {
                    List<IdNameDTO> idNameDTOs = classEventDTO.getClassList();
                    IdNameDTO idNameDTO = new IdNameDTO();
                    idNameDTO.setId(classId);
                    idNameDTO.setName(classEntry.getName());
                    idNameDTOs.add(idNameDTO);
                    classEventDTO.setClassList(idNameDTOs);
                    classEventDTOList.add(classEventDTO);
                    flag = true;
                } else {
                    classEventDTOList.add(classEventDTO);
                }
            }
        } else {
            IdNameDTO idNameDTO = new IdNameDTO();
            List<IdNameDTO> idNameDTOs = new ArrayList<IdNameDTO>();
            idNameDTO.setId(classId);
            idNameDTO.setName(classEntry.getName());
            idNameDTOs.add(idNameDTO);
            ClassEventDTO classEventDTO = new ClassEventDTO();
            classEventDTO.setName("无课事务");
            classEventDTO.setX(x);
            classEventDTO.setY(y);
            classEventDTO.setClassList(idNameDTOs);
            classEventDTOList.add(classEventDTO);
        }
        if (!flag) {
            IdNameDTO idNameDTO = new IdNameDTO();
            List<IdNameDTO> idNameDTOs = new ArrayList<IdNameDTO>();
            idNameDTO.setId(classId);
            idNameDTO.setName(classEntry.getName());
            idNameDTOs.add(idNameDTO);
            ClassEventDTO classEventDTO1 = new ClassEventDTO();
            classEventDTO1.setName("无课事务");
            classEventDTO1.setX(x);
            classEventDTO1.setY(y);
            classEventDTO1.setClassList(idNameDTOs);
            classEventDTOList.add(classEventDTO1);
        }

        timetableConf.setClassEventList(classEventDTOList);
        updateConf(timetableConf);
    }


    /**
     * 锁定课表配置
     *
     * @param schoolId
     * @param timetableConfDTO
     */
    public void lockTimetableConf(ObjectId schoolId, TimetableConfDTO timetableConfDTO) {
        //与原配置对比，少了的格子删除已排课程，并清空相关事务冲突；

        String term = timetableConfDTO.getTerm();
        List<GradeView> gradeList = schoolService.findGradeList(schoolId.toString());
        List<ObjectId> gradeIdList = new ArrayList<ObjectId>();

        for (GradeView gv : gradeList) {
            gradeIdList.add(new ObjectId(gv.getId()));
        }


        //删除课表中相关课程

        TimetableConfEntry oldConf = timetableConfDao.getTimetableConf(timetableConfDTO.getTerm(), new ObjectId(gradeList.get(0).getId()));
        //x轴
        List<Integer> oldDays = oldConf.getDays();
        List<Integer> newDays = timetableConfDTO.getDays();
        for (Integer oldDay : oldDays) {
             if (!newDays.contains(oldDay)) {
                 //删除课程
                 timeTableDao.removeCourseByXY(term, schoolId, oldDay, -1);
                 //删除冲突
                 eventConflictService.removeEventConflictByXY(term, gradeIdList, oldDay, -1);
            }
        }

        //y轴
        int oldClassCount = oldConf.getClassCount();
        int newClassCount = timetableConfDTO.getClassCount();
        if (newClassCount < oldClassCount) {
            for (int i = newClassCount + 1; i <= oldClassCount; i++) {
                //删除课程
                timeTableDao.removeCourseByXY(term, schoolId, -1, i);
                //删除冲突
                eventConflictService.removeEventConflictByXY(term, gradeIdList, -1, i);
            }
        }

        //更新课表结构
        for (ObjectId gid : gradeIdList) {
            TimetableConfEntry timetableConf = timetableConfDao.getTimetableConf(timetableConfDTO.getTerm(), gid);
            if (null != timetableConf) {
                timetableConf.setDays(timetableConfDTO.getDays());
                timetableConf.setClassTime(timetableConfDTO.getClassTime());
                timetableConf.setClassCount(timetableConfDTO.getClassCount());
                //更新课表结构
                updateConf(new TimetableConfDTO(timetableConf));
            } else {
                timetableConf = timetableConfDTO.exportEntry();
                timetableConf.setGradeId(gid);
                timetableConfDao.add(timetableConf);
            }
        }


    }


    /**
     * 获取课表配置以及班级事务（事务维度全校及年级）
     *
     * @param gradeId
     * @param term
     * @param schoolId
     * @return
     */
    public TimetableConfDTO getTimetableConfDTO(String gradeId, String term, String schoolId) {
        Map<String, List<ClassEventDTO>> timetableConfDTOList = new HashMap<String, List<ClassEventDTO>>();
        TimetableConfDTO timetableConf;
        List<PointDTO> points = new ArrayList<PointDTO>();
        Boolean flag = false;
        //生成所有年级的班级课表
        List<GradeView> gradeList = schoolService.findGradeList(schoolId);
        for (GradeView gradeView : gradeList) {
            TimetableConfDTO timetableConfDTO = getTimetableConf(term, new ObjectId(gradeView.getId()));
            if (timetableConfDTO.getLock() == 0) {
                timetableConfDTO.setLock(1);
                //删除课表
                clearTimetable(term, gradeView.getId());
                //新增课表
                createTimetable(term, gradeView.getId(), schoolId);
                //更新课表结构
                updateConf(timetableConfDTO);
                //取消发布
                timeTableService.cancelPublishCourse(term, new ObjectId(schoolId), new ObjectId(gradeView.getId()));
                //设置进度
                zoubanStateService.setZoubanState(term, gradeView.getId(), 4);
            }
        }

        if (("All").equals(gradeId)) {
            timetableConf = getTimetableConf(term, new ObjectId(gradeList.get(0).getId()));
            if (timetableConf.getLock() != 0) {
                for (GradeView gradeView : gradeList) {
                    timetableConf = getTimetableConf(term, new ObjectId(gradeView.getId()));
                    List<ClassEventDTO> classEventDTOs = timetableConf.getClassEventList();
                    timetableConfDTOList.put(gradeView.getId(), classEventDTOs);
                }
                for (Map.Entry<String, List<ClassEventDTO>> entry : timetableConfDTOList.entrySet()) {

                    String key = entry.getKey();
                    int count = classService.findClassByGradeId(key).size();
                    List<ClassEventDTO> value = entry.getValue();
                    if (value.size() == 0) {
                        flag = true;
                        for (int i = 0; i < points.size(); i++) {
                            PointDTO point1 = points.get(i);
                            PointDTO point = new PointDTO();
                            point.setX(point1.getX());
                            point.setY(point1.getY());
                            point.setDescription("各班不一样");
                            points.set(i, point);
                        }
                    } else {
                        if (flag) {
                            for (int i = 0; i < points.size(); i++) {
                                PointDTO point1 = points.get(i);
                                PointDTO point = new PointDTO();
                                point.setX(point1.getX());
                                point.setY(point1.getY());
                                point.setDescription("各班不一样");
                                points.set(i, point);
                            }
                            for (ClassEventDTO classEventDTO : value) {
                                boolean temp = false;
                                if (points.size() > 0) {
                                    for (int i = 0; i < points.size(); i++) {
                                        PointDTO point1 = points.get(i);
                                        if (point1.getX() == classEventDTO.getX() && point1.getY() == classEventDTO.getY()) {
                                            temp = true;
                                        }
                                    }
                                    if (!temp) {
                                        PointDTO point = new PointDTO();
                                        point.setX(classEventDTO.getX());
                                        point.setY(classEventDTO.getY());
                                        point.setDescription("各班不一样");
                                        points.add(point);
                                    }
                                } else {
                                    PointDTO point = new PointDTO();
                                    point.setX(classEventDTO.getX());
                                    point.setY(classEventDTO.getY());
                                    point.setDescription("各班不一样");
                                    points.add(point);
                                }
                            }
                        } else {
                            if (points.size() == 0) {
                                for (ClassEventDTO classEventDTO : value) {
                                    if (classEventDTO.getClassList().size() == count) {
                                        PointDTO point = new PointDTO();
                                        point.setX(classEventDTO.getX());
                                        point.setY(classEventDTO.getY());
                                        point.setDescription("无课");
                                        points.add(point);
                                    } else {
                                        PointDTO point = new PointDTO();
                                        point.setX(classEventDTO.getX());
                                        point.setY(classEventDTO.getY());
                                        point.setDescription("各班不一样");
                                        points.add(point);
                                    }
                                }
                            } else {

                                List<PointDTO> pointDTOs = new ArrayList<PointDTO>();
                                //记录数据
                                List<PointJson> pointJsons = new ArrayList<PointJson>();
                                List<PointJson> pointJsonList = new ArrayList<PointJson>();
                                for (int j = 0; j < value.size(); j++) {
                                    boolean ff = false;
                                    for (int i = 0; i < points.size(); i++) {
                                        if (value.get(j).getX() == points.get(i).getX()
                                                && value.get(j).getY() == points.get(i).getY()) {
                                            ff = true;
                                        }
                                    }
                                    if (!ff) {
                                        PointJson pointJson = new PointJson();
                                        pointJson.setX(value.get(j).getX());
                                        pointJson.setY(value.get(j).getY());
                                        pointJsons.add(pointJson);
                                    }
                                }

                                for (int j = 0; j < points.size(); j++) {
                                    boolean ff = false;
                                    for (int i = 0; i < value.size(); i++) {
                                        if (value.get(i).getX() == points.get(j).getX()
                                                && value.get(i).getY() == points.get(j).getY()) {
                                            ff = true;
                                        }
                                    }
                                    if (!ff) {
                                        PointJson pointJson = new PointJson();
                                        pointJson.setX(points.get(j).getX());
                                        pointJson.setY(points.get(j).getY());
                                        pointJsonList.add(pointJson);
                                    }
                                }


                                //各个年级之间的比较
                                for (ClassEventDTO classEventDTO : value) {
                                    pointDTOs.clear();
                                    pointDTOs.addAll(points);
                                    boolean f = false;
                                    for (int i = 0; i < pointDTOs.size(); i++) {
                                        PointDTO point1 = pointDTOs.get(i);
                                        if (point1.getX() == classEventDTO.getX() && point1.getY() == classEventDTO.getY()) {
                                            if (classEventDTO.getClassList().size() != count) {
                                                if ("无课".equals(point1.getDescription())) {
                                                    PointDTO point = new PointDTO();
                                                    point.setX(classEventDTO.getX());
                                                    point.setY(classEventDTO.getY());
                                                    point.setDescription("各班不一样");
                                                    pointDTOs.set(i, point);
                                                }
                                            }
                                            f = true;
                                        }
                                    }
                                    if (!f) {
                                        //添加事务
                                        PointDTO pointDTO = new PointDTO();
                                        pointDTO.setX(classEventDTO.getX());
                                        pointDTO.setY(classEventDTO.getY());
                                        pointDTO.setDescription("各班不一样");
                                        pointDTOs.add(pointDTO);
                                    }
                                    points.clear();
                                    points.addAll(pointDTOs);
                                }
                                for (int i = 0; i < points.size(); i++) {
                                    for (int j = 0; j < pointJsons.size(); j++) {
                                        if (points.get(i).getX() == pointJsons.get(j).getX() && points.get(i).getY() == pointJsons.get(j).getY()) {
                                            PointDTO point = new PointDTO();
                                            point.setX(points.get(i).getX());
                                            point.setY(points.get(i).getY());
                                            point.setDescription("各班不一样");
                                            points.set(i, point);
                                        }
                                    }
                                    for (int j = 0; j < pointJsonList.size(); j++) {
                                        if (points.get(i).getX() == pointJsonList.get(j).getX() && points.get(i).getY() == pointJsonList.get(j).getY()) {
                                            PointDTO point = new PointDTO();
                                            point.setX(points.get(i).getX());
                                            point.setY(points.get(i).getY());
                                            point.setDescription("各班不一样");
                                            points.set(i, point);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            timetableConf = getTimetableConf(term, new ObjectId(gradeId));
            if (timetableConf.getLock() != 0) {
                List<ClassEventDTO> classEventDTOs = timetableConf.getClassEventList();
                int count = classService.findClassByGradeId(gradeId).size();
                for (ClassEventDTO classEventDTO : classEventDTOs) {
                    if (classEventDTO.getClassList().size() == count) {
                        PointDTO point = new PointDTO();
                        point.setX(classEventDTO.getX());
                        point.setY(classEventDTO.getY());
                        point.setDescription("无课");
                        points.add(point);
                    } else {
                        PointDTO point = new PointDTO();
                        point.setX(classEventDTO.getX());
                        point.setY(classEventDTO.getY());
                        point.setDescription("各班不一样");
                        points.add(point);
                    }
                }
            }
        }
        timetableConf.setPointDTOs(points);
        return timetableConf;
    }

    /**
     * 获取课表配置
     *
     * @param term
     * @param gradeId
     * @return
     */
    public TimetableConfDTO getTimetableConf(String term, ObjectId gradeId) {
        TimetableConfEntry timetableConfEntry = timetableConfDao.getTimetableConf(term, gradeId);
        if (timetableConfEntry == null) {//没有课表配置则新增一个默认配置
            timetableConfEntry = new TimetableConfEntry();
            timetableConfEntry.setTerm(term);
            timetableConfEntry.setGradeId(gradeId);
            timetableConfEntry.setClassCount(8);
            timetableConfEntry.setLock(0);
            timetableConfEntry.setEvent(new ArrayList<TimetableConfEntry.Event>());
            timetableConfEntry.setClassEvent(new ArrayList<TimetableConfEntry.ClassEvent>());
            List<Integer> days = new ArrayList<Integer>() {{
                add(1);
                add(2);
                add(3);
                add(4);
                add(5);
            }};
            timetableConfEntry.setDays(days);

            List<String> classTime = new ArrayList<String>() {{
                add("08:00~08:45");
                add("08:55~09:40");
                add("10:00~10:45");
                add("10:55~11:40");
                add("14:00~14:45");
                add("14:55~15:40");
                add("16:00~16:45");
                add("16:55~17:40");
            }};
            timetableConfEntry.setClassTime(classTime);
            timetableConfEntry.setID(new ObjectId());
            timetableConfDao.add(timetableConfEntry);

        }
        return new TimetableConfDTO(timetableConfEntry);
    }

    /**
     * 根据事务id获取事务
     *
     * @param term
     * @param eventId
     * @return
     */
    public TimetableConfEntry.Event getConfEvent(String term, ObjectId eventId) {
        TimetableConfEntry timetableConfEntry = timetableConfDao.findConfByEventId(term, eventId);

        for (TimetableConfEntry.Event event : timetableConfEntry.getEvent()) {
            if (event.getID().equals(eventId)) {
                return event;
            }
        }
        return null;
    }


    /**
     * 是否锁定
     *
     * @param term
     * @param gradeId
     * @return
     */
    public boolean isLocked(String term, String gradeId) {
        return timetableConfDao.isLocked(term, new ObjectId(gradeId));
    }

    /**
     * 更新课表结构配置
     *
     * @param timetableConfDTO
     */
    public void updateConf(TimetableConfDTO timetableConfDTO) {
        timetableConfDao.add(timetableConfDTO.exportEntry());
    }

    /**
     * 解锁课表配置
     *
     * @param term
     * @param gradeId
     */
    public void unlock(String term, String gradeId) {
        timetableConfDao.lock(term, new ObjectId(gradeId), 0);
    }

    /**
     * 获取不可排课事务
     *
     * @param term
     * @param gradeId
     * @return
     */
    public List<EventDTO> getEventList(String term, String gradeId) {
        TimetableConfDTO timetableConfDTO = getTimetableConf(term, new ObjectId(gradeId));
        return timetableConfDTO.getEventList();
    }

    /**
     * 根据事务id获取事务详情
     *
     * @param term
     * @param gradeId
     * @param eventId
     * @return
     */
    public EventDTO getEventDetail(String term, String gradeId, String eventId) {
        //获取课表配置
        TimetableConfEntry timetableConfEntry = timetableConfDao.getTimetableConf(term, new ObjectId(gradeId));
        //不可排事务
        List<TimetableConfEntry.Event> eventList = timetableConfEntry.getEvent();
        for (TimetableConfEntry.Event event : eventList) {
            if (event.getID().toString().equals(eventId)) {
                return new EventDTO(event);
            }
        }
        return null;
    }


    /**
     * 清除掉对应位置的课
     *
     * @param term
     * @param gradeId
     * @param eventDTO
     */
    public void removePositionTimetable(String term, String gradeId, EventDTO eventDTO) {
        //清除掉对应位置的课
        List<PointJson> pointJsons = eventDTO.getPointList();
        List<IdNameDTO> teachers = eventDTO.getTeacherList();
        //存储所有老师的课程信息Id
        List<ObjectId> courseIds = new ArrayList<ObjectId>();
        //获取全年级课表
        List<TimeTableEntry> timeTableEntryList = timeTableDao.findTimeTableByGrade(term, new ObjectId(gradeId), TimetableState.NOTPUBLISHED.getState(), 0);
        //获取所有老师所教的课程信息
        for (IdNameDTO idNameDTO : teachers) {
            List<ZouBanCourseEntry> zouBanCourseEntries = zouBanCourseDao.findCourseListByTeacherId(term, new ObjectId(idNameDTO.getId()), false);
            for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntries) {
                courseIds.add(zouBanCourseEntry.getID());
            }
        }
        for (TimeTableEntry timeTableEntry : timeTableEntryList) {
            ObjectId classId = timeTableEntry.getClassId();
            List<CourseItem> courseItems = timeTableEntry.getCourseList();
            for (CourseItem courseItem : courseItems) {
                for (PointJson pointJson : pointJsons) {
                    int x = pointJson.getX();
                    int y = pointJson.getY();
                    if (courseItem.getXIndex() == x && courseItem.getYIndex() == y) {
                        List<ObjectId> courseIdList = courseItem.getCourse();
                        for (ObjectId id : courseIdList) {
                            if (courseIds.contains(id)) {
                                //删除课程信息
                                timeTableDao.removeCourse(term, classId, 1, courseItem);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * 新增不可排课事务
     *
     * @param term
     * @param gradeId
     * @param eventDTO
     */
    public void addEvent(String term, String gradeId, EventDTO eventDTO) {
        timetableConfDao.addEvent(term, new ObjectId(gradeId), eventDTO.exportEntry());
        //删除对应的该位置的课程
        removePositionTimetable(term, gradeId, eventDTO);
    }

    /**
     * 清除掉对应位置的课表
     *
     * @param term
     * @param eventDTO
     */
    public void removePositionTimetable(String term, String gradeId, ClassEventDTO eventDTO) {
        //清除掉对应位置的课表
        int x = eventDTO.getX();
        int y = eventDTO.getY();
        List<IdNameDTO> classList = eventDTO.getClassList();
        //存储班级信息
        List<ObjectId> objectIds = new ArrayList<ObjectId>();
        for (IdNameDTO idNameDTO : classList) {
            objectIds.add(new ObjectId(idNameDTO.getId()));
        }
        //根据班级id列表查询课表
        List<TimeTableEntry> timeTableEntryList = timeTableDao.findTimeTableList(term, objectIds, 0);

        for (TimeTableEntry timeTableEntry : timeTableEntryList) {
            List<CourseItem> courseItems = timeTableEntry.getCourseList();
            for (CourseItem courseItem : courseItems) {
                if (courseItem.getXIndex() == x && courseItem.getYIndex() == y) {
                    timeTableDao.removeCourse(term, timeTableEntry.getClassId(), 1, courseItem);
                }
            }
        }
    }

    /**
     * 新增班级事务
     *
     * @param term
     * @param gradeId
     * @param eventDTO
     */
    public void addClassEvent(String term, String gradeId, ClassEventDTO eventDTO) {
        timetableConfDao.addClassEvent(term, new ObjectId(gradeId), eventDTO.exportEntry());
        //删除对应位置的课
        removePositionTimetable(term, gradeId, eventDTO);
    }


    /**
     * 更新不可排课事务
     *
     * @param term
     * @param gradeId
     * @param eventDTO
     */
    public void updateEvent(String term, String gradeId, EventDTO eventDTO) {
        //先根据老师删除冲突
        removePositionEventConflict(term, gradeId, eventDTO);
        //更新数据事务
        timetableConfDao.updateEvent(term, new ObjectId(gradeId), eventDTO.exportEntry());
        //删除对应的该位置的课程
        removePositionTimetable(term, gradeId, eventDTO);
    }

    /**
     * 根据老师删除冲突
     *
     * @param term
     * @param gradeId
     * @param eventDTO
     */
    public void removePositionEventConflict(String term, String gradeId, EventDTO eventDTO) {
        TimetableConfEntry timetableConfEntry = timetableConfDao.getTimetableConf(term, new ObjectId(gradeId));
        List<TimetableConfEntry.Event> events = timetableConfEntry.getEvent();
        TimetableConfEntry.Event event = new TimetableConfEntry.Event();
        for (TimetableConfEntry.Event event1 : events) {
            if (event1.getID().equals(new ObjectId(eventDTO.getId()))) {
                event = event1;
                break;
            }
        }

        List<PointEntry> entries = event.getPointList();
        List<IdNamePair> tes = event.getTeacherList();
        List<PointJson> pointJsons = new ArrayList<PointJson>();
        List<IdNameDTO> idNameDTOs = new ArrayList<IdNameDTO>();
        List<ObjectId> objectIds = new ArrayList<ObjectId>();
        List<ObjectId> tecs = new ArrayList<ObjectId>();
        //记录相同位置
        List<PointJson> pointJsonList = new ArrayList<PointJson>();
        for (PointEntry pointEntry : entries) {
            pointJsons.add(new PointJson(pointEntry.getX(), pointEntry.getY()));
        }
        for (IdNamePair idNamePair : tes) {
            idNameDTOs.add(new IdNameDTO(idNamePair.getId().toString(), idNamePair.getName()));
            objectIds.add(idNamePair.getId());
        }

        //判别不同位置
        for (PointJson pointJson : pointJsons) {
            boolean flag = false;
            for (PointJson pointJson1 : eventDTO.getPointList()) {
                if (pointJson.getX() == pointJson1.getX() && pointJson.getY() == pointJson1.getY()) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                eventConflictService.removeConflictByTeacherList(term, new ObjectId(gradeId), pointJson.getX(), pointJson.getY(), objectIds);
            } else {
                pointJsonList.add(new PointJson(pointJson.getX(), pointJson.getY()));
            }
        }

        //判别相同位置不同的老师Id
        for (IdNameDTO idNameDTO : idNameDTOs) {
            boolean flag = false;
            for (IdNameDTO idNameDTO1 : eventDTO.getTeacherList()) {
                if (idNameDTO.getId().equals(idNameDTO1.getId())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                tecs.add(new ObjectId(idNameDTO.getId()));
            }
        }

        //循环删除相同位置不同老师冲突
        for (PointJson pointJson : pointJsonList) {
            eventConflictService.removeConflictByTeacherList(term, new ObjectId(gradeId), pointJson.getX(), pointJson.getY(), tecs);
        }

    }

    /**
     * 删除事务
     *
     * @param term
     * @param event
     */
    public void removeEvent(String term, TimetableConfEntry.Event event, String schoolId) {
        String eventName = event.getName();
        List<PointEntry> pointList = event.getPointList();
        List<IdNamePair> teacherList = event.getTeacherList();

        List<GradeView> gradeViewList = schoolService.findGradeList(schoolId);

        for (GradeView gv : gradeViewList) {
            ObjectId gradeId = new ObjectId(gv.getId());
            TimetableConfEntry entry = timetableConfDao.findConfByEvent(term, gradeId, eventName, pointList, teacherList);

            for (TimetableConfEntry.Event e : entry.getEvent()) {
                String eName = e.getName();
                List<PointEntry> pl = e.getPointList();
                List<IdNamePair> tl = e.getTeacherList();

                if (eventName.equals(eName) &&
                        checkPointList(pointList, pl) &&
                        checkTeacherList(teacherList, tl)) {
                    //根据事务Id删除冲突
                    eventConflictService.removeEventConflictByEventId(e.getID());
                    break;
                }
            }
        }
        //删除事务
        timetableConfDao.removeEvent(term, eventName, pointList, teacherList);
    }

    //检查时间点是否相同
    private boolean checkPointList(List<PointEntry> pl1, List<PointEntry> pl2) {
        if (pl1.size() != pl2.size()) {
            return false;
        }

        pl2.retainAll(pl1);
        if (pl2.size() != pl1.size()) {
            return false;
        }
        return true;
    }

    //检查老师是否相同
    private boolean checkTeacherList(List<IdNamePair> tl, List<IdNamePair> tl2) {
        if (tl.size() != tl2.size()) {
            return false;
        }

        for (int i = 0; i < tl.size(); i++) {
            if (!tl.get(i).getId().equals(tl2.get(i).getId())) {
                return false;
            }
        }

        return true;
    }


    /**
     * 删除班级事务
     *
     * @param term
     * @param gradeId
     */
    public void removeClassEvent(String term, String gradeId, int x, int y) {
        timetableConfDao.removeClassEvent(term, new ObjectId(gradeId), x, y);
    }

    /**
     * 获取某个时间点的所有不可排课事务
     *
     * @param term
     * @param gradeId
     * @param x
     * @param y
     * @return
     */
    public List<EventDetailDTO> eventDetailList(String term, String gradeId, int x, int y) {
        //获取课表配置
        TimetableConfEntry timetableConfEntry = timetableConfDao.getTimetableConf(term, new ObjectId(gradeId));
        //不可排事务
        List<TimetableConfEntry.Event> eventList = timetableConfEntry.getEvent();

        List<EventDetailDTO> eventDetailDTOList = new ArrayList<EventDetailDTO>();
        for (TimetableConfEntry.Event event : eventList) {
            if (event.getPointList().contains(new PointEntry(x, y))) {
                EventDetailDTO detailDTO = new EventDetailDTO();
                detailDTO.setName(event.getName());
                List<String> teacherList = new ArrayList<String>();
                for (IdNamePair inp : event.getTeacherList()) {
                    teacherList.add(inp.getName());
                }
                eventDetailDTOList.add(detailDTO);
            }
        }

        return eventDetailDTOList;
    }

    /**
     * 清空课表
     *
     * @param term
     * @param gradeId
     */
    public void clearTimetable(String term, String gradeId) {
        //清空本学期本年级课表
        timeTableDao.deleteTimetableByGradeId(term, new ObjectId(gradeId), 0);
    }

    /**
     * 生成本学期原始课表(第0周课表)
     *
     * @param term
     * @param gradeId
     */
    public void createTimetable(String term, String gradeId, String schoolId) {
        List<ClassInfoDTO> classList = classService.findClassByGradeId(gradeId);

        for (ClassInfoDTO classInfoDTO : classList) {
            TimeTableEntry timeTableEntry = new TimeTableEntry(term, new ObjectId(schoolId), new ObjectId(gradeId),
                    new ObjectId(classInfoDTO.getId()), null, TimetableState.NOTPUBLISHED.getState(), 0, 0);
            timeTableDao.addTimeTable(timeTableEntry);
        }
    }

    /**
     * 删除全校事务
     *
     * @param schoolId
     * @param term
     */
    public void removeAllEvent(String schoolId, String term) {
        List<GradeView> gradeList = schoolService.findGradeList(schoolId);
        List<ObjectId> gradeIdList = new ArrayList<ObjectId>();

        for (GradeView gv : gradeList) {
            gradeIdList.add(new ObjectId(gv.getId()));
        }
        timetableConfDao.removeAllEvent(term, gradeIdList);
    }


}
