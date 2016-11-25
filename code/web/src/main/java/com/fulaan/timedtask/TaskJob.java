package com.fulaan.timedtask;

import com.db.businessactivity.FieryActivityDao;
import com.db.ebusiness.EVoucherDao;
import com.db.microblog.MicroBlogDao;
import com.fulaan.logreport.service.BuildLogReportService;
import com.pojo.app.RegionEntry;
import com.pojo.emailmanage.EmailManageEntry;
import com.pojo.forum.FLogEntry;
import com.sys.constants.Constant;
import com.sys.mails.MailUtils;
import com.sys.props.Resources;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

/**
 * Created by guojing on 2015/7/22.
 */

public class TaskJob {
    private static final Logger cLogger = Logger.getLogger(TaskJob.class);

    @Autowired
    private BuildLogReportService buildLogReportService;


    /**
     * 定时结束火热活动
     *
     * @return
     */
    public void timedFieryActivityIsEnd() {
        FieryActivityDao fieryActivityDao = new FieryActivityDao();
        cLogger.info("\r\n");
        cLogger.info("\r\n");
        cLogger.info("******************************************************************************************************");
        cLogger.info("******************************************************************************************************");
        cLogger.info(DateTimeUtils.getChineseDate() + ";定时检查火热活动是否结束");
        fieryActivityDao.FieryActivityIsEnd();
    }

    /**
     * 定时开启火热活动
     *
     * @return
     */
    public void timedFieryActivityIsStart() {
        FieryActivityDao fieryActivityDao = new FieryActivityDao();
        cLogger.info("\r\n");
        cLogger.info("\r\n");
        cLogger.info("******************************************************************************************************");
        cLogger.info("******************************************************************************************************");
        cLogger.info(DateTimeUtils.getChineseDate() + ";定时开启火热活动");
        fieryActivityDao.FieryActivityIsStart();
    }

    /**
     * 给日期字符串拼接时分秒
     *
     * @param dateTime 日期
     * @param type     1：表示开始时间，2：表示结束时间
     * @return
     */
    private String handleTime(String dateTime, int type) {
        if (type == 1) {
            //判断查询条件的开始时间是否为空
            dateTime = dateTime == null ? "" : dateTime;
            if (!"".equals(dateTime)) {//不等于空时，拼接" 00:00:00"
                dateTime = dateTime + " 00:00:00";
            } else {//等于空时，设置默认时间
                dateTime = "2014-09-01 00:00:00";
            }
        }
        if (type == 2) {
            //判断查询条件的结束时间是否为空
            dateTime = dateTime == null ? "" : dateTime;
            if (!"".equals(dateTime)) {//不等于空时，拼接" 23:59:59"
                dateTime = dateTime + " 23:59:59";
            } else {//等于空时，设置默认时间
                dateTime = DateTimeUtils.getCurrDate() + " 23:59:59";
            }
        }
        return dateTime;
    }

    /**
     * 定时检查过期抵用券
     *
     * @return
     */
    public void checkVoucherExpiration() {
        EVoucherDao eVoucherDao = new EVoucherDao();
        cLogger.info("\r\n");
        cLogger.info("\r\n");
        cLogger.info("******************************************************************************************************");
        cLogger.info("******************************************************************************************************");
        cLogger.info(DateTimeUtils.getChineseDate() + ";定时检查过期抵用券");
        eVoucherDao.checkVoucherExpiration();
    }

    /**
     * 定时开启新学期兴趣班选课
     *
     * @return
     */
    public void timedSchoolNewInterestTerm() {
        cLogger.info("\r\n");
        cLogger.info("\r\n");
        cLogger.info("******************************************************************************************************");
        cLogger.info("******************************************************************************************************");
        cLogger.info(DateTimeUtils.getChineseDate() + ";定时开启新学期兴趣班选课");
    }

