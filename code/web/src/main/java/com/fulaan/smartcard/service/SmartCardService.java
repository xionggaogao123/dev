package com.fulaan.smartcard.service;

import com.db.smartcard.*;
import com.fulaan.managecount.service.ManageCountService;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.myschool.controller.GradeView;
import com.fulaan.school.service.SchoolService;
import com.fulaan.smartcard.dto.DoorDTO;
import com.fulaan.smartcard.dto.KaoQinDTO;
import com.fulaan.smartcard.dto.KaoQinStateDTO;
import com.fulaan.user.service.UserService;
import com.mongodb.BasicDBObject;
import com.pojo.school.ClassInfoDTO;
import com.pojo.school.DepartmentEntry;
import com.pojo.smartcard.*;
import com.pojo.user.UserEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guojing on 2016/6/15.
 */
@Service
public class SmartCardService {

    private AccountInfoDao accountInfoDao=new AccountInfoDao();

    private KaoQinInfoDao kaoQinInfoDao=new KaoQinInfoDao();

    private DoorInfoDao doorInfoDao=new DoorInfoDao();

    private TransInfoDao transInfoDao=new TransInfoDao();

    private KaoQinTimeSetDao kaoQinTimeSetDao=new KaoQinTimeSetDao();

    @Autowired
    private KaoQinStateService kaoQinStateService;

    @Autowired
    private ClassService classService;

    @Autowired
    private UserService userService;

    @Autowired
    private SchoolService schoolService;

    @Autowired
    private ManageCountService manageCountService;


    public List<List<KaoQinDTO>> searchKaoQinData(String selDate, String userId, ObjectId schoolId) {
        List<List<KaoQinDTO>> reList=new ArrayList<List<KaoQinDTO>>();

        KaoQinTimeSetEntry timeSetEntry = kaoQinTimeSetDao.getKaoQinTimeSetEntry(schoolId);

        String lateTime=timeSetEntry.getLateTime();
        String middleTime=timeSetEntry.getMiddleTime();
        String punctualTime=timeSetEntry.getPunctualTime();
        Map<String, List<KaoQinInfoEntry>> map=new HashMap<String, List<KaoQinInfoEntry>>();
        String startDate = DateTimeUtils.getMinMonthDate(selDate, DateTimeUtils.DATE_YYYY_MM);
        String endDate = DateTimeUtils.getMaxMonthDate(selDate, DateTimeUtils.DATE_YYYY_MM);
        DateTimeUtils time=new DateTimeUtils();
        List<String> dateAreas=time.getUseTimeArea(startDate,endDate);

        AccountInfoEntry accountInfo = accountInfoDao.getAccountInfoEntryByUserId(new ObjectId(userId));
        if(accountInfo!=null) {
            Integer accountNo = accountInfo.getAccounts();
            long dsl=time.getStrToLongTime(time.handleTime(startDate,1));
            long del=time.getStrToLongTime(time.handleTime(endDate,2));
            List<KaoQinInfoEntry> list = kaoQinInfoDao.getKaoQinInfoEntryList(accountNo,dsl,del);
            List<KaoQinInfoEntry> subList=null;
            for(KaoQinInfoEntry entry : list) {
                String cardDate=time.getLongToStrTime(entry.getCardDate());
                subList = map.get(cardDate);
                if(subList==null) {
                    subList=new ArrayList<KaoQinInfoEntry>();
                }
                subList.add(entry);
                map.put(cardDate,subList);
            }
        }
        //把每日的用户考勤，放到对应的日期下
        for(int k=0;k<dateAreas.size();k++){
            List<KaoQinDTO> reSubList=new ArrayList<KaoQinDTO>();
            for(int w=0;w<7;w++) {
                String date = dateAreas.get(w+k);
                int week = time.somedayIsWeekDay(time.stringToDate(date, time.DATE_YYYY_MM_DD));
                if (w==0&&k == 0) {
                    for (int i = 0; i < week; i++) {
                        KaoQinDTO kong = new KaoQinDTO();
                        kong.setState("null");
                        reSubList.add(kong);
                    }
                }

                if (week == 0||week == 6) {
                    KaoQinDTO kong = new KaoQinDTO();
                    kong.setDateTime(date);
                    kong.setState("holiday");
                    reSubList.add(kong);
                } else {
                    List<KaoQinInfoEntry> subs = map.get(date);
                    if (subs != null && subs.size() > 0) {
                        KaoQinInfoEntry info1 = subs.get(0);
                        KaoQinDTO dto = new KaoQinDTO(info1);
                        String sxtime = time.getLongToStrTimeThree(info1.getCardDate());
                        int flag11 = lateTime.compareTo(sxtime);
                        int flag12 = middleTime.compareTo(sxtime);

                        KaoQinInfoEntry info2 = subs.get(subs.size() - 1);
                        String fxtime = time.getLongToStrTimeThree(info2.getCardDate());
                        int flag21 = fxtime.compareTo(punctualTime);
                        int flag22 = fxtime.compareTo(middleTime);

                        dto.setShangXueDate(sxtime);
                        dto.setFangXueDate(fxtime);
                        if (flag11 >= 0 && flag21 >= 0) {
                            dto.setState("normal");
                        } else {
                            if (flag11 < 0&& flag12>=0) {
                                dto.setState("late");
                            }
                            if (flag21 < 0&&flag22>0) {
                                dto.setState("punctual");
                            }
                        }
                        reSubList.add(dto);
                    } else {
                        KaoQinDTO kong = new KaoQinDTO();
                        kong.setDateTime(date);
                        kong.setState("kuangke");
                        reSubList.add(kong);
                    }
                }

                if (k+w>=dateAreas.size()-1) {
                    date = dateAreas.get(dateAreas.size()-1);
                    week = time.somedayIsWeekDay(time.stringToDate(date, time.DATE_YYYY_MM_DD));
                    for (int i = 0; i < 6-week; i++) {
                        KaoQinDTO kong = new KaoQinDTO();
                        kong.setState("null");
                        reSubList.add(kong);
                    }
                }

                if(reSubList.size()>=7){
                    k=w+k;
                    break;
                }
            }
            reList.add(reSubList);
        }
        return reList;
    }


