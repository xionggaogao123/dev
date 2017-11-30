package com.fulaan.controlphone.service;

import com.db.appmarket.AppDetailDao;
import com.db.controlphone.*;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.GroupDao;
import com.db.fcommunity.MemberDao;
import com.db.user.NewVersionBindRelationDao;
import com.fulaan.appmarket.dto.AppDetailDTO;
import com.fulaan.community.dto.CommunityDTO;
import com.fulaan.controlphone.dto.*;
import com.fulaan.mqtt.MQTTSendMsg;
import com.fulaan.newVersionBind.service.NewVersionBindService;
import com.fulaan.user.service.UserService;
import com.mongodb.DBObject;
import com.pojo.appmarket.AppDetailEntry;
import com.pojo.controlphone.*;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.user.NewVersionBindRelationEntry;
import com.pojo.user.UserDetailInfoDTO;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by James on 2017/11/3.
 */
@Service
public class ControlPhoneService {

    private ControlPhoneDao controlPhoneDao  = new ControlPhoneDao();

    private ControlAppDao controlAppDao = new ControlAppDao();

    private CommunityDao communityDao = new CommunityDao();

    private AppDetailDao appDetailDao=new AppDetailDao();

    private ControlTimeDao  controlTimeDao = new ControlTimeDao();

    private ControlMapDao controlMapDao = new ControlMapDao();

    private ControlAppResultDao controlAppResultDao = new ControlAppResultDao();

    private ControlMessageDao controlMessageDao = new ControlMessageDao();

    private MemberDao memberDao = new MemberDao();

    private GroupDao groupDao = new GroupDao();

    private ControlSchoolTimeDao controlSchoolTimeDao = new ControlSchoolTimeDao();

    private ControlAppUserDao controlAppUserDao = new ControlAppUserDao();

    private ControlNowTimeDao controlNowTimeDao = new ControlNowTimeDao();

    private NewVersionBindRelationDao newVersionBindRelationDao = new NewVersionBindRelationDao();

    private ControlSetBackDao controlSetBackDao = new ControlSetBackDao();

    @Autowired
    private NewVersionBindService newVersionBindService;
    @Autowired
    private UserService userService;




    //添加可用电话
    public String addControlPhone(ControlPhoneDTO dto){
        ControlPhoneEntry entry = dto.buildAddEntry();
        String id = "";
        if(entry != null){
            id = controlPhoneDao.addEntry(entry);
        }

        //向学生端推送消息
        try {
            MQTTSendMsg.sendMessage(MQTTType.phone.getEname(), dto.getUserId().toString());
        }catch (Exception e){

        }
        return id;
    }
    //添加地图实体
    public String addSimpleMapEntry(ControlMapDTO dto){
        ControlMapEntry entry = dto.buildAddEntry();
        String id = "";
        if(entry != null){
            id = controlMapDao.addEntry(entry);
        }
        return id;
    }

