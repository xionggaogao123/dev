package com.fulaan.zouban.service;

import com.db.school.ClassDao;
import com.db.zouban.*;
import com.fulaan.examresult.controller.IdNameDTO;
import com.fulaan.myschool.controller.SubjectView;
import com.fulaan.school.service.SchoolService;
import com.fulaan.user.service.UserService;
import com.fulaan.zouban.dto.SubjectConfDTO;
import com.fulaan.zouban.dto.XuanKeDTO;
import com.mongodb.BasicDBObject;
import com.pojo.app.*;
import com.pojo.school.ClassEntry;
import com.pojo.school.Subject;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.pojo.zouban.*;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
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
 * Created by qiangm on 2015/10/8.
 */
@Service
public class StudentXuankeService {
    private static final Logger logger = Logger.getLogger(StudentXuankeService.class);
    private StudentXuankeDao studentXuankeDao = new StudentXuankeDao();
    private XuanKeConfDao xuanKeConfDao = new XuanKeConfDao();
    private ClassDao classDao = new ClassDao();
    private SchoolService schoolService = new SchoolService();
    private ZouBanCourseDao zoubanCourseDao = new ZouBanCourseDao();
    private SubjectConfDao subjectConfDao = new SubjectConfDao();
    private SchoolSubjectGroupDao schoolSubjectGroupDao = new SchoolSubjectGroupDao();
    @Autowired
    private UserService userService;
    @Autowired
    private XuanKeService xuanKeService;
    @Autowired
    private ZoubanStateService zoubanStateService;


    /**
     * 学生选课结果-----finish
     *
     * @param userId
     * @param classId
     * @param advance
     * @param simple
     * @return
     */
    public String studentXuanke(ObjectId userId, String userName, ObjectId classId, String advance, String simple, ObjectId xuankeId) {
        String[] advanceList = advance.split(Constant.COMMA);
        List<ObjectId> advanceIdList = new ArrayList<ObjectId>();
        for (String str : advanceList) {
            advanceIdList.add(new ObjectId(str));
        }
        String[] simpleList = simple.split(Constant.COMMA);
        List<ObjectId> simpleIdList = new ArrayList<ObjectId>();
        for (String str : simpleList) {
            simpleIdList.add(new ObjectId(str));
        }
        //先添加到学生选课记录表中
        StudentChooseEntry studentChooseEntry = new StudentChooseEntry();
        studentChooseEntry.setUserName(userName);
        studentChooseEntry.setIsXuan(1);
        studentChooseEntry.setUserId(userId);
        studentChooseEntry.setAdvancelist(advanceIdList);
        studentChooseEntry.setSimplelist(simpleIdList);
        studentChooseEntry.setClassId(classId);
        studentChooseEntry.setXuanKeId(xuankeId);
        studentXuankeDao.updateStudentChoose(studentChooseEntry);
        //删除subjectConfEntry中的原有选课记录，并添加新的记录
        subjectConfDao.removeXuankeHistory(xuankeId, userId, classId, advanceIdList, simpleIdList);
        //分别在科目表中添加学生名单
        advanceIdList = new ArrayList<ObjectId>();
        for (String str : advanceList) {
            advanceIdList.add(new ObjectId(str));
        }
        subjectConfDao.addStudents(advanceIdList, userId, classId, xuankeId, 2);
        subjectConfDao.addStudents(simpleIdList, userId, classId, xuankeId, 1);

        XuankeConfEntry xuankeConfEntry = xuanKeConfDao.findXuanKeConfByXuanKeId(xuankeId);
        String term = xuankeConfEntry.getTerm();
        ObjectId gradeId = xuankeConfEntry.getGradeId();

        //设置进度
        int state = 2;
        //未选课人数
        int count = studentXuankeDao.findStudentChooseByType(xuankeId, 0);
        if (count == 0) {
            state = 3;
        }
        zoubanStateService.setZoubanState(term, gradeId.toString(), state);

        return Constant.SUCCESS_CODE;
    }

