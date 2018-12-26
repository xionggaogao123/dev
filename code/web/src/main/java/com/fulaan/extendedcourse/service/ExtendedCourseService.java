package com.fulaan.extendedcourse.service;

import com.db.backstage.TeacherApproveDao;
import com.db.extendedcourse.*;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.MemberDao;
import com.db.fcommunity.MineCommunityDao;
import com.db.fcommunity.NewVersionCommunityBindDao;
import com.db.indexPage.IndexContentDao;
import com.db.indexPage.IndexPageDao;
import com.db.jiaschool.HomeSchoolDao;
import com.db.jiaschool.SchoolCommunityDao;
import com.db.jiaschool.SchoolPersionDao;
import com.db.user.NewVersionBindRelationDao;
import com.db.user.NewVersionUserRoleDao;
import com.db.user.UserDao;
import com.fulaan.backstage.service.BackStageService;
import com.fulaan.community.dto.CommunityDTO;
import com.fulaan.extendedcourse.dto.ExtendedCourseDTO;
import com.fulaan.extendedcourse.dto.ExtendedSchoolLabelDTO;
import com.fulaan.extendedcourse.dto.ExtendedSchoolTeacherDTO;
import com.fulaan.indexpage.dto.IndexContentDTO;
import com.fulaan.indexpage.dto.IndexPageDTO;
import com.fulaan.instantmessage.service.RedDotService;
import com.fulaan.pojo.User;
import com.fulaan.systemMessage.service.SystemMessageService;
import com.fulaan.utils.HSSFUtils;
import com.mongodb.DBObject;
import com.pojo.extendedcourse.*;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.fcommunity.MineCommunityEntry;
import com.pojo.fcommunity.NewVersionCommunityBindEntry;
import com.pojo.indexPage.IndexContentEntry;
import com.pojo.indexPage.IndexPageEntry;
import com.pojo.instantmessage.ApplyTypeEn;
import com.pojo.jiaschool.HomeSchoolEntry;
import com.pojo.jiaschool.SchoolCommunityEntry;
import com.pojo.jiaschool.SchoolPersionEntry;
import com.pojo.newVersionGrade.CommunityType;
import com.pojo.user.NewVersionBindRelationEntry;
import com.pojo.user.NewVersionUserRoleEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.DateTimeUtils;
import com.sys.utils.MD5Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by James on 2018-12-06.
 */
@Service
public class ExtendedCourseService {

    private ExtendedSchoolSettingDao extendedSchoolSettingDao = new ExtendedSchoolSettingDao();

    private ExtendedSchoolLabelDao extendedSchoolLabelDao = new ExtendedSchoolLabelDao();

    private ExtendedCourseDao extendedCourseDao = new ExtendedCourseDao();

    private ExtendedUserApplyDao extendedUserApplyDao = new ExtendedUserApplyDao();

    private ExtendedCourseClassDao extendedCourseClassDao =new ExtendedCourseClassDao();

    private ExtendedSchoolTeacherDao extendedSchoolTeacherDao = new ExtendedSchoolTeacherDao();

    private SchoolCommunityDao schoolCommunityDao = new SchoolCommunityDao();

    private CommunityDao communityDao = new CommunityDao();
    private TeacherApproveDao teacherApproveDao = new TeacherApproveDao();

    private UserDao userDao = new UserDao();

    private NewVersionBindRelationDao newVersionBindRelationDao = new NewVersionBindRelationDao();

    private NewVersionUserRoleDao newVersionUserRoleDao = new  NewVersionUserRoleDao();

    private MineCommunityDao mineCommunityDao = new MineCommunityDao();

    private NewVersionCommunityBindDao newVersionCommunityBindDao = new NewVersionCommunityBindDao();

    private BackStageService backStageService = new BackStageService();

    private IndexPageDao indexPageDao = new IndexPageDao();

    private IndexContentDao indexContentDao = new IndexContentDao();

    private SchoolPersionDao schoolPersionDao = new SchoolPersionDao();

    private HomeSchoolDao homeSchoolDao = new HomeSchoolDao();

    private MemberDao memberDao = new MemberDao();
    @Autowired
    private SystemMessageService systemMessageService;
    @Autowired
    private RedDotService redDotService;

    private static final String[] gradeString = new String[]{"","一年级","二年级","三年级","四年级","五年级","六年级","初一","初二","初三","高一","高二","高三"};

    private static final String[] weekString = new String[]{"","周一","周二","三年级","四年级","五年级","六年级","初一","初二","初三","高一","高二","高三"};

    /**
     * 保存学校拓展课设置(开课类型)
     */
    public void  saveExtendedSchoolSettingEntry(ObjectId schoolId,int courseType){
        ExtendedSchoolSettingEntry extendedSchoolSettingEntry = extendedSchoolSettingDao.getEntryBySchoolId(schoolId);
        if(extendedSchoolSettingEntry==null){
            ExtendedSchoolSettingEntry entry = new ExtendedSchoolSettingEntry(schoolId,courseType);
            extendedSchoolSettingDao.saveEntry(entry);
        }else{
            extendedSchoolSettingEntry.setCourseType(courseType);
            extendedSchoolSettingDao.saveEntry(extendedSchoolSettingEntry);
        }
    }

    /**
     * 获得学校设置
     * @param schoolId
     */
    public String  getExtendedSchoolSetting(ObjectId schoolId){
        ExtendedSchoolSettingEntry extendedSchoolSettingEntry = extendedSchoolSettingDao.getEntryBySchoolId(schoolId);
        if(extendedSchoolSettingEntry==null){
            return "1";
        }else{
            return  extendedSchoolSettingEntry.getCourseType()+"";
        }
    }

    /**
     * 删除课程标签
     */
    public  void deleteExtendedSchoolLabel(ObjectId id){
        extendedSchoolLabelDao.delEntryById(id);
    }

    /**
     * 删除课程标签
     */
    public  void deleteExtendedSchoolTeacher(ObjectId id){
        extendedSchoolTeacherDao.delEntryById(id);
    }

    /**
     * 添加课程标签
     */
    public  String  addExtendedSchoolLabel(ObjectId schoolId,ObjectId userId,String name){
        ExtendedSchoolLabelEntry extendedSchoolLabelEntry = extendedSchoolLabelDao.getEntryBySchoolIdAndName(schoolId,name);
        if(extendedSchoolLabelEntry==null){
            ExtendedSchoolLabelEntry entry = new ExtendedSchoolLabelEntry(schoolId,userId,name);
            extendedSchoolLabelDao.saveEntry(entry);
        }else{
            return "标签已存在，不可重复添加";
        }
        return "添加成功";
    }

    /**
     * 添加课程标签
     */
    public  String  addExtendedSchoolTeacher(ObjectId schoolId,ObjectId userId,String name){
        ExtendedSchoolTeacherEntry extendedSchoolTeacherEntry = extendedSchoolTeacherDao.getEntryBySchoolIdAndName(schoolId,name);
        if(extendedSchoolTeacherEntry==null){
            ExtendedSchoolTeacherEntry entry = new ExtendedSchoolTeacherEntry(schoolId,name,null,userId);
            extendedSchoolTeacherDao.saveEntry(entry);
        }else{
            return "标签已存在，不可重复添加";
        }
        return "添加成功";
    }


    /**
     * 查询课程标签列表
     */
    public  List<ExtendedSchoolLabelDTO> selectExtendedSchoolLabelList(ObjectId schoolId){
        List<ExtendedSchoolLabelDTO> dtos = new ArrayList<ExtendedSchoolLabelDTO>();
        List<ExtendedSchoolLabelEntry> extendedSchoolLabelEntries =  extendedSchoolLabelDao.getListBySchoolId(schoolId);
        for(ExtendedSchoolLabelEntry entry :extendedSchoolLabelEntries){
            ExtendedSchoolLabelDTO dto = new ExtendedSchoolLabelDTO(entry);
            dtos.add(dto);
        }
        return dtos;
    }

    /**
     * 查询课程老师列表
     */
    public  List<ExtendedSchoolTeacherDTO> selectExtendedSchoolTeacherList(ObjectId schoolId){
        List<ExtendedSchoolTeacherDTO> dtos = new ArrayList<ExtendedSchoolTeacherDTO>();
        List<ExtendedSchoolTeacherEntry> extendedSchoolTeacherEntries =  extendedSchoolTeacherDao.getListBySchoolId(schoolId);
        for(ExtendedSchoolTeacherEntry entry :extendedSchoolTeacherEntries){
            ExtendedSchoolTeacherDTO dto = new ExtendedSchoolTeacherDTO(entry);
            dtos.add(dto);
        }
        return dtos;
    }

    /**
     * 批量增加课时
     * @param list
     */
    public void addEntryBatch(List<ExtendedCourseClassEntry> list) {
        List<DBObject> dbList = new ArrayList<DBObject>();
        for (int i = 0; list != null && i < list.size(); i++) {
            ExtendedCourseClassEntry si = list.get(i);
            dbList.add(si.getBaseEntry());
        }
        //导入新纪录
        if(dbList.size()>0) {
            extendedCourseClassDao.addBatch(dbList);
        }
    }

    //课节生成
    public void addClassList(ObjectId id,ObjectId schoolId, long startTime,long endTime,int week,int lessonType){
        List<String> strings = getDate(startTime,endTime,week,lessonType);
        ExtendedCourseClassEntry entry = new ExtendedCourseClassEntry(schoolId,id,strings);
        extendedCourseClassDao.saveEntry(entry);
    }

