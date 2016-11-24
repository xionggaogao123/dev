package com.fulaan.overtime.service;

import com.db.duty.DutyDao;
import com.db.overtime.OverTimeDao;
import com.db.user.UserDao;
import com.fulaan.duty.dto.DutyModelDTO;
import com.fulaan.overtime.dto.MyOverTimeDTO;
import com.fulaan.overtime.dto.OverTimeDTO;
import com.fulaan.utils.LastDayOfMonth;
import com.fulaan.utils.WeekUtil;
import com.mongodb.BasicDBObject;
import com.pojo.duty.DutySetEntry;
import com.pojo.lesson.LessonWare;
import com.pojo.overtime.OverTimeEntry;
import com.pojo.overtime.OverTimeModelEntry;
import com.pojo.user.UserEntry;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wang_xinxin on 2016/7/13.
 */

@Service
public class OverTimeService {

    private OverTimeDao overTimeDao = new OverTimeDao();

    private UserDao userDao = new UserDao();
    private DutyDao dutyDao = new DutyDao();


    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd EEE");

    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd");

    /**
     *
     * @param jbUserId
     * @param date
     * @param startTime
     * @param endTime
     * @param cause
     * @param shUserId
     */
    public void addJiaBanInfo(String overTimeId,ObjectId schoolId,ObjectId userId,String jbUserId, String date,
                              String startTime, String endTime, String cause, String shUserId,Map map) {
        //检查时间冲突
        Date stAry = DateTimeUtils.stringToDate(startTime,DateTimeUtils.DATE_HH_MM);
        Date etAry = DateTimeUtils.stringToDate(endTime, DateTimeUtils.DATE_HH_MM);
        List<OverTimeEntry> overTimeEntryList = overTimeDao.selOverTimeByDate(schoolId,userId,DateTimeUtils.getStrToLongTime(date,DateTimeUtils.DATE_YYYY_MM_DD_N),overTimeId);
        boolean checkFlag = false;
        if (overTimeEntryList!=null && overTimeEntryList.size()!=0) {
            for(OverTimeEntry overTimeEntry : overTimeEntryList) {
                Date stAry2 = DateTimeUtils.stringToDate(overTimeEntry.getStartTime(),DateTimeUtils.DATE_HH_MM);
                Date etAry2 = DateTimeUtils.stringToDate(overTimeEntry.getEndTime(),DateTimeUtils.DATE_HH_MM);
                if ((stAry2.getTime()<=stAry.getTime() && stAry.getTime()<= etAry2.getTime())||(stAry2.getTime()<=etAry.getTime() && etAry.getTime()<= etAry2.getTime())) {
                    checkFlag = true;
                    break;
                }
            }
        }
       if (!checkFlag) {
           if (StringUtils.isEmpty(overTimeId)) {
               overTimeDao.addJiaBanInfo(new OverTimeEntry(schoolId,userId,new ObjectId(jbUserId), DateTimeUtils.getStrToLongTime(date,DateTimeUtils.DATE_YYYY_MM_DD_N),
                       startTime,endTime,cause,new ObjectId(shUserId),0d,"",new ArrayList<LessonWare>()));
           } else {
               overTimeDao.updateJiaBanInfo(new ObjectId(overTimeId),new ObjectId(jbUserId),DateTimeUtils.getStrToLongTime(date,DateTimeUtils.DATE_YYYY_MM_DD_N),startTime,endTime,cause,new ObjectId(shUserId));
           }
           map.put("check",true);
       } else {
           map.put("check",false);
       }


    }

