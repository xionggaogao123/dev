package com.fulaan.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.RichTextString;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by niefang on 2017/3/7.
 * poi帮助类
 */
public class HSSFUtils {

    /**
     * 导出Excel
     *
     * @param response
     * @param workbook
     * @param fileName
     */
    public static void exportExcel(HttpServletResponse response, HSSFWorkbook workbook, String fileName) {
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.setContentType("application/force-download");
            String filename = fileName.endsWith(".xls") ? fileName : fileName + ".xls";
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
            workbook.write(outputStream);
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
     * 设置打印边距
     *
     * @param sheet
     * @param top
     * @param right
     * @param bottom
     * @param left
     */
    public static void setMargin(HSSFSheet sheet, double top, double right, double bottom, double left) {
        sheet.setMargin(HSSFSheet.HeaderMargin, top);
        sheet.setMargin(HSSFSheet.RightMargin, right);
        sheet.setMargin(HSSFSheet.BottomMargin, bottom);
        sheet.setMargin(HSSFSheet.LeftMargin, left);
    }

    /**
     * 设置字体通用方法，方法中没有的属性通过返回的font对象调用poi的相关方法添加
     *
     * @param wb         workbook对象
     * @param fontSize   字体大小，也可认为是字体的高度对应cell的行高（可以通过计算实现自适应行高）
     * @param fontName   字体名字
     * @param boldWeight 字体粗细（是否为黑体）
     * @param color      颜色
     * @param ifItalic   是否是斜体
     * @return
     */
    public static HSSFFont getFont(HSSFWorkbook wb, short fontSize, String fontName, short boldWeight, short color, boolean ifItalic) {
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints(fontSize);
        font.setFontName(fontName);
        font.setBoldweight(boldWeight);
        font.setColor(color);
        font.setItalic(ifItalic);
        return font;
    }

    /**
     * 设置基础单元格格式，方法中没有的属性使用返回的对象调用poi相关方法设置
     *
     * @param wb
     * @param alignment
     * @param wraptext          自动换行
     * @param verticalAlignment 单元格字体对齐方式
     * @param bleft             左边框
     * @param btop              上边框
     * @param bright            右边框
     * @param bbootom           底部边框
     * @param font              字体
     * @return
     */
    public static HSSFCellStyle getHSSFCellStyle(HSSFWorkbook wb, short alignment, boolean wraptext, short verticalAlignment,
                                                 short bleft, short btop, short bright, short bbootom, HSSFFont font) {
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(alignment);
        cellStyle.setWrapText(wraptext);
        cellStyle.setVerticalAlignment(verticalAlignment);// 指定单元格垂直居中对
        cellStyle.setBorderLeft(bleft);//左边框
        cellStyle.setBorderTop(btop);//上边框
        cellStyle.setBorderRight(bright);//右边框
        cellStyle.setBorderBottom(bbootom);//右边框
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * 设置单元格样式
     * 
     * @param row
     * @param style
     * @param start
     * @param end
     */
    public static void setStyle(HSSFRow row, HSSFCellStyle style, int start, int end) {
    	for (; start <= end; start++) {
    		HSSFCell cell = row.getCell(start);
    		if (cell == null) {
    			cell = row.createCell(start);
    		}
    		cell.setCellStyle(style);
    	}
    }
    
    /**
     * 填充单元格
     * 
     * @param row
     * @param cellValues   单元格数据列表
     * @param cellStyle    单元格样式（可为null）
     */
    public static <T extends Object> void fillCell(HSSFRow row, List<T> cellValues, HSSFCellStyle cellStyle) {
    	fillCell(row, cellValues, cellStyle, 0, cellValues.size() - 1);
    }
    
    /**
     * 填充单元格
     * 
     * @param row
     * @param cellValues   单元格数据列表
     * @param cellStyle    单元格样式（可为null）
     * @param startStyleIndex 样式应用的开始位置
     * @param endStyleIndex 样式应用的结束位置
     */
    public static <T extends Object> void fillCell(HSSFRow row, List<T> cellValues, HSSFCellStyle cellStyle, int startStyleIndex, int endStyleIndex) {
    	if (cellValues != null && !cellValues.isEmpty()) {
    		for (int i = 0, j = cellValues.size(); i < j; i++) {
    			
    			HSSFCell cell = row.createCell(i);
    			Object value = cellValues.get(i);
    			
    			if (value instanceof String) {
    				cell.setCellValue((String) value);
    			} else if (value instanceof Date) {
    				cell.setCellValue((Date) value);
    			} else if (value instanceof Double) {
    				cell.setCellValue((Double) value);
    			} else if (value instanceof Boolean) {
    				cell.setCellValue((Boolean) value);
    			} else if (value instanceof Calendar) {
    				cell.setCellValue((Calendar) value);
    			} else if (value instanceof RichTextString) {
    				cell.setCellValue((RichTextString) value);
    			}
    			
    		}
    		
    		for (; startStyleIndex <= endStyleIndex; startStyleIndex++) {
    			if (cellStyle != null) {
    				HSSFCell cell = row.getCell(startStyleIndex);
    				if (cell == null) {
    					cell = row.createCell(startStyleIndex);
    				}
    				cell.setCellStyle(cellStyle);
    			}
    		}
    	}
    }


}