    /**
     * 获取学生已经选择的走班科目 ----finish
     *
     * @param userId
     * @param xuankeId
     * @param schoolId
     * @return
     */
    public Map<String, List<IdValuePair>> getChoosedCourse(ObjectId userId, ObjectId xuankeId, String schoolId) {
        Map<String, List<IdValuePair>> map = new HashMap<String, List<IdValuePair>>();
        StudentChooseEntry studentChooseEntry = studentXuankeDao.getStudentChoose(userId, xuankeId);
        List<ObjectId> advList = studentChooseEntry.getAdvancelist();
        List<ObjectId> simList = studentChooseEntry.getSimplelist();
        Map<ObjectId, Subject> subjectMap = schoolService.getSubjectEntryMap(schoolId);
        List<IdValuePair> advPairList = new ArrayList<IdValuePair>();
        for (ObjectId o : advList) {
            IdValuePair idValuePair = new IdValuePair(o, subjectMap.get(o).getName());
            advPairList.add(idValuePair);
        }
        List<IdValuePair> simPairList = new ArrayList<IdValuePair>();
        for (ObjectId o : simList) {
            IdValuePair idValuePair = new IdValuePair(o, subjectMap.get(o).getName());
            simPairList.add(idValuePair);
        }
        map.put("adv", advPairList);
        map.put("sim", simPairList);
        return map;
    }

    /**
     * 获取本年级需要的选班课目---finish
     *
     * @param xuankeId
     * @return
     */
    public List<SubjectConfEntry> getCourseConfList(String xuankeId) {
        return subjectConfDao.findSubjectConf(new ObjectId(xuankeId), ZoubanType.ZOUBAN.getType());
    }