    /**
     *
     * @param startDate
     * @param endDate
     * @param name
     * @param userId
     * @param schoolId
     * @return
     */
    public List<OverTimeDTO> selJiaBanList(String startDate, String endDate, String name,ObjectId userId,ObjectId schoolId,int type,int index) {
        long stime = 0;
        long etime = 0;
        if (!StringUtils.isEmpty(startDate)) {
            String sdate = startDate + " " +"00:00";
            stime = DateTimeUtils.getStrToLongTime(sdate,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM);
        }
        if (!StringUtils.isEmpty(endDate)) {
            String edate = endDate + " " +"23:59";
            etime = DateTimeUtils.getStrToLongTime(edate,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM);
        }
        List<ObjectId> users = new ArrayList<ObjectId>();
        Map<ObjectId,UserEntry> userEntryMap = new HashMap<ObjectId, UserEntry>();
        List<UserEntry> userEntryList = userDao.searchUsersWithSchool(name, schoolId,new BasicDBObject("nm",1));
        if (userEntryList!=null && userEntryList.size()!=0) {
            for (UserEntry userEntry : userEntryList) {
                users.add(userEntry.getID());
                userEntryMap.put(userEntry.getID(),userEntry);
            }
        }
        List<OverTimeDTO> overTimeDTOs = new ArrayList<OverTimeDTO>();
        List<OverTimeEntry> overTimeEntryList = overTimeDao.selJiaBanList(stime,etime,users,userId,type,index);
        if (overTimeEntryList!=null && overTimeEntryList.size()!=0) {
            for (OverTimeEntry overTimeEntry : overTimeEntryList) {
                OverTimeDTO overTimeDTO = new OverTimeDTO(overTimeEntry);
                overTimeDTO.setJbUserName(userEntryMap.get(overTimeEntry.getJbUserId()).getUserName());
                String time = "";
                String st=overTimeEntry.getStartTime();
                String et = overTimeEntry.getEndTime();
                DecimalFormat df = new DecimalFormat("#.00");
                int se = Integer.valueOf(et.split(":")[0])-Integer.valueOf(st.split(":")[0]);
                if (Integer.valueOf(st.split(":")[1])>Integer.valueOf(et.split(":")[1])) {
                    double a = (double)(60+Integer.valueOf(et.split(":")[1])-Integer.valueOf(st.split(":")[1]));
                    double b = 0;
                    if (a!=0) {
                        b = a/60;
                    }
                    time = String.valueOf(se-1+df.format(b));
                } else {
                    double a = (double)(Integer.valueOf(et.split(":")[1])-Integer.valueOf(st.split(":")[1]));
                    double b = 0;
                    if (a!=0) {
                        b = a/60d;
                    }
                    time = String.valueOf(se+df.format(b));
                }
                overTimeDTO.setTimes(time);
                overTimeDTOs.add(overTimeDTO);
            }
        }
        return overTimeDTOs;
    }

    /**
     *
     * @param overTimeId
     */
    public void delJiaBanInfo(String overTimeId) {
        overTimeDao.delJiaBanInfo(new ObjectId(overTimeId));
    }

    public void submitJiaBan(String overTimeId,int type) {
        overTimeDao.submitJiaBan(new ObjectId(overTimeId), type);
    }

    /**
     *
     * @param overTimeId
     * @param map
     */
    public void selSingleOverTime(String overTimeId,Map<String,Object> map) {
        OverTimeEntry overTimeEntry = overTimeDao.selSingleOverTime(new ObjectId(overTimeId));
        OverTimeDTO overTimeDTO = new OverTimeDTO(overTimeEntry);
        overTimeDTO.setSqUserName(userDao.getUserEntry(overTimeEntry.getApplicantUserId(),new BasicDBObject("nm",1)).getUserName());
        overTimeDTO.setJbUserName(userDao.getUserEntry(overTimeEntry.getJbUserId(),new BasicDBObject("nm",1)).getUserName());
        overTimeDTO.setShUserName(userDao.getUserEntry(overTimeEntry.getAuditUserId(),new BasicDBObject("nm",1)).getUserName());
        map.put("rows",overTimeDTO);
    }

