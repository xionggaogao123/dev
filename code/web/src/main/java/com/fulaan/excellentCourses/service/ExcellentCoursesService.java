package com.fulaan.excellentCourses.service;

import com.db.backstage.LogMessageDao;
import com.db.backstage.PushMessageDao;
import com.db.backstage.TeacherApproveDao;
import com.db.excellentCourses.*;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.GroupDao;
import com.db.fcommunity.MemberDao;
import com.db.user.NewVersionBindRelationDao;
import com.db.user.NewVersionUserRoleDao;
import com.db.user.UserDao;
import com.fulaan.backstage.dto.LogMessageDTO;
import com.fulaan.cache.CacheHandler;
import com.fulaan.community.dto.CommunityDTO;
import com.fulaan.excellentCourses.dto.*;
import com.fulaan.newVersionBind.service.NewVersionBindService;
import com.fulaan.pojo.User;
import com.fulaan.service.CommunityService;
import com.fulaan.systemMessage.service.SystemMessageService;
import com.fulaan.wrongquestion.controller.DefaultWrongQuestionController;
import com.mongodb.DBObject;
import com.pojo.backstage.LogMessageType;
import com.pojo.backstage.PushMessageEntry;
import com.pojo.backstage.TeacherApproveEntry;
import com.pojo.excellentCourses.*;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.user.NewVersionBindRelationEntry;
import com.pojo.user.NewVersionUserRoleEntry;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by James on 2018-04-26.
 */
@Service
public class ExcellentCoursesService {

    private ExcellentCoursesDao excellentCoursesDao = new ExcellentCoursesDao();

    private HourClassDao hourClassDao = new HourClassDao();

    private ClassOrderDao classOrderDao = new ClassOrderDao();

    private UserBehaviorDao userBehaviorDao = new UserBehaviorDao();
    private RechargeResultDao rechargeResultDao = new RechargeResultDao();
    @Autowired
    private NewVersionBindService newVersionBindService;

    private UserDao userDao = new UserDao();

    private CoursesRoomDao coursesRoomDao = new CoursesRoomDao();

    private TeacherApproveDao teacherApproveDao = new TeacherApproveDao();

    private NewVersionBindRelationDao newVersionBindRelationDao=new NewVersionBindRelationDao();

    private CommunityDao communityDao = new CommunityDao();


    private UserAccountDao userAccountDao = new UserAccountDao();

    private AccountLogDao accountLogDao = new AccountLogDao();

    private AccountFrashDao accountFrashDao = new AccountFrashDao();

    private AccountOrderDao accountOrderDao = new AccountOrderDao();

    private RefundDao refundDao = new RefundDao();

    private ExtractCashDao extractCashDao = new ExtractCashDao();

    private LogMessageDao logMessageDao = new LogMessageDao();
    private NewVersionUserRoleDao newVersionUserRoleDao = new NewVersionUserRoleDao();
    @Autowired
    private SystemMessageService systemMessageService;
    @Autowired
    private CommunityService communityService;
    @Autowired
    private CoursesRoomService coursesRoomService;

    private UserClassDescDao userClassDescDao = new UserClassDescDao();

    private UserCCLoinDao userCCLoinDao = new UserCCLoinDao();

    private MemberDao memberDao = new MemberDao();

    private GroupDao groupDao = new GroupDao();

    private PushMessageDao pushMessageDao = new PushMessageDao();

    private CoursesBusinessDao coursesBusinessDao = new CoursesBusinessDao();

    private static final Logger logger =Logger.getLogger(DefaultWrongQuestionController.class);


    private static final int  TEACHER_TIME = 24*60*60*1000;  //老师提前时间  ->  60分钟
    private static final int  STUDENT_TIME = 5*60*1000;  //学生提前时间  ->  5分钟
    private static final int  TUI_TIME = 12*60*60*1000;  //退款超时时间  ->  12小时
    private static final int  CURRENT_TIME = 100*60*1000; //课程持续时间
   public static final String[] arg1 = {"http://7xiclj.com1.z0.glb.clouddn.com/5b2cd0143d4df93a9389fa73.jpg",
                                        "http://7xiclj.com1.z0.glb.clouddn.com/5b2cd0143d4df93a9389fa73.jpg",
                                        "http://7xiclj.com1.z0.glb.clouddn.com/5b2cd0493d4df93a9389fde3.jpg",
                                        "http://7xiclj.com1.z0.glb.clouddn.com/5b2cd04e3d4df93a9389fdfe.jpg",
                                        "http://7xiclj.com1.z0.glb.clouddn.com/5b2cd0543d4df93a9389fe56.jpg",
                                        "http://7xiclj.com1.z0.glb.clouddn.com/5b2cd0e53d4df93a938a05b6.jpg",
                                        "http://7xiclj.com1.z0.glb.clouddn.com/5b2cd0ed3d4df93a938a05cb.jpg",
                                        "http://7xiclj.com1.z0.glb.clouddn.com/5b2cd0f13d4df93a938a0609.jpg",
                                        "http://7xiclj.com1.z0.glb.clouddn.com/5b2cd0f53d4df93a938a0639.jpg",
                                        "http://7xiclj.com1.z0.glb.clouddn.com/5b2cd15d3d4df93a938a0b25.jpg",
                                        "http://7xiclj.com1.z0.glb.clouddn.com/5b2cd1613d4df93a938a0b54.jpg"};//大图

   public static final String[] arg2 = {"http://7xiclj.com1.z0.glb.clouddn.com/5b2cd0463d4df93a9389fddb.jpg",
                                        "http://7xiclj.com1.z0.glb.clouddn.com/5b2cd0463d4df93a9389fddb.jpg",
                                        "http://7xiclj.com1.z0.glb.clouddn.com/5b2cd04c3d4df93a9389fdfc.jpg",
                                        "http://7xiclj.com1.z0.glb.clouddn.com/5b2cd0503d4df93a9389fe0b.jpg",
                                        "http://7xiclj.com1.z0.glb.clouddn.com/5b2cd0563d4df93a9389fe7c.jpg",
                                        "http://7xiclj.com1.z0.glb.clouddn.com/5b2cd0e73d4df93a938a05b7.jpg",
                                        "http://7xiclj.com1.z0.glb.clouddn.com/5b2cd0ef3d4df93a938a05f1.jpg",
                                        "http://7xiclj.com1.z0.glb.clouddn.com/5b2cd0f33d4df93a938a061c.jpg",
                                        "http://7xiclj.com1.z0.glb.clouddn.com/5b2cd0f73d4df93a938a064e.jpg",
                                        "http://7xiclj.com1.z0.glb.clouddn.com/5b2cd15f3d4df93a938a0b35.jpg",
                                        "http://7xiclj.com1.z0.glb.clouddn.com/5b2cd1633d4df93a938a0b5f.jpg"};//小图


    /**
     * 充值消费日志
     */
    public void addLog(ObjectId userId,ObjectId contactId,String description){
        AccountLogEntry accountLogEntry = new AccountLogEntry(userId,contactId,description);
        accountLogDao.addEntry(accountLogEntry);
    }
    /**
     * 老师开课
     * @param dto
     * @param userId
     * @return
     */
    public String addEntry(ExcellentCoursesDTO dto,ObjectId userId){
        String str = dto.getId();
        if(str!=null && !str.equals("")){
            //ExcellentCoursesEntry excellentCoursesEntry2 = dto.buildAddEntry();
            ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(new ObjectId(str));
            excellentCoursesEntry.setCreateTime(new Date().getTime());
            List<ObjectId> oids2 = new ArrayList<ObjectId>();
            List<String> stringList = dto.getCommunityIdList();
            for(String str3 : stringList){
                oids2.add(new ObjectId(str3));
            }
            excellentCoursesEntry.setCommunityIdList(oids2);
            excellentCoursesEntry.setUserId(userId);
            excellentCoursesEntry.setUserName(dto.getUserName());
            excellentCoursesEntry.setSubjectId(new ObjectId(dto.getSubjectId()));
            excellentCoursesEntry.setSubjectName(dto.getSubjectName());
            excellentCoursesEntry.setTitle(dto.getTitle());
            excellentCoursesEntry.setTarget(dto.getTarget());
            excellentCoursesEntry.setOpen(dto.getOpen());
            str = excellentCoursesDao.addEntry(excellentCoursesEntry);
        }else{
            dto.setUserId(userId.toString());
            Random random = new Random();
            int i1 = random.nextInt(10)+1;
            dto.setCover(arg2[i1]);
            dto.setBigCover(arg1[i1]);
            //初次创建   状态为  未发布
            dto.setStatus(0);
            ExcellentCoursesEntry excellentCoursesEntry = dto.buildAddEntry();
            if(dto.getId()!=null && !dto.getId().equals("")){
                excellentCoursesEntry.setID(new ObjectId(dto.getId()));
            }
            str = excellentCoursesDao.addEntry(excellentCoursesEntry);
        }
        return str;
    }

    /**
     * 老师批量增加课时
     * @param dto
     * @param userId
     * @return
     * @throws Exception
     */
    public String addEntry(HourResultDTO dto,ObjectId userId)throws Exception{
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(new ObjectId(dto.getParentId()));
        long oldEnd = 0l;
        if(excellentCoursesEntry !=null){
            List<HourClassEntry> entryList =new ArrayList<HourClassEntry>();
            List<HourClassDTO> dtoList = dto.getDtos();
            double oldPrice = 0.00;
            long st = 0l;
            long et = 0l;
            for(HourClassDTO dto1:dtoList){
                dto1.setType(Constant.ZERO);
                dto1.setUserId(userId.toString());
                dto1.setWeek(getWeek(dto1.getDateTime()));
                dto1.setParentId(dto.getParentId());
                //转换
                dto1.setClassOldPrice(getTwoDouble(dto1.getClassOldPrice()));
                HourClassEntry classEntry =  dto1.buildAddEntry();
                oldPrice = sum(oldPrice,dto1.getClassOldPrice());
                long st2 = classEntry.getStartTime();
                long et2 = classEntry.getStartTime()+classEntry.getCurrentTime();
                if(st2< oldEnd+classEntry.getCurrentTime()){
                    throw new Exception("第"+dto1.getOrder()+"课节与上一课节间隔太短，两节课间隔不少于"+classEntry.getCurrentTime()/60000+"分钟！");
                }
                oldEnd = st2;
                if(st==0l){
                    st = st2;
                }else{
                    if(st2<st){
                        st = st2;
                    }
                }
                if(et==0l){
                    et = et2;
                }else{
                    if(et <et2){
                        et = et2;
                    }
                }
                entryList.add(classEntry);
            }
            hourClassDao.delEntry(new ObjectId(dto.getParentId()),userId);
            this.addEntryBatch(entryList);
            excellentCoursesEntry.setAllClassCount(entryList.size());
            excellentCoursesEntry.setOldPrice(oldPrice);
            excellentCoursesEntry.setStartTime(st);
            excellentCoursesEntry.setEndTime(et);
            excellentCoursesDao.addEntry(excellentCoursesEntry);
        }else{
            throw new Exception("课程不存在！");
        }

        return "";
    }


    /**
     * 老师批量增加课时
     * @param dto
     * @param userId
     * @return
     * @throws Exception
     */
    public String addNewEntry(HourResultDTO dto,ObjectId userId)throws Exception{
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(new ObjectId(dto.getParentId()));
        long oldEnd = 0l;
        Set<ObjectId> teacherList = new HashSet<ObjectId>();
        if(excellentCoursesEntry !=null){
            List<HourClassEntry> entryList =new ArrayList<HourClassEntry>();
            List<HourClassDTO> dtoList = dto.getDtos();
            double oldPrice = 0.00;
            long st = 0l;
            long et = 0l;
            double price = getTwoDouble(dto.getPrice()/dtoList.size());
            for(HourClassDTO dto1:dtoList){
                dto1.setType(Constant.ZERO);
                dto1.setUserId(userId.toString());
                dto1.setWeek(getWeek(dto1.getDateTime()));
                dto1.setParentId(dto.getParentId());
                //转换
                if(dto.getType()==1){//总价类型
                    dto1.setClassOldPrice(price);
                }else{
                    dto1.setClassOldPrice(getTwoDouble(dto1.getClassOldPrice()));
                }
                if(dto1.getOwnId() !=null && dto1.getOwnId().equals("无")){
                    dto1.setOwnId(userId.toString());
                }
                HourClassEntry classEntry =  dto1.buildAddEntry();
                oldPrice = sum(oldPrice,dto1.getClassOldPrice());
                long st2 = classEntry.getStartTime();
                long et2 = classEntry.getStartTime()+classEntry.getCurrentTime();
                if(st2< oldEnd+classEntry.getCurrentTime()){
                    throw new Exception("第"+dto1.getOrder()+"课节与上一课节间隔太短，两节课间隔不少于"+classEntry.getCurrentTime()/60000+"分钟！");
                }
                oldEnd = st2;
                if(st==0l){
                    st = st2;
                }else{
                    if(st2<st){
                        st = st2;
                    }
                }
                if(et==0l){
                    et = et2;
                }else{
                    if(et <et2){
                        et = et2;
                    }
                }
                entryList.add(classEntry);
                //修改
                if(classEntry.getOwnId()!=null){
                    teacherList.add(classEntry.getOwnId());
                }


            }
            hourClassDao.delEntry(new ObjectId(dto.getParentId()),userId);
            this.addEntryBatch(entryList);
            excellentCoursesEntry.setAllClassCount(entryList.size());
            if(dto.getType()==1){
                excellentCoursesEntry.setOldPrice(dto.getPrice());
                excellentCoursesEntry.setCourseType(1);
            }else{
                excellentCoursesEntry.setOldPrice(oldPrice);
                excellentCoursesEntry.setCourseType(0);
            }
            excellentCoursesEntry.setStartTime(st);
            excellentCoursesEntry.setEndTime(et);
            //修改
            List<ObjectId> objectIdList = new ArrayList<ObjectId>();
            objectIdList.addAll(teacherList);
            excellentCoursesEntry.setTeacherIdList(objectIdList);
            excellentCoursesDao.addEntry(excellentCoursesEntry);
        }else{
            throw new Exception("课程不存在！");
        }

        return "";
    }
    public int getWeek(String dateTime){
        long startNum = DateTimeUtils.getStrToLongTime(dateTime, "yyyy-MM-dd");
        Date date = new Date(startNum);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if(w==0){
            w= 7;
        }
        return w;
    }


    /**
     * 批量增加课时
     * @param list
     */
    public void addEntryBatch(List<HourClassEntry> list) {
        List<DBObject> dbList = new ArrayList<DBObject>();
        for (int i = 0; list != null && i < list.size(); i++) {
            HourClassEntry si = list.get(i);
            dbList.add(si.getBaseEntry());
        }
        //导入新纪录
        if(dbList.size()>0) {
            hourClassDao.addBatch(dbList);
        }
    }

    /**
     * 开课提交申请
     * @param id
     * @param userId
     */
    public void finishEntry(ObjectId id,ObjectId userId){
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        if(excellentCoursesEntry!=null && excellentCoursesEntry.getUserId().equals(userId)){//存在且为本人创建
            //todo  大V
            TeacherApproveEntry teacherApproveEntry = teacherApproveDao.getEntry(userId);
            if(teacherApproveEntry!=null && teacherApproveEntry.getType()==2){
                //大V 为申请状态
                excellentCoursesDao.finishEntry(id,1,new Date().getTime());
            }else{
                //非大V 为编辑状态
                excellentCoursesDao.finishEntry(id,0,new Date().getTime());
            }

        }

    }


    /**
     * 学生首页加载
     * @param userId
     * @return
     */
    public Map<String,Object> getMyCoursesList(ObjectId userId){
        Map<String,Object> map = new HashMap<String, Object>();
        List<ClassOrderEntry> classOrderEntries =  classOrderDao.getEntry(userId);
        //获得学生所在社群
        List<ObjectId> objectIdList = newVersionBindService.getCommunityIdsByUserId(userId);
        long current = System.currentTimeMillis();
        //推荐名单
        List<ExcellentCoursesEntry> coursesEntries = excellentCoursesDao.getEntryList(objectIdList,current);
        //Map<ObjectId, CommunityEntry> map3 = communityDao.findMapInfo(objectIdList);
        List<ObjectId> objectIdList1 = new ArrayList<ObjectId>();
        List<ObjectId> hourClassIds = new ArrayList<ObjectId>();
        for(ClassOrderEntry classOrderEntry:classOrderEntries){
            if(classOrderEntry.getIsBuy()==1){
                objectIdList1.add(classOrderEntry.getContactId());
                hourClassIds.add(classOrderEntry.getParentId());
            }
        }
        //objectIdList1
        List<ExcellentCoursesEntry> coursesEntries2 = excellentCoursesDao.getEntryListById(objectIdList1,current);
        List<ExcellentCoursesDTO> dtos = new ArrayList<ExcellentCoursesDTO>();
        List<ExcellentCoursesDTO> dtos2 = new ArrayList<ExcellentCoursesDTO>();
        for(ExcellentCoursesEntry excellentCoursesEntry:coursesEntries){//推荐
            if(!objectIdList1.contains(excellentCoursesEntry.getID())){
                ExcellentCoursesDTO dto = new ExcellentCoursesDTO(excellentCoursesEntry);
                dto.setIsBuy(0);
                dtos2.add(dto);
            }
        }
        Map<String,ExcellentCoursesDTO> dtoMap = new HashMap<String, ExcellentCoursesDTO>();
        for(ExcellentCoursesEntry excellentCoursesEntry:coursesEntries2){//已购买
            ExcellentCoursesDTO dto = new ExcellentCoursesDTO(excellentCoursesEntry);
            dto.setIsBuy(1);
           // dtos.add(dto);
            dtoMap.put(dto.getId(),dto);
        }
        //Set<ObjectId> objectIds = new HashSet<ObjectId>();
        //objectIds.addAll(objectIdList1);
        List<ObjectId>  oids = new ArrayList<ObjectId>();
        for(ObjectId oid : objectIdList1){
            if(oids.contains(oid)){

            }else{
                oids.add(oid);
                ExcellentCoursesDTO dto1 = dtoMap.get(oid.toString());
                if(dto1!=null){
                    dtos.add(dto1);
                }
            }
        }
        dtos.addAll(dtos2);
        map.put("list",dtos);
        map.put("count",dtos.size());
        //map.put("dto",getNowEntry(hourClassIds));
        return map;
    }


    /**
     * 学生首页加载
     * @param userId
     * @return
     */
    public Map<String,Object> getMyChildCoursesList(ObjectId userId){
        Map<String,Object> map = new HashMap<String, Object>();
        List<ClassOrderEntry> classOrderEntries =  classOrderDao.getEntry(userId);
        //获得学生所在社群
        List<ObjectId> objectIdList = newVersionBindService.getCommunityIdsByUserId(userId);
        long current = System.currentTimeMillis();
        //推荐名单
        List<ExcellentCoursesEntry> coursesEntries = excellentCoursesDao.getEntryList(objectIdList,current);
        //Map<ObjectId, CommunityEntry> map3 = communityDao.findMapInfo(objectIdList);
        List<ObjectId> objectIdList1 = new ArrayList<ObjectId>();
        List<ObjectId> hourClassIds = new ArrayList<ObjectId>();
        for(ClassOrderEntry classOrderEntry:classOrderEntries){
            if(classOrderEntry.getIsBuy()==1){
                objectIdList1.add(classOrderEntry.getContactId());
                hourClassIds.add(classOrderEntry.getParentId());
            }
        }
        //objectIdList1
        List<ExcellentCoursesEntry> coursesEntries2 = excellentCoursesDao.getEntryListById(objectIdList1,current);
        List<ExcellentCoursesDTO> dtos = new ArrayList<ExcellentCoursesDTO>();
        List<ExcellentCoursesDTO> dtos2 = new ArrayList<ExcellentCoursesDTO>();
        for(ExcellentCoursesEntry excellentCoursesEntry:coursesEntries){//推荐
            if(!objectIdList1.contains(excellentCoursesEntry.getID())){
                ExcellentCoursesDTO dto = new ExcellentCoursesDTO(excellentCoursesEntry);
                dto.setIsBuy(0);
                dtos2.add(dto);
            }
        }
        Map<String,ExcellentCoursesDTO> dtoMap = new HashMap<String, ExcellentCoursesDTO>();
        for(ExcellentCoursesEntry excellentCoursesEntry:coursesEntries2){//已购买
            ExcellentCoursesDTO dto = new ExcellentCoursesDTO(excellentCoursesEntry);
            dto.setIsBuy(1);
            // dtos.add(dto);
            dtoMap.put(dto.getId(),dto);
        }
        //Set<ObjectId> objectIds = new HashSet<ObjectId>();
        //objectIds.addAll(objectIdList1);
        List<ObjectId>  oids = new ArrayList<ObjectId>();
        for(ObjectId oid : objectIdList1){
            if(oids.contains(oid)){

            }else{
                oids.add(oid);
                ExcellentCoursesDTO dto1 = dtoMap.get(oid.toString());
                if(dto1!=null){
                    dtos.add(dto1);
                }
            }
        }
        dtos.addAll(dtos2);
        map.put("list",dtos);
        map.put("count",dtos.size());
        //map.put("dto",getNowEntry(hourClassIds));
        return map;
    }
    /**
     * 家长孩子们的购买课程
     * @param userId
     * @return
     */
    public Map<String,Object> getMyChildBuyCoursesList(ObjectId userId){
        Map<String,Object> map = new HashMap<String, Object>();
        List<ClassOrderEntry> classOrderEntries =  classOrderDao.getEntry(userId);
        long current = System.currentTimeMillis();
        List<ObjectId> objectIdList1 = new ArrayList<ObjectId>();
        List<ObjectId> hourClassIds = new ArrayList<ObjectId>();
        for(ClassOrderEntry classOrderEntry:classOrderEntries){
            if(classOrderEntry.getIsBuy()==1){
                objectIdList1.add(classOrderEntry.getContactId());
                hourClassIds.add(classOrderEntry.getParentId());//5a17dafd0a9d324986663c9a
            }
        }
        Map<String,ExcellentCoursesDTO> dtoMap = new HashMap<String, ExcellentCoursesDTO>();
        List<ExcellentCoursesEntry> coursesEntries2 = excellentCoursesDao.getEntryListById(objectIdList1,current);
        List<ExcellentCoursesDTO> dtos = new ArrayList<ExcellentCoursesDTO>();
        for(ExcellentCoursesEntry excellentCoursesEntry:coursesEntries2){//已购买
            ExcellentCoursesDTO dto = new ExcellentCoursesDTO(excellentCoursesEntry);
            dto.setIsBuy(1);
            dtoMap.put(dto.getId(),dto);
        }
        //重新排序
        List<ObjectId>  oids = new ArrayList<ObjectId>();
        for(ObjectId oid : objectIdList1){
            if(oids.contains(oid)){

            }else{
                oids.add(oid);
                ExcellentCoursesDTO dto1 = dtoMap.get(oid.toString());
                if(dto1!=null){
                    dtos.add(dto1);
                }
            }
        }
        map.put("list",dtos);
        map.put("count",dtos.size());
        //map.put("dto",getNowEntry(hourClassIds));
        return map;
    }

