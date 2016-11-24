package com.fulaan.leave.service;

import com.db.leave.LeaveDao;
import com.db.leave.ReplaceCourseDao;
import com.db.zouban.CourseConfDao;
import com.db.zouban.TimeTableDao;
import com.db.zouban.ZouBanCourseDao;
import com.fulaan.leave.dto.LeaveDTO;
import com.fulaan.leave.dto.ReplaceCourseDTO;
import com.fulaan.leave.dto.TeacherInfo;
import com.fulaan.leave.dto.WeekTime;
import com.fulaan.user.service.UserService;
import com.fulaan.zouban.dto.CourseConfDTO;
import com.fulaan.zouban.service.PaikeService;
import com.fulaan.zouban.service.TermService;
import com.pojo.app.IdNameValuePairDTO;
import com.pojo.leave.LeaveEntry;
import com.pojo.leave.ReplaceCourse;
import com.pojo.leave.ReplyEnum;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.zouban.*;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by qiangm on 2016/3/1.
 */
@Service
public class LeaveService {
    private LeaveDao leaveDao = new LeaveDao();
    private ZouBanCourseDao zouBanCourseDao = new ZouBanCourseDao();
    private TimeTableDao timeTableDao = new TimeTableDao();
    private CourseConfDao courseConfDao = new CourseConfDao();
    private PaikeService paikeService = new PaikeService();
    private ReplaceCourseDao replaceCourseDao = new ReplaceCourseDao();
    @Autowired
    private UserService userService;
    private TermService termService = new TermService();

