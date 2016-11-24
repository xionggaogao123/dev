package com.fulaan.zouban.service;

import com.db.school.ClassDao;
import com.db.school.TeacherDao;
import com.db.user.UserDao;
import com.db.zouban.*;
import com.fulaan.classroom.service.ClassroomService;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.controller.GradeView;
import com.fulaan.myschool.controller.SubjectView;
import com.fulaan.school.service.SchoolService;
import com.fulaan.user.service.UserService;
import com.fulaan.zouban.dto.*;
import com.fulaan.zouban.dto.GradeSubjectCourse;
import com.fulaan.zouban.dto.PointJson;
import com.fulaan.zouban.dto.TermDTO;
import com.fulaan.zouban.dto.ClassDetail;

import com.mongodb.BasicDBObject;
import com.pojo.classroom.ClassroomEntry;
import com.pojo.school.*;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.pojo.zouban.*;

import com.pojo.zouban.SubjectConfEntry;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.Region;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;
import java.util.List;

/**
 * Created by qiangm on 2015/9/15.
 */
@Service
public class TimeTableService {
    private static final Logger logger =Logger.getLogger(TimeTableService.class);
    private TeacherDao teacherDao = new TeacherDao();
    private TimeTableDao timeTableDao = new TimeTableDao();
    private ClassDao classDao = new ClassDao();
    private ZouBanCourseDao zoubanCourseDao = new ZouBanCourseDao();
    private UserDao userDao = new UserDao();
    private ZoubanNoticeDao zoubanNoticeDao = new ZoubanNoticeDao();
    @Autowired
    private ClassroomService classroomService;
    @Autowired
    private PaikeService paikeService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private UserService userService;
    @Autowired
    private ClassService classService;
    @Autowired
    private XuanKeService xuanKeService;
    @Autowired
    private TermService termService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private StudentXuankeService studentXuankeService;
    @Autowired
    private TimetableConfService timetableConfService;
    @Autowired
    private BianbanService bianbanService;

    /**
     * 获取班级的课程表
     *
     * @param term
     * @param classId
     * @param type
     * @return
     */
    public TimeTableDTO findTimeTable(String term, String classId, int type, int week) {
        TimeTableEntry timeTableEntry = timeTableDao.findTimeTable(term, new ObjectId(classId), type, week);
        if (timeTableEntry != null)
            return new TimeTableDTO(timeTableEntry);
        return null;
    }


    /**
     *
     */
    public TimeTableEntry findTimeTableEntry(String term, String classId, int type, int week) {
        TimeTableEntry timeTableEntry = timeTableDao.findTimeTable(term, new ObjectId(classId), type, week);
        if (timeTableEntry != null)
            return timeTableEntry;
        return null;
    }


    public void addTimeTableEntry(TimeTableEntry timeTableEntry) {
        timeTableDao.addTimeTable(timeTableEntry);
    }

    /**
     * 清空课表
     *
     * @param term
     * @param gradeId
     */
    public void deleteAllCourse(String term, String gradeId) {
        timeTableDao.removeAllCourseItem(term, new ObjectId(gradeId));
    }

    /**
     * 根据学期和年级id获取课表配置
     *
     * @param term
     * @param gradeId
     * @return
     */
    public CourseConfDTO findCourseConfByGradeId(String term, ObjectId gradeId) {
        TimetableConfDTO timetableConf = timetableConfService.getTimetableConf(term, gradeId);
        CourseConfDTO courseConfDTO = new CourseConfDTO(timetableConf);
        return courseConfDTO;
    }


    /**
     * 获取学生课表-----finish
     *
     * @param term
     * @param studentId
     * @return
     */
    public List<StudentTimeTable> getStudentTimeTable(String term, String year, String studentId, String gradeId, int week, int type) {
        //通过userId获取classId
        String classId = classDao.getClassEntryByStuId(new ObjectId(studentId), Constant.FIELDS).getID().toString();

        List<ZouBanCourseEntry> zouBanCourseEntries = zoubanCourseDao.getStudentChooseZB(new ObjectId(studentId), year, new ObjectId(gradeId));
        List<String> zoubanCourseIds = new ArrayList<String>();
        //体育走班
        List<String> peCourseIds = new ArrayList<String>();
        //拓展课
        List<String> interestCourseIds = new ArrayList<String>();
        //单双周课
        List<String> oddEvenCourseIds = new ArrayList<String>();
        //分组走班课
        List<String> groupZoubanCourseIds = new ArrayList<String>();
        for (ZouBanCourseEntry z : zouBanCourseEntries) {
            if (z.getType() == 1)
                zoubanCourseIds.add(z.getID().toString());
            else if (z.getType() == 3 || z.getType() == 4)
                peCourseIds.add(z.getID().toString());
            else if (z.getType() == 6) {
                interestCourseIds.add(z.getID().toString());
            } else if (z.getType() == ZoubanType.ODDEVEN.getType()) {
                oddEvenCourseIds.add(z.getID().toString());
            } else if (z.getType() == ZoubanType.GROUPZOUBAN.getType()) {
                groupZoubanCourseIds.add(z.getID().toString());
            }
        }

        TimeTableDTO timeTableDTO = findTimeTable(term, classId, type, week);
        if (timeTableDTO == null) {
            return new ArrayList<StudentTimeTable>();
        }
        List<CourseItemDTO> courseItemDTOList = timeTableDTO.getCourseList();

        //获取每个学生选择的拓展课
        //todo

        List<StudentTimeTable> studentTimeTableArrayList = new ArrayList<StudentTimeTable>();
        if (courseItemDTOList != null && !courseItemDTOList.isEmpty()) {

            for (CourseItemDTO courseItemDTO : courseItemDTOList) {
                StudentTimeTable studentTimeTable = new StudentTimeTable();
                int type1 = courseItemDTO.getType();
                if (type1 == ZoubanType.ZOUBAN.getType())//走班，可以存在多个
                {
                    studentTimeTable.setxIndex(courseItemDTO.getxIndex());
                    studentTimeTable.setyIndex(courseItemDTO.getyIndex());
                    studentTimeTable.setType(0);
                    List<CourseTeacherRoom> ctrList = courseItemDTO.getCourseIdList();
                    List<String> courseIdList = new ArrayList<String>();
                    for (CourseTeacherRoom ctr : ctrList) {
                        courseIdList.add(ctr.getCourseId().toString());
                    }
                    String zoubanCourseId = findCourseId(courseIdList, zoubanCourseIds);
                    if (!zoubanCourseId.equals(Constant.EMPTY))//走班
                    {
                        studentTimeTable.setCourseId(zoubanCourseId);
                        studentTimeTableArrayList.add(studentTimeTable);
                    }
                } else if (type1 == ZoubanType.GROUPZOUBAN.getType()) {
                    studentTimeTable.setxIndex(courseItemDTO.getxIndex());
                    studentTimeTable.setyIndex(courseItemDTO.getyIndex());
                    studentTimeTable.setType(7);
                    List<CourseTeacherRoom> ctrList = courseItemDTO.getCourseIdList();
                    List<String> courseIdList = new ArrayList<String>();
                    for (CourseTeacherRoom ctr : ctrList) {
                        courseIdList.add(ctr.getCourseId().toString());
                    }
                    String groupZoubanCourseId = findCourseId(courseIdList, groupZoubanCourseIds);
                    if (!groupZoubanCourseId.equals(Constant.EMPTY))//走班
                    {
                        studentTimeTable.setCourseId(groupZoubanCourseId);
                        studentTimeTableArrayList.add(studentTimeTable);
                    }

                } else if (type1 == ZoubanType.FEIZOUBAN.getType())//非走班
                {
                    studentTimeTable.setType(2);
                    studentTimeTable.setxIndex(courseItemDTO.getxIndex());
                    studentTimeTable.setyIndex(courseItemDTO.getyIndex());
                    studentTimeTable.setCourseId(courseItemDTO.getCourseIdList().get(0).getCourseId());
                    studentTimeTableArrayList.add(studentTimeTable);
                } else if (type1 == ZoubanType.ODDEVEN.getType()) {
                    studentTimeTable.setType(8);
                    studentTimeTable.setxIndex(courseItemDTO.getxIndex());
                    studentTimeTable.setyIndex(courseItemDTO.getyIndex());
                    List<CourseTeacherRoom> ctrList = courseItemDTO.getCourseIdList();
                    List<String> courseIdList = new ArrayList<String>();
                    for (CourseTeacherRoom ctr : ctrList) {
                        courseIdList.add(ctr.getCourseId().toString());
                    }
                    Boolean flag = isEquals(courseIdList, oddEvenCourseIds);
                    if (flag)//单双周
                    {
                        studentTimeTable.setCourseId(courseIdList.get(0) + "," + courseIdList.get(1));
                        studentTimeTableArrayList.add(studentTimeTable);
                    }
                } else if (type1 == 4 || type1 == 3) {
                    studentTimeTable.setxIndex(courseItemDTO.getxIndex());
                    studentTimeTable.setyIndex(courseItemDTO.getyIndex());
                    studentTimeTable.setType(type1);
                    List<CourseTeacherRoom> ctrList = courseItemDTO.getCourseIdList();
                    List<String> courseIdList = new ArrayList<String>();
                    for (CourseTeacherRoom ctr : ctrList) {
                        courseIdList.add(ctr.getCourseId().toString());
                    }
                    String zoubanCourseId = findCourseId(courseIdList, peCourseIds);
                    if (!zoubanCourseId.equals(Constant.EMPTY)) {
                        studentTimeTable.setCourseId(zoubanCourseId);
                        studentTimeTableArrayList.add(studentTimeTable);
                    }
                } else if (type1 == 5)//非走班
                {
                    studentTimeTable.setType(5);
                    studentTimeTable.setxIndex(courseItemDTO.getxIndex());
                    studentTimeTable.setyIndex(courseItemDTO.getyIndex());
                    studentTimeTable.setCourseId(courseItemDTO.getCourseIdList().get(0).getCourseId());
                    studentTimeTableArrayList.add(studentTimeTable);
                } else if (type1 == 6)//兴趣拓展课
                {
                    studentTimeTable.setType(6);
                    studentTimeTable.setxIndex(courseItemDTO.getxIndex());
                    studentTimeTable.setyIndex(courseItemDTO.getyIndex());
                    String interestCourseId = "";
                    for (CourseTeacherRoom ctr : courseItemDTO.getCourseIdList()) {
                        for (String interestId : interestCourseIds) {
                            if (interestId.equals(ctr.getCourseId())) {
                                interestCourseId = interestId;
                                break;
                            }
                        }
                        if (!StringUtils.isBlank(interestCourseId)) {
                            break;
                        }
                    }
                    studentTimeTable.setCourseId(interestCourseId);
                    studentTimeTableArrayList.add(studentTimeTable);
                }
            }
        }
        //填充课表详细信息
        List<ObjectId> courseIds = new ArrayList<ObjectId>();
        for (StudentTimeTable s : studentTimeTableArrayList) {
            if (!StringUtils.isBlank(s.getCourseId())) {
                if (s.getCourseId().indexOf(",") > -1) {
                    String[] str = s.getCourseId().split(",");
                    for (String item : str) {
                        courseIds.add(new ObjectId(item));
                    }
                } else {
                    courseIds.add(new ObjectId(s.getCourseId()));
                }
            }
        }
        List<ObjectId> roomIds = new ArrayList<ObjectId>();
        for (ZouBanCourseEntry z : zouBanCourseEntries) {
            if (z.getClassRoomId() != null) {
                roomIds.add(z.getClassRoomId());
            }
        }
        Map<ObjectId, ClassroomEntry> classroomEntryMap = classroomService.findClassRoomEntryMap(roomIds);
        for (StudentTimeTable s : studentTimeTableArrayList) {
            if (s.getType() == ZoubanType.ODDEVEN.getType()) {
                String[] courseId = s.getCourseId().split(",");
                String roomId = "";
                String className = "";
                String teacherName = "";
                for (ZouBanCourseEntry z : zouBanCourseEntries) {
                    for (String str : courseId) {
                        if (z.getID().toString().equals(str)) {
                            if (classroomEntryMap != null) {
                                if (z.getClassRoomId() == null)
                                    roomId = "";
                                else if (classroomEntryMap.containsKey(z.getClassRoomId())) {
                                    roomId = classroomEntryMap.get(z.getClassRoomId()).getRoomName();
                                } else {
                                    roomId = "";
                                }
                            }
                            if (className.indexOf("/") < -1) {
                                className = z.getClassName() + "/";
                            } else {
                                className = className + "/" + z.getClassName();
                            }
                            if (teacherName.indexOf("/") < -1) {
                                teacherName = z.getTeacherName() + "/";
                            } else {
                                teacherName = teacherName + "/" + z.getTeacherName();
                            }
                        }
                    }
                }
                s.setClassName(className.substring(1, className.length()));
                s.setClassRoom(roomId);
                s.setTeacherName(teacherName.substring(1, teacherName.length()));
            } else {
                for (ZouBanCourseEntry z : zouBanCourseEntries) {
                    if (z.getID().toString().equals(s.getCourseId())) {
                        s.setClassName(z.getClassName());
                        if (classroomEntryMap != null) {
                            if (z.getClassRoomId() == null)
                                s.setClassRoom("");
                            else if (classroomEntryMap.containsKey(z.getClassRoomId())) {
                                s.setClassRoom(classroomEntryMap.get(z.getClassRoomId()).getRoomName());
                            } else {
                                s.setClassRoom("");
                            }
                        } else {
                            s.setClassName("");
                        }
                        s.setTeacherName(z.getTeacherName());
                    }
                }
            }
        }
        return studentTimeTableArrayList;
    }