    /**
     * 导入学生选课结果
     *
     * @param inputStream
     * @param term
     * @param gradeId
     * @param schoolId
     * @return
     */
    public Map<String, String> importXuankexcel(InputStream inputStream, String term, String gradeId, String schoolId) {
        Map<String, String> map = new HashMap<String, String>();

        XuankeConfEntry xuankeConfEntry = xuanKeConfDao.findXuanKeConf(term, new ObjectId(gradeId));
        ObjectId xuankeId = xuankeConfEntry.getID();

        if (xuankeId == null) {
            map.put("result", "fail");
            map.put("reason", "选课未开始");
            map.put("line", "");
            return map;
        } else {
            List<ClassEntry> classEntryList = classDao.findClassEntryByGradeId(new ObjectId(gradeId));
            //班级map
            Map<String, ObjectId> classMap = new HashMap<String, ObjectId>();
            List<ObjectId> studentIds = new ArrayList<ObjectId>();

            for (ClassEntry classEntry : classEntryList) {
                classMap.put(classEntry.getName(), classEntry.getID());
                studentIds.addAll(classEntry.getStudents());
            }

            List<UserDetailInfoDTO> userList = userService.findUserInfoByIds(studentIds);
            //学生map
            Map<String, ObjectId> userMap = new HashMap<String, ObjectId>();
            for (UserDetailInfoDTO ud : userList) {
                if (studentIds.contains(new ObjectId(ud.getId())))
                    userMap.put(ud.getUserName(), new ObjectId(ud.getId()));
            }


            List<SubjectView> subjectList = schoolService.findSubjectList(schoolId);
            Map<ObjectId, String> subNameMap = new HashMap<ObjectId, String>();

            for (SubjectView sv : subjectList) {
                subNameMap.put(new ObjectId(sv.getId()), sv.getName());
            }

            //获取本校学科
            List<ObjectId> subjectObjIds = new ArrayList<ObjectId>();
            //走班学科map
            Map<String, String> zbSubjectMap = new HashMap<String, String>();
            //走班学科
            List<SubjectConfEntry> subjectConfEntries = subjectConfDao.findSubjectConf(xuankeId, ZoubanType.ZOUBAN.getType());
            for (SubjectConfEntry sce : subjectConfEntries) {
                if (subNameMap.containsKey(sce.getSubjectId())) {
                    zbSubjectMap.put(subNameMap.get(sce.getSubjectId()), sce.getSubjectId().toString());
                    subjectObjIds.add(sce.getSubjectId());
                }
            }

            //解析excel
            try {
                HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
                HSSFSheet xuankeSheet = workbook.getSheetAt(0);
                int rowNum = xuankeSheet.getLastRowNum();
                //先判断表头是否被修改
                if (!(getStringCellValue(xuankeSheet.getRow(0).getCell(0)).equals("学号") &&
                        getStringCellValue(xuankeSheet.getRow(0).getCell(1)).equals("姓名") &&
                        getStringCellValue(xuankeSheet.getRow(0).getCell(2)).equals("班级") &&
                        getStringCellValue(xuankeSheet.getRow(0).getCell(3)).equals("等级考") &&
                        getStringCellValue(xuankeSheet.getRow(0).getCell(4)).equals("合格考"))) {
                    map.put("result", "fail");
                    map.put("reason", "请勿修改表头");
                    map.put("line", "");
                    return map;
                } else {
                    //验证格式是否正确
                    for (int i = 1; i <= rowNum; i++) {
                        try {
                            String userName = getStringCellValue(xuankeSheet.getRow(i).getCell(1)).trim();
                            if (StringUtils.isBlank(userName)) {
                                break;
                            }
                            String className = getStringCellValue(xuankeSheet.getRow(i).getCell(2));
                            String dengjikao = getStringCellValue(xuankeSheet.getRow(i).getCell(3));
                            String hegekao = getStringCellValue(xuankeSheet.getRow(i).getCell(4));
                            if (dengjikao.length() != 6) {
                                map.put("result", "fail");
                                map.put("reason", "等级考个数应该为3个");
                                map.put("line", Integer.toString(i + 1));
                                return map;
                            }
                            if (hegekao.length() != 6 && hegekao.length() != 4) {
                                map.put("result", "fail");
                                map.put("reason", "合格考科目个数应该为2个或3个");
                                map.put("line", Integer.toString(i + 1));
                                return map;
                            }

                            List<String> dengjiList = new ArrayList<String>();
                            for (int m = 0; m < 3; m++) {
                                dengjiList.add(dengjikao.substring(m * 2, m * 2 + 2));
                            }
                            List<String> hegeList = new ArrayList<String>();
                            for (int m = 0; m < hegekao.length() / 2; m++) {
                                hegeList.add(hegekao.substring(m * 2, m * 2 + 2));
                            }
                            if (dengjiList.size() != 3 || hegeList.size() < 2 || hegeList.size() > 3) {
                                map.put("result", "fail");
                                map.put("reason", "科目不合适");
                                map.put("line", Integer.toString(i + 1));
                                return map;
                            }
                            ObjectId userId = userMap.get(userName);
                            ObjectId classId = classMap.get(className);
                            if (userId == null) {
                                map.put("result", "fail");
                                map.put("reason", userName + "未找到");
                                map.put("line", Integer.toString(i + 1));
                                return map;
                            }
                            if (classId == null) {
                                map.put("result", "fail");
                                map.put("reason", className + "未找到");
                                map.put("line", Integer.toString(i + 1));
                                return map;
                            }
                            if (zbSubjectMap.get(dengjiList.get(0)).equals("") || zbSubjectMap.get(dengjiList.get(1)).equals("") ||
                                    zbSubjectMap.get(dengjiList.get(2)).equals("")) {
                                map.put("result", "fail");
                                map.put("reason", "等级考名字有误");
                                map.put("line", Integer.toString(i + 1));
                                return map;
                            }
                            if (hegeList.size() == 2) {
                                if (zbSubjectMap.get(hegeList.get(0)).equals("") || zbSubjectMap.get(hegeList.get(1)).equals("")) {
                                    map.put("result", "fail");
                                    map.put("reason", "合格考名字有误");
                                    map.put("line", Integer.toString(i + 1));
                                    return map;
                                }
                            } else {
                                if (zbSubjectMap.get(hegeList.get(0)).equals("") || zbSubjectMap.get(hegeList.get(1)).equals("") ||
                                        zbSubjectMap.get(hegeList.get(2)).equals("")) {
                                    map.put("result", "fail");
                                    map.put("reason", "合格考名字有误");
                                    map.put("line", Integer.toString(i + 1));
                                    return map;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            map.put("result", "fail");
                            map.put("reason", "数据有误");
                            map.put("line", Integer.toString(i + 1));
                            return map;
                        }
                    }

                    //删除学生选课结果
                    studentXuankeDao.removeStudentChooseEntry(xuankeId);
                    //更新学生列表
                    xuanKeService.updateStudentList(xuankeId, new ObjectId(gradeId));
                    //清空学科配置中已选课的人
                    subjectConfDao.removeXuankeStudent(xuankeId, subjectObjIds);


                    for (int i = 1; i <= rowNum; i++) {
                        try {
                            List<String> dengjiList = new ArrayList<String>();
                            String userName = getStringCellValue(xuankeSheet.getRow(i).getCell(1)).trim();
                            if (StringUtils.isBlank(userName)) {
                                map.put("result", "success");
                                return map;
                            }

                            String className = getStringCellValue(xuankeSheet.getRow(i).getCell(2));
                            String dengjikao = getStringCellValue(xuankeSheet.getRow(i).getCell(3));
                            String hegekao = getStringCellValue(xuankeSheet.getRow(i).getCell(4));
                            for (int m = 0; m < 3; m++) {
                                dengjiList.add(dengjikao.substring(m * 2, m * 2 + 2));
                            }
                            List<String> hegeList = new ArrayList<String>();
                            for (int m = 0; m < hegekao.length() / 2; m++) {
                                hegeList.add(hegekao.substring(m * 2, m * 2 + 2));
                            }
                            ObjectId userId = userMap.get(userName);

                            ObjectId classId = classMap.get(className);
                            String advance = zbSubjectMap.get(dengjiList.get(0)) + "," + zbSubjectMap.get(dengjiList.get(1)) + "," +
                                    zbSubjectMap.get(dengjiList.get(2));
                            String simple = "";
                            if (hegeList.size() == 2) {
                                simple = zbSubjectMap.get(hegeList.get(0)) + "," + zbSubjectMap.get(hegeList.get(1));
                            } else {
                                simple = zbSubjectMap.get(hegeList.get(0)) + "," + zbSubjectMap.get(hegeList.get(1)) + "," +
                                        zbSubjectMap.get(hegeList.get(2));
                            }

                            studentXuanke(userId, userName, classId, advance, simple, xuankeId);
                        } catch (Exception e) {
                            e.printStackTrace();
                            logger.error("导入选课数据exception:" + e.toString());
                            map.put("result", "fail");
                            map.put("reason", "导入异常");
                            map.put("line", Integer.toString(i + 1));
                            return map;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                map.put("result", "fail");
                map.put("reason", "数据有误");
                map.put("line", "");
                return map;
            }
        }

        //清空走班课
        zoubanCourseDao.removeCourseByType(term, new ObjectId(gradeId), ZoubanType.ZOUBAN.getType());
        //设置进度
        int state = 2;
        //未选课人数
        int count = studentXuankeDao.findStudentChooseByType(xuankeId, 0);
        if (count != 0) {
            map.put("result", "fail");
            map.put("reason", "有部分人未选课");
            map.put("line", "");
            return map;
        } else {
            state = 3;
        }
        zoubanStateService.setZoubanState(term, gradeId, state);
        zoubanStateService.setZoubanSubState(term, gradeId, 3);
        map.put("result", "success");
        return map;
    }

    /**
     * 下载模板
     *
     * @param response
     */
    public void generalExcel(HttpServletResponse response, String gradeId) {

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("选课模板");
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("学号");

        cell = row.createCell(1);
        cell.setCellValue("姓名");

        cell = row.createCell(2);
        cell.setCellValue("班级");
        cell = row.createCell(3);
        cell.setCellValue("等级考");
        cell = row.createCell(4);
        cell.setCellValue("合格考");

        List<ClassEntry> classEntryList = classDao.findClassEntryByGradeId(new ObjectId(gradeId));
        List<ObjectId> studentIds = new ArrayList<ObjectId>();
        for (ClassEntry classEntry : classEntryList) {
            studentIds.addAll(classEntry.getStudents());
        }
        Map<ObjectId, UserEntry> userEntryMap = userService.getUserEntryMap(studentIds, new BasicDBObject());
        int rowLine = 0;
        for (ClassEntry classEntry : classEntryList) {
            String className = classEntry.getName();
            for (ObjectId studentId : classEntry.getStudents()) {
                rowLine++;
                UserEntry userEntry = userEntryMap.get(studentId);
                if (userEntry != null) {
                    String userName = userEntry.getRealUserName();

                    row = sheet.createRow(rowLine);
                    cell = row.createCell(0);

                    if (userEntry.getStudyNum() != null) {
                        cell.setCellValue(String.valueOf(userEntry.getStudyNum()));
                    } else {
                        cell.setCellValue("");
                    }

                    cell = row.createCell(1);
                    cell.setCellValue(userName);
                    cell = row.createCell(2);
                    cell.setCellValue(className);
                    cell = row.createCell(3);
                    cell.setCellValue("");
                    cell = row.createCell(4);
                    cell.setCellValue("");
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
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("选课.xls", "UTF-8"));
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


    public Map<String, Map<String, List<IdValuePair>>> getStudentChooseEntrys(List<ObjectId> stuIds, ObjectId xuankeId, String schoolId) {
        Map<String, Map<String, List<IdValuePair>>> model = new HashMap<String, Map<String, List<IdValuePair>>>();
        Map<ObjectId, Subject> subjectMap = schoolService.getSubjectEntryMap(schoolId);

        List<StudentChooseEntry> studentChooseEntries = studentXuankeDao.getStudentChooses(stuIds, xuankeId);
        for (StudentChooseEntry studentChooseEntry : studentChooseEntries) {
            Map<String, List<IdValuePair>> map = new HashMap<String, List<IdValuePair>>();
            List<ObjectId> advList = studentChooseEntry.getAdvancelist();
            List<ObjectId> simList = studentChooseEntry.getSimplelist();
            List<IdValuePair> advPairList = new ArrayList<IdValuePair>();
            for (ObjectId o : advList) {
                IdValuePair idValuePair = new IdValuePair(o, subjectMap.get(o).getName());
                advPairList.add(idValuePair);
            }
            List<IdValuePair> simPairList = new ArrayList<IdValuePair>();
            for (ObjectId o : simList) {
                IdValuePair idValuePair = new IdValuePair(o, subjectMap.get(o).getName());
                simPairList.add(idValuePair);
            }
            map.put("adv", advPairList);
            map.put("sim", simPairList);
            model.put(studentChooseEntry.getUserId().toString(), map);
        }
        return model;
    }

    /**
     * 组合学生列表
     *
     * @param subjectGroupId
     * @param xuanKeId
     * @param schoolId
     * @param gradeId
     * @param year
     * @return
     */
    public List<NameValuePairDTO> getStudentChoosesBySubjectGroup(ObjectId subjectGroupId, ObjectId xuanKeId, ObjectId schoolId, ObjectId gradeId, String year) {
        //根据组合查询所有学生的选课结果
        List<StudentChooseEntry> studentChooseEntries = new ArrayList<StudentChooseEntry>();
        SchoolSubjectGroupEntry schoolSubjectGroupEntry = schoolSubjectGroupDao.getEntry(schoolId, year, gradeId);
        List<SchoolSubjectGroupEntry.SubjectGroup> subjectGroups = schoolSubjectGroupEntry.getSubjectGroups();
        for (SchoolSubjectGroupEntry.SubjectGroup subjectGroup : subjectGroups) {
            if (subjectGroup.getId().equals(subjectGroupId)) {
                studentChooseEntries.addAll(studentXuankeDao.getStudentChoosesBySubjectGroup(subjectGroup.getAdvSubjects(), xuanKeId));
                break;
            }
        }

        //班级id
        Set<ObjectId> classIds = new HashSet<ObjectId>();
        for (StudentChooseEntry studentChooseEntry : studentChooseEntries) {
            classIds.add(studentChooseEntry.getClassId());
        }
        //班级信息
        Map<ObjectId, ClassEntry> classEntryMap = classDao.getClassEntryMap(classIds, new BasicDBObject("nm", 1));

        //学生-班级map
        Map<ObjectId, String> stuClassMap = new HashMap<ObjectId, String>();

        for (StudentChooseEntry chooseEntry : studentChooseEntries) {
            stuClassMap.put(chooseEntry.getUserId(), classEntryMap.get(chooseEntry.getClassId()).getName());
        }

        List<IdNameDTO> idNameDTOs = userService.findUserIdNameByIds(new ArrayList<ObjectId>(stuClassMap.keySet()));

        List<NameValuePairDTO> result = new ArrayList<NameValuePairDTO>();

        for (IdNameDTO idNameDTO : idNameDTOs) {
            result.add(new NameValuePairDTO(idNameDTO.getName(), stuClassMap.get(new ObjectId(idNameDTO.getId()))));
        }

        return result;
    }

    /**
     * 组合名称及人数
     *
     * @param schoolId
     * @param year
     * @param gradeId
     * @param xuanKeId
     * @return
     * @throws Exception
     */
    public List<IdNameValuePairDTO> getSubjectGroupStudentNumPair(ObjectId schoolId, String year, ObjectId gradeId, ObjectId xuanKeId) throws Exception {
        List<IdNameValuePairDTO> pairDTOs = new ArrayList<IdNameValuePairDTO>();
        SchoolSubjectGroupEntry schoolSubjectGroupEntry = schoolSubjectGroupDao.getEntry(schoolId, year, gradeId);
        if (schoolSubjectGroupEntry != null) {
            List<SchoolSubjectGroupEntry.SubjectGroup> subjectGroups = schoolSubjectGroupEntry.getSubjectGroups();
            for (SchoolSubjectGroupEntry.SubjectGroup subjectGroup : subjectGroups) {
                if (subjectGroup.getIsPublic()) {
                    int count = studentXuankeDao.countStudentChoosesBySubjectGroup(subjectGroup.getAdvSubjects(), xuanKeId);
                    pairDTOs.add(new IdNameValuePairDTO(subjectGroup.getId().toString(), subjectGroup.getName(), count));
                }
            }
            Collections.sort(pairDTOs, new Comparator<IdNameValuePairDTO>() {
                @Override
                public int compare(IdNameValuePairDTO dto1, IdNameValuePairDTO dto2) {
                    int num1 = (Integer) dto1.getValue();
                    int num2 = (Integer) dto2.getValue();
                    return num2 - num1;
                }
            });
        }

        return pairDTOs;
    }

    public void exportResultByClass(ObjectId xuankeId, ObjectId gradeId, String term, ObjectId schoolId, HttpServletResponse response) {

        XuanKeDTO xuanKeDTO = xuanKeService.findXuanKeConf(term, gradeId.toString(), 1, schoolId.toString());
        List<SubjectConfDTO> subjectConfDTOs = xuanKeDTO.getSubConfList();
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("选课结果");
        //为sheet1生成第一行，用于放表头信息
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("学生姓名");
        sheet.setColumnWidth(0, 8000);
        cell = row.createCell(1);
        cell.setCellValue("班级");
        sheet.setColumnWidth(1, 5000);
        int i = 2;
        for (SubjectConfDTO subjectConfDTO : subjectConfDTOs) {
            cell = row.createCell(i++);
            cell.setCellValue(subjectConfDTO.getSubjectName());
            sheet.setColumnWidth(1, 5000);
        }
        int rowNo = 1;
        List<ClassEntry> classEntries = classDao.findClassEntryByGradeId(gradeId);
        for (ClassEntry classEntry : classEntries) {
            List<StudentChooseEntry> studentChooseEntries = studentXuankeDao.studentXuanKeList(xuankeId, classEntry.getID());
            for (StudentChooseEntry studentChooseEntry : studentChooseEntries) {
                row = sheet.createRow(rowNo);

                cell = row.createCell(0);
                cell.setCellValue(studentChooseEntry.getUserName());
                cell = row.createCell(1);
                cell.setCellValue(classEntry.getName());
                int j = 2;
                for (SubjectConfDTO subjectConfDTO : subjectConfDTOs) {
                    cell = row.createCell(j++);
                    List<ObjectId> advs = studentChooseEntry.getAdvancelist();
                    String value = advs.contains(new ObjectId(subjectConfDTO.getSubjectId())) ? "1" : "";
                    cell.setCellValue(value);
                }
                rowNo++;
            }


        }

        outPutWorkBook(wb, response, "按班级-");

    }

    public void exportResultByGroup(ObjectId xuankeId, ObjectId gradeId, String term, ObjectId schoolId, HttpServletResponse response) throws Exception {

        XuanKeDTO xuanKeDTO = xuanKeService.findXuanKeConf(term, gradeId.toString(), 1, schoolId.toString());
        List<SubjectConfDTO> subjectConfDTOs = xuanKeDTO.getSubConfList();
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("选课结果");
        //为sheet1生成第一行，用于放表头信息
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("学生姓名");
        sheet.setColumnWidth(0, 8000);
        cell = row.createCell(1);
        cell.setCellValue("班级");
        sheet.setColumnWidth(1, 5000);
        int i = 2;
        for (SubjectConfDTO subjectConfDTO : subjectConfDTOs) {
            cell = row.createCell(i++);
            cell.setCellValue(subjectConfDTO.getSubjectName());
            sheet.setColumnWidth(1, 5000);
        }
        int rowNo = 1;
        List<ClassEntry> classEntries = classDao.findClassEntryByGradeId(gradeId);
        Map<ObjectId, String> classEntryMap = new HashMap<ObjectId, String>();
        for (ClassEntry classEntry : classEntries) {
            classEntryMap.put(classEntry.getID(), classEntry.getName());
        }
        List<IdValuePairDTO> idValuePairDTOs = getSubjectGroupStudentPair(schoolId, term, gradeId, xuankeId);
        for (IdValuePairDTO idValuePairDTO : idValuePairDTOs) {
            List<StudentChooseEntry> studentChooseEntries = (List<StudentChooseEntry>) idValuePairDTO.getValue();
            for (StudentChooseEntry studentChooseEntry : studentChooseEntries) {
                row = sheet.createRow(rowNo);

                cell = row.createCell(0);
                cell.setCellValue(studentChooseEntry.getUserName());
                cell = row.createCell(1);
                cell.setCellValue(classEntryMap.get(studentChooseEntry.getClassId()));
                int j = 2;
                for (SubjectConfDTO subjectConfDTO : subjectConfDTOs) {
                    cell = row.createCell(j++);
                    List<ObjectId> advs = studentChooseEntry.getAdvancelist();
                    String value = advs.contains(new ObjectId(subjectConfDTO.getSubjectId())) ? "1" : "";
                    cell.setCellValue(value);
                }
                rowNo++;
            }


        }

        outPutWorkBook(wb, response, "按组合-");

    }

    private List<IdValuePairDTO> getSubjectGroupStudentPair(ObjectId schoolId, String year, ObjectId gradeId, ObjectId xuanKeId) throws Exception {
        List<IdValuePairDTO> pairDTOs = new ArrayList<IdValuePairDTO>();
        SchoolSubjectGroupEntry schoolSubjectGroupEntry = schoolSubjectGroupDao.getEntry(schoolId, year, gradeId);
        List<SchoolSubjectGroupEntry.SubjectGroup> subjectGroups = schoolSubjectGroupEntry.getSubjectGroups();
        for (SchoolSubjectGroupEntry.SubjectGroup subjectGroup : subjectGroups) {
            if (subjectGroup.getIsPublic()) {
                List<StudentChooseEntry> studentChooseEntries = studentXuankeDao.getStudentChoosesBySubjectGroup(subjectGroup.getAdvSubjects(), xuanKeId);
                pairDTOs.add(new IdValuePairDTO(subjectGroup.getId(), studentChooseEntries));
            }
        }
        Collections.sort(pairDTOs, new Comparator<IdValuePairDTO>() {
            @Override
            public int compare(IdValuePairDTO dto1, IdValuePairDTO dto2) {
                List<StudentChooseEntry> list1 = (List<StudentChooseEntry>) dto1.getValue();
                List<StudentChooseEntry> list2 = (List<StudentChooseEntry>) dto2.getValue();
                return list2.size() - list1.size();
            }
        });

        return pairDTOs;
    }


    private void outPutWorkBook(HSSFWorkbook wb, HttpServletResponse response, String typeName) {
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
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(typeName + "选课结果.xls", "UTF-8"));
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


}
