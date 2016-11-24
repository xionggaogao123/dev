package com.fulaan.zouban.controller.jinyuan;

import com.db.school.ClassDao;
import com.db.zouban.ZouBanCourseDao;
import com.fulaan.base.controller.BaseController;
import com.fulaan.user.service.UserService;
import com.fulaan.zouban.service.BianbanService;
import com.mongodb.BasicDBObject;
import com.pojo.school.ClassEntry;
import com.pojo.user.UserEntry;
import com.pojo.zouban.ZouBanCourseEntry;
import com.pojo.zouban.ZoubanType;
import com.sys.utils.RespObj;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
 * Created by wangkaidong on 2016/10/24.
 */
@Controller
@RequestMapping("/importZBData")
public class ImportController extends BaseController {

    private ClassDao classDao = new ClassDao();
    private ZouBanCourseDao zouBanCourseDao = new ZouBanCourseDao();

    @Autowired
    private UserService userService;
    @Autowired
    private BianbanService bianbanService;



    final ObjectId gradeId = new ObjectId("56937c660cf2dd4bb8da5e49");
    final String term = "2016-2017学年";

    @RequestMapping
    public String importPage() {
        return "/zoubannew/admin/importData";
    }


    /**
     * 下载学生学号模板
     *
     * @param response
     */
    @RequestMapping("/downloadNumTemplate")
    @ResponseBody
    public void downloadNumTemplate(HttpServletResponse response) {
        byte[] content = downloadNumTemplate();
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("学生学号模板.xls", "UTF-8"));
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
     * 下载学生学号模板
     *
     * @return
     */
    private byte[] downloadNumTemplate() {
        //查全年级学生
        List<ClassEntry> classEntryList = classDao.findClassEntryByGradeId(gradeId);
        List<ObjectId> studentIds = new ArrayList<ObjectId>();
        for (ClassEntry classEntry : classEntryList) {
            studentIds.addAll(classEntry.getStudents());
        }
        Map<ObjectId, UserEntry> userEntryMap = userService.getUserEntryMap(studentIds, new BasicDBObject());


        //生成Excel
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成sheet
        HSSFSheet sheet = wb.createSheet("学生学号");
        //Excel样式
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中

        //设置表头
        HSSFRow row0 = sheet.createRow(0);
        HSSFCell cell0 = row0.createCell(0);
        cell0.setCellValue("学生姓名");
        cell0.setCellStyle(cellStyle);
        HSSFCell cell1 = row0.createCell(1);
        cell1.setCellValue("学号");
        cell1.setCellStyle(cellStyle);

        //生成模板
        int rowLine = 0;
        for (UserEntry userEntry : userEntryMap.values()) {
            rowLine++;
            if (userEntry == null) {
                continue;
            }
            String userName = userEntry.getRealUserName();

            HSSFRow row = sheet.createRow(rowLine);
            HSSFCell cell = row.createCell(0);
            cell.setCellValue(userName);
            cell = row.createCell(1);
            cell.setCellValue("");
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
     * 导入学号
     *
     * @param file
     * @return
     */
    @RequestMapping("/importStuNum")
    @ResponseBody
    public RespObj importStuNum(MultipartFile file) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);

        String fileName = file.getOriginalFilename();
        if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
            respObj.setMessage("文件格式错误，请选择Excel文件");
        } else {
            try {
                importStuNum(file.getInputStream());
                respObj.setCode(RespObj.SUCCESS.code);
                respObj.setMessage("导入成功");
            } catch (Exception e) {
                e.printStackTrace();
                respObj.setMessage(e.getMessage());
            }
        }

