package com.fulaan.zouban.service;

import com.db.school.ClassDao;
import com.db.school.TeacherClassSubjectDao;
import com.db.user.UserDao;
import com.db.zouban.*;
import com.fulaan.classroom.service.ClassroomService;
import com.fulaan.examresult.controller.IdNameDTO;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.school.service.SchoolService;
import com.fulaan.zouban.dto.*;
import com.mongodb.BasicDBObject;
import com.mongodb.gridfs.CLI;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.classroom.ClassroomEntry;
import com.pojo.school.ClassEntry;
import com.pojo.school.ClassInfoDTO;
import com.pojo.school.Subject;
import com.pojo.school.TeacherClassSubjectEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.pojo.zouban.*;
import org.bson.types.ObjectId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.List;


/**
 * Created by qiangm on 2015/9/25.
 */
@Service
public class PaikeService {
    private TimeTableDao timeTableDao = new TimeTableDao();
    private ZouBanCourseDao zoubanCourseDao = new ZouBanCourseDao();
    private FenDuanDao fenDuanDao = new FenDuanDao();
    private UserDao userDao = new UserDao();
    private TeacherClassSubjectDao teacherClassSubjectDao = new TeacherClassSubjectDao();


    //@Autowired
    private ClassService classService = new ClassService();
    @Autowired
    private ClassroomService classroomService;
    @Autowired
    private ZoubanStateService zoubanStateService;
    @Autowired
    private TimetableConfService timetableConfService;
    @Autowired
    private EventConflictService eventConflictService;

    //=====================================================排走班课======================================================

    /**
     * 获取走班课已排课课表
     *
     * @param term
     * @param groupId
     * @param mode    走班模式 1：逻辑位置模式， 2：虚拟班模式
     * @return
     */
    public List<CourseItemDTO> getArrangedZBCourse(String term, ObjectId gradeId, ObjectId groupId, int mode) {
        //逻辑位置模式：通过groupId查找课表，每个分段每个班的课表都一样，只需要取一个班的课表；
        //虚拟班模式：不分段，不需要groupId，每个班走班课表可能不一样，需要查找全年级课表并合并；

        //课表item
        List<CourseItemDTO> courseItemDTOList = new ArrayList<CourseItemDTO>();
        //已排课课程id
        Set<ObjectId> courseIdSet = new HashSet<ObjectId>();
        if (mode == 1) {
            //分段
            ClassFengDuanEntry fengDuanEntry = fenDuanDao.findFenDuanById(groupId);
            //课表
            TimeTableEntry timeTableEntry = timeTableDao.findTimeTable(term, fengDuanEntry.getClassIds().get(0), TimetableState.NOTPUBLISHED.getState(), 0);
            TimeTableDTO timeTableDTO = new TimeTableDTO(timeTableEntry);

            for (CourseItemDTO itemDTO : timeTableDTO.getCourseList()) {
                if (itemDTO.getType() == ZoubanType.ZOUBAN.getType()) {
                    courseItemDTOList.add(itemDTO);
                }
            }

            for (CourseItem courseItem : timeTableEntry.getCourseList()) {
                for (ObjectId courseId : courseItem.getCourse()) {
                    courseIdSet.add(courseId);
                }
            }
        } else {
            //全年级课表
            List<TimeTableEntry> timeTableEntryList = timeTableDao.findTimeTableByGrade(term, gradeId, TimetableState.NOTPUBLISHED.getState(), 0);
            Map<PointJson, CourseItemDTO> pointCourseMap = new HashMap<PointJson, CourseItemDTO>();

            //合并课表
            for (TimeTableEntry timeTableEntry : timeTableEntryList) {
                TimeTableDTO timeTableDTO = new TimeTableDTO(timeTableEntry);

                for (CourseItemDTO itemDTO : timeTableDTO.getCourseList()) {
                    if (itemDTO.getType() == ZoubanType.ZOUBAN.getType()) {
                        PointJson p = new PointJson(itemDTO.getxIndex(), itemDTO.getyIndex());
                        List<CourseTeacherRoom> courseList = itemDTO.getCourseIdList();

                        for (CourseTeacherRoom courseTeacherRoom : courseList) {
                            courseIdSet.add(new ObjectId(courseTeacherRoom.getCourseId()));
                        }

                        if (pointCourseMap.containsKey(p)) {
                            CourseItemDTO oldItem = pointCourseMap.get(p);
                            List<CourseTeacherRoom> oldCourseList = oldItem.getCourseIdList();
                            oldItem.setCourseIdList(mergeItem(oldCourseList, courseList));
                            pointCourseMap.put(p, oldItem);
                        } else {
                            pointCourseMap.put(p, itemDTO);
                        }
                    }
                }
            }
            courseItemDTOList.addAll(pointCourseMap.values());
        }

        setCourseTeacherAndClassroom(courseItemDTOList, courseIdSet);

        return courseItemDTOList;
    }

    /**
     * 合并课表项
     *
     * @param oldCourseList
     * @param nowCourseList
     * @return
     */
    private List<CourseTeacherRoom> mergeItem(List<CourseTeacherRoom> oldCourseList, List<CourseTeacherRoom> nowCourseList) {
        Map<String, CourseTeacherRoom> courseIdTeacherRoomMap = new HashMap<String, CourseTeacherRoom>();

        for (CourseTeacherRoom courseTeacherRoom : oldCourseList) {
            courseIdTeacherRoomMap.put(courseTeacherRoom.getCourseId(), courseTeacherRoom);
        }
        for (CourseTeacherRoom courseTeacherRoom : nowCourseList) {
            if (!courseIdTeacherRoomMap.containsKey(courseTeacherRoom.getCourseId())) {
                courseIdTeacherRoomMap.put(courseTeacherRoom.getCourseId(), courseTeacherRoom);
            }
        }
        return new ArrayList<CourseTeacherRoom>(courseIdTeacherRoomMap.values());
    }

    /**
     * 设置已排走班课的老师和教室
     *
     * @param courseItemDTOList
     * @param courseIdSet
     */
    private void setCourseTeacherAndClassroom(List<CourseItemDTO> courseItemDTOList, Set<ObjectId> courseIdSet) {
        //获取已排课课程信息
        List<ZouBanCourseEntry> zouBanCourseEntries = zoubanCourseDao.findCourseListByIds(new ArrayList<ObjectId>(courseIdSet));
        //课程id-entry Map
        Map<String, ZouBanCourseEntry> zouBanCourseEntryMap = new HashMap<String, ZouBanCourseEntry>();
        //教室id列表
        List<ObjectId> classroomIds = new ArrayList<ObjectId>();
        for (ZouBanCourseEntry entry : zouBanCourseEntries) {
            zouBanCourseEntryMap.put(entry.getID().toString(), entry);
            classroomIds.add(entry.getClassRoomId());
        }

        //教室id-entry Map
        Map<ObjectId, ClassroomEntry> classroomMap = classroomService.findClassRoomEntryMap(classroomIds);

        //获取classroomEntry
        //填充教室名字信息
        for (CourseItemDTO courseItemDTO : courseItemDTOList) {
            for (CourseTeacherRoom ctr : courseItemDTO.getCourseIdList()) {
                ZouBanCourseEntry entry = zouBanCourseEntryMap.get(ctr.getCourseId());
                if (ctr.getCourseId() != null && !ctr.getCourseId().equals("")) {
                    ctr.setCourseName(entry.getClassName());
                }
                if (entry.getClassRoomId() != null) {
                    ctr.setClassRoomId(entry.getClassRoomId().toString());
                    if (classroomMap.containsKey(new ObjectId(ctr.getClassRoomId()))) {
                        ctr.setClassRoom(classroomMap.get(new ObjectId(ctr.getClassRoomId())).getRoomName());
                    }
                }

                ctr.setTeacherId(entry.getTeacherId().toString());
                ctr.setTeacherName(zouBanCourseEntryMap.get(ctr.getCourseId()).getTeacherName());
            }
        }
    }


