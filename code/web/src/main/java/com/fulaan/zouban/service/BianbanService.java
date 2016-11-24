package com.fulaan.zouban.service;

import com.db.school.ClassDao;
import com.db.school.TeacherDao;
import com.db.zouban.*;
import com.fulaan.classroom.ClassRoomDTO;
import com.fulaan.classroom.service.ClassroomService;
import com.fulaan.examresult.controller.IdNameDTO;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.controller.SubjectView;
import com.fulaan.school.service.SchoolService;
import com.fulaan.user.service.UserService;
import com.fulaan.zouban.dto.*;
import com.mongodb.BasicDBObject;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.app.IdValuePair;
import com.pojo.classroom.ClassroomEntry;
import com.pojo.school.ClassEntry;
import com.pojo.school.ClassInfoDTO;
import com.pojo.school.Subject;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.user.UserInfoDTO;
import com.pojo.zouban.*;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by wang_xinxin on 2015/10/10.
 */

@Service
public class BianbanService {
    private static final Logger logger = Logger.getLogger(StudentXuankeService.class);
    private XuanKeConfDao xuanKeConfDao = new XuanKeConfDao();
    private ZouBanCourseDao zouBanCourseDao = new ZouBanCourseDao();
    private ClassDao classDao = new ClassDao();
    private FenDuanDao fenDuanDao = new FenDuanDao();
    private SubjectConfDao subjectConfDao = new SubjectConfDao();
    private TeacherDao teacherDao = new TeacherDao();
    private TimeTableDao timeTableDao = new TimeTableDao();

    private ScoreDao scoreDao = new ScoreDao();
    private SchoolSubjectGroupDao schoolSubjectGroupDao = new SchoolSubjectGroupDao();
    private StudentXuankeDao studentXuankeDao = new StudentXuankeDao();

    private ClassService classService = new ClassService();
    private SchoolService schoolService = new SchoolService();
    private UserService userService = new UserService();
    @Autowired
    private ClassroomService classroomService;
    @Autowired
    private TimeTableService timetableService;
    @Autowired
    private ZoubanStateService zoubanStateService;
    @Autowired
    private CZFenbanService czFenbanService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private SubjectConfService subjectConfService;
    @Autowired
    private TimeTableService timeTableService;
    @Autowired
    private PaikeService paikeService;


