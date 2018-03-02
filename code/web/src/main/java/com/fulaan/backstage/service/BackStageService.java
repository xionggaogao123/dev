package com.fulaan.backstage.service;

import cn.jiguang.commom.utils.StringUtils;
import com.db.activity.FriendDao;
import com.db.appmarket.AppDetailDao;
import com.db.backstage.*;
import com.db.controlphone.*;
import com.db.fcommunity.*;
import com.db.jiaschool.HomeSchoolDao;
import com.db.jiaschool.SchoolAppDao;
import com.db.operation.AppCommentDao;
import com.db.operation.AppNoticeDao;
import com.db.operation.AppOperationDao;
import com.db.questionbook.QuestionAdditionDao;
import com.db.questionbook.QuestionBookDao;
import com.db.user.UserDao;
import com.easemob.server.comm.constant.MsgType;
import com.fulaan.appmarket.dto.AppDetailDTO;
import com.fulaan.backstage.dto.*;
import com.fulaan.controlphone.dto.ControlAppSystemDTO;
import com.fulaan.controlphone.dto.ControlPhoneDTO;
import com.fulaan.controlphone.dto.ControlSchoolTimeDTO;
import com.fulaan.controlphone.dto.ControlSetBackDTO;
import com.fulaan.fgroup.service.EmService;
import com.fulaan.indexpage.dto.IndexPageDTO;
import com.fulaan.jiaschool.dto.SchoolAppDTO;
import com.fulaan.picturetext.runnable.PictureRunNable;
import com.fulaan.user.service.UserService;
import com.fulaan.util.DownloadUtil;
import com.fulaan.util.imageInit;
import com.pojo.activity.FriendEntry;
import com.pojo.appmarket.AppDetailEntry;
import com.pojo.appnotice.AppNoticeEntry;
import com.pojo.backstage.*;
import com.pojo.controlphone.*;
import com.pojo.fcommunity.AttachmentEntry;
import com.pojo.fcommunity.CommunityDetailEntry;
import com.pojo.fcommunity.CommunityEntry;
import com.pojo.fcommunity.MemberEntry;
import com.pojo.indexPage.IndexPageEntry;
import com.pojo.jiaschool.HomeSchoolEntry;
import com.pojo.jiaschool.SchoolAppEntry;
import com.pojo.newVersionGrade.CommunityType;
import com.pojo.operation.AppCommentEntry;
import com.pojo.operation.AppOperationEntry;
import com.pojo.questionbook.QuestionAdditionEntry;
import com.pojo.questionbook.QuestionBookEntry;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import com.sys.utils.QiniuFileUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

/**
 * Created by James on 2017/11/18.
 */
@Service
public class BackStageService {

    @Autowired
    private UserService userService;

    private UnlawfulPictureTextDao unlawfulPictureTextDao = new UnlawfulPictureTextDao();

    private ControlSetBackDao controlSetBackDao = new ControlSetBackDao();

    private ControlSetTimeDao controlSetTimeDao = new ControlSetTimeDao();

    private ControlPhoneDao controlPhoneDao = new ControlPhoneDao();

    private ControlSchoolTimeDao controlSchoolTimeDao = new ControlSchoolTimeDao();

    private UserDao userDao = new UserDao();

    private CommunityDao communityDao =new CommunityDao();

    private AppNoticeDao appNoticeDao = new AppNoticeDao();

    private AppCommentDao appCommentDao = new AppCommentDao();

    private CommunityDetailDao communityDetailDao = new CommunityDetailDao();

    private AppOperationDao appOperationDao = new AppOperationDao();

    private QuestionBookDao questionBookDao = new QuestionBookDao();

    private QuestionAdditionDao questionAdditionDao = new QuestionAdditionDao();
    
    private TeacherApproveDao teacherApproveDao = new TeacherApproveDao();

    private JxmAppVersionDao jxmAppVersionDao = new JxmAppVersionDao();

    private UserRoleOfPathDao userRoleOfPathDao = new UserRoleOfPathDao();

    private UserLogResultDao userLogResultDao = new UserLogResultDao();

    private AppDetailDao appDetailDao = new AppDetailDao();

    private ControlAppSystemDao controlAppSystemDao = new ControlAppSystemDao();

    private LogMessageDao logMessageDao = new LogMessageDao();

    private SystemMessageDao systemMessageDao = new SystemMessageDao();

    private MemberDao memberDao = new MemberDao();

    private FriendDao friendDao = new FriendDao();

    private GroupDao groupDao = new GroupDao();

    private NewVersionCommunityBindDao  newVersionCommunityBindDao = new NewVersionCommunityBindDao();

    private SchoolAppDao schoolAppDao = new SchoolAppDao();

    private HomeSchoolDao homeSchoolDao = new HomeSchoolDao();



    private static String imageUrl = "http://7xiclj.com1.z0.glb.clouddn.com/5a1bdcfd27fddd15c8649dea.png";


    public static void main(String[] args){
//        int i = 1;
//        System.out.print(i);
        EmService emService = new EmService();
        List<String> targets = new ArrayList<String>();
        targets.add("58f6bea2de04cb5a4bc72d38");
        String userId="579961cbde04cb783df3074f";
        Map<String, String> ext = new HashMap<String, String>();
        Map<String, String> sendMessage = new HashMap<String, String>();
        sendMessage.put("type", MsgType.IMG);
        sendMessage.put("url", "https://a1.easemob.com/fulan/fulanmall/chatfiles/2b3ce640-0cb7-11e8-8a92-29b46c527a8a");
        sendMessage.put("filename","operationBook.jpg");
        sendMessage.put("secret","KzzmSgy3EeisbBEBikKn-2bhdi55QYWQdkgC8mYR_o3-LmTX");
//        Map<String,Integer> map =new HashMap<String, Integer>();
//        map.put("width",480);
//        map.put("height",720);
//        sendMessage.put("size",map);
        emService.sendTextMessage("users", targets, userId.toString(), ext, sendMessage);

        File file= new File("D:\\logo\\logo_1.png");
        Object obj=emService.uploadFile(file);
        if(obj instanceof Map){
            Map  map= (Map)obj;
            System.out.println(obj.toString());
        }else{
            System.out.println(obj.toString());
        }
        /*JxmAppVersionDao controlSetBackDao = new JxmAppVersionDao();*/
       /* ControlSetBackDao controlSetBackDao = new ControlSetBackDao();
        ControlSetBackEntry entry1 = new ControlSetBackEntry();
        entry1.setType(1);
        entry1.setBackTime(15);
        entry1.setAppTime(15);
        controlSetBackDao.addEntry(entry1);*/
      /*  JxmAppVersionDTO dto = new JxmAppVersionDTO();
        dto.setFileUrl("http://7xiclj.com1.z0.glb.clouddn.com/5a16432794b5ea7a4cbde78e.apk");
        dto.setName("cn.lt.appstore");
        dto.setVersion("4.3.0");*/
        //dto.setName("2011-11-23 17:04:00");
        //TeacherApproveDao teacherApproveDao1 = new TeacherApproveDao();
     /*   controlSetBackDao.addEntry(dto.buildAddEntry());*/
    }


