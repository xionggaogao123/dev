package com.fulaan.controlphone.service;

import cn.jiguang.commom.utils.StringUtils;
import com.db.appmarket.AppDetailDao;
import com.db.backstage.TeacherApproveDao;
import com.db.business.ModuleTimeDao;
import com.db.business.VersionOpenDao;
import com.db.controlphone.*;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.GroupDao;
import com.db.fcommunity.MemberDao;
import com.db.groupchatrecord.RecordChatPersonalDao;
import com.db.jiaschool.HomeSchoolDao;
import com.db.jiaschool.SchoolAppDao;
import com.db.jiaschool.SchoolCommunityDao;
import com.db.user.NewVersionBindRelationDao;
import com.fulaan.appmarket.dto.AppDetailDTO;
import com.fulaan.cache.CacheHandler;
import com.fulaan.community.dto.CommunityDTO;
import com.fulaan.controlphone.dto.*;
import com.fulaan.mqtt.MQTTSendMsg;
import com.fulaan.newVersionBind.service.NewVersionBindService;
import com.fulaan.operation.dto.GroupOfCommunityDTO;
import com.fulaan.user.service.UserService;
import com.mongodb.DBObject;
import com.pojo.app.SessionValue;
import com.pojo.appmarket.AppDetailEntry;
import com.pojo.backstage.TeacherApproveEntry;
import com.pojo.business.VersionOpenEntry;
import com.pojo.controlphone.*;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.instantmessage.ApplyTypeEn;
import com.pojo.jiaschool.SchoolAppEntry;
import com.pojo.jiaschool.SchoolCommunityEntry;
import com.pojo.user.NewVersionBindRelationEntry;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
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

    private ControlAppSystemDao controlAppSystemDao = new ControlAppSystemDao();

    private TeacherApproveDao teacherApproveDao = new TeacherApproveDao();

    private RecordChatPersonalDao recordChatPersonalDao =  new RecordChatPersonalDao();

    private SchoolCommunityDao schoolCommunityDao = new SchoolCommunityDao();

    private SchoolAppDao schoolAppDao = new SchoolAppDao();

    private HomeSchoolDao homeSchoolDao = new HomeSchoolDao();

    private ControlVersionDao controlVersionDao = new ControlVersionDao();

    private ControlStudentResultDao controlStudentResultDao = new ControlStudentResultDao();

    private ModuleTimeDao moduleTimeDao =  new ModuleTimeDao();
    private VersionOpenDao versionOpenDao = new VersionOpenDao();

    private ControlShareDao controlShareDao = new ControlShareDao();

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
        long current = System.currentTimeMillis();
        //向学生端推送消息
        try {
            MQTTSendMsg.sendMessage(MQTTType.phone.getEname(), dto.getUserId().toString(),current);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateString = formatter.format(new Date(current));
            String scontent = dateString + MQTTType.phone.getEname();
            this.addControlVersion(entry.getUserId(),entry.getParentId(),scontent,2);
            controlTimeDao.delEntry(new ObjectId(dto.getUserId()),current);
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
        long current = System.currentTimeMillis();
        //向学生端推送消息
        try {
            MQTTSendMsg.sendMessage(MQTTType.app.getEname(), dto.getUserId().toString(),current);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateString = formatter.format(new Date(current));
            String scontent = dateString + MQTTType.app.getEname();
            this.addControlVersion(entry.getUserId(),entry.getParentId(),scontent,2);
            controlTimeDao.delEntry(new ObjectId(dto.getUserId()),current);
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
            long current = System.currentTimeMillis();
            //向学生端推送消息
            try {
                MQTTSendMsg.sendMessage(MQTTType.app.getEname(), entry.getUserId().toString(),current);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                String dateString = formatter.format(new Date(current));
                String scontent = dateString + MQTTType.app.getEname();
                this.addControlVersion(entry.getUserId(),entry.getParentId(),scontent,2);
                controlTimeDao.delEntry(entry.getUserId(),current);
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
    public void addTeaCommunityAppList(ObjectId userId,ObjectId communityId,ObjectId appId,int type){
        ControlAppEntry entry = controlAppDao.getEntry(userId,communityId);
        if(null == entry){
            ControlAppDTO dto = new ControlAppDTO();
            List<String> olist = new ArrayList<String>();
            olist.add(appId.toString());
            dto.setAppIdList(olist);
            dto.setCommunityId(communityId.toString());
            dto.setUserId(userId.toString());
            ControlAppEntry entry1 = dto.buildAddEntry();
            controlAppDao.addEntry(entry1);
            //社区推送发送记录
            moduleTimeDao.addEntry(userId, ApplyTypeEn.school.getType(),communityId);
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
                //社区推送发送记录
                moduleTimeDao.addEntry(userId, ApplyTypeEn.school.getType(),communityId);
            }

        }
        long current = System.currentTimeMillis();
        List<String> objectIdList = newVersionBindService.getStudentIdListByCommunityId(communityId);
        //向学生端推送消息
        try {
            MQTTSendMsg.sendMessageList(MQTTType.phone.getEname(),objectIdList,current);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateString = formatter.format(new Date(current));
            String scontent = dateString + MQTTType.phone.getEname();
            this.addControlVersion(communityId,userId,scontent,2);
            List<ObjectId> oids = new ArrayList<ObjectId>();
            for(String str : objectIdList){
                oids.add(new ObjectId(str));
            }
            controlTimeDao.delAllEntry(oids, current);
        }catch (Exception e){

        }
    }

    //添加/修改推送应用（老师端）
    public void addCommunityAppList(ObjectId userId,ObjectId communityId,List<String> appIds){
        ControlAppEntry entry = controlAppDao.getEntry(userId,communityId);

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
            entry1.setUserId(userId);
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
            //社区推送发送记录
           // moduleTimeDao.addEntry(parentId, ApplyTypeEn.school.getType());
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
                //社区推送发送记录
              //  moduleTimeDao.addEntry(parentId, ApplyTypeEn.school.getType());
            }

        }
        long current = System.currentTimeMillis();
        //向学生端推送消息
        try {
            MQTTSendMsg.sendMessage(MQTTType.app.getEname(), sonId.toString(),current);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateString = formatter.format(new Date(current));
            String scontent = dateString + MQTTType.app.getEname();
            this.addControlVersion(sonId,parentId,scontent,2);
            controlTimeDao.delEntry(sonId, current);
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
        long current = System.currentTimeMillis();
        try {
            MQTTSendMsg.sendMessage(MQTTType.mi.getEname(), userId.toString(),current);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateString = formatter.format(new Date(current));
            String scontent = dateString + MQTTType.mi.getEname();
            this.addControlVersion(userId,parentId,scontent,2);
            controlTimeDao.delEntry(userId, current);
        }catch (Exception e){

        }
    }
    //接受应用使用1情况记录表
    public long acceptOldAppResultList(ResultAppDTO dto,ObjectId userId){
        List<ControlAppResultDTO> dtos = dto.getAppList();
        NewVersionBindRelationEntry newEntry = newVersionBindRelationDao.getBindEntry(userId);
        long current = System.currentTimeMillis();
        //获得时间批次(时间批次)
        String str = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,10);
        long zero = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
        //long zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        //分割点
        long jiedian = zero+8*60*60*1000;//今天零点零分零秒的毫秒数
        long startTime = 0l;
        long endTime = 0l;
        if(current>=jiedian){
            startTime = jiedian;
            endTime = jiedian+ 24*60*60*1000;
        }else{
            startTime = jiedian - 24*60*60*1000;
            endTime = jiedian;
        }
        //变更最新数据
        List<ObjectId> oids = controlAppResultDao.getIsNewObjectId(userId,startTime,endTime);
        if(dtos != null && dtos.size()>0){
            this.addRedDotEntryBatch(dtos,userId,newEntry.getMainUserId(),current,dto.getAddiction());
        }
        if(oids.size()>0){
            //controlAppResultDao.updEntry(oids);
            //修改
            controlAppResultDao.updEntry(oids);
        }
        ControlTimeEntry entr = controlTimeDao.getEntryByUserId(userId);
        if(entr ==null){
            return 0l;
        }
        return entr.getBackTime();
    }

    //新接受应用使用1情况记录表
    public long acceptAppResultList(ResultAppDTO dto,ObjectId userId){
        List<ControlAppResultDTO> dtos = dto.getAppList();
        NewVersionBindRelationEntry newEntry = newVersionBindRelationDao.getBindEntry(userId);
        if(newEntry==null || newEntry.getMainUserId()==null){
            return 0l;
        }
        ObjectId parentId = newEntry.getMainUserId();
        long current = System.currentTimeMillis();
        //获得时间批次(时间批次)
        String str = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,10);
        long zero = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
        //分割点
        long jiedian = zero+8*60*60*1000;//今天零点零分零秒的毫秒数
        long datetime = 0l;
        if(current>=jiedian){
            //今日
            datetime = zero;
        }else{
            //昨日
            datetime = zero- 24*60*60*1000;
        }
        if(dtos != null && dtos.size()>0){
            this.addRedDotEntryBatch(dtos,userId,parentId,current,dto.getAddiction());
        }
        //变更最新数据
        ControlStudentResultEntry controlStudentResultEntry = controlStudentResultDao.getEntry(userId,parentId,datetime);
        if(controlStudentResultEntry==null){
            //添加
            ControlStudentResultEntry entry3 = new ControlStudentResultEntry(parentId,userId,dto.getAddiction(),current,current,0,datetime);
            controlStudentResultDao.addEntry(entry3);
        }else{
            //修改
            controlStudentResultDao.updateEntry(controlStudentResultEntry.getID(),dto.getAddiction(), current,0,current);
        }
        ControlTimeEntry entr = controlTimeDao.getEntryByUserId(userId);
        if(entr ==null){
            return 0l;
        }
        long tt = entr.getBackTime();
        entr = null;
        return tt;
    }

    public long acceptNewAppResultList(ResultNewAppDTO dto,ObjectId userId){//最新
        List<ControlAppResultDTO> dtos = dto.getAppList();
        NewVersionBindRelationEntry newEntry = newVersionBindRelationDao.getBindEntry(userId);
        if(newEntry==null || newEntry.getMainUserId()==null){
            return 0l;
        }
        ObjectId parentId = newEntry.getMainUserId();
        long current = System.currentTimeMillis();
        //获得时间批次(时间批次)
        String str = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,10);
        long zero = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
        //分割点
        long jiedian = zero+8*60*60*1000;//今天零点零分零秒的毫秒数
        long datetime = 0l;
        if(current>=jiedian){
            //今日
            datetime = zero;
        }else{
            //昨日
            datetime = zero- 24*60*60*1000;
        }
        if(dtos != null && dtos.size()>0){
            this.addRedDotEntryBatch(dtos,userId,parentId,current,dto.getAddiction());
        }
        //变更最新数据
        ControlStudentResultEntry controlStudentResultEntry = controlStudentResultDao.getEntry(userId,parentId,datetime);
        if(controlStudentResultEntry==null){
            //添加
            ControlStudentResultEntry entry3 = new ControlStudentResultEntry(parentId,userId,dto.getAddiction(),current,current,dto.getElectric(),datetime);
            controlStudentResultDao.addEntry(entry3);
        }else{
            //修改
            controlStudentResultDao.updateEntry(controlStudentResultEntry.getID(),dto.getAddiction(), current,dto.getElectric(),current);
        }
        ControlTimeEntry entr = controlTimeDao.getEntryByUserId(userId);
        if(entr ==null){
            return 0l;
        }
        long tt = entr.getBackTime();
        entr = null;
        return tt;
    }

    public Map<String,Object> seacherAppResultList(ObjectId parentId,ObjectId sonId,long time){
        Map<String,Object> map = new HashMap<String, Object>();
        //防沉迷时间
        ControlTimeEntry controlTimeEntry = controlTimeDao.getEntry(sonId, parentId);
        long timecu = 30*60*1000;
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
        List<ControlAppResultDTO> dtos = new ArrayList<ControlAppResultDTO>();
        //修改   todo
      /*  long startTime = time +8*60*60*1000;
        long endTime = time +8*60*60*1000 + 24*60*60*1000;
        List<ControlAppResultEntry> entries = controlAppResultDao.getIsNewEntryList(sonId,startTime,endTime);*/
        //查询最新使用使用时间
        ControlStudentResultEntry entry6 = controlStudentResultDao.getEntry(sonId, parentId, time);
        if(entry6==null){
            map.put("list",dtos);
            map.put("allTime","暂未使用");
            return map;
        }
        List<ControlAppResultEntry> entries = controlAppResultDao.getNewNewEntryList(sonId, parentId, entry6.getNewAppTime());
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

    /**
     * 黑名单应用使用时间统计
     * @param parentId
     * @param sonId
     * @param time
     * @return
     */
    public Map<String,Object> seacherAppResultList2(ObjectId parentId,ObjectId sonId,long time){
        Map<String,Object> map = new HashMap<String, Object>();
        //防沉迷时间
        ControlTimeEntry controlTimeEntry = controlTimeDao.getEntry(sonId, parentId);
        long timecu = 30*60*1000;
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
        long startTime = time +8*60*60*1000;
        long endTime = time +8*60*60*1000 + 24*60*60*1000;
        //查询黑名单使用时间
        List<AppDetailEntry> detailEntries =  appDetailDao.getSimpleAppEntry();
        List<String> oisd = new ArrayList<String>();
        for(AppDetailEntry appDetailEntry:detailEntries){
            oisd.add(appDetailEntry.getAppPackageName());
        }
        List<ControlAppResultEntry> entries = controlAppResultDao.getBlackIsNewEntryList(oisd,sonId,startTime,endTime);
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
            /*ControlAppResultDTO entry2 = new ControlAppResultDTO();
            entry2.setAppName("其他");
            entry2.setUserTime(dtm);
            entry2.setDateTime("");
            entry2.setAppId("");
            entry2.setLogo("");
            entry2.setParentId("");
            entry2.setUserId("");*/
            //dtos.add(entry2);
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

    public Map<String,Object> seacherAppResultList3(ObjectId parentId,ObjectId sonId,long time){
        Map<String,Object> map = new HashMap<String, Object>();
        //防沉迷时间
        ControlTimeEntry controlTimeEntry = controlTimeDao.getEntry(sonId, parentId);
        long timecu = 30*60*1000;
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
        //map.put("intTime",timecu);
        List<ControlAppResultDTO> dtos = new ArrayList<ControlAppResultDTO>();
        //修改 TODO
        /*long startTime = time +8*60*60*1000;
        long endTime = time +8*60*60*1000 + 24*60*60*1000;
        List<ControlAppResultEntry> entries = controlAppResultDao.getIsNewEntryList(sonId,startTime,endTime);*/
        //查询最新使用使用时间
        //查询最新使用使用时间
        ControlStudentResultEntry entry6 = controlStudentResultDao.getEntry(sonId, parentId, time);
        if(entry6==null){
            map.put("list",dtos);
            map.put("allTime","暂未使用");
            return map;
        }
        List<ControlAppResultEntry> entries = controlAppResultDao.getLinNewNewEntryList(sonId, parentId, entry6.getNewAppTime());
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
           /* ControlAppResultDTO entry2 = new ControlAppResultDTO();
            entry2.setAppName("其他");
            entry2.setUserTime(dtm);
            entry2.setDateTime("");
            entry2.setAppId("");
            entry2.setLogo("");
            entry2.setParentId("");
            entry2.setUserId("");
            dtos.add(entry2);*/
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

    public Map<String,Object> seacherAppResultListFour(ObjectId parentId,ObjectId sonId,long time){
        Map<String,Object> map = new HashMap<String, Object>();
        //防沉迷时间
        ControlTimeEntry controlTimeEntry = controlTimeDao.getEntry(sonId, parentId);
        long timecu = 30*60*1000;
        if(controlTimeEntry != null){
            timecu = controlTimeEntry.getTime();
        }
        if(timecu==1000 * 60 * 60 * 24){
            map.put("time","24小时");
        }else{
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
        }
        //map.put("intTime",timecu);
        List<ControlAppResultDTO> dtos = new ArrayList<ControlAppResultDTO>();
        //修改 TODO
        /*long startTime = time +8*60*60*1000;
        long endTime = time +8*60*60*1000 + 24*60*60*1000;
        List<ControlAppResultEntry> entries = controlAppResultDao.getIsNewEntryList(sonId,startTime,endTime);*/
        //接口打开
        int status = getVersion("controlTime");
        map.put("status",status);
        //查询最新使用使用时间
        ControlStudentResultEntry entry6 = controlStudentResultDao.getEntry(sonId, parentId, time);
        if(entry6==null){
            map.put("list",dtos);
            map.put("allTime","暂未使用");
            return map;
        }
        List<ControlAppResultEntry> entries = controlAppResultDao.getLinNewNewEntryList(sonId, parentId, entry6.getNewAppTime());
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
           /* ControlAppResultDTO entry2 = new ControlAppResultDTO();
            entry2.setAppName("其他");
            entry2.setUserTime(dtm);
            entry2.setDateTime("");
            entry2.setAppId("");
            entry2.setLogo("");
            entry2.setParentId("");
            entry2.setUserId("");
            dtos.add(entry2);*/
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

    public int getVersion(String moduleName){
        VersionOpenEntry versionOpenEntry = versionOpenDao.getEntry(moduleName);
        if(versionOpenEntry!=null){
            return versionOpenEntry.getModuleType();
        }
        return 0;
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
        String dateTime = "2018-01-09";
        long dTm = 0l;
        if(dateTime != null && !dateTime.equals("")){
            dTm = DateTimeUtils.getStrToLongTime(dateTime, "yyyy-MM-dd");
        }
        long current = 1515456000000l;
        String str = DateTimeUtils.getLongToStrTimeTwo(current).substring(0, 11);
        long strNum = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
        //long current2  = 1546041600000l;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(current));
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        // long zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        //long jiedian = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset()+8*60*60*1000;//今天八点的时间
        if(dTm==strNum){
            if(hour<8){
                dTm = dTm - 24*60*60*1000;
            }
        }
        System.out.println(dTm);
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
       /* int i = 1;
        String str = getNewLogo("http://www.fulaan.com/static/images/community/upload.png");
        String str2 = "http://www.fulaan.com/static/images/community/upload.png";
        String str3 = str2.replace("http://www.fulaan.com/","http://appapi.jiaxiaomei.com/");

        System.out.println(str);*/
    }
    public ControlMapDTO getStudentMap(ObjectId parentId,ObjectId sonId){
        long current = System.currentTimeMillis();
        //获得时间批次
        String str = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,10);
        long zero = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
        ControlMapEntry entry = controlMapDao.getEntryByParentId(parentId, sonId, zero);
        if (entry != null) {
            return new ControlMapDTO(entry);
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
            return controlMapDTO;
        }
    }
    //获得地图位置
    public Map<String,Object> getMapNow(ObjectId parentId,ObjectId sonId) {
        //地图信息
        Map<String,Object> map = new HashMap<String, Object>();
        //获得当前时间
        long current = System.currentTimeMillis();
        //获得时间批次
        //long zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        String str = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,10);
        long zero = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
        //分割点
        long jiedian = zero+8*60*60*1000;//今天零点零分零秒的毫秒数
        /*long startTime = 0l;
        long endTime = 0l;
        if(current>=jiedian){
            startTime = jiedian;
            endTime = jiedian+ 24*60*60*1000;
        }else{
            startTime = jiedian - 24*60*60*1000;
            endTime = jiedian;
        }*/
        long datetime = 0l;
        if(current>=jiedian){
            //今日
            datetime = zero;
        }else{
            //昨日
            datetime = zero- 24*60*60*1000;
        }
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
        int count = controlPhoneDao.getParentNumber(parentId, sonId);
        map.put("phoneCount",count);
        //已推送应用数量
        //获得绑定的社区id
        List<ObjectId> obList = newVersionBindService.getCommunityIdsByUserId(sonId);
        //获得各个社区的推送应用记录
        int size = 0;
        if(obList.size()>0){
            List<ControlAppEntry> entries = controlAppDao.getEntryListByCommunityId(obList);
            ControlAppUserEntry entryList2 = controlAppUserDao.getEntry(parentId,sonId);
            Set<ObjectId> set = new HashSet<ObjectId>();
            for(ControlAppEntry entry1:entries){
                if(entry1 !=null && entry1.getAppIdList() != null) {
                    set.addAll(entry1.getAppIdList());
                }
            }
            if(entryList2 !=null && entryList2.getAppIdList() != null){
                set.addAll(entryList2.getAppIdList());
            }
            List<ObjectId> objectIds = new ArrayList<ObjectId>();
            objectIds.addAll(set);
            List<AppDetailEntry> detailEntries3 =  appDetailDao.getEntriesByIds(objectIds);
            size = detailEntries3.size();
        }else{
            ControlAppUserEntry entryList2 = controlAppUserDao.getEntry(parentId,sonId);
            Set<ObjectId> set = new HashSet<ObjectId>();
            if(entryList2 !=null && entryList2.getAppIdList() != null){
                set.addAll(entryList2.getAppIdList());
            }
            size = set.size();
        }
        map.put("appCount",size);
        //防沉迷时间
        ControlTimeEntry controlTimeEntry = controlTimeDao.getEntry(sonId, parentId);
        long timecu = 30*60*1000;
        if(controlTimeEntry != null){
            timecu = controlTimeEntry.getTime();

        }
        map.put("time",timecu/60000);
        //使用时间
        //long useTime  = controlAppResultDao.getUserAllTime(sonId, startTime,endTime);
        ControlStudentResultEntry controlStudentResultEntry = controlStudentResultDao.getEntry(sonId,parentId,datetime);
        long useTime = 0l;
        if(controlStudentResultEntry!=null){
            useTime = controlStudentResultEntry.getNewAppUser();
        }
        map.put("useTime",useTime/60000);
        //剩余时间
        if(timecu/60000-useTime/60000 <0){
            map.put("reTime",0);
        }else{
            map.put("reTime",timecu/60000-useTime/60000);
        }
        UserDetailInfoDTO dto = userService.getUserInfoById(sonId.toString());
        if(dto!= null){
            String name = dto.getNickName()!=null?dto.getNickName():dto.getName();
            map.put("userName",name);
        }else{
            map.put("userName","");
        }
        int count2=recordChatPersonalDao.countChatEntries(sonId);
        map.put("chatCount",count2);
        //当前版本
        ControlVersionDTO controlVersionDTO = getSimpleUserVersion(parentId, sonId);
        map.put("version",controlVersionDTO.getVersion());
        return map;
    }

    //获得地图位置
    public Map<String,Object> getNewMapNow(ObjectId parentId,ObjectId sonId) {
        //地图信息
        Map<String,Object> map = new HashMap<String, Object>();
        //获得当前时间
        long current = System.currentTimeMillis();
        //获得时间批次
        //long zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        String str = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,10);
        long zero = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
        //分割点
        long jiedian = zero+8*60*60*1000;//今天零点零分零秒的毫秒数
        //修改  todo
      /*  long startTime = 0l;
        long endTime = 0l;
        if(current>=jiedian){
            startTime = jiedian;
            endTime = jiedian+ 24*60*60*1000;
        }else{
            startTime = jiedian - 24*60*60*1000;
            endTime = jiedian;
        }*/
        long datetime = 0l;
        if(current>=jiedian){
            //今日
            datetime = zero;
        }else{
            //昨日
            datetime = zero- 24*60*60*1000;
        }
       // ControlMapEntry entry = controlMapDao.getEntryByParentId(parentId, sonId, zero);
       /* if (entry != null) {
            map.put("dto",new ControlMapDTO(entry));
        } else {*/
        //废弃数据
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
        //}
        //亲情电话个数
        int count = controlPhoneDao.getParentNumber(parentId, sonId);
        map.put("phoneCount",count);
        //已推送应用数量
        //获得绑定的社区id
        List<ObjectId> obList = newVersionBindService.getCommunityIdsByUserId(sonId);
        map.put("appCount",obList.size());
        //获得各个社区的推送应用记录
        int size = 0;
        if(obList.size()>0){
            List<ControlAppEntry> entries = controlAppDao.getEntryListByCommunityId(obList);
            ControlAppUserEntry entryList2 = controlAppUserDao.getEntry(parentId,sonId);
            Set<ObjectId> set = new HashSet<ObjectId>();
            for(ControlAppEntry entry1:entries){
                if(entry1 !=null && entry1.getAppIdList() != null) {
                    set.addAll(entry1.getAppIdList());
                }
            }
            if(entryList2 !=null && entryList2.getAppIdList() != null){
                set.addAll(entryList2.getAppIdList());
            }
            List<ObjectId> objectIds = new ArrayList<ObjectId>();
            objectIds.addAll(set);
            List<AppDetailEntry> detailEntries3 =  appDetailDao.getEntriesByIds(objectIds);
            size = detailEntries3.size();
        }else{
            ControlAppUserEntry entryList2 = controlAppUserDao.getEntry(parentId,sonId);
            Set<ObjectId> set = new HashSet<ObjectId>();
            if(entryList2 !=null && entryList2.getAppIdList() != null){
                set.addAll(entryList2.getAppIdList());
            }
            size = set.size();
        }
       // map.put("appCount",size);
        //防沉迷时间
        ControlTimeEntry controlTimeEntry = controlTimeDao.getEntry(sonId, parentId);
        long timecu = 30*60*1000;
        if(controlTimeEntry != null){
            timecu = controlTimeEntry.getTime();

        }
        map.put("time",timecu/60000);
        //使用时间
       // long useTime  = controlAppResultDao.getUserAllTime(sonId, startTime,endTime);
        ControlStudentResultEntry controlStudentResultEntry = controlStudentResultDao.getEntry(sonId,parentId,datetime);
        long useTime = 0l;
        if(controlStudentResultEntry!=null){
            useTime = controlStudentResultEntry.getNewAppUser();
        }
        map.put("useTime",useTime/60000);
        //剩余时间
        if(timecu/60000-useTime/60000 <0){
            map.put("reTime",0);
        }else{
            map.put("reTime",timecu/60000-useTime/60000);
        }
        UserDetailInfoDTO dto = userService.getUserInfoById(sonId.toString());
        if(dto!= null){
            String name = dto.getNickName()!=null?dto.getNickName():dto.getName();
            map.put("userName",name);
        }else{
            map.put("userName","");
        }
        int count2=recordChatPersonalDao.countChatEntries(sonId);
        map.put("chatCount",count2);
        //当前版本
        ControlVersionDTO controlVersionDTO = getSimpleUserVersion(parentId, sonId);
        map.put("version",controlVersionDTO.getVersion());
        return map;
    }

    public Map<String,Object> getFourMapNow(ObjectId parentId,ObjectId sonId) {
        //地图信息
        Map<String,Object> map = new HashMap<String, Object>();
        //获得当前时间
        long current = System.currentTimeMillis();
        //获得时间批次
        //long zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        String str = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,10);
        long zero = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
        //分割点
        long jiedian = zero+8*60*60*1000;//今天零点零分零秒的毫秒数
        //修改  todo
      /*  long startTime = 0l;
        long endTime = 0l;
        if(current>=jiedian){
            startTime = jiedian;
            endTime = jiedian+ 24*60*60*1000;
        }else{
            startTime = jiedian - 24*60*60*1000;
            endTime = jiedian;
        }*/
        long datetime = 0l;
        if(current>=jiedian){
            //今日
            datetime = zero;
        }else{
            //昨日
            datetime = zero- 24*60*60*1000;
        }
        // ControlMapEntry entry = controlMapDao.getEntryByParentId(parentId, sonId, zero);
       /* if (entry != null) {
            map.put("dto",new ControlMapDTO(entry));
        } else {*/
        //废弃数据
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
        //}
        //亲情电话个数
        int count = controlPhoneDao.getParentNumber(parentId, sonId);
        map.put("phoneCount",count);
        //已推送应用数量
        //获得绑定的社区id
        List<ObjectId> obList = newVersionBindService.getCommunityIdsByUserId(sonId);
        map.put("appCount",obList.size());
        //获得各个社区的推送应用记录
        int size = 0;
        if(obList.size()>0){
            List<ControlAppEntry> entries = controlAppDao.getEntryListByCommunityId(obList);
            ControlAppUserEntry entryList2 = controlAppUserDao.getEntry(parentId,sonId);
            Set<ObjectId> set = new HashSet<ObjectId>();
            for(ControlAppEntry entry1:entries){
                if(entry1 !=null && entry1.getAppIdList() != null) {
                    set.addAll(entry1.getAppIdList());
                }
            }
            if(entryList2 !=null && entryList2.getAppIdList() != null){
                set.addAll(entryList2.getAppIdList());
            }
            List<ObjectId> objectIds = new ArrayList<ObjectId>();
            objectIds.addAll(set);
            List<AppDetailEntry> detailEntries3 =  appDetailDao.getEntriesByIds(objectIds);
            size = detailEntries3.size();
        }else{
            ControlAppUserEntry entryList2 = controlAppUserDao.getEntry(parentId,sonId);
            Set<ObjectId> set = new HashSet<ObjectId>();
            if(entryList2 !=null && entryList2.getAppIdList() != null){
                set.addAll(entryList2.getAppIdList());
            }
            size = set.size();
        }
        // map.put("appCount",size);
        //防沉迷时间
        ControlTimeEntry controlTimeEntry = controlTimeDao.getEntry(sonId, parentId);
        long timecu = 30*60*1000;
        if(controlTimeEntry != null){
            timecu = controlTimeEntry.getTime();

        }
        map.put("time",timecu/60000);
        //使用时间
        // long useTime  = controlAppResultDao.getUserAllTime(sonId, startTime,endTime);
        ControlStudentResultEntry controlStudentResultEntry = controlStudentResultDao.getEntry(sonId,parentId,datetime);
        long useTime = 0l;
        if(controlStudentResultEntry!=null){
            useTime = controlStudentResultEntry.getNewAppUser();
        }
        map.put("useTime",useTime/60000);
        //剩余时间
        if(timecu/60000-useTime/60000 <0){
            map.put("reTime",0);
        }else{
            map.put("reTime",timecu/60000-useTime/60000);
        }
        UserDetailInfoDTO dto = userService.getUserInfoById(sonId.toString());
        if(dto!= null){
            String name = dto.getNickName()!=null?dto.getNickName():dto.getName();
            map.put("userName",name);
        }else{
            map.put("userName","");
        }
        int count2=recordChatPersonalDao.countChatEntries(sonId);
        map.put("chatCount",count2);
        //当前版本
        ControlVersionDTO controlVersionDTO = getSimpleUserVersion(parentId, sonId);
        ControlVersionDTO controlVersionDTO2 = getStudentVersion(sonId);
        String ver1 = controlVersionDTO.getVersion();
        String ver2 = controlVersionDTO2.getVersion();
        if(controlVersionDTO ==null || controlVersionDTO2==null){//记录不存在
            map.put("status",5);//未在线
        }else{
            if(ver1==null || ver2==null){//版本号不存在
                map.put("status",5);//未在线
            }else{
                if(ver1.equals("已退出")||ver2.equals("已退出") || ver1.equals("暂无数据")|| ver2.equals("暂无数据")){
                    map.put("status",5);//未在线
                }else{
                    if(getVersionLong(ver2)>=getVersionLong(ver1)){//版本号已更新
                        //判断mqtt状态
                        if(controlVersionDTO2.getStatus()==0){//离线
                            map.put("status",2);//版本号一致   离线
                        }else{
                            map.put("status",1);//版本号一致   在线
                        }
                    }else{
                        //判断mqtt状态
                        if(controlVersionDTO2.getStatus()==0){//离线
                            map.put("status",4);//版本号不一致   离线
                        }else{
                            map.put("status",3);//版本号不一致   在线
                        }
                    }
                }
            }
        }
        map.put("version",controlVersionDTO.getVersion());
        return map;
    }

    //版本号转换
    public long getVersionLong(String version){
        String vs = version.substring(0,14);
        long lTm = DateTimeUtils.getStrToLongTime(vs, "yyyyMMddHHmmss");
        return lTm;
    }

    public List<GroupOfCommunityDTO> getSonCommunityList(ObjectId userId){
        List<GroupOfCommunityDTO> dtos =new ArrayList<GroupOfCommunityDTO>();
        List<ObjectId> communityIds = newVersionBindService.getCommunityIdsByUserId(userId);
        Map<ObjectId,CommunityEntry> communityEntryMap = communityDao.findMapInfo(new ArrayList<ObjectId>(communityIds));
        for(Map.Entry<ObjectId,CommunityEntry> item:communityEntryMap.entrySet()){
            CommunityEntry communityEntry=item.getValue();
            GroupOfCommunityDTO dto =new GroupOfCommunityDTO(communityEntry.getGroupId().toString(),communityEntry.getID().toString(),
                    communityEntry.getCommunityName());
            dtos.add(dto);
        }
        return dtos;
    }

    public List<ControlMapDTO> getMapListEntry(ObjectId parentId,ObjectId sonId,String startTime,String endTime){
        List<ControlMapDTO> dtos = new ArrayList<ControlMapDTO>();
        long sl = 0l;
        if(startTime != null && !startTime.equals("")){
            sl = DateTimeUtils.getStrToLongTime(startTime);
        }
        long el = 0l;
        if(endTime != null && !endTime.equals("")){
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
        if(dateTime != null && !dateTime.equals("")){
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
        long timecu = 3*60*1000;
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
        ControlSchoolTimeEntry entry2 = controlSchoolTimeDao.getEntry(1);
        if(entry2 != null){
            String stm = entry2.getStartTime();
            long sl = 0l;
            if(stm != null && !stm.equals("")){
                sl = DateTimeUtils.getStrToLongTime(dateNowStr+" "+stm, "yyyy-MM-dd HH:mm:ss");
            }
            String etm = entry2.getEndTime();
            long el = 0l;
            if(etm != null && !etm.equals("")){
                el = DateTimeUtils.getStrToLongTime(dateNowStr+" "+etm, "yyyy-MM-dd HH:mm:ss");
            }
            if(current>sl && current < el){
                map.put("isControl",true);
            }else{
                map.put("isControl",false);
            }
            map.put("dto",new ControlSchoolTimeDTO(entry2));
        }
        map.put("freeTime",-1);
        return map;
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
        ControlSchoolTimeEntry entry2 = controlSchoolTimeDao.getOtherEntry(2, dateNowStr);
        if(entry2 != null){
            String stm = entry2.getStartTime();
            long sl = 0l;
            if(stm != null && !stm.equals("")){
                sl = DateTimeUtils.getStrToLongTime(dateNowStr+" "+stm, "yyyy-MM-dd HH:mm:ss");
            }
            String etm = entry2.getEndTime();
            long el = 0l;
            if(etm != null && !etm.equals("")){
                el = DateTimeUtils.getStrToLongTime(dateNowStr+" "+etm, "yyyy-MM-dd HH:mm:ss");
            }
            if(current>sl && current < el){
                map.put("isControl",true);
            }else{
                map.put("isControl",true);
            }
            map.put("dto",new ControlSchoolTimeDTO(entry2));
        }else{
            //管控时间
            Calendar cal = Calendar.getInstance();
            int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if(w==0){
                w= 7;
            }
            ControlSchoolTimeEntry entry = controlSchoolTimeDao.getEntry(w);
            if(entry != null){
                String stm = entry.getStartTime();
                long sl = 0l;
                if(stm != null && !stm.equals("")){
                    sl = DateTimeUtils.getStrToLongTime(dateNowStr+" "+stm, "yyyy-MM-dd HH:mm:ss");
                }
                String etm = entry.getEndTime();
                long el = 0l;
                if(etm != null && !etm.equals("")){
                    el = DateTimeUtils.getStrToLongTime(dateNowStr+" "+etm, "yyyy-MM-dd HH:mm:ss");
                }
                if(current>sl && current < el){
                    map.put("isControl",true);
                }else{
                    map.put("isControl",true);
                }
                map.put("dto",new ControlSchoolTimeDTO(entry));
            }else{
                map.put("isControl",true);
                map.put("dto",new ControlSchoolTimeDTO());
            }
        }
        map.put("freeTime",-1);
        List<PhoneTimeDTO> phoneTimeDTOs = new ArrayList<PhoneTimeDTO>();
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

    //学生管控未登陆获取默认信息
    public Map<String,Object> getCurrentSimpleMessageForSon(){
        //appDetailDao.get
        Map<String,Object> map = new HashMap<String, Object>();
        //可用应用列表
        List<AppDetailDTO> detailDTOs = new ArrayList<AppDetailDTO>();
        ControlAppSystemEntry controlAppSystemEntry = controlAppSystemDao.getEntry();
        if(controlAppSystemEntry != null && controlAppSystemEntry.getAppIdList()!=null && controlAppSystemEntry.getAppIdList().size()>0){
            List<AppDetailEntry> detailEntries3 =  appDetailDao.getEntriesByIds(controlAppSystemEntry.getAppIdList());
            for(AppDetailEntry detailEntry : detailEntries3){
                detailDTOs.add(new AppDetailDTO(detailEntry,1));
            }
        }
        List<AppDetailDTO> detailDTOs2 = new ArrayList<AppDetailDTO>();
        //map.put("acceptApp",detailDTOs);
        //禁用黑名单
        List<AppDetailEntry> detailEntries =  appDetailDao.getSimpleAppEntry();
        for(AppDetailEntry detailEntry : detailEntries){
            detailDTOs2.add(new AppDetailDTO(detailEntry,1));
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
        long timecu = 0;//未登录0分钟使用时间
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
        map.put("freeTime",-1);
        List<PhoneTimeDTO> phoneTimeDTOs = new ArrayList<PhoneTimeDTO>();
        List<ControlSchoolTimeEntry> entryList = controlSchoolTimeDao.getAllEntryList();
        map.put("dto",this.getCurrentNowControlDto(entryList));
        map.put("isControl",true);
        Map<String,PhoneTimeDTO> phoneMap = new HashMap<String, PhoneTimeDTO>();
        for(ControlSchoolTimeEntry ctm : entryList){
            PhoneTimeDTO phoneTimeDTO = new PhoneTimeDTO();
            phoneTimeDTO.setClassTime(ctm.getStartTime()+"-"+ctm.getEndTime());
            phoneTimeDTO.setStart("");
            phoneTimeDTO.setEnd("");
            phoneTimeDTO.setType(ctm.getType());
            String currentTime = ctm.getStartTime()+"-"+ctm.getEndTime();
            if(ctm.getType()==1){
                phoneTimeDTO.setCurrentTime(ctm.getWeek()+"");
                PhoneTimeDTO phoneTimeDTO1 = phoneMap.get(ctm.getWeek()+"");
                if(phoneTimeDTO1!=null){
                    phoneTimeDTO1.setClassTime(phoneTimeDTO1.getClassTime()+"###"+currentTime);
                    phoneMap.put(ctm.getWeek()+"",phoneTimeDTO1);
                }else{
                    phoneTimeDTO.setClassTime(currentTime);
                    phoneMap.put(ctm.getWeek()+"",phoneTimeDTO);
                }

            }else if(ctm.getType()==2){
                phoneTimeDTO.setCurrentTime(ctm.getDataTime().substring(5,10));
                PhoneTimeDTO phoneTimeDTO1 = phoneMap.get(ctm.getDataTime());
                if(phoneTimeDTO1!=null){
                    phoneTimeDTO1.setClassTime(phoneTimeDTO1.getClassTime()+"###"+currentTime);
                    phoneMap.put(ctm.getDataTime()+"",phoneTimeDTO1);
                }else{
                    phoneTimeDTO.setClassTime(currentTime);
                    phoneMap.put(ctm.getDataTime()+"",phoneTimeDTO);
                }
            }else if(ctm.getType()==3){
                phoneTimeDTO.setCurrentTime(ctm.getDataTime());
                PhoneTimeDTO phoneTimeDTO1 = phoneMap.get(ctm.getDataTime());
                if(phoneTimeDTO1!=null){
                    phoneTimeDTO1.setClassTime(phoneTimeDTO1.getClassTime()+"###"+currentTime);
                    phoneMap.put(ctm.getDataTime()+"",phoneTimeDTO1);
                }else{
                    phoneTimeDTO.setClassTime(currentTime);
                    phoneMap.put(ctm.getDataTime()+"",phoneTimeDTO);
                }
            }
        }


        phoneTimeDTOs.addAll(phoneMap.values());
      /*  for(ControlSchoolTimeEntry ctm : entryList){
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
        }*/
        map.put("controlList",phoneTimeDTOs);
        return map;
    }
    private List<PhoneTimeDTO> getNewCurrent( List<ControlSchoolTimeEntry> entryList){
        List<PhoneTimeDTO> phoneTimeDTOs = new ArrayList<PhoneTimeDTO>();
        Map<String,PhoneTimeDTO> phoneMap = new HashMap<String, PhoneTimeDTO>();
        for(ControlSchoolTimeEntry ctm : entryList){
            PhoneTimeDTO phoneTimeDTO = new PhoneTimeDTO();
            phoneTimeDTO.setClassTime(ctm.getStartTime()+"-"+ctm.getEndTime());
            phoneTimeDTO.setStart("");
            phoneTimeDTO.setEnd("");
            phoneTimeDTO.setType(ctm.getType());
            String currentTime = ctm.getStartTime()+"-"+ctm.getEndTime();
            if(ctm.getType()==1){
                phoneTimeDTO.setCurrentTime(ctm.getWeek()+"");
                PhoneTimeDTO phoneTimeDTO1 = phoneMap.get(ctm.getWeek()+"");
                if(phoneTimeDTO1!=null){
                    phoneTimeDTO1.setClassTime(phoneTimeDTO1.getClassTime()+"###"+currentTime);
                    phoneMap.put(ctm.getWeek()+"",phoneTimeDTO1);
                }else{
                    phoneTimeDTO.setClassTime(currentTime);
                    phoneMap.put(ctm.getWeek()+"",phoneTimeDTO);
                }

            }else if(ctm.getType()==2){
                phoneTimeDTO.setCurrentTime(ctm.getDataTime().substring(5,10));
                PhoneTimeDTO phoneTimeDTO1 = phoneMap.get(ctm.getDataTime());
                if(phoneTimeDTO1!=null){
                    phoneTimeDTO1.setClassTime(phoneTimeDTO1.getClassTime()+"###"+currentTime);
                    phoneMap.put(ctm.getDataTime()+"",phoneTimeDTO1);
                }else{
                    phoneTimeDTO.setClassTime(currentTime);
                    phoneMap.put(ctm.getDataTime()+"",phoneTimeDTO);
                }
            }else if(ctm.getType()==3){
                phoneTimeDTO.setCurrentTime(ctm.getDataTime());
                PhoneTimeDTO phoneTimeDTO1 = phoneMap.get(ctm.getDataTime());
                if(phoneTimeDTO1!=null){
                    phoneTimeDTO1.setClassTime(phoneTimeDTO1.getClassTime()+"###"+currentTime);
                    phoneMap.put(ctm.getDataTime()+"",phoneTimeDTO1);
                }else{
                    phoneTimeDTO.setClassTime(currentTime);
                    phoneMap.put(ctm.getDataTime()+"",phoneTimeDTO);
                }
            }
        }
        phoneTimeDTOs.addAll(phoneMap.values());
        return phoneTimeDTOs;
    }

    //学生登录获取所有信息
    public Map<String,Object> getAllMessageForSon(ObjectId sonId){
        NewVersionBindRelationEntry newEntry = newVersionBindRelationDao.getBindEntry(sonId);
        if(null == newEntry){
            return this.getSimpleMessageForSon();
        }
        //家长推荐
        ObjectId parentId = newEntry.getMainUserId();
        //ControlTimeEntry entry = controlTimeDao.getEntry(userId, parentId);
        ControlTimeEntry controlTimeEntry = controlTimeDao.getEntry(sonId, parentId);
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
        ControlSchoolTimeEntry entry2 = controlSchoolTimeDao.getOtherEntry(2,dateNowStr);
        if(entry2 != null){
            String stm = entry2.getStartTime();
            long sl = 0l;
            if(stm != null && !stm.equals("")){
                sl = DateTimeUtils.getStrToLongTime(dateNowStr+" "+stm, "yyyy-MM-dd HH:mm:ss");
            }
            String etm = entry2.getEndTime();
            long el = 0l;
            if(etm != null && !etm.equals("")){
                el = DateTimeUtils.getStrToLongTime(dateNowStr+" "+etm, "yyyy-MM-dd HH:mm:ss");
            }
            if(current>sl && current < el){
                map.put("isControl",true);
            }else{
                map.put("isControl",true);
            }
            map.put("dto",new ControlSchoolTimeDTO(entry2));
        }else{
            //管控时间
            Calendar cal = Calendar.getInstance();
            int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if(w==0){
                w= 7;
            }
            ControlSchoolTimeEntry entry = controlSchoolTimeDao.getEntry(w);
            if(entry != null){
                String stm = entry.getStartTime();
                long sl = 0l;
                if(stm != null && !stm.equals("")){
                    sl = DateTimeUtils.getStrToLongTime(dateNowStr+" "+stm, "yyyy-MM-dd HH:mm:ss");
                }
                String etm = entry.getEndTime();
                long el = 0l;
                if(etm != null && !etm.equals("")){
                    el = DateTimeUtils.getStrToLongTime(dateNowStr+" "+etm, "yyyy-MM-dd HH:mm:ss");
                }
                if(current>sl && current < el){
                    map.put("isControl",true);
                }else{
                    map.put("isControl",true);
                }
                map.put("dto",new ControlSchoolTimeDTO(entry));
            }else{
                map.put("isControl",true);
                map.put("dto",new ControlSchoolTimeDTO());
            }
        }

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
        return map;
    }

    //学生新登录获取所有信息
    public Map<String,Object> getNewAllMessageForSon(ObjectId sonId){
        NewVersionBindRelationEntry newEntry = newVersionBindRelationDao.getBindEntry(sonId);
        if(null == newEntry){
            return this.getNewSimpleMessageForSon();
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
        ObjectId parentId = newEntry.getMainUserId();
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
        ControlSchoolTimeEntry entry2 = controlSchoolTimeDao.getOtherEntry(2,dateNowStr);
        if(entry2 != null){
            String stm = entry2.getStartTime();
            long sl = 0l;
            if(stm != null && !stm.equals("")){
                sl = DateTimeUtils.getStrToLongTime(dateNowStr+" "+stm, "yyyy-MM-dd HH:mm:ss");
            }
            String etm = entry2.getEndTime();
            long el = 0l;
            if(etm != null && !etm.equals("")){
                el = DateTimeUtils.getStrToLongTime(dateNowStr+" "+etm, "yyyy-MM-dd HH:mm:ss");
            }
            if(current>sl && current < el){
                map.put("isControl",true);
            }else{
                map.put("isControl",true);
            }
            map.put("dto",new ControlSchoolTimeDTO(entry2));
        }else{
            //管控时间
            Calendar cal = Calendar.getInstance();
            int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if(w==0){
                w= 7;
            }
            ControlSchoolTimeEntry entry = controlSchoolTimeDao.getEntry(w);
            if(entry != null){
                String stm = entry.getStartTime();
                long sl = 0l;
                if(stm != null && !stm.equals("")){
                    sl = DateTimeUtils.getStrToLongTime(dateNowStr+" "+stm, "yyyy-MM-dd HH:mm:ss");
                }
                String etm = entry.getEndTime();
                long el = 0l;
                if(etm != null && !etm.equals("")){
                    el = DateTimeUtils.getStrToLongTime(dateNowStr+" "+etm, "yyyy-MM-dd HH:mm:ss");
                }
                if(current>sl && current < el){
                    map.put("isControl",true);
                }else{
                    map.put("isControl",true);
                }
                map.put("dto",new ControlSchoolTimeDTO(entry));
            }else{
                map.put("isControl",true);
                map.put("dto",new ControlSchoolTimeDTO());
            }
        }

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
    /**
     *  分段学校学生新登录获取所有信息
     */
    public Map<String,Object> getCurrentAllSchoolMessageForSon(ObjectId sonId){
        NewVersionBindRelationEntry newEntry = newVersionBindRelationDao.getBindEntry(sonId);
        if(null == newEntry) {
            return this.getCurrentSimpleMessageForSon();
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
        map.put("dto",this.getCurrentNowControlDto(entryList));
      /*  for(ControlSchoolTimeEntry ctm : entryList){
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
        }*/
        Map<String,PhoneTimeDTO> phoneMap = new HashMap<String, PhoneTimeDTO>();
        for(ControlSchoolTimeEntry ctm : entryList){
            PhoneTimeDTO phoneTimeDTO = new PhoneTimeDTO();
            phoneTimeDTO.setClassTime(ctm.getStartTime()+"-"+ctm.getEndTime());
            phoneTimeDTO.setStart("");
            phoneTimeDTO.setEnd("");
            phoneTimeDTO.setType(ctm.getType());
            String currentTime = ctm.getStartTime()+"-"+ctm.getEndTime();
            if(ctm.getType()==1){
                phoneTimeDTO.setCurrentTime(ctm.getWeek()+"");
                PhoneTimeDTO phoneTimeDTO1 = phoneMap.get(ctm.getWeek()+"");
                if(phoneTimeDTO1!=null){
                    phoneTimeDTO1.setClassTime(phoneTimeDTO1.getClassTime()+"###"+currentTime);
                    phoneMap.put(ctm.getWeek()+"",phoneTimeDTO1);
                }else{
                    phoneTimeDTO.setClassTime(currentTime);
                    phoneMap.put(ctm.getWeek()+"",phoneTimeDTO);
                }

            }else if(ctm.getType()==2){
                phoneTimeDTO.setCurrentTime(ctm.getDataTime().substring(5,10));
                PhoneTimeDTO phoneTimeDTO1 = phoneMap.get(ctm.getDataTime());
                if(phoneTimeDTO1!=null){
                    phoneTimeDTO1.setClassTime(phoneTimeDTO1.getClassTime()+"###"+currentTime);
                    phoneMap.put(ctm.getDataTime()+"",phoneTimeDTO1);
                }else{
                    phoneTimeDTO.setClassTime(currentTime);
                    phoneMap.put(ctm.getDataTime()+"",phoneTimeDTO);
                }
            }else if(ctm.getType()==3){
                phoneTimeDTO.setCurrentTime(ctm.getDataTime());
                PhoneTimeDTO phoneTimeDTO1 = phoneMap.get(ctm.getDataTime());
                if(phoneTimeDTO1!=null){
                    phoneTimeDTO1.setClassTime(phoneTimeDTO1.getClassTime()+"###"+currentTime);
                    phoneMap.put(ctm.getDataTime()+"",phoneTimeDTO1);
                }else{
                    phoneTimeDTO.setClassTime(currentTime);
                    phoneMap.put(ctm.getDataTime()+"",phoneTimeDTO);
                }
            }
        }


        phoneTimeDTOs.addAll(phoneMap.values());
        map.put("controlList",phoneTimeDTOs);

        //个人版本
        // ControlVersionDTO controlVersionDTO = getStudentVersion(sonId);
        //map.put("version",controlVersionDTO.getVersion());
        return map;
    }

    public String getMyNewVersion(ObjectId userId){
        ControlVersionDTO controlVersionDTO = getStudentVersion(userId);
        if(controlVersionDTO!=null){
            return controlVersionDTO.getVersion();
        }
        return "";
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
    public ControlSchoolTimeDTO getCurrentNowControlDto(List<ControlSchoolTimeEntry> controlSchoolTimeEntries){
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
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(current));
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if(hour<8){
            current = current - 24*60*60*1000;
        }
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
                        ControlSchoolTimeEntry entry2 = map1.get(oid);
                        String ctm = controlSchoolTimeEntry.getStartTime()+"-"+controlSchoolTimeEntry.getEndTime();
                        if(entry2!=null){
                            if(entry2.getDataTime().equals(controlSchoolTimeEntry.getDataTime())){
                                controlSchoolTimeEntry.setManyClassTime(entry2.getManyClassTime()+"###"+ctm);
                            }
                        }else{
                            controlSchoolTimeEntry.setManyClassTime(ctm);
                        }
                        map1.put(oid,controlSchoolTimeEntry);
                    }
                }
            }else if(controlSchoolTimeEntry.getType()==2 && controlSchoolTimeEntry.getDataTime() != null && controlSchoolTimeEntry.getDataTime().equals(str)){
                ControlSchoolTimeEntry entry2 = map2.get(oid);
                String ctm = controlSchoolTimeEntry.getStartTime()+"-"+controlSchoolTimeEntry.getEndTime();
                if(entry2!=null){
                    controlSchoolTimeEntry.setManyClassTime(entry2.getManyClassTime()+"###"+ctm);
                }else{
                    controlSchoolTimeEntry.setManyClassTime(ctm);
                }
                map2.put(oid,controlSchoolTimeEntry);//特殊

            }else if(controlSchoolTimeEntry.getType()==1 && controlSchoolTimeEntry.getWeek()==w){
                ControlSchoolTimeEntry entry2 = map3.get(oid);
                String ctm = controlSchoolTimeEntry.getStartTime()+"-"+controlSchoolTimeEntry.getEndTime();
                if(entry2!=null){
                    controlSchoolTimeEntry.setManyClassTime(entry2.getManyClassTime()+"###"+ctm);
                }else{
                    controlSchoolTimeEntry.setManyClassTime(ctm);
                }
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
        return new ControlSchoolTimeDTO(controlSchoolTimeEntry,controlSchoolTimeEntry.getManyClassTime());
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
     * 待完成多学校处理
     */
    public List<ControlSchoolTimeEntry> getMoreThanTwoSchool(List<ControlSchoolTimeEntry> entryList){
        List<ControlSchoolTimeEntry> entries1 = new ArrayList<ControlSchoolTimeEntry>();
        List<ControlSchoolTimeEntry> entries2 = new ArrayList<ControlSchoolTimeEntry>();
        List<ControlSchoolTimeEntry> entries3 = new ArrayList<ControlSchoolTimeEntry>();

        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        //long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        String str = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,10);
        long zero = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(zero);

        List<ControlSchoolTimeEntry> finalEntries = new ArrayList<ControlSchoolTimeEntry>();
        for(ControlSchoolTimeEntry controlSchoolTimeEntry:entryList){
            if(controlSchoolTimeEntry.getType()==1){
                entries1.add(controlSchoolTimeEntry);
            }else if(controlSchoolTimeEntry.getType()==2){
                entries2.add(controlSchoolTimeEntry);
            }else if(controlSchoolTimeEntry.getType()==3){
                entries3.add(controlSchoolTimeEntry);
            }
        }
        //常规
        if(entries1.size()>0){
            for(ControlSchoolTimeEntry schoolTimeEntry : entries1){
                String stm = schoolTimeEntry.getStartTime();
                long sl = 0l;
                if(stm != null && !stm.equals("")){
                    sl = DateTimeUtils.getStrToLongTime(dateNowStr+" "+stm, "yyyy-MM-dd HH:mm:ss");
                }
                String etm = schoolTimeEntry.getEndTime();
                long el = 0l;
                if(etm != null && !etm.equals("")){
                    el = DateTimeUtils.getStrToLongTime(dateNowStr+" "+etm, "yyyy-MM-dd HH:mm:ss");
                }
                for(ControlSchoolTimeEntry schoolTimeEntry2 : entries1){
                    if(schoolTimeEntry.getWeek()==schoolTimeEntry2.getWeek()){
                        String stm2 = schoolTimeEntry.getStartTime();
                        long sl2 = 0l;
                        if(stm2 != null && !stm2.equals("")){
                            sl2 = DateTimeUtils.getStrToLongTime(dateNowStr+" "+stm2, "yyyy-MM-dd HH:mm:ss");
                        }
                        String etm2 = schoolTimeEntry.getEndTime();
                        long el2 = 0l;
                        if(etm2 != null && !etm2.equals("")){
                            el2 = DateTimeUtils.getStrToLongTime(dateNowStr+" "+etm2, "yyyy-MM-dd HH:mm:ss");
                        }
                        if(sl >sl2){
                            sl=sl2;
                            schoolTimeEntry.setStartTime(DateTimeUtils.getLongToStrTimeTwo(sl2).substring(11,19));
                        }
                        if(el <el2){
                            el=el2;
                            schoolTimeEntry.setEndTime(DateTimeUtils.getLongToStrTimeTwo(el2).substring(11,19));
                        }
                    }
                }
            }
        }




        return finalEntries;
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

    //老师查询可推送应用
    public List<AppDetailDTO> getShouldAppList(ObjectId userId,ObjectId communityId,String keyword){
        List<AppDetailDTO> dtos = new ArrayList<AppDetailDTO>();
        //获取所有可推送的第三方应用
        List<AppDetailEntry> entries = appDetailDao.getThirdEntries();
        //查询该社区推送的记录
       ControlAppEntry entry = controlAppDao.getEntry(userId,communityId);
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
        if(dtos.size()>0 && !keyword.equals("")){
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
            //存在加入用户记录
            oblist.addAll(entry2.getAppIdList());
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
            if(controlAppSystemEntry != null){
                oblist.addAll(controlAppSystemEntry.getAppIdList());
            }
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
            //存在加入用户记录
            oblist.addAll(entry2.getAppIdList());
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
            oblist.addAll(controlAppSystemEntry.getAppIdList());
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
                if(oblist.contains(detailEntry.getID())){
                    dto.setIsCheck(1);
                    dtos1.add(dto);
                }else if(capplist.contains(detailEntry.getID())){
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
        TeacherApproveEntry entry = teacherApproveDao.getEntry(teacherId);
        if(entry != null && entry.getType()==2){
            map.put("isRen",true);
        }else{
            map.put("isRen",false);
        }
        //map.put("isRen",true);
        return map;
    }

    public Map<String,Object> getNewSimpleMessageForTea(ObjectId teacherId){
        Map<String,Object> map = new HashMap<String, Object>();
        List<ObjectId> oids = getMyRoleList(teacherId);
        List<CommunityDTO> dtos = new ArrayList<CommunityDTO>();
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        if(oids.size()>0){
            List<CommunityEntry> communityEntries = communityDao.findByObjectIds(oids);
            for(CommunityEntry com : communityEntries){
                CommunityDTO communityDTO = new CommunityDTO(com);
                communityDTO.setLogo(this.getNewLogo(communityDTO.getLogo()));
                dtos.add(communityDTO);
                objectIdList.add(com.getID());
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
        if(objectIdList.size()>0){
            //map.put("controlList",getFourAllMessageForTea(objectIdList));旧
            map.put("controlList",getFourAllMessageForSheTea(objectIdList));
        }else{
            map.put("controlList",new ArrayList<ControlMiduleDTO>());
        }
        //map.put("isRen",true);
        return map;
    }

    public Map<String,Object> getCurrentSimpleMessageForTea(ObjectId teacherId){
        Map<String,Object> map = new HashMap<String, Object>();
        List<ObjectId> oids = getMyRoleList(teacherId);
        List<CommunityDTO> dtos = new ArrayList<CommunityDTO>();
        List<ObjectId> objectIdList = new ArrayList<ObjectId>();
        if(oids.size()>0){
            List<CommunityEntry> communityEntries = communityDao.findByObjectIds(oids);
            for(CommunityEntry com : communityEntries){
                CommunityDTO communityDTO = new CommunityDTO(com);
                communityDTO.setLogo(this.getNewLogo(communityDTO.getLogo()));
                dtos.add(communityDTO);
                objectIdList.add(com.getID());
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
        if(objectIdList.size()>0){
            //map.put("controlList",getFourAllMessageForTea(objectIdList));旧
            map.put("controlList",getNewFourAllMessageForSheTea(objectIdList));
        }else{
            map.put("controlList",new ArrayList<ControlMiduleDTO>());
        }
        //map.put("isRen",true);
        return map;
    }

    public List<ControlMiduleDTO> getNewFourAllMessageForSheTea(List<ObjectId> communityIds){
        List<ControlMiduleDTO> controlMiduleDTOs = new ArrayList<ControlMiduleDTO>();
        List<SchoolCommunityEntry> schoolCommunityEntries = schoolCommunityDao.getReviewList2(communityIds);
        List<ObjectId> oids = new ArrayList<ObjectId>();
        Map<ObjectId,ObjectId> obmap = new HashMap<ObjectId, ObjectId>();
        for(SchoolCommunityEntry schoolCommunityEntry: schoolCommunityEntries){
            oids.add(schoolCommunityEntry.getSchoolId());
            obmap.put(schoolCommunityEntry.getCommunityId(),schoolCommunityEntry.getSchoolId());
        }

        //学校设置
        List<ControlSchoolTimeEntry> controlSchoolTimeEntries = controlSchoolTimeDao.getAllSchoolEntryList(oids);
        //List<ControlSchoolTimeEntry> controlSchoolTimeEntries = controlSchoolTimeDao.getAllSchoolEntryList(communityIds);
        Map<ObjectId,ControlSchoolTimeEntry> map1 = new HashMap<ObjectId, ControlSchoolTimeEntry>();//时间段配置
        Map<ObjectId,ControlSchoolTimeEntry> map2 = new HashMap<ObjectId, ControlSchoolTimeEntry>();//特殊配置
        Map<ObjectId,ControlSchoolTimeEntry> map3 = new HashMap<ObjectId, ControlSchoolTimeEntry>();//周常配置

        Map<ObjectId,List<ControlSchoolTimeEntry>> newMap1 = new HashMap<ObjectId, List<ControlSchoolTimeEntry>>();//时间段配置
        Map<ObjectId,List<ControlSchoolTimeEntry>> newMap2 = new HashMap<ObjectId, List<ControlSchoolTimeEntry>>();//特殊配置
        Map<ObjectId,List<ControlSchoolTimeEntry>> newMap3 = new HashMap<ObjectId, List<ControlSchoolTimeEntry>>();//周常配置
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
        for(ControlSchoolTimeEntry controlSchoolTimeEntry : controlSchoolTimeEntries){
            List<ControlSchoolTimeEntry> newList1 = newMap1.get(controlSchoolTimeEntry.getParentId());
            List<ControlSchoolTimeEntry> newList2 = newMap2.get(controlSchoolTimeEntry.getParentId());
            List<ControlSchoolTimeEntry> newList3 = newMap3.get(controlSchoolTimeEntry.getParentId());
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
                        if(newList1==null){
                            List<ControlSchoolTimeEntry> clist = new ArrayList<ControlSchoolTimeEntry>();
                            clist.add(controlSchoolTimeEntry);
                            newMap1.put(controlSchoolTimeEntry.getParentId(),clist);
                        }else{
                            newList1.add(controlSchoolTimeEntry);
                            newMap1.put(controlSchoolTimeEntry.getParentId(),newList1);
                        }
                       // map1.put(controlSchoolTimeEntry.getParentId(),controlSchoolTimeEntry);
                    }
                }
            }else if(controlSchoolTimeEntry.getType()==2 && controlSchoolTimeEntry.getDataTime() != null && controlSchoolTimeEntry.getDataTime().equals(str)){
                if(newList2==null){
                    List<ControlSchoolTimeEntry> clist = new ArrayList<ControlSchoolTimeEntry>();
                    clist.add(controlSchoolTimeEntry);
                    newMap2.put(controlSchoolTimeEntry.getParentId(),clist);
                }else{
                    newList2.add(controlSchoolTimeEntry);
                    newMap2.put(controlSchoolTimeEntry.getParentId(),newList2);
                }
               /* long sl = 0l;
                String date = controlSchoolTimeEntry.getDataTime();
                String startStr= controlSchoolTimeEntry.getStartTime();
                String endStr = controlSchoolTimeEntry.getEndTime();
                if(startStr != null && !startStr.equals("")){
                    sl = DateTimeUtils.getStrToLongTime(date+startStr, "yyyy-MM-dd HH:mm:ss");
                }
                long el = 0l;
                if(endStr != null && !endStr.equals("")){
                    el = DateTimeUtils.getStrToLongTime(date+endStr, "yyyy-MM-dd HH:mm:ss");
                }
                if(current>sl && current < el){
                    map2.put(controlSchoolTimeEntry.getParentId(),controlSchoolTimeEntry);//特殊
                }
                //map2.put(controlSchoolTimeEntry.getParentId(),controlSchoolTimeEntry);//特殊*/

            }else if(controlSchoolTimeEntry.getType()==1 && controlSchoolTimeEntry.getWeek()==w){
                if(newList3==null){
                    List<ControlSchoolTimeEntry> clist = new ArrayList<ControlSchoolTimeEntry>();
                    clist.add(controlSchoolTimeEntry);
                    newMap3.put(controlSchoolTimeEntry.getParentId(),clist);
                }else{
                    newList3.add(controlSchoolTimeEntry);
                    newMap3.put(controlSchoolTimeEntry.getParentId(),newList3);
                }
         /*       long sl = 0l;
                String date = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,10);
                String startStr= controlSchoolTimeEntry.getStartTime();
                String endStr = controlSchoolTimeEntry.getEndTime();
                if(startStr != null && !startStr.equals("")){
                    sl = DateTimeUtils.getStrToLongTime(date+startStr, "yyyy-MM-dd HH:mm:ss");
                }
                long el = 0l;
                if(endStr != null && !endStr.equals("")){
                    el = DateTimeUtils.getStrToLongTime(date+endStr, "yyyy-MM-dd HH:mm:ss");
                }
                if(current>sl && current < el){
                    map3.put(controlSchoolTimeEntry.getParentId(),controlSchoolTimeEntry);//周常
                }*/
               // map3.put(controlSchoolTimeEntry.getParentId(),controlSchoolTimeEntry);//周常

            }
        }
        ObjectId objectId = new ObjectId();
        //默认设置
        List<ControlSchoolTimeEntry> controlSchoolTimeEntries2 = controlSchoolTimeDao.getAllEntryList();
        for(ControlSchoolTimeEntry controlSchoolTimeEntry : controlSchoolTimeEntries2){
            List<ControlSchoolTimeEntry> newList1 = newMap1.get(controlSchoolTimeEntry.getParentId());
            List<ControlSchoolTimeEntry> newList2 = newMap2.get(controlSchoolTimeEntry.getParentId());
            List<ControlSchoolTimeEntry> newList3 = newMap3.get(controlSchoolTimeEntry.getParentId());
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
                        if(newList1==null){
                            List<ControlSchoolTimeEntry> clist = new ArrayList<ControlSchoolTimeEntry>();
                            clist.add(controlSchoolTimeEntry);
                            newMap1.put(objectId,clist);
                        }else{
                            newList1.add(controlSchoolTimeEntry);
                            newMap1.put(objectId,newList1);
                        }
                        //map1.put(objectId,controlSchoolTimeEntry);
                    }
                }
            }else if(controlSchoolTimeEntry.getType()==2 && controlSchoolTimeEntry.getDataTime() != null && controlSchoolTimeEntry.getDataTime().equals(str)){
                if(newList2==null){
                    List<ControlSchoolTimeEntry> clist = new ArrayList<ControlSchoolTimeEntry>();
                    clist.add(controlSchoolTimeEntry);
                    newMap2.put(objectId,clist);
                }else{
                    newList2.add(controlSchoolTimeEntry);
                    newMap2.put(objectId,newList2);
                }
               /* long sl = 0l;
                String date = controlSchoolTimeEntry.getDataTime();
                String startStr= controlSchoolTimeEntry.getStartTime();
                String endStr = controlSchoolTimeEntry.getEndTime();
                if(startStr != null && !startStr.equals("")){
                    sl = DateTimeUtils.getStrToLongTime(date+startStr, "yyyy-MM-dd HH:mm:ss");
                }
                long el = 0l;
                if(endStr != null && !endStr.equals("")){
                    el = DateTimeUtils.getStrToLongTime(date+endStr, "yyyy-MM-dd HH:mm:ss");
                }
                if(current>sl && current < el){
                    map2.put(controlSchoolTimeEntry.getParentId(),controlSchoolTimeEntry);//特殊
                }*/
                //map2.put(objectId,controlSchoolTimeEntry);//特殊
            }else if(controlSchoolTimeEntry.getType()==1 && controlSchoolTimeEntry.getWeek()==w){
                if(newList3==null){
                    List<ControlSchoolTimeEntry> clist = new ArrayList<ControlSchoolTimeEntry>();
                    clist.add(controlSchoolTimeEntry);
                    newMap3.put(objectId,clist);
                }else{
                    newList3.add(controlSchoolTimeEntry);
                    newMap3.put(objectId,newList3);
                }
              /*  long sl = 0l;
                String date = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,10);
                String startStr= controlSchoolTimeEntry.getStartTime();
                String endStr = controlSchoolTimeEntry.getEndTime();
                if(startStr != null && !startStr.equals("")){
                    sl = DateTimeUtils.getStrToLongTime(date+startStr, "yyyy-MM-dd HH:mm:ss");
                }
                long el = 0l;
                if(endStr != null && !endStr.equals("")){
                    el = DateTimeUtils.getStrToLongTime(date+endStr, "yyyy-MM-dd HH:mm:ss");
                }
                if(current>sl && current < el){
                    map3.put(controlSchoolTimeEntry.getParentId(),controlSchoolTimeEntry);//周常
                }*/
                //map3.put(objectId,controlSchoolTimeEntry);//周常
            }
        }


        //特殊设置
        List<ControlNowTimeEntry> controlNowTimeEntries = controlNowTimeDao.getOtherEntryList(str, communityIds);
        Map<ObjectId,ControlNowTimeEntry>  map4 = new HashMap<ObjectId, ControlNowTimeEntry>();
        for(ControlNowTimeEntry entry3: controlNowTimeEntries){
            map4.put(entry3.getCommunityId(),entry3);
        }
        //计算
        for(ObjectId oid: communityIds){
            ControlNowTimeEntry entry3 =  map4.get(oid);
            ControlMiduleDTO controlMiduleDTO = new ControlMiduleDTO();
            ObjectId ssid = obmap.get(oid);
            //获取应该执行的设置
            ControlSchoolTimeEntry controlSchoolTimeEntry = new ControlSchoolTimeEntry();
            if(ssid !=null && newMap1.get(ssid)!=null){
                List<ControlSchoolTimeEntry> conlist  = newMap1.get(ssid);
                controlSchoolTimeEntry = getSimple(conlist,current,str);
            }else if(ssid !=null && newMap2.get(ssid)!=null){
                List<ControlSchoolTimeEntry> conlist  = newMap2.get(ssid);
                controlSchoolTimeEntry = getSimple(conlist,current,str);
            }else if(ssid !=null && newMap3.get(ssid)!=null){
                List<ControlSchoolTimeEntry> conlist  = newMap3.get(ssid);
                controlSchoolTimeEntry = getSimple(conlist, current, str);
            }else if(newMap1.get(objectId)!=null){
                List<ControlSchoolTimeEntry> conlist  = newMap1.get(objectId);
                controlSchoolTimeEntry = getSimple(conlist, current, str);
            }else if(newMap2.get(objectId)!=null){
                List<ControlSchoolTimeEntry> conlist  = newMap2.get(objectId);
                controlSchoolTimeEntry = getSimple(conlist, current, str);
            }else if(newMap3.get(objectId)!=null){
                List<ControlSchoolTimeEntry> conlist  = newMap3.get(objectId);
                controlSchoolTimeEntry = getSimple(conlist, current, str);
            }
            controlSchoolTimeEntry.setCommunityId(oid);
            controlMiduleDTO.setDto(new ControlSchoolTimeDTO(controlSchoolTimeEntry));
            String stm = controlSchoolTimeEntry.getStartTime();
            long sl = 0l;
            if(stm != null && !stm.equals("")){
                sl = DateTimeUtils.getStrToLongTime(str+" "+stm, "yyyy-MM-dd HH:mm:ss");
            }
            String etm = controlSchoolTimeEntry.getEndTime();
            long el = 0l;
            if(etm != null && !etm.equals("")){
                el = DateTimeUtils.getStrToLongTime(str+" "+etm, "yyyy-MM-dd HH:mm:ss");
            }
            if(current>sl && current < el){
                controlMiduleDTO.setTime(el-current);
                controlMiduleDTO.setIsControl(1);
            }else{
                controlMiduleDTO.setTime(0);
                controlMiduleDTO.setIsControl(2);
            }
            //溢出时间处理  controlMiduleDTO
            if(entry3 != null && current < el){
                String stm2 = entry3.getStartTime();
                long sl2 = 0l;
                if(stm2 != null && !stm2.equals("")){
                    sl2 = DateTimeUtils.getStrToLongTime(str+" "+stm2, "yyyy-MM-dd HH:mm:ss");
                }
                String etm2 = entry3.getEndTime();
                long el2 = 0l;
                if(etm2 != null && !etm2.equals("")){
                    el2 = DateTimeUtils.getStrToLongTime(str+" "+etm2, "yyyy-MM-dd HH:mm:ss");
                }
                if(current>sl2 && current < el2){
                    controlMiduleDTO.setTime(el2-current);
                    controlMiduleDTO.setIsControl(3);
                }else{
                    controlMiduleDTO.setTime(0);
                }
                controlMiduleDTO.setThird(new ControlNowTimeDTO(entry3));
            }else{
                ControlNowTimeEntry controlNowTimeEntry = new ControlNowTimeEntry();
                controlMiduleDTO.setThird(new ControlNowTimeDTO(controlNowTimeEntry));
            }
            controlMiduleDTOs.add(controlMiduleDTO);
        }

        return controlMiduleDTOs;
    }

    private ControlSchoolTimeEntry getSimple( List<ControlSchoolTimeEntry> conlist,long current,String str){
        ControlSchoolTimeEntry controlSchoolTimeEntry = null;
        for(ControlSchoolTimeEntry controlSchoolTimeEntry1 : conlist){
            String stm = controlSchoolTimeEntry1.getStartTime();
            long sl = 0l;
            if(stm != null && !stm.equals("")){
                sl = DateTimeUtils.getStrToLongTime(str+" "+stm, "yyyy-MM-dd HH:mm:ss");
            }
            String etm = controlSchoolTimeEntry1.getEndTime();
            long el = 0l;
            if(etm != null && !etm.equals("")){
                el = DateTimeUtils.getStrToLongTime(str+" "+etm, "yyyy-MM-dd HH:mm:ss");
            }
            if(current>sl && current < el){
                controlSchoolTimeEntry = controlSchoolTimeEntry1;
            }
        }
        if(controlSchoolTimeEntry==null){//无上课时间，直接取第一个
            controlSchoolTimeEntry = conlist.get(0);
        }

        return controlSchoolTimeEntry;
    }


    public List<ControlMiduleDTO> getFourAllMessageForSheTea(List<ObjectId> communityIds){
        List<ControlMiduleDTO> controlMiduleDTOs = new ArrayList<ControlMiduleDTO>();
        List<SchoolCommunityEntry> schoolCommunityEntries = schoolCommunityDao.getReviewList2(communityIds);
        List<ObjectId> oids = new ArrayList<ObjectId>();
        Map<ObjectId,ObjectId> obmap = new HashMap<ObjectId, ObjectId>();
        for(SchoolCommunityEntry schoolCommunityEntry: schoolCommunityEntries){
            oids.add(schoolCommunityEntry.getSchoolId());
            obmap.put(schoolCommunityEntry.getCommunityId(),schoolCommunityEntry.getSchoolId());
        }

        //学校设置
        List<ControlSchoolTimeEntry> controlSchoolTimeEntries = controlSchoolTimeDao.getAllSchoolEntryList(oids);
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
                        map1.put(controlSchoolTimeEntry.getParentId(),controlSchoolTimeEntry);
                    }
                }
            }else if(controlSchoolTimeEntry.getType()==2 && controlSchoolTimeEntry.getDataTime() != null && controlSchoolTimeEntry.getDataTime().equals(str)){
                map2.put(controlSchoolTimeEntry.getParentId(),controlSchoolTimeEntry);//特殊
            }else if(controlSchoolTimeEntry.getType()==1 && controlSchoolTimeEntry.getWeek()==w){
                map3.put(controlSchoolTimeEntry.getParentId(),controlSchoolTimeEntry);//周常
            }
        }
        ObjectId objectId = new ObjectId();
        //默认设置
        List<ControlSchoolTimeEntry> controlSchoolTimeEntries2 = controlSchoolTimeDao.getAllEntryList();
        for(ControlSchoolTimeEntry controlSchoolTimeEntry : controlSchoolTimeEntries2){
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
                        map1.put(objectId,controlSchoolTimeEntry);
                    }
                }
            }else if(controlSchoolTimeEntry.getType()==2 && controlSchoolTimeEntry.getDataTime() != null && controlSchoolTimeEntry.getDataTime().equals(str)){
                map2.put(objectId,controlSchoolTimeEntry);//特殊
            }else if(controlSchoolTimeEntry.getType()==1 && controlSchoolTimeEntry.getWeek()==w){
                map3.put(objectId,controlSchoolTimeEntry);//周常
            }
        }


        //特殊设置
        List<ControlNowTimeEntry> controlNowTimeEntries = controlNowTimeDao.getOtherEntryList(str, communityIds);
        Map<ObjectId,ControlNowTimeEntry>  map4 = new HashMap<ObjectId, ControlNowTimeEntry>();
        for(ControlNowTimeEntry entry3: controlNowTimeEntries){
            map4.put(entry3.getCommunityId(),entry3);
        }
        //计算
        for(ObjectId oid: communityIds){
            ControlNowTimeEntry entry3 =  map4.get(oid);
            ControlMiduleDTO controlMiduleDTO = new ControlMiduleDTO();
            ObjectId ssid = obmap.get(oid);
            //获取应该执行的设置
            ControlSchoolTimeEntry controlSchoolTimeEntry = new ControlSchoolTimeEntry();
           if(ssid !=null && map1.get(ssid)!=null){
               controlSchoolTimeEntry = map1.get(ssid);
           }else if(ssid !=null && map2.get(ssid)!=null){
               controlSchoolTimeEntry = map2.get(ssid);
           }else if(ssid !=null && map3.get(ssid)!=null){
               controlSchoolTimeEntry = map3.get(ssid);
           }else if(map1.get(objectId)!=null){
               controlSchoolTimeEntry = map1.get(objectId);
           }else if(map2.get(objectId)!=null){
               controlSchoolTimeEntry = map2.get(objectId);
           }else if(map3.get(objectId)!=null){
               controlSchoolTimeEntry = map3.get(objectId);
           }

            controlSchoolTimeEntry.setCommunityId(oid);
            controlMiduleDTO.setDto(new ControlSchoolTimeDTO(controlSchoolTimeEntry));
            //ControlSchoolTimeDTO controlSchoolTimeDTO =  new ControlSchoolTimeDTO();
            String stm = controlSchoolTimeEntry.getStartTime();
            long sl = 0l;
            if(stm != null && !stm.equals("")){
                sl = DateTimeUtils.getStrToLongTime(str+" "+stm, "yyyy-MM-dd HH:mm:ss");
            }
            String etm = controlSchoolTimeEntry.getEndTime();
            long el = 0l;
            if(etm != null && !etm.equals("")){
                el = DateTimeUtils.getStrToLongTime(str+" "+etm, "yyyy-MM-dd HH:mm:ss");
            }
            if(current>sl && current < el){
                controlMiduleDTO.setTime(el-current);
                controlMiduleDTO.setIsControl(1);
            }else{
                controlMiduleDTO.setTime(0);
                controlMiduleDTO.setIsControl(2);
            }
            //溢出时间处理  controlMiduleDTO
            if(entry3 != null && current < el){
                String stm2 = entry3.getStartTime();
                long sl2 = 0l;
                if(stm2 != null && !stm2.equals("")){
                    sl2 = DateTimeUtils.getStrToLongTime(str+" "+stm2, "yyyy-MM-dd HH:mm:ss");
                }
                String etm2 = entry3.getEndTime();
                long el2 = 0l;
                if(etm2 != null && !etm2.equals("")){
                    el2 = DateTimeUtils.getStrToLongTime(str+" "+etm2, "yyyy-MM-dd HH:mm:ss");
                }
                if(current>sl2 && current < el2){
                    controlMiduleDTO.setTime(el2-current);
                    controlMiduleDTO.setIsControl(3);
                }else{
                    controlMiduleDTO.setTime(0);
                }
                controlMiduleDTO.setThird(new ControlNowTimeDTO(entry3));
            }else{
                ControlNowTimeEntry controlNowTimeEntry = new ControlNowTimeEntry();
                controlMiduleDTO.setThird(new ControlNowTimeDTO(controlNowTimeEntry));
            }
            controlMiduleDTOs.add(controlMiduleDTO);
        }

        return controlMiduleDTOs;
    }
    public List<ControlMiduleDTO> getFourAllMessageForTea(List<ObjectId> communityIds){
        List<ControlMiduleDTO> controlMiduleDTOs = new ArrayList<ControlMiduleDTO>();
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        String str = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,10);
        long zero = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(zero);
        //通用设置
        //时间段设置
        ControlSchoolTimeEntry schoolTimeDTO = this.getDateTime();
        ControlSchoolTimeEntry entry2 = null;
        if(schoolTimeDTO !=null){
            entry2 = schoolTimeDTO;
        }else{
            entry2 = controlSchoolTimeDao.getOtherEntry(2,dateNowStr);
        }
        long time = 0l;
        int isControl = 0;
      //  ControlSchoolTimeDTO dto = null;
        ControlSchoolTimeEntry controlSchoolTimeEntry = null;
        if(entry2 != null){
            String stm = entry2.getStartTime();
            long sl = 0l;
            if(stm != null && !stm.equals("")){
                sl = DateTimeUtils.getStrToLongTime(dateNowStr+" "+stm, "yyyy-MM-dd HH:mm:ss");
            }
            String etm = entry2.getEndTime();
            long el = 0l;
            if(etm != null && !etm.equals("")){
                el = DateTimeUtils.getStrToLongTime(dateNowStr+" "+etm, "yyyy-MM-dd HH:mm:ss");
            }
            if(current>sl && current < el){
                isControl=1;
            }else{
                isControl=2;
            }
            controlSchoolTimeEntry = entry2;
            //dto = new ControlSchoolTimeDTO(entry2);
        }else{
            //通用管控时间
            Calendar cal = Calendar.getInstance();
            int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if(w==0){
                w= 7;
            }
            ControlSchoolTimeEntry entry = controlSchoolTimeDao.getEntry(w);
            if(entry != null){
                String stm = entry.getStartTime();
                long sl = 0l;
                if(stm != null && !stm.equals("")){
                    sl = DateTimeUtils.getStrToLongTime(dateNowStr+" "+stm, "yyyy-MM-dd HH:mm:ss");
                }
                String etm = entry.getEndTime();
                long el = 0l;
                if(etm != null && !etm.equals("")){
                    el = DateTimeUtils.getStrToLongTime(dateNowStr+" "+etm, "yyyy-MM-dd HH:mm:ss");
                }
                if(current>sl && current < el){
                    time=el-current;
                    isControl=1;
                }else{
                    time = 0l;
                    isControl=2;
                }
                controlSchoolTimeEntry = entry;
               // dto = new ControlSchoolTimeDTO(entry);
            }else{
                time = 0l;
                isControl=2;
                //dto = new ControlSchoolTimeDTO();
                controlSchoolTimeEntry = new ControlSchoolTimeEntry();
            }
        }
        //特殊设置
        List<ControlNowTimeEntry> controlNowTimeEntries = controlNowTimeDao.getOtherEntryList(dateNowStr, communityIds);
        Map<ObjectId,ControlNowTimeEntry>  map3 = new HashMap<ObjectId, ControlNowTimeEntry>();

        for(ControlNowTimeEntry entry3: controlNowTimeEntries){
            map3.put(entry3.getCommunityId(),entry3);
        }
        for(ObjectId oid: communityIds){
            ControlNowTimeEntry entry3 =  map3.get(oid);
            ControlMiduleDTO controlMiduleDTO = new ControlMiduleDTO();
            controlSchoolTimeEntry.setCommunityId(oid);
            controlMiduleDTO.setDto(new ControlSchoolTimeDTO(controlSchoolTimeEntry));
            //ControlSchoolTimeDTO controlSchoolTimeDTO =  new ControlSchoolTimeDTO();
            controlMiduleDTO.setTime(time);
            controlMiduleDTO.setIsControl(isControl);
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
                    controlMiduleDTO.setTime(el-current);
                    controlMiduleDTO.setIsControl(3);
                }else{
                    controlMiduleDTO.setTime(0);
                }
                controlMiduleDTO.setThird(new ControlNowTimeDTO(entry3));
            }else{
                ControlNowTimeEntry controlNowTimeEntry = new ControlNowTimeEntry();
                controlMiduleDTO.setThird(new ControlNowTimeDTO(controlNowTimeEntry));
            }
            controlMiduleDTOs.add(controlMiduleDTO);
        }



        return controlMiduleDTOs;
    }

    /**
     * 计算时间段
     * @return
     */
    public ControlSchoolTimeEntry getDateTime(){
        ControlSchoolTimeDTO controlSchoolTimeDTO =new ControlSchoolTimeDTO();
        List<ControlSchoolTimeEntry> entryList = controlSchoolTimeDao.getAllDuringList();
        if(entryList.size()>0){
            boolean flag = false;
            for(ControlSchoolTimeEntry controlSchoolTimeEntry:entryList){
                String[] arg = controlSchoolTimeEntry.getDataTime().split("=");
                if(arg.length==2){
                    String startStr = arg[0];
                    String endStr = arg[1];
                    long sl = 0l;
                    String stm = controlSchoolTimeEntry.getStartTime();
                    if(startStr != null && !startStr.equals("")){
                        sl = DateTimeUtils.getStrToLongTime(startStr+" "+"00:00:00", "yyyy-MM-dd HH:mm:ss");
                    }
                    long el = 0l;
                    String etm = controlSchoolTimeEntry.getEndTime();
                    if(endStr != null && !endStr.equals("")){
                        el = DateTimeUtils.getStrToLongTime(endStr+" "+"23:59:59", "yyyy-MM-dd HH:mm:ss");
                    }
                    long current = System.currentTimeMillis();
                    if(current>sl && current < el){
                        controlSchoolTimeDTO.setStartTime(stm);
                        controlSchoolTimeDTO.setEndTime(etm);
                        controlSchoolTimeDTO.setType(3);
                        controlSchoolTimeDTO.setDataTime(DateTimeUtils.getLongToStrTimeTwo(current).substring(0,11));
                        //controlSchoolTimeDTO.set
                        flag = true;
                        break;
                    }
                }
            }
            if(flag){
                return controlSchoolTimeDTO.buildAddEntry();
            }else{
                return null;
            }
        }else{
            return null;
        }
    }
    public Map<String,Object> getNewSchoolOneMessageForTea(ObjectId teacherId,ObjectId communityId){
        Map<String,Object> map = new HashMap<String, Object>();
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        //long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒
        String str = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,10);
        long zero = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(zero);
        //通用管控时间
        Calendar scal = Calendar.getInstance();
        int sw = scal.get(Calendar.DAY_OF_WEEK) - 1;
        if(sw==0){
            sw= 7;
        }
        //通用设置
        //ControlSchoolTimeEntry entry2 = controlSchoolTimeDao.getOtherEntry(2,dateNowStr);
        //判断是否有学校设置
        //学校设置
        SchoolCommunityEntry schoolCommunityEntries = schoolCommunityDao.getEntryById(communityId);
        List<ControlSchoolTimeEntry> controlSchoolTimeEntryList = new ArrayList<ControlSchoolTimeEntry>();
        if(schoolCommunityEntries!=null && schoolCommunityEntries.getSchoolId()!=null){
            controlSchoolTimeEntryList = controlSchoolTimeDao.getOneSchoolEntryList(schoolCommunityEntries.getSchoolId());
        }
        //有，使用学校配置，没有使用默认配置
        if(controlSchoolTimeEntryList.size()>0){

        }else{
            controlSchoolTimeEntryList = controlSchoolTimeDao.getAllEntryList();
        }
        long endTime2 = 0l;
        if(controlSchoolTimeEntryList.size()>0){
            List<ControlSchoolTimeEntry> newList1 = new ArrayList<ControlSchoolTimeEntry>();
            List<ControlSchoolTimeEntry> newList2 = new ArrayList<ControlSchoolTimeEntry>();
            List<ControlSchoolTimeEntry> newList3 = new ArrayList<ControlSchoolTimeEntry>();
            for(ControlSchoolTimeEntry controlSchoolTimeEntry : controlSchoolTimeEntryList){
                if(controlSchoolTimeEntry.getType()==3){//时间段
                    String[] arg = controlSchoolTimeEntry.getDataTime().split("=");
                    if(arg.length==2){
                        String startStr = arg[0];
                        String endStr = arg[1];
                        long sl = 0l;
                        String stm = controlSchoolTimeEntry.getStartTime();
                        if(startStr != null && !startStr.equals("")){
                            sl = DateTimeUtils.getStrToLongTime(startStr+" "+"00:00:00", "yyyy-MM-dd HH:mm:ss");
                        }
                        long el = 0l;
                        String etm = controlSchoolTimeEntry.getEndTime();
                        if(endStr != null && !endStr.equals("")){
                            el = DateTimeUtils.getStrToLongTime(endStr+" "+"23:59:59", "yyyy-MM-dd HH:mm:ss");
                        }
                        if(current>sl && current < el){
                            newList1.add(controlSchoolTimeEntry);
                        }
                    }
                }else if(controlSchoolTimeEntry.getType()==2 && controlSchoolTimeEntry.getDataTime() != null && controlSchoolTimeEntry.getDataTime().equals(str)){
                    newList2.add(controlSchoolTimeEntry);
                }else if(controlSchoolTimeEntry.getType()==1 && controlSchoolTimeEntry.getWeek()==sw){
                    newList3.add(controlSchoolTimeEntry);
                }
            }
            ControlSchoolTimeEntry nowControlSchoolTimeEntry4 = null;
            if(newList1.size()>0){
                nowControlSchoolTimeEntry4 = getSimple(newList1,current,str);
            }else if(newList2.size()>0){
                nowControlSchoolTimeEntry4 = getSimple(newList2,current,str);
            }else if(newList3.size()>0){
                nowControlSchoolTimeEntry4 = getSimple(newList3,current,str);
            }
            if(nowControlSchoolTimeEntry4!=null){
                String stm = nowControlSchoolTimeEntry4.getStartTime();
                long sl = 0l;
                if(stm != null && !stm.equals("")){
                    sl = DateTimeUtils.getStrToLongTime(dateNowStr+" "+stm, "yyyy-MM-dd HH:mm:ss");
                }
                String etm = nowControlSchoolTimeEntry4.getEndTime();
                long el = 0l;
                if(etm != null && !etm.equals("")){
                    el = DateTimeUtils.getStrToLongTime(dateNowStr+" "+etm, "yyyy-MM-dd HH:mm:ss");
                }
                endTime2 = el;
                if(current>sl && current < el){
                    map.put("isControl",1);
                }else{
                    map.put("isControl",2);
                }
                map.put("dto",new ControlSchoolTimeDTO(nowControlSchoolTimeEntry4));
            }
        }
        //特殊设置
        ControlNowTimeEntry entry3 = controlNowTimeDao.getOtherEntry(dateNowStr,communityId);
        if(entry3 != null && current < endTime2){
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
                map.put("time",el-current);
                map.put("isControl",3);
            }else{
                map.put("time",0);
            }
            map.put("third",new ControlNowTimeDTO(entry3));
        }else{
            ControlNowTimeEntry controlNowTimeEntry = new ControlNowTimeEntry();
            map.put("third",new ControlNowTimeDTO(controlNowTimeEntry));
        }
        return map;
    }
    public Map<String,Object> getSchoolOneMessageForTea(ObjectId teacherId,ObjectId communityId){
        Map<String,Object> map = new HashMap<String, Object>();
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        //long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒
        String str = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,10);
        long zero = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(zero);
        //通用管控时间
        Calendar scal = Calendar.getInstance();
        int sw = scal.get(Calendar.DAY_OF_WEEK) - 1;
        if(sw==0){
            sw= 7;
        }
        //通用设置
        //ControlSchoolTimeEntry entry2 = controlSchoolTimeDao.getOtherEntry(2,dateNowStr);
        //判断是否有学校设置
        //学校设置
        SchoolCommunityEntry schoolCommunityEntries = schoolCommunityDao.getEntryById(communityId);
        List<ControlSchoolTimeEntry> controlSchoolTimeEntryList = new ArrayList<ControlSchoolTimeEntry>();
        if(schoolCommunityEntries!=null && schoolCommunityEntries.getSchoolId()!=null){
            controlSchoolTimeEntryList = controlSchoolTimeDao.getOneSchoolEntryList(schoolCommunityEntries.getSchoolId());
        }
        //有，使用学校配置，没有使用默认配置
        if(controlSchoolTimeEntryList.size()>0){

        }else{
            controlSchoolTimeEntryList = controlSchoolTimeDao.getAllEntryList();
        }
        long endTime2 = 0l;
        if(controlSchoolTimeEntryList.size()>0){
            ControlSchoolTimeEntry nowControlSchoolTimeEntry1 = null;
            ControlSchoolTimeEntry nowControlSchoolTimeEntry2 = null;
            ControlSchoolTimeEntry nowControlSchoolTimeEntry3 = null;
            for(ControlSchoolTimeEntry controlSchoolTimeEntry : controlSchoolTimeEntryList){
                if(controlSchoolTimeEntry.getType()==3){//时间段
                    String[] arg = controlSchoolTimeEntry.getDataTime().split("=");
                    if(arg.length==2){
                        String startStr = arg[0];
                        String endStr = arg[1];
                        long sl = 0l;
                        String stm = controlSchoolTimeEntry.getStartTime();
                        if(startStr != null && !startStr.equals("")){
                            sl = DateTimeUtils.getStrToLongTime(startStr+" "+"00:00:00", "yyyy-MM-dd HH:mm:ss");
                        }
                        long el = 0l;
                        String etm = controlSchoolTimeEntry.getEndTime();
                        if(endStr != null && !endStr.equals("")){
                            el = DateTimeUtils.getStrToLongTime(endStr+" "+"23:59:59", "yyyy-MM-dd HH:mm:ss");
                        }
                        if(current>sl && current < el){
                            nowControlSchoolTimeEntry1= controlSchoolTimeEntry;
                            break;
                        }
                    }
                }else if(controlSchoolTimeEntry.getType()==2 && controlSchoolTimeEntry.getDataTime() != null && controlSchoolTimeEntry.getDataTime().equals(str)){
                    nowControlSchoolTimeEntry2= controlSchoolTimeEntry;
                }else if(controlSchoolTimeEntry.getType()==1 && controlSchoolTimeEntry.getWeek()==sw){
                    nowControlSchoolTimeEntry3= controlSchoolTimeEntry;
                }
            }
            ControlSchoolTimeEntry nowControlSchoolTimeEntry4 = null;
            if(nowControlSchoolTimeEntry1!=null){
                nowControlSchoolTimeEntry4 = nowControlSchoolTimeEntry1;
            }else if(nowControlSchoolTimeEntry2!=null){
                nowControlSchoolTimeEntry4 = nowControlSchoolTimeEntry2;
            }else if(nowControlSchoolTimeEntry3!=null){
                nowControlSchoolTimeEntry4 = nowControlSchoolTimeEntry3;
            }
            if(nowControlSchoolTimeEntry4!=null){
                String stm = nowControlSchoolTimeEntry4.getStartTime();
                long sl = 0l;
                if(stm != null && !stm.equals("")){
                    sl = DateTimeUtils.getStrToLongTime(dateNowStr+" "+stm, "yyyy-MM-dd HH:mm:ss");
                }
                String etm = nowControlSchoolTimeEntry4.getEndTime();
                long el = 0l;
                if(etm != null && !etm.equals("")){
                    el = DateTimeUtils.getStrToLongTime(dateNowStr+" "+etm, "yyyy-MM-dd HH:mm:ss");
                }
                endTime2 = el;
                if(current>sl && current < el){
                    map.put("isControl",1);
                }else{
                    map.put("isControl",2);
                }
                map.put("dto",new ControlSchoolTimeDTO(nowControlSchoolTimeEntry4));
            }
        }
        //特殊设置
        ControlNowTimeEntry entry3 = controlNowTimeDao.getOtherEntry(dateNowStr,communityId);
        if(entry3 != null && current < endTime2){
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
                map.put("time",el-current);
                map.put("isControl",3);
            }else{
                map.put("time",0);
            }
            map.put("third",new ControlNowTimeDTO(entry3));
        }else{
            ControlNowTimeEntry controlNowTimeEntry = new ControlNowTimeEntry();
            map.put("third",new ControlNowTimeDTO(controlNowTimeEntry));
        }
        return map;
    }
    public Map<String,Object> getAllMessageForTea(ObjectId teacherId,ObjectId communityId){
        Map<String,Object> map = new HashMap<String, Object>();
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        //long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒
        String str = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,10);
        long zero = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(zero);
        //通用设置
        //ControlSchoolTimeEntry entry2 = controlSchoolTimeDao.getOtherEntry(2,dateNowStr);
        //时间段设置
        ControlSchoolTimeEntry schoolTimeDTO = this.getDateTime();
        ControlSchoolTimeEntry entry2 = null;
        if(schoolTimeDTO !=null){
            entry2 = schoolTimeDTO;
        }else{
            entry2 = controlSchoolTimeDao.getOtherEntry(2,dateNowStr);
        }
        if(entry2 != null){
            String stm = entry2.getStartTime();
            long sl = 0l;
            if(stm != null && !stm.equals("")){
                sl = DateTimeUtils.getStrToLongTime(dateNowStr+" "+stm, "yyyy-MM-dd HH:mm:ss");
            }
            String etm = entry2.getEndTime();
            long el = 0l;
            if(etm != null && !etm.equals("")){
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
            if(w==0){
                w= 7;
            }
            ControlSchoolTimeEntry entry = controlSchoolTimeDao.getEntry(w);
            if(entry != null){
                String stm = entry.getStartTime();
                long sl = 0l;
                if(stm != null && !stm.equals("")){
                    sl = DateTimeUtils.getStrToLongTime(dateNowStr+" "+stm, "yyyy-MM-dd HH:mm:ss");
                }
                String etm = entry.getEndTime();
                long el = 0l;
                if(etm != null && !etm.equals("")){
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
            if(stm != null && !stm.equals("")){
                sl = DateTimeUtils.getStrToLongTime(dateNowStr+" "+stm, "yyyy-MM-dd HH:mm:ss");
            }
            String etm = entry3.getEndTime();
            long el = 0l;
            if(etm != null && !etm.equals("")){
                el = DateTimeUtils.getStrToLongTime(dateNowStr+" "+etm, "yyyy-MM-dd HH:mm:ss");
            }
            if(current>sl && current < el){
                map.put("time",el-current);
                map.put("isControl",3);
            }else{
                map.put("time",0);
            }
            map.put("third",new ControlNowTimeDTO(entry3));
        }else{
            ControlNowTimeEntry controlNowTimeEntry = new ControlNowTimeEntry();
            map.put("third",new ControlNowTimeDTO(controlNowTimeEntry));
        }
        return map;
    }

    /**
     * 获得用户的所有具有管理员权限的社区id
     *
     */
    public List<ObjectId> getMyRoleList(ObjectId userId){
        List<ObjectId> olsit = memberDao.getManagerGroupIdsByUserId(userId);
        List<ObjectId> clist = new ArrayList<ObjectId>();
        List<ObjectId> mlist =   groupDao.getGroupIdsList(olsit);
        return mlist;
    }
    public List<ObjectId> getMySimpleList(ObjectId userId){
        List<ObjectId> olsit = memberDao.getGroupIdsByUserId(userId);
       // List<ObjectId> clist = new ArrayList<ObjectId>();
        List<ObjectId> mlist =   groupDao.getGroupIdsList(olsit);
        return mlist;
    }
    //老师修改管控状态
    public void deleteControlTime(ObjectId userId,ObjectId communityId){
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        //long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        String str2 = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,10);
        long zero = DateTimeUtils.getStrToLongTime(str2, "yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(zero);
        controlNowTimeDao.deleteControlTime(communityId,dateNowStr);
        List<String> objectIdList = newVersionBindService.getStudentIdListByCommunityId(communityId);
        //向学生端推送消息
        try {
            MQTTSendMsg.sendMessageList(MQTTType.phone.getEname(),objectIdList,current);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateString = formatter.format(new Date(current));
            String scontent = dateString + MQTTType.phone.getEname();
            this.addControlVersion(communityId,userId,scontent,2);
            List<ObjectId> oids = new ArrayList<ObjectId>();
            for(String str : objectIdList){
                oids.add(new ObjectId(str));
            }
            controlTimeDao.delAllEntry(oids, current);
        }catch (Exception e){

        }
    }
    //获得今天的放学时间
    public long getBackHomeTime(long time){
        //获得当前时间
        long current=System.currentTimeMillis();
        //获得时间批次
        //long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        String str = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,10);
        long zero = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
        SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr4 = sdf3.format(zero);
        //通用设置
        ControlSchoolTimeEntry entry2 = controlSchoolTimeDao.getOtherEntry(2,dateNowStr4);
        long el = 0l;
        if(entry2 == null){
            Calendar cal = Calendar.getInstance();
            int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
            if(w==0){
                w= 7;
            }
            ControlSchoolTimeEntry entry = controlSchoolTimeDao.getEntry(w);
            String etm = entry.getEndTime();

            if(etm != null && !etm.equals("")){
                el = DateTimeUtils.getStrToLongTime(dateNowStr4+" "+etm, "yyyy-MM-dd HH:mm:ss");
            }

        }else{
            String etm = entry2.getEndTime();
            if(etm != null && !etm.equals("")){
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
        //long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        String str2 = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,10);
        long zero = DateTimeUtils.getStrToLongTime(str2, "yyyy-MM-dd");
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
            MQTTSendMsg.sendMessageList(MQTTType.phone.getEname(),objectIdList,current);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateString = formatter.format(new Date(current));
            String scontent = dateString + MQTTType.phone.getEname();
            this.addControlVersion(communityId,userId,scontent,2);
            List<ObjectId> oids = new ArrayList<ObjectId>();
            for(String str : objectIdList){
                oids.add(new ObjectId(str));
            }
            controlTimeDao.delAllEntry(oids, current);
        }catch (Exception e){

        }
    }

    public void loadStudentMap(ObjectId userId,ObjectId sonId){
        //向学生端推送消息
        long current=System.currentTimeMillis();
        try {
            MQTTSendMsg.sendMessage(MQTTType.map.getEname(), sonId.toString(),current);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateString = formatter.format(new Date(current));
            String scontent = dateString + MQTTType.map.getEname();
            this.addControlVersion(sonId,userId,scontent,2);
            controlTimeDao.delEntry(sonId, current);
        }catch (Exception e){

        }

    }

    /**
     * 批量增加应用记录
     * @param list
     */
    public void addRedDotEntryBatch(List<ControlAppResultDTO> list,ObjectId userId,ObjectId parentId,long zero,long addiction) {
        List<DBObject> dbList = new ArrayList<DBObject>();
        for (int i = 0; list != null && i < list.size(); i++) {
            ControlAppResultDTO si = list.get(i);
            ControlAppResultEntry obj = si.buildAddEntry();
            obj.setUserId(userId);
            obj.setParentId(parentId);
            obj.setDateTime(zero);
            //obj.setAddiction(addiction);
            dbList.add(obj.getBaseEntry());
            obj = null;
        }
        //导入新纪录
        if(dbList.size()>0) {
            controlAppResultDao.addBatch(dbList);
            //导入缓存记录
            controlAppResultDao.addLinBatch(dbList);
        }
        dbList = null;
       // System.gc();
    }

    /**
     * 发现首页加载
     * @param userId
     * @return
     */
    public List<AppDetailDTO> getThirdAppList(ObjectId userId){
        List<AppDetailDTO> detailDTOs = new ArrayList<AppDetailDTO>();
        List<AppDetailEntry> appDetailEntries = appDetailDao.getAppByCondition("");
        appDetailEntries.addAll(getSchoolAppList(userId));
        for (AppDetailEntry entry : appDetailEntries) {
            detailDTOs.add(new AppDetailDTO(entry));
        }
        getNewThirdAppList(userId);

        return detailDTOs;
    }

    /**
     * 发现页面（校本资源显示）
     */
    public List<AppDetailEntry> getSchoolAppList(ObjectId userId){
        //List<ObjectId> oids = getMyRoleList(userId);
        List<ObjectId> oids = getMySimpleList(userId);
        List<SchoolCommunityEntry> schoolCommunityEntries = schoolCommunityDao.getReviewList2(oids);
        List<ObjectId> oisd = new ArrayList<ObjectId>();
        for(SchoolCommunityEntry schoolCommunityEntry:schoolCommunityEntries){
            oisd.add(schoolCommunityEntry.getSchoolId());
        }
        List<AppDetailEntry> appDetailEntries = new ArrayList<AppDetailEntry>();
        if(oisd.size()>0){
            List<ObjectId> objectIdList = homeSchoolDao.getSchoolObjectList(oisd);
            List<SchoolAppEntry> schoolAppEntries = schoolAppDao.getList(objectIdList);
            List<ObjectId> appIds = new ArrayList<ObjectId>();
            for(SchoolAppEntry schoolAppEntry: schoolAppEntries){
                if(schoolAppEntry.getAppIdList()!=null && schoolAppEntry.getAppIdList().size()>0){
                    appIds.addAll(schoolAppEntry.getAppIdList());
                }
            }
           // appDetailEntries = appDetailDao.searchSchoolAppByIds(schoolAppEntry.getAppIdList());
            appDetailEntries = appDetailDao.searchSchoolAppByIds(appIds);
        }
        return appDetailEntries;
    }





    public void getNewThirdAppList(ObjectId userId){
        //查询所有该用户的绑定关系
        List<ObjectId> childIds = newVersionBindRelationDao.getIdsByMainUserId(userId);
        for(ObjectId sonId : childIds){
            ControlAppUserEntry entry2 = controlAppUserDao.getEntry(userId,sonId);
            if(entry2 != null ){

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
                dto.setParentId(userId.toString());
                ControlAppUserEntry entry1 = dto.buildAddEntry();
                //添加系统设置
                controlAppUserDao.addEntry(entry1);
            }
        }
    }

    public Map<String,Object> getUserSendAppList(ObjectId userId){
        Map<String,Object> map = new HashMap<String, Object>();
        //
        //查询所有该用户的绑定关系
        List<ObjectId> childIds = newVersionBindRelationDao.getIdsByMainUserId(userId);
        if(childIds.size()>0){
            map.put("isParent",true);
        }else{
            map.put("isParent",false);
        }
        childIds.add(userId);
        List<UserDetailInfoDTO> infos = userService.findUserInfoByIds(childIds);
        Map<String,UserDetailInfoDTO> map2 = new HashMap<String, UserDetailInfoDTO>();
        if(infos != null && infos.size()>0){
            for(UserDetailInfoDTO dto4 : infos){
                map2.put(dto4.getId(),dto4);
            }
        }

        //查询具有管理员权限的社区
        List<ObjectId> oids = getMyRoleList(userId);
        if(oids.size()>0){
            map.put("isTeacher",true);
        }else{
            map.put("isTeacher",false);
        }
        oids.add(userId);
        List<CommunityEntry> communityEntries = communityDao.findByObjectIds(oids);
        Map<String,CommunityEntry> map3 = new HashMap<String, CommunityEntry>();
        if(communityEntries != null && communityEntries.size()>0){
            for(CommunityEntry dto4 : communityEntries){
                map3.put(dto4.getID().toString(),dto4);
            }
        }
        childIds.remove(userId);
        oids.remove(userId);

        TeacherApproveEntry teacherApproveEntry = teacherApproveDao.getEntry(userId);
        if(teacherApproveEntry != null && teacherApproveEntry.getType()==2){
            map.put("isRen",true);
        }else{
            map.put("isRen",false);
        }
        List<ControlAppUserEntry> entryList1 = controlAppUserDao.getUserSendAppList(userId, childIds);
        List<ControlAppEntry> entryList2 = controlAppDao.getEntryListByUserId(userId, oids);
        List<ResultAppListDTO> dtos1 = new ArrayList<ResultAppListDTO>();
        List<ResultAppListDTO> dtos2 = new ArrayList<ResultAppListDTO>();
        List<ObjectId> objectIds = new ArrayList<ObjectId>();
        List<ObjectId> delIds = new ArrayList<ObjectId>();
        List<ObjectId> delIds2 = new ArrayList<ObjectId>();
        List<ObjectId> objectIds2 = new ArrayList<ObjectId>();
        if(entryList1.size()>0){
            for(ControlAppUserEntry entry : entryList1){
                if(!objectIds.contains(entry.getUserId())){
                    objectIds.add(entry.getUserId());
                    UserDetailInfoDTO infoDTO = map2.get(entry.getUserId().toString());
                    ResultAppListDTO dto = new ResultAppListDTO();
                    String name = StringUtils.isNotEmpty(infoDTO.getNickName())?infoDTO.getNickName():infoDTO.getUserName();
                    dto.setName(name);
                    dto.setUrl(infoDTO.getImgUrl());
                    if(entry.getAppIdList() != null){
                        List<AppDetailEntry> entries3 = appDetailDao.getEntriesByIds(entry.getAppIdList());
                        dto.setCount(entries3.size());
                    }else{
                        dto.setCount(0);
                    }
                    dto.setContactId(entry.getUserId().toString());
                    dto.setType(1);//孩子
                    childIds.remove(entry.getUserId());
                    dtos1.add(dto);
                }else{//孩子id 已包含
                    if(!delIds.contains(entry.getID())){//非已有对象
                        controlAppUserDao.delEntry(entry.getID());
                    }
                }
                delIds.add(entry.getID());
            }
        }
        if(entryList2.size()>0){
            for(ControlAppEntry entry : entryList2){
                if(!objectIds2.contains(entry.getCommunityId())) {
                    objectIds2.add(entry.getCommunityId());
                    CommunityEntry entry2 = map3.get(entry.getCommunityId().toString());
                    ResultAppListDTO dto = new ResultAppListDTO();
                    dto.setName(entry2.getCommunityName());
                    dto.setUrl(getNewLogo(entry2.getCommunityLogo()));
                    if (entry.getAppIdList() != null) {
                        List<AppDetailEntry> entries3 = appDetailDao.getEntriesByIds(entry.getAppIdList());
                        dto.setCount(entries3.size());
                    } else {
                        dto.setCount(0);
                    }
                    dto.setContactId(entry.getCommunityId().toString());
                    dto.setType(2);//社区
                    oids.remove(entry.getCommunityId());
                    dtos2.add(dto);
                }else{//社区id 已包含
                    if(!delIds2.contains(entry.getID())){//非已有对象
                        controlAppDao.delEntry(entry.getID());
                    }
                }
                delIds2.add(entry.getID());
            }
        }
        for(ObjectId id : childIds){
            UserDetailInfoDTO infoDTO = map2.get(id.toString());
            ResultAppListDTO dto = new ResultAppListDTO();
            String name = StringUtils.isNotEmpty(infoDTO.getNickName())?infoDTO.getNickName():infoDTO.getUserName();
            dto.setName(name);
            dto.setUrl(infoDTO.getImgUrl());
            dto.setCount(0);
            dto.setContactId(id.toString());
            dto.setType(1);//孩子
            dtos1.add(dto);
        }

        for(ObjectId id : oids){
            CommunityEntry entry2 = map3.get(id.toString());
            ResultAppListDTO dto = new ResultAppListDTO();
            dto.setName(entry2.getCommunityName());
            dto.setUrl(getNewLogo(entry2.getCommunityLogo()));
            dto.setCount(0);
            dto.setContactId(id.toString());
            dto.setType(2);//社区
            dtos2.add(dto);
        }
        map.put("childList",dtos1);
        map.put("communityList",dtos2);
        return map;
    }

    public List<AppDetailDTO> getOneAppList(ObjectId parentId,ObjectId contactId,int type){
        List<AppDetailDTO> dtos = new ArrayList<AppDetailDTO>();
        List<AppDetailEntry> entries = new ArrayList<AppDetailEntry>();
        if(type==1){//孩子
            ControlAppUserEntry entry = controlAppUserDao.getEntry(parentId, contactId);
            if(entry!=null && entry.getAppIdList()!=null && entry.getAppIdList().size() >0){
                entries = appDetailDao.getEntriesByIds(entry.getAppIdList());
                /*if(entry.getAppIdList().size()!=entries.size()){
                    List<ObjectId> oids = new ArrayList<ObjectId>();
                    for(AppDetailEntry entry3 : entries){
                        oids.add(entry3.getID());
                    }
                    entry.setAppIdList(oids);
                    controlAppUserDao.addEntry(entry);
                }*/
            }
        }else{
            ControlAppEntry entry = controlAppDao.getEntry(parentId, contactId);
            if(entry!=null && entry.getAppIdList()!=null && entry.getAppIdList().size() >0){
                entries = appDetailDao.getEntriesByIds(entry.getAppIdList());
                /*if(entry.getAppIdList().size()!=entries.size()){
                    List<ObjectId> oids = new ArrayList<ObjectId>();
                    for(AppDetailEntry entry3 : entries){
                        oids.add(entry3.getID());
                    }
                    entry.setAppIdList(oids);
                    controlAppDao.addEntry(entry);
                }*/
            }




        }
        for(AppDetailEntry entry2 : entries){
            dtos.add(new AppDetailDTO(entry2));
        }
        return dtos;
    }

    public void addAppToChildOrCommunity(ObjectId parentId,ObjectId contactId,int type,ObjectId appId,int isCheckId){
        if(type==1){//孩子
            this.addParentAppList(parentId,contactId,appId,isCheckId);
        }else if(type==2){//社区
            this.addTeaCommunityAppList(parentId,contactId,appId,isCheckId);
        }
    }


    public Map<String,Object> selectOneAppFromOwen(ObjectId userId,ObjectId appId){
        Map<String,Object> map = new HashMap<String, Object>();
        //查询所有该用户的绑定关系
        List<ObjectId> childIds = newVersionBindRelationDao.getIdsByMainUserId(userId);
        if(childIds.size()>0){
            map.put("isParent",true);
        }else{
            map.put("isParent",false);
        }
        childIds.add(userId);
        List<UserDetailInfoDTO> infos = userService.findUserInfoByIds(childIds);
        Map<String,UserDetailInfoDTO> map2 = new HashMap<String, UserDetailInfoDTO>();
        if(infos != null && infos.size()>0){
            for(UserDetailInfoDTO dto4 : infos){
                map2.put(dto4.getId(),dto4);
            }
        }

        //查询具有管理员权限的社区
        List<ObjectId> oids = getMyRoleList(userId);
        if(oids.size()>0){
            map.put("isTeacher",true);
        }else{
            map.put("isTeacher",false);
        }
        oids.add(userId);
        List<CommunityEntry> communityEntries = communityDao.findByObjectIds(oids);
        Map<String,CommunityEntry> map3 = new HashMap<String, CommunityEntry>();
        if(communityEntries != null && communityEntries.size()>0){
            for(CommunityEntry dto4 : communityEntries){
                map3.put(dto4.getID().toString(),dto4);
            }
        }
        childIds.remove(userId);
        oids.remove(userId);
        TeacherApproveEntry teacherApproveEntry = teacherApproveDao.getEntry(userId);
        if(teacherApproveEntry != null && teacherApproveEntry.getType()==2){
            map.put("isRen",true);
        }else{
            map.put("isRen",false);
        }

        AppDetailEntry appDetailEntry = appDetailDao.findEntryById(appId);
        map.put("app",new AppDetailDTO(appDetailEntry));
        List<ControlAppUserEntry> entryList1 = controlAppUserDao.getUserSendAppList(userId,childIds);
        List<ControlAppEntry> entryList2 = controlAppDao.getEntryListByUserId(userId,oids);
        List<ResultUserAppList> dtos1 = new ArrayList<ResultUserAppList>();
        List<ResultUserAppList> dtos2 = new ArrayList<ResultUserAppList>();
        List<ObjectId>  ol = new ArrayList<ObjectId>();
        if(entryList1.size()>0){
            for(ControlAppUserEntry entry : entryList1){
                UserDetailInfoDTO infoDTO = map2.get(entry.getUserId().toString());
                ResultUserAppList dto = new ResultUserAppList();
                String name = StringUtils.isNotEmpty(infoDTO.getNickName())?infoDTO.getNickName():infoDTO.getUserName();
                dto.setName(name);
                dto.setUrl(infoDTO.getImgUrl());
                dto.setContactId(entry.getUserId().toString());
                dto.setType(1);//孩子
                dto.setAppId(appId.toString());
                if(entry.getAppIdList() != null && entry.getAppIdList().contains(appId)){
                    dto.setIsCheck(1);
                }else{
                    dto.setIsCheck(2);
                }
                childIds.remove(entry.getUserId());
                //过滤重复记录
                if(!ol.contains(entry.getUserId())){
                    dtos1.add(dto);
                    ol.add(entry.getUserId());
                }

            }
        }
        if(entryList2.size()>0){
            for(ControlAppEntry entry : entryList2){
                CommunityEntry entry2 = map3.get(entry.getCommunityId().toString());
                ResultUserAppList dto = new ResultUserAppList();
                dto.setName(entry2.getCommunityName());
                dto.setUrl(getNewLogo(entry2.getCommunityLogo()));
                dto.setContactId(entry.getCommunityId().toString());
                dto.setAppId(appId.toString());
                dto.setType(2);//社区
                if(entry.getAppIdList() != null && entry.getAppIdList().contains(appId)){
                    dto.setIsCheck(1);
                }else{
                    dto.setIsCheck(2);
                }
                oids.remove(entry.getCommunityId());
                dtos2.add(dto);
            }
        }
        for(ObjectId id : childIds){
            UserDetailInfoDTO infoDTO = map2.get(id.toString());
            ResultUserAppList dto = new ResultUserAppList();
            String name = StringUtils.isNotEmpty(infoDTO.getNickName())?infoDTO.getNickName():infoDTO.getUserName();
            dto.setName(name);
            dto.setUrl(infoDTO.getImgUrl());
            dto.setAppId(appId.toString());
            dto.setIsCheck(2);
            dto.setContactId(id.toString());
            dto.setType(1);//孩子
            dtos1.add(dto);
        }

        for(ObjectId id : oids){
            CommunityEntry entry2 = map3.get(id.toString());
            ResultUserAppList dto = new ResultUserAppList();
            dto.setName(entry2.getCommunityName());
            dto.setUrl(getNewLogo(entry2.getCommunityLogo()));
            dto.setAppId(appId.toString());
            dto.setIsCheck(2);
            dto.setContactId(id.toString());
            dto.setType(2);//社区
            dtos2.add(dto);
        }
        map.put("childList",dtos1);
        map.put("communityList",dtos2);
        return  map;
    }

/*http://www.fulaan.com/static/images/community/upload.png*/
    //处理社区logo
    public static String getNewLogo(String url){
        String str = "";
        if(url != null && url.contains("http://www.fulaan.com/")){
            str = url.replace("http://www.fulaan.com/", "http://appapi.jiaxiaomei.com/");
        }else{
            str = url;
        }
        if(url != null && url.contains("/static/images/community/upload.png")){
            str = str.replace("upload.png", "head_group.png");
        }
        //http://7xiclj.com1.z0.glb.clouddn.com/585b344ade04cb0539a32724.JPG
        if(url != null && url.contains("http://7xiclj.com1.z0.glb.clouddn.com")){
            str = url + "-head";
        }
        return str;
    }


    //处理
    public void addRemoveToGroupEntry(){
        memberDao.getNoRemoveMembers();
    }


    //获取班级管控版本
    public Map<String,Object> getCommunityVersionList(ObjectId communityId){
        Map<String,Object> map = new HashMap<String, Object>();
        List<ControlVersionDTO> dtos = new ArrayList<ControlVersionDTO>();
        List<ControlVersionEntry> entries = controlVersionDao.getCommunityVersionList(communityId);
        ControlVersionEntry entry = null;
        Map<String,ControlVersionEntry> cmap = new HashMap<String, ControlVersionEntry>();
       /* for(ControlVersionEntry controlVersionEntry : entries){
            if(entry ==null  && controlVersionEntry.getType()==2){
                entry = controlVersionEntry;
            }
            if(entry!=null && entry.getDateTime()<controlVersionEntry.getDateTime()){
                entry = controlVersionEntry;
            }
           *//* if(controlVersionEntry.getType()==1){
                cmap.put(controlVersionEntry.getUserId().toString(),controlVersionEntry);
            }*//*
        }*/
        if(entries.size()>0){
            entry = entries.get(0);
        }
        List<String> objectIdList4 = newVersionBindService.getStudentIdListByCommunityId(communityId);
        List<ObjectId> objectIdList5 = new ArrayList<ObjectId>();
        for(String string : objectIdList4){
            objectIdList5.add(new ObjectId(string));
        }
        long current = System.currentTimeMillis()-7*24*60*60*1000;
        List<ControlVersionEntry> entries3 = controlVersionDao.getAllStudentVersionList(objectIdList5,current);
        for(ControlVersionEntry controlVersionEntry3 : entries3){
            ControlVersionEntry controlVersionEntry4 = cmap.get(controlVersionEntry3.getUserId().toString());
            if(controlVersionEntry4!=null){
                if(controlVersionEntry4.getDateTime()<controlVersionEntry3.getDateTime()){
                    cmap.put(controlVersionEntry3.getUserId().toString(),controlVersionEntry3);
                }
            }else{
                cmap.put(controlVersionEntry3.getUserId().toString(),controlVersionEntry3);
            }
        }
        List<UserDetailInfoDTO> unList = userService.findUserInfoByUserIds(objectIdList4);
        for(UserDetailInfoDTO ddto:unList){
            ControlVersionEntry controlVersionEntry2 = cmap.get(ddto.getId());
            if(controlVersionEntry2 !=null){
                ControlVersionDTO dto = new ControlVersionDTO(controlVersionEntry2);
                boolean flag = false;
                String cacheUserKey= CacheHandler.getUserKey(controlVersionEntry2.getUserId().toString());
                if(org.apache.commons.lang3.StringUtils.isNotEmpty(cacheUserKey)){
                    SessionValue sv = CacheHandler.getSessionValue(cacheUserKey);
                    if (null != sv && !sv.isEmpty()) {
                        flag = true;
                    }
                }
                if(flag){

                }else{
                    dto.setVersion("已退出");
                }
                if(entry != null && controlVersionEntry2.getVersion().equals(entry.getVersion())){
                    dto.setLevel(3);//匹配
                }else{
                    dto.setLevel(2);//不匹配
                }
                String name = StringUtils.isNotEmpty(ddto.getNickName())?ddto.getNickName():ddto.getUserName();
                dto.setUserName(name);
                dto.setDateTime(0l);
                dtos.add(dto);
            }else{
                ControlVersionDTO dto = new ControlVersionDTO();
                String name = StringUtils.isNotEmpty(ddto.getNickName())?ddto.getNickName():ddto.getUserName();
                dto.setUserName(name);
                dto.setVersion("暂无数据");
                dto.setLevel(2);
                dto.setCommunityId(communityId.toString());
                dto.setDateTime(0l);
                dto.setId("");
                dto.setType(1);
                dto.setUserId(ddto.getId());
                dtos.add(dto);
            }
        }
        if(entry!=null){
            map.put("dto",new ControlVersionDTO(entry));
        }else{
            ControlVersionDTO dto = new ControlVersionDTO();
            dto.setUserName("");
            dto.setVersion("暂无数据");
            dto.setLevel(1);
            dto.setCommunityId(communityId.toString());
            dto.setDateTime(0l);
            dto.setId("");
            dto.setType(2);
            dto.setUserId("");
            map.put("dto",dto);
        }
        map.put("list",dtos);
        return map;
    }

    //获取新班级管控版本
    public Map<String,Object> getNewCommunityVersionList(ObjectId communityId){
        Map<String,Object> map = new HashMap<String, Object>();
        List<ControlVersionDTO> dtos = new ArrayList<ControlVersionDTO>();
        List<ControlVersionEntry> entries = controlVersionDao.getCommunityVersionList(communityId);
        ControlVersionEntry entry = null;
        Map<String,ControlVersionEntry> cmap = new HashMap<String, ControlVersionEntry>();
        if(entries.size()>0){
            entry = entries.get(0);
        }
        List<String> objectIdList4 = newVersionBindService.getStudentIdListByCommunityId(communityId);
        List<ObjectId> objectIdList5 = new ArrayList<ObjectId>();
        for(String string : objectIdList4){
            objectIdList5.add(new ObjectId(string));
        }
        long current = System.currentTimeMillis()-7*24*60*60*1000;
        List<ControlVersionEntry> entries3 = controlVersionDao.getAllStudentVersionList(objectIdList5,current);
        for(ControlVersionEntry controlVersionEntry3 : entries3){
            ControlVersionEntry controlVersionEntry4 = cmap.get(controlVersionEntry3.getUserId().toString());
            if(controlVersionEntry4!=null){
                if(controlVersionEntry4.getDateTime()<controlVersionEntry3.getDateTime()){
                    cmap.put(controlVersionEntry3.getUserId().toString(),controlVersionEntry3);
                }
            }else{
                cmap.put(controlVersionEntry3.getUserId().toString(),controlVersionEntry3);
            }
        }
        List<UserDetailInfoDTO> unList = userService.findUserInfoByUserIds(objectIdList4);
        for(UserDetailInfoDTO ddto:unList){
            ControlVersionEntry controlVersionEntry2 = cmap.get(ddto.getId());
            if(controlVersionEntry2 !=null){
                ControlVersionDTO dto = new ControlVersionDTO(controlVersionEntry2);
                boolean flag = false;
                String cacheUserKey= CacheHandler.getUserKey(controlVersionEntry2.getUserId().toString());
                if(org.apache.commons.lang3.StringUtils.isNotEmpty(cacheUserKey)){
                    SessionValue sv = CacheHandler.getSessionValue(cacheUserKey);
                    if (null != sv && !sv.isEmpty()) {
                        flag = true;
                    }
                }
                if(flag){

                }else{
                    dto.setVersion("已退出");
                }
                if(entry != null && controlVersionEntry2.getVersion().equals(entry.getVersion())){
                    dto.setLevel(3);//匹配
                }else{
                    dto.setLevel(2);//不匹配
                }
                String name = StringUtils.isNotEmpty(ddto.getNickName())?ddto.getNickName():ddto.getUserName();
                dto.setUserName(name);
                dto.setDateTime(0l);
                dtos.add(dto);
            }else{
                ControlVersionDTO dto = new ControlVersionDTO();
                String name = StringUtils.isNotEmpty(ddto.getNickName())?ddto.getNickName():ddto.getUserName();
                dto.setUserName(name);
                dto.setVersion("暂无数据");
                dto.setLevel(2);
                dto.setCommunityId(communityId.toString());
                dto.setDateTime(0l);
                dto.setId("");
                dto.setType(1);
                dto.setUserId(ddto.getId());
                dtos.add(dto);
            }
        }
        if(entry!=null){
            map.put("dto",new ControlVersionDTO(entry));
        }else{
            ControlVersionDTO dto = new ControlVersionDTO();
            dto.setUserName("");
            dto.setVersion("暂无数据");
            dto.setLevel(1);
            dto.setCommunityId(communityId.toString());
            dto.setDateTime(0l);
            dto.setId("");
            dto.setType(2);
            dto.setUserId("");
            map.put("dto",dto);
        }
        map.put("list",dtos);
        return map;
    }

    //添加孩子社区管控版本
    public void addCommunityPersion(ObjectId userId,String version){
        ControlVersionEntry entry = controlVersionDao.getEntry(userId, 1);
        if(entry!=null){
            entry.setVersion(version);
            entry.setDateTime(new Date().getTime());
            controlVersionDao.addEntry(entry);
        }else{
           ControlVersionEntry controlVersionEntry = new ControlVersionEntry(null,userId,version,1,1);
            controlVersionDao.addEntry(controlVersionEntry);
        }
    }

    //添加社区发出版本（老师和家长通用）
    public void addControlVersion(ObjectId communityId,ObjectId userId,String version,int type){
        ControlVersionEntry entry = controlVersionDao.getEntry(communityId,userId, type);
        if(entry!=null){
            entry.setVersion(version);
            entry.setDateTime(new Date().getTime());
            controlVersionDao.addEntry(entry);
        }else{
            ControlVersionEntry controlVersionEntry = new ControlVersionEntry(communityId,userId,version,1,type);
            controlVersionDao.addEntry(controlVersionEntry);
        }
    }

    //查询学生最新的收到版本
    public ControlVersionDTO getStudentVersion(ObjectId userId){
        ControlVersionEntry entry = controlVersionDao.getEntry(userId, 1);
        if(entry!=null){
            return new ControlVersionDTO(entry);
        }else{
            ControlVersionDTO dto = new ControlVersionDTO();
            dto.setVersion("暂无数据");
            return dto;
        }

    }

    //查询社区最新的发出指令版本
    public ControlVersionDTO getSimpleCommunityVersion(ObjectId communityId,int type){
        ControlVersionEntry entry = controlVersionDao.getEntryByCommunityId(communityId, type);
        return new ControlVersionDTO(entry);
    }

    //查询家长对孩子最新的发出指令版本
    public ControlVersionDTO getSimpleUserVersion(ObjectId userId,ObjectId sonId){
        ControlVersionEntry entry = controlVersionDao.getEntryForParent(userId, sonId, 2);
        if(entry!=null){
            boolean flag = false;
            String cacheUserKey= CacheHandler.getUserKey(sonId.toString());
            if(org.apache.commons.lang3.StringUtils.isNotEmpty(cacheUserKey)){
                SessionValue sv = CacheHandler.getSessionValue(cacheUserKey);
                if (null != sv && !sv.isEmpty()) {
                    flag = true;
                }
            }
            if(flag){
                return new ControlVersionDTO(entry);
            }else{
                ControlVersionDTO dto = new ControlVersionDTO();
                dto.setVersion("已退出");
                return dto;
            }

        }
        ControlVersionDTO dto = new ControlVersionDTO();
        dto.setVersion("暂无数据");
        return dto;
    }

    public void deleteAppResultPersion(){
        controlAppResultDao.delEntryList();
    }


    public List<ControlAppResultDTO> getNewAppResultList(ObjectId sonId){
        List<ControlAppResultDTO> dtos = new ArrayList<ControlAppResultDTO>();
        NewVersionBindRelationEntry newEntry = newVersionBindRelationDao.getBindEntry(sonId);
        if(newEntry!=null){
            ObjectId parentId = newEntry.getMainUserId();
            long current = System.currentTimeMillis();
            String str = DateTimeUtils.getLongToStrTimeTwo(current).substring(0,11);
            long time = DateTimeUtils.getStrToLongTime(str, "yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(current));
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            if(hour<8){
                time = time - 24*60*60*1000;
            }
            ControlStudentResultEntry entry6 = controlStudentResultDao.getEntry(sonId, parentId, time);
            if(entry6!=null){
                List<ControlAppResultEntry> entries = controlAppResultDao.getLinNewNewEntryList(sonId, parentId, entry6.getNewAppTime());
                for(ControlAppResultEntry appResultEntry:entries) {
                    ControlAppResultDTO dto = new ControlAppResultDTO(appResultEntry);
                    dtos.add(dto);
                }
            }

        }
        return dtos;
    }

    //添加共享管控用户
    public void addControlShareUser(ObjectId userId,ObjectId sonId,String jiaId)throws  Exception{
        UserEntry userEntry = userService.findByGenerateCode(jiaId);
        if(userEntry==null){
            throw  new Exception("用户不存在，请重新输入家校美ID！");
        }
        ControlShareEntry controlShareEntry = new ControlShareEntry(userId,userEntry.getID(),sonId, Constant.ONE,new ArrayList<String>());
        controlShareDao.addEntry(controlShareEntry);
    }


    //添加共享管控用户
    public void deleteControlShareUser(ObjectId id){
        controlShareDao.deleteEntry(id);
    }

    //查询共享用户列表
    public  List<Map<String,Object>> selectControlShareUser(ObjectId userId,ObjectId sonId){
        List<ControlShareEntry> entries = controlShareDao.getEntryList(userId, sonId);
        List<Map<String,Object>> mapList = new ArrayList<Map<String, Object>>();
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        for(ControlShareEntry controlShareEntry : entries){
            userIds.add(controlShareEntry.getShareId());
        }
        Map<ObjectId,UserEntry> userEntryMap=userService.getUserEntryMap(userIds,Constant.FIELDS);
        for(ControlShareEntry controlShareEntry:entries){
            UserEntry userEntry= userEntryMap.get(controlShareEntry.getShareId());
            if(userEntry!=null){
                Map<String,Object>  map  = new HashMap<String, Object>();
                String name = StringUtils.isNotEmpty(userEntry.getNickName())?userEntry.getNickName():userEntry.getUserName();
                map.put("name",name);
                map.put("jiaId",userEntry.getGenerateUserCode());
                map.put("userId",userEntry.getID().toString());
                map.put("id",controlShareEntry.getID().toString());
                mapList.add(map);
            }
        }

        return mapList;
    }
}
