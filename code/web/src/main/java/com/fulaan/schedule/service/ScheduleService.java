package com.fulaan.schedule.service;

import com.db.leave.LeaveDao;
import com.db.leave.ReplaceCourseDao;
import com.db.zouban.TimeTableDao;
import com.db.zouban.ZouBanCourseDao;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.controller.SubjectView;
import com.fulaan.schedule.dto.GradeClassDTO;
import com.fulaan.school.service.SchoolService;
import com.fulaan.user.service.UserService;
import com.fulaan.zouban.dto.*;
import com.fulaan.zouban.dto.TermDTO;
import com.fulaan.zouban.service.CourseService;
import com.fulaan.zouban.service.TermService;
import com.mongodb.BasicDBObject;
import com.pojo.school.ClassEntry;
import com.pojo.school.ClassInfoDTO;
import com.pojo.school.Grade;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserInfoDTO;
import com.pojo.zouban.*;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by qiangm on 2016/2/29.
 */
@Service
public class ScheduleService {
    private SchoolService schoolService = new SchoolService();
    private ClassService classService = new ClassService();
    private UserService userService = new UserService();
    private ZouBanCourseDao zoubanCourseDao = new ZouBanCourseDao();
    private TimeTableDao timeTableDao = new TimeTableDao();
    private TermService termService = new TermService();
    private CourseService courseService = new CourseService();
    private ReplaceCourseDao replaceCourseDao = new ReplaceCourseDao();
    private LeaveDao leaveDao = new LeaveDao();