    public List<String> getDate(long startNum,long endNum,int wk,int lessonType){
        List<String> str = new ArrayList<String>();
        List<Long> numberList = new ArrayList<Long>();
        //管控时间
        Date date = new Date(startNum);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if(w==0){
            w= 7;
        }
        boolean flage = true;
        long ssstime = startNum;
        String[] strings = new String[]{wk+""};
        for(int j = 0; j < strings.length;j++){
            int week = Integer.parseInt(strings[j]);
            int i = 0;
            if(w< week){
                startNum = startNum + (week-w)*24*60*60*1000;
            }else if(w==week){

            }else{//3   1
                startNum = startNum + (7-w+week)*24*60*60*1000;
            }
            if(endNum>=startNum){
                numberList.add(startNum);
            }else{
                flage = false;
            }
            while(flage){
                i++;
                startNum += 7*24*60*60*1000;
                if(endNum>=startNum){
                    // str.add(DateTimeUtils.getLongToStrTimeTwo(startNum).substring(0,11));
                    numberList.add(startNum);
                }else{
                    flage = false;
                }
                if(i>100){
                    flage = false;
                }
            }
            startNum = ssstime;
            flage = true;
        }
        //排序
        Collections.sort(numberList);
        for(Long lo  :numberList){
            str.add(DateTimeUtils.getLongToStrTimeTwo(lo).substring(0,11)+"*"+lessonType);
        }
        return str;
    }

    /**
     * 新增拓展课
     */
    public String saveExtendedCourse(ExtendedCourseDTO dto,ObjectId userId,ObjectId schoolId){
        dto.setUserId(userId.toString());
        ExtendedCourseEntry entry = dto.addEntry();
        entry.setSchoolId(schoolId);
        extendedCourseDao.saveEntry(entry);
        //添加课节记录
        this.addClassList(entry.getID(),schoolId,entry.getVoteStartTime(),entry.getVoteEndTime(),entry.getWeek(),entry.getLessonType());
        //添加首页记录() //添加红点
        List<ObjectId> communityIds = schoolCommunityDao.getCommunityIdsListBySchoolId(schoolId);
        List<CommunityEntry> communityEntrys =  communityDao.findByObjectIds(communityIds);
        //String gradeName = getGrade(dto.getGradeList());
        List<String> gradeNames = dto.getGradeList();
        List<String> cids = new ArrayList<String>();
        List<ObjectId> gids  = new ArrayList<ObjectId>();
        for(CommunityEntry com:communityEntrys){
            if(gradeNames.contains(com.getGradeVal())){
                cids.add(com.getID().toString());
                gids.add(com.getGroupId());
            }
        }
        this.sendIndexPageMessage(dto,userId,cids,entry.getID(),gids);
        return "";
    }

    public  String getGrade(List<String> gradeList){
        String name = "";
        for(String str:gradeList){
            int index = Integer.parseInt(str);
            name = name + gradeString[index]+"、";
        }
        String string  = name.substring(0,name.length()-1);
        return string;
    }

    //todo 选课首页
    public void sendIndexPageMessage(ExtendedCourseDTO dto,ObjectId userId,List<String> communityIds,ObjectId id,List<ObjectId> groupIds){

        IndexPageDTO dto2 = new IndexPageDTO();
        dto2.setType(CommunityType.extended.getType());
        dto2.setUserId(userId.toString());
        dto2.setCommunityId(dto.getSchoolId());
        dto2.setReceiveIdList(communityIds);
        dto2.setContactId(id.toString());
        IndexPageEntry entry = dto2.buildAddEntry();
        indexPageDao.addEntry(entry);
        IndexContentDTO indexContentDTO = new IndexContentDTO(
                dto.getTypeName(),
                dto.getCourseName(),
                dto.getDescription(),
                dto.getVideoList(),
                dto.getImageList(),
                dto.getAttachements(),
                dto.getVoiceList(),
                dto.getGradeName(),
                "");
        List<ObjectId> members=memberDao.getAllGroupIdsMembers(groupIds);
        IndexContentEntry indexContentEntry = indexContentDTO.buildEntry(dto.getUserId(),null, null,null,3);
        indexContentEntry.setReadList(new ArrayList<ObjectId>());
        indexContentEntry.setContactId(id);
        indexContentEntry.setContactType(11);
        indexContentEntry.setAllCount(members.size());
        indexContentDao.addEntry(indexContentEntry);
        List<ObjectId> oids = new ArrayList<ObjectId>();
        for(String st:communityIds){
            oids.add(new ObjectId(st));
        }
        //添加红点
        redDotService.addOtherEntryList(oids, id, ApplyTypeEn.extend.getType(), 3);

    }


    /**
     * 查询所有课程（app  家长、老师共用 三种状态）
     */
    public Map<String,Object> selectExtendedCourseList(ObjectId communityId,ObjectId userId,String keyword,int status,int page,int pageSize) throws Exception{
        Map<String,Object> map = new HashMap<String, Object>();
        //清除红点
        redDotService.cleanThirdResult(userId, ApplyTypeEn.extend.getType());
        //获得社群
        CommunityEntry communityEntry = communityDao.findByObjectId(communityId);
        if(communityEntry==null){
            throw new Exception("该社群不存在");
        }
        String gradeName = "";
        if(communityEntry.getGradeVal()!=null){
            gradeName = communityEntry.getGradeVal();
        }
        //获得班级学校
        SchoolCommunityEntry schoolCommunityEntry = schoolCommunityDao.getEntryById(communityId);
        if(schoolCommunityEntry==null){
            throw new Exception("该社群未绑定学校");
        }
        ObjectId schoolId = schoolCommunityEntry.getSchoolId();
        long current  = System.currentTimeMillis();
        //0  全部   1  报名中   2  学习中   3 已学完
        //获得身份 ( true 老师    false 家长)
        boolean falge = teacherApproveDao.booleanBig(userId);
        int role = 2;
        if(falge){
            role = 1;
        }
        List<ExtendedCourseEntry> courseEntries = new ArrayList<ExtendedCourseEntry>();
        int count  = 0;
        if(role == 1){//老师查询所有
            courseEntries = extendedCourseDao.getAllEntryList(gradeName, schoolId, keyword, status,current, page, pageSize);
            count = extendedCourseDao.countAllEntryList(gradeName, schoolId, keyword, status,current);
        }else{
            List<NewVersionCommunityBindEntry> sids = newVersionCommunityBindDao.getBindEntries(communityId, userId);
            List<ObjectId> sonIds = new ArrayList<ObjectId>();
            for(NewVersionCommunityBindEntry communityBindEntry:sids){
                sonIds.add(communityBindEntry.getUserId());
            }
            courseEntries = extendedCourseDao.getParentAllEntryList(sonIds, gradeName, schoolId, keyword, status, current, page, pageSize);
            count = extendedCourseDao.countParentAllEntryList(sonIds,gradeName, schoolId, keyword, status,current);
        }
        List<ExtendedCourseDTO> dtos1 = new ArrayList<ExtendedCourseDTO>();
        this.getMyCourseList(courseEntries,dtos1,userId,current,status,role);
        map.put("count", count);
        map.put("list",dtos1);
        map.put("role", role);
        return map;
    }

    /**
     * 查询所有课程（app  家长、老师共用 三种状态）
     */
    public Map<String,Object> selectWebExtendedCourseList(ObjectId communityId,ObjectId userId,String keyword,int status,int page,int pageSize) throws Exception{
        Map<String,Object> map = new HashMap<String, Object>();
        //获得社群
        CommunityEntry communityEntry = communityDao.findByObjectId(communityId);
        if(communityEntry==null){
            throw new Exception("该社群不存在");
        }
        String gradeName = "";
        if(communityEntry.getGradeVal()!=null){
            gradeName = communityEntry.getGradeVal();
        }
        //获得班级学校
        SchoolCommunityEntry schoolCommunityEntry = schoolCommunityDao.getEntryById(communityId);
        if(schoolCommunityEntry==null){
            throw new Exception("该社群未绑定学校");
        }
        ObjectId schoolId = schoolCommunityEntry.getSchoolId();
        long current  = System.currentTimeMillis();
        //0  全部   1  报名中   2  学习中   3 已学完
        //获得身份 ( true 老师    false 家长)
        boolean falge = teacherApproveDao.booleanBig(userId);
        int role = 2;
        if(falge){
            role = 1;
        }
        List<ExtendedCourseEntry> courseEntries = new ArrayList<ExtendedCourseEntry>();
        int count  = 0;
        if(role == 1){//老师查询所有
            courseEntries = extendedCourseDao.getAllEntryList(gradeName, schoolId, keyword, status,current, page, pageSize);
            count = extendedCourseDao.countAllEntryList(gradeName, schoolId, keyword, status,current);
        }else{
            List<NewVersionCommunityBindEntry> sids = newVersionCommunityBindDao.getBindEntries(communityId, userId);
            List<ObjectId> sonIds = new ArrayList<ObjectId>();
            for(NewVersionCommunityBindEntry communityBindEntry:sids){
                sonIds.add(communityBindEntry.getUserId());
            }
            courseEntries = extendedCourseDao.getParentAllEntryList(sonIds, gradeName, schoolId, keyword, status, current, page, pageSize);
            count = extendedCourseDao.countParentAllEntryList(sonIds,gradeName, schoolId, keyword, status,current);
        }
        List<ExtendedCourseDTO> dtos1 = new ArrayList<ExtendedCourseDTO>();
        this.getMyWebCourseList(courseEntries, dtos1, userId, current, status, role);
        map.put("count", count);
        map.put("list",dtos1);
        map.put("role",role);
        //清除红点
        redDotService.cleanThirdResult(userId, ApplyTypeEn.extend.getType());
        return map;
    }

    /**
     * 查询所有课程（查询所有课程（校领导））
     */
    public Map<String,Object> selectAllWebExtendedCourseList(ObjectId schoolId,ObjectId userId,String gradeName,String keyword,int status,int page,int pageSize) throws Exception{
        Map<String,Object> map = new HashMap<String, Object>();
        long current  = System.currentTimeMillis();
        if(gradeName!=null && gradeName.equals("0")){
            gradeName = "";
        }
        //0  全部   1  报名中   2  学习中   3 已学完
        List<ExtendedCourseEntry> courseEntries = extendedCourseDao.getAllEntryList(gradeName, schoolId, keyword, status,current, page, pageSize);
        int count = extendedCourseDao.countAllEntryList(gradeName, schoolId, keyword, status,current);
        //获得身份 ( true 老师    false 家长)
        int role = 1;
        List<ExtendedCourseDTO> dtos1 = new ArrayList<ExtendedCourseDTO>();
        this.getMyWebCourseList(courseEntries,dtos1,userId,current,status,role);
        map.put("count", count);
        map.put("list",dtos1);
        map.put("role",role);
        //清除红点
        redDotService.cleanThirdResult(userId, ApplyTypeEn.extend.getType());
        return map;
    }

