package com.fulaan.zouban.service;

import com.db.school.ClassDao;
import com.db.zouban.*;
import com.fulaan.classroom.service.ClassroomService;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.controller.SubjectView;
import com.fulaan.school.service.SchoolService;
import com.fulaan.user.service.UserService;
import com.fulaan.zouban.dto.ClassFenDuanDTO;
import com.fulaan.zouban.dto.ZouBanCourseDTO;
import com.pojo.classroom.ClassroomEntry;
import com.pojo.school.ClassEntry;
import com.pojo.school.ClassInfoDTO;
import com.pojo.school.Subject;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.zouban.*;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.Region;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by qiangm on 2016/4/20.
 */
@Service
public class ZhuZhouService {

    private static final Logger logger = Logger.getLogger(ZhuZhouService.class);
    //@Autowired
    private SchoolService schoolService = new SchoolService();
    //@Autowired
    private UserService userService = new UserService();
    //@Autowired
    private ClassService classService = new ClassService();
    //@Autowired
    private ClassroomService classroomService = new ClassroomService();
    private TermService termService = new TermService();
    private ZouBanCourseDao zouBanCourseDao = new ZouBanCourseDao();
    private TimeTableDao timeTableDao = new TimeTableDao();
    private ZoubanStateService zoubanStateService = new ZoubanStateService();
    private XuanKeConfDao xuanKeConfDao = new XuanKeConfDao();
    private ClassDao classDao = new ClassDao();
    private PaikeService paikeService = new PaikeService();
    private FenDuanDao fenDuanDao = new FenDuanDao();
    private SubjectConfDao subjectConfDao = new SubjectConfDao();
    @Autowired
    private BianbanService bianbanService;