    /**
     * 判断两个数组中有相同元素。
     *
     * @param all
     * @param part
     * @return
     */
    public boolean isEquals(List<String> part, List<String> all) {
        if (all == null || all.isEmpty() || part == null || part.isEmpty()) {
            return false;
        } else {
            boolean r = false;
            for (String str : part) {
                boolean flag = false;
                for (String str2 : all) {
                    if (str.equals(str2)) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    r = flag;
                    break;
                }
            }
            return r;
        }
    }

    //判断是否包含
    public String findCourseId(List<String> all, List<String> part) {
        if (all == null || all.isEmpty() || part == null || part.isEmpty()) {
            return Constant.EMPTY;
        }
        for (String str : all) {
            for (String str2 : part) {
                if (str.equals(str2))
                    return str;
            }
        }
        return Constant.EMPTY;
    }

    /**
     * 获取任课老师课表------finish
     *
     * @param term
     * @param teacherIdStr
     * @return
     */
    public List<TeacherTimeTableItem> getTeacherTimeTable(String year, String term, String teacherIdStr, int week) {
        ObjectId teacherId = new ObjectId(teacherIdStr);
        List<ZouBanCourseEntry> zouBanCourseEntries = zoubanCourseDao.findCourseListByTeacherId(term, teacherId, false);

        UserEntry userEntry = userDao.getUserEntry(teacherId, Constant.FIELDS);
        ObjectId schoolId = userEntry.getSchoolID();

        List<TimeTableEntry> timeTableEntryList = timeTableDao.findAllTimeTable(year, schoolId, TimetableState.PUBLISHED.getState(), week);

        List<TeacherTimeTableItem> teacherTimeTableItems = new ArrayList<TeacherTimeTableItem>();

        int index = 0;
        for (ZouBanCourseEntry z : zouBanCourseEntries) {
            String classroomId = z.getClassRoomId() == null ? "" : z.getClassRoomId().toString();

            for (TimeTableEntry timeTableEntry : timeTableEntryList) {
                for (CourseItem c : timeTableEntry.getCourseList()) {
                    if (c.getType() == z.getType() && c.getCourse().contains(z.getID())) {
                        TeacherTimeTableItem tableItem = new TeacherTimeTableItem();
                        tableItem.setClassName(z.getClassName());
                        tableItem.setClassRoom(classroomId);
                        tableItem.setxIndex(c.getXIndex());
                        tableItem.setyIndex(c.getYIndex());

                        boolean have = false;
                        if(z.getType() != ZoubanType.FEIZOUBAN.getType() &&
                                z.getType() != ZoubanType.ODDEVEN.getType()) {
                            for (TeacherTimeTableItem existItem : teacherTimeTableItems) {
                                if (existItem.getxIndex() == tableItem.getxIndex() &&
                                        existItem.getyIndex() == tableItem.getyIndex()) {
                                    have = true;
                                    break;
                                }
                            }
                        }
                        if (!have) {
                            teacherTimeTableItems.add(tableItem);
                        }
                    }
                }
            }
        }
        //收集classroomid
        List<ObjectId> classRoomIds = new ArrayList<ObjectId>();
        for (TeacherTimeTableItem tt : teacherTimeTableItems) {
            if (!tt.getClassRoom().equals("")) {
                classRoomIds.add(new ObjectId(tt.getClassRoom()));
            }
        }
        Map<ObjectId, ClassroomEntry> maps = classroomService.findClassRoomEntryMap(classRoomIds);
        for (TeacherTimeTableItem tt : teacherTimeTableItems) {
            if (!tt.getClassRoom().equals("")) {
                ObjectId classroomId = new ObjectId(tt.getClassRoom());
                if (maps.containsKey(classroomId)) {
                    tt.setClassRoom(maps.get(classroomId).getRoomName());
                }
            }
        }
        return teacherTimeTableItems;
    }



    /**
     * 获取老师所在的学科列表
     *
     * @param year
     * @param teacherId
     * @return
     */
    public List<String> getSubjectIdsByTeacher(String year, String teacherId) {
        List<String> subjectIds = new ArrayList<String>();
        List<ZouBanCourseEntry> zouBanCourseEntries = zoubanCourseDao.findCourseListByTeacherId(year, new ObjectId(teacherId), false);
        for (ZouBanCourseEntry z : zouBanCourseEntries) {
            subjectIds.add(z.getSubjectId().toString());
        }
        return subjectIds;
    }

    /**
     * 获取教学班课表---finish
     *
     * @param term
     * @param courseId
     * @return
     */
    public List<PointJson> getCourseTimeTable(String term, String courseId, String gradeId, int type, int week) {
        List<Point> pointList = new ArrayList<Point>();
        List<TimeTableEntry> timeTableEntries = timeTableDao.findTimeTableByGrade(term, new ObjectId(gradeId), type, week);
        for (TimeTableEntry entry : timeTableEntries) {
            List<CourseItem> courseItemList = entry.getCourseList();
            for (CourseItem c : courseItemList) {
                if (c.getCourse().contains(new ObjectId(courseId))) {
                    if (!pointList.contains(new Point(c.getXIndex(), c.getYIndex())))
                        pointList.add(new Point(c.getXIndex(), c.getYIndex()));
                }
            }
        }

        return convertPoint(pointList);
    }

    private List<PointJson> convertPoint(List<Point> pointList) {
        List<PointJson> pointReturnList = new ArrayList<PointJson>();
        for (Point point : pointList) {
            PointJson pointJson = new PointJson(point.x, point.y);
            pointReturnList.add(pointJson);
        }
        return pointReturnList;
    }

    /**
     * 获取科目课程基本配置
     *
     * @param subjectId
     * @return
     */
    public ZouBanCourseEntry getSubjectConf(String subjectId) {
        return zoubanCourseDao.getCourseInfoById(new ObjectId(subjectId));
    }

    /**
     * 获取行政班课表----finish
     *
     * @param classId
     * @return
     */
    public List<StudentTimeTable> getClassTimeTable(String term, String classId, int t, int week) {
        TimeTableDTO timeTableDTO = findTimeTable(term, classId, t, week);
        if (timeTableDTO == null) {
            return null;
        }
        List<CourseItemDTO> courseItemDTOList = timeTableDTO.getCourseList();
        List<StudentTimeTable> studentTimeTableArrayList = new ArrayList<StudentTimeTable>();
        if (courseItemDTOList != null && !courseItemDTOList.isEmpty()) {

            for (CourseItemDTO courseItemDTO : courseItemDTOList) {
                StudentTimeTable studentTimeTable = new StudentTimeTable();

                studentTimeTable.setxIndex(courseItemDTO.getxIndex());
                studentTimeTable.setyIndex(courseItemDTO.getyIndex());

                int type = courseItemDTO.getType();
                if (type == ZoubanType.ZOUBAN.getType() || type == 0) {
                    //获取走班课名称
                    String subjectName = "走班";
                    studentTimeTable.setClassName(subjectName);
                    studentTimeTable.setClassRoom(Constant.EMPTY);
                    studentTimeTable.setCourseId(Constant.EMPTY);
                    studentTimeTable.setType(courseItemDTO.getType());
                    studentTimeTable.setCourseItemId(courseItemDTO.getId());
                } else if (type == ZoubanType.GROUPZOUBAN.getType()) {
                    String subjectName = "分层走班";
                    studentTimeTable.setClassName(subjectName);
                    studentTimeTable.setType(courseItemDTO.getType());
                    studentTimeTable.setClassRoom(Constant.EMPTY);
                    studentTimeTable.setCourseId(Constant.EMPTY);
                    studentTimeTable.setCourseItemId(courseItemDTO.getId());
                } else if (type == ZoubanType.FEIZOUBAN.getType()) {
                    studentTimeTable.setClassName(courseItemDTO.getCourseIdList().get(0).getCourseName());
                    studentTimeTable.setCourseId(courseItemDTO.getCourseIdList().get(0).getCourseId());
                    studentTimeTable.setClassRoom(courseItemDTO.getCourseIdList().get(0).getClassRoom());
                    studentTimeTable.setType(courseItemDTO.getType());
                    studentTimeTable.setCourseItemId(courseItemDTO.getId());
                } else if (type == ZoubanType.ODDEVEN.getType()) {
                    studentTimeTable.setClassName(courseItemDTO.getCourseIdList().get(0).getCourseName());
                    studentTimeTable.setCourseId(courseItemDTO.getCourseIdList().get(0).getCourseId()
                            + "," + courseItemDTO.getCourseIdList().get(1).getCourseId());
                    studentTimeTable.setClassRoom(courseItemDTO.getCourseIdList().get(0).getClassRoom());
                    studentTimeTable.setType(courseItemDTO.getType());
                    studentTimeTable.setCourseItemId(courseItemDTO.getId());
                } else if (type == ZoubanType.PE.getType()) {
                    studentTimeTable.setClassName("体育");
                    studentTimeTable.setCourseId(Constant.EMPTY);
                    studentTimeTable.setClassRoom(Constant.EMPTY);
                    studentTimeTable.setType(courseItemDTO.getType());
                    studentTimeTable.setCourseItemId(courseItemDTO.getId());
                } else if (type == ZoubanType.OTHER.getType()) {
                    studentTimeTable.setClassName(courseItemDTO.getCourseIdList().get(0).getCourseName());
                    studentTimeTable.setCourseId(courseItemDTO.getCourseIdList().get(0).getCourseId());
                    studentTimeTable.setClassRoom(courseItemDTO.getCourseIdList().get(0).getClassRoom());
                    studentTimeTable.setType(courseItemDTO.getType());
                    studentTimeTable.setCourseItemId(courseItemDTO.getId());
                } else if (type == ZoubanType.INTEREST.getType()) {
                    studentTimeTable.setClassName("拓展课" + courseItemDTO.getCourseIdList().size());
                    studentTimeTable.setCourseId("");
                    studentTimeTable.setClassRoom("");
                    studentTimeTable.setType(courseItemDTO.getType());
                    studentTimeTable.setCourseItemId(courseItemDTO.getId());
                }
                studentTimeTableArrayList.add(studentTimeTable);
            }
        }
        //填充课表详细信息
        List<ObjectId> courseIds = new ArrayList<ObjectId>();
        for (StudentTimeTable s : studentTimeTableArrayList) {
            if (!s.getCourseId().equals(Constant.EMPTY)) {
                if (s.getCourseId().indexOf(",") > -1) {
                    String[] corIds = s.getCourseId().split(",");
                    for (String cid : corIds) {
                        courseIds.add(new ObjectId(cid));
                    }
                } else {
                    courseIds.add(new ObjectId(s.getCourseId()));
                }
            }
        }
        List<ZouBanCourseEntry> zouBanCourseEntries = new ArrayList<ZouBanCourseEntry>();
        if (!courseIds.isEmpty())
            zouBanCourseEntries = zoubanCourseDao.findCourseListByIds(courseIds);
        List<ObjectId> classroomIds = new ArrayList<ObjectId>();
        Map<String, ZouBanCourseEntry> zouBanCourseEntryMap = new HashMap<String, ZouBanCourseEntry>();
        for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntries) {
            zouBanCourseEntryMap.put(zouBanCourseEntry.getID().toString(), zouBanCourseEntry);
        }
        for (StudentTimeTable s : studentTimeTableArrayList) {
            if (s.getCourseId().indexOf(",") > -1) {
                String[] corIds = s.getCourseId().split(",");
                ZouBanCourseEntry z1 = zouBanCourseEntryMap.get(corIds[0]);
                ZouBanCourseEntry z2 = zouBanCourseEntryMap.get(corIds[1]);
                if (z1.getClassRoomId() != null) {
                    s.setClassRoom(z1.getClassRoomId().toString());
                    classroomIds.add(z1.getClassRoomId());
                } else {
                    s.setClassRoom("");
                }
                s.setClassName(z1.getClassName() + "/" +
                        z2.getClassName());
                s.setTeacherName(z1.getTeacherName() + "/" + z2.getTeacherName());
            } else {
                ZouBanCourseEntry z = zouBanCourseEntryMap.get(s.getCourseId());
                if (!s.getCourseId().equals(Constant.EMPTY)) {
                    s.setClassName(z.getClassName());
                    if (z.getClassRoomId() != null) {
                        s.setClassRoom(z.getClassRoomId().toString());
                        classroomIds.add(z.getClassRoomId());
                    } else {
                        s.setClassRoom("");
                    }
                    s.setTeacherName(z.getTeacherName());
                    s.setTeacherId(z.getTeacherId().toString());
                }
            }
        }

