package com.fulaan.controlphone.service;

import com.db.appmarket.AppDetailDao;
import com.db.controlphone.ControlAppDao;
import com.db.controlphone.ControlMapDao;
import com.db.controlphone.ControlPhoneDao;
import com.db.controlphone.ControlTimeDao;
import com.db.fcommunity.CommunityDao;
import com.fulaan.appmarket.dto.AppDetailDTO;
import com.fulaan.controlphone.dto.ControlMapDTO;
import com.fulaan.controlphone.dto.ControlPhoneDTO;
import com.fulaan.mqtt.MQTTSendMsg;
import com.fulaan.newVersionBind.service.NewVersionBindService;
import com.pojo.appmarket.AppDetailEntry;
import com.pojo.controlphone.*;
import com.pojo.fcommunity.CommunityEntry;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Autowired
    private NewVersionBindService newVersionBindService;



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
                    code= code + entry1.getName()+"**";
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

    //查询推送应用（家长端）
    public List<AppDetailDTO> getCommunityAppList(ObjectId communityId){
        List<AppDetailDTO> dtos = new ArrayList<AppDetailDTO>();
        ControlAppEntry entry = controlAppDao.getEntry(communityId);
        List<ObjectId> olist = entry.getAppIdList();
        List<AppDetailEntry> entries = appDetailDao.getEntriesByIds(olist);
        if(entries.size()>0){
            for(AppDetailEntry entry2 : entries){
                dtos.add(new AppDetailDTO(entry2));
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

    public List<AppDetailDTO> getAppListForStudent(ObjectId studentId){
        List<AppDetailDTO> dtos = new ArrayList<AppDetailDTO>();
        //获得绑定的社区id
        List<ObjectId> obList = newVersionBindService.getCommunityIdsByUserId(studentId);
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
        List<AppDetailEntry> entries2 = appDetailDao.getEntriesByIds(appIds);
        if(entries2.size()>0){
            for(AppDetailEntry entry2 : entries2){
                dtos.add(new AppDetailDTO(entry2));
            }
        }
        return dtos;
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
}
