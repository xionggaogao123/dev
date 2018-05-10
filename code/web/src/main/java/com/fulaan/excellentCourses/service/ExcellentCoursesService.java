package com.fulaan.excellentCourses.service;

import com.db.backstage.TeacherApproveDao;
import com.db.excellentCourses.*;
import com.db.user.UserDao;
import com.fulaan.excellentCourses.dto.ExcellentCoursesDTO;
import com.fulaan.excellentCourses.dto.HourClassDTO;
import com.fulaan.excellentCourses.dto.HourResultDTO;
import com.fulaan.newVersionBind.service.NewVersionBindService;
import com.fulaan.pojo.User;
import com.mongodb.DBObject;
import com.pojo.backstage.TeacherApproveEntry;
import com.pojo.excellentCourses.*;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.DateTimeUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Autowired
    private CoursesRoomService coursesRoomService;

    private UserDao userDao = new UserDao();

    private CoursesRoomDao coursesRoomDao = new CoursesRoomDao();

    private TeacherApproveDao teacherApproveDao = new TeacherApproveDao();


    /**
     * 老师开课
     * @param dto
     * @param userId
     * @return
     */
    public String addEntry(ExcellentCoursesDTO dto,ObjectId userId){
        dto.setUserId(userId.toString());
        dto.setCover("http://7xiclj.com1.z0.glb.clouddn.com/5ae4035bbf2e7927f09df7d8.png");
        dto.setStatus(0);
        ExcellentCoursesEntry excellentCoursesEntry = dto.buildAddEntry();
        if(dto.getId()!=null && !dto.getId().equals("")){
            excellentCoursesEntry.setID(new ObjectId(dto.getId()));
        }
        String str = excellentCoursesDao.addEntry(excellentCoursesEntry);
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
        if(excellentCoursesEntry !=null){
            List<HourClassEntry> entryList =new ArrayList<HourClassEntry>();
            List<HourClassDTO> dtoList = dto.getDtos();
            int oldPrice = 0;
            long st = 0l;
            long et = 0l;
            for(HourClassDTO dto1:dtoList){
                dto1.setType(Constant.ZERO);
                dto1.setUserId(userId.toString());
                ///todo        二期改为后台设定
                dto1.setClassNewPrice(dto1.getClassOldPrice());
                dto1.setWeek(getWeek(dto1.getDateTime()));
                dto1.setParentId(dto.getParentId());
                HourClassEntry classEntry =  dto1.buildAddEntry();
                oldPrice = oldPrice+dto1.getClassOldPrice();
                long st2 = classEntry.getStartTime();
                long et2 = classEntry.getStartTime()+classEntry.getCurrentTime();
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
            //todo        二期改为后台设定
            excellentCoursesEntry.setNewPrice(oldPrice);
            excellentCoursesEntry.setStartTime(st);
            excellentCoursesEntry.setEndTime(et);
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
            //todo 暂为通过二期修改为审批 1
            TeacherApproveEntry teacherApproveEntry = teacherApproveDao.getEntry(userId);
            if(teacherApproveEntry!=null && teacherApproveEntry.getType()==2){
                excellentCoursesDao.finishEntry(id,2);
                long start  = excellentCoursesEntry.getStartTime();
                String startTime = "";
                if(start!=0l){
                    startTime = DateTimeUtils.getLongToStrTimeTwo(start);
                }
                //todo
                coursesRoomService.createCourses(excellentCoursesEntry.getTitle(),excellentCoursesEntry.getTarget(),excellentCoursesEntry.getID(),startTime);
            }else{
                excellentCoursesDao.finishEntry(id,1);
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
        //推荐名单
        List<ExcellentCoursesEntry> coursesEntries = excellentCoursesDao.getEntryList(objectIdList);
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
        List<ExcellentCoursesEntry> coursesEntries2 = excellentCoursesDao.getEntryListById(objectIdList1);
        List<ExcellentCoursesDTO> dtos = new ArrayList<ExcellentCoursesDTO>();
        for(ExcellentCoursesEntry excellentCoursesEntry:coursesEntries){//推荐
            if(!objectIdList1.contains(excellentCoursesEntry.getID())){
                ExcellentCoursesDTO dto = new ExcellentCoursesDTO(excellentCoursesEntry);
                dto.setIsBuy(0);
                dtos.add(dto);
            }
        }
        for(ExcellentCoursesEntry excellentCoursesEntry:coursesEntries2){//已购买
            ExcellentCoursesDTO dto = new ExcellentCoursesDTO(excellentCoursesEntry);
            dto.setIsBuy(1);
            dtos.add(dto);
        }
        map.put("list",dtos);
        map.put("count",dtos.size());
        //map.put("dto",getNowEntry(hourClassIds));
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
                Set<ObjectId> objectIds  = new HashSet<ObjectId>();
                objectIds.addAll(objectIdList);
                objectIds.remove(id);//取消
                List<ObjectId>  objectIdList2 = new ArrayList<ObjectId>();
                objectIdList2.addAll(objectIds);
                userBehaviorEntry.setCollectList(objectIdList2);
                userBehaviorDao.addEntry(userBehaviorEntry);
            }else if(type==1){
                List<ObjectId> objectIdList = userBehaviorEntry.getCollectList();
                Set<ObjectId> objectIds  = new HashSet<ObjectId>();
                objectIds.addAll(objectIdList);
                objectIds.add(id);//添加
                List<ObjectId>  objectIdList2 = new ArrayList<ObjectId>();
                objectIdList2.addAll(objectIds);
                userBehaviorEntry.setCollectList(objectIdList2);
                userBehaviorDao.addEntry(userBehaviorEntry);
            }

        }else{
            if(type==0){

            }else if(type ==1){
                List<ObjectId>  objectIdList = new ArrayList<ObjectId>();
                List<ObjectId>  objectIdList2 = new ArrayList<ObjectId>();
                objectIdList2.add(id);
                UserBehaviorEntry userBehaviorEntry1 = new UserBehaviorEntry(userId,0,objectIdList,objectIdList2);
                userBehaviorDao.addEntry(userBehaviorEntry1);
            }
        }

    }

    public  Map<String,Object>  getSimpleCourses(ObjectId userId){
        List<ObjectId> hourClassIds =  classOrderDao.getObjectIdEntry(userId);
        Map<String,Object> dto = this.getNowEntry(hourClassIds);
        return dto;
    }

    public Map<String,Object> getNowEntry(List<ObjectId> hourClassIds){
        //用户订单查询
        List<HourClassEntry> hourClassEntries = hourClassDao.getEntryList(hourClassIds);
        Map<String,Object> map = new HashMap<String, Object>();
        HourClassEntry entry = null;
        long current = System.currentTimeMillis();
        for(HourClassEntry hourClassEntry:hourClassEntries){
            long startTime =  hourClassEntry.getStartTime()-5*30*1000;
            long endTime = hourClassEntry.getStartTime() + hourClassEntry.getCurrentTime();
            if(entry ==null){
                if(endTime>current){
                    entry = hourClassEntry;
                }
            }else{
                if(endTime>current && startTime <entry.getStartTime()){
                    entry = hourClassEntry;
                }
            }
        }
        if(entry != null && entry.getStartTime()<current){

            map.put("start",1);//正在上课
            map.put("time",0);
        }else{
            if(entry==null){
                map.put("start",0);//未上课
                map.put("time",0);
            }else{
                map.put("start",0);//未上课
                map.put("time",entry.getStartTime());
            }

        }
        map.put("dto",new HourClassDTO(entry));
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
            UserBehaviorEntry userBehaviorEntry1 = new UserBehaviorEntry(userId,0,objectIdList,objectIdList2);
            userBehaviorDao.addEntry(userBehaviorEntry1);
        }
        return dto;
    }

    public Map<String,Object> getCoursesDesc(ObjectId id,ObjectId userId){
        Map<String,Object> map = new HashMap<String, Object>();
        //课程相关
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        //ExcellentCoursesDTO dto = new ExcellentCoursesDTO(excellentCoursesEntry);
        //用户行为
        //UserBehaviorEntry userBehaviorEntry = userBehaviorDao.getEntry(userId);
        //此课程已购买项目
        List<ObjectId> cids= classOrderDao.getEntryIdList(userId,id);
        if(cids.size()==0){
            //dto.setIsBuy(0);  //未购买
            map.put("isBuy",0);
        }else{
            //dto.setIsBuy(1);  //已购买
            map.put("isBuy",1);
        }
        //是否购买
        /*if(cids.size()==0){
            dto.setIsBuy(0);  //未购买
        }else{
            dto.setIsBuy(1);  //已购买
        }*/
        //是否收藏   （行为统计）
        /*if(userBehaviorEntry!=null){
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
            UserBehaviorEntry userBehaviorEntry1 = new UserBehaviorEntry(userId,objectIdList,objectIdList2);
            userBehaviorDao.addEntry(userBehaviorEntry1);
        }*/
        //map.put("dto",dto);
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
            }else{
                if(cids.contains(hourClassEntry.getID())){
                    hourClassDTO.setStatus(2);//已购买
                    if(hourClassEntry.getStartTime()<=current+5*60*1000 && endTime >current ){
                        nowHourClassDTO = hourClassDTO;
                    }
                }else{
                    falge2=true;//还可以购买
                }
            }
            if(flage){//课程结束
                hourClassDTO.setStatus(0);//已结束
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
            dtos.add(nowHourClassDTO);
        }
        map.put("now",dtos);
        map.put("list",hourClassDTOs);
        return map;
    }

    public String buyClassList(ObjectId id,ObjectId userId,String classIds){
        //创建订单
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
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
        if(excellentCoursesEntry!=null && classEntries.size()>0){
            for(HourClassEntry hourClassEntry: classEntries){
                if(!classOrderEntries.contains(hourClassEntry.getID())){
                    ClassOrderEntry classOrderEntry = new ClassOrderEntry();
                    classOrderEntry.setIsBuy(1);
                    //下单
                    classOrderEntry.setType(1);
                    classOrderEntry.setCreateTime(current);
                    classOrderEntry.setContactId(id);
                    classOrderEntry.setIsRemove(0);
                    classOrderEntry.setParentId(hourClassEntry.getID());
                    classOrderEntry.setFunction(1);
                    classOrderEntry.setPrice(hourClassEntry.getClassNewPrice());
                    classOrderEntry.setUserId(userId);
                    classOrderEntries1.add(classOrderEntry);
                }
            }
            this.addClassEntryBatch(classOrderEntries1);
            if(classOrderEntries.size()==0){
                excellentCoursesEntry.setStudentNumber(excellentCoursesEntry.getStudentNumber()+1);
                excellentCoursesDao.addEntry(excellentCoursesEntry);
            }
            return "购买成功";
        }else{
            return "订单信息不存在";
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
        if (userBehaviorEntry != null) {
            List<ObjectId> objectIdList = userBehaviorEntry.getCollectList();
            List<ExcellentCoursesEntry> excellentCoursesEntries = excellentCoursesDao.getEntryListById(objectIdList);
            for (ExcellentCoursesEntry entry : excellentCoursesEntries) {
                dtos.add(new ExcellentCoursesDTO(entry));
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
        List<ExcellentCoursesEntry> excellentCoursesEntries = excellentCoursesDao.getAllEntryList(subjectId,priceType,persionType,timeType,page,pageSize);
        int count = excellentCoursesDao.selectCount(subjectId);
        List<ExcellentCoursesDTO> dtos = new ArrayList<ExcellentCoursesDTO>();
        for(ExcellentCoursesEntry excellentCoursesEntry:excellentCoursesEntries){
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
        List<ExcellentCoursesEntry> excellentCoursesEntries = excellentCoursesDao.getMyKeyClassList(keyword);
        for(ExcellentCoursesEntry excellentCoursesEntry:excellentCoursesEntries) {
            dtos.add(new ExcellentCoursesDTO(excellentCoursesEntry));
        }
        return dtos;
    }

    /**
     * 老师 获得我的课程列表
     * @return
     */
    public Map<String,Object> getMyExcellentCourses(ObjectId userId,int page,int pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        List<ExcellentCoursesEntry> excellentCoursesEntries = excellentCoursesDao.getMyExcellentCourses(userId, page, pageSize);
        int count = excellentCoursesDao.selectMyCount(userId);
        List<ExcellentCoursesDTO> dtos = new ArrayList<ExcellentCoursesDTO>();
        long current = System.currentTimeMillis();
        for(ExcellentCoursesEntry excellentCoursesEntry:excellentCoursesEntries){
            ExcellentCoursesDTO dto = new ExcellentCoursesDTO(excellentCoursesEntry);
            if(dto.getStatus()==2){
                if(excellentCoursesEntry.getStartTime()> current){//未开始
                    dto.setType(0);
                }else if(excellentCoursesEntry.getEndTime()<current){//已结束
                    dto.setType(2);
                }else{
                    dto.setType(1);
                }
            }else{//未开始
                dto.setType(0);
            }
            dtos.add(dto);
        }
        map.put("list",dtos);
        map.put("count",count);
        return map;
    }

    public Map<String,Object> getMyDetails(ObjectId id){
        Map<String,Object> map = new HashMap<String, Object>();
        //课程相关
        ExcellentCoursesEntry excellentCoursesEntry = excellentCoursesDao.getEntry(id);
        ExcellentCoursesDTO dto = new ExcellentCoursesDTO(excellentCoursesEntry);
        map.put("dto",dto);
        //课时相关
        List<HourClassEntry> hourClassEntries = hourClassDao.getEntryList(id);
        List<HourClassDTO> hourClassDTOs = new ArrayList<HourClassDTO>();
        long current = System.currentTimeMillis();
        for(HourClassEntry entry : hourClassEntries){
            long startTime = entry.getStartTime();
            long endTime = entry.getStartTime() + entry.getCurrentTime();
            HourClassDTO hourClassDTO = new HourClassDTO(entry);
            if(current<startTime - 45*60*1000){//未开始
                hourClassDTO.setStatus(0);
            }else if(current >endTime){//已结束
                hourClassDTO.setStatus(2);
            }else{//正在进行
                hourClassDTO.setStatus(1);
            }
            hourClassDTOs.add(hourClassDTO);
        }
        map.put("list",hourClassDTOs);
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
        long start = hourClassEntry.getStartTime();
        long end = start + hourClassEntry.getCurrentTime();
        if(current>start  && current < end){//上课中
            CoursesRoomEntry coursesRoomEntry = coursesRoomDao.getEntry(excellentCoursesEntry.getID());
            UserEntry userEntry = userDao.findByUserId(userId);
            if(userEntry==null){
                throw  new Exception("非法用户！");
            }
            String name = StringUtils.isNotEmpty(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
            map.put("uid",coursesRoomEntry.getUserId());
            map.put("roomid",coursesRoomEntry.getRoomId());
            map.put("userName",name);
            map.put("password",coursesRoomEntry.getPlaypass());
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
        long start = hourClassEntry.getStartTime()-30*60*1000;
        long end =  hourClassEntry.getStartTime() + hourClassEntry.getCurrentTime();
        if(current>start  && current < end) {//上课中
            CoursesRoomEntry coursesRoomEntry = coursesRoomDao.getEntry(excellentCoursesEntry.getID());
            UserEntry userEntry = userDao.findByUserId(userId);
            if(userEntry==null){
                throw  new Exception("非法用户！");
            }
             /*https://view.csslcloud.net/api/view/lecturer?roomid=xxx&userid=xxx&publishname=xxx&publishpassword=xxx*/
            return  "https://view.csslcloud.net/api/view/lecturer?roomid="
                    +coursesRoomEntry.getRoomId()+"&userid="
                    +coursesRoomEntry.getUserId()+"&publishname="
                    +excellentCoursesEntry.getUserName()+"&publishpassword="
                    +coursesRoomEntry.getPublisherpass();
        }
        return  "";
    }

    /************************************充值相关*****************************************/
    public String freeChong(ObjectId userId,int number){
        /*  ObjectId userId,
            String description,
            int way,
            int type,
            int money,
            ObjectId sonId,
            ObjectId contactId,
            List<ObjectId> classList*/
        RechargeResultEntry rechargeResultEntry = new RechargeResultEntry(
               userId,"自定义充值  "+number+"¥",Constant.THREE,Constant.ONE,number,userId,null,new ArrayList<ObjectId>());
        rechargeResultDao.saveEntry(rechargeResultEntry);
        return "充值成功！";
    }
}
