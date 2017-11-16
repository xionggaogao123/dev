package com.fulaan.controlphone.service;

import com.db.appmarket.AppDetailDao;
import com.db.controlphone.*;
import com.db.fcommunity.CommunityDao;
import com.fulaan.appmarket.dto.AppDetailDTO;
import com.fulaan.controlphone.dto.*;
import com.fulaan.mqtt.MQTTSendMsg;
import com.fulaan.newVersionBind.service.NewVersionBindService;
import com.fulaan.user.service.UserService;
import com.mongodb.DBObject;
import com.pojo.appmarket.AppDetailEntry;
import com.pojo.controlphone.*;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.user.UserDetailInfoDTO;
import com.sys.utils.DateTimeUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        controlPhoneDao.delEntry(id);
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
    public void addAppTimeEntry(ObjectId userId,ObjectId parentId,long time){
        ControlTimeEntry entry = controlTimeDao.getEntry(userId, parentId);
        if(null == entry){
            ControlTimeEntry entry1 = new ControlTimeEntry();
            entry1.setParentId(parentId);
            entry1.setUserId(userId);
            entry1.setTime(time);
            controlTimeDao.addEntry(entry1);
        }else{
            entry.setTime(time);
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
        //变更最新数据
        List<ObjectId> oids = controlAppResultDao.getIsNewObjectId(userId);
        if(dtos != null && dtos.size()>0){
            this.addRedDotEntryBatch(dtos,userId);
        }
        if(oids.size()>0){
            controlAppResultDao.updEntry(oids);
        }
    }

    //接受地图情况
    public void acceptMapResult(ControlMapDTO dto,ObjectId userId){
        ControlMapEntry entry = dto.buildAddEntry();
        entry.setUserId(userId);
        controlMapDao.addEntry(entry);
    }
    public static void main(String[] args){
        ControlPhoneDTO entry = new ControlPhoneDTO();
        ControlPhoneDao controlMapDao =new ControlPhoneDao();//59e71f222675642a181100fc
        for(int i = 0;i <1;i++){
            entry.setType(1);
            entry.setName("客服电话");
            entry.setPhone("400800");
            entry.setUserId("59e71bedbf2e79192c36267b");
            entry.setParentId("59e71c38bf2e79192c362681");
            controlMapDao.addEntry(entry.buildAddEntry());
        }
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

    //学生登录获取所有信息
    public Map<String,Object> getAllMessageForSon(ObjectId parentId,ObjectId sonId){
        Map<String,Object> map = new HashMap<String, Object>();





        return map;
    }

    //老师查询可推送应用
    public List<AppDetailDTO> getShouldAppList(ObjectId communityId){
        List<AppDetailDTO> dtos = new ArrayList<AppDetailDTO>();








        return dtos;
    }


    /**
     * 批量增加应用记录
     * @param list
     */
    public void addRedDotEntryBatch(List<ControlAppResultDTO> list,ObjectId userId) {
        List<DBObject> dbList = new ArrayList<DBObject>();
        for (int i = 0; list != null && i < list.size(); i++) {
            ControlAppResultDTO si = list.get(i);
            ControlAppResultEntry obj = si.buildAddEntry();
            obj.setUserId(userId);
            dbList.add(obj.getBaseEntry());
        }
        //导入新纪录
        if(dbList.size()>0) {
            controlAppResultDao.addBatch(dbList);
        }
    }
}
