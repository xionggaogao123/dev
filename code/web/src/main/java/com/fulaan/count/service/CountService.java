package com.fulaan.count.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.db.backstage.TeacherApproveDao;
import com.db.controlphone.AppTsDao;
import com.db.excellentCourses.ExcellentCoursesDao;
import com.db.excellentCourses.HourClassDao;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.CommunityDetailDao;
import com.db.fcommunity.CommunityHyDao;
import com.db.fcommunity.GroupDao;
import com.db.fcommunity.MemberDao;
import com.db.fcommunity.NewVersionCommunityBindDao;
import com.db.fcommunity.PartInContentDao;
import com.db.jiaschool.HomeSchoolDao;
import com.db.jiaschool.SchoolCommunityDao;
import com.db.newVersionGrade.NewVersionSubjectDao;
import com.db.operation.AppCommentDao;
import com.db.operation.AppNoticeDao;
import com.db.operation.AppOperationDao;
import com.db.smalllesson.LessonUserResultDao;
import com.db.smalllesson.SmallLessonDao;
import com.db.user.UserDao;
import com.db.wrongquestion.SubjectClassDao;
import com.fulaan.count.dto.JxmCountDto;
import com.fulaan.count.dto.TczyDto;
import com.fulaan.count.dto.TzDto;
import com.fulaan.count.dto.TztbDto;
import com.fulaan.count.dto.XktDto;
import com.fulaan.count.dto.XqstDto;
import com.fulaan.count.dto.ZytbDto;
import com.fulaan.jiaschool.dto.HomeSchoolDTO;
import com.fulaan.wrongquestion.dto.SubjectClassDTO;
import com.mongodb.BasicDBObject;
import com.pojo.appnotice.AppNoticeEntry;
import com.pojo.fcommunity.CommunityDetailEntry;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.fcommunity.CommunityHyEntry;
import com.pojo.jiaschool.HomeSchoolEntry;
import com.pojo.newVersionGrade.NewVersionSubjectEntry;
import com.pojo.operation.AppCommentEntry;
import com.pojo.smalllesson.SmallLessonEntry;
import com.pojo.wrongquestion.SubjectClassEntry;

@Service
public class CountService {
    
    
    private SchoolCommunityDao schoolCommunityDao = new SchoolCommunityDao();
    
    private GroupDao groupDao = new GroupDao();
    
    private MemberDao memberDao = new MemberDao();
    
    private TeacherApproveDao teacherApproveDao = new TeacherApproveDao();
    
    private NewVersionCommunityBindDao newVersionCommunityBindDao = new NewVersionCommunityBindDao();
    
    private HomeSchoolDao homeSchoolDao = new HomeSchoolDao();
    
    private UserDao userDao = new UserDao();
    //作业
    private AppCommentDao appCommentDao = new AppCommentDao();
    //通知
    private AppNoticeDao appNoticeDao = new AppNoticeDao();
    //帖子
    private CommunityDetailDao communityDetailDao = new CommunityDetailDao();
    //活跃社群
    private CommunityHyDao communityHyDao = new CommunityHyDao();
    //直播课
    private ExcellentCoursesDao excellentCourseDao = new ExcellentCoursesDao();
    private HourClassDao hourClassDao = new HourClassDao();
    //推荐应用数量
    private AppTsDao appTsDao = new AppTsDao();
    //科目
    private SubjectClassDao subjectClassDao = new SubjectClassDao();
    //作业提交
    private AppOperationDao appOperationDao = new AppOperationDao();
    
    private CommunityDao communityDao = new CommunityDao();
    
    private PartInContentDao partInContentDao = new PartInContentDao();
    
    private NewVersionSubjectDao newVersionSubjectDao = new NewVersionSubjectDao();
    
    private SmallLessonDao smallLessonDao = new SmallLessonDao();
    
    private LessonUserResultDao lessonUserResultDao = new LessonUserResultDao();
    
    
    
    /**
     * 
     *〈简述〉获取学校列表
     *〈详细描述〉
     * @author Administrator
     * @return
     */
    public List<HomeSchoolDTO> getSimpleSchoolList(){
        List<HomeSchoolDTO> homeSchoolDTOs = new ArrayList<HomeSchoolDTO>();
        List<HomeSchoolEntry> entries = homeSchoolDao.getSchoolList();
        for(HomeSchoolEntry homeSchoolEntry : entries){
            if(homeSchoolEntry.getName()!=null){
                homeSchoolDTOs.add(new HomeSchoolDTO(homeSchoolEntry));
            }
        }
        return homeSchoolDTOs;
    }
    
    public List<ObjectId> reList(List<ObjectId> communityIdList,String grade) {
        List<ObjectId> communityIdList1 = new ArrayList<ObjectId>();
        if (StringUtils.isNotBlank(grade)) {
            for (ObjectId id : communityIdList) {
                CommunityEntry e = communityDao.findByObjectId(id);
                if (e != null) {
                    if (StringUtils.isNotBlank(e.getGradeVal())) {
                        if (e.getGradeVal().equals(grade)) {
                            communityIdList1.add(id);
                        }
                    }
                }
            }
            communityIdList = communityIdList1;
        }
        return communityIdList;
    }

