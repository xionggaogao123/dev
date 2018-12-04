package com.fulaan.count.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.backstage.TeacherApproveDao;
import com.db.controlphone.AppTsDao;
import com.db.excellentCourses.ExcellentCoursesDao;
import com.db.excellentCourses.HourClassDao;
import com.db.fcommunity.CommunityDetailDao;
import com.db.fcommunity.CommunityHyDao;
import com.db.fcommunity.GroupDao;
import com.db.fcommunity.MemberDao;
import com.db.fcommunity.NewVersionCommunityBindDao;
import com.db.jiaschool.HomeSchoolDao;
import com.db.jiaschool.SchoolCommunityDao;
import com.db.operation.AppCommentDao;
import com.db.operation.AppNoticeDao;
import com.db.user.UserDao;
import com.fulaan.count.dto.JxmCountDto;
import com.fulaan.jiaschool.dto.HomeSchoolDTO;
import com.fulaan.jiaschool.service.HomeSchoolService;
import com.fulaan.operation.service.AppCommentService;
import com.pojo.fcommunity.CommunityHyEntry;
import com.pojo.jiaschool.HomeSchoolEntry;
import com.pojo.operation.AppCommentEntry;

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

    /**
     * 
     *〈简述〉家校美统计
     *〈详细描述〉
     * @author Administrator
     * @param schooleId
     * @return
     */
    public JxmCountDto jxmCount(String schooleId) {
        JxmCountDto jxmCountDto = new JxmCountDto();
        if (schooleId != null) {
            //社区数
            List<ObjectId> communityIdList = schoolCommunityDao.getCommunityIdsListBySchoolId(new ObjectId(schooleId));
            jxmCountDto.setCommunityCount(communityIdList.size());
            //通过社区id查找群组
            List<ObjectId> groupIdList = groupDao.getCommunitysIdsList(communityIdList);
            //成员id
            List<ObjectId> memberList = memberDao.getAllGroupIdsMembers(groupIdList);
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
            zuoyeNum.add(zuoye1);
            zuoyeNum.add(zuoye2);
            zuoyeNum.add(zuoye3);
            zuoyeNum.add(zuoye4);
            zuoyeNum.add(zuoye5);
            zuoyeNum.add(zuoye6);
            jxmCountDto.setZuoyeNum(zuoyeNum);
            //本日通知发布数量
            List<Integer> noticeNum = new ArrayList<Integer>();
            int notice1 = appNoticeDao.countAppNoticeEntries(communityIdList,map.get(1), map.get(2));
            int notice2 = appNoticeDao.countAppNoticeEntries(communityIdList,map.get(2), map.get(3));
            int notice3 = appNoticeDao.countAppNoticeEntries(communityIdList,map.get(3), map.get(4));
            int notice4 = appNoticeDao.countAppNoticeEntries(communityIdList,map.get(4), map.get(5));
            int notice5 = appNoticeDao.countAppNoticeEntries(communityIdList,map.get(5), map.get(6));
            int notice6 = appNoticeDao.countAppNoticeEntries(communityIdList,map.get(6), map.get(7));
            noticeNum.add(notice1);
            noticeNum.add(notice2);
            noticeNum.add(notice3);
            noticeNum.add(notice4);
            noticeNum.add(notice5);
            noticeNum.add(notice6);
            jxmCountDto.setNoticeNum(noticeNum);
            //本日帖子发布数量
            List<Integer> tzNum = new ArrayList<Integer>();
            int tz1 = communityDetailDao.countTz(communityIdList,3, map.get(1), map.get(2));
            int tz2 = communityDetailDao.countTz(communityIdList,3, map.get(2), map.get(3));
            int tz3 = communityDetailDao.countTz(communityIdList,3, map.get(3), map.get(4));
            int tz4 = communityDetailDao.countTz(communityIdList,3, map.get(4), map.get(5));
            int tz5 = communityDetailDao.countTz(communityIdList,3, map.get(5), map.get(6));
            int tz6 = communityDetailDao.countTz(communityIdList,3, map.get(6), map.get(7));
            tzNum.add(tz1);
            tzNum.add(tz2);
            tzNum.add(tz3);
            tzNum.add(tz4);
            tzNum.add(tz5);
            tzNum.add(tz6);
            jxmCountDto.setTzNum(tzNum);
            //本日社群活跃数量
            List<Integer> hyNum = new ArrayList<Integer>();
            List<CommunityHyEntry> list1 = communityHyDao.communityHyCountEntry(communityIdList,map.get(1), map.get(2));
            List<CommunityHyEntry> list2 = communityHyDao.communityHyCountEntry(communityIdList,map.get(2), map.get(3));
            List<CommunityHyEntry> list3 = communityHyDao.communityHyCountEntry(communityIdList,map.get(3), map.get(4));
            List<CommunityHyEntry> list4 = communityHyDao.communityHyCountEntry(communityIdList,map.get(4), map.get(5));
            List<CommunityHyEntry> list5 = communityHyDao.communityHyCountEntry(communityIdList,map.get(5), map.get(6));
            List<CommunityHyEntry> list6 = communityHyDao.communityHyCountEntry(communityIdList,map.get(6), map.get(7));
            hyNum.add(listToSet(list1));
            hyNum.add(listToSet(list2));
            hyNum.add(listToSet(list3));
            hyNum.add(listToSet(list4));
            hyNum.add(listToSet(list5));
            hyNum.add(listToSet(list6));
            jxmCountDto.setHyNum(hyNum);
            //本日直播课数量
            List<Integer> zbNum = new ArrayList<Integer>();
            zbNum.add(getzbNum(communityIdList,map.get(1), map.get(2)));
            zbNum.add(getzbNum(communityIdList,map.get(2), map.get(3)));
            zbNum.add(getzbNum(communityIdList,map.get(3), map.get(4)));
            zbNum.add(getzbNum(communityIdList,map.get(4), map.get(5)));
            zbNum.add(getzbNum(communityIdList,map.get(5), map.get(6)));
            zbNum.add(getzbNum(communityIdList,map.get(6), map.get(7)));
            jxmCountDto.setZbNum(zbNum);
            //本日推荐引用数量
            List<Integer> yyNum = new ArrayList<Integer>();
            int yy1 = appTsDao.tsCount(communityIdList,map.get(1), map.get(2));
            int yy2 = appTsDao.tsCount(communityIdList,map.get(2), map.get(3));
            int yy3 = appTsDao.tsCount(communityIdList,map.get(3), map.get(4));
            int yy4 = appTsDao.tsCount(communityIdList,map.get(4), map.get(5));
            int yy5 = appTsDao.tsCount(communityIdList,map.get(5), map.get(6));
            int yy6 = appTsDao.tsCount(communityIdList,map.get(6), map.get(7));
            yyNum.add(yy1);
            yyNum.add(yy2);
            yyNum.add(yy3);
            yyNum.add(yy4);
            yyNum.add(yy5);
            yyNum.add(yy6);
            jxmCountDto.setYyNum(yyNum);
        }
        
        return jxmCountDto;
    }
    
    //作业图表
    public void zytb(String schooleId, String startTime, String endTime) {
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
            List<AppCommentEntry> list = appCommentDao.getWebAllDatePageByTime(communityIdList, null, startTimeL, endTimeL);
        }
        
    }
    
    public int getzbNum(List<ObjectId> communityIdList, long  startTime,long endTime) {
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
    
    
}