    /**
     * 查询所有课程（学生）
     */
    public Map<String,Object> selectExtendedCourseListFromSon(ObjectId communityId,ObjectId userId,String keyword,int status,int page,int pageSize) throws Exception{
        Map<String,Object> map = new HashMap<String, Object>();
        List<ExtendedCourseEntry> dtos = new ArrayList<ExtendedCourseEntry>();
        //获得社群
        CommunityEntry communityEntry = communityDao.findByObjectId(communityId);
        if(communityEntry==null){
            throw new Exception("该社群不存在");
        }
        String gradeName = "";
        if(communityEntry.getGradeVal()!=null){
            gradeName = communityEntry.getGradeVal();
        }
        //获得班级学校
        SchoolCommunityEntry schoolCommunityEntry = schoolCommunityDao.getEntryById(communityId);
        if(schoolCommunityEntry==null){
            throw new Exception("该社群未绑定学校");
        }
        ObjectId schoolId = schoolCommunityEntry.getSchoolId();
        long current  = System.currentTimeMillis();
        //0  全部   1  报名中   2  学习中   3 已学完
        List<ObjectId> sonIds = new ArrayList<ObjectId>();
        sonIds.add(userId);
        List<ExtendedCourseEntry> courseEntries = extendedCourseDao.getParentAllEntryList(sonIds, gradeName, schoolId, keyword, status, current, page, pageSize);
        int count = extendedCourseDao.countParentAllEntryList(sonIds, gradeName, schoolId, keyword, status, current);
        //获得身份 ( 学生)
        int role = 3;
        List<ExtendedCourseDTO> dtos1 = new ArrayList<ExtendedCourseDTO>();
        this.getMyCourseList(courseEntries,dtos1,userId,current,status,role);
        map.put("count", count);
        map.put("list",dtos1);
        map.put("role",role);
        //清除红点
        redDotService.cleanThirdResult(userId, ApplyTypeEn.extend.getType());
        return map;
    }

    //组装实体
    public void getMyCourseList(List<ExtendedCourseEntry> entries,List<ExtendedCourseDTO> dtos,ObjectId userId,long current,int status,int role){
        for(ExtendedCourseEntry entry:entries){
            ExtendedCourseDTO dto = new ExtendedCourseDTO(entry);
           /*private int status; //0  全部   1  报名中   2  学习中   3 已学完
    private int stage;//   1 报名未开始   2 报名进行中  3 火速报名进行中  4 报名已抢光  5 已报名  6 报名已结束 （针对报名中）
    private int role;  // 1 老师  2 家长*/
            dto.setStatus(status);

            dto.setRole(role);
            if(entry.getApplyStartTime()>current){
                dto.setStage(1);
            }else if(entry.getApplyEndTime()<current){
                dto.setStage(6);
            }else{
                if(entry.getType()==1){//抢课
                    dto.setStage(2);
                    List<ObjectId> userIds = entry.getUserSelectedList();
                    if(userIds!=null){
                        if(userIds.size()>= entry.getUserAllNumber()){//报名人数和班级人数相等(或大于)  && !userIds.contains(userId)
                            dto.setStage(4);
                        }
                    }
                }else if(entry.getType()==2){
                    dto.setStage(3);
                }
            }
            dtos.add(dto);
        }
    }

    public void getMyWebCourseList(List<ExtendedCourseEntry> entries,List<ExtendedCourseDTO> dtos,ObjectId userId,long current,int status,int role){
        for(ExtendedCourseEntry entry:entries){
            ExtendedCourseDTO dto = new ExtendedCourseDTO(entry,1);
           /*private int status; //0  全部   1  报名中   2  学习中   3 已学完
    private int stage;//   1 报名未开始   2 报名进行中  3 火速报名进行中  4 报名已抢光  5 已报名  6 报名已结束 （针对报名中）
    private int role;  // 1 老师  2 家长*/
            dto.setStatus(status);
            if(status==0){
                if(entry.getVoteStartTime()>current){//报名中
                    dto.setStatus(1);
                }else if(entry.getVoteEndTime()<current){//已结束
                    dto.setStatus(3);
                }else{
                    dto.setStatus(2);
                }
            }
            dto.setRole(role);
            if(entry.getApplyStartTime()>current){
                dto.setStage(1);
            }else if(entry.getApplyEndTime()<current){
                dto.setStage(6);
            }else{
                if(entry.getType()==1){//抢课
                    dto.setStage(2);
                    List<ObjectId> userIds = entry.getUserSelectedList();
                    if(userIds!=null){
                        if(userIds.size()>= entry.getUserAllNumber()){//报名人数和班级人数相等(或大于)  && !userIds.contains(userId)
                            dto.setStage(4);
                        }
                    }
                }else if(entry.getType()==2){
                    dto.setStage(3);
                }
            }
            dtos.add(dto);
        }
    }
    public int getStatus(ExtendedCourseEntry entry){
        long current = System.currentTimeMillis();
        int status = 0;
        if(entry.getVoteStartTime()>current){
            status = 1;
        }else if(entry.getVoteStartTime()<current && entry.getVoteEndTime()>current){
            status = 2;
        }else if(entry.getVoteEndTime()<current){
            status = 3;
        }
        return status;
    }