    /**
     * 定时发送日志报表邮件
     *
     * @return
     */
    public void timedSendLogReportEmail() throws Exception {
        int type = 1;
        EmailManageEntry emailManage = buildLogReportService.getEmailManageEntry(type);
        if (emailManage != null) {
            cLogger.info("\r\n");
            cLogger.info("\r\n");
            cLogger.info("******************************************************************************************************");
            cLogger.info("******************************************************************************************************");
            cLogger.info(DateTimeUtils.getChineseDate() + ";开始定时发送日志报表邮件");
            String serverIp = getServerIp();
            cLogger.info(DateTimeUtils.getChineseDate() + ";获取服务器Ip地址:" + serverIp);
            if (serverIp.equals(emailManage.getServerInnerIp()) || serverIp.equals(emailManage.getServerOuterIp())) {
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
            }
            cLogger.info(DateTimeUtils.getChineseDate() + ";定时发送日志报表邮件完成");
        }
    }

    /**
     * 获取服务器Ip地址
     *
     * @return
     */
    private String getServerIp() {
        String SERVER_IP = "";
        try {
            Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) netInterfaces
                        .nextElement();
                ip = (InetAddress) ni.getInetAddresses().nextElement();
                SERVER_IP = ip.getHostAddress();
                if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                    SERVER_IP = ip.getHostAddress();
                    break;
                } else {
                    ip = null;
                }
            }
        } catch (SocketException e) {
            //e.printStackTrace();
        } finally {
            return SERVER_IP;
        }
    }

    /**
     * 定时检查主题帖
     *
     * @return
     */
    public void checkMicroBlog() {
        MicroBlogDao microBlogDao = new MicroBlogDao();
        cLogger.info("\r\n");
        cLogger.info("\r\n");
        cLogger.info("******************************************************************************************************");
        cLogger.info("******************************************************************************************************");
        cLogger.info(DateTimeUtils.getChineseDate() + ";定时检查主题帖");
    }


    /**
     * 商城每日报告
     */
    public void mallDailyReport() {

        MallDailyReportDTO dto = new MallDailyReportDTO();

        int skip = 0;
        int limit = 1000;

        HashSet<ObjectId> userIdSet = new HashSet<ObjectId>();
        while (true) {
            List<FLogEntry> list = buildLogReportService.getFLogEntrys(skip, limit);
            if (null == list || list.isEmpty()) {
                break;
            }

            for (FLogEntry fe : list) {
                try {
                    if (null != fe.getPersonId()) {
                        userIdSet.add(fe.getPersonId());
                    }
                    if (StringUtils.isNotBlank(fe.getActionName())) {
                        dto.increaseBankuaiMap(fe.getActionName());
                    }

                    String minTime = DateTimeUtils.getLongToStrTimeFour(fe.getTime());
                    String maxTime = DateTimeUtils.getLongToStrTimeFour(fe.getTime() + Constant.MS_SECONDS_IN_HOUR);

                    dto.increaseLoginMap(minTime + "-" + maxTime);

                    ObjectId areaId = buildLogReportService.getUserArea(fe.getPersonId());
                    if (null != areaId) {
                        dto.increaseAreaMap(areaId);
                    }
                } catch (Exception ex) {
                    cLogger.error("", ex);
                }
            }

            skip = skip + 1000;
        }

        dto.setCount(userIdSet.size());


        StringBuilder builder = new StringBuilder();
        builder.append("访问人数：" + dto.getCount());
        builder.append("<br/>");


        builder.append("时间访问分布情况：");
        for (Map.Entry<String, Integer> entry : dto.getLoginMap().entrySet()) {
            builder.append(entry.getKey());
            builder.append(":");
            builder.append(entry.getValue());
            builder.append("|");
        }
        builder.append("<br/>");


        builder.append("板块访问分布情况：");
        for (Map.Entry<String, Integer> entry : dto.getBankuaiMap().entrySet()) {
            builder.append(getString(entry.getKey()));
            builder.append(":");
            builder.append(entry.getValue());
            builder.append("|");
        }

        builder.append("<br/>");
        builder.append("地区访问分布情况：");
        for (Map.Entry<ObjectId, Integer> entry : dto.getAreaMap().entrySet()) {
            RegionEntry re = buildLogReportService.getRegionEntry(entry.getKey());
            if (null != re) {
                builder.append(re.getName());
                builder.append(":");
                builder.append(entry.getValue());
                builder.append("|");
            }
        }

        cLogger.info(builder.toString());

        MailUtils mailUtils = new MailUtils();
        String dailyStr = DateTimeUtils.convert(System.currentTimeMillis(), DateTimeUtils.DATE_YYYY_MM_DD);
        try {
            String toEmails = Resources.getProperty("mall.daily.report.emails");

            if (StringUtils.isNotBlank(toEmails)) {
                mailUtils.sendMail(dailyStr + "商城访问情况", toEmails, builder.toString());
            }
        } catch (Exception e) {
            cLogger.error("", e);
        }
    }

    private String getString(String code) {
        if (code.equalsIgnoreCase("login")) {
            return "登录页";
        } else if (code.equalsIgnoreCase("head")) {
            return "首页";
        } else if (code.equalsIgnoreCase("mallHead")) {
            return "商城首页";
        } else if (code.equalsIgnoreCase("mall")) {
            return "mall";
        } else if (code.equalsIgnoreCase("productDetail")) {
            return "商品详情";
        } else if (code.equalsIgnoreCase("cart")) {
            return "购物车";
        } else if (code.equalsIgnoreCase("order")) {
            return "下单";
        } else if (code.equalsIgnoreCase("forumIndex")) {
            return "论坛首页";
        } else if (code.equalsIgnoreCase("postIndex")) {
            return "板块页";
        } else if (code.equalsIgnoreCase("newPost")) {
            return "新增帖子页";
        } else if (code.equalsIgnoreCase("postDetail")) {
            return "帖子详情";
        } else if (code.equalsIgnoreCase("postSearch")) {
            return "搜索页";
        } else if (code.equalsIgnoreCase("task")) {
            return "任务页";
        } else if (code.equalsIgnoreCase("mallA")) {
            return "文具/乐器/棋类";
        } else if (code.equalsIgnoreCase("mallB")) {
            return "STEM创客/益智玩具";
        } else if (code.equalsIgnoreCase("mallC")) {
            return "教辅教材/小说文学";
        } else if (code.equalsIgnoreCase("mallD")) {
            return "名人传记/成功励志";
        } else if (code.equalsIgnoreCase("mallE")) {
            return "智能硬件/健康防护";
        } else if (code.equalsIgnoreCase("postIndexA")) {
            return "晒才艺";
        } else if (code.equalsIgnoreCase("postIndexB")) {
            return "STEM创客空间";
        } else if (code.equalsIgnoreCase("postIndexC")) {
            return "读书/学霸";
        } else if (code.equalsIgnoreCase("postIndexD")) {
            return "演讲口才";
        } else if (code.equalsIgnoreCase("postIndexE")) {
            return "安全健康";
        }


        return "其他";
    }

    static class MallDailyReportDTO {
        //总访问人数
        private int count;
        //时段访问
        private Map<String, Integer> loginMap = new HashMap<String, Integer>();
        //板块map
        private Map<String, Integer> bankuaiMap = new HashMap<String, Integer>();
        //地域map
        private Map<ObjectId, Integer> areaMap = new HashMap<ObjectId, Integer>();

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public Map<String, Integer> getLoginMap() {
            return loginMap;
        }

        public void setLoginMap(Map<String, Integer> loginMap) {
            this.loginMap = loginMap;
        }

        public Map<String, Integer> getBankuaiMap() {
            return bankuaiMap;
        }

        public void setBankuaiMap(Map<String, Integer> bankuaiMap) {
            this.bankuaiMap = bankuaiMap;
        }

        public Map<ObjectId, Integer> getAreaMap() {
            return areaMap;
        }

        public void setAreaMap(Map<ObjectId, Integer> areaMap) {
            this.areaMap = areaMap;
        }


        public void increaseCount() {
            this.count = this.count + 1;
        }


        public void increaseLoginMap(String time) {
            if (this.loginMap.containsKey(time)) {
                int c = this.loginMap.get(time);
                this.loginMap.put(time, c + 1);
            } else {
                this.loginMap.put(time, 1);
            }
        }


        public void increaseBankuaiMap(String bankuai) {
            if (this.bankuaiMap.containsKey(bankuai)) {
                int c = this.bankuaiMap.get(bankuai);
                this.bankuaiMap.put(bankuai, c + 1);
            } else {
                this.bankuaiMap.put(bankuai, 1);
            }
        }


        public void increaseAreaMap(ObjectId area) {
            if (this.areaMap.containsKey(area)) {
                int c = this.areaMap.get(area);
                this.areaMap.put(area, c + 1);
            } else {
                this.areaMap.put(area, 1);
            }
        }

    }
}