    public List<KaoQinStateDTO> searchKaoQinData(String dataType, String timeType, String classId, String deptId) {
        List<KaoQinStateDTO> reList=new ArrayList<KaoQinStateDTO>();
        List<ObjectId> userIds=null;
        if("stu".equals(dataType)){
            ClassInfoDTO classInfo=classService.findClassInfoByClassId(classId);
            userIds=classInfo.getStudentIds();
        }

        if("tea".equals(dataType)){
            DepartmentEntry deptEntry = schoolService.getDepartmentEntry(new ObjectId(deptId));
            userIds=deptEntry.getMembers();
        }

        if(userIds==null){
            userIds=new ArrayList<ObjectId>();
        }

        Map<ObjectId, UserEntry>  userMap = userService.getUserEntryMap(userIds,new BasicDBObject("nm",1));
        List<KaoQinStateEntry> list=kaoQinStateService.getKaoQinStateEntryByUserIds(userIds,timeType);
        Map<ObjectId, KaoQinStateEntry> stateMap=new HashMap<ObjectId, KaoQinStateEntry>();
        KaoQinStateDTO dto=null;
        for(KaoQinStateEntry entry:list){
            stateMap.put(entry.getUserId(),entry);
        }
        int kaoQinTotal=getKaoQinTotal(timeType,1);
        for(Map.Entry<ObjectId,UserEntry> entry: userMap.entrySet()){
            KaoQinStateEntry kaoQinEntry = stateMap.get(entry.getKey());
            UserEntry userEntry=entry.getValue();
            if(kaoQinEntry!=null){
                dto=new KaoQinStateDTO(kaoQinEntry);
            }else{
                dto=new KaoQinStateDTO();
                dto.setUserId(entry.getKey().toString());
                dto.setKuangkeCount(kaoQinTotal);
            }
            dto.setUserName(userEntry.getUserName());
            reList.add(dto);
        }
        return reList;
    }


