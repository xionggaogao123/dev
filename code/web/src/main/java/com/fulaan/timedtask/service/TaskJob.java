package com.fulaan.timedtask.service;

import com.db.businessactivity.FieryActivityDao;
import com.db.ebusiness.EVoucherDao;
import com.db.educationbureau.EducationBureauDao;
import com.db.microblog.MicroBlogDao;
import com.db.school.SchoolDao;
import com.db.user.UserSchoolYearExperienceDao;
import com.fulaan.cache.CacheHandler;
import com.fulaan.homeschool.service.HomeSchoolService;
import com.fulaan.logreport.service.BuildLogReportService;
import com.fulaan.managecount.service.ManageCountService;
import com.fulaan.myschool.service.InterestClassService;
import com.fulaan.smartcard.service.AsWebService;
import com.fulaan.smartcard.service.KaoQinStateService;
import com.pojo.educationbureau.EducationBureauEntry;
import com.pojo.emailmanage.EmailManageEntry;
import com.pojo.school.SchoolEntry;
import com.sys.constants.Constant;
import com.sys.mails.MailUtils;
import com.sys.props.Resources;
import com.sys.utils.DateTimeUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.bson.types.ObjectId;
import org.json.JSONObject;
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
    private static final Logger cLogger =Logger.getLogger(TaskJob.class);
    @Autowired
    private ManageCountService manageCountService;

    @Autowired
    private BuildLogReportService buildLogReportService;

    @Autowired
    private KaoQinStateService kaoQinStateService;
    /**
     * 定时清空用户学年积分
     * @return
     */
    public void timedClearSchoolYearExpJob() {
        UserSchoolYearExperienceDao userSchoolYearExperienceDao=new UserSchoolYearExperienceDao();
        cLogger.info("\r\n");
        cLogger.info("\r\n");
        cLogger.info("\r\n");
        cLogger.info("******************************************************************************************************");
        cLogger.info("******************************************************************************************************");
        cLogger.info(DateTimeUtils.getChineseDate()+";清空学年积分");
        userSchoolYearExperienceDao.clearSchoolYearExp();
    }

    /**
     * 定时结束火热活动
     * @return
     */
    public void timedFieryActivityIsEnd() {
        FieryActivityDao fieryActivityDao=new FieryActivityDao();
        cLogger.info("\r\n");
        cLogger.info("\r\n");
        cLogger.info("******************************************************************************************************");
        cLogger.info("******************************************************************************************************");
        cLogger.info(DateTimeUtils.getChineseDate()+";定时检查火热活动是否结束");
        fieryActivityDao.FieryActivityIsEnd();
    }

    /**
     * 定时开启火热活动
     * @return
     */
    public void timedFieryActivityIsStart() {
        FieryActivityDao fieryActivityDao=new FieryActivityDao();
        cLogger.info("\r\n");
        cLogger.info("\r\n");
        cLogger.info("******************************************************************************************************");
        cLogger.info("******************************************************************************************************");
        cLogger.info(DateTimeUtils.getChineseDate()+";定时开启火热活动");
        fieryActivityDao.FieryActivityIsStart();
    }

    /**
     * 定时教育局管理统计
     * @return
     */
    public void timedEduSchoolsTotalData() {
        cLogger.info("\r\n");
        cLogger.info("\r\n");
        cLogger.info("******************************************************************************************************");
        cLogger.info("******************************************************************************************************");
        cLogger.info(DateTimeUtils.getChineseDate()+";定时教育局管理统计");
        try {
            EducationBureauDao educationBureauDao =new EducationBureauDao();
            List<EducationBureauEntry> EElist= educationBureauDao.selAllEducation();
            DateTimeUtils time=new DateTimeUtils();
            //获取当前日期
            String currDate=time.getCurrDate();
            String dateStart="";
            String params="";
            int gradeType=0;

            for(EducationBureauEntry item: EElist){
                if(item.getUserIds()!=null&&item.getUserIds().size()>0) {
                    dateStart=item.getSchoolCreateDate();
                    if(dateStart==null||"".equals(dateStart)){
                        dateStart="2014-09-01";
                    }
                    dateStart = handleTime(dateStart, 1);
                    String dateEnd = handleTime(currDate, 2);
                    params+=gradeType;
                    params+=dateStart;
                    params+=dateEnd;
                    params += item.getID().toString();
                    String key = CacheHandler.getKeyString(CacheHandler.CACHE_SCHOOL_MANAGER_KEY, params);

                    String userId = item.getUserIds().get(0).toString();
                    //查询用户统计数据
                    Map<String, Object> result = manageCountService.eduSchoolsTotalData(userId, gradeType, "", dateStart, dateEnd);
                    String jsonstr = JSONObject.valueToString(result);     //将集合解析为 json对象语句
                    CacheHandler.cache(key, jsonstr, Constant.SECONDS_IN_DAY);
                }
            }
        }catch (Exception ex) {
            cLogger.error("", ex);
        }
    }

    /**
     * 定时学校管理统计
     * @return
     */
    public void timedSchoolTotalData() {
        cLogger.info("\r\n");
        cLogger.info("\r\n");
        cLogger.info("******************************************************************************************************");
        cLogger.info("******************************************************************************************************");
        cLogger.info(DateTimeUtils.getChineseDate()+";定时学校管理统计");
        try {
            DateTimeUtils time=new DateTimeUtils();
            //获取当前日期
            String currDate=time.getCurrDate();

            SchoolDao schoolDao = new SchoolDao();

            List<SchoolEntry> list= schoolDao.getSchoolEntryListByRegionForEdu();
            for(SchoolEntry entry:list) {

                String schoolId = entry.getID().toString();

                String dateStart = handleTime(time.getLongToStrTime(new ObjectId(schoolId).getTime()), 1);

                String dateEnd = handleTime(currDate, 2);

                String params = schoolId + dateStart + dateEnd;

                String key = CacheHandler.getKeyString(CacheHandler.CACHE_SCHOOL_MANAGER_KEY, params);
                //查询用户统计数据
                Map<String, Object> result = manageCountService.schoolTotalData("", "", schoolId, dateStart, dateEnd);
                String jsonstr = JSONObject.valueToString(result);     //将集合解析为 json对象语句
                CacheHandler.cache(key, jsonstr, Constant.SECONDS_IN_DAY);
            }
        }catch (Exception ex) {
            cLogger.error("", ex);
        }
    }

    /**
     * 给日期字符串拼接时分秒
     * @param dateTime 日期
     * @param type 1：表示开始时间，2：表示结束时间
     * @return
     */
    private String handleTime(String dateTime, int type){
        if(type==1) {
            //判断查询条件的开始时间是否为空
            dateTime = dateTime == null ? "" : dateTime;
            if (!"".equals(dateTime)) {//不等于空时，拼接" 00:00:00"
                dateTime = dateTime + " 00:00:00";
            } else {//等于空时，设置默认时间
                dateTime = "2014-09-01 00:00:00";
            }
        }
        if(type==2){
            //判断查询条件的结束时间是否为空
            dateTime=dateTime==null?"":dateTime;
            if(!"".equals(dateTime)){//不等于空时，拼接" 23:59:59"
                dateTime=dateTime+" 23:59:59";
            }else{//等于空时，设置默认时间
                dateTime=DateTimeUtils.getCurrDate()+" 23:59:59";
            }
        }
        return dateTime;
    }

    /**
     * 定时检查过期抵用券
     * @return
     */
    public void checkVoucherExpiration() {
        EVoucherDao eVoucherDao = new EVoucherDao();
        cLogger.info("\r\n");
        cLogger.info("\r\n");
        cLogger.info("******************************************************************************************************");
        cLogger.info("******************************************************************************************************");
        cLogger.info(DateTimeUtils.getChineseDate()+";定时检查过期抵用券");
        eVoucherDao.checkVoucherExpiration();
    }

    /**
    * 定时开启新学期兴趣班选课
            * @return
    */
    public void timedSchoolNewInterestTerm() {
        cLogger.info("\r\n");
        cLogger.info("\r\n");
        cLogger.info("******************************************************************************************************");
        cLogger.info("******************************************************************************************************");
        cLogger.info(DateTimeUtils.getChineseDate()+";定时开启新学期兴趣班选课");
        InterestClassService interestClassService = new InterestClassService();
        interestClassService.timedNewTerm();
    }

    /**
     * 定时发送日志报表邮件
     * @return
     */
    public void timedSendLogReportEmail() throws Exception {
        int type = 1;
        EmailManageEntry emailManage = buildLogReportService.getEmailManageEntry(type);
        if(emailManage!=null) {
            cLogger.info("\r\n");
            cLogger.info("\r\n");
            cLogger.info("******************************************************************************************************");
            cLogger.info("******************************************************************************************************");
            cLogger.info(DateTimeUtils.getChineseDate()+";开始定时发送日志报表邮件");
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
     * @return
     */
    private String getServerIp(){
        String SERVER_IP="";
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
        }finally {
            return SERVER_IP;
        }
    }

    /**
     * 定时检查主题帖
     * @return
     */
    public void checkMicroBlog() {
        MicroBlogDao microBlogDao = new MicroBlogDao();
        cLogger.info("\r\n");
        cLogger.info("\r\n");
        cLogger.info("******************************************************************************************************");
        cLogger.info("******************************************************************************************************");
        cLogger.info(DateTimeUtils.getChineseDate()+";定时检查主题帖");
        microBlogDao.updateZhuTi(HomeSchoolService.getDateBefore(new Date(), 7));
    }

    /**
     * 定时考勤
     * @return
     */
    public void  timedUploadKaoQinState() {
        cLogger.info("\r\n");
        cLogger.info("\r\n");
        cLogger.info("******************************************************************************************************");
        cLogger.info("******************************************************************************************************");
        cLogger.info(DateTimeUtils.getChineseDate()+";定时考勤");

        AsWebService test = new AsWebService();
        //test.getAccount();
        //test.getUserKaoQin();
        //test.getDoorData();
        //test.getTransData();
        DateTimeUtils time=new DateTimeUtils();
        Date prevDay=time.getPrevDay(new Date(),-1);
        String prevDayStr = time.getDateToStrTime(prevDay);
        String currDayStr = time.getCurrDate();
        test.createUserKaoQinData(prevDayStr,currDayStr);
        test.createUserDoorData(prevDayStr,currDayStr);
        //kaoQinStateService.updateKaoQinState(new ObjectId("55934c14f6f28b7261c19c62"));;
    }

}