    /**
     * 分段列表
     *
     * @param term
     * @param gradeId
     * @return
     */
    public List<FenDuanDTO> getFenDuanList(String term, String gradeId, ObjectId schoolId) {
        XuankeConfEntry xuanke = xuanKeConfDao.findXuanKeConf(term, new ObjectId(gradeId));
        List<ClassFengDuanEntry> fengDuanEntryList = fenDuanDao.getClassFenduanList(xuanke.getID());
        List<ClassEntry> classEntryList = classDao.findClassEntryByGradeId(new ObjectId(gradeId));

        Collections.sort(classEntryList, new Comparator<ClassEntry>() {
            @Override
            public int compare(ClassEntry o1, ClassEntry o2) {
                String className1 = o1.getName();
                String className2 = o2.getName();
                String n1 = "";
                String n2 = "";

                try {

                    if (className1.contains("(")) {
                        n1 = className1.substring(className1.indexOf("(") + 1, className1.indexOf(")"));
                    } else {
                        n1 = className1.substring(className1.indexOf("（") + 1, className1.indexOf("）"));
                    }

                    if (className2.contains("(")) {
                        n2 = className2.substring(className2.indexOf("(") + 1, className2.indexOf(")"));
                    } else {
                        n2 = className2.substring(className2.indexOf("（") + 1, className2.indexOf("）"));
                    }

                    return Integer.parseInt(n1) - Integer.parseInt(n2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });


        Map<ObjectId, ClassEntry> classEntryMap = new LinkedHashMap<ObjectId, ClassEntry>();
        for (ClassEntry entry : classEntryList) {
            classEntryMap.put(entry.getID(), entry);
        }

        //如果没有分段，默认分为一段
        if (fengDuanEntryList.size() == 0) {
            ClassFengDuanEntry fengDuanEntry = new ClassFengDuanEntry(xuanke.getID(), 1, new ArrayList<ObjectId>(classEntryMap.keySet()));
            fenDuanDao.addFenDuan(fengDuanEntry);
            zoubanStateService.setZoubanSubState(term, gradeId, 3);
            fengDuanEntryList.add(fengDuanEntry);
            dealWithTimetable(term, gradeId, schoolId);
        }

        List<FenDuanDTO> fenDuanDTOList = new ArrayList<FenDuanDTO>();
        for (ClassFengDuanEntry entry : fengDuanEntryList) {
            FenDuanDTO fenDuanDTO = new FenDuanDTO();
            fenDuanDTO.setId(entry.getID().toString());
            fenDuanDTO.setGroup(entry.getGroup());

            List<IdNameValuePairDTO> classList = new ArrayList<IdNameValuePairDTO>();
            for (ObjectId classId : entry.getClassIds()) {
                ClassEntry classEntry = classEntryMap.get(classId);
                IdNameValuePairDTO classInfo = new IdNameValuePairDTO(classId.toString(), classEntry.getName(), classEntry.getTotalStudent());
                classList.add(classInfo);
            }
            fenDuanDTO.setClassList(classList);

            fenDuanDTOList.add(fenDuanDTO);
        }

        return fenDuanDTOList;
    }

    /**
     * 在处理分段时，先对课表进行处理，有课表则清空走班课，没有则新建课表
     *
     * @param term
     * @param gradeId
     * @param schoolId
     */
    public void dealWithTimetable(String term, String gradeId, ObjectId schoolId) {
        //先查询本年级课表，若有则清空所有的courseItem,若没有则新增本年级课表
        List<ClassInfoDTO> classInfoDTOs = classService.findClassByGradeId(gradeId);
        boolean flag = false;
        for (ClassInfoDTO classInfoDTO : classInfoDTOs) {
            TimeTableEntry timeTableEntry = timeTableService.findTimeTableEntry(term, classInfoDTO.getId(), TimetableState.NOTPUBLISHED.getState(), 0);
            if (null == timeTableEntry) {
                TimeTableEntry tableEntry = new TimeTableEntry(term, schoolId, new ObjectId(gradeId),
                        new ObjectId(classInfoDTO.getId()), null, TimetableState.NOTPUBLISHED.getState(), 0, 0);
                timeTableService.addTimeTableEntry(tableEntry);
            } else {
                flag = true;
            }
        }
        if (flag) {
            //清空所有的courseItem
            paikeService.clearTimetableCourse(term, gradeId, 1, null);
        }
    }

    /**
     * 调整分段获取分段列表
     *
     * @param term
     * @param gradeId
     * @return
     */
    public List<ChangeFenDuanDTO> getChangeFenDuanList(String term, String gradeId) {
        XuankeConfEntry xuanke = xuanKeConfDao.findXuanKeConf(term, new ObjectId(gradeId));
        List<ClassFengDuanEntry> fengDuanEntryList = fenDuanDao.getClassFenduanList(xuanke.getID());
        List<ClassEntry> classEntryList = classDao.findClassEntryByGradeId(new ObjectId(gradeId));

        //分段信息列表
        List<IdNameDTO> groupList = new ArrayList<IdNameDTO>();
        //班级分段Map
        Map<ObjectId, ObjectId> classGroup = new HashMap<ObjectId, ObjectId>();
        for (ClassFengDuanEntry fengDuanEntry : fengDuanEntryList) {
            IdNameDTO fenDuanInfo = new IdNameDTO(fengDuanEntry.getID().toString(), "第" + fengDuanEntry.getGroup() + "段");
            groupList.add(fenDuanInfo);

            for (ObjectId classId : fengDuanEntry.getClassIds()) {
                classGroup.put(classId, fengDuanEntry.getID());
            }
        }

        List<ChangeFenDuanDTO> fenDuanDTOList = new ArrayList<ChangeFenDuanDTO>();
        for (ClassEntry classEntry : classEntryList) {
            ChangeFenDuanDTO changeFenDuanDTO = new ChangeFenDuanDTO();
            changeFenDuanDTO.setClassId(classEntry.getID().toString());
            changeFenDuanDTO.setClassName(classEntry.getName());
            changeFenDuanDTO.setGroupId(classGroup.get(classEntry.getID()).toString());
            changeFenDuanDTO.setGroupList(groupList);

            fenDuanDTOList.add(changeFenDuanDTO);
        }

        return fenDuanDTOList;
    }

    /**
     * 调整分段
     *
     * @param classId
     * @param oldGroupId
     * @param newGroupId
     */
    public void updateFenDuan(String classId, String oldGroupId, String newGroupId) {
        fenDuanDao.removeFenDuanClass(new ObjectId(oldGroupId), new ObjectId(classId));
        fenDuanDao.addFenDuanClass(new ObjectId(newGroupId), new ObjectId(classId));
    }

    /**
     * 自动分段(根据班级数和段数均匀分段)
     *
     * @param term
     * @param gradeId
     * @param count
     * @return
     */
    public void autoFenDuan(String term, String gradeId, int count, ObjectId schoolId) {
        XuankeConfEntry xuankeConfEntry = xuanKeConfDao.findXuanKeConf(term, new ObjectId(gradeId));
        ObjectId xuankeId = xuankeConfEntry.getID();

        //删除已有分段信息
        fenDuanDao.removeFenDuan(xuankeId);
        //清除本年级的所有课表记录
//        timetableService.deleteAllCourse(term, gradeId);
        //处理课表
        dealWithTimetable(term, gradeId, schoolId);
        //清空所有教学班
        zouBanCourseDao.removeCourseByType(term, new ObjectId(gradeId), ZoubanType.ZOUBAN.getType());

        //年级下的班级
        List<ClassInfoDTO> clsList = classService.findClassByGradeId(gradeId);

        //分段算法(先分配整数，再分配余数)
        //每段班级数量
        int classCount = clsList.size() / count;
        //余数
        int leaveCount = clsList.size() % count;
        Map<Integer, Integer> groupMap = new HashMap<Integer, Integer>();
        for (int i = 1; i <= count; i++) {
            groupMap.put(i, classCount);
        }

        for (int i = 1; i <= leaveCount; i++) {
            groupMap.put(i, groupMap.get(i) + 1);
        }

        Map<Integer, List<ObjectId>> group = new HashMap<Integer, List<ObjectId>>();

        int index = 0;
        for (int i = 1; i <= count; i++) {
            int clsCount = groupMap.get(i);
            for (int j = 0; j < clsCount; j++) {
                List<ObjectId> classIdList = new ArrayList<ObjectId>();
                if (group.get(i) != null) {
                    classIdList.addAll(group.get(i));
                }
                classIdList.add(new ObjectId(clsList.get(index).getId()));
                group.put(i, classIdList);
                index++;
            }
        }


        if (group != null && group.size() != 0) {
            //分段
            for (int i = 1; i <= count; i++) {
                fenDuanDao.addFenDuan(new ClassFengDuanEntry(xuankeId, i, group.get(i)));
            }

            //设置进度
            zoubanStateService.setZoubanSubState(term, gradeId, 3);
            zoubanStateService.setZoubanState(term, gradeId, 3);
        }
    }

    /**
     * 下载模板
     *
     * @param term
     * @param gradeId
     * @param schoolId
     * @param subjectList
     */
    public byte[] downloadChengjiTemplate(String term, String gradeId, String schoolId, String[] subjectList) {
        XuankeConfEntry xuankeConfEntry = xuanKeConfDao.findXuanKeConf(term, new ObjectId(gradeId));
        List<ObjectId> subjects = new ArrayList<ObjectId>();
        for (String subStr : subjectList) {
            subjects.add(new ObjectId(subStr));
        }
        //学科配置
        List<SubjectConfEntry> subjectConfEntryList = subjectConfDao.findSubjectConfList(xuankeConfEntry.getID(), ZoubanType.ZOUBAN.getType(), subjects);
        //学科信息
        Map<String, SubjectView> subjectViewMap = schoolService.findSubjectViewMapBySchoolIdAndGradeId(schoolId, gradeId);

        //生成Excel
        HSSFWorkbook wb = new HSSFWorkbook();
        //Excel样式
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中

        for (SubjectConfEntry subjectConfEntry : subjectConfEntryList) {
            //学科名
            String subjectName = subjectViewMap.get(subjectConfEntry.getSubjectId().toString()).getName();

            //生成sheet
            HSSFSheet sheet = wb.createSheet(subjectName);

            //设置表头
            HSSFRow row0 = sheet.createRow(0);
            HSSFCell cell0 = row0.createCell(0);
            cell0.setCellValue("学生姓名");
            cell0.setCellStyle(cellStyle);
            HSSFCell cell1 = row0.createCell(1);
            cell1.setCellValue("成绩");
            cell1.setCellStyle(cellStyle);

            List<ObjectId> stuIdList = new ArrayList<ObjectId>();
            for (IdValuePair ivp : subjectConfEntry.getAdvUsers()) {
                stuIdList.add(ivp.getId());
            }

            Map<ObjectId, UserEntry> userEntryMap = userService.getUserEntryMap(stuIdList, new BasicDBObject("nm", 1));

            int rowCount = 0;
            for (ObjectId stuId : stuIdList) {
                rowCount++;
                HSSFRow row = sheet.createRow(rowCount);
                HSSFCell cell = row.createCell(0);
                cell.setCellValue(userEntryMap.get(stuId).getRealUserName());
                cell = row.createCell(1);
                cell.setCellValue("");
            }
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return os.toByteArray();
    }

    /**
     * 导入学生成绩
     *
     * @param inputStream
     * @param term
     * @param gradeId
     * @param schoolId
     * @throws IOException
     */
    public void importScore(InputStream inputStream, String term, String gradeId, String schoolId) throws Exception {
        XuankeConfEntry xuankeConfEntry = xuanKeConfDao.findXuanKeConf(term, new ObjectId(gradeId));
        //学科配置
        List<SubjectConfEntry> subjectConfEntryList = subjectConfDao.findSubjectConf(xuankeConfEntry.getID(), 1);
        //学科信息
        Map<String, SubjectView> subjectViewMap = schoolService.findSubjectViewMapBySchoolIdAndGradeId(schoolId, gradeId);

        //学科名称id Map
        Map<String, ObjectId> subjectMap = new HashMap<String, ObjectId>();
        //学科id-等级考学生Map
        Map<ObjectId, Map<String, ObjectId>> subjectStuMap = new HashMap<ObjectId, Map<String, ObjectId>>();

        for (SubjectConfEntry entry : subjectConfEntryList) {
            ObjectId subjectId = entry.getSubjectId();
            SubjectView subjectView = subjectViewMap.get(subjectId.toString());
            String subjectName = subjectView.getName();
            subjectMap.put(subjectName, subjectId);

            List<ObjectId> stuIdList = new ArrayList<ObjectId>();
            for (IdValuePair ivp : entry.getAdvUsers()) {
                stuIdList.add(ivp.getId());
            }
            Map<String, ObjectId> stuMap = new HashMap<String, ObjectId>();
            List<UserDetailInfoDTO> userInfoList = userService.findUserInfoByIds(stuIdList);
            for (UserDetailInfoDTO userDetailInfoDTO : userInfoList) {
                stuMap.put(userDetailInfoDTO.getUserName(), new ObjectId(userDetailInfoDTO.getId()));
            }
            subjectStuMap.put(entry.getSubjectId(), stuMap);
        }

        //模板
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);

        try {
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                HSSFSheet sheet = workbook.getSheetAt(i);
                ObjectId subjectId = subjectMap.get(sheet.getSheetName());
                if (subjectId == null) {
                    throw new Exception("学科名称错误");
                }

                Map<String, ObjectId> studentMap = subjectStuMap.get(subjectId);
                List<IdValuePair> stuScoreList = new ArrayList<IdValuePair>();

                for (int j = 1; j <= sheet.getLastRowNum(); j++) {
                    HSSFRow row = sheet.getRow(j);
                    String stuName = row.getCell(0).getStringCellValue();
                    int score = (int) row.getCell(1).getNumericCellValue();
                    ObjectId studentId = studentMap.get(stuName);
                    if (studentId == null) {
                        throw new Exception(sheet.getSheetName() + "第" + j + "行学生" + stuName + "未找到");
                    }
                    stuScoreList.add(new IdValuePair(studentId, score));
                }

                ScoreEntry scoreEntry = scoreDao.findScore(term, new ObjectId(gradeId), subjectId);
                if (scoreEntry != null) {
                    scoreEntry.setScoreList(stuScoreList);
                } else {
                    scoreEntry = new ScoreEntry(term, new ObjectId(gradeId), subjectId, stuScoreList);
                }
                scoreDao.addOrUpdateScore(scoreEntry);
            }
        } catch (Exception e) {
            throw new Exception("请勿修改模板并填写正确的数据！");
        }
    }


    /**
     * 下载走班课模板
     *
     * @param mode 走班模式 1 ： 3个逻辑位置模式， 2 ： 虚拟班模式
     * @return
     */
    public byte[] downloadZBCourseTemplate(int mode) {
        //生成Excel
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成sheet
        HSSFSheet sheet = wb.createSheet("分班结果");

        for (int i = 0; i < 7; i++) {//自动列宽
            sheet.setColumnWidth(i, 4000);
        }

        //Excel样式
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中
        //设置表头
        HSSFRow row0 = sheet.createRow(0);
        HSSFCell cell0 = row0.createCell(0);
        cell0.setCellValue("教学班名称");
        cell0.setCellStyle(cellStyle);
        HSSFCell cell1 = row0.createCell(1);
        cell1.setCellValue("学科");
        cell1.setCellStyle(cellStyle);
        HSSFCell cell2 = row0.createCell(2);
        cell2.setCellValue("老师");
        cell2.setCellStyle(cellStyle);
        HSSFCell cell3 = row0.createCell(3);
        cell3.setCellValue("教室");
        cell3.setCellStyle(cellStyle);
        HSSFCell cell4 = row0.createCell(4);
        cell4.setCellValue("课时");
        cell4.setCellStyle(cellStyle);
        HSSFCell cell5 = row0.createCell(5);
        cell5.setCellValue("等级/合格(1,2)");
        cell5.setCellStyle(cellStyle);
        if (mode == 1) {// 虚拟走班模式不需要设置逻辑位置
            HSSFCell cell6 = row0.createCell(6);
            cell6.setCellValue("时间（逻辑位置）");
            cell6.setCellStyle(cellStyle);
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return os.toByteArray();
    }

    /**
     * 导入走班分班结果
     *
     * @param inputStream
     * @param term
     * @param gradeId
     * @param schoolId
     * @param mode        走班模式 1 ： 3个逻辑位置模式， 2 ： 虚拟班模式
     * @throws IOException
     */
    public void importZBCourse(InputStream inputStream, String term, String gradeId, ObjectId schoolId, int mode) throws Exception {
        //设置进度
        zoubanStateService.setZoubanState(term, gradeId, 3);
        zoubanStateService.setZoubanSubState(term, gradeId, 3);
        //先清空本年级课表中的走班课
        paikeService.clearTimetableCourse(term, gradeId, 1, null);
        //删除已有的走班课
        zouBanCourseDao.removeCourseByType(term, new ObjectId(gradeId), ZoubanType.ZOUBAN.getType());

        XuankeConfEntry xuankeConfEntry = xuanKeConfDao.findXuanKeConf(term, new ObjectId(gradeId));
        List<ClassFengDuanEntry> classFengDuanEntryList = fenDuanDao.getClassFenduanList(xuankeConfEntry.getID());
        ObjectId grpId = classFengDuanEntryList.get(0).getID(); //分段id
        ObjectId group1 = new ObjectId();//逻辑位置1
        ObjectId group2 = new ObjectId();//逻辑位置2
        ObjectId group3 = new ObjectId();//逻辑位置3

        final int type = ZoubanType.ZOUBAN.getType();


        //学科配置
        List<SubjectConfEntry> subjectConfEntryList = subjectConfDao.findSubjectConf(xuankeConfEntry.getID(), type);
        //学科信息
        Map<String, SubjectView> subjectViewMap = schoolService.findSubjectViewMapBySchoolIdAndGradeId(schoolId.toString(), gradeId);

        //学科名称-id Map
        Map<String, ObjectId> subjectMap = new HashMap<String, ObjectId>();
        //学科-老师 Map
        Map<ObjectId, List<IdNameDTO>> subjectTeacherMap = new HashMap<ObjectId, List<IdNameDTO>>();

        for (SubjectConfEntry entry : subjectConfEntryList) {
            ObjectId subjectId = entry.getSubjectId();
            SubjectView subjectView = subjectViewMap.get(subjectId.toString());
            String subjectName = subjectView.getName();

            subjectMap.put(subjectName, subjectId);
            subjectTeacherMap.put(subjectId, commonService.findTeacherBySubject(gradeId, subjectId.toString()));
        }

        //本校教室
        List<ClassRoomDTO> classRoomDTOList = classroomService.findClassroomList(schoolId);

        //教室名称-id Map
        Map<String, ObjectId> classroomMap = new HashMap<String, ObjectId>();

        for (ClassRoomDTO classRoomDTO : classRoomDTOList) {
            classroomMap.put(classRoomDTO.getRoomName(), new ObjectId(classRoomDTO.getId()));
        }


        //模板
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheetAt(0);

        List<ZouBanCourseEntry> zouBanCourseEntryList = new ArrayList<ZouBanCourseEntry>();
        HSSFRow firstRow = sheet.getRow(0);

        if (firstRow.getCell(0).getStringCellValue().equals("教学班名称") &&
                firstRow.getCell(1).getStringCellValue().equals("学科") &&
                firstRow.getCell(2).getStringCellValue().equals("老师") &&
                firstRow.getCell(3).getStringCellValue().equals("教室") &&
                firstRow.getCell(4).getStringCellValue().equals("课时") &&
                firstRow.getCell(5).getStringCellValue().equals("等级/合格(1,2)")) {
            if (mode == 1) {
                if (!firstRow.getCell(6).getStringCellValue().equals("时间（逻辑位置）")) {
                    throw new Exception("数据格式有误，请勿修改表头！");
                }
            }
            try {
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    HSSFRow row = sheet.getRow(i);
                    String className = row.getCell(0).getStringCellValue().trim();
                    String subjectName = row.getCell(1).getStringCellValue().trim();
                    String teacherName = row.getCell(2).getStringCellValue().trim();
                    String classroomName = row.getCell(3).getStringCellValue().trim();
                    int lessonCount = (int) row.getCell(4).getNumericCellValue();
                    int level = (int) row.getCell(5).getNumericCellValue();
                    int groupNum = mode == 1 ? (int) row.getCell(6).getNumericCellValue() : -1;//虚拟走班模式不需要,设为-1

                    ObjectId subjectId = subjectMap.get(subjectName);
                    if (subjectId == null) {
                        throw new Exception("第" + (i + 1) + "行学科名有误");
                    }

                    ObjectId teacherId = null;
                    for (IdNameDTO teacher : subjectTeacherMap.get(subjectId)) {
                        if (teacher.getName().equals(teacherName)) {
                            teacherId = new ObjectId(teacher.getId());
                            break;
                        }
                    }
                    if (teacherId == null) {
                        throw new Exception("第" + (i + 1) + "行老师不存在");
                    }


                    ObjectId classroomId = classroomMap.get(classroomName);
                    if (classroomId == null) {
                        throw new Exception("第" + (i + 1) + "行教室不存在");
                    }

                    ObjectId group = null;
                    if (mode == 1) {
                        switch (groupNum) {
                            case 1:
                                group = group1;
                                break;
                            case 2:
                                group = group2;
                                break;
                            case 3:
                                group = group3;
                                break;
                            default:
                                throw new Exception("第" + (i + 1) + "行逻辑位置不存在");
                        }
                    }

                    ZouBanCourseEntry zouBanCourseEntry = new ZouBanCourseEntry(schoolId, term, new ObjectId(gradeId), grpId, subjectId,
                            className, teacherId, teacherName, classroomId, lessonCount, group, level);
                    zouBanCourseEntryList.add(zouBanCourseEntry);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("导入走班分班结果失败: " + e.getMessage());
                throw new Exception("导入失败！\n" + e.getMessage());
            }

            for (ZouBanCourseEntry entry : zouBanCourseEntryList) {//插入数据库
                zouBanCourseDao.addZouBanCourseEntry(entry);
            }

            //设置进度
            zoubanStateService.setZoubanState(term, gradeId, 4);
            zoubanStateService.setZoubanSubState(term, gradeId, 5);
        } else {
            throw new Exception("数据格式有误，请勿修改表头！");
        }
    }


    /**
     * 自动分班(走班课)
     *
     * @param term
     * @param schoolId
     * @param gradeId
     */
    public boolean autoFenBan(String term, String schoolId, String gradeId, String groupId, int advMax, int advMin, int simMax, int simMin, int classroomCount) {
        //设置进度
        zoubanStateService.setZoubanState(term, gradeId, 3);
        //先清空本年级课表走班课
        paikeService.clearTimetableCourse(term, gradeId, 1, null);
        //删除已有的走班课
        ObjectId grpId = groupId == "" ? null : new ObjectId(groupId);
        zouBanCourseDao.removeCourseByGroupId(term, new ObjectId(gradeId), grpId);
        //走班课分班
        List<ZouBanCourseEntry> result = czFenbanService.fenban2(term, schoolId, gradeId, grpId, advMax, advMin, simMax, simMin, classroomCount);
        if (result.size() > 0) {
            if (checkFenBanFinish(term, gradeId)) {
                zoubanStateService.setZoubanSubState(term, gradeId, 5);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查是否完成分班
     *
     * @param term
     * @param gradeId
     * @return
     */
    private boolean checkFenBanFinish(String term, String gradeId) {
        //检查每个段是否有教学班，如果没有则分班未完成

        XuankeConfEntry xuankeConfEntry = xuanKeConfDao.findXuanKeConf(term, new ObjectId(gradeId));
        List<ClassFengDuanEntry> fengDuanEntryList = fenDuanDao.getClassFenduanList(xuankeConfEntry.getID());
        for (ClassFengDuanEntry fengDuanEntry : fengDuanEntryList) {
            List<ZouBanCourseEntry> zouBanCourseEntryList = zouBanCourseDao.findCourseList(term, new ObjectId(gradeId), fengDuanEntry.getID());
            if (zouBanCourseEntryList.size() == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取走班课列表
     *
     * @param term
     * @param gradeId
     * @param groupId
     * @param schoolId
     * @return
     */
    public List<SubjectCourseDTO> getCourseList(String term, String gradeId, String groupId, String schoolId) {
        XuankeConfEntry xuankeConfEntry = xuanKeConfDao.findXuanKeConf(term, new ObjectId(gradeId));
        //学科配置
        List<SubjectConfEntry> subjectConfEntryList = subjectConfDao.findSubjectConf(xuankeConfEntry.getID(), 1);
        //学科信息
        Map<String, SubjectView> subjectViewMap = schoolService.findSubjectViewMapBySchoolIdAndGradeId(schoolId, gradeId);

        //学科id-名称Map
        Map<ObjectId, String> subjectMap = new HashMap<ObjectId, String>();
        for (SubjectConfEntry entry : subjectConfEntryList) {
            ObjectId subjectId = entry.getSubjectId();
            SubjectView subjectView = subjectViewMap.get(subjectId.toString());
            String subjectName = subjectView.getName();

            subjectMap.put(subjectId, subjectName);
        }

        ObjectId grpId = groupId.equals("") ? null : new ObjectId(groupId);
        List<ZouBanCourseEntry> zouBanCourseEntryList = zouBanCourseDao.findCourseList(term, new ObjectId(gradeId), grpId);

        //学科-教学班Map
        Map<ObjectId, SubjectCourseDTO> subjectCourseMap = new HashMap<ObjectId, SubjectCourseDTO>();
        for (ZouBanCourseEntry entry : zouBanCourseEntryList) {
            SubjectCourseDTO subjectCourseDTO = subjectCourseMap.get(entry.getSubjectId());
            if (subjectCourseDTO == null) {
                subjectCourseDTO = new SubjectCourseDTO();
                subjectCourseDTO.setSubjectName(subjectMap.get(entry.getSubjectId()));
            }
            if (entry.getLevel() == 1) {
                subjectCourseDTO.setAdvCourseList(entry.getClassName());
            } else {
                subjectCourseDTO.setSimCourseList(entry.getClassName());
            }
            subjectCourseMap.put(entry.getSubjectId(), subjectCourseDTO);
        }

        List<SubjectCourseDTO> courseDTOList = new ArrayList<SubjectCourseDTO>(subjectCourseMap.values());

        return courseDTOList;
    }


    /**
     * 学生调班-获取走班课列表
     *
     * @param term
     * @param gradeId
     * @param groupId
     * @param level
     * @return
     */
    public List<Map<String, Object>> getCourseList(String term, String gradeId, String groupId, int level, int mode) throws Exception {
        List<Map<String, Object>> zouBanCourseDTOs = new ArrayList<Map<String, Object>>();

        ObjectId grpId = groupId.equals("") ? null : new ObjectId(groupId);
        //走班课列表
        List<ZouBanCourseEntry> zouBanCourseEntryList = zouBanCourseDao.findCourseList(term, new ObjectId(gradeId), grpId, level);

        if (zouBanCourseEntryList.size() == 0) {
            return zouBanCourseDTOs;
        }

        //教室
        List<ObjectId> classroomIdList = new ArrayList<ObjectId>();
        for (ZouBanCourseEntry entry : zouBanCourseEntryList) {
            classroomIdList.add(entry.getClassRoomId());
        }
        Map<ObjectId, ClassroomEntry> classroomEntryMap = classroomService.findClassRoomEntryMap(classroomIdList);

        /*if (classroomEntryMap.isEmpty()) {
            throw new Exception("请设置教室");
        }*/

        //逻辑位置-走班课Map
        Map<ObjectId, List<ZouBanCourseDTO>> map = new HashMap<ObjectId, List<ZouBanCourseDTO>>();
        for (ZouBanCourseEntry entry : zouBanCourseEntryList) {
            List<ZouBanCourseDTO> dtoList = new ArrayList<ZouBanCourseDTO>();
            if (map.get(entry.getGroup()) != null) {
                dtoList.addAll(map.get(entry.getGroup()));
            }
            ZouBanCourseDTO dto = new ZouBanCourseDTO();
            dto.setZbCourseId(entry.getID().toString());
            dto.setCourseName(entry.getClassName());
            dto.setStudentsCount(entry.getStudentList().size());
            dto.setTeacherName(entry.getTeacherName() == null ? "" : entry.getTeacherName());
            String classroomName = "";
            if (entry.getClassRoomId() != null) {
                classroomName = classroomEntryMap.get(entry.getClassRoomId()).getRoomName();
            }
            dto.setClassRoom(classroomName);
            if (mode == 1) {
                dto.setGroupId(entry.getGroup().toString());
            }
            dtoList.add(dto);

            map.put(entry.getGroup(), dtoList);
        }


        for (Map.Entry entry : map.entrySet()) {
            List<ZouBanCourseDTO> dtoList = (List<ZouBanCourseDTO>) entry.getValue();
            Map<String, Object> so = new HashMap<String, Object>();
            so.put("count", dtoList.size());
            so.put("courseList", dtoList);
            zouBanCourseDTOs.add(so);
        }

        return zouBanCourseDTOs;
    }


    /**
     * 检查课程名称是否重复
     *
     * @param term
     * @param gradeId
     * @param courseName
     * @return
     */
    public boolean isRepeat(String term, String gradeId, String courseName) {
        List<ZouBanCourseEntry> zouBanCourseEntryList = zouBanCourseDao.findCourseList(term, new ObjectId(gradeId), courseName);
        return zouBanCourseEntryList.size() > 0;
    }

    /**
     * 走班重命名
     *
     * @param courseId
     * @param courseName
     */
    public void updateCourseName(String courseId, String courseName) {
        zouBanCourseDao.updateName(new ObjectId(courseId), courseName);
    }


    /**
     * 获取班级学生列表
     *
     * @param courseId
     */
    public Map<String, Object> getCourseStuList(String courseId, ObjectId schoolId) {
        Map<String, Object> result = new HashMap<String, Object>();

        ZouBanCourseEntry zouBanCourseEntry = zouBanCourseDao.getCourseInfoById(new ObjectId(courseId));

        //可调整班级列表
        List<ZouBanCourseEntry> availableCourseList = zouBanCourseDao.findAvailableCourse(zouBanCourseEntry.getSubjectId(), zouBanCourseEntry.getGroup(), zouBanCourseEntry.getID());
        if (availableCourseList.size() > 0) {
            result.put("change", 1);
            List<IdNameDTO> avaList = new ArrayList<IdNameDTO>();
            for (ZouBanCourseEntry entry : availableCourseList) {
                avaList.add(new IdNameDTO(entry.getID().toString(), entry.getClassName()));
            }
            result.put("availableCourseList", avaList);
        } else {
            result.put("change", 0);
            result.put("availableCourseList", new ArrayList<IdNameDTO>());
        }

        List<ObjectId> stuIdList = zouBanCourseEntry.getStudentList();
        Map<ObjectId, ClassInfoDTO> userClassMap = classService.getStudentClassInfo(schoolId, stuIdList);
        Map<ObjectId, UserEntry> userMap = userService.getUserEntryMap(stuIdList, new BasicDBObject("nm", 1).append("sex", 1));

        List<ZBStudentDTO> studentList = new ArrayList<ZBStudentDTO>();
        for (ObjectId userId : stuIdList) {
            ZBStudentDTO zbStudentDTO = new ZBStudentDTO();
            zbStudentDTO.setUserId(userId.toString());
            if (userMap.containsKey(userId)) {
                UserEntry userEntry = userMap.get(userId);
                zbStudentDTO.setUserName(userEntry.getUserName());
                zbStudentDTO.setStudyNum(userEntry.getStudyNum());
                zbStudentDTO.setSex(userEntry.getSex() == 1 ? "男" : "女");
                zbStudentDTO.setClassName(userClassMap.get(userId).getClassName());
                zbStudentDTO.setStudentNum(userEntry.getStudyNum());
                studentList.add(zbStudentDTO);
            }
        }
        sortByClass(studentList);

        result.put("studentList", studentList);
        result.put("count", studentList.size());

        return result;
    }

    /**
     * 对学生列表按班级排序
     *
     * @param studentDTOList
     */
    private void sortByClass(List<ZBStudentDTO> studentDTOList) {
        Collections.sort(studentDTOList, new Comparator<ZBStudentDTO>() {
            @Override
            public int compare(ZBStudentDTO o1, ZBStudentDTO o2) {
                String className1 = o1.getClassName();
                String className2 = o2.getClassName();
                String n1 = "";
                String n2 = "";

                try {

                    if (className1.contains("(")) {
                        n1 = className1.substring(className1.indexOf("(") + 1, className1.indexOf(")"));
                    } else {
                        n1 = className1.substring(className1.indexOf("（") + 1, className1.indexOf("）"));
                    }

                    if (className2.contains("(")) {
                        n2 = className2.substring(className2.indexOf("(") + 1, className2.indexOf(")"));
                    } else {
                        n2 = className2.substring(className2.indexOf("（") + 1, className2.indexOf("）"));
                    }

                    return Integer.parseInt(n1) - Integer.parseInt(n2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }


    /**
     * 学生调班
     *
     * @param studentId
     * @param oldClassId
     * @param newClassId
     */
    public void changeStuClass(String studentId, String oldClassId, String newClassId) {
        zouBanCourseDao.removeStudent(new ObjectId(oldClassId), new ObjectId(studentId));
        zouBanCourseDao.addStudent(new ObjectId(newClassId), new ObjectId(studentId));
    }

    /**
     * 学生添加进教学班
     *
     * @param oddEvenId
     */
    public void addTeachStudent(String oddEvenId, String studentId) {
        zouBanCourseDao.addStudent(new ObjectId(oddEvenId), new ObjectId(studentId));
    }

    /**
     * 从教学班里删除学生
     *
     * @param oddEvenId
     */
    public void removeTeachStudent(String oddEvenId, String studentId) {
        zouBanCourseDao.removeStudent(new ObjectId(oddEvenId), new ObjectId(studentId));
    }


    /**
     * 获取学科组合列表
     *
     * @param schoolId
     * @param term
     * @param gradeId
     * @return
     */
    public List<IdNameDTO> getSubjectGroupList(ObjectId schoolId, String term, ObjectId gradeId, ObjectId courseId) {
        List<IdNameDTO> subjectGroupList = new ArrayList<IdNameDTO>();
        SchoolSubjectGroupEntry schoolSubjectGroupEntry = schoolSubjectGroupDao.getEntry(schoolId, term, gradeId);
        ZouBanCourseEntry zouBanCourseEntry = zouBanCourseDao.getCourseInfoById(courseId);
        List<SchoolSubjectGroupEntry.SubjectGroup> subjectGroups = schoolSubjectGroupEntry.getSubjectGroups();
        for (SchoolSubjectGroupEntry.SubjectGroup subjectGroup : subjectGroups) {
            if (zouBanCourseEntry.getLevel() == 1) {
                if (subjectGroup.getAdvSubjects().contains(zouBanCourseEntry.getSubjectId())) {
                    subjectGroupList.add(new IdNameDTO(subjectGroup.getId().toString(), subjectGroup.getName()));
                }
            } else {
                if (subjectGroup.getSimSubjects().contains(zouBanCourseEntry.getSubjectId())) {
                    subjectGroupList.add(new IdNameDTO(subjectGroup.getId().toString(), subjectGroup.getName()));
                }
            }
        }

        return subjectGroupList;
    }


    /**
     * 获取组合学生列表
     *
     * @param subjectGroupId
     * @param schoolId
     * @param term
     * @param gradeId
     * @param courseId
     * @return
     */
    public List<IdNameDTO> getSubjectGroupStuList(ObjectId subjectGroupId, ObjectId schoolId, String term, ObjectId gradeId, ObjectId courseId, int mode) {
        //第一步：取得组合所有学生；
        //第二步：剔除已分配到本教学班所在逻辑位置的学生；
        //第三步：剔除已分配到其他相同学科教学班的学生；

        XuankeConfEntry xuankeConfEntry = xuanKeConfDao.findXuanKeConf(term, gradeId);

        List<StudentChooseEntry> studentChooseEntries = new ArrayList<StudentChooseEntry>();
        SchoolSubjectGroupEntry schoolSubjectGroupEntry = schoolSubjectGroupDao.getEntry(schoolId, term, gradeId);
        List<SchoolSubjectGroupEntry.SubjectGroup> subjectGroups = schoolSubjectGroupEntry.getSubjectGroups();
        for (SchoolSubjectGroupEntry.SubjectGroup subjectGroup : subjectGroups) {
            if (subjectGroup.getId().equals(subjectGroupId)) {
                studentChooseEntries = studentXuankeDao.getStudentChoosesBySubjectGroup(subjectGroup.getAdvSubjects(), xuankeConfEntry.getID());
                break;
            }
        }

        //已分配学生
        Set<ObjectId> stuList = new HashSet<ObjectId>();
        ZouBanCourseEntry zouBanCourseEntry = zouBanCourseDao.getCourseInfoById(courseId);
        List<ZouBanCourseEntry> zouBanCourseEntryList = new ArrayList<ZouBanCourseEntry>();
        if (mode == 1) {
            //同一逻辑位置教学班
            zouBanCourseEntryList.addAll(zouBanCourseDao.findCourseListByGroup(term, gradeId, zouBanCourseEntry.getGroup(), ZoubanType.ZOUBAN.getType()));
            for (ZouBanCourseEntry entry : zouBanCourseEntryList) {
                stuList.addAll(entry.getStudentList());
            }
        } else {
            stuList.addAll(zouBanCourseEntry.getStudentList());
        }


        //同一学科教学班
        List<ZouBanCourseEntry> subjectCourseList = zouBanCourseDao.findCourseListBySubjectId(term, gradeId,
                zouBanCourseEntry.getGroup(), zouBanCourseEntry.getSubjectId(), ZoubanType.ZOUBAN.getType());
        for (ZouBanCourseEntry entry : subjectCourseList) {
            stuList.addAll(entry.getStudentList());
        }

        List<ObjectId> subjectGroupStuIds = new ArrayList<ObjectId>();
        for (StudentChooseEntry studentChooseEntry : studentChooseEntries) {
            if (!stuList.contains(studentChooseEntry.getUserId())) {//去除本教学班的学生
                subjectGroupStuIds.add(studentChooseEntry.getUserId());
            }
        }

        return userService.findUserIdNameByIds(subjectGroupStuIds);
    }

    /**
     * 获取教学班学生列表
     *
     * @param courseId
     * @return
     */
    public List<IdNameDTO> getZBCourseStuList(ObjectId courseId) {
        ZouBanCourseEntry zouBanCourseEntry = zouBanCourseDao.getCourseInfoById(courseId);
        return userService.findUserIdNameByIds(zouBanCourseEntry.getStudentList());
    }


    /**
     * 更新教学班学生
     *
     * @param courseId
     * @param studentId
     * @param type      0: 添加, 1:删除
     */
    public void updateZBCourseStu(ObjectId courseId, ObjectId studentId, int type) {
        if (type == 0) {
            zouBanCourseDao.addStudent(courseId, studentId);
        } else {
            zouBanCourseDao.removeStudent(courseId, studentId);
        }
        updateZBCourseClassId(courseId);
    }

    /**
     * 更新教学班关联的行政班
     *
     * @param courseId
     */
    public void updateZBCourseClassId(ObjectId courseId) {
        ZouBanCourseEntry zouBanCourseEntry = zouBanCourseDao.getCourseInfoById(courseId);
        XuankeConfEntry xuankeConfEntry = xuanKeConfDao.findXuanKeConf(zouBanCourseEntry.getTerm(), zouBanCourseEntry.getGradeId());
        List<StudentChooseEntry> studentChooseEntrieList = studentXuankeDao.getStudentChooses(zouBanCourseEntry.getStudentList(), xuankeConfEntry.getID());
        Set<ObjectId> classIdSet = new HashSet<ObjectId>();
        for (StudentChooseEntry studentChooseEntry : studentChooseEntrieList) {
            classIdSet.add(studentChooseEntry.getClassId());
        }
        zouBanCourseDao.updateClassIds(courseId, new ArrayList<ObjectId>(classIdSet));
    }


    /**
     * 获取一个逻辑位置的老师和教室
     *
     * @param term
     * @param gradeId
     * @param group
     * @return
     */
    public List<TeacherClassroomDTO> getTeacherClassroomList(String term, String gradeId, String group, String schoolId) {
        List<ZouBanCourseEntry> zouBanCourseEntryList = zouBanCourseDao.findCourseList(new ObjectId(group));
        String groupId = zouBanCourseEntryList.get(0).getGroupId().toString();
        List<ClassRoomDTO> classRoomList = getAvailableClassroom(term, gradeId, groupId, schoolId);
        List<IdNameDTO> classroomList = new ArrayList<IdNameDTO>();
        for (ClassRoomDTO classRoomDTO : classRoomList) {
            classroomList.add(new IdNameDTO(classRoomDTO.getId().toString(), classRoomDTO.getRoomName()));
        }

        List<TeacherClassroomDTO> teacherClassroomDTOList = new ArrayList<TeacherClassroomDTO>();
        for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntryList) {
            TeacherClassroomDTO dto = new TeacherClassroomDTO();

            dto.setCourseId(zouBanCourseEntry.getID().toString());
            dto.setCourseName(zouBanCourseEntry.getClassName());
            dto.setStudentCount(zouBanCourseEntry.getStudentList().size());
            dto.setTeacherId(zouBanCourseEntry.getTeacherId() == null ? "" : zouBanCourseEntry.getTeacherId().toString());

            List<IdNameDTO> teacherList = commonService.findTeacherBySubject(gradeId, zouBanCourseEntry.getSubjectId().toString());
            dto.setTeacherList(teacherList);

            dto.setClassroomId(zouBanCourseEntry.getClassRoomId() == null ? "" : zouBanCourseEntry.getClassRoomId().toString());
            dto.setClassroomList(classroomList);

            teacherClassroomDTOList.add(dto);
        }

        return teacherClassroomDTOList;
    }

    /**
     * 设置老师和教室
     *
     * @param teacherAndClassroomList
     */
    public void setTeacherAndClassroom(List<TeacherClassroomDTO> teacherAndClassroomList) {
        for (TeacherClassroomDTO dto : teacherAndClassroomList) {
            zouBanCourseDao.updateTeacherAndClassRoom(new ObjectId(dto.getCourseId()), new ObjectId(dto.getTeacherId()), dto.getTeacherName(), new ObjectId(dto.getClassroomId()));
        }
    }


    /**
     * 取得编班列表
     *
     * @param term
     * @param gradeId
     * @param groupId
     * @param subjectId
     */
    public List<ZouBanCourseDTO> findBianBanList(String term, String gradeId, String groupId, String subjectId, String schoolid, int type) {
        ObjectId group = null;
        ObjectId subject = null;
        if (!("-1").equals(groupId) && groupId != null) {
            group = new ObjectId(groupId);
        }
        if (!("-1").equals(subjectId) && subjectId != null) {
            subject = new ObjectId(subjectId);
        }
        List<ZouBanCourseEntry> courseEntryList = zouBanCourseDao.findCourseListBySubjectId(term, new ObjectId(gradeId), group, subject, type);
        List<ZouBanCourseDTO> courseList = new ArrayList<ZouBanCourseDTO>();
        List<ObjectId> groupIds = new ArrayList<ObjectId>();
        List<ObjectId> classRoomIds = new ArrayList<ObjectId>();
        Map<ObjectId, Subject> subjectMap = schoolService.getSubjectEntryMap(schoolid);
        if (courseEntryList != null && courseEntryList.size() != 0) {

            for (ZouBanCourseEntry course : courseEntryList) {
                ZouBanCourseDTO zouBanCourseDTO = new ZouBanCourseDTO();
                if (type == 2 && course.getClassId() != null) {
                    String className = classService.getClassEntryById(course.getClassId().get(0), Constant.FIELDS).getName();
                    zouBanCourseDTO.setClassIdList(course.getClassId());
                    zouBanCourseDTO.setClassName(className);
                }
                zouBanCourseDTO.setCourseName(course.getClassName());
                zouBanCourseDTO.setZbCourseId(course.getID().toString());
                zouBanCourseDTO.setCount(course.getStudentList().size());
                zouBanCourseDTO.setLessonCount(course.getLessonCount());
                zouBanCourseDTO.setSubjectId(course.getSubjectId().toString());
                if (subjectMap.containsKey(course.getSubjectId())) {
                    zouBanCourseDTO.setSubjectName(subjectMap.get(course.getSubjectId()).getName());
                } else {
                    zouBanCourseDTO.setSubjectName("");
                }

                zouBanCourseDTO.setClassIdList(course.getClassId());
                zouBanCourseDTO.setClassNumber(course.getClassNumber());
                zouBanCourseDTO.setCheck(0);
                if (course.getTeacherId() != null) {
                    zouBanCourseDTO.setTeacherId(course.getTeacherId().toString());
                }
                if (course.getClassRoomId() != null) {
                    zouBanCourseDTO.setClassRoomId(course.getClassRoomId().toString());
                }
                zouBanCourseDTO.setTeacherName(course.getTeacherName() == null ? "" : course.getTeacherName());
                if (course.getGroupId() != null) {
                    zouBanCourseDTO.setGroupId(course.getGroupId().toString());
                }

                if (course.getClassRoomId() != null) {
                    zouBanCourseDTO.setClassRoomId(course.getClassRoomId().toString());
                }
                groupIds.add(course.getGroupId());
                classRoomIds.add(course.getClassRoomId());
                courseList.add(zouBanCourseDTO);
            }

            Map<ObjectId, ClassFengDuanEntry> groupMap = fenDuanDao.findGroupEntryMap(groupIds, new BasicDBObject("group", 1));
            for (ZouBanCourseDTO courseDTO : courseList) {
                if (courseDTO.getGroupId() != null) {
                    courseDTO.setGroup(groupMap.get(new ObjectId(courseDTO.getGroupId())).getGroup());
                }
            }
        }
        return courseList;
    }


    /**
     * 更新老师和课时
     *
     * @param courseId
     * @param teacherId
     * @param teacherName
     */
    public void updateCourse(String courseId, String teacherId, String teacherName, int lessonCount) {
        //老师如果没设置，则对后面步骤没有影响，直接更新老师即可；
        //老师如果已经设置，需要判断老师是否变更，如果老师不变，只更新课时，有两种情况：
        //1.课时增加，只更新课时；2.课时减少，更新课时并把已排课程弹出；
        //如果老师变了，更新老师和课时，同时弹出已排课时。

        ZouBanCourseEntry course = zouBanCourseDao.getCourseInfoById(new ObjectId(courseId));

        if (course.getTeacherId() != null) {
            if (!course.getTeacherId().toString().equals(teacherId) ||
                    (course.getTeacherId().toString().equals(teacherId) && lessonCount < course.getLessonCount())) {
                List<ObjectId> courseIds = new ArrayList<ObjectId>();
                courseIds.add(new ObjectId(courseId));
                timeTableDao.removeCourseByIds(course.getTerm(), null, courseIds);
            }
        }

        zouBanCourseDao.updateTeacherAndLessonCount(new ObjectId(courseId), new ObjectId(teacherId), teacherName, lessonCount);
    }


    /**
     * 设置走班进度
     *
     * @param term
     * @param gradeId
     */
    public void setZouBanState(String term, String gradeId) {
        if (checkTeacherSetFinish(term, gradeId) && checkClassroomSetFinish(term, gradeId)) {
            zoubanStateService.setZoubanState(term, gradeId, 4);
        }
    }

    /**
     * 一键设置分组走班课老师
     *
     * @param year
     * @param gradeId
     * @param schoolId
     * @param type
     */
    public void autoSetFZZBTeacher(String year, String gradeId, String schoolId, int type) throws Exception {
        //step1获取本年级的走班课课程
        //step2获取本年级班级id列表
        //step3课程按照学科分类
        //step4根据班级ids和学科id获取老师列表，轮流设置
        List<ZouBanCourseDTO> zouBanCourseDTOList = findBianBanList(year, gradeId, "-1", "-1", schoolId, type);
        List<ClassInfoDTO> classInfoDTOList = classService.findClassByGradeId(gradeId);
        List<ObjectId> classIds = new ArrayList<ObjectId>();
        for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
            classIds.add(new ObjectId(classInfoDTO.getId()));
        }
        Map<String, List<ZouBanCourseDTO>> subjectMap = new HashMap<String, List<ZouBanCourseDTO>>();
        for (ZouBanCourseDTO zouBanCourseDTO : zouBanCourseDTOList) {
            if (subjectMap.containsKey(zouBanCourseDTO.getSubjectId())) {
                List<ZouBanCourseDTO> zouBanCourseDTOs = subjectMap.get(zouBanCourseDTO.getSubjectId());
                zouBanCourseDTOs.add(zouBanCourseDTO);
                subjectMap.put(zouBanCourseDTO.getSubjectId(), zouBanCourseDTOs);
            } else {
                List<ZouBanCourseDTO> zouBanCourseDTOs = new ArrayList<ZouBanCourseDTO>();
                zouBanCourseDTOs.add(zouBanCourseDTO);
                subjectMap.put(zouBanCourseDTO.getSubjectId(), zouBanCourseDTOs);
            }
        }
        List<UserInfoDTO> userInfoDTOList = userService.findTeatherBySchoolId(schoolId);
        for (Map.Entry entry : subjectMap.entrySet()) {
            ObjectId subjectId = new ObjectId((String) entry.getKey());

            List<ObjectId> teacherIds = teacherDao.findTeacherBySubjectIdAndClassIds(subjectId, classIds);
            //去重
            teacherIds = new ArrayList<ObjectId>(new HashSet<ObjectId>(teacherIds));
            List<ZouBanCourseDTO> zouBanCourseDTOs = (List<ZouBanCourseDTO>) entry.getValue();
            List<ObjectId> objectIds = zouBanCourseDTOs.get(0).getClassIdList();
            List<ObjectId> tecs = teacherDao.findTeacherBySubjectIdAndClassIds(subjectId, objectIds);

            if (tecs.size() == 0) {
                throw new Exception("部分学科没有老师");
            }

            tecs = new ArrayList<ObjectId>(new HashSet<ObjectId>(tecs));
            int classNumber = zouBanCourseDTOs.get(0).getClassNumber();
            int index = 0;
            int sIndex = 0;
            teacherIds.removeAll(tecs);
            if (classNumber > tecs.size()) {
                for (ZouBanCourseDTO zouBanCourseDTO : zouBanCourseDTOs) {
                    //获取老师
                    ObjectId teacherId;
                    if (index < tecs.size()) {
                        int teacherIndex = index % (tecs.size());
                        teacherId = tecs.get(teacherIndex);
                    } else {
                        teacherId = teacherIds.get(sIndex);
                        sIndex++;
                    }
                    index++;
                    String teacherName = "";
                    for (UserInfoDTO userInfoDTO : userInfoDTOList) {
                        if (userInfoDTO.getId().equals(teacherId.toString())) {
                            teacherName = userInfoDTO.getName();
                            break;
                        }
                    }
                    zouBanCourseDao.updateTeacher(new ObjectId(zouBanCourseDTO.getZbCourseId()), teacherId, teacherName);
                }
            } else {
                for (ZouBanCourseDTO zouBanCourseDTO : zouBanCourseDTOs) {
                    //获取老师
                    int teacherIndex = index % (tecs.size());
                    index++;
                    ObjectId teacherId = tecs.get(teacherIndex);
                    String teacherName = "";
                    for (UserInfoDTO userInfoDTO : userInfoDTOList) {
                        if (userInfoDTO.getId().equals(teacherId.toString())) {
                            teacherName = userInfoDTO.getName();
                            break;
                        }
                    }
                    zouBanCourseDao.updateTeacher(new ObjectId(zouBanCourseDTO.getZbCourseId()), teacherId, teacherName);
                }
            }

        }
    }

    /**
     * 分组走班教室列表
     *
     * @param year
     * @param gradeId
     * @param schoolId
     * @param type
     */
    public void autoSetFZZBClassRoom(String year, String gradeId, String schoolId, int type) throws Exception {
        List<ZouBanCourseDTO> zouBanCourseDTOList = findBianBanList(year, gradeId, "-1", "-1", schoolId, type);
        List<ClassInfoDTO> classInfoDTOList = classService.findClassByGradeId(gradeId);
        List<ObjectId> classIds = new ArrayList<ObjectId>();
        for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
            classIds.add(new ObjectId(classInfoDTO.getId()));
        }
        Map<String, List<ZouBanCourseDTO>> subjectMap = new HashMap<String, List<ZouBanCourseDTO>>();
        for (ZouBanCourseDTO zouBanCourseDTO : zouBanCourseDTOList) {
            if (subjectMap.containsKey(zouBanCourseDTO.getSubjectId())) {
                List<ZouBanCourseDTO> zouBanCourseDTOs = subjectMap.get(zouBanCourseDTO.getSubjectId());
                zouBanCourseDTOs.add(zouBanCourseDTO);
                subjectMap.put(zouBanCourseDTO.getSubjectId(), zouBanCourseDTOs);
            } else {
                List<ZouBanCourseDTO> zouBanCourseDTOs = new ArrayList<ZouBanCourseDTO>();
                zouBanCourseDTOs.add(zouBanCourseDTO);
                subjectMap.put(zouBanCourseDTO.getSubjectId(), zouBanCourseDTOs);
            }

        }
        for (Map.Entry entry : subjectMap.entrySet()) {
            //去重
            List<ZouBanCourseDTO> zouBanCourseDTOs = (List<ZouBanCourseDTO>) entry.getValue();
            //教室列表
            List<IdNameDTO> classroomList = new ArrayList<IdNameDTO>();
            List<ObjectId> classIdList = zouBanCourseDTOs.get(0).getClassIdList();
            for (ObjectId objectId : classIdList) {
                IdNameDTO idNameDTO = new IdNameDTO();
                ClassroomEntry classRoomEntry = subjectConfService.getClassRoom(objectId);
                idNameDTO.setId(classRoomEntry.getID().toString());
                idNameDTO.setName(classRoomEntry.getRoomName());
                classroomList.add(idNameDTO);
            }

            List<ClassRoomDTO> classRoomDTOList = classroomService.findClassroomList(new ObjectId(schoolId));
            for (ClassRoomDTO classRoomDTO : classRoomDTOList) {
                if (classRoomDTO.getClassId().equals("5404a60cf6f28b7261cfad53")) {
                    IdNameDTO idNameDTO = new IdNameDTO();
                    idNameDTO.setId(classRoomDTO.getId());
                    idNameDTO.setName(classRoomDTO.getRoomName());
                    classroomList.add(idNameDTO);
                }
            }

            if (classroomList.size() == 0) {
                throw new Exception("请先设置教室");
            }

            int index = 0;
            for (ZouBanCourseDTO zouBanCourseDTO : zouBanCourseDTOs) {
                int crIndex = index % (classroomList.size());
                index++;
                String classRoomId = classroomList.get(crIndex).getId();
                zouBanCourseDao.updateClassroom(new ObjectId(zouBanCourseDTO.getZbCourseId()), new ObjectId(classRoomId));
            }
        }
    }

    //格致中学自动设置教室
    public void autoSetClassRoom(String year, String gradeId, String schoolId) {
        //获取本年级走班课
        int type = 1;
        List<ZouBanCourseEntry> courseEntryList = zouBanCourseDao.findCourseList(year, new ObjectId(gradeId), type);
        Map<ObjectId, List<ZouBanCourseEntry>> map1 = new HashMap<ObjectId, List<ZouBanCourseEntry>>();//按照段分组
        for (ZouBanCourseEntry zouBanCourseEntry : courseEntryList) {
            if (map1.containsKey(zouBanCourseEntry.getGroupId())) {
                List<ZouBanCourseEntry> list = map1.get(zouBanCourseEntry.getGroupId());
                list.add(zouBanCourseEntry);
                map1.put(zouBanCourseEntry.getGroupId(), list);
            } else {
                List<ZouBanCourseEntry> list = new ArrayList<ZouBanCourseEntry>();
                list.add(zouBanCourseEntry);
                map1.put(zouBanCourseEntry.getGroupId(), list);
            }
        }
        XuankeConfEntry xuanke = xuanKeConfDao.findXuanKeConf(year, new ObjectId(gradeId));
        //获取全校教室
        List<ClassRoomDTO> classRoomDTOList = classroomService.findClassroomList(new ObjectId(schoolId));
        List<ClassRoomDTO> publicClassRoom = new ArrayList<ClassRoomDTO>();//公共教室
        for (ClassRoomDTO classRoomDTO : classRoomDTOList) {
            if (classRoomDTO.getClassId().equals("5404a60cf6f28b7261cfad53")) {
                publicClassRoom.add(classRoomDTO);
            }
        }
        List<ClassFengDuanEntry> fengduans = fenDuanDao.getClassFenduanList(xuanke.getID());
        for (Map.Entry entry : map1.entrySet()) {
            List<ZouBanCourseEntry> zouBanCourseEntries = (List<ZouBanCourseEntry>) entry.getValue();
            Map<ObjectId, List<ZouBanCourseEntry>> map2 = new HashMap<ObjectId, List<ZouBanCourseEntry>>();//按照组分组
            for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntries) {
                if (map2.containsKey(zouBanCourseEntry.getGroup())) {
                    List<ZouBanCourseEntry> list = map2.get(zouBanCourseEntry.getGroup());
                    list.add(zouBanCourseEntry);
                    map2.put(zouBanCourseEntry.getGroup(), list);
                } else {
                    List<ZouBanCourseEntry> list = new ArrayList<ZouBanCourseEntry>();
                    list.add(zouBanCourseEntry);
                    map2.put(zouBanCourseEntry.getGroup(), list);
                }
            }
            //获取当前段的班级数
            List<ObjectId> classIds = new ArrayList<ObjectId>();
            for (ClassFengDuanEntry classFengDuanEntry : fengduans) {
                if (classFengDuanEntry.getID().equals(zouBanCourseEntries.get(0).getGroupId())) {
                    classIds = classFengDuanEntry.getClassIds();
                }
            }
            //获取本段班级的教室
            List<ClassRoomDTO> ownerClassRoomList = new ArrayList<ClassRoomDTO>();
            for (ClassRoomDTO classRoomDTO : classRoomDTOList) {
                if (classIds.contains(new ObjectId(classRoomDTO.getClassId()))) {
                    ownerClassRoomList.add(classRoomDTO);
                }
            }

            for (Map.Entry entry1 : map2.entrySet()) {
                List<ZouBanCourseEntry> zouBanCourseEntries2 = (List<ZouBanCourseEntry>) entry1.getValue();

                int count = zouBanCourseEntries2.size() - ownerClassRoomList.size();
                if (publicClassRoom.size() < count) {
                    System.out.println("教室不够");
                    return;//教室不够
                }

                Collections.shuffle(publicClassRoom);
                int publicRoomIndex = 0;

                Collections.shuffle(ownerClassRoomList);//打乱待选教室
                int i = 0;
                for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntries2) {
                    if (i < ownerClassRoomList.size()) {
                        zouBanCourseDao.updateClassroom(zouBanCourseEntry.getID(), new ObjectId(ownerClassRoomList.get(i).getId()));
                    } else {
                        if (publicRoomIndex < publicClassRoom.size()) {
                            zouBanCourseDao.updateClassroom(zouBanCourseEntry.getID(), new ObjectId(publicClassRoom.get(publicRoomIndex).getId()));
                            publicRoomIndex++;
                        }
                    }
                    i++;
                }
            }
        }
    }

    //获取本班可以选择的教室列表
    public List<ClassRoomDTO> getAvailableClassroom(String year, String gradeId, String groupId, String schoolId) {
        XuankeConfEntry xuanke = xuanKeConfDao.findXuanKeConf(year, new ObjectId(gradeId));
        List<ClassFengDuanEntry> fengduans = fenDuanDao.getClassFenduanList(xuanke.getID());
        //获取当前段的班级数
        List<ObjectId> classIds = new ArrayList<ObjectId>();

        //获取全校教室
        List<ClassRoomDTO> classRoomDTOList = classroomService.findClassroomList(new ObjectId(schoolId));
        List<ClassRoomDTO> publicClassRoom = new ArrayList<ClassRoomDTO>();//公共教室
        for (ClassRoomDTO classRoomDTO : classRoomDTOList) {
            if (classRoomDTO.getClassId().equals("5404a60cf6f28b7261cfad53")) {
                publicClassRoom.add(classRoomDTO);
            }
        }
        if (!StringUtils.isBlank(groupId) && !"-1".equals(groupId)) {
            for (ClassFengDuanEntry classFengDuanEntry : fengduans) {
                if (classFengDuanEntry.getID().equals(new ObjectId(groupId))) {
                    classIds = classFengDuanEntry.getClassIds();
                }
            }
        } else {
            List<ClassInfoDTO> classInfoDTOList = classService.findClassByGradeId(gradeId);
            for (ClassInfoDTO c : classInfoDTOList) {
                classIds.add(new ObjectId(c.getId()));
            }
        }
        //获取本段班级的教室
        for (ClassRoomDTO classRoomDTO : classRoomDTOList) {
            if (classIds.contains(new ObjectId(classRoomDTO.getClassId()))) {
                publicClassRoom.add(classRoomDTO);
            }
        }
        return publicClassRoom;
    }

    //格致中学走班课自动设置老师
    public void autoSetTeacher(String year, String gradeId, String schoolId, int type) throws Exception {
        //step1获取本年级的走班课课程
        //step2获取本年级班级id列表
        //step3课程按照学科分类
        //step4根据班级ids和学科id获取老师列表，轮流设置

        List<ZouBanCourseDTO> zouBanCourseDTOList = findBianBanList(year, gradeId, "-1", "-1", schoolId, type);
        List<ClassInfoDTO> classInfoDTOList = classService.findClassByGradeId(gradeId);
        List<ObjectId> classIds = new ArrayList<ObjectId>();
        for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
            classIds.add(new ObjectId(classInfoDTO.getId()));
        }
        Map<String, List<ZouBanCourseDTO>> subjectMap = new HashMap<String, List<ZouBanCourseDTO>>();

        for (ZouBanCourseDTO zouBanCourseDTO : zouBanCourseDTOList) {
            if (subjectMap.containsKey(zouBanCourseDTO.getSubjectId())) {
                subjectMap.get(zouBanCourseDTO.getSubjectId()).add(zouBanCourseDTO);
            } else {
                List<ZouBanCourseDTO> zouBanCourseDTOs = new ArrayList<ZouBanCourseDTO>();
                zouBanCourseDTOs.add(zouBanCourseDTO);
                subjectMap.put(zouBanCourseDTO.getSubjectId(), zouBanCourseDTOs);
            }
        }
        List<UserInfoDTO> userInfoDTOList = userService.findTeatherBySchoolId(schoolId);
        Map<ObjectId, String> teacherIdNameMap = new HashMap<ObjectId, String>();

        for (UserInfoDTO u : userInfoDTOList) {
            teacherIdNameMap.put(new ObjectId(u.getId()), u.getName());
        }


        for (Map.Entry entry : subjectMap.entrySet()) {
            ObjectId subjectId = new ObjectId((String) entry.getKey());
            List<ObjectId> teacherIds = teacherDao.findTeacherBySubjectIdAndClassIds(subjectId, classIds);

            if (teacherIds.size() == 0) {
                throw new Exception("部分学科没有老师");
            }

            //去重
            teacherIds = new ArrayList<ObjectId>(new HashSet<ObjectId>(teacherIds));
            List<ZouBanCourseDTO> zouBanCourseDTOs = (List<ZouBanCourseDTO>) entry.getValue();

            for (ZouBanCourseDTO zouBanCourseDTO : zouBanCourseDTOs) {
                Collections.shuffle(teacherIds);
                ObjectId teacherId = teacherIds.get(0);
                String teacherName = "";
                if (teacherIdNameMap.containsKey(teacherId)) {
                    teacherName = teacherIdNameMap.get(teacherId);
                }
                zouBanCourseDao.updateTeacher(new ObjectId(zouBanCourseDTO.getZbCourseId()), teacherId, teacherName);
            }
        }
    }

    //检查老师是否设置完成
    public boolean checkTeacherSetFinish(String year, String gradeId) {
        List<ZouBanCourseEntry> courseEntryList = zouBanCourseDao.findCourseList(year, new ObjectId(gradeId), 1);
        for (ZouBanCourseEntry z : courseEntryList) {
            if (z.getTeacherId() == null)
                return false;
        }
        return true;
    }

    //检查教室是否设置完成
    public boolean checkClassroomSetFinish(String year, String gradeId) {
        List<ZouBanCourseEntry> courseEntryList = zouBanCourseDao.findCourseList(year, new ObjectId(gradeId), 1);
        for (ZouBanCourseEntry z : courseEntryList) {
            if (z.getClassRoomId() == null)
                return false;
        }
        return true;
    }


}