        Map<ObjectId, ClassroomEntry> classroomEntryMap = classroomService.findClassRoomEntryMap(classroomIds);
        for (StudentTimeTable s : studentTimeTableArrayList) {
            if (!s.getClassRoom().equals("")) {
                if (classroomEntryMap.get(new ObjectId(s.getClassRoom())) != null)
                    s.setClassRoom(classroomEntryMap.get(new ObjectId(s.getClassRoom())).getRoomName());
            }
        }
        return studentTimeTableArrayList;
    }


    /**
     * 获取行政班明细------finish
     *
     * @param term
     * @param classId
     * @param xIndex
     * @param yIndex
     * @param t
     * @return
     */
    public List<ClassDetail> getDetailList(String term, String classId, int xIndex, int yIndex, int t, int week) {
        TimeTableDTO timeTableDTO = findTimeTable(term, classId, TimetableState.PUBLISHED.getState(), week);

        List<CourseItemDTO> courseItemDTOList = timeTableDTO.getCourseList();
        List<String> courseIds = new ArrayList<String>();
        int type = -1;
        if (courseItemDTOList != null && !courseItemDTOList.isEmpty()) {
            for (CourseItemDTO courseItemDTO : courseItemDTOList) {
                if (courseItemDTO.getxIndex() == xIndex && courseItemDTO.getyIndex() == yIndex) {
                    for (CourseTeacherRoom ctr : courseItemDTO.getCourseIdList()) {
                        courseIds.add(ctr.getCourseId());
                    }
                    type = courseItemDTO.getType();
                    //break;
                }
            }
        }
        List<ObjectId> courseObjIds = new ArrayList<ObjectId>();
        for (String s : courseIds) {
            courseObjIds.add(new ObjectId(s));
        }
        //根据classId获取本班学生列表
        ClassEntry classEntry = classDao.getClassEntryById(new ObjectId(classId), Constant.FIELDS);
        List<ObjectId> objectIdList = classEntry.getStudents();
        //再根据courseIds获取具体数据
        List<ClassDetail> list = new ArrayList<ClassDetail>();
        if (type == ZoubanType.ZOUBAN.getType() || type == ZoubanType.GROUPZOUBAN.getType())//走班
        {
            List<ZouBanCourseEntry> zouBanCourseEntries = zoubanCourseDao.findCourseListByIds(courseObjIds);

            for (ZouBanCourseEntry z : zouBanCourseEntries) {
                ClassDetail classDetail = new ClassDetail();
                classDetail.setClassRoom(z.getClassRoomId().toString());
                classDetail.setClassName(z.getClassName());
                classDetail.setTeacherName(z.getTeacherName());
                classDetail.setPeople(z.getStudentList().size());
                //获取本班学生列表
                List<ObjectId> curCourseStus = new ArrayList<ObjectId>();
                curCourseStus.addAll(z.getStudentList());
                curCourseStus.retainAll(objectIdList);
                classDetail.setMyClassAmount(curCourseStus.size());
                list.add(classDetail);
            }
        } else if (type == ZoubanType.FEIZOUBAN.getType()) {
            List<ZouBanCourseEntry> zouBanCourseEntries = zoubanCourseDao.findCourseListByIds(courseObjIds);
            ClassDetail classDetail = new ClassDetail();
            ZouBanCourseEntry z = zouBanCourseEntries.get(0);
            classDetail.setClassRoom(z.getClassRoomId().toString());
            classDetail.setClassName(z.getClassName());
            classDetail.setTeacherName(z.getTeacherName());
            classDetail.setPeople(objectIdList.size());
            classDetail.setMyClassAmount(objectIdList.size());
            list.add(classDetail);
        } else if (type == ZoubanType.PE.getType()) {
            List<ZouBanCourseEntry> zouBanCourseEntries = zoubanCourseDao.findCourseListByIds(courseObjIds);

            for (ZouBanCourseEntry z : zouBanCourseEntries) {
                ClassDetail classDetail = new ClassDetail();
                //classDetail.setClassRoom(z.getClassRoomId().toString());
                classDetail.setClassName(z.getClassName());
                classDetail.setTeacherName(z.getTeacherName());
                classDetail.setPeople(z.getStudentList().size());
                //获取本班学生列表
                List<ObjectId> curCourseStus = new ArrayList<ObjectId>();
                curCourseStus.addAll(z.getStudentList());
                curCourseStus.retainAll(objectIdList);
                classDetail.setMyClassAmount(curCourseStus.size());
                list.add(classDetail);
            }
        } else if (type == ZoubanType.INTEREST.getType())//拓展课
        {
            List<ZouBanCourseEntry> zouBanCourseEntries = zoubanCourseDao.findCourseListByIds(courseObjIds);

            for (ZouBanCourseEntry z : zouBanCourseEntries) {
                ClassDetail classDetail = new ClassDetail();
                classDetail.setClassRoom(z.getClassRoomId().toString());
                classDetail.setClassName(z.getClassName());
                classDetail.setTeacherName(z.getTeacherName());
                classDetail.setPeople(z.getStudentList().size());
                //获取本班学生列表
                List<ObjectId> curCourseStus = new ArrayList<ObjectId>();
                curCourseStus.addAll(z.getStudentList());
                curCourseStus.retainAll(objectIdList);
                classDetail.setMyClassAmount(curCourseStus.size());
                list.add(classDetail);
            }
        }

        return list;
    }


    /**
     * 发布课表
     *
     * @param term
     * @param schoolId
     * @param gradeId
     * @return
     */
    public Map<String, String> publishCourse(String term, ObjectId schoolId, ObjectId gradeId) {
        //第一步：检查所有课是否排完
        //第二步：把原始课表复制到两学期的每一周
        //第三步：删除调课课表

        Map<String, String> map = new HashMap<String, String>();
        //检查课程是否排完
        if (!paikeService.checkZBFinished(term, gradeId.toString())) {
            map.put("result", Constant.FAILD);
            map.put("reason", "走班课未排完");
            return map;
        }

        if (!paikeService.checkFZZBFinished(term, gradeId.toString())) {
            map.put("result", Constant.FAILD);
            map.put("reason", "分层走班课未排完");
            return map;
        }

        if (!paikeService.checkPEFinished(term, gradeId.toString())) {
            map.put("result", Constant.FAILD);
            map.put("reason", "体育课未排完");
            return map;
        }
        if (!paikeService.checkFZBFinished(term, gradeId.toString())) {
            map.put("result", Constant.FAILD);
            map.put("reason", "非走班课未排完");
            return map;
        }

        if (!paikeService.checkDSZFinished(term, gradeId.toString())) {
            map.put("result", Constant.FAILD);
            map.put("reason", "单双周课未排完");
            return map;
        }


        List<TimeTableEntry> timeTableEntries = timeTableDao.findTimeTableByGrade(term, gradeId, 1, 0);
        if (timeTableEntries != null && !timeTableEntries.isEmpty()) {
            //获取本年级教学周
            TermDTO termDTO = termService.findTermDTO(term.substring(0, 11), schoolId);
            for (TimeTableEntry t : timeTableEntries) {
                for (int i = 1; i <= termDTO.getFweek(); i++) {
                    TimeTableEntry timeTableEntry = new TimeTableEntry();
                    timeTableEntry.setSchoolId(t.getSchoolId());
                    timeTableEntry.setGradeId(t.getGradeId());
                    timeTableEntry.setTerm(term.substring(0, 11) + "第一学期");
                    timeTableEntry.setClassId(t.getClassId());
                    timeTableEntry.setCourseList(t.getCourseList());
                    timeTableEntry.setType(TimetableState.PUBLISHED.getState());
                    timeTableEntry.setWeek(i);
                    timeTableDao.addTimeTable(timeTableEntry);
                }
                for (int i = 1; i <= termDTO.getSweek(); i++) {
                    TimeTableEntry timeTableEntry = new TimeTableEntry();
                    timeTableEntry.setSchoolId(t.getSchoolId());
                    timeTableEntry.setGradeId(t.getGradeId());
                    timeTableEntry.setTerm(term.substring(0, 11) + "第二学期");
                    timeTableEntry.setClassId(t.getClassId());
                    timeTableEntry.setCourseList(t.getCourseList());
                    timeTableEntry.setType(TimetableState.PUBLISHED.getState());
                    timeTableEntry.setWeek(i);
                    timeTableDao.addTimeTable(timeTableEntry);
                }
            }

            //删除调课课表
            timeTableDao.deleteTimetableByGradeId(term, gradeId, TimetableState.ADJUSTING.getState());
            timeTableDao.deleteTimetableByGradeId(term, gradeId, TimetableState.ADJUSTED.getState());
        } else {
            map.put("result", Constant.FAILD);
            map.put("reason", "获取课表失败");
            return map;
        }
        map.put("result", Constant.SUCCESS);
        map.put("reason", Constant.EMPTY);
        return map;
    }

    /**
     * 取消发布课表
     *
     * @param term
     * @param schoolId
     * @param gradeId
     * @return
     */
    public Map<String, String> cancelPublishCourse(String term, ObjectId schoolId, ObjectId gradeId) {
        String term1 = term.substring(0, 11) + "第一学期";
        String term2 = term.substring(0, 11) + "第二学期";

        //清空所有已发布课表
        timeTableDao.removeAllPublishedTimetable(schoolId, gradeId);
        deleteZoubanNotice(term1, gradeId);
        deleteZoubanNotice(term2, gradeId);
        Map<String, String> map = new HashMap<String, String>();
        map.put("result", Constant.SUCCESS);
        return map;
    }

    /**
     * 删除本学期的所有调课通知
     *
     * @param term
     * @param gradeId
     */
    private void deleteZoubanNotice(String term, ObjectId gradeId) {
        zoubanNoticeDao.deleteAllNotice(term, gradeId);
    }


    /**
     * 获取本年级学科列表
     *
     * @param term
     * @param gradeId
     * @param schoolId
     * @return
     */
    public List<SubjectView> getSubjectList(String term, String gradeId, String schoolId) {
        List<SubjectView> subjectViewList = new ArrayList<SubjectView>();
        List<ZouBanCourseEntry> zouBanCourseEntries = zoubanCourseDao.findCourseList(term, new ObjectId(gradeId), 1);
        List<String> subjectIds = new ArrayList<String>();
        if (zouBanCourseEntries != null && !zouBanCourseEntries.isEmpty()) {
            for (ZouBanCourseEntry ze : zouBanCourseEntries) {
                subjectIds.add(ze.getSubjectId().toString());
            }
        }
        HashSet h = new HashSet(subjectIds);
        subjectIds.clear();
        subjectIds.addAll(h);
        List<SubjectView> svList = schoolService.findSubjectList(schoolId);
        for (SubjectView sv : svList) {
            if (subjectIds.contains(sv.getId())) {
                subjectViewList.add(sv);
            }
        }
        return subjectViewList;
    }


    //获取学生课表
    public Map<String, Object> getMyTimeTable(String studentId, int week, String userId, int role, String term, String year) {
        Map<String, Object> map = new HashMap<String, Object>();

        List<StudentTimeTable> timeTableList = new ArrayList<StudentTimeTable>();
        if (UserRole.isStudent(role)) {
            String gradeId = classService.getClassEntryByStuId(new ObjectId(userId), Constant.FIELDS).getGradeId().toString();
            timeTableList = getStudentTimeTable(term, year, userId.toString(), gradeId, week, TimetableState.PUBLISHED.getState());
            map.put("course", timeTableList);
            CourseConfDTO courseConfDTO = findCourseConfByGradeId(year, new ObjectId(gradeId));
            map.put("conf", courseConfDTO);
        } else if (UserRole.isParent(role)) {
            String childId = userService.findStuInfoByParentId(userId.toString()).getId();
            String gradeId = classService.getClassEntryByStuId(new ObjectId(childId), Constant.FIELDS).getGradeId().toString();
            timeTableList = getStudentTimeTable(term, year, childId, gradeId, week, TimetableState.PUBLISHED.getState());
            map.put("course", timeTableList);
            CourseConfDTO courseConfDTO = findCourseConfByGradeId(year, new ObjectId(gradeId));
            map.put("conf", courseConfDTO);
        } else {
            String gradeId = classService.getClassEntryByStuId(new ObjectId(studentId), Constant.FIELDS).getGradeId().toString();
            timeTableList = getStudentTimeTable(term, year, studentId, gradeId, week, TimetableState.PUBLISHED.getState());
            map.put("course", timeTableList);
            CourseConfDTO courseConfDTO = findCourseConfByGradeId(year, new ObjectId(gradeId));
            map.put("conf", courseConfDTO);
        }
        return map;
    }

    /**
     * 合并全校的课表结构
     *
     * @param term
     * @param gradeIds
     * @return
     */
    public CourseConfDTO getCourseConf(String term, List<ObjectId> gradeIds) {
        CourseConfDTO courseConfDTO;
        TimetableConfDTO timetableConf = new TimetableConfDTO();
        Map<ObjectId, TimetableConfDTO> condition = new HashMap<ObjectId, TimetableConfDTO>();
        for (ObjectId gradeId : gradeIds) {
            TimetableConfDTO timetableConfDTO = timetableConfService.getTimetableConf(term.substring(0, term.indexOf("第")), gradeId);
            condition.put(gradeId, timetableConfDTO);
        }
        for (Map.Entry<ObjectId, TimetableConfDTO> entry : condition.entrySet()) {
            TimetableConfDTO timetableConfDTO = entry.getValue();
            if (StringUtils.isBlank(timetableConf.getId())) {
                timetableConf.setId(timetableConfDTO.getId());
                timetableConf.setTerm(timetableConfDTO.getTerm());
                timetableConf.setGradeId(timetableConfDTO.getGradeId());
                timetableConf.setClassCount(timetableConfDTO.getClassCount());
                timetableConf.setClassTime(timetableConfDTO.getClassTime());
                timetableConf.setDays(timetableConfDTO.getDays());
            } else {
                if (timetableConf.getDays().size() < timetableConfDTO.getDays().size()) {
                    timetableConf.setDays(timetableConfDTO.getDays());
                }
                if (timetableConf.getClassCount() < timetableConfDTO.getClassCount()) {
                    timetableConf.setClassCount(timetableConfDTO.getClassCount());
                    timetableConf.setClassTime(timetableConfDTO.getClassTime());
                }
            }

        }

        courseConfDTO = new CourseConfDTO(timetableConf);
        return courseConfDTO;
    }

    //获取教师课表
    public Map<String, Object> getTeacherTimeTable(String term, String year, String teacherId, String gradeId, int week, String userId, String schoolId) {
        if (StringUtils.isBlank(teacherId)) {
            teacherId = userId;
        }
        List<TeacherTimeTableItem> teacherTimeTables = getTeacherTimeTable(term, year, teacherId, week);
        Map<String, Object> map = new HashMap<String, Object>();
        CourseConfDTO courseConfDTO;
        if (gradeId.equals("")) {
            courseConfDTO = courseService.findCourseConfList(year, schoolId);
        } else {
            if (("All").equals(gradeId)) {
                List<GradeView> gradeList = schoolService.findGradeList(schoolId);
                TimetableConfDTO timetableConf = timetableConfService.getTimetableConf(term.substring(0, term.indexOf("第")), new ObjectId(gradeList.get(0).getId()));
                courseConfDTO = new CourseConfDTO(timetableConf);
            }else{
                TimetableConfDTO timetableConf = timetableConfService.getTimetableConf(term.substring(0, term.indexOf("第")), new ObjectId(gradeId));
                courseConfDTO = new CourseConfDTO(timetableConf);
            }

        }
        List<String> subjectIds = getSubjectIdsByTeacher(year, teacherId);
        map.put("subject", subjectIds);
        map.put("conf", courseConfDTO);
        map.put("course", teacherTimeTables);
        map.put("notice", null);
        return map;
    }

    //获取教学班课表
    public GradeSubjectCourse getGradeSubjectCourseList(String term, String gradeId, String schoolId) {
        SchoolDTO schoolDTO = schoolService.findSchoolById(schoolId);
        List<Subject> subjectList = schoolDTO.getSubjectList();
        List<ZouBanCourseEntry> zouBanCourseEntries = zoubanCourseDao.findCourseList(term, new ObjectId(gradeId), 1);
        GradeSubjectCourse gradeSubjectCourse = new GradeSubjectCourse();
        gradeSubjectCourse.setGradeId(gradeId);
        List<GradeView> gradeViewList = schoolService.findGradeList(schoolId);
        String gradeName = "";
        if (gradeViewList != null && !gradeViewList.isEmpty()) {
            for (GradeView gradeView : gradeViewList) {
                if (gradeView.getId().equals(gradeId)) {
                    gradeName = gradeView.getName();
                }
            }
        }
        gradeSubjectCourse.setGradeName(gradeName);
        XuanKeDTO xuanKeDTO = xuanKeService.findXuanKeConf(term, gradeId, 1, schoolId);
        if (xuanKeDTO != null && xuanKeDTO.getXuankeId() != null) {
            List<SubjectConfEntry> courseConfEntries = studentXuankeService.getCourseConfList(xuanKeDTO.getXuankeId());
            Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
            List<ClassInfoDTO> classInfoDTOList = classService.findClassByGradeId(gradeId);
            for (SubjectConfEntry c : courseConfEntries) {
                Map<String, String> map2 = new HashMap<String, String>();
                if (c.getType() == 1) {
                    for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntries) {
                        if (zouBanCourseEntry.getSubjectId().equals(c.getSubjectId()) && zouBanCourseEntry.getGradeId().equals(new ObjectId(gradeId))) {
                            if (zouBanCourseEntry.getType() == 1)
                                map2.put(zouBanCourseEntry.getID().toString(), zouBanCourseEntry.getClassName());
                            else {//长征中学物理等课程走班非走班混合
                                if (zouBanCourseEntry.getClassId() != null)
                                    for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
                                        if (zouBanCourseEntry.getClassId() != null)
                                            if (classInfoDTO.getId().equals(zouBanCourseEntry.getClassId().toString())) {
                                                map2.put(zouBanCourseEntry.getID().toString(), classInfoDTO.getClassName());
                                                break;
                                            }
                                    }
                            }
                            //break;
                        }
                    }
                    String subjectName = "";
                    for (Subject s : subjectList) {
                        if (s.getSubjectId().equals(c.getSubjectId())) {
                            subjectName = s.getName();
                            if (map2.size() > 0)
                                map.put(subjectName, map2);
                        }
                    }
                } else if (c.getType() == 2) {
                    for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntries) {
                        if (zouBanCourseEntry.getSubjectId().equals(c.getSubjectId()) && zouBanCourseEntry.getGradeId().equals(new ObjectId(gradeId))) {
                            for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
                                if (zouBanCourseEntry.getClassId() != null)
                                    if (classInfoDTO.getId().equals(zouBanCourseEntry.getClassId().toString())) {
                                        map2.put(zouBanCourseEntry.getID().toString(), classInfoDTO.getClassName());
                                        break;
                                    }
                            }
                        }
                    }
                    String subjectName = "";
                    for (Subject s : subjectList) {
                        if (s.getSubjectId().equals(c.getSubjectId())) {
                            subjectName = s.getName();
                            if (map2.size() > 0)
                                map.put(subjectName, map2);
                        }
                    }
                } else if (c.getType() == 3) {
                    int index = 1;
                    for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntries) {
                        if (zouBanCourseEntry.getSubjectId().equals(c.getSubjectId()) && zouBanCourseEntry.getGradeId().equals(new ObjectId(gradeId))) {
                            map2.put(zouBanCourseEntry.getID().toString(), zouBanCourseEntry.getClassName() + index);
                            index++;
                        }
                    }
                    String subjectName = "";
                    for (Subject s : subjectList) {
                        if (s.getSubjectId().equals(c.getSubjectId())) {
                            subjectName = s.getName();
                            if (map2.size() > 0)
                                map.put(subjectName, map2);
                        }
                    }
                }
                gradeSubjectCourse.setGroupInfo(map);
            }
            Map<String, String> mapTemp = new HashMap<String, String>();
        }
        return gradeSubjectCourse;
    }

    /**
     * 导出教学班课表
     *
     * @param term
     * @param year
     * @param courseId
     * @param gradeId
     * @param week
     * @param response
     */
    public void exportCourseExcel(String term, String year, String courseId, String gradeId, int week, HttpServletResponse response) {
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("教学班课表");

        //获取课表详情
        ZouBanCourseEntry zouBanCourseEntry = zoubanCourseDao.getCourseInfoById(new ObjectId(courseId));
        ClassroomEntry classroomEntry = classroomService.findEntryById(zouBanCourseEntry.getClassRoomId());
        List<PointJson> pointList = getCourseTimeTable(term, courseId, gradeId, 1, week);
        if (pointList == null || pointList.isEmpty()) {
            pointList = getCourseTimeTable(term, courseId, gradeId, 0, week);
        }

        CourseConfDTO courseConfDTO = findCourseConfByGradeId(year, new ObjectId(gradeId));
        int cellCount = courseConfDTO.getClassDays().size();
        HSSFCellStyle cellStyle = wb.createCellStyle();


        //第一行，课程表
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) (60 * 20));
        sheet.addMergedRegion(new Region(0, (short) (0), 0, (short) (cellCount)));
        HSSFCell cel0 = row.createCell((short) 0);
        sheet.setDefaultRowHeight((short) 200);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setWrapText(true);

        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对
        cellStyle.setBorderLeft((short) 2);//左边框
        cellStyle.setBorderTop((short) 2);//上边框
        cellStyle.setBorderRight((short) 2);//右边框
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_NONE);//下边框
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 24); // 字体高度
        font.setFontName("宋体"); // 字体
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 宽度

        HSSFFont font1 = wb.createFont();
        font1.setFontHeightInPoints((short) 8); // 字体高度
        font1.setFontName("宋体"); // 字体
        font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 宽度
        HSSFRichTextString richString = new HSSFRichTextString("课程表\r\n" + term + "第" + week + "周");
        richString.applyFont(font1);
        richString.applyFont(0, 3, font);
        cel0.setCellValue(richString);
        cel0.setCellStyle(cellStyle);


        //第二行
        HSSFRow row1 = sheet.createRow(1);
        row1.setHeight((short) (20 * 20));
        int half = cellCount / 2;
        sheet.addMergedRegion(new Region(1, (short) (0), 1, (short) (half)));
        HSSFCell cel1_0 = row1.createCell((short) 0);
        cellStyle = wb.createCellStyle();
        cel1_0.setCellValue("班级：" + zouBanCourseEntry.getClassName());
        cel1_0.setCellStyle(cellStyle);

        sheet.addMergedRegion(new Region(1, (short) (half + 1), (short) (1), (short) (cellCount)));
        HSSFCell cel1_1 = row1.createCell((short) half + 1);
        cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