    public KaoQinStateDTO getKaoQinReta(List<KaoQinStateDTO> list, String timeType) {
        KaoQinStateDTO reDto=new KaoQinStateDTO();
        int workDayCount=getKaoQinTotal(timeType,1);
        int kaoQinTotal = workDayCount*list.size()==0?1 : workDayCount*list.size();
        int normalCount=0;
        int lateCount=0;
        int punctualCount=0;
        int kuangkeCount=0;
        for(KaoQinStateDTO dto : list){
            normalCount+=dto.getNormalCount();
            lateCount+=dto.getLateCount();
            punctualCount+=dto.getPunctualCount();
            kuangkeCount+=dto.getKuangkeCount();
        }

        DecimalFormat df = new DecimalFormat("######0.00");

        double normalReta = normalCount * 100.0 / kaoQinTotal;
        reDto.setNormalReta(df.format(normalReta)+"%");

        double lateReta = lateCount * 100.0 / kaoQinTotal;
        reDto.setLateReta(df.format(lateReta)+"%");

        double punctualReta = punctualCount * 100.0 / kaoQinTotal;
        reDto.setPunctualReta(df.format(punctualReta)+"%");

        double kuangkeReta = kuangkeCount * 100.0 / kaoQinTotal;
        reDto.setKuangkeReta(df.format(kuangkeReta)+"%");
        return reDto;
    }

    public int getKaoQinTotal(String type, int operType) {
        Map<String, String> map=kaoQinStateService.getTimeArea(type, operType);
        String dateStart=map.get("dateStart");
        String dateEnd=map.get("dateEnd");
        DateTimeUtils time=new DateTimeUtils();
        List<String> dateAreas=time.getUseTimeArea(dateStart,dateEnd);
        int workDayCount=0;
        for(String date:dateAreas){
            int week = time.somedayIsWeekDay(time.stringToDate(date, time.DATE_YYYY_MM_DD));
            if (week != 0&&week != 6) {
                workDayCount++;
            }
        }
        return workDayCount;
    }

