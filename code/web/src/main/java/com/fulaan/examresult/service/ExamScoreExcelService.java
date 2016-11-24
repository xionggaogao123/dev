package com.fulaan.examresult.service;

import com.db.examresult.PerformanceDao;
import com.fulaan.examresult.controller.PerformanceDTO;
import com.sys.constants.Constant;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by fl on 2015/8/6.
 */
@Service
public class ExamScoreExcelService {

    private PerformanceDao performanceDao = new PerformanceDao();

    /**
     * 下载成绩
     * @param performanceDTOList
     * @param response
     */
    public void exportExamScoreExcel(List<PerformanceDTO> performanceDTOList, HttpServletResponse response, String examTitle) {
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("成绩列表");
        //为sheet1生成第一行，用于放表头信息
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("姓名");
        cell = row.createCell(1);
        cell.setCellValue("考试分数");
        cell = row.createCell(2);
        cell.setCellValue("缺考");
        cell = row.createCell(3);
        cell.setCellValue("免考");

        int page=0;
        for(int i=0; i<performanceDTOList.size(); i++) {
            PerformanceDTO performanceDTO = performanceDTOList.get(i);
            page++;
            row = sheet.createRow(page);

            cell = row.createCell(0);
            cell.setCellValue(performanceDTO.getStudentName());
            cell = row.createCell(1);
//            Double score = performanceDTO.getSubjectScore();
//            String value = score==null?"":score.toString();
            cell.setCellValue("");
            cell = row.createCell(2);
            cell.setCellValue(performanceDTO.getAbsence());
            cell = row.createCell(3);
            cell.setCellValue(performanceDTO.getExemption());
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
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(examTitle + "成绩.xls", "UTF-8"));
            response.setContentLength(content.length);
            outputStream.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(outputStream != null){
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
     * 导入成绩
     * @param inputStream
     * @param examId
     */
    public void importStudentScore(InputStream inputStream,ObjectId examId, ObjectId subjectId) {
        HSSFWorkbook workbook = null;
        try {
            workbook = new HSSFWorkbook(inputStream);
            HSSFSheet studentSheet = workbook.getSheet("成绩列表");
            int rowNum = studentSheet.getLastRowNum();

            for (int i = 1; i <= rowNum; i++) {
                try {
                    String name = studentSheet.getRow(i).getCell(0).getStringCellValue();
                    HSSFCell cell = studentSheet.getRow(i).getCell(1);
                    Double score = getStringCellValue(cell) == "" ? null : cell.getNumericCellValue();
                    int absence = (int)studentSheet.getRow(i).getCell(2).getNumericCellValue();
                    int exemption = (int)studentSheet.getRow(i).getCell(3).getNumericCellValue();
//                    System.out.println(name + "\t\t" + score + "\t\t" + absence + "\t\t" + exemption);
                    performanceDao.updateScore(examId, subjectId, score, absence, exemption, name);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取单元格数据内容为字符串类型的数据
     *
     * @param cell Excel单元格
     * @return String 单元格数据内容
     */
    private String getStringCellValue(HSSFCell cell) {
        if(cell==null) return Constant.EMPTY;
        String strCell;

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

        return StringUtils.isBlank(strCell)?Constant.EMPTY:strCell;
    }

}