    public void addBackTimeEntry(ObjectId userId,int time){
        ControlSetBackEntry entry = controlSetBackDao.getEntry();
        String id = "";
        if(null == entry){
            ControlSetBackEntry entry1 =new ControlSetBackEntry();
            entry1.setType(1);
            entry1.setBackTime(time);
            entry1.setAppTime(24 * 60);
            entry1.setIsRemove(0);
            id = controlSetBackDao.addEntry(entry1);
        }else{
            entry.setBackTime(time);
            controlSetBackDao.updEntry(entry);
            id= entry.getID().toString();
        }
        this.addLogMessage(id,"修改默认学生地图回调时间为"+time+"分钟",LogMessageType.backTime.getDes(),userId.toString());

    }
    public void addAppBackTimeEntry(ObjectId userId,int time){
        ControlSetBackEntry entry = controlSetBackDao.getEntry();
        String id = "";
        if(null == entry){
            ControlSetBackEntry entry1 = new ControlSetBackEntry();
            entry1.setType(1);
            entry1.setAppTime(time);
            entry1.setBackTime(24*60);
            entry1.setIsRemove(0);
            id = controlSetBackDao.addEntry(entry1);
        }else{
            entry.setAppTime(time);
            controlSetBackDao.updEntry(entry);
            id= entry.getID().toString();
        }
        this.addLogMessage(id,"修改默认学生应用回调时间"+time+"分钟",LogMessageType.backTime.getDes(),userId.toString());

    }

    public  List<ControlSetBackDTO> selectSetAppBackEntryList(ObjectId userId){
        List<ControlSetBackDTO> dtos = new ArrayList<ControlSetBackDTO>();
        ControlSetBackEntry setBackEntry = controlSetBackDao.getEntry();
        if(setBackEntry != null){
            dtos.add(new ControlSetBackDTO(setBackEntry));
        }
        return dtos;
    }


