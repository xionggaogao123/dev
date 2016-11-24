package com.fulaan.zouban.service;

import com.db.zouban.TimeTableDao;
import com.db.zouban.ZouBanCourseDao;
import com.db.zouban.ZoubanNoticeDao;
import com.fulaan.groupdiscussion.service.EaseMobService;
import com.fulaan.letter.service.LetterService;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.user.service.UserService;
import com.fulaan.zouban.dto.*;
import com.pojo.letter.LetterEntry;
import com.pojo.school.ClassInfoDTO;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.zouban.*;
import com.pojo.zouban.ZoubanNoticeEntry;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.Region;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by wangkaidong on 2016/10/12.
 * <p/>
 * 调课Service
 */
@Service
public class AdjustCourseService {
    private ZoubanNoticeDao zoubanNoticeDao = new ZoubanNoticeDao();
    private TimeTableDao timeTableDao = new TimeTableDao();
    private ZouBanCourseDao zouBanCourseDao = new ZouBanCourseDao();

    @Autowired
    private TimeTableService timeTableService;
    @Autowired
    private TimetableConfService timetableConfService;
    @Autowired
    private ClassService classService;
    @Autowired
    private UserService userService;
    @Autowired
    private EaseMobService easeMobService;
    @Autowired
    private LetterService letterService;


    /**
     * 检查多周是否调过课
     *
     * @param term
     * @param schoolId
     * @param startWeek
     * @param endWeek
     * @return
     */
    public boolean checkNotice(String term, ObjectId schoolId, int startWeek, int endWeek) {
        List<Integer> weekList = new ArrayList<Integer>();
        for (int i = startWeek; i <= endWeek; i++) {
            weekList.add(i);
        }
        return zoubanNoticeDao.hasNotice(term, schoolId, 1, weekList);
    }