    /**
     * 
     *〈简述〉家校美统计
     *〈详细描述〉
     * @author Administrator
     * @param schooleId
     * @return
     */
    public JxmCountDto jxmCount(String schooleId, String grade) {
        JxmCountDto jxmCountDto = new JxmCountDto();
        if (schooleId != null) {
            //社区数
            List<ObjectId> communityIdList = schoolCommunityDao.getCommunityIdsListBySchoolId(new ObjectId(schooleId));
            communityIdList = this.reList(communityIdList, grade);
            
            jxmCountDto.setCommunityCount(communityIdList.size());
            //通过社区id查找群组
            List<ObjectId> groupIdList = groupDao.getCommunitysIdsList(communityIdList);
            //成员id
            List<ObjectId> memberList = memberDao.getAllGroupIdsMembers(groupIdList);
            HashSet<ObjectId> h = new HashSet<ObjectId>(memberList);
            memberList.clear();
            memberList.addAll(h);
            
            jxmCountDto.setUserCount(memberList.size());
            //大V老师的id
            List<ObjectId> objectIdList = teacherApproveDao.selectMap(memberList);
            jxmCountDto.setTeacherCount(objectIdList.size());
            
            //家长数量
            jxmCountDto.setParentCount(memberList.size() - objectIdList.size());
            //学生数量
            Integer stuNum = 0;
            for (ObjectId cId : communityIdList) {
                List<ObjectId> userId = newVersionCommunityBindDao.getStudentListByCommunityId(cId);
                stuNum += userDao.getStudentNum(userId);
            }
            jxmCountDto.setStudentCount(stuNum);
            //获取时间点
            Map<Integer, Long> map = this.getTimePointOneDay();
            //本日发布作业数量
            List<Integer> zuoyeNum = new ArrayList<Integer>();
            int zuoye1 = appCommentDao.getWebAllDatePageNumberByTime( communityIdList, null, map.get(1), map.get(2));
            int zuoye2 = appCommentDao.getWebAllDatePageNumberByTime( communityIdList, null, map.get(2), map.get(3));
            int zuoye3 = appCommentDao.getWebAllDatePageNumberByTime( communityIdList, null, map.get(3), map.get(4));
            int zuoye4 = appCommentDao.getWebAllDatePageNumberByTime( communityIdList, null, map.get(4), map.get(5));
            int zuoye5 = appCommentDao.getWebAllDatePageNumberByTime( communityIdList, null, map.get(5), map.get(6));
            int zuoye6 = appCommentDao.getWebAllDatePageNumberByTime( communityIdList, null, map.get(6), map.get(7));
            int zuoyeAll = appCommentDao.getWebAllDatePageNumberByTime( communityIdList, null, null, null);
            zuoyeNum.add(zuoye1);
            zuoyeNum.add(zuoye2);
            zuoyeNum.add(zuoye3);
            zuoyeNum.add(zuoye4);
            zuoyeNum.add(zuoye5);
            zuoyeNum.add(zuoye6);
            jxmCountDto.setZuoyeNum(zuoyeNum);
            jxmCountDto.setAllZuoyeNum(zuoyeAll);
            //本日通知发布数量
            List<Integer> noticeNum = new ArrayList<Integer>();
            int notice1 = appNoticeDao.countAppNoticeEntries(communityIdList,map.get(1), map.get(2));
            int notice2 = appNoticeDao.countAppNoticeEntries(communityIdList,map.get(2), map.get(3));
            int notice3 = appNoticeDao.countAppNoticeEntries(communityIdList,map.get(3), map.get(4));
            int notice4 = appNoticeDao.countAppNoticeEntries(communityIdList,map.get(4), map.get(5));
            int notice5 = appNoticeDao.countAppNoticeEntries(communityIdList,map.get(5), map.get(6));
            int notice6 = appNoticeDao.countAppNoticeEntries(communityIdList,map.get(6), map.get(7));
            int noticeAll = appNoticeDao.countAppNoticeEntries(communityIdList,null, null);
            noticeNum.add(notice1);
            noticeNum.add(notice2);
            noticeNum.add(notice3);
            noticeNum.add(notice4);
            noticeNum.add(notice5);
            noticeNum.add(notice6);
            jxmCountDto.setNoticeNum(noticeNum);
            jxmCountDto.setAllNoticeNum(noticeAll);
            //本日帖子发布数量
            List<Integer> tzNum = new ArrayList<Integer>();
            int tz1 = communityDetailDao.countTz(communityIdList,3, map.get(1), map.get(2));
            int tz2 = communityDetailDao.countTz(communityIdList,3, map.get(2), map.get(3));
            int tz3 = communityDetailDao.countTz(communityIdList,3, map.get(3), map.get(4));
            int tz4 = communityDetailDao.countTz(communityIdList,3, map.get(4), map.get(5));
            int tz5 = communityDetailDao.countTz(communityIdList,3, map.get(5), map.get(6));
            int tz6 = communityDetailDao.countTz(communityIdList,3, map.get(6), map.get(7));
            int tzAll = communityDetailDao.countTz(communityIdList,3, null, null);
            
            tzNum.add(tz1);
            tzNum.add(tz2);
            tzNum.add(tz3);
            tzNum.add(tz4);
            tzNum.add(tz5);
            tzNum.add(tz6);
            jxmCountDto.setTzNum(tzNum);
            jxmCountDto.setAllTzNum(tzAll);
            //本日社群活跃数量
            List<Integer> hyNum = new ArrayList<Integer>();
            List<CommunityHyEntry> list1 = communityHyDao.communityHyCountEntry(communityIdList,map.get(1), map.get(2));
            List<CommunityHyEntry> list2 = communityHyDao.communityHyCountEntry(communityIdList,map.get(2), map.get(3));
            List<CommunityHyEntry> list3 = communityHyDao.communityHyCountEntry(communityIdList,map.get(3), map.get(4));
            List<CommunityHyEntry> list4 = communityHyDao.communityHyCountEntry(communityIdList,map.get(4), map.get(5));
            List<CommunityHyEntry> list5 = communityHyDao.communityHyCountEntry(communityIdList,map.get(5), map.get(6));
            List<CommunityHyEntry> list6 = communityHyDao.communityHyCountEntry(communityIdList,map.get(6), map.get(7));
            List<CommunityHyEntry> listAll = communityHyDao.communityHyCountEntry(communityIdList,null, null);
            hyNum.add(listToSet(list1));
            hyNum.add(listToSet(list2));
            hyNum.add(listToSet(list3));
            hyNum.add(listToSet(list4));
            hyNum.add(listToSet(list5));
            hyNum.add(listToSet(list6));
            jxmCountDto.setHyNum(hyNum);
            jxmCountDto.setAllHyNum(listToSet(listAll));
            //本日直播课数量
            List<Integer> zbNum = new ArrayList<Integer>();
            zbNum.add(getzbNum(communityIdList,map.get(1), map.get(2)));
            zbNum.add(getzbNum(communityIdList,map.get(2), map.get(3)));
            zbNum.add(getzbNum(communityIdList,map.get(3), map.get(4)));
            zbNum.add(getzbNum(communityIdList,map.get(4), map.get(5)));
            zbNum.add(getzbNum(communityIdList,map.get(5), map.get(6)));
            zbNum.add(getzbNum(communityIdList,map.get(6), map.get(7)));
            jxmCountDto.setZbNum(zbNum);
            jxmCountDto.setAllZbNum(getzbNum(communityIdList,null, null));
            //本日推荐引用数量
            List<Integer> yyNum = new ArrayList<Integer>();
            int yy1 = appTsDao.tsCount(communityIdList,map.get(1), map.get(2));
            int yy2 = appTsDao.tsCount(communityIdList,map.get(2), map.get(3));
            int yy3 = appTsDao.tsCount(communityIdList,map.get(3), map.get(4));
            int yy4 = appTsDao.tsCount(communityIdList,map.get(4), map.get(5));
            int yy5 = appTsDao.tsCount(communityIdList,map.get(5), map.get(6));
            int yy6 = appTsDao.tsCount(communityIdList,map.get(6), map.get(7));
            int yyAll = appTsDao.tsCount(communityIdList,null, null);
            yyNum.add(yy1);
            yyNum.add(yy2);
            yyNum.add(yy3);
            yyNum.add(yy4);
            yyNum.add(yy5);
            yyNum.add(yy6);
            jxmCountDto.setYyNum(yyNum);
            jxmCountDto.setAllYyNum(yyAll);
        }
        
        return jxmCountDto;
    }
    