    /**
     * 获取待排走班课
     *
     * @param term
     * @param gradeId
     * @param groupId
     * @param mode    走班模式, 1 : 逻辑位置模式， 2 ： 虚拟班模式
     * @return
     */
    public List<List<IdNameDTO>> getArrangingZBCourse(String term, String gradeId, String groupId, int mode) throws Exception {
        //第一步：获取所有走班课
        //第二步：过滤已排课课时

        //未分配走班课组合列表
        List<List<IdNameDTO>> groupCourseList = new ArrayList<List<IdNameDTO>>();

        if (mode == 1) {
            ClassFengDuanEntry fengDuanEntry = fenDuanDao.findFenDuanById(new ObjectId(groupId));
            TimeTableEntry timeTableEntry = timeTableDao.findTimeTable(term, fengDuanEntry.getClassIds().get(0), TimetableState.NOTPUBLISHED.getState(), 0);

            if (timeTableEntry == null) {
                throw new Exception("请先保存课表结构！");
            }

            //本段走班课
            List<ZouBanCourseEntry> zouBanCourseEntryList = zoubanCourseDao.findCourseList(term, new ObjectId(gradeId), new ObjectId(groupId));
            //逻辑位置-课程Map
            Map<ObjectId, List<ZouBanCourseEntry>> groupCourseMap = new HashMap<ObjectId, List<ZouBanCourseEntry>>();

            //按照逻辑位置分组
            for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntryList) {
                if (zouBanCourseEntry.getTeacherId() == null) {
                    throw new Exception("请先分配老师");
                }
                if (zouBanCourseEntry.getClassRoomId() == null) {
                    throw new Exception("请先设置教室");
                }

                ObjectId group = zouBanCourseEntry.getGroup();
                if (groupCourseMap.containsKey(group)) {
                    groupCourseMap.get(group).add(zouBanCourseEntry);
                } else {
                    List<ZouBanCourseEntry> list = new ArrayList<ZouBanCourseEntry>();
                    list.add(zouBanCourseEntry);
                    groupCourseMap.put(zouBanCourseEntry.getGroup(), list);
                }
            }

            for (Map.Entry entry : groupCourseMap.entrySet()) {
                //逻辑位置的所有课
                List<ZouBanCourseEntry> list = (ArrayList<ZouBanCourseEntry>) entry.getValue();
                //课时-课程列表Map
                Map<Integer, List<ZouBanCourseEntry>> classCountEntryMap = new HashMap<Integer, List<ZouBanCourseEntry>>();

                for (ZouBanCourseEntry zouBanCourseEntry : list) {
                    int lessonCount = zouBanCourseEntry.getLessonCount();

                    //剔除已排课课时
                    for (CourseItem item : timeTableEntry.getCourseList()) {
                        if (item.getType() == ZoubanType.ZOUBAN.getType()) {
                            if (item.getCourse().contains(zouBanCourseEntry.getID())) {
                                lessonCount--;
                            }
                        }
                    }

                    for (int i = 0; i < lessonCount; i++) {
                        if (classCountEntryMap.containsKey(i)) {
                            classCountEntryMap.get(i).add(zouBanCourseEntry);
                        } else {
                            List<ZouBanCourseEntry> entryList = new ArrayList<ZouBanCourseEntry>();
                            entryList.add(zouBanCourseEntry);
                            classCountEntryMap.put(i, entryList);
                        }
                    }
                }

                for (int i = 0; i < classCountEntryMap.size(); i++) {


                    List<ZouBanCourseEntry> entryList = classCountEntryMap.get(i);
                    List<IdNameDTO> IdNameDTOList = new ArrayList<IdNameDTO>();

                    for (ZouBanCourseEntry zouBanCourseEntry : entryList) {
                        IdNameDTOList.add(new IdNameDTO(zouBanCourseEntry.getID().toString(), zouBanCourseEntry.getClassName()));
                    }
                    groupCourseList.add(IdNameDTOList);
                }
            }
        } else {
            //获取本年级走班课
            List<ZouBanCourseEntry> zouBanCourseEntryList = zoubanCourseDao.findCourseList(term, new ObjectId(gradeId), ZoubanType.ZOUBAN.getType());

            for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntryList) {
                //总课时
                int lessonCount = zouBanCourseEntry.getLessonCount();
                List<ObjectId> classIdList = zouBanCourseEntry.getClassId();
                List<TimeTableEntry> timeTableEntryList = timeTableDao.findTimeTableList(term, classIdList, 0);
                if (timeTableEntryList.size() == 0) {
                    throw new Exception("请先保存课表结构并给教学班分配学生");
                }
                List<CourseItem> courseItemList = timeTableEntryList.get(0).getCourseList();
                for (CourseItem courseItem : courseItemList) {
                    if (courseItem.getCourse().contains(zouBanCourseEntry.getID())) {
                        lessonCount--;
                    }
                }
                for (int i = 0; i < lessonCount; i++) {
                    List<IdNameDTO> courseList = new ArrayList<IdNameDTO>();
                    courseList.add(new IdNameDTO(zouBanCourseEntry.getID().toString(), zouBanCourseEntry.getClassName()));
                    groupCourseList.add(courseList);
                }
            }
        }

        return groupCourseList;
    }


    /**
     * 走班获取可用时间点
     *
     * @param term
     * @param gradeId
     * @param courseIdStr
     * @return
     */
    public List<PointJson> getAvailablePointForZB(String term, ObjectId schoolId, String gradeId, String courseIdStr) throws Exception{
        return getAvailablePoint(term, schoolId, gradeId, courseIdStr, ZoubanType.ZOUBAN.getType());
    }

    /**
     * 获取可用时间点列表
     *
     * @param term
     * @param schoolId
     * @param gradeId
     * @param courseIdStr
     * @param type
     * @return
     */
    private List<PointJson> getAvailablePoint(String term, ObjectId schoolId, String gradeId, String courseIdStr, int type) throws Exception{
        String[] courseIds = courseIdStr.split(",");
        List<ObjectId> teacherIdList = new ArrayList<ObjectId>();
        List<ObjectId> classroomIdList = new ArrayList<ObjectId>();
        Set<ObjectId> classIdSet = new HashSet<ObjectId>();
        Set<ObjectId> studentIdSet = new HashSet<ObjectId>();

        for (String courseId : courseIds) {
            ZouBanCourseEntry zouBanCourseEntry = zoubanCourseDao.getCourseInfoById(new ObjectId(courseId));
            if (zouBanCourseEntry.getTeacherId() == null) {
                throw new Exception(zouBanCourseEntry.getClassName() + "未设置老师");
            }
            teacherIdList.add(zouBanCourseEntry.getTeacherId());
            if (type != ZoubanType.PE.getType()) {
                classroomIdList.add(zouBanCourseEntry.getClassRoomId());
            }
            classIdSet.addAll(zouBanCourseEntry.getClassId());
            studentIdSet.addAll(zouBanCourseEntry.getStudentList());
        }
        List<ObjectId> classIdList = new ArrayList<ObjectId>(classIdSet);

        return getAvailablePoint(term, schoolId, gradeId, type, classIdList, teacherIdList, classroomIdList, new ArrayList<ObjectId>(studentIdSet));
    }

    /**
     * 获取所有可用时间点
     *
     * @param term
     * @param schoolId
     * @param gradeId
     * @param type
     * @param classIdList
     * @param teacherIdList
     * @param classroomIdList
     * @return
     */
    private List<PointJson> getAvailablePoint(String term, ObjectId schoolId, String gradeId, int type,
                                              List<ObjectId> classIdList, List<ObjectId> teacherIdList,
                                              List<ObjectId> classroomIdList, List<ObjectId> studentIdList) {
        //课表配置
        TimetableConfDTO timetableConfDTO = timetableConfService.getTimetableConf(term, new ObjectId(gradeId));
        //班级事务
        List<ClassEventDTO> classEventDTOList = timetableConfDTO.getClassEventList();
        //个人事务列表
        List<EventDTO> eventDTOList = timetableConfDTO.getEventList();
        //每天上课节数
        int classCount = timetableConfDTO.getClassCount();
        //上课天数
        List<Integer> days = timetableConfDTO.getDays();


        //全校课表中的所有课
        List<TimeTableEntry> timeTableEntryList = timeTableDao.findAllTimeTable(term, schoolId, TimetableState.NOTPUBLISHED.getState(), 0);
        Map<PointJson, Set<ObjectId>> courseItemMap = new HashMap<PointJson, Set<ObjectId>>();

        for (TimeTableEntry timeTableEntry : timeTableEntryList) {
            for (CourseItem courseItem : timeTableEntry.getCourseList()) {
                PointJson p = new PointJson(courseItem.getXIndex(), courseItem.getYIndex());
                if (courseItemMap.containsKey(p)) {
                    courseItemMap.get(p).addAll(courseItem.getCourse());
                } else {
                    courseItemMap.put(p, new HashSet<ObjectId>(courseItem.getCourse()));
                }
            }
        }


        //遍历课表，查找所有可用时间点
        List<PointJson> pointList = new ArrayList<PointJson>();
        for (int x : days) {
            for (int y = 1; y <= classCount; y++) {

                if (checkClassEvent(classEventDTOList, classIdList, x, y) &&
                        chechEvent(eventDTOList, x, y, teacherIdList)) {//检查事务
                    PointJson point = new PointJson(x, y);
                    if (courseItemMap.containsKey(point)) {//有课，检查是否冲突
                        if (checkConflict(courseItemMap.get(point), teacherIdList, classroomIdList, studentIdList)) {
                            pointList.add(point);
                        }
                    } else {//无课，直接添加
                        pointList.add(point);
                    }
                }
            }
        }
        return pointList;
    }

    /**
     * 检查班级事务（如果本时间点相关班级有班级事务，则时间点不可用）
     *
     * @param classEventDTOList
     * @param classIdList
     * @param x
     * @param y
     * @return
     */
    public boolean checkClassEvent(List<ClassEventDTO> classEventDTOList, List<ObjectId> classIdList, int x, int y) {
        for (ClassEventDTO classEventDTO : classEventDTOList) {
            if (classEventDTO.getX() == x && classEventDTO.getY() == y) {
                List<IdNameDTO> classIds = classEventDTO.getClassList();
                for (IdNameDTO cls : classIds) {
                    if (classIdList.contains(new ObjectId(cls.getId()))) {
                        return false;
                    }
                }
                break;
            }
        }
        return true;
    }


    /**
     * 检查事务（教研、老师个人事务等）
     *
     * @param eventDTOList
     * @param x
     * @param y
     * @param teacherIdList
     * @return
     */
    private boolean chechEvent(List<EventDTO> eventDTOList, int x, int y, List<ObjectId> teacherIdList) {
        //检查事务是否冲突
        for (EventDTO eventDTO : eventDTOList) {
            List<PointJson> eventPointList = eventDTO.getPointList();

            if (eventPointList.contains(new PointJson(x, y))) {
                List<IdNameDTO> eventTeacherList = eventDTO.getTeacherList();
                for (IdNameDTO idNameDTO : eventTeacherList) {
                    if (teacherIdList.contains(new ObjectId(idNameDTO.getId()))) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * 检查该时间点是否可用（检查老师冲突、教室冲突和学生冲突）
     *
     * @param existCourseIds
     * @param teacherIdList
     * @param classroomIdList
     * @param studentIdList
     * @return
     */
    private boolean checkConflict(Collection<ObjectId> existCourseIds, List<ObjectId> teacherIdList,
                                  List<ObjectId> classroomIdList, List<ObjectId> studentIdList) {
        //本时间点已有的课
        List<ZouBanCourseEntry> existCourseList = zoubanCourseDao.findCourseListByIds(new ArrayList<ObjectId>(existCourseIds));

        if (existCourseIds.size() > 0) {
            Set<ObjectId> existTeacherIdList = new HashSet<ObjectId>();
            Set<ObjectId> existClassroomIdList = new HashSet<ObjectId>();
            Set<ObjectId> existStudentIdList = new HashSet<ObjectId>();

            for (ZouBanCourseEntry zouBanCourseEntry : existCourseList) {
                existTeacherIdList.add(zouBanCourseEntry.getTeacherId());
                existClassroomIdList.add(zouBanCourseEntry.getClassRoomId());
                existStudentIdList.addAll(zouBanCourseEntry.getStudentList());
            }


            if (classroomIdList == null || classroomIdList.size() == 0) {//体育课只需检查老师&学生是否冲突
                existTeacherIdList.retainAll(teacherIdList);
                existStudentIdList.retainAll(studentIdList);
                if (existTeacherIdList.size() > 0 ||
                        existStudentIdList.size() > 0) {
                    return false;
                }
            } else {//检查老师、学生、教室是否冲突
                existTeacherIdList.retainAll(teacherIdList);
                existStudentIdList.retainAll(studentIdList);
                existClassroomIdList.retainAll(classroomIdList);

                if (existTeacherIdList.size() > 0 ||
                        existClassroomIdList.size() > 0 ||
                        existStudentIdList.size() > 0) {
                    return false;
                }
            }
        }

        return true;
    }


    /**
     * 添加课程
     *
     * @param term
     * @param gradeId
     * @param x
     * @param y
     * @param courseIdStr
     * @param type
     */
    public void addCourse(String term, String gradeId, int x, int y, String courseIdStr, int type) throws Exception{
        //教学班id集合
        Set<ObjectId> courseIdSet = new HashSet<ObjectId>();
        String[] courseIds = courseIdStr.split(",");
        for (String cid : courseIds) {
            courseIdSet.add(new ObjectId(cid));
        }

        List<ObjectId> courseIdList = new ArrayList<ObjectId>(courseIdSet);

        //行政班
        Set<ObjectId> classIdSet = new HashSet<ObjectId>();

        List<ZouBanCourseEntry> courseEntryList = zoubanCourseDao.findCourseListByIds(courseIdList);
        for (ZouBanCourseEntry zouBanCourseEntry : courseEntryList) {
            if (zouBanCourseEntry.getTeacherId() == null) {
                throw new Exception(zouBanCourseEntry.getClassName() + "未设置老师");
            }

            classIdSet.addAll(zouBanCourseEntry.getClassId());
        }

        if (gradeId == null) {
            gradeId = courseEntryList.get(0).getGradeId().toString();
        }

        //检查并添加事务冲突
        eventConflictService.addConflict(term, new ObjectId(gradeId), x, y, courseIdSet);

        for (ObjectId classId : classIdSet) {
            // 新增
            CourseItem courseItem = new CourseItem(new ObjectId(), x, y, courseIdList, type);
            timeTableDao.addCourse(term, classId, courseItem);
        }
    }


    /**
     * 新增走班课
     *
     * @param term
     * @param xIndex
     * @param yIndex
     * @param courseIdStr
     */
    public void addZBCourse(String term, String gradeId, String groupId, int xIndex, int yIndex, String courseIdStr, int mode) {
        //行政班
        Set<ObjectId> classIdSet = new HashSet<ObjectId>();

        //教学班集合
        Set<ObjectId> courseSet = new HashSet<ObjectId>();
        String[] courseIds = courseIdStr.split(",");
        for (String cid : courseIds) {
            courseSet.add(new ObjectId(cid));
        }

        //检查并添加事务冲突
        eventConflictService.addConflict(term, new ObjectId(gradeId), xIndex, yIndex, courseSet);

        if (mode == 1) {
            ClassFengDuanEntry fengDuanEntry = fenDuanDao.findFenDuanById(new ObjectId(groupId));
            classIdSet.addAll(fengDuanEntry.getClassIds());
        } else {
            List<ZouBanCourseEntry> zouBanCourseEntryList = zoubanCourseDao.findCourseListByIds(new ArrayList<ObjectId>(courseSet));
            for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntryList) {
                classIdSet.addAll(zouBanCourseEntry.getClassId());
            }
        }

        for (ObjectId classId : classIdSet) {
            TimeTableEntry timeTableEntry = timeTableDao.findTimeTable(term, classId, TimetableState.NOTPUBLISHED.getState(), 0);
            List<ObjectId> courseIdList = new ArrayList<ObjectId>(courseSet);
            CourseItem courseItem = null;

            for (CourseItem item : timeTableEntry.getCourseList()) {
                if (item.getXIndex() == xIndex && item.getYIndex() == yIndex) {
                    courseItem = item;
                    break;
                }
            }

            if (courseItem == null) {// 新增
                courseItem = new CourseItem(new ObjectId(), xIndex, yIndex,
                        courseIdList, ZoubanType.ZOUBAN.getType());
                timeTableDao.addCourse(term, classId, courseItem);
            } else {//更新
                courseIdList.addAll(courseItem.getCourse());
                timeTableDao.updateCourseItem(term, classId, courseItem.getId(), courseIdList);
            }
        }
    }


    /**
     * 删除课表中某个位置的走班课
     *
     * @param term
     * @param groupId
     * @param x
     * @param y
     */
    public void removeZBCourse(String term, String gradeId, String groupId, int x, int y) {
        ClassFengDuanEntry fengDuanEntry = fenDuanDao.findFenDuanById(new ObjectId(groupId));
        //删除冲突
        removeConflict(term, new ObjectId(gradeId), x, y, ZoubanType.ZOUBAN.getType());
        //删除课程
        timeTableDao.removeCourse(term, fengDuanEntry.getClassIds(), x, y);
    }

    /**
     * 删除事务冲突
     *
     * @param term
     * @param gradeId
     * @param x
     * @param y
     * @param type
     */
    private void removeConflict(String term, ObjectId gradeId, int x, int y, int type) {
        List<TimeTableEntry> timeTableEntryList = timeTableDao.findTimeTableByGrade(term, gradeId, 0, 0);
        Set<ObjectId> courseIdSet = new HashSet<ObjectId>();

        for (TimeTableEntry timeTableEntry : timeTableEntryList) {
            for (CourseItem item : timeTableEntry.getCourseList()) {
                if (item.getType() == type && item.getXIndex() == x && item.getYIndex() == y) {
                    courseIdSet.addAll(item.getCourse());
                    break;
                }
            }
        }
        eventConflictService.removeConflictByCourseList(term, gradeId, x, y, courseIdSet);
    }

    /**
     * 清空课表中的课程
     *
     * @param term
     * @param gradeId
     * @param type
     * @param classId
     */
    public void clearTimetableCourse(String term, String gradeId, int type, String classId) {
        if (type == 3) {//只清空本班的非走班课
            List<ObjectId> classIdList = new ArrayList<ObjectId>();
            classIdList.add(new ObjectId(classId));
            timeTableDao.removeCourse(term, classIdList, ZoubanType.FEIZOUBAN.getType());
            timeTableDao.removeCourse(term, classIdList, ZoubanType.ODDEVEN.getType());
        } else {
            ObjectId gid = new ObjectId(gradeId);

            if (type == 1) {//清空走班课
                timeTableDao.removeCourse(term, gid, ZoubanType.ZOUBAN.getType());
            } else if (type == 2) {//清空体育课
                timeTableDao.removeCourse(term, gid, ZoubanType.PE.getType());
            } else if (type == 4) {//清空全年级的非走班课
                timeTableDao.removeCourse(term, gid, ZoubanType.FEIZOUBAN.getType());
            } else if (type == 5) {//清空分层走班课
                timeTableDao.removeCourse(term, gid, ZoubanType.GROUPZOUBAN.getType());
            } else if (type == 6) {//清空全年级的单双周课
                timeTableDao.removeCourse(term, gid, ZoubanType.ODDEVEN.getType());
            }
        }
    }



    //============================================排分组走班排课============================================================

    /**
     * 获取分组走班课已排课表
     *
     * @param term
     * @param gradeId
     * @return
     */
    public List<CourseItemDTO> getArrangedFZZBCourse(String term, ObjectId gradeId) {
        //课表item
        List<CourseItemDTO> courseItemDTOList = new ArrayList<CourseItemDTO>();
        //已排课课程id
        Set<ObjectId> courseIdSet = new HashSet<ObjectId>();

        List<TimeTableEntry> timeTableEntryList = timeTableDao.findTimeTableByGrade(term, gradeId, TimetableState.NOTPUBLISHED.getState(), 0);
        Map<PointJson, CourseItemDTO> pointCourseMap = new HashMap<PointJson, CourseItemDTO>();

        //合并课表
        for (TimeTableEntry timeTableEntry : timeTableEntryList) {
            TimeTableDTO timeTableDTO = new TimeTableDTO(timeTableEntry);

            for (CourseItemDTO itemDTO : timeTableDTO.getCourseList()) {
                if (itemDTO.getType() == ZoubanType.GROUPZOUBAN.getType()) {
                    PointJson p = new PointJson(itemDTO.getxIndex(), itemDTO.getyIndex());
                    List<CourseTeacherRoom> courseList = itemDTO.getCourseIdList();

                    for (CourseTeacherRoom courseTeacherRoom : courseList) {
                        courseIdSet.add(new ObjectId(courseTeacherRoom.getCourseId()));
                    }

                    if (pointCourseMap.containsKey(p)) {
                        CourseItemDTO oldItem = pointCourseMap.get(p);
                        List<CourseTeacherRoom> oldCourseList = oldItem.getCourseIdList();
                        oldItem.setCourseIdList(mergeItem(oldCourseList, courseList));
                        pointCourseMap.put(p, oldItem);
                    } else {
                        pointCourseMap.put(p, itemDTO);
                    }
                }
            }
        }
        courseItemDTOList.addAll(pointCourseMap.values());

        setCourseTeacherAndClassroom(courseItemDTOList, courseIdSet);

        return courseItemDTOList;
    }


    /**
     * 获取待排分组走班课
     *
     * @param term
     * @param gradeId
     * @return
     */
    public List<List<IdNameDTO>> getArrangingFZZBCourse(String term, ObjectId gradeId) throws Exception {
        //第一步：获取所有分组走班课
        //第二步：过滤已排课课时

        //教学班类型
        final int type = ZoubanType.GROUPZOUBAN.getType();

        //未分配分组走班课列表
        List<List<IdNameDTO>> groupCourseList = new ArrayList<List<IdNameDTO>>();

        //全年级的分组走班课
        List<ZouBanCourseEntry> zouBanCourseEntryList = zoubanCourseDao.findCourseList(term, gradeId, type);
        //分组-教学班Map
        Map<ObjectId, List<ZouBanCourseEntry>> groupCourseMap = new HashMap<ObjectId, List<ZouBanCourseEntry>>();

        //按照分组组合教学班
        for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntryList) {
            if (zouBanCourseEntry.getTeacherId() == null) {
                throw new Exception("请设置老师");
            }

            if (zouBanCourseEntry.getClassRoomId() == null) {
                throw new Exception("请设置教室");
            }

            List<ZouBanCourseEntry> list = new ArrayList<ZouBanCourseEntry>();
            list.add(zouBanCourseEntry);

            if (groupCourseMap.containsKey(zouBanCourseEntry.getGroup())) {
                list.addAll(groupCourseMap.get(zouBanCourseEntry.getGroup()));
            }

            groupCourseMap.put(zouBanCourseEntry.getGroup(), list);
        }

        //所有未分配课时
        for (Map.Entry<ObjectId, List<ZouBanCourseEntry>> entry : groupCourseMap.entrySet()) {
            List<ZouBanCourseEntry> courseEntryList = entry.getValue();
            List<ObjectId> courseIdList = new ArrayList<ObjectId>();

            for (ZouBanCourseEntry zouBanCourseEntry : courseEntryList) {
                courseIdList.add(zouBanCourseEntry.getID());
            }

            //总课时
            int lessonCount = courseEntryList.get(0).getLessonCount();
            //关联行政班课表
            List<TimeTableEntry> timeTableEntryList = timeTableDao.findTimeTableList(term, courseEntryList.get(0).getClassId(), 0);

            //过滤已排课时
            for (CourseItem item : timeTableEntryList.get(0).getCourseList()) {
                if (item.getType() == type) {
                    List<ObjectId> courseIds = item.getCourse();
                    courseIds.retainAll(courseIdList);
                    if (courseIds.size() == courseIdList.size()) {
                        lessonCount--;
                    }
                }
            }

            for (int i = 0; i < lessonCount; i++) {
                List<IdNameDTO> IdNameDTOList = new ArrayList<IdNameDTO>();
                for (ZouBanCourseEntry zouBanCourseEntry : courseEntryList) {
                    IdNameDTOList.add(new IdNameDTO(zouBanCourseEntry.getID().toString(), zouBanCourseEntry.getClassName()));
                }
                groupCourseList.add(IdNameDTOList);
            }
        }

        return groupCourseList;
    }

    /**
     * 获取分组走班课可用时间点
     *
     * @param term
     * @param schoolId
     * @param gradeId
     * @param courseIdStr
     * @return
     */
    public List<PointJson> getAvailablePointForFZZB(String term, ObjectId schoolId, String gradeId, String courseIdStr) throws Exception{
        return getAvailablePoint(term, schoolId, gradeId, courseIdStr, ZoubanType.GROUPZOUBAN.getType());
    }


    /**
     * 添加分组走班课
     *
     * @param term
     * @param courseIdStr
     * @param x
     * @param y
     */
    public void addFZZBCourse(String term, String courseIdStr, int x, int y) throws Exception{
        addCourse(term, null, x, y, courseIdStr, ZoubanType.GROUPZOUBAN.getType());
    }


    /**
     * 删除分组走班课
     *
     * @param term
     * @param gradeId
     * @param x
     * @param y
     */
    public void removeFZZBCourse(String term, ObjectId gradeId, int x, int y) {
        //删除事务冲突
        removeConflict(term, gradeId, x, y, ZoubanType.GROUPZOUBAN.getType());
        //删除课时
        timeTableDao.removeCourse(term, gradeId, x, y, ZoubanType.GROUPZOUBAN.getType());
    }


    /**
     * 清空分组走班课
     *
     * @param term
     * @param gradeId
     */
    public void clearFZZBCourse(String term, ObjectId gradeId) {
        timeTableDao.removeCourse(term, gradeId, ZoubanType.GROUPZOUBAN.getType());
    }


    //================================================排体育课===========================================================

    /**
     * 获取未排的体育课
     *
     * @param term
     * @param gradeId
     * @return
     */
    public List<List<IdNameDTO>> getArrangingPECourse(String term, String gradeId) {
        //本年级体育课
        List<ZouBanCourseEntry> zouBanCourseEntryList = zoubanCourseDao.findCourseList(term, new ObjectId(gradeId), ZoubanType.PE.getType());
        //逻辑位置-课程Map
        Map<ObjectId, List<ZouBanCourseEntry>> groupCourseMap = new HashMap<ObjectId, List<ZouBanCourseEntry>>();
        //按照逻辑位置分组
        for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntryList) {
            List<ZouBanCourseEntry> list = new ArrayList<ZouBanCourseEntry>();
            if (groupCourseMap.containsKey(zouBanCourseEntry.getGroup())) {
                list.addAll(groupCourseMap.get(zouBanCourseEntry.getGroup()));
            }
            list.add(zouBanCourseEntry);
            groupCourseMap.put(zouBanCourseEntry.getGroup(), list);
        }

        //未分配走班课组合列表
        List<List<IdNameDTO>> groupCourseList = new ArrayList<List<IdNameDTO>>();

        for (Map.Entry<ObjectId, List<ZouBanCourseEntry>> entry : groupCourseMap.entrySet()) {
            //逻辑位置的所有课
            List<ZouBanCourseEntry> list = entry.getValue();

            int lessonCount = list.get(0).getLessonCount();

            //排除已排课课时
            ObjectId classId = list.get(0).getClassId().get(0);
            TimeTableEntry timeTableEntry = timeTableDao.findTimeTable(term, classId, TimetableState.NOTPUBLISHED.getState(), 0);
            List<CourseItem> courseItemList = timeTableEntry.getCourseList();
            for (CourseItem item : courseItemList) {
                if (item.getType() == ZoubanType.PE.getType()) {
                    lessonCount--;
                }
            }

            for (int i = 0; i < lessonCount; i++) {
                List<IdNameDTO> idNameDTOList = new ArrayList<IdNameDTO>();
                for (ZouBanCourseEntry zouBanCourseEntry : list) {
                    idNameDTOList.add(new IdNameDTO(zouBanCourseEntry.getID().toString(), zouBanCourseEntry.getClassName()));
                }
                groupCourseList.add(idNameDTOList);
            }
        }

        return groupCourseList;
    }


    /**
     * 获取体育课课表
     *
     * @param term
     * @param gradeId
     * @return
     */
    public List<CourseItemDTO> getPETimetable(String term, ObjectId gradeId) {
        //获取全年级的课表
        List<TimeTableEntry> timeTableEntryList = timeTableDao.findTimeTableByGrade(term, gradeId, 0, 0);

        Map<PointJson, CourseItem> courseItemMap = new HashMap<PointJson, CourseItem>();

        //位置重复的体育课
        List<CourseItem> courseItemList = new ArrayList<CourseItem>();

        //合并课表
        for (TimeTableEntry timeTableEntry : timeTableEntryList) {
            for (CourseItem courseItem : timeTableEntry.getCourseList()) {
                PointJson p = new PointJson(courseItem.getXIndex(), courseItem.getYIndex());
                if (courseItemMap.containsKey(p)) {
                    CourseItem item = courseItemMap.get(p);

                    if (courseItem.getType() == ZoubanType.PE.getType()) {
                        //排除重复的体育课
                        List<ObjectId> itemCourseIds = new ArrayList<ObjectId>(item.getCourse());
                        List<ObjectId> courseIds = new ArrayList<ObjectId>(courseItem.getCourse());
                        itemCourseIds.retainAll(courseIds);

                        boolean flag = true;
                        for (CourseItem extraItem : courseItemList) {
                            if (extraItem.getXIndex() == p.getX() && extraItem.getYIndex() == p.getY()) {
                                List<ObjectId> couIds = new ArrayList<ObjectId>(extraItem.getCourse());
                                couIds.retainAll(courseIds);
                                if (couIds.size() == courseIds.size()) {
                                    flag = false;
                                    break;
                                }
                            }
                        }
                        if (flag && itemCourseIds.size() != item.getCourse().size()) {
                            if (item.getType() == ZoubanType.PE.getType()) {
                                courseItemList.add(courseItem);
                            } else {
                                courseItemMap.put(p, courseItem);
                            }
                        }
                    }
                } else {
                    courseItemMap.put(p, courseItem);
                }
            }
        }

        List<CourseItemDTO> list = new ArrayList<CourseItemDTO>();
        for (CourseItem courseItem : courseItemMap.values()) {
            list.add(new CourseItemDTO(courseItem));
        }

        for (CourseItem courseItem : courseItemList) {
            list.add(new CourseItemDTO(courseItem));
        }

        //所有教学班id
        Set<ObjectId> courseIdSet = new HashSet<ObjectId>();
        for (CourseItemDTO courseItemDTO : list) {
            List<CourseTeacherRoom> list2 = courseItemDTO.getCourseIdList();
            for (CourseTeacherRoom ctr : list2) {
                courseIdSet.add(new ObjectId(ctr.getCourseId()));
            }
        }

        setCourseTeacherAndClassroom(list, courseIdSet);

        return list;
    }

    /**
     * 体育课自动排课
     *
     * @param term
     * @param gradeId
     */
    public void autoArrangePE(String term, ObjectId schoolId, String gradeId) {
        //清空体育课和非走班课
        clearTimetableCourse(term, gradeId, 2, null);


        //第一步：获取体育课
        List<ZouBanCourseEntry> zouBanCourseEntryList = zoubanCourseDao.findCourseList(term, new ObjectId(gradeId), ZoubanType.PE.getType());

        if (zouBanCourseEntryList.size() > 0) {
            Map<ObjectId, List<ZouBanCourseEntry>> byGroup = new HashMap<ObjectId, List<ZouBanCourseEntry>>();

            for (ZouBanCourseEntry entry : zouBanCourseEntryList) {
                List<ZouBanCourseEntry> list = new ArrayList<ZouBanCourseEntry>();
                list.add(entry);
                if (byGroup.get(entry.getGroup()) != null) {
                    list.addAll(byGroup.get(entry.getGroup()));
                }
                byGroup.put(entry.getGroup(), list);
            }


            //第二步：分配体育课
            for (Map.Entry<ObjectId, List<ZouBanCourseEntry>> entry : byGroup.entrySet()) {
                List<ZouBanCourseEntry> courseEntryList = entry.getValue();
                List<ObjectId> teacherIdList = new ArrayList<ObjectId>();
                List<ObjectId> studentList = new ArrayList<ObjectId>();

                for (ZouBanCourseEntry zouBanCourseEntry : courseEntryList) {
                    teacherIdList.add(zouBanCourseEntry.getTeacherId());
                    studentList.addAll(zouBanCourseEntry.getStudentList());
                }
                List<ObjectId> classIds = courseEntryList.get(0).getClassId();
                //1：获取可用时间点
                List<PointJson> pointList = getAvailablePointForPE(term, schoolId, gradeId, classIds, teacherIdList, studentList);

                //2:筛选时间点，剔除前两节
                Iterator<PointJson> pointIterator = pointList.iterator();
                while (pointIterator.hasNext()) {
                    PointJson p = pointIterator.next();
                    if (p.getY() < 3) {//剔除前两节
                        pointIterator.remove();
                    }
                }

                //记录排课星期几（1-5），同一个班每天只能有一节体育课
                List<Integer> days = new ArrayList<Integer>();

                List<CourseItem> courseItemList = new ArrayList<CourseItem>();

                //课时
                int lessonCount = courseEntryList.get(0).getLessonCount();

                //3: 根据课时分配体育课
                for (int i = 0; i < lessonCount; i++) {
                    //随机取一个时间点
                    int x = 0;
                    while (true) {
                        x = (int) Math.floor(Math.random() * pointList.size());
                        if (!days.contains(pointList.get(x).getX())) {
                            break;
                        }
                    }
                    PointJson point = pointList.get(x);
                    days.add(point.getX());

                    //新增CourseItem
                    List<ObjectId> courseIdList = new ArrayList<ObjectId>();
                    courseIdList.add(courseEntryList.get(0).getID());
                    courseIdList.add(courseEntryList.get(1).getID());

                    CourseItem courseItem = new CourseItem();
                    courseItem.setId(new ObjectId());
                    courseItem.setType(ZoubanType.PE.getType());
                    courseItem.setXIndex(point.getX());
                    courseItem.setYIndex(point.getY());
                    courseItem.setCourse(courseIdList);

                    courseItemList.add(courseItem);
                    //从可用时间点删除
                    Iterator<PointJson> usedIterator = pointList.iterator();
                    while (usedIterator.hasNext()) {
                        PointJson p = usedIterator.next();
                        if (point.getX() == p.getX() && point.getY() == p.getY()) {
                            usedIterator.remove();
                            break;
                        }
                    }
                }

                //4: 把CourseItem更新到课表中
                for (ObjectId classId : classIds) {
                    timeTableDao.addCourseList(term, classId, 0, courseItemList);
                }
            }
        }
    }

    /**
     * 体育课排课获取可用位置---用于自动排课
     *
     * @param term
     * @param schoolId
     * @param gradeId
     * @param classIdList
     * @param teacherIdList
     * @return
     */
    public List<PointJson> getAvailablePointForPE(String term, ObjectId schoolId, String gradeId, List<ObjectId> classIdList,
                                                  List<ObjectId> teacherIdList, List<ObjectId> studentList) {
        return getAvailablePoint(term, schoolId, gradeId, ZoubanType.PE.getType(), classIdList, teacherIdList, null, studentList);
    }

    /**
     * 体育课排课获取可用位置---用于手动排课
     *
     * @param term
     * @param schoolId
     * @param gradeId
     * @param courseIdStr
     * @return
     */
    public List<PointJson> getAvailablePointForPE(String term, ObjectId schoolId, String gradeId, String courseIdStr) throws Exception{
        return getAvailablePoint(term, schoolId, gradeId, courseIdStr, ZoubanType.PE.getType());
    }

    /**
     * 新增体育课
     *
     * @param term
     * @param courseIdStr
     * @param xIndex
     * @param yIndex
     */
    public void addPECourse(String term, String courseIdStr, int xIndex, int yIndex) throws Exception{
        addCourse(term, null, xIndex, yIndex, courseIdStr, ZoubanType.PE.getType());
    }

    /**
     * 删除体育课
     *
     * @param term
     * @param gradeId
     * @param x
     * @param y
     */
    public void removePECourse(String term, String gradeId, int x, int y) {
        //删除事务冲突
        removeConflict(term, new ObjectId(gradeId), x, y, ZoubanType.PE.getType());
        //删除课时
        timeTableDao.removeCourse(term, new ObjectId(gradeId), x, y, ZoubanType.PE.getType());
    }


    //==================================================排非走班课========================================================

    /**
     * 获取非走班课未排课时
     *
     * @param term
     * @param classId
     * @return
     */
    public List<List<IdNameDTO>> getArrangingFZBCourse(String term, String classId) {

        List<List<IdNameDTO>> lists = new ArrayList<List<IdNameDTO>>();
        //本班非走班课
        List<ZouBanCourseEntry> fzbCourseList = zoubanCourseDao.getCourseListByClassId(term, new ObjectId(classId), ZoubanType.FEIZOUBAN.getType());
        //本班单双周课
        List<ZouBanCourseEntry> dszCourseLIst = zoubanCourseDao.getCourseListByClassId(term, new ObjectId(classId), ZoubanType.ODDEVEN.getType());

        //未排的非走班课
        for (ZouBanCourseEntry zouBanCourseEntry : fzbCourseList) {
            for (int i = 0; i < zouBanCourseEntry.getLessonCount(); i++) {
                List<IdNameDTO> courseList = new ArrayList<IdNameDTO>();
                courseList.add(new IdNameDTO(zouBanCourseEntry.getID().toString(), zouBanCourseEntry.getClassName()));
                lists.add(courseList);
            }
        }

        //未排的单双周课
        Map<ObjectId, List<IdNameDTO>> map = new HashMap<ObjectId, List<IdNameDTO>>();
        for (int i = 0; i < dszCourseLIst.size(); i++) {
            if (map.containsKey(dszCourseLIst.get(i).getGroup())) {
                List<IdNameDTO> idNameDTOs = map.get(dszCourseLIst.get(i).getGroup());
                idNameDTOs.add(new IdNameDTO(dszCourseLIst.get(i).getID().toString(), dszCourseLIst.get(i).getClassName()));
                map.put(dszCourseLIst.get(i).getGroup(), idNameDTOs);
            } else {
                List<IdNameDTO> idNameDTOs = new ArrayList<IdNameDTO>();
                idNameDTOs.add(new IdNameDTO(dszCourseLIst.get(i).getID().toString(), dszCourseLIst.get(i).getClassName()));
                map.put(dszCourseLIst.get(i).getGroup(), idNameDTOs);
            }
        }
        for (Map.Entry<ObjectId, List<IdNameDTO>> entry : map.entrySet()) {
            lists.add(entry.getValue());
        }


        //剔除已排课课时
        TimeTableEntry timeTableEntry = timeTableDao.findTimeTable(term, new ObjectId(classId), 0, 0);

        for (CourseItem item : timeTableEntry.getCourseList()) {
            if (item.getType() == ZoubanType.FEIZOUBAN.getType()) {
                Iterator<List<IdNameDTO>> iterator = lists.iterator();
                while (iterator.hasNext()) {
                    List<IdNameDTO> course = iterator.next();
                    if (item.getCourse().size() != 0 && course.size() != 0) {
                        if (course.get(0).getId().equals(item.getCourse().get(0).toString())) {
                            iterator.remove();
                            break;
                        }
                    }

                }
            } else if (item.getType() == ZoubanType.ODDEVEN.getType()) {
                Iterator<List<IdNameDTO>> iterator = lists.iterator();
                while (iterator.hasNext()) {
                    List<IdNameDTO> course = iterator.next();
                    boolean flag = false;
                    for (IdNameDTO idNameDTO : course) {
                        for (ObjectId objectId : item.getCourse()) {
                            if (idNameDTO.getId().equals(objectId.toString())) {
                                flag = true;
                                break;
                            }
                        }
                        if (flag) {
                            iterator.remove();
                            break;
                        }
                    }
                }
            }
        }
        return lists;
    }

    /**
     * 获取本班课表
     *
     * @param term
     * @param classId
     * @return
     */
    public List<CourseItemDTO> getFZBTimetable(String term, String classId) {
        //本班课表
        TimeTableEntry timeTableEntry = timeTableDao.findTimeTable(term, new ObjectId(classId), 0, 0);
        TimeTableDTO timeTableDTO = new TimeTableDTO(timeTableEntry);
        List<CourseItemDTO> courseItemDTOList = timeTableDTO.getCourseList();

        //已排课课程id
        Set<ObjectId> courseIdSet = new HashSet<ObjectId>();
        for (CourseItem courseItem : timeTableEntry.getCourseList()) {
            for (ObjectId courseId : courseItem.getCourse()) {
                courseIdSet.add(courseId);
            }
        }

        setCourseTeacherAndClassroom(courseItemDTOList, courseIdSet);

        return courseItemDTOList;
    }

    /**
     * 获取非走班课可用时间点
     *
     * @param term
     * @param schoolId
     * @param gradeId
     * @param courseId
     * @return
     */
    public List<PointJson> getAvailablePointForFZB(String term, ObjectId schoolId, String gradeId, String courseId) throws Exception{
        String[] courseIds = courseId.split(",");
        if (courseIds.length == 1) {
            return getAvailablePoint(term, schoolId, gradeId, courseId, ZoubanType.FEIZOUBAN.getType());
        } else {
            return getAvailablePoint(term, schoolId, gradeId, courseId, ZoubanType.ODDEVEN.getType());
        }

    }


    /**
     * 添加非走班课
     *
     * @param term
     * @param courseIdStr
     * @param x
     * @param y
     */
    public void addFZBCourse(String term, String gradeId, String courseIdStr, int x, int y) throws Exception{
        String[] courseIds = courseIdStr.split(",");
        int type = courseIds.length == 1 ? ZoubanType.FEIZOUBAN.getType() : ZoubanType.ODDEVEN.getType();
        addCourse(term, gradeId, x, y, courseIdStr, type);
    }

    /**
     * 删除非走班课
     *
     * @param term
     * @param classId
     * @param x
     * @param y
     */
    public void removeFZBCourse(String term, String gradeId, String classId, int x, int y) {
        List<ObjectId> classIds = new ArrayList<ObjectId>();
        classIds.add(new ObjectId(classId));
        //删除事务冲突
        removeFZBConflict(term, new ObjectId(gradeId), new ObjectId(classId), x, y);
        timeTableDao.removeCourse(term, classIds, x, y);
    }


    /**
     * 删除非走班课事务冲突
     *
     * @param term
     * @param gradeId
     * @param x
     * @param y
     */
    private void removeFZBConflict(String term, ObjectId gradeId, ObjectId classId, int x, int y) {
        TimeTableEntry timeTableEntry = timeTableDao.findTimeTable(term, classId, 0, 0);
        Set<ObjectId> courseIdSet = new HashSet<ObjectId>();

        for (CourseItem item : timeTableEntry.getCourseList()) {
            if ((item.getType() == ZoubanType.FEIZOUBAN.getType() || item.getType() == ZoubanType.ODDEVEN.getType())
                    && item.getXIndex() == x && item.getYIndex() == y) {
                courseIdSet.addAll(item.getCourse());
                break;
            }
        }
        eventConflictService.removeConflictByCourseList(term, gradeId, x, y, courseIdSet);
    }

    /**
     * 非走班自动排课
     *
     * @param term
     * @param gradeId
     * @param classId
     */
    public void autoArrangeFZBCourse(String term, ObjectId schoolId, String gradeId, String classId) throws Exception {
        //第一步，获取本班未排的非走班课
        List<List<IdNameDTO>> arrangingCourseList = getArrangingFZBCourse(term, classId);
        if (arrangingCourseList.size() == 0) {
            //清空本班已经排的非走班课程
            clearTimetableCourse(term, null, 3, classId);
        }
        arrangingCourseList = getArrangingFZBCourse(term, classId);
        Collections.shuffle(arrangingCourseList);


        //第二步，随机分配未排的课
        for (List<IdNameDTO> course : arrangingCourseList) {
            if (course.size() == 1) {//非走班
                String courseId = course.get(0).getId();
                ZouBanCourseEntry zouBanCourseEntry = zoubanCourseDao.getCourseInfoById(new ObjectId(courseId));

                if (zouBanCourseEntry.getTeacherId() == null) {
                    throw new Exception("部分课未设置老师");
                }

                //1. 先获取可用时间点
                List<PointJson> availablePoint = getAvailablePointForFZB(term, schoolId, gradeId, courseId);
                if (availablePoint.size() > 0) {
                    //2. 随机取一个点
                    Collections.shuffle(availablePoint);
                    PointJson p = availablePoint.get(0);
                    //3. 添加课程
                    addFZBCourse(term, gradeId, course.get(0).getId(), p.getX(), p.getY());
                } else {
                    throw new Exception("无可用位置");
                }
            } else if (course.size() == 2) {//单双周
                ZouBanCourseEntry zouBanCourseEntry = zoubanCourseDao.getCourseInfoById(new ObjectId(course.get(0).getId()));
                ZouBanCourseEntry zouBanCourseEntry1 = zoubanCourseDao.getCourseInfoById(new ObjectId(course.get(1).getId()));
                if (zouBanCourseEntry.getTeacherId() == null || zouBanCourseEntry1.getTeacherId() == null) {
                    throw new Exception("部分课未设置老师");
                }

                //1. 先获取单双周可用时间点
                String courseStr = course.get(0).getId() + "," + course.get(1).getId();
                List<PointJson> availablePoint = getAvailablePointForFZB(term, schoolId, gradeId, courseStr);
                if (availablePoint.size() > 0) {
                    //2. 随机取一个点
                    Collections.shuffle(availablePoint);
                    PointJson p = availablePoint.get(0);
                    //3. 添加课程
                    addFZBCourse(term, gradeId, courseStr, p.getX(), p.getY());
                } else {
                    throw new Exception("无可用位置");
                }
            }
        }
    }

    /**
     * 检查课表是否发布
     *
     * @param term
     * @param schoolId
     * @param gradeId
     * @return
     */
    public int isPublished(String term, String schoolId, String gradeId) {
        int state = zoubanStateService.getZoubanState(term, schoolId, gradeId);
        return state > 4 ? 1 : 0;
    }

    /**
     * 检查全校是否有发布课表
     * @param term
     * @param schoolId
     * @return
     */
    public boolean isPublic(String term, ObjectId schoolId) {
        return timeTableDao.findTimetable(term, schoolId, TimetableState.PUBLISHED.getState()) != null;
    }



    /**
     * 检查走班课是否排完
     *
     * @param term
     * @return
     */
    public boolean checkZBFinished(String term, String gradeId) {
        //本年级所有走班课
        List<ZouBanCourseEntry> zouBanCourseEntries = zoubanCourseDao.findCourseList(term, new ObjectId(gradeId), ZoubanType.ZOUBAN.getType());
        //所有课总课时数
        int count = 0;
        for (ZouBanCourseEntry zc : zouBanCourseEntries) {
            count += zc.getLessonCount();
        }

        List<TimeTableEntry> timeTableEntryList = timeTableDao.findTimeTableByGrade(term, new ObjectId(gradeId), 0, 0);

        Map<PointJson, CourseItem> pointCourseMap = new HashMap<PointJson, CourseItem>();

        //合并课表
        for (TimeTableEntry timeTableEntry : timeTableEntryList) {

            for (CourseItem item : timeTableEntry.getCourseList()) {
                if (item.getType() == ZoubanType.ZOUBAN.getType()) {
                    PointJson p = new PointJson(item.getXIndex(), item.getYIndex());
                    List<ObjectId> courseList = item.getCourse();

                    if (pointCourseMap.containsKey(p)) {
                        CourseItem oldItem = pointCourseMap.get(p);
                        List<ObjectId> oldCourseList = oldItem.getCourse();

                        Set<ObjectId> courseIdSet = new HashSet<ObjectId>();
                        courseIdSet.addAll(oldCourseList);
                        courseIdSet.addAll(courseList);
                        oldItem.setCourse(new ArrayList<ObjectId>(courseIdSet));

                        pointCourseMap.put(p, oldItem);
                    } else {
                        pointCourseMap.put(p, item);
                    }
                }
            }
        }

        //课表中走班课课时数
        int arrangedCount = 0;
        List<CourseItem> courseList = new ArrayList<CourseItem>(pointCourseMap.values());
        for (CourseItem item : courseList) {
            arrangedCount += item.getCourse().size();
        }

        if (count != arrangedCount) {
            return false;
        }

        return true;
    }

    /**
     * 检查分层走班课是否排完
     *
     * @param term
     * @return
     */
    public boolean checkFZZBFinished(String term, String gradeId) {
        //本年级所有分组走班课
        List<ZouBanCourseEntry> zouBanCourseEntries = zoubanCourseDao.findCourseList(term, new ObjectId(gradeId), ZoubanType.GROUPZOUBAN.getType());
        //所有课总课时数
        int count = 0;
        for (ZouBanCourseEntry zc : zouBanCourseEntries) {
            count += zc.getLessonCount();
        }

        List<TimeTableEntry> timeTableEntryList = timeTableDao.findTimeTableByGrade(term, new ObjectId(gradeId), 0, 0);

        Map<PointJson, CourseItem> pointCourseMap = new HashMap<PointJson, CourseItem>();

        //合并课表
        for (TimeTableEntry timeTableEntry : timeTableEntryList) {

            for (CourseItem item : timeTableEntry.getCourseList()) {
                if (item.getType() == ZoubanType.GROUPZOUBAN.getType()) {
                    PointJson p = new PointJson(item.getXIndex(), item.getYIndex());
                    List<ObjectId> courseList = item.getCourse();

                    if (pointCourseMap.containsKey(p)) {
                        CourseItem oldItem = pointCourseMap.get(p);
                        List<ObjectId> oldCourseList = oldItem.getCourse();

                        Set<ObjectId> courseIdSet = new HashSet<ObjectId>();
                        courseIdSet.addAll(oldCourseList);
                        courseIdSet.addAll(courseList);
                        oldItem.setCourse(new ArrayList<ObjectId>(courseIdSet));

                        pointCourseMap.put(p, oldItem);
                    } else {
                        pointCourseMap.put(p, item);
                    }
                }
            }
        }

        //课表中分组走班课课时数
        int arrangedCount = 0;
        List<CourseItem> courseList = new ArrayList<CourseItem>(pointCourseMap.values());
        for (CourseItem item : courseList) {
            arrangedCount += item.getCourse().size();
        }

        if (count != arrangedCount) {
            return false;
        }

        return true;
    }


    /**
     * 检查体育课是否排完
     *
     * @param term
     * @return
     */
    public boolean checkPEFinished(String term, String gradeId) {
        return checkFinished(term, gradeId, ZoubanType.PE.getType());
    }

    /**
     * 检查非走班课是否排完
     *
     * @param term
     * @param gradeId
     * @return
     */
    public boolean checkFZBFinished(String term, String gradeId) {
        return checkFinished(term, gradeId, ZoubanType.FEIZOUBAN.getType());
    }

    /**
     * 检查单双周课是否排完
     *
     * @param term
     * @param gradeId
     * @return
     */
    public boolean checkDSZFinished(String term, String gradeId) {
        return checkFinished(term, gradeId, ZoubanType.ODDEVEN.getType());
    }

    /**
     * 检查课程是否排完
     *
     * @param term
     * @param gradeId
     * @param type
     * @return
     */
    private boolean checkFinished(String term, String gradeId, int type) {
        List<ZouBanCourseEntry> zouBanCourseEntries = zoubanCourseDao.findCourseList(term, new ObjectId(gradeId), type);
        //所有课总课时数
        int count = 0;
        if (type == ZoubanType.FEIZOUBAN.getType() || type == ZoubanType.ODDEVEN.getType()) {
            for (ZouBanCourseEntry courseEntry : zouBanCourseEntries) {
                count += courseEntry.getLessonCount();
            }
        } else if (type == ZoubanType.PE.getType()) {
            for (ZouBanCourseEntry courseEntry : zouBanCourseEntries) {
                count += courseEntry.getLessonCount() * courseEntry.getClassId().size();
            }
            count /= 2;
        }

        List<ClassInfoDTO> classInfoDTOList = classService.findClassByGradeId(gradeId);

        //体育课课时数
        int arrangedCount = 0;

        for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
            TimeTableEntry timeTableEntry = timeTableDao.findTimeTable(term, new ObjectId(classInfoDTO.getId()), 0, 0);
            List<CourseItem> courseList = timeTableEntry.getCourseList();
            for (CourseItem item : courseList) {
                if (item.getType() == type) {
                    if (type == ZoubanType.ODDEVEN.getType()) {
                        arrangedCount += 2;
                    } else {
                        arrangedCount++;
                    }

                }
            }
        }

        if (count != arrangedCount) {
            return false;
        }

        return true;
    }


    /**
     * 获取班级的课程表
     *
     * @param term
     * @param classId
     * @param type
     * @return
     */
    public TimeTableDTO findTimeTable(String schoolId, String term, String classId, int type, int week) {
        TimeTableEntry timeTableEntry = timeTableDao.findTimeTable(term, new ObjectId(classId), type, week);
        if (timeTableEntry != null)
            return new TimeTableDTO(timeTableEntry);
        else {
            if (type == 3) {
                String gradeId = classService.findClassInfoByClassId(classId).getGradeId();
                TimeTableEntry timeTableEntry1 = new TimeTableEntry(term, new ObjectId(schoolId), new ObjectId(gradeId), new ObjectId(classId),
                        new ArrayList<CourseItem>(), TimetableState.PUBLISHED.getState(), 0, 0);
                timeTableDao.addTimeTable(timeTableEntry1);
                return new TimeTableDTO(timeTableEntry1);
            } else
                return null;
        }
    }


    /**
     * 根据学校id获取老师
     *
     * @param schoolId
     * @return
     */
    public Map<IdNameValuePairDTO, Set<IdNameValuePairDTO>> findTeacherListBySchool(ObjectId schoolId) {
        Map<IdNameValuePairDTO, Set<IdNameValuePairDTO>> retMap = new HashMap<IdNameValuePairDTO, Set<IdNameValuePairDTO>>();
        List<UserEntry> userEntryList = userDao.getUserEntryBySchoolIdList(schoolId, new BasicDBObject("r", 1).append("nm", 1));
        Map<ObjectId, UserEntry> userMap = new HashMap<ObjectId, UserEntry>();
        for (UserEntry e : userEntryList) {
            if (UserRole.isTeacher(e.getRole())) {
                userMap.put(e.getID(), e);
            }
        }
        UserEntry e;
        List<TeacherClassSubjectEntry> tcjList = teacherClassSubjectDao.findSubjectByTeacherIds(userMap.keySet());
        for (TeacherClassSubjectEntry tcj : tcjList) {
            IdNameValuePairDTO dto = new IdNameValuePairDTO(tcj.getSubjectInfo());
            if (!retMap.containsKey(dto)) {
                retMap.put(dto, new HashSet<IdNameValuePairDTO>());
            }
            e = userMap.get(tcj.getTeacherId());
            if (null != e) {
                retMap.get(dto).add(new IdNameValuePairDTO(e));
            }
        }
        return retMap;
    }




    /**
     * 删除非法教学班
     *
     * @param term
     * @param schoolId
     * @return
     */
    public int removeInvalidCourse(String term, ObjectId schoolId) {
        return zoubanCourseDao.removeInvalidCourse(term, schoolId);
    }


}