    /**
     * 查询调课起始结束周
     *
     * @param noticeId
     * @return
     */
    public Map<String, Integer> getNoticeInfo(ObjectId noticeId) {
        ZoubanNoticeEntry zoubanNoticeEntry = zoubanNoticeDao.findNoticeById(noticeId);
        List<Integer> weekList = zoubanNoticeEntry.getWeek();
        Collections.sort(weekList, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
        Map<String, Integer> weekMap = new HashMap<String, Integer>();
        weekMap.put("startWeek", weekList.get(0));
        weekMap.put("endWeek", weekList.get(weekList.size() - 1));

        return weekMap;
    }


    /**
     * 添加调课记录
     *
     * @param term
     * @param schoolId
     * @param gradeId
     * @param userName
     * @param description
     * @param startWeek
     * @param endWeek
     */
    public void addNotice(String term, ObjectId schoolId, ObjectId gradeId, String userName, String description, int startWeek, int endWeek) {
        List<Integer> weekList = new ArrayList<Integer>();
        for (int i = startWeek; i <= endWeek; i++) {
            weekList.add(i);
        }
        int type = startWeek == endWeek ? 1 : 2;
        ZoubanNoticeEntry zoubanNoticeEntry = new ZoubanNoticeEntry(term, schoolId, gradeId, userName, type, description, weekList);
        zoubanNoticeDao.addNotice(zoubanNoticeEntry);
    }


    /**
     * 分页查询调课记录
     *
     * @param term
     * @param gradeId
     * @param page
     * @param pageSize
     * @return
     */
    public List<ZoubanNoticeDTO> findNoticeList(String term, ObjectId gradeId, int page, int pageSize) {
        List<ZoubanNoticeDTO> zoubanNoticeDTOs = new ArrayList<ZoubanNoticeDTO>();
        List<ZoubanNoticeEntry> zoubanNoticeEntries = zoubanNoticeDao.findNoticeList(term, gradeId, page, pageSize);

        for (ZoubanNoticeEntry zoubanNoticeEntry : zoubanNoticeEntries) {
            List<NoticeDetailDTO> noticeDetailDTOList = new ArrayList<NoticeDetailDTO>();
            List<ZoubanNoticeEntry.NoticeDetail> noticeDetails = zoubanNoticeEntry.getNoticeDetail();
            noticeDetailDTOList.clear();
            for (ZoubanNoticeEntry.NoticeDetail noticeDetail : noticeDetails) {
                noticeDetailDTOList.add(new NoticeDetailDTO(noticeDetail));
            }
            List<String> classIds = new ArrayList<String>();
            for (ObjectId objectId : zoubanNoticeEntry.getClassIdList()) {
                classIds.add(objectId.toString());
            }
            zoubanNoticeDTOs.add(new ZoubanNoticeDTO(zoubanNoticeEntry.getID(), zoubanNoticeEntry.getUserName(), zoubanNoticeEntry.getType(),
                    zoubanNoticeEntry.getDes(), zoubanNoticeEntry.getState(),
                    zoubanNoticeEntry.getWeek(), classIds, noticeDetailDTOList));
        }
        return zoubanNoticeDTOs;
    }


    /**
     * 调课记录数
     *
     * @return
     */
    public int getNoticeCount(String term, ObjectId gradeId) {
        return zoubanNoticeDao.countNotice(term, gradeId);
    }

    /**
     * 查询调课课表
     *
     * @param term
     * @param classId
     * @param startWeek
     * @return
     * @throws Exception
     */
    public List<StudentTimeTable> getTimetable(String term, ObjectId classId, int startWeek) throws Exception {
        List<StudentTimeTable> timetableItemList = timeTableService.getClassTimeTable(term, classId.toString(), TimetableState.ADJUSTING.getState(), startWeek);
        if (timetableItemList == null || timetableItemList.size() == 0) {
            timetableItemList = timeTableService.getClassTimeTable(term, classId.toString(), TimetableState.PUBLISHED.getState(), startWeek);
        }

        return timetableItemList;
    }


    /**
     * 查询调课可用时间点
     *
     * @param term
     * @param classId
     * @param week
     * @param teacherId
     * @return
     */
    public List<PointJson> getAvailablePoint(String term, String gradeId, ObjectId classId, int week, ObjectId teacherId, int x, int y) {
        //调课只能调非走班课
        //先排除班级事务，再判断要调课的老师在哪些时间点可以调课，再遍历每个可调课时间点，判断该点的老师是否可以调到本时间点

        List<PointJson> pointJsonList = new ArrayList<PointJson>();
        TimeTableEntry timeTableEntry = timeTableDao.findTimeTable(term, classId, TimetableState.ADJUSTING.getState(), week);
        if (timeTableEntry == null) {
            timeTableEntry = timeTableDao.findTimeTable(term, classId, TimetableState.PUBLISHED.getState(), week);
        }

        TimetableConfDTO timetableConfDTO = timetableConfService.getClassEventTimetable(term, gradeId, classId.toString());

        //班级事务
        List<PointDTO> pointDTOs = timetableConfDTO.getPointDTOs();
        List<PointJson> pointJsons = new ArrayList<PointJson>();
        for (PointDTO p : pointDTOs) {
            pointJsons.add(new PointJson(p.getX(), p.getY()));
        }


        for (CourseItem item : timeTableEntry.getCourseList()) {
            //排除班级事务
            if (!pointJsons.contains(new PointJson(item.getXIndex(), item.getYIndex())) &&
                    item.getType() == ZoubanType.FEIZOUBAN.getType() &&
                    !(item.getXIndex() == x && item.getYIndex() == y)) {
                //先判断所调课程能否调到该时间点
                if (checkTeacher(term, new ObjectId(gradeId), teacherId, week, item.getXIndex(), item.getYIndex())) {
                    List<ObjectId> courseList = item.getCourse();
                    ZouBanCourseEntry zouBanCourseEntry = zouBanCourseDao.getCourseInfoById(courseList.get(0));
                    ObjectId teacherId2 = zouBanCourseEntry.getTeacherId();

                    //无老师的非走班课
                    if (teacherId2 == null || teacherId2.equals("")) {
                        pointJsonList.add(new PointJson(item.getXIndex(), item.getYIndex()));
                    } else {
                        //再判断该点的课程能否调到本时间点
                        if (checkTeacher(term, new ObjectId(gradeId), teacherId2, week, x, y)) {
                            pointJsonList.add(new PointJson(item.getXIndex(), item.getYIndex()));
                        }
                    }
                }
            }
        }

        return pointJsonList;
    }


    /**
     * 检查是否可调课
     *
     * @param term
     * @param gradeId
     * @param teacherId
     * @param week
     * @param x
     * @param y
     * @return
     */
    private boolean checkTeacher(String term, ObjectId gradeId, ObjectId teacherId, int week, int x, int y) {
        //第一步：查询老师带的所有教学班
        //第二步：根据教学班关联的行政班查询课表
        //第三步：检查所有课表在(x,y)是否有该老师的课，如果有则不能调课

        List<ZouBanCourseEntry> zouBanCourseEntryList = zouBanCourseDao.findCourseListByTeacherId(term.substring(0, 11), teacherId, false);

        Set<ObjectId> courseIdSet = new HashSet<ObjectId>();
        Set<ObjectId> classIdSet = new HashSet<ObjectId>();
        for (ZouBanCourseEntry entry : zouBanCourseEntryList) {
            if (gradeId.equals(entry.getGradeId())) {
                courseIdSet.add(entry.getID());
                classIdSet.addAll(entry.getClassId());
            }
        }

        for (ObjectId classId : classIdSet) {
            TimeTableEntry timeTableEntry = timeTableDao.findTimeTable(term, classId, TimetableState.ADJUSTING.getState(), week);
            if (timeTableEntry == null) {
                timeTableEntry = timeTableDao.findTimeTable(term, classId, TimetableState.PUBLISHED.getState(), week);
            }

            List<CourseItem> courseItemList = timeTableEntry.getCourseList();
            for (CourseItem item : courseItemList) {
                if (item.getXIndex() == x && item.getYIndex() == y && courseIdSet.contains(item.getCourse().get(0))) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * 调课
     *
     * @param term
     * @param classId
     * @param weekStr
     * @param courseItemId1
     * @param courseItemId2
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    public void addChange(String term, ObjectId classId, String weekStr, ObjectId courseItemId1, ObjectId courseItemId2,
                          int x1, int y1, int x2, int y2) {
        List<Integer> weekList = getWeekList(weekStr);

        //先查询调课课表，如果没有则添加
        for (int week : weekList) {
            TimeTableEntry timeTableEntry = timeTableDao.findTimeTable(term, classId, TimetableState.ADJUSTING.getState(), week);
            if (timeTableEntry == null) {
                timeTableEntry = timeTableDao.findTimeTable(term, classId, TimetableState.PUBLISHED.getState(), week);
                timeTableEntry.setType(TimetableState.ADJUSTING.getState());
                timeTableEntry.setID(null);
                timeTableDao.addTimeTable(timeTableEntry);
            }
        }

        timeTableDao.updateCourseItem(term, classId, TimetableState.ADJUSTING.getState(), weekList, courseItemId1, x2, y2);
        timeTableDao.updateCourseItem(term, classId, TimetableState.ADJUSTING.getState(), weekList, courseItemId2, x1, y1);
    }


    private List<Integer> getWeekList(String weekStr) {
        String[] weeks = weekStr.split(",");
        List<Integer> weekList = new ArrayList<Integer>();
        for (String week : weeks) {
            weekList.add(Integer.valueOf(week));
        }
        return weekList;
    }


    /**
     * 保存调课结果
     *
     * @param term
     * @param gradeId
     * @param weekStr
     */
    public void saveChange(String term, ObjectId gradeId, String weekStr, ObjectId noticeId, ObjectId userId, String chatId, String userName, String maxImage) {
        //把调课课表复制到原课表中并删除调课课表

        List<Integer> weekList = getWeekList(weekStr);

        //查询本年级的所有班级id
        List<ClassInfoDTO> classInfoDTOList = classService.findClassByGradeId(gradeId.toString());
        //班级id-name map
        Map<ObjectId, String> classIdNameMap = new HashMap<ObjectId, String>();
        List<ObjectId> classIdList = new ArrayList<ObjectId>();
        for (ClassInfoDTO classInfoDTO : classInfoDTOList) {
            classIdNameMap.put(new ObjectId(classInfoDTO.getId()), classInfoDTO.getClassName());
            classIdList.add(new ObjectId(classInfoDTO.getId()));
        }

        //调课详情列表
        List<ZoubanNoticeEntry.NoticeDetail> noticeDetailList = new ArrayList<ZoubanNoticeEntry.NoticeDetail>();

        //调课老师列表，用于发送调课通知
        List<ObjectId> teacherIdList = new ArrayList<ObjectId>();

        String[] numStr = {"一", "二", "三", "四", "五", "六", "日"};

        for (ObjectId classId : classIdList) {
            String className = classIdNameMap.get(classId);
            //调课课表
            TimeTableEntry timeTableEntry = timeTableDao.findTimeTable(term, classId, TimetableState.ADJUSTING.getState(), weekList.get(0));
            if (timeTableEntry != null) {
                //调课课表项
                List<CourseItem> courseItemList = timeTableEntry.getCourseList();
                Map<ObjectId, CourseItem> courseItemMap = new HashMap<ObjectId, CourseItem>();
                for (CourseItem item : courseItemList) {
                    courseItemMap.put(item.getId(), item);
                }

                //原课表
                TimeTableEntry oTable = timeTableDao.findTimeTable(term, classId, TimetableState.PUBLISHED.getState(), weekList.get(0));
                //原课表项
                List<CourseItem> oCourseItemList = oTable.getCourseList();

                //遍历原课表，添加调课详情
                for (CourseItem item : oCourseItemList) {
                    CourseItem adjustItem = courseItemMap.get(item.getId());
                    if (item.getXIndex() != adjustItem.getXIndex() ||
                            item.getYIndex() != adjustItem.getYIndex()) {

                        ZouBanCourseEntry zouBanCourseEntry = zouBanCourseDao.getCourseInfoById(item.getCourse().get(0));
                        String teacherName = zouBanCourseEntry.getTeacherName();
                        String courseName = zouBanCourseEntry.getClassName();
                        String oTime = "周" + numStr[item.getXIndex() - 1] + "第" + item.getYIndex() + "节";
                        String nTime = "周" + numStr[adjustItem.getXIndex() - 1] + "第" + adjustItem.getYIndex() + "节";

                        ZoubanNoticeEntry.NoticeDetail detail = new ZoubanNoticeEntry.NoticeDetail(className, courseName, teacherName, oTime, nTime);
                        noticeDetailList.add(detail);

                        teacherIdList.add(zouBanCourseEntry.getTeacherId());
                    }
                }

                //更新到原课表
                timeTableDao.updateCourseItems(term, classId, TimetableState.PUBLISHED.getState(), weekList, courseItemList);
            }
        }

        //更新调课详情
        zoubanNoticeDao.updateNotice(noticeId, 1, noticeDetailList);

        //删除调课课表
        timeTableDao.deleteTimetableByGradeId(term, gradeId, TimetableState.ADJUSTING.getState());

        String message;

        if (weekList.size() == 1) {
            message = "第" + weekList.get(0) + "周已经调课，请查看最新课表！";
        } else {
            message = "第" + weekList.get(0) + "周 ~ " + weekList.get(weekList.size() - 1) + "周已经调课，请查看最新课表！";
        }

        //发送调课通知
        sendMessage(userId, chatId, teacherIdList, userName, maxImage, message);
    }


    //发送调课通知
    public void sendMessage(ObjectId userId, String chatId, List<ObjectId> recipientIds, String userName, String maxImage, String message) {
        List<UserDetailInfoDTO> userDetailInfoDTOs = userService.findUserInfoByIds(recipientIds);
        List<String> recipientChatIds = new ArrayList<String>();

        for (UserDetailInfoDTO userDetailInfoDTO : userDetailInfoDTOs) {
            recipientChatIds.add(userDetailInfoDTO.getChatid());
        }

        LetterEntry letterEntry = new LetterEntry(userId, message, recipientIds);
        letterService.sendLetter(letterEntry);
        easeMobService.sendMessage(chatId, recipientChatIds, message, userName, maxImage);
    }


    /**
     * 删除调课课表
     *
     * @param term
     * @param gradeId
     */
    public void removeChange(String term, ObjectId gradeId) {
        //删除调课课表
        timeTableDao.deleteTimetableByGradeId(term, gradeId, TimetableState.ADJUSTING.getState());
    }

    /**
     * 导出调课申请单
     * @param noticeId
     * @param response
     */
    public void exportTKNotice(String noticeId, HttpServletResponse response){
        ZoubanNoticeEntry zoubanNoticeEntry = zoubanNoticeDao.findNoticeById(new ObjectId(noticeId));
        HSSFWorkbook wb = new HSSFWorkbook();
        //生成一个sheet1
        HSSFSheet sheet = wb.createSheet("调课通知");
        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 指定单元格垂直居中对
        if(zoubanNoticeEntry!=null){
            String userName = zoubanNoticeEntry.getUserName();
            String reason = zoubanNoticeEntry.getDes();
            HSSFRow row0 = sheet.createRow(0);
            row0.setHeight((short) (25 * 20));
            row0.createCell(0).setCellStyle(cellStyle);
            row0.createCell(0).setCellValue("申请人："+userName);
            HSSFRow row1 = sheet.createRow(1);
            row1.setHeight((short) (25 * 20));
            row1.createCell(0).setCellStyle(cellStyle);
            row1.createCell(0).setCellValue("原因："+reason);
            List<NoticeDetailDTO> noticeDetailDTOList = new ArrayList<NoticeDetailDTO>();
            List<ZoubanNoticeEntry.NoticeDetail> noticeDetails = zoubanNoticeEntry.getNoticeDetail();
            noticeDetailDTOList.clear();
            for (ZoubanNoticeEntry.NoticeDetail noticeDetail : noticeDetails) {
                noticeDetailDTOList.add(new NoticeDetailDTO(noticeDetail));
            }
            String [] head = {"老师","班级","课程","原上课时间","新上课时间"};
            HSSFRow row = sheet.createRow(2);
            row.setHeight((short) (25 * 20));
            for(int i = 0; i<head.length; i++) {
                sheet.setColumnWidth(i, 4000);
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(head[i]);
                cell.setCellStyle(cellStyle);
            }
            if(noticeDetailDTOList.size()>0){
                for(int i = 0; i<noticeDetailDTOList.size(); i++){
                    NoticeDetailDTO noticeDetailDTO = noticeDetailDTOList.get(i);
                    HSSFRow noticeRow = sheet.createRow(i+3);
                    noticeRow.setHeight((short) (25 * 20));
                    HSSFCell cell0 = noticeRow.createCell(0);
                    cell0.setCellStyle(cellStyle);
                    cell0.setCellValue(noticeDetailDTO.getTeacherName());
                    HSSFCell cell1 = noticeRow.createCell(1);
                    cell1.setCellStyle(cellStyle);
                    cell1.setCellValue(noticeDetailDTO.getClassName());
                    HSSFCell cell2 = noticeRow.createCell(2);
                    cell2.setCellStyle(cellStyle);
                    cell2.setCellValue(noticeDetailDTO.getCourseName());
                    HSSFCell cell3 = noticeRow.createCell(3);
                    cell3.setCellStyle(cellStyle);
                    cell3.setCellValue(noticeDetailDTO.getOldTime());
                    HSSFCell cell4 = noticeRow.createCell(4);
                    cell4.setCellStyle(cellStyle);
                    cell4.setCellValue(noticeDetailDTO.getNewTime());
                }

            }
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();


        OutputStream outputStream = null;
        try {
            wb.write(os);
            byte[] content = os.toByteArray();
            outputStream = response.getOutputStream();
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("调课通知.xls", "UTF-8"));
            response.setContentLength((int) content.length);
            outputStream.write(content);
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
}