    //查询拓展课
    public List<ZouBanCourseDTO> findZoubanCourseList(String term, String gradeId, String schoolId) {
        List<ZouBanCourseDTO> zouBanCourseDTOs = new ArrayList<ZouBanCourseDTO>();
        List<ObjectId> groupIds = new ArrayList<ObjectId>();
        List<ZouBanCourseEntry> zouBanCourseEntries = zouBanCourseDao.findCourseList(term, new ObjectId(gradeId), 6);
        List<ObjectId> classRoomIds = new ArrayList<ObjectId>();
        for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntries) {
            ZouBanCourseDTO zouBanCourseDTO = new ZouBanCourseDTO(zouBanCourseEntry);
            classRoomIds.add(zouBanCourseEntry.getClassRoomId());
            zouBanCourseDTOs.add(zouBanCourseDTO);
            if (zouBanCourseEntry.getGroupId() != null)
                groupIds.add(zouBanCourseEntry.getGroupId());
        }
        Map<ObjectId, ClassroomEntry> classroomEntryMap = classroomService.findClassRoomEntryMap(classRoomIds);
        List<SubjectView> subjectViewList = schoolService.findSubjectList(schoolId);
        //List<ClassInfoDTO> classInfoDTOList=classService.findClassByGradeId(gradeId);
        List<ClassFengDuanEntry> fengDuanEntries = fenDuanDao.findFenDuanByIds(groupIds);
        for (ZouBanCourseDTO zouBanCourseDTO : zouBanCourseDTOs) {
            zouBanCourseDTO.setClassRoom(classroomEntryMap.get(new ObjectId(zouBanCourseDTO.getClassRoomId())).getRoomName());
            String subjectName = "";
            for (SubjectView sv : subjectViewList) {
                if (sv.getId().equals(zouBanCourseDTO.getSubjectId())) {
                    subjectName = sv.getName();
                }
            }
            for (ClassFengDuanEntry c : fengDuanEntries) {
                if (c.getID().toString().equals(zouBanCourseDTO.getGroupId())) {
                    zouBanCourseDTO.setGroupName(c.getGroupName());
                }
            }
            zouBanCourseDTO.setSubjectName(subjectName);
        }
        return zouBanCourseDTOs;
    }


    //添加拓展课
    public void addInterestCourse(ZouBanCourseDTO zouBanCourseDTO) {
        ZouBanCourseEntry zouBanCourseEntry = zouBanCourseDTO.exportEntry();
        zouBanCourseDao.addZouBanCourseEntry(zouBanCourseEntry);
    }

    //修改拓展课
    public void updateInterestCourse(ZouBanCourseDTO zouBanCourseDTO) {
        ZouBanCourseEntry zouBanCourseEntry = zouBanCourseDTO.exportEntry();
        zouBanCourseDao.updateZoubanCourse(zouBanCourseEntry);
    }

    //删除拓展课
    public void deleteInterestCourse(String courseId) {
        zouBanCourseDao.removeCourseById(new ObjectId(courseId));
    }

    //查看拓展课详情----主要是学生列表
    public List<UserDetailInfoDTO> getInterestCourseDetail(String courseId) {
        ZouBanCourseEntry zouBanCourseEntry = zouBanCourseDao.getCourseInfoById(new ObjectId(courseId));
        List<ObjectId> studentIds = zouBanCourseEntry.getStudentList();
        List<UserDetailInfoDTO> userDetailInfoDTOList = userService.findUserInfoByIds(studentIds);
        return userDetailInfoDTOList;
    }

    //导出拓展课选课模板
    public void exportInterestCourse(String term, String gradeId, String schoolId, HttpServletResponse response) {
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("兴趣拓展课");

        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中
        List<ZouBanCourseEntry> zouBanCourseEntries = zouBanCourseDao.findCourseList(term, new ObjectId(gradeId), 6);
        //排序处理，先音体美，后拓展课
        Collections.sort(zouBanCourseEntries, new Comparator<ZouBanCourseEntry>() {
            @Override
            public int compare(ZouBanCourseEntry o1, ZouBanCourseEntry o2) {
                if (o1.getGroupId() == null && o2.getGroupId() == null)
                    return 0;
                else if (o1.getGroupId() == null)
                    return -1;
                else if (o2.getGroupId() == null)
                    return 1;
                /*return o1.getGroupId().compareTo(o2.getGroupId());*/
                return o1.getGroupId().compareTo(o2.getGroupId());
            }
        });
        //获取段
        XuankeConfEntry xuankeConfEntry = xuanKeConfDao.findXuanKeConf(term, new ObjectId(gradeId));
        List<ClassFengDuanEntry> classFengDuanEntries = fenDuanDao.getClassFenduanList(xuankeConfEntry.getID());
        Map<String, ObjectId> classDuanMap = new HashMap<String, ObjectId>();
        for (ClassFengDuanEntry entry : classFengDuanEntries) {
            List<ObjectId> classIds = entry.getClassIds();
            for (ObjectId cid : classIds) {
                classDuanMap.put(cid.toString(), entry.getID());
            }
        }
        if (zouBanCourseEntries != null && !zouBanCourseEntries.isEmpty()) {
            //先按照学科分类
            Map<String, List<String>> subjectMaps = new LinkedHashMap<String, List<String>>();
            Map<String, List<ZouBanCourseEntry>> subjectZoubanCourseMaps = new LinkedHashMap<String, List<ZouBanCourseEntry>>();
            List<SubjectView> subjectViewList = schoolService.findSubjectList(schoolId);
            for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntries) {
                String courseName = zouBanCourseEntry.getClassName();
                if (subjectMaps.containsKey(zouBanCourseEntry.getSubjectId().toString())) {
                    List<String> courseList = subjectMaps.get(zouBanCourseEntry.getSubjectId().toString());
                    courseList.add(courseName);
                    subjectMaps.put(zouBanCourseEntry.getSubjectId().toString(), courseList);
                    List<ZouBanCourseEntry> zoubanCourseList = subjectZoubanCourseMaps.get(zouBanCourseEntry.getSubjectId().toString());
                    zoubanCourseList.add(zouBanCourseEntry);
                    subjectZoubanCourseMaps.put(zouBanCourseEntry.getSubjectId().toString(), zoubanCourseList);
                } else {
                    List<String> courseList = new ArrayList<String>();
                    courseList.add(courseName);
                    subjectMaps.put(zouBanCourseEntry.getSubjectId().toString(), courseList);
                    List<ZouBanCourseEntry> zoubanCourseList = new ArrayList<ZouBanCourseEntry>();
                    zoubanCourseList.add(zouBanCourseEntry);
                    subjectZoubanCourseMaps.put(zouBanCourseEntry.getSubjectId().toString(), zoubanCourseList);
                }
            }

            //第一行，科目列表
            HSSFRow row = sheet.createRow(0);
            //第二行，课程列表
            HSSFRow row2 = sheet.createRow(1);

            int from = 2;
            int cellCount = 1;
            int cell1_index = 2;//第一行索引
            int cell2_index = 2;//第二行索引
            for (Map.Entry entry : subjectMaps.entrySet()) {
                String subjectId = entry.getKey().toString();
                String subjectName = "";
                for (SubjectView sv : subjectViewList) {
                    if (sv.getId().equals(subjectId)) {
                        subjectName = sv.getName();
                        List<String> courseList = (List<String>) entry.getValue();
                        cellCount += courseList.size();
                        System.out.println("from:" + from + ",to:" + cellCount);
                        sheet.addMergedRegion(new Region(0, (short) (from), 0, (short) (cellCount)));
                        HSSFCell cel0 = row.createCell(cell1_index);
                        cel0.setCellValue(subjectName);
                        cel0.setCellStyle(cellStyle);
                        for (String str : courseList) {
                            HSSFCell cel1 = row2.createCell(cell2_index);
                            cel1.setCellValue(str);
                            cel1.setCellStyle(cellStyle);
                            cell2_index++;
                        }
                        //rom += cellCount;
                        from = cellCount + 1;
                        //cell1_index+=cellCount;
                        //cell1_index=cellCount;
                        cell1_index = from;
                        break;
                    }
                }
            }
            //第三行开始全部学生,非本单元学生选择的课程输出/
            List<ClassInfoDTO> classInfoDTOList = classService.findClassByGradeId(gradeId);

            int rowIndex = 2;
            for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
                List<ObjectId> userIdList = new ArrayList<ObjectId>();
                userIdList.addAll(classInfoDTO.getStudentIds());

                List<UserDetailInfoDTO> userDetailInfoDTOList = userService.findUserInfoByIds(userIdList);
                for (UserDetailInfoDTO dto : userDetailInfoDTOList) {
                    String studentName = dto.getUserName();
                    HSSFRow rowStu = sheet.createRow(rowIndex);
                    HSSFCell celStu = rowStu.createCell(0);
                    celStu.setCellValue(studentName);
                    HSSFCell celCla = rowStu.createCell(1);
                    celCla.setCellValue(classInfoDTO.getClassName());


                    cell2_index = 2;//第二行索引
                    for (Map.Entry entry : subjectZoubanCourseMaps.entrySet()) {
                        String subjectId = entry.getKey().toString();
                        for (SubjectView sv : subjectViewList) {
                            if (sv.getId().equals(subjectId)) {
                                List<ZouBanCourseEntry> zouBanCourseEntryList = (List<ZouBanCourseEntry>) entry.getValue();
                                for (ZouBanCourseEntry z : zouBanCourseEntryList) {
                                    HSSFCell cel1 = rowStu.createCell(cell2_index);
                                    //todo
                                    try {
                                        if (z.getGroupId() != null && !classDuanMap.get(classInfoDTO.getId()).equals(z.getGroupId())) {
                                            cel1.setCellValue("/");
                                            cel1.setCellStyle(cellStyle);
                                        }
                                        cell2_index++;
                                    } catch (Exception e) {
                                        //System.out.println(e);
                                    }
                                }
                                break;
                            }
                        }
                    }
                    rowIndex++;
                }
            }
            //List<UserDetailInfoDTO> userDetailInfoDTOList = userService.findUserInfoByIds(userIdList);
            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            cellStyle.setWrapText(true);
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
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("拓展课模板.xls", "UTF-8"));
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

    //导入学生兴趣班选课数据
    public Map<String, String> importInterestCourse(String year, String gradeId, String schoolId, InputStream inputStream) {
        Map<String, String> map = new HashMap<String, String>();
        //学生列表
        List<ClassInfoDTO> classInfoDTOList = classService.findClassByGradeId(gradeId);
        List<ObjectId> userIdList = new ArrayList<ObjectId>();
        for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
            userIdList.addAll(classInfoDTO.getStudentIds());
        }
        List<UserDetailInfoDTO> userDetailInfoDTOList = userService.findUserInfoByIds(userIdList);
        //获取拓展课列表
        List<ZouBanCourseEntry> zouBanCourseEntries = zouBanCourseDao.findCourseList(year, new ObjectId(gradeId), 6);
        //人数上限map
        Map<ObjectId, Integer> peopleMaxMap = new HashMap<ObjectId, Integer>();
        //转成map类型
        //学科列表
        List<ObjectId> subjectIds = new ArrayList<ObjectId>();
        Map<String, ZouBanCourseEntry> zouBanCourseEntryMap = new HashMap<String, ZouBanCourseEntry>();
        for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntries) {
            String groupId = "";
            if (zouBanCourseEntry.getGroupId() != null) {
                groupId = zouBanCourseEntry.getGroupId().toString();
            }
            zouBanCourseEntryMap.put(zouBanCourseEntry.getClassName() + groupId, zouBanCourseEntry);
            if (!subjectIds.contains(zouBanCourseEntry.getSubjectId())) {
                subjectIds.add(zouBanCourseEntry.getSubjectId());
            }
            peopleMaxMap.put(zouBanCourseEntry.getID(), zouBanCourseEntry.getMax());
        }

        List<String> courseNames = new ArrayList<String>();
        for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntries) {
            courseNames.add(zouBanCourseEntry.getClassName());
        }
        //获取段
        XuankeConfEntry xuankeConfEntry = xuanKeConfDao.findXuanKeConf(year, new ObjectId(gradeId));
        List<ClassFengDuanEntry> classFengDuanEntries = fenDuanDao.getClassFenduanList(xuankeConfEntry.getID());
        Map<ObjectId, List<ObjectId>> duanUserMap = new HashMap<ObjectId, List<ObjectId>>();
        for (ClassFengDuanEntry entry : classFengDuanEntries) {
            ObjectId groupId = entry.getID();
            List<ObjectId> classIds = entry.getClassIds();
            //userService.ge

            List<ObjectId> userIds = new ArrayList<ObjectId>();
            for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
                if (classIds.contains(new ObjectId(classInfoDTO.getId()))) {
                    userIds.addAll(classInfoDTO.getStudentIds());
                }
            }
            duanUserMap.put(groupId, userIds);
        }
        //解析excel文件
        HSSFWorkbook workbook = null;
        try {
            workbook = new HSSFWorkbook(inputStream);
            HSSFSheet sheet = workbook.getSheet("兴趣拓展课");
            int rowNum = sheet.getLastRowNum();
            int colNum = courseNames.size() + 2;
            //先判断课程名是否合适
            for (int i = 2; i < colNum; i++) {
                String celVal = getStringCellValue(sheet.getRow(1).getCell(i));
                boolean find = false;
                for (String str : courseNames) {
                    if (str.equals(celVal)) {
                        find = true;
                        break;
                    }
                }
                if (!find) {
                    map.put("result", "fail");
                    map.put("reason", celVal + "未找到");
                    map.put("line", "2");
                    return map;
                }
            }
            //判断学生姓名是否都合适
            for (int i = 2; i <= rowNum; i++) {
                String studentName = getStringCellValue(sheet.getRow(i).getCell(0)).trim();
                boolean find = false;
                for (UserDetailInfoDTO user : userDetailInfoDTOList) {
                    if (user.getUserName().equals(studentName)) {
                        find = true;
                        break;
                    }
                }
                if (!find) {
                    map.put("result", "fail");
                    map.put("reason", studentName + "未找到");
                    map.put("line", Integer.toString(i));
                    return map;
                }
            }
            //判断每个学生是否都有唯一选课
            Map<String, List<String>> studentChoose = new HashMap<String, List<String>>();
            for (int i = 2; i <= rowNum; i++) {
                List<String> courseList = new ArrayList<String>();
                String studentName = getStringCellValue(sheet.getRow(i).getCell(0)).trim();
                for (int j = 2; j < colNum; j++) {
                    String choose = getStringCellValue(sheet.getRow(i).getCell(j)).trim();
                    if (choose.equals("1") || choose.equals("1.0")) {
                        String courseName = getStringCellValue(sheet.getRow(1).getCell(j)).trim();
                        courseList.add(courseName);
                    }
                }
                studentChoose.put(studentName, courseList);
            }
            //导入数据类型
            Map<ObjectId, List<ObjectId>> courseStudentsMap = new HashMap<ObjectId, List<ObjectId>>();
            for (Map.Entry entry : studentChoose.entrySet()) {
                List<String> choose = (List<String>) entry.getValue();
                String studentName = entry.getKey().toString();
                ObjectId studentId = null;
                for (UserDetailInfoDTO user : userDetailInfoDTOList) {
                    if (user.getUserName().equals(studentName)) {
                        studentId = new ObjectId(user.getId());
                    }
                }
                ObjectId duanId = null;
                for (Map.Entry entry1 : duanUserMap.entrySet()) {
                    List<ObjectId> userIds = (List<ObjectId>) entry1.getValue();
                    if (userIds.contains(studentId)) {
                        duanId = (ObjectId) entry1.getKey();
                        break;
                    }
                }
                List<ZouBanCourseEntry> choosedZoubanList = new ArrayList<ZouBanCourseEntry>();
                for (String str : choose) {
                    //todo 此处改为结合班级和课程名确定走班
                    ZouBanCourseEntry zouBanCourseEntry = zouBanCourseEntryMap.get(str);
                    if (zouBanCourseEntry == null) {
                        zouBanCourseEntry = zouBanCourseEntryMap.get(str + duanId.toString());
                    }
                    if (zouBanCourseEntry == null) {
                        System.out.println(str + duanId.toString());
                    }
                    choosedZoubanList.add(zouBanCourseEntry);
                }
                //判断是否是相同学科，并且每个学科有唯一选择
                /*if (choosedZoubanList.size() != subjectIds.size()) {
                    map.put("result", "fail");
                    map.put("reason", studentName + "选课不完整");
                    map.put("line", "0");
                    return map;
                }*/
                List<ObjectId> studentSubjectIds = new ArrayList<ObjectId>();
                for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntries) {
                    if (!studentSubjectIds.contains(zouBanCourseEntry.getSubjectId())) {
                        studentSubjectIds.add(zouBanCourseEntry.getSubjectId());
                    }
                }
                //判断是否有同一个学科选两门
                /*if (studentSubjectIds.size() != subjectIds.size()) {
                    map.put("result", "fail");
                    map.put("reason", studentName + "选课重复，同一学科多选课程");
                    map.put("line", "0");
                    return map;
                }*/
                //加入导入数据类型中
                for (ZouBanCourseEntry z : choosedZoubanList) {
                    if (courseStudentsMap.containsKey(z.getID())) {
                        List<ObjectId> studentIds = courseStudentsMap.get(z.getID());
                        studentIds.add(studentId);
                        courseStudentsMap.put(z.getID(), studentIds);
                    } else {
                        List<ObjectId> studentIds = new ArrayList<ObjectId>();
                        studentIds.add(studentId);
                        courseStudentsMap.put(z.getID(), studentIds);
                    }
                }
            }
            //判断每个学科的人数是否超过人数上限
            for (Map.Entry entry : courseStudentsMap.entrySet()) {
                ObjectId zoubanCourseId = (ObjectId) entry.getKey();
                List<ObjectId> studentIdList = (List<ObjectId>) entry.getValue();
                if (studentIdList.size() > peopleMaxMap.get(zoubanCourseId)) {
                    String courseName = "";
                    for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntries) {
                        if (zouBanCourseEntry.getID().equals(zoubanCourseId)) {
                            courseName = zouBanCourseEntry.getClassName();
                        }
                    }
                    map.put("result", "fail");
                    map.put("reason", courseName + "选课人数超过上限");
                    map.put("line", "0");
                    return map;
                }
            }
            //数据检验合格，开始导入数据
            for (Map.Entry entry : courseStudentsMap.entrySet()) {
                ObjectId zoubanCourseId = (ObjectId) entry.getKey();
                List<ObjectId> studentIdList = (List<ObjectId>) entry.getValue();
                zouBanCourseDao.updateSetClassCourseUserInfo(zoubanCourseId, studentIdList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("ZhuZhouService", e);
            map.put("result", "fail");
            map.put("reason", e.toString());
            map.put("line", "0");
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

    //模拟学生随机选课，为了生成数据
    public Map<String, String> autoSetInterestClassStudent(String term, String gradeId, String schoolId) {
        Map<String, String> map = new HashMap<String, String>();
        //本年级全部学生
        List<ClassInfoDTO> classInfoDTOList = classService.findClassByGradeId(gradeId);
        List<ObjectId> userIdList = new ArrayList<ObjectId>();
        for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
            userIdList.addAll(classInfoDTO.getStudentIds());
        }
        List<UserDetailInfoDTO> userDetailInfoDTOList = userService.findUserInfoByIds(userIdList);
        int studentCount = userDetailInfoDTOList.size();
        //全部课程
        List<ZouBanCourseEntry> zouBanCourseEntries = zouBanCourseDao.findCourseList(term, new ObjectId(gradeId), 6);
        Map<ObjectId, List<ObjectId>> subjectGroup = new HashMap<ObjectId, List<ObjectId>>();//按照学科分组
        Map<ObjectId, Integer> subjectPeople = new HashMap<ObjectId, Integer>();//每个学科选课容纳总人数
        Map<ObjectId, Integer> courseMaxMap = new HashMap<ObjectId, Integer>();//每门课的学生人数上限
        for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntries) {
            if (subjectGroup.containsKey(zouBanCourseEntry.getSubjectId())) {
                List<ObjectId> courseIds = subjectGroup.get(zouBanCourseEntry.getSubjectId());
                courseIds.add(zouBanCourseEntry.getID());
                subjectGroup.put(zouBanCourseEntry.getSubjectId(), courseIds);
                int max = subjectPeople.get(zouBanCourseEntry.getSubjectId());
                max += zouBanCourseEntry.getMax();
                subjectPeople.put(zouBanCourseEntry.getSubjectId(), max);
            } else {
                List<ObjectId> courseIds = new ArrayList<ObjectId>();
                courseIds.add(zouBanCourseEntry.getID());
                subjectGroup.put(zouBanCourseEntry.getSubjectId(), courseIds);
                subjectPeople.put(zouBanCourseEntry.getSubjectId(), zouBanCourseEntry.getMax());
            }
            courseMaxMap.put(zouBanCourseEntry.getID(), zouBanCourseEntry.getMax());
        }

        //判断每个学科各课程总数是否低于学生总人数
        for (Map.Entry entry : subjectPeople.entrySet()) {
            int people = (Integer) entry.getValue();
            if (people < studentCount) {
                map.put("result", "fail");
                map.put("reason", "学科课程总人数低于本年级学生总数");
                return map;
            }
        }

        //选课结果
        Map<ObjectId, List<ObjectId>> choosedResult = new HashMap<ObjectId, List<ObjectId>>();
        //为每个学生随机选课
        for (UserDetailInfoDTO userDetailInfoDTO : userDetailInfoDTOList) {
            ObjectId userId = new ObjectId(userDetailInfoDTO.getId());
            //在每个学科中随机选择一个课程,并且保证没有超出上限
            for (Map.Entry entry : subjectGroup.entrySet()) {
                List<ObjectId> courseList = (List<ObjectId>) entry.getValue();
                boolean start = true;
                while (start) {
                    Random random = new Random();
                    ObjectId x = courseList.get(random.nextInt(courseList.size()));
                    //加入选课结果中
                    if (choosedResult.containsKey(x)) {
                        List<ObjectId> studentIds = choosedResult.get(x);
                        if (studentIds.size() < courseMaxMap.get(x)) {
                            studentIds.add(userId);
                            choosedResult.put(x, studentIds);
                            start = false;
                        } else {
                            start = true;
                            courseList.remove(x);
                        }
                    } else {
                        List<ObjectId> studentIds = new ArrayList<ObjectId>();
                        studentIds.add(userId);
                        choosedResult.put(x, studentIds);
                        start = false;
                    }
                }
            }
        }
        //开始导入数据
        for (Map.Entry entry : choosedResult.entrySet()) {
            ObjectId zoubanCourseId = (ObjectId) entry.getKey();
            List<ObjectId> studentIdList = (List<ObjectId>) entry.getValue();
            zouBanCourseDao.updateSetClassCourseUserInfo(zoubanCourseId, studentIdList);
        }

        map.put("result", "success");
        return map;
    }

    //将拓展课一键加入到课表中
    public void addInterestClassToTimetable(String year, String gradeId, String schoolId) {
        //获取该年级下的所有班级
        List<ClassInfoDTO> classInfoDTOList = classService.findClassByGradeId(gradeId);
        List<ObjectId> classIds = new ArrayList<ObjectId>();
        for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
            classIds.add(new ObjectId(classInfoDTO.getId()));
        }
        //先删除该班级已经有的拓展课
        timeTableDao.removeCourse(year, classIds, 6);
        List<ZouBanCourseEntry> zouBanCourseEntries = zouBanCourseDao.findCourseList(year, new ObjectId(gradeId), 6);
        List<CourseItem> courseItemList = new ArrayList<CourseItem>();
        for (ZouBanCourseEntry z : zouBanCourseEntries) {
            List<PointEntry> pointEntries = z.getPointEntry();
            for (PointEntry pointEntry : pointEntries) {
                boolean have = false;
                for (CourseItem courseItem : courseItemList) {
                    if (courseItem.getXIndex() == pointEntry.getX() && courseItem.getYIndex() == pointEntry.getY()) {
                        have = true;
                        List<ObjectId> courseIds = courseItem.getCourse();
                        courseIds.add(z.getID());
                        courseItem.setCourse(courseIds);
                        break;
                    }
                }
                if (!have) {
                    List<ObjectId> courseIds = new ArrayList<ObjectId>();
                    courseIds.add(z.getID());
                    CourseItem courseItem = new CourseItem(new ObjectId(), pointEntry.getX(), pointEntry.getY(), courseIds, 6);
                    courseItemList.add(courseItem);
                }
            }
        }

        //先查询每个班级的课表，无课表会自动创建课表
        for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
            ObjectId classId = new ObjectId(classInfoDTO.getId());
            paikeService.findTimeTable(schoolId, year, classId.toString(), 3, 0);
            timeTableDao.addCourseList(year, classId, 3, courseItemList);
        }
    }

    //非走班编班
    public void fzbBianban(String year, String gradeId, String schoolId) {
        List<ZouBanCourseEntry> courseEntryList = zouBanCourseDao.findCourseList(year, new ObjectId(gradeId), 2);
        if (courseEntryList != null && courseEntryList.size() != 0) {
            zouBanCourseDao.removeCourseByType(year, new ObjectId(gradeId), 2);
        }
        //编班非走班
        XuankeConfEntry xuankeConfEntry = xuanKeConfDao.findXuanKeConf(year, new ObjectId(gradeId));
        Map<ObjectId, Subject> subjectMap = schoolService.getSubjectEntryMap(schoolId);
        //XuankeConfEntry xuankeConfEntry = xuanKeConfDao.findXuanKeConf(year, new ObjectId(gradeId));
        List<SubjectConfEntry> subjectConfEntryList = subjectConfDao.findSubjectConf(xuankeConfEntry.getID(), 2);
        List<ClassEntry> classEntryList = classDao.findClassEntryByGradeId(xuankeConfEntry.getGradeId());

        for (SubjectConfEntry subjectConfEntry : subjectConfEntryList) {
            for (ClassEntry cls : classEntryList) {
                List<ObjectId> classIdList = new ArrayList<ObjectId>();
                classIdList.add(cls.getID());
                zouBanCourseDao.addZouBanCourseEntry(
                        new ZouBanCourseEntry(new ObjectId(schoolId), subjectConfEntry.getSubjectId(), xuankeConfEntry.getTerm(),
                        xuankeConfEntry.getGradeId(), classIdList, null, subjectMap.get(subjectConfEntry.getSubjectId()).getName(),
                        subjectConfEntry.getAdvanceTime(), cls.getStudents(), null, subjectConfEntry.getType()));
            }
        }
        bianbanService.autoSetClassRoom(year, gradeId, schoolId);
        try {
            bianbanService.autoSetTeacher(year, gradeId, schoolId, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //更新选课状态
        zoubanStateService.setZoubanState(year, gradeId, 7);
    }


    //===============================株洲模式下的分段部分================================================
    //添加分段
    public Map<String, String> addFenDuan(ClassFenDuanDTO dto) {
        Map<String, String> map = new HashMap<String, String>();
        fenDuanDao.addFenDuan(dto.export());
        map.put("code", "200");
        return map;
    }

    //修改分段
    public Map<String, String> updateFenDuan(List<ClassFenDuanDTO> classFenDuanDTOs) {
        Map<String, String> map = new HashMap<String, String>();
        for (ClassFenDuanDTO classFenDuanDTO : classFenDuanDTOs) {
            fenDuanDao.updateFenDuan(classFenDuanDTO.export());
        }
        map.put("code", "200");
        return map;
    }

    //删除分组
    public Map<String, String> deleteFenDuan(String fenDuanId) {
        Map<String, String> map = new HashMap<String, String>();
        fenDuanDao.removeFenDuan(new ObjectId(fenDuanId));
        map.put("code", "200");
        return map;
    }

    //查询
    public List<ClassFenDuanDTO> findFenDuanDTOS(String year, String gradeId) {
        XuankeConfEntry xuanke = xuanKeConfDao.findXuanKeConf(year, new ObjectId(gradeId));
        List<ClassFengDuanEntry> classFengDuanEntries = fenDuanDao.getClassFenduanList(xuanke.getID());
        List<ClassFenDuanDTO> classFenDuanDTOs = new ArrayList<ClassFenDuanDTO>();
        List<ClassInfoDTO> classInfoDTOList = classService.findClassByGradeId(gradeId);
        for (ClassFengDuanEntry classFengDuanEntry : classFengDuanEntries) {
            ClassFenDuanDTO classFenDuanDTO = new ClassFenDuanDTO(classFengDuanEntry);
            List<String> classIds = classFenDuanDTO.getClassIds();
            List<String> classNames = new ArrayList<String>();
            for (String cid : classIds) {
                for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
                    if (classInfoDTO.getId().equals(cid)) {
                        classNames.add(classInfoDTO.getClassName());
                        break;
                    }
                }
            }
            classFenDuanDTO.setClassNames(classNames);
            classFenDuanDTOs.add(classFenDuanDTO);
        }
        return classFenDuanDTOs;
    }

    //查询本年级未被编入段的班级
    public List<ClassInfoDTO> getLeftGradeClassList(String year, String gradeId) {
        List<ClassInfoDTO> classInfoDTOList = classService.findClassByGradeId(gradeId);
        XuankeConfEntry xuanke = xuanKeConfDao.findXuanKeConf(year, new ObjectId(gradeId));
        List<ClassFengDuanEntry> classFengDuanEntries = fenDuanDao.getClassFenduanList(xuanke.getID());

        for (ClassFengDuanEntry entry : classFengDuanEntries) {
            List<ObjectId> classIds = entry.getClassIds();
            for (ObjectId cid : classIds) {
                for (ClassInfoDTO c : classInfoDTOList) {
                    if (c.getId().equals(cid.toString())) {
                        classInfoDTOList.remove(c);
                        break;
                    }
                }
            }
        }
        Collections.sort(classInfoDTOList, new Comparator<ClassInfoDTO>() {
            @Override
            public int compare(ClassInfoDTO o1, ClassInfoDTO o2) {
                return o1.getClassName().compareTo(o2.getClassName());
            }
        });
        return classInfoDTOList;
    }

    //通过学生id查询需要选择的拓展课
    public Map<String, Object> findCourseListByStudent(String userId, String schoolId) {
        List<ZouBanCourseDTO> zouBanCourseDTOs = new ArrayList<ZouBanCourseDTO>();
        List<ZouBanCourseDTO> singleZouBanCourseDTOs = new ArrayList<ZouBanCourseDTO>();//面向全年级拓展课
        Map<String, Object> map = new HashMap<String, Object>();

        ClassEntry classEntry = classService.getClassEntryByStuId(new ObjectId(userId), Constant.FIELDS);
        ObjectId classId = classEntry.getID();
        String gradeId = classEntry.getGradeId().toString();
        Map<String, Object> map2 = termService.getCurrentTerm(schoolId);
        if (!map2.containsKey("year")) {
            map.put("result", 0);
            //return map;
        }
        String term = map2.get("year").toString();

        int state = zoubanStateService.getZoubanState(term, schoolId, gradeId);
        if (state == 1)
            state = 0;//未开始
        else if (state == 2)
            state = 2;//正在进行
        else if (state > 2)
            state = 1;//已结束


        map.put("year", term);
        map.put("result", state);
        map.put("term", map2.get("term").toString());


        ObjectId xuankeId = xuanKeConfDao.findXuanKeConf(term, new ObjectId(gradeId)).getID();
        List<ClassFengDuanEntry> classFengDuanEntries = fenDuanDao.getClassFenduanList(xuankeId);
        ObjectId groupId = null;
        for (ClassFengDuanEntry classFengDuanEntry : classFengDuanEntries) {
            if (classFengDuanEntry.getClassIds().contains(classId)) {
                groupId = classFengDuanEntry.getID();
            }
        }
        List<String> choosedCourse = new ArrayList<String>();
        List<ZouBanCourseEntry> zouBanCourseEntries = zouBanCourseDao.findCourseList(term, new ObjectId(gradeId), 6);
        List<ObjectId> classRoomIds = new ArrayList<ObjectId>();
        for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntries) {
            if (zouBanCourseEntry.getGroupId() != null && zouBanCourseEntry.getGroupId().equals(groupId)) {
                if (zouBanCourseEntry.getStudentList().contains(new ObjectId(userId))) {
                    choosedCourse.add(zouBanCourseEntry.getID().toString());
                }
                ZouBanCourseDTO zouBanCourseDTO = new ZouBanCourseDTO(zouBanCourseEntry);
                classRoomIds.add(zouBanCourseEntry.getClassRoomId());
                zouBanCourseDTOs.add(zouBanCourseDTO);
            }
            if (zouBanCourseEntry.getGroupId() == null) {
                if (zouBanCourseEntry.getStudentList().contains(new ObjectId(userId))) {
                    choosedCourse.add(zouBanCourseEntry.getID().toString());
                }
                ZouBanCourseDTO zouBanCourseDTO = new ZouBanCourseDTO(zouBanCourseEntry);
                classRoomIds.add(zouBanCourseEntry.getClassRoomId());
                singleZouBanCourseDTOs.add(zouBanCourseDTO);
            }
        }
        Map<ObjectId, ClassroomEntry> classroomEntryMap = classroomService.findClassRoomEntryMap(classRoomIds);
        List<SubjectView> subjectViewList = schoolService.findSubjectList(schoolId);
        Map<String, List<ZouBanCourseDTO>> groupCourseDTOMap = new HashMap<String, List<ZouBanCourseDTO>>();
        for (ZouBanCourseDTO zouBanCourseDTO : zouBanCourseDTOs) {
            zouBanCourseDTO.setClassRoom(classroomEntryMap.get(new ObjectId(zouBanCourseDTO.getClassRoomId())).getRoomName());
            String subjectName = "";
            for (SubjectView sv : subjectViewList) {
                if (sv.getId().equals(zouBanCourseDTO.getSubjectId())) {
                    subjectName = sv.getName();
                }
            }
            zouBanCourseDTO.setSubjectName(subjectName);
            /*for (Map.Entry entry:groupCourseDTOMap.entrySet())
            {
                if(entry)
            }*/
            if (groupCourseDTOMap.containsKey(zouBanCourseDTO.getSubjectName())) {
                List<ZouBanCourseDTO> zouBanCourseDTOs1 = groupCourseDTOMap.get(zouBanCourseDTO.getSubjectName());
                zouBanCourseDTOs1.add(zouBanCourseDTO);
                groupCourseDTOMap.put(zouBanCourseDTO.getSubjectName(), zouBanCourseDTOs1);
            } else {
                List<ZouBanCourseDTO> zouBanCourseDTOs1 = new ArrayList<ZouBanCourseDTO>();
                zouBanCourseDTOs1.add(zouBanCourseDTO);
                groupCourseDTOMap.put(zouBanCourseDTO.getSubjectName(), zouBanCourseDTOs1);
            }
        }
        for (ZouBanCourseDTO zouBanCourseDTO : singleZouBanCourseDTOs) {
            zouBanCourseDTO.setClassRoom(classroomEntryMap.get(new ObjectId(zouBanCourseDTO.getClassRoomId())).getRoomName());
            String subjectName = "";
            for (SubjectView sv : subjectViewList) {
                if (sv.getId().equals(zouBanCourseDTO.getSubjectId())) {
                    subjectName = sv.getName();
                }
            }
            zouBanCourseDTO.setSubjectName(subjectName);
        }

        map.put("groupCourse", groupCourseDTOMap);
        map.put("singleCourse", singleZouBanCourseDTOs);
        //已选课程
        map.put("choosedCourse", choosedCourse);
        return map;
    }

    //学生选课
    public Map<String, String> studentXuanke(String year, String courseId, String userId) {
        //基本流程
        //1检查courseId对应的课程是否超过人数上限，若未超过，则将该学生添加进去；否则，返回人数已达到上限的结果
        //2若该课程成功选课，则判断该学科是否已经有选过课，有的话则自动删除原来选择的。
        Map<String, String> map = new HashMap<String, String>();
        ZouBanCourseEntry zouBanCourseEntry = zouBanCourseDao.getCourseInfoById(new ObjectId(courseId));
        String quitCourseId = "";//自动退掉的课程id
        if (zouBanCourseEntry.getStudentList().size() >= zouBanCourseEntry.getMax()) {
            map.put("result", "fail");
            map.put("reason", "人数已达上限");
            return map;
        } else {
            List<ObjectId> userIds = zouBanCourseEntry.getStudentList();
            userIds.add(new ObjectId(userId));
            zouBanCourseDao.updateSetClassCourseUserInfo(new ObjectId(courseId), userIds);
            //如果有已经选的课程，删除已选的
            ObjectId subjectId = zouBanCourseEntry.getSubjectId();
            ObjectId groupId = zouBanCourseEntry.getGroupId();
            if (groupId != null)//音体美
            {
                List<ZouBanCourseEntry> zouBanCourseEntries = zouBanCourseDao.findCourseListByGroupId(year, groupId, 6);
                for (ZouBanCourseEntry z : zouBanCourseEntries) {
                    if (!z.getID().equals(new ObjectId(courseId)) && z.getSubjectId().equals(subjectId) && z.getStudentList().contains(new ObjectId(userId))) {
                        //去除该课程中的选课
                        List<ObjectId> courseUserIds = z.getStudentList();
                        courseUserIds.remove(new ObjectId(userId));
                        zouBanCourseDao.updateSetClassCourseUserInfo(z.getID(), courseUserIds);
                        quitCourseId = z.getID().toString();
                        break;
                    }
                }
            } else//全校的拓展课
            {
                List<ZouBanCourseEntry> zouBanCourseEntries = zouBanCourseDao.findCourseListByGroupId(year, groupId, 6);
                for (ZouBanCourseEntry z : zouBanCourseEntries) {
                    if (!z.getID().equals(new ObjectId(courseId)) && z.getStudentList().contains(new ObjectId(userId))) {
                        //去除该课程中的选课
                        List<ObjectId> courseUserIds = z.getStudentList();
                        courseUserIds.remove(new ObjectId(userId));
                        zouBanCourseDao.updateSetClassCourseUserInfo(z.getID(), courseUserIds);
                        quitCourseId = z.getID().toString();
                        break;
                    }
                }
            }
        }
        map.put("result", "success");
        map.put("quit", quitCourseId);
        return map;
    }
}