    /**
     * 家长孩子们的推荐课程
     * @param userId
     * @return
     */
    public Map<String,Object> getMyChildCommunityCoursesList(ObjectId userId,ObjectId id){
        Map<String,Object> map = new HashMap<String, Object>();
        List<ClassOrderEntry> classOrderEntries =  classOrderDao.getEntry(userId);
        List<ExcellentCoursesEntry> coursesEntries  = new ArrayList<ExcellentCoursesEntry>();
        if(userId.equals(id)){//自己
            //获得家长所在社群
            List<ObjectId> objectIdList = communityService.getCommunitys3(id, 1, 100);
            long current = System.currentTimeMillis();
            //推荐名单
            coursesEntries = excellentCoursesDao.getEntryList(objectIdList,current);

        }else{//孩子
            //获得学生所在社群
            List<ObjectId> objectIdList = newVersionBindService.getCommunityIdsByUserId(userId);
            long current = System.currentTimeMillis();
            //推荐名单
            coursesEntries = excellentCoursesDao.getEntryList(objectIdList,current);
        }


        List<ObjectId> objectIdList1 = new ArrayList<ObjectId>();
        List<ObjectId> hourClassIds = new ArrayList<ObjectId>();
        for(ClassOrderEntry classOrderEntry:classOrderEntries){
            if(classOrderEntry.getIsBuy()==1){
                objectIdList1.add(classOrderEntry.getContactId());
                hourClassIds.add(classOrderEntry.getParentId());
            }
        }

        List<ExcellentCoursesDTO> dtos2 = new ArrayList<ExcellentCoursesDTO>();
        for(ExcellentCoursesEntry excellentCoursesEntry:coursesEntries){//推荐
            if(!objectIdList1.contains(excellentCoursesEntry.getID())){
                ExcellentCoursesDTO dto = new ExcellentCoursesDTO(excellentCoursesEntry);
                dto.setIsBuy(0);
                dtos2.add(dto);
            }
        }
        map.put("list",dtos2);
        map.put("count",dtos2.size());
        return map;
    }

