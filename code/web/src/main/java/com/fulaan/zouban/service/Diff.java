package com.fulaan.zouban.service;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by wangkaidong on 2016/10/25.
 */
public class Diff {

    public static void main(String[] args) {
        Map<String, String> oldMap = new HashMap<String, String>();
        Map<String, String> newMap = new HashMap<String, String>();


        try {
            File file = new File("D:\\diff.xls");
            InputStream inputStream = new FileInputStream(file);

            //模板
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);

            HSSFSheet sheet1 = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet1.getLastRowNum(); i++) {
                HSSFRow row = sheet1.getRow(i);
                row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                String num = row.getCell(0).getStringCellValue().trim();
                String dengji = "";

                for (int j = 2; j < 8; j++) {
                    if (row.getCell(j) != null) {
                        if (row.getCell(j).getStringCellValue().trim().length() == 2) {
                            dengji = dengji + row.getCell(j).getStringCellValue().trim() + ",";
                        }
                    }
                }
                newMap.put(num, dengji);
            }

            HSSFSheet sheet2 = workbook.getSheetAt(1);

            for (int i = 1; i <= sheet1.getLastRowNum(); i++) {
                HSSFRow row = sheet2.getRow(i);
                row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                String num = row.getCell(0).getStringCellValue().trim();
                String dengji = row.getCell(3).getStringCellValue().trim();
                oldMap.put(num, dengji);
            }

            inputStream.close();
            inputStream = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("oldLength:" + oldMap.size());
        System.out.println("newLength:" + newMap.size());

        List<String> numList = new ArrayList<String>();
        if (oldMap.size() > 0 && newMap.size() > 0) {
            for (Map.Entry<String, String> entry : oldMap.entrySet()) {
                String num = entry.getKey();
                String oldSubject = entry.getValue();
                String[] oldSubjs = {oldSubject.substring(0, 2), oldSubject.substring(2, 4), oldSubject.substring(4, 6)};
                if (newMap.containsKey(num)) {
                    String newSubject = newMap.get(num);
                    String[] newSubjs = newSubject.split(",");

                    List<String> oldSubjList = new ArrayList<String>(Arrays.asList(oldSubjs));
                    List<String> newSubjList = new ArrayList<String>(Arrays.asList(newSubjs));
                    oldSubjList.retainAll(newSubjList);
                    if (oldSubjList.size() < 3) {
                        numList.add(num);
                    }
                } else {
                    System.out.println(num + "未找到");
                }
            }
        }

        System.out.println("有" + numList.size() + "个人选课结果不对");
        for (String num : numList) {
            System.out.println(num + ": oldSubj:" + oldMap.get(num) + ", newSubj:" + newMap.get(num));
        }
    }
}
