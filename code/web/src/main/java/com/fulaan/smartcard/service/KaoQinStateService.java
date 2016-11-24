package com.fulaan.smartcard.service;

import com.db.smartcard.*;
import com.fulaan.myclass.service.ClassService;
import com.fulaan.user.service.UserService;
import com.pojo.smartcard.AccountInfoEntry;
import com.pojo.smartcard.KaoQinInfoEntry;
import com.pojo.smartcard.KaoQinStateEntry;
import com.pojo.smartcard.KaoQinTimeSetEntry;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guojing on 2016/6/20.
 */
@Service
public class KaoQinStateService {
    private KaoQinStateDao kaoQinStateDao=new KaoQinStateDao();

    private KaoQinTimeSetDao kaoQinTimeSetDao=new KaoQinTimeSetDao();

    private AccountInfoDao accountInfoDao=new AccountInfoDao();

    private KaoQinInfoDao kaoQinInfoDao=new KaoQinInfoDao();

    private DoorInfoDao doorInfoDao=new DoorInfoDao();

    private TransInfoDao transInfoDao=new TransInfoDao();

    @Autowired
    private ClassService classService;

    @Autowired
    private UserService userService;



    public void updateKaoQinState(ObjectId schoolId){
        updateKaoQinState("xq",schoolId);
        updateKaoQinState("by",schoolId);
        updateKaoQinState("bz",schoolId);
    }

    public void updateKaoQinState(String type, ObjectId schoolId){
        KaoQinTimeSetEntry timeSetEntry = kaoQinTimeSetDao.getKaoQinTimeSetEntry(schoolId);

        String lateTime=timeSetEntry.getLateTime();
        String middleTime=timeSetEntry.getMiddleTime();
        String punctualTime=timeSetEntry.getPunctualTime();

        Map<String, String> map=getTimeArea(type, 1);
        String dateStart=map.get("dateStart");
        String dateEnd=map.get("dateEnd");
        DateTimeUtils time=new DateTimeUtils();
        List<String> dateAreas=time.getUseTimeArea(dateStart,dateEnd);

        List<Integer> accountNos=new ArrayList<Integer>();
        Map<Integer,String> userIdMap=new HashMap<Integer, String>();
        List<AccountInfoEntry> accList = accountInfoDao.getAccountInfoEntryBySchoolId(schoolId);
        for (AccountInfoEntry acc:accList){
            String userId=acc.getUserId()==null?"":acc.getUserId().toString();
            userIdMap.put(acc.getAccounts(),userId);
            accountNos.add(acc.getAccounts());
        }
        long dsl=time.getStrToLongTime(time.handleTime(dateStart,1));
        long del=time.getStrToLongTime(time.handleTime(dateEnd,2));
        List<KaoQinInfoEntry> list =null;
        list = kaoQinInfoDao.getKaoQinInfoEntryList(accountNos,dsl,del);
        Map<Integer,List<KaoQinInfoEntry>> accMap=new HashMap<Integer, List<KaoQinInfoEntry>>();
        List<KaoQinInfoEntry> subList=null;
        for(KaoQinInfoEntry entry : list) {
            subList = accMap.get(entry.getAccounts());
            if(subList==null) {
                subList=new ArrayList<KaoQinInfoEntry>();
            }
            subList.add(entry);
            accMap.put(entry.getAccounts(),subList);
        }

        Map<String, Map<String, List<KaoQinInfoEntry>>> kqmap=new HashMap<String, Map<String, List<KaoQinInfoEntry>>>();
        Map<String,List<KaoQinInfoEntry>> subMap=null;

        for(Map.Entry<Integer,String> entry: userIdMap.entrySet()){
            String userId=entry.getValue();
            list=accMap.get(entry.getKey());
            if(list==null){
                list=new ArrayList<KaoQinInfoEntry>();
            }
            subMap = kqmap.get(userId);
            if(subMap==null) {
                subMap=new HashMap<String,List<KaoQinInfoEntry>>();
            }
            for(KaoQinInfoEntry item : list) {
                String cardDate=time.getLongToStrTime(item.getCardDate());
                subList = subMap.get(cardDate);
                if(subList==null) {
                    subList=new ArrayList<KaoQinInfoEntry>();
                }
                subList.add(item);
                subMap.put(cardDate,subList);
            }
            kqmap.put(userId,subMap);
        }

        List<KaoQinStateEntry> addList=new ArrayList<KaoQinStateEntry>();
        for(Map.Entry<String, Map<String, List<KaoQinInfoEntry>>> entry : kqmap.entrySet()){
            String userId = entry.getKey();
            int normalCount=0;
            int lateCount=0;
            int punctualCount=0;
            int kuangkeCount=0;
            Map<String,List<KaoQinInfoEntry>> subMs=entry.getValue();
            //把每日的用户考勤，放到对应的日期下
            for(int k=0;k<dateAreas.size();k++){
                 String date = dateAreas.get(k);
                int week = time.somedayIsWeekDay(time.stringToDate(date, time.DATE_YYYY_MM_DD));
                if (week != 0&&week != 6) {
                    List<KaoQinInfoEntry> subs = subMs.get(date);
                    if (subs != null && subs.size() > 0) {
                        KaoQinInfoEntry info1 = subs.get(0);
                        String sxtime = time.getLongToStrTimeThree(info1.getCardDate());
                        int flag11 = lateTime.compareTo(sxtime);
                        int flag12 = middleTime.compareTo(sxtime);
                        KaoQinInfoEntry info2 = subs.get(subs.size() - 1);
                        String fxtime = time.getLongToStrTimeThree(info2.getCardDate());
                        int flag21 = fxtime.compareTo(punctualTime);
                        int flag22 = fxtime.compareTo(middleTime);

                        if (flag11 >= 0 && flag21 >= 0) {
                            normalCount++;
                        } else {
                            if (flag11 < 0 && flag12 >= 0) {
                                lateCount++;
                            }else if (flag21 < 0 && flag22 > 0) {
                                punctualCount++;
                            }else{
                                kuangkeCount++;
                            }
                        }
                    } else {
                        kuangkeCount++;
                    }
                }
            }

            KaoQinStateEntry kaoQinStateEntry=kaoQinStateDao.getKaoQinStateEntry(new ObjectId(userId),type);
            if(kaoQinStateEntry==null){
                kaoQinStateEntry=new KaoQinStateEntry(new ObjectId(userId),type,normalCount,lateCount,punctualCount,kuangkeCount);
                addList.add(kaoQinStateEntry);
            }else{
                kaoQinStateEntry.setNormalCount(normalCount);
                kaoQinStateEntry.setLateCount(lateCount);
                kaoQinStateEntry.setPunctualCount(punctualCount);
                kaoQinStateEntry.setKuangkeCount(kuangkeCount);
                kaoQinStateDao.updKaoQinStateEntry(kaoQinStateEntry);
            }
        }
        if(addList.size()>0){
            kaoQinStateDao.addKaoQinStateEntrys(addList);
        }
    }