    public int getzbNum(List<ObjectId> communityIdList, Long  startTime,Long endTime) {
        List<ObjectId> courseId = excellentCourseDao.getCourseIdByCid(communityIdList);
        return hourClassDao.countHourClass(courseId, startTime, endTime);
    }
    
    public int listToSet(List<CommunityHyEntry> list) {
        Set<ObjectId> set = new HashSet<ObjectId>();
        for(CommunityHyEntry c : list) {
            set.add(c.getCommunityId());
        }
        return set.size();
    }
    
    public Map<Integer, Long> getTimePointOneDay() {
        Map<Integer, Long> map = new HashMap<Integer, Long>();
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DATE);
        String dateStr1 = "" + year + "-" + month + "-" + day+" " + "00:00:00";
        String dateStr2 = "" + year + "-" + month + "-" + day+" " + "04:00:00";
        String dateStr3 = "" + year + "-" + month + "-" + day+" " + "08:00:00";
        String dateStr4 = "" + year + "-" + month + "-" + day+" " + "12:00:00";
        String dateStr5 = "" + year + "-" + month + "-" + day+" " + "16:00:00";
        String dateStr6 = "" + year + "-" + month + "-" + day+" " + "20:00:00";
        String dateStr7 = "" + year + "-" + month + "-" + day+" " + "24:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = sdf.parse(dateStr1);
            map.put(1, date1.getTime());
            
            Date date2 = sdf.parse(dateStr2);
            map.put(2, date2.getTime());
            
            Date date3 = sdf.parse(dateStr3);
            map.put(3, date3.getTime());
            
            Date date4 = sdf.parse(dateStr4);
            map.put(4, date4.getTime());
            
            Date date5 = sdf.parse(dateStr5);
            map.put(5, date5.getTime());
            
            Date date6 = sdf.parse(dateStr6);
            map.put(6, date6.getTime());
            