    /**
     *
     * @param modelName
     * @param schoolId
     * @param userId
     * @param jbUserId
     * @param date
     * @param startTime
     * @param endTime
     * @param cause
     * @param shUserId
     */
    public void saveModelInfo(String modelId,String modelName, ObjectId schoolId, ObjectId userId, String jbUserId,
                              String date, String startTime, String endTime, String cause, String shUserId,Map map) {
        int count = overTimeDao.checkModelName(schoolId,modelName,userId,modelId);
        map.put("count", count);
        if (count==0) {
            if (StringUtils.isEmpty(modelId)) {
                overTimeDao.saveModelInfo(new OverTimeModelEntry(schoolId, modelName, userId, new ObjectId(jbUserId), DateTimeUtils.getStrToLongTime(date, DateTimeUtils.DATE_YYYY_MM_DD_N),
                        startTime, endTime, cause, new ObjectId(shUserId)));
            } else {
                overTimeDao.updateModelInfo(new ObjectId(modelId),modelName);
            }
        }


    }

    /**
     *
     * @param userId
     * @return
     */
    public List<DutyModelDTO> selJiaBanModelList(ObjectId userId) {
        List<OverTimeModelEntry> overTimeModelEntryList = overTimeDao.selJiaBanModelList(userId);
        List<DutyModelDTO> dutyModelDTOList = new ArrayList<DutyModelDTO>();
        if (overTimeModelEntryList!=null && overTimeModelEntryList.size()!=0) {
            for (OverTimeModelEntry overTimeModelEntry : overTimeModelEntryList) {
                DutyModelDTO dutyModelDTO  = new DutyModelDTO();
                dutyModelDTO.setId(overTimeModelEntry.getID().toString());
                dutyModelDTO.setName(overTimeModelEntry.getModelName());
                dutyModelDTO.setCause(overTimeModelEntry.getCause());
                dutyModelDTO.setJbUserId(overTimeModelEntry.getJbUserId().toString());
                dutyModelDTO.setShUserId(overTimeModelEntry.getAuditUserId().toString());
                dutyModelDTO.setStartTime(overTimeModelEntry.getStartTime());
                dutyModelDTO.setEndTime(overTimeModelEntry.getEndTime());
                dutyModelDTO.setDate(DateTimeUtils.convert(overTimeModelEntry.getDate(),DateTimeUtils.DATE_YYYY_MM_DD_N));
                dutyModelDTOList.add(dutyModelDTO);
            }
        }
        return dutyModelDTOList;
    }

    /**
     *
     * @param overTimeModelId
     */
    public void delOverTimeModel(String overTimeModelId) {
        overTimeDao.delOverTimeModel(new ObjectId(overTimeModelId));
    }

    /**
     *
     * @param overTimeId
     * @param salary
     */
    public void updateSalaryById(String overTimeId, double salary) {
        overTimeDao.updateSalaryById(new ObjectId(overTimeId),salary);
    }

