package com.fulaan.controlphone.service;

import com.db.appmarket.AppDetailDao;
import com.db.backstage.TeacherApproveDao;
import com.db.controlphone.ControlAppDao;
import com.db.controlphone.ControlAppSchoolDao;
import com.db.controlphone.ControlAppSchoolResultDao;
import com.db.controlphone.ControlAppSchoolUserDao;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.GroupDao;
import com.db.fcommunity.MemberDao;
import com.fulaan.community.dto.CommunityDTO;
import com.pojo.appmarket.AppDetailEntry;
import com.pojo.backstage.TeacherApproveEntry;
import com.pojo.controlphone.ControlAppEntry;
import com.pojo.controlphone.ControlAppSchoolEntry;
import com.pojo.controlphone.ControlAppSchoolResultEntry;
import com.pojo.controlphone.ControlAppSchoolUserEntry;
import com.pojo.fcommunity.CommunityEntry;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by James on 2018-09-27.
 */
@Service
public class ControlSchoolPhoneService {

    private CommunityDao communityDao = new CommunityDao();

    private TeacherApproveDao teacherApproveDao = new TeacherApproveDao();

    private MemberDao memberDao = new MemberDao();

    private GroupDao groupDao = new GroupDao();

    private ControlAppSchoolDao controlAppSchoolDao = new ControlAppSchoolDao();

    private ControlAppSchoolResultDao controlAppSchoolResultDao = new ControlAppSchoolResultDao();

    private ControlAppSchoolUserDao controlAppSchoolUserDao = new ControlAppSchoolUserDao();

    private AppDetailDao appDetailDao = new AppDetailDao();

    private ControlAppDao controlAppDao = new ControlAppDao();

    //首页加载社群及判断权限
    public Map<String,Object> getSimpleMessageForTea(ObjectId teacherId){
        Map<String,Object> map = new HashMap<String, Object>();
        List<ObjectId> oids = getMyRoleList(teacherId);
        List<CommunityDTO> dtos = new ArrayList<CommunityDTO>();
        if(oids.size()>0){
            List<CommunityEntry> communityEntries = communityDao.findByObjectIds(oids);
            for(CommunityEntry com : communityEntries){
                dtos.add(new CommunityDTO(com));
            }
            map.put("list",dtos);
            map.put("isTeacher",true);
        }else{
            map.put("isTeacher",false);
            map.put("list",dtos);
        }
        //是否认证
        TeacherApproveEntry entry = teacherApproveDao.getEntry(teacherId);
        if(entry != null && entry.getType()==2){
            map.put("isRen",true);
        }else{
            map.put("isRen",false);
        }
        //map.put("isRen",true);
        return map;
    }