    /**++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/

    //添加可用电话2
    public String addControlSimplePhone(ControlPhoneDTO dto){
        ControlPhoneEntry entry = dto.buildAddEntry();
        String id = "";
        if(entry != null){
            id = controlPhoneDao.addEntry(entry);
        }

        //向学生端推送消息
        try {
            MQTTSendMsg.sendMessage(MQTTType.app.getEname(), dto.getUserId().toString());
        }catch (Exception e){

        }
        return id;
    }
    //获得包名
    public String getSimpleAppList(ObjectId userId,int type){
        //ControlPhoneEntry entry = dto.buildAddEntry();
        List<ControlPhoneEntry> entries = controlPhoneDao.getEntryListByUserId2(userId,type);
        String code = "";
        if(entries.size()>0){
            for(ControlPhoneEntry entry1 :entries ){
                if(!code.contains(entry1.getName())){
                    code= code + entry1.getName()+"##";
                }
            }
        }
        return code;
    }
    //获得电话
    public List<ControlPhoneDTO> getSimpleAppList2(ObjectId userId,int type){
        //ControlPhoneEntry entry = dto.buildAddEntry();
        List<ControlPhoneEntry> entries = controlPhoneDao.getEntryListByUserId2(userId,type);
        List<ControlPhoneDTO> dtos = new ArrayList<ControlPhoneDTO>();
        if(entries.size()>0){
            for(ControlPhoneEntry entry : entries){
                dtos.add(new ControlPhoneDTO(entry));
            }
        }
        return dtos;
    }

    /**++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
    //查询可用电话列表（家长）
    public List<ControlPhoneDTO> getControlPhoneList(ObjectId parentId,ObjectId sonId){
        List<ControlPhoneDTO> dtos = new ArrayList<ControlPhoneDTO>();
        List<ControlPhoneEntry> entries = controlPhoneDao.getEntryListByparentIdAndUserId(parentId, sonId);
        if(entries.size()>0){
            for(ControlPhoneEntry entry : entries){
                dtos.add(new ControlPhoneDTO(entry));
            }
        }
        return dtos;
    }

    //查询可用电话列表（学生）
    public List<ControlPhoneDTO> getPhoneListForStudent(ObjectId sonId){
        List<ControlPhoneDTO> dtos = new ArrayList<ControlPhoneDTO>();
        List<ControlPhoneEntry> entries = controlPhoneDao.getEntryListByUserId(sonId);
        if(entries.size()>0){
            for(ControlPhoneEntry entry : entries){
                dtos.add(new ControlPhoneDTO(entry));
            }
        }
        return dtos;
    }
    //删除可用电话
    public void  delControlPhone(ObjectId id){
        ControlPhoneEntry entry = controlPhoneDao.getEntry2(id);
        if(null != entry){
            controlPhoneDao.delEntry(id);
            //向学生端推送消息
            try {
                MQTTSendMsg.sendMessage(MQTTType.app.getEname(), entry.getUserId().toString());
            }catch (Exception e){

            }
        }
    }

    //修改可用电话
    public void updateControlPhone(ObjectId id,String name,String phone){
        controlPhoneDao.updateEntry(name,phone,id);
    }

    //查询推送应用（学生端）
    public List<AppDetailDTO> getCommunityAppList(ObjectId sonId){
        List<Map<String,Object>>  mlist = new ArrayList<Map<String, Object>>();
        List<AppDetailDTO> dtos = new ArrayList<AppDetailDTO>();
        //获得绑定的社区id
        List<ObjectId> obList = newVersionBindService.getCommunityIdsByUserId(sonId);
        //获得各个社区的推送应用记录
        List<ControlAppEntry> entries = controlAppDao.getEntryListByCommunityId(obList);
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        if(entries.size()>0){
            for(ControlAppEntry entry : entries){
                if(entry.getAppIdList()!=null){
                    objectIdList.addAll(entry.getAppIdList());
                }
            }
        }
        Set<ObjectId> set = new HashSet<ObjectId>();
        set.addAll(objectIdList);
        List<ObjectId> appIds =new ArrayList<ObjectId>();
        appIds.addAll(set);
        //查询所有的推送结果
        if(appIds.size()>0){
            List<AppDetailEntry> entries2 = appDetailDao.getEntriesByIds(appIds);
            if(entries2.size()>0){
                for(AppDetailEntry entry2 : entries2){
                    dtos.add(new AppDetailDTO(entry2));
                }
            }
        }

        return dtos;
    }
    //添加 修改 老师应用
    public void addTeaCommunityAppList(ObjectId communityId,ObjectId appId,int type){
        ControlAppEntry entry = controlAppDao.getEntry(communityId);
        if(null == entry){
            ControlAppDTO dto = new ControlAppDTO();
            List<String> olist = new ArrayList<String>();
            olist.add(appId.toString());
            dto.setAppIdList(olist);
            dto.setCommunityId(communityId.toString());
            ControlAppEntry entry1 = dto.buildAddEntry();
            controlAppDao.addEntry(entry1);
        }else{
            if(type==1){//卸载
                List<ObjectId> appIds = entry.getAppIdList();
                appIds.remove(appId);
                entry.setAppIdList(appIds);
                controlAppDao.updEntry(entry);
            }else if (type==2){//推送
                List<ObjectId> appIds = entry.getAppIdList();
                appIds.add(appId);
                entry.setAppIdList(appIds);
                controlAppDao.updEntry(entry);
            }

        }
        List<String> objectIdList = newVersionBindService.getStudentIdListByCommunityId(communityId);
        //向学生端推送消息
        try {
            MQTTSendMsg.sendMessageList(MQTTType.phone.getEname(),objectIdList);
        }catch (Exception e){

        }
    }

    //添加/修改推送应用（老师端）
    public void addCommunityAppList(ObjectId communityId,List<String> appIds){
        ControlAppEntry entry = controlAppDao.getEntry(communityId);

        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        if(appIds!=null){
            for(String str : appIds){
                objectIdList.add(new ObjectId(str));
            }
        }
        if(null == entry){
            ControlAppEntry entry1 = new ControlAppEntry();
            CommunityEntry entry2 = communityDao.findByObjectId(communityId);
            entry1.setCommunityName(entry2.getCommunityName());
            entry1.setAppIdList(objectIdList);
            entry1.setCommunityId(communityId);
            controlAppDao.addEntry(entry1);
        }else{
            entry.setAppIdList(objectIdList);
            controlAppDao.updEntry(entry);
        }
    }

    //添加/修改推送应用（家长端）
    public void addParentAppList(ObjectId parentId,ObjectId sonId,ObjectId appId,int type){
        ControlAppUserEntry entry = controlAppUserDao.getEntry(parentId,sonId);
        if(null == entry){
            ControlAppUserDTO dto = new ControlAppUserDTO();
            List<String> olist = new ArrayList<String>();
            olist.add(appId.toString());
            dto.setAppIdList(olist);
            dto.setUserId(sonId.toString());
            dto.setParentId(parentId.toString());
            ControlAppUserEntry entry1 = dto.buildAddEntry();
            controlAppUserDao.addEntry(entry1);
        }else{
            if(type==1){//卸载
                List<ObjectId> appIds = entry.getAppIdList();
                appIds.remove(appId);
                entry.setAppIdList(appIds);
                controlAppUserDao.updEntry(entry);
            }else if (type==2){//推送
                List<ObjectId> appIds = entry.getAppIdList();
                appIds.add(appId);
                entry.setAppIdList(appIds);
                controlAppUserDao.updEntry(entry);
            }

        }
        //向学生端推送消息
        try {
            MQTTSendMsg.sendMessage(MQTTType.app.getEname(), sonId.toString());
        }catch (Exception e){

        }
    }
    //家长端
    public List<Map<String,Object>> getAppListForStudent(ObjectId studentId){
        List<Map<String,Object>>  mlist = new ArrayList<Map<String, Object>>();
        List<AppDetailDTO> dtos = new ArrayList<AppDetailDTO>();
        //获得绑定的社区id
        List<ObjectId> obList = newVersionBindService.getCommunityIdsByUserId(studentId);
        List<CommunityEntry> communityEntries = communityDao.findByObjectIds(obList);
        //获得各个社区的推送应用记录
        List<ControlAppEntry> entries = controlAppDao.getEntryListByCommunityId(obList);
        Map<ObjectId,ControlAppEntry> cmap = new HashMap<ObjectId, ControlAppEntry>();
        if(entries.size()>0){
            for(ControlAppEntry entry3 : entries){
                cmap.put(entry3.getCommunityId(),entry3);
            }
        }
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        if(entries.size()>0){
            for(ControlAppEntry entry : entries){
                if(entry.getAppIdList()!=null){
                    objectIdList.addAll(entry.getAppIdList());
                }
            }
        }
        Set<ObjectId> set = new HashSet<ObjectId>();
        set.addAll(objectIdList);
        List<ObjectId> appIds =new ArrayList<ObjectId>();
        appIds.addAll(set);
        //查询所有的推送结果
        List<AppDetailEntry> entries5 = new ArrayList<AppDetailEntry>();
        if(appIds.size()>0){
            List<AppDetailEntry> entries2 = appDetailDao.getEntriesByIds(appIds);
            entries5 =entries2;
         /*   if(entries2.size()>0){
                for(AppDetailEntry entry2 : entries2){
                    dtos.add(new AppDetailDTO(entry2));
                }
            }*/
        }
        for(CommunityEntry entry4:communityEntries){
            ControlAppEntry entry2 = cmap.get(entry4.getID());
            if(entry2 != null){
                List<ObjectId> objectIds =entry2.getAppIdList();
                Map<String,Object> map =new HashMap<String, Object>();
                List<AppDetailDTO> dtoList =new ArrayList<AppDetailDTO>();
                for(AppDetailEntry dto2:entries5){
                    if(objectIds != null && objectIds.contains(dto2.getID())){
                        dtoList.add(new AppDetailDTO(dto2));
                    }
                }
                map.put("list",dtoList);
                map.put("name",entry4.getCommunityName());
                map.put("count",dtoList.size());
                mlist.add(map);
            }else{
                Map<String,Object> map =new HashMap<String, Object>();
                map.put("list",new ArrayList<AppDetailDTO>());
                map.put("name",entry4.getCommunityName());
                map.put("count",0);
                mlist.add(map);
            }

        }

        return mlist;
    }
    public void addAppTimeEntry(ObjectId userId,ObjectId parentId,int time){
        ControlTimeEntry entry = controlTimeDao.getEntry(userId, parentId);
        if(null == entry){
            ControlTimeEntry entry1 = new ControlTimeEntry();
            entry1.setParentId(parentId);
            entry1.setUserId(userId);
            entry1.setTime(time*60000);
            entry1.setIsRemove(0);
            controlTimeDao.addEntry(entry1);
        }else{
            entry.setTime(time*60000);
            controlTimeDao.updEntry(entry);
        }
        //推送孩子禁用时间
        //向学生端推送消息
        try {
            MQTTSendMsg.sendMessage(MQTTType.mi.getEname(), userId.toString());
        }catch (Exception e){

        }
    }
    //接受应用使用1情况记录表
    public void acceptAppResultList(ResultAppDTO dto,ObjectId userId){
        List<ControlAppResultDTO> dtos = dto.getAppList();
        NewVersionBindRelationEntry newEntry = newVersionBindRelationDao.getBindEntry(userId);
        long current = System.currentTimeMillis();
        //获得时间批次
        long zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        //变更最新数据
        List<ObjectId> oids = controlAppResultDao.getIsNewObjectId(userId,zero);
        if(dtos != null && dtos.size()>0){
            this.addRedDotEntryBatch(dtos,userId,newEntry.getMainUserId(),zero);
        }
        if(oids.size()>0){
            controlAppResultDao.updEntry(oids);
        }
    }

    public Map<String,Object> seacherAppResultList(ObjectId parentId,ObjectId sonId,long time){
        Map<String,Object> map = new HashMap<String, Object>();
        //防沉迷时间
        ControlTimeEntry controlTimeEntry = controlTimeDao.getEntry(sonId, parentId);
        long timecu = 0l;
        if(controlTimeEntry != null){
            timecu = controlTimeEntry.getTime();

        }
        long hours2 = (timecu % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes2 = (timecu % (1000 * 60 * 60)) / (1000 * 60);
        String timeStr = "";
        if(hours2 != 0 ){
            timeStr = timeStr + hours2+"小时";
        }
        if(minutes2 != 0){
            timeStr = timeStr + minutes2+"分钟";
        }
        map.put("time",timeStr);
        List<ControlAppResultEntry> entries = controlAppResultDao.getIsNewEntryList(sonId,time);
        List<ControlAppResultDTO> dtos = new ArrayList<ControlAppResultDTO>();
        if(entries.size()>0){
            int i = 0;
            long dtm = 0l;
            long atm = 0l;
            List<String> oid = new ArrayList<String>();
            for(ControlAppResultEntry entry : entries){
                if(i >2){
                    dtm = dtm + entry.getUseTime();
                    atm = atm + entry.getUseTime();
                }else{
                    oid.add(entry.getPackageName());
                    dtos.add(new ControlAppResultDTO(entry));
                    atm = atm + entry.getUseTime();
                }
                i++;
            }
            List<AppDetailEntry> entryList = appDetailDao.getEntriesByPackName(oid);
            for(AppDetailEntry entry3 : entryList){
                for(ControlAppResultDTO dto : dtos){
                    if(entry3.getAppPackageName().equals(dto.getPackageName())){
                        dto.setAppName(entry3.getAppName());
                        dto.setLogo(entry3.getLogo());
                    }
                }
            }
            ControlAppResultDTO entry2 = new ControlAppResultDTO();
            entry2.setAppName("其他");
            entry2.setUserTime(dtm);
            entry2.setDateTime("");
            entry2.setAppId("");
            entry2.setLogo("");
            entry2.setParentId("");
            entry2.setUserId("");
            dtos.add(entry2);
            long hours = (atm % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (atm % (1000 * 60 * 60)) / (1000 * 60);
            map.put("list",dtos);
            String timeStr2 = "";
            if(hours != 0 ){
                timeStr2 = timeStr2 + hours+"小时";
            }
            if(minutes != 0){
                timeStr2 = timeStr2 + minutes+"分钟";
            }
            map.put("allTime",timeStr2);
        }else{
            map.put("list",dtos);
            map.put("allTime","暂未使用");
        }
        return map;
    }

    //接受地图情况
    public void acceptMapResult(ControlMapDTO dto,ObjectId userId){
        NewVersionBindRelationEntry newEntry = newVersionBindRelationDao.getBindEntry(userId);
        ControlMapEntry entry = dto.buildAddEntry();
        entry.setUserId(userId);
        entry.setParentId(newEntry.getMainUserId());
        controlMapDao.addEntry(entry);
    }
    public static void main(String[] args){
       /* ControlSetBackDTO entry = new ControlSetBackDTO();
        ControlSetBackDao controlMapDao =new ControlSetBackDao();//59e71f222675642a181100fc
        long current = System.currentTimeMillis();
        //获得时间批次
        long zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        for(int i = 0;i <1;i++){
          *//*  entry.setParentId("5a00253d3d4df9241620d173");
            entry.setUserId("5a0022df3d4df9241620d15d");
            entry.setUserTime(100000l);
            entry.setAppId("5a15027294b5ea47fc6da688");
            entry.setDateTime("2017-11-22 00:00:00");
            //entry.set
            List<String> olist  = new ArrayList<String>();
            olist.add("5a15027294b5ea47fc6da688");
            entry.setAppIdList(olist);
            controlMapDao.addEntry(entry.buildAddEntry());*//*
            entry.setType(1);
            entry.setAppTime(1);
            entry.setBackTime(1);
            controlMapDao.addEntry(entry.buildAddEntry());
        }*/
        int i = 1;
        System.out.println(i);
    }
    //获得地图位置
    public Map<String,Object> getMapNow(ObjectId parentId,ObjectId sonId) {
        //地图信息
        Map<String,Object> map = new HashMap<String, Object>();
        //获得当前时间
        long current = System.currentTimeMillis();
        //获得时间批次
        long zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        ControlMapEntry entry = controlMapDao.getEntryByParentId(parentId, sonId, zero);
        if (entry != null) {
            map.put("dto",new ControlMapDTO(entry));
        } else {
            ControlMapDTO controlMapDTO =new ControlMapDTO();
            controlMapDTO.setId("");
            controlMapDTO.setUserId("");
            controlMapDTO.setParentId("");
            controlMapDTO.setLongitude("");
            controlMapDTO.setLatitude("");
            controlMapDTO.setAngle("");
            controlMapDTO.setCreateTime("");
            controlMapDTO.setDistance("");
            map.put("dto",controlMapDTO);
        }
        //亲情电话个数
        int count = controlPhoneDao.getNumber(sonId);
        map.put("phoneCount",count);
        //已推送应用数量
        //获得绑定的社区id
        List<ObjectId> obList = newVersionBindService.getCommunityIdsByUserId(sonId);
        //获得各个社区的推送应用记录
        int size = 0;
        if(obList.size()>0){
            List<ControlAppEntry> entries = controlAppDao.getEntryListByCommunityId(obList);
            if(entries.size()>0){
                Set<ObjectId> set = new HashSet<ObjectId>();
                for(ControlAppEntry entry1:entries){
                    set.addAll(entry1.getAppIdList());
                }
                size = set.size();
            }
        }
        map.put("appCount",size);
        //防沉迷时间
        ControlTimeEntry controlTimeEntry = controlTimeDao.getEntry(sonId, parentId);
        long timecu = 0l;
        if(controlTimeEntry != null){
            timecu = controlTimeEntry.getTime();

        }
        map.put("time",timecu/60000);
        //使用时间
        int useTime  = controlAppResultDao.getAllTime(sonId);
        map.put("useTime",useTime/60000);
        //剩余时间
        map.put("reTime",timecu/60000-useTime/60000);
        UserDetailInfoDTO dto = userService.getUserInfoById(sonId.toString());
        if(dto!= null){
            String name = dto.getNickName()!=null?dto.getNickName():dto.getName();
            map.put("userName",name);
        }else{
            map.put("userName","");
        }
        return map;
    }

    public List<ControlMapDTO> getMapListEntry(ObjectId parentId,ObjectId sonId,String startTime,String endTime){
        List<ControlMapDTO> dtos = new ArrayList<ControlMapDTO>();
        long sl = 0l;
        if(startTime != null && startTime != ""){
            sl = DateTimeUtils.getStrToLongTime(startTime);
        }
        long el = 0l;
        if(endTime != null && endTime != ""){
            el = DateTimeUtils.getStrToLongTime(endTime);
        }
        List<ControlMapEntry> entries = controlMapDao.getMapListEntry(parentId,sonId,sl,el);
        if(entries.size()>0){
            for(ControlMapEntry entry : entries){
                dtos.add(new ControlMapDTO(entry));
            }
        }
        return dtos;
    }
    public List<ControlMapDTO> getSimpleMapListEntry(ObjectId parentId,ObjectId sonId,String dateTime){
        List<ControlMapDTO> dtos = new ArrayList<ControlMapDTO>();
        long sl = 0l;
        if(dateTime != null && dateTime != ""){
            sl = DateTimeUtils.getStrToLongTime(dateTime, "yyyy-MM-dd");
        }
        long endTime = sl+24*60*60*1000;
        List<ControlMapEntry> entries = controlMapDao.getSimpleMapListEntry(parentId,sonId,sl,endTime);
        if(entries.size()>0){
            for(ControlMapEntry entry : entries){
                dtos.add(new ControlMapDTO(entry));
            }
        }
        return dtos;
    }

    //家长获得信息列表
    public Map<String,Object> getSonMessage(ObjectId parentId,ObjectId sonId,int page,int pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        List<ControlMessageEntry> entries = controlMessageDao.getUserResultPageList(parentId,sonId,page,pageSize);
        int count = controlMessageDao.getNumber(parentId,sonId);
        List<ControlMessageDTO> dtos = new ArrayList<ControlMessageDTO>();
        if(entries.size()>0){
            for(ControlMessageEntry entry : entries) {
                dtos.add(new ControlMessageDTO(entry));
            }
        }
        map.put("list",dtos);
        map.put("count",count);
        return map;
    }
    //学生未登陆获取默认信息
    public Map<String,Object> getSimpleMessageForSon(){
        //appDetailDao.get
        Map<String,Object> map = new HashMap<String, Object>();
        //可用应用列表
        List<AppDetailDTO> detailDTOs = new ArrayList<AppDetailDTO>();
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
        long timecu = 0l;
        map.put("time",timecu/60000);
        map.put("backTime",24*60);
        map.put("appTime",24*60);
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(zero);
        ControlSchoolTimeEntry entry2 = controlSchoolTimeDao.getEntry(1);
        if(entry2 != null){
            String stm = entry2.getStartTime();
            long sl = 0l;
            if(stm != null && stm != ""){
                sl = DateTimeUtils.getStrToLongTime(dateNowStr+" "+stm, "yyyy-MM-dd HH:mm:ss");
            }
            String etm = entry2.getEndTime();
            long el = 0l;
            if(etm != null && etm != ""){
                el = DateTimeUtils.getStrToLongTime(dateNowStr+" "+etm, "yyyy-MM-dd HH:mm:ss");
            }
            if(current>sl && current < el){
                map.put("isControl",true);
            }else{
                map.put("isControl",false);
            }
            map.put("dto",new ControlSchoolTimeDTO(entry2));
        }
        return map;
    }
    //学生登录获取所有信息
    public Map<String,Object> getAllMessageForSon(ObjectId sonId){
        NewVersionBindRelationEntry newEntry = newVersionBindRelationDao.getBindEntry(sonId);
        if(null == newEntry){
            return this.getSimpleMessageForSon();
        }

        ControlTimeEntry controlTimeEntry = controlTimeDao.getEntryByUserId(sonId);
       /* if(controlTimeEntry != null){
            ObjectId parentId = controlTimeEntry.getParentId();
        }*/
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
        if(controlTimeEntry != null){
            ObjectId parentId = controlTimeEntry.getParentId();
            ControlAppUserEntry controlAppUserEntry = controlAppUserDao.getEntry(parentId,sonId);
            if(controlAppUserEntry!= null){
                objectIdList.addAll(controlAppUserEntry.getAppIdList());
            }
        }
        Set<ObjectId> set= new HashSet<ObjectId>();
        set.addAll(objectIdList);
        List<ObjectId> objectIdList1 =new ArrayList<ObjectId>();
        objectIdList1.addAll(set);
        List<AppDetailEntry> entries2 = appDetailDao.getEntriesByIds(objectIdList1);
        /*List<AppDetailEntry> entries3 =  appDetailDao.getAllByCondition();
        entries2.addAll(entries3);*/
        List<AppDetailDTO> detailDTOs = new ArrayList<AppDetailDTO>();                                                              
        List<AppDetailDTO> detailDTOs2 = new ArrayList<AppDetailDTO>();
        for(AppDetailEntry detailEntry : entries2){
            detailDTOs.add(new AppDetailDTO(detailEntry));
        }
     /*   map.put("blackApp",detailDTOs2);
        map.put("thirdApp",detailDTOs);*/
        map.put("thirdApp",detailDTOs);
        //黑名单应用
        List<AppDetailEntry> detailEntries =  appDetailDao.getSimpleAppEntry();
        for(AppDetailEntry detailEntry : detailEntries){
            detailDTOs2.add(new AppDetailDTO(detailEntry));
        }
        map.put("blackApp",detailDTOs2);
        //可用电话记录
        List<ControlPhoneDTO> dtos = new ArrayList<ControlPhoneDTO>();
        List<ControlPhoneEntry> entries = controlPhoneDao.getEntryListByparentIdAndUserId2(sonId);
        List<ControlPhoneEntry> entries6 = controlPhoneDao.getEntryListByType();
        entries.addAll(entries6);
        if(entries.size()>0){
            for(ControlPhoneEntry entry : entries){
                dtos.add(new ControlPhoneDTO(entry));
            }
        }
        map.put("phone",dtos);
        //管控时间
        long timecu = 0l;
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
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(zero);
        ControlSchoolTimeEntry entry2 = controlSchoolTimeDao.getOtherEntry(2,dateNowStr);
        if(entry2 != null){
            String stm = entry2.getStartTime();
            long sl = 0l;
            if(stm != null && stm != ""){
                sl = DateTimeUtils.getStrToLongTime(dateNowStr+" "+stm, "yyyy-MM-dd HH:mm:ss");
            }
            String etm = entry2.getEndTime();
            long el = 0l;
            if(etm != null && etm != ""){
                el = DateTimeUtils.getStrToLongTime(dateNowStr+" "+etm, "yyyy-MM-dd HH:mm:ss");
            }
            if(current>sl && current < el){
                map.put("isControl",true);
            }else{
                map.put("isControl",false);
            }
            map.put("dto",new ControlSchoolTimeDTO(entry2));
        }else{
            //管控时间
            Calendar cal = Calendar.getInstance();
            int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
            ControlSchoolTimeEntry entry = controlSchoolTimeDao.getEntry(w);
            if(entry != null){
                String stm = entry.getStartTime();
                long sl = 0l;
                if(stm != null && stm != ""){
                    sl = DateTimeUtils.getStrToLongTime(dateNowStr+" "+stm, "yyyy-MM-dd HH:mm:ss");
                }
                String etm = entry.getEndTime();
                long el = 0l;
                if(etm != null && etm != ""){
                    el = DateTimeUtils.getStrToLongTime(dateNowStr+" "+etm, "yyyy-MM-dd HH:mm:ss");
                }
                if(current>sl && current < el){
                    map.put("isControl",true);
                }else{
                    map.put("isControl",false);
                }
                map.put("dto",new ControlSchoolTimeDTO(entry));
            }else{
                map.put("isControl",false);
                map.put("dto",new ControlSchoolTimeDTO());
            }
        }

        //特殊设置
        ControlNowTimeEntry entry3 = controlNowTimeDao.getOtherEntryByCommunityIds(dateNowStr, obList);
        if(entry3 != null){
            String stm = entry3.getStartTime();
            long sl = 0l;
            if(stm != null && stm != ""){
                sl = DateTimeUtils.getStrToLongTime(dateNowStr+" "+stm, "yyyy-MM-dd HH:mm:ss");
            }
            String etm = entry3.getEndTime();
            long el = 0l;
            if(etm != null && etm != ""){
                el = DateTimeUtils.getStrToLongTime(dateNowStr+" "+etm, "yyyy-MM-dd HH:mm:ss");
            }
            if(current>sl && current < el){
                map.put("isControl",false);
            }
        }
        return map;
    }

    //老师查询可推送应用
    public List<AppDetailDTO> getShouldAppList(ObjectId communityId,String keyword){
        List<AppDetailDTO> dtos = new ArrayList<AppDetailDTO>();
        //获取所有可推送的第三方应用
        List<AppDetailEntry> entries = appDetailDao.getThirdEntries();
        //查询该社区推送的记录
       ControlAppEntry entry = controlAppDao.getEntry(communityId);
        if(null == entry){
            for(AppDetailEntry entry1 : entries){
                AppDetailDTO dto = new AppDetailDTO(entry1);
                dto.setIsCheck(1);
                dtos.add(dto);
            }
        }else{
            for(AppDetailEntry entry1 : entries){
                if(entry.getAppIdList() != null && entry.getAppIdList().contains(entry1.getID())){
                    AppDetailDTO dto = new AppDetailDTO(entry1);
                    dto.setIsCheck(2);//卸载显示
                    dtos.add(dto);
                }else{
                    AppDetailDTO dto = new AppDetailDTO(entry1);
                    dto.setIsCheck(1);//推送显示
                    dtos.add(dto);
                }
            }
        }
        List<AppDetailDTO> dtos4 = new ArrayList<AppDetailDTO>();
        if(dtos.size()>0 && keyword != ""){
            for(AppDetailDTO appDetailDTO : dtos){
                if(appDetailDTO.getAppName().contains(keyword)){
                    dtos4.add(appDetailDTO);
                }
            }
        }
        return dtos4;
    }

    public List<AppDetailDTO> getParentAppList(ObjectId parentId,ObjectId sonId){
        //家长推荐app
        ControlAppUserEntry entry2 = controlAppUserDao.getEntry(parentId,sonId);
        List<AppDetailDTO> dtos = new ArrayList<AppDetailDTO>();
        List<ObjectId> oblist = new ArrayList<ObjectId>();
        if(entry2 != null ){
            oblist.addAll(entry2.getAppIdList());
        }

        //获取所有可推送的第三方应用
        List<AppDetailEntry> entries = appDetailDao.getThirdEntries();

        //查询该社区推送的记录
        List<ObjectId> obList = newVersionBindService.getCommunityIdsByUserId(sonId);
        List<ControlAppEntry> entry3 = controlAppDao.getEntryListByCommunityId(obList);
        Set<ObjectId> set = new HashSet<ObjectId>();
        if(entry3.size()>0){
            for(ControlAppEntry controlAppEntry : entry3){
                set.addAll(controlAppEntry.getAppIdList());
            }
        }
        List<ObjectId> capplist = new ArrayList<ObjectId>();
        capplist.addAll(set);

        List<AppDetailDTO> dtos1 = new ArrayList<AppDetailDTO>();
        List<AppDetailDTO> dtos2 = new ArrayList<AppDetailDTO>();
        List<AppDetailDTO> dtos3 = new ArrayList<AppDetailDTO>();
        if(entries.size()>0){
            for(AppDetailEntry detailEntry : entries){
                AppDetailDTO dto = new AppDetailDTO(detailEntry);
                if(oblist != null && oblist.contains(detailEntry.getID())){
                    dto.setIsCheck(1);
                    dtos1.add(dto);
                }else if(capplist != null && capplist.contains(detailEntry.getID())){
                    dto.setIsCheck(2);
                    dtos2.add(dto);
                }else{
                    dto.setIsCheck(3);
                    dtos3.add(dto);
                }
            }
        }
        dtos.addAll(dtos1);
        dtos.addAll(dtos2);
        dtos.addAll(dtos3);
        return dtos;
    }
    public List<AppDetailDTO> seacherParentAppList(ObjectId parentId,ObjectId sonId,String keyword){
        //家长推荐app
        ControlAppUserEntry entry2 = controlAppUserDao.getEntry(parentId,sonId);
        List<AppDetailDTO> dtos = new ArrayList<AppDetailDTO>();
        List<ObjectId> oblist = new ArrayList<ObjectId>();
        if(entry2 != null ){
            oblist.addAll(entry2.getAppIdList());
        }

        //获取所有可推送的第三方应用
        List<AppDetailEntry> entries = appDetailDao.getThirdEntries();

        //查询该社区推送的记录
        List<ObjectId> obList = newVersionBindService.getCommunityIdsByUserId(sonId);
        List<ControlAppEntry> entry3 = controlAppDao.getEntryListByCommunityId(obList);
        Set<ObjectId> set = new HashSet<ObjectId>();
        if(entry3.size()>0){
            for(ControlAppEntry controlAppEntry : entry3){
                set.addAll(controlAppEntry.getAppIdList());
            }
        }
        List<ObjectId> capplist = new ArrayList<ObjectId>();
        capplist.addAll(set);

        List<AppDetailDTO> dtos1 = new ArrayList<AppDetailDTO>();
        List<AppDetailDTO> dtos2 = new ArrayList<AppDetailDTO>();
        List<AppDetailDTO> dtos3 = new ArrayList<AppDetailDTO>();
        if(entries.size()>0){
            for(AppDetailEntry detailEntry : entries){
                AppDetailDTO dto = new AppDetailDTO(detailEntry);
                if(oblist != null && oblist.contains(detailEntry.getID())){
                    dto.setIsCheck(1);
                    dtos1.add(dto);
                }else if(capplist != null && capplist.contains(detailEntry.getID())){
                    dto.setIsCheck(2);
                    dtos2.add(dto);
                }else{
                    dto.setIsCheck(3);
                    dtos3.add(dto);
                }
            }
        }
        dtos.addAll(dtos1);
        dtos.addAll(dtos2);
        dtos.addAll(dtos3);
        List<AppDetailDTO> dtos4 = new ArrayList<AppDetailDTO>();
        if(dtos.size()>0 ){
            for(AppDetailDTO appDetailDTO : dtos){
                if(appDetailDTO.getAppName().contains(keyword)){
                    dtos4.add(appDetailDTO);
                }
            }
        }
        return dtos4;
    }

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
        map.put("isRen",true);
        return map;
    }

    public Map<String,Object> getAllMessageForTea(ObjectId teacherId,ObjectId communityId){
        Map<String,Object> map = new HashMap<String, Object>();
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(zero);
        //通用设置
        ControlSchoolTimeEntry entry2 = controlSchoolTimeDao.getOtherEntry(2,dateNowStr);
        if(entry2 != null){
            String stm = entry2.getStartTime();
            long sl = 0l;
            if(stm != null && stm != ""){
                sl = DateTimeUtils.getStrToLongTime(dateNowStr+" "+stm, "yyyy-MM-dd HH:mm:ss");
            }
            String etm = entry2.getEndTime();
            long el = 0l;
            if(etm != null && etm != ""){
                el = DateTimeUtils.getStrToLongTime(dateNowStr+" "+etm, "yyyy-MM-dd HH:mm:ss");
            }
            if(current>sl && current < el){
                map.put("isControl",1);
            }else{
                map.put("isControl",2);
            }
            map.put("dto",new ControlSchoolTimeDTO(entry2));
        }else{
            //通用管控时间
            Calendar cal = Calendar.getInstance();
            int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
            ControlSchoolTimeEntry entry = controlSchoolTimeDao.getEntry(w);
            if(entry != null){
                String stm = entry.getStartTime();
                long sl = 0l;
                if(stm != null && stm != ""){
                    sl = DateTimeUtils.getStrToLongTime(dateNowStr+" "+stm, "yyyy-MM-dd HH:mm:ss");
                }
                String etm = entry.getEndTime();
                long el = 0l;
                if(etm != null && etm != ""){
                    el = DateTimeUtils.getStrToLongTime(dateNowStr+" "+etm, "yyyy-MM-dd HH:mm:ss");
                }
                if(current>sl && current < el){
                    map.put("time",el-current);
                    map.put("isControl",1);
                }else{
                    map.put("time",0);
                    map.put("isControl",2);
                }
                map.put("dto",new ControlSchoolTimeDTO(entry));
            }else{
                map.put("time",0);
                map.put("isControl",2);
                map.put("dto",new ControlSchoolTimeDTO());
            }
        }
        //特殊设置
        ControlNowTimeEntry entry3 = controlNowTimeDao.getOtherEntry(dateNowStr,communityId);
        if(entry3 != null){
            String stm = entry3.getStartTime();
            long sl = 0l;
            if(stm != null && stm != ""){
                sl = DateTimeUtils.getStrToLongTime(dateNowStr+" "+stm, "yyyy-MM-dd HH:mm:ss");
            }
            String etm = entry3.getEndTime();
            long el = 0l;
            if(etm != null && etm != ""){
                el = DateTimeUtils.getStrToLongTime(dateNowStr+" "+etm, "yyyy-MM-dd HH:mm:ss");
            }
            if(current>sl && current < el){
                map.put("time",el-current);
                map.put("isControl",3);
            }else{
                map.put("time",0);
                map.put("isControl",1);
            }
            map.put("third",new ControlNowTimeDTO(entry3));
        }else{
            ControlNowTimeEntry controlNowTimeEntry = new ControlNowTimeEntry();
            map.put("third",new ControlNowTimeDTO(controlNowTimeEntry));
        }
        return map;
    }
    public List<ObjectId> getMyRoleList(ObjectId userId){
        List<ObjectId> olsit = memberDao.getGroupIdsList(userId);
        List<ObjectId> clist = new ArrayList<ObjectId>();
        List<ObjectId> mlist =   groupDao.getGroupIdsList(olsit);
        return mlist;
    }
    //老师修改管控状态
    public void deleteControlTime(ObjectId userId,ObjectId communityId){
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(zero);
        controlNowTimeDao.deleteControlTime(communityId,dateNowStr);
        List<String> objectIdList = newVersionBindService.getStudentIdListByCommunityId(communityId);
        //向学生端推送消息
        try {
            MQTTSendMsg.sendMessageList(MQTTType.phone.getEname(),objectIdList);
        }catch (Exception e){

        }
    }
    //获得今天的放学时间
    public long getBackHomeTime(long time){
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数

        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr4 = sdf3.format(zero);
        //通用设置
        ControlSchoolTimeEntry entry2 = controlSchoolTimeDao.getOtherEntry(2,dateNowStr4);
        long el = 0l;
        if(entry2 == null){
            Calendar cal = Calendar.getInstance();
            int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
            ControlSchoolTimeEntry entry = controlSchoolTimeDao.getEntry(w);
            String etm = entry.getEndTime();

            if(etm != null && etm != ""){
                el = DateTimeUtils.getStrToLongTime(dateNowStr4+" "+etm, "yyyy-MM-dd HH:mm:ss");
            }

        }else{
            String etm = entry2.getEndTime();
            if(etm != null && etm != ""){
                el = DateTimeUtils.getStrToLongTime(dateNowStr4+" "+etm, "yyyy-MM-dd HH:mm:ss");
            }
        }
        if(el ==zero ){
            el = time;
        }else if (time > el){

        }else if (el > time){
            el = time;
        }
        return el;
    }
    public void setControlTime(ObjectId userId,ObjectId communityId,int time){
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        long tentTime = time*60*1000;
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr3 = sdf2.format(zero);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(current);
       // long ent = getBackHomeTime();
        String dateNowStr2 = sdf.format(current+tentTime);
        ControlNowTimeDTO dto = new ControlNowTimeDTO();
        dto.setCommunityId(communityId.toString());
        dto.setUserId(userId.toString());
        String startTime = dateNowStr.substring(11,dateNowStr.length());
        String endTime = dateNowStr2.substring(11,dateNowStr2.length());
        dto.setDataTime(dateNowStr3);
        dto.setStartTime(startTime);
        dto.setEndTime(endTime);
        controlNowTimeDao.addEntry(dto.buildAddEntry());
        List<String> objectIdList = newVersionBindService.getStudentIdListByCommunityId(communityId);
        //向学生端推送消息
        try {
            MQTTSendMsg.sendMessageList(MQTTType.phone.getEname(),objectIdList);
        }catch (Exception e){

        }
    }


    /**
     * 批量增加应用记录
     * @param list
     */
    public void addRedDotEntryBatch(List<ControlAppResultDTO> list,ObjectId userId,ObjectId parentId,long zero) {
        List<DBObject> dbList = new ArrayList<DBObject>();
        for (int i = 0; list != null && i < list.size(); i++) {
            ControlAppResultDTO si = list.get(i);
            ControlAppResultEntry obj = si.buildAddEntry();
            obj.setUserId(userId);
            obj.setParentId(parentId);
            obj.setDateTime(zero);
            dbList.add(obj.getBaseEntry());
        }
        //导入新纪录
        if(dbList.size()>0) {
            controlAppResultDao.addBatch(dbList);
        }
    }
}
