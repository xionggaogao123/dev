package com.fulaan.admin;

import com.fulaan.pojo.FLoginLog;
import com.fulaan.user.service.UserService;
import com.fulaan.util.DateUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.util.List;

/**
 * Created by jerry on 2016/10/20.
 * 登录数据
 */
@Controller
@RequestMapping("/loginInfo")
public class LoginInfoController {

    @Autowired
    private UserService loginLogService;

    @RequestMapping("/downloadXls")
    public void loginInfo(String date, HttpServletResponse response) throws IOException, ParseException {

        long start = DateUtils.strToTimeStamp(date + " 00:00:00") * 1000;
        long end = DateUtils.strToTimeStamp(date + " 24:00:00") * 1000;

        List<FLoginLog> loginLogs = loginLogService.getLoginLog(start, end);
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName="
                + java.net.URLEncoder.encode(date + ".xls", "UTF-8"));

        //创建excel工作簿
        Workbook wb = new HSSFWorkbook();
        //创建第一个sheet（页），命名为 new sheet
        Sheet sheet = wb.createSheet("登录数据");

        for (int i = 0; i < loginLogs.size(); i++) {
            // 创建一行，在页sheet上
            Row row = sheet.createRow((short) i);

            FLoginLog loginLog = loginLogs.get(i);
            // Or do it on one line.
            row.createCell(0).setCellValue(loginLog.getUserName());
            row.createCell(1).setCellValue(loginLog.getNickName());
            row.createCell(2).setCellValue(DateUtils.timeStampToStr(loginLog.getLoginTime() / 1000));
            row.createCell(3).setCellValue(loginLog.getIp());
            row.createCell(4).setCellValue(loginLog.getPf().getName());
        }

        //创建一个文件 命名为workbook.xls
        File file = new File("/word/" + date + ".xls");
        FileOutputStream fileOut = new FileOutputStream(file);
        // 把上面创建的工作簿输出到文件中
        wb.write(fileOut);
        //关闭输出流
        IOUtils.closeQuietly(fileOut);
        try {
            FileInputStream input = new FileInputStream(file);
            OutputStream os = response.getOutputStream();
            IOUtils.copy(input, os);
            // 这里主要关闭。
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(os);
            //删除文件
            FileUtils.deleteQuietly(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
