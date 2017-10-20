package com.fulaan.logreport.controller;

import com.fulaan.base.BaseController;
import com.fulaan.logreport.service.BuildLogReportService;
import com.pojo.emailmanage.EmailManageEntry;
import com.pojo.user.UserRole;
import com.sys.mails.MailUtils;
import com.sys.props.Resources;
import com.sys.utils.DateTimeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Vector;

/**
 * Created by guojing on 2016/5/18.
 */
@Api(value="logreport",hidden = true)
@Controller
@RequestMapping("/jxmapi/logreport")
public class DefaultLogReportController extends BaseController {

    private static final Logger cLogger = Logger.getLogger(DefaultLogReportController.class);

    @Autowired
    private BuildLogReportService buildLogReportService;
    @ApiOperation(value = "sendLogReportEmail", httpMethod = "GET", produces = "application/json")
    @ApiResponses( value = {@ApiResponse(code = 200, message = "Successful — 请求已完成")})
    @RequestMapping("/sendLogReportEmail")
    public void sendLogReportEmail() throws Exception {
        int userRole = getSessionValue().getUserRole();
        if (UserRole.isSysManager(userRole)) {
            int type = 1;
            EmailManageEntry emailManage = buildLogReportService.getEmailManageEntry(type);
            if (emailManage != null) {
                String bathPath = Resources.getProperty("upload.file");
                File dir = new File(bathPath);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                cLogger.info(DateTimeUtils.getChineseDate() + ";开始生产日志报表");

                //分段
                int section = emailManage.getUserDef();

                String emails = emailManage.getEmails();


                buildLogReportService.setSection(section);
                HSSFWorkbook wb = new HSSFWorkbook();
                buildLogReportService.buildDailyLogReport(wb);
                buildLogReportService.buildWeekLogReport(wb);
                buildLogReportService.buildMonthLogReport(wb);

                FileOutputStream out = new FileOutputStream(bathPath + "/日志报表.xls");
                wb.write(out);
                out.flush();
                out.close();
                cLogger.info(DateTimeUtils.getChineseDate() + ";生产日志报表完成");
                MailUtils mailUtils = new MailUtils();
                Vector vector = new Vector();

                File file = new File(bathPath + "/日志报表.xls");
                vector.add(file);
                mailUtils.sendMail("日志报表", emails, "日志报表", vector);
                file.delete();
                cLogger.info(DateTimeUtils.getChineseDate() + ";定时发送日志报表邮件完成");
            }
        }
    }
}