    //获取某社区详细数据
    public Map<String,Object> getOneCommunityMessageForTea(ObjectId communityId,ObjectId userId){
        Map<String,Object> map = new HashMap<String, Object>();
        //获取所有非三方应用
        List<AppDetailEntry> appDetailEntries =  appDetailDao.getNoThreeAppList();

        //获取该用户在该社群的推送app
        ControlAppEntry entry = controlAppDao.getEntry(userId, communityId);
        if(entry!=null && entry.getAppIdList()!=null && entry.getAppIdList().size() >0){
            List<AppDetailEntry> appDetailEntries1 = appDetailDao.getEntriesByIds(entry.getAppIdList());
            appDetailEntries.addAll(appDetailEntries1);
        }
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        Map<ObjectId,AppDetailEntry> map1 = new HashMap<ObjectId, AppDetailEntry>();
        for(AppDetailEntry appDetailEntry:appDetailEntries){
            objectIdList.add(appDetailEntry.getID());
        }
        long current = System.currentTimeMillis();
        String str = DateTimeUtils.getLongToStrTimeTwo(current);
        long startNum = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
        int week = getWeek(startNum);
        //获取目前所有记录
        Map<String,ControlAppSchoolResultEntry> resultEntryMap = controlAppSchoolResultDao.getEntryListByCommunityId(objectIdList, communityId);
        //获取默认配置
        Map<String,ControlAppSchoolEntry> schoolEntryMap =  controlAppSchoolDao.getEntryList(objectIdList);
        //在校记录
        List<Map<String,Object>> mapList1 = new ArrayList<Map<String, Object>>();
        for(AppDetailEntry appDetailEntry:appDetailEntries){
            Map<String,Object> appMap = new HashMap<String, Object>();
            appMap.put("logo",appDetailEntry.getLogo());
            appMap.put("name",appDetailEntry.getAppName());
            appMap.put("id",appDetailEntry.getID().toString());
            ControlAppSchoolResultEntry controlAppSchoolResultEntry = resultEntryMap.get(appDetailEntry.getID().toString()+"*"+1);
            appMap.put("isControl",1);
            appMap.put("freeTime",0);
            appMap.put("markStartFreeTime",current);
            if(controlAppSchoolResultEntry!=null){
                //生效时间
                long markStartFreeTime = controlAppSchoolResultEntry.getMarkStartFreeTime();
                //持续时间
                long freeTime = controlAppSchoolResultEntry.getFreeTime();
                if(markStartFreeTime+freeTime>current){
                    //自由时间
                    appMap.put("isControl",0);
                    appMap.put("freeTime",freeTime);
                    appMap.put("markStartFreeTime",markStartFreeTime);
                }else{
                    appMap.put("isControl",1);
                }
            }else{
                ControlAppSchoolEntry controlAppSchoolEntry = schoolEntryMap.get(appDetailEntry.getID().toString()+"*"+1);
                if(controlAppSchoolEntry!=null){//暂时都开放
                    appMap.put("isControl",1);
                }
            }
            mapList1.add(appMap);
        }
        //放学记录
        List<Map<String,Object>> mapList2 = new ArrayList<Map<String, Object>>();
        for(AppDetailEntry appDetailEntry:appDetailEntries){
            Map<String,Object> appMap = new HashMap<String, Object>();
            appMap.put("logo",appDetailEntry.getLogo());
            appMap.put("name",appDetailEntry.getAppName());
            appMap.put("id",appDetailEntry.getID().toString());
            ControlAppSchoolResultEntry controlAppSchoolResultEntry = resultEntryMap.get(appDetailEntry.getID().toString()+"*"+0+"*"+startNum);
            if(controlAppSchoolResultEntry==null){
                controlAppSchoolResultEntry = resultEntryMap.get(appDetailEntry.getID().toString()+"*"+0+"*"+week);
            }
            appMap.put("isControl",1);//管控中
            appMap.put("freeTime",0);
            appMap.put("today",0);
            appMap.put("controlType",2);
            if(controlAppSchoolResultEntry!=null){
                appMap.put("controlType",controlAppSchoolResultEntry.getControlType());
                appMap.put("freeTime",controlAppSchoolResultEntry.getOutSchoolCanUseTime());
                if(controlAppSchoolResultEntry.getOutSchoolRule()==0){
                    appMap.put("today",1);
                }
                if(controlAppSchoolResultEntry.getControlType()==2 || controlAppSchoolResultEntry.getFreeTime()!=0){
                    appMap.put("isControl",0);
                }

            }else{
                ControlAppSchoolEntry controlAppSchoolEntry = schoolEntryMap.get(appDetailEntry.getID().toString()+"*"+0);
                if(controlAppSchoolEntry!=null){//暂时都开放
                    appMap.put("controlType",controlAppSchoolEntry.getControlType());
                    appMap.put("freeTime",controlAppSchoolEntry.getFreeTime());
                    if(controlAppSchoolEntry.getControlType()==2 || controlAppSchoolEntry.getFreeTime()!=0){
                        appMap.put("isControl",0);
                    }
                }
            }
            mapList2.add(appMap);
        }
        map.put("schoolList",mapList1);
        map.put("school","");
        map.put("homeList",mapList2);
        map.put("homeTime","");
        return map;
    }