        return respObj;
    }

    /**
     * 导入学号
     *
     * @param inputStream
     * @throws IOException
     */
    private void importStuNum(InputStream inputStream) throws Exception {
        //查全年级学生
        List<ClassEntry> classEntryList = classDao.findClassEntryByGradeId(gradeId);
        List<ObjectId> studentIds = new ArrayList<ObjectId>();
        for (ClassEntry classEntry : classEntryList) {
            studentIds.addAll(classEntry.getStudents());
        }
        Map<ObjectId, UserEntry> userEntryMap = userService.getUserEntryMap(studentIds, new BasicDBObject());
        Map<String, ObjectId> userMap = new HashMap<String, ObjectId>();

        for (UserEntry userEntry : userEntryMap.values()) {
            userMap.put(userEntry.getRealUserName(), userEntry.getID());
        }


        //模板
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheetAt(0);

        try {
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                HSSFRow row = sheet.getRow(i);
                String userName = row.getCell(0).getStringCellValue().trim();
                String num = "";
                if (row.getCell(1) != null) {
                    row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
                    num = row.getCell(1).getStringCellValue().trim();
                    if (userMap.containsKey(userName)) {
                        ObjectId userId = userMap.get(userName);
                        userService.update(userId, "sn", num);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("导入失败！\n" + e.getMessage());
        } finally {
            inputStream.close();
            inputStream = null;
        }
    }


    /**
     * 下载教学班学生模板
     *
     * @param response
     */
    @RequestMapping("/downloadStuTemplate")
    @ResponseBody
    public void downloadStuTemplate(HttpServletResponse response) {
        byte[] content = downloadStuTemplate();
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("教学班学生模板.xls", "UTF-8"));
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
     * 下载教学班学生模板
     *
     * @return
     */
    private byte[] downloadStuTemplate() {
        List<ZouBanCourseEntry> zouBanCourseEntryList = zouBanCourseDao.findCourseList(term, gradeId, ZoubanType.ZOUBAN.getType());


        //生成Excel
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成sheet
        HSSFSheet sheet = wb.createSheet("教学班学生");
        //Excel样式
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中


        int colIndex = 0;//列号
        HSSFRow row0 = sheet.createRow(0);
        HSSFRow row1 = sheet.createRow(1);
        HSSFRow row2 = sheet.createRow(2);
        //设置表头
        for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntryList) {
            HSSFCell cell0 = row0.createCell(colIndex);
            cell0.setCellValue(zouBanCourseEntry.getClassName());
            cell0.setCellStyle(cellStyle);

            HSSFCell cell1 = row1.createCell(colIndex);
            cell1.setCellValue(zouBanCourseEntry.getID().toString());
            cell1.setCellStyle(cellStyle);

            HSSFCell cell2 = row2.createCell(colIndex);
            cell2.setCellValue("学号");
            cell2.setCellStyle(cellStyle);

            colIndex++;
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
     * 导入教学班学生
     *
     * @param file
     * @return
     */
    @RequestMapping("/importStus")
    @ResponseBody
    public RespObj importStus(MultipartFile file) {
        RespObj respObj = new RespObj(RespObj.FAILD.code);

        String fileName = file.getOriginalFilename();
        if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
            respObj.setMessage("文件格式错误，请选择Excel文件");
        } else {
            try {
                importStus(file.getInputStream());
                respObj.setCode(RespObj.SUCCESS.code);
                respObj.setMessage("导入成功");
            } catch (Exception e) {
                e.printStackTrace();
                respObj.setMessage(e.getMessage());
            }
        }

        return respObj;
    }

    /**
     * 导入教学班学生
     *
     * @param inputStream
     * @throws IOException
     */
    private void importStus(InputStream inputStream) throws Exception {
        //查全年级学生
        List<ClassEntry> classEntryList = classDao.findClassEntryByGradeId(gradeId);
        List<ObjectId> studentIds = new ArrayList<ObjectId>();
        for (ClassEntry classEntry : classEntryList) {
            studentIds.addAll(classEntry.getStudents());
        }
        Map<ObjectId, UserEntry> userEntryMap = userService.getUserEntryMap(studentIds, new BasicDBObject());
        Map<String, ObjectId> userMap = new HashMap<String, ObjectId>();

        for (UserEntry userEntry : userEntryMap.values()) {
            userMap.put(userEntry.getStudyNum(), userEntry.getID());
        }

        //走班教学班
        List<ZouBanCourseEntry> zouBanCourseEntryList = zouBanCourseDao.findCourseList(term, gradeId, ZoubanType.ZOUBAN.getType());
        Map<ObjectId, List<ObjectId>> zoubanStuMap = new HashMap<ObjectId, List<ObjectId>>();

        for (ZouBanCourseEntry zouBanCourseEntry : zouBanCourseEntryList) {
            zoubanStuMap.put(zouBanCourseEntry.getID(), new ArrayList<ObjectId>());
        }

        List<ObjectId> courseIdList = new ArrayList<ObjectId>();


        //模板
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheetAt(0);

        try {
            HSSFRow idRow = sheet.getRow(1);

            for (int i = 0; i < zouBanCourseEntryList.size(); i++) {
                if (idRow.getCell(i) != null) {
                    String courseId = idRow.getCell(i).getStringCellValue().trim();
                    courseIdList.add(new ObjectId(courseId));
                }

            }


            for (int i = 3; i <= sheet.getLastRowNum(); i++) {
                HSSFRow row = sheet.getRow(i);
                for (int col = 0; col < zouBanCourseEntryList.size(); col++) {
                    if (row.getCell(col) != null) {
                        row.getCell(col).setCellType(Cell.CELL_TYPE_STRING);
                        if (row.getCell(col).getStringCellValue().trim().length() > 0) {
                            String studyNum = row.getCell(col).getStringCellValue().trim();
                            ObjectId courseId = courseIdList.get(col);

                            if (userMap.containsKey(studyNum) && zoubanStuMap.containsKey(courseId)) {
                                zoubanStuMap.get(courseId).add(userMap.get(studyNum));
                            }
                        }
                    }
                }
            }


            for (Map.Entry<ObjectId, List<ObjectId>> entry : zoubanStuMap.entrySet()) {
                ObjectId courseId = entry.getKey();
                List<ObjectId> stuList = entry.getValue();
                zouBanCourseDao.addStudents(courseId, stuList);
                bianbanService.updateZBCourseClassId(courseId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("导入失败！\n" + e.getMessage());
        } finally {
            inputStream.close();
            inputStream = null;
        }
    }


}
