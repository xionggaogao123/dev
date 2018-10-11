package com.fulaan.controlphone.service;

import com.db.appmarket.AppDetailDao;
import com.db.backstage.SchoolControlTimeDao;
import com.db.backstage.SystemMessageDao;
import com.db.backstage.TeacherApproveDao;
import com.db.controlphone.*;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.GroupDao;
import com.db.fcommunity.MemberDao;
import com.db.indexPage.IndexPageDao;
import com.db.jiaschool.HomeSchoolDao;
import com.db.jiaschool.SchoolCommunityDao;
import com.db.user.NewVersionBindRelationDao;
import com.fulaan.appmarket.dto.AppDetailDTO;
import com.fulaan.community.dto.CommunityDTO;
import com.fulaan.controlphone.dto.ControlAppUserDTO;
import com.fulaan.controlphone.dto.ControlPhoneDTO;
import com.fulaan.controlphone.dto.PhoneSchoolTimeDTO;
import com.fulaan.indexpage.dto.IndexPageDTO;
import com.fulaan.indexpage.dto.SystemMessageDTO;
import com.fulaan.mqtt.MQTTSendMsg;
import com.fulaan.newVersionBind.service.NewVersionBindService;
import com.pojo.appmarket.AppDetailEntry;
import com.pojo.backstage.SchoolControlTimeEntry;
import com.pojo.backstage.TeacherApproveEntry;
import com.pojo.controlphone.*;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.indexPage.IndexPageEntry;
import com.pojo.jiaschool.SchoolCommunityEntry;
import com.pojo.newVersionGrade.CommunityType;
import com.pojo.user.NewVersionBindRelationEntry;
import com.sys.constants.Constant;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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

    private NewVersionBindRelationDao newVersionBindRelationDao = new NewVersionBindRelationDao();

    private ControlAppSystemDao controlAppSystemDao = new ControlAppSystemDao();

    private ControlPhoneDao controlPhoneDao = new ControlPhoneDao();

    private ControlTimeDao controlTimeDao = new ControlTimeDao();

    private ControlSetBackDao controlSetBackDao = new ControlSetBackDao();

    private HomeSchoolDao homeSchoolDao = new HomeSchoolDao();

    private SchoolCommunityDao schoolCommunityDao = new SchoolCommunityDao();

    private SchoolControlTimeDao schoolControlTimeDao = new SchoolControlTimeDao();

    @Autowired
    private NewVersionBindService newVersionBindService;

    private ControlAppUserDao controlAppUserDao = new ControlAppUserDao();

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
        int ty = 2;//校管控阶段
        //校管控
        this.getCommunityControlTime(current,communityId,week,startNum,map,ty);

        //在校记录
        List<Map<String,Object>> mapList1 = new ArrayList<Map<String, Object>>();
        for(AppDetailEntry appDetailEntry:appDetailEntries){
            Map<String,Object> appMap = new HashMap<String, Object>();
            appMap.put("logo",appDetailEntry.getLogo());
            appMap.put("name",appDetailEntry.getAppName());
            appMap.put("id",appDetailEntry.getID().toString());
            ControlAppSchoolResultEntry controlAppSchoolResultEntry = resultEntryMap.get(appDetailEntry.getID().toString()+"*"+1+"*");
            appMap.put("isControl",1);//管控中
            appMap.put("freeTime",0);
            appMap.put("markStartFreeTime",current);
            if(controlAppSchoolResultEntry!=null && ty==2){
                //生效时间
                long markStartFreeTime = controlAppSchoolResultEntry.getMarkStartFreeTime();
                //持续时间
                long freeTime = controlAppSchoolResultEntry.getFreeTime();
                if(markStartFreeTime+freeTime>current){
                    long htm = markStartFreeTime+freeTime-current;
                    //自由时间
                    appMap.put("isControl",0);
                    appMap.put("freeTime",htm);
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
        //不受管控的类型
        List<ObjectId> objectIdList1 = controlAppSchoolDao.getNoAppList();
        for(AppDetailEntry appDetailEntry:appDetailEntries){
            if(!objectIdList1.contains(appDetailEntry.getID())){//过滤放学后不受管控的类型
                Map<String,Object> appMap = new HashMap<String, Object>();
                appMap.put("logo",appDetailEntry.getLogo());
                appMap.put("name",appDetailEntry.getAppName());
                appMap.put("id",appDetailEntry.getID().toString());
                ControlAppSchoolResultEntry controlAppSchoolResultEntry = resultEntryMap.get(appDetailEntry.getID().toString()+"*"+0+"*"+startNum);
                if(controlAppSchoolResultEntry==null){
                    if(week<6){
                        controlAppSchoolResultEntry = resultEntryMap.get(appDetailEntry.getID().toString()+"*"+0+"*1");
                    }else{
                        controlAppSchoolResultEntry = resultEntryMap.get(appDetailEntry.getID().toString()+"*"+0+"*6");
                    }

                }
                appMap.put("isControl",1);//管控中
                appMap.put("freeTime",0);
                appMap.put("today",0);
                appMap.put("controlType",1);
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
        }
        map.put("schoolList",mapList1);
        map.put("homeList",mapList2);
        map.put("week",week);
        return map;
    }

    //获得某个社群的校管控时间
    public void getCommunityControlTime(long current,ObjectId communityId,int week,long dateTime,Map<String,Object> objectMap,int type){
        SchoolCommunityEntry schoolCommunityEntry = schoolCommunityDao.getEntryById(communityId);
        String string = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,11);
        Map<String,SchoolControlTimeEntry>  map = new HashMap<String, SchoolControlTimeEntry>();
        if(schoolCommunityEntry==null){
            //查询默认管控
            List<SchoolControlTimeEntry> schoolControlTimeEntryList2 = schoolControlTimeDao.getEachSchoolControlSettingList(null);
            for (SchoolControlTimeEntry entry : schoolControlTimeEntryList2){
                if(entry.getType()==1){
                    map.put(entry.getWeek()+"",entry);
                }else if(entry.getType()==2){
                    long stm = DateTimeUtils.getStrToLongTime(entry.getDateFrom(), "yyyy-MM-dd");
                    long etm = DateTimeUtils.getStrToLongTime(entry.getDateTo(), "yyyy-MM-dd");
                    if(stm<dateTime && dateTime<etm){
                        map.put(dateTime+"",entry);
                    }
                    if(stm ==dateTime){
                        map.put(dateTime+"",entry);
                    }
                    if(etm==dateTime){
                        map.put(dateTime+"",entry);
                    }
                }
            }
        }else{
            List<SchoolControlTimeEntry> schoolControlTimeEntryList = schoolControlTimeDao.getEachSchoolControlSettingList(schoolCommunityEntry.getSchoolId());
            if(schoolControlTimeEntryList.size()==0){//无设置
                //查询默认管控
                List<SchoolControlTimeEntry> schoolControlTimeEntryList2 = schoolControlTimeDao.getEachSchoolControlSettingList(null);
                for (SchoolControlTimeEntry entry : schoolControlTimeEntryList2){
                    if(entry.getType()==1){
                        map.put(entry.getWeek()+"",entry);
                    }else if(entry.getType()==2){
                        long stm = DateTimeUtils.getStrToLongTime(entry.getDateFrom(), "yyyy-MM-dd");
                        long etm = DateTimeUtils.getStrToLongTime(entry.getDateTo(), "yyyy-MM-dd");
                        if(stm<dateTime && dateTime<etm){
                            map.put(dateTime+"",entry);
                        }
                        if(stm ==dateTime){
                            map.put(dateTime+"",entry);
                        }
                        if(etm==dateTime){
                            map.put(dateTime+"",entry);
                        }
                    }
                }
            }else{
                for (SchoolControlTimeEntry entry : schoolControlTimeEntryList){
                    if(entry.getType()==1){
                        map.put(entry.getWeek()+"",entry);
                    }else if(entry.getType()==2){
                        long stm = DateTimeUtils.getStrToLongTime(entry.getDateFrom(), "yyyy-MM-dd");
                        long etm = DateTimeUtils.getStrToLongTime(entry.getDateTo(), "yyyy-MM-dd");
                        if(stm<dateTime && dateTime<etm){
                            map.put("0",entry);
                        }
                        if(stm ==dateTime){
                            map.put("0",entry);
                        }
                        if(etm==dateTime){
                            map.put("0",entry);
                        }
                    }
                }
            }
        }
        //计算生效的结果
        SchoolControlTimeEntry schoolControlTimeEntry = map.get("0");
        if(schoolControlTimeEntry!=null){
            objectMap.put("schoolTime",schoolControlTimeEntry.getSchoolTimeFrom()+"-"+schoolControlTimeEntry.getSchoolTimeTo());
            objectMap.put("homeTime",schoolControlTimeEntry.getSchoolTimeTo()+"-"+schoolControlTimeEntry.getBedTimeFrom());
            long ssm = dateTime;
            if(schoolControlTimeEntry.getSchoolTimeFrom()!=null&& !schoolControlTimeEntry.getSchoolTimeFrom().equals("")){
                ssm = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry.getSchoolTimeFrom(), "yyyy-MM-dd HH:mm");
            }
            long sem = dateTime;
            if(schoolControlTimeEntry.getSchoolTimeTo()!=null&& !schoolControlTimeEntry.getSchoolTimeTo().equals("")){
                sem = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry.getSchoolTimeTo(), "yyyy-MM-dd HH:mm");
            }
            long bsm = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry.getBedTimeFrom(), "yyyy-MM-dd HH:mm");
            long bem = DateTimeUtils.getStrToLongTime(string+" 23:59", "yyyy-MM-dd HH:mm");
            objectMap.put("type",2);
            if(current>ssm && current<sem){//上课时间，跳1
                objectMap.put("type",1);
                type=1;
            }
            if(current>bsm && current<bem){//睡眠时间，跳1
                objectMap.put("type",3);
                type=3;
            }

        }else{
            //计算生效的结果
            SchoolControlTimeEntry schoolControlTimeEntry2 = map.get(week+"");
            if(schoolControlTimeEntry2!=null){

                objectMap.put("schoolTime",schoolControlTimeEntry2.getSchoolTimeFrom()+"-"+schoolControlTimeEntry2.getSchoolTimeTo());
                objectMap.put("homeTime",schoolControlTimeEntry2.getSchoolTimeTo()+"-"+schoolControlTimeEntry2.getBedTimeFrom());
                if(schoolControlTimeEntry2.getSchoolTimeFrom().equals("") && schoolControlTimeEntry2.getSchoolTimeTo().equals("")){
                    objectMap.put("schoolTime","07:30-07:30");
                    objectMap.put("homeTime",schoolControlTimeEntry2.getBedTimeTo()+"-"+schoolControlTimeEntry2.getBedTimeFrom());
                }
                long ssm = dateTime;
                if(schoolControlTimeEntry2.getSchoolTimeFrom()!=null&& !schoolControlTimeEntry2.getSchoolTimeFrom().equals("")){
                    ssm = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry2.getSchoolTimeFrom(), "yyyy-MM-dd HH:mm");
                }
                long sem = dateTime;
                if(schoolControlTimeEntry2.getSchoolTimeTo()!=null&& !schoolControlTimeEntry2.getSchoolTimeTo().equals("")){
                    sem = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry2.getSchoolTimeTo(), "yyyy-MM-dd HH:mm");
                }
                long bsm = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry2.getBedTimeFrom(), "yyyy-MM-dd HH:mm");
                long bem = DateTimeUtils.getStrToLongTime(string+" 23:59", "yyyy-MM-dd HH:mm");
                objectMap.put("type",2);
                if(current>ssm && current<sem){//上课时间，跳1
                    objectMap.put("type",1);
                    type=1;
                }
                if(current>bsm && current<bem){//睡眠时间，跳1
                    objectMap.put("type",3);
                    type=3;
                }
            }else{
                objectMap.put("schoolTime","07:30-17:30");
                objectMap.put("homeTime","17:30-22:00");
                long ssm = DateTimeUtils.getStrToLongTime(string+" 07:30", "yyyy-MM-dd HH:mm");
                long sem = DateTimeUtils.getStrToLongTime(string+" 17:30", "yyyy-MM-dd HH:mm");
                long bsm = DateTimeUtils.getStrToLongTime(string+" 22:00", "yyyy-MM-dd HH:mm");
                long bem = DateTimeUtils.getStrToLongTime(string+" 23:59", "yyyy-MM-dd HH:mm");
                objectMap.put("type",2);
                if(current>ssm && current<sem){//上课时间，跳1
                    objectMap.put("type",1);
                    type=1;
                }
                if(current>bsm && current<bem){//睡眠时间，跳1
                    objectMap.put("type",3);
                    type=3;
                }
            }
        }
    }


    //开放社群上学阶段应用自由时间
    /**
     *    针对上课时间的管控
     */
    public void addCommunityFreeTime(ObjectId appId,ObjectId communityId,ObjectId userId,String dateTime,int freeTime)throws Exception{
        long current = System.currentTimeMillis();
        String string = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,11);
        long etm = 0;
        if(!dateTime.equals("")){
            etm = DateTimeUtils.getStrToLongTime(string+" "+dateTime, "yyyy-MM-dd HH:mm");
        }
        long time = freeTime*60000;
        long endTime =current + time;//截止时间
        if(etm>0 && endTime>etm){
            time = etm -current;
        }

        AppDetailEntry appDetailEntry = appDetailDao.findEntryById(appId);
        if(appDetailEntry==null){
            throw new Exception("应用已被删除!");
        }
        //获取社群当前的管控状态

        //1.获取社群状态并添加记录
        ControlAppSchoolResultEntry controlAppSchoolResultEntry = controlAppSchoolResultDao.getEntry(Constant.ONE,communityId,appId);
        if(controlAppSchoolResultEntry==null){
            //无记录添加新纪录
            ControlAppSchoolResultEntry controlAppSchoolResultEntry1 = new ControlAppSchoolResultEntry(
                    appId,communityId,appDetailEntry.getAppPackageName(),Constant.ONE,time,
                    Constant.ZERO,Constant.ZERO,Constant.ZERO,current,current,Constant.ONE);
            controlAppSchoolResultDao.addEntry(controlAppSchoolResultEntry1);
        }else{
            //有记录，更新记录
            controlAppSchoolResultEntry.setFreeTime(time);
            controlAppSchoolResultEntry.setMarkStartFreeTime(current);
            controlAppSchoolResultEntry.setSaveTime(current);
            controlAppSchoolResultDao.addEntry(controlAppSchoolResultEntry);
        }
        //2 添加用户操作记录
        ControlAppSchoolUserEntry controlAppSchoolUserEntry = new ControlAppSchoolUserEntry(userId,appId,communityId,Constant.ONE,time*60*1000,Constant.ONE);
        controlAppSchoolUserDao.addEntry(controlAppSchoolUserEntry);
        //向学生端推送消息
        List<String> objectIdList = newVersionBindService.getStudentIdListByCommunityId(communityId);
        try {
            MQTTSendMsg.sendMessageList(MQTTType.phone.getEname(), objectIdList, current);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateString = formatter.format(new Date(current));
            List<ObjectId> oids = new ArrayList<ObjectId>();
            for(String str : objectIdList){
                oids.add(new ObjectId(str));
            }
            controlTimeDao.delAllEntry(oids, current);
        }catch (Exception e){

        }
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
        //向学生端推送消息
        List<String> objectIdList = newVersionBindService.getStudentIdListByCommunityId(communityId);
        try {
            MQTTSendMsg.sendMessageList(MQTTType.phone.getEname(), objectIdList, current);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateString = formatter.format(new Date(current));
            List<ObjectId> oids = new ArrayList<ObjectId>();
            for(String str : objectIdList){
                oids.add(new ObjectId(str));
            }
            controlTimeDao.delAllEntry(oids, current);
        }catch (Exception e){

        }
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
        dto2.put("type",-1);
        dto2.put("freeTime",0);
        long current = System.currentTimeMillis();
        String str = DateTimeUtils.getLongToStrTimeTwo(current);
        long startNum = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
        int week = getWeek(startNum);
        Map<String,Object> dto3 = null;
        List<Map<String,Object>> mapList = new ArrayList<Map<String, Object>>();
        for(ControlAppSchoolResultEntry controlAppSchoolResultEntry:controlAppSchoolResultEntrys){
            if(controlAppSchoolResultEntry.getOutSchoolRule()==1){//1-5
                Map<String,Object> dto = new HashMap<String, Object>();
                dto.put("type",controlAppSchoolResultEntry.getOutSchoolRule());
                dto.put("freeTime",controlAppSchoolResultEntry.getOutSchoolCanUseTime()/60000);
                dto.put("index",1);
                mapList.add(dto);
                if(week<6){
                    dto2.put("type",controlAppSchoolResultEntry.getOutSchoolRule());
                    dto2.put("freeTime",controlAppSchoolResultEntry.getOutSchoolCanUseTime()/60000);
                }
            }
            if(controlAppSchoolResultEntry.getOutSchoolRule()==6){//6-7
                Map<String,Object> dto = new HashMap<String, Object>();
                dto.put("type",controlAppSchoolResultEntry.getOutSchoolRule());
                dto.put("freeTime",controlAppSchoolResultEntry.getOutSchoolCanUseTime()/60000);
                dto.put("index",2);
                mapList.add(dto);
                if(week>5){
                    dto2.put("type",controlAppSchoolResultEntry.getOutSchoolRule());
                    dto2.put("freeTime",controlAppSchoolResultEntry.getOutSchoolCanUseTime()/60000);
                }
            }

            if(controlAppSchoolResultEntry.getOutSchoolRule()==0){
                if(controlAppSchoolResultEntry.getDateTime()==startNum){//仅当天
                    Map<String,Object> dto = new HashMap<String, Object>();
                    dto.put("type",controlAppSchoolResultEntry.getOutSchoolRule());
                    dto.put("freeTime",controlAppSchoolResultEntry.getOutSchoolCanUseTime()/60000);
                    dto3 = dto;
                }
            }

        }
        if(dto3!=null){
            dto2=dto3;
        }
        map.put("list",mapList);
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
        //向学生端推送消息
        List<String> objectIdList = newVersionBindService.getStudentIdListByCommunityId(communityId);
        try {
            MQTTSendMsg.sendMessageList(MQTTType.phone.getEname(), objectIdList, current);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateString = formatter.format(new Date(current));
            List<ObjectId> oids = new ArrayList<ObjectId>();
            for(String str2 : objectIdList){
                oids.add(new ObjectId(str2));
            }
            controlTimeDao.delAllEntry(oids, current);
        }catch (Exception e){

        }
    }

    //学生新未登陆获取默认信息
    public Map<String,Object> getNewSimpleMessageForSon(){
        //appDetailDao.get
        Map<String,Object> map = new HashMap<String, Object>();
        //可用应用列表
        List<AppDetailDTO> detailDTOs = new ArrayList<AppDetailDTO>();
        List<ObjectId> appIds = new ArrayList<ObjectId>();
        List<AppDetailEntry> detailEntries3 = new ArrayList<AppDetailEntry>();
        ControlAppSystemEntry controlAppSystemEntry = controlAppSystemDao.getEntry();
        if(controlAppSystemEntry != null && controlAppSystemEntry.getAppIdList()!=null && controlAppSystemEntry.getAppIdList().size()>0){
            detailEntries3 =  appDetailDao.getEntriesByIds(controlAppSystemEntry.getAppIdList());
            //allAppList.addAll(detailEntries3);
            for(AppDetailEntry detailEntry : detailEntries3){
                detailDTOs.add(new AppDetailDTO(detailEntry));
                appIds.add(detailEntry.getID());
            }
        }
        List<AppDetailDTO> detailDTOs2 = new ArrayList<AppDetailDTO>();
        //map.put("acceptApp",detailDTOs);
        //禁用黑名单
        List<AppDetailEntry> detailEntries =  appDetailDao.getSimpleAppEntry();
        for(AppDetailEntry detailEntry : detailEntries){
            detailDTOs2.add(new AppDetailDTO(detailEntry));
        }
        map.put("blackApp",detailDTOs2);
        map.put("thirdApp",detailDTOs);
        //可用电话记录
        List<ControlPhoneDTO> dtos = new ArrayList<ControlPhoneDTO>();
        List<ControlPhoneEntry> entries = controlPhoneDao.getEntryListByType();
        if(entries.size()>0){
            for(ControlPhoneEntry entry : entries){
                dtos.add(new ControlPhoneDTO(entry));
            }
        }
        map.put("phone",dtos);
        //可用时间
       /* long timecu = 3*60*1000;//未登录3分钟使用时间
        map.put("time",timecu/60000);*/
        map.put("backTime",24*60);
        map.put("appTime",24*60);
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        String str = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,10);
        long zero = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(zero);
        int week = getWeek(zero);
        //获取默认校管控
        List<PhoneSchoolTimeDTO> phoneTimeDTOs = new ArrayList<PhoneSchoolTimeDTO>();
        this.getSchoolControlTime(current, dateNowStr, week, zero, map, phoneTimeDTOs);
       /* map.put("freeTime",-1);*/
        map.put("controlList",phoneTimeDTOs);
        //获取默认应用管控
        this.getAppControlTime(detailEntries3,current,map);
        return map;
    }

    //获得默认应用管控时间
    public void getAppControlTime(List<AppDetailEntry> appDetailEntries,long current,Map<String,Object> map){
        //获取所有非三方应用
        List<AppDetailEntry> appDetailEntries2 =  appDetailDao.getNoThreeAppList();
        appDetailEntries.addAll(appDetailEntries2);
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        for(AppDetailEntry appDetailEntry:appDetailEntries){
            objectIdList.add(appDetailEntry.getID());
        }
        //获取默认配置
        Map<String,ControlAppSchoolEntry> schoolEntryMap =  controlAppSchoolDao.getEntryList(objectIdList);
        List<Map<String,Object>> mapList = new ArrayList<Map<String, Object>>();
        //不受管控的类型
        List<ObjectId> objectIdList1 = controlAppSchoolDao.getNoAppList();
        for(AppDetailEntry appDetailEntry:appDetailEntries){
            ControlAppSchoolEntry controlAppSchoolEntry = schoolEntryMap.get(appDetailEntry.getID().toString()+"*0");
            Map<String,Object>  dto  = new HashMap<String, Object>();
            if(controlAppSchoolEntry!=null){
                dto.put("freeTime",0);
                dto.put("appName",appDetailEntry.getAppName());
                dto.put("packageName",appDetailEntry.getAppPackageName());
                dto.put("id",appDetailEntry.getID().toString());
                dto.put("markStartFreeTime",current);
                dto.put("saveTime",current);
                long ctm =  controlAppSchoolEntry.getFreeTime()/60000;
                dto.put("outSchoolCanUseTime",ctm+"#"+ctm+"#"+ctm);
                dto.put("outSchoolRule","0#1#6");
                dto.put("controlType",controlAppSchoolEntry.getControlType());
            }else {
                dto.put("freeTime", 0);
                dto.put("appName",appDetailEntry.getAppName());
                dto.put("packageName", appDetailEntry.getAppPackageName());
                dto.put("id", appDetailEntry.getID().toString());
                dto.put("markStartFreeTime", current);
                dto.put("saveTime", current);
                dto.put("outSchoolCanUseTime", "0#0#0");
                dto.put("outSchoolRule", "0#1#6");
                if(objectIdList1.contains(appDetailEntry.getID())){
                    dto.put("controlType", 2);
                }else{
                    dto.put("controlType", 1);
                }
            }
            mapList.add(dto);
        }
        map.put("controlApp",mapList);
    }


    //获得默认的校管控时间
    public void getSchoolControlTime(long current,String string,int week,long dateTime,Map<String,Object> objectMap,List<PhoneSchoolTimeDTO> phoneTimeDTOs){
        Map<String,SchoolControlTimeEntry>  map = new HashMap<String, SchoolControlTimeEntry>();
        //查询默认管控
        List<SchoolControlTimeEntry> schoolControlTimeEntryList2 = schoolControlTimeDao.getEachSchoolControlSettingList(null);
        for (SchoolControlTimeEntry entry : schoolControlTimeEntryList2){
            PhoneSchoolTimeDTO phoneTimeDTO = new PhoneSchoolTimeDTO();
            if(entry.getType()==1){
                map.put(entry.getWeek()+"",entry);
                phoneTimeDTO.setCurrentTime(entry.getWeek()+"");
                phoneTimeDTO.setType(1);
            }else if(entry.getType()==2){
                long stm = DateTimeUtils.getStrToLongTime(entry.getDateFrom(), "yyyy-MM-dd");
                long etm = DateTimeUtils.getStrToLongTime(entry.getDateTo(), "yyyy-MM-dd");
                if(stm<dateTime && dateTime<etm){
                    map.put(dateTime+"",entry);
                }
                if(stm ==dateTime){
                    map.put(dateTime+"",entry);
                }
                if(etm==dateTime){
                    map.put(dateTime+"",entry);
                }
                phoneTimeDTO.setCurrentTime(entry.getDateFrom()+"="+entry.getDateTo());
                phoneTimeDTO.setType(3);
            }
            if(entry.getSchoolTimeFrom().equals("") && entry.getSchoolTimeTo().equals("")){
                phoneTimeDTO.setClassTime(entry.getBedTimeTo()+"-"+entry.getBedTimeTo());
            }else{
                phoneTimeDTO.setClassTime(entry.getSchoolTimeFrom()+"-"+entry.getSchoolTimeTo());
            }
            phoneTimeDTO.setBedTime(entry.getBedTimeFrom()+"-"+entry.getBedTimeTo());
            phoneTimeDTO.setStart("");
            phoneTimeDTO.setEnd("");
            phoneTimeDTOs.add(phoneTimeDTO);
        }
        //计算生效的结果
        SchoolControlTimeEntry schoolControlTimeEntry = map.get("0");
        if(schoolControlTimeEntry!=null){
            objectMap.put("schoolTime",schoolControlTimeEntry.getSchoolTimeFrom()+"-"+schoolControlTimeEntry.getSchoolTimeTo());
            objectMap.put("bedTime",schoolControlTimeEntry.getBedTimeFrom()+"-"+schoolControlTimeEntry.getBedTimeTo());
            if(schoolControlTimeEntry.getSchoolTimeFrom().equals("") && schoolControlTimeEntry.getSchoolTimeTo().equals("")){
                objectMap.put("schoolTime",schoolControlTimeEntry.getBedTimeTo()+"-"+schoolControlTimeEntry.getBedTimeTo());
                objectMap.put("homeTime",schoolControlTimeEntry.getBedTimeTo()+"-"+schoolControlTimeEntry.getBedTimeFrom());
            }else{
                if(schoolControlTimeEntry.getSchoolTimeFrom().equals(schoolControlTimeEntry.getBedTimeTo())){//一段
                    objectMap.put("homeTime",schoolControlTimeEntry.getSchoolTimeTo()+"-"+schoolControlTimeEntry.getBedTimeFrom());
                }else{//两段
                    objectMap.put("homeTime",schoolControlTimeEntry.getBedTimeTo()+"-"+schoolControlTimeEntry.getSchoolTimeFrom()+" "+schoolControlTimeEntry.getSchoolTimeTo()+"-"+schoolControlTimeEntry.getBedTimeFrom());
                }

            }
            long ssm = dateTime;
            if(schoolControlTimeEntry.getSchoolTimeFrom()!=null&& !schoolControlTimeEntry.getSchoolTimeFrom().equals("")){
                ssm = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry.getSchoolTimeFrom(), "yyyy-MM-dd HH:mm");
            }
            long sem = dateTime;
            if(schoolControlTimeEntry.getSchoolTimeTo()!=null&& !schoolControlTimeEntry.getSchoolTimeTo().equals("")){
                sem = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry.getSchoolTimeTo(), "yyyy-MM-dd HH:mm");
            }
            objectMap.put("isControl",false);
            if(current>ssm && current<sem){//上课时间 管控
                objectMap.put("isControl",true);
            }
        }else{
            //计算生效的结果
            SchoolControlTimeEntry schoolControlTimeEntry2 = map.get(week+"");
            if(schoolControlTimeEntry2!=null){
                objectMap.put("schoolTime",schoolControlTimeEntry2.getSchoolTimeFrom()+"-"+schoolControlTimeEntry2.getSchoolTimeTo());
                objectMap.put("bedTime",schoolControlTimeEntry2.getBedTimeFrom()+"-"+schoolControlTimeEntry2.getBedTimeTo());
                if(schoolControlTimeEntry2.getSchoolTimeFrom().equals("") && schoolControlTimeEntry2.getSchoolTimeTo().equals("")){
                    objectMap.put("schoolTime",schoolControlTimeEntry2.getBedTimeTo()+"-"+schoolControlTimeEntry2.getBedTimeTo());
                    objectMap.put("homeTime",schoolControlTimeEntry2.getBedTimeTo()+"-"+schoolControlTimeEntry2.getBedTimeFrom());
                }else{
                    if(schoolControlTimeEntry2.getSchoolTimeFrom().equals(schoolControlTimeEntry2.getBedTimeTo())){//一段
                        objectMap.put("homeTime",schoolControlTimeEntry2.getSchoolTimeTo()+"-"+schoolControlTimeEntry2.getBedTimeFrom());
                    }else{//两段
                        objectMap.put("homeTime",schoolControlTimeEntry2.getBedTimeTo()+"-"+schoolControlTimeEntry2.getSchoolTimeFrom()+" "+schoolControlTimeEntry2.getSchoolTimeTo()+"-"+schoolControlTimeEntry2.getBedTimeFrom());
                    }
                }
                long ssm = dateTime;
                if(schoolControlTimeEntry2.getSchoolTimeFrom()!=null&& !schoolControlTimeEntry2.getSchoolTimeFrom().equals("")){
                    ssm = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry2.getSchoolTimeFrom(), "yyyy-MM-dd HH:mm");
                }
                long sem = dateTime;
                if(schoolControlTimeEntry2.getSchoolTimeTo()!=null&& !schoolControlTimeEntry2.getSchoolTimeTo().equals("")){
                    sem = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry2.getSchoolTimeTo(), "yyyy-MM-dd HH:mm");
                }
                objectMap.put("isControl",false);
                if(current>ssm && current<sem){//上课时间，跳1
                    objectMap.put("isControl",true);
                }
            }else{
                objectMap.put("schoolTime","07:30-17:30");
                objectMap.put("bedTime","22:00-07:30");
                objectMap.put("homeTime","17:30-22:00");
                long ssm = DateTimeUtils.getStrToLongTime(string+" 07:30", "yyyy-MM-dd HH:mm");
                long sem = DateTimeUtils.getStrToLongTime(string+" 17:30", "yyyy-MM-dd HH:mm");
                objectMap.put("isControl",false);
                if(current>ssm && current<sem){//上课时间，跳1
                    objectMap.put("isControl",true);
                }
            }
        }
    }

    /**
     *  学校学生新登录获取所有信息
     */
    public Map<String,Object> getNewAllSchoolMessageForSon(ObjectId sonId){
        NewVersionBindRelationEntry newEntry = newVersionBindRelationDao.getBindEntry(sonId);
        if(null == newEntry) {
            return this.getNewSimpleMessageForSon();
        }
        //家长id
        ObjectId parentId = newEntry.getMainUserId();
        Map<String,Object> map = new HashMap<String, Object>();
        //可用电话记录
        List<ControlPhoneDTO> phoneDTOs = new ArrayList<ControlPhoneDTO>();
        //家长类型  系统推荐
        List<ControlPhoneEntry> entries = controlPhoneDao.getSonAllList(parentId, sonId);
        if(entries.size()>0){
            for(ControlPhoneEntry entry : entries){
                phoneDTOs.add(new ControlPhoneDTO(entry));
            }
        }
        map.put("phone",phoneDTOs);
        //管控时间
        ControlTimeEntry controlTimeEntry = controlTimeDao.getEntry(sonId, parentId);
        long timecu = 30*60*1000;
        if(controlTimeEntry != null){
            timecu = controlTimeEntry.getTime();
        }
        map.put("time",timecu/60000);
        ControlSetBackEntry setBackEntry = controlSetBackDao.getEntry();
        if(null != setBackEntry){
            map.put("backTime",setBackEntry.getBacktime());
            map.put("appTime",setBackEntry.getAppTime());
        }else{
            map.put("backTime",24*60);
            map.put("appTime",24*60);
        }
        //黑名单应用
        List<AppDetailEntry> detailEntries =  appDetailDao.getSimpleAppEntry();
        List<AppDetailDTO> detailDTOs2 = new ArrayList<AppDetailDTO>();
        for(AppDetailEntry detailEntry : detailEntries){
            detailDTOs2.add(new AppDetailDTO(detailEntry));
        }
        map.put("blackApp",detailDTOs2);
        //校管控应用列表
        //所在社群
        List<ObjectId> obList = newVersionBindService.getCommunityIdsByUserId(sonId);
        //所在学校
        List<ObjectId> schoolIdsList = schoolCommunityDao.getSchoolIdsList(obList);
        //真实学校
        List<ObjectId> trueSchoolIds =  homeSchoolDao.getSchoolObjectList(schoolIdsList);
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        String str = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,10);
        long zero = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(zero);
        int week = getWeek(zero);
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        //必须加入系统推荐
        if(trueSchoolIds.size()==0){
            if(controlTimeEntry.getControlType()==2 || controlTimeEntry.getControlType()==0){//以前为校管控或为管控
                this.sendMessage(parentId,1);
                controlTimeDao.updateTypeEntry(controlTimeEntry.getID(),1);
            }
            //走家长端家管控
            map.put("loginType",1);
            //家管控实行家长推荐应用
            //家长推荐  //可用应用列表
            ControlAppUserEntry controlAppUserEntry = controlAppUserDao.getEntry(parentId,sonId);
            if(controlAppUserEntry!= null){
                //存在取用户应用
                objectIdList.addAll(controlAppUserEntry.getAppIdList());
            }else{
                ControlAppSystemEntry controlAppSystemEntry = controlAppSystemDao.getEntry();
                ControlAppUserDTO dto = new ControlAppUserDTO();
                List<String> olist = new ArrayList<String>();
                if(controlAppSystemEntry != null){
                    List<ObjectId> ids = controlAppSystemEntry.getAppIdList();
                    for(ObjectId obid : ids){
                        olist.add(obid.toString());
                    }
                }
                dto.setAppIdList(olist);
                dto.setUserId(sonId.toString());
                dto.setParentId(parentId.toString());
                ControlAppUserEntry entry1 = dto.buildAddEntry();
                //添加系统设置
                controlAppUserDao.addEntry(entry1);
                objectIdList.addAll(controlAppSystemEntry.getAppIdList());
            }
            Set<ObjectId> set= new HashSet<ObjectId>();
            set.addAll(objectIdList);
            List<ObjectId> objectIdList1 =new ArrayList<ObjectId>();
            objectIdList1.addAll(set);
            //三方可用应用
            List<AppDetailEntry> entries2 = appDetailDao.getEntriesByIds(objectIdList1);
            List<AppDetailDTO> detailDTOs = new ArrayList<AppDetailDTO>();
            for(AppDetailEntry detailEntry : entries2){
                detailDTOs.add(new AppDetailDTO(detailEntry));
            }
            map.put("thirdApp",detailDTOs);


            //获取默认校管控
            List<PhoneSchoolTimeDTO> phoneTimeDTOs = new ArrayList<PhoneSchoolTimeDTO>();
            this.getSchoolControlTime(current, dateNowStr, week, zero, map, phoneTimeDTOs);
            map.put("controlList",phoneTimeDTOs);
            //获取默认应用管控
            this.getAppControlTime(entries2,current,map);
        }else{
            if(controlTimeEntry.getControlType()==1 || controlTimeEntry.getControlType()==0){//以前为家管控或为管控
                this.sendMessage(parentId,2);
                controlTimeDao.updateTypeEntry(controlTimeEntry.getID(),2);
            }
            //走老师端校管控
            map.put("loginType",2);
            //获得各个社区的推送应用记录
            List<ControlAppEntry> controlAppEntries = controlAppDao.getEntryListByCommunityId(obList);
            //社区推荐
            if(controlAppEntries.size()>0){
                for(ControlAppEntry controlAppEntry : controlAppEntries){
                    objectIdList.addAll(controlAppEntry.getAppIdList());
                }
            }
            //无社区推荐，加入系统推送
            if(objectIdList.size()==0){
                ControlAppSystemEntry controlAppSystemEntry = controlAppSystemDao.getEntry();
                if(controlAppSystemEntry!=null && controlAppSystemEntry.getAppIdList()!=null){
                    objectIdList.addAll(controlAppSystemEntry.getAppIdList());
                }
            }
            Set<ObjectId> set= new HashSet<ObjectId>();
            set.addAll(objectIdList);
            List<ObjectId> objectIdList1 =new ArrayList<ObjectId>();
            objectIdList1.addAll(set);
            List<AppDetailEntry> entries2 = appDetailDao.getEntriesByIds(objectIdList1);
            List<AppDetailDTO> detailDTOs = new ArrayList<AppDetailDTO>();
            for(AppDetailEntry detailEntry : entries2){
                detailDTOs.add(new AppDetailDTO(detailEntry));
            }
            map.put("thirdApp",detailDTOs);



            List<PhoneSchoolTimeDTO> phoneTimeDTOs = new ArrayList<PhoneSchoolTimeDTO>();
            this.getMoreSchoolControlTime(current, dateNowStr, week, zero, map, phoneTimeDTOs, trueSchoolIds);
            map.put("controlList",phoneTimeDTOs);
            this.getMoreAppControlTime(entries2, current, map, obList,zero);
        }
        return map;
    }
    //发送管控状态切换消息
    public void sendMessage(final ObjectId userId,final int type){
        new Thread(){
            public void run(){

                SystemMessageDao systemMessageDao = new SystemMessageDao();
                IndexPageDao indexPageDao = new IndexPageDao();
                //添加系统信息
                SystemMessageDTO dto = new SystemMessageDTO();
                dto.setType(5);
                dto.setAvatar("");
                dto.setName("");
                dto.setFileUrl("");
                dto.setSourceId("");
                if(type==2){
                    dto.setContent("您的小孩已进入校管控！");
                }else{
                    dto.setContent("您的小孩已进入家管控！");
                }
                dto.setFileType(1);
                dto.setSourceName("");
                dto.setSourceType(0);
                dto.setTitle("");
                String id = systemMessageDao.addEntry(dto.buildAddEntry());

                //添加首页记录
                IndexPageDTO dto1 = new IndexPageDTO();
                dto1.setType(CommunityType.system.getType());
                dto1.setUserId(userId.toString());
                dto1.setCommunityId(userId.toString());
                dto1.setContactId(id.toString());
                IndexPageEntry entry = dto1.buildAddEntry();
                indexPageDao.addEntry(entry);
            }
        }.start();
    }
    //多社群应用管控
    //获得默认应用管控时间
    public void getMoreAppControlTime(List<AppDetailEntry> appDetailEntries,long current,Map<String,Object> map,List<ObjectId> communityIds,long startNum){
        //获取所有非三方应用
        List<AppDetailEntry> appDetailEntries2 =  appDetailDao.getNoThreeAppList();
        appDetailEntries.addAll(appDetailEntries2);
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        for(AppDetailEntry appDetailEntry:appDetailEntries){
            objectIdList.add(appDetailEntry.getID());
        }
        //获取目前所有记录
        Map<String,ControlAppSchoolResultEntry> resultEntryMap = controlAppSchoolResultDao.getAllEntryListByCommunityId(objectIdList, communityIds);
        //获取默认配置
        Map<String,ControlAppSchoolEntry> schoolEntryMap =  controlAppSchoolDao.getEntryList(objectIdList);
        List<Map<String,Object>> mapList = new ArrayList<Map<String, Object>>();
        //不受管控的类型
        List<ObjectId> objectIdList1 = controlAppSchoolDao.getNoAppList();
        for(AppDetailEntry appDetailEntry:appDetailEntries){
            //获得 上课时间的数据
            ControlAppSchoolResultEntry controlAppSchoolResultEntry1 = resultEntryMap.get(appDetailEntry.getID().toString()+"*"+1+"*");
            //放学后仅当天
            ControlAppSchoolResultEntry controlAppSchoolResultEntry2 = resultEntryMap.get(appDetailEntry.getID().toString()+"*"+0+"*"+startNum);
            //周一到周五
            ControlAppSchoolResultEntry controlAppSchoolResultEntry3 = resultEntryMap.get(appDetailEntry.getID().toString()+"*"+0+"*1");
            //周六到周日
            ControlAppSchoolResultEntry controlAppSchoolResultEntry4 = resultEntryMap.get(appDetailEntry.getID().toString()+"*"+0+"*6");
            Map<String,Object>  dto  = new HashMap<String, Object>();
            dto.put("packageName",appDetailEntry.getAppPackageName());
            dto.put("id",appDetailEntry.getID().toString());
            dto.put("appName",appDetailEntry.getAppName());
            // 判断上课时间
            if(controlAppSchoolResultEntry1!=null){
                dto.put("freeTime",controlAppSchoolResultEntry1.getFreeTime()/60000);
                dto.put("markStartFreeTime",current);
                dto.put("saveTime",current);
                if(objectIdList1.contains(appDetailEntry.getID())){
                    dto.put("controlType",2);
                }else{
                    dto.put("controlType",controlAppSchoolResultEntry1.getControlType());
                }

            }else {
                dto.put("freeTime", 0);
                dto.put("markStartFreeTime", current);
                dto.put("saveTime", current);
                if(objectIdList1.contains(appDetailEntry.getID())){
                    dto.put("controlType", 2);
                }else{
                    dto.put("controlType", 1);
                }
            }
            StringBuffer timeStr = new StringBuffer();
            int i =0;
            //判断放学时间
            if(controlAppSchoolResultEntry2!=null){
                timeStr.append(controlAppSchoolResultEntry2.getOutSchoolCanUseTime()/60000);
                timeStr.append("#");
                i++;
            }else{
                timeStr.append("0");
                timeStr.append("#");
            }
            if(controlAppSchoolResultEntry3!=null){
                timeStr.append(controlAppSchoolResultEntry3.getOutSchoolCanUseTime()/60000);
                timeStr.append("#");
                i++;
            }else{
                timeStr.append("0");
                timeStr.append("#");
            }

            if(controlAppSchoolResultEntry4!=null){
                timeStr.append(controlAppSchoolResultEntry3.getOutSchoolCanUseTime()/60000);
                i++;
            }else{
                timeStr.append("0");
            }
            if(i==0){//无校管控记录
                ControlAppSchoolEntry controlAppSchoolEntry = schoolEntryMap.get(appDetailEntry.getID().toString()+"*0");
                if(controlAppSchoolEntry!=null){
                    long ctm =  controlAppSchoolEntry.getFreeTime()/60000;
                    timeStr  = new StringBuffer();
                    timeStr.append(ctm+"#"+ctm+"#"+ctm);
                }
            }
            dto.put("outSchoolCanUseTime",timeStr.toString());
            dto.put("outSchoolRule","0#1#6#");
            mapList.add(dto);
        }
        map.put("controlApp",mapList);
    }

    //多学校校管控
    public void getMoreSchoolControlTime(long current,String string,int week,long dateTime,Map<String,Object> objectMap,List<PhoneSchoolTimeDTO> phoneTimeDTOs,List<ObjectId> schoolIdsList){
        Map<String,List<SchoolControlTimeEntry>>  map =new HashMap<String, List<SchoolControlTimeEntry>>();
        //查询默认管控
        List<SchoolControlTimeEntry> schoolControlTimeEntryList2 = schoolControlTimeDao.getMoreSchoolControlSettingList(schoolIdsList);
        Map<String,PhoneSchoolTimeDTO> phoneSchoolTimeDTOMap = new HashMap<String, PhoneSchoolTimeDTO>();
        for (SchoolControlTimeEntry entry : schoolControlTimeEntryList2){
            PhoneSchoolTimeDTO phoneTimeDTO = new PhoneSchoolTimeDTO();
            if(entry.getType()==1){
                List<SchoolControlTimeEntry> list1 = map.get(entry.getWeek() + "");
                if(list1==null){
                    List<SchoolControlTimeEntry> list2 = new ArrayList<SchoolControlTimeEntry>();
                    list2.add(entry);
                    map.put(entry.getWeek()+"",list2);
                }else{
                    list1.add(entry);
                    map.put(entry.getWeek() + "", list1);
                }
                phoneTimeDTO.setCurrentTime(entry.getWeek()+"");
                phoneTimeDTO.setType(1);

            }else if(entry.getType()==2){
                long stm = DateTimeUtils.getStrToLongTime(entry.getDateFrom(), "yyyy-MM-dd");
                long etm = DateTimeUtils.getStrToLongTime(entry.getDateTo(), "yyyy-MM-dd");
                if(stm<dateTime && dateTime<etm){
                    List<SchoolControlTimeEntry> list1 = map.get(dateTime + "");
                    if(list1==null){
                        List<SchoolControlTimeEntry> list2 = new ArrayList<SchoolControlTimeEntry>();
                        list2.add(entry);
                        map.put(entry.getWeek()+"",list2);
                    }else{
                        list1.add(entry);
                        map.put(dateTime+"",list1);
                    }
                }
                if(stm ==dateTime){
                    List<SchoolControlTimeEntry> list1 = map.get(dateTime + "");
                    if(list1==null){
                        List<SchoolControlTimeEntry> list2 = new ArrayList<SchoolControlTimeEntry>();
                        list2.add(entry);
                        map.put(entry.getWeek()+"",list2);
                    }else{
                        list1.add(entry);
                        map.put(dateTime+"",list1);
                    }
                }
                if(etm==dateTime){
                    List<SchoolControlTimeEntry> list1 = map.get(dateTime + "");
                    if(list1==null){
                        List<SchoolControlTimeEntry> list2 = new ArrayList<SchoolControlTimeEntry>();
                        list2.add(entry);
                        map.put(entry.getWeek()+"",list2);
                    }else{
                        list1.add(entry);
                        map.put(dateTime+"",list1);
                    }
                }
                phoneTimeDTO.setCurrentTime(entry.getDateFrom()+"="+entry.getDateTo());
                phoneTimeDTO.setType(3);
            }

            phoneTimeDTO.setClassTime(entry.getSchoolTimeFrom()+"-"+entry.getSchoolTimeTo());
            phoneTimeDTO.setBedTime(entry.getBedTimeFrom()+"-"+entry.getBedTimeTo());
            phoneTimeDTO.setStart("");
            phoneTimeDTO.setEnd("");
            phoneSchoolTimeDTOMap.put(phoneTimeDTO.getCurrentTime(),phoneTimeDTO);
            //phoneTimeDTOs.add(phoneTimeDTO);
        }
        for(Map.Entry<String,PhoneSchoolTimeDTO> pen:phoneSchoolTimeDTOMap.entrySet()){
            phoneTimeDTOs.add(pen.getValue());
        }
        //计算生效的结果
        List<SchoolControlTimeEntry> schoolControlTimeEntrys = map.get("0");
        if(schoolControlTimeEntrys !=null &&  schoolControlTimeEntrys.size()>0){
            SchoolControlTimeEntry schoolControlTimeEntry = null;
            for(SchoolControlTimeEntry schoolControlTimeEntry2 : schoolControlTimeEntrys){
                if(schoolControlTimeEntry==null){
                    schoolControlTimeEntry = schoolControlTimeEntry2;
                }else{
                    long nstm = dateTime;
                    long netm = dateTime;
                    long nbsm = dateTime;
                    long nbem = dateTime;
                    long ostm = dateTime;
                    long oetm = dateTime;
                    long obsm = dateTime;
                    long obem = dateTime;
                    if(schoolControlTimeEntry2.getSchoolTimeFrom()!=null && !schoolControlTimeEntry2.getSchoolTimeFrom().equals("")){
                        nstm = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry2.getSchoolTimeFrom(), "yyyy-MM-dd HH:mm");
                    }
                    if(schoolControlTimeEntry2.getSchoolTimeTo()!=null && !schoolControlTimeEntry2.getSchoolTimeTo().equals("")){
                        netm = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry2.getSchoolTimeTo(), "yyyy-MM-dd HH:mm");
                    }
                    if(schoolControlTimeEntry2.getBedTimeFrom()!=null && !schoolControlTimeEntry2.getBedTimeFrom().equals("")){
                        nbsm = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry2.getBedTimeFrom(), "yyyy-MM-dd HH:mm");
                    }
                    if(schoolControlTimeEntry2.getBedTimeTo()!=null && !schoolControlTimeEntry2.getBedTimeTo().equals("")){
                        nbem = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry2.getBedTimeTo(), "yyyy-MM-dd HH:mm");
                    }
                    if(schoolControlTimeEntry.getSchoolTimeFrom()!=null && !schoolControlTimeEntry.getSchoolTimeFrom().equals("")){
                        ostm = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry.getSchoolTimeFrom(), "yyyy-MM-dd HH:mm");
                    }
                    if(schoolControlTimeEntry.getSchoolTimeTo()!=null && !schoolControlTimeEntry.getSchoolTimeTo().equals("")){
                        oetm = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry.getSchoolTimeTo(), "yyyy-MM-dd HH:mm");
                    }
                    if(schoolControlTimeEntry.getBedTimeFrom()!=null && !schoolControlTimeEntry.getBedTimeFrom().equals("")){
                        obsm = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry.getBedTimeFrom(), "yyyy-MM-dd HH:mm");
                    }
                    if(schoolControlTimeEntry.getBedTimeTo()!=null && !schoolControlTimeEntry.getBedTimeTo().equals("")){
                        obem = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry.getBedTimeTo(), "yyyy-MM-dd HH:mm");
                    }
                    if(nstm<ostm){
                        schoolControlTimeEntry.setSchoolTimeFrom(schoolControlTimeEntry2.getSchoolTimeFrom());
                    }
                    if(netm>oetm){
                        schoolControlTimeEntry.setSchoolTimeTo(schoolControlTimeEntry2.getSchoolTimeTo());
                    }
                    if(nbsm<obsm){
                        schoolControlTimeEntry.setBedTimeFrom(schoolControlTimeEntry2.getBedTimeFrom());
                    }
                    if(nbem>obem){
                        schoolControlTimeEntry.setBedTimeTo(schoolControlTimeEntry2.getBedTimeTo());
                    }

                }
            }
            objectMap.put("schoolTime",schoolControlTimeEntry.getSchoolTimeFrom()+"-"+schoolControlTimeEntry.getSchoolTimeTo());
            objectMap.put("bedTime",schoolControlTimeEntry.getBedTimeFrom()+"-"+schoolControlTimeEntry.getBedTimeTo());
            if(schoolControlTimeEntry.getSchoolTimeFrom().equals("") && schoolControlTimeEntry.getSchoolTimeTo().equals("")){
                objectMap.put("schoolTime",schoolControlTimeEntry.getBedTimeTo()+"-"+schoolControlTimeEntry.getBedTimeTo());
                objectMap.put("homeTime",schoolControlTimeEntry.getBedTimeTo()+"-"+schoolControlTimeEntry.getBedTimeFrom());
            }else{
                if(schoolControlTimeEntry.getSchoolTimeFrom().equals(schoolControlTimeEntry.getBedTimeTo())){//一段
                    objectMap.put("homeTime",schoolControlTimeEntry.getSchoolTimeTo()+"-"+schoolControlTimeEntry.getBedTimeFrom());
                }else{//两段
                    objectMap.put("homeTime",schoolControlTimeEntry.getBedTimeTo()+"-"+schoolControlTimeEntry.getSchoolTimeFrom()+" "+schoolControlTimeEntry.getSchoolTimeTo()+"-"+schoolControlTimeEntry.getBedTimeFrom());
                }
            }
            long ssm = dateTime;
            if(schoolControlTimeEntry.getSchoolTimeFrom()!=null&& !schoolControlTimeEntry.getSchoolTimeFrom().equals("")){
                ssm = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry.getSchoolTimeFrom(), "yyyy-MM-dd HH:mm");
            }
            long sem = dateTime;
            if(schoolControlTimeEntry.getSchoolTimeTo()!=null&& !schoolControlTimeEntry.getSchoolTimeTo().equals("")){
                sem = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry.getSchoolTimeTo(), "yyyy-MM-dd HH:mm");
            }
            objectMap.put("isControl",false);
            if(current>ssm && current<sem){//上课时间 管控
                objectMap.put("isControl",true);
            }


        }else{
            //计算生效的结果
            List<SchoolControlTimeEntry> schoolControlTimeEntryMap = map.get(week+"");
            SchoolControlTimeEntry schoolControlTimeEntry2 = null;
            for(SchoolControlTimeEntry schoolControlTimeEntry : schoolControlTimeEntryMap){
                if(schoolControlTimeEntry2==null){
                    schoolControlTimeEntry2 = schoolControlTimeEntry;
                }else{
                    long nstm = dateTime;
                    long netm = dateTime;
                    long nbsm = dateTime;
                    long nbem = dateTime;
                    long ostm = dateTime;
                    long oetm = dateTime;
                    long obsm = dateTime;
                    long obem = dateTime;
                    if(schoolControlTimeEntry.getSchoolTimeFrom()!=null && !schoolControlTimeEntry.getSchoolTimeFrom().equals("")){
                        nstm = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry.getSchoolTimeFrom(), "yyyy-MM-dd HH:mm");
                    }
                    if(schoolControlTimeEntry.getSchoolTimeTo()!=null && !schoolControlTimeEntry.getSchoolTimeTo().equals("")){
                        netm = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry.getSchoolTimeTo(), "yyyy-MM-dd HH:mm");
                    }
                    if(schoolControlTimeEntry.getBedTimeFrom()!=null && !schoolControlTimeEntry.getBedTimeFrom().equals("")){
                        nbsm = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry.getBedTimeFrom(), "yyyy-MM-dd HH:mm");
                    }
                    if(schoolControlTimeEntry.getBedTimeTo()!=null && !schoolControlTimeEntry.getBedTimeTo().equals("")){
                        nbem = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry.getBedTimeTo(), "yyyy-MM-dd HH:mm");
                    }
                    if(schoolControlTimeEntry2.getSchoolTimeFrom()!=null && !schoolControlTimeEntry2.getSchoolTimeFrom().equals("")){
                        ostm = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry2.getSchoolTimeFrom(), "yyyy-MM-dd HH:mm");
                    }
                    if(schoolControlTimeEntry2.getSchoolTimeTo()!=null && !schoolControlTimeEntry2.getSchoolTimeTo().equals("")){
                        oetm = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry2.getSchoolTimeTo(), "yyyy-MM-dd HH:mm");
                    }
                    if(schoolControlTimeEntry2.getBedTimeFrom()!=null && !schoolControlTimeEntry2.getBedTimeFrom().equals("")){
                        obsm = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry2.getBedTimeFrom(), "yyyy-MM-dd HH:mm");
                    }
                    if(schoolControlTimeEntry2.getBedTimeTo()!=null && !schoolControlTimeEntry2.getBedTimeTo().equals("")){
                        obem = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry2.getBedTimeTo(), "yyyy-MM-dd HH:mm");
                    }
                    if(nstm<ostm){
                        schoolControlTimeEntry2.setSchoolTimeFrom(schoolControlTimeEntry.getSchoolTimeFrom());
                    }
                    if(netm>oetm){
                        schoolControlTimeEntry2.setSchoolTimeTo(schoolControlTimeEntry.getSchoolTimeTo());
                    }
                    if(nbsm<obsm){
                        schoolControlTimeEntry2.setBedTimeFrom(schoolControlTimeEntry.getBedTimeFrom());
                    }
                    if(nbem>obem){
                        schoolControlTimeEntry2.setBedTimeTo(schoolControlTimeEntry.getBedTimeTo());
                    }

                }
            }
            if(schoolControlTimeEntry2!=null){
                objectMap.put("schoolTime",schoolControlTimeEntry2.getSchoolTimeFrom()+"-"+schoolControlTimeEntry2.getSchoolTimeTo());
                objectMap.put("bedTime",schoolControlTimeEntry2.getBedTimeFrom()+"-"+schoolControlTimeEntry2.getBedTimeTo());
                if(schoolControlTimeEntry2.getSchoolTimeFrom().equals("") && schoolControlTimeEntry2.getSchoolTimeTo().equals("")){
                    objectMap.put("schoolTime",schoolControlTimeEntry2.getBedTimeTo()+"-"+schoolControlTimeEntry2.getBedTimeTo());
                    objectMap.put("homeTime",schoolControlTimeEntry2.getBedTimeTo()+"-"+schoolControlTimeEntry2.getBedTimeFrom());
                }else{
                    if(schoolControlTimeEntry2.getSchoolTimeFrom().equals(schoolControlTimeEntry2.getBedTimeTo())){//一段
                        objectMap.put("homeTime",schoolControlTimeEntry2.getSchoolTimeTo()+"-"+schoolControlTimeEntry2.getBedTimeFrom());
                    }else{//两段
                        objectMap.put("homeTime",schoolControlTimeEntry2.getBedTimeTo()+"-"+schoolControlTimeEntry2.getSchoolTimeFrom()+" "+schoolControlTimeEntry2.getSchoolTimeTo()+"-"+schoolControlTimeEntry2.getBedTimeFrom());
                    }
                }
                long ssm = dateTime;
                if(schoolControlTimeEntry2.getSchoolTimeFrom()!=null&& !schoolControlTimeEntry2.getSchoolTimeFrom().equals("")){
                    ssm = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry2.getSchoolTimeFrom(), "yyyy-MM-dd HH:mm");
                }
                long sem = dateTime;
                if(schoolControlTimeEntry2.getSchoolTimeTo()!=null&& !schoolControlTimeEntry2.getSchoolTimeTo().equals("")){
                    sem = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry2.getSchoolTimeTo(), "yyyy-MM-dd HH:mm");
                }
                objectMap.put("isControl",false);
                if(current>ssm && current<sem){//上课时间，跳1
                    objectMap.put("isControl",true);
                }
            }else{
                objectMap.put("schoolTime","07:30-17:30");
                objectMap.put("homeTime","22:00-07:30");
                objectMap.put("homeTime","17:30-22:00");
                long ssm = DateTimeUtils.getStrToLongTime(string+" 07:30", "yyyy-MM-dd HH:mm");
                long sem = DateTimeUtils.getStrToLongTime(string+" 17:30", "yyyy-MM-dd HH:mm");
                objectMap.put("isControl",false);
                if(current>ssm && current<sem){//上课时间，跳1
                    objectMap.put("isControl",true);
                }
            }
        }
    }

    //修改默认应用管控
    public void updateAppControlTime(ObjectId appId,int time,int type)throws Exception{
        AppDetailEntry appDetailEntry = appDetailDao.findEntryById(appId);
        if(appDetailEntry==null){
            throw new Exception("应用已被删除!");
        }
        ControlAppSchoolEntry controlAppSchoolEntry = controlAppSchoolDao.getEntry(appId);
        if(controlAppSchoolEntry==null){
            ControlAppSchoolEntry controlAppSchoolEntry1 = new ControlAppSchoolEntry(appId,appDetailEntry.getAppPackageName(),type,time*60000,Constant.ZERO);
            controlAppSchoolDao.addEntry(controlAppSchoolEntry1);
        }else{
            controlAppSchoolEntry.setControlType(type);
            controlAppSchoolEntry.setFreeTime(time * 60000);
            controlAppSchoolDao.addEntry(controlAppSchoolEntry);
        }
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