    /**
     * 管理员获取全校的年级班级
     *
     * @param schoolId
     * @return
     */
    public List<GradeClassDTO> getAllClasses(String schoolId) {
        List<GradeClassDTO> gradeClassDTOs = new ArrayList<GradeClassDTO>();
        //获取全校的班级
        SchoolEntry schoolEntry = schoolService.getSchoolEntry(new ObjectId(schoolId), new BasicDBObject());
        List<ObjectId> gradeIds = new ArrayList<ObjectId>();

        for (Grade grade : schoolEntry.getGradeList()) {
            if (grade.getGradeType() != -1) {//过滤毕业班
                gradeIds.add(grade.getGradeId());
            }
        }
        List<ClassInfoDTO> classInfoDTOList = classService.findClassByGradeIds(gradeIds);
        List<ClassEntry> classEntryList = classService.findClassInfoBySchoolId(new ObjectId(schoolId), new BasicDBObject());
        for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
            for (ClassEntry classEntry : classEntryList) {
                if (classInfoDTO.getId().equals(classEntry.getID().toString())) {
                    classInfoDTO.setGradeId(classEntry.getGradeId().toString());
                    break;
                }
            }
        }
        for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
            for (Grade grade : schoolEntry.getGradeList()) {
                if (classInfoDTO.getGradeId().equals(grade.getGradeId().toString())) {
                    classInfoDTO.setGradeName(grade.getName());
                    boolean have = false;
                    for (GradeClassDTO gcd : gradeClassDTOs) {
                        if (gcd.getGradeId().equals(classInfoDTO.getGradeId())) {
                            GradeClassDTO.ClassDTO classDTO = gcd.new ClassDTO();
                            classDTO.setClassId(classInfoDTO.getId());
                            classDTO.setClassName(classInfoDTO.getClassName());
                            gcd.getClassDTOs().add(classDTO);
                            have = true;
                            break;
                        }
                    }
                    if (!have) {
                        GradeClassDTO gcd = new GradeClassDTO();
                        List<GradeClassDTO.ClassDTO> classDTOs = new ArrayList<GradeClassDTO.ClassDTO>();
                        GradeClassDTO.ClassDTO classDTO = gcd.new ClassDTO();
                        classDTO.setClassId(classInfoDTO.getId());
                        classDTO.setClassName(classInfoDTO.getClassName());
                        classDTOs.add(classDTO);
                        gcd.setGradeId(classInfoDTO.getGradeId());
                        gcd.setGradeName(classInfoDTO.getGradeName());
                        gcd.setClassDTOs(classDTOs);
                        gradeClassDTOs.add(gcd);
                    }
                    /*if(classInfoDTO.getGradeId())
                    GradeClassDTO gradeClassDTO = new GradeClassDTO();*/
                    break;
                }
            }
        }
        return gradeClassDTOs;
    }

    //导出模板
    public void exportTemplate(String term, String schoolId, HttpServletResponse response) {
        //获取全校的班级
        SchoolEntry schoolEntry = schoolService.getSchoolEntry(new ObjectId(schoolId), new BasicDBObject());
        List<ObjectId> gradeIds = new ArrayList<ObjectId>();

        for (Grade grade : schoolEntry.getGradeList()) {
            if (grade.getGradeType() != -1) {//过滤毕业班
                gradeIds.add(grade.getGradeId());
            }
        }
        List<ClassInfoDTO> classInfoDTOList = classService.findClassByGradeIds(gradeIds);
        List<ClassEntry> classEntryList = classService.findClassInfoBySchoolId(new ObjectId(schoolId), new BasicDBObject());
        for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
            for (ClassEntry classEntry : classEntryList) {
                if (classInfoDTO.getId().equals(classEntry.getID().toString())) {
                    classInfoDTO.setGradeId(classEntry.getGradeId().toString());
                    break;
                }
            }
        }
        for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
            for (Grade grade : schoolEntry.getGradeList()) {
                if (classInfoDTO.getGradeId().equals(grade.getGradeId().toString())) {
                    classInfoDTO.setGradeName(grade.getName());
                    break;
                }
            }
        }
        String schoolName = schoolEntry.getName();
        CourseConfDTO courseConfDTO = courseService.findCourseConfBySchool(schoolId, term);
        List<Integer> classDays = courseConfDTO.getClassDays();
        int classCount = courseConfDTO.getClassCount();
        HSSFWorkbook wb = new HSSFWorkbook();
        //为每个班级生成一份课表
        for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
            //生成一个sheet1
            HSSFSheet sheet = wb.createSheet(classInfoDTO.getGradeName() + "-" + classInfoDTO.getClassName());

            //第一行，课程表
            HSSFRow row1 = sheet.createRow(0);
            row1.setHeight((short) (20 * 20));
            HSSFCell cel1_0 = row1.createCell((short) 0);
            cel1_0.setCellValue("");

            for (int i = 0; i < classDays.size(); i++) {
                String dayStr = "";
                switch (classDays.get(i)) {
                    case 1:
                        dayStr = "星期一";
                        break;
                    case 2:
                        dayStr = "星期二";
                        break;
                    case 3:
                        dayStr = "星期三";
                        break;
                    case 4:
                        dayStr = "星期四";
                        break;
                    case 5:
                        dayStr = "星期五";
                        break;
                    case 6:
                        dayStr = "星期六";
                        break;
                    case 7:
                        dayStr = "星期日";
                        break;
                    default:
                        break;
                }
                HSSFCell cel1_1 = row1.createCell((short) (1 + i));
                cel1_1.setCellValue(dayStr);
            }
            for (int i = 0; i < classCount; i++) {
                HSSFRow row_for = sheet.createRow(1 + i);
                HSSFCell cel1_1 = row_for.createCell((short) (0));
                cel1_1.setCellValue("第" + (i + 1) + "节");
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
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(term + schoolName + "课表.xls", "UTF-8"));
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

    //导入数据
    public Map<String, String> importExcel(InputStream inputStream, String schoolId, String term) {
        Map<String, String> map = new HashMap<String, String>();
        Map<String, ObjectId> gradeMap = new HashMap<String, ObjectId>();
        Map<String, ObjectId> classMap = new HashMap<String, ObjectId>();
        //获取全校的班级
        SchoolEntry schoolEntry = schoolService.getSchoolEntry(new ObjectId(schoolId), new BasicDBObject());
        List<ObjectId> gradeIds = new ArrayList<ObjectId>();

        for (Grade grade : schoolEntry.getGradeList()) {
            if (grade.getGradeType() != -1) {//过滤毕业班
                gradeIds.add(grade.getGradeId());
            }
        }
        List<ClassInfoDTO> classInfoDTOList = classService.findClassByGradeIds(gradeIds);
        List<ClassEntry> classEntryList = classService.findClassInfoBySchoolId(new ObjectId(schoolId), new BasicDBObject());
        for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
            for (ClassEntry classEntry : classEntryList) {
                if (classInfoDTO.getId().equals(classEntry.getID().toString())) {
                    classInfoDTO.setGradeId(classEntry.getGradeId().toString());
                    break;
                }
            }
        }
        for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
            for (Grade grade : schoolEntry.getGradeList()) {
                if (classInfoDTO.getGradeId().equals(grade.getGradeId().toString())) {
                    classInfoDTO.setGradeName(grade.getName());
                    gradeMap.put(classInfoDTO.getGradeName() + "-" + classInfoDTO.getClassName(), grade.getGradeId());
                    classMap.put(classInfoDTO.getGradeName() + "-" + classInfoDTO.getClassName(), new ObjectId(classInfoDTO.getId()));
                    break;
                }
            }
        }
        //获取subjectMap
        List<SubjectView> subjectViewList = schoolService.findSubjectList(schoolId);
        //获取所有的老师
        List<UserInfoDTO> teachers = userService.findTeatherBySchoolId(schoolId);
        //解析excel数据
        HSSFWorkbook workbook = null;
        try {
            workbook = new HSSFWorkbook(inputStream);
            CourseConfDTO courseConfDTO = courseService.findCourseConfBySchool(schoolId, term);
            List<Integer> classDays = courseConfDTO.getClassDays();
            int classCount = courseConfDTO.getClassCount();
            for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
                String sheetName = classInfoDTO.getGradeName() + "-" + classInfoDTO.getClassName();
                ObjectId subjectId = null;
                ObjectId gradeId = gradeMap.get(sheetName);
                ObjectId classId = classMap.get(sheetName);
                String className = "";
                ObjectId teacherId = null;
                String teacherName = "";
                HSSFSheet scheduleSheet = workbook.getSheet(sheetName);
                int rowNum = scheduleSheet.getLastRowNum();
                TimeTableEntry timeTableEntry = new TimeTableEntry();
                timeTableEntry.setSchoolId(new ObjectId(schoolId));
                timeTableEntry.setTerm(term);
                timeTableEntry.setGradeId(gradeId);
                timeTableEntry.setClassId(classId);
                timeTableEntry.setType(3);
                timeTableEntry.setLock(1);
                timeTableEntry.setWeek(0);
                List<CourseItem> courseItemList = new ArrayList<CourseItem>();
                for (int i = 1; i <= classCount; i++) {
                    for (int j = 1; j <= classDays.size(); j++) {
                        String courseVal = getStringCellValue(scheduleSheet.getRow(i).getCell(j)).trim();
                        courseVal = courseVal.replaceAll("\r", "");
                        courseVal = courseVal.replaceAll("\n", "");
                        courseVal = courseVal.replaceAll("\\r", "");
                        courseVal = courseVal.replaceAll("\\n", "");
                        if (!StringUtils.isBlank(courseVal)) {
                            className = courseVal.split("-")[0];

                            teacherName = courseVal.split("-")[1];
                            subjectId = null;
                            for (SubjectView subjectView : subjectViewList) {
                                if (subjectView.getName().equals(className)) {
                                    subjectId = new ObjectId(subjectView.getId());
                                    break;
                                }
                            }
                            if (subjectId == null) {
                                map.put("result", "fail");
                                map.put("reason", "课程名有误");
                                map.put("line", sheetName + "第" + (i + 1) + "行第" + (j + 1) + "列");
                                return map;
                            }
                            teacherId = null;
                            for (UserInfoDTO userInfoDTO : teachers) {
                                if (userInfoDTO.getName().equals(teacherName)) {
                                    teacherId = new ObjectId(userInfoDTO.getId());
                                    break;
                                }
                            }
                            if (teacherId == null) {
                                map.put("result", "fail");
                                map.put("reason", "老师名有误");
                                map.put("line", sheetName + "第" + (i + 1) + "行第" + (j + 1) + "列");
                                return map;
                            }
                        }
                    }
                }
            }
            //解析无误，开始添加数据库
            for (Grade grade : schoolEntry.getGradeList()) {
                if (grade.getGradeType() != -1) {//过滤毕业班
                    zoubanCourseDao.removeCourse(term, grade.getGradeId());
                    //先删除本班的course以及timetale
                    timeTableDao.deleteTimetableByGradeId(term, grade.getGradeId(), 3);
                    timeTableDao.deleteTimetableByGradeId(term, grade.getGradeId(), 0);

                }
            }
            //删除代课记录
            replaceCourseDao.removeByTermAndSchool(term, new ObjectId(schoolId));
            leaveDao.removeByTermAndSchool(term, new ObjectId(schoolId));
            for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
                String sheetName = classInfoDTO.getGradeName() + "-" + classInfoDTO.getClassName();
                ObjectId subjectId = null;
                ObjectId gradeId = gradeMap.get(sheetName);
                ObjectId classId = classMap.get(sheetName);
                String className = "";
                ObjectId teacherId = null;
                String teacherName = "";
                HSSFSheet scheduleSheet = workbook.getSheet(sheetName);
                TimeTableEntry timeTableEntry = new TimeTableEntry();
                timeTableEntry.setSchoolId(new ObjectId(schoolId));
                timeTableEntry.setTerm(term);
                timeTableEntry.setGradeId(gradeId);
                timeTableEntry.setClassId(classId);
                timeTableEntry.setType(3);
                timeTableEntry.setLock(1);
                timeTableEntry.setWeek(0);
                List<CourseItem> courseItemList = new ArrayList<CourseItem>();
                for (int i = 1; i <= classCount; i++) {
                    /*String course1=getStringCellValue(scheduleSheet.getRow(i).getCell(1)).trim();
                    String course2=getStringCellValue(scheduleSheet.getRow(i).getCell(2)).trim();
                    String course3=getStringCellValue(scheduleSheet.getRow(i).getCell(3)).trim();
                    String course4=getStringCellValue(scheduleSheet.getRow(i).getCell(4)).trim();
                    String course5=getStringCellValue(scheduleSheet.getRow(i).getCell(5)).trim();
                    String course6=getStringCellValue(scheduleSheet.getRow(i).getCell(6)).trim();
                    String course7=getStringCellValue(scheduleSheet.getRow(i).getCell(7)).trim();
                    if(StringUtils.isBlank(course1)&&StringUtils.isBlank(course2)&&StringUtils.isBlank(course3)&&
                            StringUtils.isBlank(course4)&&StringUtils.isBlank(course5)&&StringUtils.isBlank(course6)&&
                            StringUtils.isBlank(course7))
                    {
                        break;
                    }*/
                    for (int j = 1; j <= classDays.size(); j++) {
                        String courseVal = getStringCellValue(scheduleSheet.getRow(i).getCell(j)).trim();
                        courseVal = courseVal.replaceAll("\r", "");
                        courseVal = courseVal.replaceAll("\n", "");
                        courseVal = courseVal.replaceAll("\\r", "");
                        courseVal = courseVal.replaceAll("\\n", "");
                        if (!StringUtils.isBlank(courseVal)) {
                            className = courseVal.split("-")[0];
                            teacherName = courseVal.split("-")[1];
                            subjectId = null;
                            for (SubjectView subjectView : subjectViewList) {
                                if (subjectView.getName().equals(className)) {
                                    subjectId = new ObjectId(subjectView.getId());
                                    break;
                                }
                            }
                            teacherId = null;
                            for (UserInfoDTO userInfoDTO : teachers) {
                                if (userInfoDTO.getName().equals(teacherName)) {
                                    teacherId = new ObjectId(userInfoDTO.getId());
                                    break;
                                }
                            }
                            if (subjectId != null && teacherId != null) {
                                List<ObjectId> studentIds = new ArrayList<ObjectId>();
                                List<ObjectId> classIdList = new ArrayList<ObjectId>();
                                classIdList.add(classId);
                                ZouBanCourseEntry zouBanCourseEntry = new ZouBanCourseEntry(new ObjectId(schoolId), subjectId, term, gradeId,
                                        classIdList, null, className, 1, teacherId, teacherName, null, studentIds, 2, null);
                                zoubanCourseDao.addZouBanCourseEntry(zouBanCourseEntry);
                                List<ObjectId> courseIds = new ArrayList<ObjectId>();
                                courseIds.add(zouBanCourseEntry.getID());
                                courseItemList.add(new CourseItem(new ObjectId(), classDays.get(j - 1), i, courseIds, 2));
                            }
                        }
                    }
                }
                timeTableEntry.setCourseList(courseItemList);
                timeTableDao.addTimeTable(timeTableEntry);
            }
        } catch (IOException e) {
            e.printStackTrace();
            map.put("result", "fail");
            map.put("reason", "解析失败");
            map.put("line", "");
            return map;
        }
        map.put("result", "success");
        return map;
    }

    /**
     * 获取单元格数据内容为字符串类型的数据
     *
     * @param cell Excel单元格
     * @return String 单元格数据内容
     */
    private String getStringCellValue(HSSFCell cell) {
        if (cell == null) return Constant.EMPTY;
        String strCell = Constant.EMPTY;

        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                strCell = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                strCell = String.valueOf(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            default:
                strCell = Constant.EMPTY;
                break;
        }

        return StringUtils.isBlank(strCell) ? Constant.EMPTY : strCell;
    }

    //发布课表
    public Map<String, String> publishSchedule(String term, ObjectId schoolId) {
        Map<String, String> map = new HashMap<String, String>();
        //获取全校的班级id
        SchoolEntry schoolEntry = schoolService.getSchoolEntry(schoolId, new BasicDBObject());
        List<ObjectId> gradeIds = new ArrayList<ObjectId>();

        for (Grade grade : schoolEntry.getGradeList()) {
            if (grade.getGradeType() != -1) {//过滤毕业班
                gradeIds.add(grade.getGradeId());
            }
        }
        List<ClassInfoDTO> classInfoDTOList = classService.findClassByGradeIds(gradeIds);
        List<TimeTableEntry> timeTableEntries = timeTableDao.findAllTimeTable(term, schoolId, 0, 0);

        for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
            boolean have = false;
            for (TimeTableEntry tte : timeTableEntries) {
                if (classInfoDTO.getId().equals(tte.getClassId().toString())) {
                    have = true;
                    break;
                }
            }
            if (!have) {
                map.put("result", Constant.FAILD);
                map.put("reason", classInfoDTO.getClassName() + "课表未导入");
                return map;
            }
        }
        //List<TimeTableEntry> timeTableEntries=timeTableDao.getCourseTableByGradeId(term,gradeId,3,0);
        if (timeTableEntries != null && !timeTableEntries.isEmpty()) {
            //获取本年级教学周
            TermDTO termDTO = termService.findTermDTO(term.substring(0, 11), schoolId);
            for (TimeTableEntry t : timeTableEntries) {
                if (term.endsWith("第一学期")) {
                    for (int i = 1; i <= termDTO.getFweek(); i++) {
                        TimeTableEntry timeTableEntry = new TimeTableEntry();
                        timeTableEntry.setSchoolId(t.getSchoolId());
                        timeTableEntry.setType(0);//发布
                        timeTableEntry.setGradeId(t.getGradeId());
                        timeTableEntry.setTerm(term);
                        timeTableEntry.setClassId(t.getClassId());
                        timeTableEntry.setCourseList(t.getCourseList());
                        timeTableEntry.setWeek(i);
                        timeTableDao.addTimeTable(timeTableEntry);
                    }
                } else if (term.endsWith("第二学期")) {
                    for (int i = 1; i <= termDTO.getSweek(); i++) {
                        TimeTableEntry timeTableEntry = new TimeTableEntry();
                        timeTableEntry.setSchoolId(t.getSchoolId());
                        timeTableEntry.setType(0);//发布
                        timeTableEntry.setGradeId(t.getGradeId());
                        timeTableEntry.setTerm(term);
                        timeTableEntry.setClassId(t.getClassId());
                        timeTableEntry.setCourseList(t.getCourseList());
                        timeTableEntry.setWeek(i);
                        timeTableDao.addTimeTable(timeTableEntry);
                    }
                }
            }
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

    //获取班级课表
    public Map<String, Object> getClassSchedule(String schoolId, String term, String classId, int week) {
        int type = 2;//0无课表 1有课表未发布 2已发布
        List<StudentTimeTable> studentTimeTables = getClassTimeTable(term, classId, 0, week);
        if (studentTimeTables.size() == 0) {
            studentTimeTables = getClassTimeTable(term, classId, 3, 0);
            type = 1;
        }
        CourseConfDTO courseConfDTO = courseService.findCourseConfBySchool(schoolId, term);
        //记录上课天数以及几节课
        List<Integer> classDays = new ArrayList<Integer>();
        int classCount = 1;

        classDays = courseConfDTO.getClassDays();
        classCount = courseConfDTO.getClassCount();
        List<String> classTime = courseConfDTO.getClassTime();
        if (studentTimeTables.size() == 0) {
            type = 0;
        }

        List<Integer> classCountList = new ArrayList<Integer>();
        for (int i = 1; i <= classCount; i++) {
            classCountList.add(i);
        }
        //填充课表空格
        for (int x : classDays) {
            for (int y : classCountList) {
                boolean have = false;
                for (StudentTimeTable studentTimeTable : studentTimeTables) {
                    if (studentTimeTable.getxIndex() == x && studentTimeTable.getyIndex() == y) {
                        have = true;
                        break;
                    }
                }
                if (!have) {
                    studentTimeTables.add(new StudentTimeTable(x, y));
                }
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("course", studentTimeTables);
        map.put("classCount", classCountList);
        map.put("classDays", classDays);
        map.put("classTime", classTime);
        map.put("type", type);
        return map;
    }

    public List<StudentTimeTable> getClassTimeTable(String term, String classId, int t, int week) {
        TimeTableDTO timeTableDTO = null;
        if (t == 3)//排课中
            timeTableDTO = findTimeTable(term, classId, t, week);
        else if (t == 4)//调课版
            timeTableDTO = findTimeTable(term, classId, 1, week);
        else if (t == 5)//原始课表
            timeTableDTO = findTimeTable(term, classId, 0, week);
        else//状态不定
        {
            timeTableDTO = findTimeTable(term, classId, 1, week);
            if (timeTableDTO == null)
                timeTableDTO = findTimeTable(term, classId, 0, week);
        }
        if (timeTableDTO == null) {
            return new ArrayList<StudentTimeTable>();
        }
        List<CourseItemDTO> courseItemDTOList = timeTableDTO.getCourseList();

        List<StudentTimeTable> studentTimeTableArrayList = new ArrayList<StudentTimeTable>();
        if (courseItemDTOList != null && !courseItemDTOList.isEmpty()) {
            for (CourseItemDTO courseItemDTO : courseItemDTOList) {
                StudentTimeTable studentTimeTable = new StudentTimeTable();
                int type = courseItemDTO.getType();
                if (type == 2)//非走班
                {
                    studentTimeTable.setxIndex(courseItemDTO.getxIndex());
                    studentTimeTable.setyIndex(courseItemDTO.getyIndex());
                    studentTimeTable.setClassName(courseItemDTO.getCourseIdList().get(0).getCourseName());
                    studentTimeTable.setCourseId(courseItemDTO.getCourseIdList().get(0).getCourseId());
                    studentTimeTable.setType(courseItemDTO.getType());
                    studentTimeTableArrayList.add(studentTimeTable);
                }
            }
        }
        //填充课表详细信息
        List<ObjectId> courseIds = new ArrayList<ObjectId>();
        for (StudentTimeTable s : studentTimeTableArrayList) {
            if (!s.getCourseId().equals(Constant.EMPTY))
                courseIds.add(new ObjectId(s.getCourseId()));
        }
        List<ZouBanCourseEntry> zouBanCourseEntries = new ArrayList<ZouBanCourseEntry>();
        if (!courseIds.isEmpty())
            zouBanCourseEntries = zoubanCourseDao.findCourseListByIds(courseIds);
        for (StudentTimeTable s : studentTimeTableArrayList) {
            for (ZouBanCourseEntry z : zouBanCourseEntries) {
                if (!s.getCourseId().equals(Constant.EMPTY) && z.getID().toString().equals(s.getCourseId())) {
                    s.setClassName(z.getClassName());
                    s.setTeacherName(z.getTeacherName());
                }
            }
        }
        return studentTimeTableArrayList;
    }
}