    public List<ControlSchoolTimeDTO> selectSchoolTime(){
        List<ControlSchoolTimeDTO> dtos = new ArrayList<ControlSchoolTimeDTO>();
        List<ControlSchoolTimeEntry> entries =  controlSchoolTimeDao.getAllEntryList();
        if(entries.size()>0){
            for(ControlSchoolTimeEntry entry : entries){
                dtos.add(new ControlSchoolTimeDTO(entry));
            }
        }
        return dtos;
    }
    public List<ControlSchoolTimeDTO> selectCommunityTime(ObjectId schoolId){
        List<ControlSchoolTimeDTO> dtos = new ArrayList<ControlSchoolTimeDTO>();
        List<ControlSchoolTimeEntry> entries =  controlSchoolTimeDao.getOneSchoolEntryList(schoolId);
        if(entries.size()>0){
            for(ControlSchoolTimeEntry entry : entries){
                dtos.add(new ControlSchoolTimeDTO(entry));
            }
        }
        return dtos;
    }
    public void addSetTimeListEntry(ObjectId userId,int time){
        long time2 = time * 60 * 1000;
        long hours2 = (time2 % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes2 = (time2 % (1000 * 60 * 60)) / (1000 * 60);
        String timeStr = "";
        if(hours2 != 0 ){
            timeStr = timeStr + hours2+"小时";
        }
        if(minutes2 != 0){
            timeStr = timeStr + minutes2+"分钟";
        }
        ControlSetTimeEntry controlSetTimeEntry = new ControlSetTimeEntry();
        controlSetTimeEntry.setName(timeStr);
        controlSetTimeEntry.setTime(time);
        controlSetTimeDao.addEntry(controlSetTimeEntry);
    }
    public void addPhoneEntry(ObjectId userId,String name, String phone){
        ControlPhoneEntry entry = controlPhoneDao.getEntry(phone);
        String id = "";
        if(null == entry){
            ControlPhoneDTO dto = new ControlPhoneDTO();
            dto.setName(name);
            dto.setPhone(phone);
            dto.setType(1);
            id = controlPhoneDao.addEntry(dto.buildAddEntry());
        }else{
            entry.setName(name);
            controlPhoneDao.updEntry(entry);
            id = entry.getID().toString();
        }
        this.addLogMessage(id,"添加系统常用电话："+phone,LogMessageType.systemPhone.getDes(),userId.toString());
    }

    public void delPhoneEntry(ObjectId userId,ObjectId id){
        ControlPhoneEntry entry = controlPhoneDao.getEntryById(id);
        if(null != entry){
            controlPhoneDao.delEntry(id);
            this.addLogMessage(id.toString(),"删除系统常用电话："+entry.getPhone(),LogMessageType.systemPhone.getDes(),userId.toString());
        }
    }

    public List<ControlPhoneDTO> selectPhoneEntryList(){
        List<ControlPhoneDTO> dtos = new ArrayList<ControlPhoneDTO>();
        List<ControlPhoneEntry> entries = controlPhoneDao.getEntryListByType();
        if(entries.size()>0){
            for(ControlPhoneEntry entry : entries){
                dtos.add(new ControlPhoneDTO(entry));
            }
        }
        return dtos;
    }
    public void addSchoolTime(ObjectId userId,String startTime,String endTime,int week){
        ControlSchoolTimeEntry entry = controlSchoolTimeDao.getEntry(week);
        String id = "";
        if(null==entry){
            ControlSchoolTimeDTO dto = new ControlSchoolTimeDTO();
            dto.setStartTime(startTime);
            dto.setEndTime(endTime);
            dto.setWeek(week);
            dto.setType(1);
            id = controlSchoolTimeDao.addEntry(dto.buildAddEntry());
        }else{
            entry.setStartTime(startTime);
            entry.setEndTime(endTime);
            controlSchoolTimeDao.updEntry(entry);
            id = entry.getID().toString();
        }
        this.addLogMessage(id.toString(),"添加常用管控默认上课时间："+startTime+"-"+endTime,LogMessageType.schoolTime.getDes(),userId.toString());
    }

    public void addCommunityTime(ObjectId userId,String startTime,String endTime,ObjectId schoolId,int week){
        HomeSchoolEntry homeSchoolEntry = homeSchoolDao.getEntryById(schoolId);
        if(homeSchoolEntry!=null){
            ControlSchoolTimeEntry entry = controlSchoolTimeDao.getcommunityEntry(week,schoolId);
            String id = "";
            if(null==entry){
                ControlSchoolTimeDTO dto = new ControlSchoolTimeDTO();
                dto.setStartTime(startTime);
                dto.setParentId(schoolId.toString());
                dto.setEndTime(endTime);
                dto.setWeek(week);
                dto.setType(1);
                ControlSchoolTimeEntry entry1 = dto.buildAddEntry();
                entry1.setIsRemove(Constant.TWO);
                id = controlSchoolTimeDao.addEntry(entry1);
            }else{
                entry.setStartTime(startTime);
                entry.setEndTime(endTime);
                controlSchoolTimeDao.updEntry(entry);
                id = entry.getID().toString();
            }

            this.addLogMessage(id.toString(),"添加学校: "+homeSchoolEntry.getName()+" 管控默认上课时间："+startTime+"-"+endTime,LogMessageType.communitTime.getDes(),userId.toString());
        }
    }

    public void delSchoolTime(ObjectId userId,ObjectId id){
        ControlSchoolTimeEntry entry = controlSchoolTimeDao.getEntryById(id);
        if(null != entry){
            controlSchoolTimeDao.delAppCommentEntry(id);
            this.addLogMessage(id.toString(),"删除管控默认上课时间："+ entry.getStartTime()+"-"+entry.getEndTime(),LogMessageType.schoolTime.getDes(),userId.toString());
        }

    }

    public void delCommunityTime(ObjectId userId,ObjectId id){
        ControlSchoolTimeEntry entry = controlSchoolTimeDao.getCommunityEntryById(id);
        if(null != entry){
            HomeSchoolEntry homeSchoolEntry = homeSchoolDao.getEntryById(entry.getParentId());
            if(homeSchoolEntry!=null){
                controlSchoolTimeDao.delAppCommentEntry(id);
                this.addLogMessage(id.toString(),"删除学校："+homeSchoolEntry.getName()+" 管控默认上课时间："+ entry.getStartTime()+"-"+entry.getEndTime(),LogMessageType.communitTime.getDes(),userId.toString());
            }

        }

    }
    public void addOtherSchoolTime(ObjectId userId,String startTime,String endTime,String dateTime){
        ControlSchoolTimeEntry entry = controlSchoolTimeDao.getOtherEntry(dateTime);
        String id = "";
        if(null==entry){
            ControlSchoolTimeDTO dto = new ControlSchoolTimeDTO();
            dto.setStartTime(startTime);
            dto.setEndTime(endTime);
            dto.setDataTime(dateTime);
            dto.setType(2);
            id = controlSchoolTimeDao.addEntry(dto.buildAddEntry());
        }else{
            entry.setStartTime(startTime);
            entry.setEndTime(endTime);
            controlSchoolTimeDao.updEntry(entry);
            id = entry.getID().toString();
        }

        this.addLogMessage(id.toString(),"添加特殊管控默认上课时间："+ startTime+"-"+endTime,LogMessageType.schoolTime.getDes(),userId.toString());
    }

    public void addOtherCommunityTime(ObjectId userId,String startTime,String endTime,ObjectId schoolId,String dateTime){
        HomeSchoolEntry homeSchoolEntry = homeSchoolDao.getEntryById(schoolId);
        if(homeSchoolEntry!=null){
            ControlSchoolTimeEntry entry = controlSchoolTimeDao.getOtherCommunityEntry(dateTime, schoolId);
            String id = "";
            if(null==entry){
                ControlSchoolTimeDTO dto = new ControlSchoolTimeDTO();
                dto.setStartTime(startTime);
                dto.setEndTime(endTime);
                dto.setParentId(schoolId.toString());
                dto.setDataTime(dateTime);
                dto.setType(2);
                ControlSchoolTimeEntry controlSchoolTimeEntry = dto.buildAddEntry();
                controlSchoolTimeEntry.setIsRemove(Constant.TWO);
                id = controlSchoolTimeDao.addEntry(controlSchoolTimeEntry);
            }else{
                entry.setStartTime(startTime);
                entry.setEndTime(endTime);
                controlSchoolTimeDao.updEntry(entry);
                id = entry.getID().toString();
            }

            this.addLogMessage(id.toString(),"添加学校："+homeSchoolEntry.getName()+" 特殊管控上课时间："+ startTime+"-"+endTime,LogMessageType.communitTime.getDes(),userId.toString());
        }

    }
    public void addDuringSchoolTime(ObjectId userId,String startTime,String endTime,String dateTime){
        ControlSchoolTimeEntry entry = controlSchoolTimeDao.getOtherEntry(dateTime);
        String id = "";
        if(null==entry){
            ControlSchoolTimeDTO dto = new ControlSchoolTimeDTO();
            dto.setStartTime(startTime);
            dto.setEndTime(endTime);
            dto.setDataTime(dateTime);
            dto.setType(3);
            id = controlSchoolTimeDao.addEntry(dto.buildAddEntry());
        }else{
            entry.setStartTime(startTime);
            entry.setEndTime(endTime);
            controlSchoolTimeDao.updEntry(entry);
            id = entry.getID().toString();
        }

        this.addLogMessage(id.toString(),"添加特殊管控默认上课时间："+ startTime+"-"+endTime,LogMessageType.schoolTime.getDes(),userId.toString());
    }

    public void addDuringCommunityTime(ObjectId userId,String startTime,String endTime,ObjectId schoolId,String dateTime){
        HomeSchoolEntry homeSchoolEntry = homeSchoolDao.getEntryById(schoolId);
        if(homeSchoolEntry!=null){
            ControlSchoolTimeEntry entry = controlSchoolTimeDao.getOtherCommunityEntry(dateTime, schoolId);
            String id = "";
            if(null==entry){
                ControlSchoolTimeDTO dto = new ControlSchoolTimeDTO();
                dto.setStartTime(startTime);
                dto.setEndTime(endTime);
                dto.setDataTime(dateTime);
                dto.setParentId(schoolId.toString());
                dto.setType(3);
                ControlSchoolTimeEntry controlSchoolTimeEntry = dto.buildAddEntry();
                controlSchoolTimeEntry.setIsRemove(Constant.TWO);
                id = controlSchoolTimeDao.addEntry(controlSchoolTimeEntry);
            }else{
                entry.setStartTime(startTime);
                entry.setEndTime(endTime);
                controlSchoolTimeDao.updEntry(entry);
                id = entry.getID().toString();
            }

            this.addLogMessage(id.toString(),"添加学校："+homeSchoolEntry.getName()+" 特殊管控学校上课时间："+ startTime+"-"+endTime,LogMessageType.communitTime.getDes(),userId.toString());
        }

    }
    //教师认证1未验证，2 验证通过 3 验证不通过
    public Map<String,Object> selectTeacherList(ObjectId userId,int type,String groupId,int page,int pageSize){
        String searchId = "";
        if(groupId != null && !groupId.equals("")){
            UserEntry userEntry = userDao.getJiaUserEntry(groupId);
            searchId = userEntry.getID().toString();
        }
        Map<String,Object> map = new HashMap<String, Object>();
        List<TeacherApproveEntry> entries = new ArrayList<TeacherApproveEntry>();
        List<ObjectId> oids = teacherApproveDao.selectContentObjectList();
        //oids.add(userId);
        List<MemberEntry> entries2 = memberDao.getMembersFromTeacher(oids,searchId,page, pageSize);
        int count = 0;
        List<ObjectId> objectIdList =new ArrayList<ObjectId>();
        int level = 1;
        if(type==0 || type==1){
            for(MemberEntry memberEntry : entries2){
                TeacherApproveEntry teaentry= new TeacherApproveEntry(memberEntry.getUserId(),memberEntry.getUserName(),memberEntry.getAvator(),"",memberEntry.getGroupId(),0l,1);
                entries.add(teaentry);
            }
            if(groupId !=null && !groupId.equals("")){
                count = entries2.size();
            }else{
                count = memberDao.getMembersFromTeacherCount(oids);
            }
            level =2;
        }else if(type==2){
            //memberDao.get
            entries = teacherApproveDao.selectContentList(searchId, type, page, pageSize);
            count = teacherApproveDao.getNumber(searchId, type);
        }else if(type==3){
            //memberDao.get
            entries = teacherApproveDao.selectContentList(searchId, type, page, pageSize);
            count = teacherApproveDao.getNumber(searchId, type);
        }
        List<TeacherApproveDTO> dtos = new ArrayList<TeacherApproveDTO>();
        if(entries.size()>0){
            Set<ObjectId> userIds=new HashSet<ObjectId>();
            for(TeacherApproveEntry entry:entries){
                userIds.add(entry.getUserId());
                objectIdList.add(entry.getApproveId());
            }
            Map<ObjectId,String> entries4 = communityDao.getMapCommunityEntriesByGroupIds(objectIdList);
            Map<ObjectId,UserEntry> userEntryMap=userService.getUserEntryMap(userIds, Constant.FIELDS);
            List<ObjectId> ids2 = new ArrayList<ObjectId>();
            ids2.addAll(userIds);
            Map<ObjectId,String> map5 = getOwnerCommunityList(ids2);
            for(TeacherApproveEntry entry : entries){
                TeacherApproveDTO dto=new TeacherApproveDTO(entry);
                UserEntry userEntry=userEntryMap.get(entry.getUserId());
                if(null!=userEntry){
                    dto.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()));
                    dto.setUid(userEntry.getGenerateUserCode());
                }
                if(entries4.get(entry.getApproveId())!=null){
                    dto.setCommunityName(entries4.get(entry.getApproveId()));
                    dtos.add(dto);
                }else if(map5.get(entry.getUserId())!= null && level==1){
                    dto.setCommunityName(map5.get(entry.getUserId()));
                    dtos.add(dto);
                }else{

                }
                //dto.setCommunityName();
            }
        }

