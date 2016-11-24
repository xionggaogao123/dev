package com.fulaan.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by moslpc on 2016/10/18.
 */
public class ExcelHelper {

    public static void writeToExcel(List<List<Object>> content,String path,String sheetName) throws IOException{
        //创建excel工作簿
        Workbook wb = new HSSFWorkbook();
        //创建第一个sheet（页），命名为 new sheet
        Sheet sheet = wb.createSheet(sheetName);
        for(int i=0;i<content.size();i++){
            // 创建一行，在页sheet上
            Row row = sheet.createRow((short) i);
            List<Object> objects = content.get(i);
            for(int j=0;j<objects.size();j++){
                Cell cell = row.createCell(j);
                Object obj = objects.get(j);
                if (obj instanceof Date)
                    cell.setCellValue((Date) obj);
                else if (obj instanceof Boolean)
                    cell.setCellValue((Boolean) obj);
                else if (obj instanceof String)
                    cell.setCellValue((String) obj);
                else if (obj instanceof Double)
                    cell.setCellValue((Double) obj);
                else if(obj instanceof Integer)
                    cell.setCellValue((Integer) obj);
                else
                    cell.setCellValue(obj.toString());
            }
        }

        //创建一个文件 命名为workbook.xls
        FileOutputStream fileOut = new FileOutputStream(path);
        // 把上面创建的工作簿输出到文件中
        wb.write(fileOut);
        //关闭输出流
        fileOut.close();
    }


    public static void main(String[] args){

        List<List<Object>> list = new ArrayList<List<Object>>();
        List<Object> objects = new ArrayList<Object>();
        objects.add("sdfdsf");
        objects.add(2323);
        objects.add(23.23);
        list.add(objects);

        try {
            writeToExcel(list,"E:\\hello.xls","点赞");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
