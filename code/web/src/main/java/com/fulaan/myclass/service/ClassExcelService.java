package com.fulaan.myclass.service;

import com.db.school.InterestClassDao;
import com.db.school.SchoolDao;
import com.db.temp.InterestClass;
import com.db.user.UserDao;
import com.fulaan.myclass.controller.TeacherInfoView;
import com.fulaan.utils.ExportUtil;
import com.pojo.app.FieldValuePair;
import com.pojo.school.InterestClassEntry;
import com.pojo.school.InterestClassStudent;
import com.pojo.school.SchoolEntry;
import com.pojo.user.StudentStat;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.Region;
import org.bson.types.ObjectId;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qiangm on 2015/7/17.
 */
public class ClassExcelService {

    private ClassService classService = new ClassService();
    private UserDao userDao = new UserDao();
    private SchoolDao schoolDao = new SchoolDao();
    private InterestClassDao interestClassDao = new InterestClassDao();

    /**
     * 导出信息
     *
     * @param classId
     * @param response
     */
    public void exportExcel(String classId, HttpServletResponse response) {
        List<StudentStat> userInfoDTOList = classService.statCommonClassStudentInfo(new ObjectId(classId));
        String className = classService.findClassInfoByClassId(classId).getClassName();
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("学生列表");
        //为sheet1生成第一行，用于放表头信息
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("学生姓名");
        cell = row.createCell(1);
        cell.setCellValue("看完视频数");
        cell = row.createCell(2);
        cell.setCellValue("做题数");
        cell = row.createCell(3);
        cell.setCellValue("学号");
        cell = row.createCell(4);
        cell.setCellValue("班干部或课代表");

        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
        for (int i = 0; i < userInfoDTOList.size(); i++) {
            StudentStat student = userInfoDTOList.get(i);
            row = sheet.createRow(i + 1);

            cell = row.createCell(0);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(student.getUserName());
            cell = row.createCell(1);

            cell.setCellStyle(cellStyle);
            cell.setCellValue(student.getEndViewNum());
            cell = row.createCell(2);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(student.getEndQuestionNum());
            cell = row.createCell(3);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(student.getStudentNum());
            cell = row.createCell(4);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(student.getStudentJob());
        }
        List<TeacherInfoView> teacherInfoViews = classService.findTeachersByClassId(new ObjectId(classId));
        //生成sheet2
        HSSFSheet sheet2 = wb.createSheet("老师列表");
        HSSFRow row2 = sheet2.createRow(0);

        HSSFCell cell2 = row2.createCell(0);
        cell2.setCellValue("教师姓名");

        cell2 = row2.createCell(1);
        cell2.setCellValue("教师职务");


        int page = 0;
        for (int i = 0; i < teacherInfoViews.size(); i++) {

            TeacherInfoView teacher = teacherInfoViews.get(i);
            for (int j = 0; j < teacher.getSubjectNameList().size(); j++) {
                page++;
                row2 = sheet2.createRow(page);

                cell2 = row2.createCell(0);
                cell2.setCellStyle(cellStyle);
                cell2.setCellValue(teacher.getUserName());
                cell2 = row2.createCell(1);
                cell2.setCellValue(teacher.getSubjectNameList().get(j) + "老师");
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

            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(className + "信息统计.xls", "UTF-8"));
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
     * 下载模板
     *
     * @param classId
     * @param response
     */
    public void generalExcel(String classId, HttpServletResponse response) {
        List<UserDetailInfoDTO> userDetailInfoDTOList = classService.findStuByClassId(classId);
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("sheet1");
        //为sheet1生成第一行，用于放表头信息
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("学生姓名");

        cell = row.createCell(1);
        cell.setCellValue("学号");
        cell = row.createCell(2);
        cell.setCellValue("班干部或课代表");

        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
        for (int i = 0; i < userDetailInfoDTOList.size(); i++) {
            UserDetailInfoDTO user = userDetailInfoDTOList.get(i);
            row = sheet.createRow(i + 1);

            cell = row.createCell(0);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(user.getUserName());
            cell = row.createCell(1);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(user.getStudentNum());
            cell = row.createCell(2);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(user.getJobName());
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

            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("批量学号及班干部管理.xls", "UTF-8"));
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
     * 将excel表格内容转为List
     *
     * @param inputStream
     * @return
     */
    public List<UserDetailInfoDTO> getStudentList(InputStream inputStream, String classId) {
        List<UserDetailInfoDTO> userDetailInfoDTOList = classService.findStuByClassId(classId);
        List<String> nameList = new ArrayList<String>();
        Map<String, String> nameIdMap = new HashMap<String, String>();
        for (UserDetailInfoDTO user : userDetailInfoDTOList) {
            nameList.add(user.getUserName());
            nameIdMap.put(user.getUserName(), user.getId());
        }
        List<UserDetailInfoDTO> userEntryList = new ArrayList<UserDetailInfoDTO>();
        HSSFWorkbook workbook = null;
        try {
            workbook = new HSSFWorkbook(inputStream);
            HSSFSheet studentSheet = workbook.getSheet("sheet1");
            int rowNum = studentSheet.getLastRowNum();
            //先判断栏目是否合适
            if (getStringCellValue(studentSheet.getRow(0).getCell(0)).equals("学生姓名") &&
                    getStringCellValue(studentSheet.getRow(0).getCell(1)).equals("学号") &&
                    getStringCellValue(studentSheet.getRow(0).getCell(2)).equals("班干部或课代表")) {
                for (int i = 1; i <= rowNum; i++) {

                    try {
                        String userName = getStringCellValue(studentSheet.getRow(i).getCell(0));
                        if (nameList.contains(userName)) {
                            String studentNum = getStringCellValue(studentSheet.getRow(i).getCell(1));
                            String studentJob = getStringCellValue(studentSheet.getRow(i).getCell(2));
                            if (studentNum.endsWith(".0"))
                                studentNum = studentNum.substring(0, studentNum.length() - 2);
                            if (studentJob.endsWith(".0"))
                                studentJob = studentJob.substring(0, studentJob.length() - 2);

                            UserDetailInfoDTO user = new UserDetailInfoDTO();
                            user.setId(nameIdMap.get(userName));
                            user.setUserName(userName);
                            user.setStudentNum(studentNum);
                            user.setJobName(studentJob);
                            userEntryList.add(user);
                        }
                    } catch (Exception e) {

                    }
                }
            }
        } catch (Exception e) {

        }
        return userEntryList;
    }

    //根据Excel表格内容修改学号
    public void updateStudent(InputStream inputStream, String classId) {
        List<UserDetailInfoDTO> studentList = getStudentList(inputStream, classId);
        for (UserDetailInfoDTO userDetailInfoDTO : studentList) {
            UserEntry userEntry = userDetailInfoDTO.exportEntry();
            userDao.update(userEntry.getID(), new FieldValuePair("sn", userEntry.getStudyNum()), new FieldValuePair("jo", userEntry.getJob()));
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


    /**
     * 导出学生兴趣班excel
     *
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param response
     */
    public void exportStuInteClassExcel(String schoolId, String gradeId, String classId, int termType, HttpServletResponse response) {
        List<StudentStat> list = classService.studentInterestClassInfo(schoolId, gradeId, classId, termType);
        SchoolEntry se = schoolDao.getSchoolEntry(new ObjectId(schoolId),Constant.FIELDS);
        String titleName = se.getName();

        if (list != null && list.size() > 0) {
            StudentStat temp = list.get(0);
            if (classId != null && !"".equals(classId)) {
                titleName += temp.getGradeName();
                titleName += temp.getClassName();
            } else if (gradeId != null && !"".equals(gradeId)) {//当班级id为空时，判断年级id是否为空
                titleName += temp.getGradeName();
            }
        }
        titleName += "学生选课去向表";
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet();
        sheet.setColumnWidth(0, 357 * 4);
        sheet.setColumnWidth(1, 357 * 10);
        sheet.setColumnWidth(2, 357 * 12);
        sheet.setColumnWidth(3, 357 * 12);
        sheet.setColumnWidth(4, 357 * 30);

        //设置一级表头样式字体，颜色，填充，样式
        HSSFCellStyle titleStyle = ExportUtil.publicCellStyle(wb, "宋体", "BLACK", (short) 14,
                "BOLD", "FOREGROUND", "GREY_25_PERCENT", "CENTER", "CENTER", "THIN", "THIN", "THIN", "THIN", false);

        //为sheet1生成第一行，用于放表头信息
        HSSFRow titleRow = sheet.createRow(0);
        HSSFCell titleCell = titleRow.createCell(0);

        titleCell.setCellStyle(titleStyle);

        titleCell.setCellValue(titleName);
        sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) 4));// 起始行号, 起始列号，结束行号，结束列号

        //标题字段行的字体，颜色，填充，样式
        HSSFCellStyle style = ExportUtil.publicCellStyle(wb, "黑体", "BLACK", (short) 12,
                "BOLD", "FOREGROUND", "GREY_25_PERCENT", "CENTER", "CENTER", "THIN", "THIN", "THIN", "THIN", false);
        HSSFRow row = sheet.createRow(1);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("年级");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellStyle(style);
        cell.setCellValue("班级");
        cell = row.createCell(3);
        cell.setCellStyle(style);
        cell.setCellValue("学生姓名");
        cell = row.createCell(4);
        cell.setCellStyle(style);
        cell.setCellValue("选课信息");
        cell.setCellStyle(style);

        //数据行的字体，颜色，填充，样式
        HSSFCellStyle cellStyle = ExportUtil.publicCellStyle(wb, "黑体", "BLACK", (short) 10,
                "NORMAL", "", "", "CENTER", "CENTER", "THIN", "THIN", "THIN", "THIN", true);
        cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
        for (int i = 0; i < list.size(); i++) {
            StudentStat student = list.get(i);
            row = sheet.createRow(i + 2);
            cell = row.createCell(0);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(i + 1);
            cell = row.createCell(1);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(student.getGradeName());
            cell = row.createCell(2);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(student.getClassName());
            cell = row.createCell(3);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(student.getUserName());
            cell = row.createCell(4);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(student.getInterestClassInfo());
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

            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(titleName + ".xls", "UTF-8"));
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


    public void exportInteClassStuExcel(String schoolId, int termType, HttpServletResponse response){
        SchoolEntry se = schoolDao.getSchoolEntry(new ObjectId(schoolId),Constant.FIELDS);
        if(termType < 0){
            termType = se.getTermType();
        }
        Map<String, StudentStat> studentStatMap = new HashMap<String, StudentStat>();
        List<StudentStat> list = classService.studentInterestClassInfo(schoolId, null, null, termType);
        if(list != null){
            for(StudentStat studentStat : list){
                studentStatMap.put(studentStat.getStudentId(), studentStat);
            }
        }

        String titleName = se.getName() + "学生选课去向表";

        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet();
        sheet.setColumnWidth(0, 357 * 4);
        sheet.setColumnWidth(1, 357 * 20);
        sheet.setColumnWidth(2, 357 * 12);
        sheet.setColumnWidth(3, 357 * 12);
        sheet.setColumnWidth(4, 357 * 12);

        //设置一级表头样式字体，颜色，填充，样式
        HSSFCellStyle titleStyle = ExportUtil.publicCellStyle(wb, "宋体", "BLACK", (short) 14,
                "BOLD", "FOREGROUND", "GREY_25_PERCENT", "CENTER", "CENTER", "THIN", "THIN", "THIN", "THIN", false);

        //为sheet1生成第一行，用于放表头信息
        HSSFRow titleRow = sheet.createRow(0);
        HSSFCell titleCell = titleRow.createCell(0);

        titleCell.setCellStyle(titleStyle);

        titleCell.setCellValue(titleName);
        sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) 4));// 起始行号, 起始列号，结束行号，结束列号

        //标题字段行的字体，颜色，填充，样式
        HSSFCellStyle style = ExportUtil.publicCellStyle(wb, "黑体", "BLACK", (short) 12,
                "BOLD", "FOREGROUND", "GREY_25_PERCENT", "CENTER", "CENTER", "THIN", "THIN", "THIN", "THIN", false);
        HSSFRow row = sheet.createRow(1);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("拓展课班级");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellStyle(style);
        cell.setCellValue("学生姓名");
        cell = row.createCell(3);
        cell.setCellStyle(style);
        cell.setCellValue("年级");
        cell = row.createCell(4);
        cell.setCellStyle(style);
        cell.setCellValue("来自行政班级");
        cell.setCellStyle(style);

        //数据行的字体，颜色，填充，样式
        HSSFCellStyle cellStyle = ExportUtil.publicCellStyle(wb, "黑体", "BLACK", (short) 10,
                "NORMAL", "", "", "CENTER", "CENTER", "THIN", "THIN", "THIN", "THIN", true);
        cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));


        List<InterestClassEntry> interestClassEntries = interestClassDao.findClassBySchoolId(new ObjectId(schoolId), termType, null, null);
        int i = 0;
        if(interestClassEntries != null){
            for(InterestClassEntry interestClassEntry : interestClassEntries){
                List<InterestClassStudent> students = interestClassEntry.getInterestClassStudentsByTermType(termType);
                if(students.size() > 0) {
                    for (InterestClassStudent stu : students) {
                        StudentStat student = studentStatMap.get(stu.getStudentId().toString());
                        if(student == null){
                            continue;
                        }
                        row = sheet.createRow(i + 2);

                        cell = row.createCell(0);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(i + 1);

                        cell = row.createCell(1);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(interestClassEntry.getClassName());

                        cell = row.createCell(2);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(student.getUserName());

                        cell = row.createCell(3);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(student.getGradeName());

                        cell = row.createCell(4);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(student.getClassName());

                        i++;
                    }
                } else {
                    row = sheet.createRow(i + 2);

                    cell = row.createCell(0);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(i + 1);

                    cell = row.createCell(1);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(interestClassEntry.getClassName());

                    cell = row.createCell(2);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("");

                    cell = row.createCell(3);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("");

                    cell = row.createCell(4);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("");

                    i++;
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

            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(titleName + ".xls", "UTF-8"));
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
