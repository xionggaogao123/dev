package com.fulaan.excellentCourses.service;

import com.db.excellentCourses.ClassOrderDao;
import com.db.excellentCourses.ExcellentCoursesDao;
import com.db.excellentCourses.HourClassDao;
import com.db.excellentCourses.UserBehaviorDao;
import com.fulaan.excellentCourses.dto.ExcellentCoursesDTO;
import com.fulaan.excellentCourses.dto.HourClassDTO;
import com.fulaan.excellentCourses.dto.HourResultDTO;
import com.fulaan.newVersionBind.service.NewVersionBindService;
import com.mongodb.DBObject;
import com.pojo.excellentCourses.ClassOrderEntry;
import com.pojo.excellentCourses.ExcellentCoursesEntry;
import com.pojo.excellentCourses.HourClassEntry;
import com.pojo.excellentCourses.UserBehaviorEntry;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;
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

    private NewVersionBindService newVersionBindService = new NewVersionBindService();

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
            for(HourClassDTO dto1:dtoList){
                dto1.setType(Constant.ZERO);
                dto1.setUserId(userId.toString());
                ///todo        二期改为后台设定
                dto1.setClassNewPrice(dto1.getClassOldPrice());
                dto1.setWeek(getWeek(dto1.getDateTime()));
                dto1.setParentId(dto.getParentId());
                HourClassEntry classEntry =  dto1.buildAddEntry();
                oldPrice = oldPrice+dto1.getClassOldPrice();
                entryList.add(classEntry);
            }
            hourClassDao.delEntry(new ObjectId(dto.getParentId()),userId);
            this.addEntryBatch(entryList);
            excellentCoursesEntry.setAllClassCount(entryList.size());
            excellentCoursesEntry.setOldPrice(oldPrice);
            //todo        二期改为后台设定
            excellentCoursesEntry.setNewPrice(oldPrice);
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
            excellentCoursesDao.finishEntry(id,2);
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
        map.put("dto",getNowEntry(hourClassIds));
        return map;
    }

    public HourClassDTO getNowEntry(List<ObjectId> hourClassIds){
        //用户订单查询
        List<HourClassEntry> hourClassEntries = hourClassDao.getEntryList(hourClassIds);
        HourClassDTO hourClassDTO = null;
        long current = System.currentTimeMillis();
        for(HourClassEntry hourClassEntry:hourClassEntries){
            long endTime = hourClassEntry.getStartTime() + hourClassEntry.getCurrentTime();
            if(hourClassEntry.getStartTime()<=current+5*60*1000 && endTime >current ){
                hourClassDTO = new HourClassDTO(hourClassEntry);
            }
        }
        return hourClassDTO;
    }

    public Map<String,Object> getCoursesDesc(ObjectId id,ObjectId userId){
        Map<String,Object> map = new HashMap<String, Object>();
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
            UserBehaviorEntry userBehaviorEntry1 = new UserBehaviorEntry(userId,objectIdList,objectIdList2);
            userBehaviorDao.addEntry(userBehaviorEntry1);
        }
        map.put("dto",dto);
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
        map.put("now",nowHourClassDTO);
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
                    classOrderEntry.setType(0);
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
            if(classOrderEntries1.size()==0){
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
     * @param userId
     * @param subjectId
     * @param priceType
     * @param persionType
     * @param timeType
     * @param page
     * @param pageSize
     * @return
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

}
