package com.fulaan.controlphone.service;

import com.db.appmarket.AppDetailDao;
import com.db.backstage.SchoolControlTimeDao;
import com.db.backstage.TeacherApproveDao;
import com.db.controlphone.*;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.GroupDao;
import com.db.fcommunity.MemberDao;
import com.db.jiaschool.HomeSchoolDao;
import com.db.jiaschool.SchoolCommunityDao;
import com.db.user.NewVersionBindRelationDao;
import com.fulaan.appmarket.dto.AppDetailDTO;
import com.fulaan.community.dto.CommunityDTO;
import com.fulaan.controlphone.dto.ControlAppUserDTO;
import com.fulaan.controlphone.dto.ControlPhoneDTO;
import com.fulaan.controlphone.dto.ControlSchoolTimeDTO;
import com.fulaan.controlphone.dto.PhoneTimeDTO;
import com.fulaan.newVersionBind.service.NewVersionBindService;
import com.pojo.appmarket.AppDetailEntry;
import com.pojo.backstage.SchoolControlTimeEntry;
import com.pojo.backstage.TeacherApproveEntry;
import com.pojo.controlphone.*;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.jiaschool.SchoolCommunityEntry;
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

    private ControlSchoolTimeDao controlSchoolTimeDao = new ControlSchoolTimeDao();

    private ControlTimeDao controlTimeDao = new ControlTimeDao();

    private ControlSetBackDao controlSetBackDao = new ControlSetBackDao();

    private ControlNowTimeDao controlNowTimeDao = new ControlNowTimeDao();

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
        map.put("homeList",mapList2);
        map.put("week",week);
        this.getCommunityControlTime(current,communityId,week,startNum,map);
        return map;
    }

    //获得某个社群的校管控时间
    public void getCommunityControlTime(long current,ObjectId communityId,int week,long dateTime,Map<String,Object> objectMap){
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
            long ssm = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry.getSchoolTimeFrom(), "yyyy-MM-dd HH:mm");
            long sem = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry.getSchoolTimeTo(), "yyyy-MM-dd HH:mm");
            long bsm = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry.getBedTimeFrom(), "yyyy-MM-dd HH:mm");
            long bem = DateTimeUtils.getStrToLongTime(string+" 23:59", "yyyy-MM-dd HH:mm");
            objectMap.put("type",2);
            if(current>ssm && current<sem){//上课时间，跳1
                objectMap.put("type",1);
            }
            if(current>bsm && current<bem){//睡眠时间，跳1
                objectMap.put("type",1);
            }

        }else{
            //计算生效的结果
            SchoolControlTimeEntry schoolControlTimeEntry2 = map.get(week+"");
            if(schoolControlTimeEntry2!=null){
                objectMap.put("schoolTime",schoolControlTimeEntry2.getSchoolTimeFrom()+"-"+schoolControlTimeEntry2.getSchoolTimeTo());
                objectMap.put("homeTime",schoolControlTimeEntry2.getSchoolTimeTo()+"-"+schoolControlTimeEntry2.getBedTimeFrom());
                long ssm = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry.getSchoolTimeFrom(), "yyyy-MM-dd HH:mm");
                long sem = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry.getSchoolTimeTo(), "yyyy-MM-dd HH:mm");
                long bsm = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry.getBedTimeFrom(), "yyyy-MM-dd HH:mm");
                long bem = DateTimeUtils.getStrToLongTime(string+" 23:59", "yyyy-MM-dd HH:mm");
                objectMap.put("type",2);
                if(current>ssm && current<sem){//上课时间，跳1
                    objectMap.put("type",1);
                }
                if(current>bsm && current<bem){//睡眠时间，跳1
                    objectMap.put("type",1);
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
                }
                if(current>bsm && current<bem){//睡眠时间，跳1
                    objectMap.put("type",1);
                }
            }
        }
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

    //学生新未登陆获取默认信息
    public Map<String,Object> getNewSimpleMessageForSon(){
        //appDetailDao.get
        Map<String,Object> map = new HashMap<String, Object>();
        //可用应用列表
        List<AppDetailDTO> detailDTOs = new ArrayList<AppDetailDTO>();
        ControlAppSystemEntry controlAppSystemEntry = controlAppSystemDao.getEntry();
        if(controlAppSystemEntry != null && controlAppSystemEntry.getAppIdList()!=null && controlAppSystemEntry.getAppIdList().size()>0){
            List<AppDetailEntry> detailEntries3 =  appDetailDao.getEntriesByIds(controlAppSystemEntry.getAppIdList());
            for(AppDetailEntry detailEntry : detailEntries3){
                detailDTOs.add(new AppDetailDTO(detailEntry));
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
        long timecu = 3*60*1000;//未登录3分钟使用时间
        map.put("time",timecu/60000);
        map.put("backTime",24*60);
        map.put("appTime",24*60);
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        //long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        String str = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,10);
        long zero = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(zero);
        // ControlSchoolTimeEntry entry2 = controlSchoolTimeDao.getEntry(1);
        int week = getWeek(zero);
        //获取默认校管控
        List<PhoneTimeDTO> phoneTimeDTOs = new ArrayList<PhoneTimeDTO>();
        this.getAppControlTime(current,dateNowStr,week,zero,map,phoneTimeDTOs);
        map.put("freeTime",-1);


        List<ControlSchoolTimeEntry> entryList = controlSchoolTimeDao.getAllEntryList();
        for(ControlSchoolTimeEntry ctm : entryList){
            PhoneTimeDTO phoneTimeDTO = new PhoneTimeDTO();
            if(ctm.getType()==1){
                phoneTimeDTO.setCurrentTime(ctm.getWeek()+"");
            }else if(ctm.getType()==2){
                phoneTimeDTO.setCurrentTime(ctm.getDataTime().substring(5,10));
            }else if(ctm.getType()==3){
                phoneTimeDTO.setCurrentTime(ctm.getDataTime());
            }

            phoneTimeDTO.setClassTime(ctm.getStartTime()+"-"+ctm.getEndTime());
            phoneTimeDTO.setStart("");
            phoneTimeDTO.setEnd("");
            phoneTimeDTO.setType(ctm.getType());
            phoneTimeDTOs.add(phoneTimeDTO);
        }
        map.put("controlList",phoneTimeDTOs);
        return map;
    }


    //获得某个社群的校管控时间
    public void getAppControlTime(long current,String string,int week,long dateTime,Map<String,Object> objectMap,List<PhoneTimeDTO> phoneTimeDTOs){
        Map<String,SchoolControlTimeEntry>  map = new HashMap<String, SchoolControlTimeEntry>();
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
        //计算生效的结果
        SchoolControlTimeEntry schoolControlTimeEntry = map.get("0");
        if(schoolControlTimeEntry!=null){
            objectMap.put("schoolTime",schoolControlTimeEntry.getSchoolTimeFrom()+"-"+schoolControlTimeEntry.getSchoolTimeTo());
            objectMap.put("bedTime",schoolControlTimeEntry.getBedTimeFrom()+"-"+schoolControlTimeEntry.getBedTimeTo());
            long ssm = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry.getSchoolTimeFrom(), "yyyy-MM-dd HH:mm");
            long sem = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry.getSchoolTimeTo(), "yyyy-MM-dd HH:mm");
            objectMap.put("isControl",false);
            if(current>ssm && current<sem){//上课时间 管控
                objectMap.put("isControl",true);
            }
        }else{
            //计算生效的结果
            SchoolControlTimeEntry schoolControlTimeEntry2 = map.get(week+"");
            if(schoolControlTimeEntry2!=null){
                objectMap.put("schoolTime",schoolControlTimeEntry2.getSchoolTimeFrom()+"-"+schoolControlTimeEntry2.getSchoolTimeTo());
                objectMap.put("homeTime",schoolControlTimeEntry2.getBedTimeFrom()+"-"+schoolControlTimeEntry2.getBedTimeTo());
                long ssm = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry.getSchoolTimeFrom(), "yyyy-MM-dd HH:mm");
                long sem = DateTimeUtils.getStrToLongTime(string+" "+schoolControlTimeEntry.getSchoolTimeTo(), "yyyy-MM-dd HH:mm");
                objectMap.put("isControl",false);
                if(current>ssm && current<sem){//上课时间，跳1
                    objectMap.put("isControl",true);
                }
            }else{
                objectMap.put("schoolTime","07:30-17:30");
                objectMap.put("homeTime","22:00-07:30");
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
        //ControlTimeEntry controlTimeEntry = controlTimeDao.getEntryByUserId(sonId);
        //家长推荐
        ObjectId parentId = newEntry.getMainUserId();
        //ControlTimeEntry entry = controlTimeDao.getEntry(userId, parentId);
        ControlTimeEntry controlTimeEntry = controlTimeDao.getEntry(sonId, parentId);
        Map<String,Object> map = new HashMap<String, Object>();
        //可用应用列表
        List<ObjectId> obList = newVersionBindService.getCommunityIdsByUserId(sonId);
        //获得各个社区的推送应用记录
        List<ControlAppEntry> controlAppEntries = controlAppDao.getEntryListByCommunityId(obList);
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        //社区推荐
        if(controlAppEntries.size()>0){
            for(ControlAppEntry controlAppEntry : controlAppEntries){
                objectIdList.addAll(controlAppEntry.getAppIdList());
            }
        }
        //家长推荐
        // ObjectId parentId = newEntry.getMainUserId();
        ControlAppUserEntry controlAppUserEntry = controlAppUserDao.getEntry(parentId,sonId);
        if(controlAppUserEntry!= null){
            //存在取用户应用
            objectIdList.addAll(controlAppUserEntry.getAppIdList());
        }else{
            //不存在加入系统推荐
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
        List<AppDetailEntry> entries2 = appDetailDao.getEntriesByIds(objectIdList1);
        List<AppDetailDTO> detailDTOs = new ArrayList<AppDetailDTO>();
        List<AppDetailDTO> detailDTOs2 = new ArrayList<AppDetailDTO>();
        for(AppDetailEntry detailEntry : entries2){
            detailDTOs.add(new AppDetailDTO(detailEntry));
        }
        map.put("thirdApp",detailDTOs);
        //黑名单应用
        List<AppDetailEntry> detailEntries =  appDetailDao.getSimpleAppEntry();
        for(AppDetailEntry detailEntry : detailEntries){
            detailDTOs2.add(new AppDetailDTO(detailEntry));
        }
        map.put("blackApp",detailDTOs2);
        //可用电话记录
        List<ControlPhoneDTO> dtos = new ArrayList<ControlPhoneDTO>();
        List<ControlPhoneEntry> entries = controlPhoneDao.getEntryListByparentIdAndUserId3(parentId,sonId);
        List<ControlPhoneEntry> entries6 = controlPhoneDao.getEntryListByType();
        entries.addAll(entries6);
        if(entries.size()>0){
            for(ControlPhoneEntry entry : entries){
                dtos.add(new ControlPhoneDTO(entry));
            }
        }
        map.put("phone",dtos);
        //管控时间
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
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        //long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        String str = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,10);
        long zero = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(zero);
        map.put("isControl",true);
        //特殊设置
        ControlNowTimeEntry entry3 = controlNowTimeDao.getOtherEntryByCommunityIds(dateNowStr, obList);
        if(entry3 != null){
            String stm = entry3.getStartTime();
            long sl = 0l;
            if(stm != null && !stm.equals("")){
                sl = DateTimeUtils.getStrToLongTime(dateNowStr+" "+stm, "yyyy-MM-dd HH:mm:ss");
            }
            String etm = entry3.getEndTime();
            long el = 0l;
            if(etm != null && !etm.equals("")){
                el = DateTimeUtils.getStrToLongTime(dateNowStr+" "+etm, "yyyy-MM-dd HH:mm:ss");
            }
            if(current>sl && current < el){
                map.put("isControl",false);
                map.put("freeTime",el-current);
            }else{
                map.put("freeTime",-1);
            }

        }else{
            map.put("freeTime",-1);
        }
        List<PhoneTimeDTO> phoneTimeDTOs = new ArrayList<PhoneTimeDTO>();
        List<ControlSchoolTimeEntry> entryList =  this.getStudentSchoolList(obList);
        if(entryList.size()>0){

        }else{
            entryList = controlSchoolTimeDao.getAllEntryList();
        }
        map.put("dto",this.getNowControlDto(entryList));
        for(ControlSchoolTimeEntry ctm : entryList){
            PhoneTimeDTO phoneTimeDTO = new PhoneTimeDTO();
            if(ctm.getType()==1){
                phoneTimeDTO.setCurrentTime(ctm.getWeek()+"");
            }else if(ctm.getType()==2){
                phoneTimeDTO.setCurrentTime(ctm.getDataTime().substring(5,10));
            }else if(ctm.getType()==3){
                phoneTimeDTO.setCurrentTime(ctm.getDataTime());
            }

            phoneTimeDTO.setClassTime(ctm.getStartTime()+"-"+ctm.getEndTime());
            phoneTimeDTO.setStart("");
            phoneTimeDTO.setEnd("");
            phoneTimeDTO.setType(ctm.getType());
            phoneTimeDTOs.add(phoneTimeDTO);
        }
        map.put("controlList",phoneTimeDTOs);

        //个人版本
        // ControlVersionDTO controlVersionDTO = getStudentVersion(sonId);
        //map.put("version",controlVersionDTO.getVersion());
        return map;
    }

    /**
     * 学校的管控
     * @return
     */
    public List<ControlSchoolTimeEntry> getStudentSchoolList(List<ObjectId> obList){
        List<ControlSchoolTimeEntry> newEntryList = new ArrayList<ControlSchoolTimeEntry>();
        List<SchoolCommunityEntry> schoolCommunityEntries = schoolCommunityDao.getReviewList2(obList);
        if(schoolCommunityEntries.size()==0){
            return newEntryList;
        }

        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        for(SchoolCommunityEntry schoolCommunityEntry :schoolCommunityEntries){
            if(schoolCommunityEntry.getSchoolId()!=null){
                objectIdList.add(schoolCommunityEntry.getSchoolId());
            }
        }
        //查询未删除的
        List<ObjectId> objectIdList1 =  homeSchoolDao.getSchoolObjectList(objectIdList);
        List<ControlSchoolTimeEntry> entryList = controlSchoolTimeDao.getAllSchoolEntryList(objectIdList1);

        if(entryList.size()>0){
            Set<ObjectId> set = new HashSet<ObjectId>();
            for(ControlSchoolTimeEntry controlSchoolTimeEntry:entryList){
                if(controlSchoolTimeEntry.getParentId()!=null){
                    set.add(controlSchoolTimeEntry.getParentId());
                }
            }
            if(set.size()==1){
                return entryList;
            }else{
                return getMoreThanTwoSchool2(entryList);
            }

        }
        return newEntryList;
    }

    /**
     * 暂定多学校处理
     */
    public List<ControlSchoolTimeEntry> getMoreThanTwoSchool2(List<ControlSchoolTimeEntry> entryList){
        List<ControlSchoolTimeEntry> finalEntries = new ArrayList<ControlSchoolTimeEntry>();
        Map<String,ControlSchoolTimeEntry> map= new HashMap<String, ControlSchoolTimeEntry>();

        //获得当前时间
        long current=System.currentTimeMillis();
        String str = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,10);
        long zero = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(zero);

        List<ControlSchoolTimeEntry> controlSchoolTimeEntries = new ArrayList<ControlSchoolTimeEntry>();

        //常规和特殊
        for(ControlSchoolTimeEntry controlSchoolTimeEntry : entryList){
            if(controlSchoolTimeEntry.getType()==1 || controlSchoolTimeEntry.getType()==2){
                String key = controlSchoolTimeEntry.getWeek()+"-"+controlSchoolTimeEntry.getDataTime();
                ControlSchoolTimeEntry controlSchoolTimeEntry1 = map.get(key);
                if(controlSchoolTimeEntry1==null){

                }else{
                    String stm = controlSchoolTimeEntry1.getStartTime();
                    long sl = 0l;
                    if(stm != null && !stm.equals("")){
                        sl = DateTimeUtils.getStrToLongTime(dateNowStr+" "+stm, "yyyy-MM-dd HH:mm:ss");
                    }
                    String etm = controlSchoolTimeEntry1.getEndTime();
                    long el = 0l;
                    if(etm != null && !etm.equals("")){
                        el = DateTimeUtils.getStrToLongTime(dateNowStr+" "+etm, "yyyy-MM-dd HH:mm:ss");
                    }

                    String stm2 = controlSchoolTimeEntry.getStartTime();
                    long sl2 = 0l;
                    if(stm2 != null && !stm2.equals("")){
                        sl2 = DateTimeUtils.getStrToLongTime(dateNowStr+" "+stm2, "yyyy-MM-dd HH:mm:ss");
                    }
                    String etm2 = controlSchoolTimeEntry.getEndTime();
                    long el2 = 0l;
                    if(etm2 != null && !etm2.equals("")){
                        el2 = DateTimeUtils.getStrToLongTime(dateNowStr+" "+etm2, "yyyy-MM-dd HH:mm:ss");
                    }
                    if(sl<sl2){
                        controlSchoolTimeEntry.setStartTime(DateTimeUtils.getLongToStrTimeTwo(sl).substring(11,19));
                    }

                    if(el>el2){
                        controlSchoolTimeEntry.setEndTime(DateTimeUtils.getLongToStrTimeTwo(el).substring(11, 19));
                    }
                }
                map.put(key,controlSchoolTimeEntry);
            }else{
                controlSchoolTimeEntries.add(controlSchoolTimeEntry);
            }
        }
        //时间段


        for(String key : map.keySet()){
            ControlSchoolTimeEntry mapValue = map.get(key);
            finalEntries.add(mapValue);
        }
        finalEntries.addAll(controlSchoolTimeEntries);
        return finalEntries;
    }


    public ControlSchoolTimeDTO getNowControlDto(List<ControlSchoolTimeEntry> controlSchoolTimeEntries){
        //List<ControlSchoolTimeEntry> controlSchoolTimeEntries = controlSchoolTimeDao.getAllSchoolEntryList(communityIds);
        Map<ObjectId,ControlSchoolTimeEntry> map1 = new HashMap<ObjectId, ControlSchoolTimeEntry>();//时间段配置
        Map<ObjectId,ControlSchoolTimeEntry> map2 = new HashMap<ObjectId, ControlSchoolTimeEntry>();//特殊配置
        Map<ObjectId,ControlSchoolTimeEntry> map3 = new HashMap<ObjectId, ControlSchoolTimeEntry>();//周常配置
        //通用管控时间
        Calendar cal = Calendar.getInstance();
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if(w==0){
            w= 7;
        }
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        String str = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,10);
        ObjectId oid = new ObjectId();
        for(ControlSchoolTimeEntry controlSchoolTimeEntry : controlSchoolTimeEntries){
            if(controlSchoolTimeEntry.getType()==3){//时间段
                String[] arg = controlSchoolTimeEntry.getDataTime().split("=");
                if(arg.length==2){
                    String startStr = arg[0];
                    String endStr = arg[1];
                    long sl = 0l;
                    if(startStr != null && !startStr.equals("")){
                        sl = DateTimeUtils.getStrToLongTime(startStr+" "+"00:00:00", "yyyy-MM-dd HH:mm:ss");
                    }
                    long el = 0l;
                    if(endStr != null && !endStr.equals("")){
                        el = DateTimeUtils.getStrToLongTime(endStr+" "+"23:59:59", "yyyy-MM-dd HH:mm:ss");
                    }
                    if(current>sl && current < el){
                        map1.put(oid,controlSchoolTimeEntry);
                    }
                }
            }else if(controlSchoolTimeEntry.getType()==2 && controlSchoolTimeEntry.getDataTime() != null && controlSchoolTimeEntry.getDataTime().equals(str)){
                map2.put(oid,controlSchoolTimeEntry);//特殊
            }else if(controlSchoolTimeEntry.getType()==1 && controlSchoolTimeEntry.getWeek()==w){
                map3.put(oid,controlSchoolTimeEntry);//周常
            }
        }
        //获取应该执行的设置
        ControlSchoolTimeEntry controlSchoolTimeEntry = new ControlSchoolTimeEntry();
        if(oid !=null && map1.get(oid)!=null){
            controlSchoolTimeEntry = map1.get(oid);
        }else if(oid !=null && map2.get(oid)!=null){
            controlSchoolTimeEntry = map2.get(oid);
        }else if(oid !=null && map3.get(oid)!=null){
            controlSchoolTimeEntry = map3.get(oid);
        }else{
            controlSchoolTimeEntry = controlSchoolTimeDao.getEntry(1);
        }
        return new ControlSchoolTimeDTO(controlSchoolTimeEntry);
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
