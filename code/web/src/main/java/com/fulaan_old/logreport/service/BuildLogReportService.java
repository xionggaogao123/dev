package com.fulaan_old.logreport.service;

import com.db.emailmanage.EmailManageDao;
import com.db.forum.FLogDao;
import com.db.microblog.MicroBlogDao;
import com.db.user.UserDao;
import com.fulaan.log.service.LogService;
import com.fulaan_old.logreport.dto.CountDTO;
import com.fulaan_old.logreport.dto.LogReportDTO;
import com.fulaan_old.school.SchoolService;
import com.fulaan_old.utils.ExportUtil;
import com.fulaan_old.utils.pojo.KeyValue;
import com.mongodb.BasicDBObject;
import com.pojo.app.RegionEntry;
import com.pojo.emailmanage.EmailManageEntry;
import com.pojo.forum.FLogEntry;
import com.pojo.lesson.LessonType;
import com.pojo.log.LogEntry;
import com.pojo.log.LogType;
import com.pojo.microblog.MicroBlogEntry;
import com.pojo.school.SchoolEntry;
import com.pojo.user.UserEntry;
import com.pojo.user.UserRole;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.Region;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by guojing on 2016/5/18.
 */
@Service
public class BuildLogReportService {

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private LogService logService;

    private MicroBlogDao microBlogDao = new MicroBlogDao();

    private EmailManageDao emailManageDao = new EmailManageDao();


    private FLogDao fLogDao = new FLogDao();

    private UserDao userDao = new UserDao();

    DateTimeUtils time = new DateTimeUtils();

    private int section;

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public void buildDailyLogReport(HSSFWorkbook wb) {
        Date prevDay = time.getPrevDay(new Date(), -1);
        String prevDayStr = time.getDateToStrTime(prevDay);

        long dateStart = time.getDayMinTime(prevDay.getTime());
        long dateEnd = time.getDayMaxTime(prevDay.getTime());

        List<LogReportDTO> logReportList = getLogReportDataProcess(dateStart, dateEnd);
        String titleName = prevDayStr + "用户使用情况统计-日报";
        sheetAddData(wb, titleName, logReportList);
    }