//        cel1_1.setCellValue("学生:" + userName);
        cel1_1.setCellStyle(cellStyle);

        //第三行
        HSSFRow row2 = sheet.createRow(2);
        row2.setHeight((short) (20 * 20));
        HSSFCell cel2_0 = row2.createCell((short) 0);
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        HSSFClientAnchor anchor = new HSSFClientAnchor();
        anchor.setAnchor(cel2_0.getCellNum(), row2.getRowNum(), 0, 0, (short) (cel2_0.getCellNum() + 1),
                row2.getRowNum() + 1, 0, 0);
        HSSFSimpleShape shape1 = patriarch.createSimpleShape(anchor);
        shape1.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
        shape1.setLineStyle(HSSFSimpleShape.LINESTYLE_SOLID);
        patriarch.createSimpleShape(anchor);
        sheet.setColumnWidth(0, 4000);
        cellStyle = wb.createCellStyle();
        cellStyle.setBorderLeft((short) 2);//左边框
        cellStyle.setBorderTop((short) 2);//上边框
        cellStyle.setBorderRight((short) 2);//右边框
        cellStyle.setBorderBottom((short) 2);//右边框
        cel2_0.setCellStyle(cellStyle);

        cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对
        cellStyle.setBorderLeft((short) 2);//左边框
        cellStyle.setBorderTop((short) 2);//上边框
        cellStyle.setBorderRight((short) 2);//右边框
        cellStyle.setBorderBottom((short) 2);//右边框
        for (int i = 1; i <= courseConfDTO.getClassDays().size(); i++) {
            HSSFCell cel2_i = row2.createCell((short) i);
            cel2_i.setCellValue("星期" + courseConfDTO.getClassDays().get(i - 1));
            sheet.setColumnWidth(i, 4800);
            cel2_i.setCellStyle(cellStyle);
        }
        //第四行开始
        cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setWrapText(true);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对
        cellStyle.setBorderLeft((short) 2);//左边框
        cellStyle.setBorderTop((short) 2);//上边框
        cellStyle.setBorderRight((short) 2);//右边框
        cellStyle.setBorderBottom((short) 2);//右边框
        for (int i = 0; i < courseConfDTO.getClassTime().size(); i++) {
            HSSFRow rowi = sheet.createRow(3 + i);
            rowi.setHeight((short) (50 * 20));
            HSSFCell celi_0 = rowi.createCell((short) 0);
            celi_0.setCellStyle(cellStyle);
            celi_0.setCellValue("第" + (i + 1) + "节\r\n" + courseConfDTO.getClassTime().get(i));
            for (int j = 0; j < courseConfDTO.getClassDays().size(); j++) {
                HSSFCell celi_j = rowi.createCell((short) j + 1);
                celi_j.setCellStyle(cellStyle);
                for (PointJson st : pointList) {
                    if (st.getX() == courseConfDTO.getClassDays().get(j) &&
                            st.getY() == i + 1) {
                        celi_j.setCellValue(zouBanCourseEntry.getTeacherName() + "\r\n" + classroomEntry.getRoomName());
                        break;
                    }
                }
                for (CourseEventDTO ced : courseConfDTO.getEvents()) {
                    if (ced.getxIndex() == courseConfDTO.getClassDays().get(j) &&
                            ced.getyIndex() == i + 1) {
                        if (ced.getForbidEvent().size() > 0)
                            celi_j.setCellValue(ced.getForbidEvent().get(0));
                        break;
                    }
                }
            }
        }

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
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(zouBanCourseEntry.getClassName() + "课表.xls", "UTF-8"));
            response.setContentLength((int) content.length);
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
     * 导出学生课表
     *
     * @param userId
     * @param term
     * @param userName
     * @param response
     */
    public void exportExcel(String userId, String term, String year, String userName, int week, HttpServletResponse response) {
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("学生课表");

        //获取课表详情
        ClassEntry classEntry = classService.getClassEntryByStuId(new ObjectId(userId), Constant.FIELDS);
        String gradeId = classEntry.getGradeId().toString();

        List<StudentTimeTable> timeTableList = getStudentTimeTable(term, year, userId.toString(), gradeId, week, TimetableState.PUBLISHED.getState());
        CourseConfDTO courseConfDTO = findCourseConfByGradeId(year, new ObjectId(gradeId));
        int cellCount = courseConfDTO.getClassDays().size();
        HSSFCellStyle cellStyle = wb.createCellStyle();


        //第一行，课程表
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) (60 * 20));
        sheet.addMergedRegion(new Region(0, (short) (0), 0, (short) (cellCount)));
        HSSFCell cel0 = row.createCell((short) 0);
        sheet.setDefaultRowHeight((short) 200);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setWrapText(true);

        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对
        cellStyle.setBorderLeft((short) 2);//左边框
        cellStyle.setBorderTop((short) 2);//上边框
        cellStyle.setBorderRight((short) 2);//右边框
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_NONE);//下边框
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 24); // 字体高度
        font.setFontName("宋体"); // 字体
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 宽度

        HSSFFont font1 = wb.createFont();
        font1.setFontHeightInPoints((short) 8); // 字体高度
        font1.setFontName("宋体"); // 字体
        font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 宽度
        HSSFRichTextString richString = new HSSFRichTextString("课程表\r\n" + term + "第" + week + "周");
        richString.applyFont(font1);
        richString.applyFont(0, 3, font);
        cel0.setCellValue(richString);
        cel0.setCellStyle(cellStyle);


        //第二行
        HSSFRow row1 = sheet.createRow(1);
        row1.setHeight((short) (20 * 20));
        int half = cellCount / 2;
        sheet.addMergedRegion(new Region(1, (short) (0), 1, (short) (half)));
        HSSFCell cel1_0 = row1.createCell((short) 0);
        cellStyle = wb.createCellStyle();
        cel1_0.setCellValue("班级：" + classEntry.getName());
        cel1_0.setCellStyle(cellStyle);

        sheet.addMergedRegion(new Region(1, (short) (half + 1), (short) (1), (short) (cellCount)));
        HSSFCell cel1_1 = row1.createCell((short) half + 1);
        cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        cel1_1.setCellValue("学生:" + userName);
        cel1_1.setCellStyle(cellStyle);

        //第三行
        HSSFRow row2 = sheet.createRow(2);
        row2.setHeight((short) (20 * 20));
        HSSFCell cel2_0 = row2.createCell((short) 0);
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        HSSFClientAnchor anchor = new HSSFClientAnchor();
        anchor.setAnchor(cel2_0.getCellNum(), row2.getRowNum(), 0, 0, (short) (cel2_0.getCellNum() + 1),
                row2.getRowNum() + 1, 0, 0);
        HSSFSimpleShape shape1 = patriarch.createSimpleShape(anchor);
        shape1.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
        shape1.setLineStyle(HSSFSimpleShape.LINESTYLE_SOLID);
        patriarch.createSimpleShape(anchor);
        sheet.setColumnWidth(0, 4000);
        cellStyle = wb.createCellStyle();
        cellStyle.setBorderLeft((short) 2);//左边框
        cellStyle.setBorderTop((short) 2);//上边框
        cellStyle.setBorderRight((short) 2);//右边框
        cellStyle.setBorderBottom((short) 2);//右边框
        cel2_0.setCellStyle(cellStyle);

        cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对
        cellStyle.setBorderLeft((short) 2);//左边框
        cellStyle.setBorderTop((short) 2);//上边框
        cellStyle.setBorderRight((short) 2);//右边框
        cellStyle.setBorderBottom((short) 2);//右边框
        for (int i = 1; i <= courseConfDTO.getClassDays().size(); i++) {
            HSSFCell cel2_i = row2.createCell((short) i);
            cel2_i.setCellValue("星期" + courseConfDTO.getClassDays().get(i - 1));
            sheet.setColumnWidth(i, 4800);
            cel2_i.setCellStyle(cellStyle);
        }
        //第四行开始
        cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setWrapText(true);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对
        cellStyle.setBorderLeft((short) 2);//左边框
        cellStyle.setBorderTop((short) 2);//上边框
        cellStyle.setBorderRight((short) 2);//右边框
        cellStyle.setBorderBottom((short) 2);//右边框
        for (int i = 0; i < courseConfDTO.getClassTime().size(); i++) {
            HSSFRow rowi = sheet.createRow(3 + i);
            rowi.setHeight((short) (50 * 20));
            HSSFCell celi_0 = rowi.createCell((short) 0);
            celi_0.setCellStyle(cellStyle);
            celi_0.setCellValue("第" + (i + 1) + "节\r\n" + courseConfDTO.getClassTime().get(i));
            for (int j = 0; j < courseConfDTO.getClassDays().size(); j++) {
                HSSFCell celi_j = rowi.createCell((short) j + 1);
                celi_j.setCellStyle(cellStyle);
                for (StudentTimeTable st : timeTableList) {
                    if (st.getxIndex() == courseConfDTO.getClassDays().get(j) &&
                            st.getyIndex() == i + 1) {
                        if (st.getType() == 5) {
                            celi_j.setCellValue(st.getClassName());
                        } else if (StringUtils.isBlank(st.getTeacherName())) {
                            celi_j.setCellValue(st.getClassName());
                        } else {
                            celi_j.setCellValue(st.getClassName() + "\r\n(" + st.getTeacherName() + ")\r\n" + st.getClassRoom());
                        }
                        break;
                    }
                }
                for (CourseEventDTO ced : courseConfDTO.getEvents()) {
                    if (ced.getxIndex() == courseConfDTO.getClassDays().get(j) &&
                            ced.getyIndex() == i + 1) {
                        if (ced.getForbidEvent().size() > 0)
                            celi_j.setCellValue(ced.getForbidEvent().get(0));
                        break;
                    }
                }
            }
        }

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
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(userName + "课表.xls", "UTF-8"));
            response.setContentLength((int) content.length);
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
     * 导出全部学生的课表
     *
     * @param gradeId
     * @param term
     * @param schoolId
     * @param classId
     * @param response
     */
    public void exportAllExcel(String gradeId, String term, String year, String schoolId, String classId, int tableType, int week, HttpServletResponse response) {
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("学生课表");

        ClassInfoDTO cid = classService.findClassInfoByClassId(classId);
        String className = cid.getClassName();
        Map<ObjectId, UserEntry> map = userService.getUserEntryMap(cid.getStudentIds(), Constant.FIELDS);

        //获取课表详情
        CourseConfDTO courseConfDTO = findCourseConfByGradeId(year, new ObjectId(gradeId));


        HSSFCellStyle cellStyle1 = wb.createCellStyle();
        cellStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle1.setWrapText(true);

        HSSFCellStyle cellStyle2 = wb.createCellStyle();
        cellStyle2 = wb.createCellStyle();
        cellStyle2.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        cellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对

        HSSFCellStyle cellStyle3 = wb.createCellStyle();
        cellStyle3 = wb.createCellStyle();
        cellStyle3.setBorderLeft((short) 2);//左边框
        cellStyle3.setBorderTop((short) 2);//上边框
        cellStyle3.setBorderRight((short) 2);//右边框
        cellStyle3.setBorderBottom((short) 2);//右边框

        HSSFCellStyle cellStyle4 = wb.createCellStyle();
        cellStyle4 = wb.createCellStyle();
        cellStyle4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对
        cellStyle4.setBorderLeft((short) 2);//左边框
        cellStyle4.setBorderTop((short) 2);//上边框
        cellStyle4.setBorderRight((short) 2);//右边框
        cellStyle4.setBorderBottom((short) 2);//右边框

        HSSFCellStyle cellStyle5 = wb.createCellStyle();
        cellStyle5 = wb.createCellStyle();
        cellStyle5.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle5.setWrapText(true);
        cellStyle5.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对
        cellStyle5.setBorderLeft((short) 2);//左边框
        cellStyle5.setBorderTop((short) 2);//上边框
        cellStyle5.setBorderRight((short) 2);//右边框
        cellStyle5.setBorderBottom((short) 2);//右边框
        int index = 0;
        for (Map.Entry<ObjectId, UserEntry> entry : map.entrySet()) {
            int x_index = index % 2;
            int y_index = index / 2;
            String userId = entry.getKey().toString();
            String userName = entry.getValue().getUserName();
            List<StudentTimeTable> timeTableList = getStudentTimeTable(term, year, userId.toString(), gradeId, week, tableType);

            int cellCount = courseConfDTO.getClassDays().size();

            //第一行，课程表
            HSSFRow row;
            if (x_index == 0) {
                row = sheet.createRow(13 * y_index);
            } else
                row = sheet.getRow(13 * y_index);
            row.setHeight((short) (60 * 20));
            sheet.addMergedRegion(new Region(13 * y_index, (short) (7 * x_index), 13 * y_index, (short) (cellCount + 7 * x_index)));
            HSSFCell cel0 = row.createCell((short) 7 * x_index);
            sheet.setDefaultRowHeight((short) 200);

            HSSFFont font = wb.createFont();
            font.setFontHeightInPoints((short) 24); // 字体高度
            font.setFontName("宋体"); // 字体
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 宽度

            HSSFFont font1 = wb.createFont();
            font1.setFontHeightInPoints((short) 8); // 字体高度
            font1.setFontName("宋体"); // 字体
            font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 宽度
            String weekShow = "";
            if (week > 0) {
                weekShow += "第" + week + "周";
            }
            HSSFRichTextString richString = new HSSFRichTextString("课程表\r\n" + term + weekShow);
            richString.applyFont(font1);
            richString.applyFont(0, 3, font);
            cel0.setCellValue(richString);
            cel0.setCellStyle(cellStyle1);


            //第二行
            HSSFRow row1;// = sheet.createRow(1+13*y_index);
            if (x_index == 0) {
                row1 = sheet.createRow(1 + 13 * y_index);
            } else
                row1 = sheet.getRow(1 + 13 * y_index);
            row1.setHeight((short) (20 * 20));
            int half = cellCount / 2;
            sheet.addMergedRegion(new Region(1 + 13 * y_index, (short) (7 * x_index), 1 + 13 * y_index, (short) (half + 7 * x_index)));
            HSSFCell cel1_0 = row1.createCell((short) 7 * x_index);
            cel1_0.setCellValue("班级：" + className);

            sheet.addMergedRegion(new Region(1 + 13 * y_index, (short) (half + 1 + 7 * x_index), (short) (1 + 13 * y_index), (short) (cellCount + 7 * x_index)));
            HSSFCell cel1_1 = row1.createCell((short) half + 1 + 7 * x_index);

            cel1_1.setCellValue("学生:" + userName);
            cel1_1.setCellStyle(cellStyle2);

            //第三行
            HSSFRow row2;//= sheet.createRow(2+13*y_index);
            if (x_index == 0) {
                row2 = sheet.createRow(2 + 13 * y_index);
            } else
                row2 = sheet.getRow(2 + 13 * y_index);
            row2.setHeight((short) (20 * 20));
            HSSFCell cel2_0 = row2.createCell((short) 7 * x_index);
            HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
            HSSFClientAnchor anchor = new HSSFClientAnchor();
            anchor.setAnchor(cel2_0.getCellNum(), row2.getRowNum(), 0, 0, (short) (cel2_0.getCellNum() + 1),
                    row2.getRowNum() + 1, 0, 0);
            HSSFSimpleShape shape1 = patriarch.createSimpleShape(anchor);
            shape1.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
            shape1.setLineStyle(HSSFSimpleShape.LINESTYLE_SOLID);
            patriarch.createSimpleShape(anchor);
            sheet.setColumnWidth(7 * x_index, 4000);

            cel2_0.setCellStyle(cellStyle3);

            for (int i = 1; i <= courseConfDTO.getClassDays().size(); i++) {
                HSSFCell cel2_i = row2.createCell((short) i + 7 * x_index);
                cel2_i.setCellValue("星期" + courseConfDTO.getClassDays().get(i - 1));
                sheet.setColumnWidth(i + 7 * x_index, 4800);
                cel2_i.setCellStyle(cellStyle4);
            }

            for (int i = 0; i < courseConfDTO.getClassTime().size(); i++) {
                HSSFRow rowi;//= sheet.createRow(3+i+13*y_index);
                if (x_index == 0) {
                    rowi = sheet.createRow(3 + i + 13 * y_index);
                } else
                    rowi = sheet.getRow(3 + i + 13 * y_index);
                rowi.setHeight((short) (50 * 20));
                HSSFCell celi_0 = rowi.createCell((short) 7 * x_index);
                celi_0.setCellStyle(cellStyle5);
                celi_0.setCellValue("第" + (i + 1) + "节\r\n" + courseConfDTO.getClassTime().get(i));
                for (int j = 0; j < courseConfDTO.getClassDays().size(); j++) {
                    HSSFCell celi_j = rowi.createCell((short) j + 1 + 7 * x_index);
                    celi_j.setCellStyle(cellStyle5);
                    for (StudentTimeTable st : timeTableList) {
                        if (st.getxIndex() == courseConfDTO.getClassDays().get(j) &&
                                st.getyIndex() == i + 1) {
                            if (StringUtils.isBlank(st.getTeacherName())) {
                                celi_j.setCellValue(st.getClassName());
                            } else {
                                celi_j.setCellValue(st.getClassName() + "\r\n(" + st.getTeacherName() + ")\r\n" + st.getClassRoom());
                            }
                            break;
                        }
                    }
                    for (CourseEventDTO ced : courseConfDTO.getEvents()) {
                        if (ced.getxIndex() == courseConfDTO.getClassDays().get(j) &&
                                ced.getyIndex() == i + 1) {
                            if (ced.getForbidEvent().size() > 0)
                                celi_j.setCellValue(ced.getForbidEvent().get(0));
                            break;
                        }
                    }
                }
            }
            index++;
        }


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
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(className + "学生课表.xls", "UTF-8"));
            response.setContentLength((int) content.length);
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
     * 导出该老师课表
     */

    public void exportCurrTeacherExcel(List<ObjectId> gradeIds, String teacherId, String term, String teacherName, int week, HttpServletResponse response) {
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("教师课表");
        String year = term.substring(0, 11);
        List<TeacherTimeTableItem> teacherTimeTables = getTeacherTimeTable(term, year, teacherId, week);
        CourseConfDTO courseConfDTO = getCourseConf(term, gradeIds);
        int cellCount = courseConfDTO.getClassDays().size();
        HSSFCellStyle cellStyle = wb.createCellStyle();


        //第一行，课程表
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) (60 * 20));
        sheet.addMergedRegion(new Region(0, (short) (0), 0, (short) (cellCount)));
        HSSFCell cel0 = row.createCell((short) 0);
        sheet.setDefaultRowHeight((short) 200);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setWrapText(true);

        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对
        cellStyle.setBorderLeft((short) 2);//左边框
        cellStyle.setBorderTop((short) 2);//上边框
        cellStyle.setBorderRight((short) 2);//右边框
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_NONE);//下边框
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 24); // 字体高度
        font.setFontName("宋体"); // 字体
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 宽度

        HSSFFont font1 = wb.createFont();
        font1.setFontHeightInPoints((short) 8); // 字体高度
        font1.setFontName("宋体"); // 字体
        font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 宽度
        HSSFRichTextString richString = new HSSFRichTextString("课程表\r\n" + term + "第" + week + "周");
        richString.applyFont(font1);
        richString.applyFont(0, 3, font);
        cel0.setCellValue(richString);
        cel0.setCellStyle(cellStyle);


        //第二行
        HSSFRow row1 = sheet.createRow(1);
        row1.setHeight((short) (20 * 20));
        int half = cellCount / 2;
        sheet.addMergedRegion(new Region(1, (short) (0), 1, (short) (half)));
        HSSFCell cel1_0 = row1.createCell((short) 0);
        cellStyle = wb.createCellStyle();
        cel1_0.setCellValue("");
        cel1_0.setCellStyle(cellStyle);

        sheet.addMergedRegion(new Region(1, (short) (half + 1), (short) (1), (short) (cellCount)));
        HSSFCell cel1_1 = row1.createCell((short) half + 1);
        cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        cel1_1.setCellValue("老师:" + teacherName);
        cel1_1.setCellStyle(cellStyle);

        //第三行
        HSSFRow row2 = sheet.createRow(2);
        row2.setHeight((short) (20 * 20));
        HSSFCell cel2_0 = row2.createCell((short) 0);
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        HSSFClientAnchor anchor = new HSSFClientAnchor();
        anchor.setAnchor(cel2_0.getCellNum(), row2.getRowNum(), 0, 0, (short) (cel2_0.getCellNum() + 1),
                row2.getRowNum() + 1, 0, 0);
        HSSFSimpleShape shape1 = patriarch.createSimpleShape(anchor);
        shape1.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
        shape1.setLineStyle(HSSFSimpleShape.LINESTYLE_SOLID);
        patriarch.createSimpleShape(anchor);
        sheet.setColumnWidth(0, 4000);
        cellStyle = wb.createCellStyle();
        cellStyle.setBorderLeft((short) 2);//左边框
        cellStyle.setBorderTop((short) 2);//上边框
        cellStyle.setBorderRight((short) 2);//右边框
        cellStyle.setBorderBottom((short) 2);//右边框
        cel2_0.setCellStyle(cellStyle);

        cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对
        cellStyle.setBorderLeft((short) 2);//左边框
        cellStyle.setBorderTop((short) 2);//上边框
        cellStyle.setBorderRight((short) 2);//右边框
        cellStyle.setBorderBottom((short) 2);//右边框
        for (int i = 1; i <= courseConfDTO.getClassDays().size(); i++) {
            HSSFCell cel2_i = row2.createCell((short) i);
            cel2_i.setCellValue("星期" + courseConfDTO.getClassDays().get(i - 1));
            sheet.setColumnWidth(i, 4800);
            cel2_i.setCellStyle(cellStyle);
        }
        //第四行开始
        cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setWrapText(true);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对
        cellStyle.setBorderLeft((short) 2);//左边框
        cellStyle.setBorderTop((short) 2);//上边框
        cellStyle.setBorderRight((short) 2);//右边框
        cellStyle.setBorderBottom((short) 2);//右边框
        for (int i = 0; i < courseConfDTO.getClassTime().size(); i++) {
            HSSFRow rowi = sheet.createRow(3 + i);
            rowi.setHeight((short) (50 * 20));
            HSSFCell celi_0 = rowi.createCell((short) 0);
            celi_0.setCellStyle(cellStyle);
            celi_0.setCellValue("第" + (i + 1) + "节\r\n" + courseConfDTO.getClassTime().get(i));
            for (int j = 0; j < courseConfDTO.getClassDays().size(); j++) {
                HSSFCell celi_j = rowi.createCell((short) j + 1);
                celi_j.setCellStyle(cellStyle);
                for (TeacherTimeTableItem st : teacherTimeTables) {
                    if (st.getxIndex() == courseConfDTO.getClassDays().get(j) &&
                            st.getyIndex() == i + 1) {
                        celi_j.setCellValue(st.getClassName() + "\r\n(" + st.getClassRoom() + ")");
                        break;
                    }
                }
            }
        }

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
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(teacherName + "课表.xls", "UTF-8"));
            response.setContentLength((int) content.length);
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
     * 导出老师课表
     *
     * @param teacherId
     * @param term
     * @param gradeId
     * @param teacherName
     * @param gradeName
     * @param response
     */
    public void exportTeacherExcel(String teacherId, String term, String gradeId, String teacherName, String gradeName, int week, HttpServletResponse response) {
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("教师课表");

        //获取课表详情
        String year = term.substring(0, 11);
        List<TeacherTimeTableItem> teacherTimeTables = getTeacherTimeTable(term, year, teacherId, week);
        CourseConfDTO courseConfDTO = findCourseConfByGradeId(year, new ObjectId(gradeId));
        int cellCount = courseConfDTO.getClassDays().size();
        HSSFCellStyle cellStyle = wb.createCellStyle();


        //第一行，课程表
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) (60 * 20));
        sheet.addMergedRegion(new Region(0, (short) (0), 0, (short) (cellCount)));
        HSSFCell cel0 = row.createCell((short) 0);
        sheet.setDefaultRowHeight((short) 200);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setWrapText(true);

        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对
        cellStyle.setBorderLeft((short) 2);//左边框
        cellStyle.setBorderTop((short) 2);//上边框
        cellStyle.setBorderRight((short) 2);//右边框
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_NONE);//下边框
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 24); // 字体高度
        font.setFontName("宋体"); // 字体
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 宽度

        HSSFFont font1 = wb.createFont();
        font1.setFontHeightInPoints((short) 8); // 字体高度
        font1.setFontName("宋体"); // 字体
        font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 宽度
        HSSFRichTextString richString = new HSSFRichTextString("课程表\r\n" + term + "第" + week + "周");
        richString.applyFont(font1);
        richString.applyFont(0, 3, font);
        cel0.setCellValue(richString);
        cel0.setCellStyle(cellStyle);


        //第二行
        HSSFRow row1 = sheet.createRow(1);
        row1.setHeight((short) (20 * 20));
        int half = cellCount / 2;
        sheet.addMergedRegion(new Region(1, (short) (0), 1, (short) (half)));
        HSSFCell cel1_0 = row1.createCell((short) 0);
        cellStyle = wb.createCellStyle();
        cel1_0.setCellValue("年级：" + gradeName);
        cel1_0.setCellStyle(cellStyle);

        sheet.addMergedRegion(new Region(1, (short) (half + 1), (short) (1), (short) (cellCount)));
        HSSFCell cel1_1 = row1.createCell((short) half + 1);
        cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        cel1_1.setCellValue("老师:" + teacherName);
        cel1_1.setCellStyle(cellStyle);

        //第三行
        HSSFRow row2 = sheet.createRow(2);
        row2.setHeight((short) (20 * 20));
        HSSFCell cel2_0 = row2.createCell((short) 0);
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        HSSFClientAnchor anchor = new HSSFClientAnchor();
        anchor.setAnchor(cel2_0.getCellNum(), row2.getRowNum(), 0, 0, (short) (cel2_0.getCellNum() + 1),
                row2.getRowNum() + 1, 0, 0);
        HSSFSimpleShape shape1 = patriarch.createSimpleShape(anchor);
        shape1.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
        shape1.setLineStyle(HSSFSimpleShape.LINESTYLE_SOLID);
        patriarch.createSimpleShape(anchor);
        sheet.setColumnWidth(0, 4000);
        cellStyle = wb.createCellStyle();
        cellStyle.setBorderLeft((short) 2);//左边框
        cellStyle.setBorderTop((short) 2);//上边框
        cellStyle.setBorderRight((short) 2);//右边框
        cellStyle.setBorderBottom((short) 2);//右边框
        cel2_0.setCellStyle(cellStyle);

        cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对
        cellStyle.setBorderLeft((short) 2);//左边框
        cellStyle.setBorderTop((short) 2);//上边框
        cellStyle.setBorderRight((short) 2);//右边框
        cellStyle.setBorderBottom((short) 2);//右边框
        for (int i = 1; i <= courseConfDTO.getClassDays().size(); i++) {
            HSSFCell cel2_i = row2.createCell((short) i);
            cel2_i.setCellValue("星期" + courseConfDTO.getClassDays().get(i - 1));
            sheet.setColumnWidth(i, 4800);
            cel2_i.setCellStyle(cellStyle);
        }
        //第四行开始
        cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setWrapText(true);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对
        cellStyle.setBorderLeft((short) 2);//左边框
        cellStyle.setBorderTop((short) 2);//上边框
        cellStyle.setBorderRight((short) 2);//右边框
        cellStyle.setBorderBottom((short) 2);//右边框
        for (int i = 0; i < courseConfDTO.getClassTime().size(); i++) {
            HSSFRow rowi = sheet.createRow(3 + i);
            rowi.setHeight((short) (30 * 20));
            HSSFCell celi_0 = rowi.createCell((short) 0);
            celi_0.setCellStyle(cellStyle);
            celi_0.setCellValue("第" + (i + 1) + "节\r\n" + courseConfDTO.getClassTime().get(i));
            for (int j = 0; j < courseConfDTO.getClassDays().size(); j++) {
                HSSFCell celi_j = rowi.createCell((short) j + 1);
                celi_j.setCellStyle(cellStyle);
                String cellValue = "";
                for (TeacherTimeTableItem st : teacherTimeTables) {
                    if (st.getxIndex() == courseConfDTO.getClassDays().get(j) &&
                            st.getyIndex() == i + 1) {
                        cellValue += st.getClassName() + "\r\n(" + st.getClassRoom() + ")\r\n";
                    }
                }
                celi_j.setCellValue(cellValue);
            }
        }

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
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(teacherName + "课表.xls", "UTF-8"));
            response.setContentLength((int) content.length);
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
     * 导出走班课学生列表
     * @param zouBanCourseDTOList
     */
    public void exportStudentListExcel(List<Map<String, Object>> zouBanCourseDTOList, ObjectId schoolId, HttpServletResponse response){
        HSSFWorkbook wb = new HSSFWorkbook();
        if(zouBanCourseDTOList.size()==0){
            HSSFSheet sheet = wb.createSheet("无数据");
        }else{
            try{
                for(Map<String, Object> m:zouBanCourseDTOList){
                    List<ZouBanCourseDTO> zdList = (List<ZouBanCourseDTO>) m.get("courseList");
                    logger.error("courseList:"+zdList.toString());
                    for(ZouBanCourseDTO zd : zdList){//遍历courseList 每一个course都要生成一个sheet
                        String courseId = zd.getZbCourseId();
                        String courseName = zd.getCourseName();
                        String teacherName = zd.getTeacherName();
                        String sheetName = courseName+"_"+teacherName;
                        HSSFSheet sheet = wb.createSheet(sheetName);
                        //第一行，表头
                        String [] head = {"学号","姓名","班级"};
                        HSSFRow row = sheet.createRow(0);
                        for(int i = 0; i<head.length; i++) {
                            HSSFCell cell = row.createCell(i);
                            cell.setCellValue(head[i]);
                        }
                        //生成sheet
                        Map<String,Object> studentsMap = bianbanService.getCourseStuList(courseId, schoolId);
                        List<ZBStudentDTO> studentList = (List<ZBStudentDTO>) studentsMap.get("studentList");//课程对应的students列表，每一个学生对应sheet的一个行
                        ZBStudentDTO student;
                        for(int j=0 ; j<studentList.size(); j++){
                            student = studentList.get(j);
                            HSSFRow studentRow = sheet.createRow(j+1);
                            HSSFCell cell0 = studentRow.createCell(0);
                            cell0.setCellValue(student.getStudentNum());
                            HSSFCell cell1 = studentRow.createCell(1);
                            cell1.setCellValue(student.getUserName());
                            HSSFCell cell2 = studentRow.createCell(2);
                            cell2.setCellValue(student.getClassName());
                        }
                    }
                }
            }catch(Exception e){
                logger.error("",e);
            }

        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        OutputStream outputStream = null;
        try {
            wb.write(os);
            byte[] content = os.toByteArray();
            outputStream = response.getOutputStream();
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("走班学生名单.xls", "UTF-8"));
            response.setContentLength((int) content.length);
            outputStream.write(content);
        } catch (Exception e) {
            logger.error("流输出异常",e);
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
     * 删除全校课表
     *
     * @param schoolId
     */
    public void removeTimetableBySchoolId(ObjectId schoolId) {
        timeTableDao.deleteTimetableBySchoolId(schoolId);
    }


    /**
     * 删除单双周课
     *
     * @param term
     * @param gradeId
     */
    public void removeCourse(String term, ObjectId gradeId, int type) {
        zoubanCourseDao.removeCourseByType(term, gradeId, type);
        timeTableDao.removeCourse(term, gradeId, type);
    }


    /**
     * 导出年级行政班
     * @param gradeId
     * @param gradeName
     * @param term
     * @param year
     * @param schoolId
     * @param tableType
     * @param week
     * @param response
     */
    public void exportClassExcel(String gradeId, String gradeName, String term, String year, String schoolId, int tableType, int week, HttpServletResponse response) {
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("行政班课表");
        List<ClassInfoDTO> classInfos = classService.findClassByGradeId(gradeId);
        //获取课表详情
        CourseConfDTO courseConfDTO = findCourseConfByGradeId(year, new ObjectId(gradeId));


        HSSFCellStyle cellStyle1 = wb.createCellStyle();
        cellStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle1.setWrapText(true);

        HSSFCellStyle cellStyle2 = wb.createCellStyle();
        cellStyle2 = wb.createCellStyle();
        cellStyle2.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        cellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对

        HSSFCellStyle cellStyle3 = wb.createCellStyle();
        cellStyle3 = wb.createCellStyle();
        cellStyle3.setBorderLeft((short) 2);//左边框
        cellStyle3.setBorderTop((short) 2);//上边框
        cellStyle3.setBorderRight((short) 2);//右边框
        cellStyle3.setBorderBottom((short) 2);//右边框

        HSSFCellStyle cellStyle4 = wb.createCellStyle();
        cellStyle4 = wb.createCellStyle();
        cellStyle4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对
        cellStyle4.setBorderLeft((short) 2);//左边框
        cellStyle4.setBorderTop((short) 2);//上边框
        cellStyle4.setBorderRight((short) 2);//右边框
        cellStyle4.setBorderBottom((short) 2);//右边框

        HSSFCellStyle cellStyle5 = wb.createCellStyle();
        cellStyle5 = wb.createCellStyle();
        cellStyle5.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle5.setWrapText(true);
        cellStyle5.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对
        cellStyle5.setBorderLeft((short) 2);//左边框
        cellStyle5.setBorderTop((short) 2);//上边框
        cellStyle5.setBorderRight((short) 2);//右边框
        cellStyle5.setBorderBottom((short) 2);//右边框
        int index = 0;
        for (ClassInfoDTO classinfo : classInfos) {
            int x_index = index % 2;
            int y_index = index / 2;
            String classId = classinfo.getId();
            String className = classinfo.getClassName();
            List<StudentTimeTable> timeTableList = getClassTimeTable(term, classId, tableType, week);

            int cellCount = courseConfDTO.getClassDays().size();

            //第一行，课程表
            HSSFRow row;
            if (x_index == 0) {
                row = sheet.createRow(13 * y_index);
            } else
                row = sheet.getRow(13 * y_index);
            row.setHeight((short) (60 * 20));
            sheet.addMergedRegion(new Region(13 * y_index, (short) (7 * x_index), 13 * y_index, (short) (cellCount + 7 * x_index)));
            HSSFCell cel0 = row.createCell((short) 7 * x_index);
            sheet.setDefaultRowHeight((short) 200);

            HSSFFont font = wb.createFont();
            font.setFontHeightInPoints((short) 24); // 字体高度
            font.setFontName("宋体"); // 字体
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 宽度

            HSSFFont font1 = wb.createFont();
            font1.setFontHeightInPoints((short) 8); // 字体高度
            font1.setFontName("宋体"); // 字体
            font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 宽度
            String weekShow = "";
            if (week > 0) {
                weekShow += "第" + week + "周";
            }
            HSSFRichTextString richString = new HSSFRichTextString("课程表\r\n" + term + weekShow);
            richString.applyFont(font1);
            richString.applyFont(0, 3, font);
            cel0.setCellValue(richString);
            cel0.setCellStyle(cellStyle1);


            //第二行
            HSSFRow row1;// = sheet.createRow(1+13*y_index);
            if (x_index == 0) {
                row1 = sheet.createRow(1 + 13 * y_index);
            } else
                row1 = sheet.getRow(1 + 13 * y_index);
            row1.setHeight((short) (20 * 20));
            int half = cellCount / 2;
            sheet.addMergedRegion(new Region(1 + 13 * y_index, (short) (7 * x_index), 1 + 13 * y_index, (short) (half + 7 * x_index)));
            HSSFCell cel1_0 = row1.createCell((short) 7 * x_index);
            cel1_0.setCellValue("年级：" + gradeName);

            sheet.addMergedRegion(new Region(1 + 13 * y_index, (short) (half + 1 + 7 * x_index), (short) (1 + 13 * y_index), (short) (cellCount + 7 * x_index)));
            HSSFCell cel1_1 = row1.createCell((short) half + 1 + 7 * x_index);

            cel1_1.setCellValue("班级:" + className);
            cel1_1.setCellStyle(cellStyle2);

            //第三行
            HSSFRow row2;//= sheet.createRow(2+13*y_index);
            if (x_index == 0) {
                row2 = sheet.createRow(2 + 13 * y_index);
            } else
                row2 = sheet.getRow(2 + 13 * y_index);
            row2.setHeight((short) (20 * 20));
            HSSFCell cel2_0 = row2.createCell((short) 7 * x_index);
            HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
            HSSFClientAnchor anchor = new HSSFClientAnchor();
            anchor.setAnchor(cel2_0.getCellNum(), row2.getRowNum(), 0, 0, (short) (cel2_0.getCellNum() + 1),
                    row2.getRowNum() + 1, 0, 0);
            HSSFSimpleShape shape1 = patriarch.createSimpleShape(anchor);
            shape1.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
            shape1.setLineStyle(HSSFSimpleShape.LINESTYLE_SOLID);
            patriarch.createSimpleShape(anchor);
            sheet.setColumnWidth(7 * x_index, 4000);

            cel2_0.setCellStyle(cellStyle3);

            for (int i = 1; i <= courseConfDTO.getClassDays().size(); i++) {
                HSSFCell cel2_i = row2.createCell((short) i + 7 * x_index);
                cel2_i.setCellValue("星期" + courseConfDTO.getClassDays().get(i - 1));
                sheet.setColumnWidth(i + 7 * x_index, 4800);
                cel2_i.setCellStyle(cellStyle4);
            }

            for (int i = 0; i < courseConfDTO.getClassTime().size(); i++) {
                HSSFRow rowi;//= sheet.createRow(3+i+13*y_index);
                if (x_index == 0) {
                    rowi = sheet.createRow(3 + i + 13 * y_index);
                } else
                    rowi = sheet.getRow(3 + i + 13 * y_index);
                rowi.setHeight((short) (50 * 20));
                HSSFCell celi_0 = rowi.createCell((short) 7 * x_index);
                celi_0.setCellStyle(cellStyle5);
                celi_0.setCellValue("第" + (i + 1) + "节\r\n" + courseConfDTO.getClassTime().get(i));
                for (int j = 0; j < courseConfDTO.getClassDays().size(); j++) {
                    HSSFCell celi_j = rowi.createCell((short) j + 1 + 7 * x_index);
                    celi_j.setCellStyle(cellStyle5);
                    for (StudentTimeTable st : timeTableList) {
                        if (st.getxIndex() == courseConfDTO.getClassDays().get(j) &&
                                st.getyIndex() == i + 1) {
                            if (StringUtils.isBlank(st.getTeacherName())) {
                                celi_j.setCellValue(st.getClassName());
                            } else {
                                celi_j.setCellValue(st.getClassName() + "\r\n(" + st.getTeacherName() + ")");
                            }
                            break;
                        }
                    }
                    for (CourseEventDTO ced : courseConfDTO.getEvents()) {
                        if (ced.getxIndex() == courseConfDTO.getClassDays().get(j) &&
                                ced.getyIndex() == i + 1) {
                            if (ced.getForbidEvent().size() > 0)
                                celi_j.setCellValue(ced.getForbidEvent().get(0));
                            break;
                        }
                    }
                }
            }
            index++;
        }


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
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(gradeName + "行政班课表.xls", "UTF-8"));
            response.setContentLength((int) content.length);
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
     * 导出学科老师课表
     * @param courseId
     * @param courseName
     * @param term
     * @param year
     * @param schoolId
     * @param tableType
     * @param week
     * @param response
     */
    public void exportCourseTeacherExcel(String courseId, String courseName, String term, String year, String schoolId, int tableType, int week, HttpServletResponse response) {
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("学科课表");
        List<ObjectId> teacherIds = teacherDao.findTeacherBySubjectId(new ObjectId(courseId));
        Set<ObjectId> teacherIdSet = new HashSet<ObjectId>(teacherIds);
        List<ObjectId> teacherIdList = new ArrayList<ObjectId>(teacherIdSet);
        Map<ObjectId, UserEntry> userMap = userService.getUserEntryMap(teacherIdList, new BasicDBObject("nm", 1));
        List<GradeView> gradeList = schoolService.findGradeList(schoolId);
        //获取课表详情
        CourseConfDTO courseConfDTO = findCourseConfByGradeId(year, new ObjectId(gradeList.get(0).getId()));


        HSSFCellStyle cellStyle1 = wb.createCellStyle();
        cellStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle1.setWrapText(true);

        HSSFCellStyle cellStyle2 = wb.createCellStyle();
        cellStyle2 = wb.createCellStyle();
        cellStyle2.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        cellStyle1.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对

        HSSFCellStyle cellStyle3 = wb.createCellStyle();
        cellStyle3 = wb.createCellStyle();
        cellStyle3.setBorderLeft((short) 2);//左边框
        cellStyle3.setBorderTop((short) 2);//上边框
        cellStyle3.setBorderRight((short) 2);//右边框
        cellStyle3.setBorderBottom((short) 2);//右边框

        HSSFCellStyle cellStyle4 = wb.createCellStyle();
        cellStyle4 = wb.createCellStyle();
        cellStyle4.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle4.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对
        cellStyle4.setBorderLeft((short) 2);//左边框
        cellStyle4.setBorderTop((short) 2);//上边框
        cellStyle4.setBorderRight((short) 2);//右边框
        cellStyle4.setBorderBottom((short) 2);//右边框

        HSSFCellStyle cellStyle5 = wb.createCellStyle();
        cellStyle5 = wb.createCellStyle();
        cellStyle5.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle5.setWrapText(true);
        cellStyle5.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对
        cellStyle5.setBorderLeft((short) 2);//左边框
        cellStyle5.setBorderTop((short) 2);//上边框
        cellStyle5.setBorderRight((short) 2);//右边框
        cellStyle5.setBorderBottom((short) 2);//右边框
        int index = 0;
        for (Map.Entry entry : userMap.entrySet()) {
            int x_index = index % 2;
            int y_index = index / 2;
            String userId = entry.getKey().toString();
            UserEntry user = (UserEntry)entry.getValue();
            String userName = user.getUserName();
            List<TeacherTimeTableItem> teacherTimeTables = getTeacherTimeTable(term, year, userId, week);

            int cellCount = courseConfDTO.getClassDays().size();

            //第一行，课程表
            HSSFRow row;
            if (x_index == 0) {
                row = sheet.createRow(13 * y_index);
            } else
                row = sheet.getRow(13 * y_index);
            row.setHeight((short) (60 * 20));
            sheet.addMergedRegion(new Region(13 * y_index, (short) (7 * x_index), 13 * y_index, (short) (cellCount + 7 * x_index)));
            HSSFCell cel0 = row.createCell((short) 7 * x_index);
            sheet.setDefaultRowHeight((short) 200);

            HSSFFont font = wb.createFont();
            font.setFontHeightInPoints((short) 24); // 字体高度
            font.setFontName("宋体"); // 字体
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 宽度

            HSSFFont font1 = wb.createFont();
            font1.setFontHeightInPoints((short) 8); // 字体高度
            font1.setFontName("宋体"); // 字体
            font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 宽度
            String weekShow = "";
            if (week > 0) {
                weekShow += "第" + week + "周";
            }
            HSSFRichTextString richString = new HSSFRichTextString("课程表\r\n" + term + weekShow);
            richString.applyFont(font1);
            richString.applyFont(0, 3, font);
            cel0.setCellValue(richString);
            cel0.setCellStyle(cellStyle1);


            //第二行
            HSSFRow row1;// = sheet.createRow(1+13*y_index);
            if (x_index == 0) {
                row1 = sheet.createRow(1 + 13 * y_index);
            } else
                row1 = sheet.getRow(1 + 13 * y_index);
            row1.setHeight((short) (20 * 20));
            int half = cellCount / 2;
            sheet.addMergedRegion(new Region(1 + 13 * y_index, (short) (7 * x_index), 1 + 13 * y_index, (short) (half + 7 * x_index)));
            HSSFCell cel1_0 = row1.createCell((short) 7 * x_index);
            cel1_0.setCellValue("学科：" + courseName);

            sheet.addMergedRegion(new Region(1 + 13 * y_index, (short) (half + 1 + 7 * x_index), (short) (1 + 13 * y_index), (short) (cellCount + 7 * x_index)));
            HSSFCell cel1_1 = row1.createCell((short) half + 1 + 7 * x_index);

            cel1_1.setCellValue("老师:" + userName);
            cel1_1.setCellStyle(cellStyle2);

            //第三行
            HSSFRow row2;//= sheet.createRow(2+13*y_index);
            if (x_index == 0) {
                row2 = sheet.createRow(2 + 13 * y_index);
            } else
                row2 = sheet.getRow(2 + 13 * y_index);
            row2.setHeight((short) (20 * 20));
            HSSFCell cel2_0 = row2.createCell((short) 7 * x_index);
            HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
            HSSFClientAnchor anchor = new HSSFClientAnchor();
            anchor.setAnchor(cel2_0.getCellNum(), row2.getRowNum(), 0, 0, (short) (cel2_0.getCellNum() + 1),
                    row2.getRowNum() + 1, 0, 0);
            HSSFSimpleShape shape1 = patriarch.createSimpleShape(anchor);
            shape1.setShapeType(HSSFSimpleShape.OBJECT_TYPE_LINE);
            shape1.setLineStyle(HSSFSimpleShape.LINESTYLE_SOLID);
            patriarch.createSimpleShape(anchor);
            sheet.setColumnWidth(7 * x_index, 4000);

            cel2_0.setCellStyle(cellStyle3);

            for (int i = 1; i <= courseConfDTO.getClassDays().size(); i++) {
                HSSFCell cel2_i = row2.createCell((short) i + 7 * x_index);
                cel2_i.setCellValue("星期" + courseConfDTO.getClassDays().get(i - 1));
                sheet.setColumnWidth(i + 7 * x_index, 4800);
                cel2_i.setCellStyle(cellStyle4);
            }

            for (int i = 0; i < courseConfDTO.getClassTime().size(); i++) {
                HSSFRow rowi;//= sheet.createRow(3+i+13*y_index);
                if (x_index == 0) {
                    rowi = sheet.createRow(3 + i + 13 * y_index);
                } else
                    rowi = sheet.getRow(3 + i + 13 * y_index);
                rowi.setHeight((short) (30 * 20));
                HSSFCell celi_0 = rowi.createCell((short) 7 * x_index);
                celi_0.setCellStyle(cellStyle5);
                celi_0.setCellValue("第" + (i + 1) + "节\r\n" + courseConfDTO.getClassTime().get(i));
                for (int j = 0; j < courseConfDTO.getClassDays().size(); j++) {
                    HSSFCell celi_j = rowi.createCell((short) j + 1 + 7 * x_index);
                    celi_j.setCellStyle(cellStyle5);
                    for (TeacherTimeTableItem teacher : teacherTimeTables) {
                        if (teacher.getxIndex() == courseConfDTO.getClassDays().get(j) &&
                                teacher.getyIndex() == i + 1) {
                            celi_j.setCellValue(teacher.getClassName() + "\r\n" + teacher.getClassRoom());
                            break;
                        }
                    }
                    for (CourseEventDTO ced : courseConfDTO.getEvents()) {
                        if (ced.getxIndex() == courseConfDTO.getClassDays().get(j) &&
                                ced.getyIndex() == i + 1) {
                            if (ced.getForbidEvent().size() > 0)
                                celi_j.setCellValue(ced.getForbidEvent().get(0));
                            break;
                        }
                    }
                }
            }
            index++;
        }


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
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(courseName + "学科课表.xls", "UTF-8"));
            response.setContentLength((int) content.length);
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
}