    //开放社群上学阶段应用自由时间
    /**
     *    针对上课时间的管控
     */
    public void addCommunityFreeTime(ObjectId appId,ObjectId communityId,ObjectId userId,int time)throws Exception{
        AppDetailEntry appDetailEntry = appDetailDao.findEntryById(appId);
        if(appDetailEntry==null){
            throw new Exception("应用已被删除!");
        }
        //1.获取社群状态并添加记录
        ControlAppSchoolResultEntry controlAppSchoolResultEntry = controlAppSchoolResultDao.getEntry(Constant.ONE,communityId,appId);
        long current = System.currentTimeMillis();
        if(controlAppSchoolResultEntry==null){
            //无记录添加新纪录
            ControlAppSchoolResultEntry controlAppSchoolResultEntry1 = new ControlAppSchoolResultEntry(
                    appId,communityId,appDetailEntry.getAppPackageName(),Constant.ONE,time*60*1000,
                    Constant.ZERO,Constant.ZERO,Constant.ZERO,current,current,Constant.ONE);
            controlAppSchoolResultDao.addEntry(controlAppSchoolResultEntry1);
        }else{
            //有记录，更新记录
            controlAppSchoolResultEntry.setFreeTime(time * 60 * 1000);
            controlAppSchoolResultEntry.setMarkStartFreeTime(current);
            controlAppSchoolResultEntry.setSaveTime(current);
            controlAppSchoolResultDao.addEntry(controlAppSchoolResultEntry);
        }
        //2 添加用户操作记录
        ControlAppSchoolUserEntry controlAppSchoolUserEntry = new ControlAppSchoolUserEntry(userId,appId,communityId,Constant.ONE,time*60*1000,Constant.ONE);
        controlAppSchoolUserDao.addEntry(controlAppSchoolUserEntry);

    }

    //关闭社群上学阶段应用自由时间
    public void deleteCommunuityFreeTime(ObjectId appId,ObjectId communityId,ObjectId userId)throws Exception{
        AppDetailEntry appDetailEntry = appDetailDao.findEntryById(appId);
        if(appDetailEntry==null){
            throw new Exception("应用已被删除!");
        }
        //1.获取社群状态并添加记录
        ControlAppSchoolResultEntry controlAppSchoolResultEntry = controlAppSchoolResultDao.getEntry(Constant.ONE,communityId,appId);
        long current = System.currentTimeMillis();
        if(controlAppSchoolResultEntry==null){
            //无记录添加新纪录
            ControlAppSchoolResultEntry controlAppSchoolResultEntry1 = new ControlAppSchoolResultEntry(
                    appId,communityId,appDetailEntry.getAppPackageName(),Constant.ONE,0,
                    Constant.ZERO,Constant.ZERO,Constant.ZERO,current,current,Constant.ONE);
            controlAppSchoolResultDao.addEntry(controlAppSchoolResultEntry1);
        }else{
            //有记录，更新记录
            controlAppSchoolResultEntry.setFreeTime(0);
            controlAppSchoolResultEntry.setSaveTime(current);
            controlAppSchoolResultDao.addEntry(controlAppSchoolResultEntry);
        }
        //2 添加用户操作记录
        ControlAppSchoolUserEntry controlAppSchoolUserEntry = new ControlAppSchoolUserEntry(userId,appId,communityId,Constant.ONE,0,Constant.ZERO);
        controlAppSchoolUserDao.addEntry(controlAppSchoolUserEntry);
    }

