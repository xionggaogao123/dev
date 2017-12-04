package com.fulaan.backstage.service;

import cn.jiguang.commom.utils.StringUtils;
import com.db.appmarket.AppDetailDao;
import com.db.backstage.*;
import com.db.controlphone.*;
import com.db.fcommunity.CommunityDao;
import com.db.fcommunity.CommunityDetailDao;
import com.db.operation.AppCommentDao;
import com.db.operation.AppNoticeDao;
import com.db.operation.AppOperationDao;
import com.db.questionbook.QuestionAdditionDao;
import com.db.questionbook.QuestionBookDao;
import com.db.user.UserDao;
import com.fulaan.appmarket.dto.AppDetailDTO;
import com.fulaan.backstage.dto.*;
import com.fulaan.controlphone.dto.ControlAppSystemDTO;
import com.fulaan.controlphone.dto.ControlPhoneDTO;
import com.fulaan.controlphone.dto.ControlSchoolTimeDTO;
import com.fulaan.controlphone.dto.ControlSetBackDTO;
import com.fulaan.user.service.UserService;
import com.pojo.appmarket.AppDetailEntry;
import com.pojo.appnotice.AppNoticeEntry;
import com.pojo.backstage.*;
import com.pojo.controlphone.*;
import com.pojo.fcommunity.AttachmentEntry;
import com.pojo.fcommunity.CommunityDetailEntry;
import com.pojo.operation.AppCommentEntry;
import com.pojo.operation.AppOperationEntry;
import com.pojo.questionbook.QuestionAdditionEntry;
import com.pojo.questionbook.QuestionBookEntry;
import com.pojo.user.UserDetailInfoDTO;
import com.pojo.user.UserEntry;
import com.sys.constants.Constant;
import com.sys.utils.AvatarUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private static String imageUrl = "http://7xiclj.com1.z0.glb.clouddn.com/5a1bdcfd27fddd15c8649dea.png";


    public static void main(String[] args){
        JxmAppVersionDao controlSetBackDao = new JxmAppVersionDao();
       /* ControlSetBackDao controlSetBackDao = new ControlSetBackDao();
        ControlSetBackEntry entry1 = new ControlSetBackEntry();
        entry1.setType(1);
        entry1.setBackTime(15);
        entry1.setAppTime(15);
        controlSetBackDao.addEntry(entry1);*/
        JxmAppVersionDTO dto = new JxmAppVersionDTO();
        dto.setFileUrl("http://7xiclj.com1.z0.glb.clouddn.com/5a16432794b5ea7a4cbde78e.apk");
        dto.setName("cn.lt.appstore");
        dto.setVersion("4.3.0");
        //dto.setName("2011-11-23 17:04:00");
        //TeacherApproveDao teacherApproveDao1 = new TeacherApproveDao();
        controlSetBackDao.addEntry(dto.buildAddEntry());
    }


    public void addBackTimeEntry(ObjectId userId,int time){
        ControlSetBackEntry entry = controlSetBackDao.getEntry();
        if(null == entry){
            ControlSetBackEntry entry1 =new ControlSetBackEntry();
            entry1.setType(1);
            entry1.setBackTime(time);
            entry1.setAppTime(24 * 60);
            entry1.setIsRemove(0);
            controlSetBackDao.addEntry(entry1);
        }else{
            entry.setBackTime(time);
            controlSetBackDao.updEntry(entry);
        }

    }
    public void addAppBackTimeEntry(ObjectId userId,int time){
        ControlSetBackEntry entry = controlSetBackDao.getEntry();
        if(null == entry){
            ControlSetBackEntry entry1 = new ControlSetBackEntry();
            entry1.setType(1);
            entry1.setAppTime(time);
            entry1.setBackTime(24*60);
            entry1.setIsRemove(0);
            controlSetBackDao.addEntry(entry1);
        }else{
            entry.setAppTime(time);
            controlSetBackDao.updEntry(entry);
        }

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
    public void addPhoneEntry(String name, String phone){
        ControlPhoneEntry entry = controlPhoneDao.getEntry(phone);
        if(null == entry){
            ControlPhoneDTO dto = new ControlPhoneDTO();
            dto.setName(name);
            dto.setPhone(phone);
            dto.setType(1);
            controlPhoneDao.addEntry(dto.buildAddEntry());
        }else{
            entry.setName(name);
            controlPhoneDao.updEntry(entry);
        }

    }

    public void delPhoneEntry(ObjectId id){
        controlPhoneDao.delEntry(id);
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
    public void addSchoolTime(String startTime,String endTime,int week){
        ControlSchoolTimeEntry entry = controlSchoolTimeDao.getEntry(week);
        if(null==entry){
            ControlSchoolTimeDTO dto = new ControlSchoolTimeDTO();
            dto.setStartTime(startTime);
            dto.setEndTime(endTime);
            dto.setWeek(week);
            dto.setType(1);
            controlSchoolTimeDao.addEntry(dto.buildAddEntry());
        }else{
            entry.setStartTime(startTime);
            entry.setEndTime(endTime);
            controlSchoolTimeDao.updEntry(entry);
        }
    }

    public void delSchoolTime(ObjectId id){
        controlSchoolTimeDao.delAppCommentEntry(id);
    }
    public void addOtherSchoolTime(String startTime,String endTime,String dateTime){
        ControlSchoolTimeEntry entry = controlSchoolTimeDao.getOtherEntry(dateTime);
        if(null==entry){
            ControlSchoolTimeDTO dto = new ControlSchoolTimeDTO();
            dto.setStartTime(startTime);
            dto.setEndTime(endTime);
            dto.setDataTime(dateTime);
            dto.setType(2);
            controlSchoolTimeDao.addEntry(dto.buildAddEntry());
        }else{
            entry.setStartTime(startTime);
            entry.setEndTime(endTime);
            controlSchoolTimeDao.updEntry(entry);
        }
    }
    //教师认证
    public Map<String,Object> selectTeacherList(ObjectId userId,int type,String searchId,int page,int pageSize){
        Map<String,Object> map = new HashMap<String, Object>();
        List<TeacherApproveEntry> entries = teacherApproveDao.selectContentList(searchId, type, page, pageSize);
        int count = teacherApproveDao.getNumber(searchId, type);
        List<TeacherApproveDTO> dtos = new ArrayList<TeacherApproveDTO>();
        if(entries.size()>0){
            Set<ObjectId> userIds=new HashSet<ObjectId>();
            for(TeacherApproveEntry entry:entries){
                userIds.add(entry.getUserId());
            }
            Map<ObjectId,UserEntry> userEntryMap=userService.getUserEntryMap(userIds, Constant.FIELDS);
            for(TeacherApproveEntry entry : entries){
                TeacherApproveDTO dto=new TeacherApproveDTO(entry);
                UserEntry userEntry=userEntryMap.get(entry.getUserId());
                if(null!=userEntry){
                    dto.setAvatar(AvatarUtils.getAvatar(userEntry.getAvatar(),userEntry.getRole(),userEntry.getSex()));
                }
                dtos.add(dto);
            }
        }

        map.put("list",dtos);
        map.put("count",count);
        return map;
    }
    public void addTeacherList(ObjectId id,int type){
        teacherApproveDao.updateEntry(id,type);
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
        if(null==entry){
            AppDetailDTO dto = new AppDetailDTO();
            dto.setAppName(name);
            dto.setAppPackageName(packageName);
            dto.setLogo("/static/images/sm_apk.png");
            dto.setIsControl(1);
            dto.setWhiteOrBlack(1);
            dto.setType(0);
            appDetailDao.saveAppDetailEntry(dto.buildEntry(userId));
        }else{
            entry.setWhiteOrBlack(1);
            entry.setIsControl(1);
            appDetailDao.updEntry(entry);
        }
    }
    //移除黑名单
    public void delBlackAppEntry(ObjectId userId,ObjectId id){
        appDetailDao.updateEntry(id);
    }
    //添加为系统推送
    public void addSystemAppEntry(ObjectId appId){
        AppDetailEntry entry = appDetailDao.findEntryById(appId);
        if(entry != null){
            ControlAppSystemEntry entry1 = controlAppSystemDao.getEntry();
            if(entry1 == null){
                ControlAppSystemDTO dto = new ControlAppSystemDTO();
                List<String> stringList = new ArrayList<String>();
                stringList.add(appId.toString());
                controlAppSystemDao.addEntry(dto.buildAddEntry());
            }else{
                List<ObjectId> ob = entry1.getAppIdList();
                if(!ob.contains(appId)){
                    ob.add(appId);
                    entry1.setAppIdList(ob);
                    controlAppSystemDao.updEntry(entry1);
                }
            }
        }


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
    public void passContentEntry(ObjectId id){
        unlawfulPictureTextDao.passContentEntry(id);
    }


    //删除
    public void deleteContentEntry(ObjectId id){
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


    public void saveUserRoleOfPath(String pathStr,
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
        userRoleOfPathDao.saveUserRoleOfPath(pathEntry);
    }



}