    /**
     *
     * @param startDate
     * @param endDate
     * @param name
     * @param userId
     * @param schoolId
     * @param type
     * @param response
     */
    public void exportJiaBanList(String startDate, String endDate, String name, ObjectId userId, ObjectId schoolId, int type,int index, HttpServletResponse response) {
        List<OverTimeDTO> overTimeDTOList = selJiaBanList(startDate,endDate,name,userId,schoolId,type,index);
        HSSFWorkbook wb = new HSSFWorkbook();
        String tablename = "";
        if (type==1) {
            tablename = "加班申请表";
        } else if (type==2) {
            tablename = "加班薪酬表";
        } else if (type==3) {
            tablename = "加班审核";
        }
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet(tablename);
        //为sheet1生成第一行，用于放表头信息
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        if (type==1) {
            cell.setCellValue("教师姓名");
            cell = row.createCell(1);
            cell.setCellValue("日期");
            cell = row.createCell(2);
            cell.setCellValue("起止时间");
            cell = row.createCell(3);
            cell.setCellValue("加班事由");
        } else if (type==2) {
            cell.setCellValue("加班日期");
            cell = row.createCell(1);
            cell.setCellValue("姓名");
            cell = row.createCell(2);
            cell.setCellValue("加班时间");
            cell = row.createCell(3);
            cell.setCellValue("加班内容");
            cell = row.createCell(4);
            cell.setCellValue("加班时长(h)");
            cell = row.createCell(5);
            cell.setCellValue("加班薪酬(元)");
        } else if (type==3) {
            cell.setCellValue("教师姓名");
            cell = row.createCell(1);
            cell.setCellValue("日期");
            cell = row.createCell(2);
            cell.setCellValue("起止时间");
            cell = row.createCell(3);
            cell.setCellValue("加班事由");
            cell = row.createCell(4);
            cell.setCellValue("审核状态");
        }

        int page=0;
        if (overTimeDTOList!=null && overTimeDTOList.size()!=0) {
            for (OverTimeDTO overTimeDTO : overTimeDTOList) {
                page++;
                row = sheet.createRow(page);
                if (type==1) {
                    cell = row.createCell(0);
                    cell.setCellValue(overTimeDTO.getJbUserName());
                    cell = row.createCell(1);
                    cell.setCellValue(overTimeDTO.getDate());
                    cell = row.createCell(2);
                    cell.setCellValue(overTimeDTO.getTimeDuan());
                    cell = row.createCell(3);
                    cell.setCellValue(overTimeDTO.getCause());
                } else if (type==2) {
                    cell = row.createCell(0);
                    cell.setCellValue(overTimeDTO.getDate());
                    cell = row.createCell(1);
                    cell.setCellValue(overTimeDTO.getJbUserName());
                    cell = row.createCell(2);
                    cell.setCellValue(overTimeDTO.getTimeDuan());
                    cell = row.createCell(3);
                    cell.setCellValue(overTimeDTO.getCause());
                    cell = row.createCell(4);
                    cell.setCellValue(overTimeDTO.getTimes());
                    cell = row.createCell(5);
                    cell.setCellValue(overTimeDTO.getSalary());
                } else if (type==3) {
                    cell = row.createCell(0);
                    cell.setCellValue(overTimeDTO.getJbUserName());
                    cell = row.createCell(1);
                    cell.setCellValue(overTimeDTO.getDate());
                    cell = row.createCell(2);
                    cell.setCellValue(overTimeDTO.getTimeDuan());
                    cell = row.createCell(3);
                    cell.setCellValue(overTimeDTO.getCause());
                    cell = row.createCell(4);
                    String status = "";
                    if (overTimeDTO.getType()==1) {
                        status="未审核";
                    } else if (overTimeDTO.getType()==2) {
                        status="审核通过";
                    } else if (overTimeDTO.getType()==3) {
                        status="审核驳回";
                    }
                    cell.setCellValue(status);
                }
            }
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
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(tablename + ".xls", "UTF-8"));
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
     *
     * @param overTimeId
     * @param type
     * @param map
     */
    public void checkInOut(String overTimeId, int type, Map<String, Object> map,String ip,ObjectId schoolId) {
        OverTimeEntry overTimeEntry = overTimeDao.selSingleOverTime(new ObjectId(overTimeId));
//        DutySetEntry dutySetEntry = dutyDao.selDutySetInfo(schoolId);
//        if (!StringUtils.isEmpty(dutySetEntry.getIp()) && !ip.contains(dutySetEntry.getIp())) {
//            map.put("flag",false);
//            map.put("mesg","IP不在学校范围内，不能签到签退！");
//            return;
//        }
        if (type==1) {
//            if (!sdf2.format(overTimeEntry.getDate()).equals(sdf2.format(new Date()))) {
//                map.put("flag",false);
//                map.put("mesg","还没有到签到时间！");
//                return;
//            }
//            String date = sdf2.format(overTimeEntry.getDate()) + " "+overTimeEntry.getEndTime();
//            if (DateTimeUtils.compare_date(date,new Date())==-1) {
//                map.put("flag",false);
//                map.put("mesg","已超过了上班时间，不能签到！");
//                return;
//            }
        }
        overTimeDao.updateCheckTime(type, new ObjectId(overTimeId));
        map.put("flag",true);
    }

    /**
     *
     * @param overTimeId
     * @param log
     * @param realName
     */
    public void addOverTimeLog(String overTimeId, String log, String[] filepath,String[] realName) {
        List<BasicDBObject> basicDBObjects = new ArrayList<BasicDBObject>();
        if (filepath!=null && filepath.length!=0 && realName!=null && realName.length!=0) {
            for (int i=0;i<filepath.length;i++) {
                if (!StringUtils.isEmpty(realName[i])) {
                    String fileKey = filepath[i].substring(filepath[i].lastIndexOf('/') + 1);
                    String href = "/commonupload/doc/down.do?type=2&fileKey="+fileKey+"&fileName="+realName[i];
                    basicDBObjects.add(new LessonWare(href,realName[i],filepath[i]).getBaseEntry());
                }
            }
        }
        overTimeDao.addOverTimeLog(new ObjectId(overTimeId), log,basicDBObjects);
    }

    /**
     *
     * @param overTimeId
     * @param type
     */
    public void updOverTimeType(String overTimeId, int type) {
        overTimeDao.updOverTimeType(new ObjectId(overTimeId),type);
    }

    public void updTongGuo(String overTimeId, String userId) {
        if (StringUtils.isEmpty(userId)) {
            overTimeDao.updOverTimeType(new ObjectId(overTimeId),2);
        } else {
            overTimeDao.updShUserById(new ObjectId(overTimeId),new ObjectId(userId));
        }
    }

    /**
     *
     * @param year
     * @param month
     * @param userId
     * @return
     */
    public List<OverTimeDTO> selMyJiaBanSalary(int year, int month, ObjectId userId) {
        String startTime = String.valueOf(year)+"/"+((String.valueOf(month).length()!=2)?("0"+month):String.valueOf(month))+"/"+"01"+" "+"00:00";
        String endTime = LastDayOfMonth.getLastDayOfMonth(year, month)+" "+"23:59";
        long stime = DateTimeUtils.getStrToLongTime(startTime,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM);
        long etime = DateTimeUtils.getStrToLongTime(endTime,DateTimeUtils.DATE_YYYY_MM_DD_HH_MM);
        List<OverTimeEntry> overTimeEntryList = overTimeDao.selMyJiaBanSalary(stime,etime,userId);
        List<OverTimeDTO> overTimeDTOs = new ArrayList<OverTimeDTO>();
        if (overTimeEntryList!=null && overTimeEntryList.size()!=0) {
            for (OverTimeEntry overTimeEntry : overTimeEntryList) {
                OverTimeDTO overTimeDTO = new OverTimeDTO(overTimeEntry);
                String time = "";
                String st=overTimeEntry.getStartTime();
                String et = overTimeEntry.getEndTime();
                DecimalFormat df = new DecimalFormat("#.00");
                int se = Integer.valueOf(et.split(":")[0])-Integer.valueOf(st.split(":")[0]);
                if (Integer.valueOf(st.split(":")[1])>Integer.valueOf(et.split(":")[1])) {
                    double a = (double)(60+Integer.valueOf(et.split(":")[1])-Integer.valueOf(st.split(":")[1]));
                    double b = 0;
                    if (a!=0) {
                        b = a/60;
                    }
                    time = String.valueOf(se-1+df.format(b));
                } else {
                    double a = (double)(Integer.valueOf(et.split(":")[1])-Integer.valueOf(st.split(":")[1]));
                    double b = 0;
                    if (a!=0) {
                        b = a/60;
                    }
                    time = String.valueOf(se+df.format(b));
                }
                overTimeDTO.setTimes(time);
                overTimeDTOs.add(overTimeDTO);
            }
        }
        return overTimeDTOs;
    }

    /**
     *
     * @param year
     * @param month
     * @param userId
     * @param response
     */
    public void exportMySarlaryList(int year, int month, ObjectId userId, HttpServletResponse response) {
        List<OverTimeDTO> overTimeDTOList = selMyJiaBanSalary(year, month,userId);
        HSSFWorkbook wb = new HSSFWorkbook();
//生成一个sheet1
        HSSFSheet sheet = wb.createSheet("加班薪酬表");
        //为sheet1生成第一行，用于放表头信息
        HSSFRow row = sheet.createRow(0);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("加班日期");
        cell = row.createCell(1);
        cell.setCellValue("加班时间");
        cell = row.createCell(2);
        cell.setCellValue("加班内容");
        cell = row.createCell(3);
        cell.setCellValue("加班时长(h)");
        cell = row.createCell(4);
        cell.setCellValue("加班薪酬(元)");
        int page=0;
        if (overTimeDTOList!=null && overTimeDTOList.size()!=0) {
            for(OverTimeDTO overTimeDTO : overTimeDTOList) {
                page++;
                row = sheet.createRow(page);
                cell = row.createCell(0);
                cell.setCellValue(overTimeDTO.getDate());
                cell = row.createCell(1);
                cell.setCellValue(overTimeDTO.getTimeDuan());
                cell = row.createCell(2);
                cell.setCellValue(overTimeDTO.getCause());
                cell = row.createCell(3);
                cell.setCellValue(overTimeDTO.getTimes());
                cell = row.createCell(4);
                cell.setCellValue(overTimeDTO.getSalary());
            }
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
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("加班薪酬表.xls", "UTF-8"));
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
     *
     * @param schoolId
     * @param map
     * @param userId
     */
    public void selMyOverTimeInfo(ObjectId schoolId, Map<String, Object> map, ObjectId userId) {
        List<Date> dateList = WeekUtil.dateToWeek(new Date());
        List<OverTimeEntry> overTimeEntryList = overTimeDao.selMyJiaBanSalary(dateList.get(0).getTime(),dateList.get(dateList.size()-1).getTime(),userId);
        List<MyOverTimeDTO> myOverTimeDTOs = new ArrayList<MyOverTimeDTO>();
        List<OverTimeDTO> overTimeDTOs = new ArrayList<OverTimeDTO>();
        MyOverTimeDTO myOverTimeDTO = new MyOverTimeDTO();
        if (overTimeEntryList!=null && overTimeEntryList.size()!=0) {
            for (Date date : dateList) {
                myOverTimeDTO = new MyOverTimeDTO();
                myOverTimeDTO.setTimes(sdf.format(date));
                myOverTimeDTO.setIsToday(0);
                if (sdf.format(date).equals(sdf.format(new Date()))) {
                    myOverTimeDTO.setIsToday(1);
                }
                overTimeDTOs = new ArrayList<OverTimeDTO>();
                for(OverTimeEntry overTimeEntry : overTimeEntryList) {
                    if (sdf.format(date).contains(DateTimeUtils.convert(overTimeEntry.getDate(), DateTimeUtils.DATE_YYYY_MM_DD_N))) {
                        OverTimeDTO overTimeDTO = new OverTimeDTO(overTimeEntry);
                        overTimeDTOs.add(overTimeDTO);
                    }
                }
                myOverTimeDTO.setOverTimeDTOs(overTimeDTOs);
                myOverTimeDTO.setCount(overTimeDTOs.size());
                myOverTimeDTOs.add(myOverTimeDTO);
            }
        } else {
            for (Date date : dateList) {
                myOverTimeDTO = new MyOverTimeDTO();
                myOverTimeDTO.setTimes(sdf.format(date));
                myOverTimeDTO.setIsToday(0);
                if (sdf.format(date).equals(sdf.format(new Date()))) {
                    myOverTimeDTO.setIsToday(1);
                }
                myOverTimeDTOs.add(myOverTimeDTO);
            }
        }
        map.put("rows",myOverTimeDTOs);

    }
}