    /**
     * 学生首页加载
     * @param userId
     * @return
     */
    public Map<String,Object> getOldMyCoursesList(ObjectId userId){
        Map<String,Object> map = new HashMap<String, Object>();
        //List<ClassOrderEntry> classOrderEntries =  classOrderDao.getEntry(userId);
        long current = System.currentTimeMillis();
        List<ClassOrderEntry> classOrderEntries =  classOrderDao.getEntry(userId);
        List<ObjectId> objectIdList1 = new ArrayList<ObjectId>();
        List<ObjectId> hourClassIds = new ArrayList<ObjectId>();
        for(ClassOrderEntry classOrderEntry:classOrderEntries){
            if(classOrderEntry.getIsBuy()==1){
                objectIdList1.add(classOrderEntry.getContactId());
                hourClassIds.add(classOrderEntry.getParentId());
            }
        }
        //objectIdList1
        List<ExcellentCoursesEntry> coursesEntries2 = excellentCoursesDao.getEntryListById4(objectIdList1);
        Collections.sort(coursesEntries2, new Comparator<ExcellentCoursesEntry>() {
            @Override
            public int compare(ExcellentCoursesEntry o1, ExcellentCoursesEntry o2) {
                if (o1.getEndTime() > o2.getEndTime()) {
                    return 1;
                } else if (o1.getEndTime() == o2.getEndTime()) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
        // update 已结束课程中不在显示已结束未购买的课程
        //获得学生所在社群
      /*  List<ObjectId> objectIdList = newVersionBindService.getCommunityIdsByUserId(userId);
        //推荐名单
        List<ExcellentCoursesEntry> coursesEntries = excellentCoursesDao.getOldEntryList(objectIdList, current);
        Collections.sort(coursesEntries, new Comparator<ExcellentCoursesEntry>() {
            @Override
            public int compare(ExcellentCoursesEntry o1, ExcellentCoursesEntry o2) {
                if (o1.getEndTime() > o2.getEndTime()) {
                    return 1;
                } else if (o1.getEndTime() == o2.getEndTime()) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });*/
        //新增
        List<ExcellentCoursesEntry> coursesEntries = new ArrayList<ExcellentCoursesEntry>();
        for(ExcellentCoursesEntry excellentCoursesEntry:coursesEntries2){
            if(excellentCoursesEntry.getEndTime()<current){
                coursesEntries.add(excellentCoursesEntry);
            }
        }
        Collections.reverse(coursesEntries);
        /*//Map<ObjectId, CommunityEntry> map3 = communityDao.findMapInfo(objectIdList);
        List<ObjectId> objectIdList1 = new ArrayList<ObjectId>();
        List<ObjectId> hourClassIds = new ArrayList<ObjectId>();
        for(ClassOrderEntry classOrderEntry:classOrderEntries){
            if(classOrderEntry.getIsBuy()==1){
                objectIdList1.add(classOrderEntry.getContactId());
                hourClassIds.add(classOrderEntry.getParentId());
            }
        }
        //objectIdList1
        List<ExcellentCoursesEntry> coursesEntries2 = excellentCoursesDao.getOldEntryList(objectIdList1, current);
        List<ExcellentCoursesDTO> dtos = new ArrayList<ExcellentCoursesDTO>();
        List<ExcellentCoursesDTO> dtos2 = new ArrayList<ExcellentCoursesDTO>();
        for(ExcellentCoursesEntry excellentCoursesEntry:coursesEntries){//推荐
            if(!objectIdList1.contains(excellentCoursesEntry.getID())){
                ExcellentCoursesDTO dto = new ExcellentCoursesDTO(excellentCoursesEntry);
                dto.setIsBuy(0);
                dtos2.add(dto);
            }
        }
        Map<String,ExcellentCoursesDTO> dtoMap = new HashMap<String, ExcellentCoursesDTO>();
        for(ExcellentCoursesEntry excellentCoursesEntry:coursesEntries2){//已购买
            ExcellentCoursesDTO dto = new ExcellentCoursesDTO(excellentCoursesEntry);
            dto.setIsBuy(1);
            // dtos.add(dto);
            dtoMap.put(dto.getId(),dto);
        }
        //Set<ObjectId> objectIds = new HashSet<ObjectId>();
        //objectIds.addAll(objectIdList1);
        List<ObjectId>  oids = new ArrayList<ObjectId>();
        for(ObjectId oid : objectIdList1){
            if(oids.contains(oid)){

            }else{
                oids.add(oid);
                ExcellentCoursesDTO dto1 = dtoMap.get(oid.toString());
                if(dto1!=null){
                    dtos.add(dto1);
                }
            }
        }*/
        List<ObjectId> objectIdList2 = new ArrayList<ObjectId>();
        List<ExcellentCoursesDTO> dtos2 = new ArrayList<ExcellentCoursesDTO>();

        for(ExcellentCoursesEntry excellentCoursesEntry:coursesEntries){//推荐
            if(objectIdList2.contains(excellentCoursesEntry.getID())){

            }else{
                ExcellentCoursesDTO dto = new ExcellentCoursesDTO(excellentCoursesEntry);
                dto.setIsBuy(0);
                dtos2.add(dto);
                objectIdList2.add(excellentCoursesEntry.getID());
            }

        }
        //dtos.addAll(dtos2);
        //Collections.reverse(dtos2);

        map.put("list",dtos2);
        map.put("count",dtos2.size());
        //map.put("dto",getNowEntry(hourClassIds));
        return map;
    }

    /**
     * 学生首页加载
     * @param userId
     * @return
     */
    public Map<String,Object> getMyChildOldCoursesList(ObjectId userId,ObjectId id){
        Map<String,Object> map = new HashMap<String, Object>();
        long current = System.currentTimeMillis();
        List<ClassOrderEntry> classOrderEntries =  classOrderDao.getEntry(userId);
        List<ObjectId> objectIdList1 = new ArrayList<ObjectId>();
        List<ObjectId> hourClassIds = new ArrayList<ObjectId>();
        for(ClassOrderEntry classOrderEntry:classOrderEntries){
            if(classOrderEntry.getIsBuy()==1){
                objectIdList1.add(classOrderEntry.getContactId());
                hourClassIds.add(classOrderEntry.getParentId());
            }
        }
        //objectIdList1
        List<ExcellentCoursesEntry> coursesEntries2 = excellentCoursesDao.getEntryListById4(objectIdList1);
        //获得学生所在社群
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        if(userId.equals(id)){//自己
            //获得家长所在社群
            objectIdList = communityService.getCommunitys3(id, 1, 100);
        }else{//孩子
            //获得学生所在社群
           objectIdList = newVersionBindService.getCommunityIdsByUserId(userId);
        }
        //推荐名单
        List<ExcellentCoursesEntry> coursesEntries = excellentCoursesDao.getOldEntryList(objectIdList, current);
        for(ExcellentCoursesEntry excellentCoursesEntry:coursesEntries2){
            if(excellentCoursesEntry.getEndTime()<current){
                coursesEntries.add(excellentCoursesEntry);
            }
        }
        List<ObjectId> objectIdList2 = new ArrayList<ObjectId>();
        List<ExcellentCoursesDTO> dtos2 = new ArrayList<ExcellentCoursesDTO>();
        for(ExcellentCoursesEntry excellentCoursesEntry:coursesEntries){//推荐
            if(objectIdList2.contains(excellentCoursesEntry.getID())){

            }else{
                ExcellentCoursesDTO dto = new ExcellentCoursesDTO(excellentCoursesEntry);
                dto.setIsBuy(0);
                dtos2.add(dto);
                objectIdList2.add(excellentCoursesEntry.getID());
            }
        }

        map.put("list",dtos2);
        map.put("count",dtos2.size());
        return map;
    }

    /**
     * 学生首页加载（不包含推荐课程）
     * @param userId
     * @return
     */
    public Map<String,Object> getNewMyChildOldCoursesList(ObjectId userId,ObjectId id){
        Map<String,Object> map = new HashMap<String, Object>();
        long current = System.currentTimeMillis();
        List<ClassOrderEntry> classOrderEntries =  classOrderDao.getEntry(userId);
        List<ObjectId> objectIdList1 = new ArrayList<ObjectId>();
        List<ObjectId> hourClassIds = new ArrayList<ObjectId>();
        for(ClassOrderEntry classOrderEntry:classOrderEntries){
            if(classOrderEntry.getIsBuy()==1){
                objectIdList1.add(classOrderEntry.getContactId());
                hourClassIds.add(classOrderEntry.getParentId());
            }
        }
        //objectIdList1
        List<ExcellentCoursesEntry> coursesEntries2 = excellentCoursesDao.getEntryListById4(objectIdList1);

        //推荐名单
        List<ExcellentCoursesEntry> coursesEntries = new ArrayList<ExcellentCoursesEntry>();
        for(ExcellentCoursesEntry excellentCoursesEntry:coursesEntries2){
            if(excellentCoursesEntry.getEndTime()<current){
                coursesEntries.add(excellentCoursesEntry);
            }
        }
        List<ExcellentCoursesDTO> dtos2 = new ArrayList<ExcellentCoursesDTO>();
        for(ExcellentCoursesEntry excellentCoursesEntry:coursesEntries){//推荐
            ExcellentCoursesDTO dto = new ExcellentCoursesDTO(excellentCoursesEntry);
            dto.setIsBuy(1);
            dtos2.add(dto);
        }
        map.put("list",dtos2);
        map.put("count",dtos2.size());
        return map;
    }

    /**
     * 添加收藏
     * @param userId
     * @param type
     * @param id
     */
    public void addCollect(ObjectId userId,int type,ObjectId id){
        UserBehaviorEntry userBehaviorEntry = userBehaviorDao.getEntry(userId);
        if(userBehaviorEntry!=null){
            if(type==0){//取消
                List<ObjectId> objectIdList = userBehaviorEntry.getCollectList();
                //Set<ObjectId> objectIds  = new HashSet<ObjectId>();
                //objectIds.addAll(objectIdList);
                objectIdList.remove(id);//取消
                //List<ObjectId>  objectIdList2 = new ArrayList<ObjectId>();
                //objectIdList2.addAll(objectIdList);
                userBehaviorEntry.setCollectList(objectIdList);
                userBehaviorDao.addEntry(userBehaviorEntry);
            }else if(type==1){
                List<ObjectId> objectIdList = userBehaviorEntry.getCollectList();
               // Set<ObjectId> objectIds  = new HashSet<ObjectId>();
                //objectIds.addAll(objectIdList);
                objectIdList.add(id);//添加
               // List<ObjectId>  objectIdList2 = new ArrayList<ObjectId>();
                //objectIdList2.addAll(objectIdList);
                userBehaviorEntry.setCollectList(objectIdList);
                userBehaviorDao.addEntry(userBehaviorEntry);
            }

        }else{
            if(type==0){

            }else if(type ==1){
                List<ObjectId>  objectIdList = new ArrayList<ObjectId>();
                List<ObjectId>  objectIdList2 = new ArrayList<ObjectId>();
                objectIdList2.add(id);
                UserBehaviorEntry userBehaviorEntry1 = new UserBehaviorEntry(userId,0,objectIdList,objectIdList2,objectIdList);
                userBehaviorDao.addEntry(userBehaviorEntry1);
            }
        }

    }

    public  Map<String,Object>  getSimpleCourses(ObjectId userId){
        List<ObjectId> hourClassIds =  classOrderDao.getObjectIdEntry(userId);
        Map<String,Object> dto = this.getNowEntry(hourClassIds);
        return dto;
    }

    public  Map<String,Object>  getNewSimpleCourses(ObjectId userId){
        List<ObjectId> hourClassIds =  classOrderDao.getObjectIdEntry(userId);
        Map<String,Object> dto = this.getNewNowEntry(hourClassIds);
        return dto;
    }

    public Map<String,Object> getNowEntry(List<ObjectId> hourClassIds){
        //用户订单查询
        List<HourClassEntry> hourClassEntries = hourClassDao.getEntryList(hourClassIds);
        Map<String,Object> map = new HashMap<String, Object>();
        HourClassEntry entry = null;
        long current = System.currentTimeMillis();
        /*long current = System.currentTimeMillis();
        long start = hourClassEntry.getStartTime() -5*60*1000;
        long end = start + hourClassEntry.getCurrentTime();*/
        for(HourClassEntry hourClassEntry:hourClassEntries){
            long startTime =  hourClassEntry.getStartTime()-STUDENT_TIME;
            long endTime = hourClassEntry.getStartTime() + hourClassEntry.getCurrentTime();
            if(entry ==null){
                if(endTime>current){
                    entry = hourClassEntry;
                }
            }else{
                if(endTime>current && startTime <current){
                    entry = hourClassEntry;
                }
            }
        }

        if(entry != null && entry.getStartTime()-STUDENT_TIME<current){
            map.put("start",1);//正在上课
            map.put("time",0);
        }else{
            if(entry==null){
                map.put("start",0);//未上课
                map.put("time",0);
            }else{
                map.put("start",0);//未上课
                map.put("time",(entry.getStartTime()-current)/1000);
            }

        }
        map.put("dto",new HourClassDTO(entry));
        return map;
    }

    public Map<String,Object> getNewNowEntry(List<ObjectId> hourClassIds){
        //用户订单查询
        List<HourClassEntry> hourClassEntries = hourClassDao.getEntryList(hourClassIds);
        Map<String,Object> map = new HashMap<String, Object>();
        HourClassEntry entry = null;
        long current = System.currentTimeMillis();
        /*long current = System.currentTimeMillis();
        long start = hourClassEntry.getStartTime() -5*60*1000;
        long end = start + hourClassEntry.getCurrentTime();*/
        for(HourClassEntry hourClassEntry:hourClassEntries){
            long startTime =  hourClassEntry.getStartTime()-STUDENT_TIME;
            long endTime = hourClassEntry.getStartTime() + hourClassEntry.getCurrentTime();
            if(entry ==null){
                if(endTime>current){
                    entry = hourClassEntry;
                }
            }else{
                if(endTime>current && startTime <current){
                    entry = hourClassEntry;
                }
            }
        }

        if(entry != null && entry.getStartTime()-STUDENT_TIME<current){
            map.put("start",1);//正在上课
            map.put("time",0);
        }else{
            if(entry==null){
                map.put("start",0);//未上课
                map.put("time",0);
            }else{
                map.put("start",0);//未上课
                map.put("time",(entry.getStartTime()-current)/1000);
            }

        }
        map.put("dto",new HourClassDTO(entry));
        map.put("courseType",0);
        if(entry!=null){
            ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(entry.getParentId());
            if(excellentCoursesEntry!=null && excellentCoursesEntry.getCourseType()==2){
                map.put("courseType",2);
            }
        }
        return map;
    }

    public ExcellentCoursesDTO getSimpleDesc(ObjectId id,ObjectId userId){
        //课程相关
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        ExcellentCoursesDTO dto = new ExcellentCoursesDTO(excellentCoursesEntry);
        //用户行为
        UserBehaviorEntry userBehaviorEntry = userBehaviorDao.getEntry(userId);
        //此课程已购买项目
        List<ObjectId> cids= classOrderDao.getEntryIdList(userId,id);
        //是否购买
        if(cids.size()==0){
            dto.setIsBuy(0);  //未购买
        }else{
            dto.setIsBuy(1);  //已购买
        }
        //是否收藏   （行为统计）
        if(userBehaviorEntry!=null){
            if(userBehaviorEntry.getCollectList().contains(id)){
                dto.setIsCollect(1);
            }else{
                dto.setIsCollect(0);
            }
            List<ObjectId> objectIdList2 =userBehaviorEntry.getBrowseList();
            if(!objectIdList2.contains(id)){
                objectIdList2.add(id);
                userBehaviorEntry.setBrowseList(objectIdList2);
                userBehaviorDao.addEntry(userBehaviorEntry);
            }
        }else{
            List<ObjectId> objectIdList = new ArrayList<ObjectId>();
            List<ObjectId> objectIdList2 = new ArrayList<ObjectId>();
            objectIdList2.add(id);
            UserBehaviorEntry userBehaviorEntry1 = new UserBehaviorEntry(userId,0,objectIdList,objectIdList2,objectIdList);
            userBehaviorDao.addEntry(userBehaviorEntry1);
        }
        return dto;
    }

    public ExcellentCoursesDTO getChildSimpleDesc(ObjectId id,ObjectId userId,ObjectId sonId){
        //课程相关
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        ExcellentCoursesDTO dto = new ExcellentCoursesDTO(excellentCoursesEntry);
        //用户行为
        UserBehaviorEntry userBehaviorEntry = userBehaviorDao.getEntry(userId);
        //此课程已购买项目
        List<ObjectId> cids= classOrderDao.getEntryIdList(sonId,id);
        //是否购买
        if(cids.size()==0){
            dto.setIsBuy(0);  //未购买
        }else{
            dto.setIsBuy(1);  //已购买
        }
        //是否收藏   （行为统计）
        if(userBehaviorEntry!=null){
            if(userBehaviorEntry.getCollectList().contains(id)){
                dto.setIsCollect(1);
            }else{
                dto.setIsCollect(0);
            }
            List<ObjectId> objectIdList2 =userBehaviorEntry.getBrowseList();
            if(!objectIdList2.contains(id)){
                objectIdList2.add(id);
                userBehaviorEntry.setBrowseList(objectIdList2);
                userBehaviorDao.addEntry(userBehaviorEntry);
            }
        }else{
            List<ObjectId> objectIdList = new ArrayList<ObjectId>();
            List<ObjectId> objectIdList2 = new ArrayList<ObjectId>();
            objectIdList2.add(id);
            UserBehaviorEntry userBehaviorEntry1 = new UserBehaviorEntry(userId,0,objectIdList,objectIdList2,objectIdList);
            userBehaviorDao.addEntry(userBehaviorEntry1);
        }
        return dto;
    }

    public ExcellentCoursesDTO getOneSimpleDesc(ObjectId id,ObjectId userId){
        //课程相关
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        ExcellentCoursesDTO dto = new ExcellentCoursesDTO(excellentCoursesEntry);
        //用户行为
        UserBehaviorEntry userBehaviorEntry = userBehaviorDao.getEntry(userId);
        long current = System.currentTimeMillis();
        if(excellentCoursesEntry.getEndTime()<=current){//上课时间已过
            dto.setType(1);
        }
        //是否收藏   （行为统计）
        if(userBehaviorEntry!=null){
            if(userBehaviorEntry.getCollectList().contains(id)){
                dto.setIsCollect(1);
            }else{
                dto.setIsCollect(0);
            }
            List<ObjectId> objectIdList2 =userBehaviorEntry.getBrowseList();
            if(!objectIdList2.contains(id)){
                objectIdList2.add(id);
                userBehaviorEntry.setBrowseList(objectIdList2);
                userBehaviorDao.addEntry(userBehaviorEntry);
            }
        }else{
            List<ObjectId> objectIdList = new ArrayList<ObjectId>();
            List<ObjectId> objectIdList2 = new ArrayList<ObjectId>();
            objectIdList2.add(id);
            UserBehaviorEntry userBehaviorEntry1 = new UserBehaviorEntry(userId,0,objectIdList,objectIdList2,objectIdList);
            userBehaviorDao.addEntry(userBehaviorEntry1);
        }

        return dto;
    }

    public Map<String,Object> getCoursesDesc(ObjectId id,ObjectId userId){
        Map<String,Object> map = new HashMap<String, Object>();
        //课程相关
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        //此课程已购买项目
        List<ObjectId> cids= classOrderDao.getEntryIdList(userId,id);
        if(cids.size()==0){
            map.put("isBuy",0);
        }else{
            map.put("isBuy",1);
        }
        long current = System.currentTimeMillis();
        boolean flage = false;
        map.put("isEnd",0);
        if(excellentCoursesEntry.getEndTime()<=current){//上课时间已过
            map.put("isEnd",1);
            flage = true;
        }
        //课时相关
        List<HourClassEntry> hourClassEntries = hourClassDao.getEntryList(id);
        List<HourClassDTO> hourClassDTOs = new ArrayList<HourClassDTO>();

        //
        long allEndTime = 0l;
        boolean falge2 = false;
        HourClassEntry startClass = null;
        HourClassEntry endClass = null;
        HourClassDTO nowHourClassDTO  = null;
        for(HourClassEntry hourClassEntry:hourClassEntries){
            HourClassDTO hourClassDTO = new HourClassDTO(hourClassEntry);
            long endTime = hourClassEntry.getStartTime() + hourClassEntry.getCurrentTime();
            hourClassDTO.setStatus(1);//未购买
            if(endTime> allEndTime){//获得最终结束时间
                allEndTime = endTime;
            }
            if(endTime<=current){//上课时间已结束
                hourClassDTO.setStatus(0);//已结束
                //已结束的取一个最近的
                if(cids.contains(hourClassEntry.getID())){//已购买
                    if(startClass==null){
                        startClass = hourClassEntry;
                    }else{
                        long etm = startClass.getStartTime() + startClass.getCurrentTime();
                        if(endTime>etm){
                            startClass = hourClassEntry;
                        }
                    }
                }
            }else{
                if(cids.contains(hourClassEntry.getID())){
                    hourClassDTO.setStatus(2);//已购买
                    if(hourClassEntry.getStartTime()<=current+STUDENT_TIME && endTime >current ){
                        nowHourClassDTO = hourClassDTO;
                    }
                    //未结束的取一个最近的
                    if(endClass==null){
                        endClass = hourClassEntry;
                    }else{
                        long stm = endClass.getStartTime();
                        if(hourClassEntry.getStartTime()<stm){
                            endClass = hourClassEntry;
                        }
                    }

                }else{
                    falge2=true;//还可以购买
                }
            }
            if(flage){//课程结束
                hourClassDTO.setStatus(0);//已结束
            }
            if(hourClassDTO.getOwnName()==null){
                hourClassDTO.setOwnId(userId.toString());
                hourClassDTO.setOwnName(excellentCoursesEntry.getUserName());
                hourClassDTO.setSubjectName(excellentCoursesEntry.getSubjectName());
            }
            hourClassDTOs.add(hourClassDTO);
        }
        if(falge2){
            map.put("isXian",1);
        }else{
            map.put("isXian",0);
        }
        List<HourClassDTO> dtos = new ArrayList<HourClassDTO>();
        if(nowHourClassDTO!=null){
            //HourClassEntry hourClassEntry = nowHourClassDTO.buildAddEntry();
            HourClassDTO h = new HourClassDTO();
            h.setParentId(nowHourClassDTO.getParentId());
            h.setStatus(nowHourClassDTO.getStatus());
            h.setCreateTime(nowHourClassDTO.getCreateTime());
            h.setWeek(nowHourClassDTO.getWeek());
            h.setClassOldPrice(nowHourClassDTO.getClassOldPrice());
            h.setClassNewPrice(nowHourClassDTO.getClassNewPrice());
            h.setContent(nowHourClassDTO.getContent());
            h.setDateTime(nowHourClassDTO.getDateTime());
            h.setStartTime(nowHourClassDTO.getStartTime());
            h.setId(nowHourClassDTO.getId());
            h.setCurrentTime(nowHourClassDTO.getCurrentTime());
            h.setOrder(nowHourClassDTO.getOrder());
            h.setUserId(nowHourClassDTO.getUserId());
            h.setType(nowHourClassDTO.getType());
            dtos.add(h);
        }

        //如果没有正在上的课，尝试开放已结束的课程（满足一下喜欢拖堂的老师，让他们上个够，让上课的小伙伴可以出来溜达溜达再回去）
        if(dtos.size()==0){
            //HourClassEntry startClass = null;
            //HourClassEntry endClass = null;
            if(startClass !=null){//前有结束的课
                long etm = startClass.getStartTime() + startClass.getCurrentTime();
                if(endClass==null){//后无
                    //1小时显示
                    if(etm< current + 60*60*1000 ){
                        HourClassDTO hourClassDTO2 = new HourClassDTO(startClass);
                        hourClassDTO2.setStatus(2);
                        dtos.add(hourClassDTO2);
                    }

                }else{//后有
                    if(endClass.getDateTime()==startClass.getDateTime()){//同一天
                        if(current<endClass.getStartTime()-STUDENT_TIME){//延长显示
                            HourClassDTO hourClassDTO2 = new HourClassDTO(startClass);
                            hourClassDTO2.setStatus(2);
                            dtos.add(hourClassDTO2);
                        }
                    }else{//不同 一小时
                        if(etm< current + 60*60*1000 ){
                            HourClassDTO hourClassDTO2 = new HourClassDTO(startClass);
                            hourClassDTO2.setStatus(2);
                            dtos.add(hourClassDTO2);
                        }
                    }

                }
            }
        }
        if(dtos.size()>0){
            map.put("isEnd",0);
        }
        map.put("now",dtos);
        map.put("list",hourClassDTOs);
        return map;
    }

    public Map<String,Object> getNewCoursesDesc(ObjectId id,ObjectId userId){
        Map<String,Object> map = new HashMap<String, Object>();
        //课程相关
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        //此课程已购买项目
        List<ObjectId> cids= classOrderDao.getEntryIdList(userId,id);
        if(cids.size()==0){
            map.put("isBuy",0);
        }else{
            map.put("isBuy",1);
        }
        long current = System.currentTimeMillis();
        boolean flage = false;
        map.put("isEnd",0);
        if(excellentCoursesEntry.getEndTime()<=current){//上课时间已过
            map.put("isEnd",1);
            flage = true;
        }
        //课时相关
        List<HourClassEntry> hourClassEntries = hourClassDao.getEntryList(id);
        List<HourClassDTO> hourClassDTOs = new ArrayList<HourClassDTO>();

        //
        long allEndTime = 0l;
        boolean falge2 = false;
        HourClassEntry startClass = null;
        HourClassEntry endClass = null;
        HourClassDTO nowHourClassDTO  = null;
        for(HourClassEntry hourClassEntry:hourClassEntries){
            HourClassDTO hourClassDTO = new HourClassDTO(hourClassEntry);
            if(hourClassDTO.getOwnName()==null){//旧版
                hourClassDTO.setOwnId(userId.toString());
                hourClassDTO.setOwnName(excellentCoursesEntry.getUserName());
                hourClassDTO.setSubjectName(excellentCoursesEntry.getSubjectName());
            }
            long endTime = hourClassEntry.getStartTime() + hourClassEntry.getCurrentTime();
            hourClassDTO.setStatus(1);//未购买
            if(endTime> allEndTime){//获得最终结束时间
                allEndTime = endTime;
            }
            if(endTime<=current){//上课时间已结束
                if(cids.contains(hourClassEntry.getID())){
                    hourClassDTO.setStatus(3);//已结束已购买
                }else{
                    hourClassDTO.setStatus(0);//已结束未购买
                }
                //已结束的取一个最近的
                if(cids.contains(hourClassEntry.getID())){//已购买
                    if(startClass==null){
                        startClass = hourClassEntry;
                    }else{
                        long etm = startClass.getStartTime() + startClass.getCurrentTime();
                        if(endTime>etm){
                            startClass = hourClassEntry;
                        }
                    }
                }
            }else{//未结束
                if(cids.contains(hourClassEntry.getID())){
                    hourClassDTO.setStatus(2);//已购买
                    if(hourClassEntry.getStartTime()<=current+STUDENT_TIME && endTime >current ){
                        nowHourClassDTO = hourClassDTO;
                    }
                    //未结束的取一个最近的
                    if(endClass==null){
                        endClass = hourClassEntry;
                    }else{
                        long stm = endClass.getStartTime();
                        if(hourClassEntry.getStartTime()<stm){
                            endClass = hourClassEntry;
                        }
                    }
                }else{
                    falge2=true;//还可以购买
                }
            }

            if(flage){//课程结束
                if(cids.contains(hourClassEntry.getID())){
                    hourClassDTO.setStatus(3);//已结束已购买
                }else{
                    hourClassDTO.setStatus(0);//已结束未购买
                }

            }
            hourClassDTOs.add(hourClassDTO);
        }
        if(falge2){
            map.put("isXian",1);
        }else{
            map.put("isXian",0);
        }
        List<HourClassDTO> dtos = new ArrayList<HourClassDTO>();
        if(nowHourClassDTO!=null){
            //HourClassEntry hourClassEntry = nowHourClassDTO.buildAddEntry();
            HourClassDTO h = new HourClassDTO();
            h.setParentId(nowHourClassDTO.getParentId());
            h.setStatus(nowHourClassDTO.getStatus());
            h.setCreateTime(nowHourClassDTO.getCreateTime());
            h.setWeek(nowHourClassDTO.getWeek());
            h.setClassOldPrice(nowHourClassDTO.getClassOldPrice());
            h.setClassNewPrice(nowHourClassDTO.getClassNewPrice());
            h.setContent(nowHourClassDTO.getContent());
            h.setDateTime(nowHourClassDTO.getDateTime());
            h.setStartTime(nowHourClassDTO.getStartTime());
            h.setId(nowHourClassDTO.getId());
            h.setCurrentTime(nowHourClassDTO.getCurrentTime());
            h.setOrder(nowHourClassDTO.getOrder());
            h.setUserId(nowHourClassDTO.getUserId());
            h.setType(nowHourClassDTO.getType());
            dtos.add(h);
        }

        //如果没有正在上的课，尝试开放已结束的课程（满足一下喜欢拖堂的老师，让他们上个够，让上课的小伙伴可以出来溜达溜达再回去）
        if(dtos.size()==0){
            //HourClassEntry startClass = null;
            //HourClassEntry endClass = null;
            if(startClass !=null){//前有结束的课
                long etm = startClass.getStartTime() + startClass.getCurrentTime();
                if(endClass==null){//后无
                    //1小时显示
                    if(etm> current - 60*60*1000 ){
                        HourClassDTO hourClassDTO2 = new HourClassDTO(startClass);
                        hourClassDTO2.setStatus(2);
                        dtos.add(hourClassDTO2);
                    }

                }else{//后有
                    if(endClass.getDateTime()==startClass.getDateTime()){//同一天
                        if(current<endClass.getStartTime()-STUDENT_TIME){//延长显示
                            HourClassDTO hourClassDTO2 = new HourClassDTO(startClass);
                            hourClassDTO2.setStatus(2);
                            dtos.add(hourClassDTO2);
                        }
                    }else{//不同 一小时
                        if(etm> current - 60*60*1000 ){
                            HourClassDTO hourClassDTO2 = new HourClassDTO(startClass);
                            hourClassDTO2.setStatus(2);
                            dtos.add(hourClassDTO2);
                        }
                    }

                }
            }
        }
        if(dtos.size()>0){
            map.put("isEnd",0);
        }
        map.put("now",dtos);
        map.put("list",hourClassDTOs);
        return map;
    }

    public Map<String,Object> getNewParentCoursesDesc(ObjectId id,ObjectId userId,ObjectId parentId){
        Map<String,Object> map = new HashMap<String, Object>();
        //课程相关
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        //此课程已购买项目
        List<ObjectId> cids= classOrderDao.getEntryIdList(userId,id);
        if(cids.size()==0){
            map.put("isBuy",0);
        }else{
            map.put("isBuy",1);
        }
        long current = System.currentTimeMillis();
        boolean flage = false;
        map.put("isEnd",0);
        if(excellentCoursesEntry.getEndTime()<=current){//上课时间已过
            map.put("isEnd",1);
            flage = true;
        }
        //课时相关
        List<HourClassEntry> hourClassEntries = hourClassDao.getEntryList(id);
        List<HourClassDTO> hourClassDTOs = new ArrayList<HourClassDTO>();

        //
        long allEndTime = 0l;
        boolean falge2 = false;
        HourClassEntry startClass = null;
        HourClassEntry endClass = null;
        HourClassDTO nowHourClassDTO  = null;
        for(HourClassEntry hourClassEntry:hourClassEntries){
            HourClassDTO hourClassDTO = new HourClassDTO(hourClassEntry);
            long endTime = hourClassEntry.getStartTime() + hourClassEntry.getCurrentTime();
            hourClassDTO.setStatus(1);//未购买
            if(endTime> allEndTime){//获得最终结束时间
                allEndTime = endTime;
            }
            if(endTime<=current){//上课时间已结束
                if(cids.contains(hourClassEntry.getID())){
                    if(userId.equals(parentId)){
                        hourClassDTO.setStatus(3);//已结束已购买
                    }else{
                        hourClassDTO.setStatus(0);//已结束未购买
                    }

                }else{
                    hourClassDTO.setStatus(0);//已结束未购买
                }
                //已结束的取一个最近的
                if(cids.contains(hourClassEntry.getID())){//已购买
                    if(startClass==null){
                        startClass = hourClassEntry;
                    }else{
                        long etm = startClass.getStartTime() + startClass.getCurrentTime();
                        if(endTime>etm){
                            startClass = hourClassEntry;
                        }
                    }
                }
            }else{//未结束
                if(cids.contains(hourClassEntry.getID())){
                    hourClassDTO.setStatus(2);//已购买
                    if(hourClassEntry.getStartTime()<=current+STUDENT_TIME && endTime >current ){
                        nowHourClassDTO = hourClassDTO;
                    }
                    //未结束的取一个最近的
                    if(endClass==null){
                        endClass = hourClassEntry;
                    }else{
                        long stm = endClass.getStartTime();
                        if(hourClassEntry.getStartTime()<stm){
                            endClass = hourClassEntry;
                        }
                    }
                }else{
                    falge2=true;//还可以购买
                }
            }

            if(flage){//课程结束
                if(cids.contains(hourClassEntry.getID())){
                    if(userId.equals(parentId)){
                        hourClassDTO.setStatus(3);//已结束已购买
                    }else{
                        hourClassDTO.setStatus(0);//已结束未购买
                    }
                }else{
                    hourClassDTO.setStatus(0);//已结束未购买
                }

            }
            if(hourClassDTO.getOwnName()==null){//旧版
                hourClassDTO.setOwnId(userId.toString());
                hourClassDTO.setOwnName(excellentCoursesEntry.getUserName());
                hourClassDTO.setSubjectName(excellentCoursesEntry.getSubjectName());
            }
            hourClassDTOs.add(hourClassDTO);
        }
        if(falge2){
            map.put("isXian",1);
        }else{
            map.put("isXian",0);
        }
        List<HourClassDTO> dtos = new ArrayList<HourClassDTO>();
        String nowId = "";
        if(nowHourClassDTO!=null){
            //HourClassEntry hourClassEntry = nowHourClassDTO.buildAddEntry();
            HourClassDTO h = new HourClassDTO();
            h.setParentId(nowHourClassDTO.getParentId());
            h.setStatus(nowHourClassDTO.getStatus());
            h.setCreateTime(nowHourClassDTO.getCreateTime());
            h.setWeek(nowHourClassDTO.getWeek());
            h.setClassOldPrice(nowHourClassDTO.getClassOldPrice());
            h.setClassNewPrice(nowHourClassDTO.getClassNewPrice());
            h.setContent(nowHourClassDTO.getContent());
            h.setDateTime(nowHourClassDTO.getDateTime());
            h.setStartTime(nowHourClassDTO.getStartTime());
            h.setId(nowHourClassDTO.getId());
            h.setCurrentTime(nowHourClassDTO.getCurrentTime());
            h.setOrder(nowHourClassDTO.getOrder());
            h.setUserId(nowHourClassDTO.getUserId());
            h.setType(nowHourClassDTO.getType());
            nowId = nowHourClassDTO.getId();
            dtos.add(h);
        }

        //如果没有正在上的课，尝试开放已结束的课程（满足一下喜欢拖堂的老师，让他们上个够，让上课的小伙伴可以出来溜达溜达再回去）
        boolean fale = true;
        if(dtos.size()==0){
            fale = false;
            //HourClassEntry startClass = null;
            //HourClassEntry endClass = null;
            if(startClass !=null){//前有结束的课
                long etm = startClass.getStartTime() + startClass.getCurrentTime();
                if(endClass==null){//后无
                    //1小时显示
                    if(etm> current - 60*60*1000 ){
                        HourClassDTO hourClassDTO2 = new HourClassDTO(startClass);
                        hourClassDTO2.setStatus(2);
                        nowId = hourClassDTO2.getId();
                        dtos.add(hourClassDTO2);
                    }

                }else{//后有
                    if(endClass.getDateTime()==startClass.getDateTime()){//同一天
                        if(current<endClass.getStartTime()-STUDENT_TIME){//延长显示
                            HourClassDTO hourClassDTO2 = new HourClassDTO(startClass);
                            hourClassDTO2.setStatus(2);
                            nowId = hourClassDTO2.getId();
                            dtos.add(hourClassDTO2);
                        }
                    }else{//不同 一小时
                        if(etm> current - 60*60*1000 ){
                            HourClassDTO hourClassDTO2 = new HourClassDTO(startClass);
                            hourClassDTO2.setStatus(2);
                            nowId = hourClassDTO2.getId();
                            dtos.add(hourClassDTO2);
                        }
                    }

                }
            }
        }
        if(dtos.size()>0){
            map.put("isEnd",0);
        }
        map.put("now",dtos);
        if(!parentId.equals(userId)){
            map.put("now",new ArrayList<HourClassDTO>());
        }else{
            if(dtos.size()>0 && fale){
                for(HourClassDTO hourClassDTO4:hourClassDTOs){
                    if(hourClassDTO4.getId().equals(nowId)){
                        hourClassDTO4.setStatus(5);
                    }
                }
            }
        }
        map.put("list",hourClassDTOs);
        return map;
    }


    public Map<String,Object> getOneCoursesDesc(ObjectId id){
        Map<String,Object> map = new HashMap<String, Object>();
        //课程相关
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        map.put("isBuy",0);
        long current = System.currentTimeMillis();
        boolean flage = false;
        map.put("isEnd",0);
        if(excellentCoursesEntry.getEndTime()<=current){//上课时间已过
            map.put("isEnd",1);
            flage = true;
        }
        //课时相关
        List<HourClassEntry> hourClassEntries = hourClassDao.getEntryList(id);
        List<HourClassDTO> hourClassDTOs = new ArrayList<HourClassDTO>();
        for(HourClassEntry hourClassEntry:hourClassEntries){
            HourClassDTO hourClassDTO = new HourClassDTO(hourClassEntry);
            long endTime = hourClassEntry.getStartTime() + hourClassEntry.getCurrentTime();
            hourClassDTO.setStatus(1);//未开始
            if(endTime<=current){//上课时间已结束
                hourClassDTO.setStatus(0);//已结束
            }else{

            }
            if(flage){//课程结束
                hourClassDTO.setStatus(0);//已结束
            }
            if(hourClassDTO.getOwnName()==null){//旧版
                hourClassDTO.setOwnId(excellentCoursesEntry.getUserId().toString());
                hourClassDTO.setOwnName(excellentCoursesEntry.getUserName());
                hourClassDTO.setSubjectName(excellentCoursesEntry.getSubjectName());
            }
            hourClassDTOs.add(hourClassDTO);
        }
        map.put("isXian",0);
        List<HourClassDTO> dtos = new ArrayList<HourClassDTO>();
        map.put("now",dtos);
        map.put("list",hourClassDTOs);
        return map;
    }

    /**
     *   important
     * @param id
     * @param userId
     * @param classIds
     * @return
     * @throws Exception
     */
    public String buyClassList(ObjectId id,ObjectId userId,String classIds,String ip) throws  Exception{
        //绑定关系
        NewVersionBindRelationEntry newVersionBindRelationEntry = newVersionBindRelationDao.getBindEntry(userId);
        if(newVersionBindRelationEntry==null){
            throw new Exception("未绑定的学生账户不能购买课程");
        }
        ObjectId parentId = newVersionBindRelationEntry.getMainUserId();

        //用户父母美豆账户
        UserBehaviorEntry userBehaviorEntry = userBehaviorDao.getEntry(parentId);
        List<ObjectId> objectIdList6 = new ArrayList<ObjectId>();
        if(userBehaviorEntry==null){
            List<ObjectId>  ot = new ArrayList<ObjectId>();
            UserBehaviorEntry userBehaviorEntry1 = new UserBehaviorEntry(parentId,0,ot,ot,ot);
            userBehaviorDao.addEntry(userBehaviorEntry1);
            userBehaviorEntry= userBehaviorEntry1;
        }else{
            objectIdList6 = userBehaviorEntry.getSonOpenList();
        }
        if(userBehaviorEntry!=null && objectIdList6.contains(userId)){
            throw new Exception("未授权的学生账户不能自主购买课程");
        }

        //用户父母充值账户
        AccountFrashEntry accountFrashEntry = accountFrashDao.getEntry(parentId);
        if(accountFrashEntry==null){
            AccountFrashEntry accountFrashEntry1 = new AccountFrashEntry(parentId,0,Constant.ZERO,Constant.ZERO);
            accountFrashDao.addEntry(accountFrashEntry1);
            accountFrashEntry = accountFrashEntry1;
        }

        //创建订单
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        if(classIds==null|| classIds.equals("")){
            throw  new Exception("请至少购买一节课程");
        }
        String[] strings = classIds.split(",");
        for(String str:strings){
            objectIdList.add(new ObjectId(str));
        }
        //此次所下订单
        List<HourClassEntry> classEntries = hourClassDao.getEntryList(objectIdList);
        //用户订单查询
        List<ObjectId> classOrderEntries = classOrderDao.getOwnEntry(objectIdList, userId);
        List<ClassOrderEntry> classOrderEntries1 = new ArrayList<ClassOrderEntry>();
        long current = System.currentTimeMillis();
        double price = 0.00;//此次消费金额
        String description = "购买课程："+excellentCoursesEntry.getTitle()+"的";
        List<ObjectId>  cIds = new ArrayList<ObjectId>();
        if(excellentCoursesEntry!=null && classEntries.size()>0){
            for(HourClassEntry hourClassEntry: classEntries){
                if(!classOrderEntries.contains(hourClassEntry.getID())){
                    ClassOrderEntry classOrderEntry = new ClassOrderEntry();
                    //购买  1  未购买
                    classOrderEntry.setIsBuy(1);
                    //下单
                    classOrderEntry.setType(1);
                    classOrderEntry.setCreateTime(current);
                    classOrderEntry.setContactId(id);
                    classOrderEntry.setIsRemove(0);
                    classOrderEntry.setParentId(hourClassEntry.getID());
                    classOrderEntry.setFunction(0);
                    classOrderEntry.setPrice(hourClassEntry.getClassNewPrice());
                    classOrderEntry.setUserId(userId);
                    classOrderEntry.setOrderId("");
                    classOrderEntries1.add(classOrderEntry);
                    price = sum(price,hourClassEntry.getClassNewPrice());
                    description  = description + "第"+hourClassEntry.getOrder()+"课时 ";
                    cIds.add(hourClassEntry.getID());
                }
            }
            description = description + " 共花费"+price+"¥";

            //正式购买，扣除金额
            ObjectId contactId = new ObjectId();
            addLog(userId,contactId,"学生开始创建订单！余额:"+accountFrashEntry.getAccount()+" 订单价值"+price);
            logger.info(userId.toString()+"-"+contactId.toString()+"学生开始创建订单！余额:"+accountFrashEntry.getAccount()+" 订单价值:"+price);
            //try{
            if( userBehaviorEntry.getAccount()>= price){
                //判断余额
                if(accountFrashEntry.getAccount()< price){
                    throw new Exception("余额不足！");
                }
                double newPr = sub(userBehaviorEntry.getAccount(),price);
                try{
                    //修改美豆账户余额
                    userBehaviorDao.updateEntry(userBehaviorEntry.getID(), newPr);
                    addLog(userId,contactId,"修改美豆账户成功！余额:"+newPr);
                    logger.info(userId.toString()+"-"+contactId.toString()+"修改美豆账户成功！余额:"+newPr);

                    //添加美豆账户消费记录
                    RechargeResultEntry rechargeResultEntry = new RechargeResultEntry(parentId,userId,description,Constant.ZERO,Constant.ZERO,price,userId,excellentCoursesEntry.getID(),cIds);
                    rechargeResultDao.saveEntry(rechargeResultEntry);
                    addLog(userId,contactId,"添加美豆账户消费记录成功！本次消费："+price);
                    logger.info(userId.toString()+"-"+contactId.toString()+"添加美豆账户消费记录成功！本次消费："+price);

                    //修改充值账户余额
                    double newPrice = sub(accountFrashEntry.getAccount(),price);
                    accountFrashDao.updateEntry(accountFrashEntry.getID(),newPrice);
                    addLog(userId,contactId,"修改充值账户消费记录成功！余额:"+newPrice);
                    logger.info(userId.toString()+"-"+contactId.toString()+"修改美豆账户消费记录成功！余额:"+newPrice);

                    //添加充值账户记录
                    AccountOrderEntry accountOrderEntry = new AccountOrderEntry(contactId,parentId,"","","",price,"",Constant.ZERO,ip,0l,"");
                    accountOrderDao.addEntry(accountOrderEntry);
                    addLog(userId,contactId,"添加充值账户记录成功！余额:"+newPrice);
                    logger.info(userId.toString()+"-"+contactId.toString()+"添加充值账户记录成功！余额:"+newPrice);


                    //添加课节订单
                    this.addClassEntryBatch(classOrderEntries1);
                }catch (Exception e){
                    throw new Exception("购买异常，请联系客服！");
                }

            }else{
                throw new Exception("余额不足！");
            }


            //修改课程人数
            Set<ObjectId> set = classOrderDao.getUserIdEntry(excellentCoursesEntry.getID());
            excellentCoursesEntry.setStudentNumber(set.size());
            excellentCoursesDao.addEntry(excellentCoursesEntry);
            return "购买成功";
        }else{
            throw  new Exception("订单信息不存在！");
        }
    }



    public String buyChildClassList(ObjectId id,ObjectId userId,String classIds,ObjectId sonId,String ip) throws  Exception{
        if(userId.equals(sonId)){
            return this.buyMyClassList(id,userId,classIds,sonId,ip);
        }
        //创建订单
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        if(classIds==null|| classIds.equals("")){
            throw  new Exception("请至少购买一节课程");
        }
        if(excellentCoursesEntry.getCourseType()==2 && userId.equals(sonId)){
            throw  new Exception("家长不可购买赶考网课程");
        }
        //美豆账户
        UserBehaviorEntry userBehaviorEntry = userBehaviorDao.getEntry(userId);
        if(userBehaviorEntry==null){
            List<ObjectId>  ot = new ArrayList<ObjectId>();
            UserBehaviorEntry userBehaviorEntry1 = new UserBehaviorEntry(userId,0,ot,ot,ot);
            userBehaviorDao.addEntry(userBehaviorEntry1);
            userBehaviorEntry= userBehaviorEntry1;
        }

        //充值账户
        AccountFrashEntry accountFrashEntry = accountFrashDao.getEntry(userId);
        if(accountFrashEntry==null){
            AccountFrashEntry accountFrashEntry1 = new AccountFrashEntry(userId,0,Constant.ZERO,Constant.ZERO);
            accountFrashDao.addEntry(accountFrashEntry1);
            accountFrashEntry = accountFrashEntry1;
        }

        String[] strings = classIds.split(",");
        for(String str:strings){
            objectIdList.add(new ObjectId(str));
        }
        //此次所下订单
        List<HourClassEntry> classEntries = hourClassDao.getEntryList(objectIdList);
        //孩子订单查询
        List<ObjectId> classOrderEntries = classOrderDao.getOwnEntry(objectIdList, sonId);
        List<ClassOrderEntry> classOrderEntries1 = new ArrayList<ClassOrderEntry>();
        long current = System.currentTimeMillis();
        double price = 0.00;
        String description = "购买课程："+excellentCoursesEntry.getTitle()+"的";
        List<ObjectId>  cIds = new ArrayList<ObjectId>();
        if(excellentCoursesEntry!=null && classEntries.size()>0){
            for(HourClassEntry hourClassEntry: classEntries){
                if(!classOrderEntries.contains(hourClassEntry.getID())){
                    ClassOrderEntry classOrderEntry = new ClassOrderEntry();
                    //购买  1  未购买
                    classOrderEntry.setIsBuy(1);
                    //下单
                    classOrderEntry.setType(1);
                    classOrderEntry.setCreateTime(current);
                    classOrderEntry.setContactId(id);
                    classOrderEntry.setOrderId("");
                    classOrderEntry.setIsRemove(0);
                    classOrderEntry.setParentId(hourClassEntry.getID());
                    classOrderEntry.setFunction(0);
                    classOrderEntry.setPrice(hourClassEntry.getClassNewPrice());
                    classOrderEntry.setUserId(sonId);//孩子的订单
                    classOrderEntries1.add(classOrderEntry);
                    price = price + hourClassEntry.getClassNewPrice();
                    description  = description + "第"+hourClassEntry.getOrder()+"课时 ";
                    cIds.add(hourClassEntry.getID());
                }
            }
            description = description + " 共花费"+price+"¥";

            //正式购买，扣除金额
            ObjectId contactId = new ObjectId();
            addLog(userId,contactId,"用户开始创建订单！余额:"+accountFrashEntry.getAccount()+" 订单价值:"+price);
            logger.info(userId.toString()+"-"+contactId.toString()+"用户开始创建订单！余额:"+accountFrashEntry.getAccount()+" 订单价值"+price);
            //自定义回滚
            //try{
            if(userBehaviorEntry!=null && userBehaviorEntry.getAccount()>= price){
                //判断余额
                if(accountFrashEntry.getAccount()< price){
                    throw new Exception("余额不足！");
                }
                double newPr = sub(userBehaviorEntry.getAccount(), price);
                try{
                    //修改美豆账户余额
                    userBehaviorDao.updateEntry(userBehaviorEntry.getID(), newPr);
                    addLog(userId,contactId,"修改美豆账户成功！余额:"+newPr);
                    logger.info(userId.toString()+"-"+contactId.toString()+"修改美豆账户成功！余额:"+newPr);

                    //添加美豆账户消费记录
                    RechargeResultEntry rechargeResultEntry = new RechargeResultEntry(userId,userId,description,Constant.ZERO,Constant.ZERO,price,userId,excellentCoursesEntry.getID(),cIds);
                    rechargeResultDao.saveEntry(rechargeResultEntry);
                    addLog(userId,contactId,"添加美豆账户消费记录成功！本次消费："+price);
                    logger.info(userId.toString()+"-"+contactId.toString()+"添加美豆账户消费记录成功！本次消费："+price);

                    //修改充值账户余额
                    double newPrice = sub(accountFrashEntry.getAccount(),price);
                    accountFrashDao.updateEntry(accountFrashEntry.getID(),newPrice);
                    addLog(userId,contactId,"修改充值账户成功！余额:"+newPrice);
                    logger.info(userId.toString()+"-"+contactId.toString()+"修改充值账户成功！余额:"+newPrice);

                    //添加充值账户记录
                    AccountOrderEntry accountOrderEntry = new AccountOrderEntry(contactId,userId,"","","",price,"",Constant.ZERO,ip,0l,"");
                    accountOrderDao.addEntry(accountOrderEntry);
                    addLog(userId,contactId,"添加充值账户记录成功！余额:"+newPrice);
                    logger.info(userId.toString()+"-"+contactId.toString()+"添加充值账户记录成功！余额:"+newPrice);

                    //添加课节订单
                    this.addClassEntryBatch(classOrderEntries1);
                }catch (Exception e){
                    throw new Exception("购买异常，请联系客服！");
                }

                /*//修改余额
                userBehaviorDao.updateEntry(userBehaviorEntry.getID(), userBehaviorEntry.getAccount() - price);
                //添加记录
                RechargeResultEntry rechargeResultEntry = new RechargeResultEntry(userId,userId,description,Constant.ZERO,Constant.ZERO,price,sonId,excellentCoursesEntry.getID(),cIds);
                rechargeResultDao.saveEntry(rechargeResultEntry);
                //添加课节订单
                this.addClassEntryBatch(classOrderEntries1);*/
            }else{
                throw  new Exception("余额不足！");
            }
            //修改课程人数
            Set<ObjectId> set = classOrderDao.getUserIdEntry(excellentCoursesEntry.getID());
            excellentCoursesEntry.setStudentNumber(set.size());
            excellentCoursesDao.addEntry(excellentCoursesEntry);
            return "购买成功";
        }else{
            throw  new Exception("订单信息不存在！");
        }
    }

    /**
     * double 相加
     * @param d1
     * @param d2
     * @return
     */
    public static double sum(double d1,double d2){
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.add(bd2).doubleValue();
    }


    /**
     * double 相减
     * @param d1
     * @param d2
     * @return
     */
    public static double sub(double d1,double d2){
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.subtract(bd2).doubleValue();
    }

    /**
     * 直接购课
     * @param id
     * @param userId
     * @param classIds
     * @param sonId
     * @param ip
     * @return
     * @throws Exception
     */
    public String buyOwnClassList(ObjectId id,ObjectId userId,String classIds,ObjectId sonId,String ip) throws  Exception{
        //创建订单
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        if(classIds==null|| classIds.equals("")){
            throw  new Exception("请至少购买一节课程");
        }

        //美豆账户
        UserBehaviorEntry userBehaviorEntry = userBehaviorDao.getEntry(userId);
        if(userBehaviorEntry==null){
            List<ObjectId>  ot = new ArrayList<ObjectId>();
            UserBehaviorEntry userBehaviorEntry1 = new UserBehaviorEntry(userId,0,ot,ot,ot);
            userBehaviorDao.addEntry(userBehaviorEntry1);
            userBehaviorEntry= userBehaviorEntry1;
        }

        //充值账户
        AccountFrashEntry accountFrashEntry = accountFrashDao.getEntry(userId);
        if(accountFrashEntry==null){
            AccountFrashEntry accountFrashEntry1 = new AccountFrashEntry(userId,0,Constant.ZERO,Constant.ZERO);
            accountFrashDao.addEntry(accountFrashEntry1);
            accountFrashEntry = accountFrashEntry1;
        }

        String[] strings = classIds.split(",");
        for(String str:strings){
            objectIdList.add(new ObjectId(new String(str)));
        }
        strings = null;
        //此次所下订单
        List<HourClassEntry> classEntries = hourClassDao.getEntryList(objectIdList);
        //孩子订单查询
        List<ObjectId> classOrderEntries = classOrderDao.getOwnEntry(objectIdList, sonId);
        List<ClassOrderEntry> classOrderEntries1 = new ArrayList<ClassOrderEntry>();
        long current = System.currentTimeMillis();
        double price = 0.00;
        double price2 = 0.00;
        StringBuffer sb = new StringBuffer();
        sb.append("购买课程：");
        sb.append(excellentCoursesEntry.getTitle());
        sb.append("的");
        List<ObjectId>  cIds = new ArrayList<ObjectId>();
        if(excellentCoursesEntry!=null && classEntries.size()>0){
            for(HourClassEntry hourClassEntry: classEntries){
                if(!classOrderEntries.contains(hourClassEntry.getID())){
                    ClassOrderEntry classOrderEntry = new ClassOrderEntry();
                    //购买  1  未购买
                    classOrderEntry.setIsBuy(1);
                    //下单
                    classOrderEntry.setType(1);
                    classOrderEntry.setCreateTime(current);
                    classOrderEntry.setContactId(id);
                    classOrderEntry.setOrderId("");
                    classOrderEntry.setIsRemove(0);
                    classOrderEntry.setParentId(hourClassEntry.getID());
                    classOrderEntry.setFunction(3);
                    classOrderEntry.setPrice(hourClassEntry.getClassNewPrice());
                    classOrderEntry.setUserId(sonId);//孩子的订单
                    classOrderEntries1.add(classOrderEntry);
                    //相加
                    price2 = sum(price2 , hourClassEntry.getClassNewPrice());
                    sb.append("第");
                    sb.append(hourClassEntry.getOrder());
                    sb.append("课时 ");
                    cIds.add(hourClassEntry.getID());
                }
            }
            if(cIds.size()==0){
                return "已经购买的用户无需再次购买！";
            }
            sb.append(" 共花费");
            sb.append(price2);
            sb.append("¥");

            //正式购买，扣除金额
            ObjectId contactId = new ObjectId();
            addLog(userId,contactId,"后台代购订单！余额:"+accountFrashEntry.getAccount()+" 订单价值:"+price2);
            logger.info(userId.toString()+"-"+contactId.toString()+"用户开始创建订单！余额:"+accountFrashEntry.getAccount()+" 订单价值"+price2);
            //自定义回滚
            //try{
            if(userBehaviorEntry!=null && userBehaviorEntry.getAccount()>= price){
                //判断余额
                if(accountFrashEntry.getAccount()< price){
                    throw new Exception("余额不足！");
                }
                double newPr = sub(userBehaviorEntry.getAccount(),price);
                try{
                    //修改美豆账户余额
                    userBehaviorDao.updateEntry(userBehaviorEntry.getID(), newPr);
                    addLog(userId,contactId,"修改美豆账户成功！余额:"+newPr);
                    logger.info(userId.toString()+"-"+contactId.toString()+"修改美豆账户成功！余额:"+newPr);

                    //添加美豆账户消费记录
                    RechargeResultEntry rechargeResultEntry = new RechargeResultEntry(userId,userId,sb.toString(),Constant.ZERO,Constant.ZERO,price,userId,excellentCoursesEntry.getID(),cIds);
                    rechargeResultDao.saveEntry(rechargeResultEntry);
                    addLog(userId,contactId,"添加美豆账户消费记录成功！本次消费："+price2);
                    logger.info(userId.toString()+"-"+contactId.toString()+"添加美豆账户消费记录成功！本次消费："+price2);

                    //修改充值账户余额
                    double newPrice = sub(accountFrashEntry.getAccount(),price);
                    accountFrashDao.updateEntry(accountFrashEntry.getID(),newPrice);
                    addLog(userId,contactId,"修改充值账户成功！余额:"+newPrice);
                    logger.info(userId.toString()+"-"+contactId.toString()+"修改充值账户成功！余额:"+newPrice);

                    //添加充值账户记录
                    AccountOrderEntry accountOrderEntry = new AccountOrderEntry(contactId,userId,"","","",price,"",Constant.ZERO,ip,0l,"");
                    accountOrderDao.addEntry(accountOrderEntry);
                    addLog(userId,contactId,"添加充值账户记录成功！余额:"+newPrice);
                    logger.info(userId.toString()+"-"+contactId.toString()+"添加充值账户记录成功！余额:"+newPrice);

                    //添加课节订单
                    this.addClassEntryBatch(classOrderEntries1);
                }catch (Exception e){
                    throw new Exception("购买异常！");
                }
            }else{
                throw  new Exception("余额不足！");
            }
            //修改课程人数
            Set<ObjectId> set = classOrderDao.getUserIdEntry(excellentCoursesEntry.getID());
            excellentCoursesEntry.setStudentNumber(set.size());
            excellentCoursesDao.addEntry(excellentCoursesEntry);
            return "购买成功";
        }else{
            throw  new Exception("订单信息不存在！");
        }
    }
    /**
     * 新直接购课
     * @param id
     * @param userId
     * @param classIds
     * @param sonId
     * @param ip
     * @return
     * @throws Exception
     */
    public String buyNewOwnClassList(ObjectId id,ObjectId userId,String classIds,ObjectId sonId,String ip,int role,int money) throws  Exception{
        //创建订单
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        if(classIds==null|| classIds.equals("")){
            throw  new Exception("请至少购买一节课程");
        }
        if(excellentCoursesEntry.getCourseType()==2 && userId.equals(sonId)){
            throw  new Exception("家长不可购买赶考网课程");
        }
        //美豆账户
        UserBehaviorEntry userBehaviorEntry = userBehaviorDao.getEntry(userId);
        if(userBehaviorEntry==null){
            List<ObjectId>  ot = new ArrayList<ObjectId>();
            UserBehaviorEntry userBehaviorEntry1 = new UserBehaviorEntry(userId,0,ot,ot,ot);
            userBehaviorDao.addEntry(userBehaviorEntry1);
            userBehaviorEntry= userBehaviorEntry1;
        }

        //充值账户
        AccountFrashEntry accountFrashEntry = accountFrashDao.getEntry(userId);
        if(accountFrashEntry==null){
            AccountFrashEntry accountFrashEntry1 = new AccountFrashEntry(userId,0,Constant.ZERO,Constant.ZERO);
            accountFrashDao.addEntry(accountFrashEntry1);
            accountFrashEntry = accountFrashEntry1;
        }

        String[] strings = classIds.split(",");
        for(String str:strings){
            objectIdList.add(new ObjectId(new String(str)));
        }
        strings = null;
        //此次所下订单
        List<HourClassEntry> classEntries = hourClassDao.getEntryList(objectIdList);
        //孩子订单查询
        List<ObjectId> classOrderEntries = classOrderDao.getOwnEntry(objectIdList, sonId);
        List<ClassOrderEntry> classOrderEntries1 = new ArrayList<ClassOrderEntry>();
        long current = System.currentTimeMillis();
        double price = 0.00;
        double price2 = 0.00;
        StringBuffer sb = new StringBuffer();
        sb.append("购买课程：");
        sb.append(excellentCoursesEntry.getTitle());
        sb.append("的");
        List<ObjectId>  cIds = new ArrayList<ObjectId>();
        if(excellentCoursesEntry!=null && classEntries.size()>0){
            for(HourClassEntry hourClassEntry: classEntries){
                if(!classOrderEntries.contains(hourClassEntry.getID())){
                    ClassOrderEntry classOrderEntry = new ClassOrderEntry();
                    //购买  1  未购买
                    classOrderEntry.setIsBuy(1);
                    //下单
                    classOrderEntry.setType(1);
                    classOrderEntry.setCreateTime(current);
                    classOrderEntry.setContactId(id);
                    classOrderEntry.setOrderId("");
                    classOrderEntry.setIsRemove(0);
                    classOrderEntry.setParentId(hourClassEntry.getID());
                    classOrderEntry.setFunction(3);
                    classOrderEntry.setPrice(hourClassEntry.getClassNewPrice());
                    classOrderEntry.setUserId(sonId);//孩子的订单
                    //
                    classOrderEntry.setRole(role);
                    classOrderEntry.setMoney(money);
                    //
                    classOrderEntries1.add(classOrderEntry);
                    //相加
                    price2 = sum(price2 , hourClassEntry.getClassNewPrice());
                    sb.append("第");
                    sb.append(hourClassEntry.getOrder());
                    sb.append("课时 ");
                    cIds.add(hourClassEntry.getID());
                }
            }
            if(cIds.size()==0){
                return "已经购买的用户无需再次购买！";
            }
            sb.append(" 共花费");
            sb.append(price2);
            sb.append("¥");

            //正式购买，扣除金额
            ObjectId contactId = new ObjectId();
            addLog(userId,contactId,"后台代购订单！余额:"+accountFrashEntry.getAccount()+" 订单价值:"+price2);
            logger.info(userId.toString()+"-"+contactId.toString()+"用户开始创建订单！余额:"+accountFrashEntry.getAccount()+" 订单价值"+price2);
            //自定义回滚
            //try{
            if(userBehaviorEntry!=null && userBehaviorEntry.getAccount()>= price){
                //判断余额
                if(accountFrashEntry.getAccount()< price){
                    throw new Exception("余额不足！");
                }
                double newPr = sub(userBehaviorEntry.getAccount(),price);
                try{
                    //修改美豆账户余额
                    userBehaviorDao.updateEntry(userBehaviorEntry.getID(), newPr);
                    addLog(userId,contactId,"修改美豆账户成功！余额:"+newPr);
                    logger.info(userId.toString()+"-"+contactId.toString()+"修改美豆账户成功！余额:"+newPr);

                    //添加美豆账户消费记录
                    RechargeResultEntry rechargeResultEntry = new RechargeResultEntry(userId,userId,sb.toString(),Constant.ZERO,Constant.ZERO,price,userId,excellentCoursesEntry.getID(),cIds);
                    rechargeResultDao.saveEntry(rechargeResultEntry);
                    addLog(userId,contactId,"添加美豆账户消费记录成功！本次消费："+price2);
                    logger.info(userId.toString()+"-"+contactId.toString()+"添加美豆账户消费记录成功！本次消费："+price2);

                    //修改充值账户余额
                    double newPrice = sub(accountFrashEntry.getAccount(),price);
                    accountFrashDao.updateEntry(accountFrashEntry.getID(),newPrice);
                    addLog(userId,contactId,"修改充值账户成功！余额:"+newPrice);
                    logger.info(userId.toString()+"-"+contactId.toString()+"修改充值账户成功！余额:"+newPrice);

                    //添加充值账户记录
                    AccountOrderEntry accountOrderEntry = new AccountOrderEntry(contactId,userId,"","","",price,"",Constant.ZERO,ip,0l,"");
                    accountOrderDao.addEntry(accountOrderEntry);
                    addLog(userId,contactId,"添加充值账户记录成功！余额:"+newPrice);
                    logger.info(userId.toString()+"-"+contactId.toString()+"添加充值账户记录成功！余额:"+newPrice);

                    //添加课节订单
                    this.addClassEntryBatch(classOrderEntries1);
                }catch (Exception e){
                    throw new Exception("购买异常！");
                }
            }else{
                throw  new Exception("余额不足！");
            }
            //修改课程人数
            Set<ObjectId> set = classOrderDao.getUserIdEntry(excellentCoursesEntry.getID());
            excellentCoursesEntry.setStudentNumber(set.size());
            excellentCoursesDao.addEntry(excellentCoursesEntry);
            return "购买成功";
        }else{
            throw  new Exception("订单信息不存在！");
        }
    }

    public String buyMyClassList(ObjectId id,ObjectId userId,String classIds,ObjectId sonId,String ip) throws  Exception{
        //创建订单
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        if(classIds==null|| classIds.equals("")){
            throw  new Exception("请至少购买一节课程");
        }
        if(excellentCoursesEntry.getCourseType()==2 && userId.equals(sonId)){
            throw  new Exception("家长不可购买赶考网课程");
        }
        //美豆账户
        UserBehaviorEntry userBehaviorEntry = userBehaviorDao.getEntry(userId);
        if(userBehaviorEntry==null){
            List<ObjectId>  ot = new ArrayList<ObjectId>();
            UserBehaviorEntry userBehaviorEntry1 = new UserBehaviorEntry(userId,0,ot,ot,ot);
            userBehaviorDao.addEntry(userBehaviorEntry1);
            userBehaviorEntry= userBehaviorEntry1;
        }

        //充值账户
        AccountFrashEntry accountFrashEntry = accountFrashDao.getEntry(userId);
        if(accountFrashEntry==null){
            AccountFrashEntry accountFrashEntry1 = new AccountFrashEntry(userId,0,Constant.ZERO,Constant.ZERO);
            accountFrashDao.addEntry(accountFrashEntry1);
            accountFrashEntry = accountFrashEntry1;
        }

        String[] strings = classIds.split(",");
        for(String str:strings){
            objectIdList.add(new ObjectId(str));
        }
        //此次所下订单
        List<HourClassEntry> classEntries = hourClassDao.getEntryList(objectIdList);
        //孩子订单查询
        List<ObjectId> classOrderEntries = classOrderDao.getOwnEntry(objectIdList, sonId);
        List<ClassOrderEntry> classOrderEntries1 = new ArrayList<ClassOrderEntry>();
        long current = System.currentTimeMillis();
        double price = 0.00;
        String description = "购买课程："+excellentCoursesEntry.getTitle()+"的";
        List<ObjectId>  cIds = new ArrayList<ObjectId>();
        if(excellentCoursesEntry!=null && classEntries.size()>0){
            for(HourClassEntry hourClassEntry: classEntries){
                if(!classOrderEntries.contains(hourClassEntry.getID())){
                    ClassOrderEntry classOrderEntry = new ClassOrderEntry();
                    //购买  1  未购买
                    classOrderEntry.setIsBuy(1);
                    //下单
                    classOrderEntry.setType(1);
                    classOrderEntry.setCreateTime(current);
                    classOrderEntry.setContactId(id);
                    classOrderEntry.setOrderId("");
                    classOrderEntry.setIsRemove(0);
                    classOrderEntry.setParentId(hourClassEntry.getID());
                    classOrderEntry.setFunction(0);
                    classOrderEntry.setPrice(hourClassEntry.getClassNewPrice());
                    classOrderEntry.setUserId(sonId);//孩子的订单
                    classOrderEntries1.add(classOrderEntry);
                    price = sum(price , hourClassEntry.getClassNewPrice());
                    description  = description + "第"+hourClassEntry.getOrder()+"课时 ";
                    cIds.add(hourClassEntry.getID());
                }
            }
            description = description + " 共花费"+price+"¥";

            //正式购买，扣除金额
            ObjectId contactId = new ObjectId();
            addLog(userId,contactId,"用户开始创建订单！余额:"+accountFrashEntry.getAccount()+" 订单价值:"+price);
            logger.info(userId.toString()+"-"+contactId.toString()+"用户开始创建订单！余额:"+accountFrashEntry.getAccount()+" 订单价值"+price);
            //自定义回滚
            //try{
            if(userBehaviorEntry!=null && userBehaviorEntry.getAccount()>= price){
                //判断余额
                if(accountFrashEntry.getAccount()< price){
                    throw new Exception("余额不足！");
                }
                double newPr = sub(userBehaviorEntry.getAccount(),price);
                try{
                    //修改美豆账户余额
                    userBehaviorDao.updateEntry(userBehaviorEntry.getID(), newPr);
                    addLog(userId,contactId,"修改美豆账户成功！余额:"+newPr);
                    logger.info(userId.toString()+"-"+contactId.toString()+"修改美豆账户成功！余额:"+newPr);

                    //添加美豆账户消费记录
                    RechargeResultEntry rechargeResultEntry = new RechargeResultEntry(userId,userId,description,Constant.ZERO,Constant.ZERO,price,userId,excellentCoursesEntry.getID(),cIds);
                    rechargeResultDao.saveEntry(rechargeResultEntry);
                    addLog(userId,contactId,"添加美豆账户消费记录成功！本次消费："+price);
                    logger.info(userId.toString()+"-"+contactId.toString()+"添加美豆账户消费记录成功！本次消费："+price);

                    //修改充值账户余额
                    double newPrice = sub(accountFrashEntry.getAccount(),price);
                    accountFrashDao.updateEntry(accountFrashEntry.getID(),newPrice);
                    addLog(userId,contactId,"修改充值账户成功！余额:"+newPrice);
                    logger.info(userId.toString()+"-"+contactId.toString()+"修改充值账户成功！余额:"+newPrice);

                    //添加充值账户记录
                    AccountOrderEntry accountOrderEntry = new AccountOrderEntry(contactId,userId,"","","",price,"",Constant.ZERO,ip,0l,"");
                    accountOrderDao.addEntry(accountOrderEntry);
                    addLog(userId,contactId,"添加充值账户记录成功！余额:"+newPrice);
                    logger.info(userId.toString()+"-"+contactId.toString()+"添加充值账户记录成功！余额:"+newPrice);

                    //添加课节订单
                    this.addClassEntryBatch(classOrderEntries1);
                }catch (Exception e){
                    throw new Exception("购买异常，请联系客服！");
                }

                /*//修改余额
                userBehaviorDao.updateEntry(userBehaviorEntry.getID(), userBehaviorEntry.getAccount() - price);
                //添加记录
                RechargeResultEntry rechargeResultEntry = new RechargeResultEntry(userId,userId,description,Constant.ZERO,Constant.ZERO,price,sonId,excellentCoursesEntry.getID(),cIds);
                rechargeResultDao.saveEntry(rechargeResultEntry);
                //添加课节订单
                this.addClassEntryBatch(classOrderEntries1);*/
            }else{
                throw  new Exception("余额不足！");
            }
            //修改课程人数
            Set<ObjectId> set = classOrderDao.getUserIdEntry(excellentCoursesEntry.getID());
            excellentCoursesEntry.setStudentNumber(set.size());
            excellentCoursesDao.addEntry(excellentCoursesEntry);
            return "购买成功";
        }else{
            throw  new Exception("订单信息不存在！");
        }
    }

    public double getAccount(ObjectId sonId){
        NewVersionBindRelationEntry newVersionBindRelationEntry = newVersionBindRelationDao.getBindEntry(sonId);
        if(newVersionBindRelationEntry==null){
            return 0;
        }
        UserBehaviorEntry userBehaviorEntry = userBehaviorDao.getEntry(newVersionBindRelationEntry.getMainUserId());
        //
      /*  if(userBehaviorEntry!=null){
            return userBehaviorEntry.getAccount();
        }
        return  0;*/
        if(userBehaviorEntry!=null && !userBehaviorEntry.getSonOpenList().contains(sonId)){
            return userBehaviorEntry.getAccount();
        }
        return 0;
    }

    public double getMyAccount(ObjectId userId){
        UserBehaviorEntry userBehaviorEntry = userBehaviorDao.getEntry(userId);
        if(userBehaviorEntry==null){
            return 0;
        }else{
            return userBehaviorEntry.getAccount();
        }
    }

    /**
     * 批量增加课时
     * @param list
     */
    public void addClassEntryBatch(List<ClassOrderEntry> list) {
        List<DBObject> dbList = new ArrayList<DBObject>();
        for (int i = 0; list != null && i < list.size(); i++) {
            ClassOrderEntry si = list.get(i);
            dbList.add(si.getBaseEntry());
        }
        //导入新纪录
        if(dbList.size()>0) {
            classOrderDao.addBatch(dbList);
        }
    }

    /**
     * 我的收藏
     * @return
     */
    public Map<String,Object> myCollectList(ObjectId userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        UserBehaviorEntry userBehaviorEntry = userBehaviorDao.getEntry(userId);
        List<ExcellentCoursesDTO> dtos = new ArrayList<ExcellentCoursesDTO>();
        Map<ObjectId,ExcellentCoursesDTO> listMap = new HashMap<ObjectId, ExcellentCoursesDTO>();
        long current = System.currentTimeMillis();
        if (userBehaviorEntry != null) {
            List<ObjectId> objectIdList = userBehaviorEntry.getCollectList();
            List<ExcellentCoursesEntry> excellentCoursesEntries = excellentCoursesDao.getOtherEntryListById(objectIdList);
            for (ExcellentCoursesEntry entry : excellentCoursesEntries) {
                //dtos.add(new ExcellentCoursesDTO(entry));
                if(entry.getEndTime()>current){
                    listMap.put(entry.getID(),new ExcellentCoursesDTO(entry));
                }
            }
            int size = objectIdList.size();
            List<String> ol = new ArrayList<String>();
            for(int i = 0;i<size;i++){
                ExcellentCoursesDTO excellentCoursesDTO = listMap.get(objectIdList.get(size-i-1));
                if(excellentCoursesDTO!=null && !ol.contains(excellentCoursesDTO.getId())){
                    ol.add(excellentCoursesDTO.getId());
                    dtos.add(excellentCoursesDTO);
                }
            }
        }

        map.put("list", dtos);
        map.put("count", dtos.size());
        return map;
    }

    /**
     * 课程中心
     */
    public Map<String,Object> myClassList(ObjectId userId,String subjectId,int priceType,int persionType,int timeType,int page,int pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        long current = System.currentTimeMillis();
        NewVersionUserRoleEntry newVersionUserRoleEntry = newVersionUserRoleDao.getEntry(userId);
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        if(newVersionUserRoleEntry.getNewRole()==3 || newVersionUserRoleEntry.getNewRole()==0){//家长
            //获得家长所在社群
            objectIdList = communityService.getCommunitys3(userId, 1, 100);
        }else{//学生
            //获得学生所在社群
            objectIdList = newVersionBindService.getCommunityIdsByUserId(userId);
        }
        List<ExcellentCoursesEntry> excellentCoursesEntries = excellentCoursesDao.getAllEntryList(subjectId,priceType,persionType,timeType,page,pageSize,current,objectIdList);
        int count = excellentCoursesDao.selectCount(subjectId,current,objectIdList);
        List<ExcellentCoursesDTO> dtos = new ArrayList<ExcellentCoursesDTO>();
        for(ExcellentCoursesEntry excellentCoursesEntry:excellentCoursesEntries){
            if(excellentCoursesEntry.getOpen()==1){
                List<ObjectId> newList = new ArrayList<ObjectId>();
                newList.addAll(objectIdList);
                newList.removeAll(excellentCoursesEntry.getCommunityIdList());
                if(objectIdList.size()!=newList.size()){//推荐
                    excellentCoursesEntry.setOpen(0);
                }
            }
            dtos.add(new ExcellentCoursesDTO(excellentCoursesEntry));
        }
        map.put("list", dtos);
        map.put("count", count);
        return map;
    }

    /**
     * 搜索课程中心
     * @return
     */
    public List<ExcellentCoursesDTO> myKeyClassList(String keyword){
        List<ExcellentCoursesDTO> dtos = new ArrayList<ExcellentCoursesDTO>();
        if(keyword!=null && !keyword.equals("")){

        }else{
            return dtos;
        }
        long current = System.currentTimeMillis();
        List<ExcellentCoursesEntry> excellentCoursesEntries = excellentCoursesDao.getMyKeyClassList(keyword,current);
        for(ExcellentCoursesEntry excellentCoursesEntry:excellentCoursesEntries) {
            dtos.add(new ExcellentCoursesDTO(excellentCoursesEntry));
        }
        return dtos;
    }

    /**
     * 老师 获得我的课程列表（增加打包课的列表）
     * @return
     */
    public Map<String,Object> getMyExcellentCourses(ObjectId userId,int page,int pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        //List<ExcellentCoursesEntry> excellentCoursesEntries = excellentCoursesDao.getMyExcellentCourses(userId, page, pageSize);
        //打包课逻辑
        List<ExcellentCoursesEntry> excellentCoursesEntries = excellentCoursesDao.getMyAndNewExcellentCourses(userId, page, pageSize);
        //int count = excellentCoursesDao.selectMyCount(userId);
        int count = excellentCoursesDao.selectMyNewCount(userId);
        List<ExcellentCoursesDTO> dtos = new ArrayList<ExcellentCoursesDTO>();
        long current = System.currentTimeMillis();
        List<ObjectId> communityIds = new ArrayList<ObjectId>();
        for(ExcellentCoursesEntry excellentCoursesEntry:excellentCoursesEntries){
            communityIds.addAll(excellentCoursesEntry.getCommunityIdList());
            ExcellentCoursesDTO dto = new ExcellentCoursesDTO(excellentCoursesEntry);
            if(dto.getStatus()==2){
                if(excellentCoursesEntry.getStartTime()> current){//未开始
                    dto.setType(1);
                }else if(excellentCoursesEntry.getEndTime()<current){//已结束
                    dto.setType(2);
                }else{
                    dto.setType(1);
                }
            }
            if(dto.getUserId()!=null && dto.getUserId().equals(userId.toString())){
                dto.setIsCollect(1);
            }else{
                dto.setIsCollect(0);
                List<HourClassEntry> hourClassEntries = hourClassDao.getEntryList(excellentCoursesEntry.getID());
                long stm = 0;
                long etm = 0;
                int  index = 0;
                for(HourClassEntry hourClassEntry:hourClassEntries){
                    if(hourClassEntry.getOwnId()!=null && hourClassEntry.getOwnId().toString().equals(userId.toString())){
                        index++;
                        long s = hourClassEntry.getStartTime();
                        long e = hourClassEntry.getStartTime()+hourClassEntry.getCurrentTime();
                        if(index==1){
                            stm = s;
                            etm = e;
                        }else{
                            if(stm>s){
                                stm= s;
                            }
                            if(e>etm){
                                etm = e;
                            }
                        }
                    }
                }

                if(stm!=0l){
                    dto.setStartTime(DateTimeUtils.getLongToStrTimeTwo(stm).substring(0, 11));
                }else{
                    dto.setStartTime("");
                }

                if(etm!=0l){
                    dto.setEndTime(DateTimeUtils.getLongToStrTimeTwo(etm).substring(0, 11));
                }else{
                    dto.setEndTime("");
                }
            }

            dtos.add(dto);
        }
        Map<ObjectId, CommunityEntry> cmap = communityDao.findMapInfo(communityIds);
        for(ExcellentCoursesDTO dto2: dtos){
            List<String> oids = dto2.getCommunityIdList();
            String name = "";
            for(String str: oids){
                CommunityEntry c = cmap.get(new ObjectId(str));
                if(c!=null){
                    name = name + c.getCommunityName()+"、\n";
                }
            }
            if(name.equals("")){
                dto2.setCommunitName("无");
            }else{
                String name2 = name.substring(0, name.length()-2);

                dto2.setCommunitName(name2);
            }

        }
        map.put("list",dtos);
        map.put("count",count);
        return map;
    }

    /**
     * 家长 获得我所有的课程列表
     * @return
     */
    public Map<String,Object> getMyAllBuyExcellentCourses(ObjectId userId,int page,int pageSize){
        Map<String,Object> map  = new HashMap<String, Object>();
        List<ObjectId> objectIdList = newVersionBindRelationDao.getIdsByMainUserId(userId);
        //加入自己表明自己购买的
        objectIdList.add(userId);
        List<ClassOrderEntry> classOrderEntries =  classOrderDao.getCoursesUserList(objectIdList);
        List<ObjectId> objectIdList1 = new ArrayList<ObjectId>();
        List<ObjectId> hourClassIds = new ArrayList<ObjectId>();
        for(ClassOrderEntry classOrderEntry:classOrderEntries){
            if(classOrderEntry.getIsBuy()==1){
                objectIdList1.add(classOrderEntry.getContactId());
                hourClassIds.add(classOrderEntry.getParentId());
            }
        }
        long current = System.currentTimeMillis();
        List<ExcellentCoursesEntry> excellentCoursesEntries = excellentCoursesDao.getMyBuyEntryListById(objectIdList1, page, pageSize);
        int count = excellentCoursesDao.countMyBuyEntryListById(objectIdList1);
        List<ExcellentCoursesDTO> dtos = new ArrayList<ExcellentCoursesDTO>();
        List<ObjectId> communityIds = new ArrayList<ObjectId>();
        for(ExcellentCoursesEntry excellentCoursesEntry:excellentCoursesEntries){
            communityIds.addAll(excellentCoursesEntry.getCommunityIdList());
            ExcellentCoursesDTO dto = new ExcellentCoursesDTO(excellentCoursesEntry);
            if(dto.getStatus()==2){
                if(excellentCoursesEntry.getStartTime()> current){//未开始
                    dto.setType(1);
                }else if(excellentCoursesEntry.getEndTime()<current){//已结束
                    dto.setType(2);
                }else{
                    dto.setType(1);
                }
            }
            dtos.add(dto);
        }
        Map<ObjectId, CommunityEntry> cmap = communityDao.findMapInfo(communityIds);
        for(ExcellentCoursesDTO dto2: dtos){
            List<String> oids = dto2.getCommunityIdList();
            String name = "";
            for(String str: oids){
                CommunityEntry c = cmap.get(new ObjectId(str));
                if(c!=null){
                    name = name + c.getCommunityName()+"、\n";
                }
            }
            if(name.equals("")){
                dto2.setCommunitName("无");
            }else{
                String name2 = name.substring(0, name.length()-2);

                dto2.setCommunitName(name2);
            }

        }
        map.put("list",dtos);
        map.put("count", count);
        return map;
    }

    /**
     * 学生 获得我所有的课程列表
     * @return
     */
    public Map<String,Object> getStudentBuyExcellentCourses(ObjectId userId,int page,int pageSize){
        Map<String,Object> map  = new HashMap<String, Object>();
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        NewVersionBindRelationEntry bindEntry = newVersionBindRelationDao.getBindEntry(userId);
        //加入自己表明自己购买的
        objectIdList.add(userId);
        List<ClassOrderEntry> classOrderEntries =  classOrderDao.getCoursesUserList(objectIdList);
        List<ObjectId> objectIdList1 = new ArrayList<ObjectId>();
        List<ObjectId> hourClassIds = new ArrayList<ObjectId>();
        for(ClassOrderEntry classOrderEntry:classOrderEntries){
            if(classOrderEntry.getIsBuy()==1){
                objectIdList1.add(classOrderEntry.getContactId());
                hourClassIds.add(classOrderEntry.getParentId());
            }
        }
        long current = System.currentTimeMillis();
        List<ExcellentCoursesEntry> excellentCoursesEntries = excellentCoursesDao.getMyBuyEntryListById(objectIdList1, page, pageSize);
        int count = excellentCoursesDao.countMyBuyEntryListById(objectIdList1);
        List<ExcellentCoursesDTO> dtos = new ArrayList<ExcellentCoursesDTO>();
        List<ObjectId> communityIds = new ArrayList<ObjectId>();
        for(ExcellentCoursesEntry excellentCoursesEntry:excellentCoursesEntries){
            communityIds.addAll(excellentCoursesEntry.getCommunityIdList());
            ExcellentCoursesDTO dto = new ExcellentCoursesDTO(excellentCoursesEntry);
            if(dto.getStatus()==2){
                if(excellentCoursesEntry.getStartTime()> current){//未开始
                    dto.setType(1);
                }else if(excellentCoursesEntry.getEndTime()<current){//已结束
                    dto.setType(2);
                }else{
                    dto.setType(1);
                }
            }
            dtos.add(dto);
        }
        Map<ObjectId, CommunityEntry> cmap = communityDao.findMapInfo(communityIds);
        for(ExcellentCoursesDTO dto2: dtos){
            List<String> oids = dto2.getCommunityIdList();
            String name = "";
            for(String str: oids){
                CommunityEntry c = cmap.get(new ObjectId(str));
                if(c!=null){
                    name = name + c.getCommunityName()+"、\n";
                }
            }
            if(name.equals("")){
                dto2.setCommunitName("无");
            }else{
                String name2 = name.substring(0, name.length()-2);

                dto2.setCommunitName(name2);
            }

        }
        map.put("list",dtos);
        map.put("count", count);
        return map;
    }

    public Map<String,Object> getMyDetails(ObjectId id){
        Map<String,Object> map = new HashMap<String, Object>();
        //课程相关
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        ExcellentCoursesDTO dto = new ExcellentCoursesDTO(excellentCoursesEntry);
        List<CommunityEntry> communityEntries = communityDao.findByObjectIds(excellentCoursesEntry.getCommunityIdList());//
        String stringList = "";
        for(CommunityEntry communityEntry:communityEntries){
            stringList = stringList +communityEntry.getCommunityName()+ "、\n";
        }
        if(stringList.equals("")){
            dto.setCommunitName("无");
        }else{
            String stringList2 = stringList.substring(0, stringList.length()-2);
            dto.setCommunitName(stringList2);
        }

        long current = System.currentTimeMillis();
        if(dto.getStatus()==2){
            if(excellentCoursesEntry.getStartTime()> current){//未开始
                dto.setType(1);
            }else if(excellentCoursesEntry.getEndTime()<current){//已结束
                dto.setType(2);
            }else{
                dto.setType(1);
            }
        }
        map.put("dto",dto);
        //课时相关
        List<HourClassEntry> hourClassEntries = hourClassDao.getEntryList(id);
        List<HourClassDTO> hourClassDTOs = new ArrayList<HourClassDTO>();
        for(HourClassEntry entry : hourClassEntries){
            long startTime = entry.getStartTime();
            long endTime = entry.getStartTime() + entry.getCurrentTime();
            HourClassDTO hourClassDTO = new HourClassDTO(entry);
            if(excellentCoursesEntry.getStatus()==2){
                if(current<startTime - TEACHER_TIME){//未开始
                    hourClassDTO.setStatus(0);
                }else if(current >endTime){//已结束
                    hourClassDTO.setStatus(2);
                }else{//正在进行
                    hourClassDTO.setStatus(1);
                }
            }
            hourClassDTOs.add(hourClassDTO);
        }
        map.put("list",hourClassDTOs);
        return map;
    }

    public Map<String,Object> getMyNewDetails(ObjectId id,ObjectId userId){
        Map<String,Object> map = new HashMap<String, Object>();
        //课程相关
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        ExcellentCoursesDTO dto = new ExcellentCoursesDTO(excellentCoursesEntry);
        List<CommunityEntry> communityEntries = communityDao.findByObjectIds(excellentCoursesEntry.getCommunityIdList());//
        String stringList = "";
        for(CommunityEntry communityEntry:communityEntries){
            stringList = stringList +communityEntry.getCommunityName()+ "、\n";
        }
        if(stringList.equals("")){
            dto.setCommunitName("无");
        }else{
            String stringList2 = stringList.substring(0, stringList.length()-2);
            dto.setCommunitName(stringList2);
        }

        long current = System.currentTimeMillis();
        if(dto.getStatus()==2){
            if(excellentCoursesEntry.getStartTime()> current){//未开始
                dto.setType(1);
            }else if(excellentCoursesEntry.getEndTime()<current){//已结束
                dto.setType(2);
            }else{
                dto.setType(1);
            }
        }

        boolean fl = false;
        if(excellentCoursesEntry.getUserId().toString().equals(userId.toString())){
            fl=true;
        }
        String name = excellentCoursesEntry.getUserName();
        //课时相关
        List<HourClassEntry> hourClassEntries = hourClassDao.getEntryList(id);
        List<HourClassDTO> hourClassDTOs = new ArrayList<HourClassDTO>();
        long stm = 0;
        long etm = 0;
        int index = 0;
        for(HourClassEntry entry : hourClassEntries){
            long startTime = entry.getStartTime();
            long endTime = entry.getStartTime() + entry.getCurrentTime();
            HourClassDTO hourClassDTO = new HourClassDTO(entry);
            if(excellentCoursesEntry.getStatus()==2){
                if(current<startTime - TEACHER_TIME){//未开始
                    hourClassDTO.setStatus(0);
                }else if(current >endTime){//已结束
                    hourClassDTO.setStatus(2);
                }else{//正在进行
                    if(hourClassDTO.getOwnId()==null || hourClassDTO.getOwnId().equals(userId.toString())){//是上课人
                        hourClassDTO.setStatus(1);
                    }else{
                        hourClassDTO.setStatus(0);
                    }

                }
            }
            if(fl){//是开课人
                dto.setIsCollect(1);
                hourClassDTOs.add(hourClassDTO);
            }else{
                dto.setIsCollect(0);
                if(hourClassDTO.getOwnId()!=null && hourClassDTO.getOwnId().equals(userId.toString())){
                    name = hourClassDTO.getOwnName();
                    hourClassDTOs.add(hourClassDTO);
                    index++;
                    long s = entry.getStartTime();
                    long e = entry.getStartTime()+entry.getCurrentTime();
                    if(index==1){
                        stm = s;
                        etm = e;
                    }else{
                        if(stm>s){
                            stm=s;
                        }
                        if(etm<e){
                            etm=e;
                        }
                    }
                }
            }

        }
        if(stm!=0l){
            dto.setStartTime(DateTimeUtils.getLongToStrTimeTwo(stm).substring(0,11));
        }
        if(etm!=0l){
            dto.setEndTime(DateTimeUtils.getLongToStrTimeTwo(etm).substring(0,11));
        }
        dto.setUserName(name);
        map.put("dto",dto);
        map.put("list", hourClassDTOs);
        return map;
    }

    public Map<String,Object> getMyOwnDetails(ObjectId id,ObjectId userId){
        Map<String,Object> map = new HashMap<String, Object>();
        //课程相关
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        ExcellentCoursesDTO dto = new ExcellentCoursesDTO(excellentCoursesEntry);
        List<CommunityEntry> communityEntries = communityDao.findByObjectIds(excellentCoursesEntry.getCommunityIdList());//
        String stringList = "";
        for(CommunityEntry communityEntry:communityEntries){
            stringList = stringList +communityEntry.getCommunityName()+ "、\n";
        }
        if(stringList.equals("")){
            dto.setCommunitName("无");
        }else{
            String stringList2 = stringList.substring(0, stringList.length()-2);
            dto.setCommunitName(stringList2);
        }

        long current = System.currentTimeMillis();
        if(dto.getStatus()==2){
            if(excellentCoursesEntry.getStartTime()> current){//未开始
                dto.setType(1);
            }else if(excellentCoursesEntry.getEndTime()<current){//已结束
                dto.setType(2);
            }else{
                dto.setType(1);
            }
        }
        map.put("dto",dto);
        //课时相关
        List<HourClassEntry> hourClassEntries = hourClassDao.getEntryList(id);
        //订单相关
        //此课程已购买项目
        List<ObjectId> cids= classOrderDao.getEntryIdList(userId,id);
        List<HourClassDTO> hourClassDTOs = new ArrayList<HourClassDTO>();
        for(HourClassEntry entry : hourClassEntries){
            long startTime = entry.getStartTime();
            long endTime = entry.getStartTime() + entry.getCurrentTime();
            HourClassDTO hourClassDTO = new HourClassDTO(entry);
            if(excellentCoursesEntry.getStatus()==2){
                if(current<startTime - STUDENT_TIME){//未开始
                    hourClassDTO.setStatus(0);
                }else if(current >endTime){//已结束
                    if(cids.contains(new ObjectId(hourClassDTO.getId()))){
                        hourClassDTO.setStatus(5);
                    }else{
                        hourClassDTO.setStatus(2);
                    }

                }else{//正在进行
                    if(cids.contains(new ObjectId(hourClassDTO.getId()))){
                        hourClassDTO.setStatus(1);
                    }else{
                        hourClassDTO.setStatus(4);
                    }
                }
            }
            hourClassDTOs.add(hourClassDTO);
        }
        map.put("list", hourClassDTOs);
        List<ReplayDTO> dtos =  coursesRoomService.getAllBackList(excellentCoursesEntry.getID(), "123456", excellentCoursesEntry.getUserName(),userId);
        map.put("backList",dtos);
        return map;
    }

    public List<User> getCoursesPersionNum(ObjectId id){
        List<User> userList = new ArrayList<User>();
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        if(excellentCoursesEntry==null){
            return userList;
        }
        //用户订单查询
        Set<ObjectId> userIds = classOrderDao.getUserIdEntry(id);
        List<UserEntry> userEntries = userDao.getUserEntryList(userIds, Constant.FIELDS);
        for(UserEntry userEntry : userEntries){
            String name = StringUtils.isNotEmpty(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
            User user=new User(name,
                    name,
                    userEntry.getID().toString(),
                    AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(), userEntry.getSex()),
                    userEntry.getSex(),
                    "");
            userList.add(user);
        }
        excellentCoursesEntry.setStudentNumber(userList.size());
        excellentCoursesDao.addEntry(excellentCoursesEntry);
        return userList;
    }

    /**
     *
     */
    public String deleteCourses(ObjectId id,ObjectId userId){
        //1. 判断是否可删除
        //课程相关
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        if(excellentCoursesEntry==null){
            return "课程已被删除";
        }

        if(!excellentCoursesEntry.getUserId().equals(userId)){
            return "您没有权限删除该课程";
        }
        //用户订单查询
        Set<ObjectId> userIds = classOrderDao.getUserIdEntry(id);
        if(userIds.size()>0){//无用户下单
            return "有用户下单了，不可删除";
        }
        //2. 删除课程
        excellentCoursesDao.delEntry(id);
        //3. 删除课节
        hourClassDao.delEntry(id,userId);
        //4. 删除附加表
        coursesBusinessDao.delEntry(id);

        return "删除成功";
    }

    public Map<String,Object> booleanUpdateCourses(ObjectId id,ObjectId userId){
        Map<String,Object> map = new HashMap<String, Object>();
        //1. 判断是否可删除
        //课程相关
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        if(excellentCoursesEntry==null){
            map.put("yin","该课程不存在");
            map.put("bo",false);
            return map;
        }

        if(!excellentCoursesEntry.getUserId().equals(userId)){
            map.put("yin","您没有权限删除该课程");
            map.put("bo",false);
            return map;
        }
        //用户订单查询
        Set<ObjectId> userIds = classOrderDao.getUserIdEntry(id);
        if(userIds.size()>0){//有用户下单
            map.put("yin","有用户下单了，不可删除");
            map.put("bo",false);
            return map;
        }
        map.put("yin","可编辑");
        map.put("bo",true);
        return map;
    }


    public Map<String,Object> gotoClass(ObjectId id,ObjectId userId) throws Exception{
        Map<String,Object> map = new HashMap<String, Object>();
        //是否过期
        HourClassEntry hourClassEntry = hourClassDao.getEntry(id);
        if(hourClassEntry==null){
            throw  new Exception("该课程不存在！");
        }
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(hourClassEntry.getParentId());
        if(excellentCoursesEntry==null){
            throw  new Exception("该课程不存在！");
        }
        ClassOrderEntry classOrderEntry = classOrderDao.getEntry(id, hourClassEntry.getParentId(), userId);
        if(classOrderEntry==null){
            throw  new Exception("无该订单！");
        }
        long current = System.currentTimeMillis();
        long start = hourClassEntry.getStartTime() -STUDENT_TIME;
        long end = hourClassEntry.getStartTime() + hourClassEntry.getCurrentTime();
        //if(current>start && current < end){//上课中
        if(current>start){//上课中
            CoursesRoomEntry coursesRoomEntry = coursesRoomDao.getEntry(excellentCoursesEntry.getID());
            UserEntry userEntry = userDao.findByUserId(userId);
            NewVersionBindRelationEntry newVersionBindRelationEntry = newVersionBindRelationDao.getBindEntry(userId);
            if(userEntry==null){
                throw  new Exception("非法用户！");
            }
            String name = StringUtils.isNotEmpty(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
            if(newVersionBindRelationEntry!=null && newVersionBindRelationEntry.getUserName()!=null && !newVersionBindRelationEntry.getUserName().equals("")){
                name = newVersionBindRelationEntry.getUserName();
            }
            map.put("uid",coursesRoomEntry.getUserId());
            map.put("roomid",coursesRoomEntry.getRoomId());
            map.put("userName",name);
           // map.put("password",coursesRoomEntry.getPlaypass());
            if(coursesRoomEntry.getAuthtype()==0){//接口认证
                map.put("password",userId.toString());
            }else{
                map.put("password",coursesRoomEntry.getPlaypass());
            }
            //去上课
            if( classOrderEntry.getType()==1){
                classOrderDao.updateToEntry(classOrderEntry.getID());

                if(newVersionBindRelationEntry!=null){
                    //发送通知
                    try{
                        systemMessageService.sendClassNotice(newVersionBindRelationEntry.getMainUserId(),1,excellentCoursesEntry.getTitle(),name);
                    }catch (Exception e){

                    }

                }
            }
        }else{
            throw  new Exception("上课时间未到，请稍后进入");
        }
        return map;
    }

    public Map<String,Object> gotoNewClass(ObjectId id,ObjectId userId) throws Exception{
        Map<String,Object> map = new HashMap<String, Object>();
        //是否过期
        HourClassEntry hourClassEntry = hourClassDao.getEntry(id);
        if(hourClassEntry==null){
            throw  new Exception("该课程不存在！");
        }
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(hourClassEntry.getParentId());
        if(excellentCoursesEntry==null){
            throw  new Exception("该课程不存在！");
        }
        ClassOrderEntry classOrderEntry = classOrderDao.getEntry(id, hourClassEntry.getParentId(), userId);
        if(classOrderEntry==null){
            throw  new Exception("无该订单！");
        }
        long current = System.currentTimeMillis();
        long start = hourClassEntry.getStartTime() -STUDENT_TIME;
        long end = hourClassEntry.getStartTime() + hourClassEntry.getCurrentTime();
        //if(current>start && current < end){//上课中
        if(current>start){//上课中
            CoursesRoomEntry coursesRoomEntry = coursesRoomDao.getEntry(excellentCoursesEntry.getID());
            UserEntry userEntry = userDao.findByUserId(userId);
            NewVersionBindRelationEntry newVersionBindRelationEntry = newVersionBindRelationDao.getBindEntry(userId);
            if(userEntry==null){
                throw  new Exception("非法用户！");
            }
            String name = StringUtils.isNotEmpty(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
            map.put("uid",coursesRoomEntry.getUserId());
            map.put("roomid",coursesRoomEntry.getRoomId());
            if(newVersionBindRelationEntry!=null && newVersionBindRelationEntry.getUserName()!=null && !newVersionBindRelationEntry.getUserName().equals("")){
                name = newVersionBindRelationEntry.getUserName();
            }
            map.put("userName",name);
            if(coursesRoomEntry.getAuthtype()==0){//接口认证
                map.put("password",userId.toString());
            }else{
                map.put("password",coursesRoomEntry.getPlaypass());
            }
            map.put("id",excellentCoursesEntry.getID().toString());
            map.put("bigCover",excellentCoursesEntry.getBigCouver());
            //去上课
            if( classOrderEntry.getType()==1){
                classOrderDao.updateToEntry(classOrderEntry.getID());

                if(newVersionBindRelationEntry!=null){
                    //发送通知
                    try{
                        systemMessageService.sendClassNotice(newVersionBindRelationEntry.getMainUserId(),1,excellentCoursesEntry.getTitle(),name);
                    }catch (Exception e){

                    }

                }
            }
        }else{
            throw  new Exception("上课时间未到，请稍后进入");
        }
        return map;
    }

    public Map<String,Object> gotoThreeClass(ObjectId id,ObjectId userId) throws Exception{
        Map<String,Object> map = new HashMap<String, Object>();
        //是否过期
        HourClassEntry hourClassEntry = hourClassDao.getEntry(id);
        if(hourClassEntry==null){
            throw  new Exception("该课程不存在！");
        }
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(hourClassEntry.getParentId());
        if(excellentCoursesEntry==null){
            throw  new Exception("该课程不存在！");
        }
        ClassOrderEntry classOrderEntry = classOrderDao.getEntry(id, hourClassEntry.getParentId(), userId);
        if(classOrderEntry==null){
            throw  new Exception("无该订单！");
        }
        long current = System.currentTimeMillis();
        long start = hourClassEntry.getStartTime() -STUDENT_TIME;
        long end = hourClassEntry.getStartTime() + hourClassEntry.getCurrentTime();
        //if(current>start && current < end){//上课中
        if(current>start){//上课中
            CoursesRoomEntry coursesRoomEntry = coursesRoomDao.getEntry(excellentCoursesEntry.getID());
            UserEntry userEntry = userDao.findByUserId(userId);
            NewVersionBindRelationEntry newVersionBindRelationEntry = newVersionBindRelationDao.getBindEntry(userId);
            if(userEntry==null){
                throw  new Exception("非法用户！");
            }
            String name = StringUtils.isNotEmpty(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
            map.put("uid",coursesRoomEntry.getUserId());
            map.put("roomid",coursesRoomEntry.getRoomId());
            if(newVersionBindRelationEntry!=null && newVersionBindRelationEntry.getUserName()!=null && !newVersionBindRelationEntry.getUserName().equals("")){
                name = newVersionBindRelationEntry.getUserName();
            }
            map.put("userName",name);
          //  map.put("password",coursesRoomEntry.getPlaypass());
            if(coursesRoomEntry.getAuthtype()==0){//接口认证
                map.put("password",userId.toString());
            }else{
                map.put("password",coursesRoomEntry.getPlaypass());
            }
            //map.put("id",excellentCoursesEntry.getID().toString());
            //map.put("bigCover",excellentCoursesEntry.getBigCouver());
            //去上课
            if( classOrderEntry.getType()==1){
                classOrderDao.updateToEntry(classOrderEntry.getID());

                if(newVersionBindRelationEntry!=null){
                    //发送通知
                    try{
                        systemMessageService.sendClassNotice(newVersionBindRelationEntry.getMainUserId(),1,excellentCoursesEntry.getTitle(),name);
                    }catch (Exception e){

                    }

                }
            }
        }else{
            throw  new Exception("上课时间未到，请稍后进入");
        }
        return map;
    }

    //老师自动登陆
    public String teacherLogin(ObjectId userId,ObjectId id) throws Exception{
        //是否过期
        HourClassEntry hourClassEntry = hourClassDao.getEntry(id);
        if(hourClassEntry==null){
            throw  new Exception("该课程不存在！");
        }
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(hourClassEntry.getParentId());
        if(excellentCoursesEntry==null){
            throw  new Exception("该课程不存在！");
        }
        long current = System.currentTimeMillis();
        long start = hourClassEntry.getStartTime()-TEACHER_TIME;
        long end =  hourClassEntry.getStartTime() + hourClassEntry.getCurrentTime();
        if(current>start  && current < end) {//上课中
            CoursesRoomEntry coursesRoomEntry = coursesRoomDao.getEntry(excellentCoursesEntry.getID());
            UserEntry userEntry = userDao.findByUserId(userId);
            if(userEntry==null){
                throw  new Exception("非法用户！");
            }
             /*https://view.csslcloud.net/api/view/lecturer?roomid=xxx&userid=xxx&publishname=xxx&publishpassword=xxx*/
         /*   return  "https://view.csslcloud.net/api/view/lecturer?roomid="
                    +coursesRoomEntry.getRoomId()+"&userid="
                    +coursesRoomEntry.getUserId()+"&publishname="
                    +excellentCoursesEntry.getUserName()+"&publishpassword="
                    +coursesRoomEntry.getPublisherpass();*/

            return "cclive://"+coursesRoomEntry.getUserId()+"/"+coursesRoomEntry.getRoomId()+"/"+excellentCoursesEntry.getUserName()+"/"+coursesRoomEntry.getPublisherpass();
        }
        return  "";
    }

    //新老师自动登陆
    public String teacherNewLogin(ObjectId userId,ObjectId id) throws Exception{
        //是否过期
        HourClassEntry hourClassEntry = hourClassDao.getEntry(id);
        if(hourClassEntry==null){
            throw  new Exception("该课程不存在！");
        }
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(hourClassEntry.getParentId());
        if(excellentCoursesEntry==null){
            throw  new Exception("该课程不存在！");
        }
        long current = System.currentTimeMillis();
        long start = hourClassEntry.getStartTime()-TEACHER_TIME;
        long end =  hourClassEntry.getStartTime() + hourClassEntry.getCurrentTime();
        String userName = excellentCoursesEntry.getUserName();
        if(hourClassEntry.getOwnName()!=null){
            userName = hourClassEntry.getOwnName();
        }
        if(current>start  && current < end) {//上课中
            CoursesRoomEntry coursesRoomEntry = coursesRoomDao.getEntry(excellentCoursesEntry.getID());
            UserEntry userEntry = userDao.findByUserId(userId);
            if(userEntry==null){
                throw  new Exception("非法用户！");
            }
             /*https://view.csslcloud.net/api/view/lecturer?roomid=xxx&userid=xxx&publishname=xxx&publishpassword=xxx*/
         /*   return  "https://view.csslcloud.net/api/view/lecturer?roomid="
                    +coursesRoomEntry.getRoomId()+"&userid="
                    +coursesRoomEntry.getUserId()+"&publishname="
                    +excellentCoursesEntry.getUserName()+"&publishpassword="
                    +coursesRoomEntry.getPublisherpass();*/

            return "cclive://"+coursesRoomEntry.getUserId()+"/"+coursesRoomEntry.getRoomId()+"/"+userName+"/"+coursesRoomEntry.getPublisherpass();
        }
        return  "";
    }

    /************************************充值相关START*****************************************/
    public String freeChong(ObjectId userId,int number,String userName){
        UserEntry userEntry = userDao.getUserEntryByName(userName);
        if(userEntry!=null){
            //添加美豆记录
            RechargeResultEntry rechargeResultEntry = new RechargeResultEntry(userId,
                    userId,"自定义充值  "+number+"¥",Constant.THREE,Constant.ONE,number,userEntry.getID(),null,new ArrayList<ObjectId>());
            rechargeResultDao.saveEntry(rechargeResultEntry);

            //添加美豆金额
            UserBehaviorEntry userBehaviorEntry = userBehaviorDao.getEntry(userEntry.getID());
            if(userBehaviorEntry!=null){
                userBehaviorDao.updateEntry(userBehaviorEntry.getID(),userBehaviorEntry.getAccount()+number);
            }else{
                List<ObjectId>  objectIdList = new ArrayList<ObjectId>();
                List<ObjectId>  objectIdList2 = new ArrayList<ObjectId>();
                UserBehaviorEntry userBehaviorEntry1 = new UserBehaviorEntry(userId,number,objectIdList,objectIdList2,objectIdList);
                userBehaviorDao.addEntry(userBehaviorEntry1);
            }

            //增加账户记录
            AccountOrderEntry accountOrderEntry = new AccountOrderEntry(null,userId,"","","",number,"",Constant.ONE,"",0l,"");
            accountOrderDao.addEntry(accountOrderEntry);


            //增加账户金额
            AccountFrashEntry accountFrashEntry = accountFrashDao.getEntry(userId);
            if(accountFrashEntry!=null){
                accountFrashEntry.setAccount(accountFrashEntry.getAccount()+number);
                accountFrashDao.addEntry(accountFrashEntry);
            }else{
                AccountFrashEntry accountFrashEntry1 = new AccountFrashEntry(userId,number,Constant.ZERO,Constant.ZERO);
                accountFrashDao.addEntry(accountFrashEntry1);
            }


            return "充值成功！";
        }else{
            return "用户不存在，充值失败！";
        }

    }
    //家长退课流程
    public Map<String,Object> deleteOrderSelectClass(ObjectId id,ObjectId userId,ObjectId sonId) throws Exception{
        Map<String,Object> map = new HashMap<String, Object>();
        long current = System.currentTimeMillis();
        //1.查询该课程
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        if(excellentCoursesEntry==null){
            throw new Exception("课程不存在");
        }
        // 课程状态判断
        boolean flag = false;
        if(current>excellentCoursesEntry.getEndTime() || excellentCoursesEntry.getType()!=0){
            flag = true;
        }
        //2.查询该用户的订单记录
        Map<ObjectId,ClassOrderEntry> entryMap = classOrderDao.getEntryList(sonId, id);
        List<HourClassDTO> dtos = new ArrayList<HourClassDTO>();
        //3.查询课节
        List<HourClassEntry> hourClassEntries = hourClassDao.getEntryList(id);
        //4.组装状态（使用下单时成交的价格）
        for(HourClassEntry hourClassEntry : hourClassEntries) {
            HourClassDTO hourClassDTO = new HourClassDTO(hourClassEntry);
            long endTime = hourClassEntry.getStartTime() + hourClassEntry.getCurrentTime();
            hourClassDTO.setStatus(1);//未购买
            ClassOrderEntry classOrderEntry = entryMap.get(hourClassEntry.getID());
            if(classOrderEntry !=null && classOrderEntry.getIsBuy()==1){//已买
                hourClassDTO.setStatus(2);//已购买
                hourClassDTO.setClassNewPrice(classOrderEntry.getPrice());
            }
            //时间判断
            if (endTime <= current) {//上课时间已结束
                hourClassDTO.setStatus(0);//已结束
            }else if(hourClassEntry.getStartTime() <= current + TUI_TIME){  //超过12小时，课时已超时
                hourClassDTO.setStatus(3);//已超时
            }else{

            }

            //异常判断
            if (flag) {//课程结束或异常
                hourClassDTO.setStatus(0);//已结束
            }
            dtos.add(hourClassDTO);
        }
        //5.确定用户
        UserEntry entry =  userDao.findByUserId(sonId);
        if(entry==null){
            throw new Exception("用户不存在");
        }

        String name = StringUtils.isNotEmpty(entry.getNickName())?entry.getNickName():entry.getUserName();
        map.put("dto",new ExcellentCoursesDTO(excellentCoursesEntry));
        map.put("name",name);
        map.put("list",dtos);
        return map;
    }

    /**
     * 退款
     * @param id
     * @param userId
     * @param sonId
     * @return
     * @throws Exception
     */
    public void refund(ObjectId id,ObjectId userId,ObjectId sonId) throws Exception{
        String str = id.toString()+"&"+sonId.toString();
        long current = System.currentTimeMillis();
        String cacheKey = CacheHandler.getKeyString(CacheHandler.CACHE_SHORTMESSAGE, str);
        String cacheKeyTime = CacheHandler.getStringValue(cacheKey);
        if (StringUtils.isNotBlank(cacheKeyTime)) {
            String str4 = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,11);
            String str5 = DateTimeUtils.getLongToStrTimeTwo(Long.parseLong(cacheKeyTime)).substring(0,11);
            if(str4.equals(str5)){
                //model.put("message", "获取验证码太频繁");
                throw new Exception("您今天已经退过该门课程，请明天再试");
            }
        }
        //1.查询该课程
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        if(excellentCoursesEntry==null){
            throw new Exception("课程不存在");
        }
        UserEntry userEntry = userDao.findByUserId(userId);
        if(userEntry==null){
            throw new Exception("用户不存在");
        }
        // 课程状态判断
        boolean flag = false;
        if(current>excellentCoursesEntry.getEndTime() || excellentCoursesEntry.getType()!=0){
            flag = true;
        }
        //2.查询该用户的订单记录
        Map<ObjectId,ClassOrderEntry> entryMap = classOrderDao.getEntryList(sonId, id);
        List<HourClassDTO> dtos = new ArrayList<HourClassDTO>();
        //3.查询课节
        List<HourClassEntry> hourClassEntries = hourClassDao.getEntryList(id);
        //4.组装状态（使用下单时成交的价格）
        List<ObjectId> oids = new ArrayList<ObjectId>();
        int price = 0;
        StringBuffer sb = new StringBuffer();
        sb.append("课程："+excellentCoursesEntry.getTitle()+"退课，课时：");
        for(HourClassEntry hourClassEntry : hourClassEntries) {
            HourClassDTO hourClassDTO = new HourClassDTO(hourClassEntry);
            long endTime = hourClassEntry.getStartTime() + hourClassEntry.getCurrentTime();
            hourClassDTO.setStatus(1);//未购买
            ClassOrderEntry classOrderEntry = entryMap.get(hourClassEntry.getID());
            if(classOrderEntry !=null && classOrderEntry.getIsBuy()==1){//已买
                hourClassDTO.setStatus(2);//已购买
                hourClassDTO.setClassNewPrice(classOrderEntry.getPrice());
            }
            //时间判断
            if (endTime <= current) {//上课时间已结束
                hourClassDTO.setStatus(0);//已结束
            }else if(hourClassEntry.getStartTime() <= current + TUI_TIME){  //超过12小时，课时已超时
                hourClassDTO.setStatus(3);//已超时
            }else{

            }

            //异常判断
            if (flag) {//课程结束或异常
                hourClassDTO.setStatus(0);//已结束
            }
            if(hourClassDTO.getStatus()==2){
                dtos.add(hourClassDTO);
                oids.add(hourClassEntry.getID());
                price += hourClassDTO.getClassNewPrice();
                sb.append("第"+hourClassDTO.getOrder()+"课时 ");
            }
        }
        if(dtos.size()==0){
            throw new Exception("没有课节可以退款！");
        }
        sb.append("，共退款"+price);
        //订单置为退款中
        classOrderDao.updateEntry(oids, sonId);
        CacheHandler.cache(cacheKey, String.valueOf(System.currentTimeMillis()), Constant.SECONDS_IN_DAY);//24小时内

        //修改课程人数
        Set<ObjectId> set = classOrderDao.getUserIdEntry(excellentCoursesEntry.getID());
        excellentCoursesEntry.setStudentNumber(set.size());
        excellentCoursesDao.addEntry(excellentCoursesEntry);
       /* //生成申请   todo
        RefundEntry refundEntry = new RefundEntry(userId,id,oids);
        refundDao.addEntry(refundEntry);*/
        ObjectId contactId  = new ObjectId();
        addLog(userId,contactId,"用户:"+userEntry.getGenerateUserCode()+"开始进行退课操作");
        logger.info(userId.toString() + "-" + contactId.toString() + "用户:"+userEntry.getGenerateUserCode()+"开始进行退课操作");
        //美豆账户
        UserBehaviorEntry userBehaviorEntry = userBehaviorDao.getEntry(userId);
        if(userBehaviorEntry==null){
            List<ObjectId>  ot = new ArrayList<ObjectId>();
            UserBehaviorEntry userBehaviorEntry1 = new UserBehaviorEntry(userId,0,ot,ot,ot);
            userBehaviorDao.addEntry(userBehaviorEntry1);
            userBehaviorEntry= userBehaviorEntry1;
        }
        addLog(userId,contactId,"用户:"+userEntry.getGenerateUserCode()+"美豆账户余额："+userBehaviorEntry.getAccount());
        logger.info(userId.toString() + "-" + contactId.toString() + "用户:"+userEntry.getGenerateUserCode()+"美豆账户余额："+userBehaviorEntry.getAccount());
        //充值账户
        AccountFrashEntry accountFrashEntry = accountFrashDao.getEntry(userId);
        if(accountFrashEntry==null){
            AccountFrashEntry accountFrashEntry1 = new AccountFrashEntry(userId,0,Constant.ZERO,Constant.ZERO);
            accountFrashDao.addEntry(accountFrashEntry1);
            accountFrashEntry = accountFrashEntry1;
        }
        addLog(userId,contactId,"用户:"+userEntry.getGenerateUserCode()+"充值账户余额："+accountFrashEntry.getAccount());
        logger.info(userId.toString() + "-" + contactId.toString() + "用户:"+userEntry.getGenerateUserCode()+"充值账户余额："+accountFrashEntry.getAccount());
        //直接退款
        double newPr = sum(userBehaviorEntry.getAccount(),price);
        //修改美豆账户余额
        userBehaviorDao.updateEntry(userBehaviorEntry.getID(), newPr);
        addLog(userId,contactId,"修改美豆账户成功！余额:"+newPr);
        logger.info(userId.toString()+"-"+contactId.toString()+"修改美豆账户成功！余额:"+newPr);

        //添加美豆账户消费记录
        RechargeResultEntry rechargeResultEntry = new RechargeResultEntry(userId,userId,sb.toString(),Constant.ZERO,Constant.THREE,price,userId,excellentCoursesEntry.getID(),oids);
        rechargeResultDao.saveEntry(rechargeResultEntry);
        addLog(userId,contactId,"添加美豆账户获得记录成功！本次增加："+price);
        logger.info(userId.toString()+"-"+contactId.toString()+"添加美豆账户获得记录成功！本次增加："+price);

        //修改充值账户余额
        double newPrice = sum(accountFrashEntry.getAccount(),price);
        accountFrashDao.updateEntry(accountFrashEntry.getID(),newPrice);
        addLog(userId,contactId,"修改充值账户成功！余额:"+newPrice);
        logger.info(userId.toString()+"-"+contactId.toString()+"修改充值账户成功！余额:"+newPrice);

        //添加充值账户记录
        AccountOrderEntry accountOrderEntry = new AccountOrderEntry(contactId,userId,"","","",price,"",Constant.ZERO,"",0l,"");
        accountOrderDao.addEntry(accountOrderEntry);
        addLog(userId,contactId,"添加充值账户记录成功！余额:"+newPrice);
        logger.info(userId.toString()+"-"+contactId.toString()+"添加充值账户记录成功！余额:"+newPrice);
    }



    /************************************充值相关END*****************************************/
    public Map<String,Object> accountList(int page,int pageSize,ObjectId userId){
        Map<String,Object> map = new HashMap<String, Object>();
        List<RechargeResultDTO> dtos = new ArrayList<RechargeResultDTO>();
        NewVersionBindRelationEntry newVersionBindRelationEntry = newVersionBindRelationDao.getBindEntry(userId);
        if(newVersionBindRelationEntry==null){
            map.put("count",0);
            map.put("list",dtos);
            return map;
        }
       /* List<ObjectId> ids = new ArrayList<ObjectId>();
        ids.add(userId);
        ids.add(newVersionBindRelationEntry.getMainUserId());*/
        ObjectId parentId  = newVersionBindRelationEntry.getMainUserId();
      /*  List<RechargeResultEntry> entries = rechargeResultDao.getAllEntryList(ids,userId, page, pageSize);
        int count = rechargeResultDao.selectMyCount(ids,userId);*/
        List<RechargeResultEntry> entries = rechargeResultDao.getMyAllEntryList(parentId, page, pageSize);
        int count = rechargeResultDao.selectCount(parentId);
        for(RechargeResultEntry entry:entries){
            RechargeResultDTO dto = new RechargeResultDTO(entry);
            dtos.add(dto);
        }
        map.put("list",dtos);
        map.put("count",count);
        return map;
    }
    public static void main(String[] args){
        double a = 3.33;
        double b = 1.11;
        double c = sub(a,b);
        System.out.print(c);
    }

    public Map<String,Object> accountMyList(int page,int pageSize,ObjectId userId){
        Map<String,Object> map = new HashMap<String, Object>();
        List<RechargeResultDTO> dtos = new ArrayList<RechargeResultDTO>();
        List<RechargeResultEntry> entries = rechargeResultDao.getMyAllEntryList(userId, page, pageSize);
        int count = rechargeResultDao.selectCount(userId);
        for(RechargeResultEntry entry:entries){
            RechargeResultDTO dto = new RechargeResultDTO(entry);
            dtos.add(dto);
        }
        map.put("list",dtos);
        map.put("count",count);
        map.put("page", page);
        map.put("pageSize", pageSize);
        return map;
    }

    //重新开课
    public String  newOpen(ObjectId userId,ObjectId id){
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        long time = System.currentTimeMillis();
        String str = "";
        if(excellentCoursesEntry!=null){
            List<HourClassEntry> entryList = hourClassDao.getEntryList(excellentCoursesEntry.getID());
            //copy
            excellentCoursesEntry.setID(null);
            //重新开课
            excellentCoursesEntry.setStatus(0);
            excellentCoursesEntry.setStudentNumber(0);
            excellentCoursesEntry.setNewPrice(0.00);
            excellentCoursesEntry.setCreateTime(time);
            str = excellentCoursesDao.addEntry(excellentCoursesEntry);
            for(HourClassEntry hourClassEntry :entryList){
                hourClassEntry.setParentId(excellentCoursesEntry.getID());
                hourClassEntry.setID(null);
                hourClassEntry.setCreateTime(time);
                hourClassEntry.setClassNewPrice(0.00);
                hourClassEntry.setType(0);
            }
            this.addEntryBatch(entryList);
        }
        return str;
    }

    public void updateAccount(String accountName,ObjectId userId){
        userAccountDao.deleteEntry(userId);
        UserAccountEntry userAccountEntry = new UserAccountEntry();
        userAccountEntry.setAccountName(accountName);
        userAccountEntry.setUserId(userId);
        userAccountEntry.setRsaPrivateUserId("");
        userAccountEntry.setType(Constant.ONE);
        userAccountEntry.setIsRemove(Constant.ZERO);
       userAccountDao.addEntry(userAccountEntry);
    }


    public String getApppliAccount(ObjectId userId){
        UserAccountEntry userAccountEntry = userAccountDao.getEntry(userId);
        if(userAccountEntry!=null){
            if(userAccountEntry.getAccountName()!=null){
                return userAccountEntry.getAccountName();
            }
        }
        return "";
    }


    /**
     * 获得授权列表
     */
    public List<Map<String,Object>>  getMyRoleToSon(ObjectId userId){
        List<Map<String,Object>> mapList = new ArrayList<Map<String, Object>>();
        UserBehaviorEntry userBehaviorEntry = userBehaviorDao.getEntry(userId);
        //查询所有该用户的绑定关系
        List<ObjectId> childIds = newVersionBindRelationDao.getIdsByMainUserId(userId);
        List<ObjectId> sonIds = new ArrayList<ObjectId>();
        if(userBehaviorEntry!=null &&  userBehaviorEntry.getSonOpenList()!=null){
            sonIds = userBehaviorEntry.getSonOpenList();
        }
        if(childIds.size()==0){
            return mapList;
        }
        List<UserEntry> userEntries = userDao.getUserEntryList(childIds, Constant.FIELDS);
        for(UserEntry userEntry:userEntries){
            Map<String,Object> map = new HashMap<String, Object>();
            String name = StringUtils.isBlank(userEntry.getNickName())?userEntry.getUserName():userEntry.getNickName();
            map.put("userName",name);
            map.put("userId",userEntry.getID().toString());
            if(sonIds.contains(userEntry.getID())){
                map.put("isCheck",0);
            }else{
                map.put("isCheck",1);
            }
           mapList.add(map);
        }
        return mapList;
    }

    public void updateMyRoleToSon(ObjectId userId,String sonId,int status){
        UserBehaviorEntry userBehaviorEntry = userBehaviorDao.getEntry(userId);
        if(sonId!=null&& !sonId.equals("")){
            ObjectId id = new ObjectId(sonId);
            if(userBehaviorEntry!=null){
                List<ObjectId>  sonIds = userBehaviorEntry.getSonOpenList();
                if(status==1){
                    sonIds.remove(id);
                    userBehaviorEntry.setSonOpenList(sonIds);
                }else if(status==0){
                   if(!sonIds.contains(id)){
                       sonIds.add(id);
                       userBehaviorEntry.setSonOpenList(sonIds);
                   }
                }
                userBehaviorDao.addEntry(userBehaviorEntry);
            }else{
                if(status==0){
                    List<ObjectId> objectIdList = new ArrayList<ObjectId>();
                    List<ObjectId> list = new ArrayList<ObjectId>();
                    list.add(id);
                    UserBehaviorEntry userBehaviorEntry1 = new UserBehaviorEntry(userId,0,objectIdList,objectIdList,list);
                    userBehaviorDao.addEntry(userBehaviorEntry1);
                }
            }
        }

    }

    /**
     * 查询所有提现记录
     * @param jiaId
     * @param type
     * @param page
     * @param pageSize
     * @return
     */
    public Map<String,Object> selectAllUserMoney(String jiaId,int type,int page,int pageSize ){
        Map<String,Object> map = new HashMap<String, Object>();
        List<ExtractCashEntry> extractCashEntries = extractCashDao.getAllMemberBySchoolId(type, jiaId, page, pageSize);
        int count = extractCashDao.countAllMemberBySchoolId(type,jiaId);
        List<Map<String,Object>> mapList = new ArrayList<Map<String, Object>>();
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        for(ExtractCashEntry entry:extractCashEntries){
            objectIdList.add(entry.getUserId());
        }
        Map<ObjectId, UserEntry> mainUserEntryMap = userDao.getUserEntryMap(objectIdList, Constant.FIELDS);
        for(ExtractCashEntry entry:extractCashEntries){
            Map<String,Object> objectMap = new HashMap<String, Object>();
            objectMap.put("userId",entry.getUserId().toString());
            objectMap.put("id",entry.getID().toString());
            objectMap.put("money",entry.getCash());
            objectMap.put("account",entry.getAccount());
            objectMap.put("accountType",entry.getAccountType());
            if(entry.getCreateTime()!=0l){
                objectMap.put("time",DateTimeUtils.getLongToStrTimeTwo(entry.getCreateTime()));
            }else{
                objectMap.put("time","未知");
            }
            objectMap.put("jiaId",entry.getJiaId());
            objectMap.put("type",entry.getType());
            if(entry.getDateTime()!=0l){
                objectMap.put("dateTime",DateTimeUtils.getLongToStrTimeTwo(entry.getDateTime()));
            }else{
                objectMap.put("dateTime","未处理");
            }
            UserEntry userEntry = mainUserEntryMap.get(entry.getUserId());
            if(userEntry!=null){
                String name = StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
                objectMap.put("userName",name);
                objectMap.put("avatar",AvatarUtils.getAvatar(userEntry.getAvatar(), userEntry.getRole(), userEntry.getSex()));
                mapList.add(objectMap);
            }
        }
        map.put("list",mapList);
        map.put("count",count);
        return map;
    }

    /**
     * 通过提现
     */
    public void agreeUserMoney(ObjectId id,ObjectId uuid)throws Exception{
        ExtractCashEntry extractCashEntry = extractCashDao.getEntry(id);
        if(extractCashEntry==null || extractCashEntry.getType()!=1){
            return;
        }
        ObjectId userId = extractCashEntry.getUserId();
        double price = extractCashEntry.getCash();
        ObjectId contactId = extractCashEntry.getID();
        //查询用户充值账户
        AccountFrashEntry accountFrashEntry = accountFrashDao.getEntry(userId);
        if(accountFrashEntry == null || price>accountFrashEntry.getAccount()){
            throw new Exception("用户账户无可用余额!");
        }
        extractCashEntry.setType(Constant.TWO);
        extractCashEntry.setDateTime(System.currentTimeMillis());
        extractCashDao.addEntry(extractCashEntry);
        //修改充值账户余额
        double newPrice = sub(accountFrashEntry.getAccount(),price);
        accountFrashDao.updateEntry(accountFrashEntry.getID(),newPrice);
        addLog(userId,contactId,"提现充值账户成功！余额:"+newPrice);
        logger.info(userId.toString()+"-"+contactId.toString()+"提现美豆账户成功！余额:"+newPrice);

        //添加充值账户记录
        AccountOrderEntry accountOrderEntry = new AccountOrderEntry(contactId,userId,"","","",(int)price,"",Constant.ZERO,"",0l,"");
        accountOrderDao.addEntry(accountOrderEntry);
        addLog(userId,contactId,"添加充值账户记录成功！余额:"+newPrice);
        logger.info(userId.toString()+"-"+contactId.toString()+"添加充值账户记录成功！余额:"+newPrice);
        this.addLogMessage(id.toString(),"通过了用户："+userId.toString()+"的提现申请", LogMessageType.coursesRole.getDes(),uuid.toString());
    }

    /**
     * 拒绝提现
     */
    public void deleteUserMoney(ObjectId id,ObjectId uuid)throws Exception{
        ExtractCashEntry extractCashEntry = extractCashDao.getEntry(id);
        if(extractCashEntry==null || extractCashEntry.getType()!=1){
            return;
        }
        ObjectId userId = extractCashEntry.getUserId();
        int price = (int)extractCashEntry.getCash();
        ObjectId contactId = extractCashEntry.getID();
        //美豆账户
        UserBehaviorEntry userBehaviorEntry = userBehaviorDao.getEntry(userId);
        if(userBehaviorEntry==null){
            throw new Exception("该用户并无美豆账户！");
        }
        extractCashEntry.setType(Constant.THREE);
        extractCashEntry.setDateTime(System.currentTimeMillis());
        extractCashDao.addEntry(extractCashEntry);
        double newPr = sum(userBehaviorEntry.getAccount(),price);
        //修改美豆账户余额
        userBehaviorDao.updateEntry(userBehaviorEntry.getID(), newPr);
        addLog(userId,contactId,"提现拒绝：修改美豆账户成功！余额:"+newPr);
        logger.info(userId.toString()+"-"+contactId.toString()+"修改美豆账户成功！余额:"+newPr);

        //添加美豆账户消费记录
        RechargeResultEntry rechargeResultEntry = new RechargeResultEntry(userId,userId,"用户提现驳回",Constant.ZERO,Constant.FOUR,price,userId,null,new ArrayList<ObjectId>());
        rechargeResultDao.saveEntry(rechargeResultEntry);
        addLog(userId,contactId,"提现拒绝：添加美豆账户消费记录成功！本次新增："+price);
        logger.info(userId.toString()+"-"+contactId.toString()+"添加美豆账户消费记录成功！本次新增："+price);
        this.addLogMessage(id.toString(),"拒绝了用户："+userId.toString()+"的提现申请", LogMessageType.coursesRole.getDes(),uuid.toString());

    }

    public Map<String,Object> selectUserList(ObjectId userId,String contactId,int page,int pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        List<AccountLogEntry> logEntries = accountLogDao.getAllMemberBySchoolId(userId,contactId,page,pageSize);
        int count = accountLogDao.countAllMemberBySchoolId(userId,contactId);
        UserEntry userEntry = userDao.findByUserId(userId);
        if(userEntry==null){
            return map;
        }
        List<Map<String,Object>> mapList = new ArrayList<Map<String, Object>>();
        for(AccountLogEntry accountLogEntry:logEntries){
            Map<String,Object> map1 = new HashMap<String, Object>();
            String name = StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
            map1.put("userName",name);
            if(accountLogEntry.getCreateTime()!=0l){
                map1.put("time",DateTimeUtils.getLongToStrTimeTwo(accountLogEntry.getCreateTime()));
            }else{
                map1.put("time","未知");
            }
            map1.put("description",accountLogEntry.getDescription());
            map1.put("status",accountLogEntry.getStatus());
            map1.put("contact",accountLogEntry.getContactId().toString());
            mapList.add(map1);
        }
        map.put("list",mapList);
        map.put("count",count);
        return map;
    }

    public void addLogMessage(String contactId,String content,String function,String userId){
        LogMessageDTO dto = new LogMessageDTO();
        dto.setType(1);
        dto.setContactId(contactId);
        dto.setContent(content);
        dto.setFunction(function);
        dto.setUserId(userId);
        logMessageDao.addEntry(dto.buildAddEntry());
    }


    public  void setLun(ObjectId id,ObjectId userId,int status){
        excellentCoursesDao.setLunEntry(id,status);
    }

    public Map<String,Object> getLunList(int page,int pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        long current = System.currentTimeMillis();
        List<ExcellentCoursesEntry> entries = excellentCoursesDao.getLunList(page,pageSize,current);
        int count = excellentCoursesDao.countLunList(current);
        List<ExcellentCoursesDTO> dtos = new ArrayList<ExcellentCoursesDTO>();
        for(ExcellentCoursesEntry entry:entries){
            ExcellentCoursesDTO dto = new ExcellentCoursesDTO(entry);
            dtos.add(dto);
        }
        map.put("list",dtos);
        map.put("count",count);
        return map;
    }

    public Map<String,Object> getOldLunList(int page,int pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        long current = System.currentTimeMillis();
        List<ExcellentCoursesEntry> entries = excellentCoursesDao.getOldLunList(page,pageSize);
        int count = excellentCoursesDao.countOldLunList();
        List<ExcellentCoursesDTO> dtos = new ArrayList<ExcellentCoursesDTO>();
        for(ExcellentCoursesEntry entry:entries){
            ExcellentCoursesDTO dto = new ExcellentCoursesDTO(entry);
            dtos.add(dto);
        }
        map.put("list",dtos);
        map.put("count",count);
        return map;
    }


    public List<ReplayDTO>  getBackList(ObjectId id,ObjectId userId){
        List<ReplayDTO> dtos = new ArrayList<ReplayDTO>();
        HourClassEntry hourClassEntry = hourClassDao.getEntry(id);
        ObjectId ownId = hourClassEntry.getOwnId();
        if(ownId==null){
            ownId  =  userId;
        }
        UserEntry userEntry = userDao.findByUserId(ownId);
        if(hourClassEntry!=null && userEntry!=null){//课节存在
            ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(hourClassEntry.getParentId());
            //课程存在
            ClassOrderEntry classOrderEntry = classOrderDao.getEntry(hourClassEntry.getID(),hourClassEntry.getParentId(),userId);
            if(excellentCoursesEntry !=null && classOrderEntry!=null){//订单存在
                String name = StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
                String teacherName = excellentCoursesEntry.getUserName();
                if(hourClassEntry.getOwnName()!=null){
                    teacherName = hourClassEntry.getOwnName();
                }
                long time = hourClassEntry.getStartTime()+hourClassEntry.getCurrentTime();
                dtos = coursesRoomService.getBackList(excellentCoursesEntry.getID(),name,teacherName,hourClassEntry.getStartTime(),time,userId);
            }
        }
        return dtos;
    }

    /**
     * 获得用户的所有具有管理员权限的社区id
     *
     */
    public List<ObjectId> getMyRoleList(ObjectId userId){
        List<ObjectId> olsit = memberDao.getGroupIdsList(userId);
        List<ObjectId> clist = new ArrayList<ObjectId>();
        List<ObjectId> mlist =   groupDao.getGroupIdsList(olsit);
        return mlist;
    }

    public List<CommunityDTO> getCommunityList(ObjectId id,ObjectId userId){
        ExcellentCoursesEntry excellentCoursesEntry =  excellentCoursesDao.getEntry(id);
        if(excellentCoursesEntry==null){
            return new ArrayList<CommunityDTO>();
        }
        List<ObjectId> communityIdList = excellentCoursesEntry.getCommunityIdList();
        List<ObjectId> objectIdList3 = getMyRoleList(excellentCoursesEntry.getUserId());
        List<CommunityDTO> objectIdList4 = communityService.getCommunitys2(excellentCoursesEntry.getUserId(), 1, 200);
        List<CommunityDTO> objectIdList = new ArrayList<CommunityDTO>();
        for(CommunityDTO communityDTO:objectIdList4){
            if(objectIdList3.contains(new ObjectId(communityDTO.getId()))){
                if(communityIdList.contains(new ObjectId(communityDTO.getId()))){
                    communityDTO.setJoin(true);
                }else{
                    communityDTO.setJoin(false);
                }
                objectIdList.add(communityDTO);
            }
        }
        List<CommunityDTO> objectIdList2 = new ArrayList<CommunityDTO>();
        for(CommunityDTO communityDTO : objectIdList){
            if(!communityDTO.getName().equals("复兰社区") &&  !communityDTO.getName().equals("复兰大学")){
                objectIdList2.add(communityDTO);
            }
        }
        return objectIdList2;
    }

    public String updateCommunityList(ObjectId id,String communityIds){
        ExcellentCoursesEntry excellentCoursesEntry =  excellentCoursesDao.getEntry(id);
        if(excellentCoursesEntry==null){
           return "课程不存在";
        }
        String[] str = communityIds.split(",");
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        for(String string:str){
            if(ObjectId.isValid(string)){
                objectIdList.add(new ObjectId(string));
            }
        }
        if(excellentCoursesEntry.getOpen()==0 && objectIdList.size()==0){
            return "不公开的课程至少需要一个推荐社群";
        }
        excellentCoursesEntry.setCommunityIdList(objectIdList);
        excellentCoursesDao.addEntry(excellentCoursesEntry);
        return "调整成功";
    }

    public void  selectUserClassDesc(ObjectId id,ObjectId userId){
        ExcellentCoursesEntry excellentCoursesEntry =  excellentCoursesDao.getEntry(id);
        CoursesRoomEntry coursesRoomEntry = coursesRoomDao.getEntry(id);
        if(excellentCoursesEntry==null){
            return;
        }
        if(coursesRoomEntry==null){
            return;
        }
        /*String roomId,ObjectId contactId,long startTime,long endTime*/
        List<HourClassEntry> hourClassEntries = hourClassDao.getEntryList(id);
        List<ObjectId> classOrderEntries = classOrderDao.getEntryIdList(userId, id);
        //1 .找出已结束课节
        List<ObjectId>  endIdList = new ArrayList<ObjectId>();
        List<ObjectId>  allList = new ArrayList<ObjectId>();
        long nowTime = System.currentTimeMillis();
        Map<ObjectId,HourClassEntry> hourClassEntryMap = new HashMap<ObjectId, HourClassEntry>();
        for(HourClassEntry hourClassEntry:hourClassEntries){
            long endTime = hourClassEntry.getStartTime() + hourClassEntry.getCurrentTime();
            if(nowTime>endTime){//已结束
                endIdList.add(hourClassEntry.getID());
            }
            allList.add(hourClassEntry.getID());
            hourClassEntryMap.put(hourClassEntry.getID(), hourClassEntry);
        }
        //2 .判断课节是否已产生记录
        if(endIdList.size()>0){
            Set<ObjectId> se = userClassDescDao.getEntryIdList(endIdList, id);
            //去除已存在记录
            allList.removeAll(se);
            //String roomId,ObjectId contactId,long startTime,long endTime
            List<UserCCLoinEntry> userCCLoinEntries = new ArrayList<UserCCLoinEntry>();
            for(ObjectId oid:allList){
                HourClassEntry hourClassEntry = hourClassEntryMap.get(oid);
                if(hourClassEntry!=null){
                    userCCLoinEntries.addAll(coursesRoomService.getAllUserList(coursesRoomEntry.getRoomId(), id, hourClassEntry.getStartTime(), hourClassEntry.getStartTime() + hourClassEntry.getCurrentTime()));
                }
            }
            this.addEntryBatch2(userCCLoinEntries);
        }

        List<UserClassDescEntry> entries = new ArrayList<UserClassDescEntry>();
        if(allList.size()>0){//无记录课节，生成记录
            List<UserCCLoinEntry> userCCLoinEntries = userCCLoinDao.getAllEntryIdList(userId, id);
            for(ObjectId oid:allList){
                HourClassEntry hourClassEntry = hourClassEntryMap.get(oid);
                int number = 0;
                int currentTime = 0;
                long st = hourClassEntry.getStartTime();
                long et = hourClassEntry.getStartTime()+hourClassEntry.getCurrentTime();
                for(UserCCLoinEntry ccLoinEntry:userCCLoinEntries){
                    long s = 0;
                    if(ccLoinEntry.getEnterTime()!=null){
                        s = DateTimeUtils.getStrToLongTime(ccLoinEntry.getEnterTime(), "yyyy-MM-dd HH:mm");
                    }
                    long e = 0;
                    if(ccLoinEntry.getLeaveTime()!=null){
                        e = DateTimeUtils.getStrToLongTime(ccLoinEntry.getLeaveTime(), "yyyy-MM-dd HH:mm");
                    }
                    if(checkTime(st,et,s,e)){
                        number++;
                        currentTime += e-s;
                    }
                }
                UserClassDescEntry userClassDescEntry = new UserClassDescEntry(userId,id,hourClassEntry.getID(),hourClassEntry.getCurrentTime(),number,currentTime,st,et,Constant.ONE);
                entries.add(userClassDescEntry);
            }
        }
        this.addEntryBatch3(entries);



        //3 .未产生记录的循环查询生成记录（并返回稍后信息）

        //

    }

    public  Map<String,Object>  selectSimpleList(ObjectId id,ObjectId userId) throws  Exception{
        Map<String,Object> map = new HashMap<String, Object>();
        ExcellentCoursesEntry excellentCoursesEntry =  excellentCoursesDao.getEntry(id);
        CoursesRoomEntry coursesRoomEntry = coursesRoomDao.getEntry(id);
        if(excellentCoursesEntry==null){
            throw  new Exception("课程不存在！");
        }
        if(coursesRoomEntry==null){
            throw  new Exception("直播间不存在！");
        }
        List<HourClassEntry> hourClassEntries = hourClassDao.getEntryList(id);
        Map<ObjectId,ClassOrderEntry> entryMap = classOrderDao.getEntryList(userId, id);
        long nowTime = System.currentTimeMillis();
        UserEntry userEntry = userDao.findByUserId(userId);
        if(userEntry==null){
            throw  new Exception("用户不存在！");
        }
        String name = StringUtils.isNotBlank(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
        map.put("name",name);
        List<UserClassDescDTO>  userClassDescDTOs = new ArrayList<UserClassDescDTO>();
        for(HourClassEntry hourClassEntry:hourClassEntries){
            HourClassDTO hourClassDTO = new HourClassDTO(hourClassEntry);
            long endTime = hourClassEntry.getStartTime() + hourClassEntry.getCurrentTime();
            UserClassDescDTO userClassDescDTO = new UserClassDescDTO();
            userClassDescDTO.setEndTime("");
            ClassOrderEntry classOrderEntry = entryMap.get(hourClassEntry.getID());
            if(classOrderEntry==null){
                userClassDescDTO.setStatus(0);//未购买
            }else{
                if(classOrderEntry.getType()==0){
                    userClassDescDTO.setStatus(1);//已进入
                }else{
                    userClassDescDTO.setStatus(2);//未进入
                }
            }

            userClassDescDTO.setStartTime(hourClassDTO.getStartTime());
            userClassDescDTO.setAllTime(hourClassEntry.getCurrentTime() / 60000);
            userClassDescDTO.setContactId(hourClassDTO.getId());
            userClassDescDTO.setCurrentTime(hourClassEntry.getCurrentTime() / 60000);
            userClassDescDTO.setNumber(0);
            userClassDescDTO.setOrder(hourClassEntry.getOrder());
            userClassDescDTO.setUserName(name);
            userClassDescDTOs.add(userClassDescDTO);
        }

        map.put("list",userClassDescDTOs);
        return map;
    }

    public boolean checkTime(long stm,long etm,long stm2,long etm2){
        if(stm2>stm && etm2<etm ){//1
            return true;
        }

        if(etm2 > stm && etm2 < etm ){//2
            return true;
        }

        if(stm2 < stm && etm2 > etm){//3
            return true;
        }

        if(stm2 >stm && stm2 < etm){//4
            return true;
        }
        if(stm==stm2 || stm== etm2 || etm == stm2 || etm ==etm2){
            return true;
        }
        return false;
    }

    /**
     * 批量增加红点记录
     * @param list
     */
    public void addEntryBatch2(List<UserCCLoinEntry> list) {
        List<DBObject> dbList = new ArrayList<DBObject>();
        for (int i = 0; list != null && i < list.size(); i++) {
            dbList.add(list.get(i).getBaseEntry());
        }
        //导入新纪录
        if(dbList.size()>0) {
            userCCLoinDao.addBatch(dbList);
        }
    }

    public void addEntryBatch3(List<UserClassDescEntry> list) {
        List<DBObject> dbList = new ArrayList<DBObject>();
        for (int i = 0; list != null && i < list.size(); i++) {
            dbList.add(list.get(i).getBaseEntry());
        }
        //导入新纪录
        if(dbList.size()>0) {
            userClassDescDao.addBatch(dbList);
        }
    }


    //cc 获取直播间状态
    public int getRoomStatus(String roomId){
        int status = coursesRoomService.getRoomStatus(roomId);
        return status;
    }


    public double getTwoDouble(double d){
        BigDecimal b = new BigDecimal(d);
        d = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return d;
    }

    //删除课节
    public String deleteClass(ObjectId classId,ObjectId userId){
        HourClassEntry hourClassEntry = hourClassDao.getEntry(classId);
        if(hourClassEntry==null){
            return "课节不存在";
        }
        ExcellentCoursesEntry excellentCoursesEntry =  excellentCoursesDao.getEntry(hourClassEntry.getParentId());
        if(excellentCoursesEntry==null){
            return "课程不存在";
        }
        //删除课节
        hourClassDao.delOneEntry(classId);
        //删除未进入提示
        pushMessageDao.delOneEntry(classId);
        this.addLogMessage(classId.toString(),"删除课程："+excellentCoursesEntry.getTitle()+"，第"+hourClassEntry.getOrder()+"课节：",LogMessageType.courses.getDes(),userId.toString());
        //重新组装
        List<HourClassEntry> hourClassEntries = hourClassDao.getEntryList(excellentCoursesEntry.getID());
        double newPrice = 0.00;
        long st = 0l;
        long et = 0l;
        Set<ObjectId> teacherList = new HashSet<ObjectId>();
        int index= 0;
        for(HourClassEntry entry:hourClassEntries){
            index++;
            newPrice = sum(newPrice,entry.getClassNewPrice());
            long st2 = entry.getStartTime();
            long et2 = entry.getStartTime()+entry.getCurrentTime();
            if(st==0l){
                st = st2;
            }else{
                if(st2<st){
                    st = st2;
                }
            }
            if(et==0l){
                et = et2;
            }else{
                if(et <et2){
                    et = et2;
                }
            }

            //修改
            if(entry.getOwnId()!=null){
                teacherList.add(entry.getOwnId());
            }
            //修改排序
            hourClassDao.sortEntry(entry.getID(), index);
        }

        excellentCoursesEntry.setAllClassCount(hourClassEntries.size());
       /* if(excellentCoursesEntry.getCourseType()==1){//打包课价格不变

        }else{
            excellentCoursesEntry.setNewPrice(newPrice);
        }*/
        excellentCoursesEntry.setNewPrice(newPrice);
        excellentCoursesEntry.setStartTime(st);
        excellentCoursesEntry.setEndTime(et);
        //修改
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        objectIdList.addAll(teacherList);
        excellentCoursesEntry.setTeacherIdList(objectIdList);
        excellentCoursesDao.addEntry(excellentCoursesEntry);
        return "删除成功";

    }


    //修改总价
    public void updateAllPrice(ObjectId  id,double price,ObjectId userId) throws Exception{
        if(price<0){
            throw  new Exception("课程定价不能小于0");
        }
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        if(excellentCoursesEntry==null){
            throw new Exception("课程不存在！");
        }
        List<ObjectId> hourClassEntries = hourClassDao.getObjectIdList(id);
        int count = hourClassEntries.size();
        //修改总价和数量
        excellentCoursesEntry.setAllClassCount(count);
        excellentCoursesEntry.setNewPrice(price);
        excellentCoursesDao.addEntry(excellentCoursesEntry);
        double onePrice = getTwoDouble(price / count);
        //修改课节价格
        hourClassDao.updateNewPrice(id,hourClassEntries,onePrice);
        this.addLogMessage(id.toString(),"修改了课程："+excellentCoursesEntry.getTitle()+"的总价",LogMessageType.courses.getDes(),userId.toString());
    }
    //修改课节
    public void updateOneClass(ObjectId id,HourClassDTO dto,ObjectId userId,int type)throws Exception{
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        if(excellentCoursesEntry==null){
            throw new Exception("课程不存在！");
        }
        dto.setType(Constant.ZERO);
        dto.setUserId(excellentCoursesEntry.getUserId().toString());
        dto.setWeek(getWeek(dto.getDateTime()));
        dto.setParentId(excellentCoursesEntry.getID().toString());
        //转换
        if(dto.getOwnId() !=null && dto.getOwnId().equals("无")){
            dto.setOwnId(userId.toString());
        }
        HourClassEntry hourClassEntry = dto.buildUpdateEntry();
        //修改推送消息
        long sTm = hourClassEntry.getStartTime();
        String str = DateTimeUtils.getLongToStrTimeTwo(sTm).substring(0,11);
        long strNum = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
        if(hourClassEntry.getID()!=null){
            hourClassDao.addEntry(hourClassEntry);
            pushMessageDao.updateEntry(hourClassEntry.getID(),sTm,strNum);
        }else{
            hourClassDao.addEntry(hourClassEntry);
            this.sendMessage(excellentCoursesEntry.getTitle(),hourClassEntry.getID(),sTm,strNum);
            //添加订单
            if(type==2){
                addClassTime(hourClassEntry);
            }
        }
        this.addLogMessage(hourClassEntry.getID().toString(),"修改了课程："+excellentCoursesEntry.getTitle()+"的第"+dto.getOrder()+"课节",LogMessageType.courses.getDes(),userId.toString());
        //排序
        sortClassTime(excellentCoursesEntry);
    }

    //增加课节订单
    public void addClassTime(HourClassEntry classEntry){
        ObjectId pid = classEntry.getParentId();
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(pid);
        //用户订单查询
        Map<ObjectId,ClassOrderEntry> mapEntry = classOrderDao.getMapEntry(excellentCoursesEntry.getID());
        Set<ObjectId> userIds =  mapEntry.keySet();
        List<UserEntry> userEntries = userDao.getUserEntryList(userIds, Constant.FIELDS);
        List<ClassOrderEntry> classOrderEntries1 = new ArrayList<ClassOrderEntry>();
        long current = System.currentTimeMillis();
        for(UserEntry u : userEntries){
            ClassOrderEntry oldClassOrderEntry = mapEntry.get(u.getID());
            ClassOrderEntry classOrderEntry = new ClassOrderEntry();
            //购买  1  未购买
            classOrderEntry.setIsBuy(1);
            //下单
            classOrderEntry.setType(1);
            classOrderEntry.setCreateTime(current);
            classOrderEntry.setContactId(excellentCoursesEntry.getID());
            classOrderEntry.setIsRemove(0);
            classOrderEntry.setParentId(classEntry.getID());
            if(oldClassOrderEntry!=null){
                classOrderEntry.setOrderId(oldClassOrderEntry.getOrderId());
                classOrderEntry.setFunction(oldClassOrderEntry.getFunction());
            }else{
                classOrderEntry.setOrderId("系统添加课程附带添加");
                classOrderEntry.setFunction(4);
            }
            classOrderEntry.setPrice(classEntry.getClassNewPrice());
            classOrderEntry.setUserId(u.getID());//孩子的订单
            classOrderEntries1.add(classOrderEntry);
        }
        //添加课节订单
        if(classOrderEntries1.size()>0){
            this.addClassEntryBatch(classOrderEntries1);
        }
    }

    //修改上课时间
    public void updateClassTime(ObjectId userId,ObjectId id,String ids)throws Exception{
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        if(excellentCoursesEntry==null){
            throw new Exception("课程不存在！");
        }
        List<ObjectId>  objectIdList = new ArrayList<ObjectId>();
        String[] strings = ids.split(",");
        Map<ObjectId,String> map = new HashMap<ObjectId, String>();
        for(String s:strings){
            String[] strings1 = s.split("#");
            if(strings1.length==2){
                if(ObjectId.isValid(strings1[0])){
                    objectIdList.add(new ObjectId(strings1[0]));
                    map.put(new ObjectId(strings1[0]),strings1[1]);
                }
            }
        }
        List<HourClassEntry> hourClassEntries = hourClassDao.getEntryList(objectIdList);
        for(HourClassEntry hourClassEntry:hourClassEntries){
            String str = map.get(hourClassEntry.getID());
           if(str!=null){
               long stm = 0l;
               if(!str.equals("")){
                   stm = DateTimeUtils.getStrToLongTime(str);
                   hourClassEntry.setStartTime(stm);
                   hourClassDao.addEntry(hourClassEntry);
               }
           }
        }
        this.addLogMessage(excellentCoursesEntry.getID().toString(),"批量修改了课程："+excellentCoursesEntry.getTitle()+"的上课时间",LogMessageType.courses.getDes(),userId.toString());
        //排序
        sortClassTime(excellentCoursesEntry);
    }

    //修改课节老师和学科
    public void updateClassTeacher(ObjectId userId,ObjectId id,String ids,ObjectId ownId,String ownName,String subjectName)throws Exception{
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        if(excellentCoursesEntry==null){
            throw new Exception("课程不存在！");
        }
        List<ObjectId>  objectIdList = new ArrayList<ObjectId>();
        String[] strings = ids.split(",");
        for(String s:strings){
            if(ObjectId.isValid(s)){
                objectIdList.add(new ObjectId(s));
            }
        }
        List<HourClassEntry> hourClassEntries = hourClassDao.getEntryList(objectIdList);
        for(HourClassEntry hourClassEntry:hourClassEntries){
            hourClassEntry.setOwnId(ownId);
            hourClassEntry.setOwnName(ownName);
            hourClassEntry.setSubjectName(subjectName);
            hourClassDao.addEntry(hourClassEntry);
        }
        this.addLogMessage(excellentCoursesEntry.getID().toString(),"批量修改了课程："+excellentCoursesEntry.getTitle()+"的课节上课老师",LogMessageType.courses.getDes(),userId.toString());
        //排序
        sortClassTime(excellentCoursesEntry);
    }

    /**
     * 生成推送消息
     * @return
     */
    public void sendMessage(String title,ObjectId id,long startTime,long dateTime){
        if(pushMessageDao.isNotHave(id)){
            PushMessageEntry pushMessageEntry = new PushMessageEntry(
                    "复兰课堂",
                    new ArrayList<ObjectId>(),
                    id,
                    "未上课提醒",
                    title,
                    startTime,
                    dateTime,
                    1,
                    0);
            pushMessageDao.addEntry(pushMessageEntry);
        }
    }
    public void sortClassTime(ExcellentCoursesEntry excellentCoursesEntry){
        //重新组装
       // List<HourClassEntry> hourClassEntries = hourClassDao.getEntryList(excellentCoursesEntry.getID());
        List<HourClassEntry> hourClassEntries =  hourClassDao.getEntryListByStartTime(excellentCoursesEntry.getID());
        double newPrice = 0.00;
        long st = 0l;
        long et = 0l;
        Set<ObjectId> teacherList = new HashSet<ObjectId>();
        int index= 0;
        for(HourClassEntry entry:hourClassEntries){
            index++;
            newPrice = sum(newPrice,entry.getClassNewPrice());
            long st2 = entry.getStartTime();
            long et2 = entry.getStartTime()+entry.getCurrentTime();
            if(st==0l){
                st = st2;
            }else{
                if(st2<st){
                    st = st2;
                }
            }
            if(et==0l){
                et = et2;
            }else{
                if(et <et2){
                    et = et2;
                }
            }

            //修改
            if(entry.getOwnId()!=null){
                teacherList.add(entry.getOwnId());
            }
            //修改排序
            hourClassDao.sortEntry(entry.getID(), index);
        }

        excellentCoursesEntry.setAllClassCount(hourClassEntries.size());
       /* if(excellentCoursesEntry.getCourseType()==1){//打包课价格不变

        }else{
            excellentCoursesEntry.setNewPrice(newPrice);
        }*/
        excellentCoursesEntry.setNewPrice(newPrice);
        excellentCoursesEntry.setStartTime(st);
        excellentCoursesEntry.setEndTime(et);
        //修改
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        objectIdList.addAll(teacherList);
        excellentCoursesEntry.setTeacherIdList(objectIdList);
        excellentCoursesDao.addEntry(excellentCoursesEntry);
    }




    /**
     * 两个Double数相除，并保留scale位小数
     * @param v1
     * @param v2
     * @param scale
     * @return Double
     */
    public static Double div(Double v1,Double v2,int scale){
        if(scale<0){
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
