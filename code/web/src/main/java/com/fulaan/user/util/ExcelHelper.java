package com.fulaan.user.util;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by moslpc on 2016/8/11.
 */
public class ExcelHelper {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 字符
    private static DecimalFormat df = new DecimalFormat("0");// 格式化 number String
    private static DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字


    public static Workbook getWorkbook(File file) throws Exception {
        String fileName = file.getName();
        String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName
                .substring(fileName.lastIndexOf(".") + 1);
        FileInputStream fis = new FileInputStream(file);
        // 根据不同的文件名返回不同类型的WorkBook
        if (extension.equals("xls")) {
            return new HSSFWorkbook(fis);
        } else if (extension.equals("xlsx")) {
            return new XSSFWorkbook(fis);
        } else {
            throw new Exception("不支持该格式的文件！");
        }
    }

    /**
     * 读取excel 文件
     *
     * @param file
     * @param startSheet
     * @param startRow
     * @return
     */
    public static List<List<Object>> readExcel(File file, int startSheet, int startRow) {
        List<List<Object>> list = new LinkedList<List<Object>>();
        Workbook wb = null;
        try {
            wb = getWorkbook(file);
            Sheet sheet = wb.getSheetAt(startSheet);
            Object value = null;
            Row row = null;
            Cell cell = null;
            CellStyle cs = null;
            String csStr = null;
            Double numval = null;
            Iterator<Row> rows = sheet.rowIterator();
            while (rows.hasNext()) {
                row = (Row) rows.next();
                if (row.getRowNum() >= startRow) {
                    List<Object> cellList = new LinkedList<Object>();
                    ;
                    Iterator<Cell> cells = row.cellIterator();
                    while (cells.hasNext()) {
                        cell = (Cell) cells.next();
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_NUMERIC:
                                cs = cell.getCellStyle();
                                csStr = cs.getDataFormatString();
                                numval = cell.getNumericCellValue();
                                if ("@".equals(csStr)) {
                                    value = df.format(numval);
                                } else if ("General".equals(csStr)) {
                                    value = nf.format(numval);
                                } else {
                                    value = sdf.format(HSSFDateUtil.getJavaDate(numval));
                                }
                                break;
                            case Cell.CELL_TYPE_STRING:
                                value = cell.getStringCellValue();
                                break;
                            case Cell.CELL_TYPE_FORMULA:
                                if (!cell.getStringCellValue().equals("")) {
                                    value = cell.getStringCellValue();
                                } else {
                                    value = cell.getNumericCellValue() + "";
                                }
                                break;
                            case Cell.CELL_TYPE_BLANK:
                                value = "";
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                value = cell.getBooleanCellValue();
                                break;
                            default:
                                value = cell.toString();
                        }
                        cellList.add(value);
                    }
                    list.add(cellList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static final void writeExcel(String sheetName, List<Map<String, String>> list, String filename) {


        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(sheetName);

        Map<String, Object[]> data = new HashMap<String, Object[]>();
        for (int i = 0; i < list.size(); i++) {
            String line = String.valueOf(i + 1);
            Map<String, String> maps = list.get(i);
            data.put(line, new Object[]{maps.get("id"), maps.get("nm"), maps.get("sex"),
                    maps.get("nnm")});
        }

        Set<String> keyset = data.keySet();
        int rownum = 0;
        for (String key : keyset) {
            Row row = sheet.createRow(rownum++);
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell = row.createCell(cellnum++);
                if (obj instanceof Date)
                    cell.setCellValue((Date) obj);
                else if (obj instanceof Boolean)
                    cell.setCellValue((Boolean) obj);
                else if (obj instanceof String)
                    cell.setCellValue((String) obj);
                else if (obj instanceof Double)
                    cell.setCellValue((Double) obj);
            }
        }

        try {
            FileOutputStream out =
                    new FileOutputStream(new File("E:\\" + filename + ".xls"));
            workbook.write(out);
            out.close();
            System.out.println("Excel written successfully..");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