    public Map<String, Object> searchGradeKaoQinData(ObjectId schoolId, String timeType) {
        Map<String, Object> reMap=new HashMap<String, Object>();
        List<KaoQinStateDTO> reList=new ArrayList<KaoQinStateDTO>();
        List<GradeView> glist=schoolService.searchSchoolGradeList(schoolId.toString());
        int kaoQinTotal=getKaoQinTotal(timeType,1);
        int allStuCount=0;
        for(GradeView grade:glist) {
            if(grade.getGradeType()!=-1) {
                Map<String, List<ObjectId>> uisMap = manageCountService.getRoleUserIdByGradeId(grade.getId());
                List<ObjectId> userIds = uisMap.get("stuIds");
                Map<ObjectId, UserEntry> userMap = userService.getUserEntryMap(userIds, new BasicDBObject("nm", 1));
                List<KaoQinStateEntry> list = kaoQinStateService.getKaoQinStateEntryByUserIds(userIds, timeType);
                Map<ObjectId, KaoQinStateEntry> stateMap = new HashMap<ObjectId, KaoQinStateEntry>();
                KaoQinStateDTO dto = new KaoQinStateDTO();
                dto.setGradeId(grade.getId());
                dto.setGradeName(grade.getName());
                for (KaoQinStateEntry entry : list) {
                    stateMap.put(entry.getUserId(), entry);
                }
                int stuCount = 0;
                for (Map.Entry<ObjectId, UserEntry> entry : userMap.entrySet()) {
                    KaoQinStateEntry kaoQinEntry = stateMap.get(entry.getKey());
                    stuCount++;
                    if (kaoQinEntry != null) {
                        dto.setNormalCount(dto.getNormalCount() + kaoQinEntry.getNormalCount());
                        dto.setLateCount(dto.getLateCount() + kaoQinEntry.getLateCount());
                        dto.setPunctualCount(dto.getPunctualCount() + kaoQinEntry.getPunctualCount());
                        dto.setKuangkeCount(dto.getKuangkeCount() + kaoQinEntry.getKuangkeCount());
                    } else {
                        dto.setKuangkeCount(dto.getKuangkeCount() + kaoQinTotal);
                    }
                }
                allStuCount += stuCount;

                int gradeKaoQinTotal = kaoQinTotal * stuCount == 0 ? 1 : kaoQinTotal * stuCount;

                DecimalFormat df = new DecimalFormat("######0.00");

                double normalReta = dto.getNormalCount() * 100.0 / gradeKaoQinTotal;
                dto.setNormalReta(df.format(normalReta) + "%");

                double lateReta = dto.getLateCount() * 100.0 / gradeKaoQinTotal;
                dto.setLateReta(df.format(lateReta) + "%");

                double punctualReta = dto.getPunctualCount() * 100.0 / gradeKaoQinTotal;
                dto.setPunctualReta(df.format(punctualReta) + "%");

                double kuangkeReta = dto.getKuangkeCount() * 100.0 / gradeKaoQinTotal;
                dto.setKuangkeReta(df.format(kuangkeReta) + "%");

                reList.add(dto);
            }
        }
        reMap.put("list",reList);

        KaoQinStateDTO reDto=new KaoQinStateDTO();
        int allKaoQinTotal = kaoQinTotal*allStuCount==0?1 : kaoQinTotal*allStuCount;
        int allNormalCount=0;
        int allLateCount=0;
        int allPunctualCount=0;
        int allKuangkeCount=0;
        for(KaoQinStateDTO dto : reList){
            allNormalCount+=dto.getNormalCount();
            allLateCount+=dto.getLateCount();
            allPunctualCount+=dto.getPunctualCount();
            allKuangkeCount+=dto.getKuangkeCount();
        }

        DecimalFormat df = new DecimalFormat("######0.00");

        double allNormalReta = allNormalCount * 100.0 / allKaoQinTotal;
        reDto.setNormalReta(df.format(allNormalReta)+"%");

        double allLateReta = allLateCount * 100.0 / allKaoQinTotal;
        reDto.setLateReta(df.format(allLateReta)+"%");

        double allPunctualReta = allPunctualCount * 100.0 / allKaoQinTotal;
        reDto.setPunctualReta(df.format(allPunctualReta)+"%");

        double allKuangkeReta = allKuangkeCount * 100.0 / allKaoQinTotal;
        reDto.setKuangkeReta(df.format(allKuangkeReta)+"%");
        reMap.put("kaoQinReta",reDto);
        return reMap;
    }