        map.put("list",dtos);
        map.put("count",count);
        return map;
    }
    public Map<ObjectId,String> getOwnerCommunityList(List<ObjectId> uids){
        Map<ObjectId,String> map = new HashMap<ObjectId, String>();
        List<MemberEntry> entries2 = memberDao.getMembersFromTeacher2(uids);
        List<ObjectId> objectIds = new ArrayList<ObjectId>();
        for(MemberEntry entry : entries2){
            objectIds.add(entry.getGroupId());
        }
        Map<ObjectId,String> entries4 = communityDao.getMapCommunityEntriesByGroupIds(objectIds);
        for(MemberEntry entry : entries2){
            String str = map.get(entry.getUserId());
            if(str==null){
                map.put(entry.getUserId(),entries4.get(entry.getGroupId()));
            }else{
                String str2 = entries4.get(entry.getGroupId());
                if(str2 !=null){
                    map.put(entry.getUserId(),str + "，" + str2);
                }
            }

        }
        return map;
    }
    public void addTeacherList2(ObjectId userId,ObjectId id,int type){
        TeacherApproveEntry entry = teacherApproveDao.getEntry(id);
        //2验证通过，3 不通过
        if(entry != null){
            if(type==2){
                teacherApproveDao.updateEntry(id, type);
                this.addLogMessage(entry.getID().toString(),"通过老师验证："+entry.getName(),LogMessageType.teaValidate.getDes(),userId.toString());
            }else if (type==3){
                teacherApproveDao.updateEntry(id,type);
                this.addLogMessage(entry.getID().toString(),"不通过老师验证："+entry.getName(),LogMessageType.teaValidate.getDes(),userId.toString());
            }
        }else{
            if(type==2){
                TeacherApproveDTO dto = new TeacherApproveDTO();
                UserEntry userEntry = userService.findById(id);
                dto.setName(userEntry.getUserName());
                dto.setUserId(id.toString());
                dto.setApproveId(userId.toString());
                dto.setType(type);
                String oid = teacherApproveDao.addEntry(dto.buildAddEntry());
                this.addLogMessage(oid.toString(),"通过老师验证："+userEntry.getUserName(),LogMessageType.teaValidate.getDes(),userId.toString());
            }else if (type==3){
                TeacherApproveDTO dto = new TeacherApproveDTO();
                UserEntry userEntry = userService.findById(id);
                dto.setName(userEntry.getUserName());
                dto.setUserId(id.toString());
                dto.setApproveId(userId.toString());
                dto.setType(type);
                String oid = teacherApproveDao.addEntry(dto.buildAddEntry());
                this.addLogMessage(oid.toString(),"不通过老师验证："+userEntry.getUserName(),LogMessageType.teaValidate.getDes(),userId.toString());
            }
        }
    }
    public void addTeacherList(ObjectId userId,ObjectId id,int type){
        TeacherApproveEntry entry = teacherApproveDao.getEntry(id);
        UserEntry entry1 =  userDao.findByUserId(id);
        //2验证通过，3 不通过
        if(entry != null){
            if(type==2){
                try{
                    String oldUrl = "";
                    String newUrl = "";
                    if(entry.getOldAvatar() !=null && entry.getOldAvatar()!=""){
                        oldUrl = entry.getOldAvatar();
                        newUrl = entry.getNewAvatar();
                    }else{
                        oldUrl = AvatarUtils.getAvatar(entry1.getAvatar(),entry1.getRole(),entry1.getSex());
                        newUrl = oldUrl+"-headv1";
                    }
                    userDao.updateAvater(id,newUrl);
                    memberDao.updateAllAvatar(id, newUrl);
                    teacherApproveDao.updateEntry4(id, type, oldUrl, newUrl);
                }catch (Exception e){

                }
                this.addLogMessage(entry.getID().toString(), "通过老师验证："+entry1.getUserName(), LogMessageType.teaValidate.getDes(), userId.toString());
            }else if (type==3){
                try{
                    String oldUrl = entry.getOldAvatar();
                    String newUrl = entry.getNewAvatar();
                    userDao.updateAvater(id,oldUrl);
                    memberDao.updateAllAvatar(id, oldUrl);
                    teacherApproveDao.updateEntry4(id, type, oldUrl, newUrl);
                }catch (Exception e){

                }
                this.addLogMessage(entry.getID().toString(),"不通过老师验证："+entry1.getUserName(),LogMessageType.teaValidate.getDes(),userId.toString());
            }
        }else{
            if(type==2){
                try {
                    TeacherApproveDTO dto = new TeacherApproveDTO();
                    UserEntry userEntry = userService.findById(id);
                    dto.setName(userEntry.getUserName());
                    dto.setUserId(id.toString());
                    dto.setApproveId(userId.toString());
                    dto.setType(type);
                    String oldUrl = AvatarUtils.getAvatar(entry1.getAvatar(),entry1.getRole(),entry1.getSex());
                    String newUrl = oldUrl+"-headv1";
                    dto.setOldAvatar(oldUrl);
                    dto.setNewAvatar(newUrl);
                    //加大v
                    userDao.updateAvater(id,newUrl);
                    memberDao.updateAllAvatar(id, newUrl);
                    String oid = teacherApproveDao.addEntry(dto.buildAddEntry());
                    this.addLogMessage(oid.toString(),"通过老师验证："+entry1.getUserName(),LogMessageType.teaValidate.getDes(),userId.toString());
                }catch (Exception e){

                }
            }else if (type==3){
                try{
                    TeacherApproveDTO dto = new TeacherApproveDTO();
                    UserEntry userEntry = userService.findById(id);
                    dto.setName(userEntry.getUserName());
                    dto.setUserId(id.toString());
                    dto.setApproveId(userId.toString());
                    dto.setType(type);
                    String oldUrl = AvatarUtils.getAvatar(entry1.getAvatar(),entry1.getRole(),entry1.getSex());
                    String newUrl = oldUrl+"-headv1";
                    dto.setOldAvatar(oldUrl);
                    dto.setNewAvatar(newUrl);
                    //不管
                    String oid = teacherApproveDao.addEntry(dto.buildAddEntry());
                    this.addLogMessage(oid.toString(),"不通过老师验证："+entry1.getUserName(),LogMessageType.teaValidate.getDes(),userId.toString());
                }catch (Exception e){

                }

            }
        }
    }
    //通过验证
    public void addUserVIP(ObjectId uid){

    }

    //取消验证
    public void delUserVIP(ObjectId uid){

    }

    public String testGenerateImage(HttpServletRequest request,String url)throws Exception{
        String fileName = new ObjectId() + ".jpg";
        String parentPath = request.getServletContext().getRealPath("/static") + "/images/upload";
//           String path= Resources.getProperty("upload.file");
        DownloadUtil.downLoadFromUrl(url, fileName, parentPath);
        String filePath = parentPath + "/" + fileName;
        String logoImg = request.getServletContext().getRealPath("/static") + "/images/upload/ic_v3.png";
        String waterImage = imageInit.mergeWaterMark(filePath, logoImg,2,2);
        File file1 = new File(filePath);
        File file = new File(waterImage);
        try {
            String extensionName = waterImage.substring(waterImage.indexOf(".") + 1, waterImage.length());
            String fileKey = new ObjectId().toString() + Constant.POINT + extensionName;
            QiniuFileUtils.uploadFile(fileKey, new FileInputStream(file), QiniuFileUtils.TYPE_IMAGE);
            String qiuNiuPath = QiniuFileUtils.getPath(QiniuFileUtils.TYPE_IMAGE, fileKey);
            return qiuNiuPath;
        }finally {
            file.delete();
            file1.delete();
        }
    }

    //获得操作日志
    public Map<String,Object> getLogMessage(int page,int pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        List<LogMessageDTO> dtos = new ArrayList<LogMessageDTO>();
        List<LogMessageEntry> logs =  logMessageDao.selectContentList(page, pageSize);
        int count = logMessageDao.getNumber();
        List<ObjectId> userIds = new ArrayList<ObjectId>();
        for(LogMessageEntry entry : logs){
            dtos.add(new LogMessageDTO(entry));
            userIds.add(entry.getUserId());
        }
        Map<ObjectId,UserEntry> userEntryMap=userService.getUserEntryMap(userIds, Constant.FIELDS);
        for(LogMessageDTO dto : dtos){
            dto.setName(userEntryMap.get(new ObjectId(dto.getUserId())).getUserName());
        }
        map.put("list", dtos);
        map.put("count",count);
        return map;
    }

    public List<JxmAppVersionDTO> getAllAppVersion(){
        List<JxmAppVersionDTO> dtos = new ArrayList<JxmAppVersionDTO>();
        List<JxmAppVersionEntry> entries = jxmAppVersionDao.getIsNewObjectId();
        for(JxmAppVersionEntry entry : entries){
            dtos.add(new JxmAppVersionDTO(entry));
        }
        return dtos;
    }
   /* public static void main(String[] args){

    }
*/  //获取黑名单列表
    public List<AppDetailDTO> getBlackAppList(){
        List<AppDetailDTO> detailDTOs = new ArrayList<AppDetailDTO>();
        List<AppDetailEntry> detailEntries =  appDetailDao.getSimpleAppEntry();
        for(AppDetailEntry detailEntry : detailEntries){
            detailDTOs.add(new AppDetailDTO(detailEntry));
        }
        return detailDTOs;

    }

    //添加黑名单
    public void addBlackAppEntry(ObjectId userId,String name,String packageName){
        AppDetailEntry entry = appDetailDao.getEntryByApkPackageName(packageName);
        String id = "";
        if(null==entry){
            AppDetailDTO dto = new AppDetailDTO();
            dto.setAppName(name);
            dto.setAppPackageName(packageName);
            dto.setLogo("/static/images/sm_apk.png");
            dto.setIsControl(1);
            dto.setWhiteOrBlack(1);
            dto.setType(0);
            id = appDetailDao.saveEntry(dto.buildEntry(userId));
        }else{
            entry.setWhiteOrBlack(1);
            entry.setIsControl(1);
            appDetailDao.updEntry(entry);
            id= entry.getID().toString();
        }
        this.addLogMessage(id.toString(),"添加应用黑名单："+name+"，包名："+packageName,LogMessageType.black.getDes(),userId.toString());
    }
    //移除黑名单
    public void delBlackAppEntry(ObjectId userId,ObjectId id){
        AppDetailEntry entry = appDetailDao.findEntryById(id);
        if(null != entry){
            appDetailDao.updateEntry(id);
            this.addLogMessage(id.toString(),"移除应用黑名单："+entry.getAppName()+"，包名："+entry.getAppPackageName(),LogMessageType.black.getDes(),userId.toString());
        }

    }
    //添加为学校推送
    public void addSchoolAppEntry(ObjectId userId,ObjectId appId,ObjectId schoolId){
        AppDetailEntry entry = appDetailDao.findEntryById(appId);
        if(entry != null){
            SchoolAppEntry entry1 = schoolAppDao.getEntryById(schoolId);
            if(entry1 == null){
                SchoolAppDTO dto = new SchoolAppDTO();
                List<String> stringList = new ArrayList<String>();
                stringList.add(appId.toString());
                dto.setAppIdList(stringList);
                dto.setSchoolId(schoolId.toString());
                String id = schoolAppDao.addEntry(dto.buildAddEntry()).toString();
                this.addLogMessage(id.toString(),"添加学校推送应用："+entry.getAppName()+"，包名："+entry.getAppPackageName(),LogMessageType.schoolApp.getDes(),userId.toString());
            }else{
                List<ObjectId> ob = entry1.getAppIdList();
                if(!ob.contains(appId)){
                    ob.add(appId);
                    entry1.setAppIdList(ob);
                    schoolAppDao.updEntry(entry1);
                    this.addLogMessage(entry1.getID().toString(),"添加学校推送应用："+entry.getAppName()+"，包名："+entry.getAppPackageName(),LogMessageType.schoolApp.getDes(),userId.toString());
                }
            }
        }
    }
    //添加为系统推送
    public void addSystemAppEntry(ObjectId userId,ObjectId appId){
        AppDetailEntry entry = appDetailDao.findEntryById(appId);
        if(entry != null){
            ControlAppSystemEntry entry1 = controlAppSystemDao.getEntry();
            if(entry1 == null){
                ControlAppSystemDTO dto = new ControlAppSystemDTO();
                List<String> stringList = new ArrayList<String>();
                stringList.add(appId.toString());
                dto.setAppIdList(stringList);
                String id = controlAppSystemDao.addEntry(dto.buildAddEntry());
                this.addLogMessage(id.toString(),"添加系统推送应用："+entry.getAppName()+"，包名："+entry.getAppPackageName(),LogMessageType.fulan.getDes(),userId.toString());
            }else{
                List<ObjectId> ob = entry1.getAppIdList();
                if(!ob.contains(appId)){
                    ob.add(appId);
                    entry1.setAppIdList(ob);
                    controlAppSystemDao.updEntry(entry1);
                    this.addLogMessage(entry1.getID().toString(),"添加系统推送应用："+entry.getAppName()+"，包名："+entry.getAppPackageName(),LogMessageType.fulan.getDes(),userId.toString());
                }
            }
        }
        addAllUserAppList(appId);
    }
    public void addAllUserAppList(ObjectId appId){
        PictureRunNable.addApp(appId);
    }
    //删除系统推送
    public void delSystemAppEntry(ObjectId userId,ObjectId appId){
        AppDetailEntry entry = appDetailDao.findEntryById(appId);
        if(entry != null){
            ControlAppSystemEntry entry1 = controlAppSystemDao.getEntry();
            if(entry1 == null){

            }else{
                List<ObjectId> ob = entry1.getAppIdList();
                if(ob.contains(appId)){
                    ob.remove(appId);
                    entry1.setAppIdList(ob);
                    controlAppSystemDao.updEntry(entry1);
                    this.addLogMessage(entry1.getID().toString(),"移除系统推送应用："+entry.getAppName()+"，包名："+entry.getAppPackageName(),LogMessageType.fulan.getDes(),userId.toString());
                }
            }
        }


    }
    //查询系统推送
    public List<AppDetailDTO> selectSystemAppEntry(){
        List<AppDetailEntry> appDetailEntries = new ArrayList<AppDetailEntry>();
        ControlAppSystemEntry entry1 = controlAppSystemDao.getEntry();
        if(null != entry1){
            List<ObjectId> oids = entry1.getAppIdList();
            if(oids!= null && oids.size()>0){
                List<AppDetailEntry> appDetailEntries2 = appDetailDao.getEntriesByIds(oids);
                appDetailEntries.addAll(appDetailEntries2);
            }
        }
        List<AppDetailDTO> dtos = new ArrayList<AppDetailDTO>();
        for(AppDetailEntry detailEntry : appDetailEntries){
            dtos.add(new AppDetailDTO(detailEntry));
        }
        return dtos;
    }

    //添加图片显示认证
    public void addYellowPicture(ObjectId userId,String imageUrl,int ename,ObjectId contactId){
        UnlawfulPictureTextDTO dto = new UnlawfulPictureTextDTO();
        dto.setType(1);
        dto.setContent(imageUrl);
        dto.setUserId(userId.toString());
        dto.setFunction(ename);
        dto.setIsCheck(0);
        dto.setContactId(contactId.toString());
        unlawfulPictureTextDao.addEntry(dto.buildAddEntry());
    }

    //列表
    public Map<String,Object> selectContentList(int isCheck,String id,int page,int pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        List<UnlawfulPictureTextEntry> entries = unlawfulPictureTextDao.selectContentList(isCheck,id,page,pageSize);
        int count = unlawfulPictureTextDao.getNumber(isCheck,id);
        List<UnlawfulPictureTextDTO> dtos = new ArrayList<UnlawfulPictureTextDTO>();
        List<String> uids = new ArrayList<String>();
        for(UnlawfulPictureTextEntry entry : entries){
            dtos.add(new UnlawfulPictureTextDTO(entry));
            uids.add(entry.getUserId().toString());
        }
        List<UserDetailInfoDTO> udtos = userService.findUserInfoByUserIds(uids);
        Map<String,UserDetailInfoDTO> map2 = new HashMap<String, UserDetailInfoDTO>();
        if(udtos != null && udtos.size()>0){
            for(UserDetailInfoDTO dto4 : udtos){
                map2.put(dto4.getId(),dto4);
            }
        }
        for(UnlawfulPictureTextDTO dto5 : dtos){
            UserDetailInfoDTO dto9 = map2.get(dto5.getUserId());
            if(dto9 != null){
                String name = StringUtils.isNotEmpty(dto9.getNickName())?dto9.getNickName():dto9.getUserName();
                dto5.setUserName(name);
                dto5.setAvatar(dto9.getImgUrl());
            }
            dto5.setFunctionName(PictureType.getDes(dto5.getFunction()));
        }
        map.put("list",dtos);
        map.put("count",count);
        return map;
    }
    //通过
    public void passContentEntry(ObjectId userId,ObjectId id){
        UnlawfulPictureTextEntry entry = unlawfulPictureTextDao.getEntryById(id);
        if(entry != null){
            unlawfulPictureTextDao.passContentEntry(id);
            this.addLogMessage(id.toString(),"通过了图片",LogMessageType.content.getDes(),userId.toString());
        }
    }


    //删除
    public void deleteContentEntry(ObjectId userId,ObjectId id){
        unlawfulPictureTextDao.deleteContentEntry(id);
        UnlawfulPictureTextEntry entry = unlawfulPictureTextDao.getEntryById(id);
        if(entry != null) {
            ObjectId cid = entry.getContactId();
            String url = entry.getContent();
            if(entry.getFunction()== PictureType.userUrl.getType()){//用户头像
                //替换用户头像
                userDao.updateAvater(cid,imageUrl);
            }else if(entry.getFunction()== PictureType.communityLogo.getType()){//社区logo
                //替换社区logo
                communityDao.updateCommunityLogo(cid,imageUrl);
            }else if(entry.getFunction()== PictureType.noticeImage.getType()){//通知
                AppNoticeEntry entry1 = appNoticeDao.getAppNoticeEntry(cid);
                List<AttachmentEntry> alist = entry1.getImageList();
                if(alist != null && alist.size()>0){
                    for(AttachmentEntry entry2 : alist){
                        if(entry2.getUrl()!=null && entry2.getUrl().contains(url)){
                            entry2.setUrl(imageUrl);
                        }
                    }
                }
                appNoticeDao.updEntry(entry1);
            }else if(entry.getFunction()== PictureType.operationImage.getType()){//作业
                AppCommentEntry entry1 = appCommentDao.getEntry(cid);
                List<AttachmentEntry> alist = entry1.getImageList();
                if(alist != null && alist.size()>0){
                    for(AttachmentEntry entry2 : alist){
                        if(entry2.getUrl()!=null && entry2.getUrl().contains(url)){
                            entry2.setUrl(imageUrl);
                        }
                    }
                }
                appCommentDao.updEntry(entry1);
            }else if(entry.getFunction()== PictureType.activeImage.getType()){//活动报名
                CommunityDetailEntry entry1 = communityDetailDao.findByObjectId(cid);
                List<AttachmentEntry> alist = entry1.getImageList();
                if(alist != null && alist.size()>0){
                    for(AttachmentEntry entry2 : alist){
                        if(entry2.getUrl()!=null && entry2.getUrl().contains(url)){
                            entry2.setUrl(imageUrl);
                        }
                    }
                }
                communityDetailDao.updEntry(entry1);
            }else if(entry.getFunction()== PictureType.studyImage.getType()){//学习用品
                CommunityDetailEntry entry1 = communityDetailDao.findByObjectId(cid);
                List<AttachmentEntry> alist = entry1.getImageList();
                if(alist != null && alist.size()>0){
                    for(AttachmentEntry entry2 : alist){
                        if(entry2.getUrl()!=null && entry2.getUrl().contains(url)){
                            entry2.setUrl(imageUrl);
                        }
                    }
                }
                communityDetailDao.updEntry(entry1);

            }else if(entry.getFunction()== PictureType.happyImage.getType()){//兴趣小组
                CommunityDetailEntry entry1 = communityDetailDao.findByObjectId(cid);
                List<AttachmentEntry> alist = entry1.getImageList();
                if(alist != null && alist.size()>0){
                    for(AttachmentEntry entry2 : alist){
                        if(entry2.getUrl()!=null && entry2.getUrl().contains(url)){
                            entry2.setUrl(imageUrl);
                        }
                    }
                }
                communityDetailDao.updEntry(entry1);
            }else if(entry.getFunction()== PictureType.commentImage.getType()){//评论
                AppOperationEntry entry1 = appOperationDao.getEntry(cid);
                entry1.setFileUrl(imageUrl);
                appOperationDao.updEntry(entry1);
            }else if(entry.getFunction()== PictureType.wrongImage.getType()){//错题本
                QuestionBookEntry entry1 = questionBookDao.getEntryById(cid);
                List<String> slist = entry1.getImageList();
                List<String> nList = new ArrayList<String>();
                if(slist != null && slist.size()>0){
                    for(String str : slist){
                        if(str!=null && str.contains(url)){
                            nList.add(imageUrl);
                        }else{
                            nList.add(str);
                        }
                    }
                }
                entry1.setImageList(nList);
                questionBookDao.updateEntry(entry1);

            }else if(entry.getFunction()== PictureType.answerImage.getType()){//错题解析
                QuestionAdditionEntry entry1 = questionAdditionDao.getEntryByObjectId(cid);
                List<String> slist = entry1.getAnswerList();
                List<String> nList = new ArrayList<String>();
                if(slist != null && slist.size()>0){
                    for(String str : slist){
                        if(str!=null && str.contains(url)){
                            nList.add(imageUrl);
                        }else{
                            nList.add(str);
                        }
                    }
                }
                entry1.setAnswerList(nList);
                questionAdditionDao.updateEntry(entry1);
            }
            this.addLogMessage(id.toString(),"删除图片",LogMessageType.content.getDes(),userId.toString());
        }

    }

    public UserRoleOfPathDTO getPathByRole(int role)throws Exception{
        UserRoleOfPathEntry pathEntry=userRoleOfPathDao.getEntryByRole(role);
        if(pathEntry!=null){
            return new UserRoleOfPathDTO(pathEntry);
        }else {
            throw new Exception("查找不到role对应的权限列表");
        }
    }

    public List<UserRoleOfPathDTO> getUserRoleOfPathDTO(){
        List<UserRoleOfPathDTO> pathDTOs=new ArrayList<UserRoleOfPathDTO>();
        List<UserRoleOfPathEntry> entries=userRoleOfPathDao.getRoleEntries();
        for(UserRoleOfPathEntry pathEntry:entries){
            pathDTOs.add(new UserRoleOfPathDTO(pathEntry));
        }
        return pathDTOs;
    }


    public void saveUserRole(ObjectId uid,String userId,int role){
        UserLogResultEntry resultEntry=userLogResultDao.getEntryByUserId(new ObjectId(userId));
        UserEntry userEntry = userDao.findByUserId(new ObjectId(userId));
        if(null!=resultEntry){
            resultEntry.setRole(role);
            this.addLogMessage(resultEntry.getID().toString(),"修改了"+userEntry.getUserName()+"的权限为"+role,LogMessageType.manageUserRole.getDes(),uid.toString());
            userLogResultDao.saveUserLogEntry(resultEntry);
        }else{
            UserLogResultEntry entry=new UserLogResultEntry(new ObjectId(userId),role);
            userLogResultDao.saveUserLogEntry(entry);
            this.addLogMessage(entry.getID().toString(),"新增了管理员用户"+userEntry.getUserName()+"权限为"+role,LogMessageType.manageUserRole.getDes(),uid.toString());
        }
    }


    public List<UserLogResultDTO> getAllUserRoles(){
        List<UserLogResultDTO> resultDTOs = new ArrayList<UserLogResultDTO>();
        List<UserLogResultEntry> entries = userLogResultDao.getEntries();
        List<ObjectId> userIds=new ArrayList<ObjectId>();
        for(UserLogResultEntry entry:entries){
            userIds.add(entry.getUserId());
        }
        Map<ObjectId,UserEntry> userEntryMap=userService.getUserEntryMap(userIds,Constant.FIELDS);
        for(UserLogResultEntry entry:entries){
            UserLogResultDTO resultDTO=new UserLogResultDTO(entry);
            UserEntry userEntry=userEntryMap.get(entry.getUserId());
            if(null!=userEntry){
                resultDTO.setUserName(org.apache.commons.lang3.StringUtils.isNotEmpty(userEntry.getNickName())?
                        userEntry.getNickName():userEntry.getUserName());
            }
            resultDTOs.add(resultDTO);
        }
        return resultDTOs;
    }


    public void saveUserRoleOfPath(ObjectId userId,String pathStr,
                                   int role){
        UserRoleOfPathEntry entry=userRoleOfPathDao.getEntryByRole(role);
        String[] paths=pathStr.split(",");
        List<String> pathList=new ArrayList<String>();
        for(String item:paths){
            pathList.add(item);
        }
        UserRoleOfPathEntry pathEntry=new UserRoleOfPathEntry(pathList,role);
        if(null!=entry){
            pathEntry.setID(entry.getID());
        }
        String id = userRoleOfPathDao.saveUserRoleOfPath(pathEntry);
        this.addLogMessage(id.toString(),"添加权限管理："+pathStr,LogMessageType.userRole.getDes(),userId.toString());
    }

    //非敏感日志操作
    public void addLogMessage(String contactId,String content,String function,String userId){
        LogMessageDTO dto = new LogMessageDTO();
        dto.setType(1);
        dto.setContactId(contactId);
        dto.setContent(content);
        dto.setFunction(function);
        dto.setUserId(userId);
        logMessageDao.addEntry(dto.buildAddEntry());
    }
    //添加系统推送2
    public String addSystemMessage(String sourceId,String fileUrl,String name,String content,String title,String userId){
        SystemMessageDTO dto = new SystemMessageDTO();
        dto.setContent(content);
        dto.setType(2);
        dto.setTitle(title);
        dto.setSourceName(name);
        dto.setSourceId(sourceId);
        dto.setSourceType(1);//社区id
        dto.setFileUrl(fileUrl);
        dto.setName("");
        dto.setAvatar("");
        String id = systemMessageDao.addEntry(dto.buildAddEntry());
        return id;
    }
    public void addIndexPageEntry(String sourceId,String fileUrl,String name,String content,String title,String userId){
        IndexPageDTO dto1 = new IndexPageDTO();
        dto1.setType(CommunityType.appNotice.getType());
        dto1.setUserId(userId.toString());
        dto1.setCommunityId(userId.toString());
       // objectIdList.add(new ObjectId(communityDTO.getCommunityId()));
       // dto1.setContactId(oid.toString());
        IndexPageEntry entry = dto1.buildAddEntry();
       // indexPageDao.addEntry(entry);
    }

    public void setAutoChildFriends(final String[] uIds,final List<ObjectId> communityIds){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ObjectId> allIds = new ArrayList<ObjectId>();
                for(String userId:uIds){
                    allIds.add(new ObjectId(userId));
                }
                for(ObjectId communityId:communityIds) {
                    List<ObjectId> userIds = newVersionCommunityBindDao.getStudentListByCommunityId(communityId);
                    Set<ObjectId> userSet = new HashSet<ObjectId>();
                    userSet.addAll(userIds);
                    userSet.addAll(allIds);
                    if (allIds.size() > 0) {
                        for (ObjectId itemId : allIds) {
                            set(itemId, new ArrayList<ObjectId>(userSet));
                        }
                    }
                }
            }
        }).start();
    }

    public void setChildAutoFriends(final String[] uIds,final ObjectId communityId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ObjectId> allIds = new ArrayList<ObjectId>();
                for(String userId:uIds){
                    allIds.add(new ObjectId(userId));
                }
                List<ObjectId> userIds = newVersionCommunityBindDao.getStudentListByCommunityId(communityId);
                Set<ObjectId> userSet = new HashSet<ObjectId>();
                userSet.addAll(userIds);
                userSet.addAll(allIds);
                if (allIds.size() > 0) {
                    for (ObjectId itemId : allIds) {
                        set(itemId,new ArrayList<ObjectId>(userSet));
                    }
                }
            }
        }).start();
    }

    public void set(ObjectId itemId,List<ObjectId> allIds){
        List<ObjectId> uIds = new ArrayList<ObjectId>();
        uIds.addAll(allIds);
        setFriendEntry(itemId, uIds);
        setOpposites(itemId, uIds);
    }

    public void setChildAutoFriends(final ObjectId userId,final ObjectId communityId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<ObjectId> userIds = newVersionCommunityBindDao.getStudentListByCommunityId(communityId);
                if (userIds.size() > 0) {
                    List<ObjectId> uIds = new ArrayList<ObjectId>();
                    uIds.addAll(userIds);
                    setFriendEntry(userId, uIds);
                    setOpposites(userId, uIds);
                }
            }
        }).start();
    }


    public void setAutoFriends(final ObjectId userId,final ObjectId groupId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean  cFlag=true;
                int pageSize=200;
                int page = 1;
                while (cFlag) {
                    List<ObjectId> members = memberDao.getPageMembers(groupId, page, pageSize);
                    if (members.size() > 0) {
                        List<ObjectId> uIds = new ArrayList<ObjectId>();
                        uIds.addAll(members);
                        setFriendEntry(userId,uIds);
                        setOpposites(userId,uIds);
                    } else {
                        cFlag = false;
                    }
                    page++;
                }
            }
        }).start();

    }


    public void setSingleFriend(ObjectId userId,ObjectId friendId){
        if (friendDao.recordIsExist(userId)) {
            FriendEntry friendEntry = friendDao.get(userId);
            if (null != friendEntry&&!friendDao.isFriend(userId,friendId)) {
                friendDao.addOneFriend(userId, friendId);
            }
        } else {
            List<ObjectId> friendIds = new ArrayList<ObjectId>();
            friendIds.add(friendId);
            friendIds.remove(userId);
            if(friendIds.size()>0) {
                friendDao.addFriendEntry(userId, friendIds);
            }
        }
    }


    public void setFriendEntry(ObjectId userId,List<ObjectId> uIds){
        if(uIds.size()>0) {
            if (friendDao.recordIsExist(userId)) {
                FriendEntry friendEntry = friendDao.get(userId);
                if (null != friendEntry) {
                    List<ObjectId> friendIds = friendEntry.getFriendIds();
                    Set<ObjectId> set = new HashSet<ObjectId>();
                    set.addAll(friendIds);
                    set.addAll(uIds);
                    set.remove(userId);
                    friendEntry.setFriendIds(new ArrayList<ObjectId>(set));
                    friendDao.saveEntry(friendEntry);
                }
            } else {
                List<ObjectId> friendIds = new ArrayList<ObjectId>();
                friendIds.addAll(uIds);
                friendIds.remove(userId);
                friendDao.addFriendEntry(userId, friendIds);
            }
        }
    }

    public void setOpposites(ObjectId userId,List<ObjectId> uIds){
        for(ObjectId opposite:uIds){
            if (friendDao.recordIsExist(opposite)) {
                FriendEntry friendEntry = friendDao.get(opposite);
                if (null != friendEntry) {
                    List<ObjectId> friendIds = friendEntry.getFriendIds();
                    Set<ObjectId> set = new HashSet<ObjectId>();
                    set.addAll(friendIds);
                    set.add(userId);
                    set.remove(opposite);
                    friendEntry.setFriendIds(new ArrayList<ObjectId>(set));
                    friendDao.saveEntry(friendEntry);
                }
            }else{
                List<ObjectId> friendIds = new ArrayList<ObjectId>();
                friendIds.add(userId);
                friendIds.remove(opposite);
                friendDao.addFriendEntry(opposite, friendIds);
            }
        }
    }


    public void recordEntries(ObjectId groupId,int pageSize){
        boolean  cFlag=true;
        int cPage = 1;
        while (cFlag) {
            List<ObjectId> members = memberDao.getPageMembers(groupId, cPage, pageSize);
            if (members.size() > 0) {
                List<ObjectId> uIds = new ArrayList<ObjectId>();
                uIds.addAll(members);
                for (ObjectId userId : members) {
                    setFriendEntry(userId,uIds);
                }
            } else {
                cFlag = false;
            }
            cPage++;
        }
    }

    public void recordEntries2(final ObjectId groupId,final String[] objectIds,final int pageSize){
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean  cFlag=true;
                int cPage = 1;
                while (cFlag) {
                    List<ObjectId> members = memberDao.getPageMembers(groupId, cPage, pageSize);
                    if (members.size() > 0) {
                        List<ObjectId> uIds = new ArrayList<ObjectId>();
                        uIds.addAll(members);
                        for (String userId : objectIds) {
                            setFriendEntry(new ObjectId(userId),uIds);
                        }
                    } else {
                        cFlag = false;
                    }
                    cPage++;
                }
            }
        }).start();

    }


    public void setAutoCommunityFriends(){
        int pageSize=200;
        int page=1;
        boolean flag=true;
        while(flag){
            List<CommunityEntry> communityEntries=communityDao.getCommunities(page,pageSize);
            if(communityEntries.size()>0){
                for(CommunityEntry communityEntry:communityEntries){
                    ObjectId groupId=communityEntry.getGroupId();
                    if(!("复兰社区").equals(communityEntry.getCommunityName())){
                        recordEntries(groupId,pageSize);
                    }
                }
            }else{
                flag=false;
            }
            page++;
        }
    }

    public void delBindPhone(String name){
        List<UserEntry> entry  = userDao.getUserEntryListFromDelPhone("13818940676");
        ///List<UserEntry> entry2  = userDao.getUserEntryListFromDelPhone("13788951487");
        List<UserEntry> entry3 = new ArrayList<UserEntry>();
        entry3.addAll(entry);
        //entry3.addAll(entry2);
        List<ObjectId> oids = new ArrayList<ObjectId>();
        if(entry3.size()<25){
            for(UserEntry entry1 : entry3){
                oids.add(entry1.getID());
            }
            if(oids.size()>0){
                userDao.updateHuanXinFromPhone(oids);
            }
        }
        List<UserEntry> entry6  = userDao.searchUsers("13818940676", 1, 1);
        List<UserEntry> entry7  = userDao.searchUsers(name, 1, 10);
        if(entry6.size()==1 && entry7.size() ==0){
            for(UserEntry entry1 : entry6){
                userDao.updateHuanXinFromName(entry1.getID(),name);
            }
        }
    }

}