    public void buildWeekLogReport(HSSFWorkbook wb) {
        int week = time.somedayIsWeekDay(new Date());
        if (week == 1) {
            try {
                Date prevDay = time.getPrevDay(new Date(), -7);
                String prevDayStr = time.getDateToStrTime(prevDay);
                Long[] dates = time.getWeekMinAndMaxTime(prevDayStr);

                long dateStart = dates[0];
                long dateEnd = dates[1];
                List<LogReportDTO> logReportList = getLogReportDataProcess(dateStart, dateEnd);
                String titleName = "上周用户使用情况统计-周报";
                sheetAddData(wb, titleName, logReportList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void buildMonthLogReport(HSSFWorkbook wb) {

        int day = time.getDay();
        if (day == 1) {
            try {
                Date prevDay = time.getPrevDay(new Date(), -1);

                String prevDayStr = time.getDateToStrTime(prevDay);

                String yearMonthStr = time.getYearMonth(prevDay);

                Long[] dates = time.getMonthMinAndMaxTime(prevDayStr);

                long dateStart = dates[0];
                long dateEnd = dates[1];
                List<LogReportDTO> logReportList = getLogReportDataProcess(dateStart, dateEnd);
                String titleName = yearMonthStr + "用户使用情况统计-月报";
                sheetAddData(wb, titleName, logReportList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sheetAddData(HSSFWorkbook wb, String titleName, List<LogReportDTO> logReportList) {
        //生成一个sheet1
        HSSFSheet sheet = createSheet(wb, titleName);
        //数据行的字体，颜色，填充，样式
        HSSFCellStyle cellStyle = ExportUtil.publicCellStyle(wb, "黑体", "BLACK", (short) 10, "NORMAL", "", "", "CENTER", "CENTER", "THIN", "THIN", "THIN", "THIN", true);

        //数据行的字体，颜色，填充，样式
        HSSFCellStyle cellStyle2 = ExportUtil.publicCellStyle(wb, "黑体", "BLACK", (short) 12, "BOLD", "", "", "CENTER", "CENTER", "THIN", "THIN", "THIN", "THIN", true);

        HSSFRow row = null;
        HSSFCell cell = null;
        int i = 0;
        int rowNum = 0;
        CountDTO totalCount = new CountDTO();
        for (LogReportDTO dto : logReportList) {
            String schoolName = dto.getSchoolName();
            List<CountDTO> countList = dto.getCountList();
            int size = countList.size();
            row = sheet.createRow(i + 2);
            sheet.addMergedRegion(new Region(i + 2, (short) 0, i + 2 + size - 1, (short) 0));// 起始行号, 起始列号，结束行号，结束列号
            sheet.addMergedRegion(new Region(i + 2, (short) 1, i + 2 + size - 1, (short) 1));// 起始行号, 起始列号，结束行号，结束列号
            rowNum++;
            for (int j = 0; j < size; j++) {
                if (j != 0) {
                    row = sheet.createRow(i + 2 + j);
                }
                CountDTO countDTO = countList.get(j);
                cell = row.createCell(0);
                cell.setCellValue(rowNum);
                cell.setCellStyle(cellStyle);

                cell = row.createCell(1);
                cell.setCellValue(schoolName);
                cell.setCellStyle(cellStyle2);

                cell = row.createCell(2);
                cell.setCellStyle(cellStyle);
                if ("All".equals(countDTO.getTimePoint())) {
                    totalCount.setTotalLoginCount(totalCount.getTotalLoginCount() + countDTO.getTotalLoginCount());
                    totalCount.setTotalLoginPeopleCount(totalCount.getTotalLoginPeopleCount() + countDTO.getTotalLoginPeopleCount());

                    totalCount.setTeaLoginCount(totalCount.getTeaLoginCount() + countDTO.getTeaLoginCount());
                    totalCount.setTeaLoginPeopleCount(totalCount.getTeaLoginPeopleCount() + countDTO.getTeaLoginPeopleCount());

                    totalCount.setStuLoginCount(totalCount.getStuLoginCount() + countDTO.getStuLoginCount());
                    totalCount.setStuLoginPeopleCount(totalCount.getStuLoginPeopleCount() + countDTO.getStuLoginPeopleCount());

                    totalCount.setParLoginCount(totalCount.getParLoginCount() + countDTO.getParLoginCount());
                    totalCount.setParLoginPeopleCount(totalCount.getParLoginPeopleCount() + countDTO.getParLoginPeopleCount());

                    totalCount.setMicroCapusPostCount(totalCount.getMicroCapusPostCount() + countDTO.getMicroCapusPostCount());

                    totalCount.setMicroHomePostCount(totalCount.getMicroHomePostCount() + countDTO.getMicroHomePostCount());

                    totalCount.setNoticePostCount(totalCount.getNoticePostCount() + countDTO.getNoticePostCount());

                    totalCount.setNoticeReadCount(totalCount.getNoticeReadCount() + countDTO.getNoticeReadCount());

                    totalCount.setHomeworkUploadCount(totalCount.getHomeworkUploadCount() + countDTO.getHomeworkUploadCount());

                    totalCount.setBackUploadCount(totalCount.getBackUploadCount() + countDTO.getBackUploadCount());

                    cell.setCellValue("小计");
                } else {
                    cell.setCellValue(countDTO.getTimePoint());
                }

                cell = row.createCell(3);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(countDTO.getTotalLoginPeopleCount());

                cell = row.createCell(4);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(countDTO.getTotalLoginCount());

                cell = row.createCell(5);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(countDTO.getTeaLoginPeopleCount());

                cell = row.createCell(6);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(countDTO.getTeaLoginCount());

                cell = row.createCell(7);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(countDTO.getStuLoginPeopleCount());

                cell = row.createCell(8);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(countDTO.getStuLoginCount());

                cell = row.createCell(9);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(countDTO.getParLoginPeopleCount());

                cell = row.createCell(10);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(countDTO.getParLoginCount());

                cell = row.createCell(11);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(countDTO.getMicroCapusPostCount());

                cell = row.createCell(12);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(countDTO.getMicroHomePostCount());

                cell = row.createCell(13);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(countDTO.getNoticePostCount());

                cell = row.createCell(14);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(countDTO.getNoticeReadCount());

                cell = row.createCell(15);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(countDTO.getHomeworkUploadCount());

                cell = row.createCell(16);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(countDTO.getBackUploadCount());
            }
            i += size;
        }

        row = sheet.createRow(i + 2);
        row.setHeight((short) (357 * 2));
        sheet.addMergedRegion(new Region(i + 2, (short) 0, i + 2, (short) 2));// 起始行号, 起始列号，结束行号，结束列号

        //数据行的字体，颜色，填充，样式
        HSSFCellStyle endStyle = ExportUtil.publicCellStyle(wb, "黑体", "RED", (short) 14, "BOLD", "", "", "CENTER", "CENTER", "THIN", "THIN", "THIN", "THIN", true);
        cell = row.createCell(0);
        cell.setCellStyle(endStyle);
        cell.setCellValue("总计");
        cell = row.createCell(1);
        cell.setCellStyle(endStyle);
        cell.setCellValue("总计");
        cell = row.createCell(2);
        cell.setCellStyle(endStyle);
        cell.setCellValue("总计");

        cell = row.createCell(3);
        cell.setCellStyle(cellStyle2);
        cell.setCellValue(totalCount.getTotalLoginPeopleCount());

        cell = row.createCell(4);
        cell.setCellStyle(cellStyle2);
        cell.setCellValue(totalCount.getTotalLoginCount());

        cell = row.createCell(5);
        cell.setCellStyle(cellStyle2);
        cell.setCellValue(totalCount.getTeaLoginPeopleCount());

        cell = row.createCell(6);
        cell.setCellStyle(cellStyle2);
        cell.setCellValue(totalCount.getTeaLoginCount());

        cell = row.createCell(7);
        cell.setCellStyle(cellStyle2);
        cell.setCellValue(totalCount.getStuLoginPeopleCount());

        cell = row.createCell(8);
        cell.setCellStyle(cellStyle2);
        cell.setCellValue(totalCount.getStuLoginCount());

        cell = row.createCell(9);
        cell.setCellStyle(cellStyle2);
        cell.setCellValue(totalCount.getParLoginPeopleCount());

        cell = row.createCell(10);
        cell.setCellStyle(cellStyle2);
        cell.setCellValue(totalCount.getParLoginCount());

        cell = row.createCell(11);
        cell.setCellStyle(cellStyle2);
        cell.setCellValue(totalCount.getMicroCapusPostCount());

        cell = row.createCell(12);
        cell.setCellStyle(cellStyle2);
        cell.setCellValue(totalCount.getMicroHomePostCount());

        cell = row.createCell(13);
        cell.setCellStyle(cellStyle2);
        cell.setCellValue(totalCount.getNoticePostCount());

        cell = row.createCell(14);
        cell.setCellStyle(cellStyle2);
        cell.setCellValue(totalCount.getNoticeReadCount());

        cell = row.createCell(15);
        cell.setCellStyle(cellStyle2);
        cell.setCellValue(totalCount.getHomeworkUploadCount());

        cell = row.createCell(16);
        cell.setCellStyle(cellStyle2);
        cell.setCellValue(totalCount.getBackUploadCount());
    }


    public HSSFSheet createSheet(HSSFWorkbook wb, String titleName) {
        List<KeyValue> titleList = new ArrayList<KeyValue>();
        KeyValue keyValue1 = new KeyValue(4, "序号");
        titleList.add(keyValue1);
        KeyValue keyValue2 = new KeyValue(30, "学校名称");
        titleList.add(keyValue2);
        KeyValue keyValue3 = new KeyValue(8, "时间点");
        titleList.add(keyValue3);
        KeyValue keyValue4 = new KeyValue(12, "总登录人数");
        titleList.add(keyValue4);
        KeyValue keyValue5 = new KeyValue(12, "总登录次数");
        titleList.add(keyValue5);
        KeyValue keyValue6 = new KeyValue(14, "教师登录人数");
        titleList.add(keyValue6);
        KeyValue keyValue7 = new KeyValue(14, "教师登录次数");
        titleList.add(keyValue7);
        KeyValue keyValue8 = new KeyValue(14, "学生登录人数");
        titleList.add(keyValue8);
        KeyValue keyValue9 = new KeyValue(14, "学生登录次数");
        titleList.add(keyValue9);
        KeyValue keyValue10 = new KeyValue(14, "家长登录人数");
        titleList.add(keyValue10);
        KeyValue keyValue11 = new KeyValue(14, "家长登录次数");
        titleList.add(keyValue11);

        KeyValue keyValue12 = new KeyValue(14, "微校园发帖数");
        titleList.add(keyValue12);
        KeyValue keyValue13 = new KeyValue(14, "微家园发帖数");
        titleList.add(keyValue13);
        KeyValue keyValue14 = new KeyValue(12, "通知发布数");
        titleList.add(keyValue14);
        KeyValue keyValue15 = new KeyValue(12, "通知阅读数");
        titleList.add(keyValue15);
        KeyValue keyValue16 = new KeyValue(12, "作业上传数");
        titleList.add(keyValue16);
        KeyValue keyValue17 = new KeyValue(12, "备课上传数");
        titleList.add(keyValue17);


        int titleLength = titleList.size();

        HSSFSheet sheet = wb.createSheet(titleName);

        //设置一级表头样式字体，颜色，填充，样式
        HSSFCellStyle titleStyle = ExportUtil.publicCellStyle(wb, "宋体", "BLACK", (short) 16, "BOLD", "FOREGROUND", "GREY_25_PERCENT", "CENTER", "CENTER", "THIN", "THIN", "THIN", "THIN", false);

        //标题字段行的字体，颜色，填充，样式
        HSSFCellStyle style = ExportUtil.publicCellStyle(wb, "黑体", "BLACK", (short) 12, "BOLD", "FOREGROUND", "GREY_25_PERCENT", "CENTER", "CENTER", "THIN", "THIN", "THIN", "THIN", false);

        //为sheet1生成第一行，用于放表头信息
        HSSFRow titleRow = sheet.createRow(0);
        HSSFCell titleCell = null;
        sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) (titleLength - 1)));// 起始行号, 起始列号，结束行号，结束列号

        HSSFRow row = sheet.createRow(1);
        HSSFCell cell = null;
        for (int i = 0; i < titleLength; i++) {
            titleCell = titleRow.createCell(i);
            titleRow.setHeight((short) (357 * 2));
            titleCell.setCellStyle(titleStyle);
            titleCell.setCellValue(titleName);

            KeyValue keyValue = titleList.get(i);
            cell = row.createCell(i);
            sheet.setColumnWidth(i, 357 * keyValue.getKey());
            cell.setCellValue(keyValue.getValue());
            cell.setCellStyle(style);
        }
        return sheet;
    }


    BasicDBObject schoolFields = new BasicDBObject("nm", Constant.ONE);

    BasicDBObject logFields = new BasicDBObject("sid", Constant.ONE)
            .append("ui", Constant.ONE)
            .append("ur", Constant.ONE)
            .append("at", Constant.ONE);

    BasicDBObject microFields = new BasicDBObject("si", Constant.ONE)
            .append("bt", Constant.ONE)
            .append("pbt", Constant.ONE);

    BasicDBObject noticeFields = new BasicDBObject("trus", Constant.ONE);

    BasicDBObject homeWorkFields = new BasicDBObject("nm", Constant.ONE);

    BasicDBObject lessonFields = new BasicDBObject("nm", Constant.ONE);

    private List<LogReportDTO> getLogReportDataProcess(long dateStart, long dateEnd) {
        List<SchoolEntry> schoolList = schoolService.getAllSchoolEntryList(schoolFields);
        Map<ObjectId, List<LogEntry>> logMap = logService.getLogEntryMapByParam(LogType.CLICK_LOGIN.getCode(), dateStart, dateEnd, logFields);
        Map<ObjectId, List<MicroBlogEntry>> microMap = microBlogDao.getMicroBlogEntryListByParam(dateStart, dateEnd, microFields);

        //时间点
        List<String> timePoints = getAllTimePoints();

        List<LogReportDTO> logReports = new ArrayList<LogReportDTO>();
        for (SchoolEntry scEntry : schoolList) {
            //总登录人数
            Map<String, List<ObjectId>> tlpcMap = new HashMap<String, List<ObjectId>>();
            //总登录次数
            Map<String, Integer> tlcMap = new HashMap<String, Integer>();
            //教师登录人数
            Map<String, List<ObjectId>> telpcMap = new HashMap<String, List<ObjectId>>();
            //教师登录次数
            Map<String, Integer> telcMap = new HashMap<String, Integer>();
            //学生登录人数
            Map<String, List<ObjectId>> slpcMap = new HashMap<String, List<ObjectId>>();
            //学生登录次数
            Map<String, Integer> slcMap = new HashMap<String, Integer>();
            //家长登录人数
            Map<String, List<ObjectId>> plpcMap = new HashMap<String, List<ObjectId>>();
            //家长登录次数
            Map<String, Integer> plcMap = new HashMap<String, Integer>();
            //微校园发帖数
            Map<String, Integer> mcpcMap = new HashMap<String, Integer>();
            //微家园发帖数
            Map<String, Integer> mhpcMap = new HashMap<String, Integer>();
            //通知发布数
            Map<String, Integer> npcMap = new HashMap<String, Integer>();
            //通知阅读数
            Map<String, Integer> nrcMap = new HashMap<String, Integer>();
            //作业上传数
            Map<String, Integer> hucMap = new HashMap<String, Integer>();
            //备课上传数
            Map<String, Integer> bucMap = new HashMap<String, Integer>();

//            Map<String,List<ObjectId>> userMap = manageCountService.getRoleUserIdBySchoolId(scEntry.getID().toString());
//            List<ObjectId> teaIds=userMap.get("teaIds");
            //List<ObjectId> stuIds=userMap.get("stuIds");
            //List<ObjectId> parIds=userMap.get("parIds");
            ObjectId dslId = new ObjectId(DateTimeUtils.longToDate(dateStart, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
            ObjectId delId = new ObjectId(DateTimeUtils.longToDate(dateEnd, DateTimeUtils.DATE_YYYY_MM_DD_HH_MM_SS_H));
            //获取统计对象通知发布信息
//            List<NoticeEntry> noticeList=noticeService.getNoticePublishByParamList(teaIds, dslId, delId, 0, 0, noticeFields, "");

//            List<HomeWorkEntry> homeworkList=homeWorkService.getHomeworkUploadByParamList(teaIds, dslId, delId, 0, 0, homeWorkFields, "");

            int type = LessonType.BACKUP_LESSON.getType();
//            List<DirEntry> dirList =dirService.getDirEntryList(teaIds, new BasicDBObject(Constant.ID, Constant.ONE).append("ow", Constant.ONE), type);
            List<ObjectId> dirids = new ArrayList<ObjectId>();
//            for(DirEntry dirEntry : dirList){
//                dirids.add(dirEntry.getID());
//            }
            //获取用户备课空间信息
//            List<LessonEntry> lessonList=lessonService.getLessonEntryByParamList(dirids, type, dateStart, dateEnd, 0, 0, lessonFields, "");

            List<LogEntry> logList = logMap.get(scEntry.getID());

            List<MicroBlogEntry> mbList = microMap.get(scEntry.getID());

            LogReportDTO logReportDTO = new LogReportDTO();
            logReportDTO.setSchoolName(scEntry.getName());
            List<CountDTO> countList = new ArrayList<CountDTO>();

//            if(lessonList!=null&&lessonList.size()>0) {
//                for (LessonEntry lEntry : lessonList) {
//                    String actionHour = time.getLongToStrTimeFour(lEntry.getID().getTime());
//                    String timePoint = getTimePoint(actionHour);
//                    logCount(timePoint, bucMap);
//                }
//            }

//            if(homeworkList!=null&&homeworkList.size()>0) {
//                for (HomeWorkEntry hkEntry : homeworkList) {
//                    String actionHour = time.getLongToStrTimeFour(hkEntry.getID().getTime());
//                    String timePoint = getTimePoint(actionHour);
//                    logCount(timePoint, hucMap);
//                }
//            }
            List<ObjectId> noticeIds = new ArrayList<ObjectId>();
//            if(noticeList!=null&&noticeList.size()>0) {
//                for (NoticeEntry nEntry : noticeList) {
//                    noticeIds.add(nEntry.getID());
//                    String actionHour = time.getLongToStrTimeFour(nEntry.getID().getTime());
//                    String timePoint = getTimePoint(actionHour);
//                    logCount(timePoint, npcMap);
//                }
//            }

//            if(noticeIds.size()>0){
//                List<NoticeReadsEntry> readList = noticeService.getNoticeReadsEntryListByParam(noticeIds, dslId, delId,noticeFields);
//                if(readList!=null&&readList.size()>0) {
//                    for (NoticeReadsEntry nrEntry : readList) {
//                        String actionHour = time.getLongToStrTimeFour(nrEntry.getID().getTime());
//                        String timePoint = getTimePoint(actionHour);
//                        logCount(timePoint, nrEntry.getTotalReadUser(), nrcMap);
//                    }
//                }
//            }


            if (logList != null && logList.size() > 0) {
                for (LogEntry logEntry : logList) {
                    String actionHour = time.getLongToStrTimeFour(logEntry.getActionTime());
                    String timePoint = getTimePoint(actionHour);
                    logCount(timePoint, tlcMap);
                    logPeopleCount(timePoint, logEntry.getUserId(), tlpcMap);
                    if (UserRole.isNotStudentAndParent(logEntry.getUserRole())) {
                        logCount(timePoint, telcMap);
                        logPeopleCount(timePoint, logEntry.getUserId(), telpcMap);
                    }
                    if (UserRole.isStudent(logEntry.getUserRole())) {
                        logCount(timePoint, slcMap);
                        logPeopleCount(timePoint, logEntry.getUserId(), slpcMap);
                    }
                    if (UserRole.isParent(logEntry.getUserRole())) {
                        logCount(timePoint, plcMap);
                        logPeopleCount(timePoint, logEntry.getUserId(), plpcMap);
                    }
                }
            }

            if (mbList != null && mbList.size() > 0) {
                for (MicroBlogEntry mbEntry : mbList) {
                    String actionHour = time.getLongToStrTimeFour(mbEntry.getPublishTime());
                    String timePoint = getTimePoint(actionHour);
                    //微校园
                    if (mbEntry.getBlogtype() == 1) {
                        logCount(timePoint, mcpcMap);
                    }
                    //微家园
                    if (mbEntry.getBlogtype() == 2) {
                        logCount(timePoint, mhpcMap);
                    }
                }
            }


            for (String str : timePoints) {
                CountDTO countDTO = new CountDTO();
                countDTO.setTimePoint(str);
                List<ObjectId> uidlist = null;
                Integer countInt = null;
                countInt = tlcMap.get(str);
                if (countInt == null) {
                    countDTO.setTotalLoginCount(0);
                } else {
                    countDTO.setTotalLoginCount(countInt);
                }
                uidlist = tlpcMap.get(str);
                if (uidlist == null) {
                    countDTO.setTotalLoginPeopleCount(0);
                } else {
                    countDTO.setTotalLoginPeopleCount(uidlist.size());
                }

                countInt = telcMap.get(str);
                if (countInt == null) {
                    countDTO.setTeaLoginCount(0);
                } else {
                    countDTO.setTeaLoginCount(countInt);
                }

                uidlist = telpcMap.get(str);
                if (uidlist == null) {
                    countDTO.setTeaLoginPeopleCount(0);
                } else {
                    countDTO.setTeaLoginPeopleCount(uidlist.size());
                }

                countInt = slcMap.get(str);
                if (countInt == null) {
                    countDTO.setStuLoginCount(0);
                } else {
                    countDTO.setStuLoginCount(countInt);
                }

                uidlist = slpcMap.get(str);
                if (uidlist == null) {
                    countDTO.setStuLoginPeopleCount(0);
                } else {
                    countDTO.setStuLoginPeopleCount(uidlist.size());
                }

                countInt = plcMap.get(str);
                if (countInt == null) {
                    countDTO.setParLoginCount(0);
                } else {
                    countDTO.setParLoginCount(countInt);
                }

                uidlist = plpcMap.get(str);
                if (uidlist == null) {
                    countDTO.setParLoginPeopleCount(0);
                } else {
                    countDTO.setParLoginPeopleCount(uidlist.size());
                }

                countInt = mcpcMap.get(str);
                if (countInt == null) {
                    countDTO.setMicroCapusPostCount(0);
                } else {
                    countDTO.setMicroCapusPostCount(countInt);
                }

                countInt = mhpcMap.get(str);
                if (countInt == null) {
                    countDTO.setMicroHomePostCount(0);
                } else {
                    countDTO.setMicroHomePostCount(countInt);
                }

                countInt = npcMap.get(str);
                if (countInt == null) {
                    countDTO.setNoticePostCount(0);
                } else {
                    countDTO.setNoticePostCount(countInt);
                }

                countInt = nrcMap.get(str);
                if (countInt == null) {
                    countDTO.setNoticeReadCount(0);
                } else {
                    countDTO.setNoticeReadCount(countInt);
                }

                countInt = hucMap.get(str);
                if (countInt == null) {
                    countDTO.setHomeworkUploadCount(0);
                } else {
                    countDTO.setHomeworkUploadCount(countInt);
                }

                countInt = bucMap.get(str);
                if (countInt == null) {
                    countDTO.setBackUploadCount(0);
                } else {
                    countDTO.setBackUploadCount(countInt);
                }
                countList.add(countDTO);
            }

            if (countList.size() > 0) {
                CountDTO total = new CountDTO();
                total.setTimePoint("All");
                for (CountDTO dto : countList) {
                    logReportDTO.setTotalCount(logReportDTO.getTotalCount() + dto.getTotalLoginCount());
                    logReportDTO.setTotalPeopleCount(logReportDTO.getTotalPeopleCount() + dto.getTotalLoginPeopleCount());

                    logReportDTO.setOtherTotalCount(logReportDTO.getOtherTotalCount() + dto.getMicroCapusPostCount() + dto.getNoticePostCount()
                            + dto.getNoticeReadCount() + dto.getHomeworkUploadCount() + dto.getBackUploadCount());

                    total.setTotalLoginCount(total.getTotalLoginCount() + dto.getTotalLoginCount());
                    total.setTotalLoginPeopleCount(total.getTotalLoginPeopleCount() + dto.getTotalLoginPeopleCount());

                    total.setTeaLoginCount(total.getTeaLoginCount() + dto.getTeaLoginCount());
                    total.setTeaLoginPeopleCount(total.getTeaLoginPeopleCount() + dto.getTeaLoginPeopleCount());

                    total.setStuLoginCount(total.getStuLoginCount() + dto.getStuLoginCount());
                    total.setStuLoginPeopleCount(total.getStuLoginPeopleCount() + dto.getStuLoginPeopleCount());

                    total.setParLoginCount(total.getParLoginCount() + dto.getParLoginCount());
                    total.setParLoginPeopleCount(total.getParLoginPeopleCount() + dto.getParLoginPeopleCount());

                    total.setMicroCapusPostCount(total.getMicroCapusPostCount() + dto.getMicroCapusPostCount());

                    total.setMicroHomePostCount(total.getMicroHomePostCount() + dto.getMicroHomePostCount());

                    total.setNoticePostCount(total.getNoticePostCount() + dto.getNoticePostCount());

                    total.setNoticeReadCount(total.getNoticeReadCount() + dto.getNoticeReadCount());

                    total.setHomeworkUploadCount(total.getHomeworkUploadCount() + dto.getHomeworkUploadCount());

                    total.setBackUploadCount(total.getBackUploadCount() + dto.getBackUploadCount());
                }
                countList.add(total);
            }
            logReportDTO.setCountList(countList);
            logReports.add(logReportDTO);
        }
        sortList(logReports);
        return logReports;
    }

    /**
     * 对list进行排序
     *
     * @param list
     */
    private void sortList(List<LogReportDTO> list) {
        Collections.sort(list, new Comparator<LogReportDTO>() {
            public int compare(LogReportDTO obj1, LogReportDTO obj2) {
                int flag = obj2.getTotalCount() - obj1.getTotalCount();
                if (flag == 0) {
                    return obj2.getOtherTotalCount() - obj1.getOtherTotalCount();
                } else {
                    return flag;
                }
            }
        });
    }

    private void logCount(String timePoint, int totalReadUser, Map<String, Integer> map) {
        Integer count = map.get(timePoint);
        if (count == null) {
            count = totalReadUser;
        } else {
            count += totalReadUser;
        }
        map.put(timePoint, count);
    }

    private void logPeopleCount(String timePoint, ObjectId userId, Map<String, List<ObjectId>> map) {
        List<ObjectId> list = map.get(timePoint);
        if (list == null) {
            list = new ArrayList<ObjectId>();
            list.add(userId);
        } else {
            if (!list.contains(userId)) {
                list.add(userId);
            }
        }
        map.put(timePoint, list);
    }

    private void logCount(String timePoint, Map<String, Integer> map) {
        Integer count = map.get(timePoint);
        if (count == null) {
            count = 1;
        } else {
            count += 1;
        }
        map.put(timePoint, count);
    }

    //Collections.sort(timePoints);
    private String getTimePoint(String actionHour) {
        String timePoint = "";
        if (!"".equals(actionHour)) {
            List<String> timeList = getTimeList();
            int space = (int) Math.ceil((double) 24 / getSection());
            for (int i = 0; i < timeList.size() - 1; ) {
                String timeOne = timeList.get(i);
                i = i + space;
                if (i > 24) {
                    i = 24;
                }
                String timeTwo = timeList.get(i);
                timePoint = actionHourCompareToTime(actionHour, timeOne, timeTwo);
                if (!"".equals(timePoint)) {
                    break;
                }
            }
        }
        return timePoint;
    }

    private String actionHourCompareToTime(String actionHour, String timeOne, String timeTwo) {
        String timePoint = "";
        if (!"".equals(actionHour)) {
            if (actionHour.compareTo(timeOne) >= 0 && actionHour.compareTo(timeTwo) < 0) {
                timePoint = timeOne + "-" + timeTwo + "点";
            }
        } else {
            timePoint = timeOne + "-" + timeTwo + "点";
        }
        return timePoint;
    }

    private List<String> getAllTimePoints() {
        List<String> timePoints = new ArrayList<String>();
        List<String> timeList = getTimeList();

        int space = (int) Math.ceil((double) 24 / getSection());

        String timePoint = "";

        for (int i = 0; i < timeList.size() - 1; ) {
            String timeOne = timeList.get(i);
            i = i + space;
            if (i > 24) {
                i = 24;
            }
            String timeTwo = timeList.get(i);
            timePoint = actionHourCompareToTime("", timeOne, timeTwo);
            timePoints.add(timePoint);
        }
        return timePoints;
    }

    private List<String> getTimeList() {
        List<String> timeList = new ArrayList<String>();
        timeList.add("00");
        timeList.add("01");
        timeList.add("02");
        timeList.add("03");
        timeList.add("04");
        timeList.add("05");
        timeList.add("06");

        timeList.add("07");
        timeList.add("08");
        timeList.add("09");
        timeList.add("10");
        timeList.add("11");
        timeList.add("12");

        timeList.add("13");
        timeList.add("14");
        timeList.add("15");
        timeList.add("16");
        timeList.add("17");
        timeList.add("18");

        timeList.add("19");
        timeList.add("20");
        timeList.add("21");
        timeList.add("22");
        timeList.add("23");
        timeList.add("24");
        return timeList;
    }

    public EmailManageEntry getEmailManageEntry(int type) {
        return emailManageDao.getEmailManageEntry(type);
    }

    /**
     * 分页查询当天的日志
     *
     * @param skip
     * @param limit
     * @return
     */
    public List<FLogEntry> getFLogEntrys(int skip, int limit) {
        return fLogDao.getFLogEntrys(skip, limit);
    }


    /**
     * 得到一个用户的地区ID
     *
     * @param userId
     * @return
     */
    public ObjectId getUserArea(ObjectId userId) {
        UserEntry ue = userDao.getUserEntry(userId, new BasicDBObject("si", 1));
        if (null != ue) {
            SchoolEntry se = schoolService.getSchoolEntry(ue.getSchoolID(), new BasicDBObject("ir", 1));
            if (null != se && null != se.getRegionId()) {
                return se.getRegionId();
            }
        }
        return null;
    }


    /**
     * 得到地区entry
     *
     * @param regionId
     * @return
     */
    public RegionEntry getRegionEntry(ObjectId regionId) {
        return schoolService.getRegionEntry(regionId);
    }
}