    public Map searchClassKaoQinData(String gradeId, String timeType) {
        List<ClassInfoDTO> classlist=classService.getGradeClassesInfo(gradeId);
        Map<String, Object> reMap=new HashMap<String, Object>();
        List<KaoQinStateDTO> reList=new ArrayList<KaoQinStateDTO>();
        int kaoQinTotal=getKaoQinTotal(timeType,1);
        int allStuCount=0;
        for(ClassInfoDTO classInfo:classlist){
            Map<String,List<ObjectId>> uisMap=manageCountService.getRoleUserIdByClassId(classInfo.getId());
            List<ObjectId> userIds=uisMap.get("stuIds");
            Map<ObjectId, UserEntry>  userMap = userService.getUserEntryMap(userIds,new BasicDBObject("nm",1));
            List<KaoQinStateEntry> list=kaoQinStateService.getKaoQinStateEntryByUserIds(userIds,timeType);
            Map<ObjectId, KaoQinStateEntry> stateMap=new HashMap<ObjectId, KaoQinStateEntry>();
            KaoQinStateDTO dto = new KaoQinStateDTO();
            dto.setClassId(classInfo.getId());
            dto.setClassName(classInfo.getClassName());
            for(KaoQinStateEntry entry:list){
                stateMap.put(entry.getUserId(),entry);
            }
            int stuCount=0;
            for(Map.Entry<ObjectId,UserEntry> entry: userMap.entrySet()){
                KaoQinStateEntry kaoQinEntry = stateMap.get(entry.getKey());
                stuCount++;
                if(kaoQinEntry!=null){
                    dto.setNormalCount(dto.getNormalCount()+kaoQinEntry.getNormalCount());
                    dto.setLateCount(dto.getLateCount()+kaoQinEntry.getLateCount());
                    dto.setPunctualCount(dto.getPunctualCount()+kaoQinEntry.getPunctualCount());
                    dto.setKuangkeCount(dto.getKuangkeCount()+kaoQinEntry.getKuangkeCount());
                }else{
                    dto.setKuangkeCount(dto.getKuangkeCount()+kaoQinTotal);
                }
            }
            allStuCount+=stuCount;

            int gradeKaoQinTotal = kaoQinTotal*stuCount==0?1 : kaoQinTotal*stuCount;

            DecimalFormat df = new DecimalFormat("######0.00");

            double normalReta = dto.getNormalCount() * 100.0 / gradeKaoQinTotal;
            dto.setNormalReta(df.format(normalReta)+"%");

            double lateReta = dto.getLateCount() * 100.0 / gradeKaoQinTotal;
            dto.setLateReta(df.format(lateReta)+"%");

            double punctualReta = dto.getPunctualCount() * 100.0 / gradeKaoQinTotal;
            dto.setPunctualReta(df.format(punctualReta)+"%");

            double kuangkeReta = dto.getKuangkeCount() * 100.0 / gradeKaoQinTotal;
            dto.setKuangkeReta(df.format(kuangkeReta)+"%");

            reList.add(dto);
        }
        reMap.put("list",reList);

        KaoQinStateDTO reDto=new KaoQinStateDTO();
        int allKaoQinTotal = kaoQinTotal*allStuCount==0?1 : kaoQinTotal*allStuCount;
        int allNormalCount=0;
        int allLateCount=0;
        int allPunctualCount=0;
        int allKuangkeCount=0;
        for(KaoQinStateDTO dto : reList){
            allNormalCount+=dto.getNormalCount();
            allLateCount+=dto.getLateCount();
            allPunctualCount+=dto.getPunctualCount();
            allKuangkeCount+=dto.getKuangkeCount();
        }

        DecimalFormat df = new DecimalFormat("######0.00");

        double allNormalReta = allNormalCount * 100.0 / allKaoQinTotal;
        reDto.setNormalReta(df.format(allNormalReta)+"%");

        double allLateReta = allLateCount * 100.0 / allKaoQinTotal;
        reDto.setLateReta(df.format(allLateReta)+"%");

        double allPunctualReta = allPunctualCount * 100.0 / allKaoQinTotal;
        reDto.setPunctualReta(df.format(allPunctualReta)+"%");

        double allKuangkeReta = allKuangkeCount * 100.0 / allKaoQinTotal;
        reDto.setKuangkeReta(df.format(allKuangkeReta)+"%");
        reMap.put("kaoQinReta",reDto);
        return reMap;
    }

    public int searchDoorDataListCount(String name, String selState, String doorNum, String dateStart, String dateEnd) {
        DateTimeUtils time=new DateTimeUtils();
        long dsl =-1l;
        long del =-1l;
        if(!"".equals(dateStart)) {
            dsl = time.getStrToLongTime(dateStart);
        }
        if(!"".equals(dateStart)) {
            del = time.getStrToLongTime(dateEnd);
        }
        return doorInfoDao.searchDoorDataListCount(name,selState,doorNum,dsl,del);
    }

    public List<DoorDTO> searchDoorDataList(String name, String selState, String doorNum, String dateStart, String dateEnd, int page, int pageSize) {
        List<DoorDTO> reList=new ArrayList<DoorDTO>();
        DateTimeUtils time=new DateTimeUtils();
        long dsl =-1l;
        long del =-1l;
        if(!"".equals(dateStart)) {
            dsl = time.getStrToLongTime(dateStart);
        }
        if(!"".equals(dateStart)) {
            del = time.getStrToLongTime(dateEnd);
        }
        int skip = page < 1 ? 0 : ((page - 1) * pageSize);
        int limit =pageSize;
        List<DoorInfoEntry> list=doorInfoDao.searchDoorDataList(name,selState,doorNum,dsl,del,skip,limit);
        for(DoorInfoEntry entry:list){
            DoorDTO dto=new DoorDTO(entry);
            reList.add(dto);
        }
        return reList;
    }
}