    //老师提交申请
    public ObjectId addTeacherLeave(String schoolId, String teacherId, String title, String content, String dateFrom, String dateEnd, int count) {
        ObjectId leaveId=null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LeaveEntry leaveEntry = null;
        String term = termService.getCurrentTerm(schoolId).get("term").toString();
        try {
            leaveEntry = new LeaveEntry(new ObjectId(teacherId), new ObjectId(schoolId), title, content, sdf.parse(dateFrom + ":00").getTime(),
                    sdf.parse(dateEnd + ":59").getTime(),
                    count, new Date().getTime(), ReplyEnum.UNDO.getIndex(), null,term);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (leaveEntry != null) {
            leaveId=leaveEntry.getID();
            leaveDao.addTeacherLeave(leaveEntry);
        }
        return leaveId;
    }

    //分页获取老师申请---根据老师id
    public List<LeaveDTO> findMyLeaveList(String userId, int page, int pageSize) {
        List<LeaveEntry> leaveEntries = leaveDao.findLeaveListByTeacherId(new ObjectId(userId), page, pageSize);
        List<LeaveDTO> leaveDTOList = new ArrayList<LeaveDTO>();
        //List<ObjectId> teacherIds=new ArrayList<ObjectId>();
        for (LeaveEntry leaveEntry : leaveEntries) {
            leaveDTOList.add(new LeaveDTO(leaveEntry));
            //teacherIds.add(leaveEntry.getTeacherId());
        }
        return leaveDTOList;
    }

    //获取详情
    public LeaveDTO findLeaveById(String leaveId) {
        LeaveEntry leaveEntry = leaveDao.findLeaveById(new ObjectId(leaveId));
        if (leaveEntry != null) {
            LeaveDTO leaveDTO = new LeaveDTO(leaveEntry);
            UserDetailInfoDTO ud = userService.getUserInfoById(leaveDTO.getUserId());
            leaveDTO.setUserName(ud.getUserName());
            return leaveDTO;
        }
        return null;
    }

    //统计个人请假数量
    public int countMyLeave(String teacherId) {
        return leaveDao.countMyLeave(new ObjectId(teacherId));
    }

    //查看全校的请假
    public List<LeaveDTO> findAllTeacherLeaveList(String schoolId, String teacher, int day, int type, int page, int pageSize,String term) {
        ObjectId teacherId = null;
        if (!StringUtils.isBlank(teacher)) {
            teacherId = new ObjectId(teacher);
        }
        Map<String, Long> map = converDayToDate(day);
        long dt1 = map.get("dt1");
        long dt2 = map.get("dt2");

        List<LeaveEntry> leaveEntries = leaveDao.findAllLeaves(new ObjectId(schoolId), teacherId, dt1, dt2, type, page, pageSize,term);
        List<LeaveDTO> leaveDTOList = new ArrayList<LeaveDTO>();
        List<ObjectId> teacherIds = new ArrayList<ObjectId>();
        for (LeaveEntry leaveEntry1 : leaveEntries) {
            leaveDTOList.add(new LeaveDTO(leaveEntry1));
            teacherIds.add(leaveEntry1.getTeacherId());
            if (leaveEntry1.getReplyPerson() != null) {
                teacherIds.add(leaveEntry1.getReplyPerson());
            }
        }
        List<UserDetailInfoDTO> userDetailInfoDTOList = userService.findUserInfoByIds(teacherIds);
        for (LeaveDTO leaveDTO : leaveDTOList) {
            for (UserDetailInfoDTO u : userDetailInfoDTOList) {
                if (u.getId().equals(leaveDTO.getUserId())) {
                    leaveDTO.setUserName(u.getUserName());
                }
                if (!StringUtils.isBlank(leaveDTO.getReplyPersonId()) && leaveDTO.getReplyPersonId() != null) {
                    if (u.getId().equals(leaveDTO.getReplyPersonId())) {
                        leaveDTO.setReplyPersonName(u.getUserName());
                    }
                }
            }
        }

        return leaveDTOList;
    }

    //统计全校的请假数量
    public int countAllTeacherLeaveCount(String schoolId, String teacher, int day, int type,String term) {
        ObjectId teacherId = null;
        if (!StringUtils.isBlank(teacher)) {
            teacherId = new ObjectId(teacher);
        }
        Map<String, Long> map = converDayToDate(day);
        long dt1 = map.get("dt1");
        long dt2 = map.get("dt2");

        return leaveDao.countAllLeave(new ObjectId(schoolId), teacherId, dt1, dt2, type,term);
    }

    public Map<String, Long> converDayToDate(int day) {
        Map<String, Long> map = new HashMap<String, Long>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (day == 0)//今天
            {
                String dt1 = df.format(new Date());
                map.put("dt1", df1.parse(dt1 + " 00:00:00").getTime());
                String dt2 = df.format(new Date());
                map.put("dt2", df1.parse(dt2 + " 23:59:59").getTime());
            } else if (day == 1)//本周
            {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //获取本周一的日期
                map.put("dt1", df1.parse(df.format(cal.getTime()) + " 00:00:00").getTime());
                cal.add(Calendar.WEEK_OF_YEAR, 1);//本周日的日期
                map.put("dt2", df1.parse(df.format(cal.getTime()) + " 23:59:59").getTime());
            } else if (day == 2)//本月
            {
                Calendar calendar = Calendar.getInstance();
                Date theDate = calendar.getTime();
                GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
                gcLast.setTime(theDate);
                gcLast.set(Calendar.DAY_OF_MONTH, 1);
                map.put("dt1", df1.parse(df.format(gcLast.getTime()) + " 00:00:00").getTime());
                calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
                theDate = calendar.getTime();
                map.put("dt2", df1.parse(df.format(theDate.getTime()) + " 23:59:59").getTime());
            } else if (day == 3)//本学期 //todo
            {
                map.put("dt1", 1234567890L);
                map.put("dt2", 1234567890L);
            } else if (day == 4)//全部
            {
                map.put("dt1", df1.parse("2016-01-01 00:00:00").getTime());
                Date date = new Date();
                map.put("dt2", date.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return map;
    }

    public Map<String,Object> getReplaceList(String userId,String term,int page,int pageSize,String schoolId)
    {
        ObjectId teacherId=null;
        if(!StringUtils.isBlank(userId))
            teacherId=new ObjectId(userId);
        int count=replaceCourseDao.countReplaceList(new ObjectId(schoolId),teacherId,term);
        List<ReplaceCourse> replaceList=replaceCourseDao.findReplaceList(new ObjectId(schoolId),page,pageSize, teacherId, term);
        List<ReplaceCourseDTO> list=new ArrayList<ReplaceCourseDTO>();

        List<ObjectId> teacherIds=new ArrayList<ObjectId>();
        for (ReplaceCourse replaceCourse:replaceList)
        {
            teacherIds.add(replaceCourse.getTeacherId());
            teacherIds.add(replaceCourse.getOldTeacherId());
            teacherIds.add(replaceCourse.getManagerId());
            list.add(new ReplaceCourseDTO(replaceCourse));
        }
        List<UserDetailInfoDTO> userDetailInfoDTOList=userService.findUserInfoByIds(teacherIds);
        for (ReplaceCourseDTO rd:list)
        {
            for (UserDetailInfoDTO ud:userDetailInfoDTOList)
            {
                if(ud.getId().equals(rd.getTeacherId()))
                {
                    rd.setTeacherName(ud.getUserName());
                }
                if(ud.getId().equals(rd.getOldTeacherId()))
                {
                    rd.setOldTeacherName(ud.getUserName());
                }
                if(ud.getId().equals(rd.getManagerId()))
                {
                    rd.setManagerName(ud.getUserName());
                }
            }
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日HH:mm");
            long time=new ObjectId(rd.getId()).getTime();
            rd.setTime(sdf.format(new Date(time)));
        }
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("total",count);
        map.put("list",list);
        map.put("page",page);
        map.put("pageSize",pageSize);
        return map;
    }
    //管理员回复请假
    public void rejectLeave(String leaveId, int replyType, String userId) {
        if (replyType == ReplyEnum.REJECT.getIndex())
            leaveDao.updateLeave(new ObjectId(leaveId), replyType, new ObjectId(userId));
    }

    //根据日期计算该段时间内老师共有多少节课
    public Map<String, Object> calClass(String schoolId, String dateFrom, String dateEnd, String teacherId) {
        //计算处于第几周，以及星期几
        Map<String, Object> result = new HashMap<String, Object>();
        List<WeekTime> weekTimes = new ArrayList<WeekTime>();
        Map<String, Object> map = termService.getCurrentTerm(schoolId);
        String year = map.get("year").toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = sdf.parse(dateFrom);
            date2 = sdf.parse(dateEnd);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int startWeek = termService.findWeekIndexByDate(schoolId, year, date1);
        int endWeek = termService.findWeekIndexByDate(schoolId, year, date2);
        List<Integer> weekList = new ArrayList<Integer>();
        for (int i = startWeek; i <= endWeek; i++) {
            weekList.add(i);
        }
        int startDayOfWeek = termService.getWeekOfDate(date1);
        int endDayOfWeek = termService.getWeekOfDate(date2);
        String term = map.get("term").toString();
        List<ZouBanCourseEntry> zouBanCourseEntries = zouBanCourseDao.findCourseListByTeacherId(term, new ObjectId(teacherId),false);
        int classCount = 0;
        List<ObjectId> courseIds = new ArrayList<ObjectId>();
        Map<ObjectId, String> courseNameMap = new HashMap<ObjectId, String>();
        for (ZouBanCourseEntry z : zouBanCourseEntries) {
            courseIds.add(z.getID());
            courseNameMap.put(z.getID(), z.getClassName());
        }
        List<TimeTableEntry> timeTableEntryList = timeTableDao.findAllTimeTableWeeks(term, new ObjectId(schoolId), 0, startWeek, endWeek);
        for (TimeTableEntry t : timeTableEntryList) {
            if (t.getWeek() == startWeek && t.getWeek() == endWeek) {
                for (CourseItem courseItem : t.getCourseList()) {
                    if (courseItem.getXIndex() >= startDayOfWeek && courseItem.getXIndex() <= endDayOfWeek) {
                        if (courseIds.contains(courseItem.getCourse().get(0))) {
                            boolean have = false;
                            //过滤重复数据
                            for (WeekTime wk : weekTimes) {
                                if (wk.getWeek() == t.getWeek() && wk.getX() == courseItem.getXIndex() && wk.getY() == courseItem.getYIndex()) {
                                    have = true;
                                    break;
                                }
                            }
                            if (!have) {
                                weekTimes.add(new WeekTime(t.getWeek(), courseItem.getXIndex(), courseItem.getYIndex(), courseItem.getCourse().get(0).toString(),
                                        courseNameMap.get(courseItem.getCourse().get(0))));
                                classCount += 1;
                            }
                        }
                    }
                }
            } else if (t.getWeek() == startWeek) {
                for (CourseItem courseItem : t.getCourseList()) {
                    if (courseItem.getXIndex() >= startDayOfWeek) {
                        if (courseIds.contains(courseItem.getCourse().get(0))) {
                            boolean have = false;
                            //过滤重复数据
                            for (WeekTime wk : weekTimes) {
                                if (wk.getWeek() == t.getWeek() && wk.getX() == courseItem.getXIndex() && wk.getY() == courseItem.getYIndex()) {
                                    have = true;
                                    break;
                                }
                            }
                            if (!have) {
                                weekTimes.add(new WeekTime(t.getWeek(), courseItem.getXIndex(), courseItem.getYIndex(), courseItem.getCourse().get(0).toString(),
                                        courseNameMap.get(courseItem.getCourse().get(0))));
                                classCount += 1;
                            }
                        }
                    }
                }
            } else if (t.getWeek() == endWeek) {
                for (CourseItem courseItem : t.getCourseList()) {
                    if (courseItem.getXIndex() <= endDayOfWeek) {
                        if (courseIds.contains(courseItem.getCourse().get(0))) {
                            boolean have = false;
                            //过滤重复数据
                            for (WeekTime wk : weekTimes) {
                                if (wk.getWeek() == t.getWeek() && wk.getX() == courseItem.getXIndex() && wk.getY() == courseItem.getYIndex()) {
                                    have = true;
                                    break;
                                }
                            }
                            if (!have) {
                                weekTimes.add(new WeekTime(t.getWeek(), courseItem.getXIndex(), courseItem.getYIndex(), courseItem.getCourse().get(0).toString(),
                                        courseNameMap.get(courseItem.getCourse().get(0))));
                                classCount += 1;
                            }
                        }
                    }
                }
            } else {
                for (CourseItem courseItem : t.getCourseList()) {
                    if (courseIds.contains(courseItem.getCourse().get(0))) {
                        boolean have = false;
                        //过滤重复数据
                        for (WeekTime wk : weekTimes) {
                            if (wk.getWeek() == t.getWeek() && wk.getX() == courseItem.getXIndex() && wk.getY() == courseItem.getYIndex()) {
                                have = true;
                                break;
                            }
                        }
                        if (!have) {
                            weekTimes.add(new WeekTime(t.getWeek(), courseItem.getXIndex(), courseItem.getYIndex(), courseItem.getCourse().get(0).toString(),
                                    courseNameMap.get(courseItem.getCourse().get(0))));
                            classCount += 1;
                        }
                    }
                }
            }
        }
        result.put("all", classCount);
        result.put("course", weekTimes);
        result.put("week", weekList);
        return result;
    }

    //获取审核时的详细内容
    public Map<String, Object> getLeaveCourseDetail(String leaveId) {
        Map<String, Object> map = new HashMap<String, Object>();
        LeaveEntry leaveEntry = leaveDao.findLeaveById(new ObjectId(leaveId));
        LeaveDTO leaveDTO = null;
        if (leaveEntry != null) {
            leaveDTO = new LeaveDTO(leaveEntry);
            UserDetailInfoDTO ud = userService.getUserInfoById(leaveDTO.getUserId());
            leaveDTO.setUserName(ud.getUserName());
        }
        map.put("leave", leaveDTO);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateFrom = sdf.format(new Date(leaveDTO.getDate1()));
        String dateEnd = sdf.format(new Date(leaveDTO.getDate2()));
        Map<String, Object> calMap = calClass(leaveDTO.getSchoolId(), dateFrom, dateEnd, leaveDTO.getUserId());
        map.put("week", calMap.get("week"));
        map.put("course", calMap.get("course"));
        return map;
    }

    //获取课表配置
    public CourseConfDTO getCourseConf(String schoolId) {
        String term = termService.getCurrentTerm(schoolId).get("term").toString();
        List<CourseConfEntry> courseConfEntries = courseConfDao.findCourseConfsBySchoolId(term, new ObjectId(schoolId));
        if (courseConfEntries.size() == 0) {
            List<Integer> classDays = new ArrayList<Integer>() {{
                add(1);
                add(2);
                add(3);
                add(4);
                add(5);
            }};
            List<String> classTime = new ArrayList<String>() {{
                add("8:00~8:45");
                add("8:55~9:40");
                add("10:00~10:45");
                add("10:55~11:40");
                add("14:00~14:45");
                add("14:55~15:40");
                add("16:00~16:45");
                add("16:55~17:40");
                add("17:50~18:35");
                add("18:45~19:30");
            }};
            CourseConfEntry courseConfEntry = new CourseConfEntry(new ObjectId(), "2015-2016学年第一学期", new ObjectId(),
                    classDays, 10, classTime, new ArrayList<CourseEvent>());
            courseConfDao.addCourseConf(courseConfEntry);
            return new CourseConfDTO(courseConfEntry);
        }
        return new CourseConfDTO(courseConfEntries.get(0));
    }

    //获取可以代课的老师列表
    public List<TeacherInfo> getAvailableTeacherList(String schoolId, int week, int x, int y, String courseName) {
        //先获取该学科的全部老师
        List<TeacherInfo> teacherList = new ArrayList<TeacherInfo>();
        Map<IdNameValuePairDTO, Set<IdNameValuePairDTO>> subjectTeacherMap = paikeService.findTeacherListBySchool(new ObjectId(schoolId));
        for (Map.Entry entry : subjectTeacherMap.entrySet()) {
            if (((IdNameValuePairDTO) entry.getKey()).getValue().toString().equals(courseName)) {
                Set<IdNameValuePairDTO> set = (Set<IdNameValuePairDTO>) entry.getValue();
                for (IdNameValuePairDTO idNameValuePairDTO : set) {
                    teacherList.add(new TeacherInfo(idNameValuePairDTO.getIdStr(),idNameValuePairDTO.getValue().toString()));
                }
                break;
            }
        }
        //获取全校课表
        String term = termService.getCurrentTerm(schoolId).get("term").toString();
        List<TimeTableEntry> timeTableEntryList = timeTableDao.findAllTimeTable(term, new ObjectId(schoolId), 0, week);
        List<ObjectId> courseIds = new ArrayList<ObjectId>();
        for (TimeTableEntry t : timeTableEntryList) {
            for (CourseItem ci : t.getCourseList()) {
                if (ci.getXIndex() == x && ci.getYIndex() == y) {
                    courseIds.add(ci.getCourse().get(0));
                }
            }
        }
        //通过courseIds查询课程详情
        List<ObjectId> hasCourseTeachers = new ArrayList<ObjectId>();//当前有课的老师列表
        List<ZouBanCourseEntry> zouBanCourseEntries = zouBanCourseDao.findCourseListByIds(courseIds);
        for (ZouBanCourseEntry z : zouBanCourseEntries) {
            hasCourseTeachers.add(z.getTeacherId());
        }
        //从全部老师列表中去除有课的
        for (ObjectId tid : hasCourseTeachers) {
            for (TeacherInfo idNameValuePairDTO : teacherList) {
                if (tid.equals( new ObjectId(idNameValuePairDTO.getId().toString()))) {
                    teacherList.remove(idNameValuePairDTO);
                    break;
                }
            }
        }
        return teacherList;
    }

    //确认代课
    public String replaceCourse(String schoolId,String teacherId,String oldTeacherId,String courseId,String courseName,int week,int x,int y,String userId,
                                String leaveId)
    {
        String term = termService.getCurrentTerm(schoolId).get("term").toString();
        ReplaceCourse replaceCourse=new ReplaceCourse(new ObjectId(schoolId),new ObjectId(teacherId),new ObjectId(courseId),courseName,week,x,y,
                new ObjectId(oldTeacherId),new ObjectId(userId),term,new ObjectId(leaveId));
        replaceCourseDao.addReplaceCourse(replaceCourse);
        return replaceCourse.getID().toString();
    }
    //同意请假
    public void agreeLeave(String replaceIds,String leaveId,String userId)
    {
        if(StringUtils.isBlank(replaceIds))
        {
            //修改请假条回复状态
            leaveDao.updateLeave(new ObjectId(leaveId), ReplyEnum.AGREE.getIndex(), new ObjectId(userId));
        }
        else {
            String[] replaceIdArr = replaceIds.split(",");
            List<ObjectId> replaceIdList = new ArrayList<ObjectId>();
            for (String str : replaceIdArr) {
                replaceIdList.add(new ObjectId(str));
            }
            replaceCourseDao.removeUnusedReplaceList(replaceIdList, new ObjectId(leaveId));
            List<ReplaceCourse> replaceCourses = replaceCourseDao.findByIds(replaceIdList);
            List<ObjectId> zoubanCourseIds = new ArrayList<ObjectId>();
            List<ObjectId> teacherIds = new ArrayList<ObjectId>();
            for (ReplaceCourse rp : replaceCourses) {
                zoubanCourseIds.add(rp.getCourseId());
                teacherIds.add(rp.getTeacherId());
            }
            List<ZouBanCourseEntry> zouBanCourseEntries = zouBanCourseDao.findCourseListByIds(zoubanCourseIds);
            List<UserDetailInfoDTO> userDetailInfoDTOs = userService.findUserInfoByIds(teacherIds);
            for (ReplaceCourse rp : replaceCourses) {
                ObjectId classId = null;
                ObjectId subjectId = null;
                ObjectId gradeId = null;
                List<ObjectId> studentIds = new ArrayList<ObjectId>();
                String teacherName = "";
                for (ZouBanCourseEntry z : zouBanCourseEntries) {
                    if (z.getID().equals(rp.getCourseId())) {
                        classId = z.getClassId().get(0);
                        subjectId = z.getSubjectId();
                        gradeId = z.getGradeId();
                        studentIds = z.getStudentList();
                        break;
                    }
                }
                for (UserDetailInfoDTO ud : userDetailInfoDTOs) {
                    if (ud.getId().equals(rp.getTeacherId().toString())) {
                        teacherName = ud.getUserName();
                    }
                }
                List<ObjectId> classIdList = new ArrayList<ObjectId>();
                classIdList.add(classId);
                ZouBanCourseEntry zouBanCourseEntry = new ZouBanCourseEntry(rp.getSchoolId(), subjectId, rp.getTerm(), gradeId,
                        classIdList, null, rp.getCourseName(), 1, rp.getTeacherId(), teacherName, null, studentIds, 2, null);
                zouBanCourseDao.addZouBanCourseEntry(zouBanCourseEntry);
                //修改课表
                timeTableDao.updateTable(rp.getTerm(), rp.getSchoolId(), classId, rp.getWeek(), 0, zouBanCourseEntry.getID(), rp.getCourseId());
            }
            //修改请假条回复状态
            leaveDao.updateLeave(new ObjectId(leaveId), ReplyEnum.AGREE.getIndex(), new ObjectId(userId));
        }
    }
    //放弃代课，删除所有代课
    public void removeReplace(String replaceIds)
    {
        String[] repIdArr=replaceIds.split(",");
        List<ObjectId> repIdList=new ArrayList<ObjectId>();
        for (String str:repIdArr)
        {
            repIdList.add(new ObjectId(str));
        }
        replaceCourseDao.removeReplaceList(repIdList);
    }

    //获取个人代课记录
    public List<ReplaceCourseDTO> findMyReplace(String schoolId,String userId,int page,int pageSize,String term)
    {
        List<ReplaceCourse> replaceCourseList=replaceCourseDao.findMyReplaceList(new ObjectId(userId),term,page,pageSize);
        List<ReplaceCourseDTO> replaceCourseDTOList=new ArrayList<ReplaceCourseDTO>();
        List<ObjectId> teacherIds=new ArrayList<ObjectId>();
        for (ReplaceCourse replaceCourse:replaceCourseList)
        {
            teacherIds.add(replaceCourse.getTeacherId());
            teacherIds.add(replaceCourse.getOldTeacherId());
            teacherIds.add(replaceCourse.getManagerId());
            replaceCourseDTOList.add(new ReplaceCourseDTO(replaceCourse));
        }
        List<UserDetailInfoDTO> userDetailInfoDTOList=userService.findUserInfoByIds(teacherIds);
        for (ReplaceCourseDTO rd:replaceCourseDTOList)
        {
            for (UserDetailInfoDTO ud:userDetailInfoDTOList)
            {
                if(ud.getId().equals(rd.getTeacherId()))
                {
                    rd.setTeacherName(ud.getUserName());
                }
                if(ud.getId().equals(rd.getOldTeacherId()))
                {
                    rd.setOldTeacherName(ud.getUserName());
                }
                if(ud.getId().equals(rd.getManagerId()))
                {
                    rd.setManagerName(ud.getUserName());
                }
            }
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日HH:mm");
            long time=new ObjectId(rd.getId()).getTime();
            rd.setTime(sdf.format(new Date(time)));
        }
        return replaceCourseDTOList;
    }
    //统计我的代课
    public int countMyLeave(String schoolId,String term)
    {
        return replaceCourseDao.countMyReplace(new ObjectId(schoolId),term);
    }
    //删除我的请假
    public Map<String,Object> removeMyLeave(String leaveId)
    {
        Map<String,Object> map=new HashMap<String, Object>();
        LeaveEntry leaveEntry=leaveDao.findLeaveById(new ObjectId(leaveId));
        if(leaveEntry.getReply()==1)
        {
            map.put("code", "400");
        }
        else {
            leaveDao.removeMyLeave(new ObjectId(leaveId));
            map.put("code", "200");
        }
        return map;
    }
}