            Date date7 = sdf.parse(dateStr7);
            map.put(7, date7.getTime());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return map;
    }
    
    public Map<Integer, Long> getTimePointOneDay(String time) {
        Map<Integer, Long> map = new HashMap<Integer, Long>();
        String dateStr1 = time+" " + "00:00:00";
        String dateStr2 = time+" " + "04:00:00";
        String dateStr3 = time+" " + "08:00:00";
        String dateStr4 = time+" " + "12:00:00";
        String dateStr5 = time+" " + "16:00:00";
        String dateStr6 = time+" " + "20:00:00";
        String dateStr7 = time+" " + "24:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date1 = sdf.parse(dateStr1);
            map.put(1, date1.getTime());
            
            Date date2 = sdf.parse(dateStr2);
            map.put(2, date2.getTime());
            
            Date date3 = sdf.parse(dateStr3);
            map.put(3, date3.getTime());
            
            Date date4 = sdf.parse(dateStr4);
            map.put(4, date4.getTime());
            
            Date date5 = sdf.parse(dateStr5);
            map.put(5, date5.getTime());
            
            Date date6 = sdf.parse(dateStr6);
            map.put(6, date6.getTime());
            
            Date date7 = sdf.parse(dateStr7);
            map.put(7, date7.getTime());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return map;
    }
    
    //比较日期大小
    public boolean compareDate(String startTime, String endTime) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(startTime);
        Date endDate = sdf.parse(endTime);
        if (startDate.getTime() <= endDate.getTime()) {
            return true;
        } else {
            return false;
        }
    }
    
    //获取两个日期之间的日期集合
    public List<String> getListDate(String startTime, String endTime) throws Exception{
        List<String> dateList = new ArrayList<String>();
        if (startTime.equals(endTime)) {
            dateList.add(startTime);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            String nextDay = startTime;
            dateList.add(nextDay);
            while (!nextDay.equals(endTime)) {
                Date d = sdf.parse(nextDay);
                c.setTime(d);
                c.add(Calendar.DATE, 1);
                d = c.getTime();
                nextDay = sdf.format(d);
                dateList.add(nextDay);
            }
            

        }
        
        return dateList;
        
    }
    
    //获得学科
    public List<SubjectClassDTO> getSubjectClass() {
        List<SubjectClassEntry> subjectClassEntriesL = subjectClassDao.getList();
        SubjectClassDTO dto1 = new SubjectClassDTO();
        dto1.setId("");
        dto1.setName("全学科");
        List<SubjectClassDTO> subjectClassDtoList = new ArrayList<SubjectClassDTO>();
        subjectClassDtoList.add(dto1);
        for (SubjectClassEntry s : subjectClassEntriesL) {
            SubjectClassDTO dto = new SubjectClassDTO(s);
            subjectClassDtoList.add(dto);
        }
        
        return subjectClassDtoList;
    }
    
  //获得学科
    public List<SubjectClassDTO> getSubjectClass1() {
        List<SubjectClassEntry> subjectClassEntriesL = subjectClassDao.getList();
  
        List<SubjectClassDTO> subjectClassDtoList = new ArrayList<SubjectClassDTO>();

        for (SubjectClassEntry s : subjectClassEntriesL) {
            SubjectClassDTO dto = new SubjectClassDTO(s);
            subjectClassDtoList.add(dto);
        }
        
        return subjectClassDtoList;
    }
    
    //作业图表
    public ZytbDto zytb(String schooleId, String startTime, String endTime) {
        ZytbDto zytbDto = new ZytbDto();
        if(StringUtils.isNotBlank(schooleId)) {
            List<ObjectId> communityIdList = schoolCommunityDao.getCommunityIdsListBySchoolId(new ObjectId(schooleId));
            long startTimeL = 0;
            long endTimeL = 0;
            if (StringUtils.isNotBlank(startTime)) {
                Map<Integer, Long> map = this.getTimePointOneDay(startTime);
                startTimeL = map.get(1);
            }
            if (StringUtils.isNotBlank(endTime)) {
                Map<Integer, Long> map = this.getTimePointOneDay(endTime);
                endTimeL = map.get(7);
            }
            
            List<SubjectClassEntry> subjectClassEntriesL = subjectClassDao.getList();
            for (SubjectClassEntry s : subjectClassEntriesL) {
                int num = appCommentDao.getWebAllDatePageNumberByTime(communityIdList, s.getID().toString(), startTimeL, endTimeL);
                zytbDto.getNum().add(num);
                zytbDto.getSubjectName().add(s.getName());
                zytbDto.getSubjectId().add(s.getID().toString());
            }
        }
        return zytbDto;
    }
    
    //作业发布统计按学科
    public Map<String, Object> tczy(String subjectId, String schooleId, String startTime, String endTime,  int page, int pageSize) {
        Map<String, Object> mapResult = new HashMap<String, Object>();
        List<TczyDto> tczyDto = new ArrayList<TczyDto>();
        if(StringUtils.isNotBlank(schooleId)) {
            
            long startTimeL = 0;
            long endTimeL = 0;
            if (StringUtils.isNotBlank(startTime)) {
                Map<Integer, Long> map = this.getTimePointOneDay(startTime);
                startTimeL = map.get(1);
            }
            if (StringUtils.isNotBlank(endTime)) {
                Map<Integer, Long> map = this.getTimePointOneDay(endTime);
                endTimeL = map.get(7);
            }
            
            List<ObjectId> communityIdList = schoolCommunityDao.getCommunityIdsListBySchoolId(new ObjectId(schooleId));
            List<BasicDBObject> acList = appCommentDao.count(communityIdList, subjectId, startTimeL, endTimeL,0,0);
            for (BasicDBObject db : acList) {
                TczyDto t = new TczyDto();
                ObjectId aid = ((BasicDBObject)db.get("_id")).getObjectId("aid");
                t.setUserName(userDao.findByUserId(aid).getNickName());
                ObjectId sid = ((BasicDBObject)db.get("_id")).getObjectId("sid");
                t.setSubjectName(subjectClassDao.getEntry(sid).getName());
                Integer count = (Integer)db.get("count");
                t.setFaNum(count);
                List<AppCommentEntry> appList = appCommentDao.getWebAllDatePageByTimePage(communityIdList, sid.toString(), aid.toString(), startTimeL, endTimeL, 0, 0);
                if (CollectionUtils.isNotEmpty(appList)) {
                    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
                    t.setFbDate(s.format(new Date(appList.get(0).getCreateTime())));
                    List<ObjectId> appId = new ArrayList<ObjectId>();
                    int tjp = 0;
                    for(AppCommentEntry a : appList) {
                        appId.add(a.getID());
                        tjp += a.getLoadNumber();
                    }
                    t.setTjPeoNum(tjp);
                    Integer entries2 =appOperationDao.getEntryListByParentId3(appId, 3);
                    t.setTjNum(entries2);
                }
                tczyDto.add(t);
                
            }
        }
        
        Collections.sort(tczyDto, new Comparator<TczyDto>() {

            @Override
            public int compare(TczyDto o1, TczyDto o2) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    if (sdf.parse(o1.getFbDate()).getTime() < sdf.parse(o2.getFbDate()).getTime()) {
                        return 1;
                    } else if (new Date(01).getTime() == new Date(02).getTime()) {
                        return 0;
                    } else {
                        return -1;
                    }
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    return 0;
                }
            }
            
        });
        mapResult.put("count", tczyDto.size());
        mapResult.put("dto", this.returnList(page, pageSize, tczyDto));
        return mapResult;
    }
    //作业统计按班级
    public Map<String, Object> bjzy(String communityId, String grade, String schooleId, String startTime, String endTime , int page, int pageSize) {
        Map<String, Object> mapResult = new HashMap<String, Object>();
        List<TczyDto> tczyDto = new ArrayList<TczyDto>();
        if(StringUtils.isNotBlank(schooleId)) {
            
            long startTimeL = 0;
            long endTimeL = 0;
            if (StringUtils.isNotBlank(startTime)) {
                Map<Integer, Long> map = this.getTimePointOneDay(startTime);
                startTimeL = map.get(1);
            }
            if (StringUtils.isNotBlank(endTime)) {
                Map<Integer, Long> map = this.getTimePointOneDay(endTime);
                endTimeL = map.get(7);
            }
            
            List<ObjectId> communityIdList = schoolCommunityDao.getCommunityIdsListBySchoolId(new ObjectId(schooleId));
            communityIdList = this.reList(communityIdList, grade);
            List<BasicDBObject> acList = appCommentDao.count1(communityIdList, null, startTimeL, endTimeL,0,0);
            for (BasicDBObject db : acList) {
                TczyDto t = new TczyDto();
                ObjectId rid = ((BasicDBObject)db.get("_id")).getObjectId("rid");
                t.setClassName(communityDao.findByObjectId(rid).getCommunityName());
                Integer count = (Integer)db.get("count");
                t.setFaNum(count);
                List<AppCommentEntry> appList = appCommentDao.getWebAllDatePageByTimePage(rid, null, null, startTimeL, endTimeL, 0, 0);
                if (CollectionUtils.isNotEmpty(appList)) {
                    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
                    t.setFbDate(s.format(new Date(appList.get(0).getCreateTime())));
                    List<ObjectId> appId = new ArrayList<ObjectId>();
                    int tjp = 0;
                    for(AppCommentEntry a : appList) {
                        appId.add(a.getID());
                        tjp += a.getLoadNumber();
                    }
                    t.setTjPeoNum(tjp);
                    Integer entries2 =appOperationDao.getEntryListByParentId3(appId, 3);
                    t.setTjNum(entries2);
                }
                tczyDto.add(t);
            }
        }
        
        Collections.sort(tczyDto, new Comparator<TczyDto>() {

            @Override
            public int compare(TczyDto o1, TczyDto o2) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    if (sdf.parse(o1.getFbDate()).getTime() < sdf.parse(o2.getFbDate()).getTime()) {
                        return 1;
                    } else if (new Date(01).getTime() == new Date(02).getTime()) {
                        return 0;
                    } else {
                        return -1;
                    }
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    return 0;
                }
            }
            
        });
        mapResult.put("count", tczyDto.size());
        mapResult.put("dto", this.returnList(page, pageSize, tczyDto));
        return mapResult;
    }
    
    //通知图表
    public TztbDto tztb(String schooleId, String startTime, String endTime) throws Exception{
        TztbDto tztbDto = new TztbDto();
        List<String> dateList = new ArrayList<String>();
        if (startTime != null && endTime != null) {
            if(this.compareDate(startTime, endTime)) {
                dateList = this.getListDate(startTime, endTime);
                tztbDto.setDateList(dateList);
            } else {
                
            }
        }
        if(StringUtils.isNotBlank(schooleId)) {
            List<ObjectId> communityIdList = schoolCommunityDao.getCommunityIdsListBySchoolId(new ObjectId(schooleId));
            for (String s : dateList) {
                Map<Integer, Long> map = this.getTimePointOneDay(s);
                
                tztbDto.getNum().add(appNoticeDao.countAppNoticeEntries(communityIdList,map.get(1), map.get(7)));
            }
            
        }
        
        return tztbDto;
    }
    
    //老师发布通知次数
    public Map<String, Object> tzsub(String subjectId, String schooleId, String startTime, String endTime, int page, int pageSize) {
        Map<String, Object> mapResult = new HashMap<String, Object>();
        List<TzDto> tczyDto = new ArrayList<TzDto>();
        if(StringUtils.isNotBlank(schooleId)) {
            
            long startTimeL = 0;
            long endTimeL = 0;
            if (StringUtils.isNotBlank(startTime)) {
                Map<Integer, Long> map = this.getTimePointOneDay(startTime);
                startTimeL = map.get(1);
            }
            if (StringUtils.isNotBlank(endTime)) {
                Map<Integer, Long> map = this.getTimePointOneDay(endTime);
                endTimeL = map.get(7);
            }
            
            List<ObjectId> communityIdList = schoolCommunityDao.getCommunityIdsListBySchoolId(new ObjectId(schooleId));
            List<BasicDBObject> acList = appNoticeDao.count(communityIdList, subjectId, startTimeL, endTimeL,0,0);
            for (BasicDBObject db : acList) {
                TzDto t = new TzDto();
                ObjectId aid = ((BasicDBObject)db.get("_id")).getObjectId("uid");
                t.setName(userDao.findByUserId(aid).getNickName());
                ObjectId sid = ((BasicDBObject)db.get("_id")).getObjectId("sid");
                t.setSubjectName(subjectClassDao.getEntry(sid).getName());
                Integer count = (Integer)db.get("count");
                t.setFbNum(count);
                if(db.containsField("rl")) {
                    List<ObjectId> l =  (List<ObjectId>)db.get("rl");
                    if (CollectionUtils.isNotEmpty(l)) {
                        t.setPeoNum(l.size());
                    } else {
                        t.setPeoNum(0);
                    }
                } else {
                    t.setPeoNum(0);
                }
                
                List<AppNoticeEntry> appList = appNoticeDao.getWebAllDatePageByTimePage(communityIdList, sid.toString(), aid.toString(), startTimeL, endTimeL, 0, 0);
                if (CollectionUtils.isNotEmpty(appList)) {
                    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
                    t.setFbDate(s.format(new Date(appList.get(0).getSubmitTime())));
            
                    
                }
                tczyDto.add(t);
                
            }
        }
        
        Collections.sort(tczyDto, new Comparator<TzDto>() {

            @Override
            public int compare(TzDto o1, TzDto o2) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    if (sdf.parse(o1.getFbDate()).getTime() < sdf.parse(o2.getFbDate()).getTime()) {
                        return 1;
                    } else if (new Date(01).getTime() == new Date(02).getTime()) {
                        return 0;
                    } else {
                        return -1;
                    }
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    return 0;
                }
            }
            
        });
        mapResult.put("count", tczyDto.size());
        mapResult.put("dto", this.returnList(page, pageSize, tczyDto));
        return mapResult;
    }
    
  //通知统计按班级
    public Map<String, Object> tzcom(String communityId, String grade,String schooleId, String startTime, String endTime, int page, int pageSize) {
        Map<String, Object> mapResult = new HashMap<String, Object>();
        List<TzDto> tczyDto = new ArrayList<TzDto>();
        if(StringUtils.isNotBlank(schooleId)) {
            
            long startTimeL = 0;
            long endTimeL = 0;
            if (StringUtils.isNotBlank(startTime)) {
                Map<Integer, Long> map = this.getTimePointOneDay(startTime);
                startTimeL = map.get(1);
            }
            if (StringUtils.isNotBlank(endTime)) {
                Map<Integer, Long> map = this.getTimePointOneDay(endTime);
                endTimeL = map.get(7);
            }
            
            List<ObjectId> communityIdList = schoolCommunityDao.getCommunityIdsListBySchoolId(new ObjectId(schooleId));
            communityIdList = this.reList(communityIdList, grade);
            
            List<BasicDBObject> acList = appNoticeDao.count1(communityIdList, null, startTimeL, endTimeL,0,0);
            for (BasicDBObject db : acList) {
                TzDto t = new TzDto();
                ObjectId rid = ((BasicDBObject)db.get("_id")).getObjectId("cmId");
                t.setClassName(communityDao.findByObjectId(rid).getCommunityName());
                Integer count = (Integer)db.get("count");
                t.setFbNum(count);
                if(db.containsField("rl")) {
                    List<ObjectId> l =  (List<ObjectId>)db.get("rl");
                    if (CollectionUtils.isNotEmpty(l)) {
                        t.setPeoNum(l.size());
                    } else {
                        t.setPeoNum(0);
                    }
                } else {
                    t.setPeoNum(0);
                }
                List<AppNoticeEntry> appList = appNoticeDao.getWebAllDatePageByTimePage(rid, null, null, startTimeL, endTimeL, 0, 0);
                if (CollectionUtils.isNotEmpty(appList)) {
                    SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
                    t.setFbDate(s.format(new Date(appList.get(0).getSubmitTime())));
                    
                }
                tczyDto.add(t);
            }
        }
        Collections.sort(tczyDto, new Comparator<TzDto>() {

            @Override
            public int compare(TzDto o1, TzDto o2) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    if (sdf.parse(o1.getFbDate()).getTime() < sdf.parse(o2.getFbDate()).getTime()) {
                        return 1;
                    } else if (new Date(01).getTime() == new Date(02).getTime()) {
                        return 0;
                    } else {
                        return -1;
                    }
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    return 0;
                }
            }
            
        });
        
        mapResult.put("count", tczyDto.size());
        mapResult.put("dto", this.returnList(page, pageSize, tczyDto));
        return mapResult;
    }
    
    //兴趣社团图表
    public XqstDto xqsttb(String schooleId, String startTime, String endTime) throws Exception{
        XqstDto xqstDto = new XqstDto();
        List<String> dateList = new ArrayList<String>();
        if (startTime != null && endTime != null) {
            if(this.compareDate(startTime, endTime)) {
                dateList = this.getListDate(startTime, endTime);
                xqstDto.setDateList(dateList);
            } else {
                
            }
        }
        if(StringUtils.isNotBlank(schooleId)) {
            List<ObjectId> communityIdList = schoolCommunityDao.getCommunityIdsListBySchoolId(new ObjectId(schooleId));
            //所有评论数
            int allToatalCount = 0;
          //所有发帖数
            int allNum = 0;
            //int allZanCount = 0;
            for (String s : dateList) {
              //评论数
                int totalCount = 0;
              //点赞数
                int zanCount = 0;
                
                
                Map<Integer, Long> map = this.getTimePointOneDay(s);
                
                
                xqstDto.getNum().add(communityDetailDao.countTz(communityIdList,3, map.get(1), map.get(7)));
                List<CommunityDetailEntry> list = communityDetailDao.TzidList(communityIdList,3, map.get(1), map.get(7));
                allNum += list.size();
                xqstDto.setFtNum(list.size());
                for (CommunityDetailEntry entry : list) {
                  //评论数
                    totalCount += partInContentDao.countPartPartInContent(entry.getID());
                    //点赞数
                    zanCount += entry.getZanCount();
                    allToatalCount += totalCount;
            
                }
                xqstDto.setAllNum(allNum);
                xqstDto.setAllPlNum(allToatalCount);
      
                xqstDto.getPlNum().add(totalCount);
                xqstDto.getZanNum().add(zanCount);
                
                
            }
            
        }
        
        return xqstDto;
    }
    
  //兴趣社团统计
    public Map<String, Object> xqsttj(String schooleId, String startTime, String endTime,int page, int pageSize) throws Exception{
        Map<String, Object> mapp = new HashMap<String, Object>();
        List<XqstDto> ll = new ArrayList<XqstDto>();
        long startTimeL = 0;
        long endTimeL = 0;
        if (StringUtils.isNotBlank(startTime)) {
            Map<Integer, Long> map = this.getTimePointOneDay(startTime);
            startTimeL = map.get(1);
        }
        if (StringUtils.isNotBlank(endTime)) {
            Map<Integer, Long> map = this.getTimePointOneDay(endTime);
            endTimeL = map.get(7);
        }
       
        if(StringUtils.isNotBlank(schooleId)) {
            List<ObjectId> communityIdList = schoolCommunityDao.getCommunityIdsListBySchoolId(new ObjectId(schooleId));

              
             
  
            List<CommunityDetailEntry> list = communityDetailDao.TzidList(communityIdList,3, startTimeL, endTimeL);

            for (CommunityDetailEntry entry : list) {
                XqstDto xqstDto = new XqstDto();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                xqstDto.setDateStr(sdf.format(new Date(entry.getCreateTime())));
                xqstDto.setName(entry.getCommunityTitle());
                xqstDto.setUserName(userDao.findByUserId(entry.getCommunityUserId()).getNickName());
                xqstDto.setYueNum(entry.getYueCount());
                xqstDto.setPllNum(partInContentDao.countPartPartInContent(entry.getID()));
                xqstDto.setZannNum(entry.getZanCount());
                xqstDto.setSelfPlNum(partInContentDao.countPartPartInContent(entry.getID(), entry.getCommunityUserId()));
                ll.add(xqstDto);
            }
                
            mapp.put("num", list.size());
            mapp.put("communityDetail", this.returnList(page, pageSize, ll));
            
            
        }
        
        return mapp;
    }
    
    
  //小课堂图表
    public XktDto xkttb(String schooleId, String startTime, String endTime) throws Exception{
        long startTimeL = 0;
        long endTimeL = 0;
        if (StringUtils.isNotBlank(startTime)) {
            Map<Integer, Long> map = this.getTimePointOneDay(startTime);
            startTimeL = map.get(1);
        }
        if (StringUtils.isNotBlank(endTime)) {
            Map<Integer, Long> map = this.getTimePointOneDay(endTime);
            endTimeL = map.get(7);
        }
        XktDto xktDto = new XktDto();  
        if(StringUtils.isNotBlank(schooleId)) {
            List<ObjectId> communityIdList = schoolCommunityDao.getCommunityIdsListBySchoolId(new ObjectId(schooleId));
            List<SubjectClassDTO> subList = this.getSubjectClass1();
        
            for (SubjectClassDTO sct : subList) {
                xktDto.getSubjectList().add(sct.getName());
                
            }
            if (CollectionUtils.isNotEmpty(communityIdList)) {
                List<ObjectId> l = this.selectTeacherSubjectList2(communityIdList);
                if (CollectionUtils.isNotEmpty(l)) {
                    List<ObjectId> list = smallLessonDao.getEntrySize(l, startTimeL, endTimeL);
                    List<NewVersionSubjectEntry> nse = newVersionSubjectDao.getEntryByUid(list);
                    Map<ObjectId, List<ObjectId>> map = new HashMap<ObjectId, List<ObjectId>>() ;
                    for (NewVersionSubjectEntry n : nse) {
                        map.put(n.getUserId(), n.getSubjectList());
                    }
                    for (SubjectClassDTO sct : subList) {
                        int i = 0;
                        for (ObjectId o : list) {
                            List<ObjectId> ll = map.get(o);
                            if (ll != null) {
                                if (ll.contains(new ObjectId(sct.getId()))) {
                                    i++;
                                }
                            }
                            
                        }
                        xktDto.getNumList().add(i);
                    }
                }
                
            }
            
            
        }
        
        return xktDto;
    }
    
    /**
     * 查询社群下的老师id
     */
    public List<ObjectId> selectTeacherSubjectList2(List<ObjectId> communityIdList){
        return memberDao.getMembersByCid(communityIdList);
        
       
    }
    
    /**
     * 查询社群下的老师id
     */
    public List<ObjectId> selectTeacherSubjectList1(ObjectId communityIdList){
        return memberDao.getMembersByCid(communityIdList);
        
       
    }
    
    //小课堂按班级统计
    public Map<String, Object> xktbj(String schooleId,String grade, String startTime, String endTime,int page, int pageSize) throws Exception{
        Map<String, Object> mapp = new HashMap<String, Object>();
        long startTimeL = 0;
        long endTimeL = 0;
        if (StringUtils.isNotBlank(startTime)) {
            Map<Integer, Long> map = this.getTimePointOneDay(startTime);
            startTimeL = map.get(1);
        }
        if (StringUtils.isNotBlank(endTime)) {
            Map<Integer, Long> map = this.getTimePointOneDay(endTime);
            endTimeL = map.get(7);
        }
        List<XktDto> list = new ArrayList<XktDto>();
        if(StringUtils.isNotBlank(schooleId)) {
            List<ObjectId> communityIdList = schoolCommunityDao.getCommunityIdsListBySchoolId(new ObjectId(schooleId));
            communityIdList = this.reList(communityIdList, grade);
            for (ObjectId id : communityIdList) {
                XktDto xktDto = new XktDto();
                List<ObjectId> userList = this.selectTeacherSubjectList1(id);
                List<SmallLessonEntry> slist = smallLessonDao.getEntrySize1(userList, startTimeL, endTimeL);
                xktDto.setClassName(communityDao.findByObjectId(id).getCommunityName());
                xktDto.setClassTimes(slist.size());
                int t = 0;
                int num = 0;
                for (SmallLessonEntry e : slist) {
                    t += e.getNodeTime();
                    num += lessonUserResultDao.getNumber(e.getID());
                }
                if (slist.size()!=0) {
                    xktDto.setAvgClassTime(new BigDecimal(t).divide(new BigDecimal(slist.size()),2).intValue());
                } else {
                    xktDto.setAvgClassTime(0);
                }
                
                xktDto.setClassPerson(num);
                list.add(xktDto);
            }
            
        }
        mapp.put("list", this.returnList(page, pageSize, list));
        mapp.put("count", list.size());
        return mapp;
    }
    
  //小课堂按班级统计
    public Map<String, Object> xktjs(String schooleId, String grade, String startTime, String endTime,int page, int pageSize) throws Exception{
        Map<String, Object> mapp = new HashMap<String, Object>();
        long startTimeL = 0;
        long endTimeL = 0;
        if (StringUtils.isNotBlank(startTime)) {
            Map<Integer, Long> map = this.getTimePointOneDay(startTime);
            startTimeL = map.get(1);
        }
        if (StringUtils.isNotBlank(endTime)) {
            Map<Integer, Long> map = this.getTimePointOneDay(endTime);
            endTimeL = map.get(7);
        }
        List<XktDto> list = new ArrayList<XktDto>();
        if(StringUtils.isNotBlank(schooleId)) {
            List<ObjectId> communityIdList = schoolCommunityDao.getCommunityIdsListBySchoolId(new ObjectId(schooleId));
            communityIdList = this.reList(communityIdList, grade);
            List<ObjectId> l = this.selectTeacherSubjectList2(communityIdList);
            
            List<NewVersionSubjectEntry> nse = newVersionSubjectDao.getEntryByUid(l);
            Map<ObjectId, List<ObjectId>> map = new HashMap<ObjectId, List<ObjectId>>() ;
            for (NewVersionSubjectEntry n : nse) {
                map.put(n.getUserId(), n.getSubjectList());
            }
            
            List<BasicDBObject> acList = smallLessonDao.getEntryByUserId(l, startTimeL, endTimeL);
            for (BasicDBObject db : acList) {
                XktDto t = new XktDto();
                ObjectId rid = ((BasicDBObject)db.get("_id")).getObjectId("uid");
                t.setUserName(userDao.findByUserId(rid).getUserName());
                //t.setClassName(communityDao.findByObjectId(rid).getCommunityName());
                Integer count = (Integer)db.get("count");
                
                for (ObjectId o : communityIdList) {
                    List<ObjectId> ll = this.selectTeacherSubjectList1(o);
                    if (ll.contains(rid)) {
                        t.setClassName(communityDao.findByObjectId(o).getCommunityName());
                        break;
                    }
                }
                
                if (map.get(rid) != null) {
                    if (CollectionUtils.isNotEmpty(map.get(rid))) {
                        String s = "";
                        int sss = 0;
                        for (ObjectId oName : map.get(rid)) {
                            if (sss != (map.get(rid).size()-1)) {
                                s = s + subjectClassDao.getEntry(oName).getName()+"、";
                            } else {
                                s = s + subjectClassDao.getEntry(oName).getName();
                            }
                            
                            sss++;
                        }
                        t.setSubject(s);
                    }
                    
                }
                List<SmallLessonEntry> listtt = smallLessonDao.getEntrySize2(rid, startTimeL, endTimeL);
                
                int tt = 0;
                int num = 0;
                for (SmallLessonEntry e : listtt) {
                    tt += e.getNodeTime();
                    num += lessonUserResultDao.getNumber(e.getID());
                }
                if (listtt.size() != 0) {
                    t.setAvgClassTime(new BigDecimal(tt).divide(new BigDecimal(listtt.size()),2).intValue());
                } else {
                    t.setAvgClassTime(0);
                }
                
                t.setClassPerson(num);
                t.setClassTimes(listtt.size());
                
                list.add(t);
            }
            
        }
        mapp.put("list", this.returnList(page, pageSize, list));
        mapp.put("count", list.size());
        return mapp;
    }
    
    public List<?> returnList(int page, int pageSize, List<?> list) {
        int start = pageSize*(page -1);
        int end = start + pageSize;
        if (list.size() < start) {
            return null;
        } else if (list.size() >= start && list.size() < end) {
            return list.subList(start, list.size());
        } else {
            return list.subList(start, end);
        }
        
        
    }
}