    public Map<String, String> getTimeArea(String timeType, int operType){
        Map<String, String> map=new HashMap<String, String>();
        String dateStart="";
        String dateEnd="";
        DateTimeUtils time=new DateTimeUtils();
        //获取当前日期
        String currDate=time.getCurrDate();
        if("xq".equals(timeType)){
            //取得当前月
            int currMonth=time.getMonth();
            //取得当前年
            int currYear=time.getYear();
            //判断当前月是否在8月到12月之间（包含12月），及判断当前时间是否在下半年的学期
            if ((currMonth > 8 && currMonth < 13)||currMonth<2) {//如果在
                if(currMonth<2){
                    currYear=currYear-1;
                }
                dateStart=currYear+"-09-01";
                dateEnd=currYear + 1+"-01-31";
            } else {//如果不在
                dateStart=currYear+"-02-01";
                dateEnd=currYear+"-07-06";
            }
        }
        if("by".equals(timeType)){
            dateStart=time.getMinMonthDate(currDate);
            dateEnd=time.getMaxMonthDate(currDate);
        }
        if("bz".equals(timeType)){
            dateStart=time.getCurrentMonday(currDate);
            dateEnd=time.getPreviousSunday(currDate);
        }

        if(operType==1){
            dateEnd=currDate;
        }
        map.put("dateStart",dateStart);
        map.put("dateEnd",dateEnd);
        return map;
    }

    public List<KaoQinStateEntry> getKaoQinStateEntryByUserIds(List<ObjectId> userIds, String timeType) {
        return kaoQinStateDao.getKaoQinStateEntry(userIds,timeType);
    }
}