    /**
     * 查询报名名单（老师）
     */
    public Map<String,Object> selectStudentList(ObjectId communityId,ObjectId userId,ObjectId id) throws Exception{
        Map<String,Object> map = new HashMap<String, Object>();
        ExtendedCourseEntry entry = extendedCourseDao.getEntryById(id);
        if(entry==null){
            throw new Exception("课程已被删除！");
        }
        ExtendedCourseDTO dto = new ExtendedCourseDTO(entry);
        dto.setStatus(getStatus(entry));
        dto.setRole(1);
        List<ObjectId> userIds = extendedUserApplyDao.getIdsByCourseId(id, communityId);
        Map<ObjectId,UserEntry> userEntryMap = userDao.getUserEntryMap(userIds, Constant.FIELDS);
        Map<ObjectId,String> nameMap = newVersionBindRelationDao.getCommunitySonEntriesByMainUserId(userIds);
        List<User> userList = new ArrayList<User>();
        for(ObjectId oid : userIds){
            UserEntry userEntry = userEntryMap.get(oid);
            if(userEntry!=null){
                String str = nameMap.get(oid);
                String name = StringUtils.isBlank(userEntry.getNickName())?userEntry.getUserName():userEntry.getNickName();
                if(str!=null && !str.equals("")){
                    name = str;
                }
                User user=new User(name,
                        name,
                        userEntry.getID().toString(),
                        AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(), userEntry.getSex()),
                        userEntry.getSex(),
                        "");
                user.setId(userEntry.getID().toString());
                userList.add(user);
            }
        }
        map.put("dto",dto);
        map.put("userList",userList);
        return map;
    }

    /**
     * 查询课程详情（家长）
     */
    public Map<String,Object> selectCourseDesc(ObjectId userId,ObjectId id,ObjectId communityId) throws Exception{
        Map<String,Object> map = new HashMap<String, Object>();
        ExtendedCourseEntry entry = extendedCourseDao.getEntryById(id);
        List<NewVersionCommunityBindEntry> newVersionCommunityBindEntries = newVersionCommunityBindDao.getBindEntries(communityId, userId);
        List<ObjectId> ids = new ArrayList<ObjectId>();
        for(NewVersionCommunityBindEntry newVersionCommunityBindEntry:newVersionCommunityBindEntries){
            ids.add(newVersionCommunityBindEntry.getUserId());
        }
        List<UserEntry> userEntries = userDao.getUserEntryList(ids, Constant.FIELDS);
        if(entry==null){
            throw new Exception("课程已被删除！");
        }
        ExtendedCourseDTO dto = new ExtendedCourseDTO(entry);
        dto.setStatus(getStatus(entry));
        dto.setRole(2);
        long current = System.currentTimeMillis();
        List<ObjectId> userIds = entry.getUserSelectedList();
        if(entry.getApplyStartTime()>current){
            dto.setStage(1);
        }else if(entry.getApplyEndTime()<current){
            dto.setStage(6);
        }else {
            if (entry.getType() == 1) {//抢课
                dto.setStage(2);
                if (userIds != null) {
                    if (userIds.size() >= entry.getUserAllNumber()) {//报名人数和班级人数相等(或大于)  && !userIds.contains(userId)
                        dto.setStage(4);
                    }
                }
            } else if (entry.getType() == 2) {
                dto.setStage(3);
            }
        }
        List<Map<String,Object>> mapList = new ArrayList<Map<String, Object>>();
        List<Map<String,Object>> mapList2 = new ArrayList<Map<String, Object>>();
        Map<ObjectId,String> nameMap = newVersionBindRelationDao.getCommunitySonEntriesByMainUserId(ids);
        if (entry.getType() == 1) {//抢课
        } else if (entry.getType() == 2) {//火速报名
            userIds = entry.getUserApplyList();
        }
        for(UserEntry userEntry:userEntries){
            if (userIds != null && userIds.contains(userEntry.getID())) {
                Map<String,Object> mapDto = new HashMap<String, Object>();
                String name = StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
                String str = nameMap.get(userEntry.getID());
                if(str!=null&& !str.equals("")){
                    name = str;
                }
                mapDto.put("name",name);
                mapDto.put("id",userEntry.getID().toString());
                mapDto.put("avatar",AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(), userEntry.getSex()));
                mapDto.put("isApply",true);
                mapList.add(mapDto);
            }else{
                Map<String,Object> mapDto = new HashMap<String, Object>();
                String name = StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
                String str = nameMap.get(userEntry.getID());
                if(str!=null&& !str.equals("")){
                    name = str;
                }
                mapDto.put("name",name);
                mapDto.put("id",userEntry.getID().toString());
                mapDto.put("avatar",AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(), userEntry.getSex()));
                mapDto.put("isApply",false);
                mapList2.add(mapDto);
            }
        }
        map.put("dto",dto);
        map.put("selectList",mapList);
        map.put("unSelectList",mapList2);
        return map;
    }

    /**
     * 查询课程详情（孩子）
     */
    public ExtendedCourseDTO selectCourseDescFromSon(ObjectId userId,ObjectId id) throws Exception{
        ExtendedCourseEntry entry = extendedCourseDao.getEntryById(id);
        if(entry==null){
            throw new Exception("课程已被删除！");
        }
        ExtendedCourseDTO dto = new ExtendedCourseDTO(entry);
        dto.setStatus(getStatus(entry));
        dto.setRole(2);
        long current = System.currentTimeMillis();
        if(entry.getApplyStartTime()>current){
            dto.setStage(1);
        }else if(entry.getApplyEndTime()<current){
            dto.setStage(6);
            List<ObjectId> userIds = entry.getUserSelectedList();
            if(userIds.contains(userId)){
                dto.setStage(7);
            }
        }else {
            if (entry.getType() == 1) {//抢课
                dto.setStage(2);
                List<ObjectId> userIds = entry.getUserSelectedList();
                if (userIds != null) {
                    if (userIds.size() >= entry.getUserAllNumber()) {//报名人数和班级人数相等(或大于)  && !userIds.contains(userId)
                        dto.setStage(4);
                    }
                    // 取消报名
                    if(userIds.contains(userId)){
                        dto.setStage(5);
                    }
                }
            } else if (entry.getType() == 2) {
                dto.setStage(3);
                List<ObjectId> userIds = entry.getUserApplyList();
                if (userIds != null) {
                    // 取消报名
                    if(userIds.contains(userId)){
                        dto.setStage(5);
                    }
                }
            }
        }
        return dto;
    }


    /**
     * 报名、抢课（家长）
     */
    public String applyCourse(ObjectId communityId,ObjectId userId,ObjectId id,String sonIds) throws Exception{
        ExtendedCourseEntry extendedCourseEntry = extendedCourseDao.getEntryById(id);
        if(extendedCourseEntry==null){
            throw new Exception("该课程已被删除！");
        }
        //1.判断是否可投
        long current = System.currentTimeMillis();
        if(extendedCourseEntry.getApplyStartTime()>current){
            throw new Exception("报名未开始！");
        }
        if(extendedCourseEntry.getApplyEndTime()<current){
            throw new Exception("报名已结束！");
        }
        int type = extendedCourseEntry.getType();
        //2.上课人员
        List<ObjectId> userSelectList = extendedCourseEntry.getUserSelectedList();
        if(userSelectList==null){
            userSelectList = new ArrayList<ObjectId>();
        }
        //3.申请人员
        List<ObjectId> applyUserList = extendedCourseEntry.getUserApplyList();
        if(applyUserList==null){
            applyUserList = new ArrayList<ObjectId>();
        }
        //4.抢光
        if(type==1){//抢课
            if(userSelectList.size()>=extendedCourseEntry.getUserAllNumber()){
                throw new Exception("课程已抢光！");
            }
        }

        //5.前置满足
        SchoolCommunityEntry schoolCommunityEntry = schoolCommunityDao.getEntryById(communityId);
        CommunityEntry communityEntry = communityDao.findByObjectId(communityId);
        if(schoolCommunityEntry==null){
            throw new Exception("该社群未绑定学校！");
        }
        if(communityEntry==null){
            throw new Exception("该社群已被删除！");
        }
        String gradeName = "";
        if(communityEntry.getGradeVal()!=null){
            gradeName = communityEntry.getGradeVal();
        }

        //成功添加
        String[] sonIdList = sonIds.split(",");
        List<ObjectId> seList = new ArrayList<ObjectId>();
        for(String str:sonIdList){
            seList.add(new ObjectId(str));
        }
        //发送冲突
        this.sendMessage(id,seList);

        for(ObjectId sonId:seList){
            ExtendedUserApplyEntry extendedUserApplyEntry = extendedUserApplyDao.getEntryById(id,sonId);
            if(extendedUserApplyEntry!=null){//已存在
                throw new Exception("已报名，不可重复报名！");
            }else{
                if(type==1){//抢课
                    if(userSelectList.contains(sonId)){
                        throw new Exception("已报名，不可重复报名！");
                    }
                    if(userSelectList.size()>=extendedCourseEntry.getUserAllNumber()){
                        throw new Exception("课程已抢光！");
                    }
                    int count = extendedUserApplyDao.countCourseUser(id,communityId);
                    if (extendedCourseEntry.getClassUserNumber()!=0 && count>=extendedCourseEntry.getClassUserNumber()) {
                        throw new Exception("本班级名额已抢光！");
                    }
                    ExtendedUserApplyEntry extendedUserApplyEntry1 = new ExtendedUserApplyEntry(
                            schoolCommunityEntry.getSchoolId(),
                            gradeName,
                            null,
                            communityEntry.getID(),
                            id,
                            sonId,
                            Constant.TWO,//默认抢上
                            userId,
                            Constant.ONE//家长帮抢
                            );
                    extendedUserApplyDao.saveEntry(extendedUserApplyEntry1);
                    //抢课成功
                    systemMessageService.sendExtendedMessage(sonId,extendedCourseEntry,1);
                    //成功
                    userSelectList.remove(sonId);
                    userSelectList.add(sonId);
                    applyUserList.remove(sonId);
                    applyUserList.add(sonId);
                    extendedCourseEntry.setUserSelectedList(userSelectList);
                    extendedCourseEntry.setUserApplyList(applyUserList);
                    extendedCourseDao.saveEntry(extendedCourseEntry);
                    userSelectList = extendedCourseEntry.getUserSelectedList();
                }else{//火速报名
                    if(applyUserList.contains(sonId)){
                        throw new Exception("已报名，不可重复报名！");
                    }
                    ExtendedUserApplyEntry extendedUserApplyEntry1 = new ExtendedUserApplyEntry(
                            schoolCommunityEntry.getSchoolId(),
                            gradeName,
                            null,
                            communityEntry.getID(),
                            id,
                            sonId,
                            Constant.ONE,//默认申请
                            userId,
                            Constant.ONE//家长帮抢
                    );
                    //成功
                    applyUserList.remove(sonId);
                    applyUserList.add(sonId);
                    extendedCourseEntry.setUserApplyList(applyUserList);
                    int count = extendedUserApplyDao.countCourseUser(id,communityId);
                    if (extendedCourseEntry.getClassUserNumber()>count) {
                        if(extendedCourseEntry.getUserAllNumber()>userSelectList.size()){
                            userSelectList.remove(sonId);
                            userSelectList.add(sonId);
                            extendedCourseEntry.setUserSelectedList(userSelectList);
                            extendedUserApplyEntry1.setStatus(Constant.TWO);
                        }
                    }
                    extendedUserApplyDao.saveEntry(extendedUserApplyEntry1);
                    extendedCourseDao.saveEntry(extendedCourseEntry);
                }
            }
        }
        return "报名成功！";
    }

    /**
     * 报名、抢课(孩子)
     */
    public String applyCourseForSon(ObjectId communityId,ObjectId sonId,ObjectId id) throws Exception{
        ExtendedCourseEntry extendedCourseEntry = extendedCourseDao.getEntryById(id);
        if(extendedCourseEntry==null){
            throw new Exception("该课程已被删除！");
        }
        //1.判断是否可投
        long current = System.currentTimeMillis();
        if(extendedCourseEntry.getApplyStartTime()>current){
            throw new Exception("报名未开始！");
        }
        if(extendedCourseEntry.getApplyEndTime()<current){
            throw new Exception("报名已结束！");
        }
        int type = extendedCourseEntry.getType();
        //2.上课人员
        List<ObjectId> userSelectList = extendedCourseEntry.getUserSelectedList();
        if(userSelectList==null){
            userSelectList = new ArrayList<ObjectId>();
        }
        //3.申请人员
        List<ObjectId> applyUserList = extendedCourseEntry.getUserApplyList();
        if(applyUserList==null){
            applyUserList = new ArrayList<ObjectId>();
        }
        //4.抢光
        if(type==1){//抢课
            if(userSelectList.size()>=extendedCourseEntry.getUserAllNumber()){
                throw new Exception("课程已抢光！");
            }
        }
        //5.前置满足
        SchoolCommunityEntry schoolCommunityEntry = schoolCommunityDao.getEntryById(communityId);
        CommunityEntry communityEntry = communityDao.findByObjectId(communityId);
        if(schoolCommunityEntry==null){
            throw new Exception("该社群未绑定学校！");
        }
        if(communityEntry==null){
            throw new Exception("该社群已被删除！");
        }
        String gradeName = "";
        if(communityEntry.getGradeVal()!=null){
            gradeName = communityEntry.getGradeVal();
        }

        //发送冲突
        List<ObjectId> seList = new ArrayList<ObjectId>();
        seList.add(sonId);
        this.sendMessage(id,seList);
        //成功添加
        ExtendedUserApplyEntry extendedUserApplyEntry = extendedUserApplyDao.getEntryById(id,sonId);
        if(extendedUserApplyEntry!=null){//已存在
            throw new Exception("已报名，不可重复报名！");
        }else {
            if (type == 1) {//抢课
                if (userSelectList.contains(sonId)) {
                    throw new Exception("已报名，不可重复报名！");
                }
                int count = extendedUserApplyDao.countCourseUser(id,communityId);
                if (extendedCourseEntry.getClassUserNumber()!=0 && count>=extendedCourseEntry.getClassUserNumber()) {
                    throw new Exception("本班级名额已抢光！");
                }
                ExtendedUserApplyEntry extendedUserApplyEntry1 = new ExtendedUserApplyEntry(
                        schoolCommunityEntry.getSchoolId(),
                        gradeName,
                        null,
                        communityEntry.getID(),
                        id,
                        sonId,
                        Constant.TWO,//默认抢上
                        sonId,
                        Constant.ZERO//自己抢
                );
                extendedUserApplyDao.saveEntry(extendedUserApplyEntry1);
                //抢课成功
                systemMessageService.sendExtendedMessage(sonId,extendedCourseEntry,1);
                //成功
                userSelectList.remove(sonId);
                userSelectList.add(sonId);
                applyUserList.remove(sonId);
                applyUserList.add(sonId);
                extendedCourseEntry.setUserSelectedList(userSelectList);
                extendedCourseEntry.setUserApplyList(applyUserList);
                extendedCourseDao.saveEntry(extendedCourseEntry);
                userSelectList = extendedCourseEntry.getUserSelectedList();
            } else {//火速报名
                if (applyUserList.contains(sonId)) {
                    throw new Exception("已报名，不可重复报名！");
                }
                ExtendedUserApplyEntry extendedUserApplyEntry1 = new ExtendedUserApplyEntry(
                        schoolCommunityEntry.getSchoolId(),
                        gradeName,
                        null,
                        communityEntry.getID(),
                        id,
                        sonId,
                        Constant.ONE,//默认申请
                        sonId,
                        Constant.ZERO//自己抢
                );
                //成功
                applyUserList.remove(sonId);
                applyUserList.add(sonId);
                extendedCourseEntry.setUserApplyList(applyUserList);
                //名额在加入选课成功列表
                int count = extendedUserApplyDao.countCourseUser(id,communityId);
                if (extendedCourseEntry.getClassUserNumber()>count) {
                    if(extendedCourseEntry.getUserAllNumber()>userSelectList.size()){
                        userSelectList.remove(sonId);
                        userSelectList.add(sonId);
                        extendedUserApplyEntry1.setStatus(Constant.TWO);
                        extendedCourseEntry.setUserSelectedList(userSelectList);
                    }
                }
                extendedUserApplyDao.saveEntry(extendedUserApplyEntry1);
                extendedCourseDao.saveEntry(extendedCourseEntry);
            }

        }
        return "报名成功！";
    }

    public void  deleteMyApply(ObjectId id,ObjectId communityId,ObjectId userId,ObjectId sonId) throws Exception{
        ExtendedCourseEntry extendedCourseEntry = extendedCourseDao.getEntryById(id);
        long current = System.currentTimeMillis();
        if(extendedCourseEntry.getApplyEndTime()<current){
            throw new Exception("报名已结束！");
        }
        ExtendedUserApplyEntry extendedUserApplyEntry = extendedUserApplyDao.getEntryById(id,sonId);
        int type = extendedCourseEntry.getType();
        //1.上课人员
        List<ObjectId> userSelectList = extendedCourseEntry.getUserSelectedList();
        if(userSelectList==null){
            userSelectList = new ArrayList<ObjectId>();
        }
        //2.申请人员
        List<ObjectId> applyUserList = extendedCourseEntry.getUserApplyList();
        if(applyUserList==null){
            applyUserList = new ArrayList<ObjectId>();
        }
        //构造
        if(type==1){
            applyUserList.remove(sonId);
            userSelectList.remove(sonId);
            extendedCourseEntry.setUserSelectedList(userSelectList);
            extendedCourseEntry.setUserApplyList(applyUserList);
        }else{
            userSelectList.remove(sonId);
            applyUserList.remove(sonId);
            extendedCourseEntry.setUserSelectedList(userSelectList);
            extendedCourseEntry.setUserApplyList(applyUserList);
        }
        if(extendedUserApplyEntry==null){
            extendedCourseDao.saveEntry(extendedCourseEntry);
        }else{
            extendedUserApplyEntry.setStatus(0);//退课
            extendedUserApplyEntry.setApplyType(1);//家长
            extendedUserApplyEntry.setApplyUserId(userId);
            extendedUserApplyEntry.setIsRemove(Constant.ONE);//删除
            extendedUserApplyDao.saveEntry(extendedUserApplyEntry);
            extendedCourseDao.saveEntry(extendedCourseEntry);
        }
    }

    public void  deleteMyApplyForSon(ObjectId id,ObjectId communityId,ObjectId sonId) throws Exception{
        ExtendedCourseEntry extendedCourseEntry = extendedCourseDao.getEntryById(id);
        long current = System.currentTimeMillis();
        if(extendedCourseEntry.getApplyEndTime()<current){
            throw new Exception("报名已结束！");
        }
        ExtendedUserApplyEntry extendedUserApplyEntry = extendedUserApplyDao.getEntryById(id,sonId);
        int type = extendedCourseEntry.getType();
        //1.上课人员
        List<ObjectId> userSelectList = extendedCourseEntry.getUserSelectedList();
        if(userSelectList==null){
            userSelectList = new ArrayList<ObjectId>();
        }
        //2.申请人员
        List<ObjectId> applyUserList = extendedCourseEntry.getUserApplyList();
        if(applyUserList==null){
            applyUserList = new ArrayList<ObjectId>();
        }
        //构造
        if(type==1){
            applyUserList.remove(sonId);
            userSelectList.remove(sonId);
            extendedCourseEntry.setUserSelectedList(userSelectList);
            extendedCourseEntry.setUserApplyList(applyUserList);
        }else{
            userSelectList.remove(sonId);
            applyUserList.remove(sonId);
            extendedCourseEntry.setUserSelectedList(userSelectList);
            extendedCourseEntry.setUserApplyList(applyUserList);
        }
        if(extendedUserApplyEntry==null){
            extendedCourseDao.saveEntry(extendedCourseEntry);
        }else{
            extendedUserApplyEntry.setStatus(0);//退课
            extendedUserApplyEntry.setApplyType(0);//家长
            extendedUserApplyEntry.setApplyUserId(sonId);
            extendedUserApplyEntry.setIsRemove(Constant.ONE);//删除
            extendedUserApplyDao.saveEntry(extendedUserApplyEntry);
            extendedCourseDao.saveEntry(extendedCourseEntry);
        }
    }


    /**
     * 查询社群
     */
    public Map<String,Object> getCommunityList(ObjectId userId){
        Map<String,Object> objectMap = new HashMap<String, Object>();
        List<CommunityDTO> communityDTOs = getCommunitys2(userId,1,100);
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        Map<ObjectId,CommunityDTO> communityDTOMap = new HashMap<ObjectId, CommunityDTO>();
        for(CommunityDTO communityDTO:communityDTOs){
            objectIdList.add(new ObjectId(communityDTO.getId()));
            communityDTOMap.put(new ObjectId(communityDTO.getId()),communityDTO);
        }
        List<NewVersionBindRelationEntry> bindRelationEntries = newVersionBindRelationDao.getEntriesByMainUserId(userId);
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        Map<ObjectId,String> nameMap = new HashMap<ObjectId, String>();
        for(NewVersionBindRelationEntry entry:bindRelationEntries){
            userIds.add(entry.getUserId());
            nameMap.put(entry.getUserId(),entry.getUserName());
        }
        Map<ObjectId,Set<ObjectId>> setMap = newVersionCommunityBindDao.getNewEntryList(userIds);
        List<ObjectId> communityIdsList = schoolCommunityDao.getTrueCommunityIdsList(objectIdList);
        //获得身份 ( true 老师    false 家长)
        boolean falge = teacherApproveDao.booleanBig(userId);
        List<Map<String,Object>> dtos = new ArrayList<Map<String,Object>>();
        for(ObjectId oid:communityIdsList){
            CommunityDTO dto = communityDTOMap.get(oid);
            Map<String,Object> dtoMap = new HashMap<String, Object>();
            dtoMap.put("communityName",dto.getName());
            dtoMap.put("id",dto.getId());
            dtoMap.put("logo","http://doc.k6kt.com/5bc826c6c4a727797c1c355a.png");
            Set<ObjectId> set = setMap.get(oid);
            if(set!=null && set.size()!=0){
                dtoMap.put("isBind",true);
            }else{
                dtoMap.put("isBind",false);
            }
            if(falge){
                dtoMap.put("isTeacher",true);
            }else{
                dtoMap.put("isTeacher",false);
            }
            dtos.add(dtoMap);
        }
        objectMap.put("list",dtos);
        List<UserEntry> userEntries = userDao.getUserEntryList(userIds, Constant.FIELDS);
        List<User> userList = new ArrayList<User>();
        for(UserEntry userEntry:userEntries){
            String str = nameMap.get(userEntry.getID());
            String name = StringUtils.isBlank(userEntry.getNickName())?userEntry.getUserName():userEntry.getNickName();
            if(str!=null && !str.equals("")){
                name = str;
            }
            User user=new User(name,
                    name,
                    userEntry.getID().toString(),
                    AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(), userEntry.getSex()),
                    userEntry.getSex(),
                    "");
            user.setId(userEntry.getID().toString());
            userList.add(user);
        }
        objectMap.put("sonList",userList);
        return objectMap;
    }

    /**
     * 查询社群
     */
    public Map<String,Object> getSonCommunityList(ObjectId userId){
        Map<String,Object> objectMap = new HashMap<String, Object>();
        List<NewVersionCommunityBindEntry> entries = newVersionCommunityBindDao.getAllStudentBindEntries(userId);
        List<ObjectId> communityIds = new ArrayList<ObjectId>();
        for(NewVersionCommunityBindEntry newVersionCommunityBindEntry:entries ){
            communityIds.add(newVersionCommunityBindEntry.getCommunityId());
        }
        List<SchoolCommunityEntry> schoolCommunityEntries = schoolCommunityDao.getReviewList2(communityIds);
        List<ObjectId> seList = new ArrayList<ObjectId>();
        for(SchoolCommunityEntry schoolCommunityEntry:schoolCommunityEntries){
            seList.add(schoolCommunityEntry.getCommunityId());
        }
        List<CommunityEntry> communityEntries =  communityDao.findByObjectIds(seList);
        List<Map<String,Object>> dtos = new ArrayList<Map<String,Object>>();
        for(CommunityEntry communityEntry:communityEntries){
            Map<String,Object> dtoMap = new HashMap<String, Object>();
            dtoMap.put("communityName",communityEntry.getCommunityName());
            dtoMap.put("id",communityEntry.getID().toString());
            dtoMap.put("logo","http://doc.k6kt.com/5bc826c6c4a727797c1c355a.png");
            dtoMap.put("isBind",true);
            dtoMap.put("isTeacher",false);
            dtos.add(dtoMap);
        }
        objectMap.put("list",dtos);
        List<User> userList = new ArrayList<User>();
        UserEntry userEntry =  userDao.findByUserId(userId);
        String name = StringUtils.isBlank(userEntry.getNickName())?userEntry.getUserName():userEntry.getNickName();
        User user=new User(name,
                name,
                userEntry.getID().toString(),
                AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(), userEntry.getSex()),
                userEntry.getSex(),
                "");
        user.setId(userEntry.getID().toString());
        userList.add(user);
        objectMap.put("sonList",userList);
        return objectMap;
    }


    /**
     * 添加孩子
     */
    public String registerAvailableUser(HttpServletRequest request,String name, String phoneNumber,int newRole,
                                        String nickName,ObjectId parentId,ObjectId communityId)throws Exception{
        String userName = this.createUserName(name,parentId);
        UserEntry userEntry=userDao.findByUserName(userName);
        if(null==userEntry){
            //创建用户
            UserEntry user=registerUserEntry(request,Constant.EMPTY,userName,"123456",phoneNumber,
                    nickName);
            ObjectId userId=userDao.addUserEntry(user);
            if(newRole!=-1) {
                if(null==newVersionUserRoleDao.getEntry(userId)){
                    newVersionUserRoleDao.saveEntry(new NewVersionUserRoleEntry(userId, newRole));
                }
            }
            //绑定用户
            this.saveNewVersionBindRelationEntry(parentId,userId,Constant.ONE,nickName);
            //绑定社群
            this.saveNewVersionCommunityEntry(communityId,parentId,userId.toString());
            return userId.toString();
        }else{
            throw new Exception("该用户名已用过");
        }
    }

    public void saveNewVersionCommunityEntry(ObjectId communityId,ObjectId mainUserId,String uId){
        NewVersionCommunityBindEntry entry = new NewVersionCommunityBindEntry(communityId, mainUserId, new ObjectId(uId));
        newVersionCommunityBindDao.saveEntry(entry);
        String[] uIds = new String[]{uId};
        //加进来的互相加为好友
        backStageService.setChildAutoFriends(uIds,communityId);
        //社长和孩子成为好友(暂时注释)
        backStageService.setChildCommunityFriends(uIds,communityId);
    }

    public void saveNewVersionBindRelationEntry (
            ObjectId mainUserId,
            ObjectId userId,
            int relation,
            String nickName
    ){
            try {
                NewVersionBindRelationEntry relationEntry
                        =new NewVersionBindRelationEntry(mainUserId,
                        userId,
                        relation,
                        Constant.EMPTY,
                        Constant.EMPTY,
                        Constant.EMPTY,
                        Constant.EMPTY,
                        Constant.EMPTY);
                relationEntry.setUserName(nickName);
                newVersionBindRelationDao.saveNewVersionBindEntry(relationEntry);
                //绑定的家长和学生自动成为好友
                backStageService.setSingleFriend(mainUserId,userId);
                backStageService.setSingleFriend(userId,mainUserId);
            }catch (Exception e){
                e.printStackTrace();
                throw  new RuntimeException("传入的生日数据有误!");
            }
    }

    public String createUserName(String userName,ObjectId userId) throws Exception{
        UserEntry userEntry=userDao.findByUserId(userId);
        long current = System.currentTimeMillis();
        if(userEntry==null){
            throw  new Exception("用户不存在！");
        }
        String end = current+"";
        String newName = userEntry.getGenerateUserCode()+userName+end.substring(end.length()-6,end.length()-1);
        return newName;
    }


    private UserEntry registerUserEntry(HttpServletRequest request,String email, String userName, String passWord, String phoneNumber, String nickName) {
        UserEntry userEntry = new UserEntry(userName, MD5Utils.getMD5String(passWord), phoneNumber, email, nickName);
        userEntry.setK6KT(0);
        userEntry.setIsRemove(0);
        userEntry.setStatisticTime(0L);
        userEntry.setRegisterIP(getIP(request));
        userEntry.setSilencedStatus(0);
        userEntry.setEmailStatus(0);
        if (org.apache.commons.lang.StringUtils.isNotBlank(email)) {
            userEntry.setEmailValidateCode(new ObjectId().toString());
        } else {
            userEntry.setEmailValidateCode(Constant.EMPTY);
        }
        return userEntry;
    }

    /**
     * 得到ip地址
     *
     * @return ip
     */
    protected String getIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


    /**
     * 简洁获取我的社团
     *
     * @param uid
     */
    public List<CommunityDTO> getCommunitys2(ObjectId uid, int page, int pageSize) {
        Map<ObjectId,CommunityDTO> map = new HashMap<ObjectId, CommunityDTO>();
        if(pageSize==100){//因为去除复兰社群和复兰大学社群导致社群数量不对的临时处理方法
            pageSize=102;
        }
        List<MineCommunityEntry> allMineCommunitys = mineCommunityDao.findAll(uid, page, pageSize);
        List<CommunityDTO> list = new ArrayList<CommunityDTO>();
        List<ObjectId> groupIds=new ArrayList<ObjectId>();
        List<ObjectId> communityIds = new ArrayList<ObjectId>();
        for (MineCommunityEntry mineCommunityEntry : allMineCommunitys) {
            communityIds.add(mineCommunityEntry.getCommunityId());
        }
        List<CommunityEntry> communityEntries = communityDao.findByNotObjectIds(communityIds);
        for(CommunityEntry communityEntry:communityEntries){
            groupIds.add(communityEntry.getGroupId());
            CommunityDTO communityDTO = new CommunityDTO(communityEntry);
            list.add(communityDTO);
        }
        if(list.size()>=100){//社群数量即将超过100
            SystemMessageService.sendMoreNotice(uid, 1);
        }
        return list;
    }


    /**
     * 获得用户的学校
     */
    public Map<String,Object> getUserRole(ObjectId userId){
        Map<String,Object> map = new HashMap<String, Object>();
        List<SchoolPersionEntry> schoolPersionEntrys = schoolPersionDao.getAllEntry(userId);
        if(schoolPersionEntrys.size()==0){
            map.put("isHeader",false);
            map.put("schoolId","");
            map.put("schoolName","");
            map.put("gradeList",null);
            return map;
        }
        ObjectId schoolId = schoolPersionEntrys.get(0).getSchoolId();
        HomeSchoolEntry homeSchoolEntry = homeSchoolDao.getEntryById(schoolId);
        if(homeSchoolEntry ==null){
            map.put("isHeader",false);
            map.put("schoolId","");
            map.put("schoolName","");
            map.put("gradeList",null);
            return map;
        }
        //组装年级
        List<String> stringList = homeSchoolEntry.getSchoolParagraph();
        map.put("isHeader",true);
        map.put("schoolId",schoolId.toString());
        map.put("schoolName",homeSchoolEntry.getName());
        map.put("gradeList",stringList);
        return map;
    }


    public void deleteCourse(ObjectId schoolId,ObjectId userId,ObjectId id) throws  Exception{
        ExtendedCourseEntry extendedCourseEntry = extendedCourseDao.getEntryById(id);
        if(extendedCourseEntry!=null){
            extendedCourseEntry.setRemoveId(userId);
            extendedCourseEntry.setIsRemove(Constant.ONE);
            extendedCourseDao.saveEntry(extendedCourseEntry);
            //删除首页记录
            indexPageDao.delEntry(id);
        }
    }


    public void endCourse(ObjectId schoolId,ObjectId userId,ObjectId id) throws  Exception{
        ExtendedCourseEntry extendedCourseEntry = extendedCourseDao.getEntryById(id);
        if(extendedCourseEntry!=null){
            //提前结束
            extendedCourseEntry.setRemoveId(userId);
            long current = System.currentTimeMillis();
            extendedCourseEntry.setApplyEndTime(current);
            extendedCourseDao.saveEntry(extendedCourseEntry);
        }
    }

    public ExtendedCourseDTO editCourse(ObjectId schoolId,ObjectId userId,ObjectId id) throws  Exception{
        ExtendedCourseEntry extendedCourseEntry = extendedCourseDao.getEntryById(id);
        if(extendedCourseEntry!=null){
            //提前结束
           return new ExtendedCourseDTO(extendedCourseEntry,1);
        }
        return null;
    }


    /**
     * 查询所有已入选用户
     */
    public Map<String,Object> selectAllUserList(ObjectId userId,ObjectId id,String communityId,int type){
        Map<String,Object> map = new HashMap<String, Object>();
        ExtendedCourseEntry entry = extendedCourseDao.getEntryById(id);
        if(entry==null){//课程不存在
            return map;
        }
        long current = System.currentTimeMillis();
        ExtendedCourseDTO dto = new ExtendedCourseDTO(entry,1);
        if(entry.getVoteStartTime()>current){//报名中
            dto.setStatus(1);
        }else if(entry.getVoteEndTime()<current){//已结束
            dto.setStatus(3);
        }else{
            dto.setStatus(2);
        }
        List<ExtendedUserApplyEntry> extendedUserApplyEntries = new ArrayList<ExtendedUserApplyEntry>();
        //查询所有已入选用户用户
        if(type==1){//老师
            extendedUserApplyEntries = extendedUserApplyDao.getEntrysByCourseId2(id,new ObjectId(communityId));
        }else if(type==2){//领导
            extendedUserApplyEntries = extendedUserApplyDao.getEntrysByCourseId(id);
        }

        List<Map<String,Object>>  mapList  = new ArrayList<Map<String, Object>>();
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        Set<ObjectId> communityIds = new HashSet<ObjectId>();
        List<ObjectId> communityIds2 = new ArrayList<ObjectId>();
        Map<ObjectId,Set<ObjectId>> sortMap = new HashMap<ObjectId, Set<ObjectId>>();
        for(ExtendedUserApplyEntry entry2:extendedUserApplyEntries){
            userIds.add(entry2.getUserId());
            communityIds.add(entry2.getCommunityId());
            communityIds2.add(entry2.getCommunityId());
            Set<ObjectId> uids = sortMap.get(entry2.getCommunityId());
            if(uids!=null){
                uids.add(entry2.getUserId());
                sortMap.put(entry2.getCommunityId(),uids);
            }else{
                Set<ObjectId> uids2 = new HashSet<ObjectId>();
                uids2.add(entry2.getUserId());
                sortMap.put(entry2.getCommunityId(),uids2);
            }
        }

        Map<ObjectId,UserEntry> userEntryMap = userDao.getUserEntryMap(userIds, Constant.FIELDS);
        Map<ObjectId,String> nameMap = newVersionBindRelationDao.getCommunitySonEntriesByMainUserId(userIds);
        Map<ObjectId,CommunityEntry> communityEntryMap = communityDao.findMapByObjectIds(communityIds2);
        for(ObjectId cid : communityIds){
            CommunityEntry communityEntry = communityEntryMap.get(cid);
            if(communityEntry!=null){
                Set<ObjectId> userIds2 = sortMap.get(cid);
                if(userIds!=null){
                    for(ObjectId uid:userIds2){
                        UserEntry userEntry = userEntryMap.get(uid);
                        if(userEntry!=null){
                            String name = StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
                            String str = nameMap.get(uid);
                            if(str!=null && !str.equals("")){
                                name = str;
                            }
                            Map<String,Object> mapDto = new HashMap<String, Object>();
                            mapDto.put("name",name);
                            mapDto.put("userId",uid.toString());
                            mapDto.put("class",communityEntry.getCommunityName());
                            mapDto.put("communityId",cid.toString());
                            mapList.add(mapDto);
                        }
                    }
                }
            }
        }
        map.put("dto",dto);
        map.put("list",mapList);
        map.put("count",mapList.size());
        return map;
    }

    /**
     * 查询所有已入选用户
     */
    public void getExclForSelectAllUserList(HttpServletResponse response,
                                            HttpServletRequest request,ObjectId userId,ObjectId id,String communityId,int type){
        ExtendedCourseEntry entry = extendedCourseDao.getEntryById(id);
        ExtendedCourseDTO dto = new ExtendedCourseDTO(entry,1);
        List<ExtendedUserApplyEntry> extendedUserApplyEntries = new ArrayList<ExtendedUserApplyEntry>();
        //查询所有已入选用户用户
        if(type==1){//老师
            extendedUserApplyEntries = extendedUserApplyDao.getEntrysByCourseId2(id,new ObjectId(communityId));
        }else if(type==2){//领导
            extendedUserApplyEntries = extendedUserApplyDao.getEntrysByCourseId(id);
        }
        List<Map<String,Object>>  mapList  = new ArrayList<Map<String, Object>>();
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        Set<ObjectId> communityIds = new HashSet<ObjectId>();
        List<ObjectId> communityIds2 = new ArrayList<ObjectId>();
        Map<ObjectId,Set<ObjectId>> sortMap = new HashMap<ObjectId, Set<ObjectId>>();
        for(ExtendedUserApplyEntry entry2:extendedUserApplyEntries){
            userIds.add(entry2.getUserId());
            communityIds.add(entry2.getCommunityId());
            communityIds2.add(entry2.getCommunityId());
            Set<ObjectId> uids = sortMap.get(entry2.getCommunityId());
            if(uids!=null){
                uids.add(entry2.getUserId());
                sortMap.put(entry2.getCommunityId(),uids);
            }else{
                Set<ObjectId> uids2 = new HashSet<ObjectId>();
                uids2.add(entry2.getUserId());
                sortMap.put(entry2.getCommunityId(),uids2);
            }
        }

        Map<ObjectId,UserEntry> userEntryMap = userDao.getUserEntryMap(userIds, Constant.FIELDS);
        Map<ObjectId,String> nameMap = newVersionBindRelationDao.getCommunitySonEntriesByMainUserId(userIds);
        Map<ObjectId,CommunityEntry> communityEntryMap = communityDao.findMapByObjectIds(communityIds2);

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet;
        String sheetName =  dto.getCourseName()+"-选课数据统计";
        try {
            sheet = wb.createSheet(dto.getCourseName()+"-选课数据统计");
        } catch (Exception e) {
            // TODO: handle exception
            sheet = wb.createSheet("选课数据统计");
        }
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
        HSSFRow rowZero = sheet.createRow(0);
        HSSFCell cellZero = rowZero.createCell(0);
        cellZero.setCellValue("成功选课人员列表");

        HSSFRow row = sheet.createRow(1);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("序号");

        cell = row.createCell(1);
        cell.setCellValue("学生姓名");

        cell = row.createCell(2);
        cell.setCellValue("班级");


        int rowLine = 2;

        HSSFRow rowItem;
        HSSFCell cellItem;
        int index = 0;
        for(ObjectId cid : communityIds) {
            CommunityEntry communityEntry = communityEntryMap.get(cid);
            if (communityEntry != null) {
                Set<ObjectId> userIds2 = sortMap.get(cid);
                if (userIds != null) {
                    for (ObjectId uid : userIds2) {
                        UserEntry userEntry = userEntryMap.get(uid);
                        if (userEntry != null) {
                            String name = StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName();
                            String str = nameMap.get(uid);
                            if (str != null && !str.equals("")) {
                                name = str;
                            }

                            index++;
                            rowItem = sheet.createRow(rowLine);

                            cellItem = rowItem.createCell(0);
                            cellItem.setCellValue(index + "");

                            cellItem = rowItem.createCell(1);
                            cellItem.setCellValue(name);

                            cellItem = rowItem.createCell(2);
                            cellItem.setCellValue(communityEntry.getCommunityName());

                            rowLine++;
                        }
                    }
                }
            }
        }
        String fileName = sheetName + ".xls";
        String userAgent = request.getHeader("USER-AGENT");
        HSSFUtils.exportExcel(userAgent, response, wb, fileName);

    }

    public Map<String,Object> selectOneUserList(ObjectId userId,ObjectId id,ObjectId communityId){
        Map<String,Object> map = new HashMap<String, Object>();
        ExtendedCourseEntry entry = extendedCourseDao.getEntryById(id);
        if(entry==null){//课程不存在
            return map;
        }
        ExtendedCourseDTO dto = new ExtendedCourseDTO(entry,1);
        List<NewVersionCommunityBindEntry> entries=newVersionCommunityBindDao.getStudentIdListByCommunityId(communityId);
        List<ExtendedUserApplyEntry> extendedUserApplyEntries = extendedUserApplyDao.getAllEntrysByCourseId(id,communityId);
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        List<ObjectId> unApplyList = new ArrayList<ObjectId>();
        for(NewVersionCommunityBindEntry entry1:entries){
            userIds.add(entry1.getUserId());
            unApplyList.add(entry1.getUserId());
        }
        for(ExtendedUserApplyEntry entry2:extendedUserApplyEntries){
            userIds.add(entry2.getUserId());
            unApplyList.remove(entry2.getUserId());
        }
        Map<ObjectId,UserEntry> userEntryMap = userDao.getUserEntryMap(userIds, Constant.FIELDS);
        Map<ObjectId,String> nameMap = newVersionBindRelationDao.getCommunitySonEntriesByMainUserId(userIds);
        CommunityEntry communityEntry = communityDao.findByObjectId(communityId);
        List<Map<String,Object>>  mapUnApplyList  = new ArrayList<Map<String, Object>>();
        List<Map<String,Object>>  mapApplyList  = new ArrayList<Map<String, Object>>();
        List<Map<String,Object>>  mapVoteList  = new ArrayList<Map<String, Object>>();
        for(ObjectId uid1 : unApplyList){
            UserEntry userEntry = userEntryMap.get(uid1);
            if(userEntry!=null){
                String name = StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
                String str = nameMap.get(uid1);
                if(str!=null && !str.equals("")){
                    name = str;
                }
                Map<String,Object> dtoMap1 = new HashMap<String, Object>();
                dtoMap1.put("name",name);
                dtoMap1.put("userId",uid1.toString());
                dtoMap1.put("avatar",AvatarUtils.getAvatar(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()));
                dtoMap1.put("status",0);
                mapUnApplyList.add(dtoMap1);
            }
        }
        for(ExtendedUserApplyEntry entry2:extendedUserApplyEntries){
            ObjectId uid1 = entry2.getUserId();
            if(true) {//报名
                UserEntry userEntry = userEntryMap.get(uid1);
                if (userEntry != null) {
                    String name = StringUtils.isNotBlank(userEntry.getNickName()) ? userEntry.getNickName() : userEntry.getUserName();
                    String str = nameMap.get(uid1);
                    if (str != null && !str.equals("")) {
                        name = str;
                    }
                    Map<String, Object> dtoMap1 = new HashMap<String, Object>();
                    dtoMap1.put("name", name);
                    dtoMap1.put("userId", uid1.toString());
                    dtoMap1.put("avatar", AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(), userEntry.getSex()));
                    if(entry2.getStatus()==2){
                        dtoMap1.put("status",1);
                    }else{
                        dtoMap1.put("status",0);
                    }
                    mapApplyList.add(dtoMap1);
                }
            }
            if(entry2.getStatus()==2){//成功
                UserEntry userEntry = userEntryMap.get(uid1);
                if(userEntry!=null){
                    String name = StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
                    String str = nameMap.get(uid1);
                    if(str!=null && !str.equals("")){
                        name = str;
                    }
                    Map<String,Object> dtoMap1 = new HashMap<String, Object>();
                    dtoMap1.put("name",name);
                    dtoMap1.put("userId",uid1.toString());
                    dtoMap1.put("avatar",AvatarUtils.getAvatar(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()));
                    dtoMap1.put("status",0);
                    mapVoteList.add(dtoMap1);
                }

            }

        }
        map.put("dto",dto);
        map.put("mapUnApplyList",mapUnApplyList);
        map.put("mapApplyList",mapApplyList);
        map.put("mapVoteList",mapVoteList);
        map.put("userId",userId.toString());
        map.put("community",new CommunityDTO(communityEntry));
        return map;
    }

    public void updateUserList(ObjectId id,ObjectId userId,ObjectId communityId,String sonIds) throws Exception{
        ExtendedCourseEntry extendedCourseEntry = extendedCourseDao.getEntryById(id);
        if(extendedCourseEntry==null){
            throw new Exception("该课程已被删除！");
        }
        //1.前置满足
        SchoolCommunityEntry schoolCommunityEntry = schoolCommunityDao.getEntryById(communityId);
        CommunityEntry communityEntry = communityDao.findByObjectId(communityId);
        if(schoolCommunityEntry==null){
            throw new Exception("该社群未绑定学校！");
        }
        if(communityEntry==null){
            throw new Exception("该社群已被删除！");
        }
        String gradeName = "";
        if(communityEntry.getGradeVal()!=null){
            gradeName = communityEntry.getGradeVal();
        }

        //成功添加
        String[] sonIdList = sonIds.split(",");
        //本次选择的
        List<ObjectId> selectList = new ArrayList<ObjectId>();
        for(String str:sonIdList){
            selectList.add(new ObjectId(str));
        }
        //该课程的所有参与者
        List<ExtendedUserApplyEntry> extendedUserApplyEntries = extendedUserApplyDao.getAllEntrysByCourseId(id);
        Set<ObjectId> userIds  = new HashSet<ObjectId>();
        Map<ObjectId,Set<ObjectId>> setMap = new HashMap<ObjectId, Set<ObjectId>>();
        Set<ObjectId> seList = new HashSet<ObjectId>();
        Set<ObjectId> applyList = new HashSet<ObjectId>();
        for(ExtendedUserApplyEntry extendedUserApplyEntry : extendedUserApplyEntries){
            ObjectId addUserId = extendedUserApplyEntry.getUserId();
            if(extendedUserApplyEntry.getStatus()==2){
                ObjectId addCommunityId = extendedUserApplyEntry.getCommunityId();
                userIds.add(addUserId);
                Set<ObjectId> communityMap = setMap.get(addCommunityId);
                if(communityMap==null){
                    Set<ObjectId> communityMap2 = new HashSet<ObjectId>();
                    communityMap2.add(addUserId);
                    setMap.put(addCommunityId,communityMap2);
                }else{
                    communityMap.add(addUserId);
                    setMap.put(addCommunityId,communityMap);
                }
                //入选者
                seList.add(addUserId);
                applyList.add(addUserId);
            }else if(extendedUserApplyEntry.getStatus()==1){
                //参选者
                applyList.add(addUserId);
            }

        }
        List<ObjectId> userSelectList = new ArrayList<ObjectId>();
        List<ObjectId> applyUserList = new ArrayList<ObjectId>();
        userSelectList.addAll(seList);
        applyUserList.addAll(applyList);
        //本次无选择，去除所有本社群记录
        if(sonIds.equals("")){
            Set<ObjectId> set = setMap.get(communityId);
            if(set!=null){
                List<ObjectId> objectIdList =  new ArrayList<ObjectId>();
                objectIdList.addAll(set);
                extendedUserApplyDao.delAllEntry(id,communityId,objectIdList);
                userSelectList.removeAll(objectIdList);
                applyUserList.removeAll(objectIdList);
                extendedCourseDao.saveEntry(extendedCourseEntry);
            }
            return;
        }
        //该班级人数
        Set<ObjectId> set = setMap.get(communityId);
        //总人数
        //加入选择的人
        if(set!=null){
            userIds.removeAll(set);
        }
        userIds.addAll(selectList);
        int allCount = userIds.size();
        if(allCount >extendedCourseEntry.getUserAllNumber()){//抢课
            throw new Exception("超过课程总人数限制！");
        }
        //班级人数
        int communityCount = selectList.size();
        if(extendedCourseEntry.getClassUserNumber() !=0 && communityCount >extendedCourseEntry.getClassUserNumber()){//抢课
            throw new Exception("超过课程班级限制人数限制！");
        }
        if(set!=null){
            set.removeAll(selectList);
            if(set.size()>0){
                List<ObjectId> objectIdList = new ArrayList<ObjectId>();
                objectIdList.addAll(set);
                extendedUserApplyDao.delAllEntry(id,communityId,objectIdList);
                userSelectList.removeAll(objectIdList);
                applyUserList.removeAll(objectIdList);
            }
        }

        Map<ObjectId,ExtendedUserApplyEntry> entryMap = extendedUserApplyDao.getAllEntryMapById(id, selectList);
        for(ObjectId sonId: selectList){
            ExtendedUserApplyEntry extendedUserApplyEntry = entryMap.get(sonId);
            if(extendedUserApplyEntry!=null){//无记录
                //报名状态改为老师调整
                extendedUserApplyEntry.setStatus(Constant.TWO);
                extendedUserApplyEntry.setApplyUserId(userId);
                extendedUserApplyEntry.setApplyType(Constant.TWO);
                extendedUserApplyEntry.setCommunityId(communityId);
                extendedUserApplyDao.saveEntry(extendedUserApplyEntry);
                //选课成功
                userSelectList.remove(sonId);
                userSelectList.add(sonId);
                extendedCourseEntry.setUserSelectedList(userSelectList);
                //报名成功
                applyUserList.remove(sonId);
                applyUserList.add(sonId);
                extendedCourseEntry.setUserApplyList(applyUserList);
            }else {
                //老师帮助选上
                ExtendedUserApplyEntry extendedUserApplyEntry1 = new ExtendedUserApplyEntry(
                        schoolCommunityEntry.getSchoolId(),
                        gradeName,
                        null,
                        communityEntry.getID(),
                        id,
                        sonId,
                        Constant.TWO,//默认抢上
                        userId,
                        Constant.TWO//家长帮抢
                );
                extendedUserApplyDao.saveEntry(extendedUserApplyEntry1);
                //选课成功
                userSelectList.remove(sonId);
                userSelectList.add(sonId);
                extendedCourseEntry.setUserSelectedList(userSelectList);
                //报名成功
                applyUserList.remove(sonId);
                applyUserList.add(sonId);
                extendedCourseEntry.setUserApplyList(applyUserList);
            }
        }
        extendedCourseDao.saveEntry(extendedCourseEntry);
    }

    public Map<String,Object> getAllCommunityAllUserList(ObjectId communityId,int status,int page,int pageSize) throws Exception{
        Map<String,Object> map = new HashMap<String, Object>();
        //获得社群
        CommunityEntry communityEntry1 = communityDao.findByObjectId(communityId);
        if(communityEntry1==null){
            throw new Exception("该社群不存在");
        }
        String gradeName = "";
        if(communityEntry1.getGradeVal()!=null){
            gradeName = communityEntry1.getGradeVal();
        }
        //获得班级学校
        SchoolCommunityEntry schoolCommunityEntry = schoolCommunityDao.getEntryById(communityId);
        if(schoolCommunityEntry==null){
            throw new Exception("该社群未绑定学校");
        }
        ObjectId schoolId = schoolCommunityEntry.getSchoolId();
        long current  = System.currentTimeMillis();
        //查询所有
        List<ExtendedCourseEntry> courseEntries = extendedCourseDao.getAllEntryList(gradeName, schoolId, "",status, current, page, pageSize);
        int count = extendedCourseDao.countAllEntryList(gradeName, schoolId, "", status, current);
        List<Map<String,Object>>  mapList  = new ArrayList<Map<String, Object>>();
        List<ObjectId> ids = new ArrayList<ObjectId>();
        Map<ObjectId,ExtendedCourseEntry> courseEntryMap = new HashMap<ObjectId, ExtendedCourseEntry>();
        for(ExtendedCourseEntry entry:courseEntries){
            ids.add(entry.getID());
            courseEntryMap.put(entry.getID(),entry);
        }
        List<ExtendedUserApplyEntry> extendedUserApplyEntries =  extendedUserApplyDao.getEntrysByCourseId3(ids, communityId);
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        Set<ObjectId> courseIds = new HashSet<ObjectId>();
        List<ObjectId> courseIds2 = new ArrayList<ObjectId>();
        Map<ObjectId,Set<ObjectId>> sortMap = new HashMap<ObjectId, Set<ObjectId>>();
        for(ExtendedUserApplyEntry entry2:extendedUserApplyEntries){
            userIds.add(entry2.getUserId());
            courseIds.add(entry2.getCourseId());
            courseIds2.add(entry2.getCourseId());
            Set<ObjectId> uids = sortMap.get(entry2.getCourseId());
            if(uids!=null){
                uids.add(entry2.getUserId());
                sortMap.put(entry2.getCourseId(),uids);
            }else{
                Set<ObjectId> uids2 = new HashSet<ObjectId>();
                uids2.add(entry2.getUserId());
                sortMap.put(entry2.getCourseId(),uids2);
            }
        }

        Map<ObjectId,UserEntry> userEntryMap = userDao.getUserEntryMap(userIds, Constant.FIELDS);
        Map<ObjectId,String> nameMap = newVersionBindRelationDao.getCommunitySonEntriesByMainUserId(userIds);

        for(ObjectId cid : ids){
            ExtendedCourseEntry extendedCourseEntry = courseEntryMap.get(cid);
            if(extendedCourseEntry!=null){
                ExtendedCourseDTO dto = new ExtendedCourseDTO(extendedCourseEntry,1);
                Set<ObjectId> userIds2 = sortMap.get(cid);
                Map<String,Object> mapDto = new HashMap<String, Object>();
                List<Map<String,Object>> newMapList = new ArrayList<Map<String, Object>>();
                if(userIds2!=null){
                    for(ObjectId uid:userIds2){
                        UserEntry userEntry = userEntryMap.get(uid);
                        if(userEntry!=null){
                            String name = StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
                            String str = nameMap.get(uid);
                            if(str!=null && !str.equals("")){
                                name = str;
                            }
                            Map<String,Object> mapDto2 = new HashMap<String, Object>();
                            mapDto2.put("name",name);
                            mapDto2.put("userId",uid.toString());
                            mapDto2.put("communityId",cid.toString());
                            newMapList.add(mapDto2);
                        }
                    }
                }
                mapDto.put("dto",dto);
                mapDto.put("status",0);
                mapDto.put("list",newMapList);
                mapList.add(mapDto);
            }
        }
        map.put("count",count);
        map.put("list",mapList);
        return map;
    }

    public void sendMessage(ObjectId id,List<ObjectId> ids){


    }


}