    //查询社群放学阶段应用某个设置列表
    public Map<String,Object> selectHomeAppList(ObjectId appId,ObjectId communityId,ObjectId userId)throws Exception{
        Map<String,Object> map = new HashMap<String, Object>();
        AppDetailEntry appDetailEntry = appDetailDao.findEntryById(appId);
        if(appDetailEntry==null){
            throw new Exception("应用已被删除!");
        }
        List<Integer> integers = new ArrayList<Integer>();
        integers.add(1);
        integers.add(0);
        integers.add(6);
        //1.获取社群状态并添加记录
        List<ControlAppSchoolResultEntry> controlAppSchoolResultEntrys = controlAppSchoolResultDao.getEntryList(appId,communityId,integers);
        Map<String,Object> dto2 = new HashMap<String, Object>();
        dto2.put("type",0);
        dto2.put("freeTime",0);
        long current = System.currentTimeMillis();
        String str = DateTimeUtils.getLongToStrTimeTwo(current);
        long startNum = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
        int week = getWeek(startNum);
        Map<String,Object> dto3 = null;
        for(ControlAppSchoolResultEntry controlAppSchoolResultEntry:controlAppSchoolResultEntrys){
            if(controlAppSchoolResultEntry.getOutSchoolRule()==1){//1-5
                Map<String,Object> dto = new HashMap<String, Object>();
                dto.put("type",controlAppSchoolResultEntry.getOutSchoolRule());
                dto.put("freeTime",controlAppSchoolResultEntry.getOutSchoolCanUseTime()/60000);
                map.put("one",dto);
                if(week<6){
                    dto2.put("type",controlAppSchoolResultEntry.getOutSchoolRule());
                    dto2.put("freeTime",controlAppSchoolResultEntry.getOutSchoolCanUseTime()/60000);
                }
            }
            if(controlAppSchoolResultEntry.getOutSchoolRule()==6){//6-7
                Map<String,Object> dto = new HashMap<String, Object>();
                dto.put("type",controlAppSchoolResultEntry.getOutSchoolRule());
                dto.put("freeTime",controlAppSchoolResultEntry.getOutSchoolCanUseTime()/60000);
                map.put("six",dto);
                if(week>5){
                    dto2.put("type",controlAppSchoolResultEntry.getOutSchoolRule());
                    dto2.put("freeTime",controlAppSchoolResultEntry.getOutSchoolCanUseTime()/60000);
                }
            }

            if(controlAppSchoolResultEntry.getOutSchoolRule()==0){
                if(controlAppSchoolResultEntry.getDateTime()==startNum){//仅当天
                    dto3 = new HashMap<String, Object>();
                }
            }

        }
        if(dto3!=null){
            dto2=dto3;
        }
        map.put("today",dto2);
        return map;
    }

    //设置社群放学阶段应用
    public void updateHomeAppTime(ObjectId appId,ObjectId communityId,ObjectId userId,int time,int type)throws Exception{//type =1 type=6  type=0
        AppDetailEntry appDetailEntry = appDetailDao.findEntryById(appId);
        if(appDetailEntry==null){
            throw new Exception("应用已被删除!");
        }
        //1.获取社群状态并添加记录
        ControlAppSchoolResultEntry controlAppSchoolResultEntry = controlAppSchoolResultDao.getHomeEntry(Constant.ZERO, communityId, appId, type);
        long current = System.currentTimeMillis();
        String str = DateTimeUtils.getLongToStrTimeTwo(current);
        long startNum = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
        if(controlAppSchoolResultEntry==null){
            //无记录添加新纪录
            ControlAppSchoolResultEntry controlAppSchoolResultEntry1 = new ControlAppSchoolResultEntry(
                    appId,communityId,appDetailEntry.getAppPackageName(),Constant.ONE,0,
                    time*60*1000,type,startNum,current,current,Constant.ZERO);
            controlAppSchoolResultDao.addEntry(controlAppSchoolResultEntry1);
        }else{
            //有记录，更新记录
            controlAppSchoolResultEntry.setOutSchoolCanUseTime(time*60*1000);
            controlAppSchoolResultEntry.setDateTime(startNum);
            controlAppSchoolResultEntry.setSaveTime(current);
            controlAppSchoolResultDao.addEntry(controlAppSchoolResultEntry);
        }
        //2 添加用户操作记录
        ControlAppSchoolUserEntry controlAppSchoolUserEntry = new ControlAppSchoolUserEntry(userId,appId,communityId,Constant.ZERO,time*60*1000,Constant.ONE);
        controlAppSchoolUserDao.addEntry(controlAppSchoolUserEntry);
    }






    /**
     * 获得用户的所有具有管理员权限的社区id
     *
     */
    public List<ObjectId> getMyRoleList(ObjectId userId){
        List<ObjectId> olsit = memberDao.getManagerGroupIdsByUserId(userId);
        List<ObjectId> mlist =   groupDao.getGroupIdsList(olsit);
        return mlist;
    }


    public int getWeek(long startNum){
        Date date = new Date(startNum);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if(w==0){
            w= 7;
        }
        return w;
    }
}
